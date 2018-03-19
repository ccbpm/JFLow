package cn.jflow.system.ui.core;

import java.util.ArrayList;
import java.util.List;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.EntityNoName;
import BP.Web.WebUser;
import cn.jflow.model.designer.ListSelectionMode;

public class ListBox extends BaseWebControl{
	
	public boolean AutoPostBack;
	
	public boolean getAutoPostBack() {
		return AutoPostBack;
	}
	public void setAutoPostBack(boolean autoPostBack) {
		this.AutoPostBack = autoPostBack;
	}

	public List<ListItem> Items=new ArrayList<ListItem>();
	
	private int SelectedIndex;
	
	public int getSelectedIndex() {
		return SelectedIndex;
	}
	public void setSelectedIndex(int selectedIndex) {
		SelectedIndex = selectedIndex;
	}

	public ListItem SelectedItem;
	
	public ListItem getSelectedItem() {
		return SelectedItem;
	}
	public void setSelectedItem(ListItem selectedItem) {
		SelectedItem = selectedItem;
	}

	private int height;
	
	private ListSelectionMode SelectionMode;
	
	public ListSelectionMode getSelectionMode() {
		return SelectionMode;
	}
	public void setSelectionMode(ListSelectionMode selectionMode) {
		SelectionMode = selectionMode;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public ListBox(Attr attr)
	{
		//		this.MaxLength =attr.MaxLength;
		//		//this.Width = Unit.Pixel(attr.UIWidth ); 
		//		this.DefaultWith = attr.UIWidth;
		//		 
		//		this.ReadOnly = attr.UIIsReadonly ;
		//		this.ShowType=attr.UITBShowType ;
		this.addAttr("size",""+attr.getUIWidth());

		this.setVisible(attr.getUIVisible());			 
		//		this.DataHelpKey=attr.UIBindKey ;
		//		this.ShowType = attr.UITBShowType ;
		//		this.DataHelpKey = attr.UIBindKey;

//		this.Style.Clear();
		//this.Style.Add("width",attr.UIWidth.ToString()+"px") ;			

		this.setCssClass("DGLB"+WebUser.getStyle());
//		this.PreRender += new System.EventHandler(this.LBPreRender);
	}
	public ListBox()
	{
		this.setCssClass("LB"+WebUser.getStyle());
	}
	/// <summary>
	/// OID , Name . 
	/// </summary>
	/// <param name="dt"></param>
	public void BindByTable(DataTable dt)
	{
		for(DataRow dr:dt.Rows)
		{
			//ListItem li = new ListItem();
			this.Items.add(new ListItem(dr.getValue("Name").toString(),dr.getValue("OID").toString()));
		}
	}
	public void BindByTableNoName(DataTable dt )
	{
		boolean first= true;
		for(DataRow dr:dt.Rows)
		{
			if(first){
				this.setSelectedItem(new ListItem( (String)dr.getValue(dr.columns.get("Name")),(String)dr.getValue(dr.columns.get("No"))));
				first  = false;
			}
//			//ListItem li = new ListItem();
//			if(dr.get("Name")==null || dr.get("No")==null)
//			{
//				this.Items.add(new ListItem("",""));
//			}else if(dr.get("No")==null || dr.get("No")!=null)
//			{
//				this.Items.add(new ListItem("",dr.get("No").toString()));
//			}else if(dr.get("No")!=null || dr.get("No")!=null)
//			{
//				this.Items.add(new ListItem(dr.get("Name").toString(),dr.get("No").toString()));
//			}else if(dr.get("No")!=null || dr.get("No")==null)
//			{
//				this.Items.add(new ListItem(dr.get("Name").toString(),""));
//			}
			this.Items.add(new ListItem( (String)dr.getValue(dr.columns.get("Name")),(String)dr.getValue(dr.columns.get("No"))));
		}
	}
	public void BindAppTaxpayerTax(String taxpayerNo)
	{
		this.Items.clear();
		String sql = " SELECT DISTINCT  TaxTypeNo, TaxTypeName as Name, TaxTypeNo as No FROM V_IncMapTax WHERE TaxpayerNo='"+taxpayerNo+"'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		this.BindByTableNoName(dt);
	}
	/// <summary>
	/// 会计期间范围设定
	/// Evaluate AND Check
	/// </summary>
	/// <param name="type">Evaluate / Check</param>
	public void Bind_AppPeriodScope(String type )
	{
		this.Items.clear();
		String sql="select * from B_PeriodScope WHERE type='"+type+"'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for(DataRow dr:dt.Rows)
		{
			this.Items.add( new ListItem(dr.getValue("FromYear").toString()+"年"+dr.getValue("ToMonth").toString()+"月 -- "+dr.getValue("ToYear").toString()+"年"+dr.getValue("ToMonth").toString()+"月； 设定日期："+dr.getValue("CreateDate").toString(),dr.getValue("PSID").toString() ));
		}
	}
	
	private int selectedItemIntVal;
	
	private String selectedItemStringVal;
	

	
	public String getSelectedItemStringVal() {
		return this.getSelectedItem().getValue();
	}
	public void setSelectedItemStringVal(String selectedItemStringVal) {
		this.selectedItemStringVal = selectedItemStringVal;
	}
	public int getSelectedItemIntVal() {
		return this.SelectedItem.getValue()==null?0:Integer.parseInt(this.SelectedItem.getValue());
	}
	public void setSelectedItemIntVal(int selectedItemIntVal) {
		this.selectedItemIntVal = selectedItemIntVal;
	}

//	public int SelectedItemIntVal
//	{
//		get
//		{
//			return int.Parse(this.SelectedItem.Value);
//		}
//	}
//	public string SelectedItemStringVal
//	{
//		get
//		{
//			return this.SelectedItem.Value;
//		}
//	}
	
	
	public void BindAppEntities(BP.En.EntitiesNoName ens)
	{
		this.Items.clear();
		for (EntityNoName  en: ens.ToJavaListEnNo())
		{
			this.Items.add(new ListItem(en.getNo()+" "+en.getName(), en.getNo()) ) ; 
		}
	}
	/// <summary>
	/// 设置选择的值
	/// </summary>
	/// <param name="val"></param>
	public void SetSelectItem(Object val)
	{
		for(ListItem li:this.Items)
		{
			li.setSelected(false);

		}

		for(ListItem li:this.Items)
		{

			if (li.getValue()==  val.toString() )
			{
				li.setSelected(true);
				break;
			}
			else
			{
				li.setSelected(false);
			}
		}
	}
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("<select name=\""+this.getName()+"\" id=\""+this.getId()+"\" size=\"20\" multiple=\"multiple\" width=\"100%\">");
		for(ListItem li:Items){
			if(li!=null){
				sb.append(li.toString());
			}
		}
		sb.append("</select>");
		return sb.toString();
	}
	
}

