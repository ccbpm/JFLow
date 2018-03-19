<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<link rel='stylesheet' type='text/css' href='<%=basePath%>WF/Comm/track/trackStyle.css'/>

<%
TruakModel tm = new TruakModel(request, response);
tm.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
    <script language="javascript" type="text/javascript">
        $(document).ready(function () {
        });

        function WinOpen(url, winName) {
            var newWindow = window.open(url, winName, 'height=800,width=1030,top=' + (window.screen.availHeight - 800) / 2 + ',left=' + (window.screen.availWidth - 1030) / 2 + ',scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
            newWindow.focus();
            return;
        }
        function ReinitIframe(frmID, tdID) {
            try {

                var iframe = document.getElementById(frmID);
                var tdF = document.getElementById(tdID);
                iframe.height = iframe.contentWindow.document.body.scrollHeight;
                iframe.width = iframe.contentWindow.document.body.scrollWidth;
                if (tdF.width < iframe.width) {
                    tdF.width = iframe.width;
                } else {
                    iframe.width = tdF.width;
                }

                tdF.height = iframe.height;
                return;

            } catch (ex) {

                return;
            }
            return;
        }
    </script>
    
</head>
<body>
<%=tm.Pub1.toString()%>
<%=tm.UCEn1.Pub.toString()%>
<div style="clear:both;"></div>
</body>
</html>