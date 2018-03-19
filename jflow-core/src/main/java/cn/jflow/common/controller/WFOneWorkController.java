package cn.jflow.common.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.WF.ActionType;
import BP.WF.SMS;
import BP.WF.SMSAttr;
import BP.WF.Track;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/WorkOpt/OneWork")
public class WFOneWorkController extends BaseController {
	HttpServletResponse response;
	HttpServletRequest request;
      public String getUserName(){
    	  String str=getRequest().getParameter("UserName");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
      public String getMsg(){
    	  String str=getRequest().getParameter("TB_Msg");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
      public String getTitle(){
    	  String str=getRequest().getParameter("Title");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
	@RequestMapping(value = "/FlowBBS.do", method = RequestMethod.POST)
	protected void execute(HttpServletRequest request,HttpServletResponse response) {		
		String msg = "";
		PrintWriter out = null;
		try {
			if ("FlowBBSList".equals(getDoType())) {// 初始化流程列表
				msg = this.FlowBBSList(super.getFK_Flow());
			} else if ("FlowBBSDept".equals(getDoType())) {// 获取每个人的部门信息
				msg = this.FlowBBSDept(this.getUserName());
			} else if ("FlowBBSCheck".equals(getDoType())) {// 查看某一用户的所有评论
				msg = this.FlowBBSCheck(super.getFK_Flow(), this.getUserName());
			} else if ("FlowBBSCount".equals(getDoType())) {// 统计评论的数量
				msg = this.FlowBBSCount(super.getFK_Flow());
			} else if ("FlowBBSDelete".equals(getDoType())) {// 删除评论
				msg = BP.WF.Dev2Interface.Flow_BBSDelete(super.getFK_Flow(), super.getMyPK(),
						BP.Web.WebUser.getNo());
			} else if ("FlowBBSUserName".equals(getDoType())) {// 获取当前登录用户的Name
				msg = this.FlowBBSUserName();
			} else if ("FlowBBSReplay".equals(getDoType())) {// 评论回复
				msg = this.FlowBBSReplay(this.getUserName(), this.getMsg(), this.getTitle());
			} else if ("FlowBBSSave".equals(getDoType())) {// 保存提交评论
				msg = this.FlowBBSSave(this.getMsg(), super.getFK_Flow(),super.getWorkID());
			} else {
				msg = "err@没有判断的标记:" + getDoType();
			}
			out = response.getWriter();
			out.write(msg);
		} catch (Exception ex) {
			msg = "err@" + ex.getMessage();
		}
	}

	public String FlowBBSList(String FK_Flow) {
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(FK_Flow) + "Track WHERE ActionType="
				+BP.WF.ActionType.FlowBBS.getValue()+" AND WORKID="+super.getWorkID();
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));

	}

	public String FlowBBSDept(String UserName) {
		Paras ps = new Paras();
		ps.SQL = "select a.name from port_dept a INNER join port_emp b on b.FK_Dept=a.no and b.name='"
				+ UserName + "'";
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnString(ps));
	}

	public String FlowBBSCount(String FK_Flow) {
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(ActionType) FROM ND" + Integer.parseInt(FK_Flow)
				+ "Track WHERE ActionType="
				+ BP.WF.ActionType.FlowBBS.getValue()+" AND WORKID="+super.getWorkID();   
		String count = BP.DA.DBAccess.RunSQLReturnString(ps).toString();
		return count;
	}

	public String FlowBBSCheck(String FK_Flow, String UserName) {
		Paras pss = new Paras();
		pss.SQL = "SELECT * FROM ND" + Integer.parseInt(FK_Flow) + "Track WHERE ActionType="
				+ BP.Sys.SystemConfig.getAppCenterDBVarStr()
				+ "ActionType AND  EMPFROMT='" + UserName + "'"+" AND WORKID="+super.getWorkID();
		pss.Add("ActionType", BP.WF.ActionType.FlowBBS.getValue());
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(pss));

	}

	public String FlowBBSUserName() {
		String name = "";
		name = BP.Web.WebUser.getName();
		return name;

	}

	public String FlowBBSReplay(String UserName, String msg, String title) {
		SMS sms = new SMS();
		sms.RetrieveByAttr(SMSAttr.MyPK, getMyPK());
		sms.setMyPK(DBAccess.GenerGUID());
		sms.setRDT(DataType.getCurrentDataTime());
		sms.setSendToEmpNo(UserName);
		sms.setSender(WebUser.getNo());
		sms.setTitle(title);
		sms.setDocOfEmail(msg);
		sms.Insert();
		return null;
	}

	public String FlowBBSSave(String Msg, String FK_Flow, long WorkID) {
		int FID = 0;
		String mypk = AddToTrack(ActionType.FlowBBS, FK_Flow, WorkID, FID,
				WebUser.getNo(), WebUser.getName(), 0, null, WebUser.getNo(),
				WebUser.getName(), Msg, null);
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(FK_Flow) + "Track WHERE MyPK="
				+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "MyPK";
		ps.Add("MyPK", mypk);
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));
	}

	public static String AddToTrack(ActionType at, String flowNo, long WorkID,
			long fid, String fromEmpID, String fromEmpName, int toNodeID,
			String toNodeName, String toEmpID, String toEmpName, String note,
			String tag) {
		Track t = new Track();
		t.setWorkID(WorkID);
		t.setFID(fid);
		t.setRDT(DataType.getCurrentDataTime());
		t.setHisActionType(at);
		t.setEmpFrom(fromEmpID);
		t.setEmpFromT(fromEmpName);
		t.FK_Flow = flowNo;
		t.setNDTo(toNodeID);
		t.setNDToT(toNodeName);
		t.setEmpTo(toEmpID);
		t.setEmpToT(toEmpName);
		t.setMsg(note);
		if (tag != null) {
			t.setTag(tag);
		}
		try {
			t.Insert();
		} catch (java.lang.Exception e) {
			t.CheckPhysicsTable();
			t.Insert();
		}
		return t.getMyPK();
	}
}
