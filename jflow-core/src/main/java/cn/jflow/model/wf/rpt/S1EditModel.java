package cn.jflow.model.wf.rpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class S1EditModel{

	private HttpServletRequest req;
	private HttpServletResponse res;
	public S1EditModel()
	{
		
	}
	public S1EditModel(HttpServletRequest request,HttpServletResponse response)
	{
		this.req=request;
		this.res=response;
	}
	//#region 属性.
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
    // #endregion 属性.

//     public void Page_Load()
//     {
//         MapRpt rpt = new MapRpt();
//         if (RptNo != null)
//         {
//             /**/
//             rpt.setNo(RptNo);
//             rpt.RetrieveFromDBSources();
//         }
//
//         
//         TB_No.Text = rpt.No;
//         this.TB_Name.Text = rpt.Name;
//         this.TB_Note.Text = rpt.Note;
//
//         if (StringHelper.isNullOrEmpty(rpt.getNo()) == false)
//             this.TB_No.Enabled = false;
//     }

//     protected void Btn_Save_Click(object sender, EventArgs e)
//     {
//         Save();
//
//         Response.Redirect(string.Format("S1_Edit.aspx?FK_MapData={0}&FK_Flow={1}&RptNo={2}", FK_MapData,
//                                         FK_Flow, RptNo), true);
//     }
//
//     protected void Btn_Cancel_Click(object sender, EventArgs e)
//     {
//         this.WinClose();
//     }
//
//     private void Save()
//     {
//         MapRpt rpt = new MapRpt();
//         rpt = (MapRpt) BP.Sys.PubClass.copyFromRequest(rpt, req);
//         if (RptNo != null)
//             rpt.setNo(RptNo);
//
//         rpt.setParentMapData(FK_MapData);
//
//         Flow fl = new Flow(FK_Flow);
//         rpt.setPTable(fl.getPTable());
//
//         if (StringHelper.isNullOrWhiteSpace(this.RptNo))
//         {
//             if (rpt.getNo()==RptNo)
//             {
//                 BP.Sys.PubClass.Alert("@该编号已经存在:" + rpt.getNo(), res);
//                 return;
//             }
//
//             rpt.Insert();
//         }
//         else
//         {
//             rpt.Update();
//         }
//     }
//
//     protected void Btn_SaveAndNext1_Click(object sender, EventArgs e)
//     {
//         Save();
//
//         Response.Redirect(string.Format("S2_ColsChose.aspx?FK_MapData={0}&FK_Flow={1}&RptNo={2}", FK_MapData,
//                                         FK_Flow, RptNo), true);
//     }
}
