package cn.jflow.model.wf.rpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.Glo;
import BP.WF.ReturnWork;
import BP.WF.ReturnWorkAttr;
import BP.WF.ReturnWorks;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;

public class DealSubThreadReturnToHLModel extends BaseModel {

	public DealSubThreadReturnToHLModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}
	public final long getFID() {
		return Long.parseLong( get_request().getParameter("FID"));
	}
	public final long getWorkID() {
		return Long.parseLong( get_request().getParameter("WorkID"));
	}
	public final int getFK_Node() {
		return Integer.parseInt( get_request().getParameter("FK_Node"));
	}
	public final String getFK_Flow() {
		return  get_request().getParameter("FK_Flow");
	}
     
	public String webUser;
	
	public StringBuffer msgInfo;
	
	public void init(){
		msgInfo=new StringBuffer();
		webUser=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName());
		
		ReturnWorks rws = new ReturnWorks();
        rws.Retrieve(ReturnWorkAttr.ReturnToNode, this.getFK_Node(),
           ReturnWorkAttr.WorkID, this.getWorkID(),
           ReturnWorkAttr.RDT);
        if (rws.size() != 0){
            for(int i=0;i<rws.size();i++){
            	ReturnWork rw=(ReturnWork) rws.get(i);
            	msgInfo.append("<fieldset width='100%' ><legend>&nbsp; 来自节点:" + rw.getReturnNodeName() + " 退回人:" + rw.getReturnerName() + "  " + rw.getRDT() + "&nbsp;<a href='./../../DataUser/ReturnLog/" + this.getFK_Flow() + "/" + rw.getMyPK() + ".htm' target=_blank>工作日志</a></legend>");
            	msgInfo.append(rw.getBeiZhuHtml());
            	msgInfo.append("</fieldset>");
            }
        }
        
	}
	
	
	
	
	
	
	public String getWebUser() {
		return webUser;
	}
	public void setWebUser(String webUser) {
		this.webUser = webUser;
	}
	public String getMsgInfo() {
		if(!"".equals(msgInfo)){
			return msgInfo.toString();
		}
		return "";
	}
	public void setMsgInfo(StringBuffer msgInfo) {
		this.msgInfo = msgInfo;
	}

	
	
	
}
