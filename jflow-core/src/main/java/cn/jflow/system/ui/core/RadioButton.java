package cn.jflow.system.ui.core;

import BP.Tools.StringHelper;


public class RadioButton extends BaseWebControl{

	public boolean checked = false;
	
	public boolean AutoPostBack = false;
	

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	public boolean isAutoPostBack() {
		return AutoPostBack;
	}

	public void setAutoPostBack(boolean autoPostBack) {
		this.AutoPostBack = autoPostBack;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("<div class=\"am-radio\">");
		str.append("<input type=\"radio\" ");
		str.append(" id=\""+this.getId()+"\" ");
		if(!StringHelper.isNullOrEmpty(getGroupName())){
			str.append(" name = \""+this.getGroupName()+"\" ");
		}else{
			str.append(" name = \""+this.getName()+"\" ");
		}
		
		if(this.checked)
			str.append(" checked = \"checked\" ");
		if(!StringHelper.isNullOrEmpty(getCssClass()))
			str.append(" class = \""+this.getCssClass()+"\" ");
		if(this.getReadOnly() || !getEnabled())
			str.append(" disabled=\"disabled\" ");
		str.append(this.buildAttributes());
		if(!StringHelper.isNullOrEmpty(getValue())){
			str.append(" value = \"");
			str.append(getValue());
			str.append("\" ");
		}
		str.append(">");
		str.append("<label for=\"");
		str.append(getId());
		str.append("\"");
		if(!StringHelper.isNullOrEmpty(getTitle())){
			str.append(" title=\""+this.getTitle()+"\"");
		}
		str.append(">"+this.getText()+"</label>");
		str.append("</input>");
		return str.append("</div>").toString();
	}
	
	private String GroupName;

	public void setGroupName(String groupName) {
		GroupName = groupName;
		if(StringHelper.isNullOrEmpty(getValue())){
			setValue(getId());
		}
		
	}

	public String getGroupName() {
		return GroupName;
	}


}
