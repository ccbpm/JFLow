package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.Attr;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeSheet;
import BP.WF.Template.NodeSheets;
import BP.WF.XML.FeatureSet;
import BP.WF.XML.FeatureSets;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;

public class FeatureSetUIModel extends BaseModel{

	private String basePath;
	
	public UiFatory pub1 = null;
	public UiFatory pub2 = null;
	public FeatureSetUIModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		super(request, response);
		
		this.basePath = basePath;
		this.pub1 = new UiFatory();
		this.pub2 = new UiFatory();
	}
	
	public String Lab = null;
	public void init(){
		try{
			   this.BindLeft();
			   
			   String doType = this.getDoType();
			   if (null == doType || "".equals(doType)){
	                this.pub2.append(BaseModel.AddFieldSet("帮助", " 特性集就是整个流程节点中的特定属性批量的修改。"));
	                return;
	           }
			   
			   this.pub2.append(BaseModel.AddFieldSet("编辑:" + this.Lab));
			   this.pub2.append(BaseModel.AddTable("border=0"));
			   this.pub2.append(BaseModel.AddTR());
			   
			   if("Base".equals(doType)){
				   this.pub2.append(BaseModel.AddTDTitle("步骤"));
				   this.pub2.append(BaseModel.AddTDTitle("节点名称"));
				   this.pub2.append(BaseModel.AddTDTitle("是否可以退回"));
				   this.pub2.append(BaseModel.AddTDTitle("是否可删除"));
				   this.pub2.append(BaseModel.AddTDTitle("是否可转发"));
				   this.pub2.append(BaseModel.AddTDTitle("允许分配工作否?"));
				   this.pub2.append(BaseModel.AddTDTitle("是否可以查看工作报告?"));
				   this.pub2.append(BaseModel.AddTDTitle("是否是保密步骤?"));
			   }else if("FormType".equals(doType)){
				   this.pub2.append(BaseModel.AddTDTitle("步骤"));
				   this.pub2.append(BaseModel.AddTDTitle("节点名称"));
				   this.pub2.append(BaseModel.AddTDTitle("类型"));
				   this.pub2.append(BaseModel.AddTDTitle("URL"));
			   }else{
				   this.pub2.append(BaseModel.AddTDTitle("步骤"));
				   this.pub2.append(BaseModel.AddTDTitle("节点名称"));
				   this.pub2.append(BaseModel.AddTDTitle(this.Lab));
			   }
			   this.pub2.append(BaseModel.AddTREnd());
			   
			   NodeSheets nds = new NodeSheets();
	           nds.Retrieve("FK_Flow", this.getFK_Flow());
	           NodeSheet mynd = new NodeSheet();
	           
	           Attr attr = null;
	           attr = mynd.getEnMap().GetAttrByKey(doType);
	           
	           for (NodeSheet nd : nds.ToJavaList()){
	        	   if(this.getFK_Node() == nd.getNodeID()){
	        		   this.pub2.append(BaseModel.AddTR1());
	        	   }else{
	        		   this.pub2.append(BaseModel.AddTR());
	        	   }
	        	   
	        	   if("Base".equals(doType)){
	        	   }else if("FormType".equals(doType)){
	        		   this.pub2.append(BaseModel.AddTDIdx(nd.getStep()));
	        		   this.pub2.append(BaseModel.AddTD(nd.getName()));
	        		   DDL ddl = this.pub2.creatDDL("DDL_" + nd.getNodeID());
                       ddl.BindSysEnum(NodeAttr.FormType, nd.GetValIntByKey(NodeAttr.FormType));
                       this.pub2.append("\n<TD nowrap = 'nowrap'>");
                       this.pub2.append(ddl);
                       this.pub2.append("</TD>");
                       TextBox mytbURL = this.pub2.creatTextBox("TB_" + nd.getNodeID());
                       mytbURL.setText(nd.GetValStringByKey(NodeAttr.FormUrl));
                       mytbURL.setCols(50);
                       this.pub2.append("\n<TD nowrap = 'nowrap'>");
                       this.pub2.append(mytbURL);
                       this.pub2.append("</TD>");
	        	   }else{
	        		   this.pub2.append(BaseModel.AddTDIdx(nd.getStep()));
	        		   this.pub2.append(BaseModel.AddTD(nd.getName()));
	        		   switch (attr.getUIContralType()){
	        		   		case TB:
		        		   		TextBox mytb = this.pub2.creatTextBox("TB_" + nd.getNodeID());
		        		   		mytb.setText(nd.GetValStringByKey(doType));
		        		   		mytb.setCols(50);
		        		   		this.pub2.append("\n<TD nowrap = 'nowrap'>");
		                        this.pub2.append(mytb);
		                        this.pub2.append("</TD>");
	        		   			break;
	        		   	    case CheckBok:
	        		   	    	CheckBox mycb = this.pub2.creatCheckBox("CB_" + nd.getNodeID());
	        		   	    	mycb.setText(attr.getDesc());
	        		   	    	mycb.setChecked(nd.GetValBooleanByKey(doType));
	        		   	    	this.pub2.append("\n<TD nowrap = 'nowrap'>");
		                        this.pub2.append(mycb);
		                        this.pub2.append("</TD>");
	        		   	    	break;
	        		   	    case DDL:
	        		   	    	DDL ddlm = this.pub2.creatDDL("DDL_" + nd.getNodeID());
	        		   	    	ddlm.BindSysEnum(attr.getUIBindKey(), nd.GetValIntByKey(doType));
	        		   	    	this.pub2.append("\n<TD nowrap = 'nowrap'>");
		                        this.pub2.append(ddlm);
		                        this.pub2.append("</TD>");
	        		   	    	break;
	        		   	    default:
                                break;
	        		   }
	        	   }
	        	   this.pub2.append(BaseModel.AddTREnd());
	           }
			
	           this.pub2.append(BaseModel.AddTableEndWithHR());
	           Button btn = this.pub2.creatButton("Btn_Save");
	           btn.setText(" Save ");
	           btn.setCssClass("Btn");
	           btn.addAttr("onclick", "onSave()");
	           this.pub2.append(btn);
	           this.pub2.append(BaseModel.AddFieldSetEnd());
	           
		}catch(Exception e){
			this.pub1.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
	
	public void BindLeft(){
		 FeatureSets fss = new FeatureSets();
         fss.RetrieveAll();
         
         this.pub1.append(BaseModel.AddFieldSet("流程特性"));
         this.pub1.append(BaseModel.AddUL());
         for (FeatureSet fs : fss.ToJavaList()){
             if (fs.getNo().equals(this.getDoType())) {
            	 this.pub1.append(BaseModel.AddLiB(basePath+"WF/Admin/FeatureSetUI.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&DoType=" + fs.getNo(), fs.getName()));
                 this.Lab = fs.getName();
             } else{
            	 this.pub1.append(BaseModel.AddLi(basePath+"WF/Admin/FeatureSetUI.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&DoType=" + fs.getNo(), fs.getName()));
             }
         }
         this.pub1.append(BaseModel.AddULEnd());

         this.pub1.append(BaseModel.AddFieldSetEnd());  
	}
}
