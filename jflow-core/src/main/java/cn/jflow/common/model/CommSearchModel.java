package cn.jflow.common.model;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.ToolBar;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrSearch;
import BP.En.AttrSearchs;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

public class CommSearchModel extends BaseModel {

	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();

	private Entity HisEn;
	private ToolBar toolBar1;
	private Label label1 = new Label();
	private StringBuilder UCSys1 = new StringBuilder();
	private StringBuilder UCSys2;

	public CommSearchModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		this.request.set(request);
		this.response.set(response);
	}

	public HttpServletRequest getRequest() {
		return request.get();
	}

	public HttpServletResponse getResponse() {
		return response.get();
	}

	public void init() {
		Entity en = getHisEn();
		Map map = en.getEnMap();
		// #region 设置toolbar2 的 contral 设置查寻功能.
		toolBar1 = new ToolBar(getRequest(), getResponse());
		toolBar1.InitByMapV2(map, 1);
		// toolBar1.AddSpt("spt2");
		// toolBar1.AddLinkBtn(true, NamesOfBtn.Excel.getCode());
		boolean isEdit = true;
		if (getIsReadonly())
			isEdit = false;
		if (!getHisEn().getHisUAC().IsInsert)
			isEdit = false;
		if (isEdit) {
			int h =0; // BP.Sys.EnsAppCfgs.GetValInt(getEnsName(), "WinCardH");
			if (h == 0) {
				h = 500;
			}
			int w =0; // BP.Sys.EnsAppCfgs.GetValInt(getEnsName(), "WinCardW");
			if (w == 0) {
				w = 800;
			}
			/*toolBar1.AddLab(
					"inse",
					"<input type=button id='Btn_New' class=Btn name='Btn_New' "
							+ "onclick=\"javascript:ShowEn('./RefFunc/UIEn.jsp?EnsName="
							+ getEnsName() + "','cd','" + h + "' , '" + w
							+ "');\"  value='新建(N)'  />");*/
		
			String js = "ShowEn('./RefFunc/UIEn.jsp?EnsName=" + this.getEnsName() + "','cd','" + h + "' , '" +w + "');";
			toolBar1.AddLinkBtn(NamesOfBtn.New, "新建", js);


		}

		if ("admin".equals(WebUser.getNo()))
			/*toolBar1.AddLab(
					"sw",
					"<input type=button class=Btn  id='Btn_P' name='Btn_P' onclick=\"javascript:OpenAttrs('"
							+ getEnsName() + "');\" value='设置(P)'/>");*/
			toolBar1.AddLinkBtn(NamesOfBtn.Setting, "设置", "javascript:OpenAttrs('" + this.getEnsName() + "');");
		
		// #endregion
		// #region 设置选择的 默认值
		AttrSearchs searchs = map.getSearchAttrs();
		for (AttrSearch attr : searchs) {
			String mykey = getRequest().getParameter(attr.Key);
			if (mykey == null || "".equals(mykey))
				continue;
			else
				toolBar1.GetDDLByKey("DDL_" + attr.Key).SetSelectItem(mykey,
						attr.HisAttr);
		}

		if (getRequest().getParameter("Key") != null) {
			toolBar1.GetTBByID("TB_Key").setText(
					getRequest().getParameter("Key"));
		}
		// #endregion

		this.SetDGData();
		toolBar1.GetLinkBtnByID(NamesOfBtn.Search).setHref("Search();");
		if (null == toolBar1.GetLinkBtnByID(NamesOfBtn.Export)) {
			toolBar1.AddLinkBtn(true, NamesOfBtn.Export.getCode(),
					NamesOfBtn.Export.getDesc());
		}
		toolBar1.GetLinkBtnByID(NamesOfBtn.Export).addAttr("onclick",
				"DoExp();");
		label1.setId("Label1");
		label1.setName("Label1");
		label1.setText(GenerCaption(this.HisEn.getEnMap().getEnDesc() + ""
				+ StringHelper.isEmpty(this.HisEn.getEnMap().TitleExt, "")));
	}

	public String getToolBar() {
		return toolBar1.toString();
	}

	public String getLable1() {
		return label1.toString();
	}

	public String getUCSys1() {
		return UCSys1.toString();
	}

	public String getUCSys2() {
		if (null == UCSys2) {
			return "";
		}
		return UCSys2.toString();
	}

	private void SetDGData() {
		SetDGData(getPageIdx());
	}

	private void SetDGData(int pageIdx) {
		Entities ens = this.getHisEns();
		Entity en = ens.getGetNewEntity();
		QueryObject qo = new QueryObject(ens);
		qo = this.toolBar1.GetnQueryObject(ens, en);
		// 导出
		if (getDoType().equals("Exp")) {
			/* 如果是导出，就把它导出到excel. */
			try {
				String httpFilePath = ExportDGToExcel(qo.DoQueryToTable(),
						en.getEnMap(), en.getEnDesc());
				ContextHolderUtils.getResponse().getWriter().write(httpFilePath);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		int maxPageNum = 0;
		try {
			this.UCSys2 = new StringBuilder();
			maxPageNum = BindPageIdx(UCSys2, qo.GetCount(),
					SystemConfig.getPageSize(), pageIdx, "Search.jsp?EnsName="
							+ this.getEnsName());
			if (maxPageNum > 1)
				this.UCSys2.append("翻页键:← → PageUp PageDown");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				en.CheckPhysicsTable();
			} catch (Exception wx) {
				wx.printStackTrace();
				BP.DA.Log.DefaultLogWriteLineError(wx.getMessage());
			}
			maxPageNum = BindPageIdx(UCSys2, qo.GetCount(),
					SystemConfig.getPageSize(), pageIdx, "Search.jsp?EnsName="
							+ this.getEnsName());
		}

		qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), pageIdx);

		if (en.getEnMap().IsShowSearchKey) {
			String keyVal = this.toolBar1.GetTBByID("TB_Key").getText().trim();
			if (keyVal.length() >= 1) {
				Attrs attrs = en.getEnMap().getAttrs();
				for (Object obj : ens.subList(0, ens.size())) {
					for (Attr attr : attrs) {
						if (attr.getIsFKorEnum())
							continue;

						if (attr.getIsPK())
							continue;

						switch (attr.getMyDataType()) {
						case DataType.AppRate:
						case DataType.AppMoney:
						case DataType.AppInt:
						case DataType.AppFloat:
						case DataType.AppDouble:
						case DataType.AppBoolean:
							continue;
						default:
							break;
						}
						Entity myen = (Entity) obj;
						myen.SetValByKey(
								attr.getKey(),
								myen.GetValStrByKey(attr.getKey())
										.replace(
												keyVal,
												"<font color=red>" + keyVal
														+ "</font>"));
					}
				}
			}
		}
		DataPanelDtl(UCSys1, ens, null);

		int ToPageIdx = getPageIdx() + 1;
		int PPageIdx = getPageIdx() - 1;

		this.UCSys1.append("<SCRIPT language=javascript>");
		this.UCSys1.append("\t\n document.onkeydown = chang_page;");
		this.UCSys1.append("\t\n function chang_page() { ");
		// this.UCSys3.Add("\t\n  alert(event.keyCode); ");
		if (getPageIdx() == 1) {
			this.UCSys1
					.append("\t\n if (event.keyCode == 37 || event.keyCode == 33) alert('已经是第一页');");
		} else {
			this.UCSys1
					.append("\t\n if (event.keyCode == 37  || event.keyCode == 38 || event.keyCode == 33) ");
			this.UCSys1.append("\t\n     location='Search.jsp?EnsName="
					+ this.getEnsName() + "&PageIdx=" + PPageIdx + "';");
		}

		if (getPageIdx() == maxPageNum) {
			this.UCSys1
					.append("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) alert('已经是最后一页');");
		} else {
			this.UCSys1
					.append("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) ");
			this.UCSys1.append("\t\n     location='Search.jsp?EnsName="
					+ this.getEnsName() + "&PageIdx=" + ToPageIdx + "';");
		}

		this.UCSys1.append("\t\n } ");
		this.UCSys1.append("</SCRIPT>");
	}

	public Entity getHisEn() {
		if (HisEn == null) {
			HisEn = getHisEns().getGetNewEntity();
		}
		return HisEn;
	}

	Entities HisEns;

	public Entities getHisEns() {
		if (getEnsName() != null) {
			if (HisEns == null)
				HisEns = BP.En.ClassFactory.GetEns(getEnsName());
		}
		return HisEns;
	}

	@Override
	public String getEnsName() {
		String ensName = super.getEnsName();
		if (StringHelper.isNullOrEmpty(ensName)) {
			ensName = super.getEnName();
		}

		return ensName;
	}

}
