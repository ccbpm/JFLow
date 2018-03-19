package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Tools.StringHelper;

public class CopyDtlFieldModel extends BaseModel {

	private StringBuilder pub;

	public CopyDtlFieldModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		pub=new StringBuilder();
	}
	public StringBuilder getPub() {
		return pub;
	}
	private void setPub(String str) {
		pub.append(str);
	}
	public final String getDtl() {
		return getParameter("Dtl");
	}
	public final void BindAttrs() {
//		MapDtl dtlFrom = new MapDtl(this.getDtl());
		MapDtl dtl = new MapDtl(this.getMyPK());
		MapAttrs attrsFrom = new MapAttrs(this.getDtl());
		MapAttrs attrs = new MapAttrs(dtl.getNo());
		setPub(AddTable());
		setPub(AddCaptionLeft("<A href='CopyDtlField.jsp?MyPK=" + this.getMyPK()+ "'>返回</A> - 选择要复制的字段"));
		setPub(AddTR());
		setPub(AddTDTitle("IDX"));
		setPub(AddTDTitle("名称"));
		setPub(AddTDTitle("字段"));
		setPub(AddTDTitle("类型"));
		setPub(AddTDTitle("默认值"));
		setPub(AddTREnd());
		boolean isHave = false;
		int idx = 0;
		for (MapAttr attr : attrsFrom.ToJavaList()) {
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals("OID")
					|| keyOfEn.equals("FID")
					|| keyOfEn.equals("WorkID")
					|| keyOfEn.equals("Rec")
					|| keyOfEn.equals("RDT")) {
				continue;
			} else {
			}
			idx++;
			setPub(AddTR());
			setPub(AddTDIdx(idx));
			CheckBox cb = new CheckBox();
			cb.setId("CB_" + attr.getMyPK());
			cb.setText(attr.getName());
			if (attrs.Contains(MapAttrAttr.KeyOfEn, keyOfEn)) {
				cb.setEnabled(false);
			}
			setPub(AddTD("nowarp=true", cb));
			setPub(AddTD(keyOfEn));
			setPub(AddTD(attr.getMyDataTypeStr()));
			setPub(AddTD(attr.getDefValReal()));
			isHave = true;
			setPub(AddTREnd());
		}
		idx++;
		setPub(AddTRSum());
		setPub(AddTDIdx(idx));
		setPub(AddTD());
		Button btn = new Button();
		btn.setCssClass("Btn");
		btn.setId("Btn_Copy");
		btn.setText("复制");
		btn.addAttr("onclick","btn_Click()");
		btn.setEnabled(isHave);
		setPub(AddTD("colspan=3", btn));
		setPub(AddTREnd());
		setPub(AddTableEnd());
	}
	public final void page_Load() {
		// this.Page.Title = "Copy Node Dtl Fields  Esc to exit.";
		if (!StringHelper.isNullOrEmpty(this.getDtl())) {
			this.BindAttrs();
			return;
		}
//		MapDtl dtl = new MapDtl(this.getMyPK());
		String sql = "SELECT b.Name as NodeName, a.No AS DtlNo, a.Name as DtlName";
		sql += " FROM Sys_MapDtl a , Sys_MapData b  ";
		sql += " WHERE A.FK_MapData=b.No AND B.No LIKE '"
				+ this.getMyPK().substring(0, 4) + "%' AND B.No<>'"
				+ this.getMyPK() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			this.winCloseWithMsg("没有可复制的节点数据。");
			return;
		}
		if (dt.Rows.size() == 1) {
			 sendRedirect(
						"CopyDtlField.jsp?MyPK=" + this.getMyPK() + "&Dtl="
								+ dt.Rows.get(0).getValue("DtlNo").toString());
			return;
		}
		setPub(AddFieldSet("本流程上的明细数据"));
		setPub(AddUL());
		for (DataRow dr : dt.Rows) {
			setPub(AddLi("CopyDtlField.jsp?MyPK=" + this.getMyPK() + "&Dtl="+ dr.getValue("DtlNo").toString(),
					"节点名：" + dr.getValue("NodeName").toString() + " 表名称："
							+ dr.getValue("DtlName").toString()));
		}
		setPub(AddULEnd());
		setPub(AddFieldSetEnd());
	}
}
