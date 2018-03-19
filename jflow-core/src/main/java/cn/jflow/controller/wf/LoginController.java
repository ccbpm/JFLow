package cn.jflow.controller.wf;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.Port.Emp;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF")
public class LoginController {
	
	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson login(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		try{
			 
			
			Emp emp = new Emp(object.getLoginName());
			if(object.getLoginPass().equals(emp.getPass())){
				//验证登录通过，执行登录的接口.
				BP.WF.Dev2Interface.Port_Login(emp.getNo());
			}else{
				j.setSuccess(false);
				j.setMsg("账户名或者密码错误！");
			}
			
			return j;
		}catch(Exception e){
			j.setSuccess(false);
			j.setMsg("验证{"+object.getLoginName()+"}失败:"+e.getMessage() +"Ext:"+e.toString() );
			return j;
		}
		
	}
	
	@RequestMapping(value = "/Logout", method = RequestMethod.GET)
	public ModelAndView logout(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
		try{
			
			//退出登录.
			BP.WF.Dev2Interface.Port_SigOut();
			mv.setViewName("redirect:" + "/WF/login.htm");
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
