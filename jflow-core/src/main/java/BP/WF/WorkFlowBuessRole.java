package BP.WF;

import BP.DA.*;
import BP.Difference.Handler.PortalInterface;
import BP.WF.Data.*;
import BP.WF.Port.WFEmp;
import BP.WF.Template.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Tools.StringHelper;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.time.*;

/** 
 工作流程业务规则
*/
public class WorkFlowBuessRole
{

		///#region 生成标题的方法.
	/** 
	 生成标题
	 
	 @param wk 工作
	 @param emp 人员
	 @param rdt 日期
	 @return 生成string.
	 * @throws Exception 
	*/
	public static String GenerTitle(Flow fl, Work wk, Emp emp, String rdt) throws Exception
	{
		Object tempVar = fl.getTitleRole();
		String titleRole = tempVar instanceof String ? (String)tempVar : null;
		if (DataType.IsNullOrEmpty(titleRole))
		{
			// 为了保持与ccflow4.5的兼容,从开始节点属性里获取.
			Attr myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			if (myattr == null)
			{
				myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			}

			if (myattr != null)
			{
				titleRole = myattr.getDefaultVal().toString();
			}

			if (DataType.IsNullOrEmpty(titleRole) || titleRole.contains("@") == false)
			{
				titleRole = "@WebUser.getFK_DeptName-@WebUser.getNo(),@WebUser.getName()在@RDT发起.";
			}
		}


		titleRole = titleRole.replace("WebUser.No", emp.getNo());
		titleRole = titleRole.replace("@WebUser.Name", emp.getName());
		titleRole = titleRole.replace("@WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		titleRole = titleRole.replace("@WebUser.FK_DeptName", emp.getFK_DeptText());
		titleRole = titleRole.replace("@WebUser.FK_Dept", emp.getFK_Dept());
		titleRole = titleRole.replace("@RDT", rdt);
		if (titleRole.contains("@") == true)
		{
			Attrs attrs = wk.getEnMap().getAttrs();

			// 优先考虑外键的替换。
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}
				if (attr.getIsRefAttr() == false)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}

			//在考虑其它的字段替换.
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}
				if (attr.getIsRefAttr() == true)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}
		}
		titleRole = titleRole.replace('~', '-');
		titleRole = titleRole.replace("'", "”");

		if (titleRole.contains("@"))
		{
			/*如果没有替换干净，就考虑是用户字段拼写错误*/
			throw new RuntimeException("@请检查是否是字段拼写错误，标题中有变量没有被替换下来. @" + titleRole);
		}

		if (titleRole.contains("@"))
		{
			titleRole = GenerTitleExt(fl, wk.getNodeID(), wk.getOID(), titleRole);
		}

		wk.SetValByKey("Title", titleRole);
		return titleRole;
	}
	/** 
	 生成标题
	 
	 @param wk
	 @return 
	 * @throws Exception 
	*/
	public static String GenerTitle(Flow fl, Work wk) throws Exception
	{
		Object tempVar = fl.getTitleRole();
		String titleRole = tempVar instanceof String ? (String)tempVar : null;
		if (DataType.IsNullOrEmpty(titleRole))
		{
			// 为了保持与ccflow4.5的兼容,从开始节点属性里获取.
			Attr myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			if (myattr == null)
			{
				myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			}

			if (myattr != null)
			{
				titleRole = myattr.getDefaultVal().toString();
			}

			if (DataType.IsNullOrEmpty(titleRole) || titleRole.contains("@") == false)
			{
				titleRole = "@WebUser.getFK_DeptName-@WebUser.getNo(),@WebUser.getName()在@RDT发起.";
			}
		}

		if (titleRole.equals("@OutPara") || DataType.IsNullOrEmpty(titleRole) == true)
		{
			titleRole = "@WebUser.getFK_DeptName-@WebUser.getNo(),@WebUser.getName()在@RDT发起.";
		}

		titleRole = titleRole.replace("WebUser.No", wk.getRec());
		titleRole = titleRole.replace("@WebUser.Name", wk.getRecText());
		titleRole = titleRole.replace("@WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		titleRole = titleRole.replace("@WebUser.FK_DeptName", wk.getRecOfEmp().getFK_DeptText());
		titleRole = titleRole.replace("@WebUser.FK_Dept", wk.getRecOfEmp().getFK_Dept());
		titleRole = titleRole.replace("@RDT", wk.getRDT());

		if (titleRole.contains("@"))
		{
			Attrs attrs = wk.getEnMap().getAttrs();

			// 优先考虑外键的替换 , 因为外键文本的字段的长度相对较长。
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}
				if (attr.getIsRefAttr() == false)
				{
					continue;
				}

				String temp = wk.GetValStrByKey(attr.getKey());
				
				titleRole = titleRole.replace("@" + attr.getKey(), temp);
			}

			//在考虑其它的字段替换.
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}

				if (attr.getIsRefAttr() == true)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}
		}
		titleRole = titleRole.replace('~', '-');
		titleRole = titleRole.replace("'", "”");

		if (titleRole.contains("@"))
		{
			titleRole = GenerTitleExt(fl, wk.getNodeID(), wk.getOID(), titleRole);
		}

		// 为当前的工作设置title.
		wk.SetValByKey("Title", titleRole);

		return titleRole;
	}
	/** 
	 生成标题
	 
	 @param fl
	 @param wk
	 @return 
	 * @throws Exception 
	*/
	public static String GenerTitle(Flow fl, GERpt wk) throws Exception
	{
		Object tempVar = fl.getTitleRole();
		String titleRole = tempVar instanceof String ? (String)tempVar : null;
		if (DataType.IsNullOrEmpty(titleRole))
		{
			// 为了保持与ccflow4.5的兼容,从开始节点属性里获取.
			Attr myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			if (myattr == null)
			{
				myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			}

			if (myattr != null)
			{
				titleRole = myattr.getDefaultVal().toString();
			}

			if (DataType.IsNullOrEmpty(titleRole) || titleRole.contains("@") == false)
			{
				titleRole = "@WebUser.getFK_DeptName-@WebUser.getNo(),@WebUser.getName()在@RDT发起.";
			}
		}

		if (titleRole.equals("@OutPara") || DataType.IsNullOrEmpty(titleRole) == true)
		{
			titleRole = "@WebUser.getFK_DeptName-@WebUser.getNo(),@WebUser.getName()在@RDT发起.";
		}


		titleRole = titleRole.replace("@WebUser.getNo()", wk.getFlowStarter());
		titleRole = titleRole.replace("@WebUser.getName()", WebUser.getName());
		titleRole = titleRole.replace("@WebUser.getFK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		titleRole = titleRole.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName());
		titleRole = titleRole.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		titleRole = titleRole.replace("@RDT", wk.getFlowStartRDT());
		if (titleRole.contains("@"))
		{
			Attrs attrs = wk.getEnMap().getAttrs();

			// 优先考虑外键的替换,因为外键文本的字段的长度相对较长。
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}
				if (attr.getIsRefAttr() == false)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}

			//在考虑其它的字段替换.
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}

				if (attr.getIsRefAttr() == true)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}
		}
		titleRole = titleRole.replace('~', '-');
		titleRole = titleRole.replace("'", "”");

		if (titleRole.contains("@"))
		{
			titleRole = GenerTitleExt(fl, Integer.parseInt(fl.getNo() + "01"), wk.getOID(), titleRole);
		}

		// 为当前的工作设置title.
		wk.SetValByKey("Title", titleRole);
		return titleRole;
	}
	/** 
	 如果从节点表单上没有替换下来，就考虑独立表单的替换.
	 
	 @param fl 流程
	 @param workid 工作ID
	 @return 返回生成的标题
	 * @throws Exception 
	*/
	private static String GenerTitleExt(Flow fl, int nodeId, long workid, String titleRole) throws Exception
	{
		FrmNodes nds = new FrmNodes(fl.getNo(), nodeId);
		for (FrmNode item : nds.ToJavaList())
		{
			GEEntity en = null;
			try
			{
				en = new GEEntity(item.getFK_Frm());
				en.setPKVal(workid);
				if (en.RetrieveFromDBSources() == 0)
				{
					continue;
				}
			}
			catch (RuntimeException ex)
			{
				continue;
			}

			Attrs attrs = en.getEnMap().getAttrs();
			// 优先考虑外键的替换,因为外键文本的字段的长度相对较长。
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}
				if (attr.getIsRefAttr() == false)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), en.GetValStrByKey(attr.getKey()));
			}

			//在考虑其它的字段替换.
			for (Attr attr : attrs)
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}

				if (attr.getIsRefAttr() == true)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), en.GetValStrByKey(attr.getKey()));
			}

			//如果全部已经替换完成.
			if (titleRole.contains("@") == false)
			{
				return titleRole;
			}
		}
		return titleRole;
	}

		///#endregion 生成标题的方法.


		///#region 产生单据编号
	/** 
	 产生单据编号
	 
	 @param billFormat
	 @param en
	 @return 
	 * @throws Exception 
	*/
	public static String GenerBillNo(String billNo, long workid, Entity en, String flowPTable) throws Exception
	{
		if (DataType.IsNullOrEmpty(billNo))
		{
			return "";
		}

		if (billNo.contains("@"))
		{
			billNo = BP.WF.Glo.DealExp(billNo, en, null);
		}

		/*如果，Bill 有规则 */
		billNo = billNo.replace("{YYYY}", DateUtils.format(new Date(), "yyyy"));
		billNo = billNo.replace("{yyyy}", DateUtils.format(new Date(), "yyyy"));

		billNo = billNo.replace("{yy}", DateUtils.format(new Date(), "yy"));
		billNo = billNo.replace("{YY}", DateUtils.format(new Date(), "yy"));

		billNo = billNo.replace("{MM}", DateUtils.format(new Date(), "MM"));
		billNo = billNo.replace("{mm}", DateUtils.format(new Date(), "mm"));

		billNo = billNo.replace("{DD}",DateUtils.format(new Date(), "dd"));
		billNo = billNo.replace("{dd}",DateUtils.format(new Date(), "dd"));
		
		billNo = billNo.replace("{HH}", DateUtils.format(new Date(), "HH"));
		billNo = billNo.replace("{hh}", DateUtils.format(new Date(), "HH"));

		billNo = billNo.replace("{LSH}", (new Long(workid)).toString());
		billNo = billNo.replace("{WorkID}", (new Long(workid)).toString());
		billNo = billNo.replace("{OID}", (new Long(workid)).toString());

		if (billNo.contains("@WebUser.DeptZi"))
		{
			String val = DBAccess.RunSQLReturnStringIsNull("SELECT Zi FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'", "");
			billNo = billNo.replace("@WebUser.DeptZi", val.toString());
		}

		if (billNo.contains("{ParentBillNo}"))
		{
			String pWorkID = DBAccess.RunSQLReturnStringIsNull("SELECT PWorkID FROM " + flowPTable + " WHERE   WFState >1 AND  OID=" + workid, "0");
			String parentBillNo = DBAccess.RunSQLReturnStringIsNull("SELECT BillNo FROM WF_GenerWorkFlow WHERE WorkID=" + pWorkID, "");
			billNo = billNo.replace("{ParentBillNo}", parentBillNo);

			String sql = "";
			int num = 0;
			for (int i = 2; i < 9; i++)
			{
				if (billNo.contains("{LSH" + i + "}") == false)
				{
					continue;
				}

				sql = "SELECT COUNT(OID) FROM " + flowPTable + " WHERE PWorkID =" + pWorkID + " AND WFState >1 ";
				num = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				billNo = billNo + StringHelper.padLeft(String.valueOf(num), i, '0');
				billNo = billNo.replace("{LSH" + i + "}", "");
				break;
			}
		}
		else
		{
			String sql = "";
			int num = 0;
			String supposeBillNo = billNo; //假设单据号，长度与真实单据号一致
			ArrayList<Entry<Integer, Integer>> loc = new ArrayList<Entry<Integer, Integer>>(); //流水号位置，流水号位数
			String lsh; //流水号设置码
			int lshIdx = -1; //流水号设置码所在位置
			Map<Integer, Integer>  map = new HashMap<Integer, Integer>();
			for (int i = 2; i < 9; i++)
			{
				lsh = "{LSH" + i + "}";

				if (!supposeBillNo.contains(lsh))
				{
					continue;
				}

				while (supposeBillNo.contains(lsh))
				{
					//查找流水号所在位置
					lshIdx = supposeBillNo.indexOf(lsh);
					//将找到的流水号码替换成假设的流水号
					supposeBillNo = (lshIdx == 0 ? "" : supposeBillNo.substring(0, lshIdx)) + StringHelper.padLeft("", i, '_') + (lshIdx + 6 < supposeBillNo.length() ? supposeBillNo.substring(lshIdx + 6) : "");
					//保存当前流程号所处位置，及流程号长度，以便之后使用替换成正确的流水号
					map.put(lshIdx, i);
				}
			}
			
			Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Integer> entry = iterator.next();
				loc.add(entry);
			}

			//数据库中查找符合的单据号集合,NOTE:此处需要注意，在LIKE中带有左广方括号时，要使用一对广播号将其转义
			sql = "SELECT BillNo FROM " + flowPTable + " WHERE BillNo LIKE '" + supposeBillNo.replace("[", "[[]") + "'" + (flowPTable.toLowerCase().equals("wf_generworkflow") ? (" AND WorkID <> " + workid) : (" AND OID <> " + workid)) + " ORDER BY BillNo DESC";

			String maxBillNo = DBAccess.RunSQLReturnString(sql);
			int ilsh = 0;

			if (DataType.IsNullOrEmpty(maxBillNo))
			{
				//没有数据，则所有流水号都从1开始
				for (java.util.Map.Entry<Integer, Integer> kv : loc)
				{
					supposeBillNo = (kv.getKey() == 0 ? "" : supposeBillNo.substring(0, kv.getKey())) + StringHelper.padLeft("1", kv.getValue(), '0') + (kv.getKey() + kv.getValue() + 1 < supposeBillNo.length() ? supposeBillNo.substring(kv.getKey() + kv.getValue()) : "");
				}
			}
			else
			{
				//有数据，则从右向左开始判断流水号，当右侧的流水号达到最大值，则左侧的流水号自动加1
				HashMap<Integer, Integer> mlsh = new HashMap<Integer, Integer>();
				int plus1idx = -1;

				for (int i = loc.size() - 1; i >= 0; i--)
				{
					//获取单据号中当前位的流水码数
					ilsh = Integer.parseInt(StringHelper.substring(maxBillNo, loc.get(i).getKey(), loc.get(i).getValue()));

					if (plus1idx >= 0)
					{
						//如果当前码位被置为+1，则+1，同时将标识置为-1
						ilsh++;
						plus1idx = -1;
					}
					else
					{
						mlsh.put(loc.get(i).getKey(), i == loc.size() - 1 ? ilsh + 1 : ilsh);
						continue;
					}

					if (ilsh >= Integer.parseInt(StringHelper.padLeft("", loc.get(i).getValue(), '9')))
					{
						//右侧已经达到最大值
						if (i > 0)
						{
							//记录前位的码
							mlsh.put(loc.get(i).getKey(), 1);
						}
						else
						{
							supposeBillNo = "单据号超出范围";
							break;
						}

						//则将前一个流水码位，标记为+1
						plus1idx = i - 1;
					}
					else
					{
						mlsh.put(loc.get(i).getKey(), ilsh + 1);
					}
				}

				if (supposeBillNo.equals("单据号超出范围"))
				{
					return supposeBillNo;
				}

				//拼接单据号
				for (java.util.Map.Entry<Integer, Integer> kv : loc)
				{
					supposeBillNo = (kv.getKey() == 0 ? "" : supposeBillNo.substring(0, kv.getKey())) + StringHelper.padLeft(mlsh.get(kv.getKey()).toString(), kv.getValue(), '0') + (kv.getKey() + kv.getValue() + 1 < supposeBillNo.length() ? supposeBillNo.substring(kv.getKey() + kv.getValue()) : "");
				}
			}

			billNo = supposeBillNo;
		}

		return billNo;
	}

		///#endregion 产生单据编号


		///#region 获得下一个节点.
	/** 
	 获得下一个节点
	 
	 @param currNode 当前的节点
	 @param workid 工作ID
	 @param currWorkFlow 当前的工作主表信息
	 @param enPara 参数
	 @return 返回找到的节点
	 * @throws Exception 
	*/

	public static Node RequestNextNode(Node currNode, long workid, GenerWorkFlow currWorkFlow) throws Exception
	{
		return RequestNextNode(currNode, workid, currWorkFlow, null);
	}

	public static Node RequestNextNode(Node currNode, long workid, GenerWorkFlow currWorkFlow, GERpt enPara) throws Exception
	{
		if (currNode.getHisToNodes().size() == 1)
		{
			return (Node)currNode.getHisToNodes().get(0);
		}

		// 判断是否有用户选择的节点。
		if (currNode.getCondModel() == CondModel.ByUserSelected)
		{
			if (currWorkFlow == null)
			{
				throw new RuntimeException("@参数错误:currWorkFlow");
			}
			// 获取用户选择的节点.
			String nodes = currWorkFlow.getParas_ToNodes();
			if (DataType.IsNullOrEmpty(nodes))
			{
				throw new RuntimeException("@用户没有选择发送到的节点.");
			}

			String[] mynodes = nodes.split("[,]", -1);
			for (String item : mynodes)
			{
				if (DataType.IsNullOrEmpty(item))
				{
					continue;
				}
				//排除到达自身节点
				if (String.valueOf(currNode.getNodeID()).equals(item))
				{
					continue;
				}

				return new Node(Integer.parseInt(item));
			}

			//设置他为空,以防止下一次发送出现错误.
			currWorkFlow.setParas_ToNodes("");
		}


		// 检查当前的状态是是否是退回，.
		Nodes nds = currNode.getHisToNodes();
		if (nds.size() == 1)
		{
			Node toND = (Node)nds.get(0);
			return toND;
		}
		if (nds.size() == 0)
		{
			throw new RuntimeException("@没有找到它的下了步节点.");
		}

		Conds dcsAll = new Conds();
		dcsAll.Retrieve(CondAttr.NodeID, currNode.getNodeID(), CondAttr.CondType, CondType.Dir.getValue(), CondAttr.PRI);
		if (dcsAll.size() == 0)
		{
			throw new RuntimeException("@没有为节点(" + currNode.getNodeID() + " , " + currNode.getName() + ")设置方向条件,有分支的地方必须有方向条件.");
		}


			///#region 获取能够通过的节点集合，如果没有设置方向条件就默认通过.
		Nodes myNodes = new Nodes();
		int toNodeId = 0;
		int numOfWay = 0;
		for (Node nd : nds.ToJavaList())
		{
			Conds dcs = new Conds();
			for (Cond dc : dcsAll.ToJavaList())
			{
				if (dc.getToNodeID() != nd.getNodeID())
				{
					continue;
				}

				dc.setWorkID(workid);
				dc.setFID(workid);

				//如果当前的参数不为空.
				if (enPara != null)
				{
					dc.en = enPara;
				}

				dcs.AddEntity(dc);
			}

			if (dcs.size() == 0)
			{
			   // throw new Exception("@流程设计错误：从节点(" + currNode.Name + ")到节点(" + nd.Name + ")，没有设置方向条件，有分支的节点必须有方向条件。");
				continue;
			}

			if (dcs.getIsPass()) // 如果通过了.
			{
				myNodes.AddEntity(nd);
			}
		}

			///#endregion 获取能够通过的节点集合，如果没有设置方向条件就默认通过.

		// 如果没有找到,就找到没有设置方向条件的节点,没有设置方向条件的节点是默认同意的.
		if (myNodes.size() == 0)
		{
			for (Node nd : nds.ToJavaList())
			{
				Conds dcs = new Conds();
				for (Cond dc : dcsAll.ToJavaList())
				{
					if (dc.getToNodeID() != nd.getNodeID())
					{
						continue;
					}
					dcs.AddEntity(dc);
				}

				if (dcs.size() == 0)
				{
					return nd;
				}
			}
		}

		if (myNodes.size() == 0)
		{
			throw new RuntimeException("@当前用户(" + WebUser.getName() + "),定义节点的方向条件错误:从{" + currNode.getNodeID() + currNode.getName() + "}节点到其它节点,定义的所有转向条件都不成立.");
		}

		//如果找到1个.
		if (myNodes.size() == 1)
		{
			Node toND = myNodes.get(0) instanceof Node ? (Node)myNodes.get(0) : null;
			return toND;
		}

		//如果找到了多个.
		for (Cond dc : dcsAll.ToJavaList())
		{
			for (Node myND : myNodes.ToJavaList())
			{
				if (dc.getToNodeID() == myND.getNodeID())
				{
					return myND;
				}
			}
		}

		throw new RuntimeException("@找到下一步节点.");
	}

		///#endregion


		///#region 找到下一个节点的接受人员
	/** 
	 找到下一个节点的接受人员
	 
	 @param fl 流程
	 @param currNode 当前节点
	 @param toNode 到达节点
	 @return 下一步工作人员No,Name格式的返回.
	 * @throws Exception 
	*/
	public static DataTable RequetNextNodeWorkers(Flow fl, Node currNode, Node toNode, Entity enParas, long workid) throws Exception
	{
		if (toNode.getIsGuestNode())
		{
			/*到达的节点是客户参与的节点. add by zhoupeng 2016.5.11*/
			DataTable mydt = new DataTable();
			mydt.Columns.Add("No", String.class);
			mydt.Columns.Add("Name", String.class);

			DataRow dr = mydt.NewRow();
			dr.set("No", "Guest");
			dr.set("Name", "外部用户");
			mydt.Rows.add(dr);
			return mydt;
		}

		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		String sql;
		String FK_Emp;

		//变量.
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

		Paras ps = new Paras();
		// 按上一节点发送人处理。
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByPreviousNodeEmp)
		{
			DataRow dr = dt.NewRow();
			dr.setValue(0, WebUser.getNo());
			dt.Rows.add(dr);
			return dt;
		}


			///#region 首先判断是否配置了获取下一步接受人员的sql.
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySQL || toNode.getHisDeliveryWay() == DeliveryWay.BySQLTemplate || toNode.getHisDeliveryWay() == DeliveryWay.BySQLAsSubThreadEmpsAndData)
		{
			if (toNode.getHisDeliveryWay() == DeliveryWay.BySQLTemplate)
			{
				SQLTemplate st = new SQLTemplate(toNode.getDeliveryParas());
				sql = st.getDocs();
			}
			else
			{
				if (toNode.getDeliveryParas().length() < 4)
				{
					throw new RuntimeException("@您设置的当前节点按照SQL，决定下一步的接受人员，但是你没有设置SQL.");
				}
				sql = toNode.getDeliveryParas();
				sql = sql.toString();
			}

			//特殊的变量.
			sql = sql.replace("@FK_Node", String.valueOf(toNode.getNodeID()));
			sql = sql.replace("@NodeID", String.valueOf(toNode.getNodeID()));

			sql = Glo.DealExp(sql, enParas, null);
			if (sql.contains("@"))
			{
				if (Glo.getSendHTOfTemp() != null)
				{
					for (Object key : Glo.getSendHTOfTemp().keySet())
					{
						if(key == null)
							continue;
						sql = sql.replace("@" + key.toString(), Glo.getSendHTOfTemp().get(key).toString());
					}
				}
			}

			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 && toNode.getHisWhenNoWorker() == false)
			{
				throw new RuntimeException("@没有找到可接受的工作人员。@技术信息：执行的SQL没有发现人员:" + sql);
			}
			return dt;
		}

			///#endregion 首先判断是否配置了获取下一步接受人员的sql.





			///#region 按绑定部门计算,该部门一人处理标识该工作结束(子线程)..
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySetDeptAsSubthread)
		{
			if (toNode.getHisRunModel() != RunModel.SubThread)
			{
				throw new RuntimeException("@您设置的节点接收人方式为：按绑定部门计算,该部门一人处理标识该工作结束(子线程)，但是当前节点非子线程节点。");
			}

			sql = "SELECT No, Name,FK_Dept AS GroupMark FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + toNode.getNodeID() + ")";
			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 && toNode.getHisWhenNoWorker() == false)
			{
				throw new RuntimeException("@没有找到可接受的工作人员,接受人方式为, ‘按绑定部门计算,该部门一人处理标识该工作结束(子线程)’ @技术信息：执行的SQL没有发现人员:" + sql);
			}
			return dt;
		}

			///#endregion 按绑定部门计算,该部门一人处理标识该工作结束(子线程)..


			///#region 按照明细表,作为子线程的接收人.
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByDtlAsSubThreadEmps)
		{
			if (toNode.getHisRunModel() != RunModel.SubThread)
			{
				throw new RuntimeException("@您设置的节点接收人方式为：以分流点表单的明细表数据源确定子线程的接收人，但是当前节点非子线程节点。");
			}

			currNode.WorkID = workid; //为获取表单ID ( NodeFrmID )提供参数.

			BP.Sys.MapDtls dtls = new BP.Sys.MapDtls(currNode.getNodeFrmID());
			String msg = null;
			for (BP.Sys.MapDtl dtl : dtls.ToJavaList())
			{
				try
				{
					String empFild = toNode.getDeliveryParas();
					if (DataType.IsNullOrEmpty(empFild))
					{
						empFild = " UserNo ";
					}

					ps = new Paras();
					ps.SQL = "SELECT " + empFild + ", * FROM " + dtl.getPTable() + " WHERE RefPK=" + dbStr + "OID ORDER BY OID";
					ps.Add("OID", workid);
					dt = DBAccess.RunSQLReturnTable(ps);
					if (dt.Rows.size() == 0 && toNode.getHisWhenNoWorker() == false)
					{
						throw new RuntimeException("@流程设计错误，到达的节点（" + toNode.getName() + "）在指定的节点中没有数据，无法找到子线程的工作人员。");
					}
					return dt;
				}
				catch (RuntimeException ex)
				{
					msg += ex.getMessage();
					//if (dtls.size() == 1)
					//    throw new Exception("@估计是流程设计错误,没有在分流节点的明细表中设置");
				}
			}
			throw new RuntimeException("@没有找到分流节点的明细表作为子线程的发起的数据源，流程设计错误，请确认分流节点表单中的明细表是否有UserNo约定的系统字段。" + msg);
		}

			///#endregion 按照明细表,作为子线程的接收人.


			///#region 按节点绑定的人员处理.
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByBindEmp)
		{
			ps = new Paras();
			ps.Add("FK_Node", toNode.getNodeID());
			ps.SQL = "SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node=" + dbStr + "FK_Node ORDER BY FK_Emp";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@流程设计错误:下一个节点(" + toNode.getName() + ")没有绑定工作人员 . ");
			}
			return dt;
		}

			///#endregion 按节点绑定的人员处理.


			///#region 按照选择的人员处理。
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySelected || toNode.getHisDeliveryWay() == DeliveryWay.ByFEE)
		{
			ps = new Paras();
			ps.Add("FK_Node", toNode.getNodeID());
			ps.Add("WorkID", workid);
			ps.SQL = "SELECT FK_Emp FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				/*从上次发送设置的地方查询. */
				SelectAccpers sas = new SelectAccpers();
				int i = sas.QueryAccepterPriSetting(toNode.getNodeID());
				if (i == 0)
				{
					if (toNode.getHisDeliveryWay() == DeliveryWay.BySelected)
					{
						if (currNode.getCondModel() != CondModel.SendButtonSileSelect)
						{
							currNode.setCondModel(CondModel.SendButtonSileSelect);
							currNode.Update();
							throw new RuntimeException("@下一个节点的接收人规则是按照上一步发送人员选择器选择的，但是在当前节点您没有启接收人选择器，系统已经自动做了设置，请关闭当前窗口重新打开重试。");
						}

						throw new RuntimeException("@请选择下一步骤工作(" + toNode.getName() + ")接受人员。");
					}
					else
					{
						throw new RuntimeException("@流程设计错误，请重写FEE，然后为节点(" + toNode.getName() + ")设置接受人员，详细请参考cc流程设计手册。");
					}
				}

				//插入里面.
				for (SelectAccper item : sas.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, item.getFK_Emp());
					dt.Rows.add(dr);
				}
				return dt;
			}
			return dt;
		}

			///#endregion 按照选择的人员处理。


			///#region 按照指定节点的处理人计算。
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySpecNodeEmp || toNode.getHisDeliveryWay() == DeliveryWay.ByStarter)
		{
			/* 按指定节点岗位上的人员计算 */
			String strs = toNode.getDeliveryParas();
			if (toNode.getHisDeliveryWay() == DeliveryWay.ByStarter)
			{
				/*找开始节点的处理人员. */
				strs = Integer.parseInt(fl.getNo()) + "01";
				ps = new Paras();
				ps.SQL = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node AND IsPass=1 AND IsEnable=1 ";
				ps.Add("FK_Node", Integer.parseInt(strs));
				ps.Add("OID", workid);
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 1)
				{
					return dt;
				}
				else
				{
					/* 有可能当前节点就是第一个节点，那个时间还没有初始化数据，就返回当前人. */
					DataRow dr = dt.NewRow();
					dr.setValue(0, WebUser.getNo());
					dt.Rows.add(dr);
					return dt;
				}
			}

			// 首先从本流程里去找。
			strs = strs.replace(";", ",");
			String[] nds = strs.split("[,]", -1);
			for (String nd : nds)
			{
				if (DataType.IsNullOrEmpty(nd))
				{
					continue;
				}

				if (DataType.IsNumStr(nd) == false)
				{
					throw new RuntimeException("流程设计错误:您设置的节点(" + toNode.getName() + ")的接收方式为按指定的节点岗位投递，但是您没有在访问规则设置中设置节点编号。");
				}

				ps = new Paras();
				ps.SQL = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node AND IsPass=1 AND IsEnable=1 ";
				ps.Add("FK_Node", Integer.parseInt(nd));
				if (currNode.getHisRunModel() == RunModel.SubThread)
				{
					ps.Add("OID", workid);
				}
				else
				{
					ps.Add("OID", workid);
				}

				DataTable dt_ND = DBAccess.RunSQLReturnTable(ps);
				//添加到结果表
				if (dt_ND.Rows.size() != 0)
				{
					for (DataRow row : dt_ND.Rows)
					{
						DataRow dr = dt.NewRow();
						dr.setValue(0, row.get(0).toString());
						dt.Rows.add(dr);
					}
					//此节点已找到数据则不向下找，继续下个节点
					continue;
				}

				//就要到轨迹表里查,因为有可能是跳过的节点.
				ps = new Paras();
				ps.SQL = "SELECT " + TrackAttr.EmpFrom + " FROM ND" + Integer.parseInt(fl.getNo()) + "Track WHERE (ActionType=" + dbStr + "ActionType1 OR ActionType=" + dbStr + "ActionType2 OR ActionType=" + dbStr + "ActionType3 OR ActionType=" + dbStr + "ActionType4 OR ActionType=" + dbStr + "ActionType5) AND NDFrom=" + dbStr + "NDFrom AND WorkID=" + dbStr + "WorkID";
				ps.Add("ActionType1", ActionType.Skip.getValue());
				ps.Add("ActionType2", ActionType.Forward.getValue());
				ps.Add("ActionType3", ActionType.ForwardFL.getValue());
				ps.Add("ActionType4", ActionType.ForwardHL.getValue());
				ps.Add("ActionType5", ActionType.Start.getValue());

				ps.Add("NDFrom", Integer.parseInt(nd));
				ps.Add("WorkID", workid);
				dt_ND = DBAccess.RunSQLReturnTable(ps);
				if (dt_ND.Rows.size() != 0)
				{
					for (DataRow row : dt_ND.Rows)
					{
						DataRow dr = dt.NewRow();
						dr.setValue(0, row.get(0).toString());
						dt.Rows.add(dr);
					}
				}
			}

			//本流程里没有有可能该节点是配置的父流程节点,也就是说子流程的一个节点与父流程指定的节点的工作人员一致.
			GenerWorkFlow gwf = new GenerWorkFlow(workid);
			if (gwf.getPWorkID() != 0)
			{
				for (String pnodeiD : nds)
				{
					if (DataType.IsNullOrEmpty(pnodeiD))
					{
						continue;
					}

					Node nd = new Node(Integer.parseInt(pnodeiD));
					if (!nd.getFK_Flow().equals(gwf.getPFlowNo()))
					{
						continue; // 如果不是父流程的节点，就不执行.
					}

					ps = new Paras();
					ps.SQL = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node AND IsPass=1 AND IsEnable=1 ";
					ps.Add("FK_Node", nd.getNodeID());
					if (currNode.getHisRunModel() == RunModel.SubThread)
					{
						ps.Add("OID", gwf.getPFID());
					}
					else
					{
						ps.Add("OID", gwf.getPWorkID());
					}

					DataTable dt_PWork = DBAccess.RunSQLReturnTable(ps);
					if (dt_PWork.Rows.size() != 0)
					{
						for (DataRow row : dt_PWork.Rows)
						{
							DataRow dr = dt.NewRow();
							dr.setValue(0, row.get(0).toString());
							dt.Rows.add(dr);
						}
						//此节点已找到数据则不向下找，继续下个节点
						continue;
					}

					//就要到轨迹表里查,因为有可能是跳过的节点.
					ps = new Paras();
					ps.SQL = "SELECT " + TrackAttr.EmpFrom + " FROM ND" + Integer.parseInt(fl.getNo()) + "Track WHERE (ActionType=" + dbStr + "ActionType1 OR ActionType=" + dbStr + "ActionType2 OR ActionType=" + dbStr + "ActionType3 OR ActionType=" + dbStr + "ActionType4 OR ActionType=" + dbStr + "ActionType5) AND NDFrom=" + dbStr + "NDFrom AND WorkID=" + dbStr + "WorkID";
					ps.Add("ActionType1", ActionType.Start.getValue());
					ps.Add("ActionType2", ActionType.Forward.getValue());
					ps.Add("ActionType3", ActionType.ForwardFL.getValue());
					ps.Add("ActionType4", ActionType.ForwardHL.getValue());
					ps.Add("ActionType5", ActionType.Skip.getValue());
					ps.Add("NDFrom", nd.getNodeID());

					if (currNode.getHisRunModel() == RunModel.SubThread)
					{
						ps.Add("OID", gwf.getPFID());
					}
					else
					{
						ps.Add("OID", gwf.getPWorkID());
					}

					dt_PWork = DBAccess.RunSQLReturnTable(ps);
					if (dt_PWork.Rows.size() != 0)
					{
						for (DataRow row : dt_PWork.Rows)
						{
							DataRow dr = dt.NewRow();
							dr.setValue(0, row.get(0).toString());
							dt.Rows.add(dr);
						}
					}
				}
			}
			//返回指定节点的处理人
			if (dt.Rows.size() != 0)
			{
				return dt;
			}

			throw new RuntimeException("@流程设计错误，到达的节点（" + toNode.getName() + "）在指定的节点(" + strs + ")中没有数据，无法找到工作的人员。 @技术信息如下: 投递方式:BySpecNodeEmp sql=" + ps.getSQLNoPara());
		}

			///#endregion 按照节点绑定的人员处理。


			///#region 按照上一个节点表单指定字段的人员处理。
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormEmpsField)
		{
			// 检查接受人员规则,是否符合设计要求.
			String specEmpFields = toNode.getDeliveryParas();
			if (DataType.IsNullOrEmpty(specEmpFields))
			{
				specEmpFields = "SysSendEmps";
			}

			if (enParas.getEnMap().getAttrs().Contains(specEmpFields) == false)
			{
				throw new RuntimeException("@您设置的接受人规则是按照表单指定的字段，决定下一步的接受人员，该字段{" + specEmpFields + "}已经删除或者丢失。");
			}

			//获取接受人并格式化接受人, 
			String emps = enParas.GetValStringByKey(specEmpFields);
			emps = emps.replace(" ", "");
			if (emps.contains(",") && emps.contains(";"))
			{
				/*如果包含,; 例如 zhangsan,张三;lisi,李四;*/
				String[] myemps1 = emps.split("[;]", -1);
				for (String str : myemps1)
				{
					if (DataType.IsNullOrEmpty(str))
					{
						continue;
					}

					String[] ss = str.split("[,]", -1);
					DataRow dr = dt.NewRow();
					dr.setValue(0, ss[0]);
					dt.Rows.add(dr);
				}
				if (dt.Rows.size() == 0)
				{
					throw new RuntimeException("@输入的接受人员信息错误;[" + emps + "]。");
				}
				else
				{
					return dt;
				}
			}

			emps = emps.replace(";", ",");
			emps = emps.replace("；", ",");
			emps = emps.replace("，", ",");
			emps = emps.replace("、", ",");
			emps = emps.replace("@", ",");

			if (DataType.IsNullOrEmpty(emps))
			{
				throw new RuntimeException("@没有在字段[" + enParas.getEnMap().getAttrs().GetAttrByKey(specEmpFields).getDesc() + "]中指定接受人，工作无法向下发送。");
			}

			// 把它加入接受人员列表中.
			String[] myemps = emps.split("[,]", -1);
			for (String s : myemps)
			{
				if (DataType.IsNullOrEmpty(s))
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue(0, s);
				dt.Rows.add(dr);
			}
			return dt;
		}

			///#endregion 按照上一个节点表单指定字段的人员处理。


			///#region 获得项目编号.
		String prjNo = "";
		FlowAppType flowAppType = currNode.getHisFlow().getHisFlowAppType();
		sql = "";
		if (currNode.getHisFlow().getHisFlowAppType() == FlowAppType.PRJ)
		{
			prjNo = "";
			try
			{
				prjNo = enParas.GetValStrByKey("PrjNo");
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@当前流程是工程类流程，但是在节点表单中没有PrjNo字段(注意区分大小写)，请确认。@异常信息:" + ex.getMessage());
			}
		}

			///#endregion 获得项目编号.


			///#region 按部门与岗位的交集计算.
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByDeptAndStation)
		{

			sql = "SELECT pdes.FK_Emp AS No"
				  + " FROM   Port_DeptEmpStation pdes"
				  + " INNER JOIN WF_NodeDept wnd ON wnd.FK_Dept = pdes.FK_Dept"
				  + " AND wnd.FK_Node = " + toNode.getNodeID() + " INNER JOIN WF_NodeStation wns ON  wns.FK_Station = pdes.FK_Station"
				  + " AND wns.FK_Node =" + toNode.getNodeID() + " ORDER BY pdes.FK_Emp";

			dt = DBAccess.RunSQLReturnTable(sql);


			if (dt.Rows.size() > 0)
			{
				return dt;
			}
			else
			{
				throw new RuntimeException("@节点访问规则(" + toNode.getHisDeliveryWay().toString() + ")错误:节点(" + toNode.getNodeID() + "," + toNode.getName() + "), 按照岗位与部门的交集确定接受人的范围错误，没有找到人员:SQL=" + sql);
			}
		}

			///#endregion 按部门与岗位的交集计算.


			///#region 判断节点部门里面是否设置了部门，如果设置了就按照它的部门处理。
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByDept)
		{
			ps = new Paras();
			ps.Add("FK_Node", toNode.getNodeID());
			ps.Add("WorkID", workid);
			ps.SQL = "SELECT FK_Emp FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() > 0)
			{
				return dt;
			}

			if (flowAppType == FlowAppType.Normal)
			{
				ps = new Paras();
				ps.SQL = "SELECT  A.No, A.Name  FROM Port_Emp A, WF_NodeDept B WHERE A.FK_Dept=B.FK_Dept AND B.FK_Node=" + dbStr + "FK_Node";
				ps.Add("FK_Node", toNode.getNodeID());
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() > 0 && toNode.getHisWhenNoWorker() == false)
				{
					return dt;
				}
				else
				{
					throw new RuntimeException("@按部门确定接受人的范围,没有找到人员.");
				}
			}

			if (flowAppType == FlowAppType.PRJ)
			{
				sql = " SELECT A.No,A.Name FROM Port_Emp A, WF_NodeDept B, Prj_EmpPrjStation C, WF_NodeStation D ";
				sql += "  WHERE A.FK_Dept=B.FK_Dept AND A.No=C.FK_Emp AND C.FK_Station=D.FK_Station AND B.FK_Node=D.FK_Node ";
				sql += "  AND C.FK_Prj=" + dbStr + "FK_Prj  AND D.FK_Node=" + dbStr + "FK_Node";

				ps = new Paras();
				ps.Add("FK_Prj", prjNo);
				ps.Add("FK_Node", toNode.getNodeID());
				ps.SQL = sql;

				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					/* 如果项目组里没有工作人员就提交到公共部门里去找。*/
					sql = "SELECT NO FROM Port_Emp WHERE NO IN ";

					if (Glo.getOSModel() == OSModel.OneOne)
					{
					   sql += "(SELECT No FK_Emp FROM Port_Emp WHERE FK_Dept IN ";
					}
					else
					{
						sql += "(SELECT No FROM Port_Emp WHERE FK_Dept IN ";
					}


					sql += "( SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + dbStr + "FK_Node1)";
					sql += ")";
					sql += "AND NO IN ";
					sql += "(";
					sql += "SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN ";
					sql += "( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node2)";
					sql += ")";
					sql += " ORDER BY No";

					ps = new Paras();
					ps.Add("FK_Node1", toNode.getNodeID());
					ps.Add("FK_Node2", toNode.getNodeID());
					ps.SQL = sql;
				}
				else
				{
					return dt;
				}

				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() > 0)
				{
					return dt;
				}
			}
		}

			///#endregion 判断节点部门里面是否设置了部门，如果设置了，就按照它的部门处理。


			///#region 仅按岗位计算
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByStationOnly)
		{
			sql = "SELECT A.FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node ORDER BY A.FK_Emp";
			ps = new Paras();
			ps.Add("FK_Node", toNode.getNodeID());
			ps.SQL = sql;
			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() > 0)
			{
				return dt;
			}
			else
			{
				throw new RuntimeException("@节点访问规则错误:节点(" + toNode.getNodeID() + "," + toNode.getName() + "), 仅按岗位计算，没有找到人员:SQL=" + ps.getSQLNoPara());
			}
		}

			///#endregion


			///#region 按岗位计算(以部门集合为纬度).
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByStationAndEmpDept)
		{
			/* 考虑当前操作人员的部门, 如果本部门没有这个岗位就不向上寻找. */

			ps = new Paras();
			sql = "SELECT No,Name FROM Port_Emp WHERE No=" + dbStr + "FK_Emp ";
			ps.Add("FK_Emp", WebUser.getNo());
			dt = DBAccess.RunSQLReturnTable(ps);



			if (dt.Rows.size() > 0)
			{
				return dt;
			}
			else
			{
				throw new RuntimeException("@节点访问规则(" + toNode.getHisDeliveryWay().toString() + ")错误:节点(" + toNode.getNodeID() + "," + toNode.getName() + "), 按岗位计算(以部门集合为纬度)。技术信息,执行的SQL=" + ps.getSQLNoPara());
			}
		}

			///#endregion

		String empNo = WebUser.getNo();
		String empDept = WebUser.getFK_Dept();


			///#region 按指定的节点的人员岗位，做为下一步骤的流程接受人。
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySpecNodeEmpStation)
		{
			/* 按指定的节点的人员岗位 */
			String para = toNode.getDeliveryParas();
			para = para.replace("@", "");

			if (DataType.IsNumStr(para) == true)
			{
				ps = new Paras();
				ps.SQL = "SELECT FK_Emp,FK_Dept FROM WF_GenerWorkerList WHERE WorkID=" + dbStr + "OID AND FK_Node=" + dbStr + "FK_Node ";
				ps.Add("OID", workid);
				ps.Add("FK_Node", Integer.parseInt(para));

				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() != 1)
				{
					throw new RuntimeException("@流程设计错误，到达的节点（" + toNode.getName() + "）在指定的节点中没有数据，无法找到工作的人员。");
				}

				empNo = dt.Rows.get(0).getValue(0).toString();
				empDept = dt.Rows.get(0).getValue(1).toString();
			}
			else
			{
				if (enParas.getRow().containsKey(para) == false)
				{
					throw new RuntimeException("@在找人接收人的时候错误@字段{" + para + "}不包含在rpt里，流程设计错误。");
				}

				empNo = enParas.GetValStrByKey(para);
				if (DataType.IsNullOrEmpty(empNo))
				{
					throw new RuntimeException("@字段{" + para + "}不能为空，没有取出来处理人员。");
				}

				BP.Port.Emp em = new BP.Port.Emp(empNo);
				empDept = em.getFK_Dept();
			}
		}

			///#endregion 按指定的节点人员，做为下一步骤的流程接受人。


			///#region 最后判断 - 按照岗位来执行。
		if (currNode.getIsStartNode() == false)
		{
			ps = new Paras();
			if (flowAppType == FlowAppType.Normal || flowAppType == FlowAppType.DocFlow)
			{
				// 如果当前的节点不是开始节点， 从轨迹里面查询。
				sql = "SELECT DISTINCT FK_Emp  FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN "
				   + "(SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + toNode.getNodeID() + ") "
				   + "AND FK_Emp IN (SELECT FK_Emp FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node IN (" + DataType.PraseAtToInSql(toNode.getGroupStaNDs(), true) + ") )";

				sql += " ORDER BY FK_Emp ";

				ps.SQL = sql;
				ps.Add("WorkID", workid);
			}

			if (flowAppType == FlowAppType.PRJ)
			{
				// 如果当前的节点不是开始节点， 从轨迹里面查询。
				sql = "SELECT DISTINCT FK_Emp  FROM Prj_EmpPrjStation WHERE FK_Station IN "
				   + "(SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node ) AND FK_Prj=" + dbStr + "FK_Prj "
				   + "AND FK_Emp IN (SELECT FK_Emp FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node IN (" + DataType.PraseAtToInSql(toNode.getGroupStaNDs(), true) + ") )";
				sql += " ORDER BY FK_Emp ";

				ps = new Paras();
				ps.SQL = sql;
				ps.Add("FK_Node", toNode.getNodeID());
				ps.Add("FK_Prj", prjNo);
				ps.Add("WorkID", workid);

				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					/* 如果项目组里没有工作人员就提交到公共部门里去找。*/
					sql = "SELECT DISTINCT FK_Emp  FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN "
					 + "(SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node ) "
					 + "AND FK_Emp IN (SELECT FK_Emp FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node IN (" + DataType.PraseAtToInSql(toNode.getGroupStaNDs(), true) + ") )";
					sql += " ORDER BY FK_Emp ";

					ps = new Paras();
					ps.SQL = sql;
					ps.Add("FK_Node", toNode.getNodeID());
					ps.Add("WorkID", workid);
				}
				else
				{
					return dt;
				}
			}

			dt = DBAccess.RunSQLReturnTable(ps);
			// 如果能够找到.
			if (dt.Rows.size() >= 1)
			{
				if (dt.Rows.size() == 1)
				{
					/*如果人员只有一个的情况，说明他可能要 */
				}
				return dt;
			}
		}

		/* 如果执行节点 与 接受节点岗位集合一致 */
		if (currNode.getGroupStaNDs().equals(toNode.getGroupStaNDs()))
		{
			/* 说明，就把当前人员做为下一个节点处理人。*/
			DataRow dr = dt.NewRow();
			dr.setValue(0, WebUser.getNo());
			dt.Rows.add(dr);
			return dt;
		}

		/* 如果执行节点 与 接受节点岗位集合不一致 */
		if (!currNode.getGroupStaNDs().equals(toNode.getGroupStaNDs()))
		{
			/* 没有查询到的情况下, 先按照本部门计算。*/
			if (flowAppType == FlowAppType.Normal)
			{
				if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database)
				{
						sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B  WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND A.FK_Dept=" + dbStr + "FK_Dept";
					ps = new Paras();
					ps.SQL = sql;
					ps.Add("FK_Node", toNode.getNodeID());
					ps.Add("FK_Dept", empDept);
				}

				if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
				{
					DataTable dtStas = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + toNode.getNodeID());
					String stas = DBAccess.GenerWhereInPKsString(dtStas);
					PortalInterface ws = new PortalInterface();
					return ws.GenerEmpsBySpecDeptAndStats(empDept, stas);
				}
			}

			if (flowAppType == FlowAppType.PRJ)
			{
				sql = "SELECT  FK_Emp  FROM Prj_EmpPrjStation WHERE FK_Prj=" + dbStr + "FK_Prj1 AND FK_Station IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node)"
				+ " AND  FK_Prj=" + dbStr + "FK_Prj2 ";
				sql += " ORDER BY FK_Emp ";

				ps = new Paras();
				ps.SQL = sql;
				ps.Add("FK_Prj1", prjNo);
				ps.Add("FK_Node", toNode.getNodeID());
				ps.Add("FK_Prj2", prjNo);
				dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0)
				{
					/* 如果项目组里没有工作人员就提交到公共部门里去找。 */

					if (Glo.getOSModel() == OSModel.OneMore)
					{
						sql = "SELECT No FROM Port_Emp WHERE NO IN "
					  + "(SELECT  FK_Emp  FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node))"
					  + " AND  NO IN "
					  + "(SELECT FK_Emp FROM Port_DeptEmp WHERE FK_Dept =" + dbStr + "FK_Dept)";
						sql += " ORDER BY No ";
					}
					else
					{
						sql = "SELECT No FROM Port_Emp WHERE NO IN "
					+ "(SELECT  FK_Emp  FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node))"
					+ " AND  NO IN "
					+ "(SELECT No FK_Emp FROM Port_Emp WHERE FK_Dept =" + dbStr + "FK_Dept)";
						sql += " ORDER BY No ";
					}


					ps = new Paras();
					ps.SQL = sql;
					ps.Add("FK_Node", toNode.getNodeID());
					ps.Add("FK_Dept", empDept);
					//  dt = DBAccess.RunSQLReturnTable(ps);
				}
				else
				{
					return dt;
				}
			}

			dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				NodeStations nextStations = toNode.getNodeStations();
				if (nextStations.size() == 0)
				{
					throw new RuntimeException("@节点没有岗位:" + toNode.getNodeID() + "  " + toNode.getName());
				}
			}
			else
			{
				boolean isInit = false;
				for (DataRow dr : dt.Rows)
				{
					if (dr.get(0).toString().equals(WebUser.getNo()))
					{
						/* 如果岗位分组不一样，并且结果集合里还有当前的人员，就说明了出现了当前操作员，拥有本节点上的岗位也拥有下一个节点的工作岗位
						 导致：节点的分组不同，传递到同一个人身上。 */
						isInit = true;
					}
				}
				return dt;
			}
		}

		/*这里去掉了向下级别寻找的算法. */


		/* 没有查询到的情况下, 按照最大匹配数 提高一个级别计算，递归算法未完成。
		 * 因为:以上已经做的岗位的判断，就没有必要在判断其它类型的节点处理了。
		 * */
		Object tempVar = empDept;
		String nowDeptID = tempVar instanceof String ? (String)tempVar : null;
		while (true)
		{
			BP.Port.Dept myDept = new BP.Port.Dept(nowDeptID);
			nowDeptID = myDept.getParentNo();
			if (nowDeptID.equals("-1") || nowDeptID.toString().equals("0"))
			{
				break; //一直找到了最高级仍然没有发现，就跳出来循环从当前操作员人部门向下找。
			}

			//检查指定的部门下面是否有该人员.
			DataTable mydtTemp = RequetNextNodeWorkers_DiGui(nowDeptID, empNo, toNode);
			if (mydtTemp == null)
			{
				/*如果父亲级没有，就找父级的平级. */
				BP.Port.Depts myDepts = new BP.Port.Depts();
				myDepts.Retrieve(BP.Port.DeptAttr.ParentNo, myDept.getParentNo());
				for (BP.Port.Dept item : myDepts.ToJavaList())
				{
					if (nowDeptID.equals(item.getNo()))
					{
						continue;
					}
					mydtTemp = RequetNextNodeWorkers_DiGui(item.getNo(), empNo, toNode);
					if (mydtTemp == null)
					{
						continue;
					}
					else
					{
						return mydtTemp;
					}
				}

				continue; //如果平级也没有，就continue.
			}
			else
			{
				return mydtTemp;
			}
		}

		/*如果向上找没有找到，就考虑从本级部门上向下找。 */
		Object tempVar2 = empDept;
		nowDeptID = tempVar2 instanceof String ? (String)tempVar2 : null;
		BP.Port.Depts subDepts = new BP.Port.Depts(nowDeptID);

		//递归出来子部门下有该岗位的人员.
		DataTable mydt123 = RequetNextNodeWorkers_DiGui_ByDepts(subDepts, empNo, toNode);
		if (mydt123 == null)
		{
			throw new RuntimeException("@按岗位计算没有找到(" + toNode.getName() + ")接受人.");
		}
		return mydt123;

			///#endregion  按照岗位来执行。
	}
	/** 
	 递归出来子部门下有该岗位的人员
	 
	 @param subDepts
	 @param empNo
	 @return 
	 * @throws Exception 
	*/
	private static DataTable RequetNextNodeWorkers_DiGui_ByDepts(BP.Port.Depts subDepts, String empNo, Node toNode) throws Exception
	{
		for (BP.Port.Dept item : subDepts.ToJavaList())
		{
			DataTable dt = RequetNextNodeWorkers_DiGui(item.getNo(), empNo, toNode);
			if (dt != null)
			{
				return dt;
			}

			dt = RequetNextNodeWorkers_DiGui_ByDepts(item.getHisSubDepts(), empNo, toNode);
			if (dt != null)
			{
				return dt;
			}
		}
		return null;
	}
	/** 
	 根据部门获取下一步的操作员
	 
	 @param deptNo
	 @param emp1
	 @return 
	 * @throws Exception 
	*/
	private static DataTable RequetNextNodeWorkers_DiGui(String deptNo, String empNo, Node toNode) throws Exception
	{
		String sql;
		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();

			sql = "SELECT FK_Emp as No FROM Port_DeptEmpStation A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node AND A.FK_Dept=" + dbStr + "FK_Dept AND A.FK_Emp!=" + dbStr + "FK_Emp";
		Paras ps = new Paras();
		ps.SQL = sql;
		ps.Add("FK_Node", toNode.getNodeID());
		ps.Add("FK_Dept", deptNo);
		ps.Add("FK_Emp", empNo);

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() == 0)
		{
			NodeStations nextStations = toNode.getNodeStations();
			if (nextStations.size() == 0)
			{
				throw new RuntimeException("@节点没有岗位:" + toNode.getNodeID() + "  " + toNode.getName());
			}

			sql = "SELECT No FROM Port_Emp WHERE No IN ";
			sql += "(SELECT  FK_Emp  FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + dbStr + "FK_Node ) )";
			sql += " AND No IN ";

			if (deptNo.equals("1"))
			{
				sql += "(SELECT No as FK_Emp FROM Port_Emp WHERE No!=" + dbStr + "FK_Emp ) ";
			}
			else
			{
				BP.Port.Dept deptP = new BP.Port.Dept(deptNo);
				sql += "(SELECT No as FK_Emp FROM Port_Emp WHERE No!=" + dbStr + "FK_Emp AND FK_Dept = '" + deptP.getParentNo() + "')";
			}

			ps = new Paras();
			ps.SQL = sql;
			ps.Add("FK_Node", toNode.getNodeID());
			ps.Add("FK_Emp", empNo);
			dt = DBAccess.RunSQLReturnTable(ps);

			if (dt.Rows.size() == 0)
			{
				return null;
			}
			return dt;
		}
		else
		{
			return dt;
		}
	}

		///#endregion 找到下一个节点的接受人员


		///#region 执行抄送.
	/** 
	 执行抄送.
	 
	 @param rpt
	 @param workid
	 @return 
	 * @throws Exception 
	*/
	public static String DoCCAuto(Node node, GERpt rpt, long workid, long fid) throws Exception
	{

			if (node.getHisCCRole() == CCRole.AutoCC || node.getHisCCRole() == CCRole.HandAndAuto)
			{

			}
		else
		{
			return "";
		}

		CC ccEn = new CC(node.getNodeID());

		/*如果是自动抄送*/

		//执行抄送.
		DataTable dt = ccEn.GenerCCers(rpt, workid);
		if (dt.Rows.size() == 0)
		{
			return "@设置的抄送规则，没有找到抄送人员。";
		}

		String ccMsg = "@消息自动抄送给";
		String basePath = BP.WF.Glo.getHostURL();

		for (DataRow dr : dt.Rows)
		{
			String toUserNo = dr.get(0).toString();
			String toUserName = dr.get(1).toString();

			//生成标题与内容.
			Object tempVar = ccEn.getCCTitle();
			String ccTitle = tempVar instanceof String ? (String)tempVar : null;
			ccTitle = BP.WF.Glo.DealExp(ccTitle, rpt, null);

			Object tempVar2 = ccEn.getCCDoc();
			String ccDoc = tempVar2 instanceof String ? (String)tempVar2 : null;
			ccDoc = BP.WF.Glo.DealExp(ccDoc, rpt, null);

			ccDoc = ccDoc.replace("@Accepter", toUserNo);
			ccTitle = ccTitle.replace("@Accepter", toUserNo);

			//抄送信息.
			ccMsg += "(" + toUserNo + " - " + toUserName + ");";
			CCList list = new CCList();
			list.setMyPK( workid + "_" + node.getNodeID() + "_" + dr.get(0).toString());
			list.setFK_Flow(node.getFK_Flow());
			list.setFlowName(node.getFlowName());
			list.setFK_Node(node.getNodeID());
			list.setNodeName(node.getName());
			list.setTitle(ccTitle);
			list.setDoc(ccDoc);
			list.setCCTo(dr.get(0).toString());
			list.setCCToName(dr.get(1).toString());
			list.setRDT(DataType.getCurrentDataTime());
			list.setRec(WebUser.getNo());
			list.setWorkID(workid);
			list.setFID(fid);

			// if (this.HisNode.CCWriteTo == CCWriteTo.Todolist)
			list.setInEmpWorks(node.getCCWriteTo() == CCWriteTo.CCList ? false : true); //added by liuxc,2015.7.6

			//写入待办和写入待办与抄送列表,状态不同
			if (node.getCCWriteTo() == CCWriteTo.All || node.getCCWriteTo() == CCWriteTo.Todolist)
			{
				//如果为写入待办则抄送列表中置为已读，原因：只为不提示有未读抄送。
				//list.HisSta = node.CCWriteTo == CCWriteTo.All ? CCSta.UnRead : CCSta.Read;
				list.setHisSta(CCSta.UnRead);
			}
			//结束节点只写入抄送列表
			if (node.getIsEndNode() == true)
			{
				list.setHisSta(CCSta.UnRead);
				list.setInEmpWorks(false);
			}
			try
			{
				list.Insert();
			}
			catch (java.lang.Exception e)
			{
				list.Update();
			}

			if (BP.WF.Glo.getIsEnableSysMessage() == true)
			{
				//     //写入消息提示.
				//     ccMsg += list.CCTo + "(" + dr[1].ToString() + ");";
				//     WFEmp wfemp = new Port.WFEmp(list.CCTo);
				//     string sid = list.CCTo + "_" + list.WorkID + "_" + list.FK_Node + "_" + list.RDT;
				//     string url = basePath + "WF/Do.aspx?DoType=OF&SID=" + sid;
				//     string urlWap = basePath + "WF/Do.aspx?DoType=OF&SID=" + sid + "&IsWap=1";
				//     string mytemp = mailTemp as string;
				//     mytemp = string.Format(mytemp, wfemp.Name, WebUser.getName(), url, urlWap);
				//     string title = string.Format("工作抄送:{0}.工作:{1},发送人:{2},需您查阅",
				//this.HisNode.FlowName, this.HisNode.Name, WebUser.getName());
				//     BP.WF.Dev2Interface.Port_SendMsg(wfemp.No, title, mytemp, null, BP.Sys.SMSMsgType.CC, list.FK_Flow, list.FK_Node, list.WorkID, list.FID);
			}
		}


		//写入日志.

		return ccMsg;
	}
	/** 
	 按照指定的字段执行抄送.
	 
	 @param nd
	 @param rptGE
	 @param workid
	 @param fid
	 @return 
	 * @throws Exception 
	*/
	public static String DoCCByEmps(Node nd, GERpt rptGE, long workid, long fid) throws Exception
	{
		if (nd.getHisCCRole() != CCRole.BySysCCEmps)
		{
			return "";
		}

		CC cc = nd.getHisCC();

		//生成标题与内容.
		Object tempVar = cc.getCCTitle();
		String ccTitle = tempVar instanceof String ? (String)tempVar : null;
		ccTitle = BP.WF.Glo.DealExp(ccTitle, rptGE, null);

		Object tempVar2 = cc.getCCDoc();
		String ccDoc = tempVar2 instanceof String ? (String)tempVar2 : null;
		ccDoc = BP.WF.Glo.DealExp(ccDoc, rptGE, null);

		//取出抄送人列表
		String ccers = rptGE.GetValStrByKey("SysCCEmps");
		if (DataType.IsNullOrEmpty(ccers) == false)
		{
			return "";
		}

		String[] cclist = ccers.split("[|]", -1);
		Hashtable ht = new Hashtable();
		for (String item : cclist)
		{
			String[] tmp = item.split("[,]", -1);
			ht.put(tmp[0], tmp[1]);
		}
		String ccMsg = "@消息自动抄送给";
		String basePath = BP.WF.Glo.getHostURL();

		String mailTemp = BP.DA.DataType.ReadTextFile2Html(BP.Sys.SystemConfig.getPathOfDataUser() + "/EmailTemplete/CC_" + WebUser.getSysLang() + ".txt");
		for (Object item : ht.keySet())
		{
			ccDoc = ccDoc.replace("@Accepter", ht.get(item).toString());
			ccTitle = ccTitle.replace("@Accepter", ht.get(item).toString());
			//抄送信息.
			ccMsg += "(" + ht.get(item).toString() + " - " + ht.get(item).toString() + ");";


				///#region 如果是写入抄送列表.
			CCList list = new CCList();
			list.setMyPK( DBAccess.GenerGUID()); // workid + "_" + node.NodeID + "_" + item.Key.ToString();
			list.setFK_Flow(nd.getFK_Flow());
			list.setFlowName(nd.getFlowName());
			list.setFK_Node(nd.getNodeID());
			list.setNodeName(nd.getName());
			list.setTitle(ccTitle);
			list.setDoc(ccDoc);
			list.setCCTo(item.toString());
			list.setCCToName(ht.get(item).toString());
			list.setRDT(DataType.getCurrentDataTime());
			list.setRec(WebUser.getNo());
			list.setWorkID(workid);
			list.setFID(fid);
			list.setInEmpWorks(nd.getCCWriteTo() == CCWriteTo.CCList ? false : true); //added by liuxc,2015.7.6
			//写入待办和写入待办与抄送列表,状态不同
			if (nd.getCCWriteTo() == CCWriteTo.All || nd.getCCWriteTo() == CCWriteTo.Todolist)
			{
				//如果为写入待办则抄送列表中置为已读，原因：只为不提示有未读抄送。
				list.setHisSta(nd.getCCWriteTo() == CCWriteTo.All ? CCSta.UnRead : CCSta.Read);
			}
			//如果为结束节点，只写入抄送列表
			if (nd.getIsEndNode() == true)
			{
				list.setHisSta(CCSta.UnRead);
				list.setInEmpWorks(false);
			}

			//执行保存或更新
			try
			{
				list.Insert();
			}
			catch (java.lang.Exception e)
			{
				list.CheckPhysicsTable();
				list.Update();
			}

				///#endregion 如果要写入抄送


				///#region 写入消息机制.
			if (BP.WF.Glo.getIsEnableSysMessage() == true)
			{
				ccMsg += list.getCCTo() + "(" + ht.get(item).toString() + ");";
				WFEmp wfemp = new WFEmp(list.getCCTo());

				String sid = list.getCCTo() + "_" + list.getWorkID() + "_" + list.getFK_Node() + "_" + list.getRDT();
				String url = basePath + "WF/Do.htm?DoType=OF&SID=" + sid;
				url = url.replace("//", "/");
				url = url.replace("//", "/");

				String urlWap = basePath + "WF/Do.htm?DoType=OF&SID=" + sid + "&IsWap=1";
				urlWap = urlWap.replace("//", "/");
				urlWap = urlWap.replace("//", "/");

				Object tempVar3 = mailTemp;
				String mytemp = tempVar3 instanceof String ? (String)tempVar3 : null;
				mytemp = String.format(mytemp, wfemp.getName(), WebUser.getName(), url, urlWap);

				String title = String.format("工作抄送:%1$s.工作:%2$s,发送人:%3$s,需您查阅", nd.getFlowName(), nd.getName(), WebUser.getName());

				BP.WF.Dev2Interface.Port_SendMsg(wfemp.getNo(), title, mytemp, null, BP.WF.SMSMsgType.CC, list.getFK_Flow(), list.getFK_Node(), list.getWorkID(), list.getFID());
			}

		}

		return ccMsg;
	}

}