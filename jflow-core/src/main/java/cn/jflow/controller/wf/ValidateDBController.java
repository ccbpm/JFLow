package cn.jflow.controller.wf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.AjaxJson;
import BP.DA.DBAccess;

@Controller
@RequestMapping("/WF")
public class ValidateDBController{

	@RequestMapping(value = "/validateDB")
	@ResponseBody
	public AjaxJson validateDB() throws Exception {
		AjaxJson j = new AjaxJson();

		//  #region 检查一下数据库是否链接成功.
          try
          {
              switch (BP.Sys.SystemConfig.getAppCenterDBType())
              {
                  case MSSQL:
                      BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 ");
                      break;
                  case Oracle:
                  case MySQL:
                      BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 FROM DUAL ");
                      break;
                  case Informix:
                      BP.DA.DBAccess.RunSQLReturnString("SELECT 1+2 FROM DUAL ");
                      break;
                  default:
                      break;
              }
          }
          catch (Exception ex)
          {
        	j.setSuccess(false);
  			j.setMsg("DBFalse");
  			return j;
          }

		try
		{
				    
		 if(!DBAccess.IsExitsObject("WF_Flow")){
			j.setSuccess(false);
			j.setMsg("JflowFalse");
		}else{
			j.setSuccess(true);
		}
		 return j;
		}catch (Exception ex){
		   j.setSuccess(false);
		   j.setMsg("未安装jflow数据库");
		   return j;
		
	  }
			
	}
}
