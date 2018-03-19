package cn.jflow.controller.wf.rpt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.UserRegedit;
import BP.Web.WebUser;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Rpt")
public class RptSelectMValController extends BaseController {

	@RequestMapping(value = "/RptSelectSave", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson dtlSave(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		try {
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");

			// HashMap<String,BaseWebControl> controls =
			// HtmlUtils.httpParser(object.getFormHtml(), request);

			UserRegedit ur = new UserRegedit();
			ur.setMyPK(getMyPK(map));
			ur.RetrieveFromDBSources();
			ur.setFK_Emp(WebUser.getNo());
			ur.setCfgKey(map.get("EnsName") + "_SearchAttrs");

			Entity en = ClassFactory.GetEns(map.get("EnsName"))
					.getGetNewEntity();
			Attr attr = en.getEnMap().GetAttrByKey(map.get("AttrKey"));

			String cfgVal = ur.getMVals();
			AtPara ap = new AtPara(cfgVal);
			String old_Val = ap.GetValStrByKey(map.get("AttrKey"));

			String keys = "";
			if (attr.getIsEnum()) {
				SysEnums ses = new SysEnums(attr.getUIBindKey());
				for (SysEnum item : ses.ToJavaList()) {
					Object obj = request.getParameter("CB_" + item.getIntKey());
					if (null == obj)
						continue;
					if (keys.length() > 0) {
						keys += "." + item.getIntKey()+".";
					} else {
						keys += item.getIntKey();
					}
					// keys += "." + item.getIntKey() + ".";
				}

				keys = "@" + map.get("AttrKey") + "=" + keys;
				if (ur.getMVals().contains("@" + map.get("AttrKey")))
					ur.setMVals(ur.getMVals().replace(
							"@" + map.get("AttrKey") + "=" + old_Val, keys));
				else
					ur.setMVals(ur.getMVals() + keys);

				ur.DirectUpdate();
			} else if (attr.getIsBindTable()) {

				String sql = "select * from " + attr.getUIBindKey();
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					Object obj = request.getParameter("CB_"
							+ dr.getValue(attr.getUIRefKeyValue()));
					if (null == obj)
						continue;
					if (keys.length() > 0) {
						keys += "." + dr.getValue(attr.getUIRefKeyValue())+".";
					} else {
						keys += dr.getValue(attr.getUIRefKeyValue());
					}
				}

				keys = "@" + map.get("AttrKey") + "=" + keys;
				if (ur.getMVals().contains("@" + map.get("AttrKey")))
					ur.setMVals(ur.getMVals().replace(
							"@" + map.get("AttrKey") + "=" + old_Val, keys));
				else
					ur.setMVals(ur.getMVals() + keys);

				ur.DirectUpdate();
			} else {
				if (attr.getUIBindKey().equals("BP.Port.Depts")) {
					DataTable dt;
					if (WebUser.getNo().toString().equals("admin"))
						dt = DBAccess
								.RunSQLReturnTable("SELECT No,Name FROM Port_Dept ");
					else
						dt = DBAccess
								.RunSQLReturnTable("SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='"
										+ WebUser.getNo()
										+ "'  AND FK_Flow='"
										+ this.getFK_Flow() + "')");

					for (DataRow dr : dt.Rows) {
						String obj = request.getParameter("CB_" + dr.getValue(0).toString());
						if(null   == obj){
							continue;
						}
						keys += "." + dr.getValue(0).toString() + ".";
					}
				} else {
					Entities ens = BP.En.ClassFactory.GetEns(attr
							.getUIBindKey());
					ens.RetrieveAll();

					for (Entity item : ens.ToJavaListEn()) {
						Object obj = request.getParameter("CB_" + item.GetValStrByKey(attr.getUIRefKeyValue()));
						if (null == obj)
							continue;
						
						if (keys.length() > 0) {
							keys += "." + item.GetValStrByKey(attr.getUIRefKeyValue())+".";
						} else {
							keys += item.GetValStrByKey(attr.getUIRefKeyValue());
						}
					}
				}

				keys = "@" + map.get("AttrKey") + "=" + keys;
				if (ur.getMVals().contains("@" + map.get("AttrKey")))
					ur.setMVals(ur.getMVals().replace( "@" + map.get("AttrKey") + "=" + old_Val, keys));
				else
					ur.setMVals(ur.getMVals() + keys);

				ur.DirectUpdate();
			}
			keys = keys.replace("@" + map.get("AttrKey") + "=", "");
			j.setMsg(keys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	private String getMyPK(HashMap<String, String> map) {
		return WebUser.getNo() + map.get("EnsName") + "_SearchAttrs";
	}

	private HashMap<String, String> getParamsMap(String queryString, String enc) {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			String[] paramPair;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex,
							ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}
				paramPair = subStr.split("=");
				param = paramPair[0];
				value = paramPair.length == 1 ? "" : paramPair[1];
				try {
					value = URLDecoder.decode(value, enc);
				} catch (UnsupportedEncodingException ignored) {
				}
				paramsMap.put(param, value);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}
}
