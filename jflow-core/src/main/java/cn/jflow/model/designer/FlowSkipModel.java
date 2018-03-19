package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.Nodes;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;

public class FlowSkipModel {

	//private String basePath;
	private HttpServletRequest _request = null;
	//private HttpServletResponse _response = null;
	
	public StringBuilder Pub = null;
	public FlowSkipModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		//this.basePath = basePath; 
		this._request = request;
		Pub = new StringBuilder();
		//this._response = response;
	}

	
	public String getParameter(String key){
		String value = _request.getParameter(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
	}
	public final String getFK_Flow(){
		return getParameter("FK_Flow");
	}
	public final int getFK_Node(){
		String fk_node = getParameter("FK_Node");
		if("".equals(fk_node)){
			return 0;
		}else{
			return Integer.parseInt(fk_node);
		}
	}
	public final Long getFID(){
		String fid = getParameter("FID"); 
		if("".equals(fid)){
			return 0l;
		}else{
			return Long.valueOf(fid);
		}
	}
	public final Long getWorkID(){
		String workid = getParameter("WorkID"); 
		if("".equals(workid)){
			return 0l;
		}else{
			return Long.valueOf(workid);
		}
	}
	
	public void init(){
		
		 Nodes nds = new Nodes(this.getFK_Flow());
		 DDL SkipToNode = new DDL();
		 SkipToNode.setId("DDL_SkipToNode");
		 SkipToNode.setName("DDL_SkipToNode");
		 for (Node nd : nds.ToJavaList()){
			 SkipToNode.Items.add(new ListItem("步骤:" + nd.getStep() + "名称:" + nd.getName(), String.valueOf(nd.getNodeID())));
		 }
		 this.Pub.append(SkipToNode);
	}
	
}
