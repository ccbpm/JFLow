package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 修改人员编号 的摘要说明
*/
public class GenerPinYinForEmp extends Method
{
	/** 
	 不带有参数的方法
	*/
	public GenerPinYinForEmp()
	{
		this.Title = "为人员生成拼音，放入到 Port_Emp.PinYin 字段里.";
		this.Help = "为了检索方便，为所有的人员生成拼音, 方便在会签，移交，接受人查询。";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原用户名", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新用户名", true, false, 0, 10, 10);
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (BP.Web.WebUser.IsAdmin == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.AppCenterDBType) == true)
		{
			return "port_emp 是一个视图无法生成拼音.";
		}

		if (BP.DA.DBAccess.IsExitsTableCol("Port_Emp", BP.GPM.EmpAttr.PinYin) == false)
		{
			return "port_emp 不包含PinYin 这一列,无法生成拼音.";
		}

		BP.GPM.Emps emps = new BP.GPM.Emps();
		emps.RetrieveAll();
		for (BP.GPM.Emp item : emps)
		{
			if (item.PinYin.Contains("/") == true)
			{
				continue;
			}
			item.Update();
		}
		return "执行成功...";
	}
}