package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/WF")
public class ReLoadController {

	@RequestMapping(value = "/TodoList", method = RequestMethod.POST)
	public ModelAndView todoList(HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView mv = new ModelAndView("/WF/TodolistInfo");
		
		return mv;
	}
	
	@RequestMapping(value = "/RuningS", method = RequestMethod.POST)
	public ModelAndView runing(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView mv = new ModelAndView("/WF/RuningInfo");
		mv.addObject("FK_Flow", object.getFK_Flow());
 		mv.addObject("GroupBy", object.getGroupBy());
 		mv.addObject("PageID", object.getPageID());
 		mv.addObject("PageSmall", object.getPageSmall());
		
		return mv;
	}
	
	@RequestMapping(value = "/CCS", method = RequestMethod.POST)
	public ModelAndView cc(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView mv = new ModelAndView("/WF/CCInfo");
		mv.addObject("Sta", object.getSta());
		mv.addObject("FK_Flow", object.getFK_Flow());
 		mv.addObject("GroupBy", object.getGroupBy());
		
		return mv;
	}
	
	@RequestMapping(value = "/Complete", method = RequestMethod.POST)
	public ModelAndView complete(HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView mv = new ModelAndView("/WF/CompleteInfo");
		
		return mv;
	}
	
	@RequestMapping(value = "/Left", method = RequestMethod.POST)
	public ModelAndView left(HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView mv = new ModelAndView("/WF/LeftInfo");
		
		return mv;
	}
	
	@RequestMapping(value = "/Index", method = RequestMethod.POST)
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) {
	
		ModelAndView mv = new ModelAndView("/WF/IndexInfo");
		
		return mv;
	}
}
