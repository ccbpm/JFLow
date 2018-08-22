package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.OSModel;

/** 
 操作员 的摘要说明。
 
*/
public class GPMEmp extends EntityNoName
{
		///#region 扩展属性
	/** 
	 该人员是否被禁用.
	 
	*/
	public final boolean getIsEnable()
	{
		if (this.getNo().equals("admin"))
		{
			return true;
		}

		 
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
		

		 

		return true;
	}
	/** 
	 拼音
	 
	*/
	public final String getPinYin()
	{
		return this.GetValStrByKey(EmpAttr.PinYin);
	}
	public final void setPinYin(String value)
	{
		this.SetValByKey(EmpAttr.PinYin, value);
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
	 签字类型
	 
	*/
	public final int getSignType()
	{
		return this.GetValIntByKey(EmpAttr.SignType);
	}
	public final void setSignType(int value)
	{
		this.SetValByKey(EmpAttr.SignType, value);
	}

		///#region 公共方法
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

		///#region 构造函数
	/** 
	 操作员
	 
	*/
	public GPMEmp()
	{
	}
	/** 
	 操作员
	 
	 @param no 编号
	 * @throws Exception 
	*/
	public GPMEmp(String no) throws Exception
	{
		super(no);
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
			///#region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //要连接的数据源（表示要连接到的那个系统数据库）。
		map.setPhysicsTable("Port_Emp"); // 要物理表。
		map.Java_SetDepositaryOfMap(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体存放位置
		map.setEnDesc("用户"); // "用户"; // 实体的描述.
		map.Java_SetEnType(EnType.App); //实体类型。

			///#region 字段
			//关于字段属性的增加 
		map.AddTBStringPK(EmpAttr.No, null, "登陆账号", true, false, 1, 50, 50);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 30);
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 100, 10);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "主要部门", new BP.Port.Depts(), true);

		map.AddTBString(EmpAttr.SID, null, "安全校验码", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Tel, null, "电话", true, false, 0, 20, 130);
		map.AddTBString(EmpAttr.Email, null, "邮箱", true, false, 0, 100, 132,true);
		map.AddTBString(EmpAttr.PinYin, null, "拼音", true, false, 0, 500, 132, true);

			// 0=不签名 1=图片签名, 2=电子签名.
		map.AddDDLSysEnum(EmpAttr.SignType, 0, "签字类型", true,true, EmpAttr.SignType, "@0=不签名@1=图片签名@2=电子签名");

		map.AddTBInt(EmpAttr.Idx, 0, "序号", true, false);

			///#endregion 字段

		map.AddSearchAttr(EmpAttr.SignType);

			//节点绑定部门. 节点绑定部门.
		map.getAttrsOfOneVSM().AddBranches(new EmpMenus(), new BP.GPM.Menus(), BP.GPM.EmpMenuAttr.FK_Emp, BP.GPM.EmpMenuAttr.FK_Menu, "人员菜单", EmpAttr.Name, EmpAttr.No, "0");

		RefMethod rm = new RefMethod();
		rm.Title = "设置图片签名";
		rm.ClassMethodName = this.toString() + ".DoSinger";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "部门岗位";
		rm.ClassMethodName = this.toString() + ".DoEmpDepts";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

			//节点绑定部门. 节点绑定部门.
		map.getAttrsOfOneVSM().AddBranches(new DeptEmps(), new BP.GPM.Depts(), BP.GPM.DeptEmpAttr.FK_Emp, BP.GPM.DeptEmpAttr.FK_Dept, "部门维护", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");


		this.set_enMap(map);
		return this.get_enMap();
	}

	public final String DoEmpDepts()
	{
		return "../../../GPM/EmpDepts.htm?FK_Emp=" + this.getNo();
	}

	public final String DoSinger()
	{
		return "../../../GPM/Siganture.htm?EmpNo=" + this.getNo();
	}

	public static GPMEmp GenerData(GPMEmp en) throws Exception
	{
		//增加拼音，以方便查找.
		String pinyinQP = BP.DA.DataType.ParseStringToPinyin(en.getName()).toLowerCase();
		String pinyinJX = BP.DA.DataType.ParseStringToPinyinJianXie(en.getName()).toLowerCase();
		en.setPinYin("," + pinyinQP + "," + pinyinJX + ",");

		//处理岗位信息.
		DeptEmpStations des = new DeptEmpStations();
		des.Retrieve(DeptEmpStationAttr.FK_Emp, en.getNo());

		String depts = "";
		String stas = "";

		for (DeptEmpStation item : des.ToJavaList())
		{
			BP.GPM.Dept dept = new BP.GPM.Dept();
			dept.setNo(item.getFK_Dept());
			if (dept.RetrieveFromDBSources() == 0)
			{
				item.Delete();
				continue;
			}

			//给拼音重新定义值,让其加上部门的信息.
			en.setPinYin(en.getPinYin() + pinyinJX + "/" + BP.DA.DataType.ParseStringToPinyinJianXie(dept.getName()).toLowerCase() + ",");

			BP.Port.Station sta = new BP.Port.Station();
			sta.setNo(item.getFK_Station());
			if (sta.RetrieveFromDBSources() == 0)
			{
				item.Delete();
				continue;
			}

			stas += "@" + dept.getNameOfPath() + "|" + sta.getName();

			if (depts.contains("@" + dept.getNameOfPath()) == false)
			{
				depts += "@" + dept.getNameOfPath();
			}
		}
		return en;
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		//处理其他的数据.
		BP.GPM.GPMEmp.GenerData(this);
		return super.beforeUpdateInsertAction();
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造函数
}