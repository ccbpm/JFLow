// JavaScript Document
$(function() {
	$("input[name=leixing]").click(function(){
		if($("input[name=leixing]:checked").length>0){
			$(".airs").show();
			$(this).parent().parent().children("td:first").text(" ");
			$(this).parent().parent().next("tr").children("td:first").html("<span class='next_tr'></span>")
		}else{
			$(".airs input:checkbox").removeAttr("checked","checked");
			$(".waypoint input:checkbox").removeAttr("checked","checked");
			$(".airs td:first").text(" ");
			$(".waypoint td:first").text(" ");
			$(this).parent().parent().children("td:first").html("<span class='next_tr'></span>")
			$(".airs").hide();
			$(".waypoint").hide();
		}
	})
	$(".airs input:checkbox").click(function(){
		if($("input[name=airs]:checked").length>0){
			$(".waypoint").show();
			$(".airs td:first").text(" ");
			$(".waypoint td:first").html("<span class='next_tr'></span>")
		}else{
			$(".waypoint").hide();
			$(".waypoint td:first").text(" ");
			$(".airs td:first").html("<span class='next_tr'></span>")
		}
	})
	$(".week select").change(function(){
		if($(".week select").eq(0).val()!="" && $(".week select").eq(1).val()!=""){
			$(".week td:first").text(" ");
			$(".week").next("tr").children("td:first").html("<span class='next_tr'></span>");
			weekDate();
		}else{
			$(".week").next("tr").children("td:first").text(" ");
			$(".week td:first").html("<span class='next_tr'></span>");
			$(".airs input:checkbox").removeAttr("checked","checked");
			$(".waypoint input:checkbox").removeAttr("checked","checked");
			$(".airs td:first").text(" ");
			$(".waypoint td:first").text(" ");
			$(".select_week_day").text(" ");
			$(this).parent().parent().children("td:first").html("<span class='next_tr'></span>")
			$(".airs").hide();
			$(".waypoint").hide();
			
		}
	})
});

function checkAll(a){
	if($('input[name='+a+']:checked').length<$('input[name='+a+']').length){		
		$('input[name='+a+']').attr("checked","checked");		
	}else{
		$('input[name='+a+']').removeAttr("checked","checked");	
	}
	
}
function weekDate(){
			var dd = new Date();
			var fweek=new Date($(".select_year").val(),0,1).getDay();			
			var myDate=new Date($(".select_year").val(),0,1);
			
			myDate=myDate.valueOf();
			myDate=myDate+(5-fweek)* 24 * 60 * 60 * 1000;
			myDate = new Date(myDate);
			myDate=myDate.valueOf();
			var arrayObj = new Array();
			var arrMons=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
			for(var i=0;i<52;i++){
				w=myDate+6* 24 * 60 * 60 * 1000;
				w=new Date(w);
				t=new Date(myDate);
				arrayObj[i]=t.getDate()+arrMons[t.getMonth()]+t.getFullYear()+" - "+w.getDate()+arrMons[w.getMonth()]+w.getFullYear();
				myDate=w.valueOf();
				myDate=myDate+1* 24 * 60 * 60 * 1000
			}
			$(".select_week_day").text(arrayObj[$(".select_week").val()-1]);
}