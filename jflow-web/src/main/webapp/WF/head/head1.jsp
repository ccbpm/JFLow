<%@taglib uri='http://java.sun.com/jstl/core_rt' prefix='c'%>
<%@taglib uri='http://java.sun.com/jstl/fmt_rt' prefix='fmt'%>
<%@taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn'%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Graphics"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="BP.Sys.SystemConfig"%>
<%@page import="BP.Tools.StringHelper"%>

<%@page import="BP.WF.Glo"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.DTS.GenerSiganture"%>

<%@page import="BP.WF.Node"%>
<%@page import="BP.WF.Nodes"%>
<%@page import="BP.WF.Template.CC.*"%>
<%@page import="BP.WF.DeliveryWay"%>
<%@page import="BP.WF.Flow"%>
<%@page import="BP.WF.Template.HungUp" %>
<%@page import="BP.WF.ActionType" %>


<%@page import="BP.WF.Entity.TrackAttr"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="BP.WF.Entity.FrmWorkCheckSta"%>
<%@page import="BP.WF.Entity.ReturnWork"%>
<%@page import="BP.WF.Entity.ReturnWorks"%>
<%@page import="BP.WF.Entity.GenerWorkFlow"%>
<%@page import="BP.WF.Entity.GenerWorkerListAttr"%>
<%@page import="BP.WF.Entity.GenerWorkFlowAttr"%>
<%@page import="BP.WF.Entity.GenerWorkerList"%>
<%@page import="BP.WF.Entity.GenerWorkerLists"%>

<%@page import="BP.Port.Emp"%>
<%@page import="BP.Port.Dept"%>
<%@page import="BP.Port.Depts"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.Port.EmpStation"%>
<%@page import="BP.Port.EmpStations"%>

<%@page import="BP.DA.DataTable"%>
<%@page import="BP.DA.DataRow"%>
<%@page import="BP.DA.DataType"%>
<%@page import="BP.DA.DBAccess"%>

<%@page import="cn.jflow.common.app.*"%>
<%@page import="cn.jflow.common.model.*"%>
<%@page import="cn.jflow.model.designer.*"%>
<%@page import="cn.jflow.controller.wf.workopt.AllotTaskController" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
%>
<%
	String name=SystemConfig.getSysName();
	name= new String(name.getBytes("ISO-8859-1"),"utf-8");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=name%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link rel="stylesheet" type="text/css" href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath%>WF/Comm/Style/CommStyle.css"  />
<link rel='stylesheet' type='text/css' href='<%=basePath%>WF/Comm/Style/Table0.css'  />

<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript"src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath%>WF/Comm/Menu.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/ShortKey.js"></script>

<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/CCForm/MapExt.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/QueryString.js" ></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/EasyUIUtility.js" ></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/config.js" ></script>