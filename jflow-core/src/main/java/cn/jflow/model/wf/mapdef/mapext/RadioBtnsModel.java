package cn.jflow.model.wf.mapdef.mapext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.SysEnums;
import BP.Sys.FrmRBs;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapExtXmlList;
import cn.jflow.common.model.BaseModel;

public class RadioBtnsModel extends BaseModel{
	
	
	public RadioBtnsModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}


	public final String getFK_MapData() {
		return get_request().getParameter("FK_MapData");
	}
	public final String getOperAttrKey() {
		return get_request().getParameter("OperAttrKey");
	}
	public final String getKeyOfEn() {
		return get_request().getParameter("KeyOfEn");
	}
	

	public final String getExtType() {
		return MapExtXmlList.DDLFullCtrl;
	}
	
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 高级设置 js表达式文本域
	 */
	public String jsTextArea;
	public String radionChecked1;
	public String radionChecked2;
	public String radionChecked3;
	/**
	 * 字段列表
	 */
	public List<Map<String,String>> filedList=new ArrayList<Map<String,String>>();
	

	/**
	 * @Description: 初始化数据
	 * @author peixiaofeng
	 * @date 2016年5月23日
	 */
	public final void init(){
		
		MapAttr attr =new MapAttr(this.getFK_MapData()+"_"+this.getKeyOfEn());
		this.setTitle(attr.getName());
		FrmRBs rbs=new FrmRBs(this.getFK_MapData(),this.getKeyOfEn());
		SysEnums ses = new 	SysEnums(attr.getUIBindKey());	
		//以上不知道什么用处
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		MapAttrs attrs =new MapAttrs(this.getFK_MapData());
		for(int i=0;i<attrs.size();i++){
			MapAttr myAttr=(MapAttr) attrs.get(i);
			Map<String,String> map=new HashMap<String,String>();
			map.put("KeyOfEn", myAttr.getKeyOfEn());
			map.put("FiledName", myAttr.getName());
			list.add(map);
		}
		this.setFiledList(list);
		
	}

/*************************get()/set()**********************************************************/

	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getJsTextArea() {
		return jsTextArea;
	}
	public void setJsTextArea(String jsTextArea) {
		this.jsTextArea = jsTextArea;
	}


	public List<Map<String, String>> getFiledList() {
		return filedList;
	}


	public void setFiledList(List<Map<String, String>> filedList) {
		this.filedList = filedList;
	}


	
	
	
}
