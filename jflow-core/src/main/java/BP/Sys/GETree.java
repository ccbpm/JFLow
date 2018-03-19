package BP.Sys;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/**
 * 树结构实体
 */
public class GETree extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	@Override
	public String toString()
	{
		return this.PhysicsTable;
	}
	
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	public GETree()
	{
		
	}
	
	/**
	 * 编号
	 * 
	 * @param no
	 *            编号
	 */
	public GETree(String no)
	{
		super(no);
		
	}
	
	public GETree(String sftable, String tableDesc)
	{
		this.PhysicsTable = sftable;
		this.Desc = tableDesc;
	}
	
	@Override
	public Map getEnMap()
	{
		// if (this.get_enMap() != null) return this.get_enMap();
		Map map = new Map(this.PhysicsTable);
		map.setEnDesc(this.Desc);
		map.setIsAutoGenerNo(true);
		
		map.setDepositaryOfEntity(Depositary.Application);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnType(EnType.App);
		map.setIsAutoGenerNo(true);
		
		map.AddTBStringPK(GETreeAttr.No, null, "编号", true, true, 1, 30, 3);
		map.AddTBString(GETreeAttr.Name, null, "名称", true, false, 1, 60, 500);
		return map;
		// this.set_enMap(map);
		// return this.get_enMap();
	}
	
	public String PhysicsTable = null;
	public String Desc = null;
}