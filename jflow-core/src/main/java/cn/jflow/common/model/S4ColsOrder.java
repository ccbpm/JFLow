package cn.jflow.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;

public class S4ColsOrder extends BaseModel{

	private String FK_Flow;
	
	private String RptNo;
	
	private String FK_MapAttr;
	
	private int Idx;
	
	StringBuffer Pub2=null;
	
	HttpServletRequest request;
	
	HttpServletResponse response;
	
	public S4ColsOrder(HttpServletRequest request, HttpServletResponse response,String FK_Flow,String RptNo,String FK_MapAttr,int Idx) {
		super(request, response);
		this.request=request;
		this.response=response;
		this.FK_Flow=FK_Flow;
		this.FK_MapAttr=FK_MapAttr;
		this.Idx=Idx;
		this.RptNo=RptNo;
		Pub2=new StringBuffer();
	}
	
	public void init(){
		
//		String ActionType=request.getParameter("ActionType");
//		
//		if(ActionType!=null && !ActionType.equals("")){
//			if(ActionType.equals("Left")){
//				MapAttr attr = new MapAttr(this.FK_MapAttr);
//				attr.DoUp();
//			}
//			else if(ActionType.equals("Right")){
//				MapAttr attrR = new MapAttr(this.FK_MapAttr);
//				attrR.DoDown();
//			}
//			else{
//
//			}
//		}

        MapAttrs attrs = new MapAttrs(this.RptNo);
        this.Pub2.append(AddTable("align=left"));
        this.Pub2.append(AddCaptionLeft("列表字段显示顺序- 移动箭头改变顺序"));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDTitle("序"));

        int idx = -1;    
        SimpleDateFormat sdf=new SimpleDateFormat("HHmmss");
        String tKey=sdf.format(new Date());
        for (MapAttr attr: attrs.ToJavaList())
        {
            idx++;
            this.Pub2.append("<TD class=Title>");
            if (idx != 0)
                this.Pub2.append("<a href=\"javascript:DoLeft('" + FK_Flow + "','" + this.RptNo + "','" + attr.getMyPK() + "','" + tKey + "')\" ><img src='/WF/Img/Arr/Arrowhead_Previous_S.gif' ></a>");

            this.Pub2.append(attr.getName());

            this.Pub2.append("<a href=\"javascript:DoRight('" + FK_Flow + "','" + this.RptNo + "','" + attr.getMyPK() + "','" + tKey + "')\" ><img src='/WF/Img/Arr/Arrowhead_Next_S.gif' ></a>");

            this.Pub2.append("</TD>");
        }
        this.Pub2.append(AddTREnd());


        for (int i = 0; i < 12; i++)
        {
            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDIdx(i));

            for (MapAttr attr: attrs.ToJavaList()){
                this.Pub2.append(AddTD());
            }
            this.Pub2.append(AddTREnd());
        }
        this.Pub2.append(AddTableEnd());
	}

}
