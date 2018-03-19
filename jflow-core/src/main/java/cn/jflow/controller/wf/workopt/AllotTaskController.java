package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Work;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerList;
import BP.WF.Entity.GenerWorkerListAttr;
import BP.WF.Entity.GenerWorkerLists;
import BP.WF.Entity.RememberMe;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.CheckBox;

@Controller
@RequestMapping("/WF/WorkOpt")
public class AllotTaskController extends BaseController
{
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	/** 
	 IsFHL
	 
	*/
	public final boolean getIsFHL()
	{
		if (this.getWorkID() == this.getFID())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static  void addCheckBox(GenerWorkerList wl,StringBuilder html)
	{
		if(!WebUser.getNo().equals(wl.getFK_Emp()))
		{
			String idAndName = "CB_" + wl.getFK_Emp();
			CheckBox cb = new CheckBox();
			cb.setId(idAndName);
			cb.setName(idAndName);
			if(wl.getIsEnable()){
				cb.setChecked(true);
			}
			cb.setText(BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText()));
			html.append(cb.toString());
		}
		//html.append("<input id=\""+idAndName+"\" name='"+idAndName+"' type=checkbox checked="+wl.getIsEnable()+"/>");
		//html.append("<label for=\""+idAndName+"\">"+BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText())+"</label>");
//		html.append("</li>");
	}
	public static void addCheckBox(StringBuilder html)
	{
	
		String idAndName = "seleall";
		CheckBox cb = new CheckBox();
		cb.setId(idAndName);
		cb.setName(idAndName);
		cb.setChecked(true);
		cb.setText("全部选择");
		cb.attributes.put("onclick", "SetSelected(this)");
		html.append(cb.toString());
//		html.append("<input id=\""+idAndName+"\" name='"+idAndName+
//				"' type=\"checkbox\" checked" +
//				" onclick='"+"SetSelected(this,'" + ids + "')"+
//				" >"+"选择全部"+"</>");
//		html.append("<label for=\""+idAndName+"\">"+"全部选择"+"</label>");

	}	
	public static void addButton(StringBuilder html, String cb_ids)
	{

		String idAndName = "Btn_Do";
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' id=\""+idAndName+"\" name='"+idAndName+"' value=\""+"  确定  "+"\" class=\"am-btn am-btn-primary am-btn-xs\" onclick='checkSubmit(\""+cb_ids+"\")'/>");
	}

	@RequestMapping(value = "/AllotTask", method = RequestMethod.POST)
	protected final void execute(HttpServletRequest request,
			HttpServletResponse response)
	{
		_request = request;
		_response = response;

		this.Confirm();
	}

	/** 
	 确定窗口
	 
	*/
	public final void Confirm()
	{
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(), this.getNodeID(), true);
		try
		{
//			boolean isHave0 = true;
//			for (GenerWorkerList wl : wls.ToJavaList())
//			{
//				boolean cbChecked = _request.getParameter("CB_" + wl.getFK_Emp())==null?false:Boolean.valueOf(_request.getParameter("CB_" + wl.getFK_Emp()));
//				if (cbChecked)
//				{
//					isHave0 = false;
//					break;
//				}
//			}
//
//			if (isHave0)
//			{
//				try {
//					printAlert(_response, "当前工作中你没有分配给任何人，此工作将不能被其他人所执行！");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return;
//			}

			for (GenerWorkerList wl : wls.ToJavaList())
			{
				boolean cbChecked = false;
				String cb = _request.getParameter("CB_" + wl.getFK_Emp());
				if(null != cb){
					if(cb.equals("1") || cb.equals("on")){
						cbChecked = true;
					}
				}
				
				if (wl.getIsEnable() != cbChecked)
				{
					wl.setIsEnable(cbChecked);
					wl.Update();
				}
			}

			RememberMe rm = new RememberMe();
			rm.setFK_Emp(WebUser.getNo());
			rm.setFK_Node(getNodeID());
			rm.setObjs("@");
			rm.setObjsExt("");

			for (GenerWorkerList wl : wls.ToJavaList())
			{
				if (!wl.getIsEnable())
				{
					continue;
				}

				rm.setObjs(rm.getObjs()+ wl.getFK_Emp() + "@");
				rm.setObjsExt(rm.getObjsExt()+wl.getFK_EmpText() + "&nbsp;&nbsp;");
			}

			rm.setEmps("@");
			rm.setEmpsExt("");

			for (GenerWorkerList wl : wls.ToJavaList())
			{
				if(!WebUser.getNo().equals(wl.getFK_Emp()))
				{
					rm.setEmps(rm.getEmps()+wl.getFK_Emp() + "@");
	
					String empInfo = Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText());
					if (rm.getObjs().indexOf(wl.getFK_Emp()) != -1)
					{
						rm.setEmpsExt(rm.getEmpsExt()+ "<font color=green>" + BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText()) + "</font>&nbsp;&nbsp;");
					}
					else
					{
						rm.setEmpsExt(rm.getEmpsExt()+"<strike><font color=red>(" + BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText()) + "</font></strike>&nbsp;&nbsp;");
					}
				}
			}
			rm.Save();

			if (WebUser.getIsWap())
			{
				StringBuilder builder = new StringBuilder();
				
				builder.append("<br>&nbsp;&nbsp;任务分配成功，特别提示：当下一次流程发送时系统会按照您设置的路径进行智能投递。");
				builder.append(BaseModel.AddUL());
				builder.append(BaseModel.AddLi("<a href='./WAP/Home.jsp' ><img src='/WF/Img/Home.gif' border=0/>主页</a>"));
				builder.append(BaseModel.AddLi("<a href='./WAP/Start.jsp' ><img src='/WF/Img/Start.gif' border=0/>发起</a>"));
				builder.append(BaseModel.AddLi("<a href='./WAP/Runing.jsp' ><img src='/WF/Img/Runing.gif' border=0/>待办</a>"));
				builder.append(BaseModel.AddULEnd());
				_request.getSession().setAttribute("info", builder.toString());
				try {
					_response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Comm/Port/InfoPage.jsp");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				Glo.ToMsg("任务分配成功。", _response);
				//this.WinCloseWithMsg("任务分配成功。");
			}
		}
		catch (RuntimeException ex)
		{
			//this.Response.Write(ex.getMessage());
			//Log.DebugWriteWarning(ex.getMessage());
			Glo.ToMsg("任务分配出错：" + ex.getMessage(), _response);
		}
	}

	public final void DealWithFHLFlow(ArrayList al, GenerWorkerLists wlSeles)
	{
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Retrieve(GenerWorkerListAttr.FID, this.getFID());

		DBAccess.RunSQL("UPDATE  WF_GenerWorkerlist SET IsEnable=0  WHERE FID=" + this.getFID());

		String emps = "";
		String myemp = "";
		for (Object obj : al)
		{
			emps += obj.toString() + ",";
			myemp = obj.toString();
			DBAccess.RunSQL("UPDATE  WF_GenerWorkerlist SET IsEnable=1  WHERE FID=" + this.getFID() + " AND FK_Emp='" + obj + "'");
		}

		//BP.WF.Node nd = new BP.WF.Node(NodeID);
		//Work wk = nd.HisWork;
		//wk.OID = this.WorkID;
		//wk.Retrieve();
		//wk.Emps = emps;
		//wk.Update();
	}

	public final void DealWithPanelFlow(ArrayList al, GenerWorkerLists wlSeles)
	{
		// 删除当前非配的工作。
		// 已经非配或者自动分配的任务。
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		int NodeID = gwf.getFK_Node();
		long workId = this.getWorkID();
		//GenerWorkerLists wls = new GenerWorkerLists(this.WorkID,NodeID);
		DBAccess.RunSQL("UPDATE  WF_GenerWorkerlist SET IsEnable=0  WHERE WorkID=" + this.getWorkID() + " AND FK_Node=" + NodeID);
		//  string vals = "";
		String emps = "";
		String myemp = "";
		for (Object obj : al)
		{
			emps += obj.toString() + ",";
			myemp = obj.toString();
			DBAccess.RunSQL("UPDATE  WF_GenerWorkerlist SET IsEnable=1  WHERE WorkID=" + this.getWorkID() + " AND FK_Node=" + NodeID + " AND fk_emp='" + obj + "'");
		}

		Node nd = new Node(NodeID);
		Work wk = nd.getHisWork();

		wk.setOID(this.getWorkID());
		wk.Retrieve();

		wk.setEmps(emps);
		wk.Update();
	}
}
