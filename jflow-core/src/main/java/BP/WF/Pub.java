package BP.WF;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.UIContralType;
import BP.Sys.SystemConfig;
import cn.jflow.common.model.BaseModel;
import cn.jflow.model.designer.ListSelectionMode;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LB;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TB;
import cn.jflow.system.ui.core.TextBoxMode;

public class Pub extends BaseModel{
		/*	///#region 方法
		public final boolean getIsReadonly()
		{
			return (boolean)this.ViewState("IsReadonly");
		}
		public final void setIsReadonly(boolean value)
		{
			ViewState["IsReadonly"] = value;
		}

		public final boolean getIsShowDtl()
		{
			return (boolean)this.ViewState["IsShowDtl"];
		}
		public final void setIsShowDtl(boolean value)
		{
			ViewState["IsShowDtl"] = value;
		}*/
		public boolean IsReadonly = false;
		public boolean IsShowDtl = false;
		public StringBuffer Pub1 = null;
		public StringBuffer Controls = null;
		public String visibility  = "";
	
		public Pub(HttpServletRequest request, HttpServletResponse response) {
			super(request, response);
			Pub1 = new StringBuffer();
			Controls = new StringBuffer();
		}

		//xxx 2016年7月6日
		public final void SetValByKey(String key, String val)
		{
			TB en = new TB();
			en.setId("TB_" + key);
			if (en != null)
			{
				en.setText(val);
			}
		}

		public final Object GetValByKey(String key)
		{
			TB en = new TB();
			en.setId("TB_" + key);
			if (en == null)
			{
				return null;
			}
			return en.getText();
		}
			///#endregion
		
		public final String BindAttrs(Attrs attrs)
		{
			boolean isReadonly = false;
			IsReadonly = false;
			IsShowDtl = false;
			//this.Attributes["visibility"] = "hidden";
			this.visibility = "hidden";
			this.Pub1.append(Add("<table width='100%' id='a1' border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >"));
			boolean isLeft = true;
			Object val = null;
			boolean isAddTR = true;
			for (Attr attr : attrs.ToJavaList())
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}

				if (attr.getKey().equals("MyNum"))
				{
					continue;
				}

				if (isLeft)
				{
					isAddTR = true;
					this.Pub1.append(AddTR());
				}

				val = attr.getDefaultVal();
				if (attr.getUIContralType().equals(UIContralType.TB))
				{
					if (attr.getMyFieldType().equals(FieldType.RefText))
					{
						//xxx 2016年7月6日
						this.SetValByKey(attr.getKey(), val.toString());
						continue;
					}
					else if (attr.getMyFieldType().equals(FieldType.MultiValues))
					{
						// 如果是多值的.
						LB lb = new LB(attr);
						lb.setVisible(true);
						lb.setHeight(128);
						lb.setSelectionMode(ListSelectionMode.Multiple);
						Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
						ens.RetrieveAll();
						this.Controls.append(Add(lb));
					}
					else
					{
						if (attr.getUIVisible() == false)
						{
							//不可见控件
							TB tb = new TB();
							tb.LoadMapAttr(attr);
							tb.setId("TB_" + attr.getKey());
							//tb.Attributes["Visible"] = "false";
							tb.addAttr("Visible", "false");
							this.Controls.append(Add(tb));
							continue;
						}
						else
						{
							if (attr.getUIHeight() != 0)
							{
								TB area = new TB();
								area.LoadMapAttr(attr);
								area.setId("TB_" + attr.getKey());
								area.setText(val.toString());
								area.setRows(8);
								area.setTextMode(TextBoxMode.MultiLine);
								//area.Attributes["onchange"] += "Change('TB_" + attr.getKey() + "');";
								area.addAttr("onchange", "Change('TB_" + attr.getKey() + "');");
								if (isReadonly)
								{
									area.setEnabled(false);
								}
								this.AddContral(attr.getDesc(), area);
							}
							else
							{
								TB tb = new TB();
								tb.LoadMapAttr(attr);

								tb.setId("TB_" + attr.getKey());
								if (isReadonly)
								{
									tb.setEnabled(false);
								}
								switch (attr.getMyDataType())
								{
									case DataType.AppMoney:
										tb.setText(((BigDecimal) val).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
										break;
									default:
										tb.setText(val.toString());
										break;
								}
								//tb.Attributes["width"] = "100%";
								//tb.Attributes["onchange"] += "Change('TB_" + attr.getKey() + "');";
								tb.addAttr("style", "width:100%;");
								tb.addAttr("onchange", "Change('TB_" + attr.getKey() + "');");
								this.AddContral(attr.getDesc(), tb);
							}
						}
					}
				}
				else if (attr.getUIContralType().equals(UIContralType.CheckBok))
				{
					CheckBox cb = new CheckBox();
					if (attr.getDefaultVal().toString().equals("1"))
					{
						cb.setChecked(true);
					}
					else
					{
						cb.setChecked(false);
					}

					if (isReadonly)
					{
						cb.setEnabled(false);
					}
					else
					{
						cb.setEnabled(attr.getUIVisible());
					}

					cb.setId("CB_" + attr.getKey());
					//cb.Attributes["onmousedown"] = "Change('CB_" + attr.getKey() + "')";
					cb.addAttr("onmousedown", "Change('CB_" + attr.getKey() + "')");
					this.AddContral(attr.getDesc(), cb);
				}
				else if (attr.getUIContralType().equals(UIContralType.DDL))
				{
					if (isReadonly || !attr.getUIIsReadonly())
					{
						// 如果是 DDLIsEnable 的, 就要找到. 
						if (attr.getMyFieldType().equals(FieldType.Enum))
						{
							// 如果是 enum 类型 
							int enumKey = 0;
							try
							{
								enumKey = Integer.parseInt(val.toString());
							}
							catch (java.lang.Exception e)
							{
								throw new RuntimeException("默认值错误：" + attr.getKey() + " = " + val.toString());
							}

							BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);

							//DDL ddl = new DDL(attr,text,en.Lab,false);
							DDL ddl = new DDL();
							ddl.Items.add(new ListItem(enEnum.getLab(), val.toString()));
							ddl.Items.get(0).setSelected(true);
							ddl.setEnabled(false);
							ddl.setId("DDL_" + attr.getKey());

							this.AddContral(attr.getDesc(), ddl, true);
						}
						else
						{
							// 如果是 ens 类型 
							Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
							Entity en1 = ens.getGetNewEntity();
							en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
							String lab = "";
							try
							{
								en1.Retrieve();
								lab = en1.GetValStringByKey(attr.getUIRefKeyText());
							}
							catch (java.lang.Exception e2)
							{
								if (SystemConfig.getIsDebug() == false)
								{
									lab = "" + val.toString();
								}
								else
								{
									lab = "" + val.toString();
								}
							}

							DDL ddl = new DDL(attr, val.toString(), lab, false, getBasePath());

							ddl.setId("DDL_" + attr.getKey());
							this.AddContral(attr.getDesc(), ddl, true);
						}
					}
					else
					{
						// 可以使用的情况. 
						DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, getBasePath());
						ddl1.setId("DDL_" + attr.getKey());
						//ddl1.Attributes["onchange"] = "Change('DDL_" + attr.getKey() + "')";
						ddl1.addAttr("onchange", "Change('DDL_" + attr.getKey() + "')");
						this.AddContral(attr.getDesc(), ddl1, true);
					}
				}
				else if (attr.getUIContralType().equals(UIContralType.RadioBtn))
				{
					//					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
					//					foreach(SysEnum en in enums)
					//					{
					//						return ;
					//					}
				}

				if (isLeft == false)
				{
					isAddTR = false;
					this.Pub1.append(AddTREnd());
				}

				isLeft = !isLeft;
			} // 结束循环.
			//补充TR
			if (isAddTR == true)
			{
				this.Pub1.append(AddTD(""));
				this.Pub1.append(AddTD(""));
				this.Pub1.append(AddTREnd());
			}
			this.Pub1.append("</TABLE>");
			return this.Pub1.toString();
		}

		/** 
		 填充数据
		 
		 @param attrs
		 @param pa
		*/
		public final String BindAttrs(Attrs attrs, AtPara pa)
		{
			boolean isReadonly = false;
			this.setIsReadonly(false);
			this.setIsShowDtl(false);
			//this.Attributes["visibility"] = "hidden";
			this.visibility = "hidden";
			this.Pub1.append("<table width='100%' id='a1' border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
			boolean isLeft = true;
			Object val = null;
			boolean isAddTR = true;
			for (Attr attr : attrs)
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}

				if (attr.getKey().equals("MyNum"))
				{
					continue;
				}

				if (isLeft)
				{
					isAddTR = true;
					this.Pub1.append(AddTR());
				}

				val = pa.GetValStrByKey(attr.getKey());
				if (attr.getUIContralType() == UIContralType.TB)
				{
					if (attr.getMyFieldType() == FieldType.RefText)
					{
						//xxx 2016年7月6日
						this.SetValByKey(attr.getKey(), val.toString());
						continue;
					}
					else if (attr.getMyFieldType() == FieldType.MultiValues)
					{
						// 如果是多值的.
						LB lb = new LB(attr);
						lb.setVisible(true);
						lb.setHeight(128);
						lb.setSelectionMode(ListSelectionMode.Multiple);
						Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
						ens.RetrieveAll();
						this.Controls.append(Add(lb));
					}
					else
					{
						if (attr.getUIVisible() == false)
						{

							TB tb = new TB();
							tb.LoadMapAttr(attr);
							tb.setId("TB_" + attr.getKey());
							//tb.Attributes["Visible"] = "false";
							tb.addAttr("Visible", "false");
							this.Controls.append(Add(tb));
							continue;
						}
						else
						{
							if (attr.getUIHeight() != 0)
							{
								TB area = new TB();
								area.LoadMapAttr(attr);
								area.setId("TB_" + attr.getKey());
								area.setText(val.toString());
								area.setRows(8);
								area.setTextMode(TextBoxMode.MultiLine);
								//area.Attributes["onchange"] += "Change('TB_" + attr.getKey() + "');";
								area.addAttr("onchange", "Change('TB_" + attr.getKey() + "');");
								if (isReadonly)
								{
									area.setEnabled(false);
								}
								this.AddContral(attr.getDesc(), area);
							}
							else
							{
								TB tb = new TB();
								tb.LoadMapAttr(attr);

								tb.setId("TB_" + attr.getKey());
								if (isReadonly)
								{
									tb.setEnabled(false);
								}
								switch (attr.getMyDataType())
								{
									case DataType.AppMoney:
										tb.setText(((BigDecimal) val).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
										break;
									default:
										tb.setText(val.toString());
										break;
								}
								//tb.Attributes["width"] = "100%";
								tb.addAttr("style", "100%;");
								//tb.Attributes["onchange"] += "Change('TB_" + attr.getKey() + "');";
								tb.addAttr("onchange", "Change('TB_" + attr.getKey() + "');");
								this.AddContral(attr.getDesc(), tb);
							}
						}
					}
				}
				else if (attr.getUIContralType() == UIContralType.CheckBok)
				{
					val = pa.GetValBoolenByKey(attr.getKey());
					CheckBox cb = new CheckBox();
					cb.setChecked(((Boolean)val).booleanValue());

					if (isReadonly)
					{
						cb.setEnabled(false);
					}
					else
					{
						cb.setEnabled(attr.getUIVisible());
						//cb.Attributes["onmousedown"] = "Change('CB_" + attr.getKey() + "')";
						cb.addAttr("onmousedown", "Change('CB_" + attr.getKey() + "')");
					}

					cb.setId("CB_" + attr.getKey());
					this.AddContral(attr.getDesc(), cb);
				}
				else if (attr.getUIContralType() == UIContralType.DDL)
				{
					if (isReadonly || !attr.getUIIsReadonly())
					{
						// 如果是 DDLIsEnable 的, 就要找到. 
						if (attr.getMyFieldType() == FieldType.Enum)
						{
							// 如果是 enum 类型 
							int enumKey = 0;
							try
							{
								enumKey = Integer.parseInt(val.toString());
							}
							catch (java.lang.Exception e)
							{
								throw new RuntimeException("默认值错误：" + attr.getKey() + " = " + val.toString());
							}

							BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);

							//DDL ddl = new DDL(attr,text,en.Lab,false);
							DDL ddl = new DDL();
							ddl.Items.add(new ListItem(enEnum.getLab(), val.toString()));
							ddl.Items.get(0).setSelected(true);
							ddl.setEnabled(false);
							ddl.setId("DDL_" + attr.getKey());

							this.AddContral(attr.getDesc(), ddl, true);
						}
						else
						{
							// 如果是 ens 类型 
							Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
							Entity en1 = ens.getGetNewEntity();
							en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
							String lab = "";
							try
							{
								en1.Retrieve();
								lab = en1.GetValStringByKey(attr.getUIRefKeyText());
							}
							catch (java.lang.Exception e2)
							{
								if (SystemConfig.getIsDebug() == false)
								{
									lab = "" + val.toString();
								}
								else
								{
									lab = "" + val.toString();
								}
							}

							DDL ddl = new DDL(attr, val.toString(), lab, false, getBasePath());

							ddl.setId("DDL_" + attr.getKey());
							this.AddContral(attr.getDesc(), ddl, true);
						}
					}
					else
					{
						// 可以使用的情况. 
						DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, getBasePath());
						ddl1.setId("DDL_" + attr.getKey());
						//ddl1.Attributes["onchange"] = "Change('DDL_" + attr.getKey() + "')";
						ddl1.addAttr("onchange", "Change('DDL_" + attr.getKey() + "')");
						this.AddContral(attr.getDesc(), ddl1, true);
					}
				}
				else if (attr.getUIContralType() == UIContralType.RadioBtn)
				{
					//					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
					//					foreach(SysEnum en in enums)
					//					{
					//						return ;
					//					}
				}

				if (isLeft == false)
				{
					isAddTR = false;
					this.Pub1.append(AddTREnd());
				}

				isLeft = !isLeft;
			} // 结束循环.
			//补充TR
			if (isAddTR == true)
			{
				this.Pub1.append(AddTD(""));
				this.Pub1.append(AddTD(""));
				this.Pub1.append(AddTREnd());
			}
			this.Pub1.append("</TABLE>");
			return this.Pub1.toString();
		}

		/** 
		 绑定数据返回只读，不包含TB控件
		 
		 @param attrs
		 @param pa
		*/
		public final String BindAttrsForHtml(Attrs attrs, AtPara pa)
		{
			this.Pub1.append("<table width='100%' id='a1' border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
			boolean isLeft = true;
			Object val = null;
			boolean isAddTR = true;
			for (Attr attr : attrs)
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}

				if (attr.getKey().equals("MyNum"))
				{
					continue;
				}

				if (isLeft)
				{
					isAddTR = true;
					this.Pub1.append(AddTR());
				}

				val = pa.GetValStrByKey(attr.getKey());
				if (attr.getUIContralType() == UIContralType.TB)
				{
					if (attr.getMyFieldType() == FieldType.RefText)
					{
						//this.SetValByKey(attr.Key, val.ToString());
						continue;
					}
					else if (attr.getMyFieldType() == FieldType.MultiValues)
					{
						// 如果是多值的.
						LB lb = new LB(attr);
						lb.setVisible(true);
						lb.setHeight(128);
						lb.setSelectionMode(ListSelectionMode.Multiple);
						Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
						ens.RetrieveAll();
						this.Controls.append(Add(lb));
					}
					else
					{
						if (attr.getUIVisible() == false)
						{
							continue;
						}
						else
						{
							switch (attr.getMyDataType())
							{
								case DataType.AppMoney:
									val = ((BigDecimal) val).setScale(2, BigDecimal.ROUND_HALF_UP);
									break;
								default:
									val = val.toString();
									break;
							}

							this.Pub1.append("<td class='FDesc' nowrap width=1% > " + attr.getDesc() + "</td>");
							this.Pub1.append("<td class='TD' nowrap width='30%'>");
							this.Pub1.append(val.toString());
							this.Pub1.append("</td>");
						}
					}
				}
				else if (attr.getUIContralType() == UIContralType.CheckBok)
				{
					CheckBox cb = new CheckBox();
					if (attr.getDefaultVal().toString().equals("1"))
					{
						cb.setChecked(true);
					}
					else
					{
						cb.setChecked(false);
					}

					cb.setId("CB_RD_" + attr.getKey());
					this.AddContral(attr.getDesc(), cb);
				}
				else if (attr.getUIContralType() == UIContralType.DDL)
				{
					// 如果是 DDLIsEnable 的, 就要找到. 
					if (attr.getMyFieldType() == FieldType.Enum)
					{
						// 如果是 enum 类型 
						int enumKey = 0;
						try
						{
							enumKey = Integer.parseInt(val.toString());
						}
						catch (java.lang.Exception e)
						{
							throw new RuntimeException("默认值错误：" + attr.getKey() + " = " + val.toString());
						}

						BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);

						this.Pub1.append("<td class='FDesc' nowrap width=1% > " + attr.getDesc() + "</td>");
						this.Pub1.append("<td class='TD' nowrap width='30%'>");
						this.Pub1.append(enEnum.getLab());
						this.Pub1.append("</td>");
					}
					else
					{
						// 如果是 ens 类型 
						Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
						Entity en1 = ens.getGetNewEntity();
						en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
						String lab = "";
						try
						{
							en1.Retrieve();
							lab = en1.GetValStringByKey(attr.getUIRefKeyText());
						}
						catch (java.lang.Exception e2)
						{
							if (SystemConfig.getIsDebug() == false)
							{
								lab = "" + val.toString();
							}
							else
							{
								lab = "" + val.toString();
							}
						}

						this.Pub1.append("<td class='FDesc' nowrap width=1% > " + attr.getDesc() + "</td>");
						this.Pub1.append("<td class='TD' nowrap width='30%'>");
						this.Pub1.append(lab);
						this.Pub1.append("</td>");
					}
				}
				else if (attr.getUIContralType() == UIContralType.RadioBtn)
				{
					//					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
					//					foreach(SysEnum en in enums)
					//					{
					//						return ;
					//					}
				}

				if (isLeft == false)
				{
					isAddTR = false;
					this.Pub1.append(AddTREnd());
				}

				isLeft = !isLeft;
			} // 结束循环.
			//补充TR
			if (isAddTR == true)
			{
				this.Pub1.append(AddTD(""));
				this.Pub1.append(AddTD(""));
				this.Pub1.append(AddTREnd());
			}
			this.Pub1.append(Add("</TABLE>"));
			return this.Pub1.toString();
		}
		
		public final void AddContral(String desc, DDL ddl, boolean isRefBtn)
		{
			this.Pub1.append(Add("<td class='FDesc' nowrap width=1% > " + desc + "</td><td class=TD nowrap>"));
			this.Controls.append(Add(ddl));
			if (ddl.getEnabled())
			{
				if (ddl.getSelfBindKey().indexOf(".") == -1)
				{
					this.Pub1.append(AddTDEnd());
				}
				else
				{
					if (isRefBtn && ddl.Items.size() > 15)
					{
						String srip = "javascript:HalperOfDDL('" + ddl.getAppPath() + "','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.getClientID().toString() + "' ); ";
						this.Pub1.append(Add("<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.getId() + "' ></td>"));
					}
					else
					{
						this.Pub1.append(AddTDEnd());
					}
				}
			}
			else
			{
				this.Pub1.append(AddTDEnd());
			}
		}
		public final void AddContral(String desc, CheckBox cb)
		{
			/*this.Controls.append(Add(new LiteralControl("<td class='FDesc' nowrap width=1% > " + desc + "</td>")));
			this.Controls.append(Add(new LiteralControl("<td class='TD' nowrap >")));
			this.Controls.append(Add(cb));
			this.Controls.append(Add(new LiteralControl("</td>")));*/
			this.Controls.append(Add("<td class='FDesc' nowrap width=1% > " + desc + "</td>"));
			this.Controls.append(Add("<td class='TD' nowrap >"));
			this.Controls.append(Add(cb));
			this.Controls.append(Add("</td>"));
		}

		/** 
		 增加空件
		 
		 @param desc
		 @param tb
		*/
		public final void AddContral(String desc, TB tb)
		{
			if (tb.getReadOnly())
			{
				//xxx 2016年7月6日
				if (tb.attributes.containsValue("TBNum"))
				{
					tb.addAttr("class", "TBNumReadonly");
				}
				else
				{
					//tb.Attributes["Class"] = "TBReadonly";
					tb.addAttr("class", "TBReadonly");
				}
			}

			//tb.Attributes["style"] = "width=100%";
			tb.addAttr("style", "width=100%");
			if (tb.getTextMode() == TextBoxMode.MultiLine)
			{
				AddContralDoc(desc, tb);
				return;
			}

			this.Pub1.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");

			this.Pub1.append("<td class='TD' nowrap width='30%'>");
			this.Pub1.append(tb);
			this.Pub1.append("</td>");
		}

		public final void AddContralDoc(String desc, TB tb)
		{
			this.Pub1.append("<td colspan='2' width='500px' >" + desc + "<br>");
			if (tb.getReadOnly())
			{
				//tb.Attributes["Class"] = "TBReadonly";
				tb.addAttr("Class", "TBReadonly");
			}
			this.Pub1.append(tb);
			this.Pub1.append("</td>");
		}
		public final void AddContralDoc(String desc, TB tb, int colspanOfctl)
		{
			this.Pub1.append("<td  colspan='" + colspanOfctl + "' width='100%'>" + desc + "<br>");
			tb.setColumns(0);
			tb.setCssClass("TBDoc");
			this.Pub1.append(tb);
			this.Pub1.append("</td>");
		}

		public boolean isIsReadonly() {
			return IsReadonly;
		}

		public void setIsReadonly(boolean isReadonly) {
			IsReadonly = isReadonly;
		}

		public boolean isIsShowDtl() {
			return IsShowDtl;
		}

		public void setIsShowDtl(boolean isShowDtl) {
			IsShowDtl = isShowDtl;
		}

		public StringBuffer getPub1() {
			return Pub1;
		}

		public void setPub1(StringBuffer pub1) {
			Pub1 = pub1;
		}

		public StringBuffer getControls() {
			return Controls;
		}

		public void setControls(StringBuffer controls) {
			Controls = controls;
		}
		
		//get set
		
}
