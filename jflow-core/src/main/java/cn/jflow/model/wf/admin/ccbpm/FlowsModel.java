package cn.jflow.model.wf.admin.ccbpm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.Data.CHSta;
import BP.WF.Template.FlowSorts;
import cn.jflow.common.model.BaseModel;

public class FlowsModel extends BaseModel{

	public FlowsModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	
	private FlowSorts flowSorts =null;
	private Flows flows =null;
	
	public void page_load(){
		  //类别.
        flowSorts = new BP.WF.Template.FlowSorts();
        flowSorts.RetrieveAll();

        //流程.
        flows = new BP.WF.Flows();
        flows.RetrieveAll();



        // 获得超期时间
        DataTable dtOverTimeMin
         = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Flow, SUM(OverMinutes) AS OverMinutes FROM WF_CH WHERE OverMinutes > 0 GROUP BY FK_Flow  ");

        //及时完成
        DataTable dtInTimeCount
         = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Flow,COUNT( distinct WorkID) Num FROM WF_CH WHERE CHSta='" +
                                           CHSta.JiShi.getValue() + "' GROUP BY FK_Flow ");

        //按期完成
       DataTable dtOnTimeCount
         = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Flow,COUNT( distinct WorkID) Num FROM WF_CH WHERE CHSta='" +
                                           CHSta.AnQi.getValue() + "' GROUP BY FK_Flow ");

        // 获得逾期的工作数量.
        DataTable dtOverTimeCount
         = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Flow,COUNT( distinct WorkID) Num FROM WF_CH WHERE CHSta='" +
                                           CHSta.YuQi.getValue() + "' GROUP BY FK_Flow ");

        // 获得超期的工作数量.
        DataTable dtCqTimeCount
         = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Flow,COUNT( distinct WorkID) Num FROM WF_CH WHERE CHSta='" +
                                           CHSta.ChaoQi.getValue() + "' GROUP BY FK_Flow  ");


        // 获得流程状态.
       DataTable dt
            = BP.DA.DBAccess.RunSQLReturnTable("SELECT FK_Flow, WFSta, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState!=0 GROUP BY FK_Flow,WFSta ");
       
       //设置流程状态
       for(int i=0;i<flows.size();i++){
    	   Flow flow=(Flow) flows.get(i);
    	   String flowNo=flow.getNo();
    	   String sta0 = "0";
    	   String sta1 = "0";
    	   String sta2 = "0";
    	   for(int j=0;j<dt.Rows.size();j++){
    		   DataRow dr=dt.Rows.get(j);
    		   if(!flowNo.equals(dr.get("FK_Flow"))){
    			   continue;
    		   }
    		   if("0".equals(dr.get("WFSta"))){
    			   sta0 = String.valueOf(dr.get("WFSta"));
                   continue;
    		   }
    		   if("1".equals(dr.get("WFSta"))){
    			   sta1 = String.valueOf(dr.get("WFSta"));
    			   continue;
    		   }
    		   if("2".equals(dr.get("WFSta"))){
    			   sta2 =String.valueOf(dr.get("WFSta"));
    			   continue;
    		   }
    	   }
    	   flow.setSta0(sta0);
    	   flow.setSta1(sta1);
    	   flow.setSta2(sta2);
       }
       
	}
	
	
	
	public FlowSorts getFlowSorts() {
		return flowSorts;
	}

	public void setFlowSorts(FlowSorts flowSorts) {
		this.flowSorts = flowSorts;
	}

	public Flows getFlows() {
		return flows;
	}

	public void setFlows(Flows flows) {
		this.flows = flows;
	}

	
	
}
