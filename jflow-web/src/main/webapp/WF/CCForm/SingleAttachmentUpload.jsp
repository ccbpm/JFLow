<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="cn.jflow.common.model.SingleAttachmentUploadModel"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	SingleAttachmentUploadModel sau = new SingleAttachmentUploadModel(request,response);
	String FK_Node = String.valueOf(sau.getFK_Node());
	String EnName = sau.getEnName();
	String PKVal = sau.getPKVal();
	sau.init();
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>单附件</title>
    <script src="<%=basePath %>WF/Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
    <link href="<%=basePath %>WF/Scripts/slideBox/style/jquery.slideBox.css" rel="stylesheet" type="text/css" />
  	<script src="<%=basePath %>WF/Scripts/slideBox/jquery.slideBox.min.js" type="text/javascript"></script>
	<script language="javascript" type="text/javascript">


    function UploadChange(btn) {
    	$("#BtnID").val(btn);
        document.getElementById("btn_sumit").click();
    }
    
    function OpenOfiice(fk_ath, pkVal, delPKVal, FK_MapData, NoOfObj, FK_Node) {
        var url = '<%=basePath %>WF/WebOffice/AttachOffice.jsp?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + "&FK_MapData=" + FK_MapData + "&NoOfObj=" + NoOfObj + "&FK_Node=" + FK_Node;
        window.open(url, 'WebOffice编辑', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    }

</script>

<style  type="text/css">
   .TBNote
   {
       border-bottom-color:Black;
       background-color:Silver;
   }
   
   Button
   {
       font-size:12px;
   }
   
   .Btn
   {
       font-size:12px;
   }
</style>

    <script language="JavaScript" src="<%=basePath %>WF/Comm/JS/Calendar/WdatePicker.js" type="text/javascript"
        defer="defer"></script>
    <script language="JavaScript" src="<%=basePath %>WF/Comm/JScript.js" type="text/javascript"></script>
    <link href="<%=basePath %>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" /><link href="../Comm/Style/Tabs.css" rel="stylesheet" type="text/css" />
    <base target="_self" />
</head>
<body style="padding:0px;margin:0px;">
    	<%=sau.Pub1.toString() %>
</body>
</html>
