package BP.WF.Port.SubInc;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 岗位
*/
public class Station extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 实现基本的方方法
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Port_Station", "岗位");
		map.Java_SetEnType(EnType.Admin);

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("4"); // 最大级别是7.

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 4, 4, 36);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 2, 50, 250);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "岗位类型", new StationTypes(), true);
		map.AddTBString(StationAttr.OrgNo, null, "OrgNo", true, false, 0, 60, 250);

			//增加隐藏查询条件.
		map.AddHidden(StationAttr.OrgNo,"=", BP.Web.WebUser.FK_Dept);

			//查询条件.
		map.AddSearchAttr(StationAttr.FK_StationType);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}