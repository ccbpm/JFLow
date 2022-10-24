package bp.wf.dts;

import bp.en.*;

// using Security.Principal.WindowsIdentity;


/** 
 Method 的摘要说明
*/
public class DTSDominInfo extends Method
{
	/** 
	 不带有参数的方法
	*/
	public DTSDominInfo()throws Exception
	{
		this.Title = "生成域数据";
		this.Help = "生成域数据(未完成)";

		this.GroupName = "AD数据管理";

		// this.HisAttrs.AddTBString("Path", "C:/ccflow.Template", "生成的路径", true, false, 1, 1900, 200);
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
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()throws Exception
	{
		return "功能未实现。";

	}
}