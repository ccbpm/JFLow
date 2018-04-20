package BP.GPM;

import BP.DA.Depositary;
import BP.En.EntityNoName;
import BP.En.Map;

/** 
  岗位类型
*/
public class StationType extends EntityNoName
{
	public final String getFK_StationType()
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value)
	{
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

	public final String getFK_StationTypeText()
	{
		return this.GetValRefTextByKey(StationAttr.FK_StationType);
	}

	/** 
	 岗位类型
	*/
	public StationType()
	{
	}
	/** 
	 岗位类型
	 @param _No
	 * @throws Exception 
	*/
	public StationType(String _No) throws Exception
	{
		super(_No);
	}

	/** 
	 岗位类型Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_StationType", "岗位类型");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(StationTypeAttr.No, null, "编号", true, true, 2, 2, 2);
		map.AddTBString(StationTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(StationTypeAttr.Idx, 0, "顺序", true, false);
		this.set_enMap(map);
		return this.get_enMap();
	}
}