package WebServiceImp;

import bp.tools.HttpClientUtil;
import bp.tools.Json;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

public class Test {
	//服务的地址
	static String host="http://localhost:8085/API";
	static String webPath="";
	//秘钥：用于双方系统登录的通讯, 只有在用户登录的时候用到.
	static String privateKey= "DiGuaDiGua,IamCCBPM";
	public static void main(String[] args) throws Exception {
		String flowNo ="001";
		try {
			//1.执行登录:
			String UserNo="admin";
			JSONObject postData = Port_Login( UserNo);
			if (postData.toString().startsWith("err@"))
				System.out.println("登录失败");
			String Token = postData.get("Token").toString(); //通过token执行相关的操作.

			/*列表Start*/
			//发起列表
			String startDb = DB_Start(Token,null);
			//待办列表
			Integer nodeID=0;//要取得指定节点的待办就传入,默认为0
			String todoDb = DB_Todolist(Token,flowNo, String.valueOf(nodeID),null);
			//在途
			String todoRuning = DB_Runing(Token,null);
			/*列表End*/

			/*测试流程Start*/
			//2. 测试流程: 创建workid。
			long WorkID=Node_CreateBlankWorkID(Token, flowNo);

			//执行设置流程标题
			String Title ="关于深化中国中亚合作 八点建议指明方向的申请";
			String msg = Flow_SetTitle(Token, String.valueOf(WorkID), Title, flowNo);
			//执行保存草稿.
			String msgDra = Node_SetDraft(Token,flowNo,String.valueOf(WorkID));
			//执行保存参数.
			String Paras="Tel=18660153393@Addr=山东济南@Age=35";
			String msgPra =  Node_SaveParas( Token,String.valueOf(WorkID), Paras);
			//执行发送. 102
			String ToNodeID ="102";//0时系统自动计算要发送到的节点ID
			String ToEmps ="zhanghaicheng";//可不传，不传时流程要设定好接收人，系统可自动寻找对应的接收人
			String msgSend =  Node_SendWork(Token, String.valueOf(WorkID),flowNo, ToNodeID, ToEmps);
			//执行发送.到节点103
			String ToNodeID3 ="103";//0时系统自动计算要发送到的节点ID
			String ToEmps3 ="zhoupeng";//可不传，不传时流程要设定好接收人，系统可自动寻找对应的接收人
			String msgSend3 =  Node_SendWork(Token, String.valueOf(WorkID),flowNo, ToNodeID3, ToEmps3);
			//执行退回到 102
			Integer FK_Node=103;
			Integer ReturnToNodeID =102;////为空时系统自动计算上一个节点
			String ReturnToEmp="zhanghaicheng";//为空时系统自动计算上一个节点的处理人
			String Msg="请补充说明情况";
			boolean IsBackToThisNode =false;//是否按原路返回
			String msgReturn =  Node_ReturnWork(Token, String.valueOf(WorkID), String.valueOf(FK_Node),String.valueOf(ReturnToNodeID), ReturnToEmp, Msg, IsBackToThisNode);

			//执行流程结束.
			String WorkIDs="142,153";
			String msgOver = Flow_DoFlowOver(Token, WorkIDs);
			/*测试流程End*/

			/*组织结构维护Start*/
			//人员信息保存
			String kvs="@Tel=18660153303@Email=123@qq.com";
			String userNo="cheche";
			String userName="管理员";
			String orgNo="";//非集团模式传空
			String deptNo="1071";
			String stats="08,09";
			Port_Emp_Save(Token, orgNo, userNo, userName, deptNo, kvs, stats);

			//人员删除
			Port_Emp_Delete(Token, orgNo, userNo);

			//集团模式下同步组织以及管理员信息
			String orgNo2="7201";
			String name="北方分公司";
			String adminer="zhanghaicheng";
			String adminerName="张海成";
			String keyVals="@Leaer=zhangsan@Tel=12233333@Idx=1";
			Port_Org_Save(Token, orgNo2,name, adminer, adminerName,  keyVals);

			//保存部门,如果有此数据则修改,无此数据则增加
			String no="10051";
			String nameD="开发部";
			String parentNo="1005";
			String keyValsD="@Leaer=zhangsan@Idx=1";
			Port_Dept_Save(Token,orgNo, no, nameD,  parentNo, keyValsD);
			//删除部门
			Port_Dept_Delete(Token, no);
			//保存岗位, 如果有此数据则修改，无此数据则增加
			String noSta="99";
			String nameSta="客服";
			String keyValsSta="@FK_StationType=3";
			Port_Station_Save(Token,orgNo, noSta, nameSta, keyValsSta);
			//删除岗位
			Port_Station_Delete(Token,noSta);
			/*组织结构维护End*/
	        /*Service service = new Service();
	        Call call = (Call) service.createCall();
	        call.setTargetEndpointAddress(webPath);
	        call.setOperationName(new QName("http://WebServiceImp","SendWork"));// WSDL里面描述的接口名称
	        call.addParameter("flowNo",
	                org.apache.axis.encoding.XMLType.XSD_DATE,
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.addParameter("workid",
	                org.apache.axis.encoding.XMLType.XSD_DATE,
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.addParameter("ht",
	                org.apache.axis.encoding.XMLType.XSD_DATE,
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.addParameter("toNodeID",
	                org.apache.axis.encoding.XMLType.XSD_DATE,
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.addParameter("toEmps",
	                org.apache.axis.encoding.XMLType.XSD_DATE,
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
	        String temp = "admin";
	        Hashtable ht = new Hashtable();
	        ht.put("ZiDuan1", "111");
	        ht.put("ZiDuan2", "222");
	        String result = (String) call.invoke(new Object[] { "226",(long)798,ht,22602,"zhangyifan" });
	        // 给方法传递参数，并且调用方法
	        System.out.println("result is " + result);  */
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public static JSONObject Port_Login(String UserNo)
	{
		Map<String, String> param = new HashMap<String, String>();
		param.put("PrivateKey",privateKey);
		param.put("UserNo",UserNo);
		webPath=host+"/Port_Login_Submit";
		String postData = HttpClientUtil.doPost(webPath,param,null,null);
		JSONObject j = JSONObject.fromObject(postData);

		//获取返回的数据
		/*String Name = j.get("Name").toString();
		String No = j.get("No").toString();
		String FK_Dept = j.get("FK_Dept").toString();
		String FK_DeptName = j.get("FK_DeptName").toString();
		String Token = j.get("Token").toString(); //通过token执行相关的操作.
		System.out.println("***"+j.toString()+"***");*/
		return j;
	}

	public static  long Node_CreateBlankWorkID(String Token, String FlowNo)
	{
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("FlowNo",FlowNo);
		webPath=host+"/Node_CreateBlankWorkID";
		String workid = HttpClientUtil.doPost(webPath,param,null,null);
		return Long.valueOf(workid);
	}
	public static String Flow_SetTitle(String Token, String WorkID, String Title, String flowNo){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("WorkID",WorkID);
		param.put("Title",Title);
		param.put("flowNo",flowNo);
		webPath=host+"/Flow_SetTitle";
		String msg = HttpClientUtil.doPost(webPath,param,null,null);
		return msg;
	}

	public static String Node_SetDraft(String Token,String flowNo,String WorkID){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("WorkID",WorkID);
		param.put("flowNo",flowNo);
		webPath=host+"/Node_SetDraft";
		String msg = HttpClientUtil.doPost(webPath,param,null,null);
		return msg;
	}

	public static String Node_SaveParas(String Token,String WorkID,String Paras){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("WorkID",WorkID);
		param.put("Paras",Paras);
		webPath=host+"/Node_SaveParas";
		String msg = HttpClientUtil.doPost(webPath,param,null,null);
		return msg;
	}

	public static  String Node_SendWork(String Token,String WorkID,String FK_Flow,String ToNodeID,String ToEmps)
	{
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("WorkID",WorkID);
		param.put("FK_Flow",FK_Flow);
		param.put("ToNodeID",ToNodeID);
		param.put("ToEmps",ToEmps);
		webPath=host+"/Node_SendWork";
		String msg = HttpClientUtil.doPost(webPath,param,null,null);
		return msg;
	}

	public static String Node_ReturnWork(String Token,String WorkID, String FK_Node,String ReturnToNodeID,String ReturnToEmp,String Msg,boolean IsBackToThisNode){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("WorkID",WorkID);
		param.put("FK_Node",FK_Node);
		param.put("ReturnToNodeID", ReturnToNodeID);
		param.put("ReturnToEmp",ReturnToEmp);
		param.put("Msg",Msg);
		param.put("IsBackToThisNode", String.valueOf(IsBackToThisNode));
		webPath=host+"/Node_ReturnWork";
		String msg = HttpClientUtil.doPost(webPath,param,null,null);
		return msg;
	}
	public static String Flow_DoFlowOver(String Token,String WorkIDs){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("WorkIDs",WorkIDs);
		webPath=host+"/Flow_DoFlowOver";
		String msg = HttpClientUtil.doPost(webPath,param,null,null);
		return msg;
	}
	public static String DB_Start(String Token,String Domain){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("Domain",Domain);
		webPath=host+"/DB_Start";
		String dtStrat = HttpClientUtil.doPost(webPath,param,null,null);
		return dtStrat;
	}
	public static String DB_Todolist(String Token,String flowNo,String nodeID,String Domain){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("flowNo",flowNo);
		param.put("nodeID",nodeID);
		param.put("Domain",Domain);
		webPath=host+"/DB_Todolist";
		String dtTodo = HttpClientUtil.doPost(webPath,param,null,null);
		return dtTodo;
	}
	public static String DB_Runing(String Token,String Domain){
		Map<String, String> param = new HashMap<String, String>();
		param.put("Token",Token);
		param.put("Domain",Domain);
		webPath=host+"/DB_Runing";
		String dtRun = HttpClientUtil.doPost(webPath,param,null,null);
		return dtRun;
	}
	public static String Port_Emp_Save(String token, String orgNo, String userNo, String userName, String deptNo, String kvs, String stats){
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("orgNo",orgNo);
		param.put("userNo",userNo);
		param.put("userName",userName);
		param.put("deptNo",deptNo);
		param.put("kvs",kvs);
		param.put("stats",stats);
		webPath=host+"/Port_Emp_Save";
		String dtSave = HttpClientUtil.doPost(webPath,param,null,null);
		return dtSave;
	}
	public static String Port_Emp_Delete(String token,String orgNo, String userNo){
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("orgNo",orgNo);
		param.put("userNo",userNo);
		webPath=host+"/Port_Emp_Delete";
		String dtDel = HttpClientUtil.doPost(webPath,param,null,null);
		return dtDel;
	}
	public static String Port_Org_Save(String token,String orgNo,  String name, String adminer, String adminerName, String keyVals){
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("orgNo",orgNo);
		param.put("name",name);
		param.put("adminer",adminer);
		param.put("adminerName",adminerName);
		param.put("keyVals",keyVals);
		webPath=host+"/Port_Org_Save";
		String dtSave = HttpClientUtil.doPost(webPath,param,null,null);
		return dtSave;
	}
	public static String Port_Dept_Save(String token,String orgNo, String no, String name, String parentNo, String keyVals){
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("orgNo",orgNo);
		param.put("no",no);
		param.put("name",name);
		param.put("parentNo",parentNo);
		param.put("keyVals",keyVals);
		webPath=host+"/Port_Dept_Save";
		String dtSave = HttpClientUtil.doPost(webPath,param,null,null);
		return dtSave;
	}
	public static String Port_Dept_Delete(String token,String no){
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("no",no);
		webPath=host+"/Port_Dept_Delete";
		String dtDel = HttpClientUtil.doPost(webPath,param,null,null);
		return dtDel;
	}
	public static String Port_Station_Save(String token,String orgNo, String no, String name, String keyVals) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("orgNo",orgNo);
		param.put("no",no);
		param.put("name",name);
		param.put("keyVals",keyVals);
		webPath=host+"/Port_Station_Save";
		String dtSave = HttpClientUtil.doPost(webPath,param,null,null);
		return dtSave;
	}

	public static String Port_Station_Delete(String token,String no) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("token",token);
		param.put("no",no);
		webPath=host+"/Port_Station_Delete";
		String dtDel = HttpClientUtil.doPost(webPath,param,null,null);
		return dtDel;
	}
}
