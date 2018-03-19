package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class EditTableController extends BaseController{
	@RequestMapping(value = "/btn_Save_Click1", method = RequestMethod.POST)
	public void btn_Save_Click(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
		try
		{

//			switch (btn.ID)
//ORIGINAL LINE: case "Btn_Del":
			if (object.getBtnName().equals("Btn_Del"))
			{
					try {
						MapAttr attrDel = new MapAttr();
	                     attrDel.setMyPK(this.getRefNo());
	                     attrDel.Delete();
	                     this.winClose(response);
//						response.sendRedirect("Do.jsp?DoType=Del&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
			}
			else
			{
			}

			MapAttr attr = new MapAttr();
			if (StringHelper.isNullOrEmpty(this.getRefNo()))
			{
				attr.setMyPK(this.getMyPK() + "_" + request.getParameter("TB_KeyOfEn"));
				attr.setUIContralType(UIContralType.DDL);
				attr.setMyDataType(BP.DA.DataType.AppString);
				attr.setLGType(FieldTypeS.FK);
				attr.setDefVal("");
				attr.setUIBindKey(request.getParameter("SFKey"));
				attr.setUIIsEnable(true);
			}
			else
			{
				attr.setMyPK(this.getRefNo());
				attr.Retrieve();
			}
			attr = (MapAttr)BaseModel.Copy(request, attr, null,attr.getEnMap(), controls);
			attr.setFK_MapData(this.getMyPK());
			attr.setGroupID(Integer.parseInt(request.getParameter("DDL_GroupID")));
			attr.setDefVal(request.getParameter("TB_DefVal"));
			attr.setUIBindKey(request.getParameter("TB_UIBindKey"));

			//if (this.Pub1.IsExit("CB_IsDefValNull"))
			//{
			//    if (this.Pub1.GetCBByID("CB_IsDefValNull").Checked == false)
			//        attr.DefVal = this.Pub1.GetDDLByID("DDL").SelectedItemStringVal;
			//    else
			//        attr.DefVal = "";
			//}
			//else
			//{
			//    string s = this.Pub1.GetDDLByID("DDL_DefVal").SelectedItemStringVal;
			//    if (s == "@Select")
			//    {
			//        attr.DefVal = this.Pub1.GetDDLByID("DDL").SelectedItemStringVal;
			//    }
			//    else
			//    {
			//        attr.DefVal = s;
			//    }
			//}

			if (StringHelper.isNullOrEmpty(getRefNo()))
			{
				attr.Insert();
			}
			else
			{
				attr.Update();
			}


//			switch (btn.ID)
//ORIGINAL LINE: case "Btn_SaveAndClose":
			if (object.getBtnName().equals("Btn_SaveAndClose"))
			{
					try {
						this.winClose(response);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
			}
//ORIGINAL LINE: case "Btn_SaveAndNew":
			else if (object.getBtnName().equals("Btn_SaveAndNew"))
			{
					try {
						response.sendRedirect("Do.jsp?DoType=AddF&MyPK=" + this.getMyPK() + "&IDX=" + attr.getIdx() + "&GroupField=" + object.getGroupField());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
			}
			else
			{
			}
			try {
				response.sendRedirect("EditTable.jsp?DoType=Edit&MyPK=" + this.getMyPK() + "&RefNo=" + attr.getMyPK() + "&GroupField=" + object.getGroupField());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (RuntimeException ex)
		{
			try {
				this.printAlert(response, ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
