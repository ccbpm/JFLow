package cn.jflow.controller.wf.workopt;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.Log;
import BP.Tools.StringHelper;
import BP.WF.TodolistModel;
import BP.WF.Template.TransferCustom;

@Controller
@RequestMapping("/WF/WorkOpt")
public class TransferCustomSimpleController extends BaseController{
	
	@RequestMapping(value="TransferCustomSimple", method = RequestMethod.POST)
	public void saveCfg(HttpServletRequest request, HttpServletResponse response)
	{
		//String method1 = request.getParameter("method");
		String method = request.getParameter("method");
		String re = "";

		if (StringHelper.isNullOrWhiteSpace(method))
		{
			return;
		}

		try
		{
			long workId = 0;
			if ("savecfg".equals(method))
			{
				workId = Long.parseLong((request.getParameter("workId") != null) ? request.getParameter("workId") : "0");
				int nodeid = Integer.parseInt((request.getParameter("nodeId") != null) ? request.getParameter("nodeId") : "0");
				String empNos = request.getParameter("empNos");
				String empNames = request.getParameter("empNames");
				String plan = request.getParameter("plan");
				int step = Integer.parseInt(request.getParameter("step"));
				TransferCustom tfc = null;

				if (workId == 0)
				{
					re = ReturnJson(false, "workid参数不正确", false);
				}
				else
				{
					tfc = new TransferCustom();
					tfc.setMyPK(nodeid + "_" + workId);

					if (StringHelper.isNullOrWhiteSpace(empNos))
					{
						tfc.Delete();
					}
					else
					{
						tfc.setWorkID(workId);
						tfc.setFK_Node(nodeid);
						tfc.setWorker(empNos);
						tfc.setWorkerName(empNames);
						tfc.setPlanDT(plan);
						tfc.setTodolistModel(TodolistModel.QiangBan);
						tfc.setIdx(step);
						tfc.Save();
					}

					re = ReturnJson(true, "保存成功！", false);
				}
			}
		}
		catch (RuntimeException ex)
		{
			re = ReturnJson(false, ex.getMessage(), false);
		}

		/**
		Response.Charset = "UTF-8";
		Response.ContentEncoding = System.Text.Encoding.UTF8;
		Response.ContentType = "text/html";
		Response.Expires = 0;
		Response.Write(re);
		Response.End();
		 */
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out ;
		try{
			out = response.getWriter();
			out.write(re);
			out.close();
		}catch(Exception ex)
		{
			Log.DebugWriteError(ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}
	/** 
	 生成返给前台页面的JSON字符串信息
	 
	 @param success 是否操作成功
	 @param msg 消息
	 @param haveMsgJsoned msg是否已经JSON化
	 @return 
	*/
	private String ReturnJson(boolean success, String msg, boolean haveMsgJsoned)
	{
		String kh = haveMsgJsoned ? "" : "\"";
		return "{\"success\":" + String.valueOf(success).toLowerCase() + ",\"msg\":" + kh + (haveMsgJsoned ? msg : msg.replace("\"", "'")) + kh + "}";
	}
}
