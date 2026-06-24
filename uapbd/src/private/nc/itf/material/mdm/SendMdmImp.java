package nc.itf.material.mdm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.trade.business.HYPubBO;
import nc.cmp.tools.StringUtil;
import nc.impl.pm.util.db.QueryUtil;
import nc.itf.bd.defdoc.IDefdocQryService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ProcessorUtils;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.message.util.MessageSender;
import nc.pubitf.qc.message.MessageUtils;
import nc.uap.ws.message.MessageUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.material.mdm.MdmMaterialBatchUtil;
import nc.vo.material.mdm.SendMdmPropetys;
import nc.vo.materialclass.mdm.MaterialClassFoMdmPropetys;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class SendMdmImp implements SendMdmItf {

	String mdmSystemUrl;// http://10.1.131.24/iuapmdm/cxf/mdmrs/newcenter/newCenterService
	String SYSCODE = "NCC";

	// private Object object;

	/**
	 * 获取一个主数据请求
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private Request getMdmPost(String method) throws BusinessException {

		String systemURl = this.getSystemURl("MDM");
		Request request = Request.Post(systemURl + method);
		String token = "";
		try {
			token = getTokenForAPI(SYSCODE);
		} catch (Exception e) {
			throw new BusinessException("token 获取失败：" + e.getMessage());
		}

		request.addHeader("User-Agent",
				"Apipost client Runtime/+https://www.apipost.cn/");
		request.addHeader("Mdmtoken", token);
		request.addHeader("Tenantid", "tenant");
		request.addHeader("Content-Type", "application/json");

		return request;

	}

	@Override
	/**
	 * 条件查询主数据主表
	 */
	public List<Map<String, String>> queryMdmPrimary(
			Map<String, List<String>> conds) throws BusinessException {
		if (conds == null || conds.size() == 0) {
			return null;
		}

		Request request = this.getMdmPost("/queryListMdByConditions");
		JSONObject json = new JSONObject();
		List<Map<String, String>> retlist = new ArrayList<Map<String, String>>();

		// 由于主数据不无对or进行区别查询，所以这里需要依次对查询条件进行查询后，再做数据组装；后期如果要增加条件，关键词只能用and
		for (String key : conds.keySet()) {
			Map<String, List<String>> cond = new HashMap<String, List<String>>();
			cond.put(key, conds.get(key));
			JSONObject condjson = (JSONObject) JSON.toJSON(cond);

			try {
				json.put("systemCode", SYSCODE);
				json.put("gdCode", "GROUPMATERIAL");
				// json.put("conditions", new JSONObject(condMap));// test
				json.put("conditions", condjson);
				json.put("pageable", "false");
				json.put("pageIndex", 1);
				json.put("pageSize", 3000);
			} catch (Exception e) {
				throw new BusinessException("json组装时出现问题，导致查询失败！");
			}

			request.bodyString(json.toString(), ContentType.APPLICATION_JSON);
			HttpResponse httpResponse = null;

			try {
				httpResponse = request.execute().returnResponse();

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (200 != statusCode) {
					throw new BusinessException("主数据获取失败，请联系技术人员进行检查！");
				}
				// System.out.println(httpResponse.getStatusLine());
				if (httpResponse.getEntity() != null) {
					// StringEntity strEntity = (StringEntity)
					// httpResponse.getEntity();
					String html = EntityUtils.toString(
							httpResponse.getEntity(), "UTF-8");

					JSONObject jsonObject = JSON.parseObject(html);
					Boolean success = (Boolean) jsonObject.get("success");
					if (!success) {
						throw new BusinessException(
								jsonObject.getString("message"));
					}
					if (null == jsonObject.get("data")) {
						// return retlist;
						continue;
					}
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (null != jsonArray && jsonArray.size() > 0) {
						// retlist = new ArrayList<Map<String, String>>();

						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject object = (JSONObject) jsonArray.get(i);
							if (null == object
									|| "null".equals(object
											.getString("groupMarbascode"))) // 过滤空数据或者是集团统一分类为空的数据
								continue;
							// JSONObject jsonObject2 = new JSONObject(object);
							Map<String, String> rowData = new HashMap<String, String>();

							retlist.add(rowData);
							for (String key1 : SendMdmPropetys.bodyItems) {
								// if("null".equals(object.getString("groupMarbascode")))
								// break;
								rowData.put(key1, object.getString(key1));
							}
						}
					}

					// System.out.println(html);
					Logger.debug(html);
				}
			} catch (ClientProtocolException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (IOException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (JSONException e) {
				throw new BusinessException("数据解析异常，请联系技术人员处理！\r\n"
						+ e.getMessage());
			}

		}
		Set<Object> retSet = new HashSet<Object>(Arrays.asList(retlist
				.toArray()));
		if (0 != retSet.size()) {
			retlist.clear();
			Iterator<Object> iterator = retSet.iterator();
			while (iterator.hasNext()) {
				Map<String, String> obj = (Map<String, String>) iterator.next();
				retlist.add(obj);
			}
		}

		return retlist;
	}

	@Override
	public List<Map<String, String>> queryMdmPrimary(
			Map<String, List<String>> conds , String mode) throws BusinessException {
		
		if (conds == null || conds.size() == 0) {
			return null;
		}

		Request request = this.getMdmPost("/queryListMdByConditions");
		JSONObject json = new JSONObject();
		List<Map<String, String>> retlist = new ArrayList<Map<String, String>>();
		if("unity".equals(mode)){//查询所有条件联合结果
			Map<String, List<String>> cond = new HashMap<String, List<String>>();
			for (String key : conds.keySet()){
				cond.put(key, conds.get(key));
			}
			JSONObject condjson = (JSONObject) JSON.toJSON(cond);
			
			try {
				json.put("systemCode", SYSCODE);
				json.put("gdCode", "GROUPMATERIAL");
				json.put("conditions", condjson);
				json.put("pageable", "false");
				json.put("pageIndex", 1);
				json.put("pageSize", 3000);
			} catch (Exception e) {
				throw new BusinessException("json组装时出现问题，导致查询失败！");
			}

			request.bodyString(json.toString(),
					ContentType.APPLICATION_JSON);
			HttpResponse httpResponse = null;

			try {
				httpResponse = request.execute().returnResponse();

				int statusCode = httpResponse.getStatusLine()
						.getStatusCode();
				if (200 != statusCode) {
					throw new BusinessException("主数据获取失败，请联系技术人员进行检查！");
				}
				// System.out.println(httpResponse.getStatusLine());
				if (httpResponse.getEntity() != null) {
					// StringEntity strEntity = (StringEntity)
					// httpResponse.getEntity();
					String html = EntityUtils.toString(
							httpResponse.getEntity(), "UTF-8");

					JSONObject jsonObject = JSON.parseObject(html);
					Boolean success = (Boolean) jsonObject.get("success");
					if (!success) {
						throw new BusinessException(
								jsonObject.getString("message"));
					}
					if (null == jsonObject.get("data")) {
						// return retlist;
//						continue;
					}
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (null != jsonArray && jsonArray.size() > 0) {
						// retlist = new ArrayList<Map<String, String>>();

						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject object = (JSONObject) jsonArray
									.get(i);
							if (null == object
									|| "null".equals(object
											.getString("groupMarbascode"))) // 过滤空数据或者是集团统一分类为空的数据
								continue;
							// JSONObject jsonObject2 = new
							// JSONObject(object);
							Map<String, String> rowData = new HashMap<String, String>();

							retlist.add(rowData);
							for (String key1 : SendMdmPropetys.bodyItems) {
								// if("null".equals(object.getString("groupMarbascode")))
								// break;
								rowData.put(key1, object.getString(key1));
							}
						}
					}

					// System.out.println(html);
					Logger.debug(html);
				}
			} catch (ClientProtocolException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (IOException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (JSONException e) {
				throw new BusinessException("数据解析异常，请联系技术人员处理！\r\n"
						+ e.getMessage());
			}
			
		}
		if ("and".equals(mode)) {
			// 由于主数据不无对or进行区别查询，所以这里需要依次对查询条件进行查询后，再做数据组装；后期如果要增加条件，关键词只能用and
			for (String key : conds.keySet()) {
				Map<String, List<String>> cond = new HashMap<String, List<String>>();
				cond.put(key, conds.get(key));
				JSONObject condjson = (JSONObject) JSON.toJSON(cond);

				try {
					json.put("systemCode", SYSCODE);
					json.put("gdCode", "GROUPMATERIAL");
					// json.put("conditions", new JSONObject(condMap));// test
					json.put("conditions", condjson);
					json.put("pageable", "false");
					json.put("pageIndex", 1);
					json.put("pageSize", 3000);
				} catch (Exception e) {
					throw new BusinessException("json组装时出现问题，导致查询失败！");
				}

				request.bodyString(json.toString(),
						ContentType.APPLICATION_JSON);
				HttpResponse httpResponse = null;

				try {
					httpResponse = request.execute().returnResponse();

					int statusCode = httpResponse.getStatusLine()
							.getStatusCode();
					if (200 != statusCode) {
						throw new BusinessException("主数据获取失败，请联系技术人员进行检查！");
					}
					// System.out.println(httpResponse.getStatusLine());
					if (httpResponse.getEntity() != null) {
						// StringEntity strEntity = (StringEntity)
						// httpResponse.getEntity();
						String html = EntityUtils.toString(
								httpResponse.getEntity(), "UTF-8");

						JSONObject jsonObject = JSON.parseObject(html);
						Boolean success = (Boolean) jsonObject.get("success");
						if (!success) {
							throw new BusinessException(
									jsonObject.getString("message"));
						}
						if (null == jsonObject.get("data")) {
							// return retlist;
							continue;
						}
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						if (null != jsonArray && jsonArray.size() > 0) {
							// retlist = new ArrayList<Map<String, String>>();

							for (int i = 0; i < jsonArray.size(); i++) {
								JSONObject object = (JSONObject) jsonArray
										.get(i);
								if (null == object
										|| "null".equals(object
												.getString("groupMarbascode"))) // 过滤空数据或者是集团统一分类为空的数据
									continue;
								// JSONObject jsonObject2 = new
								// JSONObject(object);
								Map<String, String> rowData = new HashMap<String, String>();

								retlist.add(rowData);
								for (String key1 : SendMdmPropetys.bodyItems) {
									// if("null".equals(object.getString("groupMarbascode")))
									// break;
									rowData.put(key1, object.getString(key1));
								}
							}
						}

						// System.out.println(html);
						Logger.debug(html);
					}
				} catch (ClientProtocolException e) {
					throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
							+ e.getMessage());
				} catch (IOException e) {
					throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
							+ e.getMessage());
				} catch (JSONException e) {
					throw new BusinessException("数据解析异常，请联系技术人员处理！\r\n"
							+ e.getMessage());
				}

			}
		}
		Set<Object> retSet = new HashSet<Object>(Arrays.asList(retlist
				.toArray()));
		if (0 != retSet.size()) {
			retlist.clear();
			Iterator<Object> iterator = retSet.iterator();
			while (iterator.hasNext()) {
				Map<String, String> obj = (Map<String, String>) iterator.next();
				retlist.add(obj);
			}
		}

		return retlist;
	}
	
	/**
	 * 通过系统编码获取在ERP注册的服务地址； Erp 档案编码 JZYY_PZQD
	 * 
	 * @param syscode
	 *            系统编码
	 * @return url
	 * @throws BusinessException
	 */
	public String getSystemURl(String syscode) throws BusinessException {
		if (mdmSystemUrl == null) {
			IDefdocQryService lookup = NCLocator.getInstance().lookup(
					IDefdocQryService.class);
			/*
			 * String pk_defdoclist, String pk_org, String pk_group, String
			 * whereSql
			 */
			DefdocVO[] queryDefdocVOsByDoclistPkAndWhereSql = lookup
					.queryDefdocVOsByDoclistPkAndWhereSql(
							"1001V1100000000FJN0D", "0001V110000000000FH0",
							"0001V110000000000FH0", "bd_defdoc.code = '"
									+ syscode + "'");
			if (null == queryDefdocVOsByDoclistPkAndWhereSql
					|| queryDefdocVOsByDoclistPkAndWhereSql.length < 1) {
				throw new BusinessException("未配置主数据系统，请检查！");
			}
			queryDefdocVOsByDoclistPkAndWhereSql[0].getName();

			mdmSystemUrl = queryDefdocVOsByDoclistPkAndWhereSql[0].getName();
		}
		return mdmSystemUrl;
	}

	/**
	 * 获取主数据MDM系统分配的动态token
	 * 
	 * @param syscode
	 *            注册的系统编码
	 * @return token
	 * @throws Exception
	 *             Connection\IO\JSONObject
	 */

	public String getTokenForAPI(String syscode) throws Exception {

		// 创建URL对象
		URL url = new URL(this.getSystemURl(SYSCODE) + "/getToken/" + syscode);

		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置请求方法为GET
		connection.setRequestMethod("GET");

		// 接收响应码
		int responseCode = connection.getResponseCode();
		// System.out.println("Response Code: " + responseCode);
		if (responseCode != 200) {
			throw new Exception("链接错误！");
		}

		// 读取响应内容
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close(); // 流关闭

		// 打印结果
		// System.out.println(response.toString());
		JSONObject jsonObject = JSON.parseObject(response.toString());
		connection.disconnect(); // // 断开连接

		JSONArray data = (JSONArray) jsonObject.get("data");

		if (null != data) {
			jsonObject = (JSONObject) data.get(0);
		} else {
			throw new Exception("mdmToken 获取失败!");
		}
		jsonObject.get("mdmtoken");

		return (String) jsonObject.get("mdmtoken");

	}

	/**
	 * 同步主数据主表 bbt
	 * 
	 * @return <pk_material,<field,value>>
	 */
	@Override
	public Map<String,Map<String, String>> insetMdmMaterialHost(MaterialVO[] matials,
			String pk_org) throws BusinessException {
		/*
		 * 先调用主数据主表的新增 再调用已有主数据查询 ：
		 * 这里需要做界面刷新，需要以方法传进来的参数重新构建查询条件，如果主数据能够返回主数据编码就直接用主数据编码调出对应主数据是最好的效果，
		 * 将查询出来的主数据通过返回值 进行返回，以重新构建表体显示
 		 * 新增或关联物料的逻辑 1、用户必须先调用MDM按钮进行一次已有数据查询
		 * 2、用户对选中行，做“升级到主数据”操作时，将界面信息组装成json，生成一个集团码传给主数据进行新增
		 * 3、用户对选中行，做“关联主数据”操作时，将界面信息组装成json，获取原有集团码传给主数据进行新增
		 * 4、以上操作执行结束后，均要对ui界面进行更新，即重新调用一遍MDM按钮
		 */

		// masterData存储的是一个JSONArray[Map<String,JSONArray>]
		// 外部JSONArray在后续进行定义，将生成的Map添加进去
		// Map<String, String> masterMap = new HashMap<String, String>();
//		Map<String, String> masterMap = null;
		// 存储整个报文的结果
		// 最终返回<物料ID,<field,value>>
		Map<String, Map<String,String>> resultData = new HashMap<String, Map<String,String>>();
		try {
			// 组装报文信息
			List<Map<String, String>> listMas = new ArrayList<Map<String, String>>();
			Map<String,MaterialVO> nc_materialmap = new HashMap<String,MaterialVO>();
			for(MaterialVO mt : matials){
				// 放入map值到list中，如做多个materialVO则要将其放到循环体内
				listMas.add(messagePackage(mt));
				nc_materialmap.put(mt.getPk_material(), mt);
			}


			JSONObject json = new JSONObject();
			json.put("systemCode", SYSCODE);
			json.put("gdCode", "GROUPMATERIAL");
			json.put("masterData", listMas);

			// 组装插入接口地址
			Request request = this.getMdmPost("/insertMd");
			HttpResponse httpResponse = null;
			request.bodyString(json.toString(), ContentType.APPLICATION_JSON);
			try {
				httpResponse = request.execute().returnResponse();

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (200 != statusCode) {
					throw new BusinessException("主数据获取失败，请联系技术人员进行检查！");
				}
				if (httpResponse.getEntity() != null) {
					String html = EntityUtils.toString(
							httpResponse.getEntity(), "UTF-8");

					JSONObject jsonObject = JSONObject.parseObject(html);
					Boolean success = (Boolean) jsonObject.get("success");
					if (!success) {
						throw new BusinessException(
								jsonObject.getString("message"));
					}
					Logger.debug(html);

					// 处理返回的mdmCode
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					for (int j = 0; j < jsonArray.size(); j++) {//返回的批量数据
						JSONObject jsonObject2 = jsonArray.getJSONObject(j);
//						jsonObject2.get("mdm_code");
						Map<String,String> rst = new HashMap<String,String>();
						//取原始物料ID封装返回
						
						rst.put(SendMdmPropetys.MDMCODE, (String) jsonObject2.get("mdm_code")); 
						String pk_material = (String) jsonObject2.get("id");
						MaterialVO materialVO = nc_materialmap.get(pk_material);
						
						rst.put(SendMdmPropetys.MDMCLASS,materialVO.getDef14() );
						rst.put("material_code",materialVO.getCode() );
						
						resultData.put(jsonObject2.getString("id"), rst);
						
					}
					
				

					List<MaterialVO> lst = new ArrayList<MaterialVO>();
					MaterialVO[] materials  = null;
					for(String key : nc_materialmap.keySet()){
						lst.add(nc_materialmap.get(key));
					};
					materials = lst.toArray(new MaterialVO[0]);
					// 处理辅表同步
					replenishDef7(resultData, materials);
					
					insetMdmMaterialAssist(materials, pk_org);

					int updateVOArray = getDao().updateVOArray(materials,
							new String[] { "def7", "def14" });

					// System.out.println(updateVOArray);
				}
			} catch (ClientProtocolException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (IOException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (JSONException e) {
				throw new BusinessException("数据解析异常，请联系技术人员处理！\r\n"
						+ e.getMessage());
			}

			// Map<String, List<String>> condsByVO =
			// SendMdmPropetys.getCondsByVO(matial);
			// listJson = queryMdmPrimary(condsByVO);

			// 无异常后处理关联和初始化界面功能

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		// return listJson;
		return resultData;
	}

	/**
	 * 组装报文信息 bbt 将VO中的信息放到目标Map中
	 * */
	private Map<String, String> messagePackage(MaterialVO matial)
			throws DAOException {
		/************ 报文内的值组装：编码 释义 是否必填 **********************************************/
		Map<String, String> masterMap = null;
		if (null == matial)
			return masterMap;
		masterMap = new HashMap<String, String>();
		// id 主数据唯一值 Y
		masterMap.put("id", matial.getPk_material());
		// brand 品牌 N
		// 做特殊处理，传生产厂家名
		masterMap.put("brand",
				(String) matial.getAttributeValue("vmanufacturer_148"));
		// cpack 包装规格 N
		masterMap.put("cpack", "");
		// enablestate#name 启用状态 Y
		masterMap.put("enablestate#name", matial.getEnablestate() == 2 ? "启用"
				: "停用");
		// //english_name 英文名 N
		masterMap.put("english_name", matial.getEname());
		// fromsystem 数据源系统 N
		masterMap.put("fromsystem", SYSCODE);
		// fromsystemid 源系统ID N
		// 将pk_material放在源系统ID中，用作界面数据更新使用
		masterMap.put("fromsystemid", matial.getPk_material());
		// groupcode 九洲集团物料码 N
		masterMap.put("groupcode", null != matial.getDef5() ? matial.getDef5()
				: matial.getCode());
		// groupMarbasclass#name 集团物料分类 N
		// masterMap
		// .put("groupMarbasclass#name",
		// null != queryDocNameByID(matial.getPk_marbasclass(),
		// "bd_marbasclass", "PK_MARBASCLASS", "name") ? queryDocNameByID(
		// matial.getPk_marbasclass(), "bd_marbasclass",
		// "PK_MARBASCLASS", "name") : "物料分类");
		// groupMarbascode 集团分类编码
		masterMap.put(
				"groupMarbascode#name", /* groupMarbascode */
				queryDocNameByID(matial.getDef14(), " bd_defdoc ",
						"bd_defdoc.pk_defdoc ", "bd_defdoc.name"));
		// invname 通用名称 Y
		masterMap.put("invname", matial.getName());
		// model 型号 Y
		masterMap.put("model", matial.getMaterialtype());
		// pk_material 物料编码 Y
		masterMap.put("pk_material", matial.getCode());
		// postunit#name 计量单位 Y
		masterMap.put(
				"postunit#code",
				null != queryDocNameByID(matial.getPk_measdoc(), "bd_measdoc",
						"pk_measdoc", "code") ? queryDocNameByID(
						matial.getPk_measdoc(), "bd_measdoc", "pk_measdoc",
						"code") : "计量单位");
		// reserve1 预留1 N
		masterMap.put("reserve1", matial.getDef1());
		// reserve10 预留10 N
		masterMap.put("reserve10", matial.getDef10());
		// reserve2 预留2 N
		masterMap.put("reserve2", matial.getDef2());
		// reserve3 预留3 N
		masterMap.put("reserve3", matial.getDef3());
		// reserve4 预留4 N
		masterMap.put("reserve4", matial.getDef4());
		// reserve5 预留5 N
		masterMap.put("reserve5", matial.getDef5());
		// reserve6 预留6 N
		masterMap.put("reserve6", matial.getDef6());
		// reserve7 预留7 N
		masterMap.put("reserve7", matial.getDef7());
		// reserve8 预留8 N
		masterMap.put("reserve8", matial.getDef8());
		// reserve9 预留9 N
		masterMap.put("reserve9", matial.getDef9());
		// spec 规格 Y
		masterMap.put("spec", matial.getMaterialspec());
		// splicename 唯一校验的特殊名 Y
		masterMap.put("splicename",
				matial.getDef8() + " " + matial.getMaterialspec() + " "
						+ matial.getMaterialtype());
		// updatetime 最后的更新时间 Y
		UFDateTime serverTime = AppContext.getInstance().getServerTime();
		masterMap.put("updatetime", serverTime.toString());
		// variant_name 异名 N
		masterMap.put("variant_name", "");
		masterMap.put("materialmnecode", matial.getMaterialmnecode());// 助记码
		masterMap.put("memo", matial.getMemo());// 备注
		masterMap.put("cz", matial.getDef19()); // 材质--文本
		String yzb = (null == matial.getDef18() || "N"
				.equals(matial.getDef18())) ? "否" : "是";
		masterMap.put("yzb#name", yzb);
		String yzd = (null == matial.getDef18() || "N"
				.equals(matial.getDef20())) ? "否" : "是";
		masterMap.put("yzd#name", yzd);
		/***************************************************************************************/
		return masterMap;
	}

	public JSONArray materialAssistFoJson(MaterialVO[] mateirals, String pk_org)
			throws JSONException, DAOException {
		if (null == mateirals || mateirals.length == 0 || null == pk_org) {
			return null;
		}
		JSONArray dataArray = new JSONArray();
		for (MaterialVO material : mateirals) {
			JSONObject rowobj = new JSONObject();
			dataArray.add(rowobj);
			rowobj.put("id", material.getPk_material());
			rowobj.put("pk_material", material.getPk_material());
			rowobj.put("enablestate#name",
					material.getEnablestate() == 2 ? "启用" : "停用");// 启用状态名称
			rowobj.put("ename", material.getEname());// 英文名称
			rowobj.put("name", material.getName());// 通用名，取物料名称
			rowobj.put("material_code", material.getCode());// 物料编码
			rowobj.put("marbasclasscode", this.queryDocNameByID(
					material.getPk_marbasclass(), "bd_marbasclass",
					"pk_marbasclass", "code"));// 物料基本分类编码
			rowobj.put("marbasclassname", this.queryDocNameByID(
					material.getPk_marbasclass(), "bd_marbasclass",
					"pk_marbasclass", "name"));// 物料基本分类名称
			rowobj.put("pk_marbasclass", material.getPk_marbasclass());
			rowobj.put("materialspec", material.getMaterialspec());// 规格
			rowobj.put("materialtype", material.getMaterialtype());// 型号

			rowobj.put("groupclasscode", this.queryDocNameByID(
					material.getPk_marbasclass(), "bd_marbasclass",
					"pk_marbasclass", "code"));// 集团物料分类编码
			rowobj.put("groupclassname", this.queryDocNameByID(
					material.getPk_marbasclass(), "bd_marbasclass",
					"pk_marbasclass", "name"));// 集团物料分类名称
			rowobj.put("invclassid", material.getPk_marbasclass());// 物料基本分类ID
			rowobj.put("measdoc#code", this.queryDocNameByID(
					material.getPk_measdoc(), "bd_measdoc", "pk_measdoc",
					"code"));// 计量单位名称

			String org_code = this.queryDocNameByID(pk_org, "org_orgs",
					"pk_org", "code");
			org_code = "G".equals(org_code) ? "JZ" : org_code;
			rowobj.put("pk_org#code", org_code);// 组织编码 /取申请组织
			rowobj.put("sup_code#mdm_code", material.getDef7());// 关联主数据编码*******
			rowobj.put("vmanufacturer",
					material.getAttributeValue("vmanufacturer_148"));// 生产厂家

			rowobj.put(
					"groupMarbascode#name", /* groupMarbascode */
					queryDocNameByID(material.getDef14(),
							" (select * from bd_defdoc where pk_defdoclist ='1001V1100000000W68CQ') bd_defdoc ",
							"bd_defdoc.pk_defdoc ", "bd_defdoc.name"));

			rowobj.put("cz", material.getDef19()); // 材质--文本
			String yzb = (null == material.getDef18() || "N".equals(material
					.getDef18())) ? "否" : "是";
			rowobj.put("yzb#name", yzb);
			String yzd = (null == material.getDef18() || "N".equals(material
					.getDef20())) ? "否" : "是";
			rowobj.put("yzd#name", yzd);
		}

		return dataArray;
	}

	/**
	 * 同步主数据辅表 zhian.ye
	 */
	@Override
	public UFBoolean insetMdmMaterialAssist(MaterialVO[] materialvos,
			String pk_org) throws BusinessException {
		/*
		 * 调用物料辅表新增
		 */
		if (null == materialvos || materialvos.length == 0) {
			throw new BusinessException("未接收到参数，请检查界面选择！");
		}
		// 检查同公司是否已有相同的主数据编码，
		checkOrgMdm(materialvos, pk_org);

		Request mdmPost = getMdmPost("/insertMd"); // 获取连接对象
		// 准备传输参数
		JSONObject jsobj = new JSONObject();
		JSONArray dataArray = new JSONArray();
		try {
			jsobj.put("systemCode", SYSCODE);
			jsobj.put("gdCode", "ORGMATERIAL");
			dataArray = materialAssistFoJson(materialvos, pk_org);
			jsobj.put("masterData", dataArray);
		} catch (JSONException e) {
			throw new BusinessException(
					"JSON 封装错误，请联系技术人员检查；位置：/uapbd/src/private/nc/itf/material/mdm/SendMdmImp.insetMdmMaterialAssist");
		}
		mdmPost.bodyString(jsobj.toString(), ContentType.APPLICATION_JSON);

		// //执行
		HttpResponse returnResponse = null;
		try {
			returnResponse = mdmPost.execute().returnResponse();
			int statusCode = returnResponse.getStatusLine().getStatusCode();
			if (200 != statusCode) {
				throw new BusinessException("主数据辅表新增失败，请联系技术人员进行检查！通信异常:"
						+ statusCode);
			}
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}

		// //解析结果集
		try {
			String html = EntityUtils.toString(returnResponse.getEntity(),
					"UTF-8");
			JSONObject jsonObject = JSON.parseObject(html);
			Boolean success = (Boolean) jsonObject.get("success");
			if (!success) {
				throw new BusinessException(jsonObject.getString("message"));
			}
		} catch (ParseException | IOException | JSONException e) {
			throw new BusinessException("新增结果解析失败，请联系技术人员进行检查！"
					+ e.getMessage());
		}
		// 这里只管把数据写进去，只要不返回错误应该就已经成功了，主数据编码由前台界面去获取

		return UFBoolean.TRUE;

	}

	/**
	 * 检查同公司下是否有相同的物料主数据编码
	 * 
	 * @param materialvos
	 * @param pk_org
	 */
	private void checkOrgMdm(MaterialVO[] materialvos, String pk_org)
			throws BusinessException {

		if (null == materialvos) {
			throw new BusinessException("物料信息为空，无法同步主数据！"
					+ this.getClass().getName());
		}
		List<MaterialVO> voList = new ArrayList<MaterialVO>();
		Map<String, MaterialVO> keymap = new HashMap<String, MaterialVO>();

		for (MaterialVO vo : materialvos) {
			keymap.put(vo.getDef7(), vo);
		}

		String str = " pk_org = '"
				+ pk_org
				+ "' and "
				+ SQLUtil.buildSqlForIn("def7",
						keymap.keySet().toArray(new String[0]));
		MaterialVO[] queryVOByCond = QueryUtil.queryVOByCond(MaterialVO.class,
				str, null);

		if (null != queryVOByCond || queryVOByCond.length > 0) {
			// 查询出有来值时，把MaterialVO剔除并把可执行的数据向下执行，
			for (MaterialVO qvo : queryVOByCond) {
				String def7 = qvo.getDef7();
				throw new BusinessException("组织下重复主数据编码：【" + def7
						+ "】 ，请先检查组织下【" + qvo.getCode() + "】是否与当前物料重复！"
						+ this.getClass().getName());
			}
		}

	}

	/**
	 * 通过档案ID查询指定字段值 zhian.ye
	 * 
	 * @throws DAOException
	 */
	@Override
	public String queryDocNameByID(String id, String tablename, String keyname,
			String fielName) throws DAOException {
		String sql = "select " + fielName + " from " + tablename + " where "
				+ keyname + "= '" + id + "'";
		String value = (String) getDao().executeQuery(sql,
				new ResultSetProcessor() {

					@Override
					public Object handleResultSet(ResultSet paramResultSet)
							throws SQLException {
						Object[] array = null;
						while (paramResultSet.next()) {
							array = ProcessorUtils.toArray(paramResultSet);

						}
						if (null != array && array.length == 1) {
							return array[0];
						}
						return "~";
					}
				});
		return value;
	}

	private BaseDAO dao;

	private BaseDAO getDao() {
		if (null == dao) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 批量初始化
	 * 
	 * @throws JSONException
	 */
	@Override
	@Deprecated
	public boolean batchToMdm(String pk_org) throws BusinessException,
			JSONException {
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select bd_material.* from bd_material ");
//		sql.append(" inner join bd_marbasclass  ");
//		sql.append(" on bd_material.pk_marbasclass  = bd_marbasclass.pk_marbasclass   ");
//		sql.append(" and bd_marbasclass.pk_marbasclass in (select pk_marbasclass  ");
//		sql.append(" from bd_marbasclass where  nvl(def1,'~')<>'~') ");
//		sql.append(" left join bd_materialstock on bd_material.pk_material = bd_materialstock.pk_material");
//		sql.append(" where nvl(bd_material.dr,0)=0 and bd_materialstock.pk_org='"
//				+ pk_org + "'");
//		List<MaterialVO> executeQuery = (List<MaterialVO>) getDao()
//				.executeQuery(sql.toString(),
//						new BeanListProcessor(MaterialVO.class));
//		if (null == executeQuery) {
//			throw new BusinessException("未查询到数据！请检查数据状态！");
//
//		}
//
//		Map<String, String> resultData = new HashMap<String, String>();
//		int max = 100; // 批处理量
//		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
//		List<MaterialVO> bufferVO = new ArrayList<MaterialVO>();
//		StringBuffer erroMessage = new StringBuffer();
//
//		for (int i = 0; i < executeQuery.size(); i++) {
//			bufferVO.add(executeQuery.get(i));
//			// 组装行数据
//			Map<String, String> messagePackage = messagePackage(executeQuery
//					.get(i));
//			mapList.add(messagePackage);
//			// 如果到达单发最大行数执行请求发送，以减少传输压力
//			if ((i + 1) % max == 0 || i == executeQuery.size() - 1) {
//				JSONObject json = new JSONObject();
//				json.put("systemCode", SYSCODE);
//				json.put("gdCode", "GROUPMATERIAL");
//				json.put("masterData", mapList);
//
//				Request request = this.getMdmPost("/insertMd");
//				HttpResponse httpResponse = null;
//				request.bodyString(json.toString(),
//						ContentType.APPLICATION_JSON);
//				try {
//					httpResponse = request.execute().returnResponse();
//					int statusCode = httpResponse.getStatusLine()
//							.getStatusCode();
//					if (200 != statusCode) {
//						// throw new BusinessException("主数据获取失败，请联系技术人员进行检查！");
//						erroMessage.append("主表同步失败！" + mapList.toString());
//					}
//					if (httpResponse.getEntity() != null) {
//						String html = EntityUtils.toString(
//								httpResponse.getEntity(), "UTF-8");
//
//						JSONObject jsonObject = JSON.parseObject(html);
//						Boolean success = (Boolean) jsonObject.get("success");
//
//						if (!success) {
//							// throw new
//							// BusinessException(jsonObject.getString("message")+"\n"+
//							// jsonObject.getString("errorRet"));
//							erroMessage.append(jsonObject.getString("message")
//									+ "\n" + jsonObject.getString("errorRet"));
//						} else {
//
//							JSONArray jsonArray = jsonObject
//									.getJSONArray("data");
//							for (int j = 0; j < jsonArray.size(); j++) {
//								JSONObject jsonObject2 = jsonArray
//										.getJSONObject(j);
//								resultData.put((String) jsonObject2.get("id"),
//										(String) jsonObject2.get("mdm_code"));
//							}
//						}
//						// 处理辅表同步
//						replenishDef7(resultData,
//								bufferVO.toArray(new MaterialVO[0]));
//						insetMdmMaterialAssist(
//								bufferVO.toArray(new MaterialVO[0]), pk_org);
//
//						getDao().updateVOArray(
//								bufferVO.toArray(new MaterialVO[0]),
//								new String[] { "def7" });
//
//						resultData.clear();
//						bufferVO.clear();
//						mapList.clear(); // 同步完成后缓存清除
//					}
//				} catch (ClientProtocolException e) {
//					// throw new BusinessException(e.getMessage());
//					erroMessage.append(e.getMessage());
//				} catch (IOException e) {
//					// throw new BusinessException(e.getMessage());
//					erroMessage.append(e.getMessage());
//				}
//
//			}
//
//		}

		return false;
	}

	private void replenishDef7(Map<String, Map<String, String>> resultData,
			MaterialVO[] bufferVO) {

		if (bufferVO.length == 0 || resultData.size() == 0) {
			return;
		}
		for (MaterialVO mateiral : bufferVO) {
			String string = resultData.get(mateiral.getPk_material()).get(SendMdmPropetys.MDMCODE);
			mateiral.setDef7(string);
		}

	}

	/**
	 * 集团统一分类同步 yza
	 */
	@Override
	public String insetMdmMaterialClass(DefdocVO[] vos)
			throws BusinessException {
		Request mdmPost = getMdmPost("/insertMd"); // 获取连接对象
		// 准备传输参数
		JSONObject jsobj = new JSONObject();
		JSONArray dataArray = new JSONArray();
		try {
			jsobj.put("systemCode", SYSCODE);
			jsobj.put("gdCode", "groupclass");
			dataArray = mateiralclassConverter(vos);
			jsobj.put("masterData", dataArray);
		} catch (JSONException e) {
			throw new BusinessException(
					"JSON 封装错误，请联系技术人员检查；位置：/uapbd/src/private/nc/itf/material/mdm/SendMdmImp.insetMdmMaterialAssist");
		}
		mdmPost.bodyString(jsobj.toString(), ContentType.APPLICATION_JSON);

		// //执行
		HttpResponse returnResponse = null;
		try {
			returnResponse = mdmPost.execute().returnResponse();
			int statusCode = returnResponse.getStatusLine().getStatusCode();
			if (200 != statusCode) {
				throw new BusinessException("主数据辅表新增失败，请联系技术人员进行检查！通信异常:"
						+ statusCode);
			}
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}

		// //解析结果集
		try {
			String html = EntityUtils.toString(returnResponse.getEntity(),
					"UTF-8");
			JSONObject jsonObject = JSON.parseObject(html);
			Boolean success = (Boolean) jsonObject.get("success");
			if (!success) {
				throw new BusinessException(jsonObject.getString("message"));
			}
			JSONArray data = (JSONArray) jsonObject.get("data");
			if (null != data && data.size() > 0) {
				for (Object obj : data) {
					JSONObject jsonobj = (JSONObject) obj;
					String mdm_code = (String) jsonobj.get("mdm_code");
					return mdm_code;
				}
			}
		} catch (ParseException | IOException | JSONException e) {
			throw new BusinessException("新增结果解析失败，请联系技术人员进行检查！"
					+ e.getMessage());
		}
		// 这里只管把数据写进去，只要不返回错误应该就已经成功了，主数据编码由前台界面去获取

		return null;
	}

	private IDefdocQryService getDocSever() {
		return NCLocator.getInstance().lookup(IDefdocQryService.class);

	}

	private JSONArray mateiralclassConverter(DefdocVO[] vos)
			throws BusinessException {
		JSONArray dataArray = new JSONArray();
		for (DefdocVO defdoc : vos) {
			JSONObject rowobj = new JSONObject();
			dataArray.add(rowobj);
			for (String key : MaterialClassFoMdmPropetys.materialFoMdmPropetyMap
					.keySet()) {
				rowobj.put(
						key,
						defdoc.getAttributeValue(MaterialClassFoMdmPropetys.materialFoMdmPropetyMap
								.get(key)));
			}
			if (2 == (Integer) rowobj.get("stu#name")) {
				rowobj.put("stu#name", "启用");
			} else {
				rowobj.put("stu#name", "停用");
			}
			DefdocVO[] queryDefdocByPk = getDocSever().queryDefdocByPk(
					new String[] { (String) rowobj.get("supcode#code") }); // PID锁定，必然只有一个结果
			if (null == queryDefdocByPk) {
				rowobj.put("supcode#code", null);
			} else {
				rowobj.put("supcode#code", queryDefdocByPk[0].getCode());
			}
		}

		return dataArray;

	}

	@Override
	public void materialSendGw(MaterialVO[] materialVOs)
			throws BusinessException {
		getHead();
		// MaterialVO[] materialVOs = VOCollectUtil.process((BDCommonEvent)
		// event,MaterialVO.class);
		Map<String, Object> map = new HashMap<>();

		map.put("OptionFlag", "C");

		List<Map<String, Object>> SkuList = new ArrayList<>();
		for (MaterialVO vo : materialVOs) {
			getDao().executeUpdate(
					"update bd_material set def17 = 'Y' where pk_material = '"
							+ vo.getPk_material() + "' ");
			Map<String, Object> item = new HashMap<>();
			item.put("OwnerOrgId", OwnerOrgId);// 归属企业固定值CommonFlag
			item.put("OwnerTeamId", OwnerTeamId);// 归属团队固定值
			item.put("CommonFlag", 1);// 归属企业固定值

			item.put("Sku", vo.getCode());// 物料编码
			item.put("GoodEnName", vo.getEname());// 英文名称
			item.put("GUnitEn", getUnitCode(vo.getPk_measdoc()));// 计量单位编码
			item.put("HsName", vo.getGoodsprtname());// 物料名称
			item.put("DangerousType", vo.getDef4());// 物料def4
			SkuList.add(item);
		}
		map.put("SkuList", SkuList);
		if (SkuList == null || SkuList.size() == 0) {
			return;
		}
		// 最外层中间平台数据
		Map<String, Object> outside = new HashMap<>();

		outside.put("srccode", "NC");
		outside.put("srcappkey", gwMiddleCode);
		outside.put("targetcode", "GW");
		outside.put("targetrule", "gwMaterial");
		outside.put("vbillcode", "");
		outside.put("data", map);
		restTemplate(outside);

	}

	public String OwnerOrgId;
	public String OwnerTeamId;
	public String gwMiddleCode;

	/**
	 * 查head信息
	 * 
	 * @throws BusinessException
	 */
	public void getHead() throws BusinessException {
		String head = (String) getDao().executeQuery(
				"select name from bd_defdoc where code = 'gwcode'",
				new ColumnProcessor());
		if (StringUtil.isEmpty(head)) {
			throw new BusinessException(
					"关务归属企业与归属团队未配置配置编码：gwcode 企业在前团队在后用,分割");
		}
		String[] split = head.split(",");
		OwnerOrgId = split[0];
		OwnerTeamId = split[1];
		String gwCode = (String) getDao().executeQuery(
				"select name from bd_defdoc where code = 'gwMiddleCode'",
				new ColumnProcessor());
		if (StringUtil.isEmpty(head)) {
			throw new BusinessException(
					"关务归属企业与归属团队未配置配置编码：gwcode 企业在前团队在后用,分割");
		}
		gwMiddleCode = gwCode;
	}

	/**
	 * 查计量单位
	 * 
	 * @throws DAOException
	 */

	protected String getUnitCode(String pk) throws DAOException {
		String sql = " select code  from bd_measdoc where pk_measdoc = '" + pk
				+ "';";
		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	/**
	 * 请求方法
	 * 
	 * @throws RestClientException
	 * @throws BusinessException
	 */
	protected void restTemplate(Map poSend) throws RestClientException,
			BusinessException {
		JSONObject json = new JSONObject(poSend);
		RestTemplate rest = new RestTemplate();
		JSONObject postForObject = rest.postForObject(
				(String) getMnecodeByCode("JZYY_PZQD", "GWURL", null), json,
				JSONObject.class);
		if (!"000000".equals(postForObject.getString("Code"))) {
			JSONObject jsonObject = postForObject.getJSONObject("Body");
			if (jsonObject != null) {
				JSONArray jsonArray = jsonObject.getJSONArray("SkuList");
				if (jsonArray != null && jsonArray.size() > 0
						&& jsonArray.getJSONObject(0) != null) {
					throw new BusinessException(jsonArray.getJSONObject(0)
							.getString("ErrorMessage"));
				}
			}
			throw new BusinessException("同步关务失败:"
					+ postForObject.getString("Msg"));
		}
	}

	/**
	 * 自定义项档案，查助记码
	 * 
	 * @param value
	 * @return
	 * @throws DAOException
	 * @throws UifException
	 */
	public Object getMnecodeByCode(String listcode, String doccode,
			String pk_org) throws DAOException, UifException {
		String where = " bd_defdoc.pk_defdoclist in (select t.pk_defdoclist from bd_defdoclist t where t.code ='"
				+ listcode + "') and bd_defdoc.code = '" + doccode + "'";

		if (!nc.vo.jcom.lang.StringUtil.isEmpty(pk_org)) {
			where = where + "and bd_defdoc.pk_org = '" + pk_org + "'";
		}

		DefdocVO[] vos = (DefdocVO[]) new HYPubBO().queryByCondition(
				DefdocVO.class, where);

		if (vos != null && vos.length > 0) {
			return vos[0].getName();
		} else {
			return null;
		}
	}

	/**
	 * ERP物料信息内部交换 主要处理场景：跨组织、跨系统协同单据
	 */
	@Override
	public String materialExchange(String pk_org, String pk_mateiral, String org)
			throws BusinessException {
		if (null == pk_org || pk_org.equals("") || null == pk_mateiral
				|| pk_mateiral.equals("") || null == org || org.equals("")) {
			throw new BusinessException(
					"转换信息不全："
							+ this.getClass().getName()
							+ "materialExchange(String pk_org, String pk_mateiral, String org)  参数都不可以为空。");
		}
		// 1、先检查物料来源，是否为来源公司或对方公司已经交叉分配，如果为交叉分配，那么可以维持原物料不做转换

		// 2、如果物料没有交叉分配，那么需要两方都配置相同物料编码
		return null;
	}

	/**
	 * ERP物料交换，将物料ID转换成目标公司ID 返回物料ID或null
	 */
	@Override
	public String materialChangeByOrg(String pk_material, String pk_org_dest)
			throws BusinessException {
		if (null == pk_material || "".equals(pk_material)
				|| null == pk_org_dest || "".equals(pk_org_dest)) {
			return null;
		}
		String sql = " select destmaterial.pk_material  "
				+ "from bd_material srcmaterial "
				+ "inner  join bd_material destmaterial on srcmaterial.def7 = destmaterial.def7 " +
				" and nvl(srcmaterial.def7,'~') <>'~' and nvl(destmaterial.def7,'~')<>'~' "
				+ "where nvl(srcmaterial.dr,0)=0 and nvl(destmaterial.dr,0)=0  "
				+ " and srcmaterial.pk_material ='"+pk_material+"' "
				+ " and destmaterial.pk_org = '"+pk_org_dest+"' ";
		
		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	@Override
	public String materialCodeChange(String mateiralcode, String pk_org_dest)
			throws BusinessException {
		if (null == mateiralcode || "".equals(mateiralcode)
				|| null == pk_org_dest || "".equals(pk_org_dest)) {
			return null;
		}
		String sql = " select destmaterial.code  "
				+ "from bd_material srcmaterial "
				+ "inner  join bd_material destmaterial on srcmaterial.def7 = destmaterial.def7 " +
				" and nvl(srcmaterial.def7,'~') <>'~' and nvl(destmaterial.def7,'~')<>'~' "
				+ "where nvl(srcmaterial.dr,0)=0 and nvl(destmaterial.dr,0)=0  "
				+ " and srcmaterial.code ='"+mateiralcode+"' "
				+ " and destmaterial.pk_org = '"+pk_org_dest+"' ";
		// 这里在找GXP集团物料号时，会存在问题，可能匹配结果会有多个，这时是无法精确匹配的，需要有来源组织，但目前现状OA没有限制库存组织进行物料限制，可能 会有15/16选到10的情况这时可能 还是会
		return (String) getDao().executeQuery(sql, new ColumnProcessor());
	}

	
	
	@Override
	public String updaterMdmMaterial(MaterialVO[] datas)
			throws BusinessException {
		// TODO Auto-generated method stub
		/*
		 * 1、数据检查，数据是否带有主数据编码
		 * 2、通过数据调用主数据主表查询，检查出哪些数据需要更新进主表的，
		 * 3、更新辅表数据。
		 */
		StringBuffer retMassge= new StringBuffer();
		if(null == datas || datas.length == 0){
			retMassge.append("未获取到需要更新的数据！");
		}
		Map<String,List<MaterialVO>> map = new HashMap<String,List<MaterialVO>>();
		List<MaterialVO> nomdmcode = new ArrayList<MaterialVO>();
		List<String> mdmcodes  = new ArrayList<String>();
		
		for(MaterialVO materialvo  : datas){
			String def7 = materialvo.getDef7();//主数据编码
			if(StringUtil.isEmpty(def7)){//如果主数据编码为空时存到 nomdmcode列表，用于后续返回
				nomdmcode.add(materialvo);
			}else{
				List<MaterialVO> list = map.get(def7);
				if(null  == list){
					list = new ArrayList<MaterialVO>();
				}
				list.add(materialvo);
				map.put(def7, list);
				mdmcodes.add(def7);
			}
		}
		if(map.size() == 0){
			 retMassge.append("没有可更新的数据！");
			 return retMassge.toString();
		}
		//获取主数据
		

			//查主数据主表中的物料是哪个
			List<Map<String, String>> queryMdmPrimary = queryListMdByMdmCodes(mdmcodes); //主数据系统主表数据
			MdmMaterialBatchUtil mdmMaterialBatchUtil = new MdmMaterialBatchUtil(queryMdmPrimary);
			 Map<String,List<MaterialVO>> update_mdm_h =  mdmMaterialBatchUtil.getRest_h(); //取需要更新到主表的数据
			 //取辅表数据执行更新
			 Map<String, List<MaterialVO>> update_mdm_b = mdmMaterialBatchUtil.getRest_b();
			
			//调用主表更新：批量更新，以组织为单位
			 this.batchInsetHostMaterial(update_mdm_h);
			//批量更新辅表数据
			 this.batchInsetAssistMaterial(update_mdm_b);
			
			


		

		return retMassge.toString();
	}
	
	
	public void batchInsetAssistMaterial(
			Map<String, List<MaterialVO>> update_mdm_b) throws BusinessException {
		
		if(null == update_mdm_b || update_mdm_b.size() == 0){
			//空数据直接返回
			return ;
		}
		//v1组织循环调用，(后续更新完善)或启用独立线程调用
		for(String org :update_mdm_b.keySet()){
			this.insetMdmMaterialHost(update_mdm_b.get(org).toArray(new MaterialVO[0]), org);
		}
	}

	/**
	 * 调用主表更新：批量更新，以组织为单位
	 * 同时辅表的数据也会被更新
	 * 主要用于已经升级过主数据的信息进行批量数据刷新
	 * @param update_mdm_h
	 * @throws BusinessException 
	 */
	public  void batchInsetHostMaterial(
			Map<String, List<MaterialVO>> update_mdm_h) throws BusinessException {
		if(null == update_mdm_h || update_mdm_h.size() == 0){
			//空数据直接返回
			return ;
		}
		//v1组织循环调用，(后续更新完善)或启用独立线程调用
		for(String org :update_mdm_h.keySet()){
			this.insetMdmMaterialHost(update_mdm_h.get(org).toArray(new MaterialVO[0]), org);
		}
		
	}

	public List<Map<String, String>> queryListMdByMdmCodes(
		 List<String> codes) throws BusinessException {
		if (codes == null || codes.size() == 0) {
			return null;
		}

		Request request = this.getMdmPost("/queryListMdByMdmCodes");
		JSONObject json = new JSONObject();
		List<Map<String, String>> retlist = new ArrayList<Map<String, String>>();


			try {
				json.put("systemCode", SYSCODE);
				json.put("gdCode", "GROUPMATERIAL");
				json.put("codes", codes);
				json.put("pageable", "false");
				json.put("pageIndex", 1);
				json.put("pageSize", 3000);
			} catch (Exception e) {
				throw new BusinessException("json组装时出现问题，导致查询失败！");
			}

			request.bodyString(json.toString(), ContentType.APPLICATION_JSON);
			HttpResponse httpResponse = null;

			try {
				httpResponse = request.execute().returnResponse();

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (200 != statusCode) {
					throw new BusinessException("主数据获取失败，请联系技术人员进行检查！");
				}
				// System.out.println(httpResponse.getStatusLine());
				if (httpResponse.getEntity() != null) {
					// StringEntity strEntity = (StringEntity)
					// httpResponse.getEntity();
					String html = EntityUtils.toString(
							httpResponse.getEntity(), "UTF-8");

					JSONObject jsonObject = JSON.parseObject(html);
					Boolean success = (Boolean) jsonObject.get("success");
					if (!success) {
						throw new BusinessException(
								jsonObject.getString("message"));
					}
					if (null == jsonObject.get("data")) {
						// return retlist;
						return null;
					}
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (null != jsonArray && jsonArray.size() > 0) {
						// retlist = new ArrayList<Map<String, String>>();

						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject object = (JSONObject) jsonArray.get(i);
							if (null == object
									|| "null".equals(object
											.getString("groupMarbascode"))) // 过滤空数据或者是集团统一分类为空的数据
								continue;
							// JSONObject jsonObject2 = new JSONObject(object);
							Map<String, String> rowData = new HashMap<String, String>();

							retlist.add(rowData);
							for (String key1 : SendMdmPropetys.bodyItems) {
								// if("null".equals(object.getString("groupMarbascode")))
								// break;
								rowData.put(key1, object.getString(key1));
							}
						}
					}

					// System.out.println(html);
					Logger.debug(html);
				}
			} catch (ClientProtocolException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (IOException e) {
				throw new BusinessException("接口调用异常，请联系技术人员处理！（检查接口通信）\r\n"
						+ e.getMessage());
			} catch (JSONException e) {
				throw new BusinessException("数据解析异常，请联系技术人员处理！\r\n"
						+ e.getMessage());
			}


	

		return retlist;
	}
	

}
