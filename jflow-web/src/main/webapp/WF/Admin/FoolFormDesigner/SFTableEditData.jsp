<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/WF/head/head1.jsp"%>
    <%
	SFTableEditDataModel SFTable = new SFTableEditDataModel(request, response);
    SFTable.init();
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=SFTable.getTitle() %></title>
    <script language="javascript">
        /* ESC Key Down  */
        function Esc() {
            if (event.keyCode == 27)
                window.close();
            return true;
        }
        
        function Del(refno, pageidx, enpk) {
            if (window.confirm('您确定要删除字段[' + enpk + ']吗？') == false)
                return;
            var url = 'SFTableEditData.jsp?RefNo=' + refno + '&PageIdx=' + pageidx + '&EnPK=' + enpk;
            window.location.href = url;
        }
        function RSize() {

            if (document.body.scrollWidth > (window.screen.availWidth - 100)) {
                window.dialogWidth = (window.screen.availWidth - 100).toString() + "px"
            } else {
                window.dialogWidth = (document.body.scrollWidth + 50).toString() + "px"
            }

            if (document.body.scrollHeight > (window.screen.availHeight - 70)) {
                window.dialogHeight = (window.screen.availHeight - 50).toString() + "px"
            } else {
                window.dialogHeight = (document.body.scrollHeight + 115).toString() + "px"
            }
            window.dialogLeft = ((window.screen.availWidth - document.body.clientWidth) / 2).toString() + "px"
            window.dialogTop = ((window.screen.availHeight - document.body.clientHeight) / 2).toString() + "px"
        }
        function btn_Click(){
            var url = "<%=basePath%>WF/MapDef/btn_Click3.do?RefNo=<%=SFTable.getRefNo() %>&PageIdx=<%=SFTable.getPageIdx() %>";
            $("#form1").attr("action",url);
     		$("#form1").submit();
        }
    </script>
    <base target="_self" />
</head>
<body onkeypress="Esc()" class="easyui-layout">
    <div data-options="region:'center',title:'<%=SFTable.getTitle() %>'" style="padding: 5px;">
        <form id="form1" action="SFTableEditData.jsp?RefNo=<%=SFTable.getRefNo() %>&amp;PageIdx=<%=SFTable.getPageIdx() %>" method="post">
        <%=SFTable.Pub1.toString() %>
        <%=SFTable.Pub2.toString() %>
        <%=SFTable.Pub3.toString() %>
        </form>
    </div>
</body>
</html>