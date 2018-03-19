package cn.jflow.common.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
 

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.AttrOfOneVSM;
import BP.En.Attrs;
import BP.En.AttrsOfOneVSM;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.AttachmentUploadType;
import BP.Sys.DtlShowModel;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmBtn;
import BP.Sys.FrmBtns;
import BP.Sys.FrmEle;
import BP.Sys.FrmEleAttr;
import BP.Sys.FrmEleDB;
import BP.Sys.FrmEleDBAttr;
import BP.Sys.FrmEleDBs;
import BP.Sys.FrmEles;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImg;
import BP.Sys.FrmImgAth;
import BP.Sys.FrmImgAthDB;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLab;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLine;
import BP.Sys.FrmLines;
import BP.Sys.FrmLink;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBs;
import BP.Sys.FrmRpt;
import BP.Sys.FrmRpts;
import BP.Sys.FrmType;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.ImgAppType;
import BP.Sys.M2MType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapFrame;
import BP.Sys.MapFrames;
import BP.Sys.MapM2M;
import BP.Sys.MapM2Ms;
import BP.Sys.PicType;
import BP.Sys.SignType;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.Flow;
import BP.WF.FormRunType;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.RunModel;
import BP.WF.Work;
import BP.WF.Template.*;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Entity.FrmWorkCheckSta;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.ReturnWork;
import BP.WF.Entity.ReturnWorkAttr;
import BP.WF.Entity.ReturnWorks;
import BP.WF.Entity.Track;
import BP.WF.Entity.TrackAttr;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CCList;
import BP.WF.Template.CCSta;
import BP.WF.Template.Frm;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmNode;
import BP.WF.Template.Frms;
import BP.WF.Template.BillTemplate;
import BP.Web.WebUser;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;


public class WfrptModel {

	private int FK_Node;
	private String basePath;
	private String FK_Flow;
	private String DoType;
	private int StartNodeID;
	private long WorkID;
	private long CWorkID;
	private int NodeID;
	private int FID;
	private String MyPK;
	private String ViewWork;
	
	private String Width;
	private String Height;
	private String BtnWord;
	
	private String CCID;
	
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	public WfrptModel(String basePath, String FK_Flow, int FK_Node,
			String DoType, int StartNodeID, long WorkID, long CWorkID,
			int NodeID, int FID, String MyPK, String ViewWork, 
			String Width, String Height, String BtnWord, String CCID,
			HttpServletRequest _request, HttpServletResponse _response) {
		this.basePath = basePath;
		this.FK_Flow = FK_Flow;
		this.FK_Node = FK_Node;
		this.DoType = DoType;
		this.StartNodeID = StartNodeID;
		this.WorkID = WorkID;
		this.CWorkID = CWorkID;
		this.NodeID = NodeID;
		this.FID = FID;
		this.MyPK = MyPK;
		this.ViewWork = ViewWork;
		
		this.Width = Width;
		this.Height = Height;
		this.BtnWord = BtnWord;
		
		this.CCID = CCID;
		
		this._request = _request;
		this._response = _response;
	}
	
	public UiFatory ui = null;
	public List<String> scripts;
	public List<String> csslinks;
	public void init() {
		
		this.ui = new UiFatory();
		scripts = new ArrayList<String>();
		csslinks = new ArrayList<String>();
		
		if ("View".equals(this.DoType)){
	        /*如果是查看一个工作的详细情况. */
	        this.BindTrack_ViewWorkForm();
	        return;
        }
		
		if ("ViewSpecialWork".equals(this.DoType)){
            this.BindTrack_ViewSpecialWork();
            return;
        }
		
	   this.ui.append(BaseModel.AddTable());
	   this.ui.append(BaseModel.AddTR());
	   this.ui.append(BaseModel.AddTDTitle("IDX"));
	   this.ui.append(BaseModel.AddTDTitle("日期时间"));
	   this.ui.append(BaseModel.AddTDTitle("从节点"));
	   this.ui.append(BaseModel.AddTDTitle("人员"));
       this.ui.append(BaseModel.AddTDTitle("到节点"));
       this.ui.append(BaseModel.AddTDTitle("人员"));
       this.ui.append(BaseModel.AddTDTitle("活动"));
       this.ui.append(BaseModel.AddTDTitle("信息"));
       this.ui.append(BaseModel.AddTDTitle("表单"));
       this.ui.append(BaseModel.AddTDTitle("执行人"));
       this.ui.append(BaseModel.AddTREnd());
		
       String sqlOfWhere2 = "";
       String sqlOfWhere1 = "";

       String dbStr =SystemConfig.getAppCenterDBVarStr();
       Paras prs = new Paras();
       if (this.FID == 0){
           sqlOfWhere1 = " WHERE (FID=" + dbStr + "WorkID11 OR WorkID=" + dbStr + "WorkID12 )  ";
           prs.Add("WorkID11", this.WorkID);
           prs.Add("WorkID12", this.WorkID);
       }else{
           sqlOfWhere1 = " WHERE (FID=" + dbStr + "FID11 OR WorkID=" + dbStr + "FID12 ) ";
           prs.Add("FID11", this.FID);
           prs.Add("FID12", this.FID);
       }
       
       String sql = "";
      //sunxd 
      //问题:不同类型的数据库执行以下语句所返回的字段名称不一样(oracle 返回的字段名称全部为大写)，导致前端获取时获取不到。
      //解决:为了保证满足所有不同类型的数据库统一，将别名加上“”号
       sql = "SELECT MyPK \"MyPK\",ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",FID \"FID\",WorkID \"WorkID\",NDFrom \"NDFrom\","
       		+ "NDFromT \"NDFromT\",NDTo \"NDTo\",NDToT \"NDToT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\",EmpTo \"EmpTo\",EmpToT \"EmpToT\","
       		+ "RDT \"RDT\",WorkTimeSpan \"WorkTimeSpan\",Msg \"Msg\",NodeData \"NodeData\",Exer \"Exer\" FROM ND" + Integer.parseInt(this.FK_Flow) + "Track " + sqlOfWhere1;
       prs.SQL = sql;
       DataTable dt = DBAccess.RunSQLReturnTable(prs);
       //DataView dv = dt.DefaultView;
       //dv.Sort = "RDT";
       
       int idx = 1;
       for (DataRow dr : dt.Rows){
    	   this.ui.append(BaseModel.AddTR());
    	   this.ui.append(BaseModel.AddTDIdx(idx++));
           Date dtt = DataType.ParseSysDateTime2DateTime(dr.getValue(TrackAttr.RDT).toString());
           
       	   java.text.DateFormat df = new java.text.SimpleDateFormat("MM月dd日HH:mm");
       	   this.ui.append(BaseModel.AddTD(df.format(dtt)));

       	   this.ui.append(BaseModel.AddTD(dr.getValue(TrackAttr.NDFromT).toString()));
       	   this.ui.append(BaseModel.AddTD(dr.getValue(TrackAttr.EmpFromT).toString()));
       	   this.ui.append(BaseModel.AddTD(dr.getValue(TrackAttr.NDToT).toString()));
       	   this.ui.append(BaseModel.AddTD(dr.getValue(TrackAttr.EmpToT).toString()));
           ActionType at = ActionType.forValue(StringUtils.isEmpty(dr.getValue(TrackAttr.ActionType).toString())==true?0:Integer.parseInt(dr.getValue(TrackAttr.ActionType).toString()));
//
           this.ui.append(BaseModel.AddTD("<img src='"+basePath+"WF/Img/Action/" + at.toString() + ".png' class='ActionType' border=0/>" + dr.getValue(TrackAttr.ActionTypeText).toString()));
           this.ui.append(BaseModel.AddTD(DataType.ParseText2Html(String.valueOf(dr.getValue(TrackAttr.Msg)))));
           this.ui.append(BaseModel.AddTD("<a href=\"javascript:WinOpen('" + basePath + "WF/WFRpt.jsp?WorkID=" + dr.getValue(TrackAttr.WorkID).toString() + "&FK_Flow=" + this.FK_Flow + "&DoType=View&MyPK=" + dr.getValue(TrackAttr.MyPK).toString() + "','a3');\">表单</a>"));
           this.ui.append(BaseModel.AddTD(dr.getValue(TrackAttr.Exer).toString()));
           this.ui.append(BaseModel.AddTREnd());
       }
       this.ui.append(BaseModel.AddTableEnd());
       
       if (!"".equals(this.CCID)){
           CCList cl = new CCList();
           cl.setMyPK(this.CCID);
           cl.RetrieveFromDBSources();
           this.ui.append(BaseModel.AddFieldSet(cl.getTitle()));
           this.ui.append("抄送人:" + cl.getRec() + ", 抄送日期:" + cl.getRDT());
           this.ui.append(BaseModel.AddHR());
           this.ui.append(cl.getDocHtml());
           this.ui.append(BaseModel.AddFieldSetEnd());

           if (cl.getHisSta() == CCSta.UnRead){
               cl.setHisSta(CCSta.Read);
               cl.Update();
           }
       }
	}
	
	public void BindTrack_ViewWorkForm(){
		
	    Node nd = null;
        Track tk = new Track();
        tk.FK_Flow = this.FK_Flow;
        tk.setNDFrom(this.FK_Node);
        
        tk.setWorkID(this.WorkID);
        if(StringHelper.isNullOrEmpty(this.MyPK)){
        	 tk = new Track(this.FK_Flow, this.MyPK);
             nd = new Node(tk.getNDFrom());
        }else{
        	 nd = new Node(this.FK_Node);
        }
        
        Flow fl = new Flow(this.FK_Flow);
        long workid = 0;
        if (nd.getHisRunModel() == RunModel.SubThread)
            workid = tk.getFID();
        else
            workid = tk.getWorkID();
        
        long fid = Long.parseLong(String.valueOf(this.FID));
        if(0 == fid)fid = tk.getFID();
        
        DataTable ndrpt = DBAccess.RunSQLReturnTable("SELECT PFlowNo,PWorkID FROM " + fl.getPTable() + " WHERE OID=" + workid);
        
        if (ndrpt.Rows.size() == 0){
            this.ui.append("<div style=\"padding:30px;background:white;\">没有找到表单数据。</div>");
        	return;
        }
        
        String urlExt = "&PFlowNo=" + ndrpt.Rows.get(0).getValue("PFlowNo") + "&PWorkID=" + ndrpt.Rows.get(0).getValue("PWorkID") + "&IsToobar=0&IsHidden=true";
        
        if (nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType()==NodeFormType.SelfForm){
        	try{
		    	if (nd.getFormUrl().contains("?")){
		    		this._response.sendRedirect(nd.getFormUrl() + "&WorkID=" + tk.getWorkID() + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt);
		    	}
//		    	else{
//		    		this._response.sendRedirect(nd.getFormUrl() + "?WorkID=" + tk.getWorkID() + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt); 
//		    	}
		    	/*if (nd.getHisFormType() == NodeFormType.SDKForm){
		    		if (nd.getFormUrl().contains("?"))
                        this._response.sendRedirect(nd.getFormUrl() + "&WorkID=" + tk.getWorkID() + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt);
                    else
                        this._response.sendRedirect(nd.getFormUrl() + "?WorkID=" + tk.getWorkID() + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt);
                    return;
		    	}
		    	*/
		    	this._response.sendRedirect(nd.getFormUrl() + "&WorkID=" + tk.getWorkID() + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt);
	            return;
	        }catch(IOException e){
	        	e.printStackTrace();
	        }
        }
        
        Work wk = nd.getHisWork();
        wk.setOID(tk.getWorkID());
        if (wk.RetrieveFromDBSources() == 0){
        	this.ui.append(BaseModel.AddFieldSet("打开(" + nd.getName() + ")错误"));
        	this.ui.append(BaseModel.AddH1("当前的节点数据已经被删除！！！<br> 造成此问题出现的原因如下。"));
        	this.ui.append(BaseModel.AddBR("1、当前节点数据被非法删除。"));
        	this.ui.append(BaseModel.AddBR("2、节点数据是退回人与被退回人中间的节点，这部分节点数据查看不支持。"));
        	this.ui.append(BaseModel.AddBR("技术信息:表" + wk.getEnMap().getPhysicsTable() + " WorkID=" + this.getWorkID()));
        	this.ui.append(BaseModel.AddFieldSetEnd());
            return;
        }
        
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(wk.getOID());
        if (nd.getHisFlow().getIsMD5() && !wk.IsPassCheckMD5()){
        	this.ui.append(BaseModel.AddFieldSet("打开(" + nd.getName() + ")错误"));
        	this.ui.append(BaseModel.AddH1("当前的节点数据已经被篡改，请报告管理员。"));
        	this.ui.append(BaseModel.AddFieldSetEnd());
            return;
        }
        
        //this.UCEn1.IsReadonly = true;
        Frms frms = nd.getHisFrms();
        if (frms.size() == 0){
        	 if (nd.getHisFormType() == NodeFormType.FreeForm){
        		  MapData map = new MapData("ND" + this.FK_Node);
                  /* 自由表单 */
                  this.Width = map.getMaxRight() + map.getMaxLeft() * 2 + 10 + "";
                  if (Double.parseDouble(Width) < 500)
                      Width = "900";
                  this.Height = map.getMaxEnd()+"";
                  BtnLab btnLab = new BtnLab(this.FK_Node);

                  this.BtnWord = btnLab.getWebOfficeEnable() + "";
                  
                  this.ui.append("<div id='divCCForm' style='width: 100%;height:" + Height + "px' >");
                  this.BindCCForm(wk, "ND" + nd.getNodeID(), true, 0); //, false, false, null);
                  //if (wk.WorkEndInfo.Length > 2)
                  //    this.UCEn1.Add(wk.WorkEndInfo);
                  this.ui.append("</div>");
        	 }
        	 
        	 if (nd.getHisFormType() == NodeFormType.FoolForm){
        		  MapData map = new MapData("ND" + this.FK_Node);
//        		  if (map.getTableWidth().contains("px"))
//                      Width = map.getTableWidth().replace("px", "");
//                  else
//                      Width = map.getTableWidth() + "";
//        		  int labCol = 80;
//        		  int ctrlCol = 260;
//        		  this.Width = String.valueOf((labCol + ctrlCol) * map.getTableCol() / 2);
        		  this.Width = map.getMaxRight() + map.getMaxLeft() * 2 + 10 + "";
        		  if (map.getTableWidth().equals("100%"))
                      Width = "900";
        		  this.ui.append("<div id=divCCForm style='width:" + Width + "px;height:" + map.getFrmH() + "px' >");
        		  /*傻瓜表单*/
        		  this.IsReadonly = true;
        		  this.BindColumn4(wk, "ND" + nd.getNodeID()); //, false, false, null);
                  //if (wk.WorkEndInfo.Length > 2)
                  //    this.UCEn1.Add(wk.WorkEndInfo);
        		  this.ui.append("</div>");
        	  }
        	  
        	   BillTemplates bills = new BillTemplates();
               bills.Retrieve(BillTemplateAttr.NodeID, nd.getNodeID());
               if (bills.size() >= 1){
            	   String title = "";
                   for(BillTemplate item : bills.ToJavaList())
                       title += "<img src='"+basePath+"../Img/Btn/Word.gif' border=0/>" + item.getName() + "</a>";

                   String urlr = basePath + "WF/WorkOpt/PrintDoc.jsp?FK_Node=" + nd.getNodeID() + "&FID=" + fid + "&WorkID=" + tk.getWorkID() + "&FK_Flow=" + nd.getFK_Flow();
                   this.ui.append("<p><a href=\"javascript:WinOpen('" + urlr + "','dsdd');\"  />" + title + "</a></p>");
                   //this.UCEn1.Add("<a href='' target=_blank><img src='../Img/Btn/Word.gif' border=0/>" + bt.Name + "</a>");
               }
        }else{
        	  /* 涉及到多个表单的情况...*/
            if (nd.getHisFormType() == NodeFormType.SheetTree){
            	try{
            		this._response.sendRedirect(basePath +"/WF/FlowFormTree/FlowFormTreeView.jsp?WorkID=" + tk.getWorkID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + "&FK_Node=" + nd.getNodeID() + "&CWorkID=" + this.CWorkID);
            	}catch(IOException e){
    	        	e.printStackTrace();
    	        }
            }else if(nd.getHisFormType() != NodeFormType.DisableIt){
            	
            	Frm myfrm = new Frm();
                myfrm.setNo("ND" + nd.getNodeID());
                myfrm.setName(wk.getEnDesc());
                myfrm.setHisFormRunType(FormRunType.forValue(nd.getHisFormType().getValue()));
                
                FrmNode fnNode = new FrmNode();
                fnNode.setFK_Frm(myfrm.getNo());
                fnNode.setIsEdit(true);
                fnNode.setIsPrint(false);
                switch (nd.getHisFormType()){
                	case FoolForm:
                		fnNode.setHisFrmType(FrmType.FoolForm);
                		break;
                	case FreeForm:
                		 fnNode.setHisFrmType(FrmType.FreeFrm);
                		break;
                	case SelfForm:
                          fnNode.setHisFrmType(FrmType.Url);
                          break;
                    default:
                    	throw new RuntimeException("出现了未判断的异常。");
                }
                myfrm.HisFrmNode = fnNode;
                frms.AddEntity(myfrm, 0);
            }
            if (frms.size() == 1){
            	 /* 如果禁用了节点表单，并且只有一个表单的情况。*/
                Frm frm = (Frm)frms.get(0);
                FrmNode fn = frm.HisFrmNode;
                String src = "";
                src = fn.getFrmUrl() + ".jsp?FK_MapData=" + frm.getNo() + "&FID=" + fid + "&IsEdit=0&IsPrint=0&FK_Node=" + nd.getNodeID() + "&WorkID=" + tk.getWorkID() + "&CWorkID=" + this.CWorkID;
                this.ui.append("\t\n <DIV id='" + frm.getNo() + "' style='width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left; background-color:white;margin:0;padding:0;' >");
                this.ui.append("\t\n <iframe ID='F" + frm.getNo() + "' src='" + src + "' frameborder=0  style='margin:0;padding:0;width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;'  leftMargin='0'  topMargin='0'  /></iframe>");
                this.ui.append("\t\n </DIV>");
            }else{
            	 Frm frmFirst = null;
            	 for(Frm frm : frms.ToJavaList()){
            		 if (frmFirst == null) frmFirst = frm;

            		 if (frmFirst.getFrmW() < frm.getFrmW())
            			 frmFirst = frm;
                 }
            	 // #region 载入相关文件.
            	 
            	 csslinks.add(basePath+"WF/Style/Frm/tab.css");
            	 //scripts.add(basePath+"WF/Style/Frm/jquery.min.js");
            	 scripts.add(basePath+"WF/Style/Frm/jquery.idTabs.min.js");
            	 scripts.add(basePath+"WF/Style/Frm/TabClick.js");
            	 
            	 this.ui.getTmpList().clear();
            	 this.ui.append("<div  style='clear:both' ></div>"); //
            	 this.ui.append("\t\n<div  id='usual2' class='usual' style='width:" + frmFirst.getFrmW() + "px;height:auto;margin:0 auto;background-color:white;'>");  //begain.
            	 
            	 //#region 输出标签.
            	 this.ui.append("\t\n <ul  class='abc' style='background:red;border-color: #800000;border-width: 10px;' >");
            	 for (Frm frm : frms.ToJavaList()){
                     FrmNode fn = frm.HisFrmNode;
                     String src = "";
                     FrmType a = fn.getHisFrmType();
                     fn.setHisFrmType(fn.getHisFrmType());
                     src = fn.getFrmUrl() + ".jsp?FK_MapData=" + frm.getNo() + "&FID=" + fid + "&IsEdit=0&IsPrint=0&FK_Node=" + nd.getNodeID() + "&WorkID=" + tk.getWorkID() + "&CWorkID=" + this.CWorkID;
                     this.ui.append("\t\n<li><a href=\"#" + frm.getNo() + "\" onclick=\"TabClick('" + frm.getNo() + "','" + src + "');\" >" + frm.getName() + "</a></li>");
                 }
            	 this.ui.append("\t\n </ul>");
                 //#endregion 输出标签.
            	 
            	 //#region 输出表单 iframe 内容.
            	 for(Frm frm : frms.ToJavaList()){
            		 FrmNode fn = frm.HisFrmNode;
            		 this.ui.append("\t\n <DIV id='" + frm.getNo() + "' style='width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;margin:0px;padding:0px;' >");
                     String src = "loading.htm";
                     this.ui.append("\t\n <iframe ID='F" + frm.getNo() + "' src='" + src + "' frameborder=0  style='margin:0px;padding:0px;width:" + frm.getFrmW() + "px; height:" + frm.getFrmH() + "px;text-align: left;'  leftMargin='0'  topMargin='0'   /></iframe>");
                     this.ui.append("\t\n </DIV>");
            	 }
            	 //#endregion 输出表单 iframe 内容.
            	 
            	 this.ui.append("\t\n</div>"); // end  usual2
            	 
            	  // 设置选择的默认值.
            	 this.ui.append("\t\n<script type='text/javascript'>");
            	 this.ui.append("\t\n  $(\"#usual2 ul\").idTabs(\"" + ((Frm)frms.get(0)).getNo() + "\");");
            	 this.ui.append("\t\n</script>");
            }
        }
	}
	
	
	 public void BindTrack_ViewSpecialWork(){
	     ReturnWorks rws = new ReturnWorks();
	     rws.Retrieve(ReturnWorkAttr.ReturnToNode, this.FK_Node, ReturnWorkAttr.WorkID, this.WorkID);
	
	     Node nd = new Node(this.FK_Node);
	     Work wk = nd.getHisWork();
	     wk.setOID(this.WorkID);
	     wk.RetrieveFromDBSources();
	     this.ui.append(BaseModel.AddB(wk.getEnDesc()));
	     this.ADDWork(wk, rws, this.FK_Node);
     }
	
	//#region 输出自由格式的表单.
	//根据宽度计算出来微调.暂时900px
	private float wtX = 0;
	private float x=0;
	private String paramsStr = "";
		
	private String ctrlUseSta = "";
    private String FK_MapData = "";
    private FrmEvents fes = null;
    private String EnName = "";
    private String LinkFields = "";
    private MapData mapData = null;
    private MapExts mes = null;
    private MapAttrs mattrs = null;
    private Entity HisEn = null;
    private MapM2Ms m2ms = null;
    private MapDtls dtls = null;
    
    private boolean IsAddCa;
    private boolean IsReadonly;
    private boolean IsPostBack = false;
    public void BindCCForm(Entity en, String enName, boolean isReadonly, float srcWidth){
       MapData md = new MapData(enName);
       BindCCForm(en, md, md.getMapAttrs(), enName, isReadonly, srcWidth);
    }
  
    public void BindCCForm(Entity en, MapData md, MapAttrs mattrs, String enName, boolean isReadonly, float srcWidth) { 
		this.ctrlUseSta = "";
	
	    this.EnName = enName;
	    this.mapData = md;
	    this.mattrs = mattrs;
	    
	    this.paramsStr= "&" + this._request.getQueryString();
    	
	    //根据宽度计算出来微调.
        wtX = MapData.GenerSpanWeiYi(md, srcWidth);
        x=0;
        this.mes = this.mapData.getMapExts();
        this.IsReadonly = isReadonly;
        this.FK_MapData = enName;
        this.HisEn = en;
        this.m2ms = this.mapData.getMapM2Ms();
        this.dtls = this.mapData.getMapDtls();
        
        //是否加载CA签名 dll.
        this.IsAddCa = false;
        //#region 处理事件.
        this.fes = this.mapData.getFrmEvents();
//        if (!this.IsPostBack){
//            try{
//                String msg = fes.DoEventNode(FrmEventList.FrmLoadBefore, en);
//                if (msg == "OK"){
//                    en.RetrieveFromDBSources();
//                }else{
//                    if (!StringHelper.isNullOrEmpty(msg)){
//                        en.RetrieveFromDBSources();
//                        //this.Alert(msg);
//                    }
//                }
//            }
//            catch (Exception ex){
//                //this.Alert(ex.Message);
//                return;
//            }
//        }
        //#endregion 处理事件.
        
        //MapAttrs mattrs = this.mapData.getMapAttrs();
        //处理它的默认值.
        this.DealDefVal(mattrs);
       //处理装载前填充.
        this.LoadData(mattrs, en);
        
        //#region 输出Ele
        this.printFrmEles();
    	//输出按钮
        this.printFrmBtns();
		// 输出标签
        this.printFrmLabs();
		// 输出线
        this.printFrmLines();
	    // 输出超链接
        this.printFrmLinks();
		// 输出图片
        this.printFrmImgs();
        // 输出数据控件，编辑框，复选框，下拉菜单
     	this.printBaseDataUI();
     	// 输出 单选.
     	this.printFrmRBs();
     	// 输出明细.
     	this.printFrmDtls();
     	// 输出报表
        this.printFrmRpts();
        // 输出审核组件
		FrmWorkCheck fwc = new FrmWorkCheck(this.EnName);
     	this.printFrmWorkCheck(fwc);
     	// 父子流程组件
     	this.printSubFLow(fwc);
     	// 输出多对多的关系
     	this.printMapM2Ms();
     	// 输出附件
     	this.printFrmAttachments();
     	// 处理扩展.
 		if (!this.IsReadonly){
 			AfterBindEn_DealMapExt(enName, mattrs, HisEn);
 		}
    }
    
    private int idx = 0;
	private int rowIdx = 0;
	private GroupField currGF;
	private GroupFields gfs;
	private MapFrames frames;
	private FrmAttachments aths;
	private boolean isLeftNext;
    public void BindColumn4(Entity en, String enName){
    	this.ctrlUseSta = "";
    	this.EnName = enName;
    	this.HisEn = en;
    	this.mapData = new MapData(enName);
  
    	this.currGF = new GroupField();
  		MapAttrs mattrs = mapData.getMapAttrs();
  		this.gfs = mapData.getGroupFields();
  		this.dtls = mapData.getMapDtls();
  		this.frames = mapData.getMapFrames();
  		this.m2ms = mapData.getMapM2Ms();
  		this.aths = mapData.getFrmAttachments();
  		this.mes = mapData.getMapExts();
  		
  		this.paramsStr= "&" + this._request.getQueryString();
  		
  		//#region 处理事件.
  		fes = mapData.getFrmEvents();
//		if (!IsPostBack){
//			try{
//				String msg = fes.DoEventNode(FrmEventList.FrmLoadBefore, en);
//				if (!StringHelper.isNullOrEmpty(msg)){
//					Alert(msg);
//				}
//			}catch (RuntimeException ex){
//				//string msg = ex.Message;
//				Alert(ex.getMessage());
//				return;
//			}
//		}  
  		
  		//处理默认值.
  		DealDefVal(mattrs);
  		//处理装载前填充.
  		LoadData(mattrs, en);
  		
  		// region 计算出来列的宽度.
		int labCol = 80;
		int ctrlCol = 260;
		int width = (labCol + ctrlCol) * mapData.getTableCol() / 2;
		// endregion 计算出来列的宽度.
		
		// region 生成表头.
		this.ui.append("\t\n<Table style='width:" + width + "px;margin-left:110px'>");

		//this.ui.append(BaseModel.AddTREnd());
		// endregion 生成表头.
		for (GroupField gf : GroupFields.convertGroupFields(gfs)){
			currGF = gf;
			this.ui.append(BaseModel.AddTR());
			if (gfs.size() == 1){
				this.ui.append(BaseModel.AddTD("colspan=" + mapData.getTableCol() + " style='width:" + width + "px' class=GroupField valign='top' align=left ", "<div style='text-align:left; float:left'>&nbsp;" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			}else{
				this.ui.append(BaseModel.AddTD("colspan=" + mapData.getTableCol() + " style='width:" + width + "px' class=GroupField valign='top' align=left  onclick=\"GroupBarClick('" + gf.getIdx() + "')\"  ", "<div style='text-align:left; float:left'>&nbsp;<img src='" + basePath + "WF/Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "' border=0 />&nbsp;" + gf.getLab() + "</div><div style='text-align:right; float:right'></div>"));
			}
			this.ui.append(BaseModel.AddTREnd());

			boolean isHaveH = false;
			idx = -1;

			rowIdx = 0;
			int colSpan = mapData.getTableCol(); // 定义colspan的宽度.
			this.ui.append(BaseModel.AddTR());
			for (int i = 0; i < mattrs.size(); i++){
				MapAttr attr = (MapAttr)((mattrs.get(i) instanceof MapAttr) ? mattrs.get(i) : null);
				
				// region 过滤不显示的字段.
				if (attr.getGroupID() != gf.getOID()){
					if (gf.getIdx() == 0 && attr.getGroupID() == 0){
					}else{
						continue;
					}
				}
				if (attr.getHisAttr().getIsRefAttr() || !attr.getUIVisible()){
					continue;
				}
				
				if (colSpan == 0){
					InsertObjects(true);
				}
				// endregion 过滤不显示的字段.

			    // region 补充空白的列.
				if (colSpan <= 0){
					//如果列已经用完.
					this.ui.append(BaseModel.AddTREnd());
					colSpan = mapData.getTableCol(); //补充列.
					rowIdx++;
				}
				// endregion 补充空白的列.

				// region 处理大块文本的输出.
				// 显示的顺序号.
				idx++;
				if (attr.getIsBigDoc() && (attr.getColSpan() == mapData.getTableCol() || attr.getColSpan() == 0)){
					int h = attr.getUIHeightInt() + 20;
					if (attr.getUIIsEnable()){
						this.ui.append("<TD height='" + (new Integer(h)).toString() + "px'  colspan=" + mapData.getTableCol() + " width='100%' valign=top align=left>");
					}else{
						this.ui.append("<TD height='" + (new Integer(h)).toString() + "px'  colspan=" + mapData.getTableCol() + " width='100%' valign=top class=TBReadonly>");
					}
					
					this.ui.append("<div style='font-size:12px;color:black;' >");
					Label lab = this.ui.creatLabel("Lab" + attr.getKeyOfEn());
					lab.setText(attr.getName());
					this.ui.append(lab);
					//this.ui.append("<label id='"+attr.getKeyOfEn()+"'>");
					//this.ui.append(attr.getName()+"</label>");
					this.ui.append("</div>");
					
					if (attr.getTBModel() == 2){
						//富文本输出.
						AddRichTextBox(en, attr);
					}else{
						
						TextBox mytbLine = this.ui.creatTextBox("TB_" + attr.getKeyOfEn());
						mytbLine.setTextMode(TextBoxMode.MultiLine);
						mytbLine.setText(en.GetValStrByKey(attr.getKeyOfEn()).replace("\\n", "\n"));
						
						mytbLine.setEnabled(attr.getUIIsEnable());
						if (!mytbLine.getEnabled()){
							mytbLine.setReadOnly(true);
						}else{
							mytbLine.addAttr("class", "TBDoc");
						}
						mytbLine.addAttr("style", "width:98%;height:" + attr.getUIHeight() + "px;padding: 0px;margin: 0px;");
						this.ui.append(mytbLine);
						
						/*if (mytbLine.getEnabled()){
							String ctlID = mytbLine.getId();
							Label mylab = (Label) this.ui.GetUIByID("Lab" + attr.getKeyOfEn());
							mylab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + basePath + "','" + enName + "','" + attr.getKeyOfEn() + "')\">" + attr.getName() + "</a>");
						}*/
					}
					
					this.ui.append(BaseModel.AddTDEnd());
					this.ui.append(BaseModel.AddTREnd());
					rowIdx++;
					isLeftNext = true;
					continue;
				}
			
				if (attr.getIsBigDoc()){
					if (colSpan == mapData.getTableCol()){
						//已经加满了
						this.ui.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
						colSpan = colSpan - attr.getColSpan(); // 减去已经占用的col.
					}
					
					this.ui.append("<TD class=FDesc colspan=" + attr.getColSpan() + " height='" + attr.getUIHeight() + "px' >");
					this.ui.append(attr.getName());
					
					TextBox mytbLine = this.ui.creatTextBox("TB_" + attr.getKeyOfEn());
					mytbLine.setTextMode(TextBoxMode.MultiLine);
					mytbLine.addAttr("class", "TBDoc");
					if (!mytbLine.getEnabled()){
						mytbLine.addAttr("class", "TBReadonly");
						mytbLine.setReadOnly(true);
					}
					mytbLine.addAttr("style", "width:98%;height:100%;padding: 0px;margin: 0px;");
					this.ui.append(mytbLine);
					this.ui.append(BaseModel.AddTDEnd());
					continue;
				}
				// endregion 大块文本的输出.
				
				// #region 处理超链接
				if (!attr.getUIIsEnable()){
					// 判断是否有隐藏的超链接字段. 
					if (LinkFields.contains("," + attr.getKeyOfEn() + ",")){
						Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.Link);
						MapExt meLink = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
						String url = meLink.getTag();
						if (!url.contains("?")){
							url = url + "?a3=2";
						}
						url = url + "&WebUserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&EnName=" + enName;
						if (url.contains("@AppPath")){
							url = url.replace("@AppPath", basePath);
						}
						if (url.contains("@")){
							Attrs attrs = en.getEnMap().getAttrs();
							for (Attr item : attrs){
								url = url.replace("@" + attr.getKeyOfEn(), en.GetValStrByKey(attr.getKeyOfEn()));
								if (!url.contains("@")){
									break;
								}
							}
						}
						this.ui.append(BaseModel.AddTD("colspan=" + colSpan, "<a href='" + url + "' target='" + meLink.getTag1() + "' >" + en.GetValByKey(attr.getKeyOfEn()) + "</a>"));
						continue;
					}
				}
				// endregion 处理超链接
				
				// region  首先判断当前剩余的单元格是否满足当前控件的需要。
				if (attr.getColSpan() + 1 > mapData.getTableCol()){
					attr.setColSpan(mapData.getTableCol() - 1); //如果设置的
				}
				if (colSpan < attr.getColSpan() + 1 || colSpan == 1 || colSpan == 0){
					//如果剩余的列不能满足当前的单元格，就补充上它，让它换行.
					if (colSpan != 0){
						this.ui.append(BaseModel.AddTD("colspan=" + colSpan, ""));
					}
					this.ui.append(BaseModel.AddTREnd());

					colSpan = mapData.getTableCol();
					this.ui.append(BaseModel.AddTR());
				}
				// endregion  首先判断当前剩余的单元格是否满足当前控件的需要。
				
				//#region 其它的就是增加一列控件一列描述的字段.
				TextBox tb = this.ui.creatTextBox("TB_" + attr.getKeyOfEn());
				tb.setEnabled(attr.getUIIsEnable());
				colSpan = colSpan - 1 - attr.getColSpan(); // 首先减去当前的占位.
				switch (attr.getLGType()){
					case Normal:
						switch (attr.getMyDataType()){
							case BP.DA.DataType.AppString:
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								if (attr.getIsSigan()){
									String v = en.GetValStrByKey(attr.getKeyOfEn());
									if (v.length() == 0){
										this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), "<img src='" + basePath + "DataUser/Siganture/" + WebUser.getNo() + ".jpg' border=0 onerror=\"this.src='" + basePath + "DataUser/Siganture/UnName.jpg'\"/>"));
									}else{
										this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), "<img src='" + basePath + "DataUser/Siganture/" + v + ".jpg' border=0 onerror=\"this.src='" + basePath + "DataUser/Siganture/UnName.jpg'\"/>"));
									}
								}else{
									tb.setShowType(TBType.TB);
									tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
									tb.addAttr("width", "100%");
									this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								}
								break;
							case BP.DA.DataType.AppDate:
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								tb.setShowType(TBType.Date);
								tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
								if (attr.getUIIsEnable()){
									tb.addAttr("onfocus", "WdatePicker();");
								}
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								break;
							case BP.DA.DataType.AppDateTime:
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								tb.setShowType(TBType.DateTime);
								tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
								if (attr.getUIIsEnable()){
									tb.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
								}
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								break;
							case BP.DA.DataType.AppBoolean:
								this.ui.append(BaseModel.AddTDDesc(""));
								CheckBox cb = this.ui.creatCheckBox("CB_" + attr.getKeyOfEn());
								cb.setText(attr.getName().trim());
								//cb.setChecked(attr.getDefValOfBool());
								cb.setEnabled(attr.getUIIsEnable());
								cb.setChecked(en.GetValBooleanByKey(attr.getKeyOfEn()));
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), cb));
								break;
							case BP.DA.DataType.AppDouble:
							case BP.DA.DataType.AppFloat:
								//增加验证
								//tb.Attributes.Add("onkeyup", @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								tb.setShowType(TBType.Num);
								tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								break;
							case BP.DA.DataType.AppInt:
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								tb.setShowType(TBType.Num);
								//增加验证
								//tb.Attributes.Add("onkeyup", @"value=value.replace(/[^-?\d]/g,'')");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d]/g,'')");
								tb.setText(en.GetValStrByKey(attr.getKeyOfEn()));
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								break;
							case BP.DA.DataType.AppMoney:
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								tb.setShowType(TBType.Moneny);
		
								if (SystemConfig.getAppSettings().get("IsEnableNull").equals("1")){
									java.math.BigDecimal v = en.GetValMoneyByKey(attr.getKeyOfEn());
									if (v.equals(567567567)){
										tb.setText("");
									}else{
										tb.setText(decimalFormat(v));
									}
								}else{
									tb.setText(decimalFormat(en.GetValMoneyByKey(attr.getKeyOfEn())));
								}
		
								//tb.Text = en.GetValMoneyByKey(attr.KeyOfEn).ToString("0.00");
		
								//增加验证
								//tb.Attributes.Add("onkeyup", @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								break;
							case BP.DA.DataType.AppRate:
								this.ui.append(BaseModel.AddTDDesc(attr.getName()));
								tb.setShowType(TBType.Moneny);
								tb.setText(decimalFormat(en.GetValMoneyByKey(attr.getKeyOfEn())));
								//增加验证
								//tb.Attributes.Add("onkeyup", @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
								this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
								break;
							default:
								break;
						}
						// tb.Attributes["width"] = "100%";
						switch (attr.getMyDataType()){
							case BP.DA.DataType.AppString:
							case BP.DA.DataType.AppDateTime:
							case BP.DA.DataType.AppDate:
								if (tb.getEnabled()){
									tb.addAttr("maxlength", String.valueOf(attr.getMaxLen()));	
								}else{
									tb.addAttr("class", "TBReadonly");
								}
								break;
							default:
								if (tb.getEnabled()){
									tb.addAttr("class", "TBNum");
								}else{
									tb.addAttr("class", "TBNumReadonly");
								}
								break;
						}
						break;
					case Enum:
						if (attr.getUIContralType() == UIContralType.DDL){
							this.ui.append(BaseModel.AddTDDesc(attr.getName()));
							DDL ddle = this.ui.creatDDL("DDL_" + attr.getKeyOfEn());
							ddle.BindSysEnum(attr.getUIBindKey());
							ddle.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
							ddle.setEnabled(attr.getUIIsEnable());
							this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), ddle));
						}else{
							this.ui.append(BaseModel.AddTDDesc(attr.getName()));
							this.ui.append("<TD class=TD colspan='" + attr.getColSpan() + "'>");
							SysEnums ses = new SysEnums(attr.getUIBindKey());
							for (SysEnum item : SysEnums.convertSysEnums(ses)){
								RadioButton rb = this.ui.creatRadioButton("RB_" + attr.getKeyOfEn() + "_" + item.getIntKey());
								rb.setText(item.getLab());
								if (item.getIntKey() == en.GetValIntByKey(attr.getKeyOfEn())){
									rb.setChecked(true);
								} else{
									rb.setChecked(false);
								}
								rb.setName(attr.getKeyOfEn());
								this.ui.append(rb);
							}
							this.ui.append(BaseModel.AddTDEnd());
						}
						break;
					case FK:
						this.ui.append(BaseModel.AddTDDesc(attr.getName()));
						DDL ddl1 = this.ui.creatDDL("DDL_" + attr.getKeyOfEn());
						try{
							EntitiesNoName ens = attr.getHisEntitiesNoName();
							ens.RetrieveAll();
							ddl1.BindEntities(ens);
							ddl1.SetSelectItem(en.GetValStrByKey(attr.getKeyOfEn()));
						}catch (java.lang.Exception e){}
						ddl1.setEnabled(attr.getUIIsEnable());
						this.ui.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), ddl1));
						break;
					default:
						break;
				}
				// endregion 其它的就是增加一列控件一列描述的字段
			}// 结束字段集合循环.
			
			// 在分组后处理它, 首先判断当前剩余的单元格是否满足当前控件的需要。
			if (colSpan != mapData.getTableCol()){
				// 如果剩余的列不能满足当前的单元格，就补充上它，让它换行.
				if (colSpan != 0){
					this.ui.append(BaseModel.AddTD("colspan=" + colSpan, ""));
				}
				this.ui.append(BaseModel.AddTREnd());
				colSpan = mapData.getTableCol();
			}
			InsertObjects(false);
		}// 结束分组循环.
		
		// region 审核组件
		FrmWorkCheck fwc = new FrmWorkCheck(enName);
		if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable){
			rowIdx++;

			this.ui.append(BaseModel.AddTR());
			this.ui.append(BaseModel.AddTD("colspan=" + mapData.getTableCol() + " class=GroupField valign='top' align=left ", "<div style='text-align:left; float:left'>&nbsp;审核信息</div><div style='text-align:right; float:right'></div>"));
			this.ui.append(BaseModel.AddTREnd());
			
			// myidx++;
			this.ui.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			this.ui.append("<TD colspan=" + mapData.getTableCol() + " ID='TD" + enName + "' height='50px' width='100%' style='align:left'>");
			String src = basePath + "WF/WorkOpt/WorkCheck.jsp?s=2";
			String paras = paramsStr;
			try{
				if (!paras.contains("FID=")){
					paras += "&FID=" + en.GetValStrByKey("FID");
				}
			}catch (java.lang.Exception e2){}
			if (!paras.contains("OID=")){
				paras += "&OID=" + en.GetValStrByKey("OID");
			}
			src += "&r=q" + paras;
			this.ui.append("<iframe ID='F33" + fwc.getNo() + "'  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0'  width='100%'  scrolling=auto/></iframe>");
			this.ui.append(BaseModel.AddTDEnd());
			this.ui.append(BaseModel.AddTREnd());
		}
		// endregion 审核组件
		
		this.ui.append(BaseModel.AddTREnd());
		this.ui.append(BaseModel.AddTableEnd());
		
		// region 处理iFrom 的自适应的问题。
		String js = "\t\n<script type='text/javascript' >";
		for (MapDtl dtl : MapDtls.convertMapDtls(dtls)){
			if (!dtl.getIsView())
				continue;

			js += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.getNo() + "','TD" + dtl.getNo() + "')\", 200);";
		}
		 
		for (FrmAttachment ath : FrmAttachments.convertFrmAttachments(aths)){
			// if (ath.IsAutoSize)
			js += "\t\n window.setInterval(\"ReinitIframe('F" + ath.getMyPK() + "','TD" + ath.getMyPK() + "')\", 200);";
		}
		js += "\t\n</script>";
		this.ui.append(js);
		// endregion 处理iFrom 的自适应的问题。
		
		// 处理扩展。
		AfterBindEn_DealMapExt(enName, mattrs, en);
		if (!this.IsReadonly ){
				// region 处理iFrom SaveDtlData。
			js = "\t\n<script type='text/javascript' >";
			js += "\t\n function SaveDtl(dtl) { ";
			//    js += "\t\n    GenerPageKVs(); //调用产生kvs ";
			js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveDtlData(); ";
			js += "\t\n } ";
			js += "\t\n</script>";
			this.ui.append(js);
			// endregion 处理iFrom SaveDtlData。

			// region 处理iFrom  SaveM2M Save
			js = "\t\n<script type='text/javascript' >";
			js += "\t\n function SaveM2M(dtl) { ";
			js += "\t\n document.getElementById('F' + dtl ).contentWindow.SaveM2M();";
			js += "\t\n } ";
			js += "\t\n</script>";
			this.ui.append(js);
			// endregion 处理iFrom  SaveM2M Save。
		}
		
    }
    
    private void DealDefVal(MapAttrs mattrs){
        if (this.IsReadonly)
            return;
        
        scripts.add(basePath+"DataUser/JSLibData/" + this.EnName + "_Self.js");
		scripts.add(basePath+"DataUser/JSLibData/" + this.EnName + ".js");
//        this.Page.RegisterClientScriptBlock("y7",
//        		"<script language='JavaScript' src='" + CCFlowAppPath + "DataUser/JSLibData/" + this.EnName + "_Self.js' ></script>");
//
//        this.Page.RegisterClientScriptBlock("yfd7",
//        		"<script language='JavaScript' src='" + CCFlowAppPath + "DataUser/JSLibData/" + this.EnName + ".js' ></script>");

        for(MapAttr attr : MapAttrs.convertMapAttrs(mattrs)){
            if (!attr.getDefValReal().contains("@"))
                continue;

            this.HisEn.SetValByKey(attr.getKeyOfEn(), attr.getDefVal());
        }
    }
    
    private boolean IsLoadData = false;
    private void LoadData(MapAttrs mattrs, Entity en) {
    	 this.LinkFields = "";
         if (mes.size() == 0)
             return;
         
     	for (MapExt myitem : MapExts.convertMapExts(mes)){
			if (myitem.getExtType() == MapExtXmlList.Link){
				LinkFields += "," + myitem.getAttrOfOper() + ",";
			}
		}
     	
     	if (!IsLoadData){
			return;
		}
     	
    	Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull);
		MapExt item = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
		if (item == null){
			return;
		}

		DataTable dt = null;
		String sql = item.getTag();
		if (!StringHelper.isNullOrEmpty(sql)){
			// 如果有填充主表的sql
			// #region 处理sql变量
			sql = sql.replace("WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
			for (MapAttr attr : MapAttrs.convertMapAttrs(mattrs)){
				if (sql.contains("@")){
					sql = sql.replace("@" + attr.getKeyOfEn(), en.GetValStrByKey(attr.getKeyOfEn()));
				}else{
					break;
				}
			}
			//#endregion 处理sql变量
			
			if (!StringHelper.isNullOrEmpty(sql)){
				if (sql.contains("@")){
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1){
					DataRow dr = dt.Rows.get(0);
					for (DataColumn dc : dt.Columns){
						en.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
					}
				}
			}
			
			if (StringHelper.isNullOrEmpty(item.getTag1()) || item.getTag1().length() < 15){
				return;
			}
			
			// 填充从表.
			for (MapDtl dtl : MapDtls.convertMapDtls(dtls)){
				String[] sqls = item.getTag1().split("\\*");
				for (String mysql : sqls){
					if (StringHelper.isNullOrEmpty(mysql))
						continue;
					if (!mysql.contains(dtl.getNo() + "="))
						continue;

					//#region 处理sql.
					sql = mysql;
					sql = sql.replace(dtl.getNo() + "=", "");
					sql = sql.replace("WebUser.No", WebUser.getNo());
					sql = sql.replace("@WebUser.Name", WebUser.getName());
					sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
					for (MapAttr attr : MapAttrs.convertMapAttrs(mattrs)){
						if (sql.contains("@")){
							sql = sql.replace("@" + attr.getKeyOfEn(), en.GetValStrByKey(attr.getKeyOfEn()));
						}else{
							break;
						}
					}
					// #endregion 处理sql.
					
					if (StringHelper.isNullOrEmpty(sql))
						continue;
					if (sql.contains("@")){
						throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
					}
					
					GEDtls gedtls = new GEDtls(dtl.getNo());
					gedtls.Delete(GEDtlAttr.RefPK, en.getPKVal());
					
					dt = DBAccess.RunSQLReturnTable(sql);
					for (DataRow dr : dt.Rows){
						GEDtl gedtl = (GEDtl)((gedtls.getGetNewEntity() instanceof GEDtl) ? gedtls.getGetNewEntity() : null);
						for (DataColumn dc : dt.Columns){
							gedtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
						}
						gedtl.setRefPK(en.getPKVal().toString());
						gedtl.setRDT(DataType.getCurrentDataTime());
						gedtl.setRec(WebUser.getNo());
						gedtl.Insert();
					}
				}
			}
		}
    }
    /**
	 * 输出事件
	 */
	private void printFrmEles(){
		FrmEles eles = mapData.getFrmEles();
		if (eles.size() >= 1){
			String myjs = "\t\n<script type='text/javascript' >";
			myjs += "\t\n function BPPaint(ctrl,url,w,h,fk_FrmEle)";
			myjs += "\t\n {";
			myjs += "\t\n  var v= window.showModalDialog(url, 'ddf', 'dialogHeight: '+h+'px; dialogWidth: '+w+'px;center: yes; help: no'); ";
			myjs += "\t\n  if (v==null )  ";
			myjs += "\t\n     return ; ";
			
			myjs += "\t\n  ctrl.src=v+'?temp='+new Date(); ";
			myjs += "\t\n }";
			myjs += "\t\n</script>";
			this.ui.append(myjs);
			
			FrmEleDBs dbs = new FrmEleDBs(this.FK_MapData, this.HisEn.getPKVal().toString());
			for (FrmEle ele : FrmEles.convertFrmEles(eles)){
				float y = ele.getY();
				this.x= ele.getX() + this.wtX;
				this.ui.append("\t\n<DIV id=" + ele.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
				if(ele.getEleType().equals(FrmEle.HandSiganture)){
					Object tempVar = dbs.GetEntityByKey(FrmEleDBAttr.EleID, ele.getEleID());
					FrmEleDB db = (FrmEleDB)((tempVar instanceof FrmEleDB) ? tempVar : null);
					String dbFile = this.basePath + "DataUser/BPPaint/Def.png";
					if (db != null){
						dbFile = db.getTag1();
					}

					if (this.IsReadonly || !ele.getIsEnable()){
						this.ui.append("\t\n<img src='" + dbFile + "' onerror=\"this.src='" + basePath + "DataUser/BPPaint/Def.png'\" style='padding: 0px;margin: 0px;border-width: 0px;width:" + ele.getW() + "px;height:" + ele.getH() + "px;' />");
					}else{
						String url = basePath + "WF/CCForm/BPPaint.jsp?W=" + ele.getHandSiganture_WinOpenW() + "&H=" + ele.getHandSiganture_WinOpenH() + "&MyPK=" + ele.getPKVal() + "&PKVal=" + HisEn.getPKVal();
						myjs = "javascript:BPPaint(this,'" + url + "','" + ele.getHandSiganture_WinOpenW() + "','" + ele.getHandSiganture_WinOpenH() + "','" + ele.getMyPK() + "');";
						//string myjs = "javascript:window.open('" + appPath + "WF/CCForm/BPPaint.jsp?PKVal=" + en.PKVal + "&MyPK=" + ele.MyPK + "&H=" + ele.HandSiganture_WinOpenH + "&W=" + ele.HandSiganture_WinOpenW + "', 'sdf', 'dialogHeight: " + ele.HandSiganture_WinOpenH + "px; dialogWidth: " + ele.HandSiganture_WinOpenW + "px;center: yes; help: no');";
						this.ui.append("\t\n<img id='Ele" + ele.getMyPK() + "' onclick=\"" + myjs + "\" onerror=\"this.src='" + basePath + "DataUser/BPPaint/Def.png'\" src='" + dbFile + "' style='padding: 0px;margin: 0px;border-width: 0px;width:" + ele.getW() + "px;height:" + ele.getH() + "px;' />");
					}
				}else if(ele.getEleType().equals(FrmEle.iFrame)){
					String paras = paramsStr;
					if (!paras.contains("FID=")){
						paras += "&FID=" + this.HisEn.GetValStrByKey("FID");
					}

					if (!paras.contains("WorkID=")){
						paras += "&WorkID=" + this.HisEn.GetValStrByKey("OID");
					}
					Object tempVar2 = ele.getTag1();
					String src = (String)((tempVar2 instanceof String) ? tempVar2 : null); // url
					 if (src.contains("?"))
                         src += "&r=q" + paras;
                     else
                         src += "?r=q" + paras;
					 
					if (!src.contains("UserNo"))
						src += "&UserNo=" + WebUser.getNo();
					if (!src.contains("SID"))
						src += "&SID=" + WebUser.getSID();
					
					if (src.contains("@")){
						for (Attr m : HisEn.getEnMap().getAttrs()){
							if (!src.contains("@"))
								break;
							src = src.replace("@" + m.getKey(), HisEn.GetValStrByKey(m.getKey()));
						}
					}
					this.ui.append("<iframe ID='F" + ele.getEleID() + "'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + ele.getW() + "' height='" + ele.getH() + "' scrolling=auto /></iframe>");
				}else{
					this.ui.append("未处理");
				}
			}
			this.ui.append("\t\n</DIV>");
		}
		//#endregion 输出Ele
	}
	/**
	 * 输出按钮
	 */
	private void printFrmBtns(){
		FrmBtns btns = mapData.getFrmBtns();
		for (FrmBtn btn : FrmBtns.convertFrmBtns(btns)) {
			this.x= btn.getX() + wtX;
			this.ui.append("\t\n<DIV id='u2' style=\"position:absolute;left: "+x +"px;top:"+btn.getY()+"px;text-align:left;\" ><span >");
			String doDoc = BP.WF.Glo.DealExp(btn.getEventContext(), HisEn, null);
			switch (btn.getHisBtnEventType()){
				case Disable:
					this.ui.append("<input type='button' class='Btn' value='"+ btn.getText().replace("&nbsp;", " ")+"' disabled='disabled'/>");
					break;
				case RunExe:
				case RunJS:
					this.ui.append("<input type='button' class='Btn' value='"+ btn.getText().replace("&nbsp;", " ")+"' enable=true onclick=\""+ doDoc+"\" />");
					break;
				default:
					//this.Pub1.append("<input id=\""+btn.getMyPK()+"\" class=\"Btn\" >"+btn.getText().replace("&nbsp;", " ")+"</input>");
					Button myBtn = this.ui.creatButton(btn.getMyPK());
					myBtn.setEnabled(true);
					myBtn.setCssClass("Btn");
					myBtn.setText(btn.getText().replace("&nbsp;", " "));
					//warning myBtn.Click += new EventHandler(myBtn_Click);
					this.ui.append(myBtn);
					break;
			}
			this.ui.append("\t\n</span></DIV>");
		}
		//endregion
	}
	
	/**
	 * 输出标签
	 */
	private void printFrmLabs(){
		FrmLabs labs = mapData.getFrmLabs();
	    for(FrmLab lab : FrmLabs.convertFrmLabs(labs)){
	    	  //Color col = ColorTranslator.FromHtml(lab.FontColor);
	    	  this.x = lab.getX() + wtX;
	    	  this.ui.append("\t\n<DIV id='u2' style='position:absolute;left:"+this.x+"px;top:"+lab.getY()+"px;text-align:left;'>");
	    	  this.ui.append("\t\n<span style='color: "+lab.getFontColorHtml()+"; font-family:"+lab.getFontName()+"; font-size: "+lab.getFontSize()+"px;'>"+lab.getTextHtml()+"");
	    	  this.ui.append("\t\n</span> </DIV>");
		}
	}
	
	/**
	 * 输出线
	 */
	private void printFrmLines(){
		FrmLines lines = mapData.getFrmLines();
		for (FrmLine line : FrmLines.convertFrmLines(lines)){
			if (line.getX1() == line.getX2()){
				// 一道竖线 
				float h = line.getY1() - line.getY2();
				h = Math.abs(h);
				if (line.getY1() < line.getY2()){
					x = line.getX1() + wtX;
					this.ui.append("\t\n<img id=\""+line.getMyPK()+"\" style=\"padding:0px;position:absolute; left:"+x +"px; top:"+line.getY1()+"px; width:"+line.getBorderWidth()+"px; height:"+h +"px;background-color:"+line.getBorderColorHtml()+"\">");
				}
				else{
					x = line.getX2() + wtX;
					this.ui.append("\t\n<img id=\""+line.getMyPK()+"\" style=\"padding:0px;position:absolute; left:"+x +"px; top:"+line.getY2()+"px; width:"+line.getBorderWidth()+"px; height:"+h +"px;background-color:"+line.getBorderColorHtml()+"\">");
				}
			}else{
				// 一道横线 
				float w = line.getX2() - line.getX1();
				if (line.getX1() < line.getX2()){
					x = line.getX1() + wtX;
					this.ui.append("\t\n<img id=\""+line.getMyPK()+"\" style=\"padding:0px;position:absolute; left:"+x +"px; top:"+line.getY1()+"px; width:"+w +"px; height:"+line.getBorderWidth()+"px;background-color:"+line.getBorderColorHtml()+"\">");
				}else{
					x = line.getX2() + wtX;
					this.ui.append("\t\n<img id=\""+line.getMyPK()+"\" style=\"padding:0px;position:absolute; left:"+x +"px; top:"+line.getY2()+"px; width:"+w +"px; height:"+line.getBorderWidth()+"px;background-color:"+line.getBorderColorHtml()+"\">");
				}
			}
		}
	}
	
	/**
	 * 输出超链接
	 */
	private void printFrmLinks(){
		FrmLinks links = mapData.getFrmLinks();
		for (FrmLink link : FrmLinks.convertFrmLinks(links)){
			String url = link.getURL();
			if (url.contains("@")){
				for (MapAttr attr : MapAttrs.convertMapAttrs(this.mattrs)){
					if (!url.contains("@"))
						break;
					url = url.replace("@" + attr.getKeyOfEn(), HisEn.GetValStrByKey(attr.getKeyOfEn()));
				}
			}
			x = link.getX() + wtX;
			this.ui.append("\t\n<DIV id='u2' style=\"position:absolute;left:"+ x+"px;top:"+link.getY()+"px;text-align:left;\">");
			this.ui.append("\t\n<span style=\"color:"+link.getFontColorHtml()+";font-family: "+link.getFontName()+";font-size: "+link.getFontSize()+"px\"> ");
			this.ui.append("<a href="+ url+" target="+link.getTarget()+"> "+link.getText()+"</a></span>");
			this.ui.append("\t\n</DIV>");
		}
	}
	
	/**
	 * 输出图片
	 */
	private void printFrmImgs(){
		FrmImgs imgs = mapData.getFrmImgs();
		for (FrmImg img : FrmImgs.convertFrmImgs(imgs)){
			float y = img.getY();
			String imgSrc = "";
			//imgSrc = appPath + "DataUser/ICON/" + BP.Sys.SystemConfig.CompanyID + "/LogBiger.png";
			//图片类型
			if (img.getHisImgAppType() == ImgAppType.Img){
				//数据来源为本地.
				if (img.getImgSrcType() == 0){
					if (!img.getImgPath().contains(";")){
						imgSrc = img.getImgPath();
					}
				}
				
				//数据来源为指定路径.
                if (img.getImgSrcType() == 1){
                	//图片路径不为默认值
					imgSrc = img.getImgURL();
					if (imgSrc.contains("@")){
						//如果有变量
						imgSrc = BP.WF.Glo.DealExp(imgSrc, HisEn, "");
					}
                }
                
                x = img.getX() + wtX;
                this.ui.append("\t\n<DIV id="+img.getMyPK()+" style='position:absolute;left:"+x+"px;top:"+y+"px;text-align:left;vertical-align:top' >");
				
				String img_src = basePath + imgSrc;
				imgSrc = img_src.replace("//", "/");
				if (!StringHelper.isNullOrEmpty(img.getLinkURL())){
					this.ui.append("\t\n<a href='"+img.getLinkURL()+"' target="+img.getLinkTarget()+" >");
					this.ui.append("<img src='"+imgSrc +"'  onerror=\"this.src='"+basePath +"DataUser/ICON/CCFlow/LogBig.png'\"  style='padding: 0px;margin: 0px;border-width: 0px;width:"+img.getW()+"px;height:"+img.getH()+"px;' />");
					this.ui.append("</a>");
				}else{
					this.ui.append("\t\n<img src='"+imgSrc +"'  onerror=\"this.src='"+basePath +"DataUser/ICON/CCFlow/LogBig.png'\"   style='padding: 0px;margin: 0px;border-width: 0px;width:"+img.getW()+"px;height:"+img.getH()+"px;' />");
				}
				this.ui.append("\t\n</DIV>");
				continue;
			}
			
			// 电子签章
			//获取登录人岗位
			String stationNo = "";
			//签章对应部门
			String fk_dept = WebUser.getFK_Dept();
			//部门来源类别
			String sealType = "0";
			//签章对应岗位
			String fk_station = img.getTag0();
			//表单字段
			String sealField = "";
			String sql = "";
			//如果设置了部门与岗位的集合进行拆分
			if (!StringHelper.isNullOrEmpty(img.getTag0()) && img.getTag0().contains("^") && img.getTag0().split("\\^").length == 4){
				fk_dept = img.getTag0().split("\\^")[0];
				fk_station = img.getTag0().split("\\^")[1];
				sealType = img.getTag0().split("\\^")[2];
				sealField = img.getTag0().split("\\^")[3];
				//如果部门没有设定，就获取部门来源
				///if (fk_dept == "all")
				//{
				//默认当前登录人
				fk_dept = WebUser.getFK_Dept();
				//发起人
				if (sealType.equals("1")){
					sql = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID=" + this.HisEn.GetValStrByKey("OID");
					fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
				}
				//表单字段
				if (sealType.equals("2") && !StringHelper.isNullOrEmpty(sealField)){
					//判断字段是否存在
					for (MapAttr attr : MapAttrs.convertMapAttrs(mattrs)){
						if (sealField.equals(attr.getKeyOfEn())){
							fk_dept = HisEn.GetValStrByKey(sealField);
							break;
						}
					}
				}
			}
			//判断本部门下是否有此人
			//sql = "SELECT fk_station from port_deptEmpStation where fk_dept='" + fk_dept + "' and fk_emp='" + WebUser.getNo() + "'";
			sql = String.format(" select FK_Station from Port_DeptStation where FK_Dept ='%1$s' and FK_Station in (select FK_Station from Port_EmpStation where FK_Emp='%2$s')", fk_dept, WebUser.getNo());
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for(DataRow dr : dt.Rows){
				if (fk_station.contains(dr.getValue(0).toString() + ",")){
					stationNo = dr.getValue(0).toString();
					break;
				}
			}
			//重新加载 可能有缓存
			img.Retrieve("MyPk", img.getMyPK());
			//0.不可以修改，从数据表中取，1可以修改，使用组合获取并保存数据
			if (img.getIsEdit() == 1 && !this.IsReadonly){
				imgSrc = basePath + "DataUser/Seal/" + fk_dept + "_" + stationNo + ".jpg";
				//设置主键
				String myPK = StringHelper.isNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK();
				myPK = myPK + "_" + HisEn.GetValStrByKey("OID") + "_" + img.getMyPK();

				FrmEleDB imgDb = new FrmEleDB();
				QueryObject queryInfo = new QueryObject(imgDb);
				queryInfo.AddWhere(FrmEleAttr.MyPK, myPK);
				queryInfo.DoQuery();
				//判断是否存在
				if (imgDb != null && !StringHelper.isNullOrEmpty(imgDb.getFK_MapData())){
					imgDb.setFK_MapData(StringHelper.isNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK());
					imgDb.setEleID(HisEn.GetValStrByKey("OID"));
					imgDb.setRefPKVal(img.getMyPK());
					imgDb.setTag1(imgSrc);
					imgDb.Update();
				}
				else{
					imgDb.setFK_MapData(StringHelper.isNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK());
					imgDb.setEleID(HisEn.GetValStrByKey("OID"));
					imgDb.setRefPKVal(img.getMyPK());
					imgDb.setTag1(imgSrc);
					imgDb.Insert();
				}
				//添加控件
				x = img.getX() + wtX;
				this.ui.append("\t\n<DIV id=\""+img.getMyPK()+"\" style='position:absolute;left:"+x +"px;top:"+y +"px;text-align:left;vertical-align:top' >");
				this.ui.append("\t\n<img src='"+imgSrc +"' onerror='javascript:this.src="+basePath +"DataUser/ICON/"+SystemConfig.getCompanyID()+"/LogBiger.png\";' style='padding: 0px;margin: 0px;border-width: 0px;width:"+img.getW()+"px;height:"+img.getH()+"px' />");
				this.ui.append("\t\n</DIV>");
			}
			else{
				FrmEleDB realDB = null;
				FrmEleDB imgDb = new FrmEleDB();
				QueryObject objQuery = new QueryObject(imgDb);
				objQuery.AddWhere(FrmEleAttr.FK_MapData, img.getEnPK());
				objQuery.addAnd();
				objQuery.AddWhere(FrmEleAttr.EleID, HisEn.GetValStrByKey("OID"));
				objQuery.DoQuery();
				if (objQuery.GetCount() == 0){
					FrmEleDBs imgdbs = new FrmEleDBs();
					QueryObject objQuerys = new QueryObject(imgdbs);
					objQuerys.AddWhere(FrmEleAttr.EleID, HisEn.GetValStrByKey("OID"));
					objQuerys.DoQuery();
					for (FrmEleDB single : FrmEleDBs.convertFrmEleDBs(imgdbs)){
						if (single.getFK_MapData().substring(6, single.getFK_MapData().length()-6).equals(img.getEnPK().substring(6, img.getEnPK().length()-6))){
							single.setFK_MapData(img.getEnPK());
							single.setMyPK(img.getEnPK() + "_" + HisEn.GetValStrByKey("OID") + "_" + img.getEnPK());
							single.setRefPKVal(img.getEnPK());
							single.DirectInsert();
							realDB = single;
							break;
						}
					}
				}else{
					realDB = imgDb;
				}
				imgSrc = realDB.getTag1();
				// 如果没有查到记录，控件不显示。说明没有走盖章的一步
				x = img.getX() + wtX;
				this.ui.append("\t\n<DIV id=\""+img.getMyPK()+"\" style='position:absolute;left:"+x +"px;top:"+y +"px;text-align:left;vertical-align:top' >");
				this.ui.append("\t\n<img src='"+imgSrc +"' onerror='javascript:this.src="+basePath +"DataUser/ICON/"+SystemConfig.getCompanyID()+"/LogBiger.png\";' style='padding: 0px;margin: 0px;border-width: 0px;width:"+img.getW()+"px;height:"+img.getH()+"px' />");	
				this.ui.append("\t\n</DIV>");
			}
			//#endregion
		}
	}
	
	/**
	 * 输出编辑框，复选框，下拉菜单
	 */
	private void printBaseDataUI(){
		for (MapAttr attr : MapAttrs.convertMapAttrs(this.mattrs)){
			if (!attr.getUIVisible() && attr.getUIIsEnable()){
				TextBox tbH = this.ui.creatTextBox("TB_" + attr.getKeyOfEn());
				//tbH.Visible = false;
				
				tbH.addAttr("Style", "display:none;");
				tbH.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));
				this.ui.append(tbH);
				continue;
			}
			if (!attr.getUIVisible()){
				continue;
			}
			
			x = attr.getX() + wtX;
			if (attr.getLGType() == FieldTypeS.Enum || attr.getLGType() == FieldTypeS.FK){
				this.ui.append("<DIV id='F" + attr.getKeyOfEn() + "' style='position:absolute; left:" + x + "px; top:" + attr.getY() + "px;  height:16px;text-align: left;word-break: keep-all;' >");
			}
			else{
				this.ui.append("<DIV id='F" + attr.getKeyOfEn() + "' style='position:absolute; left:" + x + "px; top:" + attr.getY() + "px; width:" + attr.getUIWidth() + "px; height:16px;text-align: left;word-break: keep-all;' >");
			}
			
			this.ui.append("<span>");

			//region add contrals.
			if (!attr.getUIIsEnable() && this.LinkFields.contains("," + attr.getKeyOfEn() + ",")){
				Object tempVar3 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.Link);
				MapExt meLink = (MapExt)((tempVar3 instanceof MapExt) ? tempVar3 : null);
				String url = meLink.getTag();
				if (!url.contains("?")){
					url = url + "?a3=2";
				}
				url = url + "&WebUserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&EnName=" + this.EnName;
				if (url.contains("@AppPath")){
					url = url.replace("@AppPath", basePath);
				}
				if (url.contains("@")){
					Attrs attrs = HisEn.getEnMap().getAttrs();
					for (Attr item : attrs){
						url = url.replace("@" + attr.getKeyOfEn(), HisEn.GetValStrByKey(attr.getKeyOfEn()));
						if (!url.contains("@")){
							break;
						}
					}
				}
				this.ui.append("<a href='" + url + "' target='" + meLink.getTag1() + "' >" + HisEn.GetValByKey(attr.getKeyOfEn()) + "</a>");
				this.ui.append("</span>");
				this.ui.append("</DIV>");
				continue;
			}
			
			// region 数字签名
			if (attr.getIsSigan()){
				// region 图片签名 (dai guoqiang)
				if (attr.getSignType() == SignType.Pic){
					boolean isEdit = false; //是否可以编辑签名
					String v = HisEn.GetValStrByKey(attr.getKeyOfEn());
					//如果为空，默认使用当前登录人签名
					if (StringHelper.isNullOrEmpty(v)){
						v = WebUser.getNo();
						//如果为只读并且为空，显示为未签名
						if (this.IsReadonly){
							v = "sigan-readonly";
						}

						if (attr.getPicType() == PicType.ShouDong){
							isEdit = true;
							v = "sigan-readonly";
						}
					}
					if (this.FK_Node != 0 && !this.IsReadonly){
						//获取表单方案，如果为可编辑，则对属性设置为true
						v = HisEn.GetValStrByKey(attr.getKeyOfEn());
						long workId = Long.parseLong(HisEn.GetValStrByKey("OID"));
						FrmField keyOfEn = new FrmField();
						QueryObject info = new QueryObject(keyOfEn);
						info.AddWhere(FrmFieldAttr.FK_Node, this.FK_Node);
						info.addAnd();
						info.AddWhere(FrmFieldAttr.FK_MapData, attr.getFK_MapData());
						info.addAnd();
						info.AddWhere(FrmFieldAttr.KeyOfEn, attr.getKeyOfEn());
						info.addAnd();
						info.AddWhere(MapAttrAttr.UIIsEnable, "1");
						if (info.DoQuery() > 0){
							isEdit = true; //可编辑，如果值为空显示可编辑图片
							if (StringHelper.isNullOrEmpty(v)){
								v = "siganture";
							}
						}
						else{
							//不可编辑，如果值为空显示不可编辑图片
							if (StringHelper.isNullOrEmpty(v)){
								v = "sigan-readonly";
							}
						}
					}
					//如果为可编辑，对签名进行修改
					if (isEdit){
						this.ui.append("<img src='" + basePath + "DataUser/Siganture/" + v + ".jpg' " + "ondblclick=\"SigantureAct(this,'" + WebUser.getNo() + "','" + attr.getFK_MapData() + "','" + attr.getKeyOfEn() + "','" + HisEn.GetValStrByKey("OID") + "');\" border=\"0\" alt=\"双击进行签名或取消签名\" onerror=\"this.src='" + basePath + "DataUser/Siganture/UnName.jpg'\"/>");
					}
					else{
						this.ui.append("<img src='" + basePath + "DataUser/Siganture/" + v + ".jpg' border=0 onerror=\"this.src='" + basePath + "DataUser/Siganture/UnName.jpg'\"/>");
					}

				} //结束图片签名.
				// endregion 结束图片签名
				
				// region CA签名 (song honggang 2014-06-08)
				if (attr.getSignType() == SignType.CA){
					if (!IsAddCa){
						IsAddCa = true;
						scripts.add(basePath+"WF/Activex/Sign/Loadwebsign.js");
						scripts.add(basePath+"WF/Activex/Sign/main.js");
					}

					if (!StringHelper.isNullOrEmpty(attr.getPara_SiganField())){
						//string signClient = GetTBByID("TB_" + attr.Para_SiganField).ClientID;
						String signClient = "";
						if (getPageID().equals("Frm")){
							signClient = "TB_" + attr.getPara_SiganField();
						}
						else{
							signClient = "TB_" + attr.getPara_SiganField();
						}

						this.ui.append("<span id='" + signClient + "sealpostion' />");
						this.ui.append("<img  src='" + basePath + "DataUser/Siganture/setting.JPG' ondblclick=\"addseal('" + signClient + "');\"  border='0' onerror=\"this.src='" + basePath + "DataUser/Siganture/UnName.jpg'\"/>");
					}
				}
				// endregion 结束CA签名
				this.ui.append("</span>");
				this.ui.append("</DIV>");
				continue;
			}
			// endregion
			
			if (attr.getMaxLen() >= 3999 && attr.getTBModel() == 2){
				AddRichTextBox(HisEn, attr);
				this.ui.append("</span>");
				this.ui.append("</DIV>");
				continue;
			}
			
			TextBox tb = null;
			if (attr.getUIContralType() == UIContralType.TB){
				tb = this.ui.creatTextBox("TB_" + attr.getKeyOfEn());
				if (!attr.getUIIsEnable() || this.IsReadonly ){
					tb.setReadOnly(true);
					tb.setCssClass("TBReadonly");
				}else{
					//add by dgq 2013-4-9 添加修改事件
					tb.addAttr("onchange", "Change('" + attr.getFK_MapData() + "');");
				}
				tb.addAttr("tabindex", String.valueOf(attr.getIdx()));
			}
			
			switch (attr.getLGType()){
				case Normal:
					switch (attr.getMyDataType()){
						case BP.DA.DataType.AppString:
							if (attr.getUIRows() == 1){
								if (!StringHelper.isNullOrEmpty(HisEn.GetValStringByKey(attr.getKeyOfEn()))){
									tb.setText(HisEn.GetValStringByKey(attr.getKeyOfEn()));
								}else{
									tb.setText(attr.getDefVal());
								}
								tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 15px;padding: 0px;margin: 0px;");
								if (attr.getUIIsEnable() && !this.IsReadonly){
									tb.setCssClass("TB");
								}
								else{
									tb.setCssClass("TBReadonly");
								}
								this.ui.append(tb);
							}else{
								tb.setTextMode(TextBoxMode.MultiLine);

								if (!StringHelper.isNullOrEmpty(HisEn.GetValStringByKey(attr.getKeyOfEn()))){
									tb.setText(HisEn.GetValStringByKey(attr.getKeyOfEn()));
								}else{
									tb.setText(attr.getDefVal());
								}
								tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left;padding: 0px;margin: 0px;");
								tb.addAttr("maxlength", String.valueOf(attr.getMaxLen()));
								tb.setRows(attr.getUIRows());

								if (attr.getUIIsEnable() && !this.IsReadonly){
									tb.setCssClass("TBDoc");
									tb.addAttr("ondblclick", "TBHelp('" + tb.getId() + "','" + basePath + "','" + this.EnName + "','" + attr.getKeyOfEn() + "');");
								}else{
									tb.setCssClass("TBReadonly");
								}
								this.ui.append(tb);
							}
							break;
						case BP.DA.DataType.AppDate:
							tb.setShowType(TBType.Date);
							tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));
							if (attr.getUIIsEnable() && !this.IsReadonly){
								tb.addAttr("onfocus", "WdatePicker();");
								tb.addAttr("class", "TB");
							}else{
								tb.addAttr("class", "TBReadonly");
							}
							tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 19px;");
							this.ui.append(tb);
							break;
						case BP.DA.DataType.AppDateTime:
							tb.setShowType(TBType.DateTime);
							tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));

							if (attr.getUIIsEnable() && !this.IsReadonly){
								tb.addAttr("class", "TBcalendar");
							}else{
								tb.addAttr("class", "TBReadonly");
							}

							if (attr.getUIIsEnable()){
								tb.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
							}
							tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 19px;");
							this.ui.append(tb);
							break;
						case BP.DA.DataType.AppBoolean:
							CheckBox cb = this.ui.creatCheckBox("CB_" + attr.getKeyOfEn());
							//cb.addAttr("style", "width: 350px");
							cb.setText(attr.getName().trim());
							cb.setChecked(attr.getDefValOfBool());
							cb.setEnabled(attr.getUIIsEnable());
							cb.setChecked(HisEn.GetValBooleanByKey(attr.getKeyOfEn()));
							if (!cb.getEnabled() || this.IsReadonly){
								cb.setEnabled(false);
							}else{
								//add by dgq 2013-4-9,添加内容修改后的事件
								tb.addAttr("onmousedown", "Change('" + attr.getFK_MapData() + "')");
								cb.setEnabled(true);
							}
							this.ui.append(cb);
							break;
						case BP.DA.DataType.AppDouble:
						case BP.DA.DataType.AppFloat:
							tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;word-break: keep-all;");
							tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));

							if (attr.getUIIsEnable() && !this.IsReadonly){
								//增加验证
								//tb.Attributes.Add("onkeyup", @"value=value.replace(/[^-?\d+\.*\d*$]/g,'');Change('" + attr.FK_MapData + "');");
								tb.addAttr("onkeyup", "Change('" + attr.getFK_MapData() + "');");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,0);");
								tb.addAttr("onClick", "TB_ClickNum(this)");
								tb.addAttr("onkeydown", "VirtyNum(this)");
								tb.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'float');");
								tb.addAttr("class", "TBNum");
							}else{
								tb.addAttr("class", "TBReadonly");
							}
							this.ui.append(tb);
							break;
						case BP.DA.DataType.AppInt:
							// tb.ShowType = TBType.Num;
							tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;word-break: keep-all;");
							tb.setText(HisEn.GetValStrByKey(attr.getKeyOfEn()));
							if (attr.getUIIsEnable() && !this.IsReadonly){
								//增加验证
								tb.addAttr("onkeyup", "Change('" + attr.getFK_MapData() + "');");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d]/g,'');TB_ClickNum(this,0);");
								tb.addAttr("onClick", "TB_ClickNum(this)");
								tb.addAttr("onkeydown", "VirtyNum(this)");
								tb.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'int');");
								tb.addAttr("class", "TBNum");
							}else{
								tb.addAttr("class", "TBReadonly");
							}
							this.ui.append(tb);
							break;
						case BP.DA.DataType.AppMoney:
							tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;");

							if (attr.getUIIsEnable() && !this.IsReadonly){
								//增加验证
								tb.addAttr("onkeyup", "Change('" + attr.getFK_MapData() + "');");
								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,'0.00');");
								tb.addAttr("onClick", "TB_ClickNum(this)");
								tb.addAttr("onkeydown", "VirtyNum(this)");
								tb.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'float');");
								tb.addAttr("class", "TBNum");
							}else{
								tb.addAttr("class", "TBReadonly");
							}

							if (SystemConfig.getAppSettings().get("IsEnableNull").equals("1")){
								BigDecimal v = HisEn.GetValMoneyByKey(attr.getKeyOfEn());
								if (v.equals(567567567)){
									tb.setText("");
								}else{
									tb.setText(decimalFormat(v));
								}
							}else{
								tb.setText(decimalFormat(HisEn.GetValMoneyByKey(attr.getKeyOfEn())));
							}

							this.ui.append(tb);
							break;
						case BP.DA.DataType.AppRate:
							if (attr.getUIIsEnable() && !this.IsReadonly){
								tb.addAttr("class", "TBNum");
							}else{
								tb.addAttr("class", "TBReadonly");
							}
							tb.setShowType(TBType.Moneny);
							tb.setText(decimalFormat(HisEn.GetValMoneyByKey(attr.getKeyOfEn())));
							tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;");
							//增加验证
							//tb.Attributes.Add("onkeyup", @"value=value.replace(/[^-?\d+\.*\d*$]/g,'')");
							tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'')");
							this.ui.append(tb);
							break;
						default:
							break;
					}
					break;
				case Enum:
					if (attr.getUIContralType() == UIContralType.DDL){
						DDL ddle = this.ui.creatDDL("DDL_" + attr.getKeyOfEn());
						ddle.BindSysEnum(attr.getUIBindKey());
						ddle.SetSelectItem(HisEn.GetValStrByKey(attr.getKeyOfEn()));
						ddle.setEnabled(attr.getUIIsEnable());
						ddle.addAttr("tabindex", String.valueOf(attr.getIdx()));
						if (attr.getUIIsEnable()){
							//add by dgq 2013-4-9,添加内容修改后的事件
							ddle.addAttr("onchange", "Change('" + attr.getFK_MapData() + "')");
						}
						if (ddle.getEnabled()  && this.IsReadonly ){
							ddle.setEnabled(false);
						}
						this.ui.append(ddle);
					}else{
						//BP.Sys.FrmRBs rbs = new FrmRBs();
						//rbs.Retrieve(FrmRBAttr.FK_MapData, enName,
						//    FrmRBAttr.KeyOfEn, attr.KeyOfEn);
					}
					break;
				case FK:
					DDL ddl1 = this.ui.creatDDL("DDL_" + attr.getKeyOfEn());
					ddl1.addAttr("tabindex", String.valueOf(attr.getIdx()));
					ddl1.setEnabled(attr.getUIIsEnable());
					if (ddl1.getEnabled()){
						EntitiesNoName ens = attr.getHisEntitiesNoName();
						ens.RetrieveAll();
						ddl1.BindEntities(ens);

						ddl1.Items.add(new ListItem("请选择", ""));

						String val = HisEn.GetValStrByKey(attr.getKeyOfEn());
						if (StringHelper.isNullOrEmpty(val)){
							ddl1.SetSelectItem("");
						}else{
							ddl1.SetSelectItem(val);
						}
						//add by dgq 2013-4-9,添加内容修改后的事件
						ddl1.addAttr("onchange", "Change('" + attr.getFK_MapData() + "')");
					}else{
						// ddl1.Attributes["style"] = "width: " + attr.UIWidth + "px;height: 19px;";
						if (ddl1.getEnabled()  && this.IsReadonly ){
							ddl1.setEnabled(false);
						}
						ddl1.addAttr("Width", String.valueOf(attr.getUIWidth()));
						ddl1.Items.add(new ListItem(HisEn.GetValRefTextByKey(attr.getKeyOfEn()), HisEn.GetValStrByKey(attr.getKeyOfEn())));
					}

					if (attr.getUIIsEnable()  && this.IsReadonly ){
						ddl1.setEnabled(false);
					}
					this.ui.append(ddl1);
					break;
				default:
					break;
			}
			//#endregion add contrals.
			
			this.ui.append("</span>");
			this.ui.append("</DIV>");
		}
	}
	
	/**
	 * 单选按钮
	 * @param mattrs
	 */
	private void printFrmRBs(){
		FrmRBs myrbs = this.mapData.getFrmRBs();
		MapAttr attrRB = new MapAttr();
		for (FrmRB rb : FrmRBs.convertFrmRBs(myrbs)){
			this.x = rb.getX() + wtX;
			this.ui.append("<DIV id='F" + rb.getMyPK() + "' style='position:absolute; left:" + x + "px; top:" + rb.getY() + "px; width:100%; height:16px;text-align: left;word-break: keep-all;' >");
			this.ui.append("<span style='word-break: keep-all;font-size:12px;'>");

			RadioButton rbCtl = this.ui.creatRadioButton("RB_" + rb.getKeyOfEn() + "_" + rb.getIntKey());
			rbCtl.setName(rb.getKeyOfEn());
			rbCtl.setText(rb.getLab());
			this.ui.append(rbCtl);

			if (!attrRB.getKeyOfEn().equals(rb.getKeyOfEn())){
				for (MapAttr ma : MapAttrs.convertMapAttrs(mattrs)){
					if (ma.getKeyOfEn().equals(rb.getKeyOfEn())){
						attrRB = ma;
						break;
					}
				}
			}
			
			if (this.IsReadonly  || !attrRB.getUIIsEnable()){
				rbCtl.setEnabled(false);
			}else{
				//add by dgq 2013-4-9,添加内容修改后的事件
				rbCtl.addAttr("onmousedown", "Change('" + attrRB.getFK_MapData() + "')");
			}
			
			this.ui.append("</span>");
			this.ui.append("</DIV>");
		}
		
		for(MapAttr attr : MapAttrs.convertMapAttrs(mattrs)){
            if (attr.getUIContralType() == UIContralType.RadioBtn){
            	String id = "RB_" + attr.getKeyOfEn() + "_" + HisEn.GetValStrByKey(attr.getKeyOfEn());
                Object ctl = this.ui.GetUIByID(id);
                if (ctl != null){
                	RadioButton rb = (RadioButton) ctl;
                    rb.setChecked(true);
                }
            }
        }
		// endregion 输出 rb.
	}
	
	/**
	 * 输出明细表
	 */
	private void printFrmDtls(){
		for (MapDtl dtl : MapDtls.convertMapDtls(this.dtls)){
			if (!dtl.getIsView())
				continue;
			
			this.x = dtl.getX() + wtX;
			float y = dtl.getY();
			this.ui.append("<DIV id='Fd"+dtl.getNo()+"' style='position:absolute; left:"+x+"px; top:"+y+"px; width:"+dtl.getW()+"px; height:"+dtl.getH()+"px;text-align: left;' >");
			this.ui.append("<span>");
			
			String src = "";
			if (dtl.getHisDtlShowModel() == DtlShowModel.Table){
				if (this.IsReadonly){
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=1&FID=" + HisEn.GetValStrByKey("FID", "0") + "&FK_Node=" + this.FK_Node;
				} else{
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=0&FID=" + HisEn.GetValStrByKey("FID", "0") + "&FK_Node=" + this.FK_Node;
				}
			} else{
				if (this.IsReadonly){
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName=" + dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=1&FID=" + HisEn.GetValStrByKey("FID", "0");
				} else{
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName=" + dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=0&FID=" + HisEn.GetValStrByKey("FID", "0");
				}
			}
			
			if (this.IsReadonly || dtl.getIsReadonly()){
				this.ui.append("<iframe ID='F"+dtl.getNo()+"' src='"+src+"' frameborder=0  style='position:absolute;width:"+dtl.getW()+"px; height:"+dtl.getH()+"px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			} else{
				AddLoadFunction(dtl.getNo(), "blur", "SaveDtl");
				
				//this.Add("<iframe ID='F" + dtl.No + "' Onblur=\"SaveDtl('" + dtl.No + "');\"  src='" + src + "' frameborder=0  style='position:absolute;width:" + dtl.W + "px; height:" + dtl.H + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
				this.ui.append("<iframe ID='F"+dtl.getNo()+"' onload= '"+dtl.getNo()+"load();'  src='"+src+"' frameborder=0  style='position:absolute;width:"+dtl.getW()+"px; height:"+dtl.getH()+"px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			}
			
			this.ui.append("</span></DIV>");
		}
		
		String js = "";
		if (!this.IsReadonly){
			js = "\t\n<script type='text/javascript' >";
			js += "\t\n function SaveDtl(dtl) { ";
			js += "\t\n   GenerPageKVs(); //调用产生kvs ";
			js += "\t\n   document.getElementById('F' + dtl ).contentWindow.SaveDtlData();";
			js += "\t\n } ";

			js += "\t\n function SaveM2M(dtl) { ";
			js += "\t\n   document.getElementById('F' + dtl ).contentWindow.SaveM2M();";
			js += "\t\n } ";

			js += "\t\n</script>";
			this.ui.append(js);
		}
	}
	
	/**
	 * 输出报表
	 */
	private void printFrmRpts(){
		for (FrmRpt rpt : FrmRpts.convertFrmRpts(mapData.getFrmRpts())){
			if (!rpt.getIsView())
				continue;
			
			this.x = rpt.getX() + wtX;
			float y = rpt.getY();
			
			this.ui.append("<DIV id='Fd" + rpt.getNo() + "' style='position:absolute; left:" + x + "px; top:" + y + "px; width:" + rpt.getW() + "px; height:" + rpt.getH() + "px;text-align: left;' >");
			this.ui.append("<span>");
			
			String src = "";
			if (rpt.getHisDtlShowModel() == DtlShowModel.Table){
				if (this.IsReadonly){
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=1&FID=" + HisEn.GetValStrByKey("FID", "0");
				} else{
					src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=0&FID=" + HisEn.GetValStrByKey("FID", "0");
				}
			}else	{
				if (this.IsReadonly){
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName=" + rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=1&FID=" + HisEn.GetValStrByKey("FID", "0");
				} else{
					src = basePath + "WF/CCForm/DtlCard.jsp?EnsName=" + rpt.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsReadonly=0&FID=" + HisEn.GetValStrByKey("FID", "0");
				}
			}
			
			if (this.IsReadonly || rpt.getIsReadonly()){
				this.ui.append("<iframe ID='F" + rpt.getNo() + "' src='" + src + "' frameborder=0  style='position:absolute;width:" + rpt.getW() + "px; height:" + rpt.getH() + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			} else{
				AddLoadFunction(rpt.getNo(), "blur", "SaveDtl");

				//Add("<iframe ID='F" + rpt.No + "' Onblur=\"SaveDtl('" + rpt.No + "');\"  src='" + src + "' frameborder=0  style='position:absolute;width:" + rpt.W + "px; height:" + rpt.H + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
				this.ui.append("<iframe ID='F" + rpt.getNo() + "' onload='" + rpt.getNo() + "load();'  src='" + src + "' frameborder=0  style='position:absolute;width:" + rpt.getW() + "px; height:" + rpt.getH() + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
			}
			
			this.ui.append("</span>");
			this.ui.append("</DIV>");
		}
	}
	
	/**
	 * 输出审核组件
	 */
	private void printFrmWorkCheck(FrmWorkCheck fwc){
		if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable){
			this.x = fwc.getFWC_X() + wtX;
			this.ui.append("<DIV id='FWC" + fwc.getNo() + "' style='position:absolute; left:" + x + "px; top:" + fwc.getFWC_Y() + "px; width:" + fwc.getFWC_W() + "px; height:" + fwc.getFWC_H() + "px;text-align: left;' >");
			this.ui.append("<span>");
			String src = basePath + "WF/WorkOpt/WorkCheck.jsp?s=2";
			String paras = paramsStr;
			try{
				if (!paras.contains("FID=")){
					paras += "&FID=" + HisEn.GetValStrByKey("FID");
				}
			}catch (java.lang.Exception e){}
			
			if (!paras.contains("OID=")){
				paras += "&OID=" + HisEn.GetValStrByKey("OID");
			}
			if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Readonly){
				src += "&DoType=View";
			}
			src += "&r=q" + paras;
			this.ui.append("<iframe ID='F33" + fwc.getNo() + "'  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + fwc.getFWC_W() + "' height='" + fwc.getFWC_H() + "'   scrolling=auto/></iframe>");
			this.ui.append("</span>");
			this.ui.append("</DIV>");
		}
	}
	
	/**
	 * 父子流程组件
	 */
	private void printSubFLow(FrmWorkCheck fwc){
		Entity en = this.HisEn;
        FrmSubFlow subFlow = new FrmSubFlow(this.EnName);
        if (subFlow.getHisFrmSubFlowSta() != FrmSubFlowSta.Disable)
        {
            x = subFlow.getSF_X() + wtX;
            this.ui.append("<DIV id='DIVWC" + fwc.getNo() + "' style='position:absolute; left:" + x + "px; top:" 
            + subFlow.getSF_Y() + "px; width:" + subFlow.getSF_W() + "px; height:" + subFlow.getSF_H() + "px;text-align: left;' >");
            this.ui.append("<span>");
            
            String src = basePath + "WF/WorkOpt/SubFlow.jsp?s=2";
            String fwcOnload = "";
            String paras = paramsStr;
                if (paras.contains("FID=") == false && en.getEnMap().getAttrs().Contains("FID")==true )
                    paras += "&FID=" + en.GetValStrByKey("FID");

            if (paras.contains("OID=") == false)
                paras += "&OID=" + en.GetValStrByKey("OID");
            if (subFlow.getHisFrmSubFlowSta() == FrmSubFlowSta.Readonly)
            {
                src += "&DoType=View";
            }
            src += "&r=q" + paras;
            this.ui.append("<iframe ID='FWC" + subFlow.getNo() + "' " + fwcOnload 
            		+ "  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" 
            		+ subFlow.getSF_W() + "' height='" + subFlow.getSF_H() + "'   scrolling=auto/></iframe>");

            this.ui.append("</span>");
            this.ui.append("</DIV>");
        }
	}
	
	/**
	 * 输出多对多业务
	 */
	private void printMapM2Ms(){
		for (MapM2M m2m : MapM2Ms.convertMapM2Ms(this.m2ms)){
			this.x = m2m.getX() + wtX;
			this.ui.append("<DIV id='Fd" + m2m.getNoOfObj() + "' style='position:absolute; left:" +x + "px; top:" + m2m.getY() + "px; width:" + m2m.getW() + "px; height:" + m2m.getH() + "px;text-align: left;' >");
			this.ui.append("<span>");

			String src = ".jsp?NoOfObj=" + m2m.getNoOfObj();
			String paras = paramsStr;
			try{
				if (!paras.contains("FID=")){
					paras += "&FID=" + HisEn.GetValStrByKey("FID");
				}
			}catch (java.lang.Exception e2){}
			
			if (!paras.contains("OID=")){
				paras += "&OID=" + HisEn.GetValStrByKey("OID");
			}
			src += "&r=q" + paras;
			if (m2m.getIsEdit()){
				src += "&IsEdit=1";
			}else{
				src += "&IsEdit=0";
			}

			if (!src.contains("FK_MapData")){
				src += "&FK_MapData=" + this.EnName;
			}
			if (m2m.getHisM2MType() == M2MType.M2MM){
				src = basePath + "WF/CCForm/M2MM" + src;
			}else{
				src = basePath + "WF/CCForm/M2M" + src;
			}

			switch (m2m.getShowWay()){
				case FrmAutoSize:
				case FrmSpecSize:
					if (m2m.getIsEdit()){
						AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

						// Add("<iframe ID='F" + m2m.NoOfObj + "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.W + "' height='" + m2m.H + "'   scrolling=auto/></iframe>");
						this.ui.append("<iframe ID='F" + m2m.getNoOfObj() + "'  onload='" + m2m.getNoOfObj() + "load();'  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.getW() + "' height='" + m2m.getH() + "'   scrolling=auto/></iframe>");

					}else{
						this.ui.append("<iframe ID='F" + m2m.getNoOfObj() + "'  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.getW() + "' height='" + m2m.getH() + "'   scrolling=auto/></iframe>");
					}
					break;
				case Hidden:
					break;
				case WinOpen:
					this.ui.append("<a href=\"javascript:WinOpen('" + src + "&IsOpen=1','" + m2m.getW() + "','" + m2m.getH() + "');\"  />" + m2m.getName() + "</a>");
					break;
				default:
					break;
			}
			
			this.ui.append("</span>");
			this.ui.append("</DIV>");
		}
	}
	
	/**
	 * 输出附件
	 */
	private void printFrmAttachments(){
		FrmAttachments aths = mapData.getFrmAttachments();
		FrmAttachmentDBs athDBs = null;
		if (aths.size() > 0){
			athDBs = new FrmAttachmentDBs(this.EnName, HisEn.getPKVal().toString());
		}
		
		for (FrmAttachment ath : FrmAttachments.convertFrmAttachments(aths)){
			if (ath.getUploadType() == AttachmentUploadType.Single){
				// 单个文件 
				//Object tempVar4 = athDBs.GetEntityByKey(FrmAttachmentDBAttr.FK_FrmAttachment, ath.getMyPK());
				//FrmAttachmentDB athDB = (FrmAttachmentDB)((tempVar4 instanceof FrmAttachmentDB) ? tempVar4 : null);
				this.x = ath.getX() + wtX;
				float y = ath.getY();
				
				this.ui.append("<DIV id='Fa" + ath.getMyPK() + "' style='position:absolute;width:400px; left:" + x + "px; top:" + y + "px; text-align: left;float:left' >");
				this.ui.append("<span>");
				if (ath.getIsUpload() && !this.IsReadonly ){
					String src = Glo.getCCFlowAppPath()+"WF/CCForm/SingleAttachmentUpload.jsp?HFK_Node="+this.FK_Node+"&OID="+HisEn.GetValStrByKey("OID")+"&EnName="+this.EnName+"&PKVal=" + HisEn.getPKVal() + "&Ath=" + ath.getNoOfObj() + "&FK_FrmAttachment=" + ath.getMyPK() + paramsStr;
					float h = ath.getH() <= 0 ? 50: ath.getH();
					this.ui.append("<iframe id='F" + ath.getMyPK()+"' src=\""+src+"\"frameborder=0  style='position:absolute;text-align: left;width:500px;height:"+h+"px'    leftMargin='0'  topMargin='0' scrolling=auto");
					this.ui.append("</iframe>");
				}
				this.ui.append("</span>");
				this.ui.append("</DIV>");
			}
			
			if (ath.getUploadType() == AttachmentUploadType.Multi){
				this.x = ath.getX() + wtX;
				this.ui.append("<DIV id='Fd" + ath.getMyPK() + "' style='position:absolute; left:" + x + "px; top:" + ath.getY() + "px; width:" + ath.getW() + "px; height:" + ath.getH() + "px;text-align: left;' >");
				this.ui.append("<span>");
				String src = "";
				if (this.IsReadonly){
					src = basePath + "WF/CCForm/AttachmentUpload.jsp?PKVal=" + HisEn.getPKVal() + "&Ath=" + ath.getNoOfObj() + "&FK_FrmAttachment=" + ath.getMyPK() + "&IsReadonly=1" + paramsStr;
				} else{
					src = basePath + "WF/CCForm/AttachmentUpload.jsp?PKVal=" + HisEn.getPKVal() + "&Ath=" + ath.getNoOfObj() + "&FK_FrmAttachment=" + ath.getMyPK() + paramsStr;
				}

				this.ui.append("<iframe ID='F" + ath.getMyPK() + "'    src='" + src + "' frameborder=0  style='position:absolute;width:" + ath.getW() + "px; height:" + ath.getH() + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
				this.ui.append("</span>");
				this.ui.append("</DIV>");
			}
		}
		// endregion 输出附件.
		
		// region 输出 img 附件
		FrmImgAths imgAths = mapData.getFrmImgAths();
		if (imgAths.size() != 0 && !this.IsReadonly){
			String js = "\t\n<script type='text/javascript' >";
			js += "\t\n function ImgAth(url, athMyPK)";
			js += "\t\n {";
			js += "\t\n  var v= window.showModalDialog(url, 'ddf', 'dialogHeight: 650px; dialogWidth: 950px;center: yes; help: no'); ";
			js += "\t\n  if (v==null )  ";
			js += "\t\n     return ;";
			js += "\t\n document.getElementById('Img'+athMyPK ).setAttribute('src', v); ";
			js += "\t\n }";
			js += "\t\n</script>";
			this.ui.append(js);
		}
		
		for (FrmImgAth ath : FrmImgAths.convertFrmImgAths(imgAths)){
			this.x = ath.getX() + wtX;
			this.ui.append("\t\n<DIV id=" + ath.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + ath.getY() + "px;text-align:left;vertical-align:top' >");
			String url = basePath + "WF/CCForm/ImgAth.jsp?W=" + ath.getW() + "&H=" + ath.getH() + "&FK_MapData=" + this.EnName + "&MyPK=" + HisEn.getPKVal() + "&ImgAth=" + ath.getMyPK();
			if (!this.IsReadonly  && ath.getIsEdit())
			{
				//warning this.ui.append(BaseModel.AddFieldSet("<a href=\"javascript:ImgAth('" + url + "','" + ath.getMyPK() + "');\" >编辑</a>"));
				this.ui.append(BaseModel.AddFieldSet("<a href=\"#\" >编辑</a>"));
			}

			FrmImgAthDB imgAthDb = new FrmImgAthDB();
			imgAthDb.setMyPK(ath.getMyPK() + "_" + HisEn.getPKVal());
			imgAthDb.RetrieveFromDBSources();
			if (imgAthDb != null && !StringHelper.isNullOrEmpty(imgAthDb.getFileName()))
			{
				this.ui.append("\t\n<img src='" + basePath + "DataUser/ImgAth/Data/" + imgAthDb.getFileName() + ".png' onerror=\"this.src='" + basePath + "WF/Data/Img/LogH.PNG'\" name='Img" + ath.getMyPK() + "' id='Img" + ath.getMyPK() + "' style='padding: 0px;margin: 0px;border-width: 0px;' width=" + ath.getW() + " height=" + ath.getH() + " />");
			}
			else
			{
				this.ui.append("\t\n<img src='" + basePath + "DataUser/ImgAth/Data/" + ath.getMyPK() + "_" + HisEn.getPKVal() + ".png' onerror=\"this.src='" + basePath + "WF/Data/Img/LogH.PNG'\" name='Img" + ath.getMyPK() + "' id='Img" + ath.getMyPK() + "' style='padding: 0px;margin: 0px;border-width: 0px;' width=" + ath.getW() + " height=" + ath.getH() + " />");
			}
			if (!this.IsReadonly && ath.getIsEdit())
			{
				this.ui.append(BaseModel.AddFieldSetEnd());
			}
			this.ui.append("\t\n</DIV>");
		}
		// region 输出 img 附件
	}
	
	private void AfterBindEn_DealMapExt(String enName, MapAttrs mattrs, Entity en){
		// region 处理事件.
		if (dtls.size() >= 1){
			String scriptSaveDtl = "";
			scriptSaveDtl = "\t\n<script type='text/javascript' >";
			scriptSaveDtl += "\t\n function SaveDtlAll(){ ";
			for (MapDtl dtl : MapDtls.convertMapDtls(dtls)){
				if (dtl.getIsUpdate()|| dtl.getIsInsert()){
					scriptSaveDtl += "\t\n try{  ";

					if (dtl.getHisDtlShowModel() == DtlShowModel.Table){
						scriptSaveDtl += "\t\n  SaveDtl('" + dtl.getNo() + "'); ";
					}

					scriptSaveDtl += "\t\n } catch(e) { ";
					scriptSaveDtl += "\t\n  alert(e.name  + e.message);  return false;";
					scriptSaveDtl += "\t\n } ";
				}
			}
			
			scriptSaveDtl += "\t\n  return true; } ";
			scriptSaveDtl += "\t\n</script>";

			this.ui.append(scriptSaveDtl);
		}else{
			String scriptSaveDtl = "";
			scriptSaveDtl = "\t\n<script type='text/javascript' >";
			scriptSaveDtl += "\t\n function SaveDtlAll() { ";
			scriptSaveDtl += "\t\n return true; } ";
			scriptSaveDtl += "\t\n</script>";
			this.ui.append(scriptSaveDtl);
		}
		
		fes = this.mapData.getFrmEvents();
//		if (!this.IsPostBack){
//			try{
//				String msg = fes.DoEventNode(FrmEventList.FrmLoadAfter, en);
//				if (msg != null){
//					this.Alert(msg);
//				}
//			}catch (RuntimeException ex){
//				this.Alert("载入之前错误:" + ex.getMessage());
//				return;
//			}
//		}
		
		 //#region 处理扩展设置
		if (mes.size() != 0){
			
			scripts.add(basePath+"DataUser/JSLibData/" + enName + ".js");
			
			this.ui.append("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");

			// region 首先处理自动填充，下拉框数据。
			for (MapExt me : MapExts.convertMapExts(mes)){
				// 自动填充下拉框.
				if(me.getExtType().equals(MapExtXmlList.AutoFullDLL)){
					Object full = this.ui.GetUIByID("DDL_" + me.getAttrOfOper());
					if(null == full){
						me.Delete();
						continue;
					}
					DDL ddlFull = (DDL) full;
					String valOld = ddlFull.getSelectedItemStringVal();
					Object tempVar = me.getDoc();
					String fullSQL = (String)((tempVar instanceof String) ? tempVar : null);
					if (!IsLoadData){
						//替换保存的时候EN中表单中变量
						for (Attr item : en.getEnMap().getAttrs()){
							if (fullSQL.contains("@" + item.getKey() + ";")){
								Enumeration enumeration = get_request().getParameterNames();
								while(enumeration.hasMoreElements()){
									String key = (String)enumeration.nextElement();
									if (key.endsWith(item.getKey())){
										en.SetValByKey(item.getKey(), get_request().getParameter(key));
									}
								}
							}
						}
					}
					
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, "");

					ddlFull.Items.clear();
					DataTable table = DBAccess.RunSQLReturnTable(fullSQL);
					if(table.Rows.size() == 0){
						DataRow row = table.NewRow();
						row.setValue("No", "");
						row.setValue("Name", "*请选择");
						table.Rows.add(row);

					}

					ddlFull.Bind(table, "No", "Name");

					String val = "";
					if (!IsLoadData){
						Enumeration enumeration = get_request().getParameterNames();
						while(enumeration.hasMoreElements()){
							String key = (String)enumeration.nextElement();
							if (key.endsWith(me.getAttrOfOper()))
							{
								val = get_request().getParameter(key);
							}
						}
					}
					if (StringHelper.isNullOrEmpty(val)){
						ddlFull.SetSelectItem(en.GetValStrByKey(me.getAttrOfOper()));
					}else{
						ddlFull.SetSelectItem(val);
					}
				}
			}
			// endregion 首先处理自动填充，下拉框数据。
			
			// region 在处理其它。
			DataTable dt = new DataTable();
			for (MapExt me : MapExts.convertMapExts(mes)){
				// 自动填充其他的控件..
				if(me.getExtType().equals(MapExtXmlList.DDLFullCtrl)){
					Object oper = this.ui.GetUIByID("DDL_" + me.getAttrOfOper());
					if(null == oper)
						continue;
					DDL ddlOper = (DDL) oper;
					ddlOper.addAttr("onchange", "DDLFullCtrl(this.value,\'" + ddlOper.getId() + "\', \'" + me.getMyPK() + "\')");
					if (!me.getTag().equals("")){
						// 处理下拉框的选择范围的问题 
						String[] strs = me.getTag().split("\\$");
						for (String str : strs){
							String[] myCtl = str.split("\\:");
							String ctlID = myCtl[0];
							Object c1 = this.ui.GetUIByID("DDL_" + ctlID);
							if(null == c1){
								//me.Tag = "";
								//me.Update();
								continue;
							}
							DDL ddlC1 = (DDL) c1;
							String sql = myCtl[1].replace("~", "'");
							sql = BP.WF.Glo.DealExp(sql, en, null);
							sql = sql.replace("@Key", ddlOper.getSelectedItemStringVal().trim());
							
							//sql = sql.Replace("WebUser.No", WebUser.getNo());
							//sql = sql.Replace("@WebUser.Name", WebUser.Name);
							//sql = sql.Replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
							//sql = sql.Replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
							//sql = sql.Replace("@Key", ddlOper.SelectedItemStringVal.Trim());
							//if (sql.Contains("@"))
							//{
							//    foreach (MapAttr attr in mattrs)
							//    {
							//        if (sql.Contains("@" + attr.KeyOfEn) == false)
							//            continue;
							//        sql = sql.Replace("@" + attr.KeyOfEn, en.GetValStrByKey(attr.KeyOfEn));
							//        if (sql.Contains("@") == false)
							//            break;
							//    }
							//}
							
							dt = DBAccess.RunSQLReturnTable(sql);
							String valC1 = ddlC1.getSelectedItemStringVal();
							ddlC1.Items.clear();
							for (DataRow dr : dt.Rows){
								ddlC1.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(1).toString()));
							}
							ddlC1.SetSelectItem(valC1);
						}
					}
				}else if(me.getExtType().equals(MapExtXmlList.ActiveDDL)){// 自动初始化ddl的下拉框数据
					Object perant = this.ui.GetUIByID("DDL_" + me.getAttrOfOper());
					Object child = this.ui.GetUIByID("DDL_" + me.getAttrsOfActive());
					if (perant == null || child == null){
						continue;
					}
					DDL ddlPerant = (DDL) perant;
					DDL ddlChild = (DDL) child;
					ddlPerant.addAttr("onchange", "DDLAnsc(this.value,\'" + ddlChild.getId() + "\', \'" + me.getMyPK() + "\')");
					String val = ddlPerant.getSelectedItemStringVal();
					String valClient = en.GetValStrByKey(me.getAttrsOfActive()); // ddlChild.SelectedItemStringVal;
					
					Object tempVar2 = me.getDoc();
					String fullSQL = (String)((tempVar2 instanceof String) ? tempVar2 : null);
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = fullSQL.replace("@Key", val);
					fullSQL = fullSQL.replace("WebUser.No", WebUser.getNo());
					fullSQL = fullSQL.replace("@WebUser.Name", WebUser.getName());
					fullSQL = fullSQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
					
					dt = DBAccess.RunSQLReturnTable(fullSQL);
					// ddlChild.Items.Clear();
					for (DataRow dr : dt.Rows){
						ddlChild.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
					}
					ddlChild.SetSelectItem(valClient);
				}else if(me.getExtType().equals(MapExtXmlList.AutoFullDLL)){// 自动填充下拉框
					continue;
				}else if(me.getExtType().equals(MapExtXmlList.TBFullCtrl)){// 自动填充
					Object auto = this.ui.GetUIByID("TB_" + me.getAttrOfOper());
					if (auto == null){
						continue;
					}
					TextBox tbAuto = (TextBox) auto;
					
					// onpropertychange
					// tbAuto.Attributes["onpropertychange"] = "DoAnscToFillDiv(this,this.value,\'" + tbAuto.ClientID + "\', \'" + me.MyPK + "\');";
					// tbAuto.Attributes["onkeydown"] = "DoAnscToFillDiv(this,this.value,\'" + tbAuto.ClientID + "\', \'" + me.MyPK + "\');";
					// tbAuto.Attributes["onkeyup"] = "DoAnscToFillDiv(this,this.value,\'" + tbAuto.ClientID + "\', \'" + me.MyPK + "\');";
					// tbAuto.Attributes["ondblclick"] = "ReturnValTBFullCtrl(this,'" + me.MyPK + "','sd');";
					tbAuto.addAttr("ondblclick", "ReturnValTBFullCtrl(this,'" + me.getMyPK() + "');");
					tbAuto.addAttr("onkeyup", "DoAnscToFillDiv(this,this.value,\'" + tbAuto.getId() + "\', \'" + me.getMyPK() + "\');");
					tbAuto.addAttr("AUTOCOMPLETE", "OFF");
					if (!me.getTag().equals("")){
						// 处理下拉框的选择范围的问题 
						String[] strs = me.getTag().split("\\$");
						for (String str : strs){
							String[] myCtl = str.split("\\:");
							String ctlID = myCtl[0];
							Object c1 = this.ui.GetUIByID("DDL_" + ctlID);
							if(null == c1){
								//me.Tag = "";
								//me.Update();
								continue;
							}
							DDL ddlC1 = (DDL) c1;
							String sql = myCtl[1].replace("~", "'");

							String txt = tbAuto.getText().trim();
							//if (string.IsNullOrEmpty(txt))
							//    txt = "$";

							sql = sql.replace("@Key", txt);
							sql = BP.WF.Glo.DealExp(sql, en, null);

							//sql = sql.Replace("WebUser.No", WebUser.getNo());
							//sql = sql.Replace("@WebUser.Name", WebUser.Name);
							//sql = sql.Replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
							//sql = sql.Replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
							//if (sql.Contains("@"))
							//{
							//    foreach (MapAttr attr in mattrs)
							//    {
							//        if (sql.Contains("@" + attr.KeyOfEn) == false)
							//            continue;
							//        sql = sql.Replace("@" + attr.KeyOfEn, en.GetValStrByKey(attr.KeyOfEn));
							//        if (sql.Contains("@") == false)
							//            break;
							//    }
							//}
							
							try{
								dt = DBAccess.RunSQLReturnTable(sql);
							}
							catch (Exception ex){
								this.ui.getTmpList().clear();
								this.ui.append(BaseModel.AddFieldSet("配置错误"));
								this.ui.append(me.ToStringAtParas() + "<hr>错误信息:<br>" + ex.getMessage());
								this.ui.append(BaseModel.AddFieldSetEnd());
								return;
							}
							
							String valC1 = ddlC1.getSelectedItemStringVal();
							ddlC1.Items.clear();
							for (DataRow dr : dt.Rows){
								ddlC1.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
							}
							ddlC1.SetSelectItem(valC1);
						}
					}
				}else if(me.getExtType().equals(MapExtXmlList.InputCheck)){
					Object js = this.ui.GetUIByID("TB_" + me.getAttrOfOper());
					if(null != js){
						TextBox tbJS = (TextBox) js;
						tbJS.addAttr(me.getTag2(), me.getTag1() + "(this);");
					}else{
						Object dl = this.ui.GetUIByID("DDL_" + me.getAttrOfOper());
						if (dl != null){
							 DDL ddl = (DDL) dl;
							 ddl.addAttr(me.getTag2(), me.getTag1() + "(this);");
						}
					}
				}else if(me.getExtType().equals(MapExtXmlList.PopVal)){// 弹出窗
					Object b = this.ui.GetUIByID("TB_" + me.getAttrOfOper());
					if (null == b){
						continue;
					}
					TextBox tb = (TextBox) b;
					
					//移除常用词汇事件
					if (tb.getRows() > 1){
						tb.attributes.remove("ondblclick");
					}
					if (!tb.getCssClass().equals("TBReadonly")){
						if (me.getPopValWorkModel() == 0){
							tb.addAttr("ondblclick", "ReturnVal(this,'" + BP.WF.Glo.DealExp(me.getDoc(), en, null) + "','sd');");
						}else{
							tb.addAttr("ondblclick", "ReturnValCCFormPopVal(this,'" + me.getMyPK() + "','" + en.getPKVal() + "');");
						}
					}
				}else if(me.getExtType().equals(MapExtXmlList.RegularExpression)){//正则表达式,对数据控件处理
					Object exp = this.ui.GetUIByID("TB_" + me.getAttrOfOper());
					if(null == exp)
						exp = this.ui.GetUIByID("CB_" + me.getAttrOfOper());
					if(null == exp)
						exp = this.ui.GetUIByID("DDL_" + me.getAttrOfOper());
					
					if(null == exp || me.getTag().equals("onsubmit"))continue;
					
					BaseWebControl tbExp = (BaseWebControl) exp;
					//验证输入的正则格式
					String regFilter = me.getDoc();
					if (regFilter.lastIndexOf("/g") < 0 && regFilter.lastIndexOf('/') < 0){
						regFilter = "'" + regFilter + "'";
					}
					//处理事件
					if (me.getTag().equals("onkeyup") || me.getTag().equals("onkeypress")){
						tbExp.addAttr(me.getTag(), "return txtTest_Onkeyup(this," + regFilter + ",'" + me.getTag1() + "')");
						//tbExp.Attributes[me.Tag] += "value=value.replace(" + regFilter + ",'')";
					}else if (me.getTag().equals("onclick")){
						tbExp.addAttr(me.getTag(), me.getDoc());
					}else{
						tbExp.addAttr(me.getTag(), "EleInputCheck2(this," + regFilter + ",'" + me.getTag1() + "');");
					}
				}
			}
			// endregion 在处理其它。
         }
		
		// region 保存时处理正则表达式验证
		String scriptCheckFrm = "";
		scriptCheckFrm = "\t\n<script type='text/javascript' >";
		scriptCheckFrm += "\t\n function SysCheckFrm(){ ";
		scriptCheckFrm += "\t\n var isPass = true;";
		scriptCheckFrm += "\t\n var alloweSave = true;";
		scriptCheckFrm += "\t\n var erroMsg = '提示信息:';";
		for (MapExt me :MapExts.convertMapExts(mes)){
			if (me.getExtType() == MapExtXmlList.RegularExpression && me.getTag().equals("onsubmit")){
				Object b = this.ui.GetUIByID("TB_" + me.getAttrOfOper());
				if (b == null){
					continue;
				}
				TextBox tb = (TextBox) b;
				scriptCheckFrm += "\t\n try{  ";
				scriptCheckFrm += "\t\n var element = document.getElementById('" + tb.getId() + "');";
				//验证输入的正则格式
				String regFilter = me.getDoc();
				if (regFilter.lastIndexOf("/g") < 0 && regFilter.lastIndexOf('/') < 0){
					regFilter = "'" + regFilter + "'";
				}
				scriptCheckFrm += "\t\n isPass = EleSubmitCheck(element," + regFilter + ",'" + me.getTag1() + "');";
				//scriptCheckFrm += "\t\n var reg =new RegExp(" + regFilter + ");   isPass = reg.test(element.value); ";
				scriptCheckFrm += "\t\n  if(isPass == false){";
				scriptCheckFrm += "\t\n   //EleSubmitCheck(element," + regFilter + ",'" + me.getTag1() + "'); alloweSave = false;";
				scriptCheckFrm += "\t\n   alloweSave = false;";
				scriptCheckFrm += "\t\n    erroMsg += '" + me.getTag1() + ";';";
				scriptCheckFrm += "\t\n  }";
				scriptCheckFrm += "\t\n } catch(e) { ";
				scriptCheckFrm += "\t\n  alert(e.name  + e.message);  return false;";
				scriptCheckFrm += "\t\n } ";
				
			}
		}
		scriptCheckFrm += "\t\n if(alloweSave == false){";
		scriptCheckFrm += "\t\n     alert(erroMsg);";
		scriptCheckFrm += "\t\n  } ";
		scriptCheckFrm += "\t\n return alloweSave; } ";
		scriptCheckFrm += "\t\n</script>";
		this.ui.append(scriptCheckFrm);
		// endregion

		// endregion 处理扩展设置
		
		// region 处理自动计算
		String js = "\t\n <script type='text/javascript' >oid=" + en.getPKVal() + ";</script>";
		this.ui.append(js);
		for (MapExt ext : MapExts.convertMapExts(mes)){
			if (!ext.getTag().equals("1")){
				continue;
			}
			js = "\t\n <script type='text/javascript' >";
			TextBox tb = null;
			try{
				Object  b = this.ui.GetUIByID("TB_" + ext.getAttrOfOper());
				if (b == null){
					continue;
				}
				tb = (TextBox) b;
			}catch (java.lang.Exception e){
				continue;
			}
			
			String left = "\n  document.forms[0]." + tb.getId() + ".value = ";
			String right = ext.getDoc();
			
			Paras ps = new Paras();
			ps.SQL = "SELECT KeyOfEn,Name FROM Sys_MapAttr WHERE FK_MapData=" + ps.getDBStr() + "FK_MapData AND LGType=0 AND (MyDataType=2 OR MyDataType=3 OR MyDataType=5 OR MyDataType=8 OR MyDataType=9) ORDER BY KeyOfEn DESC";
			ps.Add("FK_MapData", enName);
			
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			for (DataRow dr : dt.Rows){
				String keyofen = dr.getValue(0).toString();
				String name = dr.getValue(1).toString();
				
				if (ext.getDoc().contains("@" + keyofen) || (ext.getDoc().contains("@" + name) && !StringHelper.isNullOrEmpty(name))){
				}else{
					continue;
				}
				
				String tbID = "TB_" + keyofen;
				TextBox mytb = (TextBox)this.ui.GetUIByID(tbID);
				mytb.addAttr("onkeyup", "javascript:Auto" + ext.getAttrOfOper() + "();");

				right = right.replace("@" + keyofen, " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
				if (!StringHelper.isNullOrEmpty(name)){
					right = right.replace("@" + name, " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
				}
			}
			
			int myDataType = BP.DA.DataType.AppMoney;
			//判断类型
			for (MapAttr attr : MapAttrs.convertMapAttrs(mattrs)){
				if (attr.getKeyOfEn().equals(ext.getAttrOfOper())){
					myDataType = attr.getMyDataType();
				}
			}
			
			js += "\t\n function Auto" + ext.getAttrOfOper() + "() { ";
			js += left + right + ";";
			if (myDataType == BP.DA.DataType.AppFloat || myDataType == BP.DA.DataType.AppDouble)
			{
				js += " \t\n  document.forms[0]." + tb.getId() + ".value= document.forms[0]." + tb.getId() + ".value;";
			}
			else
			{
				js += " \t\n  document.forms[0]." + tb.getId() + ".value= VirtyMoney(document.forms[0]." + tb.getId() + ".value ) ;";
			}
			js += "\t\n } ";
			js += "\t\n</script>";
			this.ui.append(js); //加入里面.
		}
		// endregion
	}
	
	
	private void ADDWork(Work en, ReturnWorks rws, int nodeId){
		 this.BindViewEn(en, "width=90%");
		 for (ReturnWork rw : ReturnWorks.convertReturnWorks(rws)){
             if (rw.getReturnToNode() != nodeId)
                 continue;

             this.ui.append(BaseModel.AddBR());
             this.ui.append(BaseModel.AddMsgOfInfo("退回信息：", rw.getNoteHtml()));
         }
		 //foreach (ShiftWork fw in fws)
         //{
         //    if (fw.FK_Node != nodeId)
         //        continue;
         //    this.AddBR();
         //    this.AddMsgOfInfo("转发信息：", fw.NoteHtml);
         //}
		 
		 String refstrs = "";
         if (en.getIsEmpty()){
             refstrs += "";
             return;
         }
         
         String keys = "&PK=" + en.getPKVal().toString();
         for (Attr attr : en.getEnMap().getAttrs()){
        	 if (attr.getMyFieldType() == FieldType.Enum ||
                     attr.getMyFieldType() == FieldType.FK ||
                     attr.getMyFieldType() == FieldType.PK ||
                     attr.getMyFieldType() == FieldType.PKEnum ||
                     attr.getMyFieldType() == FieldType.PKFK)
                     keys += "&" + attr.getKey() + "=" + en.GetValStrByKey(attr.getKey());
         }
         Entities hisens = en.getGetNewEntities();
         
         //#region 加入他的明细
         EnDtls enDtls = en.getEnMap().getDtls();
         if (enDtls.size() > 0){
         	for (EnDtl enDtl : enDtls){
         		 String url = basePath + "WF/WFRptDtl.jsp?RefPK=" + en.getPKVal().toString() + "&EnName=" + enDtl.getEns().getGetNewEntity().toString();
                  int i = 0;
                  try{
                      i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
                  }catch(Exception e){
                      i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
                  }
                  
                  if (i == 0)
                      refstrs += "[<a href=\"javascript:WinOpen('" + url + "','u8');\" >" + enDtl.getDesc() + "</a>]";
                  else
                      refstrs += "[<a  href=\"javascript:WinOpen('" + url + "','u8');\" >" + enDtl.getDesc() + "-" + i + "</a>]";
         	}
         }
         //#endregion
         
         //#region 加入一对多的实体编辑
         AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
         if (oneVsM.size() > 0)
         {
             for (AttrOfOneVSM vsM : oneVsM){
                 String url = "UIEn1ToM.jsp?EnsName=" + en.toString() + "&AttrKey=" + vsM.getEnsOfMM().toString() + keys;
                 String sql = "SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'";
                 int i = DBAccess.RunSQLReturnValInt(sql);

                 if (i == 0)
                     refstrs += "[<a href='" + url + "' target='_blank' >" + vsM.getDesc() + "</a>]";
                 else
                     refstrs += "[<a href='" + url + "' target='_blank' >" + vsM.getDesc() + "-" + i + "</a>]";
             }
         }
         //#endregion
		 
         //#region 加入他门的相关功能
         //			SysUIEnsRefFuncs reffuncs = en.GetNewEntities.HisSysUIEnsRefFuncs ;
         //			if ( reffuncs.Count > 0  )
         //			{
         //				foreach(SysUIEnsRefFunc en1 in reffuncs)
         //				{
         //					string url="RefFuncLink.jsp?RefFuncOID="+en1.OID.ToString()+"&MainEnsName="+hisens.ToString()+keys;
         //					//int i=DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM "+vsM.EnsOfMM.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE "+vsM.AttrOfMInMM+"='"+en.PKVal+"'");
         //					refstrs+="[<a href='"+url+"' target='_blank' >"+en1.Name+"</a>]";
         //					//refstrs+="编辑: <a href=\"javascript:window.open(RefFuncLink.jsp?RefFuncOID="+en1.OID.ToString()+"&MainEnsName="+ens.ToString()+"'> )\" > "+en1.Name+"</a>";
         //					//var newWindow= window.open( this.Request.ApplicationPath+'/Comm/'+'RefFuncLink.jsp?RefFuncOID='+OID+'&MainEnsName='+ CurrEnsName +CurrKeys,'chosecol', 'width=100,top=400,left=400,height=50,scrollbars=yes,resizable=yes,toolbar=false,location=false' );
         //					//refstrs+="编辑: <a href=\"javascript:EnsRefFunc('"+en1.OID.ToString()+"')\" > "+en1.Name+"</a>";
         //					//refstrs+="编辑:"+en1.Name+"javascript: EnsRefFunc('"+en1.OID.ToString()+"',)";
         //					//this.AddItem(en1.Name,"EnsRefFunc('"+en1.OID.ToString()+"')",en1.Icon);
         //				}
         //			}
         //#endregion

         // 不知道为什么去掉。
         this.ui.append(refstrs);
	}
	
	private void BindViewEn(Entity en, String tableAttr){
		
		  //this.Attributes["visibility"] = "hidden";
          //this.AddTable("width=100%");
          //this.AddTable("width=90%");

          this.ui.append(BaseModel.AddTable(tableAttr ));
          
          boolean isLeft = true;
          Object val = null;
          boolean isAddTR = true;
          //Map map = (Map) en.getEnMap();
          Attrs attrs = en.getEnMap().getAttrs();
          for(Attr attr : attrs){
        	  if (!attr.getUIVisible())
                  continue;
              if (attr.getMyFieldType() == FieldType.RefText)
                  continue;
              
              val = en.GetValByKey(attr.getKey());
              
              //#region 判断是否单列显示
              if (attr.UIIsLine){
            	  if (!isAddTR){
            		  this.ui.append(BaseModel.AddTD());
            		  this.ui.append(BaseModel.AddTD());
            		  this.ui.append(BaseModel.AddTDEnd());
                  }
            	  
            	   isLeft = true;
                   isAddTR = true; /*让他下次从0开始。*/
                   if (attr.getUIHeight() != 0){
                       /*大块文本采集, 特殊处理。*/
                       if (val.toString().length() == 0 && !en.getIsEmpty() && "Doc".equals(attr.getKey()))
                           val = en.GetValDocHtml();
                       else
                           val = DataType.ParseText2Html(val.toString() );

                       this.ui.append(AddAttrDescValDoc(attr.getDesc(), val.toString(), 4));
                       continue;
                   }
                   
                   this.ui.append(BaseModel.AddTR());
                   if (attr.getMyDataType() == DataType.AppBoolean){
                	   if ("1".equals(val.toString()))
                		   this.ui.append(AddAttrDescVal("", "<b>是</b> " + attr.getDesc(), 3));
                       else
                    	   this.ui.append(AddAttrDescVal("", "<b>否</b> " + attr.getDesc(), 3));
                   }else{
                	   this.ui.append(AddAttrDescVal(attr.getDesc(), val.toString(), 3));
                   }
                   
                   this.ui.append(BaseModel.AddTREnd());
                   continue;
              }
              
              //#endregion 判断是否单列显示 // 结束要显示单行的情况。
              if (isLeft)
            	  this.ui.append(BaseModel.AddTR());
              
              switch (attr.getUIContralType()){
              	 case TB:
              		 this.ui.append(AddAttrDescVal(attr.getDesc(), val.toString(), 1));
              		 //if (attr.UIHeight != 0)
                     //{
                     //    if (val.ToString().Length == 0 && en.IsEmpty == false && attr.Key == "Doc")
                     //        val = en.GetValDocHtml();
                     //    else
                     //        val = DataType.ParseText2Html(val as string);

                     //    this.AddAttrDescValDoc(attr.Desc, val.ToString(), 2);
                     //}
                     //else
                     //{
                     //    this.AddAttrDescVal(attr.Desc, val.ToString(), 1);
                     //}
              		 break;
              	 case DDL:
              		 this.ui.append(AddAttrDescVal(attr.getDesc(), en.GetValRefTextByKey(attr.getKey()), 1 ));
                     break;
                 case CheckBok:
                     if (en.GetValBooleanByKey(attr.getKey()))
                    	 this.ui.append(AddAttrDescVal(attr.getDesc(), "是", 1));
                     else
                    	 this.ui.append(AddAttrDescVal(attr.getDesc(), "否", 1));
                     break;
                 default:
                     break;
              }
              
              if (isLeft == false)
            	  this.ui.append(BaseModel.AddTREnd());
              isLeft = !isLeft;
          }//结束循环.
          this.ui.append(BaseModel.AddTableEnd());
	}
	//#endregion
	
//	 private String RequestParas(){
//         String urlExt = "";
//         String rawUrl = this.get_request().getRequestURL().toString();
//         rawUrl = "&" + rawUrl.substring(rawUrl.indexOf("?") + 1);
//         String[] paras = rawUrl.split("&");
//         for(String para : paras){
//             if (para == null
//                 || para == ""
//                 || !para.contains("="))
//                 continue;
//             urlExt += "&" + para;
//         }
//        return urlExt;
//     }
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
	public int getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
	public String getDoType() {
		return DoType;
	}
	public void setDoType(String doType) {
		DoType = doType;
	}
	public int getStartNodeID() {
		return StartNodeID;
	}
	public void setStartNodeID(int startNodeID) {
		StartNodeID = startNodeID;
	}
	public long getWorkID() {
		return WorkID;
	}
	public void setWorkID(long workID) {
		WorkID = workID;
	}
	public long getCWorkID() {
		return CWorkID;
	}
	public void setCWorkID(long cWorkID) {
		CWorkID = cWorkID;
	}
	public int getNodeID() {
		return NodeID;
	}
	public void setNodeID(int nodeID) {
		NodeID = nodeID;
	}
	public int getFID() {
		return FID;
	}
	public void setFID(int fID) {
		FID = fID;
	}
	public String getMyPK() {
		return MyPK;
	}
	public void setMyPK(String myPK) {
		MyPK = myPK;
	}
	public String getViewWork() {
		return ViewWork;
	}
	public void setViewWork(String viewWork) {
		ViewWork = viewWork;
	}
	public String getWidth() {
		return Width;
	}
	public void setWidth(String width) {
		Width = width;
	}
	public String getHeight() {
		return Height;
	}
	public void setHeight(String height) {
		Height = height;
	}
	public String getBtnWord() {
		return BtnWord;
	}
	public void setBtnWord(String btnWord) {
		BtnWord = btnWord;
	}
	
	public String getCCID() {
		return CCID;
	}
	public void setCCID(String cCID) {
		CCID = cCID;
	}

	public HttpServletRequest get_request() {
		return _request;
	}
	public void set_request(HttpServletRequest _request) {
		this._request = _request;
	}
	public HttpServletResponse get_response() {
		return _response;
	}
	public void set_response(HttpServletResponse _response) {
		this._response = _response;
	}
	
	private String getPageID(){
		try {
			String url = this._request.getRequestURI();

			int i = url.lastIndexOf("/") + 1;
			int i2 = url.indexOf(".jsp") - 5;
			try {
				url = url.substring(i);
				return url.substring(0, url.indexOf(".jsp"));

			} catch (RuntimeException ex) {
				throw new RuntimeException(ex.getMessage() + url + " i=" + i
						+ " i2=" + i2);
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException("获取当前PageID错误:" + ex.getMessage());
		}
	}
	private String decimalFormat(Object object){
		if(null == object || "".equals(object.toString())){
			object = 0.00;
		}
		String str = new DecimalFormat("#0.00").format(Double.parseDouble(object.toString()));
		return str;
	}
	private void AddLoadFunction(String id, String eventName, String method){
		String js = "";
		js = "\t\n<script type='text/javascript' >";
		js += "\t\n function " + id + "load() { ";
		js += "\t\n   if (document.all) {";
		js += "\t\n     document.getElementById('F" + id + "').attachEvent('on" + eventName + "',function(event){" + method + "('" + id + "');});";
		js += "\t\n } ";

		js += "\t\n else { ";
		js += "\t\n  document.getElementById('F" + id + "').contentWindow.addEventListener('" + eventName + "',function(event){" + method + "('" + id + "');}, false); ";
		js += "\t\n } }";

		js += "\t\n</script>";
		this.ui.append(js);
	}
	/**
	 * 添加富文本
	 * @param en
	 * @param attr
	 */
	private void AddRichTextBox(Entity en, MapAttr attr){
		//说明这是富文本输出
		scripts.add(basePath+"WF/Comm/kindeditor/kindeditor-all.js");
		scripts.add(basePath+"WF/Comm/kindeditor/lang/zh_CN.js");
		scripts.add(basePath+"WF/Comm/kindeditor/plugins/code/prettify.js");
		
		csslinks.add(basePath+"WF/Comm/kindeditor/plugins/code/prettify.css");
		csslinks.add(basePath+"WF/Comm/kindeditor/themes/default/default.css");
		
		TextBox tbd = this.ui.creatTextBox("TB_" + attr.getKeyOfEn());
		tbd.setTextMode(TextBoxMode.MultiLine);
		tbd.setText(htmlspecialchars(en.GetValStrByKey(attr.getKeyOfEn())));
		tbd.addAttr("style", "width:" + attr.getUIWidth() + "px;height:" + attr.getUIHeight() + "px;visibility:hidden;");
		this.ui.append(tbd);
		
		String strs = "\t\n <script>";
		strs += "\t\n var editor1; ";
		strs += "\t\n KindEditor.ready(function(K) {";
          
		strs += "\t\n var tbID='TB_" + attr.getKeyOfEn() + "'; ";

		strs += "\t\n var ctrl =document.getElementById( tbID);";

		strs += "\t\n if (ctrl == null) { ";
		strs += "\t\n     alert('没有找到要帮定的控件'); ";
		strs += "\t\n  } ";

		strs += "\t\n  editor1 = K.create('#'+tbID, {";
		strs += "\t\n  afterBlur: function(){ ";
		strs += "\t\n  this.sync(); ";
		strs += "\t\n  }, ";
		strs += "\t\n cssPath : '" + basePath + "WF/Comm/kindeditor/plugins/code/prettify.css',";
		strs += "\t\n uploadJson : '" + basePath + "WF/Comm/kindeditor/jsp/upload_json.jsp',";
		strs += "\t\n fileManagerJson : '" + basePath + "WF/Comm/kindeditor/jsp/file_manager_json.jsp',";
		strs += "\t\n allowFileManager : true,";

		strs += "\t\n width : '100%',";
		//strs += "\t\n width : '" + attr.UIWidth + "px',";

		strs += "\t\n height : '" + attr.getUIHeight() + "px'";

		strs += "\t\n });";
		strs += "\t\n });";

		//strs += "\t\n KindEditor.show(function(K) {";
		//strs += "\t\n KindEditor.ready(function(K) {";

		strs += "\t\n </script>";
		this.ui.append(strs);
	}
	
	private String htmlspecialchars(String str) {
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}
	
	private String AddAttrDescValDoc(String desc, String doc, int colspan){
    	String str = "";
        if (colspan == 4){
        	str+=BaseModel.AddTR();
        	str+=BaseModel.AddTDDesc(desc, colspan);
        	str+=BaseModel.AddTREnd();

        	str+=BaseModel.AddTR();
        	str+=BaseModel.AddTDBigDoc(" class=BigDoc  align=left colspan=4", doc);
        	str+=BaseModel.AddTREnd();

        } else{
        	str+=BaseModel.AddTDBegin(" class=BigDoc align=left colspan=" + colspan);
        	str+="<b>" + desc + "</b><br>";
        	str+=doc;
        	str+=BaseModel.AddTDEnd();
        }
        return str;
    }
	
	 private String AddAttrDescVal(String desc, String doc, int colspan){
	    return  BaseModel.AddTDDesc(desc)+
	        		BaseModel.AddTD(" class=TD align=left width=40% colspan=" + colspan, doc);
	}
	
	private void InsertObjects(boolean isJudgeRowIdx){
		// region 从表
		for (MapDtl dtl : MapDtls.convertMapDtls(dtls)){
			
			if (!dtl.getIsView() || ctrlUseSta.contains(dtl.getNo())){
				continue;
			}

			if (dtl.getGroupID() == 0){
				dtl.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				dtl.setRowIdx(0);
				dtl.Update();
			}
			
			if (isJudgeRowIdx){
				if (dtl.getRowIdx() != rowIdx){
					continue;
				}
			}

			if (dtl.getGroupID() == currGF.getOID()){
			}else{
				continue;
			}
			
			ctrlUseSta += dtl.getNo();

			rowIdx++;
			this.ui.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			this.ui.append("<TD colspan=" + mapData.getTableCol() + " ID='TD" + dtl.getNo() + "' height='50px' width='100%' style='align:left'>");
			String src = "";
			try{
				src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&FID=" + HisEn.GetValStringByKey("FID") + "&IsWap=0&FK_Node=" + dtl.getFK_MapData().replace("ND", "");
			}catch (java.lang.Exception e){
				src = basePath + "WF/CCForm/Dtl2.jsp?EnsName=" + dtl.getNo() + "&RefPKVal=" + HisEn.getPKVal() + "&IsWap=0&FK_Node=" + dtl.getFK_MapData().replace("ND", "");
			}
			
			if (this.IsReadonly || dtl.getIsReadonly()){
				this.ui.append("<iframe ID='F" + dtl.getNo() + "'  src='" + src + "&IsReadonly=1' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='30px' /></iframe>");
			}else{
				//Add("<iframe ID='F" + dtl.No + "'   Onblur=\"SaveDtl('" + dtl.No + "');\"  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' /></iframe>");

				AddLoadFunction(dtl.getNo(), "blur", "SaveDtl");

				this.ui.append("<iframe ID='F" + dtl.getNo() + "'  onload='" + dtl.getNo() + "load();'  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' /></iframe>");
			}

			this.ui.append(BaseModel.AddTDEnd());
			this.ui.append(BaseModel.AddTREnd());
		}
		// endregion 从表

		// region 多对多的关系
		for (MapM2M m2m : MapM2Ms.convertMapM2Ms(m2ms)){
			
			if (ctrlUseSta.contains("@" + m2m.getMyPK())){
				continue;
			}
			if (isJudgeRowIdx){
				if (m2m.getRowIdx() != rowIdx){
					continue;
				}
			}
			
			if (m2m.getGroupID() == 0 && rowIdx == 0){
				m2m.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				m2m.setRowIdx(0);
				m2m.Update();
			}else if (m2m.getGroupID() == currGF.getOID()){
			}else{
				continue;
			}
			
			ctrlUseSta += "@" + m2m.getMyPK();
			
			rowIdx++;
			this.ui.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			String src = basePath + "WF/CCForm/M2M.jsp?NoOfObj=" + m2m.getNoOfObj();
			String paras = paramsStr;
			if (!paras.contains("FID=")){
				paras += "&FID=" + HisEn.GetValStrByKey("FID");
			}

			if (!paras.contains("OID=")){
				paras += "&OID=" + HisEn.GetValStrByKey("OID");
			}
			
			src += "&r=q" + paras;
			if (!src.contains("FK_MapData")){
				src += "&FK_MapData=" + m2m.getFK_MapData();
			}
			switch (m2m.getShowWay()){
				case FrmAutoSize:
					this.ui.append("<TD colspan=" + mapData.getTableCol() + " ID='TD" + m2m.getNoOfObj() + "' height='20px' width='100%'  >");
					if (m2m.getHisM2MType() == M2MType.M2M){
						AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

						//  Add("<iframe ID='F" + m2m.NoOfObj + "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");
						this.ui.append("<iframe ID='F" + m2m.getNoOfObj() + "'  onload='" + m2m.getNoOfObj() + "load();'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");

					}else{
						this.ui.append("<iframe ID='F" + m2m.getNoOfObj() + "' src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=no /></iframe>");
					}
					break;
				case FrmSpecSize:
					this.ui.append("<TD colspan=" + mapData.getTableCol() + "  ID='TD" + m2m.getNoOfObj() + "' height='" + m2m.getH() + "' width='" + m2m.getW() + "'  >");
					if (m2m.getHisM2MType() == M2MType.M2M){
						AddLoadFunction(m2m.getNoOfObj(), "blur", "SaveM2M");

						// appendPub("<iframe ID='F" + m2m.NoOfObj + "'   Onblur=\"SaveM2M('" + m2m.NoOfObj + "');\"  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.W + "' height='" + m2m.H + "' scrolling=auto /></iframe>");
						this.ui.append("<iframe ID='F" + m2m.getNoOfObj() + "' onload='" + m2m.getNoOfObj() + "load();'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.getW() + "' height='" + m2m.getH() + "' scrolling=auto /></iframe>");

					}else{
						this.ui.append("<iframe ID='F" + m2m.getNoOfObj() + "'    src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.getW() + "' height='" + m2m.getH() + "' scrolling=auto /></iframe>");
					}
					break;
				case Hidden:
					break;
				case WinOpen:
					this.ui.append("<TD colspan=" + mapData.getTableCol() + " ID='TD" + m2m.getNoOfObj() + "' height='20px' width='100%'  >");
					this.ui.append("<a href=\"javascript:WinOpen('" + src + "&IsOpen=1','" + m2m.getW() + "','" + m2m.getH() + "');\"  />" + m2m.getName() + "</a>");
					break;
				default:
					break;
			}
			
			this.ui.append(BaseModel.AddTDEnd());
			this.ui.append(BaseModel.AddTREnd());
		}
		
		// endregion 多对多的关系

		 

		// region 附件
		for (FrmAttachment ath : FrmAttachments.convertFrmAttachments(aths)){
			if (ctrlUseSta.contains("@" + ath.getMyPK())){
				continue;
			}
			if (isJudgeRowIdx){
				if (ath.getRowIdx() != rowIdx){
					continue;
				}
			}
			
			if (ath.getGroupID() == 0 && rowIdx == 0){
				ath.setGroupID(Integer.parseInt(String.valueOf(currGF.getOID())));
				ath.setRowIdx(0);
				ath.Update();
			}else if (ath.getGroupID() == currGF.getOID()){
			}else{
				continue;
			}
			
			ctrlUseSta += "@" + ath.getMyPK();
			rowIdx++;
			// myidx++;
			this.ui.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' "));
			this.ui.append("<TD colspan=" + mapData.getTableCol() + " ID='TD" + ath.getMyPK() + "' height='50px' width='100%' style='align:left'>");
			String src = "";
			if (this.IsReadonly){
				src = basePath + "WF/CCForm/AttachmentUpload.jsp?PKVal=" + HisEn.getPKVal() + "&Ath=" + ath.getNoOfObj() + "&FK_MapData=" + this.EnName + "&FK_FrmAttachment=" + ath.getMyPK() + "&IsReadonly=1" + paramsStr;
			}else{
				src = basePath + "WF/CCForm/AttachmentUpload.jsp?PKVal=" + HisEn.getPKVal() + "&Ath=" + ath.getNoOfObj() + "&FK_MapData=" + this.EnName + "&FK_FrmAttachment=" + ath.getMyPK() + paramsStr;
			}
			
			if (ath.getIsAutoSize()){
				this.ui.append("<iframe ID='F" + ath.getMyPK() + "'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='100%' height='10px' scrolling=auto /></iframe>");
			}else{
				this.ui.append("<iframe ID='F" + ath.getMyPK() + "'   src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + ath.getW() + "' height='" + ath.getH() + "' scrolling=auto /></iframe>");
			}
			
			this.ui.append(BaseModel.AddTDEnd());
			this.ui.append(BaseModel.AddTREnd());
		}
		// endregion 附件
	}
}
