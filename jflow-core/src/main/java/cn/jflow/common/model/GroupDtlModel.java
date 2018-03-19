package cn.jflow.common.model;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.DTSearchWay;
import BP.Sys.XML.Search;
import BP.Sys.XML.Searchs;
import BP.Tools.StringHelper;

public class GroupDtlModel extends BaseModel {

	private String FK_Dept;
	public StringBuilder Pub1;
	
	public GroupDtlModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuilder();
	}
	
		public final String getRptNo() {
			return StringHelper.isEmpty(getParameter("RptNo"), "");
		}
		public final String getDTFrom() {
			return StringHelper.isEmpty(getParameter("DTFrom"), "");
		}
		public final String getDTTo() {
			return StringHelper.isEmpty(getParameter("DTTo"), "");
		}
		public final String getTBKey() {
			return StringHelper.isEmpty(getParameter("Key"), "");
		}
		public final String getFK_Flow() {
			return StringHelper.isEmpty(getParameter("FK_Flow"), "");
		}

		public final String getFK_Dept() {
			return FK_Dept;
		}
		public final void setFK_Dept(String value) {
			String val = value;
			if (val.equals("all")) {
				return;
			}

			if (this.getFK_Dept() == null) {
				FK_Dept=value;
				return;
			}
			if (this.getFK_Dept().length() > val.length()) {
				return;
			}
			FK_Dept=value;
		}
		
		public  void pageLoad() {
			this.BindData();
		}
		
		public final void BindData() {
			Entities ens = BP.En.ClassFactory.GetEns(getRptNo());
			Entity en = ens.getGetNewEntity();
			QueryObject qo = new QueryObject(ens);
			
			String [] strs=get_request().getQueryString().toString().split("[&]",-1);
//			String [] strs1=get_request().getQueryString().toString().split("[&]",-1);
			Attrs attrs = en.getEnMap().getAttrs();
			for (String str : strs) {
				if (str.indexOf("RptNo") != -1) {
					continue;
				}
				String[] mykey = str.split("[=]", -1);
				String key = mykey[0];

				if (key.equals("OID") || key.equals("MyPK")) {
					continue;
				}

				if (key.equals("FK_Dept")) {
					this.setFK_Dept(mykey[1]);
					continue;
				}

				if (!attrs.Contains(key)) {
					continue;
				}

				qo.AddWhere(mykey[0], mykey[1]);
				qo.addAnd();
			}

			if (!StringHelper.isNullOrEmpty(this.getFK_Dept())&&(StringHelper.isNullOrEmpty(getParameter("FK_Emp"))|| getParameter("FK_Emp").equals("all"))) {
				if (this.getFK_Dept().length() == 2) {
					qo.AddWhere("FK_Dept", " = ", "all");
					qo.addAnd();
				}
				else {
					if (this.getFK_Dept().length() == 8) {
						qo.AddWhere("FK_Dept", " = ", this.getFK_Dept());
					}
					else {
						qo.AddWhere("FK_Dept", " like ", this.getFK_Dept() + "%");
					}
					qo.addAnd();
				}
			}
			qo.AddHD();
			///#region 加上日期时间段.
			Map map = en.getEnMap();
			if (map.DTSearchWay != DTSearchWay.None) {
				String field = en.getEnMap().DTSearchKey;
				qo.addAnd();
				qo.addLeftBracket();
				if (map.DTSearchWay == DTSearchWay.ByDate) {
					qo.AddWhere(field, " >= ", this.getDTFrom() + " 01:01");
					qo.addAnd();
					qo.AddWhere(field, " >= ", this.getDTTo() + " 23:59");
				}
				else {
					qo.AddWhere(field, " >= ", this.getDTFrom());
					qo.addAnd();
					qo.AddWhere(field, " >= ", this.getDTTo());
				}
				qo.addRightBracket();
			}
			qo.DoQuery();
			this.DataPanelDtl(ens, null);
		}
		public void DataPanelDtl(Entities ens, String ctrlId) {
			Entity myen = ens.getGetNewEntity();
//			String pk = myen.getPK();
//			String clName = myen.toString();
			Attrs attrs = myen.getEnMap().getAttrs();
			int attrCount = 0;
			java.util.ArrayList<Attr> visibleAttrs = new java.util.ArrayList<Attr>();
			for (Attr attrT : attrs) {
				if (attrT.getUIVisible() == false) {
					continue;
				}

				if (attrT.getKey().equals("Title") || attrT.getKey().equals("MyNum")) {
					continue;
				}
				attrCount++;
				visibleAttrs.add(attrT);
			}
//			MapRpt md = new MapRpt(this.getRptNo());
			this.Pub1.append(AddTable(" cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDGroupTitle("colspan=" + (attrCount + 2), myen.getEnMap().getEnDesc() + " 记录：" + ens.size() + "条"));
			this.Pub1.append(AddTREnd());
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDGroupTitle("style='text-align:center'", "序"));
			this.Pub1.append(AddTDGroupTitle("标题"));
			int size = visibleAttrs.size();
			for (int i = 0; i < size; i++) {
				Attr attr = visibleAttrs.get(i);
				this.Pub1.append(AddTDGroupTitle(attr.getDesc()));
			}
//			boolean isRefFunc = false;
//			isRefFunc = true;
			int pageidx =getPageIdx() - 1;
			int idx = SystemConfig.getPageSize() * pageidx;
			this.Pub1.append(AddTREnd());
//			String style = WebUser.getStyle();
			for (Entity en : ens.ToJavaListEn()) {
				this.Pub1.append(AddTR());
				idx++;
				this.Pub1.append(AddTDIdx(idx));
				this.Pub1.append("<TD class='TD'><a href=\"javascript:WinOpen('../WorkOpt/OneWork/Track.jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + en.GetValStrByKey("OID") + "');\" ><img src='../Img/Track.png' border=0 />" + en.GetValStrByKey("Title") + "</a></TD>");
				for (Attr attr : visibleAttrs) {
					if (attr.getUIContralType().equals(UIContralType.DDL)) {
						if (attr.getUIBindKey().equals("BP.Pub.NYs")) {
							this.Pub1.append(AddTD(en.GetValStrByKey(attr.getKey())));
						}
						else {
							this.Pub1.append(AddTD(en.GetValRefTextByKey(attr.getKey())));
						}
						continue;
					}

					if (attr.getUIHeight() != 0) {
						this.Pub1.append(AddTDDoc("...", "..."));
						continue;
					}

					String str = en.GetValStrByKey(attr.getKey());
					switch (attr.getMyDataType()) {
						case DataType.AppDate:
						case DataType.AppDateTime:
							if (str.equals("") || str == null) {
								str = "&nbsp;";
							}
							this.Pub1.append(AddTD(str));
							break;
						case DataType.AppString:
							if (str.equals("") || str == null) {
								str = "&nbsp;";
							}
							if (attr.getUIHeight() != 0) {
								this.Pub1.append(AddTDDoc(str, str));
							}
							else {
								this.Pub1.append(AddTD(str));
							}
							break;
						case DataType.AppBoolean:
							if (str.equals("1")) {
								this.Pub1.append(AddTD("是"));
							}
							else {
								this.Pub1.append(AddTD("否"));
							}
							break;
						case DataType.AppFloat:
						case DataType.AppInt:
						case DataType.AppRate:
						case DataType.AppDouble:
							this.Pub1.append(AddTDNum(str));
							break;
						case DataType.AppMoney:
							BigDecimal bd= new BigDecimal(str);
							bd.setScale(2, BigDecimal.ROUND_HALF_UP);
							this.Pub1.append(AddTDNum(bd.toString()));
							break;
						default:
							throw new RuntimeException("no this case ...");
					}
				}

				this.Pub1.append(AddTREnd());
			}
			boolean IsHJ = false;
			for (Attr attr : attrs) {
				if (attr.getMyFieldType() == FieldType.RefText || attr.getUIContralType() == UIContralType.DDL) {
					continue;
				}

				if (attr.getKey().equals("OID") || attr.getKey().equals("FID") || attr.getKey().equals("MID") || attr.getKey().toUpperCase().equals("WORKID") || attr.getKey().equals(BP.WF.Data.NDXRptBaseAttr.FlowEndNode) || attr.getKey().equals(BP.WF.Data.NDXRptBaseAttr.PWorkID)) {
					continue;
				}

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
			if (IsHJ) {
				// 找出配置是不显示合计的列。
				this.Pub1.append("<TR class='TRSum'>");
				this.Pub1.append(AddTD("class=Sum", "合计"));
				this.Pub1.append(AddTD("class=Sum", ""));
				for (Attr attr : attrs) {
					if (attr.getMyFieldType() == FieldType.RefText || attr.getUIVisible() == false || attr.getKey().equals("MyNum")) {
						continue;
					}
					if (attr.getMyDataType() == DataType.AppBoolean) {
						this.Pub1.append(AddTD("class=Sum", ""));
						continue;
					}
					if (attr.getUIContralType() == UIContralType.DDL) {
						this.Pub1.append(AddTD("class=Sum", ""));
						continue;
					}
					if (attr.getKey().equals("OID") || attr.getKey().equals("FID") || attr.getKey().equals("MID") || attr.getKey().toUpperCase().equals("WORKID")) {
						this.Pub1.append(AddTD("class=Sum", ""));
						continue;
					}
					switch (attr.getMyDataType()) {
						case DataType.AppDouble:
							this.Pub1.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
							break;
						case DataType.AppFloat:
							this.Pub1.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
							break;
						case DataType.AppInt:
							this.Pub1.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
							break;
						case DataType.AppMoney:
							this.Pub1.append(AddTDJE(ens.GetSumDecimalByKey(attr.getKey())));
							break;
						default:
							this.Pub1.append(AddTD("class=Sum", ""));
							break;
					}
				}
				this.Pub1.append(AddTREnd());
			}
			this.Pub1.append(AddTableEnd());
		}
		public void DataPanelDtlAdd(Entity en, Attr attr,Searchs cfgs, String url, String cardUrl, String focusField) {
			String cfgurl = "";
			if (attr.getUIContralType() == UIContralType.DDL) {
				this.Pub1.append(AddTD(en.GetValRefTextByKey(attr.getKey())));
				return;
			}

			if (attr.getUIHeight() != 0) {
				this.Pub1.append(AddTDDoc("...", "..."));
				return;
			}

			String str = en.GetValStrByKey(attr.getKey());

			if (focusField.equals(attr.getKey())) {
				str = "<a href=" + cardUrl + ">" + str + "</a>";
			}

			switch (attr.getMyDataType()) {
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (str.equals("") || str == null) {
						str = "&nbsp;";
					}
					this.Pub1.append(AddTD(str));
					break;
				case DataType.AppString:
					if (str.equals("") || str == null) {
						str = "&nbsp;";
					}

					if (attr.getUIHeight() != 0) {
						this.Pub1.append(AddTDDoc(str, str));
					}
					else {
						if (attr.getKey().indexOf("ail") == -1) {
							this.Pub1.append(AddTD(str));
						}
						else {
							this.Pub1.append(AddTD("<a href=\"javascript:mailto:" + str + "\"' >" + str + "</a>"));
						}
					}
					break;
				case DataType.AppBoolean:
					if (str.equals("1")) {
						this.Pub1.append(AddTD("是"));
					}
					else {
						this.Pub1.append(AddTD("否"));
					}
					break;
				case DataType.AppFloat:
				case DataType.AppInt:
				case DataType.AppRate:
				case DataType.AppDouble:
					//for (BP.Sys.Xml.Search pe : cfgs) { 					
					for (Search pe : cfgs.ToJavaList()) {
						if (pe.getAttr() == attr.getKey()) {
							cfgurl = pe.getURL();
							Attrs attrs = en.getEnMap().getAttrs();
							for (Attr attr1 : attrs) {
								cfgurl = cfgurl.replace("@" + attr1.getKey(), en.GetValStringByKey(attr1.getKey()));
							}
							break;
						}
					}
					if (cfgurl.equals("")) {
						this.Pub1.append(AddTDNum(str));
					}
					else {
						cfgurl = cfgurl.replace("@Keys", url);
						this.Pub1.append(AddTDNum("<a href=\"javascript:WinOpen('" + cfgurl + "','dtl1');\" >" + str + "</a>"));
					}
					break;
				case DataType.AppMoney:
					cfgurl = "";
				for (Search pe : cfgs.ToJavaList()) {
						if (pe.getAttr() == attr.getKey()) {
							cfgurl = pe.getURL();
							Attrs attrs = en.getEnMap().getAttrs();
							for (Attr attr2 : attrs) {
								cfgurl = cfgurl.replace("@" + attr2.getKey(), en.GetValStringByKey(attr2.getKey()));
							}
							break;
						}
					}
					if (cfgurl.equals("")) {
						//this.Pub1.AddTDNum(java.math.BigDecimal.Parse(str).ToString("0.00"));
						BigDecimal bd= new BigDecimal(str);
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						this.Pub1.append(AddTDNum(bd.toString()));
					}
					else {
						cfgurl = cfgurl.replace("@Keys", url);
						BigDecimal bd= new BigDecimal(str);
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						this.Pub1.append(AddTDNum("<a href=\"javascript:WinOpen('" + cfgurl + "','dtl1');\" >" + bd.toString() + "</a>"));
					}
					break;
				default:
					throw new RuntimeException("no this case ...");
			}
		}
}
