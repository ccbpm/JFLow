package BP.WF.HttpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.GPM.StationType;
import BP.Sys.OSModel;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Comm.EasyuiTreeNode;
import BP.WF.Comm.EasyuiTreeNodeAttributes;
import BP.WF.Comm.JsonResultEmp;
import BP.WF.Comm.JsonResultInnerData;
import BP.WF.Comm.JsonResultStation;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.NodeDept;
import BP.WF.Template.NodeDeptAttr;

public class WF_Comm_RefFunc extends WebContralBase {
	/**
	 * 保存节点绑定人员信息
	 * @return
	 * @throws Exception 
	 */
    public String Dot2DotTreeDeptEmpModel_SaveNodeEmps()
    {
    	//JsonResultInnerData jr = new JsonResultInnerData();
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
    /**
     * 获取部门树根结点
     * @throws Exception 
     */
    public String Dot2DotTreeDeptEmpModel_GetStructureTreeRoot()
    {
    	//JsonResultInnerData jr = new JsonResultInnerData();

        EasyuiTreeNode node = null;
        EasyuiTreeNodeAttributes attributes = null;
        List<EasyuiTreeNode> d= new ArrayList<EasyuiTreeNode>();
        String parentrootid = this.GetRequestVal("parentrootid");

        if (StringHelper.isNullOrEmpty(parentrootid))
        	throw new RuntimeException("参数parentrootid不能为空");

        if (BP.Sys.SystemConfig.getOSModel() == OSModel.OneOne)
        {
            BP.WF.Port.Dept dept = new BP.WF.Port.Dept();

            if (dept.Retrieve(BP.WF.Port.DeptAttr.ParentNo, parentrootid) == 0)
            {
                dept.setNo("-1");
                dept.setName("无部门");
                dept.setParentNo("");
            }

            node = new EasyuiTreeNode();
            node.setid("DEPT_" + dept.getNo());
            node.settext(dept.getName());
            node.seticonCls("icon-department");
            node.setattributes(new EasyuiTreeNodeAttributes());
            node.getattributes().setNo(dept.getNo());
            node.getattributes().setName(dept.getName());
            node.getattributes().setParentNo(dept.getParentNo());
            node.getattributes().setTType("DEPT");
            node.setstate("closed");
            node.setchildren(new ArrayList<EasyuiTreeNode>());
            node.getchildren().add(new EasyuiTreeNode());
            node.getchildren().get(0).settext("loading...");

            d.add(node);
        }
        else
        {
            BP.GPM.Dept dept = new BP.GPM.Dept();

            if (dept.Retrieve(BP.GPM.DeptAttr.ParentNo, parentrootid) == 0)
            {
                dept.setNo("-1");
                dept.setName("无部门");
                dept.setParentNo("");
            }

            node = new EasyuiTreeNode();
            node.setid("DEPT_" + dept.getNo());
            node.settext(dept.getName());
            node.seticonCls("icon-department");
            node.setattributes(new EasyuiTreeNodeAttributes());
            node.getattributes().setNo(dept.getNo());
            node.getattributes().setName(dept.getName());
            node.getattributes().setParentNo(dept.getParentNo());
            node.getattributes().setTType("DEPT");
            node.setstate("closed");
            node.setchildren(new ArrayList<EasyuiTreeNode>());
            node.getchildren().add(new EasyuiTreeNode());
            node.getchildren().get(0).settext("loading...");

            d.add(node);
        }

        //jr.setInnerData(d);

        return transction(BP.Tools.Json.ToJson(d),"");
    }

    /**
     * 获取指定部门下一级子部门及人员列表
     * @throws Exception 
     */
    public String Dot2DotTreeDeptEmpModel_GetSubDepts()
    {
    	//JsonResultInnerData jr = new JsonResultInnerData();

        EasyuiTreeNode node = null;
        JsonResultEmp jremp = null;
        List<JsonResultEmp> jremps = new ArrayList<JsonResultEmp>();
        List<EasyuiTreeNode> d = new ArrayList<EasyuiTreeNode>();
        List<EasyuiTreeNode> parentNodes = new ArrayList<EasyuiTreeNode>();
        EasyuiTreeNode parent = null;
        String rootid = this.GetRequestVal("rootid");
        String parentid = this.GetRequestVal("parentid");
        String nid = this.GetRequestVal("NodeId");
        String currparentid = parentid;
        BP.WF.Template.NodeEmps semps = new BP.WF.Template.NodeEmps();

        if (StringHelper.isNullOrEmpty(rootid))
        	throw new RuntimeException("参数rootid不能为空");
        if (StringHelper.isNullOrEmpty(parentid))
        	throw new RuntimeException("参数parentid不能为空");
        if (StringHelper.isNullOrEmpty(nid))
        	throw new RuntimeException("参数nodeid不能为空");

        semps.Retrieve(BP.WF.Template.NodeEmpAttr.FK_Node, Integer.parseInt(nid));

        if (BP.Sys.SystemConfig.getOSModel() == OSModel.OneOne)
        {
        	BP.Port.Dept parentDept = new BP.Port.Dept(parentid);
        	BP.Port.Depts depts = new BP.Port.Depts();
			depts.Retrieve(BP.Port.DeptAttr.ParentNo, parentid, BP.Port.DeptAttr.Name);
			BP.Port.Emps emps = new BP.Port.Emps();
			emps.Retrieve(BP.Port.EmpAttr.FK_Dept, parentid, BP.Port.EmpAttr.Name);
			// 增加部门
			for (BP.Port.Dept dept : depts.ToJavaList()) {
				node = new EasyuiTreeNode();
				node.setid("DEPT_" + dept.getNo());
				node.settext(dept.getName());
				node.seticonCls("icon-department");
				node.setchecked(semps.GetEntityByKey(BP.WF.Template.NodeDeptAttr.FK_Dept, dept.getNo()) != null);
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo(dept.getNo());
				node.getattributes().setName(dept.getName());
				node.getattributes().setParentNo(dept.getParentNo());
				node.getattributes().setTType("DEPT");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.getName()));
				node.setstate("closed");
				node.setchildren(new ArrayList<EasyuiTreeNode>());
				node.getchildren().add(new EasyuiTreeNode());
				node.getchildren().get(0).settext("loading...");
				d.add(node);
			}
			//增加人员
			for (BP.Port.Emp emp : emps.ToJavaList()) {
				node = new EasyuiTreeNode();
				node.setid("EMP_" + parentid + "_" + emp.getNo());
				node.settext(emp.getName());
				node.seticonCls("icon-user");
				node.setchecked(semps.GetEntityByKey(BP.WF.Template.NodeEmpAttr.FK_Emp, emp.getNo()) != null);
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo(emp.getNo());
				node.getattributes().setName(emp.getName());
				node.getattributes().setParentNo(parentDept.getParentNo());
				node.getattributes().setParentName(parentDept.getParentName());
				node.getattributes().setTType("EMP");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(emp.getName()));
				d.add(node);
			}
			
            //逐级计算当前部门下的所有父级部门
//            do
//            {
//                BP.Port.Dept parentdept = new BP.Port.Dept(parentid);
//
//                parent = new EasyuiTreeNode();
//                parent.setid("DEPT_" + parentdept.getNo());
//                parent.settext(parentdept.getName());
//                parent.seticonCls("icon-department");
//                parent.setattributes(new EasyuiTreeNodeAttributes());
//                parent.getattributes().setNo(parentdept.getNo());
//                parent.getattributes().setName(parentdept.getName());
//                parent.getattributes().setParentNo(parentdept.getParentNo());
//                parent.getattributes().setTType(parentid == currparentid ? "CDEPT" : "PDEPT");
//                parent.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(parentdept.getName()));
//                parent.setstate("open");
//                parent.setchildren(new ArrayList<EasyuiTreeNode>());
//
//                parentNodes.add(parent);
//                parentid = parentdept.getParentNo();
//            } while (!rootid.equals(parent.getattributes().getNo()));
//
//            //生成父级tree结构
//            for (int i = parentNodes.size() - 1; i >= 0; i--)
//            {
//                parent = parentNodes.get(i);
//
//                if (i == parentNodes.size() - 1)
//                    d.add(parent);
//
//                if (i == 0)
//                    break;
//
//                parent.getchildren().add(parentNodes.get(i - 1));
//            }
//
//            BP.Port.Depts depts = new BP.Port.Depts();
//            depts.Retrieve(BP.Port.DeptAttr.ParentNo, parent.getattributes().getNo(), BP.Port.DeptAttr.Name);
//            BP.Port.Emps emps = new BP.Port.Emps();
//            emps.Retrieve(BP.Port.EmpAttr.FK_Dept, parent.getattributes().getNo(), BP.Port.EmpAttr.Name);
//
//            //增加部门
//            for(BP.Port.Dept dept : depts.ToJavaList())
//            {
//                node = new EasyuiTreeNode();
//                node.setid("DEPT_" + dept.getNo());
//                node.settext(dept.getName());
//                node.seticonCls("icon-department");
//                node.setattributes(new EasyuiTreeNodeAttributes());
//                node.getattributes().setNo(dept.getNo());
//                node.getattributes().setName(dept.getName());
//                node.getattributes().setParentNo(dept.getParentNo());
//                node.getattributes().setTType("DEPT");
//                node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.getName()));
//                node.setstate("closed");
//                node.setchildren(new ArrayList<EasyuiTreeNode>());
//                node.getchildren().add(new EasyuiTreeNode());
//                node.getchildren().get(0).settext ("loading...");
//
//                parent.getchildren().add(node);
//            }
//
//            //增加人员
//            for(BP.Port.Emp emp : emps.ToJavaList())
//            {
//                node = new EasyuiTreeNode();
//                node.setid("EMP_" + parent.getattributes().getNo() + "_" + emp.getNo());
//                node.settext(emp.getName());
//                node.seticonCls("icon-user");
//                node.setchecked(semps.GetEntityByKey(BP.WF.Template.NodeEmpAttr.FK_Emp, emp.getNo()) != null);
//                node.setattributes(new EasyuiTreeNodeAttributes());
//                node.getattributes().setNo(emp.getNo());
//                node.getattributes().setName(emp.getName());
//                node.getattributes().setParentNo(parent.getattributes().getNo());
//                node.getattributes().setTType("EMP");
//                node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(emp.getName()));
//
//                parent.getchildren().add(node);
//
//                jremp = new JsonResultEmp();
//                jremp.setNo(emp.getNo());
//                jremp.setName(emp.getName());
//                jremp.setDeptNo(parent.getattributes().getNo());
//                jremp.setDeptName(parent.getattributes().getName());
//                jremp.setChecked(node.getchecked());
//                jremp.setCode(BP.Tools.chs2py.ConvertStr2Code(emp.getName()));
//
//                jremps.add(jremp);
//            }
        }
        else
        {
            //逐级计算当前部门下的所有父级部门
            do
            {
                BP.GPM.Dept parentdept = new BP.GPM.Dept(parentid);

                parent = new EasyuiTreeNode();
                parent.setid("DEPT_" + parentdept.getNo());
                parent.settext(parentdept.getName());
                parent.seticonCls("icon-department");
                parent.setattributes(new EasyuiTreeNodeAttributes());
                parent.getattributes().setNo(parentdept.getNo());
                parent.getattributes().setName(parentdept.getName());
                parent.getattributes().setParentNo(parentdept.getParentNo());
                parent.getattributes().setTType(parentid == currparentid ? "CDEPT" : "PDEPT");
                parent.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(parentdept.getName()));
                parent.setstate("open");
                parent.setchildren(new ArrayList<EasyuiTreeNode>());

                parentNodes.add(parent);
                parentid = parentdept.getParentNo();
            } while (!rootid.equals(parent.getattributes().getNo()));

            //生成父级tree结构
            for (int i = parentNodes.size() - 1; i >= 0; i--)
            {
                parent = parentNodes.get(i);

                if (i == parentNodes.size() - 1)
                    d.add(parent);

                if (i == 0)
                    break;

                parent.getchildren().add(parentNodes.get(i - 1));
            }

            BP.GPM.Depts depts = new BP.GPM.Depts();
            depts.Retrieve(BP.GPM.DeptAttr.ParentNo, parent.getattributes().getNo());
            BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();
            des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, parent.getattributes().getNo());
            BP.GPM.Emps emps = new BP.GPM.Emps();
            emps.RetrieveAll(BP.GPM.EmpAttr.Name);
            BP.GPM.Emp emp = null;

            //增加部门
            for(BP.GPM.Dept dept : depts.ToJavaList())
            {
                node = new EasyuiTreeNode();
                node.setid("DEPT_" + dept.getNo());
                node.settext(dept.getName());
                node.seticonCls("icon-department");
                node.setattributes(new EasyuiTreeNodeAttributes());
                node.getattributes().setNo(dept.getNo());
                node.getattributes().setName(dept.getName());
                node.getattributes().setParentNo(dept.getParentNo());
                node.getattributes().setTType("DEPT");
                node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.getName()));
                node.setstate("closed");
                node.setchildren(new ArrayList<EasyuiTreeNode>());
                node.getchildren().add(new EasyuiTreeNode());
                node.getchildren().get(0).settext("loading...");

                parent.getchildren().add(node);
            }
            //增加人员
            for(BP.GPM.DeptEmp de : des.ToJavaList())
            {
                emp = (BP.GPM.Emp)emps.GetEntityByKey(BP.GPM.EmpAttr.No, de.getFK_Emp());
                if (emp == null)
                    continue;
                node = new EasyuiTreeNode();
                node.setid("EMP_" + parent.getattributes().getNo() + "_" + emp.getNo());
                node.settext(emp.getName());
                node.seticonCls("icon-user");
                node.setchecked(semps.GetEntityByKey(BP.WF.Template.NodeEmpAttr.FK_Emp, emp.getNo()) != null);
                node.setattributes(new EasyuiTreeNodeAttributes());
                node.getattributes().setNo(emp.getNo());
                node.getattributes().setName(emp.getName());
                node.getattributes().setParentNo(parent.getattributes().getNo());
                node.getattributes().setTType ("EMP");
                node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(emp.getName()));
                parent.getchildren().add(node);
                jremp = new JsonResultEmp();
                jremp.setNo(emp.getNo());
                jremp.setName(emp.getName());
                jremp.setDeptNo(parent.getattributes().getNo());
                jremp.setDeptName(parent.getattributes().getName());
                jremp.setChecked(node.getchecked());
                jremp.setCode(BP.Tools.chs2py.ConvertStr2Code(emp.getName()));
                jremps.add(jremp);
            }
        }
        Map<String,ArrayList<?>> jre = new HashMap<String,ArrayList<?>>();
         jre.put("TreeData", (ArrayList<?>) d);
         jre.put("Depts", (ArrayList<?>) jremps);
         //jr.setInnerData(jre);

         return transction(BP.Tools.Json.ToJson(jre),"");
    }

    /**
     * 获取节点绑定人员信息列表
     */
    public String Dot2DotTreeDeptEmpModel_GetNodeEmps()
    {
    	//JsonResultInnerData jr = new JsonResultInnerData();

        DataTable dt = null;
        String nid = this.GetRequestVal("NodeId");
        int pagesize = Integer.parseInt(this.GetRequestVal("pagesize"));
        int pageidx = Integer.parseInt(this.GetRequestVal("pageidx"));
        String sql = "SELECT pe.No,pe.Name,pd.No DeptNo,pd.Name DeptName FROM WF_NodeEmp wne "
                     + "  INNER JOIN Port_Emp pe ON pe.No = wne.FK_Emp "
                     + "  INNER JOIN Port_Dept pd ON pd.No = pe.FK_Dept "
                     + "WHERE wne.FK_Node = " + nid + " ORDER BY pe.Name";

        dt = DBAccess.RunSQLReturnTable(sql);   //, pagesize, pageidx, "No", "Name", "ASC"
        dt.Columns.Add("Code", String.class);
        dt.Columns.Add("Checked", Boolean.class);

        for(DataRow row : dt.Rows)
        {
        	if (Glo.Plant == BP.WF.Plant.JFlow&&(DBAccess.getAppCenterDBType() == DBType.Oracle))
        		row.setValue("Code", BP.Tools.chs2py.ConvertStr2Code(row.getValue("NAME").toString()));
    		else
        		row.setValue("Code", BP.Tools.chs2py.ConvertStr2Code(row.getValue("Name").toString()));
            row.setValue("Checked", true);
        }

        //对Oracle数据库做兼容性处理
        if (DBAccess.getAppCenterDBType() == DBType.Oracle)
        {
            for(DataColumn col : dt.Columns)
            {
            	if("NO".equals(col.ColumnName))
            	{
            		 col.ColumnName = "No";
            	}else if("NAME".equals(col.ColumnName))
            	{
            		 col.ColumnName = "Name";
            	}else if("DEPTNO".equals(col.ColumnName))
            	{
            		col.ColumnName = "DeptNo";
            	}else if("DEPTNAME".equals(col.ColumnName))
            	{
            		 col.ColumnName = "DeptName";
            	}
            }
        }
        //jr.setInnerData(dt);
        return transction(BP.Tools.Json.ToJson(dt),"");
    }

    public String transction(String innerData,String msg){
    	return "{\"innerData\": "+innerData+",\"msg\":\""+msg+"\"}";
    }
    
    /**
     * 保存节点绑定部门信息
     */
    public String Dot2DotTreeDeptModel_SaveNodeDepts()
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
     * 获取指定部门下一级子部门列表
     */
    public String Dot2DotTreeDeptModel_GetSubDepts()
    {
    	JsonResultInnerData jr = new JsonResultInnerData();

        EasyuiTreeNode node = null;
        JsonResultEmp jrdept = null;
        List<JsonResultEmp> jrdepts = new ArrayList<JsonResultEmp>();
        List<EasyuiTreeNode> d = new ArrayList<EasyuiTreeNode>();
        List<EasyuiTreeNode> parentNodes = new ArrayList<EasyuiTreeNode>();
        EasyuiTreeNode parent = null;
        String rootid = this.GetRequestVal("rootid");
        String parentid = this.GetRequestVal("parentid");
        String nid = this.GetRequestVal("NodeId");
        String currparentid = parentid;
        BP.WF.Template.NodeDepts sdepts = new BP.WF.Template.NodeDepts();

        if (StringHelper.isNullOrEmpty(rootid))
        	throw new RuntimeException("参数rootid不能为空");
        if (StringHelper.isNullOrEmpty(parentid))
        	throw new RuntimeException("参数parentid不能为空");
        if (StringHelper.isNullOrEmpty(nid))
        	throw new RuntimeException("参数nodeid不能为空");

        sdepts.Retrieve(BP.WF.Template.NodeEmpAttr.FK_Node, Integer.parseInt(nid));

        if (BP.Sys.SystemConfig.getOSModel() == OSModel.OneOne)
        {
        	// 20171103
			BP.Port.Depts depts = new BP.Port.Depts();
			depts.Retrieve(BP.Port.DeptAttr.ParentNo, parentid, BP.Port.DeptAttr.Name);
			// 增加部门
			for (BP.Port.Dept dept : depts.ToJavaList()) {
				node = new EasyuiTreeNode();
				node.setid("DEPT_" + dept.getNo());
				node.settext(dept.getName());
				node.seticonCls("icon-department");
				node.setchecked(sdepts.GetEntityByKey(BP.WF.Template.NodeDeptAttr.FK_Dept, dept.getNo()) != null);
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo(dept.getNo());
				node.getattributes().setName(dept.getName());
				node.getattributes().setParentNo(dept.getParentNo());
				node.getattributes().setTType("DEPT");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.getName()));
				node.setstate("closed");
				node.setchildren(new ArrayList<EasyuiTreeNode>());
				node.getchildren().add(new EasyuiTreeNode());
				node.getchildren().get(0).settext("loading...");
				d.add(node);
			}
            
//            //逐级计算当前部门下的所有父级部门
//            do
//            {
//                BP.Port.Dept parentdept = new BP.Port.Dept(parentid);
//
//                parent = new EasyuiTreeNode();
//                parent.setid("DEPT_" + parentdept.getNo());
//                parent.settext(parentdept.getName());
//                parent.seticonCls("icon-department");
//                parent.setattributes(new EasyuiTreeNodeAttributes());
//                parent.getattributes().setNo(parentdept.getNo());
//                parent.getattributes().setName(parentdept.getName());
//                parent.getattributes().setParentNo(parentdept.getParentNo());
//                parent.getattributes().setTType(parentid == currparentid ? "CDEPT" : "PDEPT");
//                parent.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(parentdept.getName()));
//                parent.setstate("open");
//                parent.setchildren(new ArrayList<EasyuiTreeNode>());
//
//                parentNodes.add(parent);
//                parentid = parentdept.getParentNo();
//                
//            } while (!rootid.equals(parent.getattributes().getNo()));
//
//            //生成父级tree结构
//            for (int i = parentNodes.size() - 1; i >= 0; i--)
//            {
//                parent = parentNodes.get(i);
//
//                if (i == parentNodes.size() - 1)
//                    d.add(parent);
//
//                if (i == 0)
//                    break;
//
//                parent.getchildren().add(parentNodes.get(i - 1));
//            }
//
//            BP.Port.Depts depts = new BP.Port.Depts();
//            depts.Retrieve(BP.Port.DeptAttr.ParentNo, parent.getattributes().getNo(), BP.Port.DeptAttr.Name);
//            BP.Port.Emps emps = new BP.Port.Emps();
//            emps.Retrieve(BP.Port.EmpAttr.FK_Dept, parent.getattributes().getNo(), BP.Port.EmpAttr.Name);
//
//            //增加部门
//            for(BP.Port.Dept dept : depts.ToJavaList())
//            {
//                node = new EasyuiTreeNode();
//                node.setid("DEPT_" + dept.getNo());
//                node.settext(dept.getName());
//                node.seticonCls("icon-department");
//                node.setchecked(sdepts.GetEntityByKey(BP.WF.Template.NodeDeptAttr.FK_Dept, dept.getNo()) != null);
//                node.setattributes(new EasyuiTreeNodeAttributes());
//                node.getattributes().setNo(dept.getNo());
//                node.getattributes().setName(dept.getName());
//                node.getattributes().setParentNo(dept.getParentNo());
//                node.getattributes().setTType("DEPT");
//                node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.getName()));
//                node.setstate("closed");
//                node.setchildren(new ArrayList<EasyuiTreeNode>());
//                node.getchildren().add(new EasyuiTreeNode());
//                node.getchildren().get(0).settext("loading...");
//
//                parent.getchildren().add(node);
//
//                jrdept = new JsonResultEmp();
//                jrdept.setNo(dept.getNo());
//                jrdept.setName(dept.getName());
//                jrdept.setDeptNo(dept.getParentNo());
//                jrdept.setDeptName(parent.getattributes().getName());
//                jrdept.setChecked(node.getchecked());
//                jrdept.setCode(node.getattributes().getCode());
//
//                jrdepts.add(jrdept);
//            }
        }
        else
        {
            //逐级计算当前部门下的所有父级部门
            do
            {
                BP.GPM.Dept parentdept = new BP.GPM.Dept(parentid);

                parent = new EasyuiTreeNode();
                parent.setid("DEPT_" + parentdept.getNo());
                parent.settext(parentdept.getName());
                parent.seticonCls("icon-department");
                parent.setattributes(new EasyuiTreeNodeAttributes());
                parent.getattributes().setNo(parentdept.getNo());
                parent.getattributes().setName(parentdept.getName());
                parent.getattributes().setParentNo(parentdept.getParentNo());
                parent.getattributes().setTType(parentid == currparentid ? "CDEPT" : "PDEPT");
                parent.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(parentdept.getName()));
                parent.setstate("open");
                parent.setchildren(new ArrayList<EasyuiTreeNode>());

                parentNodes.add(parent);
                parentid = parentdept.getParentNo();
            } while (!rootid.equals(parent.getattributes().getNo()));

            //生成父级tree结构
            for (int i = parentNodes.size() - 1; i >= 0; i--)
            {
                parent = parentNodes.get(i);

                if (i == parentNodes.size() - 1)
                    d.add(parent);

                if (i == 0)
                    break;

                parent.getchildren().add(parentNodes.get(i - 1));
            }

            BP.GPM.Depts depts = new BP.GPM.Depts();
            depts.Retrieve(BP.GPM.DeptAttr.ParentNo, parent.getattributes().getNo());

            //增加部门
            for(BP.GPM.Dept dept : depts.ToJavaList())
            {
                node = new EasyuiTreeNode();
                node.setid("DEPT_" + dept.getNo());
                node.settext(dept.getName());
                node.seticonCls("icon-department");
                node.setchecked(sdepts.GetEntityByKey(BP.WF.Template.NodeDeptAttr.FK_Dept, dept.getNo()) != null);
                node.setattributes(new EasyuiTreeNodeAttributes());
                node.getattributes().setNo(dept.getNo());
                node.getattributes().setName(dept.getName());
                node.getattributes().setParentNo(dept.getParentNo());
                node.getattributes().setTType("DEPT");
                node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code(dept.getName()));
                node.setstate("closed");
                node.setchildren(new ArrayList<EasyuiTreeNode>());
                node.getchildren().add(new EasyuiTreeNode());
                node.getchildren().get(0).settext("loading...");

                parent.getchildren().add(node);
                jrdept = new JsonResultEmp();
                jrdept.setNo(dept.getNo());
                jrdept.setName(dept.getName());
                jrdept.setDeptNo(dept.getParentNo());
                jrdept.setDeptName(parent.getattributes().getName());
                jrdept.setChecked(node.getchecked());
                jrdept.setCode(node.getattributes().getCode());

                jrdepts.add(jrdept);
            }
        }
      
        Map<String,ArrayList<?>> jre = new HashMap<String,ArrayList<?>>();
        jre.put("TreeData", (ArrayList<?>) d);
        jre.put("Depts", (ArrayList<?>) jrdepts);
        jr.setInnerData(jre);
        return BP.Tools.Json.ToJson(jr);
		}
    
    /**
     * 获取节点绑定人员信息列表
     */
    public String Dot2DotTreeDeptModel_GetNodeDepts()
    {
    	JsonResultInnerData jr = new JsonResultInnerData();

        DataTable dt = null;
        String nid = this.GetRequestVal("nodeid");
        String sql = "SELECT pd.No,pd.Name,pd1.No DeptNo,pd1.Name DeptName FROM WF_NodeDept wnd "
                     + "  INNER JOIN Port_Dept pd ON pd.No = wnd.FK_Dept "
                     + "  LEFT JOIN Port_Dept pd1 ON pd1.No = pd.ParentNo "
                     + "WHERE wnd.FK_Node = " + nid + " ORDER BY pd.Name";

        dt = DBAccess.RunSQLReturnTable(sql);   //, pagesize, pageidx, "No", "Name", "ASC"
        dt.Columns.Add("Code");
        dt.Columns.Add("Checked");

        for(DataRow row : dt.Rows)
        {
            row.setValue("Code", BP.Tools.chs2py.ConvertStr2Code(row.getValue("Name").toString())) ;
            row.setValue("Checked", true);
        }

        //对Oracle数据库做兼容性处理
        if (DBAccess.getAppCenterDBType() == DBType.Oracle)
        {
            for(DataColumn col : dt.Columns)
            {
            	if("NO".equals(col.ColumnName))
            	{
            		col.ColumnName = "No";
            	}else if("NAME".equals(col.ColumnName))
            	{
            		 col.ColumnName = "Name";
            	}else if("DEPTNO".equals(col.ColumnName))
            	{
            		col.ColumnName = "DeptNo";
            	}else if("DEPTNAME".equals(col.ColumnName))
            	{
            		 col.ColumnName = "DeptName";
            	}
            }
        }

        jr.setInnerData(dt.Rows);
        String re = BP.Tools.Json.ToJson(jr);
        if(Glo.Plant == BP.WF.Plant.JFlow){
        	re = re.replaceAll("\"NO\"", "\"No\"").replaceAll("\"NAME\"", "\"Name\"").replaceAll("\"DEPTNO\"", "\"DeptNo\"").replaceAll("\"DEPTNAME\"", "\"DeptName\"");
        }
        return re;
    }
	
    /**
     * 保存节点绑定岗位信息
     */
    public String Dot2DotStationModel_SaveNodeStations()
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
     * 获取部门树根结点
     * @throws Exception 
     */
    public String Dot2DotStationModel_GetStructureTreeRoot() throws Exception
    {
        JsonResultInnerData jr = new JsonResultInnerData();
        EasyuiTreeNode node, subnode;
        List<EasyuiTreeNode> d = new ArrayList<EasyuiTreeNode>();
        String parentrootid = this.GetRequestVal("parentrootid");
        String sql = null;
        DataTable dt = null;

        if (StringHelper.isNullOrEmpty(parentrootid))
            throw new RuntimeException("参数parentrootid不能为空");

        CheckStationTypeIdxExists();
        boolean isUnitModel = DBAccess.IsExitsTableCol("Port_Dept", "IsUnit");

        if (isUnitModel)
        {
            boolean isValid = DBAccess.IsExitsTableCol("Port_Station", "FK_Unit");

            if (!isValid)
                isUnitModel = false;
        }
        

        if (isUnitModel)
        {
            sql = String.format("SELECT No,Name,ParentNo FROM Port_Dept WHERE IsUnit = 1 AND ParentNo = '{0}'", parentrootid);
            dt = DBAccess.RunSQLReturnTable(sql);

            if (dt.Rows.size() == 0)
                dt.Rows.AddRow("-1", "无单位数据", parentrootid); 

            node = new EasyuiTreeNode();
            node.setid("UNITROOT_" + dt.Rows.get(0).getValue("No"));
            node.settext(String.valueOf(dt.Rows.get(0).getValue("Name")));
            node.seticonCls("icon-department");
            node.setattributes(new EasyuiTreeNodeAttributes());
            node.getattributes().setNo(String.valueOf(dt.Rows.get(0).getValue("No")));
            node.getattributes().setName(String.valueOf(dt.Rows.get(0).getValue("Name")));
            node.getattributes().setParentNo(parentrootid);
            node.getattributes().setTType("UNITROOT");
            node.setstate("closed");

            if (node.gettext() != "无单位数据")
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

        //return Newtonsoft.Json.JsonConvert.SerializeObject(jr);
        return BP.Tools.Json.ToJson(jr);
		} 
    
    
    /**
     * 获取指定部门下一级子部门及人员列表
     * @throws Exception 
     */
    public final String Dot2DotStationModel_GetSubUnits() throws Exception
	{
		String parentid = this.GetRequestVal("parentid");
		String nid = this.GetRequestVal("nodeid");
		String tp = this.GetRequestVal("stype"); //ST,UNIT
		String ttype = this.GetRequestVal("ttype"); //STROOT,UNITROOT,ST,CST,S

		if (StringUtils.isEmpty(parentid))
		{
			throw new RuntimeException("参数parentid不能为空");
		}
		if (StringUtils.isEmpty(nid))
		{
			throw new RuntimeException("参数nodeid不能为空");
		}

		EasyuiTreeNode node = null;
		java.util.ArrayList<EasyuiTreeNode> d = new java.util.ArrayList<EasyuiTreeNode>();
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
					node.settext((String)((row.getValue("Name") instanceof String) ? row.getValue("Name") : null));
					node.seticonCls("icon-department");
					node.setattributes(new EasyuiTreeNodeAttributes());
					node.getattributes().setNo((String)((row.getValue("No") instanceof String) ? row.getValue("No") : null));
					node.getattributes().setName((String)((row.getValue("Name") instanceof String) ? row.getValue("Name") : null));
					node.getattributes().setParentNo("-1");
					node.getattributes().setTType("ST");
					node.setstate("closed");
					node.setchildren(new java.util.ArrayList<EasyuiTreeNode>());
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
					node.settext((String)((row.getValue("Name") instanceof String) ? row.getValue("Name") : null));
					node.seticonCls("icon-user");
					node.setchecked(sts.GetEntityByKey(BP.WF.Template.NodeStationAttr.FK_Station, row.getValue("No")) != null);
					node.setattributes(new EasyuiTreeNodeAttributes());
					node.getattributes().setNo((String)((row.getValue("No") instanceof String) ? row.getValue("No") : null));
					node.getattributes().setName((String)((row.getValue("Name") instanceof String) ? row.getValue("Name") : null));
					node.getattributes().setParentNo((String)((row.getValue("FK_StationType") instanceof String) ? row.getValue("FK_StationType") : null));
					node.getattributes().setParentName((String)((row.getValue("FK_StationTypeName") instanceof String) ? row.getValue("FK_StationTypeName") : null));
					node.getattributes().setTType("S");
					node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code((String)((row.getValue("Name") instanceof String) ? row.getValue("Name") : null)));

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
				node.setid("UNIT_" + dept.getValue("No"));
				node.settext((String)((dept.getValue("Name") instanceof String) ? dept.getValue("Name") : null));
				node.seticonCls("icon-department");
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo((String)((dept.getValue("No") instanceof String) ? dept.getValue("No") : null));
				node.getattributes().setName((String)((dept.getValue("Name") instanceof String) ? dept.getValue("Name") : null));
				node.getattributes().setParentNo((String)((dept.getValue("ParentNo") instanceof String) ? dept.getValue("ParentNo") : null));
				node.getattributes().setTType("UNIT");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code((String)((dept.getValue("Name") instanceof String) ? dept.getValue("Name") : null)));
				node.setstate("closed");
				node.setchildren(new java.util.ArrayList<EasyuiTreeNode>());
				node.getchildren().add(new EasyuiTreeNode());
				node.getchildren().get(0).settext("loading...");

				d.add(node);
			}

			dt = DBAccess.RunSQLReturnTable(String.format("SELECT ps.No,ps.Name,pst.No FK_StationType, pst.Name FK_StationTypeName,ps.FK_Unit,pd.Name FK_UnitName FROM Port_Station ps" + " INNER JOIN Port_StationType pst ON pst.No = ps.FK_StationType" + " INNER JOIN Port_Dept pd ON pd.No = ps.FK_Unit" + " WHERE ps.FK_Unit = '%1$s' ORDER BY pst.%2$s ASC,ps.Name ASC", parentid, sortField));

			//增加岗位
			for (DataRow st : dt.Rows)
			{
				node = new EasyuiTreeNode();
				node.setid("S_" + st.getValue("FK_Unit") + "_" + st.getValue("No"));
				node.settext(st.getValue("Name") + ".getValue(" + st.getValue("FK_StationTypeName") + "]");
				node.seticonCls("icon-user");
				node.setchecked(sts.GetEntityByKey(BP.WF.Template.NodeStationAttr.FK_Station, st.getValue("No")) != null);
				node.setattributes(new EasyuiTreeNodeAttributes());
				node.getattributes().setNo((String)((st.getValue("No") instanceof String) ? st.getValue("No") : null));
				node.getattributes().setName((String)((st.getValue("Name") instanceof String) ? st.getValue("Name") : null));
				node.getattributes().setParentNo((String)((st.getValue("FK_Unit") instanceof String) ? st.getValue("FK_Unit") : null));
				node.getattributes().setParentName((String)((st.getValue("FK_UnitName") instanceof String) ? st.getValue("FK_UnitName") : null));
				node.getattributes().setTType("S");
				node.getattributes().setCode(BP.Tools.chs2py.ConvertStr2Code((String)((st.getValue("Name") instanceof String) ? st.getValue("Name") : null)));

				d.add(node);
			}
		}
		return BP.Tools.Json.ToJson(d);
	}
    
    /**
     * 获取节点绑定人员信息列表
     * @throws Exception 
     */
    public String Dot2DotStationModel_GetNodeStations() throws Exception
    {
        JsonResultInnerData jr = new JsonResultInnerData();

        DataTable dt = null;
        String nid = this.GetRequestVal("nodeid");
        int pagesize = Integer.parseInt(this.GetRequestVal("pagesize"));
        int pageidx = Integer.parseInt(this.GetRequestVal("pageidx"));
        String st = this.GetRequestVal("stype");
        String sql ="";
        String sortField = CheckStationTypeIdxExists() ? "Idx" : "No";

        if ("UNIT".equals(st))
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

        dt = DBAccess.RunSQLReturnTable(sql);   //, pagesize, pageidx, "No", "Name", "ASC"
        dt.Columns.Add("Code");
        dt.Columns.Add("Checked");

        for(DataRow row : dt.Rows)
        {
            row.setValue("Code", BP.Tools.chs2py.ConvertStr2Code(row.getValue("Name").toString()));
            row.setValue("Checked", true);
        }

        //对Oracle数据库做兼容性处理
        if (DBAccess.getAppCenterDBType() == DBType.Oracle)
        {
            for(DataColumn col : dt.Columns)
            {
            	if("NO".equals(col.ColumnName))
            	{
            		  col.ColumnName = "No";
            	}else if("NAME".equals(col.ColumnName))
            	{
            		col.ColumnName = "Name";
            	}else if("UNITNO".equals(col.ColumnName))
            	{
            		col.ColumnName = "UnitNo";
            	}else if("UNITNAME".equals(col.ColumnName))
            	{
            		 col.ColumnName = "UnitName";
            	}
            }
        }
        jr.setInnerData(dt.Rows);
        String re = BP.Tools.Json.ToJson(jr);
        if(Glo.Plant == BP.WF.Plant.JFlow){
        	re = re.replaceAll("\"NO\"", "\"No\"").replaceAll("\"NAME\"", "\"Name\"").replaceAll("\"UNITNO\"", "\"UnitNo\"").replaceAll("\"UNITNAME\"", "\"UnitName\"");
        }
        return re;
    }
    
    /** 判断Port_StationType表中是否含有Idx字段
	 
	 @return 
     * @throws Exception 
     */
	public final boolean CheckStationTypeIdxExists() throws Exception
	{
		if (DBAccess.IsExitsTableCol("Port_StationType", "Idx") == false)
		{
			if (DBAccess.IsView("Port_StationType") == false)
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
	
	/** 获取部门树根结点
	 
	 @return 
*/
	public final String Dot2DotTreeDeptModel_GetStructureTreeRoot()
	{
		JsonResultInnerData jr = new JsonResultInnerData();

		EasyuiTreeNode node = null;
		java.util.ArrayList<EasyuiTreeNode> d = new java.util.ArrayList<EasyuiTreeNode>();
		String parentrootid = this.GetRequestVal("parentrootid");
		String nodeid = this.GetRequestVal("nodeid");

		if (StringUtils.isEmpty(parentrootid))
		{
			throw new RuntimeException("参数parentrootid不能为空");
		}
		if (StringUtils.isEmpty(nodeid))
		{
			throw new RuntimeException("参数nodeid不能为空");
		}

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.WF.Port.Dept dept = new BP.WF.Port.Dept();

			if (dept.Retrieve(BP.WF.Port.DeptAttr.ParentNo, parentrootid) == 0)
			{
				dept.setNo("-1");
				dept.setName("无部门");
				dept.setParentNo("");
			}

			node = new EasyuiTreeNode();
			node.setid("DEPT_" + dept.getNo());
			node.settext(dept.getName());
			node.seticonCls("icon-department");
			node.setattributes(new EasyuiTreeNodeAttributes());
			node.getattributes().setNo(dept.getNo());
			node.getattributes().setName(dept.getName());
			node.getattributes().setParentNo(dept.getParentNo());
			node.setchecked(new NodeDept().IsExit(NodeDeptAttr.FK_Node, Integer.parseInt(nodeid), NodeDeptAttr.FK_Dept, dept.getNo()));
			node.getattributes().setTType("DEPT");
			node.setstate("closed");
			node.setchildren(new java.util.ArrayList<EasyuiTreeNode>());
			node.getchildren().add(new EasyuiTreeNode());
			node.getchildren().get(0).settext("loading...");

			d.add(node);
		}
		else
		{
			BP.GPM.Dept dept = new BP.GPM.Dept();

			if (dept.Retrieve(BP.GPM.DeptAttr.ParentNo, parentrootid) == 0)
			{
				dept.setNo("-1");
				dept.setName("无部门");
				dept.setParentNo("");
			}

			node = new EasyuiTreeNode();
			node.setid("DEPT_" + dept.getNo());
			node.settext(dept.getName());
			node.seticonCls("icon-department");
			node.setattributes(new EasyuiTreeNodeAttributes());
			node.getattributes().setNo(dept.getNo());
			node.getattributes().setName(dept.getName());
			node.getattributes().setParentNo(dept.getParentNo());
			node.setchecked(new NodeDept().IsExit(NodeDeptAttr.FK_Node, Integer.parseInt(nodeid), NodeDeptAttr.FK_Dept, dept.getNo()));
			node.getattributes().setTType("DEPT");
			node.setstate("closed");
			node.setchildren(new java.util.ArrayList<EasyuiTreeNode>());
			node.getchildren().add(new EasyuiTreeNode());
			node.getchildren().get(0).settext("loading...");

			d.add(node);
		}

		jr.setInnerData(d);

		return BP.Tools.Json.ToJson(jr);
	}
}

