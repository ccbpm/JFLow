package cn.jflow.controller.des;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Random;

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
import BP.Sys.EventDoType;
import BP.Sys.FrmEvent;
import BP.Tools.StringHelper;


@Controller
@RequestMapping("/DES")
public class ActionController{
	
	@RequestMapping(value = "/ActionEvent", method = RequestMethod.POST)
	public ModelAndView actionEvent(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			FrmEvent fe = new FrmEvent();
	        fe.setMyPK(this.getFK_MapData(map)+"_"+map.get("Event"));
	        fe.RetrieveFromDBSources();
	        
	        BaseModel.Copy(request, fe, null, fe.getEnMap(), controls);
	        fe.setMyPK(this.getFK_MapData(map)+"_"+map.get("Event"));
	        fe.setDoDoc(request.getParameter("TB_Doc")==null?"":request.getParameter("TB_Doc"));
	        fe.setFK_Event(map.get("Event"));
	        fe.setFK_MapData(this.getFK_MapData(map));
	        String eventDoType = request.getParameter("DDL_EventDoType")==null?"0":request.getParameter("DDL_EventDoType");
	        fe.setHisDoType(EventDoType.forValue(Integer.valueOf(eventDoType)));
	        fe.setMsgOKString(request.getParameter("TB_MsgOK")==null?"":request.getParameter("TB_MsgOK"));
	        fe.setMsgErrorString(request.getParameter("TB_MsgErr")==null?"":request.getParameter("TB_MsgErr"));
	        
	        fe.Save();
			
            mv.setViewName("redirect:" + "/WF/Admin/AttrNode/ActionEvent.jsp?NodeID=" + map.get("NodeID") + "&MyPK=" + fe.getMyPK() + "&Event=" + map.get("Event") + "&tk=" + new Random().nextDouble() + "&FK_MapData=" + this.getFK_MapData(map));
            
		}catch(Exception e){
			e.printStackTrace();
		}
		return mv;
	}
	
	private String getFK_MapData(HashMap<String, String> map) {
		String fk_mapdata = map.get("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata)) {
			fk_mapdata = "ND" + map.get("NodeID");
		}
		return fk_mapdata;
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