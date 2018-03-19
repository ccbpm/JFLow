package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Tools.StringHelper;
import BP.WF.Rpt.MapRpt;


public class S1EditModel extends BaseModel{

	private String FK_Flow;
	
	private String RptNo;
	
	private String FK_MapData;
	
	public S1EditModel(HttpServletRequest request, HttpServletResponse response,String FK_Flow,String RptNo,String FK_MapData) {
		super(request, response);
		this.FK_Flow=FK_Flow;
		this.RptNo=RptNo;
		this.FK_MapData=FK_MapData;
	}

	public void init(){
		
		MapRpt rpt = new MapRpt();
        if (this.RptNo != null)
        {
            /**/
            rpt.setNo(this.RptNo);
            rpt.RetrieveFromDBSources();
        }

//        this.TB_No = rpt.No;
//        this.TB_Name.Text = rpt.Name;
//        this.TB_Note.Text = rpt.Note;

        if (StringHelper.isNullOrEmpty(rpt.getNo()) == false){
//            this.TB_No.Enabled = false;
        }
	}
	
	

//    protected void Btn_Save_Click(object sender, EventArgs e)
//    {
//        Save();
//
//        Response.Redirect(string.Format("S1_Edit.aspx?FK_MapData={0}&FK_Flow={1}&RptNo={2}", FK_MapData,
//                                        FK_Flow, RptNo), true);
//    }

//    protected void Btn_Cancel_Click(object sender, EventArgs e)
//    {
//        this.WinClose();
//    }

    private void Save()
    {
//        MapRpt rpt = new MapRpt();
//        rpt = BP.Sys.PubClass.CopyFromRequest(rpt) as MapRpt;
//        if (this.RptNo != null)
//            rpt.No = this.RptNo;
//
//        rpt.ParentMapData = this.FK_MapData;
//
//        Flow fl = new Flow(this.FK_Flow);
//        rpt.PTable = fl.PTable;
//
//        if (StringHelper.isNullOrWhiteSpace(this.RptNo))
//        {
//            if (rpt.IsExits)
//            {
//                BP.Sys.PubClass.Alert("@该编号已经存在:" + rpt.No);
//                return;
//            }
//
//            rpt.Insert();
//        }
//        else
//        {
//            rpt.Update();
//        }
    }

//    protected void Btn_SaveAndNext1_Click(object sender, EventArgs e)
//    {
//        Save();
//
//        Response.Redirect(string.Format("S2_ColsChose.aspx?FK_MapData={0}&FK_Flow={1}&RptNo={2}", FK_MapData,
//                                        FK_Flow, RptNo), true);
//    }
}
