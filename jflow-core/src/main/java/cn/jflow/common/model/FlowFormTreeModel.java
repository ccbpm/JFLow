package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntitySimpleTree;
import BP.En.EntityTree;
import BP.En.QueryObject;
import BP.Sys.PubClass;
import BP.Tools.StringHelper;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.SysForm;
import BP.WF.Template.SysFormAttr;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTrees;
import BP.WF.Template.SysForms;
import BP.Web.WebUser;

public class FlowFormTreeModel extends BaseModel{

	public FlowFormTreeModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	

	public void loadPage()
	{
		if (!WebUser.getNo().equals("admin"))
		{
			ToErrorPage("@非法的用户必须由admin才能操作，现在登录用户是：" + WebUser.getNo());
			return;
			//throw new RuntimeException("@非法的用户必须由admin才能操作，现在登录用户是：" + WebUser.getNo());
		}

		String method = "";
		//返回值
		String s_responsetext = "";
		if (!StringHelper.isNullOrEmpty(getParameter("method")))
		{
			method = getParameter("method").toString();
		}

		if (method.equals("getflowformtree"))
		{
				s_responsetext = GetFlowFormTree();
		}
		else if (method.equals("getnodeformtree"))
		{
				s_responsetext = GetNodeFormTree();
		}
		else if (method.equals("saveflowformtree"))
		{
				s_responsetext = SaveFlowFormTree();
		}
		else if (method.equals("savenodeformtree"))
		{
				s_responsetext = SaveNodeFormTree();
		}
		if (StringHelper.isNullOrEmpty(s_responsetext))
		{
			s_responsetext = "";
		}

		//组装ajax字符串格式,返回调用客户端
		PubClass.ResponseWriteScript(s_responsetext);
	}

	/** 
	 获取流程表单树
	 
	 @return 
	*/
	private String GetFlowFormTree()
	{
		String flowId = getParameter("flowId");
		String parentNo = getParameter("parentno");
		String isFirstLoad = getParameter("isFirstLoad");

		//获取子节点内容
		SysFormTrees flowFormTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(flowFormTrees);
		objInfo.AddWhere("ParentNo", parentNo);
		objInfo.addOrderBy("Idx");
		objInfo.DoQuery();

		if (!StringHelper.isNullOrEmpty(isFirstLoad) && isFirstLoad.equals("true"))
		{
			SysFormTree formTree = new SysFormTree("0");
			StringBuilder appSend = new StringBuilder();
			appSend.append("[");
			appSend.append("{");
			appSend.append("\"id\":\"0\"");
			appSend.append(",\"text\":\"" + formTree.getName() + "\"");

			appSend.append(",iconCls:\"icon-0\"");
			appSend.append(",\"children\":");
			appSend.append("[");
			//获取节点下的表单
			SysForms sysForms = new SysForms();
			QueryObject objFlowForms = new QueryObject(sysForms);
			objFlowForms.AddWhere(SysFormAttr.FK_FormTree, parentNo);
			objFlowForms.addOrderBy(SysFormAttr.Name);
			objFlowForms.DoQuery();

			//添加子项文件夹
			for (SysFormTree item : flowFormTrees.ToJavaList())
			{
				//获取已选择项
				FrmNodes flowForms = new FrmNodes();
				QueryObject objFlowForm = new QueryObject(flowForms);
				objFlowForm.AddWhere("FK_Flow", flowId);
				objFlowForm.addAnd();
				objFlowForm.AddWhere("FK_FlowFormTree", item.getNo());
				objFlowForm.DoQuery();

				if (flowForms != null && flowForms.size() > 0)
				{

				}
			}
			//添加表单
			for (SysForm sysForm : sysForms.ToJavaList())
			{
				appSend.append("{");
				appSend.append("\"id\":\"0\"");
				appSend.append(",\"text\":\"" + formTree.getName() + "\"");

				appSend.append(",iconCls:\"icon-3\"");
				appSend.append("},");
			}
			appSend.append("]");
			appSend.append("}");
			appSend.append("]");
			return appSend.toString();
		}

		return "";
	}
	/** 
	 获取节点表单树
	 
	 @return 
	*/
	private String GetNodeFormTree()
	{
		return "";
	}
	/** 
	 保存流程表单树
	 
	 @return 
	*/
	private String SaveFlowFormTree()
	{
		return "";
	}
	/** 
	 保存节点表单树
	 
	 @return 
	*/
	private String SaveNodeFormTree()
	{
		return "";
	}

	/** 
	 获取树节点列表
	 
	 @param ens
	 @param checkIds
	 @return 
	*/
	//public final String GetTreeList(Entities ens, String checkIds, String unCheckIds)
	public final String GetTreeList(EntitySimpleTree ens, String checkIds, String unCheckIds)
	{
		StringBuilder appSend = new StringBuilder();
		appSend.append("[");
		//for (Entity entity : ens.ToJavaListEn())
		for (int i=0;i<ens.getRow().size();i++)
		{
			EntitySimpleTree  item = (EntitySimpleTree) ens.getRow().get(i);
			if (appSend.length() > 1)
			{
				appSend.append(",{");
			}
				else
				{
					appSend.append("{");
				}

			appSend.append("\"id\":\"" + item.getNo() + "\"");
			appSend.append(",\"text\":\"" + item.getName() + "\"");

			SysFormTree node = (SysFormTree)((item instanceof SysFormTree) ? item : null);

			//文件夹节点图标
			String ico = "icon-tree_folder";
			//判断未完全选中
			if (unCheckIds.contains("," + item.getNo() + ","))
			{
				ico = "collaboration";
			}

			appSend.append(",iconCls:\"");
			appSend.append(ico);
			appSend.append("\"");

			if (checkIds.contains("," + item.getNo() + ","))
			{
				appSend.append(",\"checked\":true");
			}

			//判断是否还有子节点icon-3
			//BP.GPM.Menus menus = new BP.GPM.Menus();
			//menus.RetrieveByAttr("ParentNo", item.No);

			//if (menus != null && menus.Count > 0)
			//{
			//    appSend.Append(",state:\"closed\"");
			//    appSend.Append(",\"children\":");
			//    appSend.Append("[{");
			//    appSend.Append(string.Format("\"id\":\"{0}\",\"text\":\"{1}\"", item.No + "01", "加载中..."));
			//    appSend.Append("}]");
			//}
			appSend.append("}");
		}
		appSend.append("]");

		return appSend.toString();
	}

}
