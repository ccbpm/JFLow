package BP.WF.DTS;

import BP.En.Method;
import BP.GPM.Dept;
import BP.GPM.Depts;
import BP.Sys.OSModel;
import BP.WF.Glo;

/** 
 组织结构处理部门全路径
*/
public class OrgInit_NameOfPath extends Method
{
	/** 
	 组织结构处理部门全路径
	*/
	public OrgInit_NameOfPath()
	{
		this.Title = "组织结构-部门全路径NameOfPath";
		this.Help = "循环所有部门，重新生成NameOfPath";
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
		if (Glo.getOSModel() == OSModel.OneMore)
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		try
		{
			Depts depts = new Depts();
			depts.RetrieveAll();
			for (Dept dept : depts.ToJavaList())
			{ 
				dept.GenerNameOfPath();
			}
			return "执行成功...";
		}
		catch (RuntimeException ex)
		{
			return "执行失败：" + ex.getMessage();
		}
	}
}