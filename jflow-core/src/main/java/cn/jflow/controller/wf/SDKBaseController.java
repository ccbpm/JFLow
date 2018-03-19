package cn.jflow.controller.wf;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityMultiTree;
import BP.En.QueryObject;
import BP.Sys.AppType;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.SendReturnObjs;
import BP.WF.WorkNode;
import BP.WF.Data.GERpt;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CondModel;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.ShowWhere;
import BP.WF.Template.CCSta;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.FlowFormTree;
import BP.WF.Template.FlowFormTrees;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTreeAttr;
import BP.WF.Template.SysFormTrees;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.CCRole;
import BP.WF.DeliveryWay;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF/SDKComponents/Base")
public class SDKBaseController extends BaseController
{
		///#region 参数.
	public final String getUTF8ToString(String param)
	{
		return ContextHolderUtils.getRequest().getParameter(param);
	}
	public final String getDoFunc()
	{
		return getUTF8ToString("DoFunc");
	}
	public final String getCFlowNo()
	{
		return getUTF8ToString("CFlowNo");
	}
	public final String getWorkIDs()
	{
		return getUTF8ToString("WorkIDs");
	}
	public final int getFK_Node()
	{
		String fk_node = getUTF8ToString("FK_Node");
		if (!StringHelper.isNullOrEmpty(fk_node))
		{
			return Integer.parseInt(getUTF8ToString("FK_Node"));
		}
		return 0;
	}
	public final long getFID()
	{
		return Long.parseLong(getUTF8ToString("FID"));
	}
	// endregion 参数.
	@RequestMapping(value = "/SDKBaseHolder", method = RequestMethod.GET)
	public ModelAndView SDKBaseHolder()
	{

		if (WebUser.getNo() == null)
		{
			return null;
		}

		String method = "";
		//返回值
		String s_responsetext = "";
		
		if (!StringHelper.isNullOrEmpty(getUTF8ToString("method")))
		{
			method = getUTF8ToString("method");
		}


//		switch (method)
//ORIGINAL LINE: case "getapptoolbar":
		if (method.equals("getapptoolbar"))
		{
				s_responsetext = GetAppToolBar();
		}
//ORIGINAL LINE: case "getflowformtree":
		else if (method.equals("getflowformtree")) //获取表单树
		{
				s_responsetext = GetFlowFormTree();
		}
//ORIGINAL LINE: case "checkaccepter":
		else if (method.equals("checkaccepter")) //接受人检查
		{
				s_responsetext = CheckAccepterOper();
		}
//ORIGINAL LINE: case "sendcase":
		else if (method.equals("sendcase")) //执行发送
		{
				s_responsetext = SendCase();
		}
//ORIGINAL LINE: case "sendcasetonode":
		else if (method.equals("sendcasetonode")) //执行发送到指定节点
		{
				s_responsetext = SendCaseToNode();
		}
//ORIGINAL LINE: case "unsendcase":
		else if (method.equals("unsendcase")) //撤销发送
		{
				s_responsetext = UnSendCase();
		}
//ORIGINAL LINE: case "":
		else if (method.equals("")) //保存
		{
				s_responsetext = Send();
		}
//ORIGINAL LINE: case "delcase":
		else if (method.equals("delcase")) //删除流程
		{
				s_responsetext = Delcase();
		}
//ORIGINAL LINE: case "signcase":
		else if (method.equals("signcase")) //流程签名
		{
				s_responsetext = Signcase();
		}
//ORIGINAL LINE: case "endcase":
		else if (method.equals("endcase")) //结束流程
		{
				s_responsetext = EndCase();
		}
//ORIGINAL LINE: case "readCC":
		else if (method.equals("readCC"))
		{
				s_responsetext = ReadCC();

		}
		if (StringHelper.isNullOrEmpty(s_responsetext))
		{
			s_responsetext = "";
		}

		//组装ajax字符串格式,返回调用客户端
		try {
			wirteMsg(ContextHolderUtils.getResponse(), s_responsetext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public final String ReadCC()
	{
		String str = "";
		try
		{
			BP.WF.Dev2Interface.Node_CC_SetSta(getFK_Node(), getWorkID(), WebUser.getNo(), CCSta.CheckOver);
			str = "true";
		}
		catch (RuntimeException e)
		{
			str = "false";

		}
		return str;


	}
	/** 
	 保存 
	 
	 @return 
	*/
	public final String Send()
	{
		return "";
	}
	/** 
	 结束流程
	 
	 @return 
	*/
	public final String EndCase()
	{
		String flowId = getUTF8ToString("flowId");
		long workID = Long.parseLong(getUTF8ToString("workId"));

		return BP.WF.Dev2Interface.Flow_DoFlowOverByCoercion(flowId,this.getFK_Node(), workID, this.getFID(), "");
	}
	/** 
	 删除流程
	 
	*/
	public final String Delcase()
	{
		String returnstr = "";
		String flowId = getUTF8ToString("flowId");
		int fk_Node = Integer.parseInt(getUTF8ToString("nodeId"));
		long workID = Long.parseLong(getUTF8ToString("workId"));
		long fId = Long.parseLong(getUTF8ToString("fId"));
		Node currND = new  Node(fk_Node);

		return BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(flowId, workID, false);
	}
	/** 
	 签名流程
	 
	*/
	public final String Signcase()
	{
		String flowId = getUTF8ToString("flowId");
		int fk_Node = Integer.parseInt(getUTF8ToString("nodeId"));
		long workID = Long.parseLong(getUTF8ToString("workId"));
		long fId = Long.parseLong(getUTF8ToString("fId"));
		if (fId > 0)
		{
			workID = fId;
		}
		String yj = getUTF8ToString("yj");
		if (yj == null || yj.equals(""))
		{
			yj = "已阅";
		}

		BP.DA.Paras ps = new BP.DA.Paras();
		ps.Add("FK_Node", fk_Node);

		DataTable Sys_FrmSln = BP.DA.DBAccess.RunSQLReturnTable("select Sys_FrmSln.FK_MapData,Sys_FrmSln.KeyOfEn,Sys_FrmSln.IsSigan,Sys_MapAttr.MyDataType from Sys_FrmSln,Sys_MapAttr where Sys_FrmSln.UIIsEnable=1 and Sys_FrmSln.IsNotNull=1 and Sys_FrmSln.FK_Node=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "FK_Node and Sys_FrmSln.KeyOfEn=Sys_MapAttr.KeyOfEn and Sys_FrmSln.FK_MapData=Sys_MapAttr.FK_MapData", ps);
		boolean IsSign = false;
		for (DataRow DR : Sys_FrmSln.Rows)
		{
			String PTableField = DR.getValue("KeyOfEn").toString();
			String autotext = "";
			if (DR.getValue("IsSigan").toString().equals("1"))
			{
				autotext = WebUser.getNo();
			}
			else if (DR.getValue("MyDataType").toString().equals("6")) //时间字段
			{
				autotext = DataType.dateToStr(new Date(), "yyyy-MM-dd");
			}
			else if (DR.getValue("MyDataType").toString().equals("1")) //意见字段
			{
				PTableField = PTableField.toUpperCase();
				if (PTableField.endsWith("YJ") || PTableField.endsWith("YJ1") || PTableField.endsWith("YJ2") || PTableField.endsWith("YJ3") || PTableField.endsWith("YJ4") || PTableField.endsWith("YJ5") || PTableField.endsWith("YJ6") || PTableField.endsWith("YJ7") || PTableField.endsWith("YJ8") || PTableField.endsWith("YJ9"))
				{
					autotext = yj;
				}
				else
				{
					continue;
				}
			}
			else
			{
				continue;
			}
			String PTable = BP.DA.DBAccess.RunSQLReturnString("select PTable from Sys_MapData where No='" + DR.getValue("FK_MapData").toString() + "'");
			if (PTable != null)
			{
				int HavData = BP.DA.DBAccess.RunSQLReturnValInt("select count(oid) from " + PTable + " where oid=" + (new Long(workID)).toString());
				if (HavData == 0)
				{
					BP.DA.DBAccess.RunSQL("insert into " + PTable + "(oid," + PTableField + ") values(" + (new Long(workID)).toString() + ",'" + autotext + "')");
				}
				else
				{
					BP.DA.DBAccess.RunSQL("update " + PTable + " set " + PTableField + "='" + autotext + "' where oid=" + (new Long(workID)).toString());
				}
				IsSign = true;
			}
		}
		if (IsSign)
		{
			return "签名完毕";
		}
		else
		{
			return "没有签名可签";
		}
	}
	/** 
	 获取工具栏
	 
	 @return 
	*/
	private String GetAppToolBar()
	{
		int fk_Node = Integer.parseInt(getUTF8ToString("nodeId"));
		BtnLab btnLab = new BtnLab(fk_Node);
		StringBuilder toolsBar = new StringBuilder();
		toolsBar.append("{");

		//系统工具
		toolsBar.append("tools:[");
		//Send,Save,Thread,Return,CC,Shift,Del,EndFLow，RptTrack,HungUp"
		//发送
		if (btnLab.getSendEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Send',btnlabel:'" + btnLab.getSendLab() + "'");
			toolsBar.append("},");
		}
		//保存
		if (btnLab.getSaveEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Save',btnlabel:'" + btnLab.getSaveLab() + "'");
			toolsBar.append("},");
		}
		//子线程
		if (btnLab.getThreadEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Thread',btnlabel:'" + btnLab.getThreadLab() + "'");
			toolsBar.append("},");
		}

		//退回
		if (btnLab.getReturnEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Return',btnlabel:'" + btnLab.getReturnLab() + "'");
			toolsBar.append("},");
		}
		//抄送
		// if (btnLab.getCCRole() != 0)
		if (btnLab.getCCRole().getValue() != CCRole.UnCC.getValue())
		{
			toolsBar.append("{");
			toolsBar.append("no:'CC',btnlabel:'" + btnLab.getCCLab() + "'");
			toolsBar.append("},");
		}
		//移交
		if (btnLab.getShiftEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Shift',btnlabel:'" + btnLab.getShiftLab() + "'");
			toolsBar.append("},");
		}
		//删除 
		if (btnLab.getDeleteEnable() != 0)
		{
			toolsBar.append("{");
			toolsBar.append("no:'Del',btnlabel:'" + btnLab.getDeleteLab() + "'");
			toolsBar.append("},");
		}
		//结束 
		if (btnLab.getEndFlowEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'EndFLow',btnlabel:'" + btnLab.getEndFlowLab() + "'");
			toolsBar.append("},");
		}
		//打印 
		if (btnLab.getPrintDocEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Rpt',btnlabel:'" + btnLab.getPrintDocLab() + "'");
			toolsBar.append("},");
		}
		//轨迹
		if (btnLab.getTrackEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'Track',btnlabel:'" + btnLab.getTrackLab() + "'");
			toolsBar.append("},");
		}
		//挂起
		if (btnLab.getHungEnable())
		{
			toolsBar.append("{");
			toolsBar.append("no:'HungUp',btnlabel:'" + btnLab.getHungLab() + "'");
			toolsBar.append("},");
		}
		if (toolsBar.length() > 8)
		{
			toolsBar.deleteCharAt(toolsBar.length() - 1);
		}
		toolsBar.append("]");
		//扩展工具
		NodeToolbars extToolBars = new NodeToolbars();
		extToolBars.RetrieveByAttr(NodeToolbarAttr.FK_Node, fk_Node);
		toolsBar.append(",extTools:[");
		if (extToolBars.size() > 0)
		{
			for (NodeToolbar item : extToolBars.ToJavaList())
			{
				toolsBar.append("{OID:'" + item.getOID() + "',Title:'" + item.getTitle() + "',Target:'" + item.getTarget() + "',Url:'" + item.getUrl() + "'},");
			} 
			toolsBar.deleteCharAt(toolsBar.length() - 1);
		}
		toolsBar.append("]");
		toolsBar.append("}");
		return toolsBar.toString();
	}

	/** 
	 检查节点是否是启用接收人选择器
	 
	 @return 
	*/
	private String CheckAccepterOper()
	{
		int tempToNodeID = 0;
		//获取到当前节点
		Node _HisNode = new Node(this.getFK_Node());

		//如果到达的点为空 
		Nodes nds = _HisNode.getHisToNodes();
		if (nds.size() == 0)
		{
			//当前点是最后的一个节点，不能使用此功能
			return "end";
		}
		else if (nds.size() == 1)
		{
			Node toND = (Node)((nds.get(0) instanceof Node) ? nds.get(0) : null);
			tempToNodeID = toND.getNodeID();
		}
		else
		{
			for (Node mynd : nds.ToJavaList())
			{
				//if (mynd.HisDeliveryWay != DeliveryWay.BySelected)
				//    continue;

				GERpt _wk = _HisNode.getHisFlow().getHisGERpt();
				_wk.setOID(this.getWorkID());
				_wk.Retrieve();
				_wk.ResetDefaultVal();

				///#region 过滤不能到达的节点.
				Cond cond = new Cond();
				int i = cond.Retrieve(CondAttr.FK_Node, _HisNode.getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
				if (i == 0)
				{
					continue; // 没有设置方向条件，就让它跳过去。
				}
				cond.setWorkID(this.getWorkID());
				cond.en = _wk;
				if (cond.getIsPassed() == false)
				{
					continue;
				}
				///#endregion 过滤不能到达的节点.
				tempToNodeID = mynd.getNodeID();
			}
		}
		//不存在下一个节点,检查是否配置了有用户选择节点
		if (tempToNodeID == 0)
		{
			try
			{
				//检查必填项
				WorkNode workeNode = new WorkNode(this.getWorkID(), this.getFK_Node());
				workeNode.CheckFrmIsNotNull();
			}
			catch (RuntimeException ex)
			{
				return "error:" + ex.getMessage();
			}
			//按照用户选择计算
			if (_HisNode.getCondModel() == CondModel.ByUserSelected)
			{
				return "byuserselected";
			}
			return "notonode";
		}

		//判断到达的节点是否是按接受人选择
		Node toNode = new Node(tempToNodeID);
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySelected)
		{
			return "byselected";
		}
		return "nodata";
	}

	/** 
	 执行发送
	 
	 @return 
	*/
	private String SendCase()
	{
		String resultMsg = "";
		try
		{
			if (Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), WebUser.getNo()) == false)
			{
				resultMsg = "error|您好：" +  WebUser.getNo() + ", " + WebUser.getName() + "当前的工作已经被处理，或者您没有执行此工作的权限。";
			}
			SendReturnObjs returnObjs = Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID());
			resultMsg = returnObjs.ToMsgOfHtml();
			if (resultMsg.indexOf("@<a") > 0)
			{
				String kj = resultMsg.substring(0, resultMsg.indexOf("@<a"));
				resultMsg = resultMsg.substring(resultMsg.indexOf("@<a")) + "<br/><br/>" + kj;
			}
			//撤销单据
			int docindex = resultMsg.indexOf("@<img src='../../Img/FileType/doc.gif' />");
			if (docindex != -1)
			{
				String kj = resultMsg.substring(0, docindex);
				String kp = "";
				int nextdocindex = resultMsg.indexOf("@", docindex + 1);
				if (nextdocindex != -1)
				{
					kp = resultMsg.substring(nextdocindex);
				}
				resultMsg = kj + kp;
			}
			//撤销 撤销本次发送
			int UnSendindex = resultMsg.indexOf("@<a href='../../MyFlowInfo.jsp?DoType=UnSend");
			if (UnSendindex != -1)
			{
				String kj = resultMsg.substring(0, UnSendindex);
				String kp = "";
				int nextUnSendindex = resultMsg.indexOf("@", UnSendindex + 1);
				if (nextUnSendindex != -1)
				{
					kp = resultMsg.substring(nextUnSendindex);
				}
				resultMsg = kj + "<a href='javascript:UnSend();'><img src='../../Img/UnDo.gif' border=0/>撤销本次发送</a>" + kp;
			}

			resultMsg = resultMsg.replace("指定特定的处理人处理", "指定人员");
			resultMsg = resultMsg.replace("发手机短信提醒他(们)", "短信通知");
			resultMsg = resultMsg.replace("撤销本次发送", "撤销案件");
			resultMsg = resultMsg.replace("新建流程", "发起案件");
			resultMsg = resultMsg.replace("。", "");
			resultMsg = resultMsg.replace("，", "");

			resultMsg = resultMsg.replace("@下一步", "<br/><br/>&nbsp;&nbsp;&nbsp;下一步");
			resultMsg = "success|<br/>" + resultMsg.replace("@", "&nbsp;&nbsp;&nbsp;");

			///#region 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
//                这里有两种情况
//                 * 1，从中间的节点，通过批量处理，也就是说合并审批处理的情况，这种情况子流程需要执行到下一步。
//                   2，从流程已经完成，或者正在运行中，也就是说合并审批处理的情况. 
			try
			{
				//处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
				BP.WF.Glo.DealBuinessAfterSendWork(this.getFK_Flow(), this.getWorkID(), this.getDoFunc(), getWorkIDs(), this.getCFlowNo(), 0, null);
			}
			catch (RuntimeException ex)
			{
				resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
				return resultMsg;
			}
			///#endregion 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.

		}
		catch (RuntimeException ex)
		{
			resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
		}
		return resultMsg;
	}

	/** 
	 执行发送到指定节点
	 
	 @return 
	*/
	private String SendCaseToNode()
	{
		int ToNode = Integer.parseInt(getUTF8ToString("ToNode"));
		String resultMsg = "";
		try
		{
			if (Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), WebUser.getNo()) == false)
			{
				return resultMsg = "error|您好：" + WebUser.getNo() + ", " + WebUser.getName() + "当前的工作已经被处理，或者您没有执行此工作的权限。";
			}
			SendReturnObjs returnObjs = Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), ToNode, null);
			resultMsg = returnObjs.ToMsgOfHtml();
			if (resultMsg.indexOf("@<a") > 0)
			{
				String kj = resultMsg.substring(0, resultMsg.indexOf("@<a"));
				resultMsg = resultMsg.substring(resultMsg.indexOf("@<a")) + "<br/><br/>" + kj;
			}
			//撤销单据
			int docindex = resultMsg.indexOf("@<img src='../../Img/FileType/doc.gif' />");
			if (docindex != -1)
			{
				String kj = resultMsg.substring(0, docindex);
				String kp = "";
				int nextdocindex = resultMsg.indexOf("@", docindex + 1);
				if (nextdocindex != -1)
				{
					kp = resultMsg.substring(nextdocindex);
				}
				resultMsg = kj + kp;
			}
			//撤销 撤销本次发送
			int UnSendindex = resultMsg.indexOf("@<a href='MyFlowInfo.jsp?DoType=UnSend");
			if (UnSendindex != -1)
			{
				String kj = resultMsg.substring(0, UnSendindex);
				String kp = "";
				int nextUnSendindex = resultMsg.indexOf("@", UnSendindex + 1);
				if (nextUnSendindex != -1)
				{
					kp = resultMsg.substring(nextUnSendindex);
				}
				resultMsg = kj + "<a href='javascript:UnSend();'><img src='../../Img/UnDo.gif' border=0/>撤销本次发送</a>" + kp;
			}

			resultMsg = resultMsg.replace("指定特定的处理人处理", "指定人员");
			resultMsg = resultMsg.replace("发手机短信提醒他(们)", "短信通知");
			resultMsg = resultMsg.replace("撤销本次发送", "撤销案件");
			resultMsg = resultMsg.replace("新建流程", "发起案件");
			resultMsg = resultMsg.replace("。", "");
			resultMsg = resultMsg.replace("，", "");

			resultMsg = resultMsg.replace("@下一步", "<br/><br/>&nbsp;&nbsp;&nbsp;下一步");
			resultMsg = "success|<br/>" + resultMsg.replace("@", "&nbsp;&nbsp;&nbsp;");

			///#region 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
//                这里有两种情况
//                 * 1，从中间的节点，通过批量处理，也就是说合并审批处理的情况，这种情况子流程需要执行到下一步。
//                   2，从流程已经完成，或者正在运行中，也就是说合并审批处理的情况. 
			try
			{
				//处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
				BP.WF.Glo.DealBuinessAfterSendWork(this.getFK_Flow(), this.getWorkID(), this.getDoFunc(), getWorkIDs(), this.getCFlowNo(), 0, null);
			}
			catch (RuntimeException ex)
			{
				resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
				return resultMsg;
			}
			///#endregion 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.

		}
		catch (RuntimeException ex)
		{
			resultMsg = "sysError|" + ex.getMessage().replace("@", "<br/>");
		}
		return resultMsg;
	}

	/** 
	 撤销发送
	 
	 @return 
	*/
	private String UnSendCase()
	{
		try
		{
			String FK_Flow = getUTF8ToString("FK_Flow");
			String WorkID = getUTF8ToString("WorkID");
			String str1 = BP.WF.Dev2Interface.Flow_DoUnSend(FK_Flow, Long.parseLong(WorkID));
			return "true";
		}
		catch (RuntimeException ex)
		{
			return "{message:'执行撤消失败，失败信息" + ex.getMessage() + "'}";
		}
	}
	/** 
	 获取表单树
	 
	 @return 
	*/
	private FlowFormTrees appFlowFormTree = new FlowFormTrees();
	private String GetFlowFormTree()
	{
		String flowId = getUTF8ToString("flowId");
		String nodeId = getUTF8ToString("nodeId");

		//add root
		FlowFormTree root = new FlowFormTree();
		root.setNo ("01");
		root.setParentNo("0");
		root.setName("目录");
		root.setNodeType("root");
		appFlowFormTree.clear();
		appFlowFormTree.AddEntity(root);

		// region 添加表单及文件夹

		//节点表单
		FrmNodes frmNodes = new FrmNodes();
		QueryObject qo = new QueryObject(frmNodes);
		qo.AddWhere(FrmNodeAttr.FK_Node, nodeId);
		qo.addAnd();
		qo.AddWhere(FrmNodeAttr.FK_Flow, flowId);
		qo.addOrderBy(FrmNodeAttr.Idx);
		qo.DoQuery();
		//文件夹
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Name);
		//所有表单集合
		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.AppType, AppType.Application.getValue());
		for (FrmNode frmNode : frmNodes.ToJavaList())
		{
			for (MapData md : mds.ToJavaList())
			{
				if (frmNode.getFK_Frm() != md.getNo())
				{
					continue;
				}

				for (SysFormTree formTree : formTrees.ToJavaList())
				{
					if (md.getFK_FormTree() != formTree.getNo())
					{
						continue;
					}

					if (!appFlowFormTree.Contains("No", formTree.getNo()))
					{
						FlowFormTree nodeFolder = new FlowFormTree();
						nodeFolder.setNo(formTree.getNo());
						nodeFolder.setParentNo(root.getNo());
						nodeFolder.setName(formTree.getName());
						nodeFolder.setNodeType("folder");
						appFlowFormTree.AddEntity(nodeFolder);
					}
				}
				//检查必填项
				boolean IsNotNull = false;
				FrmFields formFields = new FrmFields();
				QueryObject obj = new QueryObject(formFields);
				obj.AddWhere(FrmFieldAttr.FK_Node, nodeId);
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.FK_MapData, md.getNo());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.IsNotNull, "1");
				obj.DoQuery();
				if (formFields != null && formFields.size() > 0)
				{
					IsNotNull = true;
				}

				FlowFormTree nodeForm = new FlowFormTree();
				nodeForm.setNo(md.getNo());
				nodeForm.setParentNo(md.getFK_FormTree());
				nodeForm.setName(md.getName());
				nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
				appFlowFormTree.AddEntity(nodeForm);
			}
		}
		// endregion
		//扩展工具，显示位置为表单树类型
		NodeToolbars extToolBars = new NodeToolbars();
		QueryObject info = new QueryObject(extToolBars);
		info.AddWhere(NodeToolbarAttr.FK_Node, nodeId);
		info.addAnd();
		info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Tree.getValue());
		info.DoQuery();

		for (NodeToolbar item : extToolBars.ToJavaList())
		{
			String url = "";
			if (StringHelper.isNullOrEmpty(item.getUrl()))
			{
				continue;
			}

			url = item.getUrl();

			FlowFormTree formTree = new FlowFormTree();
			formTree.setNo(String.valueOf(item.getOID()));
			formTree.setParentNo("01");
			formTree.setName(item.getTitle());
			formTree.setNodeType("tools|0");
			if (!StringHelper.isNullOrEmpty(item.getTarget()) && item.getTarget().toUpperCase().equals("_BLANK"))
			{
				formTree.setNodeType("tools|1");
			}

			formTree.setUrl(url);
			appFlowFormTree.AddEntity(formTree);
		}
		TansEntitiesToGenerTree(appFlowFormTree, root.getNo(), "");
		return appendMenus.toString();
	}

	//private string GetFlowFormTree_old()
	//{
	//    string flowId = getUTF8ToString("flowId");
	//    string nodeId = getUTF8ToString("nodeId");
	//    //流程表单树父节点
	//    BP.WF.Template.FlowFormTrees flowFormTree = new BP.WF.Template.FlowFormTrees();
	//    QueryObject obj = new QueryObject(flowFormTree);
	//    obj.AddWhere(FlowFormTreeAttr.FK_Flow, flowId);
	//    obj.addAnd();
	//    obj.AddWhere(FlowFormTreeAttr.ParentNo, "01");
	//    obj.addOrderBy(FlowFormTreeAttr.Name);
	//    obj.DoQuery();
	//    //如果为空，则初始数据
	//    if (flowFormTree == null || flowFormTree.Count == 0)
	//    {
	//        InitFlowFormTree(flowId);

	//        //重新获取
	//        flowFormTree = new BP.WF.Template.FlowFormTrees();
	//        obj = new QueryObject(flowFormTree);
	//        obj.AddWhere(FlowFormTreeAttr.FK_Flow, flowId);
	//        obj.addAnd();
	//        obj.AddWhere(FlowFormTreeAttr.ParentNo, "01");
	//        obj.addOrderBy(FlowFormTreeAttr.Idx);
	//        obj.DoQuery();
	//    }
	//    //流程表单
	//    FlowForms flowForms = new FlowForms();
	//    flowForms.RetrieveByAttr(FlowFormAttr.FK_Flow, flowId);
	//    //流程节点表单
	//    NodeForms nodeForms = new NodeForms();
	//    nodeForms.RetrieveByAttr(NodeFormAttr.FK_Node, nodeId);
	//    //如果节点表单不存在
	//    if (nodeForms == null || nodeForms.Count == 0)
	//    {
	//        InitNodeForms(flowId, nodeId);
	//        //重新查询
	//        nodeForms = new NodeForms();
	//        nodeForms.RetrieveByAttr(NodeFormAttr.FK_Node, nodeId);
	//    }
	//    //add root
	//    BP.WF.Template.FlowFormTree root = new BP.WF.Template.FlowFormTree();
	//    root.No = "01";
	//    root.ParentNo = "0";
	//    root.Name = "目录";
	//    root.NodeType = "root";
	//    appFlowFormTree.Clear();
	//    appFlowFormTree.AddEntity(root);
	//    //添加json
	//    foreach (BP.WF.Template.FlowFormTree item in flowFormTree)
	//    {
	//        item.NodeType = "folder";
	//        appFlowFormTree.AddEntity(item);
	//        foreach (FlowForm flowForm in flowForms)
	//        {
	//            if (item.No != flowForm.FK_FlowFormTree)
	//                continue;

	//            foreach (NodeForm nodeForm in nodeForms)
	//            {
	//                if (nodeForm.FK_SysForm == flowForm.FK_SysForm)
	//                {
	//                    SysForm sysForm = new SysForm(nodeForm.FK_SysForm);
	//                    BP.WF.Template.FlowFormTree formTree = new BP.WF.Template.FlowFormTree();
	//                    formTree.No = nodeForm.FK_SysForm;
	//                    formTree.ParentNo = item.No;
	//                    formTree.Name = sysForm.Name;
	//                    formTree.NodeType = "form";
	//                    appFlowFormTree.AddEntity(formTree);
	//                }
	//            }
	//        }
	//        //添加子节点
	//        GetChildTreeNode(flowId, item.No, flowForms, nodeForms);
	//    }
	//    //扩展工具
	//    NodeToolbars extToolBars = new NodeToolbars();
	//    extToolBars.RetrieveByAttr(NodeToolbarAttr.FK_Node, nodeId);

	//    foreach (NodeToolbar item in extToolBars)
	//    {
	//        string url = "";
	//        if (string.IsNullOrEmpty(item.Url))
	//            continue;

	//        url = item.Url;

	//        BP.WF.Template.FlowFormTree formTree = new BP.WF.Template.FlowFormTree();
	//        formTree.No = item.OID.ToString();
	//        formTree.ParentNo = "01";
	//        formTree.Name = item.Title;
	//        formTree.NodeType = "tools";
	//        formTree.Url = url;
	//        appFlowFormTree.AddEntity(formTree);
	//    }
	//    TansEntitiesToGenerTree(appFlowFormTree, root.No, "");
	//    return appendMenus.ToString();
	//}

	//private void GetChildTreeNode(string flowId, string parentNo, FlowForms flowForms, NodeForms nodeForms)
	//{
	//    //流程表单树父节点
	//    BP.WF.Template.FlowFormTrees flowFormTree = new BP.WF.Template.FlowFormTrees();
	//    QueryObject obj = new QueryObject(flowFormTree);
	//    obj.AddWhere(FlowFormTreeAttr.FK_Flow, flowId);
	//    obj.addAnd();
	//    obj.AddWhere(FlowFormTreeAttr.ParentNo, parentNo);
	//    obj.addOrderBy(FlowFormTreeAttr.Name);
	//    obj.DoQuery();

	//    foreach (BP.WF.Template.FlowFormTree item in flowFormTree)
	//    {
	//        item.NodeType = "folder";
	//        appFlowFormTree.AddEntity(item);
	//        foreach (FlowForm flowForm in flowForms)
	//        {
	//            if (item.No != flowForm.FK_FlowFormTree)
	//                continue;

	//            foreach (NodeForm nodeForm in nodeForms)
	//            {
	//                if (nodeForm.FK_SysForm == flowForm.FK_SysForm)
	//                {
	//                    SysForm sysForm = new SysForm(nodeForm.FK_SysForm);
	//                    BP.WF.Template.FlowFormTree formTree = new BP.WF.Template.FlowFormTree();
	//                    formTree.No = nodeForm.FK_SysForm;
	//                    formTree.ParentNo = item.No;
	//                    formTree.Name = sysForm.Name;
	//                    formTree.NodeType = "form";
	//                    appFlowFormTree.AddEntity(formTree);
	//                }
	//            }
	//        }
	//        //添加子节点
	//        GetChildTreeNode(flowId, item.No, flowForms, nodeForms);
	//    }
	//}

		///#region 初始节点表单树
	/** 
	 初始节点表单树
	 
	//private void InitFlowFormTree(string flowId)
	//{
	//    BP.WF.Flow flow = new Flow(flowId);
	//    //获取模版
	//    SysFormTree sysFormTree = new SysFormTree();
	//    sysFormTree.RetrieveByAttr(SysFormTreeAttr.Name, flow.Name);
	*/

	//    if (sysFormTree != null)
	//    {
	//        //添加到流程
	//        BP.WF.Template.FlowFormTree flowFormTree = new BP.WF.Template.FlowFormTree();
	//        QueryObject objFlowFormTree = new QueryObject(flowFormTree);
	//        objFlowFormTree.AddWhere("No", "01");
	//        objFlowFormTree.DoQuery();

	//        if (flowFormTree == null || flowFormTree.Name == "")
	//        {
	//            flowFormTree = new BP.WF.Template.FlowFormTree();
	//            flowFormTree.No = "01";
	//            flowFormTree.Name = "目录";
	//            flowFormTree.ParentNo = "0";
	//            flowFormTree.Idx = 0;
	//            flowFormTree.IsDir = true;
	//            flowFormTree.DirectInsert();
	//        }
	//        string subNo = flowFormTree.DoCreateSubNode().No;
	//        flowFormTree = new BP.WF.Template.FlowFormTree(subNo);
	//        flowFormTree.Name = flow.Name;
	//        flowFormTree.FK_Flow = flowId;
	//        flowFormTree.Update();
	//        //添加子级
	//        InitChildNode(flowId, sysFormTree.No, subNo);
	//    }
	//}

	//private void InitNodeForms(string flowId, string nodeId)
	//{
	//    FlowForms flowForm = new FlowForms(flowId);

	//    NodeForm fnodeForm = new NodeForm();
	//    fnodeForm.Delete(NodeFormAttr.FK_Node, nodeId);
	//    foreach (FlowForm item in flowForm)
	//    {
	//        fnodeForm = new NodeForm();
	//        fnodeForm.FK_Node = nodeId;
	//        fnodeForm.FK_SysForm = item.FK_SysForm;
	//        fnodeForm.Insert();
	//    }
	//}

	//private void InitChildNode(string flowId, string parentNo, string wfParetNo)
	//{
	//    //获取模版
	//    SysFormTrees formTrees = new SysFormTrees();
	//    QueryObject objInfo = new QueryObject(formTrees);
	//    objInfo.AddWhere(SysFormTreeAttr.ParentNo, parentNo);
	//    objInfo.addOrderBy(SysFormTreeAttr.Name);
	//    objInfo.DoQuery();

	//    BP.WF.Template.FlowFormTree pFlowFormTree = new BP.WF.Template.FlowFormTree(wfParetNo);

	//    foreach (SysFormTree item in formTrees)
	//    {
	//        string subNo = pFlowFormTree.DoCreateSubNode().No;
	//        BP.WF.Template.FlowFormTree flowFormTree = new BP.WF.Template.FlowFormTree(subNo);
	//        flowFormTree.Name = item.Name;
	//        flowFormTree.FK_Flow = flowId;
	//        flowFormTree.Update();
	//        InitChildNode(flowId, item.No, subNo);
	//    }
	//    //表单树表单
	//    SysForm sysForm = new SysForm();
	//    DataTable dt_Forms = sysForm.RunSQLReturnTable("SELECT No,Name FROM Sys_MapData WHERE FK_FormTree='" + parentNo + "' ORDER BY Name");
	//    //流程表单
	//    foreach (DataRow row in dt_Forms.Rows)
	//    {
	//        FlowForm flowForm = new FlowForm();
	//        flowForm.FK_Flow = flowId;
	//        flowForm.FK_SysForm = row["No"].ToString();
	//        flowForm.FK_FlowFormTree = wfParetNo;
	//        flowForm.Insert();
	//    }
	//}
		///#endregion

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
		EntityMultiTree root = (EntityMultiTree)((tempVar instanceof EntityMultiTree) ? tempVar : null);
		if (root == null)
		{
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");

		//attributes
		FlowFormTree formTree = (FlowFormTree)((root instanceof FlowFormTree) ? root : null);
		if (formTree != null)
		{
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"Url\":\"" + url + "\"}");
		}
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityMultiTree parentEn, Entities ens, String checkIds)
	{
		appendMenus.append(appendMenuSb);
		appendMenuSb = new StringBuilder();

		appendMenuSb.append("[");
		for (Entity entity : ens.ToJavaListEn())
		{
			EntityMultiTree item = (EntityMultiTree) entity;
			if (item.getParentNo() != parentEn.getNo())
			{
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ","))
			{
				appendMenuSb.append("{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":true");
			}
			else
			{
				appendMenuSb.append("{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":false");
			}


			//attributes
			FlowFormTree formTree = (FlowFormTree)((item instanceof FlowFormTree) ? item : null);
			if (formTree != null)
			{
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"Url\":\"" + url + "\"}");
				//图标
				if (formTree.getNodeType().equals("form|0"))
				{
					ico = "form0";
				}
				if (formTree.getNodeType().equals("form|1"))
				{
					ico = "form1";
				}
				if (formTree.getNodeType().contains("tools"))
				{
					ico = "icon-4";
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
		if (appendMenuSb.length() > 1)
		{
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb.delete(0, appendMenuSb.length()-1);
	}
}
