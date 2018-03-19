package cn.jflow.system.ui.core;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;


public abstract class BaseWebControl {

	private String id = "";
	private String name = "";
	public boolean enabled = true;
	private String cssClass = "";
	private boolean readOnly;
	private boolean visible;
	private String text = "";
	private String value ="";
	private String title = "";
	public HashMap<String,String> attributes  = new HashMap<String,String>();
	
	public void addAttr(String key,String value)
	{
		if (StringUtils.equalsIgnoreCase(key, "class")){
			if (!StringUtils.containsIgnoreCase(" " + this.getCssClass(), value)){
				this.setCssClass(this.getCssClass() + " " + value);
			}
			return;
		}
		String old = attributes.get(key)==null?"":attributes.get(key);
		if(!old.equals("")){
			old += ";";
		}
		attributes.put(key, old+value);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		this.name = id;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getName() {
		if(name.equals(""))
			return id;
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.enabled = isEnabled;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public String buildAttributes()
	{
		StringBuilder str = new StringBuilder();
		Iterator itor = this.attributes.keySet().iterator();  
		while(itor.hasNext())  
		{  
		  String key = (String) itor.next();  
		  String value = this.attributes.get(key);
		  str.append(key+"=\""+value+"\" ");
		}
		return str.toString();
	}
	
	public abstract String toString();
	
}
