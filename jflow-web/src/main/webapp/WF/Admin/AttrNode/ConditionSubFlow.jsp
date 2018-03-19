<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/WF/head/head1.jsp"%>
	
<!-- <link rel="stylesheet" type="text/css" href="../CCBPMDesigner/normalize/css/demo.css" />
<link rel="stylesheet" type="text/css" href="../CCBPMDesigner//normalize/css/ns-default.css" />
<link rel="stylesheet" type="text/css" href="../CCBPMDesigner//normalize/css/ns-style-bar.css" />
<script type="text/javascript" src="../CCBPMDesigner/normalize/js/modernizr.custom.js"></script> -->

<style type="text/css">
    body
    {
        margin: 0px;
        padding: 0px;
    }
    .btn_Save
    {
        cursor: pointer;
    }
</style>
<script type="text/javascript">
	function ShowHidden(id){
		$("#"+id).toggle();
	}
</script>
<%
	String nodeid = request.getParameter("FK_Node");
    int nodeID =  Integer.parseInt(nodeid);
	BP.WF.Node nd = new BP.WF.Node(nodeID);
	BP.Sys.FrmSubFlow frmSubFlow = new BP.Sys.FrmSubFlow(nodeID);
	//控件状态
	boolean RB_Disable=false;
	boolean RB_Enable=false;
	boolean RB_Readonly=false;
    if (frmSubFlow.getSFSta() == BP.Sys.FrmSubFlowSta.Disable.getValue())
	{
		RB_Disable = true;
	}
	if (frmSubFlow.getSFSta() == BP.Sys.FrmSubFlowSta.Enable.getValue())
	{
		RB_Enable = true;
	}
	if (frmSubFlow.getSFSta() == BP.Sys.FrmSubFlowSta.Readonly.getValue())

	{
	 	RB_Readonly = true;
	}

	  //显示方式
	boolean RB_Table = false;
	boolean RB_Free = false;
	if (frmSubFlow.getSFShowModel() == BP.Sys.FrmWorkShowModel.Table.getValue())
	{
		RB_Table = true;
	}


	if (frmSubFlow.getSFShowModel() == BP.Sys.FrmWorkShowModel.Free.getValue())
	{
		RB_Free = true;
	}


	String TB_SFCaption=frmSubFlow.getSFCaption();
	String TB_SFDefInfo=frmSubFlow.getSFDefInfo();
	//高度,宽度.
	float SF_H=frmSubFlow.getSF_H();
	float SF_W=frmSubFlow.getSF_W();    
%>
<body>
	<table style="width: 100%; min-width: 800px;">
        <caption>
            <img src="../CCBPMDesigner/Img/Menu/SubFlows.png" border="0" alt="子流程" />节点【<%=nd.getName() %>】调用父子</caption>
        <tr>
            <td valign="top"  style=" width:20%;color:Gray">
                <fieldset>
                    <legend>父子流程定义</legend>
                    <ul>
                        <li>一些业务需求，需要多个流程协同才能完成，他们之间形成一个流程调用另外一个流程的情况，我们称为父子流程。</li>
                        <li>被吊起的成为子流程，吊起自称的节点叫做父流程节点，在子流程上，也可能吊起子流程，相对于这个当前流程来说，是子子流程(孙子流程)，ccbpm只研究两两关系，不研究跨代关系。</li>
                        <li>被吊起的成为子流程，吊起自称的节点叫做父流程节点。</li>
                    </ul>
                </fieldset>
                <fieldset>
                    <legend>关于父子流程控件</legend>
                    <ul>
                        <li>该控件的位置位于：/WF/SDKComponents/SubFlowDtl.ascx </li>
                        <li>如果您使用SDK的模式开发，仅仅需要把该控件拖入您的页面就可以了，而该控件的属性需要在右边配置。</li>
                        <li>如果您在节点表单里启用了该控件，您可以在这里设置他的属性。</li>
                    </ul>
                </fieldset>
            </td>
            <td style="width: 50%;" valign="top">

            <fieldset>
            <legend>控件状态 </legend>

             <a href="javascript:ShowHidden('state')">设置状态: </a>
            <div id="state" style=" display:none;color:Gray">
            <ul>
            <li>禁用:不使用控件，在表单上不可见。  </li>
            <li>启用:在表单上正常工作。</li>
            <li>只读:表单上可见，但是不能操作。</li>
            </ul>
            </div>

              <input type="radio" <% if(RB_Disable){%>checked=checked<%} %> ID="RB_Disable" name="SFSta" runat="server" />禁用
              <input type="radio" <% if(RB_Enable){%>checked=checked<%} %> ID="RB_Enable" name="SFSta" runat="server" />启用
              <input type="radio" <% if(RB_Readonly){%>checked=checked<%} %> ID="RB_Readonly" name="SFSta" runat="server" />只读
            </fieldset>

             <fieldset>
            <legend>显示方式</legend>

            <a href="javascript:ShowHidden('style')">控件显示个风格: </a>
            <div id="style" style="display:none;color:Gray">
            <ul>
            <li>目前仅仅支持表格方式. </li>
            <li>我们会在以后开发更多的风格支持，满足不同的用户需要. </li>
            </ul>
            </div>


            <input type="radio" <% if(RB_Table){%>checked=checked<%} %> ID="RB_Table" name="SFShowModel" runat="server" />表格方式
             <input type="radio" <% if(RB_Free){%>checked=checked<%} %> ID="RB_Free" name="SFShowModel" runat="server" />自由方式
            </fieldset>

              <fieldset>
            <legend>标题(显示控件头部) </legend>

             <a href="javascript:ShowHidden('title')">控件标题:</a>
            <div id="title" style=" display:none;color:Gray">
            <ul>
             <li>该文字显示到父子流程控件的头部，用来提示该控件的作用。</li>
             <li>默认为空。 </li>
            </ul>
            </div>
			<br>
            <input type="text" value="<%=TB_SFCaption %>" ID="TB_SFCaption"  runat="server" style="Width:95%"/>
            </fieldset>

          <fieldset>
            <legend>可手工启动的子流程 </legend>
            <a href="javascript:ShowHidden('xx')">设置可以手工启动的流程编号:</a>
            <div id="xx" style=" display:none;color:Gray">
            <ul>
            <li>单个流程设置格式为:001  </li>
            <li>多个流程用逗号分开比如: 001,002  </li>
            </ul>
            </div>
            <br>
            <input type="text" value="<%=TB_SFDefInfo %>" ID="TB_SFDefInfo"  runat="server" style="Width:95%" ToolTip="节点编号,例:101,102..."/>

            <br />
            <a href="../ConditionSubFlow.jsp?FK_Node=<%=nodeid %>&FK_Flow=<%=nd.getFK_Flow() %>" target=_blank >触发条件设置</a>
            </fieldset>

            <fieldset>
            <legend>控件的显示控制</legend>
            高度(设置0标识为100%): <input type="text" value="<%=SF_H %>" ID="SF_H"  runat="server" ToolTip="只可以输入数字"/>
            <br />
            宽度(设置0标识为100%): <input type="text" value="<%=SF_W %>" ID="SF_W"  runat="server" ToolTip="只可以输入数字"/>
            </fieldset>

            <input type="button" ID="BtnSave" runat="server" class="btn_Save" value="保存" OnClick="save()" />
              
            </td>
        </tr>
    </table>
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
			TB_SFDefInfo:TB_SFDefInfo,SF_H:SF_H,SF_W:SF_W,FK_Node:<%=nodeID%>},
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