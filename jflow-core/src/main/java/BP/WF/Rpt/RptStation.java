package BP.WF.Rpt;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 RptStation 的摘要说明。
*/
public class RptStation extends Entity
{
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 报表ID
	*/
	public final String getFK_Rpt()
	{
		return this.GetValStringByKey(RptStationAttr.FK_Rpt);
	}
	public final void setFK_Rpt(String value)
	{
		SetValByKey(RptStationAttr.FK_Rpt, value);
	}
	public final String getFK_StationT()
	{
		return this.GetValRefTextByKey(RptStationAttr.FK_Station);
	}
	/** 
	岗位
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(RptStationAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		SetValByKey(RptStationAttr.FK_Station, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 报表岗位
	*/
	public RptStation()
	{
	}
	/** 
	 报表岗位对应
	 
	 @param _empoid 报表ID
	 @param wsNo 岗位编号 	
	*/
	public RptStation(String _empoid, String wsNo)
	{
		this.setFK_Rpt(_empoid);
		this.setFK_Station(wsNo);
		if (this.Retrieve() == 0)
		{
			this.Insert();
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_RptStation", "报表岗位对应信息");

		map.Java_SetEnType(EnType.Dot2Dot);

		map.AddTBStringPK(RptStationAttr.FK_Rpt, null, "报表", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(RptStationAttr.FK_Station, null, "岗位", new Stations(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}