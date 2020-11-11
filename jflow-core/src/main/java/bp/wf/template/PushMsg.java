package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.web.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.wf.*;

/** 
 消息推送
*/
public class PushMsg extends EntityMyPK
{

		///基本属性
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.FK_Flow, value);
	}
	/** 
	 事件
	*/
	public final String getFK_Event()throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Event);
	}
	public final void setFK_Event(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.FK_Event, value);
	}
	/** 
	 推送方式.
	*/
	public final int getPushWay()throws Exception
	{
		return this.GetValIntByKey(PushMsgAttr.PushWay);
	}
	public final void setPushWay(int value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.PushWay, value);
	}
	/** 
	节点
	*/
	public final int getFK_Node()throws Exception
	{
		return this.GetValIntByKey(PushMsgAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.FK_Node, value);
	}
	public final String getPushDoc()throws Exception
	{
		String s = this.GetValStringByKey(PushMsgAttr.PushDoc);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setPushDoc(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.PushDoc, value);
	}
	public final String getTag()throws Exception
	{
		String s = this.GetValStringByKey(PushMsgAttr.Tag);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setTag(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.Tag, value);
	}

		///


		///事件消息.
	/** 
	 邮件推送方式
	*/
	public final int getMailPushWay()throws Exception
	{
		return this.GetValIntByKey(PushMsgAttr.MailPushWay);
	}
	public final void setMailPushWay(int value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.MailPushWay, value);
	}
	/** 
	 推送方式Name
	*/
	public final String getMailPushWayText()throws Exception
	{
		if (this.getFK_Event().equals(EventListNode.WorkArrive))
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

		if (this.getFK_Event().equals(EventListNode.SendSuccess))
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

		if (this.getFK_Event().equals(EventListNode.ReturnAfter))
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
	public final String getMailAddress()throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.MailAddress);
	}
	public final void setMailAddress(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.MailAddress, value);
	}
	/** 
	 邮件标题.
	*/
	public final String getMailTitle()throws Exception
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}
		switch (this.getFK_Event())
		{
			case EventListNode.WorkArrive:
				return "新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListNode.SendSuccess:
				return "新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListNode.ShitAfter:
				return "移交来的新工作{{Title}},移交人@WebUser.No,@WebUser.Name";
			case EventListNode.ReturnAfter:
				return "被退回来{{Title}},退回人@WebUser.No,@WebUser.Name";
			case EventListNode.UndoneAfter:
				return "工作被撤销{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListNode.AskerReAfter:
				return "加签新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
			case EventListFlow.FlowOverAfter:
				return "流程{{Title}}已经结束,处理人@WebUser.No,@WebUser.Name";
			case EventListFlow.AfterFlowDel:
				return "流程{{Title}}已经删除,处理人@WebUser.No,@WebUser.Name";
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
	}
	/** 
	 Email节点s
	 * @throws Exception 
	*/
	public final String getMailNodes() throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.MailNodes);
	}
	public final void setMailNodes(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.MailNodes, value);
	}
	/** 
	 邮件标题
	*/
	public final String getMailTitleReal() throws Exception
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		return str;
	}
	public final void setMailTitle_Real(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.MailTitle, value);
	}
	/** 
	 邮件内容
	*/
	public final String getMailDocReal() throws Exception
	{
		return this.GetValStrByKey(PushMsgAttr.MailDoc);
	}
	public final void setMailDoc_Real(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.MailDoc, value);
	}
	public final String getMailDoc() throws Exception
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailDoc);
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}
		switch (this.getFK_Event())
		{
			case EventListNode.WorkArrive:
				str += "\t\n您好:";
				str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作报告{Url} .";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n致! ";
				str += "\t\n    @WebUser.No, @WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.SendSuccess:

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
			case EventListNode.ReturnAfter:
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
			case EventListNode.ShitAfter:
				str += "\t\n 您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.UndoneAfter:
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作报告{Url} .";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.AskerReAfter: //加签.
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开报告{Url} .";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n ";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListFlow.FlowOverAfter: //流程结束后.
				str += "\t\n您好:";
				str += "\t\n    工作{{Title}}已经结束, 点击这里打开工作报告{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
		return str;
	}
	/** 
	 短信接收人字段
	 * @throws Exception 
	*/
	public final String getSMSField() throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.SMSField);
	}
	public final void setSMSField(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.SMSField, value);
	}
	public final String getSMSNodes() throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.SMSNodes);
	}
	public final void setSMSNodes(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.SMSNodes, value);
	}
	public final String getSMSPushModel() throws Exception
	{
		return this.GetValStringByKey(PushMsgAttr.SMSPushModel);
	}
	public final void setSMSPushModel(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.SMSPushModel, value);
	}
	/** 
	 短信提醒方式
	*/
	public final int getSMSPushWay() throws Exception
	{
		return this.GetValIntByKey(PushMsgAttr.SMSPushWay);
	}
	public final void setSMSPushWay(int value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.SMSPushWay, value);
	}
	/** 
	 发送消息标签
	*/
	public final String getSMSPushWayText() throws Exception
	{
		if (this.getFK_Event().equals(EventListNode.WorkArrive))
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

		if (this.getFK_Event().equals(EventListNode.SendSuccess))
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

		if (this.getFK_Event().equals(EventListNode.ReturnAfter))
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

		if (this.getFK_Event().equals(EventListFlow.FlowOverAfter))
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
	public final String getSMSDocReal() throws Exception
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		return str;
	}
	public final void setSMSDoc_Real(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	/** 
	 短信模版内容
	*/
	public final String getSMSDoc() throws Exception
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}

		switch (this.getFK_Event())
		{
			case EventListNode.WorkArrive:
			case EventListNode.SendSuccess:
				str = "有新工作{{Title}}需要您处理, 发送人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListNode.ReturnAfter:
				str = "工作{{Title}}被退回,退回人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListNode.ShitAfter:
				str = "移交工作{{Title}},移交人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListNode.UndoneAfter:
				str = "工作撤销{{Title}},撤销人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			case EventListNode.AskerReAfter: //加签.
				str = "工作加签{{Title}},加签人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			case EventListFlow.FlowOverAfter: //加签.
				str = "流程{{Title}}已经结束,最后处理人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
		return str;
	}
	public final void setSMSDoc(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	/** 
	 按照指定的SQL
	*/
	public final String getBySQL() throws Exception
	{
		return this.GetValStrByKey(PushMsgAttr.BySQL);
	}
	public final void setBySQL(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.BySQL, value);
	}
	/** 
	 发送给指定的人员，人员之间以逗号分割
	*/
	public final String getByEmps() throws Exception
	{
		return this.GetValStrByKey(PushMsgAttr.ByEmps);
	}
	public final void setByEmps(String value) throws Exception
	{
		this.SetValByKey(PushMsgAttr.ByEmps, value);
	}

		///


		///构造方法
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
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_PushMsg", "消息推送");

		map.AddMyPK();

		map.AddTBString(PushMsgAttr.FK_Flow, null, "流程", true, false, 0, 5, 10);
		map.AddTBInt(PushMsgAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(PushMsgAttr.FK_Event, null, "事件类型", true, false, 0, 20, 10);


			///将要删除.
		map.AddDDLSysEnum(PushMsgAttr.PushWay, 0, "推送方式", true, false, PushMsgAttr.PushWay, "@0=按照指定节点的工作人员@1=按照指定的工作人员@2=按照指定的工作岗位@3=按照指定的部门@4=按照指定的SQL@5=按照系统指定的字段");
			//设置内容.
		map.AddTBString(PushMsgAttr.PushDoc, null, "推送保存内容", true, false, 0, 3500, 10);
		map.AddTBString(PushMsgAttr.Tag, null, "Tag", true, false, 0, 500, 10);

			/// 将要删除.


			///短消息.
		map.AddTBInt(PushMsgAttr.SMSPushWay, 0, "短消息发送方式", true, true);
		map.AddTBString(PushMsgAttr.SMSField, null, "短消息字段", true, false, 0, 100, 10);
		map.AddTBStringDoc(PushMsgAttr.SMSDoc, null, "短消息内容模版", true, false, true);
		map.AddTBString(PushMsgAttr.SMSNodes, null, "SMS节点s", true, false, 0, 100, 10);

			// 邮件,站内消息,短信,钉钉,微信,WebServices.
		map.AddTBString(PushMsgAttr.SMSPushModel, "", "短消息发送设置", true, false, 0, 50, 10);

			/// 短消息.


			///邮件.
		map.AddTBInt(PushMsgAttr.MailPushWay, 0, "邮件发送方式", true, true);
		map.AddTBString(PushMsgAttr.MailAddress, null, "邮件字段", true, false, 0, 100, 10);
		map.AddTBString(PushMsgAttr.MailTitle, null, "邮件标题模版", true, false, 0, 200, 20, true);
		map.AddTBStringDoc(PushMsgAttr.MailDoc, null, "邮件内容模版", true, false, true);
		map.AddTBString(PushMsgAttr.MailNodes, null, "Mail节点s", true, false, 0, 100, 10);

			/// 邮件.

		map.AddTBString(PushMsgAttr.BySQL, null, "按照SQL计算", true, false, 0, 500, 10);
		map.AddTBString(PushMsgAttr.ByEmps, null, "发送给指定的人员", true, false, 0, 100, 10);
		map.AddTBAtParas(500);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///


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


	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode, String jumpToEmps) throws Exception
	{
		if (en == null)
		{
			return "";
		}


			///处理参数.
		Row r = en.getRow();
		try
		{
			//系统参数.
			r.SetValByKey("FK_MapData", en.getClassID());
		}
		catch (java.lang.Exception e)
		{
			r.put("FK_MapData",  en.getClassID());
		}

		if (atPara != null)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.getHisHT().keySet())
			{
				try
				{
					r.SetValByKey(s, ap.GetValStrByKey(s));
				}
				catch (java.lang.Exception e2)
				{
					r.put(s,ap.GetValStrByKey(s));
				}
			}
		}

		//生成标题.
		long workid = Long.parseLong(en.getPKVal().toString());
		String title = "标题";
		if (en.getRow().containsKey("Title") == true)
		{
			title = en.GetValStringByKey("Title"); // 获得工作标题.
			if (DataType.IsNullOrEmpty(title))
			{
				title = DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.getPKVal(), "标题");
			}
		}
		else
		{
			title = DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.getPKVal(), "标题");
		}

		//生成URL.
		String hostUrl = bp.wf.Glo.getHostURL();

		String sid = "{EmpStr}_" + workid + "_" + currNode.getNodeID() + "_" + DBAccess.GenerGUID();
		String openWorkURl = hostUrl + "WF/Do.htm?DoType=OF&SID=" + sid;
		openWorkURl = openWorkURl.replace("//", "/");
		openWorkURl = openWorkURl.replace("http:/", "http://");

			///

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
	 * @throws Exception 
	*/
	private String SendMessage(String title, Entity en, Node currNode, long workid, String jumpToEmps, String openUrl, SendReturnObjs objs, Row r) throws Exception
	{
		//不启用消息.
		if (this.getSMSPushWay() == 0)
		{
			return "";
		}

		String atParas = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@NodeID=" + currNode.getNodeID() + "@FK_Node=" + currNode.getNodeID();
		String generAlertMessage = ""; //定义要返回的提示消息.
		String mailTitle = this.getMailTitle(); // 邮件标题.
		String smsDoc = this.getSMSDoc(); //消息模板.


			///邮件标题
		mailTitle = this.getMailTitle();
		mailTitle = mailTitle.replace("{Title}", title);
		mailTitle = mailTitle.replace("@WebUser.No", WebUser.getNo());
		mailTitle = mailTitle.replace("@WebUser.Name", WebUser.getName());

			/// 邮件标题


			/// 处理消息内容
		smsDoc = smsDoc.replace("{Title}", title);
		smsDoc = smsDoc.replace("{Url}", openUrl);
		smsDoc = smsDoc.replace("@WebUser.No", WebUser.getNo());
		smsDoc = smsDoc.replace("@WebUser.Name", WebUser.getName());
		smsDoc = smsDoc.replace("@WorkID", en.getPKVal().toString());
		smsDoc = smsDoc.replace("@OID", en.getPKVal().toString());

		/*如果仍然有没有替换下来的变量.*/
		if (smsDoc.contains("@") == true)
		{
			smsDoc = bp.wf.Glo.DealExp(smsDoc, en, null);
		}

		if (this.getFK_Event().equals(EventListNode.ReturnAfter))
		{
			//获取退回原因
			Paras ps = new Paras();
			ps.SQL="SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID  ORDER BY RDT DESC";
			ps.Add(ReturnWorkAttr.WorkID, Long.parseLong(en.getPKVal().toString()));
			DataTable retunWdt = DBAccess.RunSQLReturnTable(ps);
			if (retunWdt.Rows.size() != 0)
			{
				String returnMsg = retunWdt.Rows.get(0).getValue("BeiZhu").toString();
				String returner = retunWdt.Rows.get(0).getValue("ReturnerName").toString();
				smsDoc = smsDoc.replace("ReturnMsg", returnMsg);
			}
		}

			/// 处理消息内容

		String toEmpIDs = "";


			///表单字段作为接受人
		if (this.getSMSPushWay() == 2)
		{
			/*从字段里取数据. */
			String toEmp = r.GetValStrByKey(this.getSMSField()) instanceof String ? r.GetValStrByKey(this.getSMSField()) : null;
			//修改内容
			smsDoc = smsDoc.replace("{EmpStr}", toEmp);
			openUrl = openUrl.replace("{EmpStr}", toEmp);

			//发送消息
			bp.wf.Dev2Interface.Port_SendMessage(toEmp, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);
			return "@已向:{" + toEmp + "}发送提醒信息.";
		}

			/// 表单字段作为接受人


			///如果发送给指定的节点处理人,就计算出来直接退回,任何方式的处理人都是一致的.
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
				if (dt.Rows.size() == 0)
				{
					continue;
				}

				for (DataRow dr : dt.Rows)
				{
					String empName = dr.getValue("Name").toString();
					String empNo = dr.getValue("No").toString();


					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar = smsDoc;
					String smsDocReal = tempVar instanceof String ? (String)tempVar : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empName);
					openUrl = openUrl.replace("{EmpStr}", empNo);

					String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node() + "_" + empNo;

					//发送消息.
					bp.wf.Dev2Interface.Port_SendMessage(empNo, smsDocReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);
					//处理短消息.
					toEmpIDs += empName + ",";
				}
			}
			return "@已向:{" + toEmpIDs + "}发送了短消息提醒.";
		}

			/// 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.


			///按照SQL计算
		if (this.getSMSPushWay() == 4)
		{
			String bySQL = this.getBySQL();
			if (DataType.IsNullOrEmpty(getBySQL()) == true)
			{
				return "按照指定的SQL发送消息，SQL数据不能为空";
			}

			bySQL = bySQL.replace("~", "'");
			//替换SQL中的参数
			bySQL = bySQL.replace("@WebUser.No", WebUser.getNo());
			bySQL = bySQL.replace("@WebUser.Name", WebUser.getName());
			bySQL = bySQL.replace("@WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
			bySQL = bySQL.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
			bySQL = bySQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			/*如果仍然有没有替换下来的变量.*/
			if (bySQL.contains("@") == true)
			{
				bySQL = bp.wf.Glo.DealExp(bySQL, en, null);
			}
			DataTable dt = DBAccess.RunSQLReturnTable(bySQL);
			for (DataRow dr : dt.Rows)
			{
				String empName = dr.getValue("Name").toString();
				String empNo = dr.getValue("No").toString();

				// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
				Object tempVar2 = smsDoc;
				String smsDocReal = tempVar2 instanceof String ? (String)tempVar2 : null;
				smsDocReal = smsDocReal.replace("{EmpStr}", empName);
				openUrl = openUrl.replace("{EmpStr}", empNo);

				String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node() + "_" + empNo;

				//发送消息
				bp.wf.Dev2Interface.Port_SendMessage(empNo, smsDocReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);

				//处理短消息.
				toEmpIDs += empName + ",";
			}
		}

			/// 按照SQL计算


			///发送给指定的接收人
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
				bp.wf.port.WFEmp emp = new bp.wf.port.WFEmp(empNo);
				// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
				Object tempVar3 = smsDoc;
				String smsDocReal = tempVar3 instanceof String ? (String)tempVar3 : null;
				smsDocReal = smsDocReal.replace("{EmpStr}", emp.getName());
				openUrl = openUrl.replace("{EmpStr}", emp.getNo());
				//发送消息
				bp.wf.Dev2Interface.Port_SendMessage(empNo, smsDocReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);
				//处理短消息.
				toEmpIDs += emp.getName() + ",";
			}
		}

			/// 发送给指定的接收人


			///不同的消息事件，接收人不同的处理
		if (this.getSMSPushWay() == 1)
		{

				///工作到达、退回、移交、撤销
			if ((this.getFK_Event().equals(EventListNode.WorkArrive) || this.getFK_Event().equals(EventListNode.ReturnAfter) || this.getFK_Event().equals(EventListNode.ShitAfter) || this.getFK_Event().equals(EventListNode.UndoneAfter)) && DataType.IsNullOrEmpty(jumpToEmps) == false)
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
					Object tempVar4 = smsDoc;
					String smsDocReal = tempVar4 instanceof String ? (String)tempVar4 : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);

					bp.wf.Dev2Interface.Port_SendMessage(empNo, smsDocReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒信息.";
			}

				/// 工作到达、退回、移交、撤销


				///节点发送成功后
			if (this.getFK_Event().equals(EventListNode.SendSuccess) && objs != null && objs.getVarAcceptersID() != null)
			{
				/*如果向接受人发送消息.*/
				toEmpIDs = objs.getVarAcceptersID();
				String toEmpNames = objs.getVarAcceptersName();
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
					{
						continue;
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar5 = smsDoc;
					String smsDocReal = tempVar5 instanceof String ? (String)tempVar5 : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);
					bp.wf.Dev2Interface.Port_SendMessage(empNo, smsDocReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);

				}
				return "@已向:{" + toEmpNames + "}发送提醒信息.";
			}

				/// 节点发送成功后


				///流程结束后、流程删除后
			if (this.getFK_Event().equals(EventListFlow.FlowOverAfter) || this.getFK_Event().equals(EventListFlow.AfterFlowDel))
			{
				/*向所有参与人发送消息. */
				DataTable dt = DBAccess.RunSQLReturnTable("SELECT Emps,TodoEmps FROM WF_GenerWorkFlow WHERE WorkID=" + workid);
				if (dt.Rows.size() == 0)
				{
					return "";
				}
				String empsStrs = "";
				for (DataRow dr : dt.Rows)
				{
					empsStrs += dr.getValue("Emps");
					String todoEmps = dr.getValue("TodoEmps").toString();
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
				String empNo = "";
				for (String str : emps)
				{
					if (DataType.IsNullOrEmpty(str))
					{
						continue;
					}

					empNo = str;
					if (str.indexOf(",") != -1)
					{
						empNo = str.split("[,]", -1)[0];
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar6 = smsDoc;
					String smsDoccReal = tempVar6 instanceof String ? (String)tempVar6 : null;
					smsDoccReal = smsDoccReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);
					//发送消息
					bp.wf.Dev2Interface.Port_SendMessage(empNo, smsDoccReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);
				}
				return "@已向:{" + empsStrs + "}发送提醒信息.";
			}

				/// 流程结束后、流程删除后


				///节点预警、逾期
			if (this.getFK_Event().equals(EventListNode.NodeWarning) || this.getFK_Event().equals(EventListNode.NodeOverDue))
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
					if (DataType.IsNullOrEmpty(empA[0]) == true || empA[0].equals(WebUser.getNo()))
					{
						continue;
					}
					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar7 = smsDoc;
					String smsDocReal = tempVar7 instanceof String ? (String)tempVar7 : null;
					smsDocReal = smsDocReal.replace("{EmpStr}", empA[0]);
					openUrl = openUrl.replace("{EmpStr}", empA[0]);
					bp.wf.Dev2Interface.Port_SendMessage(empA[0], smsDocReal, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), openUrl, this.getSMSPushModel(), workid, null, atParas);
				}
			}

				/// 节点预警、逾期

		}

			/// 不同的消息事件，接收人不同的处理

		return "";
	}
	
}