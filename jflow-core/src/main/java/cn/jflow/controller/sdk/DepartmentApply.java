package cn.jflow.controller.sdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.WF.Glo;
import BP.Web.WebUser;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/departmentApply")
public class DepartmentApply {
	
	/**
	 * 初始化表单
	 * @param model
	 * @param FK_Flow
	 * @param FK_Node
	 * @param WorkID
	 * @param FID
	 * @return
	 */
	@RequestMapping(value="/initLeave")
	public String initLeave(Model model,String FK_Flow,int FK_Node,int WorkID,int FID){
		
		Map<String,Object> map=new HashMap<String ,Object>();
		String sql="select * from leave_apply_tb where workId="+WorkID;
		DataTable dt=DBAccess.RunSQLReturnTable(sql);
		for(DataRow dr:dt.Rows){
			for(DataColumn dc:dt.Columns){
				map.put(dc.ColumnName, dr.getValue(dc));
			}
		}
		model.addAttribute("workID", WorkID);
		model.addAttribute("FK_Flow", FK_Flow);
		model.addAttribute("FK_Node", FK_Node);
		model.addAttribute("FID", FID);
		map.put("bm_user", WebUser.getName());
		model.addAttribute("map", map);
		
		return "WF/SDKFlow/bumenshenpi";
		
	}
	

	public PrintWriter out;
	
	/**
	 * 执行发送
	 * 
	 * @param sender
	 * @param e
	 */
	@RequestMapping(value = "/Btn_Send_Click", method = RequestMethod.POST)
	public void Btn_Send_Click(HttpServletRequest request,HttpServletResponse response,int workID,String FK_Flow,String bm_reason,String bm_user,String bm_date) {

		try {
			out=response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 第1步: 执行保存.
		new DepartmentApply().Btn_Save_Click(request,response,workID,bm_reason,bm_user,bm_date);

		// 检查完整性
		//用来检测请假天数是否合理

		// /#region 第2步: 执行发送.
		BP.WF.SendReturnObjs objs = null;
		try {
			// 调用发送api, 返回发送对象.
			objs = BP.WF.Dev2Interface.Node_SendWork(FK_Flow,workID);
		} catch (RuntimeException ex) {
			out.write("<font color=red size='3px'> 发送期间出现异常:" + ex.getMessage()+ "</font>");
			out.flush();
			out.close();
			return;
		}
		//
		// 这里注意：
		// * 1,发送api有多个, 根据不同的场景使用不同的api 但是常用的就那1个，您可以产看该参数使用说明.
		// * BP.WF.Node_SendWork(string fk_flow, Int64 workID, int toNodeID,
		// string toEmps)
		// * 2,回来的发送对象里面有系统变量，这些系统变量包括发送给谁了，发送到那里了.
		// * 开发人员可以根据系统变量,执行相关的业务逻辑操作.
		//
		// /#endregion 第2步: 执行发送.

		// /#region 第3步: 把发送信息提示出来.
		String info = objs.ToMsgOfHtml();
		info = info.replace("\t\n", "<br>@");
		info = info.replace("@", "<br>@");
		out.write("<font color=blue size='3px'>" + info + "</font>");
		out.flush();
		out.close();
	}
	
	/**
	 * 执行保存.
	 * @param sender
	 */
	@RequestMapping(value = "/Btn_Save_Click", method = RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request,HttpServletResponse response,int workID,String bm_reason,String bm_user,String bm_date) {

		String sql="update leave_apply_tb set bm_reason=:bm_reason,bm_user=:bm_user,bm_date=:bm_date where workID=:workID";
		Paras para=new Paras();
		para.Add("workID",workID);
		para.Add("bm_reason",bm_reason);
		para.Add("bm_user",bm_user);
		para.Add("bm_date",bm_date);
		DBAccess.RunSQL(sql, para);
		
	}
	/**
	 *  打开流程
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Btn_Track_Click", method = RequestMethod.POST)
	public final void Btn_Track_Click(HttpServletRequest request,HttpServletResponse response,String FK_Flow,int workID,int FID) {
		//BP.WF.Dev2Interface.UI_Window_OneWork(response,this.getFK_Flow(request),this.getWorkID(request), this.getFID(request));
	
		try {
			out = response.getWriter();
			out.print(Glo.getCCFlowAppPath() + "WF/WorkOpt/OneWork/Track.jsp?FK_Flow=" + FK_Flow + "&WorkID=" + workID + "&FID=" +FID);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	@RequestMapping(value = "/Btn_Return_Click", method = RequestMethod.POST)
	public void Btn_Return_Click(HttpServletRequest request,HttpServletResponse response,String FK_Flow,int FK_Node,int workID,int FID) {
		String url=BP.WF.Dev2Interface.UI_Window_Return(FK_Flow, FK_Node, workID, FID);
		try {
			out = response.getWriter();
			out.print(url);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
