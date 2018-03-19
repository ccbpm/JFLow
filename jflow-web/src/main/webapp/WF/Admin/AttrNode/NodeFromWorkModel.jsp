<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="BP.WF.RunModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import= "java.util.ArrayList"%>
<%@page import= "java.util.List"%>
<%@page import="cn.jflow.controller.wf.workopt.BaseController"%>
<%@page import="BP.WF.Template.*"%>
<%@page import="BP.WF.NodeFormType"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.DA.*"%>  
<%@include file  = "/WF/head/head1.jsp" %>
<link href="../../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="../../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="../../Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
    <script src="../../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="../../Scripts/CommonUnite.js" type="text/javascript"></script>
    <script src="../../Comm/JScript.js" type="text/javascript"></script>
    <style type="text/css">
        .DemoImg
        {
            width: 220px;
            border: 1px;
            border-color: Blue;
            margin: 0px;
        }
        .DemoImgTD
        {
            margin: 0px;
            vertical-align: top;
        }
    </style>
    <script type="text/javascript">
        function ChangeImg(val) {
            if (val == "0")
                this.BindingImg.src = "./Img/Tree.png";
            else
                this.BindingImg.src = "./Img/Tab.png";
        }
        function SelectImg(val) {
            if (val == "0")
                this.FrmImg.src = "./Img/FreeFrm.png";
            else
                this.FrmImg.src = "./Img/Col4Frm.JPG";
        }
        function SetDDLEnable(ctrl, val) {
            if (val == "enable")
                $("#" + ctrl).attr("disabled", "disabled");
            else
                $("#" + ctrl).removeAttr("disabled");
        }
        //默认选中第一个按钮
        function RB_Frm(){
        	$("#RB_Frm_0").attr("checked","checked");
        	$("#RB_CurrentForm").attr("checked","checked");
        }
    </script>
    <% 
        String nodeIDstr = request.getParameter("FK_Node");
        String nodeID = nodeIDstr;//int.Parse(nodeIDstr);
        BP.WF.Node nd = new BP.WF.Node(nodeID);
        BP.WF.Nodes nds = new BP.WF.Nodes(nd.getFK_Flow());
        
      //加载请选择一个节点表单
		//String NodeID = request.getParameter("NodeID");
		//Node nd = new Node(NodeID);
		//this.DDL_Frm.Items.Clear();
		//Nodes nds = new Nodes(nd.getHisFlow().getNo());
		
		HashMap<String,String> DDL_Frm = new HashMap<String,String>();
		for (Node item : nds.ToJavaList())
		{
			DDL_Frm.put(item.getNodeID()+"",item.getNodeID() + " " + item.getName());
		}
		
		//this.RB_CurrentForm.Attributes["onclick"] = "SetDDLEnable('" + this.DDL_Frm.ClientID + "','enable')";
		//this.RB_OtherForms.Attributes["onclick"] = "SetDDLEnable('" + this.DDL_Frm.ClientID + "','disable')";


		//Node nd = new Node(NodeID);
		
		boolean RB_FixFrm=false;     boolean RB_Frm_1=false;          boolean RB_CurrentForm=false;
		boolean RB_OtherForms=false; boolean RB_Frm_0=false;          boolean RB_SelfForm=false;
		boolean RB_SDKForm=false;    boolean RB_SheetTree=false;      boolean RB_tree=false;
		boolean RB_WebOffice=false;  boolean RB_WebOffice_Frm2=false; boolean RB_WebOffice_Frm3=false;
		boolean RB_tab=false;
		String DDL_FrmV="";String TB_CustomURL="";String TB_FormURL="";
		
		BtnLab btn = new BtnLab(Integer.parseInt(nodeID));

		if (btn.getWebOfficeWorkModel() == WebOfficeWorkModel.FrmFirst || btn.getWebOfficeWorkModel() == WebOfficeWorkModel.WordFirst)
		{
			nd.setFormType(NodeFormType.WebOffice);
		}

		switch (nd.getFormType())
		{
			 
			//加载使用ccbpm内置的节点表单自由表单
			case FreeForm:
				RB_FixFrm = true;
				RB_Frm_0=true; //.SelectedValue ="1";
				if (nd.getNodeFrmID().equals("ND"+nodeID))
				{
					RB_CurrentForm = true;
				}
				else
				{
					RB_OtherForms = true;

				}
				DDL_FrmV = nd.getNodeFrmID().substring(2);
				break;
			//加载使用嵌入式表单
			case SelfForm:
				RB_SelfForm = true;
				TB_CustomURL=nd.getFormUrl();
				break;
			//加载使用SDK表单
			case SDKForm:
				RB_SDKForm = true;
				TB_FormURL=nd.getFormUrl();
				break;
			//加载表单树
			case SheetTree:
				RB_SheetTree = true;
				RB_tree = true;
				break;
			case WebOffice: //公文表单.
				RB_WebOffice = true;
				RB_WebOffice_Frm2 = true;

				if (btn.getWebOfficeWorkModel() == WebOfficeWorkModel.FrmFirst)
				{
					RB_WebOffice_Frm2 = true;
				}
				else
				{
					RB_WebOffice_Frm3 = true;
				}

			  //  this.RB_tree.Checked = true;
				break;
			//加载禁用(对多表单流程有效)
			case DisableIt:
				RB_SheetTree = true;
				RB_tab =true;
				break;
		}
    %>
<body topmargin="0" leftmargin="0">
    <table style="width: 100%;">
        <caption>
            <img src="../../Img/Form.png" border="0" style="vertical-align: middle;"/>节点[<%=nd.getName() %>]表单解决方案</caption>
        <!-- =================================== 使用ccbpm内置的节点表单 -->
        <tr>
            <th colspan="2">
                <input onclick=RB_Frm() type="radio" <%if(RB_FixFrm){ %>checked="checked"<%} %> ID="RB_FixFrm" runat="server" value="" name="xxx" />使用ccbpm内置的节点表单
            </th>
        </tr>
        <tr>
            <td class="DemoImgTD">
                <a href="http://ccbpm.mydoc.io/?v=5404&t=17923" target="_blank">
                    <img src="./Img/FreeFrm.png" id="FrmImg" class="DemoImg" alt="点击放大" />
                </a>
            </td>
            <td>
                <table>
                    <tr>
                        <td nowrap="nowrap">
                            呈现风格：
                        </td>
                        <td>
                            <input type="radio" <%if(RB_Frm_0){ %>checked="checked"<%} %> ID="RB_Frm_0" runat="server" value="" name="x22xy" onclick="SelectImg('0')"/>自由模式
                            <input type="radio" <%if(RB_Frm_1){ %>checked="checked"<%} %> ID="RB_Frm_1" runat="server" value="" name="x22xy" onclick="SelectImg('1')"/>傻瓜模式
                        </td>
                        <td>

                            [<a href="javascript:WinOpen('');"><!-- ../CCFormDesigner/FormDesigner.htm?FK_MapData=ND<%=nodeIDstr %> -->设计自由表单(Html5)</a>]

                                 [<a href="javascript:WinOpen('<%=basePath %>WF/MapDef/CCForm/Frm.jsp?FK_Flow=<%=nd.getFK_Flow() %>&FK_MapData=ND<%=nodeIDstr %>&UserNo=<%=BP.Web.WebUser.getNo() %>&SID=<%=BP.Web.WebUser.getSID()%>');">
                                设计自由表单(Silverlight)</a>]

                                  [<a href="javascript:WinOpen('../AttrNode/SortingMapAttrs.htm?FK_Flow=<%=nd.getFK_Flow() %>&FK_MapData=ND<%=nodeIDstr %>&UserNo=<%=BP.Web.WebUser.getNo() %>&SID=<%=BP.Web.WebUser.getSID()%>');">
                                手机表单</a>] - [设计傻瓜表单]
                        </td>
                    </tr>
                    <tr>
                        <td>表单引用:</td>
                        <td>
                            <input type="radio" <%if(RB_CurrentForm){ %>checked="checked"<%} %> ID="RB_CurrentForm" runat="server" value="" name="xxy" onclick="SetDDLEnable('DDL_Frm','enable')"/>当前节点表单
                            <input type="radio" <%if(RB_OtherForms){ %>checked="checked"<%} %> ID="RB_OtherForms" runat="server" value="" name="xxy" onclick="SetDDLEnable('DDL_Frm','disable')"/>其他节点表单
                        </td>
                        <td>
                            <font color="gray">当前节点可以设置与其他节点共用一个表单</font>
                        </td>
                    </tr>
                    <tr>
                        <td>要引用的节点:</td>
                        <td>
                            <!-- <asp:DropDownList ID="DDL_Frm" runat="server">
                            </asp:DropDownList> -->
                            <select id="DDL_Frm" style="max-width:300px;">
                                    	<% for(Map.Entry<String, String> entry:DDL_Frm.entrySet()){%>
                                    		<option <%if(DDL_FrmV.endsWith(entry.getKey())){ %>selected="selected"<%} %> value='<%=entry.getKey() %>'><%=entry.getValue()%></option>
										<%}%>
                            </select>
                        </td>
                        <td>
                            <font color="gray">对于选择使用【其他节点表单】设置有效. </font>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Entity.FrmWorkCheck&PK=<%=nodeID%>')">
                                设置审核组件属性</a>

                               
                        </td>
                        <td colspan="2">
                            <font color="gray">ccbpm为您提供了一个demo流程\\流程树\\表单解决方案\\审核组件流程，使用审核组件可以方便用户设计审批类的流程。</font>
                        </td>
                    </tr>
                    <%--
             <tr>
            <td colspan="2">
             审核组件应用场景  
            <ul>
            <li>在一些流程里，通常都是开始节点填写一个表单，之后的节点做审批，这就用到了审核组件。</li>
            <li>应用步骤:</li>
            </ul>
            <%--，说明：在使用审核组件的模式下，ccbpm允许使用集成表单，比如A，B，C, D四个节点的表单，当前节点表单C,可以使用节点D的表单。  </td>
            </tr>
                    --%>
                </table>
            </td>
        </tr>
        <!-- =================================== 使用 嵌入式表单  -->
        <tr>
            <th colspan="2" class="DemoImgTD">
                <input type="radio" <%if(RB_SelfForm){ %>checked="checked"<%} %> ID="RB_SelfForm" runat="server" name="xxx" value="" />使用嵌入式表单
            </th>
        </tr>
        <tr>
            <td class="DemoImgTD">
                <a href="http://ccbpm.mydoc.io/?v=5404&t=17925" target="_blank">
                    <img src="./Img/SelfFrm.png" class="DemoImg" alt="点击放大" />
                </a>
            </td>
            <td>
                请输入嵌入式表单的URL:
                <input type="text" ID="TB_CustomURL" value="<%=TB_CustomURL %>" runat="server" style="Width:424px;Height:20px" />
                <br />
                <font color="gray">
                    <ul>
                        <li>该表单必须有javascript 的 Save保存方法，该Save方法里如果需要执行存盘，并验证。</li>
                        <li>如果使用绝对路径可以使用ccbpm的全局变量@SDKFromServHost ，比如: @SDKFromServHost/MyFile.jsp </li>
                        <li>ccbpm团队为您提供了一个嵌入式表单的 demo ，位于:\\流程树\\表单解决方案\\嵌入式表单. </li>
                         <li> <a href="javascript:WinOpen('SDKComponents.jsp?DoType=FrmCheck&FK_Node=<%=nodeID%>')">组件属性</a></li>
                    </ul>
                </font>
            </td>
        </tr>
        <!-- =================================== 使用SDK表单 -->
        <tr>
            <th colspan="2" class="DemoImgTD">
                <input type="radio" <%if(RB_SDKForm){ %>checked="checked"<%} %> ID="RB_SDKForm" runat="server" name="xxx" value="" />使用SDK表单
            </th>
        </tr>
        <tr>
            <td class="DemoImgTD">
                <a href="http://ccbpm.mydoc.io/?v=5404&t=18388" target="_blank">
                    <img src="./Img/SDKFrm.png" class="DemoImg" alt="点击放大" />
                </a>
            </td>
            <td>
                请输入表单的URL:
                <input type="text" ID="TB_FormURL" value="<%=TB_FormURL %>" runat="server" style="Width:424px;Height:20px"/>
                <br />
                <font color="gray">
                    <ul>
                        <li>SDK表单就是ccbpm把界面的展现完全交给了开发人员处理,开发人员只要设计一个表单,增加一个发送按钮,调用ccbpm的发送API就可以完成</li>
                        <li>如果使用绝对路径可以使用ccbpm的全局变量@SDKFromServHost ，比如: @SDKFromServHost/MyFile.jsp </li>
                        <li>ccbpm团队为您提供了一个demo流程 \\流程树\\SDK流程\\ 该目录下有很多SDK模式的流程供您参考。</li>
                    </ul>
                </font>
            </td>
        </tr>

        <!-- =================================== 绑定多表单 -->
        <tr>
            <th colspan="2" class="DemoImgTD">
                <input type="radio" <%if(RB_SheetTree){ %>checked="checked"<%} %> ID="RB_SheetTree" runat="server" name="xxx" value="" />绑定多表单
            </th>
        </tr>

        <tr>
            <td class="DemoImgTD">
                <a href="http://blog.csdn.net/jflows/article/details/50160423" target="_blank">
                    <img src="./Img/Tree.png" id="BindingImg"   class="DemoImg" alt="点击放大" />
                </a>
            </td>
            <td>
                呈现风格：
                <input type="radio" <%if(RB_tree){ %>checked="checked"<%} %> ID="RB_tree" runat="server" Checked=true value="表单树" name="x22axy" onclick="ChangeImg('0')"/>表单树
                <input type="radio" <%if(RB_tab){ %>checked="checked"<%} %> ID="RB_tab" runat="server" value="Tab标签页" name="x22axy" onclick="ChangeImg('1')"/>Tab标签页
                [<a href="javascript:WinOpen('<%=basePath %>WF/Admin/BindFrms.jsp?FK_Node=<%=nd.getNodeID()%>&FK_Flow=<%=nd.getFK_Flow()%>&DoType=SelectedFrm')">
                    绑定/取消绑定</a> ] [ <a href="javascript:WinOpen('<%=basePath %>WF/Admin/BindFrms.jsp?FK_Node=<%=nd.getNodeID()%>&FK_Flow=<%=nd.getFK_Flow()%>')">
                        设置表单字段控件权限</a>]
                <br />
                <font color="gray">
                    <ul>
                        <li>我们把一个节点需要绑定多个表单的节点称为多表单节点，它有两种展现方式，标签页与表单树。</li>
                        <li>对应的流程demo:\\流程树\\表单解决方案\\树形表单与多表单 </li>
                    </ul>
                </font>
            </td>
        </tr>


        
        <!-- =================================== 绑定公文表单 -->
        <tr>
            <th colspan="2" class="DemoImgTD">
                <input type="radio" <%if(RB_WebOffice){ %>checked="checked"<%} %> ID="RB_WebOffice" runat="server" name="xxx" value="" />公文表单
            </th>
        </tr>
        <tr>
            <td class="DemoImgTD">
                <a href="http://blog.csdn.net/jflows/article/details/50160423" target="_blank">
                    <img src="./Img/Doc.png" id="Img1"   class="DemoImg" alt="点击放大" />
                </a>
            </td>
            <td>
                呈现风格：
                <input type="radio" <%if(RB_WebOffice_Frm2){ %>checked="checked"<%} %> ID="RB_WebOffice_Frm2" runat="server" Checked="true" value="" name="RB_Doc" />表单在前面
                <input type="radio" <%if(RB_WebOffice_Frm3){ %>checked="checked"<%} %> ID="RB_WebOffice_Frm3" runat="server" value="" name="RB_Doc" />公文在前面

                <ul>
                <li> <a href="javascript:WinOpen('<%=basePath %>WF/Admin/BindFrms.jsp?FK_Node=<%=nd.getNodeID()%>&FK_Flow=<%=nd.getFK_Flow()%>&DoType=SelectedFrm')">
                    设置附件权限</a>  - 
                    <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Template.BtnLabExtWebOffice&PK=<%=nd.getNodeID()%>&FK_Flow=<%=nd.getFK_Flow()%>')" >
                        设置公文按钮权限</a>
                        </li>
                <li>
                         [<a href="javascript:WinOpen('');"><!-- ../CCFormDesigner/FormDesigner.jsp?FK_MapData=ND<%=nodeIDstr %> -->
                                设计自由表单(Html5)</a>]

                                 [<a href="javascript:WinOpen('<%=basePath %>WF/MapDef/CCForm/Frm.jsp?FK_Flow=<%=nd.getFK_Flow() %>&FK_MapData=ND<%=nodeIDstr %>&UserNo=<%=BP.Web.WebUser.getNo() %>&SID=<%=BP.Web.WebUser.getSID()%>');">
                                设计自由表单(Silverlight)</a>]
                                [手机表单]-[设计傻瓜表单]
                          </li>
                </ul>
                <font color="gray">
                    <ul>
                        <li>我们把一个节点需要绑定多个表单的节点称为多表单节点，它有两种展现方式，标签页与表单树。</li>
                        <li>对应的流程demo:\\流程树\\表单解决方案\\树形表单与多表单 </li>
                    </ul>
                </font>
            </td>
        </tr>


        <tr>
            <td colspan="2" class="DemoImgTD">
                <input type="button" class="easyui-linkbutton" ID="Btn_Save" runat="server" value="保存" OnClick="save(0)" />
                <input type="button" class="easyui-linkbutton" ID="Btn_Cancel" runat="server" value="保存并关闭"
                    OnClick="save(1)" />
            </td>
        </tr>
    </table>
</body>
<script>
//ajax 提交
function save(x){
	var keys = "";
	var xxy=$("input[name=xxy]:checked").attr("id");
	var x22xy=$("input[name=x22xy]:checked").attr("id");
	var xxx=$("input[name=xxx]:checked").attr("id");
	var DDL_Frm=$("#DDL_Frm").val();
	var TB_CustomURL=$("#TB_CustomURL").val();
	var TB_FormURL=$("#TB_FormURL").val();
	var x22axy=$("input[name=x22axy]:checked").attr("id");
	var RB_Doc=$("input[name=RB_Doc]:checked").attr("id");

	$.ajax({
		url:'<%=basePath%>WF/NodeFromWorkModel/BtnSaveClick.do',
		type:'post', //数据发送方式
		dataType:'json', //接受数据格式
		data:{xxy:xxy,x22xy:x22xy,xxx:xxx,DDL_Frm:DDL_Frm,TB_CustomURL:TB_CustomURL,TB_FormURL:TB_FormURL,x22axy:x22axy,RB_Doc:RB_Doc,FK_Node:'<%=nodeIDstr%>'},
		async: false ,
		error: function(data){},
		success: function(json){
			keys = json.msg;
			alert(keys);
		}
	});
	if (window.opener != undefined) {
        window.top.returnValue = keys;
    } else {
        window.returnValue = keys;
    }
	if(x>0){
		window.close();
	}
}
</script>
</html>