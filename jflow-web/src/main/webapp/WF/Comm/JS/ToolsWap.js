function DoAutoTo( fk_emp, empName )
	{
	   if (window.confirm('您确定要把您的工作授权给['+fk_emp+']吗？')==false)
	       return;

	    var url='Do.jsp?DoType=AutoTo&FK_Emp='+fk_emp;
	    WinShowModalDialog(url,'');
	    alert('授权成功，请别忘记收回。'); 
	    window.location.href='Tools.jsp';
	}

	function ExitAuth(fk_emp) {
	    if (window.confirm('您确定要退出授权登录模式吗？') == false)
	        return;
	    var url = 'Do.jsp?DoType=ExitAuth&FK_Emp=' + fk_emp;
	    WinShowModalDialog(url, '');
	    window.location.href = 'Tools.jsp';
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
	    window.location.href='EmpWorks.jsp';
	}

	function CHPass()
	{
	    var url='Do.jsp?DoType=TakeBack';
	   // WinShowModalDialog(url,'');
	    alert('密码修改成功，请牢记您的新密码。'); 
	}
	function btn_Profile_Click(path){
		var tel=document.getElementById("TB_Tel").value;
		var mail=document.getElementById("TB_Email").value;
		var way=document.getElementById("DDL_Way").value;
        var url = path+"/WF/ToolsWap.do?tel="+tel+"&mail="+mail+"&way="+way;
		$("#form1").attr("action", url);
		$("#form1").submit();
		alert("设置生效，谢谢使用。");
		var url2 = path+"/WF/Tools.jsp";
		window.location.href=url2;
	}
	function btn_Click(path){
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
        	/*var url = "<%=basePath%>/WF/btn_Click.do?p1="+p1+"&p2="+p2+"&p3="+p3;
      		$("#form1").attr("action", url);
      		$("#form1").submit();
      		alert("保存成功");
      		return;*/
        	var url = path+"/WF/btn_Click.do?p1="+p1+"&p2="+p2+"&p3="+p3;
        	var url2 = path+"/WF/Tools.jsp";
      		$.ajax({
    			cache: true,
    			type: "POST",
    			url:url,
    			data:$('#form1').serialize(),
    		    success: function(data) {
    		    	alert(data);
    		    	window.location.href=url2;
    		    	
    		    }
    		});
         }else{
        	 alert("两次密码不一致。");
        	 return;
         }
         
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
	        	 img.src=document.all.F.value; 
	        	 var sInfo="Width:"+img.width+"px Height:"+img.height+ "px Size:"+getsizestring(img.fileSize); 
// 	        	 var img = null;  
// 	     	    img = document.createElement("img");  
// 	     	    document.body.insertAdjacentElement("beforeEnd", img); // firefox不行  
// 	     	    img.style.visibility = "hidden";   
// 	     	    img.src = ths.value;  
// 	     	    var imgwidth = img.offsetWidth;  
// 	     	    var imgheight = img.offsetHeight;  
	     	       
// 	     	    alert(imgwidth + "," + imgheight);  
	     	       
 	     	    if(img.width != 90 || img.width != 30) {  
 	     	        alert("图的尺寸应该是" + 90 + "x"+ 30);  
 	     	        ths.value = "";  
 	     	        return false;  
 	     	    }  
 	     	}
	}  
	
	function btn_Siganture_Click(path){
		var url = path+"/WF/btn_Siganture_Click.do?";
		$("#form1").attr("action", url);
		$("#form1").attr("enctype", "multipart/form-data");
		$("#form1").submit();
		alert("上传成功.");
		var url2 = path+"/WF/Tools.jsp";
		window.location.href=url2;
	}
	function btn_AdminSet_Click(path){
		var url = path+"/WF/btn_AdminSet_Click.do?";
		$("#form1").attr("action", url);
		$("#form1").attr("enctype", "multipart/form-data");
		$("#form1").submit();
		alert("保存成功.");
		var url2 = path+"/WF/Tools.jsp";
		window.location.href=url2;
	}
	
	function getFullPath(obj) 
	{
		if(obj) 
		{ 
			//ie 
			if (window.navigator.userAgent.indexOf("MSIE")>=1) 
			{ 
				obj.select();
				//alert(document.selection.createRange().text);
				return document.selection.createRange().text; 
			} 
			//firefox 
			else if(window.navigator.userAgent.indexOf("Firefox")>=1) 
			{ 
				if(obj.files) 
				{ 
					//return obj.files.item(0).getAsDataURL();
					return window.URL.createObjectURL(obj.files[0]);
				}
				//return obj.value;
				return window.URL.createObjectURL(obj.files[0]);
			}
			//alert(obj.value);
			return window.URL.createObjectURL(obj.files[0]); 
		} 
	}
	function uploadImg(path){
		if($("#x").val()==''||$("#x").val()==null||$("#x").val().length==0){
			alert("未获取到截取图片的数值");
			return false;
		}
		//console.log($('div .jcrop-tracker'));
		$("div .jcrop-tracker").each(function(){
			$("#height").val($(this).height());
		});
		$("div .jcrop-tracker").each(function(){
			$("#width").val($(this).width());
		});
		//return false;
        var url = path+"/WF/saveImgx.do";
		$("#form1").attr("action", url);
		$("#form1").attr("enctype", "multipart/form-data");
		$("#form1").submit();
		//alert("上传成功.");
	}