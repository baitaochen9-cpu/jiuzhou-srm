package nc.jdbc.framework;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.exception.ExceptionFactory;
import nc.jdbc.framework.exception.UnKnownException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.mapping.AttributeMapping;
import nc.jdbc.framework.mapping.BeanMapping;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.mapping.MappingMetaManager;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanMappingListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.util.DBConsts;
import nc.jdbc.framework.util.DBUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.SuperVO;

/**
 * User: 贺扬<br>
 * Date: 2005-6-24 Time: 10:56:48<br>
 * 对象持久化管理器
 */
public class JdbcPersistenceManager extends PersistenceManager {
	// 数据库会话
	JdbcSession session;

	// 数据源名称
	String dataSource = null;

	private DatabaseMetaData dbmd = null;

	private static Map<String, ColCache> colCacheMap = new ConcurrentHashMap<String, ColCache>();

	class ColCache {
		private Map<String, Map<String, Integer>> typeCache = new ConcurrentHashMap<String, Map<String, Integer>>();
		private Map<String, Map<String, Integer>> sizeCache = new ConcurrentHashMap<String, Map<String, Integer>>();;
	}

	/**
	 * 无参数构造函数
	 * 
	 * @throws DbException
	 */
	protected JdbcPersistenceManager() throws DbException {
		init();
	}

	/**
	 * 有参数构造函数
	 * 
	 * @param dataSource
	 *            数据源名称
	 * @throws DbException
	 *             如果获得连接发生错误则抛出异常
	 */
	protected JdbcPersistenceManager(String dataSource) throws DbException {
		this.dataSource = dataSource;
		init();
	}

	protected JdbcPersistenceManager(JdbcSession session) {
		session.setMaxRows(maxRows);
		this.session = session;
	}

	/**
	 * 得到JdbcSession
	 * 
	 * @return 返回JdbcSession
	 */
	public JdbcSession getJdbcSession() {
		return session;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (dbmd != null)
			dbmd = null;
		if (session != null) {
			session.closeAll();
			session = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.jdbc.framework.ee#insertWithPK(nc.vo.pub.SuperVO)
	 */
	public String insertWithPK(final SuperVO vo) throws DbException {
		String pk[] = insertWithPK(new SuperVO[] { vo });
		return pk[0];
	}

	/**
	 * 把一个值对象插入到数据库中
	 * 
	 * @param vo
	 *            值对象
	 * @throws DbException
	 *             如果插入过程中发生错误则抛出异常
	 */
	public String insert(final SuperVO vo) throws DbException {
		String pk[] = insert(new SuperVO[] { vo });
		return pk[0];
	}

	/**
	 * 把一个值对象集合插入到数据库中
	 * 
	 * @param vos
	 *            值对象集合
	 * @throws DbException
	 *             如果插入过程中发生错误则抛出异常
	 */
	public String[] insertWithPK(final List vos) throws DbException {
		return insertWithPK((SuperVO[]) vos.toArray(new SuperVO[] {}));
	}

	/**
	 * 把一个值对象集合插入到数据库中
	 * 
	 * @param vos
	 *            值对象集合
	 * @throws DbException
	 *             如果插入过程中发生错误则抛出异常
	 */
	public String[] insert(final List vos) throws DbException {

		return insert((SuperVO[]) vos.toArray(new SuperVO[] {}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.jdbc.framework.ee#insertWithPK(nc.vo.pub.SuperVO[])
	 */
	public String[] insertWithPK(final SuperVO vos[]) throws DbException {
		return insert(vos, true);

	}

	/**
	 * 把一个值对象数组插入到数据库中
	 * 
	 * @param vos
	 *            值对象数据
	 * @throws DAOException
	 *             如果插入过程中发生错误则抛出异常
	 */
	public String[] insert(final SuperVO vos[]) throws DbException {
		return insert(vos, false);
	}

	private String[] preparePK(final SuperVO vos[], boolean withPK) {
		String corpPk = SQLHelper.getCorpPk();
		if (withPK) {
			String[] pks = new String[vos.length];
			int[] idx = new int[vos.length];
			int length = 0;
			for (int i = 0; i < vos.length; i++) {
				if (vos[i] == null) {
					continue;
				} else {
					String thePK = vos[i].getPrimaryKey();
					if (thePK == null || thePK.trim().length() == 0) {
						idx[length++] = i;
					} else {
						pks[i] = thePK;
					}
				}
			}

			if (length > 0) {
				String[] npks = new SequenceGenerator(dataSource).generate(
						corpPk, length);
				for (int i = 0; i < length; i++) {
					vos[idx[i]].setPrimaryKey(npks[i]);
					pks[idx[i]] = npks[i];
				}
			}
			return pks;

		} else {
			String[] pks = new SequenceGenerator(dataSource).generate(corpPk,
					vos.length);
			for (int i = 0; i < vos.length; i++) {
				if (vos[i] != null) {
					vos[i].setPrimaryKey(pks[i]);
				} else {
					pks[i] = null;
				}
			}
			return pks;
		}
	}

	@SuppressWarnings("unchecked")
	protected String[] insert(final SuperVO vos[], boolean withPK)
			throws DbException {
		isNull(vos);
		if (vos.length == 0) {
			return new String[0];
		}
		String[] pks = null;
		try {
			String tableName = vos[0].getTableName();

			Map<String, Integer> types = getColmnTypes(tableName);
			Map<String, Integer> sizes = getColmnSize(tableName);

			String names[] = getValidNames(vos[0], types);

			String sql = SQLHelper.getInsertSQL(tableName, names);

			pks = preparePK(vos, withPK);

			if (vos.length == 1) {
				SQLParameter parameter = getSQLParam(vos[0], names, types,
						sizes);
				session.executeUpdate(sql, parameter);
			} else {
				SQLParameter[] parameters = new SQLParameter[vos.length];
				for (int i = 0; i < vos.length; i++) {
					if (vos[i] == null)
						continue;
					parameters[i] = getSQLParam(vos[i], names, types, sizes);

				}
				session.addBatch(sql, parameters);
				session.executeBatch();
			}

		} finally {

		}
		return pks;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.jdbc.framework.ee#insertObjectWithPK(java.lang.Object,
	 * nc.jdbc.framework.mapping.IMappingMeta)
	 */
	public String insertObjectWithPK(final Object vo, IMappingMeta meta)
			throws DbException {
		return insertObjectWithPK(new Object[] { vo }, meta)[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.jdbc.framework.ee#insertObjectWithPK(java.lang.Object[],
	 * nc.jdbc.framework.mapping.IMappingMeta)
	 */
	public String[] insertObjectWithPK(final Object vo[], IMappingMeta meta)
			throws DbException {
		return insertObject(vo, meta, true);
	}

	public String insertObject(final Object vo, IMappingMeta meta)
			throws DbException {
		return insertObject(new Object[] { vo }, meta)[0];
	}

	/**
	 * 把一个值对象数组插入到数据库中
	 * 
	 * @param vo
	 *            值对象数据
	 * @throws DAOException
	 *             如果插入过程中发生错误则抛出异常
	 */
	public String[] insertObject(final Object vo[], IMappingMeta meta)
			throws DbException {
		return insertObject(vo, meta, false);
	}

	protected String[] insertObject(final Object vo[], IMappingMeta meta,
			boolean withPK) throws DbException {
		isNull(vo);
		if (vo.length == 0)
			return new String[0];
		// 如果是VO类型，则属性可以没有Getter,Setter方法
		if (vo[0] instanceof SuperVO) {
			SuperVO[] svos = new SuperVO[vo.length];
			System.arraycopy(vo, 0, svos, 0, vo.length);
			return insert(svos, meta, withPK);
		}
		String[] pk;
		AttributeMapping map = MappingMetaManager.getMapingMeta(meta);
		// 得到表名
		String tableName = meta.getTableName();
		// 得到公司主键
		String corpPk = SQLHelper.getCorpPk();
		pk = new SequenceGenerator(dataSource).generate(corpPk, vo.length);
		Map<String, Integer> types = getColmnTypes(tableName);
		Map<String, Integer> sizes = getColmnSize(tableName);
		// 得到插入的SQL语句
		// String sql = SQLHelper.getInsertSQL(tableName, names);
		// 循环插入VO数组
		for (int i = 0; i < vo.length; i++) {
			if (vo[i] == null)
				continue;
			String beanPkName = map.getAttributeName(meta.getPrimaryKey())
					.toLowerCase();
			if (withPK) {// if vo has pk
				String thePK = (String) BeanHelper.getProperty(vo[i],
						beanPkName);
				// BeanHelper.setProperty(vo[i], beanPkName, pk[i]);
				if (thePK == null || thePK.trim().length() == 0) {
					BeanHelper.setProperty(vo[i], beanPkName, pk[i]);
				} else {
					pk[i] = thePK;
				}
			} else {
				BeanHelper.setProperty(vo[i], beanPkName, pk[i]);
			}
			BeanMapping mapping = new BeanMapping(vo[i], meta);
			if (types != null)
				mapping.setType(types);
			if (sizes != null)
				mapping.setSizes(sizes);
			SQLParameter parameter = mapping.getInsertParamter();
			session.addBatch(mapping.getInsertSQL(), parameter);
		}
		session.executeBatch();
		return pk;
	}

	/**
	 * 更新一个在数据库中已经存在值对象
	 * 
	 * @param vo
	 * @throws DAOException
	 */
	public int update(final SuperVO vo) throws DbException {
		if (vo == null) {
			throw new IllegalArgumentException("vo parameter is null");
		}
		return update(new SuperVO[] { vo }, null);
	}

	public int update(final List vos) throws DbException {
		return update((SuperVO[]) vos.toArray(new SuperVO[] {}), null);

	}

	public int update(final SuperVO[] vo) throws DbException {
		return update(vo, null);

	}

	public int update(final SuperVO[] vo, String[] fieldNames)
			throws DbException {
		return update(vo, fieldNames, null, null);
	}

	@Override
	public int update(final SuperVO[] vo, String[] fieldNames,
			String whereClause, SQLParameter param) throws DbException {
		isNull(vo);
		if (vo.length == 0)
			return 0;
		int row = 0;

		// session.setAddTimeStamp(false);
		// 得到表名
		String tableName = vo[0].getTableName();
		String pkName = vo[0].getPKFieldName();
		// 得到版本
		// UFDateTime ts = new UFDateTime(new SystemTsGenerator().generateTS());
		String[] names;
		Map<String, Integer> types = getColmnTypes(tableName);
		Map<String, Integer> sizes = getColmnSize(tableName);
		if (fieldNames != null) {
			names = fieldNames; // 指定更新字段
		} else { // 得到插入字段类型的列表
			// 得到合法的字段列表
			names = getUpdateValidNames(vo[0], types, pkName);
		}
		// 得到插入的SQL语句
		String sql = SQLHelper.getUpdateSQL(tableName, names, pkName);
		if (vo.length == 1) {
			SQLParameter parameter = getSQLParam(vo[0], names, types, sizes);
			parameter.addParam(vo[0].getPrimaryKey());
			if (whereClause == null)
				row = session.executeUpdate(sql, parameter);
			else {
				addParameter(parameter, param);
				row = session.executeUpdate(sql + " and " + whereClause,
						parameter);
			}
		} else {
			for (int i = 0; i < vo.length; i++) {
				if (vo[i] == null)
					continue;
				// vo[i].setAttributeValue("ts", ts);
				SQLParameter parameter = getSQLParam(vo[i], names, types, sizes);
				parameter.addParam(vo[i].getPrimaryKey());
				if (whereClause == null)
					session.addBatch(sql, parameter);
				else {
					addParameter(parameter, param);
					session.addBatch(sql + " and " + whereClause, parameter);
				}
			}
			row = session.executeBatch();
		}
		return row;
	}

	public int updateObject(final Object vo, IMappingMeta meta)
			throws DbException {
		return updateObject(new Object[] { vo }, meta);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#updateObject(java.lang.Object,
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String)
	 */
	public int updateObject(final Object vo, IMappingMeta meta,
			String whereClause) throws DbException {
		return updateObject(new Object[] { vo }, meta, whereClause);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#updateObject(java.lang.Object[],
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String)
	 */
	public int updateObject(final Object[] vo, IMappingMeta meta,
			String whereClause) throws DbException {
		return updateObject(vo, meta, whereClause, null);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#updateObject(java.lang.Object[],
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String)
	 */
	public int updateObject(final Object[] vo, IMappingMeta meta,
			String whereClause, SQLParameter param) throws DbException {

		isNull(vo);
		if (vo.length == 0)
			return 0;

		// 如果是VO类型，则属性可以没有Getter,Setter方法
		if (vo[0] instanceof SuperVO) {
			SuperVO[] svos = new SuperVO[vo.length];
			System.arraycopy(vo, 0, svos, 0, vo.length);
			return update(svos, meta, whereClause, param);
		}
		// 得到表名
		String tableName = meta.getTableName();
		Map<String, Integer> types = getColmnTypes(tableName);
		Map<String, Integer> sizes = getColmnSize(tableName);
		if (vo.length == 1) {
			if (vo[0] == null)
				return -1;
			BeanMapping mapping = new BeanMapping(vo[0], meta);
			if (types != null) {
				mapping.setType(types);
			}
			if (sizes != null) {
				mapping.setSizes(sizes);
			}
			SQLParameter parameter = mapping.getUpdateParamter();
			if (whereClause == null) {
				if (mapping.isNullPK())
					return -1;
				return session.executeUpdate(mapping.getUpdateSQL(), parameter);
			} else {
				// 合并参数
				addParameter(parameter, param);
				if (mapping.isNullPK())
					return session.executeUpdate(mapping.getUpdateSQL()
							+ " WHERE " + whereClause, parameter);
				else
					return session.executeUpdate(mapping.getUpdateSQL()
							+ " AND " + whereClause, parameter);
			}
		}
		for (int i = 0; i < vo.length; i++) {
			if (vo[i] == null)
				continue;
			BeanMapping mapping = new BeanMapping(vo[i], meta);
			if (types != null)
				mapping.setType(types);
			SQLParameter parameter = mapping.getUpdateParamter();
			if (whereClause == null) {
				if (mapping.isNullPK())
					return -1;
				session.addBatch(mapping.getUpdateSQL(), parameter);
			} else {
				// 合并参数
				addParameter(parameter, param);
				if (mapping.isNullPK())
					session.addBatch(mapping.getUpdateSQL() + " WHERE "
							+ whereClause, parameter);
				else
					session.addBatch(mapping.getUpdateSQL() + " AND "
							+ whereClause, parameter);
			}
		}
		return session.executeBatch();
	}

	private void addParameter(SQLParameter parameter, SQLParameter addParams) {
		if (addParams != null)
			for (int i = 0; i < addParams.getCountParams(); i++) {
				parameter.addParam(addParams.get(i));
			}
	}

	/**
	 * 
	 */
	public int updateObject(final Object[] vo, IMappingMeta meta)
			throws DbException {
		return updateObject(vo, meta, null);
	}

	public int delete(final List vos) throws DbException {
		isNull(vos);
		return delete((SuperVO[]) vos.toArray(new SuperVO[] {}));
	}

	public int delete(final SuperVO vo) throws DbException {
		isNull(vo);
		return delete(new SuperVO[] { vo });
	}

	public int delete(final SuperVO vo[]) throws DbException {
		isNull(vo);
		if (vo.length == 0)
			return 0;
		// 得到表名
		String sql = SQLHelper.getDeleteByPKSQL(vo[0].getTableName(),
				vo[0].getPKFieldName());

		for (int i = 0; i < vo.length; i++) {
			if (vo[i] == null)
				continue;
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(vo[i].getPrimaryKey());
			session.addBatch(sql, parameter);
		}
		return session.executeBatch();
	}

	public void deleteObject(final Object vo, IMappingMeta meta)
			throws DbException {
		deleteObject(new Object[] { vo }, meta);
	}

	public void deleteObject(final Object vos[], IMappingMeta meta)
			throws DbException {
		isNull(vos);
		if (vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
			if (vos[i] == null)
				continue;
			BeanMapping mapping = new BeanMapping(vos[i], meta);
			SQLParameter parameter = mapping.getDeleteParamter();
			session.addBatch(mapping.getDeleteSQL(), parameter);
		}
		session.executeBatch();

	}

	public void deleteObject(final Object vos[], IMappingMeta meta,
			boolean dealNull) throws DbException {
		isNull(vos);
		if (vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
			if (vos[i] == null)
				continue;
			BeanMapping mapping = new BeanMapping(vos[i], meta, dealNull);
			SQLParameter parameter = mapping.getDeleteParamter();
			session.addBatch(mapping.getDeleteSQL(), parameter);
		}
		session.executeBatch();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * nc.jdbc.framework.ee#deleteByPK(nc.jdbc.framework.mapping.IMappingMeta,
	 * java.lang.String)
	 */
	public int deleteByPK(IMappingMeta meta, String pk) throws DbException {
		return deleteByPKs(meta, new String[] { pk });
	}

	/**
	 * 
	 * @param meta
	 * @param pks
	 * @return
	 * @throws DbException
	 * 
	 *             modified by cch
	 */
	public int deleteByPKs(IMappingMeta meta, String[] pks) throws DbException {
		String sql = "DELETE FROM " + meta.getTableName() + " WHERE "
				+ meta.getPrimaryKey() + "=?";
		for (int i = 0; i < pks.length; i++) {
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(pks[i]);
			session.addBatch(sql, parameter);
		}
		return session.executeBatch();
	}

	/**
	 * 
	 */
	public int deleteByPK(Class className, String pk) throws DbException {
		return deleteByPKs(className, new String[] { pk });
	}

	/**
	 * 
	 */
	public int deleteByPKs(Class className, String[] pks) throws DbException {
		SuperVO supervo = initSuperVOClass(className);
		String sql = "DELETE FROM " + supervo.getTableName() + " WHERE "
				+ supervo.getPKFieldName() + "=?";
		for (int i = 0; i < pks.length; i++) {
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(pks[i]);
			session.addBatch(sql, parameter);
		}
		return session.executeBatch();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * nc.jdbc.framework.ee#deleteByClause(nc.jdbc.framework.mapping.IMappingMeta
	 * , java.lang.String)
	 */
	public int deleteByClause(IMappingMeta meta, String wherestr)
			throws DbException {
		return deleteByClause(meta, wherestr, null);
	}

	/**
	 * @param className
	 * @param wherestr
	 * @return
	 * @throws DbException
	 */
	public int deleteByClause(Class className, String wherestr)
			throws DbException {
		return deleteByClause(className, wherestr, null);

	}

	public int deleteByClause(Class className, String wherestr,
			SQLParameter params) throws DbException {
		SuperVO supervo = initSuperVOClass(className);
		String sql = new StringBuffer().append("DELETE FROM ")
				.append(supervo.getTableName()).toString();
		if (wherestr != null) {
			wherestr = wherestr.trim();
			if (wherestr.length() > 0) {
				if (wherestr.toLowerCase().startsWith("WHERE"))
					wherestr = wherestr.substring(5);
				if (wherestr.length() > 0)
					sql = sql + " WHERE " + wherestr;
			}
		}
		if (params == null)
			return session.executeUpdate(sql);
		else
			return session.executeUpdate(sql, params);

	}

	public Collection retrieveByCorp(Class c, String pkCorp) throws DbException {
		return retrieveByCorp(c, pkCorp, null);
	}

	public Collection retrieveByCorp(Class c, String pkCorp,
			String[] selectedFields) throws DbException {

		if (pkCorp.equals("0001") || pkCorp.equals("@@@@")) {
			SQLParameter param = new SQLParameter();
			param.addParam("0001");
			param.addParam("@@@@");
			return retrieveByClause(c, "pk_corp=? or pk_corp=?",
					selectedFields, param);
		} else {
			SQLParameter param = new SQLParameter();
			param.addParam(pkCorp);
			return retrieveByClause(c, "pk_corp=?", selectedFields, param);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#retrieveByCorp(java.lang.Class,
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String)
	 */
	public Collection retrieveByCorp(Class c, IMappingMeta meta, String pkCorp)
			throws DbException {
		return retrieveByCorp(c, meta, pkCorp, null);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#retrieveByCorp(java.lang.Class,
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String,
	 * java.lang.String[])
	 */
	public Collection retrieveByCorp(Class c, IMappingMeta meta, String pkCorp,
			String[] selectedFields) throws DbException {
		if (pkCorp.equals("0001") || pkCorp.equals("@@@@")) {
			SQLParameter param = new SQLParameter();
			param.addParam("0001");
			param.addParam("@@@@");
			return retrieveByClause(c, meta, "pk_corp=? or pk_corp=?",
					selectedFields, param);
		} else {
			SQLParameter param = new SQLParameter();
			param.addParam(pkCorp);
			return retrieveByClause(c, meta, "pk_corp=?", selectedFields, param);
		}
	}

	/**
	 * 
	 */
	public Object retrieveByPK(Class className, String pk) throws DbException {
		return retrieveByPK(className, pk, null);
	}

	/**
	 * 
	 * 
	 */
	public Object retrieveByPK(Class className, String pk,
			String[] selectedFields) throws DbException {
		SuperVO vo = initSuperVOClass(className);
		if (pk == null)
			throw new IllegalArgumentException("pk is null");
		SQLParameter param = new SQLParameter();
		param.addParam(pk.trim());
		List results = (List) retrieveByClause(className, vo.getPKFieldName()
				+ "=?", selectedFields, param);
		if (results.size() >= 1)
			return results.get(0);
		return null;

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#retrieveByPK(java.lang.Class,
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String)
	 */
	public Object retrieveByPK(Class className, IMappingMeta meta, String pk)
			throws DbException {
		return retrieveByPK(className, meta, pk, null);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.jdbc.framework.ee#retrieveByPK(java.lang.Class,
	 * nc.jdbc.framework.mapping.IMappingMeta, java.lang.String,
	 * java.lang.String[])
	 */
	public Object retrieveByPK(Class className, IMappingMeta meta, String pk,
			String[] selectedFields) throws DbException {
		if (pk == null)
			throw new IllegalArgumentException("pk is null");
		SQLParameter param = new SQLParameter();
		param.addParam(pk.trim());
		List results = (List) retrieveByClause(className, meta,
				meta.getPrimaryKey() + "=?", selectedFields, param);
		if (results.size() >= 1)
			return results.get(0);
		return null;
	}

	/**
	 * 
	 */
	public Collection retrieve(SuperVO vo, boolean isAnd) throws DbException {
		return retrieve(vo, isAnd, null);

	}

	public Collection retrieve(Object vo, IMappingMeta meta) throws DbException {
		isNull(vo);
		BeanMapping mapping = new BeanMapping(vo, meta);
		// 得到插入的SQL语句
		String sql = mapping.getSelectwithParamSQL();
		SQLParameter param = mapping.getSelectParameter();
		// session.setReadOnly(true);
		return (Collection) session.executeQuery(sql, param,
				new BeanMappingListProcessor(vo.getClass(), meta));
	}

	public Collection retrieve(SuperVO vo, boolean isAnd, String[] fields)
			throws DbException {
		return (Collection) retrieve(vo, isAnd, fields, new BeanListProcessor(
				vo.getClass()));
	}

	public Collection retrieve(SuperVO vo, boolean isAnd, String[] fields,
			String[] orderbyFields) throws DbException {
		isNull(vo);
		String tableName = vo.getTableName();
		// 得到插入字段类型的列表
		Map types = getColmnTypes(tableName);
		// 得到合法的字段列表
		String names[] = getNotNullValidNames(vo, types);
		// 得到插入的SQL语句
		String sql = SQLHelper.getSelectSQL(tableName, names, isAnd, fields);

		sql = appendOrderBy(sql, orderbyFields);

		SQLParameter param = getSQLParam(vo, names);
		// session.setReadOnly(true);
		return (Collection) session.executeQuery(sql, param,
				new BeanListProcessor(vo.getClass()));
	}

	private String appendOrderBy(String sql, String[] orderBys) {
		if (sql == null) {
			throw new RuntimeException("sql is null");
		}

		if (orderBys == null || orderBys.length == 0) {
			return sql;
		}

		StringBuffer orderClause = new StringBuffer(" ORDER BY ");

		int len = orderClause.length();

		for (String s : orderBys) {
			if (s != null) {
				orderClause.append(s).append(',');
			}
		}

		if (orderClause.length() > len) {
			orderClause.setLength(orderClause.length() - 1);

			return sql + orderClause;
		} else {
			return sql;
		}

	}

	public Object retrieve(SuperVO vo, boolean isAnd, String[] fields,
			ResultSetProcessor processor) throws DbException {
		isNull(vo);
		String tableName = vo.getTableName();
		// 得到插入字段类型的列表
		Map types = getColmnTypes(tableName);
		// 得到合法的字段列表
		String names[] = getNotNullValidNames(vo, types);
		// 得到插入的SQL语句
		String sql = SQLHelper.getSelectSQL(tableName, names, isAnd, fields);
		SQLParameter param = getSQLParam(vo, names);
		// session.setReadOnly(true);
		return session.executeQuery(sql, param, processor);
	}

	/**
	 * @param className
	 * @param meta
	 * @return
	 * @throws DbException
	 */
	public Collection retrieveAll(Class className, IMappingMeta meta)
			throws DbException {
		Object vo = InitClass(className);
		BeanMapping mapping = new BeanMapping(vo, meta);
		// session.setReadOnly(true);
		return (Collection) session.executeQuery(mapping.getSelectSQL(),
				new BeanMappingListProcessor(className, meta));

	}

	/**
	 * 
	 */
	public Collection retrieveAll(Class className) throws DbException {

		SuperVO vo = initSuperVOClass(className);
		String tableName = vo.getTableName();
		String sql = "SELECT * FROM " + tableName;
		// session.setReadOnly(true);
		return (Collection) session.executeQuery(sql, new BeanListProcessor(
				className));

	}

	/**
	 * 
	 */
	public Collection retrieveByClause(Class className, String condition)
			throws DbException {
		return retrieveByClause(className, condition, null);
	}

	public Collection retrieveByClause(Class className, String condition,
			String[] fields, SQLParameter parameters) throws DbException {
		BaseProcessor processor = new BeanListProcessor(className);
		return (Collection) session.executeQuery(
				buildSql(className, condition, fields), parameters, processor);

	}

	public Collection retrieveByClause(Class className, String condition,
			String[] fields) throws DbException {
		return retrieveByClause(className, condition, fields, null);
	}

	/**
	 * @param className
	 * @param meta
	 * @param condition
	 * @param fields
	 * @return
	 * @throws DbException
	 */
	public Collection retrieveByClause(Class className, IMappingMeta meta,
			String condition, String[] fields) throws DbException {
		return retrieveByClause(className, meta, condition, fields, null);
	}

	public int getDBType() {
		return session.getDbType();
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public DatabaseMetaData getMetaData() {
		if (dbmd == null)
			dbmd = getJdbcSession().getMetaData();
		return dbmd;
	}

	/**
	 * @param isAddTimeStamp
	 */
	public void setAddTimeStamp(boolean isAddTimeStamp) {
		session.setAddTimeStamp(isAddTimeStamp);
	}

	/**
	 * @param isTranslator
	 */
	public void setSQLTranslator(boolean isTranslator) {
		session.setSQLTranslator(isTranslator);
	}

	public String getCatalog() {
		String catalog = null;
		switch (getDBType()) {
		case DBConsts.GBASE:
		case DBConsts.POSTGRESQL:
			try {
				catalog = getConnection().getCatalog();
			} catch (SQLException e) {
			}
			break;
		case DBConsts.ALCEDO:
		case DBConsts.SQLSERVER:
		case DBConsts.DB2:
			// null means drop catalog name from the selection criteria
			catalog = null;
			break;
		case DBConsts.ORACLE:
			catalog = null;
			break;
		case DBConsts.OSCAR:
			// "" retrieves those without a catalog
			catalog = "";
			break;
		}
		return catalog;
	}

	public String getSchema() {
		String strSche = null;
		try {
			String schema = getMetaData().getUserName();
			switch (getDBType()) {
			case DBConsts.POSTGRESQL:
			case DBConsts.ALCEDO:
				strSche = null;
				break;
			case DBConsts.SQLSERVER:
				strSche = "dbo";
				break;
			case DBConsts.ORACLE:
			case DBConsts.OSCAR:
			case DBConsts.DB2: {
				if (schema == null || schema.length() == 0)
					throw new IllegalArgumentException(
							"ORACLE Database mode does not allow to be null!!");
				// ORACLE需将模式名大写
				strSche = schema.toUpperCase();
				break;
			}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return strSche;
	}

	public void setReadOnly(boolean isReadOnly) throws DbException {
		session.setReadOnly(isReadOnly);
	}

	/**
	 * 初始化数据库会话连接
	 * 
	 * @throws DbException
	 */
	private void init() throws DbException {
		if (dataSource == null)
			session = new JdbcSession();
		else
			session = new JdbcSession(dataSource);

		session.setMaxRows(maxRows);
	}

	private void isNull(Object vo) {
		if (vo == null) {
			throw new IllegalArgumentException("vo object parameter is null!!");
		}
	}

	public void setMaxRows(int maxRows) {
		super.setMaxRows(maxRows);
		session.setMaxRows(maxRows);
	}

	/**
	 * @param className
	 * @param meta
	 * @param condition
	 * @return
	 * @throws DbException
	 */
	public Collection retrieveByClause(Class className, IMappingMeta meta,
			String condition) throws DbException {
		return retrieveByClause(className, meta, condition, meta.getColumns());
	}

	/**
	 * 得到参数类型对象
	 * 
	 * @param vo
	 * @param names
	 * @param types
	 * @return
	 */
	private SQLParameter getSQLParam(SuperVO vo, String names[],
			Map<String, Integer> types, Map<String, Integer> size) {
		SQLParameter params = new SQLParameter();
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase("ts"))
				continue;
			int type = types.get(names[i].toUpperCase());
			Object value = vo.getAttributeValue(names[i]);
			if (value == null && type == Types.VARCHAR) {
				Integer length = size.get(names[i].toUpperCase());
				if (length != null
						&& (length == 20 || length == 36 || length == 101)) {
					params.addParam(DBConsts.NULL_WAVE);
					continue;
				}
			}
			if (value == null && type == Types.NVARCHAR) {
				Integer length = size.get(names[i].toUpperCase());
				if (length != null
						&& (length == 20 || length == 36 || length == 101)) {
					params.addParam(DBConsts.NULL_WAVE);
					continue;
				}
			}
			if (value == null) {
				params.addNullParam(type);
				continue;
			}
			if (type == Types.BLOB || type == Types.LONGVARBINARY
					|| type == Types.VARBINARY || type == Types.BINARY) {
				params.addBlobParam(value);
				continue;
			}
			if (type == Types.CLOB || type == Types.LONGVARCHAR) {
				params.addClobParam(String.valueOf(value));
				continue;
			}
			params.addParam(value);

		}
		return params;
	}

	/**
	 * 得到有效的列名称
	 * 
	 * @param vo
	 * @param types
	 * @return
	 */
	private String[] getValidNames(final SuperVO vo, Map types) {
		String names[] = vo.getAttributeNames();
		List nameList = new ArrayList();
		for (int i = 0; i < names.length; i++) {
			if (types.get(names[i].toUpperCase()) != null
					&& !names[i].equalsIgnoreCase("ts")) {
				nameList.add(names[i]);
			}
		}
		return (String[]) nameList.toArray(new String[] {});
	}

	/**
	 * 得到有效列名称
	 * 
	 * @param vo
	 * @param types
	 * @param pkName
	 * @return
	 */
	private String[] getUpdateValidNames(SuperVO vo, Map types, String pkName) {
		String names[] = vo.getAttributeNames();
		List nameList = new ArrayList();
		for (int i = 0; i < names.length; i++) {
			if (types.get(names[i].toUpperCase()) != null
					&& !names[i].equalsIgnoreCase(pkName)
					&& !names[i].equalsIgnoreCase("ts")) {
				nameList.add(names[i]);
			}
		}
		return (String[]) nameList.toArray(new String[] {});
	}

	/**
	 * @param vo
	 * @param type
	 * @return
	 */
	private String[] getNotNullValidNames(SuperVO vo, Map type) {
		String names[] = vo.getAttributeNames();
		List nameList = new ArrayList();
		for (int i = 0; i < names.length; i++) {
			if (type.get(names[i].toUpperCase()) != null
					&& vo.getAttributeValue(names[i]) != null) {
				nameList.add(names[i]);
			}
		}
		if (nameList.size() == 0)
			return new String[0];
		return (String[]) nameList.toArray(new String[] {});
	}

	/**
	 * @param vo
	 * @param names
	 * @return
	 */
	private SQLParameter getSQLParam(SuperVO vo, String[] names) {
		if (names == null || names.length == 0) {
			return null;
		}
		SQLParameter parameter = new SQLParameter();
		for (int i = 0; i < names.length; i++) {
			parameter.addParam(vo.getAttributeValue(names[i]));
		}
		return parameter;
	}

	public Connection getConnection() {
		if (session != null)
			return session.getConnection();
		return null;
	}

	private ColCache getColCache() {
		String ds = ds();
		synchronized (colCacheMap) {
			ColCache colCache = colCacheMap.get(ds);
			if (colCache == null) {
				colCache = new ColCache();
				colCacheMap.put(ds, colCache);
			}
			return colCache;
		}
	}

	private String ds() {
		return dataSource == null ? DataSourceCenter.getInstance()
				.getSourceName() : dataSource;
	}

	private Map<String, Integer> getColmnSize(String table) throws DbException {
		ColCache cache = getColCache();

		Map<String, Integer> result = cache.sizeCache.get(table);
		if (result == null || result.size() == 0) {
			result = new HashMap<String, Integer>();
			ResultSet rsColumns = null;
			try {
				if (getDBType() == DBConsts.SQLSERVER && table.startsWith("#")) {
					Statement stmt = null;
					try {
						stmt = getConnection().createStatement();
						rsColumns = stmt.executeQuery("select top 0 * from "
								+ table);
						ResultSetMetaData rsMeta = rsColumns.getMetaData();
						int count = rsMeta.getColumnCount();

						for (int i = 1; i < count + 1; i++) {
							result.put(rsMeta.getColumnName(i).toUpperCase(),
									rsMeta.getPrecision(i));
						}
						if (result.size() >= 0) {
							cache.sizeCache.put(table, result);
						} else {
							throw new UnKnownException("no column info for: "
									+ table + " at datasource: " + ds());

						}

						return result;
					} finally {
						if (stmt != null) {
							stmt.close();
						}
					}
				} else {
					DatabaseMetaData dmd = getConnection().getMetaData();
					if (getDBType() == DBConsts.ORACLE
							|| getDBType() == DBConsts.OSCAR
							|| getDBType() == DBConsts.DB2) {
						rsColumns = dmd.getColumns(null, dmd.getUserName()
								.toUpperCase(), table.toUpperCase(), "%");
					} else if (getDBType() == DBConsts.POSTGRESQL) {
						rsColumns = getMetaData().getColumns(null, null,
								table.toLowerCase(), "%");
					} else if (getDBType() == DBConsts.ALCEDO) {
						rsColumns = getMetaData().getColumns(null, null,
								table.toLowerCase(), "%");
					} else if (getDBType() == DBConsts.GBASE) {
						rsColumns = dmd.getColumns(getCatalog(),
								dmd.getUserName(), table, "%");
					} else {
						rsColumns = dmd.getColumns(null, null,
								table.toUpperCase(), "%");
					}
					while (rsColumns.next()) {
						result.put(rsColumns.getString("COLUMN_NAME")
								.toUpperCase(), rsColumns.getInt("COLUMN_SIZE"));
					}

					if (result.size() >= 0) {
						cache.sizeCache.put(table, result);
					} else {
						throw new UnKnownException("no column info for: "
								+ table + " at datasource: " + ds());

					}
				}
			} catch (SQLException e) {
				Logger.error("get table metadata error", e);
				throw ExceptionFactory.getException(getDBType(),
						"get table metadata error", e);
			} finally {
				DBUtil.closeRs(rsColumns);
			}
		}
		return result;
	}

	/**
	 * 得到列的类型
	 * 
	 * @param table
	 * @return
	 */
	private Map<String, Integer> getColmnTypes(String table) throws DbException {

		ColCache cache = getColCache();

		Map<String, Integer> result = cache.typeCache.get(table);
		// 同时查出列的长度信息
		Map<String, Integer> sizeMap = cache.sizeCache.get(table);
		if (result == null || result.size() == 0) {
			boolean querySize = false;
			if (sizeMap == null) {
				sizeMap = new HashMap<String, Integer>();
				querySize = true;
			}
			Map<String, Integer> typeMap = new HashMap<String, Integer>();
			ResultSet rsColumns = null;
			try {
				if (getDBType() == DBConsts.SQLSERVER && table.startsWith("#")) {
					Statement stmt = null;
					try {
						stmt = getConnection().createStatement();
						rsColumns = stmt.executeQuery("select top 0 * from "
								+ table);
						ResultSetMetaData rsMeta = rsColumns.getMetaData();
						int count = rsMeta.getColumnCount();

						for (int i = 1; i < count + 1; i++) {
							typeMap.put(rsMeta.getColumnName(i),
									rsMeta.getColumnType(i));
							if (querySize) {
								sizeMap.put(rsMeta.getColumnName(i)
										.toUpperCase(), rsMeta.getPrecision(i));
							}
						}
						if (typeMap.size() > 0) {
							cache.typeCache.put(table, typeMap);
							cache.sizeCache.put(table, sizeMap);
						} else {
							throw new UnKnownException("no column info for: "
									+ table + " at datasource: " + ds());

						}

						return typeMap;
					} finally {
						if (stmt != null) {
							stmt.close();
						}
					}

				} else {

					if (getDBType() == DBConsts.SQLSERVER)
						rsColumns = getMetaData().getColumns(null, null,
								table.toUpperCase(), "%");
					else if (getDBType() == DBConsts.POSTGRESQL)
						rsColumns = getMetaData().getColumns(null, null,
								table.toLowerCase(), "%");
					else if (getDBType() == DBConsts.ALCEDO)
						rsColumns = getMetaData().getColumns(null, null,
								table.toLowerCase(), "%");
					else
						rsColumns = getMetaData().getColumns(null, getSchema(),
								table.toUpperCase(), "%");
					while (rsColumns.next()) {
						String columnName = rsColumns.getString("COLUMN_NAME")
								.toUpperCase();
						int columnType = rsColumns.getShort("DATA_TYPE");
						typeMap.put(columnName, columnType);
						if (querySize) {
							sizeMap.put(rsColumns.getString("COLUMN_NAME")
									.toUpperCase(), rsColumns
									.getInt("COLUMN_SIZE"));
						}
					}

					if (typeMap.size() > 0) {
						cache.typeCache.put(table, typeMap);
						cache.sizeCache.put(table, sizeMap);
					} else {
						throw new UnKnownException("no column info for: "
								+ table + " at datasource: " + ds());

					}

					return typeMap;
				}
			} catch (SQLException e) {
				Logger.error("get table metadata error", e);
				throw ExceptionFactory.getException(getDBType(),
						"get table metadata error", e);
			} finally {
				DBUtil.closeRs(rsColumns);
			}
		}
		return result;
	}

	public static void clearAllTableInfo() {
		if (colCacheMap.size() == 0) {
			return;
		}
		for (ColCache colCache : colCacheMap.values()) {
			colCache.typeCache.clear();
			colCache.sizeCache.clear();
		}
	}

	public static void clearColumnTypes(String table) {
		if (colCacheMap.size() == 0) {
			return;
		}
		for (ColCache colCache : colCacheMap.values()) {
			colCache.typeCache.remove(table);
			colCache.sizeCache.remove(table);
		}
	}

	private Object InitClass(Class className) {
		try {
			return className.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(
					"Parameter Class can not be instantiated!!");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Illegal Parameter!!");
		}
	}

	private SuperVO initSuperVOClass(Class className) {
		Object vo;
		try {
			vo = className.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(
					"Parameter Class can not be instantiated!!");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(" Illegal Parameter!!");
		}
		if (!(vo instanceof SuperVO))
			throw new IllegalArgumentException("Parameter Class is not SuperVO");
		return (SuperVO) vo;
	}

	private String buildSql(Class className, String condition, String[] fields) {
		SuperVO vo = (SuperVO) InitClass(className);
		String pkName = vo.getPKFieldName();
		boolean hasPKField = false;
		StringBuffer buffer = new StringBuffer();
		String tableName = vo.getTableName();
		if (fields == null)
			buffer.append("SELECT * FROM ").append(tableName);
		else {
			buffer.append("SELECT ");
			for (int i = 0; i < fields.length; i++) {
				if (fields[i] != null) {
					buffer.append(fields[i]).append(",");
					if (fields[i].equalsIgnoreCase(pkName))
						hasPKField = true;
				}
			}
			if (!hasPKField)
				buffer.append(pkName).append(",");
			buffer.setLength(buffer.length() - 1);
			buffer.append(" FROM ").append(tableName);
		}
		if (condition != null && condition.length() != 0) {
			if (condition.toUpperCase().trim().startsWith("ORDER "))
				buffer.append(" ").append(condition);
			else
				buffer.append(" WHERE ").append(condition);
		}

		return buffer.toString();
	}

	@Override
	public Collection retrieveByClause(Class className, IMappingMeta meta,
			String condition, String[] fields, SQLParameter params)
			throws DbException {
		String sql = SQLHelper.getSelectSQL(meta.getTableName(), fields);
		if (condition != null && condition.length() != 0) {
			if (condition.trim().toUpperCase().startsWith("ORDER "))
				sql = new StringBuffer().append(sql).append(" ")
						.append(condition).toString();
			else
				sql = new StringBuffer().append(sql).append(" WHERE ")
						.append(condition).toString();
		}
		BaseProcessor processor = new BeanMappingListProcessor(className, meta,
				fields);
		if (params != null) {
			return (Collection) session.executeQuery(sql, params, processor);
		} else {
			return (Collection) session.executeQuery(sql, processor);
		}
	}

	@Override
	public int deleteByClause(IMappingMeta meta, String wherestr,
			SQLParameter params) throws DbException {
		String sql = new StringBuffer().append("DELETE FROM ")
				.append(meta.getTableName()).toString();
		if (wherestr != null) {
			wherestr = wherestr.trim();
			if (wherestr.length() > 0) {
				if (wherestr.toLowerCase().startsWith("WHERE"))
					wherestr = wherestr.substring(5);
				if (wherestr.length() > 0)
					sql = sql + " WHERE " + wherestr;
			}
		}
		if (params == null)
			return session.executeUpdate(sql);
		else
			return session.executeUpdate(sql, params);

	}

	/**
	 * 插入属性动态变化的VO，属性名可能与字段不一致
	 */
	private String[] insert(final SuperVO vos[], IMappingMeta meta,
			boolean withPK) throws DbException {
		String[] pks = null;
		try {
			String tableName = meta.getTableName();

			Map<String, Integer> types = getColmnTypes(tableName);
			Map<String, Integer> sizes = getColmnSize(tableName);
			String sql = SQLHelper.getInsertSQL(tableName, meta.getColumns());

			pks = preparePK(vos, withPK);

			if (vos.length == 1) {
				SQLParameter parameter = getSQLParam(vos[0],
						meta.getAttributes(), meta.getColumns(), types, sizes);
				session.executeUpdate(sql, parameter);
			} else {
				SQLParameter[] parameters = new SQLParameter[vos.length];
				for (int i = 0; i < vos.length; i++) {
					if (vos[i] == null)
						continue;
					parameters[i] = getSQLParam(vos[i], meta.getAttributes(),
							meta.getColumns(), types, sizes);
				}
				session.addBatch(sql, parameters);
				session.executeBatch();
			}

		} finally {

		}
		return pks;

	}

	/**
	 * 更新属性动态变化的VO，属性名可能与字段不一致
	 */
	private int update(final SuperVO[] vo, IMappingMeta meta,
			String whereClause, SQLParameter param) throws DbException {
		isNull(vo);
		if (vo.length == 0)
			return 0;
		int row = 0;
		String tableName = meta.getTableName();
		String pkName = meta.getPrimaryKey();
		Map<String, Integer> types = getColmnTypes(tableName);
		Map<String, Integer> sizes = getColmnSize(tableName);
		String sql = SQLHelper.getUpdateSQL(tableName, meta.getColumns(),
				pkName);
		if (vo.length == 1) {
			SQLParameter parameter = getSQLParam(vo[0], meta.getAttributes(),
					meta.getColumns(), types, sizes);
			parameter.addParam(vo[0].getAttributeValue(pkName));
			if (whereClause == null)
				row = session.executeUpdate(sql, parameter);
			else {
				addParameter(parameter, param);
				row = session.executeUpdate(sql + " and " + whereClause,
						parameter);
			}
		} else {
			for (int i = 0; i < vo.length; i++) {
				if (vo[i] == null)
					continue;
				SQLParameter parameter = getSQLParam(vo[i],
						meta.getAttributes(), meta.getColumns(), types, sizes);
				parameter.addParam(vo[i].getAttributeValue(pkName));
				if (whereClause == null)
					session.addBatch(sql, parameter);
				else {
					addParameter(parameter, param);
					session.addBatch(sql + " and " + whereClause, parameter);
				}
			}
			row = session.executeBatch();
		}
		return row;
	}

	/**
	 * 构造参数对象
	 */
	private SQLParameter getSQLParam(SuperVO vo, String[] attribNames,
			String[] columnNames, Map<String, Integer> types,
			Map<String, Integer> size) {
		SQLParameter params = new SQLParameter();
		for (int i = 0; i < attribNames.length; i++) {
			if (attribNames[i].equalsIgnoreCase("ts"))
				continue;
			// int type = types.get(columnNames[i].toUpperCase());
			int type = -1;
			try {
				type = types.get(columnNames[i].toUpperCase());
			} catch (NullPointerException e) {
				Logger.error("get param type error, types=" + types
						+ ",columnNames=" + columnNames[i] + ",i=" + i);
				throw e;
			}
			Object value = vo.getAttributeValue(attribNames[i]);
			if (value == null && type == Types.VARCHAR) {
				int length = size.get(columnNames[i].toUpperCase());
				if (length == 20 || length == 36 || length == 101) {
					params.addParam(DBConsts.NULL_WAVE);
					continue;
				}
			}
			if (value == null && type == Types.NVARCHAR) {
				int length = size.get(columnNames[i].toUpperCase());
				if (length == 20 || length == 36 || length == 101) {
					params.addParam(DBConsts.NULL_WAVE);
					continue;
				}
			}
			if (value == null) {
				params.addNullParam(type);
				continue;
			}
			if (type == Types.BLOB || type == Types.LONGVARBINARY
					|| type == Types.VARBINARY || type == Types.BINARY) {
				params.addBlobParam(value);
				continue;
			}
			if (type == Types.CLOB || type == Types.LONGVARCHAR) {
				params.addClobParam(String.valueOf(value));
				continue;
			}
			params.addParam(value);

		}
		return params;
	}

}
