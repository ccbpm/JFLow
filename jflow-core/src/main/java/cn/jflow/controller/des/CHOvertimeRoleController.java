package cn.jflow.controller.des;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.BlockModel;
import BP.WF.Node;
import BP.WF.Template.OutTimeDeal;

@Controller
@RequestMapping("/CHOvertime")
public class CHOvertimeRoleController {

    /**
     * 流程节点属性-超时处理规则
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/CHOvertime_btn_Save", method = RequestMethod.GET)
	public ModelAndView CHOvertime_btn_Save(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String FK_Node = request.getParameter("FK_Node");
		String RoleContent = request.getParameter("RoleContent");
		System.out.println("RoleContent1=========="+RoleContent);
		String TB_IsEval = request.getParameter("TB_IsEval");//是否质量考核点
		String RolekType = request.getParameter("RolekType");
		Node nd = new Node(FK_Node);
		//遍历页面radiobutton
		if("None".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.None);
			nd.setDoOutTime("");
		}
		else if("delete".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.DeleteFlow);
			nd.setDoOutTime("");
		}
		else if("AppointNode".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.AutoJumpToSpecNode);
			nd.setDoOutTime(RoleContent);
		}
		else if("down".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.AutoTurntoNextStep);
			nd.setDoOutTime(RoleContent);
		}
		else if("Transfer".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.AutoShiftToSpecUser);
			nd.setDoOutTime(RoleContent);
		}
		else if("EMP".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.SendMsgToSpecUser);
			nd.setDoOutTime(RoleContent);
		}
		else if("BySQL".equals(RolekType)){
			nd.setHisOutTimeDeal(OutTimeDeal.RunSQL);
			nd.setDoOutTime(RoleContent);
		}
		nd.Update();
		mv.setViewName("redirect:" + "/WF/Admin/AttrNode/CHOvertimeRole.jsp?FK_Node=" + FK_Node); 
		return mv;
	}
}
