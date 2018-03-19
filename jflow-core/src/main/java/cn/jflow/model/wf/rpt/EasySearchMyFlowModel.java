package cn.jflow.model.wf.rpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;

public class EasySearchMyFlowModel extends BaseModel {
	public StringBuffer MyFq = new StringBuffer();
	public StringBuffer MyDb = new StringBuffer();
	public StringBuffer MyZt = new StringBuffer();
	public StringBuffer Ygd = new StringBuffer();
	public EasySearchMyFlowModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}

	public  void init(){
		//我发起的流程
		String myfqSql="select FK_Flow, FlowName,Count(WorkID) as Num from WF_GenerWorkFlow  WHERE Starter='" + WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dt = DBAccess.RunSQLReturnTable(myfqSql);
		this.MyFq.append("<ul>");
    	for(int i=0;i<dt.Rows.size();i++){
    		DataRow dr=dt.Rows.get(i);
    		this.MyFq.append("<li><a href='"+Glo.getCCFlowAppPath()+"WF/Comm/Search.jsp?EnsName=BP.WF.Data.MyStartFlows&FK_Flow=" + dr.get("fk_flow") + "&WFSta=All&TSpan=All' >" + dr.get("flowname") + "(" + dr.get("num") + ")</a></li>");
	    }
	    this.MyFq.append("</ul>");
	    
	    // 我的待办流程
	    String mydbSql="select FK_Flow, FlowName,Count(WorkID) as Num from wf_empworks  WHERE FK_Emp='"+WebUser.getNo()+"' GROUP BY FK_Flow, FlowName ";
	    dt = DBAccess.RunSQLReturnTable(mydbSql);
	    this.MyDb.append("<ul>");
	    for(int i=0;i<dt.Rows.size();i++){
	    	DataRow dr=dt.Rows.get(i);
	    	this.MyDb.append("<li><a href='"+Glo.getCCFlowAppPath()+"WF/App/Classic/Todolist.jsp?FK_Flow="+dr.get("fk_flow")+"'>" + dr.get("flowname") + "(" + dr.get("num") + ")</a></li>");
	    }
	    this.MyDb.append("</ul>");
	    
	    
	    // 我的在途流程
	    dt = Dev2Interface.DB_TongJi_Runing(); 
	    this.MyZt.append("<ul>");
	    for(int i=0;i<dt.Rows.size();i++){
	    	DataRow dr=dt.Rows.get(i);
	    	this.MyZt.append("<li><a href='"+Glo.getCCFlowAppPath()+"WF/App/Classic/Runing.jsp?FK_Flow=" + dr.get("fk_flow") + "'>" + dr.get("flowname") + "(" + dr.get("num") + ")</a></li>");
	    }
	    this.MyZt.append("</ul>");
	    
	    // 已归档
	    dt = Dev2Interface.DB_TongJi_FlowComplete(); 
	    this.Ygd.append("<ul>");
	    for(int i=0;i<dt.Rows.size();i++){
	    	DataRow dr=dt.Rows.get(i);
	    	this.Ygd.append("<li><a href='"+Glo.getCCFlowAppPath()+"WF/Comm/Search.jsp?EnsName=BP.WF.Data.MyFlows&FK_Flow=" + dr.get("fk_flow") + "&WFSta=1&TSpan=All' >" + dr.get("flowname") + "(" + dr.get("num") + ")</a></li>");
	    }
	    this.Ygd.append("</ul>");
	}

	public String getMyFq() {
		
		return MyFq.toString();
	}


	public String getMyDb() {
		return MyDb.toString();
	}


	public String getMyZt() {
		return MyZt.toString();
	}


	public String getYgd() {
		return Ygd.toString();
	}


	
	
	
}
