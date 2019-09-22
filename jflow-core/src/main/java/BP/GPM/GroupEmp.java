package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 权限组人员
*/
public class GroupEmp extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(GroupEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(GroupEmpAttr.FK_Emp, value);
	}
	public final String getFK_Group()
	{
		return this.GetValStringByKey(GroupEmpAttr.FK_Group);
	}
	public final void setFK_Group(String value)
	{
		this.SetValByKey(GroupEmpAttr.FK_Group, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 权限组人员
	*/
	public GroupEmp()
	{
	}
	/** 
	 权限组人员
	 
	 @param mypk
	*/
	public GroupEmp(String no)
	{
		this.Retrieve();
	}
	/** 
	 权限组人员
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}
		Map map = new Map("GPM_GroupEmp");
		map.DepositaryOfEntity = Depositary.None;
		map.DepositaryOfMap = Depositary.Application;
		map.EnDesc = "权限组人员";
		map.EnType = EnType.App;

		map.AddTBStringPK(GroupEmpAttr.FK_Group, null, "权限组", true, false, 0, 50, 20);
		map.AddDDLEntitiesPK(GroupEmpAttr.FK_Emp, null, "人员", new Emps(), true);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}