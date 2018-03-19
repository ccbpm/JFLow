package BP.WF.Data;


import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.WF.Flows;
import BP.WF.Template.FlowSorts;

/** 
 流程统计
*/
public class GenerWorkFlowTodolist extends EntityMyPK
{
	public final String getParas_ToNodes()
	{
		return this.GetParaString("ToNodes");
	}

	public final void setParas_ToNodes(String value)
	{
		this.SetPara("ToNodes", value);
	}
	/** 
	 加签信息
	*/

	public final String getParas_AskForReply()
	{
		return this.GetParaString("AskForReply");
	}

	public final void setParas_AskForReply(String value)
	{
		this.SetPara("AskForReply", value);
	}
		
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	/** 
	 产生的工作流程
	*/
	public GenerWorkFlowTodolist()
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

		Map map = new Map("V_Todolist", "流程统计");

		map.AddMyPK();

		map.AddDDLEntities(GenerWorkFlowTodolistAttr.FK_FlowSort, null, "类别", new FlowSorts(), false);
		map.AddDDLEntities(GenerWorkFlowTodolistAttr.FK_Flow, null, "流程", new Flows(), false);


		map.AddTBInt(GenerWorkFlowTodolistAttr.TodoSta0, 0, "待办中", true, true);
		map.AddTBInt(GenerWorkFlowTodolistAttr.TodoSta1, 0, "预警中", true, true);
		map.AddTBInt(GenerWorkFlowTodolistAttr.TodoSta2, 0, "逾期中", true, true);
		map.AddTBInt(GenerWorkFlowTodolistAttr.TodoSta3, 0, "正常办结", true, true);
		map.AddTBInt(GenerWorkFlowTodolistAttr.TodoSta4, 0, "超期办结", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

}