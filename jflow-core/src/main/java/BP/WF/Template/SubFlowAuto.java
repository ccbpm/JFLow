package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 自动触发子流程.
*/
public class SubFlowAuto extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 主流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(SubFlowAutoAttr.FK_Flow, value);
	}
	/** 
	 流程编号
	*/
	public final String getSubFlowNo()
	{
		return this.GetValStringByKey(SubFlowAttr.SubFlowNo);
	}
	public final void setSubFlowNo(String value)
	{
		SetValByKey(SubFlowAutoAttr.SubFlowNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.SubFlowName);
	}
	/** 
	 条件表达式.
	*/
	public final String getCondExp()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.CondExp);
	}
	public final void setCondExp(String value)
	{
		SetValByKey(SubFlowAutoAttr.CondExp, value);
	}
	/** 
	 表达式类型
	*/
	public final ConnDataFrom getExpType()
	{
		return ConnDataFrom.forValue(this.GetValIntByKey(SubFlowAutoAttr.ExpType));
	}
	public final void setExpType(ConnDataFrom value)
	{
		SetValByKey(SubFlowAutoAttr.ExpType, value.getValue());
	}
	public final int getFK_Node()
	{
		return this.GetValIntByKey(SubFlowAutoAttr.FK_Node);
	}
	/** 
	 调用时间 0=工作发送时, 1=工作到达时.
	*/
	public final int getInvokeTime()
	{
		return this.GetValIntByKey(SubFlowAutoAttr.InvokeTime);
	}

	/** 
	 运行类型
	*/
	public final SubFlowModel getHisSubFlowModel()
	{
		return SubFlowModel.forValue(this.GetValIntByKey(SubFlowAutoAttr.SubFlowModel));
	}
	/** 
	 类型
	*/
	public final SubFlowType getHisSubFlowType()
	{
		return SubFlowType.forValue(this.GetValIntByKey(SubFlowAutoAttr.SubFlowType));
	}
	/** 
	 仅仅发起一次.
	*/
	public final boolean getStartOnceOnly()
	{
		return this.GetValBooleanByKey(SubFlowAutoAttr.StartOnceOnly);
	}

	public final boolean getCompleteReStart()
	{
		return this.GetValBooleanByKey(SubFlowAutoAttr.CompleteReStart);
	}
	/** 
	 指定的流程启动后,才能启动该子流程(请在文本框配置子流程).
	*/
	public final boolean getIsEnableSpecFlowStart()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSpecFlowStart);
		if (val == false)
		{
			return false;
		}

		if (this.getSpecFlowStart().length() > 2)
		{
			return true;
		}
		return false;
	}
	public final String getSpecFlowStart()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.SpecFlowStart);
	}
	/** 
	 指定的流程结束后,才能启动该子流程(请在文本框配置子流程).
	*/
	public final boolean getIsEnableSpecFlowOver()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSpecFlowOver);
		if (val == false)
		{
			return false;
		}

		if (this.getSpecFlowOver().length() > 2)
		{
			return true;
		}
		return false;
	}
	public final String getSpecFlowOver()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.SpecFlowOver);
	}
	/** 
	 按SQL配置
	*/
	public final boolean getIsEnableSQL()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSQL);
		if (val == false)
		{
			return false;
		}

		if (this.getSpecSQL().length() > 2)
		{
			return true;
		}
		return false;

	}

	public final String getSpecSQL()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.SpecSQL);
	}

	/** 
	 指定平级子流程节点结束后启动子流程
	*/
	public final boolean getIsEnableSameLevelNode()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSameLevelNode);
		if (val == false)
		{
			return false;
		}

		if (this.getSameLevelNode().length() > 2)
		{
			return true;
		}
		return false;

	}

	public final String getSameLevelNode()
	{
		return this.GetValStringByKey(SubFlowAutoAttr.SameLevelNode);
	}


	/** 
	 自动发起的子流程发送方式
	*/
	public final int getSendModel()
	{
		return this.GetValIntByKey(SubFlowAutoAttr.SendModel);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 自动触发子流程
	*/
	public SubFlowAuto()
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

		Map map = new Map("WF_NodeSubFlow", "自动触发子流程");


		map.AddMyPK();


		map.AddTBString(SubFlowAttr.FK_Flow, null, "主流程编号", true, false, 0, 10, 100, true);


		map.AddTBInt(SubFlowHandAttr.FK_Node, 0, "节点", false, true);
		map.AddDDLSysEnum(SubFlowHandAttr.SubFlowType, 0, "子流程类型", true, false, SubFlowHandAttr.SubFlowType, "@0=手动启动子流程@1=触发启动子流程@2=延续子流程");

		map.AddTBString(SubFlowYanXuAttr.SubFlowNo, null, "子流程编号", true, true, 0, 10, 150, false);
		map.AddTBString(SubFlowYanXuAttr.SubFlowName, null, "子流程名称", true, true, 0, 200, 150, false);

		map.AddDDLSysEnum(SubFlowYanXuAttr.SubFlowModel, 0, "子流程模式", true, true, SubFlowYanXuAttr.SubFlowModel, "@0=下级子流程@1=同级子流程");

		map.AddDDLSysEnum(FlowAttr.IsAutoSendSubFlowOver, 0, "父子流程结束规则", true, true, FlowAttr.IsAutoSendSubFlowOver, "@0=不处理@1=让父流程自动运行下一步@2=结束父流程");


		map.AddDDLSysEnum(FlowAttr.IsAutoSendSLSubFlowOver, 0, "同级子流程结束规则", true, true, FlowAttr.IsAutoSendSLSubFlowOver, "@0=不处理@1=让同级子流程自动运行下一步@2=结束同级子流程");

		map.AddDDLSysEnum(SubFlowAttr.InvokeTime, 0, "调用时间", true, true, SubFlowAttr.InvokeTime, "@0=发送时@1=工作到达时");

		map.AddBoolean(SubFlowHandAttr.StartOnceOnly, false, "仅能被调用1次.", true, true, true);

		map.AddBoolean(SubFlowHandAttr.CompleteReStart, false, "该子流程运行结束后才可以重新发起.", true, true, true);

			//启动限制规则.
		map.AddBoolean(SubFlowHandAttr.IsEnableSpecFlowStart, false, "指定的流程启动后,才能启动该子流程(请在文本框配置子流程).", true, true, true);
		map.AddTBString(SubFlowHandAttr.SpecFlowStart, null, "子流程编号", true, false, 0, 200, 150, true);
		map.SetHelperAlert(SubFlowHandAttr.SpecFlowStart, "指定的流程启动后，才能启动该子流程，多个子流程用逗号分开. 001,002");
		map.AddTBString(SubFlowHandAttr.SpecFlowStartNote, null, "备注", true, false, 0, 500, 150, true);

			//启动限制规则.
		map.AddBoolean(SubFlowHandAttr.IsEnableSpecFlowOver, false, "指定的流程结束后,才能启动该子流程(请在文本框配置子流程).", true, true, true);
		map.AddTBString(SubFlowHandAttr.SpecFlowOver, null, "子流程编号", true, false, 0, 200, 150, true);
		map.SetHelperAlert(SubFlowHandAttr.SpecFlowOver, "指定的流程结束后，才能启动该子流程，多个子流程用逗号分开. 001,002");
		map.AddTBString(SubFlowHandAttr.SpecFlowOverNote, null, "备注", true, false, 0, 500, 150, true);

			//启动限制规则
		map.AddBoolean(SubFlowHandAttr.IsEnableSQL, false, "按照指定的SQL配置.", true, true, true);
		map.AddTBString(SubFlowHandAttr.SpecSQL, null, "SQL语句", true, false, 0, 500, 150, true);

			//启动限制规则
		map.AddBoolean(SubFlowHandAttr.IsEnableSameLevelNode, false, "按照指定平级子流程节点完成后启动.", true, true, true);
		map.AddTBString(SubFlowHandAttr.SameLevelNode, null, "平级子流程节点", true, false, 0, 500, 150, true);
		map.SetHelperAlert(SubFlowHandAttr.SameLevelNode, "按照指定平级子流程节点完成后启动，才能启动该子流程，多个平级子流程节点用逗号分开. 001,102;002,206");

			//自动发送方式.
		map.AddDDLSysEnum(SubFlowHandAttr.SendModel, 0, "自动发送方式", true, true, SubFlowHandAttr.SendModel, "@0=给当前人员设置开始节点待办@1=发送到下一个节点");
		map.SetHelperAlert(SubFlowHandAttr.SendModel, "如果您选择了[发送到下一个节点]该流程的下一个节点的接受人规则必须是自动计算的,而不能手工选择.");

		map.AddTBInt(SubFlowHandAttr.Idx, 0, "显示顺序", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 设置主键
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		this.setMyPK( this.getFK_Node() + "_" + this.getSubFlowNo() + "_1";
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		if (this.getSendModel() == 1)
		{
			//设置的发送到，发送到下一个节点上.

			Node nd = new Node(Integer.parseInt(this.getSubFlowNo() + "01"));

			Nodes tonds = nd.getHisToNodes();
			for (Node item : tonds)
			{
				if (item.getHisDeliveryWay() == DeliveryWay.BySelected)
				{
					throw new RuntimeException("err@【自动发送方式】设置错误，您选择了[发送到下一个节点]但是该节点的接收人规则为由上一步发送人员选择，这是不符合规则的。");
				}
			}
		}

		//设置主流程ID.
		Node myNd = new Node(this.getFK_Node());
		this.setFK_Flow(myNd.getFK_Flow());

		return super.beforeUpdateInsertAction();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 移动.
	/** 
	 上移
	 
	 @return 
	*/
	public final String DoUp()
	{
		this.DoOrderUp(SubFlowAutoAttr.FK_Node, String.valueOf(this.getFK_Node()), SubFlowAutoAttr.SubFlowType, "1", SubFlowAutoAttr.Idx);
		return "执行成功";
	}
	/** 
	 下移
	 
	 @return 
	*/
	public final String DoDown()
	{
		this.DoOrderDown(SubFlowAutoAttr.FK_Node, String.valueOf(this.getFK_Node()), SubFlowAutoAttr.SubFlowType, "2", SubFlowAutoAttr.Idx);
		return "执行成功";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 移动.

}