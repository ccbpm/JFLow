package cn.jflow.controller.wf.admin.FindWorker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DataTable;
import BP.En.Map;
import BP.WF.DeliveryWay;
import BP.WF.Node;
import BP.WF.Template.NodeDepts;
import BP.WF.Template.NodeEmpAttr;
import BP.WF.Template.NodeEmps;
import BP.WF.Template.NodeStationAttr;
import BP.WF.Template.NodeStations;

@Controller
@RequestMapping("/WF/NodeAccepterRole")
@Scope("request")
public class NodeAccepterRoleController extends BP.Web.WebPage {

	@RequestMapping(value = "/Save", method = RequestMethod.POST)
	public void Save(HttpServletRequest request, HttpServletResponse response,
			String CBL_8, String CBL_11, String xxx, String NodeID,
			String TB_2, String DDL_5, String TB_12, String TB_13,
			Boolean CB_IsSSS, boolean CB_IsRememme, boolean CB_IsExpSender) {
		// 获取选择的 09.与指定节点处理人相同
		String strzdjd = CBL_8;
		/*
		 * if ("".equals(CBL_8)) { strzdjd += li.getValue() + ","; }
		 */

		strzdjd = trimEnd(strzdjd, ',');
		String strzdjdTemp = "";
		String[] a = strzdjd.split(",");
		for(String x:a){
			strzdjdTemp+=(x.split(" ")[0]+",");
		}
		
		// 12.按指定节点的人员岗位计算
		String strzdjdgw = CBL_11;
		strzdjdgw = trimEnd(strzdjdgw, ',');
		String strzdjdgwTemp = "";
		String[] b = strzdjdgw.split(",");
		for(String x:b){
			strzdjdgwTemp+=(x.split(" ")[0]+",");
		}
		if(strzdjdTemp.length()>1){
			strzdjd = trimEnd(strzdjdTemp, ',');
		}
		if(strzdjdgwTemp.length()>1){
			strzdjdgw = trimEnd(strzdjdgwTemp, ',');
		}
		/*
		 * for (ListItem li : this.CBL_11.Items) { if (li.Selected) { strzdjdgw
		 * += li.getValue() + ","; } }
		 */

		Node nd = new Node(NodeID);
		String sql = "";
		if ("RB_0".equals(xxx)) {
			// 按岗位(以部门为纬度)
			nd.setHisDeliveryWay(DeliveryWay.ByStation);
			nd.DirectUpdate();
			// 按照岗位.
			NodeStations nss = new NodeStations();
			nss.Retrieve(NodeStationAttr.FK_Node, NodeID);
//			if (nss.size() == 0) {
//				BP.Sys.PubClass.Alert(
//						"@您选择的是请设置岗位来计算接受人，请设置岗位没有岗位无法计算该节点的接受人。", response);
//			}

		}

		if ("RB_1".equals(xxx)) {
			// 按部门
			nd.setHisDeliveryWay(DeliveryWay.ByDept);
			nd.DirectUpdate();
			// 按照岗位.
			NodeDepts nss = new NodeDepts();
			nss.Retrieve(NodeStationAttr.FK_Node, NodeID);
//			if (nss.size() == 0) {
//				BP.Sys.PubClass.Alert(
//						"@您选择的是请设置部门来计算接受人，请设置部门没有部门无法计算该节点的接受人。", response);
//			}

		}

		if ("RB_2".equals(xxx)) {
			// 按SQL
			nd.setHisDeliveryWay(DeliveryWay.BySQL);
			nd.setDeliveryParas(TB_2);
			nd.DirectUpdate();
			sql = TB_2;
			if (sql.length() <= 5) {
				BP.Sys.PubClass.Alert("@请设置完整的SQL", response);
				//return "err@请设置完整的SQL";
			}

			// 检查SQL是否符合要求.
			try {
				 //替换.避免出错
                sql = sql.replace("@WorkID", "0");//工作ID
                sql = sql.replace("@FID", "0");//
                sql = sql.replace("@OID", "0");
                sql = sql.replace("@FK_Node", "0");
                sql = sql.replace("@FK_Flow", "0");
                sql = sql.replace("@PWorkID", "0");
                sql = sql.replace("@PFlowNo", "0");
                sql = sql.replace("@PNodeID", "0");
				
				sql = BP.WF.Glo.DealExp(sql, null, null);
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				if (dt.Columns.contains("No") == false
						|| dt.Columns.contains("Name") == false) {
					BP.Sys.PubClass
							.Alert("@查询结果集合里不包含No,Name两个列, 分别标识操作员编号，操作员名称。",
									response);
					//return "err@查询结果集合里不包含No,Name两个列, 分别标识操作员编号，操作员名称。";
				}
			} catch (RuntimeException ex) {
				BP.Sys.PubClass.Alert("@设置的SQL不符合要求SQL=" + sql + ",其他信息：" + ex.getMessage(),response);
				//return "err@设置的SQL不符合要求SQL=" + sql + ",其他信息：" + ex.getMessage();
			}

		}

		if ("RB_3".equals(xxx)) {
			// 按本节点绑定的人员
			nd.setHisDeliveryWay(DeliveryWay.ByBindEmp);
			nd.DirectUpdate();

			// 按照岗位.
			NodeEmps nss = new NodeEmps();
			nss.Retrieve(NodeEmpAttr.FK_Node, NodeID);
//			if (nss.size() == 0) {
//				BP.Sys.PubClass.Alert(
//						"@您选择的是请设置人员来计算接受人，请设置人员没有人员无法计算该节点的接受人。", response);
//			}

		}
		if ("RB_4".equals(xxx)) {
			// 由上一步发送人选择
			nd.setHisDeliveryWay(DeliveryWay.BySelected);
			nd.DirectUpdate();

		}
		Map p = new Map();
		if ("RB_5".equals(xxx)) {

			// 按表单选择人员
			nd.setHisDeliveryWay(DeliveryWay.ByPreviousNodeFormEmpsField);
			nd.setDeliveryParas(DDL_5);
			nd.DirectUpdate();

		}
		if ("RB_6".equals(xxx)) {
			// 与上一节点的人员相同
			nd.setHisDeliveryWay(DeliveryWay.ByPreviousNodeEmp);
			nd.DirectUpdate();
		}
		if ("RB_7".equals(xxx)) {
			// 与开始节点的人员相同
			nd.setHisDeliveryWay(DeliveryWay.ByStarter);
			nd.DirectUpdate();
		}
		if ("RB_8".equals(xxx)) {
			// 与指定节点的人员相同
			nd.setHisDeliveryWay(DeliveryWay.BySpecNodeEmp);
			nd.setDeliveryParas(strzdjd);
			nd.DirectUpdate();
		}
		if ("RB_9".equals(xxx)) {
			// 按岗位与部门交集计算
			nd.setHisDeliveryWay(DeliveryWay.ByDeptAndStation);
			nd.DirectUpdate();
		}
		if ("RB_10".equals(xxx)) {
			// 按岗位计算(以部门集合为纬度)
			nd.setHisDeliveryWay(DeliveryWay.ByStationAndEmpDept);
			nd.DirectUpdate();
		}
		if ("RB_11".equals(xxx)) {
			// 按指定节点的人员或者指定字段作为人员的岗位计算
			nd.setHisDeliveryWay(DeliveryWay.BySpecNodeEmpStation);
			nd.setDeliveryParas(strzdjdgw);
			nd.DirectUpdate();
		}
		if ("RB_12".equals(xxx)) {
			sql = TB_12;
			if (sql.length() <= 5) {
				BP.Sys.PubClass.Alert("@请设置完整的SQL", response);
			}
			// 按SQL确定子线程接受人与数据源.
			nd.setHisDeliveryWay(DeliveryWay.BySQLAsSubThreadEmpsAndData);
			nd.setDeliveryParas(TB_12);
			nd.DirectUpdate();

		}
		if ("RB_13".equals(xxx)) {
			sql = TB_13;
			if (sql.length() <= 5) {
				BP.Sys.PubClass.Alert("@请设置完整的SQL", response);
			}
			// 按明细表确定子线程接受人.
			nd.setHisDeliveryWay(DeliveryWay.ByDtlAsSubThreadEmps);
			nd.setDeliveryParas(TB_13);
			nd.DirectUpdate();

		}
		if ("RB_14".equals(xxx)) {
			// 仅按岗位计算.
			nd.setHisDeliveryWay(DeliveryWay.ByStationOnly);
			nd.DirectUpdate();
		}
		if ("RB_15".equals(xxx)) {
			// FEE计算.
			nd.setHisDeliveryWay(DeliveryWay.ByFEE);
			nd.DirectUpdate();
		}
		if ("RB_16".equals(xxx)) {
			// 按照按绑定部门计算，该部门一人处理标识该工作结束(子线程)
			nd.setHisDeliveryWay(DeliveryWay.BySetDeptAsSubthread);
			nd.DirectUpdate();
		}
		if ("RB_17".equals(xxx)) {
			// 按照ccflow的BPM模式处理
			nd.setHisDeliveryWay(DeliveryWay.ByCCFlowBPM);
			nd.DirectUpdate();

		}
		// 是否可以分配工作？
		nd.setIsTask(CB_IsSSS);
		// 是否启用自动记忆功能

		nd.setIsRememberMe(CB_IsRememme);

		// 本节点接收人不允许包含上一步发送人
		nd.setIsExpSender(CB_IsExpSender);
		// 发送后转向
	}

	/**
	 * 06.按上一节点表单指定的字段值作为本步骤的接受人 参数: 请在上一节点表单创建SysSendEmps文本框
	 */
	/*
	 * public final void bindDDL_5() { String str = (new
	 * Integer(this.getNodeID())).toString().substring(0, (new
	 * Integer(this.getNodeID())).toString().length() - 2); BP.Sys.MapAttrs
	 * attrs = new BP.Sys.MapAttrs("ND" + str + "Rpt");
	 * 
	 * for (BP.Sys.MapAttr item : MapAttrs.convertMapAttrs(attrs)) {
	 * this.DDL_5.Items.Add(new ListItem(item.KeyOfEn + " " + item.getName(),
	 * item.KeyOfEn)); } }
	 */

	/**
	 * 09.与指定节点处理人相同 取值 12.按指定节点的人员岗位计算
	 */
	/*
	 * public final void bindCBL() { BP.WF.Node nd = new
	 * BP.WF.Node(this.getNodeID()); this.CBL_8.Items.Clear();
	 * this.CBL_11.Items.Clear(); BP.WF.Nodes nds = new
	 * BP.WF.Nodes(nd.HisFlow.No); for (BP.WF.Node item : nds) {
	 * this.CBL_8.Items.Add(new ListItem(item.NodeID + " " + item.getName(),
	 * item.NodeID.toString())); this.CBL_11.Items.Add(new ListItem(item.NodeID
	 * + " " + item.getName(), item.NodeID.toString())); } }
	 */
	// ----------------------------------------------------------------------------------------
	// Copyright © 2006 - 2010 Tangible Software Solutions Inc.
	// This class can be used by anyone provided that the copyright notice
	// remains
	// intact.
	//
	// This class is used to simulate some .NET string functions in Java.
	// ----------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'IsNullOrEmpty'.
	// ------------------------------------------------------------------------------------
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (2 parameter
	// version).
	// ------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (4 parameter
	// version).
	// ------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray,
			int startindex, int count) {
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length
				&& index - startindex < count; index++) {
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'TrimEnd'.
	// ------------------------------------------------------------------------------------
	public static String trimEnd(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					lengthToKeep = index;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'TrimStart'.
	// ------------------------------------------------------------------------------------
	public static String trimStart(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					startingIndex = index + 1;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(startingIndex);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Trim' when arguments
	// are used.
	// ------------------------------------------------------------------------------------
	public static String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	// ------------------------------------------------------------------------------------
	// This method is used for string equality comparisons when the option
	// 'Use helper 'stringsEqual' method to handle null strings' is selected
	// (The Java String 'equals' method can't be called on a null instance).
	// ------------------------------------------------------------------------------------
	public static boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}

}