package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 Method 的摘要说明
*/
public class UpdateCCFlow extends Method
{
	/** 
	 不带有参数的方法
	*/
	public UpdateCCFlow()
	{
		this.Title = "升级ccflow";
		this.Help = "执行对ccflow升级，如果您更新下来了最新的代码，您就需要执行该功能，进行对ccflow的数据库升级。";
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
	public Object Do()
	{
		if (!WebUser.getNo().equals("admin"))
		{
			return "非法的用户执行。";
		}

		BP.WF.Glo.UpdataCCFlowVer();

		return "执行成功,系统已经修复了最新版本的数据库.";
	}
}