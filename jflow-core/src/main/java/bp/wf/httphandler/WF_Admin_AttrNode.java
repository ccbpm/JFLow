package bp.wf.httphandler;

import bp.wf.*;
import bp.web.*;
import bp.sys.*;
import bp.tools.StringHelper;
import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.en.*;
import bp.wf.template.*;
import bp.wf.xml.*;
import bp.wf.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

public class WF_Admin_AttrNode extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_AttrNode()
	{
	}


		///事件基类.
	/** 
	 事件类型
	*/
	public final String getShowType()
	{
		if (this.getFK_Node() != 0)
		{
			return "Node";
		}

		if (this.getFK_Node() == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false && this.getFK_Flow().length() >= 3)
		{
			return "Flow";
		}

		if (this.getFK_Node() == 0 && DataType.IsNullOrEmpty(this.getFK_MapData()) == false)
		{
			return "Frm";
		}

		return "Node";
	}
	/** 
	 获得该节点下已经绑定该类型的实体.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ActionDtl_Init() throws Exception
	{
		//业务单元集合.
		DataTable dtBuess = new DataTable();
		dtBuess.Columns.Add("No", String.class);
		dtBuess.Columns.Add("Name", String.class);
		dtBuess.TableName = "BuessUnits";
		ArrayList<BuessUnitBase> al =ClassFactory.GetObjects("bp.sys.BuessUnitBase");
		for (BuessUnitBase en : al)
		{
			DataRow dr = dtBuess.NewRow();
			dr.setValue("No", en.toString());
			dr.setValue("Name", en.getTitle());
			dtBuess.Rows.add(dr);
		}

		return bp.tools.Json.ToJson(dtBuess);
	}

		/// 事件基类.


		///  公文维护
	/** 
	 选择一个模版
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SelectDocTemp_Save() throws Exception
	{
		String docTempNo = this.GetRequestVal("no");

		DocTemplate docTemplate = new DocTemplate(docTempNo);

		if ((new File(docTemplate.getFilePath())).isFile() == false)
		{
			return "err@选择的模版文件不存在.";
		}

		//获得模版的流.
		byte[] bytes = DataType.ConvertFileToByte(docTemplate.getFilePath());

		//保存到数据库里.
		Flow fl = new Flow(this.getFK_Flow());
		DBAccess.SaveBytesToDB(bytes, fl.getPTable(), "OID", String.valueOf(this.getWorkID()), "WordFile");

		
		return "模板导入成功.";
	}

	public final String FlowDocInit() throws Exception
	{
		Hashtable<String, Object>  msg = new Hashtable<String, Object>();
		
		msg.put("Success", true);

		try
		{
			int nodeId = Integer.parseInt(this.GetRequestVal("nodeId"));
			int workId = Integer.parseInt(this.GetRequestVal("workId"));
			String flowNo = this.GetRequestVal("fk_flow");
			String tableName = "ND" + Integer.parseInt(flowNo) + "Rpt";

			String str = "WordFile";
			if (DBAccess.IsExitsTableCol(tableName, str) == false)
			{
				/*如果没有此列，就自动创建此列.*/
				String sql = "ALTER TABLE " + tableName + " ADD  " + str + " image ";

				if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
				{
					sql = "ALTER TABLE " + tableName + " ADD  " + str + " image ";
				}

				DBAccess.RunSQL(sql);
			}

			byte[] bytes = DBAccess.GetByteFromDB(tableName, "OID", String.valueOf(workId), "WordFile");
			Node node = new Node(nodeId);

			if (!node.getIsStartNode())
			{
				if (bytes == null)
				{
					
					msg.put("Message", "{\"IsStartNode\":0,\"IsExistFlowData\":0,\"IsExistTempData\":0}");
				}
				else
				{
					msg.put("Message","{\"IsStartNode\":0,\"IsExistFlowData\":1,\"IsExistTempData\":0}");
				}
			}
			else //开始节点
			{
				DocTemplates dts = new DocTemplates();
				int count = dts.Retrieve(DocTemplateAttr.FK_Node, nodeId);

				if (bytes == null)
				{
					if (count == 0)
					{
						msg.put("Message","{\"IsStartNode\":1,\"IsExistFlowData\":0,\"IsExistTempData\":0}");
						msg.put("Data",null);
					}
					else
					{
						msg.put("Message","{\"IsStartNode\":1,\"IsExistFlowData\":0,\"IsExistTempData\":" + count + "}");
						msg.put("Data",dts.ToJson());
					}
				}
				else
				{
					if (count == 0)
					{
						msg.put("Message","{\"IsStartNode\":1,\"IsExistFlowData\":1,\"IsExistTempData\":0}");
						msg.put("Data",null);
					}
					else
					{
						msg.put("Message","{\"IsStartNode\":1,\"IsExistFlowData\":1,\"IsExistTempData\":" + count + "}");
						msg.put("Data",dts.ToJson());
					}
				}
			}
		}
		catch (RuntimeException ex)
		{
			msg.put("Success", false);
			msg.put("Message",ex.getMessage());
		}

		return bp.tools.Json.ToJson(msg);
	}

	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DocTemp_Del() throws Exception
	{
		int no = Integer.parseInt(this.GetRequestVal("no"));

		bp.wf.template.DocTemplate dt = new DocTemplate();
		dt.Retrieve(DocTemplateAttr.No, no);
		dt.Delete();

		return "操作成功";
	}
	/** 
	 模版文件上传
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DocTemp_Upload() throws Exception
	{
		Node nd = new Node(this.getFK_Node());
		HttpServletRequest request = getRequest();
		String contentType = request.getContentType();
		String fileName="";
		String fileFullPath="";
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
			fileName = CommonFileUtils.getOriginalFilename(request, "file");
			String path = SystemConfig.getPathOfDataUser() + "DocTemplate/" + nd.getFK_Flow();
			fileFullPath = path + "/" + fileName;

			//上传文件.
			if ((new File(path)).isDirectory() == false)
			{
				(new File(path)).mkdirs();
			}
			
			try {
				CommonFileUtils.upload(request, "file", new File(fileFullPath));
			} catch (Exception e) {
				e.printStackTrace();
				return "err@执行失败";
			}
		}
		
		

		//插入模版.
		DocTemplate dt = new DocTemplate();
		dt.setFK_Node(getFK_Node());
		dt.setNo(DBAccess.GenerGUID());
		dt.setName( fileName);
		dt.setFilePath(fileFullPath); //路径
		dt.setFK_Node(this.getFK_Node());
		dt.Insert();

		//保存文件.
		DBAccess.SaveFileToDB(fileFullPath, dt.getEnMap().getPhysicsTable(), "No", dt.getNo(), "FileTemplate");
		return dt.ToJson();
	}

		///


		/// 单据模版维护
	/** 
	 @李国文.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Bill_Save() throws Exception
	{
		BillTemplate bt = new BillTemplate();
		HttpServletRequest request = getRequest();
		// 上传附件
		String fileName = CommonFileUtils.getOriginalFilename(request,"bill");
		String filepath = SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + fileName;
		filepath = filepath.replace("\\", "/");
		
		try{
			CommonFileUtils.upload(request, "bill",new File(filepath));
		}catch(Exception e){
			e.printStackTrace();
			return "err@执行失败";		
		}

		bt.setNodeID(this.getFK_Node());
		bt.setFK_MapData(this.getFK_MapData());
		bt.setNo(request.getParameter("TB_No"));
		if (StringHelper.isNullOrEmpty(bt.getNo())) {
			bt.setNo(String.valueOf(DBAccess.GenerOID("Template")));
		}
		bt.setName(request.getParameter("TB_Name"));
		bt.setTempFilePath(fileName);
		
		bt.setHisBillFileType(Integer.parseInt(request.getParameter("DDL_BillFileType")));
		bt.setBillOpenModel(Integer.parseInt(request.getParameter("DDL_BillOpenModel")));
		bt.setQRModel(Integer.parseInt(request.getParameter("DDL_BillOpenModel")));
		
		//模版类型.rtf / VSTOForWord / VSTOForExcel .
        if (fileName.contains(".doc"))
            bt.setTemplateFileModel(TemplateFileModel.VSTOForWord);

        if (fileName.contains(".xls"))
            bt.setTemplateFileModel(TemplateFileModel.VSTOForExcel);

        if (fileName.contains(".rtf"))
            bt.setTemplateFileModel(TemplateFileModel.RTF);

        bt.Save();

        bt.SaveFileToDB("DBFile", filepath); //把文件保存到数据库里. 

		return "保存成功";
	}
	
	private HttpServletResponse response;
	public void setHttpServletResp(HttpServletResponse response) {

		this.response = response;
	}
	/** 
	 下载文件.
	*/
	public final void Bill_Download() throws Exception
	{
		BillTemplate en = new BillTemplate(this.getNo());
		String MyFilePath = en.getTempFilePath();
		String fileName = MyFilePath.substring(MyFilePath.lastIndexOf("/") + 1);

		// 设置响应头和客户端保存文件名
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

		InputStream inputStream = new FileInputStream(MyFilePath);

		OutputStream os = response.getOutputStream();

		long downloadedLength = 0l;
		byte[] b = new byte[2048];
		int length;
		while ((length = inputStream.read(b)) > 0) {
			os.write(b, 0, length);
			downloadedLength += b.length;
		}

		os.close();
		inputStream.close();
		
	}
		///


		/// 节点消息
	public final String PushMsg_Init() throws Exception
	{
		//增加上单据模版集合.
		int nodeID = this.GetRequestValInt("FK_Node");
		bp.wf.template.PushMsgs ens = new bp.wf.template.PushMsgs(nodeID);
		return ens.ToJson();
	}
	public final String PushMsg_Save() throws Exception
	{
		bp.wf.template.PushMsg msg = new bp.wf.template.PushMsg();
		msg.setMyPK(this.getMyPK());
		msg.RetrieveFromDBSources();

		msg.setFK_Event(this.getFK_Event());
		msg.setFK_Node(this.getFK_Node());

		bp.wf.Node nd = new bp.wf.Node(this.getFK_Node());
		bp.wf.Nodes nds = new bp.wf.Nodes(nd.getFK_Flow());
		msg.setFK_Flow(nd.getFK_Flow());

		//推送方式。
		msg.setSMSPushWay(Integer.parseInt(this.GetRequestVal("RB_SMS").replace("RB_SMS_", "")));

		//表单字段作为接收人.
		msg.setSMSField(this.GetRequestVal("DDL_SMS_Fields"));


			///其他节点的处理人方式（求选择的节点）
		String nodesOfSMS = "";
		for (bp.wf.Node mynd : nds.ToJavaList())
		{
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				if (key.contains("CB_SMS_" + mynd.getNodeID()) && nodesOfSMS.contains(mynd.getNodeID() + "") == false)
				{
					nodesOfSMS += mynd.getNodeID() + ",";
				}


			}
		}
		msg.setSMSNodes(nodesOfSMS);

			/// 其他节点的处理人方式（求选择的节点）

		//按照SQL
		msg.setBySQL(this.GetRequestVal("TB_SQL"));

		//发给指定的人员
		msg.setByEmps(this.GetRequestVal("TB_Emps"));

		//短消息发送设备
		msg.setSMSPushModel(this.GetRequestVal("PushModel"));

		//邮件标题
		msg.setMailTitle_Real(this.GetRequestVal("TB_title"));

		//短信内容模版.
		msg.setSMSDoc_Real(this.GetRequestVal("TB_SMS"));

		//节点预警
		if (this.getFK_Event().equals(bp.sys.EventListNode.NodeWarning))
		{
			int noticeType =Integer.parseInt(this.GetRequestVal("RB_NoticeType").replace("RB_NoticeType", ""));
			msg.SetPara("NoticeType", noticeType);
			int hour = this.GetRequestValInt("TB_NoticeHour");
			msg.SetPara("NoticeHour", hour);
		}

		//节点逾期
		if (this.getFK_Event().equals(bp.sys.EventListNode.NodeOverDue))
		{
			int noticeType = Integer.parseInt(this.GetRequestVal("RB_NoticeType").replace("RB_NoticeType", ""));
			msg.SetPara("NoticeType", noticeType);
			int day = this.GetRequestValInt("TB_NoticeDay");
			msg.SetPara("NoticeDay", day);
		}

		//保存.
		if (DataType.IsNullOrEmpty(msg.getMyPK()) == true)
		{
			msg.setMyPK(DBAccess.GenerGUID());
			msg.Insert();
		}
		else
		{
			msg.Update();
		}

		return "保存成功..";
	}

	public final String PushMsgEntity_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//字段下拉框.

		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + this.getFK_Node(), "LGType", 0, "MyDataType", 1);
		ds.Tables.add(attrs.ToDataTableField("FrmFields"));

		//节点 
		//TODO 数据太多优化一下
		bp.wf.Node nd = new bp.wf.Node(this.getFK_Node());
		bp.wf.Nodes nds = new bp.wf.Nodes(nd.getFK_Flow());
		ds.Tables.add(nds.ToDataTableField("Nodes"));

		//mypk
		bp.wf.template.PushMsg msg = new bp.wf.template.PushMsg();
		msg.setMyPK(this.getMyPK());
		msg.RetrieveFromDBSources();
		ds.Tables.add(msg.ToDataTableField("PushMsgEntity"));

		return bp.tools.Json.ToJson(ds);
	}



		///


		///表单模式
	/** 
	 表单模式
	 
	 @return 
	 * @throws Exception 
	*/
	public final String NodeFromWorkModel_Init() throws Exception
	{
		//数据容器.
		DataSet ds = new DataSet();

		// 当前节点信息.
		Node nd = new Node(this.getFK_Node());

		nd.WorkID=this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		nd.setNodeFrmID(nd.getNodeFrmID());
		// nd.FormUrl = nd.FormUrl;

		DataTable mydt = nd.ToDataTableField("WF_Node");
		ds.Tables.add(mydt);

		BtnLab btn = new BtnLab(this.getFK_Node());
		DataTable dtBtn = btn.ToDataTableField("WF_BtnLab");
		ds.Tables.add(dtBtn);

		//节点s
		Nodes nds = new Nodes(nd.getFK_Flow());

		//节点s
		ds.Tables.add(nds.ToDataTableField("Nodes"));

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 表单模式
	 
	 @return 
	 * @throws Exception 
	*/
	public final String NodeFromWorkModel_Save() throws Exception
	{
		Node nd = new Node(this.getFK_Node());

		MapData md = new MapData("ND" + this.getFK_Node());

		//用户选择的表单类型.
		String selectFModel = this.GetValFromFrmByKey("FrmS");

		//使用ccbpm内置的节点表单
		if (selectFModel.equals("DefFrm"))
		{
			//呈现风格
			String frmModel = this.GetValFromFrmByKey("RB_Frm");
			if (frmModel.equals("0"))
			{
				//自由表单
				nd.setFormType(NodeFormType.FreeForm);
				nd.DirectUpdate();

				md.setHisFrmType(FrmType.FreeFrm);
				md.Update();
			}
			else
			{
				//傻瓜表单
				nd.setFormType(NodeFormType.FoolForm);
				nd.DirectUpdate();

				md.setHisFrmType(FrmType.FoolForm);
				md.Update();
			}
			//表单引用
			String refFrm = this.GetValFromFrmByKey("RefFrm");
			//当前节点表单
			if (refFrm.equals("0"))
			{
				nd.setNodeFrmID("");
				nd.DirectUpdate();
			}
			//其他节点表单
			if (refFrm.equals("1"))
			{
				nd.setNodeFrmID("ND" + this.GetValFromFrmByKey("DDL_Frm"));
				nd.DirectUpdate();
			}
		}

		//使用傻瓜轨迹表单模式.
		if (selectFModel.equals("FoolTruck"))
		{
			nd.setFormType(NodeFormType.FoolTruck);
			nd.DirectUpdate();

			md.setHisFrmType(FrmType.FoolForm); //同时更新表单表住表.
			md.Update();
		}

		//使用嵌入式表单
		if (selectFModel.equals("SelfForm"))
		{
			nd.setFormType(NodeFormType.SelfForm);
			nd.setFormUrl(this.GetValFromFrmByKey("TB_CustomURL"));
			nd.DirectUpdate();

			md.setHisFrmType(FrmType.Url); //同时更新表单表住表.
			md.setUrl(this.GetValFromFrmByKey("TB_CustomURL"));
			md.Update();

		}
		//使用SDK表单
		if (selectFModel.equals("SDKForm"))
		{
			nd.setFormType(NodeFormType.SDKForm);
			nd.setFormUrl(this.GetValFromFrmByKey("TB_FormURL"));
			nd.DirectUpdate();

			md.setHisFrmType(FrmType.Url);
			md.setUrl(this.GetValFromFrmByKey("TB_FormURL"));
			md.Update();

		}
		//绑定多表单
		if (selectFModel.equals("SheetTree"))
		{

			String sheetTreeModel = this.GetValFromFrmByKey("SheetTreeModel");

			if (sheetTreeModel.equals("0"))
			{
				nd.setFormType(NodeFormType.SheetTree);
				nd.DirectUpdate();

				md.setHisFrmType(FrmType.FreeFrm); //同时更新表单表住表.
				md.Update();
			}
			else
			{
				nd.setFormType(NodeFormType.DisableIt);
				nd.DirectUpdate();

				md.setHisFrmType(FrmType.FreeFrm); //同时更新表单表住表.
				md.Update();
			}
		}

		return "保存成功...";
	}

		/// 表单模式


		///节点属性（列表）的操作
	/** 
	 初始化节点属性列表.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String NodeAttrs_Init() throws Exception
	{
		String strFlowId = GetRequestVal("FK_Flow");
		if (DataType.IsNullOrEmpty(strFlowId))
		{
			return "err@参数错误！";
		}
		Nodes nodes = new Nodes();
		nodes.Retrieve("FK_Flow", strFlowId);
		//因直接使用nodes.ToJson()无法获取某些字段（e.g.HisFormTypeText,原因：Node没有自己的Attr类）
		//故此处手动创建前台所需的DataTable
		DataTable dt = new DataTable();
		dt.Columns.Add("NodeID"); //节点ID
		dt.Columns.Add("Name"); //节点名称
		dt.Columns.Add("HisFormType"); //表单方案
		dt.Columns.Add("HisFormTypeText");
		dt.Columns.Add("HisRunModel"); //节点类型
		dt.Columns.Add("HisRunModelT");

		dt.Columns.Add("HisDeliveryWay"); //接收方类型
		dt.Columns.Add("HisDeliveryWayText");
		dt.Columns.Add("HisDeliveryWayJsFnPara");
		dt.Columns.Add("HisDeliveryWayCountLabel");
		dt.Columns.Add("HisDeliveryWayCount"); //接收方Count

		dt.Columns.Add("HisCCRole"); //抄送人
		dt.Columns.Add("HisCCRoleText");
		dt.Columns.Add("HisFrmEventsCount"); //消息&事件Count
		dt.Columns.Add("HisFinishCondsCount"); //流程完成条件Count
		DataRow dr;
		for (Node node : nodes.ToJavaList())
		{
			dr = dt.NewRow();
			dr.setValue("NodeID", node.getNodeID());
			dr.setValue("Name", node.getName());
			dr.setValue("HisFormType", node.getHisFormType());
			dr.setValue("HisFormTypeText", node.getHisFormTypeText());
			dr.setValue("HisRunModel", node.getHisRunModel());
			dr.setValue("HisRunModelT", node.getHisRunModelT());
			dr.setValue("HisDeliveryWay", node.getHisDeliveryWay());
			dr.setValue("HisDeliveryWayText", node.getHisDeliveryWayText());

			//接收方数量
			int intHisDeliveryWayCount = 0;
			if (node.getHisDeliveryWay() == bp.wf.DeliveryWay.ByStation)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByStation");
				dr.setValue("HisDeliveryWayCountLabel", "岗位");
				bp.wf.template.NodeStations nss = new bp.wf.template.NodeStations();
				intHisDeliveryWayCount = nss.Retrieve(bp.wf.template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			else if (node.getHisDeliveryWay() == bp.wf.DeliveryWay.ByDept)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "部门");
				bp.wf.template.NodeDepts nss = new bp.wf.template.NodeDepts();
				intHisDeliveryWayCount = nss.Retrieve(bp.wf.template.NodeDeptAttr.FK_Node, node.getNodeID());
			}
			else if (node.getHisDeliveryWay() == bp.wf.DeliveryWay.ByBindEmp)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "人员");
				bp.wf.template.NodeEmps nes = new bp.wf.template.NodeEmps();
				intHisDeliveryWayCount = nes.Retrieve(bp.wf.template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			dr.setValue("HisDeliveryWayCount", intHisDeliveryWayCount);

			//抄送
			dr.setValue("HisCCRole", node.getHisCCRole());
			dr.setValue("HisCCRoleText", node.getHisCCRoleText());

			//消息&事件Count
			bp.sys.FrmEvents fes = new bp.sys.FrmEvents();
			dr.setValue("HisFrmEventsCount", fes.Retrieve(bp.sys.FrmEventAttr.FK_MapData, "ND" + node.getNodeID()));

			//流程完成条件Count
			bp.wf.template.Conds conds = new bp.wf.template.Conds(bp.wf.template.CondType.Flow, node.getNodeID());
			dr.setValue("HisFinishCondsCount", conds.size());

			dt.Rows.add(dr);
		}
		return bp.tools.Json.ToJson(dt);
	}

		///


		///特别控件特别用户权限
	public final String SepcFiledsSepcUsers_Init()
	{

		/*string fk_mapdata = this.GetRequestVal("FK_MapData");
		if (DataType.IsNullOrEmpty(fk_mapdata))
		    fk_mapdata = "ND101";

		string fk_node = this.GetRequestVal("FK_Node");
		if (DataType.IsNullOrEmpty(fk_node))
		    fk_mapdata = "101";


		MapAttrs attrs = new MapAttrs(fk_mapdata);

		bp.sys.FrmImgs imgs = new bp.sys.FrmImgs(fk_mapdata);

		bp.sys.MapExts exts = new bp.sys.MapExts();
		int mecount = exts.Retrieve(bp.sys.MapExtAttr.FK_MapData, fk_mapdata,
		    bp.sys.MapExtAttr.Tag, this.GetRequestVal("FK_Node"),
		    bp.sys.MapExtAttr.ExtType, "SepcFiledsSepcUsers");

		bp.sys.FrmAttachments aths = new bp.sys.FrmAttachments(fk_mapdata);

		exts = new bp.sys.MapExts();
		exts.Retrieve(bp.sys.MapExtAttr.FK_MapData, fk_mapdata,
		    bp.sys.MapExtAttr.Tag, this.GetRequestVal("FK_Node"),
		    bp.sys.MapExtAttr.ExtType, "SepcAthSepcUsers");
		*/
		return ""; //toJson
	}

		///


		///批量发起规则设置
	public final String BatchStartFields_Init() throws Exception
	{
		int nodeID = Integer.parseInt(String.valueOf(this.getFK_Node()));
		//获取节点字段集合
		MapAttrs attrs = new MapAttrs("ND" + nodeID);
		//获取节点对象
		bp.wf.Node nd = new bp.wf.Node(nodeID);
		//获取批量发起设置规则
		bp.sys.SysEnums ses = new bp.sys.SysEnums(bp.wf.template.NodeAttr.BatchRole);
		//获取当前节点设置的批处理规则
		String srole = "";
		if (nd.getHisBatchRole() == BatchRole.None)
		{
			srole = "0";
		}
		else if (nd.getHisBatchRole() == BatchRole.Ordinary)
		{
			srole = "1";
		}
		else
		{
			srole = "2";
		}
		return "{\"nd\":" + nd.ToJson() + ",\"ses\":" + ses.ToJson() + ",\"attrs\":" + attrs.ToJson() + ",\"BatchRole\":" + srole + "}";
	}

		///


		///批量发起规则设置save
	public final String BatchStartFields_Save() throws Exception
	{
		int nodeID = Integer.parseInt(String.valueOf(this.getFK_Node()));
		MapAttrs attrs = new MapAttrs("ND" + nodeID);
		bp.wf.Node nd = new bp.wf.Node(nodeID);

		//给变量赋值.
		//批处理的类型
		int selectval = Integer.parseInt(this.GetRequestVal("DDL_BRole"));
		switch (selectval)
		{
			case 0:
				nd.setHisBatchRole(bp.wf.BatchRole.None);
				break;
			case 1:
				nd.setHisBatchRole(bp.wf.BatchRole.Ordinary);
				break;
			default:
				nd.setHisBatchRole(bp.wf.BatchRole.Group);
				break;
		}
		//批处理的数量
		nd.setBatchListCount(Integer.parseInt(this.GetRequestVal("TB_BatchListCount")));
		//批处理的参数 
		String sbatchparas = "";
		if (this.GetRequestVal("CheckBoxIDs") != null)
		{
			sbatchparas = this.GetRequestVal("CheckBoxIDs");
		}
		nd.setBatchParas(sbatchparas);
		nd.Update();

		return "保存成功.";
	}

		///


		///发送阻塞模式
	public final String BlockModel_Save() throws Exception
	{
		bp.wf.Node nd = new bp.wf.Node(this.getFK_Node());

		nd.setBlockAlert(this.GetRequestVal("TB_Alert")); //提示信息.

		int val = this.GetRequestValInt("RB_BlockModel");
		nd.SetValByKey(bp.wf.template.NodeAttr.BlockModel, val);
		if (nd.getBlockModel() == bp.wf.BlockModel.None)
		{
			nd.setBlockModel(bp.wf.BlockModel.None);
		}

		if (nd.getBlockModel() == bp.wf.BlockModel.CurrNodeAll)
		{
			nd.setBlockModel(bp.wf.BlockModel.CurrNodeAll);
		}

		if (nd.getBlockModel() == bp.wf.BlockModel.SpecSubFlow)
		{
			nd.setBlockModel(bp.wf.BlockModel.SpecSubFlow);
			nd.setBlockExp(this.GetRequestVal("TB_SpecSubFlow"));
		}

		if (nd.getBlockModel() == bp.wf.BlockModel.BySQL)
		{
			nd.setBlockModel(bp.wf.BlockModel.BySQL);
			nd.setBlockExp(this.GetRequestVal("TB_SQL"));
		}

		if (nd.getBlockModel() == bp.wf.BlockModel.ByExp)
		{
			nd.setBlockModel(bp.wf.BlockModel.ByExp);
			nd.setBlockExp(this.GetRequestVal("TB_Exp"));
		}

		if (nd.getBlockModel() == bp.wf.BlockModel.SpecSubFlowNode)
		{
			nd.setBlockModel(bp.wf.BlockModel.SpecSubFlowNode);
			nd.setBlockExp(this.GetRequestVal("TB_SpecSubFlowNode"));
		}
		if (nd.getBlockModel() == bp.wf.BlockModel.SameLevelSubFlow)
		{
			nd.setBlockModel(bp.wf.BlockModel.SameLevelSubFlow);
			nd.setBlockExp(this.GetRequestVal("TB_SameLevelSubFlow"));
		}

		nd.setBlockAlert(this.GetRequestVal("TB_Alert"));
		nd.Update();

		return "保存成功.";
	}

		///


		///可以撤销的节点
	public final String CanCancelNodes_Save() throws Exception
	{
		bp.wf.template.NodeCancels rnds = new bp.wf.template.NodeCancels();
		rnds.Delete(bp.wf.template.NodeCancelAttr.FK_Node, this.getFK_Node());

		bp.wf.Nodes nds = new Nodes();
		nds.Retrieve(bp.wf.template.NodeAttr.FK_Flow, this.getFK_Flow());

		int i = 0;
		for (bp.wf.Node nd : nds.ToJavaList())
		{
			String cb = this.GetRequestVal("CB_" + nd.getNodeID());
			if (cb == null || cb.equals(""))
			{
				continue;
			}

			NodeCancel nr = new NodeCancel();
			nr.setFK_Node(this.getFK_Node());
			nr.setCancelTo(nd.getNodeID());
			nr.Insert();
			i++;
		}
		if (i == 0)
		{
			return "请您选择要撤销的节点。";
		}

		return "设置成功.";
	}

		///


		///表单检查(CheckFrm.htm)
	public final String CheckFrm_Check() throws Exception
	{
		if (!WebUser.getNo().equals("admin"))
		{
			return "err@只有管理员有权限进行此项操作！";
		}

		if (DataType.IsNullOrEmpty(this.getFK_MapData()))
		{
			return "err@参数FK_MapData不能为空！";
		}

		String msg = "";

		//1.检查字段扩展设置
		MapExts mes = new MapExts(this.getFK_MapData());
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		MapDtls dtls = new MapDtls(this.getFK_MapData());
		Entity en = null;
		String fieldMsg = "";

		//1.1主表
		for (MapExt me : mes.ToJavaList())
		{
			if (!DataType.IsNullOrEmpty(me.getAttrOfOper()))
			{
				en = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, me.getAttrOfOper());

				if (en != null && !DataType.IsNullOrEmpty(me.getAttrsOfActive()))
				{
					en = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, me.getAttrsOfActive());
				}
			}

			if (en == null)
			{
				me.DirectDelete();
				msg += "删除扩展设置中MyPK=" + me.getPKVal() + "的设置项；<br />";
			}
		}

		//1.2明细表
		for (MapDtl dtl : dtls.ToJavaList())
		{
			mes = new MapExts(dtl.getNo());
			attrs = new MapAttrs(dtl.getNo());

			for (MapExt me : mes.ToJavaList())
			{
				if (!DataType.IsNullOrEmpty(me.getAttrOfOper()))
				{
					en = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, me.getAttrOfOper());

					if (en != null && !DataType.IsNullOrEmpty(me.getAttrsOfActive()))
					{
						en = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, me.getAttrsOfActive());
					}
				}

				if (en == null)
				{
					me.DirectDelete();
					msg += "删除扩展设置中MyPK=" + me.getPKVal() + "的设置项；<br />";
				}
			}
		}

		//2.检查字段权限
		FrmFields ffs = new FrmFields();
		ffs.Retrieve(FrmFieldAttr.FK_MapData, this.getFK_MapData());

		//2.1主表
		for (FrmField ff : ffs.ToJavaList())
		{
			en = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ff.getKeyOfEn());

			if (en == null)
			{
				ff.DirectDelete();
				msg += "删除字段权限中MyPK=" + ff.getPKVal() + "的设置项；<br />";
			}
		}

		//2.2明细表
		for (MapDtl dtl : dtls.ToJavaList())
		{
			ffs = new FrmFields();
			ffs.Retrieve(FrmFieldAttr.FK_MapData, dtl.getNo());
			attrs = new MapAttrs(dtl.getNo());

			for (FrmField ff : ffs.ToJavaList())
			{
				en = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ff.getKeyOfEn());

				if (en == null)
				{
					ff.DirectDelete();
					msg += "删除字段权限中MyPK=" + ff.getPKVal() + "的设置项；<br />";
				}
			}
		}

		msg += "检查完成！";

		return msg;
	}

		///

}