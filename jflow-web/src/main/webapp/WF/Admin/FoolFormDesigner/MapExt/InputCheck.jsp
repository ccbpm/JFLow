<%@page import="BP.WF.Glo"%>
<%@page import="cn.jflow.model.wf.admin.FoolFormDesigner.MaoExt.InputCheckModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%
InputCheckModel inputCheckModel = new InputCheckModel(request, response);
inputCheckModel.Page_Load();
%>
<html>
<head>
</head>
<body>
	<%-- <link href="<%=basePath %>WF/Comm/Style/CommStyle.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath %>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath %>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="<%=basePath %>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="<%=basePath %>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script> --%>
    <script type="text/javascript">
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

        function DoDel(mypk, fk_mapdata, extType) {
            if (window.confirm('您确定要删除吗？') == false)
                return;
            window.location.href = 'InputCheck.jsp?DoType=Del&FK_MapData=' + fk_mapdata + '&ExtType=' + extType + '&MyPK=' + mypk;
        }
        
        function rb_CheckedChanged(){
        	var id = $("input[name=s]:checked").attr("id");
        	var urlx = window.location.href;
        	if(urlx.indexOf("RB_0C")>0){
        		urlx=urlx.replace("RB_0C=RB_0","RB_0C="+id);
        		urlx=urlx.replace("RB_0C=RB_1","RB_0C="+id);
        	}else{
        		urlx = window.location.href + "&RB_0C="+id;
        	}
        	window.location.href = urlx;
        }
        
        function btn_SaveInputCheck_Click(){
        	var param = window.location.href.split("?")[1];
        	param = param + "&LB1=" + $("#LB1").val();
        	var url = "<%=basePath%>wf/InputCheck/btn_save.do?"+param;
        	$.ajax({
    			url:url,
    			type:'post', //数据发送方式
    			async: false ,
    			error: function(data){},
    			success: function(data){
    				if(data=='success'||data=="success"){
    					//$("input[name*='CB_']:checkbox").removeAttr("checked");
    					alert("操作成功！");
    				}else{
    					alert("操作失败！");
    				}
    			}
    		});
        }
    </script>
    <%=inputCheckModel.Pub1.toString() %>
</body>
</html>