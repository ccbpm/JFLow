package cn.jflow.controller.des;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Tools.StringHelper;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.WorkAttr;

@Controller
@RequestMapping("/WF/MapDef")
public class SlnController extends BaseController {
	@RequestMapping(value = "/btn_Field_Click", method = RequestMethod.POST)
	public void btn_Field_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response, String btnName) {
		if (btnName.equals("Btn_Del")) {
			FrmFields fss1 = new FrmFields();
			fss1.Delete(FrmFieldAttr.FK_MapData, object.getFK_MapData(),
					FrmFieldAttr.FK_Node, object.getFK_Node());
			try {
				response.sendRedirect("Sln.jsp?FK_Flow=" + object.getFK_Flow()
						+ "&FK_Node=" + object.getFK_Node() + "&FK_MapData="
						+ object.getFK_MapData() + "&IsOk=1");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		MapAttrs attrs = new MapAttrs(object.getFK_MapData());
		// 查询出来解决方案.
		FrmFields fss = new FrmFields();
		fss.Delete(FrmFieldAttr.FK_MapData, object.getFK_MapData(),
				FrmFieldAttr.FK_Node, object.getFK_Node());

		for (MapAttr attr : attrs.ToJavaList()) {
			if (attr.getKeyOfEn().equals(WorkAttr.RDT)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.FID)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.OID)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.Rec)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.MyNum)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.MD5)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.Emps)) {

			} else if (attr.getKeyOfEn().equals(WorkAttr.CDT)) {
				// continue;
			} else {
				// break;
			}
			// switch (attr.KeyOfEn) {
			// case BP.WF.WorkAttr.RDT:
			// case BP.WF.WorkAttr.FID:
			// case BP.WF.WorkAttr.OID:
			// case BP.WF.WorkAttr.Rec:
			// case BP.WF.WorkAttr.MyNum:
			// case BP.WF.WorkAttr.MD5:
			// case BP.WF.WorkAttr.Emps:
			// case BP.WF.WorkAttr.CDT:
			// continue;
			// default:
			// break;
			// }

			boolean isChange = false;
			String Visible = request.getParameter("CB_" + attr.getKeyOfEn()
					+ "_UIVisible");
			boolean UIVisible = true;
			if (Visible != null && Visible.equals("on")) {
				UIVisible = true;
			} else {
				UIVisible = false;
			}
			// boolean UIVisible = this.Pub2.GetCBByID("CB_" + attr.KeyOfEn
			// + "_UIVisible").Checked;
			if (attr.getUIVisible() != UIVisible) {
				isChange = true;
			}
			String IsEnable = request.getParameter("CB_" + attr.getKeyOfEn()
					+ "_UIIsEnable");
			boolean UIIsEnable = true;
			if (IsEnable != null && IsEnable.equals("on")) {
				UIIsEnable = true;
			} else {
				UIIsEnable = false;
			}
			// boolean UIIsEnable = this.Pub2.GetCBByID("CB_" + attr.KeyOfEn
			// + "_UIIsEnable").Checked;
			if (attr.getUIIsEnable() != UIIsEnable) {
				isChange = true;
			}

			String Sigan = request.getParameter("CB_" + attr.getKeyOfEn()
					+ "_IsSigan");
			boolean IsSigan = true;
			if (Sigan != null && Sigan.equals("on")) {
				IsSigan = true;
			} else {
				IsSigan = false;
			}
			// boolean IsSigan = this.Pub2.GetCBByID("CB_" + attr.KeyOfEn
			// + "_IsSigan").Checked;
			if (attr.getIsSigan() != IsSigan) {
				isChange = true;
			}

			String defVal = request.getParameter("TB_" + attr.getKeyOfEn() + "_DefVal");
			
			defVal = StringHelper.isEmpty(defVal, "");
			
			// String defVal = this.Pub2.GetTextBoxByID(
			// "TB_" + attr.KeyOfEn + "_DefVal").getText();
			if (!defVal.equals(attr.getDefValReal())) {
				isChange = true;
			}

			String NotNull = request.getParameter("CB_" + attr.getKeyOfEn()
					+ "_IsNotNull");
			boolean IsNotNull = true;
			if (NotNull != null && NotNull.equals("on")) {
				IsNotNull = true;
			} else {
				IsNotNull = false;
			}
			// boolean IsNotNull = this.Pub2.GetCBByID("CB_" + attr.KeyOfEn
			// + "_IsNotNull").Checked;
			if (IsNotNull == true) {
				isChange = true;
			}

			String WriteToFlowTable = request
					.getParameter("CB_" + attr.getKeyOfEn() + "_"
							+ FrmFieldAttr.IsWriteToFlowTable);
			boolean IsWriteToFlowTable = true;
			if (WriteToFlowTable != null && WriteToFlowTable.equals("on")) {
				IsWriteToFlowTable = true;
			} else {
				IsWriteToFlowTable = false;
			}
			// boolean IsWriteToFlowTable = this.Pub2.GetCBByID("CB_"
			// + attr.KeyOfEn + "_" + FrmFieldAttr.IsWriteToFlowTable).Checked;
			if (IsWriteToFlowTable == true) {
				isChange = true;
			}

			String exp = request.getParameter("TB_" + attr.getKeyOfEn()
					+ "_RegularExp");
			// String exp = this.Pub2.GetTextBoxByID(
			// "TB_" + attr.KeyOfEn + "_RegularExp").getText();
			if (StringHelper.isNullOrEmpty(exp)) {
				isChange = true;
			}

			if (isChange == false) {
				continue;
			}

			FrmField sln = new FrmField();
			sln.setUIVisible(UIVisible);
			sln.setUIIsEnable(UIIsEnable);
			sln.setIsSigan(IsSigan);
			sln.setDefVal(defVal);

			sln.setIsNotNull(IsNotNull);
			sln.setRegularExp(exp);
			sln.setIsWriteToFlowTable(IsWriteToFlowTable);
			sln.setFK_Node(object.getFK_Node());
			sln.setFK_Flow(object.getFK_Flow());

			sln.setFK_MapData(object.getFK_MapData());
			sln.setKeyOfEn(attr.getKeyOfEn());
			sln.setName(attr.getName());

			sln.setMyPK(object.getFK_MapData() + "_" + object.getFK_Flow()
					+ "_" + object.getFK_Node() + "_" + attr.getKeyOfEn());
			sln.Insert();
		}
		try {
			response.sendRedirect("Sln.jsp?FK_Flow=" + object.getFK_Flow()
					+ "&FK_Node=" + object.getFK_Node() + "&FK_MapData="
					+ object.getFK_MapData() + "&IsOk=1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
