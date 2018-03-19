package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.MapExt;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;

public class FrmReturnValTBFullCtrlModel extends EnModel {

	public FrmReturnValTBFullCtrlModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	private String getFK_MapExt()
	{
		return get_request().getParameter("FK_MapExt");
	}
	private String getVal()
	{
		String s = get_request().getParameter("CtrlVal");
		if (s==null || s.equals(""))
		{
			s = "";
		}
		return s;
	}

	public void loadModel()
	{
		MapExt ext = new MapExt(this.getFK_MapExt());
		String sql = ext.getTagOfSQL_autoFullTB();
		if (this.getVal() != null)
		{
			sql = sql.replace("@Key", this.getVal());
		}

		sql = sql.replace("$", "");
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		BindIt(dt);
	}
	public final void BindIt(DataTable dt)
	{
		appendPub(AddTable("width=80%"));
		appendPub(AddTR());
		appendPub(AddTDBegin("class=Title colspan=" + dt.Columns.size()+1));
		appendPub(Add("关键字"));
		TextBox tb = new TextBox();
		tb.setId("TB_Key");
		tb.setText(this.getVal());
		appendPub(Add(tb));

		Button btn = new Button();
		btn.setCssClass("Btn");
		btn.setId("Btn_Search");
		btn.setText("查找");
		//btn.Click += new EventHandler(btn_Search_Click);
		btn.addAttr("onclick", "btn_Search_Click('"+this.getFK_MapExt()+"')");
		appendPub(Add(btn));
		appendPub(AddTDEnd());
		appendPub(AddTREnd());

		appendPub(AddTR());
		appendPub(AddTDTitle("选择"));
		for (DataColumn dc : dt.Columns)
		{
			if (dc.ColumnName.equals("No") || dc.ColumnName.equals("Name"))
			{
				continue;
			}
			appendPub(AddTDTitle(dc.ColumnName));
		}
		appendPub(AddTREnd());

		for (DataRow dr : dt.Rows)
		{
			appendPub(AddTR());
			RadioButton rb = new RadioButton();
			rb.setText(dr.getValue("No").toString() + "," + dr.getValue("Name").toString());
			rb.setId("RB_" + dr.getValue("No").toString());
			rb.setGroupName("sd");
			appendPub(AddTD(rb));

			for (DataColumn dc : dt.Columns)
			{
				if (dc.ColumnName.equals("No") || dc.ColumnName.equals("Name"))
				{
					continue;
				}

				appendPub(AddTD(dr.getValue(dc.ColumnName).toString()));
			}
			appendPub(AddTREnd());
		}
		appendPub(AddTableEndWithHR());
		btn = new Button();
		btn.setId("s");
		btn.setCssClass("Btn");
		btn.setText("确定");
		//btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onclick", "btn_Click()");
		appendPub(Add(btn));
	}
	/*private void btn_Search_Click(Object sender, EventArgs e)
	{
		String key = this.GetTextBoxByID("TB_Key").getText();
		this.Response.Redirect("FrmReturnValTBFullCtrl.aspx?FK_MapExt=" + this.getFK_MapExt() + "&CtrlVal=" + key, true);
	}*/
	private void btn_Click()
	{
		MapExt ext = new MapExt(this.getFK_MapExt());
		String sql = ext.getTagOfSQL_autoFullTB();
		if (this.getVal() != null)
		{
			sql = sql.replace("@Key", this.getVal());
		}

		sql = sql.replace("$", "");

		String val = "";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			RadioButton rb = new RadioButton();//this.GetRadioButtonByID("RB_" + dr.getValue("No"));
			if (rb.getChecked())
			{
				val = dr.getValue("No").toString();
				this.WinClose();
				return;

			}
		}
		this.WinClose();
	}
}
