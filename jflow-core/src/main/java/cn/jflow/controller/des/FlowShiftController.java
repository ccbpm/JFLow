package cn.jflow.controller.des;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Port.Emp;
import BP.WF.Node;

@Controller
@RequestMapping("/DES")
public class FlowShiftController{

	@RequestMapping(value = "/FlowForward", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson forward(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			Emp emp = new Emp();
			emp.setNo(object.getTB_Emp());
			if (emp.RetrieveFromDBSources() == 0){
				j.setSuccess(false);
	            j.setMsg("人员编号输入错误："+object.getTB_Emp());
	            return j;
	        }else{
	        	Dev2Interface.Node_Shift(this.getFK_Flow(map),
						this.getFK_Node(map), this.getWorkID(map), this.getFID(map),
						emp.getNo(), object.getTB_Note());
	        	j.setMsg("已经成功的移交给："+object.getTB_Emp());
	        	return j;
	        }
		}catch(Exception e){
			j.setSuccess(false);
            j.setMsg("移交错误："+e.getMessage());
            return j;
		}
	}
	
	@RequestMapping(value = "/FlowSkip", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson skip(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			Emp emp = new Emp();
			emp.setNo(object.getTB_SkipToEmp());
			if (emp.RetrieveFromDBSources() == 0){
				j.setSuccess(false);
	            j.setMsg("人员编号输入错误："+object.getTB_SkipToEmp());
	            return j;
	        }else{
	        	Node node = new Node(object.getDDL_SkipToNode());
	        	
	        	Dev2Interface.Node_SendWork(this.getFK_Flow(map),
						 this.getWorkID(map), null, null, node.getNodeID(),
						emp.getNo());
	        	j.setMsg("已经成功的跳转给："+object.getTB_SkipToEmp()+" 跳转到："+node.getName());
	        	return j;
	        }
		}catch(Exception e){
			j.setSuccess(false);
            j.setMsg("移交错误："+e.getMessage());
            return j;
		}
	}
	
	
	
	public String getParameter(String key, HashMap<String, String> map){
		String value = map.get(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
	} 
	
	public final String getFK_Flow(HashMap<String, String> map){
		return getParameter("FK_Flow", map);
	}
	
	public  int getFK_Node(HashMap<String, String> map){
		String fk_node = getParameter("FK_Node", map);
		if("".equals(fk_node)){
			return 0;
		}else{
			return Integer.parseInt(fk_node);
		}
	}
	public  Long getFID(HashMap<String, String> map){
		String fid = getParameter("FID", map); 
		if("".equals(fid)){
			return 0l;
		}else{
			return Long.valueOf(fid);
		}
	}
	public  Long getWorkID(HashMap<String, String> map){
		String workid = getParameter("WorkID", map); 
		if("".equals(workid)){
			return 0l;
		}else{
			return Long.valueOf(workid);
		}
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


