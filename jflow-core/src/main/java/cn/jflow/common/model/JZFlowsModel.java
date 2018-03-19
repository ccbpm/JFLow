package cn.jflow.common.model;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.Flows;
import BP.WF.Glo;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.Web.WebUser;
import cn.jflow.common.util.BigDecimalUtil;
import cn.jflow.system.ui.UiFatory;


public class JZFlowsModel {

	private String basePath;
	
	private HttpServletRequest _request = null;
	
	public JZFlowsModel(String basePath, HttpServletRequest _request){
		this.basePath = basePath;
		this._request = _request;
	}
	
	public UiFatory ui = null;
	public void init() {
		this.ui = new UiFatory();
		
		Flows fls = new Flows();
        fls.RetrieveAll();
        
        FlowSorts ens = new FlowSorts();
        ens.RetrieveAll();
        
        DataTable dt = Dev2Interface.DB_GenerCanStartFlowsOfDataTable(WebUser.getNo());
        
        int cols = 3; //定义显示列数 从0开始。
        double widthCell = BigDecimalUtil.div(100, cols, 2);
        this.ui.append(BaseModel.AddTable());
        this.ui.append(BaseModel.AddCaption("发起流程-(说明:不能点的流程是您没有发起的权限.)"));
        int idx = -1;
        boolean is1 = false;
        
        String timeKey = "s" + this._request.getSession().getId() + DataType.dateToStr(new Date(), "yyMMddHHmmss");
        for (FlowSort en : ens.ToJavaListFs()){
        	
//        	 if(!"线性流程".equals(en.getName()))
//             	 continue;
        	 idx++;
             if (idx == 0){
            	  //this.ui.append(BaseModel.AddTR(is1));
            	  this.ui.append(BaseModel.AddTR());
	              is1 = !is1;
             }
             
             this.ui.append(BaseModel.AddTDBegin("width='" + widthCell + "%' border=0 valign=top"));
             //输出类别.
             //this.AddFieldSet(en.Name);
             this.ui.append(BaseModel.AddB(en.getName()));
             this.ui.append(BaseModel.AddUL());
             
             //#region 输出流程。
             for (Flow fl : fls.ToJavaList()){
            	 if (fl.getFlowAppType() == FlowAppType.DocFlow)
                     continue;
            	 
            	 if (!(fl.getFK_FlowSort()).equals(en.getNo()))
                     continue;
            	 
//            	 if(!"124".equals(fl.getNo()) && !"128".equals(fl.getNo()))
//            		 continue;

                 boolean isHaveIt = false;
                 if(dt != null)
                 {
                 for(DataRow dr : dt.Rows){
                     if (!(fl.getNo()).equals(dr.getValue("No").toString()))
                         continue;
                     
                     isHaveIt = true;
                     break;
                 }
                 }
                 
                String extUrl = "";
                 if (fl.getIsBatchStart()){
                     extUrl = "<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/BatchStart.jsp?FK_Flow="+fl.getNo()+"','批量发起');\" >批量发起</a>|<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/Rpt/Search.jsp?RptNo=ND" + Integer.parseInt(fl.getNo()) + "MyRpt&FK_Flow=" + fl.getNo() + "','查询')\">查询</a>|<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/WorkOpt/OneWork/OneWork.htm?FK_Flow=" + fl.getNo() + "&DoType=Chart&T=" + timeKey + "','流程图');\"  >图</a>";
                 }else{
                	 extUrl = "<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/Rpt/Search.jsp?RptNo=ND" + Integer.parseInt(fl.getNo()) + "MyRpt&FK_Flow=" + fl.getNo() + "','查询');\">查询</a>|<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/WorkOpt/OneWork/OneWork.htm?FK_Flow=" + fl.getNo() + "&DoType=Chart&T=" + timeKey + "','流程图');\" >图</a>";
                 }
                     
                 if (isHaveIt){
                     if (Glo.getIsWinOpenStartWork() == 1)
                    	 this.ui.append(BaseModel.AddLi("<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/MyFlow.htm?FK_Flow=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01&T=" + timeKey + "','" + fl.getName() + "');\" >" + fl.getName() + "</a> - " + extUrl));
                     else if (Glo.getIsWinOpenStartWork() == 2)
                    	 this.ui.append(BaseModel.AddLi("<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/OneFlow/MyFlow.htm?FK_Flow=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01&T=" + timeKey + "','" + fl.getName() + "');\" >" + fl.getName() + "</a> - " + extUrl));
                     else
                    	 this.ui.append(BaseModel.AddLi("<a  class='btnList' href=\"javascript:winOpen('"+basePath+"/WF/MyFlow.htm?FK_Flow=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01','" + fl.getName() + "')\" >" + fl.getName() + "</a> - " + extUrl));
                 }else{
                	 this.ui.append(BaseModel.AddLi(fl.getName() + " - " + extUrl));
                 }
             }
             //#endregion 输出流程
             this.ui.append(BaseModel.AddULEnd());
             // this.AddFieldSetEnd();

             this.ui.append(BaseModel.AddTDEnd());
             if (idx == cols - 1){
                  idx = -1;
                  this.ui.append(BaseModel.AddTREnd());
             }
        }
        
        while (idx != -1){
            idx++;
            if (idx == cols - 1){
                idx = -1;
                this.ui.append(BaseModel.AddTD());
                this.ui.append(BaseModel.AddTREnd());
            }else{
            	this.ui.append(BaseModel.AddTD());
            }
        }
        this.ui.append(BaseModel.AddTableEnd());
	}
	
	public String getBasePath() {
		return basePath;
	}
	
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
//	private String WinOpen(String url){
//        return this.WinOpen(url, "", "msg", 900, 500, 0, 0);
//    }
//	private String WinOpen(String url, String title, String winName, int width, int height, int top, int left){
//        url = url.replace("<", "[");
//        url = url.replace(">", "]");
//        url = url.trim();
//        title = title.replace("<", "[");
//        title = title.replace(">", "]");
//        title = title.replace("\"", "‘");
//        return "<script type=='text/javascript'> var newWindow = window.open(' " + url + "','" + winName + "','width=" + width + ",top=" + top + ",left=" + left + ",height=" + height + ",scrollbars=yes,resizable=yes') ; newWindow.focus(); </script> ";
//    }
}
