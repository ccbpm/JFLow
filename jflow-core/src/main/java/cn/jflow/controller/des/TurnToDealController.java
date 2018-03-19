package cn.jflow.controller.des;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.Node;
import BP.WF.TurnToDeal;


@Controller
@RequestMapping("/turnToDeal")
public class TurnToDealController {
	
	
     /**
      * @Description: 流程节点属性-发送后转向页面保存操作
      * @Title: TurnToDeal_btn_Save
      * @author peixiaofeng
      * @date 2016年5月11日
      */
	@RequestMapping(value = "/TurnToDeal_btn_Save", method = RequestMethod.GET)
	public ModelAndView TurnToDeal_btn_Save(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String FK_Node = request.getParameter("FK_Node");
		String TurnToDealDoc = request.getParameter("TurnToDealDoc");
//		try {
//			TurnToDealDoc=new String( TurnToDealDoc.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		String TurnToDealType = request.getParameter("TurnToDealType");
		Node nd = new Node(FK_Node);
		//遍历页面radiobutton
		if("CCFlowMsg".equals(TurnToDealType)){
			nd.setHisTurnToDeal(TurnToDeal.CCFlowMsg);
		}
		else if("SpecMsg".equals(TurnToDealType)){
			nd.setHisTurnToDeal(TurnToDeal.SpecMsg);
			nd.setTurnToDealDoc(TurnToDealDoc);
		}
		else if("SpecUrl".equals(TurnToDealType)){
			nd.setHisTurnToDeal(TurnToDeal.SpecUrl);
			nd.setTurnToDealDoc(TurnToDealDoc);
		}
		nd.Update();
		mv.setViewName("redirect:" + "/WF/Admin/AttrNode/TurnToDeal.jsp?FK_Node=" + FK_Node); 
		return mv;
	}

}
