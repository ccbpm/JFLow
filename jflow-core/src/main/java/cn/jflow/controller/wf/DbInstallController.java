package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.DA.DBAccess;
import BP.WF.Glo;


@Controller
@RequestMapping("/WF")
public class DbInstallController {

	/* @RequestMapping(value = "/dbinstall")
     public void dbInstall() {
		Glo.DoInstallDataBase("CH", false);
		
	 }
	 */
	 
	 @RequestMapping(value = "/dbinstall", method = RequestMethod.POST)
		@ResponseBody
		public AjaxJson dbInstall(String demo, HttpServletRequest request,
				HttpServletResponse response) {
			AjaxJson j = new AjaxJson();
			long t1 = System.currentTimeMillis();
			try{
				if("RB_DemoOn".equals(demo)){
					Glo.DoInstallDataBase("CH", true, false);
				}else{
					Glo.DoInstallDataBase("CH", false, false);
				}
				j.setSuccess(true);
				System.out.println("安装成功用时"+(System.currentTimeMillis()-t1));
				return j;
			}catch(Exception e){
				j.setSuccess(false);
				System.out.println("安装失败用时"+(System.currentTimeMillis()-t1));
				return j;
			}
	 }
	 
	 /**
	  * 安装前检查数据库用户权限
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping(value = "checkDb",method = RequestMethod.POST)
	 @ResponseBody
	 public AjaxJson checkDb(HttpServletRequest request,
				HttpServletResponse response)
	 {
		 AjaxJson ajaxJson = new AjaxJson();
		 if(DBAccess.isCanInstall())
		 {
			 ajaxJson.setSuccess(true);
		 }else
		 {
			 ajaxJson.setSuccess(false);
		 }
		 return ajaxJson;
	 }
	
}
