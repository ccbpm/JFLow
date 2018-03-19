package BP.WF.Port;

import BP.DA.*;
import BP.En.*;

/** 
 人员岗位 
*/
public class EmpStations extends Entities
{

		
	/** 
	 工作人员岗位
	*/
	public EmpStations()
	{
	}
	/** 
	 工作人员与工作岗位集合
	*/
	public EmpStations(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpStationAttr.FK_Station, stationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new EmpStation();
	}
	/** 
	 工作岗位对应的节点
	 @param stationNo 工作岗位编号
	 @return 节点s
	*/
	public final Emps GetHisEmps(String stationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpStationAttr.FK_Station, stationNo);
		qo.addOrderBy(EmpStationAttr.FK_Station);
		qo.DoQuery();

		Emps ens = new Emps();
		for(EmpStation en : this.ToJavaList())
		{
			ens.AddEntity(new Emp(en.getFK_Emp()));
		}

		return ens;
	}
	/** 
	 工作人员岗位s
	 @param empId empId
	 @return 工作人员岗位s 
	*/
	public final Stations GetHisStations(String empId)
	{
		Stations ens = new Stations();
		if (Cash.IsExits("EmpStationsOf"+empId, Depositary.Application))
		{
			return (Stations)Cash.GetObjFormApplication("EmpStationsOf"+empId,null);
		}
		else
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(EmpStationAttr.FK_Emp,empId);
			qo.addOrderBy(EmpStationAttr.FK_Station);
			qo.DoQuery();
			for(EmpStation en : this.ToJavaList())
			{
				ens.AddEntity(new Station(en.getFK_Station()));
			}
			Cash.AddObj("EmpStationsOf"+empId,Depositary.Application,ens);
			return ens;
		}
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<EmpStation> ToJavaList()
	{
		return (java.util.List<EmpStation>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<EmpStation> Tolist()
	{
		java.util.ArrayList<EmpStation> list = new java.util.ArrayList<EmpStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EmpStation)this.get(i));
		}
		return list;
	}
}