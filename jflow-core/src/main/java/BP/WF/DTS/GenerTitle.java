package BP.WF.DTS;

import BP.En.Method;

/** 
 重新生成标题
 
*/
public class GenerTitle extends Method
{
	/** 
	 重新生成标题
	*/
	public GenerTitle()
	{
		this.Title = "重新生成标题（为所有的流程，根据新的规则生成流程标题）";
		this.Help = "您也可以打开流程属性一个个的单独执行。";
	}
	/** 
	 设置执行变量
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	 
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		BP.WF.Template.FlowSheets ens = new BP.WF.Template.FlowSheets();
		for (BP.WF.Template.FlowSheet en : ens.ToJavaList())
		{
			en.DoGenerTitle();
		}
		return "执行成功...";
	}
}