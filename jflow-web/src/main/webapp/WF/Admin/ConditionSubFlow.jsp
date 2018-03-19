<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/WF/head/head1.jsp"%>
	
<title>触发子流程的条件设置</title>
    <link href="../Comm/Style/CommStyle.css" rel="stylesheet" type="text/css" />
    <link href="../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="../Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script language="javascript">

        var currShow;

        //在右侧框架中显示指定url的页面
        function OpenUrlInRightFrame(ele, url) {
            if (ele != null && ele != undefined) {
                //if (currShow == $(ele).text()) return;

                currShow = $(ele).parents('li').text(); //有回车符

                $.each($('ul.navlist'), function () {
                    $.each($(this).children('li'), function () {
                        $(this).children('div').css('font-weight', $(this).text() == currShow ? 'bold' : 'normal');
                    });
                });

                $('#context').attr('src', url + '&s=' + Math.random());
            }
        }

        $(document).ready(function () {
            $('ul.navlist').find("a[id='a<%--=this. ToFlow --%>']").click();
        });
    </script>


<%
	    
%>
<body>
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'west',split:true,title:'节点方向'" style="width: 300px;">
                <ul class="navlist">
                    <%-- <asp:Repeater ID="rptLines" runat="server">
                        <ItemTemplate>
                            <li>
                                <div>
                                    <a id='a<%# Eval("No") %>' href="javascript:void(0)" onclick="OpenUrlInRightFrame(this, 'ConditionSubFlowDtl.aspx?CondType=3&FK_Flow=<%=this.FK_Flow %>&FK_Node=<%# Eval("No") %>&FK_MainNode=<%=this.FK_Node %>&ToFlow=<%# Eval("No") %>&FK_Attr=<%=this.FK_Attr %>&DirType=0')">
                                        <span class="nav">到:<%# Eval("No") %> <%# Eval("Name") %></span></a></div>
                            </li>
                        </ItemTemplate>
                    </asp:Repeater> --%>
                </ul>
            </div>
            <div data-options="region:'center',noheader:true" style="overflow-y: hidden">
                <iframe id="context" scrolling="auto" frameborder="0" src="" style="width: 100%;
                    height: 100%;"></iframe>
            </div>
        </div>
    </div>
</body>
<script>
//ajax 提交
function save(){
	var keys = "";
	var SFSta=$("input[name=SFSta]:checked").attr("id");
	var SFShowModel=$("input[name=SFShowModel]:checked").attr("id");
	var TB_SFCaption=$("#TB_SFCaption").val();
	var TB_SFDefInfo=$("#TB_SFDefInfo").val();
	var SF_H=$("#SF_H").val();
	var SF_W=$("#SF_W").val();
	$.ajax({
		url:'<%=basePath%>WF/SubFlows/BtnSaveClick.do',
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		data:{SFSta:SFSta,SFShowModel:SFShowModel,TB_SFCaption:TB_SFCaption,
			TB_SFDefInfo:TB_SFDefInfo,SF_H:SF_H,SF_W:SF_W,FK_Node:<%--=nodeID--%>},
		async: false ,
		error: function(data){},
		success: function(data){
			var json = eval("("+data+")");
			if(json.success){
				keys = json.msg;
				alert(keys);
			}else{
				alert("保存失败");
			}
		}
	});
	if (window.opener != undefined) {
        window.top.returnValue = keys;
    } else {
        window.returnValue = keys;
    }
}
</script>
</html>