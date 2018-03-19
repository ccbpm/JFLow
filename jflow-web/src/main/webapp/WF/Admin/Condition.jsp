<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String CondType=request.getParameter("CondType");
	String FK_Flow=request.getParameter("FK_Flow");
	String FK_MainNode=request.getParameter("FK_MainNode");
	String FK_Node=request.getParameter("FK_Node");
	String FK_Attr=request.getParameter("FK_Attr");
	String DirType=request.getParameter("DirType");
	String ToNodeID=request.getParameter("ToNodeID");
	String CurrentCond="";
	ConditionModel cmodel=new ConditionModel(CurrentCond,CondType,FK_Flow,FK_MainNode,FK_Node,FK_Attr,DirType,ToNodeID);
	cmodel.init();
	CurrentCond=cmodel.CurrentCond;
%>
 <script type="text/javascript">
 function SelectAll(cb_selectAll) {
	    var arrObj = document.all;
	    if (cb_selectAll.checked) {
	        for (var i = 0; i < arrObj.length; i++) {
	            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
	                arrObj[i].checked = true;
	            }
	        }
	    } else {
	        for (var i = 0; i < arrObj.length; i++) {
	            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox')
	                arrObj[i].checked = false;
	        }
	    }
	}
        var currCond = '<%=CurrentCond %>';

        function changeCond(c) {
            if (c == null || c.value.length == 0) return;

            $('#mainCond').layout('panel', 'center').panel('setTitle', c.text);
            $('#context').attr('src', c.value + '.jsp?CondType=<%=CondType %>&FK_Flow=<%=FK_Flow %>&FK_MainNode=<%=FK_MainNode %>&FK_Node=<%=FK_Node %>&FK_Attr=<%=FK_Attr %>&DirType=<%=DirType %>&ToNodeID=<%=ToNodeID %>');
        }

        $(document).ready(function () {
            if (currCond.length > 0) {
                $('#cond').combobox('select', currCond);
            }
            else {
                $('#cond').combobox('select', 'Cond');
            }
        });
    </script>
</head>
<body class="easyui-layout">
<form method="post" action="" class="am-form" id="form1">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			<form id="form1" runat="server">
				<input type="hidden" id="FormHtml" name="FormHtml" value="">
			    <div data-options="region:'center',border:false">
			        <div id="mainCond" class="easyui-layout" data-options="fit:true">
			            <div data-options="region:'north',border:false" style="height:35px; padding:5px">
			                <label for="">
			                    	请选择方向条件设置类型：</label>
			                <select id="cond" class="easyui-combobox" name="cond" style="width: 300px;" data-options="editable:false,onSelect:function(rec){ changeCond(rec); }">
			                    <option value="Cond">按表单条件计算</option>
			                    <option value="CondStation">按指定操作员的岗位条件</option>
			                    <option value="CondDept">按指定操作员的部门条件</option>
			                    <option value="CondBySQL">按SQL条件计算</option>
			                    <option value="CondByPara">按开发者参数计算</option>
			                    <option value="CondByUrl">按Url条件计算</option>
			                </select>
			            </div>
			            <div data-options="region:'center',title:' '" style="overflow-y:hidden;">
			             <iframe id="context" scrolling="no" frameborder="0" src=""
			                    style="width: 100%; height: 100%;"></iframe>
			            </div>
			        </div>
			    </div>
			    </form>
			</div>
		</div>
	</form>
    
</body>
</html>