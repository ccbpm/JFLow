<%@page import="java.text.DecimalFormat"%>
<%@page import="BP.En.QueryObject"%>
<%@page import="BP.En.FieldTypeS"%>
<%@page import="BP.En.Attr"%>
<%@page import="BP.En.EntitiesNoName"%>
<%@page import="BP.En.EntityNoName"%>
<%@page import="BP.En.Attrs"%>
<%@page import="BP.En.UIContralType"%>
<%@page import="BP.WF.Node"%>
<%@page import="BP.Sys.*"%>
<%@page import="java.util.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.Sys.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="cn.jflow.common.model.DtlOptModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String basePath;
	String ensName; 
	String refPKVal; 
	String fId; 
	String fkNode; 
	String isWap;
	String requestParas; 
	String FK_MapData;
	StringBuffer dtlCtrl = null;
	int isReadonly = 0;
	String rowCount;
	String AddRowNum;
	String path = request.getContextPath();
	basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";

	// 明细表
	ensName = request.getParameter("EnsName");
	// workId
	refPKVal = request.getParameter("RefPKVal");
	// 是否可读
	if(!StringHelper.isNullOrEmpty(request.getParameter("IsReadonly"))){
		isReadonly = Integer.parseInt(request.getParameter("IsReadonly"));
	}
	rowCount = request.getParameter("rowCount");
	AddRowNum = request.getParameter("AddRowNum");
	fId = request.getParameter("FID");
	fkNode = request.getParameter("FK_Node");
	isWap = request.getParameter("IsWap");
	
	if(null == isWap){
		isWap = "0";
	}
	// 拼接重定向参数
	requestParas = "&EnsName="+ensName+"&RefPKVal="+refPKVal+"&IsReadonly="+isReadonly+"&FID="+fId+"&FK_Node="+fkNode+"";
	DtlOptModel dm = new DtlOptModel(request,response);
	dm.init();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
     <meta http-equiv="Page-Enter" content="revealTrans(duration=0.5, transition=8)" />
    <title></title>
    <script src="<%=basePath%>WF/Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />  
    <script src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js" type="text/javascript"></script>
    <link href="<%=basePath%>WF/Style/FormThemes/Table0.css" rel="stylesheet" type="text/css" />
 	<script  src="<%=basePath%>WF/CCForm/MapExt.js"  type='text/javascript' ></script>
	<link href="<%=basePath%>WF/Comm/Style/Tabs.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript" src="../Comm/JScript.js"></script>
    <script language="JavaScript" src="../Comm/JS/Calendar/WdatePicker.js" defer="defer"></script>
    <script src="MapExt.js" type="text/javascript"></script>
 	<base target=_self />
 	
 <script language=javascript>
        function selectAll() {
            var arrObj = document.all;
            if (document.forms[0].checkedAll.checked) {
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

        function checkType() 
        {
            //得到上传文件的值   
            var fileName = document.getElementById("fup").value;

            //返回String对象中子字符串最后出现的位置.   
            var seat = fileName.lastIndexOf(".");

            //返回位于String对象中指定位置的子字符串并转换为小写.   
            var extension = fileName.substring(seat).toLowerCase();

            //判断允许上传的文件格式   
            //if(extension!=".jpg"&&extension!=".jpeg"&&extension!=".gif"&&extension!=".png"&&extension!=".bmp"){   
            //alert("不支持"+extension+"文件的上传!");   
            //return false;   
            //}else{   
            //return true;   
            //}   

            var allowed = [".jpg", ".gif", ".png", ".bmp", ".jpeg"];
            for (var i = 0; i < allowed.length; i++) {
                if (!(allowed[i] != extension)) {
                    return true;
                }
            }
            alert("不支持" + extension + "格式");
            return false;
        } 
        
        //DtlOptImport
        function DtlOptImport(dtlId){
        	var DDL_ImpWay = $("#DDL_ImpWay").val();
        	if(DDL_ImpWay=="all"||DDL_ImpWay<0){
        		alert("请选择导入方式.");
        		return;
        	}
        	var fup = $("#fup").get(0).files[0];
        	if(fup==undefined||"undefined"==fup||""==fup||fup==null){
        		alert("请选择选择要上传的EXCEL文件");
        		return;
        	}else{
        		var fileName = fup.name;
        		var len = fileName.split(".").length-1;
        		var ext = fileName.split(".")[len];
        		if(ext.indexOf("xls")<0){
        			alert("上传文件必须是EXCEL格式文件");
            		return;
        		}
        	}
        	// 上传EXCEL文件
        	//$.ajax(settings) /WF/CCForm DtlOptImport
        	var para = window.location.search;
        	var url = "<%=basePath%>WF/CCForm/DtlOptImport.do"+para;
        	$("#form2").attr("action", url);
			$("#form2").attr("enctype", "multipart/form-data");
			$("#form2").submit();
			alert("上传成功.");
			location.reload();
        	<%-- $.ajax({
    				type: "POST",
    				dataType : 'json',
    				url : '<%=basePath%>WF/CCForm/DtlOptImport.do',
    				data : $('#form2').serialize(),
    				success : function(data) {
    					if (null != data || "" != data) {
    						alert(data);
    						location.reload();
    					}
    				}
    		}); --%>
        }
    </script>
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
    <form id="form2"  onkeypress="NoSubmit(event);" action="" method="post">
    <div id="mainPanle" region="center" border="false"  >
        <%=dm.Pub1.toString() %>
        <%=dm.Pub2.toString() %>
    </div>
    </form>
</body>
</html>
