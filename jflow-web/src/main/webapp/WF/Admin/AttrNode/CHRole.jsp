 <%@include file="/WF/Admin/WinOpen.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
	function GetQueryString(name)
	{
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
		LoadData();
	function LoadData() {
		var FK_Node = GetQueryString("FK_Node");
		var params = {
			method : "getMyData",
			FK_Node:FK_Node
		};
		
            queryData(params,function(js,v){
            	 var pushData = eval('(' + js + ')');
	
            	 $("#TB_TSpanDay").val(pushData.tb_tspanday);
            	 $("#TB_TSpanHour").val(pushData.tb_tspanhour);
            	 $("#TB_WarningHour").val(pushData.tb_warninghour);
            	 $("#TB_WarningDay").val(pushData.tb_warningday);
            	 $("#TB_TCent").val(pushData.tb_tcent);
            	 $('#DDL_WAlertRole').combobox({    
            		    data:pushData.ddl_walertrole,    
            		    valueField:'IntKey',    
            		    textField:'Lab',
            		    editable:false,
            		    value:pushData.wrolesed
            		}); 
            	 $('#DDL_WAlertWay').combobox({    
         		    data:pushData.ddl_walertway,    
         		    valueField:'IntKey',    
         		    textField:'Lab',
        		    editable:false,
        		    value:pushData.wwaysed
         		}); 
            	 $('#DDL_TAlertWay').combobox({    
         		    data:pushData.ddl_talertway,    
         		    valueField:'IntKey',    
         		    textField:'Lab',
        		    editable:false,
        		    value:pushData.ttwaysed
         		}); 
            	 $('#DDL_TAlertRole').combobox({    
         		    data:pushData.ddl_talertrole,    
         		    valueField:'IntKey',    
         		    textField:'Lab',
        		    editable:false,
        		    value:pushData.ttrolesed
         		}); 
            	 $("input[name='xxx']").eq(pushData.hischway).attr("checked","checked");
            	 /* alert(pushData.cb_iseval+1); */ // Xzd modified(Cancellation) 2016-10-13  判断为该js代码目的为调试，无任何业务逻辑功能
            	 $("#CB_IsEval").attr("checked",pushData.cb_iseval=="false"?false:true);
            });
	}
	
	  //公共方法
    function queryData(param, callback, scope, method, showErrMsg) {
        if (!method) method = 'GET';
        $.ajax({
            type: method, //使用GET或POST方法访问后台
            dataType: "text", //返回json格式的数据
            contentType: "application/json; charset=utf-8",
            url:"<%=basePath%>WF/Admin/AttrNode/CHRoleModel.do", //要访问的后台地址
			data : param, //要发送的数据
			async : true,
			cache : false,
			complete : function() {
			}, //AJAX请求完成时隐藏loading提示
			error : function(XMLHttpRequest, errorThrown) {
				callback(XMLHttpRequest);
			},
			success : function(msg) {//msg为返回的数据，在这里做数据绑定
				var data = msg;
				callback(data, scope);
			}
		});
	}
	  function savekhgz(){
		  /* 
		  var hischway;
		  $("input[name='xxx']").each(function(){
			  if(this.attributes["checked"].value){
				  hischway=this.id;
			  }
		  })
		  */
		  var radio= $("input[name='xxx']:checked").val(); 
		  var CB_IsEval =document.getElementById("CB_IsEval").checked;
		  
		  var TB_TSpanDay= $("#TB_TSpanDay").val();
		  var TB_TSpanHour= $("#TB_TSpanHour").val();
		  var TB_WarningHour= $("#TB_WarningHour").val();
		  var TB_WarningDay= $("#TB_WarningDay").val();
		  var TB_TCent= $("#TB_TCent").val();
		  
		  //select 选中的值
		  var DDL_TAlertRole = $("#DDL_TAlertRole").combobox("getValue");
		  var DDL_TAlertWay = $("#DDL_TAlertWay").combobox("getValue");
		  var DDL_WAlertRole = $("#DDL_WAlertRole").combobox("getValue");
		  var DDL_WAlertWay = $("#DDL_WAlertWay").combobox("getValue");
		  
		  var FK_Node = GetQueryString("FK_Node");
		  var params = {
					"FK_Node":FK_Node,
					"TB_TSpanDay":TB_TSpanDay,
					"TB_TSpanHour":TB_TSpanHour,
					"TB_WarningHour":TB_WarningHour,
					"TB_WarningDay":TB_WarningDay,
					"TB_TCent":TB_TCent,
					"DDL_TAlertRole":DDL_TAlertRole,
					"DDL_TAlertWay":DDL_TAlertWay,
					"DDL_WAlertRole":DDL_WAlertRole,
					"DDL_WAlertWay":DDL_WAlertWay,
					"radio":radio,
					"CB_IsEval":CB_IsEval
				};
		  $.ajax({
	            type: 'GET', //使用POST方法访问后台
	            contentType: "application/json; charset=utf-8",
	            url:"<%=basePath%>WF/Admin/AttrNode/chRoleSave.do", //要访问的后台地址
	            data : params,
	            error : function() {
	            	alert("保存失败");
				},
				success : function(msg) {//msg为返回的数据，在这里做数据绑定
					alert(msg);
				}
		  
		  });
	  }
</script>
<body>
	<div id="rightFrame" data-options="region:'center',noheader:true">
		<div class="easyui-layout" data-options="fit:true">
			<table style="width: 100%;">
				<caption>考核规则</caption>
				<tr>
					<td>
						<fieldset>
							<legend>
								<input id="RB_None" type="radio" name="xxx" value="RB_None" /><label
									style="cursor: pointer;" for="RB_None">不考核</label>
							</legend>
							<ul>
								<li style="color: Gray">默认为不考核，当前节点不设置任何形式的考核。</li>
							</ul>
						</fieldset>
						<fieldset>
							<legend>
								<input id="RB_ByTime" type="radio" name="xxx" value="RB_ByTime" /> <label
									style="cursor: pointer;" for="RB_ByTime">按时效考核</label>
							</legend>
							<table style="width: 100%;">
								<tr>
									<td nowarp="true">限期完成时限：</td>
									<td>
									<input id="TB_TSpanDay" type="text" style="width:35px;" />
									天,</td>
									<td><input id="TB_TSpanHour" type="text" style="width:35px;" />	小时.</td>
									<td style="color: Gray">工作量考核是按照小时来计算(必须输入正整数,不能大于8)</td>
								</tr>
								<tr>
									<td>预警：</td>
									<td><input id="TB_WarningDay" type="text" style="width:35px;" />	 天,</td>
									<td><input id="TB_WarningHour" type="text" style="width:35px;" />小时.</td>
									<td style="color: Gray">
										提前xx天xx小时预警(必须输入正整数)，预警就是提醒该工作应该处理了的时间点。</td>
								</tr>
								<tr>
									<td>扣分</td>
									<td><input id="TB_TCent" type="text" style="width:35px;" />分</td>
									<td colspan="2" style="color: Gray">
										此分值可以作为时效考核的依据或者参考，每延期1小时的扣分。 <br />
										如果设置此分值，那末系统就会计算出来得分。该分值，可以转化为奖励或者罚款的金额。
									</td>
								</tr>
								<tr>
									<td>预警提醒规则</td>
									<td>
									<input id="DDL_WAlertRole" name="" >  </td>
									<td>提醒方式</td>
									<td><input id="DDL_WAlertWay" name="DDL_WAlertWay" >  </td>
								</tr>
								<tr>
									<td>逾期提醒规则</td>
									<td><input id="DDL_TAlertRole" name="DDL_TAlertRole" value=""/></td>
									<td>提醒方式</td>
									<td><input id="DDL_TAlertWay" name="DDL_TAlertWay" >  </td>
								</tr>
							</table>
						</fieldset>
						<fieldset>
							<legend>
								<input id="RB_ByWorkNum" type="radio" name="xxx" value="RB_ByWorkNum" /> <label
									style="cursor: pointer;" for="RB_ByWorkNum">按工作量考核</label>
							</legend>
							<ul style="color: Gray">
								<li>按照处理工作的多少进行考核。</li>
								<li>这样的节点，一般都是多人处理的节点。</li>
							</ul>
						</fieldset>
						<fieldset>
							<legend>是否是质量考核点？</legend>
							<ul style="color: Gray">
								<li>质量考核，是当前节点对上一步的工作进行一个工作好坏的一个考核。</li>
								<li>考核的方式是对上一个节点进行打分，该分值记录到WF_CHEval的表里，开发人员对WF_CHEval的数据根据用户的需求进行二次处理。</li>
							</ul>
							<input id="CB_IsEval" type="checkbox" value="CB_IsEval"/> <label
								style="cursor: pointer;" for="CB_IsEval">是否是质量考核点？</label>
						</fieldset> <input id="Btn_Save" type="button" value="保存"
						class="easyui-linkbutton" onclick="savekhgz();" style="cursor: pointer;" />
					</td>
					<td valign="top" style="white-space: 30%;">
						<fieldset>
							<legend>帮助</legend>
							<ul>
								<li>当设置的工作超时了以后，如何去处理的规则。</li>
							</ul>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>