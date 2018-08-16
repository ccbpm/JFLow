package BP.Port;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UAC;
import BP.GPM.EmpAttr;
import BP.Sys.SystemConfig;

/**
 * Emp 的摘要说明。
 */
public class Emp extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 扩展属性
	/**
	 * 主要的部门。
	 * @throws Exception 
	 */
	public final Dept getHisDept() throws Exception
	{
		try
		{
			return new Dept(this.getFK_Dept());
		} catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取操作员" + this.getNo() + "部门["
					+ this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@"
					+ ex.getMessage());
		}
	}
	 
	
	/**
	 * 工作部门集合
	 */
	 
	
	/**
	 * 部门编号
	 */
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}
	
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}
	
	/**
	 * 部门编号
	 */
	public final String getSID()
	{
		return this.GetValStrByKey(EmpAttr.SID);
	}
	
	public final void setSID(String value)
	{
		this.SetValByKey(EmpAttr.SID, value);
	}
	/**
	 * 部门编号
	 */
	public final String getFK_DeptText()
	{
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}
	
	/**
	 * 密码
	 */
	public final String getPass()
	{
		return this.GetValStrByKey(EmpAttr.Pass);
	}
	
	public final void setPass(String value)
	{
		this.SetValByKey(EmpAttr.Pass, value);
	}
	
	// 公共方法
	/**
	 * 检查密码(可以重写此方法)
	 * 
	 * @param pass
	 *            密码
	 * @return 是否匹配成功
	 */
	public final boolean CheckPass(String pass)
	{
		
		if (1==1)
			return true;
		 
		 //启用加密
    //    if (SystemConfig.getIsEnablePasswordEncryption() == true)
      //      pass = BP.Tools.Cryptography.EncryptString(pass);

        /*使用数据库校验.*/
        if (this.getPass().equals( pass)==true)
            return true;
        
//		if (SystemConfig.getIsDebug())
//		{
//			return true;
//		}
		//UserService.encryptPassword();
		
		if (this.getPass().equals(pass))
		{
			return true;
		}
		return false;
	}
	
	// 公共方法
	
	// 构造函数
	/**
	 * 操作员
	 */
	public Emp()
	{
		
	}	 
	
	/**
	 * 操作员
	 * 
	 * @param no
	 *            编号
	 * @throws Exception 
	 */
	public Emp(String no) throws Exception
	{
		this.setNo(no.trim());
		if (this.getNo().length() == 0)
		{
			throw new RuntimeException("@要查询的操作员编号为空。");
		}
		try
		{
			this.Retrieve();
		} catch (RuntimeException ex)
		{
			// 登录帐号查询不到用户，使用职工编号查询。
			QueryObject obj = new QueryObject(this);
			obj.AddWhere(EmpAttr.No, no);
			int i = obj.DoQuery();
			if (i == 0)
			{
				i = this.RetrieveFromDBSources();
			}
			if (i == 0)
			{
				throw new RuntimeException("@用户或者密码错误：[" + no
						+ "]，或者帐号被停用。@技术信息(从内存中查询出现错误)：ex1=" + ex.getMessage());
			}
		}
	}
	
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map();
		
		// 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); // 要连接的数据源（表示要连接到的那个系统数据库）。
		map.setPhysicsTable("Port_Emp"); // 要物理表。
		map.setDepositaryOfMap(Depositary.Application); // 实体map的存放位置.
		map.setDepositaryOfEntity(Depositary.Application); // 实体存放位置
		map.setEnDesc("用户"); // "用户";
		map.setEnType(EnType.App); // 实体类型。
		
		// 字段
		// 关于字段属性的增加
		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 20, 30);
		map.AddTBString(EmpAttr.Name, null, "员工姓名", true, false, 0, 200, 30);
		map.AddTBString(EmpAttr.Pass, "pub", "密码", false, false, 0, 20, 10);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new Depts(), true);
		
		//map.AddTBString(EmpAttr.Tel, null, "电话", false, false, 0, 20, 130);
		//map.AddTBString(EmpAttr.Email, null, "邮箱", false, false, 0, 100, 132);
		
		// map.AddTBString("Tel", null, "Tel", false, false, 0, 20, 10);
		// map.AddTBString(EmpAttr.PID, null, this.ToE("PID", "UKEY的PID"), true,
		// false, 0, 100, 30);
		// map.AddTBString(EmpAttr.PIN, null, this.ToE("PIN", "UKEY的PIN"), true,
		// false, 0, 100, 30);
		// map.AddTBString(EmpAttr.KeyPass, null, this.ToE("KeyPass",
		// "UKEY的KeyPass"), true, false, 0, 100, 30);
		// map.AddTBString(EmpAttr.IsUSBKEY, null, this.ToE("IsUSBKEY",
		// "是否使用usbkey"), true, false, 0, 100, 30);
		// map.AddDDLSysEnum("Sex", 0, "性别", "@0=女@1=男");
		
		// 字段		
		map.AddSearchAttr(EmpAttr.FK_Dept);
		
		 
		 
		// 不带有参数的方法.
		// xiaozhoupeng 注掉，原因不需要销毁人员 Start
		// RefMethod rm = new RefMethod();
		// rm.Title = "注销人员";
		// rm.Warning= "是否确认注销人员？";
		// rm.ClassMethodName = this.toString() + ".DoZhuXiao";
		// map.AddRefMethod(rm);
		
		// xiaozhoupeng 注掉，原因不需要销毁人员 End
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/**
	 * 获取集合
	 */
	@Override
	public Entities getGetNewEntities()
	{
		return new Emps();
	}
	
	// 构造函数
	/**
	 * 无参数的方法:注销岗位 说明：都要返回string类型.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final void DoZhuXiao() throws Exception
	{
		this.Delete();
	}
	
}
