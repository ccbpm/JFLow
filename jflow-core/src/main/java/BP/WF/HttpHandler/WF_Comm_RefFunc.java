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
import BP.Difference.Handler.WebContralBase;
import BP.GPM.StationType;
import BP.Sys.OSModel;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Comm.JsonResultEmp;
import BP.WF.Comm.JsonResultInnerData;
import BP.WF.Comm.JsonResultStation;
import BP.WF.Template.NodeDept;
import BP.WF.Template.NodeDeptAttr;

public class WF_Comm_RefFunc extends WebContralBase {
	
	
	/**
	 * 构造函数
	 */
	public WF_Comm_RefFunc()
	{
	
	}
	
	/**
	 * 保存节点绑定人员信息
	 * @return
	 * @throws Exception 
	 */
    public String Dot2DotTreeDeptEmpModel_SaveNodeEmps() throws Exception
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
     * @throws Exception 
     */
    public String Dot2DotTreeDeptModel_SaveNodeDepts() throws Exception
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
     * @throws Exception 
     */
    public String Dot2DotStationModel_SaveNodeStations() throws Exception
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
	

}

