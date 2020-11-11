package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.data.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_AppClassic extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_AppClassic()
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


	/** 
	 初始化Home
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Home_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		//系统名称.
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("CustomerName", SystemConfig.getCustomerName());

		ht.put("Todolist_EmpWorks", bp.wf.Dev2Interface.getTodolistEmpWorks());
		ht.put("Todolist_Runing", bp.wf.Dev2Interface.getTodolistRuning());
		ht.put("Todolist_Sharing", bp.wf.Dev2Interface.getTodolistSharing());
		ht.put("Todolist_CCWorks", bp.wf.Dev2Interface.getTodolistCCWorks());
		ht.put("Todolist_Apply", bp.wf.Dev2Interface.getTodolistApply()); //申请下来的任务个数.
		ht.put("Todolist_Draft", bp.wf.Dev2Interface.getTodolistDraft()); //草稿数量.
		ht.put("Todolist_Complete", bp.wf.Dev2Interface.getTodolistComplete()); //完成数量.
		ht.put("UserDeptName", WebUser.getFK_DeptName());

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

		return bp.tools.Json.ToJsonEntityModel(ht);
	}
	public final String Index_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("Todolist_Runing", bp.wf.Dev2Interface.getTodolistRuning()); //运行中.
		ht.put("Todolist_EmpWorks", bp.wf.Dev2Interface.getTodolistEmpWorks()); //待办
		ht.put("Todolist_CCWorks", bp.wf.Dev2Interface.getTodolistCCWorks()); //抄送.

		//本周.
		ht.put("TodayNum", bp.wf.Dev2Interface.getTodolistCCWorks()); //抄送.

		return bp.tools.Json.ToJsonEntityModel(ht);
	}


		///登录界面.
	public final String Portal_Login() throws Exception
	{
		String userNo = this.GetRequestVal("UserNo");

		try
		{
			bp.port.Emp emp = new Emp(userNo);

			bp.wf.Dev2Interface.Port_Login(emp.getNo());
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
	 * @throws Exception 
	*/
	public final String Login_Submit() throws Exception
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

			bp.port.Emp emp = new Emp();
			emp.setNo(userNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*如果包含昵称列,就检查昵称是否存在.*/
					Paras ps = new Paras();
					ps.SQL="SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else if (DBAccess.IsExitsTableCol("Port_Emp", "Tel") == true)
				{
					/*如果包含Name列,就检查Name是否存在.*/
					Paras ps = new Paras();
					ps.SQL="SELECT No FROM Port_Emp WHERE Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
					ps.Add("Tel", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
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
			bp.wf.Dev2Interface.Port_Login(emp.getNo());

			if (DBAccess.IsView("Port_Emp") == false)
			{
				String sid = DBAccess.GenerGUID();
				DBAccess.RunSQL("UPDATE Port_Emp SET SID='" + sid + "' WHERE No='" + emp.getNo() + "'");
				WebUser.setSID(sid);
				//emp.SID = sid;
			}

			return "登陆成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}


	/** 
	 执行登录
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Login_Init() throws Exception
	{
		String doType = GetRequestVal("LoginType");
		if (DataType.IsNullOrEmpty(doType) == false && doType.equals("Out") == true)
		{
			//清空cookie
			WebUser.Exit();
		}


		if (this.getDoWhat() != null && this.getDoWhat().equals("Login") == true)
		{
			//调用登录方法.
			bp.wf.Dev2Interface.Port_Login(this.getUserNo(), this.getSID());
			return "url@Home.htm?UserNo=" + this.getUserNo();
			//this.Login_Submit();
			//return;
		}

		Hashtable ht = new Hashtable();
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("ServiceTel", SystemConfig.getServiceTel());
		ht.put("CustomerName", SystemConfig.getCustomerName());
		if (WebUser.getNoOfRel() == null)
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

		return bp.tools.Json.ToJsonEntityModel(ht);
	}

		/// 登录界面.

}