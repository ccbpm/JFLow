package BP.GPM;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityTree;
import BP.En.Map;
import BP.En.UAC;

/** 
 部门
*/
public class Dept extends EntityTree
{

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
	/** 
	 领导
	*/
	public final String getLeader()
	{
		return this.GetValStrByKey(DeptAttr.Leader);
	}
	public final void setLeader(String value)
	{
		this.SetValByKey(DeptAttr.Leader, value);
	}
	private Depts _HisSubDepts = null;
	/**
	 * 部门类型
	 */
	public final String getFK_DeptType()
	{
		return this.GetValStrByKey(DeptAttr.FK_DeptType);
	}
	
	public final void setFK_DeptType(String value)
	{
		this.SetValByKey(DeptAttr.FK_DeptType, value);
	}
	/** 
	 它的子节点
	 * @throws Exception 
	*/
	public final Depts getHisSubDepts() throws Exception
	{
		if (_HisSubDepts == null)
		{
			_HisSubDepts = new Depts(this.getNo());
		}
		return _HisSubDepts;
	}

	/** 
	 部门
	*/
	public Dept()
	{
	}
	/** 
	 部门
	 @param no 编号
	 * @throws Exception 
	*/
	public Dept(String no) throws Exception
	{
		super(no);
	}

	@Override
	public UAC getHisUAC() throws Exception
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

		map.setEnDesc("部门"); // 实体的描述.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, true, 1, 50, 20);

			//比如xx分公司财务部
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 100, 30);

			//比如:\\驰骋集团\\南方分公司\\财务部
		map.AddTBString(DeptAttr.NameOfPath, null, "部门路径", false, false, 0, 300, 30);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", false, false, 0, 100, 30);

			// 01,0101,010101.
		map.AddTBString(DeptAttr.TreeNo, null, "树编号", false, false, 0, 100, 30);

			//部门领导.
		map.AddTBString(DeptAttr.Leader, null, "领导", false, false, 0, 100, 30);
		map.AddTBString(DeptAttr.Tel, null, "联系电话", false, false, 0, 100, 30);
		
		map.AddTBString("OrgNo", null, "联系电话", false, false, 0, 100, 30);

			//顺序号.
		map.AddTBInt(DeptAttr.Idx, 0, "Idx", false, false);

			//是否是目录
		map.AddTBInt(DeptAttr.IsDir, 0, "是否是目录", false, false);

		  //  map.AddDDLEntities(DeptAttr. null, "部门类型", new DeptTypes(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/** 
	 生成部门全名称.
	 * @throws Exception 
	*/
	public final void GenerNameOfPath() throws Exception
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
	}

	/** 
	 处理子部门全名称
	 @param FK_Dept
	 * @throws Exception 
	*/
	public final void GenerChildNameOfPath(String FK_Dept) throws Exception
	{
		Depts depts = new Depts(FK_Dept);
		if (depts != null && depts.size() > 0)
		{
			for (Dept dept : depts.ToJavaList())
			{
				dept.GenerNameOfPath();
				GenerChildNameOfPath(dept.getNo());
			}
		}
	}
}