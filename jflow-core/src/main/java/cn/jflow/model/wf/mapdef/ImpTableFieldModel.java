package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.SFDBSrc;
import BP.Sys.SFDBSrcs;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Tools.StringHelper;
import BP.WF.Glo;

public class ImpTableFieldModel extends BaseModel {
	
	private String basePath;
	
	public ImpTableFieldModel(HttpServletRequest request,
			HttpServletResponse response,String basePath) {
		super(request, response);
		this.basePath=basePath;
	}

	private StringBuffer Pub1;

	public String getPub1() {
		return Pub1.toString();
	}

	// /#region 参数.
	public int getStep() {
		try {
			return Integer.parseInt(getParameter("Step"));
		} catch (java.lang.Exception e) {
			return 1;
		}
	}

	public String getFK_MapData() {
		String str = getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(str)) {
			return "abc";
		}
		return str;
	}

	public String getFK_SFDBSrc() {
		return getParameter("FK_SFDBSrc");
	}

	private String _STable = null;

	public String getSTable() {
		if (_STable == null) {
			_STable = getParameter("STable");
			if (_STable == null) {
				BP.En.Entity en = BP.En.ClassFactory
						.GetEn(this.getFK_MapData());
				if (en != null) {
					_STable = en.getEnMap().getPhysicsTable();
				} else {
					MapData md = new MapData(this.getFK_MapData());
					_STable = md.getPTable();
				}
			}
		}

		if (_STable == null) {
			_STable = "";
		}
		return _STable;
	}

	public String getSColumns() {
		return getParameter("SColumns");
	}

	// /#endregion 参数.

	public void loadPage() {
		// this.Response.Redirect("ImpTableField1504.jsp?EnsName=" +
		// this.FK_MapData,
		// true);
		// return;

		Pub1 = new StringBuffer();
		// /#region 第1步.
		if (this.getStep() == 1) {
			SFDBSrcs ens = new SFDBSrcs();
			ens.RetrieveAll();

			Pub1.append(AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));
			Pub1.append(AddTR());
			Pub1.append(AddTDGroupTitle("", "第1步：请选择数据源"));
			Pub1.append(AddTREnd());

			Pub1.append(AddTR());
			Pub1.append(AddTDBegin());
			Pub1.append(AddUL("class='navlist'"));

			for (SFDBSrc item : ens.ToJavaList()) {
				Pub1.append(AddLi("<div><a href='ImpTableField.jsp?Step=2&FK_MapData=" + this.getFK_MapData() + "&FK_SFDBSrc=" + item.getNo() + "'><span class='nav'>" + item.getNo() + "  -  " + item.getName() + "</span></a></div>"));
			}

			Pub1.append(AddLi("<div><a href=\"javascript:WinOpen('"+basePath+"/WF/Comm/RefFunc/UIEn.jsp?EnsName=BP.Sys.SFDBSrcs')\" ><img src='../Img/New.gif' align='middle' /><span class='nav'>新建数据源</span></a></div>"));

			Pub1.append(AddULEnd());
			Pub1.append(AddTDEnd());
			Pub1.append(AddTREnd());
			Pub1.append(AddTableEnd());
		}

		// /#endregion 第1步.

		// /#region 第2步.
		if (this.getStep() == 2) {
			SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());

			this.Pub1.append("<div class='easyui-layout' style='height:800px' data-options=\"fit:true\">");
			this.Pub1.append("<div data-options=\"region:'west',split:true,title:'选择 "+src.getDBName()+" 数据表/视图'\" style='width:200px;heigh:100%'>");

			DataTable dt = src.GetTables();
			this.Pub1.append(AddUL());
			for (DataRow dr : dt.Rows) {
				String url = "ImpTableField.jsp?Step=" + this.getStep() + "&FK_MapData=" + this.getFK_MapData() + "&FK_SFDBSrc=" + this.getFK_SFDBSrc() + "&STable=" + dr.getValue("No").toString();
				if (dr.getValue("No").toString().equals(this.getSTable())) {
					this.Pub1.append("<li><font color=red><b>" + dr.getValue("Name") + "</font></b></li>");
				} else {
					this.Pub1.append("<li><a href='" + url + "' >" + dr.getValue("Name") + "</a></li>");
				}
			}
			this.Pub1.append(AddULEnd());

			Pub1.append(AddDivEnd());

			this.Pub1 .append("<div data-options=\"region:'center',title:'第2步：请选择要导入的数据列（"
							+ this.getSTable() + "）'\" style='padding:5px;height:1000px'>");

			// 加载选中数据表/视图的列信息
			this.Pub1.append(AddTable()); // ("id='maintable' class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'");
			this.Pub1.append(AddTR());

			CheckBox cb = new CheckBox();
			cb.setId("CB_CheckAll");
			cb.setText("字段名");	//
			cb.attributes.put("onclick", "CheckAll(this.checked)");

			this.Pub1.append(AddTDGroupTitle("序"));
			this.Pub1.append(AddTDGroupTitle("style='width:40px;text-align:center'", cb.toString()));
//			this.Pub1.append(AddTDGroupTitle("字段名"));
			this.Pub1.append(AddTDGroupTitle("中文描述"));
			this.Pub1.append(AddTDGroupTitle("style='width:80px;text-align:center'", "类型"));
			this.Pub1.append(AddTDGroupTitle("style='width:50px;text-align:center'", "最大长度"));
			this.Pub1.append(AddTREnd());

			MapAttrs attrs = new MapAttrs(this.getFK_MapData());
//			boolean isHave = false;

			DataTable tableColumns = src.GetColumns(this.getSTable());
			if(tableColumns!=null && tableColumns.Rows.size()>0){
				for (DataRow dr : tableColumns.Rows) {
					cb = new CheckBox();
					cb.setId("CB_Col_" + dr.getValue("No"));
					cb.setText(dr.getValue("No").toString());
					if (StringHelper.isNullOrEmpty(this.getSColumns())) {
						cb.setChecked(false);
					} else {
						cb.setChecked(this.getSColumns().contains(dr.getValue("No") + ","));
					}

					// 如果已经有该字段，就放弃.
					if (attrs.Contains(MapAttrAttr.KeyOfEn, dr.getValue("No").toString())) {
//						continue;
						cb.enabled = false;
						cb.setEnabled(false);
					}

					this.Pub1.append(AddTR());
					this.Pub1.append(AddTD(dr.getValue("colid").toString()));
					this.Pub1.append(AddTD(cb));
//					this.Pub1.append(AddTD(dr.getValue("No").toString()));
					this.Pub1.append(AddTD(dr.getValue("Name").toString()));
					this.Pub1.append(AddTD(dr.getValue("DBType").toString()));
					this.Pub1.append(AddTD(dr.getValue("DBLength").toString()));
					this.Pub1.append(AddTREnd());
				}
			}

			this.Pub1.append(AddTableEnd());
			this.Pub1.append(AddBR());
			this.Pub1.append(AddBR());
			this.Pub1.append(AddSpace(1));

			this.Pub1.append("<a href='javascript:void(0);' onclick='btn_Next_Click();' class='easyui-linkbutton'>下一步</a>");
			this.Pub1.append(AddSpace(1));

			this.Pub1.append(String.format("<a href='%1$s?Step=1&FK_MapData=%2$s' class='easyui-linkbutton'>上一步</a>", Glo.getCCFlowAppPath()+"WF/Admin/FoolFormDesigner/ImpTableField.jsp", this.getFK_MapData()));
			this.Pub1.append(AddBR());
			this.Pub1.append(AddBR());
			this.Pub1.append(AddDivEnd());
			this.Pub1.append(AddDivEnd());
		}

		// /#endregion 第2步.

		// /#region 第3步.
		if (this.getStep() == 3) {
			SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
			this.Pub1.append(AddTable()); // ("id='maintable' class='Table' cellSpacing='0' cellPadding='0' border='0' style='width:100%'");
			this.Pub1.append(AddTDGroupTitle("序"));
			this.Pub1.append(AddTDGroupTitle("字段名"));
			this.Pub1.append(AddTDGroupTitle("中文描述"));
			this.Pub1.append(AddTDGroupTitle("数据类型"));
			this.Pub1.append(AddTDGroupTitle("逻辑类型"));
			this.Pub1.append(AddTDGroupTitle("绑定值(双击选择)"));
			this.Pub1.append(AddTDGroupTitle("最大长度"));
			this.Pub1.append(AddTDGroupTitle("顺序"));
			this.Pub1.append(AddTREnd());

			DataTable tableColumns = src.GetColumns(this.getSTable());
			int idx = 0;

			for (DataRow dr : tableColumns.Rows) {
				if (this.getSColumns().contains(dr.getValue("name") + ",") == false) {
					continue;
				}

				String typeString = dr.getValue("type").toString()
						.toLowerCase();
				int mydatatype = 1;
				if (typeString.contains("int")) {
					mydatatype = BP.DA.DataType.AppInt;
				}
				if (typeString.contains("float")) {
					mydatatype = BP.DA.DataType.AppFloat;
				}
				if (typeString.contains("double")) {
					mydatatype = BP.DA.DataType.AppDouble;
				}

				idx++;
				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDIdx(idx));
				this.Pub1.append(AddTD(dr.getValue("name").toString()));

				// 中文描述.
				TextBox tb = new TextBox();
				tb.setId("TB_Desc_" + dr.getValue("name"));
				tb.setColumns(20);
				tb.setText(dr.getValue("Desc").toString());
				if (tb.getText().length() == 0) {
					tb.setText(dr.getValue("name").toString());
				}
				this.Pub1.append(AddTD(tb));

				// 数据类型.
				DDL ddl = new DDL();
				ddl.setId("DDL_DBType_" + dr.getValue("name"));
				ddl.SelfBindSysEnum(MapAttrAttr.MyDataType);
				ddl.SetSelectItem(mydatatype); // 设置选择的项目.
				this.Pub1.append(AddTD(ddl));

				// 逻辑类型.
				ddl = new DDL();
				ddl.setId("DDL_LogicType_" + dr.getValue("name"));
				ddl.SelfBindSysEnum(MapAttrAttr.LGType);
				this.Pub1.append(AddTD(ddl));

				// 绑定的逻辑类型.
				tb = new TextBox();
				tb.setId("TB_BindKey_" + dr.getValue("name"));
				tb.setColumns(30);
				tb.setText(dr.getValue("name").toString());
				tb.attributes.put("ondblclick", "OpenSelectBindKey(this);");
				this.Pub1.append(AddTD(tb));

				// 最大长度.
				// 绑定的逻辑类型.
				tb = new TextBox();
				tb.setId("TB_Len_" + dr.getValue("name"));
				tb.setColumns(5);
				tb.setText(dr.getValue("length").toString());
				this.Pub1.append(AddTD(tb));

				this.Pub1.append(AddTDBegin("style='text-align:center'"));
				TextBox hiddenIdx = new TextBox();
				hiddenIdx.setId("HID_Idx_" + dr.getValue("name"));
				hiddenIdx.setText(String.valueOf(idx));
				this.Pub1.append(hiddenIdx.toString());

				this.Pub1
						.append("<a href='javascript:void(0)' onclick='up(this, 6)' class='easyui-linkbutton' data-options=\"iconCls:'icon-up'\"></a>&nbsp;");
				this.Pub1
						.append("<a href='javascript:void(0)' onclick='down(this, 6)' class='easyui-linkbutton' data-options=\"iconCls:'icon-down'\"></a>");

				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			}

			this.Pub1.append(AddTableEnd());
			this.Pub1.append(AddBR());
			this.Pub1.append(AddBR());
			this.Pub1.append(AddSpace(1));

			this.Pub1.append("<a href='#javascript:void(0);' onclick='btn_Save_Click();' class='easyui-linkbutton'>导入字段，生成表单</a>");
			
			this.Pub1.append(AddSpace(1));

			this.Pub1.append(String.format("<a href='%1$s' class='easyui-linkbutton'>上一步</a>", get_request().getRequestURL().toString() .replace("Step=3", "Step=2")));
			this.Pub1.append(AddBR());
			this.Pub1.append(AddBR());
		}

		// /#endregion 第3步.
	}

	/**
	 * 将SQL数据库字段类型转为系统类型
	 * 
	 * @param sqlDataType
	 *            SQL数据库字段类型
	 * @return
	 */
//	private int GetMyDataType(String sqlDataType) {
//		if (sqlDataType.toLowerCase().equals("tinyint")
//				|| sqlDataType.toLowerCase().equals("smallint")
//				|| sqlDataType.toLowerCase().equals("int")) {
//			return DataType.AppInt;
//		} else if (sqlDataType.toLowerCase().equals("money")
//				|| sqlDataType.toLowerCase().equals("smallmoney")) {
//			return DataType.AppMoney;
//		} else if (sqlDataType.toLowerCase().equals("float")
//				|| sqlDataType.toLowerCase().equals("decimal")
//				|| sqlDataType.toLowerCase().equals("bigint")
//				|| sqlDataType.toLowerCase().equals("real")) {
//			return DataType.AppDouble;
//		} else if (sqlDataType.toLowerCase().equals("bit")) {
//			return DataType.AppBoolean;
//		} else if (sqlDataType.toLowerCase().equals("datetime")
//				|| sqlDataType.toLowerCase().equals("smalldatetime")) {
//			return DataType.AppDateTime;
//		} else if (sqlDataType.toLowerCase().equals("date")) {
//			return DataType.AppDate;
//		} else if (sqlDataType.toLowerCase().equals("char")
//				|| sqlDataType.toLowerCase().equals("nchar")
//				|| sqlDataType.toLowerCase().equals("varchar")
//				|| sqlDataType.toLowerCase().equals("nvarchar")
//				|| sqlDataType.toLowerCase().equals("text")
//				|| sqlDataType.toLowerCase().equals("ntext")
//				|| sqlDataType.toLowerCase().equals("xml")) {
//		} else {
//			return DataType.AppString;
//		}
//		return 0;
//	}

	

	/**
	 * 数据源
	 * 
	 * @param attrs
	 *            数据字符串，规则如下:
	 *            <p>
	 *            1.每个字段间用 ^ 来分隔
	 *            </p>
	 *            <p>
	 *            2.字段的信息间用 ~ 来分隔
	 *            </p>
	 *            <p>
	 *            3.字段的信息分别为：英文名称，中文名称，数据类型，最大长度，逻辑类型，序号
	 *            </p>
	 * @param tableName
	 *            数据表名称
	 */
	public void InitMapAttr(String tableName, String attrs) {
		Pub1.append(AddEasyUiPanelInfo("发送信息", attrs));
		return;
//		// 删除有可能存在的临时数据.
//		String tempStr = tableName + "Tmp";
//
//		MapAttr ma = new MapAttr();
//		ma.Delete(MapAttrAttr.FK_MapData, tempStr);
//
//		String[] strs = attrs.split("[^]", -1);
//		for (String str : strs) {
//			if (StringHelper.isNullOrEmpty(str)) {
//				continue;
//			}
//
//			String[] mystrs = str.split("[~]", -1);
//			ma = new MapAttr();
//			ma.setKeyOfEn(mystrs[0]);
//			ma.setName(mystrs[1]);
//			ma.setFK_MapData(tempStr);
//			ma.setMyDataType(Integer.parseInt(mystrs[2]));
//			ma.setMaxLen(Integer.parseInt(mystrs[3]));
//			ma.setLGType(FieldTypeS.valueOf((mystrs[4])));
//			ma.setIDX(Integer.parseInt(mystrs[5]));
//			ma.setMyPK(tempStr + "_" + ma.getKeyOfEn());
//			ma.Insert();
//		}
	}

	/**
	 * 绑定集合.
	 */
	public void BindAttrs() {

	}

}
