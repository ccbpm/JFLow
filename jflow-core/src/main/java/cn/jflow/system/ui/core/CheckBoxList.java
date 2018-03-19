package cn.jflow.system.ui.core;

import java.util.ArrayList;

public class CheckBoxList extends BaseWebControl{

	public ArrayList<ListItem> Items = new ArrayList<ListItem>();
	
	private String id;
	private String onclick;

	public CheckBoxList(String id){
		this.id = id;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("<table id=\""+getId()+"\"");
		str.append(this.buildAttributes()).append(">");
		str.append(this.buildCheckBox());
		str.append("</table>");
		return str.toString();
	}
	private String buildCheckBox(){
		StringBuilder str = new StringBuilder();
		for(ListItem item : this.Items){
			str.append("<tr><td>");
			CheckBox cb = new CheckBox();
			cb.setId(item.getId());
			cb.setName(item.getId());
			cb.setValue(item.getValue());
			cb.setText(item.getText());
			cb.addAttr(" onclick", getOnclick());
			
			if(item.getSelected()){
				cb.setChecked(true);
			} 
			str.append(cb.toString());
			str.append("</td></tr>");
		}
		return str.toString();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

}
