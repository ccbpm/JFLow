package BP.GPM;

import BP.DA.DBAccess;
import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.UAC;
import BP.Tools.StringHelper;

/** 
 操作员 的摘要说明。
 
*/
public class Emp extends EntityNoName
{
	/** 
	 该人员是否被禁用.
	*/
	public final boolean getIsEnable()
	{
		if("admin".equals(getNo()))
		{
			return true;
		}

		if (BP.Sys.SystemConfig.getOSModel() == BP.Sys.OSModel.OneMore)
		{
			String sql = "SELECT COUNT(FK_Emp) FROM Port_DeptEmpStation WHERE FK_Emp='" + this.getNo() + "'";
			if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
			{
				return false;
			}

			sql = "SELECT COUNT(FK_Emp) FROM Port_DeptEmp WHERE FK_Emp='" + this.getNo() + "'";
			if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
			{
				return false;
			}
		}

		if (BP.Sys.SystemConfig.getOSModel() == BP.Sys.OSModel.OneOne)
		{
			String sql = "SELECT COUNT(FK_Emp) FROM Port_EmpStation WHERE FK_Emp='" + this.getNo() + "'";
			if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
			{
				return false;
			}

			sql = "SELECT COUNT(FK_Emp) FROM Port_DeptEmp WHERE FK_Emp='" + this.getNo() + "'";
			if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
			{
				return false;
			}
		}

		return true;
	}
	/** 
	 员工编号
	 
	*/
	public final String getEmpNo()
	{
		return this.GetValStrByKey(EmpAttr.EmpNo);
	}
	public final void setEmpNo(String value)
	{
		this.SetValByKey(EmpAttr.EmpNo, value);
	}
	/** 
	 主要的部门。
	 * @throws Exception 
	 
	*/
	public final Dept getHisDept() throws Exception
	{
		try
		{
			return new Dept(this.getFK_Dept());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取操作员" + this.getNo() + "部门[" + this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@" + ex.getMessage());
		}
	}
	/** 
	 部门
	 
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}
	 
	 
	public final String getFK_DeptText()
	{
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}
	public final String getTel()
	{
		return this.GetValStrByKey(EmpAttr.Tel);
	}
	public final void setTel(String value)
	{
		this.SetValByKey(EmpAttr.Tel, value);
	}
	public final String getEmail()
	{
		return this.GetValStrByKey(EmpAttr.Email);
	}
	public final void setEmail(String value)
	{
		this.SetValByKey(EmpAttr.Email, value);
	}

	public final String getLeader()
	{
		return this.GetValStrByKey(DeptEmpAttr.Leader);
	}
	public final void setLeader(String value)
	{
		this.SetValByKey(DeptEmpAttr.Leader, value);
	}
	/** 
	 密码
	 
	*/
	public final String getPass()
	{
		return this.GetValStrByKey(EmpAttr.Pass);
	}
	public final void setPass(String value)
	{
		this.SetValByKey(EmpAttr.Pass, value);
	}
	/** 
	 顺序号
	 
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(EmpAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(EmpAttr.Idx, value);
	}
	/** 
	 检查密码(可以重写此方法)
	 
	 @param pass 密码
	 @return 是否匹配成功
	*/
	public final boolean CheckPass(String pass)
	{
		if (this.getPass().equals(pass))
		{
			return true;
		}
		return false;
	}
	/** 
	 操作员
	 
	*/
	public Emp()
	{
	}
	/** 
	 操作员
	 @param no 编号
	 * @throws Exception 
	*/
	public Emp(String no) throws Exception
	{
		this.setNo(no.trim());
		if (getNo().length() == 0)
		{
			throw new RuntimeException("@要查询的操作员编号为空。");
		}
		try
		{
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
			int i = this.RetrieveFromDBSources();
			if (i == 0)
			{
				throw new RuntimeException("@用户或者密码错误：[" + no + "]，或者帐号被停用。@技术信息(从内存中查询出现错误)：ex1=" + ex.getMessage());
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
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map();


		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //要连接的数据源（表示要连接到的那个系统数据库）。
		map.setPhysicsTable("Port_Emp"); // 要物理表。
		map.Java_SetDepositaryOfMap(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体存放位置
		map.setEnDesc("用户"); // "用户"; // 实体的描述.
		map.Java_SetEnType(EnType.App); //实体类型。

			//关于字段属性的增加 
		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 50, 50);
		 
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 30);
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 100, 10);

			//map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new Port.Depts(), true);

		map.AddTBString(EmpAttr.FK_Dept, null, "当前部门", false, false, 0, 50, 50);
		 

		map.AddTBString(EmpAttr.SID, null, "安全校验码", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Tel, null, "电话", true, false, 0, 20, 130);
		map.AddTBString(EmpAttr.Email, null, "邮箱", true, false, 0, 100, 132);
 
		map.AddTBInt(EmpAttr.Idx, 0, "序号", true, false);
	//	 map.AddSearchAttr(EmpAttr.FK_Dept);

			////#region 增加点对多属性
			////他的部门权限
			//map.AttrsOfOneVSM.Add(new EmpDepts(), new Depts(), EmpDeptAttr.FK_Emp, EmpDeptAttr.FK_Dept,
			//    DeptAttr.Name, DeptAttr.No, "部门权限");

			//map.AttrsOfOneVSM.Add(new EmpStations(), new Stations(), EmpStationAttr.FK_Emp, EmpStationAttr.FK_Station,
			//    DeptAttr.Name, DeptAttr.No, "岗位权限");
			////#endregion

		RefMethod rm = new RefMethod();
		rm.Title = "修改密码";
		rm.ClassMethodName = this.toString() + ".DoResetpassword";
		rm.getHisAttrs().AddTBString("pass1", null, "输入密码", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("pass2", null, "再次输入", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 向上移动
	 
	*/
	public final void DoUp()
	{
		this.DoOrderUp(EmpAttr.FK_Dept, this.getFK_Dept(), EmpAttr.Idx);
	}
	/** 
	 向下移动
	 
	*/
	public final void DoDown()
	{
		this.DoOrderDown(EmpAttr.FK_Dept, this.getFK_Dept(), EmpAttr.Idx);
	}

	public final String DoResetpassword(String pass1, String pass2) throws Exception
	{
		if (pass1.equals(pass2) == false)
		{
			return "两次密码不一致";
		}

		this.setPass(pass1);
		this.Update();
		return "密码设置成功";
	}
	/** 
	 获取集合
	 
	*/
	@Override
	public Entities getGetNewEntities()
	{
		return new Emps();
	}

}