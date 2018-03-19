package cn.jflow.controller.wf.workopt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Port.Dept;
import BP.Port.Depts;
import BP.Port.Emp;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.Tools.DataTableConvertJson;
import BP.Tools.PinYinF4jUtils;
import BP.Tools.StringHelper;
import BP.WF.AskforHelpSta;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.SelectDept;
import cn.jflow.common.model.SelectUser;
import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/WF/WorkOpt")
public class AskForController {

	@RequestMapping(value = "/AskforS", method = RequestMethod.POST)
	public ModelAndView askfor(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView mv = new ModelAndView();
		try {
			AskforHelpSta sta = AskforHelpSta.AfterDealSend;
			if ("RB_0".equals(object.getToEmp())) {
				sta = AskforHelpSta.AfterDealSend;
			} else if ("RB_1".equals(object.getToEmp())) {
				sta = AskforHelpSta.AfterDealSendByWorker;
			}
			String str = new String(request.getParameter("Info").getBytes("iso-8859-1"),"UTF-8") ;
			String info = Dev2Interface.Node_Askfor(object.getWorkID(), sta,
					object.getAskFor(), str);

			request.getSession().setAttribute("info", info);

			mv.addObject("WorkID", object.getWorkID());
			mv.addObject("FK_Type", "Info");
			mv.addObject("FK_Node", object.getFK_Node());
			mv.addObject("FK_Flow", object.getFK_Flow());
			mv.setViewName("redirect:" + "/WF/MyFlowInfo"
					+ Glo.getFromPageType() + ".jsp");
		} catch (Exception e) {
			mv.addObject("WorkID", object.getWorkID());
			mv.addObject("FID", object.getFID());
			mv.addObject("FK_Node", object.getFK_Node());
			mv.addObject("FK_Flow", object.getFK_Flow());
			mv.addObject("errMsg", "工作加签出错：" + e.getMessage());
			mv.setViewName("redirect:" + "/WF/WorkOpt/Askfor.jsp");
		}
		return mv;
	}
	
	@RequestMapping(value = "/AskforRe", method = RequestMethod.POST)
	public ModelAndView askforRe(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView mv = new ModelAndView();
		try {
			String info = Dev2Interface.Node_AskforReply(object.getFK_Flow(),
					object.getFK_Node(), object.getWorkID(), object.getFID(),
					object.getInfo());
			
			request.getSession().setAttribute("info", info);
			
			mv.addObject("WorkID", object.getWorkID());
			mv.addObject("FK_Type", "Info");
			mv.addObject("FK_Node", object.getFK_Node());
			mv.addObject("FK_Flow", object.getFK_Flow());
			mv.setViewName("redirect:" + "/WF/MyFlowInfo"
					+ Glo.getFromPageType() + ".jsp");
		}catch (Exception e) {
			mv.addObject("WorkID", object.getWorkID());
			mv.addObject("FID", object.getFID());
			mv.addObject("FK_Node", object.getFK_Node());
			mv.addObject("FK_Flow", object.getFK_Flow());
			mv.addObject("errMsg", "回复加签出错：" + e.getMessage());
			mv.setViewName("redirect:" + "/WF/WorkOpt/AskForRe.jsp");
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/CheckAskfor", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson CheckAskfor(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		try {
			Emp emp = new Emp();
			emp.setNo(object.getAskFor());
			int i = emp.RetrieveFromDBSources();
			if (i != 1)
				j.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(value = "/LoadSelectedEmployees", method = RequestMethod.POST)
	@ResponseBody
	public String loadSelectedEmployees(SelectUser user, HttpServletRequest request,
			HttpServletResponse response) {
		
		 DataTable dt = DBAccess.RunSQLReturnTable("select * from Port_Emp");
		 if(null != dt){
			 List<DataRow> lists = new ArrayList<DataRow>();
			 for(DataRow dr : dt.Rows){
				 if(!String.format(",%s,", user.getSelUsers()).contains(String.format(",%s,", dr.getValue("No")))){
					 lists.add(dr);
				 }
			 }
			 dt.Rows.removeAll(lists);
		 }
		 return DataTableConvertJson.DataTable2Json(dt);
	}
	
	@RequestMapping(value = "/LoadEmps", method = RequestMethod.POST)
	@ResponseBody
	public String loadEmps(SelectUser user, HttpServletRequest request,
			HttpServletResponse response){
		 DataTable dt = DBAccess.RunSQLReturnTable(user.getKeyWord());
		 return DataTableConvertJson.DataTable2Json(dt);
	}

	@RequestMapping(value = "/GetDepts", method = RequestMethod.POST)
	@ResponseBody
	public String getDepts(HttpServletRequest request,
			HttpServletResponse response) {

		String str = "";
		try {
			DataTable dt_dept = DBAccess
					.RunSQLReturnTable("select NO,NAME,ParentNo from port_dept order by Idx");

			str = DataTableConvertJson.TransDataTable2TreeJson(dt_dept, "NO",
					"NAME", "ParentNo", "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "/GetUser", method = RequestMethod.POST)
	@ResponseBody
	public String getUser(SelectUser user, HttpServletRequest request,
			HttpServletResponse response) {

		String name = user.getKeyWord();
		String deptId = user.getDeptId();
		// String currUserNo = WebUser.getNo();
		if (user.isSearchChild()) {
			deptId = GetDeptAndChild(deptId);
		} else {
			deptId = "'" + deptId + "'";
		}

		String sql = "";
		if (Glo.getOSModel() == OSModel.OneMore) {
			String filter_dept = "0".equals(deptId) ? ""
					: String.format(
							" and Port_Emp.No in (Select FK_Emp from Port_DeptEmp where FK_Dept in (%s))",
							deptId);
			String filter_name = StringHelper.isNullOrEmpty(name) ? "" : String
					.format(" and Port_Emp.Name+','+Port_Emp.NO like '%s'", "%"
							+ name + "%");
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				filter_name = StringHelper.isNullOrEmpty(name) ? ""
						: String.format(
								" and CONCAT(Port_Emp.Name,Port_Emp.NO) like '%s'",
								"%" + name + "%");
			}
			sql = String
					.format("select Port_Emp.*,Port_Dept.Name as DeptName from Port_Emp,Port_Dept where Port_Emp.FK_Dept = Port_Dept.No %1$s %2$s",
							filter_dept, filter_name);
		} else {
			String filter_dept = "0".equals(deptId) ? ""
					: String.format(
							" and Port_Emp.No in (Select No from Port_Emp where FK_Dept in (%s))",
							deptId);
			String filter_name = StringHelper.isNullOrEmpty(name) ? "" : String
					.format(" and Port_Emp.Name+','+Port_Emp.NO like '%s'", "%"
							+ name + "%");
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				filter_name = StringHelper.isNullOrEmpty(name) ? ""
						: String.format(
								" and CONCAT(Port_Emp.Name,Port_Emp.NO) like '%s'",
								"%" + name + "%");
			}
			sql = String
					.format("select Port_Emp.*,Port_Dept.Name as DeptName from Port_Emp,Port_Dept where Port_Emp.FK_Dept = Port_Dept.No %1$s %2$s",
							filter_dept, filter_name);
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return DataTableConvertJson.DataTable2Json(dt);
	}

	private String GetDeptAndChild(String deptId) {
		String strDepts = "'" + deptId + "'";
		strDepts = GetChildDept(deptId, strDepts);
		return strDepts;
	}

	private String GetChildDept(String parentNo, String strDepts) {
		Depts depts = new Depts(parentNo);
		if (null != depts && depts.size() > 0) {
			for (Dept item : depts.ToJavaList()) {
				strDepts += ",'" + item.getNo() + "'";
				GetChildDept(item.getNo(), strDepts);
			}
		}
		return strDepts;
	}
	
	
	@RequestMapping(value = "/SearchDepts", method = RequestMethod.POST)
	@ResponseBody
	public String getUser(SelectDept dept, HttpServletRequest request,
			HttpServletResponse response) {
		
		String reJson = "";
		String method = StringHelper.isEmpty(dept.getMethod(), "");
		if("search".equals(method)){
			reJson = search(dept);
		}else if("group".equals(method)){
			reJson = group(dept);
		}else if("all".equals(method)){
			reJson = all(dept);
		}else if("getdepts".equals(method)){
			reJson = getdepts(dept);
		}
		return reJson;
	}
	
	private String getdepts(SelectDept selectDept){
		
		String selectedDepts = StringHelper.isEmpty(selectDept.getKw(), "");
		if("".equals(selectedDepts)) return "[]";
		
		String depts[] = selectedDepts.split(",");
		String sql = "SELECT No,Name FROM Port_Dept WHERE";
		
		String str = "";
		for (String dept : depts){
			if(str.length()>0){
				str += String.format(" OR No = '%s' ", dept);
			}else{
				str = String.format(" No = '%s' ", dept);
			}
		}
		sql = sql + str;
		
		String reJson = "[";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		str = "";
        for(DataRow dr : dt.Rows){
        	if(str.length()>0){
        		str += ",{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"}";
        	}else{
        		str = "{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"}";
        	}
        }
        reJson = reJson + str + "]";
        
        return reJson;
	}
	
	private String search(SelectDept selectDept){
		
		String sql = "SELECT No,Name,ParentNo FROM Port_Dept ORDER BY No ASC";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		
		String kw = StringHelper.isEmpty(selectDept.getKw(), "").toLowerCase();
		boolean haveSub = selectDept.isHavesub();
		boolean haveSame = selectDept.isHavesame();
		
		String reJson = "[";
		List<DataRow> drResults = new ArrayList<DataRow>();
		
		String zjm = "";
		for(DataRow dr : dt.Rows){
			 String name = dr.getValue("Name").toString();
			 zjm = PinYinF4jUtils.spell(name).toLowerCase();
			 if (zjm.contains(kw) || name.contains(kw)){
				 if(!drResults.contains(dr)){
					 reJson += "{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"},";
                     drResults.add(dr);
                     
                     if (haveSub){
                    	 reJson += getSubDatarow(drResults, dr.getValue("No").toString(), dt);
                     }
                     
                     if (haveSame){
                    	 reJson += getSameDatarow(drResults, dr.getValue("ParentNo").toString(), dt);
                     }
				 }
				 
			 }
		}
		reJson = StringHelper.trimEnd(reJson, ',') + "]";
		return reJson;
	}
	
	private String group(SelectDept selectDept){
		
		String sql = "SELECT No,Name,ParentNo FROM Port_Dept ORDER BY No ASC";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		
		String kw = StringHelper.isEmpty(selectDept.getKw(), "");
		boolean haveSub = selectDept.isHavesub();
		boolean haveSame = selectDept.isHavesame();
		
		String reJson = "[";
		List<DataRow> drResults = new ArrayList<DataRow>();
		
		String zjm = "";
		for(DataRow dr : dt.Rows){
			 String name = dr.getValue("Name").toString();
			 zjm = PinYinF4jUtils.spell(name);
			 if(!StringHelper.isNullOrEmpty(zjm) && zjm.substring(0, 1).equals(kw)){
				 if(!drResults.contains(dr)){
					 reJson += "{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"},";
                     drResults.add(dr);
                     
                     if (haveSub){
                    	 reJson += getSubDatarow(drResults, dr.getValue("No").toString(), dt);
                     }
                     
                     if (haveSame){
                    	 reJson += getSameDatarow(drResults, dr.getValue("ParentNo").toString(), dt);
                     }
				 }
			 }
		}
		
		reJson = StringHelper.trimEnd(reJson, ',') + "]";
		return reJson;
	}
	
	private String all(SelectDept selectDept){
	
		  String sql = "SELECT No,Name FROM Port_Dept ORDER BY No ASC";
		  DataTable dt = DBAccess.RunSQLReturnTable(sql);
		  
		  String reJson = "[";
		  for(DataRow dr : dt.Rows){
			  reJson += "{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"},";
		  }
		  reJson = StringHelper.trimEnd(reJson, ',') + "]";
		  return reJson;
	}
	
	 /// <summary>
    /// 获取指定结点下的所有子结点
    /// </summary>
    /// <param name="drResults">用于识别结点已经加入JSON的集合</param>
    /// <param name="no">结点</param>
    /// <param name="dt">所有结点Table</param>
    /// <returns></returns>
    private String getSubDatarow(List<DataRow> drResults, String no, DataTable dt){
    	
    	Map<String,Object> filer = new HashMap<String, Object>();
        filer.put("ParentNo", no);
        
        String reJson = "";
        try{
	        List<DataRow> drs = dt.Select(filer);
	        for (DataRow dr : drs){
	        	 if(!drResults.contains(dr)){
	        		  reJson += "{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"},";
	                  drResults.add(dr);
	                  
	                  reJson += getSubDatarow(drResults,  dr.getValue("No").toString(), dt);
	        	 }
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }
    	
		return reJson;
    }
	
	   /// <summary>
    /// 获取指定父结点下的同级结点
    /// </summary>
    /// <param name="drResults">用于识别结点已经加入JSON的集合</param>
    /// <param name="parentNo">父结点</param>
    /// <param name="dt">所有结点Table</param>
    /// <returns></returns>
    private String getSameDatarow(List<DataRow> drResults, String parentNo, DataTable dt){
    	
    	Map<String,Object> filer = new HashMap<String, Object>();
        filer.put("ParentNo", parentNo);
    	
        String reJson = "";
        try{
	        List<DataRow> drs = dt.Select(filer);
	        for (DataRow dr : drs){
	        	 if(!drResults.contains(dr)){
	        		  reJson += "{\"No\":\"" + dr.getValue("No") + "\",\"Name\":\"" + dr.getValue("Name") + "\"},";
	                  drResults.add(dr);
	        	 }
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }
    	
		return reJson;
    }
}