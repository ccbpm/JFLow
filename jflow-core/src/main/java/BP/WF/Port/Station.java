package BP.WF.Port;

import BP.DA.*;
import BP.En.*;

/** 
 岗位
*/
public class Station extends EntityNoName
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

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("2"); // 最大级别是7.
		;

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 2, 2, 2);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 2, 50, 250);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "岗位类型", new StationTypes(), true);
		
		map.AddTBString(StationAttr.OrgNo, "0", "组织", true, false, 0, 100, 100);

			//查询条件.
		map.AddSearchAttr(StationAttr.FK_StationType);

		this.set_enMap(map);
		return this.get_enMap();
	}
}