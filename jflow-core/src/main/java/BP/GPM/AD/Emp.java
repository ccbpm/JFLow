package BP.GPM.AD;

import BP.DA.*;
import BP.En.*;
import BP.GPM.*;
import java.util.*;

/** 
 操作员 的摘要说明。
*/
public class Emp extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性
	/** 
	 该人员是否被禁用.
	*/
	public final boolean getIsEnable()
	{
		if (this.No.equals("admin"))
		{
			return true;
		}

		String sql = "SELECT COUNT(FK_Emp) FROM Port_DeptEmpStation WHERE FK_Emp=' " + this.getNo()+ " '";
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			return false;
		}

		sql = "SELECT COUNT(FK_Emp) FROM Port_DeptEmp WHERE FK_Emp=' " + this.getNo()+ " '";
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
	*/
	public final Dept getHisDept()
	{
		try
		{
			return new Dept(this.getFK_Dept());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取操作员 " + this.getNo()+ " 部门[" + this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@" + ex.getMessage());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公共方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 操作员
	*/
	public Emp()
	{
	}
	/** 
	 操作员
	 
	 @param no 编号
	*/
	public Emp(String no)
	{
		this.No = no.trim();
		this.Retrieve();
	}
	@Override
	public UAC getHisUAC()
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性
		map.EnDBUrl = new DBUrl(DBUrlType.AppCenterDSN); //要连接的数据源（表示要连接到的那个系统数据库）。
		map.PhysicsTable = "Port_Emp"; // 要物理表。
		map.Java_SetDepositaryOfMap(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfEntity(Depositary.None); //实体存放位置
		map.EnDesc = "用户"; // "用户"; // 实体的描述.
		map.Java_SetEnType(EnType.App); //实体类型。
		map.EnType = EnType.App;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段

			/*关于字段属性的增加 */
		map.AddTBStringPK(EmpAttr.No, null, "登陆账号", true, false, 1, 50, 90);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 130);
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 100, 10);

		map.AddDDLEntities(EmpAttr.FK_Dept, null, "主要部门", new BP.Port.Depts(), true);

		map.AddTBString(EmpAttr.SID, null, "安全校验码", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Tel, null, "电话", true, false, 0, 20, 130);
		map.AddTBString(EmpAttr.Email, null, "邮箱", true, false, 0, 100, 132, true);
		map.AddTBString(EmpAttr.PinYin, null, "拼音", true, false, 0, 500, 132, true);


		map.AddTBString(EmpAttr.Manager, null, "Manager", true, false, 0, 500, 132, true);
		map.AddTBString("mobile", null, "mobile", true, false, 0, 500, 132, true);


		map.AddTBInt(EmpAttr.Idx, 0, "序号", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造函数
}