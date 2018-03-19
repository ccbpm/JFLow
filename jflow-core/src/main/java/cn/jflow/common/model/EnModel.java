package cn.jflow.common.model;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.AttachmentUploadType;
import BP.Sys.DtlShowModel;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmBtn;
import BP.Sys.FrmBtns;
import BP.Sys.FrmEle;
import BP.Sys.FrmEleAttr;
import BP.Sys.FrmEleDB;
import BP.Sys.FrmEleDBAttr;
import BP.Sys.FrmEleDBs;
import BP.Sys.FrmEles;
import BP.Sys.FrmEventList;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImg;
import BP.Sys.FrmImgAth;
import BP.Sys.FrmImgAthDB;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLab;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLine;
import BP.Sys.FrmLines;
import BP.Sys.FrmLink;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBs;
import BP.Sys.FrmRpt;
import BP.Sys.FrmShowWay;
import BP.Sys.FrmSubFlow;
import BP.Sys.FrmSubFlowSta;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.ImgAppType;
import BP.Sys.M2MType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapFrame;
import BP.Sys.MapFrames;
import BP.Sys.MapM2M;
import BP.Sys.MapM2Ms;
import BP.Sys.PicType;
import BP.Sys.SignType;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Work;
import BP.WF.WorkAttr;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Entity.FrmWorkCheckSta;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

/**
 * Myflow UI界面组件
 * 
 * @author Administrator
 * 
 */
public class EnModel extends BaseModel {

	private String fk_node="0";
	private String enName;
	private String ensName;

	private String paramsStr;
	private String basePath;

	// 根据宽度计算出来微调.暂时900px
	private float wtX = 0;
	private float x = 0;

	// 是否加载CA签名 dll.
	private boolean IsAddCa = false;
	private boolean isReadonly, IsPostBack;
	private String FK_MapData = null;
	private String LinkFields = "";
	private String ctrlUseSta;

	private FrmEvents fes = null;
	private MapExts mes = null;
	private MapAttrs mattrs = null;
	private Entity HisEn;
	private MapData mapData;
	private MapM2Ms m2ms;
	private MapDtls dtls;

	private ArrayList<String> scripts = new ArrayList<String>();
	private ArrayList<String> csslinks = new ArrayList<String>();

	public StringBuilder Pub = new StringBuilder();
	public StringBuilder scriptsBlock = new StringBuilder();

	private void initParamater() {
		// fk_flow = get_request().getParameter("FK_Flow");
		// fk_node = get_request().getParameter("FK_Node");
		// workId = get_request().getParameter("WorkID");
		ensName = get_request().getParameter("EnsName");
		basePath = BP.WF.Glo.getCCFlowAppPath();
		paramsStr = "&" + get_request().getQueryString();
	}

	public void setFk_node(String fk_node) {
		this.fk_node = fk_node;
	}

	public ArrayList<String> getScripts() {
		return scripts;
	}

	public ArrayList<String> getCCSLinks() {
		return csslinks;
	}

	public void appendPub(String content) {
		Pub.append(content);
	}

	public EnModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		if (!IsLoadData) {
			IsLoadData = true;
		}
		
		initParamater();
	}

	/*public void BindCCForm(Entity en, String enName, boolean isReadonly,
			float srcWidth) {
		MapData md = new MapData(enName);
		BindCCForm(en, md, md.getMapAttrs(), enName, isReadonly, srcWidth);
	}*/
	public void BindCCForm(Entity en, String enName, boolean isReadonly, float srcWidth, boolean IsEnableLoadData)
	{
		MapData md = new MapData(enName);
		BindCCForm(en, md, md.getMapAttrs(), enName, isReadonly, srcWidth, IsEnableLoadData);
	}
	/*public void BindCCForm(Entity en, MapData md, MapAttrs mattrs,
			String enName, boolean isReadonly, float srcWidth) {
		ctrlUseSta = "";
		this.isReadonly = isReadonly;
		// en.getRow();

		this.enName = enName;
		mapData = md;

		// 根据宽度计算出来微调.
		wtX = MapData.GenerSpanWeiYi(md, srcWidth);

		// mes = mapData.getMapExts();
		// setIsReadonly(isReadonly);
		FK_MapData = enName;
		HisEn = en;
		m2ms = mapData.getMapM2Ms();
		dtls = mapData.getMapDtls();
		mes = mapData.getMapExts();
		this.mattrs = mattrs;

		// 是否加载CA签名 dll.
		IsAddCa = false;

		// region 处理事件.
		fes = mapData.getFrmEvents();
		if (!IsPostBack) {
			// try
			// {
			// String msg = fes.DoEventNode(FrmEventList.FrmLoadBefore, en);
			// if (msg.equals("OK"))
			// {
			// en.RetrieveFromDBSources();
			// }
			// else
			// {
			// if (!StringHelper.isNullOrEmpty(msg))
			// {
			// en.RetrieveFromDBSources();
			// //Alert(msg);
			// }
			// }
			// }
			// catch (RuntimeException ex)
			// {
			// //Alert(ex.getMessage());
			// ToErrorPage(ex.getMessage());
			// return;
			// }
		}
		// endregion 处理事件.

		// MapAttrs mattrs = mapData.MapAttrs;
		DealDefVal(mattrs);

		// 处理装载前填充.
		LoadData(mattrs, en);
		// 输出Ele
		printFrmEles();
		
		// 输出按钮
		printFrmBtns();
		
		// 输出标签
		printFrmLabs();
		// 输出线
		printFrmLines();
		// 输出超链接
		printFrmLinks();
		// 输出图片
		printFrmImgs();
		// 输出数据控件，编辑框，复选框，下拉菜单
		printBaseDataUI();
		// 输出 单选.
		printFrmRBs();
		// 输出报表
		printFrmRpts();
		
		// 输出明细.
		printFrmDtls();
		
		// 输出审核组件
		printFrmWorkCheck();
		
		//输出父子流程.
		printSubFlow();
		
		
		// 输出多对多的关系, 用户不常用，最后再翻译
		// printMapM2Ms();
		// 输出附件
		printFrmAttachments();
		// 处理扩展.
		if (!isReadonly) {
			AfterBindEn_DealMapExt(enName, mattrs, HisEn);
		}
	}*/
	public void BindCCForm(Entity en, MapData md, MapAttrs mattrs,
			String enName, boolean isReadonly, float srcWidth, boolean IsEnableLoadData) {
		ctrlUseSta = "";
		this.isReadonly = isReadonly;
		// en.getRow();

		this.enName = enName;
		mapData = md;

		// 根据宽度计算出来微调.
		wtX = MapData.GenerSpanWeiYi(md, srcWidth);

		// mes = mapData.getMapExts();
		// setIsReadonly(isReadonly);
		FK_MapData = enName;
		HisEn = en;
		m2ms = mapData.getMapM2Ms();
		dtls = mapData.getMapDtls();
		mes = mapData.getMapExts();
		this.mattrs = mattrs;

		// 是否加载CA签名 dll.
		IsAddCa = false;

		// region 处理事件.
		fes = mapData.getFrmEvents();
		/*if (!IsPostBack) {
			try
			{
			String msg = fes.DoEventNode(FrmEventList.FrmLoadBefore, en);
			if (msg.equals("OK"))
			{
			en.RetrieveFromDBSources();
			}
			else
			{
			if (!StringHelper.isNullOrEmpty(msg))
			{
			en.RetrieveFromDBSources();
			Alert(msg);
			}
			}
			}
			catch (RuntimeException ex)
			{
			 Alert(ex.getMessage());
			 ToErrorPage(ex.getMessage());
			 return;
			 }*/
		//}
		// endregion 处理事件.

		// MapAttrs mattrs = mapData.MapAttrs;
		DealDefVal(mattrs);

		// 处理装载前填充.
        if (IsEnableLoadData == true)
		LoadData(mattrs, en);
        
        /*//#region 判断是否是手机.
		if (BP.Web.WebUser.UserWorkDev == UserWorkDev.Mobile)
		{
			//判断是否是手机
			this.BindHtml5Model(mattrs, en);
			// 处理扩展.
			if (isReadonly == false)
			{
				this.AfterBindEn_DealMapExt(enName, mattrs, en);
			}
			return;
		}*/
		
		// 输出Ele
		printFrmEles2();
		
		// 输出按钮
		printFrmBtns();
		
		// 输出标签
		printFrmLabs();
		// 输出线
		printFrmLines();
		// 输出超链接
		printFrmLinks();
		// 输出图片
		printFrmImgs();
		// 输出数据控件，编辑框，复选框，下拉菜单
		printBaseDataUI();
		// 输出 单选.
		printFrmRBs();
		// 输出报表
		printFrmRpts();
		
		// 输出明细.
		printFrmDtls();
		
		// 输出审核组件
		printFrmWorkCheck();
		
		//输出父子流程.
		printSubFlow();
		
		
		// 输出多对多的关系, 用户不常用，最后再翻译
		//printMapM2Ms();
		// 输出附件
		printFrmAttachments();
		// 处理扩展.
		if (!isReadonly) {
			AfterBindEn_DealMapExt(enName, mattrs, HisEn);
		}
	}
	/**
	 * 输出附件
	 */
	private void printFrmAttachments() {
		FrmAttachments aths = mapData.getFrmAttachments();
		// FrmAttachmentDBs athDBs = null;
		// if (aths.size() > 0)
		// {
		// athDBs = new FrmAttachmentDBs(enName, HisEn.getPKVal().toString());
		// }
		StringBuilder tempStr = new StringBuilder();
		for (FrmAttachment ath : aths.ToJavaList()) {
			if (!ath.getIsVisable()) {
				continue;
			}

			if (ath.getUploadType().equals(AttachmentUploadType.Single)) {
				// 单个文件
				// Object tempVar4 =
				// athDBs.GetEntityByKey(FrmAttachmentDBAttr.FK_FrmAttachment,
				// ath.getMyPK());
				// FrmAttachmentDB athDB = (FrmAttachmentDB)((tempVar4
				// instanceof FrmAttachmentDB) ? tempVar4 : null);
				x = ath.getX() + wtX;
				float y = ath.getY();

				tempStr.append("<DIV id='Fa")
						.append(ath.getMyPK())
						.append("' style='position:absolute;width:400px; left:")
						.append(x).append("px; top:").append(y)
						.append("px; text-align: left;float:left' >");
				tempStr.append("<span>");
				// if (ath.getIsUpload() && !getIsReadonly() )
				// {
				StringBuilder src = new StringBuilder();
				src.append(basePath)
						.append("WF/CCForm/SingleAttachmentUpload.jsp?HFK_Node=")
						.append(fk_node).append("&OID=")
						.append(HisEn.GetValStrByKey("OID")).append("&EnName=")
						.append(enName).append("&PKVal=")
						.append(HisEn.getPKVal().toString()).append("&Ath=")
						.append(ath.getNoOfObj()).append("&FK_FrmAttachment=")
						.append(ath.getMyPK()).append(paramsStr);
//				float h = ath.getH() <= 0 ? 50 : ath.getH();
				tempStr.append("<iframe id='F")
						.append(ath.getMyPK())
						.append("' src=\"")
						.append(src.toString())
						.append("\"frameborder=0  style='position:absolute;text-align: left;width:500px;height:")
						.append("50px' leftMargin='0'  topMargin='0' scrolling=auto>");
				tempStr.append("</iframe>");
				// }
				tempStr.append("</span>");
				tempStr.append("</DIV>");
			}

			if (ath.getUploadType().equals(AttachmentUploadType.Multi)) {
				x = ath.getX() + wtX;
				tempStr.append("<DIV id='Fd").append(ath.getMyPK())
						.append("' style='position:absolute; left:").append(x)
						.append("px; top:").append(ath.getY())
						.append("px; width:").append(ath.getW())
						.append("px; height:").append(ath.getH())
						.append("px;text-align: left;' >");
				tempStr.append("<span>");
				StringBuilder src = new StringBuilder();
				if (getIsReadonly()) {
					src.append(basePath)
							.append("WF/CCForm/AttachmentUpload.jsp?PKVal=")
							.append(HisEn.getPKVal().toString())
							.append("&Ath=").append(ath.getNoOfObj())
							.append("&FK_FrmAttachment=").append(ath.getMyPK())
							.append("&IsReadonly=1").append(paramsStr);
				} else {
					src.append(basePath)
							.append("WF/CCForm/AttachmentUpload.jsp?PKVal=")
							.append(HisEn.getPKVal().toString())
							.append("&Ath=").append(ath.getNoOfObj())
							.append("&FK_FrmAttachment=").append(ath.getMyPK())
							.append(paramsStr);
				}

				tempStr.append("<iframe ID='F")
						.append(ath.getMyPK())
						.append("'src='")
						.append(src.toString())
						.append("' frameborder=0  style='position:absolute;width:")
						.append(ath.getW())
						.append("px; height:")
						.append(ath.getH())
						.append("px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto></iframe>");
				tempStr.append("</span>");
				tempStr.append("</DIV>");
			}
		}

		appendPub(tempStr.toString());
		// endregion 输出附件.

		// region 输出 img 附件
		FrmImgAths imgAths = mapData.getFrmImgAths();
		if (imgAths.size() != 0 && !getIsReadonly()) {

			StringBuilder js = new StringBuilder(
					"\t\n<script type='text/javascript' >");
			js.append("\t\n function ImgAth(url, athMyPK)");
			js.append("\t\n {");
			js.append("\t\n  var v= window.showModalDialog(url, 'ddf', 'dialogHeight: 650px; dialogWidth: 950px;center: yes; help: no'); ");
			js.append("\t\n  if (v==null )  ");
			js.append("\t\n     return ;");
			js.append("\t\n document.getElementById('Img'+athMyPK ).setAttribute('src', v); ");
			js.append("\t\n }");
			js.append("\t\n</script>");
			appendPub(js.toString());
		}

		for (FrmImgAth ath : imgAths.ToJavaList()) {
			x = ath.getX() + wtX;
			appendPub("\t\n<DIV id=" + ath.getMyPK()
					+ " style='position:absolute;left:" + x + "px;top:"
					+ ath.getY() + "px;text-align:left;vertical-align:top' >");
			String url = basePath + "WF/CCForm/ImgAth.jsp?W=" + ath.getW()
					+ "&H=" + ath.getH() + "&FK_MapData=" + enName + "&MyPK="
					+ HisEn.getPKVal() + "&ImgAth=" + ath.getMyPK();
			if (!isReadonly && ath.getIsEdit()) {
				appendPub(AddFieldSet("<a href=\"javascript:ImgAth('" + url
						+ "','" + ath.getMyPK() + "');\" >编辑</a>"));
				// appendPub(AddFieldSet("<a href=\"#\" >编辑</a>"));
			}

			FrmImgAthDB imgAthDb = new FrmImgAthDB();
			imgAthDb.setMyPK(ath.getMyPK() + "_" + HisEn.getPKVal());
			imgAthDb.RetrieveFromDBSources();
			if (imgAthDb != null
					&& !StringHelper.isNullOrEmpty(imgAthDb.getFileName())) {
				appendPub("\t\n<img src='"
						+ basePath
						+ "DataUser/ImgAth/Data/"
						+ imgAthDb.getFileName()
						+ ".png' onerror=\"src='"
						+ basePath
						+ "WF/Data/Img/LogH.PNG'\" name='Img"
						+ ath.getMyPK()
						+ "' id='Img"
						+ ath.getMyPK()
						+ "' style='padding: 0px;margin: 0px;border-width: 0px;' width="
						+ ath.getW() + " height=" + ath.getH() + " />");
			} else {
				appendPub("\t\n<img src='"
						+ basePath
						+ "DataUser/ImgAth/Data/"
						+ ath.getMyPK()
						+ "_"
						+ HisEn.getPKVal()
						+ ".png' onerror=\"src='"
						+ basePath
						+ "WF/Data/Img/LogH.PNG'\" name='Img"
						+ ath.getMyPK()
						+ "' id='Img"
						+ ath.getMyPK()
						+ "' style='padding: 0px;margin: 0px;border-width: 0px;' width="
						+ ath.getW() + " height=" + ath.getH() + " />");
			}
			if (!isReadonly && ath.getIsEdit()) {
				appendPub(AddFieldSetEnd());
			}
			appendPub("\t\n</DIV>");
		}
		// endregion 输出附件
	}

	// private boolean CanEditor(String fileType)
	// {
	// try
	// {
	// String fileTypes = (String)
	// BP.Sys.SystemConfig.getAppSettings().get("OpenTypes");
	// if (StringHelper.isNullOrEmpty(fileTypes) == true)
	// {
	// fileTypes = "doc,docx,pdf,xls,xlsx";
	// }
	//
	// if (fileTypes.contains(fileType.toLowerCase()))
	// {
	// return true;
	// }
	// else
	// {
	// return false;
	// }
	// }
	// catch (RuntimeException e)
	// {
	// return false;
	// }
	// }

	/**
	 * 输出多对多业务
	 */
	private void printMapM2Ms() {
		for (MapM2M m2m : m2ms.ToJavaList()) {
			x = m2m.getX() + wtX;
			appendPub("<DIV id='Fd" + m2m.getNoOfObj()
					+ "' style='position:absolute; left:" + x + "px; top:"
					+ m2m.getY() + "px; width:" + m2m.getW() + "px; height:"
					+ m2m.getH() + "px;text-align: left;' >");
			appendPub("<span>");

			String src = ".jsp?NoOfObj=" + m2m.getNoOfObj();
			String paras = paramsStr;
			try {
				if (!paras.contains("FID=")) {
					paras += "&FID=" + HisEn.GetValStrByKey("FID");
				}
			} catch (java.lang.Exception e2) {
			}

			if (!paras.contains("OID=")) {
				paras += "&OID=" + HisEn.GetValStrByKey("OID");
			}
			src += "&r=q" + paras;
			if (m2m.getIsEdit()) {
				src += "&IsEdit=1";
			} else {
				src += "&IsEdit=0";
			}

			if (!src.contains("FK_MapData")) {
				src += "&FK_MapData=" + enName;
			}

			if (m2m.getHisM2MType().equals(M2MType.M2MM)) {
				src = basePath + "WF/CCForm/M2MM" + src;
			} else {
				src = basePath + "WF/CCForm/M2M" + src;
			}

			switch (m2m.getShowWay()) {
			case FrmAutoSize:
			case FrmSpecSize:
				if (m2m.getIsEdit()) {
					AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

					// Add("<iframe ID='F" + m2m.NoOfObj +
					// "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" +
					// src +
					// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
					// + m2m.W + "' height='" + m2m.H +
					// "'   scrolling=auto/></iframe>");
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'  onload='"
							+ m2m.getNoOfObj()
							+ "load();'  src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ m2m.getW() + "' height='" + m2m.getH()
							+ "'   scrolling=auto/></iframe>");

				} else {
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'  src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ m2m.getW() + "' height='" + m2m.getH()
							+ "'   scrolling=auto/></iframe>");
				}
				break;
			case Hidden:
				break;
			case WinOpen:
				appendPub("<a href=\"javascript:WinOpen('" + src
						+ "&IsOpen=1','" + m2m.getW() + "','" + m2m.getH()
						+ "');\"  />" + m2m.getName() + "</a>");
				break;
			default:
				break;
			}
			appendPub("</span>");
			appendPub("</DIV>");
		}
		// endregion 多对多的关系
	}
	/**
	 * 输出审核组件
	 */
	private void printFrmWorkCheck() {
		FrmWorkCheck fwc = new FrmWorkCheck(enName);
		if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable) {
			x = fwc.getFWC_X() + wtX;
			appendPub("<DIV id='FWC" + fwc.getNo()
					+ "' style='position:absolute; left:" + x + "px; top:"
					+ fwc.getFWC_Y() + "px; width:" + fwc.getFWC_W()
					+ "px; height:" + fwc.getFWC_H()
					+ "px;text-align: left;' >");
			appendPub("<span>");
			String src = basePath + "WF/WorkOpt/WorkCheck.jsp?s=2";
			String fwcOnload = "";
			String paras = paramsStr;
			try {
				if (!paras.contains("FID=")) {
					paras += "&FID=" + HisEn.GetValStrByKey("FID");
				}
			} catch (java.lang.Exception e) {
			}
			if (!paras.contains("OID=")) {
				paras += "&OID=" + HisEn.GetValStrByKey("OID");
			}
			if (fwc.getHisFrmWorkCheckSta().equals(FrmWorkCheckSta.Readonly)) {
				src += "&DoType=View";
			} else// yqh 2016年7月5日翻译
            {
                fwcOnload = "onload= 'WC" + fwc.getNo() + "load();'";
                AddLoadFunction("WC" + fwc.getNo(), "blur", "SaveDtl");
            }
			src += "&r=q" + paras;
			/*appendPub("<iframe ID='F33"
					+ fwc.getNo()
					+ "'  src='"
					+ src
					+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
					+ fwc.getFWC_W() + "' height='" + fwc.getFWC_H()
					+ "'   scrolling=auto/></iframe>");*/
			//yqh 2016年7月5日翻译
			appendPub("<iframe ID='FWC" + fwc.getNo() + "' " + fwcOnload + "  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + fwc.getFWC_W() + "' height='" + fwc.getFWC_H() + "'   scrolling=auto/></iframe>");
			appendPub("</span>");
			appendPub("</DIV>");
		}
	}
	
	/**
	 * 输出父子流程
	 */
	private void printSubFlow() {
		
		FrmSubFlow  subFlow = new FrmSubFlow(enName);
         if (subFlow.getHisFrmSubFlowSta() != FrmSubFlowSta.Disable.getValue()) {
        	 
			x = subFlow.getSF_X() + wtX;
			appendPub("<DIV id='DIVWC" + subFlow.getNodeID() + "' style='position:absolute; left:" + x + "px; top:" + subFlow.getSF_Y()
					+ "px; width:" + subFlow.getSF_W() + "px; height:" + subFlow.getSF_H() + "px;text-align: left;' >");
			appendPub("<span>");
			String src = basePath + "WF/WorkOpt/SubFlow.jsp?s=2";
			String fwcOnload = "";
			String paras = paramsStr;

			if (paras.contains("FID=") == false && HisEn.getRow().containsKey("FID"))
				paras += "&FID=" + HisEn.GetValStrByKey("FID");

			if (paras.contains("OID=") == false)
				paras += "&OID=" + this.HisEn.GetValStrByKey("OID");
			if (subFlow.getHisFrmSubFlowSta() == 2) {
				src += "&DoType=View";
			} else {
				//  fwcOnload = "onload= 'WC" + fwc.No + "load();'";
				// AddLoadFunction("WC" + fwc.No, "blur", "SaveDtl");
			}
			src += "&r=q" + paras;
			appendPub("<iframe ID='SF" + subFlow.getNodeID() + "' " + fwcOnload + "  src='" + src
					+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + subFlow.getSF_W() + "' height='"
					+ subFlow.getSF_H() + "'   scrolling=auto/></iframe>");
			appendPub("</span>");
			appendPub("</DIV>");  
			
         }            
	}

	/**
	 * 输出报表
	 */
	private void printFrmRpts() {
		// region 输出报表.
		for (FrmRpt rpt : mapData.getFrmRpts().ToJavaList()) {
			if (!rpt.getIsView()) {
				continue;
			}

			x = rpt.getX() + wtX;
			float y = rpt.getY();

			appendPub("<DIV id='Fd" + rpt.getNo()
					+ "' style='position:absolute; left:" + x + "px; top:" + y
					+ "px; width:" + rpt.getW() + "px; height:" + rpt.getH()
					+ "px;text-align: left;' >");
			appendPub("<span>");

			String src = "";
			if (rpt.getHisDtlShowModel().equals(DtlShowModel.Table)) {
				if (isReadonly) {
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName="
							+ rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=1&FID="
							+ HisEn.GetValStrByKey("FK_Node", "0");
				} else {
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName="
							+ rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=0&FID="
							+ HisEn.GetValStrByKey("FK_Node", "0");
				}
			} else {
				if (isReadonly) {
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName="
							+ rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=1&FID="
							+ HisEn.GetValStrByKey("FID", "0");
				} else {
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName="
							+ rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=0&FID="
							+ HisEn.GetValStrByKey("FID", "0");
				}
			}

			if (getIsReadonly() || rpt.getIsReadonly()) {
				appendPub("<iframe ID='F"
						+ rpt.getNo()
						+ "' src='"
						+ src
						+ "' frameborder=0  style='position:absolute;width:"
						+ rpt.getW()
						+ "px; height:"
						+ rpt.getH()
						+ "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			} else {
				AddLoadFunction(rpt.getNo(), "blur", "SaveDtl");

				// Add("<iframe ID='F" + rpt.No + "' Onblur=\"SaveDtl('" +
				// rpt.No + "');\"  src='" + src +
				// "' frameborder=0  style='position:absolute;width:" + rpt.W +
				// "px; height:" + rpt.H +
				// "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
				appendPub("<iframe ID='F"
						+ rpt.getNo()
						+ "' onload='"
						+ rpt.getNo()
						+ "load();'  src='"
						+ src
						+ "' frameborder=0  style='position:absolute;width:"
						+ rpt.getW()
						+ "px; height:"
						+ rpt.getH()
						+ "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			}

			appendPub("</span>");
			appendPub("</DIV>");
		}
		// endregion 输出报表.
	}

	/**
	 * 输出明细表
	 */
	private void printFrmDtls() {
		MapDtls dtls = mapData.getMapDtls();
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (!dtl.getIsView()) {
				continue;
			}

			x = dtl.getX() + wtX;
			float y = dtl.getY();
			appendPub("<DIV id='Fd" + dtl.getNo()
					+ "' style='position:absolute; left:" + x + "px; top:" + y
					+ "px; width:" + dtl.getW() + "px; height:" + dtl.getH()
					+ "px;text-align: left;' >");
			appendPub("<span>");

			String src = "";
			if (dtl.getHisDtlShowModel().equals(DtlShowModel.Table)) {
				if (getIsReadonly()) {
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName="
							+ dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=1&FID="
							+ HisEn.GetValStrByKey("FID", "0") + "&FK_Node="
							+ HisEn.GetValStrByKey("FK_Node", "0");
				} else {
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName="
							+ dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=0&FID="
							+ HisEn.GetValStrByKey("FID", "0") + "&FK_Node="
							+ HisEn.GetValStrByKey("FK_Node", "0");
				}
			} else {
				if (getIsReadonly())
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName="
							+ dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=1&FID="
							+ HisEn.GetValStrByKey("FID", "0");
				else
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName="
							+ dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal()
							+ "&IsReadonly=0&FID="
							+ HisEn.GetValStrByKey("FID", "0");
			}

			if (getIsReadonly() || dtl.getIsReadonly()) {
				appendPub("<iframe ID='F"
						+ dtl.getNo()
						+ "' src='"
						+ src
						+ "' frameborder=0  style='position:absolute;width:"
						+ dtl.getW()
						+ "px; height:"
						+ dtl.getH()
						+ "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			} else {
				AddLoadFunction(dtl.getNo(), "blur", "SaveDtl");
				appendPub("<iframe ID='F"
						+ dtl.getNo()
						+ "' onblur='SaveDtl(\""
						+ dtl.getNo()
						+ "\");' onload= '"
						+ dtl.getNo()
						+ "load();'  src='"
						+ src
						+ "' frameborder=0  style='position:absolute;width:"
						+ dtl.getW()
						+ "px; height:"
						+ dtl.getH()
						+ "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			}
			appendPub("</span></DIV>");
			if (!getIsReadonly()) {
				scriptsBlock.append("\t\n<script type='text/javascript' >");
				scriptsBlock.append("\t\n function SaveDtl(dtl) { ");
				scriptsBlock.append("\t\n   GenerPageKVs(); //调用产生kvs ");
				scriptsBlock
						.append("\t\n   document.getElementById('F' + dtl ).contentWindow.SaveDtlData();");
				scriptsBlock.append("\t\n } ");

				scriptsBlock.append("\t\n function SaveM2M(dtl) { ");
				scriptsBlock
						.append("\t\n   document.getElementById('F' + dtl ).contentWindow.SaveM2M();");
				scriptsBlock.append("\t\n } ");

				scriptsBlock.append("\t\n</script>");
			}
		}
	}

	/**
	 * 单选按钮
	 * 
	 * @param mattrs
	 */
	private void printFrmRBs() {
		ArrayList<String> selectRBsList = new ArrayList<String>();
		// 获取中集合
		for (MapAttr attr : mattrs.ToJavaList()) {
			if (attr.getUIContralType().equals(UIContralType.RadioBtn)) {
				String id = "RB_" + attr.getKeyOfEn() + "_"//ToLowerCase
						+ HisEn.GetValStrByKey(attr.getKeyOfEn());//ToLowerCase
				selectRBsList.add(id);
			}
		}

		FrmRBs myrbs = this.mapData.getFrmRBs();
		MapAttr attrRB = new MapAttr();
		for (FrmRB rb : myrbs.ToJavaList()) {
			x = rb.getX() + wtX;
			appendPub("<DIV id='F"
					+ rb.getMyPK()
					+ "' style='position:absolute; left:"
					+ x
					+ "px; top:"
					+ rb.getY()
					+ "px; height:16px;text-align: left;word-break: keep-all;' >");
			appendPub("<span style='word-break: keep-all;font-size:12px;'>");

			RadioButton rbCtl = new RadioButton();
			rbCtl.setId("RB_" + rb.getKeyOfEn() + "_"//ToLowerCase
					+ rb.getIntKey());
			rbCtl.setName("RB_" + rb.getKeyOfEn());//ToLowerCase
			rbCtl.setValue(String.valueOf(rb.getIntKey()));
			rbCtl.setText(rb.getLab());

			if (!attrRB.getKeyOfEn().equals(rb.getKeyOfEn())) {
				for (MapAttr ma : mattrs.ToJavaList()) {
					if (ma.getKeyOfEn().equals(rb.getKeyOfEn())) {
						attrRB = ma;
						break;
					}
				}
			}
			if (isReadonly || !attrRB.getUIIsEnable()) {
				rbCtl.setEnabled(false);
			} else {
				// add by dgq 2013-4-9,添加内容修改后的事件
				rbCtl.attributes.put("onmousedown", "Change('" + attrRB.getFK_MapData() + "')");
			}

			// 设置选中
			if (selectRBsList.contains(rbCtl.getId())) {
				rbCtl.setChecked(true);
			}

			appendPub(rbCtl.toString());
			appendPub("</span>");
			appendPub("</DIV>");
		}

		// 原来.net选中业务，有上面selectRBsList 代替
		// for (MapAttr attr : mattrs.ToJavaList())
		// {
		// if (attr.getUIContralType() == UIContralType.RadioBtn)
		// {
		// String id = "RB_" + attr.getKeyOfEn() + "_" +
		// HisEn.GetValStrByKey(attr.getKeyOfEn());
		// RadioButton rb = this.GetRBLByID(id);
		// if (rb != null)
		// {
		// rb.Checked = true;
		// }
		// }
		// }
		// endregion 输出 rb.
	}

	/**
	 * 输出编辑框，复选框，下拉菜单
	 */
	private void printBaseDataUI() {
		for (MapAttr attr : mattrs.ToJavaList()) {
			if (!attr.getUIVisible()) {
				TextBox tbH = new TextBox();
				tbH.setId("TB_" + attr.getKeyOfEn());//getKeyOfEnToLowerCase())
				tbH.setName("TB_" + attr.getKeyOfEn());//getKeyOfEnToLowerCase());
				tbH.setTextMode(TextBoxMode.Hidden);
				tbH.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));
				appendPub(tbH.toString());
				continue;
			}

			if (!attr.getUIVisible()) {
				continue;
			}

			x = attr.getX() + wtX;
			if (attr.getLGType().equals(FieldTypeS.Enum)
					|| attr.getLGType().equals(FieldTypeS.FK)) {
				appendPub("<DIV id='F"
						+ attr.getKeyOfEn()//ToLowerCase
						+ "' style='position:absolute; left:"
						+ x
						+ "px; top:"
						+ attr.getY()
						+ "px;  height:16px;text-align: left;word-break: keep-all;padding:0;' >");
			} else {
				appendPub("<DIV id='F"
						+ attr.getKeyOfEn()//ToLowerCase
						+ "' style='position:absolute; left:"
						+ x
						+ "px; top:"
						+ attr.getY()
						+ "px;  width:auto "
						+ attr.getUIWidth()
						+ "px; height:16px;text-align: left;word-break: keep-all;padding:0;' >");
			}

			appendPub("<span>");

			// region add contrals.
			if (!attr.getUIIsEnable()
					&& LinkFields.contains("," + attr.getKeyOfEn()//ToLowerCase
							+ ",")) {
				Object tempVar3 = mes.GetEntityByKey(MapExtAttr.ExtType,
						MapExtXmlList.Link);
				MapExt meLink = (MapExt) ((tempVar3 instanceof MapExt) ? tempVar3
						: null);
				String url = meLink.getTag();
				if (!url.contains("?")) {
					url = url + "?a3=2";
				}
				url = url + "&WebUserNo=" + WebUser.getNo() + "&SID="
						+ WebUser.getSID() + "&EnName=" + enName;
				if (url.contains("@AppPath")) {
					url = url.replace("@AppPath", basePath);
				}
				if (url.contains("@")) {
					Attrs attrs = HisEn.getEnMap().getAttrs();
					int size_attrs = attrs.size();
					for (int i = 0; i < size_attrs; i++) {
						url = url.replace("@" + attr.getKeyOfEn(),
								HisEn.GetValStrByKey(attr.getKeyOfEn()));
						if (!url.contains("@")) {
							break;
						}
					}
				}
				appendPub("<a href='" + url + "' target='" + meLink.getTag1()
						+ "' >" + HisEn.GetValByKey(attr.getKeyOfEn()) + "</a>");
				appendPub("</span>");
				appendPub("</DIV>");
				continue;
			}

			// region 数字签名
			if (attr.getIsSigan()) {
				// region 图片签名 (dai guoqiang)
				boolean isEdit = false; // 是否可以编辑签名

				String v = HisEn.GetValStrByKey(attr.getKeyOfEn());

				// 如果为空，默认使用当前登录人签名
				if (StringHelper.isNullOrEmpty(v))
					v = WebUser.getNo();

				// 如果为只读并且为空，显示为未签名
				if (getIsReadonly())
					v = "sigan-readonly";
				
				if (attr.getPicType().equals(PicType.ShouDong)) {
					isEdit = true;
					v = "sigan-readonly";
				}

				if (getFK_Node() != 0 && !getIsReadonly()) {
					// 获取表单方案，如果为可编辑，则对属性设置为true
					v = HisEn.GetValStrByKey(attr.getKeyOfEn());
					// long workId =
					// Long.parseLong(HisEn.GetValStrByKey("OID"));
					FrmField keyOfEn = new FrmField();
					QueryObject info = new QueryObject(keyOfEn);
					info.AddWhere(FrmFieldAttr.FK_Node, getFK_Node());
					info.addAnd();
					info.AddWhere(FrmFieldAttr.FK_MapData, attr.getFK_MapData());
					info.addAnd();
					info.AddWhere(FrmFieldAttr.KeyOfEn, attr.getKeyOfEn());
					info.addAnd();
					info.AddWhere(MapAttrAttr.UIIsEnable, "1");
					if (info.DoQuery() > 0) {
						isEdit = true; // 可编辑，如果值为空显示可编辑图片
						if (StringHelper.isNullOrEmpty(v)) {
							v = "siganture";
						}
					} else {
						isEdit = false;
						// 不可编辑，如果值为空显示不可编辑图片
						if (StringHelper.isNullOrEmpty(v)) {
							v = "sigan-readonly";
						}
					}
				}
				// 如果为可编辑，对签名进行修改

				if (attr.getSignType().equals(SignType.Pic)) {
					if (isEdit) {
						appendPub("<img src='"
								+ basePath
								+ "DataUser/Siganture/"
								+ v
								+ ".jpg' "
								+ "ondblclick=\"SigantureAct(this,'"
								+ WebUser.getNo()
								+ "','"
								+ attr.getFK_MapData()
								+ "','"
								+ attr.getKeyOfEn()
								+ "','"
								+ HisEn.GetValStrByKey("OID")
								+ "');\" border=\"0\" alt=\"双击进行签名或取消签名\" onerror=\"src='"
								+ basePath
								+ "DataUser/Siganture/UnName.jpg'\"/>");
					} else {
						appendPub("<img src='" + basePath
								+ "DataUser/Siganture/" + v
								+ ".jpg' border=0 onerror=\"src='" + basePath
								+ "DataUser/Siganture/UnName.jpg'\"/>");
					}

				} // 结束图片签名.
					// endregion 结束图片签名

				// region CA签名 (song honggang 2014-06-08)
				if (attr.getSignType().equals(SignType.CA)) {
					if (!IsAddCa) {
						IsAddCa = true;
						scripts.add("WF/Activex/Sign/Loadwebsign.js");
						scripts.add("WF/Activex/Sign/main.js");
					}

					if (!StringHelper.isNullOrEmpty(attr.getPara_SiganField())) {
						// string signClient = GetTBByID("TB_" +
						// attr.Para_SiganField).ClientID;
						String signClient = "TB_" + attr.getPara_SiganField();
						// if (getPageID().equals("Frm"))
						// {
						// signClient = "ctl00$ContentPlaceHolder1$UCEn1$TB_" +
						// attr.getPara_SiganField();
						// }
						// else
						// {
						// signClient =
						// "ctl00$ContentPlaceHolder1$MyFlowUC1$MyFlow1$UCEn1$TB_"
						// + attr.getPara_SiganField();
						// }

						appendPub("<span id='" + signClient + "sealpostion' />");
						appendPub("<img  src='"
								+ basePath
								+ "DataUser/Siganture/setting.JPG' ondblclick=\"addseal('"
								+ signClient
								+ "');\"  border=0 onerror=\"src='" + basePath
								+ "DataUser/Siganture/UnName.jpg'\"/>");
					}
				}
				// endregion 结束CA签名

				appendPub("</span>");
				appendPub("</DIV>");
				continue;

			}
			if (attr.getMaxLen() >= 3999 && attr.getTBModel() == 2) {
				AddRichTextBox(HisEn, attr);
				appendPub("</span>");
				appendPub("</DIV>");
				continue;
			}
			TextBox tb = new TextBox();
			if (attr.getUIContralType().equals(UIContralType.TB)) {
				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
				tb.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
				if (!attr.getUIIsEnable() || isReadonly) {
					tb.setReadOnly(true);
					 tb.setCssClass("TBReadonly");
				} else {
					// add by dgq 2013-4-9 添加修改事件
					tb.addAttr("onchange", "Change('" + attr.getFK_MapData()
							+ "');");
				}
				tb.attributes.put("tabindex", String.valueOf(attr.getIdx()));
			}

			switch (attr.getLGType()) {
			case Normal:
				switch (attr.getMyDataType()) {
				case BP.DA.DataType.AppString:
					if (attr.getUIRows() == 1) {
						if (!StringHelper.isNullOrEmpty(HisEn
								.GetValStringByKey(attr.getKeyOfEn()))) {
							tb.setText(HisEn.GetValStringByKey(attr
									.getKeyOfEn()));
						} else {
							tb.setText(attr.getDefVal());
						}
						tb.attributes
								.put("style",
										"height:"
												+ attr.getUIHeight()
												+ "px;"
												+ "width: "
												+ attr.getUIWidth()
												+ "px; text-align: left; padding: 0px;margin: 0px;");
						if (attr.getUIIsEnable() && !isReadonly) {
							tb.setCssClass("TB");
						} else {
							tb.setReadOnly(true);
							// tb.setCssClass("TBReadonly");
						}
						appendPub(tb.toString());
					} else {
						tb.setTextMode(TextBoxMode.MultiLine);

						if (!StringHelper.isNullOrEmpty(HisEn
								.GetValStringByKey(attr.getKeyOfEn()))) {
							tb.setText(HisEn.GetValStringByKey(attr
									.getKeyOfEn()));
						} else {
							tb.setText(attr.getDefVal());
						}
						tb.attributes
								.put("style",
										"height:"
												+ attr.getUIHeight()
												+ "px;"
												+ "width: "
												+ attr.getUIWidth()
												+ "px; text-align: left;padding: 0px;margin: 0px;");
						tb.attributes.put("maxlength",
								String.valueOf(attr.getMaxLen()));
						tb.setRows(attr.getUIRows());

						if (attr.getUIIsEnable() && !isReadonly) {
							tb.setCssClass("TBDoc");
							tb.attributes.put(
									"ondblclick",
									"TBHelp('" + tb.getId() + "','" + basePath
											+ "','" + enName + "','"
											+ attr.getKeyOfEn() + "');");
						} else {
							tb.setReadOnly(true);
							// tb.setCssClass("TBReadonly");
						}

						appendPub(tb.toString());
					}
					break;
				case BP.DA.DataType.AppDate:
					tb.setShowType(TBType.Date);
					String date = HisEn.GetValStrByKey(attr.getKeyOfEn());
					try {
						if (!StringHelper.isNullOrEmpty(date)) {
							date = date.substring(0, 10);
						}
					} catch (Exception e) {
					}

					tb.setText(date);
					if (attr.getUIIsEnable() && !getIsReadonly()) {
						tb.attributes.put("onfocus", "WdatePicker();");
						tb.attributes.put("class", "TB");
					} else {
						tb.setReadOnly(true);
						// tb.attributes.put("class", "TBReadonly");
					}
					tb.attributes.put("style", "height:" + attr.getUIHeight()
							+ "px;" + "width: " + attr.getUIWidth()
							+ "px; text-align: left;");
					appendPub(tb.toString());
					break;
				case BP.DA.DataType.AppDateTime:
					tb.setShowType(TBType.DateTime);
					tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));

					if (attr.getUIIsEnable() && !getIsReadonly()) {
						tb.attributes.put("class", "TBcalendar");
					} else {
						tb.setReadOnly(true);
						// tb.attributes.put("class", "TBReadonly");
					}

					if (attr.getUIIsEnable()) {
						tb.attributes.put("onfocus",
								"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
					}
					tb.attributes.put("style", "height:" + attr.getUIHeight()
							+ "px;" + "width: " + attr.getUIWidth()
							+ "px; text-align: left; ");
					appendPub(tb.toString());
					break;
				case BP.DA.DataType.AppBoolean:
					CheckBox cb = new CheckBox();
					// cb.attributes.put("style", "width: 350px");
					cb.setText(attr.getName().trim());
					cb.setId("CB_" + attr.getKeyOfEn());//ToLowerCase
					cb.setName("CB_" + attr.getKeyOfEn());//ToLowerCase
					cb.setChecked(attr.getDefValOfBool());
					cb.setEnabled(attr.getUIIsEnable());
					cb.setChecked(HisEn.GetValBooleanByKey(attr.getKeyOfEn()));
					if (!cb.getEnabled() || isReadonly) {
						cb.setEnabled(false);
					} else {
						// add by dgq 2013-4-9,添加内容修改后的事件
						cb.attributes.put("onmousedown",
								"Change('" + attr.getFK_MapData() + "')");
						cb.setEnabled(true);
					}
					appendPub(cb.toString());
					break;
				case BP.DA.DataType.AppDouble:
				case BP.DA.DataType.AppFloat:
					tb.attributes
							.put("style",
									"height:"
											+ attr.getUIHeight()
											+ "px;"
											+ "width: "
											+ attr.GetValStrByKey("UIWidth")
											+ "px; text-align: right; word-break: keep-all;");
					tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));

					if (attr.getUIIsEnable() && !isReadonly) {
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'');Change('"
						// + attr.FK_MapData + "');");
						tb.attributes.put("onkeyup",
								"Change('" + attr.getFK_MapData() + "');");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,0);");
						tb.attributes.put("onClick", "TB_ClickNum(this)");
						tb.attributes.put("onkeydown", "VirtyNum(this)");
						tb.addAttr("OnKeyPress",
								"javascript:return  VirtyNum(this,'float');");
						tb.attributes.put("class", "TBNum");
					} else {
						tb.setReadOnly(true);
						// tb.attributes.put("class", "TBReadonly");
					}

					appendPub(tb.toString());
					break;
				case BP.DA.DataType.AppInt:
					// tb.ShowType = TBType.Num;
					tb.attributes
							.put("style",
									"height:"
											+ attr.getUIHeight()
											+ "px;"
											+ "width: "
											+ attr.GetValStrByKey("UIWidth")
											+ "px; text-align: right; word-break: keep-all;");
					tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));
					if (attr.getUIIsEnable() && !isReadonly) {
						// 增加验证
						tb.attributes.put("onkeyup",
								"Change('" + attr.getFK_MapData() + "');");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d]/g,'');TB_ClickNum(this,0);");
						tb.attributes.put("onClick", "TB_ClickNum(this)");
						tb.attributes.put("onkeydown", "VirtyNum(this)");
						tb.addAttr("OnKeyPress",
								"javascript:return  VirtyNum(this,'int');");
						tb.attributes.put("class", "TBNum");
					} else {
						tb.setReadOnly(true);
						// tb.attributes.put("class", "TBReadonly");
					}

					appendPub(tb.toString());
					break;
				case BP.DA.DataType.AppMoney:
					tb.attributes.put("style",
							"height:" + attr.getUIHeight() + "px;" + "width: "
									+ attr.GetValStrByKey("UIWidth")
									+ "px; text-align: right; ");

					if (attr.getUIIsEnable() && !isReadonly) {
						// 增加验证
						tb.attributes.put("onkeyup",
								"Change('" + attr.getFK_MapData() + "');");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,'0.00');");
						tb.attributes.put("onClick", "TB_ClickNum(this)");
						tb.attributes.put("onkeydown", "VirtyNum(this)");
						tb.addAttr("OnKeyPress",
								"javascript:return  VirtyNum(this,'float');");
						tb.attributes.put("class", "TBNum");
					} else {
						tb.setReadOnly(true);
						// tb.attributes.put("class", "TBReadonly");
					}

					if (SystemConfig.getAppSettings().get("IsEnableNull")
							.equals("1")) {
						BigDecimal v = HisEn
								.GetValMoneyByKey(attr.getKeyOfEn());
						if (v.equals(567567567)) {
							tb.setText("");
						} else {
							tb.setText(String.valueOf(v.doubleValue()));
						}
					} else {
						tb.setText(decimalFormat(HisEn.GetValMoneyByKey(attr
								.getKeyOfEn())));
					}

					appendPub(tb.toString());
					break;
				case BP.DA.DataType.AppRate:
					if (attr.getUIIsEnable() && !isReadonly) {
						tb.attributes.put("class", "TBNum");
					} else {
						tb.setReadOnly(true);
						// tb.attributes.put("class", "TBReadonly");
					}
					tb.setShowType(TBType.Moneny);
					tb.setText(decimalFormat(HisEn.GetValMoneyByKey(attr
							.getKeyOfEn())));
					tb.attributes.put("style",
							"height:" + attr.getUIHeight() + "px;" + "width: "
									+ attr.GetValStrByKey("UIWidth")
									+ "px; text-align: right; ");
					// 增加验证
					// tb.Attributes.Add("onkeyup",
					// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
					tb.attributes.put("onblur",
							"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
					appendPub(tb.toString());
					break;
				default:
					break;
				}
				break;
			case Enum:
				if (attr.getUIContralType().equals(UIContralType.DDL)) {
					DDL ddle = new DDL();
					ddle.setId("DDL_" + attr.getKeyOfEn());//ToLowerCase
					ddle.setName("DDL_" + attr.getKeyOfEn());//ToLowerCase
					ddle.BindSysEnum(attr.getUIBindKey());
					ddle.SetSelectItem(HisEn.GetValStrByKey(attr.getKeyOfEn()));
					ddle.setEnabled(attr.getUIIsEnable());
					ddle.attributes.put("tabindex",
							String.valueOf(attr.getIdx()));

					if (attr.getUIIsEnable()) {
						// add by dgq 2013-4-9,添加内容修改后的事件
						ddle.attributes.put("onchange",
								"Change('" + attr.getFK_MapData() + "')");
					}
					if (ddle.getEnabled() && isReadonly) {
						ddle.setEnabled(false);
					}
					appendPub(ddle.toString());
				} else {
					// BP.Sys.FrmRBs rbs = new FrmRBs();
					// rbs.Retrieve(FrmRBAttr.FK_MapData, enName,
					// FrmRBAttr.KeyOfEn, attr.KeyOfEn);
				}
				break;
			case FK:
				DDL ddl1 = new DDL();
				ddl1.setId("DDL_" + attr.getKeyOfEn());//ToLowerCase
				ddl1.setName("DDL_" + attr.getKeyOfEn());//ToLowerCase
				ddl1.attributes.put("tabindex", String.valueOf(attr.getIdx()));
				ddl1.setEnabled(attr.getUIIsEnable());
				if (ddl1.getEnabled()) {
					EntitiesNoName ens = attr.getHisEntitiesNoName();
					ens.RetrieveAll();
					ddl1.BindEntities(ens);

					ddl1.Items.add(new ListItem("请选择", ""));

					String val = HisEn.GetValStrByKey(attr.getKeyOfEn());
					if (StringHelper.isNullOrEmpty(val)) {
						ddl1.SetSelectItem("");
					} else {
						ddl1.SetSelectItem(val);
					}

					// add by dgq 2013-4-9,添加内容修改后的事件
					ddl1.attributes.put("onchange",
							"Change('" + attr.getFK_MapData() + "')");
				} else {
					// ddl1.Attributes["style"] = "width: " + attr.UIWidth +
					// "px;height: 19px;";
					if (ddl1.getEnabled() && isReadonly) {
						ddl1.setEnabled(false);
					}
					ddl1.attributes.put("style","width: "+String.valueOf(attr.getUIWidth())+"px;height: 19px;");
					ddl1.Items.add(new ListItem(HisEn.GetValRefTextByKey(attr
							.getKeyOfEn()), HisEn.GetValStrByKey(attr
							.getKeyOfEn())));
				}

				if (attr.getUIIsEnable() && getIsReadonly()) {
					ddl1.setEnabled(false);
				}
				appendPub(ddl1.toString());
				break;
			default:
				break;
			}
			// endregion add contrals.

			appendPub("</span>");
			appendPub("</DIV>");
		}
	}

	/**
	 * 输出图片
	 */
	private void printFrmImgs() {
		FrmImgs imgs = mapData.getFrmImgs();
		for (FrmImg img : imgs.ToJavaList()) {
			float y = img.getY();
			String imgSrc = "";
			// imgSrc = appPath + "DataUser/ICON/" +
			// BP.Sys.SystemConfig.CompanyID + "/LogBiger.png";
			// 图片类型
			if (img.getHisImgAppType().equals(ImgAppType.Img)) {
				// 数据来源为本地.
				if (img.getImgSrcType() == 0) {
					if (!img.getImgPath().contains(";")) {
						imgSrc = img.getImgPath();
					}
				}

				// 数据来源为指定路径.
				if (img.getImgSrcType() == 1) {
					// 图片路径不为默认值
					imgSrc = img.getImgURL();
					if (imgSrc.contains("@")) {
						// 如果有变量
						imgSrc = BP.WF.Glo.DealExp(imgSrc, HisEn, "");
					}
				}

				x = img.getX() + wtX;
				// 由于火狐 不支持onerror 所以 判断图片是否存在
				if (imgSrc.equals("")
						|| !new File(Glo.getIntallPath()
								+ imgSrc.replace("/", "\\")).exists())
					imgSrc = "/DataUser/ICON/CCFlow/LogBig.png";

				appendPub("<DIV id=" + img.getMyPK()
						+ " style='position:absolute;left:" + x + "px;top:" + y
						+ "px;text-align:left;vertical-align:top' >");

				// String img_src = "";
				// if(!StringHelper.isNullOrEmpty(imgSrc)){
				// img_src = basePath + imgSrc.replace("//", "/").substring(1,
				// imgSrc.length());
				// }
				if (!StringHelper.isNullOrEmpty(img.getLinkURL())) {
					appendPub("<a href='" + img.getLinkURL() + "' target="
							+ img.getLinkTarget() + " >");
					appendPub("<img src='"
							+ imgSrc
							+ "'  onerror=\"src='"
							+ basePath
							+ "DataUser/ICON/CCFlow/LogBig.png'\"  style='padding: 0px;margin: 0px;border-width: 0px;width:"
							+ img.getW() + "px;height:" + img.getH()
							+ "px;' />");
					appendPub("</a>");

				} else {
					appendPub("<img src='"
							+ imgSrc
							+ "'  onerror=\"src='"
							+ basePath
							+ "DataUser/ICON/CCFlow/LogBig.png'\"   style='padding: 0px;margin: 0px;border-width: 0px;width:"
							+ img.getW() + "px;height:" + img.getH()
							+ "px;' />");
				}
				appendPub("</DIV>");
				continue;
			}

			// 电子签章
			// 获取登录人岗位
			String stationNo = "";
			// 签章对应部门
			String fk_dept = WebUser.getFK_Dept();
			// 部门来源类别
			String sealType = "0";
			// 签章对应岗位
			String fk_station = img.getTag0();
			// 表单字段
			String sealField = "";
			String sql = "";
			// 如果设置了部门与岗位的集合进行拆分
			if (!StringHelper.isNullOrEmpty(img.getTag0())
					&& img.getTag0().contains("^")
					&& img.getTag0().split("[^]", -1).length == 4) {
				fk_dept = img.getTag0().split("[^]", -1)[0];
				fk_station = img.getTag0().split("[^]", -1)[1];
				sealType = img.getTag0().split("[^]", -1)[2];
				sealField = img.getTag0().split("[^]", -1)[3];
				// 如果部门没有设定，就获取部门来源
				// /if (fk_dept == "all")
				// {
				// 默认当前登录人
				fk_dept = WebUser.getFK_Dept();
				// 发起人
				if (sealType.equals("1")) {
					sql = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID="
							+ HisEn.GetValStrByKey("OID");
					fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
				}
				// 表单字段
				if (sealType.equals("2")
						&& !StringHelper.isNullOrEmpty(sealField)) {
					// 判断字段是否存在
					for (MapAttr attr : mattrs.ToJavaList()) {
						if (sealField.equals(attr.getKeyOfEn())) {
							fk_dept = HisEn.GetValStrByKey(sealField);
							break;
						}
					}
				}
				// /}
			}
			// 判断本部门下是否有此人
			// sql =
			// "SELECT fk_station from port_deptEmpStation where fk_dept='" +
			// fk_dept + "' and fk_emp='" + WebUser.getNo() + "'";
			sql = String
					.format(" select FK_Station from Port_DeptStation where FK_Dept ='%1$s' and FK_Station in (select FK_Station from Port_EmpStation where FK_Emp='%2$s')",
							fk_dept, WebUser.getNo());
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			ArrayList<DataRow> rows = dt.Rows;
			int size = rows.size();
			for (int i = 0; i < size; i++) {
				DataRow dr = rows.get(i);
				if (fk_station.contains(dr.getValue(0).toString() + ",")) {
					stationNo = dr.getValue(0).toString();
					break;
				}
			}
			// 重新加载 可能有缓存
			img.Retrieve("MyPk", img.getMyPK());
			// 0.不可以修改，从数据表中取，1可以修改，使用组合获取并保存数据
			if (img.getIsEdit() == 1 && !getIsReadonly()) {
				imgSrc = basePath + "DataUser/Seal/" + fk_dept + "_"
						+ stationNo + ".jpg";
				// 设置主键
				String myPK = StringHelper.isNullOrEmpty(img.getEnPK()) ? "seal"
						: img.getEnPK();
				myPK = myPK + "_" + HisEn.GetValStrByKey("OID") + "_"
						+ img.getMyPK();

				FrmEleDB imgDb = new FrmEleDB();
				QueryObject queryInfo = new QueryObject(imgDb);
				queryInfo.AddWhere(FrmEleAttr.MyPK, myPK);
				queryInfo.DoQuery();
				// 判断是否存在
				if (imgDb != null
						&& !StringHelper.isNullOrEmpty(imgDb.getFK_MapData())) {
					imgDb.setFK_MapData(StringHelper.isNullOrEmpty(img
							.getEnPK()) ? "seal" : img.getEnPK());
					imgDb.setEleID(HisEn.GetValStrByKey("OID"));
					imgDb.setRefPKVal(img.getMyPK());
					imgDb.setTag1(imgSrc);
					imgDb.Update();
				} else {
					imgDb.setFK_MapData(StringHelper.isNullOrEmpty(img
							.getEnPK()) ? "seal" : img.getEnPK());
					imgDb.setEleID(HisEn.GetValStrByKey("OID"));
					imgDb.setRefPKVal(img.getMyPK());
					imgDb.setTag1(imgSrc);
					imgDb.Insert();
				}
				// 添加控件
				x = img.getX() + wtX;
				appendPub("<DIV id=\"" + img.getMyPK()
						+ "\" style='position:absolute;left:" + x + "px;top:"
						+ y + "px;text-align:left;vertical-align:top' >");
				appendPub("<img src='"
						+ imgSrc
						+ "' onerror='javascript:src=\"DataUser/ICON/"
						+ SystemConfig.getCompanyID()
						+ "/LogBiger.png\";' style='padding: 0px;margin: 0px;border-width: 0px;width:"
						+ img.getW() + "px;height:" + img.getH() + "px' />");
				appendPub("</DIV>");

			} else {
				FrmEleDB realDB = null;
				FrmEleDB imgDb = new FrmEleDB();
				QueryObject objQuery = new QueryObject(imgDb);
				objQuery.AddWhere(FrmEleAttr.FK_MapData, img.getEnPK());
				objQuery.addAnd();
				objQuery.AddWhere(FrmEleAttr.EleID, HisEn.GetValStrByKey("OID"));
				objQuery.DoQuery();
				if (objQuery.GetCount() == 0) {
					FrmEleDBs imgdbs = new FrmEleDBs();
					QueryObject objQuerys = new QueryObject(imgdbs);
					objQuerys.AddWhere(FrmEleAttr.EleID,
							HisEn.GetValStrByKey("OID"));
					objQuerys.DoQuery();
					for (FrmEleDB single : imgdbs.ToJavaList()) {
						if (single
								.getFK_MapData()
								.substring(6, single.getFK_MapData().length())
								.equals(img.getEnPK().substring(6,
										img.getEnPK().length()))) {
							single.setFK_MapData(img.getEnPK());
							single.setMyPK(img.getEnPK() + "_"
									+ HisEn.GetValStrByKey("OID") + "_"
									+ img.getEnPK());
							single.setRefPKVal(img.getEnPK());
							single.DirectInsert();
							realDB = single;
							break;
						}
					}
				} else {
					realDB = imgDb;
				}
				imgSrc = realDB.getTag1();
				// 如果没有查到记录，控件不显示。说明没有走盖章的一步
				x = img.getX() + wtX;
				appendPub("<DIV id=\"" + img.getMyPK()
						+ "\" style='position:absolute;left:" + x + "px;top:"
						+ y + "px;text-align:left;vertical-align:top' >");

				appendPub("<img src='"
						+ imgSrc
						+ "' onerror='javascript:src=\"DataUser/ICON/"
						+ SystemConfig.getCompanyID()
						+ "/LogBiger.png\";' style='padding: 0px;margin: 0px;border-width: 0px;width:"
						+ img.getW() + "px;height:" + img.getH() + "px' />");

				appendPub("</DIV>");
			}
			// endregion
		}
	}

	/**
	 * 输出超链接
	 */
	private void printFrmLinks() {
		FrmLinks links = mapData.getFrmLinks();
		for (FrmLink link : links.ToJavaList()) {
			String url = link.getURL();
			if (url.contains("@")) {
				for (MapAttr attr : mattrs.ToJavaList()) {
					if (!url.contains("@")) {
						break;
					}
					url = url.replace("@" + attr.getKeyOfEn(),
							HisEn.GetValStrByKey(attr.getKeyOfEn()));
				}
			}
			x = link.getX() + wtX;
			appendPub("<DIV id=u2 style=\"position:absolute;left:" + x
					+ "px;top:" + link.getY() + "px;text-align:left;\">");
			appendPub("<span style=\"color:" + link.getFontColorHtml()
					+ ";font-family: " + link.getFontName() + ";font-size: "
					+ link.getFontSize() + "px\"> ");
			appendPub("<a href=" + url + " target=" + link.getTarget() + "><font color='#0000FF'>"
					+ link.getText() + "</font></a>");
			appendPub("</span></DIV>");
		}
	}

	/**
	 * 输出线
	 */
	private void printFrmLines() {
		FrmLines lines = mapData.getFrmLines();
		for (FrmLine line : lines.ToJavaList()) {
			if (line.getX1() == line.getX2()) {
				// 一道竖线
				float h = line.getY1() - line.getY2();
				h = Math.abs(h);
				if (line.getY1() < line.getY2()) {
					x = line.getX1() + wtX;

					appendPub("<img id=\"" + line.getMyPK()
							+ "\" style=\"padding:0px;position:absolute; left:"
							+ x + "px; top:" + line.getY1() + "px; width:"
							//+ line.getBorderWidth() + "px; height:" + h
							+ "1px; height:" + h
							+ "px;background-color:"
							+ line.getBorderColorHtml() + "\">");

				} else {
					x = line.getX2() + wtX;
					appendPub("<img id=\"" + line.getMyPK()
							+ "\" style=\"padding:0px;position:absolute; left:"
							+ x + "px; top:" + line.getY2() + "px; width:"
							//+ line.getBorderWidth() + "px; height:" + h
							+ "1px; height:" + h
							+ "px;background-color:"
							+ line.getBorderColorHtml() + "\">");

				}
			} else {
				// 一道横线
				float w = line.getX2() - line.getX1();

				if (line.getX1() < line.getX2()) {
					x = line.getX1() + wtX;
					appendPub("<img id=\"" + line.getMyPK()
							+ "\" style=\"padding:0px;position:absolute; left:"
							+ x + "px; top:" + line.getY1() + "px; width:" + w
//							+ "px; height:" + line.getBorderWidth()
//							+ "px;background-color:"
							+ "px; height:1px;background-color:"
							+ line.getBorderColorHtml() + "\">");

				} else {
					x = line.getX2() + wtX;
					appendPub("<img id=\"" + line.getMyPK()
							+ "\" style=\"padding:0px;position:absolute; left:"
							+ x + "px; top:" + line.getY2() + "px; width:" + w
//							+ "px; height:" + line.getBorderWidth()
//							+ "px;background-color:"
							+ "px; height:1px;background-color:"
							+ line.getBorderColorHtml() + "\">");

				}
			}
		}
	}

	private void printFrmLabs() {
		FrmLabs labs = mapData.getFrmLabs();
		float x1 = 0;
		for (FrmLab lab : labs.ToJavaList()) {
			x1 = lab.getX() + wtX;
			appendPub("<DIV id=u2 style='position:absolute;left:" + x1
					+ "px;top:" + lab.getY() + "px;text-align:left;'>");
			appendPub("<span style='color: " + lab.getFontColorHtml()
					+ "; font-family:" + lab.getFontName() + "; font-size: "
					+ lab.getFontSize() + "px;'>" + lab.getTextHtml() + "");
			appendPub("</span> </DIV>");
		}
	}

	/**
	 * 输出按钮
	 */
	private void printFrmBtns() {
		FrmBtns btns = mapData.getFrmBtns();
		for (FrmBtn btn : btns.ToJavaList()) {
			x = btn.getX() + wtX;
			appendPub("<DIV id='u2' style=\"position:absolute;left: " + x
					+ "px;top:" + btn.getY() + "px;text-align:left;\" ><span >");

			String doDoc = BP.WF.Glo
					.DealExp(btn.getEventContext(), HisEn, null);
			//Button button = new Button();
			//button.setType("button");
			//button.setCssClass("am-btn am-btn-primary am-btn-xs");
			//button.setText(btn.getText().replace("&nbsp;", " "));
			switch (btn.getHisBtnEventType()) {
			case Disable:
				//button.setEnabled(false);
				 appendPub("<input type='button' class='Btn' value='"+
				 btn.getText().replace("&nbsp;",
				 " ")+"' disabled='disabled'/>");
				break;
			case RunExe:
			case RunJS:
				//button.attributes.put("onclick", doDoc);
				appendPub("<input type='button' class='Btn' value='"+
				btn.getText().replace("&nbsp;",
				" ")+"' enable=true onclick=\""+ doDoc+"\" />");
				break;
			default:
				//button.setId(btn.getMyPK());
				// appendPub("<input id=\""+btn.getMyPK()+"\" class=\"Btn\" >"+btn.getText().replace("&nbsp;",
				// " ")+"</input>");

				Button myBtn = new Button();
				myBtn.setEnabled(true);
				myBtn.setCssClass("Btn");
				myBtn.setId(btn.getMyPK());
				myBtn.setText(btn.getText().replace("&nbsp;", " "));
				//myBtn.Click += new EventHandler(myBtn_Click);
				//myBtn.addAttr("onclick", "myBtn_Click()");
				//myBtn_Click(myBtn);
				appendPub(myBtn.toString());
				break;
			}
			//appendPub(myBtn.toString());
			appendPub("</span></DIV>");
		}
		// endregion
	}

	private void myBtn_Click(Object sender) {//, EventArgs e
		Button btn = (Button)((sender instanceof Button) ? sender : null);
		FrmBtn mybtn = new FrmBtn(btn.getId());
		String doc = mybtn.getEventContext().replace("~", "'");

		Attrs attrs = this.HisEn.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			doc = doc.replace("@" + attr.getKey(), this.HisEn.GetValStrByKey(attr.getKey()));
		}
		doc = doc.replace("@FK_Dept", WebUser.getFK_Dept());
		doc = doc.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		doc = doc.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		doc = doc.replace("WebUser.No", WebUser.getNo());
		doc = doc.replace("@WebUser.Name", WebUser.getName());
		doc = doc.replace("@MyPK", this.HisEn.getPKVal().toString());

		///#region 处理两个变量.
		String alertMsgErr = mybtn.getMsgErr();
		String alertMsgOK = mybtn.getMsgOK();
		if (alertMsgOK.contains("@"))
		{
			for (Attr attr : attrs)
			{
				alertMsgOK = alertMsgOK.replace("@" + attr.getKey(), this.HisEn.GetValStrByKey(attr.getKey()));
			}
		}

		if (alertMsgErr.contains("@"))
		{
			for (Attr attr : attrs)
			{
				alertMsgErr = alertMsgErr.replace("@" + attr.getKey(), this.HisEn.GetValStrByKey(attr.getKey()));
			}
		}
		///#endregion 处理两个变量.
		try
		{
			switch (mybtn.getHisBtnEventType())
			{
				case RunSQL:
					DBAccess.RunSQL(doc);
					this.Alert(alertMsgOK);
					return;
				case RunSP:
					DBAccess.RunSP(doc);
					this.Alert(alertMsgOK);
					return;
				case RunURL:
					doc = doc.replace("@AppPath", BP.WF.Glo.getCCFlowAppPath());
					String text = DataType.ReadURLContext(doc, 800);//, System.text.Encoding.UTF8
					if (text != null && text.substring(0, 7).contains("Err"))
					{
						throw new RuntimeException(text);
					}
					alertMsgOK += text;
					this.Alert(alertMsgOK);
					return;
				default:
					throw new RuntimeException("没有处理的执行类型:" + mybtn.getHisBtnEventType());
			}
		}
		catch (RuntimeException ex)
		{
			this.Alert(alertMsgErr + ex.getMessage());
		}
	}

	/**
	 * 输出事件
	 */
	private void printFrmEles() {
		FrmEles eles = mapData.getFrmEles();
		if (eles.size() >= 1) {
			String myjs = "\t\n<script type='text/javascript' >";
			myjs += "\t\n function BPPaint(ctrl,url,w,h,fk_FrmEle)";
			myjs += "\t\n {";
			myjs += "\t\n  var v= window.showModalDialog(url, 'ddf', 'dialogHeight: '+h+'px; dialogWidth: '+w+'px;center: yes; help: no'); ";
			myjs += "\t\n  if (v==null )  ";
			myjs += "\t\n     return ; ";

			// / myjs +=
			// "\t\n     alert(document.getElementById('Ele'+fk_FrmEle ));";
			// myjs += "\t\n  ctrl.src='dsdsd'; ";
			// myjs += "\t\n  alert('已经执行成功，谢谢使用')";
			// myjs += "\t\n  ctrl.src=v; ";
			myjs += "\t\n  ctrl.src=v+'?temp='+new Date(); ";

			// myjs += "\t\n  alert(ctrl.src)";
			// myjs += "\t\n  ctrl.setAttribute('src',v); ";
			// myjs +=
			// "\t\n  document.getElementById('Ele'+fk_FrmEle ).src=v; ";
			// myjs +=
			// "\t\n  document.getElementById('Ele'+fk_FrmEle ).setAttribute('src', v); ";
			myjs += "\t\n }";
			myjs += "\t\n</script>";
			scriptsBlock.append(myjs);

			FrmEleDBs dbs = new FrmEleDBs(FK_MapData, HisEn.getPKVal()
					.toString());
			for (FrmEle ele : eles.ToJavaList()) {
				float y = ele.getY();
				x = ele.getX() + wtX;
				appendPub("\t\n<DIV id=" + ele.getMyPK()
						+ " style='position:absolute;left:" + x + "px;top:" + y
						+ "px;text-align:left;vertical-align:top' >");
				if (ele.getEleType().equals(FrmEle.HandSiganture)) {

				} else if (ele.getEleType().equals(FrmEle.HandSiganture)) {
					Object tempVar = dbs.GetEntityByKey(FrmEleDBAttr.EleID,
							ele.getEleID());
					FrmEleDB db = (FrmEleDB) ((tempVar instanceof FrmEleDB) ? tempVar
							: null);
					String dbFile = basePath + "DataUser/BPPaint/Def.png";
					if (db != null) {
						dbFile = db.getTag1();
					}

					if (getIsReadonly() || !ele.getIsEnable()) {
						appendPub("\t\n<img src='"
								+ dbFile
								+ "' onerror=\"src='"
								+ basePath
								+ "DataUser/BPPaint/Def.png'\" style='padding: 0px;margin: 0px;border-width: 0px;width:"
								+ ele.getW() + "px;height:" + ele.getH()
								+ "px;' />");
					} else {
						String url = basePath + "WF/CCForm/BPPaint.jsp?W="
								+ ele.getHandSiganture_WinOpenW() + "&H="
								+ ele.getHandSiganture_WinOpenH() + "&MyPK="
								+ ele.getPKVal() + "&PKVal=" + HisEn.getPKVal();
						myjs = "javascript:BPPaint(this,'" + url + "','"
								+ ele.getHandSiganture_WinOpenW() + "','"
								+ ele.getHandSiganture_WinOpenH() + "','"
								+ ele.getMyPK() + "');";
						// string myjs = "javascript:window.open('" + appPath +
						// "WF/CCForm/BPPaint.jsp?PKVal=" + en.PKVal + "&MyPK="
						// + ele.MyPK + "&H=" + ele.HandSiganture_WinOpenH +
						// "&W=" + ele.HandSiganture_WinOpenW +
						// "', 'sdf', 'dialogHeight: " +
						// ele.HandSiganture_WinOpenH + "px; dialogWidth: " +
						// ele.HandSiganture_WinOpenW +
						// "px;center: yes; help: no');";
						appendPub("\t\n<img id='Ele"
								+ ele.getMyPK()
								+ "' onclick=\""
								+ myjs
								+ "\" onerror=\"src='"
								+ basePath
								+ "DataUser/BPPaint/Def.png'\" src='"
								+ dbFile
								+ "' style='padding: 0px;margin: 0px;border-width: 0px;width:"
								+ ele.getW() + "px;height:" + ele.getH()
								+ "px;' />");
					}
				} else if (ele.getEleType().equals(FrmEle.iFrame)) {
					String paras = paramsStr;
					if (!paras.contains("FID=")) {
						paras += "&FID=" + HisEn.GetValStrByKey("FID");
					}

					if (!paras.contains("WorkID=")) {
						paras += "&WorkID=" + HisEn.GetValStrByKey("OID");
					}

					Object tempVar2 = ele.getTag1();
					String src = (String) ((tempVar2 instanceof String) ? tempVar2
							: null); // url
					if (src.contains("?")) {
						src += "&r=q" + paras;
					} else {
						src += "?r=q" + paras;
					}

					if (!src.contains("UserNo")) {
						src += "&UserNo=" + WebUser.getNo();
					}
					if (!src.contains("SID")) {
						src += "&SID=" + WebUser.getSID();
					}
					if (src.contains("@")) {
						for (Attr m : HisEn.getEnMap().getAttrs()) {
							if (!src.contains("@")) {
								break;
							}
							src = src.replace("@" + m.getKey(),
									HisEn.GetValStrByKey(m.getKey()));
						}
					}
					appendPub("<iframe ID='F"
							+ ele.getEleID()
							+ "'   src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ ele.getW() + "' height='" + ele.getH()
							+ "' scrolling=auto /></iframe>");

				} else {
					appendPub("未处理");
				}
			}
			appendPub("\t\n</DIV>");
		}
		// endregion 输出Ele
	}

	/**
	 * 输出事件
	 */
	private void printFrmEles2() {
		///#region 输出Ele
		FrmEles eles = this.mapData.getFrmEles();
		if (eles.size() >= 1)
		{
			String myjs = "\t\n<script type='text/javascript' >";
			myjs += "\t\n function BPPaint(ctrl,url,w,h,fk_FrmEle)";
			myjs += "\t\n {";
			myjs += "\t\n  var v= window.showModalDialog(url, 'ddf', 'dialogHeight: '+h+'px; dialogWidth: '+w+'px;center: yes; help: no'); ";
			myjs += "\t\n  if (v==null )  ";
			myjs += "\t\n     return ; ";
			myjs += "\t\n  ctrl.src=v+'?temp='+new Date(); ";
			myjs += "\t\n }";
			myjs += "\t\n</script>";
			appendPub(myjs);

			FrmEleDBs dbs = new FrmEleDBs(FK_MapData, this.HisEn.getPKVal()+"");
			for (FrmEle ele : eles.ToJavaList())
			{
				float y = ele.getY();
				x = ele.getX() + wtX;
				appendPub("\t\n<DIV id=" + ele.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
				
				String eleType = ele.getEleType();
				String paras = "";
				String src = "";
				if (eleType.equals(FrmEle.HandSiganture))
				{
					Object tempVar = dbs.GetEntityByKey(FrmEleDBAttr.EleID, ele.getEleID());
					FrmEleDB db = (FrmEleDB)((tempVar instanceof FrmEleDB) ? tempVar : null);
					String dbFile = basePath + "DataUser/BPPaint/Def.png";
					if (db != null)
					{
						dbFile = db.getTag1();
					}

					if (this.getIsReadonly() || ele.getIsEnable() == false)
					{
						appendPub("\t\n<img src='" + dbFile + "' onerror=\"this.src='" + basePath + "DataUser/BPPaint/Def.png'\" style='padding: 0px;margin: 0px;border-width: 0px;width:" + ele.getW() + "px;height:" + ele.getH() + "px;' />");
					}
					else
					{
						String url = basePath + "WF/CCForm/BPPaint.jsp?W=" + ele.getHandSiganture_WinOpenW() + "&H=" + ele.getHandSiganture_WinOpenH() + "&MyPK=" + ele.getPKVal() + "&PKVal=" + HisEn.getPKVal();
						myjs = "javascript:BPPaint(this,'" + url + "','" + ele.getHandSiganture_WinOpenW() + "','" + ele.getHandSiganture_WinOpenH() + "','" + ele.getMyPK() + "');";
						//string myjs = "javascript:window.open('" + appPath + "WF/CCForm/BPPaint.jsp?PKVal=" + en.PKVal + "&MyPK=" + ele.MyPK + "&H=" + ele.HandSiganture_WinOpenH + "&W=" + ele.HandSiganture_WinOpenW + "', 'sdf', 'dialogHeight: " + ele.HandSiganture_WinOpenH + "px; dialogWidth: " + ele.HandSiganture_WinOpenW + "px;center: yes; help: no');";
						appendPub("\t\n<img id='Ele" + ele.getMyPK() + "' onclick=\"" + myjs + "\" onerror=\"this.src='" + basePath + "DataUser/BPPaint/Def.png'\" src='" + dbFile + "' style='padding: 0px;margin: 0px;border-width: 0px;width:" + ele.getW() + "px;height:" + ele.getH() + "px;' />");
					}
				} else if (eleType.equals(FrmEle.iFrame))
				{
					paras = paramsStr;
					if (paras.contains("FID=") == false && this.HisEn.GetValStrByKey("FID")!=null)
					{
						paras += "&FID=" + this.HisEn.GetValStrByKey("FID");
					}

					if (paras.contains("WorkID=") == false && this.HisEn.GetValStrByKey("OID")!=null)
					{
						paras += "&WorkID=" + this.HisEn.GetValStrByKey("OID");
					}

					Object tempVar2 = ele.getTag1();
					src = (String)((tempVar2 instanceof String) ? tempVar2 : null); // url
					if (src.contains("?"))
					{
						src += "&r=q" + paras;
					}
					else
					{
						src += "?r=q" + paras;
					}

					if (src.contains("UserNo") == false)
					{
						src += "&UserNo=" + WebUser.getNo();
					}
					if (src.contains("SID") == false)
					{
						src += "&SID=" + WebUser.getSID();
					}
					if (src.contains("@"))
					{
						for (Attr m : HisEn.getEnMap().getAttrs())
						{
							if (src.contains("@") == false)
							{
								break;
							}
							src = src.replace("@" + m.getKey(), HisEn.GetValStrByKey(m.getKey()));
						}
					}

					if (this.getIsReadonly() == true)
					{
						appendPub("<iframe ID='F" + ele.getEleID() + "'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + ele.getW() + "' height='" + ele.getH() + "' scrolling=auto /></iframe>");
					}
					else
					{
						AddLoadFunction(ele.getEleID(), "blur", "SaveDtl");
						appendPub("<iframe ID='F" + ele.getEleID() + "' onload= '" + ele.getEleID() + "load();'  src='" + src + "' frameborder=0  style='position:absolute;width:" + ele.getW() + "px; height:" + ele.getH() + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
					}
				} else if (eleType.equals(FrmEle.EleSiganture))
				{
					appendPub("未处理");
				} else if (eleType.equals(FrmEle.SubThread))
				{
					paras = paramsStr;
					if (paras.contains("FID=") == false && this.HisEn.GetValStrByKey("FID")!=null)
					{
						paras += "&FID=" + this.HisEn.GetValStrByKey("FID");
					}

					if (paras.contains("WorkID=") == false && this.HisEn.GetValStrByKey("OID")!=null)
					{
						paras += "&WorkID=" + this.HisEn.GetValStrByKey("OID");
					}

					src = "/WF/WorkOpt/ThreadDtl.jsp?1=2" + paras;
					if (src.contains("UserNo") == false)
					{
						src += "&UserNo=" + WebUser.getNo();
					}
					if (src.contains("SID") == false)
					{
						src += "&SID=" + WebUser.getSID();
					}
					if (src.contains("@"))
					{
						for (Attr m : HisEn.getEnMap().getAttrs())
						{
							if (src.contains("@") == false)
							{
								break;
							}
							src = src.replace("@" + m.getKey(), HisEn.GetValStrByKey(m.getKey()));
						}
					}
					appendPub("<iframe ID='F" + ele.getEleID() + "'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + ele.getW() + "' height='" + ele.getH() + "' scrolling=auto /></iframe>");
					
				}
				appendPub("\t\n</DIV>");
			}
		}
	}
	
	public void BindColumn2(Entity en, String enName) {

		ctrlUseSta = "";
		this.enName = enName;
		HisEn = en;
		mapData = new MapData(enName);
		currGF = new GroupField();
		MapAttrs mattrs = mapData.getMapAttrs();
		gfs = mapData.getGroupFields();
		dtls = mapData.getMapDtls();
		frames = mapData.getMapFrames();
		m2ms = mapData.getMapM2Ms();
		aths = mapData.getFrmAttachments();
		mes = mapData.getMapExts();

		// /#region 处理事件.
		fes = mapData.getFrmEvents();
		if (!IsPostBack) {
			try {
				String msg = fes.DoEventNode(FrmEventList.FrmLoadBefore, en);
				if (!StringHelper.isNullOrEmpty(msg)) {
					// Alert(msg);
				}
			} catch (RuntimeException ex) {
				// Alert(ex.getMessage());
				return;
			}
		}
		// endregion 处理事件.

		// 处理默认值.
		DealDefVal(mattrs);
		// 处理装载前填充.
		LoadData(mattrs, en);

		appendPub("<table width=100% >");
		for (GroupField gf : gfs.ToJavaList()) {
			currGF = gf;
			appendPub(AddTR());
			appendPub(AddTD(
					"colspan=2 class=GroupField valign='top' align=left ",
					"<div style='text-align:left; float:left'>&nbsp;"
							+ gf.getLab()
							+ "</div><div style='text-align:right; float:right'></div>"));
			appendPub(AddTREnd());

			// int idx = -1;
			// isLeftNext = true;
			rowIdx = 0;

			// region 增加字段.
			for (MapAttr attr : mattrs.ToJavaList()) {
				// /#region 排除
				if (attr.getGroupID() != gf.getOID()) {
					if (gf.getIdx() == 0 && attr.getGroupID() == 0) {
					} else {
						continue;
					}
				}
				if (attr.getHisAttr().getIsRefAttr() || !attr.getUIVisible()) {
					continue;
				}
				// endregion 排除

				// region 设置
				rowIdx++;
				appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "'"));
				if (!attr.getUIIsEnable()) {
					if (LinkFields.contains("," + attr.getKeyOfEn() + ",")) {
						Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType,
								MapExtXmlList.Link);
						MapExt meLink = (MapExt) ((tempVar instanceof MapExt) ? tempVar
								: null);
						String url = meLink.getTag();
						if (!url.contains("?")) {
							url = url + "?a3=2";
						}
						url = url + "&WebUserNo=" + WebUser.getNo() + "&SID="
								+ WebUser.getSID() + "&EnName=" + enName;
						if (url.contains("@AppPath")) {
							url = url.replace("@AppPath", basePath);
						}
						if (url.contains("@")) {
							Attrs attrs = en.getEnMap().getAttrs();
							int size_attrs = attrs.size();
							for (int i = 0; i < size_attrs; i++) {
								url = url.replace("@" + attr.getKeyOfEn(),
										en.GetValStrByKey(attr.getKeyOfEn()));
								if (!url.contains("@")) {
									break;
								}
							}
						}
						appendPub(AddTD("<a href='" + url + "' target='"
								+ meLink.getTag1() + "' >"
								+ en.GetValByKey(attr.getKeyOfEn()) + "</a>"));
						appendPub(AddTREnd());
						continue;
					}
				}
				// endregion 设置

				// region 加入字段
				// 显示的顺序号.
				// idx++;
				if (attr.getIsBigDoc() && attr.getUIIsLine()) {
					if (attr.getUIIsEnable()) {
						appendPub("<TD colspan=2 height='" + attr.getUIHeight()
								+ "px'    width='100%' valign=top align=left>"
								+ attr.getName() + "<br>");
					} else {
						appendPub("<TD colspan=2 height='"
								+ attr.getUIHeight()
								+ "px'   width='100%' valign=top class=TBReadonly>"
								+ attr.getName() + "<br>");
					}

					TextBox mytbLine = new TextBox();
					if (attr.getIsBigDoc()) {
						mytbLine.setTextMode(TextBoxMode.MultiLine);
						mytbLine.attributes.put("class", "TBDoc");
						;
					}

					mytbLine.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
					mytbLine.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
					if (attr.getIsBigDoc()) {
						// mytbLine = 5;
						// mytbLine.Columns = 30;
					}

					mytbLine.attributes.put("style",
							"width:98%;height:100%;padding: 0px;margin: 0px;");
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					mytbLine.setEnabled(attr.getUIIsEnable());

					appendPub(mytbLine.toString());
					appendPub(AddTDEnd());
					appendPub(AddTREnd());
					rowIdx++;
					continue;
				}
				TextBox tb = new TextBox();
				tb.attributes.put("width", "100%");
				tb.attributes.put("border", "1px");
				tb.setCols(40);
				tb.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
				tb.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
				BaseWebControl ctl = tb;

				// region add contrals.
				switch (attr.getLGType()) {
				case Normal:
					tb.setEnabled(attr.getUIIsEnable());
					switch (attr.getMyDataType()) {
					case BP.DA.DataType.AppString:
						tb.setShowType(TBType.TB);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						break;
					case BP.DA.DataType.AppDate:
						tb.setShowType(TBType.Date);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable()) {
							tb.attributes.put("onfocus", "WdatePicker();");
						}
						break;
					case BP.DA.DataType.AppDateTime:
						tb.setShowType(TBType.DateTime);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable()) {
							tb.attributes
									.put("onfocus",
											"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
						}
						break;
					case BP.DA.DataType.AppBoolean:
						CheckBox cb = new CheckBox();
						cb.setText(attr.getName().trim());
						cb.setId("CB_" + attr.getKeyOfEn());//ToLowerCase
						cb.setName("CB_" + attr.getKeyOfEn());//ToLowerCase
						cb.setChecked(attr.getDefValOfBool());
						cb.setEnabled(attr.getUIIsEnable());
						cb.setChecked(en.GetValBooleanByKey(attr.getKeyOfEn()));
						appendPub(AddTD("colspan=2", cb.toString()));
						continue;
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppFloat:
						tb.setShowType(TBType.Num);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");

						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
						break;
					case BP.DA.DataType.AppInt:
						tb.setShowType(TBType.Num);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d]/g,'')");
						tb.attributes.put("onblur",
								"value=value.replace(/[^-?\\d]/g,'')");
						break;
					case BP.DA.DataType.AppMoney:
					case BP.DA.DataType.AppRate:
						tb.setShowType(TBType.Moneny);

						tb.setText(decimalFormat(en.GetValStrByKey(attr
								.getKeyOfEn())));
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
						break;
					default:
						break;
					}
					switch (attr.getMyDataType()) {
					case BP.DA.DataType.AppString:
					case BP.DA.DataType.AppDateTime:
					case BP.DA.DataType.AppDate:
						if (tb.getEnabled()) {
							tb.attributes.put("class", "TB");
						} else {
							tb.setReadOnly(true);
							// tb.attributes.put("class", "TBReadonly");
						}
						break;
					default:
						if (tb.getEnabled()) {
							tb.attributes.put("class", "TBNum");
						} else {
							tb.setReadOnly(true);
							// tb.attributes.put("class", "TBReadonly");
						}
						break;
					}
					break;
				case Enum:
					DDL ddle = new DDL();
					ddle.setId("DDL_" + attr.getKeyOfEn());//ToLowerCase
					ddle.setName("DDL_" + attr.getKeyOfEn());//ToLowerCase
					ddle.BindSysEnum(attr.getUIBindKey());
					ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					ddle.setEnabled(attr.getUIIsEnable());
					ctl = ddle;
					break;
				case FK:
					DDL ddl1 = new DDL();
					ddl1.setId("DDL_" + attr.getKeyOfEn());//ToLowerCase
					ddl1.setName("DDL_" + attr.getKeyOfEn());//ToLowerCase
					try {
						EntitiesNoName ens = attr.getHisEntitiesNoName();
						ens.RetrieveAll();
						ddl1.BindEntities(ens);
						ddl1.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					} catch (java.lang.Exception e) {

					}
					ddl1.setEnabled(attr.getUIIsEnable());
					ctl = ddl1;
					break;
				default:
					break;
				}
				// endregion add contrals.

				String desc = attr.getName().replace("：", "");
				desc = desc.replace(":", "");
				desc = desc.replace(" ", "");

				if (desc.length() >= 5) {
					appendPub("<TD colspan=2 class=FDesc width='100%' ><div style='float:left'>"
							+ desc + "</div><br>");
					appendPub(ctl.toString());
					appendPub(AddTREnd());
				} else {
					appendPub(AddTDDesc(desc));
					ctl.setReadOnly(true);
					appendPub(AddTD("width='100%' class=TBReadonly",
							ctl.toString()));
					appendPub(AddTREnd());
				}
				// endregion 加入字段
			}
			// endregion 增加字段.

			// 插入col.
			String fid = "0";
			try {
				fid = en.GetValStrByKey("FID");
			} catch (java.lang.Exception e2) {
			}
			InsertObjects2Col(true, en.getPKVal().toString(), fid);
		}
		appendPub(AddTableEnd());

		// region 处理iFrom 的自适应的问题。
		String js = "\t\n<script type='text/javascript' >";
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (!dtl.getIsView()) {
				continue;
			}

			js += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.getNo()
					+ "','TD" + dtl.getNo() + "')\", 200);";
		}
		 
		for (MapM2M m2m : m2ms.ToJavaList()) {
			// if (m2m.ShowWay == FrmShowWay.FrmAutoSize)
			js += "\t\n window.setInterval(\"ReinitIframe('F"
					+ m2m.getNoOfObj() + "','TD" + m2m.getNoOfObj()
					+ "')\", 200);";
		}
		for (FrmAttachment ath : aths.ToJavaList()) {
			// if (ath.IsAutoSize)
			js += "\t\n window.setInterval(\"ReinitIframe('F" + ath.getMyPK()
					+ "','TD" + ath.getMyPK() + "')\", 200);";
		}
		js += "\t\n</script>";
		scriptsBlock.append(js);
		// endregion 处理iFrom 的自适应的问题。

		// 处理扩展。
		AfterBindEn_DealMapExt(enName, mattrs, en);
		if (!getIsReadonly()) {
			// region 处理iFrom SaveDtlData。
			js = "\t\n<script type='text/javascript' >";
			js += "\t\n function SaveDtl(dtl) { ";
			// js += "\t\n    GenerPageKVs(); //调用产生kvs ";
			js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveDtlData(); ";
			js += "\t\n } ";
			js += "\t\n</script>";
			scriptsBlock.append(js);
			// endregion 处理iFrom SaveDtlData。

			// region 处理iFrom SaveM2M Save
			js = "\t\n<script type='text/javascript' >";
			js += "\t\n function SaveM2M(dtl) { ";
			js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveM2M();";
			js += "\t\n } ";
			js += "\t\n</script>";
			scriptsBlock.append(js);
			// endregion 处理iFrom SaveM2M Save。
		}

	}

	public String _tempAddDtls = "";

	public final void InsertObjects2Col(boolean isJudgeRowIdx, String pk,
			String fid) {

		// region 从表
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (!dtl.getIsView()) {
				continue;
			}

			if (_tempAddDtls.contains(dtl.getNo())) {
				continue;
			}

			// if (dtl.IsUse)
			// continue;

			// if (isJudgeRowIdx)
			// {
			// if (dtl.RowIdx != rowIdx)
			// continue;
			// }

			if (dtl.getGroupID() != currGF.getOID()) {
				continue;
			}

			if (dtl.getGroupID() == 0 && rowIdx == 0) {
				dtl.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				dtl.setRowIdx(0);
				dtl.Update();
			}

			dtl.IsUse = true;
			rowIdx++;

			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			appendPub("<TD colspan=2 ID='TD" + dtl.getNo()
					+ "' height='100px' width='100%' style='align:left'>");
			String src = "";
			try {
				src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + dtl.getNo()
						+ "&RefPKVal=" + HisEn.getPKVal() + "&FID="
						+ HisEn.GetValStringByKey("FID") + "&IsWap=0&FK_Node="
						+ dtl.getFK_MapData().replace("ND", "");
			} catch (java.lang.Exception e) {
				src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + dtl.getNo()
						+ "&RefPKVal=" + HisEn.getPKVal() + "&IsWap=0&FK_Node="
						+ dtl.getFK_MapData().replace("ND", "");
			}

			if (getIsReadonly() || dtl.getIsReadonly()) {
				appendPub("<iframe ID='F"
						+ dtl.getNo()
						+ "'  src='"
						+ src
						+ "&IsReadonly=1' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='100px' />");
			} else {
				AddLoadFunction(dtl.getNo(), "blur", "SaveDtl");
				// Add("<iframe ID='F" + dtl.No + "'   Onblur=\"SaveDtl('" +
				// dtl.No + "');\"  src='" + src +
				// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='100px' />");

				appendPub("<iframe ID='F"
						+ dtl.getNo()
						+ "'  onload='"
						+ dtl.getNo()
						+ "load();'    src='"
						+ src
						+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='100px' />");

			}

			appendPub(AddTDEnd());
			appendPub(AddTREnd());
			_tempAddDtls += dtl.getNo();

			// 下面使用Link 的方案.
			// // myidx++;
			// AddTR(" ID='" + currGF.Idx + "_" + rowIdx + "' ");
			// string src = "";
			// try
			// {
			// src = "/WF/CCForm/Dtl2.jsp?EnsName=" + dtl.No + "&RefPKVal=" +
			// HisEn.PKVal + "&FID=" + HisEn.GetValStringByKey("FID") +
			// "&IsWap=1&FK_Node=" + dtl.FK_MapData.Replace("ND", "");
			// }
			// catch
			// {
			// src = "/WF/CCForm/Dtl2.jsp?EnsName=" + dtl.No + "&RefPKVal=" +
			// HisEn.PKVal + "&IsWap=1&FK_Node=" + dtl.FK_MapData.Replace("ND",
			// "");
			// }
			// _tempAddDtls += dtl.No;
			// Add("<TD colspan=2 class=FDesc ID='TD" + dtl.No + "'><a href='" +
			// src + "'>" + dtl.Name + "</a></TD>");
			// // Add("<iframe ID='F" + dtl.No +
			// "' frameborder=0 Onblur=\"SaveDtl('" + dtl.No +
			// "');\" style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='"
			// + src + "' height='10px' scrolling=no  /></iframe>");
			// //AddTDEnd();
			// AddTREnd();
		}
		// endregion 从表

		// region 框架表
		for (MapFrame fram : frames.ToJavaList()) {
			if (fram.IsUse) {
				continue;
			}

			if (isJudgeRowIdx) {
				if (fram.getRowIdx() != rowIdx) {
					continue;
				}
			}

			 
			fram.IsUse = true;
			rowIdx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			String src = fram.getURL();

			if (src.contains("?")) {
				src += "&Table=" + fram.getFK_MapData() + "&WorkID=" + pk
						+ "&FID=" + fid;
			} else {
				src += "?Table=" + fram.getFK_MapData() + "&WorkID=" + pk
						+ "&FID=" + fid;
			}
			appendPub("<TD colspan=2 class=FDesc ID='TD" + fram.getNoOfObj()
					+ "'><a href='" + src + "'>" + fram.getName() + "</a></TD>");
			appendPub(AddTREnd());
		}
		// /#endregion 从表

		// /#region 附件
		for (FrmAttachment ath : aths.ToJavaList()) {
			if (ath.IsUse) {
				continue;
			}
			if (isJudgeRowIdx) {
				if (ath.getRowIdx() != rowIdx) {
					continue;
				}
			}

			if (ath.getGroupID() == 0 && rowIdx == 0) {
				ath.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				ath.setRowIdx(0);
				ath.Update();
			} else if (ath.getGroupID() == currGF.getOID()) {

			} else {
				continue;
			}
			ath.IsUse = true;
			rowIdx++;

			String src = basePath
					+ "WF/CCForm/AttachmentUpload.jsp?IsWap=1&PKVal="
					+ HisEn.getPKVal() + "&NoOfObj=" + ath.getNoOfObj()
					+ "&FK_MapData=" + ensName + "&FK_FrmAttachment="
					+ ath.getMyPK() + paramsStr;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			appendPub("<TD colspan=2 class=FDesc ID='TD" + ath.getNoOfObj()
					+ "'><a href='" + src + "'>" + ath.getName() + "</a></TD>");
			appendPub(AddTREnd());
		}
		// endregion 附件

		// region 多对多的关系
		for (MapM2M m2m : m2ms.ToJavaList()) {
			if (m2m.IsUse) {
				continue;
			}

			if (isJudgeRowIdx) {
				if (m2m.getRowIdx() != rowIdx) {
					continue;
				}
			}

			if (m2m.getGroupID() == 0 && rowIdx == 0) {
				m2m.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				m2m.setRowIdx(0);
				m2m.Update();
			} else if (m2m.getGroupID() == currGF.getOID()) {

			} else {
				continue;
			}
			m2m.IsUse = true;
			rowIdx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			if (m2m.getShowWay().equals(FrmShowWay.FrmAutoSize)) {
				appendPub("<TD colspan=4 ID='TD" + m2m.getNoOfObj()
						+ "' height='50px' width='100%'  >");
			} else {
				appendPub("<TD colspan=4 ID='TD" + m2m.getNoOfObj()
						+ "' height='" + m2m.getH() + "' width='" + m2m.getW()
						+ "'  >");
			}

			String src = "";
			if (m2m.getHisM2MType().equals(M2MType.M2M)) {
				src = basePath + "WF/CCForm/M2M.jsp?NoOfObj="
						+ m2m.getNoOfObj();
			} else {
				src = basePath + "WF/CCForm/M2MM.jsp?NoOfObj="
						+ m2m.getNoOfObj();
			}

			String paras = paramsStr;

			if (!paras.contains("FID=")) {
				paras += "&FID=" + HisEn.GetValStrByKey("FID");
			}

			if (!paras.contains("OID=")) {
				paras += "&OID=" + HisEn.GetValStrByKey("OID");
			}

			src += "&r=q" + paras;

			if (!src.contains("FK_MapData")) {
				src += "&FK_MapData=" + m2m.getFK_MapData();
			}

			switch (m2m.getShowWay()) {
			case FrmAutoSize:
				if (m2m.getIsEdit()) {
					AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");
					// Add("<iframe ID='F" + m2m.NoOfObj +
					// "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" +
					// src +
					// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");

					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "' onload='"
							+ m2m.getNoOfObj()
							+ "load();'    src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");

				} else {
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'   src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");
				}
				break;
			case FrmSpecSize:
				if (m2m.getIsEdit()) {
					// Add("<iframe ID='F" + m2m.NoOfObj +
					// "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" +
					// src +
					// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
					// + m2m.W + "' height='" + m2m.H +
					// "' scrolling=auto /></iframe>");
					AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'   onload='"
							+ m2m.getNoOfObj()
							+ "load();' src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ m2m.getW() + "' height='" + m2m.getH()
							+ "' scrolling=auto /></iframe>");

				} else {
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'    src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ m2m.getW() + "' height='" + m2m.getH()
							+ "' scrolling=auto /></iframe>");
				}
				break;
			case Hidden:
				break;
			case WinOpen:
				appendPub("<a href=\"javascript:WinOpen('" + src
						+ "&IsOpen=1','" + m2m.getW() + "','" + m2m.getH()
						+ "');\"  />" + m2m.getName() + "</a>");
				break;
			default:
				break;
			}
		}
		// /#endregion 多对多的关系

	}

	/*
	 * 加载界面
	 */
	public void loadMyFlowEn() {
//		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		// 当前节点
		Node currND = new Node(getFK_Node());
		// 当前工作
		Work en = (Work) ContextHolderUtils.getRequest().getAttribute("Work");
		if (en == null) {
			return;
		}
		// 绑定表单
//		BindCCForm(en, currND.getNodeFrmID(), false, dimension.width);
		BindCCForm(en, currND.getNodeFrmID(), false, 900,true);
	}

	private void AddLoadFunction(String id, String eventName, String method) {
		String js = "";
		js = "\t\n<script type='text/javascript' >";
		js += "\t\n function " + id + "load() { ";
		js += "\t\n   if (document.all) {";
		js += "\t\n     document.getElementById('F" + id + "').attachEvent('on"
				+ eventName + "',function(event){" + method + "('" + id
				+ "');});";
		js += "\t\n } ";

		js += "\t\n else { ";
		js += "\t\n  document.getElementById('F" + id
				+ "').contentWindow.addEventListener('" + eventName
				+ "',function(event){" + method + "('" + id + "');}, false); ";
		js += "\t\n } }";

		js += "\t\n</script>";
		scriptsBlock.append(js);
	}

	// public boolean getIsReadonly(){
	// return isReadonly;
	// }
	//
	// public void setIsReadonly(boolean isReadonly){
	// this.isReadonly = isReadonly;
	// }

	/**
	 * 处理它的默认值.
	 * 
	 * @param mattrs
	 */
	private void DealDefVal(MapAttrs mattrs) {
		if (getIsReadonly()) {
			return;
		}
		scripts.add("DataUser/JSLibData/" + enName + "_Self.js");
		scripts.add("DataUser/JSLibData/" + enName + ".js");
		// Page.RegisterClientScriptBlock("y7",
		// "<script language='JavaScript' src='" + basePath +
		// "DataUser/JSLibData/" + EnName + "_Self.js' ></script>");

		// Page.RegisterClientScriptBlock("yfd7",
		// "<script language='JavaScript' src='" + basePath +
		// "DataUser/JSLibData/" + EnName + ".js' ></script>");

		for (MapAttr attr : mattrs.ToJavaList()) {
			if (!attr.getDefValReal().contains("@")) {
				continue;
			}

			HisEn.SetValByKey(attr.getKeyOfEn(), attr.getDefVal());
		}
	}

	public boolean IsLoadData = false;

	private void LoadData(MapAttrs mattrs, Entity en) {
		LinkFields = "";
		if (mes.size() == 0) {
			return;
		}
		for (MapExt myitem : mes.ToJavaList()) {
			if (myitem.getExtType().equals(MapExtXmlList.Link)) {
				LinkFields += "," + myitem.getAttrOfOper() + ",";//ToLowerCase
			}
		}

		if (!IsLoadData) {
			return;
		}

		/*Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType.toLowerCase(),
		MapExtXmlList.PageLoadFull);*/
		Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType.toLowerCase(),
		MapExtXmlList.TBFullCtrl);
		MapExt item = (MapExt) ((tempVar instanceof MapExt) ? tempVar : null);
		if (item == null) {
			return;
		}

		DataTable dt = null;
		String sql = item.getTag();
		if (StringHelper.isNullOrEmpty(sql)==false) {
			/*// 如果有填充主表的sql
			// region 处理sql变量
			sql = sql.replace("WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
			for (MapAttr attr : mattrs.ToJavaList()) {
				if (sql.contains("@")) {
					sql = sql.replace("@" + attr.getKeyOfEn(),
							en.GetValStrByKey(attr.getKeyOfEn()));
				} else {
					break;
				}
			}
			// endregion 处理sql变量

			if (!StringHelper.isNullOrEmpty(sql)) {
				if (sql.contains("@")) {
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1) {
					DataRow dr = dt.Rows.get(0);
					for (DataColumn dc : dt.Columns) {
						en.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName)
								.toString());
					}
				}
			}*/
			// 如果有填充主表的sql  
			sql = Glo.DealExp(sql, en, null);

			if (StringHelper.isNullOrEmpty(sql) == false)
			{
				if (sql.contains("@"))
				{
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1)
				{
					DataRow dr = dt.Rows.get(0);
					for (DataColumn dc : dt.Columns)
					{
						//去掉一些不需要copy的字段.
						String columnName = dc.ColumnName;
						if (columnName.equals(WorkAttr.OID) || columnName.equals(WorkAttr.FID) || columnName.equals(WorkAttr.Rec) || columnName.equals(WorkAttr.MD5) || columnName.equals(WorkAttr.MyNum) || columnName.equals(WorkAttr.RDT) || columnName.equals("RefPK") || columnName.equals(WorkAttr.RecText))
						{
							continue;
						}

						if (StringHelper.isNullOrEmpty(en.GetValStringByKey(dc.ColumnName)) || en.GetValStringByKey(dc.ColumnName).equals("0"))
						{
							if(dr.get(dc.ColumnName)==null){
								en.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName));
							}else{
								en.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
							}
						}
					}
				}
			}
		}

		if (StringHelper.isNullOrEmpty(item.getTag1())
				|| item.getTag1().length() < 15) {
			return;
		}

		// 填充从表.
		for (MapDtl dtl : dtls.ToJavaList()) {
			String[] sqls = item.getTag1().split("[*]", -1);
			for (String mysql : sqls) {
				if (StringHelper.isNullOrEmpty(mysql)) {
					continue;
				}

				if (!mysql.contains(dtl.getNo() + "=")) {
					continue;
				}

				// region 处理sql.
				sql = mysql;
				sql = sql.replace(dtl.getNo() + "=", "");
				sql = sql.replace("WebUser.No", WebUser.getNo());
				sql = sql.replace("@WebUser.Name", WebUser.getName());
				sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				sql = sql.replace("@WebUser.FK_DeptName",
						WebUser.getFK_DeptName());
				for (MapAttr attr : mattrs.ToJavaList()) {
					if (sql.contains("@")) {
						sql = sql.replace("@" + attr.getKeyOfEn(),
								en.GetValStrByKey(attr.getKeyOfEn()));
					} else {
						break;
					}
				}
				// endregion 处理sql.

				if (StringHelper.isNullOrEmpty(sql)) {
					continue;
				}

				if (sql.contains("@")) {
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}

				GEDtls gedtls = new GEDtls(dtl.getNo());
				gedtls.Delete(GEDtlAttr.RefPK, en.getPKVal());

				dt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					GEDtl gedtl = (GEDtl) ((gedtls.getGetNewEntity() instanceof GEDtl) ? gedtls
							.getGetNewEntity() : null);
					for (DataColumn dc : dt.Columns) {
						gedtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName)
								.toString());
					}
					gedtl.setRefPK(en.getPKVal().toString());
					gedtl.setRDT(DataType.getCurrentDataTime());
					gedtl.setRec(WebUser.getNo());
					gedtl.Insert();
				}
			}
		}
	}

	public boolean isShowDtl;

	public boolean getIsShowDtl() {
		return isShowDtl;
	}

	public void setIsShowDtl(boolean value) {
		isShowDtl = value;
	}

	// private int idx = 0;
	private int rowIdx = 0;
	private GroupField currGF;
	private GroupFields gfs;
	private MapFrames frames;
	private FrmAttachments aths;

	// private boolean isLeftNext;

	public final void BindColumn4(Entity en, String enName) {
		ctrlUseSta = "";
		this.enName = enName;
		HisEn = en;
		mapData = new MapData(enName);
		currGF = new GroupField();
		MapAttrs mattrs = mapData.getMapAttrs();
		gfs = mapData.getGroupFields();
		dtls = mapData.getMapDtls();
		frames = mapData.getMapFrames();
		m2ms = mapData.getMapM2Ms();
		aths = mapData.getFrmAttachments();
		mes = mapData.getMapExts();

		// region 处理事件.
		fes = mapData.getFrmEvents();
		if (!IsPostBack) {
			try {
				String msg = fes.DoEventNode(FrmEventList.FrmLoadBefore, en);
				if (!StringHelper.isNullOrEmpty(msg)) {
					Alert(msg);
				}
			} catch (RuntimeException ex) {
				// string msg = ex.Message;
				Alert(ex.getMessage());
				return;
			}
		}
		// endregion 处理事件.

		// 处理默认值.
		DealDefVal(mattrs);
		// 处理装载前填充.
		LoadData(mattrs, en);

		// region 计算出来列的宽度.
		int labCol = 80;
		int ctrlCol = 260;
		int width = (labCol + ctrlCol) * mapData.getTableCol() / 2;
		// endregion 计算出来列的宽度.

		// region 生成表头.
		appendPub("\t\n<Table style='width:" + width + "px;' align=left>");

		appendPub(AddTREnd());
		// endregion 生成表头.

		for (GroupField gf : gfs.ToJavaList()) {
			currGF = gf;
			appendPub(AddTR());
			if (gfs.size() == 1) {
				appendPub(AddTD(
						"colspan="
								+ mapData.getTableCol()
								+ " style='width:"
								+ width
								+ "px' class=GroupField valign='top' align=left ",
						"<div style='text-align:left; float:left'>&nbsp;"
								+ gf.getLab()
								+ "</div><div style='text-align:right; float:right'></div>"));
			} else {
				appendPub(AddTD(
						"colspan="
								+ mapData.getTableCol()
								+ " style='width:"
								+ width
								+ "px' class=GroupField valign='top' align=left  onclick=\"GroupBarClick('"
								+ gf.getIdx() + "')\"  ",
						"<div style='text-align:left; float:left'>&nbsp;<img src='"
								+ basePath
								+ "WF/Style/Min.gif' alert='Min' id='Img"
								+ gf.getIdx()
								+ "' border=0 />&nbsp;"
								+ gf.getLab()
								+ "</div><div style='text-align:right; float:right'></div>"));
			}
			appendPub(AddTREnd());

			// boolean isHaveH = false;
			// idx = -1;

			rowIdx = 0;
			int colSpan = mapData.getTableCol(); // 定义colspan的宽度.
			appendPub(AddTR());
			for (int i = 0; i < mattrs.size(); i++) {
				MapAttr attr = (MapAttr) ((mattrs.get(i) instanceof MapAttr) ? mattrs
						.get(i) : null);

				// region 过滤不显示的字段.
				if (attr.getGroupID() != gf.getOID()) {
					if (gf.getIdx() == 0 && attr.getGroupID() == 0) {
					} else {
						continue;
					}
				}
				if (attr.getHisAttr().getIsRefAttr() || !attr.getUIVisible()) {
					continue;
				}

				if (colSpan == 0) {
					InsertObjects(true);
				}
				// endregion 过滤不显示的字段.

				// region 补充空白的列.
				if (colSpan <= 0) {
					// 如果列已经用完.
					appendPub(AddTREnd());
					colSpan = mapData.getTableCol(); // 补充列.
					rowIdx++;
				}
				// endregion 补充空白的列.

				// region 处理大块文本的输出.
				// 显示的顺序号.
				// idx++;
				if (attr.getIsBigDoc()
						&& (attr.getColSpan() == mapData.getTableCol() || attr
								.getColSpan() == 0)) {
					int h = attr.getUIHeightInt() + 20;
					if (attr.getUIIsEnable()) {
						appendPub("<TD height='" + (new Integer(h)).toString()
								+ "px'  colspan=" + mapData.getTableCol()
								+ " width='100%' valign=top align=left>");
					} else {
						appendPub("<TD height='" + (new Integer(h)).toString()
								+ "px'  colspan=" + mapData.getTableCol()
								+ " width='100%' valign=top class=TBReadonly>");
					}

					appendPub("<div style='font-size:12px;color:black;' >");
					appendPub("<label id='" + attr.getKeyOfEn() + "'>");
					appendPub(attr.getName() + "</label>");
					appendPub("</div>");
					if (attr.getTBModel() == 2) {
						// 富文本输出.
						AddRichTextBox(en, attr);
					} else {
						TextBox mytbLine = new TextBox();
						mytbLine.setTextMode(TextBoxMode.MultiLine);
						mytbLine.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
						mytbLine.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
						mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn())
								.replace("\\n", "\n"));

						mytbLine.setEnabled(attr.getUIIsEnable());
						if (!mytbLine.getEnabled()) {
							mytbLine.setReadOnly(true);
						} else {
							mytbLine.attributes.put("class", "TBDoc");
						}
						mytbLine.attributes.put("style", "width:98%;height:"
								+ attr.getUIHeight()
								+ "px;padding: 0px;margin: 0px;");
						appendPub(mytbLine.toString());

						if (mytbLine.getEnabled()) {
							// String ctlID = mytbLine.getId();
							// Label mylab = this.GetLabelByID("Lab" +
							// attr.getKeyOfEn());
							// mylab.setText("<a href=\"javascript:TBHelp('" +
							// ctlID + "','" + basePath + "','" + enName + "','"
							// + attr.getKeyOfEn() + "')\">" + attr.getName() +
							// "</a>");
						}
					}

					appendPub(AddTDEnd());
					appendPub(AddTREnd());
					rowIdx++;
					// isLeftNext = true;
					continue;
				}

				if (attr.getIsBigDoc()) {
					if (colSpan == mapData.getTableCol()) {
						// 已经加满了
						appendPub(AddTR(" ID='" + currGF.getIdx() + "_"
								+ rowIdx + "' "));
						colSpan = colSpan - attr.getColSpan(); // 减去已经占用的col.
					}

					appendPub("<TD class=FDesc colspan=" + attr.getColSpan()
							+ " height='" + attr.getUIHeight() + "px' >");
					appendPub(attr.getName());

					TextBox mytbLine = new TextBox();
					mytbLine.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
					mytbLine.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
					mytbLine.setTextMode(TextBoxMode.MultiLine);
					mytbLine.attributes.put("class", "TBDoc");
					mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()));
					if (!mytbLine.getEnabled()) {
						// mytbLine.attributes.put("class", "TBReadonly");
						mytbLine.setReadOnly(true);
					}
					mytbLine.attributes.put("style",
							"width:98%;height:100%;padding: 0px;margin: 0px;");
					appendPub(mytbLine.toString());
					appendPub(AddTDEnd());
					continue;
				}
				// endregion 大块文本的输出.

				// region 处理超链接
				if (!attr.getUIIsEnable()) {
					// 判断是否有隐藏的超链接字段.
					if (LinkFields.contains("," + attr.getKeyOfEn() + ",")) {
						Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType,
								MapExtXmlList.Link);
						MapExt meLink = (MapExt) ((tempVar instanceof MapExt) ? tempVar
								: null);
						String url = meLink.getTag();
						if (!url.contains("?")) {
							url = url + "?a3=2";
						}
						url = url + "&WebUserNo=" + WebUser.getNo() + "&SID="
								+ WebUser.getSID() + "&EnName=" + enName;
						if (url.contains("@AppPath")) {
							url = url.replace("@AppPath", basePath);
						}
						if (url.contains("@")) {
							Attrs attrs = en.getEnMap().getAttrs();
							int size_attrs = attrs.size();
							for (int j = 0; j < size_attrs; j++) {
								url = url.replace("@" + attr.getKeyOfEn(),
										en.GetValStrByKey(attr.getKeyOfEn()));
								if (!url.contains("@")) {
									break;
								}
							}
						}
						appendPub(AddTD(
								"colspan=" + colSpan,
								"<a href='" + url + "' target='"
										+ meLink.getTag1() + "' >"
										+ en.GetValByKey(attr.getKeyOfEn())
										+ "</a>"));
						continue;
					}
				}
				// endregion 处理超链接

				// region 首先判断当前剩余的单元格是否满足当前控件的需要。
				if (attr.getColSpan() + 1 > mapData.getTableCol()) {
					attr.setColSpan(mapData.getTableCol() - 1); // 如果设置的
				}

				if (colSpan < attr.getColSpan() + 1 || colSpan == 1
						|| colSpan == 0) {
					// 如果剩余的列不能满足当前的单元格，就补充上它，让它换行.
					if (colSpan != 0) {
						appendPub(AddTD("colspan=" + colSpan, ""));
					}
					appendPub(AddTREnd());

					colSpan = mapData.getTableCol();
					appendPub(AddTR());
				}
				// endregion 首先判断当前剩余的单元格是否满足当前控件的需要。

				// region 其它的就是增加一列控件一列描述的字段.
				TextBox tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
				tb.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
				tb.setEnabled(attr.getUIIsEnable());
				colSpan = colSpan - 1 - attr.getColSpan(); // 首先减去当前的占位.
				switch (attr.getLGType()) {
				case Normal:
					switch (attr.getMyDataType()) {
					case BP.DA.DataType.AppString:
						appendPub(AddTDDesc(attr.getName()));
						if (attr.getIsSigan()) {
							String v = en.GetValStrByKey(attr.getKeyOfEn());
							if (v.length() == 0) {
								appendPub(AddTD(
										"colspan=" + attr.getColSpan(),
										"<img src='"
												+ basePath
												+ "DataUser/Siganture/"
												+ WebUser.getNo()
												+ ".jpg' border=0 onerror=\"this.src='"
												+ basePath
												+ "DataUser/Siganture/UnName.jpg'\"/>"));
							} else {
								appendPub(AddTD(
										"colspan=" + attr.getColSpan(),
										"<img src='"
												+ basePath
												+ "DataUser/Siganture/"
												+ v
												+ ".jpg' border=0 onerror=\"this.src='"
												+ basePath
												+ "DataUser/Siganture/UnName.jpg'\"/>"));
							}
						} else {
							tb.setShowType(TBType.TB);
							tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
							tb.attributes.put("width", "100%");
							appendPub(AddTD("colspan=" + attr.getColSpan(),
									tb.toString()));
						}
						break;
					case BP.DA.DataType.AppDate:
						appendPub(AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Date);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable()) {
							tb.attributes.put("onfocus", "WdatePicker();");
						}

						appendPub(AddTD("colspan=" + attr.getColSpan(), tb));
						break;
					case BP.DA.DataType.AppDateTime:
						appendPub(AddTDDesc(attr.getName()));
						tb.setShowType(TBType.DateTime);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						if (attr.getUIIsEnable()) {
							tb.attributes
									.put("onfocus",
											"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
						}

						appendPub(AddTD("colspan=" + attr.getColSpan(), tb));
						break;
					case BP.DA.DataType.AppBoolean:
						appendPub(AddTDDesc(""));
						CheckBox cb = new CheckBox();
						cb.setText(attr.getName().trim());
						cb.setId("CB_" + attr.getKeyOfEn());//ToLowerCase
						cb.setName("CB_" + attr.getKeyOfEn());//ToLowerCase
						cb.setChecked(attr.getDefValOfBool());
						cb.setEnabled(attr.getUIIsEnable());
						cb.setChecked(en.GetValBooleanByKey(attr.getKeyOfEn()));
						appendPub(AddTD("colspan=" + attr.getColSpan(), cb));
						break;
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppFloat:
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
						appendPub(AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Num);
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						appendPub(AddTD("colspan=" + attr.getColSpan(),
								tb.toString()));
						break;
					case BP.DA.DataType.AppInt:
						appendPub(AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Num);
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d]/g,'')");
						tb.attributes.put("onblur",
								"value=value.replace(/[^-?\\d]/g,'')");
						tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
						appendPub(AddTD("colspan=" + attr.getColSpan(), tb));
						break;
					case BP.DA.DataType.AppMoney:
						appendPub(AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Moneny);

						if (SystemConfig.getAppSettings().get("IsEnableNull")
								.equals("1")) {
							java.math.BigDecimal v = en.GetValMoneyByKey(attr
									.getKeyOfEn());
							if (v.equals(567567567)) {
								tb.setText("");
							} else {
								tb.setText(decimalFormat(v));
							}
						} else {
							tb.setText(decimalFormat(en.GetValMoneyByKey(attr
									.getKeyOfEn())));
						}

						// tb.Text =
						// en.GetValMoneyByKey(attr.KeyOfEn).ToString("0.00");

						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
						appendPub(AddTD("colspan=" + attr.getColSpan(),
								tb.toString()));
						break;
					case BP.DA.DataType.AppRate:
						appendPub(AddTDDesc(attr.getName()));
						tb.setShowType(TBType.Moneny);
						tb.setText(decimalFormat(en.GetValMoneyByKey(attr
								.getKeyOfEn())));
						// 增加验证
						// tb.Attributes.Add("onkeyup",
						// @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
						tb.attributes
								.put("onblur",
										"value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
						appendPub(AddTD("colspan=" + attr.getColSpan(),
								tb.toString()));
						break;
					default:
						break;
					}
					// tb.Attributes["width"] = "100%";
					switch (attr.getMyDataType()) {
					case BP.DA.DataType.AppString:
					case BP.DA.DataType.AppDateTime:
					case BP.DA.DataType.AppDate:
						if (tb.getEnabled()) {
							tb.attributes.put("maxlength",
									String.valueOf(attr.getMaxLen()));
						} else {
							tb.setReadOnly(true);
							// tb.attributes.put("class", "TBReadonly");
						}
						break;
					default:
						if (tb.getEnabled()) {
							tb.attributes.put("class", "TBNum");
						} else {
							tb.setReadOnly(true);
							// tb.attributes.put("class", "TBNumReadonly");
						}
						break;
					}
					break;
				case Enum:
					if (attr.getUIContralType().equals(UIContralType.DDL)) {
						appendPub(AddTDDesc(attr.getName()));
						DDL ddle = new DDL();
						ddle.setId("DDL_" + attr.getKeyOfEn());//ToLowerCase
						ddle.setName("DDL_" + attr.getKeyOfEn());//ToLowerCase
						ddle.BindSysEnum(attr.getUIBindKey());
						ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
						ddle.setEnabled(attr.getUIIsEnable());
						appendPub(AddTD("colspan=" + attr.getColSpan(),
								ddle.toString()));
					} else {
						appendPub(AddTDDesc(attr.getName()));
						appendPub("<TD class=TD colspan='" + attr.getColSpan()
								+ "'>");
						SysEnums ses = new SysEnums(attr.getUIBindKey());
						for (SysEnum item : ses.ToJavaList()) {
							RadioButton rb = new RadioButton();
							rb.setId("RB_" + attr.getKeyOfEn() + "_"//ToLowerCase
									+ item.getIntKey());
							rb.setText(item.getLab());
							if (item.getIntKey() == en.GetValIntByKey(attr
									.getKeyOfEn())) {
								rb.setChecked(true);
							} else {
								rb.setChecked(false);
							}
							rb.setName(attr.getKeyOfEn());
							appendPub(rb.toString());
						}
						appendPub(AddTDEnd());
					}
					break;
				case FK:
					appendPub(AddTDDesc(attr.getName()));
					DDL ddl1 = new DDL();
					ddl1.setId("DDL_" + attr.getKeyOfEn());//ToLowerCase
					ddl1.setName("DDL_" + attr.getKeyOfEn());//ToLowerCase
					try {
						EntitiesNoName ens = attr.getHisEntitiesNoName();
						ens.RetrieveAll();
						ddl1.BindEntities(ens);
						ddl1.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
					} catch (java.lang.Exception e) {
					}
					ddl1.setEnabled(attr.getUIIsEnable());
					appendPub(AddTD("colspan=" + attr.getColSpan(),
							ddl1.toString()));
					break;
				default:
					break;
				}
				// endregion 其它的就是增加一列控件一列描述的字段

			} // 结束字段集合循环.

			// 在分组后处理它, 首先判断当前剩余的单元格是否满足当前控件的需要。
			if (colSpan != mapData.getTableCol()) {
				// 如果剩余的列不能满足当前的单元格，就补充上它，让它换行.
				if (colSpan != 0) {
					appendPub(AddTD("colspan=" + colSpan, ""));
				}

				appendPub(AddTREnd());
				colSpan = mapData.getTableCol();
			}
			InsertObjects(false);
		} // 结束分组循环.

		// region 审核组件
		FrmWorkCheck fwc = new FrmWorkCheck(enName);
		if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable) {
			rowIdx++;

			appendPub(AddTR());
			appendPub(AddTD(
					"colspan=" + mapData.getTableCol()
							+ " class=GroupField valign='top' align=left ",
					"<div style='text-align:left; float:left'>&nbsp;审核信息</div><div style='text-align:right; float:right'></div>"));
			appendPub(AddTREnd());

			// myidx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
					+ enName
					+ "' height='50px' width='100%' style='align:left'>");
			String src = basePath + "WF/WorkOpt/WorkCheck.jsp?s=2";
			String paras = paramsStr;
			try {
				if (!paras.contains("FID=")) {
					paras += "&FID=" + en.GetValStrByKey("FID");
				}
			} catch (java.lang.Exception e2) {
			}
			if (!paras.contains("OID=")) {
				paras += "&OID=" + en.GetValStrByKey("OID");
			}
			src += "&r=q" + paras;
			appendPub("<iframe ID='F33"
					+ fwc.getNo()
					+ "'  src='"
					+ src
					+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0'  width='100%'  scrolling=auto/></iframe>");
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}
		// endregion 审核组件
		appendPub(AddTREnd());
		appendPub(AddTableEnd());

		// region 处理iFrom 的自适应的问题。
		scriptsBlock.append("\t\n<script type='text/javascript' >");
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (!dtl.getIsView()) {
				continue;
			}

			scriptsBlock.append("\t\n window.setInterval(\"ReinitIframe('F"
					+ dtl.getNo() + "','TD" + dtl.getNo() + "')\", 200);");
		}
		for (MapFrame fr : frames.ToJavaList()) {
			// if (fr.IsAutoSize)
			scriptsBlock.append("\t\n window.setInterval(\"ReinitIframe('F"
					+ fr.getNoOfObj() + "','TD" + fr.getNoOfObj()
					+ "')\", 200);");
		}
		for (MapM2M m2m : m2ms.ToJavaList()) {
			// if (m2m.ShowWay == FrmAutoSize)
			scriptsBlock.append("\t\n window.setInterval(\"ReinitIframe('F"
					+ m2m.getNoOfObj() + "','TD" + m2m.getNoOfObj()
					+ "')\", 200);");
		}
		for (FrmAttachment ath : aths.ToJavaList()) {
			// if (ath.IsAutoSize)
			scriptsBlock.append("\t\n window.setInterval(\"ReinitIframe('F"
					+ ath.getMyPK() + "','TD" + ath.getMyPK() + "')\", 200);");
		}
		scriptsBlock.append("\t\n</script>");
		// endregion 处理iFrom 的自适应的问题。

		// 处理扩展。
		AfterBindEn_DealMapExt(enName, mattrs, en);
		if (!getIsReadonly()) {
			// region 处理iFrom SaveDtlData。
			scriptsBlock.append("\t\n<script type='text/javascript' >");
			scriptsBlock.append("\t\n function SaveDtl(dtl) { ");
			// js += "\t\n    GenerPageKVs(); //调用产生kvs ";
			scriptsBlock
					.append("\t\n document.getElementById('F' + dtl ).contentWindow.SaveDtlData(); ");
			scriptsBlock.append("\t\n } ");
			scriptsBlock.append("\t\n</script>");
			// endregion 处理iFrom SaveDtlData。

			// region 处理iFrom SaveM2M Save
			scriptsBlock.append("\t\n<script type='text/javascript' >");
			scriptsBlock.append("\t\n function SaveM2M(dtl) { ");
			scriptsBlock
					.append("\t\n document.getElementById('F' + dtl ).contentWindow.SaveM2M();");
			scriptsBlock.append("\t\n } ");
			scriptsBlock.append("\t\n</script>");
			// endregion 处理iFrom SaveM2M Save。
		}
	}

	private void AfterBindEn_DealMapExt(String enName, MapAttrs mattrs,
			Entity en) {

		// region 处理事件.
		if (dtls.size() >= 1) {
			scriptsBlock.append("\t\n<script type='text/javascript' >");
			scriptsBlock.append("\t\n function SaveDtlAll(){ ");

			for (MapDtl dtl : dtls.ToJavaList()) {
				if (dtl.getIsUpdate() == true || dtl.getIsInsert() == true) {
					scriptsBlock.append("\t\n try{  ");

					if (dtl.getHisDtlShowModel().equals(DtlShowModel.Table)) {
						scriptsBlock.append("\t\n  SaveDtl('" + dtl.getNo()
								+ "'); ");
					}

					scriptsBlock.append("\t\n } catch(e) { ");
					scriptsBlock
							.append("\t\n  alert(e.name  + e.message);  return false;");
					scriptsBlock.append("\t\n } ");
				}
			}

			scriptsBlock.append("\t\n  return true; } ");
			scriptsBlock.append("\t\n</script>");

		} else {
			scriptsBlock.append("\t\n<script type='text/javascript' >");
			scriptsBlock.append("\t\n function SaveDtlAll() { ");
			scriptsBlock.append("\t\n return true; } ");
			scriptsBlock.append("\t\n</script>");
		}

		fes = this.mapData.getFrmEvents();
		if (this.IsPostBack == false) {
			try {
				String msg = fes.DoEventNode(FrmEventList.FrmLoadAfter, en);
				if (msg != null) {
					Alert(msg);
				}
			} catch (RuntimeException ex) {

				Alert("载入之前错误:" + ex.getMessage());
				return;
			}
		}
		// endregion 处理事件.
		// 解析html 到对象
		HashMap<String, BaseWebControl> ctrlMap = HtmlUtils.httpParser(
				Pub.toString(), false);
		// xiaozhoupeng 测试数据
		// DDL ddlTemp = (DDL) ctrlMap.get("DDL_QingJiaLeiXing");
		// ddlTemp.Items.add(new ListItem("你好", "5"));
		// ddlTemp.SetSelectItem("5");
		// setHtmlByCtrl(ddlTemp);
		//
		// TextBox tb_fqr = (TextBox) ctrlMap.get("TB_FaQiRen");
		// tb_fqr.setText("小周鹏");
		// setHtmlByCtrl(tb_fqr);
		//
		// TextBox tb_qjyy = (TextBox) ctrlMap.get("TB_QingJiaYuanYin");
		// tb_qjyy.setText("请假原因");
		// setHtmlByCtrl(tb_qjyy);

		// region 处理扩展设置
		if (mes.size() != 0) {
			// region load js.
			scripts.add("WF/Scripts/jquery-1.4.1.min.js");
			scripts.add("WF/CCForm/MapExt.js");
			scripts.add("DataUser/JSLibData/" + enName + ".js");

			this.appendPub("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");
			// endregion load js.

			// region 首先处理自动填充，下拉框数据。
			for (MapExt me : mes.ToJavaList()) {
				// 自动填充下拉框.
				if (me.getExtType().equals(MapExtXmlList.AutoFullDLL)) {
					DDL ddlFull = (DDL) ctrlMap.get("DDL_"
							+ me.getAttrOfOper());//ToLowerCase
					if (ddlFull == null) {
						me.Delete();
						continue;
					}

					// String valOld = ddlFull.getSelectedItemStringVal();
					Object tempVar = me.getDoc();
					String fullSQL = (String) ((tempVar instanceof String) ? tempVar
							: null);
					if (!IsLoadData) {
						// 替换保存的时候EN中表单中变量
						for (Attr item : en.getEnMap().getAttrs()) {
							if (fullSQL.contains("@" + item.getKey() + ";")) {
								Enumeration enumeration = get_request()
										.getParameterNames();
								while (enumeration.hasMoreElements()) {
									String key = (String) enumeration
											.nextElement();
									if (key.endsWith(item.getKey())) {
										en.SetValByKey(item.getKey(),
												get_request().getParameter(key));
									}
								}
							}
						}
					}

					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, "");

					ddlFull.Items.clear();
					DataTable table = DBAccess.RunSQLReturnTable(fullSQL);
					if (null == table) {
						return;
					}
					if (table.Rows.size() == 0) {
						DataRow row = table.NewRow();
						row.setValue("No", "");
						row.setValue("Name", "*请选择");
						table.Rows.add(row);

					}

					ddlFull.Bind(table, "No", "Name");

					String val = "";
					if (!IsLoadData) {
						Enumeration enumeration = get_request()
								.getParameterNames();
						while (enumeration.hasMoreElements()) {
							String key = (String) enumeration.nextElement();
							// if (key.endsWith(me.getAttrOfOper()))
							if (key.equalsIgnoreCase(me
									.getAttrOfOper())) {//ToLowerCase
								val = get_request().getParameter(key);
							}
						}
					}
					if (StringHelper.isNullOrEmpty(val)) {
						ddlFull.SetSelectItem(en.GetValStrByKey(me
								.getAttrOfOper()));//ToLowerCase
					} else {
						ddlFull.SetSelectItem(val);
					}
					setHtmlByCtrl(ddlFull);
				}
			}
			// endregion 首先处理自动填充，下拉框数据。

			// region 在处理其它。
			DataTable dt = new DataTable();
			for (MapExt me : mes.ToJavaList()) {
				// 自动填充其他的控件..
				if (me.getExtType().equals(MapExtXmlList.DDLFullCtrl)) {
					DDL ddlOper = (DDL) ctrlMap.get("DDL_"
							+ me.getAttrOfOper());//ToLowerCase
					if (ddlOper == null) {
						continue;
					}
					ddlOper.attributes.put("onchange",
							"DDLFullCtrl(this.value,\'" + ddlOper.getId()
									+ "\', \'" + me.getMyPK() + "\')");

					if (!me.getTag().equals("")) {
						// 处理下拉框的选择范围的问题
						String[] strs = me.getTag().split("[$]", -1);
						for (String str : strs) {
							String[] myCtl = str.split("[:]", -1);
							String ctlID = myCtl[0];
							DDL ddlC1 = (DDL) ctrlMap.get("DDL_"
									+ ctlID);//.toLowerCase()
							if (ddlC1 == null) {
								// me.Tag = "";
								// me.Update();
								continue;
							}

							String sql = myCtl[1].replace("~", "'");
							sql = BP.WF.Glo.DealExp(sql, en, null);
							sql = sql.replace("@Key", ddlOper
									.getSelectedItemStringVal().trim());

							// sql = sql.Replace("WebUser.No", WebUser.getNo());
							// sql = sql.Replace("@WebUser.Name", WebUser.Name);
							// sql = sql.Replace("@WebUser.FK_Dept",
							// WebUser.getFK_Dept());
							// sql = sql.Replace("@WebUser.FK_DeptName",
							// WebUser.getFK_DeptName());
							// sql = sql.Replace("@Key",
							// ddlOper.SelectedItemStringVal.Trim());
							// if (sql.Contains("@"))
							// {
							// foreach (MapAttr attr in mattrs)
							// {
							// if (sql.Contains("@" + attr.KeyOfEn) == false)
							// continue;
							// sql = sql.Replace("@" + attr.KeyOfEn,
							// en.GetValStrByKey(attr.KeyOfEn));
							// if (sql.Contains("@") == false)
							// break;
							// }
							// }

							dt = DBAccess.RunSQLReturnTable(sql);
							String valC1 = ddlC1.getSelectedItemStringVal();
							ddlC1.Items.clear();
							for (DataRow dr : dt.Rows) {
								ddlC1.Items
										.add(new ListItem(dr.getValue(1)
												.toString(), dr.getValue(1)
												.toString()));
							}
							ddlC1.SetSelectItem(valC1);
							setHtmlByCtrl(ddlC1);
						}
					}
					setHtmlByCtrl(ddlOper);
					// 自动初始化ddl的下拉框数据
				} else if (me.getExtType().equals(MapExtXmlList.ActiveDDL)) {
					DDL ddlPerant = (DDL) ctrlMap.get("DDL_"
							+ me.getAttrOfOper());//ToLowerCase
					DDL ddlChild = (DDL) ctrlMap.get("DDL_"
							+ me.getAttrsOfActive());//ToLowerCase
					if (ddlPerant == null || ddlChild == null) {
						continue;
					}
					ddlPerant.attributes.put("onchange",
							"DDLAnsc(this.value,\'" + ddlChild.getId()
									+ "\', \'" + me.getMyPK() + "\')");
					// 处理默认选择。
					String val = ddlPerant.getSelectedItemStringVal();
					String valClient = en.GetValStrByKey(me.getAttrsOfActive()); // ddlChild.SelectedItemStringVal;

					Object tempVar2 = me.getDoc();
					String fullSQL = (String) ((tempVar2 instanceof String) ? tempVar2
							: null);
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = fullSQL.replace("@Key", val);
					fullSQL = fullSQL.replace("WebUser.No", WebUser.getNo());
					fullSQL = fullSQL.replace("@WebUser.Name",
							WebUser.getName());
					fullSQL = fullSQL.replace("@WebUser.FK_Dept",
							WebUser.getFK_Dept());
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);

					dt = DBAccess.RunSQLReturnTable(fullSQL);
					if (null == dt) {
						dt = new DataTable();
					}
					// ddlChild.Items.Clear();
					for (DataRow dr : dt.Rows) {
						ddlChild.Items.add(new ListItem(dr.getValue(1)
								.toString(), dr.getValue(0).toString()));
					}

					ddlChild.SetSelectItem(valClient);

					setHtmlByCtrl(ddlChild);
					setHtmlByCtrl(ddlPerant);

				} else if (me.getExtType().equals(MapExtXmlList.AutoFullDLL)) {// 自动填充下拉框
					continue;
				} else if (me.getExtType().equals(MapExtXmlList.TBFullCtrl)) {// 自动填充
					TextBox tbAuto = (TextBox) ctrlMap.get("TB_"
							+ me.getAttrOfOper());//ToLowerCase
					if (tbAuto == null) {
						continue;
					}

					// onpropertychange
					// tbAuto.Attributes["onpropertychange"] =
					// "DoAnscToFillDiv(this,this.value,\'" + tbAuto.ClientID +
					// "\', \'" + me.MyPK + "\');";
					// tbAuto.Attributes["onkeydown"] =
					// "DoAnscToFillDiv(this,this.value,\'" + tbAuto.ClientID +
					// "\', \'" + me.MyPK + "\');";
					// tbAuto.Attributes["onkeyup"] =
					// "DoAnscToFillDiv(this,this.value,\'" + tbAuto.ClientID +
					// "\', \'" + me.MyPK + "\');";
					// tbAuto.Attributes["ondblclick"] =
					// "ReturnValTBFullCtrl(this,'" + me.MyPK + "','sd');";
					tbAuto.attributes
							.put("ondblclick", "ReturnValTBFullCtrl(this,'"
									+ me.getMyPK() + "');");
					tbAuto.attributes.put(
							"onkeyup",
							"DoAnscToFillDiv(this,this.value,\'"
									+ tbAuto.getId() + "\', \'" + me.getMyPK()
									+ "\');");
					tbAuto.attributes.put("AUTOCOMPLETE", "OFF");
					if (!me.getTag().equals("")) {
						// 处理下拉框的选择范围的问题
						String[] strs = me.getTag().split("[$]", -1);
						for (String str : strs) {
							String[] myCtl = str.split("[:]", -1);
							String ctlID = myCtl[0];
							DDL ddlC1 = (DDL) ctrlMap.get("DDL_"
									+ ctlID);//.toLowerCase()
							if (ddlC1 == null) {
								// me.Tag = "";
								// me.Update();
								continue;
							}

							String sql = myCtl[1].replace("~", "'");

							String txt = tbAuto.getText().trim();

							// if (string.IsNullOrEmpty(txt))
							// txt = "$";

							sql = sql.replace("@Key", txt);
							sql = BP.WF.Glo.DealExp(sql, en, null);

							// sql = sql.Replace("WebUser.No", WebUser.getNo());
							// sql = sql.Replace("@WebUser.Name", WebUser.Name);
							// sql = sql.Replace("@WebUser.FK_Dept",
							// WebUser.getFK_Dept());
							// sql = sql.Replace("@WebUser.FK_DeptName",
							// WebUser.getFK_DeptName());
							// if (sql.Contains("@"))
							// {
							// foreach (MapAttr attr in mattrs)
							// {
							// if (sql.Contains("@" + attr.KeyOfEn) == false)
							// continue;
							// sql = sql.Replace("@" + attr.KeyOfEn,
							// en.GetValStrByKey(attr.KeyOfEn));
							// if (sql.Contains("@") == false)
							// break;
							// }
							// }

							try {
								dt = DBAccess.RunSQLReturnTable(sql);
							} catch (RuntimeException ex) {
								// this.Clear();

								appendPub(AddFieldSet("配置错误"));
								appendPub(me.ToStringAtParas()
										+ "<hr>错误信息:<br>" + ex.getMessage());
								appendPub(AddFieldSetEnd());
								return;
							}

							String valC1 = ddlC1.getSelectedItemStringVal();
							ddlC1.Items.clear();
							for (DataRow dr : dt.Rows) {
								ddlC1.Items
										.add(new ListItem(dr.getValue(1)
												.toString(), dr.getValue(0)
												.toString()));
							}
							ddlC1.SetSelectItem(valC1);
							setHtmlByCtrl(ddlC1);
						}
					}
					setHtmlByCtrl(tbAuto);
				} else if (me.getExtType().equals(MapExtXmlList.InputCheck)) {
					TextBox tbJS = (TextBox) ctrlMap.get("TB_"
							+ me.getAttrOfOper());//ToLowerCase
					if (tbJS != null) {
						tbJS.attributes.put(me.getTag2(), me.getTag1()
								+ "(this);");
						setHtmlByCtrl(tbJS);
					} else {
						DDL ddl = (DDL) ctrlMap.get("DDL_"
								+ me.getAttrOfOper());//ToLowerCase
						if (ddl != null) {
							ddl.attributes.put(me.getTag2(), me.getTag1()
									+ "(this);");
							setHtmlByCtrl(ddl);
						}

					}
					// 小周鹏 2015/06/3 增加 扩展js 函数 Start
					String doc = me.getDoc();
					if (!StringHelper.isNullOrEmpty(doc)) {
						scriptsBlock
								.append("\t\n<script type='text/javascript' >");
						scriptsBlock.append(doc);
						scriptsBlock.append("</script>");
					}
					// 小周鹏 2015/06/3 增加 扩展js 函数 End

				} else if (me.getExtType().equals(MapExtXmlList.PopVal)) {// 弹出窗
					// TB
					TextBox tb = (TextBox) ctrlMap.get("TB_"
							+ me.getAttrOfOper());//ToLowerCase
					if (tb == null) {
						continue;
					}

					// 移除常用词汇事件
					if (tb.getRows() > 1) {
						tb.attributes.remove("ondblclick");
					}

					// if ( ! tb.getCssClass().equals("TBReadonly"))
					if (!tb.getReadOnly()) {

						if (me.getPopValWorkModel() == 0) {
							tb.attributes.put("ondblclick", "ReturnVal(this,'"
									+ BP.WF.Glo.DealExp(me.getDoc(), en, null)
									+ "','sd');");
						} else {
							tb.attributes.put(
									"ondblclick",
									"ReturnValCCFormPopVal(this,'"
											+ me.getMyPK() + "','"
											+ en.getPKVal() + "');");
						}
					}
					setHtmlByCtrl(tb);
				} else if (me.getExtType().equals(
						MapExtXmlList.RegularExpression)) {// 正则表达式,对数据控件处理
					BaseWebControl tbExp = ctrlMap.get("TB_"
							+ me.getAttrOfOper());//ToLowerCase

					if (tbExp == null) {
						tbExp = ctrlMap.get("CB_"
								+ me.getAttrOfOper());//ToLowerCase
					}

					if (tbExp == null) {
						tbExp = ctrlMap.get("DDL_"
								+ me.getAttrOfOper());//ToLowerCase
					}
					// 20150613 xiaozhoupeng 添加，原因：radioButton 获取不到正则表达式事件 START
					boolean isSetExt = false;
					if (tbExp == null) {
						String rbName = "RB_" + me.getAttrOfOper();//ToLowerCase
						if (HtmlUtils.radioGroupMap.containsKey(rbName)) {
							ArrayList<String> radioGroupIds = HtmlUtils.radioGroupMap
									.get(rbName);
							int size = radioGroupIds.size();
							for (int i = 0; i < size; i++) {
								tbExp = ctrlMap.get(radioGroupIds.get(i));
								if (tbExp != null) {
									isSetExt = true;
									// 验证输入的正则格式
									String regFilter = me.getDoc();
									if (regFilter.lastIndexOf("/g") < 0
											&& regFilter.lastIndexOf('/') < 0) {
										regFilter = "'" + regFilter + "'";
									}
									// 处理事件
									if (me.getTag().equals("onkeyup")
											|| me.getTag().equals("onkeypress")) {
										tbExp.addAttr(
												me.getTag(),
												"return txtTest_Onkeyup(this,"
														+ regFilter + ",'"
														+ me.getTag1() + "')");
										// tbExp.Attributes[me.Tag] +=
										// "value=value.replace(" + regFilter +
										// ",'')";
									} else if (me.getTag().equals("onclick")) {
										tbExp.addAttr(me.getTag(), me.getDoc());
									} else {
										tbExp.addAttr(
												me.getTag(),
												"EleInputCheck2(this,"
														+ regFilter + ",'"
														+ me.getTag1() + "');");
									}
									setHtmlByCtrl(tbExp);
								}
							}

						}
					}
					if (isSetExt) {
						continue;
					}
					// 20150613 xiaozhoupeng 添加，原因：radioButton 获取不到正则表达式事件 END
					if (tbExp == null || me.getTag().equals("onsubmit")) {
						continue;
					}

					// 验证输入的正则格式
					String regFilter = me.getDoc();
					if (regFilter.lastIndexOf("/g") < 0
							&& regFilter.lastIndexOf('/') < 0) {
						regFilter = "'" + regFilter + "'";
					}
					// 处理事件
					if (me.getTag().equals("onkeyup")
							|| me.getTag().equals("onkeypress")) {
						tbExp.addAttr(me.getTag(),
								"return txtTest_Onkeyup(this," + regFilter
										+ ",'" + me.getTag1() + "')");
						// tbExp.Attributes[me.Tag] += "value=value.replace(" +
						// regFilter + ",'')";
					} else if (me.getTag().equals("onclick")) {
						tbExp.addAttr(me.getTag(), me.getDoc());
					} else {
						tbExp.addAttr(me.getTag(), "EleInputCheck2(this,"
								+ regFilter + ",'" + me.getTag1() + "');");
					}
					setHtmlByCtrl(tbExp);
				}
			}
			// endregion 在处理其它。

		}

		// region 保存时处理正则表达式验证
		StringBuilder scriptCheckFrm = new StringBuilder();
		scriptCheckFrm.append("\t\n<script type='text/javascript' >")
				.append("\t\n function SysCheckFrm(){ ")
				.append("\t\n var isPass = true;")
				.append("\t\n var alloweSave = true;")
				.append("\t\n var erroMsg = '提示信息:';");

		BaseWebControl regularContrl = null;

		for (MapExt me : mes.ToJavaList()) {
			if (me.getExtType().equals(MapExtXmlList.RegularExpression)
					&& me.getTag().equals("onsubmit")) {
				// TB tb = this.GetTBByID("TB_" + me.getAttrOfOper());
				// TextBox tb = (TextBox)ctrlMap.get("TB_" +
				// me.getAttrOfOperToLowerCase());
				regularContrl = ctrlMap.get("TB_"
						+ me.getAttrOfOper());//ToLowerCase
				if (null == regularContrl) {
					regularContrl = ctrlMap.get("DDL_"
							+ me.getAttrOfOper());//ToLowerCase
					if (null == regularContrl) {
						continue;
					}
				}
				scriptCheckFrm.append("\t\n try{  ");
				scriptCheckFrm
						.append("\t\n var element = document.getElementById('"
								+ regularContrl.getId() + "');");
				// 验证输入的正则格式
				String regFilter = me.getDoc();
				if (regFilter.lastIndexOf("/g") < 0
						&& regFilter.lastIndexOf('/') < 0) {
					regFilter = "'" + regFilter + "'";
				}

				scriptCheckFrm.append("\t\n isPass = EleSubmitCheck(element,"
						+ regFilter + ",'" + me.getTag1() + "');");
				// scriptCheckFrm += "\t\n var reg =new RegExp(" + regFilter +
				// ");   isPass = reg.test(element.value); ";
				scriptCheckFrm.append("\t\n  if(isPass == false){");
				scriptCheckFrm.append("\t\n   //EleSubmitCheck(element,"
						+ regFilter + ",'" + me.getTag1()
						+ "'); alloweSave = false;");
				scriptCheckFrm.append("\t\n   alloweSave = false;");
				scriptCheckFrm.append("\t\n    erroMsg += '" + me.getTag1()
						+ ";';");
				scriptCheckFrm.append("\t\n  }");
				scriptCheckFrm.append("\t\n } catch(e) { ");
				scriptCheckFrm
						.append("\t\n  alert(e.name  + e.message);  return false;");
				scriptCheckFrm.append("\t\n } ");
			}
		}
		scriptCheckFrm.append("\t\n if(alloweSave == false){");
		scriptCheckFrm.append("\t\n     alert(erroMsg);");
		scriptCheckFrm.append("\t\n  } ");
		scriptCheckFrm.append("\t\n return alloweSave; } ");
		scriptCheckFrm.append("\t\n</script>");
		scriptsBlock.append(scriptCheckFrm.toString());
		// endregion

		// endregion 处理扩展设置

		// region 处理自动计算

		StringBuilder js = new StringBuilder();
		js.append("\t\n <script type='text/javascript' >oid=")
				.append(en.getPKVal()).append(";</script>");
		this.appendPub(js.toString());
		for (MapExt ext : mes.ToJavaList()) {
			if (!ext.getTag().equals("1")) {
				continue;
			}
			js = new StringBuilder("\t\n <script type='text/javascript' >");
			// TB tb = null;
			TextBox tb = null;
			try {
				tb = (TextBox) ctrlMap.get("TB_"
						+ ext.getAttrOfOper());//ToLowerCase
				if (tb == null) {
					continue;
				}
			} catch (java.lang.Exception e) {
				continue;
			}

			String left = "\n  document.forms[0]." + tb.getId() + ".value = ";
			String right = ext.getDoc();

			Paras ps = new Paras();
			ps.SQL = "SELECT KeyOfEn,Name FROM Sys_MapAttr WHERE FK_MapData="
					+ ps.getDBStr()
					+ "FK_MapData AND LGType=0 AND (MyDataType=2 OR MyDataType=3 OR MyDataType=5 OR MyDataType=8 OR MyDataType=9) ORDER BY KeyOfEn DESC";
			ps.Add("FK_MapData", enName);

			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			for (DataRow dr : dt.Rows) {
				String keyofen = dr.getValue(0).toString();
				String name = dr.getValue(1).toString();

				if (ext.getDoc().contains("@" + keyofen)
						|| (ext.getDoc().contains("@" + name) && !StringHelper
								.isNullOrEmpty(name))) {
				} else {
					continue;
				}

				String tbID = "TB_" + keyofen;//.toLowerCase()
				// TB mytb = this.GetTBByID(tbID);
				TextBox mytb = (TextBox) ctrlMap.get(tbID);
				mytb.addAttr("onkeyup",
						"javascript:Auto" + ext.getAttrOfOper()//ToLowerCase
								+ "();");

				right = right.replace("@" + keyofen,
						" parseFloat( document.forms[0]." + mytb.getId()
								+ ".value.replace( ',' ,  '' ) ) ");
				if (!StringHelper.isNullOrEmpty(name)) {
					right = right.replace("@" + name,
							" parseFloat( document.forms[0]." + mytb.getId()
									+ ".value.replace( ',' ,  '' ) ) ");
				}

				setHtmlByCtrl(mytb);
			}

			int myDataType = BP.DA.DataType.AppMoney;

			// 判断类型
			for (MapAttr attr : mattrs.ToJavaList()) {
				if (attr.getKeyOfEn().equals(//ToLowerCase
						ext.getAttrOfOper())) {//ToLowerCase
					myDataType = attr.getMyDataType();
				}
			}

			js.append("\t\n function Auto" + ext.getAttrOfOper()//ToLowerCase
					+ "() { ");
			js.append(left).append(right).append(";");
			if (myDataType == BP.DA.DataType.AppFloat
					|| myDataType == BP.DA.DataType.AppDouble) {
				js.append(" \t\n  document.forms[0].").append(tb.getId())
						.append(".value= VirtyMoney(document.forms[0].")
						.append(tb.getId()).append(".value);");
			} else {
				js.append(" \t\n  document.forms[0].").append(tb.getId())
						.append(".value= document.forms[0].")
						.append(tb.getId()).append(".value;");
			}
			js.append("\t\n } ");
			js.append("\t\n</script>");
			scriptsBlock.append(js.toString());
		}
		// endregion
	}

	public final void InsertObjects(boolean isJudgeRowIdx) {

		// region 从表
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (!dtl.getIsView() || ctrlUseSta.contains(dtl.getNo())) {
				continue;
			}

			if (dtl.getGroupID() == 0) {
				dtl.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				dtl.setRowIdx(0);
				dtl.Update();
			}

			if (isJudgeRowIdx) {
				if (dtl.getRowIdx() != rowIdx) {
					continue;
				}
			}

			if (dtl.getGroupID() == currGF.getOID()) {

			} else {
				continue;
			}

			// dtl.IsUse = true;

			ctrlUseSta += dtl.getNo();

			rowIdx++;
			// myidx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
					+ dtl.getNo()
					+ "' height='50px' width='100%' style='align:left'>");
			StringBuilder src = new StringBuilder();
			try {
				src.append(basePath).append("WF/CCForm/Dtl2.jsp?EnsName=")
						.append(dtl.getNo()).append("&RefPKVal=")
						.append(HisEn.getPKVal()).append("&FID=")
						.append(HisEn.GetValStringByKey("FID"))
						.append("&IsWap=0&FK_Node=")
						.append(dtl.getFK_MapData().replace("ND", ""));
			} catch (java.lang.Exception e) {
				src.append(basePath).append("WF/CCForm/Dtl2.jsp?EnsName=")
						.append(dtl.getNo()).append("&RefPKVal=")
						.append(HisEn.getPKVal()).append("&IsWap=0&FK_Node=")
						.append(dtl.getFK_MapData().replace("ND", ""));
			}

			if (getIsReadonly() || dtl.getIsReadonly()) {
				appendPub("<iframe ID='F"
						+ dtl.getNo()
						+ "'  src='"
						+ src
						+ "&IsReadonly=1' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='30px' /></iframe>");
			} else {
				// Add("<iframe ID='F" + dtl.No + "'   Onblur=\"SaveDtl('" +
				// dtl.No + "');\"  src='" + src +
				// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' /></iframe>");

				AddLoadFunction(dtl.getNo(), "blur", "SaveDtl");

				appendPub("<iframe ID='F"
						+ dtl.getNo()
						+ "'   onload='"
						+ dtl.getNo()
						+ "load();'  src='"
						+ src
						+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' /></iframe>");

			}

			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}
		// endregion 从表

		// region 多对多的关系
		for (MapM2M m2m : m2ms.ToJavaList()) {
			if (ctrlUseSta.contains("@" + m2m.getMyPK())) {
				continue;
			}

			if (isJudgeRowIdx) {
				if (m2m.getRowIdx() != rowIdx) {
					continue;
				}
			}

			if (m2m.getGroupID() == 0 && rowIdx == 0) {
				m2m.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				m2m.setRowIdx(0);
				m2m.Update();
			} else if (m2m.getGroupID() == currGF.getOID()) {

			} else {
				continue;
			}

			ctrlUseSta += "@" + m2m.getMyPK();

			rowIdx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));

			StringBuilder src = new StringBuilder();
			src.append(basePath).append("WF/CCForm/M2M.jsp?NoOfObj=")
					.append(m2m.getNoOfObj());
			StringBuilder paras = new StringBuilder(paramsStr);
			if (!paras.toString().contains("FID=")) {
				paras.append("&FID=").append(HisEn.GetValStrByKey("FID"));
			}

			if (!paras.toString().contains("OID=")) {
				paras.append("&OID=").append(HisEn.GetValStrByKey("OID"));
			}

			src.append("&r=q").append(paras.toString());
			if (!src.toString().contains("FK_MapData")) {
				src.append("&FK_MapData=").append(m2m.getFK_MapData());
			}
			switch (m2m.getShowWay()) {
			case FrmAutoSize:
				appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
						+ m2m.getNoOfObj() + "' height='20px' width='100%'  >");
				if (m2m.getHisM2MType().equals(M2MType.M2M)) {

					AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

					// Add("<iframe ID='F" + m2m.NoOfObj +
					// "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" +
					// src +
					// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'  onload='"
							+ m2m.getNoOfObj()
							+ "load();'   src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");

				} else {
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "' src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");
				}
				break;
			case FrmSpecSize:
				appendPub("<TD colspan=" + mapData.getTableCol() + "  ID='TD"
						+ m2m.getNoOfObj() + "' height='" + m2m.getH()
						+ "' width='" + m2m.getW() + "'  >");
				if (m2m.getHisM2MType().equals(M2MType.M2M)) {
					AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

					// appendPub("<iframe ID='F" + m2m.NoOfObj +
					// "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" +
					// src +
					// "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
					// + m2m.W + "' height='" + m2m.H +
					// "' scrolling=auto /></iframe>");
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "' onload='"
							+ m2m.getNoOfObj()
							+ "load();'   src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ m2m.getW() + "' height='" + m2m.getH()
							+ "' scrolling=auto /></iframe>");

				} else {
					appendPub("<iframe ID='F"
							+ m2m.getNoOfObj()
							+ "'    src='"
							+ src
							+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
							+ m2m.getW() + "' height='" + m2m.getH()
							+ "' scrolling=auto /></iframe>");
				}
				break;
			case Hidden:
				break;
			case WinOpen:
				appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
						+ m2m.getNoOfObj() + "' height='20px' width='100%'  >");
				appendPub("<a href=\"javascript:WinOpen('" + src
						+ "&IsOpen=1','" + m2m.getW() + "','" + m2m.getH()
						+ "');\"  />" + m2m.getName() + "</a>");
				break;
			default:
				break;
			}
		}
		// endregion 多对多的关系

		// region 框架
		for (MapFrame fram : frames.ToJavaList()) {
			if (ctrlUseSta.contains("@" + fram.getMyPK())) {
				continue;
			}

			if (isJudgeRowIdx) {
				if (fram.getRowIdx() != rowIdx) {
					continue;
				}
			}

		 

			ctrlUseSta += "@" + fram.getMyPK();
			rowIdx++;
			// myidx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			if (fram.getIsAutoSize()) {
				appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
						+ fram.getNoOfObj() + "' height='50px' width='100%'  >");
			} else {
				appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
						+ fram.getNoOfObj() + "' height='" + fram.getH()
						+ "' width='" + fram.getW() + "'  >");
			}

			String paras = paramsStr;
			if (!paras.contains("FID=")) {
				paras += "&FID=" + HisEn.GetValStrByKey("FID");
			}

			if (!paras.contains("WorkID=")) {
				paras += "&WorkID=" + HisEn.GetValStrByKey("OID");
			}

			String src = fram.getURL();
			if (src.contains("?")) {
				src += "&r=q" + paras;
			} else {
				src += "?r=q" + paras;
			}

			if (fram.getIsAutoSize()) {
				appendPub("<iframe ID='F"
						+ fram.getNoOfObj()
						+ "'   src='"
						+ src
						+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=auto /></iframe>");
			} else {
				appendPub("<iframe ID='F"
						+ fram.getNoOfObj()
						+ "'   src='"
						+ src
						+ "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='"
						+ fram.getW() + "' height='" + fram.getH()
						+ "' scrolling=auto /></iframe>");
			}

			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}
		// endregion 框架

		// region 附件
		for (FrmAttachment ath : aths.ToJavaList()) {
			if (ctrlUseSta.contains("@" + ath.getMyPK())) {
				continue;
			}
			if (isJudgeRowIdx) {
				if (ath.getRowIdx() != rowIdx) {
					continue;
				}
			}

			if (ath.getGroupID() == 0 && rowIdx == 0) {
				ath.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				ath.setRowIdx(0);
				ath.Update();
			} else if (ath.getGroupID() == currGF.getOID()) {
			} else {
				continue;
			}
			ctrlUseSta += "@" + ath.getMyPK();
			rowIdx++;
			// myidx++;
			appendPub(AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			appendPub("<TD colspan=" + mapData.getTableCol() + " ID='TD"
					+ ath.getMyPK()
					+ "' height='50px' width='100%' style='align:left'>");
			StringBuilder src = new StringBuilder();
			if (getIsReadonly()) {
				src.append(basePath)
						.append("WF/CCForm/AttachmentUpload.jsp?PKVal=")
						.append(HisEn.getPKVal()).append("&Ath=")
						.append(ath.getNoOfObj()).append("&FK_MapData=")
						.append(enName).append("&FK_FrmAttachment=")
						.append(ath.getMyPK()).append("&IsReadonly=1")
						.append(paramsStr);
			} else {
				src.append(basePath)
						.append("WF/CCForm/AttachmentUpload.jsp?PKVal=")
						.append(HisEn.getPKVal()).append("&Ath=")
						.append(ath.getNoOfObj()).append("&FK_MapData=")
						.append(enName).append("&FK_FrmAttachment=")
						.append(ath.getMyPK()).append(paramsStr);
			}

			StringBuilder iframeBuilder = new StringBuilder();
			if (ath.getIsAutoSize()) {
				iframeBuilder
						.append("<iframe ID='F")
						.append(ath.getMyPK())
						.append("'   src='")
						.append(src.toString())
						.append("' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=auto /></iframe>");
			} else {
				iframeBuilder
						.append("<iframe ID='F")
						.append(ath.getMyPK())
						.append("'   src='")
						.append(src.toString())
						.append("' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='")
						.append(ath.getW()).append("' height='")
						.append(ath.getH())
						.append("' scrolling=auto /></iframe>");
			}
			appendPub(iframeBuilder.toString());
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}
		// endregion 附件

	}

	/**
	 * 添加富文本
	 * 
	 * @param en
	 * @param attr
	 */
	public final void AddRichTextBox(Entity en, MapAttr attr) {
		// 说明这是富文本输出
		scripts.add("WF/Comm/kindeditor/kindeditor-all.js");
		scripts.add("WF/Comm/kindeditor/lang/zh_CN.js");
		scripts.add("WF/Comm/kindeditor/plugins/code/prettify.js");
		csslinks.add("WF/Comm/kindeditor/plugins/code/prettify.css");
		csslinks.add("WF/Comm/kindeditor/themes/default/default.css");

		TextBox tbd = new TextBox();
		tbd.setTextMode(TextBoxMode.MultiLine);
		tbd.setId("TB_" + attr.getKeyOfEn());//ToLowerCase
		tbd.setName("TB_" + attr.getKeyOfEn());//ToLowerCase
		tbd.setText(htmlspecialchars(en.GetValStrByKey(attr.getKeyOfEn())));
		tbd.attributes.put("style", "width:" + attr.getUIWidth() + "px;height:"
				+ attr.getUIHeight() + "px;visibility:hidden;");
		appendPub(tbd.toString());

		StringBuilder strs = new StringBuilder("\t\n <script>");
		strs.append("\t\n var editor1; ");
		strs.append("\t\n KindEditor.ready(function(K) {");

		strs.append("\t\n var tbID='TB_").append(attr.getKeyOfEn())//ToLowerCase
				.append("'; ");

		strs.append("\t\n var ctrl =document.getElementById( tbID);");

		strs.append("\t\n if (ctrl == null) { ");
		strs.append("\t\n     alert('没有找到要帮定的控件'); ");
		strs.append("\t\n  } ");

		strs.append("\t\n  editor1 = K.create('#'+tbID, {");
		strs.append("\t\n  afterBlur: function(){ ");
		strs.append("\t\n  this.sync(); ");
		strs.append("\t\n  }, ");
		strs.append("\t\n cssPath : '").append(basePath)
				.append("WF/Comm/kindeditor/plugins/code/prettify.css',");
		strs.append("\t\n uploadJson : '").append(basePath)
				.append("WF/Comm/kindeditor/jsp/upload_json.jsp',");
		strs.append("\t\n fileManagerJson : '").append(basePath)
				.append("WF/Comm/kindeditor/jsp/file_manager_json.jsp',");
		strs.append("\t\n allowFileManager : true,");

		strs.append("\t\n width : '100%',");
		// strs.append("\t\n width : '" + attr.UIWidth + "px',";

		strs.append("\t\n height : '" + attr.getUIHeight() + "px'");

		strs.append("\t\n });");
		strs.append("\t\n });");

		// strs.append("\t\n KindEditor.show(function(K) {";
		// strs.append("\t\n KindEditor.ready(function(K) {";

		strs.append("\t\n </script>");
		scriptsBlock.append(strs.toString());

	}

	private String htmlspecialchars(String str) {
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	private String decimalFormat(Object object) {
		if (object == null || "".equals(object.toString())) {
			object = 0.00;
		}
		String str = new DecimalFormat("#0.00").format(Double
				.parseDouble(object.toString()));

		return str;
	}

	/**
	 * 修改替换指定 view
	 * 
	 * @param control
	 */
	private void setHtmlByCtrl(BaseWebControl control) {
		String replacedHtml = HtmlUtils.setCtrlHtml(control, Pub.toString());
		Pub = new StringBuilder(replacedHtml);
	}
}
