<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="cn.jflow.common.model.WorkCheckModel" %>
<%@page import="cn.jflow.common.model.AttachmentUploadModel"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String PKVal=request.getParameter("PKVal");
	String Ath=request.getParameter("Ath");
	String FK_FrmAttachment=request.getParameter("FK_FrmAttachment");
	String FK_Flow=request.getParameter("FK_Flow");
	String DoWhat=request.getParameter("DoWhat");
	String UserNo=WebUser.getNo();
	String Lang=request.getParameter("Lang");
	String Type=request.getParameter("Type");
	String FK_Node=request.getParameter("FK_Node");
	AttachmentUploadModel au = new AttachmentUploadModel();
	au.init();
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>
	多附件
</title>
     <script src="<%=basePath %>WF/Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
    <link href="<%=basePath %>WF/Scripts/slideBox/style/jquery.slideBox.css" rel="stylesheet" type="text/css" />
  <script src="<%=basePath %>WF/Scripts/slideBox/jquery.slideBox.min.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
    function Del(fk_ath, pkVal, delPKVal) {
        if (window.confirm('您确定要删除吗？ ') == false)
            return ;
        window.location.href = '<%=basePath%>WF/CCForm/AttachmentUpload.jsp?DoType=Del&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + '&FK_Node=<%=FK_Node%>&FK_Flow = <%=FK_Flow%>&FK_MapData=ND<%=FK_Node%>&Ath=<%=Ath%>';
    }
    function Down(fk_ath, pkVal, delPKVal) {
        window.location.href = '<%=basePath%>WF/CCForm/AttachmentUpload.jsp?DoType=Down&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + '&FK_Node=<%=FK_Node%>&FK_Flow = <%=FK_Flow%>&FK_MapData=ND<%=FK_Node%>&Ath=<%=Ath%>';

    }
    function OpenOfiice(fk_ath, pkVal, delPKVal, FK_MapData, NoOfObj, FK_Node) {
        var date = new Date();
        var t =   date.getFullYear()+""+date.getMonth()+""+ date.getDay() +""+ date.getHours()+"" + date.getMinutes()+"" + date.getSeconds();

        var url = '<%=basePath %>WF/WebOffice/AttachOffice.jsp?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + "&FK_MapData=" + FK_MapData + "&NoOfObj=" + NoOfObj + "&FK_Node=" + FK_Node + "&T=" + t;
        window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    }

    function UploadChange(btn) {
        document.getElementById("Btn_Upload").click();
        //docment.form1.submit();
    }

    function OpenFileView(pkVal, delPKVal) {
        var url = '<%=basePath %>WF/WorkOpt/FilesView.jsp?DoType=view&DelPKVal=' + delPKVal + '&PKVal=' + pkVal;
        window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    }

    function setIframeHeight() {
		$("#" + window.frameElement.getAttribute("id"), parent.document).height(Math.max($(document).height(), $("body").height(), $("#attachmentContainer table").height()) +  + ($(".Idx").length * 2));
	}

	$(setIframeHeight);
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

    <script language="JavaScript"  src="<%=basePath %>WF/Comm/JS/Calendar/WdatePicker.js" type="text/javascript"
        defer="defer"></script>
    <script language="JavaScript" src="<%=basePath %>WF/Comm/JScript.js" type="text/javascript"></script>
    <link href="<%=basePath %>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" /><link href="../Comm/Style/Tabs.css" rel="stylesheet" type="text/css" />
    <base target="_self" />
</head>
<body onkeypress="Esc();" style="padding:0px;margin:0px;">
    <form method="post" action="<%=basePath %>WF/CCForm/AttachmentUploadS.do?PKVal=<%=PKVal %>&Ath=<%=Ath %>&FK_FrmAttachment=<%=FK_FrmAttachment %>&FK_Flow=<%=FK_Flow %>&DoWhat=<%=DoWhat %>&UserNo=<%=UserNo %>&Lang=<%=Lang %>&Type=<%=Type %>&FK_Node=<%=FK_Node %>"  id="form1"  name="form1" enctype="multipart/form-data">
    <div id="attachmentContainer" style="position: fixed; height: 100%; height: 100%; width: 100%;overflow-x:hidden;overflow-y:auto;">
        
   <%=au.Pub1.toString() %>
    <input type="submit" name="Btn_Upload" value="" id="Btn_Upload" style="display:none;" />

    </div>
    </form>
</body>
</html>
