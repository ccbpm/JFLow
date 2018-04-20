package BP.Port;

import java.util.ArrayList;
import java.util.List;

import BP.DA.Cash;
import BP.DA.Depositary;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.OSDBSrc;

/**
 * 人员岗位
 */
public class EmpStations extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<EmpStation> convertEmpStations(Object obj)
	{
		return (ArrayList<EmpStation>) obj;
	}
	
	public ArrayList<EmpStation> ToJavaList()
	{
		return (ArrayList<EmpStation>) (Object)this;
	}

	// 构造
	/**
	 * 工作人员岗位
	 */
	public EmpStations()
	{
	}
	
	/**
	 * 工作人员与工作岗位集合
	 * @throws Exception 
	 */
	public EmpStations(String empNo) throws Exception
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database){
			this.Retrieve(EmpStationAttr.FK_Emp, empNo);
		}
	}
	
	// 方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new EmpStation();
	}
	
	
	// 查询方法
	/**
	 * 工作岗位对应的节点
	 * 
	 * @param stationNo
	 *            工作岗位编号
	 * @return 节点s
	 * @throws Exception 
	 */
	public final Emps GetHisEmps(String stationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpStationAttr.FK_Station, stationNo);
		qo.addOrderBy(EmpStationAttr.FK_Station);
		qo.DoQuery();
		
		Emps ens = new Emps();
		for (Object en : this)
		{
			ens.AddEntity(new Emp(((EmpStation) en).getFK_Emp()));
		}
		
		return ens;
	}
	
	/**
	 * 工作人员岗位s
	 * 
	 * @param empId
	 *            empId
	 * @return 工作人员岗位s
	 * @throws Exception 
	 */
	public final Stations GetHisStations(String empId) throws Exception
	{
		Stations ens = new Stations();
		if (Cash.IsExits("EmpStationsOf" + empId, Depositary.Application))
		{
			return (Stations) Cash.GetObjFormApplication("EmpStationsOf"
					+ empId, null);
		} else
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(EmpStationAttr.FK_Emp, empId);
			qo.addOrderBy(EmpStationAttr.FK_Station);
			qo.DoQuery();
			for (Object en : this)
			{
				ens.AddEntity(new Station(((EmpStation) en).getFK_Station()));
			}
			Cash.AddObj("EmpStationsOf" + empId, Depositary.Application, ens);
			return ens;
		}
	}
}