package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.FrmPopVal;
import BP.Sys.MapExt;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.RadioButton;

public class FrmPopValDirModel extends BaseModel {

	public FrmPopValDirModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public UiFatory Pub1 = new UiFatory();
	public UiFatory Left = new UiFatory();
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
	public final String getGroupVal() {
		return  get_request().getParameter("GroupVal");
	}

	public MapExt me = null;

	
	// /#endregion 属性.

	public void init() {
		
		FK_MapExt=get_request().getParameter("FK_MapExt");
		me = new MapExt();
		me.setMyPK(this.getFK_MapExt());

		if (me.RetrieveFromDBSources() == 0) {
			FrmPopVal pv = new FrmPopVal(this.getFK_MapExt());
			me.Copy(pv);
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
		DataTable  dtGroup =null;
		if(!"".equals(sqlGroup)){
			dtGroup =BP.DA.DBAccess.RunSQLReturnTable(sqlGroup);
		}
		if (dtGroup==null ||dtGroup.Rows.size() == 0) {
			this.Pub1.append(AddFieldSet("配置错误","分组数据源为空:"+sqlGroup));
			return;
		}
		this.Left.append(AddUL());
		for (DataRow dr : dtGroup.Rows) {
			String no = dr.getValue(0).toString();
			String name = dr.getValue(1).toString();
			this.Left.append(AddLi("<a href='FrmPopValDir.jsp?GroupVal=" + no + "&FK_MapExt=" + this.getFK_MapExt() + "&RefPK=" + this.getRefPK() + "&CtrlVal=" +this.get_CtrlVal() + "' >" + dr.getValue(1).toString() + "</a>"));
		}
		this.Left.append(AddULEnd());


		String gVal = this.getGroupVal();
		if (StringHelper.isNullOrEmpty(gVal)) {
			gVal = dtGroup.Rows.get(0).getValue(0).toString();
		}


		String sqlObjs = me.getTag2();
		sqlObjs = sqlObjs.replace("WebUser.No", WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		DataTable dtObjs = null;
		if(!"".equals(sqlObjs)){
			dtObjs =BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
		}
		if (dtObjs==null || dtObjs.Rows.size() == 0) {
			this.Pub1.append(AddFieldSet("配置或者数据源错误", "查询的entity是空的:" + sqlObjs));
			return;
		}
		for (DataRow dr : dtObjs.Rows) {
			String no = dr.getValue(0).toString();
			String name = dr.getValue(1).toString();

			if (isCheckBox == true) {
				CheckBox cb = new CheckBox();
				cb.setId("CB_" + no);
				cb.setText(name);
				this.Pub1.append(Add(cb));
			}
			else {
				RadioButton rb = new RadioButton();
				rb.setId ("RB_" + no);
				rb.setText(name);
				rb.setGroupName("ss");
				this.Pub1.append(Add(rb));
			}
		}

		
	}
		
}
