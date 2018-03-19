package cn.jflow.model.wf.admin.ccbpm;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataTable;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;

public class WelcomeModel extends BaseModel {

	public WelcomeModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	
	//流程引擎信息
	public int totalFlow=0;
	public int runFlowNum=0;
	public int nodeNum=0;
	public String avgNum;
	public String flowRate;
	public int flowRateNum;
	//考核信息
	public int beforeOver=0;
	public int afterOver=0;
	public int inTimeOverCount=0;
	public int afterOverCount=0;
	public int totalCount=0;
	public String asRate;
	public int runningFlowOverTime=0;
	
	
	public void init(){
		 totalFlow = BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM WF_Flow ");
         runFlowNum = BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM WF_Flow WHERE IsCanStart=1  ");
         nodeNum = BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(NodeID) FROM WF_Node ");
         DecimalFormat df = new DecimalFormat("#.00");  
         if(totalFlow==0){
        	 totalFlow=1; 
         }
         //平均每流程发起数量。
         avgNum = df.format(Double.valueOf(nodeNum) /Double.valueOf(totalFlow));
         //流程启用比率。
         flowRate = df.format(Double.valueOf(runFlowNum) /Double.valueOf(totalFlow)*100) ;
         flowRateNum=Integer.parseInt(new DecimalFormat("0").format(Double.valueOf(flowRate)));
         
         
         
         //OverMinutes小于0表明提前 
         String sql = "SELECT SUM(OverMinutes) FROM WF_CH WHERE  OverMinutes <0";
         beforeOver = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);

         //OverMinutes大于0表明逾期
         sql = "SELECT SUM(OverMinutes) FROM WF_CH WHERE OverMinutes >0 ";
         afterOver = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);


         sql = "SELECT SUM(ASNum) AS ASNum , SUM(CSNum) CSNum ,SUM(AllNum) AllNum FROM V_TOTALCH  ";
         DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

         if (dt.Rows.size() == 1){
        	 //按时
        	 String asNum=String.valueOf(dt.Rows.get(0).get("asnum"));
        	 inTimeOverCount=Integer.parseInt(StringHelper.isNullOrEmpty(asNum)?"0":asNum);
        	 //超时
        	 String CSNum=String.valueOf(dt.Rows.get(0).get("csnum"));
        	 afterOverCount=Integer.parseInt(StringHelper.isNullOrEmpty(CSNum)?"0":CSNum);
        	 
        	 String AllNum=String.valueOf(dt.Rows.get(0).get("allnum"));
        	 totalCount=Integer.parseInt(StringHelper.isNullOrEmpty(AllNum)?"0":AllNum);
         }

         //求按时办结率.
         if (totalCount == 0)
             asRate = "0";
         else
             asRate =  df.format(Double.valueOf(inTimeOverCount) /Double.valueOf(totalCount)*100) ;

         //在运行的逾期.
         sql = "SELECT COUNT(WorkID) as Num  FROM WF_GenerWorkFlow WHERE SDTOfNode >='2015-07-06 10:43' AND WFState NOT IN (0,3)";
         runningFlowOverTime = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);

	}


	public int getTotalFlow() {
		return totalFlow;
	}


	public void setTotalFlow(int totalFlow) {
		this.totalFlow = totalFlow;
	}


	public int getRunFlowNum() {
		return runFlowNum;
	}


	public void setRunFlowNum(int runFlowNum) {
		this.runFlowNum = runFlowNum;
	}


	public int getNodeNum() {
		return nodeNum;
	}


	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}


	public String getAvgNum() {
		return avgNum;
	}


	public void setAvgNum(String avgNum) {
		this.avgNum = avgNum;
	}


	public String getFlowRate() {
		return flowRate;
	}


	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}


	public int getFlowRateNum() {
		return flowRateNum;
	}


	public void setFlowRateNum(int flowRateNum) {
		this.flowRateNum = flowRateNum;
	}


	public int getBeforeOver() {
		return beforeOver;
	}


	public void setBeforeOver(int beforeOver) {
		this.beforeOver = beforeOver;
	}


	public int getAfterOver() {
		return afterOver;
	}


	public void setAfterOver(int afterOver) {
		this.afterOver = afterOver;
	}


	public int getInTimeOverCount() {
		return inTimeOverCount;
	}


	public void setInTimeOverCount(int inTimeOverCount) {
		this.inTimeOverCount = inTimeOverCount;
	}


	public int getAfterOverCount() {
		return afterOverCount;
	}


	public void setAfterOverCount(int afterOverCount) {
		this.afterOverCount = afterOverCount;
	}


	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public String getAsRate() {
		return asRate;
	}


	public void setAsRate(String asRate) {
		this.asRate = asRate;
	}


	public int getRunningFlowOverTime() {
		return runningFlowOverTime;
	}


	public void setRunningFlowOverTime(int runningFlowOverTime) {
		this.runningFlowOverTime = runningFlowOverTime;
	}

	
}
