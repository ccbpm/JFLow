package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 消息
*/
public class SMS extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 新方法 2013
	/** 
	 发送消息
	 
	 @param userNo 接受人
	 @param msgTitle 标题
	 @param msgDoc 内容
	 @param msgFlag 标记
	 @param msgType 类型
	 @param paras 扩展参数
	*/
	public static void SendMsg(String userNo, String msgTitle, String msgDoc, String msgFlag, String msgType, String paras)
	{

		SMS sms = new SMS();
		sms.MyPK = DBAccess.GenerGUID();
		sms.setHisEmailSta(MsgSta.UnRun);

		sms.setSender(WebUser.No);
		sms.setSendToEmpNo(userNo);

		sms.setTitle(msgTitle);
		sms.setDocOfEmail(msgDoc);

		sms.setSender(BP.Web.WebUser.No);
		sms.setRDT(BP.DA.DataType.CurrentDataTime);

		sms.setMsgFlag(msgFlag); // 消息标志.
		sms.setMsgType(msgType); // 消息类型.'

		sms.setAtPara(paras);
		sms.Insert();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 新方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 手机短信属性
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		if (DataType.IsNullOrEmpty(doc))
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
		if (DataType.IsNullOrEmpty(doc))
		{
			return this.getTitle();
		}
		return doc.replace('~', '\'');

		return this.getDocOfEmail();
	}
	public final void setDoc(String value)
	{
		SetValByKey(SMSAttr.EmailDoc, value);
	}
	/** 
	 打开的连接
	*/
	public final String getOpenURL()
	{
		return this.GetParaString(SMSAttr.OpenUrl);
	}
	public final void setOpenURL(String value)
	{
		this.SetPara(SMSAttr.OpenUrl, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String getPushModel()
	{
		return this.GetParaString(SMSAttr.PushModel);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
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
		if (this._enMap != null)
		{
			return this._enMap;
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
		map.AddTBDateTime(SMSAttr.SendDT, null, "发送时间", false, false);

		map.AddTBInt(SMSAttr.IsRead, 0, "是否读取?", true, true);
		map.AddTBInt(SMSAttr.IsAlert, 0, "是否提示?", true, true);

		map.AddTBString(SMSAttr.MsgFlag, null, "消息标记(用于防止发送重复)", false, true, 0, 200, 20);
		map.AddTBString(SMSAttr.MsgType, null, "消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...)", false, true, 0, 200, 20);

			//其他参数.
		map.AddTBAtParas(500);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 发送邮件
	 
	 @param mail
	 @param mailTitle
	 @param mailDoc
	 @return 
	*/
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent in Java to the 'async' keyword:
//ORIGINAL LINE: public static async Task<bool> SendEmailNowAsync(string mail, string mailTitle, string mailDoc)
	public static Task<Boolean> SendEmailNowAsync(String mail, String mailTitle, String mailDoc)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to 'await' in Java:
		return await Task.Run(() ->
		{
				try
				{
					System.Net.Mail.MailMessage myEmail = new System.Net.Mail.MailMessage();

					//邮件地址.
					String emailAddr = SystemConfig.GetValByKey("SendEmailAddress", null);
					if (emailAddr == null)
					{
						emailAddr = "ccbpmtester@tom.com";
					}

					String emailPassword = SystemConfig.GetValByKey("SendEmailPass", null);
					if (emailPassword == null)
					{
						emailPassword = "ccbpm123";
					}

					String displayName = SystemConfig.GetValByKey("SendEmailDisplayName", "驰骋BPM");


					myEmail.From = new System.Net.Mail.MailAddress(emailAddr, displayName, System.Text.Encoding.UTF8);

					myEmail.To.Add(mail);
					myEmail.Subject = mailTitle;
					myEmail.SubjectEncoding = System.Text.Encoding.UTF8; //邮件标题编码

					myEmail.IsBodyHtml = true;

					mailDoc = BP.DA.DataType.ParseText2Html(mailDoc);

					myEmail.Body = mailDoc;
					myEmail.BodyEncoding = System.Text.Encoding.UTF8; //邮件内容编码
					myEmail.IsBodyHtml = true; //是否是HTML邮件
					myEmail.Priority = MailPriority.High; // 邮件优先级

					SmtpClient client = new SmtpClient();

					//是否启用ssl?
					boolean isEnableSSL = false;
					String emailEnableSSL = SystemConfig.GetValByKey("SendEmailEnableSsl", null);
					if (emailEnableSSL == null || emailEnableSSL.equals("0"))
					{
						isEnableSSL = false;
					}
					else
					{
						isEnableSSL = true;
					}

					client.Credentials = new System.Net.NetworkCredential(emailAddr, emailPassword);

					//上述写你的邮箱和密码
					client.Port = SystemConfig.GetValByKeyInt("SendEmailPort", 587); //使用的端口
					client.Host = SystemConfig.GetValByKey("SendEmailHost", "smtp.gmail.com");

					// 经过ssl加密.
					if (SystemConfig.GetValByKeyInt("SendEmailEnableSsl", 1) == 1)
					{
						client.EnableSsl = true; //经过ssl加密.
					}
					else
					{
						client.EnableSsl = false; //经过ssl加密.
					}

					Object userState = myEmail;
					client.SendAsync(myEmail, userState);
					return true;
				}
				catch (RuntimeException e)
				{
					return false;
				}
		});
	}
	/** 
	 插入之后执行的方法.
	*/
	@Override
	protected void afterInsert()
	{
		try
		{
			BP.WF.CCInterface.PortalInterfaceSoapClient soap = null;
			if (this.getHisEmailSta() != MsgSta.UnRun)
			{
				return;
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 发送邮件
			if (this.getPushModel().contains("Email") == true && DataType.IsNullOrEmpty(this.getEmail()) == false)
			{
				String emailStrs = this.getEmail();
				emailStrs = emailStrs.replace(",", ";");
				emailStrs = emailStrs.replace("，", ";");

				//包含多个邮箱
				if (emailStrs.contains(";") == true)
				{
					String[] emails = emailStrs.split("[;]", -1);
					for (String email : emails)
					{
						if (DataType.IsNullOrEmpty(email) == true)
						{
							continue;
						}

						SendEmailNowAsync(email, this.getTitle(), this.getDocOfEmail());
					}
				}
				else
				{ //单个邮箱
					SendEmailNowAsync(this.getEmail(), this.getTitle(), this.getDocOfEmail());
				}

			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 发送邮件

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 发送短消息 调用接口
			//发送短消息的前提必须是手机号不能为空
			//if (DataType.IsNullOrEmpty(this.Mobile) == true)
			//    return;
				//throw new Exception("发送短消息时接收人的手机号不能为空,否则接受不到消息");

			soap = BP.WF.Glo.GetPortalInterfaceSoapClient();
			//站内消息
			if (this.getPushModel().contains("CCMsg") == true)
			{
				soap.SendToCCMSG(this.MyPK, WebUser.No, this.getSendToEmpNo(), this.getMobile(), this.getMobileInfo(), this.getTitle(), this.getOpenURL());
			}
			//短信
			if (this.getPushModel().contains("SMS") == true)
			{
			   soap.SendToWebServices(this.MyPK, WebUser.No, this.getSendToEmpNo(), this.getMobile(), this.getMobileInfo(), this.getTitle(), this.getOpenURL());
			}
			//钉钉
			if (this.getPushModel().contains("DingDing") == true)
			{

				soap.SendToDingDing(this.MyPK, WebUser.No, this.getSendToEmpNo(), this.getMobile(), this.getMobileInfo(), this.getTitle(), this.getOpenURL());
			}
			//微信
			if (this.getPushModel().contains("WeiXin") == true)
			{
				BP.WF.WeiXin.WeiXinMessage.SendMsgToUsers(this.getSendToEmpNo(), this.getTitle(), this.getDoc(), WebUser.No);
			}
			//WebService
			if (this.getPushModel().contains("WS") == true)
			{
				soap.SendToWebServices(this.MyPK, WebUser.No, this.getSendToEmpNo(), this.getMobile(), this.getMobileInfo(), this.getTitle(), this.getOpenURL());
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 发送短消息 调用接口

		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DebugWriteError("@消息机制没有配置成功." + ex.getMessage());
		}
		super.afterInsert();
	}
}