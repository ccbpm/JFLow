
/* 
ʹ��˵��:
1. ���ļ����Զ������뵽 /WF/Comm/Search.htm ��.
2. �����������д���Լ��ķ����������������ϵİ�ť����, ����ɸ߼���js����.

 */

function NewFlowTemplate() {
    //   alert();
   //var url = "/App/FlowDesigner/NewFlow/Default.htm";
    var url = "/WF/Comm/Search.htm?EnsName=BP.Cloud.Template.FlowExts";
    var url = "/App/FlowDesigner/NewFlow.htm?EnsName=BP.Cloud.Template.FlowExts";

    window.location.href = url;
}

function ImpFlowTemplate() {

    //alert();
    //var url = "/App/FlowDesigner/NewFlow/Default.htm";
    var url = "/WF/Comm/Search.htm?EnsName=BP.Cloud.Template.FlowExts";
    var url = "/App/FlowDesigner/Template.htm?EnsName=BP.Cloud.Template.FlowExts";
    window.location.href = url;
}
function DelTodoEmps(str) {
    return str;
}