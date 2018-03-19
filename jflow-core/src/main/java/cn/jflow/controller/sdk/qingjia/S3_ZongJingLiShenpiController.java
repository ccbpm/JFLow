package cn.jflow.controller.sdk.qingjia;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Demo.QingJia;
import BP.WF.Glo;

@Controller
@RequestMapping("/sdkflowdemo/qingjia")
@Scope("request")
public class S3_ZongJingLiShenpiController {
	
	public PrintWriter out;
	
	@RequestMapping(value = "/Btn_Send_Click3", method = RequestMethod.POST)
	public  void Btn_Send_Click(HttpServletRequest request,HttpServletResponse response) {
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 执行保存.
		Btn_Save_Click(request, response);

		/// 第2步: 执行发送.
		
		//调用发送api, 返回发送对象.
		BP.WF.SendReturnObjs objs = null;

		// 查询出来.
		QingJia en = new QingJia();
		en.setOID((int) this.getWorkID(request));
		en.Retrieve();

		// 向下发送.
		objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow(request), this.getWorkID(request), 0, null);

//             这里注意： 
//             * 1,发送api有多个, 根据不同的场景使用不同的api 但是常用的就那1个，您可以产看该参数使用说明.
//             * BP.WF.Node_SendWork(string fk_flow, Int64 workID, int toNodeID, string toEmps) 
//             *
//             * 2,回来的发送对象里面有系统变量，这些系统变量包括发送给谁了，发送到那里了.
//             * 开发人员可以根据系统变量,执行相关的业务逻辑操作.
//            
		//#end第2步: 执行发送.

		//start 第3步: 把发送信息提示出来.
		String info = objs.ToMsgOfHtml();
		info = info.replace("\t\n", "<br>@");
		info = info.replace("@", "<br>@");
		out.write("<font color=blue size='3px'>" + info + "</font>");
		//end 第3步: 把发送信息提示出来.

		//设置界面按钮不可以用.
		//this.Btn_Save.setEnabled(false);
		//this.Btn_Send.setEnabled(false);
	}

	@RequestMapping(value = "/Btn_Save_Click3", method = RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request,HttpServletResponse response) {
	
		QingJia en = new QingJia();
		en.setOID((int) this.getWorkID(request));
		en.Retrieve();
		//en.setNoteBM(request.getParameter("TB_NoteRL"));
		en.setNoteZJL(request.getParameter("TB_NoteZJL"));
		en.Update(); // 写入部门经理意见.
	}

	@RequestMapping(value = "/Btn_Track_Click3", method = RequestMethod.POST)
	public  void Btn_Track_Click(HttpServletRequest request,HttpServletResponse response) {
		//BP.WF.Dev2Interface.UI_Window_OneWork(this.getFK_Flow(), this.getWorkID(), this.getFID());
		try {
			out = response.getWriter();
			out.print(Glo.getCCFlowAppPath()
					+ "WF/WorkOpt/OneWork/Track.jsp?FK_Flow="
					+ this.getFK_Flow(request) + "&WorkID="
					+ this.getWorkID(request) + "&FID=" + this.getFID(request));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/Btn_Return_Click3", method = RequestMethod.POST)
	public void Btn_Return_Click(HttpServletRequest request,HttpServletResponse response) {
		String url=BP.WF.Dev2Interface.UI_Window_Return(this.getFK_Flow(request), this.getFK_Node(request), this.getWorkID(request), this.getFID(request));
		try {
			out = response.getWriter();
			out.print(url);
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

	public final int getFK_Node(HttpServletRequest request) {
		return Integer.parseInt(request.getParameter("FK_Node"));
	}

	public final long getFID(HttpServletRequest request) {
		return Long.parseLong(request.getParameter("FID"));
	}
}
