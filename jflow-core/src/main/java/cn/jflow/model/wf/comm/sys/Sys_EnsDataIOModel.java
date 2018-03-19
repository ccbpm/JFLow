package cn.jflow.model.wf.comm.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.FileUpload;
import cn.jflow.system.ui.core.ListItem;
import BP.DA.DataColumn;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.Web.WebPage;
import BP.Web.WebUser;

public class Sys_EnsDataIOModel extends BaseModel {

	private StringBuilder builder;

	public Sys_EnsDataIOModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public String getEnsName() {
		String s = getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(s)) {
			s = "BP.GE.Infos";
		}
		return s;
	}

	public String getStep() {
		return StringHelper.isEmpty(getParameter("Step"), "");
	}
	
	public String getPub() {
		return builder.toString();
	}

	public void Bind2() {
		builder.append("<div>");

		builder.append("<b>第2/3步：</b>设置字段对应关系<hr>");
		
		String filePath = Glo.getFlowFields() + WebUser.getNo() + "DTS.xls";
		DataTable dt = BP.DA.DBLoad.GetTableByExt(filePath);

		builder.append(AddTable());
		builder.append(AddTR());
		builder.append(AddTDTitle("是否导入该字段?"));
		builder.append(AddTDTitle("中文名称"));
		builder.append(AddTDTitle("字段手工匹配"));
		builder.append(AddTREnd());
		
		Entity en = BP.En.ClassFactory.GetEns(this.getEnsName()) .getGetNewEntity();
		Attrs attrs = en.getEnMap().getAttrs();

		for (Attr attr : attrs) {
			builder.append(AddTR());
			CheckBox cb = new CheckBox();
			cb.setId("CB_" + attr.getKey());
			cb.setText(attr.getKey());
			cb.setChecked(true);
			builder.append(AddTD(cb));

			builder.append(AddTD(attr.getDesc()));

			DDL ddl = new DDL();
			ddl.setId("DDL_" + attr.getKey());

			int i = -1;
			for (DataColumn dc : dt.Columns) {
				i++;
				ListItem li = new ListItem();
				li.setText(dc.ColumnName);
				li.setValue((new Integer(i)).toString());
				if (li.getText().equals(attr.getDesc())) {
					li.setSelected(true);
				}
				ddl.Items.add(li);
			}
			builder.append(AddTD(ddl));
			builder.append(AddTREnd());
		}
		builder.append(AddTableEndWithHR());

		Button btn = new Button();
		btn.setId("Btn_Clear");
		btn.setCssClass("Btn");
		btn.setText("清空方式导入");

		btn.addAttr("onclick", "Btn_DataIO_Click();");
		builder.append(btn);
		builder.append(AddB("&nbsp;&nbsp;&nbsp;&nbsp;以:"));
		DDL ddl1 = new DDL();
		ddl1.setId("DDL_PK");
		for (Attr attr : attrs) {
			ddl1.Items.add(new ListItem(attr.getDesc(), attr.getKey()));
		}

		ddl1.SetSelectItem(en.getPK());
		builder.append(ddl1);
		
		btn = new Button();
		btn.setCssClass("Btn");
		btn.setId("Btn_UpdateKey");
		btn.setText("为主键，执行更新方式导入。");
		btn.attributes.put("onclick", "Btn_UpdateIO_Click();");
		builder.append(btn);

		builder.append(" - <a href='EnsDataIO.jsp?EnsName=" + this.getEnsName()
				+ "&Step=1' >返回数据文件上传</a> - <a href='EnsDataIO.jsp?EnsName="
				+ this.getEnsName()
				+ "&DoType=OutHtml' target=_blank >打开现有的数据源</a>");

		builder.append("</div>");
	}

	public void OutAll() {

	}

	public void OutCurrent() {
	}

	public void pageLoad() {
		builder = new StringBuilder();
		if (this.getDoType().equals("OutHtml")) {
			Entities ens2 = BP.En.ClassFactory.GetEns(this.getEnsName());
			ens2.RetrieveAll();
			builder.append(BindEns(ens2));
			return;
		} else if (this.getDoType().equals("OutAll")) {
			Entities ens = BP.En.ClassFactory.GetEns(this.getEnsName());
			ens.RetrieveAll();
			String file1 = WebPage.ExportDGToExcel(ens);
			sendRedirect(Glo.getCCFlowAppPath() + "Temp/" + file1);
			return;
		} else if (this.getDoType().equals("OutCurrent")) {
			Entities ens1 = BP.En.ClassFactory.GetEns(this.getEnsName());
			// QueryObject qo = BP.Web.Com
			ens1.RetrieveAll();
//			String file = this.ExportDGToExcel(ens1);
//			sendRedirect(Glo.getCCFlowAppPath() + "Temp" + "\\" + file);
			return;
		}
		if (this.getStep().equals("3")) {

		} else if (this.getStep().equals("2")) {
			this.Bind2();
			return;
		} else if (this.getStep().equals("1")) {
			this.Bind1();
			return;
		} else {
			builder = new StringBuilder();
			builder.append(AddUL("class='navlist'"));

			builder.append(AddLi("<div><a href='EnsDataIO.jsp?EnsName="
					+ this.getEnsName()
					+ "&DoType=OutAll' target=_self ><span class='nav'>1. 导出全部数据到Excel（把所有的数据导出Excel）</span></a></div>"));
			//builder.append("<font color=green>导出全部数据到Excel。</font>");

			builder.append(AddLi("<div><a href='EnsDataIO.jsp?EnsName="
					+ this.getEnsName() + "&Step=1' target=_self  ><span class='nav'>2. 执行数据导入（按照固定的格式从Excel中导入数据）</span></a></div>"));
			//builder.append("<font color=green>按照固定的格式从Excel中导入数据。</font>");

			builder.append(AddULEnd());
		}
	}

	public void Bind1() {
		FileUpload file = new FileUpload();
		file.addAttr("accept", ".xls");
//		HtmlInputFile file = new HtmlInputFile();
		file.setId("f");
		builder.append("<div>");
		builder.append("<b>第1/3步：</b>上传Excel数据文件<hr>");
		builder.append(file);
		Button btn = new Button();
		btn.setCssClass("Btn");
		btn.setId("Btn_Up");
		btn.setText("上传数据文件");
		btn.addAttr("onclick", "Btn_Step(2);");
		builder.append(btn);
//		btn.Click += new EventHandler(btn_Click);

		builder.append("</div>");
		builder.append(AddBR());
		this.OutExcel();
	}
	

	public void OutExcel() {
		Entities ens = BP.En.ClassFactory.GetEns(this.getEnsName());
		Map map = ens.getGetNewEntity().getEnMap();
		String strLine = "<table border=1 >";
		// 生成文件标题
		strLine += "<TR>";
		Attrs attrs = map.getAttrs();
		for (Attr attr : attrs) {
			if (attr.getKey().indexOf("Text") == -1) {
				if (attr.getUIVisible() == false) {
					continue;
				}
			}

			if (attr.getMyFieldType() == FieldType.Enum
					|| attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK
					|| attr.getMyFieldType() == FieldType.FK) {
				continue;
			}

			strLine += "<TD>" + attr.getDesc() + "</TD>";
		}
		strLine += "</TR>";
		strLine += "</Table>";

		builder.append("<div>");
		builder.append("Excel表格样式(您可以复制并copy到Excel中完成数据采集。)");
		builder.append(strLine);
		builder.append("</div>");

	}
	

}
