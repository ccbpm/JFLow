package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_AppClassic extends DirectoryPageBase
{


	/** 
	 构造函数
	*/
	public WF_AppClassic()
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


		///#region xxx 界面 .


		///#endregion xxx 界面方法.

	/** 
	 初始化Home
	 
	 @return 
	*/
	public final String Home_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		//系统名称.
		ht.put("SysName", BP.Sys.SystemConfig.SysName);
		ht.put("CustomerName", BP.Sys.SystemConfig.CustomerName);

		ht.put("Todolist_EmpWorks", BP.WF.Dev2Interface.getTodolist_EmpWorks());
		ht.put("Todolist_Runing", BP.WF.Dev2Interface.getTodolist_Runing());
		ht.put("Todolist_Sharing", BP.WF.Dev2Interface.getTodolist_Sharing());
		ht.put("Todolist_CCWorks", BP.WF.Dev2Interface.getTodolist_CCWorks());
		ht.put("Todolist_Apply", BP.WF.Dev2Interface.getTodolist_Apply()); //申请下来的任务个数.
		ht.put("Todolist_Draft", BP.WF.Dev2Interface.getTodolist_Draft()); //草稿数量.
		ht.put("Todolist_Complete", BP.WF.Dev2Interface.getTodolist_Complete()); //完成数量.
		ht.put("UserDeptName", WebUser.getFK_DeptName);

		//我发起
		MyStartFlows myStartFlows = new MyStartFlows();
		QueryObject obj = new QueryObject(myStartFlows);
		obj.AddWhere(MyStartFlowAttr.Starter, WebUser.getNo());
		obj.addAnd();
		//运行中\已完成\挂起\退回\转发\加签\批处理\
		obj.addLeftBracket();
		obj.AddWhere("WFState=2 or WFState=3 or WFState=4 or WFState=5 or WFState=6 or WFState=8 or WFState=10");
		obj.addRightBracket();
		obj.DoQuery();
		ht.put("Todolist_MyStartFlow", myStartFlows.size());

		//我参与
		MyJoinFlows myFlows = new MyJoinFlows();
		obj = new QueryObject(myFlows);
		obj.AddWhere("Emps like '%" + WebUser.getNo() + "%'");
		obj.DoQuery();
		ht.put("Todolist_MyFlow", myFlows.size());

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	public final String Index_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("Todolist_Runing", BP.WF.Dev2Interface.getTodolist_Runing()); //运行中.
		ht.put("Todolist_EmpWorks", BP.WF.Dev2Interface.getTodolist_EmpWorks()); //待办
		ht.put("Todolist_CCWorks", BP.WF.Dev2Interface.getTodolist_CCWorks()); //抄送.

		//本周.
		ht.put("TodayNum", BP.WF.Dev2Interface.getTodolist_CCWorks()); //抄送.

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}


		///#region 登录界面.
	public final String Portal_Login()
	{
		String userNo = this.GetRequestVal("UserNo");

		try
		{
			BP.Port.Emp emp = new Emp(userNo);

			BP.WF.Dev2Interface.Port_Login(emp.No);
			return ".";
		}
		catch (RuntimeException ex)
		{
			return "err@用户[" + userNo + "]登录失败." + ex.getMessage();
		}

	}
	/** 
	 登录.
	 
	 @return 
	*/
	public final String Login_Submit()
	{
		try
		{
			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			BP.Port.Emp emp = new Emp();
			emp.setNo (userNo;
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*如果包含昵称列,就检查昵称是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo (no;
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else if (DBAccess.IsExitsTableCol("Port_Emp", "Name") == true)
				{
					/*如果包含Name列,就检查Name是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE Name=" + SystemConfig.getAppCenterDBVarStr() + "Name";
					ps.Add("Name", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo (no;
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}


				}
				else
				{
					return "err@用户名或者密码错误.";
				}
			}

			if (emp.CheckPass(pass) == false)
			{
				return "err@用户名或者密码错误.";
			}

			//调用登录方法.
			BP.WF.Dev2Interface.Port_Login(emp.No);

			return "";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行登录
	 
	 @return 
	*/
	public final String Login_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("SysName", SystemConfig.SysName);
		ht.put("ServiceTel", SystemConfig.ServiceTel);
		ht.put("CustomerName", SystemConfig.CustomerName);
		if (WebUser.getNo()OfRel == null)
		{
			ht.put("UserNo", "");
			ht.put("UserName", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());

			String name = WebUser.getName();

			if (DataType.IsNullOrEmpty(name) == true)
			{
				ht.put("UserName", WebUser.getNo());
			}
			else
			{
				ht.put("UserName", name);
			}
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

		///#endregion 登录界面.

}