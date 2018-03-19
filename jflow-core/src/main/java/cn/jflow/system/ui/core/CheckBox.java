package cn.jflow.system.ui.core;

import BP.Tools.StringHelper;


/**
 * @author Petter
 *
 */
public class CheckBox extends BaseWebControl{

	public boolean checked = false;
	
	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("<input type=\"checkbox\" ");
		str.append(" id=\""+this.getId()+"\" ");
		str.append(" name=\""+this.getName()+"\" ");
		if(this.checked)
			str.append(" checked=\"checked\" ");
		if(this.getCssClass()!=null)
			str.append(" class=\""+this.getCssClass()+"\" ");
		if(this.getReadOnly() || !this.getEnabled()) {
			str.append(" disabled=\"disabled\" ");
			str.append("onclick=\"return false\"");
		}
		if(!StringHelper.isNullOrEmpty(getValue())){
			str.append("value=\"");
			str.append(getValue()+"\"");
		}
		str.append(this.buildAttributes());
		str.append(">");
		str.append("<label for=\""+this.getId()+"\"");
		if(getTitle() != null && !"".equals(getTitle())){
			str.append(" title=\""+this.getTitle()+"\"");
		}
		str.append(">"+this.getText()+"</label>");
		return str.toString();
	}
}
