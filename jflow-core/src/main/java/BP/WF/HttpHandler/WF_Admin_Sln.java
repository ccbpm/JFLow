package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import Newtonsoft.Json.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class WF_Admin_Sln extends DirectoryPageBase
{

	/** 
	 构造函数
	*/
	public WF_Admin_Sln()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 绑定流程表单
	/** 
	 获取所有节点，复制表单
	 
	 @return 
	*/
	public final String BindForm_GetFlowNodeDropList()
	{
		Nodes nodes = new Nodes();
		nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, getFK_Flow(), BP.WF.Template.NodeAttr.Step);

		if (nodes.size() == 0)
		{
			return "";
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("<select id = \"copynodesdll\"  multiple = \"multiple\" style = \"border - style:None; width: 100%; Height: 100%; \">");

		for (Node node : nodes)
		{
			sBuilder.append("<option " + (getFK_Node() == node.getNodeID() ? "disabled = \"disabled\"" : "") + " value = \"" + node.getNodeID() + "\" >" + "[" + node.getNodeID() + "]" + node.getName() + "</ option >");
		}

		sBuilder.append("</select>");

		return sBuilder.toString();
	}

	/** 
	 复制表单到节点 
	 
	 @return 
	*/
	public final String BindFrmsDtl_DoCopyFrmToNodes()
	{
		String nodeStr = this.GetRequestVal("NodeStr"); //节点string,
		String frmStr = this.GetRequestVal("frmStr"); //表单string,

		String[] nodeList = nodeStr.split("[,]", -1);
		String[] frmList = frmStr.split("[,]", -1);

		for (String node : nodeList)
		{
			if (DataType.IsNullOrEmpty(node))
			{
				continue;
			}

			int nodeid = Integer.parseInt(node);

			//删除节点绑定的表单
			DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Node=" + nodeid);

			for (String frm : frmList)
			{
				if (DataType.IsNullOrEmpty(frm))
				{
					continue;
				}

				FrmNode fn = new FrmNode();
				FrmNode frmNode = new FrmNode();

				if (fn.IsExit("mypk", frm + "_" + this.getFK_Node() + "_" + this.getFK_Flow()))
				{
					frmNode.Copy(fn);
					frmNode.setMyPK( frm + "_" + nodeid + "_" + this.getFK_Flow();
					frmNode.setFK_Flow(this.getFK_Flow());
					frmNode.setFK_Node(nodeid);
					frmNode.setFK_Frm(frm);
				}
				else
				{
					frmNode.setMyPK( frm + "_" + nodeid + "_" + this.getFK_Flow();
					frmNode.setFK_Flow(this.getFK_Flow());
					frmNode.setFK_Node(nodeid);
					frmNode.setFK_Frm(frm);
				}

				frmNode.Insert();
			}
		}

		return "操作成功！";
	}

	/** 
	 保存流程表单
	 
	 @return 
	*/
	public final String BindFrmsDtl_Save()
	{
		try
		{
			String formNos = HttpContextHelper.RequestParams("formNos"); // this.context.Request["formNos"];

			FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
			//删除已经删除的。
			for (FrmNode fn : fns)
			{
				if (formNos.contains("," + fn.getFK_Frm() + ",") == false)
				{
					fn.Delete();
					continue;
				}
			}

			// 增加集合中没有的。
			String[] strs = formNos.split("[,]", -1);
			for (String s : strs)
			{
				if (DataType.IsNullOrEmpty(s))
				{
					continue;
				}
				if (fns.Contains(FrmNodeAttr.FK_Frm, s))
				{
					continue;
				}

				FrmNode fn = new FrmNode();
				fn.setFK_Frm(s);
				fn.setFK_Flow(this.getFK_Flow());
				fn.setFK_Node(this.getFK_Node());

				fn.setMyPK( fn.getFK_Frm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow();

				fn.Save();
			}
			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err:保存失败." + ex.getMessage();
		}
	}

	/** 
	 获取表单库所有表单
	 
	 @return 
	*/
	public final String BindForm_GenerForms()
	{
		//形成树
		FlowFormTrees appendFormTrees = new FlowFormTrees();
		//节点绑定表单
		FrmNodes frmNodes = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		//所有表单类别
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Idx);

		//根节点
		BP.WF.Template.FlowFormTree root = new BP.WF.Template.FlowFormTree();
		root.Name = "表单库";
		int i = root.Retrieve(FlowFormTreeAttr.ParentNo, "0");
		if (i == 0)
		{
			root.Name = "表单库";
			root.No = "1";
			root.setNodeType("root");
			root.Insert();
		}
		root.setNodeType("root");

		appendFormTrees.AddEntity(root);

		for (SysFormTree formTree : formTrees)
		{
			//已经添加排除
			if (appendFormTrees.Contains("No", formTree.No) == true)
			{
				continue;
			}

			//根节点排除
			if (formTree.getParentNo().equals("0"))
			{
				root.No = formTree.No;
				continue;
			}



			//文件夹
			BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
			nodeFolder.No = formTree.No;
			nodeFolder.ParentNo = formTree.getParentNo();
			nodeFolder.Name = formTree.Name;
			nodeFolder.setNodeType("folder");
			if (formTree.getParentNo().equals("0"))
			{
				nodeFolder.ParentNo = root.No;
			}
			appendFormTrees.AddEntity(nodeFolder);

			//表单
			MapDatas mapS = new MapDatas();
			mapS.RetrieveByAttr(MapDataAttr.FK_FormTree, formTree.No);
			if (mapS != null && mapS.size() > 0)
			{
				for (MapData map : mapS)
				{
					BP.WF.Template.FlowFormTree formFolder = new BP.WF.Template.FlowFormTree();
					formFolder.No = map.No;
					formFolder.ParentNo = map.FK_FormTree;
					formFolder.Name = map.Name + "[" + map.No + "]";
					formFolder.setNodeType("form");
					appendFormTrees.AddEntity(formFolder);
				}
			}
		}

		String strCheckedNos = "";
		//设置选中
		for (FrmNode frmNode : frmNodes)
		{
			strCheckedNos += "," + frmNode.getFK_Frm() + ",";
		}
		//重置
		appendMenus.setLength(0);
		//生成数据
		TansEntitiesToGenerTree(appendFormTrees, root.No, strCheckedNos);
		return appendMenus.toString();
	}

	/** 
	 将实体转为树形
	 
	 @param ens
	 @param rootNo
	 @param checkIds
	*/
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();
	public final void TansEntitiesToGenerTree(Entities ens, String rootNo, String checkIds)
	{
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityTree root = tempVar instanceof EntityTree ? (EntityTree)tempVar : null;
		if (root == null)
		{
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.Name + "\"");
		appendMenus.append(",\"state\":\"open\"");

		//attributes
		BP.WF.Template.FlowFormTree formTree = root instanceof BP.WF.Template.FlowFormTree ? (BP.WF.Template.FlowFormTree)root : null;
		if (formTree != null)
		{
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\"" + formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
		}
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityTree parentEn, Entities ens, String checkIds)
	{
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);

		appendMenuSb.append("[");
		for (EntityTree item : ens)
		{
			if (item.ParentNo != parentEn.No)
			{
				continue;
			}

			if (checkIds.contains("," + item.No + ","))
			{
				appendMenuSb.append("{\"id\":\"" + item.No + "\",\"text\":\"" + item.Name + "\",\"checked\":true");
			}
			else
			{
				appendMenuSb.append("{\"id\":\"" + item.No + "\",\"text\":\"" + item.Name + "\",\"checked\":false");
			}


			//attributes
			BP.WF.Template.FlowFormTree formTree = item instanceof BP.WF.Template.FlowFormTree ? (BP.WF.Template.FlowFormTree)item : null;
			if (formTree != null)
			{
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				String treeState = "closed";
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\"" + formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
				//图标
				if (formTree.getNodeType().equals("form"))
				{
					ico = "icon-sheet";
				}
				appendMenuSb.append(",\"state\":\"" + treeState + "\"");
				appendMenuSb.append(",iconCls:\"");
				appendMenuSb.append(ico);
				appendMenuSb.append("\"");
			}
			// 增加它的子级.
			appendMenuSb.append(",\"children\":");
			AddChildren(item, ens, checkIds);
			appendMenuSb.append("},");
		}
		if (appendMenuSb.length() > 1)
		{
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 表单方案.
	/** 
	 表单方案
	 
	 @return 
	*/
	public final String BindFrms_Init()
	{
		//注册这个枚举，防止第一次运行出错.
		BP.Sys.SysEnums ses = new SysEnums("FrmEnableRole");

		String text = "";
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());

		//FrmNodeExt fns = new FrmNodeExt(this.FK_Flow, this.FK_Node);

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果没有ndFrm 就增加上.
		boolean isHaveNDFrm = false;
		for (FrmNode fn : fns)
		{
			if (fn.getFK_Frm().equals("ND" + this.getFK_Node()))
			{
				isHaveNDFrm = true;
				break;
			}
		}

		if (isHaveNDFrm == false)
		{
			FrmNode fn = new FrmNode();
			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Frm("ND" + this.getFK_Node());
			fn.setFK_Node(this.getFK_Node());

			fn.setFrmEnableRole(FrmEnableRole.Disable); //就是默认不启用.
			fn.setFrmSln(BP.WF.Template.FrmSln.forValue(0));
			//  fn.IsEdit = true;
			fn.setIsEnableLoadData(true);
			fn.Insert();
			fns.AddEntity(fn);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果没有ndFrm 就增加上.

		//组合这个实体才有外键信息.
		FrmNodeExts fnes = new FrmNodeExts();
		for (FrmNode fn : fns)
		{
			MapData md = new MapData();
			md.No = fn.getFK_Frm();
			if (md.IsExits == false)
			{
				fn.Delete(); //说明该表单不存在了，就需要把这个删除掉.
				continue;
			}

			FrmNodeExt myen = new FrmNodeExt(fn.MyPK);
			fnes.AddEntity(myen);
		}

		//把json数据返回过去.
		return fnes.ToJson();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 表单方案.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 字段权限.

	public static class FieldsAttrs
	{
		public int idx;
		public String KeyOfEn;
		public String Name;
		public String LGTypeT;
		public boolean UIVisible;
		public boolean UIIsEnable;
		public boolean IsSigan;
		public String DefVal;
		public boolean IsNotNull;
		public String RegularExp;
		public boolean IsWriteToFlowTable;
		/** 
		  add new attr 是否写入流程注册表
		*/
		public boolean IsWriteToGenerWorkFlow;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 字段权限.

}