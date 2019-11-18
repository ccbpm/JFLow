package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.WF.Glo;
import BP.Web.WebUser;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 抄送
*/
public class CC extends Entity
{

		///#region 属性
	/** 
	 抄送
	 
	 @param rpt
	 @return 
	 * @throws Exception 
	*/
	public final DataTable GenerCCers(Entity rpt, long workid) throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add(new DataColumn("No", String.class));
		dt.Columns.Add(new DataColumn("Name", String.class));

		DataTable mydt = new DataTable();
		String sql = "";
		if (this.getCCIsDepts() == true)
		{
			/*如果抄送到部门. */
			if (Glo.getOSModel() == BP.Sys.OSModel.OneOne)
			{
				sql = "SELECT A.No, A.Name FROM Port_Emp A, WF_CCDept B WHERE  A.FK_Dept=B.FK_Dept AND B.FK_Node=" + this.getNodeID();
			}
			else
			{
				sql = "SELECT A.No, A.Name FROM Port_Emp A, WF_CCDept B  WHERE  B.FK_Dept=A.FK_Dept AND B.FK_Node=" + this.getNodeID();
			}

			mydt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow mydr : mydt.Rows)
			{
				DataRow dr = dt.NewRow();
				dr.setValue("No", mydr.getValue("No"));
				dr.setValue("Name", mydr.getValue("Name"));
				dt.Rows.add(dr);
			}
		}

		if (this.getCCIsEmps() == true)
		{
			/*如果抄送到人员. */
			sql = "SELECT A.No, A.Name FROM Port_Emp A, WF_CCEmp B WHERE A.No=B.FK_Emp AND B.FK_Node=" + this.getNodeID();
			mydt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow mydr : mydt.Rows)
			{
				DataRow dr = dt.NewRow();
				dr.setValue("No", mydr.getValue("No"));
				dr.setValue("Name", mydr.getValue("Name"));
				dt.Rows.add(dr);
			}
		}

		if (this.getCCIsStations() == true)
		{
			if (this.getCCStaWay() == CCStaWay.StationOnly)
			{

					sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C  WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Node=" + this.getNodeID();

				mydt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow mydr : mydt.Rows)
				{
					DataRow dr = dt.NewRow();
					dr.setValue("No", mydr.getValue("No"));
					dr.setValue("Name", mydr.getValue("Name"));
					dt.Rows.add(dr);
				}
			}

			if (this.getCCStaWay() == CCStaWay.StationSmartCurrNodeWorker || this.getCCStaWay() == CCStaWay.StationSmartNextNodeWorker)
			{
				/*按岗位智能计算*/
				String deptNo = "";
				if (this.getCCStaWay() == CCStaWay.StationSmartCurrNodeWorker)
				{
					deptNo = WebUser.getFK_Dept();
				}
				else
				{
					deptNo = DBAccess.RunSQLReturnStringIsNull("SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID=" + workid + " AND IsEnable=1 AND IsPass=0", WebUser.getFK_Dept());
				}


					sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node=" + this.getNodeID() + " AND B.FK_Dept='" + deptNo + "'";

				mydt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow mydr : mydt.Rows)
				{
					DataRow dr = dt.NewRow();
					dr.setValue("No", mydr.getValue("No"));
					dr.setValue("Name", mydr.getValue("Name"));
					dt.Rows.add(dr);
				}
			}

			if (this.getCCStaWay() == CCStaWay.StationAdndDept)
			{

					sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C, WF_CCDept D WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND A.FK_Dept=D.FK_Dept AND B.FK_Dept=D.FK_Dept AND C.FK_Node=" + this.getNodeID() + " AND D.FK_Node=" + this.getNodeID();

				mydt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow mydr : mydt.Rows)
				{
					DataRow dr = dt.NewRow();
					dr.setValue("No", mydr.getValue("No"));
					dr.setValue("Name", mydr.getValue("Name"));
					dt.Rows.add(dr);
				}
			}

			if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelCurrNodeWorker || this.getCCStaWay() == CCStaWay.StationDeptUpLevelNextNodeWorker)
			{
				// 求当事人的部门编号.
				String deptNo = "";

				if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelCurrNodeWorker)
				{
					deptNo = WebUser.getFK_Dept();
				}

				if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelNextNodeWorker)
				{
					deptNo = DBAccess.RunSQLReturnStringIsNull("SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID=" + workid + " AND IsEnable=1 AND IsPass=0", WebUser.getFK_Dept());
				}

				while (true)
				{
					BP.Port.Dept dept = new Dept(deptNo);


						sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node=" + this.getNodeID() + " AND B.FK_Dept='" + deptNo + "'";

					mydt = DBAccess.RunSQLReturnTable(sql);
					for (DataRow mydr : mydt.Rows)
					{
						DataRow dr = dt.NewRow();
						dr.setValue("No", mydr.getValue("No"));
						dr.setValue("Name", mydr.getValue("Name"));
						dt.Rows.add(dr);
					}

					if (dept.getParentNo().equals("0"))
					{
						break;
					}

					deptNo = dept.getParentNo();
				}
			}
		}

		if (this.getCCIsSQLs() == true)
		{
			Object tempVar = this.getCCSQL();
			sql = tempVar instanceof String ? (String)tempVar : null;
			sql = sql.replace("@WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			if (sql.contains("@") == true)
			{
				sql = BP.WF.Glo.DealExp(sql, rpt, null);
			}

			/*按照SQL抄送. */
			mydt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow mydr : mydt.Rows)
			{
				DataRow dr = dt.NewRow();
				dr.setValue("No", mydr.getValue("No"));
				dr.setValue("Name", mydr.getValue("Name"));
				dt.Rows.add(dr);
			}
		}
		//将dt中的重复数据过滤掉  
//		DataView myDataView = new DataView(dt);
//		//此处可加任意数据项组合  
//		String[] strComuns = {"No", "Name"};
//		dt = myDataView.ToTable(true, strComuns);

		return dt;
	}
	/** 
	节点ID
	 * @throws Exception 
	*/
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (!WebUser.getNo().equals("admin"))
		{
			uac.IsView = false;
			return uac;
		}
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 抄送标题
	 * @throws Exception 
	*/
	public final String getCCTitle() throws Exception
	{
		String s = this.GetValStringByKey(CCAttr.CCTitle);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "来自@Rec的抄送信息.";
		}
		return s;
	}
	public final void setCCTitle(String value) throws Exception
	{
		this.SetValByKey(CCAttr.CCTitle, value);
	}
	/** 
	 抄送内容
	 * @throws Exception 
	*/
	public final String getCCDoc() throws Exception
	{
		String s = this.GetValStringByKey(CCAttr.CCDoc);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "来自@Rec的抄送信息.";
		}
		return s;
	}
	public final void setCCDoc(String value) throws Exception
	{
		this.SetValByKey(CCAttr.CCDoc, value);
	}
	/** 
	 抄送对象
	 * @throws Exception 
	*/
	public final String getCCSQL() throws Exception
	{
		String sql = this.GetValStringByKey(CCAttr.CCSQL);
		sql = sql.replace("~", "'");
		sql = sql.replace("‘", "'");
		sql = sql.replace("’", "'");
		sql = sql.replace("''", "'");
		return sql;
	}
	public final void setCCSQL(String value) throws Exception
	{
		this.SetValByKey(CCAttr.CCSQL, value);
	}
	/** 
	 是否启用按照岗位抄送
	 * @throws Exception 
	*/
	public final boolean getCCIsStations() throws Exception
	{
		return this.GetValBooleanByKey(CCAttr.CCIsStations);
	}
	public final void setCCIsStations(boolean value) throws Exception
	{
		this.SetValByKey(CCAttr.CCIsStations, value);
	}
	/** 
	 抄送到岗位计算方式.
	 * @throws Exception 
	*/
	public final CCStaWay getCCStaWay() throws Exception
	{
		return CCStaWay.forValue(this.GetValIntByKey(CCAttr.CCStaWay));
	}
	public final void setCCStaWay(CCStaWay value) throws Exception
	{
		this.SetValByKey(CCAttr.CCStaWay, value.getValue());
	}
	/** 
	 是否启用按照部门抄送
	 * @throws Exception 
	*/
	public final boolean getCCIsDepts() throws Exception
	{
		return this.GetValBooleanByKey(CCAttr.CCIsDepts);
	}
	public final void setCCIsDepts(boolean value) throws Exception
	{
		this.SetValByKey(CCAttr.CCIsDepts, value);
	}
	/** 
	 是否启用按照人员抄送
	 * @throws Exception 
	*/
	public final boolean getCCIsEmps() throws Exception
	{
		return this.GetValBooleanByKey(CCAttr.CCIsEmps);
	}
	public final void setCCIsEmps(boolean value) throws Exception
	{
		this.SetValByKey(CCAttr.CCIsEmps, value);
	}
	/** 
	 是否启用按照SQL抄送
	 * @throws Exception 
	*/
	public final boolean getCCIsSQLs() throws Exception
	{
		return this.GetValBooleanByKey(CCAttr.CCIsSQLs);
	}
	public final void setCCIsSQLs(boolean value) throws Exception
	{
		this.SetValByKey(CCAttr.CCIsSQLs, value);
	}


		///#endregion


		///#region 构造函数
	/** 
	 抄送设置
	*/
	public CC()
	{
	}
	/** 
	 抄送设置
	 
	 @param nodeid
	 * @throws Exception 
	*/
	public CC(int nodeid) throws Exception
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
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "抄送规则");

		map.Java_SetEnType(EnType.Admin);

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10, false);
		map.AddTBString(NodeAttr.FK_Flow, null, "FK_Flow", false, false, 0, 3, 10);
		map.AddDDLSysEnum(NodeAttr.CCWriteTo, 0, "抄送数据写入规则", true, true, NodeAttr.CCWriteTo,"@0=写入抄送列表@1=写入待办@2=写入待办与抄送列表");
		map.AddBoolean(CCAttr.CCIsAttr, false, "按表单字段抄送", true, true, true);
		map.AddTBString(CCAttr.CCFormAttr, null, "抄送人员字段", true, false, 0, 100, 10, true);

		map.AddBoolean(CCAttr.CCIsStations, false, "是否启用？-按照岗位抄送", true, true, false);
		map.AddDDLSysEnum(CCAttr.CCStaWay, 0, "抄送岗位计算方式", true, true, CCAttr.CCStaWay, "@0=仅按岗位计算@1=按岗位智能计算(当前节点)@2=按岗位智能计算(发送到节点)@3=按岗位与部门的交集@4=按直线上级部门找岗位下的人员(当前节点)@5=按直线上级部门找岗位下的人员(接受节点)");

		map.AddBoolean(CCAttr.CCIsDepts, false, "是否启用？-按照部门抄送", true, true, false);
		map.AddBoolean(CCAttr.CCIsEmps, false, "是否启用？-按照人员抄送", true, true, false);
		map.AddBoolean(CCAttr.CCIsSQLs, false, "是否启用？-按照SQL抄送", true, true, true);
		map.AddTBString(CCAttr.CCSQL, null, "SQL表达式", true, false, 0, 200, 10, true);

		map.AddTBString(CCAttr.CCTitle, null, "抄送标题", true, false, 0, 100, 10, true);
		map.AddTBStringDoc(CCAttr.CCDoc, null, "抄送内容(标题与内容支持变量)", true, false, true);



			///#region 对应关系
			// 相关功能。

			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new BP.WF.Template.CCStations(), new BP.WF.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, "抄送岗位(AddGroupPanelModel)", StationAttr.FK_StationType);

		map.getAttrsOfOneVSM().AddGroupListModel(new BP.WF.Template.CCStations(), new BP.WF.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, "抄送岗位(AddGroupListModel)", StationAttr.FK_StationType);


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranches(new BP.WF.Template.CCDepts(), new BP.Port.Depts(), BP.WF.Template.NodeDeptAttr.FK_Node, BP.WF.Template.NodeDeptAttr.FK_Dept, "抄送部门AddBranches", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new BP.WF.Template.CCEmps(), new BP.Port.Emps(), BP.WF.Template.NodeEmpAttr.FK_Node, BP.WF.Template.NodeEmpAttr.FK_Emp, "抄送接受人(AddBranchesAndLeaf)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");


			///#endregion 对应关系

			//// 相关功能。
			//map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCStations(), new BP.WF.Port.Stations(),
			//    NodeStationAttr.FK_Node, NodeStationAttr.FK_Station,
			//    DeptAttr.Name, DeptAttr.No, "抄送岗位");

			//map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCDepts(), new BP.WF.Port.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name,
			//DeptAttr.No,  "抄送部门" );

			//map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCEmps(), new BP.WF.Port.Emps(), NodeEmpAttr.FK_Node, NodeEmpAttr.FK_Emp, DeptAttr.Name,
			//    DeptAttr.No, "抄送人员");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}