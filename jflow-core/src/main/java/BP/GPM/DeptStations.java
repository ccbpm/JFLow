package BP.GPM;

import BP.DA.Cash;
import BP.DA.Depositary;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 部门岗位对应 
*/
public class DeptStations extends Entities
{

	/** 
	 工作部门岗位对应
	*/
	public DeptStations()
	{
	}
	/** 
	 工作人员与岗位集合
	*/
	public DeptStations(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptStationAttr.FK_Station, stationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptStation();
	}

	/** 
	 岗位对应的节点
	 @param stationNo 岗位编号
	 @return 节点s
	*/
	public final Emps GetHisEmps(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptStationAttr.FK_Station, stationNo);
		qo.addOrderBy(DeptStationAttr.FK_Station);
		qo.DoQuery();

		Emps ens = new Emps();
		for(DeptStation en : this.ToJavaList())
		{
			ens.AddEntity(new Emp(en.getFK_Dept()));
		}

		return ens;
	}
	/** 
	 工作部门岗位对应s
	 @param empId empId
	 @return 工作部门岗位对应s 
	*/
	public final Stations GetHisStations(String empId)
	{
		Stations ens = new Stations();
		if (Cash.IsExits("DeptStationsOf"+empId, Depositary.Application))
		{
			return (Stations)Cash.GetObjFormApplication("DeptStationsOf"+empId, null);
		}
		else
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(DeptStationAttr.FK_Dept, empId);
			qo.addOrderBy(DeptStationAttr.FK_Station);
			qo.DoQuery();
			for(DeptStation en : this.ToJavaList())
			{
				ens.AddEntity(new Station(en.getFK_Station()));
			}
			Cash.AddObj("DeptStationsOf"+empId, Depositary.Application, ens);
			return ens;
		}
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<DeptStation> ToJavaList()
	{
		return (java.util.List<DeptStation>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<DeptStation> Tolist()
	{
		java.util.ArrayList<DeptStation> list = new java.util.ArrayList<DeptStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptStation)this.get(i));
		}
		return list;
	}

}