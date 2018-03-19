package BP.Sys;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntityMyPK;
import BP.En.Map;

/**
 * 用户日志
 */
public class UserLog extends EntityMyPK
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 用户日志信息键值列表
	// 基本属性
	public final String getIP()
	{
		return this.GetValStringByKey(UserLogAttr.IP);
	}
	
	public final void setIP(String value)
	{
		this.SetValByKey(UserLogAttr.IP, value);
	}
	
	/**
	 * 日志标记键
	 */
	public final String getLogFlag()
	{
		return this.GetValStringByKey(UserLogAttr.LogFlag);
	}
	
	public final void setLogFlag(String value)
	{
		this.SetValByKey(UserLogAttr.LogFlag, value);
	}
	
	/**
	 * FK_Emp
	 */
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(UserLogAttr.FK_Emp);
	}
	
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(UserLogAttr.FK_Emp, value);
	}
	
	public final String getRDT()
	{
		return this.GetValStringByKey(UserLogAttr.RDT);
	}
	
	public final void setRDT(String value)
	{
		this.SetValByKey(UserLogAttr.RDT, value);
	}
	
	public final String getDocs()
	{
		return this.GetValStringByKey(UserLogAttr.Docs);
	}
	
	public final void setDocs(String value)
	{
		this.SetValByKey(UserLogAttr.Docs, value);
	}
	
	// 构造方法
	/**
	 * 用户日志
	 */
	public UserLog()
	{
	}
	
	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_UserLogT");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		
		map.setEnDesc("用户日志");
		map.setEnType(EnType.Sys);
		map.AddMyPK();
		map.AddTBString(UserLogAttr.FK_Emp, null, "用户", false, false, 0, 30, 20);
		map.AddTBString(UserLogAttr.IP, null, "IP", true, false, 0, 200, 20);
		map.AddTBString(UserLogAttr.LogFlag, null, "Flag", true, false, 0, 300,
				20);
		map.AddTBString(UserLogAttr.Docs, null, "Docs", true, false, 0, 300, 20);
		map.AddTBString(UserLogAttr.RDT, null, "记录日期", true, false, 0, 20, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	// 重写
	@Override
	public Entities getGetNewEntities()
	{
		return new UserLogs();
	}
	// 重写
}