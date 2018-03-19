package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;

import BP.En.QueryObject;
import BP.Tools.StringHelper;
import BP.WF.Template.ShowWhere;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;

/** 
关于:/WF/FlowFormTree/FlowFormTreeEdit.aspx 功能界面使用说明：
此页面是功能调用页面，它需要两个参数. FK_Flow,WorkID.
是用来解决流程表单树的查看与编辑，他对应的是流程编号而非节点编号.
主要应用到如下场景:
1, 需要查看流程信息，而非指定特定的节点。
2，需要查看流程信息，并且可以编辑表单信息，应用于数据采集，即时该流程已经走完。
3, 在该功能工具栏上，仅具有， 保存，轨迹，关闭三个按钮。

相关：
1, 正常流程节点表单树的信息绑定，实在节点属性，节点表单中设置。
2, 节点绑定的表单在节点属性设置. 流程绑定的表单在流程属性里设置.
3, 节点绑定流程表单的功能是页面是 FlowFormTreeView.aspx 需要的参数是 FK_Flow,WorkID,FK_Node,FID
*/
public class FlowFormTreeEditModel{
	
	public String toolBars;
	
	private HttpServletRequest request;

	public FlowFormTreeEditModel(HttpServletRequest request) {
		this.request = request;
	}
	
	/** 
	 初始化工具栏
	 
	*/
	public void InitToolsBar()
	{
		StringBuilder toolsDefault = new StringBuilder();
		//保存
		toolsDefault.append("<a id=\"save\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-save'\" onclick=\"EventFactory('save')\">保存</a>");

		//轨迹
		toolsDefault.append("<a id=\"Track\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-flowmap'\" onclick=\"EventFactory('showchart')\">轨迹</a>");
		////查询
		//toolsDefault += "<a id=\"Search\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-search'\" onclick=\"EventFactory('search')\">查询</a>";
		//扩展工具，显示位置为工具栏类型
		NodeToolbars extToolBars = new NodeToolbars();
		QueryObject info = new QueryObject(extToolBars);
		info.AddWhere(NodeToolbarAttr.FK_Node, this.getFK_Node());
		info.addAnd();
		info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Toolbar.getValue());
		info.DoQuery();
		for (NodeToolbar item : extToolBars.ToJavaList())
		{
			String url = "";
			if (StringHelper.isNullOrEmpty(item.getUrl()))
			{
				continue;
			}

			String urlExt = this.getRequestParas();
			url = item.getUrl();
			if (url.contains("?"))
			{
				url += urlExt;
			}
			else
			{
				url += "?" + urlExt;
			}
			toolsDefault.append("<a target=\"").append(item.getTarget()).append("\" href=\"").append(url).append("\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-new'\">").append(item.getTitle()).append("</a>");
		}
		//关闭
		toolsDefault.append("<a id=\"closeWin\" href=\"#\" class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-no'\" onclick=\"EventFactory('closeWin')\">关闭</a>");
		//添加内容
		this.toolBars = toolsDefault.toString();
	}

	private String getFK_Node() {
		return request.getParameter("FK_Node");
	}

	public final String getRequestParas()
	{
		String urlExt = "";
		String rawUrl = request.getQueryString();
		rawUrl = "&" + rawUrl.substring(rawUrl.indexOf('?') + 1);
		String[] paras = rawUrl.split("[&]", -1);
		for (String para : paras)
		{
			if (para == null || para.equals("") || para.contains("=") == false)
			{
				continue;
			}
			urlExt += "&" + para;
		}
		return urlExt;
	}
}
