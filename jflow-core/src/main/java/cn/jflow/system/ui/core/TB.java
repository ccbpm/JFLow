package cn.jflow.system.ui.core;

import java.math.BigDecimal;

import BP.DA.DataType;
import BP.En.Attr;
import BP.En.TBType;
import BP.Sys.MapAttr;
/** 
 BPListBox 的摘要说明。
 
*/
//[System.Drawing.ToolboxBitmap(typeof(System.Web.UI.WebControls.TextBox))]
public class TB extends TextBox
{
		///#region 处理 DataHelpKey 的扩展属性
	private String RefKey = "";
	private String RefText = "";
	private String DataHelpKey = "";
	private String AttrKey;
	private String Tag = "";
	private boolean HelpKey = false;
	private String EnsName = "";

	/** 
	 TB类型
	 
	*/
	//private TBType _ShowType = TBType.TB;
	
		
	public TB()
	{
		//this.CssClass="TB"+WebUser.Style;
		if (this.getTextMode() == TextBoxMode.MultiLine)
		{
			//this.Attributes["onkeydown"] = "javascript:if(event.keyCode == 13) event.keyCode = 9";
			this.addAttr("onkeydown","javascript:if(event.keyCode == 13) event.keyCode = 9");
		}

		//this.Attributes["onmouseover"] = "TBOnfocus(this)";
		//this.Attributes["onmouseout"] = "TBOnblur(this)";
	}
	public final void LoadMapAttr(Attr attr)
	{
		this.setMaxLength(attr.getMaxLength());
		this.setAttrKey(attr.getKey());
		this.setReadOnly(attr.getUIIsReadonly());
		this.addAttr("width", ""+attr.getUIWidth());
		this.addAttr("size", ""+attr.getUIWidth());//?
		this.setVisible(attr.getUIVisible());
		this.setDataHelpKey(attr.getUIBindKey());
		if (attr.getUIWidth() == 0)
		{
			//this.Columns = attr.MaxLength;
			this.setCols(attr.getMaxLength());
		}
		else
		{
			//this.Columns = attr.UIWidthInt;
			this.setCols(attr.getUIWidthInt());
		}

		switch (attr.getMyDataType())
		{
			case DataType.AppInt:
			case DataType.AppFloat:
			case DataType.AppDouble:
				this.setShowType(TBType.Num);
				break;
			case DataType.AppDate:
				this.setShowType(TBType.Date);
				this.addAttr("onfocus","WdatePicker();");
				break;
			case DataType.AppDateTime:
				this.setShowType(TBType.DateTime);
				this.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
				break;
			case DataType.AppMoney:
			case DataType.AppRate:
				this.setShowType(TBType.Moneny);
				break;
			case DataType.AppString:
				this.setShowType(TBType.TB);
				break;
			default:
				this.setShowType(TBType.TB);
				break;
		}
		//this.PreRender += new System.EventHandler(this.TBPreRender);
	}
	public final void LoadMapAttr(MapAttr attr)
	{
		this.setMaxLength(attr.getMaxLen());
		this.setAttrKey(attr.getKeyOfEn());
		this.setReadOnly(attr.getUIIsEnable());
		this.setVisible(attr.getUIIsEnable());
		this.setDataHelpKey(attr.getUIBindKey());

		switch (attr.getMyDataType())
		{
			case DataType.AppInt:
			case DataType.AppFloat:
			case DataType.AppDouble:
				this.setShowType(TBType.Num);
				break;
			case DataType.AppDate:
				this.setShowType(TBType.Date);
				this.addAttr("onfocus","WdatePicker();");
				break;
			case DataType.AppDateTime:
				this.setShowType(TBType.DateTime);
				this.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
				break;
			case DataType.AppMoney:
			case DataType.AppRate:
				this.setShowType(TBType.Moneny);
				break;
			case DataType.AppString:
				this.setShowType(TBType.TB);
				break;
			default:
				break;
		}
		//this.PreRender += new System.EventHandler(this.TBPreRender);
	}
		///#endregion

		///#region 处理不同的数值类型(在页面呈现时间)
	/*private void TBPreRender(Object sender, System.EventArgs e)
	{
		if (this.getIsHelpKey() == false)
		{
			return;
		}

		if (this.ReadOnly)
		{
			if (this.getText().equals("&nbsp;"))
			{
				this.setText(null);
			}

			if (this.TextMode == TextBoxMode.MultiLine)
			{
				//this.Attributes["style"]="height=100%;width=100%;";
				this.Attributes["onkeydown"] += " return ; if(event.keyCode==13)  event.keyCode=13;";
			}
			else
			{
				// this.CssClass = "TBReadonly" + WebUser.Style;
				this.CssClass = "TBReadonly"; // +WebUser.Style;
			}
			return;
		}

		if (this.getText().equals("&nbsp;"))
		{
			this.setText(null);
		}

		//RenderJavaScript();
		String script, url;
		String appPath = this.Page.Request.ApplicationPath; // System.Web.HttpContext.Current.Request.CurrentExecutionFilePath ;
		if (appPath.equals("/"))
		{
			return;
		}

		switch (getShowType())
		{
			case TBType.Ens: //如果是要制定的Ens.
				//this.Width=Unit.Pixel(this.DefaultWith);
				url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/UIEns.aspx?EnsName=" + this.getDataHelpKey() + "&IsDataHelp=1";
				script = " if ( event.button != 2)  return; str=" + this.ClientID + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 500px; dialogWidth:800px; dialogTop: 150px; dialogLeft: 170px; center: no; help: no'); if ( str==undefined) return ; " + this.ClientID + ".value=str ; ";
				this.Attributes["onmousedown"] = script;
				//this.ToolTip="右健高级查找并选择。";
				break;
			case TBType.EnsOfMany: //如果是要制定的Ens.
				//this.Width=Unit.Pixel(this.DefaultWith);
				url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/UIDataHelpEnsValues.aspx?EnsName=" + this.getDataHelpKey() + "&IsDataHelp=1&RefKey=" + this.getRefKey() + "&RefText=" + this.getRefText();
				script = " if ( event.button != 2)  return; str=" + this.ClientID + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 400px; dialogWidth:600px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; " + this.ClientID + ".value=str ; ";
				this.Attributes["onmousedown"] = script;
				//this.ToolTip =
				this.ToolTip = "右健高级查找并选择。";
				break;
			case TBType.Self:
				if (this.getDataHelpKey() == null)
				{
					throw new RuntimeException("@您没有指定要邦定Key.");
				}
				break;
			case TBType.Date:
				this.Columns = 10;
				this.MaxLength = 20;
				if (this.getText() == null || this.getText() == null)
				{
					this.setText(DataType.CurrentData);
				}

				if (this.ReadOnly == false)
				{
					this.Attributes["onfocus"] = "WdatePicker();";
				}


			  //  this.Attributes["onfocus"] = "calendar();";


				//this.Attributes["onmousedown"] = "javascript:ShowDateTime('" + appPath + "', this );";
				//this.Attributes[""] = "javascript:ShowDateTime('" + appPath + "', this );";
				break;
			case TBType.DateTime:
				this.Columns = 16;
				if (this.getText() == null || this.getText() == null)
				{
					this.setText(DataType.getCurrentDataTime());
				}

				if (this.ReadOnly == false)
				{
					this.Attributes["onfocus"] = "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});";
				}

				//this.Attributes["onmousedown"] = "javascript:ShowDateTime('" + appPath + "', this );";
				//if (this.ReadOnly == false)
				//{
				//    this.Attributes["onmousedown"] = "javascript:ShowDateTime('" + appPath + "',this);";
				//    this.MaxLength = 20;
				//    //this.Attributes["OnKeyPress"]="javascript:return VirtyDatetime(this);";
				//}
				break;
			case TBType.Email:
				if (this.getText() == null || this.getText() == null)
				{
					this.setText("@");
				}
				break;
			case TBType.Moneny:
				  this.MaxLength = 14;
				this.CssClass = "TBNum";
				this.Columns = 12;
				this.Attributes["OnKeyPress"] = "javascript:return VirtyNum(this);";
				this.Attributes["onblur"] = "this.value=VirtyMoney(this.value);";

				if (this.getText() == null || this.getText().equals("") || this.getText().equals("&nbsp;") || this.getText().equals("0"))
				{
					this.setText("0.00");
				}
				if (this.getText().indexOf(".") == -1)
				{
					this.setText(this.getText() + ".00");
				}
				break;
			case TBType.Decimal:
			case TBType.Float:
				this.MaxLength = 14;
				this.CssClass = "TBNum";
				this.Columns = 12;
				//this.Attributes["size"]="14";
				this.Attributes["OnKeyPress"] = "javascript:return VirtyNum(this);";
				if (this.getText() == null || this.getText().equals("") || this.getText().equals("&nbsp;") || this.getText().equals("0"))
				{
					this.setText("0.00");
				}
				if (this.getText().indexOf(".") == -1)
				{
					this.setText(this.getText() + ".00");
				}
				break;
			case TBType.Num:
			case TBType.Int:
				this.MaxLength = 14;
				this.CssClass = "TBNum";
				//this.Attributes["text-align"]="right";
				//this.Attributes["size"]="14";
				this.Columns = 8;
				this.Attributes["OnKeyPress"] = "javascript:return VirtyNum(this);";
				if (this.getText() == null || this.getText() == null)
				{
					this.setText("0");
				}
				break;
			case TBType.TB:
				if (this.getEnsName() != null && this.getIsHelpKey())
				{
					url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/HelperOfTB.aspx?EnsName=" + this.getEnsName() + "&AttrKey=" + this.getAttrKey();
					script = " if ( event.button != 2)  return; str=" + this.ClientID + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 500px; dialogWidth:850px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; " + this.ClientID + ".value=str ; ";
					this.Attributes["onmousedown"] = script;
					//this.ToolTip="右健选择或设置定义默认值。";
				}
				break;
			case TBType.Area:
				if (this.getEnsName() != null && this.getIsHelpKey())
				{
					url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/HelperOfTB.aspx?EnsName=" + this.getEnsName() + "&AttrKey=" + this.getAttrKey();
					script = " if ( event.button != 2)  return; str=" + this.ClientID + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 500px; dialogWidth:850px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; " + this.ClientID + ".value=str ; ";
					this.Attributes["onmousedown"] = script;
				}
				else
				{
					this.Rows = 8;
					this.TextMode = TextBoxMode.MultiLine;
				}
				break;
			default:
				break;
		}
//            
//            if (this.TextMode!= TextBoxMode.MultiLine)
//            {
//                this.Attributes.Add("onmouseover","DGTBOnOn(this)");
//                this.Attributes.Add("onmouseout","DGTBOnOut(this)");
//            }
//            
	}*/
		///#endregion

		///#region 取出扩展的属性（用于方便取信息）
	/** 
	 取出扩展TextExt属性
	 
	*/
	public final Object getTextExt()
	{
		return this.getText();
	}
	public final void setTextExt(Object value)
	{
		this.setText(value.toString());
	}
	/** 
	 取出扩展Int属性
	 
	*/
	public final int getTextExtInt()
	{
		return Integer.parseInt(this.getText());
	}
	public final void setTextExtInt(int value)
	{
		this.setText((new Integer(value)).toString());
	}
	/** 
	 取出扩展Float属性
	 
	*/
	public final float getTextExtFloat()
	{
		return Float.parseFloat(this.getText().trim());
	}
	public final void setTextExtFloat(float value)
	{
		this.setText((new Float(value)).toString());
	}
	/** 
	 取出扩展Float属性
	 
	*/
	public final java.math.BigDecimal getTextExtMoney()
	{
		return new BigDecimal(this.getText().trim());
	}
	public final void setTextExtMoney(java.math.BigDecimal value)
	{
		this.setText(value.toString());
	}
	/** 
	 取出扩展Decimal属性
	 
	*/
	public final java.math.BigDecimal getTextExtDecimal()
	{
		String str = this.getText().trim();
		if (str.length() == 0)
		{
			str = "0";
		}
		try
		{
			return new BigDecimal(str);
		}
		catch (java.lang.Exception e)
		{
			this.setText("0");
			return new BigDecimal("0");
		}
	}
	public final void setTextExtDecimal(java.math.BigDecimal value)
	{
		this.setText(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
	}
	/** 
	 取出扩展日期属性
	 
	*/
	public final String getTextExtDate()
	{
		return DataType.StringToDateStr(this.getText().trim());
	}
	public final void setTextExtDate(String value)
	{
		this.setText(DataType.StringToDateStr(value));
	}
	//#endregion
	
	private int width;
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int value)
	{
		width = value;
	}
	private int height;
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int value)
	{
		height = value;
	}
	
	public String getRefKey() {
		return RefKey;
	}
	public void setRefKey(String refKey) {
		RefKey = refKey;
	}
	public String getRefText() {
		return RefText;
	}
	public void setRefText(String refText) {
		RefText = refText;
	}
	public String getDataHelpKey() {
		return DataHelpKey;
	}
	public void setDataHelpKey(String dataHelpKey) {
		DataHelpKey = dataHelpKey;
	}
	public String getAttrKey() {
		return AttrKey;
	}
	public void setAttrKey(String attrKey) {
		AttrKey = attrKey;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public boolean isHelpKey() {
		return HelpKey;
	}
	public void setHelpKey(boolean helpKey) {
		HelpKey = helpKey;
	}
	public String getEnsName() {
		return EnsName;
	}
	public void setEnsName(String ensName) {
		EnsName = ensName;
	}
	//get set 
	
}