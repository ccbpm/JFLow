package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 子流程.
*/
public class SubFlow extends EntityMyPK
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 子流程编号
	 * @throws Exception 
	*/
	public final String getSubFlowNo() throws Exception
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.SubFlowNo);
	}
	public final void setSubFlowNo(String value) throws Exception
	{
		SetValByKey(SubFlowYanXuAttr.SubFlowNo, value);
	}
	/** 
	 流程名称
	 * @throws Exception 
	*/
	public final String getSubFlowName() throws Exception
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.SubFlowName);
	}
	/** 
	 条件表达式.
	 * @throws Exception 
	*/
	public final String getCondExp() throws Exception
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.CondExp);
	}
	public final void setCondExp(String value) throws Exception
	{
		SetValByKey(SubFlowYanXuAttr.CondExp, value);
	}
	/** 
	 表达式类型
	 * @throws Exception 
	*/
	public final ConnDataFrom getExpType() throws Exception
	{
		return ConnDataFrom.forValue(this.GetValIntByKey(SubFlowYanXuAttr.ExpType));
	}
	public final void setExpType(ConnDataFrom value) throws Exception
	{
		SetValByKey(SubFlowYanXuAttr.ExpType, value.getValue());
	}
	/** 
	 主流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(SubFlowYanXuAttr.FK_Flow, value);
	}
	/** 
	 主流程NodeID
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(SubFlowYanXuAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(SubFlowYanXuAttr.FK_Node, value);
	}
	public final SubFlowType getSubFlowType() throws Exception
	{
		return SubFlowType.forValue(this.GetValIntByKey(SubFlowYanXuAttr.SubFlowType));
	}
	/** 
	 指定的流程启动后,才能启动该子流程(请在文本框配置子流程).
	 * @throws Exception 
	*/
	public final boolean getIsEnableSpecFlowStart() throws Exception
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		boolean val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSpecFlowStart);
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
	/** 
	 仅仅可以启动一次?
	 * @throws Exception 
	*/
	public final boolean getStartOnceOnly() throws Exception
	{
		return this.GetValBooleanByKey(SubFlowYanXuAttr.StartOnceOnly);
	}

	/** 
	 指定的流程结束后,才能启动该子流程(请在文本框配置子流程).
	 * @throws Exception 
	*/
	public final boolean getIsEnableSpecFlowOver() throws Exception
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		boolean val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSpecFlowOver);
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
	public final String getSpecFlowOver() throws Exception
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.SpecFlowOver);
	}
	public final String getSpecFlowStart() throws Exception
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.SpecFlowStart);
	}
	/** 
	 自动发起的子流程发送方式
	 * @throws Exception 
	*/
	public final int getSendModel() throws Exception
	{
		return this.GetValIntByKey(SubFlowAutoAttr.SendModel);
	}

	/** 
	 父子流程结束规则
	 * @throws Exception 
	*/
	public final int getIsAutoSendSubFlowOver() throws Exception
	{
		return this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver);
	}

	/** 
	 同级子流程结束规则
	 * @throws Exception 
	*/
	public final int getIsAutoSendSLSubFlowOver() throws Exception
	{
		return this.GetValIntByKey(FlowAttr.IsAutoSendSLSubFlowOver);
	}

		///#endregion


		///#region 构造函数
	/** 
	 子流程
	*/
	public SubFlow()
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

		Map map = new Map("WF_NodeSubFlow", "子流程(所有类型子流程属性)");
		map.IndexField = SubFlowAttr.FK_Node;

		map.AddMyPK();

		map.AddTBString(SubFlowAttr.FK_Flow, null, "主流程编号", true, true, 0, 10, 150);
		map.AddTBInt(SubFlowAttr.FK_Node, 0, "主流程节点", false, true);

		map.AddTBInt(SubFlowAttr.SubFlowType, 0, "子流程类型", false, true);
		map.AddTBInt(SubFlowAttr.SubFlowModel, 0, "子流程模式", false, true);

		map.AddTBInt(FlowAttr.IsAutoSendSubFlowOver, 0, "父子流程结束规则", false, true);
		map.AddTBInt(FlowAttr.IsAutoSendSLSubFlowOver, 0, "同级子流程结束规则", false, true);


		map.AddTBString(SubFlowAttr.SubFlowNo, null, "子流程编号", true, true, 0, 10, 150, false);
		map.AddTBString(SubFlowAttr.SubFlowName, null, "子流程名称", true, true, 0, 200, 150, false);

			//启动限制规则0.
		map.AddTBInt(SubFlowAttr.StartOnceOnly, 0, "仅能被调用1次", false, true);

			//启动限制规则1.
		map.AddTBInt(SubFlowAttr.IsEnableSpecFlowStart, 0, "指定的流程启动后,才能启动该子流程(请在文本框配置子流程).", false, true);
		map.AddTBString(SubFlowHandAttr.SpecFlowStart, null, "子流程编号", true, false, 0, 200, 150, true);
		map.AddTBString(SubFlowHandAttr.SpecFlowStartNote, null, "备注", true, false, 0, 500, 150, true);

			//启动限制规则2.
		map.AddTBInt(SubFlowHandAttr.IsEnableSpecFlowOver, 0, "指定的流程结束后,才能启动该子流程(请在文本框配置子流程).", true, true);
		map.AddTBString(SubFlowHandAttr.SpecFlowOver, null, "子流程编号", true, false, 0, 200, 150, true);
		map.AddTBString(SubFlowHandAttr.SpecFlowOverNote, null, "备注", true, false, 0, 500, 150, true);


		map.AddTBInt(SubFlowAttr.ExpType, 0, "表达式类型", false, true);
		map.AddTBString(SubFlowAttr.CondExp, null, "条件表达式", true, false, 0, 500, 150, true);

		map.AddTBInt(SubFlowAttr.YBFlowReturnRole, 0, "退回方式", false, true);

		map.AddTBString(SubFlowAttr.ReturnToNode, null, "要退回的节点", true, true, 0, 200, 150, false);

		map.AddTBInt(SubFlowAttr.SendModel, 0, "自动触发的子流程发送方式", false, true);

		map.AddTBInt(SubFlowAttr.Idx, 0, "顺序", true, false);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 设置主键
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(this.getFK_Node() + "_" + this.getSubFlowNo() + "_" + this.getSubFlowType());
		return super.beforeInsert();
	}
}