package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.WF.DelWorkFlowRole;
import BP.WF.Node;
import BP.WF.Template.BtnAttr;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class DeleteWorkFlowModel extends BaseModel {

	public StringBuilder ToolBar1 = null;
	public StringBuilder Pub1 = null;

	public DeleteWorkFlowModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		ToolBar1 = new StringBuilder();
		Pub1 = new StringBuilder();
	}

	public  void init() {
		DDL ddl = new DDL();
		Node nd = new Node(this.getFK_Node());
		this.ToolBar1.append("<b>删除方式:</b>");
		ddl.setId("DDL1");
		ddl.setName("DDL1");
		ddl.addAttr("onchange", "OnChange(this);");
		Button btn_ok = new Button();
		btn_ok.setId("Btn_OK");
		btn_ok.setName("Btn_OK");
		btn_ok.setText("确定");
		btn_ok.setType("submit");
		btn_ok.addAttr("onclick", " document.getElementById('BtnID').value='Btn_OK';" +
				"return confirm('您确定要执行吗?');");
		Button btn_cancel = new Button();
		btn_cancel.setId("Btn_Cancel");
		btn_cancel.setName("Btn_Cancel");
		btn_cancel.setText("取消");
		btn_cancel.setType("submit");
		btn_cancel.addAttr("onclick", " document.getElementById('BtnID').value='Btn_Cancel';");
		TextBox tb = new TextBox();
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setId("TB_Doc");
		tb.setRows(15);
		tb.setCols(50);
		this.Pub1.append(tb.toString());
		
		// 彻底删除
		if(nd.getHisDelWorkFlowRole() == DelWorkFlowRole.DeleteReal){
			SysEnum se = new SysEnum(BtnAttr.DelEnable,
					DelWorkFlowRole.DeleteReal.ordinal());
			ddl.Items.add(new ListItem(se.getLab(), String.valueOf(se
					.getIntKey())));
		}
			
		if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.DeleteAndWriteToLog) {
			// 删除并记录日志
			SysEnum se = new SysEnum(BtnAttr.DelEnable,
					DelWorkFlowRole.DeleteAndWriteToLog.ordinal());
			ddl.Items.add(new ListItem(se.getLab(), String.valueOf(se
					.getIntKey())));
		}

		if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.DeleteByFlag) {
			// 逻辑删除
			SysEnum se = new SysEnum(BtnAttr.DelEnable,
					DelWorkFlowRole.DeleteByFlag.ordinal());
			ddl.Items.add(new ListItem(se.getLab(), String.valueOf(se
					.getIntKey())));
		}

		if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.ByUser) {
			// 让用户来决定.
			SysEnums ses = new SysEnums(BtnAttr.DelEnable);
			for (Object se : ses) {
				DelWorkFlowRole role = DelWorkFlowRole.forValue(((SysEnum) se)
						.getIntKey());
				if (role == DelWorkFlowRole.None) {
					continue;
				}
				if (role == DelWorkFlowRole.ByUser) {
					continue;
				}
				ddl.Items.add(new ListItem(((SysEnum) se).getLab(), String
						.valueOf(((SysEnum) se).getIntKey())));
			}
		}
		this.ToolBar1.append(ddl.toString());
		this.ToolBar1.append(btn_ok.toString());
		this.ToolBar1.append(btn_cancel.toString());

	}
	// private void ReturnWork_Click(Object sender, EventArgs e)
	// {
	// Button btn = (Button)((sender instanceof Button) ? sender : null);
	// if (btn.ID.equals("Btn_Cancel"))
	// {
	// this.Response.Redirect("MyFlow" + BP.WF.Glo.FromPageType +
	// ".aspx?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() +
	// "&FK_Node=" + this.getFK_Node(), true);
	// return;
	// }
	// else
	// {
	// }
	//
	// try
	// {
	// String info = this.getTB1().getText();
	// BP.WF.DelWorkFlowRole role =
	// (BP.WF.DelWorkFlowRole)this.DDL1.SelectedItemIntVal;
	// String rInfo = "";
	// switch (role)
	// {
	// case DelWorkFlowRole.DeleteAndWriteToLog:
	// rInfo=BP.WF.Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(),
	// this.getWorkID(), info, true);
	// break;
	// case DelWorkFlowRole.DeleteByFlag:
	// rInfo = BP.WF.Dev2Interface.Flow_DoDeleteFlowByFlag(this.getFK_Flow(),
	// this.getWorkID(), info, true);
	// break;
	// case DelWorkFlowRole.DeleteReal:
	// rInfo = BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(),
	// this.getWorkID(), true);
	// break;
	// default:
	// throw new RuntimeException("@没有涉及到的删除情况。");
	// }
	// this.ToMsg(rInfo, "info");
	// }
	// catch (RuntimeException ex)
	// {
	// this.ToMsg(ex.getMessage(), "info");
	// }
	// }
	// public final void ToMsg(String msg, String type)
	// {
	// this.Session["info"] = msg;
	// this.Response.Redirect("MyFlowInfo" + BP.WF.Glo.FromPageType +
	// ".aspx?FK_Flow=" + this.getFK_Flow() + "&FK_Type=" + type + "&FK_Node=" +
	// this.getFK_Node() + "&WorkID=" + this.getWorkID(), false);
	// }
}
