package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DataType;
import BP.Tools.StringHelper;
import BP.WF.HungUpWay;
import BP.WF.WFState;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.HungUp;

@Controller
@RequestMapping("/WF/WorkOpt")
@Scope("request")
public class HungUpController extends BaseController {

	@RequestMapping(value = "/HungUp", method = RequestMethod.GET)
	public ModelAndView toHungUp(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		
		HungUp hu = new HungUp();
		hu.setMyPK(this.getWorkID() + "_" + this.getFK_Node());

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(getWorkID());
		if (gwf.RetrieveFromDBSources() == 0)
		{
			try {
				printAlert(response, "当前是开始节点，或者工作不存在!");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		HungUpWay huw = hu.getHungUpWay();
		String isForever = "true";
		if(huw == HungUpWay.SpecDataRel){
			isForever = "false";
		}
		String DTOfUnHungUpPlan = hu.getDTOfUnHungUpPlan();
		if(StringHelper.isNullOrEmpty(DTOfUnHungUpPlan)){
			hu.setDTOfUnHungUpPlan(DataType.dateToStr(DataType.AddDays(new Date(), 7),"yyyy-MM-dd HH:mm"));
		}
		String isHungUp = "挂起"; 
		if(gwf.getWFState() == WFState.HungUp){
			isHungUp = "取消挂起";
		}
		mv.addObject("FK_Node",this.getFK_Node());
		mv.addObject("FID",this.getFID());
		mv.addObject("WorkID",this.getWorkID());
		mv.addObject("FK_Flow",this.getFK_Flow());
		mv.addObject("title", gwf.getTitle());
		mv.addObject("isCheckForever", isForever);
		mv.addObject("DTOfUnHungUpPlan", hu.getDTOfUnHungUpPlan());
		mv.addObject("isHungUp", isHungUp);
		mv.setViewName("redirect:" + "/WF/WorkOpt/HungUp.jsp");

		return mv;
	}
	
	@RequestMapping(value = "/doHungUp", method = RequestMethod.POST)
	public void doHungUp(HttpServletRequest request,
			HttpServletResponse response){
		String RB_HungWay = request.getParameter("RB_HungWay");
		String TB_RelData = request.getParameter("TB_RelData");
		String TB_Note = request.getParameter("TB_Note");
		try {
			HungUpWay way = HungUpWay.SpecDataRel;
			if("RB_HungWay0".equals(RB_HungWay)){
				way = HungUpWay.Forever;
			}
			GenerWorkFlow gwf = new GenerWorkFlow(getWorkID());
			if (gwf.getWFState() == WFState.HungUp){
				BP.WF.Dev2Interface.Node_UnHungUpWork(this.getFK_Flow(),
						getWorkID(), TB_Note);
			}else{
				BP.WF.Dev2Interface.Node_HungUpWork(this.getFK_Flow(), getWorkID(),
						way.getValue(), TB_RelData, TB_Note);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@RequestMapping(value = "/ajaxHungUp", method = RequestMethod.POST)
	@ResponseBody
	public String ajaxHungUp(HttpServletRequest request,
			HttpServletResponse response){
		String RB_HungWay = request.getParameter("RB_HungWay");
		String TB_RelData = request.getParameter("TB_RelData");
		String TB_Note = request.getParameter("TB_Note");
		try {
			HungUpWay way = HungUpWay.SpecDataRel;
			if("RB_HungWay0".equals(RB_HungWay)){
				way = HungUpWay.Forever;
			}
			GenerWorkFlow gwf = new GenerWorkFlow(getWorkID());
			if (gwf.getWFState() == WFState.HungUp){
				BP.WF.Dev2Interface.Node_UnHungUpWork(this.getFK_Flow(),
						getWorkID(), TB_Note);
			}else{
				BP.WF.Dev2Interface.Node_HungUpWork(this.getFK_Flow(), getWorkID(),
						way.getValue(), TB_RelData, TB_Note);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "err";
		}
		return "success";
	}
	
}
