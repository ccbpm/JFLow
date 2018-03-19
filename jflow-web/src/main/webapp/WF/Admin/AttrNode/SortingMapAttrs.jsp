<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@page import="cn.jflow.model.designer.SortingMapAttrsModel"%> --%>
<%@include file="/WF/head/head1.jsp"%>
<%
	SortingMapAttrsModel smamodel = new SortingMapAttrsModel(request, response);
	smamodel.Page_Load();
%>
<title>手机屏幕显示字段排序</title>
<head>
    <!-- <link href="../../Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
    <link href="../../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <link href="../../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <script src="../../Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="../../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="../../Scripts/EasyUIUtility.js" type="text/javascript"></script> -->
    <script language="javascript" type="text/javascript">
        function ShowEditWindow(field, url) {
            if (!field || !url) {
                return;
            }

            OpenEasyUiDialog(url, "eudlgframe", "编辑字段：" + field, 600, 550, "icon-edit");
            $('#eudlg').dialog({
                onClose: function () {                    
                    location.href = location.href + "&t=" + Math.random();
                }
            });
        }
        //新增分组名称
        function GroupFieldNew(ensName) {
            var url = '../FoolFormDesigner/GroupField.jsp?DoType=NewGroup&RefNo=' + ensName;
            var b = window.showModalDialog(url, 'ass', 'dialogHeight: 500px; dialogWidth: 700px;center: yes; help: no');
            window.location.href = window.location.href;
        }
        //编辑分组名称
        function GroupField(fk_mapdata, OID) {
            var url = '../FoolFormDesigner/GroupField.aspx?FK_MapData=' + fk_mapdata + "&GroupField=" + OID;
            var b = window.showModalDialog(url, 'ass', 'dialogHeight: 500px; dialogWidth: 700px;center: yes; help: no');
            window.location.href = window.location.href;
        }
        //编辑明细表
        function EditDtl(mypk, dtlKey) {
            var url = '../FoolFormDesigner/MapDtl.jsp?DoType=Edit&FK_MapData=' + mypk + '&FK_MapDtl=' + dtlKey;
            var b = window.showModalDialog(url, 'ass', 'dialogHeight: 600px; dialogWidth: 700px;center: yes; help:no;resizable:yes');
            window.location.href = window.location.href;
        }
        //编辑多附件
        function EditAthMent(fk_mapdata, athMentKey) {
            var url = '../FoolFormDesigner/Attachment.jsp?FK_MapData=' + fk_mapdata + '&Ath=' + athMentKey;
            var b = window.showModalDialog(url, 'ass', 'dialogHeight: 600px; dialogWidth: 700px;center: yes; help:no;resizable:yes');
            window.location.href = window.location.href;
        }
        //预览手机端
        function Form_View(FK_MapData, FK_Flow) {
            var url = '../../../CCMobile/Frm.jsp?FK_MapData=' + FK_MapData + '&IsTest=1&WorkID=0&FK_Node=999999';
            OpenEasyUiDialog(url, "eudlgframe", "预览表单", 360, 568, "icon-search");
        }
    </script>
</head>
<body>
<form id="form1" runat="server">
    <div data-options="region:'center',title:'字段排序',border:false" style="padding:5px">
        <!-- <Pub:uc1 ID="pub1" runat="server" /> -->
        <%=smamodel.pub1.toString()
        %>
        <asp:HiddenField ID="hidCopyNodes" runat="server" />
    </div>
</form>
</body>
</html>