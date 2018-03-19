package cn.jflow.controller.wf.rpt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ToolBar;

@Controller
@RequestMapping("/WF/Rpt")
public class SearchController extends BaseController {

	public StringBuilder pub;
	private String rptNo = null;
	private String fkFlow = null;
	private Entities HisEns = null;
	private ToolBar toolBar;
	private StringBuilder pub2 = new StringBuilder();
	private StringBuffer UCSys1 = new StringBuffer();
	/**
	 * 导出方法，，重新编写适用于与其他平台项目整合
	 * @param request
	 * @param response    参数
	 * @author peixiaofeng
	 * @date 2016年6月21日
	 */
	@RequestMapping(value = "/ExpExel", method = RequestMethod.POST)
	public void ExpExel(HttpServletRequest request, HttpServletResponse response) {
		java.util.Map controlMap = HtmlUtils.httpParser(request.getParameter("inputValue"), request);
		UiFatory uiFatory = new UiFatory();
		uiFatory.setTmpMap(controlMap);
		
		toolBar = new ToolBar(getRequest(), getResponse(), uiFatory);
	
		Entities ens = this.getHisEns();
		Entity en = ens.getGetNewEntity();
		QueryObject qo = new QueryObject(ens);
		qo = toolBar.GetnQueryObject(ens, en);
		// 导出
		try {
			String httpFilePath = BaseModel.ExportDGToExcel(qo.DoQueryToTable(), en.getEnMap(), en.getEnDesc());
			wirteMsg(getResponse(), httpFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询方法，重新编写适用于与其他平台项目整合
	 * @param object
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年6月21日
	 */
	@RequestMapping(value = "/btn_search", method = RequestMethod.POST)
	public String btn_search(TempObject object, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		rptNo = null;
		Map controlMap = HtmlUtils.httpParser(request.getParameter("inputValue"), request);
		UiFatory uiFatory = new UiFatory();
		uiFatory.setTmpMap(controlMap);
		
		
		toolBar= new ToolBar(getRequest(), getResponse(), uiFatory);
		this.SetDGData(1);
		toolBar.SaveSearchState(rptNo, null);
		StringBuffer htmlStr = new StringBuffer();
		htmlStr.append(UCSys1);
		htmlStr.append(pub2);
		// 转换json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pub",htmlStr.toString());
		try {
			wirteMsg(getResponse(), jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Entities SetDGData(int pageIdx) {
		rptNo = null;
		Entities ens = this.getHisEns();
        Entity en = ens.getGetNewEntity();
		 QueryObject qo = this.toolBar.GetnQueryObject(ens, en);
		// 执行数据分页查询，并绑定分页控件
		BaseModel.BindPageIdxEasyUi(pub2, qo.GetCount(), getPageID() + ".jsp?RptNo="
				+ getRptNo() + "&EnsName=" + getRptNo() + "&FK_Flow="
				+ getFK_Flow(), pageIdx, SystemConfig.getPageSize(),
				"'first','prev','sep','manual','sep','next','last'", false);

		qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), pageIdx);
		// 检查是否显示按关键字查询，如果是就把关键标注为红色.
		if (en.getEnMap().IsShowSearchKey) {
			String keyVal = toolBar.GetTBByID("TB_Key").getText().trim();

			if (keyVal.length() >= 1) {
				Attrs attrs = en.getEnMap().getAttrs();

				for (Object obj : ens.subList(0, ens.size())) {
					Entity myen = (Entity) obj;
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
		BindEns(ens, null);
		return ens;
	}
	public void BindEns(Entities ens, String ctrlId) {
		// #region 定义变量.
		Entity myen = ens.getGetNewEntity();
		Attrs attrs = myen.getEnMap().getAttrs();
		UCSys1.setLength(0);
		// MapData md = new MapData(getRptNo());
		// String pk = myen.getPK();
		// String clName = myen.toString();

		UCSys1.append(BaseModel.AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%;line-height:22px'"));
		// #region 生成表格标题
		UCSys1.append(BaseModel.AddTR());
		UCSys1.append(BaseModel.AddTDGroupTitle2("序"));
		UCSys1.append(BaseModel.AddTDGroupTitle2("标题"));

		for (Attr attr : attrs) {
			if (attr.getIsRefAttr() || "Title".equals(attr.getKey())
					|| "MyNum".equals(attr.getKey()))
				continue;

			UCSys1.append(BaseModel.AddTDGroupTitle2(attr.getDesc()));
		}

		UCSys1.append(BaseModel.AddTREnd());

		// #region 用户界面属性设置
		int pageidx = getPageIdx() - 1;
		int idx = SystemConfig.getPageSize() * pageidx;

		// #region 数据输出.
		for (Object obj : ens.subList(0, ens.size())) {
			// #region 输出字段。
			Entity en = (Entity) obj;
			idx++;
			UCSys1.append(BaseModel.AddTR());
			UCSys1.append(BaseModel.AddTDIdx(idx));
			UCSys1.append(BaseModel.AddTD("<a href=\"javascript:WinOpen('"
					+ Glo.getCCFlowAppPath() + "WF/WFRpt.jsp?FK_Flow="
					+ getFK_Flow() + "&WorkID="
					+ en.GetValStrByKey("OID") + "','tdr');\" >"
					+ en.GetValByKey("Title") + "</a>"));

			for (Attr attr : attrs) {
				String key = attr.getKey();
				if (attr.getIsRefAttr() || "MyNum".equals(key)
						|| "Title".equals(key))
					continue;

				if (attr.getUIContralType() == UIContralType.DDL) {
					String s = en.GetValRefTextByKey(key);
					if (StringHelper.isNullOrEmpty(s)) {
						if ("FK_NY".equals(key)) {
							s = en.GetValStringByKey(key);
						} else {
							s = en.GetValStringByKey(key);
						}
					}
					UCSys1.append(BaseModel.AddTD(s));
					continue;
				}

				String str="";
				str = en.GetValStrByKey(key);
				if(key.equals("XingBie"))
				{
					if(en.GetValStrByKey(key).equals("0"))
					{
						str="男";
					}else
					{
						str="女";
					}
				}

				switch (attr.getMyDataType()) {
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (StringHelper.isNullOrEmpty(str))
						str = "&nbsp;";
					UCSys1.append(BaseModel.AddTD(str));
					break;
				case DataType.AppString:
					if (StringHelper.isNullOrEmpty(str))
						str = "&nbsp;";
					if (attr.getUIHeight() != 0)
						UCSys1.append(BaseModel.AddTDDoc(str, str));
					else
						UCSys1.append(BaseModel.AddTD(str));
					break;
				case DataType.AppBoolean:
					if ("1".equals(str))
						UCSys1.append(BaseModel.AddTD("是"));
					else
						UCSys1.append(BaseModel.AddTD("否"));
					break;
				case DataType.AppFloat:
				case DataType.AppInt:
				case DataType.AppRate:
				case DataType.AppDouble:
					UCSys1.append(BaseModel.AddTDNum(str));
					break;
				case DataType.AppMoney:
					UCSys1.append(BaseModel.AddTDNum((new BigDecimal(str)).setScale(2,
							java.math.BigDecimal.ROUND_HALF_UP).doubleValue()
							+ ""));
					break;
				default:
					try {
						throw new Exception("no this case ...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			UCSys1.append(BaseModel.AddTREnd());
		}
		// #region 计算一下是否可以求出合计,主要是判断是否有数据类型在这个Entities中。
		boolean IsHJ = false;
		for (Attr attr : attrs) {
			String key = attr.getKey();
			if (attr.getMyFieldType() == FieldType.RefText
					|| "Title".equals(key) || "MyNum".equals(key))
				continue;

			if (!attr.getUIVisible())
				continue;

			if (attr.getUIContralType() == UIContralType.DDL)
				continue;

			if ("OID".equals(key) || "MID".equals(key) || "FID".equals(key)
					|| "PWorkID".equals(key)
					|| "WORKID".equals(key.toUpperCase()))
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
		// #region 输出合计。
		if (IsHJ) {
			UCSys1.append("<TR class='Sum' >");
			UCSys1.append(BaseModel.AddTD());
			UCSys1.append(BaseModel.AddTD("合计"));
			for (Attr attr : attrs) {
				String key = attr.getKey();
				if ("MyNum".equals(key))
					continue;

				if (attr.getUIContralType() == UIContralType.DDL) {
					UCSys1.append(BaseModel.AddTD());
					continue;
				}

				if (!attr.getUIVisible())
					continue;

				if ("OID".equals(key) || "MID".equals(key)
						|| "WORKID".equals(key.toUpperCase())
						|| "FID".equals(key)) {
					UCSys1.append(BaseModel.AddTD());
					continue;
				}

				switch (attr.getMyDataType()) {
				case DataType.AppDouble:
					UCSys1.append(BaseModel.AddTDNum(ens.GetSumDecimalByKey(key)));
					break;
				case DataType.AppFloat:
					UCSys1.append(BaseModel.AddTDNum(ens.GetSumDecimalByKey(key)));
					break;
				case DataType.AppInt:
					UCSys1.append(BaseModel.AddTDNum(ens.GetSumDecimalByKey(key)));
					break;
				case DataType.AppMoney:
					UCSys1.append(BaseModel.AddTDJE(ens.GetSumDecimalByKey(key)));
					break;
				default:
					UCSys1.append(BaseModel.AddTD());
					break;
				}
			}
			/* 结束循环 */
			UCSys1.append(BaseModel.AddTD());
			UCSys1.append(BaseModel.AddTREnd());
		}
		UCSys1.append(BaseModel.AddTableEnd());
	}
	public Entities getHisEns() {
		//if (HisEns == null) {
			if (rptNo != null) {
				HisEns = ClassFactory.GetEns(rptNo);
			} else {
				HisEns = ClassFactory.GetEns(getRptNo());
			}
		//}
		return HisEns;
	}

	public String getRptNo() {
		//if (rptNo == null) {
			rptNo = getRequest().getParameter("RptNo");
			if (StringHelper.isNullOrEmpty(rptNo)) {
				rptNo = "ND68MyRpt";
			}
		//}
		return rptNo;
	}

	public String getFkFlow() {
		//if (fkFlow == null) {
			fkFlow = getRequest().getParameter("FK_Flow");
			if (StringHelper.isNullOrEmpty(fkFlow)) {
				fkFlow = "068";
			}
		//}
		return fkFlow;
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
