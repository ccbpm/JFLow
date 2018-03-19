<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
	String selUsers = request.getParameter("In")==null?"":request.getParameter("In");
%>
<style type="text/css">
/*引用本页面，使用EasyUi的Dialog的尺寸为520px X 360px,带buttons栏*/
select.listbox {
	border-style: none;
}
</style>
<script type="text/javascript">
function getReturnText() {
    var length = $('#lbRight option').length;
    var text = "";
    if (length > 0) {
        $('#lbRight option').each(function (i, selected) {
            if (text.length > 0) text += ",";
            text += $(selected).text();
        });
    }
    return text;
}
function getReturnValue() {
    var length = $('#lbRight option').length;
    var value = "";
    if (length > 0) {
        $('#lbRight option').each(function (i, selected) {
            if (value.length > 0) value += ",";
            value += $(selected).val();
        });
    }
    return value;
}
function searchAll() {
    //获取根节点
    var node = $('#ddlDept').combotree("tree").tree('getRoot');
    if (node) {
        //设置选中值
        $('#ddlDept').combotree("setValue", node.id);
    }
    $("#cbContainChild").prop("checked", true);
    //$("#ddlStation").val("0");
    $("#txtKeyword").val("");
    search();
}
function search() {
    //获取用户变量
    var deptTree = $('#ddlDept').combotree('tree'); // 得到树对象
    var n = deptTree.tree('getSelected'); // 得到选择的节点
    var deptId = 0;
    if (n) {
        deptId = n.id;
    }
    var searchChild = $("#cbContainChild").prop("checked");
    //var stationId = $("#ddlStation").val();
    var txtKeyWords = $("#txtKeyword").val();
    //获取要写入的list控件
    var lb = document.getElementById("lbLeft"); // $("#lbLeft");
    var rb = document.getElementById("lbRight"); // $("#lbLeft");
    //操作
    $.ajax({
        url:"<%=basePath%>WF/WorkOpt/GetUser.do",
        type:'post',
        dataType:'json',
        data: { "DeptId": deptId, "SearchChild": searchChild, "KeyWord": txtKeyWords },
        success: function (data) {
            if (data != "") {
                var emps = eval(data);
                lb.options.length = 0;

                for (var emp in emps) {
                    var value = emps[emp].no;
                    var text = emps[emp].name;
                    if (list_exists_item(rb, value)) text = "*" + text;
                    lb.options.add(new Option(text, value));
                }
            }
            else {
                lb.options.length = 0;
            }
        }
    });
}
function list_exists_item(lst_ctrl, str_value) {
    for (var i = 0; i < lst_ctrl.options.length; i++) {
        var option = lst_ctrl.options[i];
        if (option.value == str_value) return true;
    }
    return false;
}
function list_find_index(lst_ctrl, str_value) {
    for (var i = 0; i < lst_ctrl.options.length; i++) {
        var option = lst_ctrl.options[i];
        if (option.value == str_value) return i;
    }
    return -1;
}
function list_find_option(lst_ctrl, str_value) {
    for (var i = 0; i < lst_ctrl.options.length; i++) {
        var option = lst_ctrl.options[i];
        if (option.value == str_value) return option;
    }
    return null;
}
function add_repeat_tag_to_option(option) {
    if (option.text.substr(0, 1) != "*")
        option.text = "*" + option.text;
}
function remove_repeat_tag_from_option(option) {
    if (option.text.substr(0, 1) == "*")
        option.text = option.text.substr(1);
}
function L2R() {
    //取得两个名称
    var lst_from_name = "lbLeft";
    var lst_to_name = "lbRight";
    //获取两个list对象
    var lst_from = document.getElementById(lst_from_name);
    var lst_to = document.getElementById(lst_to_name);
    //验证是否选择            
    var from_selIndex = lst_from.selectedIndex;
    if (from_selIndex == -1) {
        alert("请选择要添加的员工！");
        return false;
    }
    //先添加
    var numOfRepeat = 0;
    var numOfSelect = 0;
    for (var i = 0; i < lst_from.options.length; i++) {
        var option = lst_from.options[i];
        if (option.selected) {
            numOfSelect++;
            if (list_exists_item(lst_to, option.value)) {
                numOfRepeat++;
            }
            else {
                lst_to.options.add(new Option(option.text, option.value));
            }
        }
    }
    //再删除
    for (var i = lst_from.options.length - 1; i >= 0; i--) {
        var option = lst_from.options[i];
        if (option.selected) {
            lst_from.options.remove(i);
        }
    }
    //提示
    if (numOfRepeat > 0) {
        alert("添加成功" + (numOfSelect - numOfRepeat) + "个，其它" + numOfRepeat + "个已经添加！");
    }
}
function R2L() {
    //取得两个名称
    var lst_from_name = "lbLeft";
    var lst_to_name = "lbRight";
    //获取两个list对象
    var lst_from = document.getElementById(lst_from_name);
    var lst_to = document.getElementById(lst_to_name);
    //验证是否选择            
    var to_selIndex = lst_to.selectedIndex;
    if (to_selIndex == -1) {
        alert("请选择要取消员工！");
        return false;
    }
    //先添加
    for (var i = 0; i < lst_to.options.length; i++) {
        var option = lst_to.options[i];
        if (option.selected) {
            if (list_exists_item(lst_from, option.value)) {
                var option_r = list_find_option(lst_from, option.value);
                //add_repeat_tag_to_option(option_r);
                remove_repeat_tag_from_option(option_r);
            }
            else {
                lst_from.options.add(new Option(option.text, option.value));
            }
        }
    }
    //再删除
    for (var i = lst_to.options.length - 1; i >= 0; i--) {
        var option = lst_to.options[i];
        if (option.selected) {
            lst_to.options.remove(i);
        }
    }
}
</script>
</head>
<body class="easyui-layout">
	<form method="post" action="" id="form1">
		<div data-options="region:'north',noheader:true,border:false"
			style="overflow-y: hidden; height: 30px; padding: 5px">
			<input id="ddlDept" class="easyui-combotree" style="width: 140px;" />&nbsp;
			<input type="checkbox" id="cbContainChild" value="1" /><label
				for="cbContainChild">子部门</label>&nbsp;&nbsp;
				<!-- <select name="ddlStation"
				id="ddlStation" onchange="search()" style="width: 100px;">
				<option value="0">--所有职位--</option>
				</select> -->
			 <input name="txtKeyword" type="text" maxlength="10" id="txtKeyword"
				style="width: 60px;" />&nbsp; <input type="button" id="btnSearch"
				value="搜索" onclick="search()" />&nbsp;&nbsp;<input type="button"
				id="btnSearchAll" value="所有" onclick="searchAll()" />
		</div>
		<div data-options="region:'center',noheader:true,border:false"
			style="overflow: hidden; padding: 5px">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'west',noheader:true" style="width: 200px">
					<select size="4" name="lbLeft" multiple="multiple" id="lbLeft"
						title="按Ctrl键全选" class="listbox" ondblclick="L2R()"
						style="height: 100%; width: 100%;">

					</select>
				</div>
				<div data-options="region:'center',noheader:true,border:false"
					style="text-align: center">
					<br /> <br /> <br /> <br /> <a href="javascript:void(0)"
						class="easyui-linkbutton" onclick="L2R()"
						data-options="iconCls:'icon-right'"> </a> <br /> <br /> <a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="R2L()" data-options="iconCls:'icon-left'"> </a>
				</div>
				<div data-options="region:'east',noheader:true" style="width: 200px">
					<select size="4" name="lbRight" multiple="multiple" id="lbRight"
						class="listbox" ondblclick="R2L()"
						style="height: 100%; width: 100%;">

					</select>
				</div>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
function onPageLoad(){
	var selUsers = "<%=selUsers%>";
	if("" != selUsers){
		 $.ajax({
		        url:"<%=basePath%>WF/WorkOpt/LoadSelectedEmployees.do",
				type : 'post',
				dataType : 'json',
				data : {
					"SelUsers" : selUsers
				},
				success : function(data) {
					if (data != "") {
						var depts = eval(data);

						var rb = document.getElementById("lbRight");
						rb.options.length = 0;
						for ( var dept in depts) {
							var value = depts[dept].no;
							var text = depts[dept].name;
							rb.options.add(new Option(text, value));
						}
						//lbRight.DataTextField = "Name";
						//lbRight.DataValueField = "No";
						//lbRight.DataSource = depts;
						//lbRight.DataBind();
					}
				}
			});
		}
	}
	$(document).ready(function() {
		onPageLoad();
		//初始化部门树
		$('#ddlDept').combotree({
			onSelect : function(node) {
				search();
			},
			onLoadSuccess : function(node, data) {
				//获取根节点
				var root = $('#ddlDept').combotree("tree").tree('getRoot');
				if (root) {
					//设置选中值
					$('#ddlDept').combotree("setValue", root.id);
					//执行查询
					search();
				}
			}
		});
		//操作
		$.ajax({
			url : "<%=basePath%>WF/WorkOpt/GetDepts.do",
	        type:'post',
	        dataType:'json',
	        success: function (data) {
	            if (data != "") {
	            	 var depts = eval(data);
	            	 $('#ddlDept').combotree('loadData', depts);
	            }
        	}
    	});
});
</script>
</html>