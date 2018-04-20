package BP.WF.Template;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;
import BP.Port.Dept;
import BP.Port.DeptAttr;
import BP.Sys.OSModel;
import BP.Tools.StringHelper;
import BP.WF.CCStaWay;
import BP.WF.Glo;

/** 
 抄送
 
*/
public class CC extends Entity
{

		
	/** 
	 抄送
	 @param rpt
	 @return 
	 * @throws Exception 
	*/
	 public DataTable GenerCCers(Entity rpt, long workid) throws Exception
     {
         DataTable dt = new DataTable();
         dt.Columns.add(new DataColumn("No"));
         dt.Columns.add(new DataColumn("Name"));

         DataTable mydt = new DataTable();
         String sql = "";
         if (this.getCCIsDepts() == true)
         {
             /*如果抄送到部门. */
             if (Glo.getOSModel() == BP.Sys.OSModel.OneOne)
                 sql = "SELECT A.No, A.Name FROM Port_Emp A, WF_CCDept B WHERE  A.FK_Dept=B.FK_Dept AND B.FK_Node=" + this.getNodeID();
             else
                 sql = "SELECT A.No, A.Name FROM Port_Emp A, WF_CCDept B WHERE  A.No=C.No AND B.FK_Node=" + this.getNodeID();

             //

             mydt = DBAccess.RunSQLReturnTable(sql);
             for(DataRow mydr : mydt.Rows)
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
             for(DataRow mydr : mydt.Rows)
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
                 /* 如果抄送到岗位. */
                 if (Glo.getOSModel() == OSModel.OneOne)
                     sql = "SELECT No,Name FROM Port_Emp A, Port_EmpStation B, WF_CCStation C  WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Node="+this.getNodeID();
                 else
                     sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C  WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND C.FK_Node=" + this.getNodeID();

                 mydt = DBAccess.RunSQLReturnTable(sql);
                 for(DataRow mydr : mydt.Rows)
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
                     deptNo = BP.Web.WebUser.getFK_Dept();
                 else
                     deptNo = DBAccess.RunSQLReturnStringIsNull("SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID=" + workid + " AND IsEnable=1 AND IsPass=0", BP.Web.WebUser.getFK_Dept());

                 /* 抄送到当前登陆人. */
                 if (Glo.getOSModel() == OSModel.OneOne)
                     sql = "SELECT No,Name FROM Port_Emp A, Port_EmpStation B, WF_CCStation C WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node=" + this.getNodeID() + " AND A.FK_Dept='" + deptNo + "'";
                 else
                     sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node=" + this.getNodeID() + " AND B.FK_Dept='" + deptNo + "'";

                 mydt = DBAccess.RunSQLReturnTable(sql);
                 for(DataRow mydr : mydt.Rows)
                 {
                     DataRow dr = dt.NewRow();
                     dr.setValue("No", mydr.getValue("No"));
                     dr.setValue("Name", mydr.getValue("Name"));
                     dt.Rows.add(dr);
                 }
             }

             if (this.getCCStaWay() == CCStaWay.StationAdndDept)
             {
                 /* 如果抄送到岗位与部门的交集. */
                 if (Glo.getOSModel() == OSModel.OneOne)
                     sql = "SELECT No,Name FROM Port_Emp A, Port_EmpStation B, WF_CCStation C, WF_CCDept D WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND A.FK_Dept=D.FK_Dept AND C.FK_Node=D.FK_Node AND D.FK_Node="+this.getNodeID();
                 else
                     sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C, WF_CCDept D WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station AND A.FK_Dept=D.FK_Dept AND B.FK_Dept=D.FK_Dept AND C.FK_Node="+this.getNodeID()+" AND D.FK_Node="+this.getNodeID();

                 mydt = DBAccess.RunSQLReturnTable(sql);
                 for(DataRow mydr : mydt.Rows)
                 {
                     DataRow dr = dt.NewRow();
                     dr.setValue("No", mydr.getValue("No"));
                     dr.setValue("Name", mydr.getValue("Name"));
                     dt.Rows.add(dr);
                 }
             }

             if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelCurrNodeWorker ||
                 this.getCCStaWay() == CCStaWay.StationDeptUpLevelNextNodeWorker )
             {
                 // 求当事人的部门编号.
                 String deptNo = "";

                 if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelCurrNodeWorker)
                     deptNo = BP.Web.WebUser.getFK_Dept();

                 if (this.getCCStaWay() == CCStaWay.StationDeptUpLevelNextNodeWorker)
                     deptNo = DBAccess.RunSQLReturnStringIsNull("SELECT FK_Dept FROM WF_GenerWorkerlist WHERE WorkID="+workid+" AND IsEnable=1 AND IsPass=0", BP.Web.WebUser.getFK_Dept());

                 while (true)
                 {
                     BP.Port.Dept dept = new Dept(deptNo);

                     /* 抄送到当前登陆人. */
                     if (Glo.getOSModel() == OSModel.OneOne)
                         sql = "SELECT No,Name FROM Port_Emp A, Port_EmpStation B, WF_CCStation C WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node="+this.getNodeID()+" AND A.FK_Dept='"+deptNo+"'";
                     else
                         sql = "SELECT No,Name FROM Port_Emp A, Port_DeptEmpStation B, WF_CCStation C WHERE A.No=B.FK_Emp AND B.FK_Station=C.FK_Station  AND C.FK_Node="+this.getNodeID()+" AND B.FK_Dept='"+deptNo+"'";

                     mydt = DBAccess.RunSQLReturnTable(sql);
                     for(DataRow mydr : mydt.Rows)
                     {
                         DataRow dr = dt.NewRow();
                         dr.setValue("No", mydr.getValue("No"));
                         dr.setValue("Name", mydr.getValue("Name"));
                         dt.Rows.AddRow(dr);
                     }

                     if ("0".equals(dept.getParentNo()))
                         break;

                     deptNo = dept.getParentNo();
                 }
             }
         }

         if (this.getCCIsSQLs() == true)
         {
             sql = this.getCCSQL();
             sql = sql.replace("@WebUser.No", BP.Web.WebUser.getNo());
             sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
             sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
             if (sql.contains("@") == true)
                 sql = BP.WF.Glo.DealExp(sql, rpt, null);

             /*按照SQL抄送. */
             mydt = DBAccess.RunSQLReturnTable(sql);
             for(DataRow mydr : mydt.Rows)
             {
                 DataRow dr = dt.NewRow();
                 dr.setValue("No", mydr.getValue("No"));
                 dr.setValue("Name", mydr.getValue("Name"));
                 dt.Rows.add(dr);
             }
         }
        /* //将dt中的重复数据过滤掉  
         DataView myDataView = new DataView(dt);
         //此处可加任意数据项组合  
         String[] strComuns = { "No","Name" };
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
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if ( ! BP.Web.WebUser.getNo().equals("admin"))
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
	public final String getCCTitle()
	{
		String s= this.GetValStringByKey(CCAttr.CCTitle);
		if (StringHelper.isNullOrEmpty(s))
		{
			s = "来自@Rec的抄送信息.";
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
	public final String getCCDoc()
	{
		String s = this.GetValStringByKey(CCAttr.CCDoc);
		if (StringHelper.isNullOrEmpty(s))
		{
			s = "来自@Rec的抄送信息.";
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
	public final String getCCSQL()
	{
		String sql= this.GetValStringByKey(CCAttr.CCSQL);
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
	 *  抄送到岗位计算方式.
	 */
    public CCStaWay getCCStaWay()
    {
       return CCStaWay.forValue(this.GetValIntByKey(CCAttr.CCStaWay));
    }
    public final void setCCStaWay(int value)
    {   
    	this.SetValByKey(CCAttr.CCStaWay,value);
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
	 抄送方式
	*/
	public final CtrlWay getCCCtrlWay()
	{
		return CtrlWay.forValue(this.GetValIntByKey(CCAttr.CCCtrlWay));
	}
	public final void setCCCtrlWay(CtrlWay value)
	{
		this.SetValByKey(CCAttr.CCCtrlWay, value);
	}
	/** 
	 CC
	*/
	public CC()
	{
	}
	
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

		map.AddBoolean(CCAttr.CCIsStations, false, "按照岗位抄送", true, true, true);
		map.AddBoolean(CCAttr.CCIsDepts, false, "按照部门抄送", true, true, true);
		map.AddBoolean(CCAttr.CCIsEmps, false, "按照人员抄送", true, true, true);
		map.AddBoolean(CCAttr.CCIsSQLs, false, "按照SQL抄送", true, true, true);

		//map.AddDDLSysEnum(CCAttr.CCCtrlWay, 0, "控制方式",true, true,"CtrlWay");

		map.AddTBString(CCAttr.CCSQL, null, "SQL表达式", true, false, 0, 100, 10, true);
		map.AddTBString(CCAttr.CCTitle, null, "抄送标题", true, false, 0, 100, 10,true);
		map.AddTBStringDoc(CCAttr.CCDoc, null, "抄送内容(标题与内容支持变量)", true, false,true);

		//map.AddSearchAttr(CCAttr.CCCtrlWay);

			// 相关功能。
		map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCStations(), new BP.WF.Port.Stations(), NodeStationAttr.FK_Node, NodeStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "抄送岗位");

		map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCDepts(), new BP.WF.Port.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "抄送部门");

		map.getAttrsOfOneVSM().Add(new BP.WF.Template.CCEmps(), new BP.WF.Port.Emps(), NodeEmpAttr.FK_Node, NodeEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "抄送人员");

		this.set_enMap(map);
		return this.get_enMap();
	}
}