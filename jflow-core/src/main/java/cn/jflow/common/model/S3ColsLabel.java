package cn.jflow.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.WF.Rpt.MapRpt;
import cn.jflow.system.ui.core.TextBox;

public class S3ColsLabel extends BaseModel{

	private int Idx;
	
	private String FK_Flow;
	
	private String FK_MapAttr;
	
	private String RptNo;
	
	private String FK_MapData;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	StringBuffer Pub2=null;
	
	public S3ColsLabel(HttpServletRequest request, HttpServletResponse response,int Idx,String FK_MapAttr,String RptNo,String FK_MapData,String FK_Flow) {
		super(request, response);
        this.Idx=Idx;
        this.FK_Flow=FK_Flow;
        this.FK_MapAttr=FK_MapAttr;
        this.FK_MapData=FK_MapData;
        this.RptNo=RptNo;
        Pub2=new StringBuffer();
	}

	public void init(){
//		String ActionType=request.getParameter("ActionType");
//		if(ActionType!=null && !ActionType.equals("")){
//			if(ActionType.equals("Left")){
//				 MapAttr attr = new MapAttr(this.FK_MapAttr);
//	             attr.DoUp();
//			}
//			else if(ActionType.equals("Right")){
//				MapAttr attrR = new MapAttr(this.FK_MapAttr);
//	            attrR.DoDown();
//			}
//			else{
//				
//			}
//		}
       


        MapRpt mrpt = new MapRpt(RptNo, this.FK_Flow);
        MapAttrs attrs = new MapAttrs(this.RptNo);

        this.Pub2.append(AddTable("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center;width:50px'", "序"));
        this.Pub2.append(AddTDGroupTitle("字段"));
        this.Pub2.append(AddTDGroupTitle("标签"));
        this.Pub2.append(AddTDGroupTitle("显示顺序号"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center;width:100px'", "调整"));
        this.Pub2.append(AddTREnd());

        int idx = 0;
        SimpleDateFormat sdf=new SimpleDateFormat("HHmmss");
        String tKey=sdf.format(new Date());
        
        for (MapAttr mattr: attrs.ToJavaList())
        {
        	if(mattr.getKeyOfEn().equals(BP.WF.Data.NDXRptBaseAttr.Title)){
        		
        	}
        	else if(mattr.getKeyOfEn().equals(BP.WF.Data.NDXRptBaseAttr.OID)){
        		
        	}
        	else if(mattr.getKeyOfEn().equals(BP.WF.Data.NDXRptBaseAttr.MyNum)){
        		continue;
        	}
        	else{
        		break;
        	}
          

            idx++;
            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDIdx(idx));
            this.Pub2.append(AddTD(mattr.getKeyOfEn()));
            TextBox tb = new TextBox();
            tb.setText(mattr.getName());
            tb.setId("TB_" + mattr.getKeyOfEn());
            this.Pub2.append(AddTD(tb));

            tb = new TextBox();
            tb.setId("TB_" + mattr.getKeyOfEn() + "_Idx");;
            tb.setText(""+idx);
            //tb.ReadOnly = true;
            this.Pub2.append(AddTD(tb));

            //顺序.
            this.Pub2.append(AddTDBegin("style='text-align:center'"));
            this.Pub2.append("<a href='javascript:void(0)' onclick='up(this, 3)' class='easyui-linkbutton' data-options=\"iconCls:'icon-up'\"></a>&nbsp;");
            this.Pub2.append("<a href='javascript:void(0)' onclick='down(this, 3)' class='easyui-linkbutton' data-options=\"iconCls:'icon-down'\"></a>");

            this.Pub2.append(AddTDEnd());
            this.Pub2.append(AddTREnd());
        }

        this.Pub2.append(AddTableEnd());
	}
	
//    protected void Btn_Save_Click(object sender, EventArgs e)
//    {
//        Save();
//
//        this.Response.Redirect("S3_ColsLabel.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//    }
//
//    protected void Btn_Cancel_Click(object sender, EventArgs e)
//    {
//        this.WinClose();
//    }
//
//    private void Save()
//    {
//        MapAttrs attrs = new MapAttrs(this.RptNo);
//        foreach (MapAttr item in attrs)
//        {
//            switch (item.KeyOfEn)
//            {
//                case BP.WF.Data.NDXRptBaseAttr.Title:
//                case BP.WF.Data.NDXRptBaseAttr.OID:
//                case BP.WF.Data.NDXRptBaseAttr.MyNum:
//                    continue;
//                default:
//                    break;
//            }
//
//            TextBox tb = this.Pub2.GetTextBoxByID("TB_" + item.KeyOfEn);
//            item.Name = tb.Text;
//
//            tb = this.Pub2.GetTextBoxByID("TB_" + item.KeyOfEn + "_Idx");
//            item.IDX = int.Parse(tb.Text);
//
//            item.Update();
//        }
//    }
//
//    protected void Btn_SaveAndNext1_Click(object sender, EventArgs e)
//    {
//        Save();
//
//        this.Response.Redirect("S5_SearchCond.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//    }
}
