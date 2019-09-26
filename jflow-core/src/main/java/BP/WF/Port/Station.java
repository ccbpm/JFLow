package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import java.util.*;

/** 
 岗位
*/
public class Station extends EntityNoName
{

		///#region 实现基本的方方法
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

		///#endregion


		///#region 构造方法
	/** 
	 岗位
	*/
	public Station()
	{
	}
	/** 
	 岗位
	 
	 @param _No
	 * @throws Exception 
	*/
	public Station(String _No) throws Exception
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
		map.Java_SetCodeStruct("4"); // 最大级别是7.

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 4, 4, 36);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 2, 50, 250);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "岗位类型", new StationTypes(), true);

		map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 250);

			//查询条件.
		map.AddSearchAttr(StationAttr.FK_StationType);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}