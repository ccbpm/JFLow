package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 选择器
*/
public class Selector extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	@Override
	public String getPK()
	{
		return "NodeID";
	}
	/** 
	 选择模式
	*/
	public final SelectorModel getSelectorModel()
	{
		return SelectorModel.forValue(this.GetValIntByKey(SelectorAttr.SelectorModel));
	}
	public final void setSelectorModel(SelectorModel value)
	{
		this.SetValByKey(SelectorAttr.SelectorModel, value.getValue());
	}
	/** 
	 分组数据源
	*/
	public final String getSelectorP1()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP1);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP1(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP1, value);
	}
	/** 
	 实体数据源
	*/
	public final String getSelectorP2()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP2);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP2(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP2, value);
	}
	/** 
	 默认选择数据源
	*/
	public final String getSelectorP3()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP3);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP3(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP3, value);
	}
	/** 
	 强制选择数据源
	*/
	public final String getSelectorP4()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP4);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP4(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP3, value);
	}
	/** 
	 是否自动装载上一笔加载的数据
	*/
	public final boolean getIsAutoLoadEmps()
	{
		return this.GetValBooleanByKey(SelectorAttr.IsAutoLoadEmps);
	}
	public final void setIsAutoLoadEmps(boolean value)
	{
		this.SetValByKey(SelectorAttr.IsAutoLoadEmps, value);
	}
	/** 
	 是否单选？
	*/
	public final boolean getIsSimpleSelector()
	{
		return this.GetValBooleanByKey(SelectorAttr.IsSimpleSelector);
	}
	public final void setIsSimpleSelector(boolean value)
	{
		this.SetValByKey(SelectorAttr.IsSimpleSelector, value);
	}
	/** 
	 是否启用部门搜索范围限定
	*/
	public final boolean getIsEnableDeptRange()
	{
		return this.GetValBooleanByKey(SelectorAttr.IsEnableDeptRange);
	}
	public final void setIsEnableDeptRange(boolean value)
	{
		this.SetValByKey(SelectorAttr.IsEnableDeptRange, value);
	}
	/** 
	 是否启用岗位搜索范围限定
	*/
	public final boolean getIsEnableStaRange()
	{
		return this.GetValBooleanByKey(SelectorAttr.IsEnableStaRange);
	}
	public final void setIsEnableStaRange(boolean value)
	{
		this.SetValByKey(SelectorAttr.IsEnableStaRange, value);
	}
	/** 
	 节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(SelectorAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(SelectorAttr.NodeID, value);
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		if (BP.Web.WebUser.No.equals("admin"))
		{
			uac.IsUpdate = true;
			uac.IsView = true;
		}

		uac.IsUpdate = true;

		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 接受人选择器
	*/
	public Selector()
	{
	}
	/** 
	 接受人选择器
	 
	 @param nodeid
	*/
	public Selector(int nodeid)
	{
		this.setNodeID(nodeid);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段.
		Map map = new Map("WF_Node", "选择器");

		map.Java_SetDepositaryOfEntity(Depositary.Application);

		map.AddTBIntPK(SelectorAttr.NodeID, 0, "NodeID", true, true);
		map.AddTBString(SelectorAttr.Name, null, "节点名称", true, true, 0, 100, 100);

		map.AddDDLSysEnum(SelectorAttr.SelectorModel, 5, "显示方式", true, true, SelectorAttr.SelectorModel, "@0=按岗位@1=按部门@2=按人员@3=按SQL@4=按SQL模版计算@5=使用通用人员选择器@6=部门与岗位的交集@7=自定义Url@8=使用通用部门岗位人员选择器@9=按岗位智能计算(操作员所在部门)");

		map.AddDDLSQL(SelectorAttr.FK_SQLTemplate, null, "SQL模版", "SELECT No,Name FROM WF_SQLTemplate WHERE SQLType=5", true);

		map.AddBoolean(SelectorAttr.IsAutoLoadEmps, true, "是否自动加载上一次选择的人员？", true, true);
		map.AddBoolean(SelectorAttr.IsSimpleSelector, false, "是否单项选择(只能选择一个人)？", true, true);
		map.AddBoolean(SelectorAttr.IsEnableDeptRange, false, "是否启用部门搜索范围限定(对使用通用人员选择器有效)？", true, true, true);
		map.AddBoolean(SelectorAttr.IsEnableStaRange, false, "是否启用岗位搜索范围限定(对使用通用人员选择器有效)？", true, true, true);


			//     map.AddDDLSysEnum(SelectorAttr.IsMinuesAutoLoadEmps, 5, "接收人选择方式", true, true, SelectorAttr.SelectorModel,
			//   "@0=按岗位@1=按部门@2=按人员@3=按SQL@4=按SQL模版计算@5=使用通用人员选择器@6=部门与岗位的交集@7=自定义Url");

		map.AddTBStringDoc(SelectorAttr.SelectorP1, null, "分组参数:可以为空,比如:SELECT No,Name,ParentNo FROM  Port_Dept", true,false,0,300,100,3);
		map.AddTBStringDoc(SelectorAttr.SelectorP2, null, "操作员数据源:比如:SELECT No,Name,FK_Dept FROM  Port_Emp", true, false, 0, 300, 100, 3);

		map.AddTBStringDoc(SelectorAttr.SelectorP3, null, "默认选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID", true, false, 0, 300, 100, 3);
		map.AddTBStringDoc(SelectorAttr.SelectorP4, null, "强制选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID", true, false, 0, 300, 100, 3);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 对应关系
			//平铺模式.
		map.AttrsOfOneVSM.AddGroupPanelModel(new BP.WF.Template.NodeStations(), new BP.WF.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, "绑定岗位(平铺)", StationAttr.FK_StationType, "Name", "No");

		map.AttrsOfOneVSM.AddGroupListModel(new BP.WF.Template.NodeStations(), new BP.WF.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, "绑定岗位(树)", StationAttr.FK_StationType, "Name", "No");

			//节点绑定部门. 节点绑定部门.
		map.AttrsOfOneVSM.AddBranches(new BP.WF.Template.NodeDepts(), new BP.Port.Depts(), BP.WF.Template.NodeDeptAttr.FK_Node, BP.WF.Template.NodeDeptAttr.FK_Dept, "绑定部门", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.AttrsOfOneVSM.AddBranchesAndLeaf(new BP.WF.Template.NodeEmps(), new BP.Port.Emps(), BP.WF.Template.NodeEmpAttr.FK_Node, BP.WF.Template.NodeEmpAttr.FK_Emp, "绑定接受人", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion



		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 产生数据.
	 
	 @return 
	*/
	public final System.Data.DataSet GenerDataSet(int nodeid, Entity en)
	{
		DataSet ds = null;
		switch (this.getSelectorModel())
		{
			case Dept:
				ds = ByDept(nodeid, en);
				break;
			case Emp:
				ds = ByEmp(nodeid);
				break;
			case Station:
				ds = ByStation(nodeid, en);
				break;
			case DeptAndStation:
				ds = DeptAndStation(nodeid);
				break;
			case SQL:
				ds = BySQL(nodeid, en);
				break;
			case GenerUserSelecter:
				ds = ByGenerUserSelecter();
				break;
			case AccepterOfDeptStationOfCurrentOper:
				ds = AccepterOfDeptStationOfCurrentOper(nodeid, en);
				break;
			default:
				throw new RuntimeException("@错误:没有判断的选择类型:" + this.getSelectorModel());
				break;
		}

		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			for (DataTable dt : ds.Tables)
			{
				for (int i = 0; i < dt.Columns.Count; i++)
				{
					if (dt.Columns[i].ColumnName.toUpperCase().equals("NO"))
					{
						dt.Columns[i].ColumnName = "No";
					}

					if (dt.Columns[i].ColumnName.toUpperCase().equals("NAME"))
					{
						dt.Columns[i].ColumnName = "Name";
					}

					if (dt.Columns[i].ColumnName.toUpperCase().equals("PARENTNO"))
					{
						dt.Columns[i].ColumnName = "ParentNo";
					}

					if (dt.Columns[i].ColumnName.toUpperCase().equals("FK_DEPT"))
					{
						dt.Columns[i].ColumnName = "FK_Dept";
					}
				}
			}
		}

		ds.Tables.Add(this.ToDataTableField("Selector"));

		return ds;
	}
	/** 
	 通用
	 
	 @return 
	*/
	private DataSet ByGenerUserSelecter()
	{
		DataSet ds = new DataSet();

		////排序.
		//string orderByDept = "";
		//if (DBAccess.IsExitsTableCol("Port_Dept", "Idx"))
		//    orderByDept = " ORDER BY Port_Dept.Idx";

		//string orderByEmp = "";
		//if (DBAccess.IsExitsTableCol("Port_Emp", "Idx"))
		//    orderByDept = " ORDER BY Port_Emp.FK_Dept, Port_Emp.Idx";

		//部门
		String sql = "SELECT distinct No,Name, ParentNo FROM Port_Dept ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.Add(dt);

		//人员.
		sql = "SELECT distinct No, Name, FK_Dept FROM Port_Emp ";
		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.Add(dtEmp);

		return ds;
	}
	/** 
	 按照SQL计算.
	 
	 @param nodeID 节点ID
	 @return 返回值
	*/
	private DataSet BySQL(int nodeID, Entity en)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		//求部门.
		String sqlGroup = this.getSelectorP1();
		if (DataType.IsNullOrEmpty(sqlGroup) == false)
		{
			sqlGroup = BP.WF.Glo.DealExp(sqlGroup, en, null); //@祝梦娟
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlGroup);
			dt.TableName = "Depts";
			ds.Tables.Add(dt);
		}

		//求人员范围.
		String sqlDB = this.getSelectorP2();
		sqlDB = BP.WF.Glo.DealExp(sqlDB, en, null); //@祝梦娟

		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sqlDB);
		dtEmp.TableName = "Emps";
		ds.Tables.Add(dtEmp);

		//求默认选择的数据.
		if (!this.getSelectorP3().equals(""))
		{
			sqlDB = this.getSelectorP3();
			sqlDB = BP.WF.Glo.DealExp(sqlDB, en, null); //@祝梦娟

			DataTable dtDef = BP.DA.DBAccess.RunSQLReturnTable(sqlDB);
			dtDef.TableName = "DefaultSelected";

			ds.Tables.Add(dtDef);
		}


		//求强制选择的数据源.
		if (!this.getSelectorP4().equals(""))
		{
			sqlDB = this.getSelectorP4();

			sqlDB = sqlDB.replace("@WebUser.No", WebUser.No);
			sqlDB = sqlDB.replace("@WebUser.Name", WebUser.Name);
			sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.FK_Dept);

			sqlDB = sqlDB.replace("@WorkID", en.GetValStringByKey("OID"));
			sqlDB = sqlDB.replace("@OID", en.GetValStringByKey("OID"));

			if (sqlDB.contains("@"))
			{
				sqlDB = BP.WF.Glo.DealExp(sqlDB, en, null);
			}

			DataTable dtForce = BP.DA.DBAccess.RunSQLReturnTable(sqlDB);
			dtForce.TableName = "ForceSelected";
			ds.Tables.Add(dtForce);
		}

		return ds;
	}

	/** 
	 按照部门获取部门人员树.
	 
	 @param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByDept(int nodeID, Entity en)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;

		Node nd = new Node(nodeID);
		if (nd.getHisDeliveryWay() == DeliveryWay.BySelectedForPrj)
		{
			//部门.
			sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a,  WF_NodeDept b, WF_PrjEmp C,Port_DeptEmp D WHERE A.No=B.FK_Dept AND B.FK_Node=" + nodeID + " AND C.FK_Prj='" + en.GetValStrByKey("PrjNo") + "' ";
			sql += "  AND C.FK_Emp=D.FK_Emp ";


			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.Add(dt);

			//人员.
			sql = "SELECT distinct a.No, a.Name, a.FK_Dept FROM Port_Emp a, WF_NodeDept b, WF_PrjEmp C WHERE a.FK_Dept=b.FK_Dept  AND A.No=C.FK_Emp  AND B.FK_Node=" + nodeID + " AND C.FK_Prj='" + en.GetValStrByKey("PrjNo") + "'  ";
			dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
			ds.Tables.Add(dtEmp);
			dtEmp.TableName = "Emps";
			return ds;
		}


		//部门.
		sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a,  WF_NodeDept b WHERE a.No=b.FK_Dept AND B.FK_Node=" + nodeID + " ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.Add(dt);

		//人员.
		sql = "SELECT distinct a.No, a.Name, a.FK_Dept FROM Port_Emp a, WF_NodeDept b WHERE a.FK_Dept=b.FK_Dept AND B.FK_Node=" + nodeID + " ";
		dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		ds.Tables.Add(dtEmp);
		dtEmp.TableName = "Emps";
		return ds;
	}
	/** 
	 按照Emp获取部门人员树.
	 
	 @param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByEmp(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();


		//部门.
		String sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a, WF_NodeEmp b, Port_Emp c WHERE b.FK_Emp=c.No AND a.No=c.FK_Dept AND B.FK_Node=" + nodeID + " ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.Add(dt);

		//人员.
		sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a, WF_NodeEmp b WHERE a.No=b.FK_Emp AND b.FK_Node=" + nodeID + " ";

		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.Add(dtEmp);
		return ds;
	}

	private DataSet AccepterOfDeptStationOfCurrentOper(int nodeID, Entity en)
	{

		// 定义数据容器.
		DataSet ds = new DataSet();


		//部门.
		String sql = "";
		sql = "SELECT d.No,d.Name,d.ParentNo  FROM  Port_DeptEmp  de,port_dept as d where de.FK_Dept = d.No and de.FK_Emp = '" + BP.Web.WebUser.No + "'";

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		//人员.
		if (SystemConfig.AppCenterDBType == DBType.Oracle)
		{
			sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Dept='" + WebUser.FK_Dept + "' AND b.FK_Node=" + nodeID + ")  ORDER BY A.Idx ";
		}
		else
		{
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND C.FK_Dept='" + WebUser.FK_Dept + "' AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
		}

		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dtEmp.Rows.Count > 0)
		{
			dt.TableName = "Depts";
			ds.Tables.Add(dt);

			dtEmp.TableName = "Emps";
			ds.Tables.Add(dtEmp);
		}
		else //如果没人，就查询父级
		{
			//查询当前节点的workdID
			long workID = Long.parseLong(en.GetValStringByKey("OID"));
			BP.WF.WorkNode node = new WorkNode(workID, nodeID);

			sql = " select No,Name, ParentNo from port_dept where no  in (  select  ParentNo from port_dept where no  in"
			+ "( SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID ='" + workID + "' ))";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.Add(dt);

			// 如果当前的节点不是开始节点， 从轨迹里面查询。
			sql = "SELECT DISTINCT b.No,b.Name,b.FK_Dept   FROM " + BP.WF.Glo.getEmpStation() + " a,Port_Emp b  WHERE FK_Station IN "
			   + "( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nodeID + ") "
			   + "AND a.FK_Dept IN (SELECT ParentNo FROM Port_Dept WHERE No in (SELECT FK_DEPT FROM WF_GenerWorkerlist WHERE WorkID=" + workID + "))"
			   + " AND a.FK_Emp = b.No ";
			sql += " ORDER BY b.No ";

			dtEmp = DBAccess.RunSQLReturnTable(sql);
			dtEmp.TableName = "Emps";
			ds.Tables.Add(dtEmp);
		}
		return ds;
	}
	private DataSet DeptAndStation(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();


		//部门.
		String sql = "";

		if (SystemConfig.AppCenterDBType == DBType.Oracle)
		{
			sql = "SELECT * FROM (SELECT distinct a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID + ")  AS Port_Dept  ";
		}
		else
		{
			sql = "SELECT distinct a.No, a.Name, a.ParentNo FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID + " ";
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.Add(dt);



		//人员.

		if (SystemConfig.AppCenterDBType == DBType.Oracle)
		{
			sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + ")  ";
		}
		else
		{
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx ";
		}


		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.Add(dtEmp);
		return ds;
	}

	/** 
	 按照Station获取部门人员树.
	 
	 @param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByStation(int nodeID, Entity en)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();
		String sql = null;
		DataTable dt = null;
		DataTable dtEmp = null;

		Node nd = new Node(nodeID);
		if (nd.getHisDeliveryWay() == DeliveryWay.BySelectedForPrj)
		{
			//部门.
			sql = "SELECT distinct a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d, WF_PrjEmp E WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND d.No=e.FK_Emp And C.FK_Emp=E.FK_Emp  AND B.FK_Node=" + nodeID + " AND E.FK_Prj='" + en.GetValStrByKey("PrjNo") + "' ORDER BY A.No,A.Idx";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.Add(dt);

			//人员.
			if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "Idx") == true)
				{
					sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c, WF_PrjEmp d  WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station And a.No=d.FK_Emp And C.FK_Emp=d.FK_Emp AND b.FK_Node=" + nodeID + " AND D.FK_Prj='" + en.GetValStrByKey("PrjNo") + "') ORDER BY FK_Dept,Idx,No";
				}
				else
				{
					sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c, WF_PrjEmp d  WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station And a.No=d.FK_Emp And C.FK_Emp=d.FK_Emp AND b.FK_Node=" + nodeID + " AND D.FK_Prj='" + en.GetValStrByKey("PrjNo") + "'  ORDER BY A.Idx ";
				}
			}
			else
			{
				sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c, WF_PrjEmp d WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station And a.No=d.FK_Emp And C.FK_Emp=d.FK_Emp AND b.FK_Node=" + nodeID + " AND D.FK_Prj='" + en.GetValStrByKey("PrjNo") + "'  ORDER BY A.Idx ";
			}

			dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
			ds.Tables.Add(dtEmp);
			dtEmp.TableName = "Emps";
			return ds;
		}


		//部门.
		sql = "SELECT distinct a.No, a.Name, a.ParentNo,a.Idx FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID + " ORDER BY A.No,A.Idx";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.Add(dt);

		//人员.
		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			if (DBAccess.IsExitsTableCol("Port_Emp", "Idx") == true)
			{
				sql = "SELECT * FROM (SELECT distinct a.No,a.Name, a.FK_Dept,a.Idx FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + ") ORDER BY FK_Dept,Idx,No";
			}
			else
			{
				sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
			}
		}
		else
		{
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID + "  ORDER BY A.Idx";
		}

		dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.Add(dtEmp);

		return ds;
	}
}