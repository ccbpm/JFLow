package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import java.util.*;

/** 
 岗位
*/
public class Station extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 实现基本的方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	public final String getName()
	{
		return this.GetValStrByKey("Name");
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
	 
	 @param no 岗位编号
	*/
	public Station(String no)
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
		map.AddTBString(StationAttr.OrgNo, null, "隶属组织编号", true, false, 0, 100, 100);


			////如果是一人一部门多岗位,就启动这个映射.
			//if (BP.Sys.SystemConfig.OSModel == OSModel.OneOne)
			//{
			//    //岗位人员.
			//    map.AttrsOfOneVSM.Add(new EmpStations(), new Emps(), EmpStationAttr.FK_Station, EmpStationAttr.FK_Emp,
			//      DeptAttr.Name, DeptAttr.No, "人员");
			//}

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写查询. 2015.09.31 为适应ws的查询.
	/** 
	 查询
	 
	 @return 
	*/
	@Override
	public int Retrieve()
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = v.GetStation(this.getNo());
			if (dt.Rows.Count == 0)
			{
				throw new RuntimeException("@编号为(" + this.getNo() + ")的岗位不存在。");
			}
			this.getRow().LoadDataTable(dt, dt.Rows[0]);
			return 1;
		}
		else
		{
			return super.Retrieve();
		}
	}
	/** 
	 查询.
	 
	 @return 
	*/
	@Override
	public int RetrieveFromDBSources()
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = v.GetStation(this.getNo());
			if (dt.Rows.Count == 0)
			{
				return 0;
			}
			this.getRow().LoadDataTable(dt, dt.Rows[0]);
			return 1;
		}
		else
		{
			return super.RetrieveFromDBSources();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}