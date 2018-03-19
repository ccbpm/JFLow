package cn.jflow.controller.sdk.qingjia;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.system.ui.core.TextBox;
import BP.Demo.QingJia;
import BP.WF.Glo;
import BP.Web.WebUser;

@Controller
@RequestMapping("/sdkflowdemo/qingjia")
@Scope("request")
public class S1_TianxieShenqingDanController {
	
	public TextBox TB_QingJiaYuanYin;
	public TextBox TB_QingJiaTianShu;
	public PrintWriter out;
	/**
	 * 执行发送
	 * 
	 * @param sender
	 * @param e
	 */
	@RequestMapping(value = "/Btn_Send_Click", method = RequestMethod.POST)
	public final void Btn_Send_Click(HttpServletRequest request,HttpServletResponse response ) {

		try {
			out=response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 第1步: 执行保存.
		this.Btn_Save_Click(request,response);

		// 检查完整性
		BP.Demo.QingJia en = new BP.Demo.QingJia();
		en.setOID((int) this.getWorkID(request));
		en.Retrieve();
		if (en.getQingJiaTianShu() <= 0) {
	
		//	out.write("保存失败，请假天数不能小于等于零。");
			out.write("<font color=red size='3px'>保存失败，请假天数不能小于等于零。</font>");
			return;
		}

		// /#region 第2步: 执行发送.
		BP.WF.SendReturnObjs objs = null;
		try {
			// 调用发送api, 返回发送对象.
			objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow(request),this.getWorkID(request));
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
	public void Btn_Save_Click(HttpServletRequest request,HttpServletResponse response) {
		// #r 业务数据保存, 根据workid, 把它作为主键，存入数据表(此部分与ccflow无关).
		// 本demo我们使用BP框架做了数据存储.
		QingJia en = new BP.Demo.QingJia();
		en.setOID((int) getWorkID(request));
		en.setQingJiaRenNo(WebUser.getNo());
		en.setQingJiaRenName(WebUser.getName());
		en.setQingJiaRenDeptNo(WebUser.getFK_Dept());
		en.setQingJiaRenDeptName(WebUser.getFK_DeptName());
		en.setQingJiaYuanYin(request.getParameter("TB_QingJiaYuanYin"));
		en.setQingJiaTianShu(Float.parseFloat(request.getParameter("TB_QingJiaTianShu")));

		if (en.getIsExits() == false) {
			try {
				en.InsertAsOID(this.getWorkID(request));
			} catch (Exception e) {
				e.printStackTrace();
			} // 如果已经不存在.
		} else {
			en.Update();
		}
		// /#endregion 业务数据保存, 根据workid, 把它作为主键，存入数据表.
	}
	/**
	 *  打开流程
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Btn_Track_Click", method = RequestMethod.POST)
	public final void Btn_Track_Click(HttpServletRequest request,HttpServletResponse response) {
		//BP.WF.Dev2Interface.UI_Window_OneWork(response,this.getFK_Flow(request),this.getWorkID(request), this.getFID(request));
	
		try {
			out = response.getWriter();
			out.print(Glo.getCCFlowAppPath() + "WF/WorkOpt/OneWork/Track.jsp?FK_Flow=" + this.getFK_Flow(request) + "&WorkID=" + this.getWorkID(request) + "&FID=" +this.getFID(request));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public final long getWorkID(HttpServletRequest request) {
		return Long.parseLong(request.getParameter("WorkID"));
	}
	
	public final String getFK_Flow(HttpServletRequest request) {
		return request.getParameter("FK_Flow");
	}
	
	public final long getFID(HttpServletRequest request) {
		return Long.parseLong(request.getParameter("FID"));
	}
}

