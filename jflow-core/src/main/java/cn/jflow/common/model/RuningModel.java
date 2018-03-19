package cn.jflow.common.model;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkFlowAttr;
import BP.WF.Entity.GenerWorkFlows;
import BP.WF.WFState;
import BP.Web.WebUser;

public class RuningModel {

	private String PageID;
	private String PageSmall;
	private String GroupBy;
	private String basePath;
	private String FK_Flow;

	public RuningModel(String basePath, String FK_Flow, String GroupBy, String PageID, String PageSmall){
		this.basePath = basePath;
		this.FK_Flow = FK_Flow;
		this.GroupBy = GroupBy;
		this.PageID = PageID;
		this.PageSmall = PageSmall;
	}
	
	public StringBuilder Pub1 = null;
	public void init() {
		this.Pub1 = new StringBuilder();
		
		if(WebUser.getIsWap()){
			this.BindWap();
			return;
		}
		
		int colspan = 6;
        this.Pub1.append(BaseModel.AddTable("class='am-table am-table-striped am-table-hover table-main'"));
        
//        if (WebUser.getIsWap())
//            this.Pub1.append(BaseModel.AddCaption("<img src='"+basePath+"WF/Img/Home.gif' >&nbsp;<a href='"+basePath+"WF/Home.jsp' >Home</a>-<img src='"+basePath+"WF/Img/EmpWorks.gif' >在途工作"));
//        else
//            this.Pub1.append(BaseModel.AddCaption("在途工作"));
        
        this.Pub1.append(BaseModel.AddTR());
        this.Pub1.append(BaseModel.AddTDTitle("nowarp=true", "序"));
        this.Pub1.append(BaseModel.AddTDTitle("nowarp=true width='40%'", "标题"));
        
        if (!"FlowName".equals(GroupBy))
            this.Pub1.append(BaseModel.AddTDTitle("<a href='"+ PageID +".jsp?GroupBy=FlowName&FK_Flow=" + FK_Flow + "' >流程</a>"));

        if (!"NodeName".equals(GroupBy))
            this.Pub1.append(BaseModel.AddTDTitle("<a href='"+ PageID +".jsp?GroupBy=NodeName&FK_Flow=" + FK_Flow + "' >当前节点</a>"));

        if (!GenerWorkFlowAttr.StarterName.equals(GroupBy))
            this.Pub1.append(BaseModel.AddTDTitle("<a href='"+ PageID +".jsp?GroupBy=StarterName&FK_Flow=" + FK_Flow + "' >发起人</a>"));
        
        this.Pub1.append(BaseModel.AddTDTitle("nowarp=true", "发起日期"));
        this.Pub1.append(BaseModel.AddTDTitle("nowarp=true", "操作"));
        this.Pub1.append(BaseModel.AddTREnd());
        
        String groupVals = "";
        DataTable dt = Dev2Interface.DB_GenerRuning();
        for(DataRow dr : dt.Rows){
     		if(groupVals.contains("@" + dr.getValue(GroupBy)))continue;
     		 groupVals += "@" + dr.getValue(GroupBy);
     	}
        
        int i = 0;
        //boolean is1 = false;
        String title = null;
        String workid = null;
        String fk_flow = null;
        int gIdx = 0;
        String[] gVals = groupVals.split("@");
        for (String g : gVals){
        	 if (StringHelper.isNullOrEmpty(g))continue;
        	 gIdx++;
        	 
        	  this.Pub1.append(BaseModel.AddTR());
              this.Pub1.append(BaseModel.AddTD("colspan=" + colspan + " onclick=\"GroupBarClick('" + basePath + "','" + gIdx + "')\" ", "<div style='text-align:left; float:left' ><img src='"+basePath+"WF/Img/Min.gif' alert='Min' id='Img" + gIdx + "'   border=0 />&nbsp;<b>" + g + "</b>"));
              this.Pub1.append(BaseModel.AddTREnd());
              
              for(DataRow dr : dt.Rows){
            	  if(!g.equals(dr.getValue(GroupBy).toString()))continue;
            	  i++;
                  this.Pub1.append(BaseModel.AddTR("ID='" + gIdx + "_" + i + "'"));
                  this.Pub1.append(BaseModel.AddTDIdx(i));
                  
                  WFState wfstate = WFState.forValue(Integer.parseInt(dr.getValue("WFState").toString()));
                  title = "<span class='am-icon-sign-out'></span>" + dr.getValue("Title").toString();
                  
                  workid = dr.getValue("WorkID").toString();
                  fk_flow = dr.getValue("FK_Flow").toString();
                  
                  this.Pub1.append("\n<TD><a href=\"javascript:WinOpen('"+ basePath +"WF/WFRpt.jsp?WorkID=" + workid + "&FK_Flow=" + fk_flow + "&FID=" + dr.getValue("FID") + "')\" >" + title + "</a></TD>");
                  
            	  if (!"FlowName".equals(GroupBy))
            		  this.Pub1.append(BaseModel.AddTD(dr.getValue("FlowName").toString()));
            	  
            	  if (!"NodeName".equals(GroupBy))
            		  this.Pub1.append(BaseModel.AddTD(dr.getValue("NodeName").toString()));
            	  
            	  if (!GenerWorkFlowAttr.StarterName.equals(GroupBy))
            		  this.Pub1.append(BaseModel.AddTD(dr.getValue(GenerWorkFlowAttr.StarterName).toString()));
            	  
            	  this.Pub1.append(BaseModel.AddTD(dr.getValue("RDT").toString()));
                  this.Pub1.append(BaseModel.AddTDBegin());
                  this.Pub1.append("<a href=\"javascript:UnSend('" + basePath + "','" + PageSmall + "','" + dr.getValue("FID") + "','" + workid + "','" + fk_flow + "');\" ><span class='am-icon-remove'></span>撤消发送</a>&nbsp;&nbsp;");
                  this.Pub1.append("<a href=\"javascript:Press('" + basePath + "','" + dr.getValue("FID") + "','" + workid + "','" + fk_flow + "');\" ><span class='am-icon-pencil-square-o'></span>催办</a>");

                  this.Pub1.append(BaseModel.AddTDEnd());
                  this.Pub1.append(BaseModel.AddTREnd());
              }
        }
        this.Pub1.append(BaseModel.AddTRSum());
        this.Pub1.append(BaseModel.AddTD("colspan=" + colspan, "&nbsp;"));
        this.Pub1.append(BaseModel.AddTREnd());
        this.Pub1.append(BaseModel.AddTableEnd());
	}
	
	public void BindWap(){
		
		this.Pub1.append(BaseModel.AddFieldSet("<img src='"+basePath+"WF/Img/Home.gif' ><a href='"+basePath+"WF/Home.jsp' >Home</a>-<img src='"+basePath+"WF/Img/EmpWorks.gif' >" + "在途工作"));
		String sql = " SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B  WHERE A.WorkID=B.WorkID   AND B.FK_EMP='" + WebUser.getNo() + "' AND B.IsEnable=1";
        GenerWorkFlows gwfs = new GenerWorkFlows();
        gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
       //int i = 0;
       //boolean is1 = true;
        this.Pub1.append(BaseModel.AddUL());
        for (GenerWorkFlow gwf : gwfs.ToJavaList()){
        	 //i++;
             //this.Pub1.append(BaseModel.AddTR(is1));
        	 //this.Pub1.append(BaseModel.AddTDBegin("border=0"));
        	 this.Pub1.append(BaseModel.AddLi(gwf.getTitle() + gwf.getNodeName()));
             this.Pub1.append("<a href=\"javascript:Do('您确认吗？','"+basePath+"WF/MyFlowInfo" + Glo.getFromPageType() + ".jsp?DoType=UnSend&FID=" + gwf.getFID() + "&WorkID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "');\" ><img src='"+basePath+"WF/Img/Btn/delete.gif' border=0 />撤消</a>");
             this.Pub1.append("<a href=\"javascript:WinOpen('"+basePath+"WF/WFRpt.jsp?WorkID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "&FID=0')\" ><img src='"+basePath+"WF/Img/Btn/rpt.gif' border=0 />报告</a>");
        	
        }
        this.Pub1.append(BaseModel.AddULEnd());
        this.Pub1.append(BaseModel.AddFieldSetEnd());
	}
	
	
	public String getPageID() {
		return PageID;
	}
	public void setPageID(String pageID) {
		PageID = pageID;
	}
	public String getPageSmall() {
		return PageSmall;
	}
	public void setPageSmall(String pageSmall) {
		PageSmall = pageSmall;
	}
	public String getGroupBy() {
		return GroupBy;
	}
	public void setGroupBy(String groupBy) {
		GroupBy = groupBy;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
}
