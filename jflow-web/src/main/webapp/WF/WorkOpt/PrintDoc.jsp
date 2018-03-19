<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="cn.jflow.common.model.PrintDocModel"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	PrintDocModel printDoc = new PrintDocModel(request, response);
	printDoc.loadData();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base target="_self" />
<title>打印</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link rel="stylesheet" type="text/css" href="<%=basePath %>WF/Comm/Style/Table.css"  />

<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/CreateControl.js"></script>
<script type="text/javascript">
    function btnPreview_onclick(name) {
        try { 
            var ReportViewer = document.getElementById("ReportViewer");

            var Report = ReportViewer.Report;
            Report.LoadFromURL("<%=basePath%>DataUser/CyclostyleFile/" + name+".grf" );
            // Report.LoadFromURL("../../DataUser/grf/4a.grf");
           
            var json_data = { "WorkID": "<%=printDoc.getWorkID() %>", "FK_Flow": "<%=printDoc.getFK_Flow() %>", "FK_Node": "<%=printDoc.getFK_Node() %>", "DoType": "0" };
            $.ajax({
                type: "get",
                url: "WF/WorkOpt/GridData.do",
                data: json_data,
                async:false,
                beforeSend: function (XMLHttpRequest, fk_mapExt) {
                    //ShowLoading();
                },
                success: function (data, textStatus) {
                    var jsonData = eval("(" + data + ")");
                    if (jsonData.length > 0) {
                        for (var idx in jsonData) {
                            var childReport = ReportViewer.Report.ControlByName(jsonData[idx].Name);
                            if (childReport == undefined || childReport == null) {
                                alert("加载子报表为空");
                            } else {
                                childReport.AsSubReport.Report.LoadDataFromURL("/WF/WorkOpt/GridData.do?WorkID=<%=printDoc.getWorkID() %>&FK_Flow=<%=printDoc.getFK_Flow() %>&FK_Node=<%=printDoc.getFK_Node() %>&DoType=1&Name="+jsonData[idx].Name);
                            }
                        }
                    }

                    json_data = { "WorkID": "<%=printDoc.getWorkID() %>", "FK_Flow": "<%=printDoc.getFK_Flow() %>", "FK_Node": "<%=printDoc.getFK_Node() %>", "DoType": "5" };

                    $.ajax({
                        type: "get",
                        url: "WF/WorkOpt/GridData.do",
                        data: json_data,
                        async: false,
                        beforeSend: function (XMLHttpRequest, fk_mapExt) {


                            //ShowLoading();
                        },
                        success: function (PhotoData, textStatus) {

                            var dataPhoto = eval("(" + PhotoData + ")");

                            for (var index in dataPhoto) {

                                //alert(dataPhoto[index].Name + "--" + dataPhoto[index].Value);
                                try {
                                    Report.ControlByName(dataPhoto[index].Name).AsPictureBox.LoadFromFile(dataPhoto[index].Value);
                                } catch (e) {
                                    
                                }
                            }

                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            //    alert('HideLoading');
                            //HideLoading();
                        },
                        error: function () {
                            alert('加载图片出现异常！！.');
                            //请求出错处理
                        }
                    });

                    Report.LoadDataFromURL("WF/WorkOpt/GridData.do?WorkID=<%=printDoc.getWorkID() %>&FK_Flow=<%=printDoc.getFK_Flow() %>&FK_Node=<%=printDoc.getFK_Node() %>&DoType=1&Name=MainPage");
                    //ReportViewer.Start();
                    Report.PrintPreview(true);
  //                ReportViewer.Stop();
  //                Report.LoadDataFromURL("WorkOpt/GridData.do?WorkID=<%=printDoc.getWorkID() %>&FK_Flow=<%=printDoc.getFK_Flow() %>&FK_Node=<%=printDoc.getFK_Node() %>");
  //                ReportViewer.Start();
  //                Report.PrintPreview(true);
                },
                complete: function (XMLHttpRequest, textStatus) {
                    //    alert('HideLoading');
                    //HideLoading();
                },
                error: function () {
                    alert('error when load data.');
                    //请求出错处理
                }
            });

        } catch (ex) {
            alert("error when open the view");
        }
    }

</script>
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
	<form method="post" action="" id="form1">
		   <script type="text/javascript">
		   
          if ("<%=printDoc.getIsRuiLang() %>" == "true") {
        	  CreatePrintViewerEx("0", "0", "", "", false, "<param name=BorderStyle value=1>");
          }
      </script>
      <%=printDoc.Pub1.toString() %>
	</form>
</body>
</html>