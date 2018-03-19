package cn.jflow.controller.sdk;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.Paras;
import BP.WF.Glo;
import BP.Web.WebUser;

/**
 * 填写请假申请单
 * 
 * 初始化
 * 
 * 发送
 * 
 * 保存
 * 
 * 轨迹
 * 
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/leaveFlow")
public class LeaveApplyController {
	
	
	@RequestMapping(value="/initLeave")
	public String initLeave(Model model,String FK_Flow,int FK_Node,int WorkID,int FID){
		
		String sql="insert into leave_apply_tb(workid) values("+WorkID+")";
		DBAccess.RunSQL(sql);
		model.addAttribute("workID", WorkID);
		model.addAttribute("FK_Flow", FK_Flow);
		model.addAttribute("FK_Node", FK_Node);
		model.addAttribute("FID", FID);
		model.addAttribute("start_user", WebUser.getName());
		return "WF/SDKFlow/ceshi";
		
	}
	

	public PrintWriter out;
	
	/**
	 * 执行发送
	 * 
	 * @param sender
	 * @param e
	 */
	@RequestMapping(value = "/Btn_Send_Click", method = RequestMethod.POST)
	public void Btn_Send_Click(HttpServletRequest request,HttpServletResponse response,int workID,String FK_Flow,String start_user,String start_date,int leave_type,String leave_date,String leave_to_date,String leave_date_count,String leave_reason) {

		try {
			response.setCharacterEncoding("utf-8");
			out=response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 第1步: 执行保存.
		new LeaveApplyController().Btn_Save_Click(request,response,workID,FK_Flow,start_user,start_date,leave_type,leave_date,leave_to_date,leave_date_count,leave_reason);

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
	public void Btn_Save_Click(HttpServletRequest request,HttpServletResponse response,int workID,String FK_Flow,String start_user,String start_date,int leave_type,String leave_date,String leave_to_date,String leave_date_count,String leave_reason) {

		String sql="update leave_apply_tb set start_user=:start_user,start_date=:start_date,leave_type=:leave_type,leave_date=:leave_date,leave_to_date=:leave_to_date,leave_date_count=:leave_date_count,leave_reason=:leave_reason where workID=:workID";
		Paras para=new Paras();
		para.Add("workID",workID);
		para.Add("start_user",start_user);
		para.Add("start_date",start_date);
		para.Add("leave_type",leave_type);
		para.Add("leave_date",leave_date);
		para.Add("leave_to_date",leave_to_date);
		para.Add("leave_date_count",leave_date_count);
		para.Add("leave_reason",leave_reason);
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
	
	
	
}
