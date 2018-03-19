<%@page import="cn.jflow.model.wf.mapdef.HidAttrModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	HidAttrModel hidAttrModel = new HidAttrModel(request,response);
	hidAttrModel.pageLoad();
%>

<script type="text/javascript">
    function Edit(mypk, refno, ftype) {
        var url = 'EditF.jsp?DoType=Edit&MyPK=' + mypk + '&RefNo=' + refno + '&FType=' + ftype;
        var b = window.showModalDialog(url, 'ass', 'dialogHeight: 500px; dialogWidth: 700px;center: yes; help: no');
        window.close();
    }
    function EditEnum(mypk, refno, ftype) {
        var url = 'EditEnum.jsp?DoType=Edit&MyPK=' + mypk + '&RefNo=' + refno + '&FType=' + ftype;
        var b = window.showModalDialog(url, 'ass', 'dialogHeight: 500px; dialogWidth: 700px;center: yes; help: no');
        window.close();
    }
    function EditTable(mypk, refno, ftype) {
        var url = 'EditTable.jsp?DoType=Edit&MyPK=' + mypk + '&RefNo=' + refno + '&FType=' + ftype;
        var b = window.showModalDialog(url, 'ass', 'dialogHeight: 500px; dialogWidth: 700px;center: yes; help: no');
        window.close();
    }
</script>
<base target="_self" />
<body>
	<div>
		 <%=hidAttrModel.pub.toString() %>
	</div>
</body>
</html>