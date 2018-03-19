package cn.jflow.system.ui.core;


public class ListItem extends BaseWebControl{

	private String text;
	private String value;
	private boolean enabled;
	private boolean selected;
	
	public ListItem()
	{
		
	}
	 public ListItem(String text)
	 {
		 this.text = text;
	 }
	public ListItem(String text, String value)
	{
		this.text = text;
		this.value = value;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public ListItem(String text, String value, boolean enabled)
	{
		this.text = text;
		this.value = value;
		this.enabled = enabled;
	}
	public String getText() {
            if (text != null) 
                return text;
            if (value != null) 
                return value; 
            return "";
    } 

	public void setText(String value)
	{
            text = value;
	}
	


    public String getValue() { 
            if (value != null) 
                return value;
            if (text != null)
                return text;
            return ""; 
    }
    
    public void setValue(String value)
    {
            this.value = value; 

    }
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<option value=\"");
		buffer.append(value);
		buffer.append("\"");
		if(selected){
			buffer.append(" selected");
		}
		buffer.append(">");
		buffer.append(text);
		buffer.append("</option>");
		return buffer.toString();
	}

}
