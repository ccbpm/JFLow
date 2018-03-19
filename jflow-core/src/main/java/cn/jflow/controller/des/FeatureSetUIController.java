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

import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import BP.En.Attr;
import BP.WF.Template.NodeSheet;
import BP.WF.Template.NodeSheets;
import BP.WF.Template.NodeAttr;

@Controller
@RequestMapping("/DES")
public class FeatureSetUIController extends BaseController {

	@RequestMapping(value = "/FeatureSetUISave", method = RequestMethod.POST)
	public ModelAndView featureSetUISave(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			NodeSheets nds = new NodeSheets();
	        nds.Retrieve("FK_Flow", map.get("FK_Flow"));
	        NodeSheet mynd = new NodeSheet();
	           
	        Attr attr = null;
	        attr = mynd.getEnMap().GetAttrByKey(map.get("DoType"));
	        for (NodeSheet nd : nds.ToJavaList()){
	        	   if("Base".equals(map.get("DoType"))){
	        	   }else if("FormType".equals(map.get("DoType"))){
	        		   nd.SetValByKey(NodeAttr.FormType, request.getParameter("DDL_" + nd.getNodeID()));
                       nd.SetValByKey(NodeAttr.FormUrl, request.getParameter("TB_" + nd.getNodeID()));
                       nd.Update();
	        	   }else{
	        		   switch (attr.getUIContralType()){
		       		   		case TB:
		       		   			nd.SetValByKey(map.get("DoType"), request.getParameter("TB_" + nd.getNodeID()));
		       		   			break;
		       		   	    case CheckBok:
		       		   	    	Object obj = request.getParameter("CB_" + nd.getNodeID());
		       		   	    	if(null == obj){
		       		   	    		nd.SetValByKey(map.get("DoType"), false);
		       		   	    	}else{
		       		   	    		nd.SetValByKey(map.get("DoType"), true);
		       		   	    	}
		       		   	    	break;
		       		   	    case DDL:
		       		   	    	nd.SetValByKey(map.get("DoType"), request.getParameter("DDL_" + nd.getNodeID()));
		       		   	    	break;
		       		   	    default:
		                           break;
		       		   }
	        		   nd.Update();
	        	   }
	        }
			
	        mv.setViewName("redirect:" + "/WF/Admin/FeatureSetUI.jsp?"+url);
           
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mv;
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
