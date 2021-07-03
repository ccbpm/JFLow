package Controller;

import WebServiceImp.LocalWS;
import bp.da.*;
import bp.gpm.DeptEmpAttr;
import bp.gpm.DeptEmps;
import bp.port.Emp;
import bp.wf.port.WFEmp;
import net.sf.json.JSONObject;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.web.bind.annotation.*;

import bp.tools.Json;
import bp.web.WebUser;
import bp.wf.AppClass;
import bp.wf.Dev2Interface;
import bp.wf.Flow;
import bp.wf.GenerWorkFlow;
import bp.wf.Node;
import bp.wf.Nodes;
import bp.wf.SendReturnObjs;
import bp.wf.data.GERpt;
import bp.wf.template.Directions;
import bp.wf.template.FlowExt;
import bp.wf.template.Selector;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/restful")
public class RestFulController {

	/**
	 * 系统登录

	 * @return token
	 * @throws Exception
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping("/SSOLogin")
	public  String  SSOLogin(String userNo,String SecretLv)throws Exception{
		Hashtable rs = new Hashtable();
		//帐号

		//userNo = new String(Base64.decode(userNo), "UTF-8");
		if(DataType.IsNullOrEmpty(userNo)==true){
			rs.put("code", 500);
			rs.put("token",0);
			rs.put("msg","用户编码不能为空");
			return bp.tools.Json.ToJson(rs);
		}


		try{
			//验证用户信息
			Emp emp=new Emp();
			emp.setNo(userNo);
			if(emp.RetrieveFromDBSources()==0)
			{
				rs.put("code", 500);
				rs.put("token",0);
				rs.put("msg","账号"+userNo+"获取不到用户信息");
				return  bp.tools.Json.ToJson(rs);
			}

			//写入用户信息
			Dev2Interface.Port_Login(userNo);
			//生成SID
			String SID= DBAccess.GenerGUID();
			WFEmp wfEmp=new WFEmp();
			if(!wfEmp.IsExit("No", userNo))
			{
				wfEmp.setNo(emp.getNo());
				wfEmp.setName(emp.getName());
				wfEmp.setFK_Dept(emp.getFK_Dept());
				wfEmp.SetPara("SID",SID);
				wfEmp.DirectInsert();
			}
			else
			{
				wfEmp=new WFEmp(userNo);
				wfEmp.SetPara("SID",SID);
				wfEmp.DirectUpdate();
			}
			rs.put("code", 200);
			rs.put("token",SID);
			rs.put("msg","成功");
		}
		catch(Exception ex){
			rs.put("code", 500);
			rs.put("token",0);
			rs.put("msg",ex.getMessage());
		}
		return  bp.tools.Json.ToJson(rs);
	}
    /**
	 * 待办
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
     * @throws Exception 
	 */
	@CrossOrigin(origins = "*")
    @RequestMapping(value = "/DB_Todolist")
	public String DB_Todolist(String userNo, String sysNo) throws Exception {
		try {
			Dev2Interface.Port_Login(userNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 String sql = "";
         if (DataType.IsNullOrEmpty(sysNo) == true)
             sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='" + userNo + "'";
         else
             sql = "SELECT * FROM WF_EmpWorks WHERE Domain='" + sysNo + "' AND FK_Emp='" + userNo + "'";

         DataTable dt = bp.da.DBAccess.RunSQLReturnTable(sql);
         //南京公安局workid、fid互换

		for(int i=0;i<dt.Rows.size();i++){
			String fid = dt.Rows.get(i).get("FID").toString();
			if("0".equals(fid)){
				continue;
			};
			String flag = fid;
			dt.Rows.get(i).put("FID",dt.Rows.get(i).get("WORKID"));
			dt.Rows.get(i).put("WORKID",flag);
		}
         return Json.ToJson(dt);
	}
	
	  /* @CrossOrigin(origins = "*")
	   @RequestMapping(value = "/TodoAndCC")
		public String TodoAndCC(String userNo, String ADT) throws Exception {
			try {
				Dev2Interface.Port_Login(userNo);
				
				DataTable empWorkDt = new DataTable();
				if (DataType.IsNullOrEmpty(ADT) == true)
					 empWorkDt= Dev2Interface.DB_GenerEmpWorksOfDataTable(userNo, 0, null, null, null,null);
				else
					empWorkDt= Dev2Interface.DB_GenerEmpWorksOfDataTable(userNo, 0, null, null, null," and a.adt>='"+ADT+"'");
				
				DataTable ccDt= Dev2Interface.DB_CCList("");
			      
				 Map  map = new HashMap();
				 map.put("CCDataList", bp.tools.Json.ToJson(ccDt));
				 map.put("CCDataListCount", ccDt.Rows.size());
				 map.put("EmpWorksDataList",bp.tools.Json.ToJson(empWorkDt));
				 map.put("EmpWorksDataListCount", empWorkDt.Rows.size());//没有行数属性吗？
				
				 return JSONObject.fromObject(map).toString();  
//				 return bp.tools.Json.ToJson(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}*/
		
		
	


	/**
	 * 新增新用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/InsertEmp")
	public String InsertEmp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String userNo = request.getParameter("userNo");
		if(DataType.IsNullOrEmpty(userNo)==true)
			return "err@用户账号不能为空";
		bp.app.port.Emp emp = new  bp.app.port.Emp();
		emp.setNo(userNo);
		if(emp.RetrieveFromDBSources()==1){
			return "err@账号:"+userNo+"已经存在";
		}
		//根据组织编号找部门信息
		String orgCode = request.getParameter("orgCode");
		if(DataType.IsNullOrEmpty(orgCode) == true)
			return "err@账号:"+userNo+"的组织编码不能为空";
		bp.app.port.Dept dept = new bp.app.port.Dept();
		int i = dept.Retrieve("OrgCode",orgCode);
		if(i ==0)
			return "err@组织编码为"+orgCode+"的部门信息不存在";
		String userName = request.getParameter("userName");
		String tel = request.getParameter("tel");
		String SecretLv = request.getParameter("SecretLv");
		emp.setName(userName);
		emp.setFK_Dept(dept.getNo());
		if (DataType.IsNullOrEmpty(tel) == false)
			emp.setTel(tel);
		if (DataType.IsNullOrEmpty(SecretLv) == false)
			emp.setTel(SecretLv);
		emp.Insert();
		return "账号:"+userNo+"插入成功";
	}

	/**
	 * 修改用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/UpdateEmp")
	public String UpdateEmp(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String userNo = request.getParameter("userNo");
		if(DataType.IsNullOrEmpty(userNo)==true)
			return "err@用户账号不能为空";
		bp.app.port.Emp emp = new  bp.app.port.Emp();
		emp.setNo(userNo);
		if(emp.RetrieveFromDBSources()==0){
			return "err@账号:"+userNo+"信息不存在";
		}
		//根据组织编号找部门信息
		String orgCode = request.getParameter("orgCode");
		if(DataType.IsNullOrEmpty(orgCode) == true)
			return "err@账号:"+userNo+"的组织编码不能为空";
		bp.app.port.Dept dept = new bp.app.port.Dept();
		int i = dept.Retrieve("OrgCode",orgCode);
		if(i ==0)
			return "err@组织编码为"+orgCode+"的部门信息不存在";

		String userName = request.getParameter("userName");
		String tel = request.getParameter("tel");
		String SecretLv = request.getParameter("SecretLv");
		emp.setName(userName);
		emp.setFK_Dept(dept.getNo());
		if(DataType.IsNullOrEmpty(tel)==false)
			emp.setTel(tel);
		if(DataType.IsNullOrEmpty(SecretLv)==false)
			emp.setTel(SecretLv);
		emp.Update();
		return "账号:"+userNo+"更新成功";
	}

	/**
	 * 删除用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/DeleteEmpByNo")
	public String DeleteEmpByNo(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String userNo = request.getParameter("userNo");
		if(DataType.IsNullOrEmpty(userNo)==true)
			return "err@用户账号不能为空";
		bp.app.port.Emp emp = new  bp.app.port.Emp();
		emp.setNo(userNo);
		if(emp.RetrieveFromDBSources()==1){
			emp.SetValByKey("UserType",0);
			emp.Update();
			WFEmp wfEmp=new WFEmp();
			wfEmp.setNo(userNo);
			wfEmp.Delete();
		}

		return "账号:"+userNo+"删除成功";
	}

	/**
	 * 根据账号查询用户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/SelectEmpByNo")
	public String SelectEmpByNo(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String userNo = request.getParameter("userNo");
		if(DataType.IsNullOrEmpty(userNo)==true)
			return "err@用户账号不能为空";
		bp.app.port.Emp emp = new  bp.app.port.Emp();
		emp.setNo(userNo);
		if (emp.RetrieveFromDBSources() == 0)
			return null;
		return bp.tools.Json.ToJson(emp.ToDataTableField());
	}

	/**
	 * 新增部门信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/InsertDept")
	public String InsertDept(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String deptNo = request.getParameter("deptNo");
		if(DataType.IsNullOrEmpty(deptNo)==true)
			return "err@部门编号不能为空";
		bp.app.port.Dept dept = new  bp.app.port.Dept();
		dept.setNo(deptNo);
		if(dept.RetrieveFromDBSources()==1){
			return "err@部门:"+deptNo+"已经存在";
		}
		String deptName = request.getParameter("deptName");
		String parentNo = request.getParameter("parentNo");
		String leader = request.getParameter("leader");
		String nameOfPath = request.getParameter("nameOfPath");
		String deptType = request.getParameter("deptType");
		String orgCode = request.getParameter("orgCode");
		dept.setName(deptName);
		dept.setParentNo(parentNo);
		if(DataType.IsNullOrEmpty(leader)==false)
			dept.setLeader(leader);
		dept.setNameOfPath(nameOfPath);
		if(DataType.IsNullOrEmpty(deptType)==false)
			dept.setDeptType(Integer.parseInt(deptType));
		dept.setOrgCode(orgCode);
		dept.DirectInsert();
		return "部门编号:"+deptNo+"插入成功";
	}
	@RequestMapping(value = "/UpdateDept")
	public String UpdateDept(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String deptNo = request.getParameter("deptNo");
		if(DataType.IsNullOrEmpty(deptNo)==true)
			return "err@部门编号不能为空";
		bp.app.port.Dept dept = new  bp.app.port.Dept();
		dept.setNo(deptNo);
		if(dept.RetrieveFromDBSources()==0){
			return "err@部门:"+deptNo+"信息不存在";
		}
		String deptName = request.getParameter("deptName");
		String parentNo = request.getParameter("parentNo");
		String leader = request.getParameter("leader");
		String nameOfPath = request.getParameter("nameOfPath");
		String deptType = request.getParameter("deptType");
		String orgCode = request.getParameter("orgCode");
		dept.setName(deptName);
		dept.setParentNo(parentNo);
		if(DataType.IsNullOrEmpty(leader)==false)
			dept.setLeader(leader);
		dept.setNameOfPath(nameOfPath);
		if(DataType.IsNullOrEmpty(deptType)==false)
			dept.setDeptType(Integer.parseInt(deptType));
		dept.setOrgCode(orgCode);
		dept.DirectUpdate();
		return "部门编号:"+deptNo+"更新成功";
	}

	/**
	 * 根据编码信息删除部门信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/DeleteDeptByNo")
	public String DeleteDeptByNo(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String deptNo = request.getParameter("deptNo");
		if(DataType.IsNullOrEmpty(deptNo)==true)
			return "err@部门编号不能为空";
		bp.app.port.Dept dept = new  bp.app.port.Dept();
		dept.setNo(deptNo);
		if(dept.RetrieveFromDBSources()==1){
			dept.SetValByKey("DeptType",0);
			dept.DirectDelete();
		}

		return "部门编号:"+deptNo+"删除成功";

	}

	/**
	 * 根据编号查询部门信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/SelectDeptByNo")
	public String SelectDeptByNo(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String deptNo = request.getParameter("deptNo");
		if(DataType.IsNullOrEmpty(deptNo)==true)
			return "err@部门编号不能为空";
		bp.app.port.Dept dept = new  bp.app.port.Dept();
		dept.setNo(deptNo);
		if (dept.RetrieveFromDBSources() == 0)
			return null;
		return bp.tools.Json.ToJson(dept.ToDataTableField());

	}



	/**
	 * 获得在途
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 * @throws Exception 
	 */
    @RequestMapping(value = "/DB_Runing")
	public String DB_Runing(String userNo, String sysNo) throws Exception {
		 Dev2Interface.Port_Login(userNo);
		 DataTable dt =  Dev2Interface.DB_GenerRuning(userNo, null, false);
         return Json.ToJson(dt);
	}
	
	/**
	 * 我可以发起的流程
	 * @param userNo 用户编号
	 * @param domain  系统编号,为空时返回平台所有数据
	 * @return 返回我可以发起的流程列表.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/DB_StarFlows")
	public String DB_StarFlows(String userNo, String domain) throws Exception {
		Dev2Interface.Port_Login(userNo);
		DataTable dt= Dev2Interface.DB_StarFlows(userNo,domain);
        return Json.ToJson(dt);
	}
	
	/**
	 * 我发起的流程实例
	 * @param userNo 用户编号
	 * @param domain 统编号,为空时返回平台所有数据
	 * @param pageSize
	 * @param pageIdx
	 * @return
	 * @throws Exception 
	 */
    @RequestMapping(value = "/DB_MyStartFlowInstance")
	public String DB_MyStartFlowInstance(String userNo, String domain, int pageSize, int pageIdx) throws Exception {
		try {
			Dev2Interface.Port_Login(userNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "";
        if (domain == null)
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Starter='" + userNo + "'";
        else
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Domain='" + domain + "' AND Starter='" + userNo + "'";

        DataTable dt = bp.da.DBAccess.RunSQLReturnTable(sql);
        return Json.ToJson(dt);
	}

/**
	 * 创建WorkID
	 * @param flowNo 流程编号
	 * @param userNo 工作人员编号
	 * @return 一个长整型的工作流程实例
	 * @throws Exception 
	 */

    @RequestMapping(value = "/CreateWorkID")
	public long CreateWorkID(String flowNo, String userNo) throws Exception {
		 Dev2Interface.Port_Login(userNo);
		 return Dev2Interface.Node_CreateBlankWork(flowNo, userNo);
	}

	/**
	 * 执行发送
	 * @param flowNo 流的程模版ID
	 * @param workid 工作ID
	 * @param ht 参数，或者表单字段.
	 * @param toNodeID 到达的节点ID.如果让系统自动计算就传入0
	 * @param toEmps 到达的人员IDs,比如:zhangsan,lisi,wangwu. 如果为Null就标识让系统自动计算
	 * @return 发送的结果信息.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/SendWork")
	public String SendWork(String flowNo, long workid, Hashtable ht, int toNodeID, String toEmps, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		SendReturnObjs objs = Dev2Interface.Node_SendWork(flowNo, workid, ht, toNodeID, toEmps);

        String msg = objs.ToMsgOfText();
        
        Hashtable myht = new Hashtable();
        myht.put("Message", msg);
        myht.put("IsStopFlow", objs.getIsStopFlow());

        if (objs.getIsStopFlow()==false)
        {
        	myht.put("VarAcceptersID", objs.getVarAcceptersID()==null?"":objs.getVarAcceptersID());
	        myht.put("VarAcceptersName", objs.getVarAcceptersName() == null ?"":objs.getVarAcceptersName());
	        myht.put("VarToNodeID", objs.getVarToNodeID());
	        myht.put("VarToNodeName", objs.getVarToNodeName()==null?"":objs.getVarToNodeName());
        }
        return Json.ToJson(myht);
	}
    @RequestMapping(value = "/SendWorkZHZG")
	public String SendWorkZHZG(long workid, int toNodeID, String toEmps, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		String sql="select FK_Flow from wf_generworkflow where workid="+workid;
		String flowNo=bp.da.DBAccess.RunSQLReturnString(sql);
		SendReturnObjs objs = Dev2Interface.Node_SendWork(flowNo, workid, null, toNodeID, toEmps);

        String msg = objs.ToMsgOfText();

        Hashtable myht = new Hashtable();
        myht.put("Message", msg);
        myht.put("IsStopFlow", objs.getIsStopFlow());
		Hashtable xy = new Hashtable();
        if (objs.getIsStopFlow()==false)
        {
        	myht.put("VarAcceptersID", objs.getVarAcceptersID()==null?"":objs.getVarAcceptersID());
	        myht.put("VarAcceptersName", objs.getVarAcceptersName() == null ?"":objs.getVarAcceptersName());
	        myht.put("VarToNodeID", objs.getVarToNodeID());
	        myht.put("VarToNodeName", objs.getVarToNodeName()==null?"":objs.getVarToNodeName());
        }
        //返回待办信息
		GenerWorkFlow gwf=new GenerWorkFlow(workid);
		String	sql2 = "SELECT workid,fid,fk_node,fk_emp FROM WF_EmpWorks WHERE workid='" + workid + "' or fid='" + workid + "' or (workid='" + gwf.getFID() + "' and fid = 0)" ;

		DataTable dt = bp.da.DBAccess.RunSQLReturnTable(sql2);
		myht.put("VarToDoList", dt.Rows);
        return Json.ToJson(myht);
	}
	/**
	 * 保存参数
	 * @param workid 工作ID
	 * @param paras 用于控制流程运转的参数，比如方向条件. 格式为:@JinE=1000@QingJaiTianShu=100
	 * @throws Exception 
	 */

    @RequestMapping(value = "/SaveParas")
	public void SaveParas(long workid, String paras, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		Dev2Interface.Flow_SaveParas(workid, paras);
		
	}

	/**
	 * 获得下一个节点信息
	 * @param flowNo 流程编号
	 * @param workid 流程实例
	 * @param paras 方向条件所需要的参数，可以为空。
	 * @return 下一个节点的JSON.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/GenerNextStepNode")
	public String GenerNextStepNode(String flowNo, long workid, String paras, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		if (paras != null)
            Dev2Interface.Flow_SaveParas(workid, paras);

        int nodeID = Dev2Interface.Node_GetNextStepNode(flowNo, workid);
        Node nd = new Node(nodeID);

       //如果字段 DeliveryWay = 4 就表示到达的接点是由当前节点发送人选择接收人.
		//自定义参数的字段是 SelfParas, DeliveryWay 
		// CondModel = 方向条件计算规则.
        return nd.ToJson();
	}

	/**
	 * 获得下一步节点的接收人
	 * @param flowNo 流程ID
	 * @param toNodeID 节点ID
	 * @param workid 工作事例ID
	 * @return 返回两个结果集一个是分组的Depts(No,Name)，另外一个是人员的Emps(No, Name, FK_Dept),接受后，用于构造人员选择器.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/GenerNextStepNodeEmps")
	public String GenerNextStepNodeEmps(String flowNo, int toNodeID, int workid, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		Selector select = new Selector(toNodeID);
        Node nd = new Node(toNodeID);

        GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt", workid);
        DataSet ds = select.GenerDataSet(toNodeID, rpt);
        return Json.ToJson(ds);
	}
	
	/**
	 * 将要退回到的节点
	 * @param workID
	 * @return 返回节点集合的json.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/WillReturnToNodes")
	public String WillReturnToNodes(int workID, String userNo) throws Exception {
		
		try
		{
			
		Dev2Interface.Port_Login(userNo);
		
		GenerWorkFlow gwf=new GenerWorkFlow(workID);
		
		DataTable dt=Dev2Interface.DB_GenerWillReturnNodes(gwf.getFK_Node(), workID, gwf.getFID()); 
        return Json.ToJson(dt);
		}catch(Exception ex)
		{
		  return "err@"+ex.getMessage();
		}
	}

	/**
	 * 将要达到的节点
	 * @param currNodeID 当前节点ID
	 * @return 返回节点集合的json.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/WillToNodes")
	public String WillToNodes(int currNodeID, String userNo) throws Exception {
		
		try
		{
		Dev2Interface.Port_Login(userNo);
		Node nd = new Node(currNodeID);
		
        Directions dirs = new Directions();
        Nodes nds = dirs.GetHisToNodes(currNodeID, false);
        return nds.ToJson();
		}catch(Exception ex)
		{
		  return "err@"+ex.getMessage();
		}
	}

	/**
	 * 获得当前节点信息.
	 * @param currNodeID  当前节点ID
	 * @return
	 * @throws Exception 
	 */

    @RequestMapping(value = "/CurrNodeInfo")
	public String CurrNodeInfo(int currNodeID, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		Node nd = new Node(currNodeID);
         return nd.ToJson();
	}

	/**
	 * 获得当前流程信息.
	 * @param flowNo 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */

    @RequestMapping(value = "/CurrFlowInfo")
	public String CurrFlowInfo(String flowNo, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		Flow fl = new Flow(flowNo);
          return fl.ToJson();
	}

	/**
	 * 获得当前流程信息.
	 * @param workID 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */
    @RequestMapping(value = "/CurrGenerWorkFlowInfo")
	public String CurrGenerWorkFlowInfo(long workID, String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
         return gwf.ToJson();
	}

	
	/**
	 * 退回.
	 * @param workID 流程ID
	 * @param returnToNodeID 流程退回的节点ID
	 * @param returnMsg 退回原因
	 * @return 退回结果信息
	 * @throws Exception 
	 */
    @RequestMapping(value = "/Node_ReturnWork")
   public String Node_ReturnWork(long workID, int returnToNodeID, String returnMsg, String userNo) throws Exception {
	  Dev2Interface.Port_Login(userNo);
	  GenerWorkFlow gwf=new GenerWorkFlow(workID);
      return Dev2Interface.Node_ReturnWork(gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), returnToNodeID,null, returnMsg,false);
	  
	
   }
  
/**
	 * 执行流程结束 说明:强制流程结束.
	 *
	 * @param workID
	 *            工作ID
	 * @param msg
	 *            流程结束原因
	 
	 * @return 返回成功执行信息
	 * @throws Exception
	 */

    @RequestMapping(value = "/Flow_DoFlowOverQiangZhi")
	public  String Flow_DoFlowOverQiangZhi(long workID, String msg, String userNo) throws Exception {
	  Dev2Interface.Port_Login(userNo);
	  LocalWS localWS = new LocalWS();
	  String sql="select FK_Flow from wf_generworkflow where workid="+workID;
		String flowNo=bp.da.DBAccess.RunSQLReturnString(sql);
		return localWS.Flow_DoFlowOverQiangZhi(workID,msg, userNo);
				
				
		
  }

    @RequestMapping(value = "/Port_Login")
	public void Port_Login(String userNo) throws Exception{
		
		Dev2Interface.Port_Login(userNo);
	}
	
	/**
	 * 执行撤销
	 * @param flowNo 流程编码
	 * @param workID 工作ID
	 * @param unSendToNode 撤销到的节点
	 * @param fid 
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/Runing_UnSend")
	public String Runing_UnSend(String userNo,String flowNo, long workID, int unSendToNode,long fid) throws Exception{
		
		Dev2Interface.Port_Login(userNo);
		
		return Dev2Interface.Flow_DoUnSend(flowNo, workID,unSendToNode,fid);
	}
	
	/**
	 * 流程结束后回滚
	 * @param flowNo 流程编码
	 * @param workId 工作ID
	 * @param backToNodeID 回滚到的节点ID
	 * @param backMsg 回滚原因
	 * @return 回滚信息
	 * @throws Exception 
	 */
    @RequestMapping(value = "/DoRebackFlowData")
	public String DoRebackFlowData(String flowNo,long workId,int backToNodeID,String backMsg, String userNo) throws Exception{
		Dev2Interface.Port_Login(userNo);
		FlowExt flow = new FlowExt(flowNo);
		return flow.DoRebackFlowData(workId, backToNodeID, backMsg);
	}
	/** 
	
   
    
	/** 
	 获得工作进度-用于展示流程的进度图
	 
	 @param workID workID
	 @param userNo 用户编号
	 @return 返回待办
	*/
    @RequestMapping(value = "/WorkProgressBar")
    public String WorkProgressBar(long  workID, String userNo) throws Exception
    {
        DataSet ds = Dev2Interface.DB_JobSchedule(workID);
        return Json.ToJson(ds);
    }

    /** 
	 查询数据	 
	 @param sqlOfSelect 要查询的sql
	 @param password 用户密码
	 @return 返回查询数据
     * @throws Exception 
	*/
    @RequestMapping(value = "/DB_RunSQLReturnJSON")
    public String DB_RunSQLReturnJSON(String sqlOfSelect, String password) throws Exception
    {
        if ( password.equals(password) == false)
            return "err@密码错误";

        DataTable dt = bp.da.DBAccess.RunSQLReturnTable(sqlOfSelect);
        return Json.ToJson(dt);
    }

    
    /**
	 * 执行抄送
	 * 
	 * @param fk_node
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param toEmpNo
	 *            抄送人员编号
	 * @param toEmpName
	 *            抄送人员人员名称
	 * @param msgTitle
	 *            标题
	 * @param msgDoc
	 *            内容
	 * @return 执行信息
	 * @throws Exception
	 */
    @RequestMapping(value = "/Node_CC_WriteTo_CClist")
	public String Node_CC_WriteTo_CClist(int fk_node, long workID, String toEmpNo, String toEmpName,
			String msgTitle, String msgDoc,String userNo) throws Exception {
		Dev2Interface.Port_Login(userNo);
		return Dev2Interface.Node_CC_WriteTo_CClist(fk_node, workID,toEmpNo,toEmpName,msgTitle,msgDoc);
	}
	

	   /** 
	 	 是否可以查看该流程	 
	 	 @param flowNo 流程编号
	 	 @param workid 工作ID
	 	 @return 是否可以查看该工作.
	 * @throws Exception 
	 	*/
    @RequestMapping(value = "/Flow_IsCanView")
    public Boolean Flow_IsCanView(String flowNo, long workid, String userNo) throws Exception
    {
        return Dev2Interface.Flow_IsCanViewTruck(flowNo, workid,userNo);
    }
    
    /** 
	 是否可以查看该流程	 
	 @param userNo 要查询的 sql
	 @param workid 用户密码
	 @return 是否可以查看该工作.
* @throws Exception 
	*/
    @RequestMapping(value = "/Flow_IsCanDoCurrentWork")
    public Boolean Flow_IsCanDoCurrentWork(long workid, String userNo) throws Exception
    {
        return Dev2Interface.Flow_IsCanDoCurrentWork(workid, userNo);
    }

	/**
	 * 获取指定人员的抄送列表 说明:可以根据这个列表生成指定用户的抄送数据.
	 * 
	 * @param userNo
	 *            人员编号,如果是null,则返回所有的.
	 * @return 返回该人员的所有抄送列表,结构同表WF_CCList.
	 */
    @RequestMapping(value = "/DB_CCList")
	public String DB_CCList(String userNo) throws Exception{
		Dev2Interface.Port_Login(userNo);
		
		DataTable dt = Dev2Interface.DB_CCList(userNo);
		return Json.ToJson(dt);
	}
    /**
	 * 获得工作进度-用于展示流程的进度图 - for zhongkeshuguang.
	 * 
	 * @param workID
	 *            workID
	 * @param userNo
	 *            用户编号
	 * @return 返回待办
	 */
    @RequestMapping(value = "/WorkProgressBar20")
	public String WorkProgressBar20(long  workID, String userNo) throws Exception
    {

		Dev2Interface.Port_Login(userNo);
		
		return AppClass.JobSchedule(workID);
        
    }
    
    
	// 根据当前节点获得下一个节点.
    @RequestMapping(value = "/GetNextNodeID")
	public int GetNextNodeID(int nodeID, DataTable dirs)
    {
        int toNodeID = 0;
        for (DataRow dir : dirs.Rows)        
        {
            if ( Integer.parseInt(dir.getValue("Node").toString()) == nodeID)
            {
                toNodeID = Integer.parseInt( dir.getValue("ToNode").toString());
                break;
            }
        }

        int toNodeID2 = 0;
        
        for (DataRow dir11 : dirs.Rows)
        {
            if (Integer.parseInt(dir11.getValue("Node").toString()) ==nodeID )
            {
                toNodeID2 = Integer.parseInt(dir11.getValue("ToNode").toString());
            }
        }

        //两次去的不一致，就有分支，有分支就reutrn 0 .
        if (toNodeID2 == toNodeID)       
            return toNodeID; 
        return  0 ; 
    }
 
    @RequestMapping(value = "/SDK_Page_Init")
	public String SDK_Page_Init(long  workID, String userNo) throws Exception
    {
		Dev2Interface.Port_Login(userNo);
		return  Dev2Interface.SDK_Page_Init(workID);
    }
    
    /** 
    写入审核信息
	<param name="workid">workID</param>
	<param name="msg">审核信息</param>
	*
	*/
    @RequestMapping(value = "/Node_WriteWorkCheck")
   public void Node_WriteWorkCheck(long workid, String msg) throws Exception
   {
        GenerWorkFlow gwf = new GenerWorkFlow(workid);
        Dev2Interface.WriteTrackWorkCheck(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID(), msg,"审核");
   }

}
