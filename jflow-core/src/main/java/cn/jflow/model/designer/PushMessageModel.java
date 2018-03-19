package cn.jflow.model.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttrs;
import BP.WF.Node;
import BP.WF.Template.PushMsg;
import BP.WF.Template.PushMsgs;
import cn.jflow.system.ui.UiFatory;

public class PushMessageModel {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public UiFatory ui = null;
	public String basePath = "";

	
	public PushMessageModel (HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	/**
	 * 
	 */
	public final int getSetup(){
		try {
			return Integer.parseInt(request.getParameter("Step"));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public final String getDotype(){
		return request.getParameter("DoType");
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
	
	
	/**
	 * 消息列表
	 */
	public List<Map<String,Object>> pushMesList=new ArrayList<Map<String,Object>>();
	
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
		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());
		nd.RetrieveFromDBSources();
		if ("Del".equals(getDotype())) {
			PushMsg enDel = new PushMsg();
			enDel.setMyPK(this.getMyPk());
			enDel.Delete();
		}
		    
		PushMsgs ens = new PushMsgs(String.valueOf(getFK_Flow()));

		

	    List<Map<String,Object>> alist=new ArrayList<Map<String,Object>>(); 
		for(int j=0;j<ens.size();j++){
			PushMsg en=(PushMsg) ens.get(j);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("FK_Event",en.getFK_Event());
			map.put("MailPushWayText",en.getMailPushWayText());
			map.put("MailTitle_Real",en.getMailTitle_Real());
			map.put("SMSPushWayText",en.getSMSPushWayText());
			map.put("mypk",en.getMyPK());
			
			alist.add(map);
		}
		this.setPushMesList(alist);
	}

	
	
/*****************get()/set()**************************************************************/	
	

	public List<Map<String, Object>> getPushMesList() {
		return pushMesList;
	}

	public void setPushMesList(List<Map<String, Object>> pushMesList) {
		this.pushMesList = pushMesList;
	}

}
