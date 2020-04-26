package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 方向与工作岗位对应
 节点的工作岗位有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class DirectionStation extends EntityMM
{

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
	节点
	 * @throws Exception 
	*/
	public final int getFK_Direction() throws Exception
	{
		return this.GetValIntByKey(DirectionStationAttr.FK_Direction);
	}
	public final void setFK_Direction(int value) throws Exception
	{
		this.SetValByKey(DirectionStationAttr.FK_Direction, value);
	}
	public final String getFK_StationT() throws Exception
	{
		return this.GetValRefTextByKey(DirectionStationAttr.FK_Station);
	}
	/** 
	 工作岗位
	 * @throws Exception 
	*/
	public final String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(DirectionStationAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		this.SetValByKey(DirectionStationAttr.FK_Station, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 方向与工作岗位对应
	*/
	public DirectionStation()
	{
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

		Map map = new Map("WF_DirectionStation", "节点岗位");

		map.AddTBIntPK(DirectionStationAttr.FK_Direction, 0,"节点", false,false);

		if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne)
		{
			map.AddDDLEntitiesPK(DirectionStationAttr.FK_Station, null, "工作岗位", new BP.Port.Stations(), true);
		}
		else
		{
 // #warning ,这里为了方便用户选择，让分组都统一采用了枚举类型. edit zhoupeng. 2015.04.28. 注意jflow也要修改.
			map.AddDDLEntitiesPK(DirectionStationAttr.FK_Station, null, "工作岗位", new BP.Port.Stations(), true);
		}
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}