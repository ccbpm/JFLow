package BP.WF.HttpHandler;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HttpContext;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.EntityTree;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Sys.MapData;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.SystemConfig;
import BP.WF.BatchRole;
import BP.WF.CCRole;
import BP.WF.Dev2Interface;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.GenerWorkFlow;
import BP.WF.Glo;
import BP.WF.HuiQianRole;
import BP.WF.HuiQianTaskSta;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.PrintDocEnable;
import BP.WF.SMSMsgType;
import BP.WF.SendReturnObjs;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CondModel;
import BP.WF.Template.FlowFormTrees;
import BP.WF.Template.FrmEnableRole;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.ShowWhere;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTreeAttr;
import BP.WF.Template.SysFormTrees;
import BP.WF.Template.TurnTo;
import BP.WF.Template.TurnTos;
import BP.Web.WebUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WF_MyFlow extends WebContralBase {

	/**
	 * 构造函数
	 */
	public WF_MyFlow() {

	}

	public long _WorkID = 0;

	@SuppressWarnings("unchecked")
	public static ArrayList<EntityTree> convertEntityMultiTree(Object obj) {
		return (ArrayList<EntityTree>) obj;
	}

	/**
	 * 初始化函数
	 * 
	 * @param mycontext
	 */
	public WF_MyFlow(HttpContext mycontext) {
		this.context = mycontext;
	}

	public String MyFlowSelfForm_Init() throws Exception {
		return this.GenerWorkNode();
	}

	// #region 表单树操作
	/**
	 * 获取表单树数据
	 * 
	 * @return
	 */
	private BP.WF.Template.FlowFormTrees appFlowFormTree = new FlowFormTrees();

	public final String FlowFormTree_Init() throws Exception {
		// add root
		BP.WF.Template.FlowFormTree root = new BP.WF.Template.FlowFormTree();
		root.setNo("00");
		root.setParentNo("0");
		root.setName("目录");
		root.setNodeType("root");
		appFlowFormTree.clear();
		appFlowFormTree.AddEntity(root);

		/// #region 添加表单及文件夹

		// 节点表单
		FrmNodes frmNodes = new FrmNodes();
		QueryObject qo = new QueryObject(frmNodes);
		qo.AddWhere(FrmNodeAttr.FK_Node, this.getFK_Node());
		qo.addAnd();
		qo.AddWhere(FrmNodeAttr.FK_Flow, this.getFK_Flow());

		qo.addOrderBy(FrmNodeAttr.Idx);
		qo.DoQuery();

		// 文件夹
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Name);

		// 所有表单集合.
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());

		for (FrmNode frmNode : frmNodes.ToJavaList()) {

			/// #region 增加判断是否启用规则.
			switch (frmNode.getFrmEnableRole()) {
			case Allways:
				break;
			case WhenHaveData: // 判断是否有数据.
				MapData md = new MapData(frmNode.getFK_Frm());
				long pk = this.getWorkID();
				switch (frmNode.getWhoIsPK()) {
				case FID:
					pk = this.getFID();
					break;
				case PWorkID:
					pk = this.getPWorkID();
					break;
				case CWorkID:
					pk = this.getCWorkID();
					break;
				case OID:
				default:
					pk = this.getWorkID();
					break;
				}
				if (DBAccess.RunSQLReturnValInt(
						"SELECT COUNT(*) as Num FROM " + md.getPTable() + " WHERE OID=" + pk) == 0) {
					continue;
				}
				break;
			case WhenHaveFrmPara: // 判断是否有参数.
				String frms = getRequest().getParameter("Frms"); // this.context.Request["Frms"];

				// 修改算法：解决 frmID =ABC frmID=AB 的问题.
				if (DotNetToJavaStringHelper.isNullOrEmpty(frms) == true) {
					continue;
				}

				frms = frms.trim();

				frms = frms.replaceAll(" ", "");
				frms = frms.replaceAll(" ", "");

				if (frms.contains(",") == false) {
					if (!frms.equals(frmNode.getFK_Frm())) {
						continue;
					}
				}

				if (frms.contains(",") == true) {
					if (frms.contains(frmNode.getFK_Frm() + ",") == false) {
						continue;
					}
				}

				break;
			case ByFrmFields:
				throw new RuntimeException("@这种类型的判断，ByFrmFields 还没有完成。");

			case BySQL: // 按照SQL的方式.
				Object tempVar = frmNode;
				String mysql = (String) ((tempVar instanceof String) ? tempVar : null);
				mysql = mysql.replace("@OID", String.valueOf(this.getWorkID()));
				mysql = mysql.replace("@WorkID", String.valueOf(this.getWorkID()));

				mysql = mysql.replace("@NodeID", String.valueOf(this.getFK_Node()));
				mysql = mysql.replace("@FK_Node", String.valueOf(this.getFK_Node()));

				mysql = mysql.replace("@FK_Flow", this.getFK_Flow());

				mysql = mysql.replace("@WebUser.No", WebUser.getNo());
				mysql = mysql.replace("@WebUser.Name", WebUser.getName());
				mysql = mysql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				// 替换特殊字符.
				mysql = mysql.replace("~", "'");

				try {
					if (DBAccess.RunSQLReturnValFloat(mysql) <= 0) {
						continue;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Disable: // 如果禁用了，就continue出去..
				continue;
			default:
				throw new RuntimeException("@没有判断的规则." + frmNode.getFrmEnableRole());
			}

			/// #region 检查是否有没有目录的表单?
			boolean isHave = false;
			for (MapData md : mds.ToJavaList()) {
				if (md.getFK_FormTree().equals("")) {
					isHave = true;
					break;
				}
			}

			String treeNo = "0";
			if (isHave && mds.size() == 1) {
				treeNo = "00";
			} else if (isHave == true) {
				for (MapData md : mds.ToJavaList()) {
					if (!md.getFK_FormTree().equals("")) {
						treeNo = md.getFK_FormTree();
						break;
					}
				}
			}

			/// #endregion 检查是否有没有目录的表单?

			for (MapData md : mds.ToJavaList()) {
				if (!frmNode.getFK_Frm().equals(md.getNo())) {
					continue;
				}

				if (md.getFK_FormTree().equals("")) {
					md.setFK_FormTree(treeNo);
				}

				for (SysFormTree formTree : formTrees.ToJavaList()) {
					if (!md.getFK_FormTree().equals(formTree.getNo())) {
						continue;
					}
					if (appFlowFormTree.Contains("No", formTree.getNo()) == false) {
						BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
						nodeFolder.setNo(formTree.getNo());
						nodeFolder.setParentNo(formTree.getParentNo());
						nodeFolder.setName(formTree.getName());
						nodeFolder.setNodeType("folder");
						appFlowFormTree.AddEntity(nodeFolder);
					}
				}
				// 检查必填项
				boolean IsNotNull = false;
				FrmFields formFields = new FrmFields();
				QueryObject obj = new QueryObject(formFields);
				obj.AddWhere(FrmFieldAttr.FK_Node, this.getFK_Node());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.FK_MapData, md.getNo());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.IsNotNull, "1");
				obj.DoQuery();
				if (formFields != null && formFields.size() > 0) {
					IsNotNull = true;
				}

				BP.WF.Template.FlowFormTree nodeForm = new BP.WF.Template.FlowFormTree();
				nodeForm.setNo(md.getNo());
				nodeForm.setParentNo(md.getFK_FormTree());
				nodeForm.setName(md.getName());
				nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
				nodeForm.setIsEdit( frmNode.getIsEditInt() );
				appFlowFormTree.AddEntity(nodeForm);
			}
		}
		// 找上级表单文件夹
		AppendFolder(formTrees);

		/// #endregion

		// 扩展工具，显示位置为表单树类型
		NodeToolbars extToolBars = new NodeToolbars();
		QueryObject info = new QueryObject(extToolBars);
		info.AddWhere(NodeToolbarAttr.FK_Node, this.getFK_Node());
		info.addAnd();
		info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Tree.getValue());
		info.DoQuery();

		for (NodeToolbar item : extToolBars.ToJavaList()) {
			String url = "";
			System.out.println(item.getUrl());
			if (DotNetToJavaStringHelper.isNullOrEmpty(item.getUrl())) {
				continue;
			}

			url = item.getUrl();

			BP.WF.Template.FlowFormTree formTree = new BP.WF.Template.FlowFormTree();
			formTree.setNo(String.valueOf(item.getOID()));
			formTree.setParentNo("01");
			formTree.setName(item.getTitle());
			formTree.setNodeType("tools|0");
			if (!DotNetToJavaStringHelper.isNullOrEmpty(item.getTarget())
					&& item.getTarget().toUpperCase().equals("_BLANK")) {
				formTree.setNodeType("tools|1");
			}

			formTree.setUrl(url);
			appFlowFormTree.AddEntity(formTree);
		}
		TansEntitiesToGenerTree(appFlowFormTree, root.getNo(), "");
		return appendMenus.toString();
	}

	/**
	 * 拼接文件夹
	 * 
	 * @param formTrees
	 */
	private void AppendFolder(SysFormTrees formTrees) {
		BP.WF.Template.FlowFormTrees parentFolders = new BP.WF.Template.FlowFormTrees();
		// 二级目录
		for (BP.WF.Template.FlowFormTree folder : appFlowFormTree.ToJavaList()) {
			if (DotNetToJavaStringHelper.isNullOrEmpty(folder.getNodeType())
					|| !folder.getNodeType().equals("folder")) {
				continue;
			}

			for (SysFormTree item : formTrees.ToJavaList()) {
				// 排除根节点
				if (item.getParentNo().equals("0") || item.getNo().equals("0")) {
					continue;
				}
				if (parentFolders.Contains("No", item.getNo()) == true) {
					continue;
				}
				// 文件夹
				if (folder.getParentNo().equals(item.getNo())) {
					if (parentFolders.Contains("No", item.getNo()) == true) {
						continue;
					}
					if (item.getParentNo().equals("0") == true) {
						continue;
					}

					BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
					nodeFolder.setNo(item.getNo());
					nodeFolder.setParentNo(item.getParentNo());
					nodeFolder.setName(item.getName());
					nodeFolder.setNodeType("folder");
					parentFolders.AddEntity(nodeFolder);
				}
			}
		}
		// 找到父级目录添加到集合
		for (BP.WF.Template.FlowFormTree folderapp : parentFolders.ToJavaList()) {
			if(appFlowFormTree.contains(folderapp) == false)
			appFlowFormTree.AddEntity(folderapp);
		}
		// 求出没有父节点的文件夹
		parentFolders.clear();
		for (BP.WF.Template.FlowFormTree folder : appFlowFormTree.ToJavaList()) {
			if (DotNetToJavaStringHelper.isNullOrEmpty(folder.getNodeType())
					|| folder.getNodeType().equals("folder") == false) {
				continue;
			}

			boolean bHave = false;
			for (BP.WF.Template.FlowFormTree child : appFlowFormTree.ToJavaList()) {
				if (folder.getParentNo().equals(child.getNo()) == true) {
					bHave = true;
					break;
				}
			}
			// 没有父节点的文件夹
			if (bHave == false && parentFolders.Contains("No", folder.getNo()) == false) {
				parentFolders.AddEntity(folder);
			}
		}
		// 修改根节点编号
		for (BP.WF.Template.FlowFormTree folder : parentFolders.ToJavaList()) {
			for (BP.WF.Template.FlowFormTree folderApp : appFlowFormTree.ToJavaList()) {
				if (folderApp.getNo().equals(folder.getNo()) == false) {
					continue;
				}
				folderApp.setParentNo("00");
			}
		}

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

	public final void TansEntitiesToGenerTree(Entities ens, String rootNo, String checkIds) {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityTree root = (EntityTree) ((tempVar instanceof EntityTree) ? tempVar : null);
		if (root == null) {
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");

		// attributes
		BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((root instanceof BP.WF.Template.FlowFormTree)
				? root : null);
		if (formTree != null) {
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\""
					+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
		}
		appendMenus.append(",iconCls:\"icon-Wave\"");
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityTree parentEn, Entities ens, String checkIds) {
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0); // clear();

		appendMenuSb.append("[");
		for (EntityTree item : convertEntityMultiTree(ens)) {
			if (!item.getParentNo().equals(parentEn.getNo()))
				continue;

			if (checkIds.contains("," + item.getNo() + ",")) {
				appendMenuSb.append(
						"{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":true");
			} else {
				appendMenuSb.append(
						"{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":false");
			}

			// attributes
			BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((item instanceof BP.WF.Template.FlowFormTree)
					? item : null);
			if (formTree != null) {
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				if (SystemConfig.getSysNo().equals("YYT")) {
					ico = "icon-boat_16";
				}
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\""
						+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
				// 图标
				if (formTree.getNodeType().equals("form|0")) {
					ico = "form0";
					if (SystemConfig.getSysNo().equals("YYT")) {
						ico = "icon-Wave";
					}
				}
				if (formTree.getNodeType().equals("form|1")) {
					ico = "form1";
					if (SystemConfig.getSysNo().equals("YYT")) {
						ico = "icon-Shark_20";
					}
				}
				if (formTree.getNodeType().contains("tools")) {
					ico = "icon-4";
					if (SystemConfig.getSysNo().equals("YYT")) {
						ico = "icon-Wave";
					}
				}
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
		appendMenuSb.setLength(0);
		;
	}

	public String MyFlow_Init() throws Exception {

		
		
		String userNo = BP.Web.WebUser.getNo();
		if (DataType.IsNullOrEmpty(userNo) == true)
			return "err@当前登录信息丢失,请重新登录.";
		
		  String isCC = this.GetRequestVal("IsCC");
          if (isCC!=null && isCC == "1")
              return "url@WFRpt.htm?1=2" + this.getRequestParasOfAll();

		if (this.getWorkID() != 0) {
			// 判断是否有执行该工作的权限.
			boolean isCanDo = Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getFK_Node(),
					this.getWorkID(), userNo);
			if (isCanDo == false) {
				return "err@您不能执行当前工作.";
			}
		}

		// 当前工作.
		Work currWK = this.getcurrND().getHisWork();
		GenerWorkFlow gwf = new GenerWorkFlow();
		if (this.getWorkID() != 0) {
			gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			if (gwf.RetrieveFromDBSources() == 0) {
				return ("err@该流程ID{" + this.getWorkID() + "}不存在，或者已经被删除.");
			}
		}

		// /#region 判断前置导航.
		if (this.getcurrND().getIsStartNode() && this.getIsCC() == false && this.getWorkID() == 0) {
			if (BP.WF.Dev2Interface.Flow_IsCanStartThisFlow(this.getFK_Flow(), WebUser.getNo()) == false) {
				// 是否可以发起流程？
				return "err@您(" + BP.Web.WebUser.getNo() + ")没有发起或者处理该流程的权限.";
			}
		}

		if (this.getWorkID() == 0 && this.getcurrND().getIsStartNode() && this.GetRequestVal("IsCheckGuide") == null) {
			long workid = BP.WF.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow());
			
			switch (this.getcurrFlow().getStartGuideWay()) {
			case None:
				break;
			case SubFlowGuide:
			case SubFlowGuideEntity:
				return "url@StartGuide.htm?FK_Flow=" + this.getcurrFlow().getNo() + "&WorkID=" + workid+this.getRequestParasOfAll();
			case ByHistoryUrl: // 历史数据.
				if (this.getcurrFlow().getIsLoadPriData() == true) {
					return "err@流程配置错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。";
				}
			case BySystemUrlOneEntity:
				return "url@StartGuideEntities.htm?StartGuideWay=BySystemUrlOneEntity&FK_Flow="
						+ this.getcurrFlow().getNo() + "&WorkID=" + workid+this.getRequestParasOfAll();
			case BySQLMulti:
				return "url@StartGuideEntities.htm?StartGuideWay=BySQLMulti&FK_Flow=" + this.getcurrFlow().getNo()
						+ "&WorkID=" + workid+this.getRequestParasOfAll();
			case BySQLOne:
				return "url@StartGuideEntities.htm?FK_Flow=" + this.getcurrFlow().getNo() + "&WorkID=" + workid+this.getRequestParasOfAll();
			case BySelfUrl: // 按照定义的url.
				return "url@" + this.getcurrFlow().getStartGuidePara1() + this.getRequestParasOfAll() + "&WorkID=" + workid+this.getRequestParasOfAll();
			case ByFrms: // 选择表单.
				return "url@./WorkOpt/StartGuideFrms.htm?FK_Flow=" + this.getcurrFlow().getNo() + "&WorkID=" + workid+this.getRequestParasOfAll();
			default:
				break;
			}
		}

		//判断前置导航
		if (this.getWorkID() != 0 && !StringUtils.isEmpty(this.GetRequestVal("IsCheckGuide"))) {
			String key = this.GetRequestVal("KeyNo");
			
			 DataTable dt = BP.WF.Glo.StartGuidEnties(this.getWorkID(), this.getFK_Flow(), this.getFK_Node(), key);

             /*如果父流程编号，就要设置父子关系。*/
             if (dt != null && dt.Rows.size() > 0 && dt.Columns.contains("PFlowNo") == true)
             {
                 String pFlowNo = dt.Rows.get(0).getValue("PFlowNo").toString();
                 if(dt.Rows.get(0).getValue("PNodeID") == null)
                	 throw new Exception("@没有设置的父流程的节点编号 ，这是非法的。");
                 
                 int pNodeID = Integer.parseInt(dt.Rows.get(0).getValue("PNodeID").toString());
                 long pWorkID = Long.parseLong(dt.Rows.get(0).getValue("PWorkID").toString());
                 String pEmp = dt.Rows.get(0).getValue("PEmp").toString();
                 if (DataType.IsNullOrEmpty(pEmp))
                     pEmp = WebUser.getNo();

                 //设置父子关系.
                 BP.WF.Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), pFlowNo,
                     pWorkID, pNodeID, pEmp);
             }
		}

		// /#region 处理表单类型.
		if (this.getcurrND().getHisFormType() == NodeFormType.SheetTree
				|| this.getcurrND().getHisFormType() == NodeFormType.SheetAutoTree) {
			

			if (this.getWorkID() == 0) {
				
					this.setWorkID(BP.WF.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), null, null,WebUser.getNo(), null));
				

				currWK = getcurrND().getHisWork();
				currWK.setOID(this.getWorkID());
				currWK.Retrieve();
				this.setWorkID(currWK.getOID());
			} else {
				gwf.setWorkID(this.getWorkID());
				gwf.RetrieveFromDBSources();
			}

			if (this.getcurrND().getIsStartNode()) {
				// 如果是开始节点, 先检查是否启用了流程限制。
				if (BP.WF.Glo.CheckIsCanStartFlow_InitStartFlow(this.getcurrFlow()) == false) {
					// 如果启用了限制就把信息提示出来.
					String msg1 = BP.WF.Glo.DealExp(this.getcurrFlow().getStartLimitAlert(), currWK, null);
					return "err@" + msg1;
				}
			}

			 String toUrl = "";

             //toUrl = "./FlowFormTree/Default.htm?WorkID=" + this.WorkID + "&FK_Flow=" + this.FK_Flow + "&UserNo=" + WebUser.No + "&FID=" + this.FID + "&SID=" + WebUser.SID + "&PFlowNo=" + pFlowNo + "&PWorkID=" + pWorkID;
             if (this.getIsMobile() == true)
             {
                 if (gwf.getParas_Frms().equals("") == false)
                     toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo=" + gwf.getPFlowNo() + "&PWorkID=" + gwf.getPWorkID() + "&Frms=" + gwf.getParas_Frms();
                 else
                     toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo=" + gwf.getPFlowNo() + "&PWorkID=" + gwf.getPWorkID();
             }
             else
             {
                 if (gwf.getParas_Frms().equals("")==false)
                     toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo=" + gwf.getPFlowNo() + "&PWorkID=" + gwf.getPWorkID() + "&Frms=" + gwf.getParas_Frms();
                 else
                     toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.getSID() + "&PFlowNo=" + gwf.getPFlowNo() + "&PWorkID=" + gwf.getPWorkID();
             }

            String[] strs = this.getRequestParas().split("&");
             for (String str : strs)
             {
                 if (toUrl.contains(str) == true)
                     continue;
                 if (str.contains("DoType=") == true)
                     continue;
                 if (str.contains("DoMethod=") == true)
                     continue;
                 if (str.contains("HttpHandlerName=") == true)
                     continue;
                 if (str.contains("IsLoadData=") == true)
                     continue;
                 if (str.contains("IsCheckGuide=") == true)
                     continue;                     

                 toUrl += "&" + str;
                 
             }
               
             Enumeration enu = getRequest().getParameterNames();
 			while (enu.hasMoreElements())
 			{
 				
 				String key = (String) enu.nextElement();
 				 if (toUrl.contains(key+"=") == true)
                     continue;
 				
 				 toUrl += "&" + key + "=" + getRequest().getParameter(key);
 			}

            
		
			gwf.setWorkID(this.getWorkID());
			gwf.RetrieveFromDBSources();
			
			// 设置url.
			if (gwf.getWFState() == WFState.Runing || gwf.getWFState() == WFState.Blank
					|| gwf.getWFState() == WFState.Draft) {
				if (toUrl.contains("IsLoadData") == false) {
					toUrl += "&IsLoadData=1";
				} else {
					toUrl = toUrl.replace("&IsLoadData=0", "&IsLoadData=1");
				}
			}
			// SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出.
			toUrl = toUrl.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

			if (toUrl.indexOf("FK_Node=") == -1)
				toUrl = toUrl + "&FK_Node=" + this.getcurrND().getNodeID();
			
			 //如果是开始节点.
            if (this.getcurrND().getIsStartNode() == true)
            {
                if (toUrl.contains("PrjNo") == true && toUrl.contains("PrjName") == true)
                {
                    String sql = "UPDATE " + currWK.getEnMap().getPhysicsTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
                    BP.DA.DBAccess.RunSQL(sql);
                }
            }

			return "url@" + toUrl;
		}

		if (this.getcurrND().getHisFormType() == NodeFormType.SDKForm) {
			if (this.getWorkID() == 0) {
				currWK = this.getcurrFlow().NewWork();
				this.setWorkID(currWK.getOID());
			}

			String url = getcurrND().getFormUrl();
			if (DotNetToJavaStringHelper.isNullOrEmpty(url)) {
				return "err@设置读取状流程设计错误态错误,没有设置表单url.";
			}

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(getcurrND(), currWK, null);
			
			 //如果是开始节点.
            if (this.getcurrND().getIsStartNode() == true)
            {
                if (url.contains("PrjNo") == true && url.contains("PrjName") == true)
                {
                    String sql = "UPDATE " + currWK.getEnMap().getPhysicsTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
                    BP.DA.DBAccess.RunSQL(sql);
                }
            }

			// sdk表单就让其跳转.
			return "url@" + url;
		}

		// 求出当前节点frm的类型.
		NodeFormType frmtype = this.getcurrND().getHisFormType();
		if (frmtype != NodeFormType.RefOneFrmTree) {
			String nodeID = this.getcurrND().GetValStrByKey("NodeID");

			this.getcurrND().WorkID = this.getWorkID(); // 为求当前表单ID获得参数，而赋值.

			if (this.getcurrND().getNodeFrmID().contains(nodeID) == false) {
				/* 如果当前节点引用的其他节点的表单. */
				String nodeFrmID = this.getcurrND().getNodeFrmID();
				String refNodeID = nodeFrmID.replace("ND", "");
				BP.WF.Node nd = new Node(Integer.parseInt(refNodeID));

				// 表单类型.
				frmtype = nd.getHisFormType();
			}
		}
		// /#endregion 处理表单类型.

		
		// #region 内置表单类型的判断.
		/* 如果是傻瓜表单，就转到傻瓜表单的解析执行器上，为软通动力改造。 */
		if (this.getWorkID() == 0) {
			currWK = this.getcurrFlow().NewWork();
			this.setWorkID(currWK.getOID());
		}

		if (frmtype == NodeFormType.FoolTruck) {
			/* 如果是傻瓜表单，就转到傻瓜表单的解析执行器上，为软通动力改造。 */
			if (this.getWorkID() == 0) {
				currWK = this.getcurrFlow().NewWork();
				this._WorkID = currWK.getOID();
			}

			String url = "MyFlowGener.htm";

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(this.getcurrND(), currWK, url);
			
			 //如果是开始节点.
            if (this.getcurrND().getIsStartNode() == true)
            {
                if (url.contains("PrjNo") == true && url.contains("PrjName") == true)
                {
                    String sql = "UPDATE " + currWK.getEnMap().getPhysicsTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
                    BP.DA.DBAccess.RunSQL(sql);
                }
            }
			
			return "url@" + url;
		}

		if (frmtype == NodeFormType.FoolForm && this.getIsMobile() == false) {
			/* 如果是傻瓜表单，就转到傻瓜表单的解析执行器上。 */
			if (this.getWorkID() == 0) {
				currWK = this.getcurrFlow().NewWork();
				this._WorkID = currWK.getOID();
			}

			String url = "MyFlowGener.htm";
			if (this.getIsMobile())
				url = "MyFlowGener.htm";

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(this.getcurrND(), currWK, url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			
			 //如果是开始节点.
            if (this.getcurrND().getIsStartNode() == true)
            {
                if (url.contains("PrjNo") == true && url.contains("PrjName") == true)
                {
                    String sql = "UPDATE " + currWK.getEnMap().getPhysicsTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
                    BP.DA.DBAccess.RunSQL(sql);
                }
            }
            
			return "url@" + url;
		}

		// 自定义表单
		if (frmtype == NodeFormType.SelfForm && this.getIsMobile() == false) {
			if (this.getWorkID() == 0) {
				currWK = this.getcurrFlow().NewWork();
				this._WorkID = currWK.getOID();
			}

			String url = "MyFlowSelfForm.htm";

			// 处理连接.
			url = this.MyFlow_Init_DealUrl(this.getcurrND(), currWK, url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			
			 //如果是开始节点.
            if (this.getcurrND().getIsStartNode() == true)
            {
                if (url.contains("PrjNo") == true && url.contains("PrjName") == true)
                {
                    String sql = "UPDATE " + currWK.getEnMap().getPhysicsTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
                    BP.DA.DBAccess.RunSQL(sql);
                }
            }
            
			return "url@" + url;
		}
		// #endregion 内置表单类型的判断.

		String myurl = "MyFlowGener.htm";

		// 处理连接.
		myurl = this.MyFlow_Init_DealUrl(this.getcurrND(), currWK, myurl);
		myurl = myurl.replace("DoType=MyFlow_Init&", "");
		myurl = myurl.replace("&DoWhat=StartClassic", "");
		//如果是开始节点.
        if (this.getcurrND().getIsStartNode() == true)
        {
            if (myurl.contains("PrjNo") == true && myurl.contains("PrjName") == true)
            {
                String sql = "UPDATE " + currWK.getEnMap().getPhysicsTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
                BP.DA.DBAccess.RunSQL(sql);
            }
        }
		return "url@" + myurl;
	}

	public String InitToolBar() throws Exception {

		// #region 处理是否是加签，或者是否是会签模式，.
		Boolean isAskForOrHuiQian = false;
		String nodeIDStr = this.GetRequestVal("FK_Node");

		if (nodeIDStr != null && nodeIDStr.endsWith("01") == false) {
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
			if (gwf.getWFState() == WFState.Askfor) {
				isAskForOrHuiQian = true;
			}

			/* 判断是否是加签状态，如果是，就判断是否是主持人，如果不是主持人，就让其 isAskFor=true ,屏蔽退回等按钮. */
			if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing) {
				if (gwf.getHuiQianZhuChiRen().equals(WebUser.getNo()) == false)
					isAskForOrHuiQian = true;
			}
		}
		// #endregion 处理是否是加签，或者是否是会签模式，.

		String tKey = ""; // DateTime.Now.ToString("yyyy-MM-dd - hh:mm:ss");
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		String toolbar = "";
		try {
			// #region 是否是会签？.
			if (isAskForOrHuiQian == true && SystemConfig.getCustomerNo() == "LIMS")
				return "";

			if (isAskForOrHuiQian == true) {
				toolbar += "<input name='Send' type=button value='执行会签' enable=true onclick=\" " + btnLab.getSendJS()
						+ " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(); \" />";
				// toolbar += "<input name='Send' type=button value='" +
				// btnLab.getSendLab() + "' enable=true onclick=\"" +
				// btnLab.getSendJS() + " if ( SendSelfFrom()==false) return
				// false; Send(); this.disabled=true;\" />";
				if (btnLab.getPrintZipEnable() == true) {
					String packUrl = "./WorkOpt/Packup.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID()
							+ "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='PackUp'  value='" + btnLab.getPrintZipLab()
							+ "' enable=true/>";
				}

				if (btnLab.getTrackEnable())
					toolbar += "<input type=button name='Track'  value='" + btnLab.getTrackLab()
							+ "' enable=true onclick=\"WinOpen('./WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID="
							+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node="
							+ this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";

				return toolbar;
			}
			// #endregion 是否是会签.

			// #region 是否是抄送.
			if (this.getIsCC()) {
				toolbar += "<input type=button  value='流程运行轨迹' enable=true onclick=\"WinOpen('./WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node="
						+ this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";
				// 判断审核组件在当前的表单中是否启用，如果启用了.
				FrmWorkCheck fwc = new FrmWorkCheck(this.getFK_Node());
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable) {
					/* 如果不等于启用, */
					toolbar += "<input type=button  value='填写审核意见' enable=true onclick=\"WinOpen('./WorkOpt/CCCheckNote.htm?WorkID="
							+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node="
							+ this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";
				}
				return toolbar;
			}
			// #endregion 是否是抄送.

			// #region 如果当前节点启用了协作会签.
			if (btnLab.getHuiQianRole() == HuiQianRole.Teamup) {
				if (this.getIsMobile() == true)
					toolbar += "<input name='SendHuiQian' type=button value='会签发送' enable=true onclick=\" "
							+ btnLab.getSendJS()
							+ " if(SysCheckFrm()==false) return false;SaveDtlAll();SendIt(true); \" />";
				else
					toolbar += "<input name='SendHuiQian' type=button value='会签发送' enable=true onclick=\" "
							+ btnLab.getSendJS()
							+ " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(true); \" />";
			}
			// #endregion 如果当前节点启用了协作会签

			// #region 加载流程控制器 - 按钮
			if (this.getcurrND().getHisFormType() == NodeFormType.SelfForm) {
				/* 如果是嵌入式表单. */
				if (this.getcurrND().getIsEndNode()) {
					/* 如果当前节点是结束节点. */
					if (btnLab.getSendEnable() && this.getcurrND().getHisBatchRole() != BatchRole.Group) {
						/* 如果启用了发送按钮. */
						toolbar += "<input name='Send' type=button value='" + btnLab.getSendLab()
								+ "' enable=true onclick=\"" + btnLab.getSendJS()
								+ " if (SendSelfFrom()==false) return false; Send(); this.disabled=true;\" />";
					}
				} else {
					if (btnLab.getSendEnable() && this.getcurrND().getHisBatchRole() != BatchRole.Group) {
						toolbar += "<input name='Send' type=button  value='" + btnLab.getSendLab()
								+ "' enable=true onclick=\"" + btnLab.getSendJS()
								+ " if ( SendSelfFrom()==false) return false; Send(); this.disabled=true;\" />";
					}
				}

				/* 处理保存按钮. */
				if (btnLab.getSaveEnable()) {
					toolbar += "<input name='Save' type=button value='" + btnLab.getSaveLab()
							+ "' enable=true onclick=\"SaveSelfFrom();\" />";
				}
			}

			if (this.getcurrND().getHisFormType() != NodeFormType.SelfForm) {
				/* 启用了其他的表单. */
				if (this.getcurrND().getIsEndNode()) {
					/* 如果当前节点是结束节点. */
					if (btnLab.getSendEnable() && this.getcurrND().getHisBatchRole() != BatchRole.Group) {
						/* 如果启用了选择人窗口的模式是【选择既发送】. */
						if (this.getIsMobile())
							toolbar += "<input name='Send' type=button value='" + btnLab.getSendLab()
									+ "' enable=true onclick=\" " + btnLab.getSendJS()
									+ " if(SysCheckFrm()==false) return false;SaveDtlAll();SendIt(); \" />";
						else
							toolbar += "<input name='Send' type=button value='" + btnLab.getSendLab()
									+ "' enable=true onclick=\" " + btnLab.getSendJS()
									+ " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(); \" />";

					}
				} else {
					if (btnLab.getSendEnable() && this.getcurrND().getHisBatchRole() != BatchRole.Group) {
						/*
						 * 如果启用了发送按钮. 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
						 */
						if (this.getIsMobile())
							toolbar += "<input name='Send' type=button  value='" + btnLab.getSendLab()
									+ "' enable=true onclick=\" " + btnLab.getSendJS()
									+ " if(SysCheckFrm()==false) return false;SendIt();\" />";
						else
							toolbar += "<input name='Send' type=button  value='" + btnLab.getSendLab()
									+ "' enable=true onclick=\" " + btnLab.getSendJS()
									+ " if(SysCheckFrm()==false) return false;Send();\" />";
					}
				}

				/* 处理保存按钮. */
				if (btnLab.getSaveEnable()) {
					if (this.getIsMobile())
						toolbar += "<input name='Save' type=button  value='" + btnLab.getSaveLab()
								+ "' enable=true onclick=\"   if(SysCheckFrm()==false) return false; SaveIt();\" />";
					else
						toolbar += "<input name='Save' type=button  value='" + btnLab.getSaveLab()
								+ "' enable=true onclick=\"   if(SysCheckFrm()==false) return false;Save();\" />";
				}
			}

			if (btnLab.getWorkCheckEnable()) {
				/* 审核 */
				// String urlr1 = "./WorkOpt/WorkCheck.htm?FK_Node=" +
				// this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" +
				// this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" +
				// tKey;
				// toolbar += "<input name='Btn_WorkCheck' type=button value='"
				// + btnLab.getWorkCheckLab() + "' enable=true
				// onclick=\"WinOpen('" + urlr1 + "','dsdd'); \" />";
				toolbar += "<input  name='workcheckBtn' type=button  value='" + btnLab.getWorkCheckLab()
						+ "' enable=true />";
			}

			if (btnLab.getThreadEnable()) {
				/* 如果要查看子线程. */
				String ur2 = "./WorkOpt/ThreadDtl.aspx?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getThreadLab() + "' enable=true onclick=\"WinOpen('"
						+ ur2 + "'); \" />";
			}

			if (btnLab.getTCEnable() == true) {
				/* 流转自定义.. */
				String ur3 = "./WorkOpt/TransferCustom.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getTCLab() + "' enable=true onclick=\"To('" + ur3
						+ "'); \" />";
			}

			if (btnLab.getJumpWayEnable() && 1 == 2) {
				/* 跳转 */
				String urlr = "./WorkOpt/JumpWay.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getJumpWayLab() + "' enable=true onclick=\"To('"
						+ urlr + "'); \" />";
			}

			if (btnLab.getReturnEnable()) {
				/* 退回 */
				String urlr = "./WorkOpt/ReturnWork.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input name='Return' type=button  value='" + btnLab.getReturnLab()
						+ "' enable=true onclick=\"ReturnWork('" + urlr + "','" + btnLab.getReturnField() + "'); \" />";
			}

			// if (btnLab.getHungEnable() && this.currND.IsStartNode == false)
			if (btnLab.getHungEnable()) {
				/* 挂起 */
				String urlr = "./WorkOpt/HungUp.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getHungLab() + "' enable=true onclick=\"WinOpen('"
						+ urlr + "'); \" />";
			}

			if (btnLab.getShiftEnable()) {
				/* 移交 */
				String url12 = "./WorkOpt/Forward.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID()
						+ "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + "移交原因.";
				toolbar += "<input name='Shift' type=button  value='" + btnLab.getShiftLab()
						+ "' enable=true onclick=\"To('" + url12 + "'); \" />";
			}

			if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto)) {
				if (this.getIsMobile()) {
					String urlrDel = "./WorkOpt/CC.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node()
							+ "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&s=" + tKey;
					toolbar += "<input name='CC' type=button  value='" + btnLab.getCCLab()
							+ "' enable=true onclick=\"To('" + urlrDel + "'); \" />";
				} else
					// 抄送
					toolbar += "<input name='CC' type=button  value='" + btnLab.getCCLab()
							+ "' enable=true onclick=\"WinOpen('" + "./WorkOpt/CC.htm?WorkID=" + this.getWorkID()
							+ "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&FID="
							+ this.getFID() + "&s=" + tKey + "','ds'); \" />";
			}

			if (btnLab.getDeleteEnable() != 0) {
				String urlrDel = "MyFlowInfo.htm?DoType=DeleteFlow&FK_Node=" + this.getFK_Node() + "&FID="
						+ this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s="
						+ tKey;
				toolbar += "<input name='Delete' type=button  value='" + btnLab.getDeleteLab()
						+ "' enable=true onclick=\"To('" + urlrDel + "'); \" />";
			}

			if (btnLab.getEndFlowEnable() && this.getcurrND().getIsStartNode() == false) {
				toolbar += "<input type=button name='EndFlow'  value='" + btnLab.getEndFlowLab()
						+ "' enable=true onclick=\"DoStop('" + btnLab.getEndFlowLab() + "','" + this.getFK_Flow()
						+ "','" + this.getWorkID() + "');\" />";
			}

			if (btnLab.getPrintDocEnable()) {
				/* 如果不是加签 */
				if (this.getcurrND().getHisPrintDocEnable() == PrintDocEnable.PrintRTF) {
					String urlr = "./WorkOpt/PrintDoc.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
							+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					toolbar += "<input type=button name='PrintDoc' value='" + btnLab.getPrintDocLab()
							+ "' enable=true onclick=\"WinOpen('" + urlr + "','dsdd'); \" />";
				}

				if (this.getcurrND().getHisPrintDocEnable() == PrintDocEnable.PrintWord) {
					String urlr = "./Rpt/RptDoc.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
							+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&IsPrint=1&s=" + tKey;
					toolbar += "<input type=button name='PrintDoc'  value='" + btnLab.getPrintDocLab()
							+ "' enable=true onclick=\"WinOpen('" + urlr + "','dsdd'); \" />";
				}

				if (this.getcurrND().getHisPrintDocEnable() == PrintDocEnable.PrintHtml) {
					String urlr = "PrintSample.jsp?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
							+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + BP.Web.WebUser.getNo()
							+ "&IsPrint=1";
					toolbar += "<input type=button  name='PrintDoc' value='" + btnLab.getPrintDocLab()
							+ "' enable=true onclick=\"printFrom('" + urlr + "'); \" />";
				}
			}

			if (btnLab.getTrackEnable())
				toolbar += "<input type=button name='Track'  value='" + btnLab.getTrackLab()
						+ "' enable=true onclick=\"WinOpen('./WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node="
						+ this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";

			if (btnLab.getSearchEnable())
				toolbar += "<input type=button name='Search'  value='" + btnLab.getSearchLab()
						+ "' enable=true onclick=\"WinOpen('./RptDfine/Default.htm?RptNo=ND"
						+ Integer.parseInt(this.getFK_Flow()) + "MyRpt&FK_Flow=" + this.getFK_Flow()
						+ "&SearchType=My&s=" + tKey + "','dsd0'); \" />";

			if (btnLab.getBatchEnable()) {
				/* 批量处理 */
				String urlr = "Batch.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='Batch' value='" + btnLab.getBatchLab()
						+ "' enable=true onclick=\"To('" + urlr + "'); \" />";
			}

			if (btnLab.getAskforEnable()) {
				/* 加签 */
				String urlr3 = "./WorkOpt/Askfor.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='Askfor'  value='" + btnLab.getAskforLab()
						+ "' enable=true onclick=\"To('" + urlr3 + "'); \" />";
			}

			if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader) {
				/* 会签 */
				String urlr3 = "./WorkOpt/HuiQian.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
						+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='HuiQian'  value='" + btnLab.getHuiQianLab()
						+ "' enable=true onclick=\"To('" + urlr3 + "'); \" />";
			}

			// 需要翻译.
			if (this.getcurrFlow().getIsResetData() == true && this.getcurrND().getIsStartNode()) {
				/* 启用了数据重置功能 */
				String urlr3 = "MyFlow.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&IsDeleteDraft=1&s=" + tKey;
				toolbar += "<input type=button  value='数据重置' enable=true onclick=\"To('" + urlr3 + "','ds'); \" />";
			}

			// if (btnLab.SubFlowEnable == true )
			// {
			// /* 子流程 */
			// String urlr3 = appPath + "WF/WorkOpt/SubFlow.htm?FK_Node=" +
			// this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" +
			// this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" +
			// tKey;
			// toolbar += "<input type=button name='SubFlow' value='" +
			// btnLab.SubFlowLab + "' enable=true onclick=\"WinOpen('" + urlr3 +
			// "'); \" />";
			// }

			if (btnLab.getCHEnable() == true) {
				/* 节点时限设置 */
				String urlr3 = "./WorkOpt/CH.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='CH'  value='" + btnLab.getCHLab()
						+ "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />";
			}

			if (btnLab.getPRIEnable() == true) {
				/* 优先级设置 */
				String urlr3 = "./WorkOpt/PRI.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='PR'  value='" + btnLab.getPRILab()
						+ "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />";
			}

			/* 关注 */
			if (btnLab.getFocusEnable() == true) {
				if (this.getHisGenerWorkFlow().getParas_Focus() == true)
					toolbar += "<input type=button  value='取消关注' enable=true onclick=\"FocusBtn(this,'"
							+ this.getWorkID() + "'); \" />";
				else
					toolbar += "<input type=button name='Focus' value='" + btnLab.getFocusLab()
							+ "' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" />";
			}

			/* 分配工作 */
			if (btnLab.getAllotEnable() == true) {
				/* 分配工作 */
				String urlAllot = "./WorkOpt/AllotTask.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID()
						+ "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + "移交原因.";
				toolbar += "<input name='Allot' type=button  value='" + btnLab.getAllotLab()
						+ "' enable=true onclick=\"To('" + urlAllot + "'); \" />";
			}

			/* 确认 */
			if (btnLab.getConfirmEnable() == true) {
				if (this.getHisGenerWorkFlow().getParas_Confirm() == true)
					toolbar += "<input type=button  value='取消确认' enable=true onclick=\"ConfirmBtn(this,'"
							+ this.getWorkID() + "'); \" />";
				else
					toolbar += "<input type=button name='Confirm' value='" + btnLab.getConfirmLab()
							+ "' enable=true onclick=\"ConfirmBtn(this,'" + this.getWorkID() + "'); \" />";
			}

			// 需要翻译.

			/* 打包下载zip */
			if (btnLab.getPrintZipEnable() == true) {
				String packUrl = "./WorkOpt/Packup.htm?FileType=zip&FK_Node=" + this.getFK_Node() + "&WorkID="
						+ this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
				toolbar += "<input type=button name='PackUp_zip'  value='" + btnLab.getPrintZipLab()
						+ "' enable=true/>";
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlEnable() == true) {
				String packUrl = "./WorkOpt/Packup.htm?FileType=html&FK_Node=" + this.getFK_Node() + "&WorkID="
						+ this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
				toolbar += "<input type=button name='PackUp_html'  value='" + btnLab.getPrintHtmlLab()
						+ "' enable=true/>";
			}

			/* 打包下载pdf */
			if (btnLab.getPrintPDFEnable() == true) {
				String packUrl = "./WorkOpt/Packup.htm?FileType=pdf&FK_Node=" + this.getFK_Node() + "&WorkID="
						+ this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
				toolbar += "<input type=button name='PackUp_pdf'  value='" + btnLab.getPrintPDFLab()
						+ "' enable=true/>";
			}

			if (this.getcurrND().getIsStartNode() == true) {
				if (this.getcurrFlow().getIsDBTemplate() == true) {
					String packUrl = "./WorkOpt/DBTemplate.htm?FileType=pdf&FK_Node=" + this.getFK_Node() + "&WorkID="
							+ this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='DBTemplate'  value='模版' enable=true/>";

				}
			}

			// #endregion

			// #region //加载自定义的button.
			BP.WF.Template.NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node());
			for (NodeToolbar bar : bars.ToJavaList()) {
				if (bar.getShowWhere() != ShowWhere.Toolbar) {
					continue;				
				}
				
				if ( bar.GetValIntByKey("ExcType" )==1 || (!DataType.IsNullOrEmpty(bar.getTarget()) && bar.getTarget().toLowerCase() == "javascript") ) {
					toolbar += "<input type=button  value='" + bar.getTitle() + "' enable=true onclick=\""
							+ bar.getUrl() + "\" />";
				} else {
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID()
							+ "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					toolbar += "<input type=button  value='" + bar.getTitle() + "' enable=true onclick=\"WinOpen('"
							+ urlr3 + "'); \" />";
				} 
			}
			// #endregion //加载自定义的button.

		} catch (Exception ex) {
			// BP.DA.Log.DefaultLogWriteLineError(ex);
			toolbar = "err@" + ex.getMessage();
		}
		return toolbar;

	}

	/**
	 * 发送
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Send() throws Exception {
		try {
			java.util.Hashtable ht = this.GetMainTableHT();
			SendReturnObjs objs = null;
			String msg = "";

			objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), ht, null, this.getToNode(),
					null);
			msg = objs.ToMsgOfHtml();
			// BP.WF.Glo.SessionMsg = msg;

			// 当前节点.
			Node currNode = new Node(this.getFK_Node());

			/// #region 处理发送后转向.
			// 处理转向问题.
			switch (currNode.getHisTurnToDeal()) {
			case SpecUrl:
				String myurl = currNode.getTurnToDealDoc().toString();

				if (myurl.contains("?") == false) {
					myurl += "?1=1";
				}
				Attrs myattrs = currNode.getHisWork().getEnMap().getAttrs();
				Work hisWK = currNode.getHisWork();
				for (Attr attr : myattrs) {
					if (myurl.contains("@") == false) {
						break;
					}
					myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
				}
				myurl = myurl.replace("@WebUser.No", BP.Web.WebUser.getNo());
				myurl = myurl.replace("@WebUser.Name", BP.Web.WebUser.getName());
				myurl = myurl.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());

				if (myurl.contains("@")) {
					BP.WF.Dev2Interface.Port_SendMsg("admin",
							getcurrFlow().getName() + "在" + getcurrND().getName() + "节点处，出现错误",
							"流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl,
							"Err" + getcurrND().getNo() + "_" + this.getWorkID(), SMSMsgType.Err, this.getFK_Flow(),
							this.getFK_Node(), this.getWorkID(), this.getFID());
					throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
				}

				if (myurl.contains("PWorkID") == false) {
					myurl += "&PWorkID=" + this.getWorkID();
				}

				myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&UserNo="
						+ WebUser.getNo() + "&SID=" + WebUser.getSID();
				return "TurnUrl@" + myurl;
			case TurnToByCond:
				TurnTos tts = new TurnTos(this.getFK_Flow());
				if (tts.size() == 0) {
					BP.WF.Dev2Interface.Port_SendMsg("admin",
							_currFlow.getName() + "在" + getcurrND().getName() + "节点处，出现错误", "您没有设置节点完成后的转向条件。",
							"Err" + getcurrND().getNo() + "_" + this.getWorkID(), SMSMsgType.Err, this.getFK_Flow(),
							this.getFK_Node(), this.getWorkID(), this.getFID());
					throw new RuntimeException("@您没有设置节点完成后的转向条件。");
				}

				for (TurnTo tt : tts.ToJavaList()) {
					tt.HisWork = currNode.getHisWork();
					if (tt.getIsPassed() == true) {
						String url = tt.getTurnToURL().toString();
						if (url.contains("?") == false) {
							url += "?1=1";
						}
						Attrs attrs = currNode.getHisWork().getEnMap().getAttrs();
						Work hisWK1 = currNode.getHisWork();
						for (Attr attr : attrs) {
							if (url.contains("@") == false) {
								break;
							}
							url = url.replace("@" + attr.getKey(), hisWK1.GetValStrByKey(attr.getKey()));
						}
						if (url.contains("@")) {
							throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);
						}

						url += "&PFlowNo=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&PWorkID="
								+ this.getWorkID() + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
						return "url@" + url;
					}
				}
				return msg;
			default:
				msg = msg.replace("@WebUser.No", BP.Web.WebUser.getNo());
				msg = msg.replace("@WebUser.Name", BP.Web.WebUser.getName());
				msg = msg.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
				return msg;
			}
			/// #endregion

		} catch (RuntimeException ex) {
			if (ex.getMessage().contains("请选择下一步骤工作") == true || ex.getMessage().contains("用户没有选择发送到的节点") == true) {
				if (this.getcurrND().getCondModel() == CondModel.ByLineCond) {
					// 如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员.
					return "url@./WorkOpt/ToNodes.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node()
							+ "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();
				}

				if (this.getcurrND().getCondModel() != CondModel.SendButtonSileSelect) {
					getcurrND().setCondModel(CondModel.SendButtonSileSelect);
					getcurrND().Update();
				}

				return "err@下一个节点的接收人规则是，当前节点选择来选择，在当前节点属性里您没有启动接受人按钮，系统自动帮助您启动了，请关闭窗口重新打开。" + ex.getMessage();
			}

			// 绑定独立表单，表单自定义方案验证错误弹出窗口进行提示
			if (this.getcurrND().getHisFrms() != null && this.getcurrND().getHisFrms().size() > 0
					&& ex.getMessage().contains("在提交前检查到如下必输字段填写不完整") == true) {
				return "err@" + ex.getMessage().replaceAll("@@", "@").replaceAll("@", "<BR>@");
			}

			return ex.getMessage();
		}
	}

	// 执行保存.
	public String Save() throws Exception {
		try {

			return BP.WF.Dev2Interface.Node_SaveWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(),
					this.GetMainTableHT(), null);

		} catch (RuntimeException ex) {
			return "err@保存失败:" + ex.getMessage();
		}
	}

	public String DelSubFlow() throws Exception {
		// 删除子流程
		BP.WF.Dev2Interface.Flow_DeleteSubThread(this.getFK_Flow(), this.getWorkID(), "手工删除");
		return "删除成功.";
	}

	public String Focus() throws Exception {
		BP.WF.Dev2Interface.Flow_Focus(this.getWorkID());
		return "设置成功.";
	}

	/// <summary>
	/// 删除流程
	/// </summary>
	/// <returns></returns>
	public String DeleteFlow() {
		try {
			return BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	public String GenerWorkNode() throws Exception {
		try {
			DataSet ds = new DataSet();

			if (this.getDoType1()!=null && this.getDoType1().toUpperCase().equals("VIEW")) {
				DataTable trackDt = BP.WF.Dev2Interface.DB_GenerTrack(this.getFK_Flow(), this.getWorkID(),
						this.getFID()).Tables.get(0);
				ds.Tables.add(trackDt);
				return BP.Tools.Json.ToJson(ds);
			}

			ds = BP.WF.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
					BP.Web.WebUser.getNo(), "0");

			/// #region 如果是移动应用就考虑多表单的问题.
			if (getcurrND().getHisFormType() == NodeFormType.SheetTree && this.getIsMobile() == true) {
				// 如果是表单树并且是，移动模式.
				FrmNodes fns = new FrmNodes();
				QueryObject qo = new QueryObject(fns);
				qo.AddWhere(FrmNodeAttr.FK_Node, getcurrND().getNodeID());
				qo.addAnd();
				qo.AddWhere(FrmNodeAttr.FrmEnableRole, "!=", FrmEnableRole.Disable.getValue());
				qo.addOrderBy("Idx");
				qo.DoQuery();
				// 把节点与表单的关联管理放入到系统.
				ds.Tables.add(fns.ToDataTableField("FrmNodes"));
			}
			/// #endregion 如果是移动应用就考虑多表单的问题.

			String str = BP.Tools.Json.ToJson(ds);

			return str;
		} catch (RuntimeException ex) {

			return "err@" + ex.getMessage();
		}
	}

	public String Confirm() throws Exception {
		// 确认
		BP.WF.Dev2Interface.Flow_Confirm(this.getWorkID());
		return "设置成功.";
	}

	public String Dtl_Init() throws Exception {
		// 初始化从表数据
		/// #region 组织参数.
		MapDtl mdtl = new MapDtl(this.getEnsName());
		mdtl.setNo(this.getEnsName());
		mdtl.RetrieveFromDBSources();

		if (this.getFK_Node() != 0 && !mdtl.getFK_MapData().equals("Temp")
				&& this.getEnsName().contains("ND" + this.getFK_Node()) == false) {
			Node nd = new BP.WF.Node(this.getFK_Node());
			// 如果
			// * 1,传来节点ID, 不等于0.
			// * 2,不是节点表单. 就要判断是否是独立表单，如果是就要处理权限方案。

			BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(),
					mdtl.getFK_MapData());
			int i = fn.Retrieve(FrmNodeAttr.FK_Frm, mdtl.getFK_MapData(), FrmNodeAttr.FK_Node, this.getFK_Node());
			if (i != 0 && fn.getFrmSlnInt() != 0) {
				// 使用了自定义的方案.
				// * 并且，一定为dtl设定了自定义方案，就用自定义方案.
				//
				mdtl.setNo(this.getEnsName() + "_" + this.getFK_Node());
				mdtl.RetrieveFromDBSources();
			}
		}

		if (this.GetRequestVal("IsReadonly").equals("1")) {
			mdtl.setIsInsert(false);
			mdtl.setIsDelete(false);
			mdtl.setIsUpdate(false);
		} else {

		}

		String strs = this.getRequestParas();
		strs = strs.replace("?", "@");
		strs = strs.replace("&", "@");
		/// #endregion 组织参数.

		// 获得他的描述,与数据.
		DataSet ds = BP.WF.CCFormAPI.GenerDBForCCFormDtl(mdtl.getFK_MapData(), mdtl,
				Integer.parseInt(this.getRefPKVal()), strs,this.getFID());

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 默认执行的方法
	 */
	public final String DoDefaultMethod() {
		return "err@没有此方法:getDoType=" + this.getDoType();
	}

	/**
	 * MyFlow_Init_DealUrl
	 * 
	 * @param currND
	 * @param currWK
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public final String MyFlow_Init_DealUrl(BP.WF.Node currND, Work currWK, String url) throws Exception {
		if (url == null) {
			url = currND.getFormUrl();
		}

		String urlExt = this.getRequestParas();
		// 防止查询不到.
		urlExt = urlExt.replace("?WorkID=", "&WorkID=");
		if (urlExt.contains("&WorkID") == false) {
			urlExt += "&WorkID=" + this.getWorkID();
		} else {
			urlExt = urlExt.replace("&WorkID=0", "&WorkID=" + this.getWorkID());
			urlExt = urlExt.replace("&WorkID=&", "&WorkID=" + this.getWorkID() + "&");
		}
		// SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出.
		url = url.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

		if (urlExt.contains("&NodeID") == false) {
			urlExt += "&NodeID=" + currND.getNodeID();
		}

		if (urlExt.contains("FK_Flow") == false) {
			urlExt += "&FK_Flow=" + currND.getFK_Flow();
		}

		if (urlExt.contains("FK_Node") == false) {
			urlExt += "&FK_Node=" + currND.getNodeID();
		}

		if (urlExt.contains("&FID") == false && currWK != null) {
			//urlExt += "&FID=" + currWK.getFID();
			urlExt += "&FID=" + this.getFID();
		}

		if (urlExt.contains("&UserNo") == false) {
			urlExt += "&UserNo=" + WebUser.getNo();
		}

		if (urlExt.contains("&SID") == false) {
			urlExt += "&SID=" + WebUser.getSID();
		}

		if (url.contains("?") == true) {
			url += "&" + urlExt;
		} else {
			url += "?" + urlExt;
		}

		@SuppressWarnings("rawtypes")
		Enumeration allKeys = this.getRequest().getParameterNames();
		String _str;
		while (allKeys.hasMoreElements()) {
			_str = allKeys.nextElement().toString();
			if (DataType.IsNullOrEmpty(_str) == true)
				continue;
			if (url.contains(_str + "=") == true)
				continue;
			url += "&" + _str + "=" + this.GetRequestVal(_str);
		}

		url = url.replace("?&", "?");
		url = url.replace("&&", "&");
		return url;
	}

	private Node _currNode = null;

	public final Node getcurrND() throws Exception {
		if (_currNode == null) {
			_currNode = new Node(this.getFK_Node());
		} else {
			if (this.getFK_Node() != _currNode.getNodeID())
				_currNode = new Node(this.getFK_Node());

		}
		return _currNode;
	}

	private Flow _currFlow = null;

	public final Flow getcurrFlow() throws Exception {
		if (_currFlow == null) {
			_currFlow = new Flow(this.getFK_Flow());
		} else {
			if (this.getFK_Flow().equals(_currFlow.getNo()) == false)
				_currFlow = new Flow(this.getFK_Flow());

		}
		return _currFlow;
	}

	private long _workID = 0;

	/// <summary>
	/// 定义跟路径
	/// </summary>
	public static String appPath = "/";

	@Override
	public final long getWorkID() {
		if (_workID != 0) {
			return _workID;
		}

		// 杨玉慧
		String str = this.GetRequestVal("WorkID");
		if (str == null || str.equals("") || str.equals("null")) {
			return 0;
		}
		return Integer.parseInt(str);
	}

	public final void setWorkID(long value) {
		_workID = value;
	}

	/**
	 * 从节点.
	 */
	public final String getFromNode() {
		return this.GetRequestVal("FromNode");
	}

	/**
	 * 是否抄送
	 */
	public final boolean getIsCC() {
		String str = this.GetRequestVal("Paras");

		if (DotNetToJavaStringHelper.isNullOrEmpty(str) == false) {
			String myps = str;

			if (myps.contains("IsCC=1") == true) {
				return true;
			}
		}

		str = this.GetRequestVal("AtPara");
		if (DotNetToJavaStringHelper.isNullOrEmpty(str) == false) {
			if (str.contains("IsCC=1") == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 轨迹ID
	 */
	public final String getTrackID() {
		return this.GetRequestVal("TrackeID");
	}

	/**
	 * 到达的节点ID
	 */
	public final int getToNode() {
		return this.GetRequestValInt("ToNode");
	}

	private int _FK_Node = 0;

	/**
	 * 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	 */
	public final int getFK_Node() {

		String fk_nodeReq = this.GetRequestVal("FK_Node"); // this.Request.Form["FK_Node"];
		if (DotNetToJavaStringHelper.isNullOrEmpty(fk_nodeReq)) {
			fk_nodeReq = this.GetRequestVal("NodeID"); // this.Request.Form["NodeID"];
		}

		if (DotNetToJavaStringHelper.isNullOrEmpty(fk_nodeReq) == false) {
			return Integer.parseInt(fk_nodeReq);
		}

		if (_FK_Node == 0) {

			if (this.getWorkID() != 0) {
				String sql = "SELECT FK_Node from  WF_GenerWorkFlow where WorkID=" + this.getWorkID();
				_FK_Node = DBAccess.RunSQLReturnValInt(sql);

			} else {
				if (this.getFK_Flow() != null)
					_FK_Node = Integer.parseInt(this.getFK_Flow() + "01");
				else
					return 0;
			}
		}
		return _FK_Node;
	}
	
	/**
	 * 根据用户名单点登录
	 * @return
	 * @throws Exception
	 */
	public String SingleSignOn() throws Exception{
		String userNo = this.GetRequestVal("UserNo");
		BP.Port.Emp emp = new Emp();
		emp.setNo(userNo);
		if (emp.RetrieveFromDBSources() ==0)
			return "err@用户不存在.";
		//调用登录方法.
		BP.WF.Dev2Interface.Port_Login(emp.getNo(), emp.getName(), emp.getFK_Dept(), emp.getFK_DeptText(),null,null);
		return "登录成功";
	}
	
	
	private String _width = "";

	/**
	 * 表单宽度
	 */
	public final String getWidth() {
		return _width;
	}

	public final void setWidth(String value) {
		_width = value;
	}

	private String _height = "";

	/**
	 * 表单高度
	 */
	public final String getHeight() {
		return _height;
	}

	public final void setHeight(String value) {
		_height = value;
	}

	public String _btnWord = "";

	public final String getBtnWord() {
		return _btnWord;
	}

	public final void setBtnWord(String value) {
		_btnWord = value;
	}

	private GenerWorkFlow _HisGenerWorkFlow = null;

	public final GenerWorkFlow getHisGenerWorkFlow() throws Exception {
		if (_HisGenerWorkFlow == null) {
			_HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
		}
		return _HisGenerWorkFlow;
	}

	public boolean isAskFor = false;

	// 杨玉慧
	public final String getDoType1() {
		return this.GetRequestVal("DoType1");
	}

	/**
	 * 获取主表的方法.
	 * 
	 * @return
	 */
	private java.util.Hashtable GetMainTableHT() {
		java.util.Hashtable htMain = new java.util.Hashtable();
		Enumeration enu = getRequest().getParameterNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (key == null) {
				continue;
			}

			if (key.contains("TB_")) {
				if (htMain.containsKey(key.replace("TB_", "")) == false)
					htMain.put(key.replace("TB_", ""), this.GetRequestVal(key));
				continue;
			}

			if (key.contains("DDL_")) {
				if (htMain.containsKey(key.replace("DDL_", "")) == false)
					htMain.put(key.replace("DDL_", ""), this.GetRequestVal(key));
				continue;
			}

			if (key.contains("CB_")) {
				if (htMain.containsKey(key.replace("CB_", "")) == false)
					htMain.put(key.replace("CB_", ""), this.GetRequestVal(key));
				continue;
			}

			if (key.contains("RB_")) {
				if (htMain.containsKey(key.replace("RB_", "")) == false)
					htMain.put(key.replace("RB_", ""), this.GetRequestVal(key));
				continue;
			}
		}
		return htMain;
	}

	/// <summary>
	/// 方法
	/// </summary>
	/// <returns></returns>
	public String HandlerMapExt() {
		try {

			WF_CCForm wf = new WF_CCForm(context);
			return wf.HandlerMapExt();

		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	public final String InitToolBarForMobile() throws Exception {
		String str = InitToolBar();
		str = str.replace("Send()", "SendIt()");
		return str;
	}

	/**
	 * 删除流程.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String MyFlow_StopFlow() throws Exception {
		try {
			String str = BP.WF.Dev2Interface.Flow_DoFlowOver(this.getFK_Flow(), this.getWorkID(), "流程成功结束");
			if (str.equals("") || str == null) {
				return "流程成功结束";
			}
			return str;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 加载前置导航数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String StartGuide_Init() throws Exception {
		String josnData = "";
		// 流程编号
		String fk_flow = this.GetRequestVal("FK_Flow");
		// 查询的关键字
		String skey = this.GetRequestVal("Keys");
		try {
			// 获取流程实例
			Flow fl = new Flow(fk_flow);
			// 获取设置的前置导航的sql
			Object tempVar = fl.getStartGuidePara2();
			String sql = (String) ((tempVar instanceof String) ? tempVar : null);
			// 判断是否有查询条件
			if (!StringUtils.isEmpty(skey)) {
				Object tempVar2 = fl.getStartGuidePara1();
				sql = (String) ((tempVar2 instanceof String) ? tempVar2 : null);
				sql = sql.replaceAll("@Key", skey).replaceAll("@key", skey);
			}
			sql = sql.replaceAll("~", "'");
			// 替换约定参数
			sql = sql.replaceAll("@WebUser.No", WebUser.getNo());
			sql = sql.replaceAll("@WebUser.Name", WebUser.getName());
			sql = sql.replaceAll("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sql = sql.replaceAll("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
			
			Enumeration enu = getRequest().getParameterNames();
			while (enu.hasMoreElements())
			{
				
				String key = (String) enu.nextElement();
				 if (sql.contains(key) == false)
	              continue;
				
				 sql = sql.replaceAll("@"+key, getRequest().getParameter(key));
			}


			// 获取数据
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

			// 判断前置导航的类型
			switch (fl.getStartGuideWay()) {
			case BySQLOne:
			case BySystemUrlOneEntity:
				josnData = BP.Tools.Json.ToJson(dt);
				break;
			case BySQLMulti:
				josnData = BP.Tools.Json.ToJson(dt);
				break;
			default:
				break;
			}
			return josnData;
		} catch (RuntimeException ex) {
			return "err@:" + ex.getMessage().toString();
		}
	}

	/**
	 * 批量发送
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String StartGuide_MulitSend() throws Exception {
		// 获取设置的数据源
		Flow fl = new Flow(this.getFK_Flow());
		String key = this.GetRequestVal("TB_Key");
		String SKey = this.GetRequestVal("Keys");
		String sql = "";
		// 判断是否有查询条件
		Object tempVar = fl.getStartGuidePara2();
		sql = (String) ((tempVar instanceof String) ? tempVar : null);
		if (!StringUtils.isEmpty(key)) {
			Object tempVar2 = fl.getStartGuidePara1();
			sql = (String) ((tempVar2 instanceof String) ? tempVar2 : null);
			sql = sql.replace("@Key", key).replaceAll("@key", key);
		}
		// 替换变量
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		// 获取选中的数据源
		//// DataRow[] drArr = dt.Select("No in(" +
		// DotNetToJavaStringHelper.trimEnd(SKey, ',') + ")");//java筛选问题
		List<DataRow> dataRowList = new ArrayList<DataRow>();
		DataRow[] drArr = null;
		String keys[] = SKey.split(",");
		for (DataRow rw : dt.Rows) {
			for (String st : keys) {
				if (st.replaceAll("'", "").equals(rw.getValue("No")))
					dataRowList.add(rw);
			}
		}
		drArr = new DataRow[dataRowList.size()];
		for (int i = 0; i < dataRowList.size(); i++) {
			drArr[i] = dataRowList.get(i);
		}
		// 获取Nos
		String Nos = "";
		for (int i = 0; i < drArr.length; i++) {
			DataRow row = drArr[i];
			Nos += row.getValue("No") + ",";
		}
		return DotNetToJavaStringHelper.trimEnd(Nos, ',');
	}
	
	/**
	 * 批量审批
	 * @return
	 * @throws Exception
	 */
	public String BathFlow() throws  Exception{
		String workIds = this.GetRequestVal("WorkIDs");
		if(DataType.IsNullOrEmpty(workIds))
			return "不存在待办事项";
		String[] workIdArray = workIds.split(",");
		String message="";
		for (String workId:workIdArray) {
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM WF_EmpWorks WHERE WorkID=" + workId+" OR FID="+workId);
			 if (dt.Rows.size() == 0){
				 message+="没有找到WORKID="+workId+"的待办事项<br/>";
				 continue;
			 }
			 for(DataRow dr : dt.Rows){
				 message +="流程"+dr.getValue("FK_Flow").toString()+" "+dr.getValue("FlowName").toString()+"工作ID "+workId;
				 
				 // 写入审核信息
				 int Fk_Node = Integer.parseInt(dr.getValue("FK_Node").toString());
				 FrmWorkCheck wcDesc = new FrmWorkCheck(Fk_Node);
				 if(wcDesc.getHisFrmWorkCheckSta().getValue() == 1)
					 Dev2Interface.WriteTrackWorkCheck(dr.getValue("FK_Flow").toString(), Fk_Node,Long.parseLong(workId), 0, "审核通过", wcDesc.getFWCOpLabel());
				 
				 SendReturnObjs returnObj =  BP.WF.Dev2Interface.Node_SendWork(dr.getValue("FK_Flow").toString(), Long.parseLong(workId), null, null);
				 message += returnObj.ToMsgOfHtml();
				 message +="<br/>"; 
	        }
		}
		return message;
	}
	
	//删除流程
	public String DeleteWorkFlow() throws Exception{
		boolean isCan = BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(), WebUser.getNo());
		if(isCan == false)
			return "你没有删除流程的权限";
		return BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
	
	}
	
	/**
	 * 单点登录
	 * @return
	 * @throws Exception
	 */
	public String SingleLogin() throws Exception{
		String UserNo = this.GetRequestVal("UserNo");
		if(DataType.IsNullOrEmpty(UserNo) == true){
			return "err@用户账户不能为空";
		}
		BP.Port.Emp emp = new Emp();
		emp.setNo(UserNo);
		 // 调用登录接口,写入登录信息。
        BP.WF.Dev2Interface.Port_Login(UserNo);
		BP.WF.Dev2Interface.Port_Login(emp.getNo(), emp.getName(), emp.getFK_Dept(), emp.getFK_DeptText(),null,null);
		return "登录成功.";
	}

	
}
