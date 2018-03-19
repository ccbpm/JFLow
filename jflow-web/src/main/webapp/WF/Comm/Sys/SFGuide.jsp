<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	SFGuideModel sf = new SFGuideModel(request, response);
	sf.Page_Load();
%>
<script type="text/javascript">
        function generateSQL(dblink, dbname, islocal) {
            var table = $('#LB_Table').val();
            var no = $('#DDL_ColValue').val();
            var name = $('#DDL_ColText').val();
            var parentno = $('#DDL_ColParentNo').val();
            var type = $('#DDL_SFTableType').val();
            var txtsql = $('#TB_SelectStatement');

            txtsql.text('SELECT ' + no + ' AS No, ' + name + ' AS [Name]' + (type == '1' ? (', ' + parentno + ' AS ParentNo') : '') + ' FROM ' + (islocal ? '' : (dblink + '.' + dbname + '.')) + 'dbo.' + table);
        }

        function showInfo(title, msg, autoHiddenMillionSeconds) {
            $.messager.show({
                title: title,
                msg: msg,
                timeout: autoHiddenMillionSeconds,
                showType: 'slide',
                style: {
                    right: '',
                    top: document.body.scrollTop + document.documentElement.scrollTop,
                    bottom: ''
                }
            });
        }

        function showInfoAndGo(title, msg, icon, url) {
            if (url == undefined || url == null || url.length == 0) {
                $.messager.alert(title, msg, icon);
            }
            else {
                $.messager.alert(title, msg, icon, function () {
                    self.location = url;
                });
            }
        }

        function showInfoAndBack(title, msg, icon) {
            $.messager.alert(title, msg, icon, function () {
                history.back();
            });
        }
        var FK_SFDBSrc;
        var DDL_ColValue;
        var DDL_ColText;
        var DDL_ColParentNo;
        var DDL_SFTableType;
        var LB_Table;
        var TB_SelectStatement;
        function btn_Click()
        {
        	FK_SFDBSrc='<%=sf.getFK_SFDBSrc()%>';
        	DDL_ColValue=$("#DDL_ColValue").val();
        	DDL_ColText=$("#DDL_ColText").val();
        	DDL_ColParentNo=$("#DDL_ColParentNo").val();
        	DDL_SFTableType=$("#DDL_SFTableType").val();
        	LB_Table=$("#LB_Table").val();
        	TB_SelectStatement=$("#TB_SelectStatement").val();
        	$("#FormHtml").val($("#form1").html());
        	$("#form1").attr("action","<%=basePath%>Comm/Sys/SF_Btn_Click.do?FK_SFDBSrc="
    				+ FK_SFDBSrc + "&DDL_ColValue=" + DDL_ColValue + "&LB_Table="
    				+ LB_Table + "&DDL_ColText=" + DDL_ColText
    				+ "&DDL_ColParentNo=" + DDL_ColParentNo
    				+ "&TB_SelectStatement=" + TB_SelectStatement
    				+ "&DDL_SFTableType=" + DDL_SFTableType);
        	$("#form1").submit();
		}
        function btn_Create_Click()
        { 
        	FK_SFDBSrc='<%=sf.getFK_SFDBSrc()%>';
        	DDL_ColValue='<%=sf.getDDL_ColValue()%>';
        	DDL_ColText='<%=sf.getDDL_ColText()%>';
        	DDL_ColParentNo='<%=sf.getDDL_ColParentNo()%>';
        	DDL_SFTableType='<%=sf.getDDL_SFTableType()%>';
        	LB_Table='<%=sf.getLB_Table()%>';
        	TB_SelectStatement='<%=sf.getTB_SelectStatement()%>';
        	var TB_No=$("#TB_No").val();
        	var TB_Name=$("#TB_Name").val();
        	var TB_TableDesc=$("#TB_TableDesc").val();
        	location.href="<%=basePath%>Comm/Sys/SF_btn_Create_Click.do?FK_SFDBSrc="
    				+ FK_SFDBSrc + "&DDL_ColValue=" + DDL_ColValue + "&LB_Table="
    				+ LB_Table + "&DDL_ColText=" + DDL_ColText
    				+ "&DDL_ColParentNo=" + DDL_ColParentNo
    				+ "&TB_SelectStatement=" + TB_SelectStatement
    				+ "&DDL_SFTableType=" + DDL_SFTableType+"&TB_No="+TB_No+"&TB_Name="+TB_Name+"&TB_TableDesc="+TB_TableDesc;
        }
        function btn_Create_local_Click()
        {
        	var TB_No=$("#TB_No").val();
        	var TB_Name=$("#TB_Name").val();
        	var TB_TableDesc=$("#TB_TableDesc").val();
        	location.href="<%=basePath%>Comm/Sys/btn_Create_Local_Click.do?FK_SFDBSrc=local&TB_No="+TB_No+"&TB_Name="
        			+TB_Name+"&TB_TableDesc="+TB_TableDesc;
        }
</script>
</head>
<body>
		<%=sf.Pub1.ListToString()%>
</body>
</html>