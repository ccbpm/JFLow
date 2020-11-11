package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;

/** 
 页面功能实体
*/
public class WF_Admin_Port extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_Port()
	{
	}


		///执行父类的重写方法.
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
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" +CommonUtils.getRequest().getRequestURI());
	}

		/// 执行父类的重写方法.


		///OrderOfDept 部门顺序调整 .
	/** 
	 
	 
	 @return 
	*/
	public final String OrderOfDept_Init()
	{
		String sql = "SELECT No,Name,ParentNo,Idx FROM Port_Dept";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return "";
	}

		/// xxx 界面方法.



}