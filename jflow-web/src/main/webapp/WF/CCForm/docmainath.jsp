
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cn.jflow.common.model.AttachmentUploadModel" %>
<%
	// 拼接重定向参数
	//requestParas = "&EnsName="+ensName+"&RefPKVal="+refPKVal+"&IsReadonly="+isReadonly+"&FID="+fId+"&FK_Node="+fkNode+"";
	//dm.init();
	AttachmentUploadModel  au = new AttachmentUploadModel();
	au.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
     <meta http-equiv="Page-Enter" content="revealTrans(duration=0.5, transition=8)" />
    <title></title>
   <script type="text/javascript">

        function UploadChange(btn) {
            document.getElementById("Btn_Upload").click();
            //docment.form1.submit();
        }
        function OpenOfiice(fk_ath, pkVal, delPKVal, FK_MapData, NoOfObj, FK_Node) {
            var date = new Date();
            var t = date.getFullYear() + "" + date.getMonth() + "" + date.getDay() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds();

            var url = '../CCForm/AttachmentUpload.jsp?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + "&FK_MapData=" + FK_MapData + "&NoOfObj=" + NoOfObj + "&FK_Node=" + FK_Node + "&T=" + t;
            //var url = 'WebOffice.aspx?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal;
            // var str = window.showModalDialog(url, '', 'dialogHeight: 1250px; dialogWidth:900px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no;resizable:yes');
            //var str = window.open(url, '', 'dialogHeight: 1200px; dialogWidth:1110px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no;resizable:yes');
            window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        }

        function OpenFileView(pkVal, delPKVal) {    
            var url = 'FilesView.jsp?DoType=view&DelPKVal=' + delPKVal + '&PKVal=' + pkVal;
            window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        }
         
    </script>
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
    <form id="form2"  onkeypress="NoSubmit(event);" action="" method="post">
    <asp:Button ID="Btn_Upload" runat="server" value="" style="display: none" OnClick="btnUpload_Click"/>
    <input type="button" ID="Btn_Print" runat="server"va class="Btn" Visible="true" style="display: none"OnClick="btnUpload_Click()"/>
    <input type="submit" name="Btn_Upload" value="" id="Btn_Upload" style="display:none;" />
    <%=au.Pub1.toString() %>
    </form>
</body>
</html>
