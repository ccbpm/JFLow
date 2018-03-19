package BP.Sys;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntityNoName;
import BP.En.Map;

/**
 * 域
 */
public class Domain extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 基本属性
	public final String getDocs()
	{
		return this.GetValStringByKey(DomainAttr.DBLink);
	}
	
	public final void setDocs(String value)
	{
		this.SetValByKey(DomainAttr.DBLink, value);
	}
	
	// 构造方法
	/**
	 * 域
	 */
	public Domain()
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
		Map map = new Map("Sys_Domain");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		
		map.setEnDesc("域");
		map.setEnType(EnType.Sys);
		map.AddTBStringPK(DomainAttr.No, null, "编号", false, false, 0, 30, 20);
		map.AddTBString(DomainAttr.Name, null, "Name", false, false, 0, 30, 20);
		map.AddTBString(DomainAttr.DBLink, null, "DBLink", false, false, 0,
				130, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	// 重写
	@Override
	public Entities getGetNewEntities()
	{
		return new Domains();
	}
	// 重写
}