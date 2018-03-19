package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import cn.jflow.common.model.BaseModel;

public class AutoFullNewModel extends BaseModel {

	public AutoFullNewModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}

	
	public final String getDoType() {
		return get_request().getParameter("DoType");
	}
	public final String getFType() {
		return get_request().getParameter("FType");
	}
	public final String getIDX() {
		return get_request().getParameter("IDX");
	}
	
	public final String  getFK_MapData() {
		return get_request().getParameter("FK_MapData");
	}
	
	public String radioCheck1;
	public String radioCheck2;
	public String bdsTextArea;
	
	/**
	 * @Description: 初始化页面
	 * @author peixiaofeng
	 * @date 2016年5月23日
	 */
	public final void init(){
		MapAttr mattrNew = new MapAttr(this.getRefNo());
		MapExt me = new MapExt();
		me.setMyPK(this.getRefNo() + "_AutoFull");
		me.RetrieveFromDBSources();
		me.setFK_MapData(this.getFK_MapData());
		me.setAttrOfOper(mattrNew.getKeyOfEn());
		me.setExtType(MapExtXmlList.AutoFull);
		this.setBdsTextArea("");
		if ("0".equals(me.getTag())) {
			this.setRadioCheck1("checked='checked'");
		}
		else if ("1".equals(me.getTag())) {
			this.setRadioCheck2("checked='checked'");
			this.setBdsTextArea(me.getDoc());
		}
		
	}

	
	
	

	public String getRadioCheck1() {
		return radioCheck1;
	}


	public void setRadioCheck1(String radioCheck1) {
		this.radioCheck1 = radioCheck1;
	}


	public String getRadioCheck2() {
		return radioCheck2;
	}


	public void setRadioCheck2(String radioCheck2) {
		this.radioCheck2 = radioCheck2;
	}


	public String getBdsTextArea() {
		return bdsTextArea;
	}


	public void setBdsTextArea(String bdsTextArea) {
		this.bdsTextArea = bdsTextArea;
	}
	
	
	
}
