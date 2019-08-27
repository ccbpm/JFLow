package BP.WF.HttpHandler;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Difference.Handler.WebContralBase;
import BP.En.Entities;
import BP.En.EntityTree;
import BP.Port.Emp;
import BP.Sys.MapData;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.DeliveryWay;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.ConnDataFrom;
import BP.WF.Template.FrmEnableRole;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeExt;
import BP.WF.Template.FrmNodeExts;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.SQLTemplate;
import BP.WF.Template.SQLTemplates;


/**
 * 页面功能实体
 */
public class WF_Admin extends WebContralBase {
	
	/**
	 * 构造函数
	 */
	public WF_Admin()
	{
	
	}
	
	  /// <summary>
    /// 获得运行的集成平台.
    /// </summary>
    /// <returns></returns>
    public String TestFlow_GetRunOnPlant()
    {
        return BP.Sys.SystemConfig.getRunOnPlant();
    }
	
	public final String getRefNo() {
		return this.GetRequestVal("RefNo");
	}
	
	
	/**
	 * 初始化界面.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String TestFlow_Init() throws Exception {
		
		BP.Sys.SystemConfig.DoClearCash();

		// 让admin 登录.
		BP.WF.Dev2Interface.Port_Login("admin");

		if (this.getRefNo() != null) {
			HttpSession session = getRequest().getSession();
			Emp emp = new Emp(this.getRefNo());
			BP.Web.WebUser.SignInOfGener(emp);
			BP.Web.WebUser.SetSessionByKey("FK_Flow", this.getFK_Flow());
			return "url@../MyFlow.htm?FK_Flow=" + this.getFK_Flow();
		}

		Flow fl = new Flow(this.getFK_Flow());
		// fl.DoCheck();

		int nodeid = Integer.parseInt(this.getFK_Flow() + "01");
		DataTable dt = null;
		String sql = "";
		BP.WF.Node nd = new BP.WF.Node(nodeid);

		if (nd.getIsGuestNode()) {
			// 如果是guest节点，就让其跳转到 guest登录界面，让其发起流程。

			// 这个地址需要配置.
			return "url@/SDKFlowDemo/GuestApp/Login.htm?FK_Flow="
					+ this.getFK_Flow();
		}

		try {

			if (nd.getHisDeliveryWay().equals(DeliveryWay.ByStation)
					|| nd.getHisDeliveryWay().equals(DeliveryWay.ByStationOnly))// 如果是BPM
																				// 就不能工作.
			{
			 
					sql = "SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.No)   join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node="
							+ nd.getNodeID();
				
			}

			if (nd.getHisDeliveryWay().equals(DeliveryWay.ByDept)) {
				sql = "select No,Name from Port_Emp where FK_Dept in (select FK_Dept from WF_NodeDept where FK_Node='"
						+ nodeid + "') ";

			}

			if (nd.getHisDeliveryWay().equals(DeliveryWay.ByBindEmp)) {
				sql = "select No,Name from Port_Emp where No in (select FK_Emp from WF_NodeEmp where FK_Node='"
						+ nodeid + "') ";

			}

			if (nd.getHisDeliveryWay().equals(DeliveryWay.ByDeptAndStation)) {
				// 区别集成与BPM模式
				if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne) {
					sql = "SELECT No FROM Port_Emp WHERE No IN ";
					sql += "(SELECT No as FK_Emp FROM Port_Emp WHERE FK_Dept IN ";
					sql += "( SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node="
							+ nodeid + ")";
					sql += ")";
					sql += "AND No IN ";
					sql += "(";
					sql += "SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation()
							+ " WHERE FK_Station IN ";
					sql += "( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node="
							+ nodeid + ")";
					sql += ") ORDER BY No ";
				} else {
					sql = "SELECT pdes.FK_Emp AS No"
							+ " FROM   Port_DeptEmpStation pdes"
							+ "        INNER JOIN WF_NodeDept wnd"
							+ "             ON  wnd.FK_Dept = pdes.FK_Dept"
							+ "             AND wnd.FK_Node = "
							+ nodeid
							+ "        INNER JOIN WF_NodeStation wns"
							+ "             ON  wns.FK_Station = pdes.FK_Station"
							+ "             AND wnd.FK_Node =" + nodeid
							+ " ORDER BY" + "        pdes.FK_Emp";
				}
			}

			if (nd.getHisDeliveryWay().equals(DeliveryWay.BySelected))// 所有的人员多可以启动,
																		// 2016年11月开始约定此规则.
			{
				sql = "SELECT No as FK_Emp FROM Port_Emp ";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() > 300) {
					if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.MSSQL) {
						sql = "SELECT top 300 No as FK_Emp FROM Port_Emp ";
					}

					if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.Oracle) {
						sql = "SELECT  No as FK_Emp FROM Port_Emp WHERE ROWNUM <300 ";
					}

					if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.MySQL) {
						sql = "SELECT  No as FK_Emp FROM Port_Emp limit 0,300 ";
					}
				}
			}

			if (nd.getHisDeliveryWay().equals(DeliveryWay.ByDeptAndStation)) {

			}

			if (nd.getHisDeliveryWay().equals(DeliveryWay.BySQL)) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(nd
						.getDeliveryParas())) {
					return "err@您设置的按SQL访问开始节点，但是您没有设置sql.";
				}
			}

			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0) {
				return "err@您按照:" + nd.getHisDeliveryWay()
						+ "的方式设置的开始节点的访问规则，但是开始节点没有人员。";
			}

			// 构造人员表.
			DataTable dtEmps = new DataTable();
			dtEmps.Columns.Add("No");
			dtEmps.Columns.Add("Name");
			dtEmps.Columns.Add("FK_DeptText");

			// 处理发起人数据.
			String emps = "";
			for (DataRow dr : dt.Rows) {
				String myemp = dr.getValue(0).toString();
				if (emps.contains("," + myemp + ",") == true) {
					continue;
				}

				emps += "," + myemp + ",";

				BP.Port.Emp emp = new Emp(myemp);

				DataRow drNew = dtEmps.NewRow();

				drNew.setValueStr("No", emp.getNo());
				drNew.setValueStr("Name", emp.getName());
				drNew.setValueStr("FK_DeptText", emp.getFK_DeptText());

				dtEmps.Rows.add(drNew);
			}
			

            //检查物理表,避免错误.
            Nodes nds = new Nodes(this.getFK_Flow());
            for (Node mynd : nds.ToJavaList())
            {
                mynd.getHisWork().CheckPhysicsTable();
            }

			// 返回数据源.
			return BP.Tools.Json.DataTableToJson(dtEmps, false);
		} catch (RuntimeException ex) {
			return "err@<h2>您没有正确的设置开始节点的访问规则，这样导致没有可启动的人员，<a href='http://bbs.ccflow.org/showtopic-4103.aspx' target=_blank ><font color=red>点击这查看解决办法</font>.</a>。</h2> 系统错误提示:"
					+ ex.getMessage()
					+ "<br><h3>也有可能你你切换了OSModel导致的，什么是OSModel,请查看在线帮助文档 <a href='http://ccbpm.mydoc.io' target=_blank>http://ccbpm.mydoc.io</a>  .</h3>"
					+ ex.getMessage();
		}
	}

	/**
	 * 转到指定的url.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String TestFlow_ReturnToUser() throws Exception {
		
	    //检查物理表,避免错误.
        Nodes nds = new Nodes(this.getFK_Flow());
        for (Node mynd : nds.ToJavaList())
        {
            mynd.getHisWork().CheckPhysicsTable();
        }
        
		String userNo = this.GetRequestVal("UserNo");
		String sid = BP.WF.Dev2Interface.Port_Login(userNo);
		String url = "../../WF/Port.htm?UserNo=" + userNo + "&SID=" + sid
				+ "&DoWhat=" + this.GetRequestVal("DoWhat") + "&FK_Flow="
				+ this.getFK_Flow() + "&IsMobile="
				+ this.GetRequestVal("IsMobile");
		return "url@" + url;
	}

	@SuppressWarnings("unchecked")
	public final String DBInstall_Init() throws Exception {
		
		if (DBAccess.TestIsConnection() == false) {
			return "err@数据库连接配置错误 AppCenterDSN, AppCenterDBType 参数配置. ccflow请检查 web.config文件, jflow请检查 jflow.properties.";
		}

		if (BP.DA.DBAccess.IsExitsObject("WF_Flow") == true) {
			return "err@info数据库已经安装上了，您不必在执行安装. 点击:<a href='./CCBPMDesigner/Login.htm' >这里直接登录流程设计器</a>";
		}
		//检查是否区分大小写
        if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
        {
            String mysql = "CREATE TABLE TEST(OID int NOT NULL )";
            DBAccess.RunSQL(mysql);
            if (DBAccess.IsExitsObject("test") == false)
            {
                DBAccess.RunSQL("DROP TABLE TEST ");
                return "err@ccbpm不支持,mysql数据库区分大小写，请修改mysql数据库的设置,让其不区分大小写. 请参考设置:https://blog.csdn.net/ccflow/article/details/100079825";
            }
            if (DBAccess.IsExitsTableCol("test", "oid") == false)
            {
                DBAccess.RunSQL("DROP TABLE TEST ");
                return "err@ccbpm不支持,mysql数据库区分大小写，请修改mysql数据库的设置,让其不区分大小写. 请参考设置:https://blog.csdn.net/ccflow/article/details/100079825";
            }
        }

		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("OSModel", BP.WF.Glo.getOSModel().getValue()); // 组织结构类型.
		ht.put("DBType", SystemConfig.getAppCenterDBType().toString()); // 数据库类型.
		ht.put("Ver", BP.WF.Glo.Ver); // 版本号.

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	public final String DBInstall_Submit() {
		
		String lang = "CH";

		// 是否要安装demo.
		int demoTye = this.GetRequestValInt("DemoType");

		// 运行jflow的安装.
		try {

			BP.WF.Glo.DoInstallDataBase(lang, demoTye);

			// 执行jflow的升级。
			BP.WF.Glo.UpdataCCFlowVer();

			// 加注释.
			BP.Sys.PubClass.AddComment();
		} catch (Exception e) {
			e.printStackTrace();
			return "err@安装失败:" + e.getMessage();
		}
		return "info@系统成功安装 点击:<a href='./CCBPMDesigner/Login.htm' >这里直接登录流程设计器</a>";
	}

	/**
	 * 表单方案
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String BindFrms_Init() throws Exception {
		// 注册这个枚举，防止第一次运行出错.
		BP.Sys.SysEnums ses = new SysEnums("FrmEnableRole");

		String text = "";
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());

		boolean isHaveNDFrm = false;
		for (FrmNode fn : fns.ToJavaList()) {
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
			// fn.IsEdit ( true;
			fn.setIsEnableLoadData(true);
			//System.out.println(fn);
			fn.Insert();
			fns.AddEntity(fn);
		}

		// 组合这个实体才有外键信息.
		FrmNodeExts fnes = new FrmNodeExts();
		for (FrmNode fn : fns.ToJavaList()) {
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

	public final String ReLoginSubmit() throws Exception {
		String userNo = this.GetValFromFrmByKey("TB_UserNo");
		String password = this.GetValFromFrmByKey("TB_Pass");

		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo(userNo);
		if (emp.RetrieveFromDBSources() == 0) {
			return "err@用户名或密码错误.";
		}

		if (emp.CheckPass(password) == false) {
			return "err@用户名或密码错误.";
		}

		BP.Web.WebUser.SignInOfGener(emp);

		return "登录成功.";
	} 
	
	   /** 
			 加载模版.
			 
			 @return 
	   */
			public final String SettingTemplate_Init()
			{
				//类型.
				String templateType = this.GetRequestVal("TemplateType");
				String condType = this.GetRequestVal("CondType");

				BP.WF.Template.SQLTemplates sqls = new SQLTemplates();
				//sqls.Retrieve(BP.WF.Template.SQLTemplateAttr.SQLType, sqlType);

				DataTable dt = null;
				String sql = "";

	
				///#region 节点方向条件模版.
				if (templateType.equals("CondBySQL"))
				{
					//方向条件, 节点方向条件.
					sql = "SELECT MyPK,Note,OperatorValue FROM WF_Cond WHERE CondType=" + condType + " AND DataFrom=" + ConnDataFrom.SQL.getValue();
				}

				if (templateType.equals("CondByUrl"))
				{
					//方向条件, 节点方向url条件.
					sql = "SELECT MyPK,Note,OperatorValue FROM WF_Cond WHERE CondType=" + condType + " AND DataFrom=" + ConnDataFrom.Url.getValue();
				}

				if (templateType.equals("CondByPara"))
				{
					//方向条件, 节点方向url条件.
					sql = "SELECT MyPK,Note,OperatorValue FROM WF_Cond WHERE CondType=" + condType + " AND DataFrom=" + ConnDataFrom.Paras.getValue();
				}
	
				///#endregion 节点方向条件模版.

	
				///#region 表单扩展设置.
				if (templateType.equals("DDLFullCtrl"))
				{
					sql = "SELECT MyPK, '下拉框:'+ a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='DDLFullCtrl'";
				}

				if (templateType.equals("ActiveDDL"))
				{
					sql = "SELECT MyPK, '下拉框:'+ a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='ActiveDDL'";
				}

				//显示过滤.
				if (templateType.equals("AutoFullDLL"))
				{
					sql = "SELECT MyPK, '下拉框:'+ a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='AutoFullDLL'";
				}

				//文本框自动填充..
				if (templateType.equals("TBFullCtrl"))
				{
					sql = "SELECT MyPK, '文本框:'+ a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='TBFullCtrl'";
				}

				//自动计算.
				if (templateType.equals("AutoFull"))
				{
					sql = "SELECT MyPK, 'ID:'+ a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='AutoFull'";
				}
	
				///#endregion 表单扩展设置.

	
				///#region 节点属性的模版.
				//自动计算.
				if (templateType.equals("NodeAccepterRole"))
				{
					sql = "SELECT NodeID, FlowName +' - '+Name, a.DeliveryParas as Docs FROM WF_Node a WHERE  a.DeliveryWay=" + DeliveryWay.BySQL.getValue();
				}
	
				///#endregion 节点属性的模版.

				if (sql.equals(""))
				{
					return "err@没有涉及到的标记[" + templateType + "].";
				}

				dt = DBAccess.RunSQLReturnTable(sql);
				String strs = "";
				for (DataRow dr : dt.Rows)
				{
					BP.WF.Template.SQLTemplate en = new SQLTemplate();
					en.setNo(dr.getValue(0).toString());
					en.setName(dr.getValue(1).toString());
					en.setDocs(dr.getValue(2).toString());

					if (strs.contains(en.getDocs().trim() + ";") == true)
					{
						continue;
					}
					strs += en.getDocs().trim() + ";";
					sqls.AddEntity(en);
				}


				return sqls.ToJson();
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
		appendMenuSb.delete(0, appendMenuSb.length());

		appendMenuSb.append("[");
		for (EntityTree item : convertEntityMultiTree(ens)) {
			if (item.getParentNo() != null
					&& !item.getParentNo().equals(parentEn.getNo())) {
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
		appendMenuSb.delete(0, appendMenuSb.length());
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<EntityTree> convertEntityMultiTree(Object obj) {
		return (ArrayList<EntityTree>) obj;
	}

}