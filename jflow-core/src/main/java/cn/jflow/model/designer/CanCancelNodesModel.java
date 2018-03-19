package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.CancelRole;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeCancelAttr;
import BP.WF.Template.NodeCancels;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;

public class CanCancelNodesModel extends BaseModel{

	//private String basePath;
	
	public UiFatory pub = null;
	public CanCancelNodesModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		super(request, response);
		
		this.pub = new UiFatory();
		//this.basePath = basePath;
	}
	
	public void init(){
		try{
			 Node mynd = new Node();
	         mynd.setNodeID(this.getFK_Node());
	         mynd.RetrieveFromDBSources();
	         
	         if (mynd.getHisCancelRole() != CancelRole.SpecNodes){
	                this.pub.append(BaseModel.AddFieldSet("操作错误:", "<br><br>当前节点的撤销规则不是撤销到指定的节点，所以您不能操作此功能。<br><br>请在节点属性=>撤销规则属性里设置，撤销指定的节点，此功能才有效。<br><br><br><br>"));
	                return;
	         }
	         
	         this.pub.append(BaseModel.AddTable("width='100%'"));
	         this.pub.append(BaseModel.AddCaptionLeft("为“" + mynd.getName() + "”, 设置可撤销的节点。"));
	         
	         NodeCancels rnds = new NodeCancels();
	         rnds.Retrieve(NodeCancelAttr.FK_Node, this.getFK_Node());

	         Nodes nds = new Nodes();
	         
	         nds.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow());
	         
	         int idx = 0;
	         for(Node nd : nds.ToJavaList()){
	        	 
	        	 if (nd.getNodeID() == this.getFK_Node())
	                    continue;
	        	 
	        	 CheckBox cb = this.pub.creatCheckBox("CB_" + nd.getNodeID());
	        	 cb.setText(nd.getName());
	        	 cb.setChecked(rnds.IsExits(NodeCancelAttr.CancelTo, nd.getNodeID()));
	        	 
	        	 this.pub.append(BaseModel.AddTR());
	        	 this.pub.append(BaseModel.AddTDIdx(idx++));
	        	 this.pub.append(BaseModel.AddTD("第" + nd.getStep() + "步"));
	        	 this.pub.append("\n<TD nowrap = 'nowrap'>");
	        	 this.pub.append(cb);
	        	 this.pub.append("</TD>");
                 this.pub.append(BaseModel.AddTREnd());
	         }
	         
	         this.pub.append(BaseModel.AddTRSum());
	         this.pub.append(BaseModel.AddTD());
             Button btn = this.pub.creatButton("Btn_Save");
             btn.setText("Save");
             btn.setCssClass("Btn");
             btn.addAttr("onclick", "onSave()");
             this.pub.append("\n<TD nowrap = 'nowrap'>");
        	 this.pub.append(btn);
        	 this.pub.append("</TD>");
             this.pub.append(BaseModel.AddTD());
             this.pub.append(BaseModel.AddTREnd());
             this.pub.append(BaseModel.AddTableEnd());
             
             this.pub.append(BaseModel.AddFieldSet("特别说明:", "1，只有节点属性的撤销规则被设置成撤销制订的节点，此功能才有效。<br> 2，设置撤销的节点如果是当前节点下一步骤的节点，设置无意义，系统不做检查，撤销时才做检查。"));
			
		}catch(Exception e){
			this.pub.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
}
