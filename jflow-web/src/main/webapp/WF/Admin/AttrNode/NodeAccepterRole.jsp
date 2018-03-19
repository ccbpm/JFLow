<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="cn.jflow.system.ui.core.RadioButton"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="BP.WF.Template.NodeStations"%>
<%@page import="BP.WF.Template.NodeDepts"%>
<%@page import="BP.WF.Template.NodeEmps"%>
<%@page import="BP.WF.Template.NodeDept"%>
<%@page import="BP.WF.Template.NodeStationAttr"%>
<%@page import="BP.WF.Template.AccepterRole.*"%>
<%@page import="BP.WF.DeliveryWay"%>
<%@page import="BP.WF.RunModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import= "java.util.ArrayList"%>
<%@page import= "java.util.List"%>
<%@ include file="/WF/head/head1.jsp"%>
 <link href="../../Comm/Style/CommStyle.css" rel="stylesheet" type="text/css" />
 <link href="../../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
 <link href="../../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
 <script src="../../Scripts/CommonUnite.js" type="text/javascript"></script>
 <script src="../../Comm/JScript.js" type="text/javascript"></script>
 <script src="../../Scripts/CommonUnite.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Comm/JScript.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Comm/JS/Calendar/WdatePicker.js" defer="defer"
        type="text/javascript"></script>
<link href="../../../DataUser/Style/Table0.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../../Img/ccbpm.ico" type="image/x-icon" />

<style type="text/css">
    body
    {
         margin: 0 auto;
         font-size: 16px;
         color: #000;
         line-height: 25px;
         width: 100%;
         font-family: 宋体;
    }
    .Icon
    {
         width: 16px;
         height: 16px;
    }
    #mainPanel
    {
         background-color: #d0d0d0;
         position: relative !important;
       
    }
	.Btn
	{
	    border: 0;
	    background: #4D77A7;
	    color: #FFF;
	    font-size: 12px;
	    padding: 6px 10px;
	    margin: 7px 3px 7px 3px;
	}
    .icon-reddot {
		background:url('../../Img/OK.png') no-repeat center center;
    }
</style>
<script language="javascript" type="text/javascript">
	/**
	 * tree选中样式
	 * 保存后将选中样式更新
	 */
	function doSelectTree() {
		if (document.getElementById("RB_0").checked) {
			onclickSJ(1, true);
		} else if (document.getElementById("RB_1").checked) {
			onclickSJ(2, true);
		} else if (document.getElementById("RB_3").checked) {
			onclickSJ(3, true);
		} else if (document.getElementById("RB_9").checked) {
			onclickSJ(4, true);
		} else if (document.getElementById("RB_10").checked) {
			onclickSJ(5, true);
		} else if (document.getElementById("RB_11").checked) {
			onclickSJ(6, true);
		} else if (document.getElementById("RB_14").checked) {
			onclickSJ(7, true);
		} else if (document.getElementById("RB_7").checked) {
			onclickSJ(8, true);
		} else if (document.getElementById("RB_6").checked) {
			onclickSJ(9, true);
		} else if (document.getElementById("RB_8").checked) {
			onclickSJ(10, true);
		} else if (document.getElementById("RB_2").checked) {
			onclickSJ(11, true);
		} else if (document.getElementById("RB_12").checked) {
			onclickSJ(12, true);
		} else if (document.getElementById("RB_4").checked) {
			onclickSJ(13, true);
		} else if (document.getElementById("RB_5").checked) {
			onclickSJ(14, true);
		} else if (document.getElementById("RB_13").checked) {
			onclickSJ(15, true);
		} else if (document.getElementById("RB_15").checked) {
			onclickSJ(16, true);
		} else if (document.getElementById("RB_16").checked) {
			onclickSJ(17, true);
		} else if (document.getElementById("RB_17").checked) {
			onclickSJ(18, true);
		}
	}
        $(function () {
            //按当前操作员所属组织结构逐级查找岗位
            $("#RB0").hide();
            $("#YC_1").hide();
            //按节点绑定的部门计算
            $("#RB1").hide();
            $("#YC_02").hide();
            //按节点绑定的人员计算
            $("#RB3").hide();
            $("#YC_03").hide();
            //按绑定的岗位与部门交集计算
            $("#RB9").hide();
            $("#YC_04").hide();
            //按绑定的岗位计算并且以绑定的部门集合为纬度
            $("#RB10").hide();
            $("#YC_05").hide();
            //按指定节点的人员岗位计算
            $("#RB11").hide();
            $("#YC_06").hide();
            //仅按绑定的岗位计算
            $("#RB14").hide();
            $("#YC_07").hide();
            //仅按绑定的岗位计算
            $("#RB7").hide();
            $("#YC_08").hide();
            //与上一节点处理人员相同
            $("#RB6").hide();
            $("#YC_09").hide();
            //与指定节点处理人相同
            $("#RB8").hide();
            $("#YC_10").hide();
            //按设置的SQL获取接受人计算
            $("#RB2").hide();
            $("#YC_11").hide();
            //按SQL确定子线程接受人与数据源
            $("#RB12").hide();
            $("#YC_12").hide();
            //由上一节点发送人通过“人员选择器”选择接受人
            $("#RB4").hide();
            $("#YC_13").hide();
            //按上一节点表单指定的字段值作为本步骤的接受人
            $("#RB5").hide();
            $("#YC_14").hide();
            //由上一节点的明细表来决定子线程的接受人
            $("#RB13").hide();
            $("#YC_15").hide();
            //由FEE来决定
            $("#RB15").hide();
            $("#YC_16").hide();
            //按绑定部门计算，该部门一人处理标识该工作结束(子线程)
            $("#RB16").hide();
            $("#YC_17").hide();
            //按ccBPM的BPM模式处理
            $("#RB100").hide();
            $("#YC_18").hide();
            doSelectTree();

        });
        function onclickSJ(runModel, isInit) {
            //按当前操作员所属组织结构逐级查找岗位
            $("#RB0").hide();
            $("#YC_1").hide();
            //按节点绑定的部门计算
            $("#RB1").hide();
            $("#YC_02").hide();
            //按节点绑定的人员计算
            $("#RB3").hide();
            $("#YC_03").hide();
            //按绑定的岗位与部门交集计算
            $("#RB9").hide();
            $("#YC_04").hide();
            //按绑定的岗位计算并且以绑定的部门集合为纬度
            $("#RB10").hide();
            $("#YC_05").hide();
            //按指定节点的人员岗位计算
            $("#RB11").hide();
            $("#YC_06").hide();
            //仅按绑定的岗位计算
            $("#RB14").hide();
            $("#YC_07").hide();
            //与上一节点处理人员相同
            $("#RB7").hide();
            $("#YC_08").hide();
            //与开始节点处理人相同
            $("#RB6").hide();
            $("#YC_09").hide();
            //与指定节点处理人相同
            $("#RB8").hide();
            $("#YC_10").hide();
            //按设置的SQL获取接受人计算
            $("#RB2").hide();
            $("#YC_11").hide();
            //按SQL确定子线程接受人与数据源
            $("#RB12").hide();
            $("#YC_12").hide();
            //由上一节点发送人通过“人员选择器”选择接受人
            $("#RB4").hide();
            $("#YC_13").hide();
            //按上一节点表单指定的字段值作为本步骤的接受人
            $("#RB5").hide();
            $("#YC_14").hide();
            //由上一节点的明细表来决定子线程的接受人
            $("#RB13").hide();
            $("#YC_15").hide();
            //由FEE来决定
            $("#RB15").hide();
            $("#YC_16").hide();
            //按绑定部门计算，该部门一人处理标识该工作结束(子线程)
            $("#RB16").hide();
            $("#YC_17").hide();
            //按ccBPM的BPM模式处理
            $("#RB100").hide();
            $("#YC_18").hide();
            if (runModel == 1) {
                $("#RB0").show();
                document.getElementById("RB_0").checked = "checked";
                $("#YC_1").show();
            }
            if (runModel == 2) {
                $("#RB1").show();
                document.getElementById("RB_1").checked = "checked";
                $("#YC_02").show();
            }
            if (runModel == 3) {
                $("#RB3").show();
                document.getElementById("RB_3").checked = "checked";
                $("#YC_03").show();
            }
            if (runModel == 4) {
                $("#RB9").show();
                document.getElementById("RB_9").checked = "checked";
                $("#YC_04").show();
            }
            if (runModel == 5) {
                $("#RB10").show();
                document.getElementById("RB_10").checked = "checked";
                $("#YC_05").show();
            }
            if (runModel == 6) {
                $("#RB11").show();
                document.getElementById("RB_11").checked = "checked";
                $("#YC_06").show();
            }
            if (runModel == 7) {
                $("#RB14").show();
                document.getElementById("RB_14").checked = "checked";
                $("#YC_07").show();
            }
            if (runModel == 8) {
                $("#RB7").show();
                document.getElementById("RB_7").checked = "checked";
                $("#YC_08").show();
            }
            if (runModel == 9) {
                $("#RB6").show();
                document.getElementById("RB_6").checked = "checked";
                $("#YC_09").show();
            }
            if (runModel == 10) {
                $("#RB8").show();
                document.getElementById("RB_8").checked = "checked";
                $("#YC_10").show();
            }
            if (runModel == 11) {
                $("#RB2").show();
                document.getElementById("RB_2").checked = "checked";
                $("#YC_11").show();
            }
            if (runModel == 12) {
                $("#RB12").show();
                document.getElementById("RB_12").checked = "checked";
                $("#YC_12").show();
            }
            if (runModel == 13) {
                $("#RB4").show();
                document.getElementById("RB_4").checked = "checked";
                $("#YC_13").show();
            }
            if (runModel == 14) {
                $("#RB5").show();
                document.getElementById("RB_5").checked = "checked";
                $("#YC_14").show();
            }
            if (runModel == 15) {
                $("#RB13").show();
                document.getElementById("RB_13").checked = "checked";
                $("#YC_15").show();
            }
            if (runModel == 16) {
                $("#RB15").show();
                document.getElementById("RB_15").checked = "checked";
                $("#YC_16").show();
            }
            if (runModel == 17) {
                $("#RB16").show();
                document.getElementById("RB_16").checked = "checked";
                $("#YC_17").show();
            }
            if (runModel == 18) {
                $("#RB100").show();
                document.getElementById("RB_17").checked = "checked";
                $("#YC_18").show();
            }
            if (isInit){
            	$(".icon-reddot").removeClass("icon-reddot");	// 删除旧的选中样式
            	//
                $('#tt').tree('update', {
                    target: $('#tt').tree('find', "node_"+runModel).target,
                    iconCls: 'icon-reddot'
                });
            }
        }
    </script>
    <style type="text/css">
        li
        {
            font-size: 12px;
        }
        img
        {
            border: none;
        }
    </style>
    <%
        String nodeID = request.getParameter("FK_Node");
		//System.out.println("nodeID:"+nodeID);
        // int.Parse(this.Request.QueryString["FK_Node"]);
        Node nd = new Node(nodeID);
        SimpleDateFormat sdf=new SimpleDateFormat("MMddhhmmss");    
		String k=sdf.format(new java.util.Date());
        NodeStations nss = new NodeStations();
        nss.Retrieve(NodeStationAttr.FK_Node, nodeID);

        NodeDepts ndepts = new NodeDepts();
        ndepts.Retrieve(NodeStationAttr.FK_Node, nodeID);
        
        NodeEmps nEmps = new NodeEmps();
        nEmps.Retrieve(NodeStationAttr.FK_Node, nodeID);
        
        
	//加载 06.按上一节点表单指定的字段值作为本步骤的接受人
	String str = nodeID.toString().substring(0,nodeID.toString().length() - 2);
	//System.out.println("str=="+str);
    BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs("ND" + str + "Rpt");
    HashMap<String,String> DDL_5 = new HashMap<String,String>();
    for (BP.Sys.MapAttr item : BP.Sys.MapAttrs.convertMapAttrs(attrs)) {
    	DDL_5.put(item.getKeyOfEn(), item.getName());
	}
	// 加载 09.与指定节点处理人相同
	//BP.WF.Node nd = new BP.WF.Node(nodeID);
	/* this.CBL_8.Items.Clear();
	//this.CBL_11.Items.Clear(); */
	BP.WF.Nodes nds = new BP.WF.Nodes(nd.getHisFlow().getNo());
	HashMap<String,String> CBL_8 = new HashMap<String,String>();
	//HashMap<String,String> CBL_11 = new HashMap<String,String>();
	for (BP.WF.Node item : nds.ToJavaList()) {
		//CBL_8
		CBL_8.put(item.getNodeID() + " "+ item.getName(), item.getName());
	}

	//BP.WF.Node nd = new Node(this.getNodeID());

	// 是否可以分配工作？
	boolean CB_IsSSS = nd.getIsTask();

	// 是否启用自动记忆功能
	boolean CB_IsRememme = nd.getIsRememberMe();

	// 本节点接收人不允许包含上一步发送人
	 boolean CB_IsExpSender = nd.getIsExpSender();

	// 节点访问规则
	DeliveryWay intStrTem = nd.getHisDeliveryWay();
	String intStr = intStrTem.getValue()+"";
	// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
	// string member and was converted to Java 'if-else' logic:
	// switch (intStr.ToString())
	// ORIGINAL LINE: case "0":
	boolean RB_0=false;boolean RB_1=false;boolean RB_2=false;
	boolean RB_3=false;boolean RB_4=false;boolean RB_5=false;
	boolean RB_6=false;boolean RB_7=false;boolean RB_8=false;
	boolean RB_9=false;boolean RB_10=false;boolean RB_11=false;
	boolean RB_12=false;boolean RB_13=false;boolean RB_14=false;
    boolean RB_15=false;boolean RB_16=false;boolean RB_17=false;
	String TB_2="";String TB_12="";String TB_13="";String DDL_5Value="";
	List<String> CBL_8List = new ArrayList<String>();
	List<String> strDLP = new ArrayList<String>();
	if (intStr.toString().equals("0")) {

		RB_0 = true;
	}
	// ORIGINAL LINE: case "1":
	else if (intStr.toString().equals("1")) {
		RB_1 = true;
	}
	// ORIGINAL LINE: case "2":
	else if (intStr.toString().equals("2")) {
		RB_2 = true;
		TB_2=nd.getDeliveryParas(); // dt.Rows[0]["DeliveryParas"].ToString();
	}
	// ORIGINAL LINE: case "3":
	else if (intStr.toString().equals("3")) {
		RB_3 = true;
	}
	// ORIGINAL LINE: case "4":
	else if (intStr.toString().equals("4")) {
		RB_4 = true;
	}
	// ORIGINAL LINE: case "5":
	else if (intStr.toString().equals("5")) {
		RB_5 = true;
		DDL_5Value = nd.getDeliveryParas();
	}
	// ORIGINAL LINE: case "6":
	else if (intStr.toString().equals("6")) {
		RB_6 = true;
	}
	// ORIGINAL LINE: case "7":
	else if (intStr.toString().equals("7")) {
		RB_7 = true;
	}
	// ORIGINAL LINE: case "8":
	else if (intStr.toString().equals("8")) {
		RB_8 = true;

		String paras = nd.getDeliveryParas();
		//System.out.out.print("88:"+paras);
		String[] tem = paras.split(",");
		for (int i=0;i<tem.length;i++){
			CBL_8List.add(i, tem[i]);
		}
		//for (String str : strList) {
			/* for (ListItem item : this.CBL_8.Items) {
				if (str.equals(item.getValue())) {
					item.Selected = true;
				}
			} */
			
		//}
	}
	// ORIGINAL LINE: case "9":
	else if (intStr.toString().equals("9")) {
		RB_9 = true;
	}
	// ORIGINAL LINE: case "10":
	else if (intStr.toString().equals("10")) {
		RB_10 = true;
	}
	// ORIGINAL LINE: case "11":
	else if (intStr.toString().equals("11")) {
		RB_11 = true;
		String strRB = nd.getDeliveryParas();
		//System.out.print("111:"+strRB);
		String[] tem = strRB.split(",");
		for (int i=0;i<tem.length;i++){
			strDLP.add(i, tem[i]);
		}
		/* for (String str : strDLP) {
			for (ListItem item : this.CBL_11.Items) {
				if (str.equals(item.getValue())) {
					item.Selected = true;
				}
			}
		} */
	}
	// ORIGINAL LINE: case "12":
	else if (intStr.toString().equals("12")) {
		RB_12 = true;
		TB_12=nd.getDeliveryParas();
	}
	// ORIGINAL LINE: case "13":
	else if (intStr.toString().equals("13")) {
		RB_13 = true;
		TB_13=nd.getDeliveryParas();
	}
	// ORIGINAL LINE: case "14":
	else if (intStr.toString().equals("14")) {
		RB_14 = true;
	}
	// ORIGINAL LINE: case "15":
	else if (intStr.toString().equals("15")) {
		RB_15 = true;
	}
	// ORIGINAL LINE: case "16":
	else if (intStr.toString().equals("16")) {
		RB_16 = true;
	}
	// ORIGINAL LINE: case "17":
	else if (intStr.toString().equals("17")) {
		RB_17 = true;
	}
%>
    <body>
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',split:true" style="width: 260px; height: auto;">
            <div class="easyui-tree" style="height: auto;" fit="true" border="false">
                <ul id="tt" class="easyui-tree" style="height: auto;">
                    <li><span>访问规则</span>
                        <ul>
                            <li><span>按组织结构绑定</span>
                                <ul>
                                    <li id="node_1">
                                        <div>
                                            <a id="A_1" class='l-link' href="javascript:" onclick="onclickSJ(1)"><span class="nav">按照岗位智能计算</span></a></div>
                                    </li>
                                    <li id="node_2">
                                        <div>
                                            <a id="A_2" class='1-link' href="javascript:" onclick="onclickSJ(2)"><span class="nav">按节点绑定的部门计算</span></a></div>
                                    </li>
                                    <li id="node_3">
                                        <div>
                                            <a id="A_3" class='l-link' href="javascript:" onclick="onclickSJ(3)"><span class="nav">按节点绑定的人员计算
                                            </span></a>
                                        </div>
                                    </li>
                                    <li id="node_4">
                                        <div>
                                            <a id="A_4" class='l-link' href="javascript:" onclick="onclickSJ(4)"><span class="nav">按绑定的岗位与部门交集计算</span></a></div>
                                    </li>
                                    <li id="node_5">
                                        <div>
                                            <a id="A_5" class='l-link' href="javascript:" onclick="onclickSJ(5)"><span class="nav">按绑定的岗位计算并且以绑定的部门集合为纬度</span></a></div>
                                    </li>
                                    <li id="node_6">
                                        <div>
                                            <a id="A_6" class='l-link' href="javascript:" onclick="onclickSJ(6)"><span class="nav">按指定节点的人员岗位计算</span></a></div>
                                    </li>
                                    <li id="node_7">
                                        <div>
                                            <a id="A_7" class='l-link' href="javascript:" onclick="onclickSJ(7)"><span class="nav">仅按绑定的岗位计算</span></a></div>
                                    </li>
                                    <li id="node_17">
                                        <div>
                                            <a id="A_17" class='l-link' href="javascript:" onclick="onclickSJ(17)"><span class="nav">按绑定部门计算，该部门一人处理标识该工作结束(子线程)</span></a></div>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <ul>
                            <li><span>按访问规则选项</span>
                                <ul>
                                    <li id="node_8">
                                        <div>
                                            <a id="A_8" class='l-link' onclick="onclickSJ(8)" href="javascript:"><span class="nav">与开始节点处理人相同</span></a></div>
                                    </li>
                                    <li id="node_9">
                                        <div>
                                            <a id="A_9" class='l-link' onclick="onclickSJ(9)" href="javascript:"><span class="nav">与上一节点处理人员相同</span></a></div>
                                    </li>
                                    <li id="node_10">
                                        <div>
                                            <a id="A_10" class='l-link' onclick="onclickSJ(10)" href="javascript:"><span class="nav">与指定节点处理人相同</span></a></div>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <ul>
                            <li><span>按自定义SQL查询</span>
                                <ul>
                                    <li id="node_11">
                                        <div>
                                            <a id="A_11" class='l-link' onclick="onclickSJ(11)" href="javascript:"><span class="nav">按设置的SQL获取接受人计算</span></a></div>
                                    </li>
                                    <li id="node_12">
                                        <div>
                                            <a id="A_12" class='l-link' onclick="onclickSJ(12)" href="javascript:"><span class="nav">按SQL确定子线程接受人与数据源</span></a></div>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <ul>
                            <li><span>其他方式</span>
                                <ul>
                                    <li id="node_13">
                                        <div>
                                            <a id="A_13" class='l-link' onclick="onclickSJ(13)" href="javascript:"><span class="nav">由上一节点发送人通过“人员选择器”选择接受人</span></a></div>
                                    </li>
                                    <li id="node_14">
                                        <div>
                                            <a id="A_14" class='l-link' onclick="onclickSJ(14)" href="javascript:"><span class="nav">按上一节点表单指定的字段值作为本步骤的接受人</span></a></div>
                                    </li>
                                    <li id="node_15">
                                        <div>
                                            <a id="A_15" class='l-link' onclick="onclickSJ(15)" href="javascript:"><span class="nav">由上一节点的明细表来决定子线程的接受人</span></a></div>
                                    </li>
                                    <li id="node_16">
                                        <div>
                                            <a id="A_16" class='l-link' onclick="onclickSJ(16)" href="javascript:"><span class="nav">由FEE来决定</span></a></div>
                                    </li>
                                    <li id="node_18">
                                        <div>
                                            <a id="A_18" class='l-link' onclick="onclickSJ(18)" href="javascript:"><span class="nav">
                                                按ccBPM的BPM模式处理</span></a></div>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <div class="easyui-layout" data-options=" region:'center',title:'访问规则设置' ">
            <div data-options="region:'center',border:false">
                <div id="tabs" class="easyui-tabs" data-options="fit:true ,border:false">
                    <div title="节点[<%=nd.getName() %>]接受人规则设置">
                        <table style="width: 100%;">
                            <!-- ===================================  01.按当前操作员所属组织结构逐级查找岗位 -->
                            <tr id="RB0">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_0){%>checked="checked"<%}%> ID="RB_0" Text="按照岗位智能计算" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeStations&NodeID=<%=nd.getNodeID() %>&r=<%=k %>&num=None')">
                                            设置/更改岗位(<%=nss.size() %>)</a> | <a href='http://ccbpm.mydoc.io' target='_blank'>
                                                <img src='../../Img/Help.png' style="vertical-align: middle" />帮助</a></div>
                                </th>
                            </tr>
                            <tr id="YC_1">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">该方式是系统默认的方式，也是常用的方式，系统可以智能的寻找您需要的接受人，点击右上角设置岗位。</li>
                                        <li style="font-size: 12px;">在寻找接受人的时候系统会考虑当前人的部门因素，A发到B，在B节点上绑定N个岗位，系统首先判断当前操作员部门内是否具有该岗位集合之一，如果有就投递给他，没有就把自己的部门提高一个级别，在寻找，依次类推，一直查找到最高级，如果没有就抛出异常。</li>
                                        <li style="font-size: 12px;">比如：一个省机关下面有n个县，n个市，n个县. n个所. 一个所员受理人员的业务，只能让自己的所长审批，所长的业务只能投递到本区县的相关业务部分审批，而非其它区县业务部分审批。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  02.按节点绑定的部门计算 -->
                            <tr id="RB1">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_1){%>checked="checked"<%}%> ID="RB_1" Text="按节点绑定的部门计算" name="xxx" runat="server" />
                                    </div>
                                    <% if(Glo.getIsUnit()){%>
                                    	<div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/Port/DeptTree.jsp?NodeID=<%=nd.getNodeID() %>&r=<%=k %>')">
                                            设置/更改部门(<%=ndepts.size() %>)</a></div>
                                    <%} else {%>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeDepts&NodeID=<%=nd.getNodeID() %>&r=<%=k %>')">
                                            设置/更改部门(<%=ndepts.size() %>)</a></div>
                                     <%}%>
                                </th>
                            </tr>
                            <tr id="YC_02">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">该部门下所有的人员都可以处理该节点的工作。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  04.按节点绑定的人员计算 -->
                            <tr id="RB3">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_3){%>checked="checked"<%}%> ID="RB_3" Text="按节点绑定的人员计算" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotTreeDeptEmpModel.htm?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeEmps&NodeID=<%=nd.getNodeID() %>&r=<%=k %>&ShowWay=FK_Dept')">
                                            设置/更改处理人(<%=nEmps.size() %>)</a></div>
                                </th>
                            </tr>
                            <tr id="YC_03">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">绑定的所有的人员，都可以处理该节点的工作。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  10.按绑定的岗位与部门交集计算 -->
                            <tr id="RB9">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_9){%>checked="checked"<%}%> ID="RB_9" Text="按绑定的岗位与部门交集计算" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotStationModel.htm?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeStations&NodeID=<%=nd.getNodeID() %>&r=<%=k %>&num=None')">
                                            设置与更改岗位(<%=nss.size()%>)</a> |<a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeDepts&NodeID=<%=nd.getNodeID() %>&r=<%=k %>')">
                                                设置与更改部门(<%=ndepts.size()%>)</a>
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_04">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">ccBPM会取既具备此岗位集合的又具备此部门集合的人员，做为本节点的接受人员。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  11.按绑定的岗位计算并且以绑定的部门集合为纬度 -->
                            <tr id="RB10">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_10){%>checked="checked"<%}%> ID="RB_10" Text="按绑定的岗位计算并且以绑定的部门集合为纬度" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotStationModel.htm?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeStations&NodeID=<%=nd.getNodeID() %>&r=<%=k %>&num=None')">
                                            设置与更改岗位(<%=nss.size() %>)</a> |<a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeDepts&NodeID=<%=nd.getNodeID() %>&r=<%=k %>')">
                                                设置与更改部门(<%=ndepts.size() %>)</a>
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_05">
                                <td class="BigDoc">
                                    <ul>
                                        <li>ccBPM会取绑定部门集合下绑定岗位的人员 </li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  12.按指定节点的人员岗位计算 -->
                            <tr id="RB11">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_11){%>checked="checked"<%}%> ID="RB_11" Text="按指定节点的人员岗位计算" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请选择要指定的节点.
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_06">
                                <td class="BigDoc">
                                    <!-- <asp:CheckBoxList ID="CBL_11" runat="server" RepeatDirection="Horizontal" RepeatColumns="5">
                                    </asp:CheckBoxList> -->
                                    <%--
                                    <% for(Map.Entry<String, String> entry:CBL_8.entrySet()){%>
                                   			<input type="checkbox" <%if(strDLP.contains(entry.getKey().substring(0,3))){ %>checked="checked"<%} %> name="CBL_11" value="<%=entry.getKey() %>"><%=entry.getValue()%>
									<%}%>
									--%>
									<%	// 默认选中
										for(Map.Entry<String, String> entry:CBL_8.entrySet()){
                                    		String key = entry.getKey();
                                    		if (key != null) {
                                    			String[] tmp = key.split(" ");
                                    			if (tmp.length > 0) {
                                    %>
                                    				<input type="checkbox" <%if(strDLP.contains(tmp[0])){ %>checked="checked"<%} %> name="CBL_11" value="<%=entry.getKey() %>"><%=entry.getValue()%>
                                    <%			}
                                    		}
										}
									%>
                                    <ul>
                                        <li>ccBPM在处理接受人时，会按指定节点上的人员身份计算 </li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  15.仅按绑定的岗位计算 -->
                            <tr id="RB14">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_14){%>checked="checked"<%}%> ID="RB_14" Text="仅按绑定的岗位计算" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotStationModel.htm?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeStations&NodeID=<%=nd.getNodeID() %>&r=<%=k %>&num=None')">
                                            设置/更改岗位(<%=nss.size() %>)</a>
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_07">
                                <td class="BigDoc">
                                    <ul>
                                        <li>按照节点上绑定的岗位来计算接受人，这里去掉了部门维度的过滤。 </li>
                                        <li>您设置岗位范围的集合下面有多少人，该节点就有多少人处理。 </li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  08.与开始节点处理人相同 -->
                            <tr id="RB7">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_7){%>checked="checked"<%}%> ID="RB_7" Text="与开始节点处理人相同" name="xxx" runat="server" />
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_08">
                                <td class="BigDoc">
                                    <ul>
                                           <li style="font-size: 12px;">节点A是甲处理，发送到节点B,也是需要甲处理。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  07.与上一节点处理人员相同 -->
                            <tr id="RB6">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_6){%>checked="checked"<%}%> ID="RB_6" Text="与上一节点处理人员相同" name="xxx" runat="server" />
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_09">
                                <td class="BigDoc">
                                    <ul>
                                    	<li style="font-size: 12px;">当前节点的处理人与开始节点一致，发起人是zhangsan,现在节点的处理人也是他。</li>
                                        <li style="font-size: 12px;">也就是说自己发给自己。</li>
                                     </ul>
                                </td>
                            </tr>
                            <!-- ===================================  09.与指定节点处理人相同 -->
                            <tr id="RB8">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_8){%>checked="checked"<%}%> ID="RB_8" Text="与指定节点处理人相同" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请选择</div>
                                </th>
                            </tr>
                            <tr id="YC_10">
                                <td class="BigDoc">
                                    <!-- <asp:CheckBoxList ID="CBL_8" runat="server" RepeatDirection="Horizontal" RepeatColumns="5">
                                    </asp:CheckBoxList> -->
                                    <% for(Map.Entry<String, String> entry:CBL_8.entrySet()){%>
                                    			<input type="checkbox" <%if(CBL_8List.contains(entry.getKey().split(" ")[0])){ %>checked="checked"<%} %> name="CBL_8" value="<%=entry.getKey() %>"><%=entry.getValue()%>
									<%}%>
                                </td>
                            </tr>
                            <!-- ===================================  03.按设置的SQL获取接受人计算 -->
                            <tr id="RB2">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_2){%>checked="checked"<%}%> ID="RB_2" Text="按设置的SQL获取接受人计算" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请在文本框里里输入SQL.</div>
                                </th>
                            </tr>
                            <tr id="YC_11">
                                <td class="BigDoc">
                                    <textArea text="<%=TB_2 %>" ID="TB_2" runat="server" style="Width:98%" Rows="3" Height="63px" TextMode="MultiLine"><%=TB_2 %></textArea>
                                    <ul>
                                        <li style="font-size: 12px;">该SQL是需要返回No,Name两个列，分别是人员编号,人员名称。</li>
<!--                                         <li style="font-size: 12px;">该配置也适合于</li> -->
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  13.按SQL确定子线程接受人与数据源 -->
                            <tr id="RB12">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_12){%>checked="checked"<%}%> ID="RB_12" Text="按SQL确定子线程接受人与数据源" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请在文本框里里输入SQL.
                                    </div>
                                </th>
                                <%
                                    Boolean isEnable = false;
                                    if (nd.getHisRunModel() == RunModel.SubThread)
                                        isEnable = true;

                                    RB_12 = isEnable;
                                    Boolean TB_12Bn = isEnable;

                                    if (nd.getHisDeliveryWay() == DeliveryWay.BySQLAsSubThreadEmpsAndData)
                                        RB_12 = true;
                                %>
                            </tr>
                            <tr id="YC_12">
                                <td class="BigDoc">
                                    <textArea text="<%=TB_12 %>" ID="TB_12" runat="server" style="Width:98%" Rows="3" Height="63px" TextMode="MultiLine"></textArea>
                                    <ul>
                                        <li style="font-size: 12px;">此方法与分合流相关，只有当前节点是子线程才有意义，当前节点模式为：<%=nd.getHisRunModel()%>。
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  05.由上一节点发送人通过“人员选择器”选择接受人 -->
                            <tr id="RB4">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_4){%>checked="checked"<%}%> ID="RB_4" Text="由上一节点发送人通过“人员选择器”选择接受人" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                         <a href="javascript:WinOpen('<%=basePath %>WF/Comm/En.htm?EnName=BP.WF.Template.Selector&PK=<%=nd.getNodeID() %>')">
                                            设置处理人可以选择的范围</a></div>
                                </th>
                            </tr>
                            <tr id="YC_13">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">绑定的所有的人员，都可以处理该节点的工作。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  06.按上一节点表单指定的字段值作为本步骤的接受人 -->
                            <tr id="RB5">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_5){%>checked="checked"<%}%> ID="RB_5" Text="按上一节点表单指定的字段值作为本步骤的接受人" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请选择一个字段</div>
                                </th>
                            </tr>
                            <tr id="YC_14">
                                <td class="BigDoc">
                                    请选择一个节点字段：
                                    <select id="DDL_5">
                                    	<% for(Map.Entry<String, String> entry:DDL_5.entrySet()){%>
                                    		<option value='<%=entry.getKey() %>'><%=entry.getKey() %> <%=entry.getValue()%></option>
										<%}%>
                                    </select>
                                </td>
                            </tr>
                            <!-- ===================================  14.由上一节点的明细表来决定子线程的接受人 -->
                            <tr id="RB13">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_13){%>checked="checked"<%}%> ID="RB_13" Text="由上一节点的明细表来决定子线程的接受人" name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请在文本框里里输入SQL.
                                    </div>
                                </th>
                                <% 
                                    Boolean isenabled = false;
                                    if (nd.getHisRunModel() == RunModel.SubThread)
                                        isenabled = true;

                                    RB_13 = isenabled;
                                    Boolean TB_13Bn = isenabled;

                                    if (nd.getHisDeliveryWay() == DeliveryWay.ByDtlAsSubThreadEmps)
                                        RB_13 = true;
                                %>
                            </tr>
                            <tr id="YC_15">
                                <td class="BigDoc">
                                    <textArea text="<%=TB_13 %>" ID="TB_13" runat="server" style="Width:98%" Rows="3" Height="63px" TextMode="MultiLine"><%=nd.getDeliveryParas() %></textArea>
                                    <ul>
                                        <li style="font-size: 12px;">此方法与分合流相关，只有当前节点是子线程才有意义。 </li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  16.由FEE来决定. -->
                            <tr id="RB15">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_15){%>checked="checked"<%}%> ID="RB_15" Text="由FEE来决定." name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        请编写事件代码
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_16">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">用流程事件，通过调用设置接受的接口，来设置当前节点的接收人，实现的把接受人信息写入接收人列表里。 </li>
                                    </ul>
                                </td>
                            </tr>
                                                        <!-- ===================================  17.按绑定部门计算，该部门一人处理标识该工作结束(子线程) -->
                            <tr id="RB16">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_16){%>checked="checked"<%}%> ID="RB_16" Text="按绑定部门计算，该部门一人处理标识该工作结束(子线程)." name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                       
                                         <a href="javascript:WinOpen('<%=basePath %>WF/Comm/RefFunc/Dot2DotTreeDeptModel.htm?EnsName=BP.WF.Template.Selectors&EnName=BP.WF.Template.Selector&AttrKey=BP.WF.Template.NodeDepts&NodeID=<%=nd.getNodeID() %>&r=<%=k %>')">
                                                设置与更改部门(<%=ndepts.size() %>)</a>
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_17">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">仅适用于子线程节点，按照部门分组子线程上的处理人员，每个部门一个任务，如果该部门的其中有一个人处理了，就标识该部门的工作完成，可以流转到下一步 </li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- ===================================  18.按ccBPM的BPM模式处理. -->
                            <tr id="RB100">
                                <th>
                                    <div style="float: left">
                                        <input type="radio" <% if(RB_17){%>checked="checked"<%}%> ID="RB_17" Text="按ccBPM的BPM模式处理." name="xxx" runat="server" />
                                    </div>
                                    <div style="float: right">
                                        <a href="javascript:WinOpen('<%=basePath %>WF/Admin/FindWorker/List.jsp?FK_Node=<%=nd.getNodeID()%>&FK_Flow=<%=nd.getFK_Flow()%>')">
                                            BPM模式接收人设置规则</a>
                                    </div>
                                </th>
                            </tr>
                            <tr id="YC_18">
                                <td class="BigDoc">
                                    <ul>
                                        <li style="font-size: 12px;">使用与ccflow集成到别的系统之中</li>
                                    </ul>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div title="辅助属性" style="padding: 10px">
                        <table border="0" width="100%">
                            <!-- =================================== 是否可以分配工作  -->
                            <tr>
                                <th>
                                    <div style="float: left">
                                        <input type="checkbox" <%if(CB_IsSSS){ %>checked="checked"<%} %> ID="CB_IsSSS"  Text="" runat="server" />是否可以分配工作？
                                    </div>
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <ul>
                                        <li>该属性是对于该节点上有多个人处理有效。 </li>
                                        <li>比如:A,发送到B,B节点上有张三，李四，王五可以处理，您可以指定1个或者多个人处理B节点上的工作。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- =================================== 是否启用自动记忆功能  -->
                            <tr>
                                <th>
                                    <div style="float: left">
                                        <input type="checkbox" <%if(CB_IsRememme){ %>checked="checked"<%} %> ID="CB_IsRememme" Text="" runat="server" />是否启用自动记忆功能？
                                    </div>
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <ul>
                                        <li>该属性是对于该节点上有多个人处理有效。 </li>
                                        <li>比如:A,发送到B,B节点上有张三，李四，王五可以处理，这次你把工作分配给李四，如果设置了记忆，那么ccbpm就在下次发送的时候，自动投递给李四，让然您也可以重新分配。</li>
                                    </ul>
                                </td>
                            </tr>
                            <!-- =================================== 本节点接收人不允许包含上一步发送人  -->
                            <tr>
                                <th>
                                    <div style="float: left">
                                        <input type="checkbox" <%if(CB_IsExpSender){ %>checked="checked"<%} %> ID="CB_IsExpSender" Text="" runat="server" />本节点接收人不允许包含上一步发送人？
                                    </div>
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <ul>
                                        <li>该属性是对于该节点上有多个人处理有效。 </li>
                                        <li>比如:A发送到B,B节点上有张三，李四，王五可以处理，如果是李四发送的，该设置是否需要把李四排除掉。</li>
                                    </ul>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div data-options="region:'south'" style="height: 50px;">
                <input type="button" ID="Btn_Save" runat="server" value="保存" OnClick="onSave(0)" />
                <input type="button" ID="Btn_SaveAndClose" runat="server" value="保存并关闭" OnClick="onSave(1)" />
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
          $("#DDL_5 option[value='<%=DDL_5Value%>']").attr("selected", "selected");
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
            function NoSubmit(ev) {
                if (window.event.srcElement.tagName == "TEXTAREA")
                    return true;

                if (ev.keyCode == 13) {
                    window.event.keyCode = 9;
                    ev.keyCode = 9;
                    return true;
                }
                return true;
            }
            
          //ajax 提交
        	function onSave(x){
        		var keys = "";
        		//var CBL_8=$("input[name=CBL_8]:checked").val();
        		//var CBL_11=$("input[name=CBL_11]:checked").val();
        		 var CBL_8 = "";
                 $('input[name="CBL_8"]:checked').each(function(){     
                	 CBL_8+=($(this).val()+",");   
                 }); 
        		 
                 var CBL_11 ="";  
                 $('input[name="CBL_11"]:checked').each(function(){     
                	 CBL_11+=($(this).val()+","); 
                 }); 
        		
        		var xxx=$("input[name=xxx]:checked").attr("id");
        		var TB_2=$("#TB_2").val();
        		var DDL_5=$("#DDL_5").val();
        		var TB_12=$("#TB_12").val();
        		var TB_13=$("#TB_13").val();
        		var CB_IsSSS=$('#CB_IsSSS').is(':checked');
        		var CB_IsRememme=$('#CB_IsRememme').is(':checked');
        		var CB_IsExpSender=$('#CB_IsExpSender').is(':checked');
        		$.ajax({
        			url:'<%=basePath%>WF/NodeAccepterRole/Save.do',
        			type:'post', //数据发送方式
        			dataType:'text', //接受数据格式
        			data:{CBL_8:CBL_8,CBL_11:CBL_11,xxx:xxx,TB_2:TB_2,DDL_5:DDL_5,TB_12:TB_12,
        				TB_13:TB_13,CB_IsSSS:CB_IsSSS,CB_IsRememme:CB_IsRememme,
        				CB_IsExpSender:CB_IsExpSender,NodeID:'<%=nodeID%>'},
        			async: false ,
        			error: function(data){
       					alert(data);
        			},
        			success: function(data){
        				//var json = eval("("+data+")");
        				//if(json.success){
       					alert("保存成功");
       					doSelectTree();	// 更新选中样式
        				//}else{
        				//}
        			}
        		});
        		if(x>0){
//	        		window.close();
					var currentSelection = (parent || {}).currentSelection;
					if (typeof currentSelection === "function") {
						var currentTab = currentSelection.call();
						if (currentTab && typeof currentTab.title === "string") {
							var closeTab = (parent || {}).closeTab;
							if (typeof closeTab === "function") {
								closeTab.call("", currentTab.title);
							}
						}
					}
        		}
        	}
        </script>