package cn.jflow.common.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Tools.StringHelper;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/WorkOpt")
public class WorkOptController extends BaseController{
	HttpServletResponse response;
	HttpServletRequest request;
	  public String getRB_DeleteWay(){
    	  String str=getRequest().getParameter("RB_DeleteWay");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
      public String getCB_IsDeleteSubFlow(){
    	  String str=getRequest().getParameter("CB_IsDeleteSubFlow");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
      
      
	@RequestMapping(value = "/WorkOpt", method = RequestMethod.POST)
	protected void execute(TempObject object, HttpServletRequest request,HttpServletResponse response) {
 		String doType = request.getParameter("DoType");
		String msg="";
		PrintWriter out =null;
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try
        {
			if("Press".equals(doType))  //催办
			{
        		msg = press(request,response);
        	}else if("ReturnToNodes".equals(doType))//获得可以退回的节点
        	{
        		msg = ReturnToNodes();
        	}else if("DoReturnWork".equals(doType))//返回退回信息
        	{
        		msg = ReturnWork();
        	}else if ("SelectEmps".equals(doType))
        	{
        		msg = SelectEmps();
        	}else if ("DeleteFlowInstance_DoDelete".equals(doType))//执行删除流程.
        	{
        		msg = DeleteFlowInstance_DoDelete();
        	}else{
        		 msg = "err@没有判断的执行类型：" + this.getDoType();
                 }
        	out = response.getWriter();
			out.write(msg);
        }catch (IOException e) {
			Log.DebugWriteError("common/WorkOpt/Press.do?DoType= "+doType +"err@"+e.getMessage());
		}finally{
			if(null !=out){
				out.close();
			}
		}
 
    }
   /*
    * 执行催办
    */
    public String press(HttpServletRequest request,HttpServletResponse response)
    {
         String msg = request.getParameter("msg");
        //调用API.
        return BP.WF.Dev2Interface.Flow_DoPress(this.getWorkID(), msg, true);
    }
    /*
     * 获得可以退回的节点
     */
    public String ReturnToNodes()
    {
        DataTable dt = BP.WF.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(), this.getWorkID(), this.getFID());
        return BP.Tools.Json.ToJson(dt);
    }
    /*
     * 执行退回,返回退回信息.
     */
    public String ReturnWork()
    {
    	int toNodeID = Integer.parseInt(getRequest().getParameter("ReturnToNode"));
        String reMesage = getRequest().getParameter("ReturnMsg");

        boolean isBackBoolen = false;
        String isBack =getRequest().getParameter("IsBack");
        if (isBack.equals("1"))
            isBackBoolen = true;
        return BP.WF.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), this.getFID(), this.getFK_Node(), toNodeID, reMesage, isBackBoolen);
       
    }
    
    /**
     * 人员选择器
     * @return
     */
    public String SelectEmps()
    {
        String fk_dept = getFK_Dept();
        if (StringHelper.isNullOrEmpty(fk_dept))
        {
        	fk_dept = BP.Web.WebUser.getFK_Dept();
        }

        DataSet ds = new DataSet();

        String sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ";
        DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);
        dtDept.TableName = "Depts";
        ds.Tables.add(dtDept);

        sql = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE FK_Dept='" + fk_dept + "'";
        DataTable dtEmps = BP.DA.DBAccess.RunSQLReturnTable(sql);
        dtEmps.TableName = "Emps";
        ds.Tables.add(dtEmps);

        return BP.Tools.Json.ToJson(ds);
    }
    
    /**
     * 获取部门
     */
    public String getFK_Dept()
    {
    	String str  = getRequest().getParameter("FK_Dept");
    	if(null == str )
    	{
    		str = "";
    	}
    	return str;
    }
    

    /**
     * 执行删除流程
     */
    
    public String DeleteFlowInstance_DoDelete(){		
    	try {
			if (BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(super.getFK_Flow(), 
					getWorkID(), BP.Web.WebUser.getNo()) == false)
			        return "您没有删除该流程的权限.";
			 String deleteWay = getRB_DeleteWay();
			 String doc = super.getTB_Doc();
			 String isDeleteSubFlow = getCB_IsDeleteSubFlow();
	            boolean isDelSubFlow = false;
	            if (isDeleteSubFlow.equals("1"))
	                isDelSubFlow = true;
	            //按照标记删除.
	            if (deleteWay.equals("0"))
	                BP.WF.Dev2Interface.Flow_DoDeleteFlowByFlag(super.getFK_Flow(),getWorkID(), doc, isDelSubFlow);
	            //彻底删除.
	            if (deleteWay.equals("1"))
	                BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(super.getFK_Flow(), getWorkID(), isDelSubFlow);
	            //彻底并放入到删除轨迹里.
	            if (deleteWay.equals("2"))
	                BP.WF.Dev2Interface.Flow_DoDeleteFlowByWriteLog(super.getFK_Flow(), getWorkID(), doc, isDelSubFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "流程删除成功.";
    }
    
    
    
    
    
    
}
