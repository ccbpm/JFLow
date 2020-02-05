package BP.WF.HttpHandler;

import BP.WF.*;
import BP.Web.*;
import BP.Sys.*;
import BP.Tools.StringHelper;
import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.WebContralBase;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.XML.*;
import BP.WF.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WF_Admin_AttrNode extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_AttrNode()
	{

	}


		///#region 事件基类.
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
	*/
	public final String ActionDtl_Init()
	{
		//业务单元集合.
		DataTable dtBuess = new DataTable();
		dtBuess.Columns.Add("No", String.class);
		dtBuess.Columns.Add("Name", String.class);
		dtBuess.TableName = "BuessUnits";
		ArrayList<BuessUnitBase> al = BP.En.ClassFactory.GetObjects("BP.Sys.BuessUnitBase");
		for (BuessUnitBase en : al)
		{
			DataRow dr = dtBuess.NewRow();
			dr.setValue("No", en.toString());
			dr.setValue("Name", en.getTitle());
			dtBuess.Rows.add(dr);
		}

		return BP.Tools.Json.ToJson(dtBuess);
	}

		///#endregion 事件基类.



	private HttpServletResponse response;
	public void setHttpServletResp(HttpServletResponse response) {

		this.response = response;
	}
	///#region  单据模版维护
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
	/** 
	 下载文件.
	 * @throws Exception 
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

		///#endregion


		///#region  节点消息
	public final String PushMsg_Init() throws Exception
	{
		//增加上单据模版集合.
		int nodeID = this.GetRequestValInt("FK_Node");
		BP.WF.Template.PushMsgs ens = new BP.WF.Template.PushMsgs(nodeID);
		return ens.ToJson();
	}
	public final String PushMsg_Save() throws Exception
	{
		BP.WF.Template.PushMsg msg = new BP.WF.Template.PushMsg();
		msg.setMyPK(this.getMyPK());
		msg.RetrieveFromDBSources();

		msg.setFK_Event(this.getFK_Event());
		msg.setFK_Node(this.getFK_Node());

		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		BP.WF.Nodes nds = new BP.WF.Nodes(nd.getFK_Flow());
		msg.setFK_Flow(nd.getFK_Flow());

		//推送方式。
		msg.setSMSPushWay(Integer.parseInt((this.GetRequestVal("RB_SMS").replace("RB_SMS_", ""))));

		//表单字段作为接收人.
		msg.setSMSField(this.GetRequestVal("DDL_SMS_Fields"));


			///#region 其他节点的处理人方式（求选择的节点）
		String nodesOfSMS = "";
		String nodesOfEmail = "";
		for (BP.WF.Node mynd : nds.ToJavaList())
		{
			Enumeration<String> enums = getRequest().getParameterNames();
			while (enums.hasMoreElements()) {
				String key = (String) enums.nextElement();
				if (key.contains("CB_SMS_" + mynd.getNodeID()) && nodesOfSMS.contains(mynd.getNodeID() + "") == false) {
					nodesOfSMS += mynd.getNodeID() + ",";
				}

				if (key.contains("CB_Email_" + mynd.getNodeID())
						&& nodesOfEmail.contains(mynd.getNodeID() + "") == false) {
					nodesOfEmail += mynd.getNodeID() + ",";
				}
			}
		}
		msg.setSMSNodes(nodesOfSMS);

			///#endregion 其他节点的处理人方式（求选择的节点）

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
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.NodeWarning))
		{
			int noticeType = Integer.parseInt(this.GetRequestVal("RB_NoticeType").replace("RB_NoticeType", ""));
			msg.SetPara("NoticeType", noticeType);
			int hour = Integer.parseInt(this.GetRequestVal("TB_NoticeHour"));
			msg.SetPara("NoticeHour", hour);
		}

		//节点逾期
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.NodeOverDue))
		{
			int noticeType = Integer.parseInt(this.GetRequestVal("RB_NoticeType").replace("RB_NoticeType", ""));
			msg.SetPara("NoticeType", noticeType);
			int day = Integer.parseInt(this.GetRequestVal("TB_NoticeDay"));
			msg.SetPara("NoticeDay", day);
		}

		//保存.
		if (DataType.IsNullOrEmpty(msg.getMyPK()) == true)
		{
			msg.setMyPK(BP.DA.DBAccess.GenerGUID());
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
		//select * from Sys_MapAttr where FK_MapData='ND102' and LGType = 0 AND MyDataType =1

		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs();
		attrs.Retrieve(BP.Sys.MapAttrAttr.FK_MapData, "ND" + this.getFK_Node(), "LGType", 0, "MyDataType", 1);
		ds.Tables.add(attrs.ToDataTableField("FrmFields"));

		//节点 
		//TODO 数据太多优化一下
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		BP.WF.Nodes nds = new BP.WF.Nodes(nd.getFK_Flow());
		ds.Tables.add(nds.ToDataTableField("Nodes"));

		//mypk
		BP.WF.Template.PushMsg msg = new BP.WF.Template.PushMsg();
		msg.setMyPK(this.getMyPK());
		msg.RetrieveFromDBSources();
		ds.Tables.add(msg.ToDataTableField("PushMsgEntity"));

		return BP.Tools.Json.ToJson(ds);
	}



		///#endregion


		///#region 表单模式
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

		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		nd.setNodeFrmID(nd.getNodeFrmID());
		// nd.FormUrl = nd.FormUrl;

		DataTable mydt = nd.ToDataTableField("WF_Node");
		ds.Tables.add(mydt);

		BtnLabExtWebOffice mybtn = new BtnLabExtWebOffice(this.getFK_Node());
		DataTable mydt2 = mybtn.ToDataTableField("WF_BtnLabExtWebOffice");
		ds.Tables.add(mydt2);

		BtnLab btn = new BtnLab(this.getFK_Node());
		DataTable dtBtn = btn.ToDataTableField("WF_BtnLab");
		ds.Tables.add(dtBtn);

		//节点s
		Nodes nds = new Nodes(nd.getFK_Flow());

		//节点s
		ds.Tables.add(nds.ToDataTableField("Nodes"));

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 表单模式
	 
	 @return 
	 * @throws Exception 
	*/
	public final String NodeFromWorkModel_Save() throws Exception
	{
		Node nd = new Node(this.getFK_Node());

		BP.Sys.MapData md = new BP.Sys.MapData("ND" + this.getFK_Node());

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

		//如果公文表单选择了
		if (selectFModel.equals("WebOffice"))
		{
			nd.setFormType(NodeFormType.WebOffice);
			nd.Update();

			//按钮标签.
			BtnLabExtWebOffice btn = new BtnLabExtWebOffice(this.getFK_Node());

			// tab 页工作风格.
			String WebOfficeStyle = this.GetValFromFrmByKey("WebOfficeStyle");
			if (WebOfficeStyle.equals("0"))
			{
				btn.setWebOfficeWorkModel(WebOfficeWorkModel.FrmFirst);
			}
			else
			{
				btn.setWebOfficeWorkModel(WebOfficeWorkModel.WordFirst);
			}


			String WebOfficeFrmType = this.GetValFromFrmByKey("WebOfficeFrmType");
			//表单工作模式.
			if (WebOfficeFrmType.equals("0"))
			{
				btn.setWebOfficeFrmModel(BP.Sys.FrmType.FreeFrm);

				md.setHisFrmType(FrmType.FreeFrm); //同时更新表单表住表.
				md.Update();
			}
			else
			{
				btn.setWebOfficeFrmModel(BP.Sys.FrmType.FoolForm);

				md.setHisFrmType(FrmType.FoolForm); //同时更新表单表住表.
				md.Update();
			}

			btn.Update();
		}

		return "保存成功...";
	}

		///#endregion 表单模式


		///#region 手机表单字段排序

		///#region SortingMapAttrs_Init

	public final String SortingMapAttrs_Init() throws Exception
	{
		MapDatas mapdatas;
		MapAttrs attrs;
		GroupFields groups;
		MapDtls dtls;
		FrmAttachments athMents;
		FrmBtns btns;

		Nodes nodes = null;


			///#region 获取数据
		mapdatas = new MapDatas();
		QueryObject qo = new QueryObject(mapdatas);
		qo.AddWhere(MapDataAttr.No, "Like", getFK_MapData() + "%");
		qo.addOrderBy(MapDataAttr.Idx);
		qo.DoQuery();

		attrs = new MapAttrs();
		qo = new QueryObject(attrs);
		qo.AddWhere(MapAttrAttr.FK_MapData, getFK_MapData());
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.UIVisible, true);
		qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
		qo.DoQuery();

		btns = new FrmBtns(this.getFK_MapData());
		athMents = new FrmAttachments(this.getFK_MapData());
		dtls = new MapDtls(this.getFK_MapData());

		groups = new GroupFields();
		qo = new QueryObject(groups);
		qo.AddWhere(GroupFieldAttr.FrmID, getFK_MapData());
		qo.addOrderBy(GroupFieldAttr.Idx);
		qo.DoQuery();

			///#endregion

		DataSet ds = new DataSet();

		BindData4SortingMapAttrs_Init(mapdatas, attrs, groups, dtls, athMents, btns, nodes, ds);

		//控制页面按钮需要的
		MapDtl tdtl = new MapDtl();
		tdtl.setNo(getFK_MapData());
		if (tdtl.RetrieveFromDBSources() == 1)
		{
			ds.Tables.add(tdtl.ToDataTableField("tdtl"));
		}

		return BP.Tools.Json.ToJson(ds);
	}

	private void BindData4SortingMapAttrs_Init(MapDatas mapdatas, MapAttrs attrs, GroupFields groups, MapDtls dtls, FrmAttachments athMents, FrmBtns btns, Nodes nodes, DataSet ds) throws Exception
	{
		Object tempVar = mapdatas.GetEntityByKey(getFK_MapData());
		MapData mapdata = tempVar instanceof MapData ? (MapData)tempVar : null;
		DataTable dtAttrs = attrs.ToDataTableField("dtAttrs");
		DataTable dtDtls = dtls.ToDataTableField("dtDtls");
		DataTable dtGroups = groups.ToDataTableField("dtGroups");
		DataTable dtNoGroupAttrs = null;
		DataRow[] rows_Attrs = null;
		int idx_Attr = 1;
		int gidx = 1;
		GroupField group = null;

		if (mapdata != null)
		{

				///#region 一、面板1、 分组数据+未分组数据


				///#region A、构建数据dtNoGroupAttrs，这个放在前面
			//检索全部字段，查找出没有分组或分组信息不正确的字段，存入"无分组"集合
			dtNoGroupAttrs = dtAttrs.clone();

			for (DataRow dr : dtAttrs.Rows)
			{
				if (IsExistInDataRowArray(dtGroups.Rows, GroupFieldAttr.OID, dr.getValue(MapAttrAttr.GroupID)) == false)
				{
					dtNoGroupAttrs.Rows.AddDatas(dr.ItemArray);
				}
			}

				///#endregion


				///#region B、构建数据dtGroups，这个放在后面(！！涉及更新数据库)

				///#region 如果没有，则创建分组（1.明细2.多附件3.按钮）
			//01、未分组明细表,自动创建一个
			for (MapDtl mapDtl : dtls.ToJavaList())
			{
				if (GetGroupID(mapDtl.getNo(), groups) == 0)
				{
					group = new GroupField();
					group.setLab(mapDtl.getName());
					group.setFrmID(mapDtl.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Dtl);
					group.setCtrlID(mapDtl.getNo());
					group.Insert();

					groups.AddEntity(group);
				}
			}
			//02、未分组多附件自动分配一个
			for (FrmAttachment athMent : athMents.ToJavaList())
			{
				if (GetGroupID(athMent.getMyPK(), groups) == 0)
				{
					group = new GroupField();
					group.setLab(athMent.getName());
					group.setEnName(athMent.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Ath);
					group.setCtrlID(athMent.getMyPK());
					group.Insert();

					athMent.setGroupID(group.getOID());
					athMent.Update();

					groups.AddEntity(group);
				}
			}

			//03、未分组按钮自动创建一个
			for (FrmBtn fbtn : btns.ToJavaList())
			{
				if (GetGroupID(fbtn.getMyPK(), groups) == 0)
				{
					group = new GroupField();
					group.setLab(fbtn.getText());
					group.setFrmID(fbtn.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Btn);
					group.setCtrlID(fbtn.getMyPK());
					group.Insert();

					fbtn.setGroupID(group.getOID());
					fbtn.Update();

					groups.AddEntity(group);
				}
			}

				///#endregion

			dtGroups = groups.ToDataTableField("dtGroups");




				///#region 三、其他。如果是明细表的字段排序，则增加"返回"按钮；否则增加"复制排序"按钮,2016-03-21

			DataTable isDtl = new DataTable();
			isDtl.Columns.Add("tdDtl", Integer.class);
			isDtl.Columns.Add("FK_MapData", String.class);
			isDtl.Columns.Add("No", String.class);
			isDtl.TableName = "TRDtl";

			DataRow tddr = isDtl.NewRow();

			MapDtl tdtl = new MapDtl();
			tdtl.setNo(getFK_MapData());
			if (tdtl.RetrieveFromDBSources() == 1)
			{
				tddr.setValue("tdDtl", 1);
				tddr.setValue("FK_MapData", tdtl.getFK_MapData());
				tddr.setValue("No", tdtl.getNo());
			}
			else
			{
				tddr.setValue("tdDtl", 0);
				tddr.setValue("FK_MapData", getFK_MapData());
				tddr.setValue("No", tdtl.getNo());
			}


			isDtl.Rows.AddDatas(tddr.ItemArray);

				///#endregion


				///#region 增加节点信息
			if (DataType.IsNullOrEmpty(getFK_Flow()) == false)
			{
				nodes = new Nodes();
				nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, getFK_Flow(), BP.WF.Template.NodeAttr.Step);

				if (nodes.size() == 0)
				{
					String nodeid = getFK_MapData().replace("ND", "");
					String flowno = "";

					if (nodeid.length() > 2)
					{
						flowno = StringHelper.padLeft(nodeid.substring(0, nodeid.length() - 2), 3, '0');
						nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, flowno, BP.WF.Template.NodeAttr.Step);
					}
				}
				DataTable dtNodes = nodes.ToDataTableField("dtNodes");
				dtNodes.TableName = "dtNodes";
				ds.Tables.add(dtNodes);
			}


				///#endregion

			ds.Tables.add(mapdatas.ToDataTableField("mapdatas"));
			dtGroups.TableName = "dtGroups";
			ds.Tables.add(dtGroups);
			dtNoGroupAttrs.TableName = "dtNoGroupAttrs";
			ds.Tables.add(dtNoGroupAttrs);
			dtAttrs.TableName = "dtAttrs";
			ds.Tables.add(dtAttrs);
			dtDtls.TableName = "dtDtls";
			ds.Tables.add(dtDtls);
			ds.Tables.add(athMents.ToDataTableField("athMents"));
			ds.Tables.add(btns.ToDataTableField("btns"));
			ds.Tables.add(isDtl);

			//ds.Tables.add(nodes.ToDataTableField("nodes"));
		}
	}

	/** 
	 判断在DataRow数组中，是否存在指定列指定值的行
	 
	 @param rows DataRow数组
	 @param field 指定列名
	 @param value 指定值
	 @return 
	*/
	private boolean IsExistInDataRowArray(DataRowCollection rows, String field, Object value)
	{
		for (DataRow row : rows)
		{
			int rw = Integer.parseInt(row.getValue(field).toString());
			if (rw == Integer.parseInt(value.toString()))
			{
				return true;
			}
		}

		return false;
	}

	private int GetGroupID(String ctrlID, GroupFields gfs) throws Exception
	{
		Object tempVar = gfs.GetEntityByKey(GroupFieldAttr.CtrlID, ctrlID);
		GroupField gf = tempVar instanceof GroupField ? (GroupField)tempVar : null;
		return (int) (gf == null ? 0 : gf.getOID());
	}


		///#endregion


		///#region 重置字段顺序
	/** 
	 重置字段顺序
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SortingMapAttrs_ReSet() throws Exception
	{
		try
		{
			MapAttrs attrs = new MapAttrs();
			QueryObject qo = new QueryObject(attrs);
			qo.AddWhere(MapAttrAttr.FK_MapData, getFK_MapData()); //添加查询条件
			qo.addAnd();
			qo.AddWhere(MapAttrAttr.UIVisible, true);
			qo.addOrderBy(MapAttrAttr.Y, MapAttrAttr.X);
			qo.DoQuery(); //执行查询
			int rowIdx = 0;
			//执行更新
			for (MapAttr mapAttr : attrs.ToJavaList())
			{
				mapAttr.setIdx(rowIdx);
				mapAttr.DirectUpdate();
				rowIdx++;
			}

			return "重置成功！";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage().toString();
		}
	}

		///#endregion


		///#region 设置在手机端显示的字段
	/** 
	 保存需要在手机端表单显示的字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SortingMapAttrs_From_Save() throws Exception
	{
		//获取需要显示的字段集合
		String atts = this.GetRequestVal("attrs");
		try
		{
			MapAttrs attrs = new MapAttrs(getFK_MapData());
			MapAttr att = null;
			//更新每个字段的显示属性
			for (MapAttr attr : attrs.ToJavaList())
			{
				Object tempVar = attrs.GetEntityByKey(MapAttrAttr.FK_MapData, getFK_MapData(), MapAttrAttr.KeyOfEn, attr.getKeyOfEn());
				att = tempVar instanceof MapAttr ? (MapAttr)tempVar : null;
				if (atts.contains("," + attr.getKeyOfEn() + ","))
				{
					att.setIsEnableInAPP(true);
				}
				else
				{
					att.setIsEnableInAPP(false);
				}
				att.Update();
			}
			//获取附件
			FrmAttachments aths = new FrmAttachments();
			aths.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData(), FrmAttachmentAttr.FK_Node, 0);
			for(FrmAttachment ath : aths.ToJavaList())
			{
				if (atts.indexOf("," + ath.getMyPK()  + ",") !=-1)
					ath.SetPara("IsShowMobile", 1);
				else
					ath.SetPara("IsShowMobile", 0);
				ath.Update();
			}
			return "保存成功！";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage().toString();
		}
	}

		///#endregion


		///#region 将分组、字段排序复制到其他节点
	/** 
	 将分组、字段排序复制到其他节点
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SortingMapAttrs_Copy() throws Exception
	{
		try
		{
			String[] nodeids = this.GetRequestVal("NodeIDs").split("[,]", -1);

			MapDatas mapdatas = new MapDatas();
			QueryObject obj = new QueryObject(mapdatas);
			obj.AddWhere(MapDataAttr.No, "Like", getFK_MapData() + "%");
			obj.addOrderBy(MapDataAttr.Idx);
			obj.DoQuery();

			MapAttrs attrs = new MapAttrs();
			obj = new QueryObject(attrs);
			obj.AddWhere(MapAttrAttr.FK_MapData, getFK_MapData());
			obj.addAnd();
			obj.AddWhere(MapAttrAttr.UIVisible, true);
			obj.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
			obj.DoQuery();

			FrmBtns btns = new FrmBtns(this.getFK_MapData());
			FrmAttachments athMents = new FrmAttachments(this.getFK_MapData());
			MapDtls dtls = new MapDtls(this.getFK_MapData());

			GroupFields groups = new GroupFields();
			obj = new QueryObject(groups);
			obj.AddWhere(GroupFieldAttr.FrmID, getFK_MapData());
			obj.addOrderBy(GroupFieldAttr.Idx);
			obj.DoQuery();

			String tmd = null;
			GroupField group = null;
			MapDatas tmapdatas = null;
			MapAttrs tattrs = null, oattrs = null, tarattrs = null;
			GroupFields tgroups = null, ogroups = null, targroups = null;
			MapDtls tdtls = null;
			MapData tmapdata = null;
			MapAttr tattr = null;
			GroupField tgrp = null;
			MapDtl tdtl = null;
			int maxGrpIdx = 0; //当前最大分组排序号
			int maxAttrIdx = 0; //当前最大字段排序号
			int maxDtlIdx = 0; //当前最大明细表排序号
			ArrayList<String> idxGrps = new ArrayList<String>(); //复制过的分组名称集合
			ArrayList<String> idxAttrs = new ArrayList<String>(); //复制过的字段编号集合
			ArrayList<String> idxDtls = new ArrayList<String>(); //复制过的明细表编号集合

			for (String nodeid : nodeids)
			{
				tmd = "ND" + nodeid;


					///#region 获取数据
				tmapdatas = new MapDatas();
				QueryObject qo = new QueryObject(tmapdatas);
				qo.AddWhere(MapDataAttr.No, "Like", tmd + "%");
				qo.addOrderBy(MapDataAttr.Idx);
				qo.DoQuery();

				tattrs = new MapAttrs();
				qo = new QueryObject(tattrs);
				qo.AddWhere(MapAttrAttr.FK_MapData, tmd);
				qo.addAnd();
				qo.AddWhere(MapAttrAttr.UIVisible, true);
				qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
				qo.DoQuery();

				tgroups = new GroupFields();
				qo = new QueryObject(tgroups);
				qo.AddWhere(GroupFieldAttr.FrmID, tmd);
				qo.addOrderBy(GroupFieldAttr.Idx);
				qo.DoQuery();

				tdtls = new MapDtls();
				qo = new QueryObject(tdtls);
				qo.AddWhere(MapDtlAttr.FK_MapData, tmd);
				qo.addAnd();
				qo.AddWhere(MapDtlAttr.IsView, true);
				//qo.addOrderBy(MapDtlAttr.RowIdx);
				qo.DoQuery();

					///#endregion


					///#region 复制排序逻辑


					///#region //分组排序复制
				for (GroupField grp : groups.ToJavaList())
				{
					//通过分组名称来确定是同一个组，同一个组在不同的节点分组编号是不一样的
					Object tempVar = tgroups.GetEntityByKey(GroupFieldAttr.Lab, grp.getLab());
					tgrp = tempVar instanceof GroupField ? (GroupField)tempVar : null;
					if (tgrp == null)
					{
						continue;
					}

					tgrp.setIdx(grp.getIdx());
					tgrp.DirectUpdate();

					maxGrpIdx = Math.max(grp.getIdx(), maxGrpIdx);
					idxGrps.add(grp.getLab());
				}

				for (GroupField grp : tgroups.ToJavaList())
				{
					if (idxGrps.contains(grp.getLab()))
					{
						continue;
					}
					maxGrpIdx = maxGrpIdx + 1;
					grp.setIdx(maxGrpIdx);
					grp.DirectUpdate();
				}

					///#endregion


					///#region //字段排序复制
				for (MapAttr attr : attrs.ToJavaList())
				{
					//排除主键
					if (attr.getIsPK() == true)
					{
						continue;
					}

					Object tempVar2 = tattrs.GetEntityByKey(MapAttrAttr.KeyOfEn, attr.getKeyOfEn());
					tattr = tempVar2 instanceof MapAttr ? (MapAttr)tempVar2 : null;
					if (tattr == null)
					{
						continue;
					}

					Object tempVar3 = groups.GetEntityByKey(GroupFieldAttr.OID, attr.getGroupID());
					group = tempVar3 instanceof GroupField ? (GroupField)tempVar3 : null;

					//比对字段的分组是否一致，不一致则更新一致
					if (group == null)
					{
						//源字段分组为空，则目标字段分组置为0
						tattr.setGroupID(0);
					}
					else
					{
						//此处要判断目标节点中是否已经创建了这个源字段所属分组，如果没有创建，则要自动创建
						Object tempVar4 = tgroups.GetEntityByKey(GroupFieldAttr.Lab, group.getLab());
						tgrp = tempVar4 instanceof GroupField ? (GroupField)tempVar4 : null;

						if (tgrp == null)
						{
							tgrp = new GroupField();
							tgrp.setLab(group.getLab());
							tgrp.setFrmID(tmd);
							tgrp.setIdx(group.getIdx());
							tgrp.Insert();
							tgroups.AddEntity(tgrp);

							tattr.setGroupID(tgrp.getOID());
						}
						else
						{
							if (tgrp.getOID() != tattr.getGroupID())
							{
								tattr.setGroupID(tgrp.getOID());
							}
						}
					}

					tattr.setIdx(attr.getIdx());
					tattr.DirectUpdate();
					maxAttrIdx = Math.max(attr.getIdx(), maxAttrIdx);
					idxAttrs.add(attr.getKeyOfEn());
				}

				for (MapAttr attr : tattrs.ToJavaList())
				{
					//排除主键
					if (attr.getIsPK() == true)
					{
						continue;
					}
					if (idxAttrs.contains(attr.getKeyOfEn()))
					{
						continue;
					}
					maxAttrIdx = maxAttrIdx + 1;
					attr.setIdx(maxAttrIdx);
					attr.DirectUpdate();
				}

					///#endregion


					///#region //明细表排序复制
				String dtlIdx = "";
				GroupField tgroup = null;
				int groupidx = 0;
				int tgroupidx = 0;

				for (MapDtl dtl : dtls.ToJavaList())
				{
					dtlIdx = dtl.getNo().replace(dtl.getFK_MapData() + "Dtl", "");
					Object tempVar5 = tdtls.GetEntityByKey(MapDtlAttr.No, tmd + "Dtl" + dtlIdx);
					tdtl = tempVar5 instanceof MapDtl ? (MapDtl)tempVar5 : null;

					if (tdtl == null)
					{
						continue;
					}

					//判断目标明细表是否有分组，没有分组，则创建分组
					tgroup = GetGroup(tdtl.getNo(), tgroups);
					tgroupidx = tgroup == null ? 0 : tgroup.getIdx();
					group = GetGroup(dtl.getNo(), groups);
					groupidx = group == null ? 0 : group.getIdx();

					if (tgroup == null)
					{
						group = new GroupField();
						group.setLab(tdtl.getName());
						group.setFrmID(tdtl.getFK_MapData());
						group.setCtrlType(GroupCtrlType.Dtl);
						group.setCtrlID(tdtl.getNo());
						group.setIdx(groupidx);
						group.Insert();

						tgroupidx = groupidx;
						tgroups.AddEntity(group);
					}


						///#region 1.明细表排序
					if (tgroupidx != groupidx && group != null)
					{
						tgroup.setIdx(groupidx);
						tgroup.DirectUpdate();

						tgroupidx = groupidx;
						Object tempVar6 = tmapdatas.GetEntityByKey(MapDataAttr.No, tdtl.getNo());
						tmapdata = tempVar6 instanceof MapData ? (MapData)tempVar6 : null;
						if (tmapdata != null)
						{
							tmapdata.setIdx(tgroup.getIdx());
							tmapdata.DirectUpdate();
						}
					}

					maxDtlIdx = Math.max(tgroupidx, maxDtlIdx);
					idxDtls.add(dtl.getNo());

						///#endregion


						///#region 2.获取源节点明细表中的字段分组、字段信息
					oattrs = new MapAttrs();
					qo = new QueryObject(oattrs);
					qo.AddWhere(MapAttrAttr.FK_MapData, dtl.getNo());
					qo.addAnd();
					qo.AddWhere(MapAttrAttr.UIVisible, true);
					qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
					qo.DoQuery();

					ogroups = new GroupFields();
					qo = new QueryObject(ogroups);
					qo.AddWhere(GroupFieldAttr.FrmID, dtl.getNo());
					qo.addOrderBy(GroupFieldAttr.Idx);
					qo.DoQuery();

						///#endregion


						///#region 3.获取目标节点明细表中的字段分组、字段信息
					tarattrs = new MapAttrs();
					qo = new QueryObject(tarattrs);
					qo.AddWhere(MapAttrAttr.FK_MapData, tdtl.getNo());
					qo.addAnd();
					qo.AddWhere(MapAttrAttr.UIVisible, true);
					qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
					qo.DoQuery();

					targroups = new GroupFields();
					qo = new QueryObject(targroups);
					qo.AddWhere(GroupFieldAttr.FrmID, tdtl.getNo());
					qo.addOrderBy(GroupFieldAttr.Idx);
					qo.DoQuery();

						///#endregion


						///#region 4.明细表字段分组排序
					maxGrpIdx = 0;
					idxGrps = new ArrayList<String>();

					for (GroupField grp : ogroups.ToJavaList())
					{
						//通过分组名称来确定是同一个组，同一个组在不同的节点分组编号是不一样的
						Object tempVar7 = targroups.GetEntityByKey(GroupFieldAttr.Lab, grp.getLab());
						tgrp = tempVar7 instanceof GroupField ? (GroupField)tempVar7 : null;
						if (tgrp == null)
						{
							continue;
						}

						tgrp.setIdx(grp.getIdx());
						tgrp.DirectUpdate();

						maxGrpIdx = Math.max(grp.getIdx(), maxGrpIdx);
						idxGrps.add(grp.getLab());
					}

					for (GroupField grp : targroups.ToJavaList())
					{
						if (idxGrps.contains(grp.getLab()))
						{
							continue;
						}
						maxGrpIdx = maxGrpIdx + 1;
						grp.setIdx(maxGrpIdx);
						grp.DirectUpdate();
					}

						///#endregion


						///#region 5.明细表字段排序
					maxAttrIdx = 0;
					idxAttrs = new ArrayList<String>();

					for (MapAttr attr : oattrs.ToJavaList())
					{
						Object tempVar8 = tarattrs.GetEntityByKey(MapAttrAttr.KeyOfEn, attr.getKeyOfEn());
						tattr = tempVar8 instanceof MapAttr ? (MapAttr)tempVar8 : null;
						if (tattr == null)
						{
							continue;
						}

						Object tempVar9 = ogroups.GetEntityByKey(GroupFieldAttr.OID, attr.getGroupID());
						group = tempVar9 instanceof GroupField ? (GroupField)tempVar9 : null;

						//比对字段的分组是否一致，不一致则更新一致
						if (group == null)
						{
							//源字段分组为空，则目标字段分组置为0
							tattr.setGroupID(0);
						}
						else
						{
							//此处要判断目标节点中是否已经创建了这个源字段所属分组，如果没有创建，则要自动创建
							Object tempVar10 = targroups.GetEntityByKey(GroupFieldAttr.Lab, group.getLab());
							tgrp = tempVar10 instanceof GroupField ? (GroupField)tempVar10 : null;

							if (tgrp == null)
							{
								tgrp = new GroupField();
								tgrp.setLab(group.getLab());
								tgrp.setEnName(tdtl.getNo());
								tgrp.setIdx(group.getIdx());
								tgrp.Insert();
								targroups.AddEntity(tgrp);

								tattr.setGroupID(tgrp.getOID());
							}
							else
							{
								if (tgrp.getOID() != tattr.getGroupID())
								{
									tattr.setGroupID(tgrp.getOID());
								}
							}
						}

						tattr.setIdx(attr.getIdx());
						tattr.DirectUpdate();
						maxAttrIdx = Math.max(attr.getIdx(), maxAttrIdx);
						idxAttrs.add(attr.getKeyOfEn());
					}

					for (MapAttr attr : tarattrs.ToJavaList())
					{
						if (idxAttrs.contains(attr.getKeyOfEn()))
						{
							continue;
						}
						maxAttrIdx = maxAttrIdx + 1;
						attr.setIdx(maxAttrIdx);
						attr.DirectUpdate();
					}

						///#endregion
				}

				//确定目标节点中，源节点没有的明细表的排序
				for (MapDtl dtl : tdtls.ToJavaList())
				{
					if (idxDtls.contains(dtl.getNo()))
					{
						continue;
					}

					maxDtlIdx = maxDtlIdx + 1;
					tgroup = GetGroup(dtl.getNo(), tgroups);

					if (tgroup == null)
					{
						tgroup = new GroupField();
						tgroup.setLab(tdtl.getName());
						tgroup.setFrmID(tdtl.getFK_MapData());
						tgroup.setCtrlType(GroupCtrlType.Dtl);
						tgroup.setCtrlID(tdtl.getNo());
						tgroup.setIdx(maxDtlIdx);
						tgroup.Insert();

						tgroups.AddEntity(group);
					}

					if (tgroup.getIdx() != maxDtlIdx)
					{
						tgroup.setIdx(maxDtlIdx);
						tgroup.DirectUpdate();
					}

					Object tempVar11 = tmapdatas.GetEntityByKey(MapDataAttr.No, dtl.getNo());
					tmapdata = tempVar11 instanceof MapData ? (MapData)tempVar11 : null;
					if (tmapdata != null)
					{
						tmapdata.setIdx(maxDtlIdx);
						tmapdata.DirectUpdate();
					}
				}

					///#endregion


					///#endregion

			}
			return "复制成功！";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage().toString();
		}
	}

	private GroupField GetGroup(String ctrlID, GroupFields gfs) throws Exception
	{
		Object tempVar = gfs.GetEntityByKey(GroupFieldAttr.CtrlID, ctrlID);
		return tempVar instanceof GroupField ? (GroupField)tempVar : null;
	}

		///#endregion


		///#endregion


		///#region 表单模式
	public final String SortingMapAttrs_Save() throws Exception
	{
		Node nd = new Node(this.getFK_Node());

		BP.Sys.MapData md = new BP.Sys.MapData("ND" + this.getFK_Node());

		//用户选择的表单类型.
		String selectFModel = this.GetValFromFrmByKey("FrmS");

		//使用ccbpm内置的节点表单
		if (selectFModel.equals("DefFrm"))
		{
			String frmModel = this.GetValFromFrmByKey("RB_Frm");
			if (frmModel.equals("0"))
			{
				nd.setFormType(NodeFormType.FreeForm);
				nd.DirectUpdate();

				md.setHisFrmType(FrmType.FreeFrm);
				md.Update();
			}
			else
			{
				nd.setFormType(NodeFormType.FoolForm);
				nd.DirectUpdate();

				md.setHisFrmType(FrmType.FoolForm);
				md.Update();
			}

			String refFrm = this.GetValFromFrmByKey("RefFrm");

			if (refFrm.equals("0"))
			{
				nd.setNodeFrmID("");
				nd.DirectUpdate();
			}

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

		//如果公文表单选择了
		if (selectFModel.equals("WebOffice"))
		{
			nd.setFormType(NodeFormType.WebOffice);
			nd.Update();

			//按钮标签.
			BtnLabExtWebOffice btn = new BtnLabExtWebOffice(this.getFK_Node());

			// tab 页工作风格.
			String WebOfficeStyle = this.GetValFromFrmByKey("WebOfficeStyle");
			if (WebOfficeStyle.equals("0"))
			{
				btn.setWebOfficeWorkModel(WebOfficeWorkModel.FrmFirst);
			}
			else
			{
				btn.setWebOfficeWorkModel(WebOfficeWorkModel.WordFirst);
			}


			String WebOfficeFrmType = this.GetValFromFrmByKey("WebOfficeFrmType");
			//表单工作模式.
			if (WebOfficeFrmType.equals("0"))
			{
				btn.setWebOfficeFrmModel(BP.Sys.FrmType.FreeFrm);

				md.setHisFrmType(FrmType.FreeFrm); //同时更新表单表住表.
				md.Update();
			}
			else
			{
				btn.setWebOfficeFrmModel(BP.Sys.FrmType.FoolForm);

				md.setHisFrmType(FrmType.FoolForm); //同时更新表单表住表.
				md.Update();
			}

			btn.Update();
		}

		return "保存成功...";
	}

		///#endregion 表单模式


		///#region 节点属性（列表）的操作
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
			if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByStation)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByStation");
				dr.setValue("HisDeliveryWayCountLabel", "岗位");
				BP.WF.Template.NodeStations nss = new BP.WF.Template.NodeStations();
				intHisDeliveryWayCount = nss.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			else if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByDept)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "部门");
				BP.WF.Template.NodeDepts nss = new BP.WF.Template.NodeDepts();
				intHisDeliveryWayCount = nss.Retrieve(BP.WF.Template.NodeDeptAttr.FK_Node, node.getNodeID());
			}
			else if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByBindEmp)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "人员");
				BP.WF.Template.NodeEmps nes = new BP.WF.Template.NodeEmps();
				intHisDeliveryWayCount = nes.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			dr.setValue("HisDeliveryWayCount", intHisDeliveryWayCount);

			//抄送
			dr.setValue("HisCCRole", node.getHisCCRole());
			dr.setValue("HisCCRoleText", node.getHisCCRoleText());

			//消息&事件Count
			BP.Sys.FrmEvents fes = new BP.Sys.FrmEvents();
			dr.setValue("HisFrmEventsCount", fes.Retrieve(BP.Sys.FrmEventAttr.FK_MapData, "ND" + node.getNodeID()));

			//流程完成条件Count
			BP.WF.Template.Conds conds = new BP.WF.Template.Conds(BP.WF.Template.CondType.Flow, node.getNodeID());
			dr.setValue("HisFinishCondsCount", conds.size());


			dt.Rows.add(dr);
		}
		return BP.Tools.Json.ToJson(dt);
	}

		///#endregion


		///#region 特别控件特别用户权限
	public final String SepcFiledsSepcUsers_Init()
	{

		/*string fk_mapdata = this.GetRequestVal("FK_MapData");
		if (DataType.IsNullOrEmpty(fk_mapdata))
		    fk_mapdata = "ND101";

		string fk_node = this.GetRequestVal("FK_Node");
		if (DataType.IsNullOrEmpty(fk_node))
		    fk_mapdata = "101";


		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs(fk_mapdata);

		BP.Sys.FrmImgs imgs = new BP.Sys.FrmImgs(fk_mapdata);

		BP.Sys.MapExts exts = new BP.Sys.MapExts();
		int mecount = exts.Retrieve(BP.Sys.MapExtAttr.FK_MapData, fk_mapdata,
		    BP.Sys.MapExtAttr.Tag, this.GetRequestVal("FK_Node"),
		    BP.Sys.MapExtAttr.ExtType, "SepcFiledsSepcUsers");

		BP.Sys.FrmAttachments aths = new BP.Sys.FrmAttachments(fk_mapdata);

		exts = new BP.Sys.MapExts();
		exts.Retrieve(BP.Sys.MapExtAttr.FK_MapData, fk_mapdata,
		    BP.Sys.MapExtAttr.Tag, this.GetRequestVal("FK_Node"),
		    BP.Sys.MapExtAttr.ExtType, "SepcAthSepcUsers");
		*/
		return ""; //toJson
	}

		///#endregion


		///#region 批量发起规则设置
	public final String BatchStartFields_Init() throws Exception
	{
		int nodeID = Integer.parseInt(String.valueOf(this.getFK_Node()));
		//获取节点字段集合
		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs("ND" + nodeID);
		//获取节点对象
		BP.WF.Node nd = new BP.WF.Node(nodeID);
		//获取批量发起设置规则
		BP.Sys.SysEnums ses = new BP.Sys.SysEnums(BP.WF.Template.NodeAttr.BatchRole);
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

		///#endregion


		///#region 批量发起规则设置save
	public final String BatchStartFields_Save() throws Exception
	{
		int nodeID = Integer.parseInt(String.valueOf(this.getFK_Node()));
		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs("ND" + nodeID);
		BP.WF.Node nd = new BP.WF.Node(nodeID);

		//给变量赋值.
		//批处理的类型
		int selectval = Integer.parseInt(this.GetRequestVal("DDL_BRole"));
		switch (selectval)
		{
			case 0:
				nd.setHisBatchRole(BP.WF.BatchRole.None);
				break;
			case 1:
				nd.setHisBatchRole(BP.WF.BatchRole.Ordinary);
				break;
			default:
				nd.setHisBatchRole(BP.WF.BatchRole.Group);
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

		///#endregion


		///#region 发送阻塞模式
	public final String BlockModel_Save() throws Exception
	{
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());

		nd.setBlockAlert(this.GetRequestVal("TB_Alert")); //提示信息.

		int val = this.GetRequestValInt("RB_BlockModel");
		nd.SetValByKey(BP.WF.Template.NodeAttr.BlockModel, val);
		if (nd.getBlockModel() == BP.WF.BlockModel.None)
		{
			nd.setBlockModel(BP.WF.BlockModel.None);
		}

		if (nd.getBlockModel() == BP.WF.BlockModel.CurrNodeAll)
		{
			nd.setBlockModel(BP.WF.BlockModel.CurrNodeAll);
		}

		if (nd.getBlockModel() == BP.WF.BlockModel.SpecSubFlow)
		{
			nd.setBlockModel(BP.WF.BlockModel.SpecSubFlow);
			nd.setBlockExp(this.GetRequestVal("TB_SpecSubFlow"));
		}

		if (nd.getBlockModel() == BP.WF.BlockModel.BySQL)
		{
			nd.setBlockModel(BP.WF.BlockModel.BySQL);
			nd.setBlockExp(this.GetRequestVal("TB_SQL"));
		}

		if (nd.getBlockModel() == BP.WF.BlockModel.ByExp)
		{
			nd.setBlockModel(BP.WF.BlockModel.ByExp);
			nd.setBlockExp(this.GetRequestVal("TB_Exp"));
		}

		if (nd.getBlockModel() == BP.WF.BlockModel.SpecSubFlowNode)
		{
			nd.setBlockModel(BP.WF.BlockModel.SpecSubFlowNode);
			nd.setBlockExp(this.GetRequestVal("TB_SpecSubFlowNode"));
		}
		if (nd.getBlockModel() == BP.WF.BlockModel.SameLevelSubFlow)
		{
			nd.setBlockModel(BP.WF.BlockModel.SameLevelSubFlow);
			nd.setBlockExp(this.GetRequestVal("TB_SameLevelSubFlow"));
		}

		nd.setBlockAlert(this.GetRequestVal("TB_Alert"));
		nd.Update();

		return "保存成功.";
	}

		///#endregion


		///#region 可以撤销的节点
	public final String CanCancelNodes_Save() throws Exception
	{
		BP.WF.Template.NodeCancels rnds = new BP.WF.Template.NodeCancels();
		rnds.Delete(BP.WF.Template.NodeCancelAttr.FK_Node, this.getFK_Node());

		BP.WF.Nodes nds = new Nodes();
		nds.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, this.getFK_Flow());

		int i = 0;
		for (BP.WF.Node nd : nds.ToJavaList())
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

		///#endregion


		///#region 表单检查(CheckFrm.htm)
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

		///#endregion

}