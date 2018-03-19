package cn.jflow.system.ui.core;

import BP.DA.DataType;
import BP.En.Attr;
import BP.En.TBType;


public class TextBox extends BaseWebControl{


	private TextBoxMode textMode = TextBoxMode.SingleLine;
	// xiaozhoup add 20150104 start
	private boolean isHelpKey;
	private TBType showType;
	private int Columns;
	public int getColumns() {
		return Columns;
	}

	public void setColumns(int columns) {
		Columns = columns;
	}

	private String refKey = "";
	private String refText = "";
	private String attrKey = "";
	private String ensName;
	private String dataHelpKey = "";
	// xiaozhoup add 20150104 end
	private String text = "";
	private int rows = 0;
	private String font = "";
	private String backColor = "";
	private int cols = 0;//rows="3" cols="20"
	private int maxLength;

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public int getRows() {
		return rows;
	}

	public String getFont() {
		return font;
	}
	public String getEnsName() {
		return ensName;
	}

	public void setEnsName(String ensName) {
		this.ensName = ensName;
	}
	public void setFont(String font) {
		this.font = font;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextBoxMode getTextMode() {
		return textMode;
	}

	public void setTextMode(TextBoxMode textMode) {
		this.textMode = textMode;
	}
	
	public boolean getIsHelpKey(){
		return isHelpKey;
	}
	
	public String getRefKey() {
		return refKey;
	}
	
	public void setRefKey(String refKey) {
		this.refKey = refKey;
	}
	
	public String getRefText() {
		return refText;
	}
	
	public void setRefText(String refText) {
		this.refText = refText;
	}
	
	public String getAttrKey() {
		return attrKey;
	}
	
	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}
	
	public void setIsHelpKey(String isHelpKeyNumStr){
		if(isHelpKeyNumStr.equals("1")){
			this.isHelpKey = true;
		}else{
			this.isHelpKey = false;
		}
	}
	
	public String getDataHelpKey(){
		return dataHelpKey;
	}
	
	public void setDataHelpKey(String dataHelpKey){
		this.dataHelpKey = dataHelpKey;
	}
	

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		switch(this.getTextMode())
		{
			case SingleLine:
				str.append("<input type=\"text\" ");
				str.append(" id=\""+this.getId().trim()+"\" ");
				str.append(" name = \""+this.getName().trim()+"\" ");
				str.append(" class = \""+this.getCssClass()+"\" ");
				if(!this.getBackColor().equals(""))
					str.append(" backcolor = \""+this.getBackColor()+"\" ");
				if(!this.getFont().equals(""))
					str.append(" font = \""+this.getFont()+"\" ");
				if(this.getRows()>0)
					str.append(" rows = \""+this.getRows()+"\" ");
				if(this.getCols() > 0)
//					str.append(" size = \""+this.getCols()+"%\"");
				if(!this.getEnabled())
					str.append(" readonly = \"readonly\"");
				if(this.getReadOnly())
					str.append(" readonly = \"readonly\"");
//					str.append(" disabled=\"disabled\" ");
				if(this.getMaxLength() > 0){
					str.append(" maxlength=\""+getMaxLength()+"\" ");
				}
				str.append(this.buildAttributes());
				str.append(" value=\""+this.text+"\"");
				str.append(">");

				str.append("</input>");
				break;
			case MultiLine:
				//<textarea rows="3" cols="20">
				str.append("<textarea  ");
				str.append(" id=\""+this.getId().trim()+"\" ");
				str.append(" name = \""+this.getName().trim()+"\" ");
				str.append(" class = \""+this.getCssClass()+"\" ");
				if(!this.getEnabled())
					str.append(" disabled = \"disabled\"");
				if(this.getReadOnly())
					str.append(" disabled=\"disabled\" ");
				if(!this.getBackColor().equals(""))
					str.append(" backcolor = \""+this.getBackColor()+"\" ");
				if(!this.getFont().equals(""))
					str.append(" font = \""+this.getFont()+"\" ");
				if(this.getRows()>0)
					str.append(" rows = \""+this.getRows()+"\" ");
				if(this.getCols() > 0)
					str.append(" cols = \""+this.getCols()+"\" ");
				str.append(this.buildAttributes());
				str.append(">");
				str.append(this.text);
				str.append("</textarea>");
				break;
			case Password:
				//<input type="password" id="" name="" 属性 ></input>
				str.append("<input type=\"password\"");
				str.append(" id=\""+this.getId()+"\" ");
				str.append(" name = \""+this.getName()+"\" ");
				str.append(" class = \""+this.getCssClass()+"\" ");
				if(!this.getBackColor().equals(""))
					str.append(" backcolor = \""+this.getBackColor()+"\" ");
				if(!this.getFont().equals(""))
					str.append(" font = \""+this.getFont()+"\" ");
				if(this.getRows()>0)
					str.append(" rows = \""+this.getRows()+"\" ");
				if(this.getCols() > 0)
					str.append(" cols = \""+this.getCols()+"\" ");
				if(this.getMaxLength() > 0){
					str.append(" maxlength=\""+getMaxLength()+"\" ");
				}
				str.append(this.buildAttributes());
				str.append(">");
				str.append("</input>");
				break;
			case Hidden:
				str.append("<input type=\"hidden\" ");
				str.append(" id=\""+this.getId().trim()+"\" ");
				str.append(" name = \""+this.getName().trim()+"\" ");
				str.append(" class = \""+this.getCssClass()+"\" ");
				if(!this.getBackColor().equals(""))
					str.append(" backcolor = \""+this.getBackColor()+"\" ");
				if(!this.getFont().equals(""))
					str.append(" font = \""+this.getFont()+"\" ");
				if(this.getRows()>0)
					str.append(" rows = \""+this.getRows()+"\" ");
				if(this.getCols() > 0)
					str.append(" cols = \""+this.getCols()+"\" ");
				if(!this.getEnabled())
					str.append(" disabled = \"disabled\"");
				if(this.getReadOnly())
					str.append(" disabled=\"disabled\" ");
				str.append(this.buildAttributes());
				str.append(" value=\""+this.text+"\"");
				str.append(">");

				str.append("</input>");
				break;
			case Files:
				str.append("<input type=\"file\" ");
				str.append(" id=\""+this.getId().trim()+"\" ");
				str.append(" name=\""+this.getName().trim()+"\" ");
				str.append(" class=\""+this.getCssClass()+"\" ");
				if(!this.getBackColor().equals(""))
					str.append(" backcolor=\""+this.getBackColor()+"\" ");
				if(!this.getFont().equals(""))
					str.append(" font=\""+this.getFont()+"\" ");
				if(this.getRows()>0)
					str.append(" rows=\""+this.getRows()+"\" ");
				if(this.getCols() > 0)
					str.append(" cols=\""+this.getCols()+"\" ");
				if(!this.getEnabled())
					str.append(" disabled=\"disabled\"");
				if(this.getReadOnly())
					str.append(" disabled=\"disabled\" ");
				str.append(this.buildAttributes());
				str.append(">");

				str.append("</input>");
			
		}
		return str.toString();
	}
	
	 public final TBType getShowType(){
		 return showType;
	 }
	 
	 public final void setShowType(TBType value) {
		 this.showType = value;
		 if (this.showType == TBType.EnsOfMany) {
			 this.isHelpKey = true;
		 }

		 String appPath = "/Front/"; // System.Web.HttpContext.Current.Request.CurrentExecutionFilePath ;
		 String url = "";
		 String script = "";

		 switch (showType) {
			 case Ens: //如果是要制定的Ens.
							//this.Width=Unit.Pixel(this.DefaultWith);
				 url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/UIEns.jsp?EnsName=" + this.dataHelpKey + "&IsDataHelp=1";
				 script = " if ( event.button != 2)  return; str=" + getId() + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 500px; dialogWidth:800px; dialogTop: 150px; dialogLeft: 170px; center: no; help: no'); if ( str==undefined) return ; " + getId() + ".value=str ; ";
				 this.addAttr("onmousedown", script);
							//this.ToolTip="右健高级查找并选择。";
				 break;
			 case EnsOfMany: //如果是要制定的Ens.
							//this.Width=Unit.Pixel(this.DefaultWith);
				 url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/UIDataHelpEnsValues.jsp?EnsName=" + this.dataHelpKey + "&IsDataHelp=1&RefKey=" + refKey + "&RefText=" + refText;
				 script = " if ( event.button != 2)  return; str=" + getId() + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 400px; dialogWidth:600px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; " + getId() + ".value=str ; ";
				 this.addAttr("onmousedown", script);
							//this.ToolTip =
				 this.addAttr("title", "右健高级查找并选择。");
				// this.setToolTip("右健高级查找并选择。");
				 break;
			 case Self:
				 if (this.dataHelpKey == null) {
					 throw new RuntimeException("@您没有指定要邦定Key.");
				 }
				 break;
			 case Date:
				 setCols(10);
				 addAttr("maxlength", "20");
				 if (this.getText() == null || this.getText() == null) {
					 this.setText(DataType.getCurrentData());
				 }
				 this.addAttr("class", "TBcalendar");
				 break;
			 case DateTime:
				 this.addAttr("class", "TBcalendar");
						   // this.Columns = 16;
				 if (this.getText() == null || this.getText() == null) {
					 this.setText(DataType.getCurrentDataTime());
				 }
				 if (getReadOnly() == false) {
					 addAttr("maxlength", "20");
								//this.Attributes["OnKeyPress"]="javascript:return VirtyDatetime(this);";
				 }
				 this.addAttr("class", "TBcalendar");
				 break;
						//case Email:
						//    if (this.Text == null || this.Text == null)
						//        this.Text = "@";
						//    break;
			 case Moneny:
			 case Decimal:
			 case Float:
				 addAttr("maxlength", "14");
				 setCssClass("TBNum");
				 setCols(12);
							//this.Attributes["size"]="14";
				 //this.Attributes["OnKeyPress"] += "return VirtyNum(this);";
				 this.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,0);");
				 this.addAttr("onClick", "TB_ClickNum(this)");
				 this.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'float');");
				 if (this.getText() == null || this.getText().equals("") || this.getText().equals("&nbsp;") || this.getText().equals("0"))
				 {
					 this.setText("0.00");
				 }
				 if (this.getText().indexOf(".") == -1)
				 {
					 this.setText(this.getText() + ".00");
				 }

				 if (showType == TBType.Moneny)
				 {
					 this.addAttr("onblur", "this.value=VirtyMoney(this.value);");
					 //this.Attributes["onblur"] += "this.value=VirtyMoney(this.value);";
				 }
				 break;
			 case Num:
			 case Int:
				 addAttr("maxlength", "14");
				 setCssClass("TBNum");
							//this.Attributes["text-align"]="right";
							//this.Attributes["size"]="14";
				 setCols(8);
				 this.addAttr("onblur", "value=value.replace(/[^-?\\d]/g,'');TB_ClickNum(this,0);");
				 this.addAttr("onClick", "TB_ClickNum(this)");
				 this.addAttr("OnKeyPress", "javascript:return VirtyNum(this,'int');");
				 if (this.getText() == null || this.getText() == null)
				 {
					 this.setText("0");
				 }

				 if (this.getReadOnly())
				 {
					 this.addAttr("class", "TBNumReadonly");
				 }
				 else
				 {
					 this.addAttr("class", "TBNum");
				 }
				 break;
			 case TB:
				 if (this.ensName != null && this.isHelpKey)
				 {
					 url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/HelperOfTB.jsp?EnsName=" + ensName + "&AttrKey=" + this.attrKey;
					 script = " if ( event.button != 2)  return; str=" + getId() + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 500px; dialogWidth:850px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; " + getId() + ".value=str ; ";
					 addAttr("onmousedown", script);
								//this.ToolTip="右健选择或设置定义默认值。";
				 }
				 if (this.getReadOnly())
				 {
					 this.addAttr("class", "TBReadonly");
				 }
				 else
				 {
					 this.addAttr("class", "TB");
				 }
				 break;
			 case Area:
				 if (ensName != null && this.isHelpKey)
				 {
					 url = appPath + "Comm/RefFunc/DataHelp.htm?" + appPath + "Comm/HelperOfTB.jsp?EnsName=" + ensName + "&AttrKey=" + this.attrKey;
					 script = " if ( event.button != 2)  return; str=" + getId() + ".value;str= window.showModalDialog('" + url + "&Key=\'+str, '','dialogHeight: 500px; dialogWidth:850px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; " + getId() + ".value=str ; ";
					 addAttr("onmousedown", script);
				 }
				 else
				 {
					 setRows(8);
					 setTextMode(TextBoxMode.MultiLine);
				 }
				 break;
			 default:
				 break;
		 }
	 }
	 
	    public void LoadMapAttr(Attr attr)
        {
            this.setMaxLength(attr.getMaxLength());
            this.setAttrKey(attr.getKey());
            this.setReadOnly(attr.getUIIsReadonly());
            this.addAttr("width", ""+attr.getUIWidth());
            this.setVisible(attr.getUIVisible());
            this.setDataHelpKey(attr.getUIBindKey());
            if (attr.getUIWidth() == 0)
                this.setCols(attr.getMaxLength());
            else
                this.setCols(attr.getUIWidthInt());

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
//            this.PreRender += new System.EventHandler(this.TBPreRender);
        }
	 
	
}
