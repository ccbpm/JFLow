package cn.jflow.common.model;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Glo;

public class TaskPoolApplyModel extends BaseModel {

	private ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	
	private DataTable dt = null;
	private String timeKey;
	private String pageSmall = null;
	
	public TaskPoolApplyModel(HttpServletRequest request,
			HttpServletResponse response){
		super(request, response);
		this.request.set(request);
		this.response.set(response);
	}
	
	public String init(){
		timeKey = DataType.dateToStr(new Date(), "yyyyMMddHHmmss");
//		FK_Flow = getRequest().getParameter("FK_Flow");
		dt = Dev2Interface.DB_TaskPoolOfMyApply();
		return BindList();
	}
	
	private String BindList(){
		StringBuffer rtnHtml = new StringBuffer();
		String GroupBy = getGroupBy();
		if("PRI".equals(GroupBy)){
			return BindList_PRI();
		}
		String appPath = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort()	+ getRequest().getContextPath() + "/";
		boolean isPRI = Glo.getIsEnablePRI();
        String groupVals = "";
        for(int drNum=0;drNum<dt.Rows.size();drNum++){
        	DataRow dr = dt.Rows.get(drNum);
        	if (groupVals.contains("@" + dr.get(GroupBy.toLowerCase()).toString() + ","))
                continue;
            groupVals += "@" + dr.get(GroupBy.toLowerCase()).toString() + ",";
        }

        int colspan = 11;
       
        //rtnHtml.append(AddTable("align='left'"));
        rtnHtml.append(AddCaption("<a href='TaskPoolSharing.jsp' >任务池</a>-申请下来工作"));

        String extStr = "";
        if (getIsHungUp()){
            extStr = "&IsHungUp=1";
        }

        rtnHtml.append(AddTR());
        rtnHtml.append(AddTDTitle("ID"));
        rtnHtml.append(AddTDTitle("标题"));

        if (!"FlowName".equals(GroupBy)){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=FlowName" + extStr + "&T=" + timeKey + "' >流程</a>"));
        }
        if (!"NodeName".equals(GroupBy)){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=NodeName" + extStr + "&T=" + timeKey + "' >节点</a>"));
        }
        if (!"StarterName".equals(GroupBy)){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=StarterName" + extStr + "&T=" + timeKey + "' >发起人</a>"));
        }

        if (isPRI){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=PRI" + extStr + "&T=" + timeKey + "' >优先级</a>"));
        }

        rtnHtml.append(AddTDTitle("发起日期"));
        rtnHtml.append(AddTDTitle("接受日期"));
        rtnHtml.append(AddTDTitle("期限"));
        rtnHtml.append(AddTDTitle("状态"));
        rtnHtml.append(AddTDTitle("备注"));
        rtnHtml.append(AddTDTitle("操作"));
        rtnHtml.append(AddTREnd());

        int i = 0;
        Date cdt = new Date();
        String[] gVals = groupVals.split("@");
        int gIdx = 0;
        for(String gVal : gVals) {
            if (StringHelper.isNullOrEmpty(gVal)){
                continue;
            }

            gIdx++;

            rtnHtml.append(AddTR());
            if ("Rec".equals(GroupBy)){
            	rtnHtml.append(AddTD("colspan='" + colspan + "' onclick=\"GroupBarClick('" + appPath + "','" + gIdx + "')\" ", "<div style='text-align:left; float:left' ><img src='"+appPath+"WF/Img/Min.gif' alert='Min' id='Img" + gIdx + "' border='0' />&nbsp;<b>" + gVal.replace(",", "") + "</b>"));
            }else{
            	rtnHtml.append(AddTD("colspan='" + colspan + "' onclick=\"GroupBarClick('" + appPath + "','" + gIdx + "')\" ", "<div style='text-align:left; float:left' ><img src='"+appPath+"WF/Img/Min.gif' alert='Min' id='Img" + gIdx + "' border='0' />&nbsp;<b>" + gVal.replace(",", "") + "</b>"));
            }
            rtnHtml.append(AddTREnd());

            for(int drNum=0;drNum<dt.Rows.size();drNum++){
            	DataRow dr = dt.Rows.get(drNum);
            	String FK_Flow = dr.get("FK_Flow".toLowerCase()).toString();
            	String FK_Node = dr.get("FK_Node".toLowerCase()).toString();
            	String FID = dr.get("FID".toLowerCase()).toString();
            	String WorkID = dr.get("WorkID".toLowerCase()).toString();
            	String Title = dr.get("Title".toLowerCase()).toString();
            	if(!gVal.equals(dr.get(GroupBy.toLowerCase()).toString() + ",")){
            		continue;
            	}
            	String sdt = dr.get("SDT".toLowerCase()).toString();
            	rtnHtml.append(AddTR("ID='" + gIdx + "_" + i + "'"));
            	i++;
            	
            	int isRead = Integer.parseInt(dr.get("IsRead".toLowerCase()).toString());
            	rtnHtml.append(AddTDIdx(i));
            	
            	if(Glo.getIsWinOpenEmpWorks()){
            		if (isRead == 0){
            			rtnHtml.append(AddTD("onclick=\"SetImg('" + appPath + "','I" + gIdx + "_" + i + "')\"", "<a href=\"javascript:WinOpenIt('" + appPath + "WF/MyFlow.htm?FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FID=" + FID + "&WorkID=" + WorkID + "&IsRead=0&T=" + timeKey + "','" + WorkID + "');\" ><img class=Icon src='"+appPath+"WF/Img/Mail_UnRead.png' id='I" + gIdx + "_" + i + "' />" + Title + "</a>"));
            		}else{
            			rtnHtml.append(AddTD("<a href=\"javascript:WinOpenIt('"+appPath+"WF/MyFlow.htm?FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FID=" + FID + "&WorkID=" + WorkID + "','" + WorkID + "');\"  ><img src='"+appPath+"WF/Img/Mail_Read.png' id='I" + gIdx + "_" + i + "' class=Icon />" + Title + "</a>"));
            		}
            	}else{
            		if (isRead == 0){
            			rtnHtml.append(AddTD("onclick=\"SetImg('" + appPath + "','I" + gIdx + "_" + i + "')\" ", "<a href=\"MyFlow" + getPageSmall() + ".jsp?FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FID=" + FID + "&WorkID=" + WorkID + "&IsRead=0&T=" + timeKey + "\" ><img class=Icon src='"+appPath+"WF/Img/Mail_UnRead.png' id='I" + gIdx + "_" + i + "' />" + Title + "</a>"));
            		}else{
            			rtnHtml.append(AddTD("<a href=\"MyFlow" + getPageSmall() + ".jsp?FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FID=" + FID + "&WorkID=" + WorkID + "&T=" + timeKey + "\" ><img class=Icon src='"+appPath+"WF/Img/Mail_Read.png' id='I" + gIdx + "_" + i + "' />" + Title + "</a>"));
            		}
            	}
            	if (!"FlowName".equals(GroupBy)){
                	rtnHtml.append(AddTD(dr.get("FlowName".toLowerCase()).toString()));
                }
                if (!"NodeName".equals(GroupBy)){
                	rtnHtml.append(AddTD(dr.get("NodeName".toLowerCase()).toString()));
                }
                if (!"StarterName".equals(GroupBy)){
                	rtnHtml.append(AddTD(dr.get("Starter".toLowerCase()).toString() + " " + dr.get("StarterName".toLowerCase())));
                }
                if (isPRI){
                	rtnHtml.append(AddTD("<img class='Icon' src='"+appPath+"WF/Img/PRI/" + dr.get("PRI".toLowerCase()).toString() + ".png' class='Icon' />"));
                }
                rtnHtml.append(AddTD(dr.get("RDT".toLowerCase()).toString().substring(5)));
                rtnHtml.append(AddTD(dr.get("ADT".toLowerCase()).toString().substring(5)));
                rtnHtml.append(AddTD(dr.get("SDT".toLowerCase()).toString().substring(5)));
	
	            Date mysdt = DataType.ParseSysDate2DateTime(sdt);
	            if(mysdt.before(cdt) || mysdt.equals(cdt)){
                	if (DataType.dateToStr(cdt, "yyyy-MM-dd").equals(DataType.dateToStr(mysdt, "yyyy-MM-dd"))){
                		rtnHtml.append(AddTDCenter("正常"));
                	}else{
                    	rtnHtml.append(AddTDCenter("<font color='red'>逾期</font>"));
                	}
                }else{
                	rtnHtml.append(AddTDCenter("正常"));
                }
	            Object flowNode = dr.get("FlowNote".toLowerCase());
	            rtnHtml.append(AddTD(flowNode!=null?flowNode.toString():""));
	            rtnHtml.append(AddTD("<a href=\"javascript:PutOne('" + WorkID + "')\" >放入</a>"));
	            rtnHtml.append(AddTREnd());
            }
        }
        //rtnHtml.append(AddTableEnd());
		return rtnHtml.toString();
	}
	
	private String BindList_PRI(){
		StringBuffer rtnHtml = new StringBuffer();
		String appPath = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort()	+ getRequest().getContextPath() + "/";
		int colspan = 12;
		if(!"".equals(getPageSmall())){
			rtnHtml.append(AddBR());
		}
		String extStr = "";
        if (getIsHungUp()){
            extStr = "&IsHungUp=1";
        }
        //rtnHtml.append(AddTable("align='left'"));
        rtnHtml.append(AddCaption("<a href='TaskPoolSharing.jsp' >任务池</a>-申请下来工作"));
        rtnHtml.append(AddTR());
        rtnHtml.append(AddTDTitle("ID"));
        rtnHtml.append(AddTDTitle("标题"));
        
        String GroupBy = getGroupBy();

        if (!"FlowName".equals(GroupBy)){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=FlowName" + extStr + "&T=" + timeKey + "' >流程</a>"));
        }

        if (!"NodeName".equals(GroupBy)){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=NodeName" + extStr + "&T=" + timeKey + "' >节点</a>"));
        }
        
        if (!"StarterName".equals(GroupBy)){
        	rtnHtml.append(AddTDTitle("<a href='" + getPageID() + ".jsp?GroupBy=StarterName" + extStr + "&T=" + timeKey + "' >发起人</a>"));
        }
        
        rtnHtml.append(AddTDTitle("发起日期"));
        rtnHtml.append(AddTDTitle("接受日期"));
        rtnHtml.append(AddTDTitle("期限"));
        rtnHtml.append(AddTDTitle("状态"));
        rtnHtml.append(AddTDTitle("备注"));
        rtnHtml.append(AddTDTitle("操作"));
        rtnHtml.append(AddTREnd());
        
        int i = 0;
        Date cdt = new Date();
        int gIdx = 0;
        SysEnums ses = new SysEnums("PRI");
        for(Object obj : ses.toArray()) {
        	SysEnum se = (SysEnum) obj;
            gIdx++;
            rtnHtml.append(AddTR());
            rtnHtml.append(AddTD("colspan='" + colspan + "' onclick=\"GroupBarClick('" + appPath + "','" + gIdx + "')\" ", "<div style='text-align:left; float:left' ><img src='"+appPath+"WF/Style/Min.gif' alert='Min' id='Img" + gIdx + "' />&nbsp;<img src='"+appPath+"WF/Img/PRI/" + se.getIntKey() + ".png'  class='Icon' />"));
            rtnHtml.append(AddTREnd());

            String pri = String.valueOf(se.getIntKey());
            for(int drNum=0;drNum<dt.Rows.size();drNum++){
            	DataRow dr = dt.Rows.get(drNum);
            	if (!pri.equals(dr.get("PRI".toLowerCase()).toString())){
                    continue;
            	}
            	String sdt = dr.get("SDT".toLowerCase()).toString();
            	rtnHtml.append(AddTR("ID='" + gIdx + "_" + i + "'"));
            	i++;
            	rtnHtml.append(AddTDIdx(i));
            	if(Glo.getIsWinOpenEmpWorks()){
            		rtnHtml.append(AddTD("<a href=\"javascript:WinOpenIt('" + appPath + "WF/MyFlow.htm?FK_Flow=" + dr.get("fk_flow") + "&FK_Node=" + dr.get("fk_node") + "&FID=" + dr.get("fid") + "&WorkID=" + dr.get("workid") + "&T=" + timeKey + "','" + dr.get("workid") + "');\"  >" + dr.get("title").toString() + "</a>"));
            	}else{
            		rtnHtml.append(AddTD("<a href=\"MyFlow" + getPageSmall() + ".jsp?FK_Flow=" + dr.get("fk_flow") + "&FK_Node=" + dr.get("fk_node") + "&FID=" + dr.get("fid") + "&WorkID=" + dr.get("workid") + "&T=" + timeKey + "\" >" + dr.get("title").toString() + "</a>"));
            	}
            	if (!"FlowName".equals(GroupBy)){
                	rtnHtml.append(AddTD(dr.get("flowname").toString()));
                }

                if (!"NodeName".equals(GroupBy)){
                	rtnHtml.append(AddTD(dr.get("nodename").toString()));
                }
                
                if (!"StarterName".equals(GroupBy)){
                	rtnHtml.append(AddTD(dr.get("starter").toString() + " " + dr.get("startername")));
                }
                
                rtnHtml.append(AddTD(dr.get("rdt").toString().substring(5)));
                rtnHtml.append(AddTD(dr.get("adt").toString().substring(5)));
                rtnHtml.append(AddTD(dr.get("sdt").toString().substring(5)));
                
                Date mysdt = DataType.ParseSysDate2DateTime(sdt);
                if(mysdt.before(cdt) || mysdt.equals(cdt)){
                	if (DataType.dateToStr(cdt, "yyyy-MM-dd").equals(DataType.dateToStr(mysdt, "yyyy-MM-dd"))){
                		rtnHtml.append(AddTDCenter("正常"));
                	}else{
                    	rtnHtml.append(AddTDCenter("<font color='red'>逾期</font>"));
                	}
                }else{
                	rtnHtml.append(AddTDCenter("正常"));
                }
                rtnHtml.append(AddTD(dr.get("flownote").toString()));
                rtnHtml.append(AddTD("<a href=\"javascript:PutOne('" + dr.get("workid").toString() + "')\" >放入</a>"));
                rtnHtml.append(AddTREnd());
            }	
        }
        //rtnHtml.append(AddTableEnd());
		return rtnHtml.toString();
	}
	
	public HttpServletRequest getRequest(){
		return request.get();
	}
	
	public HttpServletResponse getResponse(){
		return response.get();
	}

	public String getGroupBy() {
		String groupBy = getRequest().getParameter("GroupBy");
		if(groupBy == null){
			String doType = getRequest().getParameter("DoType");
			if("CC".equals(doType)){
				groupBy = "Rec";
			}else{
				groupBy = "FlowName";
			}
		}
		return groupBy;
	}

	public boolean getIsHungUp() {
		String isHungUp = getRequest().getParameter("IsHungUp");
		if(isHungUp == null){
			return false;
		}else{
			return true;
		}
	}

	public String getPageSmall() {
		if (pageSmall == null)
        {
            if (getPageID().toLowerCase().contains("smallsingle"))
            	pageSmall = "SmallSingle";
            else if (getPageID().toLowerCase().contains("small"))
            	pageSmall = "Small";
            else
            	pageSmall = "";
        }
        return pageSmall;
	}

}
