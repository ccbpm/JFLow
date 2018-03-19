package cn.jflow.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.FrmPopVal;
import BP.Sys.MapExt;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;

public class FrmPopValModel extends BaseModel {

	public FrmPopValModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public UiFatory Pub1 = new UiFatory();
	private String _CtrlVal=null;
	private String FK_MapExt;

	public String get_CtrlVal() {
		if (_CtrlVal == null) {
			_CtrlVal = "," + get_request().getParameter("CtrlVal") + ",";
		}
		return _CtrlVal;
	}

	public void set_CtrlVal(String _CtrlVal) {
		this._CtrlVal = _CtrlVal;
	}

	public final String getFK_MapExt() {
		return get_request().getParameter("FK_MapExt");
	}

	public void setFK_MapExt(String fK_MapExt) {
		FK_MapExt = fK_MapExt;
	}

	public MapExt me = null;

	
	// /#endregion 属性.

	public void init() {
		me = new MapExt();
		me.setMyPK(this.getFK_MapExt());

		if (me.RetrieveFromDBSources() == 0) {
			FrmPopVal pv = new FrmPopVal(this.getFK_MapExt());
			me.Copy(pv);
		}

		if (me.getPopValShowModel() == 1) {
			try {
				
				get_response().sendRedirect("FrmPopValDir.jsp?a=2&"+ get_request().getQueryString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		boolean isCheckBox = false;
		if (me.getPopValSelectModel() == 0) {
			isCheckBox = true;
		} else {
			isCheckBox = false;
		}

		String sqlGroup = me.getTag1();
		sqlGroup = sqlGroup.replace("WebUser.No", WebUser.getNo());
		sqlGroup = sqlGroup.replace("@WebUser.Name", WebUser.getName());
		sqlGroup = sqlGroup.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		String sqlObjs = me.getTag2();
		sqlObjs = sqlObjs.replace("WebUser.No", WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		boolean isHaveUnGroup = true;
		DataTable dtGroup = new DataTable();
		if (sqlGroup.length() > 5) {
			isHaveUnGroup = false;
			dtGroup = BP.DA.DBAccess.RunSQLReturnTable(sqlGroup);
		} else {
			dtGroup.Columns.Add("No", String.class);
			dtGroup.Columns.Add("Name", String.class);
			DataRow dr = dtGroup.NewRow();
			dr.setValue("No", "01");
			dr.setValue("Name", "全部选择");
			dtGroup.Rows.add(dr);
		}

		DataTable dtObj = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
		if (dtObj.Columns.size() == 2) {
			dtObj.Columns.Add("Group", String.class);
//			for (DataRow dr : dtObj.Rows) {
//				dr.setValue("Group", "01");
//			}
			for (DataRow dr : dtObj.Rows) {
				dr.setValue("Group", "");
			}
		}

		int cols = 4;
		this.Pub1.append(AddTable("width=95% border=0"));
		if (isHaveUnGroup == false) {
			for (DataRow drGroup : dtGroup.Rows) {
				String ctlIDs = "";
				String groupNo = drGroup.getValue(0).toString();

				// 增加全部选择.
				this.Pub1.append(AddTR());
				CheckBox cbx = Pub1.creatCheckBox("CBs_" + drGroup.getValue(0).toString());
//				cbx.setId("CBs_" + drGroup.getValue(0).toString());
//				cbx.setName("CBs_" + drGroup.getValue(0).toString());
				cbx.setText(drGroup.getValue(1).toString());
				cbx.addAttr("onclick", "SetSelected(this,'"+groupNo+"')");
				this.Pub1.append(AddTDTitle("align=left", cbx.toString()));
				this.Pub1.append(AddTREnd());

				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDBegin("nowarp=false"));

				this.Pub1.append(AddTable("border=0"));
				int colIdx = -1;
				for (DataRow drObj : dtObj.Rows) {
					String no = drObj.getValue(0).toString();
					String name = drObj.getValue(1).toString();
					String group = drObj.getValue(2).toString();
					if (!group.trim().equals(groupNo.trim())) {
						continue;
					}

					colIdx++;
					if (colIdx == 0) {
						this.Pub1.append(AddTR());
					}

					CheckBox cb = Pub1.creatCheckBox("CB_" + no);
//					cb.setId("CB_" + no);
//					cb.setName("CB_" + no);
					ctlIDs += cb.getId() + ",";
					// cb.Attributes["onclick"] = "isChange=true;";
					cb.addAttr("onclick", "isChange=true;");
					cb.setText(name);
					cb.addAttr("title", group);
					if(!StringHelper.isNullOrEmpty(this.get_CtrlVal()))
					{
						if(this.get_CtrlVal().contains(","+name + ",") || this.get_CtrlVal().contains(","+no + ",") || this.get_CtrlVal().contains(";"+no + ","))
						{
							cb.setChecked(true);
						}
					}else
					{
						cb.setChecked(false);
					}
					
					if (cb.getChecked()) {
						cb.setText("<font color=green>" + cb.getText()
								+ "</font>");
					}
					this.Pub1.append(AddTD(cb));
					if (cols - 1 == colIdx) {
						this.Pub1.append(AddTREnd());
						colIdx = -1;
					}
				}
				// cbx.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs +
				// "')";
//				cbx.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");
				if (colIdx != -1) {
					while (colIdx != cols - 1) {
						colIdx++;
						this.Pub1.append(AddTD());
					}
					this.Pub1.append(AddTREnd());
				}
				this.Pub1.append(AddTableEnd());
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			}
		}
		
		// /#region 处理未分组的情况.
		if (isHaveUnGroup == true) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDBigDocBegain()); // ("nowarp=true");
			this.Pub1.append(AddTable());
			int colIdx = -1;
			String ctlIDs = "";
			// 增加全部选择.
			this.Pub1.append(AddTR());
			CheckBox cbx = Pub1.creatCheckBox("CBs_" + dtGroup.getValue(0, 0).toString());
//			cbx.setId("CBs_" + dtGroup.getValue(0, 0).toString());
//			cbx.setName("CBs_" + dtGroup.getValue(0, 0).toString());
			cbx.setText(dtGroup.getValue(0, 1).toString());
			cbx.addAttr("onclick", "SetSelected(this,'"+dtGroup.getValue(0, 0).toString()+"')");
			this.Pub1
					.append(AddTDTitle("align=left colspan=4", cbx.toString()));
//			this.Pub1.append("\n<TH align=left colspan=4 nowrap=true >");
//			this.Pub1.append(cbx);
//			this.Pub1.append("</TH>");
			this.Pub1.append(AddTREnd());

			for (DataRow drObj : dtObj.Rows) {
				String group = drObj.getValue(2).toString();
				isHaveUnGroup = true;
				for (DataRow drGroup : dtGroup.Rows) {
					String groupNo = drGroup.getValue(0).toString();
					if (!group.equals(groupNo)) {
						isHaveUnGroup = true;
						break;
					}
				}

				if (isHaveUnGroup == false) {
					continue;
				}

				String no = drObj.getValue(0).toString();
				String name = drObj.getValue(1).toString();

				colIdx++;
				if (colIdx == 0) {
					this.Pub1.append(AddTR());
				}

				CheckBox cb = Pub1.creatCheckBox("CB_" + no);
//				cb.setId("CB_" + no);
				ctlIDs += cb.getId() + ",";
				cb.setText(name + group);
				cb.addAttr("title", group);
				cb.setChecked(this.get_CtrlVal().contains("," + no + ","));
				if (cb.getChecked()) {
					cb.setText("<font color=green>" + cb.getText() + "</font>");
				}

				this.Pub1.append(AddTD(cb));

				if (cols - 1 == colIdx) {
					this.Pub1.append(AddTREnd());
					colIdx = -1;
				}
			}
			// cbx.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";
//			cbx.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");
			if (colIdx != -1) {
				while (colIdx != cols - 1) {
					colIdx++;
					this.Pub1.append(AddTD());
				}
				this.Pub1.append(AddTREnd());
			}
			this.Pub1.append(AddTableEnd());
			this.Pub1.append(AddTDEnd());
			this.Pub1.append(AddTREnd());
		}
		
		// /#endregion 处理未分组的情况.

		this.Pub1.append(AddTableEnd());

		Button btn = Pub1.creatButton("s");
//		btn.setId("s");
//		btn.setName("s");
		btn.setText(" OK ");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onclick", "btn_Click('s')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Cancel");
//		btn.setId("Cancel");
//		btn.setName("Cancel");
		btn.setText(" Cancel ");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onclick", "btn_Click('Cancel')");
		this.Pub1.append(btn);
	}
}
