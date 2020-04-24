package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import java.util.*;

/** 
 岗位
*/
public class Station extends EntityNoName
{
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	public final String getName() throws Exception
	{
		return this.GetValStrByKey("Name");
	}

	/** 
	 岗位
	*/
	public Station()
	{
	}
	/** 
	 岗位
	 
	 @param no 岗位编号
	 * @throws Exception 
	*/
	public Station(String no) throws Exception
	{
		this.setNo(no.trim());
		if (this.getNo().length() == 0)
		{
			throw new RuntimeException("@要查询的岗位编号为空。");
		}

		this.Retrieve();
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
		map.Java_SetDepositaryOfEntity(Depositary.Application);

		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 4, 4, 4);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 100, 100);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "类型", new StationTypes(), true);
		map.AddTBString(StationAttr.OrgNo, null, "隶属组织编号", true, false, 0, 100, 100);

		this.set_enMap(map);
		return this.get_enMap();
	}
	
}