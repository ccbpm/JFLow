package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 独立表单树-用于数据解构构造
*/
public class FlowFormTree extends EntityTree
{

		///#region 扩展属性，不做数据操作
	/** 
	 节点类型
	*/
	private String NodeType;
	public final String getNodeType()
	{
		return NodeType;
	}
	public final void setNodeType(String value)
	{
		NodeType = value;
	}
	/** 
	 是否可编辑
	*/
	private String IsEdit;
	public final String getIsEdit()
	{
		return IsEdit;
	}
	public final void setIsEdit(String value)
	{
		IsEdit = value;
	}
	/** 
	 Url
	*/
	private String Url;
	public final String getUrl()
	{
		return Url;
	}
	public final void setUrl(String value)
	{
		Url = value;
	}
	/** 
	 打开时是否关闭其它的页面？
	*/
	private String IsCloseEtcFrm;
	public final String getIsCloseEtcFrm()
	{
		return IsCloseEtcFrm;
	}
	public final void setIsCloseEtcFrm(String value)
	{
		IsCloseEtcFrm = value;
	}

		///#endregion


		///#region 属性
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FK_Flow, value);
	}

		///#endregion 属性


		///#region 构造方法
	/** 
	 独立表单树
	*/
	public FlowFormTree()
	{
	}
	/** 
	 独立表单树
	 
	 @param _No
	 * @throws Exception 
	*/
	public FlowFormTree(String _No) throws Exception
	{
		super(_No);
	}

		///#endregion

	/** 
	 独立表单树Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FormTree", "独立表单树-用于数据解构构造");
		map.Java_SetCodeStruct("2");
		map.Java_SetDepositaryOfEntity(Depositary.Application);


		map.AddTBStringPK(FlowFormTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(FlowFormTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(FlowFormTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		map.AddTBInt(FlowFormTreeAttr.Idx, 0, "Idx", false, false);

			// 隶属的流程编号.
		map.AddTBString(FlowFormTreeAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
}