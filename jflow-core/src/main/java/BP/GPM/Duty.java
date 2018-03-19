package BP.GPM;

import BP.DA.Depositary;
import BP.En.EntityNoName;
import BP.En.Map;

/** 
  职务
*/
public class Duty extends EntityNoName
{

	/** 
	 职务
	*/
	public Duty()
	{
	}
	/** 
	 职务
	 @param _No
	*/
	public Duty(String _No)
	{
		super(_No);
	}


	/** 
	 职务Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_Duty", "职务");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(DutyAttr.No, null, "编号", true, true, 2, 2, 2);
		map.AddTBString(DutyAttr.Name, null, "名称", true, false, 1, 50, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}