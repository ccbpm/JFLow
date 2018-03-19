package cn.jflow.system.ui.core;

/**
 * 
 * @author Petter
 */
public class ImageButton extends BaseWebControl{

	private String text="";
	private String href="";
	private String width="";
	private String imageUrl="";
	
	public ImageButton(){
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<a ");
		if(this.getEnabled()){
			sb.append("href=\"javascript:"+href+"\" ");
		}else{
			sb.append("href=\"javascript:void(0)\" ");
		}
		sb.append("id=\""+this.getId()+"\" ");
		sb.append("name = \""+this.getName()+"\" ");
		sb.append("class = \"easyui-linkbutton\" ");
		sb.append(this.buildAttributes());
		sb.append(">");
		sb.append("<img ");
		sb.append("src=\""+this.getImageUrl()+"\" ");
		sb.append("width = \""+this.getWidth()+"\" border='0' />");
		sb.append(text);
		sb.append("</a>");
		return sb.toString();
	}
}
