package cn.jflow.common.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethods;
import BP.En.UIContralType;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.XML.Search;
import BP.Sys.XML.SearchAttr;
import BP.Sys.XML.Searchs;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Comm.UIRowStyleGlo;
import BP.WF.Template.Btn;
import BP.Web.WebUser;
import BP.XML.XmlEn;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;

/** 
	UCWFRpt 的摘要说明。
 
*/
public class BaseModel {
	
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	public String getParameter(String key) {
		return get_request().getParameter(key);
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public HttpServletRequest get_request() {
		return _request;
	}

	public void set_request(HttpServletRequest _request) {
		this._request = _request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public HttpServletResponse get_response() {
		return _response;
	}

	public void set_response(HttpServletResponse _response) {
		this._response = _response;
	}

	public BaseModel(HttpServletRequest request, HttpServletResponse response) {
		_request = request;
		_response = response;
	}

	public static String getBasePath() {
		return BP.WF.Glo.getCCFlowAppPath();
	}

	public String getParamsStr() {
		return "&" + get_request().getQueryString();
	}

	public static String GenerCaption(String title) {
		if (WebUser.getStyle().equals("2"))
			return "<div class=Table_Title ><span>" + title + "</span></div>";

		return "<b>" + title + "</b>";
	}
	 public void Clear()
     {
         this._Text = null;
         //this.Controls.Clear();
     }
	/** 
	 ath.
	 
	*/
	public String getNoOfObj() {
		if (StringHelper.isNullOrEmpty(getParameter("NoOfObj")))
			return "";
		return getParameter("NoOfObj");
	}

	public long getFID() {
		if (StringHelper.isNullOrEmpty(getParameter("FID")))
			return 0;
		else
			return Long.valueOf(getParameter("FID"));
	}

	public String getFK_Flow() {
		if (StringHelper.isNullOrEmpty(getParameter("FK_Flow")))
			return "";
		return getParameter("FK_Flow");
	}

	public String getMyPK() {
		if (StringHelper.isNullOrEmpty(getParameter("MyPK")))
			return "";
		return getParameter("MyPK");
	}

	public String getPKVal() {
		if (StringHelper.isNullOrEmpty(getParameter("PKVal")))
			return "";
		return getParameter("PKVal");
	}

	public boolean getIsReadonly() {
		String read = getParameter("IsReadonly");
		if (StringHelper.isNullOrEmpty(read))
			return false;
		if (read.equals("1")) {
			return true;
		}
		if (read.equals("0")) {
			return false;
		}
		return Boolean.valueOf(getParameter("IsReadonly"));
	}
	 public  boolean getIsHidden() { // 是否隐藏审核输入框
    	 String IsHidden = getParameter("IsHidden");
    	 if (StringHelper.isNullOrEmpty(IsHidden)){
    		 return false;
    	 }
    	 if (IsHidden.equals("1")){
  		   return true;
    	 }
    	 if (IsHidden.equals("0")) {
 			return false;
 		}
    	 return Boolean.valueOf(getParameter("IsHidden"));
    }

	public String getDelPKVal() {
		if (StringHelper.isNullOrEmpty(getParameter("DelPKVal")))
			return "";
		return getParameter("DelPKVal");
	}

	public String getFK_FrmAttachment() {
		if (StringHelper.isNullOrEmpty(getParameter("FK_FrmAttachment")))
			return "";
		return getParameter("FK_FrmAttachment");
	}

	public String getFK_FrmAttachmentExt() {
		return "ND" + this.getFK_Node() + "_DocMultiAth"; // getParameter("FK_FrmAttachment"];
	}

	public int getFK_Node() {
		String val = getParameter("FK_Node");
		if (!StringHelper.isNullOrEmpty(val)) {
			return Integer.parseInt(val);
		}

		return 0;
	}

	public long getWorkID() {
		String str = getParameter("WorkID");
		if (StringHelper.isNullOrEmpty(str)) {
			str = getParameter("OID");
		}

		if (StringHelper.isNullOrEmpty(str)) {
			str = getParameter("PKVal");
		}
		if (str.endsWith("#tab1")) {
			str = str.replace("#tab1", "");

		}
		if (str.endsWith("#tab2")) {
			str = str.replace("#tab2", "");

		}
		if (str.endsWith("#tab3")) {
			str = str.replace("#tab3", "");

		}
		if (str.endsWith("#tab4")) {
			str = str.replace("#tab4", "");

		}
		return Long.parseLong(str);
	}

	public int getNodeID() {
		try {
			return Integer.parseInt(this.get_request().getParameter("NodeID"));
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public String getFK_MapData() {
		String fk_mapdata = getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata)) {
			fk_mapdata = "ND" + getFK_Node();
		}
		return fk_mapdata;
	}

	public String getAth() {
		if (StringHelper.isNullOrEmpty(getParameter("Ath")))
			return "";
		return _request.getParameter("Ath");
	}

	public String getIsCC() {
		String paras = _request.getParameter("Paras");
		if (!StringHelper.isNullOrEmpty(paras)) {
			if (paras.contains("IsCC=1")) {
				return "1";
			}
		}
		return "ssss";
	}

	public static String PageIdx;

	/** 
	 页面Index.
	 
	*/
	public static int getPageIdx() {
		PageIdx = ContextHolderUtils.getRequest().getParameter("PageIdx");
		if (StringHelper.isNullOrEmpty(PageIdx)) {
			PageIdx = "1";
		}
		return Integer.parseInt(PageIdx);

	}

	public static void setPageIdx(int value) {
		PageIdx = String.valueOf(value);
	}

	public static String ExportDGToExcel(DataTable dt, Map map, String title) throws Exception {
		if (null == dt || null == dt.Rows) {
			return null;
		}
		title = title.trim();

		String fileName = "Ep" + ContextHolderUtils.getRequest().getSession().getId() + ".xls";

		String fileDir = Glo.getIntallPath() + "DataUser/Temp/";

		File f = new File(fileDir);
		//如果导出目录没有建立，则建立.
		if (!f.exists()) {
			f.mkdirs();
		}

		String filePath = fileDir + fileName;
		f = new File(fileName);
		if (f.exists()) {
			f.delete();
		}
		String httpFilePath = Glo.getCCFlowAppPath() + "DataUser/Temp/" + fileName;
		// 第一步，创建一个webbook，对应一个Excel文件  
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		HSSFSheet sheet = wb.createSheet(title);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中  
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
		DataRow dd = dt.Rows.get(0);
		HSSFCell cell = null;

		// 生成标题
		for (int i = 0; i < dd.columns.size(); i++) {
			cell = row.createCell(i);
			String columns = dd.columns.get(i).ColumnName;
			cell.setCellStyle(style);
			for (Attr attr : map.getAttrs()) {
				if (attr.getKey().equalsIgnoreCase(columns)) {
					cell.setCellValue(attr.getDesc());
				}
			}
		}

		int rowsSize = dt.Rows.size();
		int columnsSize = dt.Columns.size();
		for (int i = 0; i < rowsSize; i++) {
			DataRow dr = dt.Rows.get(i);
			row = sheet.createRow((int) i + 1);
			for (int j = 0; j < columnsSize; j++) {
				// 第四步，创建单元格，并设置值  
				try {
					row.createCell(j).setCellValue(dr.getValue(dr.columns.get(j)).toString());
				} catch (Exception e) {}
			}
		}
		// 设置时间
		row = sheet.createRow((int) rowsSize + 2);
		row.createCell(Math.abs(columnsSize - 2)).setCellValue("制表人：" + WebUser.getName() + ",日期：" + DataType.getCurrentDataTimeCNOfShort());
		// 第六步，将文件存到指定位置  
		try {
			FileOutputStream fout = new FileOutputStream(filePath);
			wb.write(fout);
			fout.flush();
			fout.close();
			//            sendRedirect(httpFilePath);
			return httpFilePath;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	public static void writerMsgToClient(String msg) {
		ContextHolderUtils.getResponse().setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = ContextHolderUtils.getResponse().getWriter();
			out.write("<script>" + msg + "</script>");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	protected void WinOpenShowModalDialog(String url, String title, String key, int width, int height, int top, int left) {
		//this.ClientScript.RegisterStartupScript(this.GetType(), key, "<script language='JavaScript'>window.showModalDialog('" + url + "','" + key + "' ,'dialogHeight: 500px; dialogWidth:" + width + "px; dialogTop: " + top + "px; dialogLeft: " + left + "px; center: yes; help: no' ) ;  </script> ");
	}

	public static void winCloseWithMsg(String mess) {
		//this.ResponseWriteRedMsg(mess);
		//return;
		mess = mess.replace("'", "＇");

		mess = mess.replace("\"", "＂");

		mess = mess.replace(";", "；");
		mess = mess.replace(")", "）");
		mess = mess.replace("(", "（");

		mess = mess.replace(",", "，");
		mess = mess.replace(":", "：");

		mess = mess.replace("<", "［");
		mess = mess.replace(">", "］");

		mess = mess.replace("[", "［");
		mess = mess.replace("]", "］");

		mess = mess.replace("@", "\\n@");

		mess = mess.replace("\r\n", "");
		String script = "<script language=JavaScript>alert('" + mess + "'); window.close();</script>";
		try {
			ContextHolderUtils.getResponse().setContentType("text/html; charset=utf-8");
			ContextHolderUtils.getResponse().getWriter().write(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 不用page 参数，show message
	 
	 @param mess
	 * @throws IOException 
	*/
	public static void Alert(String mess) {
		if (StringHelper.isNullOrEmpty(mess)) {
			return;
		}

		mess = mess.replace("@@", "@");
		mess = mess.replace("@@", "@");

		mess = mess.replace("'", "＇");

		mess = mess.replace("\"", "＇");

		mess = mess.replace("\"", "＂");

		mess = mess.replace(";", "；");
		mess = mess.replace(")", "）");
		mess = mess.replace("(", "（");

		mess = mess.replace(",", "，");
		mess = mess.replace(":", "：");

		mess = mess.replace("<", "［");
		mess = mess.replace(">", "］");

		mess = mess.replace("[", "［");
		mess = mess.replace("]", "］");

		mess = mess.replace("'", "‘");

		mess = mess.replace("@", "\\n@");
		String script = "<script language=JavaScript>alert('" + mess + "');</script>";
		try {
			ContextHolderUtils.getResponse().setContentType("text/html; charset=utf-8");
			ContextHolderUtils.getResponse().getWriter().write(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void backPage() {
		writerMsgToClient("history.go(-1)");
	}

	public static void Alert(RuntimeException ex) {
		Alert(ex.getMessage());
	}

	public int getRefOID() {
		String s = this.get_request().getParameter("RefOID");
		if (StringHelper.isNullOrEmpty(s)) {
			s = this.get_request().getParameter("OID");
		}
		if (StringHelper.isNullOrEmpty(s)) {
			return 0;
		}
		return Integer.parseInt(s);
	}

	public String getEnName() {
		return this.get_request().getParameter("EnName");
	}

	public String getEnsName() {
		return this.get_request().getParameter("EnsName");
	}

	public String getRefNo() {
		String s = this.get_request().getParameter("RefNo");
		if (StringHelper.isNullOrEmpty(s)) {
			s = this.get_request().getParameter("No");
		}
		return s;
	}
	public String getRefPK(){
      String s = this.get_request().getParameter("RefPK");
      if (s == null || "".equals(s) )
          s = this.get_request().getParameter("PK");
      return s;
    }
	
	public String getDoType() {
		String str = this.get_request().getParameter("DoType");
		if (StringHelper.isNullOrEmpty(str))
			str = "";
		return str;
	}

	private String _pageID = null;

	public String getPageID() {
		if (StringHelper.isNullOrEmpty(_pageID)) {
			String url = this.get_request().getRequestURL().toString();
			int i = url.lastIndexOf("/") + 1;
			int i2 = url.indexOf(".jsp") - 6;
			try {
				url = url.substring(i);
				_pageID = url.substring(0, url.indexOf(".jsp"));

			} catch (RuntimeException ex) {
				throw new RuntimeException(ex.getMessage() + url + " i=" + i + " i2=" + i2);
			}
		}
		return _pageID;
	}

	//	public  Entity GenerEnValForView(Entity en)
	//	{
	//		Map map = en.getEnMap();
	//		String msg = "";
	//		for (Attr attr : map.getAttrs())
	//		{
	//			String ctlid = "";
	//			if (attr.getUIContralType() == UIContralType.DDL)
	//			{
	//				ctlid = "DDL_" + attr.getKey();
	//			}
	//			else
	//			{
	//				ctlid = "TB_" + attr.getKey();
	//			}
	//
	//			if (this.IsExit(ctlid) == false)
	//			{
	//				continue;
	//			}
	//
	//			try
	//			{
	//				if (attr.getUIContralType() == UIContralType.DDL)
	//				{
	//					try
	//					{
	//						this.GetDDLByID(ctlid).SetSelectItem(en.GetValStrByKey(attr.getKey()));
	//					}
	//					catch (java.lang.Exception e)
	//					{
	//
	//					}
	//
	//					try
	//					{
	//						this.GetDropDownListByID(ctlid).SelectedValue = en.GetValStrByKey(attr.getKey());
	//						continue;
	//					}
	//					catch (java.lang.Exception e2)
	//					{
	//
	//					}
	//					continue;
	//				}
	//
	//				try
	//				{
	//					this.GetTBByID(ctlid).setText(en.GetValStrByKey(attr.getKey()));
	//					this.GetTBByID(ctlid).MaxLength = attr.MaxLength;
	//
	//					continue;
	//				}
	//				catch (java.lang.Exception e3)
	//				{
	//				}
	//
	//				try
	//				{
	//
	//					this.GetTextBoxByID(ctlid).setText(en.GetValStrByKey(attr.getKey()));
	//					this.GetTextBoxByID(ctlid).MaxLength = attr.MaxLength;
	//					continue;
	//				}
	//				catch (java.lang.Exception e4)
	//				{
	//				}
	//			}
	//			catch (RuntimeException ex)
	//			{
	//				msg += ex.getMessage();
	//			}
	//		}
	//		return en;
	//	}
	/** 
	 复制一个新的 Entities .
	 
	 @param ens 包含数据的Ens
	 @return 
	*/
	//	public  Entities Copy(Entities ens)
	//	{
	//		for (Entity en : Entities.convertEntities(ens))
	//		{
	//			this.Copy(en, en.getPKVal().toString());
	//		}
	//		return ens;
	//	}
	//	public  Entity Copy(Entity en)
	//	{
	//		return Copy(en, null);
	//	}
	//	public  Entity Copy(Entity en, String pk)
	//	{
	//		return this.Copy(en, pk, en.getEnMap());
	//	}
	private static void fullValue2(HttpServletRequest request, Attr attr, TextBox tb, Entity en, String key) {
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramKey = (String) params.nextElement();
			if (paramKey.endsWith(tb.getId())) {
				en.SetValByKey(attr.getKey(), request.getParameter(paramKey));
				break;
			}
		}

	}

	private static void fullValue(HttpServletRequest request, TextBox tb, Entity en, String key) {
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramKey = (String) params.nextElement();
			if (paramKey.endsWith(tb.getId())) {
				en.SetValByKey(key, request.getParameter(paramKey));
				break;
			}
		}

	}

	/** 
	 执行复制
	 
	 @param en 实体
	 @param pk 主键
	 @param map 映射
	 @return 数据实体
	*/
	public static Entity Copy(HttpServletRequest request, Entity en, String pk, Map map, HashMap<String, BaseWebControl> controls) {
		if (pk == null || pk.equals("")) {
			pk = "";
		} else {
			return CopyRow(request, en, pk, map, controls);
			// pk = "_" + pk;
		}

		for (BaseWebControl ctl : controls.values()) {
			if (ctl == null || StringHelper.isNullOrEmpty(ctl.getId())) {
				continue;
			}

			String ctlid = ctl.getId();
			String key = null;

		///#region 处理textbox.
			if (ctlid.contains("TB_")) {
				key = ctlid.replace("TB_", "");
				TextBox tb = (TextBox) ((ctl instanceof TextBox) ? ctl : null);
				if (tb != null) {
					if (!tb.getEnabled() || tb.getReadOnly()) {
						if (request.getRequestURI().endsWith("Frm.jsp")) {
							//setValue
							fullValue(request, tb, en, key);
						} else if (request.getRequestURI().endsWith("MyFlow.htm")) {
							fullValue(request, tb, en, key);
						}
					} else {
						en.SetValByKey(key, request.getParameter(tb.getName()));
					}
					continue;
				}

			}

		///#endregion 处理textbox.

		///#region 处理ddl.
			if (ctlid.contains("DDL_")) {
				key = ctlid.replace("DDL_", "");
				DDL ddl = (DDL) ((ctl instanceof DDL) ? ctl : null);
				if (ddl != null) {
					en.SetValByKey(key, request.getParameter(ddl.getName()));
					continue;
				}

			}

		///#endregion 处理ddl.

		///#region 处理 checkbox
			if (ctlid.contains("CB_")) {
				key = ctlid.replace("CB_", "");
				CheckBox cb = (CheckBox) ((ctl instanceof CheckBox) ? ctl : null);
				if (cb != null) {

					String cb_obj = request.getParameter(ctlid);
					if (cb_obj == null) {
						en.SetValByKey(key, 0);
					} else {
						en.SetValByKey(key, 1);
					}
					//					if (cb.getChecked())
					//					{
					//						en.SetValByKey(key, 1);
					//					}
					//					else
					//					{
					//						en.SetValByKey(key, 0);
					//					}
				}
			}

		///#endregion

		///#region 处理 RadioButton.
			if (ctlid.contains("RB_")) {
				RadioButton radio = (RadioButton) ((ctl instanceof RadioButton) ? ctl : null);
				key = ctlid.replace("RB_", "");
				String val = key.substring(key.lastIndexOf('_') + 1);
				if (key.lastIndexOf('_') > 0) {
					key = key.substring(0, key.lastIndexOf('_'));
				}
				if (radio.getChecked()) {
					en.SetValByKey(key, val);
				}
				continue;
			}

		///#endregion

		}

		if (!map.getIsHaveAutoFull()) {
			return en;
		}
		en.AutoFull();
		return en;
	}

	public static Entity CopyRow(HttpServletRequest request, Entity en, String pk, Map map, HashMap<String, BaseWebControl> controls) {
		if (pk == null || pk.equals("")) {
			pk = "";
		} else {
			pk = "_" + pk;
		}

		Attrs attrs = map.getAttrs();
		for (Attr attr : Attrs.convertAttrs(attrs)) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			String ctlid = "";
			switch (attr.getUIContralType()) {
			case TB:
				ctlid = "TB_" + attr.getKeyLowerCase() + pk;
				String key = ctlid.replace("TB_", "");
				BaseWebControl ctl = controls.get(ctlid);
				if (ctl == null) {
					continue;
				}

				TextBox tb = (TextBox) ((ctl instanceof TextBox) ? ctl : null);
				if (tb != null) {

					if (!tb.getEnabled() || tb.getReadOnly()) {
						if (request.getRequestURI().endsWith("Frm.jsp")) {
							//setValue
							fullValue2(request, attr, tb, en, key);
						} else if (request.getRequestURI().endsWith("MyFlow.htm")) {
							fullValue2(request, attr, tb, en, key);
						}else{//2016年7月20日 yqh修改，此处在预览时保存字表数据 /jflow-web/WF/CCForm/DtlSave.do
							fullValue2(request, attr, tb, en, key);
						}
					} else {
						en.SetValByKey(attr.getKey(), request.getParameter(tb.getName()));
					}
					continue;
				}

				break;
			case DDL:
				ctlid = "DDL_" + attr.getKey() + pk;
				BaseWebControl ctl_ddl = controls.get(ctlid);
				DDL ddl = (DDL) ((ctl_ddl instanceof DDL) ? ctl_ddl : null);
				if (ddl != null) {
					en.SetValByKey(attr.getKey(), request.getParameter(ddl.getName()));
					continue;
				}

			case CheckBok:
				ctlid = "CB_" + attr.getKeyLowerCase() + pk;
				BaseWebControl ctl_cb = controls.get(ctlid);
				CheckBox cb = (CheckBox) ((ctl_cb instanceof CheckBox) ? ctl_cb : null);
				if (cb != null) {
					String cb_obj = request.getParameter(ctlid);
					if (cb_obj == null) {
						en.SetValByKey(attr.getKeyLowerCase(), 0);
					} else {
						en.SetValByKey(attr.getKeyLowerCase(), 1);
					}
				}
				break;
			case RadioBtn:
				if (attr.getIsEnum()) {
					SysEnums ses = new SysEnums(attr.getUIBindKey());
					for (SysEnum se : ses.convertSysEnums(ses)) {
						String id = "RB_" + attr.getKey() + "_" + se.getIntKey();
						BaseWebControl ctl_rb = controls.get(id);
						RadioButton rb = (RadioButton) ((ctl_rb instanceof CheckBox) ? ctl_rb : null);

					///#region 如果是空的,有可能是标记它是 rb 但是它用的ddl 显示的.
						if (rb == null) {
							ctlid = "DDL_" + attr.getKey() + pk;
							BaseWebControl ctl_ddl_rb = controls.get(ctlid);
							DDL myddlrb = (DDL) ((ctl_ddl_rb instanceof DDL) ? ctl_ddl_rb : null);
							if (myddlrb != null) {
								en.SetValByKey(attr.getKey(), request.getParameter(myddlrb.getName()));
								break;
							}

						}

					///#endregion 如果是空的

						if (rb != null && rb.getChecked()) {
							en.SetValByKey(attr.getKey(), se.getIntKey());
							break;
						}
					}
				}
				if (attr.getMyFieldType() == FieldType.FK) {
					Entities ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
					ens.RetrieveAll();
					for (Entity enNoName : Entities.convertEntities(ens)) {
						String id2 = attr.getKey() + "_" + enNoName.GetValStringByKey(attr.getUIRefKeyValue());
						BaseWebControl ctl_rb2 = controls.get(id2);
						RadioButton rb = (RadioButton) ((ctl_rb2 instanceof CheckBox) ? ctl_rb2 : null);
						if (rb != null && rb.getChecked()) {
							en.SetValByKey(attr.getKey(), enNoName.GetValStrByKey(attr.getUIRefKeyValue()));
							break;
						}
					}
				}
				break;
			default:
				break;
			}

			if (attr.getMyDataType() == DataType.AppBoolean) {
				ctlid = "RB_" + attr.getKey() + pk + "_1";
				boolean isOk = true;
				if (controls.containsKey(ctlid)) {
					BaseWebControl ctl_rb3 = controls.get(ctlid);
					RadioButton rb = (RadioButton) ((ctl_rb3 instanceof CheckBox) ? ctl_rb3 : null);
					if (rb != null) {
						String cb_obj = request.getParameter(ctlid);
						if (cb_obj == null) {
							en.SetValByKey(attr.getKeyLowerCase(), 0);
						} else {
							en.SetValByKey(attr.getKeyLowerCase(), 1);
						}
					}
					continue;
				}

				ctlid = "CB_" + attr.getKey() + pk;
				if (controls.containsKey(ctlid)) {
					BaseWebControl tempVar2 = controls.get(ctlid);
					CheckBox cb = (CheckBox) ((tempVar2 instanceof CheckBox) ? tempVar2 : null);
					if (cb != null) {
						String cb_obj = request.getParameter(ctlid);
						if (cb_obj == null) {
							en.SetValByKey(attr.getKeyLowerCase(), 0);
						} else {
							en.SetValByKey(attr.getKeyLowerCase(), 1);
						}
					}
					continue;
				}
			}
		}
		if (!map.getIsHaveAutoFull()) {
			return en;
		}
		en.AutoFull();
		return en;
	}

	/** 
	 重设置里面的信息
	 
	 @param en
	*/
	//	public  void ResetEnVal(Entity en)
	//	{
	//		Attrs attrs = en.getEnMap().getAttrs();
	//		for (Attr attr : attrs)
	//		{
	//			String ctlid = "";
	//			switch (attr.getUIContralType())
	//			{
	//				case UIContralType.TB:
	//					ctlid = "TB_" + attr.getKey();
	//					TB tb = this.GetTBByID(ctlid);
	//					if (tb != null)
	//					{
	//						tb.setText(en.GetValStrByKey(attr.getKey()));
	//						continue;
	//					}
	//
	//					TextBox mytb = this.GetTextBoxByID(ctlid);
	//					if (mytb != null)
	//					{
	//						tb.setText(en.GetValStrByKey(attr.getKey()));
	//						continue;
	//					}
	//
	//					break;
	//				case UIContralType.DDL:
	//					try
	//					{
	//						ctlid = "DDL_" + attr.getKey();
	//						DDL ddl = this.GetDDLByID(ctlid);
	//						if (ddl != null)
	//						{
	//							ddl.SetSelectItem(en.GetValStrByKey(attr.getKey()));
	//							continue;
	//						}
	//
	//						DropDownList myddl = this.GetDropDownListByID(ctlid);
	//						if (myddl != null)
	//						{
	//							ddl.SetSelectItem(en.GetValStrByKey(attr.getKey()));
	//						}
	//					}
	//					catch (java.lang.Exception e)
	//					{
	//
	//					}
	//					continue;
	//				case UIContralType.CheckBok:
	//					ctlid = "CB_" + attr.getKey();
	//					CheckBox cb = this.GetCBByID(ctlid);
	//					if (cb != null)
	//					{
	//						cb.Checked = en.GetValBooleanByKey(attr.getKey());
	//					}
	//					break;
	//				default:
	//					break;
	//			}
	//		}
	//	}
	public void BindTable(DataTable dt) {
		AddTable();
		AddTR();
		for (DataColumn dc : dt.Columns) {
			AddTDTitle(dc.ColumnName);
		}
		AddTREnd();
		for (DataRow dr : dt.Rows) {
			AddTR();
			for (DataColumn dc : dt.Columns) {
				AddTD(dr.getValue(dc.ColumnName).toString());
			}
			AddTREnd();
		}
		AddTableEnd();
	}

	//	public  void BindEns(Entities ens)
	//	{
	//		Attrs attrs = ens.GetNewEntity().getEnMap().getAttrs();
	//		this.AddTable();
	//		this.AddTR();
	//		for (Attr attr : attrs)
	//		{
	//			if (attr.getKey().equals("MyNum") || attr.UIIsDoc == true)
	//			{
	//				continue;
	//			}
	//
	//			if (attr.IsRefAttr || attr.UIVisible == false)
	//			{
	//				continue;
	//			}
	//
	//			this.AddTDTitle(attr.Desc);
	//		}
	//		this.AddTREnd();
	//
	//		boolean is1 = false;
	//		for (Entity en : ens)
	//		{
	//			is1 = this.AddTR(is1);
	//			for (Attr attr : attrs)
	//			{
	//				if (attr.getKey().equals("MyNum") || attr.UIIsDoc == true)
	//				{
	//					continue;
	//				}
	//
	//				if (attr.IsRefAttr || attr.UIVisible == false)
	//				{
	//					continue;
	//				}
	//
	//				if (attr.UIHeight != 0 && attr.IsNum == false)
	//				{
	//					continue;
	//				}
	//
	//				switch (attr.MyDataType)
	//				{
	//					case DataType.AppFloat:
	//					case DataType.AppDouble:
	//					case DataType.AppInt:
	//						this.AddTDNum(en.GetValStringByKey(attr.getKey()));
	//						break;
	//					case DataType.AppMoney:
	//						this.AddTDNum(en.GetValDecimalByKey(attr.getKey()).ToString("0.00"));
	//						break;
	//					default:
	//						this.AddTD(en.GetValStrByKey(attr.getKey()));
	//						break;
	//				}
	//			}
	//			this.AddTREnd();
	//
	//		}
	//
	//		this.AddTableEnd();
	//	}
	//	public  void BindCard(Entity en)
	//	{
	//		this.BindCard(en, "Table");
	//	}
	//	public  void BindCard(Entity en, String tableClass)
	//	{
	//		if (en.HisUAC.IsView == false)
	//		{
	//			this.AddMsgOfWarning("提示", "您没有查看此该数据的权限。");
	//			return;
	//		}
	//
	//		Attrs attrs = en.getEnMap().getAttrs();
	//		return "<table class='" + tableClass + "' >");
	//		boolean is1 = false;
	//		for (Attr attr : attrs)
	//		{
	//			if (attr.MyFieldType == FieldType.RefText)
	//			{
	//				continue;
	//			}
	//
	//			if (attr.UIVisible == false)
	//			{
	//				continue;
	//			}
	//
	//			if (attr.getKey().equals("MyNum") || attr.getKey().equals("Doc"))
	//			{
	//				continue;
	//			}
	//
	//			is1 = this.AddTR(is1);
	//			if (attr.getUIContralType() == UIContralType.TB)
	//			{
	//				if (attr.MyDataType == DataType.AppString && attr.MaxLength > 400)
	//				{
	//					this.AddTD("class=BigDoc colspan=2", "<B>" + attr.Desc + "</b><BR>" + en.GetValHtmlStringByKey(attr.getKey()));
	//				}
	//				else
	//				{
	//					this.AddTD("<B>" + attr.Desc + "</b>");
	//					this.AddTD(en.GetValStrByKey(attr.getKey()));
	//
	//					//if (attr.Key == "URL" || attr.Key == "Email")
	//					//{
	//					//    String val1 = en.GetValStringByKey(attr.Key);
	//					//    if (attr.Key == "URL")
	//					//    {
	//					//        this.AddTD("<a href='http://" + val1.ToUpper().Replace("HTTP://", "") + "' target=_b >" + val1 + "</a>");
	//					//    }
	//					//    else
	//					//    {
	//					//        this.AddTD("<a href='mailto:" + val1 + "' >" + val1 + "</a>");
	//					//    }
	//					//}
	//					//else
	//					//{
	//					//    this.AddTD(en.GetValStringByKey(attr.Key));
	//					//}
	//				}
	//			}
	//			else if (attr.getUIContralType() == UIContralType.CheckBok)
	//			{
	//				this.AddTD("<B>" + attr.Desc + "</b>");
	//
	//				if (en.GetValBooleanByKey(attr.getKey()))
	//				{
	//					this.AddTD("是");
	//				}
	//
	//				else
	//				{
	//					this.AddTD("否");
	//				}
	//			}
	//			else if (attr.getUIContralType() == UIContralType.DDL)
	//			{
	//
	//				this.AddTD("<B>" + attr.Desc + "</b>");
	//				this.AddTD(en.GetValRefTextByKey(attr.getKey()));
	//			}
	//
	//			this.AddTREnd();
	//		}
	//		this.AddTableEnd();
	//	}

	/** 
	 使用EasyUI中的easyui-pagination分页组件进行分页显示
	 <p></p>
	 <p>注意：此方法需要页面引入easyui库才有效</p>
	 <p>added by liuxc,2014-11-3</p>
	 
	 @param totalRecords 记录总条数
	 @param pageSize 每页记录数
	 @param pageIdx 当前页码
	 @param url 翻页时中转的URL
	 @param layout 分页显示布局，此设置请参考EasyUi中的说明[EasyUI v1.3.5之后版本支持此属性]
	 <p></p>
	 <p>格式如：'list','sep','first','prev','sep','manual','sep','next','last','sep','refresh'</p>
	 <p>1) list: 显示分页条数列表[10,20,30,50].</p>
	 <p>2) sep: 分隔符.</p>
	 <p>3) first: 第一页.</p>
	 <p>4) prev: 前一页.</p>
	 <p>5) next: 后一页.</p>
	 <p>6) last: 最末页.</p>
	 <p>7) refresh: 刷新按钮.</p>
	 <p>8) manual: 允许用户输入页码的文本框.</p>
	 <p>9) links: 显示10个页码链接.</p>
	  
	 @param showParentPanel 是否显示分页外层的easyui-panel
	*/
	//ORIGINAL LINE: public void BindPageIdxEasyUi(int totalRecords, String url, int pageIdx, int pageSize = 10, String layout = "'first','prev','sep','manual','sep','next','last'", bool showParentPanel = false)
	public static void BindPageIdxEasyUi(StringBuilder ctrl, int totalRecords, String url, int pageIdx, int pageSize, String layout,
			boolean showParentPanel) {
		ctrl.append("    <style type='text/css'>" + "        #eupage table,#eupage td" + "        {" + "            border: 0;"
				+ "            padding: 0;" + "            text-align: inherit;" + "            background-color: inherit;"
				+ "            color: inherit;" + "            font-size: inherit;" + "        }" + "    </style>");

		if (showParentPanel) {
			ctrl.append("<div class='easyui-panel'>");
		}

		ctrl.append(String
				.format("<div id='eupage' class='easyui-pagination' data-options=\"total: %1$s,pageSize: %2$s,pageNumber: %3$s,showPageList: false,showRefresh: false,layout: [%4$s],beforePageText: '第&nbsp;',afterPageText: '&nbsp;/ {pages} 页',displayMsg: '显示 {from} 到 {to} 条，共 {total} 条'\"></div>",
						totalRecords, pageSize, pageIdx, layout));

		if (showParentPanel) {
			ctrl.append("</div>");
		}

		ctrl.append("<script type='text/javascript'>");
		ctrl.append(String.format(
				"$('#eupage').pagination({	onSelectPage:function(pageNumber, pageSize){		location.href='%1$s&PageIdx=' + pageNumber	}});", url));
		ctrl.append("</script>");
	}

	public static int BindPageIdx(StringBuilder ctrl, int recNum, int pageSize, int PageIdx, String url) {
		return BindPageIdx(ctrl, recNum, pageSize, PageIdx, url, 20);
	}
	
	 /*public static int BindPageIdx(int recNum, int pageSize, int PageIdx, String url)
     {
         return BindPageIdx(recNum, pageSize, PageIdx, url, 20);
     }*/
	/** 
	 分页
	 
	 @param recNum 记录个数
	 @param pageSize 叶面大小
	 @param PageIdx
	 @param url
	 @return 返回最大页面数
	*/
	public static int BindPageIdx(StringBuilder ctrl, int recNum, int pageSize, int PageIdx, String url, int pageSpan) {
		if (recNum <= pageSize){
			return 1;
		}
		ctrl.append("<div style='text-align:center;'>");
		String appPath = Glo.getCCFlowAppPath();
		int myidx = 0;

		if (PageIdx <= 1) {
			//ctrl.append("<div class='PageIdx'>《《— 《—");
			ctrl.append("<img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/LeftEnd.png' border=0/><img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/Left.png' border=0/>");
			
		} else {
			myidx = PageIdx - 1;
			//ctrl.append("<div class='PageIdx'><a href='" + url + "&PageIdx=1' >《《—</a> <a href='" + url + "&PageIdx=" + (PageIdx - 1) + "'>《—</a>");
			ctrl.append("<a href='" + url + "&PageIdx=1' ><img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/LeftEnd.png' border=0/></a><a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/Left.png' border=0/></a>");


		}

		int pageNum = 0;// 总页数 
		if (recNum % pageSize == 0) {
			pageNum = recNum / pageSize;
		} else {
			pageNum = recNum / pageSize + 1;
		}

		List<Integer> arry = getPageList(PageIdx, pageNum, 5);
		for (int i : arry) {
			if (PageIdx == i) {
				ctrl.append("&nbsp;<font size='2'><a href='" + url + "&PageIdx=" + i + "'>" + i + "</a></font>");
			} else {
				ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + i + "'>" + i + "</a>");
			}
		}

		if (PageIdx != pageNum) {
			myidx = PageIdx + 1;

			//ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + (PageIdx + 1) + "'>—》</a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum
			//+ "'>—》》</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Total:" + recNum + ".</div>");
			ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/Right.png' border=0/></a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;页数:" + PageIdx + "/" + pageNum + "&nbsp;&nbsp;总数:" + recNum);
			
		} else {
			//ctrl.append("&nbsp; —》 —》》&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".</div>");
			ctrl.append("&nbsp;<img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/Right.png' border=0/>&nbsp;&nbsp;");
			ctrl.append("&nbsp;<img style='vertical-align:middle' src='"+appPath+"/WF/Img/Arr/RightEnd.png' border=0/>&nbsp;&nbsp;页数:" + PageIdx + "/" + pageNum + "&nbsp;&nbsp;总数:" + recNum);
		}

		//		String appPath = Glo.getCCFlowAppPath();
		//		int myidx = 0;
		//		if (PageIdx <= 1)
		//		{
		//			ctrl.append("《《— 《—");
		//
		//			// ctrl.append("<img src='/WF/Img/Arr/LeftEnd.png' border=0/><img src='/WF/Img/Arr/Left.png' border=0/>");
		//
		//		}
		//		else
		//		{
		//			
		//			myidx = PageIdx - 1;
		//			ctrl.append("<a href='" + url + "&PageIdx=1' >《《—</a> <a href='" + url + "&PageIdx=" + myidx + "'>《—</a>");
		//
		//			//ctrl.append("<a href='" + url + "&PageIdx=1' ><img src='/WF/Img/Arr/LeftEnd.png' border=0/></a> <a href='" + url + "&PageIdx=" + myidx + "'><img src='/WF/Img/Arr/Left.png' border=0/></a>");
		//
		//		}
		//
		//		int pageNum = 0;
		//		DecimalFormat mformat = new DecimalFormat("0.0000"); 
		//		java.math.BigDecimal pageCountD = java.math.BigDecimal.valueOf(recNum / pageSize); // 页面个数。
		//		String[] strs = mformat.format(pageCountD).split("[.]", -1);
		//		if (Integer.parseInt(strs[1]) > 0)
		//		{
		//			pageNum = Integer.parseInt(strs[0]) + 1;
		//		}
		//		else
		//		{
		//			pageNum = Integer.parseInt(strs[0]);
		//		}
		//
		//		int from = 0;
		//		int to = 0;
		//
		//		java.math.BigDecimal spanTemp = java.math.BigDecimal.valueOf(PageIdx / pageSpan); // 页面个数。
		//
		//		strs = mformat.format(spanTemp).split("[.]", -1);
		//		from = Integer.parseInt(strs[0]) * pageSpan;
		//		to = from + pageSpan;
		//		for (int i = 1; i <= pageNum; i++)
		//		{
		//			if (i >= from && i < to)
		//			{
		//			}
		//			else
		//			{
		//				continue;
		//			}
		//
		//			if (PageIdx == i)
		//			{
		//				ctrl.append("&nbsp;<font style='font-weight:bloder;color:#f00'>" + i + "</font>&nbsp;");
		//			}
		//			else
		//			{
		//				ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + i + "'>" + i + "</a>");
		//			}
		//		}
		//
		//		if (PageIdx != pageNum)
		//		{
		//			myidx = PageIdx + 1;
		//			ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'>-》</a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'>-》</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Total:" + recNum + ".");
		//			//ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img src='/WF/Img/Arr/Right.png' border=0/></a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img src='/WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Total:" + recNum + ".");
		//		}
		//		else
		//		{
		//			ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'> -》》</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".");
		//			//ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img src='/WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".");
		//			//ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img src='/WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".");
		//			// ctrl.append("<img src='/WF/Img/Page_Down.gif' border=1 />");
		//		}
		ctrl.append("</div>");

		return pageNum;
		//this.UCPub3.AddTDEnd();
		//this.UCPub3.AddTREnd();
		//this.UCPub3.AddTableEnd();
	}

	private static List<Integer> getPageList(int currentPage, int totalPage, int pageListSize) {
		List<Integer> arry = new ArrayList<Integer>();
		if (totalPage > pageListSize) {
			int halfSize = pageListSize / 2;
			int first = 1;
			int end = 1;
			if (currentPage - halfSize < 1) { // 当前页靠近最小数1  
				first = 1;
				end = pageListSize;
			} else if (totalPage - currentPage < halfSize) { // 当前页靠近最大数  
				first = totalPage - pageListSize + 1;
				end = totalPage;
			} else {
				first = currentPage - halfSize;
				end = currentPage + halfSize;
			}
			for (int i = first; i <= end; i++) {
				arry.add(i);
			}
		} else {
			for (int i = 0; i < totalPage; i++) {
				arry.add(i + 1);
			}
		}

		return arry;
	}

	public static int BindPageIdx_ver1(StringBuilder ctrl, int recNum, int pageSize, int PageIdx, String url) {
		//		int pageSpan = 20;
		//		if (recNum <= pageSize)
		//		{
		//			ctrl.append("<div class=PageIdx><ul><li href=#>首页</li> <li href=#>上一页</li> <li href=#>下一页</li> <li href=#>尾页</li>,<li href=#>共" + recNum + "</li>条.</DIV>");
		//			return 1;
		//		}

		//int PageIdx=1;
		//if (this.Request.QueryString["PageIdx"]==null)
		//    PageIdx=1;
		//else
		//    PageIdx = int.Parse(this.Request.QueryString["PageIdx"]);
		//if (recNum < PageIdx*pageSize)
		//	PageIdx= recNum/pageSize;
		//this.UCPub3.Clear();
		//this.UCPub3.AddTableRed();
		//this.UCPub3.AddTR();
		//this.UCPub3.Add("<TD class=TD>");

		if (recNum <= pageSize)
			return 1;
		if (PageIdx <= 1) {
			ctrl.append("<div class='PageIdx'>首页 上一页");
		} else {
			ctrl.append("<div class='PageIdx'><a href='" + url + "&PageIdx=1' >首页</a> <a href='" + url + "&PageIdx=" + (PageIdx - 1) + "'>上一页</a>");
		}

		int pageNum = 0;// 总页数 
		if (recNum % pageSize == 0) {
			pageNum = recNum / pageSize;
		} else {
			pageNum = recNum / pageSize + 1;
		}

		if (PageIdx != pageNum) {
			ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + (PageIdx + 1) + "'>下一页</a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum
					+ "'>尾页</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".</div>");
		} else {
			ctrl.append("&nbsp;下一页&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'>尾页</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:"
					+ recNum + ".</div>");
		}

		//		int myidx = 0;
		//		if (PageIdx <= 1)
		//		{
		//			ctrl.append("<div class=PageIdx><li href=#>首页</li> <li href=#>上一页</li>");
		//			//  ctrl.append("&nbsp;<img src='/WF/Img/Page_Up.gif' border=1 />");
		//		}
		//		else
		//		{
		//			myidx = PageIdx - 1;
		//			//ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img src='/WF/Img/Page_Up.gif' border=0 /></a>");
		//			ctrl.append("<div class=PageIdx><li><a href='" + url + "&PageIdx=1'>首页</a></li> <li><a href='" + url + "&PageIdx=" + myidx + "'>上一页</a></li>");
		//		}
		//
		//		int pageNum = 0;
		//		 DecimalFormat mformat = new DecimalFormat("0.0000");  
		//		java.math.BigDecimal pageCountD = java.math.BigDecimal.valueOf(recNum / pageSize); // 页面个数。
		//		String[] strs = mformat.format(pageCountD).split("[.]", -1);
		//		if (Integer.parseInt(strs[1]) > 0)
		//		{
		//			pageNum = Integer.parseInt(strs[0]) + 1;
		//		}
		//		else
		//		{
		//			pageNum = Integer.parseInt(strs[0]);
		//		}
		//
		//		int from = 0;
		//		int to = 0;
		//
		//		 //DecimalFormat mformat = new DecimalFormat("0.0000");  
		//		java.math.BigDecimal spanTemp = java.math.BigDecimal.valueOf(PageIdx /pageSpan); // 页面个数。
		//
		//		strs = mformat.format(spanTemp).split("[.]", -1);
		//		from = Integer.parseInt(strs[0]) * pageSpan;
		//		to = from + pageSpan;
		//		for (int i = 1; i <= pageNum; i++)
		//		{
		//			if (i >= from && i < to)
		//			{
		//			}
		//			else
		//			{
		//				continue;
		//			}
		//
		//			if (PageIdx == i)
		//			{
		//				ctrl.append("<li><a class=pickred>" + i + "</a></li>");
		//			}
		//			else
		//			{
		//				ctrl.append("<li><a><a href='" + url + "&PageIdx=" + i + "'>" + i + "</a></li>");
		//			}
		//		}
		//		if (PageIdx != pageNum)
		//		{
		//			myidx = PageIdx + 1;
		//			// ctrl.append("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img src='/WF/Img/Page_Down.gif' border=0 /></a>&nbsp;第" + PageIdx + "/" + pageNum + "页");
		//			ctrl.append("<li><a href='" + url + "&PageIdx=" + myidx + "'>下一页</a></li> <li>第" + PageIdx + "/<a href='" + url + "&PageIdx=" + pageNum + "'>" + pageNum + "</a>页</li> <li><a href='" + url + "&PageIdx=" + pageNum + "'>尾页</a>, " + recNum + "条.</li></div>");
		//		}
		//		else
		//		{
		//			ctrl.append("<li>下一页</li> <li>第" + PageIdx + "/" + pageNum + "页</li> <li><a href='" + url + "&PageIdx=" + pageNum + "'>尾页</a></li>,<li>共" + recNum + "条.</li></div>");
		//			// ctrl.append("<img src='/WF/Img/Page_Down.gif' border=1 />");
		//		}
		return pageNum;
		//this.UCPub3.AddTDEnd();
		//this.UCPub3.AddTREnd();
		//this.UCPub3.AddTableEnd();
	}

	//	public  RadioBtn GetRadioBtnByID(String id)
	//	{
	//		return (RadioBtn)this.FindControl(id);
	//	}
	//	public  RadioButton GetRadioButtonByID(String id)
	//	{
	//		return (RadioButton)this.FindControl(id);
	//	}
	//	public  CheckBox GetCBByID(String id)
	//	{
	//		return (CheckBox)this.FindControl(id);
	//	}
	//	public  Label GetLabelByID(String id)
	//	{
	//		Object tempVar = this.FindControl(id);
	//		return (Label)((tempVar instanceof Label) ? tempVar : null);
	//	}
	//	public  TB GetTBByID(String key)
	//	{
	//		try
	//		{
	//			return (TB)this.FindControl(key);
	//		}
	//		catch (RuntimeException ex)
	//		{
	//			throw new RuntimeException(ex.getMessage() + " 请确认：TB AND TextBox " + key);
	//		}
	//	}
	//	public  TextBox GetTextBoxByID(String key)
	//	{
	//		return (TextBox)this.FindControl(key);
	//	}
	//	public  RadioButton GetRBLByID(String id)
	//	{
	//		Object tempVar = this.FindControl(id);
	//		return (RadioButton)((tempVar instanceof RadioButton) ? tempVar : null);
	//	}
	//	public  RadioButtonList GetRadioButtonListByID(String id)
	//	{
	//		return (RadioButtonList)this.FindControl(id);
	//	}
	//	public  DDL GetDDLByID(String key)
	//	{
	//		return (DDL)this.FindControl(key);
	//	}
	//	public  DropDownList GetDropDownListByID(String key)
	//	{
	//		return (DropDownList)this.FindControl(key);
	//	}
	//	public  ImageButton GetImageButtonByID(String key)
	//	{
	//		return (ImageButton)this.FindControl(key);
	//	}
	//	public  void EnableAllBtn(boolean isEnable)
	//	{
	//		for (System.Web.UI.Control c : this.Controls)
	//		{
	//			Btn btn = (Btn)((c instanceof Btn) ? c : null);
	//			if (btn != null)
	//			{
	//				btn.setEnabled(isEnable);
	//				continue;
	//			}
	//			Button myBtn = (Button)((c instanceof Button) ? c : null);
	//			if (myBtn != null)
	//			{
	//				myBtn.setEnabled(isEnable);
	//				continue;
	//			}
	//		}
	//	}
	//
	//	public  LinkBtn GetLinkBtnByID(String key)
	//	{
	//		return (LinkBtn) this.FindControl(key);
	//	}
	//
		public static Btn GetBtnByID(String key)
		{
			return new Btn(key);
			//(Button)FindControl(key);
		}
		
		public static DDL GetDDLByID(String key)
		{
			UiFatory uiFactory = new UiFatory();
			DDL ddl = uiFactory.creatDDL(key);
			uiFactory.append(ddl);
			return ddl;
		}
	
		public  Button GetButtonByID(String key)
		{
			//Object tempVar = this.FindControl(key);
			return new Button(key);
		}
	//
	//	public static String AddContralDDL(DDL ddl)
	//	{
	//		return "<TD class=TD >");
	//		this.Controls.Add(ddl);
	//		if (ddl.getEnabled())
	//		{
	//			String srip = "javascript:HalperOfDDL('" + ddl.getAppPath() + "','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.ClientID.toString() + "' ); ";
	//			//this.Controls.Add( new LiteralControl("<a href=\"javascript:"+srip+"\" >aaaa</a></td>") ); 
	//			return "<input type='button' value='...' onclick=\"" + srip + "\"  name='b" + ddl.ID + "'  >");
	//		}
	//		return "</TD>");
	//	}
	//	public static String AddTDDocCard(String str)
	//	{
	//		return "\n<TD   >" + str + "</TD>");
	//	}
	//	public static String AddContral(String desc, DDL ddl, boolean isRefBtn)
	//	{
	//		return "<td class='FDesc' nowrap width=1% > " + desc + "</td><td class=TD nowrap>");
	//		this.Controls.Add(ddl);
	//		if (ddl.getEnabled())
	//		{
	//			if (ddl.getSelfBindKey().indexOf(".") == -1)
	//			{
	//				this.AddTDEnd();
	//			}
	//			else
	//			{
	//				if (isRefBtn && ddl.Items.size() > 15)
	//				{
	//					String srip = "javascript:HalperOfDDL('" + ddl.getAppPath() + "','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.ClientID.toString() + "' ); ";
	//					return "<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.ID + "' ></td>");
	//				}
	//				else
	//				{
	//					this.AddTDEnd();
	//				}
	//			}
	//		}
	//		else
	//		{
	//			this.AddTDEnd();
	//		}
	//	}
	//	public static String AddContral(String desc, DDL ddl, boolean isRefBtn, int colspan)
	//	{
	//		return "<td class='FDesc' nowrap width=1% > " + desc + "</td><td  colspan=" + colspan + " nowrap>");
	//		this.Controls.Add(ddl);
	//		if (ddl.getEnabled())
	//		{
	//			if (ddl.getSelfBindKey().indexOf(".") == -1)
	//			{
	//				this.AddTDEnd();
	//			}
	//			else
	//			{
	//				if (isRefBtn && ddl.Items.size() > 4)
	//				{
	//					String srip = "javascript:HalperOfDDL('" + ddl.getAppPath() + "','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.ClientID.toString() + "' ); ";
	//					return "<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.ID + "' ></td>");
	//				}
	//				else
	//				{
	//					this.AddTDEnd();
	//				}
	//			}
	//		}
	//		else
	//		{
	//			this.AddTDEnd();
	//		}
	//	}
	/** 
	 ParseControl
	 
	*/
	//	protected  void ParseControl()
	//	{
	//		this.Controls.Add(this.ParseControl(this.getText()));
	//	}
	public static String AddIframeAutoSize(String url, String frmID, String tdID) {
		String ss = "<iframe ID='"
				+ frmID
				+ "' src='"
				+ url
				+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%'  height='100%' scrolling=no /></iframe>";
		String js = "\t\n<script type='text/javascript' >";
		{
			js += "\t\n window.setInterval(\"ReinitIframe('" + frmID + "','" + tdID + "')\", 200);";
		}
		js += "\t\n</script>";
		return ss + js;
	}

	public static String AddIframeExt(String url, String attrs) {
		//return "<iframe frameborder=1 leftMargin='0'  onload=\"onloadIt('fm')\"  topMargin='0' src='" + url + "' width='100%' height='100%' class=iframe name=fm style='border-style:none;' id=fm > </iframe>");
		return "<iframe   src='" + url + "' " + attrs + "   id=fm > </iframe>";
	}

	public static String AddIframeWithOnload(String url) {
		//return "<iframe frameborder=1 leftMargin='0'  onload=\"onloadIt('fm')\"  topMargin='0' src='" + url + "' width='100%' height='100%' class=iframe name=fm style='border-style:none;' id=fm > </iframe>");
		return "<iframe frameborder=1 leftMargin='0'  topMargin='0' src='" + url
				+ "' width='100%' height='100%' class=iframe name=fm style='border-style:none;' id=fm > </iframe>";
	}

	public static String AddIframe(String url) {
		return "<iframe frameborder=1 leftMargin='0' topMargin='0' src='" + url
				+ "' width='100%' height='100%' class=iframe name=fm style='border-style:none;' id=fm > </iframe>";
	}

	public static String AddIframe(String title, String url) {
		return "<span class=TD >" + title + "</span>" + "<iframe leftMargin='0' topMargin='0' src='" + url
				+ "' width='100%' height='100%' class=iframe  name=fm > </iframe>";
	}

	public static String AddIframeItem3(String enName, String pk, String title) {
		return AddIframe(title, "Comm/Item3.jsp?EnName=" + enName + "&PK=" + pk);
	}

	public static String AddIframeItem3(String enName, String pk) {
		return AddIframe("Comm/Item3.jsp?EnName=" + enName + "&PK=" + pk);
	}

	public static String AddFieldSetRound(String title) {
		return "<table cellpadding='0' cellspacing='0'><tr><td><fieldset align='center'><legend>" + title + " </legend>";
	}

	public static String AddFieldSetRoundEnd() {
		return "</fieldset></td></tr></table>";
	}

	//	public static String AddFieldSet(RadioButton rb)
	//	{
	//		return "<fieldset ><legend>");
	//		ctrl.append(rb);
	//		return "</legend>");
	//	}
	public static String AddFieldSet(String title) {
		return "<fieldset width='100%' ><legend>&nbsp;" + title + "&nbsp;</legend>";
	}

	public static String AddFieldSetGreen(String title) {
		return "<fieldset ><legend>&nbsp;<font color=green><b>" + title + "</b></font>&nbsp;</legend>";
	}

	public static String AddFieldSet(String title, String doc) {
		String ss = "<fieldset ><legend>&nbsp;" + title + "&nbsp;</legend>";

		ss += doc;

		ss += AddFieldSetEnd();
		return ss;
	}

	public void LoadPop_del() {

		//          this.Page.RegisterClientScriptBlock("sdds",
		//"<link href='/WF/Comm/JS/jquery-easyui/themes/default/easyui.css' rel='stylesheet' type='text/css' />");

		//          this.Page.RegisterClientScriptBlock("db7",
		//       "<script language='JavaScript' src='" + this.Request.ApplicationPath + "Comm/JS/jquery-easyui/jquery-1.4.4.min.js'></script>");

		//          this.Page.RegisterClientScriptBlock("db8",
		//      "<script language='JavaScript' src='" + this.Request.ApplicationPath + "Comm/JS/jquery-easyui/query.easyui.min.js'></script>");

		//   this.Page.RegisterClientScriptBlock("sds",
		// "<link href='/WF/Comm/JS/pop/skin/qq/ymPrompt.css' rel='stylesheet' type='text/css' />");

		//   this.Page.RegisterClientScriptBlock("db7",
		//"<script language='JavaScript' src='" + this.Request.ApplicationPath + "Comm/JS/pop/ymPrompt.js'></script>");

	}

	//	public  void AlertMsg_Info(String title, String msg)
	//	{
	//		this.AddMsgOfInfo(title, msg);
	//		return;
	//
	//		//   //this.Alert(msg, false);
	//		////   return;
	//
	//		//   this.LoadPop();
	//
	//
	//
	//		//   return "<div id=myMsg style='display:none;'><div style='text-align:left' >" + msg + "</div></div>");
	//		//   String js = "<script language=JavaScript >";
	//		//   js += "\t\n $.messager.alert('" + title + "', document.getElementById('myMsg').innerHTML ,'info'); ";
	//		//   js += "</script>";
	//		//   this.Page.ClientScript.RegisterStartupScript(this.GetType(), "kesy", js);
	//
	//		//   //return "<div id=myMsg style='display:none;'><div style='text-align:left' >" + msg + "</div></div>");
	//		//   //String js = "<script language=JavaScript >";
	//		//   //js += "\t\n ymPrompt.setDefaultCfg({btn:'ok'}) ; ";
	//		//   //js += "\t\n ymPrompt.alert({message: document.getElementById('myMsg').innerHTML,title:'" + title + "',height:380,width:400,fixPosition:true,dragOut:false,allowSelect:true});";
	//		//   //js += "</script>";
	//		//   //this.Page.ClientScript.RegisterStartupScript(this.GetType(), "kesy", js);
	//	}
	//	public  void AlertMsg_Warning(String title, String msg)
	//	{
	//		this.AddMsgOfWarning(title, msg);
	//		return;
	//
	//		// this.Alert(msg, false);
	//		// return;
	//
	//		// this.LoadPop();
	//
	//		// return "<div id=myMsg style='display:none;'><div style='text-align:left' >" + msg + "</div></div>");
	//		// String js = "<script language=JavaScript >";
	//		//// js += "\t\n $.messager.alert('" + title + "', document.getElementById('myMsg').innerHTML ,'warning'); ";
	//		// //js += "";
	//
	//		// js += "\t\n  alert('sdsds') ";
	//
	//		// js += "\t\n $.messager.alert('" + title + "', 'sss','warning'); ";
	//
	//		// js += "</script>";
	//
	//		// this.Page.ClientScript.RegisterStartupScript(this.GetType(), "kesy", js);
	//
	//		//         <script language="JavaScript" src="../Comm/JS/pop/ymPrompt.js" ></script>
	//		//<link rel="stylesheet" type="text/css" href="../Comm/JS/pop/skin/qq/ymPrompt.css" /> 
	//
	//		//return "<div id=myMsg style='display:none;'><div style='text-align:left' >" + msg + "</div></div>");
	//		//String js = "<script language=JavaScript >";
	//		//js += "\t\n ymPrompt.setDefaultCfg({btn:'ok'}) ; ";
	//		//js += "\t\n ymPrompt.errorInfo({message: document.getElementById('myMsg').innerHTML,title:'" + title + "',height:380,width:400,fixPosition:true,dragOut:false,allowSelect:true});";
	//		//js += "</script>";
	//		//this.Page.ClientScript.RegisterStartupScript(this.GetType(), "kesy", js);
	//	}

	public static String AddFieldSetNone(String title) {
		return "<fieldset class=FieldSetNone ><legend>&nbsp;" + title + "&nbsp;</legend>";
	}

	public static String AddFieldSetYellow(String title) {
		return "<fieldset class=FieldSetYellow ><legend>&nbsp;" + title + "&nbsp;</legend>";
	}

	public static String AddFieldSetYellow(String title, String msg) {
		return "<fieldset class=FieldSetYellow ><legend>&nbsp;" + title + "&nbsp;</legend>" + msg + "</fieldset>";
	}

	public static String AddFieldSetBlue(String title) {
		return "<fieldset class=FieldSetBlue ><legend>&nbsp;" + title + "&nbsp;</legend>";
	}

	public static String AddFieldSetBlue(String title, String msg) {
		return "<fieldset class=FieldSetBlue ><legend>&nbsp;" + title + "&nbsp;</legend>" + msg + "</fieldset>";
	}

	public static String AddFieldSetRed(String title, String msg) {
		return "<fieldset class=FieldSetRed ><legend>&nbsp;" + title + "&nbsp;</legend>" + msg + "</fieldset>";
	}

	public static String AddFieldSetEnd() {
		return "</fieldset>";
	}

	public static String AddFieldSetEndBR() {
		return "</fieldset><BR>";
	}

	public static String AddLiInLine(String html) {
		return "<li style='display:inline'>" + html + "</li>";
	}

	public static String AddUL() {
		return "<ul>";
	}

	public static String AddUL(String attr) {
		return "<ul " + attr + ">";
	}

	public static String AddULTagCloud() {
		return "<ul class='TagCloud'>";
	}

	public static String AddULEnd() {
		return "</ul>\t\n";
	}

	public static String AddLi(String html) {
		return "<li>" + html + "</li> \t\n";
	}

	public static String AddLi(String url, String lab) {
		return "<li><a href=\"" + url + "\">" + lab + "</a></li>";
	}

	public static String AddLiB(String url, String lab) {
		return "<li><a href=\"" + url + "\"><b>" + lab + "</b></a></li>";
	}

	public static String AddLi(String url, String lab, String target) {
		return "<li><a href=\"" + url + "\" target=" + target + ">" + lab + "</a></li>";
	}

	public static String AddDiv_del(String html) {
		return "<div>" + html + "</div>";
	}

	public static String AddDivRound(String msg, int width) {
		return AddDivRound() + msg + AddDivRoundEnd();
	}

	public static String AddDivRound() {
		return "<div class='r_info'>" + "<p><img src='../Style/Img/right_line_t3.jpg' /></p>" + "<div class='info_in'>";
	}

	public static String AddDivRound(int width) {
		return "<div class='r_info'>" + "<p><img src='../Style/Img/right_line_t3.jpg' width='" + width + "px' /></p>" + "<div class='info_in'>";
	}

	public static String AddDivRoundEnd() {
		return "</div>" + "<p><img src='../Style/Img/right_line_b3.jpg' /></p>" + "</div>";
	}

	public static String AddDiv() {
		return "<div class=RoundedCorner><b class='rtop'><b class='r1'></b><b class='r2'></b><b class='r3'></b><b class='r4'></b></b><p class=divP>";
	}

	public static String AddDiv(String title, String html) {
		return AddDiv() + "<div><b>" + title + "</b>" + html + "</div>" + AddDivEnd();
	}

	public static String AddDivEnd() {
		return "</p><b class='rbottom'><b class='r4'></b><b class='r3'></b><b class='r2'></b><b class='r1'></b></b></div>";
	}

	public static String AddDivEndBR() {
		return AddDivEnd() + AddBR();
	}

	public static String AddH(String url, String lab, String t) {
		return "<a href=\"" + url + "\" target=" + t + " >" + lab + "</a>";
	}

	public static String AddH(String url, String lab) {
		return "<a href=\"" + url + "\" >" + lab + "</a>";
	}

	public static String AddLeftRight(String left, String right) {
		return "<table class='am-table am-table-striped am-table-hover table-main' border=0 width='100%'><TR><TD nowrap=\"nowrap\" align=left>"
				+ left + "</TD><TD nowrap=\"nowrap\" align=right>" + right + "</TD></TR></table>";
	}

	public static String Add(String s) {
		return s;
	}
	public static String Add(BaseWebControl ctl) {
		return "\n" + ctl.toString() + "\n";
	}
	public static String AddB(String s) {
		return "<B>" + s + "</B>";
	}

	public static String AddBR() {
		return "<BR>";
	}

	public static String AddHR() {
		return "<HR>";
	}

	public static String AddHR(String msg) {
		return "<HR>";
	}

	public static String AddP(String s) {
		if (s == null) {
			return "";
		}
		return "<P>" + s + "</P>";
	}

	public static String AddH1(String s) {
		if (s == null) {
			return "";
		}

		return "<H1>" + s + "</H1>";
	}

	public static String AddH2(String s) {
		if (s == null) {
			return "";
		}

		return "<H2>" + s + "</H2>";
	}

	public static String AddH3(String s) {
		if (s == null) {
			return "";
		}
		return "<H3>" + s + "</H3>";
	}

	public static String AddH4(String s) {
		if (s == null) {
			return "";
		}

		return "<H4>" + s + "</H4>";
	}

	public static String AddBR(String msg) {
		return "<BR>" + msg;
	}

	public static String AddSpace(int num) {
		return BP.DA.DataType.GenerSpace(num);
	}

	public static String AddPostBackTextBox(String name, boolean readOnly, String text) {
		return "<textarea name=" + name + " rows=\"3\" cols=\"20\" id=" + name
				+ " onblur=\"__doPostBack('TB_Doc','')\" style=\"width:100%;\" readonly=" + readOnly + ">" + text + "</textarea>";
	}

	//	public static String Add(System.Web.UI.Control ctl)
	//	{
	//		this.Controls.Add(ctl);
	//	}
	//	public static String AddBR(System.Web.UI.Control ctl)
	//	{
	//		this.Controls.Add(this.ParseControl("<BR>"));
	//		this.Controls.Add(ctl);
	//	}
	public static String AddTable() {
		return "<Table  class='Table' style='width:100%' cellpadding='0' cellspacing='0'>";
	}

	public static String AddTableEnd() {
		return "</Table>";
	}

	public static String AddTableEndWithHR() {
		return "</Table><HR>";
	}

	public static String AddTableEndWithBR() {
		return "</Table><Br>";
	}

	public static String AddTable(String attr) {
		//return "<Table id='table_01' "+attr+" >");
		//return "<Table " + ClassHelper.ATableClassDef + " " + attr + " >";
		return "<Table  " + attr + " >";
	}

	public static String AddTable1(String attr) {
		//return "<Table id='table_01' "+attr+" >");
		return "<Table  " + attr + " >";
	}

	public static String AddTable(String id, String styleClass) {
		return "<Table class='" + id + "'  cellpadding='0' cellspacing='0' class='" + styleClass + "'>";
	}

	//	public String AddTDNum(TB tb)
	//	{
	//		return "\n<TD class='TDNum' nowrap >");
	//		ctrl.append(tb);
	//		return "</TD>");
	//	}
	//	public static String AddTDNum(TextBox tb)
	//	{
	//		return "\n<TD class='TDNum' nowrap >");
	//		ctrl.append(tb);
	//		return "</TD>");
	//	}
	public static String AddTD(BaseWebControl ctl) {
		return "\n<TD nowrap=\"nowrap\">" + ctl.toString() + "</TD>";
	}

	public static String AddTD(String attr, BaseWebControl ctl) {
		return "\n<TD nowrap=\"nowrap\"" + attr + " >" + ctl.toString() + "</TD>";
	}

	public static String AddTDNum(String str) {
		return "\n<TD nowrap >" + str + "</TD>";
	}

	public static String AddTDNum(java.math.BigDecimal str) {
		return "\n<TD nowrap >" + str.toString() + "</TD>";
	}

	public static String AddTDJE(java.math.BigDecimal str) {
		return "\n<TD nowrap >" + str.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue() + "</TD>";
	}

	public static String AddTD(String str) {
		if (str == null || str.equals("")) {
			return "\n<TD  nowrap >&nbsp;</TD>";
		} else {
			return "\n<TD  nowrap >" + str + "</TD>";
		}
	}

	public static String AddTDA(String href, String str) {
		return "\n<TD  nowrap ><a href=\"" + href + "\">" + str + "</a></TD>";
	}

	public static String AddTDA(String href, String str, String target) {
		return "\n<TD  nowrap ><a href=\"" + href + "\" target=" + target + ">" + str + "</a></TD>";
	}

	public static String Href(String href, String str, String target) {
		return "<a href=\"" + href + "\" target=" + target + ">" + str + "</A>";
	}

	public static String Href(String href, String str, String target, int blank) {
		return "<a href=\"" + href + "\" target=" + target + ">" + str + "</A>" + BP.DA.DataType.GenerSpace(blank);
	}

	public static String Href(String href, String str) {
		return "<a href=\"" + href + "\" >" + str + "</A>";
	}

	public static String Href(String href, String str, int blank) {
		return "<a href=\"" + href + "\">" + str + "</A>" + BP.DA.DataType.GenerSpace(blank);
	}

	public static String AddTDM(String str) {
		return "\n<TD nowrap >" + str + "</TD>";
	}

	public static String AddTDMS(String str) {
		return "\n<TD nowrap >" + str + "</TD>";
	}

	public static String AddTDA(String href, int str) {
		return "\n<TD nowrap ><a href=\"" + href + "\">" + str + "</TD>";
	}

	public static String AddTD(boolean val) {
		if (val) {
			return "\n<TD nowrap>是</TD>";
		} else {
			return "\n<TD nowrap>否</TD>";
		}
	}

	public static String AddTDBegin(String attr) {
		return "\n<TD " + attr + " nowrap >";
	}

	public static String AddTDBegin() {
		return "\n<TD valign=top nowrap >";
	}

	public static String AddTDEnd() {
		return "\n</TD>";
	}

	public static String AddTDInfoBegin() {
		return "\n<TD  nowrap bgcolor=InfoBackground >";
	}

	public static String AddTDInfo(String str) {
		return "\n<TD  nowrap bgcolor=InfoBackground >" + str + "</TD>";
	}

	public static String AddTDInfo() {
		return "\n<TD  nowrap bgcolor=InfoBackground >&nbsp;</TD>";
	}

	//	public static String AddTDInfo(String attr, System.Web.UI.Control str)
	//	{
	//		return "\n<TD  " + attr + "  nowrap bgcolor=InfoBackground>";
	//		this.Controls.Add(str);
	//		return "</TD>");
	//	}
	//	public static String AddTDInfo(System.Web.UI.Control str)
	//	{
	//		return "\n<TD   nowrap bgcolor=InfoBackground>";
	//		this.Controls.Add(str);
	//		return "</TD>");
	//	}
	public static String AddTDCenter(BaseWebControl ctl) {
		return "\n<TD   nowrap >" + ctl.toString() + "</TD>";
	}

	public static String AddTDCenter(String str) {
		return "\n<TD align=center nowrap >" + str + "</TD>";
	}

	public static String AddTD() {
		return "\n<TD >&nbsp;</TD>";
	}

	public static String AddTDToolbar(String str) {
		return "\n<TD class='Toolbar' nowrap >" + str + "</TD>";
	}

	public static String AddTR() {
		return "\n<TR>";
	}

	public static String AddTRHand() {
		return "\n<TR>";
	}

	public static String AddTRHand(String attr) {
		return "\n<TR" + attr + " >";
	}

	public static String AddTRTXHand() {
		return "\n<TR class='TRHand' onmouseover='TROver(this)' onmouseout='TROut(this)' >";
	}

	public static String AddTRTXHand(String attr) {
		return "\n<TR class='TRHand' onmouseover='TROver(this)' onmouseout='TROut(this)' " + attr + " >";
	}

	public static String AddTR(String attr) {
		return "\n<TR " + attr + " >";
	}

	public static String AddTRSum() {
		return "\n<TR class='TRSum' >";
	}

	public static String AddTRRed() {
		return "\n<TR class='TRRed' >";
	}

	public static String AddTR1() {
		return "\n<TR class=TR1 >";
	}

	public static String AddTR(boolean item, String attr) {
		if (item) {
			return "\n<TR bgcolor=AliceBlue " + attr + " >";
		} else {
			return "\n<TR bgcolor=white " + attr + " class=TR>";
		}

	}

	public static String AddTRGroupTitle(int colspan, String str) {
		return AddTR() + AddTDGroupTitle("class='GroupTitle' nowrap colspan=" + colspan + "", str) + AddTREnd();
	}

	public String AddTRGroupTitle(String str) {
		return AddTR() + AddTDGroupTitle(str) + AddTREnd();
	}

	public static String AddTableNormal() {
		return AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'");
	}

	//	public static String AddEasyUiPanelInfoBegin(String title, String iconCls, int padding)
	//    {
	//		iconCls= "icon-tip";
	//		padding=10;
	//        return "<div style='width:100%'>"+"<div class='easyui-panel' title='"+title+"' data-options=\"iconCls:'"+iconCls+"',fit:true\" style='height:auto;padding:"+padding+"px'>";
	//    }

	public static String AddTDGroupTitleCenter(String str) {
		return "\n<TD class='GroupTitle' style='text-align:center'>" + str + "</TD>";
	}

	//	public  boolean AddTR(RefObject<Boolean> item)
	//	{
	//		if (item.argvalue)
	//		{
	//			return "\n<TR bgcolor=AliceBlue >");
	//		}
	//		else
	//		{
	//			return "\n<TR bgcolor=white class=TR>";
	//		}
	//
	//		item.argvalue = !item.argvalue;
	//		return item.argvalue;
	//	}
	public static String AddTR(boolean item) {
		if (item) {
			return "\n<TR bgcolor=AliceBlue >";
		} else {
			return "\n<TR bgcolor=white >";
		}
	}

	public static String AddTRTXRed() {
		return "\n<TR  bgcolor=red >";
	}

	/** 
	 加上特殊效果
	 
	*/
	public static String AddTRTX() {
		return "\n<TR onmouseover='TROver(this)' onmouseout='TROut(this)' >";
	}

	public static String AddTRTX(String attr) {
		return "\n<TR onmouseover='TROver(this)' onmouseout='TROut(this)' " + attr + ">";
	}

	public static String AddTREnd() {
		return "\n</TR>";
	}

	public static String AddTDIdx(int idx) {
		return "\n<TD class='Idx' nowrap>" + idx + "</TD>";
	}

	//	public static String AddTDIdx(System.Web.UI.Control ctl)
	//	{
	//		return "\n<TD class='Idx' nowrap>");
	//		this.Add(ctl);
	//		return "</TD>");
	//	}
	//	public static String AddTDIdx(int idx, System.Web.UI.Control ctl)
	//	{
	//		return "\n<TH class='Idx' nowrap >" + idx);
	//		this.Add(ctl);
	//		return "</TH>");
	//	}
	public static String AddTDIdx(String idx) {
		return "\n<TH nowrap >" + idx + "</TH>";
	}

	public static String AddTDH(String url, String lab) {
		return "\n<TD  nowrap ><a href='" + url + "'>" + lab + "</a></TD>";
	}

	public static String AddTDH(String url, String lab, String target) {
		return "\n<TD  nowrap ><a href='" + url + "' target=" + target + ">" + lab + "</a></TD>";
	}

	public static String AddTDH(String url, String lab, String target, String img) {
		return "\n<TD  nowrap ><a href='" + url + "' target=" + target + "><img src='" + img + "' border=0/>" + lab + "</a></TD>";
	}

	//	public static String AddCheckBoxsByEntities(Entities ens, String selectVals, String fieldName)
	//	{
	//		for (Entity en : Entities.convertEntities(ens))
	//		{
	//			CheckBox cb = new CheckBox();
	//			cb.ID = "CB_" + fieldName + "_" + en.GetValStringByKey("No");
	//			cb.setText(en.GetValStringByKey("Name"));
	//			if (StringHelper.isNullOrEmpty(selectVals) == false)
	//			{
	//				cb.Checked = selectVals.contains("," + en.GetValStringByKey("No") + ",");
	//			}
	//			this.Add(cb);
	//		}
	//	}
	//	public static String AddCheckBoxsByEntities(DataTable dt, String selectVals, String fieldName, int rowNum)
	//	{
	//		int idx = 0;
	//		for (DataRow dr : dt.Rows)
	//		{
	//			idx++;
	//			CheckBox cb = new CheckBox();
	//			cb.ID = "CB_" + fieldName + "_" + dr["No"];
	//			cb.setText(dr["Name"].toString());
	//			if (StringHelper.isNullOrEmpty(selectVals) == false)
	//			{
	//				cb.Checked = selectVals.contains("," + dr["No"] + ",");
	//			}
	//			this.Add(cb);
	//			if (idx >= rowNum)
	//			{
	//				this.AddBR();
	//				idx = 0;
	//			}
	//		}
	//	}
	//	public static String AddCheckBoxsByEntities(String sql, String selectVals, String fieldName)
	//	{
	//		AddCheckBoxsByEntities(sql, selectVals, fieldName, 100);
	//	}
	//	public static String AddCheckBoxsByEntities(String sql, String selectVals, String fieldName, int rowNum)
	//	{
	//		DataTable dt = DBAccess.RunSQLReturnTable(sql);
	//		AddCheckBoxsByEntities(dt, selectVals, fieldName, rowNum);
	//	}
	//	/** 
	//	 增加选择
	//	 
	//	 @param enumID
	//	 @param selectVals
	//	 @param fieldName
	//	*/
	//	public static String AddCheckBoxsByEnum(String enumID, String selectVals, String fieldName)
	//	{
	//		SysEnums ses = new SysEnums(enumID);
	//		for (SysEnum se : ses)
	//		{
	//			CheckBox cb = new CheckBox();
	//			cb.ID = "CB_" + fieldName + "_" + se.IntKey;
	//			cb.setText(se.Lab);
	//			if (StringHelper.isNullOrEmpty(selectVals) == false)
	//			{
	//				cb.Checked = selectVals.contains("," + se.IntKey + ",");
	//			}
	//			this.Add(cb);
	//		}
	//	}
	/** 
	 增加选择
	 
	 @param enumID
	 @param selectVals
	*/
	//	public static String AddCheckBoxsByEnum(String enumID, String selectVals)
	//	{
	//		AddCheckBoxsByEnum(enumID, selectVals, enumID);
	//	}
	//	public static String AddTD(CheckBox cb)
	//	{
	//		return "\n<TD >");
	//		this.Add(cb);
	//		return "</TD>");
	//	}
	//	public static String AddTD(System.Web.UI.Control ctl)
	//	{
	//		return "\n<TD >");
	//		this.Add(ctl);
	//		return "</TD>");
	//	}
	//	public static String AddTD(System.Web.UI.Control ctl, String note)
	//	{
	//		return "\n<TD  >");
	//		this.Add(ctl);
	//		this.Add(note + "</TD>");
	//	}
	//	public static String AddTD(String attr, System.Web.UI.Control ctl)
	//	{
	//		return "\n<TD " + attr + " >");
	//		this.Add(ctl);
	//		return "</TD>");
	//	}

	//	public static String AddTD(String attr, String msgdec, System.Web.UI.Control ctl)
	//	{
	//		msgdec = msgdec.trim();
	//		if (msgdec == null || msgdec.equals(""))
	//		{
	//		}
	//		else
	//		{
	//			msgdec += "<BR>";
	//		}
	//
	//		return "\n<TD " + attr + " >" + msgdec);
	//		this.Add(ctl);
	//		return "</TD>");
	//	}
	//	public static String AddTD(String attr, System.Web.UI.WebControls.WebControl ctl)
	//	{
	//		return "\n<TD  nowrap " + attr + "  >");
	//		this.Add(ctl);
	//		return "</TD>");
	//	}
	public static String AddTDBar(String str) {
		return "\n<TD nowrap=true >" + str + "</TD>";
	}

	public static String AddCaption(String str) {
		return "\n<th class='table-title' colspan='20' >" + str + "</th>";
	}

	public static String AddTableBarGreen(String title, int col) {
		return "\n<TR>" + "\n<TD  colspan=" + col + " >" + title + "</TD>" + "\n</TR>";
	}

	public static String AddCaptionMsg(String str) {
		return "\n<Caption ><div class='CaptionMsg' >" + str + "</div></Caption>";
	}

	public static String AddTableBarGreen(String title) {
		return AddTableBarGreen(title, 1);
	}

	public static String AddTableBarGreen(int col) {
		return AddTableBarGreen("&nbsp;", col);
	}

	public static String AddTableBarBlue(String title, int col) {
		return "\n<TR>" + "\n<TD  colspan=" + col + " >" + title + "</TD>" + "\n</TR>";
	}

	public static String AddTableBarBlue(int col) {
		return AddTableBarBlue("&nbsp;", col);
	}

	public static String AddTableBarRed(String imgUrl, String title, int col, String leftMore) {
		String msg = "";
		if (imgUrl != null) {
			msg = "<table border=0 width='100%'  " + ClassHelper.ATableClassDef + "  ><TR><TD>&nbsp;&nbsp;<img src='" + imgUrl + "' border=0 />"
					+ title + "</TD><TD align='right'>" + leftMore + "</TD></TR></Table>";
		} else {
			msg = "<table border=0 width='100%'  " + ClassHelper.ATableClassDef + "  ><TR><TD>&nbsp;&nbsp;" + title + "</TD><TD align='right'>"
					+ leftMore + "</TD></TR></Table>";
		}

		//this.AddCaption(msg);

		return "\n<TR height='0%' >" + "\n<TD height='0%' colspan=" + col + " >" + msg + "</TD>" + "\n</TR>";
	}

	public static String AddTableBarRed(String title, int col) {
		return "\n<TR>" + "\n<TD  colspan=" + col + " >" + title + "</TD>" + "\n</TR>";
	}

	public static String AddCaptionLeft(String str) {
		return "\n<Caption >" + str + "</Caption>";
	}

	public static String AddCaptionRight(String str) {
		return "\n<Caption align=right >" + str + "</Caption>";
	}

	public static String AddCaptionLeftTX(String str) {
		return "\n<Caption align=left >" + str + "</Caption>";

	}

	public static String AddCaptionLeftTX1(String str) {
		return "\n<Caption >" + str + "</Caption>";

	}

	public static String AddCaptionLeftTX2(String str) {
		return "<table " + ClassHelper.ATableClassDef + " width='1px' border=0><tr><td nowrap=true >" + str + "</td></tr></table>"
				+ "\n<Caption align=left >" + str + "</Caption>";
	}

	//	public static String AddTDTitle(String attr, System.Web.UI.Control ctl)
	//	{
	//		return "\n<TH " + attr + ">");
	//		this.Add(ctl);
	//		return "</TH>");
	//	}
	//	public static String AddTDTitle(System.Web.UI.Control ctl)
	//	{
	//		return "\n<TH>");
	//		this.Add(ctl);
	//		return "</TH>");
	//	}
	public static String AddTDTitle() {
		return "\n<TH>&nbsp;</TH>";
	}

	public static String AddTDTitleLeft(String attr, String str) {
		return "\n<TH align=left " + attr + " nowrap=true>" + str + "</TH>";
	}

	public static String AddTDTitleLeft1(String attr, String str) {
		return "\n<TH align=left " + attr + " nowrap=true>" + str + "</TH>";
	}

	public static String AddTDTitleLeft(String str) {
		return "\n<TH align=left nowrap=true>" + str + "</TH>";
	}

	public static String AddTDDoc(String str) {
		return "\n<TD>" + str + "</TD>";
	}

	public static String AddTDBigDoc(String attr, String str) {
		return "\n<TD " + attr + " valign=top>" + str + "</TD>";
	}

	public static String AddTDBigDocBegain() {
		return "\n<TD valign=top>";
	}

	//	public static String AddTDBigDoc(String attr, TextBox tb)
	//	{
	//		return "\n<TD class='BigDoc' " + attr + " valign=top >");
	//		this.Add(tb);
	//		return "</TD>");
	//	}
	public static String AddTDBigDoc(String str) {
		return "\n<TD valign=top>" + str + "</TD>";
	}

	public static String AddTDDoc(String str, int len, String title) {
		if (str.length() >= len) {
			return "\n<TD  nowrap title=\"" + title.replace("<BR>", "\n") + "\" >" + str.substring(0, len) + "...</TD>";
		} else {
			return "\n<TD nowrap>" + str + "</TD>";
		}
	}

	public static String AddTDDoc(String str, String title) {
		if (str == null || str.length() == 0) {
			return "\n<TD nowrap>...</TD>";
		}

		if (str.length() > 20) {
			return "\n<TD  nowrap title=\"" + title.replace("<BR>", "\n") + "\" >" + str.substring(0, 20) + "...</TD>";
		} else {
			return "\n<TD  nowrap>" + str + "</TD>";
		}
	}

	//	public  boolean IsExit(String ctlID)
	//	{
	//		for (BaseController ctl : this.Controls)
	//		{
	//			if (ctl.ID == null)
	//			{
	//				continue;
	//			}
	//
	//			if (ctlID.equals(ctl.ID))
	//			{
	//				return true;
	//			}
	//		}
	//		return false;
	//	}
	public static String AddTDTitleExt(String str) {
		return "\n<TD nowrap=true class='TitleExt'>" + str + "</TD>";
	}

	public static String AddTDTitle(String attr, String str) {
		return "\n<TH class='Title'  " + attr + " nowrap=true >" + str + "</TH>";
	}

	public static String AddTDB(String str) {
		return "\n<TD  nowrap=true ><b>" + str + "</b></TD>";
	}

	public static String AddTDB(String attr, String str) {
		return "\n<TD  " + attr + " nowrap=true ><b>" + str + "</b></TD>";
	}

	public static String AddTDGroupTitle() {
		return "\n<TD nowrap ></TD>";

	}

	public static String AddTDGroupTitle(String str) {
		return "\n<TD nowrap class='GroupTitle' >" + str + "</TD>";
	}

	public static String AddTDGroupTitle2(String str) {
		return "\n<TD nowrap style='width:100px' class=\"GroupTitle\" >" + str + "</TD>";
	}

	public static String AddTDGroupTitle1(String str) {
		return "\n<TD nowrap style='width:100px' class=\"panel-header panel-header-noborder\" >" + str + "</TD>";
	}

	public static String AddTDGroupTitle(String attr, String str) {
		return "\n<TD " + attr + " >" + str + "</TD>";
	}

	public static String AddTDGroupTitle1(String attr, String str) {
		return "\n<TD  nowrap class='GroupTitle'  " + attr + " >" + str + "</TD>";
	}

	//	public static String AddTDGroupTitle(String attr, System.Web.UI.Control ctl)
	//	{
	//		return "\n<TD class='GroupTitle'  " + attr + " >");
	//		this.Add(ctl);
	//		return "</TD>");
	//	}nowrap="nowrap"
	public static String AddTDTitle(String str) {
		return "\n<TH>" + str + "</TH>";
	}

	public static String AddTDTitle1(String str) {
		return "\n<TH nowrap='nowrap' style='width:100px'>" + str + "</TH>";
	}

	public static String AddTDTitleGroup(String str) {
		return "\n<TD>" + str + "</TD>";
	}

	public static String AddTDDesc(String str) {
		return "\n<TD nowrap=true >" + str + "</TD>";
	}

	public static String AddTDDesc(String str, int colspan) {
		return "\n<TD nowrap=true colspan=" + colspan + " >" + str + "</TD>";
	}

	//	public static String AddTDTitleHelp(String str, String msg)
	//	{
	//		String path = this.Request.ApplicationPath;
	//		return "\n<TH  nowrap=true title=\"" + msg + "\" >" + str + "<img src='./" + this.Request.ApplicationPath + "WF/Img/Btn/Help.gif' onclick=\"javascript:alert( '" + msg + "' )\"  border=0></TH>");
	//	}
	public static String AddTD(int val) {
		return "\n<TD  >" + val + "</TD>";
	}

	public static String AddTD(long val) {
		return "\n<TD >" + val + "</TD>";
	}

	public static String AddTD(java.math.BigDecimal val) {
		return "\n<TD >" + val + "</TD>";
	}

	public static String AddTDMoney(java.math.BigDecimal val) {
		return "\n<TD >" + val.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue() + "</TD>";
	}

	public static String AddTD(float val) {
		return "\n<TD >" + val + "</TD>";
	}

	public static String AddTD(String attr, String str) {
		return "\n<TD " + attr + " >" + str + "</TD>";
	}

	public static String AddTD1(String attr, String str) {
		return "\n<TD  nowrap='nowrap' " + attr + " >" + str + "</TD>";
	}

	public static String AddTD(String attr, int val) {
		return AddTD(attr, (new Integer(val)).toString());
	}

	public static String AddTD(String attr, java.math.BigDecimal val) {
		return AddTD(attr, val.toString());
	}

	public static String AddTH(String str) {
		return "\n<TH >" + str + "</TH>";
	}

	public static String AddTH(String attr, String str) {
		return "\n<TH class='" + attr + "' >" + str + "</TH>";
	}

	public static String AddMsgOfWarning(String title, String doc) {
		String ss = AddFieldSetYellow("<font color=red><b>" + title + "</b></font>");
		if (null != doc) {
			ss += doc.replace("@", "<BR>@");
		}
		ss += AddFieldSetEnd();
		return ss;
	}

	public static String AddMsgOfInfo(String title, String doc) {
		String ss = AddFieldSet(title);
		if (doc != null) {
			ss += doc.replace("@", "<BR>@");
		}
		ss += AddFieldSetEnd();

		return ss;
	}

	/** 
	 showmodaldialog
	 
	 @param url
	 @param title
	 @param Height
	 @param Width
	*/
	//	protected  void ShowModalDialog(String url, String title, int Height, int Width)
	//	{
	//		String script = "<script language='JavaScript'> window.showModalDialog('" + url + "','','dialogHeight: " + (new Integer(Height)).toString() + "px; dialogWidth: " + (new Integer(Width)).toString() + "px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); </script> ";
	//		this.Response.Write(script);
	//	}
	/** 
	 关闭窗口
	 
	*/
	public static void WinClose() {
		try {
			// 20171024
//			ContextHolderUtils.getResponse().getWriter().write("<script language='JavaScript'> window.close()</script>");

			/**
			 * 若是使用easyui打开的子页面(见EasyUIUtility.js的OpenEasyUiDialog)
			 * 仅调用window.close()无法将其关闭
			 * 修改后, 在打开窗口时在父页面定义了doCloseDialog()方法
			 * 此处判断父页面中若有doCloseDialog()方法, 则优先调用它来关闭窗口
			 * bug#338
			 */
			StringBuilder js = new StringBuilder();
			js.append("<script language='JavaScript'>");
			js.append("		if (parent && parent.window && typeof parent.window.doCloseDialog === 'function') { ");
			js.append("			parent.window.doCloseDialog.call(); ");
			js.append("		} else { ");
			js.append("			window.close();");
			js.append("		} ");
			js.append("</script>");
			ContextHolderUtils.getResponse().getWriter().write(js.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	protected  void WinClose(String val)
	//	{
	//		String clientscript = "<script language='javascript'> window.returnValue = '" + val + "'; window.close(); </script>";
	//		this.Page.Response.Write(clientscript);
	//	}

	/** 
	 关闭窗口
	 
	*/
	//	protected  void WinCloseWithMsg(String msg)
	//	{
	//		this.Response.Write("<script language='JavaScript'>alert('" + msg + "'); window.close()</script>");
	//	}

	/** 
	 打开一个新的窗口
	 
	 @param msg
	*/
	protected static void WinOpen(String url) {
		WinOpen(url, "", "msg", 900, 500);
	}

	protected static void WinOpen(String url, String title, String winName, int width, int height) {
		WinOpen(url, title, winName, width, height, 0, 0);
	}

	//	protected  void WinOpen(String url, String title, int width, int height)
	//	{
	//		this.WinOpen(url, title, "ActivePage", width, height, 0, 0);
	//	}
	protected static void WinOpen(String url, String title, String winName, int width, int height, int top, int left) {
		url = url.replace("<", "[");
		url = url.replace(">", "]");
		url = url.trim();
		title = title.replace("<", "[");
		title = title.replace(">", "]");
		title = title.replace("\"", "‘");
		writerMsgToClient("var newWindow =window.open(' " + url + "','" + winName + "','width=" + width + ",top=" + top + ",left=" + left
				+ ",height=" + height + ",scrollbars=yes,resizable=yes') ; newWindow.focus();");
		//		this.Response.Write("<script language='JavaScript'> var newWindow =window.open(' " + url + "','" + winName + "','width=" + width + ",top=" + top + ",left=" + left + ",height=" + height + ",scrollbars=yes,resizable=yes') ; newWindow.focus(); </script> ");
	}

	private int MsgFontSize = 1;

	/** 
	 输出到页面上红色的警告。
	 
	 @param msg 消息
	*/
	//	protected  void ResponseWriteRedMsg(String msg)
	//	{
	//		//this.Response.Write("<BR><font color='red' size='"+MsgFontSize.ToString()+"' > <b>"+msg+"</b></font>");
	//		//if (msg.Length < 200)
	//		//	return ;
	//		msg = msg.replace("@", "<BR>@");
	//		System.Web.HttpContext.Current.Session["info"] = msg;
	//		String url = "/WF/Comm/Port/ErrorPage.jsp";
	//		this.WinOpen(url, "警告", "msg", 500, 400, 150, 270);
	//	}
	//	protected  void ResponseWriteRedMsg(RuntimeException ex)
	//	{
	//		this.ResponseWriteRedMsg(ex.getMessage());
	//	}
	/** 
	 输出到页面上蓝色的信息。
	 
	 @param msg 消息
	*/
	//	protected  void ResponseWriteBlueMsg(String msg)
	//	{
	//		this.Response.Write("<BR><font color='Blue' size='" + (new Integer(MsgFontSize)).toString() + "' ><b>" + msg + "</b></font>");
	//
	//		msg = msg.replace("@", "<BR>@");
	//		System.Web.HttpContext.Current.Session["info"] = msg;
	//		String url = "/WF/Comm/Port/InfoPage.jsp";
	//		this.WinOpen(url, "错误信息", "msg", 500, 400, 150, 270);
	//	}
	/** 
	 保存成功
	 
	*/
	//	protected  void ResponseWriteBlueMsg_SaveOK()
	//	{
	//		ResponseWriteBlueMsg("保存成功!");
	//	}
	/** 
	 ResponseWriteBlueMsg_DeleteOK
	 
	*/
	//	protected  void ResponseWriteBlueMsg_DeleteOK()
	//	{
	//		ResponseWriteBlueMsg("删除成功!");
	//	}
	/** 
	 ResponseWriteBlueMsg_UpdataOK
	 
	*/
	//	protected  void ResponseWriteBlueMsg_UpdataOK()
	//	{
	//		ResponseWriteBlueMsg("更新成功!");
	//	}
	/** 
	 输出到页面上黑色的信息。
	 
	 @param msg 消息
	*/
	//	protected  void ResponseWriteBlackMsg(String msg)
	//	{
	//		this.Response.Write("<font color='Black' size=5 ><b>" + msg + "</b></font>");
	//	}
	//	protected  void ToSignInPage()
	//	{
	//
	//		System.Web.HttpContext.Current.Response.Redirect(System.Web.HttpContext.Current.Request.ApplicationPath + "SignIn.jsp?url=/Wel.jsp");
	//	}
	//	protected  void ToWelPage()
	//	{
	//		System.Web.HttpContext.Current.Response.Redirect(System.Web.HttpContext.Current.Request.ApplicationPath + "Wel.jsp");
	//	}
	public static void ToMsgPage(String mess) {
		ContextHolderUtils.getRequest().getSession().setAttribute("info", mess);
		try {
			ContextHolderUtils.getResponse().sendRedirect(Glo.getCCFlowAppPath() + "WF/Comm/Port/InfoPage.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/** 
	 切换到信息也面。
	 
	 @param mess
	*/
	public static void ToErrorPage(String mess) {
		ContextHolderUtils.getRequest().getSession().setAttribute("info", mess);
		try {
			ContextHolderUtils.getResponse().sendRedirect(Glo.getCCFlowAppPath() + "WF/Comm/Port/ToErrorPage.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/** 
	 切换到信息也面。
	 
	 @param mess
	*/
	//	protected  void ToMsgPage(String mess)
	//	{
	//		System.Web.HttpContext.Current.Session["info"] = mess;
	//		System.Web.HttpContext.Current.Response.Redirect("/WF/Comm/Port/MsgPage.jsp");
	//	}

	///#endregion

	///#region AddHyperLink
	//	public static String AddHyperLink(String text, String url, String target, String imgs)
	//	{
	//		BPHyperLink hl = new BPHyperLink();
	//		hl.setText(text);
	//		hl.NavigateUrl = url;
	//		hl.Target = target;
	//		hl.ImageUrl = imgs;
	//		this.Controls.Add(hl);
	//	}
	//	public static String AddHyperLink(String text, String url, String target)
	//	{
	//		this.AddHyperLink(text, url, target, "");
	//	}
	//	public static String AddHyperLink(String text, String url)
	//	{
	//		this.AddHyperLink(text, url, "", "");
	//	}

	///#endregion

	//	@Override
	//	protected void OnInit(EventArgs e)
	//	{
	//		//ShowRuning();
	//		super.OnInit(e);
	//	}
	//	private void Page_Load(Object sender, System.EventArgs e)
	//	{
	//		//CreateControl();
	//	}
	//	/** 
	//	 清空字体。
	//	 
	//	*/
	//	public  void Clear()
	//	{
	//		this._Text = null;
	//		this.Controls.Clear();
	//	}
	/** 
	 _Text
	 
	*/
	protected String _Text = "";

	/** 
	 文本
	 
	*/
	public String getText() {
		return this._Text;

	}

	public void setText(String value) {
		_Text = value;
	}

	/// <summary>
	/// 开始增加一个EasyUi的Panel，带有标题
	/// <remarks>
	/// <para>注意：用于AddEasyUiPanelInfoEnd方法之前，两者必须配合使用</para>
	/// </remarks>
	/// </summary>
	/// <param name="title">标题</param>
	/// <param name="iconCls">标题前面的图标，必须是EasyUi中icon.css中定义的类</param>
	/// <param name="padding">Panel内部边距(单位:px)</param>
	public static String AddEasyUiPanelInfoBegin(String title, String iconCls, int padding) {
		return "<div style='width:100%'>" + "<div class='easyui-panel' title='" + title + "' data-options=\"iconCls:'" + iconCls
				+ "',fit:true\" style='height:auto;padding:" + padding + "px'>";
	}

	public static String AddEasyUiLinkButton(String text, String url, String iconCls, boolean isPlain) {
		return "<a class='easyui-linkbutton' href=\"" + url + "\" data-options=\"plain:" + isPlain + ",iconCls:'" + iconCls + "'\">" + text
				+ "</a>&nbsp;";//,url, isPlain.ToString().ToLower(), iconCls ?? String.Empty, text));
	}

	public String AddEasyUiPanelInfoBegin(String title, String id) {
		return "<div class=\"am-panel am-panel-default\"><div class=\"am-panel-hd am-cf\" data-am-collapse=\"{target: '#" + id + "'}\">" + title
				+ "<span class=\"am-icon-chevron-down am-fr\"></span></div><div class=\"am-panel-bd am-collapse am-in\" id='" + id + "'>";

		//		return "<div class=\"am-panel am-panel-default\">"
		//				+ "<div class=\"am-panel-hd am-cf\" data-am-collapse=\"{target: '"+id+"'}\">"
		//				+title
		//				+"<span class=\"am-icon-chevron-down am-fr\"></span></div>"
		//				+ "<div class=\"am-panel-bd am-collapse am-in\" id=\""+id+"\">";
	}

	/// <summary>
	/// 结束增加一个EasyUi的Panel，带有标题
	/// <remarks>
	/// <para>注意：用于AddEasyUiPanelInfoBegin方法之后，两者必须配合使用</para>
	/// </remarks>
	/// </summary>
	public static String AddEasyUiPanelInfoEnd() {
		return "</div>" + "</div>";
	}

	// <summary>
	// 增加一个EasyUi的Panel,展示一小段信息，带有标题
	// </summary>
	// <param name="title">标题</param>
	// <param name="msg">要展示的信息</param>
	// <param name="iconCls">标题前面的图标，必须是EasyUi中icon.css中定义的类</param>
	// <param name="padding">Panel内部边距(单位:px)</param>
	public String AddEasyUiPanelInfo(String title, String msg, String iconCls, int padding) {
		return AddEasyUiPanelInfoBegin(title, iconCls, padding) + msg + AddEasyUiPanelInfoEnd();
	}

	public String AddEasyUiPanelInfo(String title, String msg, String iconCls) {
		return AddEasyUiPanelInfoBegin(title, iconCls, 10) + msg + AddEasyUiPanelInfoEnd();
	}

	public String AddEasyUiPanelInfo(String title, String msg, int padding) {
		return AddEasyUiPanelInfoBegin(title, "icon-tip", padding) + msg + AddEasyUiPanelInfoEnd();
	}

	public static String AddEasyUiPanelInfo(String title, String msg) {
		return AddEasyUiPanelInfoBegin(title, "icon-tip", 10) + msg + AddEasyUiPanelInfoEnd();
	}

	private String AddAttrDescValDoc(String desc, String doc, int colspan) {
		String str = "";
		if (colspan == 4) {
			str += this.AddTR();
			str += this.AddTDDesc(desc, colspan);
			str += this.AddTREnd();

			str += this.AddTR();
			str += this.AddTDBigDoc(" align=left colspan=4", doc);
			str += this.AddTREnd();

		} else {
			str += this.AddTDBegin(" align=left colspan=" + colspan);
			str += "<b>" + desc + "</b><br>";
			str += doc;
			str += this.AddTDEnd();
		}
		return str;
	}

	private String AddAttrDescVal(String desc, String doc, int colspan) {
		return this.AddTDDesc(desc) + this.AddTD(" align=left width=40% colspan=" + colspan, doc);
	}

	public String BindViewEn(Entity en, String tableAttr) {
		//注意这里暂时不知道怎么翻译
		//this.Attributes["visibility"] = "hidden";

		String resultStr = "";
		resultStr += this.AddTable(tableAttr);

		boolean isLeft = true;
		Object val = null;
		boolean isAddTR = true;
		Map map = en.getEnMap();
		Attrs attrs = map.getAttrs();
		for (Attr attr : attrs) {
			if (!attr.getUIVisible())
				continue;
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			val = en.GetValByKey(attr.getKey());

			// #region 判断是否单列显示
			if (attr.UIIsLine) {
				if (!isAddTR) {
					resultStr += BaseModel.AddTD();
					resultStr += BaseModel.AddTD();
					resultStr += BaseModel.AddTDEnd();
				}

				isLeft = true;
				isAddTR = true; /*让他下次从0开始。*/
				if (attr.getUIHeight() != 0) {
					/*大块文本采集, 特殊处理。*/
					if (val.toString().length() == 0 && !en.getIsEmpty() && attr.getKey().equals("Doc"))
						val = en.GetValDocHtml();
					else
						val = DataType.ParseText2Html(val.toString());

					resultStr += this.AddAttrDescValDoc(attr.getDesc(), val.toString(), 4);
					continue;
				}

				resultStr += this.AddTR();
				if (attr.getMyDataType() == DataType.AppBoolean) {
					if (val.toString().equals("1"))
						resultStr += this.AddAttrDescVal("", "<b>是</b> " + attr.getDesc(), 3);
					else
						resultStr += this.AddAttrDescVal("", "<b>否</b> " + attr.getDesc(), 3);
				} else
					resultStr += this.AddAttrDescVal(attr.getDesc(), val.toString(), 3);

				resultStr += this.AddTREnd();
				continue;
			}

			// #endregion 判断是否单列显示 // 结束要显示单行的情况。
			if (isLeft)
				resultStr += this.AddTR();
			switch (attr.getUIContralType()) {
			case TB:
				resultStr += this.AddAttrDescVal(attr.getDesc(), val.toString(), 1);
				//if (attr.UIHeight != 0)
				//{
				//    if (val.ToString().Length == 0 && en.IsEmpty == false && attr.Key == "Doc")
				//        val = en.GetValDocHtml();
				//    else
				//        val = DataType.ParseText2Html(val as String);

				//    this.AddAttrDescValDoc(attr.Desc, val.ToString(), 2);
				//}
				//else
				//{
				//    this.AddAttrDescVal(attr.Desc, val.ToString(), 1);
				//}

				break;
			case DDL:
				resultStr += this.AddAttrDescVal(attr.getDesc(), en.GetValRefTextByKey(attr.getKey()), 1);
				break;
			case CheckBok:
				if (en.GetValBooleanByKey(attr.getKey()))
					resultStr += this.AddAttrDescVal(attr.getDesc(), "是", 1);
				else
					resultStr += this.AddAttrDescVal(attr.getDesc(), "否", 1);
				break;
			default:
				break;
			}

			if (!isLeft)
				resultStr += this.AddTREnd();
			isLeft = !isLeft;
		} // 结束循环.
		resultStr += this.AddTableEnd();
		return resultStr;
	}

	///#region bind entity
	//	public  void BindEntity3ItemReadonly(Entity en, boolean isShowDtl)
	//	{
	//		Map map = en.EnMap;
	//		AttrDescs ads = new AttrDescs(en.toString());
	//
	//		return "<table border=0 >");
	//		for (AttrDesc ad : ads)
	//		{
	//			Attr attr = map.GetAttrByKey(ad.Attr);
	//			this.AddTR();
	//			this.AddTD("valign=top  align=right ", attr.Desc + "：");
	//			switch (attr.MyDataType)
	//			{
	//				case DataType.AppString:
	//					if (attr.UIHeight != 0)
	//					{
	//						this.AddTD("valign=top ", en.GetValHtmlStringByKey(ad.Attr));
	//					}
	//					else
	//					{
	//						this.AddTD("valign=top ", en.GetValStringByKey(ad.Attr));
	//					}
	//					break;
	//				case DataType.AppDateTime:
	//				case DataType.AppDate:
	//					this.AddTD("valign=top ", en.GetValStringByKey(ad.Attr));
	//					break;
	//				case DataType.AppBoolean:
	//					this.AddTD("valign=top ", en.GetValBoolStrByKey(ad.Attr));
	//					break;
	//				case DataType.AppFloat:
	//				case DataType.AppInt:
	//					this.AddTD("valign=top class='TDNum'", en.GetValStringByKey(ad.Attr));
	//					break;
	//				case DataType.AppMoney:
	//					this.AddTD("valign=top class='TDNum'", en.GetValDecimalByKey(ad.Attr).ToString("0.00"));
	//					break;
	//				case DataType.AppDouble:
	//				case DataType.AppRate:
	//					this.AddTD("valign=top class='TDNum'", en.GetValStringByKey(ad.Attr));
	//					break;
	//				default:
	//					break;
	//			}
	//			this.AddTD("valign=right  ", ad.Desc);
	//			this.AddTREnd();
	//		}
	//		this.AddTableEnd();
	//	}

	public static void DataPanelDtl(StringBuilder sb, Entities ens, String ctrlId) {
		//    	this.Controls.Clear();
		Entity myen = ens.getGetNewEntity();
		String pk = myen.getPK();
		String clName = myen.toString();

		Attrs attrs = myen.getEnMap().getAttrs();
		Attrs selectedAttrs = myen.getEnMap().GetChoseAttrs(ens);
		Searchs cfgs = new Searchs();
		cfgs.RetrieveBy(SearchAttr.For, ens.toString());

		// 生成标题
		sb.append("<Table border='1' width='100%' align=left cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#C0C0C0'>");
		sb.append(AddTR());
		sb.append(AddTDTitle("序"));		
		for (Attr attrT : selectedAttrs) {
			if (!attrT.getUIVisible())
				continue;
			if ("MyNum".equals(attrT.getKey()))
				continue;
			sb.append(AddTDTitle(attrT.getDesc()));
		}

		boolean isRefFunc = false;
		isRefFunc = true;

		int pageidx = getPageIdx() - 1;
		int idx = SystemConfig.getPageSize() * pageidx;
		boolean is1 = false;

		//        #region 用户界面属性设置
		UIRowStyleGlo tableStyle = UIRowStyleGlo.MouseAndAlternately;
		boolean IsEnableDouclickGlo = true;
		boolean IsEnableRefFunc = true;
		boolean IsEnableFocusField = true;
		boolean isShowOpenICON = true;
		String FocusField = null;
		//int WinCardH = 600;
		//int WinCardW = 500;
		int WinCardH =500; // ens.GetEnsAppCfgByKeyInt("WinCardH", 500); // 弹出窗口高度
		int WinCardW = 820;// ens.GetEnsAppCfgByKeyInt("WinCardW", 820); // 弹出窗口宽度
		try {
			tableStyle = UIRowStyleGlo.forValue(0); // UIRowStyleGlo.forValue(ens.GetEnsAppCfgByKeyInt("UIRowStyleGlo")); // 界面风格。           
			IsEnableDouclickGlo=true;// = ens.GetEnsAppCfgByKeyBoolen("IsEnableDouclickGlo"); // 是否启用双击
			IsEnableRefFunc =true;// ens.GetEnsAppCfgByKeyBoolen("IsEnableRefFunc"); // 是否显示相关功能。
			IsEnableFocusField =false; // ens.GetEnsAppCfgByKeyBoolen("IsEnableFocusField"); //是否启用焦点字段。
			isShowOpenICON = true; //ens.GetEnsAppCfgByKeyBoolen("IsEnableOpenICON"); //是否启用 OpenICON 。

			FocusField = null;
			if (IsEnableFocusField)
				FocusField = ""; //ens.GetEnsAppCfgByKeyString("FocusField");
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean isAddTitle = false; //是否显示相关功能列。
		if (isShowOpenICON)
			isAddTitle = true;
		if (IsEnableRefFunc)
			isAddTitle = true;
		//        #endregion 用户界面属性设置

		if (isAddTitle)
			sb.append(AddTDTitle());

		sb.append(AddTREnd());

		String urlExt = "";
		for (Object obj : ens.subList(0, ens.size())) {
			//            #region 处理keys
			Entity en = (Entity) obj;
			String style = WebUser.getStyle();
			String url = GenerEnUrl(en, attrs);
			//            #endregion

			urlExt = "\"javascript:ShowEn('../Comm/RefFunc/UIEn.jsp?EnsName=" + ens.toString() + "&PK=" + en.GetValByKey(pk) + url + "', 'cd','"
					+ WinCardH + "','" + WinCardW + "');\"";
			urlExt = urlExt.replace("<font color=red>", "").replace("</font>", "");
			switch (tableStyle) {
			case None:
				if (IsEnableDouclickGlo)
					sb.append(AddTR("ondblclick=" + urlExt));
				else
					sb.append(AddTR());
				break;
			case Mouse:
				if (IsEnableDouclickGlo)
					sb.append(AddTRTX("ondblclick=" + urlExt));
				else
					sb.append(AddTRTX());
				break;
			case Alternately:
			case MouseAndAlternately:
				if (IsEnableDouclickGlo)
					sb.append(AddTR(is1, "ondblclick=" + urlExt));
				else
					sb.append(AddTR(is1));
				break;
			default:
				System.err.println("@目前还没有提供。");
				//                    throw new Exception("@目前还没有提供。");
			}

			idx++;
			sb.append(AddTDIdx(idx));
			String val = "";
			for (Attr attr : selectedAttrs) {
				if (!attr.getUIVisible())
					continue;

				if ("MyNum".equals(attr.getKey()))
					continue;

				DataPanelDtlAdd(sb, en, attr, cfgs, url, urlExt, FocusField);
			}

			if (IsEnableRefFunc && isRefFunc) {
				String str = "";

				//                #region 加入他门的方法
				RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
				for (RefMethod func : myreffuncs) {
					if (func.Visable == false || func.IsForEns == false)
						continue;
					//this.Request.ApplicationPath    ====>>>>     getBasePath()
					str += "<A style='cursor:pointer;' nowrap=true onclick=\"javascript:RefMethod1('" + getBasePath() + "', '" + func.Index + "', '"
							+ func.Warning + "', '" + func.Target + "', '" + ens.toString() + "','" + url + "') \"  > " + func.GetIcon(getBasePath())
							+ "" + func.Title + "</A>";
				}
				//                #endregion

				//                #region 加入他的明细
				EnDtls enDtls = en.getEnMap().getDtls();
				for (EnDtl enDtl : enDtls) {
					str += "[<A onclick=\"javascript:EditDtl1('" + getBasePath() + "', '" + myen.ToStringAtParas() + "',  '" + enDtl.getEnsName()
							+ "', '" + enDtl.getRefKey() + "', '" + url + "&IsShowSum=1')\" >" + enDtl.getDesc() + "</A>]";
				}
				//                #endregion

				sb.append("<TD class='TD' style='cursor:pointer;' nowrap=true  >" + str + "</TD>");

			} else {
				//sb.append("<TD class='TD' style='cursor:pointer;' nowrap=true></TD>");
				//if (isShowOpenICON)
				//sb.append("<TD class='TD' style='cursor:pointer;' nowrap=true><a href=" + urlExt + " ><img src='"+getBasePath()+"WF/Img/Btn/open.gif' border=0/></a></TD>");
			}
			sb.append(AddTREnd());
		}

		//        #region  求合计代码写在这里。
		String NoShowSum = SystemConfig.GetConfigXmlEns("NoShowSum", ens.toString());
		if (StringHelper.isNullOrEmpty(NoShowSum))
			NoShowSum = "";

		boolean IsHJ = false;
		for (Attr attr : selectedAttrs) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;

			if (attr.getUIContralType() == UIContralType.DDL)
				continue;

			if (NoShowSum.indexOf("@" + attr.getKey() + "@") != -1)
				continue;

			if ("OID".equals(attr.getKey()) || "MID".equals(attr.getKey()) || "WORKID".equals(attr.getKey().toUpperCase()))
				continue;

			switch (attr.getMyDataType()) {
			case DataType.AppDouble:
			case DataType.AppFloat:
			case DataType.AppInt:
			case DataType.AppMoney:
				IsHJ = true;
				break;
			default:
				break;
			}
		}

		IsHJ = false;

		if (IsHJ) {
			// 找出配置是不显示合计的列。

			if (StringHelper.isNullOrEmpty(NoShowSum))
				NoShowSum = "";

			sb.append("<TR class='TRSum' >");
			sb.append(AddTD("合计"));
			for (Attr attr : selectedAttrs) {

				if (attr.getMyFieldType() == FieldType.RefText)
					continue;

				if (!attr.getUIVisible())
					continue;

				if ("MyNum".equals(attr.getKey()))
					continue;

				if (attr.getMyDataType() == DataType.AppBoolean) {
					sb.append(AddTD());
					continue;
				}

				if (attr.getUIContralType() == UIContralType.DDL) {
					sb.append(AddTD());
					continue;
				}
				if ("OID".equals(attr.getKey()) || "MID".equals(attr.getKey()) || "WORKID".equals(attr.getKey().toUpperCase())) {
					sb.append(AddTD());
					continue;
				}

				if (NoShowSum.indexOf("@" + attr.getKey() + "@") != -1) {
					/*不需要显示它他们的合计。*/
					sb.append(AddTD());
					continue;
				}

				switch (attr.getMyDataType()) {
				case DataType.AppDouble:
					sb.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
					break;
				case DataType.AppFloat:
					sb.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
					break;
				case DataType.AppInt:
					sb.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
					break;
				case DataType.AppMoney:
					sb.append(AddTDJE(ens.GetSumDecimalByKey(attr.getKey())));
					break;
				default:
					sb.append(AddTD());
					break;
				}
			}
			sb.append(AddTREnd());
		}
		//        #endregion
		sb.append(AddTableEnd());
	}

	private static void DataPanelDtlAdd(StringBuilder sb, Entity en, Attr attr, Searchs cfgs, String url, String cardUrl, String focusField) {
		String cfgurl = "";
		if (attr.getUIContralType() == UIContralType.DDL) {
			sb.append(AddTD(en.GetValRefTextByKey(attr.getKey())));
			return;
		}
        if (attr.getUIHeight() != 0){
        	sb.append(AddTDDoc("...", "..."));
            return;
        }
		String str = en.GetValStrByKey(attr.getKey());

		if (null != focusField && focusField.equals(attr.getKey()))
			str = "<a href=" + cardUrl + ">" + str + "</a>";

		switch (attr.getMyDataType()) {
		case DataType.AppDate:
		case DataType.AppDateTime:
			if (StringHelper.isNullOrEmpty(str))
				str = "&nbsp;";
			sb.append(AddTD(str));
			break;
		case DataType.AppString:
			if (StringHelper.isNullOrEmpty(str))
				str = "&nbsp;";

			if (attr.getUIHeight() != 0) {
				sb.append(AddTDDoc(str, str));
			} else {
				//if (attr.getKey().indexOf("ail") == -1)
				sb.append(AddTD(str));
				//else
				//sb.append(AddTD("<a href=\"javascript:mailto:" + str + "\"' >" + str + "</a>"));
			}
			break;
		case DataType.AppBoolean:
			if (str.equals("1"))
				sb.append(AddTD("是"));
			else
				sb.append(AddTD("否"));
			break;
		case DataType.AppFloat:
		case DataType.AppInt:
		case DataType.AppRate:
		case DataType.AppDouble:
			for (XmlEn xml : cfgs.subList(0, cfgs.size())) {
				Search pe = (Search) xml;
				if (pe.getAttr().equals(attr.getKey())) {
					cfgurl = pe.getURL();
					Attrs attrs = en.getEnMap().getAttrs();
					for (Attr attr1 : attrs)
						cfgurl = cfgurl.replace("@" + attr1.getKey(), en.GetValStringByKey(attr1.getKey()));

					break;
				}
			}
			if (StringHelper.isNullOrEmpty(cfgurl)) {
				sb.append(AddTDNum(str));
			} else {
				cfgurl = cfgurl.replace("@Keys", url);
				sb.append(AddTDNum("<a href=\"javascript:WinOpen('" + cfgurl + "','dtl1');\" >" + str + "</a>"));
			}
			break;
		case DataType.AppMoney:
			cfgurl = "";
			for (XmlEn xml : cfgs.subList(0, cfgs.size())) {
				Search pe = (Search) xml;
				if (pe.getAttr().equals(attr.getKey())) {
					cfgurl = pe.getURL();
					Attrs attrs = en.getEnMap().getAttrs();
					for (Attr attr2 : attrs)
						cfgurl = cfgurl.replace("@" + attr2.getKey(), en.GetValStringByKey(attr2.getKey()));
					break;
				}
			}
			if (StringHelper.isNullOrEmpty(cfgurl)) {
				sb.append(AddTDJE(new BigDecimal(str)));
			} else {
				cfgurl = cfgurl.replace("@Keys", url);
				sb.append(AddTDNum("<a href=\"javascript:WinOpen('" + cfgurl + "','dtl1');\" >"
						+ (new BigDecimal(str)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "</a>"));
			}
			break;
		default:
			System.err.println("no this case ...");
			//                throw new Exception("no this case ...");
		}
	}

	public static String GenerEnUrl(Entity en, Attrs attrs) {
		String url = "";
		for (Attr attr : attrs) {
			switch (attr.getUIContralType()) {
			case TB:
				if (attr.getIsPK())
					url += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
				break;
			case DDL:
				url += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
				break;
			default:
				break;
			}
		}
		return url;
	}

	public static void sendRedirect(String url) {
		try {
			ContextHolderUtils.getResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String GenerateLineChart(DataTable dt, String xfield, String xdesc, java.util.Map<String, String> yfields, String title,
			int chartWidth, int chartHeight) {
		StringBuilder lineStrB = new StringBuilder();

		lineStrB.append(" <chart  exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=生成PNG图片|JPG=生成JPG图片|PDF=生成PDF文件' decimalPrecision='4'  hoverCapBgColor='AFD8F8' bgColor='2E4A89, 90B1DE' outCnvBaseFontSize='12' anchorBgColor='008ED6' caption='"
				+ title + "'");
		lineStrB.append("AlternateHGridColor='ff5904' divLineColor='ff5904' divLineAlpha='20' alternateHGridAlpha='5' >");
		lineStrB.append("numberPrefix='' showNames='1' showValues='1'  showAlternateHGridColor='1' ");
		lineStrB.append(" decimalPrecision='0' formatNumberScale='0' ");
		lineStrB.append(" yAxisName='hkhkhk' "); // 底部显示的数据.
		lineStrB.append(" xAxisName='adadasd' "); // 左边显示的数据.

		lineStrB.append(" <categories font='Arial' fontSize='11' fontColor='000000' >");
		for (DataRow dr : dt.Rows) {
			lineStrB.append("<category  label='" + dr.getValue(xfield) + "' />");
		}
		lineStrB.append("</categories>");
		for (java.util.Map.Entry<String, String> yfield : yfields.entrySet()) {
			if (StringHelper.isNullOrEmpty(yfield.getValue()) == true) {
				continue;
			}
			lineStrB.append("<dataset seriesname='" + yfield.getValue() + "'  >");
			for (DataRow dr : dt.Rows) {
				lineStrB.append(" <set value='" + dr.getValue(yfield.getKey()) + "' />");
			}
			lineStrB.append(" </dataset>");
		}
		lineStrB.append(" </chart>");
		String q = lineStrB.toString();

		return "<script type='text/javascript'>" + "  var chart = new FusionCharts(\"" + Glo.getCCFlowAppPath()
				+ "WF/Comm/Charts/MSLine.swf\", \"ChartLine\", '" + chartWidth + "', '" + chartHeight + "', '0', '0');" + "  chart.setDataXML(\"" + q
				+ "\");" + "  chart.render(\"line_chart_div\");" + "</script>";
	}

	public static String GeneratePieChart(DataTable dt, String xfield, String xdesc, java.util.Map<String, String> yfields, String title,
			int chartWidth, int chartHeight) {
		java.util.Map.Entry<String, String> first = null;
		for (java.util.Map.Entry<String, String> entry : yfields.entrySet()) {
			first = entry;
			break;
		}
		StringBuilder pieStrB = new StringBuilder();

		pieStrB.append("<chart  exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' exportDialogMessage='正在生成,请稍候...' "
				+ "exportFormats='PNG=生成PNG图片|JPG=生成JPG图片|PDF=生成PDF文件' caption='"
				+ title
				+ "-"
				+ first.getValue()
				+ "' outCnvBaseFontColor='FFFFFF' "
				+ "hoverCapBgColor='2E4A89' basefontcolor='FFFFFF' basefontsize='14' bgColor='2E4A89, 90B1DE' palette='2' animation='1' formatNumberScale='0' pieSliceDepth='30' startingAngle='125'>");

		for (DataRow dr : dt.Rows) {
			pieStrB.append(" <set  value='" + dr.getValue(first.getKey()) + "' label='" + dr.getValue(xfield) + "'/>");
		}
		pieStrB.append("</chart>");

		String q = pieStrB.toString();

		return "<script type='text/javascript'>" + "  var chart = new FusionCharts(\"" + Glo.getCCFlowAppPath()
				+ "WF/Comm/Charts/Pie3D.swf\", \"ChartIdP\", '" + chartWidth + "', '" + chartHeight + "', '0', '0');" + "  chart.setDataXML(\"" + q
				+ "\");" + "  chart.render(\"pie_chart_div\");" + "</script>";

	}

	public static String GenerateColumnChart(DataTable dt, String xfield, String xdesc, java.util.Map<String, String> yfields, String title,
			int chartWidth, int chartHeight) {
		String js = "";
		StringBuilder columnStrB = new StringBuilder();

		columnStrB
				.append(" <chart  exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=生成PNG图片|JPG=生成JPG图片|PDF=生成PDF文件' decimalPrecision='4'  hoverCapBgColor='ffffff' bgColor='B8D288,FFFFFF' outCnvBaseFontSize='12' anchorBgColor='008ED6' caption='"
						+ title + "'");
		columnStrB.append("AlternateHGridColor='ff5904' divLineColor='ff5904' divLineAlpha='20' alternateHGridAlpha='5' >");
		columnStrB.append("numberPrefix='' showNames='1' showValues='1'  showAlternateHGridColor='1' ");
		columnStrB.append(" decimalPrecision='0' formatNumberScale='0' ");
		columnStrB.append(" yAxisName='hkhkhk' "); // 底部显示的数据.
		columnStrB.append(" xAxisName='adadasd' "); // 左边显示的数据.

		columnStrB.append(" <categories font='Arial' fontSize='11' fontColor='000000' >");
		for (DataRow dr : dt.Rows) {
			columnStrB.append("<category  label='" + dr.getValue(xfield) + "' />");
		}
		columnStrB.append("</categories>");
		for (java.util.Map.Entry<String, String> entry : yfields.entrySet()) {
			if (StringHelper.isNullOrEmpty(entry.getValue()))
				continue;
			columnStrB.append("<dataset seriesname='" + entry.getValue() + "'  >");
			for (DataRow dr : dt.Rows) {
				{
					columnStrB.append(" <set value='" + dr.getValue(entry.getKey()) + "' />");
				}
				columnStrB.append(" </dataset>");
			}
			columnStrB.append(" </chart>");
			String q = columnStrB.toString();

			js = ("<script type='text/javascript'>" + "  var chart = new FusionCharts(\"" + Glo.getCCFlowAppPath()
					+ "WF/Comm/Charts/MSColumn3D.swf\", \"ChartLine\", '" + chartWidth + "', '" + chartHeight + "', '0', '0');"
					+ "  chart.setDataXML(\"" + q + "\");" + "  chart.render(\"column_chart_div\");" + "</script>");

		}
		return js;
	}

	//private int MsgFontSize=1;
	/// <summary>
	/// 输出到页面上红色的警告。
	/// </summary>
	/// <param name="msg">消息</param>
	protected void ResponseWriteRedMsg(String msg) {
		msg = msg.replace("@", "<BR>@");
		get_request().getSession().setAttribute("info", msg);
		//        System.Web.HttpContext.Current.Application["info" + WebUser.getNo()] = msg;
		String url = Glo.getCCFlowAppPath() + "WF/Comm/Port/ErrorPage.jsp";
		WinOpen(url, "警告", "errmsg", 500, 400, 150, 270);
	}

	/// <summary>
	/// 开始增加菜单
	/// </summary>
	public String MenuSelfBegin() {
		return "\n<Table style='width:100%;' cellpadding='5' cellspacing='5'>\n<TR>";
	}

	/// <summary>
	/// 增加一个lab
	/// </summary>
	/// <param name="attr">TD里面的属性</param>
	/// <param name="lab">标签</param>
	public static String MenuSelfLab(String attr, String lab) {
		return "\n<TD " + attr + ">" + lab + "</TD>";
	}

	public static String MenuSelfItem(String url, String lab, String target) {
		return "\n<TD class=Menu><a href=\"" + url + "\" target=" + target + ">" + lab + "</a></TD>";
	}

	public static String MenuSelfItemLab(String lab) {
		return "\n<TD class=Menu>" + lab + "</TD>";
	}

	public static String MenuSelfItem(String url, String lab, String target, boolean selected) {
		if (selected == false)
			return MenuSelfItem(url, lab, target);
		else
			return MenuSelfItemS(url, lab, target);
	}

	public static String MenuSelfItemS(String url, String lab, String target) {
		return "\n<TD class=MenuS >" + lab + "</TD>";
	}

	/// <summary>
	/// 结束菜单
	/// </summary>
	public static String MenuSelfEnd(int perBlankLeft) {
		return "\n<TD width='" + perBlankLeft + "%' ></TD>\n</TR>" + AddTableEnd();
	}

	/// <summary>
	/// 结束菜单
	/// </summary>
	public static String MenuSelfEnd() {
		return "\n</TR>" + AddTableEnd();
	}

	public static String BindEns(Entities ens) {
		StringBuilder builder = new StringBuilder();
		Attrs attrs = ens.getGetNewEntity().getEnMap().getAttrs();
		builder.append(AddTable());
		builder.append(AddTR());
		for (Attr attr : attrs) {
			if (attr.getKey().equals("MyNum") || attr.getUIIsDoc() == true) {
				continue;
			}

			if (attr.getIsRefAttr() || attr.getUIVisible() == false) {
				continue;
			}

			builder.append(AddTDTitle(attr.getDesc()));
		}
		builder.append(AddTREnd());

		//		boolean is1 = false;
		for (Entity en : Entities.convertEntities(ens)) {
			//			is1 = this.AddTR(is1);
			for (Attr attr : attrs) {
				if (attr.getKey().equals("MyNum") || attr.getUIIsDoc() == true) {
					continue;
				}

				if (attr.getIsRefAttr() || attr.getUIVisible() == false) {
					continue;
				}

				if (attr.getUIHeight() != 0 && attr.getIsNum() == false) {
					continue;
				}

				switch (attr.getMyDataType()) {
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppInt:
					builder.append(AddTDNum(en.GetValStringByKey(attr.getKey())));
					break;
				case DataType.AppMoney:
					DecimalFormat format = new DecimalFormat("0.00");
					String val = format.format(en.GetValDecimalByKey(attr.getKey()));
					builder.append(AddTDNum(val));
					break;
				default:
					builder.append(AddTD(en.GetValStrByKey(attr.getKey())));
					break;
				}
			}
			builder.append(AddTREnd());
		}
		builder.append(AddTableEnd());
		return builder.toString();
	}
	
	public TextBox GetTBByID(String key) throws Exception
    {
        try
        {
        	UiFatory uf=new UiFatory();
            return (TextBox)uf.GetUIByID(key);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage() + " 请确认：TB AND TextBox " + key);
        }
    }

}