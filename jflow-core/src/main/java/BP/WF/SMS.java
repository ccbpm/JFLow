package BP.WF;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.Difference.Handler.PortalWebService;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

/** 
 消息
*/
public class SMS extends EntityMyPK
{
	/** 
	 发送消息
	 @param userNo 接受人
	 @param msgTitle 标题
	 @param msgDoc 内容
	 @param msgFlag 标记
	 @param msgType 类型
	 @param paras 扩展参数
	 * @throws Exception 
	*/
	public static void SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag, String msgType, String paras) throws Exception
	{

		SMS sms = new SMS();
		sms.setMyPK(DBAccess.GenerGUID());
		sms.setHisEmailSta(MsgSta.UnRun);

		sms.setSender(WebUser.getNo());
		sms.setSendToEmpNo(userNo);

		sms.setTitle(msgTitle);
		sms.setDocOfEmail(msgDoc);

		sms.setSender(BP.Web.WebUser.getNo());
		sms.setRDT(BP.DA.DataType.getCurrentDataTime());

		sms.setMsgFlag(msgFlag); // 消息标志.
		sms.setMsgType(msgType); // 消息类型.'

		sms.setAtPara(paras);
//		sms.afterInsert();
		sms.Insert();
	}
	/** 
	 发送消息
	 @param mobileNum 手机号吗
	 @param mobileInfo 短信信息
	 @param email 邮件
	 @param title 标题
	 @param infoBody 邮件内容
	 @param msgFlag 消息标记，可以为空。
	 @param guestNo 用户编号
	 * @throws Exception 
	*/
	public static void SendMsg(String mobileNum, String mobileInfo, String email, String title, String infoBody, String msgFlag, String msgType, String guestNo) throws Exception
	{
		SMS sms = new SMS();
		sms.setSender(WebUser.getNo());
		sms.setRDT(BP.DA.DataType.getCurrentDataTimess());
		sms.setSendToEmpNo(guestNo);

		// 邮件信息
		sms.setHisEmailSta(MsgSta.UnRun);
		sms.setTitle(title);
		sms.setDocOfEmail(infoBody);

		//手机信息.
		sms.setMobile(mobileNum);
		sms.setHisMobileSta(MsgSta.UnRun);
		sms.setMobileInfo(mobileInfo);
		sms.setMsgFlag(msgFlag); // 消息标志.

		if (DotNetToJavaStringHelper.isNullOrEmpty(msgFlag))
		{
			sms.setMyPK(DBAccess.GenerGUID());
			sms.Insert();
		}
		else
		{
			// 如果已经有该PK,就不让插入了.
			try
			{
				sms.setMyPK(msgFlag);
				sms.Insert();
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	/** 
	 手机号码
	*/
	public final String getMobile()
	{
		return this.GetValStringByKey(SMSAttr.Mobile);
	}
	public final void setMobile(String value)
	{
		SetValByKey(SMSAttr.Mobile, value);
	}
	/** 
	 手机状态
	*/
	public final MsgSta getHisMobileSta()
	{
		return MsgSta.forValue(this.GetValIntByKey(SMSAttr.MobileSta));
	}
	public final void setHisMobileSta(MsgSta value)
	{
		SetValByKey(SMSAttr.MobileSta, value.getValue());
	}
	/** 
	 手机信息
	 
	*/
	public final String getMobileInfo()
	{
		return this.GetValStringByKey(SMSAttr.MobileInfo);
	}
	public final void setMobileInfo(String value)
	{
		SetValByKey(SMSAttr.MobileInfo, value);
	}

		///#endregion
 
		///#region  邮件属性
	/** 
	 参数
	 
	*/
	public final String getAtPara()
	{
		return this.GetValStrByKey("AtPara", "");
	}
	public final void setAtPara(String value)
	{
		this.SetValByKey("AtPara", value);
	}
	/** 
	 邮件状态
	 
	*/
	public final MsgSta getHisEmailSta()
	{
		return MsgSta.forValue(this.GetValIntByKey(SMSAttr.EmailSta));
	}
	public final void setHisEmailSta(MsgSta value)
	{
		this.SetValByKey(SMSAttr.EmailSta, value.getValue());
	}
	/** 
	 Email
	 
	*/
	public final String getEmail()
	{
		return this.GetValStringByKey(SMSAttr.Email);
	}
	public final void setEmail(String value)
	{
		SetValByKey(SMSAttr.Email, value);
	}
	/** 
	 发送给
	 
	*/
	public final String getSendToEmpNo()
	{
		return this.GetValStringByKey(SMSAttr.SendTo);
	}
	public final void setSendToEmpNo(String value)
	{
		SetValByKey(SMSAttr.SendTo, value);
	}
	public final int getIsRead()
	{
		return this.GetValIntByKey(SMSAttr.IsRead);
	}
	public final void setIsRead(int value)
	{
		this.SetValByKey(SMSAttr.IsRead, (int)value);
	}
	public final int getIsAlert()
	{
		return this.GetValIntByKey(SMSAttr.IsAlert);
	}
	public final void setIsAlert(int value)
	{
		this.SetValByKey(SMSAttr.IsAlert, (int)value);
	}
	/** 
	 消息标记(可以用它来避免发送重复)
	 
	*/
	public final String getMsgFlag()
	{
		return this.GetValStringByKey(SMSAttr.MsgFlag);
	}
	public final void setMsgFlag(String value)
	{
		SetValByKey(SMSAttr.MsgFlag, value);
	}
	/** 
	 类型
	 
	*/
	public final String getMsgType()
	{
		return this.GetValStringByKey(SMSAttr.MsgType);
	}
	public final void setMsgType(String value)
	{
		SetValByKey(SMSAttr.MsgType, value);
	}
	/** 
	 发送人
	 
	*/
	public final String getSender()
	{
		return this.GetValStringByKey(SMSAttr.Sender);
	}
	public final void setSender(String value)
	{
		SetValByKey(SMSAttr.Sender, value);
	}
	/** 
	 记录日期
	 
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(SMSAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(SMSAttr.RDT, value);
	}
	/** 
	 标题
	 
	*/
	public final String getTitle()
	{
		return this.GetValStringByKey(SMSAttr.EmailTitle);
	}
	public final void setTitle(String value)
	{
		SetValByKey(SMSAttr.EmailTitle, value);
	}
	/** 
	 邮件内容
	 
	*/
	public final String getDocOfEmail()
	{
		String doc = this.GetValStringByKey(SMSAttr.EmailDoc);
		if (DotNetToJavaStringHelper.isNullOrEmpty(doc))
		{
			return this.getTitle();
		}
		return doc.replace('~', '\'');
	}
	public final void setDocOfEmail(String value)
	{
		SetValByKey(SMSAttr.EmailDoc, value);
	}
	/** 
	 邮件内容.
	 
	*/
	public final String getDoc()
	{
		String doc = this.GetValStringByKey(SMSAttr.EmailDoc);
		if (StringHelper.isNullOrEmpty(doc))
		{
			return this.getTitle();
		}
		return doc.replace('~', '\'');
	}
	public final void setDoc(String value)
	{
		SetValByKey(SMSAttr.EmailDoc, value);
	}

		///#endregion


		
	/** 
	 UI界面上的访问控制
	 
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	 消息
	 
	*/
	public SMS()
	{
	}
	/** 
	 Map
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_SMS", "消息");

		map.AddMyPK();

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
		map.AddTBDateTime(SMSAttr.SendDT,null, "发送时间", false, false);

		map.AddTBInt(SMSAttr.IsRead, 0, "是否读取?", true, true);
		map.AddTBInt(SMSAttr.IsAlert, 0, "是否提示?", true, true);

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
	 
	 @param mail
	 @param mailTitle
	 @param mailDoc
	 @return 
	 * @throws Exception 
	*/
	public static boolean SendEmailNow(String mail, String mailTitle, String mailDoc) throws Exception
	{
		 if(DataType.IsNullOrEmpty(mail) == true) 
			 return false;
		//邮件地址.return
        final String  emailAddr = SystemConfig.GetValByKey("SendEmailAddress", "ccbpmtester@tom.com");
       

       final String emailPassword = SystemConfig.GetValByKey("SendEmailPass", "ccbpm123");
		// 第一步：配置javax.mail.Session对象  
		Properties props = new Properties(); 
		props.setProperty("mail.transport.protocol", "smtp"); 
		props.put("mail.smtp.host",SystemConfig.GetValByKey("SendEmailHost", "smtp.tom.com"));//发邮件地址
		props.put("mail.smtp.port",SystemConfig.GetValByKeyInt("SendEmailPort", 25)); //端口号
		props.put("mail.smtp.auth","true");//使用 STARTTLS安全连接  
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication(){
			    return new PasswordAuthentication(emailAddr,emailPassword);
			 }
		};

		Session mailSession = Session.getInstance(props,auth);
	       		 
		InternetAddress fromAddress = new InternetAddress(emailAddr);
        InternetAddress toAddress = new InternetAddress(mail);

		// 3. 创建一封邮件
        MimeMessage message = new MimeMessage(mailSession);
        
        message.setFrom(fromAddress);
        message.addRecipient(RecipientType.TO, toAddress);
        
        message.setSentDate(BP.Tools.DateUtils.currentDate());
        message.setSubject(mailTitle);
        message.setText(mailDoc);
        
        //4.发送Email,
        Transport transport = mailSession.getTransport("smtp");//定义发送协议
   
		//transport.connect(SystemConfig.GetValByKey("SendEmailHost", "smtp.gmail.com"),emailAddr, emailPassword);
		//登录邮箱
        transport.send(message, message.getRecipients(RecipientType.TO));//发送邮件
        
        return true;
		
		
	}
	/** 
	 插入之后执行的方法.
	 * @throws Exception 
	 
	*/
	@Override
	protected void afterInsert() throws Exception
	{
		/* 发送消息开关 */
		if (BP.WF.Glo.getIsEnableSysMessage() == false)		
			return;
		
		
		try
		{
			//PortalInterfaceWSImpl soap = new PortalInterfaceWSImpl();
			String xmlStr = "";
			if (this.getHisEmailSta() == MsgSta.UnRun)
			{
				//发送邮件
				SendEmailNow(this.getEmail(), this.getTitle(), this.getDocOfEmail());
				return;
			}

			if (this.getHisMobileSta() == MsgSta.UnRun)
			{
				String tag = "@MsgFlag=" + this.getMsgFlag() + "@MsgType=" + this.getMsgType() + this.getatPara() + "@Sender=" + this.getSender() + "@SenderName=" + BP.Web.WebUser.getName();
				PortalWebService service = new PortalWebService();
				switch (BP.WF.Glo.getShortMessageWriteTo())
				{
					case ToSMSTable: //写入消息表。
						break;
					case ToWebservices: // 写入webservices.
						
						service.SendToWebServices(this.getSender(), this.getSendToEmpNo(), this.getTitle(),this.getMobileInfo(),this.GetParaString("OpenUrl") , this.getMsgType());
						//soap.SendToWebServices(this.getMyPK(), WebUser.getNo(), this.getSendToEmpNo(), this.getMobile(), this.getMobileInfo(),tag);
						break;
					case ToDingDing: // 写入dingding.
						service.SendToDingDing(this.getSender(), this.getSendToEmpNo(), this.getTitle(),this.getMobileInfo(),this.GetParaString("OpenUrl") , this.getMsgType());
						
						break;
					case ToWeiXin: // 写入微信.
						service.SendToWeiXin(this.getSender(), this.getSendToEmpNo(), this.getTitle(),this.getMobileInfo(),this.GetParaString("OpenUrl") , this.getMsgType());
						//soap.SendToWeiXin(this.getMyPK(), WebUser.getNo(), this.getSendToEmpNo(), this.getMobile(), this.getMobileInfo());
						break;
					case CCIM: // 写入即时通讯系统.
						service.SendToCCIM(this.getSender(), this.getSendToEmpNo(), this.getTitle(),this.getMobileInfo(),this.GetParaString("OpenUrl") , this.getMsgType());
						
						//soap.SendToCCIM(this.getMyPK(), WebUser.getNo(), this.getSendToEmpNo(), this.getMobileInfo(),tag);
						break;
					default:
						break;
				}
			}
		}
		catch(RuntimeException ex)
		{
			BP.DA.Log.DebugWriteError("@消息机制没有配置成功."+ex.getMessage());
		}
		super.afterInsert();
	}
}
