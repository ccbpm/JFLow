<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head2.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath%>/WF/Comm/JS/Calendar/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=basePath%>/WF/Comm/JS/Calendar/jquery-1.3.1.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/WF/Comm/JS/Calendar/jquery.bitmapcutter.js"></script>
<link rel="Stylesheet" type="text/css"
	href="<%=basePath%>/WF/Comm/CSS/jquery.bitmapcutter.css" />
<link href='<%=basePath%>/WF/Comm/Style/Table0.css' rel='stylesheet'
	type='text/css' />
<script src="<%=basePath%>/WF/Comm/JS/Calendar/jquery.Jcrop.js"></script>
<script type="text/javascript">
	jQuery(function($) {

		// Create variables (in this scope) to hold the API and image size
		var jcrop_api, boundx, boundy,

		// Grab some information about the preview pane
		$preview = $('#preview-pane'), $pcnt = $('#preview-pane .preview-container'), $pimg = $('#preview-pane .preview-container img'),

		xsize = $pcnt.width(), ysize = $pcnt.height();

		console.log('init', [ xsize, ysize ]);
		$('#target').Jcrop({
			onChange : updatePreview,
			onSelect : updatePreview,
			aspectRatio : xsize / ysize
		}, function() {
			// Use the API to get the real image size
			var bounds = this.getBounds();
			boundx = bounds[0];
			boundy = bounds[1];
			// Store the API in the jcrop_api variable
			jcrop_api = this;

			// Move the preview into the jcrop container for css positioning
			$preview.appendTo(jcrop_api.ui.holder);
		});

		function updatePreview(c) {
			if (parseInt(c.w) > 0) {
				var rx = xsize / c.w;
				var ry = ysize / c.h;

				$pimg.css({
					width : Math.round(rx * boundx) + 'px',
					height : Math.round(ry * boundy) + 'px',
					marginLeft : '-' + Math.round(rx * c.x) + 'px',
					marginTop : '-' + Math.round(ry * c.y) + 'px'
				});
			}
		}
		;

	});
</script>
<link rel="stylesheet" href="<%=basePath%>/WF/Comm/CSS/demos.css" type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/WF/Comm/CSS/jquery.Jcrop.css" type="text/css" />
<style type="text/css">

/* Apply these styles only when #preview-pane has
   been placed within the Jcrop widget */
.jcrop-holder #preview-pane {
	display: block;
	position: absolute;
	z-index: 2000;
	top: 10px;
	right: -280px;
	padding: 6px;
	border: 1px rgba(0, 0, 0, .4) solid;
	background-color: white;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
	-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
	box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
}

/* The Javascript code will set the aspect ratio of the crop
   area based on the size of the thumbnail preview,
   specified here */
#preview-pane .preview-container {
	width: 250px;
	height: 170px;
	overflow: hidden;
}
</style>
	
	
<script language="JavaScript" src="<%=basePath%>/WF/Comm/JScript.js"></script>
<script type="text/javascript">
$('#element_id').Jcrop();
jQuery(function($){

    // I did JSON.stringify(jcrop_api.tellSelect()) on a crop I liked:
    var c = {"x":13,"y":7,"x2":487,"y2":107,"w":474,"h":100};

    $('#target').Jcrop({
      bgFade: true,
      setSelect: [c.x,c.y,c.x2,c.y2]
    });

  });
	function SetSelected(cb,ids) {
		alert(ids);
		var arrmp = ids.split(',');
		var arrObj = document.all;
		var isCheck = false;
		if (cb.checked)
			isCheck = true;
		else
			isCheck = false;
		for (var i = 0; i < arrObj.length; i++) {
			if (typeof arrObj[i].type != "undefined"
					&& arrObj[i].type == 'checkbox') {
				for (var idx = 0; idx <= arrmp.length; idx++) {
					if (arrmp[idx] == '')
						continue;
					var cid = arrObj[i].name + ',';
					var ctmp = arrmp[idx] + ',';
					if (cid.indexOf(ctmp) > 1) {
						arrObj[i].checked = isCheck;
						//                    alert(arrObj[i].name + ' is checked ');
						//                    alert(cid + ctmp);
					}
				}
			}
		}
	}
	$(function() {
		try {
			var url = '';
			var width = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_WSize").value;
			var height = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_HSize").value;
			var cwidth = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_Chg").value;
			var name = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_ImageName").value;
			if (name) {
				url = '/DataUser/UserIcon/' + name;
				var cw = width / 2;
				var ch = width / 2;
				$.fn.bitmapCutter({
					src : url,
					renderTo : '#Container',
					holderSize : {
						width : width,
						height : height
					},
					cutterSize : {
						width : 200,
						height : 200
					},
					onGenerated : function(src) {
						alert(src);
					},
					rotateAngle : 90,
					lang : {
						clockwise : '顺时针旋转{0}度.'
					}
				});
			}
		} catch (e) {

		}
	});
	function DoAutoTo( fk_emp, empName )
	{
	   if (window.confirm('您确定要把您的工作授权给['+fk_emp+']吗？')==false)
	       return;

	    var url='Do.jsp?DoType=AutoTo&FK_Emp='+fk_emp;
	    WinShowModalDialog(url,'');
	    alert('授权成功，请别忘记收回。'); 
	    window.location.href='Tools.aspx';
	}

	function ExitAuth(fk_emp) {
	    if (window.confirm('您确定要退出授权登录模式吗？') == false)
	        return;
	    var url = 'Do.jsp?DoType=ExitAuth&FK_Emp=' + fk_emp;
	    WinShowModalDialog(url, '');
	    window.location.href = 'Tools.aspx';
	}

	function TakeBack( fk_emp )
	{
	   if (window.confirm('您确定要取消对['+fk_emp+']的授权吗？')==false)
	       return;
	    var url='Do.jsp?DoType=TakeBack';
	    WinShowModalDialog(url,'');
	    alert('您已经成功的取消。'); 
	    window.location.reload();
	}

	function LogAs( fk_emp )
	{
	   if (window.confirm('您确定要以['+fk_emp+']授权方式登录吗？')==false)
	       return;
	       
	    var url='Do.jsp?DoType=LogAs&FK_Emp='+fk_emp;
	    WinShowModalDialog(url,'');
	    alert('登录成功，现在您可以以['+fk_emp+']处理工作。'); 
	    window.location.href='EmpWorks.aspx';
	}

	function CHPass()
	{
	    var url='Do.jsp?DoType=TakeBack';
	   // WinShowModalDialog(url,'');
	    alert('密码修改成功，请牢记您的新密码。'); 
	}
	function btn_Profile_Click(){
		var tel=document.getElementById("TB_Tel").value;
		var mail=document.getElementById("TB_Email").value;
		var way=document.getElementById("DDL_Way").value;
        var url = "<%=basePath%>/WF/ToolsWap.do?tel="+tel+"&mail="+mail+"&way="+way;
		$("#form1").attr("action", url);
		$("#form1").submit();
		alert("设置生效，谢谢使用。");
	}
	function btn_Click(){
		 var p1 = document.getElementById("TB_Pass1").value;
		 var p2 = document.getElementById("TB_Pass2").value;
		 var p3 = document.getElementById("TB_Pass3").value;
         if (p2.length == 0 || p1.length == 0)
         {
             alert("密码不能为空");
             return;
         }

         if (p2 == p3)
         {
        	var url = "<%=basePath%>/WF/btn_Click.do?p1="+p1+"&p2="+p2+"&p3="+p3;
      		$("#form1").attr("action", url);
      		$("#form1").submit();
      		return;
         }else{
        	 alert("两次密码不一致。");
        	 return;
         }
         
	}
	function btnSaveIt_Click(){
		var FK_Emp='<%=request.getParameter("FK_Emp")%>';
		var TB_DT=document.getElementById("TB_DT").value;
		var DDL_AuthorWay=document.getElementById("DDL_AuthorWay").value;
		var url = "<%=basePath%>/WF/btnSaveIt_Click.do?FK_Emp=" + FK_Emp+ "&TB_DT=" + TB_DT + "&DDL_AuthorWay=" + DDL_AuthorWay;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	function btnSaveAthFlows_Click(){
		<%-- var CB_=document.getElementById("CB_<%=fl.getNo()%>").value; --%>
		var url = "<%=basePath%>/WF/btnSaveAthFlows_Click.do?";
		$("#form1").attr("action", url);
		$("#form1").submit();
		alert("保存成功.");
	}
	function checkImgType(ths){  
	        if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(ths.value)) {  
	            alert("图片类型必须是.gif,jpeg,jpg,png中的一种");  
	            ths.value = "";  
	            return false;  
	        }  
	         else
	        {
	        	 var img=new Image(); 
	        	 alert("123");
	        	 img.src=document.all.F.value; 
	        	 alert(document.all.F.value);
	        	 var sInfo="Width:"+img.width+"px Height:"+img.height+ "px Size:"+getsizestring(img.fileSize); 
	        	 alert("789");
	        	 alert(sInfo); 

// 	        	 var img = null;  
// 	     	    img = document.createElement("img");  
// 	     	    document.body.insertAdjacentElement("beforeEnd", img); // firefox不行  
// 	     	    img.style.visibility = "hidden";   
// 	     	    img.src = ths.value;  
// 	     	    var imgwidth = img.offsetWidth;  
// 	     	    var imgheight = img.offsetHeight;  
	     	       
// 	     	    alert(imgwidth + "," + imgheight);  
	     	       
// 	     	    if(imgwidth != 90 || imgheight != 30) {  
// 	     	        alert("图的尺寸应该是" + 90 + "x"+ 30);  
// 	     	        ths.value = "";  
// 	     	        return false;  
// 	     	    }  
//	     	    return true;  
	          }
	}  
	
	function btn_Siganture_Click(){
		var url = "<%=basePath%>/WF/btn_Siganture_Click.do?";
		$("#form1").attr("action", url);
		$("#form1").attr("enctype", "multipart/form-data");
		$("#form1").submit();
		alert("上传成功.");
	}
	function btn_AdminSet_Click(){
		var url = "<%=basePath%>/WF/btn_AdminSet_Click.do?";
		$("#form1").attr("action", url);
		$("#form1").attr("enctype", "multipart/form-data");
		$("#form1").submit();
		alert("保存成功.");
	}
</script>
<style type="text/css">
#Container {
	width: 1100px;
	margin: 10px auto;
}

#Player {
	width: 500px;
	height: 20px;
	padding: 1px;
}

#Container {
	width: 800px;
}

#Content {
	height: 600px;
	margin-top: 20px;
}

#Content-Left {
	height: 450px;
	width: 600px;
	margin: 20px;
	float: left;
}

#Content-Main {
	height: 60px;
	width: 60px;
	margin: 20px;
	float: left;
}

#Content-Main1 {
	height: 100px;
	width: 100px;
	margin: 20px;
	float: left;
}

#Content-Main2 {
	height: 40px;
	width: 40px;
	margin: 20px;
	float: left;
}

#Content-Main3 {
	height: 10px;
	width: 50px;
	margin: 20px;
	float: left;
}
</style>
</head>
<body>
	<legend>&nbsp;设置图标&nbsp;</legend>
	<div style="width: 800; height: 550">
		<div style="margin-left: 20px; margin-top: 20px">
			<input type="file" id="file1" name="UpFile" size="46"
				onchange="document.getElementById('img').src=getFullPath(this);" /><input
				type="button" value="确定" name="btnOk" onclick="show()" />
		</div>
		<div class="container">
			<div class="row">
				<div class="span12">
					<div class="jc-demo-box">
						<img
							src="<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>BigerCon.png"
							id="target" alt="[Jcrop Example]" />

						<div id="preview-pane">
							<div class="preview-container">
								<img
									src="<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>BigerCon.png"
									class="jcrop-preview" alt="Preview" name="imgNew" />
							</div>
							<div>
								<input type="button" onclick="uploadImg()" value="生成图标"
									name="btnSave" />
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>