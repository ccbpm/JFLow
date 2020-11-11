package bp.gpm;

import bp.sys.*;

/** 
 页面功能实体
*/
public class HttpHandler extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public HttpHandler()
	{
	}


		///注册用户- 界面 .
	/** 
	 注册用户.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String RegUser_Init() throws Exception
	{
		if (Glo.getIsEnableRegUser() == false)
		{
			return "err@该系统尚未启动注册功能，请通知管理员把全局配置项 IsEnableRegUser 设置为1，没有该项就添加该配置项.";
		}

		//返回部门信息，用与绑定部门.
		Depts ens = new Depts();
		ens.RetrieveAll();
		return ens.ToJson();
	}
	/** 
	 提交
	 
	 @return 
	 * @throws Exception 
	*/
	public final String RegUser_Submit() throws Exception
	{
		bp.gpm.Emp emp = new bp.gpm.Emp();
		emp.setNo(this.GetRequestVal("TB_No"));
		if (emp.RetrieveFromDBSources() == 1)
		{
			return "err@用户名已经存在.";
		}

		//从Request对象中复制数据.
		PubClass.CopyFromRequest(emp);

		//emp.setName( this.GetRequestVal("TB_Name");
		//emp.FK_Dept = this.GetRequestVal("DDL_FK_Dept");
		//emp.Email = this.GetRequestVal("TB_Email");
		//emp.Pass = this.GetRequestVal("TB_PW");
		//emp.Tel = this.GetRequestVal("TB_Tel");
		emp.Insert();

		return "注册成功";
	}


}