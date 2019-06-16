package WebService;

import java.util.Hashtable;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 外部接口
 * @author Administrator
 *
 */

public interface LocalWSI {
	/**
	 * 待办
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 */

	public String DB_Todolist(String userNo, String sysNo);
	
	/**
	 * 获得在途
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 * @throws Exception 
	 */

	public String DB_Runing(String userNo, String sysNo) throws Exception;
	
	/**
	 * 我可以发起的流程
	 * @param userNo 用户编号
	 * @param sysNo  系统编号,为空时返回平台所有数据
	 * @return 返回我可以发起的流程列表.
	 * @throws Exception 
	 */

	public String DB_StarFlows(String userNo, String sysNo) throws Exception;
	
	/**
	 * 我发起的流程实例
	 * @param userNo 用户编号
	 * @param sysNo 统编号,为空时返回平台所有数据
	 * @param pageSize
	 * @param pageIdx
	 * @return
	 */

	public String DB_MyStartFlowInstance(String userNo, String sysNo, int pageSize, int pageIdx);
	/**
	 * 创建WorkID
	 * @param flowNo 流程编号
	 * @param userNo 工作人员编号
	 * @return 一个长整型的工作流程实例
	 * @throws Exception 
	 */

	public long CreateWorkID(String flowNo, String userNo) throws Exception;
	
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

	public String SendWork(String flowNo, long workid, Hashtable ht, int toNodeID, String toEmps, String userNo) throws Exception;
	
	/**
	 * 保存参数
	 * @param workid 工作ID
	 * @param paras 用于控制流程运转的参数，比如方向条件. 格式为:@JinE=1000@QingJaiTianShu=100
	 * @throws Exception 
	 */

	public void SaveParas(long workid, String paras, String userNo) throws Exception;
	
	/**
	 * 获得下一个节点信息
	 * @param flowNo 流程编号
	 * @param workid 流程实例
	 * @param paras 方向条件所需要的参数，可以为空。
	 * @return 下一个节点的JSON.
	 * @throws Exception 
	 */

	public String GenerNextStepNode(String flowNo, long workid, String paras, String userNo) throws Exception;
	
	/**
	 * 获得下一步节点的接收人
	 * @param flowNo 流程ID
	 * @param toNodeID 节点ID
	 * @param workid 工作事例ID
	 * @return 返回两个结果集一个是分组的Depts(No,Name)，另外一个是人员的Emps(No, Name, FK_Dept),接受后，用于构造人员选择器.
	 * @throws Exception 
	 */

	public String GenerNextStepNodeEmps(String flowNo, int toNodeID, int workid, String userNo) throws Exception;
	
	/**
	 * 将要达到的节点
	 * @param currNodeID 当前节点ID
	 * @return 返回节点集合的json.
	 * @throws Exception 
	 */

	public String WillToNodes(int currNodeID, String userNo) throws Exception;
	
	/**
	 * 将要退回到的节点
	 * @param workID
	 * @return 返回节点集合的json.
	 * @throws Exception 
	 */

	public String WillReturnToNodes(int workID, String userNo) throws Exception;
	
	/**
	 * 获得当前节点信息.
	 * @param currNodeID  当前节点ID
	 * @return
	 * @throws Exception 
	 */

	public String CurrNodeInfo(int currNodeID, String userNo) throws Exception;
	
	/**
	 * 获得当前流程信息.
	 * @param flowNo 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */

	public String CurrFlowInfo(String flowNo, String userNo) throws Exception;
	
	/**
	 * 获得当前流程信息.
	 * @param workID 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */

	public String CurrGenerWorkFlowInfo(long workID, String userNo) throws Exception;

	

	/**
	 * 退回.
	 * @param workID 流程ID
	 * @param retunrnToNodeID 流程退回的节点ID
	 * @param returnMsg 退回原因
	 * @return 退回结果信息
	 * @throws Exception 
	 */
	public String Node_ReturnWork(long workID,int returnToNodeID,String returnMsg, String userNo) throws Exception;
	
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

	public  String Flow_DoFlowOverQiangZhi(String flowNo, long workID, String msg, String userNo) throws Exception;

	
	/**
	 * 用户登陆
	 * @param UserNo
	 * @throws Exception
	 */
	public void Port_Login(String UserNo) throws Exception;
	
	/**
	 * 执行撤销
	 * @param flowNo 流程编码
	 * @param workID 工作ID
	 * @param unSendToNode 撤销到的节点
	 * @param fid 
	 * @return
	 * @throws Exception
	 */
	public String Runing_UnSend(String userNo,String flowNo, long workID, int unSendToNode,long fid) throws Exception;
	
	/**
	 * 流程结束后回滚
	 * @param flowNo 流程编码
	 * @param workId 工作ID
	 * @param backToNodeID 回滚到的节点ID
	 * @param backMsg 回滚原因
	 * @return 回滚信息
	 * @throws Exception 
	 */
	public String DoRebackFlowData(String flowNo,long workId,int backToNodeID,String backMsg, String userNo) throws Exception;

	/// <summary>
    /// 获得当前流程信息.
    /// </summary>
    /// <param name="flowNo">流程ID.</param>
    /// <returns>当前节点信息</returns>
    public String CurrFlowInfo(String flowNo)throws Exception;
  /// <summary>
    /// 获得当前流程信息.
    /// </summary>
    /// <param name="flowNo">流程ID.</param>
    /// <returns>当前节点信息</returns>
    public String CurrGenerWorkFlowInfo(long workID)throws Exception;
    /** 
	 获得工作进度-用于展示流程的进度图
	 
	 @param workID workID
	 @param userNo 用户编号
	 @return 返回待办
	*/
   public String WorkProgressBar(long  workID, String userNo) throws Exception;
   /** 
	 查询数据	 
	 @param sqlOfSelect 要查询的sql
	 @param password 用户密码
	 @return 返回查询数据
	*/
   public String DB_RunSQLReturnJSON(String sqlOfSelect, String password) throws Exception;


   /** 
 	 是否可以查看该流程	 
 	 @param flowNo 流程编号
 	 @param workid 工作ID
 	 @return 是否可以查看该工作.
 * @throws Exception 
 	*/
   public Boolean Flow_IsCanView(String flowNo, long workid, String userNo) throws Exception;
   
   /** 
	 是否可以查看该流程	 
	 @param flowNo 要查询的 sql
	 @param workid 用户密码
	 @return 是否可以查看该工作.
 * @throws Exception 
	*/
   public Boolean Flow_IsCanDoCurrentWork(long workid, String userNo) throws Exception;
    
   
}
