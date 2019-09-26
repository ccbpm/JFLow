package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class WF_Admin_Port extends DirectoryPageBase
{


	/** 
	 构造函数
	*/
	public WF_Admin_Port()
	{
	}


		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}

		///#endregion 执行父类的重写方法.


		///#region OrderOfDept 部门顺序调整 .
	/** 
	 
	 
	 @return 
	*/
	public final String OrderOfDept_Init()
	{
		String sql = "SELECT No,Name,ParentNo,Idx FROM Port_Dept";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		return "";
	}

		///#endregion xxx 界面方法.

}