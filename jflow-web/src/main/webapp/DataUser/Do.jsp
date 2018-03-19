<%@page import="BP.DA.DataType"%>
<%@page import="BP.DA.DBAccess"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	// region 常用变量.
	String doType = request.getParameter("DoType");
	String fk_node = request.getParameter("FK_Node");
	String workId = request.getParameter("WorkID");
	String oId = request.getParameter("OID");
	if(null == oId){
		oId="0";
	}
	// endregion 常用变量.

	try
	{
		if(null == doType){
			return;
		}
		if (doType.equals("SetHeJi")){
			String sql = "UPDATE ND101 SET HeJi=(SELECT SUM(XiaoJi) FROM ND101Dtl1 WHERE RefPK=" + oId + ") WHERE OID=" + oId;
			DBAccess.RunSQL(sql);
			//把合计转化成大写.
			float hj = DBAccess.RunSQLReturnValFloat("SELECT HeJi FROM ND101 WHERE OID=" + oId, 0);
			sql = "UPDATE ND101 SET DaXie='" + DataType.ParseFloatToCash(hj) + "' WHERE OID=" + oId;
			DBAccess.RunSQL(sql);
			return;
		} else if (doType.equals("OutOK")) {
			//在这是里处理您的业务过程。
			return;
		}
	} catch(RuntimeException ex){
		response.getWriter().write("error:" + ex.getStackTrace());
	}
%>