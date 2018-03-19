<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file  = "/WF/head/head1.jsp" %>

<input type="text" ID="Content1" ContentPlaceHolderID="head" style="visibility:hidden"/>
<input type="text" ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" style="visibility:hidden"/>

<%
    int workid = Integer.parseInt(request.getParameter("WorkID"));

    BP.WF.GenerWorkFlow gwf = new BP.WF.GenerWorkFlow(workid);
%>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>

<script type="text/javascript">
    $(function(){	
    	$("#Button2").click(function(){
    		//if(confirm("您确定要关闭本页吗？")){
	    		window.opener=null;
	    		window.open('','_self');
	    		window.close();
    		//}
    	});
    })

</script>
<fieldset>
    <legend>重要性</legend>
    <input type="radio" id="RadioButton1" value="高" name="a" />高
    <br />
    <input type="radio" id="RadioButton2" value="中" name="a" />中
    <br />

    <input type="radio" id="RadioButton3" value="低" name="a" />低
    <br />

    <input type="button" id="Button1" value="保存" />
    <input type="button" id="Button2" value="取消" />

</fieldset>

