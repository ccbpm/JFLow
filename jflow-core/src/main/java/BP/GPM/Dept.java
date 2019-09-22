package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
 部门
*/
public class Dept extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 全名
	*/
	public final String getNameOfPath()
	{
		return this.GetValStrByKey(DeptAttr.NameOfPath);
	}
	public final void setNameOfPath(String value)
	{
		this.SetValByKey(DeptAttr.NameOfPath, value);
	}
	/** 
	 父节点的ID
	*/
	public final String getParentNo()
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	private Depts _HisSubDepts = null;
	/** 
	 它的子节点
	*/
	public final Depts getHisSubDepts()
	{
		if (_HisSubDepts == null)
		{
			_HisSubDepts = new Depts(this.getNo());
		}
		return _HisSubDepts;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 部门
	*/
	public Dept()
	{
	}
	/** 
	 部门
	 
	 @param no 编号
	*/
	public Dept(String no)
	{
		super(no);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map();
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //连接到的那个数据库上. (默认的是: AppCenterDSN )
		map.setPhysicsTable("Port_Dept");
		map.Java_SetEnType(EnType.Admin);

		map.setEnDesc("部门"); //  实体的描述.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, true, 1, 50, 20);

			//比如xx分公司财务部
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 100, 30);

			//比如:\\驰骋集团\\南方分公司\\财务部
		map.AddTBString(DeptAttr.NameOfPath, null, "部门路径", true, true, 0, 300, 30, true);

		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 100, 30);

			//顺序号.
		map.AddTBInt(DeptAttr.Idx, 0, "顺序号", true, false);

		RefMethod rm = new RefMethod();
		rm.Title = "重置该部门一下的部门路径";
		rm.ClassMethodName = this.toString() + ".DoResetPathName";
		rm.RefMethodType = RefMethodType.Func;

		String msg = "当该部门名称变化后,该部门与该部门的子部门名称路径(Port_Dept.NameOfPath)将发生变化.";
		msg += "\t\n 该部门与该部门的子部门的人员路径也要发生变化Port_Emp列DeptDesc.StaDesc.";
		msg += "\t\n 您确定要执行吗?";
		rm.Warning = msg;

		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "增加同级部门";
		rm.ClassMethodName = this.toString() + ".DoSameLevelDept";
		rm.getHisAttrs().AddTBString("No", null, "同级部门编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("Name", null, "部门名称", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "增加下级部门";
		rm.ClassMethodName = this.toString() + ".DoSubDept";
		rm.getHisAttrs().AddTBString("No", null, "同级部门编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("Name", null, "部门名称", true, false, 0, 100, 100);
		map.AddRefMethod(rm);


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new DeptEmps(), new BP.Port.Emps(), DeptEmpAttr.FK_Dept, DeptEmpAttr.FK_Emp, "对应人员", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.getFK_Dept()");


			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new DeptStations(), new Stations(), DeptStationAttr.FK_Dept, DeptStationAttr.FK_Station, "对应岗位(平铺)", StationAttr.FK_StationType);

		map.getAttrsOfOneVSM().AddGroupListModel(new DeptStations(), new Stations(), DeptStationAttr.FK_Dept, DeptStationAttr.FK_Station, "对应岗位(树)", StationAttr.FK_StationType);


		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 创建下级节点.
	 
	 @return 
	*/
	public final String DoMyCreateSubNode()
	{
		Entity en = this.DoCreateSubNode();
		return en.ToJson();
	}

	/** 
	 创建同级节点.
	 
	 @return 
	*/
	public final String DoMyCreateSameLevelNode()
	{
		Entity en = this.DoCreateSameLevelNode();
		return en.ToJson();
	}

	public final String DoSameLevelDept(String no, String name)
	{
		Dept en = new Dept();
		en.setNo(no);
		if (en.RetrieveFromDBSources() == 1)
		{
			return "err@编号已经存在";
		}

		en.setName(name);
		en.setParentNo(this.getParentNo());
		en.Insert();

		return "增加成功..";
	}
	public final String DoSubDept(String no, String name)
	{
		Dept en = new Dept();
		en.setNo(no);
		if (en.RetrieveFromDBSources() == 1)
		{
			return "err@编号已经存在";
		}

		en.setName(name);
		en.setParentNo(this.getNo());
		en.Insert();

		return "增加成功..";
	}
	/** 
	 重置部门
	 
	 @return 
	*/
	public final String DoResetPathName()
	{
		this.GenerNameOfPath();
		return "重置成功.";
	}

	/** 
	 生成部门全名称.
	*/
	public final void GenerNameOfPath()
	{
		String name = this.getName();

		//根目录不再处理
		if (this.getIsRoot() == true)
		{
			this.setNameOfPath(name);
			this.DirectUpdate();
			this.GenerChildNameOfPath(this.getNo());
			return;
		}

		Dept dept = new Dept();
		dept.setNo(this.getParentNo());
		if (dept.RetrieveFromDBSources() == 0)
		{
			return;
		}

		while (true)
		{
			if (dept.getIsRoot())
			{
				break;
			}

			name = dept.getName() + "\\" + name;
			dept = new Dept(dept.getParentNo());
		}
		//根目录
		name = dept.getName() + "\\" + name;
		this.setNameOfPath(name);
		this.DirectUpdate();

		this.GenerChildNameOfPath(this.getNo());

		//更新人员路径信息.
		BP.GPM.Emps emps = new Emps();
		emps.Retrieve(EmpAttr.FK_Dept, this.getNo());
		for (BP.GPM.Emp emp : emps.ToJavaList())
		{
			emp.Update();
		}
	}

	/** 
	 处理子部门全名称
	 
	 @param FK_Dept
	*/
	public final void GenerChildNameOfPath(String deptNo)
	{
		Depts depts = new Depts(deptNo);
		if (depts != null && depts.size() > 0)
		{
			for (Dept dept : depts.ToJavaList())
			{
				dept.GenerNameOfPath();
				GenerChildNameOfPath(dept.getNo());


				//更新人员路径信息.
				BP.GPM.Emps emps = new Emps();
				emps.Retrieve(EmpAttr.FK_Dept, this.getNo());
				for (BP.GPM.Emp emp : emps.ToJavaList())
				{
					emp.Update();
				}
			}
		}
	}
}