window.onload = function(){

    var workid= GetQueryString("WorkID");

   var gwf=new Entity("BP.WF.GenerWorkFlow", workid);
   console.log(gwf.FK_Node);
    var en= new Entity("BP.WF.Flow",gwf.PFlowNo);
    
    console.log(en.PTable);
    
    
    var sql="SELECT ZhuTi,ZhengWenHao,BuMen from "+en.PTable+" WHERE OID="+gwf.PWorkID;
    var data=DBAccess.RunSQLReturnTable(sql);
    $("#TB_WenHao").val(data[0].ZhengWenHao);
     $("#TB_LaiWenDanWei").val(data[0].BuMen);
    //$("#TB_BiaoTi").val(data[0].ZhuTi);
    
    //正文链接
    document.getElementById('zhengwenurl').innerHTML ='<p ><a style="font-size:26px;" target="blank" href="'+window.location.protocol+"//"+window.location.host+'/WF/MyViewGener.htm?FK_Flow='+gwf.PFlowNo+'&WorkID='+gwf.PWorkID+'&FK_Node='+gwf.PNodeID+'">'+data[0].ZhuTi+'</a></p>';
}