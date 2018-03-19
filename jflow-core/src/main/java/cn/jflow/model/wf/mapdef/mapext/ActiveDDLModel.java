package cn.jflow.model.wf.mapdef.mapext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.UIContralType;
import BP.Sys.SFDBSrc;
import BP.Sys.SFDBSrcs;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import cn.jflow.common.model.BaseModel;

public class ActiveDDLModel extends BaseModel{
	
	
	public ActiveDDLModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}


	public final String getFK_MapData() {
		return get_request().getParameter("FK_MapData");
	}
	public final String getOperAttrKey() {
		return get_request().getParameter("OperAttrKey");
	}
	public final String getRefNo() {
		return get_request().getParameter("RefNo");
	}
	

	public final String getExtType() {
		return MapExtXmlList.ActiveDDL;
	}

	/**
	 * 绑定下拉框sql
	 */
	public String bindSelectSql;
	/**
	 * 联动下拉值
	 */
	public List<Map<String,String>> linkAgeList=new ArrayList<Map<String,String>>();
	/**
	 * 数据源列表
	 */
	public List<Map<String,String>> dataSourceList=new ArrayList<Map<String,String>>();
	
	public int showDel;
	public String Lab = null;

	/**
	 * @Description: 初始化数据
	 * @author peixiaofeng
	 * @date 2016年5月24日
	 */
	public final void init(){
		MapExt me = new MapExt();
		int num=me.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.ExtType, this.getExtType(), MapExtAttr.AttrOfOper, this.getRefNo());
		this.setShowDel(num);
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		
		for(int i=0;i<attrs.size();i++){
			MapAttr attr=(MapAttr) attrs.get(i);
			if (attr.getUIVisible() == false) {
				continue;
			}
			if (attr.getUIIsEnable() == false) {
				continue;
			}
			if (attr.getUIContralType() != UIContralType.DDL) {
				continue;
			}
			if (attr.getKeyOfEn().equals(this.getRefNo())) {
				continue;
			}
			Map<String,String> map=new HashMap<String,String>();
			map.put("value", attr.getKeyOfEn());
			map.put("name", attr.getKeyOfEn()+ " - " + attr.getName());
			if(attr.getKeyOfEn().equals(me.getAttrsOfActive())){
				map.put("selected", "selected='selected'");
			}else{
				map.put("selected", "");
			}
			list.add(map);
		}
		this.setLinkAgeList(list);
		
		//设置数据源
		List<Map<String,String>> list1=new ArrayList<Map<String,String>>();
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		for(int j=0;j<srcs.size();j++){
			SFDBSrc src=(SFDBSrc) srcs.get(j);
			Map<String,String> m=new HashMap<String,String>();
			m.put("value", src.getNo());
			m.put("name", src.getName());
			if(src.getNo().equals(me.getDBSrc())){
				m.put("selected", "selected='selected'");
			}else{
				m.put("selected", "");
			}
			list1.add(m);
		}
		this.setDataSourceList(list1);
		
		this.setBindSelectSql(me.getDoc());
	}

	//*************************get()/set()**********************************************************//
	

	public String getBindSelectSql() {
		return bindSelectSql;
	}


	public void setBindSelectSql(String bindSelectSql) {
		this.bindSelectSql = bindSelectSql;
	}


	public List<Map<String, String>> getLinkAgeList() {
		return linkAgeList;
	}


	public void setLinkAgeList(List<Map<String, String>> linkAgeList) {
		this.linkAgeList = linkAgeList;
	}


	public List<Map<String, String>> getDataSourceList() {
		return dataSourceList;
	}


	public void setDataSourceList(List<Map<String, String>> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}


	public int getShowDel() {
		return showDel;
	}


	public void setShowDel(int showDel) {
		this.showDel = showDel;
	}

	
	
	
	
}
