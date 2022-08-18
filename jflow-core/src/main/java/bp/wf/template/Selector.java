package bp.wf.template;

import bp.da.*;
import bp.en.Map;
import bp.sys.*;
import bp.en.*;
import bp.port.*;
import bp.web.*;
import bp.wf.*;
import bp.wf.Glo;

import java.util.*;

/** 
 选择器
*/
public class Selector extends Entity
{

		///#region 基本属性
	@Override
	public String getPK()  {
		return "NodeID";
	}
	/** 
	 选择模式
	*/
	public final SelectorModel getSelectorModel() throws Exception {
		return SelectorModel.forValue(this.GetValIntByKey(SelectorAttr.SelectorModel));
	}
	public final void setSelectorModel(SelectorModel value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.SelectorModel, value.getValue());
	}
	/** 
	 分组数据源
	*/
	public final String getSelectorP1() throws Exception {
		String s = this.GetValStringByKey(SelectorAttr.SelectorP1);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP1(String value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.SelectorP1, value);
	}
	/** 
	 实体数据源
	*/
	public final String getSelectorP2() throws Exception {
		String s = this.GetValStringByKey(SelectorAttr.SelectorP2);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP2(String value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.SelectorP2, value);
	}
	/** 
	 默认选择数据源
	*/
	public final String getSelectorP3() throws Exception {
		String s = this.GetValStringByKey(SelectorAttr.SelectorP3);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP3(String value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.SelectorP3, value);
	}
	/** 
	 强制选择数据源
	*/
	public final String getSelectorP4() throws Exception {
		String s = this.GetValStringByKey(SelectorAttr.SelectorP4);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP4(String value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.SelectorP3, value);
	}
	/** 
	 是否自动装载上一笔加载的数据
	*/
	public final boolean isAutoLoadEmps() throws Exception
	{
		return this.GetValBooleanByKey(SelectorAttr.IsAutoLoadEmps);
	}
	public final void setAutoLoadEmps(boolean value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.IsAutoLoadEmps, value);
	}
	/** 
	 是否单选？
	*/
	public final boolean isSimpleSelector() throws Exception
	{
		return this.GetValBooleanByKey(SelectorAttr.IsSimpleSelector);
	}
	public final void setSimpleSelector(boolean value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.IsSimpleSelector, value);
	}
	/** 
	 是否启用部门搜索范围限定
	*/
	public final boolean isEnableDeptRange() throws Exception
	{
		return this.GetValBooleanByKey(SelectorAttr.IsEnableDeptRange);
	}
	public final void setEnableDeptRange(boolean value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.IsEnableDeptRange, value);
	}
	/** 
	 是否启用岗位搜索范围限定
	*/
	public final boolean isEnableStaRange() throws Exception
	{
		return this.GetValBooleanByKey(SelectorAttr.IsEnableStaRange);
	}
	public final void setEnableStaRange(boolean value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.IsEnableStaRange, value);
	}
	/** 
	 节点ID
	*/
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(SelectorAttr.NodeID);
	}
	public final void setNodeID(int value)  throws Exception
	 {
		this.SetValByKey(SelectorAttr.NodeID, value);
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		if (WebUser.getNo().equals("admin") == true)
		{
			uac.IsUpdate = true;
			uac.IsView = true;
		}

		uac.IsUpdate = true;

		return uac;
	}

		///#endregion


		///#region 构造方法
	/** 
	 接受人选择器
	*/
	public Selector()  {
	}
	/** 
	 接受人选择器
	 
	 param nodeid
	*/
	public Selector(int nodeid) throws Exception {
		this.setNodeID(nodeid);
		this.Retrieve();
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


			///#region 字段.
		Map map = new Map("WF_Node", "选择器");

		map.setDepositaryOfEntity( Depositary.Application);

		map.AddTBIntPK(SelectorAttr.NodeID, 0, "NodeID", true, true);
		map.AddTBString(SelectorAttr.Name, null, "节点名称", true, true, 0, 100, 100);

		map.AddDDLSysEnum(SelectorAttr.SelectorModel, 5, "显示方式", true, true, SelectorAttr.SelectorModel, "@0=按岗位@1=按部门@2=按人员@3=按SQL@4=按SQL模版计算@5=使用通用人员选择器@6=部门与岗位的交集@7=自定义Url@8=使用通用部门岗位人员选择器@9=按岗位智能计算(操作员所在部门)");

		map.AddDDLSQL(SelectorAttr.FK_SQLTemplate, null, "SQL模版", "SELECT No,Name FROM WF_SQLTemplate WHERE SQLType=5", true);

		map.AddBoolean(SelectorAttr.IsAutoLoadEmps, true, "是否自动加载上一次选择的人员？", true, true);
		map.AddBoolean(SelectorAttr.IsSimpleSelector, false, "是否单项选择(只能选择一个人)？", true, true);
		map.AddBoolean(SelectorAttr.IsEnableDeptRange, false, "是否启用部门搜索范围限定(对使用通用人员选择器有效)？", true, true, true);
		map.AddBoolean(SelectorAttr.IsEnableStaRange, false, "是否启用岗位搜索范围限定(对使用通用人员选择器有效)？", true, true, true);


			// map.AddDDLSysEnum(SelectorAttr.IsMinuesAutoLoadEmps, 5, "接收人选择方式", true, true, SelectorAttr.SelectorModel,
			// "@0=按岗位@1=按部门@2=按人员@3=按SQL@4=按SQL模版计算@5=使用通用人员选择器@6=部门与岗位的交集@7=自定义Url");

		map.AddTBStringDoc(SelectorAttr.SelectorP1, null, "分组参数:可以为空,比如:SELECT No,Name,ParentNo FROM  Port_Dept", true, false, 0, 300, 3);
		map.AddTBStringDoc(SelectorAttr.SelectorP2, null, "操作员数据源:比如:SELECT No,Name,FK_Dept FROM  Port_Emp", true, false, 0, 300, 3);

		map.AddTBStringDoc(SelectorAttr.SelectorP3, null, "默认选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID", true, false, 0, 300, 3);
		map.AddTBStringDoc(SelectorAttr.SelectorP4, null, "强制选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID", true, false, 0, 300, 3);
		map.AddTBString(NodeAttr.DeliveryParas, null, "访问规则设置", true, false, 0, 300, 10);

			///#endregion


			///#region 对应关系
			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new bp.wf.template.NodeStations(), new Stations(), bp.wf.template.NodeStationAttr.FK_Node, bp.wf.template.NodeStationAttr.FK_Station, "绑定岗位(平铺)", StationAttr.FK_StationType, "Name", "No");

		map.getAttrsOfOneVSM().AddGroupListModel(new bp.wf.template.NodeStations(), new Stations(), bp.wf.template.NodeStationAttr.FK_Node, bp.wf.template.NodeStationAttr.FK_Station, "绑定岗位(树)", StationAttr.FK_StationType, "Name", "No");

			//节点绑定部门. 节点绑定部门.
		map.getAttrsOfOneVSM().AddBranches(new bp.wf.template.NodeDepts(), new Depts(), bp.wf.template.NodeDeptAttr.FK_Node, bp.wf.template.NodeDeptAttr.FK_Dept, "绑定部门", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept", null);

			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new bp.wf.template.NodeEmps(), new Emps(), bp.wf.template.NodeEmpAttr.FK_Node, bp.wf.template.NodeEmpAttr.FK_Emp, "绑定接受人", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept", null);

			///#endregion


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 产生数据.
	 
	 @return 
	*/
	public final DataSet GenerDataSet(int nodeid, Entity en) throws Exception {
		DataSet ds = null;
		switch (this.getSelectorModel())
		{
			case Dept:
				ds = ByDept(nodeid, en);
				break;
			case TeamOrgOnly:
				ds = ByTeam(nodeid, en, this.getSelectorModel());
				break;
			case TeamOnly:
				ds = ByTeam(nodeid, en, this.getSelectorModel());
				break;
			case TeamDeptOnly:
				ds = ByTeam(nodeid, en, this.getSelectorModel());
				break;
			case Emp:
				ds = ByEmp(nodeid);
				break;
			case Station:
				ds = ByStation(nodeid, en);
				break;
			case ByStationAI:
				ds = ByStationAI(nodeid, en);

				break;
			case DeptAndStation:
				ds = DeptAndStation(nodeid);
				break;
			case SQL:
				ds = BySQL(nodeid, en);
				break;
			case SQLTemplate:
				ds = SQLTemplate(nodeid, en);
				break;
			case GenerUserSelecter:
				ds = ByGenerUserSelecter();
				break;
			case AccepterOfDeptStationOfCurrentOper: //按岗位智能计算.
				ds = AccepterOfDeptStationOfCurrentOper(nodeid, en);
				break;
			case ByWebAPI:
				ds = ByWebAPI(en);
				break;
			case ByMyDeptEmps:
				ds = ByMyDeptEmps();
				break;
			default:
				throw new RuntimeException("@错误:没有判断的选择类型:" + this.getSelectorModel());
		}

		if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			for (DataTable dt : ds.Tables)
			{
				for (int i = 0; i < dt.Columns.size(); i++)
				{
					if (dt.Columns.get(i).ColumnName.toUpperCase().equals("NO"))
					{
						dt.Columns.get(i).ColumnName = "No";
					}

					if (dt.Columns.get(i).ColumnName.toUpperCase().equals("NAME"))
					{
						dt.Columns.get(i).ColumnName = "Name";
					}

					if (dt.Columns.get(i).ColumnName.toUpperCase().equals("PARENTNO"))
					{
						dt.Columns.get(i).ColumnName = "ParentNo";
					}

					if (dt.Columns.get(i).ColumnName.toUpperCase().equals("FK_DEPT"))
					{
						dt.Columns.get(i).ColumnName = "FK_Dept";
					}
				}
			}
		}

		ds.Tables.add(this.ToDataTableField("Selector"));

		return ds;
	}
	/** 
	 通用
	 
	 @return 
	*/
	private DataSet ByGenerUserSelecter() throws Exception {
		DataSet ds = new DataSet();

		////排序.
		//string orderByDept = "";
		//if (DBAccess.IsExitsTableCol("Port_Dept", "Idx"))
		//    orderByDept = " ORDER BY Port_Dept.Idx";

		//string orderByEmp = "";
		//if (DBAccess.IsExitsTableCol("Port_Emp", "Idx"))
		//    orderByDept = " ORDER BY Port_Emp.FK_Dept, Port_Emp.Idx";

		//部门
		String sql = "SELECT distinct No,Name, ParentNo FROM Port_Dept  ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		sql = "SELECT  No, Name, FK_Dept FROM Port_Emp ";
		DataTable dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);

		return ds;
	}
	/** 
	 按照模版
	 
	 param nodeID 节点ID
	 param en
	 @return 
	*/
	private DataSet SQLTemplate(int nodeID, Entity en) throws Exception {
		//设置他的模版.
		//Node nd = new Node(nodeID);

		SQLTemplate sql = new SQLTemplate(this.getSelectorP1());
		this.setSelectorP2(sql.getDocs());

		return BySQL(nodeID, en);
	}

	/** 
	 按照SQL计算.
	 
	 param nodeID 节点ID
	 @return 返回值
	*/
	private DataSet BySQL(int nodeID, Entity en) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();

		//求部门.
		String sqlGroup = this.getSelectorP1(); // @sly
		if (DataType.IsNullOrEmpty(sqlGroup) == false && sqlGroup.length() > 6)
		{
			sqlGroup = Glo.DealExp(sqlGroup, en, null); //@祝梦娟
			DataTable dt = DBAccess.RunSQLReturnTable(sqlGroup);
			dt.TableName = "Depts";
			//转换大小写
			for (DataColumn col : dt.Columns)
			{
				String colName = col.ColumnName.toLowerCase();
				switch (colName)
				{
					case "no":
						col.ColumnName = "No";
						break;
					case "name":
						col.ColumnName = "Name";
						break;
					default:
						break;
				}
			}
			ds.Tables.add(dt);
		}

		//求人员范围.
		String sqlDB = this.getSelectorP2();
		sqlDB = Glo.DealExp(sqlDB, en, null); //@祝梦娟

		DataTable dtEmp = DBAccess.RunSQLReturnTable(sqlDB);
		dtEmp.TableName = "Emps";
		//转换大小写
		for (DataColumn col : dtEmp.Columns)
		{
			String colName = col.ColumnName.toLowerCase();
			switch (colName)
			{
				case "no":
					col.ColumnName = "No";
					break;
				case "name":
					col.ColumnName = "Name";
					break;
				case "fk_dept":
					col.ColumnName = "FK_Dept";
					break;
				default:
					break;
			}
		}
		ds.Tables.add(dtEmp);

		//求默认选择的数据.
		if (!this.getSelectorP3().equals(""))
		{
			sqlDB = this.getSelectorP3();
			sqlDB = Glo.DealExp(sqlDB, en, null); //@祝梦娟

			DataTable dtDef = DBAccess.RunSQLReturnTable(sqlDB);
			dtDef.TableName = "DefaultSelected";
			for (DataColumn col : dtDef.Columns)
			{
				String colName = col.ColumnName.toLowerCase();
				switch (colName)
				{
					case "no":
						col.ColumnName = "No";
						break;
					case "name":
						col.ColumnName = "Name";
						break;
					default:
						break;
				}
			}

			ds.Tables.add(dtDef);
		}


		//求强制选择的数据源.
		if (!this.getSelectorP4().equals(""))
		{
			sqlDB = this.getSelectorP4();

			sqlDB = sqlDB.replace("@WebUser.No", WebUser.getNo());
			sqlDB = sqlDB.replace("@WebUser.Name", WebUser.getName());
			sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

			sqlDB = sqlDB.replace("@WorkID", en.GetValStringByKey("OID"));
			sqlDB = sqlDB.replace("@OID", en.GetValStringByKey("OID"));

			if (sqlDB.contains("@"))
			{
				sqlDB = Glo.DealExp(sqlDB, en, null);
			}

			DataTable dtForce = DBAccess.RunSQLReturnTable(sqlDB);
			for (DataColumn col : dtForce.Columns)
			{
				String colName = col.ColumnName.toLowerCase();
				switch (colName)
				{
					case "no":
						col.ColumnName = "No";
						break;
					case "name":
						col.ColumnName = "Name";
						break;
					default:
						break;
				}
			}
			dtForce.TableName = "ForceSelected";
			ds.Tables.add(dtForce);
		}

		return ds;
	}

	/** 
	 按照部门获取部门人员树.
	 
	 param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByDept(int nodeID, Entity en) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;

		Node nd = new Node(nodeID);
		if (nd.getHisDeliveryWay() == DeliveryWay.BySelectedForPrj)
		{
			//部门.
			sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a,  WF_NodeDept b, WF_PrjEmp C,Port_DeptEmp D WHERE A.No=B.FK_Dept AND B.FK_Node=" + nodeID + " AND C.FK_Prj='" + en.GetValStrByKey("PrjNo") + "' ORDER BY a.Idx ";
			sql += "  AND C.FK_Emp=D.FK_Emp ";


			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.add(dt);

			//人员.
			sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ", a.Name, a.FK_Dept FROM Port_Emp a, WF_NodeDept b, WF_PrjEmp C WHERE a.FK_Dept=b.FK_Dept  AND A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=C.FK_Emp  AND B.FK_Node=" + nodeID + " AND C.FK_Prj='" + en.GetValStrByKey("PrjNo") + "'  ORDER BY a.Idx ";
			dtEmp = DBAccess.RunSQLReturnTable(sql);
			ds.Tables.add(dtEmp);
			dtEmp.TableName = "Emps";
			return ds;
		}


		//部门.
		sql = "SELECT distinct a.No,a.Name, a.ParentNo,a.Idx FROM Port_Dept a,WF_NodeDept b WHERE a.No=b.FK_Dept AND B.FK_Node=" + nodeID + "   ORDER BY a.Idx ";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ", a.Name, d.FK_Dept ,a.Idx FROM Port_Emp a, WF_NodeDept b,Port_DeptEmp d WHERE d.FK_Dept=b.FK_Dept AND a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=d.FK_Emp AND B.FK_Node=" + nodeID + "  ORDER BY a.Idx";
		dtEmp = DBAccess.RunSQLReturnTable(sql);
		ds.Tables.add(dtEmp);
		dtEmp.TableName = "Emps";
		return ds;
	}
	/** 
	 按照Emp获取部门人员树.
	 
	 param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByEmp(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();


		//部门.
		String sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a, WF_NodeEmp b, Port_Emp c WHERE b.FK_Emp=c." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND a.No=c.FK_Dept AND B.FK_Node=" + nodeID + " ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept FROM Port_Emp a, WF_NodeEmp b WHERE a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=b.FK_Emp AND b.FK_Node=" + nodeID;

		DataTable dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}
	/** 
	 按照Emp获取部门人员树.
	 
	 param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByMyDeptEmps() throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();


		//部门.
		String sql = "SELECT No,Name FROM Port_Dept  WHERE No='" + WebUser.getFK_Dept()+ "' ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		sql = " SELECT No,Name,FK_Dept FROM Port_Emp  WHERE FK_Dept='" + WebUser.getFK_Dept()+ "'";

		DataTable dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}
	/** 
	 
	 
	 param nodeID
	 param en
	 @return 
	*/
	private DataSet AccepterOfDeptStationOfCurrentOper(int nodeID, Entity en) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();

		//部门.
		String sql = "";
		sql = "SELECT d.No,d.Name,d.ParentNo  FROM  Port_DeptEmp  de, Port_Dept as d WHERE de.FK_Dept = d.No and de.FK_Emp = '" + WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//人员.
		if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle
			|| bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3
			|| bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Dept='" + WebUser.getFK_Dept()+ "' AND b.FK_Node=" + nodeID + ")  ORDER BY A.Idx ";
		}
		else
		{
			sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a." + bp.sys.base.Glo.getUserNo() + "=c.FK_Emp AND C.FK_Dept='" + WebUser.getFK_Dept()+ "' AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
		}

		DataTable dtEmp = DBAccess.RunSQLReturnTable(sql);
		if (dtEmp.Rows.size() > 0)
		{
			dt.TableName = "Depts";
			ds.Tables.add(dt);

			dtEmp.TableName = "Emps";
			ds.Tables.add(dtEmp);
		}
		else //如果没人，就查询父级
		{
			//查询当前节点的workdID
			long workID = Long.parseLong(en.GetValStringByKey("OID"));
			WorkNode node = new WorkNode(workID, nodeID);

			sql = " SELECT No,Name, ParentNo FROM Port_Dept WHERE no  in (  SELECT  ParentNo FROM Port_Dept WHERE No  IN " + "( SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID ='" + workID + "' ))";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.add(dt);

			// 如果当前的节点不是开始节点， 从轨迹里面查询。
			sql = "SELECT DISTINCT b." + bp.sys.base.Glo.getUserNo() + ",b.Name,b.FK_Dept   FROM Port_DeptEmpStation a,Port_Emp b  WHERE FK_Station IN " + "( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") " + "AND a.FK_Dept IN (SELECT ParentNo FROM Port_Dept WHERE No IN (SELECT FK_DEPT FROM WF_GenerWorkerlist WHERE WorkID=" + workID + "))" + " AND a.FK_Emp = b." + bp.sys.base.Glo.getUserNo() + " ";
			sql += " ORDER BY b.No ";

			dtEmp = DBAccess.RunSQLReturnTable(sql);
			dtEmp.TableName = "Emps";
			ds.Tables.add(dtEmp);
		}
		return ds;
	}
	/** 
	 部门于岗位的交集 @zkr. 
	 
	 param nodeID
	 @return 
	*/
	private DataSet DeptAndStation(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		//部门.
		String sql = "";

		sql = "SELECT B.No,B.Name,B.ParentNo FROM WF_NodeDept A, Port_Dept B WHERE A.FK_Dept=B.No AND FK_Node=" + nodeID;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//@zkr.
		sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,a.Idx FROM Port_Emp A,  WF_NodeStation b, Port_DeptEmpStation c,WF_NodeDept D WHERE a." + bp.sys.base.Glo.getUserNo() + "=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + " AND D.FK_Dept=A.FK_Dept AND D.FK_Node=" + nodeID + "  ORDER BY A.Idx ";
		DataTable dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}

	/** 
	 按用户组计算
	 
	 param nodeID
	 param en
	 @return 
	*/
	private DataSet ByTeam(int nodeID, Entity en, SelectorModel sm) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;
		Node nd = new Node(nodeID);
		if (sm == SelectorModel.TeamDeptOnly)
		{
			sql = "SELECT  No,Name FROM Port_Dept WHERE No='" + WebUser.getFK_Dept()+ "'";
		}
		if (sm == SelectorModel.TeamOnly)
		{
			sql = "SELECT DISTINCT a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeTeam b, Port_TeamEmp c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Team=c.FK_Team AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID + "   ORDER BY A.No,A.Idx";
		}
		if (sm == SelectorModel.TeamOrgOnly)
		{
			sql = "SELECT DISTINCT a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeTeam b, Port_TeamEmp c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Team=c.FK_Team AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID + " AND D.OrgNo='" + WebUser.getOrgNo() + "' ORDER BY A.No,A.Idx";
		}

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle || bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || DBAccess.getAppCenterDBType() == DBType.UX || DBAccess.getAppCenterDBType() == DBType.KingBaseR3 ||DBAccess.getAppCenterDBType() == DBType.KingBaseR6	)
		{
			if (sm == SelectorModel.TeamDeptOnly)
			{
				sql = "SELECT * FROM (SELECT DISTINCT a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeTeam b, Port_TeamEmp c WHERE a.No=c.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nodeID + " AND A.FK_Dept='" + WebUser.getFK_Dept()+ "') ORDER BY FK_Dept,Idx,No";
			}
			if (sm == SelectorModel.TeamOrgOnly)
			{
				sql = "SELECT * FROM (SELECT DISTINCT a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeTeam b, Port_TeamEmp c WHERE a.No=c.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nodeID + " AND A.OrgNo='" + WebUser.getOrgNo() + "') ORDER BY FK_Dept,Idx,No";
			}
			if (sm == SelectorModel.TeamOnly)
			{
				sql = "SELECT * FROM (SELECT DISTINCT a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeTeam b, Port_TeamEmp c WHERE a.No=c.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nodeID + " ) ORDER BY FK_Dept,Idx,No";
			}
		}
		else
		{
			if (sm == SelectorModel.TeamDeptOnly)
			{
				sql = "SELECT DISTINCT a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,a.Idx FROM Port_Emp A,  WF_NodeTeam B, Port_TeamEmp C WHERE a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=c.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nodeID + " AND A.FK_Dept='" + WebUser.getFK_Dept()+ "'  ORDER BY A.Idx";
			}
			if (sm == SelectorModel.TeamOrgOnly)
			{
				sql = "SELECT DISTINCT a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,a.Idx FROM Port_Emp A,  WF_NodeTeam B, Port_TeamEmp C WHERE a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=c.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nodeID + " AND A.OrgNo='" + WebUser.getOrgNo() + "'  ORDER BY A.Idx";
			}
			if (sm == SelectorModel.TeamOnly)
			{
				sql = "SELECT DISTINCT a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,a.Idx FROM Port_Emp A,  WF_NodeTeam B, Port_TeamEmp C WHERE a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=c.FK_Emp AND B.FK_Team=C.FK_Team AND B.FK_Node=" + nodeID + "  ORDER BY A.Idx";
			}
		}

		dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}
	private DataSet ByGroupOnly(int nodeID, Entity en) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;

		Node nd = new Node(nodeID);

		//部门.
		sql = "SELECT distinct a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeTeam b, Port_TeamEmp c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Group=c.FK_Group AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID + " ORDER BY A.No,A.Idx";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle || bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || DBAccess.getAppCenterDBType() == DBType.UX || DBAccess.getAppCenterDBType() == DBType.KingBaseR3 || DBAccess.getAppCenterDBType() == DBType.KingBaseR6)
		{
			if (DBAccess.IsExitsTableCol("Port_Emp", "Idx") == true)
			{
				sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeTeam b, Port_TeamEmp c WHERE a.No=c.FK_Emp AND B.FK_Group=C.FK_Group AND b.FK_Node=" + nodeID + ") ORDER BY FK_Dept,Idx,No";
			}
			else
			{
				sql = "SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeTeam b, Port_TeamEmp c WHERE a.No=c.FK_Emp AND B.FK_Group=C.FK_Group AND b.FK_Node=" + nodeID + " ";
			}
		}
		else
		{
			sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeTeam b, Port_TeamEmp c WHERE a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=c.FK_Emp AND B.FK_Group=C.FK_Group AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
		}

		dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}

	private DataSet ByStationAI(int nodeID, Entity en) throws Exception {
		Node nd = new Node(nodeID);

		int ShenFenModel = nd.GetParaInt("ShenFenModel", 0);

		//如果按照上一个节点的操作员身份计算.
		if (ShenFenModel == 0)
		{
			return ByStationAI(en, WebUser.getFK_Dept(), WebUser.getNo());
		}

		//如果按照指定节点的操作员身份计算.
		if (ShenFenModel == 1)
		{
			int specNodeID = nd.GetParaInt("ShenFenVal", 0);

			int workID = en.GetValIntByKey("OID");

			String sql = "SELECT FK_Emp,FK_Dept FROM WF_GenerWorkerList WHERE FK_Node=" + specNodeID + " AND WorkID=" + workID;
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			String empNo = "", deptNo = "";
			if (dt.Rows.size() == 0)
			{
				Node ndSpec = new Node(specNodeID);
				if (ndSpec.isStartNode() == false)
				{
					throw new RuntimeException("err@没有找到上一步节点，参数信息: NodeID=" + specNodeID + ",WorkID=" + workID + ", 不应该出现的异常，请联系管理员, 有可能您配置了没有路过的节点，作为指定节点的身份计算了。");
				}
				empNo = WebUser.getNo();
				deptNo = WebUser.getFK_Dept();
			}
			else
			{
				//获得指定节点的人员编号.
				empNo = dt.Rows.get(0).getValue(0).toString();
				deptNo = dt.Rows.get(0).getValue(1).toString();
			}

			return ByStationAI(en, deptNo, empNo);
		}

		//如果按指定字段的身份计算.
		if (ShenFenModel == 2)
		{
			String empNo = nd.GetParaString("ShenFenVal");
			Emp emp = new Emp(empNo);
			return ByStationAI(en, emp.getFK_Dept(), emp.getNo());
		}

		throw new RuntimeException("err@没有判断的身份模式." + ShenFenModel);
	}

	private DataSet ByStationAI(Entity en, String deptNo, String userID) throws Exception {
		//第一次计算.
		DataSet ds = ByStationAI_Ext(en, deptNo, userID);

		if (ds.Tables.get(1).Rows.size() == 0)
		{
			//如果在本部门找不到，就到父部门去找.
			Dept mydept = new Dept(deptNo);
			ds = ByStationAI_Ext(en, mydept.getParentNo(), userID);

			if (ds.Tables.get(1).Rows.size() == 0)
			{
				//如果父部门找不到，就到父父部门去找, 在找不到就不找了。
				if (mydept.getParentNo().equals("0") == false)
				{
					Dept myParentDept = new Dept(mydept.getParentNo());
					ds = ByStationAI_Ext(en, myParentDept.getParentNo(), userID);
					if (ds.Tables.get(1).Rows.size() != 0)
					{
						return ds;
					}
				}

				if (ds.Tables.get(1).Rows.size() == 0)
				{
					//如果爷爷部门也找不到，就到于父亲同一级的部门去找.
					Depts pDepts = new Depts();
					pDepts.Retrieve(DeptAttr.ParentNo, mydept.getParentNo(), null);

					for (Dept item : pDepts.ToJavaList())
					{
						ds = ByStationAI_Ext(en, item.getNo(), userID);
						if (ds.Tables.get(1).Rows.size() >= 1)
						{
							return ds;
						}
					}
				}
			}
		}

		//如果实在找不到了，就仅按岗位计算.
		if (ds.Tables.get(1).Rows.size() == 0)
		{
			ds = ByStation(this.getNodeID(), en);
		}

		return ds;

		////如果人员为空.
		//while (ds.Tables[1].Rows.size() == 0)
		//{
		//    GPM.Dept mydept = new GPM.Dept(deptNo);

		//    //首先扫描平级部门.
		//    bp.port.Depts depts = new GPM.Depts();
		//    depts.Retrieve(bp.port.DeptAttr.ParentNo, mydept.ParentNo);
		//    bp.port.Dept dept = new bp.port.Dept(WebUser.getFK_Dept());
		//    ds = ByStationAI_Ext(nodeID, en, dept.ParentNo, bp.web.WebUser.getNo());
		//}
		//return ds;
	}
	/** 
	 指定部门下的，岗位人员的数据。
	 
	 param nodeID
	 param en
	 param deptNo
	 param userNo
	 @return 
	*/
	private DataSet ByStationAI_Ext(Entity en, String deptNo, String userNo) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;

		//部门. @zkr
		sql = "";
		sql += "SELECT No, Name FROM Port_Dept WHERE No = '" + deptNo + "'";
		sql += " UNION ";
		sql += "SELECT  No, Name FROM Port_Dept A, Port_DeptEmp B WHERE A.No = B.FK_Dept AND B.FK_Emp = '" + userNo + "'";

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//查询人员.
		sql = "SELECT A.No,A.Name, A.FK_Dept FROM Port_Emp A, Port_DeptEmpStation B, WF_NodeStation C WHERE C.FK_Node = " + this.getNodeID() + " AND B.FK_Dept = '" + deptNo + "' AND A.FK_Dept = B.FK_Dept AND B.FK_Station=C.FK_Station AND A.No=b.FK_Emp  ORDER BY A.Idx";
		dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}
	/** 
	 按照Station获取部门人员树.
	 
	 param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByStation(int nodeID, Entity en) throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;

		Node nd = new Node(nodeID);
		if (nd.getHisDeliveryWay() == DeliveryWay.BySelectedForPrj)
		{
			//部门.
			sql = "SELECT distinct a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d, WF_PrjEmp E WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND d." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=e.FK_Emp And C.FK_Emp=E.FK_Emp  AND B.FK_Node=" + nodeID + " AND E.FK_Prj='" + en.GetValStrByKey("PrjNo") + "' ORDER BY A.No,A.Idx";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.add(dt);

			//人员.
			if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle || bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || DBAccess.getAppCenterDBType() == DBType.UX)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "Idx") == true)
				{
					sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c, WF_PrjEmp d  WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station And a.No=d.FK_Emp And C.FK_Emp=d.FK_Emp AND b.FK_Node=" + nodeID + " AND D.FK_Prj='" + en.GetValStrByKey("PrjNo") + "') ORDER BY FK_Dept,Idx,No";
				}
				else
				{
					sql = "SELECT distinct a.No,a.Name, a.FK_Dept,A.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c, WF_PrjEmp d  WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station And a.No=d.FK_Emp And C.FK_Emp=d.FK_Emp AND b.FK_Node=" + nodeID + " AND D.FK_Prj='" + en.GetValStrByKey("PrjNo") + "'  ORDER BY A.Idx ";
				}
			}
			else
			{
				sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,A.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c, WF_PrjEmp d WHERE a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=c.FK_Emp AND B.FK_Station=C.FK_Station And a." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=d.FK_Emp And C.FK_Emp=d.FK_Emp AND b.FK_Node=" + nodeID + " AND D.FK_Prj='" + en.GetValStrByKey("PrjNo") + "'  ORDER BY A.Idx ";
			}

			dtEmp = DBAccess.RunSQLReturnTable(sql);
			ds.Tables.add(dtEmp);
			dtEmp.TableName = "Emps";
			return ds;
		}


		//部门.
		sql = "SELECT distinct a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D." + bp.sys.base.Glo.getUserNoWhitOutAS() + " AND B.FK_Node=" + nodeID + " ORDER BY A.No,A.Idx";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		if (bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.Oracle || bp.difference.SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || DBAccess.getAppCenterDBType() == DBType.UX)
		{
			if (DBAccess.IsExitsTableCol("Port_Emp", "Idx") == true)
			{
				sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + ") ORDER BY FK_Dept,Idx,No";
			}
			else
			{
				sql = "SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
			}
		}
		else
		{
			sql = "SELECT distinct a." + bp.sys.base.Glo.getUserNo() + ",a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a." + bp.sys.base.Glo.getUserNo() + "=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
		}

		dtEmp = DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);

		return ds;
	}
	private DataSet ByWebAPI(Entity en) throws Exception {
		DataSet ds = new DataSet();
		//返回值
		String postData = "";
		//配置的api地址
		String apiUrl = this.getSelectorP1();
		if (apiUrl.contains("@WebApiHost")) //可以替换配置文件中配置的webapi地址
		{
			apiUrl = apiUrl.replace("@WebApiHost", bp.difference.SystemConfig.getAppSettings().get("WebApiHost").toString());
		}

		//增加header参数
		Hashtable headerMap = new Hashtable();


		//saas模式，需要传入systemNo
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			//获取系统编号
			//string systemNo = bp.da.DBAccess.RunSQLReturnStringIsNull("select No from port_domain where No=(select domain from port_org where No=(select orgNo from port_emp where No='" + WebUser.getNo() + "'))", "");
			//headerMap.Add("systemNo", systemNo);
			//headerMap.Add("orgNo", WebUser.getOrgNo());
		}
		//集团模式，传入域编号
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			//传入域
			headerMap.put("OrgNo", WebUser.getOrgNo());
		}

		//加入token
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Authorization", WebUser.getToken());



		apiUrl = Glo.DealExp(apiUrl, en, null);
		//执行POST
		//postData = HttpClientUtil.doPost(apiUrl, headerMap, "");

		DataTable dt = bp.tools.Json.ToDataTable(postData);
		dt.TableName = "Emps";
		ds.Tables.add(dt);

		//部门
		//string sql = "SELECT distinct No,Name, ParentNo FROM Port_Dept where No='null'";
		//DataTable dtDept = DBAccess.RunSQLReturnTable(sql);
		//dtDept.TableName = "Depts";
		//ds.Tables.add(dtDept);

		return ds;
	}
}