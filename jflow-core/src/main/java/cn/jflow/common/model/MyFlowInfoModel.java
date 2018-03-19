package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.PubClass;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.WorkFlow;
import BP.WF.Flow;

public class MyFlowInfoModel {
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String msg;
	
	public String getMsg() {
		return msg.replace("null", "");
	}
	
	public MyFlowInfoModel(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public void initFlowInfo(){
		
		 String workId = request.getParameter("WorkID")==null?"0":request.getParameter("WorkID");
		 long WorkID=Long.valueOf(workId);
		
		 String FK_Flow = request.getParameter("FK_Flow")==null?"012":request.getParameter("FK_Flow");
		 
		 String DoType = request.getParameter("DoType")==null?"":request.getParameter("DoType");
	 	 if("DeleteFlow".equals(DoType)){
			 try{
				 WorkFlow wf = new WorkFlow(new Flow(FK_Flow), WorkID);
	             wf.DoDeleteWorkFlowByReal(true);
	             request.getSession().setAttribute("info", "流程删除成功");
			 }catch(RuntimeException e){
				 request.getSession().setAttribute("info", "执行删除失败："+e.getMessage());
				 PubClass.Alert("流程删除失败："+e.getMessage(), response);
				 //out.println("<script>alert('流程删除失败："+e.getMessage()+"');</script>");
			 }
		 }else if("UnShift".equals(DoType)){
			 try{
				 WorkFlow mwf = new WorkFlow(FK_Flow, WorkID);
				 String str = mwf.DoUnShift();
				 request.getSession().setAttribute("info", str);
			 }catch(RuntimeException e){
				 request.getSession().setAttribute("info", "执行撤消失败："+e.getMessage());
				 PubClass.Alert("执行撤消失败："+e.getMessage(), response);
				 //out.println("<script>alert('执行撤消失败："+e.getMessage()+"');</script>");
			 }
		 }else if("UnSend".equals(DoType)){
			 try{
				
				 String str = Dev2Interface.Flow_DoUnSend(FK_Flow, WorkID);
				 request.getSession().setAttribute("info", str);
			 }catch(RuntimeException e){
				 request.getSession().setAttribute("info", "执行撤消失败："+e.getMessage());
				 PubClass.Alert("执行撤消失败："+e.getMessage(), response);
				 //out.println("<script>alert('执行撤消失败："+e.getMessage()+"');</script>");
			 }
		 }
	 	
	 	 Object s = request.getSession().getAttribute("info");
	 	 //if(null == s) s = application.getAttribute("info"+WebUser.getNo());
	 	 if(null == s){
	 		try {
				s = Glo.getSessionMsg();
			} catch (Exception e) {
				e.printStackTrace();
			} 
	 	 }
			
	 	 if(null != s){
	 		msg = s.toString();
	 		msg = msg.replace("@@", "@");
	 		if(msg.lastIndexOf("@") == msg.length()-1){
	 			msg = msg.substring(0, msg.length()-1);
	 		}
	 		msg = msg.replace("@", "<BR><BR><img src='"+Glo.getCCFlowAppPath()+"WF/Img/email_start.png' align='middle' />");
	 	 }
	}
}
