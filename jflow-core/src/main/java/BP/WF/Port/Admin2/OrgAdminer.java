package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.Map;
import BP.WF.Port.Emps;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 组织管理员
*/
public class OrgAdminer extends EntityMM
{
		///#region 属性
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(OrgAdminerAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(OrgAdminerAttr.FK_Emp, value);
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(OrgAdminerAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(OrgAdminerAttr.OrgNo, value);
	}
		///#endregion

		///#region 构造方法
	/** 
	 组织管理员
	*/
	public OrgAdminer()
	{
	}
	/** 
	 组织管理员
	 
	 @param no
	*/
	public OrgAdminer(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 组织管理员
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_OrgAdminer");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("组织管理员");
		map.setEnType(EnType.App);

		map.AddTBStringPK(OrgAdminerAttr.OrgNo, null, "组织", true, false, 0, 50, 20);
		map.AddDDLEntitiesPK(OrgAdminerAttr.FK_Emp, null, "管理员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}