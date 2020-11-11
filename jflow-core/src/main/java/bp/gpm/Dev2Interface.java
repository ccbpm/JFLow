package bp.gpm;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.web.*;
import bp.wf.GenerWorkFlow;
import bp.wf.WFState;
import bp.wf.data.Monitor;
import bp.wf.data.MonitorAttr;
import bp.wf.data.Monitors;
//import bp.gpm.weixin.*;
//import bp.gpm.dtalk.dingtalk.*;
//import bp.gpm.dtalk.*;
import java.util.*;

/** 
 权限调用API
*/
public class Dev2Interface
{

		///菜单权限

		/// 菜单权限


		///登陆接口
	/** 
	 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 
	 @param userNo 用户名
	 @param SID 安全ID,请参考流程设计器操作手册
	 * @throws Exception 
	*/
	public static void Port_Login(String userNo, String sid) throws Exception
	{
		String sql = "SELECT SID FROM Port_Emp WHERE No='" + userNo + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("用户不存在或者SID错误。");
		}

		if (!dt.Rows.get(0).getValue("SID").toString().equals(sid))
		{
			throw new RuntimeException("用户不存在或者SID错误。");
		}

		bp.port.Emp emp = new bp.port.Emp(userNo);
		WebUser.SignInOfGener(emp);
		return;
	}
	/** 
	 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 
	 @param userNo 用户名
	 * @throws Exception 
	*/
	public static void Port_Login(String userNo) throws Exception
	{
		bp.port.Emp emp = new bp.port.Emp(userNo);
		WebUser.SignInOfGener(emp);
		return;
	}
	/** 
	 注销当前登录
	*/
	public static void Port_SigOut()
	{
		WebUser.Exit();
	}
	/** 
	 获取未读的消息
	 用于消息提醒.
	 
	 @param userNo 用户ID
	*/
	public static String Port_SMSInfo(String userNo)
	{
		Paras ps = new Paras();
		ps.SQL="SELECT MyPK, EmailTitle  FROM sys_sms WHERE SendToEmpID=" + SystemConfig.getAppCenterDBVarStr() + "SendToEmpID AND IsAlert=0";
		ps.Add("SendToEmpID", userNo);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += "@" + dr.getValue(0) + "=" + dr.getValue(1).toString();
		}
		ps = new Paras();
		ps.SQL="UPDATE  sys_sms SET IsAlert=1 WHERE  SendToEmpID=" + SystemConfig.getAppCenterDBVarStr() + "SendToEmpID AND IsAlert=0";
		ps.Add("SendToEmpID", userNo);
		DBAccess.RunSQL(ps);
		return strs;
	}

		/// 登陆接口


		///GPM接口
	/** 
	 获取一个操作人员对于一个系统的权限
	 
	 @param userNo 用户编号
	 @param app 系统编号
	 @return 结果集
	*/
	public static DataTable DB_Menus(String userNo, String app)
	{
		Paras ps = new Paras();
		ps.SQL="SELECT * FROM GPM_EmpMenu WHERE FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND FK_App=" + SystemConfig.getAppCenterDBVarStr() + "FK_App ";
		ps.Add("FK_Emp", userNo);
		ps.Add("FK_App", app);
		return DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取一个操作人员对此应用可以访问的系统
	 
	 @param userNo 用户编号
	 @return 结果集
	*/
	public static DataTable DB_Apps(String userNo)
	{
		Paras ps = new Paras();
		ps.SQL="SELECT * FROM GPM_EmpApp WHERE FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp ";
		ps.Add("FK_Emp", userNo);
		return DBAccess.RunSQLReturnTable(ps);
	}

		/// GPM接口

	/** 
	 推送消息到微信
	 
	 @param WorkID WorkID
	 @param sender 发送人
	 * @throws Exception 
	*/
	/*public static MessageErrorModel PushMessageToTelByWeiXin(long WorkID, String sender) throws Exception
	{
		//企业应用必须存在
		String agentId = SystemConfig.getWX_AgentID() != null ? SystemConfig.getWX_AgentID() : null;
		if (agentId != null)
		{
			//获取 AccessToken
			String accessToken = bp.gpm.weixin.WeiXinEntity.getAccessToken();

			//当前业务
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(WorkID);
			gwf.RetrieveFromDBSources();
			//接收人
			Monitors empWorks = new Monitors();
			QueryObject obj = new QueryObject(empWorks);
			obj.AddWhere(MonitorAttr.WorkID, WorkID);
			obj.addOr();
			obj.AddWhere(MonitorAttr.FID, WorkID);
			obj.DoQuery();

			//发送给多人的消息格式   zhangsan|lisi|wangwu
			//此处根据手机号作为推送人的帐号，便于关联
			String toUsers = "";
			for (Monitor empWork : empWorks.ToJavaList())
			{
				if (toUsers.length() > 0)
				{
					toUsers += "|";
				}
				Emp emp = new Emp(empWork.getFK_Emp());
				toUsers += emp.getTel();
			}
			if (toUsers.length() == 0)
			{
				return null;
			}

			//消息样式为图文连接
			News_Articles newArticle = new News_Articles();
			//设置消息标题
			newArticle.setTitle("待办事项：" + gwf.getTitle());

			//设置消息内容主体
			String msgConten = "业务名称：" + gwf.getFlowName() + "\n";
			msgConten += "申请人：" + gwf.getStarterName() + "\n";
			msgConten += "申请部门：" + gwf.getDeptName() + "\n";
			msgConten += "当前步骤：" + gwf.getNodeName() + "\n";
			msgConten += "上一步处理人：" + sender + "\n";
			newArticle.setDescription(msgConten);

			//设置图片连接
			String New_Url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfig.getWX_CorpID() + "&redirect_uri=" + SystemConfig.getWX_MessageUrl() + "/CCMobile/action.aspx&response_type=code&scope=snsapi_base&state=TodoList#wechat_redirect";
			newArticle.setUrl(New_Url);

			//http://discuz.comli.com/weixin/weather/icon/cartoon.jpg
			newArticle.setPicurl(SystemConfig.getWX_MessageUrl() + "/DataUser/ICON/CCBPM.png");

			//加入消息
			WX_Msg_News wxMsg = new WX_Msg_News();
			wxMsg.setAccessToken(accessToken);
			wxMsg.setAgentid(SystemConfig.getWX_AgentID());
			wxMsg.setTouser(toUsers);
			wxMsg.getArticles().add(newArticle);
			//执行发送
			return WeiXinMessage.PostMsgOfNews(wxMsg);
		}
		return null;
	}
	*//**
	 推送消息到钉钉
	 
	 @param msgType 消息类型
	 @param WorkID WorkID
	 @param sender 发送人
	 * @throws Exception 
	*//*
	public static Ding_Post_ReturnVal PushMessageToTelByDingDing(DingMsgType msgType, long WorkID, String sender) throws Exception
	{
		//当前业务
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(WorkID);
		gwf.RetrieveFromDBSources();
		//获取接收人
		Monitors empWorks = new Monitors();
		QueryObject obj = new QueryObject(empWorks);
		obj.AddWhere(MonitorAttr.WorkID, WorkID);
		obj.addOr();
		obj.AddWhere(MonitorAttr.FID, WorkID);
		obj.DoQuery();


		//结束不发送消息
		if (gwf.getWFState() == WFState.Complete)
		{
			return null;
		}

		String toUsers = "";
		for (Monitor empWork : empWorks.ToJavaList())
		{
			if (toUsers.length() > 0)
			{
				toUsers += "|";
			}
			Emp emp = new Emp(empWork.getFK_Emp());
			toUsers += emp.getTel();
		}
		if (toUsers.length() == 0)
		{
			return null;
		}

		switch (msgType)
		{
			//文本类型
			case text:
				Ding_Msg_Text msgText = new Ding_Msg_Text();
				msgText.setAccessToken(DingDing.getAccessToken());
				msgText.setAgentid(SystemConfig.getDing_AgentID());
				msgText.setTouser(toUsers);
				msgText.setContent(gwf.getTitle() + "\n发送人：" + sender + "\n时间：" + DataType.getCurrentDataTimeCNOfShort());
				return DingTalk_Message.Msg_AgentText_Send(msgText);
			//连接类型
			case link:
				Ding_Msg_Link msgLink = new Ding_Msg_Link();
				msgLink.setAccessToken(DingDing.getAccessToken());
				msgLink.setTouser(toUsers);
				msgLink.setAgentid(SystemConfig.getDing_AgentID());
				msgLink.setMessageUrl(SystemConfig.getDing_MessageUrl() + "/CCMobile/login.aspx");
				msgLink.setPicUrl("@lALOACZwe2Rk");
				msgLink.setTitle(gwf.getTitle());
				msgLink.setText("发送人：" + sender + "\n时间：" + DataType.getCurrentDataTimeCNOfShort());
				return DingTalk_Message.Msg_AgentLink_Send(msgLink);
			//工作消息类型
			case OA:
				String[] users = toUsers.split("[|]", -1);
				String faildSend = "";
				Ding_Post_ReturnVal postVal = null;
				for (String user : users)
				{
					Ding_Msg_OA msgOA = new Ding_Msg_OA();
					msgOA.setAccessToken(DingDing.getAccessToken());
					msgOA.setAgentid(SystemConfig.getDing_AgentID());
					msgOA.setTouser(user);
					msgOA.setMessageUrl(SystemConfig.getDing_MessageUrl() + "/CCMobile/DingAction.aspx?ActionFrom=message&UserID=" + user + "&ActionType=ToDo&FK_Flow=" + gwf.getFK_Flow() + "&FK_Node=" + gwf.getFK_Node() + "&WorkID=" + WorkID + "&FID=" + gwf.getFID());
					//00是完全透明，ff是完全不透明，比较适中的透明度值是 1e
					msgOA.setHeadBgcolor("FFBBBBBB");
					msgOA.setHeadText("审批");
					msgOA.setBodyTitle(gwf.getTitle());
					Hashtable hs = new Hashtable();
					hs.put("流程名", gwf.getFlowName());
					hs.put("当前节点", gwf.getNodeName());
					hs.put("申请人", gwf.getStarterName());
					hs.put("申请时间", gwf.getRDT());
					msgOA.setBodyForm(hs);
					msgOA.setBodyAuthor(sender);
					postVal = DingTalk_Message.Msg_OAText_Send(msgOA);
					if (!postVal.getErrcode().equals("0"))
					{
						if (faildSend.length() > 0)
						{
							faildSend += ",";
						}
						faildSend += user;
					}
				}
				//有失败消息
				if (faildSend.length() > 0)
				{
					postVal.setErrcode("500");
					postVal.setErrmsg(faildSend + "消息发送失败");
				}
				return postVal;
		}
		return null;
	}*/

}