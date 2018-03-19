package cn.jflow.controller.wf.comm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.Sys.UIConfig;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/Comm/Sys")
public class EnsAppCfgController extends BaseController {
	
	
	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	public void btn_Click(HttpServletRequest request, HttpServletResponse response, TempObject object) {
		
		Entity en = BP.En.ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();
		UIConfig cfg = new UIConfig(en);
		
		StringBuilder builder = new StringBuilder();
		
		if (!StringHelper.isNullOrEmpty(this.getDoType())) {
			
			String html = object.getFormHtml();
			HashMap<String, BaseWebControl> controlMap = HtmlUtils.httpParser(html, request);
		
			for (Map.Entry<String, BaseWebControl> entry : controlMap.entrySet()) {
				if(!entry.getKey().contains("CB_")){
					continue;
				}
				CheckBox checkBox = (CheckBox) entry.getValue();
				if(!checkBox.getChecked()){
					continue;
				}
				
				builder.append(entry.getKey().replace("CB_", "")).append(",");
			}
			
			cfg.HisAP.SetVal("ShowColumns", builder.toString());
			cfg.Save();	
			
			if (object.getBtnName().equals("Btn_SaveAndClose")) {
				try {
					this.winClose(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					response.sendRedirect("EnsAppCfg.jsp?EnsName=" + this.getEnsName() + "&DoType=SelectCols");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (WebUser.getNo().equals("admin") && StringHelper.isNullOrEmpty(this.getDoType())) {
			Attrs attrs = en.getEnMap().getHisCfgAttrs();
			for (Attr attr : attrs) {
				if (attr.getIsRefAttr()) {
					continue;
				}
				
			   if (attr.getUIVisible() == false)
				   continue;

				String val = "";
				switch (attr.getUIContralType()) {
				case DDL:
					val = request.getParameter("DDL_" + attr.getKey());
					break;
				case CheckBok:
					if (request.getParameter("CB_" + attr.getKey()) != null && request.getParameter("CB_" + attr.getKey()).equals("on")) {
						val = "1";
					} else {
						val = "0";
					}
					break;
				default:
					val = request.getParameter("TB_" + attr.getKey());
					break;
				}
				cfg.HisAP.SetVal(attr.getKey(), val);
				cfg.Save();
			}
			if (object.getBtnName().equals("Btn_SaveAndClose")) {
				try {
					this.winClose(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					response.sendRedirect("EnsAppCfg.jsp?EnsName=" + this.getEnsName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
//	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
//	public void btn_Click(HttpServletRequest request,
//			HttpServletResponse response,TempObject object) {
//		if (this.getDoType() == null) {
//			EnsAppXmls xmls = new EnsAppXmls();
//			xmls.Retrieve(EnsAppCfgAttr.EnsName, this.getEnsName());
//			for (EnsAppXml xml : xmls.ToJavaList()) {
//				EnsAppCfg en = new EnsAppCfg(this.getEnsName() + "@"
//						+ xml.getNo());
//				String val = "";
//				// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on
//				// a string member and was converted to Java 'if-else' logic:
//				// switch (xml.DBType)
//				// ORIGINAL LINE: case "Enum":
//				if (xml.getDBType().equals("Enum")) {
//
//					val = request.getParameter("DDL_" + xml.getNo());
////					val = this.UCSys1.GetDDLByID("DDL_" + xml.No).SelectedItemStringVal;
//				}
//				// ORIGINAL LINE: case "Boolen":
//				else if (xml.getDBType().equals("Boolen")) {
////					if (this.UCSys1.GetCBByID("CB_" + xml.No).Checked) {
//					if (request.getParameter("CB_" + xml.getNo())!=null&&request.getParameter("CB_" + xml.getNo()).equals("on")) {
//						val = "1";
//					} else {
//						val = "0";
//					}
//				} else {
////					val = this.UCSys1.GetTextBoxByID("TB_" + xml.No).getText();
//					val = request.getParameter("TB_" + xml.getNo());
//				}
//				en.setCfgVal(val);
//				en.setEnsName(this.getEnsName());
//				en.setCfgKey(xml.getNo());
//				en.Save();
//			}
//			if (object.getBtnName().equals("Btn_SaveAndClose")) {
//				try {
//					this.winClose(response);
//				} catch (IOException e) {
//					
//					e.printStackTrace();
//				}
//			}else{
//				try {
//					response.sendRedirect("EnsAppCfg.jsp?EnsName=" + this.getEnsName() + "&DoType=Adv");
//				} catch (IOException e) {
//					
//					e.printStackTrace();
//				}
//			}
//		}
//		
//
//		if (WebUser.getNo().equals("admin") && this.getDoType() != null) {
//			Entity en1 = BP.En.ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();
//			Attrs attrs = en1.getEnMap().getHisCfgAttrs();
//			for (Attr attr : attrs) {
//				if (attr.getIsRefAttr()) {
//					continue;
//				}
//
//				EnsAppCfg en = new EnsAppCfg(this.getEnsName() + "@"
//						+ attr.getKey());
//				String val = "";
//				switch (attr.getUIContralType()) {
//				case DDL:
////					val = this.UCSys1.GetDDLByID("DDL_" + attr.getKey()).SelectedItemStringVal;
//					val = request.getParameter("DDL_" + attr.getKey());
//					break;
//				case CheckBok:
////					if (this.UCSys1.GetCBByID("CB_" + attr.getKey()).Checked) {
//					if (request.getParameter("CB_" + attr.getKey())!=null&&request.getParameter("CB_" + attr.getKey()).equals("on")) {
//						val = "1";
//					} else {
//						val = "0";
//					}
//					break;
//				default:
////					val = this.UCSys1.GetTextBoxByID("TB_" + attr.getKey())
////							.getText();
//					val = request.getParameter("TB_" + attr.getKey());
//					break;
//				}
//				en.setCfgVal(val);
//				en.setEnsName(this.getEnsName());
//				en.setCfgKey(attr.getKey());
//
//				if (attr.getKey().equals("Glo")) {
//					BP.DA.DBAccess.RunSQL("UPDATE Sys_EnsAppCfg SET CfgVal='"
//							+ val + "' WHERE CfgKey='" + attr.getKey() + "'");
//				}
//				en.Save();
//			}
//			if (object.getBtnName().equals("Btn_SaveAndClose")) {
//				try {
//					this.winClose(response);
//				} catch (IOException e) {
//					
//					e.printStackTrace();
//				}
//			}else{
//				try {
//					response.sendRedirect("EnsAppCfg.jsp?EnsName=" + this.getEnsName() + "&DoType=Adv");
//				} catch (IOException e) {
//					
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		// Button
//	}

}
