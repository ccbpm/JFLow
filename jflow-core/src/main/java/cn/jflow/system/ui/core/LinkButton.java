package cn.jflow.system.ui.core;

import BP.Tools.StringHelper;

/**
 * 
 * @author Petter
 */
public class LinkButton extends BaseWebControl{

	private String text;
	private String href;
	private boolean isPlain = false;
	
	public LinkButton(){
		this(true, null, "");
	}
	public LinkButton(boolean isPlain){
		this(isPlain,null,"");
	}
	public LinkButton(boolean isPlain, String id){
		this(isPlain,id,"");
	}
	public LinkButton(boolean isPlain, String id, String text){
		this.isPlain = isPlain;
		setId(id);
		setName(id);
		this.text = text;
		if(id != null){
			setIconCls();
		}
	}
	
	private void setIconCls(){
		String str = getId();
		switch (NamesOfBtn.getEnumByCode(str))
        {
		    case Save:
		    case SaveAndNew:
		        SetDataOption("iconCls", "icon-save");
		        break;
		    case SaveAndClose:
		        SetDataOption("iconCls", "icon-save-close");
		        break;
		    case Delete:
		        SetDataOption("iconCls", "icon-delete");
		        break;
		    case Reomve:
		        SetDataOption("iconCls", "icon-remove");
		        break;
		    case New:
		        SetDataOption("iconCls", "icon-add");
		        break;
		    case Search:
		        SetDataOption("iconCls", "icon-search");
		        break;
		    case Cancel:
		        SetDataOption("iconCls", "icon-cancel");
		        break;
		    case Print:
		        SetDataOption("iconCls", "icon-print");
		        break;
		    case Back:
		        SetDataOption("iconCls", "icon-back");
		        break;
		    case UnDo:
		        SetDataOption("iconCls", "icon-undo");
		        break;
		    case Edit:
		        SetDataOption("iconCls", "icon-edit");
		        break;
		    case Help:
		        SetDataOption("iconCls", "icon-help");
		        break;
		    case Up:
		        SetDataOption("iconCls", "icon-up");
		        break;
		    case Down:
		        SetDataOption("iconCls", "icon-down");
		        break;
		    case Excel:
		    case Export:
		        SetDataOption("iconCls", "icon-excel");
		        break;
		    case Open:
		        SetDataOption("iconCls", "icon-open");
		        break;
		    case Accept:
		        SetDataOption("iconCls", "icon-accept");
		        break;
		    case Refuse:
		        SetDataOption("iconCls", "icon-refuse");
		        break;
		    case Seal:
		        SetDataOption("iconCls", "icon-seal");
		        break;
		    case Picture:
		        SetDataOption("iconCls", "icon-picture");
		        break;
		    case FlowImage:
		        SetDataOption("iconCls", "icon-flow");
		        break;
		    case Download:
		        SetDataOption("iconCls", "icon-download");
		        break;
		    case Setting:
		        SetDataOption("iconCls", "icon-property");
		        break;
		    case Update:
		        SetDataOption("iconCls", "icon-accept");
		        break;
		    default:
		        break;
        }
	}
	
	public void SetDataOption(String paramName, String paramValue){
		addAttr(paramName, paramValue);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<a ");
		if(!this.getEnabled() || getReadOnly()){
			sb.append("href=\"#\" ");
			sb.append("disabled = \"disabled\" ");
		}else{
			if(!StringHelper.isNullOrEmpty(href)){
				sb.append("href=\"javascript:"+href+"\" ");
			}else{
				sb.append("href=\"javascript:void(0)\" ");
			}
		}
		sb.append("id=\""+this.getId()+"\" ");
		sb.append("name = \""+this.getName()+"\" ");
		sb.append("class = \"easyui-linkbutton\" ");
		if(this.isPlain){
			sb.append("plain=\"true\" ");
		}
		sb.append(this.buildAttributes());
		sb.append(">");
		sb.append(text);
		sb.append("</a>");
		return sb.toString();
	}
}
