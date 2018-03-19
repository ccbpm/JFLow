package BP.WF;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.MapAttr;
import cn.jflow.common.model.BaseModel;



public class TabIdx extends BaseModel {
	public StringBuffer Pub1 = null;
	public TabIdx(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub1 = new StringBuffer();
	}
		
	public void Page_Load(HttpServletRequest request,
	HttpServletResponse response)
    {
		if(("Up").equals(this.getDoType())) {
			MapAttr ma = new MapAttr(this.getFK_MapData() + "_" + request.getParameter("KeyOfEn"));
            ma.DoUpTabIdx();
		}else if(("Down").equals(this.getDoType())) {
			MapAttr ma1 = new MapAttr(this.getFK_MapData() + "_" + request.getParameter("KeyOfEn"));
            ma1.DoDownTabIdx();
		}

        String sql = "SELECT KeyOfEn,Name,IDX FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "' AND UIVisible=1  ORDER BY Idx ";
//        this.Pub1.append(Title = "设置tab键顺序");
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        this.Pub1.append(AddTable("<table width='100%'"));
        this.Pub1.append(AddCaption("正确的调整好Tab键的顺序,可以方便用户数据录入."));
        this.Pub1.append(AddTR());
        this.Pub1.append(AddTDTitle("IDX"));
        this.Pub1.append(AddTDTitle("字段"));
        this.Pub1.append(AddTDTitle("描述"));
        this.Pub1.append(AddTDTitle("顺序号"));
        this.Pub1.append(AddTDTitle("移动"));
        this.Pub1.append(AddTDTitle("移动"));
        this.Pub1.append(AddTREnd());

        boolean is1 = false;
        int idx = 0;
        for (DataRow dr : dt.Rows)
        {
            idx++;
            Pub1.append(AddTR(is1));
            this.Pub1.append(AddTDIdx(idx));
            this.Pub1.append(AddTD(dr.get("keyofen").toString()));
            this.Pub1.append(AddTD(dr.get("name").toString()));
            this.Pub1.append(AddTD(dr.get("idx").toString()));
            this.Pub1.append(AddTD("<a href='TabIdx.jsp?DoType=Up&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + dr.get("keyofen").toString() + "' ><img src='"+getBasePath()+"WF/Img/Btn/Up.GIF' border=0></a>"));           																																			
            this.Pub1.append(AddTD("<a href='TabIdx.jsp?DoType=Down&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + dr.get("keyofen").toString() + "' ><img src='"+getBasePath()+"WF/Img/Btn/Down.gif' border=0></a>"));
            this.Pub1.append(AddTREnd());
        }
        this.Pub1.append(AddTableEnd());
    }
	
}
	