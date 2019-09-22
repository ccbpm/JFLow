package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的工作岗位
 单据的工作岗位有两部分组成.	 
 记录了从一个单据到其他的多个单据.
 也记录了到这个单据的其他的单据.
*/
public class StationCreate extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	单据
	*/
	public final int getFrmID()
	{
		return this.GetValIntByKey(StationCreateAttr.FrmID);
	}
	public final void setFrmID(int value)
	{
		this.SetValByKey(StationCreateAttr.FrmID, value);
	}
	public final String getFK_StationT()
	{
		return this.GetValRefTextByKey(StationCreateAttr.FK_Station);
	}
	/** 
	 工作岗位
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(StationCreateAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		this.SetValByKey(StationCreateAttr.FK_Station, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 单据可创建的工作岗位
	*/
	public StationCreate()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Frm_StationCreate", "单据岗位");

		map.AddTBStringPK(StationCreateAttr.FrmID, null, "表单", true, true, 1, 100, 100);
		map.AddDDLEntitiesPK(StationCreateAttr.FK_Station, null, "可以创建岗位", new BP.GPM.Stations(), true);
		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}