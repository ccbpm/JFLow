package cn.jflow.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Attr;
import BP.En.AttrSearch;
import BP.En.AttrSearchs;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Template.Bill;
import BP.WF.Template.BillAttr;
import BP.Web.WebUser;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.ToolBar;

public class BillPrintModel extends BaseModel{

	private HttpServletRequest request =null;
	
	private HttpServletResponse response =null;
	
	public ToolBar toolBar1;
	
	public Entities _HisEns = null;
	
	public StringBuilder pub2 = new StringBuilder();
	
	public StringBuilder UCSys1 = new StringBuilder();
	
	public BillPrintModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		this.request = request;
		this.response = response;
		toolBar1 = new ToolBar(request, response);
	}

	public final void pageLoad()
	{
		if (this.getDoType().equals("Print"))
		{
			try {
				Bill b = new Bill(this.getMyPK());
				b.DoOpen();
			} catch (IOException e) {
				Log.DebugWriteError("BillPrintMidel pageLoad()" + e);
			}
			return;
		}

		if (StringHelper.isNullOrEmpty(request.getParameter("PageIdx")))
		{
			PageIdx = "1";
		}
		else
		{
			this.PageIdx = request.getParameter("PageIdx");
		}

		//#region 处理查询设的默认.
		if ("My".equals(this.getDoType()))
		{
			Entity en = this.getHisEns().getGetNewEntity();
			Map map = en.getEnMap();
			AttrSearchs searchs = map.getSearchAttrs();
		}
		else
		{
			//#region 处理查询权限
			Entity en = this.getHisEns().getGetNewEntity();
			Map map = en.getEnMap();
			toolBar1.InitByMapV2(map, 1, this.getEnsName());
			toolBar1.AddBtn(NamesOfBtn.Export.getCode());
			AttrSearchs searchs = map.getSearchAttrs();
			String defVal = "";
			DataTable dt = null;
			for (AttrSearch attr : searchs.toList())
			{
				DDL mydll = toolBar1.GetDDLByKey("DDL_" + attr.Key);
				if (mydll == null)
				{
					continue;
				}
				defVal = mydll.getSelectedItemStringVal();
				mydll.attributes.put("onchange","DDL_mvals_OnChange(this,'" + this.getEnsName() + "','" + attr.Key + "')");
				
				if("FK_Emp".equals(attr.Key))
				{
					dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_Emp FROM WF_Bill WHERE FK_Emp!='' AND FK_Flow='" + this.getFK_Flow() + "' ORDER BY FK_Emp");
					mydll.Items.clear();
					mydll.Items.add(new ListItem("=>打印人", "all"));
					for (DataRow dr : dt.Rows)
					{
						mydll.Items.add(new ListItem(dr.getValue(0).toString(), dr.getValue(0).toString()));
					}
					mydll.SetSelectItem(defVal);
				} else if("FK_NY".equals(attr.Key))
				{
					dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_NY FROM WF_Bill WHERE FK_NY!='' AND FK_Flow='" + this.getFK_Flow() + "' ORDER BY FK_NY");
					mydll.Items.clear();
					mydll.Items.add(new ListItem("=>月份", "all"));
					for (DataRow dr : dt.Rows)
					{
						//  BP.WF.Bill
						mydll.Items.add(new ListItem(dr.getValue(0).toString(), dr.getValue(0).toString()));
					}
					mydll.SetSelectItem(defVal);
				} else if("FlowStarter".equals(attr.Key))
				{
					dt = DBAccess.RunSQLReturnTable("SELECT No,Name FROM WF_Emp WHERE  FK_Dept IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "') AND No IN (SELECT DISTINCT FlowStarter FROM WF_Bill WHERE FlowStarter!='')");
					mydll.Items.clear();
					mydll.Items.add(new ListItem("=>发起人", "all"));
					for (DataRow dr : dt.Rows)
					{
						mydll.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
					}
					mydll.SetSelectItem(defVal);
					mydll.attributes.put("onchange","DDL_mvals_OnChange(this,'ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt','" + attr.Key + "')");
					
				} else if("FK_Dept".equals(attr.Key))
				{
					if (!"admin".equals(WebUser.getNo()))
					{
						dt = DBAccess.RunSQLReturnTable("SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "')");
						if (dt.Rows.size() == 0)
						{
							BP.WF.Port.DeptFlowSearch dfs = new BP.WF.Port.DeptFlowSearch();
							dfs.setFK_Dept(WebUser.getFK_Dept());
							dfs.setFK_Emp(WebUser.getNo());
							dfs.setFK_Flow(this.getFK_Flow());
							dfs.setMyPK(WebUser.getFK_Dept() + "_" + WebUser.getNo() + "_" + this.getFK_Flow());
							dfs.Insert();
							dt = DBAccess.RunSQLReturnTable("SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "')");
						}
						mydll.Items.clear();
						for (DataRow dr : dt.Rows)
						{
							mydll.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
						}
					}

					if (mydll.Items.size() >= 2)
					{
						ListItem liMvals = new ListItem("*多项组合..", "mvals");
						liMvals.attributes.put("style", "color:green");
						liMvals.attributes.put("color", "green");
						liMvals.attributes.put("style", "color:green");
					}
					mydll.SetSelectItem(defVal);
				}
			}
			//#endregion 处理查询权限
			//toolBar1.GetBtnByID("Btn_Search").Click += new System.EventHandler(this.ToolBar1_ButtonClick);
			//toolBar1.GetBtnByID(BP.Web.Controls.NamesOfBtn.Export).Click += new System.EventHandler(this.ToolBar1_ButtonClick);
		}
		//#endregion 处理查询设的默认。

		this.SetDGData();
	}
	
	
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			if (this.getEnsName() != null)
			{
				if (this._HisEns == null)
				{
					_HisEns = BP.En.ClassFactory.GetEns(this.getEnsName());
				}
			}
		}
		return _HisEns;
	}
	
	public final Entities SetDGData()
	{
		return SetDGDataPageIdx(getPageIdx());
	}
	
	public final Entities SetDGDataPageIdx(int pageIdx)
	{
		Entities ens = this.getHisEns();
		Entity en = ens.getGetNewEntity();
		QueryObject qo = new QueryObject(ens);
		if ("My".equals(this.getDoType()))
		{
			qo.AddWhere(BillAttr.FK_Emp, WebUser.getNo());
			qo.addAnd();
			qo.AddWhere(BillAttr.FK_Flow, this.getFK_Flow());

		}
		else
		{
			qo = toolBar1.GetnQueryObject(ens, en);
			qo.addAnd();
			qo.AddWhere(BillAttr.FK_Flow, this.getFK_Flow());
		}

		//this.Pub2.Clear();
		int maxPageNum = BindPageIdx(pub2,qo.GetCount(), SystemConfig.getPageSize(), pageIdx, "Bill.aspx?FK_Flow=" + this.getFK_Flow() + "&DoType=" + this.getDoType());
		if (maxPageNum > 1)
		{
			pub2.append(Add("翻页键:← → PageUp PageDown"));
		}
		qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), pageIdx);
		if ("Dept".equals(this.getDoType()) && en.getEnMap().IsShowSearchKey)
		{
			String keyVal = toolBar1.GetTBByID("TB_Key").getText().trim();
			if (keyVal.length() >= 1)
			{
				Attrs attrs = en.getEnMap().getAttrs();
				for (Entity myen : ens)
				{
					for (Attr attr : attrs.ToJavaList())
					{
						if (attr.getIsFKorEnum())
						{
							continue;
						}
						if (attr.getIsPK())
						{
							continue;
						}
						switch (attr.getMyDataType())
						{
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
						myen.SetValByKey(attr.getKey(), myen.GetValStrByKey(attr.getKey()).replace(keyVal, "<font color=red>" + keyVal + "</font>"));
					}
				}
			}
		}
		BindEns(ens, null);

		//#region 生成js
		if(StringHelper.isNullOrEmpty(PageIdx))
		{
			PageIdx = "0";
		}
		int ToPageIdx = getPageIdx() + 1;
		int PPageIdx = getPageIdx() - 1;
		this.UCSys1.append(Add("<SCRIPT language=javascript>"));
		this.UCSys1.append(Add("\t\n document.onkeydown = chang_page;"));
		this.UCSys1.append(Add("\t\n function chang_page() { "));
		if (getPageIdx() == 1)
		{
			this.UCSys1.append(Add("\t\n if (event.keyCode == 37 || event.keyCode == 33) alert('已经是第一页');"));
		}
		else
		{
			this.UCSys1.append(Add("\t\n if (event.keyCode == 37  || event.keyCode == 38 || event.keyCode == 33) "));
			this.UCSys1.append(Add("\t\n     location='Bill.aspx?DoType=" + this.getDoType() + "&FK_Flow=" + this.getFK_Flow() + "&PageIdx=" + PPageIdx + "';"));
		}

		if (getPageIdx() == maxPageNum)
		{
			this.UCSys1.append(Add("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) alert('已经是最后一页');"));
		}
		else
		{
			this.UCSys1.append(Add("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) "));
			this.UCSys1.append(Add("\t\n     location='Bill.aspx?DoType=" + this.getDoType() + "&FK_Flow=" + this.getFK_Flow() + "&PageIdx=" + ToPageIdx + "';"));
		}

		this.UCSys1.append(Add("\t\n } "));
		this.UCSys1.append(Add("</SCRIPT>"));
		//#endregion 生成js

		return ens;
	}
	
	public final void BindEns(Entities ens, String ctrlId)
	{
		//this.Title = "单据查询";
		//this.UCSys1.Controls.Clear();
		Entity myen = ens.getGetNewEntity();
		String pk = myen.getPK();
		String clName = myen.toString();
		Attrs attrs = myen.getEnMap().getAttrs();

		//#region  生成标题
		this.UCSys1.append(Add("<Table border='1' align=left width='20%' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#C0C0C0'>"));
		this.UCSys1.append(AddTR());
		this.UCSys1.append(AddTDTitle("序"));
		for (Attr attr : attrs)
		{
			if (attr.getIsRefAttr() || attr.getUIVisible() == false || "MyNum".equals(attr.getKey()))
			{
				continue;
			}
			this.UCSys1.append(AddTDTitle(attr.getDesc()));
		}
		
		//#endregion  生成标题
		int pageidx = getPageIdx() - 1;
		int idx = SystemConfig.getPageSize() * pageidx;

		//#region 用户界面属性设置
		String focusField = "Title";
		//#endregion 用户界面属性设置

		//#region 数据输出.
		this.UCSys1.append(AddTDTitle("功能"));
		this.UCSys1.append(AddTREnd());
		for (Entity en : ens)
		{
			//#region 处理keys
			String style = WebUser.getStyle();
			String url = this.GenerEnUrl(en, attrs);
			this.UCSys1.append(AddTR());

			//#region 输出字段。
			idx++;
			this.UCSys1.append(AddTDIdx(idx));
			for (Attr attr : attrs)
			{
				if ("FK_NY".equals(attr.getKey()))
				{
					this.UCSys1.append(AddTD(en.GetValStrByKey(attr.getKey())));
					continue;
				}


				if (attr.getIsRefAttr() || attr.getUIVisible() == false || "MyNum".equals(attr.getKey()))
				{
					continue;
				}

				if (attr.getUIContralType() == UIContralType.DDL)
				{
					this.UCSys1.append(AddTD(en.GetValRefTextByKey(attr.getKey())));
					continue;
				}

				if (attr.getUIHeight() != 0)
				{
					this.UCSys1.append(AddTDDoc("...", "..."));
					return;
				}

				String str = en.GetValStrByKey(attr.getKey());
				if (focusField.equals(attr.getKey()))
				{
					str = "<b><font color='blue' >" + str + "</font></a>";
				}
				switch (attr.getMyDataType())
				{
					case DataType.AppDate:
					case DataType.AppDateTime:
						if (str.equals("") || str == null)
						{
							str = "&nbsp;";
						}
						this.UCSys1.append(AddTD(str));
						break;
					case DataType.AppString:
						if (str.equals("") || str == null)
						{
							str = "&nbsp;";
						}

						if (attr.getUIHeight() != 0)
						{
							this.UCSys1.append(AddTDDoc(str, str));
						}
						else
						{
							this.UCSys1.append(AddTD(str));
						}
						break;
					case DataType.AppBoolean:
						if (str.equals("1"))
						{
							this.UCSys1.append(AddTD("是"));
						}
						else
						{
							this.UCSys1.append(AddTD("否"));
						}
						break;
					case DataType.AppFloat:
					case DataType.AppInt:
					case DataType.AppRate:
					case DataType.AppDouble:
						this.UCSys1.append(AddTDNum(str));
						break;
					case DataType.AppMoney:
						this.UCSys1.append(AddTDNum(String.format(str, "0.00")));
						break;
					default:
						throw new RuntimeException("no this case ...");
				}
			}
			//#endregion 输出字段。

			//相关功能.
			String ext = "";
			ext += "<a href=\"javascript:WinOpen('Bill.aspx?DoType=Print&MyPK=" + en.getPKVal() + "','tdr');\" ><img src='./../Img/book.gif' />打印</a>";
			ext += "-<a href=\"javascript:WinOpen('../WorkOpt/OneWork/ChartTrack.aspx?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + en.GetValStringByKey("WorkID") + "&FID=" + en.GetValStringByKey("FID") + "','tr');\" ><img src='./../Img/track.png' />轨迹图</a>";
			// ext += "-<a href=\"javascript:WinOpen('./../WFRpt.aspx?FK_Flow=" + this.FK_Flow + "&WorkID=" + en.GetValStringByKey("WorkID") + "&FID=" + en.GetValStringByKey("FID") + "','tdr');\" >工作报告</a>";

			this.UCSys1.append(AddTD(ext));
			this.UCSys1.append(AddTREnd());
		}
		//#endregion 数据输出.

		//#region  求合计代码写在这里。
		boolean IsHJ = false;
		for (Attr attr : attrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getUIContralType() == UIContralType.DDL)
			{
				continue;
			}

			if ("OID".equals(attr.getKey()) || "MID".equals(attr.getKey()) || "FID".equals(attr.getKey()) || "WORKID".equals(attr.getKey().toUpperCase()))
			{
				continue;
			}

			switch (attr.getMyDataType())
			{
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

		if (IsHJ)
		{
			this.UCSys1.append(Add("<TR class='TRSum' >"));
			this.UCSys1.append(AddTD("合计"));
			for (Attr attr : attrs)
			{
				if ("MyNum".equals(attr.getKey()))
				{
					continue;
				}

				if (attr.getUIContralType() == UIContralType.DDL)
				{
					this.UCSys1.append(AddTD());
					continue;
				}

				if (attr.getUIVisible() == false)
				{
					continue;
				}

				if ("OID".equals(attr.getKey()) || "MID".equals(attr.getKey()) || "WORKID".equals(attr.getKey().toUpperCase()))
				{
					this.UCSys1.append(AddTD(attr.getKey()));
					continue;
				}

				switch (attr.getMyDataType())
				{
					case DataType.AppDouble:
						this.UCSys1.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
						break;
					case DataType.AppFloat:
						this.UCSys1.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
						break;
					case DataType.AppInt:
						this.UCSys1.append(AddTDNum(ens.GetSumDecimalByKey(attr.getKey())));
						break;
					case DataType.AppMoney:
						this.UCSys1.append(AddTDJE(ens.GetSumDecimalByKey(attr.getKey())));
						break;
					default:
						this.UCSys1.append(AddTD());
						break;
				}
			} //结束循环
			this.UCSys1.append(AddTD());
			this.UCSys1.append(AddTREnd());
		}

		this.UCSys1.append(AddTableEnd());
	}
}
