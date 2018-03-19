package cn.jflow.controller.wf.workopt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Work;
import BP.WF.WorkNode;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.TurnTo;
import BP.WF.Template.TurnTos;
import BP.Web.WebUser;
import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/WF/WorkOpt")
public class ToNodesController {

	@RequestMapping(value = "/ToNodesX", method = RequestMethod.POST)
	public ModelAndView toNodes(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		// 执行发送.
        String msg = "";
		ModelAndView mv = new ModelAndView();
		try {
			String toNodes = object.getToEmp();
			
			Node nd = new Node(object.getFK_Node());
			Work wk = nd.getHisWork();
			wk.setOID(object.getWorkID());
			wk.Retrieve();
			String toNodeStr = Integer.parseInt(object.getFK_Flow())+"01";
			
			//如果为开始节点
			if(toNodes.equals(toNodeStr)){
				//把参数更新到数据库里面.
				 GenerWorkFlow gwf = new GenerWorkFlow();
				 gwf.setWorkID(object.getWorkID());
				 gwf.RetrieveFromDBSources();
				 gwf.setParas_ToNodes(toNodes);
				 gwf.Save();
				 
				 WorkNode firstwn = new WorkNode(wk, nd);
                 Node toNode = new Node(toNodeStr);
                 msg = firstwn.NodeSend(toNode, gwf.getStarter()).ToMsgOfHtml();
			}else{
				msg = Dev2Interface.WorkOpt_SendToNodes(object.getFK_Flow(),
						object.getFK_Node(), object.getWorkID(),
						object.getFID(), toNodes).ToMsgOfHtml();
			}
			
			//处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常
			Glo.DealBuinessAfterSendWork(object.getFK_Flow(),
					object.getWorkID(), object.getDoFunc(),
					object.getWorkIDs(), object.getCFlowNo(), 0, null);
			
			/*处理转向问题.*/
			 switch (nd.getHisTurnToDeal()){
			 	case SpecUrl:
			 		 String myurl = nd.getTurnToDealDoc();
			 		 if(!myurl.contains("&"))myurl += "?1=1";
			 		 myurl = Glo.DealExp(myurl, wk, null);
			 		 mv.addObject("FromFlow", object.getFK_Flow());
			 		 mv.addObject("FromNode", object.getFK_Node());
			 		 mv.addObject("PWorkID", object.getWorkID());
			 		 mv.addObject("UserNo", WebUser.getNo());
			 		 mv.addObject("SID", WebUser.getSID());
			 		 mv.setViewName("redirect:" + myurl);
			 		 return mv;
			 	case TurnToByCond:
			 		 TurnTos tts = new TurnTos(object.getFK_Flow());
			 		 if(tts.size() == 0)throw new Exception("您没有设置节点完成后的转向条件！");
			 		 for (TurnTo tt : tts.ToJavaList()){
			 			 tt.HisWork = wk;
			 			 if(tt.getIsPassed()){
			 				 String url = tt.getTurnToURL();
			 				 if(!url.contains("&"))url += "?1=1";
			 				 url = Glo.DealExp(url, wk, null);
			 				 mv.addObject("PFlowNo", object.getFK_Flow());
					 		 mv.addObject("FromNode", object.getFK_Node());
					 		 mv.addObject("PWorkID", object.getWorkID());
					 		 mv.addObject("UserNo", WebUser.getNo());
					 		 mv.addObject("SID", WebUser.getSID());
					 		 mv.setViewName("redirect:" + url);
					 		 return mv;
			 			 }
			 		 }
			 		 break;
			 	default:
			 		break;
			 }
			
			request.getSession().setAttribute("info", msg);
			
			mv.addObject("FK_Flow", object.getFK_Flow());
			mv.addObject("FK_Type", "Info");
			mv.addObject("FK_Node", object.getFK_Node());
			mv.addObject("WorkID", object.getWorkID());
			mv.setViewName("redirect:" + "/WF/MyFlowInfo"+ Glo.getFromPageType() +".jsp");
		} catch (Exception e) {
            mv.addObject("WorkID", object.getWorkID());
            mv.addObject("FID", object.getFID());
            mv.addObject("FK_Node", object.getFK_Node());
            mv.addObject("FK_Flow", object.getFK_Flow());
            mv.addObject("WorkIDs", object.getWorkIDs());
            mv.addObject("CFlowNo", object.getCFlowNo());
            mv.addObject("DoFunc", object.getDoFunc());
            mv.addObject("errMsg", "节点选择发送出错："+e.getMessage());
            mv.setViewName("redirect:" + "/WF/WorkOpt/ToNodes.jsp");
            //mv.setViewName("/WF/WorkOpt/Forward");
			return mv;
		}
		return mv;
	}
}