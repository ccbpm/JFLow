package Controller;

import WebServiceImp.LocalWS;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.WF.Flow;
import BP.WF.GenerWorkFlow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Data.GERpt;
import BP.WF.Template.Directions;
import BP.WF.Template.FlowExt;
import BP.WF.Template.Selector;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/restful")
public class RestFulController {
    //ip:port/project

    @RequestMapping(value = "/test")
    public String queryPrestoDemo(HttpServletRequest request, HttpServletResponse response) {
        //0000002007
        String userNo = request.getParameter("userNo");
        //请销假
        String domain = request.getParameter("domain");

        LocalWS localWS = new LocalWS();

        String result = null;
        try {
            result = localWS.DB_StarFlows(userNo,domain);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
	 * 待办
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 */

    @RequestMapping(value = "/DB_Todolist")
	public String DB_Todolist(String userNo, String sysNo) {
		try {
			BP.WF.Dev2Interface.Port_Login(userNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String sql = "";
         if (DataType.IsNullOrEmpty(sysNo) == true)
             sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='" + userNo + "'";
         else
             sql = "SELECT * FROM WF_EmpWorks WHERE Domain='" + sysNo + "' AND FK_Emp='" + userNo + "'";

         DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
         return BP.Tools.Json.ToJson(dt);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		 DataTable dt = BP.WF.Dev2Interface.DB_GenerRuning(userNo, null, false);
         return BP.Tools.Json.ToJson(dt);
	}
	
	/**
	 * 我可以发起的流程
	 * @param userNo 用户编号
	 * @param sysNo  系统编号,为空时返回平台所有数据
	 * @return 返回我可以发起的流程列表.
	 * @throws Exception 
	 */

    @RequestMapping(value = "/DB_StarFlows")
	public String DB_StarFlows(String userNo, String domain) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		DataTable dt= BP.WF.Dev2Interface.DB_StarFlows(userNo,domain);
        return BP.Tools.Json.ToJson(dt);
	}
	
	/**
	 * 我发起的流程实例
	 * @param userNo 用户编号
	 * @param sysNo 统编号,为空时返回平台所有数据
	 * @param pageSize
	 * @param pageIdx
	 * @return
	 */
    @RequestMapping(value = "/DB_MyStartFlowInstance")
	public String DB_MyStartFlowInstance(String userNo, String domain, int pageSize, int pageIdx) {
		try {
			BP.WF.Dev2Interface.Port_Login(userNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "";
        if (domain == null)
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Starter='" + userNo + "'";
        else
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Domain='" + domain + "' AND Starter='" + userNo + "'";

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        return BP.Tools.Json.ToJson(dt);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		 return BP.WF.Dev2Interface.Node_CreateBlankWork(flowNo, userNo);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		BP.WF.SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(flowNo, workid, ht, toNodeID, toEmps);

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
        return BP.Tools.Json.ToJson(myht);
	}
    @RequestMapping(value = "/SendWorkZHZG")
	public String SendWorkZHZG(long workid, int toNodeID, String toEmps, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		String sql="select FK_Flow from wf_generworkflow where workid="+workid;
		String flowNo=BP.DA.DBAccess.RunSQLReturnString(sql);
		BP.WF.SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(flowNo, workid, null, toNodeID, toEmps);

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
        return BP.Tools.Json.ToJson(myht);
	}
	/**
	 * 保存参数
	 * @param workid 工作ID
	 * @param paras 用于控制流程运转的参数，比如方向条件. 格式为:@JinE=1000@QingJaiTianShu=100
	 * @throws Exception 
	 */

    @RequestMapping(value = "/SaveParas")
	public void SaveParas(long workid, String paras, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		BP.WF.Dev2Interface.Flow_SaveParas(workid, paras);
		
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		if (paras != null)
            BP.WF.Dev2Interface.Flow_SaveParas(workid, paras);

        int nodeID = BP.WF.Dev2Interface.Node_GetNextStepNode(flowNo, workid);
        BP.WF.Node nd = new BP.WF.Node(nodeID);

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
		BP.WF.Dev2Interface.Port_Login(userNo);
		Selector select = new Selector(toNodeID);
        Node nd = new Node(toNodeID);

        GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt", workid);
        DataSet ds = select.GenerDataSet(toNodeID, rpt);
        return BP.Tools.Json.ToJson(ds);
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
			
		BP.WF.Dev2Interface.Port_Login(userNo);
		
		GenerWorkFlow gwf=new GenerWorkFlow(workID);
		
		DataTable dt=BP.WF.Dev2Interface.DB_GenerWillReturnNodes(gwf.getFK_Node(), workID, gwf.getFID()); 
        return BP.Tools.Json.ToJson(dt);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
         return gwf.ToJson();
	}

	
	/**
	 * 退回.
	 * @param workID 流程ID
	 * @param retunrnToNodeID 流程退回的节点ID
	 * @param returnMsg 退回原因
	 * @return 退回结果信息
	 * @throws Exception 
	 */
    @RequestMapping(value = "/Node_ReturnWork")
   public String Node_ReturnWork(long workID, int returnToNodeID, String returnMsg, String userNo) throws Exception {
	  BP.WF.Dev2Interface.Port_Login(userNo);
	  GenerWorkFlow gwf=new GenerWorkFlow(workID);
      return BP.WF.Dev2Interface.Node_ReturnWork(gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), returnToNodeID,null, returnMsg,false);
	  
	
   }
  
/**
	 * 执行流程结束 说明:强制流程结束.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param msg
	 *            流程结束原因
	 * @return 返回成功执行信息
	 * @throws Exception
	 */

    @RequestMapping(value = "/Flow_DoFlowOverQiangZhi")
	public  String Flow_DoFlowOverQiangZhi(String flowNo, long workID, String msg, String userNo) throws Exception {
	  BP.WF.Dev2Interface.Port_Login(userNo);
		return BP.WF.Dev2Interface.Flow_DoFlowOver(flowNo, workID, msg);
		
  }

    @RequestMapping(value = "/Port_Login")
	public void Port_Login(String userNo) throws Exception{
		
		BP.WF.Dev2Interface.Port_Login(userNo);
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
		
		BP.WF.Dev2Interface.Port_Login(userNo);
		
		return BP.WF.Dev2Interface.Flow_DoUnSend(flowNo, workID,unSendToNode,fid);
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
		BP.WF.Dev2Interface.Port_Login(userNo);
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
        DataSet ds = BP.WF.Dev2Interface.DB_JobSchedule(workID);
        return BP.Tools.Json.ToJson(ds);
    }

    /** 
	 查询数据	 
	 @param sqlOfSelect 要查询的sql
	 @param password 用户密码
	 @return 返回查询数据
	*/
    @RequestMapping(value = "/DB_RunSQLReturnJSON")
    public String DB_RunSQLReturnJSON(String sqlOfSelect, String password)
    {
        if ( password.equals(password) == false)
            return "err@密码错误";

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlOfSelect);
        return BP.Tools.Json.ToJson(dt);
    }

    
    /**
	 * 执行抄送
	 * 
	 * @param flowNo
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		return BP.WF.Dev2Interface.Node_CC_WriteTo_CClist(fk_node, workID,toEmpNo,toEmpName,msgTitle,msgDoc);
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
        return BP.WF.Dev2Interface.Flow_IsCanViewTruck(flowNo, workid,userNo);
    }
    
    /** 
	 是否可以查看该流程	 
	 @param flowNo 要查询的 sql
	 @param workid 用户密码
	 @return 是否可以查看该工作.
* @throws Exception 
	*/
    @RequestMapping(value = "/Flow_IsCanDoCurrentWork")
    public Boolean Flow_IsCanDoCurrentWork(long workid, String userNo) throws Exception
    {
        return BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(workid, userNo);
    }

	/**
	 * 获取指定人员的抄送列表 说明:可以根据这个列表生成指定用户的抄送数据.
	 * 
	 * @param FK_Emp
	 *            人员编号,如果是null,则返回所有的.
	 * @return 返回该人员的所有抄送列表,结构同表WF_CCList.
	 */
    @RequestMapping(value = "/DB_CCList")
	public String DB_CCList(String userNo) throws Exception{
		BP.WF.Dev2Interface.Port_Login(userNo);
		
		DataTable dt = BP.WF.Dev2Interface.DB_CCList(userNo);
		return BP.Tools.Json.ToJson(dt);
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
	public String WorkProgressBar20(String  workID, String userNo) throws Exception
    {
    	long workId = Long.parseLong(workID);
    	BP.WF.Dev2Interface.Port_Login(userNo);
        DataSet ds = BP.WF.Dev2Interface.DB_JobSchedule(workId);

        DataTable gwf = ds.GetTableByName("WF_GenerWorkFlow"); //工作记录.
        DataTable nodes = ds.GetTableByName("WF_Node"); //节点.
        DataTable dirs = ds.GetTableByName("WF_Direction"); //连接线.
        DataTable tracks = ds.GetTableByName("Track"); //历史记录.

        //状态,
        int wfState = Integer.parseInt(gwf.Rows.get(0).getValue("WFState").toString());
        int currNode = Integer.parseInt(gwf.Rows.get(0).getValue("FK_Node").toString());
       // String currNode = gwf.Rows.get(0).getValue("FK_Node").toString(); //停留节点.
        if (wfState == 3)
            return BP.Tools.Json.ToJson(tracks); //如果流程结束了，所有的数据都在tracks里面.
 
        //把以后的为未完成的节点放入到track里面.
        for (int i = 0; i < 100; i++)
        {
            int nextNode = GetNextNodeID(currNode, dirs);
            if (nextNode == 0)
                break;

        	for (DataRow nd : nodes.Rows)           
            {
                if (Integer.parseInt(nd.getValue("NodeID").toString()) == nextNode)
                {
                    DataRow mydr = tracks.NewRow();
                    mydr.setValue("NodeName", nd.getValue("Name").toString());
                    mydr.setValue("FK_Node", nd.getValue("NodeID").toString());     
                    
                    tracks.Rows.add(mydr);
                    break;
                }
            }
        	
            currNode = nextNode;

        }
        	
        return BP.Tools.Json.ToJson(tracks);
        
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
		BP.WF.Dev2Interface.Port_Login(userNo);
		return  BP.WF.Dev2Interface.SDK_Page_Init(workID);
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
        BP.WF.Dev2Interface.WriteTrackWorkCheck(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID(), msg,"审核");
   }
}
