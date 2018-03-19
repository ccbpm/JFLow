package cn.jflow.controller.app;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/App")
public class CreatCascaController {
	
	@RequestMapping(value = "/CreatCasca", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson casca(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		HashMap<String, Object> UCSys = new HashMap<String, Object>();
		
		try{
			//String url = request.getQueryString();
			//HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			String opt = "";
			Entity en = ClassFactory.GetEns(object.getEns_Name()).getGetNewEntity();
			Attr attr = en.getEnMap().GetAttrByKey(object.getAttr_Key1());
			if (attr.getMyFieldType() == FieldType.Enum) {//暂时枚举没有级联的
			}else if(attr.getMyFieldType() == FieldType.BindTable){
				String sql = "select * from " + attr.getUIBindKey();
				if(!"all".equals(object.getDdl_Value())){
					sql += " where " + attr.get_UIRefParentKeyValue() + " = '"+ object.getDdl_Value() +"'";
				}
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for(DataRow dr : dt.Rows){
					opt += "<option value=\"" + dr.getValue(attr.getUIRefKeyValue()) + "\">"+ dr.getValue(attr.getUIRefKeyText()) + "</option>";
				}
			}else{
				Entities ens = attr.getHisFKEns();
				QueryObject qo = new QueryObject(ens);
				if(!"all".equals(object.getDdl_Value())){
					qo.AddWhere(attr.get_UIRefParentKeyValue(), object.getDdl_Value());
				}
				qo.DoQuery();
				for (Entity e : ens.ToJavaListEn()){
					opt += "<option value=\"" + e.GetValStrByKey("No") + "\">"+ e.GetValStrByKey("Name") + "</option>";
				}
			}
			UCSys.put("data", opt) ;
		
	        j.setAttributes(UCSys);
	        return j;
		}catch(Exception e){
			j.setSuccess(false);
            j.setMsg("查询错误："+e.getMessage());
            return j;
		}
	}
	
	@RequestMapping(value = "/CreatTwoCasca", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson twoCasca(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		HashMap<String, Object> UCSys = new HashMap<String, Object>();
		
		try{
			String opt = "";
			Entity en = ClassFactory.GetEns(object.getEns_Name()).getGetNewEntity();
			Attr attr = en.getEnMap().GetAttrByKey(object.getAttr_Key2());
			if (attr.getMyFieldType() == FieldType.Enum) {//暂时枚举没有级联的
			}else if(attr.getMyFieldType() == FieldType.BindTable){
				String sql =  "select * from " + attr.getUIBindKey() + " where " + attr.get_UIRefParentKeyValue() + " = '"+ object.getDdl_Value() +"'";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for(DataRow dr : dt.Rows){
					opt += "<option value=\"" + dr.getValue(attr.getUIRefKeyValue()) + "\">"+ dr.getValue(attr.getUIRefKeyText()) + "</option>";
				}
			}else{
				Entities ens = attr.getHisFKEns();
				QueryObject qo = new QueryObject(ens);
				qo.AddWhere(attr.get_UIRefParentKeyValue(), object.getDdl_Value());
				qo.DoQuery();
				for (Entity e : ens.ToJavaListEn()){
					opt += "<option value=\"" + e.GetValStrByKey("No") + "\">"+ e.GetValStrByKey("Name") + "</option>";
				}
			}
			UCSys.put("data", opt) ;
			
	        j.setAttributes(UCSys);
	        return j;
		}catch(Exception e){
			j.setSuccess(false);
            j.setMsg("查询错误："+e.getMessage());
            return j;
		}
	}
	
	@RequestMapping(value = "/CreatThreeCasca", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson threeCasca(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		AjaxJson j = new AjaxJson();
		HashMap<String, Object> UCSys = new HashMap<String, Object>();
		
		try{
			String opt1 = "", opt2 = "";
			Entity en = ClassFactory.GetEns(object.getEns_Name()).getGetNewEntity();
			if(!StringHelper.isNullOrEmpty(object.getPk_Val())){
				en.setPKVal(object.getPk_Val());
				en.RetrieveFromDBSources();
			}
			Attr attr1 = en.getEnMap().GetAttrByKey(object.getAttr_Key1());//第二层
			Attr attr2 = en.getEnMap().GetAttrByKey(object.getAttr_Key2());//第三层
			
			String str = "";
			if (attr1.getMyFieldType() == FieldType.Enum) {//暂时枚举没有级联的
			}else if(attr1.getMyFieldType() == FieldType.BindTable){
				String sql = "select * from " + attr1.getUIBindKey();
				if(!"all".equals(object.getDdl_Value())){
					sql += " where " + attr1.get_UIRefParentKeyValue() + " = '"+ object.getDdl_Value() +"'";
				}
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for(DataRow dr : dt.Rows){
					String value = String.valueOf(dr.getValue(attr1.getUIRefKeyValue()));
					if(str.length()==0){
						str = String.valueOf(value);
					}
					if(value.equals(en.GetValStrByKey(object.getAttr_Key1()))){
						str = value;
						opt1 += "<option value=\"" + value + "\" selected=\"selected\">"+ dr.getValue(attr1.getUIRefKeyText()) + "</option>";
					}else{
						opt1 += "<option value=\"" + value + "\">"+ dr.getValue(attr1.getUIRefKeyText()) + "</option>";
					}
				}
			}else{
				Entities ens = attr1.getHisFKEns();
				QueryObject qo = new QueryObject(ens);
				if(!"all".equals(object.getDdl_Value())){
					qo.AddWhere(attr1.get_UIRefParentKeyValue(), object.getDdl_Value());
				}
				qo.DoQuery();
				for (Entity e : ens.ToJavaListEn()){
					String value = e.GetValStrByKey("No");
					if(str.length()==0){
						str = value;
					}
					if(value.equals(en.GetValStrByKey(object.getAttr_Key1()))){
						str = value;
						opt1 += "<option value=\"" + value + "\" selected=\"selected\">"+ e.GetValStrByKey("Name") + "</option>";
					}else{
						opt1 += "<option value=\"" + value + "\">"+ e.GetValStrByKey("Name") + "</option>";
					}
				}
			}
			
			if (attr2.getMyFieldType() == FieldType.Enum) {//暂时枚举没有级联的
			}else if(attr2.getMyFieldType() == FieldType.BindTable){
				String sql = "select * from " + attr2.getUIBindKey();
				if(!"all".equals(object.getDdl_Value()) && str.length()>0){
					sql += " where " + attr2.get_UIRefParentKeyValue() + " = '"+ str +"'";
				}
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for(DataRow dr : dt.Rows){
					String value = String.valueOf(dr.getValue(attr2.getUIRefKeyValue()));
					if(value.equals(en.GetValStrByKey(object.getAttr_Key2()))){
						opt2 += "<option value=\"" + value + "\" selected=\"selected\">"+ dr.getValue(attr2.getUIRefKeyText()) + "</option>";
					}else{
						opt2 += "<option value=\"" + value + "\">"+ dr.getValue(attr2.getUIRefKeyText()) + "</option>";
					}
					
				}
			}else{
				Entities ens = attr2.getHisFKEns();
				QueryObject qo = new QueryObject(ens);
				if(!"all".equals(object.getDdl_Value()) && str.length()>0){
					qo.AddWhere(attr2.get_UIRefParentKeyValue(), str);
				}
				qo.DoQuery();
				for (Entity e : ens.ToJavaListEn()){
					String value = e.GetValStrByKey("No");
					if(value.equals(en.GetValStrByKey(object.getAttr_Key2()))){
						opt2 += "<option value=\"" + value + "\" selected=\"selected\">"+ e.GetValStrByKey("Name") + "</option>";
					}else{
						opt2 += "<option value=\"" + value + "\">"+ e.GetValStrByKey("Name") + "</option>";
					}
					
				}
			}
			UCSys.put("data1", opt1) ;
			UCSys.put("data2", opt2) ;
			
			j.setAttributes(UCSys);
		    return j;
		}catch(Exception e){
			j.setSuccess(false);
            j.setMsg("查询错误："+e.getMessage());
            return j;
		}
	}
	
	/*private HashMap<String, String> getParamsMap(String queryString, String enc) {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			String[] paramPair;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex,
							ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}
				paramPair = subStr.split("=");
				param = paramPair[0];
				value = paramPair.length == 1 ? "" : paramPair[1];
				try {
					value = URLDecoder.decode(value, enc);
				} catch (UnsupportedEncodingException ignored) {
				}
				paramsMap.put(param, value);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}*/
}
