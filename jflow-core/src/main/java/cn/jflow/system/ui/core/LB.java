package cn.jflow.system.ui.core;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.EntityNoName;
import BP.Web.WebUser;

/**
 * BPListBox 的摘要说明。
 */
//[System.Drawing.ToolboxBitmap(typeof(System.Web.UI.WebControls.ListBox))]
public class LB extends ListBox {
	public LB(Attr attr) {
		//			this.MaxLength =attr.MaxLength;
		//			//this.Width = Unit.Pixel(attr.UIWidth ); 
		//			this.DefaultWith = attr.UIWidth;
		//			 
		//			this.ReadOnly = attr.UIIsReadonly ;
		//			this.ShowType=attr.UITBShowType ;
		//this.Attributes["size"] = attr.UIWidth.toString();
		this.addAttr("size",attr.getUIWidth()+"");

		this.setVisible(attr.getUIVisible());
		//			this.DataHelpKey=attr.UIBindKey ;
		//			this.ShowType = attr.UITBShowType ;
		//			this.DataHelpKey = attr.UIBindKey;

		//this.Style.Clear();
		//this.Style.Add("width",attr.UIWidth.ToString()+"px") ;			

		//this.CssClass = "DGLB" + WebUser.Style;
		this.setCssClass("DGLB"+WebUser.getStyle());
		//this.PreRender += new System.EventHandler(this.LBPreRender);
	}

	public LB() {
		//this.CssClass = "LB" + WebUser.Style;
		this.setCssClass("LB"+WebUser.getStyle());
		//
		// TODO: 在此处添加构造函数逻辑
		//
	}

	/**
	 * OID , Name .
	 * @param dt
	 */
	public final void BindByTable(DataTable dt) {
		for (DataRow dr : dt.Rows) {
			//ListItem li = new ListItem();
			//this.Items.Add(new ListItem(dr["Name"].toString(), dr["OID"].toString()));
			this.Items.add(new ListItem(dr.getValue("Name").toString(),dr.getValue("OID").toString()));
		}
	}

	public final void BindByTableNoName(DataTable dt) {
		for (DataRow dr : dt.Rows) {
			//ListItem li = new ListItem();
			//this.Items.Add(new ListItem(dr["Name"].toString(), dr["No"].toString()));
			this.Items.add(new ListItem(dr.getValue("Name").toString(),dr.getValue("No").toString()));
		}
	}

	public final void BindAppTaxpayerTax(String taxpayerNo) {
		this.Items.clear();
		String sql = " SELECT DISTINCT  TaxTypeNo, TaxTypeName as Name, TaxTypeNo as No FROM V_IncMapTax WHERE TaxpayerNo='" + taxpayerNo + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		this.BindByTableNoName(dt);
	}

	/**
	 * 会计期间范围设定 Evaluate AND Check
	 * @param type Evaluate / Check
	 */
	public final void Bind_AppPeriodScope(String type) {
		this.Items.clear();
		String sql = "select * from B_PeriodScope WHERE type='" + type + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows) {
			this.Items.add( new ListItem(dr.getValue("FromYear").toString()+"年"+dr.getValue("ToMonth").toString()+"月 -- "+dr.getValue("ToYear").toString()+"年"+dr.getValue("ToMonth").toString()+"月； 设定日期："+dr.getValue("CreateDate").toString(),dr.getValue("PSID").toString() ));
		}
	}

	///#region 取出选择的值
	public final int getSelectedItemIntVal() {
		return Integer.parseInt(this.SelectedItem.getValue());
	}

	public final String getSelectedItemStringVal() {
		return this.SelectedItem.getValue();
	}

	///#endregion

	///#region 与集合bind
	public final void BindAppEntities(BP.En.EntitiesNoName ens) {
		this.Items.clear();
		for (EntityNoName en : ens.ToJavaListEnNo()) {
			this.Items.add(new ListItem(en.getNo()+" "+en.getName(), en.getNo()) ) ; 
		}
	}

	/**
	 * 设置选择的值
	 * @param val
	 */
	public final void SetSelectItem(Object val) {
		for (ListItem li : this.Items) {
			li.setSelected(false);
		}

		for (ListItem li : this.Items) {

			if (val.toString().equals(li.getValue())) {
				li.setSelected(true);
				break;
			} else {
				li.setSelected(false);
			}
		}
	}
	///#endregion

}