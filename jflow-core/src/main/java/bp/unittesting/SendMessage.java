package bp.unittesting;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.port.Emp;
import bp.da.*;
import bp.sys.*;
import bp.web.*;
import bp.unittesting.*;
import java.util.*;

/** 
 消息发送机制测试
*/
public class SendMessage extends TestBase
{

		///变量
	/** 
	 流程编号
	*/
	public String fk_flow = "";
	/** 
	 用户编号
	*/
	public String userNo = "";
	/** 
	 所有的流程
	*/
	public Flow fl = null;
	/** 
	 主线程ID
	*/
	public long workID = 0;
	/** 
	 发送后返回对象
	*/
	public SendReturnObjs objs = null;
	/** 
	 工作人员列表
	*/
	public GenerWorkerList gwl = null;
	/** 
	 流程注册表
	*/
	public GenerWorkFlow gwf = null;
	/** 
	 发起人
	*/
	public bp.port.Emp starterEmp = null;

		/// 变量

	/** 
	 消息发送机制测试
	*/
	public SendMessage()
	{
		this.Title = "消息发送机制测试";
		this.DescIt = "流程:002请假流程(内部人员),055学生请假流程(内外部人员都有).";
		this.editState = EditState.Editing; //已经能过.
	}

	/** 
	 过程说明：
	 1，以流程 001 来测试发送流程.
	 2，仅仅测试发送功能，与检查发送后的数据是否完整.
	 3, 此测试有三个节点发起点、中间点、结束点，对应三个测试方法。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//预警.
		bp.wf.Dev2Interface.DTS_GenerWorkFlowTodoSta();


		//测试发送短消息是否。
		this.Test001_liyan_Start();

		//测试学生请假流程.
		this.Test055();

		//测试请假流程，在没有设置任何消息机制的情况下，发送默认的消息, zhanghaicheng登录，
		this.Test002();

		// 测试工作到达短消息
		this.Test002_zhoupeng_liping();


	}
	/** 
	 测试让liyan发起，发送并产生一个发送成功后的短消息.
	 * @throws Exception 
	*/
	public final void Test001_liyan_Start() throws Exception
	{
		//删除消息. 
		DBAccess.RunSQL("DELETE FROM Sys_SMS");

		// 让zhoupeng登录.
		bp.wf.Dev2Interface.Port_Login("liyan");

		//创建空白工作, 发起开始节点.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork("001");

		//执行发送工作.
		bp.wf.Dev2Interface.Node_SendWork("001", workID, null, null);



			///检查消息是否 符合预期.
		bp.wf.SMSs smss = new SMSs();
		smss.RetrieveAllFromDBSource();
		if (smss.size() != 1)
		{
			throw new RuntimeException("@应该产生 1 条，现在产生了" + smss.size() + "条。");
		}

		for (bp.wf.SMS item : smss.ToJavaList())
		{
			if (item.getMsgType().equals(EventListNode.SendSuccess))
			{
				if (item.getHisMobileSta() != MsgSta.UnRun)
				{
					throw new RuntimeException("@应该在工作到达的事件里产生一条短信息.");
				}
			}
			else
			{
				throw new RuntimeException("@短消息的标记错误." + item.getMsgType());
			}
		}

			/// 检查消息是否 符合预期.

	}
	/** 
	 测试工作到达短消息
	 * @throws Exception 
	*/
	public final void Test002_zhoupeng_liping() throws Exception
	{
		//删除消息. 
		DBAccess.RunSQL("DELETE FROM Sys_SMS");

		// 让zhoupeng登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//创建空白工作, 发起开始节点.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork("002");

		//执行发送工作.
		bp.wf.Dev2Interface.Node_SendWork("002", workID, null, null);


			///检查消息是否 符合预期.
		bp.wf.SMSs smss = new SMSs();
		smss.RetrieveAllFromDBSource();
		if (smss.size() == 0)
		{
			throw new RuntimeException("@执行了发送，应该产生消息，而没有产生。");
		}
		if (smss.size() != 2)
		{
			throw new RuntimeException("@应该产生2 条，现在产生了" + smss.size() + "条。");
		}

		for (bp.wf.SMS item : smss.ToJavaList())
		{
			if (item.getMsgType().equals(EventListNode.SendSuccess))
			{
				if (item.getHisEmailSta() != MsgSta.UnRun)
				{
					throw new RuntimeException("@应该在发送成功的事件里产生一条邮件信息.");
				}
			}

			if (item.getMsgType().equals(EventListNode.WorkArrive))
			{
				if (item.getHisMobileSta() != MsgSta.UnRun)
				{
					throw new RuntimeException("@应该在工作到达的事件里产生一条短信息.");
				}
			}
		}

			/// 检查消息是否 符合预期.

	}

	/** 
	 测试学生请假流程.
	 * @throws Exception 
	*/
	public final void Test055() throws Exception
	{
		//先执行一次流程检查.
		bp.wf.Flow fl = new Flow("055");
		fl.DoCheck();

		//让客户登录 0001登录，光头强.
		bp.wf.Dev2InterfaceGuest.Port_Login("0001", "光头强");
		//删除消息. 
		DBAccess.RunSQL("DELETE FROM Sys_SMS");

		//创建workid.
		workID = bp.wf.Dev2InterfaceGuest.Node_CreateBlankWork("055", null, null,GuestUser.getNo(), GuestUser.getName());

		//生成参数格式，把他传入给流程引擎，让其向这个手机写入消息。
		Hashtable ht = new Hashtable();
		ht.put("SQRSJH","18660153393");

		//向下发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork("055", workID, ht, null);

		String empNo = objs.getVarAcceptersID();
		if (!empNo.equals("guoxiangbin"))
		{
			throw new RuntimeException("@应该发送给guoxiangbin 处理但是发送到了:" + empNo);
		}

			///检查消息发送的结果.

		//系统设置了向下一步接受人员发送邮件，向指定字段的作为手机号发送短信。
		bp.wf.SMSs smss = new SMSs();
		smss.RetrieveAllFromDBSource();
		if (smss.size() != 1)
		{
			throw new RuntimeException("@应该产生 1 条，现在产生了" + smss.size() + "条。");
		}

		for (bp.wf.SMS sms : smss.ToJavaList())
		{
			if (!sms.getMsgType().equals(SMSMsgType.SendSuccess))
			{
				throw new RuntimeException("@应该是 SendSuccess 禁用状态,现在是:" + sms.getMsgType());
			}

			if (sms.getHisMobileSta() != MsgSta.UnRun)
			{
				throw new RuntimeException("@应该是 短消息 禁用状态，但是目前状态是:" + sms.getHisMobileSta());
			}

			if (!sms.getSender().equals(WebUser.getNo()))
			{
				throw new RuntimeException("@应该 Sender= " + WebUser.getNo() + " ，但是目前是:" + sms.getSender());
			}

			if (sms.getIsRead() != 0)
			{
				throw new RuntimeException("@应该是 IsRead=0 ，但是目前是:" + sms.getIsRead());
			}
		}

			/// 检查消息发送的结果.

		//给发起人赋值.
		starterEmp = new Emp(empNo);
		//让 userNo 登录.
		bp.wf.Dev2Interface.Port_Login(empNo);
	}
	/** 
	 测试默认的请假流程发送与退回.
	 zhanghaicheng登录，
	 zhoupeng 审批.
	 liping 核实.
	 * @throws Exception 
	*/
	public final void Test002() throws Exception
	{
		//流程编号.
		String fk_flow = "002";
		userNo = "liyan";

		//给发起人赋值.
		starterEmp = new Emp(userNo);

		//让 userNo 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//检查201节点是否有短消息？
		PushMsgs msgs = new PushMsgs(201);
		if (msgs.size() > 0)
		{
			throw new RuntimeException("@测试模版变化，以下的测试将会不正确。");
		}

		//创建空白工作, 发起开始节点.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//删除消息. 
		DBAccess.RunSQL("DELETE FROM Sys_SMS");

		//执行发送工作.
		bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID, null, null);


			///检查是否有消息产生.
		bp.wf.SMSs smss = new SMSs();
		smss.RetrieveAllFromDBSource();
		if (smss.size() == 0)
		{
			throw new RuntimeException("@执行了发送，应该产生消息，而没有产生。");
		}

		if (smss.size() != 1)
		{
			throw new RuntimeException("@应该产生1条，现在产生了多条。");
		}

		for (bp.wf.SMS sm : smss.ToJavaList())
		{
			if (sm.getHisEmailSta() != MsgSta.UnRun)
			{
				throw new RuntimeException("@应该是邮件启用状态，但是目前状态是:" + sm.getHisEmailSta());
			}

			if (!sm.getMsgType().equals(SMSMsgType.SendSuccess))
			{
				throw new RuntimeException("@应该是 SendSuccess 的类型，但是目前状态是:" + sm.getMsgType());
			}

			if (!sm.getSendToEmpNo().equals("liping"))
			{
				throw new RuntimeException("@应该是 liping 是接受人ID，但是目前是:" + sm.getSendToEmpNo());
			}

			if (!sm.getSender().equals(WebUser.getNo()))
			{
				throw new RuntimeException("@应该 Sender= " + WebUser.getNo() + " ，但是目前是:" + sm.getSender());
			}

			if (sm.getIsRead() != 0)
			{
				throw new RuntimeException("@应该是 IsRead=0 ，但是目前是:" + sm.getIsRead());
			}
		}

			/// 检查是否有消息产生.

		/*
		 * 让 zhanghaicheng 登录，走申请 __>> 总经理审批 __>> 人力资源备案.
		 */

		//让 zhanghaicheng 登录,
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");
		//创建空白工作, 发起开始节点.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		//让zhanghaicheng发送.
		bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID, null, null);

		//让 zhoupeng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//删除消息. 
		DBAccess.RunSQL("DELETE FROM Sys_SMS");

		//让zhoupeng发送, 发送到人力资源备案. 并且人力资源有短消息提醒.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID, null, null);

		//到达HR节点ID.
		int nodeIDOfHR = objs.getVarToNodeID();
		PushMsgs pms = new PushMsgs(nodeIDOfHR);
		if (pms.size() != 1)
		{
			throw new RuntimeException("@请假流程的消息机制模版, 人力资源部审批，设置的短消息到达，被改变。");
		}

		//检查是否有消息存在.
		smss = new SMSs();
		smss.RetrieveAllFromDBSource();
		if (smss.size() == 0)
		{
			throw new RuntimeException("@执行了发送，应该产生消息，而没有产生，应该产生一条短消息.");
		}

		//遍历消息.
		for (bp.wf.SMS item : smss.ToJavaList())
		{
			if (item.getMsgType().equals("WorkArrive"))
			{
				if (!item.getSendToEmpNo().equals("liping"))
				{
					throw new RuntimeException("@应该是 liping 现在是:" + item.getSendToEmpNo());
				}

				if (!item.getSender().equals("zhoupeng"))
				{
					throw new RuntimeException("@应该是 zhoupeng 现在是:" + item.getSendToEmpNo());
				}

				if (item.getHisMobileSta() != MsgSta.UnRun)
				{
					throw new RuntimeException("@ MsgSta 状态写入不正确 UnRun 现在是:" + item.getSendToEmpNo());
				}
			}
		}
	}
}