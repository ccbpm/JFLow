package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.TextBox;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnums;

public class SysEnumModel extends BaseModel {
	public UiFatory Pub1 = new UiFatory();

	public SysEnumModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public String Title;

	/**
	 * 执行类型
	 */
	public final String getDoType() {
		return get_request().getParameter("DoType");
	}

	public final String getIDX() {
		return get_request().getParameter("IDX");
	}

	public void init() {
		SysEnumMain main = new SysEnumMain();
		if (this.getRefNo() != null) {
			main.setNo(this.getRefNo());
			main = new SysEnumMain(this.getRefNo());
			// main.Retrieve();
		}
		this.BindSysEnum(main);
	}

	public final void BindSysEnum(SysEnumMain en) {
		SysEnums ses = new SysEnums();
		if (en.getNo().length() > 0) {
			// ses = new SysEnums(en.No);
			ses.Retrieve(SysEnumAttr.EnumKey, en.getNo());
		}

		this.Pub1.append(AddTable());
		if (this.getRefNo() == null) {
			this.Pub1.append(AddCaptionLeft("<a href='Do.jsp?DoType=AddF&MyPK="
					+ this.getMyPK() + "&IDX=" + this.getIDX()
					+ "'>增加新字段向导</a> - <a href='Do.jsp?DoType=AddSysEnum&MyPK="
					+ this.getMyPK() + "&IDX=" + this.getIDX()
					+ "'>枚举字段</a> - 新建"));
		} else {
			this.Pub1.append(AddCaptionLeft("<a href='Do.jsp?DoType=AddF&MyPK="
					+ this.getMyPK() + "&IDX=" + this.getIDX()
					+ "'>增加新字段向导</a> - <a href='Do.jsp?DoType=AddSysEnum&MyPK="
					+ this.getMyPK() + "&IDX=" + this.getIDX()
					+ "'>枚举字段</a> - 编辑"));
		}

		if (this.getRefNo() == null) {
			this.Title = "新建枚举";
		} else {
			this.Title = "编辑枚举类型";
		}

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("&nbsp;"));
		this.Pub1.append(AddTDTitle("&nbsp;"));
		this.Pub1.append(AddTDTitle("备注"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTRSum());
		this.Pub1.append(AddTD("编号"));
		TextBox tb = Pub1.creatTextBox("TB_No");
		// tb.ID = "TB_No";
		tb.setText(en.getNo());
		if (this.getRefNo() == null) {
			tb.setEnabled(true);
		} else {
			tb.setEnabled(false);
		}

		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("枚举英文名称"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTRSum());
		this.Pub1.append(AddTD("名称"));
		tb = Pub1.creatTextBox("TB_Name");
		// tb.ID = "TB_Name";
		tb.setText(en.getName());
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("枚举中文名称"));
		this.Pub1.append(AddTREnd());

		int idx = 0;
		while (idx < 20) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			tb = Pub1.creatTextBox("TB_" + idx);
			// tb.ID = "TB_" + idx;
			Object tempVar = ses.GetEntityByKey(SysEnumAttr.IntKey, idx);
			SysEnum se = (SysEnum) ((tempVar instanceof SysEnum) ? tempVar
					: null);
			if (se != null) {
				tb.setText(se.getLab());
			}
			// tb.Text = en.Name;
			this.Pub1.append(AddTD(tb));
			this.Pub1.append(AddTD(""));
			this.Pub1.append(AddTREnd());
			idx++;
		}

		this.Pub1.append(AddTRSum());
		this.Pub1.append("<TD colspan=3 align=center>");
		Button btn = Pub1.creatButton("Btn_Save");
		// btn.ID = "Btn_Save";
		btn.setCssClass("Btn");
		btn.setText(" 保存 ");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_Save')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_Add");
		btn.setCssClass("Btn");
		// btn.ID = "Btn_Add";
		btn.setText("添加到表单"); // "添加到表单";
		// btn.Attributes["onclick"] = " return confirm('您确认吗？');";
		
		// event wireups:
		// btn.Click += new EventHandler(btn_Add_Click);
		btn.addAttr("onclick", "btn_Add_Click('Btn_Add')");
		if (this.getRefNo() == null) {
			btn.setEnabled(false);
		}
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_Del");
		btn.setCssClass("Btn");
		// btn.ID = "Btn_Del";
		btn.setText(" 删除 ");
		// btn.Attributes["onclick"] = " return confirm('您确认吗？');";
		btn.addAttr("onclick", "btn_Del_Click('Btn_Del')");
		if (this.getRefNo() == null) {
			btn.setEnabled(false);
		}

		
		// event wireups:
		// btn.Click += new EventHandler(btn_Del_Click);
		this.Pub1.append(btn);

		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
	}

}
