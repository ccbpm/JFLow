package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.AthCtrlWay;
import BP.Sys.AthSaveWay;
import BP.Sys.AthUploadWay;
import BP.Sys.AttachmentUploadType;
import BP.Sys.FileShowWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/mapdef")
public class AttachmentController {
	@RequestMapping(value = "/condByPara_btn_Click", method = RequestMethod.POST)
	public ModelAndView btn_Click(HttpServletRequest req,HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView();
		FrmAttachment ath = new FrmAttachment();
		String TB_Name=req.getParameter("TB_Name")==null?"":req.getParameter("TB_Name");
		int FK_Node = req.getParameter("FK_Node") == null ? 0 : Integer.parseInt(req.getParameter("FK_Node"));
		String FK_MapData = req.getParameter("FK_MapData") == null ? "test": req.getParameter("FK_MapData");
		String Ath = req.getParameter("Ath");
		String TB_FastKeyGenerRole = req.getParameter("TB_FastKeyGenerRole");
		String btnId = req.getParameter("btnId");
		// 下拉框选中的值
		int DDL_GroupField = Integer.parseInt(req.getParameter("DDL_GroupField"));
		/*int DDL_CtrlWay=0,DDL_AthUploadWay=0;
		if(ath.getIsNodeSheet()==true){
			DDL_CtrlWay = Integer.parseInt(req.getParameter("DDL_CtrlWay"));
			DDL_AthUploadWay = Integer.parseInt(req.getParameter("DDL_AthUploadWay"));
		}*/
		int DDL_FileShowWay = Integer.parseInt(req.getParameter("DDL_FileShowWay"));
		String UploadType = req.getParameter("UploadType");
		String TB_Sort=req.getParameter("TB_Sort")==null?"":req.getParameter("TB_Sort");
		String TB_Exts=req.getParameter("TB_Exts")==null?"":req.getParameter("TB_Exts");
		if (FK_Node == 0)
			ath.setMyPK(FK_MapData + "_" + Ath);
		else
			ath.setMyPK(FK_MapData + "_" + Ath + "_" + FK_Node);

		ath.RetrieveFromDBSources();

		if (btnId.equals("Btn_Delete")) {
			ath.Delete();
			mv.addObject("FK_Node", FK_Node);
			mv.addObject("FK_MapData", FK_MapData);
			mv.addObject("Ath", Ath);
			mv.addObject("success", "删除成功");
			mv.setViewName("MapDef/Attachment");
			return mv;
		}

		ath.setMyPK(FK_MapData + "_" + Ath);
		if (Ath != null)
			ath.RetrieveFromDBSources();
		
		
		
		ath.setFK_MapData(FK_MapData);
		ath.setFK_Node(FK_Node);
		if (StringHelper.isNullOrEmpty(Ath) == false)
			ath.setNoOfObj(Ath);

		if (FK_Node == 0)
			ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj());
		else
			ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj() + "_" + FK_Node);

		GroupFields gfs1 = new GroupFields(FK_MapData);
		if (gfs1.size() == 1) {
			GroupField gf = (GroupField) gfs1.get(0);
			ath.setGroupID((int) gf.getOID());
		} else {
			ath.setGroupID(DDL_GroupField);// =
		}

		// 对流程的特殊判断.
		
		if(ath.getIsNodeSheet()==true){
			int DDL_CtrlWay = Integer.parseInt(req.getParameter("DDL_CtrlWay"));
			int DDL_AthUploadWay = Integer.parseInt(req.getParameter("DDL_AthUploadWay"));
			int DDL_UploadCtrl = Integer.parseInt(req.getParameter("DDL_UploadCtrl"));
			int SaveWay = Integer.parseInt(req.getParameter("DDL_" + FrmAttachmentAttr.AthSaveWay));
			String IsHeLiuHuiZong = req.getParameter("CB_" + FrmAttachmentAttr.IsHeLiuHuiZong)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsHeLiuHuiZong);
			String IsToHeLiuHZ = req.getParameter("CB_" + FrmAttachmentAttr.IsToHeLiuHZ)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsToHeLiuHZ);
			ath.setHisCtrlWay(AthCtrlWay.forValue(DDL_CtrlWay));
			ath.setAthUploadWay(AthUploadWay.forValue(DDL_AthUploadWay));
			ath.setFileShowWay(FileShowWay.forValue(DDL_FileShowWay));//文件展现方式.
			ath.setUploadCtrl(DDL_UploadCtrl); //使用的附件上传工具.
			ath.setAthSaveWay(AthSaveWay.forValue(SaveWay) ); //保存方式.
			ath.setIsHeLiuHuiZong("on".equals(IsHeLiuHuiZong)?true:false); //是否是合流节点汇总.
			ath.setIsToHeLiuHZ("on".equals(IsToHeLiuHZ)?true:false);//是否汇总到合流节点..
		}
		
		
		String IsWoEnableWF= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableWF)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableWF);
		String IsWoEnableSave= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableSave)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableSave);
		String IsWoEnableReadonly= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableReadonly)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableReadonly);
		String IsWoEnableRevise= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableRevise)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableRevise);
		String IsWoEnableViewKeepMark= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark);
		String IsWoEnablePrint= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnablePrint)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnablePrint);
		String IsWoEnableSeal= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableSeal)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableSeal);
		String IsWoEnableOver= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableOver)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableOver);
		String IsWoEnableTemplete= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableTemplete)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableTemplete);
		String IsWoEnableCheck= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableCheck)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableCheck);
		String IsWoEnableInsertFengXian= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian);
		String IsWoEnableInsertFlow= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow);
		String IsWoEnableMarks= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableMarks)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableMarks);
		String IsWoEnableDown= req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableDown)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsWoEnableDown);
		ath.setIsWoEnableWF("on".equals(IsWoEnableWF)?true:false); 
		ath.setIsWoEnableSave("on".equals(IsWoEnableSave)?true:false);
		ath.setIsWoEnableReadonly("on".equals(IsWoEnableReadonly)?true:false);
		ath.setIsWoEnableRevise ("on".equals(IsWoEnableRevise)?true:false);
		ath.setIsWoEnableViewKeepMark ("on".equals(IsWoEnableViewKeepMark)?true:false);
		ath.setIsWoEnablePrint("on".equals(IsWoEnablePrint)?true:false);
		ath.setIsWoEnableSeal("on".equals(IsWoEnableSeal)?true:false);
		ath.setIsWoEnableOver("on".equals(IsWoEnableOver)?true:false);
		ath.setIsWoEnableTemplete("on".equals(IsWoEnableTemplete)?true:false);
		ath.setIsWoEnableCheck("on".equals(IsWoEnableCheck)?true:false);
		ath.setIsWoEnableInsertFengXian ("on".equals(IsWoEnableInsertFengXian)?true:false);
		ath.setIsWoEnableInsertFlow("on".equals(IsWoEnableInsertFlow)?true:false);
		ath.setIsWoEnableMarks("on".equals(IsWoEnableMarks)?true:false);
		ath.setIsWoEnableDown("on".equals(IsWoEnableDown)?true:false);

		
		String IsVisable= req.getParameter("CB_" + FrmAttachmentAttr.IsVisable)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsVisable);
		

		ath.setIsVisable("on".equals(IsVisable)?true:false);//是否可见.

		String FastKeyIsEnable= req.getParameter("CB_" + FrmAttachmentAttr.FastKeyIsEnable)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.FastKeyIsEnable);
		
		ath.setFastKeyIsEnable("on".equals(FastKeyIsEnable)?true:false);
		
		
		String FastKeyGenerRole= req.getParameter("TB_" + FrmAttachmentAttr.FastKeyGenerRole)== null ? "":req.getParameter("TB_" + FrmAttachmentAttr.FastKeyGenerRole);
		
		ath.setFastKeyGenerRole(FastKeyGenerRole);

		String IsOrder= req.getParameter("CB_" + FrmAttachmentAttr.IsOrder)== null ? "":req.getParameter("CB_" + FrmAttachmentAttr.IsOrder);
		
		ath.setIsOrder("on".equals(IsOrder)?true:false);



		ath.setExts(TB_Exts);
		ath.setSort(TB_Sort);
		String CB_IsAutoSize = req.getParameter("CB_IsAutoSize") == null ? ""
				: req.getParameter("CB_IsAutoSize");
		if (CB_IsAutoSize.equals("on")) {
			ath.setIsAutoSize(true);
		} else {
			ath.setIsAutoSize(false);
		}
		String CB_IsNote = req.getParameter("CB_IsNote") == null ? ""
				: req.getParameter("CB_IsNote");
		if (CB_IsNote.equals("on")) {
			ath.setIsNote(true);
		} else {
			ath.setIsNote(false);
		}
		String CB_IsShowTitle = req.getParameter("CB_IsShowTitle") == null ? ""
				: req.getParameter("CB_IsShowTitle");
		if (CB_IsShowTitle.equals("on")) {
			ath.setIsShowTitle(true);
		} else {
			ath.setIsShowTitle(false);
		}
		String CB_IsDownload = req.getParameter("CB_IsDownload") == null ? ""
				: req.getParameter("CB_IsDownload");
		if (CB_IsDownload.equals("on")) {
			ath.setIsDownload(true);
		} else {
			ath.setIsDownload(false);
		}
		int DDL_IsDelete = req.getParameter("DDL_IsDelete") == null ? 0
				: Integer.parseInt(req.getParameter("DDL_IsDelete"));
			ath.setIsDeleteInt(DDL_IsDelete);
		String CB_IsUpload = req.getParameter("CB_IsUpload") == null ? ""
				: req.getParameter("CB_IsUpload");
		if (CB_IsUpload.equals("on")) {
			ath.setIsUpload(true);
		} else {
			ath.setIsUpload(false);
		}
		String CB_IsOrder = req.getParameter("CB_IsOrder") == null ? ""
				: req.getParameter("CB_IsOrder");
		if (CB_IsOrder.equals("on")) {
			ath.setIsOrder(true);
		} else {
			ath.setIsOrder(false);
		}
		String CB_IsWoEnableWF = req.getParameter("CB_IsWoEnableWF") == null ? ""
				: req.getParameter("CB_IsWoEnableWF");
		if (CB_IsWoEnableWF.equals("on")) {
			ath.setIsWoEnableWF(true);
		} else {
			ath.setIsWoEnableWF(false);
		}
		String CB_IsWoEnableSave = req.getParameter("CB_IsWoEnableSave") == null ? ""
				: req.getParameter("CB_IsWoEnableSave");
		if (CB_IsWoEnableSave.equals("on")) {
			ath.setIsWoEnableSave(true);
		} else {
			ath.setIsWoEnableSave(false);
		}
		String CB_IsWoEnableInsertFengXian = req.getParameter("CB_IsWoEnableInsertFengXian") == null ? ""
				: req.getParameter("CB_IsWoEnableInsertFengXian");
		if (CB_IsWoEnableInsertFengXian.equals("on")) {
			ath.setIsWoEnableInsertFengXian(true);
		} else {
			ath.setIsWoEnableInsertFengXian(false);
		}
		String CB_IsWoEnableReadonly = req
				.getParameter("CB_IsWoEnableReadonly") == null ? "" : req
				.getParameter("CB_IsWoEnableReadonly");
		if (CB_IsWoEnableReadonly.equals("on")) {
			ath.setIsWoEnableReadonly(true);
		} else {
			ath.setIsWoEnableReadonly(false);
		}
		String CB_IsWoEnableCheck = req
				.getParameter("CB_IsWoEnableCheck") == null ? "" : req
				.getParameter("CB_IsWoEnableCheck");
		if (CB_IsWoEnableCheck.equals("on")) {
			ath.setIsWoEnableCheck(true);
		} else {
			ath.setIsWoEnableCheck(false);
		}
		String CB_IsWoEnableInsertFlow = req
				.getParameter("CB_IsWoEnableInsertFlow") == null ? "" : req
				.getParameter("CB_IsWoEnableInsertFlow");
		if (CB_IsWoEnableInsertFlow.equals("on")) {
			ath.setIsWoEnableInsertFlow(true);
		} else {
			ath.setIsWoEnableInsertFlow(false);
		}
		String CB_IsWoEnableRevise = req.getParameter("CB_IsWoEnableRevise") == null ? ""
				: req.getParameter("CB_IsWoEnableRevise");
		if (CB_IsWoEnableRevise.equals("on")) {
			ath.setIsWoEnableRevise(true);
		} else {
			ath.setIsWoEnableRevise(false);
		}
		String CB_IsWoEnableViewKeepMark = req
				.getParameter("CB_IsWoEnableViewKeepMark") == null ? "" : req
				.getParameter("CB_IsWoEnableViewKeepMark");
		if (CB_IsWoEnableViewKeepMark.equals("on")) {
			ath.setIsWoEnableViewKeepMark(true);
		} else {
			ath.setIsWoEnableViewKeepMark(false);
		}
		String CB_IsWoEnablePrint = req.getParameter("CB_IsWoEnablePrint") == null ? ""
				: req.getParameter("CB_IsWoEnablePrint");
		if (CB_IsWoEnablePrint.equals("on")) {
			ath.setIsWoEnablePrint(true);
		} else {
			ath.setIsWoEnablePrint(false);
		}
		String CB_IsWoEnableSeal = req.getParameter("CB_IsWoEnableSeal") == null ? ""
				: req.getParameter("CB_IsWoEnableSeal");
		if (CB_IsWoEnableSeal.equals("on")) {
			ath.setIsWoEnableSeal(true);
		} else {
			ath.setIsWoEnableSeal(false);
		}
		String CB_IsWoEnableOver = req.getParameter("CB_IsWoEnableOver") == null ? ""
				: req.getParameter("CB_IsWoEnableOver");
		if (CB_IsWoEnableOver.equals("on")) {
			ath.setIsWoEnableOver(true);
		} else {
			ath.setIsWoEnableOver(false);
		}
		String CB_IsWoEnableTemplete = req
				.getParameter("CB_IsWoEnableTemplete") == null ? "" : req
				.getParameter("CB_IsWoEnableTemplete");
		if (CB_IsWoEnableTemplete.equals("on")) {
			ath.setIsWoEnableTemplete(true);
		} else {
			ath.setIsWoEnableTemplete(false);
		}
		String CB_IsWoEnableMarks = req.getParameter("CB_IsWoEnableMarks") == null ? ""
				: req.getParameter("CB_IsWoEnableMarks");
		if (CB_IsWoEnableMarks.equals("on")) {
			ath.setIsWoEnableMarks(true);
		} else {
			ath.setIsWoEnableMarks(false);
		}
		String CB_IsWoEnableDown = req.getParameter("CB_IsWoEnableDown") == null ? ""
				: req.getParameter("CB_IsWoEnableDown");
		if (CB_IsWoEnableDown.equals("on")) {
			ath.setIsWoEnableDown(true);
		} else {
			ath.setIsWoEnableDown(false);
		}
		String CB_FastKeyIsEnable = req.getParameter("CB_FastKeyIsEnable") == null ? ""
				: req.getParameter("CB_FastKeyIsEnable");
		if (CB_FastKeyIsEnable.equals("on")) {
			ath.setFastKeyIsEnable(true);
		} else {
			ath.setFastKeyIsEnable(false);
		}
		ath.setName(TB_Name);
		// ath.setIsWoEnableWF(true);//((CheckBox)this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableWF)).getChecked());
		// ath.IsWoEnableSave = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableSave).Checked;
		// ath.IsWoEnableReadonly = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableReadonly).Checked;
		// ath.IsWoEnableRevise = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableRevise).Checked;
		// ath.IsWoEnableViewKeepMark = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableViewKeepMark).Checked;
		// ath.IsWoEnablePrint = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnablePrint).Checked;
		// ath.IsWoEnableSeal = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableSeal).Checked;
		// ath.IsWoEnableOver = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableOver).Checked;
		// ath.IsWoEnableTemplete = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableTemplete).Checked;
		// ath.IsWoEnableCheck = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableCheck).Checked;
		// ath.IsWoEnableInsertFengXian = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableInsertFengXian).Checked;
		// ath.IsWoEnableInsertFlow = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableInsertFlow).Checked;
		// ath.IsWoEnableMarks = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableMarks).Checked;
		// ath.IsWoEnableDown = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableDown).Checked;
		//
		//
		// ath.FastKeyIsEnable = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.FastKeyIsEnable).Checked;
		ath.setFastKeyGenerRole(TB_FastKeyGenerRole);// =
														

		if (ath.getFastKeyIsEnable() == true)
			if (ath.getFastKeyGenerRole().contains("*OID") == false)
				throw new Exception("@快捷键生成规则必须包含*OID,否则会导致文件名重复.");

		
		FrmAttachment chooseAth = new FrmAttachment();
		if (FK_Node == 0)
			chooseAth.setMyPK(FK_MapData + "_" + Ath);
		else
			chooseAth.setMyPK(FK_MapData + "_" + Ath + "_" + FK_Node);
		int i = chooseAth.RetrieveFromDBSources();
		if (i==0) {
			ath.setUploadType(AttachmentUploadType.forValue(Integer.parseInt(UploadType)));

			if (FK_Node == 0)
				ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj());
			else
				ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj() + "_" + FK_Node);

			if (ath.getNoOfObj() == TB_FastKeyGenerRole) {
				mv.addObject("FK_Node", FK_Node);
				mv.addObject("FK_MapData", FK_MapData);
				mv.addObject("Ath", Ath);
				mv.addObject("success", "附件编号(" + ath.getNoOfObj() + ")已经存在");
				mv.setViewName("MapDef/Attachment");
				// this.WinClose("删除成功.");
				return mv;
			}
			ath.Insert();
			mv.addObject("FK_Node", FK_Node);
			mv.addObject("FK_MapData", FK_MapData);
			mv.addObject("Ath", Ath);
			mv.addObject("success", "删除成功！");
			mv.setViewName("MapDef/Attachment");
			// this.WinClose("删除成功.");
			return mv;
		} else {
			ath.setNoOfObj(Ath);
			if (FK_Node == 0)
				ath.setMyPK(FK_MapData + "_" + Ath);
			else
				ath.setMyPK(FK_MapData + "_" + Ath + "_" + FK_Node);

			ath.Update();
		}
		mv.addObject("FK_Node", FK_Node);
		mv.addObject("FK_MapData", FK_MapData);
		mv.addObject("Ath", Ath);
		mv.addObject("success", "保存成功！");
		mv.setViewName("MapDef/Attachment");
		// this.WinClose("删除成功.");
		return mv;
	}
}
