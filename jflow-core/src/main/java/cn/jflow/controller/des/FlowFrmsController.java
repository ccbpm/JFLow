package cn.jflow.controller.des;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Sys.AppType;
import BP.Sys.FrmType;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Tools.StringHelper;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.WhoIsPK;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin")
public class FlowFrmsController extends BaseController {

	@RequestMapping(value = "/btn_SavePowerOrders_Click", method = RequestMethod.POST)
	public void btn_SavePowerOrders_Click(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {
		FrmNodes fns = new FrmNodes(object.getFK_Flow(), object.getFK_Node());
		for (FrmNode fn : fns.ToJavaList()) {
			// fn.setIsEdit(this.Pub1.GetCBByID("CB_IsEdit_" +
			// fn.getFK_Frm()).Checked);
			// fn.setIsPrint(this.Pub1.GetCBByID("CB_IsPrint_" +
			// fn.getFK_Frm()).Checked);
			// fn.setIdx(Integer.parseInt(this.Pub1.GetTextBoxByID("TB_Idx_" +
			// fn.getFK_Frm()).getText()));
			// fn.setHisFrmType((FrmType)this.Pub1.GetDDLByID("DDL_FrmType_" +
			// fn.getFK_Frm()).SelectedItemIntVal);
			//
			// //权限控制方案.
			// fn.setFrmSln(this.Pub1.GetDDLByID("DDL_Sln_" +
			// fn.getFK_Frm()).SelectedItemIntVal);
			// fn.setWhoIsPK((WhoIsPK)this.Pub1.GetDDLByID("DDL_WhoIsPK_" +
			// fn.getFK_Frm()).SelectedItemIntVal);
			String CB_IsEdit = request.getParameter("CB_IsEdit_"
					+ fn.getFK_Frm());
			boolean IsEdit = true;
			if (CB_IsEdit != null && CB_IsEdit.equals("on")) {
				IsEdit = true;
			} else {
				IsEdit = false;
			}
			fn.setIsEdit(IsEdit);
			String CB_IsPrint = request.getParameter("CB_IsPrint_"
					+ fn.getFK_Frm());
			boolean IsPrint = true;
			if (CB_IsPrint != null && CB_IsPrint.equals("on")) {
				IsPrint = true;
			} else {
				IsPrint = false;
			}
			fn.setIsPrint(IsPrint);
			int TB_Idx = Integer.parseInt(request.getParameter("TB_Idx_"
					+ fn.getFK_Frm()));
			fn.setIdx(TB_Idx);
			FrmType DDL_FrmType = FrmType.forValue(Integer.parseInt(request
					.getParameter("DDL_FrmType_" + fn.getFK_Frm())));
			fn.setHisFrmType(DDL_FrmType);

			// 权限控制方案.
			int DDL_Sln = Integer.parseInt(request.getParameter("DDL_Sln_"
					+ fn.getFK_Frm()));
			fn.setFrmSln(DDL_Sln);
			WhoIsPK DDL_WhoIsPK = WhoIsPK.forValue(Integer.parseInt(request
					.getParameter("DDL_WhoIsPK_" + fn.getFK_Frm())));
			fn.setWhoIsPK(DDL_WhoIsPK);
			fn.setFK_Flow(object.getFK_Flow());
			fn.setFK_Node(object.getFK_Node());
			// fn.FK_Frm =

			fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_"
					+ fn.getFK_Flow());

			fn.Update();
		}
		try {
			response.sendRedirect("FlowFrms.jsp?ShowType=EditPowerOrder&FK_Node="
					+ object.getFK_Node() + "&FK_Flow=" + object.getFK_Flow());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/btn_SaveFrmSort_Click", method = RequestMethod.POST)
	public void btn_SaveFrmSort_Click(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {
		for (int i = 1; i <= 15; i++) {
			String tbName = request.getParameter("TB_Name_" + i);
			// TextBox tbName = this.Pub1.GetTextBoxByID("TB_Name_" + i);
			SysFormTree fs = new SysFormTree();
			fs.setNo(StringUtils.leftPad((new Integer(i)).toString(), 2, '0'));
			fs.setName(tbName);
			if (fs.getName().length() > 1) {
				fs.Save();
			} else {
				fs.Delete();
			}
		}
		// this.Alert("保存成功");
		try {
			this.printAlertReload(response, "保存成功","FlowFrms.jsp?ShowType=FrmSorts&FK_Node=" +object.getFK_Node()+"&FK_Flow="+object.getFK_Flow());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/btn_SaveFlowFrms_Click", method = RequestMethod.POST)
	public void btn_SaveFlowFrms_Click(TempObject object,
			HttpServletRequest request, HttpServletResponse response,String chk_value) {
		FrmNodes fns = new FrmNodes(object.getFK_Flow(), object.getFK_Node());
		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.AppType, AppType.Application.getValue());

		// BP.WF.Node nd = new BP.WF.Node(this.FK_Node);
		String ids = ",";
		for (MapData md : mds.ToJavaList()) {
			// CheckBox cb = this.Pub1.GetCBByID("CB_" + md.getNo());
			// if (cb == null || cb.getChecked() == false)
			// {
			// continue;
			// }
			/*String cb = request.getParameter("CB_" + md.getNo());
			if (cb == null) {
				continue;
			}*/
			System.out.println(!chk_value.contains("CB_"+md.getNo()+";"));
			if(!chk_value.contains("CB_"+md.getNo()+";")){
				continue;
			}

			ids += md.getNo() + ",";
		}

		// 删除已经删除的。
		for (FrmNode fn : fns.ToJavaList()) {
			System.out.println("," + fn.getFK_Frm() + ",");
			if (ids.contains("," + fn.getFK_Frm() + ",") == false) {
				fn.Delete();
				continue;
			}
		}

		// 增加集合中没有的。
		String[] strs = ids.split(",");
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
		try {
			response.sendRedirect("FlowFrms.jsp?ShowType=EditPowerOrder&FK_Node="
					+ object.getFK_Node() + "&FK_Flow=" + object.getFK_Flow().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/btn_SaveFrm_Click", method = RequestMethod.POST)
	public void btn_SaveFrm_Click(TempObject object,
			HttpServletRequest request, HttpServletResponse response,
			String btnName) {
		// Button btn = (Button)((sender instanceof Button) ? sender : null);
		// if (btn.getId().equals("Btn_Delete"))
		// {
		if (btnName.equals("Btn_Delete")) {
			// MapData mdDel = new MapData();
			// mdDel.No = this.FK_MapData;
			// mdDel.Delete();
			try {
				response.sendRedirect("FlowFrms.jsp?ShowType=Frm&DoType=DelFrm&FK_Node="
						+ object.getFK_Node()
						+ "&FK_MapData="
						+ object.getFK_MapData()
						+ "&FK_Flow="
						+ object.getFK_Flow());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		MapData md = new MapData();
		if (StringHelper.isNullOrEmpty(object.getFK_MapData()) == false) {
			md.setNo(object.getFK_MapData());
			md.RetrieveFromDBSources();
		}
		// HashMap<String, BaseWebControl> controls =
		// HtmlUtils.httpParser(request.getParameter("BodyHtml"),true);
		// // md = (MapData)this.Pub1.Copy(md);
		// md = (MapData) BaseModel.Copy(request, md, null, md.getEnMap(),
		// controls);
		String name = request.getParameter("TB_Name");
		String no = request.getParameter("TB_No");
		String frmType = request.getParameter("DDL_FrmType");
		String PTable = request.getParameter("TB_PTable");
		String FrmSort = request.getParameter("DDL_FK_FrmSort");
		md.setName(name);
		md.setNo(no);
		md.setHisFrmType(FrmType.forValue(Integer.parseInt(frmType)));
		md.setHisFrmTypeInt(Integer.parseInt(frmType));
		md.setFK_FrmSort(FrmSort);

		// md.setHisFrmTypeInt(this.Pub1.GetDDLByID("DDL_" +
		// MapDataAttr.FrmType).SelectedItemIntVal);
		// md.setFK_FrmSort(this.Pub1.GetDDLByID("DDL_" +
		// MapDataAttr.FK_FrmSort).SelectedItemStringVal);

		// md.setHisFrmTypeInt(Integer.parseInt(request.getParameter("DDL_"
		// + MapDataAttr.FrmType)));
		// md.setFK_FrmSort(request.getParameter("DDL_" +
		// MapDataAttr.FK_FrmSort));

		md.setHisAppType(AppType.Application);
		if (StringHelper.isNullOrEmpty(object.getFK_MapData()) == true) {
			if (md.getIsExits() == true) {
				// this.Alert("表单编号(" + md.getNo() + ")已存在");
				try {
					this.printAlert(response, "表单编号(" + md.getNo() + ")已存在");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else {
				md.Insert();
				try {
					response.sendRedirect("FlowFrms.jsp?ShowType=Frm&FK_Node="
							+ object.getFK_Node() + "&FK_MapData=" + md.getNo()
							+ "&FK_Flow=" + object.getFK_Flow());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			md.Update();
			// this.Alert("更新成功。");
			try {
				this.printAlert(response, "更新成功。");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
