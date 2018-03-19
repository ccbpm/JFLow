package cn.jflow.system.ui.core;

public class FileUpload extends BaseWebControl{

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("<input type=\"file\"");
		str.append(" id=\""+this.getId()+"\" ");
		str.append(" name=\""+this.getName()+"\" ");
		str.append(this.buildAttributes());
		str.append(">");
		str.append("</input>");
		return str.toString();
	}

}
