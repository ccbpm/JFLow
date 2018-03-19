package cn.jflow.system.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.FileUpload;
import cn.jflow.system.ui.core.ImageButton;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;

public class UiFatory {

	private Map<String,Object> tmpMap;
	private List<Object> tmpList;
	
	public UiFatory(){
		tmpMap = new HashMap<String,Object>();
		tmpList = new ArrayList<Object>();
	}

	public TextBox creatTextBox(String id){
		TextBox tb = new TextBox();
		tb.setId(id);
		tb.setName(id);
		tmpMap.put(id, tb);
		return tb;
	}
	
	public ListBox createListBox(String id){
		ListBox lb=new ListBox();
		lb.setId(id);
		lb.setName(id);
		tmpMap.put(id, lb);
		return lb;
	}
	
	public CheckBox creatCheckBox(String id){
		CheckBox cb = new CheckBox();
		cb.setId(id);
		cb.setName(id);
		tmpMap.put(id, cb);
		return cb;
	}
	
	public DDL creatDDL(String id){
		DDL ddl = new DDL();
		ddl.setId(id);
		ddl.setName(id);
		tmpMap.put(id, ddl);
		return ddl;
	}
	
	public Button creatButton(String id){
		Button bt = new Button();
		bt.setId(id);
		bt.setName(id);
		tmpMap.put(id, bt);
		return bt;
	}
	/**
	 * 创建LinkButton
	 * @param isPlain
	 * @param id
	 * @return
	 */
	public LinkButton creatLinkButton(boolean isPlain, String id){
		LinkButton lb = new LinkButton(isPlain, id);
		tmpMap.put(id, lb);
		return lb;
	}
	
	public LinkButton creatLinkButton(boolean isPlain, String id, String text){
		LinkButton lb = new LinkButton(isPlain, id, text);
		tmpMap.put(id, lb);
		return lb;
	}
	
	public ImageButton creatImageButton(String id){
		ImageButton ib = new ImageButton();
		ib.setId(id);
		ib.setName(id);
		tmpMap.put(id, ib);
		return ib;
	}
	
	public RadioButton creatRadioButton(String id){
		RadioButton rbt = new RadioButton();
		rbt.setId(id);
		rbt.setName(id);
		tmpMap.put(id, rbt);
		return rbt;
	}
	
	public Label creatLabel(String id){
		Label lb = new Label();
		lb.setId(id);
		lb.setName(id);
		tmpMap.put(id,lb);
		return lb;
	}
	
	public FileUpload creatFileUpload(String id){
		FileUpload fu = new FileUpload();
		fu.setId(id);
		fu.setName(id);
		tmpMap.put(id,fu);
		return fu;
	}
	
	public void append(Object obj){
		this.tmpList.add(obj);
	}
	
	public String ListToString(){
		StringBuilder sb = new StringBuilder();
		if(null != tmpList && tmpList.size()>0){
			for(Object obj : tmpList){
				sb.append(obj.toString());
			}
		}
		return sb.toString();
	}
	
	public Object GetUIByID(String id){
		return this.tmpMap.get(id);
	}
	
	public Map<String, Object> getTmpMap() {
		return tmpMap;
	}
	public void setTmpMap(Map<String, Object> tmpMap) {
		this.tmpMap = tmpMap;
	}
	public List<Object> getTmpList() {
		return tmpList;
	}

	public void setTmpList(List<Object> tmpList) {
		this.tmpList = tmpList;
	}

	public static void main(String[] args) {
		List<Object> PubList = new ArrayList<Object>();
		UiFatory ui = new UiFatory();
		
		CheckBox cb = ui.creatCheckBox("CB_1");
		cb.setText("123");
		
		PubList.add("456");
		PubList.add(cb);
		
		CheckBox cb1 = (CheckBox) ui.GetUIByID("CB_1");
		cb.setText("333");
		//cb1.setReadOnly(true);
		
		//cb = ui.creatCheckBox("CB_2");
		
		//cb1.setText("555");
		System.out.println(cb1.getText());
	}
	
}
