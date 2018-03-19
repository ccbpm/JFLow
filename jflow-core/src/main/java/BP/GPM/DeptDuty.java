package BP.GPM;

import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;

/** 
 部门职务 的摘要说明。
*/
public class DeptDuty extends Entity
{
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;

	}
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(DeptDutyAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(DeptDutyAttr.FK_Dept, value);
	}
	public final String getFK_DutyT()
	{
		return this.GetValRefTextByKey(DeptDutyAttr.FK_Duty);
	}
	/** 
	职务
	*/
	public final String getFK_Duty()
	{
		return this.GetValStringByKey(DeptDutyAttr.FK_Duty);
	}
	public final void setFK_Duty(String value)
	{
		SetValByKey(DeptDutyAttr.FK_Duty, value);
	}

	/** 
	 部门职务
	*/
	public DeptDuty()
	{
	}
	/** 
	 工作人员职务对应
	 @param _empoid 部门
	 @param wsNo 职务编号 	
	*/
	public DeptDuty(String _empoid, String wsNo)
	{
		this.setFK_Dept(_empoid);
		this.setFK_Duty(wsNo);
		if (this.Retrieve() == 0)
		{
			this.Insert();
		}
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

		Map map = new Map("Port_DeptDuty");
		map.setEnDesc("部门职务");
		map.Java_SetEnType(EnType.Dot2Dot); //实体类型，admin 系统管理员表，PowerAble 权限管理表,也是用户表,你要想把它加入权限管理里面请在这里设置。。

		map.AddTBStringPK(DeptDutyAttr.FK_Dept, null, "部门", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(DeptDutyAttr.FK_Duty, null, "职务", new Dutys(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
}