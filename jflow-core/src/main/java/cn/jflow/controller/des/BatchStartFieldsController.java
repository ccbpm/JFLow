package cn.jflow.controller.des;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.BatchRole;
import BP.WF.Node;
@Controller
@RequestMapping("/batchStartFields")
public class BatchStartFieldsController {
	
	/**
	 * @Description: 批量发起规则设置保存方法
	 * @Title: BatchStartFields_Btn_Save
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年5月12日
	 */
	@RequestMapping(value = "/BatchStartFields_Btn_Save", method = RequestMethod.GET)
	public ModelAndView BatchStartFields_Btn_Save(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String FK_Flow = request.getParameter("FK_Flow");
		String FK_Node = request.getParameter("FK_Node");
		String tBcount = request.getParameter("BatchListCount");
		String BRole = request.getParameter("BatchRole");
		String BatchParas = request.getParameter("BatchParas");
		
		Node nd = new Node(FK_Node);
		nd.setBatchListCount(Integer.parseInt(tBcount));
		if("0".equals(BRole)){
			nd.setHisBatchRole(BatchRole.None);
		}
		else if("1".equals(BRole)){
			nd.setHisBatchRole(BatchRole.Ordinary);
		}
		else {
			nd.setHisBatchRole(BatchRole.Group);
		}
		nd.setBatchParas(BatchParas);
		nd.Update();
		mv.setViewName("redirect:" + "/WF/Admin/AttrNode/BatchStartFields.jsp?s=d34&FK_Flow="+FK_Flow+"&FK_Node=" + FK_Node); 

		return mv;
		
	}

}
