<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<% 
    String enName = "ND" + request.getParameter("FK_Node");
    BP.WF.Template.FrmWorkCheck fwc = new BP.WF.Template.FrmWorkCheck();
    String src = pageContext.getServletContext().getContextPath()+"/WF/CCForm/AttachmentUpload.jsp?FID=" + request.getParameter("FID");
    src += "&WorkID=" + request.getParameter("WorkID");
    src += "&FK_Node=" +request.getParameter("FK_Node");
    src += "&FK_Flow=" + request.getParameter("FK_Flow");
    src += "&FK_FrmAttachment=ND" +request.getParameter("FK_Node") + "_DocMultiAth";

    String pkval = request.getParameter("WorkID");
   
        pkval = request.getParameter("OID");
    
    src += "&RefPKVal=" + pkval;
    src += "&PKVal=" + pkval;
    src += "&Ath=DocMultiAth" ;
    src += "&FK_MapData=" + enName;
    src += "&Paras=" + request.getParameter("Paras");
%>
<script type="text/javascript">

   
    function HideDivFuJian() {
        var oTb = document.getElementById("fujian");
        if (oTb.rows[1].style.display == "none") {
            for (var i = 1; i < oTb.rows.length; i++) {
                oTb.rows[i].style.display = "";
            }
            $('#fujianTitle').html("<b class='bt'>附件上传&nbsp;（隐藏）</b>");

        } else {
            for (var i = 1; i < oTb.rows.length; i++) {
                oTb.rows[i].style.display = "none";
            }
            $('#fujianTitle').html("<b class='bt'>附件上传&nbsp;（显示）</b>");
        }
    }
</script>
<div class="mainTab">
<table width="100%" border="0"cellspacing="4"  cellpadding="0" class="Tab" id="fujian" >
 
    <tr>
        <td>
            <iframe id='F332222' src='<%=src%>' frameborder="0" style='padding: 0px; border: 0px;'
                    leftmargin='0' topmargin='0' width='100%' style="align: center" height="150px"
                    scrolling="auto"></iframe>
        </td>
    </tr>
</table>
</div>
</body>
</html>