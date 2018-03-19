<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%  FrmModel frmModel = new FrmModel(request, response);
	frmModel.loadModel();
%>
<%@ include file="/WF/head/head1.jsp"%>
<title>表单预览</title>
<head>
<link href="../Style/FormThemes/CCFormFrm.css" rel="stylesheet" type="text/css" />
<style>
legend {
	display: block;
	width: 100%;
	margin-bottom: 0rem !important;
	font-size: 0rem !important;
	line-height: inherit;
	color: #333;
	border-bottom: 1px solid #e5e5e5;
	padding-bottom: 0rem !important;
    color: green;
    font-style: inherit;
    font-weight: bold;
}
</style>
<script src="../Activex/Sign/main.js" type="text/javascript"></script>
<script src="../Comm/JScript.js" type="text/javascript"></script>
<script src="MapExt.js" type="text/javascript"></script>
<script type="text/javascript">
	function ReturnVal(ctrl, url, winName) {
	    
	    if (ctrl && ctrl.value != "") {
	        if (url.indexOf('?') > 0)
	            url = url + '&CtrlVal=' + ctrl.value;
	        else
	            url = url + '?CtrlVal=' + ctrl.value;
	    }
	    if (typeof self.parent.TabFormExists != 'undefined') {
	        var bExists = self.parent.TabFormExists();
	        if (bExists) {
	            self.parent.ChangTabFormTitleRemove();
	        }
	    }
	   
	    if (window.ActiveXObject) { 
	        var v = window.showModalDialog(url, winName, 'scrollbars=yes;resizable=yes;center=yes;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 850px; dialogTop: 100px; dialogLeft: 150px;');
	        if (v == null || v == '' || v == 'NaN') {
	            return;
	        }
	        ctrl.value = v;
	    } else { 
	       	var v = window.showModalDialog(url, '', 'scrollbars=yes;resizable=yes;center=yes;resizable:no;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 550px; dialogTop: 50px; dialogLeft: 650px;');
	       	 if (v == null || v == '' || v == 'NaN') {
	                return;
	            }
	       	
	       	ctrl.value = v;
	        	 
	    }
	    if (typeof self.parent.TabFormExists != 'undefined') {
	        var bExists = self.parent.TabFormExists();
	        if (bExists) {
	            self.parent.ChangTabFormTitle();
	        }
	    }
	    	return;
	}


       $(function () {
           SetHeight();
       });

       function SetHeight() {
           var screenHeight = document.documentElement.clientHeight;

           var frmHeight = "<%=frmModel.getHeight()%>";
           if (screenHeight > parseFloat(frmHeight)) {
               $("#divCCForm").height(screenHeight);

           }
       }
       $(window).resize(function () {
           SetHeight();
       });

       function checkData() {
           try {
               var strObjectName;
               strObjectName = document.all.DWebSignSeal.FindSeal("", 0);
               while (strObjectName != "") {
                   var v = document.all.DWebSignSeal.VerifyDoc(strObjectName);
                   strObjectName = document.all.DWebSignSeal.FindSeal(strObjectName, 0);

                   SetSealType(v);

               }
           } catch (e) {
               //alert("控件没有安装，请刷新本页面，控件会自动下载。\r\n或者下载安装程序安装。" +e);
           }
       }

       // 获取DDL值
       function ReqDDL(ddlID) {
           var v = document.getElementById('DDL_' + ddlID).value;
           if (v == null) {
               alert('没有找到ID=' + ddlID + '的下拉框控件.');
           }
           return v;
       }

       // 获取TB值
       function ReqTB(tbID) {
           var v = document.getElementById('TB_' + tbID).value;
           if (v == null) {
               alert('没有找到ID=' + tbID + '的文本框控件.');
           }
           return v;
       }

       // 获取CheckBox值
       function ReqCB(cbID) {
           var v = document.getElementById('CB_' + cbID).value;
           if (v == null) {
               alert('没有找到ID=' + cbID + '的文本框控件.');
           }
           return v;
       }

       /// 获取DDL Obj
       function ReqDDLObj(ddlID) {
           var v = document.getElementById('DDL_' + ddlID);
           if (v == null) {
               alert('没有找到ID=' + ddlID + '的下拉框控件.');
           }
           return v;
       }
       // 获取TB Obj
       function ReqTBObj(tbID) {
           var v = document.getElementById('TB_' + tbID);
           if (v == null) {
               alert('没有找到ID=' + tbID + '的文本框控件.');
           }
           return v;
       }
       // 获取CheckBox Obj值
       function ReqCBObj(cbID) {
           var v = document.getElementById('CB_' + cbID);
           if (v == null) {
               alert('没有找到ID=' + cbID + '的单选控件.');
           }
           return v;
       }
       // 设置值.
       function SetCtrlVal(ctrlID, val) {
           $('#TB_' + ctrlID).val(val);
           $('#DDL_' + ctrlID).val(val);
           $('#CB_' + ctrlID).val(val);
       }

       var isFrmChange = false;
       var isChange = false;
       function SaveDtlData() {
           //加正则验证
           if (SysCheckFrm() == false)
               return false;

           if (isChange == false)
               return;

           try {
               if (document.all.DWebSignSeal) {
                   var state = document.getElementById("TB_SealState");


                   var v = document.all.DWebSignSeal.GetStoreData();
                   if (v.length == "" && "<%=frmModel.getIsSign()%>" == "True") {

                       return false;
                   }

                   if (state.value != 0) {
                       alert("表单内容已改变,请重新签名！");
                       return false;
                   }
                   var sealData = document.getElementById("TB_SealData");
                   var signData = document.getElementById("TB_SealData");
                   //SaveSealToFile();
                   var s = GetBmpSeal();
                   signData.value = s;
                   sealData.value = v;
               }
           }
           catch (e) {
           }

           var btn = document.getElementById("Btn_Save");
           if (btn) {
               btn.click();
               isChange = false;
           }
       }

       function SetSealType(v) {
           var state = document.getElementById("TB_SealState");
           state.value = v;
       }

       function Change(id) {
           if (document.all.DWebSignSeal) {
               checkData();
           }
           isChange = true;
           var tagElement = window.parent.document.getElementById("HL" + id);
           if (tagElement) {
               var tabText = tagElement.innerText;
               var lastChar = tabText.substring(tabText.length - 1, tabText.length);
               if (lastChar != "*") {
                   tagElement.innerHTML = tagElement.innerText + '*';
               }
           }

           if (typeof self.parent.TabFormExists != 'undefined') {
               var bExists = self.parent.TabFormExists();
               if (bExists) {
                   self.parent.ChangTabFormTitle();
               }
           }
       }

       function TROver(ctrl) {
           ctrl.style.backgroundColor = 'LightSteelBlue';
       }

       function TROut(ctrl) {
           ctrl.style.backgroundColor = 'white';
       }
       function Del(id, ens, refPk, pageIdx) {
           if (window.confirm('您确定要执行删除吗？') == false)
               return;

           var url = 'Do.jsp?DoType=DelDtl&OID=' + id + '&EnsName=' + ens;
           var b = window.showModalDialog(url, 'ass', 'dialogHeight: 400px; dialogWidth: 600px;center: yes; help: no');
           window.location.href = 'Dtl.jsp?EnsName=' + ens + '&RefPKVal=' + refPk + '&PageIdx=' + pageIdx;
       }
       function DtlOpt(workId, fk_mapdtl) {
           var url = 'DtlOpt.jsp?WorkID=' + workId + '&FK_MapDtl=' + fk_mapdtl;
           var b = window.showModalDialog(url, 'ass', 'dialogHeight: 400px; dialogWidth: 600px;center: yes; help: no');
           window.location.href = 'Dtl.jsp?EnsName=' + fk_mapdtl + '&RefPKVal=' + workId;
       }
       function OnKeyPress() {
       }
       function OpenOfiice(fk_ath, pkVal, delPKVal, FK_MapData, NoOfObj, FK_Node) {
           var date = new Date();
           var t = date.getFullYear() + "" + date.getMonth() + "" + date.getDay() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds();

           var url = '../WebOffice/AttachOffice.jsp?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal + "&FK_MapData=" + FK_MapData + "&NoOfObj=" + NoOfObj + "&FK_Node=" + FK_Node + "&T=" + t;
           //var url = 'WebOffice.jsp?DoType=EditOffice&DelPKVal=' + delPKVal + '&FK_FrmAttachment=' + fk_ath + '&PKVal=' + pkVal;
           // var str = window.showModalDialog(url, '', 'dialogHeight: 1250px; dialogWidth:900px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no;resizable:yes');
           //var str = window.open(url, '', 'dialogHeight: 1200px; dialogWidth:1110px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no;resizable:yes');
           window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');

       }
       function ReinitIframe(frmID, tdID) {
           try {

               var iframe = document.getElementById(frmID);
               var tdF = document.getElementById(tdID);

               iframe.height = iframe.contentWindow.document.body.scrollHeight;
               iframe.width = iframe.contentWindow.document.body.scrollWidth;

               if (tdF.width < iframe.width) {
                   //alert(tdF.width +'  ' + iframe.width);
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
       function OnLoadCheckFrmSlnIsNull() {
           $('.HBtn').hide();
           if (typeof CheckFrmSlnIsNull != 'undefined' && CheckFrmSlnIsNull instanceof Function) {
               CheckFrmSlnIsNull(); //if (CheckFrmSlnIsNull() == false) return false;
           }

           try {
               if (document.all.DWebSignSeal) {
                   var sealData = document.getElementById("TB_SingData");
                   document.all.DWebSignSeal.SetStoreData(sealData.value);
                   document.all.DWebSignSeal.ShowWebSeals();

               }
           } catch (e) {
           }


       }
       window.onload = OnLoadCheckFrmSlnIsNull;
       function GetFilePath() {
           return document.getElementById("TB_SealFile").value;
	   }
	   function Save(){
       var value = document.getElementById("ucen").innerHTML;
       $('#FrmBody').val(value);
   		$.ajax({
   				cache: true,
   				type: "POST",
   				url:"<%=basePath%>WF/CCForm/FrmSave.do",
   				data:$('#frm_view').serialize()
   			});
   	   }  
</script>
<%
	int script_size = frmModel.getScripts().size();
	for(int i = 0; i < script_size; i++){
		String script = frmModel.getScripts().get(i);
		%>
		<script type="text/javascript" src="<%=basePath+script%>"></script>
		<%
	}
%>
<%
	int ccs_links_size = frmModel.getCCSLinks().size();
	for(int i = 0; i < ccs_links_size; i++){
		String ccsLink = frmModel.getCCSLinks().get(i);
		%>
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath+ccsLink%>" />
		<%
	}
%>
<%=frmModel.scriptsBlock.toString() %>
</head>
<body >
	<div class="container">
        <div style="float: left;">
        	<input type="text" id="TB_SealState" name="TB_SealState" style="display: none">
        	<input type="text" id="TB_SingData" name="TB_SingData" style="display: none" >
        	<input type="text" id="TB_SealFile" name="TB_SealFile" style="display: none" value="<%= frmModel.TB_SealFile_Value%>" >
        	 <input type="text" id="TextBox1" name="TextBox1" style="display: none" >
        	 <input type="button" ID="Btn_Save" runat="server" Text="" Width="0" Height="0" class="HBtn"
                Visible="false" OnClick="save()" value="保存" style="display: none"/>
        </div>
       <input type="button" ID="Btn_Print" runat="server" value="打印" class="Btn" Visible="true" style="display: none"/>
        <form id="frm_view">
        	<input type="hidden" id="FrmBody" name="FrmBody" value="">
        	<input type="hidden" name="OID" value="<%=frmModel.getOID() %>">
	        <input type="hidden" name="OIDPKVal" value="<%=frmModel.getOIDPKVal() %>">
	        <input type="hidden" name="FK_MapData" value="<%=frmModel.getFK_MapData() %>">
	       	<input type="hidden" name="FK_Node" value="<%=frmModel.getFK_Node()%>">
        	<div class="uecn" id="ucen">
        		<%=frmModel.Pub.toString() %>
        	</div>
        </form>
        <input type="text" id="TB_SealData" style="display: none" value="<%= frmModel.TB_SealData_Value%>">
    </div>
</body>
</html>