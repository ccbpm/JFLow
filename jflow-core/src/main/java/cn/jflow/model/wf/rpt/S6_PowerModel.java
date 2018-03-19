package cn.jflow.model.wf.rpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class S6_PowerModel {
	private HttpServletRequest req;
	private HttpServletResponse res;
	public S6_PowerModel(){}
	public S6_PowerModel(HttpServletRequest request,HttpServletResponse response){
		this.req=request;
		this.res=response;
	}
//	 #region 属性.
	private String FK_Flow;
     public String getFK_Flow() {
		return req.getParameter("FK_Flow");
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
//	public string FK_Flow
//     {
//         get
//         {
//             return this.Request.QueryString["FK_Flow"];
//
//         }
//     }
	private String RptNo;
	
     public String getRptNo() {
		return req.getParameter("RptNo");
	}
	public void setRptNo(String rptNo) {
		RptNo = rptNo;
	}
//	public string RptNo
//     {
//         get
//         {
//             return this.Request.QueryString["RptNo"];
//
//         }
//     }
	private String FK_MapData;
     public String getFK_MapData() {
		return req.getParameter("FK_MapData");
	}
	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}
//	public string FK_MapData
//     {
//         get
//         {
//             return this.Request.QueryString["FK_MapData"];
//
//         }
//     }
}
