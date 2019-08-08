package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.Json;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HttpContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 页面功能实体
 */
public class WF_Admin_Sln extends WebContralBase {
	
	public WF_Admin_Sln() {

	}

	/**
	 * 页面功能实体
	 * 
	 * @param mycontext
	 */
	public WF_Admin_Sln(HttpContext mycontext) {
		this.context = mycontext;
	}

	// /#region 绑定流程表单
	/**
	 * 获取流程所有节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindForm_GenderFlowNode() throws Exception {
		Node nd = new Node(this.getFK_Node());

		// 规范做法.
		Nodes nds = new Nodes(nd.getFK_Flow());
		return nds.ToJson();

	}

	/**
	 * 获取所有节点，复制表单
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindForm_GetFlowNodeDropList() throws Exception {
		Nodes nodes = new Nodes();
		nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, this.getFK_Flow(),
				BP.WF.Template.NodeAttr.Step);

		if (nodes.size() == 0) {
			return "";
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("<select id = \"copynodesdll\"  multiple = \"multiple\" style = \"border - style:None; width: 100%; Height: 100%; \">");

		for (Node node : nodes.Tolist()) {
			sBuilder.append("<option "
					+ (this.getFK_Node() == node.getNodeID() ? "disabled = \"disabled\""
							: "") + " value = \"" + node.getNodeID() + "\" >"
					+ "[" + node.getNodeID() + "]" + node.getName()
					+ "</ option >");
		}

		sBuilder.append("</select>");

		return sBuilder.toString();
	}

	/**
	 * 复制表单到节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindFrmsDtl_DoCopyFrmToNodes() throws Exception {
		String nodeStr = this.GetRequestVal("NodeStr"); // 节点string,
		String frmStr = this.GetRequestVal("frmStr"); // 表单string,

		String[] nodeList = nodeStr.split("[,]", -1);
		String[] frmList = frmStr.split("[,]", -1);

		for (String node : nodeList) {
			if (DataType.IsNullOrEmpty(node)) {
				continue;
			}

			int nodeid = Integer.parseInt(node);

			DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Node=" + nodeid);

			for (String frm : frmList) {
				if (DataType.IsNullOrEmpty(frm)) {
					continue;
				}

				FrmNode fn = new FrmNode();
				if (!fn.IsExit("mypk",
						frm + "_" + nodeid + "_" + this.getFK_Flow())) {
					fn.setFK_Frm(frm);
					fn.setFK_Node(nodeid);
					fn.setFK_Flow(this.getFK_Flow());

					fn.Insert();
				}
			}
		}

		return "操作成功！";
	}

	/**
	 * 保存流程表单
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindFrmsDtl_Save() throws Exception {
		try {
			String formNos = this.GetRequestVal("formNos");

			FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
			// 删除已经删除的。
			for (FrmNode fn : fns.Tolist()) {
				if (formNos.contains("," + fn.getFK_Frm() + ",") == false) {
					fn.Delete();
					continue;
				}
			}

			// 增加集合中没有的。
			String[] strs = formNos.split("[,]", -1);
			for (String s : strs) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(s)) {
					continue;
				}
				if (fns.Contains(FrmNodeAttr.FK_Frm, s)) {
					continue;
				}

				FrmNode fn = new FrmNode();
				fn.setFK_Frm(s);
				fn.setFK_Flow(this.getFK_Flow());
				fn.setFK_Node(this.getFK_Node());

				fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_"
						+ fn.getFK_Flow());
				fn.Save();
			}
			return "true";
		} catch (RuntimeException ex) {
			return "err:保存失败。";
		}
	}

	/**
	 * 获取表单库所有表单
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindForm_GenerForms() throws Exception {
		// 形成树
		FlowFormTrees appendFormTrees = new FlowFormTrees();
		// 节点绑定表单
		FrmNodes frmNodes = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		// 所有表单类别
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Idx);

		// 根节点
		BP.WF.Template.FlowFormTree root = new BP.WF.Template.FlowFormTree();
		root.setNo("00");
		root.setParentNo("0");
		root.setName("表单库");
		root.setNodeType("root");
		appendFormTrees.AddEntity(root);

		for (SysFormTree formTree : formTrees.Tolist()) {
			
			// 已经添加排除
			if (appendFormTrees.Contains("No", formTree.getNo()) == true) {
				continue;
			}
			// 根节点排除
			if (formTree.getParentNo().equals("0")) {
				root.setNo(formTree.getNo());
				continue;
			}
			// 文件夹
			BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
			nodeFolder.setNo(formTree.getNo());
			nodeFolder.setParentNo(formTree.getParentNo());
			nodeFolder.setName(formTree.getName());
			nodeFolder.setNodeType("folder");
			if (formTree.getParentNo().equals("0")) {
				nodeFolder.setParentNo(root.getNo());
			}
			appendFormTrees.AddEntity(nodeFolder);

			// 表单
			MapDatas mapS = new MapDatas();
			mapS.RetrieveByAttr(MapDataAttr.FK_FormTree, formTree.getNo());
			if (mapS != null && mapS.size() > 0) {
				for (MapData map : mapS.Tolist()) {
					BP.WF.Template.FlowFormTree formFolder = new BP.WF.Template.FlowFormTree();
					formFolder.setNo(map.getNo());
					formFolder.setParentNo(map.getFK_FormTree());
					formFolder.setName(map.getName() + "[" + map.getNo() + "]");
					formFolder.setNodeType("form");
					appendFormTrees.AddEntity(formFolder);
				}
			}
		}

		String strCheckedNos = "";
		// 设置选中
		for (FrmNode frmNode : frmNodes.Tolist()) {
			strCheckedNos += "," + frmNode.getFK_Frm() + ",";
		}
		// 重置
		appendMenus = new StringBuilder();
		// 生成数据
		TansEntitiesToGenerTree(appendFormTrees, root.getNo(), strCheckedNos);
		return appendMenus.toString();
	}

	/**
	 * 将实体转为树形
	 * 
	 * @param ens
	 * @param rootNo
	 * @param checkIds
	 */
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();

	public final void TansEntitiesToGenerTree(Entities ens, String rootNo,
			String checkIds) {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityTree root = (EntityTree) ((tempVar instanceof EntityTree) ? tempVar
				: null);
		if (root == null) {
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");
		appendMenus.append(",\"state\":\"open\"");

		// attributes
		BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((root instanceof BP.WF.Template.FlowFormTree) ? root
				: null);
		if (formTree != null) {
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\""
					+ formTree.getNodeType() + "\",\"IsEdit\":\""
					+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
		}
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityTree parentEn, Entities ens,
			String checkIds) {
		appendMenus.append(appendMenuSb);
		appendMenuSb = new StringBuilder();
		// appendMenuSb.clear();

		appendMenuSb.append("[");
		for (Entity en : ens) {
			EntityTree item = (EntityTree) en;
			if (!item.getParentNo().equals(parentEn.getNo())) {
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ",")) {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":true");
			} else {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":false");
			}

			// attributes
			BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((item instanceof BP.WF.Template.FlowFormTree) ? item
					: null);
			if (formTree != null) {
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				String treeState = "closed";
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\""
						+ formTree.getNodeType() + "\",\"IsEdit\":\""
						+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
				// 图标
				if (formTree.getNodeType().equals("form")) {
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
		if (appendMenuSb.length() > 1) {
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb = new StringBuilder();
	}
	// /#region 表单方案.
	/**
	 * 表单方案
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindFrms_Init() throws Exception {

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());

		boolean isHaveNDFrm = false;
		for (FrmNode fn : fns.Tolist()) {
			if (fn.getFK_Frm().equals("ND" + this.getFK_Node())) {
				isHaveNDFrm = true;
				break;
			}
		}

		if (isHaveNDFrm == false) {
			FrmNode fn = new FrmNode();
			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Frm("ND" + this.getFK_Node());
			fn.setFK_Node(this.getFK_Node());

			fn.setFrmEnableRole(FrmEnableRole.Disable); // 就是默认不启用.
			fn.setFrmSlnInt(0);
			// fn.IsEdit = true;
			fn.setIsEnableLoadData(true);

			fn.setFK_Frm("ND" + this.getFK_Node());
 
 			fn.Insert();
 
			fns.AddEntity(fn);
		}
		// /#endregion 如果没有ndFrm 就增加上.

		// 组合这个实体才有外键信息.
		FrmNodeExts fnes = new FrmNodeExts();
		for (FrmNode fn : fns.Tolist()) {
			MapData md = new MapData();
			md.setNo(fn.getFK_Frm());
			if (md.getIsExits() == false) {
				fn.Delete(); // 说明该表单不存在了，就需要把这个删除掉.
				continue;
			}

			FrmNodeExt myen = new FrmNodeExt(fn.getMyPK());
			fnes.AddEntity(myen);
		}

		// 把json数据返回过去.
		return fnes.ToJson();
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindFrms_Delete() throws Exception {
		FrmNodeExt myen = new FrmNodeExt(this.getMyPK());
		myen.Delete();
		return "删除成功.";
	}

	public final String BindFrms_DoOrder() throws Exception {
		FrmNode myen = new FrmNode(this.getMyPK());

		if (this.GetRequestVal("OrderType").equals("Up")) {
			myen.DoUp();
		} else {
			myen.DoDown();
		}

		return "执行成功...";
	}
	 private class FieldsAttrs
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
			public String IsWriteToGenerWorkFlow;
		}
	
	// /#endregion 表单方案.

	
	// /#region 字段权限.
	public final String Fields_Init() throws Exception
	{
		// 查询出来解决方案.
		FrmFields fss = new FrmFields(this.getFK_MapData(), this.getFK_Node());

		// 处理好.
		MapAttrs attrs = new MapAttrs();
		//增加排序
		QueryObject obj = new QueryObject(attrs);
		obj.AddWhere(MapAttrAttr.FK_MapData, this.getFK_MapData());
		obj.addOrderBy(MapAttrAttr.Y, MapAttrAttr.X);
		obj.DoQuery();

		java.util.ArrayList<FieldsAttrs> fieldsAttrsList = new java.util.ArrayList<FieldsAttrs>();
		int idx = 0;
		for (MapAttr attr : attrs.ToJavaList())
		{
			if(attr.getKeyOfEn() == BP.WF.WorkAttr.RDT)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.FID)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.OID)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.Rec)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.MyNum)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.MD5)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.Emps)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.CDT)
				continue;

			fieldsAttrsList.add(new FieldsAttrs());
			fieldsAttrsList.get(idx).idx = idx;
			fieldsAttrsList.get(idx).KeyOfEn = attr.getKeyOfEn();
			fieldsAttrsList.get(idx).Name=attr.getName();
			fieldsAttrsList.get(idx).LGTypeT = attr.getLGTypeT();

			Object tempVar = fss.GetEntityByKey(FrmFieldAttr.KeyOfEn, attr.getKeyOfEn());
			FrmField sln = (FrmField)((tempVar instanceof FrmField) ? tempVar : null);
			if (sln == null)
			{
				fieldsAttrsList.get(idx).UIVisible = false;
				fieldsAttrsList.get(idx).UIIsEnable = false;
				fieldsAttrsList.get(idx).IsSigan = false;
				fieldsAttrsList.get(idx).DefVal = "";
				fieldsAttrsList.get(idx).IsNotNull = false;
				fieldsAttrsList.get(idx).IsSigan = false;
				fieldsAttrsList.get(idx).RegularExp = "";
				fieldsAttrsList.get(idx).IsWriteToFlowTable = false;
				//fieldsAttrsList[idx].IsWriteToGenerWorkFlow = sln.IsWriteToGenerWorkFlow;

				//this.Pub2.AddTD("<a href=\"javascript:EditSln('" + this.FK_MapData + "','" + this.SlnString + "','" + attr.KeyOfEn + "')\" >Edit</a>");
			}
			else
			{

				fieldsAttrsList.get(idx).UIVisible = sln.getUIVisible();
				fieldsAttrsList.get(idx).UIIsEnable = sln.getUIIsEnable();
				fieldsAttrsList.get(idx).IsSigan = sln.getIsSigan();
				fieldsAttrsList.get(idx).DefVal = sln.getDefVal();
				fieldsAttrsList.get(idx).IsNotNull = sln.getIsNotNull();
				fieldsAttrsList.get(idx).IsSigan = sln.getIsSigan();
				fieldsAttrsList.get(idx).RegularExp = sln.getRegularExp();
				fieldsAttrsList.get(idx).IsWriteToFlowTable = sln.getIsWriteToFlowTable();
				//fieldsAttrsList[idx].IsWriteToGenerWorkFlow = sln.IsWriteToGenerWorkFlow;

				//this.Pub2.AddTD("<a href=\"javascript:DelSln('" + this.FK_MapData + "','" + this.FK_Flow + "','" + this.FK_Node + "','" + this.FK_Node + "','" + attr.KeyOfEn + "')\" ><img src='../Img/Btn/Delete.gif' border=0/>Delete</a>");
			}

			idx++;
			//this.Pub2.AddTREnd();
		}
		String fsj = Json.ToJson(fieldsAttrsList);
		return Json.ToJson(fieldsAttrsList);
	}

	public final String Fields_Save() throws Exception {
		Node currND = new Node(this.getFK_Node());

		String FieldsAttrsObj = this.GetRequestVal("FieldsAttrsObj");
		Gson gson = new GsonBuilder().serializeNulls().create();
		List<FieldsAttrs> fieldsAttrsList = gson.fromJson(FieldsAttrsObj, new TypeToken<List<FieldsAttrs>>() {
        }.getType());//对于不是类的情况，用这个参数给出
		
		if (fieldsAttrsList != null)
		{
			MapAttrs attrs = new MapAttrs();
			//增加排序
			QueryObject obj = new QueryObject(attrs);
			obj.AddWhere(MapAttrAttr.FK_MapData, this.getFK_MapData());
			obj.DoQuery();

			for (MapAttr attr : attrs.ToJavaList())
			{
				for (FieldsAttrs fieldsAttrs : fieldsAttrsList)
				{
					if (!attr.getKeyOfEn().equals(fieldsAttrs.KeyOfEn))
					{
						continue;
					}

					if (currND.getHisFormType() == NodeFormType.RefOneFrmTree || currND.getHisFormType() == NodeFormType.SheetTree)
					{
						attr.setUIVisible(fieldsAttrs.UIVisible);
						attr.setUIIsEnable(fieldsAttrs.UIIsEnable);
						attr.setIsSigan(fieldsAttrs.IsSigan);
						attr.setDefVal(fieldsAttrs.DefVal);
						attr.setUIIsInput(fieldsAttrs.IsNotNull);
						attr.setFK_MapData(this.getFK_MapData());
						attr.setKeyOfEn(attr.getKeyOfEn());
						attr.setName(attr.getName());
						attr.Update();

						//如果是表单库表单，需要写入MapAttr
						if (!StringUtils.isEmpty(fieldsAttrs.RegularExp))
						{
							MapExt ext = new MapExt();
							boolean extisExit = ext.IsExit("MyPK", "RegularExpression_" + this.getFK_MapData() + "_" + fieldsAttrs.KeyOfEn + "_onchange");

							ext.setFK_MapData(this.getFK_MapData());
							ext.setExtType(MapExtXmlList.RegularExpression);
							ext.setDoWay(0);
							ext.setAttrOfOper(fieldsAttrs.KeyOfEn);
							ext.setDoc(fieldsAttrs.RegularExp);
							ext.setTag("onchange");
							ext.setTag1("格式不正确！");

							if (extisExit)
							{
								ext.Update();
							}
							else
							{
								ext.setMyPK("RegularExpression_" + this.getFK_MapData() + "_" + fieldsAttrs.KeyOfEn + "_onchange");
								ext.Insert();
							}
						}
					}
					FrmField frmField = new FrmField();
					boolean isExit = frmField.IsExit("mypk", this.getFK_MapData() + "_" + this.getFK_Flow() + "_" + this.getFK_Node() + "_" + fieldsAttrs.KeyOfEn + "_" + FrmEleType.Field);

					frmField.setUIVisible(fieldsAttrs.UIVisible);
					frmField.setUIIsEnable(fieldsAttrs.UIIsEnable);
					frmField.setIsSigan(fieldsAttrs.IsSigan);
					frmField.setDefVal(fieldsAttrs.DefVal);
					frmField.setIsNotNull(fieldsAttrs.IsNotNull);
					frmField.setRegularExp(fieldsAttrs.RegularExp);
					frmField.setIsWriteToFlowTable(fieldsAttrs.IsWriteToFlowTable);
					//frmField.IsWriteToGenerWorkFlow(fieldsAttrs.IsWriteToGenerWorkFlow);  //sln无此属性
					frmField.setFK_Node(this.getFK_Node());
					frmField.setFK_Flow(this.getFK_Flow());
					frmField.setFK_MapData(this.getFK_MapData());
					frmField.setKeyOfEn(attr.getKeyOfEn());
					frmField.setName(attr.getName());
					 frmField.setEleType(FrmEleType.Field);
					if (isExit)
					{
						frmField.Update();
					}
					else
					{
						frmField.InitMyPKVals();
						frmField.Insert();
					}
				}
			}

			return fieldsAttrsList.size() + "";
		}

		return "0";
	}

	
	// /#endregion 字段权限.

	
	
	  ///#region 附件权限.
			public class AthsAttrs
			{
				public int idx;
				public String NoOfObj;
				public String Name;
				public String UploadTypeT;
				public String PrimitiveAttrTag;
				public String EditTag;
				public String DelTag;
			}
			public final String Aths_Init() throws Exception
			{
				BP.Sys.FrmAttachments fas = new BP.Sys.FrmAttachments();
				fas.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData());

				java.util.ArrayList<AthsAttrs> athsAttrsList = new java.util.ArrayList<AthsAttrs>();
				int idx = 0;
				for (BP.Sys.FrmAttachment item : fas.ToJavaList())
				{
					if (item.getFK_Node() != 0)
					{
						continue;
					}

					athsAttrsList.add(new AthsAttrs ());
					athsAttrsList.get(idx).idx = idx + 1;
					athsAttrsList.get(idx).NoOfObj = item.getNoOfObj();
					athsAttrsList.get(idx).Name=item.getName();
					athsAttrsList.get(idx).UploadTypeT = item.getUploadTypeT();
					athsAttrsList.get(idx).PrimitiveAttrTag = "<a href=\"javascript:EditFJYuanShi('" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">原始属性</a>";
					athsAttrsList.get(idx).EditTag = "<a href=\"javascript:EditFJ('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">编辑</a>";

					FrmAttachment en = new FrmAttachment();
					en.setMyPK(this.getFK_MapData() + "_" + item.getNoOfObj() + "_" + this.getFK_Node());
					if (en.RetrieveFromDBSources() == 0)
					{
						athsAttrsList.get(idx).DelTag = "";
					}
					else
					{
						athsAttrsList.get(idx).DelTag = "<a href=\"javascript:DeleteFJ('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">删除</a>";
					}

					idx++;
				}

				return Json.ToJson(athsAttrsList);
			}
			
			
	public final String Aths_Save() {
		return "";
	}
	  ///#region 从表权限.
			public class DtlsAttrs
			{
				public int idx;
				public String No;
				public String Name;
				/** 
				 原始属性标签
				 
				*/
				public String PrimitiveAttrTag;
				public String EditTag;
				public String DelTag;
			}
	
	// /#region 从表权限.
	public final String Dtls_Init() throws Exception {
		 BP.Sys.MapDtls dtls = new BP.Sys.MapDtls();
			dtls.Retrieve(MapDtlAttr.FK_MapData, this.getFK_MapData());
			java.util.ArrayList<DtlsAttrs> dtlsAttrsList = new java.util.ArrayList<DtlsAttrs>();
			int idx = 0;

			for (BP.Sys.MapDtl item : dtls.ToJavaList())
			{
				if (item.getFK_Node() != 0)
				{
					continue;
				}

				dtlsAttrsList.add(new DtlsAttrs ());
				dtlsAttrsList.get(idx).idx = idx + 1;
				dtlsAttrsList.get(idx).No = item.getNo();
				dtlsAttrsList.get(idx).Name=item.getName();
				dtlsAttrsList.get(idx).PrimitiveAttrTag = "<a href=\"javascript:EditDtlYuanShi('" + this.getFK_MapData() + "','" + item.getNo() + "')\">原始属性</a>";
				dtlsAttrsList.get(idx).EditTag = "<a href=\"javascript:EditDtl('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNo() + "')\">编辑</a>";

				MapDtl en = new MapDtl();
				en.setNo(item.getNo() + "_" + this.getFK_Node());
				if (en.RetrieveFromDBSources() == 0)
				{
					dtlsAttrsList.get(idx).DelTag = "";
				}
				else
				{
					dtlsAttrsList.get(idx).DelTag = "<a href=\"javascript:DeleteDtl('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNo() + "')\">删除</a>";
				}

				idx++;
			}

			return Json.ToJson(dtlsAttrsList);
	}

	public final String Dtls_Save() {
		return "";
	}
	// /#endregion 从表权限.

}