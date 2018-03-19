package cn.jflow.system.ui.core;

public class Label extends BaseWebControl{

	private String text = "";
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("<span  ");
		str.append(" id=\""+this.getId()+"\" ");
		str.append(">");
		str.append(this.getText());
		str.append("</span>");
		return str.toString();
	}
}
