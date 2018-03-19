package cn.jflow.foolform.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.EditType;
import BP.En.EntitiesNoName;
import BP.En.FieldTypeS;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachments;
import BP.Sys.GroupCtrlType;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapFrame;
import BP.Sys.MapFrames;
import BP.Sys.MapM2Ms;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.Template.FTCSta;
import BP.WF.Template.FrmSubFlow;
import BP.WF.Template.FrmSubFlowSta;
import BP.WF.Template.FrmThread;
import BP.WF.Template.FrmThreadSta;
import BP.WF.Template.FrmTrack;
import BP.WF.Template.FrmTrackSta;
import BP.WF.Template.FrmTransferCustom;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Template.MapFoolForm;
import BP.Web.WebUser;
import cn.jflow.common.model.EnModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TB;
import cn.jflow.system.ui.core.TextBoxMode;

public class FoolFormDesingerModel extends EnModel{

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	public FoolFormDesingerModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		this.request = request;
		this.response = response;
	}

	public StringBuffer Pub1 = new StringBuffer() ;
	public MapFoolForm md = null;
	
	public void pageLoad()
	{

		md = new MapFoolForm(this.getFK_MapData());

		//如果是第一次进入，就执行旧版本的升级检查.
		if (this.getIsFirst() == true)
		{
			MapFoolForm cols = new MapFoolForm(this.getFK_MapData());
			cols.DoCheckFixFrmForUpdateVer();
			try {
				response.sendRedirect("Designer.jsp?FK_MapData=" + this.getFK_MapData() + "&FK_Flow=" + this.getFK_Flow() + "&MyPK=" + this.getMyPK() + "&IsEditMapData=" + this.getIsEditMapData()+"&FK_Node=" + this.getFK_Node());
			} catch (IOException e) {
				Log.DebugWriteError("执行旧版本的升级检查. err:"+e.getMessage());
			}
			return;
		}

		MapAttrs mattrs = new MapAttrs(md.getNo());
		int count = mattrs.size();

		this.Pub1.append(Add("<div style='width:" + md.getTableWidth() + "px;background-color:white;border:1px solid #666;padding:5px;margin:auto;' align='center'>"));
		this.Pub1.append(Add("\t\n<Table style='width:98%;border:1px;padding:5px; padding-top:11px;' >"));

		int myidx = 0;
		String src = "";

		//根据 GroupField 循环出现菜单。
		for (GroupField gf : getGfs().ToJavaList())
		{
			rowIdx = 0;
			String gfAttr = "";
			currGF = gf;
			///#region 首先判断是否是框架分组？
			if(GroupCtrlType.Frame.equals(gf.getCtrlType()))  // 框架 类型.
			{
				//#region 框架
				for (MapFrame fram : getframs().ToJavaList())
				{
					if (!fram.getMyPK().equals(gf.getCtrlID()))
					{
						continue;
					}

					fram.IsUse = true;
					myidx = rowIdx + 20;
					this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
					this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditFrame('" + this.getFK_MapData() + "','" + fram.getMyPK() + "')\" >" + fram.getName() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
					this.Pub1.append(AddTREnd());

					myidx++;
					this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
					this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TD" + fram.getMyPK() + "'  >"));

					src = fram.getURL();
					if (src.contains("?"))
					{
						src += "&FK_Node=" + this.getRefNo() + "&WorkID=" + this.getRefOID();
					}
					else
					{
						src += "?FK_Node=" + this.getRefNo() + "&WorkID=" + this.getRefOID();
					}

					this.Pub1.append(Add("<iframe ID='F" + fram.getMyPK() + "' frameborder=0 style='padding:0px;border:0px;width:100%;height:" + fram.getH() + "'  leftMargin='0'  topMargin='0' src='" + src + "'  scrolling='auto'  /></iframe>"));
					this.Pub1.append(AddTDEnd());
					this.Pub1.append(AddTREnd());
				}
			}else if(GroupCtrlType.Dtl.equals(gf.getCtrlType())) //增加从表.
			{
				for (MapDtl dtl : getdtls().ToJavaList())
				{
					if (!dtl.getNo().equals(gf.getCtrlID()))
					{
						continue;
					}

					dtl.IsUse = true;
					myidx = rowIdx + 10;

					this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
					this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " class=GroupField  ><div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditDtl('" + this.getFK_MapData() + "','" + dtl.getNo() + "')\" >" + dtl.getName() + "</a></div>  <div style='text-align:left; float:left'></div><div style='text-align:right; float:right'><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.AddF('" + dtl.getNo() + "');\"><img src='../../Img/Btn/New.gif' border=0/>插入列</a><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.AddFGroup('" + dtl.getNo() + "');\"><img src='../../Img/Btn/New.gif' border=0/>插入列组</a><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.CopyF('" + dtl.getNo() + "');\"><img src='../../Img/Btn/Copy.gif' border=0/>复制列</a>  <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div></td>"));
					this.Pub1.append(AddTREnd());

					myidx++;
					this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
					this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TD" + dtl.getNo() + "'   > "));
					src = "MapDtlDe.htm?DoType=Edit&FK_MapData=" + this.getFK_MapData() + "&FK_MapDtl=" + dtl.getNo();
					this.Pub1.append(Add("<iframe ID='F" + dtl.getNo() + "' frameborder=0 style='padding:0px;border:0px;width:100%;height:" + dtl.getH() + "px;'  leftMargin='0'  topMargin='0' src='" + src + "'  scrolling='auto' /></iframe>"));
					this.Pub1.append(AddTDEnd());
					this.Pub1.append(AddTREnd());
				}
			}else if(GroupCtrlType.Ath.equals(gf.getCtrlType()))  //增加附件.
			{
				for (FrmAttachment ath : getAths().ToJavaList())
				{
					if (!ath.getMyPK().equals(gf.getCtrlID()))
					{
						continue;
					}

					ath.IsUse = true;

					myidx = rowIdx + 10;

					this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
					this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditAth('" + this.getFK_MapData() + "','" + ath.getNoOfObj() + "')\" >" + ath.getName() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
					this.Pub1.append(AddTREnd());

					myidx++;
					this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
					this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TD" + ath.getMyPK() + "' height='" + ath.getH() + "px' width='100%' >"));

					src = "../../CCForm/AttachmentUpload.jsp?PKVal=0&Ath=" + ath.getNoOfObj() + "&FK_MapData=" + this.getFK_MapData() + "&FK_FrmAttachment=" + ath.getMyPK();

					this.Pub1.append(Add("<iframe ID='F" + ath.getMyPK() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='" + ath.getH() + "' scrolling=auto  /></iframe>"));

					this.Pub1.append(AddTDEnd());
					this.Pub1.append(AddTREnd());
				}
			}else if(GroupCtrlType.FWC.equals(gf.getCtrlType()))  //审核组件.
			{
				///#region 审核组件
				FrmWorkCheck fwc = new FrmWorkCheck(this.getFK_MapData());
				if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Disable)
				{
					gf.Delete();
					continue;
				}

				myidx = rowIdx + 10;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditFWC('" + fwc.getNodeID() + "')\" >" + fwc.getFWCLab() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
				this.Pub1.append(AddTREnd());

				myidx++;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TDFWC" + fwc.getNo() + "' height='" + fwc.getFWC_H() + "px' width='100%' >"));

				src = "NodeFrmComponents.aspx?DoType=FWC&FK_MapData=" + fwc.getNodeID();
				this.Pub1.append(Add("<iframe ID='F" + gf.getCtrlID() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='" + fwc.getFWC_H() + "px' scrolling=auto  /></iframe>"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
				//#endregion  审核组件
			}else if(GroupCtrlType.SubFlow.equals(gf.getCtrlType()))  //子流程..
			{
				//#region 子流程.
				FrmSubFlow subflow = new FrmSubFlow(this.getFK_MapData());
				if (subflow.getHisFrmSubFlowSta() == FrmSubFlowSta.Disable)
				{
					gf.Delete();
					continue;
				}

				myidx = rowIdx + 10;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='ImgSub" + subflow.getNodeID() + "'  border=0 /><a href=\"javascript:EditSubFlow('" + subflow.getNodeID() + "')\" >" + subflow.getSFLab() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
				this.Pub1.append(AddTREnd());

				myidx++;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TDFWC" + subflow.getNo() + "' height='" + subflow.getSF_H() + "px' width='100%' >"));

				src = "NodeFrmComponents.aspx?DoType=SubFlow&FK_MapData=" + subflow.getNodeID();
				this.Pub1.append(Add("<iframe ID='F" + gf.getCtrlID() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='" + subflow.getSF_H() + "px' scrolling=auto  /></iframe>"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			}else if(GroupCtrlType.Track.equals(gf.getCtrlType()))  // 轨迹图.
			{
				//#region 轨迹图.
				FrmTrack track = new FrmTrack(this.getFK_MapData());
				if (track.getFrmTrackSta() == FrmTrackSta.Disable)
				{
					gf.Delete();
					continue;
				}

				myidx = rowIdx + 10;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditTrack('" + track.getNodeID() + "')\" >" + track.getFrmTrackLab() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
				this.Pub1.append(AddTREnd());

				myidx++;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TDFWC" + track.getNo() + "' height='" + track.getFrmTrack_H() + "px' width='100%' >"));

				src = "NodeFrmComponents.aspx?DoType=FrmTrack&FK_MapData=" + track.getNodeID();
				this.Pub1.append(Add("<iframe ID='F" + gf.getCtrlID() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='" + track.getFrmTrack_H() + "px' scrolling=auto  /></iframe>"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
				//#endregion 轨迹图.
			}else if(GroupCtrlType.Thread.equals(gf.getCtrlType()))   //子线程.
			{
				//#region 子线程.
				FrmThread thread = new FrmThread(this.getFK_MapData());
				if (thread.getFrmThreadSta() == FrmThreadSta.Disable)
				{
					gf.Delete();
					continue;
				}

				myidx = rowIdx + 10;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditThread('" + thread.getNodeID() + "')\" >" + thread.getFrmThreadLab() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
				this.Pub1.append(AddTREnd());

				myidx++;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TDThread" + thread.getNo() + "' height='" + thread.getFrmThread_H() + "px' width='100%' >"));

				src = "NodeFrmComponents.aspx?DoType=FrmThread&FK_MapData=" + thread.getNodeID();
				this.Pub1.append(Add("<iframe ID='F" + gf.getCtrlID() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='" + thread.getFrmThread_H() + "px' scrolling=auto  /></iframe>"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			}else if(GroupCtrlType.FTC.equals(gf.getCtrlType()))   //流转自定义.
			{
				///#region 流转自定义.
				FrmTransferCustom ftc = new FrmTransferCustom(this.getFK_MapData());
				if (ftc.getFTCSta() == FTCSta.Disable)
				{
					gf.Delete();
					continue;
				}

				myidx = rowIdx + 10;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 /><a href=\"javascript:EditFTC('" + ftc.getNodeID() + "')\" >" + ftc.getFTCLab() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
				this.Pub1.append(AddTREnd());

				myidx++;
				this.Pub1.append(AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
				this.Pub1.append(Add("<TD colspan=" + md.getTableCol() + " ID='TDFTC" + ftc.getNo() + "' height='" + ftc.getFTC_H() + "px' width='100%' >"));

				src = "NodeFrmComponents.aspx?DoType=FrmFTC&FK_MapData=" + ftc.getNodeID();
				this.Pub1.append(Add("<iframe ID='F" + gf.getCtrlID() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='" + ftc.getFTC_H() + "px' scrolling=auto  /></iframe>"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
				//#endregion 流转自定义.
			}else{
				//#region 输出分组栏.
				this.Pub1.append(AddTR(gfAttr));
				if (getGfs().size() == 1)
				{
					this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top' align:left style='height: 24px;align:left' ", "<div style='text-align:left; float:left'>&nbsp;<a href=\"javascript:GroupField('" + this.getFK_MapData() + "','" + gf.getOID() + "')\" >" + gf.getLab() + "</a></div><div style='text-align:right; float:right'></div>"));
				}
				else
				{
					this.Pub1.append(AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='height: 24px;align:left' ", "<div style='text-align:left; float:left'><img src='../../MapDef/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 />&nbsp;<a href=\"javascript:GroupField('" + this.getFK_MapData() + "','" + gf.getOID() + "')\" >" + gf.getLab() + "</a></div> <div style='text-align:right; float:right'>  <a href=\"javascript:AddField('" + gf.getEnName() + "','" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-new',plain:true\"> </a>  <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-up',plain:true\"> </a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-down',plain:true\"> </a></div>"));
				}
				this.Pub1.append(AddTREnd());
				//#endregion 输出分组栏.
			}
			//#endregion

			idx = 0; // 设置字段的顺序号为0.

			//是否是沾满的状态.
			boolean isLeft = true;
			for (int i = 0; i < mattrs.size(); i++)
			{
				MapAttr attr = (MapAttr)((mattrs.get(i) instanceof MapAttr) ? mattrs.get(i) : null);

				//#region 过滤不需要显示的字段.
				if (attr.getGroupID() == 0)
				{
					attr.Update(MapAttrAttr.GroupID, gf.getOID());
				}

				if (attr.getGroupID() != (int)gf.getOID())
				{
					if (gf.getIdx() == 0 && attr.getGroupID() == 0)
					{
					}
					else
					{
						continue;
					}
				}
				if (attr.getHisAttr().getIsRefAttr() || attr.getUIVisible() == false)
				{
					continue;
				}
				//#endregion 过滤不需要显示的字段.

				//以下就是一列标签一列控件的方式展现了.
				TB tb = new TB();
				tb.attributes.put("width","100%");
				tb.setId("TB_" + attr.getKeyOfEn());
				tb.attributes.put("style","width:100%;height:100%;padding: 0px;margin: 0px;");

				//#region AppString .
				if (attr.getMyDataType() == DataType.AppString && attr.getLGType() != FieldTypeS.FK)
				{
					//#region 如果是1-2行, 就让其显示 大眼睛方式..
					if (attr.getUIRows() > 1 && (attr.getColSpan() == 2 || attr.getColSpan() == 1))
					{
						if (isLeft == true)
						{
							this.Pub1.append(AddTR());
						}

						/*是大块文本，并且跨度在占领了整个剩余行单元格. */
						this.Pub1.append(Add("<TD colspan=2 width='50%' height='" + attr.getUIHeight() + "px' >"));
						this.Pub1.append(Add("<span style='float:left'>" + this.GenerLab(attr, 0, count) + "</span>"));
						this.Pub1.append(Add("<span style='float:right'>"));
						Label lab = new Label();
						lab.setId("Lab" + attr.getKeyOfEn());
						this.Pub1.append(Add(lab));
						this.Pub1.append(Add("</span><br>"));


						//加入文本框.
						tb = new TB();
						tb.setId("TB_" + attr.getKeyOfEn());
						tb.setTextMode(TextBoxMode.MultiLine);
						tb.attributes.put("style","width:100%;height:100%;padding: 0px;margin: 0px;");
						tb.setEnabled(attr.getUIIsEnable());
						this.Pub1.append(Add(tb));
						//lab.setText("<a href=\"javascript:TBHelp('" + tb.getClientID() + "','" + SystemConfig.getCCFlowWebPath() + "','" + attr.getKeyOfEn() + "','" + md.getNo() + "')\">默认值</a>");
						lab.setText("<a href=\"javascript:TBHelp('" + SystemConfig.getCCFlowWebPath() + "','" + attr.getKeyOfEn() + "','" + md.getNo() + "')\">默认值</a>");

						this.Pub1.append(AddTDEnd());

						if (isLeft == false)
						{
							this.Pub1.append(AddTREnd());
						}
						isLeft = !isLeft;
						continue;
					}
					//#endregion 如果是1-2行, 就让其显示 大眼睛方式.

					//#region 如果是3行 就是独眼龙方式.
					if (attr.getUIRows() > 1 && attr.getColSpan() == 3)
					{
						if (isLeft == false)
						{
							this.Pub1.append(AddTD("colspan=2", ""));
							this.Pub1.append(AddTREnd());
							isLeft = true;
						}

						this.Pub1.append(AddTR());
						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));

						/*是大块文本，并且跨度在占领了整个剩余行单元格. */
						this.Pub1.append(Add("<TD colspan=3 width='100%' height='" + attr.getUIHeight() + "px' >"));
						this.Pub1.append(Add("<span style='float:right' height='" + attr.getUIHeight() + "px'  >"));
						Label lab = new Label();
						lab.setId("Lab" + attr.getKeyOfEn());
						String ctlID = tb.getId();
						lab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + SystemConfig.getPathOfWebApp() + "','" + attr.getKeyOfEn() + "','" + md.getNo() + "')\">默认值</a>");
						this.Pub1.append(Add(lab));
						this.Pub1.append(Add("</span><br>"));

						tb = new TB();
						tb.setId("TB_" + attr.getKeyOfEn());
						tb.setTextMode(TextBoxMode.MultiLine);
						tb.attributes.put("style","width:100%;height:100%;padding: 0px;margin: 0px;");
						tb.setEnabled(attr.getUIIsEnable());
						this.Pub1.append(Add(tb));
						tb.attributes.put("width","100%");
						//lab = this.Pub1.append(GetLabelByID("Lab" + attr.getKeyOfEn()));
						UiFatory ui  = new UiFatory();
						lab = ui.creatLabel("Lab" + attr.getKeyOfEn());
						//String ctlID = tb.getId();
						//lab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + SystemConfig.getPathOfWebApp() + "','" + attr.getKeyOfEn() + "','" + md.getNo() + "')\">默认值</a>");
						this.Pub1.append(AddTDEnd());
						this.Pub1.append(AddTREnd());
						continue;
					}
					//#endregion

					//#region 如果是4行 大块文本输出.
					if (attr.getColSpan() == 4)
					{
						if (isLeft == false)
						{
							this.Pub1.append(AddTD("colspan=2", ""));
							this.Pub1.append(AddTREnd());
							isLeft = true;
						}

						this.Pub1.append(AddTR());
						/*是大块文本，并且跨度在占领了整个剩余行单元格. */
						this.Pub1.append(Add("<TD colspan=4  height='" + attr.getUIHeight() + "px' >"));
						this.Pub1.append(Add("<span style='float:left' height='" + attr.getUIHeight() + "px' >" + this.GenerLab(attr, 0, count) + "</span>"));
						this.Pub1.append(Add("<span style='float:right' height='" + attr.getUIHeight() + "px'  >"));

						Label lab = new Label();
						lab.setId("Lab" + attr.getKeyOfEn());
						String ctlID = tb.getId();
						lab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + SystemConfig.getPathOfWebApp() + "','" + attr.getKeyOfEn() + "','" + md.getNo() + "')\">默认值</a>");
						this.Pub1.append(Add(lab));
						this.Pub1.append(Add("</span><br>"));

						tb = new TB();
						tb.setId("TB_" + attr.getKeyOfEn());
						tb.setTextMode(TextBoxMode.MultiLine);
						tb.attributes.put("style","width:100%;height:100%;padding: 0px;margin: 0px;");
						tb.setEnabled(attr.getUIIsEnable());
						this.Pub1.append(Add(tb));
						tb.attributes.put("width","100%");
						//lab = this.Pub1.append(GetLabelByID("Lab" + attr.getKeyOfEn());
						UiFatory ui  = new UiFatory();
						lab  = ui.creatLabel("Lab" + attr.getKeyOfEn());
						//String ctlID = tb.getId();
						//lab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + SystemConfig.getPathOfWebApp() + "','" + attr.getKeyOfEn() + "','" + md.getNo() + "')\">默认值</a>");
						this.Pub1.append(AddTDEnd());
						this.Pub1.append(AddTREnd());
						continue;
					}
					//#endregion 大块文本的输出.

					//#region 整行输出.
					if (attr.getColSpan() == 3)
					{
						if (isLeft == false)
						{
							this.Pub1.append(AddTD());
							this.Pub1.append(AddTREnd());
							isLeft = true;
						}

						this.Pub1.append(AddTR());
						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));

						//外键字段的输出.
						if (attr.getUIContralType() == UIContralType.DDL)
						{
							DDL ddl = new DDL();
							if (attr.getUIIsEnable() == false)
							{
								ddl.setEnabled(attr.getUIIsEnable());
								ddl.Items.add(new ListItem("数据1", "0"));
							}
							else
							{
								ddl.Bind(attr.getHisDT(), "No", "Name", attr.getDefVal());
							}
							this.Pub1.append(AddTD("colspan=3", ddl));
						}

						//普通文本的输出.
						if (attr.getUIContralType() == UIContralType.TB)
						{
							tb.setShowType(TBType.TB);
							tb.setText(attr.getDefVal());

							if (attr.getUIIsEnable() == false)
							{
								tb.setCssClass("TBReadonly");
							}

							if (attr.getIsSigan())
							{
								this.Pub1.append(AddTD("colspan=3", "<img src='/DataUser/Siganture/" + WebUser.getNo() + ".jpg'  style='border:0px;Width:70px;' onerror=\"this.src='../../DataUser/Siganture/UnName.jpg'\"/>"));
							}
							else
							{
								this.Pub1.append(AddTD("colspan=3", tb));
							}
						}

						this.Pub1.append(AddTREnd());
						continue;
					}
					//#endregion 整行输出.

					//#region 单行输出.
					if (attr.getColSpan() == 1)
					{
						if (isLeft == true)
						{
							this.Pub1.append(AddTR());
						}

						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));

						// 外部字典输出.
						if (attr.getUIContralType() == UIContralType.DDL)
						{
							DDL ddl = new DDL();
							ddl.setId("DDL_" + attr.getKeyOfEn());

							if (attr.getUIIsEnable() == false)
							{
								ddl.setEnabled(attr.getUIIsEnable());
								ddl.Items.add(new ListItem("数据1", "0"));
							}
							else
							{
								ddl.Bind(attr.getHisDT(), "No", "Name", attr.getDefVal());
							}
							this.Pub1.append(AddTD("colspan=1", ddl));
						}

						// 文本输出.
						if (attr.getUIContralType() == UIContralType.TB)
						{
							tb.setShowType(TBType.TB);
							tb.setText(attr.getDefVal());

							if (attr.getUIIsEnable() == false)
							{
								tb.setCssClass("TBReadonly");
							}

							if (attr.getIsSigan())
							{
								this.Pub1.append(AddTD("colspan=1", "<img src='/DataUser/Siganture/" + WebUser.getNo() + ".jpg'  style='border:0px;Width:70px;' onerror=\"this.src='../../../DataUser/Siganture/UnName.jpg'\"/>"));
							}
							else
							{
								this.Pub1.append(AddTD("colspan=1", tb));
							}


						}
					}
					//#endregion 单行输出.
				}
				//#endregion AppString.

				//#region AppDate.
				if (attr.getMyDataType() == DataType.AppDate)
				{
					if (isLeft == true)
					{
						this.Pub1.append(AddTR());
					}

					this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
					TB tbD = new TB();
					tbD.setText(attr.getDefVal());
					if (attr.getUIIsEnable())
					{
						tbD.attributes.put("onfocus","WdatePicker();");
						tbD.attributes.put("class","TBcalendar");
					}
					else
					{
						tbD.setEnabled(false);
						tbD.setReadOnly(true);
						tbD.attributes.put("class","TBcalendar");
					}
					this.Pub1.append(AddTD(" colspan=1", tbD));

				}
				//#endregion AppDate.

				//#region AppDateTime.
				if (attr.getMyDataType() == DataType.AppDateTime)
				{
					if (isLeft == true)
					{
						this.Pub1.append(AddTR());
					}

					this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
					TB tbDT = new TB();
					tbDT.setText(attr.getDefVal());
					if (attr.getUIIsEnable())
					{
						tbDT.attributes.put("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
						tbDT.attributes.put("class","TBcalendar");
					}
					else
					{
						tbDT.setEnabled(false);
						tbDT.setReadOnly(true);
						tbDT.attributes.put("class","TBcalendar");
					}
					this.Pub1.append(AddTD("colspan=1", tbDT));
				}
				//#endregion AppDateTime.

				//#region AppBoolean.
				if (attr.getMyDataType() == DataType.AppBoolean)
				{
					CheckBox cb = new CheckBox();
					cb.setText(attr.getName());
					cb.setChecked(attr.getDefValOfBool());
					cb.setEnabled(attr.getUIIsEnable());
					cb.setId("CB_" + attr.getKeyOfEn());
					if (attr.getColSpan() == 4 || attr.getColSpan() == 3)
					{
						if (isLeft == false)
						{
							this.Pub1.append(AddTD("colspan=2", ""));
							this.Pub1.append(AddTREnd());
							isLeft = true;
						}
						this.Pub1.append(AddTR());
						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
						this.Pub1.append(AddTD(" colspan=3", cb));
						this.Pub1.append(AddTREnd());
						continue;
					}
					else
					{
						if (isLeft == true)
						{
							this.Pub1.append(AddTR());
						}
						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
						this.Pub1.append(AddTD(" colspan=1 ", cb));
					}
				}
				//#endregion AppBoolean.

				//#region AppInt Enum
				if (attr.getMyDataType() == DataType.AppInt && attr.getLGType() == FieldTypeS.Enum)
				{
					if (attr.getUIContralType() == UIContralType.DDL)
					{
						if (isLeft == true)
						{
							this.Pub1.append(AddTR());
						}
						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
						DDL ddle = new DDL();
						ddle.setId("DDL_" + attr.getKeyOfEn());
						ddle.BindSysEnum(attr.getUIBindKey());
						ddle.SetSelectItem(attr.getDefVal());
						ddle.setEnabled(attr.getUIIsEnable());
						this.Pub1.append(AddTD("colspan=" + attr.getColSpan(), ddle));
					}

					if (attr.getUIContralType() == UIContralType.RadioBtn)
					{
						if (attr.getColSpan() == 1)
						{
							if (isLeft == true)
							{
								this.Pub1.append(AddTR());
							}

							this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
							this.Pub1.append(AddTDBegin());
							SysEnums ses = new SysEnums(attr.getUIBindKey());
							for (SysEnum item : ses.ToJavaList())
							{
								RadioButton rb = new RadioButton();
								rb.setId("RB_" + attr.getKeyOfEn() + "_" + item.getIntKey());
								rb.setText(item.getLab());
								if (String.valueOf(item.getIntKey()).equals(attr.getDefVal()))
								{
									rb.setChecked(true);
								}
								else
								{
									rb.setChecked(false);
								}
								rb.setGroupName(item.getEnumKey() + attr.getKeyOfEn());
								this.Pub1.append(Add(rb));
								if (attr.getRBShowModel() == 1)
								{
									this.Pub1.append(AddBR());
								}
							}
							this.Pub1.append(AddTDEnd());
						}

						if (attr.getColSpan() >= 3)
						{
							if (isLeft == false)
							{
								/*补充空白行.*/
								this.Pub1.append(AddTD());
								this.Pub1.append(AddTD());
								this.Pub1.append(AddTREnd());
								isLeft = true;
							}

							this.Pub1.append(AddTR());
							this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
							this.Pub1.append(AddTDBegin("colspan=3"));
							SysEnums ses = new SysEnums(attr.getUIBindKey());
							for (SysEnum item : ses.ToJavaList())
							{
								RadioButton rb = new RadioButton();
								rb.setId("RB_" + attr.getKeyOfEn() + "_" + item.getIntKey());
								rb.setText(item.getLab());
								if (String.valueOf(item.getIntKey()).equals(attr.getDefVal()))
								{
									rb.setChecked(true);
								}
								else
								{
									rb.setChecked(false);
								}
								rb.setGroupName(item.getEnumKey() + attr.getKeyOfEn());
								this.Pub1.append(Add(rb));
								if (attr.getRBShowModel() == 1)
								{
									this.Pub1.append(AddBR());
								}
							}
							this.Pub1.append(AddTDEnd());
							this.Pub1.append(AddTREnd());
							continue;
						}
					}

				}
				//#endregion AppInt Enum

				//#region AppDouble  AppFloat AppInt .
				if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat || (attr.getMyDataType() == DataType.AppInt && attr.getLGType() != FieldTypeS.Enum))
				{
					if (isLeft == true)
					{
						this.Pub1.append(AddTR());
					}

					this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
					tb.setShowType(TBType.Num);
					tb.setText(attr.getDefVal());
					if (attr.getIsNull())
					{
						tb.setText("");
					}
					this.Pub1.append(AddTD(" colspan=1", tb));
				}
				//#endregion AppDouble  AppFloat AppInt .

				//#region AppMoney  AppRate  .
				if (attr.getMyDataType() == DataType.AppMoney)
				{
					if (isLeft == true)
					{
						this.Pub1.append(AddTR());
					}

					this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
					tb.setShowType(TBType.Moneny);
					tb.setText(attr.getDefVal());
					if (attr.getIsNull())
					{
						tb.setText("");
					}
					this.Pub1.append(AddTD("colspan=1", tb));

				}
				//#endregion  AppMoney  AppRate .

				//#region FK 外键.
				if (attr.getLGType() == FieldTypeS.FK)
				{
					DDL ddlFK = new DDL();
					ddlFK.setId("DDL_" + attr.getKeyOfEn());

					EntitiesNoName ens = attr.getHisEntitiesNoName();
					if (ens == null)
					{
						ddlFK.Items.add(new ListItem("数据错误" + attr.getUIBindKey(),"xx"));
					}
					else
					{
						if (ens.size() == 0)
						{
							ens.RetrieveAll();
						}
						ddlFK.BindEntities(ens);
						ddlFK.SetSelectItem(attr.getDefVal());
					}
					ddlFK.setEnabled(attr.getUIIsEnable());

					if (attr.getColSpan() == 4 || attr.getColSpan() == 3)
					{
						if (isLeft == false)
						{
							this.Pub1.append(AddTD("colspan=2", ""));
							this.Pub1.append(AddTREnd());
							isLeft = true;
						}
						this.Pub1.append(AddTR());
						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
						this.Pub1.append(AddTD(" colspan=3", ddlFK));
						this.Pub1.append(AddTREnd());
						continue;
					}

					if (attr.getColSpan() == 1 || attr.getColSpan() == 2)
					{
						if (isLeft == true)
						{
							this.Pub1.append(AddTR());
						}

						this.Pub1.append(AddTDDesc(this.GenerLab(attr, i, count)));
						this.Pub1.append(AddTD("colspan=1", ddlFK));
					}
				}
				//#endregion FK 外键.

				if (isLeft == false)
				{
					this.Pub1.append(AddTREnd());
				}

				isLeft = !isLeft;

			} // end循环字段分组.

			if (isLeft == false)
			{
				this.Pub1.append(AddTD());
				this.Pub1.append(AddTD());
				this.Pub1.append(AddTREnd());
			}

		} // end循环分组.

				this.Pub1.append(AddTableEnd());
				this.Pub1.append(AddDivEnd());

				///#region 处理扩展信息。
				MapExts mes = new MapExts(this.getFK_MapData());
				/*if (mes.size() != 0)
				{
					String appPath = SystemConfig.getPathOfWebApp();

					this.Page.RegisterClientScriptBlock("s", "<script language='JavaScript' src='../Scripts/jquery-1.4.1.min.js' ></script>");

					this.Page.RegisterClientScriptBlock("b", "<script language='JavaScript' src='../CCForm/MapExt.js' defer='defer' type='text/javascript' ></script>");

					this.Page.RegisterClientScriptBlock("dC", "<script language='JavaScript' src='" + appPath + "DataUser/JSLibData/" + this.getFK_MapData() + ".js' ></script>");

					this.Pub1.append(Add("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>"));
				}*/

				for (MapExt me : mes.ToJavaList())
				{
					try {
						if(MapExtXmlList.DDLFullCtrl.equals(me.getExtType()))  // 自动填充.
						{
							this.Pub1.append(GetDDLByID("DDL_" + me.getAttrOfOper()));
							DDL ddlOper = GetDDLByID("DDL_" + me.getAttrOfOper());
							if (ddlOper == null)
							{
								continue;
							}
							ddlOper.attributes.put("onchange","DDLFullCtrl(this.value,\'" + ddlOper.getId() + "\', \'" + me.getMyPK() + "\')");
							
						}else if(MapExtXmlList.ActiveDDL.equals(me.getExtType()))
						{
							//DDL ddlPerant = this.Pub1.append(GetDDLByID("DDL_" + me.getAttrOfOper());
							//DDL ddlChild = this.Pub1.append(GetDDLByID("DDL_" + me.getAttrsOfActive());
							this.Pub1.append(GetDDLByID("DDL_" + me.getAttrOfOper()));
							this.Pub1.append(GetDDLByID("DDL_" + me.getAttrsOfActive()));
							DDL ddlPerant = GetDDLByID("DDL_" + me.getAttrOfOper());
							DDL ddlChild = GetDDLByID("DDL_" + me.getAttrsOfActive());
							if (ddlChild == null || ddlPerant == null)
							{
								me.Delete();
								continue;
							}

							ddlPerant.attributes.put("onch ange","DDLAnsc(this.value,\'" + ddlChild.getId() + "\', \'" + me.getMyPK() + "\')");
							
						}else if(MapExtXmlList.TBFullCtrl.equals(me.getExtType())) // 自动填充.
						{
							//TB tbAuto = this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper());
							this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper()));
							TB tbAuto = (TB) GetTBByID("TB_" + me.getAttrOfOper());
							if (tbAuto == null)
							{
								me.Delete();
								continue;
							}
							tbAuto.attributes.put("onkeyup","DoAnscToFillDiv(this,this.value,\'" + tbAuto.getId() + "\', \'" + me.getMyPK() + "\');");
							tbAuto.attributes.put("AUTOCOMPLETE","OFF");
						}else if(MapExtXmlList.InputCheck.equals(me.getExtType()))  //js 检查
						{
							//TB tbJS = this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper());
							this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper()));
							TB tbJS = (TB) GetTBByID("TB_" + me.getAttrOfOper());
							if (tbJS != null)
							{
								tbJS.attributes.put(me.getTag2(),tbJS.attributes.get(me.getTag2())+me.getTag1()+ "(this);");
								//tbJS.Attributes[me.getTag2()] += me.getTag1() + "(this);";
							}
							else
							{
								me.Delete();
							}
						}else if(MapExtXmlList.PopVal.equals(me.getExtType())) //弹出窗.
						{
							//TB tbPop = this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper());
							this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper()));
							TB tbPop = (TB) GetTBByID("TB_" + me.getAttrOfOper());
							tbPop.attributes.put("onclick","ShowHelpDiv('" + tbPop.getId() + "','','" + me.getMyPK() + "','111','returnvalccformpopval');");
							tbPop.attributes.put("ondblclick","ReturnValCCFormPopVal(this,'" + me.getMyPK() + "','111', " + me.getW() + "," + me.getH() + ",'" + me.GetParaString("Title") + "');");

						   // tbPop.Attributes["ondblclick"] = "ReturnVal(this,'" + me.Doc + "','sd');";
							
						}else if(MapExtXmlList.AutoFull.equals(me.getExtType())) //自动填充.
						{
							String js = "\t\n <script type='text/javascript' >";
							//TB tb = this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper());
							this.Pub1.append(GetTBByID("TB_" + me.getAttrOfOper()));
							TB tb = (TB) GetTBByID("TB_" + me.getAttrOfOper());
							if (tb == null)
							{
								continue;
							}

							String left = "\n  document.forms[0]." + tb.getId() + ".value = ";
							String right = me.getDoc();
							for (MapAttr mattr : mattrs.ToJavaList())
							{
								if (mattr.getIsNum() == false)
								{
									continue;
								}

								if (me.getDoc().contains("@" + mattr.getKeyOfEn()) || me.getDoc().contains("@" + mattr.getName()))
								{
								}
								else
								{
									continue;
								}

								String tbID = "TB_" + mattr.getKeyOfEn();
								//TB mytb = this.Pub1.append(GetTBByID(tbID));
								this.Pub1.append(GetTBByID(tbID));
								TB mytb = (TB) GetTBByID(tbID);
								if (mytb == null)
								{
									continue;
								}

								this.Pub1.append(GetTBByID(tbID).attributes.put("onkeyup","javascript:Auto" + me.getAttrOfOper() + "();"));
								right = right.replace("@" + mattr.getName(), " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
								right = right.replace("@" + mattr.getKeyOfEn(), " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
							}

							js += "\t\n function Auto" + me.getAttrOfOper() + "() { ";
							js += left + right + ";";
							js += " \t\n  document.forms[0]." + tb.getId() + ".value= VirtyMoney(document.forms[0]." + tb.getId() + ".value ) ;";
							js += "\t\n } ";
							js += "\t\n</script>";
							this.Pub1.append(Add(js));
						}
					} catch (Exception e) {
						Log.DebugWriteError("处理扩展信息。 err:"+e.getMessage());
					}
					
				}
				//#endregion 处理扩展信息。

				//#region 处理输入最小，最大验证.
				for (MapAttr attr : mattrs.ToJavaList())
				{
					if (attr.getMyDataType() != DataType.AppString || attr.getMinLen() == 0)
					{
						continue;
					}

					if (attr.getUIIsEnable() == false || attr.getUIVisible() == false)
					{
						continue;
					}

					try {
						this.Pub1.append(GetTBByID("TB_" + attr.getKeyOfEn()).attributes.put("onblur","checkLength(this,'" + attr.getMinLen() + "','" + attr.getMaxLen() + "')"));
					} catch (Exception e) {
						Log.DebugWriteError("处理输入最小，最大验证. err:"+e.getMessage());
					}
				}
				///#endregion 处理输入最小，最大验证.

				///#region 处理iFrom 的自适应的问题。
			   // string myjs = "\t\n<script type='text/javascript' >";
				//foreach (MapDtl dtl in dtls)
				//{
				//    myjs += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.No + "','TD" + dtl.No + "')\", 200);";
				//}

				//foreach (MapM2M M2M in dot2dots)
				//{
				//    if (M2M.ShowWay == FrmShowWay.FrmAutoSize)
				//        myjs += "\t\n window.setInterval(\"ReinitIframe('F" + M2M.NoOfObj + "','TD" + M2M.NoOfObj + "')\", 200);";
				//}
				//foreach (FrmAttachment ath in aths)
				//{
				//    if (ath.IsAutoSize)
				//        myjs += "\t\n window.setInterval(\"ReinitIframe('F" + ath.getMyPK() + "','TD" + ath.getMyPK() + "')\", 200);";
				//}
				//foreach (MapFrame fr in frams)
				//{
				//    myjs += "\t\n window.setInterval(\"ReinitIframe('F" + fr.getMyPK() + "','TD" + fr.getMyPK() + "')\", 200);";
				//}
				//myjs += "\t\n</script>";
				//this.Pub1.append(Add(myjs);
				///#endregion 处理iFrom 的自适应的问题。

				///#region 处理隐藏字段。
				DataTable dt = DBAccess.RunSQLReturnTable("SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "' AND GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.getFK_MapData() + "')");
				if (dt.Rows.size() != 0)
				{
					int gfid = getGfs().get(0).GetValIntByKey("OID");
					for (DataRow dr : dt.Rows)
					{
						DBAccess.RunSQL("UPDATE Sys_MapAttr SET GroupID=" + gfid + " WHERE MyPK='" + dr.get("MyPK") + "'");
					}

					try {
						this.getResponse().sendRedirect(getRequest().getContextPath() + getRequest().getServletPath() + getRequest().getQueryString());
					} catch (IOException e) {
						Log.DebugWriteError("处理隐藏字段。 err:"+e.getMessage());
					}
				}
				///#endregion 处理隐藏字段。
	}

	/** 
	 * 表单ID
	 */
	public final String getFK_MapData()
	{
		String key = getRequest().getParameter("FK_MapData");
		if (key == null)
		{
			key = getRequest().getParameter("PK");
		}
		if (key == null)
		{
			key = getRequest().getParameter("MyPK");
		}
		if (key == null)
		{
			key = "ND1601";
		}
		return key;
	}
	
	/** 
	 是不是第一次进入，如果是就需要执行一个检查方法。
	 */
	public final boolean getIsFirst()
	{
		if (getRequest().getParameter("IsFirst") == null)
		{
			return false;
		}
		return true;
	}

	/** 
	 IsEditMapData
	 */
	public final boolean getIsEditMapData()
	{
		String s = getRequest().getParameter("IsEditMapData");
		if (s == null || s.equals("1"))
		{
			return true;
		}
		return false;
	}
	
	private GroupFields _gfs;
	public final GroupFields getGfs()
	{
		if (_gfs == null)
		{
			_gfs = new GroupFields(this.getFK_MapData());
		}

		return _gfs;
	}
	private MapDtls _dtls;
	public final MapDtls getdtls()
	{
		if (_dtls == null)
		{
			_dtls = new MapDtls(this.getFK_MapData());
		}
		return _dtls;
	}
	private MapFrames _frams;
	public final MapFrames getframs()
	{
		if (_frams == null)
		{
			_frams = new MapFrames(this.getFK_MapData());
		}
		return _frams;
	}
	private MapM2Ms _dot2dots;
	public final MapM2Ms getdot2dots()
	{
		if (_dot2dots == null)
		{
			_dot2dots = new MapM2Ms(this.getFK_MapData());
		}
		return _dot2dots;
	}
	public int rowIdx = 0;
    public boolean isLeftNext = true;
    private FrmAttachments _aths;
	public final FrmAttachments getAths()
	{
		if (_aths == null)
		{
			_aths = new FrmAttachments(this.getFK_MapData());
		}
		return _aths;
	}

	public GroupField currGF = new GroupField();
	
	public final int getRefOID()
	{
		String s = getRequest().getParameter("RefOID");
		if (s == null)
		{
			s = getRequest().getParameter("OID");
		}
		if (s == null)
		{
			return 0;
		}
		return Integer.parseInt(s);
	}

	/** 
	 字段or控件的顺序号.
	*/
	public int idx = 0;
	
	/** 
	 * 属性
	 * @param attr
	 * @param idx
	 * @param i
	 * @param count
	 * @return 
	*/
	public final String GenerLab(MapAttr attr, int i, int count)
	{
		idx++;
		String divAttr = "";
		String lab = attr.getName();
		if (attr.getMyDataType() == DataType.AppBoolean && attr.getUIIsLine())
		{
			lab = "编辑";
		}

		String str = "";
		if (attr.getUIIsInput() == true)
		{
			str = "<font color=red><b>*</b></font>";
		}

		if (attr.getHisEditType() == EditType.Edit || attr.getHisEditType() == EditType.UnDel)
		{
			switch (attr.getLGType())
			{
				case Normal:
					if (attr.getUIContralType() == UIContralType.DDL)
					{
						lab = "<a  href=\"javascript:EditTable('" + this.getFK_MapData() + "','" + attr.getKeyOfEn() + "','" + attr.getMyPK() + "','" + attr.getUIBindKey() + "','" + attr.getGroupID() + "');\">" + lab + str + "</a>";
					}
					else
					{
						lab = "<a  href=\"javascript:Edit('" + this.getFK_MapData() + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "','" + attr.getGroupID() + "');\">" + lab + str + "</a>";
					}
					break;
				case FK:
					lab = "<a  href=\"javascript:EditTable('" + this.getFK_MapData() + "','" + attr.getKeyOfEn() + "','" + attr.getMyPK() + "','" + attr.getUIBindKey() + "','" + attr.getGroupID() + "');\">" + str + lab + str + "</a>";
					break;
				case Enum:
					lab = "<a  href=\"javascript:EditEnum('" + this.getFK_MapData() + "','" + attr.getKeyOfEn() + "','" + attr.getMyPK() + "','" + attr.getUIBindKey() + "','" + attr.getGroupID() + "');\">" + str + lab + str + "</a>";
					break;
				default:
					break;
			}
		}
		else
		{
			lab = attr.getName();
		}



		if (idx == 0)
		{
			/*第一个。*/
			return "<div " + divAttr + " >" + lab + "<a href=\"javascript:Down('" + this.getFK_MapData() + "','" + attr.getMyPK() + "','1');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-right',plain:true\" alt='向右动顺序'> </a></div>";
		}

		if (idx == count - 1)
		{
			/*到数第一个。*/
			return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.getFK_MapData() + "','" + attr.getMyPK() + "','1');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-left',plain:true\" alt='向左动顺序'> </a>" + lab + "</div>";
		}
		return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.getFK_MapData() + "','" + attr.getMyPK() + "','1');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-left',plain:true\" alt='向左动顺序'> </a>" + lab + "<a href=\"javascript:Down('" + this.getFK_MapData() + "','" + attr.getMyPK() + "','1');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-right',plain:true\" alt='向右动顺序'> </a></div>";
	}

}
