package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HttpContext;
import org.springframework.context.ApplicationContext;

import BP.Tools.StringHelper;
import cn.jflow.common.util.ContextHolderUtils;

public class BaseController {

	public HttpServletRequest getRequest() {
		return ContextHolderUtils.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ContextHolderUtils.getResponse();
	}
	
	public String getParamter(String key){
		return getRequest().getParameter(key);
	}
	
	/**
	 * 增加列的数量。
	 */
	public final int getaddRowNum() {
		try {
			int i = Integer.parseInt(ContextHolderUtils.getRequest()
					.getParameter("addRowNum"));
			if (ContextHolderUtils.getRequest().getParameter("IsCut") == null) {
				return i;
			} else {
				return i;
			}
		} catch (java.lang.Exception e) {
			return 0;
		}
	}
	
	public final int getIsWap() {
		if(ContextHolderUtils.getRequest().getParameter("IsWap")==null)
			return 0;
		if (ContextHolderUtils.getRequest().getParameter("IsWap").equals("1")) {
			return 1;
		}
		return 0;
	}
	
	public String getRefPKVal() {
		String str = ContextHolderUtils.getRequest().getParameter("RefPKVal");
		if (str == null) {
			return "1";
		}
		return str;
	}
	public final String getSta() {
		String str = ContextHolderUtils.getRequest().getParameter("Sta");
		
		return str;
	}
	public int getPageIdx() {
		String str = ContextHolderUtils.getRequest().getParameter("PageIdx");
		if (str == null || str.equals("") || str.equals("null"))
			return 1;
		return Integer.parseInt(str);
		// set
		// {
		// ViewState["PageIdx",value;
		// }
	}
	
	public int getPageSize() {
		String str = ContextHolderUtils.getRequest().getParameter("PageSize");
		if (str == null || str.equals("") || str.equals("null"))
			return 10;
		return Integer.parseInt(str);
		// set
		// {
		// ViewState["PageIdx",value;
		// }
	}
	public String getFK_MapExt() {
		return ContextHolderUtils.getRequest().getParameter("FK_MapExt");
	}
	
	public String getFK_MapDtl(){
		return ContextHolderUtils.getRequest().getParameter("FK_MapDtl");
	}

	public final String getKey() {
		return ContextHolderUtils.getRequest().getParameter("Key");
	}

	public final String getActionType()
	{
		String s = ContextHolderUtils.getRequest().getParameter("ActionType");
		if (s == null)
		{
			s = ContextHolderUtils.getRequest().getParameter("DoType");
		}

		return s;
	}
	public final String getOID()
	{
		return ContextHolderUtils.getRequest().getParameter("OID");
	}
	public String getFK_Flow()
	{
		return ContextHolderUtils.getRequest().getParameter("FK_Flow");
	}
	public String getEnName()
	{
		return ContextHolderUtils.getRequest().getParameter("EnName");
	}
	public String getRefNo()
	{
		return ContextHolderUtils.getRequest().getParameter("RefNo");
	}
	public String getEnsName()
	{
		return ContextHolderUtils.getRequest().getParameter("EnsName");
	}
	public final String getFK_Emp()
	{
		return ContextHolderUtils.getRequest().getParameter("FK_Emp");
	}
	public final String getPageID()
	{
		return ContextHolderUtils.getRequest().getParameter("PageID");
	}
	public long getWorkID()
	{
		return Long.parseLong(StringHelper.isEmpty(getParamter("WorkID"), "0"));
	}
	public boolean getIsCC()
	{
		String s = ContextHolderUtils.getRequest().getParameter("Paras");
		if (s == null)
		{
			return false;
		}

		if (s.contains("IsCC"))
		{
			return true;
		}
		return false;
	}
	public String getDoType()
	{
		return ContextHolderUtils.getRequest().getParameter("DoType");
	}
	public final int getallRowCount() {
		int i = 0;
		try {
			i = Integer.parseInt(ContextHolderUtils.getRequest().getParameter(
					"rowCount"));
		} catch (java.lang.Exception e) {
			return 0;
		}
		return i;
	}	
	public final String getTB_Doc()
	{
		return ContextHolderUtils.getRequest().getParameter("TB_Doc");
	}	
	public final int getNodeID()
	{
		String s = ContextHolderUtils.getRequest().getParameter("NodeID");
		if (s == null || s.equals(""))
		{
			s = ContextHolderUtils.getRequest().getParameter("FK_Node");
		}
		return Integer.parseInt(s);
	}
	public String getSID()
	{
		return ContextHolderUtils.getRequest().getParameter("SID");
	}
	public int getRefOID()
    {
            String s = ContextHolderUtils.getRequest().getParameter("RefOID");
            if (s == null || s.equals("")||s.equals("null"))
                s = ContextHolderUtils.getRequest().getParameter("OID");
            if (s == null || s.equals("")||s.equals("null"))
                return 0;
            return Integer.valueOf(s);
    }
	//public String FK_Node;
	//public String FID;
	//public String WorkID;
	//public String FK_Flow;
	//public String MyPK;
	
	
	
	public String getMyPK() {
		String s = ContextHolderUtils.getRequest().getParameter("MyPK");
		return s;
	}

//	public void setMyPK(String myPK) {
//		MyPK = myPK;
//	}

//	@PostConstruct
//	public void init(){
//		HttpServletRequest request = ContextHolderUtils.getRequest();
//		//setFK_Node(request.getParameter("FK_Node"));
//		//setFID(request.getParameter("FID"));
//		//setWorkID(request.getParameter("WorkID"));
//		//setFK_Flow(request.getParameter("FK_Flow"));
//		//setMyPK(request.getParameter("MyPK"));
//	}
	
	/**
	 * 输出Alert
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	protected void printAlert(HttpServletResponse response, String msg) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("<script language='javascript'>alert('" + msg + "');</script>");
		out.flush();	
	}
	protected void printAlertReload(HttpServletResponse response, String msg,String url) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("<script language='javascript'>alert('" + msg + "');window.location.href='"+url+"';</script>");
		out.flush();	
	}	
	protected void windowReload(HttpServletResponse response, String url) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("<script language='javascript'>window.location.href='"+url+"';</script>");
		out.flush();	
	}	
	protected void wirteMsg(HttpServletResponse response, String msg) throws IOException{
		if(null == msg){
			return;
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(msg);
		out.flush();	
	}
	protected void winCloseWithMsg(HttpServletResponse response, String mess) throws IOException
    {
        //this.ResponseWriteRedMsg(mess);
        //return;
        mess = mess.replace("'", "＇");

        mess = mess.replace("\"", "＂");

        mess = mess.replace(";", "；");
        mess = mess.replace(")", "）");
        mess = mess.replace("(", "（");

        mess = mess.replace(",", "，");
        mess = mess.replace(":", "：");


        mess = mess.replace("<", "［");
        mess = mess.replace(">", "］");

        mess = mess.replace("[", "［");
        mess = mess.replace("]", "］");


        mess = mess.replace("@", "\\n@");

        mess = mess.replace("\r\n", "");

        response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("<script language='javascript'>alert('" + mess + "'); window.close()</script>");
		out.flush();
    }
	
	protected void winCloseWithMsg1(HttpServletResponse response,String val) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("<script language='javascript'> if(window.opener != undefined){window.top.returnValue = '" + val + "';} else { window.returnValue = '" + val + "';} window.close(); </script>");
		out.flush();	
	}

	protected void winClose(HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("<script language='javascript'> window.close();</script>");
		out.flush();	
	}
	public int getFK_Node() {
		String str = ContextHolderUtils.getRequest().getParameter("FK_Node");
		if (str == null || str.equals("")||str.equals("null"))
			return 1;
		return Integer.parseInt(str);
	}

//	public void setFK_Node(String fK_Node) {
//		FK_Node = fK_Node;
//	}

	public long getFID() {
		String str = ContextHolderUtils.getRequest().getParameter("FID");
		if (str == null || str.equals("")||str.equals("null"))
			return 0;
		return Long.valueOf(str);

	}

//	public void setFID(String fID) {
//		FID = fID;
//	}

//	public long getWorkID() {
//		return Long.valueOf(WorkID);
//	}
//
//	public void setWorkID(String workID) {
//		WorkID = workID;
//	}

//	public String getFK_Flow() {
//		return FK_Flow;
//	}

//	public void setFK_Flow(String fK_Flow) {
//		FK_Flow = fK_Flow;
//	}
	
}
