package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Port.StationType;
import BP.Sys.*;
import BP.Tools.StringHelper;
import BP.WF.Glo;

import java.util.*;

public class WF_Comm_RefFunc extends WebContralBase
{


	/** 
	 构造函数
	*/
	public WF_Comm_RefFunc()
	{
	}


	/** 
	 保存节点绑定人员信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Dot2DotTreeDeptEmpModel_SaveNodeEmps() throws Exception
	{
        String nodeid = this.GetRequestVal("NodeId");
        String data = this.GetRequestVal("data");
        String partno = this.GetRequestVal("partno");
        boolean lastpart = false;
        int partidx = 0;
        int partcount = 0;
        int nid = 0;
        String msg = "";

        if (StringHelper.isNullOrEmpty(nodeid))
        	throw new RuntimeException("参数nodeid不正确");
        nid = Integer.parseInt(nodeid);
        if (StringHelper.isNullOrEmpty(data))
            data = "";
        BP.WF.Template.NodeEmps nemps = new BP.WF.Template.NodeEmps();
        String[] empNos = data.split(",");

        //提交内容过长时，采用分段式提交
        if (StringHelper.isNullOrEmpty(partno))
        {
            nemps.Delete(BP.WF.Template.NodeEmpAttr.FK_Node, nid);
        }
        else
        {
            String[] parts = partno.split("/");

            if (parts.length != 2)
            	throw new RuntimeException("err@参数partno不正确");

            partidx = Integer.parseInt(parts[0]);
            partcount = Integer.parseInt(parts[1]);

            empNos = data.split(",");

            if (partidx == 1)
                nemps.Delete(BP.WF.Template.NodeEmpAttr.FK_Node, nid);

            lastpart = partidx == partcount;
        }

        DataTable dtEmps = DBAccess.RunSQLReturnTable("SELECT No FROM Port_Emp");
        BP.WF.Template.NodeEmp nemp = null;

        for(String empNo : empNos)
        {
        	if (DBAccess.getAppCenterDBType() == DBType.Oracle)
            {
        		if (dtEmps.selectx("No=" + empNo).size() + dtEmps.selectx("NO=" + empNo).size() == 0)
            		continue;
            }else{
            	if (dtEmps.selectx("No=" + empNo).size() == 0)
            		continue;
            }
            nemp = new BP.WF.Template.NodeEmp();
            nemp.setFK_Node(nid);
            nemp.setFK_Emp(empNo);
            nemp.Insert();
        }

        Map<String,Object> jre = new HashMap<String,Object>();
        if (StringHelper.isNullOrEmpty(partno))
        {
            msg = "保存成功";
        }
        else
        {
             jre.put("lastpart", lastpart);
             jre.put("partidx", partidx);
             jre.put("partcount", partcount);
             //jr.setInnerData(jre);
             if (lastpart)
            	  //jr.setMsg("保存成功");
             	  msg = "保存成功";
            else
                //jr.setMsg(String.format("第{0}/{1}段保存成功", partidx, partcount));
             	msg = String.format("第{0}/{1}段保存成功", partidx, partcount);
        }
        return transction(BP.Tools.Json.ToJson(jre),msg);
	}

	public String transction(String innerData,String msg){
    	return "{\"innerData\": "+innerData+",\"msg\":\""+msg+"\"}";
    }

	/** 
	 保存节点绑定部门信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Dot2DotTreeDeptModel_SaveNodeDepts() throws Exception
	{
		JsonResultInnerData jr = new JsonResultInnerData();
        String nodeid = this.GetRequestVal("NodeId");
        String data = this.GetRequestVal("data");
        String partno = this.GetRequestVal("partno");
        boolean lastpart = false;
        int partidx = 0;
        int partcount = 0;
        int nid = 0;

        try {
        	nid = Integer.parseInt(nodeid);
        } catch (Exception e) {
        	throw new RuntimeException("参数nodeid不正确");
        }
        if (StringHelper.isNullOrEmpty(nodeid))// ||  int.TryParse(nodeid, out nid) == false
        	throw new RuntimeException("参数nodeid不正确");

        if (StringHelper.isNullOrEmpty(data))
            data = "";

        BP.WF.Template.NodeDepts ndepts = new BP.WF.Template.NodeDepts();
        String[] deptNos = data.split("\\|");

        //提交内容过长时，采用分段式提交
        if (StringHelper.isNullOrEmpty(partno))
        {
            ndepts.Delete(BP.WF.Template.NodeDeptAttr.FK_Node, nid);
        }
        else
        {
            String[] parts = partno.split("/",0);

            if (parts.length != 2)
            	throw new RuntimeException("参数partno不正确");

            partidx = Integer.parseInt(parts[0]);
            partcount = Integer.parseInt(parts[1]);

            deptNos = data.split("\\|");

            if (partidx == 1)
                ndepts.Delete(BP.WF.Template.NodeDeptAttr.FK_Node, nid);

            lastpart = partidx == partcount;
        }

        DataTable dtDepts = DBAccess.RunSQLReturnTable("SELECT No FROM Port_Dept");
        BP.WF.Template.NodeDept nemp = null;

        for(String deptNo : deptNos)
        {
            if (dtDepts.selectx("No="+deptNo).size() + dtDepts.selectx("NO="+deptNo).size() == 0)
                continue;
            nemp = new BP.WF.Template.NodeDept();
            nemp.setFK_Node(nid);
            nemp.setFK_Dept(deptNo);
            nemp.Insert();
        }

        if (StringHelper.isNullOrEmpty(partno))
        {
            jr.setMsg("保存成功");
        }
        else
        {
       	 Map<String,Object> jre = new HashMap<String,Object>();
            jre.put("lastpart", lastpart);
            jre.put("partidx", partidx);
            jre.put("partcount", partcount);
            jr.setInnerData(jre);
            if (lastpart)
           	  jr.setMsg("保存成功");
            else
                jr.setMsg(String.format("第%d/%d段保存成功", partidx, partcount));
        }

       // return Newtonsoft.Json.JsonConvert.SerializeObject(jr);
        return BP.Tools.Json.ToJson(jr);
	}


	/** 
	 获取节点绑定人员信息列表
	 
	 @return 
	*/
	public final String Dot2DotTreeDeptModel_GetNodeDepts()
	{
		JsonResultInnerData jr = new JsonResultInnerData();

		DataTable dt = null;
		String nid = this.GetRequestVal("nodeid");
		String sql = "SELECT pd.No,pd.Name,pd1.No DeptNo,pd1.Name DeptName FROM WF_NodeDept wnd "
					 + "  INNER JOIN Port_Dept pd ON pd.No = wnd.FK_Dept "
					 + "  LEFT JOIN Port_Dept pd1 ON pd1.No = pd.ParentNo "
					 + "WHERE wnd.FK_Node = " + nid + " ORDER BY pd1.Idx, pd.Name";

		dt = DBAccess.RunSQLReturnTable(sql); //, pagesize, pageidx, "No", "Name", "ASC"
		dt.Columns.Add("Code", String.class);
		dt.Columns.Add("Checked", Boolean.class);

		for (DataRow row : dt.Rows)
		{
			row.setValue("Code", BP.Tools.chs2py.ConvertStr2Code(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null));
			row.setValue("Checked", true);
		}

		//对Oracle数据库做兼容性处理
		if (DBAccess.getAppCenterDBType() == DBType.Oracle)
		{
			for (DataColumn col : dt.Columns)
			{
				switch (col.ColumnName)
				{
					case "NO":
						col.ColumnName = "No";
						break;
					case "NAME":
						col.ColumnName = "Name";
						break;
					case "DEPTNO":
						col.ColumnName = "DeptNo";
						break;
					case "DEPTNAME":
						col.ColumnName = "DeptName";
						break;
				}
			}
		}

		jr.setInnerData(dt);
		String re = BP.Tools.Json.ToJson(jr);
		if (Glo.Plant == BP.WF.Plant.JFlow)
		{
			re = re.replace("\"NO\"", "\"No\"").replace("\"NAME\"", "\"Name\"").replace("\"DEPTNO\"", "\"DeptNo\"").replace("\"DEPTNAME\"", "\"DeptName\"");
		}
		return re;
	}

		///#endregion Dot2DotTreeDeptModel.htm（部门选择）


		///#region Dot2DotStationModel.htm（岗位选择）

	/** 
	 保存节点绑定岗位信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Dot2DotStationModel_SaveNodeStations() throws Exception
	{
		JsonResultInnerData jr = new JsonResultInnerData();
        String nodeid = this.GetRequestVal("nodeid");
        String data = this.GetRequestVal("data");
        String partno = this.GetRequestVal("partno");
        boolean lastpart = false;
        int partidx = 0;
        int partcount = 0;
        int nid = 0;

        try {
//			int.TryParse(nodeid, out nid)
            nid = Integer.parseInt(nodeid);
        } catch (Exception e) {
        	throw new RuntimeException("参数nodeid不正确");
        }
        if (StringHelper.isNullOrEmpty(nodeid))
            throw new RuntimeException("参数nodeid不正确");

        if (StringHelper.isNullOrEmpty(data))
            data = "";

        BP.WF.Template.NodeStations nsts = new BP.WF.Template.NodeStations();
        String[] stNos = data.split("\\|");

        //提交内容过长时，采用分段式提交
        if (StringHelper.isNullOrEmpty(partno))
        {
            nsts.Delete(BP.WF.Template.NodeStationAttr.FK_Node, nid);
        }
        else
        {
            String[] parts = partno.split("/",0);

            if (parts.length != 2)
                throw new RuntimeException("参数partno不正确");

            partidx = Integer.parseInt(parts[0]);
            partcount = Integer.parseInt(parts[1]);

            stNos = data.split("\\|");

            if (partidx == 1)
                nsts.Delete(BP.WF.Template.NodeStationAttr.FK_Node, nid);

            lastpart = partidx == partcount;
        }

        DataTable dtSts = DBAccess.RunSQLReturnTable("SELECT No FROM Port_Station");
        BP.WF.Template.NodeStation nst = null;

        for(String stNo : stNos)
        {
            if (dtSts.selectx("No="+stNo).size() + dtSts.selectx("NO="+stNo).size() == 0)
                continue;
            nst = new BP.WF.Template.NodeStation();
            nst.setFK_Node(nid);
            nst.setFK_Station(stNo);
            nst.Insert();
        }

        if (StringHelper.isNullOrEmpty(partno))
        {
        	jr.setMsg("保存成功");
        }
        else
        {
            Map<String,Object> jre = new HashMap<String,Object>();
             jre.put("lastpart", lastpart);
             jre.put("partidx", partidx);
             jre.put("partcount", partcount);
             jr.setInnerData(jre);
             if (lastpart)
            	  jr.setMsg("保存成功");
            else
                jr.setMsg(String.format("第%d/%d段保存成功", partidx, partcount));
        }
        return BP.Tools.Json.ToJson(jr);
	}

	/** 
	 获取部门树根结点
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Dot2DotStationModel_GetStructureTreeRoot() throws Exception
	{
		JsonResultInnerData jr = new JsonResultInnerData();

		EasyuiTreeNode node;
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
			node.setid("UNITROOT_" + dt.Rows.get(0).getValue("No"));
			node.settext(dt.Rows.get(0).getValue("Name") instanceof String ? (String)dt.Rows.get(0).getValue("Name") : null);
			node.seticonCls("icon-department");
			node.setattributes(new EasyuiTreeNodeAttributes());
			node.getattributes().setNo(dt.Rows.get(0).getValue("No") instanceof String ? (String)dt.Rows.get(0).getValue("No") : null);
			node.getattributes().setName(dt.Rows.get(0).getValue("Name") instanceof String ? (String)dt.Rows.get(0).getValue("Name") : null);
			node.getattributes().setParentNo(parentrootid);
			node.getattributes().setTType("UNITROOT");
			node.setstate("closed");

			if (!node.gettext().equals("无单位数据"))
			{
				node.setchildren(new ArrayList<EasyuiTreeNode>());
				node.getchildren().add(new EasyuiTreeNode());
				node.getchildren().get(0).settext("loading...");
			}

			d.add(node);
		}
		else
		{
			sql = "SELECT No,Name FROM Port_StationType";
			dt = DBAccess.RunSQLReturnTable(sql);

			node = new EasyuiTreeNode();
			node.setid("STROOT_-1");
			node.settext("岗位类型");
			node.seticonCls("icon-department");
			node.setattributes(new EasyuiTreeNodeAttributes());
			node.getattributes().setNo("-1");
			node.getattributes().setName("岗位类型");
			node.getattributes().setParentNo(parentrootid);
			node.getattributes().setTType("STROOT");
			node.setstate("closed");

			if (dt.Rows.size() > 0)
			{
				node.setchildren(new ArrayList<EasyuiTreeNode>());
				node.getchildren().add(new EasyuiTreeNode());
				node.getchildren().get(0).settext("loading...");
			}

			d.add(node);
		}

		jr.setInnerData(d);
		jr.setMsg(String.valueOf(isUnitModel).toLowerCase());

		return BP.Tools.Json.ToJson(jr);
	}

	/** 
	 获取指定部门下一级子部门及人员列表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Dot2DotStationModel_GetSubUnits() throws Exception
	{
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
		BP.WF.Template.NodeStations sts = new BP.WF.Template.NodeStations();
		String sortField = CheckStationTypeIdxExists() ? "Idx" : "No";

		sts.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, Integer.parseInt(nid));

		if (tp.equals("ST"))
		{
			if (ttype.equals("STROOT"))
			{
				sql = "SELECT No,Name FROM Port_StationType ORDER BY " + sortField + " ASC";
				dt = DBAccess.RunSQLReturnTable(sql);

				for (DataRow row : dt.Rows)
				{
					node = new EasyuiTreeNode();
					node.setid("ST_" + row.getValue("No"));
					node.settext(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.seticonCls("icon-department");
					node.setattributes(new EasyuiTreeNodeAttributes());
					node.getattributes().setNo(row.getValue("No") instanceof String ? (String)row.getValue("No") : null);
					node.getattributes().setName(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.getattributes().setParentNo("-1");
					node.getattributes().setTType("ST");
					node.setstate("closed");
					node.setchildren(new ArrayList<EasyuiTreeNode>());
					node.getchildren().add(new EasyuiTreeNode());
					node.getchildren().get(0).settext("loading...");

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
					node.setid("S_" + parentid + "_" + row.getValue("No"));
					node.settext(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.seticonCls("icon-user");
					node.setchecked(sts.GetEntityByKey(BP.WF.Template.NodeStationAttr.FK_Station, row.getValue("No")) != null);
					node.setattributes(new EasyuiTreeNodeAttributes());
					node.getattributes().setNo(row.getValue("No") instanceof String ? (String)row.getValue("No") : null);
					node.getattributes().setName(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null);
					node.getattributes().setParentNo(row.getValue("FK_StationType") instanceof String ? (String)row.getValue("FK_StationType") : null);
					node.getattributes().setParentName(row.getValue("FK_StationTypeName") instanceof String ? (String)row.getValue("FK_StationTypeName") : null);
					node.getattributes().setTType("S");
					node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null));

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
				node.setid("UNIT_" + dept.get("No"));
				node.settext(dept.get("Name") instanceof String ? (String)dept.get("Name") : null);
				node.seticonCls("icon-department");
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo(dept.get("No") instanceof String ? (String)dept.get("No") : null);
				node.getattributes().setName(dept.get("Name") instanceof String ? (String)dept.get("Name") : null);
				node.getattributes().setParentNo(dept.get("ParentNo") instanceof String ? (String)dept.get("ParentNo") : null);
				node.getattributes().setTType("UNIT");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.get("Name") instanceof String ? (String)dept.get("Name") : null));
				node.setstate("closed");
				node.setchildren(new ArrayList<EasyuiTreeNode>());
				node.getchildren().add(new EasyuiTreeNode());
				node.getchildren().get(0).settext("loading...");

				d.add(node);
			}

			dt = DBAccess.RunSQLReturnTable(String.format("SELECT ps.No,ps.Name,pst.No FK_StationType, pst.Name FK_StationTypeName,ps.FK_Unit,pd.Name FK_UnitName FROM Port_Station ps" + " INNER JOIN Port_StationType pst ON pst.No = ps.FK_StationType" + " INNER JOIN Port_Dept pd ON pd.No = ps.FK_Unit" + " WHERE ps.FK_Unit = '%1$s' ORDER BY pst.%2$s ASC,ps.Name ASC", parentid, sortField));

			//增加岗位
			for (DataRow st : dt.Rows)
			{
				node = new EasyuiTreeNode();
				node.setid("S_" + st.get("FK_Unit") + "_" + st.get("No"));
				node.settext(st.get("Name") + "[" + st.get("FK_StationTypeName") + "]");
				node.seticonCls("icon-user");
				node.setchecked(sts.GetEntityByKey(BP.WF.Template.NodeStationAttr.FK_Station, st.get("No")) != null);
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo(st.get("No") instanceof String ? (String)st.get("No") : null);
				node.getattributes().setName(st.get("Name") instanceof String ? (String)st.get("Name") : null);
				node.getattributes().setParentNo(st.get("FK_Unit") instanceof String ? (String)st.get("FK_Unit") : null);
				node.getattributes().setParentName(st.get("FK_UnitName") instanceof String ? (String)st.get("FK_UnitName") : null);
				node.getattributes().setTType("S");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(st.get("Name") instanceof String ? (String)st.get("Name") : null));

				d.add(node);
			}
		}

		return BP.Tools.Json.ToJson(d);
	}

	/** 
	 获取节点绑定人员信息列表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Dot2DotStationModel_GetNodeStations() throws Exception
	{
		JsonResultInnerData jr = new JsonResultInnerData();

		DataTable dt = null;
		String nid = this.GetRequestVal("nodeid");
		String st = this.GetRequestVal("stype");
		String sql = "";
		String sortField = CheckStationTypeIdxExists() ? "Idx" : "No";

		if (st.equals("UNIT"))
		{
			sql = "SELECT ps.No,ps.Name,pd.No UnitNo,pd.Name UnitName FROM WF_NodeStation wns "
						 + "  INNER JOIN Port_Station ps ON ps.No = wns.FK_Station "
						 + "  INNER JOIN Port_Dept pd ON pd.No = ps.FK_Unit "
						 + "WHERE wns.FK_Node = " + nid + " ORDER BY ps.Name ASC";
		}
		else
		{
			sql = "SELECT ps.No,ps.Name,pst.No UnitNo,pst.Name UnitName FROM WF_NodeStation wns "
						 + "  INNER JOIN Port_Station ps ON ps.No = wns.FK_Station "
						 + "  INNER JOIN Port_StationType pst ON pst.No = ps.FK_StationType "
						 + "WHERE wns.FK_Node = " + nid + " ORDER BY pst." + sortField + " ASC,ps.Name ASC";
		}

		dt = DBAccess.RunSQLReturnTable(sql); //, pagesize, pageidx, "No", "Name", "ASC"
		dt.Columns.Add("Code", String.class);
		dt.Columns.Add("Checked", Boolean.class);

		for (DataRow row : dt.Rows)
		{
			row.setValue("Code", BP.Tools.chs2py.ConvertStr2Code(row.getValue("Name") instanceof String ? (String)row.getValue("Name") : null));
			row.setValue("Checked", true);
		}

		//对Oracle数据库做兼容性处理
		if (DBAccess.getAppCenterDBType() == DBType.Oracle)
		{
			for (DataColumn col : dt.Columns)
			{
				switch (col.ColumnName)
				{
					case "NO":
						col.ColumnName = "No";
						break;
					case "NAME":
						col.ColumnName = "Name";
						break;
					case "UNITNO":
						col.ColumnName = "DeptNo";
						break;
					case "UNITNAME":
						col.ColumnName = "DeptName";
						break;
				}
			}
		}

		jr.setInnerData(dt);
		jr.setMsg("");
		String re = BP.Tools.Json.ToJson(jr);
		if (Glo.Plant == BP.WF.Plant.JFlow)
		{
			re = re.replace("\"NO\"", "\"No\"").replace("\"NAME\"", "\"Name\"").replace("\"UNITNO\"", "\"UnitNo\"").replace("\"UNITNAME\"", "\"UnitName\"");
		}
		return BP.Tools.Json.ToJson(re);
	}

	/** 
	 判断Port_StationType表中是否含有Idx字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final boolean CheckStationTypeIdxExists() throws Exception
	{
		if (DBAccess.IsExitsTableCol("Port_StationType", "Idx") == false)
		{
			if (DBAccess.IsView("Port_StationType", BP.Difference.SystemConfig.getAppCenterDBType()) == false)
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
		public final String getid()
		{
			return id;
		}
		public final void setid(String value)
		{
			id = value;
		}
		private String text;
		public final String gettext()
		{
			return text;
		}
		public final void settext(String value)
		{
			text = value;
		}
		private String state;
		public final String getstate()
		{
			return state;
		}
		public final void setstate(String value)
		{
			state = value;
		}
		private boolean checked;
		public final boolean getchecked()
		{
			return checked;
		}
		public final void setchecked(boolean value)
		{
			checked = value;
		}
		private String iconCls;
		public final String geticonCls()
		{
			return iconCls;
		}
		public final void seticonCls(String value)
		{
			iconCls = value;
		}
		private EasyuiTreeNodeAttributes attributes;
		public final EasyuiTreeNodeAttributes getattributes()
		{
			return attributes;
		}
		public final void setattributes(EasyuiTreeNodeAttributes value)
		{
			attributes = value;
		}
		private ArrayList<EasyuiTreeNode> children;
		public final ArrayList<EasyuiTreeNode> getchildren()
		{
			return children;
		}
		public final void setchildren(ArrayList<EasyuiTreeNode> value)
		{
			children = value;
		}
	}

	public static class EasyuiTreeNodeAttributes
	{
		private String No;
		public final String getNo()
		{
			return No;
		}
		public final void setNo(String value)
		{
			No = value;
		}
		private String Name;
		public final String getName()
		{
			return Name;
		}
		public final void setName(String value)
		{
			Name = value;
		}
		private String ParentNo;
		public final String getParentNo()
		{
			return ParentNo;
		}
		public final void setParentNo(String value)
		{
			ParentNo = value;
		}
		private String ParentName;
		public final String getParentName()
		{
			return ParentName;
		}
		public final void setParentName(String value)
		{
			ParentName = value;
		}
		private String TType;
		public final String getTType()
		{
			return TType;
		}
		public final void setTType(String value)
		{
			TType = value;
		}
		private String Code;
		public final String getCode()
		{
			return Code;
		}
		public final void setCode(String value)
		{
			Code = value;
		}
	}

		///#endregion 辅助实体定义
}