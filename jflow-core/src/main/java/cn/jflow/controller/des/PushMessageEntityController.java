	package cn.jflow.controller.des;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DBAccess;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.PushMsg;
import BP.Web.WebUser;
@Controller
@RequestMapping("/pushMessageEntity")
public class PushMessageEntityController {
	
	/**
	 * @Description: 节点消息保存方法
	 * @Title: BatchStartFields_Btn_Save
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年5月12日
	 */
	@RequestMapping(value = "/pushMessageEntity_Btn_Save", method = RequestMethod.POST)
	@ResponseBody
	public String pushMessageEntity_Btn_Save(HttpServletRequest request,HttpServletResponse response) {
		String FK_Flow = request.getParameter("FK_Flow");
		String FK_Node = request.getParameter("FK_Node");
		
		String myPk=request.getParameter("myPK");
		String FK_Event=request.getParameter("FK_Event");
		String RB_SMS=request.getParameter("RB_SMS");
		String SMS_Fields=request.getParameter("SMS_Fields");
		String smsStr=request.getParameter("smsStr");
		
		String MailPushWay=request.getParameter("MailPushWay");
		String MailTitle_Real=request.getParameter("MailTitle_Real");
		String MailDoc_Real=request.getParameter("MailDoc_Real");
		String DDL_Email=request.getParameter("DDL_Email");
		
		String mailArrayStr=request.getParameter("mailArrayStr");
		String smsArrayStr=request.getParameter("smsArrayStr");
		
		PushMsg msg = new PushMsg();
		msg.setMyPK(myPk); ;
		msg.RetrieveFromDBSources();

		msg.setFK_Event(FK_Event);
		msg.setFK_Node(Integer.parseInt(FK_Node));
		
		msg.setSMSPushWay(Integer.parseInt(RB_SMS));


		//短信手机字段.
		msg.setSMSField(SMS_Fields); 
		//替换变量
		smsStr = smsStr.replace("@WebUser.Name", WebUser.getName());
		smsStr = smsStr.replace("WebUser.No", WebUser.getNo());

		msg.setSMSDoc_Real(smsStr);
	   
		Node nd = new Node(FK_Node);
		Nodes nds = new Nodes(nd.getFK_Flow());

		String nodesOfSMS = "";
		String nodesOfEmail = "";
		for(int i=0;i<nds.size();i++){	
			Node mynd=(Node) nds.get(i);
			
			String[] key =smsArrayStr.split(",") ;
			for(int j=0;j<key.length;j++){
				if (key[j].contains("CB_SMS_" + mynd.getNodeID()) && nodesOfSMS.contains(mynd.getNodeID()+"")==false) {
					nodesOfSMS += mynd.getNodeID()+",";
				}
			}
			
			String[] mkey =mailArrayStr.split(",") ;
			for(int n=0;n<mkey.length;n++){
				if (mkey[n].contains("CB_Email_" + mynd.getNodeID()) && nodesOfEmail.contains(mynd.getNodeID()+"")==false) {
					nodesOfEmail += mynd.getNodeID()+",";
				}
			}
		}

		//节点.
		msg.setMailNodes(nodesOfEmail);
		msg.setSMSNodes(nodesOfSMS);

		//邮件.
		msg.setPushWay(Integer.parseInt(MailPushWay));
        msg.setMailTitle_Real(MailTitle_Real);
        msg.setMailDoc_Real(MailDoc_Real);
        msg.setMailAddress(DDL_Email);

		//保存.
		if (StringHelper.isNullOrEmpty(msg.getMyPK()) == true) {
			msg.setMyPK(DBAccess.GenerGUID());
			msg.Insert();
		}else {
			msg.Update();
		}

		/*mv.setViewName("redirect:" + "/WF/Admin/AttrNode/PushMessage.jsp?FK_Flow="+FK_Flow+"&FK_Node=" + FK_Node+"&FK_Event=" +FK_Event+ "&MyPK" + myPk+"&tk="
				+ new java.util.Random().nextDouble()); */
		return "success";
	}

	/**
	 * @Description: 消息页面返回
	 * @Title: redirect_to_index
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年5月16日
	 */
	@RequestMapping(value = "/redirect_to_index", method = RequestMethod.GET)
	public ModelAndView redirect_to_index(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String FK_Flow = request.getParameter("FK_Flow");
		String FK_Node = request.getParameter("FK_Node");
		mv.setViewName("redirect:" + "/WF/Admin/AttrNode/PushMessage.jsp?FK_Flow="+FK_Flow+"&FK_Node=" + FK_Node+"&tk="
				+ new java.util.Random().nextDouble()); 
		return mv;
	}
}
