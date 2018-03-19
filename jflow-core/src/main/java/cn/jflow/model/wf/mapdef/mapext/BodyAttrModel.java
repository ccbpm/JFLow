package cn.jflow.model.wf.mapdef.mapext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapData;
import cn.jflow.common.model.BaseModel;

public class BodyAttrModel extends BaseModel{

	public BodyAttrModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}

	public String text;
	
	public void init(){
		MapData md = new MapData(this.getFK_MapData());
		text = md.getBodyAttr();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
