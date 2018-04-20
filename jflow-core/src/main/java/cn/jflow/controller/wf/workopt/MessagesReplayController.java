package cn.jflow.controller.wf.workopt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.SMS;
import BP.WF.SMSAttr;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF/WorkOpt")
public class MessagesReplayController {

	@RequestMapping(value="btn_Save_Click" ,method=RequestMethod.POST)
	public void btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response)
	{
	
	    String receiver = request.getParameter("RE");
	    String MyPK = request.getParameter("MyPK");
	    String recEmps = request.getParameter("rec");
	    String title = request.getParameter("title");
	    String con = request.getParameter("con");
	    String url = Glo.getCCFlowAppPath()+"WF/App/Classic/messages.jsp";
		SMS sms = null;
		try {
			/*
		    if (StringHelper.isNullOrEmpty(title) || StringHelper.isNullOrEmpty(con))
		    {
		        PubClass.Alert("标题和内容不可以为空！",ContextHolderUtils.getResponse());
		        return ;
		    }
			 */
		    if (!StringHelper.isNullOrEmpty(receiver) && !StringHelper.isNullOrEmpty(MyPK))
		    {
		        sms = new SMS();
		        sms.RetrieveByAttr(SMSAttr.MyPK, MyPK);
		
		        sms.setMyPK(DBAccess.GenerGUID());
		        sms.setRDT(DataType.getCurrentDataTime());  
		        sms.setSendToEmpNo(sms.getSender());//先赋发给谁，然后再赋发送人
		        sms.setSender(WebUser.getNo());
		       
		              
		        sms.setTitle(title);
		        sms.setDocOfEmail(con);
		        sms.Insert();
		
		    }
		    else
		    {
		      //String emps = (this.pub1.FindControl("Hid_FQR") as HiddenField).Value;
			    /*
		    	UiFatory uf = new UiFatory();
			    TextBox tb = (TextBox) uf.GetUIByID("rec");
			    if(null == tb)
			    {
			    	PubClass.Alert("接收人不可以为空！",ContextHolderUtils.getResponse());
			        return ;
			    }
			    String emps = tb.getValue();
			    if (StringHelper.isNullOrEmpty(emps))
			    {
			        PubClass.Alert("接受人不可以为空！",ContextHolderUtils.getResponse());
			        return;
			    }
			    */
			    String[] empArr = recEmps.split(",", -1);
			  
			    for (String emp : empArr)
			    {
			        if (StringHelper.isNullOrEmpty(emp))
			            continue;
			
			        sms = new SMS();
			
			        sms.setMyPK(DBAccess.GenerGUID());
			        sms.setRDT(DataType.getCurrentDataTime());  
			        sms.setSender(WebUser.getNo());
			        sms.setSendToEmpNo(emp);
			        sms.setTitle(title);
			        sms.setDocOfEmail(con);
			        //sms.MsgType = SMSMsgType.ToDo;
			        sms.Insert();
			       // BP.Sys.PubClass.WinClose();
			        }
		    }
	
		    //发送转向.
			response.sendRedirect(url);
		    //BP.Sys.PubClass.WinClose();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	  }
}
