package BP.Port;

import BP.DA.Cash;
import BP.DA.Depositary;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 部门岗位对应
 */
public class DeptStations extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 工作部门岗位对应
	 */
	public DeptStations()
	{
	}
	
	/**
	 * 工作人员与岗位集合
	 * @throws Exception 
	 */
	public DeptStations(String stationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptStationAttr.FK_Station, stationNo);
		qo.DoQuery();
	}
	
	// 方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptStation();
	}
	
	// 查询方法
	/**
	 * 岗位对应的节点
	 * 
	 * @param stationNo
	 *            岗位编号
	 * @return 节点s
	 * @throws Exception 
	 */
	public final Emps GetHisEmps(String stationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptStationAttr.FK_Station, stationNo);
		qo.addOrderBy(DeptStationAttr.FK_Station);
		qo.DoQuery();
		
		Emps ens = new Emps();
		for (Object en : this)
		{
			ens.AddEntity(new Emp(((DeptStation) en).getFK_Dept()));
		}
		
		return ens;
	}
	
	/**
	 * 工作部门岗位对应s
	 * 
	 * @param empId
	 *            empId
	 * @return 工作部门岗位对应s
	 * @throws Exception 
	 */
	public final Stations GetHisStations(String empId) throws Exception
	{
		Stations ens = new Stations();
		if (Cash.IsExits("DeptStationsOf" + empId, Depositary.Application))
		{
			return (Stations) Cash.GetObjFormApplication("DeptStationsOf"
					+ empId, null);
		} else
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(DeptStationAttr.FK_Dept, empId);
			qo.addOrderBy(DeptStationAttr.FK_Station);
			qo.DoQuery();
			for (Object en : this)
			{
				ens.AddEntity(new Station(((DeptStation) en).getFK_Station()));
			}
			Cash.AddObj("DeptStationsOf" + empId, Depositary.Application, ens);
			return ens;
		}
	}
}