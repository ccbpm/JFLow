package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 部门岗位对应 
*/
public class DeptStations extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptStation();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询方法
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
		for (DeptStation en : this)
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
		if (Cash.IsExits("DeptStationsOf" + empId, Depositary.Application))
		{
			return (Stations)Cash.GetObjFormApplication("DeptStationsOf" + empId, null);
		}
		else
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(DeptStationAttr.FK_Dept, empId);
			qo.addOrderBy(DeptStationAttr.FK_Station);
			qo.DoQuery();
			for (DeptStation en : this)
			{
				ens.AddEntity(new Station(en.getFK_Station()));
			}
			Cash.AddObj("DeptStationsOf" + empId, Depositary.Application, ens);
			return ens;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<DeptStation> ToJavaList()
	{
		return (List<DeptStation>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DeptStation> Tolist()
	{
		ArrayList<DeptStation> list = new ArrayList<DeptStation>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((DeptStation)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}