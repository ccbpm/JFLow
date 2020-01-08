
/*
1. 该页面,是被引用到 /WF/MyFlowGener.htm, /WF/CCForm/FrmGener.htm 里面的.
2. 这里方法大多是执行后，返回json ,可以被页面控件调用. 
*/
function funDemo() {
   alert("我被执行了。");
    return true;
}

function sendbeforea(){
	return true;
}

function setShuZhi3Value(){
	alert("我被执行了11。");
}
/**从表附件导入的固定方法，不可人为删除**/
//FK_MapData,附件属性，RefPK,FK_Node
function afterDtlImp(FK_MapData, frmAth, newOID, FK_Node, oldOID,oldFK_MapData) {
    //处理从表附件导入的事件
}

function ChangeFormValue(form1,form2){
	//根据WorkID 获取form1表单的数据
	var handler = new HttpHandler("B")
	var workID = GetQueryString("WorkID");
	var en = new Entity("BP.Sys.GEEntity");
	
	en.OID = workID;
	
	
}