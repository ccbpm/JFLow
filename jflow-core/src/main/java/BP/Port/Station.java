package BP.Port;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;
import BP.GPM.EmpAttr;

/**
 * 岗位
 */
public class Station extends EntityNoName
{
	 
	
	// 实现基本的方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	  
	public String getName()
	{
		return this.GetValStrByKey("Name");
	}
	 
	 
	// 构造方法
	/**
	 * 岗位
	 */
	public Station()
	{
	}
	
	/**
	 * 岗位
	 * 
	 * @param _No
	 */
	public Station(String _No)
	{
		super(_No);
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
		
		Map map = new Map("Port_Station");
		map.setEnDesc("岗位"); // "岗位";
		map.setEnType(EnType.Admin);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.Application);
		map.setCodeStruct("2222222"); // 最大级别是 7.
		
		map.AddTBStringPK(StationAttr.No, null, "编号", true, false, 1, 20, 100);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 0, 100, 100);
		map.AddTBString(StationAttr.FK_StationType, null, "岗位类型", true, false, 0, 100, 100);
		  
		map.AddTBString(DeptAttr.OrgNo, null, "组织编号", false, false, 0, 100, 30);
		map.AddTBInt(DeptAttr.Idx, 0, "顺序号", false, false);
		
		   
		this.set_enMap(map);
		return this.get_enMap();
	}
	 
}