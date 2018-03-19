package cn.jflow.common.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrOfOneVSM;
import BP.En.AttrsOfOneVSM;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.FieldType;
import BP.Sys.FrmType;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.FormRunType;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.NodeWorkType;
import BP.WF.TodolistModel;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerList;
import BP.WF.Entity.GenerWorkerLists;
import BP.WF.Entity.ReturnWork;
import BP.WF.Entity.ReturnWorkAttr;
import BP.WF.Entity.ReturnWorks;
import BP.WF.Entity.Track;
import BP.WF.Entity.TrackAttr;
import BP.WF.Template.BillTemplate;
import BP.WF.Template.BillTemplateAttr;
import BP.WF.Template.BillTemplates;
import BP.WF.Template.CCList;
import BP.WF.Template.CCSta;
import BP.WF.Template.Frm;
import BP.WF.Template.FrmNode;
import BP.WF.Template.Frms;

public class TruakModel extends BaseModel
{
	public EnModel UCEn1 = null;
	public StringBuilder Pub1 = null;
	public TruakModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		UCEn1 = new EnModel(request,response);
		Pub1 = new StringBuilder();
	}



	public final int getStartNodeID()
	{
		return Integer.parseInt(this.getFK_Flow() + "01");
	}




	public final String getCCID()
	{
		return this.get_request().getParameter("CCID");
	}

//
//	public final void ViewWork()
//	{
//		ReturnWorks rws = new ReturnWorks();
//		rws.Retrieve(ReturnWorkAttr.ReturnToNode, this.getFK_Node(), ReturnWorkAttr.WorkID, this.getWorkID());
//
//		//ShiftWorks fws = new ShiftWorks();
//		//fws.Retrieve(ShiftWorkAttr.FK_Node, this.FK_Node, ShiftWorkAttr.WorkID, this.WorkID);
//
//		Node nd = new Node(this.getFK_Node());
//		Work wk = nd.getHisWork();
//		wk.setOID(this.getWorkID();
//		wk.RetrieveFromDBSources();
//		Pub1.append(this.AddB(wk.getEnDesc());
//		Pub1.append(this.AddWork(wk, rws, this.getFK_Node());
//	}
	public final void BindTrack_ViewSpecialWork()
	{
		ReturnWorks rws = new ReturnWorks();
		rws.Retrieve(ReturnWorkAttr.ReturnToNode, this.getFK_Node(), ReturnWorkAttr.WorkID, this.getWorkID());

		//ShiftWorks fws = new ShiftWorks();
		//fws.Retrieve(ShiftWorkAttr.FK_Node, this.FK_Node, ShiftWorkAttr.WorkID, this.WorkID);

		BP.WF.Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();
		Pub1.append(this.AddB(wk.getEnDesc()));
		this.ADDWork(wk, rws, this.getFK_Node());
	}
	/** 
	 view work.
	 
	*/
	public final void BindTrack_ViewWork()
	{
		String appPath = BP.WF.Glo.getCCFlowAppPath(); //this.Request.ApplicationPath;
		Track tk = new Track(this.getFK_Flow(), this.getMyPK());
		Node nd = new Node(tk.getNDFrom());
		Work wk = nd.getHisWork();
		wk.setOID(tk.getWorkID());
		if (wk.RetrieveFromDBSources() == 0)
		{
			AddEasyUiPanelInfo("打开(" + nd.getName() + ")错误", "<h4>当前的节点数据已经被删除！</h4>" + "<p style='font-weight:bold'>造成此问题出现的原因如下：<br /><br />" + "1、当前节点数据被非法删除；<br />" + "2、节点数据是退回人与被退回人中间的节点，这部分节点数据查看不支持。</p>", "icon-no");
			return;
		}

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(wk.getOID());
		if (gwf.RetrieveFromDBSources() == 0)
		{

		}
		else
		{
			//if (gwf.FK_Node == wk.NodeID)
			//{
			//    this.UCEn1.AddFieldSet(wk.getEnDesc());
			//    this.UCEn1.AddH1("当工作(" + nd.Name + ")未完成，您不能查看它的工作日志。");
			//    this.UCEn1.AddFieldSetEnd();
			//    return;
			//}
		}

		if (nd.getHisFlow().getIsMD5() && !wk.IsPassCheckMD5())
		{
			AddEasyUiPanelInfo("打开(" + nd.getName() + ")错误", "<h2>当前的节点数据已经被篡改，请报告管理员。</h2>", "icon-no");
			return;
		}

		//this.UCEn1.setIsReadonly(true);
		Frms frms = nd.getHisFrms();
		if (frms.size() == 0)
		{
			if (nd.getHisFormType() == NodeFormType.FreeForm)
			{
				// 自由表单 
				this.UCEn1.appendPub("<div id=divCCForm >");
				this.UCEn1.BindCCForm(wk, "ND" + nd.getNodeID(), true, 0,true); //, false, false, null);
				if (wk.getWorkEndInfo().length() > 2)
				{
					this.UCEn1.appendPub(wk.getWorkEndInfo());
				}
				this.UCEn1.appendPub("</div>");
			}

			if (nd.getHisFormType() == NodeFormType.FoolForm)
			{
				//傻瓜表单
				//this.UCEn1.IsReadonly = true;
				this.UCEn1.BindColumn4(wk, "ND" + nd.getNodeID()); //, false, false, null);
				if (wk.getWorkEndInfo().length() > 2)
				{
					this.UCEn1.appendPub(wk.getWorkEndInfo());
				}
			}

			BillTemplates bills = new BillTemplates();
			bills.Retrieve(BillTemplateAttr.NodeID, nd.getNodeID());
			if (bills.size() >= 1)
			{
				String title = "";
				for (BillTemplate item : bills.ToJavaList())
				{
					title += "<img src='" + appPath + "WF/Img/Btn/Word.gif' border=0/>" + item.getName() + "</a>";
				}

				String urlr = appPath + "WF/WorkOpt/PrintDoc.jsp?FK_Node=" + nd.getNodeID() + "&FID=" + tk.getFID() + "&WorkID=" + tk.getWorkID() + "&FK_Flow=" + tk.FK_Flow;
				this.UCEn1.appendPub("<p><a  href=\"javascript:WinOpen('" + urlr + "','dsdd');\"  />" + title + "</a></p>");
				//this.UCEn1.Add("<a href='' target=_blank><img src='../Img/Btn/Word.gif' border=0/>" + bt.Name + "</a>");
			}
		}
		else
		{
			// 涉及到多个表单的情况...
			if (nd.getHisFormType() != NodeFormType.DisableIt)
			{
				Frm myfrm = new Frm();
				myfrm.setNo("ND" + nd.getNodeID());
				myfrm.setName(wk.getEnDesc());
				//FormRunType frt = 
				myfrm.setHisFormRunType(FormRunType.forValue(nd.getHisFormType().getValue()));

				//  myfrm.HisFormType = nd.getHisFormType();

				FrmNode fnNode = new FrmNode();
				fnNode.setFK_Frm( myfrm.getNo());
				fnNode.setIsEdit(true);
				fnNode.setIsPrint(false);
				switch (nd.getHisFormType())
				{
					case FoolForm:
						fnNode.setHisFrmType( FrmType.FoolForm);
						break;
					case FreeForm:
						fnNode.setHisFrmType( FrmType.FreeFrm);
						break;
					case SelfForm:
						fnNode.setHisFrmType( FrmType.Url);
						break;
					default:
						throw new RuntimeException("出现了未判断的异常。");
				}
				myfrm.HisFrmNode = fnNode;
				frms.AddEntity(myfrm, 0);
			}

			long fid = this.getFID();
			if (this.getFID() == 0)
			{
				fid = tk.getWorkID();
			}

			if (frms.size() == 1)
			{
				// 如果禁用了节点表单，并且只有一个表单的情况。
				Frm frm = (Frm)frms.get(0);
				FrmNode fn = frm.HisFrmNode;
				String src = "";
				src = fn.getFrmUrl() + ".jsp?FK_MapData=" + frm.getNo() + "&FID=" + fid + "&IsEdit=0&IsPrint=0&FK_Node=" + nd.getNodeID() + "&WorkID=" + tk.getWorkID();
				this.UCEn1.appendPub("\t\n <DIV id='" + frm.getNo() + "' style='width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;' >");
				this.UCEn1.appendPub("\t\n <iframe ID='F" + frm.getNo() + "' src='" + src + "' frameborder=0  style='position:absolute;width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;'  leftMargin='0'  topMargin='0'  /></iframe>");
				this.UCEn1.appendPub("\t\n </DIV>");
			}
			else
			{
				///#region 载入相关文件.
//				this.Page.RegisterClientScriptBlock("sg", "");
//
//				this.Page.RegisterClientScriptBlock("s2g4", "");
//
//				this.Page.RegisterClientScriptBlock("sdf24j", "");
//
//				this.Page.RegisterClientScriptBlock("sdsdf24j", "");
				///#endregion 载入相关文件.

				this.UCEn1.Pub.setLength(0);
				this.UCEn1.appendPub("<div  style='clear:both' ></div>");
				this.UCEn1.appendPub("\t\n<div  id='usual2' class='usual' >"); //begain.

				///#region 输出标签.
				this.UCEn1.appendPub("\t\n <ul  class='abc' style='background:red;border-color: #800000;border-width: 10px;' >");
				for (Frm frm : frms.ToJavaList())
				{
					FrmNode fn = frm.HisFrmNode;
					String src = "";
					src = fn.getFrmUrl() + ".jsp?FK_MapData=" + frm.getNo() + "&FID=" + fid + "&IsEdit=0&IsPrint=0&FK_Node=" + nd.getNodeID() + "&WorkID=" + tk.getWorkID();
					this.UCEn1.appendPub("\t\n<li><a href=\"#" + frm.getNo() + "\" onclick=\"TabClick('" + frm.getNo() + "','" + src + "');\" >" + frm.getName() + "</a></li>");
				}
				this.UCEn1.appendPub("\t\n </ul>");
				///#endregion 输出标签.

				///#region 输出表单 iframe 内容.
				for (Frm frm :frms.ToJavaList())
				{
					FrmNode fn = frm.HisFrmNode;
					this.UCEn1.appendPub("\t\n <DIV id='" + frm.getNo() + "' style='width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;' >");
					String src = "loading.htm";
					this.UCEn1.appendPub("\t\n <iframe ID='F" + frm.getNo() + "' src='" + src + "' frameborder=0  style='position:absolute;width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;'  leftMargin='0'  topMargin='0'   /></iframe>");
					this.UCEn1.appendPub("\t\n </DIV>");
				}
				///#endregion 输出表单 iframe 内容.

				this.UCEn1.appendPub("\t\n</div>"); // end usual2

				// 设置选择的默认值.
				this.UCEn1.appendPub("\t\n<script type='text/javascript'>");
				this.UCEn1.appendPub("\t\n  $(\"#usual2 ul\").idTabs(\"" + ((Frm)frms.get(0)).getNo() + "\");");
				this.UCEn1.appendPub("\t\n</script>");
			}
		}
	}


	public final void init()
	{
		if (this.getDoType()!=null && this.getDoType().equals("View"))
		{
			this.BindTrack_ViewWork();
			return;
		}

		if (this.getDoType()!=null && this.getDoType().equals("ViewSpecialWork"))
		{
			this.BindTrack_ViewSpecialWork();
			return;
		}

		/*Pub1.append(this.AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width: 100%'"));
		Pub1.append(this.AddTR());
		Pub1.append(this.AddTDGroupTitle("style='text-align:center'", "序"));
		Pub1.append(this.AddTDGroupTitle("操作时间"));
		Pub1.append(this.AddTDGroupTitle("表单")); //moved by liuxc,2014-12-18,应zhangqingpeng要求将此列提前
		Pub1.append(this.AddTDGroupTitle("从节点"));
		Pub1.append(this.AddTDGroupTitle("人员"));
		Pub1.append(this.AddTDGroupTitle("到节点"));
		Pub1.append(this.AddTDGroupTitle("人员"));
		Pub1.append(this.AddTDGroupTitle("到达时间"));
		Pub1.append(this.AddTDGroupTitle("用时"));
		Pub1.append(this.AddTDGroupTitle("活动"));
		Pub1.append(this.AddTDGroupTitle("信息"));
		Pub1.append(this.AddTDGroupTitle("执行人"));
		Pub1.append(this.AddTREnd());*/

		//获取track.
		DataTable dt = null;
		try {
			DataSet ds = BP.WF.Dev2Interface.DB_GenerTrack(this.getFK_Flow(), this.getWorkID(), this.getFID());
			for (DataTable tb : ds.getTables()) {
				if (tb.TableName.equals("Track"))
					dt = tb;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//DataView dv = dt.DefaultView;
		//dv.Sort = "RDT";

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		gwf.RetrieveFromDBSources();

		String currNodeID = "0";
		if (gwf.getWFState() != WFState.Complete)
		{
			currNodeID = String.valueOf(gwf.getFK_Node()); //获得当前运行到的节点如果流程完成则为O.
		}

		int idx = 1;
		String checkStr = "";
		
		String str = ""; //拼接字符串
		str += "<div class='content'>";
		str += "<div class='wrapper'>";
		str += "<div class='main'>";

		str += "<h1 class='title'>";
		str += "流程日志</h1>";
		str += "<div class='year'>";
		str += "<h2>";
		str += "<a href='#'>&nbsp;&nbsp;时间轴<i></i></a></h2>";
		str += "<div class='list'>";
		str += "<ul>";

		for (DataRow dr : dt.Rows) {
			long fid = Integer.parseInt(dr.getValue("FID").toString());
			if (fid != 0) {
				continue;
			}

			ActionType at = ActionType.forValue(Integer.parseInt(dr.getValue(TrackAttr.ActionType).toString()));
			//如果是协作发送，就不输出他. edit 2016.02.20 .
			if (at == ActionType.TeampUp) {
				continue;
			}
			// 记录审核节点。
			if (at == ActionType.WorkCheck) {
				checkStr = dr.getValue(TrackAttr.NDFrom).toString(); // 记录当前的审核节点id.
			}

			// 审核信息过滤,
			if (at == ActionType.WorkCheck) {
				if (currNodeID.equals(checkStr)) {
					continue;
				}
				// 如果当前节点与审核信息节点一致，就说明当前人员的审核意见已经保存，但是工作还没有发送,就不让他显示。
			}

			if (at == ActionType.Forward) {
				if (dr.getValue(TrackAttr.NDFrom).toString().equals(checkStr)) {
					continue;
				}
			}
			str += "<li  class='cls highlight' style=\"background: url('../../Img/Action/" + at.toString() + ".png') no-repeat 235px 31px\">";
			
			//Pub1.append(this.AddTR());
			//Pub1.append(this.AddTDIdx(idx));
			java.util.Date dtt = DataType.ParseSysDateTime2DateTime(dr.getValue(TrackAttr.RDT).toString());
			java.text.DateFormat df = new java.text.SimpleDateFormat("yy年MM月dd日 HH:mm");
			SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
			str += "<p class='date'>" + df.format(dtt) ;
			str += "</br>" + dateFm.format(dtt);
			str += "</br>" + BP.WF.Glo.GenerUserImgHtml(dr.getValue(TrackAttr.EmpFrom).toString(), dr.getValue(TrackAttr.EmpFromT).toString()) + "</p>";
			str += "<p class='intro'>" + dr.getValue(TrackAttr.NDFromT).toString() + "</p>";
			str += "<div class='more'>";

			
			
			//Pub1.append(this.AddTD(df.format(dtt)));

			if (at == ActionType.Forward || at == ActionType.ForwardAskfor
					|| at == ActionType.WorkCheck || at == ActionType.Order
					|| at == ActionType.FlowOver  || at == ActionType.Skip) // added by
													// liuxc,2014-12-3,正常结束结点也显示表单
			{
				/*Pub1.append(this
						.AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-sheet'\" href=\"javascript:WinOpen('"
								+ BP.WF.Glo.getCCFlowAppPath()
								+ "WF/WFRpt.jsp?WorkID="
								+ dr.getValue(TrackAttr.WorkID).toString()
								+ "&FK_Flow="
								+ this.getFK_Flow()
								+ "&FK_Node="
								+ dr.getValue(TrackAttr.NDFrom).toString()
								+ "&DoType=View&MyPK="
								+ dr.getValue(TrackAttr.MyPK).toString()
								+ "','"
								+ dr.getValue(TrackAttr.MyPK).toString()
								+ "');\">表单</a>"));*/
				
				
				//str += "<p><a class='easyui-linkbutton' data-options=\"iconCls:'icon-sheet'\" href=\"javascript:WinOpen('" + BP.WF.Glo.getCCFlowAppPath() + "WF/WFRpt.jsp?WorkID=" + dr.getValue(TrackAttr.WorkID).toString() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + dr.getValue(TrackAttr.NDFrom).toString() + "&DoType=View&MyPK=" + dr.getValue(TrackAttr.MyPK).toString() + "','" + dr.getValue(TrackAttr.MyPK).toString() + "');\">打开<img src='../../Img/Form.png'>表单</a>" + "</p>";
				str += "<p><a href=\"javascript:WinOpen('" + BP.WF.Glo.getCCFlowAppPath() + "WF/WFRpt.jsp?WorkID=" + dr.getValue(TrackAttr.WorkID).toString() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + dr.getValue(TrackAttr.NDFrom).toString() + "&DoType=View&MyPK=" + dr.getValue(TrackAttr.MyPK).toString() + "','" + dr.getValue(TrackAttr.MyPK).toString() + "');\">打开<img src='../../Img/Form.png'>表单</a>" + "</p>";
			} 
			/*else {
				Pub1.append(this.AddTD(""));
			}

			Pub1.append(this.AddTD(dr.getValue(TrackAttr.NDFromT).toString()));
			Pub1.append(this.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(dr
					.getValue(TrackAttr.EmpFrom).toString(),
					dr.getValue(TrackAttr.EmpFromT).toString())));*/

			if (at == ActionType.FlowOver || at == ActionType.CC
					|| at == ActionType.UnSend) {
				/*Pub1.append(this.AddTD());
				Pub1.append(this.AddTD());*/
				str += "<p></p>";
				str += "<p></p>";
			} else {
				/*Pub1.append(this.AddTD(dr.getValue(TrackAttr.NDToT).toString()));
				Pub1.append(this
						.AddTD(dr.getValue(TrackAttr.EmpToT).toString()));*/
				str += "<p>发送到节点：" + dr.getValue(TrackAttr.NDToT).toString() + "</p>";

			}

			// 增加两列，到达时间、用时 added by liuxc,2014-12-4
			if (idx > 1) {
				java.text.DateFormat dfstart = new java.text.SimpleDateFormat("yy-MM-dd HH:mm");
				Date dtStart = null;
				try {
					dtStart = dfstart.parse(dt.getValue(idx - 1 - 1,TrackAttr.RDT).toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				java.text.DateFormat dfs = new java.text.SimpleDateFormat("yy年MM月dd日HH:mm");
				str += "<p>到达时间：" + dfs.format(dtStart)+ " 用时：" + DataType.GetSpanTime(dtStart, dtt) + "</p>";
				/*Pub1.append(this.AddTD(dfs.format(dtStart)));
				Pub1.append(this.AddTD(DataType.GetSpanTime(dtStart, dtt)));*/
			}
			/*else {
				Pub1.append(this.AddTD());
				Pub1.append(this.AddTD());
			}*/

			/*Pub1.append(this.AddTD("<img src='../../Img/Action/"
					+ at.toString() + ".png' class='ActionType' border=0/>"
					+ Track.GetActionTypeT(at)));*/

			// 删除信息.
			String tag = dr.getValue(TrackAttr.Tag) == null ? null : dr
					.getValue(TrackAttr.Tag).toString();
			if (tag != null) {
				tag = tag.replace("~", "'");
			}

			String msg = dr.getValue(TrackAttr.Msg) == null ? "" : dr.getValue(
					TrackAttr.Msg).toString();
			switch (at) {
			case CallChildenFlow: // 被调用父流程吊起。
				if (!StringHelper.isNullOrEmpty(tag)) {
					/*AtPara ap = new AtPara(tag);
					Pub1.append(this.AddTD(
							"class=TD",
							"<a target=b" + ap.GetValStrByKey("PWorkID")
									+ " href='Track.jsp?WorkID="
									+ ap.GetValStrByKey("PWorkID")
									+ "&FK_Flow="
									+ ap.GetValStrByKey("PFlowNo") + "' >"
									+ msg + "</a>"));
				} else {
					Pub1.append(this.AddTD("class=TD", msg));
				}
				*/
				
					AtPara ap = new AtPara(tag);
					GenerWorkFlow mygwf = new GenerWorkFlow();
					mygwf.setWorkID(ap.GetValInt64ByKey("PWorkID"));
					if (mygwf.RetrieveFromDBSources() == 1) {
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上，被父流程{" + mygwf.getFlowName() + "},<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.jsp?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a></p>";
					}
					else {
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上，被父流程调用{" + mygwf.getFlowName() + "}，但是该流程被删除了.</p>" + tag;
					}
	
					msg = "<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.jsp?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a>";
				}
				break;
			case StartChildenFlow: // 吊起子流程。
				if (!StringHelper.isNullOrEmpty(tag)) {
					/*AtPara ap = new AtPara(tag);
					Pub1.append(this.AddTD(
							"class=TD",
							"<a target=b" + ap.GetValStrByKey("CWorkID")
									+ " href='Track.jsp?WorkID="
									+ ap.GetValStrByKey("CWorkID")
									+ "&FK_Flow="
									+ ap.GetValStrByKey("CFlowNo") + "' >"
									+ msg + "</a>"));
				} else {
					Pub1.append(this.AddTD("class=TD", msg));
				}*/
				
					if (tag.contains("Sub")) {
						tag = tag.replace("Sub", "C");
					}
					AtPara ap = new AtPara(tag);
					GenerWorkFlow mygwf = new GenerWorkFlow();
					mygwf.setWorkID(ap.GetValInt64ByKey("PWorkID"));
					if (mygwf.RetrieveFromDBSources() == 1) {
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上调用了子流程{" + mygwf.getFlowName() + "}, <a target=b" + ap.GetValStrByKey("CWorkID") + " href='Track.aspx?WorkID=" + ap.GetValStrByKey("CWorkID") + "&FK_Flow=" + ap.GetValStrByKey("CFlowNo") + "' >" + msg + "</a></p>";
						msg += "<p>当前子流程状态：{" + mygwf.getWFStateText()+ "}，运转到:{" + mygwf.getNodeName() + "}，最后处理人{" + mygwf.getTodoEmps() + "}，最后处理时间{" + mygwf.getRDT() + "}。</p>";
					}
					else {
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上调用了子流程{" + mygwf.getFlowName() + "}，但是该流程被删除了.</p>" + tag;
					}
				}
				break;
			default:
				//Pub1.append(this.AddTD(DataType.ParseText2Html(msg)));
				break;
			}

			/*Pub1.append(this.AddTD(dr.getValue(TrackAttr.Exer).toString()));
			Pub1.append(this.AddTREnd());*/
			msg = msg.replace("\"", "");
			str += "<p>" + msg + "</p>";
			str += "</div>";
			str += "</li>";
			idx++;
		}
		//Pub1.append(this.AddTableEnd());
		//判断当前工作是否完成，如果没有完成，就输出当前人员的待办信息 ，读取信息。

		if (gwf.getWFState() == WFState.Runing) {
			GenerWorkerLists gwls = new GenerWorkerLists(gwf.getWorkID(), gwf.getFK_Node());
			Node nd = new Node(gwf.getFK_Node());

			str += "<li  class='cls highlight' style=\"background: url('../../Img/Action/Todolist.png') no-repeat 235px 31px\" >";

			str += "<BR><BR><p class='date'>流程运行到: " + gwf.getNodeName() + "。";

			if (gwf.getTodoEmpsNum() == 1) {
				String myemp = gwf.getTodoEmps();
				myemp = myemp.replace("(", "");
				myemp = myemp.replace(")", "");
				String[] strs = myemp.split("[,]", -1);
				str += "<br>" + BP.WF.Glo.GenerUserImgHtml(strs[0], strs[1]) + "。</p>";
				
				str += "<div class='more'> ";
				for(int i=0;i<gwls.size();i++){
					GenerWorkerList swl =(GenerWorkerList) gwls.get(i);
					if(swl.getIsRead()){
						str +="<p style='margin-left: 2px' class='intro'>处理人："+swl.getFK_DeptT()+"("+swl.getFK_EmpText()+")已读</p>";
					}else{
						str +="<p style='margin-left: 2px' class='intro'>处理人："+swl.getFK_DeptT()+"("+swl.getFK_EmpText()+")未读</p>";
					}
				}
				
				str +="</div><br><br>";
			}
			else {
				str += "<br>处理人员:(" + gwf.getTodoEmps() + ")计(" + gwf.getTodoEmpsNum() + ")人。</p><br>";
				str += "<div class='more'> ";
				for(int i=0;i<gwls.size();i++){
					GenerWorkerList swl =(GenerWorkerList) gwls.get(i);
					if(swl.getIsRead()){
						if(swl.getIsPass()){ // 如果已读并且已经通过（标识处理完成）
							str +="<p style='margin-left: 2px' class='intro'>处理人："+swl.getFK_DeptT()+"("+swl.getFK_EmpText()+")已处理</p><br>";
						}else{
							str +="<p style='margin-left: 2px' class='intro'>处理人："+swl.getFK_DeptT()+"("+swl.getFK_EmpText()+")已读</p><br>";
						}
						
					}else{
						str +="<p style='margin-left: 2px' class='intro'>处理人："+swl.getFK_DeptT()+"("+swl.getFK_EmpText()+")未读</p><br>";
					}
				}
				str +="</div><br><br>";
			}

			
			
			//str += "<div></div><br><br>";原
			
			if (nd.getHisNodeWorkType() == NodeWorkType.WorkFL || nd.getHisNodeWorkType() == NodeWorkType.StartWorkFL || nd.getHisNodeWorkType() == NodeWorkType.WorkFHL) {
				str += "<div><ul>";
				for(int i=0;i<gwls.size();i++){
					GenerWorkerList item=(GenerWorkerList) gwls.get(i);
				//for (GenerWorkerList item : gwls.ToJavaList()) {
					if (gwls.size() == 0 || nd.getTodolistModel() == TodolistModel.QiangBan || nd.getTodolistModel() == TodolistModel.Sharing) {
						if (item.getIsRead() == false) {
							str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp; <font style='color:#FFFFFF; background-color:#00CC66' >未读</font></li>";
						}
						else {
							str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#FF9966' ><strong>已读</strong></font></li>";
						}
						break;
					}

					switch (nd.getTodolistModel()) {
						case QiangBan:
							str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#FF9966' ><strong>已读</strong></font></li>";
							break;
						case Order:
							if (item.getIsPassInt() == 1) {
								str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#FF9966' ><strong>已处理</strong></font></li>";
							}
							else {
								if (item.getIsRead() == false) {
									str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp; <font style='color:#FFFFFF; background-color:#00CC66' >未读</font></li>";
								}
								else {
									str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#FF9966' ><strong>已读</strong></font></li>";
								}
							}
							break;
						case Sharing:
							str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#FF9966' ><strong>已读</strong></font></li>";
							break;
						case Teamup:
							if (item.getIsPassInt() == 1) {
								str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#999966' ><strong>已处理</strong></font></li>";
							}
							else {
								if (item.getIsRead() == false) {
									str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp; <font style='color:#FFFFFF; background-color:#00CC66' >未读</font></li>";
								}
								else {
									str += "<li><img src='../../Img/Dot.png' width='8px' >处理人:" + BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + "&nbsp;&nbsp;<font style='color:#FFFFFF; background-color:#FF9966' ><strong>已读</strong></font></li>";
								}
							}
							break;
						default:
							break;
					}
				}
			}

			str += "</ul>";
			str += "</div>";

			str += "<br>";
			str += "<br>";
			str += "<br>";
			str += "<br>";
			str += "<br>";
			str += "<br>";
			str += "<br>";

			str += "</li>";
		}

		if (this.getCCID() != null) {
			CCList cl = new CCList();
			cl.setMyPK(this.getCCID());
			cl.RetrieveFromDBSources();
			/*Pub1.append(this.AddFieldSet(cl.getTitle()));
			Pub1.append("抄送人:" + cl.getRec() + ", 抄送日期:" + cl.getRDT());
			Pub1.append(this.AddHR());
			Pub1.append(cl.getDocHtml());
			Pub1.append(this.AddFieldSetEnd());*/
			str += "<li  class='cls highlight' style=\"background: url('./Img/Action/circle.png') no-repeat 235px 31px\">";
			str += "<p class='date'>&nbsp;</p>";
			str += "<p class='intro'>" + cl.getTitle() + "</p>";
			str += "<p class='version'>&nbsp;</p>";
			str += "<div class='more'>";
			str += "<p>抄送人:" + cl.getRec() + "</p>";
			str += "<p>抄送日期:" + cl.getRDT() + "</p>";
			str += "</div>";
			str += "</li>";

			if (cl.getHisSta() == CCSta.UnRead) {
				cl.setHisSta(CCSta.Read);
				cl.Update();
			}
		}
		str += "</ul>";
		str += "</div>";
		str += "</div>";
		str += "</div>";
		str += "</div>";
		str += "</div>";
		Pub1.append(str.toString());
		//HiddenField1.setValue(str.toString());

	}


	/** 
	 分流支流
	 
	 @param fl
	*/
//	public final void BindBrach(Flow fl)
//	{
//		//  WorkFlow wf = new WorkFlow(fl, this.WorkID, this.FID);
//		WorkNodes wns = new WorkNodes();
//		wns.GenerByFID(fl, this.getFID());
//
//		Pub1.append(this.AddH4("关于（" + fl.getName() + "）工作报告");
//		Pub1.append(this.AddHR();
//
//		Node nd = fl.HisStartNode;
//
//		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
//		Pub1.append(this.Add("流程发起人：" + gwf.StarterName + "，发起日期：" + gwf.RDT + " ；流程状态：" + gwf.WFStateText);
//
//		ReturnWorks rws = new ReturnWorks();
//		rws.Retrieve(ReturnWorkAttr.WorkID, this.getWorkID());
//
//
//
//		//  this.BindWorkNodes(wns, rws, fws);
//
//		Pub1.append(this.AddHR("流程报告结束");
//	}

	/** 
	 合流干流
	 
	 @param fl
	*/
//	public final void BindHeLiuRavie(Flow fl)
//	{
//	}
	/** 
	 合流支流
	 
	 @param fl
	*/
//	public final void BindHeLiuBrach(Flow fl)
//	{
//	}


//	public final void BindFHLWork(GenerFH hf)
//	{
//		Pub1.append(this.AddH4(hf.Title);
//
//		Pub1.append(this.AddHR();
//		Pub1.append(this.AddFieldSet("当前节点基本信息");
//		Pub1.append(this.AddBR("接受时间：" + hf.RDT);
//		Pub1.append(this.AddBR("接受人：" + hf.ToEmpsMsg);
//		Pub1.append(this.AddFieldSetEndBR();
//
//		GenerWorkFlows gwfs = new GenerWorkFlows();
//		gwfs.Retrieve(GenerWorkFlowAttr.FID, this.getFID());
//
//		Pub1.append(this.AddFieldSet("分流人员信息");
//
//		Pub1.append(this.AddTable();
//		Pub1.append(this.AddTR();
//		Pub1.append(this.AddTDTitle("标题");
//		Pub1.append(this.AddTDTitle("发起人");
//		Pub1.append(this.AddTDTitle("发起日期");
//		Pub1.append(this.AddTDTitle("");
//		Pub1.append(this.AddTREnd();
//
//		for (GenerWorkFlow gwf : gwfs)
//		{
//			if (gwf.WorkID == this.getFID())
//			{
//				continue;
//			}
//
//			Pub1.append(this.AddTR();
//			Pub1.append(this.AddTD(gwf.Title);
//			Pub1.append(this.AddTD(gwf.Starter);
//			Pub1.append(this.AddTD(gwf.RDT);
//			Pub1.append(this.AddTD("<a href='" + this.Request.ApplicationPath + "WF/WFRpt.jsp?WorkID=" + gwf.WorkID + "&FK_Flow=" + gwf.FK_Flow + "&FID=" + gwf.FID + "' target=_b" + gwf.WorkID + ">工作报告</a>");
//			Pub1.append(this.AddTREnd();
//		}
//		Pub1.append(this.AddTableEndWithBR();
//		Pub1.append(this.AddFieldSetEnd();
//	}

//	protected final void AddContral(String desc, String text)
//	{
//		Pub1.append(this.Add("<td  class='FDesc' nowrap> " + desc + "</td>");
//		Pub1.append(this.Add("<td width='40%' class=TD>");
//		if (text.equals(""))
//		{
//			text = "&nbsp;";
//		}
//		Pub1.append(this.Add(text);
//		Pub1.append(this.AddTDEnd();
//	}
	/** 
	 增加一个工作
	 
	 @param en
	 @param rws
	 @param fws
	 @param nodeId
	*/
	public final void ADDWork(Work en, ReturnWorks rws, int nodeId)
	{
		Pub1.append(this.BindViewEn(en, "class='Table' cellpadding='0' cellspacing='0' border='0' style='width: 100%'"));
		for (ReturnWork rw : rws.ToJavaList())
		{
			if (rw.getReturnToNode() != nodeId)
			{
				continue;
			}

			Pub1.append(this.AddBR());

			AddEasyUiPanelInfo("退回信息", rw.getNoteHtml());
		}

		//foreach (ShiftWork fw in fws)
		//{
		//    if (fw.FK_Node != nodeId)
		//        continue;

		//    Pub1.append(this.AddBR();
		//    Pub1.append(this.AddMsgOfInfo("转发信息：", fw.NoteHtml);
		//}


		String refstrs = "";
		if (en.getIsEmpty())
		{
			refstrs += "";
			return;
		}

		String keys = "&PK=" + en.getPKVal().toString();
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.PKFK)
			{
				keys += "&" + attr.getKey() + "=" + en.GetValStrByKey(attr.getKey());
			}
		}
		Entities hisens = en.getGetNewEntities();

		///#region 加入他的明细
		EnDtls enDtls = en.getEnMap().getDtls();
		if (enDtls.size() > 0)
		{
			for (EnDtl enDtl : enDtls)
			{
				String url = "WFRptDtl.jsp?RefPK=" + en.getPKVal().toString() + "&EnName=" + enDtl.getEns().getGetNewEntity().toString();
				int i = 0;
				try
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
				}
				catch (java.lang.Exception e)
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
				}

				if (i == 0)
				{
					refstrs += "[<a href=\"javascript:WinOpen('" + url + "','u8');\" >" + enDtl.getDesc() + "</a>]";
				}
				else
				{
					refstrs += "[<a  href=\"javascript:WinOpen('" + url + "','u8');\" >" + enDtl.getDesc() + "-" + i + "</a>]";
				}
			}
		}

		///#region 加入一对多的实体编辑
		AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
		if (oneVsM.size() > 0)
		{
			for (AttrOfOneVSM vsM : oneVsM)
			{
				String url = "UIEn1ToM.jsp?EnsName=" + en.toString() + "&AttrKey=" + vsM.getEnsOfMM().toString() + keys;
				String sql = "SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'";
				int i = DBAccess.RunSQLReturnValInt(sql);

				if (i == 0)
				{
					refstrs += "[<a href='" + url + "' target='_blank' >" + vsM.getDesc() + "</a>]";
				}
				else
				{
					refstrs += "[<a href='" + url + "' target='_blank' >" + vsM.getDesc() + "-" + i + "</a>]";
				}
			}
		}
		///#endregion

		///#region 加入他门的相关功能
		//			SysUIEnsRefFuncs reffuncs = en.GetNewEntities.HisSysUIEnsRefFuncs ;
		//			if ( reffuncs.Count > 0  )
		//			{
		//				foreach(SysUIEnsRefFunc en1 in reffuncs)
		//				{
		//					string url="RefFuncLink.jsp?RefFuncOID="+en1.OID.ToString()+"&MainEnsName="+hisens.ToString()+keys;
		//					//int i=DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM "+vsM.EnsOfMM.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE "+vsM.AttrOfMInMM+"='"+en.getPKVal()+"'");
		//					refstrs+="[<a href='"+url+"' target='_blank' >"+en1.Name+"</a>]";
		//					//refstrs+="编辑: <a href=\"javascript:window.open(RefFuncLink.jsp?RefFuncOID="+en1.OID.ToString()+"&MainEnsName="+ens.ToString()+"'> )\" > "+en1.Name+"</a>";
		//					//var newWindow= window.open( this.Request.ApplicationPath+'/Comm/'+'RefFuncLink.jsp?RefFuncOID='+OID+'&MainEnsName='+ CurrEnsName +CurrKeys,'chosecol', 'width=100,top=400,left=400,height=50,scrollbars=yes,resizable=yes,toolbar=false,location=false' );
		//					//refstrs+="编辑: <a href=\"javascript:EnsRefFunc('"+en1.OID.ToString()+"')\" > "+en1.Name+"</a>";
		//					//refstrs+="编辑:"+en1.Name+"javascript: EnsRefFunc('"+en1.OID.ToString()+"',)";
		//					//Pub1.append(this.AddItem(en1.Name,"EnsRefFunc('"+en1.OID.ToString()+"')",en1.Icon);
		//				}
		//			}
		///#endregion

		// 不知道为什么去掉。
		Pub1.append(refstrs);
	}

}
