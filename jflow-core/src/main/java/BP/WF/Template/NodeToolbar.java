package BP.WF.Template;

import BP.En.EntityOID;
import BP.En.Map;
import BP.En.UAC;

/** 
 工具栏.	 
 
*/
public class NodeToolbar extends EntityOID
{

		
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
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(NodeToolbarAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(NodeToolbarAttr.FK_Node, value);
	}
	public final String getTitle()
	{
		return this.GetValStringByKey(NodeToolbarAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(NodeToolbarAttr.Title, value);
	}
	public final String getUrl()
	{
		String s= this.GetValStringByKey(NodeToolbarAttr.Url);
		if (s.contains("?") == false && !this.getTarget().toLowerCase().equals("javascript"))
		{
			s = s+"?1=2";
		}
		return s;
	}
	public final void setUrl(String value)
	{
		SetValByKey(NodeToolbarAttr.Url, value);
	}
	public final String getTarget()
	{
		return this.GetValStringByKey(NodeToolbarAttr.Target);
	}
	public final void setTarget(String value)
	{
		SetValByKey(NodeToolbarAttr.Target, value);
	}
	/** 
	 显示在那里？
	 
	*/
	public final ShowWhere getShowWhere()
	{
		return ShowWhere.forValue(this.GetValIntByKey(NodeToolbarAttr.ShowWhere));
	}
	public final void setShowWhere(ShowWhere value)
	{
		SetValByKey(NodeToolbarAttr.ShowWhere,  value.getValue());
	}

		///#endregion


		
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
		this.setOID(oid);;
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
           map.AddDDLSysEnum(NodeToolbarAttr.ShowWhere, 1, "显示位置", true,true, NodeToolbarAttr.ShowWhere,"@0=树形表单@1=工具栏");

           map.AddTBInt(NodeToolbarAttr.Idx, 0, "显示顺序", true, false);
           map.AddTBInt(NodeToolbarAttr.FK_Node, 0, "节点", false,true);
           map.AddMyFile("图标");


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}