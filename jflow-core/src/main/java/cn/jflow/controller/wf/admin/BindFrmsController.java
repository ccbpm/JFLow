package cn.jflow.controller.wf.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.controller.wf.workopt.BaseController;
import BP.En.FieldTypeS;
import BP.Sys.SystemConfig;
import BP.Sys.AppType;
import BP.Sys.FrmType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.RunModel;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.WhoIsPK;

@Controller
@RequestMapping("/WF/Admin")
public class BindFrmsController extends BaseController {

	@RequestMapping(value = "/SaveFlowFrms", method = RequestMethod.POST)
	public ModelAndView SaveFlowFrms(HttpServletRequest request,
			HttpServletResponse response) {

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.AppType, AppType.Application.getValue());
		// Node nd = new Node(this.FK_Node);
		String ids = ",";
		for (MapData md : mds.ToJavaList()) {
			String cb = request.getParameter("CB_" + md.getNo());
			if (cb == null) {
				continue;
			}
			ids += md.getNo() + ",";
		}

		// 删除已经删除的。
		for (FrmNode fn : fns.ToJavaList()) {
			if (ids.contains("," + fn.getFK_Frm() + ",") == false) {
				fn.Delete();
				continue;
			}
		}

		// 增加集合中没有的。
		String[] strs = ids.split("[,]", -1);
		for (String s : strs) {
			if (StringHelper.isNullOrEmpty(s)) {
				continue;
			}
			if (fns.Contains(FrmNodeAttr.FK_Frm, s)) {
				continue;
			}

			FrmNode fn = new FrmNode();
			fn.setFK_Frm(s);
			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Node(this.getFK_Node());
			fn.Save();
		}
		ModelAndView andView = new ModelAndView("redirect:BindFrms.jsp");
		andView.addObject("FK_Node", this.getFK_Node());
		andView.addObject("FK_Flow", this.getFK_Flow());
		return andView;
	}

	@RequestMapping(value = "/SavePowerOrders", method = RequestMethod.POST)
	public ModelAndView SavePowerOrders(HttpServletRequest request,
			HttpServletResponse response) {
		String tfModel = (String) SystemConfig.getAppSettings().get("TreeFrmModel");
		Node nd = new Node(this.getFK_Node());
		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		for (FrmNode fn : fns.ToJavaList()) {
			String cb_isEdit = getParamter(("CB_IsEdit_" + fn.getFK_Frm()));
			if (cb_isEdit == null) {
				fn.setIsEdit(false);
			} else {
				fn.setIsEdit(true);
			}

			String cb_isPrint = getParamter(("CB_IsPrint_" + fn.getFK_Frm()));
			if (cb_isPrint == null) {
				fn.setIsPrint(false);
			} else {
				fn.setIsPrint(true);
			}

			// 是否启
			String cb_isEnableLoadData = getParamter(("CB_IsEnableLoadData_" + fn
					.getFK_Frm()));
			if (cb_isEnableLoadData == null) {
				fn.setIsEnableLoadData(false);
			} else {
				fn.setIsEnableLoadData(true);
			}

			// 是否启
			String idx = getParamter(("TB_Idx_" + fn.getFK_Frm()));
			if (StringHelper.isNullOrEmpty(idx)) {
				fn.setIdx(0);
				;
			} else {
				fn.setIdx(Integer.parseInt(idx));
			}
			if (tfModel!=null&& "1".equals(tfModel))
            {
                if (this.currND.getIsStartNode() == false)
                {
                     fn.setGuanJianZiDuan(getParamter("DDL_Attr_" + fn.getFK_Frm()));
                }
            }
			//String frmType = getParamter("DDL_FrmType_" + fn.getFK_Frm());
			//fn.setHisFrmType(FrmType.forValue(Integer.parseInt(frmType)));

			// 权限控制方案.
			String sln = getParamter("DDL_Sln_" + fn.getFK_Frm());
			fn.setFrmSln(Integer.parseInt(sln));

			String whoIsPK = getParamter("DDL_WhoIsPK_" + fn.getFK_Frm());
			fn.setWhoIsPK(WhoIsPK.forValue(Integer.parseInt(whoIsPK)));

			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Node(this.getFK_Node());

			fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_"
					+ fn.getFK_Flow());

			if (fn.getHisFrmType().equals(FrmType.WordFrm) || fn.getHisFrmType().equals(FrmType.ExcelFrm))
			{
				fn.setTempleteFile(getParamter("DDL_File_" + fn.getFK_Frm()));
			}

			// add  for hainan 2016.3.25 增加1对N.
			if (nd.getHisRunModel().equals(RunModel.FL) || nd.getHisRunModel().equals(RunModel.FHL))
			{
				if(null==getParamter("CB_Is1ToN_" + fn.getFK_Frm())||"".equals(getParamter("CB_Is1ToN_" + fn.getFK_Frm()))){
					fn.setIs1ToN(false);
				}else{
					fn.setIs1ToN(true);
				}
				if (fn.getIs1ToN() == true)
				{
					//检查该表单里是否具有FID的隐藏字段，如果没有系统自动给他增加上.
					MapAttrs attrs = new MapAttrs(fn.getFK_Frm());
					if (attrs.Contains("KeyOfEn", "FID") == false)
					{
						MapAttr attr = new MapAttr();
						attr.setMyPK(fn.getFK_Frm() + "_FID");
						attr.setFK_MapData(fn.getFK_Frm());
						attr.setKeyOfEn("FID");
						attr.setLGType(FieldTypeS.Normal);
						attr.setUIVisible(false);
						attr.setMyDataType(BP.DA.DataType.AppInt);
						attr.Insert();
					}
				}
			}

			if (nd.getHisRunModel().equals(RunModel.SubThread))
			{
				fn.setHuiZong(getParamter("DDL_HuiZong_" + fn.getFK_Frm()));
			}

			fn.Update();
		}
		ModelAndView andView = new ModelAndView("redirect:BindFrms.jsp");
		andView.addObject("FK_Node", this.getFK_Node());
		andView.addObject("FK_Flow", this.getFK_Flow());
		andView.addObject("ShowType", "EditPowerOrder");
		return andView;
	}

private BP.WF.Node currND = null;

public final BP.WF.Node getcurrND()
{
	if (currND == null)
	{
		currND = new BP.WF.Node(this.getFK_Node());
	}
	return currND;
}
public final void setcurrND(BP.WF.Node value)
{
	currND = value;
}
}