package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.Encodes;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.Glo;
import bp.wf.data.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;

import java.util.*;
import java.io.*;

import com.google.gson.JsonObject;
import net.sf.json.*;

/** 
 页面功能实体
*/
public class WF_AppClassic extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_AppClassic() throws Exception {
	}



		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod() throws Exception {
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + ContextHolderUtils.getRequest().getRequestURI());
	}

		///#endregion 执行父类的重写方法.


		///#region xxx 界面 .


		///#endregion xxx 界面方法.

	/** 
	 蓝信登陆
	 
	 @return 
	*/
//	public final String LanXin_Login() throws Exception {
//		String code = GetRequestVal("code");
//
//		if (DataType.IsNullOrEmpty(WebUser.Token) == false)
//		{
//			//刷新token
//			String urlr = "http://xjtyjt.e.lanxin.cn:11180//sns/oauth2/refresh_token?refresh_token=" + WebUser.Token + "&appid=100243&grant_type=refresh_token";
//			String resultr = HttpPostConnect(urlr, "");
//			JsonObject jdr = bp.tools.Json.ToJson(resultr);
//			resultr = jdr.getValue("errcode").toString();
//			if (resultr.equals("0"))
//			{
//				WebUser.Token = jdr.getValue("access_token").toString();
//			}
//			return WebUser.getNo();
//		}
//
//
//		//获取Token
//		String url = "http://xjtyjt.e.lanxin.cn:11180/sns/oauth2/access_token?code=" + code + "&appid=100243&grant_type=authorization_code";
//		String result = HttpPostConnect(url, "");
//		JsonData jd = JsonMapper.ToObject(result);
//		result = jd.get("errcode").toString();
//		if (!result.equals("0"))
//		{
//			return "err@" + jd.get("errmsg").toString();
//		}
//		String access_token = jd.get("access_token").toString();
//		String openId = jd.get("openid").toString();
//
//		//获取用户信息
//		url = "http://xjtyjt.e.lanxin.cn:11180/sns/userinfo?access_token=" + access_token + "&mobile=" + openId;
//		result = HttpPostConnect(url, "");
//		jd = JsonMapper.ToObject(result);
//		result = jd.get("errcode").toString();
//		if (!result.equals("0"))
//		{
//			return "err@" + jd.get("errmsg").toString();
//		}
//		String userNo = jd.get("openOrgMemberList").get(0).getValue("serialNumber").toString();
//		String tel = jd.get("openOrgMemberList").get(0).getValue("mobile").toString();
//
//		/**单点登陆*/
//		Paras ps = new Paras();
//		ps.SQL = "SELECT No FROM Port_Emp WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No and Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
//		ps.Add("No", userNo, false);
//		ps.Add("Tel", tel, false);
//		String No = DBAccess.RunSQLReturnString(ps);
//		if (DataType.IsNullOrEmpty(No))
//		{
//			return "err@用户信息不正确，请联系管理员";
//		}
//
//		Dev2Interface.Port_Login(userNo);
//		WebUser.Token = access_token;
//		result = jd.get("errcode").toString();
//		return userNo;
//	}
	/** 
	 httppost方式发送数据
	 
	 param url 要提交的url
	 param postDataStr
	 param timeOut 超时时间
	 param encode text code.
	 @return 成功：返回读取内容；失败：0
	*/
//	public static String HttpPostConnect(String serverUrl, String postData)
//	{
//		var dataArray = postData.getBytes(java.nio.charset.StandardCharsets.UTF_8);
//		//创建请求
//		var request = (HttpWebRequest)HttpWebRequest.Create(serverUrl);
//		request.Method = "POST";
//		request.ContentLength = dataArray.Length;
//		//设置上传服务的数据格式  设置之后不好使
//		//request.ContentType = "application/json";
//		//请求的身份验证信息为默认
//		request.Credentials = CredentialCache.DefaultCredentials;
//		//请求超时时间
//		request.Timeout = 10000;
//		//创建输入流
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
//		Stream dataStream;
//		try
//		{
//			dataStream = request.GetRequestStream();
//		}
//		catch (RuntimeException e)
//		{
//			return "0"; //连接服务器失败
//		}
//		//发送请求
//		dataStream.Write(dataArray, 0, dataArray.Length);
//		dataStream.Close();
//		//读取返回消息
//		String res;
//		try
//		{
//			var response = (HttpWebResponse)request.GetResponse();
//
//			var reader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//			res = reader.ReadToEnd();
//			reader.close();
//		}
//		catch (RuntimeException ex)
//		{
//
//			return "0"; //连接服务器失败
//		}
//		return res;
//	}
	/** 
	 初始化Home
	 
	 @return 
	*/
	public final String Home_Init() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		//系统名称.
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("CustomerName", SystemConfig.getCustomerName());

		ht.put("Todolist_EmpWorks", Dev2Interface.getTodolistEmpWorks());
		ht.put("Todolist_Runing", Dev2Interface.getTodolistRuning());
		ht.put("Todolist_Sharing", Dev2Interface.getTodolistSharing());
		ht.put("Todolist_CCWorks", Dev2Interface.getTodolistCCWorks());
		ht.put("Todolist_Apply", Dev2Interface.getTodolistApply()); //申请下来的任务个数.
		ht.put("Todolist_Draft", Dev2Interface.getTodolistDraft()); //草稿数量.
		ht.put("Todolist_Complete", Dev2Interface.getTodolistComplete()); //完成数量.
		ht.put("UserDeptName", WebUser.getFK_DeptName());

		//我发起
		MyStartFlows myStartFlows = new MyStartFlows();
		QueryObject obj = new QueryObject(myStartFlows);
		obj.AddWhere(MyStartFlowAttr.Starter, WebUser.getNo());
		obj.addAnd();
		//运行中\已完成\挂起\退回\转发\加签\批处理\
		obj.addLeftBracket();
		obj.AddWhere("WFState=2 or WFState=3 or WFState=4 or WFState=5 or WFState=6 or WFState=8 or WFState=10");
		obj.addRightBracket();
		obj.DoQuery();
		ht.put("Todolist_MyStartFlow", myStartFlows.size());

		//我参与
		MyJoinFlows myFlows = new MyJoinFlows();
		obj = new QueryObject(myFlows);
		obj.AddWhere("Emps like '%" + WebUser.getNo() + "%'");
		obj.DoQuery();
		ht.put("Todolist_MyFlow", myFlows.size());

		return bp.tools.Json.ToJsonEntityModel(ht);
	}
	public final String Index_Init() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("Todolist_Runing", Dev2Interface.getTodolistRuning()); //运行中.
		ht.put("Todolist_EmpWorks", Dev2Interface.getTodolistEmpWorks()); //待办
		ht.put("Todolist_CCWorks", Dev2Interface.getTodolistCCWorks()); //抄送.

		//本周.
		ht.put("TodayNum", Dev2Interface.getTodolistCCWorks()); //抄送.

		return bp.tools.Json.ToJsonEntityModel(ht);
	}


		///#region 登录界面.
	public final String Portal_Login() throws Exception {
		String userNo = this.GetRequestVal("UserNo");

		try
		{
			Emp emp = new Emp(userNo);

			Dev2Interface.Port_Login(emp.getUserID());
			return ".";
		}
		catch (RuntimeException ex)
		{
			return "err@用户[" + userNo + "]登录失败." + ex.getMessage();
		}

	}
	/** 
	 登录.
	 
	 @return 
	*/
	public final String Login_Submit() throws Exception {
		try
		{
			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}
			if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true) {
				pass = Encodes.decodeBase64String(pass);
			}
			Emp emp = new Emp();
			emp.setUserID (userNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*如果包含昵称列,就检查昵称是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo, false);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else if (DBAccess.IsExitsTableCol("Port_Emp", "Tel") == true)
				{
					/*如果包含Name列,就检查Name是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
					ps.Add("Tel", userNo, false);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else
				{
					return "err@用户名或者密码错误.";
				}
			}

			if (emp.CheckPass(pass) == false)
			{
				return "err@用户名或者密码错误.";
			}

			//调用登录方法.
			Dev2Interface.Port_GenerToken(emp.getUserID());
			Dev2Interface.Port_Login(emp.getUserID());


			return "登陆成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}


	/** 
	 执行登录
	 
	 @return 
	*/
	public final String Login_Init() throws Exception {
		String doType = GetRequestVal("LoginType");
		if (DataType.IsNullOrEmpty(doType) == false && doType.equals("Out") == true)
		{
			//清空cookie
			WebUser.Exit();
		}


		if (this.getDoWhat() != null && this.getDoWhat().equals("Login") == true)
		{
			//调用登录方法.
			Dev2Interface.Port_Login(this.getUserNo(), this.getSID());
			return "url@Home.htm?UserNo=" + this.getUserNo();
			//this.Login_Submit();
			//return;
		}

		Hashtable ht = new Hashtable();
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("ServiceTel", SystemConfig.getServiceTel());
		ht.put("CustomerName", SystemConfig.getCustomerName());
		if (WebUser.getNoOfRel() == null)
		{
			ht.put("UserNo", "");
			ht.put("UserName", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());

			String name = WebUser.getName();

			if (DataType.IsNullOrEmpty(name) == true)
			{
				ht.put("UserName", WebUser.getNo());
			}
			else
			{
				ht.put("UserName", name);
			}
		}

		return bp.tools.Json.ToJsonEntityModel(ht);
	}

		///#endregion 登录界面.


		///#region Welcome.htm 欢迎页面.
	public final String Welcome_Init() throws Exception {

		Hashtable ht = new Hashtable();
		// 待办.
		ht.put("Todlist", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) as Num FROM WF_GenerWorkerList WHERE IsPass=0 AND FK_Emp='" + WebUser.getNo() + "'")); //流程数

		//发起.
		ht.put("Start", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState > 1  AND Starter='" + WebUser.getNo() + "'")); //实例数.

		//逾期.
		ht.put("OverTime", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=3 AND WFSta=1 ")); //实例数.

		//退回数
		ht.put("ReturnNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState = 5  AND TodoEmps LIKE '%" + WebUser.getNo() + ",%'"));

		//草稿.
		ht.put("Darft", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=1  AND Starter='" + WebUser.getNo() + "'"));

		//运行中.
		ht.put("Runing", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) as Num FROM WF_GenerWorkerList WHERE IsPass!=0 AND FK_Emp='" + WebUser.getNo() + "'"));

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 流程监控折线图数据获取
	 
	 @return 
	*/
	public final String Welcome_EchartDataSet() throws Exception {
		DataSet ds = new DataSet();

		String whereStr = "";
		String whereStrPuls = "";

		if (Glo.getCCBPMRunModel() != CCBPMRunModel.GroupInc)
		{
			whereStr += " WHERE OrgNo = '" + WebUser.getOrgNo() + "'";
			whereStrPuls += " AND OrgNo = '" + WebUser.getOrgNo() + "'";
		}


			///#region 完成的流程-按月分析
		//按期完成
		String sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND SendDT<=SDTOfNode And WFSta=1 " + whereStrPuls + " GROUP BY FK_NY ";
		DataTable ComplateFlowsByNY = DBAccess.RunSQLReturnTable(sql);
		ComplateFlowsByNY.TableName = "ComplateFlowsByNY";
		ds.Tables.add(ComplateFlowsByNY);

		//逾期完成
		sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND SendDT>SDTOfNode And WFSta=1 " + whereStrPuls + " GROUP BY FK_NY ";
		DataTable OverComplateFlowsByNY = DBAccess.RunSQLReturnTable(sql);
		OverComplateFlowsByNY.TableName = "OverComplateFlowsByNY";
		ds.Tables.add(OverComplateFlowsByNY);

			///#endregion 完成的流程-按月分析


			///#region 运行中的流程
		//按部门
		//1.全部待办
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B WHERE A.FK_Dept=B.No GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B WHERE A.FK_Dept=B.No AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodoListAllByDept = DBAccess.RunSQLReturnTable(sql);
		TodoListAllByDept.TableName = "TodoListAllByDept";
		ds.Tables.add(TodoListAllByDept);

		//2.退回的数据
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID AND C.WFState=5 GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID AND C.WFState=5 AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodoListReturnByDept = DBAccess.RunSQLReturnTable(sql);
		TodoListReturnByDept.TableName = "TodoListReturnByDept";
		ds.Tables.add(TodoListReturnByDept);

		//3.逾期的数据
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID  and STR_TO_DATE(A.SDT,'%Y-%m-%d %H:%i') <STR_TO_DATE(C.SDTOfNode,'%Y-%m-%d %H:%i') GROUP BY B.Name";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID  and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(TO_DATE(C.SDTOfNode, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE(A.SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY B.Name ";
			sql += "UNION SELECT  B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (TO_DATE(SDTOfNode, 'yyyy-mm-dd') - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT  B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID  and convert(varchar(100),A.SDT,120) < CONVERT(varchar(100), C.SDTOfNode, 120) GROUP BY B.Name";
		}

		DataTable TodoListOverTByDept = DBAccess.RunSQLReturnTable(sql);
		TodoListOverTByDept.TableName = "TodoListOverTByDept";
		ds.Tables.add(TodoListOverTByDept);

		//4.预警的数据
		//按流程

		//1.全部待办
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_Flow B WHERE A.FK_Flow=B.No GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_Flow B WHERE A.FK_Flow=B.No AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodoListAllByFlow = DBAccess.RunSQLReturnTable(sql);
		TodoListAllByFlow.TableName = "TodoListAllByFlow";
		ds.Tables.add(TodoListAllByFlow);

		//2.退回的数据
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID AND B.WFState=5 GROUP BY B.FlowName";
		}
		else
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID AND B.WFState=5 AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.FlowName";
		}
		DataTable TodoListReturnByFlow = DBAccess.RunSQLReturnTable(sql);
		TodoListReturnByFlow.TableName = "TodoListReturnByFlow";
		ds.Tables.add(TodoListReturnByFlow);

		//3.逾期的数据
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID  and STR_TO_DATE(A.SDT,'%Y-%m-%d %H:%i') <STR_TO_DATE(B.SDTOfNode,'%Y-%m-%d %H:%i') GROUP BY B.FlowName";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID  and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(TO_DATE(B.SDTOfNode, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE(A.SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY B.FlowName ";
			sql += "UNION SELECT  B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (TO_DATE(B.SDTOfNode, 'yyyy-mm-dd') - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY B.FlowName";
		}
		else
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID  and convert(varchar(100),A.SDT,120) < CONVERT(varchar(100), B.SDTOfNode, 120) GROUP BY B.FlowName";
		}

		DataTable TodoListOverTByFlow = DBAccess.RunSQLReturnTable(sql);
		TodoListOverTByFlow.TableName = "TodoListOverTByFlow";
		ds.Tables.add(TodoListOverTByFlow);


		//按人员(仅限一个部门中的人员）
		//获取当前人员所在部门的所有人员
		sql = "SELECT A.No,A.Name From Port_Emp A,Port_DeptEmp B Where A.No=B.FK_Emp AND B.FK_Dept='" + WebUser.getFK_Dept()+ "' order By A.Idx";
		DataTable Emps = DBAccess.RunSQLReturnTable(sql);
		Emps.TableName = "Emps";
		ds.Tables.add(Emps);

		//1.全部待办
		sql = "SELECT FK_EmpText AS Name, count(WorkID) as Num FROM WF_GenerWorkerList WHERE FK_Dept='" + WebUser.getFK_Dept()+ "' GROUP BY FK_EmpText";
		DataTable TodoListAllByEmp = DBAccess.RunSQLReturnTable(sql);
		TodoListAllByEmp.TableName = "TodoListAllByEmp";
		ds.Tables.add(TodoListAllByEmp);

		//2.退回的数据
		sql = "SELECT A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE  A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID AND B.WFState=5 GROUP BY A.FK_EmpText";
		DataTable TodoListReturnByEmp = DBAccess.RunSQLReturnTable(sql);
		TodoListReturnByEmp.TableName = "TodoListReturnByEmp";
		ds.Tables.add(TodoListReturnByEmp);

		//3.逾期的数据
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE  A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID  and STR_TO_DATE(A.SDT,'%Y-%m-%d %H:%i') <STR_TO_DATE(B.SDTOfNode,'%Y-%m-%d %H:%i') GROUP BY A.FK_EmpText";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE  A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID  and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(TO_DATE(B.SDTOfNode, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE(A.SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY A.FK_EmpText ";
			sql += "UNION SELECT A.FK_EmpText AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (TO_DATE(B.SDTOfNode, 'yyyy-mm-dd') - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY A.FK_EmpText";
		}
		else
		{
			sql = "SELECT A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID  and convert(varchar(100),A.SDT,120) < CONVERT(varchar(100), B.SDTOfNode, 120) GROUP BY A.FK_EmpText";
		}

		DataTable TodoListOverTByEmp = DBAccess.RunSQLReturnTable(sql);
		TodoListOverTByEmp.TableName = "TodoListOverTByEmp";
		ds.Tables.add(TodoListOverTByEmp);

			///#endregion 运行中的流程

		return bp.tools.Json.ToJson(ds);
	}

		///#endregion



		///#region 流程监控
	/** 
	 流程监控的基础数据
	 
	 @return 
	*/
	public final String Watchdog_Init() throws Exception {
		String whereStr = "";
		String whereStrPuls = "";


		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			whereStr += " WHERE OrgNo = '" + WebUser.getOrgNo() + "'";
			whereStrPuls += " AND OrgNo = '" + WebUser.getOrgNo() + "'";

		}

		Hashtable ht = new Hashtable();
		ht.put("FlowNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM WF_Flow " + whereStr)); //流程数

		//所有的实例数量.
		ht.put("FlowInstaceNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState >1 " + whereStrPuls)); //实例数.

		//所有的已完成数量.
		ht.put("FlowComplete", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=3 AND WFSta=1 " + whereStrPuls)); //实例数.

		//所有的待办数量.
		ht.put("TodolistNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=2 " + whereStrPuls));

		//退回数.
		ht.put("ReturnNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=5 " + whereStrPuls));

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 流程监控折线图数据获取
	 
	 @return 
	*/
	public final String Watchdog_EchartDataSet() throws Exception {
		DataSet ds = new DataSet();

		String whereStr = "";
		String whereStrPuls = "";

		if (Glo.getCCBPMRunModel() != CCBPMRunModel.GroupInc)
		{
			whereStr += " WHERE OrgNo = '" + WebUser.getOrgNo() + "'";
			whereStrPuls += " AND OrgNo = '" + WebUser.getOrgNo() + "'";
		}


			///#region 完成的流程-按月分析
		//按期完成
		String sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND SendDT<=SDTOfNode And WFSta=1 " + whereStrPuls + " GROUP BY FK_NY ";
		DataTable ComplateFlowsByNY = DBAccess.RunSQLReturnTable(sql);
		ComplateFlowsByNY.TableName = "ComplateFlowsByNY";
		ds.Tables.add(ComplateFlowsByNY);

		//逾期完成
		sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND SendDT>SDTOfNode And WFSta=1 " + whereStrPuls + " GROUP BY FK_NY ";
		DataTable OverComplateFlowsByNY = DBAccess.RunSQLReturnTable(sql);
		OverComplateFlowsByNY.TableName = "OverComplateFlowsByNY";
		ds.Tables.add(OverComplateFlowsByNY);

			///#endregion 完成的流程-按月分析


			///#region 运行中的流程
		//按部门
		//1.全部待办
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B WHERE A.FK_Dept=B.No GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B WHERE A.FK_Dept=B.No AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodoListAllByDept = DBAccess.RunSQLReturnTable(sql);
		TodoListAllByDept.TableName = "TodoListAllByDept";
		ds.Tables.add(TodoListAllByDept);

		//2.退回的数据
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID AND C.WFState=5 GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID AND C.WFState=5 AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodoListReturnByDept = DBAccess.RunSQLReturnTable(sql);
		TodoListReturnByDept.TableName = "TodoListReturnByDept";
		ds.Tables.add(TodoListReturnByDept);

		//3.逾期的数据
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID  and STR_TO_DATE(A.SDT,'%Y-%m-%d %H:%i') <STR_TO_DATE(C.SDTOfNode,'%Y-%m-%d %H:%i') GROUP BY B.Name";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID  and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(TO_DATE(C.SDTOfNode, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE(A.SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY B.Name ";
			sql += "UNION SELECT  B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (TO_DATE(SDTOfNode, 'yyyy-mm-dd') - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT  B.Name, count(DISTINCT C.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B,WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID  and convert(varchar(100),A.SDT,120) < CONVERT(varchar(100), C.SDTOfNode, 120) GROUP BY B.Name";
		}

		DataTable TodoListOverTByDept = DBAccess.RunSQLReturnTable(sql);
		TodoListOverTByDept.TableName = "TodoListOverTByDept";
		ds.Tables.add(TodoListOverTByDept);

		//4.预警的数据
		//按流程

		//1.全部待办
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_Flow B WHERE A.FK_Flow=B.No GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_Flow B WHERE A.FK_Flow=B.No AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodoListAllByFlow = DBAccess.RunSQLReturnTable(sql);
		TodoListAllByFlow.TableName = "TodoListAllByFlow";
		ds.Tables.add(TodoListAllByFlow);

		//2.退回的数据
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID AND B.WFState=5 GROUP BY B.FlowName";
		}
		else
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID AND B.WFState=5 AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.FlowName";
		}
		DataTable TodoListReturnByFlow = DBAccess.RunSQLReturnTable(sql);
		TodoListReturnByFlow.TableName = "TodoListReturnByFlow";
		ds.Tables.add(TodoListReturnByFlow);

		//3.逾期的数据
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID  and STR_TO_DATE(A.SDT,'%Y-%m-%d %H:%i') <STR_TO_DATE(B.SDTOfNode,'%Y-%m-%d %H:%i') GROUP BY B.FlowName";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID  and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(TO_DATE(B.SDTOfNode, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE(A.SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY B.FlowName ";
			sql += "UNION SELECT  B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (TO_DATE(B.SDTOfNode, 'yyyy-mm-dd') - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY B.FlowName";
		}
		else
		{
			sql = "SELECT B.FlowName AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Flow=B.FK_Flow AND A.WorkID=B.WorkID  and convert(varchar(100),A.SDT,120) < CONVERT(varchar(100), B.SDTOfNode, 120) GROUP BY B.FlowName";
		}

		DataTable TodoListOverTByFlow = DBAccess.RunSQLReturnTable(sql);
		TodoListOverTByFlow.TableName = "TodoListOverTByFlow";
		ds.Tables.add(TodoListOverTByFlow);


		//按人员(仅限一个部门中的人员）
		//获取当前人员所在部门的所有人员
		sql = "SELECT A.No,A.Name From Port_Emp A,Port_DeptEmp B Where A.No=B.FK_Emp AND B.FK_Dept='" + WebUser.getFK_Dept()+ "' order By A.Idx";
		DataTable Emps = DBAccess.RunSQLReturnTable(sql);
		Emps.TableName = "Emps";
		ds.Tables.add(Emps);

		//1.全部待办
		sql = "SELECT FK_EmpText AS Name, count(WorkID) as Num FROM WF_GenerWorkerList WHERE FK_Dept='" + WebUser.getFK_Dept()+ "' GROUP BY FK_EmpText";
		DataTable TodoListAllByEmp = DBAccess.RunSQLReturnTable(sql);
		TodoListAllByEmp.TableName = "TodoListAllByEmp";
		ds.Tables.add(TodoListAllByEmp);

		//2.退回的数据
		sql = "SELECT A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE  A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID AND B.WFState=5 GROUP BY A.FK_EmpText";
		DataTable TodoListReturnByEmp = DBAccess.RunSQLReturnTable(sql);
		TodoListReturnByEmp.TableName = "TodoListReturnByEmp";
		ds.Tables.add(TodoListReturnByEmp);

		//3.逾期的数据
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE  A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID  and STR_TO_DATE(A.SDT,'%Y-%m-%d %H:%i') <STR_TO_DATE(B.SDTOfNode,'%Y-%m-%d %H:%i') GROUP BY A.FK_EmpText";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE  A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID  and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(TO_DATE(B.SDTOfNode, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE(A.SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY A.FK_EmpText ";
			sql += "UNION SELECT A.FK_EmpText AS Name, count(DISTINCT A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (TO_DATE(B.SDTOfNode, 'yyyy-mm-dd') - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY A.FK_EmpText";
		}
		else
		{
			sql = "SELECT A.FK_EmpText AS Name, count( A.WorkID) as Num FROM WF_GenerWorkerList A,WF_GenerWorkFlow B WHERE A.FK_Dept='" + WebUser.getFK_Dept()+ "' AND A.WorkID=B.WorkID  and convert(varchar(100),A.SDT,120) < CONVERT(varchar(100), B.SDTOfNode, 120) GROUP BY A.FK_EmpText";
		}

		DataTable TodoListOverTByEmp = DBAccess.RunSQLReturnTable(sql);
		TodoListOverTByEmp.TableName = "TodoListOverTByEmp";
		ds.Tables.add(TodoListOverTByEmp);


			///#endregion 运行中的流程


		return bp.tools.Json.ToJson(ds);
	}

		///#endregion 流程监控

}