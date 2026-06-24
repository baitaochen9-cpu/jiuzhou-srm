package nc.ui.qc.supplierqualitystatus.ace.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.SwingConstants;

import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.calendar.UICalendar;
import nc.vo.pub.lang.UFDate;

/**
 * 质量市场选择框
 * @author yinsen.zhang
 *
 */
public class QualityMarketDialog extends UIDialog implements ActionListener {

	public QualityMarketDialog(String pk_org) {
		initDialog(pk_org);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("sure")){
			this.closeOK();
		}else{
			this.close();
		}
	}

	// 质量市场
	private UIRefPane qualityMarketRefPane;

	public UIRefPane getQualityMarketRefPane() {
		return qualityMarketRefPane;
	}

	public void setQualityMarketRefPane(UIRefPane qualityMarketRefPane) {
		this.qualityMarketRefPane = qualityMarketRefPane;
	}

	private void initDialog(String pk_org){
		this.setName("质量市场");
		this.setTitle(this.getName());
		this.setSize(400,180);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		
		UIPanel panel=new UIPanel(); 
		panel.setBackground(null);
		panel.setPreferredSize(new Dimension(380, 170));
		
		Font font=new Font(Font.DIALOG, Font.BOLD, 15);
		
		UILabel qualityMarketLabel=new UILabel("质量市场：", UILabel.CENTER);
		qualityMarketLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//宽，高
		qualityMarketLabel.setPreferredSize(new Dimension(150, 20));
		qualityMarketLabel.setFont(font);
		
		qualityMarketRefPane=new UIRefPane("质量市场(自定义档案)");
//		qualityMarketRefPane.setValue(new UFDate().toLocalString());
		qualityMarketRefPane.setPreferredSize(new Dimension(150, 50));
		qualityMarketRefPane.setIsBatchData(true);
		qualityMarketRefPane.setMultiSelectedEnabled(true);
		qualityMarketRefPane.setPk_org(pk_org);
//		qualityMarketRefPane.setIsCustomDefined(true);
		
		panel.add(qualityMarketLabel);
		panel.add(qualityMarketRefPane);
		
		nc.ui.pub.beans.UIButton sure_btn=new nc.ui.pub.beans.UIButton("确  定");
		sure_btn.addActionListener(this);
		sure_btn.setPreferredSize(new Dimension(70, 23));
		sure_btn.setActionCommand("sure");
		sure_btn.setFont(font);
		
		nc.ui.pub.beans.UIButton esc_btn=new nc.ui.pub.beans.UIButton("取  消");
		esc_btn.addActionListener(this);
		esc_btn.setPreferredSize(new Dimension(70, 23));
		esc_btn.setActionCommand("esc");
		esc_btn.setFont(font);
		
		UIPanel btn_panel=new UIPanel(new FlowLayout(FlowLayout.CENTER));
		btn_panel.setBackground(null);
		btn_panel.setPreferredSize(new Dimension(150, 50));
		btn_panel.add(esc_btn);
		btn_panel.add(sure_btn);
		
		panel.add(btn_panel);
		
		this.add(panel);
	}

}
