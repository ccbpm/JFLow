package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.Demo.QingJia;

public class S4_RenShiModel extends BaseModel {

	public String IsPostBack;
	public TextBox TB_No;
	public TextBox TB_Name;
	public TextBox TB_QingJiaTianShu;
	public TextBox TB_QingJiaYuanYin;
	public TextBox TB_DeptNo;
	public TextBox TB_DeptName;
	public TextBox TB_BMNote;
	public TextBox TB_NoteRL;

	public S4_RenShiModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		TB_No = new TextBox();
		TB_Name = new TextBox();
		TB_QingJiaTianShu = new TextBox();
		TB_QingJiaYuanYin = new TextBox();
		TB_DeptNo = new TextBox();
		TB_DeptName = new TextBox();
		TB_BMNote = new TextBox();
		TB_NoteRL = new TextBox();

		TB_BMNote.setTextMode(TextBoxMode.MultiLine);
		TB_NoteRL.setTextMode(TextBoxMode.MultiLine);
	}

	// /#region 接受4大参数(这四大参数是有ccflow传递到此页面上的).
	// 流程编号
	public final String getFK_Flow() {
		return getParameter("FK_Flow");
	}

	// 当前节点ID
	public final int getFK_Node() {
		return Integer.parseInt(getParameter("FK_Node"));
	}

	// 工作ID
	public final long getWorkID() {
		return Long.parseLong(getParameter("WorkID"));
	}

	// 流程ID
	public final long getFID() {
		return Long.parseLong(getParameter("FID"));
	}

	public boolean getIsPostBack() {
		if (IsPostBack == null) {
			IsPostBack = getParameter("isPostBack");
		}
		if (IsPostBack == null || "".equals(IsPostBack)) {
			return false;
		} else if ("true".equals(IsPostBack.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public void Page_Load() {
		if (this.getIsPostBack() == false) {
			// 查询出来.
			QingJia en = new QingJia();
			en.setOID((int) this.getWorkID());
			en.Retrieve();

			this.TB_No.setText(en.getQingJiaRenNo());
			this.TB_No.setId("TB_No");
			this.TB_No.setName("TB_No");
			this.TB_No.setReadOnly(true);

			this.TB_Name.setText(en.getQingJiaRenName()); // 请假人名称.
			this.TB_Name.setId("TB_Name");
			this.TB_Name.setName("TB_Name");
			this.TB_Name.setReadOnly(true);

			this.TB_DeptNo.setText(en.getQingJiaRenDeptNo()); // 部门编号.
			this.TB_DeptNo.setId("TB_DeptNo");
			this.TB_DeptNo.setName("TB_DeptNo");
			this.TB_DeptNo.setReadOnly(true);

			this.TB_DeptName.setText(en.getQingJiaRenDeptName()); // 部门名称
			this.TB_DeptName.setId("TB_DeptName");
			this.TB_DeptName.setName("TB_DeptName");
			this.TB_DeptName.setReadOnly(true);

			this.TB_QingJiaYuanYin.setText(en.getQingJiaYuanYin()); // 请假原因
			this.TB_QingJiaYuanYin.setId("TB_QingJiaYuanYin");
			this.TB_QingJiaYuanYin.setName("TB_QingJiaYuanYin");
			this.TB_QingJiaYuanYin.setReadOnly(true);

			this.TB_QingJiaTianShu.setText(String.valueOf(en
					.getQingJiaTianShu())); // 请假天数
			this.TB_QingJiaTianShu.setId("TB_QingJiaTianShu");
			this.TB_QingJiaTianShu.setName("TB_QingJiaTianShu");
			this.TB_QingJiaTianShu.setReadOnly(true);

			// 给部门经理审批字段赋值.
			this.TB_BMNote.setText(en.getNoteBM());
			this.TB_BMNote.setId("TB_BMNote");
			this.TB_BMNote.setName("TB_BMNote");
			this.TB_BMNote.setRows(2);
			this.TB_BMNote.setCols(20);
			this.TB_BMNote.setCssClass("a");
			this.TB_BMNote.setReadOnly(true);

			this.TB_NoteRL.setText(en.getNoteRL());
			this.TB_NoteRL.setId("TB_NoteRL");
			this.TB_NoteRL.setName("TB_NoteRL");
			this.TB_NoteRL.setRows(2);
			this.TB_NoteRL.setCols(20);
			this.TB_NoteRL.setCssClass("a");

		}
	}
}
