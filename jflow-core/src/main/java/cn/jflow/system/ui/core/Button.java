package cn.jflow.system.ui.core;


public class Button extends BaseWebControl{

	private String text;
	private boolean visible = true;
	private String type = "";

	public Button(){
	}
	
	public Button(String key){
		this.setId(key);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		String sType = "";
		if(this.getType().equals(""))
			sType = "button";
		else
			sType = this.getType();
		str.append("<input type=\""+sType+"\" ");
		str.append(" id=\""+this.getId()+"\" ");
		str.append(" name = \""+this.getName()+"\" ");
		str.append(" value = \""+this.getText()+"\"");
		if(!this.getVisible())
			str.append(" style = \"display:none; \"");
		if(!this.getCssClass().equals(""))
			str.append(" class = \""+this.getCssClass()+"\" ");
		if(this.getReadOnly() || !this.getEnabled())
			str.append(" readonly=\"readonly\" ");
//		if(this.getImageUrl()!=null && !this.getImageUrl().equals(""))
//			str.append("scr="+this.getImageUrl()+" ");
		str.append(this.buildAttributes());
		str.append(">");
		str.append("</input>");
		return str.toString();
	}

}
