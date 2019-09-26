package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 手工启动子流程.
*/
public class SubFlowHand extends EntityMyPK
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
	 主流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(SubFlowAutoAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(SubFlowAutoAttr.FK_Flow, value);
	}
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getSubFlowNo() throws Exception
	{
		return this.GetValStringByKey(SubFlowHandAttr.SubFlowNo);
	}
	public final void setSubFlowNo(String value) throws Exception
	{
		SetValByKey(SubFlowHandAttr.SubFlowNo, value);
	}
	/** 
	 流程名称
	 * @throws Exception 
	*/
	public final String getSubFlowName() throws Exception
	{
		return this.GetValStringByKey(SubFlowHandAttr.SubFlowName);
	}
	/** 
	 条件表达式.
	 * @throws Exception 
	*/
	public final String getCondExp() throws Exception
	{
		return this.GetValStringByKey(SubFlowHandAttr.CondExp);
	}
	public final void setCondExp(String value) throws Exception
	{
		SetValByKey(SubFlowHandAttr.CondExp, value);
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
	 该流程启动的子流程运行结束后才可以再次启动
	 * @throws Exception 
	*/
	public final boolean getCompleteReStart() throws Exception
	{
		return this.GetValBooleanByKey(SubFlowAutoAttr.CompleteReStart);
	}
	/** 
	 表达式类型
	 * @throws Exception 
	*/
	public final ConnDataFrom getExpType() throws Exception
	{
		return ConnDataFrom.forValue(this.GetValIntByKey(SubFlowHandAttr.ExpType));
	}
	public final void setExpType(ConnDataFrom value) throws Exception
	{
		SetValByKey(SubFlowHandAttr.ExpType, value.getValue());
	}
	public final String getFK_Node() throws Exception
	{
		return this.GetValStringByKey(SubFlowHandAttr.FK_Node);
	}
	public final void setFK_Node(String value) throws Exception
	{
		SetValByKey(SubFlowHandAttr.FK_Node, value);
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

		///#endregion


		///#region 构造函数
	/** 
	 手工启动子流程
	*/
	public SubFlowHand()
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

		Map map = new Map("WF_NodeSubFlow", "手动启动子流程");

		map.AddMyPK();

	   map.AddTBString(SubFlowAttr.FK_Flow, null, "主流程编号", true, true, 0, 10, 100);

		map.AddTBInt(SubFlowHandAttr.FK_Node, 0, "节点", false, true);
		map.AddDDLSysEnum(SubFlowHandAttr.SubFlowType, 0, "子流程类型", true, false, SubFlowHandAttr.SubFlowType, "@0=手动启动子流程@1=触发启动子流程@2=延续子流程");

		map.AddTBString(SubFlowYanXuAttr.SubFlowNo, null, "子流程编号", true, true, 0, 10, 150, false);
		map.AddTBString(SubFlowYanXuAttr.SubFlowName, null, "子流程名称", true, true, 0, 200, 150, false);

		map.AddDDLSysEnum(SubFlowYanXuAttr.SubFlowModel, 0, "子流程模式", true, true, SubFlowYanXuAttr.SubFlowModel, "@0=下级子流程@1=同级子流程");

		map.AddDDLSysEnum(FlowAttr.IsAutoSendSubFlowOver, 0, "父子流程结束规则", true, true, FlowAttr.IsAutoSendSubFlowOver, "@0=不处理@1=让父流程自动运行下一步@2=结束父流程");


		map.AddDDLSysEnum(FlowAttr.IsAutoSendSLSubFlowOver, 0, "同级子流程结束规则", true, true, FlowAttr.IsAutoSendSLSubFlowOver, "@0=不处理@1=让同级子流程自动运行下一步@2=结束同级子流程");

		map.AddBoolean(SubFlowHandAttr.StartOnceOnly, false, "仅能被调用1次(不能被重复调用).", true, true, true);

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

		map.AddTBInt(SubFlowHandAttr.Idx, 0, "显示顺序", true, false);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(this.getFK_Node() + "_" + this.getSubFlowNo() + "_0");
		return super.beforeInsert();
	}
}