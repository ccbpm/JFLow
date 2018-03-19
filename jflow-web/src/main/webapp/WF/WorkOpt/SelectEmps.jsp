<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	 String ctrlVal = request.getParameter("CtrlVal")==null?"":request.getParameter("CtrlVal");
	 
	 int Cols = 5;
	 String DBOfGroups = "SELECT No,Name FROM Port_Dept";
	 String DBOfObjs = "SELECT No,Name,FK_Dept FROM Port_Emp";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base target="_self" />
<title>人员选择</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/Table.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/Table0.css" />

<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript">
function NoSubmit(ev) {
    //if (window.event.srcElement.tagName == "TEXTAREA")return true;
    if (ev.keyCode == 13) {
        window.event.keyCode = 9;
        ev.keyCode = 9;
        return true;
    }
    return true;
}
//确定所选值
function SubmitValue() {
	var keys = "";
	var txtKeyWords = '<%=DBOfObjs%>';
	//操作
    $.ajax({
        url:"LoadEmps.do",
        type:'post',
        dataType:'json',
        data: { "KeyWord": txtKeyWords },
        async: false,
        success: function (data) {
            if (data != "") {
                var emps = eval(data);

                for (var emp in emps) {
                    var value = emps[emp].no;
                    //var text = emps[emp].name;
                    var obj = $("#CB_"+value);
                    if(obj.length <= 0)continue;
                    if(!obj.attr("checked"))continue;
                    if(keys.length >0){
                    	keys += "," + value;
                    }else{
                    	keys += value;
                    }
                }
            }
          
        }
    });
    if (window.opener != undefined) {
        window.top.returnValue = keys;
    } else {
        window.returnValue = keys;
    }
    window.close();
}
//关闭
function CloseWin() {
    window.close();
}
</script>
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
	<form method="post" action="" id="form1">
		<div id="mainPanel" region="center" border="true" border="false" class="mainPanel">
			<%
				ctrlVal = "," + ctrlVal + ",";
				ctrlVal = ctrlVal.replace(";", ",");
	           	ctrlVal = ctrlVal.replace(" ", "");
	            ctrlVal = ctrlVal.replace(" ", "");
	            ctrlVal = ctrlVal.replace(" ", "");
	            List<String> empNos = new ArrayList<String>();
	            DataTable dtGroup = new DataTable();
	            if(DBOfGroups.length()>5){
	            	dtGroup = DBAccess.RunSQLReturnTable(DBOfGroups);
	            }else{
	            	dtGroup.Columns.Add("No", String.class);
	            	dtGroup.Columns.Add("Name", String.class);
	            	DataRow dr = dtGroup.NewRow();
	            	dr.setValue("No", "01");
	            	dr.setValue("Name", "全部选择");
	            	dtGroup.Rows.add(dr);
	            }
			
	            DataTable dtObj = DBAccess.RunSQLReturnTable(DBOfObjs);
	            if(dtObj.Columns.size() == 2){
	            	dtObj.Columns.Add("Group", String.class);
	            	for(DataRow dr : dtObj.Rows){
	            		dr.setValue("Group", "01");
	            	}
	            }
			%>
			<Table width=100% border=0 >
				<Caption >选择人员</Caption>
				<%
					for(DataRow drGroup : dtGroup.Rows){
						String ctlIDs = "";
						String groupNo = drGroup.getValue(0).toString();
						String text = drGroup.getValue(1).toString();
				%>
				<TR>
					<TH align="left">
						<input id="CBs_<%=groupNo %>" type="checkbox" name="CBs_<%=groupNo %>"/><label for="CBs_<%=groupNo %>"><%=text %></label>
					</TH>
				</TR>
				<TR>
				   <TD nowrap="nowrap">
				   		<Table class='Table' cellpadding='2' cellspacing='2'>
				   			  <%
				   			  		int colIdx = -1;
									for(DataRow drObj : dtObj.Rows){
										String no = drObj.getValue(0).toString();
						            	String name = drObj.getValue(1).toString();
						                String group = drObj.getValue(2).toString();
						                if(!group.trim().equals(groupNo.trim()))continue;
						                
						                colIdx++;
					                    if (colIdx == 0){
				   			  %>
				   			  <TR>
				   			  			<%} 
					                    	empNos.add(no);
					                    	ctlIDs += "CB_" + no + ",";
					                    	if(ctrlVal.contains(","+no+",")){
				   			  			%>
					   			  			<TD>
					   			  				<input id="CB_<%=no %>" type="checkbox" name="CB_<%=no %>" checked="checked" onclick="javascript:isChange=true;" />
					   			  				<label for="CB_<%=no %>"><font color=green><%=name %></font></label>
					   			  			</TD>
				   			  			<%}else{ %>
				   			  				<TD>
					   			  				<input id="CB_<%=no %>" type="checkbox" name="CB_<%=no %>" onclick="javascript:isChange=true;" />
					   			  				<label for="CB_<%=no %>"><%=name %></label>
					   			  			</TD>
				   			  			<%} 
				   			  				if(Cols-1 == colIdx){
				   			  					colIdx = -1;
				   			  			%>
				   			  	</TR>
				   			  			<%} %>
				   			  <%} 
									 if (colIdx != -1){
							   %>	 
								</TR> 
				   			  <%}%>
				   		</Table>
				   </TD>
				</TR>
				 <script type="text/javascript">
				 	$("#CBs_<%=groupNo%>").click(function (e) {
				 		var obj = e.target;  
				 		SetSelected(obj,'<%=ctlIDs%>');
				 	});	  
				 </script>
				<%} %>
			</Table>
			<input type="button" name="Btn_OK" value="确定" id="Btn_OK" onclick="SubmitValue()" class="Btn" />
			<input type="button" name="Btn_Cancel" value="取消" id="Btn_Cancel" onclick="CloseWin()" class="Btn" />
		</div>
	</form>
</body>
<script type="text/javascript">
function SetSelected(cb, ids) {
    var arrmp = ids.split(',');
    var isCheck = false;
    if (cb.checked)
        isCheck = true;
    else
        isCheck = false;
    for (var i = 0; i < arrmp.length; i++) {
    	$("#"+arrmp[i]).attr("checked",isCheck);
    }
}
</script>
</html>