package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.Flow;
import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/WF")
public class StartGuideController {

	@RequestMapping(value = "/StartGuide", method = RequestMethod.POST)
	public ModelAndView startGuide(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
		try {
			  mv.addObject("FK_Flow", object.getFK_Flow());
			  mv.addObject("FK_Node", Integer.parseInt(object.getFK_Flow())+"01");
		 	  mv.addObject("WorkID", 0);
              mv.addObject("IsCheckGuide", 1);
			  Flow fl = new Flow(object.getFK_Flow());
			  //必要的系统约定参数.
	          switch (fl.getStartGuideWay()){
	          	case SubFlowGuide:
//	          	case BySystemUrlOne:
//	          		mv.addObject("DoFunc", "SetParentFlow");
//	   			 	mv.addObject("WorkIDs", object.getToEmp());
//	   			 	mv.addObject("CFlowNo", fl.getStartGuidePara3());
//	          		break;
	          	case SubFlowGuideEntity:
	            case BySystemUrlOneEntity:	
	            	mv.addObject("DoFunc", fl.getStartGuideWay());
	   			 	mv.addObject("Nos", object.getToEmp());
	   			 	mv.addObject("StartGuidePara3", fl.getStartGuidePara3());
	   			 	break;
	            default:
	                break;
	          }
	          mv.setViewName("redirect:" + "/WF/MyFlow.htm");
		} catch (Exception e) {
			  mv.addObject("FK_Flow", object.getFK_Flow());
			  mv.addObject("errMsg", "启动流程出错："+e.getMessage());
	          mv.setViewName("redirect:" + "/WF/StartGuide.jsp");
			  return mv;
		}
		return mv;
	}
}