package nc.ui.cm.costrevert.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.cm.costrevert.view.CostRevertPanel;
import nc.ui.cmpub.business.util.CMActionProgressor;
import nc.ui.uif2.NCAction;
import nc.vo.cm.costrevert.CMMLangConstCostRevert;
import nc.vo.uif2.LoginContext;

/**
 * 成本还原的Action
 *
 * @since v6.3
 * @version 2014-8-19 下午3:43:09
 * @author dingyma
 */
public class CostRevertAction extends NCAction {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private LoginContext context;

    public LoginContext getContext() {
        return this.context;
    }

    public void setContext(LoginContext context) {
        this.context = context;
    }

    /**
     * 还原面板
     */
    private CostRevertPanel mainPanel;

    /**
     * 获得 mainPanel 的属性值
     *
     * @return the mainPanel
     * @since 2014-8-19
     * @author dingyma
     */
    public CostRevertPanel getMainPanel() {
        return this.mainPanel;
    }

    /**
     * 设置 mainPanel 的属性值
     *
     * @param mainPanel the mainPanel to set
     * @since 2014-8-19
     * @author dingyma
     */
    public void setMainPanel(CostRevertPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    /**
     * Constructor
     */
    public CostRevertAction() {
        this.setCode("CostRevertAction");
        this.setBtnName(CMMLangConstCostRevert.getBTN_NAME_REVERT());
        this.putValue(Action.SHORT_DESCRIPTION, CMMLangConstCostRevert.getBTN_NAME_REVERT());
    }

    /**
     * 还原按钮
     *
     * @throws Exception
     */
    public void revert() throws Exception {
        this.getMainPanel().onCostRevert();
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        this.getMainPanel().getTopPanel().stopEditing();
        CMActionProgressor handler = new CMActionProgressor(this, "revert");
        handler.process(CMMLangConstCostRevert.getREVERT_STATUSBAR(), this.getContext());

    }
}
