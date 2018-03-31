package BP.GPM;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/** 
 岗位
*/
public class Station extends EntityNoName
{

	public final String getFK_StationType()
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value)
	{
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 岗位
	*/
	public Station()
	{
	}
	/** 
	 岗位
	 @param _No
	*/
	public Station(String _No)
	{
		super(_No);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Station", "岗位");

		map.Java_SetEnType(EnType.Admin);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("2");

		map.AddTBStringPK(EmpAttr.No, null, "编号", true, true, 1, 20, 100);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 100, 200);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "类型", new StationTypes(), true);
		 
		map.AddTBStringDoc(StationAttr.OrgNo, null, "素质要求", true, false, true);
		map.AddSearchAttr(StationAttr.FK_StationType);
		this.set_enMap(map);
		return this.get_enMap();
	}
}