package nc.ui.ic.m4460;

import java.awt.event.ActionEvent;

import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.pub.BusinessException;

/**
 * 不合格状态标签打印按钮
 * 
 * @author zhw
 * 
 */
public class UnQualifiedStateLabelPrintAction extends StateLabelPrintAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811961090975795901L;

	public void initPrintInfo() {
		setPreview(false);
		setNodeKey("ot");
		setFuncode("H3010200408");
		setTranstype("JZ01-Cxx-45");
		super.setCode("unStateMarkLabel");
		super.setBtnName("拒绝标签打印");
		setActioncode("unStateMarkLabel");
		setActionname(getBtnName());
		putValue("ShortDescription", getActionname());
		putValue("AcceleratorKey", null);

	}

	public void doAction(ActionEvent e) throws Exception {
		super.doAction(e);
	}
	
	protected void checkState(StoreStateVO statevo) throws BusinessException{
		if (!"buhege".equals(statevo.getVcode())){
			throw new BusinessException("调整状态不是不合格状态，请选状态标签打印");
		}
	}

}