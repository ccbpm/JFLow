package cn.jflow.controller.des;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.BlockModel;
import BP.WF.Node;


@Controller
@RequestMapping("/block")
public class BlockModelController {
	
	
     /**
      * @Description: 流程节点属性-发送阻塞模式页面保存操作
      * @Title: TurnToDeal_btn_Save
      * @author peixiaofeng
      * @date 2016年5月11日
      */
	@RequestMapping(value = "/block_btn_Save", method = RequestMethod.GET)
	public ModelAndView TurnToDeal_btn_Save(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String FK_Node = request.getParameter("FK_Node");
		String blockContent = request.getParameter("blockContent");
		String TB_Alert = request.getParameter("TB_Alert");
		String blockType = request.getParameter("blockType");
		Node nd = new Node(FK_Node);
		nd.setBlockAlert(TB_Alert);
		//遍历页面radiobutton
		if("None".equals(blockType)){
			nd.setBlockModel(BlockModel.None);
			nd.setBlockExp("");
		}
		else if("CurrNodeAll".equals(blockType)){
			nd.setBlockModel(BlockModel.CurrNodeAll);
			nd.setBlockExp("");
		}
		else if("SpecSubFlow".equals(blockType)){
			nd.setBlockModel(BlockModel.SpecSubFlow);
			nd.setBlockExp(blockContent);
		}
		else if("BySQL".equals(blockType)){
			nd.setBlockModel(BlockModel.BySQL);
			nd.setBlockExp(blockContent);
		}
		else if("ByExp".equals(blockType)){
			nd.setBlockModel(BlockModel.ByExp);
			nd.setBlockExp(blockContent);
		}
		nd.Update();
		mv.setViewName("redirect:" + "/WF/Admin/AttrNode/BlockModel.jsp?FK_Node=" + FK_Node); 
		return mv;
	}

}
