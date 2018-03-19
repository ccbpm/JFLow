package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.ReturnRole;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeReturnAttr;
import BP.WF.Template.NodeReturns;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;


public class CanRetuenNodesModel extends BaseModel{

	//private String basePath;
	
	public UiFatory pub = null;
	public CanRetuenNodesModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		super(request, response);
		
		this.pub = new UiFatory();
		//this.basePath = basePath;
	}
	
	public void init(){
		try{
			 Node mynd = new Node();
	         mynd.setNodeID(this.getFK_Node());
	         mynd.RetrieveFromDBSources();
	         
	         if (mynd.getHisReturnRole() != ReturnRole.ReturnSpecifiedNodes){
	                this.pub.append(BaseModel.AddFieldSet("操作错误:", "<br><br>当前节点的退回规则不是退回到指定的节点，所以您不能操作此功能。<br><br>请在节点属性=>退回规则属性里设置，退回指定的节点，此功能才有效。<br><br><br><br>"));
	                return;
	         }
	         
	         this.pub.append(BaseModel.AddTable("width='100%'"));
	         this.pub.append(BaseModel.AddCaptionLeft("为“" + mynd.getName() + "”, 设置可退回的节点。"));
	         
	         NodeReturns rnds = new NodeReturns();
	         rnds.Retrieve(NodeReturnAttr.FK_Node, this.getFK_Node());

	         Nodes nds = new Nodes();
	         nds.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow());
	         int idx = 0;
	         for(Node nd : nds.ToJavaList()){
	        	 
	        	 if (nd.getNodeID() == this.getFK_Node())
	                    continue;
	        	 
	        	 CheckBox cb = this.pub.creatCheckBox("CB_" + nd.getNodeID());
	        	 cb.setText(nd.getName());
	        	 cb.setChecked(rnds.IsExits(NodeReturnAttr.ReturnTo, nd.getNodeID()));
	        	 
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
             
             this.pub.append(BaseModel.AddFieldSet("特别说明:", "1，只有节点属性的退回规则被设置成退回制订的节点，此功能才有效。<br> 2，设置退回的节点如果是当前节点下一步骤的节点，设置无意义，系统不做检查，退回时才做检查。"));
			
		}catch(Exception e){
			this.pub.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
}
