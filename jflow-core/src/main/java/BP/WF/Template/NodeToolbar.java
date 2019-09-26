package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 工具栏.	 
*/
public class NodeToolbar extends EntityOID
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
		return uac;
	}
	/** 
	 工具栏的事务编号
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(NodeToolbarAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(NodeToolbarAttr.FK_Node, value);
	}
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(NodeToolbarAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		SetValByKey(NodeToolbarAttr.Title, value);
	}
	public final String getUrl() throws Exception
	{
		String s = this.GetValStringByKey(NodeToolbarAttr.Url);

		if (this.getExcType() != 1 && s.contains("?") == false && !this.getTarget().toLowerCase().equals("javascript"))
		{
			s = s + "?1=2";
		}
		return s;
	}
	public final void setUrl(String value) throws Exception
	{
		SetValByKey(NodeToolbarAttr.Url, value);
	}
	public final String getTarget() throws Exception
	{
		return this.GetValStringByKey(NodeToolbarAttr.Target);
	}
	public final void setTarget(String value) throws Exception
	{
		SetValByKey(NodeToolbarAttr.Target, value);
	}
	/** 
	 显示在那里？
	 * @throws Exception 
	*/
	public final ShowWhere getShowWhere() throws Exception
	{
		return ShowWhere.forValue(this.GetValIntByKey(NodeToolbarAttr.ShowWhere));
	}
	public final void setShowWhere(ShowWhere value) throws Exception
	{
		SetValByKey(NodeToolbarAttr.ShowWhere, value.getValue());
	}
	/** 
	 执行类型
	 * @throws Exception 
	*/
	public final int getExcType() throws Exception
	{
		return this.GetValIntByKey(NodeToolbarAttr.ExcType);
	}
	public final void setExcType(int value) throws Exception
	{
		SetValByKey(NodeToolbarAttr.ExcType, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 工具栏
	*/
	public NodeToolbar()
	{
	}
	/** 
	 工具栏
	 
	 @param _oid 工具栏ID	
	 * @throws Exception 
	*/
	public NodeToolbar(int oid) throws Exception
	{
		this.setOID(oid);
		this.Retrieve();
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

		Map map = new Map("WF_NodeToolbar", "自定义工具栏");

		map.AddTBIntPKOID();
		map.AddTBString(NodeToolbarAttr.Title, null, "标题", true, false, 0, 100, 100, true);

			// 执行类型.
		map.AddDDLSysEnum(NodeToolbarAttr.ExcType, 0, "执行类型", true, true, "ToobarExcType", "@0=超链接@1=函数");

		map.AddTBString(NodeToolbarAttr.Url, null, "连接/函数", true, false, 0, 500, 300, true);
		map.AddTBString(NodeToolbarAttr.Target, null, "目标", true, false, 0, 100, 100, true);

			// 显示位置.
		map.AddDDLSysEnum(NodeToolbarAttr.ShowWhere, 1, "显示位置", true,true, NodeToolbarAttr.ShowWhere, "@0=树形表单@1=工具栏");

		map.AddTBInt(NodeToolbarAttr.Idx, 0, "显示顺序", true, false);
		map.AddTBInt(NodeToolbarAttr.FK_Node, 0, "节点", false,true);
		map.AddMyFile("图标");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}