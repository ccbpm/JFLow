package cn.jflow.controller.wf.admin.CCBPMDesigner;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.En.ClassFactory;
import BP.En.Entity;
import BP.GPM.DeptEmp;
import BP.GPM.DeptEmpStation;
import BP.GPM.DeptStation;
import BP.GPM.Station;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Port.Dept;
import BP.WF.Port.EmpStation;
import BP.WF.Template.DataStoreModel;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSortAttr;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.WorkflowDefintionManager;
import BP.Web.WebUser;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.util.JsonPluginsUtil;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin/CCBPMDesigner1")
@Scope("request")
public class CCBPMDesignerBase extends BaseController {

	/**
	 * http请求
	 */
	private HttpContext private_Context;

	public final HttpContext get_Context() {
		return private_Context;
	}

	public final void set_Context(HttpContext value) {
		private_Context = value;
	}

	/**
	 * 公共方法获取值
	 * 
	 * @param param
	 *            参数名
	 * @return
	 */
	public final String getUTF8ToString(String param) {
		String str=this.getRequest().getParameter(param);
		if(!"".equals(str)&&str!=null && !"null".equals(str)){
			try {
				return java.net.URLDecoder.decode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return "";
		}
	}
	/**
	 * 流程编号
	 */
	public final String getFK_Flow() {
		return getUTF8ToString("FK_Flow");
	}
	
	public final String getNo(){
		return getUTF8ToString("No");
	}
	@RequestMapping(value = "/ProcessRequest")
	@ResponseBody
	public void ProcessRequest(HttpServletRequest request,HttpServletResponse response) {
		PrintWriter out =null;
		
		String msg = "";

		msg = defaultProcessRequest(request, response);
		
		
		try{
 			out = response.getWriter();
 			out.write(msg);
        }
        catch (Exception ex)
        {
            msg = "CCBPMDesignerBase.java:" + ex.getMessage();
            ex.printStackTrace();
           
        }finally
		{
			if(null != out)
			{
				out.close();
			}
		}
    }
	
	/**
	 * 默认访问方法
	 * @return
	 */
	public String defaultProcessRequest(HttpServletRequest request,HttpServletResponse response)
	{
		String method = request.getParameter("method");
		// 返回值
		String msg="";
		// 获取用户信息
		if("WebUserInfo".equals(method)){
			msg = GetWebUserInfo();
		}
		// 获取流程树数据
		else if("GetFlowTree".equals(method)){
			msg = GetFlowTreeTable();
		}
		// 获取表单库数据
		else if("GetFormTree".equals(method)){
			msg = GetFormTreeTable();
		}
		// 获取数据源数据
		else if("GetSrcTree".equals(method)){
			msg = GetSrcTreeTable();
		}
		// 获取组织结构数据
		else if("GetStructureTree".equals(method)){
			msg = GetStructureTreeTable();
		}
		//获取组织结构根结点数据
		else if ("GetStructureTreeRoot".equals(method)){
			msg = GetStructureTreeRootTable();
		}
		//获取指定部门下一级子部门及岗位列表
		else if ("GetSubDepts".equals(method)){
			msg = GetSubDeptsTable();
		}
		//根据部门、岗位获取人员列表
		else if ("GetEmpsByStation".equals(method)){
			msg = GetEmpsByStationTable();
		}
	
		// 获取流程绑定表单列表
		else if("GetBindingForms".equals(method)){
			msg =  GetBindingFormsTable();
		}
		// 获取流程节点列表
		else if("GetFlowNodes".equals(method)){
			msg = GetFlowNodesTable();
		}
		// 公共方法
		else if("Do".equals(method)){
			msg = Do();
		}
		
		else if("DelFrm".equals(method)){
			msg = DelFrm();
		}
		// 使管理员登录
		else if("LetLogin".equals(method)){
			msg = "admin".equals(WebUser.getNo()) ? "": LetAdminLogin("CH", true);
		}
		return msg;
	}
	
	/**
	 * 根据部门、岗位获取人员列表
	 * @return
	 */
	private String GetEmpsByStationTable()
	{
		String deptid = this.getRequest().getParameter("deptid");
		String stid = this.getRequest().getParameter("stationid");
		
		if (StringHelper.isNullOrEmpty(deptid)|| StringHelper.isNullOrEmpty(stid))
			return "[]";

		DataTable dt = new DataTable();
		dt.Columns.Add("NO",String.class);	
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);
		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.GPM.DeptEmp de = null;	
			BP.Port.Emp emp = null; 	
			BP.WF.Port.EmpStations ess = new BP.WF.Port.EmpStations(stid); 	
    
			BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();	
			des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, deptid); 
    
			BP.Port.Emps emps = new BP.Port.Emps();
			emps.RetrieveAll();
			for (int i = 0; i < ess.size(); i++) {
				BP.WF.Port.EmpStation es  =  (EmpStation) ess.get(i);
   	
				Object tempVar = des.GetEntityByKey(BP.GPM.DeptEmpAttr.FK_Emp, es.getFK_Emp());
				de = (BP.GPM.DeptEmp)((tempVar instanceof BP.GPM.DeptEmp) ? tempVar : null);
				if (de == null)
				{
					continue;
				}
				Object tempVar2 = emps.GetEntityByKey(es.getFK_Emp());
				emp = (BP.Port.Emp)((tempVar2 instanceof BP.Port.Emp) ? tempVar2 : null);
				dt.Rows.Add(emp.getNo(), deptid + "|" + stid, emp.getName(), "EMP");
			}
		}	
		else 	
		{
			BP.GPM.Emp emp = null;	
			BP.GPM.Emps emps = new BP.GPM.Emps();
			emps.RetrieveAll();

			BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();

			dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, deptid, BP.GPM.DeptEmpStationAttr.FK_Station, stid);

			for(int j =0;j<dess.size() ;j++) 	
			{
				BP.GPM.DeptEmpStation des = (DeptEmpStation) dess.get(j);
				Object tempVar3 = emps.GetEntityByKey(des.getFK_Emp());
				emp = (BP.GPM.Emp)((tempVar3 instanceof BP.GPM.Emp) ? tempVar3 : null);
				dt.Rows.Add(emp.getNo(), deptid + "|" + stid, emp.getName(), "EMP");
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}
	private String GetStructureTreeRootTable()	
	{ 	
		DataTable dt = new DataTable(); 	
		dt.Columns.Add("NO", String.class);	
        dt.Columns.Add("PARENTNO", String.class); 	
        dt.Columns.Add("NAME", String.class); 	
        dt.Columns.Add("TTYPE", String.class); 	
        String parentrootid = this.getRequest().getParameter("parentrootid");
 	
        if (BP.WF.Glo.getOSModel() == OSModel.OneOne) 	
        {	
        	BP.WF.Port.Dept dept = new BP.WF.Port.Dept();
            if (dept.Retrieve(BP.WF.Port.DeptAttr.ParentNo, parentrootid) == 0) 	
            {	
            	dept.setNo("-1");;	
                dept.setName("无部门");	
                dept.setParentNo("");
            }
            dt.Rows.Add(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT"); 	
        }	
        else	
        { 	
        	BP.GPM.Dept dept = new BP.GPM.Dept();
        	if (dept.Retrieve(BP.GPM.DeptAttr.ParentNo, parentrootid) == 0)
        	{
        		dept.setNo("-1");;	
        		dept.setName("无部门");	
        		dept.setParentNo("");
        	}
        	dt.Rows.Add(dept.getNo(), dept.getParentNo(), dept.getName(),"DEPT");
        }
        return BP.Tools.Json.ToJson(dt);
	}
	
	/**
	 * 获取指定部门下一级子部门及岗位列表
	 * @return
	 */
	private String GetSubDeptsTable() {
		DataTable dt = new DataTable();
		dt.Columns.Add("NO");
		dt.Columns.Add("PARENTNO");
		dt.Columns.Add("NAME");
		dt.Columns.Add("TTYPE");

		String rootid = this.getRequest().getParameter("rootid");
		if (BP.WF.Glo.getOSModel() == OSModel.OneOne) {
			BP.Port.Depts depts = new BP.Port.Depts();
			depts.Retrieve(BP.Port.DeptAttr.ParentNo, rootid,
					BP.Port.DeptAttr.Name);
			BP.Port.Stations sts = new BP.Port.Stations();
			sts.RetrieveAll();
			BP.Port.Emps emps = new BP.Port.Emps();
			emps.Retrieve(BP.Port.EmpAttr.FK_Dept, rootid, BP.Port.EmpAttr.Name);
			BP.Port.EmpStations empsts = new BP.Port.EmpStations();
			empsts.RetrieveAll();

			BP.Port.EmpStations ess = null;
			List<String> insts = new ArrayList<String>();
			List<BP.Port.Emp> inemps = new ArrayList<BP.Port.Emp>();

			// 增加部门
			for (BP.Port.Dept dept : depts.ToJavaList()) {
				dt.Rows.Add(dept.getNo(), dept.getParentNo(), dept.getName(),
						"DEPT");
			}

			// 增加岗位
			for (BP.Port.Emp emp : emps.ToJavaList()) {
				ess = new BP.Port.EmpStations();
				ess.Retrieve(BP.Port.EmpStationAttr.FK_Emp, emp.getNo());

				for (BP.Port.EmpStation es : ess.ToJavaList()) {
					if (insts.contains(es.getFK_Station()))
						continue;

					insts.add(es.getFK_Station());
					dt.Rows.Add(es.getFK_Station(), rootid,
							es.getFK_StationT(), "STATION");
				}

				if (ess.size() == 0)
					inemps.add(emp);
			}
			// 增加没有岗位的人员
			for (BP.Port.Emp emp : inemps) {
				dt.Rows.Add(emp.getNo(), rootid, emp.getName(), "EMP");
			}
		} else {
			BP.GPM.Depts depts = new BP.GPM.Depts();
			depts.Retrieve(BP.GPM.DeptAttr.ParentNo, rootid);
			BP.GPM.Stations sts = new BP.GPM.Stations();
			sts.RetrieveAll();
			BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
			dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, rootid);
			BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();
			des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, rootid);
			BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
			dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, rootid);
			BP.GPM.Station stt = null;
			BP.GPM.Emp emp = null;
			List<String> inemps = new ArrayList<String>();

			for (BP.GPM.Dept dept : depts.ToJavaList()) {
				// 增加部门
				dt.Rows.Add(dept.getNo(), dept.getParentNo(), dept.getName(),
						"DEPT");
			}

			// 增加部门岗位
			for (BP.GPM.DeptStation ds : dss.ToJavaList()) {
				stt = (Station) sts.GetEntityByKey(ds.getFK_Station());

				if (stt == null)
					continue;

				dt.Rows.Add(ds.getFK_Station(), rootid, stt.getName(),
						"STATION");
			}

			// 增加没有岗位的人员
			for (BP.GPM.DeptEmp de : des.ToJavaList()) {
				if (dess.GetEntityByKey(BP.GPM.DeptEmpStationAttr.FK_Emp,
						de.getFK_Emp()) == null) {
					if (inemps.contains(de.getFK_Emp()))
						continue;

					inemps.add(de.getFK_Emp());
				}
			}
			for (String inemp : inemps) {
				emp = new BP.GPM.Emp(inemp);
				dt.Rows.Add(emp.getNo(), rootid, emp.getName(), "EMP");
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}
	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	private String GetWebUserInfo() {
		AjaxJson j = new AjaxJson();
		try {
			if (WebUser.getNo() == null) {
				j.setSuccess(false);
				j.setMsg("当前用户没有登录，请登录后再试。");
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("data", new Object() {
				});
				j.setAttributes(m);
				JSONArray jsonArray = JSONArray.fromObject(j);
				return jsonArray.toString();
			}
			BP.Port.Emp emp = new BP.Port.Emp(WebUser.getNo());
			j.setSuccess(true);
			j.setMsg("");
			JSONObject member1 = new JSONObject();
			member1.put("No", emp.getNo());
			member1.put("Name", emp.getName());
			member1.put("FK_Dept", emp.getFK_Dept());
			member1.put("SID", emp.getSID());
			j.setData(member1);
			return JsonPluginsUtil.beanToJson(j);
		} catch (RuntimeException ex) {
			j.setSuccess(false);
			j.setMsg(ex.getMessage());
			return JsonPluginsUtil.beanToJson(j);
		}
	}

	private StringBuilder sbJson = new StringBuilder();

	/**
	 * 获取流程树数据
	 * 
	 * @return 返回结果Json,流程树
	 */
	private String GetFlowTree() {
		String sql = "SELECT 'F'+No No,'F'+ParentNo ParentNo,Name, Idx, 1 IsParent,'FLOWTYPE' TType,-1 DType FROM WF_FlowSort"
				+ "\r\n"
				+ " union "
				+ "\r\n"
				+ " SELECT No, 'F'+FK_FlowSort as ParentNo,Name,Idx,0 IsParent,'FLOW' TType,DType FROM WF_Flow";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			sql = "SELECT 'F'||No No,'F'||ParentNo ParentNo,Name, Idx, 1 IsParent,'FLOWTYPE' TType,-1 DType FROM WF_FlowSort"
					+ "\r\n"
					+ " union "
					+ "\r\n"
					+ "  SELECT No, 'F'||FK_FlowSort as ParentNo,Name,Idx,0 IsParent,'FLOW' TType,DType FROM WF_Flow";
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		DataTable dt_Clone = dt.clone();

		// 1.流程类别；2.流程
		for (DataRow row : dt.Rows) {
			// dt_Clone.Rows.Add(row.ItemArray);

			DataRow dr = dt_Clone.NewRow();
			for (DataColumn dc : row.columns) {
				dr.put(dc.ColumnName, row.getValue(dc.ColumnName));
			}
			dt_Clone.Rows.add(dr);
		}

		// 3.流程云数据；4.共有云；5.私有云

		dt_Clone.Rows.Add("FlowCloud", "-1", "流程云", 0, 1, "FLOWCLOUD", "-1");
		dt_Clone.Rows.Add("ShareFlow", "FlowCloud", "共有流程云", 0, 0, "SHAREFLOW",
				"-1");
		dt_Clone.Rows.Add("PriFlow", "FlowCloud", "私有流程云", 0, 0, "PRIFLOW",
				"-1");

		sbJson.delete(0, sbJson.length());

		String sTmp = "";
		if (dt_Clone != null && dt_Clone.Rows.size() > 0) {
			GetTreeJsonByTable(dt_Clone, "", "ParentNo", "No", "Name",
					"IsParent", "", new String[] { "TType", "DType" });
		}
		sTmp += sbJson.toString();
		return sTmp;
	}
	
	private String GetFlowTreeTable() {
		String sql = "SELECT 'F'+No No,'F'+ParentNo ParentNo,Name, Idx, 1 IsParent,'FLOWTYPE' TType,-1 DType FROM WF_FlowSort"
				+ "\r\n"
				+ "                           union "
				+ "\r\n"
				+ "                           SELECT No, 'F'+FK_FlowSort as ParentNo,(NO + '.' + NAME) Name,Idx,0 IsParent,'FLOW' TType,DType FROM WF_Flow";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			sql = "SELECT 'F'||No No,'F'||ParentNo ParentNo,Name, Idx, 1 IsParent,'FLOWTYPE' TType,-1 DType FROM WF_FlowSort"
					+ "\r\n"
					+ "                        union "
					+ "\r\n"
					+ "                        SELECT No, 'F'||FK_FlowSort as ParentNo,NO||'.'||NAME Name,Idx,0 IsParent,'FLOW' TType,DType FROM WF_Flow";
		} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL){
			sql = "SELECT concat('F',No) No,concat('F',ParentNo) ParentNo,Name, Idx, 1 IsParent,'FLOWTYPE' TType,-1 DType FROM WF_FlowSort"
					+ "\r\n"
					+ "                           union "
					+ "\r\n"
					+ "                           SELECT No, concat('F',FK_FlowSort) as ParentNo,CONCAT(NO, '.', NAME) Name,Idx,0 IsParent,'FLOW' TType,DType FROM WF_Flow";
		}
		
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		// 判断是否为空，如果为空，则创建一个流程根结点，added by liuxc,2016-01-24
		if (dt.Rows.size() == 0) {
			FlowSort fs = new FlowSort();
			fs.setNo("99");
			fs.setParentNo("0");
			fs.setName("流程树");
			fs.Insert();

			dt.Rows.Add("F99", "F0", "流程树", 0, 1, "FLOWTYPE", -1);
		} else {
			Map<String, Object> filer = new HashMap<String, Object>();
			filer.put("Name", "流程树");
			List<DataRow> rows;
			try {
				rows = dt.Select(filer);
				if (rows.size() > 0 && !"F0".equals(rows.get(0).get("ParentNo"))) {
					rows.get(0).setValue("ParentNo", "F0");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}
	
	private String GetFlowNodesTable() {
		String fk_flow = getUTF8ToString("fk_flow");
		if (StringHelper.isNullOrEmpty(fk_flow)) {
			return "[]";
		}

		StringBuilder sql = new StringBuilder();

		switch (DBAccess.getAppCenterDBType()) {
		case MSSQL:
			sql.append("SELECT CAST(wn.NodeID AS VARCHAR) AS No,");
			sql.append("       ('(' + CAST(wn.NodeID AS VARCHAR) + ')' + wn.Name) AS Name,");
			break;
		case MySQL:
			sql.append("SELECT CAST(wn.NodeID AS CHAR) AS No,");
			sql.append("       CONCAT('(',wn.NodeID,')',wn.Name) AS Name,");
			break;
		case Informix:
			sql.append("SELECT CAST(wn.NodeID AS CHAR) AS No,"); // 未证实
			sql.append("       ('(' || wn.NodeID || ')' || wn.Name) AS Name,"); // 未证实
			break;
		case Oracle:
			sql.append("SELECT to_char(wn.NodeID) AS No,");
			sql.append("       ('(' || wn.NodeID || ')' || wn.Name) AS Name,");
			break;
		case DB2:
			sql.append("SELECT CHAR(wn.NodeID) AS No,"); // 未证实
			sql.append("       ('(' || wn.NodeID || ')' || wn.Name) AS Name,"); // 未证实，也可用CONCAT函数，但此函数只支持两两连接，且两个必须都是字符串
			break;
		case Access:
			sql.append("SELECT CStr(wn.NodeID) AS No,");
			sql.append("       ('(' & wn.NodeID & ')' & wn.Name) AS Name,"); // Access中的别名必须加AS操作符
			break;
		case Sybase:
			sql.append("SELECT CONVERT(VARCHAR, wn.NodeID) AS No,"); // 未证实
			sql.append("       ('(' + CONVERT(VARCHAR, wn.NodeID) + ')' + wn.Name) AS Name,"); // 未证实
			break;
		}

		sql.append("       NULL AS ParentNo,");
		sql.append("       'NODE' AS TType,");
		sql.append("       -1 AS DType,");
		sql.append("       0 AS IsParent");
		sql.append("FROM   WF_Node wn");
		sql.append("WHERE  wn.FK_Flow = '{0}'");
		sql.append("ORDER BY");
		sql.append("       wn.Step ASC");

		DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql.toString(),
				fk_flow));
		JSONArray jsonArray = JSONArray.fromObject(dt);
		return jsonArray.toString();
	}
	
	private String GetBindingFormsTable() {
		String fk_flow = getUTF8ToString("fk_flow");
		if (StringHelper.isNullOrEmpty(fk_flow)) {
			return "[]";
		}

		StringBuilder sql = new StringBuilder();
		switch (DBAccess.getAppCenterDBType()) {
			case Oracle:
				sql.append("SELECT wfn.FK_Frm NO,smd. NAME,NULL ParentNo,'FORM' TType,'-1' DType,'0' IsParent ");
				sql.append("FROM WF_FrmNode wfn INNER JOIN Sys_MapData smd ON smd.NO = wfn.FK_Frm WHERE wfn.FK_Flow = '{0}' ");
				sql.append("AND wfn.FK_Node  = (SELECT NodeID from ( SELECT NodeID,ROWNUM from WF_Node  where  FK_Flow = '{0}' ");
				sql.append("  ORDER BY Step  )where ROWNUM=1)");
				break;
			case MSSQL:
				sql.append("SELECT wfn.FK_Frm No,");
				sql.append("       smd.Name,");
				sql.append("       NULL ParentNo,");
				sql.append("       'FORM' TType,");
				sql.append("       -1 DType,");
				sql.append("       0 IsParent");
				sql.append("	FROM   WF_FrmNode wfn");
				sql.append("       INNER JOIN Sys_MapData smd");
				sql.append("            ON  smd.No = wfn.FK_Frm");
				sql.append("    WHERE  wfn.FK_Flow = '{0}'");
				sql.append("       AND wfn.FK_Node = (");
				sql.append("               SELECT TOP 1 wn.NodeID");
				sql.append("               FROM   WF_Node wn");
				sql.append("               WHERE  wn.FK_Flow = '{0}'");
				sql.append("               ORDER BY");
				sql.append("                      wn.Step ASC");
				sql.append("           )");
			break;
		}
		
		DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql.toString(),fk_flow));
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}

	private String GetFormTreeTable() {
		String sqls = "SELECT No ,ParentNo,Name, Idx, 1 IsParent, 'FORMTYPE' TType, DBSrc FROM Sys_FormTree;"
				+ "\r\n"
				+ " SELECT No, FK_FrmSort as ParentNo,Name,Idx,0 IsParent, 'FORM' TType FROM Sys_MapData   where AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree);"
				+ "\r\n"
				+ "  SELECT ss.No,'' ParentNo,ss.Name,0 Idx, 1 IsParent, 'SRC' TType FROM Sys_SFDBSrc ss ORDER BY ss.DBSrcType ASC;";

		DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);
		DataTable dt = ds.Tables.get(1).clone();

		Map<String, Object> filer = new HashMap<String, Object>();
		filer.put("name", "表单库");
		List<DataRow> rows = null;
		try {
			rows = ds.Tables.get(0).Select(filer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataRow rootRow = null;
		
		if (rows.size() == 0) {
			rootRow = dt.Rows.Add("0", null, "表单库", 0, 1, "FORMTYPE");
		} else {
			rootRow = dt.Rows.Add(rows.get(0).get("no"), null,
					rows.get(0).get("name"), rows.get(0).get("idx"), rows
							.get(0).get("isparent"), rows.get(0).get("ttype"));
		}

		for (DataRow row : ds.Tables.get(2).Rows) {
			//这个是添加本机数据源（默认）节点，由于一些原因注释掉，如需放开请将下方dr.setValue("parentno", "Src_" + no);注释打开并注释掉dr.setValue("parentno", no);
			/*dt.Rows.Add("Src_" + rootRow.get("no"), rootRow.get("no"), 
					row.get("name"), row.get("idx"), row.get("isparent"),
					row.get("ttype"));*/
			
			try {
				rows = ds.Tables.get(0).select("dbsrc='" + row.get("no") + "' and name != '表单库'");
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (DataRow dr : rows) {
				Object no = rootRow.get("no");

				if (no != null && !"".equals(no)&& no.equals(dr.get("parentno"))) {
					//dr.setValue("parentno", "Src_" + no);  //参见上方本机数据源注释
					dr.setValue("parentno", no);
				}

				dt.Rows.Add(dr.get("no"), dr.get("parentno"), dr.get("name"),
						dr.get("idx"), dr.get("isparent"), dr.get("ttype"));
			}
		}

	
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}
	
	/**
	 * 获取表单库数据
	 * 
	 * @return 返回结果Json,表单库
	 */
	private String GetFormTree() {
		String sqls = "SELECT No ,ParentNo,Name, Idx, 1 IsParent, 'FORMTYPE' TType FROM Sys_FormTree"
				+ " union "
				+ " SELECT No, FK_FrmSort as ParentNo,Name,Idx,0 IsParent, 'FORM' TType FROM Sys_MapData   where AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree);"
				+ "\r\n"
				+ " SELECT ss.No,'SrcRoot' AS ParentNo,ss.Name,0 AS Idx, 1 IsParent, 'SRC' TType FROM Sys_SFDBSrc ss ORDER BY ss.DBSrcType ASC;"
				+ "\r\n"
				+ " SELECT st.No, st.FK_SFDBSrc AS ParentNo,st.Name,0 AS Idx, 0 IsParent, 'SRCTABLE' TType FROM Sys_SFTable st;";

		DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);
		DataTable dt = ds.Tables.get(0).clone();

		// 1.表单类别；2.表单
		for (DataRow row : ds.Tables.get(0).Rows) {
			
			DataRow dr = dt.NewRow();
			for (DataColumn dc : row.columns) {
				dr.put(dc.ColumnName, row.getValue(dc.ColumnName));
			}
			dt.Rows.add(dr);
		}

		// 3.数据源字典表
		dt.Rows.Add("SrcRoot", "-1", "数据源字典表", 0, 1, "SRCROOT");

		// 4.数据源
		for (DataRow row : ds.Tables.get(1).Rows) {
			
			DataRow dr = dt.NewRow();
			for (DataColumn dc : row.columns) {
				dr.put(dc.ColumnName, row.getValue(dc.ColumnName));
			}
			dt.Rows.add(dr);
		}

		// 5.数据接口表
		for (DataRow row : ds.Tables.get(2).Rows) {
			dt.Rows.Add(
					row.get("no"),
					"".equals(row.get("parentno"))|| StringHelper.isNullOrEmpty(String.valueOf(row.get("parentno")))? "local" : row.get("ParentNo"),
							row.get("name"), row.get("idx"), row.get("isparent"), row.get("ttype"));
		}

		// 6.表单相关；7.枚举列表；8.JS验证库；9.Internet云数据；10.私有表单库；11.共享表单库
		dt.Rows.Add("FormRef", "-1", "表单相关", 0, 1, "FORMREF");
		dt.Rows.Add("Enums", "FormRef", "枚举列表", 0, 0, "ENUMS");
		dt.Rows.Add("JSLib", "FormRef", "JS验证库", 0, 0, "JSLIB");
		dt.Rows.Add("FUNCM", "FormRef", "功能执行", 0, 0, "FUNCM");

		dt.Rows.Add("CloundData", "-1", "ccbpm云服务-表单云", 0, 1, "CLOUNDDATA");
		dt.Rows.Add("PriForm", "CloundData", "私有表单云", 0, 0, "PRIFORM");
		dt.Rows.Add("ShareForm", "CloundData", "共有表单云", 0, 0, "SHAREFORM");
		sbJson.delete(0, sbJson.length());

		String sTmp = "";

		if (dt.Rows.size() > 0) {
			GetTreeJsonByTable(dt, "", "ParentNo", "No", "Name", "IsParent",
					"", new String[] { "TType" });
		}
		sTmp += sbJson.toString();

		return sTmp;
	}

	private String GetSrcTreeTable() {
		String sqls = "SELECT ss.No,'SrcRoot' ParentNo,ss.Name,0 Idx, 1 IsParent, 'SRC' TType FROM Sys_SFDBSrc ss ORDER BY ss.DBSrcType ASC;"
				+ "\r\n"
				+ " SELECT st.No, st.FK_SFDBSrc AS ParentNo,st.Name,0 AS Idx, 0 IsParent, 'SRCTABLE' TType FROM Sys_SFTable st;;";

		DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);
		DataTable dt = ds.Tables.get(1).clone();
		//去掉默认数据源 如需解除注释  将上面的DataTable dt = ds.Tables.get(1).clone(); 改为DataTable dt = ds.Tables.get(0).clone();
		/*for (DataRow row : ds.Tables.get(0).Rows) {
			
			DataRow dr = dt.NewRow();
			for (DataColumn dc : row.columns) {
				dr.put(dc.ColumnName, row.getValue(dc.ColumnName));
			}
			dt.Rows.add(dr);
		}
		*/
		for (DataRow row : ds.Tables.get(1).Rows) {
			
			DataRow dr = dt.NewRow();
			for (DataColumn dc : row.columns) {
				dr.put(dc.ColumnName, row.getValue(dc.ColumnName));
			}
			dr.put("parentno", "SrcRoot");
			dt.Rows.add(dr);
		}
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}

	private String GetStructureTreeTable() {
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("ParentNo", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("TType", String.class);

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne) {
			BP.WF.Port.Depts depts = new BP.WF.Port.Depts();
			depts.RetrieveAll();
			BP.WF.Port.Stations sts = new BP.WF.Port.Stations();
			sts.RetrieveAll();
			BP.WF.Port.Emps emps = new BP.WF.Port.Emps();
			emps.RetrieveAll(BP.WF.Port.EmpAttr.Name);
			BP.WF.Port.EmpStations empsts = new BP.WF.Port.EmpStations();
			empsts.RetrieveAll();
			BP.GPM.DeptEmps empdetps = new BP.GPM.DeptEmps();
			empdetps.RetrieveAll();

			// 部门人员
			HashMap<String, ArrayList<String>> des = new HashMap<String, ArrayList<String>>();
			// 岗位人员
			HashMap<String, ArrayList<String>> ses = new HashMap<String, ArrayList<String>>();
			// 部门岗位
			HashMap<String, ArrayList<String>> dss = new HashMap<String, ArrayList<String>>();
			BP.WF.Port.Station stt = null;
			BP.WF.Port.Emp empt = null;

			for (int i = 0; i < depts.size(); i++) {
				Dept dept = (Dept) depts.get(i);
				// 增加部门
				dt.Rows.Add(dept.getNo(), dept.getParentNo(), dept.getName(),"DEPT");
				des.put(dept.getNo(), new ArrayList<String>());
				dss.put(dept.getNo(), new ArrayList<String>());

				// 获取部门下的岗位
				empdetps.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, dept.getNo());
				for (int j = 0; j < empdetps.size(); j++) {
					BP.GPM.DeptEmp empdept = (DeptEmp) empdetps.get(j);

					des.get(dept.getNo()).add(empdept.getFK_Emp());
					// 判断该人员拥有的岗位
					empsts.Retrieve(BP.WF.Port.EmpStationAttr.FK_Emp,empdept.getFK_Emp());
					for (int z = 0; z < empsts.size(); z++) {
						BP.WF.Port.EmpStation es = (EmpStation) empsts.get(z);
						if (ses.containsKey(es.getFK_Station())) {
							if (ses.get(es.getFK_Station()).contains(
									es.getFK_Emp()) == false) {
								ses.get(es.getFK_Station()).add(es.getFK_Emp());
							}
						} else {
							ses.put(es.getFK_Station(),
									new ArrayList<String>(Arrays.asList(new String[] { es
													.getFK_Emp() })));
						}

						// 增加部门的岗位
						if (dss.get(dept.getNo()).contains(es.getFK_Station()) == false) {
							Object tempVar = sts.GetEntityByKey(es
									.getFK_Station());
							stt = (BP.WF.Port.Station) ((tempVar instanceof BP.WF.Port.Station) ? tempVar
									: null);

							if (stt == null) {
								continue;
							}

							dss.get(dept.getNo()).add(es.getFK_Station());
							dt.Rows.Add(
									dept.getNo() + "|" + es.getFK_Station(),
									dept.getNo(), stt.getName(), "STATION");
						}
					}
				}
				//此for循环是在部门下直接加载人员信息，和ccflow不一致，如果要保持和ccflow一致请将下方for(...){...}注释掉并放开上方/*.....*/的注释，并修改functree.js，详见注释部分
				/*for(int n=0;n<emps.size();n++){
					Emp emp=(Emp) emps.get(n);
					if(!dept.getNo().equals(emp.getFK_Dept())){
						continue;
					}
					dt.Rows.Add(dept.getNo() + "|" + emp.getFK_Dept(),
							dept.getNo(), emp.getName(), "EMP");
				}*/
			}

			for (java.util.Map.Entry<String, java.util.ArrayList<String>> ds : dss.entrySet()) 
			{
				for (String st : ds.getValue()) {
					for (String emp : ses.get(st)) {
						Object tempVar2 = emps.GetEntityByKey(emp);
						empt = (BP.WF.Port.Emp) ((tempVar2 instanceof BP.WF.Port.Emp) ? tempVar2 : null);

						if (empt == null) {
							continue;
						}

						dt.Rows.Add(ds.getKey() + "|" + st + "|" + emp,
								ds.getKey() + "|" + st, empt.getName(), "EMP");
					}
				}
			}
		} else {
			BP.GPM.Depts depts = new BP.GPM.Depts();
			depts.RetrieveAll();
			BP.GPM.Stations sts = new BP.GPM.Stations();
			sts.RetrieveAll();
			BP.GPM.Emps emps = new BP.GPM.Emps();
			emps.RetrieveAll(BP.WF.Port.EmpAttr.Name);
			BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
			dss.RetrieveAll();
			BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
			dess.RetrieveAll();
			BP.GPM.Station stt = null;
			BP.GPM.Emp empt = null;

			for (int n = 0; n < depts.size(); n++) {
				BP.GPM.Dept dept = (BP.GPM.Dept) depts.get(n);
				// 增加部门
				dt.Rows.Add(dept.getNo(), dept.getParentNo(), dept.getName(),"DEPT");

				// 增加部门岗位
				dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, dept.getNo());
				for (int m = 0; m < dss.size(); m++) {
					BP.GPM.DeptStation ds = (DeptStation) dss.get(m);
					Object tempVar3 = sts.GetEntityByKey(ds.getFK_Station());
					stt = (BP.GPM.Station) ((tempVar3 instanceof BP.GPM.Station) ? tempVar3
							: null);

					if (stt == null) {
						continue;
					}

					dt.Rows.Add(dept.getNo() + "|" + ds.getFK_Station(),
							dept.getNo(), stt.getName(), "STATION");

					// 增加部门岗位人员
					dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept,
							dept.getNo(), BP.GPM.DeptEmpStationAttr.FK_Station,
							ds.getFK_Station());

					for (int a = 0; a < dess.size(); a++) {
						BP.GPM.DeptEmpStation des = (DeptEmpStation) dess
								.get(a);
						Object tempVar4 = emps.GetEntityByKey(des.getFK_Emp());
						empt = (BP.GPM.Emp) ((tempVar4 instanceof BP.GPM.Emp) ? tempVar4
								: null);

						if (empt == null) {
							continue;
						}

						dt.Rows.Add(dept.getNo() + "|" + ds.getFK_Station()
								+ "|" + des.getFK_Emp(), dept.getNo() + "|"
								+ ds.getFK_Station(), empt.getName(), "EMP");
					}
				}
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(dt.Rows);
		return jsonArray.toString();
	}

	public final String GetStructureDatas(String deptNo, String stationNo,
			String empNo) {
		return null;
	}

	/**
	 * 根据DataTable生成Json树结构
	 */
	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol, String IsParent, String sChecked, String[] attrFields) {
		String treeJson = "";

		if (tabel.Rows.size() > 0) {
			sbJson.append("[");
			String filer = "";
			Map<String, Object> filterMap = new HashMap<String, Object>();
			if (pId.toString().equals("")) {
				filer = String.format("{0} is null or {0}='-1' or {0}='0' or {0}='F0'", rela);
				filterMap.put(rela, "is null");
				filterMap.put(rela, "-1");
				filterMap.put(rela, "0");
				filterMap.put(rela, "F0");
			}
			else {
				filer = String.format("{0}='{1}'", rela, pId);
				filterMap.put(rela, pId);
			}
			List<DataRow> rows=null;
			try {
//				rows = tabel.Select(filterMap, true);
				rows = tabel.select(filer);
			} catch (Exception e) {
				e.printStackTrace();
			}//tabel.Select(filer, idCol);
			if (rows.size() > 0) {
				for (int i = 0; i < rows.size(); i++) {
					DataRow row = rows.get(i);


					String jNo = (String)((row.get(idCol) instanceof String) ? row.get(idCol): null);
					String jText = (String)((row.get(txtCol) instanceof String) ? row.get(txtCol) : null);
					if (jText.length() > 25) {
						jText = jText.substring(0, 25) + "<img src='../Scripts/easyUI/themes/icons/add2.png' onclick='moreText(" + jNo + ")'/>";
					}

					String jIsParent = row.get(IsParent).toString();
					String jState = (new String("1")).equals(jIsParent) ? "open" : "closed";
					jState = (new String("open")).equals(jState) && i == 0 ? "open" : "closed";
					
					Map<String, Object> filterMap1 = new HashMap<String, Object>();
					filterMap1.put(rela, jNo);
					//DataRow[] rowChild = tabel.Select(String.format("{0}='{1}'", rela, jNo));
					
					
					List<DataRow> rowChild=null;
					try {
						rowChild = tabel.Select(filterMap1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					String tmp = "{\"id\":\"" + jNo + "\",\"text\":\"" + jText;

					//增加自定义attributes列，added by liuxc,2015-10-6
					String attrs = "";
					if (attrFields != null && attrFields.length > 0) {
						for (String field : attrFields) {
							if (!tabel.Columns.contains(field)) {
								continue;
							}
							if (StringHelper.isNullOrEmpty(row.get(field).toString())) {
								attrs += ",\"" + field + "\":\"\"";
								continue;
							}
							attrs += ",\"" + field + "\":" + (tabel.Columns.get(field).DataType == String.class ? String.format("\"{0}\"", row.get(field)) : row.get(field));
						}
					}

					if ((new String("0")).equals(pId.toString()) || row.get(rela).toString().equals("F0")) {
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\",\"IsRoot\":\"1\"" + attrs + "}";
					}
					else {
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\"" + attrs + "}";
					}

					if (rowChild.size() > 0) {
						tmp += ",\"checked\":" + sChecked.contains("," + jNo + ",") + ",\"state\":\"" + jState + "\"";
					}
					else {
						tmp += ",\"checked\":" + sChecked.contains("," + jNo + ",");
						//tmp += ",\"checked\":" + sChecked.contains("," + jNo + ",").toString().toLowerCase();
					}

					sbJson.append(tmp);
					if (rowChild.size() > 0) {
						sbJson.append(",\"children\":");
						GetTreeJsonByTable(tabel, jNo, rela, idCol, txtCol, IsParent, sChecked, attrFields);
					}

					sbJson.append("},");
				}
				sbJson = sbJson.deleteCharAt(sbJson.length() - 1);
			}
			sbJson.append("]");
			treeJson = sbJson.toString();
		}
		return treeJson;
	}

	/**
	 * 删除流程
	 * 
	 * @return
	 */
	private String DelFlow() {
		String msg = WorkflowDefintionManager.DeleteFlowTemplete(this
				.getFK_Flow());
		if (msg == null) {
			return "true";
		}
		return "false";
	}
	/**
	 * 删除流程表单
	 * 
	 * @return
	 */
	private String DelFrm(){
		String msg = WorkflowDefintionManager.DeleteFrmTemplate(this.getNo());
		if(msg==null){
			return "true";
		}
		return "false";
	}

	/**
	 * 树节点管理
	 */
 	public final String Do() {
		AjaxJson j = new AjaxJson();
		String doWhat = getUTF8ToString("doWhat");
		String para1 = getUTF8ToString("para1");
		// 如果admin账户登陆时有错误发生，则返回错误信息
		String result = LetAdminLogin("CH", true);

		if (StringHelper.isNullOrEmpty(result) == false) {
			return result;
		}

		if (doWhat.equals("GetFlowSorts")) { // 获取所有流程类型
			try {
				FlowSorts flowSorts = new FlowSorts();
				flowSorts.RetrieveAll(FlowSortAttr.Idx);
				return BP.Tools.Entitis2Json.ConvertEntitis2GenerTree(flowSorts, "0");
			} catch (RuntimeException ex) {
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("NewSameLevelFrmSort")) { // 创建同级别的 表单树 目录.
			SysFormTree frmSort = null;
			try {
				String[] para = para1.split("[,]", -1);
				frmSort = new SysFormTree(para[0]);
				String sameNodeNo = frmSort.DoCreateSameLevelNode().getNo();
				frmSort = new SysFormTree(sameNodeNo);
				frmSort.setName(para[1]);
				frmSort.Update();
				j.setSuccess(true);
				j.setMsg("");
				JSONObject member1 = new JSONObject();
				member1.put("F", frmSort.getNo());
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
				.DefaultLogWriteLineError("Do Method NewFormSort Branch has a error , para:\t"
						+ para1 + ex.getMessage());
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("NewSubLevelFrmSort")) { // 创建子级别的 表单树 目录.
			SysFormTree frmSortSub = null;
			try {
				String[] para = para1.split("[,]", -1);
				frmSortSub = new SysFormTree(para[0]);
				String sameNodeNo = frmSortSub.DoCreateSubNode().getNo();
				frmSortSub = new SysFormTree(sameNodeNo);
				frmSortSub.setName(para[1]);
				frmSortSub.Update();
				j.setSuccess(true);
				j.setMsg("");
				JSONObject member1 = new JSONObject();
				member1.put("F", frmSortSub.getNo());
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
				.DefaultLogWriteLineError("Do Method NewSubLevelFrmSort Branch has a error , para:\t"
						+ para1 + ex.getMessage());
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("NewSameLevelFlowSort")) { // 创建同级别的 流程树 目录.
			FlowSort fs = null;
			try {
				String[] para = para1.split("[,]", -1);
				fs = new FlowSort(para[0].replace("F", "")); // 传入的编号多出F符号，需要替换掉
				String sameNodeNo = fs.DoCreateSameLevelNode().getNo();
				fs = new FlowSort(sameNodeNo);
				fs.setName(para[1]);
				fs.Update();

				j.setSuccess(true);
				j.setMsg("");
				JSONObject member1 = new JSONObject();
				member1.put("F", fs.getNo());
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewSameLevelFlowSort Branch has a error , para:\t"
								+ para1 + ex.getMessage());
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("NewSubFlowSort")) { // 创建子级别的 流程树 目录.
			try {
				String[] para = para1.split("[,]", -1);
				FlowSort fsSub = new FlowSort(para[0].replace("F", "")); // 传入的编号多出F符号，需要替换掉
				String subNodeNo = fsSub.DoCreateSubNode().getNo();
				FlowSort subFlowSort = new FlowSort(subNodeNo);
				subFlowSort.setName(para[1]);
				subFlowSort.Update();

				j.setSuccess(true);
				j.setMsg("");
				JSONObject member1 = new JSONObject();
				member1.put("F", subFlowSort.getNo());
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewSubFlowSort Branch has a error , para:\t"
								+ ex.getMessage());

				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("EditFlowSort")) { // 编辑表单树.
			FlowSort fs = null;
			try {
				String[] para = para1.split("[,]", -1);
				fs = new FlowSort(para[0].replace("F", "")); // 传入的编号多出F符号，需要替换掉
				fs.setName(para[1]);
				fs.Save();

				j.setSuccess(true);
				j.setMsg("");
				JSONObject member1 = new JSONObject();
				member1.put("F", fs.getNo());
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method EditFlowSort Branch has a error , para:\t"
								+ para1 + ex.getMessage());
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		}  else if(doWhat.equals("EditFrmSort")){//编辑流程树类别
			SysFormTree frmSort = null;
		try{
			String[] para = para1.split("[,]", -1);
			frmSort = new SysFormTree(para[0].replace("F", "")); // 传入的编号多出F符号，需要替换掉
			frmSort.setName(para[1]);
			frmSort.Save();

			j.setSuccess(true);
			j.setMsg("");
			JSONObject member1 = new JSONObject();
			member1.put("F", frmSort.getNo());
			j.setData(member1);
			return JsonPluginsUtil.beanToJson(j);
		}catch(Exception e){
				BP.DA.Log
				.DefaultLogWriteLineError("Do Method EditFrmSort Branch has a error , para:\t"
						+ para1 + e.getMessage());
			j.setSuccess(false);
			j.setMsg(e.getMessage());
			return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("NewFlow")) { // 创建新流程.
			try {
				String[] ps = para1.split("[,]", -1);
				if (ps.length != 6) {
					throw new RuntimeException("@创建流程参数错误");
				}

				String fk_floSort = ps[0]; // 类别编号.
				fk_floSort = fk_floSort.replace("F", ""); // 传入的编号多出F符号，需要替换掉

				String flowName = ps[1]; // 流程名称.
				DataStoreModel dataSaveModel = DataStoreModel.forValue(Integer
						.parseInt(ps[2])); // 数据保存方式。
				String pTable = ps[3]; // 物理表名。
				String flowMark = ps[4]; // 流程标记.
				String flowVer = ps[5]; // 流程版本

				String FK_Flow=null;
				try {
					FK_Flow = BP.BPMN.Glo.NewFlow(fk_floSort, flowName,
							dataSaveModel, pTable, flowMark, flowVer);
				} catch (Exception e) {
					e.printStackTrace();
				}

				j.setSuccess(true);
				j.setMsg("");
				JSONObject member1 = new JSONObject();
				member1.put("no", FK_Flow);
				member1.put("name", flowName);
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewFlow Branch has a error , para:\t"
								+ para1 + ex.getMessage());
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("DelFlow")) { // 删除流程.
			try {

				j.setSuccess(true);
				j.setMsg(WorkflowDefintionManager.DeleteFlowTemplete(para1));
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if(doWhat.equals("DelFrm")){//删除表单
			try {
				j.setSuccess(true);
				j.setMsg(WorkflowDefintionManager.DeleteFrmTemplate(para1));
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
			
		}else if(doWhat.equals("DelFrmSort")){
			
			try{
				String Frm = para1.replace("F", "");
				String forceDel = getUTF8ToString("force");
				String[] para = para1.split("[,]", -1);
				SysFormTree frmSort = new SysFormTree(); // 传入的编号多出F符号，需要替换掉
				frmSort.setNo(Frm);
				// 强制删除，不需判断是否含有子项。
				if (forceDel.equals("true")) {
					//frmSort.DeleteChild();
					frmSort.Delete();			
					j.setSuccess(true);
					JSONObject member1 = new JSONObject();
					member1.put("reason", "");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);		
				}// 判断是否包含子类别
				System.out.println(frmSort.getHisSubFormSorts());
				if (frmSort.getHisSubFormSorts() != null
						&& frmSort.getHisSubFormSorts().size() > 0) {
					j.setSuccess(false);
					j.setMsg("此类别下包含子类别。");
					JSONObject member1 = new JSONObject();
					member1.put("reason", "havesubsorts");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);
				}// 判断是否包含工作流程
				if (frmSort.getHisForms() != null
						&& frmSort.getHisForms().size() > 0) {

					j.setSuccess(false);
					j.setMsg("此类别下包含表单。");
					JSONObject member1 = new JSONObject();
					member1.put("reason", "haveflows");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);
				}
				
				// 执行删除
					frmSort.Delete();
					j.setSuccess(true);
					JSONObject member1 = new JSONObject();
					member1.put("reason", "");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);
				
			}catch(Exception e){
				BP.DA.Log
				.DefaultLogWriteLineError("Do Method DelFrmSort Branch has a error , para:\t"
						+ para1 + e.getMessage());
				j.setSuccess(false);
				j.setMsg(e.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("DelFlowSort")) {
			try {
				String FK_FlowSort = para1.replace("F", "");
				String forceDel = getUTF8ToString("force");
				FlowSort delfs = new FlowSort();
				delfs.setNo(FK_FlowSort);
				// 强制删除，不需判断是否含有子项。
				if (forceDel.equals("true")) {
					delfs.DeleteFlowSortSubNode_Force();
					delfs.Delete();

					j.setSuccess(true);
					JSONObject member1 = new JSONObject();
					member1.put("reason", "");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);
				}
				System.out.println(delfs.getHisSubFlowSorts());
				// 判断是否包含子类别
				if (delfs.getHisSubFlowSorts() != null
						&& delfs.getHisSubFlowSorts().size() > 0) {
					j.setSuccess(false);
					j.setMsg("此类别下包含子类别。");
					JSONObject member1 = new JSONObject();
					member1.put("reason", "havesubsorts");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);
				}

				// 判断是否包含工作流程
				if (delfs.getHisFlows() != null
						&& delfs.getHisFlows().size() > 0) {

					j.setSuccess(false);
					j.setMsg("此类别下包含流程。");
					JSONObject member1 = new JSONObject();
					member1.put("reason", "haveflows");
					j.setData(member1);
					return JsonPluginsUtil.beanToJson(j);
				}

				// 执行删除
				delfs.Delete();
				j.setSuccess(true);
				JSONObject member1 = new JSONObject();
				member1.put("reason", "");
				j.setData(member1);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method DelFlowSort Branch has a error , para:\t"
								+ para1 + ex.getMessage());

				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("DelNode")) {
			try {
				if (!StringHelper.isNullOrEmpty(para1)) {
					BP.WF.Node delNode = new BP.WF.Node(Integer.parseInt(para1));
					delNode.Delete();
				} else {
					throw new RuntimeException("@参数错误:" + para1);
				}
			} catch (RuntimeException ex) {
				return "err:" + ex.getMessage();
			}
			return null;
		} else if (doWhat.equals("SetBUnit")) {
			try {
				if (!StringHelper.isNullOrEmpty(para1)) {
					BP.WF.Node nd = new BP.WF.Node(Integer.parseInt(para1));
					nd.setIsTask(!nd.getIsBUnit());
					nd.Update();
				} else {
					throw new RuntimeException("@参数错误:" + para1);
				}
			} catch (RuntimeException ex) {
				return "err:" + ex.getMessage();
			}
			return null;
		} else if (doWhat.equals("GetSettings")) {

			return (String) SystemConfig.getAppSettings().get(para1);
		} else if (doWhat.equals("SaveFlowFrm")) { // 保存流程表单.
			Entity en = null;
			try {
				AtPara ap = new AtPara(para1);
				String enName = ap.GetValStrByKey("EnName");
				String pk = ap.GetValStrByKey("PKVal");
				en = ClassFactory.GetEn(enName);
				en.ResetDefaultVal();
				if (en == null) {
					throw new RuntimeException("无效的类名:" + enName);
				}

				if (StringHelper.isNullOrEmpty(pk) == false) {
					en.setPKVal(pk);
					en.RetrieveFromDBSources();
				}

				for (String key : ap.getHisHT().keySet()) {
					if (key.equals("PKVal")) {
						continue;
					}
					en.SetValByKey(key, ap.getHisHT().get(key).toString()
							.replace('^', '@'));
				}
				en.Save();
				return (String) ((en.getPKVal() instanceof String) ? en
						.getPKVal() : null);
			} catch (RuntimeException ex) {
				if (en != null) {
					en.CheckPhysicsTable();
				}
				return "Error:" + ex.getMessage();
			}
		} else if (doWhat.equals("ChangeNodeType")) {
			String[] p = para1.split("[,]", -1);

			try {
				if (p.length != 3) {
					throw new RuntimeException("@修改节点类型参数错误");
				}

				String sql = "UPDATE WF_Node SET RunModel={0} WHERE FK_Flow='{1}' AND NodeID={2}";
				DBAccess.RunSQL(String.format(sql, p[0], p[1], p[2]));

				j.setSuccess(true);
				JSONArray jsonArray = JSONArray.fromObject(j);
				return jsonArray.toString();
			} catch (RuntimeException ex) {

				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else if (doWhat.equals("ChangeNodeIcon")) {
			String[] p = para1.split("[,]", -1);

			try {
				if (p.length != 3) {
					throw new RuntimeException("@修改节点图标参数错误");
				}

				String sql = "UPDATE WF_Node SET Icon='{0}' WHERE FK_Flow='{1}' AND NodeID={2}";
				DBAccess.RunSQL(String.format(sql, p[0], p[1], p[2]));

				j.setSuccess(true);
				return JsonPluginsUtil.beanToJson(j);
			} catch (RuntimeException ex) {
				j.setSuccess(false);
				j.setMsg(ex.getMessage());
				return JsonPluginsUtil.beanToJson(j);
			}
		} else {
			throw new RuntimeException("@没有约定的执行标记:" + doWhat);
		}
	}

	/**
	 * 让admin 登陆
	 * 
	 * @param lang
	 *            当前的语言
	 * @return 成功则为空，有异常时返回异常信息
	 */
	public final String LetAdminLogin(String lang, boolean islogin) {
		try {
			if (islogin) {
				BP.Port.Emp emp = new BP.Port.Emp("admin");
				WebUser.SignInOfGener(emp, lang, "admin", true, false);
			}
		} catch (RuntimeException exception) {
			return exception.getMessage();
		}
		return "";
	}

	public final boolean getIsReusable() {
		return false;
	}

}
