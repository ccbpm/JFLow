package WebService;

import java.util.Hashtable;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 外部接口
 * @author Administrator
 *
 */
@WebService
public interface LocalWSI {
	/**
	 * 待办
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 */
	@WebMethod
	public String DB_Todolist(String userNo, String sysNo);
	
	/**
	 * 获得在途
	 * @param userNo 用户编号
	 * @param sysNo 系统编号,为空时返回平台所有数据
	 * @return
	 * @throws Exception 
	 */
	@WebMethod
	public String DB_Runing(String userNo, String sysNo) throws Exception;
	
	/**
	 * 我可以发起的流程
	 * @param userNo 用户编号
	 * @param sysNo  系统编号,为空时返回平台所有数据
	 * @return 返回我可以发起的流程列表.
	 * @throws Exception 
	 */
	@WebMethod
	public String DB_StarFlows(String userNo, String sysNo) throws Exception;
	
	/**
	 * 我发起的流程实例
	 * @param userNo 用户编号
	 * @param sysNo 统编号,为空时返回平台所有数据
	 * @param pageSize
	 * @param pageIdx
	 * @return
	 */
	@WebMethod
	public String DB_MyStartFlowInstance(String userNo, String sysNo, int pageSize, int pageIdx);
	/**
	 * 创建WorkID
	 * @param flowNo 流程编号
	 * @param userNo 工作人员编号
	 * @return 一个长整型的工作流程实例
	 * @throws Exception 
	 */
	@WebMethod
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
	@WebMethod
	public String SendWork(String flowNo, long workid, Hashtable ht, int toNodeID, String toEmps) throws Exception;
	
	/**
	 * 保存参数
	 * @param workid 工作ID
	 * @param paras 用于控制流程运转的参数，比如方向条件. 格式为:@JinE=1000@QingJaiTianShu=100
	 * @throws Exception 
	 */
	@WebMethod
	public void SaveParas(long workid, String paras) throws Exception;
	
	/**
	 * 获得下一个节点信息
	 * @param flowNo 流程编号
	 * @param workid 流程实例
	 * @param paras 方向条件所需要的参数，可以为空。
	 * @return 下一个节点的JSON.
	 * @throws Exception 
	 */
	@WebMethod
	public String GenerNextStepNode(String flowNo, long workid, String paras) throws Exception;
	
	/**
	 * 获得下一步节点的接收人
	 * @param flowNo 流程ID
	 * @param toNodeID 节点ID
	 * @param workid 工作事例ID
	 * @return 返回两个结果集一个是分组的Depts(No,Name)，另外一个是人员的Emps(No, Name, FK_Dept),接受后，用于构造人员选择器.
	 * @throws Exception 
	 */
	@WebMethod
	public String GenerNextStepNodeEmps(String flowNo, int toNodeID, int workid) throws Exception;
	
	/**
	 * 将要达到的节点
	 * @param currNodeID 当前节点ID
	 * @return 返回节点集合的json.
	 * @throws Exception 
	 */
	@WebMethod
	public String WillToNodes(int currNodeID) throws Exception;
	
	/**
	 * 获得当前节点信息.
	 * @param currNodeID  当前节点ID
	 * @return
	 * @throws Exception 
	 */
	@WebMethod
	public String CurrNodeInfo(int currNodeID) throws Exception;
	
	/**
	 * 获得当前流程信息.
	 * @param flowNo 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */
	@WebMethod
	public String CurrFlowInfo(String flowNo) throws Exception;
	
	/**
	 * 获得当前流程信息.
	 * @param workID 流程ID
	 * @return 当前节点信息
	 * @throws Exception 
	 */
	@WebMethod
	public String CurrGenerWorkFlowInfo(long workID) throws Exception;
}