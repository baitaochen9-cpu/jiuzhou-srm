package nc.ui.uif2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.busilog.vo.BusiLogConvert;
import nc.bs.busilog.vo.BusinessLogAsynInfo;
import nc.bs.busilog.vo.BusinessLogContext;
import nc.bs.busilog.vo.BusinessLogESVO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.md.MDBaseQueryFacade;
import nc.md.MDBizQueryFacade;
import nc.md.model.IBean;
import nc.md.model.IBusiOperation;
import nc.md.model.MetaDataException;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.vo.bd.accessor.IBDData;
import nc.vo.bd.meta.GeneralBDObjectAdapterFactory;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.IBDObjectAdapterFactory;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.MultiLangText;

import org.apache.commons.lang.ArrayUtils;

public class BusiLogSave {

	 private IGeneralAccessor accessor = null;

	public List<BusinessLogESVO> writeModefyBusiLog(String metaDataID,
			SuperVO vo, SuperVO oldVO) throws BusinessException {
		
		IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(vo.getClass().getName());
		return writeModefyBusiLog(bean.getID(), new SuperVO[] { vo },
				new SuperVO[] { oldVO });
	}

	public List<BusinessLogESVO> writeModefyBusiLog(String metaDataID,
			SuperVO[] vos, SuperVO[] oldVOs) throws BusinessException {
		if ((vos == null) || (oldVOs.length == 0)) {
			return null;
		}

		try {
			List<BusinessLogContext> list = createBusinessLogContexts(
					metaDataID, vos, oldVOs);

			return insertBatchBusiLogAsynch(list);
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		return null;
	}

	public List<BusinessLogESVO> insertBatchBusiLogAsynch(
			List<BusinessLogContext> listvo) throws BusinessException {
		List<BusinessLogAsynInfo> listloginfo = new ArrayList();
		if (listvo != null) {
			try {
				listloginfo.add(BusiLogConvert
						.convert2BusinessLogAsynInfo(listvo));
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}
		}

		return synBusilog2Queue(listloginfo);
	}

	protected BusinessLogContext createBusinessLogContext(String metaDataID,
			SuperVO vo, SuperVO oldVo) throws BusinessException {
		String operationID = getBusiOperationByName(metaDataID, "edit");
		IBDData bd = getAccessor().getDocByPk((String)vo.getAttributeValue("pk_material"));
		BusinessLogContext logcontext = new BusinessLogContext();
		logcontext.setTypepk_busiobj(metaDataID);
		    logcontext.setPk_operation(operationID);
		     logcontext.setPk_busiobj(vo.getPrimaryKey());
		    logcontext.setBusiobjcode(bd.getCode());
	    logcontext.setBusiobjname(bd.getName() == null ? null : bd.getName().toString());
	    
		    logcontext.setOrgpk_busiobj((String)vo.getAttributeValue("pk_org"));
	     
		    logcontext.setBusiobjvo(vo);
		     logcontext.setOldbusiobjvo(oldVo);

		return logcontext;
	}

	protected List<BusinessLogContext> createBusinessLogContexts(
			String metaDataID, SuperVO[] vos, SuperVO[] oldVOs)
			throws BusinessException {
		List<BusinessLogContext> list = new ArrayList();
		if (!ArrayUtils.isEmpty(vos)) {
			for (int i = 0; i < vos.length; i++) {
				list.add(createBusinessLogContext(metaDataID, vos[i],
						oldVOs == null ? null : oldVOs[i]));
			}
		}

		return list;
	}

	public List<BusinessLogESVO> synBusilog2Queue(List<BusinessLogAsynInfo> list)
			throws BusinessException {
		if (list == null) {
			return null;
		}
		List<BusinessLogAsynInfo> list2 = new ArrayList();

		for (BusinessLogAsynInfo ainfo : list) {
			if (ainfo.getContext() != null) {
				List<BusinessLogContext> listcontext = new ArrayList();
				for (BusinessLogContext context : ainfo.getContext()) {
					listcontext.add(context);
				}
				if (listcontext.size() > 0) {
					ainfo.setContext(listcontext);
					list2.add(ainfo);
				}
			}
		}
		List<BusinessLogESVO> blist = convert2AsynSaveVOMap(list2);
		return blist;
	}

	private List<BusinessLogESVO> convert2AsynSaveVOMap(
			List<BusinessLogAsynInfo> listasyninfo) {
		if (listasyninfo == null)
			return null;
		HashMap<String, HashMap<String, List<BusinessLogESVO>>> vomap = new HashMap();
		List<BusinessLogESVO> listvo = new ArrayList();
		for (BusinessLogAsynInfo info : listasyninfo) {
			InvocationInfoProxy.getInstance().setUserDataSource(
					info.getDatasource());

			List<BusinessLogESVO> listbslogvo1 = null;
			try {
				listbslogvo1 = BusiLogVOConvert.convert2BusinessLogVO(info);
			} catch (Exception e) {
				Logger.error("异步写日志,转换BusinessLogAsynInfo为vo错误:"
						+ e.getMessage());
			}

			listvo.addAll(listbslogvo1);
		}
		return listvo;
	}

	public IGeneralAccessor getAccessor() {
		/* 61 */     if (accessor == null) {
		/* 62 */       accessor = GeneralAccessorFactory.getAccessor("c7dc0ccd-8872-4eee-8882-160e8f49dfad");
		/*    */     }
		/* 64 */     return accessor;
		/*    */   }

	private String getBusiOperationByName(String metaDataID, String operateName)
			throws MetaDataException {
		List<IBusiOperation> operations = MDBizQueryFacade.getInstance()
				.getBusiOperationByBeanID(metaDataID);

		if (operations != null) {
			for (IBusiOperation iBusiOperation : operations) {
				if (iBusiOperation.getName().equals(operateName)) {
					return iBusiOperation.getID();
				}
			}
		}
		return null;
	}
}
