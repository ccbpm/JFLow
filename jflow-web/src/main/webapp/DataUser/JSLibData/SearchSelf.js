
function NewFlow(flowNo) {
    //var url = "../MyFlow.htm?FK_Flow=" + flowNo;
    //window.open(url);
	var id= window.parent.nthTabs.getActiveId();
	var tab=window.parent.nthTabs.getTabList();
	var work= "#"+flowNo+"TLJ"+id;
	for (var i=0;i<tab.length;i++){ 
	    if(tab[i].id==work){
	    	window.parent.nthTabs.setActTab(tab[i].id);
	    	return;
	    }
	}
	window.parent.nthTabs.addTab({id:flowNo+"TLJ"+id,title:"起草",url:"../WF/MyFlow.htm?FK_Flow=" + flowNo}).setActTab(flowNo+"TLJ"+id);
    return;
}

function OpenTab(url)
{

}

/* 

ʹ��˵��:

1. ���ļ����Զ������뵽 /WF/Comm/Search.htm ��.
2. �����������д���Լ��ķ����������������ϵİ�ť����, ����ɸ߼���js����.

 */

function DealTodoEmps(str){
	var result = str.split("@");
	var reg = /[a-zA-Z\,]+/;
	while(result = str.match(reg)){
		str = str.replace(result[0],'');
	}
	return str;
	
	
}