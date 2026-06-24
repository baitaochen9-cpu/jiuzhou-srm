package nc.ui.riasm.electronicsignature.action;

import nc.ui.pubapp.uif2app.actions.BodyDelLineAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.IEditor;
import nc.ui.uif2.model.AbstractAppModel;
import nc.uif2.annoations.MethodType;
import nc.uif2.annoations.ModelMethod;
import nc.uif2.annoations.ModelType;

public class DelLineAction extends BodyDelLineAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5114311306092031992L;
	protected AbstractAppModel model = null;
	private IEditor editor;

	@Override
	protected boolean isActionEnable() {
		return model.getUiState() != UIState.NOT_EDIT;
	}

	@ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.GETTER)
	public AbstractAppModel getModel() {
		return model;
	}

	@ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.SETTER)
	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public IEditor getEditor() {
		return editor;
	}

	public void setEditor(IEditor editor) {
		this.editor = editor;
	}
	
}
