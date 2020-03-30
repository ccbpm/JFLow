package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 岗位
*/
public class Station extends EntityNoName
{
		///#region 属性.
	/** 
	 组织编号
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(StationAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(StationAttr.OrgNo, value);
	}
	/** 
	 岗位类型
	*/
	public final String getFK_StationType() throws Exception
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value) throws Exception
	{
		this.SetValByKey(StationAttr.FK_StationType, value);
	}
		///#endregion 属性.

		///#region 实现基本的方方法
	/** 
	 UI界面上的访问控制
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
	*/
	public Station(String _No) throws Exception
	{
		super(_No);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Station", "岗位");
		map.Java_SetEnType(EnType.Admin);

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("4"); // 最大级别是7.

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 1, 50, 36);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 1, 50, 250);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "岗位类型", new StationTypes(), true);
		map.AddTBString(StationAttr.OrgNo, null, "OrgNo", true, true, 0, 60, 100);
		map.AddTBInt(StationAttr.Idx, 0, "顺序", true, false);

			//增加隐藏查询条件.  
		map.AddHidden(StationAttr.OrgNo, "=", BP.Web.WebUser.getOrgNo());

			//查询条件.
		map.AddSearchAttr(StationAttr.FK_StationType, 130);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}