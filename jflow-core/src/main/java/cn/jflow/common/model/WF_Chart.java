package cn.jflow.common.model;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.util.ContextHolderUtils;


public class WF_Chart extends BaseModel{

	private int WorkID;
	
	private int FID;
	
	private int FK_Node;
	
	private String FK_Flow;
	
	private String DoType;
	
	private String basePath;
	
	public WF_Chart(HttpServletRequest request, HttpServletResponse response,int WorkID,int FID,int FK_Node,String FK_Flow,String DoType,String basePath) {
		super(request, response);
		this.WorkID=WorkID;
		this.FID=FID;
		this.FK_Node=FK_Node;
		this.FK_Flow=FK_Flow;
		this.DoType=DoType;
		this.basePath=basePath;
	}

	public void init() throws Exception{
		if (this.WorkID != 0)
        {
			// 20150507 liuyi 修改 原因: 访问的"WorkOpt/OneWork下/ChartTrack.jsp" 不存在 START
            //String url =  basePath+"WorkOpt/OneWork/ChartTrack.jsp?FID=" + this.FID + "&FK_Flow=" + this.FK_Flow + "&WorkID=" + this.WorkID + "&FK_Node=" + this.FK_Node;
			String url =  basePath+"WF/WorkOpt/OneWork/Track.jsp?FID=" + this.FID + "&FK_Flow=" + this.FK_Flow + "&WorkID=" + this.WorkID + "&FK_Node=" + this.FK_Node;
			// 20150507 liuyi 修改  END
            
			ContextHolderUtils.getResponse().sendRedirect(url);
            return;
        }

		if(DoType.equals("Chart")){
			FlowChart(this.FK_Flow);
		}
		else if(DoType.equals("DT")){
			FlowDT(this.FK_Flow, this.WorkID);
		}
		else if(DoType.equals("ALS")){
			FlowALS(this.FK_Flow);
		}
		else{
			throw new Exception("参数不完整.");
		}
		
	}
	
	public void FlowChart(String fk_flow)
    {
    }
	
    public void FlowDT(String fk_flow, int workid)
    {
    }
    
    public void FlowALS(String fk_flow)
    {
    }
	
}
