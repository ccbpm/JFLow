package cn.jflow.controller.des;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;

@Controller
@RequestMapping("/DES")
public class RefDtlController {
	
	private final int PageSize = SystemConfig.getPageSize(); 

	@RequestMapping(value = "/DtlSave", method = RequestMethod.POST)
	public ModelAndView dtlSave(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			Entities dtls = ClassFactory.GetEns(map.get("EnsName"));
			Entity en = dtls.getGetNewEntity();
	        QueryObject qo = new QueryObject(dtls);
	        qo.AddWhere(map.get("RefKey"), map.get("RefVal"));
	        int num = qo.DoQuery(en.getPK(), this.PageSize, this.getPageIdx(map), false);
	        
	        // qo.DoQuery(en.PK, 12, this.PageIdx, false);
            BP.En.Map enMap = dtls.getGetNewEntity().getEnMap();
            
            int idx = 0;
            for (Entity dtl : Entities.convertEntities(dtls)){
                try{
                    idx++;

                    BaseModel.Copy(request, dtl, dtl.getPKVal().toString(), enMap, controls);
                    dtl.SetValByKey(map.get("RefKey"), map.get("RefVal"));
                    dtl.Update();
                }
                catch (Exception ex){
                	throw new RuntimeException("在保存(" + idx + ")行出现错误：" + ex.getMessage());
                }
            }
            
            en = BaseModel.Copy(request, en, "0", enMap, controls);
            en.setPKVal("");
            boolean isInsert = false;
            if (!en.getIsBlank()){
            	if (en.getIsNoEntity()){
                    if (en.getEnMap().getIsAutoGenerNo())
                        en.SetValByKey("No", en.GenerNewNoByKey("No"));
                }
            	
            	en.SetValByKey(map.get("RefKey"), map.get("RefVal"));
            	try{
                    en.Insert();
                    isInsert = true;
                }catch (Exception ex){
                	throw new RuntimeException("@在插入新行时出现错误：" + ex.getMessage());
                }
            	
            }
            
            int pageIdx = this.getPageIdx(map);
            if (isInsert && num == this.PageSize)
                pageIdx++;
		
            if (url.contains("PageIdx=")){
            	url = url.substring(0, url.indexOf("&PageIdx"));
            }
            
            mv.setViewName("redirect:" + "/WF/Comm/RefFunc/Dtl.jsp?"+url+ "&PageIdx=" + pageIdx);
            
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mv;
	}
	
	
	@RequestMapping(value = "/DtlDel", method = RequestMethod.POST)
	public ModelAndView dtlDel(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			Entities dtls = BP.En.ClassFactory.GetEns(map.get("EnsName"));
            QueryObject qo = new QueryObject(dtls);
            qo.AddWhere(map.get("RefKey"), map.get("RefVal"));
            qo.DoQuery("OID", SystemConfig.getPageSize(), this.getPageIdx(map), false);
            for (Entity dtl : Entities.convertEntities(dtls)){
                Object cb = request.getParameter("CB_" + dtl.getPKVal());// 选种了为on 不选中为null
 				if (null == cb)continue;
                
                dtl.Delete();
            }
			
            mv.setViewName("redirect:" + "/WF/Comm/RefFunc/Dtl.jsp?"+url);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mv;
	}
	
	
	private int getPageIdx(HashMap<String, String> map){
		String PageIdx = map.get("PageIdx");
		if(null == PageIdx || "".equals(PageIdx)){
			return 1;
		}
		return Integer.parseInt(PageIdx); 
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


