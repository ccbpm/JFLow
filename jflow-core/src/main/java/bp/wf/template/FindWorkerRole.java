package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;

/** 
 找人规则
*/
public class FindWorkerRole extends EntityOIDName
{

		///#region  找同事
	/** 
	 找同事规则
	*/
	public final FindColleague getHisFindColleague() throws Exception {
		return FindColleague.forValue(Integer.parseInt(this.getTagVal3()));
	}

		///#endregion  找同事


		///#region  找领导类型
	/** 
	 寻找领导类型
	*/
	public final FindLeaderType getHisFindLeaderType() throws Exception {
		return FindLeaderType.forValue(Integer.parseInt(this.getSortVal1()));
	}
	/** 
	 模式
	*/
	public final FindLeaderModel getHisFindLeaderModel() throws Exception {
		return FindLeaderModel.forValue(Integer.parseInt(this.getSortVal2()));
	}

		///#endregion


		///#region 基本属性
	public final boolean isEnable() throws Exception
	{
		return this.GetValBooleanByKey(FindWorkerRoleAttr.IsEnable);
	}
	public final void setEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.IsEnable, value);
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 找人规则的事务编号
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(FindWorkerRoleAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.FK_Node, value);
	}

	/** 
	 类别0值
	*/
	public final String getSortVal0() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortVal0);
	}
	public final void setSortVal0(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortVal0, value);
	}
	/** 
	 类别0Text
	*/
	public final String getSortText0() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortText0);
	}
	public final void setSortText0(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortText0, value);
	}

	public final String getSortText3() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortText3);
	}
	public final void setSortText3(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortText3, value);
	}

	/** 
	 类别1值
	*/
	public final String getSortVal1() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortVal1);
	}
	public final void setSortVal1(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortVal1, value);
	}
	/** 
	 类别1Text
	*/
	public final String getSortText1() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortText1);
	}
	public final void setSortText1(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortText1, value);
	}

	/** 
	 类别2值
	*/
	public final String getSortVal2() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortVal2);
	}
	public final void setSortVal2(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortVal2, value);
	}
	/** 
	 类别2Text
	*/
	public final String getSortText2() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortText2);
	}
	public final void setSortText2(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortText2, value);
	}
	/** 
	 类别3值
	*/
	public final String getSortVal3() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortVal3);
	}
	public final void setSortVal3(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortVal3, value);
	}
	/** 
	 类别3Text
	*/
	public final String getSortText4() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.SortText4);
	}
	public final void setSortText4(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.SortText4, value);
	}
	/** 
	 数据0
	*/
	public final String getTagVal0() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagVal0);
	}
	public final void setTagVal0(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagVal0, value);
	}
	/** 
	 数据1
	*/
	public final String getTagVal1() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagVal1);
	}
	public final void setTagVal1(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagVal1, value);
	}
	/** 
	 TagVal2
	*/
	public final String getTagVal2() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagVal2);
	}
	public final void setTagVal2(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagVal2, value);
	}
	/** 
	 TagVal3
	*/
	public final String getTagVal3() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagVal3);
	}
	public final void setTagVal3(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagVal3, value);
	}
	/** 
	 数据0
	*/
	public final String getTagText0() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagText0);
	}
	public final void setTagText0(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagText0, value);
	}
	/** 
	 TagText1
	*/
	public final String getTagText1() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagText1);
	}
	public final void setTagText1(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagText1, value);
	}

	/** 
	 数据1
	*/
	public final String getTagText2() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagText2);
	}
	public final void setTagText2(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagText2, value);
	}
	/** 
	 TagText3
	*/
	public final String getTagText3() throws Exception
	{
		return this.GetValStringByKey(FindWorkerRoleAttr.TagText3);
	}
	public final void setTagText3(String value)  throws Exception
	 {
		this.SetValByKey(FindWorkerRoleAttr.TagText3, value);
	}

		///#endregion


		///#region 变量
	public WorkNode town = null;
	public WorkNode currWn = null;
	public Flow fl = null;
	private String dbStr = bp.difference.SystemConfig.getAppCenterDBVarStr();
	public Paras ps = null;
	public long WorkID = 0;
	public Node HisNode = null;

		///#endregion 变量


		///#region 构造函数
	/** 
	 找人规则
	*/
	public FindWorkerRole()  {
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FindWorkerRole", "找人规则");


		map.AddTBIntPKOID();

		map.AddTBString(FindWorkerRoleAttr.Name, null, "Name", true, false, 0, 200, 0);

		map.AddTBInt(FindWorkerRoleAttr.FK_Node, 0, "节点ID", false, false);

			// 规则存储.
		map.AddTBString(FindWorkerRoleAttr.SortVal0, null, "SortVal0", true, false, 0, 200, 0);
		map.AddTBString(FindWorkerRoleAttr.SortText0, null, "SortText0", true, false, 0, 200, 0);

		map.AddTBString(FindWorkerRoleAttr.SortVal1, null, "SortVal1", true, false, 0, 200, 0);
		map.AddTBString(FindWorkerRoleAttr.SortText1, null, "SortText1", true, false, 0, 200, 0);

		map.AddTBString(FindWorkerRoleAttr.SortVal2, null, "SortText2", true, false, 0, 200, 0);
		map.AddTBString(FindWorkerRoleAttr.SortText2, null, "SortText2", true, false, 0, 200, 0);

		map.AddTBString(FindWorkerRoleAttr.SortVal3, null, "SortVal3", true, false, 0, 200, 0);
		map.AddTBString(FindWorkerRoleAttr.SortText3, null, "SortText3", true, false, 0, 200, 0);


			// 规则采集信息值存储.
		map.AddTBString(FindWorkerRoleAttr.TagVal0, null, "TagVal0", true, false, 0, 1000, 0);
		map.AddTBString(FindWorkerRoleAttr.TagVal1, null, "TagVal1", true, false, 0, 1000, 0);
		map.AddTBString(FindWorkerRoleAttr.TagVal2, null, "TagVal2", true, false, 0, 1000, 0);
		map.AddTBString(FindWorkerRoleAttr.TagVal3, null, "TagVal3", true, false, 0, 1000, 0);

			// TagText
		map.AddTBString(FindWorkerRoleAttr.TagText0, null, "TagText0", true, false, 0, 1000, 0);
		map.AddTBString(FindWorkerRoleAttr.TagText1, null, "TagText1", true, false, 0, 1000, 0);
		map.AddTBString(FindWorkerRoleAttr.TagText2, null, "TagText2", true, false, 0, 1000, 0);
		map.AddTBString(FindWorkerRoleAttr.TagText3, null, "TagText3", true, false, 0, 1000, 0);

		map.AddTBInt(FindWorkerRoleAttr.IsEnable, 1, "是否可用", false, false);
		map.AddTBInt(FindWorkerRoleAttr.Idx, 0, "IDX", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 内部方法.
	/** 
	 上移
	*/
	public final void DoUp() throws Exception {
		this.DoOrderUp(FindWorkerRoleAttr.FK_Node, String.valueOf(this.getFK_Node()), FindWorkerRoleAttr.Idx);
	}
	/** 
	 下移
	*/
	public final void DoDown() throws Exception {
		this.DoOrderDown(FindWorkerRoleAttr.FK_Node, String.valueOf(this.getFK_Node()), FindWorkerRoleAttr.Idx);
	}
	private String sql = "";

		///#endregion 内部方法

	/** 
	 生成数据
	 
	 @return 
	*/
	public final DataTable GenerWorkerOfDataTable() throws Exception {
		DataTable dt = new DataTable();
		// 首先判断第一类别
		switch (this.getSortVal0())
		{
			case "ByDept":
				return this.GenerByDept();
			case "Leader":
			case "SpecEmps":


					///#region   首先找到2级参数，就是当事人是谁？
				String empNo = null;
				String empDept = null;
				switch (this.getHisFindLeaderType())
				{
					case Submiter: // 当前提交人的直线领导
						empNo = bp.web.WebUser.getNo();
						empDept = bp.web.WebUser.getFK_Dept();
						break;
					case SpecNodeSubmiter: // 指定节点提交人的直线领导.
						sql = "SELECT FK_Emp,FK_Dept FROM WF_GenerWorkerlist WHERE WorkID=" + this.WorkID + " AND FK_Node=" + this.getTagVal1();
						dt = DBAccess.RunSQLReturnTable(sql);
						if (dt.Rows.size() == 0)
						{
							throw new RuntimeException("@没有找到指定节点数据，请反馈给系统管理员，技术信息:" + sql);
						}
						empNo = dt.Rows.get(0).getValue(0) instanceof String ? (String)dt.Rows.get(0).getValue(0) : null;
						empDept = dt.Rows.get(0).getValue(1) instanceof String ? (String)dt.Rows.get(0).getValue(1) : null;
						break;
					case BySpecField: //指定节点字段人员的直接领导..
						sql = " SELECT " + this.getTagVal1() + " FROM " + this.HisNode.getHisFlow().getPTable() + " WHERE OID=" + this.WorkID;
						dt = DBAccess.RunSQLReturnTable(sql);
						empNo = dt.Rows.get(0).getValue(0) instanceof String ? (String)dt.Rows.get(0).getValue(0) : null;
						if (DataType.IsNullOrEmpty(empNo))
						{
							throw new RuntimeException("@指定的节点字段(" + this.getTagVal1() + ")的值为空.");
						}
						//指定它
						Emp emp = new Emp();
						emp.setUserID(empNo);
						if (emp.RetrieveFromDBSources() == 0)
						{
							throw new RuntimeException("@指定的节点字段(" + this.getTagVal1() + ")的值(" + empNo + ")是非法的人员编号...");
						}
						empDept = emp.getFK_Dept();
						break;
					default:
						//throw new RuntimeException("@尚未处理的Case:" + this.getHisFindLeaderType());
						break;
				}
				if (DataType.IsNullOrEmpty(empNo))
				{
					throw new RuntimeException("@遗漏的判断步骤，没有找到指定的工作人员.");
				}

					///#endregion

				if (this.getSortVal0().equals("Leader"))
				{
					return GenerHisLeader(empNo, empDept); // 产生他的领导并返回.
				}
				else
				{
					return GenerHisSpecEmps(empNo, empDept); // 产生他的特定的同事并返回.
				}
			default:
				break;
		}
		return null;
	}


		///#region 按部门查找
	private DataTable GenerByDept() throws Exception {
		//部门编号.
		String deptNo = this.getTagVal1();

		//职务-岗位。
		String objVal = this.getTagVal2();

		String way = this.getSortVal1();

		String sql = "";
		switch (way)
		{
			case "0": //按职务找.
				sql = "SELECT B." + bp.sys.base.Glo.getUserNo() + ",B.Name FROM Port_DeptEmp A, Port_Emp B WHERE A.FK_Dept='" + deptNo + "'  AND A.FK_Duty='" + objVal + "' AND B." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=A.FK_Emp";
				break;
			case "1": //按岗位找.
				sql = "SELECT B." + bp.sys.base.Glo.getUserNo() + ",B.Name FROM Port_DeptEmpStation A, Port_Emp B WHERE A.FK_Dept='" + deptNo + "'  AND A.FK_Station='" + objVal + "' AND B." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=A.FK_Emp";
				break;
			case "2": //所有该部门的人员.
				sql = "SELECT B." + bp.sys.base.Glo.getUserNo() + ",B.Name FROM Port_DeptEmp A, Port_Emp B WHERE A.FK_Dept='" + deptNo + "' AND B." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=A.FK_Emp";
				break;
			default:
				break;
		}
		return DBAccess.RunSQLReturnTable(sql);
	}

		///#endregion


		///#region 找同事
	/** 
	 当前提交人的直线领导
	 
	 @return 
	*/
	private DataTable GenerHisSpecEmps(String empNo, String empDept) throws Exception {
		DeptEmp de = new DeptEmp();

		DataTable dt = new DataTable();
		String leader = null;
		String tempDeptNo = "";

		switch (this.getHisFindColleague())
		{
			case All: // 所有该部门性质下的人员.
				sql = "SELECT Leader FROM Port_DeptEmp WHERE FK_Emp='" + empNo + "' AND FK_Dept='" + empDept + "'";
				dt = DBAccess.RunSQLReturnTable(sql);
				leader = dt.Rows.get(0).getValue(0) instanceof String ? (String)dt.Rows.get(0).getValue(0) : null;
				if (DataType.IsNullOrEmpty(leader))
				{
					throw new RuntimeException("@系统管理员没有给(" + empNo + ")在部门(" + empDept + ")中设置直接领导.");
				}

				break;
			case SpecDuty: // 特定职务级别的领导.
				Object tempVar = empDept;
				tempDeptNo = tempVar instanceof String ? (String)tempVar : null;
				while (true)
				{
					sql = "SELECT FK_Emp FROM Port_DeptEmp WHERE DutyLevel='" + this.getTagVal2() + "' AND FK_Dept='" + tempDeptNo + "'";
					DataTable mydt = DBAccess.RunSQLReturnTable(sql);
					if (mydt.Rows.size() != 0)
					{
						return mydt; //直接反回.
					}

					Dept d = new Dept(tempDeptNo);
					if (d.getParentNo().equals("0"))
					{
						return null; //如果到了跟节点.
					}
					tempDeptNo = d.getParentNo();
				}
			case SpecStation: // 特定岗位的领导.
				Object tempVar2 = empDept;
				tempDeptNo = tempVar2 instanceof String ? (String)tempVar2 : null;
				while (true)
				{
					sql = "SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Station='" + this.getTagVal2() + "' AND FK_Dept='" + tempDeptNo + "'";
					DataTable mydt = DBAccess.RunSQLReturnTable(sql);
					if (mydt.Rows.size() != 0)
					{
						return mydt; //直接反回.
					}

					Dept d = new Dept(tempDeptNo);
					if (d.getParentNo().equals("0"))
					{
						/* 在直线领导中没有找到 */
						return null; //如果到了跟节点.
					}
					tempDeptNo = d.getParentNo();
				}
			default:
				break;
		}

		// 增加列.
		dt.Columns.Add(new DataColumn("No", String.class));
		DataRow dr = dt.NewRow();
		dr.setValue(0, leader);
		dt.Rows.add(dr);
		return dt;
	}
	public String ErrMsg = null;

		///#endregion 直线领导


		///#region 直线领导
	/** 
	 当前提交人的直线领导
	 
	 @return 
	*/
	private DataTable GenerHisLeader(String empNo, String empDept) throws Exception {
		DeptEmp de = new DeptEmp();

		DataTable dt = new DataTable();
		String leader = null;
		String tempDeptNo = "";

		switch (this.getHisFindLeaderModel())
		{
			case DirLeader: // 直接领导.
				sql = "SELECT Leader FROM Port_DeptEmp WHERE FK_Emp='" + empNo + "' AND FK_Dept='" + empDept + "'";
				dt = DBAccess.RunSQLReturnTable(sql);
				leader = dt.Rows.get(0).getValue(0) instanceof String ? (String)dt.Rows.get(0).getValue(0) : null;
				if (DataType.IsNullOrEmpty(leader))
				{
					throw new RuntimeException("@系统管理员没有给(" + empNo + ")在部门(" + empDept + ")中设置直接领导.");
				}
				break;
			case SpecDutyLevelLeader: // 特定职务级别的领导.
				Object tempVar = empDept;
				tempDeptNo = tempVar instanceof String ? (String)tempVar : null;
				while (true)
				{
					sql = "SELECT FK_Emp FROM Port_DeptEmp WHERE DutyLevel='" + this.getTagVal2() + "' AND FK_Dept='" + tempDeptNo + "'";
					DataTable mydt = DBAccess.RunSQLReturnTable(sql);
					if (mydt.Rows.size() != 0)
					{
						return mydt; //直接反回.
					}

					Dept d = new Dept(tempDeptNo);
					if (d.getParentNo().equals("0"))
					{
						return null; //如果到了跟节点.
					}
					tempDeptNo = d.getParentNo();
				}
			case DutyLeader: // 特定职务的领导.
				Object tempVar2 = empDept;
				tempDeptNo = tempVar2 instanceof String ? (String)tempVar2 : null;
				while (true)
				{
					  sql = "SELECT FK_Emp FROM Port_DeptEmp WHERE FK_Duty='" + this.getTagVal2() + "' AND FK_Dept='" + tempDeptNo + "'";
					  DataTable mydt = DBAccess.RunSQLReturnTable(sql);
					if (mydt.Rows.size() != 0)
					{
						return mydt; //直接反回.
					}

					Dept d = new Dept(tempDeptNo);
					if (d.getParentNo().equals("0"))
					{
						return null; //如果到了跟节点.
					}
					tempDeptNo = d.getParentNo();
				}
			case SpecStation: // 特定岗位的领导.
				Object tempVar3 = empDept;
				tempDeptNo = tempVar3 instanceof String ? (String)tempVar3 : null;
				while (true)
				{
					sql = "SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Station='" + this.getTagVal2() + "' AND FK_Dept='" + tempDeptNo + "'";
					DataTable mydt = DBAccess.RunSQLReturnTable(sql);
					if (mydt.Rows.size() != 0)
					{
						return mydt; //直接反回.
					}

					Dept d = new Dept(tempDeptNo);
					if (d.getParentNo().equals("0"))
					{
						/* 在直线领导中没有找到 */
						return null; //如果到了跟节点.
					}
					tempDeptNo = d.getParentNo();
				}
			default:
				break;
		}

		// 增加列.
		dt.Columns.Add(new DataColumn("No", String.class));
		DataRow dr = dt.NewRow();
		dr.setValue(0, leader);
		dt.Rows.add(dr);
		return dt;
	}

		///#endregion 直线领导

	public final String getDBStr() throws Exception {
		return bp.difference.SystemConfig.getAppCenterDBVarStr();
	}
}