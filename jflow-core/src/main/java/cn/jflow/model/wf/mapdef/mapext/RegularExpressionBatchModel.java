package cn.jflow.model.wf.mapdef.mapext;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import cn.jflow.common.model.BaseModel;

public class RegularExpressionBatchModel extends BaseModel {

	public RegularExpressionBatchModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}

	public List<MapAttr> attrList=new ArrayList<MapAttr>();
	
	public void init(){
		attrList=new ArrayList<MapAttr>();
	    MapAttrs attrs = new MapAttrs(this.getFK_MapData());
	    for(int i=0;i<attrs.size();i++){
	    	MapAttr attr = (MapAttr) attrs.get(i);
	        if (attr.getUIVisible() == false)
	            continue;
	        if (attr.getMyDataType() != BP.DA.DataType.AppString)
	            continue;
	        
	        attrList.add(attr);
		}
	}

	public List<MapAttr> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<MapAttr> attrList) {
		this.attrList = attrList;
	}   
	
	
}
