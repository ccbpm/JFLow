package bp.wf;

import bp.da.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.difference.*;

/** 
 事件重写.
*/
public class OverrideEvent
{
	/** 
	 流程事件-总体拦截器.
	 
	 @param eventMark 流程标记
	 @param wn worknode
	 @param paras 参数
	 @param checkNote 审核意见.
	 @return 执行信息.
	*/
	public static String DoIt(String eventMark, WorkNode wn, String paras, String checkNote, int returnToNodeID, String returnToEmps, String returnMsg) throws Exception {
		//发送成功.
		if (eventMark.equals(EventListNode.SendSuccess) == true)
		{
			// xxx   WFState=0 空白.
		//	long workid=bp.wf.Dev2Interface.Node_CreateBlankWork("qingjia");
		//	bp.wf.Dev2Interface.Node_SetDraft(workid); //WFSTate=1
			//创建一个新的workid.
		//	long workid2=bp.wf.Dev2Interface.Node_CreateBlankWork("qingjia");
			return SendSuccess(wn);
		}

		//退回后事件.
		if (eventMark.equals(EventListNode.ReturnAfter) == true)
		{
			return ReturnAfter(wn, returnToNodeID, returnToEmps, returnMsg);
		}

		// 流程结束事件.
		if (eventMark.equals(EventListFlow.FlowOverAfter) == true)
		{
			return FlowOverAfter(wn);
		}

		return null;
	}
	/** 
	 执行发送.
	 
	 @param wn
	 @return 
	*/
	public static String SendSuccess(WorkNode wn) throws Exception {
		if (SystemConfig.getCustomerNo().equals("TianYu") == true)
		{
			if (wn.getHisNode().getItIsStartNode() == false)
				return null; //如果不是开始节点发送,就不处理.

			//模板目录.
			String sortNo = wn.getHisFlow().getFlowSortNo();

			//找到系统编号.
			FlowSort fs = new FlowSort(sortNo);

			//子系统:当前目录的上一级目录必定是子系统,系统约定的.
			SubSystem system = new SubSystem(fs.getParentNo());

			//检查是否配置了?
			if (DataType.IsNullOrEmpty(system.getTokenPiv()) == true)
			{
				return null;
			}

			//执行事件.
			DoPost("SendSuccess", wn, system);
		}
		return null;
	}
	/** 
	 执行退回操作.
	 
	 @param wn
	 @param toNodeID
	 @param toEmp
	 @param info
	 @return 
	*/
	public static String ReturnAfter(WorkNode wn, int toNodeID, String toEmp, String info) throws Exception {
		if (SystemConfig.getCustomerNo().equals("TianYu") == true)
		{
			if (String.valueOf(toNodeID).endsWith("01") == false)
			{
				return null; //如果不是退回的开始节点.
			}

			//模板目录.
			String sortNo = wn.getHisFlow().getFlowSortNo();

			//找到系统编号.
			FlowSort fs = new FlowSort(sortNo);

			//子系统:当前目录的上一级目录必定是子系统,系统约定的.
			SubSystem system = new SubSystem(fs.getParentNo());

			//检查是否配置了?
			if (DataType.IsNullOrEmpty(system.getTokenPiv()) == true)
			{
				return null;
			}

			//执行事件.
			DoPost("ReturnAfter", wn, system);
		}
		return null;
	}

	public static String FlowOverAfter(WorkNode wn) throws Exception {
		if (SystemConfig.getCustomerNo().equals("TianYu") == true)
		{
			//模板目录.
			String sortNo = wn.getHisFlow().getFlowSortNo();

			//找到系统编号.
			FlowSort fs = new FlowSort(sortNo);

			//子系统:当前目录的上一级目录必定是子系统,系统约定的.
			SubSystem system = new SubSystem(fs.getParentNo());

			//检查是否配置了?
			if (DataType.IsNullOrEmpty(system.getTokenPiv()) == true)
			{
				return null;
			}

			//执行事件.
			DoPost("FlowOverAfter", wn, system);
		}
		return null;
	}
	/** 
	 执行天宇的回调.
	 
	 @param eventMark 事件目录.
	 @param wn
	 @param system
	 @return 
	*/
	public static String DoPost(String eventMark, WorkNode wn, SubSystem system) throws Exception {
		String myEventMark = "0";
		if (eventMark.equals("ReturnAfter"))
		{
			myEventMark = "3";
		}
		if (eventMark.equals("SendSuccess"))
		{
			myEventMark = "1";
		}
		if (eventMark.equals("FlowOverAfter"))
		{
			myEventMark = "2";
		}

		String apiParas = system.getApiParas(); //配置的json字符串.
		apiParas = apiParas.replace("~", "\"");

		apiParas = apiParas.replace("@WorkID", String.valueOf(wn.getWorkID())); //工作ID.
		apiParas = apiParas.replace("@FlowNo", wn.getHisFlow().getNo()); //流程编号.
		apiParas = apiParas.replace("@NodeID", String.valueOf(wn.getHisNode().getNodeID())); //节点ID.
		apiParas = apiParas.replace("@TimeSpan", String.valueOf(DBAccess.GenerOID("TS"))); //时间戳.
		apiParas = apiParas.replace("@EventMark", myEventMark); //稳超定义的，事件标记.
		apiParas = apiParas.replace("@EventID", eventMark); //EventID 定义的事件类型.
		apiParas = apiParas.replace("@SPYJ", "xxx无xx"); //审批意见.

		//如果表单的数据有,就执行一次替换.
		apiParas = bp.wf.Glo.DealExp(apiParas, wn.rptGe);

		//需要补充算法. @WenChao.
		String sign = "密钥:" + system.getTokenPiv() + ",公约" + system.getTokenPublie();
		apiParas = apiParas.replace("@sign", sign); //签名，用于安全验证.

		if (apiParas.contains("@") == true)
		{
			return "err@配置的参数没有替换下来:" + apiParas;
		}

		//替换url参数.
		String url = system.getCallBack(); //回调的全路径.
		url = bp.wf.Glo.DealExp(url, wn.rptGe);

		//执行post.
		String data = bp.tools.PubGlo.HttpPostConnect(url, apiParas, system.getRequestMethod(), system.getItIsJson());
		return data;
	}
}
