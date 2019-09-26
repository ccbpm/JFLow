package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 岗位
*/
public class Station extends EntityNoName
{

		///#region 属性
	public final String getFK_StationType() throws Exception
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value) throws Exception
	{
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

		///#endregion


		///#region 实现基本的方方法
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
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetDepositaryOfEntity(Depositary.None);

			// map.Java_SetCodeStruct("4");
			// map.IsAutoGenerNo = true;

		map.AddTBStringPK(StationAttr.No, null, "编号", true, false, 1, 50, 200);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 0, 100, 200);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "类型", new StationTypes(), true);

			//map.AddTBStringDoc(StationAttr.DutyReq, null, "职责要求", true, false, true);
			//map.AddTBStringDoc(StationAttr.Makings, null, "素质要求", true, false, true);

		map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 250);
		map.AddSearchAttr(StationAttr.FK_StationType);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}