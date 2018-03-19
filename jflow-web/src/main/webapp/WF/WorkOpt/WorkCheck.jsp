<%@page import="cn.jflow.common.model.WorkCheckM"%>
<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="BP.Tools.StringHelper"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int FK_Node=0;
    String Node=request.getParameter("FK_Node");
	if(Node==null || "".equals(Node)){
		FK_Node=Integer.valueOf(request.getParameter("FK_Flow")+"01");
	}else{
		FK_Node=Integer.valueOf(Node);
	}
	
	//System.out.println(FK_Node);
 	long FID=Long.valueOf(StringHelper.isEmpty(request.getParameter("FID"), "0"));
 	String FK_Flow=request.getParameter("FK_Flow");
 	String s = request.getParameter("Paras");
 	boolean IsCC = false;
    if (s!=null && s.contains("IsCC"))
    	IsCC= true;
    String DoType=request.getParameter("DoType");
    
 	long WorkID=0;
    String workid = request.getParameter("OID");
    if (workid == null)
        workid = request.getParameter("WorkID");
    WorkID = Long.valueOf(workid);
    
    String table1 = "";
    String table2 = "";
    
    WorkCheckM wcModel = new WorkCheckM(request, response);
    wcModel.Page_Load();
%>
<!DOCTYPE>
<html>
<head>
<style type="text/css">
        /* #Pub1_TB_Doc
        {
            border: 2px solid #D6DDE6;
        }
        #loading-mask
        {
            position: absolute;
            top: 0px;
            left: 0px;
            width: 100%;
            height: 100%;
            background: #D2E0F2;
            z-index: 20000;
        }
        #pageloading
        {
            position: absolute;
            top: 50%;
            left: 50%;
            margin: -120px 0px 0px -120px;
            text-align: center;
            border: 2px solid #8DB2E3;
            width: 200px;
            height: 40px;
            font-size: 14px;
            padding: 10px;
            font-weight: bold;
            background: #fff;
            color: #15428B;
        } */
    </style>
	<link href="../Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
    <script src="../Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
    <script src="../Comm/JScript.js" type="text/javascript"></script>
    <link href="../../DataUser/Style/table0.css" rel="stylesheet" type="text/css" />
    <script src="../Scripts/Jquery-plug/fileupload/jquery.uploadify.js" type="text/javascript"></script>
    <link href="../Scripts/Jquery-plug/fileupload/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript"> 
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
 
    function show_and_hide_tr(tb_id, obj) {
        $("#" + tb_id).find("tr").each(function (i) {
            i > 0 ? (this.style.display == "none" ? this.style.display = "" : this.style.display = "none") : ($(this).next().css("display") == "none" ? (obj.src = '../Img/Tree/Cut.gif') : (obj.src = '../Img/Tree/Add.gif'));
        });
    }
 	
    //执行保存
    function SaveDtlData(msg) {
        if (isChange == true) {
        	//console.log(window.parent.document.getElementById("Btn_Save"));
        	//window.parent.document.getElementById("Btn_Save").click();
        	$("*[id$=Btn_Save]")[0].click();
            isChange = false;
            window.location.reload();//刷新当前页面.
        }
    }

    function Change(id) {
    	//alert("Change");
        isChange = true;
    }

    function UploadFileChange() {
        this.detachEvent('onblur', '');
        isChange = false;
    }
    
    function TBHelp(ctrl,enName, attrKey) {
        var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName + "&AttrKey=" + attrKey;
        var str = window.showModalDialog(url, 'sd', 'dialogHeight: 500px; dialogWidth:400px; dialogTop: 150px; dialogLeft: 200px; center: no; help: no');
        if (str == undefined)
            return;
        
        $("*[id$=" + ctrl + "]").focus().val(str);
        
    }
    function TBHelp(ctrl, enName) {
        //alert(ctrl + "-" + enName);
        var explorer = window.navigator.userAgent;
        var str = "";
        var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName + "&AttrKey=" + ctrl + "&WordsSort=0" + "&FK_MapData=" + enName + "&id=" + ctrl;
        if (explorer.indexOf("Chrome") >= 0) {
            window.open(url, "sd", "left=200,height=500,top=150,width=600,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no");
        }
        else {
            str = window.showModalDialog(url, 'sd', 'dialogHeight: 500px; dialogWidth:600px; dialogTop: 150px; dialogLeft: 200px; center: no; help: no');
            if (str == undefined)
                return;
            ctrl = ctrl.replace("WorkCheck", "TB");
            $("*[id$=" + ctrl + "]").focus().val(str);
        }
    }
    function btn_Click() {
         $("#form1").submit();
    }
    function btn_Save_Click(){
    	$("#form1").submit();
    }
    //删除附件
    function DelWorkCheckAth(MyPK) {
        isChange = false;
        var basePath = '<%=basePath%>';
        var urlx = basePath+"WF/CCFormHeader/AttachmentUpload.do?DoType=DelWorkCheckAttach&PKVal=" + MyPK;
        if (confirm("确定要删除所选文件吗？")) {
            $.ajax({
                type: "post", //使用GET或POST方法访问后台
                dataType: "json", //返回json格式的数据
                contentType: "application/json; charset=utf-8",
                url: urlx, //要访问的后台地址
                //async: false,
                cache: false,
                success: function (msg) {//msg为返回的数据，在这里做数据绑定
                	if(msg=="true"||msg=='true'||msg==true){
                		isChange = true;
                        SaveDtlData(msg);
                        //window.location.reload();//刷新当前页面.
                	}
                }
            });
        }
    }
    function Ath(paras) {
        var url = "WorkCheckAth.jsp?1=2" + paras;
        str = window.showModalDialog(url, 'sd', 'dialogHeight: 200px; dialogWidth:800px; dialogTop: 150px; dialogLeft: 200px; center: no; help: no');
        if (str == undefined)
            return;
    }


    function AthDown(fk_ath, pkVal, delPKVal, fk_node, fk_flow, ath) {
        window.location.href = '../CCForm/AttachmentUpload.jsp?DoType=Down&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + '&FK_Node=' + fk_node + '&FK_Flow=' + fk_flow + '&FK_MapData=ND' + fk_node + '&Ath=' + ath;
    }
    function AthOpenOfiice(fk_ath, pkVal, delPKVal, FK_MapData, NoOfObj, FK_Node) {
    	alert("正在开发中");
        return;
        var date = new Date();
        var t = date.getFullYear() + "" + date.getMonth() + "" + date.getDay() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds();
        var url = '../WebOffice/AttachOffice.jsp?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + "&FK_MapData=" + FK_MapData + "&NoOfObj=" + NoOfObj + "&FK_Node=" + FK_Node + "&T=" + t;
        window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    }
    function AthOpenFileView(pkVal, delPKVal, FK_FrmAttachment, FK_FrmAttachmentExt, fk_flow, fk_node, workID, isCC) {
        alert("正在开发中");
        return;
    	var url = '../CCForm/FilesView.jsp?DoType=view&DelPKVal=' + delPKVal + '&PKVal=' + pkVal + '&FK_FrmAttachment=' + FK_FrmAttachment + '&FK_FrmAttachmentExt=' + FK_FrmAttachmentExt + '&FK_Flow=' + fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workID + '&IsCC=' + isCC;
        window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    }
    function AthOpenView(pkVal, delPKVal, FK_FrmAttachment, FK_FrmAttachmentExt, FK_Flow, FK_Node, WorkID, IsCC) {
    	alert("正在开发中");
        return;
    	var url = '../CCForm/FilesView.jsp?DoType=view&DelPKVal=' + delPKVal + '&PKVal=' + pkVal + '&FK_FrmAttachment=' + FK_FrmAttachment + '&FK_FrmAttachmentExt=' + FK_FrmAttachmentExt + '&FK_Flow=' + FK_Flow + '&FK_Node=' + FK_Node + '&WorkID=' + WorkID + '&IsCC=' + IsCC;
        window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    }
    $(function () {
        //$('.HBtn').hide();
        //$('#loading-mask').fadeOut();
    });
    
</script>
</head>
<body leftMargin=0 topMargin=0 >
    <form id="form1" method="post" action="<%=basePath %>/WF/WorkOpt/WorkCheckSave.do?s=2&r=q&FK_Flow=<%=FK_Flow %>&FK_Node=<%=FK_Node %>&FID=<%=FID %>&WorkID=<%=WorkID%>"  style="word-break:break-all">
    	<!-- <div id="loading-mask" class="loddingMask">
        	<div id="pageloading" class="pageloading">
            <img alt="" src="../Img/loading.gif" align="middle" />
	            请稍候...
	        </div>
   		</div> -->
   		<input type="button" id="Btn_Save" value="" Width="0" Height="0" class="HBtn" onclick="btn_Save_Click()" />
    	<%=wcModel.Pub1.toString() %>
    </form>
</body>
</html>
