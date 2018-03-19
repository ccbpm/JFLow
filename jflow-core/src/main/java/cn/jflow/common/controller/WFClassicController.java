package cn.jflow.common.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Tools.StringHelper;
import BP.WF.SMS;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;


@Controller
@RequestMapping("/WF/App/Classic")
public class WFClassicController extends BaseController {
	HttpServletResponse response;
	HttpServletRequest request;
	
    public String getReceiver(){
  	  String str=getRequest().getParameter("RE");
  	  if(str==null){
  		  str="";
  		  
  	  }
  	  return str;
    }   
    public String getMyPK(){
    	  String str=getRequest().getParameter("MyPK");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
    
    public String getRecEmps(){
    	  String str=getRequest().getParameter("rec");
    	  if(str==null){
    		  str="";
    		  
    	  }
    	  return str;
      }
    public String getTitle(){
  	  String str=getRequest().getParameter("Title");
  	  if(str==null){
  		  str="";
  		  
  	  }
  	  return str;
    }
    public String getDoc(){
  	  String str=getRequest().getParameter("Doc");
  	  if(str==null){
  		  str="";
  		  
  	  }
  	  return str;
    }
    @RequestMapping(value = "/button.do", method = RequestMethod.POST)
	protected void execute(HttpServletRequest request,HttpServletResponse response) {		
		String msg = "";
		PrintWriter out = null;
		try {
			if ("Submit".equals(getDoType())) {// 提交新建信息发送
				msg = this.Submit(this.getRecEmps());
			}else if("Init".equals(getDoType())){
				msg = this.Init();
			}else {
				msg = "err@没有判断的标记:" + getDoType();
			}
			out = response.getWriter();
			out.write(msg);
		} catch (Exception ex) {
			msg = "err@" + ex.getMessage();
		}
		
    }
    
	public  String Submit(String recEmps) {
		SMS sms = null;
	    String[] empArr = recEmps.split(",", -1);
	    for (String emp : empArr) {
	        if (StringHelper.isNullOrEmpty(emp))
	            continue;	
	        sms = new SMS();
	        sms.setMyPK(DBAccess.GenerGUID());
	        sms.setRDT(DataType.getCurrentDataTime());  
	        sms.setSender(WebUser.getNo());
	        sms.setSendToEmpNo(emp);
	        sms.setTitle(this.getTitle());
	        sms.setDocOfEmail(this.getDoc());
	        sms.Insert();
	        }
		   return null;
	}
	
	public String Init(){
		DataTable dt;
		 if (StringHelper.isNullOrEmpty(getFK_Flow())){
			  dt=  DB_GenerRuning();
			 return BP.Tools.Json.ToJson(dt);
		 }
	    dt=BP.WF.Dev2Interface.DB_GenerRuning(WebUser.getNo(),getFK_Flow());
	    return BP.Tools.Json.ToJson(dt);
		
		
	}
	public static DataTable DB_GenerRuning() {
		DataTable dt = BP.WF.Dev2Interface.DB_GenerRuning(BP.Web.WebUser.getNo(), null);
	
		return dt;
	}
	

}
