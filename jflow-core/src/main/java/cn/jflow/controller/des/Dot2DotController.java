package cn.jflow.controller.des;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.TempObject;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;
import BP.En.AttrOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;

@Controller
@RequestMapping("/DES")
public class Dot2DotController {

	@RequestMapping(value = "/Dot2DotSave", method = RequestMethod.POST)
	public ModelAndView dot2DotSave(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			String pk = this.getPK(map);
			AttrOfOneVSM attr = this.getAttrOfOneVSM(map.get("EnName"), map.get("AttrKey"));
			Entities ensOfMM = attr.getEnsOfMM();
	        ensOfMM.Delete(attr.getAttrOfOneInMM(), pk);
			
			HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			 //执行保存.
            // edited by 2015.1.6
            // 增加去除相同项的逻辑，比如同一个人员属于多个部门，则保存的时候则可能会选中有多个相同选择项
			String key = "";
			List<String> keys = new ArrayList<String>();
			for(Map.Entry<String, BaseWebControl> entry : controls.entrySet()){ 
				String id = entry.getKey();
				//BaseWebControl control = entry.getValue();
				
				if(!id.contains("CB_")) continue;
				
				//key = id.split("_")[1];
				key = id.replace("CB_", "");
				if("FK_Dept".equals(request.getParameter("DDL_Group")))
				{
					if(key.contains("_")){
						key = key.substring(0,key.lastIndexOf("_"));
					}
				}
				
				if ("EN".equals(key) || "SE".equals(key) || keys.contains(key))continue;
				
				Object cb = request.getParameter(id);// 选种了为on 不选中为null
				if (null == cb)continue;
					
			    Entity en1 = ensOfMM.getGetNewEntity();
                en1.SetValByKey(attr.getAttrOfOneInMM(), pk);
                en1.SetValByKey(attr.getAttrOfMInMM(), key);
                en1.Insert();

                keys.add(key);
			}
			//更新entity ,防止有业务逻辑出现.
            Entity enP = ClassFactory.GetEn(map.get("EnName"));
            if (enP.getEnMap().getEnType() != EnType.View){
                enP.SetValByKey(enP.getPK(), pk);// =this.PK;
                enP.Retrieve(); //查询。
              
                enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
            }
            
            if (url.contains("ShowWay="))
            	url = url.replace("&ShowWay=", "&1=");
            mv.setViewName("redirect:" + "/WF/Comm/RefFunc/Dot2Dot.jsp?"+url+ "&ShowWay=" + map.get("DDL_Group"));
            
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mv;
	}
	
	private AttrOfOneVSM getAttrOfOneVSM(String ensName, String attrKey) throws Exception{
		 Entity en = ClassFactory.GetEn(ensName);
		 for(AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM()){
			 //System.out.println(attr.getEnsOfMM().getClass().getName());
			if ((attr.getEnsOfMM().getClass().getName()).equals(attrKey)){
               return attr;
           }
		 }
		 throw new Exception("@没有找到对应属性");
	}
	
	private String getPK(HashMap<String, String> map){
		String pk = map.get("PK");
		if(null == pk){
			pk = map.get("No");
		}
		if(null == pk){
			pk = map.get("RefNo");
		}
		if(null == pk){
			pk = map.get("OID");
		}
		if(null == pk){
			pk = map.get("MyPK");
		}
		if(null == pk){
			Entity en = ClassFactory.GetEn(map.get("EnName"));
			pk = map.get(en.getPK());
		}
		return pk;
	}
	
	private HashMap<String, String> getParamsMap(String queryString, String enc) {
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
	}
}


