// JavaScript Document
var opened=true;
function mOver()
{
var o=document.getElementById("d");
o.style.backgroundPosition=opened ? "-20px 200px" : "-60px 200px";
o.style.backgroundColor="#e2edfb";
}
function mOut()
{
var o=document.getElementById("d");
o.style.backgroundPosition=opened ? "0 200px" : "-40px 200px";
o.style.backgroundColor="#ffffff";
}
function mClick()
{
var fm=parent.document.getElementById("frame");
var o=document.getElementById("d");
o.style.backgroundColor="#ffffff";
if (opened) {
// fm.cols="0,8,*";
opened=false;
o.style.backgroundPosition="-40px 200px";
closeLeft();
}else{
// fm.cols="180,8,*";
opened=true;
o.style.backgroundPosition="0px 200px";
openLeft();
}
}
function closeLeft()
{
	$("#menu_zzjsnet").css("display","none")
	$(".right_main").width($("body").width()-10);
}
function openLeft()
{
	$("#menu_zzjsnet").css("display","inline")
	$(".right_main").width($("body").width()-230);
}

function nav_dis(t){
	if($(t).next("div").css("display")=="none")
	{
		$(".nav_box").css("display","none");
		$(t).next("div").toggle(500);
	}
	else{
		$(t).next("div").toggle(500);
	}
}
function nav_lang(s){
	if(s=="over"){	
	$(".nav_lang ul").css("display","block");
	}else{
	$(".nav_lang ul").css("display","none");
	}
}