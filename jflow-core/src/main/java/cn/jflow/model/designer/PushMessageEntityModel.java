package cn.jflow.model.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataType;
import BP.En.FieldTypeS;
import BP.Sys.EventListOfNode;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.PushMsg;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;

public class PushMessageEntityModel {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public UiFatory ui = null;
	public String basePath = "";

	
	public PushMessageEntityModel (HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	
	public final String getFK_Event(){
		return request.getParameter("FK_Event");
	}
	public final String getMyPk(){
		return request.getParameter("MyPK");
	}
	public final String getFK_Flow(){
		return request.getParameter("FK_Flow");
	}
	
	/**
	 * 节点ID
	 * @return
	 */
	public final int getFK_Node() {
		try {
			return Integer.parseInt(request.getParameter("FK_Node"));
		} catch (Exception e) {
			return 0;
		}
	}
	//短信设置
	public String smsChecked0;
	public String smsChecked1;
	public String smsChecked2;
	public String smsChecked3;
	public String smsContent;
	//邮件设置
	public String emailChecked0;
	public String emailChecked1;
	public String emailChecked2;
	public String emailChecked3;
	public String emailTitle;
	public String emailContent;
	
	public String smsSb;
	public String mailSb;
	
	/**
	 * 短信、邮件提示
	 */
	public String smsTsText;
	public String mailTsText;
	
	/**
	 * 短信选择列表
	 */
	public List<Map<String,Object>>smsFiledsList=new ArrayList<Map<String,Object>>();
	
	/**
	 * 邮件选择列表
	 */
	public List<Map<String,Object>>mailFiledsList=new ArrayList<Map<String,Object>>();
	
	/**
	 * 
	 * @Description: 节点消息页面初始化
	 * @Title: Page_Load   
	 * @author peixiaofeng
	 * @date 2016年5月12日
	 */
	public void Page_Load() {
		Node nd = new Node();
		nd.setNodeID(this.getFK_Node());
		//MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());
		nd.RetrieveFromDBSources();
	  
		//PushMsgs ens = new PushMsgs(String.valueOf(getFK_Flow()));
		PushMsg msg = new PushMsg();
		msg.setMyPK(this.getMyPk()); 
		msg.setFK_Event(this.getFK_Event());
		msg.setFK_Node(this.getFK_Node()); 

		if (msg.RetrieveFromDBSources() == 0 && (this.getFK_Event().equals(EventListOfNode.SendSuccess) ||this.getFK_Event().equals(EventListOfNode.ReturnAfter))) {
			//如果是发送成功的消息没有被查询到. 
			msg.setPushWay(1);
			msg.setSMSPushWay(0); 
		}

		List<Map<String,Object>> alist=new ArrayList<Map<String,Object>>();
		MapAttrs attrs = new MapAttrs();
		int in = attrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + getFK_Node());
		for(int i=0;i<attrs.size();i++){
			MapAttr item=(MapAttr) attrs.get(i);
			if(!FieldTypeS.Normal.equals(item.getLGType())){
				continue;
			}
			if(DataType.AppString!=item.getMyDataType()){
				continue;
			}
			/*
			该代码不起作用，注释掉
			switch (item.getKeyOfEn()) {
			case GEWorkAttr.Emps:
			case GEWorkAttr.MD5:
			case GEWorkAttr.Rec:
			case GEWorkAttr.RecText:
				break;
			default:
				break;
			}
			*/
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("KeyOfEn",item.getKeyOfEn());
			map.put("NAME",item.getName());
			if(item.getKeyOfEn().equals(msg.getSMSField())){
				map.put("smsSelected","selected='selected'");
			}
			if(item.getKeyOfEn().equals(msg.getMailAddress())){
				map.put("emailSelected","selected='selected'");
			}
			alist.add(map);
			//设置邮件单选框
			if(msg.getPushWay()==0){
				this.setEmailChecked0("checked='checked'");
			}
			if(msg.getPushWay()==1){
				this.setEmailChecked1("checked='checked'");
			}
			if(msg.getPushWay()==2){
				this.setEmailChecked2("checked='checked'");
			}
			if(msg.getPushWay()==3){
				this.setEmailChecked3("checked='checked'");
			}
			this.setEmailTitle(msg.getMailTitle());
			this.setEmailContent(msg.getMailDoc());
			
			//设置短信单选框选择
			if(msg.getSMSPushWay()==0){
				this.setSmsChecked0("checked='checked'");
			}
			if(msg.getSMSPushWay()==1){
				this.setSmsChecked1("checked='checked'");
			}
			if(msg.getSMSPushWay()==2){
				this.setSmsChecked2("checked='checked'");
			}
			if(msg.getSMSPushWay()==3){
				this.setSmsChecked3("checked='checked'");
			}
			this.setSmsContent(msg.getSMSDoc());
			
		}
		this.setMailFiledsList(alist);
		this.setSmsFiledsList(alist);
		//节点到达时
		if(this.getFK_Event().equals(EventListOfNode.WorkArrive)){
			this.setSmsTsText("发送给当前节点的所有处理人");
			this.setMailTsText("发送给当前节点的所有处理人");
		}//节点发送成功时
		else if(this.getFK_Event().equals(EventListOfNode.SendSuccess)){
			this.setSmsTsText("发送给下一个节点的所有接受人");
			this.setMailTsText("发送给下一个节点的所有接受人");
		}//节点退回后
		else if(this.getFK_Event().equals(EventListOfNode.ReturnAfter)){
			this.setSmsTsText("发送给被退回的节点处理人");
			this.setMailTsText("发送给被退回的节点处理人	");
		}//工作撤销后
		else if(this.getFK_Event().equals(EventListOfNode.UndoneAfter)){
			this.setSmsTsText("撤销工作后通知的节点接受人");
			this.setMailTsText("撤销工作后通知的节点接受人	");
		}//流程结束后
		else if(this.getFK_Event().equals(EventListOfNode.FlowOverAfter)){
			this.setSmsTsText("流程结束后通知的节点接受人");
			this.setMailTsText("流程结束后通知的节点接受人");
		}//流程删除后
		else if(this.getFK_Event().equals(EventListOfNode.AfterFlowDel)){
			this.setSmsTsText("流程删除后通知的节点接受人");
			this.setMailTsText("流程删除后通知的节点接受人");
		}
		
		
		Nodes nds = new Nodes(nd.getFK_Flow());
		StringBuffer sb1=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		for(int i=0;i<nds.size();i++){	
			Node mynd=(Node) nds.get(i);
			CheckBox cb = new CheckBox();
			cb.setId("CB_SMS_" + mynd.getNodeID());
			cb.setText(mynd.getName());
			cb.setChecked(msg.getSMSNodes().contains(mynd.getNodeID()+""));
			sb1.append(cb);

			CheckBox cb2 = new CheckBox();
			cb2.setId ("CB_Email_" + mynd.getNodeID());
			cb2.setText(mynd.getName());
			cb2.setChecked(msg.getMailNodes().contains(mynd.getNodeID() + ""));
			sb2.append(cb2);
		}
		if(sb1!=null){
			this.setSmsSb(sb1.toString());
		}
		if(sb2!=null){
			this.setMailSb(sb2.toString());
		}
		
		
		
	}


	


	
	
/*****************get()/set()**************************************************************/	
	public List<Map<String, Object>> getSmsFiledsList() {
		return smsFiledsList;
	}


	public void setSmsFiledsList(List<Map<String, Object>> smsFiledsList) {
		this.smsFiledsList = smsFiledsList;
	}


	public List<Map<String, Object>> getMailFiledsList() {
		return mailFiledsList;
	}


	public void setMailFiledsList(List<Map<String, Object>> mailFiledsList) {
		this.mailFiledsList = mailFiledsList;
	}


	public String getSmsChecked0() {
		return smsChecked0;
	}


	public void setSmsChecked0(String smsChecked0) {
		this.smsChecked0 = smsChecked0;
	}


	public String getSmsChecked1() {
		return smsChecked1;
	}


	public void setSmsChecked1(String smsChecked1) {
		this.smsChecked1 = smsChecked1;
	}


	public String getSmsChecked2() {
		return smsChecked2;
	}


	public void setSmsChecked2(String smsChecked2) {
		this.smsChecked2 = smsChecked2;
	}


	public String getSmsContent() {
		return smsContent;
	}


	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}


	public String getEmailChecked0() {
		return emailChecked0;
	}


	public void setEmailChecked0(String emailChecked0) {
		this.emailChecked0 = emailChecked0;
	}


	public String getEmailChecked1() {
		return emailChecked1;
	}


	public void setEmailChecked1(String emailChecked1) {
		this.emailChecked1 = emailChecked1;
	}


	public String getEmailChecked2() {
		return emailChecked2;
	}


	public void setEmailChecked2(String emailChecked2) {
		this.emailChecked2 = emailChecked2;
	}


	public String getEmailTitle() {
		return emailTitle;
	}


	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}


	public String getEmailContent() {
		return emailContent;
	}


	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}


	public String getSmsTsText() {
		return smsTsText;
	}


	public void setSmsTsText(String smsTsText) {
		this.smsTsText = smsTsText;
	}


	public String getMailTsText() {
		return mailTsText;
	}


	public void setMailTsText(String mailTsText) {
		this.mailTsText = mailTsText;
	}


	public String getSmsChecked3() {
		return smsChecked3;
	}


	public void setSmsChecked3(String smsChecked3) {
		this.smsChecked3 = smsChecked3;
	}


	public String getSmsSb() {
		return smsSb;
	}


	public void setSmsSb(String smsSb) {
		this.smsSb = smsSb;
	}


	public String getMailSb() {
		return mailSb;
	}


	public void setMailSb(String mailSb) {
		this.mailSb = mailSb;
	}


	public String getEmailChecked3() {
		return emailChecked3;
	}


	public void setEmailChecked3(String emailChecked3) {
		this.emailChecked3 = emailChecked3;
	}



	
}
