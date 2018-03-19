<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import = "cn.jflow.model.wf.admin.FoolFormDesigner.Tables" %>
<%@ page import = "BP.Sys.DBSrcType" %>
<%@ include file="/WF/head/head1.jsp"%>
<html>
	<head>
<%
	Tables ta = new Tables(request, response);
%>	
	
	
<script src="../../Comm/JScript.js" type="text/javascript"></script>
    <link href="../../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="../../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="../../Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="../../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script type="text/javascript">
    
        function Del(refNo) {

            if (window.confirm('您确定要删除吗？') == false)
                return;
            window.location.href = '?DoType=Del&RefNo=' + refNo;
        }

    </script>
	</head>
<body>
    <table class="easyui-layout" style="width: 100%;">
        <div  style=" float:left">
           数据源表： <a href="<%=basePath%>/WF/Comm/Sys/SFGuide.jsp?DoType=New&FromApp=SL">新建</a>
        </div>
        <tr>
            <th width="3%">
                序
            </th>
            <th>
                数据源
            </th>
            <th>
                表名
            </th>
            <th>
                中文名
            </th>
            <th>
                数据结构
            </th>
            <th>
                查看引用
            </th>
            <th>
                编辑
            </th>
            <th>
                数据
            </th>
            <th>
                删除
            </th>
        </tr>
        <%
    
            //删除数据.
            if (("Del").equals(request.getParameter("DoType")))
            {
                BP.Sys.SFTable mytab = new BP.Sys.SFTable();
                mytab.setNo(request.getParameter("RefNo"));
                mytab.Delete();
            }


            BP.Sys.SFTables tabs = new BP.Sys.SFTables();
            tabs.RetrieveAll();

            int idx = 0;
            String icon = "";
            for(BP.Sys.SFTable tab:tabs.ToJavaList())
            {
                idx++;
                
                if (tab.getNo().contains("BP.") == false)
                {
                     icon = "./Img/DBSrcTable.png";
                }
                else
                {
                     icon = "./Img/Form.png";
                }
        
        %>
        <tr  onmouseover='TROver(this)' onmouseout='TROut(this)'>
            <td class="Idx">
                <%=idx %>
            </td>
            <td>
                <%=tab.getFK_SFDBSrc() %>
            </td>
            <td>
                <%=tab.getNo() %>
            </td>
            <td>
                <img src='<%=icon %>' height="17" width="17" />
                <%=tab.getName() %>
            </td>
            <td>
                <%=tab.getCodeStructT()%>
            </td>
            <%
        int refNum = BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(KeyOfEn) FROM Sys_MapAttr WHERE UIBindKey='" + tab.getNo() + "'", 0);
        String delLink = "";
        if (refNum == 0)
            delLink = "<a href=\"javascript:Del('" + tab.getNo() + "')\">删除</a>";

        String editDBLink = "无";
        BP.Sys.SFDBSrc src = new BP.Sys.SFDBSrc(tab.getFK_SFDBSrc());
        if (src.getDBSrcType() != BP.Sys.DBSrcType.WebServices && tab.getNo().contains("BP.") == false)
        {
            int dbNum = src.RunSQLReturnInt("SELECT COUNT(*) FROM " + tab.getNo() + " ", 0);
            editDBLink = "<a href=/jflow-web/WF/MapDef/SFTableEditData.jsp?RefNo=" + tab.getNo() + ">编辑(" + dbNum + ")</a>";
        }
        
            %>
            <td>
                <a href="<%=basePath%>/WF/Admin/CCFormDesigner/TableRef.jsp?RefNo=<%=tab.getNo() %>&FromApp=SL">引用(<%=refNum %>)</a>
            </td>
            <td>
                <a href="<%=basePath%>/WF/MapDef/SFTable.jsp?RefNo=<%=tab.getNo() %>&FromApp=SL">编辑属性</a>
            </td>
            <td>
                <%=editDBLink %>
            </td>
            <td>
                <%=delLink %>
            </td>
        </tr>
        <%
    }
        %>
    </table>
</body>
</html>

