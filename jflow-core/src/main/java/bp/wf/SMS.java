package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.tools.HttpClientUtil;
import bp.web.*;
import bp.sys.*;

/** 
 消息
*/
public class SMS extends EntityMyPK
{

		///#region 新方法 2013
	/** 
	 发送消息
	 
	 param userNo 接受人
	 param msgTitle 标题
	 param msgDoc 内容
	 param msgFlag 标记
	 param msgType 类型
	 param paras 扩展参数
	*/

	public static void SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag, String msgType, String paras, long workid, String pushModel) throws Exception {
		SendMsg(userNo, msgTitle, msgDoc, msgFlag, msgType, paras, workid, pushModel, null);
	}

	public static void SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag, String msgType, String paras, long workid) throws Exception {
		SendMsg(userNo, msgTitle, msgDoc, msgFlag, msgType, paras, workid, null, null);
	}

//ORIGINAL LINE: public static void SendMsg(string userNo, string msgTitle, string msgDoc, string msgFlag, string msgType, string paras, Int64 workid, string pushModel = null, string openUrl = null)
	public static void SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag, String msgType, String paras, long workid, String pushModel, String openUrl) throws Exception {

		SMS sms = new SMS();
		sms.setMyPK(DBAccess.GenerGUID(0, null, null));
		sms.setHisEmailSta(MsgSta.UnRun);

		sms.setSender(WebUser.getNo());
		sms.setSendToEmpNo(userNo);

		sms.setTitle(msgTitle);
		sms.setDocOfEmail(msgDoc);
		sms.setMobileInfo(msgDoc);

		sms.setSender(WebUser.getNo());
		sms.setRDT(DataType.getCurrentDateTime());

		sms.setMsgFlag(msgFlag); // 消息标志.
		sms.setMsgType(msgType); // 消息类型.'

		sms.setAtPara(paras);

		sms.setWorkID(workid);

		/**如果没有设置模式，就设置邮件.
		*/
		if (pushModel == null)
		{
			pushModel = "Email";
		}

		if (DataType.IsNullOrEmpty(openUrl) == false)
		{
			sms.SetPara("OpenUrl", openUrl);
		}
		if (DataType.IsNullOrEmpty(pushModel) == false)
		{
			sms.SetPara("PushModel", pushModel);
		}

		sms.Insert();
	}

		///#endregion 新方法


		///#region 手机短信属性
	/** 
	 手机号码
	*/
	public final String getMobile() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.Mobile);
	}
	public final void setMobile(String value)throws Exception
	{SetValByKey(SMSAttr.Mobile, value);
	}
	/** 
	 手机状态
	*/
	public final MsgSta getHisMobileSta() throws Exception {
		return MsgSta.forValue(this.GetValIntByKey(SMSAttr.MobileSta));
	}
	public final void setHisMobileSta(MsgSta value)throws Exception
	{SetValByKey(SMSAttr.MobileSta, value.getValue());
	}
	/** 
	 手机信息
	*/
	public final String getMobileInfo() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.MobileInfo);
	}
	public final void setMobileInfo(String value)throws Exception
	{SetValByKey(SMSAttr.MobileInfo, value);
	}

		///#endregion


		///#region  邮件属性
	/** 
	 参数
	*/
	public final String getAtPara() throws Exception
	{
		return this.GetValStrByKey("AtPara", "");
	}
	public final void setAtPara(String value)  throws Exception
	 {
		this.SetValByKey("AtPara", value);
	}
	/** 
	 邮件状态
	*/
	public final MsgSta getHisEmailSta() throws Exception {
		return MsgSta.forValue(this.GetValIntByKey(SMSAttr.EmailSta));
	}
	public final void setHisEmailSta(MsgSta value)  throws Exception
	 {
		this.SetValByKey(SMSAttr.EmailSta, value.getValue());
	}
	/** 
	 Email
	*/
	public final String getEmail() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.Email);
	}
	public final void setEmail(String value)throws Exception
	{SetValByKey(SMSAttr.Email, value);
	}
	/** 
	 发送给
	*/
	public final String getSendToEmpNo() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.SendTo);
	}
	public final void setSendToEmpNo(String value)throws Exception
	{SetValByKey(SMSAttr.SendTo, value);
	}
	public final int isRead() throws Exception
	{
		return this.GetValIntByKey(SMSAttr.IsRead);
	}
	public final void setRead(int value)  throws Exception
	 {
		this.SetValByKey(SMSAttr.IsRead, (int)value);
	}
	public final int isAlert() throws Exception
	{
		return this.GetValIntByKey(SMSAttr.IsAlert);
	}
	public final void setAlert(int value)  throws Exception
	 {
		this.SetValByKey(SMSAttr.IsAlert, (int)value);
	}
	/** 
	 消息标记(可以用它来避免发送重复)
	*/
	public final String getMsgFlag() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.MsgFlag);
	}
	public final void setMsgFlag(String value)throws Exception
	{SetValByKey(SMSAttr.MsgFlag, value);
	}
	/** 
	 类型
	*/
	public final String getMsgType() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.MsgType);
	}
	public final void setMsgType(String value)throws Exception
	{SetValByKey(SMSAttr.MsgType, value);
	}
	/** 
	 工作ID
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(SMSAttr.WorkID);
	}
	public final void setWorkID(long value)throws Exception
	{SetValByKey(SMSAttr.WorkID, value);
	}
	/** 
	 发送人
	*/
	public final String getSender() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.Sender);
	}
	public final void setSender(String value)throws Exception
	{SetValByKey(SMSAttr.Sender, value);
	}
	/** 
	 记录日期
	*/
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.RDT);
	}
	public final void setRDT(String value)  throws Exception
	 {
		this.SetValByKey(SMSAttr.RDT, value);
	}
	/** 
	 标题
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(SMSAttr.EmailTitle);
	}
	public final void setTitle(String value)throws Exception
	{SetValByKey(SMSAttr.EmailTitle, value);
	}
	/** 
	 邮件内容
	*/
	public final String getDocOfEmail() throws Exception {
		String doc = this.GetValStringByKey(SMSAttr.EmailDoc);
		if (DataType.IsNullOrEmpty(doc))
		{
			return this.getTitle();
		}

		return doc.replace('~', '\'');
	}
	public final void setDocOfEmail(String value)throws Exception
	{SetValByKey(SMSAttr.EmailDoc, value);
	}
	/** 
	 邮件内容.
	*/
	public final String getDoc() throws Exception {
		String doc = this.GetValStringByKey(SMSAttr.EmailDoc);
		if (DataType.IsNullOrEmpty(doc))
		{
			return this.getTitle();
		}
		return doc.replace('~', '\'');

		//return this.getDocOfEmail();
	}
	public final void setDoc(String value)throws Exception
	{SetValByKey(SMSAttr.EmailDoc, value);
	}
	/** 
	 打开的连接
	*/
	public final String getOpenURL() throws Exception {
		return this.GetParaString(SMSAttr.OpenUrl);
	}
	public final void setOpenURL(String value)throws Exception
	{this.SetPara(SMSAttr.OpenUrl, value);
	}

		///#endregion


	public final String getPushModel() throws Exception {
		return this.GetParaString(SMSAttr.PushModel);
	}


		///#region 构造函数
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	 消息
	*/
	public SMS()  {
	}
	/** 
	 Map
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SMS", "消息");

		map.AddMyPK(true);

		map.AddTBString(SMSAttr.Sender, null, "发送人(可以为空)", false, true, 0, 200, 20);
		map.AddTBString(SMSAttr.SendTo, null, "发送给(可以为空)", false, true, 0, 200, 20);
		map.AddTBDateTime(SMSAttr.RDT, "写入时间", true, false);

		map.AddTBString(SMSAttr.Mobile, null, "手机号(可以为空)", false, true, 0, 30, 20);
		map.AddTBInt(SMSAttr.MobileSta, MsgSta.UnRun.getValue(), "消息状态", true, true);
		map.AddTBString(SMSAttr.MobileInfo, null, "短信信息", false, true, 0, 1000, 20);

		map.AddTBString(SMSAttr.Email, null, "Email(可以为空)", false, true, 0, 200, 20);
		map.AddTBInt(SMSAttr.EmailSta, MsgSta.UnRun.getValue(), "EmaiSta消息状态", true, true);
		map.AddTBString(SMSAttr.EmailTitle, null, "标题", false, true, 0, 3000, 20);
		map.AddTBStringDoc(SMSAttr.EmailDoc, null, "内容", false, true);
		map.AddTBDateTime(SMSAttr.SendDT, null, "发送时间", false, false);

		map.AddTBInt(SMSAttr.IsRead, 0, "是否读取?", true, true);
		map.AddTBInt(SMSAttr.IsAlert, 0, "是否提示?", true, true);

			//用于获得数据.
		map.AddTBInt(SMSAttr.WorkID, 0, "WorkID", true, true);

			//消息主键.
		map.AddTBString(SMSAttr.MsgFlag, null, "消息标记(用于防止发送重复)", false, true, 0, 200, 20);
		map.AddTBString(SMSAttr.MsgType, null, "消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...)", false, true, 0, 200, 20);

			//其他参数.
		map.AddTBAtParas(500);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 发送邮件
	 
	 param mail
	 param mailTitle
	 param mailDoc
	 @return 
	*/

//	public static boolean SendEmailNowAsync(String mail, String mailTitle, String mailDoc)
//	{
//		try
//		{
//			System.Net.Mail.MailMessage myEmail = new System.Net.Mail.MailMessage();
//
//			//邮件地址.
//			String emailAddr = bp.difference.SystemConfig.GetValByKey("SendEmailAddress", null);
//			if (emailAddr == null)
//			{
//				return false;
//				//emailAddr = "ccbpmtester@tom.com";
//			}
//
//			String emailPassword = bp.difference.SystemConfig.GetValByKey("SendEmailPass", null);
//			if (emailPassword == null)
//			{
//				return false;
//				emailPassword = "ccbpm123";
//			}
//
//			mailDoc = DataType.ParseText2Html(mailDoc);
//
//			String displayName = bp.difference.SystemConfig.GetValByKey("SendEmailDisplayName", "高凌BPM");
//			myEmail.From = new System.Net.Mail.MailAddress(emailAddr, displayName, System.Text.Encoding.UTF8);
//
//			myEmail.To.Add(mail);
//			myEmail.Subject = mailTitle;
//			myEmail.SubjectEncoding = System.Text.Encoding.UTF8; //邮件标题编码
//			myEmail.IsBodyHtml = true;
//			myEmail.Body = mailDoc;
//			myEmail.BodyEncoding = System.Text.Encoding.UTF8; //邮件内容编码
//			myEmail.IsBodyHtml = true; //是否是HTML邮件
//			myEmail.Priority = MailPriority.High; // 邮件优先级
//
//			SmtpClient client = new SmtpClient();
//			client.UseDefaultCredentials = true;
//			if (bp.difference.SystemConfig.GetValByKeyInt("SendEmailEnableSsl", 1) == 1)
//			{
//				client.EnableSsl = true; //经过ssl加密.
//			}
//			else
//			{
//				client.EnableSsl = false;
//			}
//
//			client.Credentials = new System.Net.NetworkCredential(emailAddr, emailPassword);
//			client.Port = bp.difference.SystemConfig.GetValByKeyInt("SendEmailPort", 587); //使用的端口
//			client.Host = bp.difference.SystemConfig.GetValByKey("SendEmailHost", "smtp.gmail.com");
//
//			Object userState = myEmail;
//			//调用自带的异步方法
//
//			client.Send(myEmail);
//			client.SendMailAsync(myEmail);
//			client.SendAsync(myEmail, userState);
//		}
//		catch (RuntimeException e)
//		{
//			Log.DebugWriteError(e.getMessage());
//			return false;
//		}
//
//		return true;
//	}
	/** 
	 SAAS发送.
	*/
	public final void SendMsgToSAAS() throws Exception {
		//获取设置.
		String messageUrl = bp.difference.SystemConfig.getAppSettings().get("HandlerOfMessage").toString();
		if (DataType.IsNullOrEmpty(messageUrl) == true)
		{
			return;
		}

		String httpUrl = messageUrl + "?Sender=" + WebUser.getNo() + "&OrgNo=" + WebUser.getOrgNo() + "&ToUserIDs=" + this.getSendToEmpNo() + "&Title=" + this.getTitle() + "&Docs=" + this.GetValDocText();

		String json = "";
		if (bp.difference.SystemConfig.getCustomerNo().equals("YuTong") == true)
		{
			json = "{";
			json += " \"token\": '34c45c2b30512e8a8e10467cee45d7ed',";
			json += " \"Sender\": \"" + WebUser.getNo() + "\",";
			json += " \"OrgNo\": \"" + WebUser.getOrgNo() + "\",";
			json += " \"userid\": \"" + this.getSendToEmpNo() + "\",";
			json += " \"Tel\": \"" + this.getMobile() + "\",";
			json += " \"title\":\"" + this.getTitle() + "\",";
			json += " \"MsgFlg\":\"" + this.getMsgFlag() + "\",";
			json += " \"MobileInfo\":\"" + this.getMobileInfo() + " \",";
			json += " \"contents\":\"" + this.getDoc() + " \",";
			json += " \"wx\":'true',";
			json += " \"isEmail\":'true',";
			json += " \"url\":\"" + this.getOpenURL() + " \"}";
		}
		else
		{
			json = "{";
			json += " \"Sender\": \"" + WebUser.getNo() + "\",";
			json += " \"OrgNo\": \"" + WebUser.getOrgNo() + "\",";
			json += " \"SendTo\": \"" + this.getSendToEmpNo() + "\",";
			json += " \"Tel\": \"" + this.getMobile() + "\",";
			json += " \"Title\":\"" + this.getTitle() + "\",";
			json += " \"MsgFlg\":\"" + this.getMsgFlag() + "\",";
			json += " \"MobileInfo\":\"" + this.getMobileInfo() + " \",";
			json += " \"Doc\":\"" + this.getDoc() + " \",";
			json += " \"Url\":\"" + this.getOpenURL() + " \"}";
		}


//		//微信
//		if (this.getPushModel().contains("WeiXin") == true)
//		{
//			//注册到url里面去.
//			bp.wf.HttpClientUtil.doPost(httpUrl, json);
//		}

	}
	public final void DealYuTong() throws Exception {
		//获取设置.
		String messageUrl = bp.difference.SystemConfig.getAppSettings().get("HandlerOfMessage").toString();
		if (DataType.IsNullOrEmpty(messageUrl) == true)
		{
			return;
		}

		String httpUrl = messageUrl; //  + "?Sender=" + bp.web.WebUser.getNo() + "&OrgNo=" + WebUser.getOrgNo() + "&ToUserIDs=" + this.SendToEmpNo + "&Title=" + this.Title + "&Docs=" + this.GetValDocText();

		String json = "";

		json = "{";
		json += " \"token\":\"34c45c2b30512e8a8e10467cee45d7ed\",";
	//    json += " \"Sender\": \"" + WebUser.getNo() + "\",";
	 //   json += " \"OrgNo\": \"" + WebUser.getOrgNo() + "\",";
		json += " \"userid\": \"" + this.getSendToEmpNo() + "\",";
	  //  json += " \"Tel\": \"" + this.Mobile + "\",";
		json += " \"title\":\"" + this.getTitle() + "\",";
		json += " \"MsgFlg\":\"" + this.getMsgFlag() + "\",";
	   // json += " \"MobileInfo\":\"" + this.MobileInfo + " \",";

		json += " \"MsgAL\": null,";

		json += " \"contents\":\"" + this.getDoc() + " \",";
		json += " \"wx\":'true',";
		json += " \"isEmail\":'true',";
		json += " \"url\":\"" + this.getOpenURL() + " \"}";

		//注册到url里面去.
		//bp.wf.HttpClientUtil.doPost(httpUrl, json);
		HttpClientUtil.doPostJson(httpUrl,json);
		//bp.wf.HttpClientUtil.doPost
	}
	/** 
	 插入之后执行的方法.
	*/
	@Override
	protected void afterInsert() throws Exception {
		try
		{
			//如果是SAAS模式.
			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{
				SendMsgToSAAS();
				return;
			}

			if (bp.difference.SystemConfig.getCustomerNo().equals("YuTong") == true)
			{
				DealYuTong();
				return;
			}

			if (this.getHisEmailSta() != MsgSta.UnRun)
			{
				return;
			}


				///#region 发送邮件
			if (this.getPushModel().contains("Email") == true && DataType.IsNullOrEmpty(this.getEmail()) == false)
			{
				String emailStrs = this.getEmail();
				emailStrs = emailStrs.replace(",", ";");
				emailStrs = emailStrs.replace("，", ";");

				//包含多个邮箱
//				if (emailStrs.contains(";") == true)
//				{
//					String[] emails = emailStrs.split("[;]", -1);
//					for (String email : emails)
//					{
//						if (DataType.IsNullOrEmpty(email) == true)
//						{
//							continue;
//						}
//						SendEmailNowAsync(email, this.getTitle(), this.getDocOfEmail());
//					}
//				}
//				else
//				{ //单个邮箱
//					SendEmailNowAsync(this.getEmail(), this.getTitle(), this.getDocOfEmail());
//				}
			}

				///#endregion 发送邮件


				///#region 发送短消息 调用接口
			//发送短消息的前提必须是手机号不能为空
			//if (DataType.IsNullOrEmpty(this.Mobile) == true)
			//    return;
			//throw new Exception("发送短消息时接收人的手机号不能为空,否则接受不到消息");

			String messageUrl = bp.difference.SystemConfig.getAppSettings().get("HandlerOfMessage").toString();
			if (DataType.IsNullOrEmpty(messageUrl) == true)
			{
				return;
			}

			String httpUrl = "";

			String json = "{";
			json += " \"sender\": \"" + WebUser.getNo() + "\",";
			json += " \"sendTo\": \"" + this.getSendToEmpNo() + "\",";
			json += " \"tel\": \"" + this.getMobile() + "\",";
			json += " \"title\":\"" + this.getTitle() + "\",";
			json += " \"msgFlag\":\"" + this.getMsgFlag().replace("WKAlt", "") + "\",";
			json += " \"content\":\"" + this.getMobileInfo() + " \",";
			json += " \"openUrl\":\"" + this.getOpenURL() + " \"}";

			//soap = BP.WF.Glo.GetPortalInterfaceSoapClient();
			//站内消息
			if (this.getPushModel().contains("CCMsg") == true)
			{
				httpUrl = messageUrl + "?DoType=SendToCCMSG";
				HttpClientUtil.doPostJson(httpUrl,json);
				//soap.SendToCCMSG(this.MyPK, WebUser.getNo(), this.SendToEmpNo, this.Mobile, this.MobileInfo, this.Title, this.OpenURL);
			}
			//短信
			if (this.getPushModel().contains("SMS") == true)
			{
				httpUrl = messageUrl + "?DoType=SMS";
				HttpClientUtil.doPostJson(httpUrl,json);
				//soap.SendToWebServices(this.MyPK, WebUser.getNo(), this.SendToEmpNo, this.Mobile, this.MobileInfo,this.Title, this.OpenURL);
			}
			//钉钉
			if (this.getPushModel().contains("DingDing") == true)
			{
				httpUrl = messageUrl + "?DoType=SendToDingDing&sendTo=" + this.getSendToEmpNo() + "&title=" + this.getTitle() + "&msgConten=" + this.getMobileInfo();
				HttpClientUtil.doPostJson(httpUrl,json);
				//soap.SendToDingDing(this.MyPK, WebUser.getNo(), this.SendToEmpNo, this.Mobile, this.MobileInfo, this.Title, this.OpenURL);
			}

			//微信
			if (this.getPushModel().contains("WeiXin") == true)
			{
				httpUrl = messageUrl + "?DoType=SendToWeiXin&sendTo=" + this.getSendToEmpNo();
				HttpClientUtil.doPostJson(httpUrl,json);
				//BP.WF.WeiXin.WeiXinMessage.SendMsgToUsers(this.SendToEmpNo, this.Title, this.Doc, WebUser.getNo());
			}
			//WebService
			if (this.getPushModel().contains("WS") == true)
			{
				httpUrl = messageUrl + "?DoType=SendToWebServices";
				HttpClientUtil.doPostJson(httpUrl,json);
				//soap.SendToWebServices(this.MyPK, WebUser.getNo(), this.SendToEmpNo, this.Mobile, this.MobileInfo, this.Title, this.OpenURL);
			}

				///#endregion 发送短消息 调用接口

		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError("@消息机制没有配置成功." + ex.getMessage());
		}
		super.afterInsert();
	}
	/** 
	 设置已读
	*/
	public final void DoRead() throws Exception {

		this.setRead(1);
		this.Update();
	}

}