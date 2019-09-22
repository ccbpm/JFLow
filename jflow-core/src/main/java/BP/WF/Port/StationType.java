package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
  岗位类型
*/
public class StationType extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 岗位类型
	*/
	public StationType()
	{
	}
	/** 
	 岗位类型
	 
	 @param _No
	*/
	public StationType(String _No)
	{
		super(_No);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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
		Map map = new Map("Port_StationType");
		map.EnDesc = "岗位类型";
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(StationTypeAttr.No, null, "编号", true, true, 2, 2, 2);
		map.AddTBString(StationTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}