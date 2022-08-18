package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

public class WF_Comm_RefFunc extends WebContralBase
{


	/** 
	 构造函数
	*/
	public WF_Comm_RefFunc() throws Exception {
	}



		///#region Dot2DotTreeDeptEmpModel.htm（部门人员选择）
	/** 
	 保存节点绑定人员信息
	 
	 @return 
	*/
	public final String Dot2DotTreeDeptEmpModel_SaveNodeEmps() throws Exception {
		JsonResultInnerData jr = new JsonResultInnerData();
		String nodeid = this.GetRequestVal("nodeid");
		String data = this.GetRequestVal("data");
		String partno = this.GetRequestVal("partno");
		boolean lastpart = false;
		int partidx = 0;
		int partcount = 0;
		int nid = 0;
		String msg = "";

		if (DataType.IsNullOrEmpty(nodeid)) throw new RuntimeException("参数nodeid不正确");
		nid = Integer.parseInt(nodeid);

		if (DataType.IsNullOrEmpty(data))
		{
			data = "";
		}

		bp.wf.template.NodeEmps nemps = new bp.wf.template.NodeEmps();
		String[] empNos = data.split(",");
		//提交内容过长时，采用分段式提交
		if (DataType.IsNullOrEmpty(partno))
		{
			nemps.Delete(bp.wf.template.NodeEmpAttr.FK_Node, nid);
		}
		else
		{
			String[] parts = partno.split(java.util.regex.Pattern.quote("/"), -1);

			if (parts.length != 2)
			{
				throw new RuntimeException("参数partno不正确");
			}

			partidx = Integer.parseInt(parts[0]);
			partcount = Integer.parseInt(parts[1]);

			empNos = data.split(",");

			if (partidx == 1)
			{
				nemps.Delete(bp.wf.template.NodeEmpAttr.FK_Node, nid);
			}

			lastpart = partidx == partcount;
		}

		DataTable dtEmps = DBAccess.RunSQLReturnTable("SELECT No FROM Port_Emp");
		bp.wf.template.NodeEmp nemp = null;

		for (String empNo : empNos)
		{
			if (dtEmps.Select(String.format("No='%1$s'", empNo)).length + dtEmps.Select(String.format("NO='%1$s'", empNo)).length == 0)
			{
				continue;
			}

			nemp = new bp.wf.template.NodeEmp();
			nemp.setFK_Node(nid);
			nemp.setFK_Emp(empNo);
			nemp.Insert();
		}

		if (DataType.IsNullOrEmpty(partno))
		{
			jr.setMsg("保存成功");
		}
		else
		{
			class AnonymousType
			{
				public boolean lastpart;
				public int partidx;
				public int partcount;

				public AnonymousType(boolean _lastpart, int _partidx, int _partcount)
				{
					lastpart = _lastpart;
					partidx = _partidx;
					partcount = _partcount;
				}
			}
			jr.setInnerData(new AnonymousType(lastpart, partidx, partcount));

			if (lastpart)
			{
				jr.setMsg("保存成功");
			}
			else
			{
				jr.setMsg(String.format("第%d/%d段保存成功", partidx, partcount));
			}
		}

		return bp.tools.Json.ToJson(jr);
	}



	/** 
	 保存节点绑定部门信息
	 
	 @return 
	*/
	public final String Dot2DotTreeDeptModel_SaveNodeDepts() throws Exception {
		JsonResultInnerData jr = new JsonResultInnerData();
		String nodeid = this.GetRequestVal("nodeid");
		String data = this.GetRequestVal("data");
		String partno = this.GetRequestVal("partno");
		boolean lastpart = false;
		int partidx = 0;
		int partcount = 0;
		int nid = 0;

		
		if (DataType.IsNullOrEmpty(nodeid))
			throw new RuntimeException("参数nodeid不正确");

		nid = Integer.parseInt(nodeid);


		if (DataType.IsNullOrEmpty(data))
		{
			data = "";
		}

		bp.wf.template.NodeDepts ndepts = new bp.wf.template.NodeDepts();
		String[] deptNos = data.split("\\|");
		//提交内容过长时，采用分段式提交
		if (DataType.IsNullOrEmpty(partno))
		{
			ndepts.Delete(bp.wf.template.NodeDeptAttr.FK_Node, nid);
		}
		else
		{
			String[] parts = partno.split(java.util.regex.Pattern.quote("/"), -1);

			if (parts.length != 2)
			{
				throw new RuntimeException("参数partno不正确");
			}

			partidx = Integer.parseInt(parts[0]);
			partcount = Integer.parseInt(parts[1]);

			deptNos = data.split("\\|");

			if (partidx == 1)
			{
				ndepts.Delete(bp.wf.template.NodeDeptAttr.FK_Node, nid);
			}

			lastpart = partidx == partcount;
		}

		DataTable dtDepts = DBAccess.RunSQLReturnTable("SELECT No FROM Port_Dept");
		bp.wf.template.NodeDept nemp = null;

		for (String deptNo : deptNos)
		{
			if (dtDepts.Select(String.format("No='%1$s'", deptNo)).length + dtDepts.Select(String.format("NO='%1$s'", deptNo)).length == 0)
			{
				continue;
			}

			nemp = new bp.wf.template.NodeDept();
			nemp.setFK_Node(nid);
			nemp.setFK_Dept(deptNo);
			nemp.Insert();
		}

		if (DataType.IsNullOrEmpty(partno))
		{
			jr.setMsg("保存成功");
		}
		else
		{
			class AnonymousType
			{
				public boolean lastpart;
				public int partidx;
				public int partcount;

				public AnonymousType(boolean _lastpart, int _partidx, int _partcount)
				{
					lastpart = _lastpart;
					partidx = _partidx;
					partcount = _partcount;
				}
			}
			jr.setInnerData(new AnonymousType(lastpart, partidx, partcount));

			if (lastpart)
			{
				jr.setMsg("保存成功");
			}
			else
			{
				jr.setMsg(String.format("第%1$s/%2$s段保存成功", partidx, partcount));
			}
		}

		return bp.tools.Json.ToJson(jr);
	}


	/** 
	 获取节点绑定人员信息列表
	 
	 @return 
	*/
	public final String Dot2DotTreeDeptModel_GetNodeDepts() throws Exception {

		String nid = this.GetRequestVal("nodeid");
		String sql = "SELECT pd.No,pd.Name,pd1.No DeptNo,pd1.Name DeptName FROM WF_NodeDept wnd " + "  INNER JOIN Port_Dept pd ON pd.No=wnd.FK_Dept " + "  LEFT JOIN Port_Dept pd1 ON pd1.No=pd.ParentNo " + "WHERE wnd.FK_Node = " + nid + " ORDER BY pd1.Idx, pd.Name";

		DataTable dt = DBAccess.RunSQLReturnTable(sql); //, pagesize, pageidx, "No", "Name", "ASC"
		dt.Columns.Add("Code", String.class);
		dt.Columns.Add("Checked", Boolean.class);

		for (DataRow row : dt.Rows)
		{
			row.setValue("Code", bp.tools.chs2py.ConvertStr2Code(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null));
			row.setValue("Checked", true);
		}

		if(SystemConfig.AppCenterDBFieldCaseModel()!=FieldCaseModel.None){
			dt.Columns.get(0).setColumnName("No");
			dt.Columns.get(1).setColumnName("Name");
			dt.Columns.get(2).setColumnName("DeptNo");
			dt.Columns.get(3).setColumnName("DeptName");
		}

		return bp.tools.Json.ToJson(dt);

	}

		///#endregion Dot2DotTreeDeptModel.htm（部门选择）


		///#region Dot2DotStationModel.htm（岗位选择）

	/** 
	 保存节点绑定岗位信息
	 
	 @return 
	*/
	public final String Dot2DotStationModel_SaveNodeStations() throws Exception {
		JsonResultInnerData jr = new JsonResultInnerData();
		String nodeid = this.GetRequestVal("nodeid");
		String data = this.GetRequestVal("data");
		String partno = this.GetRequestVal("partno");
		boolean lastpart = false;
		int partidx = 0;
		int partcount = 0;
		int nid = 0;

		
		if (DataType.IsNullOrEmpty(nodeid))
			throw new RuntimeException("参数nodeid不正确");
		if (DataType.IsNullOrEmpty(data))
		{
			data = "";
		}

		bp.wf.template.NodeStations nsts = new bp.wf.template.NodeStations();
		String[] stNos = data.split("\\|");

		//提交内容过长时，采用分段式提交
		if (DataType.IsNullOrEmpty(partno))
		{
			nsts.Delete(bp.wf.template.NodeStationAttr.FK_Node, nid);
		}
		else
		{
			String[] parts = partno.split(java.util.regex.Pattern.quote("/"), -1);

			if (parts.length != 2)
			{
				throw new RuntimeException("参数partno不正确");
			}

			partidx = Integer.parseInt(parts[0]);
			partcount = Integer.parseInt(parts[1]);

			stNos = data.split("\\|");

			if (partidx == 1)
			{
				nsts.Delete(bp.wf.template.NodeStationAttr.FK_Node, nid);
			}

			lastpart = partidx == partcount;
		}

		DataTable dtSts = DBAccess.RunSQLReturnTable("SELECT No FROM Port_Station");
		bp.wf.template.NodeStation nst = null;

		for (String stNo : stNos)
		{
			if (dtSts.Select(String.format("No='%1$s'", stNo)).length + dtSts.Select(String.format("NO='%1$s'", stNo)).length == 0)
			{
				continue;
			}

			nst = new bp.wf.template.NodeStation();
			nst.setFK_Node(nid);
			nst.setFK_Station(stNo);
			nst.Insert();
		}

		if (DataType.IsNullOrEmpty(partno))
		{
			jr.setMsg("保存成功");
		}
		else
		{
			class AnonymousType
			{
				public boolean lastpart;
				public int partidx;
				public int partcount;

				public AnonymousType(boolean _lastpart, int _partidx, int _partcount)
				{
					lastpart = _lastpart;
					partidx = _partidx;
					partcount = _partcount;
				}
			}
			jr.setInnerData(new AnonymousType(lastpart, partidx, partcount));

			if (lastpart)
			{
				jr.setMsg("保存成功");
			}
			else
			{
				jr.setMsg(String.format("第%1$s/%2$s段保存成功", partidx, partcount));
			}
		}

		return bp.tools.Json.ToJson(jr);
	}

	/** 
	 获取部门树根结点
	 
	 @return 
	*/
	public final String Dot2DotStationModel_GetStructureTreeRoot() throws Exception {
		JsonResultInnerData jr = new JsonResultInnerData();

		EasyuiTreeNode node, subnode;
		ArrayList<EasyuiTreeNode> d = new ArrayList<EasyuiTreeNode>();
		String parentrootid = this.GetRequestVal("parentrootid");
		String sql = null;
		DataTable dt = null;

		if (DataType.IsNullOrEmpty(parentrootid))
		{
			throw new RuntimeException("参数parentrootid不能为空");
		}

		CheckStationTypeIdxExists();
		boolean isUnitModel = DBAccess.IsExitsTableCol("Port_Dept", "IsUnit");

		if (isUnitModel)
		{
			boolean isValid = DBAccess.IsExitsTableCol("Port_Station", "FK_Unit");

			if (!isValid)
			{
				isUnitModel = false;
			}
		}

		if (isUnitModel)
		{
			sql = String.format("SELECT No,Name,ParentNo FROM Port_Dept WHERE IsUnit = 1 AND ParentNo = '%1$s'", parentrootid);
			dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() == 0)
			{
				dt.Rows.AddDatas("-1", "无单位数据", parentrootid);
			}

			node = new EasyuiTreeNode();
			node.setId("UNITROOT_" + dt.Rows.get(0).getValue("No"));
			node.setText(dt.Rows.get(0).getValue("Name") instanceof String ? (String)dt.Rows.get(0).getValue("Name") : null);
			node.setIconCls("icon-department");
			node.setAttributes(new EasyuiTreeNodeAttributes());
			node.getAttributes().setNo(dt.Rows.get(0).getValue("No") instanceof String ? (String)dt.Rows.get(0).getValue("No") : null);
			node.getAttributes().setName(dt.Rows.get(0).getValue("Name") instanceof String ? (String)dt.Rows.get(0).getValue("Name") : null);
			node.getAttributes().setParentNo(parentrootid);
			node.getAttributes().setTType("UNITROOT");
			node.setState("closed");

			if (!node.getText().equals("无单位数据"))
			{
				node.setChildren(new ArrayList<EasyuiTreeNode>());
				node.getChildren().add(new EasyuiTreeNode());
				node.getChildren().get(0).setText("loading...");
			}

			d.add(node);
		}
		else
		{
			sql = "SELECT No,Name FROM Port_StationType";
			dt = DBAccess.RunSQLReturnTable(sql);

			node = new EasyuiTreeNode();
			node.setId("STROOT_-1");
			node.setText("岗位类型");
			node.setIconCls("icon-department");
			node.setAttributes(new EasyuiTreeNodeAttributes());
			node.getAttributes().setNo("-1");
			node.getAttributes().setName("岗位类型");
			node.getAttributes().setParentNo(parentrootid);
			node.getAttributes().setTType("STROOT");
			node.setState("closed");

			if (dt.Rows.size() > 0)
			{
				node.setChildren(new ArrayList<EasyuiTreeNode>());
				node.getChildren().add(new EasyuiTreeNode());
				node.getChildren().get(0).setText("loading...");
			}

			d.add(node);
		}

		jr.setInnerData(d);
		jr.setMsg(String.valueOf(isUnitModel).toLowerCase());

		return bp.tools.Json.ToJson(jr);
	}

	/** 
	 获取指定部门下一级子部门及人员列表
	 
	 @return 
	*/
	public final String Dot2DotStationModel_GetSubUnits() throws Exception {
		String parentid = this.GetRequestVal("parentid");
		String nid = this.GetRequestVal("nodeid");
		String tp = this.GetRequestVal("stype"); //ST,UNIT
		String ttype = this.GetRequestVal("ttype"); //STROOT,UNITROOT,ST,CST,S

		if (DataType.IsNullOrEmpty(parentid))
		{
			throw new RuntimeException("参数parentid不能为空");
		}
		if (DataType.IsNullOrEmpty(nid))
		{
			throw new RuntimeException("参数nodeid不能为空");
		}

		EasyuiTreeNode node = null;
		ArrayList<EasyuiTreeNode> d = new ArrayList<EasyuiTreeNode>();
		String sql = "";
		DataTable dt = null;
		bp.wf.template.NodeStations sts = new bp.wf.template.NodeStations();
		String sortField = CheckStationTypeIdxExists() ? "Idx" : "No";

		sts.Retrieve(bp.wf.template.NodeStationAttr.FK_Node, Integer.parseInt(nid), null);

		if (tp.equals("ST"))
		{
			if (ttype.equals("STROOT"))
			{
				sql = "SELECT No,Name FROM Port_StationType ORDER BY " + sortField + " ASC";
				dt = DBAccess.RunSQLReturnTable(sql);

				for (DataRow row : dt.Rows)
				{
					node = new EasyuiTreeNode();
					node.setId("ST_" + row.getValue("No"));
					node.setText(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.setIconCls("icon-department");
					node.setAttributes(new EasyuiTreeNodeAttributes());
					node.getAttributes().setNo(row.getValue("No") instanceof String ? (String)row.getValue("No") : null);
					node.getAttributes().setName(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.getAttributes().setParentNo("-1");
					node.getAttributes().setTType("ST");
					node.setState("closed");
					node.setChildren(new ArrayList<EasyuiTreeNode>());
					node.getChildren().add(new EasyuiTreeNode());
					node.getChildren().get(0).setText("loading...");

					d.add(node);
				}
			}
			else
			{
				sql = String.format("SELECT ps.No,ps.Name,ps.FK_StationType,pst.Name FK_StationTypeName FROM Port_Station ps" + " INNER JOIN Port_StationType pst ON pst.No = ps.FK_StationType" + " WHERE ps.FK_StationType = '%1$s' ORDER BY ps.Name ASC", parentid);
				dt = DBAccess.RunSQLReturnTable(sql);

				for (DataRow row : dt.Rows)
				{
					node = new EasyuiTreeNode();
					node.setId("S_" + parentid + "_" + row.getValue("No"));
					node.setText(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.setIconCls("icon-user");
					node.setChecked(sts.GetEntityByKey(bp.wf.template.NodeStationAttr.FK_Station, row.getValue("No")) != null);
					node.setAttributes(new EasyuiTreeNodeAttributes());
					node.getAttributes().setNo(row.getValue("No") instanceof String ? (String)row.getValue("No") : null);
					node.getAttributes().setName(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.getAttributes().setParentNo(row.getValue("FK_StationType") instanceof String ? (String)row.getValue("FK_StationType") : null);
					node.getAttributes().setParentName(row.getValue("FK_StationTypeName") instanceof String ? (String)row.getValue("FK_StationTypeName") : null);
					node.getAttributes().setTType("S");
					node.getAttributes().setCode(bp.tools.chs2py.ConvertStr2Code(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null));

					d.add(node);
				}
			}
		}
		else
		{
			//岗位所属单位UNIT
			dt = DBAccess.RunSQLReturnTable(String.format("SELECT * FROM Port_Dept WHERE IsUnit = 1 AND ParentNo='%1$s' ORDER BY Name ASC", parentid));

			for (DataRow dept : dt.Rows)
			{
				node = new EasyuiTreeNode();
				node.setId("UNIT_" + dept.get("No"));
				node.setText(dept.get("Name") instanceof String ? (String)dept.get("Name") : null);
				node.setIconCls("icon-department");
				node.setAttributes(new EasyuiTreeNodeAttributes());
				node.getAttributes().setNo(dept.get("No") instanceof String ? (String)dept.get("No") : null);
				node.getAttributes().setName(dept.get("Name") instanceof String ? (String)dept.get("Name") : null);
				node.getAttributes().setParentNo(dept.get("ParentNo") instanceof String ? (String)dept.get("ParentNo") : null);
				node.getAttributes().setTType("UNIT");
				node.getAttributes().setCode(bp.tools.chs2py.ConvertStr2Code(dept.get("Name") instanceof String ? (String)dept.get("Name") : null));
				node.setState("closed");
				node.setChildren(new ArrayList<EasyuiTreeNode>());
				node.getChildren().add(new EasyuiTreeNode());
				node.getChildren().get(0).setText("loading...");

				d.add(node);
			}

			dt = DBAccess.RunSQLReturnTable(String.format("SELECT ps.No,ps.Name,pst.No FK_StationType, pst.Name FK_StationTypeName,ps.FK_Unit,pd.Name FK_UnitName FROM Port_Station ps" + " INNER JOIN Port_StationType pst ON pst.No = ps.FK_StationType" + " INNER JOIN Port_Dept pd ON pd.No=ps.FK_Unit" + " WHERE ps.FK_Unit = '%1$s' ORDER BY pst.%2$s ASC,ps.Name ASC", parentid, sortField));

			//增加岗位
			for (DataRow st : dt.Rows)
			{
				node = new EasyuiTreeNode();
				node.setId("S_" + st.get("FK_Unit") + "_" + st.get("No"));
				node.setText(st.get("Name") + "[" + st.get("FK_StationTypeName") + "]");
				node.setIconCls("icon-user");
				node.setChecked(sts.GetEntityByKey(bp.wf.template.NodeStationAttr.FK_Station, st.get("No")) != null);
				node.setAttributes(new EasyuiTreeNodeAttributes());
				node.getAttributes().setNo(st.get("No") instanceof String ? (String)st.get("No") : null);
				node.getAttributes().setName(st.get("Name") instanceof String ? (String)st.get("Name") : null);
				node.getAttributes().setParentNo(st.get("FK_Unit") instanceof String ? (String)st.get("FK_Unit") : null);
				node.getAttributes().setParentName(st.get("FK_UnitName") instanceof String ? (String)st.get("FK_UnitName") : null);
				node.getAttributes().setTType("S");
				node.getAttributes().setCode(bp.tools.chs2py.ConvertStr2Code(st.get("Name") instanceof String ? (String)st.get("Name") : null));

				d.add(node);
			}
		}

		return bp.tools.Json.ToJson(d);
	}

	/** 
	 获取节点绑定人员信息列表
	 
	 @return 
	*/
	public final String Dot2DotStationModel_GetNodeStations() throws Exception {
		JsonResultInnerData jr = new JsonResultInnerData();

		DataTable dt = null;
		String nid = this.GetRequestVal("nodeid");
		int pagesize = Integer.parseInt(this.GetRequestVal("pagesize"));
		int pageidx = Integer.parseInt(this.GetRequestVal("pageidx"));
		String st = this.GetRequestVal("stype");
		String sql = "";
		String sortField = CheckStationTypeIdxExists() ? "Idx" : "No";

		if (st.equals("UNIT"))
		{
			sql = "SELECT ps.No,ps.Name,pd.No UnitNo,pd.Name UnitName FROM WF_NodeStation wns " + "  INNER JOIN Port_Station ps ON ps.No=wns.FK_Station " + "  INNER JOIN Port_Dept pd ON pd.No=ps.FK_Unit " + "WHERE wns.FK_Node = " + nid + " ORDER BY ps.Name ASC";
		}
		else
		{
			sql = "SELECT ps.No,ps.Name,pst.No UnitNo,pst.Name UnitName FROM WF_NodeStation wns " + "  INNER JOIN Port_Station ps ON ps.No=wns.FK_Station " + "  INNER JOIN Port_StationType pst ON pst.No=ps.FK_StationType " + "WHERE wns.FK_Node = " + nid + " ORDER BY pst." + sortField + " ASC,ps.Name ASC";
		}

		dt = DBAccess.RunSQLReturnTable(sql); //, pagesize, pageidx, "No", "Name", "ASC"
		dt.Columns.Add("Code", String.class);
		dt.Columns.Add("Checked", Boolean.class);

		for (DataRow row : dt.Rows)
		{
			row.setValue("Code", bp.tools.chs2py.ConvertStr2Code(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null));
			row.setValue("Checked", true);
		}

		if(SystemConfig.AppCenterDBFieldCaseModel()!=FieldCaseModel.None){
			dt.Columns.get(0).setColumnName("No");
			dt.Columns.get(1).setColumnName("Name");
			dt.Columns.get(2).setColumnName("DeptNo");
			dt.Columns.get(3).setColumnName("DeptName");
		}
		jr.setInnerData(dt);
		jr.setMsg("");
		String re = bp.tools.Json.ToJson(jr);

		return bp.tools.Json.ToJson(re);
	}

		///#endregion Dot2DotStationModel.htm（岗位选择）


		///#region Methods
	/** 
	 判断Port_StationType表中是否含有Idx字段
	 
	 @return 
	*/
	public final boolean CheckStationTypeIdxExists() throws Exception {
		if (DBAccess.IsExitsTableCol("Port_StationType", "Idx") == false)
		{
			if (DBAccess.IsView("Port_StationType", bp.difference.SystemConfig.getAppCenterDBType( )) == false)
			{
				StationType st = new StationType();
				st.CheckPhysicsTable();

				DBAccess.RunSQL("UPDATE Port_StationType SET Idx = 1");
				return true;
			}
		}
		else
		{
			return true;
		}
		return false;
	}

		///#endregion


		///#region 辅助实体定义
	/** 
	 Eayui tree node对象
	 <p>主要用于数据的JSON化组织</p>
	*/
	public static class EasyuiTreeNode
	{
		private String id;
		public final String getId() throws Exception {
			return id;
		}
		public final void setId(String value)throws Exception
	{id = value;
		}
		private String text;
		public final String getText() throws Exception {
			return text;
		}
		public final void setText(String value)throws Exception
	{text = value;
		}
		private String state;
		public final String getState() throws Exception {
			return state;
		}
		public final void setState(String value)throws Exception
	{state = value;
		}
		private boolean checked;
		public final boolean getChecked() throws Exception {
			return checked;
		}
		public final void setChecked(boolean value)throws Exception
	{checked = value;
		}
		private String iconCls;
		public final String getIconCls() throws Exception {
			return iconCls;
		}
		public final void setIconCls(String value)throws Exception
	{iconCls = value;
		}
		private EasyuiTreeNodeAttributes attributes;
		public final EasyuiTreeNodeAttributes getAttributes() throws Exception {
			return attributes;
		}
		public final void setAttributes(EasyuiTreeNodeAttributes value)throws Exception
	{attributes = value;
		}
		private ArrayList<EasyuiTreeNode> children;
		public final ArrayList<EasyuiTreeNode> getChildren() throws Exception {
			return children;
		}
		public final void setChildren(ArrayList<EasyuiTreeNode> value)throws Exception
	{children = value;
		}
	}

	public static class EasyuiTreeNodeAttributes
	{
		private String No;
		public final String getNo() throws Exception {
			return No;
		}
		public final void setNo(String value)throws Exception
	{No = value;
		}
		private String Name;
		public final String getName() throws Exception {
			return Name;
		}
		public final void setName(String value)throws Exception
	{Name = value;
		}
		private String ParentNo;
		public final String getParentNo() throws Exception {
			return ParentNo;
		}
		public final void setParentNo(String value)throws Exception
	{ParentNo = value;
		}
		private String ParentName;
		public final String getParentName() throws Exception {
			return ParentName;
		}
		public final void setParentName(String value)throws Exception
	{ParentName = value;
		}
		private String TType;
		public final String getTType() throws Exception {
			return TType;
		}
		public final void setTType(String value)throws Exception
	{TType = value;
		}
		private String Code;
		public final String getCode() throws Exception {
			return Code;
		}
		public final void setCode(String value)throws Exception
	{Code = value;
		}
	}

		///#endregion 辅助实体定义
}