package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 工具栏.
*/
public class NodeToolbar extends EntityOID
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
			//  uac.OpenForSysAdmin();
		uac.OpenForAdmin(); // 2020.5.15zsy修改
		return uac;
	}
	/** 
	 工具栏的事务编号
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(NodeToolbarAttr.FK_Node);
	}
	public final void setFK_Node(int value)throws Exception
	{SetValByKey(NodeToolbarAttr.FK_Node, value);
	}
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(NodeToolbarAttr.Title);
	}
	public final void setTitle(String value)throws Exception
	{SetValByKey(NodeToolbarAttr.Title, value);
	}
	public final String getUrl() throws Exception {
		String s = this.GetValStringByKey(NodeToolbarAttr.UrlExt);

		if (this.getExcType() != 1 && s.contains("?") == false && !this.getTarget().toLowerCase().equals("javascript"))
		{
			s = s + "?1=2";
		}
		return s;
	}
	public final void setUrl(String value)throws Exception
	{SetValByKey(NodeToolbarAttr.UrlExt, value);
	}
	public final String getTarget() throws Exception
	{
		return this.GetValStringByKey(NodeToolbarAttr.Target);
	}
	public final void setTarget(String value)throws Exception
	{SetValByKey(NodeToolbarAttr.Target, value);
	}
	/** 
	 显示在那里？
	*/
	public final ShowWhere getShowWhere() throws Exception {
		return ShowWhere.forValue(this.GetValIntByKey(NodeToolbarAttr.ShowWhere));
	}
	public final void setShowWhere(ShowWhere value)throws Exception
	{SetValByKey(NodeToolbarAttr.ShowWhere, value.getValue());
	}
	/** 
	 执行类型
	*/
	public final int getExcType() throws Exception
	{
		return this.GetValIntByKey(NodeToolbarAttr.ExcType);
	}
	public final void setExcType(int value)throws Exception
	{SetValByKey(NodeToolbarAttr.ExcType, value);
	}
	/** 
	 显示在工具栏中
	*/
	public final boolean isMyFlow() throws Exception
	{
		return this.GetValBooleanByKey(NodeToolbarAttr.IsMyFlow);
	}
	public final void setMyFlow(boolean value)throws Exception
	{SetValByKey(NodeToolbarAttr.IsMyFlow, value);
	}
	//显示在流程树中
	public final boolean isMyTree() throws Exception
	{
		return this.GetValBooleanByKey(NodeToolbarAttr.IsMyTree);
	}
	public final void setMyTree(boolean value)throws Exception
	{SetValByKey(NodeToolbarAttr.IsMyTree, value);
	}
	//显示在工作查看器
	public final boolean isMyView() throws Exception
	{
		return this.GetValBooleanByKey(NodeToolbarAttr.IsMyView);
	}
	public final void setMyView(boolean value)throws Exception
	{SetValByKey(NodeToolbarAttr.IsMyView, value);
	}

	//显示在抄送工具栏中
	public final boolean isMyCC() throws Exception
	{
		return this.GetValBooleanByKey(NodeToolbarAttr.IsMyCC);
	}
	public final void setMyCC(boolean value)throws Exception
	{SetValByKey(NodeToolbarAttr.IsMyCC, value);
	}
	//图片附件路径
	public final String getIconPath() throws Exception
	{
		return this.GetValStringByKey(NodeToolbarAttr.IconPath);
	}
	public final void setIconPath(String value)throws Exception
	{SetValByKey(NodeToolbarAttr.IconPath, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 工具栏
	*/
	public NodeToolbar() {
	}
	/** 
	 工具栏
	 
	 param _oid 工具栏ID	
	*/
	public NodeToolbar(int oid) throws Exception {
		this.setOID(oid);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_NodeToolbar", "自定义工具栏");

		map.AddTBIntPKOID();
		map.AddTBInt(NodeToolbarAttr.FK_Node, 0, "节点", false, true);
		map.AddTBString(NodeToolbarAttr.Title, null, "标题", true, false, 0, 100, 100, true);

			// 执行类型.
		map.AddDDLSysEnum(NodeToolbarAttr.ExcType, 0, "执行类型", true, true, "ToobarExcType", "@0=超链接@1=函数");

		map.AddTBString(NodeToolbarAttr.UrlExt, null, "连接/函数", true, false, 0, 500, 300, true);
		map.AddTBString(NodeToolbarAttr.Target, null, "目标", true, false, 0, 100, 100, true);

			//显示位置.
		map.AddDDLSysEnum(NodeToolbarAttr.ShowWhere, 1, "显示位置", false,true, NodeToolbarAttr.ShowWhere, "@0=树形表单@1=工具栏@2=抄送工具栏");

		map.AddBoolean(NodeToolbarAttr.IsMyFlow, false, "工作处理器", true, true);
		map.AddBoolean(NodeToolbarAttr.IsMyTree, false, "流程树", true, true);
		map.AddBoolean(NodeToolbarAttr.IsMyView, false, "工作查看器", true, true);
		map.AddBoolean(NodeToolbarAttr.IsMyCC, false, "抄送工具栏", true, true);

		map.AddTBString(NodeToolbarAttr.IconPath, null, "ICON路径", true, false, 0, 100, 100, true);
		String msg = "提示：";
		msg += "\t\n 1. 给工具栏按钮设置图标两种方式,上传图标模式与设置指定的Icon的ID模式.";
		msg += "\t\n 2. 我们优先解决Icon的ID解析模式.";
		msg += "\t\n 3. 比如: ./Img/Btn/Save.png ";
		map.SetHelperAlert(NodeToolbarAttr.IconPath, msg);


		map.AddTBInt(NodeToolbarAttr.Idx, 0, "显示顺序", true, false);
		map.AddMyFile("图标", null, null);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}