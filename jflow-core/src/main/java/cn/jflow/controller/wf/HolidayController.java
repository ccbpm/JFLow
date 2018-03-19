package cn.jflow.controller.wf;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/WF/Holiday")
public class HolidayController {
	
	@ResponseBody
	@RequestMapping(value="Btn_Save_Click",method=RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response)
	{
		String str = "";
		HashMap map = (HashMap) request.getParameterMap();
		Iterator it=map.entrySet().iterator();    
		while(it.hasNext()){
			 Map.Entry entry = (Map.Entry)it.next();           
			 str+=entry.getKey().toString()+",";           
		}
		str=str.length()>0?str.substring(0, str.length()-1):str;
		// 保存.
		BP.Sys.GloVar var = new BP.Sys.GloVar();
		var.setNo("Holiday");
		var.RetrieveFromDBSources();
		var.setVal(str);
		//设置一下空值，让其重新从数据库取.
		BP.Sys.GloVar.setHolidays(str);
		var.Save();
	}
}
