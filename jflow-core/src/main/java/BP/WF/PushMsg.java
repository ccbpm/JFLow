package BP.WF;

import BP.DA.AtPara;
import BP.DA.DataType;
import BP.En.Entity;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.Row;
import BP.Sys.EventListOfNode;
import BP.Tools.StringHelper;
import BP.WF.Port.WFEmp;
import BP.WF.Template.PushMsgAttr;
import BP.Web.WebUser;

/**
 * 消息推送
 */
public class PushMsg extends EntityMyPK
{
		
	/** 
	 事件
	*/
	public final String getFK_Event()
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Event);
	}
	public final void setFK_Event(String value)
	{
		this.SetValByKey(PushMsgAttr.FK_Event, value);
	}
	public final int getPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.PushWay);
	}
	public final void setPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.PushWay, value);
	}
	/** 
	节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(PushMsgAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(PushMsgAttr.FK_Node, value);
	}
	public final String getPushDoc()
	{
		String s = this.GetValStringByKey(PushMsgAttr.PushDoc);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setPushDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.PushDoc, value);
	}
	public final String getTag()
	{
		String s = this.GetValStringByKey(PushMsgAttr.Tag);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setTag(String value)
	{
		this.SetValByKey(PushMsgAttr.Tag, value);
	}
		///#endregion

		///#region 事件消息.
	/** 
	 邮件推送方式
	*/
	public final int getMailPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.MailPushWay);
	}
	public final void setMailPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.MailPushWay, value);
	}
	public final String getMailPushWayText()
	{
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
			if (this.getMailPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getMailPushWay() == 1)
			{
				return "发送给当前节点的所有处理人";
			}

			if (this.getMailPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
			if (this.getMailPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getMailPushWay() == 1)
			{
				return "发送给下一个节点的所有接受人";
			}

			if (this.getMailPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			if (this.getMailPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getMailPushWay() == 1)
			{
				return "发送给被退回的节点处理人";
			}

			if (this.getMailPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		return "未知";
	}
	/** 
	 邮件地址
	*/
	public final String getMailAddress()
	{
		return this.GetValStringByKey(PushMsgAttr.MailAddress);
	}
	public final void setMailAddress(String value)
	{
		this.SetValByKey(PushMsgAttr.MailAddress, value);
	}
	/** 
	 邮件标题.
	*/
	public final String getMailTitle()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}
		String fkEvent = this.getFK_Event();
		if (fkEvent.equals(EventListOfNode.WorkArrive)) {
			return "新工作{{Title}},发送人WebUser.No,@WebUser.Name";
		} else if (fkEvent.equals(EventListOfNode.SendSuccess)) {
			return "新工作{{Title}},发送人WebUser.No,@WebUser.Name";
		} else if (fkEvent.equals(EventListOfNode.ReturnAfter)) {
			return "被退回来{{Title}},退回人WebUser.No,@WebUser.Name";
		} else if (fkEvent.equals(EventListOfNode.ShitAfter)) {
			return "移交来的新工作{{Title}},移交人WebUser.No,@WebUser.Name";
		} else if (fkEvent.equals(EventListOfNode.UndoneAfter)) {
			return "工作被撤销{{Title}},发送人WebUser.No,@WebUser.Name";
		} else if (fkEvent.equals(EventListOfNode.AskerReAfter)) {
			return "加签新工作{{Title}},发送人WebUser.No,@WebUser.Name";
		} else {
			throw new RuntimeException("@该事件类型没有定义默认的消息模版:"
					+ this.getFK_Event());
		}
	}
	/** 
	 邮件标题
	*/
	public final String getMailTitle_Real()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		return str;
	}
	public final void setMailTitle_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.MailTitle, value);
	}
	/** 
	 邮件内容
	*/
	public final String getMailDoc_Real()
	{
		return this.GetValStrByKey(PushMsgAttr.MailDoc);
	}
	public final void setMailDoc_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.MailDoc, value);
	}
	public final String getMailDoc()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailDoc);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}
		String fkEvent = this.getFK_Event();
		if (fkEvent.equals(EventListOfNode.WorkArrive)) {
			str += "\t\n您好:";
			str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作{Url} .";
			str += "\t\n致! ";
			str += "\t\n    WebUser.No, @WebUser.Name";
			str += "\t\n    @RDT";
		} else if (fkEvent.equals(EventListOfNode.SendSuccess)) {
			str += "\t\n您好:";
			str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作{Url} .";
			str += "\t\n致! ";
			str += "\t\n    WebUser.No, @WebUser.Name";
			str += "\t\n    @RDT";
		} else if (fkEvent.equals(EventListOfNode.ReturnAfter)) {
			str += "\t\n您好:";
			str += "\t\n    工作{{Title}}被退回来了, 点击这里打开工作{Url} .";
			str += "\t\n    退回意见: \t\n ";
			str += "\t\n    {  @ReturnMsg }";
			str += "\t\n 致! ";
			str += "\t\n    WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (fkEvent.equals(EventListOfNode.ShitAfter)) {
			str += "\t\n您好:";
			str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (fkEvent.equals(EventListOfNode.UndoneAfter)) {
			str += "\t\n您好:";
			str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (fkEvent.equals(EventListOfNode.AskerReAfter)) {
			str += "\t\n您好:";
			str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else {
			throw new RuntimeException("@该事件类型没有定义默认的消息模版:"
					+ this.getFK_Event());
		}
		return str;
	}
	/** 
	 短信接收人字段
	*/
	public final String getSMSField()
	{
		return this.GetValStringByKey(PushMsgAttr.SMSField);
	}
	public final void setSMSField(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSField, value);
	}
	/** 
	 短信提醒方式
	*/
	public final int getSMSPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.SMSPushWay);
	}
	public final void setSMSPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.SMSPushWay, value);
	}
	/** 
	 发送消息标签
	*/
	public final String getSMSPushWayText()
	{
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给当前节点的所有处理人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给下一个节点的所有接受人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给被退回的节点处理人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		return "未知";
	}
	/** 
	 短信模版内容
	*/
	public final String getSMSDoc_Real()
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		return str;
	}
	public final void setSMSDoc_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	/** 
	 短信模版内容
	*/
	public final String getSMSDoc()
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}

		String fkEvent = this.getFK_Event();
		if (fkEvent.equals(EventListOfNode.WorkArrive)
				|| fkEvent.equals(EventListOfNode.SendSuccess)) {
			str = "有新工作{{Title}}需要您处理, 发送人:WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (fkEvent.equals(EventListOfNode.ReturnAfter)) {
			str = "工作{{Title}}被退回,退回人:WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (fkEvent.equals(EventListOfNode.ShitAfter)) {
			str = "移交工作{{Title}},移交人:WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (fkEvent.equals(EventListOfNode.UndoneAfter)) {
			str = "工作撤销{{Title}},撤销人:WebUser.No, @WebUser.Name,打开{Url}.";
		} else if (fkEvent.equals(EventListOfNode.AskerReAfter)) {
			str = "工作加签{{Title}},加签人:WebUser.No, @WebUser.Name,打开{Url}.";
		} else {
			throw new RuntimeException("@该事件类型没有定义默认的消息模版:"
					+ this.getFK_Event());
		}
		return str;
	}
	public final void setSMSDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
		///#endregion

		
	/** 
	 消息推送
	*/
	public PushMsg()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_PushMsg", "消息推送");

		map.AddMyPK();

		map.AddTBInt(PushMsgAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(PushMsgAttr.FK_Event, null, "事件类型", true, false, 0, 15, 10);

		///#region 将要删除.
		map.AddDDLSysEnum(PushMsgAttr.PushWay, 0, "推送方式", true, false, PushMsgAttr.PushWay, "@0=按照指定节点的工作人员@1=按照指定的工作人员@2=按照指定的工作岗位@3=按照指定的部门@4=按照指定的SQL@5=按照系统指定的字段");
			//设置内容.
		map.AddTBString(PushMsgAttr.PushDoc, null, "推送保存内容", true, false, 0, 3500, 10);
		map.AddTBString(PushMsgAttr.Tag, null, "Tag", true, false, 0, 500, 10);
		///#endregion 将要删除.

		///#region 短信.
		map.AddTBInt(PushMsgAttr.SMSPushWay, 0, "短信发送方式", true, true);
		map.AddTBString(PushMsgAttr.SMSField, null, "短信字段", true, false, 0, 100, 10);
		map.AddTBStringDoc(PushMsgAttr.SMSDoc, null, "短信内容模版", true, false, true);
		///#endregion 短信.

		///#region 邮件.
		map.AddTBInt(PushMsgAttr.MailPushWay, 0, "邮件发送方式",true, true);
		map.AddTBString(PushMsgAttr.MailAddress, null, "邮件字段", true, false, 0, 100, 10);
		map.AddTBString(PushMsgAttr.MailTitle, null, "邮件标题模版", true, false, 0, 200, 20, true);
		map.AddTBStringDoc(PushMsgAttr.MailDoc, null, "邮件内容模版", true, false, true);
		///#endregion 邮件.


		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion


	/** 
	 生成提示信息.
	 
	 @return 
	*/
	private String generAlertMessage = "";

	/** 
	 执行消息发送
	 
	 @param currNode 当前节点
	 @param en 数据实体
	 @param atPara 参数
	 @param objs 发送返回对象
	 @param jumpToNode 跳转到的节点
	 @param jumpToEmps 跳转到的人员
	 @return 执行成功的消息
	 * @throws Exception 
	*/

	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode) throws Exception
	{
		return DoSendMessage(currNode, en, atPara, objs, jumpToNode, null);
	}

	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs) throws Exception
	{
		return DoSendMessage(currNode, en, atPara, objs, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string DoSendMessage(Node currNode, Entity en, string atPara, SendReturnObjs objs, Node jumpToNode = null, string jumpToEmps = null)
	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode, String jumpToEmps) throws Exception
	{
		if(!StringHelper.isNullOrEmpty(generAlertMessage))
		{
			generAlertMessage="";
		}
		if (en == null)
		{
			return "";
		}

		///#region 处理参数.
		Row r = en.getRow();
		try
		{
			//系统参数.
			r.put("FK_MapData", en.getClassID());
		}
		catch (java.lang.Exception e)
		{
			r.put("FK_MapData", en.getClassID());
		}

		if (atPara != null)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.getHisHT().keySet())
			{
				try
				{
					r.put(s, ap.GetValStrByKey(s));
				}
				catch (java.lang.Exception e2)
				{
					r.put(s, ap.GetValStrByKey(s));
				}
			}
		}

		long workid = Long.parseLong(en.getPKVal().toString());

		String title = "标题";
		if (en.getRow().containsKey("Title") == true)
		{
			title = en.GetValStringByKey("Title"); // 获得工作标题.
			if(DataType.IsNullOrEmpty(title))
				title = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.getPKVal(), "标题");
		}
		else
		{
			title = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.getPKVal(), "标题");
		}

		String hostUrl = Glo.getHostURL();
		String sid = "{EmpStr}_" + workid + "_" + currNode.getNodeID() + "_" + DataType.getCurrentDataTime();
		String openWorkURl = hostUrl + "WF/Do.htm?DoType=OF&SID=" + sid;
		openWorkURl = openWorkURl.replace("//", "/");
		openWorkURl = openWorkURl.replace("//", "/");
///#endregion

		///#region 处理发送邮件.
		// 发送邮件.
		String mailTitleTmp = "";
		String mailDocTmp = "";
		if (this.getMailPushWay() != 0)
		{
			// 标题.
			mailTitleTmp = this.getMailTitle();
			mailTitleTmp = mailTitleTmp.replace("{Title}", title);
			mailTitleTmp = mailTitleTmp.replace("WebUser.No", WebUser.getNo());
			mailTitleTmp = mailTitleTmp.replace("@WebUser.Name", WebUser.getName());

			// 内容.
			mailDocTmp = this.getMailDoc();
			mailDocTmp = mailDocTmp.replace("{Url}", openWorkURl);
			mailDocTmp = mailDocTmp.replace("{Title}", title);

			mailDocTmp = mailDocTmp.replace("WebUser.No", WebUser.getNo());
			mailDocTmp = mailDocTmp.replace("@WebUser.Name", WebUser.getName());

			/*如果仍然有没有替换下来的变量.*/
			if (mailDocTmp.contains("@"))
			{
				mailDocTmp = Glo.DealExp(mailDocTmp, en, null);
			}

			//求发送给的人员ID.
			String toEmpIDs = "";

			///#region WorkArrive-工作到达. - 邮件处理.
			if (this.getFK_Event().equals(BP.Sys.EventListOfNode.WorkArrive) || this.getFK_Event().equals(BP.Sys.EventListOfNode.ReturnAfter))
			{
				/*工作到达.*/
				if (this.getMailPushWay() == 1)
				{
					/*如果向接受人发送邮件.*/
					toEmpIDs = jumpToEmps;
					if(DataType.IsNullOrEmpty(toEmpIDs) == false){
						String[] emps = toEmpIDs.split("[,]", -1);
						for (String emp : emps)
						{
							if (StringHelper.isNullOrEmpty(emp))
							{
								continue;
							}
	
							// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
							Object tempVar = mailDocTmp;
							String mailDocReal = (String)((tempVar instanceof String) ? tempVar : null);
							mailDocReal = mailDocReal.replace("{EmpStr}", emp);
	
							//获得当前人的邮件.
							BP.WF.Port.WFEmp empEn = new WFEmp(emp);
	
							//发送邮件.
								BP.WF.Dev2Interface.Port_SendEmail(empEn.getEmail(), mailTitleTmp, mailDocReal, "ToDo", "WKAlt" + currNode.getNodeID() + "_" + workid,null,null,null,null);
						}
						generAlertMessage += "@已向:{" + toEmpIDs + "}发送提醒邮件.";
					}
				}

				if (this.getMailPushWay() == 2)
				{
					/*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
					String emailAddress = (String)((r.get(this.getMailAddress()) instanceof String) ? r.get(this.getMailAddress()) : null);

					//发送邮件
						BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, "ToDo", "WKAlt" + currNode.getNodeID() + "_" + workid,null,null,null,null);
						generAlertMessage += "@已向:{" + emailAddress + "}发送提醒邮件.";
				}
			}
			///#region SendSuccess - 发送成功事件. - 邮件处理.
			if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
			{
				/*发送成功事件.*/
				if (this.getMailPushWay() == 1 && objs.getVarAcceptersID() != null)
				{
					/*如果向接受人发送邮件.*/
					toEmpIDs = objs.getVarAcceptersID();
					if(DataType.IsNullOrEmpty(toEmpIDs) == false){
						String[] emps = toEmpIDs.split("[,]", -1);
						for (String emp : emps)
						{
							if (StringHelper.isNullOrEmpty(emp))
							{
								continue;
							}
							// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
							Object tempVar2 = mailDocTmp;
							String mailDocReal = (String)((tempVar2 instanceof String) ? tempVar2 : null);
							mailDocReal = mailDocReal.replace("{EmpStr}", emp);
	
							//获得当前人的邮件.
							BP.WF.Port.WFEmp empEn = new WFEmp(emp);
	
							//发送邮件.
							BP.WF.Dev2Interface.Port_SendEmail(empEn.getEmail(), mailTitleTmp, mailDocReal, "ToDo", "WKAlt" + objs.getVarToNodeID() + "_" + workid,null,null,null,null);
						}
						generAlertMessage += "@已向:{" + toEmpIDs + "}发送提醒邮件.";
					}
				}

				if (this.getMailPushWay() == 2)
				{
					/*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
					String emailAddress = (String)((r.get(this.getMailAddress()) instanceof String) ? r.get(this.getMailAddress()) : null);

					//发送邮件
					BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, "ToDo", "WKAlt" + objs.getVarToNodeID() + "_" + workid,null,null,null,null);

					generAlertMessage += "@已向:{" + emailAddress + "}发送提醒邮件.";
				}
			}
		}
		///#region 处理短信..
		//定义短信内容.
		String smsDocTmp = "";
		if (this.getSMSPushWay() != 0)
		{

			///#region  生成短信内容
			Object tempVar3 = this.getSMSDoc();
			smsDocTmp = (String)((tempVar3 instanceof String) ? tempVar3 : null);
				smsDocTmp = smsDocTmp.replace("{Title}", title);
				smsDocTmp = smsDocTmp.replace("{Url}", openWorkURl);
				smsDocTmp = smsDocTmp.replace("WebUser.No", WebUser.getNo());
				smsDocTmp = smsDocTmp.replace("@WebUser.Name", WebUser.getName());
				smsDocTmp = smsDocTmp.replace("@WorkID", en.getPKVal().toString());
	            smsDocTmp = smsDocTmp.replace("@OID", en.getPKVal().toString());
				
				/*如果仍然有没有替换下来的变量.*/
				if (smsDocTmp.contains("@") == true)
				{
					smsDocTmp = Glo.DealExp(smsDocTmp, en, null);
				}

			/*如果仍然有没有替换下来的变量.*/
				if (smsDocTmp.contains("@"))
				{
					smsDocTmp = Glo.DealExp(smsDocTmp, en, null);
				}
			//求发送给的人员ID.
			String toEmpIDs = "";

			///#region WorkArrive - 发送成功事件
			if (this.getFK_Event().equals(EventListOfNode.WorkArrive) || this.getFK_Event().equals(EventListOfNode.ReturnAfter))
			{
				String msgType = "ToDo";
				if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
				{
					msgType = "Return";
				}

				/*发送成功事件.*/
				if (this.getSMSPushWay() == 1)
				{
					/*如果向接受人发送短信.*/
					toEmpIDs = jumpToEmps;
					if(DataType.IsNullOrEmpty(toEmpIDs) == false){
						String[] emps = toEmpIDs.split("[,]", -1);
						for (String emp : emps)
						{
							if (StringHelper.isNullOrEmpty(emp))
							{
								continue;
							}
	
							Object tempVar4 = smsDocTmp;
							String smsDocTmpReal = (String)((tempVar4 instanceof String) ? tempVar4 : null);
							smsDocTmpReal = smsDocTmpReal.replace("{EmpStr}", emp);
							BP.WF.Port.WFEmp empEn = new WFEmp(emp);
	
							//发送短信.
							Dev2Interface.Port_SendSMS(empEn.getTel(), smsDocTmpReal, msgType, "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, emp, null);
						}
						generAlertMessage += "@已向:{" + toEmpIDs + "}发送短消息提醒，由 " + this.getFK_Event() + " 发出.";
					}
				}

				if (this.getMailPushWay() == 2)
				{
					/*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
					String tel = (String)((r.get(this.getSMSField()) instanceof String) ? r.get(this.getSMSField()) : null);
					//发送短信.

					BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, msgType, "WKAlt" + currNode.getNodeID() + "_" + workid,null,null,null,null);
					generAlertMessage += "@已向:{" + tel + "}发送短消息提醒，由 " + this.getFK_Event() + " 发出.";
				}
			}
			///#endregion WorkArrive - 工作到达事件


			///#region SendSuccess - 发送成功事件
			if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
			{
				/*发送成功事件.*/
				if (this.getSMSPushWay() == 1 && objs.getVarAcceptersID() != null)
				{
					/*如果向接受人发送短信.*/
					toEmpIDs = objs.getVarAcceptersID();
					if(DataType.IsNullOrEmpty(toEmpIDs) == false){
						String[] emps = toEmpIDs.split("[,]", -1);
						for (String emp : emps)
						{
							if (StringHelper.isNullOrEmpty(emp))
							{
								continue;
							}
	
							Object tempVar5 = smsDocTmp;
							String smsDocTmpReal = (String)((tempVar5 instanceof String) ? tempVar5 : null);
							smsDocTmpReal = smsDocTmpReal.replace("{EmpStr}", emp);
	
							BP.WF.Port.WFEmp empEn = new WFEmp(emp);
	
							//发送短信.
							Dev2Interface.Port_SendSMS(empEn.getTel(), smsDocTmpReal, "ToDo", "WKAlt" + objs.getVarToNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, emp, null);
						}
						generAlertMessage += "@已向:{" + toEmpIDs + "}发送提醒手机短信，由 SendSuccess 发出.";
					}
				}

				if (this.getMailPushWay() == 2)
				{
					/*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
					String tel = (String)((r.get(this.getSMSField()) instanceof String) ? r.get(this.getSMSField()) : null);
					if (tel != null || tel.length() > 6)
					{
						//发送短信.
						BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, "ToDo", "WKAlt" + objs.getVarToNodeID() + "_" + workid,null,null,null,null);
						generAlertMessage += "@已向:{" + tel + "}发送提醒手机短信，由 SendSuccess 发出.";
					}
				}
			}
			///#endregion SendSuccess - 发送成功事件

		}
		///#endregion 处理短信.

		return generAlertMessage;
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
	   //  this.MyPK = this.FK_Event + "_" + this.FK_Node + "_" + this.PushWay;
		return super.beforeUpdateInsertAction();
	}
}