package BP.FlowEvent;

import BP.WF.FlowEventBase;

/**
 * xxxxxx 流程事件实体
 */
public class FEETemplete extends FlowEventBase
{
	// 构造.
	/**
	 * xxxxxx 流程事件实体
	 */
	public FEETemplete()
	{
	}
	
	// 构造.
	
	// 重写属性.
	@Override
	public String getFlowMark()
	{
		return "Templete";
	}
	
	// 重写属性.
	
	// 重写流程运动事件.
	/**
	 * 删除后
	 * 
	 * @return
	 */
	@Override
	public String AfterFlowDel()
	{
		return null;
	}
	
	/**
	 * 删除前
	 * 
	 * @return
	 */
	@Override
	public String BeforeFlowDel()
	{
		return null;
	}
	
	/**
	 * 结束后
	 * 
	 * @return
	 */
	@Override
	public String FlowOverAfter()
	{
		throw new RuntimeException("@已经调用到了结束后事件了.");
		// return null;
	}
	
	/**
	 * 结束前
	 * 
	 * @return
	 */
	@Override
	public String FlowOverBefore()
	{
		return null;
	}
	
	// 重写流程运动事件
	
	// 节点表单事件
	/**
	 * 表单载入前
	 */
	@Override
	public String FrmLoadAfter()
	{
		return null;
	}
	
	/**
	 * 表单载入后
	 */
	@Override
	public String FrmLoadBefore()
	{
		return null;
	}
	
	// 重写节点运动事件.
	
	@Override
	public String SaveBefore()
	{
		return null;
	}
	
	@Override
	public String SaveAfter()
	{
		return null;
	}
	
	@Override
	public String SendWhen()
	{
		return null;
	}
	
	@Override
	public String SendSuccess()
	{
		return null;
	}
	
	@Override
	public String SendError()
	{
		return null;
	}
	
	@Override
	public String ReturnAfter()
	{
		return null;
	}
	
	@Override
	public String ReturnBefore()
	{
		return null;
	}
	
	@Override
	public String UndoneAfter()
	{
		return null;
	}
	
	@Override
	public String UndoneBefore()
	{
		return null;
	}
	// 重写节点运动事件
}