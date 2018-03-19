package cn.jflow.common.model;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.FileUpload;
import cn.jflow.system.ui.core.Label;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachmentDBs;


public class SingleAttachmentUploadModel extends BaseModel{

	public StringBuilder Pub1 = new StringBuilder();
	

	public SingleAttachmentUploadModel(HttpServletRequest request, HttpServletResponse response) {
		super(request,response);
	}
	
	public boolean getIsWoEnableWF()
	{
		if(this.get_request().getParameter("IsWoEnableWF")==null)
			return false;
		return  Boolean.valueOf(this.get_request().getParameter("IsWoEnableWF"));
	}

	public  void init()
	{
		String HFK_Node = get_request().getParameter("HFK_Node");
		String OID = get_request().getParameter("OID");
		FrmAttachment ath = new FrmAttachment(this.getFK_FrmAttachment());
		FrmAttachmentDBs athDBs = new FrmAttachmentDBs(this.getEnName(), this.getPKVal());
		FrmAttachmentDB athDB = null;
		if(athDBs.size()>0)
		{
			Object tempVar4 = athDBs.GetEntityByKey(FrmAttachmentDBAttr.FK_FrmAttachment, ath.getMyPK());
			athDB = (FrmAttachmentDB)((tempVar4 instanceof FrmAttachmentDB) ? tempVar4 : null);
		}

		//Object tempVar4 = athDBs.GetEntityByKey(FrmAttachmentDBAttr.FK_FrmAttachment, ath.getMyPK());
		//FrmAttachmentDB athDB = (FrmAttachmentDB)((tempVar4 instanceof FrmAttachmentDB) ? tempVar4 : null);
		//MapData md = new MapData(this.getEnName());
	    //<input type="hidden" id="FK_Node" name="FK_Node" value="<%=FK_Node%>"/>
	    //<input type="hidden" id="EnName" name="EnName" value="<%=EnName%>"/>
	    //<input type="hidden" id="PKVal" name="PKVal" value="<%=PKVal%>"/>    
		//HFK_Node="+fk_node+"&OID="+HisEn.GetValStrByKey("OID")+"&EnName="+enName+"&PKVal=" + HisEn.getPKVal().toString() + "&Ath=" + ath.getNoOfObj() + "&FK_FrmAttachment=" + ath.getMyPK() + paramsStr
		String actionString = this.getBasePath()+"WF/CCForm/SingleUpload.do?FK_FrmAttachment="+this.getFK_FrmAttachment()+"&HFK_Node="+HFK_Node+
		"&OID="+OID+"&FK_Node="+this.getFK_Node()+"&EnName="+this.getEnName()+"&PKVal="+this.getPKVal();
		Pub1.append("<form method=\"post\" action=\""+actionString+"\"  id=\"form1_"+ath.getMyPK()+"\"  name=\"form1_"+ath.getMyPK()+"\" enctype=\"multipart/form-data\">");
		Pub1.append("<input type=\"hidden\" id=\"BtnID\" name=\"BtnID\" value=\"hh\"/>");
		Pub1.append("<input type=\"submit\" id=\"btn_sumit\"  style=\"display:none;\"/>");
		if(getIsReadonly()){
			Pub1.append("<div style=\"position:absolute;width:100%;height:100%;z-index:1;filter:alpha(opacity=0);opacity:0;background:#ffffff\"></div>");
		}else{
			Pub1.append("<div style=\"position: fixed; height: 100%; width: 100%;overflow-x:hidden;overflow-y:auto;\">");
		}
		Pub1.append("<DIV>");
		Label lab = new Label();
		lab.setId("Lab" + ath.getMyPK());
		//appendPub(lab.toString());
		if (athDB != null)
		{
			if (ath.getIsWoEnableWF())
			{
				lab.setText("<a  href=\"javascript:OpenOfiice('" + athDB.getFK_FrmAttachment() + "','" + OID + "','" + athDB.getMyPK() + "','" + this.getEnName() + "','" + ath.getNoOfObj() + "','" + HFK_Node+ "')\"><img src='" + this.getBasePath() + "WF/Img/FileType/" + athDB.getFileExts() + ".gif' border=0/>" + athDB.getFileName() + "</a>");
			}
			else
			{
				lab.setText("<img src='" + this.getBasePath() + "WF/Img/FileType/" + athDB.getFileExts() + ".gif' border=0/>" + athDB.getFileName());
			}
		}
		Pub1.append(lab.toString());
		Pub1.append("</DIV>");

		Pub1.append("<DIV>");
		Button mybtn = new Button();
		mybtn.setCssClass("Btn");

		mybtn.setId(ath.getMyPK());
		mybtn.setText("上传");
		mybtn.setCssClass("bg");
		mybtn.setId("Btn_Upload_" + ath.getMyPK() + "_" + this.getPKVal());
		mybtn.addAttr("style","display:none;");
		mybtn.setReadOnly(!ath.getIsUpload());
		//mybtn.Click += new EventHandler(btnUpload_Click);
		Pub1.append(mybtn.toString());
		
		if (athDB != null){
			if (ath.getIsDownload()) {
				mybtn = new Button();
				mybtn.setText("下载");
				mybtn.setCssClass("Btn");
				mybtn.setId("Btn_Download_" + ath.getMyPK() + "_" + this.getPKVal());
				mybtn.setCssClass("bg");
				mybtn.addAttr("onclick", "UploadChange('" + mybtn.getId() + "');");
				Pub1.append(mybtn.toString());
			}

			if (ath.getIsDelete()) {
				mybtn = new Button();
				mybtn.setCssClass("Btn");
				mybtn.setText("删除");
				mybtn.setReadOnly(getIsReadonly());
				mybtn.setId("Btn_Delete_" + ath.getMyPK() + "_" + this.getPKVal());
				mybtn.addAttr("onclick","if(confirm('您确定要执行删除吗？'))UploadChange('" + mybtn.getId() + "');");
				// mybtn.Click += new EventHandler(btnUpload_Click);
				mybtn.setCssClass("bg");
				// mybtn.addAttr("onchange", "javascript: );

				Pub1.append(mybtn.toString());

			}
			if (ath.getIsWoEnableWF()) {
				String event = "OpenOfiice('" + athDB.getFK_FrmAttachment() + "','" + OID + "','" + athDB.getMyPK() + "','" + this.getEnName() + "','" + ath.getNoOfObj() + "','" + HFK_Node+ "')";
				
				mybtn = new Button();
				mybtn.setCssClass("Btn");
				mybtn.setText("编辑");
				mybtn.addAttr("onclick", event);
				mybtn.setCssClass("bg");
				Pub1.append(mybtn.toString());
			}
		}
		
		FileUpload fu = new FileUpload();
		fu.setId("file_single");
		fu.setName("file_single");
		fu.addAttr("width",String.valueOf(ath.getW()));
		fu.addAttr("onchange", "UploadChange('Btn_Upload_" + ath.getMyPK() + "_" + this.getPKVal()+"');");
		if(getIsReadonly()){
			fu.addAttr("readonly","readonly");
		}
		Pub1.append(fu.toString());
		
		Pub1.append("</DIV></form>");
	}


}
