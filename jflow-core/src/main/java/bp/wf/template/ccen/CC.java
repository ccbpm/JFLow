package bp.wf.template.ccen;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import bp.wf.template.*;

/** 
 抄送
*/
public class CC extends Entity
{

		///#region 属性
	/** 
	 抄送
	 
	 param rpt
	 @return 
	*/
	public final DataTable GenerCCers(Entity rpt, long workid) throws Exception {
		DataTable dt = new DataTable();
		dt.Columns.Add(new DataColumn("No", String.class));
		dt.Columns.Add(new DataColumn("Name", String.class));

		DataTable mydt = new DataTable();
		String sql = "";
		if (this.getCCIsDepts() == true)
		{
			/*如果抄送到部门. */

				sql = "SELECT A." + bp.sys.base.Glo.getUserNo() + ", A.Name FROM Port_Emp A, WF_CCDept B  WHERE  B.FK_Dept=A.FK_Dept AND B.FK_Node=" + this.getNodeID();

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
			sql = "SELECT A." + bp.sys.base.Glo.getUserNo() + ", A.Name FROM Port_Emp A, WF_CCEmp B WHERE A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=B.FK_Emp AND B.FK_Node=" + this.getNodeID();
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
			if (this.getCCStaWay() == bp.wf.CCStaWay.StationOnly)
			{

					sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C  WHERE A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "= B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Node=" + this.getNodeID();

				mydt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow mydr : mydt.Rows)
				{
					DataRow dr = dt.NewRow();
					dr.setValue("No", mydr.getValue("No"));
					dr.setValue("Name", mydr.getValue("Name"));
					dt.Rows.add(dr);
				}
			}

			if (this.getCCStaWay() == bp.wf.CCStaWay.StationSmartCurrNodeWorker || this.getCCStaWay() == bp.wf.CCStaWay.StationSmartNextNodeWorker)
			{
				/*按岗位智能计算*/
				String deptNo = "";
				if (this.getCCStaWay() == bp.wf.CCStaWay.StationSmartCurrNodeWorker)
				{
					deptNo = bp.web.WebUser.getFK_Dept();
				}
				else
				{
					deptNo = DBAccess.RunSQLReturnStringIsNull("SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID=" + workid + " AND IsEnable=1 AND IsPass=0", bp.web.WebUser.getFK_Dept());
				}


					sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C WHERE A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node=" + this.getNodeID() + " AND B.FK_Dept='" + deptNo + "'";

				mydt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow mydr : mydt.Rows)
				{
					DataRow dr = dt.NewRow();
					dr.setValue("No", mydr.getValue("No"));
					dr.setValue("Name", mydr.getValue("Name"));
					dt.Rows.add(dr);
				}
			}

			if (this.getCCStaWay() == bp.wf.CCStaWay.StationAdndDept)
			{

					sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C, WF_CCDept D WHERE A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=B.FK_Emp AND B.FK_Station=C.FK_Station AND A.FK_Dept=D.FK_Dept AND B.FK_Dept=D.FK_Dept AND C.FK_Node=" + this.getNodeID() + " AND D.FK_Node=" + this.getNodeID();

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
					deptNo = bp.web.WebUser.getFK_Dept();
				}

				if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelNextNodeWorker)
				{
					deptNo = DBAccess.RunSQLReturnStringIsNull("SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID=" + workid + " AND IsEnable=1 AND IsPass=0", bp.web.WebUser.getFK_Dept());
				}

				while (true)
				{
					Dept dept = new Dept(deptNo);


						sql = "SELECT " + bp.sys.base.Glo.getUserNo() + ",Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C WHERE A." + bp.sys.base.Glo.getUserNoWhitOutAS() + "=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node=" + this.getNodeID() + " AND B.FK_Dept='" + deptNo + "'";

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
			sql = sql.replace("@WebUser.No", bp.web.WebUser.getNo());
			sql = sql.replace("@WebUser.Name", bp.web.WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", bp.web.WebUser.getFK_Dept());
			if (sql.contains("@") == true)
			{
				sql = bp.wf.Glo.DealExp(sql, rpt, null);
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
		/**按照表单字段抄送*/
		if (this.getCCIsAttr() == true)
		{
			if (DataType.IsNullOrEmpty(this.getCCFormAttr()) == true)
			{
				throw new RuntimeException("抄送规则自动抄送选择按照表单字段抄送没有设置抄送人员字段");
			}

			String[] attrs = this.getCCFormAttr().split("[,]", -1);
			for (String attr : attrs)
			{
				String ccers = rpt.GetValStrByKey(attr);
				if (DataType.IsNullOrEmpty(ccers) == false)
				{
					//判断该字段是否启用了pop返回值？
					sql = "SELECT  Tag1 AS VAL FROM Sys_FrmEleDB WHERE RefPKVal=" + workid + " AND EleID='" + attr + "'";
					DataTable dtVals = DBAccess.RunSQLReturnTable(sql);
					String emps = "";
					//获取接受人并格式化接受人, 
					if (dtVals.Rows.size() > 0)
					{
						for (DataRow dr : dtVals.Rows)
						{
							emps += dr.getValue(0).toString() + ",";
						}
					}
					else
					{
						emps = ccers;
					}
					//end判断该字段是否启用了pop返回值

					emps = emps.replace(" ", ""); //去掉空格.
					if (DataType.IsNullOrEmpty(emps) == false)
					{
						/*如果包含,; 例如 zhangsan,张三;lisi,李四;*/
						String[] ccemp = emps.split("[,]", -1);
						for (String empNo : ccemp)
						{
							if (DataType.IsNullOrEmpty(empNo) == true)
							{
								continue;
							}
							Emp emp = new Emp();
							emp.setUserID(empNo);
							if (emp.RetrieveFromDBSources() == 1)
							{
								DataRow dr = dt.NewRow();
								dr.setValue("No", empNo);
								dr.setValue("Name", emp.getName());
								dt.Rows.add(dr);
							}

						}
					}

				}
			}
		}
		/*//将dt中的重复数据过滤掉
		DataView myDataView = new DataView(dt);
		//此处可加任意数据项组合  
		String[] strComuns = {"No", "Name"};
		dt = myDataView.ToTable(true, strComuns);*/

		return dt;
	}
	/** 
	节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)
	 {
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();

		if (bp.web.WebUser.getIsAdmin() == false)
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
	*/
	public final String getCCTitle() {
		String s = this.GetValStringByKey(CCAttr.CCTitle);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "@Title";
		}
		return s;
	}
	public final void setCCTitle(String value)
	 {
		this.SetValByKey(CCAttr.CCTitle, value);
	}
	/** 
	 抄送内容
	*/
	public final String getCCDoc() {
		String s = this.GetValStringByKey(CCAttr.CCDoc);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "{@Title}";
		}
		return s;
	}
	public final void setCCDoc(String value)
	 {
		this.SetValByKey(CCAttr.CCDoc, value);
	}
	/** 
	 抄送对象
	*/
	public final String getCCSQL() {
		String sql = this.GetValStringByKey(CCAttr.CCSQL);
		sql = sql.replace("~", "'");
		sql = sql.replace("‘", "'");
		sql = sql.replace("’", "'");
		sql = sql.replace("''", "'");
		return sql;
	}
	public final void setCCSQL(String value)
	 {
		this.SetValByKey(CCAttr.CCSQL, value);
	}
	/** 
	 是否启用按照岗位抄送
	*/
	public final boolean getCCIsStations()
	{
		return this.GetValBooleanByKey(CCAttr.CCIsStations);
	}
	public final void setCCIsStations(boolean value)
	 {
		this.SetValByKey(CCAttr.CCIsStations, value);
	}
	/** 
	 抄送到岗位计算方式.
	*/
	public final CCStaWay getCCStaWay() {
		return CCStaWay.forValue(this.GetValIntByKey(CCAttr.CCStaWay));
	}
	public final void setCCStaWay(CCStaWay value)
	 {
		this.SetValByKey(CCAttr.CCStaWay, value.getValue());
	}
	/** 
	 是否启用按照部门抄送
	*/
	public final boolean getCCIsDepts()
	{
		return this.GetValBooleanByKey(CCAttr.CCIsDepts);
	}
	public final void setCCIsDepts(boolean value)
	 {
		this.SetValByKey(CCAttr.CCIsDepts, value);
	}
	/** 
	 是否启用按照人员抄送
	*/
	public final boolean getCCIsEmps()
	{
		return this.GetValBooleanByKey(CCAttr.CCIsEmps);
	}
	public final void setCCIsEmps(boolean value)
	 {
		this.SetValByKey(CCAttr.CCIsEmps, value);
	}
	/** 
	 是否启用按照SQL抄送
	*/
	public final boolean getCCIsSQLs()
	{
		return this.GetValBooleanByKey(CCAttr.CCIsSQLs);
	}
	public final void setCCIsSQLs(boolean value)
	 {
		this.SetValByKey(CCAttr.CCIsSQLs, value);
	}

	/** 
	 是否按照表单字段抄送
	*/
	public final boolean getCCIsAttr()
	{
		return this.GetValBooleanByKey(CCAttr.CCIsAttr);
	}
	public final void setCCIsAttr(boolean value)
	 {
		this.SetValByKey(CCAttr.CCIsAttr, value);
	}

	public final String getCCFormAttr()
	{
		return this.GetValStringByKey(CCAttr.CCFormAttr);
	}
	public final void setCCFormAttr(String value)
	 {
		this.SetValByKey(CCAttr.CCFormAttr, value);
	}



		///#endregion


		///#region 构造函数
	/** 
	 抄送设置
	*/
	public CC() {
	}
	/** 
	 抄送设置
	 
	 param nodeid
	*/
	public CC(int nodeid) throws Exception {
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

		Map map = new Map("WF_Node", "抄送规则");



		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10, false);
		map.AddTBString(NodeAttr.FK_Flow, null, "FK_Flow", false, false, 0, 4, 10);
		map.AddDDLSysEnum(NodeAttr.CCWriteTo, 0, "抄送数据写入规则", true, true, NodeAttr.CCWriteTo,"@0=写入抄送列表@1=写入待办@2=写入待办与抄送列表");
		map.SetHelperUrl(NodeAttr.CCWriteTo, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3580017&doc_id=31094"); //增加帮助.


		map.AddBoolean(CCAttr.CCIsAttr, false, "按表单字段抄送", true, true, true);
		map.AddTBString(CCAttr.CCFormAttr, null, "抄送人员字段", true, false, 0, 100, 10, true);

		map.AddBoolean(CCAttr.CCIsStations, false, "是否启用？-按照岗位抄送", true, true, false);
		map.AddDDLSysEnum(CCAttr.CCStaWay, 0, "抄送岗位计算方式", true, true, CCAttr.CCStaWay, "@0=仅按岗位计算@1=按岗位智能计算(当前节点)@2=按岗位智能计算(发送到节点)@3=按岗位与部门的交集@4=按直线上级部门找岗位下的人员(当前节点)@5=按直线上级部门找岗位下的人员(接受节点)");

		map.AddBoolean(CCAttr.CCIsDepts, false, "是否启用？-按照部门抄送", true, true, false);
		map.AddBoolean(CCAttr.CCIsEmps, false, "是否启用？-按照人员抄送", true, true, false);
		map.AddBoolean(CCAttr.CCIsSQLs, false, "是否启用？-按照SQL抄送", true, true, true);
		map.AddTBString(CCAttr.CCSQL, null, "SQL表达式", true, false, 0, 500, 10, true);

		map.AddTBString(CCAttr.CCTitle, null, "抄送标题", true, false, 0, 100, 10, true);
		map.AddTBStringDoc(CCAttr.CCDoc, null, "抄送内容(标题与内容支持变量)", true, false, true, 10);



			///#region 对应关系
			// 相关功能。

			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new bp.wf.template.ccen.CCStations(), new Stations(), NodeStationAttr.FK_Node, NodeStationAttr.FK_Station, "抄送岗位(分组模式)", StationAttr.FK_StationType, "Name", "No");

		map.getAttrsOfOneVSM().AddGroupListModel(new bp.wf.template.ccen.CCStations(), new Stations(), NodeStationAttr.FK_Node, NodeStationAttr.FK_Station, "抄送岗位(分组列表模式)", StationAttr.FK_StationType, "Name", "No");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranches(new bp.wf.template.ccen.CCDepts(), new Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, "抄送部门(树模式)", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept", null);


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new bp.wf.template.ccen.CCEmps(), new Emps(), NodeEmpAttr.FK_Node, NodeEmpAttr.FK_Emp, "抄送接受人(树干叶子模式)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept", null);


			///#endregion 对应关系

			//// 相关功能。
			//map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCStations(), new bp.port.Stations(),
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