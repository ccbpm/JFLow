package cn.jflow.controller.des;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import BP.Tools.StringHelper;
import BP.WF.Template.PushMsg;
import BP.WF.Template.PushMsgAttr;
import BP.WF.Template.PushWay;

@Controller
@RequestMapping(value="/DES")
public class ActionPush2SpecController extends BaseController{
	
	
	@RequestMapping(value="/ActionPush2SpecDelete",method=RequestMethod.POST)
	public void onDelete(HttpServletResponse response,String Event,String NodeID,String MyPK,String FK_Flow) throws IOException
	{
		PushMsg pm = new PushMsg();
		pm.Retrieve(PushMsgAttr.FK_Event, Event, PushMsgAttr.FK_Node, NodeID);
		pm.Delete();

		response.sendRedirect("ActionPush2Spec.jsp?NodeID="+NodeID+"&MyPK="+MyPK+"&Event="+Event+"&FK_Flow="+FK_Flow+"&tk="+new Random().nextInt());
	}

	@RequestMapping(value="/ActionPush2SpecSave",method=RequestMethod.POST)
	public String btn_Click(HttpServletRequest request,HttpServletResponse response,String Event,String NodeID,String FK_Flow,String MyPK,TempObject object) throws IOException
	{
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
		PushMsg pm = new PushMsg();
		pm.Retrieve(PushMsgAttr.FK_Event, Event, PushMsgAttr.FK_Node, NodeID);

		if (!StringHelper.isNullOrEmpty(pm.getMyPK()))
			pm.Delete();

		pm.setFK_Event(Event);
		pm.setFK_Node(Integer.parseInt(NodeID));

		DDL ddl = (DDL) controls.get("DDL_" + PushMsgAttr.PushWay);
		pm.setPushWay(ddl.getSelectedItemIntVal());
		pm.setPushDoc(null);
		
		int i=pm.getPushWay();
		
		if(i==PushWay.ByParas.getValue()){
			//#region 按照系统指定参数

			RadioButton rb = (RadioButton) controls.get("RB_0");

			if (rb.getChecked())
			{
				pm.setPushDoc("0");
				TextBox tb=(TextBox)controls.get("TB_" + PushMsgAttr.Tag);
				pm.setTag(tb.getText());
			}
			else
			{
				rb = (RadioButton) controls.get("RB_1");

				if (rb.getChecked())
					pm.setPushDoc("1");
				DDL ddl1=(DDL) controls.get("DDL_" + PushMsgAttr.Tag);
				pm.setTag(ddl1.getSelectedItemStringVal());
			}

		}
		else if(i==PushWay.NodeWorker.getValue()){
			//#region 按照指定结点的工作人员

			CheckBox cb = null;

			for(BaseWebControl ctrl:controls.values())
			{
				try {
					cb = (CheckBox)ctrl;
					if (cb == null || !cb.getId().startsWith("CB_") || !cb.getChecked()) continue;
					pm.setPushDoc(pm.getPushDoc()+ "@" + cb.getId().substring(3) + "@");
				} catch (Exception e) {
				}
			}
		}
		else if(i==PushWay.SpecDepts.getValue()){
			//#region 按照指定的部门

			for(BaseWebControl ctrl:controls.values())
			{
				CheckBox cb = (CheckBox)ctrl;
				if (cb == null || !cb.getId().startsWith("CB_") || !cb.getChecked()) continue;

				pm.setPushDoc(pm.getPushDoc()+"@" +cb.getId().substring(3) + "@");
			}
		}
		else if(i==PushWay.SpecEmps.getValue()){
			//#region 按照指定的人员

			TextBox hid = (TextBox) controls.get("HID_Users");

			if(!StringHelper.isNullOrEmpty(hid.getText()))
			{
//				pm.PushDoc = hid.Value.Split(',').Select(o => "@" + o + "@").Aggregate(string.Empty,
//						(curr, next) =>
//				curr + next);
			}
			//foreach (var ctrl in Pub1.Controls)
			//{
			//    cb = ctrl as CheckBox;
			//    if (cb == null || !cb.ID.StartsWith("CB_E_") || !cb.Checked) continue;

			//    pm.PushDoc += "@" + cb.ID.substing(5) + "@";
			//}

		}
		else if(i==PushWay.SpecSQL.getValue()){
			//#region 按照指定的SQL查询语句
			TextBox t=(TextBox) controls.get("TB_" + PushMsgAttr.PushDoc);
			pm.setPushDoc(t.getText());
		}
		else if(i==PushWay.SpecStations.getValue()){
			//#region 按照指定的岗位

			for(BaseWebControl ctrl:controls.values())
			{
				CheckBox cb = (CheckBox)ctrl;
				if (cb == null || !cb.getId().startsWith("CB_S_") || !cb.getChecked()) continue;

				pm.setPushDoc("@" + cb.getId().substring(5) + "@");
			}
		}
		


		pm.Save();
		return "redirect:ActionPush2Spec.jsp?NodeID="+NodeID+"&MyPK="+MyPK+"&Event="+Event+"&FK_Flow="+FK_Flow+"&tk="+new Random().nextInt();
	}

//	public void ddl_SelectedIndexChanged(HttpServletResponse response,String NodeID,String MyPK,String FK_Flow,String ThePushWay,S)
//	{
//		Response.Redirect(string.Format("ActionPush2Spec.jsp?NodeID={0}&MyPK={1}&Event={2}&FK_Flow={3}&ThePushWay={4}&tk={5}", NodeID, MyPK, Event,
//				FK_Flow, (sender as DDL).SelectedItemIntVal, new Random().NextDouble()), true);
//	}
	
}
