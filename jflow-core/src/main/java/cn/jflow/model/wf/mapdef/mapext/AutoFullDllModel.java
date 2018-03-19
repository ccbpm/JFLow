package cn.jflow.model.wf.mapdef.mapext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import cn.jflow.common.model.BaseModel;

public class AutoFullDllModel extends BaseModel{
	
	
	public AutoFullDllModel(HttpServletRequest request,HttpServletResponse response) {
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
		return MapExtXmlList.AutoFullDLL;
	}

	/**
	 * 自动填充sql
	 */
	public String tbSql;
	
	public List<Map<String,String>> DDL_DBSrc=new ArrayList<Map<String,String>>();
	
	public String Lab = null;

	/**
	 * @Description: 初始化数据
	 * @author peixiaofeng
	 * @date 2016年5月24日
	 */
	public final void init(){
		MapExt me = new MapExt();

		me.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.ExtType, this.getExtType(), MapExtAttr.AttrOfOper, this.getRefNo());
		this.setTbSql(me.getDoc());
		SysEnums ens = new SysEnums("DBSrcType");

		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<ens.size();i++){
			SysEnum en=(SysEnum) ens.get(i);
			Map<String,String> map=new HashMap<String, String>();
			String lab = en.getLab();
			if (lab.equals("SQLServer数据库"))
			{
				map.put("s_val","1");
				map.put("s_name",en.getLab());
				if("1".equals(me.getDBSrc())){
					map.put("selected","selected='selected'");
				}else{
					map.put("selected","");
				}
				list.add(map);
			} else if (lab.equals("应用系统主数据库(默认)"))
			{
				map.put("s_val","100");
				map.put("s_name",en.getLab());
				if("100".equals(me.getDBSrc())){
					map.put("selected","selected='selected'");
				}else{
					map.put("selected","");
				}
				list.add(map);
			} else if (lab.equals("WebService数据源"))
			{
				map.put("s_val","100");
				map.put("s_name",en.getLab());
				if("100".equals(me.getDBSrc())){
					map.put("selected","selected='selected'");
				}else{
					map.put("selected","");
				}
				list.add(map);
			} else if (lab.equals("Oracle数据库"))
			{
				map.put("s_val","2");
				map.put("s_name",en.getLab());
				if("2".equals(me.getDBSrc())){
					map.put("selected","selected='selected'");
				}else{
					map.put("selected","");
				}
				list.add(map);
			} else if (lab.equals("Informix数据库"))
			{
				map.put("s_val","4");
				map.put("s_name",en.getLab());
				if("4".equals(me.getDBSrc())){
					map.put("selected","selected='selected'");
				}else{
					map.put("selected","");
				}
				list.add(map);
			}
		}
		this.setDDL_DBSrc(list);

	}

//*************************get()/set()**********************************************************//
	public String getTbSql() {
		return tbSql;
	}


	public void setTbSql(String tbSql) {
		this.tbSql = tbSql;
	}


	public List<Map<String, String>> getDDL_DBSrc() {
		return DDL_DBSrc;
	}


	public void setDDL_DBSrc(List<Map<String, String>> dDL_DBSrc) {
		DDL_DBSrc = dDL_DBSrc;
	}
	
	
	
	
}
