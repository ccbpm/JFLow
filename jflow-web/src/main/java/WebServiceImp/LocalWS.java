package WebServiceImp;

import java.util.Hashtable;

import javax.jws.WebService;

import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.WF.Flow;
import BP.WF.GenerWorkFlow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Data.GERpt;
import BP.WF.Template.CondModel;
import BP.WF.Template.Directions;
import BP.WF.Template.Selector;
import WebService.LocalWSI;

@WebService
public class LocalWS implements LocalWSI{
	
	/**
	 * 待办
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 */
	@Override
	public String DB_Todolist(String userNo, String sysNo) {
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
	@Override
	public String DB_Runing(String userNo, String sysNo) throws Exception {
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
	@Override
	public String DB_StarFlows(String userNo, String sysNo) throws Exception {
		DataTable dt= BP.WF.Dev2Interface.DB_StarFlows(userNo);
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
	@Override
	public String DB_MyStartFlowInstance(String userNo, String sysNo, int pageSize, int pageIdx) {
		String sql = "";
        if (sysNo == null)
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Starter='" + userNo + "'";
        else
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Domain='" + sysNo + "' AND Starter='" + userNo + "'";

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
	@Override
	public long CreateWorkID(String flowNo, String userNo) throws Exception {
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
	@Override
	public String SendWork(String flowNo, long workid, Hashtable ht, int toNodeID, String toEmps) throws Exception {
		BP.WF.SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(flowNo, workid, ht, toNodeID, toEmps);

        String msg = objs.ToMsgOfText();

        Hashtable myht = new Hashtable();
        myht.put("Message", msg);
        myht.put("IsStopFlow", objs.getIsStopFlow());
        myht.put("VarAcceptersID", objs.getVarAcceptersID());
        myht.put("VarAcceptersName", objs.getVarAcceptersName());
        myht.put("VarToNodeID", objs.getVarToNodeID());
        myht.put("VarToNodeName", objs.getVarToNodeName());

        return BP.Tools.Json.ToJson(myht);
	}

	/**
	 * 保存参数
	 * @param workid 工作ID
	 * @param paras 用于控制流程运转的参数，比如方向条件. 格式为:@JinE=1000@QingJaiTianShu=100
	 * @throws Exception 
	 */
	@Override
	public void SaveParas(long workid, String paras) throws Exception {
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
	@Override
	public String GenerNextStepNode(String flowNo, long workid, String paras) throws Exception {
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
	@Override
	public String GenerNextStepNodeEmps(String flowNo, int toNodeID, int workid) throws Exception {
		Selector select = new Selector(toNodeID);
        Node nd = new Node(toNodeID);

        GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt", workid);
        DataSet ds = select.GenerDataSet(toNodeID, rpt);
        return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 将要达到的节点
	 * @param currNodeID 当前节点ID
	 * @return 返回节点集合的json.
	 * @throws Exception 
	 */
	@Override
	public String WillToNodes(int currNodeID) throws Exception {
		Node nd = new Node(currNodeID);
        if (nd.getCondModel() != CondModel.SendButtonSileSelect)
            return "err@";

        Directions dirs = new Directions();
        Nodes nds = dirs.GetHisToNodes(currNodeID, false);
        return nds.ToJson();
	}

	/**
	 * 获得当前节点信息.
	 * @param currNodeID  当前节点ID
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String CurrNodeInfo(int currNodeID) throws Exception {
		 Node nd = new Node(currNodeID);
         return nd.ToJson();
	}

	/**
	 * 获得当前流程信息.
	 * @param flowNo 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */
	@Override
	public String CurrFlowInfo(String flowNo) throws Exception {
		  Flow fl = new Flow(flowNo);
          return fl.ToJson();
	}

	/**
	 * 获得当前流程信息.
	 * @param workID 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */
	@Override
	public String CurrGenerWorkFlowInfo(long workID) throws Exception {
		 GenerWorkFlow gwf = new GenerWorkFlow(workID);
         return gwf.ToJson();
	}

}