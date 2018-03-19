package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WF_WorkOpt_OneWork_ChartTrack extends BaseModel{
	
	private int workID;
	private int fk_flow;
	private int fid;
	private String basePath;
	
	public WF_WorkOpt_OneWork_ChartTrack(HttpServletRequest request,
			HttpServletResponse response,int workID,int fk_flow,int fid,String basePath) {
		super(request, response);
		this.workID=workID;
		this.fk_flow=fk_flow;
		this.fid=fid;
		this.basePath=basePath;
	}

	public void init(){
		try
        {
            String url = "";
            url = basePath+"ChartTrack.jsp?FID=" + fid + "&FK_Flow=" + fk_flow + "&WorkID=" + workID;
            //content.Attributes.Add("src", url);
        }
        catch (Exception ee) {
            ee.printStackTrace();
        }
	}
}
