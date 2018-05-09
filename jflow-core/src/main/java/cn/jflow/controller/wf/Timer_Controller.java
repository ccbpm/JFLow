package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.Log;
import BP.WF.DTS.AutoRunOverTimeFlow;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/OverrideFiles")
@ResponseBody
public class Timer_Controller extends HttpHandlerBase{

	/**   
	 * @DL.D
	 * createDate 2018-05-09
	 * 定时任务  
	 * 按约定处理逾期流程
	 */ 
	@RequestMapping("/Timer")
    public  ModelAndView ProcessRequest(HttpServletRequest request)
    {
		AutoRunOverTimeFlow  overTime = new AutoRunOverTimeFlow();
		String info = null ;
		try {
			 info = overTime.Do().toString();
		} catch (Exception e) {
			e.printStackTrace();
			info += e.getMessage();
			Log.DebugWriteInfo(info);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("info", info);
        mav.setViewName("OverrideFiles/Timer");
		return mav;
    }
    
    @Override
    public Class<AutoRunOverTimeFlow> getCtrlType() {
    	
    	return AutoRunOverTimeFlow.class;
    }
}
