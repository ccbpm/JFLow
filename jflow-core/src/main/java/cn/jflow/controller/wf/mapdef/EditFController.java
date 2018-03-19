package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DataType;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.PicType;
import BP.Sys.SignType;
import BP.Tools.PinYinF4jUtils;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/MapDefEditF")
public class EditFController extends BaseController {

	@RequestMapping(value = "/ddlType_SelectedIndexChanged", method = RequestMethod.POST)
	public void ddlType_SelectedIndexChanged(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
		
		MapAttr attr = new MapAttr(this.getRefNo());
		attr.setMyDataTypeS(request.getParameter("DDL_DTType"));
		attr.Update();

		try {
			response.sendRedirect(basePath+"WF/MapDef/EditF.jsp?DoType=" + this.getDoType()
					+ "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo()
					+ "&FType=" + attr.getMyDataType() + "&GroupField="
					+ object.getGroupField());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// this.Response.Redirect(this.Request.RawUrl, true);
	}

	@RequestMapping(value = "/btn_Click1", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
		if (object.getBtnName().equals("Btn_New")) {

			MapAttr mapAttr = new MapAttr(this.getRefNo());
			String url = "WF/MapDef/Do.jsp?DoType=AddF&MyPK=" + mapAttr.getFK_MapData()
					+ "&IDX=" + mapAttr.getIdx() + "&GroupField = "
					+ object.getGroupField();

			try {
				response.sendRedirect(basePath+url);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;

		} else if (object.getBtnName().equals("Btn_Back")) {

			String url1 = "WF/MapDef/Do.jsp?DoType=AddF&MyPK=" + this.getMyPK()
					+ "&GroupField = " + object.getGroupField();
			try {
				response.sendRedirect(basePath+url1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
		}

	}

	public String getRefNo(HttpServletRequest request) {
		String s = request.getParameter("RefNo");
		if (s == null || "".equals(s))
			s = request.getParameter("No");

		if (s == null || "".equals(s))
			s = null;
		return s;
	}

	@ResponseBody
	@RequestMapping(value = "/parse", method = RequestMethod.GET)
	public Map<String, String> parse(String parse) throws UnsupportedEncodingException {
		parse = URLDecoder.decode(parse, "utf-8");
		String s = PinYinF4jUtils.spell(parse);
		Map<String, String> m = new HashMap<String, String>();
		m.put("result", s);
		return m;
	}

	@RequestMapping(value = "/btn_Save_Click", method = RequestMethod.POST)
	public void btn_Save_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
		
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		try {
			if (object.getBtnName().equals("Btn_Del")) {
				try {
					MapAttr attrDel = new MapAttr();
					attrDel.setMyPK(this.getRefNo());
					attrDel.Delete();
					this.winClose(response);
					// response.sendRedirect("Do.jsp?DoType=Del&MyPK=" +
					// this.getMyPK() + "&RefNo=" + this.getRefNo() +
					// "&GroupField = " + object.getGroupField());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			MapAttr attr = new MapAttr();
			if (!StringHelper.isNullOrEmpty(getRefNo(request))) {
				attr.setMyPK(getRefNo(request));
				try {
					attr.Retrieve();
				} catch (java.lang.Exception e) {
					attr.CheckPhysicsTable();
					try {
						attr.Retrieve();
					} catch (java.lang.Exception e2) {
						
					}
				}
				attr = (MapAttr) BaseModel.Copy(request, attr, null,
						attr.getEnMap(), controls);
				attr.setGroupID(Integer.parseInt(request
						.getParameter("DDL_GroupID")));
				if(null!=request.getParameter("DDL_ColSpan")){
					attr.setColSpan(Integer.parseInt(request
											.getParameter("DDL_ColSpan")));
				}
				if (attr.getUIIsEnable() == false
						&& attr.getMyDataType() == DataType.AppString) {
					try {
						// attr.IsSigan =
						// this.Pub1.GetCBByID("CB_IsSigan").Checked;
						String cb = request.getParameter("CB_IsSigan");
						if (cb != null) {
							attr.setIsSigan(true);
						} else {
							attr.setIsSigan(false);
						}
					} catch (java.lang.Exception e2) {
					}
				}
				attr.setMyDataType(object.getFType());
				switch (object.getFType()) {
				case DataType.AppBoolean:
					attr.setMyDataType(BP.DA.DataType.AppBoolean);
					attr.setUIContralType(UIContralType.CheckBok);
					// attr.DefValOfBool =
					// this.Pub1.GetCBByID("CB_DefVal").Checked;
					String cb = request.getParameter("CB_DefVal");
					if (cb != null && cb.equals("on")) {
						attr.setDefValOfBool(true);
					} else {
						attr.setDefValOfBool(false);
					}
					break;
				case DataType.AppDateTime:
				case DataType.AppDate:
					attr.setDefValReal(request.getParameter("TB_DefVal"));
					// if (this.Pub1.GetCBByID("CB_DefVal").Checked)
					// attr.DefValReal = "1";
					// else
					// attr.DefValReal = "0";
					break;
				case DataType.AppString:
					attr.setUIBindKey(request.getParameter("DDL_TBModel"));
					if (attr.getTBModel() == 2) {
						attr.setMaxLen(4000);
					}
					break;
				default:
					break;
				}
			} else {
				attr = (MapAttr) BaseModel.Copy(request, attr, null,
						attr.getEnMap(), controls);
				// attr = (MapAttr)this.Pub1.Copy(attr);
				attr.setGroupID(Integer.parseInt(request
						.getParameter("DDL_GroupID")));
				attr.setColSpan(Integer.parseInt(request
						.getParameter("DDL_ColSpan")));

				MapAttrs attrS = new MapAttrs(this.getMyPK());
				int idx = 0;
				for (MapAttr en : attrS.ToJavaList()) {
					idx++;
					en.setIdx(idx);
					en.Update();
					if (en.getKeyOfEn() == attr.getKeyOfEn()) {
						throw new RuntimeException("字段已经存在 Key="
								+ attr.getKeyOfEn());
					}
				}
				if (StringHelper.isNullOrEmpty(object.getIDX())) {
					attr.setIdx(0);
				} else {
					// System.out.println("--------"+object.getIDX()==null?"0":object.getIDX());
					// int
					// num=object.getIDX()==null?0:Integer.parseInt(object.getIDX());
					// System.out.println("++++++"+num);
					attr.setIdx(Integer.parseInt(object.getIDX()) - 1);
				}

				attr.setMyDataType(object.getFType());
				switch (object.getFType()) {
				case DataType.AppBoolean:
					attr.setMyDataType(BP.DA.DataType.AppBoolean);
					attr.setUIContralType(UIContralType.CheckBok);
					// attr.DefValOfBool =
					// this.Pub1.GetCBByID("CB_DefVal").Checked;
					String cb = request.getParameter("CB_DefVal");
					if (cb != null && cb.equals("on")) {
						attr.setDefValOfBool(true);
					} else {
						attr.setDefValOfBool(false);
					}
					break;
				case DataType.AppString:
					attr.setUIBindKey(request.getParameter("DDL_TBModel"));
					break;
				default:
					break;
				}
			}

			// 增加是否为空, 对数字类型的字段有效.
			try {
				attr.setMinLen(Integer.parseInt(request
						.getParameter("DDL_IsNull")));
			} catch (java.lang.Exception e3) {
			}

			// 数字签名.
			try {
				// 签名类型.
				attr.setSignType(SignType.forValue(Integer.parseInt(request
						.getParameter("DDL_SignType") == null ? "0" : request
						.getParameter("DDL_SignType"))));

				if (attr.getSignType() == SignType.Pic) {
					attr.setPicType(PicType.forValue(Integer.parseInt(request
							.getParameter("DDL_PicType") == null ? "0"
							: request.getParameter("DDL_PicType")))); // 是否为自动签名
				} else if (attr.getSignType() == SignType.CA) {
					if (StringHelper.isNullOrEmpty(request
							.getParameter("TB_SiganField"))) {
						attr.setPara_SiganField("");
					} else {
						attr.setPara_SiganField(request
								.getParameter("TB_SiganField")); // 数字签名字段.
					}
				}

			} catch (java.lang.Exception e4) {
				e4.printStackTrace();
			}

			// 保存数字签名.
			// response.Buffer = true;
			attr.setFK_MapData(this.getMyPK());
			attr.setMyPK(getRefNo(request));

			// 执行一次update 处理mapdata的计算的业务逻辑.
			MapData md = new MapData(attr.getFK_MapData());
			md.Update();

			attr.Save();

			if (object.getBtnName().equals("Btn_SaveAndClose")) {
				try {
					this.winClose(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else if (object.getBtnName().equals("Btn_SaveAndNew")) {
				try {
					response.sendRedirect(basePath+"WF/MapDef/Do.jsp?DoType=AddF&MyPK="
							+ this.getMyPK() + "&IDX=" + object.getIDX()
							+ "&GroupField=" + attr.getGroupID());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			} else {
			}
			try {
				response.sendRedirect(basePath+"WF/MapDef/EditF.jsp?DoType=Edit&MyPK="
						+ this.getMyPK() + "&RefNo=" + attr.getMyPK()
						+ "&FType=" + object.getFType() + "&GroupField="
						+ attr.getGroupID());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			// try {
			// this.printAlert(response, ex.getMessage());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
	}

}
