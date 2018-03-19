package cn.jflow.common.model;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.QueryObject;
import BP.WF.Dev2Interface;
import BP.WF.Entity.GetTask;
import BP.WF.Entity.GetTasks;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.FlowAppType;
import BP.WF.Template.FlowAttr;
import BP.Web.WebUser;


public class GetTaskModel {

	private String PageID;
	private String PageSmall;
	private String basePath;
	private long WorkID;
	private int FK_Node;
	private int ToNode;
	private String FK_Flow;
	private String DoType;

	public GetTaskModel(String basePath,long WorkID, int FK_Node, int ToNode, String FK_Flow, String DoType, String PageID, String PageSmall){
		this.basePath = basePath;
		this.WorkID = WorkID;
		this.FK_Node = FK_Node;
		this.ToNode = ToNode;
		this.FK_Flow = FK_Flow;
		this.DoType = DoType;
		this.PageID = PageID;
		this.PageSmall = PageSmall;
	}
	
	public StringBuilder Pub1 = null;
	public void init() {
		this.Pub1 = new StringBuilder();
		
		if("Tackback".equals(DoType)){
			try{
				String s = Dev2Interface.Node_Tackback(FK_Node, WorkID, ToNode);
				//this.Pub1.append(BaseModel.AddTable("align='left'"));
				this.Pub1.append(BaseModel.AddCaption("<a href='"+basePath+"WF/GetTask" + PageSmall + ".jsp'><img src='"+basePath+"WF/Img/Start.gif' >取回处理</a>-<a href='"+basePath+"WF/GetTask" + PageSmall + ".jsp?FK_Flow=" + this.FK_Flow + "&FK_Node=" + this.FK_Node + "'>返回</a>"));
				
				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTDBegin());
				this.Pub1.append(BaseModel.AddFieldSet("取回成功", "<h3>工作已经放入您的待办里</h3><hr><a href='"+basePath+"WF/MyFlow" + PageSmall + ".jsp?FK_Flow=" + this.FK_Flow + "&FK_Node=" + this.ToNode + "&WorkID=" + this.WorkID + "&FID=0' >点这里请执行</a><br><br>"));
				this.Pub1.append(BaseModel.AddTDEnd());
				this.Pub1.append(BaseModel.AddTREnd());
				
				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTD(s));
				this.Pub1.append(BaseModel.AddTDEnd());
				this.Pub1.append(BaseModel.AddTREnd());
				
				//this.Pub1.append(BaseModel.AddTableEnd());
			}catch(Exception e){
				this.Pub1.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
			}
			
			return;
		}
		
		if(!"".equals(FK_Flow) && null != FK_Flow){
			this.BindWorkList();
			return;
		}
		
		Flows fls = new Flows();
		QueryObject qo = new QueryObject(fls);
		qo.addOrderBy(FlowAttr.FK_FlowSort);
		qo.DoQuery();
		
		//int colspan = 5;
		//this.Pub1.append(BaseModel.AddTable("align='left'"));
		this.Pub1.append(BaseModel.AddCaption("<a href='"+basePath+"WF/GetTask" + PageSmall + ".jsp'><img src='"+basePath+"WF/Img/Start.gif' >取回处理</a>"));
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("序"));
		this.Pub1.append(BaseModel.AddTDTitle("流程类别"));
		this.Pub1.append(BaseModel.AddTDTitle("名称"));
		this.Pub1.append(BaseModel.AddTDTitle("流程图"));
		this.Pub1.append(BaseModel.AddTDTitle("描述"));
		this.Pub1.append(BaseModel.AddTREnd());
		
	    int i = 0;
        boolean is1 = false;
        String fk_sort = "";
        for (Flow fl : fls.ToJavaList()){
        	if(fl.getFlowAppType() == FlowAppType.DocFlow)continue;
        	
        	i++;
            //this.Pub1.append(BaseModel.AddTR(is1));
        	this.Pub1.append(BaseModel.AddTR());
            is1 = !is1;
            this.Pub1.append(BaseModel.AddTDIdx(i));
            if (fk_sort.equals(fl.getFK_FlowSort())){
                this.Pub1.append(BaseModel.AddTD());
            }else{
            	this.Pub1.append(BaseModel.AddTDB(fl.getFK_FlowSortText()));
            }
            fk_sort = fl.getFK_FlowSort();
            this.Pub1.append(BaseModel.AddTD("<a href='"+basePath+"WF/GetTask" + PageSmall + ".jsp?FK_Flow=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01' >" + fl.getName() + "</a>"));

            this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:WinOpen('"+basePath+"WF/WorkOpt/OneWork/OneWork.htm?FK_Flow=" + fl.getNo() + "&DoType=Chart','sd');\"  >打开</a>"));
            this.Pub1.append(BaseModel.AddTD(fl.getNote()));
            this.Pub1.append(BaseModel.AddTREnd());
        }
        //this.Pub1.append(BaseModel.AddTableEnd());
	}
	
	public void BindWorkList(){
		int colspan = 10;
		//this.Pub1.append(BaseModel.AddTable("width='90%' align='left'"));
		this.Pub1.append(BaseModel.AddCaption("<a href='"+basePath+"WF/GetTask" + PageSmall + ".jsp'><img src='"+basePath+"WF/Img/Start.gif' >取回处理</a>"));
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("序"));
		this.Pub1.append(BaseModel.AddTDTitle("标题"));
		this.Pub1.append(BaseModel.AddTDTitle("发起人"));
		this.Pub1.append(BaseModel.AddTDTitle("发起时间"));
		this.Pub1.append(BaseModel.AddTDTitle("停留节点"));
		this.Pub1.append(BaseModel.AddTDTitle("当前处理人"));
		this.Pub1.append(BaseModel.AddTDTitle("到达时间"));
		this.Pub1.append(BaseModel.AddTDTitle("应完成时间"));
		this.Pub1.append(BaseModel.AddTDTitle("操作"));
		this.Pub1.append(BaseModel.AddTREnd());
		
		 // 根据发起人的权限来判断，是否具有操作此人员的权限。
        GetTasks jcs = new GetTasks(this.FK_Flow);
        //String canDealNodes = "";
        int idx = 1;
        for (GetTask jc : jcs.ToJavaList()){
        	  /* 判断我是否可以处理当前点数据？ */
        	 if(!jc.Can_I_Do_It())continue;
        	 
        	 //canDealNodes += "''";
        	 DataTable dt = DBAccess.RunSQLReturnTable("SELECT * FROM WF_EmpWorks WHERE FK_Node IN (" + jc.getCheckNodes() + ") AND FK_Flow='" + this.FK_Flow + "' AND FK_Dept LIKE '" + WebUser.getFK_Dept() + "%'");
        	 if(0 == dt.Rows.size()){
        		 if(WebUser.getFK_Dept().length() >= 4){
        			 dt = DBAccess.RunSQLReturnTable("SELECT * FROM WF_EmpWorks WHERE FK_Node IN (" + jc.getCheckNodes() + ") AND FK_Flow='" + this.FK_Flow + "' AND FK_Dept LIKE '" + WebUser.getFK_Dept().substring(0, 2) + "%'");
        		 }else{
        			 dt = DBAccess.RunSQLReturnTable("SELECT * FROM WF_EmpWorks WHERE FK_Node IN (" + jc.getCheckNodes() + ") AND FK_Flow='" + this.FK_Flow + "' AND FK_Dept LIKE '" + WebUser.getFK_Dept() + "%'");
        		 }
        	 }
        	 this.Pub1.append(BaseModel.AddTR());
        	 this.Pub1.append("\n<TD  colspan='" + colspan + "' align='left'>" + jc.getName() + " ;  =》可跳转审核的节点:" + jc.getCheckNodes() + "</TD>");
        	 this.Pub1.append(BaseModel.AddTREnd());
        	 for(DataRow dr : dt.Rows){
        		 this.Pub1.append(BaseModel.AddTR());
        		 this.Pub1.append(BaseModel.AddTDIdx(idx++));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("Title").toString()));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("Starter").toString()));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("RDT").toString()));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("NodeName").toString()));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("FK_EmpText").toString()));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("ADT").toString()));
        		 this.Pub1.append(BaseModel.AddTD(dr.getValue("SDT").toString()));
        		 this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:WinOpen('"+basePath+"WF/WFRpt.jsp?WorkID=" + dr.getValue("WorkID") + "&FK_Flow=" + this.FK_Flow + "&FID=" + dr.getValue("FID") + "')\">报告</a> - <a href=\"javascript:Tackback('" + this.FK_Flow + "','" + dr.getValue("FK_Node") + "','" + jc.getNodeID() + "','" + dr.getValue("WorkID") + "')\">取回</a>"));
        		 this.Pub1.append(BaseModel.AddTREnd());
        	 }
        }
        //this.Pub1.append(BaseModel.AddTableEnd());
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
	public long getWorkID() {
		return WorkID;
	}
	public void setWorkID(long workID) {
		WorkID = workID;
	}
	public int getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
	public int getToNode() {
		return ToNode;
	}
	public void setToNode(int toNode) {
		ToNode = toNode;
	}
	public String getDoType() {
		return DoType;
	}
	public void setDoType(String doType) {
		DoType = doType;
	}
}
