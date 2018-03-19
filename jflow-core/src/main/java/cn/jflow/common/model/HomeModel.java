package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeModel extends BaseModel {

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	
	
	public HomeModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void init(){
//		  var fl = new Flow(FK_Flow);
//	        Title = "报表设计：" + fl.Name;
//
//	        // 清除缓存数据.
//	        Cash.EnsData_Cash.Clear();
	}
	
}
