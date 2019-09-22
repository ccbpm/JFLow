package BP.WF.Template;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 消息推送
*/
public class PushMsg extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(PushMsgAttr.FK_Flow, value);
	}
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
	/** 
	 推送方式.
	*/
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
		if (DataType.IsNullOrEmpty(s) == true)
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
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setTag(String value)
	{
		this.SetValByKey(PushMsgAttr.Tag, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	/** 
	 推送方式Name
	*/
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
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}
		switch (this.getFK_Event())
		{
			case EventListOfNode.WorkArrive:
				return "新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListOfNode.SendSuccess:
				return "新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListOfNode.ShitAfter:
				return "移交来的新工作{{Title}},移交人@WebUser.No,@WebUser.Name";
			case EventListOfNode.ReturnAfter:
				return "被退回来{{Title}},退回人@WebUser.No,@WebUser.Name";
			case EventListOfNode.UndoneAfter:
				return "工作被撤销{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListOfNode.AskerReAfter:
				return "加签新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListOfNode.FlowOverAfter:
				return "流程{{Title}}已经结束,处理人@WebUser.No,@WebUser.Name";
			case EventListOfNode.AfterFlowDel:
				return "流程{{Title}}已经删除,处理人@WebUser.No,@WebUser.Name";
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
				break;
		}
		return str;
	}
	/** 
	 Email节点s
	*/
	public final String getMailNodes()
	{
		return this.GetValStringByKey(PushMsgAttr.MailNodes);
	}
	public final void setMailNodes(String value)
	{
		this.SetValByKey(PushMsgAttr.MailNodes, value);
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
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}
		switch (this.getFK_Event())
		{
			case EventListOfNode.WorkArrive:
				str += "\t\n您好:";
				str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作报告{Url} .";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n致! ";
				str += "\t\n    @WebUser.No, @WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListOfNode.SendSuccess:

				str += "\t\nHi,您好您有新工作.";
				str += "\t\n    标题:{{Title}} .";
				str += "\t\n    单号:{{BillNo}} .";
				str += "\t\n    详细信息:请打开工作{Url} ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n 致!! ";
				str += "\t\n    @WebUser.No, @WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListOfNode.ReturnAfter:
				str += "\t\n您好:";
				str += "\t\n    工作{{Title}}被退回来了, 点击这里打开工作报告{Url} .";
				str += "\t\n    退回意见: \t\n ";
				str += "\t\n    {  @ReturnMsg }";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListOfNode.ShitAfter:
				str += "\t\n 您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListOfNode.UndoneAfter:
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作报告{Url} .";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListOfNode.AskerReAfter: //加签.
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开报告{Url} .";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListOfNode.FlowOverAfter: //流程结束后.
				str += "\t\n您好:";
				str += "\t\n    工作{{Title}}已经结束, 点击这里打开工作报告{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
				break;
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
	public final String getSMSNodes()
	{
		return this.GetValStringByKey(PushMsgAttr.SMSNodes);
	}
	public final void setSMSNodes(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSNodes, value);
	}
	public final String getSMSPushModel()
	{
		return this.GetValStringByKey(PushMsgAttr.SMSPushModel);
	}
	public final void setSMSPushModel(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSPushModel, value);
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

		if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给所有节点处理人";
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
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}

		switch (this.getFK_Event())
		{
			case EventListOfNode.WorkArrive:
			case EventListOfNode.SendSuccess:
				str = "有新工作{{Title}}需要您处理, 发送人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListOfNode.ReturnAfter:
				str = "工作{{Title}}被退回,退回人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListOfNode.ShitAfter:
				str = "移交工作{{Title}},移交人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListOfNode.UndoneAfter:
				str = "工作撤销{{Title}},撤销人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			case EventListOfNode.AskerReAfter: //加签.
				str = "工作加签{{Title}},加签人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			case EventListOfNode.FlowOverAfter: //加签.
				str = "流程{{Title}}已经结束,最后处理人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
				break;
		}
		return str;
	}
	public final void setSMSDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	/** 
	 按照指定的SQL
	*/
	public final String getBySQL()
	{
		return this.GetValStrByKey(PushMsgAttr.BySQL);
	}
	public final void setBySQL(String value)
	{
		this.SetValByKey(PushMsgAttr.BySQL, value);
	}
	/** 
	 发送给指定的人员，人员之间以逗号分割
	*/
	public final String getByEmps()
	{
		return this.GetValStrByKey(PushMsgAttr.ByEmps);
	}
	public final void setByEmps(String value)
	{
		this.SetValByKey(PushMsgAttr.ByEmps, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_PushMsg", "消息推送");

		map.AddMyPK();

		map.AddTBString(PushMsgAttr.FK_Flow, null, "流程", true, false, 0, 3, 10);
		map.AddTBInt(PushMsgAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(PushMsgAttr.FK_Event, null, "事件类型", true, false, 0, 20, 10);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 将要删除.
		map.AddDDLSysEnum(PushMsgAttr.PushWay, 0, "推送方式", true, false, PushMsgAttr.PushWay, "@0=按照指定节点的工作人员@1=按照指定的工作人员@2=按照指定的工作岗位@3=按照指定的部门@4=按照指定的SQL@5=按照系统指定的字段");
			//设置内容.
		map.AddTBString(PushMsgAttr.PushDoc, null, "推送保存内容", true, false, 0, 3500, 10);
		map.AddTBString(PushMsgAttr.Tag, null, "Tag", true, false, 0, 500, 10);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 将要删除.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 短消息.
		map.AddTBInt(PushMsgAttr.SMSPushWay, 0, "短消息发送方式", true, true);
		map.AddTBString(PushMsgAttr.SMSField, null, "短消息字段", true, false, 0, 100, 10);
		map.AddTBStringDoc(PushMsgAttr.SMSDoc, null, "短消息内容模版", true, false, true);
		map.AddTBString(PushMsgAttr.SMSNodes, null, "SMS节点s", true, false, 0, 100, 10);

			//@0=站内消息@1=短信@2=钉钉@3=微信@4=即时通
		map.AddTBString(PushMsgAttr.SMSPushModel, null, "短消息发送设置", true, false, 0, 50, 10);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 短消息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 邮件.
		map.AddTBInt(PushMsgAttr.MailPushWay, 0, "邮件发送方式", true, true);
		map.AddTBString(PushMsgAttr.MailAddress, null, "邮件字段", true, false, 0, 100, 10);
		map.AddTBString(PushMsgAttr.MailTitle, null, "邮件标题模版", true, false, 0, 200, 20, true);
		map.AddTBStringDoc(PushMsgAttr.MailDoc, null, "邮件内容模版", true, false, true);
		map.AddTBString(PushMsgAttr.MailNodes, null, "Mail节点s", true, false, 0, 100, 10);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 邮件.

		map.AddTBString(PushMsgAttr.BySQL, null, "按照SQL计算", true, false, 0, 500, 10);
		map.AddTBString(PushMsgAttr.ByEmps, null, "发送给指定的人员", true, false, 0, 100, 10);
		map.AddTBAtParas(500);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


	/** 
	 生成提示信息.
	 
	 @return 
	*/
	private String generAlertMessage = null;
	/** 
	 执行消息发送
	 
	 @param currNode 当前节点
	 @param en 数据实体
	 @param atPara 参数
	 @param objs 发送返回对象
	 @param jumpToNode 跳转到的节点
	 @param jumpToEmps 跳转到的人员
	 @return 执行成功的消息
	*/

	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode)
	{
		return DoSendMessage(currNode, en, atPara, objs, jumpToNode, null);
	}

	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs)
	{
		return DoSendMessage(currNode, en, atPara, objs, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string DoSendMessage(Node currNode, Entity en, string atPara, SendReturnObjs objs, Node jumpToNode = null, string jumpToEmps = null)
	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode, String jumpToEmps)
	{
		if (en == null)
		{
			return "";
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理参数.
		Row r = en.Row;
		try
		{
			//系统参数.
			r.Add("FK_MapData", en.ClassID);
		}
		catch (java.lang.Exception e)
		{
			r["FK_MapData"] = en.ClassID;
		}

		if (atPara != null)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.HisHT.keySet())
			{
				try
				{
					r.Add(s, ap.GetValStrByKey(s));
				}
				catch (java.lang.Exception e2)
				{
					r[s] = ap.GetValStrByKey(s);
				}
			}
		}

		//生成标题.
		long workid = Long.parseLong(en.PKVal.toString());
		String title = "标题";
		if (en.Row.ContainsKey("Title") == true)
		{
			title = en.GetValStringByKey("Title"); // 获得工作标题.
			if (DataType.IsNullOrEmpty(title))
			{
				title = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.PKVal, "标题");
			}
		}
		else
		{
			title = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.PKVal, "标题");
		}

		//生成URL.
		String hostUrl = BP.WF.Glo.getHostURL();
		String sid = "{EmpStr}_" + workid + "_" + currNode.getNodeID() + "_" + DataType.CurrentDataTime;
		String openWorkURl = hostUrl + "WF/Do.htm?DoType=OF&SID=" + sid;
		openWorkURl = openWorkURl.replace("//", "/");
		openWorkURl = openWorkURl.replace("http:/", "http://");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion


		// 有可能是退回信息. 翻译.
		if (jumpToEmps == null)
		{
			if (atPara != null)
			{
				AtPara ap = new AtPara(atPara);
				jumpToEmps = ap.GetValStrByKey("SendToEmpIDs");
			}
		}

		//发送消息
		String msg = this.SendMessage(title, en, currNode, workid, jumpToEmps, openWorkURl, objs, r);

		//发送短消息.
	   // string msg1 = this.SendShortMessageToSpecNodes(title, openWorkURl, en, currNode, workid, objs, null, jumpToEmps);
		//发送邮件.
		//string msg2 = this.SendEmail(title, openWorkURl, en, jumpToEmps, currNode, workid, objs, r);

		return msg;
	}
	/** 
	 发送消息
	 
	 @param title 标题
	 @param en 数据实体
	 @param currNode 当前节点
	 @param workid 流程WorkId
	 @param jumpToEmps 下一个节点的接收人
	 @param openUrl 打开链接的URL
	 @param objs 发送返回的对象
	 @param r 表单数据HashTable
	 @return 
	*/
	private String SendMessage(String title, Entity en, Node currNode, long workid, String jumpToEmps, String openUrl, SendReturnObjs objs, Row r)
	{
		//不启用消息
		if (this.getSMSPushWay() == 0)
		{
			return "";
		}
		String generAlertMessage = ""; //定义要返回的提示消息.
		String mailTitle = this.getMailTitle(); // 邮件标题
		String smsDoc = this.getSMSDoc(); //消息模板

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 邮件标题
		mailTitle = this.getMailTitle();
		mailTitle = mailTitle.replace("{Title}", title);
		mailTitle = mailTitle.replace("@WebUser.No", WebUser.No);
		mailTitle = mailTitle.replace("@WebUser.Name", WebUser.Name);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 邮件标题

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  处理消息内容
		smsDoc = smsDoc.replace("{Title}", title);
		smsDoc = smsDoc.replace("{Url}", openUrl);
		smsDoc = smsDoc.replace("@WebUser.No", WebUser.No);
		smsDoc = smsDoc.replace("@WebUser.Name", WebUser.Name);
		smsDoc = smsDoc.replace("@WorkID", en.PKVal.toString());
		smsDoc = smsDoc.replace("@OID", en.PKVal.toString());

		/*如果仍然有没有替换下来的变量.*/
		if (smsDoc.contains("@") == true)
		{
			smsDoc = BP.WF.Glo.DealExp(smsDoc, en, null);
		}

		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.ReturnAfter))
		{
			//获取退回原因
			Paras ps = new Paras();
			ps.SQL = "SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "WorkID  ORDER BY RDT DESC";
			ps.Add(ReturnWorkAttr.WorkID, Long.parseLong(en.PKVal.toString()));
			DataTable retunWdt = DBAccess.RunSQLReturnTable(ps);
			if (retunWdt.Rows.Count != 0)
			{
				String returnMsg = retunWdt.Rows[0]["BeiZhu"].toString();
				String returner = retunWdt.Rows[0]["ReturnerName"].toString();
				smsDoc = smsDoc.replace("ReturnMsg", returnMsg);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理消息内容

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 消息发送
		String toEmpIDs = "";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 表单字段作为接受人
		if (this.getSMSPushWay() == 2)
		{
			/*从字段里取数据. */
			String toEmp = r[this.getSMSField()] instanceof String ? (String)r[this.getSMSField()] : null;
			//修改内容
			smsDoc = smsDoc.replace("{EmpStr}", toEmp);
			openUrl = openUrl.replace("{EmpStr}", toEmp);

			//发送消息
			BP.WF.Dev2Interface.Port_SendMessage(toEmp, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel());
			return "@已向:{" + toEmp + "}发送提醒信息.";
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 表单字段作为接受人

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果发送给指定的节点处理人,就计算出来直接退回,任何方式的处理人都是一致的.
		if (this.getSMSPushWay() == 3)
		{
			/*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
			String[] nodes = this.getSMSNodes().split("[,]", -1);

			String msg = "";
			for (String nodeID : nodes)
			{
				if (DataType.IsNullOrEmpty(nodeID) == true)
				{
					continue;
				}

				String sql = "SELECT EmpFromT AS Name,EmpFrom AS No FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A  WHERE  A.ActionType=1 AND A.WorkID=" + workid + " AND A.NDFrom=" + nodeID;
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.Count == 0)
				{
					continue;
				}

				for (DataRow dr : dt.Rows)
				{
					String empName = dr.get("Name").toString();
					String empNo = dr.get("No").toString();


					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar = smsDoc.Clone();
					String smsDocReal = tempVar instanceof String ? (String)tempVar : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empName);
					openUrl = openUrl.replace("{EmpStr}", empNo);

					String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node();

					//发送消息
					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel());
					//处理短消息.
					toEmpIDs += empName + ",";
				}
			}
			return "@已向:{" + toEmpIDs + "}发送了短消息提醒.";
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 按照SQL计算
		if (this.getSMSPushWay() == 4)
		{
			String bySQL = this.getBySQL();
			if (DataType.IsNullOrEmpty(getBySQL()) == true)
			{
				return "按照指定的SQL发送消息，SQL数据不能为空";
			}
			bySQL = bySQL.replace("~", "'");
			//替换SQL中的参数
			bySQL = bySQL.replace("@WebUser.No", WebUser.No);
			bySQL = bySQL.replace("@WebUser.Name", WebUser.Name);
			bySQL = bySQL.replace("@WebUser.FK_DeptNameOfFull", WebUser.FK_DeptNameOfFull);
			bySQL = bySQL.replace("@WebUser.FK_DeptName", WebUser.FK_DeptName);
			bySQL = bySQL.replace("@WebUser.FK_Dept", WebUser.FK_Dept);
			/*如果仍然有没有替换下来的变量.*/
			if (bySQL.contains("@") == true)
			{
				bySQL = BP.WF.Glo.DealExp(bySQL, en, null);
			}
			DataTable dt = DBAccess.RunSQLReturnTable(bySQL);
			for (DataRow dr : dt.Rows)
			{
				String empName = dr.get("Name").toString();
				String empNo = dr.get("No").toString();


				// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
				Object tempVar2 = smsDoc.Clone();
				String smsDocReal = tempVar2 instanceof String ? (String)tempVar2 : null;
				smsDocReal = smsDocReal.replace("{EmpStr}", empName);
				openUrl = openUrl.replace("{EmpStr}", empNo);

				String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node();

				//发送消息
				BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel());

				//处理短消息.
				toEmpIDs += empName + ",";
			}

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 按照SQL计算

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 发送给指定的接收人
		if (this.getSMSPushWay() == 5)
		{
			if (DataType.IsNullOrEmpty(this.getByEmps()) == true)
			{
				return "发送给指定的人员，则人员集合不能为空。";
			}
			//以逗号分割开
			String[] toEmps = this.getByEmps().split("[,]", -1);
			for (String empNo : toEmps)
			{
				if (DataType.IsNullOrEmpty(empNo) == true)
				{
					continue;
				}
				BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(empNo);
				// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
				Object tempVar3 = smsDoc.Clone();
				String smsDocReal = tempVar3 instanceof String ? (String)tempVar3 : null;
				smsDocReal = smsDocReal.replace("{EmpStr}", emp.Name);
				openUrl = openUrl.replace("{EmpStr}", emp.No);
				//发送消息
				BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel());
				//处理短消息.
				toEmpIDs += emp.Name + ",";
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 发送给指定的接收人

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 不同的消息事件，接收人不同的处理
		if (this.getSMSPushWay() == 1)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 工作到达、退回、移交、撤销
			if ((this.getFK_Event().equals(BP.Sys.EventListOfNode.WorkArrive) || this.getFK_Event().equals(BP.Sys.EventListOfNode.ReturnAfter) || this.getFK_Event().equals(BP.Sys.EventListOfNode.ShitAfter) || this.getFK_Event().equals(BP.Sys.EventListOfNode.UndoneAfter)) && DataType.IsNullOrEmpty(jumpToEmps) == false)
			{
				/*当前节点的处理人.*/
				toEmpIDs = jumpToEmps;
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
					{
						continue;
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar4 = smsDoc.Clone();
					String smsDocReal = tempVar4 instanceof String ? (String)tempVar4 : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);

					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel());
				}
				return "@已向:{" + toEmpIDs + "}发送提醒信息.";
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 工作到达、退回、移交、撤销

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 节点发送成功后
			if (this.getFK_Event().equals(BP.Sys.EventListOfNode.SendSuccess) && objs.getVarAcceptersID() != null)
			{
				/*如果向接受人发送消息.*/
				toEmpIDs = objs.getVarAcceptersID();
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
					{
						continue;
					}
					if (empNo.equals(WebUser.No))
					{
						continue;
					}
					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar5 = smsDoc.Clone();
					String smsDocReal = tempVar5 instanceof String ? (String)tempVar5 : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;
					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel(), paras);

				}
				return "@已向:{" + toEmpIDs + "}发送提醒信息.";
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 节点发送成功后


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 流程结束后、流程删除后
			if (this.getFK_Event().equals(BP.Sys.EventListOfNode.FlowOverAfter) || this.getFK_Event().equals(BP.Sys.EventListOfNode.AfterFlowDel))
			{
				/*向所有参与人发送消息. */
				DataTable dt = DBAccess.RunSQLReturnTable("SELECT Emps,TodoEmps FROM WF_GenerWorkFlow WHERE WorkID=" + workid);
				if (dt.Rows.Count == 0)
				{
					return "";
				}
				String empsStrs = "";
				for (DataRow dr : dt.Rows)
				{
					empsStrs += dr.get("Emps");
					String todoEmps = dr.get("TodoEmps").toString();
					if (DataType.IsNullOrEmpty(todoEmps) == false)
					{
						String[] strs = todoEmps.split("[;]", -1);
						todoEmps = "";
						for (String str : strs)
						{
							if (DataType.IsNullOrEmpty(str) == true || empsStrs.contains(str) == true)
							{
								continue;
							}
							empsStrs += str.split("[,]", -1)[0] + "@";
						}
					}
				}
				String[] emps = empsStrs.split("[@]", -1);

				for (String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
					{
						continue;
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar6 = smsDoc.Clone();
					String smsDoccReal = tempVar6 instanceof String ? (String)tempVar6 : null;
					smsDoc = smsDoc.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;

					//发送消息
					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel(), paras);
				}
				return "@已向:{" + empsStrs + "}发送提醒信息.";
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 流程结束后、流程删除后

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 节点预警、逾期
			if (this.getFK_Event().equals(BP.Sys.EventListOfNode.NodeWarning) || this.getFK_Event().equals(BP.Sys.EventListOfNode.NodeOverDue))
			{
				//获取当前节点的接收人
				GenerWorkFlow gwf = new GenerWorkFlow(workid);
				String[] emps = gwf.getTodoEmps().split("[;]", -1);
				for (String emp : emps)
				{
					if (DataType.IsNullOrEmpty(emp))
					{
						continue;
					}
					String[] empA = emp.split("[,]", -1);
					if (DataType.IsNullOrEmpty(empA[0]) == true || empA[0].equals(WebUser.No))
					{
						continue;
					}
					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar7 = smsDoc.Clone();
					String smsDocReal = tempVar7 instanceof String ? (String)tempVar7 : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empA[0]);
					openUrl = openUrl.replace("{EmpStr}", empA[0]);
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;
					BP.WF.Dev2Interface.Port_SendMessage(empA[0], smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.No, openUrl, this.getSMSPushModel(), paras);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 节点预警、逾期

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 不同的消息事件，接收人不同的处理

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion  消息发送

		return "";

	}
	/** 
	 发送邮件
	 
	 @param title
	 @param openWorkURl
	 @param en
	 @param jumpToEmps
	 @param currNode
	 @param workid
	 @param objs
	 @param r 处理好的变量集合
	 @return 
	*/
	//private string SendEmail(string title, string openWorkURl, Entity en, string jumpToEmps, Node currNode, Int64 workid, SendReturnObjs objs, Row r)
	//{
	//    if (this.MailPushWay == 0)
	//        return "";

	//    #region 生成相关的变量？
	//    string generAlertMessage = ""; //定义要返回的提示消息.
	//    // 发送邮件.
	//    string mailTitleTmp = ""; //定义标题.
	//    string mailDocTmp = ""; //定义内容.




	//    // 内容.
	//    mailDocTmp = this.MailDoc;
	//    mailDocTmp = mailDocTmp.Replace("{Url}", openWorkURl);
	//    mailDocTmp = mailDocTmp.Replace("{Title}", title);
	//    mailDocTmp = mailDocTmp.Replace("{Title}", title);

	//    mailDocTmp = mailDocTmp.Replace("@WebUser.No", WebUser.No);
	//    mailDocTmp = mailDocTmp.Replace("@WebUser.Name", WebUser.Name);

	//    /*如果仍然有没有替换下来的变量.*/
	//    if (mailDocTmp.Contains("@"))
	//        mailDocTmp = BP.WF.Glo.DealExp(mailDocTmp, en, null);

	//    #endregion 生成相关的变量？

	//    //求发送给的人员 ID.
	//    string toEmpIDs = "";

	//    #region 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.
	//    if (this.MailPushWay == 3)
	//    {
	//        /*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
	//        string[] nodes = this.SMSNodes.Split(',');

	//        string msg = "";
	//        foreach (string nodeID in nodes)
	//        {
	//            if (DataType.IsNullOrEmpty(nodeID) == true)
	//                continue;
	//            if (this.FK_Event == BP.Sys.EventListOfNode.ReturnAfter)
	//            {
	//                //获取退回原因
	//                Paras ps = new Paras();
	//                ps.SQL = "SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "WorkID  ORDER BY RDT DESC";
	//                ps.Add(ReturnWorkAttr.WorkID,Int64.Parse( en.PKVal.ToString()));
	//                DataTable retunWdt = DBAccess.RunSQLReturnTable(ps);
	//                if (retunWdt.Rows.Count != 0)
	//                {
	//                    string returnMsg = retunWdt.Rows[0]["BeiZhu"].ToString();
	//                    string returner = retunWdt.Rows[0]["ReturnerName"].ToString();
	//                    mailDocTmp = mailDocTmp.Replace("ReturnMsg", returnMsg);

	//                }
	//            }

	//            string sql = "SELECT b.Name, b.Email, b.No FROM ND" + int.Parse(this.FK_Flow) + "Track a, WF_Emp b WHERE  a.ActionType=1 AND A.WorkID=" + workid + " AND a.NDFrom=" + nodeID + " AND a.EmpFrom=B.No ";
	//            DataTable dt = DBAccess.RunSQLReturnTable(sql);
	//            if (dt.Rows.Count == 0)
	//                continue;

	//            foreach (DataRow dr in dt.Rows)
	//            {
	//                string emailAddress = dr["Email"].ToString();
	//                string empName = dr["Name"].ToString();
	//                string empNo = dr["No"].ToString();

	//                if (DataType.IsNullOrEmpty(emailAddress))
	//                    continue;

	//                string paras = "@FK_Flow=" + currNode.FK_Flow + "&FK_Node=" + currNode.NodeID + "@WorkID=" + workid;
	//                //发送邮件
	//                BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.FK_Event, this.FK_Event + workid, BP.Web.WebUser.No, null, empNo, paras);
	//                msg += dr["Name"].ToString() + ",";
	//            }
	//        }
	//        return "@已向:{" + msg + "}发送提醒消息.";


	//    }
	//    #endregion 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.

	//    #region WorkArrive-工作到达. - 邮件处理.
	//    if (this.FK_Event == BP.Sys.EventListOfNode.WorkArrive || this.FK_Event == BP.Sys.EventListOfNode.ReturnAfter
	//        || this.FK_Event == BP.Sys.EventListOfNode.NodeWarning || this.FK_Event == BP.Sys.EventListOfNode.NodeOverDue)
	//    {
	//        if (this.FK_Event == BP.Sys.EventListOfNode.ReturnAfter)
	//        {
	//            //获取退回原因
	//            Paras ps = new Paras();
	//            ps.SQL = "SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "WorkID  ORDER BY RDT DESC";
	//            ps.Add(ReturnWorkAttr.WorkID, Int64.Parse( en.PKVal.ToString()));
	//            DataTable retunWdt = DBAccess.RunSQLReturnTable(ps);
	//            if (retunWdt.Rows.Count != 0)
	//            {
	//                string returnMsg = retunWdt.Rows[0]["BeiZhu"].ToString();
	//                string returner = retunWdt.Rows[0]["ReturnerName"].ToString();
	//                mailDocTmp = mailDocTmp.Replace("ReturnMsg", returnMsg);

	//            }
	//        }
	//        /*工作到达.*/
	//        if (this.MailPushWay == 1 && !string.IsNullOrWhiteSpace(jumpToEmps))
	//        {
	//            /*如果向接受人发送邮件.*/
	//            toEmpIDs = jumpToEmps;
	//            string[] emps = toEmpIDs.Split(',');
	//            foreach (string emp in emps)
	//            {
	//                if (DataType.IsNullOrEmpty(emp))
	//                    continue;

	//                // 因为要发给不同的人，所有需要clone 一下，然后替换发送.
	//                string mailDocReal = mailDocTmp.Clone() as string;
	//                mailDocReal = mailDocReal.Replace("{EmpStr}", emp);

	//                //获得当前人的邮件.
	//                BP.WF.Port.WFEmp empEn = new BP.WF.Port.WFEmp(emp);

	//                //发送邮件.
	//                BP.WF.Dev2Interface.Port_SendEmail(empEn.Email, mailTitleTmp, mailDocReal, this.FK_Event,
	//                    "WKAlt" + currNode.NodeID + "_" + workid, BP.Web.WebUser.No, null, emp);
	//            }
	//            return "@已向:{" + toEmpIDs + "}发送提醒信息.";
	//        }

	//        if (this.MailPushWay == 2)
	//        {
	//            /*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
	//            string emailAddress = r[this.MailAddress] as string;

	//            //发送邮件
	//            BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.FK_Event, "WKAlt" + currNode.NodeID + "_" + workid, BP.Web.WebUser.No, null, null);
	//            return "@已向:{" + emailAddress + "}发送提醒信息.";
	//        }
	//    }
	//    #endregion 发送成功事件.

	//    #region SendSuccess - 发送成功事件. - 邮件处理.
	//    if (this.FK_Event == BP.Sys.EventListOfNode.SendSuccess)
	//    {
	//        /*发送成功事件.*/
	//        if (this.MailPushWay == 1 && objs.VarAcceptersID != null)
	//        {
	//            /*如果向接受人发送邮件.*/
	//            toEmpIDs = objs.VarAcceptersID;
	//            string[] emps = toEmpIDs.Split(',');
	//            foreach (string emp in emps)
	//            {
	//                if (DataType.IsNullOrEmpty(emp))
	//                    continue;
	//                if (emp == WebUser.No)
	//                    continue;

	//                // 因为要发给不同的人，所有需要clone 一下，然后替换发送.
	//                string mailDocReal = mailDocTmp.Clone() as string;
	//                mailDocReal = mailDocReal.Replace("{EmpStr}", emp);

	//                //获得当前人的邮件.
	//                BP.WF.Port.WFEmp empEn = new BP.WF.Port.WFEmp(emp);

	//                string paras = "@FK_Flow=" + currNode.FK_Flow + "&FK_Node=" + currNode.NodeID + "@WorkID=" + workid;

	//                string mail = empEn.Email;
	//                if (DataType.IsNullOrEmpty(mail) == true)
	//                {
	//                    BP.GPM.Emp empGPM = new GPM.Emp(emp);
	//                    mail = empGPM.Email;
	//                    empEn.Email = mail;
	//                    empEn.Update();
	//                }

	//                //发送邮件.
	//                BP.WF.Dev2Interface.Port_SendEmail(mail, mailTitleTmp, mailDocReal, this.FK_Event, "WKAlt" + objs.VarToNodeID + "_" + workid, BP.Web.WebUser.No, null, emp, paras);

	//            }
	//            return "@已向:{" + toEmpIDs + "}发送提醒信息.";
	//        }

	//        if (this.MailPushWay == 2)
	//        {
	//            /*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
	//            string emailAddress = r[this.MailAddress] as string;
	//            string paras = "@FK_Flow=" + currNode.FK_Flow + "&FK_Node=" + currNode.NodeID + "@WorkID=" + workid;

	//            //发送邮件
	//            BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.FK_Event, "WKAlt" + objs.VarToNodeID + "_" + workid, BP.Web.WebUser.No, null, null, paras);

	//            return "@已向:{" + emailAddress + "}发送提醒信息.";
	//        }
	//    }
	//    #endregion 发送成功事件.



	//    #region SendSuccess - 流程结束/预警/逾期. - 邮件处理.
	//    if (this.FK_Event == BP.Sys.EventListOfNode.FlowOverAfter
	//        || this.FK_Event == BP.Sys.EventListOfNode.FlowWarning
	//        || this.FK_Event == BP.Sys.EventListOfNode.FlowOverDue)
	//    {
	//        /*发送成功事件.*/
	//        if (this.MailPushWay == 1)
	//        {
	//            /*如果向接受人发送邮件.*/
	//            /*向所有参与人. */
	//            string empsStrs = DBAccess.RunSQLReturnStringIsNull("SELECT Emps FROM WF_GenerWorkFlow WHERE WorkID=" + workid, "");
	//            string[] emps = empsStrs.Split('@');

	//            foreach (string emp in emps)
	//            {
	//                if (DataType.IsNullOrEmpty(emp))
	//                    continue;

	//                // 因为要发给不同的人，所有需要clone 一下，然后替换发送.
	//                string mailDocReal = mailDocTmp.Clone() as string;
	//                mailDocReal = mailDocReal.Replace("{EmpStr}", emp);

	//                //获得当前人的邮件.
	//                BP.WF.Port.WFEmp empEn = new BP.WF.Port.WFEmp(emp);

	//                string paras = "@FK_Flow=" + currNode.FK_Flow + "&FK_Node=" + currNode.NodeID + "@WorkID=" + workid;

	//                //发送邮件.
	//                BP.WF.Dev2Interface.Port_SendEmail(empEn.Email, mailTitleTmp, mailDocReal, this.FK_Event, "FlowOver" + workid, BP.Web.WebUser.No, null, emp, paras);
	//            }
	//            return "@已向:{" + toEmpIDs + "}发送提醒信息.";
	//        }

	//        if (this.MailPushWay == 2)
	//        {
	//            /*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
	//            string emailAddress = r[this.MailAddress] as string;

	//            string paras = "@FK_Flow=" + currNode.FK_Flow + "&FK_Node=" + currNode.NodeID + "@WorkID=" + workid;

	//            //发送邮件
	//            BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.FK_Event, "FlowOver" + workid, BP.Web.WebUser.No, null, null, paras);
	//            return "@已向:{" + emailAddress + "}发送提醒信息.";
	//        }
	//    }
	//    #endregion 发送成功事件.

	//    return generAlertMessage;
	//}
	/** 
	 发送短消息.
	 
	 @param title
	 @param openWorkURl
	 @param en
	 @param jumpToEmps
	 @param currNode
	 @param workid
	 @param objs
	 @param r 处理好的变量集合
	 @return 
	*/
	//private string SendShortMessageToSpecNodes(string title, string openWorkURL, Entity en, Node currNode, Int64 workid, SendReturnObjs objs, Row r, string SendToEmpIDs)
	//{
	//    if (this.SMSPushWay == 0)
	//        return "";

	//    //定义短信内容.......
	//    string smsDocTmp = "";

	//    #region  生成短信内容
	//    smsDocTmp = this.SMSDoc.Clone() as string;
	//    smsDocTmp = smsDocTmp.Replace("{Title}", title);
	//    smsDocTmp = smsDocTmp.Replace("{Url}", openWorkURL);
	//    smsDocTmp = smsDocTmp.Replace("@WebUser.No", WebUser.No);
	//    smsDocTmp = smsDocTmp.Replace("@WebUser.Name", WebUser.Name);
	//    smsDocTmp = smsDocTmp.Replace("@WorkID", en.PKVal.ToString());
	//    smsDocTmp = smsDocTmp.Replace("@OID", en.PKVal.ToString());

	//    /*如果仍然有没有替换下来的变量.*/
	//    if (smsDocTmp.Contains("@") == true)
	//        smsDocTmp = BP.WF.Glo.DealExp(smsDocTmp, en, null);

	//    /*如果仍然有没有替换下来的变量.*/
	//    if (smsDocTmp.Contains("@"))
	//        smsDocTmp = BP.WF.Glo.DealExp(smsDocTmp, en, null);

	//    //if (smsDocTmp.Contains("@"))
	//    //    throw new Exception("@短信消息内容配置错误,里面有未替换的变量，请确认参数是否正确:"+smsDocTmp);

	//    string toEmpIDs = "";
	//    #endregion 处理当前的内容.

	//    #region 如果发送给指定的节点处理人,就计算出来直接退回,任何方式的处理人都是一致的.
	//    if (this.SMSPushWay == 3)
	//    {
	//        /*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
	//        string[] nodes = this.SMSNodes.Split(',');

	//        string msg = "";
	//        foreach (string nodeID in nodes)
	//        {
	//            if (DataType.IsNullOrEmpty(nodeID) == true)
	//                continue;

	//            string sql = "SELECT b.Name, b.Tel ,b.No FROM ND" + int.Parse(this.FK_Flow) + "Track a, WF_Emp b WHERE  a.ActionType=1 AND A.WorkID=" + workid + " AND a.NDFrom=" + nodeID + " AND a.EmpFrom=B.No ";
	//            DataTable dt = DBAccess.RunSQLReturnTable(sql);
	//            if (dt.Rows.Count == 0)
	//                continue;

	//            if (this.FK_Event == BP.Sys.EventListOfNode.ReturnAfter)
	//            {
	//                //获取退回原因
	//                Paras ps = new Paras();
	//                ps.SQL = "SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "WorkID  ORDER BY RDT DESC";
	//                ps.Add(ReturnWorkAttr.WorkID, Int64.Parse( en.PKVal.ToString()));
	//                DataTable retunWdt = DBAccess.RunSQLReturnTable(ps);
	//                if (retunWdt.Rows.Count != 0)
	//                {
	//                    string returnMsg = retunWdt.Rows[0]["BeiZhu"].ToString();
	//                    string returner = retunWdt.Rows[0]["ReturnerName"].ToString();
	//                    smsDocTmp = smsDocTmp.Replace("ReturnMsg", returnMsg);

	//                }
	//            }


	//            foreach (DataRow dr in dt.Rows)
	//            {
	//                string tel = dr["Tel"].ToString();
	//                string empName = dr["Name"].ToString();
	//                string empNo = dr["No"].ToString();

	//                if (DataType.IsNullOrEmpty(tel))
	//                    continue;

	//                // 因为要发给不同的人，所有需要clone 一下，然后替换发送.
	//                string mailDocReal = smsDocTmp.Clone() as string;
	//                mailDocReal = mailDocReal.Replace("{EmpStr}", empName);
	//                openWorkURL = openWorkURL.Replace("{EmpStr}", empName);

	//                string paras = "@FK_Flow=" + this.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + this.FK_Node;

	//                //发送邮件.
	//                BP.WF.Dev2Interface.Port_SendSMS(tel, mailDocReal, this.FK_Event, "WKAlt" + currNode.NodeID + "_" + workid, WebUser.No, null, empNo, paras, title, openWorkURL, this.SMSPushModel);

	//                //处理短消息.
	//                toEmpIDs += empName + ",";
	//            }
	//        }
	//        return "@已向:{" + toEmpIDs + "}发送了短消息提醒.";
	//    }
	//    #endregion 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.

	//    //求发送给的人员ID.
	//    #region WorkArrive - 工作到达事件.
	//    if (this.FK_Event == BP.Sys.EventListOfNode.WorkArrive
	//        || this.FK_Event == BP.Sys.EventListOfNode.ReturnAfter
	//        || this.FK_Event == BP.Sys.EventListOfNode.NodeWarning 
	//        || this.FK_Event == BP.Sys.EventListOfNode.NodeOverDue)
	//    {
	//        if (this.FK_Event == BP.Sys.EventListOfNode.ReturnAfter)
	//        {
	//            //获取退回原因
	//            Paras ps = new Paras();
	//            ps.SQL = "SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "WorkID  ORDER BY RDT DESC";
	//            ps.Add(ReturnWorkAttr.WorkID, Int64.Parse( en.PKVal.ToString()));
	//            DataTable dt = DBAccess.RunSQLReturnTable(ps);
	//            if (dt.Rows.Count != 0)
	//            {
	//                string returnMsg = dt.Rows[0]["BeiZhu"].ToString();
	//                string returner = dt.Rows[0]["ReturnerName"].ToString();
	//                smsDocTmp = smsDocTmp.Replace("ReturnMsg", returnMsg);

	//            }
	//        }
	//        /*发送成功事件, 退回后事件. */
	//        if (this.SMSPushWay == 1)
	//        {
	//            /*如果向接受人发送短信.*/
	//            toEmpIDs = SendToEmpIDs;
	//            if (DataType.IsNullOrEmpty(toEmpIDs) == false)
	//            {
	//                string[] emps = toEmpIDs.Split(',');
	//                foreach (string emp in emps)
	//                {
	//                    if (DataType.IsNullOrEmpty(emp))
	//                        continue;

	//                    string smsDocTmpReal = smsDocTmp.Clone() as string;
	//                    smsDocTmpReal = smsDocTmpReal.Replace("{EmpStr}", emp);
	//                    openWorkURL = openWorkURL.Replace("{EmpStr}", emp);
	//                    BP.WF.Port.WFEmp empEn = new Port.WFEmp(emp);

	//                    string paras = "@FK_Flow=" + currNode.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + currNode.NodeID;

	//                    //发送短信.
	//                    Dev2Interface.Port_SendSMS(empEn.Tel, smsDocTmpReal, this.FK_Event, "WKAlt" + currNode.NodeID + "_" + workid, BP.Web.WebUser.No, null, emp, null, title, openWorkURL, this.SMSPushModel);
	//                }
	//                //return "@已向:{" + toEmpIDs + "}发送提醒手机短信，由 " + this.FK_Event + " 发出.";
	//                return "@已向:{" + toEmpIDs + "}发送提醒消息.";
	//            }
	//        }

	//        if (this.SMSPushWay == 2)
	//        {
	//            /*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
	//            string tel = r[this.SMSField] as string;

	//            //发送短信.
	//            string paras = "@FK_Flow=" + currNode.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + currNode.NodeID;
	//            BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, this.FK_Event, "WKAlt" + currNode.NodeID + "_" + workid, BP.Web.WebUser.No, null, paras, title, openWorkURL, this.SMSPushModel);
	//            return "@已向:{" + tel + "}发送提醒消息.";
	//            //  return "@已向:{" + tel + "}发送提醒手机短信，由 " + this.FK_Event + " 发出.";

	//        }
	//    }
	//    #endregion WorkArrive - 工作到达事件

	//    #region SendSuccess - 发送成功事件
	//    if (this.FK_Event == BP.Sys.EventListOfNode.SendSuccess)
	//    {
	//        /*发送成功事件.*/
	//        if (this.SMSPushWay == 1 && objs.VarAcceptersID != null)
	//        {
	//            /*如果向接受人发送短信.*/
	//            toEmpIDs = objs.VarAcceptersID;

	//            toEmpIDs = toEmpIDs.Replace("(", "");
	//            toEmpIDs = toEmpIDs.Replace(")", "");

	//            string hasSendEmps = ",";

	//            if (DataType.IsNullOrEmpty(toEmpIDs) == false)
	//            {
	//                #warning 人员是（zhangyifan,张一凡） 获取结果有问题
	//                string[] emps = toEmpIDs.Split(';');
	//                foreach (string empID in emps)
	//                {
	//                    if (DataType.IsNullOrEmpty(empID))
	//                        continue;
	//                    string[] empIDs = empID.Split(',');
	//                    if (DataType.IsNullOrEmpty(empIDs[0]))
	//                        continue;

	//                    if (hasSendEmps.Contains("," + empIDs[0] + ",") == true)
	//                        continue;

	//                    hasSendEmps += empIDs[0] + ",";

	//                    string smsDocTmpReal = smsDocTmp.Clone() as string;
	//                    smsDocTmpReal = smsDocTmpReal.Replace("{EmpStr}", empIDs[0]);
	//                    openWorkURL = openWorkURL.Replace("{EmpStr}", empIDs[0]);

	//                    string myEmpID = empIDs[0];
	//                    BP.GPM.Emp empEn = new BP.GPM.Emp();
	//                    empEn.No = myEmpID;
	//                    if (empEn.RetrieveFromDBSources() == 0)
	//                        continue;

	//                    string paras = "@FK_Flow=" + currNode.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + currNode.NodeID;

	//                    //发送短信.
	//                    Dev2Interface.Port_SendSMS(empEn.Tel, smsDocTmpReal, this.FK_Event, "WKAlt" + objs.VarToNodeID + "_" + workid, BP.Web.WebUser.No, null, empID, paras, title, openWorkURL, this.SMSPushModel);
	//                }
	//                return "";
	//                //return "@已向:{" + toEmpIDs + "}发送提醒消息.";
	//            }
	//        }

	//        if (this.SMSPushWay == 2)
	//        {
	//            /*如果向指定的字段作为发送短信的发送对象, 从字段里取数据. */
	//            string tel = r[this.SMSField] as string;
	//            if (tel != null || tel.Length > 6)
	//            {

	//                string paras = "@FK_Flow=" + currNode.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + currNode.NodeID;

	//                //发送短信.
	//                BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, this.FK_Event, "WKAlt" + objs.VarToNodeID + "_" + workid, BP.Web.WebUser.No, null, paras, title, openWorkURL, this.SMSPushModel);
	//                return "@已向:{" + tel + "}发送提醒手机短信.";
	//            }
	//        }
	//    }
	//    #endregion SendSuccess - 发送成功事件

	//    #region FlowOverAfter -  流程结束/预警/逾期  短信息事件
	//    if (this.FK_Event == BP.Sys.EventListOfNode.FlowOverAfter
	//        || this.FK_Event ==BP.Sys.EventListOfNode.FlowWarning
	//        || this.FK_Event == BP.Sys.EventListOfNode.FlowOverDue)
	//    {
	//        /*发送成功事件.*/
	//        if (this.SMSPushWay == 1)
	//        {
	//            /*向所有参与人. */
	//            string empsStrs = DBAccess.RunSQLReturnStringIsNull("SELECT Emps FROM WF_GenerWorkFlow WHERE WorkID=" + workid, "");
	//            if (DataType.IsNullOrEmpty(empsStrs) == false)
	//            {
	//                string[] emps = empsStrs.Split('@');
	//                foreach (string empID in emps)
	//                {
	//                    if (DataType.IsNullOrEmpty(empID))
	//                        continue;

	//                    if (empID == WebUser.No)
	//                        continue;

	//                    string smsDocTmpReal = smsDocTmp.Clone() as string;
	//                    smsDocTmpReal = smsDocTmpReal.Replace("{EmpStr}", empID);
	//                    openWorkURL = openWorkURL.Replace("{EmpStr}", empID);

	//                    BP.WF.Port.WFEmp empEn = new Port.WFEmp();
	//                    empEn.No = empID;
	//                    empEn.RetrieveFromDBSources();

	//                    string paras = "@FK_Flow=" + currNode.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + currNode.NodeID;

	//                    //发送短信.
	//                    Dev2Interface.Port_SendSMS(empEn.Tel, smsDocTmpReal, this.FK_Event, "FlowOver" + workid, BP.Web.WebUser.No, null, empID, paras, title, openWorkURL, this.SMSPushModel);
	//                }

	//                return "";
	//                //return "@已向:{" + toEmpIDs + "}发送提醒消息.";

	//            }
	//        }

	//        if (this.SMSPushWay == 2)
	//        {
	//            /*如果向指定的字段作为发送短信的发送对象, 从字段里取数据. */
	//            string tel = r[this.SMSField] as string;
	//            if (tel != null || tel.Length > 6)
	//            {
	//                string paras = "@FK_Flow=" + currNode.FK_Flow + "@WorkID=" + workid + "@FK_Node=" + currNode.NodeID;
	//                //发送短信.
	//                BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, this.FK_Event, "FlowOver" + workid, BP.Web.WebUser.No, null, paras, title, openWorkURL, this.SMSPushModel);
	//                return "@已向:{" + tel + "}发送提醒消息.";
	//            }
	//        }
	//    }
	//    #endregion SendSuccess - 发送成功事件

	//    return "";
	//}

	/** 
	 发送短信到其它节点的处理人.
	*/
	private void SendShortMessageToSpecNodeWorks()
	{
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		//  this.MyPK = this.FK_Event + "_" + this.FK_Node + "_" + this.PushWay;

		String sql = "UPDATE WF_PushMsg SET FK_Flow=(SELECT FK_Flow FROM WF_Node WHERE NodeID= WF_PushMsg.FK_Node)";
		BP.DA.DBAccess.RunSQL(sql);

		return super.beforeUpdateInsertAction();
	}
}