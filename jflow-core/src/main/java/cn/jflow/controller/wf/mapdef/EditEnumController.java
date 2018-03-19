package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.EditType;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/MapDef")
public class EditEnumController extends BaseController {
	@RequestMapping(value = "/btn_Save_Click4", method = RequestMethod.POST)
	public void btn_Save_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		try {
			// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
			// string member and was converted to Java 'if-else' logic:
			// switch (btn.ID)
			// ORIGINAL LINE: case "Btn_Del":
			if (object.getBtnName().equals("Btn_Del")) {
				try {
					MapAttr attrDel = new MapAttr();
					attrDel.setMyPK(this.getRefNo());
					attrDel.Delete();
					this.winClose(response);
					// response.sendRedirect("Do.jsp?DoType=Del&MyPK=" +
					// this.getMyPK() + "&RefNo=" + this.getRefNo());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;

			} else {
			}

			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getRefNo());
			if (this.getRefNo() != null) {
				attr.Retrieve();
			}
			attr = (MapAttr) BaseModel.Copy(request, attr, null,
					attr.getEnMap(), controls);
			//attr.setFK_MapData(this.getMyPK());
			attr.setDefVal(request.getParameter("DDL"));
			attr.setGroupID(Integer.valueOf(request.getParameter("DDL_GroupID")));
			attr.setColSpan(Integer.valueOf(request.getParameter("DDL_ColSpan")));
			if (request.getParameter("Ctrl") != null
					&& request.getParameter("Ctrl").equals("RB_Ctrl_0")) {
				attr.setUIContralType(UIContralType.DDL);
			} else {
				attr.setUIContralType(UIContralType.RadioBtn);
			}

			if (StringHelper.isNullOrEmpty(this.getRefNo())) {
				attr.setMyPK(this.getMyPK() + "_"
						+ request.getParameter("TB_KeyOfEn"));
				String idx = request.getParameter("IDX");
				if (StringHelper.isNullOrEmpty(idx)) {
				} else {
					attr.setIdx(Integer.parseInt(idx));
				}

				String enumKey = request.getParameter("EnumKey");
				attr.setUIBindKey(enumKey);
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setHisEditType(EditType.Edit);

				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.Enum);
				attr.Insert();
			} else {
				attr.Update();
			}

			// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
			// string member and was converted to Java 'if-else' logic:
			// switch (btn.ID)
			// ORIGINAL LINE: case "Btn_SaveAndClose":
			if (object.getBtnName().equals("Btn_SaveAndClose")) {
				try {
					this.winClose(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			// ORIGINAL LINE: case "Btn_SaveAndNew":
			else if (object.getBtnName().equals("Btn_SaveAndNew")) {
				try {
					response.sendRedirect("Do.jsp?DoType=AddF&MyPK="
							+ this.getMyPK() + "&IDX=" + attr.getIdx()
							+ "&GroupField=" + object.getGroupField());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else {
			}
			if (this.getRefNo() == null) {
				try {
					response.sendRedirect("EditEnum.jsp?DoType=Edit&MyPK="
							+ this.getMyPK() + "&RefNo=" + attr.getMyPK()
							+ "&GroupField=" + object.getGroupField());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					response.sendRedirect("EditEnum.jsp?DoType=Edit&MyPK="
							+ this.getMyPK() + "&RefNo=" + this.getRefNo()
							+ "&GroupField=" + object.getGroupField());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (RuntimeException ex) {
			try {
				this.printAlert(response, ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
