package bp.wf.dts;

import bp.en.*;
import bp.wf.*;

/** 
 修复节点表单map 的摘要说明
*/
public class RepariNodeFrmMap extends Method
{
	/** 
	 不带有参数的方法
	*/
	public RepariNodeFrmMap()throws Exception
	{
		this.Title = "修复节点表单";
		this.Help = "检查节点表单系统字段是否被非法删除，如果非法删除自动增加上它，这些字段包括:Rec,Title,OID,FID,WFState,RDT,CDT";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()throws Exception
	{
		Nodes nds = new Nodes();
		nds.RetrieveAllFromDBSource();


		String info = "";
		for (Node nd : nds.ToJavaList())
		{
			String msg = nd.RepareMap(nd.getHisFlow());
			if (!msg.equals(""))
			{
				info += "<b>对流程" + nd.getFlowName() + ",节点(" + nd.getNodeID() + ")(" + nd.getName() + "), 检查结果如下:</b>" + msg + "<hr>";
			}
		}
		return info + "执行完成...";
	}
}