package cn.jflow.model.wf.rpt;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Tools.StringHelper;
import BP.WF.Data.NDXRptBaseAttr;
import BP.WF.Rpt.MapRpt;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.TextBox;

public class S3ColsLabelModel{
	private HttpServletRequest req;
	private HttpServletResponse res;
	public S3ColsLabelModel(){}
	public UiFatory Pub2=null;
	public S3ColsLabelModel(HttpServletRequest request,HttpServletResponse response)
	{
		this.req=request;
		this.res=response;
	}
//	 #region 属性.
	private int Idx;
     public int getIdx() {
    	 String s =req.getParameter("Idx");// this.Request.QueryString["Idx"];
         if (StringHelper.isNullOrEmpty(s))
             s = "0";
         return Integer.parseInt(s);
	}

	public void setIdx(int idx) {
		Idx = idx;
	}

	private String FK_MapAttr;
     public String getFK_MapAttr() {
		return req.getParameter("FK_MapAttr");
	}

	public void setFK_MapAttr(String fK_MapAttr) {
		FK_MapAttr = fK_MapAttr;
	}

	private String RptNo;
     public String getRptNo() {
		return req.getParameter("RptNo");
	}

	public void setRptNo(String rptNo) {
		RptNo = rptNo;
	}

	private String FK_MapData;
     public String getFK_MapData() {
		return req.getParameter("FK_MapData");
	}

	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}

	private String FK_Flow;
     public String getFK_Flow() {
		return req.getParameter("FK_Flow");
	}

	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}


     public void Page_Load()
     {
    	 Pub2=new UiFatory(); 
//         #region 处理活动.
    	 Pub2.append("<div data-options=\"region:'center',title:'3. 设置报表显示列次序',border:false\" style=\"padding: 5px; height: auto\">");
    	 String ActionType=req.getParameter("ActionType")==null?"":req.getParameter("ActionType");
    	 if(ActionType.equals("Left"))
    	 {
    		 MapAttr attr = new MapAttr(this.getFK_MapAttr());
             attr.DoUp();
    	 }
    	 if(ActionType.equals("Right"))
    	 {
    		 MapAttr attr = new MapAttr(this.getFK_MapAttr());
             attr.DoUp();
    	 }
   
    	 MapRpt mrpt = new MapRpt(getRptNo());
         MapAttrs attrs = new MapAttrs(this.getRptNo());

         this.Pub2.append(BaseModel.AddTable("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));
         this.Pub2.append(BaseModel.AddTR());
         this.Pub2.append(BaseModel.AddTDGroupTitle("style='text-align:center;width:50px' class=\"GroupTitle\"", "序"));
         this.Pub2.append(BaseModel.AddTDGroupTitle2("字段"));
         this.Pub2.append(BaseModel.AddTDGroupTitle2("标签"));
         this.Pub2.append(BaseModel.AddTDGroupTitle2("显示顺序号"));
         this.Pub2.append(BaseModel.AddTDGroupTitle("style='text-align:center;width:100px' class=\"GroupTitle\"", "调整"));
         this.Pub2.append(BaseModel.AddTREnd());

         int idx = 0;
         SimpleDateFormat sdf=new SimpleDateFormat("HHmmss");
         String tKey = sdf.format(new Date());
         for (int i = 0; i < attrs.size(); i++) {
        	 MapAttr mattr=(MapAttr) attrs.get(i);
			 if (mattr.getKeyOfEn() != null || !mattr.getKeyOfEn().equals("")) {
				if (mattr.getKeyOfEn().equals(NDXRptBaseAttr.Title)) {
					continue;
				}
				if (mattr.getKeyOfEn().equals(NDXRptBaseAttr.OID)) {
					continue;
				}
				if (mattr.getKeyOfEn().equals(NDXRptBaseAttr.MyNum)) {
					continue;
				}
			}

             idx++;
             this.Pub2.append(BaseModel.AddTR());
             this.Pub2.append(BaseModel.AddTDIdx(idx));
             this.Pub2.append(BaseModel.AddTD(mattr.getKeyOfEn()));
             TextBox tb = Pub2.creatTextBox("TB_" + mattr.getKeyOfEn());
             tb.setText(mattr.getName());
//             tb.ID = "TB_" + mattr.KeyOfEn;
             this.Pub2.append(BaseModel.AddTD(tb));

             tb = Pub2.creatTextBox("TB_" + mattr.getKeyOfEn() + "_Idx");
             tb.setText(Integer.toString(idx));
             this.Pub2.append(BaseModel.AddTD(tb));

             //顺序.
             this.Pub2.append(BaseModel.AddTDBegin("style='text-align:center'"));
             this.Pub2.append("<a href='javascript:void(0)' onclick='up(this, 3)' class='easyui-linkbutton' data-options=\"iconCls:'icon-up'\"></a>&nbsp;");
             this.Pub2.append("<a href='javascript:void(0)' onclick='down(this, 3)' class='easyui-linkbutton' data-options=\"iconCls:'icon-down'\"></a>");

             this.Pub2.append(BaseModel.AddTDEnd());
             this.Pub2.append(BaseModel.AddTREnd());
		}

         this.Pub2.append(BaseModel.AddTableEnd());
     }


}
