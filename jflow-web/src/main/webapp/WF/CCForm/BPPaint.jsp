<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%@page import="BP.Sys.FrmEle"%>
<%
String PKVal = request.getParameter("PKVal"); 
String H = request.getParameter("H");
String W = request.getParameter("W");
String MyPK = request.getParameter("MyPK");
FrmEle ele = new FrmEle(MyPK);
// String path = ele.getHandSigantureSavePath();
String fk_mapdata = ele.getFK_MapData();
String initParams = ("mypk=" + MyPK + ",pkval=" + PKVal + ",H=" + ele.getHandSiganture_WinOpenH() + ",W=" + ele.getHandSiganture_WinOpenW());
System.out.println(initParams+"==============================");
%>
<script type="text/javascript">
	function saveok() {
		question = confirm("已经成功保存签名，是否关闭本窗口?")
		if (question != "0") {
			//window.open('', '_top'); window.top.close();
			window.close();
		}
	}
	function GoBack(url, mypk, workid, dd) {
		window.returnValue = url;
		this.close();
	}
</script>
<style type="text/css">
html,body {
	height: 100%;
	overflow: auto;
}

body {
	padding: 0;
	margin: 0;
}

#silverlightControlHost {
	height: 100%;
	text-align: center;
}
</style>
<style type="text/css">
html,body {
	height: 100%;
	overflow: auto;
}

body {
	padding: 0;
	margin: 0;
}

#silverlightControlHost {
	height: 100%;
	text-align: center;
}
</style>
<script type="text/javascript">
	function onSilverlightError(sender, args) {
		var appSource = "";
		if (sender != null && sender != 0) {
			appSource = sender.getHost().Source;
		}

		var errorType = args.ErrorType;
		var iErrorCode = args.ErrorCode;

		if (errorType == "ImageError" || errorType == "MediaError") {
			return;
		}

		var errMsg = "Unhandled Error in Silverlight Application " + appSource
				+ "\n";

		errMsg += "Code: " + iErrorCode + "    \n";
		errMsg += "Category: " + errorType + "       \n";
		errMsg += "Message: " + args.ErrorMessage + "     \n";

		if (errorType == "ParserError") {
			errMsg += "File: " + args.xamlFile + "     \n";
			errMsg += "Line: " + args.lineNumber + "     \n";
			errMsg += "Position: " + args.charPosition + "     \n";
		} else if (errorType == "RuntimeError") {
			if (args.lineNumber != 0) {
				errMsg += "Line: " + args.lineNumber + "     \n";
				errMsg += "Position: " + args.charPosition + "     \n";
			}
			errMsg += "MethodName: " + args.methodName + "     \n";
		}
		throw new Error(errMsg);
	}
</script>
<body>
	<div id="silverlightControlHost">
        <object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%">
		  <param name="source" value="/WF/Admin/ClientBin/BPPaint.xap"/>
		  <param name="onError" value="onSilverlightError" />
		  <param name="background" value="white" />
		  <param name="minRuntimeVersion" value="4.0.50826.0" />
		 <%--  <param name="uiculture" value="<%= System.Threading.Thread.CurrentThread.CurrentUICulture %>" />
          <param name="culture" value="<%= System.Threading.Thread.CurrentThread.CurrentCulture %>" />
		   --%><param name="autoUpgrade" value="true" />
          <param name="initParams" value="<%= initParams %>" />
		  <a href="http://go.microsoft.com/fwlink/?LinkID=149156&v=4.0.50826.0" style="text-decoration:none">
 			  <img src="http://go.microsoft.com/fwlink/?LinkId=161376" alt="Get Microsoft Silverlight" style="border-style:none"/>
		  </a>
	    </object>
        <iframe id="_sl_historyFrame" style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
      </div>
</body>
</html>