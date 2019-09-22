package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 轨迹
*/
public class Track extends BP.En.EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性.
	/** 
	 表单数据
	*/
	public String FrmDB = null;
	public String FK_Flow = null;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 字段属性.
	/** 
	 节点从
	*/
	public final int getNDFrom()
	{
		return this.GetValIntByKey(TrackAttr.NDFrom);
	}
	public final void setNDFrom(int value)
	{
		this.SetValByKey(TrackAttr.NDFrom, value);
	}
	/** 
	 节点到
	*/
	public final int getNDTo()
	{
		return this.GetValIntByKey(TrackAttr.NDTo);
	}
	public final void setNDTo(int value)
	{
		this.SetValByKey(TrackAttr.NDTo, value);
	}
	/** 
	 从人员
	*/
	public final String getEmpFrom()
	{
		return this.GetValStringByKey(TrackAttr.EmpFrom);
	}
	public final void setEmpFrom(String value)
	{
		this.SetValByKey(TrackAttr.EmpFrom, value);
	}
	/** 
	 到人员
	*/
	public final String getEmpTo()
	{
		return this.GetValStringByKey(TrackAttr.EmpTo);
	}
	public final void setEmpTo(String value)
	{
		this.SetValByKey(TrackAttr.EmpTo, value);
	}
	/** 
	 参数数据.
	*/
	public final String getTag()
	{
		return this.GetValStringByKey(TrackAttr.Tag);
	}
	public final void setTag(String value)
	{
		this.SetValByKey(TrackAttr.Tag, value);
	}
	/** 
	 记录日期
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(TrackAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(TrackAttr.RDT, value);
	}
	/** 
	 fid
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(TrackAttr.FID);
	}
	public final void setFID(long value)
	{
		this.SetValByKey(TrackAttr.FID, value);
	}
	/** 
	 工作ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(TrackAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		this.SetValByKey(TrackAttr.WorkID, value);
	}
	/** 
	 CWorkID
	*/
	public final long getCWorkID()
	{
		return this.GetValInt64ByKey(TrackAttr.CWorkID);
	}
	public final void setCWorkID(long value)
	{
		this.SetValByKey(TrackAttr.CWorkID, value);
	}
	/** 
	 活动类型
	*/
	public final ActionType getHisActionType()
	{
		return ActionType.forValue(this.GetValIntByKey(TrackAttr.ActionType));
	}
	public final void setHisActionType(ActionType value)
	{
		this.SetValByKey(TrackAttr.ActionType, value.getValue());
	}
	/** 
	 获取动作文本
	 
	 @param at
	 @return 
	*/
	public static String GetActionTypeT(ActionType at)
	{
		switch (at)
		{
			case Forward:
				return "发送";
			case Return:
				return "退回";
			case ReturnAndBackWay:
				return "退回并原路返回";
			case Shift:
				return "移交";
			case UnShift:
				return "撤消移交";
			case Start:
				return "发起";
			case UnSend:
				return "撤消发送";
			case ForwardFL:
				return " -前进(分流点)";
			case ForwardHL:
				return " -向合流点发送";
			case FlowOver:
				return "流程结束";
			case CallChildenFlow:
				return "子流程调用";
			case StartChildenFlow:
				return "子流程发起";
			case SubThreadForward:
				return "线程前进";
			case RebackOverFlow:
				return "恢复已完成的流程";
			case FlowOverByCoercion:
				return "强制结束流程";
			case HungUp:
				return "挂起";
			case UnHungUp:
				return "取消挂起";
			case Press:
				return "催办";
			case CC:
				return "抄送";
			case WorkCheck:
				return "审核";
			case ForwardAskfor:
				return "加签发送";
			case AskforHelp:
				return "加签";
			case Skip:
				return "跳转";
			case HuiQian:
				return "主持人执行会签";
			case DeleteFlowByFlag:
				return "逻辑删除";
			case Order:
				return "队列发送";
			case FlowBBS:
				return "评论";
			case TeampUp:
				return "协作";
			default:
				return "信息" + at.toString();
		}
	}
	/** 
	 活动名称
	*/
	public final String getActionTypeText()
	{
		return this.GetValStringByKey(TrackAttr.ActionTypeText);
	}
	public final void setActionTypeText(String value)
	{
		this.SetValByKey(TrackAttr.ActionTypeText, value);
	}
	/** 
	 节点数据
	*/
	public final String getNodeData()
	{
		return this.GetValStringByKey(TrackAttr.NodeData);
	}
	public final void setNodeData(String value)
	{
		this.SetValByKey(TrackAttr.NodeData, value);
	}
	/** 
	 实际执行人
	*/
	public final String getExer()
	{
		return this.GetValStringByKey(TrackAttr.Exer);
	}
	public final void setExer(String value)
	{
		this.SetValByKey(TrackAttr.Exer, value);
	}
	/** 
	 审核意见
	*/
	public final String getMsg()
	{
		return this.GetValStringByKey(TrackAttr.Msg);
	}
	public final void setMsg(String value)
	{
		this.SetValByKey(TrackAttr.Msg, value);
	}
	/** 
	 消息
	*/
	public final String getMsgHtml()
	{
		return this.GetValHtmlStringByKey(TrackAttr.Msg);
	}
	/** 
	 人员到
	*/
	public final String getEmpToT()
	{
		return this.GetValStringByKey(TrackAttr.EmpToT);
	}
	public final void setEmpToT(String value)
	{
		this.SetValByKey(TrackAttr.EmpToT, value);
	}
	/** 
	 人员从
	*/
	public final String getEmpFromT()
	{
		return this.GetValStringByKey(TrackAttr.EmpFromT);
	}
	public final void setEmpFromT(String value)
	{
		this.SetValByKey(TrackAttr.EmpFromT, value);
	}
	/** 
	 节点从
	*/
	public final String getNDFromT()
	{
		return this.GetValStringByKey(TrackAttr.NDFromT);
	}
	public final void setNDFromT(String value)
	{
		this.SetValByKey(TrackAttr.NDFromT, value);
	}
	/** 
	 节点到
	*/
	public final String getNDToT()
	{
		return this.GetValStringByKey(TrackAttr.NDToT);
	}
	public final void setNDToT(String value)
	{
		this.SetValByKey(TrackAttr.NDToT, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion attrs

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造.
	public String RptName = null;
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Frm_Track", "轨迹表");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段
		map.AddMyPK(); //增加一个自动增长的列.

		map.AddTBInt(TrackAttr.ActionType, 0, "类型", true, false);
		map.AddTBString(TrackAttr.ActionTypeText, null, "类型(名称)", true, false, 0, 30, 100);
		map.AddTBInt(TrackAttr.FID, 0, "流程ID", true, false);
		map.AddTBInt(TrackAttr.WorkID, 0, "工作ID", true, false);

		map.AddTBInt(TrackAttr.NDFrom, 0, "从节点", true, false);
		map.AddTBString(TrackAttr.NDFromT, null, "从节点(名称)", true, false, 0, 300, 100);

		map.AddTBInt(TrackAttr.NDTo, 0, "到节点", true, false);
		map.AddTBString(TrackAttr.NDToT, null, "到节点(名称)", true, false, 0, 999, 900);

		map.AddTBString(TrackAttr.EmpFrom, null, "从人员", true, false, 0, 20, 100);
		map.AddTBString(TrackAttr.EmpFromT, null, "从人员(名称)", true, false, 0, 30, 100);

		map.AddTBString(TrackAttr.EmpTo, null, "到人员", true, false, 0, 2000, 100);
		map.AddTBString(TrackAttr.EmpToT, null, "到人员(名称)", true, false, 0, 2000, 100);

		map.AddTBString(TrackAttr.RDT, null, "日期", true, false, 0, 20, 100);
		map.AddTBFloat(TrackAttr.WorkTimeSpan, 0, "时间跨度(天)", true, false);
		map.AddTBStringDoc(TrackAttr.Msg, null, "消息", true, false);
		map.AddTBStringDoc(TrackAttr.NodeData, null, "节点数据(日志信息)", true, false);
		map.AddTBString(TrackAttr.Tag, null, "参数", true, false, 0, 300, 3000);
		map.AddTBString(TrackAttr.Exer, null, "执行人", true, false, 0, 200, 100);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段

		this._enMap = map;
		return this._enMap;
	}
	/** 
	 轨迹
	*/
	public Track()
	{
	}
	/** 
	 增加授权人
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		if (BP.Web.WebUser.No.equals("Guest"))
		{
			this.setExer(BP.Web.GuestUser.No + "," + BP.Web.GuestUser.Name);
		}
		else
		{
			if (BP.Web.WebUser.IsAuthorize)
			{
				this.setExer(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.Auth, BP.Web.WebUser.AuthName));
			}
			else
			{
				this.setExer(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.No, BP.Web.WebUser.Name));
			}
		}

		LocalDateTime d = LocalDateTime.MIN;
		tangible.OutObject<LocalDateTime> tempOut_d = new tangible.OutObject<LocalDateTime>();
		if (tangible.StringHelper.isNullOrWhiteSpace(getRDT()) || LocalDateTime.TryParse(this.getRDT(), tempOut_d) == false)
		{
		d = tempOut_d.argValue;
			this.setRDT(BP.DA.DataType.CurrentDataTimess);
		}
	else
	{
		d = tempOut_d.argValue;
	}

		return super.beforeInsert();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造.
}