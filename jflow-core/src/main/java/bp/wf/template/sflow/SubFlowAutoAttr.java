package bp.wf.template.sflow;


/** 
 自动触发子流程属性
*/
public class SubFlowAutoAttr extends SubFlowAttr
{

	/** 
	 当前节点为子流程时，所有子流程完成后启动他的同级子流程自动运行或者结束
	*/
	public static final String IsAutoSendSLSubFlowOver = "IsAutoSendSLSubFlowOver";
}