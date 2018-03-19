package cn.jflow.common.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachments;
import BP.Sys.MapData;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.WorkFlow;
import BP.WF.Data.GERpt;
import BP.WF.Template.BtnLab;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.NodeWorkType;
import BP.WF.WFState;
import BP.Web.WebUser;


public class WebOfficeModel {

	private String basePath;
	private int FK_Node;
	private int FID;
	private long WorkID;
	private String FK_Flow;
	private boolean IsTrueTH;
	private String IsTrueTHTemplate;
	private String UserName;
	private String HeBing;
	private boolean ReadOnly = false;
	private boolean IsCheckInfo;
	private String NodeInfo;
	private boolean IsSavePDF;
	private boolean IsMarks;
	private String OfficeTemplate;
	private boolean IsLoadTempLate;
	
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;
	
	public String _MarkName = "";

	public final String getMarkName()
	{
		return _MarkName;
	}
	public final void setMarkName(String value)
	{
		_MarkName = value;
	}
	
	public WebOfficeModel(String basePath, int FK_Node, int FID, long WorkID, String FK_Flow, boolean IsTrueTH, String IsTrueTHTemplate,
			String UserName, String HeBing, boolean ReadOnly, boolean IsCheckInfo, String NodeInfo, boolean IsSavePDF, boolean IsMarks,
			String OfficeTemplate, boolean IsLoadTempLate, HttpServletRequest _request, HttpServletResponse _response){
		this.basePath = basePath;
		this.FK_Node = FK_Node;
		this.FID = FID;
		this.WorkID = WorkID;
		this.FK_Flow = FK_Flow;
		this.IsTrueTH = IsTrueTH;
		this.IsTrueTHTemplate = IsTrueTHTemplate;
		this.UserName = UserName;
		this.HeBing = HeBing;
		this.ReadOnly = ReadOnly;
		this.IsCheckInfo = IsCheckInfo;
		this.NodeInfo = NodeInfo;
		this.IsSavePDF = IsSavePDF;
		this.IsMarks = IsMarks;
		this.OfficeTemplate = OfficeTemplate;
		this.IsLoadTempLate = IsLoadTempLate;
		
		this._request = _request;
		this._response = _response;
	}
	
	public UiFatory ui = null;
	public StringBuffer divMenu;
    private boolean IsPostBack = false;
	public boolean init() {
		this.ui = new UiFatory();
		this.divMenu = new StringBuffer();
		 if ("-1".equals(String.valueOf(this.FK_Node)) || "0".equals(String.valueOf(this.WorkID))){
             divMenu.append("<h1 style='color:red'>传入参数错误!<h1>");
             return true;
         }
		 
		 if (!IsPostBack){
             String type = this._request.getParameter("action")==null?"":this._request.getParameter("action");
             if (StringHelper.isNullOrEmpty(type)){
            	 if("View".equals(_request.getParameter("DoType"))){
            		 ReadOnly = true ;
            	 }
                 LoadMenu(!ReadOnly);
                 ReadFile();
             } else {
                 LoadMenu(false);
                 if ("LoadFile".equals(type)){
                     LoadFile();
                     return false;
                 }else if ("SaveFile".equals(type)){
                     SaveFile(this._request,this._response);
                     return false;
                 }else if (type.equals("LoadOver")){
                     GetFileBytes();
                     return false;
                 }else if (type.equals("SaveBak")){
                     SaveBak();
                     return false;
                 }else if (type.equals("Download")){
                     DownloadFile();
                     return false;
                 }else{
                     throw new RuntimeException("传入的参数不正确!");
                 }
             }
         }
		 return true;
	}
	
	private void SaveBak()
	{
		String result = "true";
		try
		{
//			HttpFileCollection files = HttpContext.Current.Request.Files;
			DiskFileItemFactory dfif = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(dfif);
			List<FileItem> files = servletFileUpload.parseRequest(_request); //获取上传的文件

			BP.WF.Node node = new BP.WF.Node(FK_Node);

			String fileStart = WorkID + "Mark";
			if (node.getHisNodeWorkType() == NodeWorkType.SubThreadWork)
			{
				fileStart = String.valueOf(FID);
			}

			//string file = Request["Path"];
			//file = HttpUtility.UrlDecode(file, Encoding.UTF8);

//			String path = Server.MapPath("~/DataUser/OfficeFile/" + FK_Flow + "/");
			String path = this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeFile/"+this.FK_Flow);
			String[] haveFiles = new File(path).list();
			String fileName = "", fileExtension = "";

			boolean isHave = false;
			for (String file : haveFiles)
			{
				java.io.File fileInfo = new java.io.File(file);
				if (fileInfo.getName().startsWith(fileStart + "."))
				{
					isHave = true;
				}

			}
			if (isHave)
			{
				fileStart += "." + UUID.randomUUID().toString().replaceAll("-", "");
			}

			if (files.size() > 0)
			{
				/**'检查文件扩展名字
				*/
//				HttpPostedFile postedFile = files.getItem(0);
				FileItem postedFile = files.get(0);
				fileName = postedFile.getName();

				if (!fileName.equals(""))
				{
					// if (!isHave)
					fileExtension = FileAccess.getExtensionName(fileName);

					String realSaveTo = path + "\\" + fileStart + fileExtension;
					FileUtils.copyInputStreamToFile(postedFile.getInputStream(), new File(realSaveTo));
					this.setMarkName(fileStart + fileExtension);
				}
			}

		}
		catch (java.lang.Exception e)
		{
			result = "false";
		}
		try {
			_response.reset();
			_response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 获取套红文件的记录
	*/
	private void GetFileBytes()
	{
		int name = Integer.parseInt(_request.getParameter("fileName"));

		String type = _request.getParameter("type");
		String realFileName = "";
//		String path = Server.MapPath("~/DataUser/OfficeOverTemplate/");
		String path = this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeOverTemplate/");
		if (type.equals("1"))
		{
			String[] files = new File(path).list();
			if (files != null){
				int i = 0;
				for (String fileName : files)
				{
					if (i == name)
					{
						realFileName = path + File.separator + fileName;
					}
					i++;
				}
			}
		}
		else
		{
//			realFileName = Server.MapPath("~/DataUser/OfficeOverTemplate/" + IsTrueTHTemplate);
			realFileName = this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeOverTemplate/" + IsTrueTHTemplate);
		}
        try {
        	this._response.reset();
        	File file = new File(realFileName);
        	if (file.exists()){
        		this._response.getOutputStream().write(FileUtils.readFileToByteArray(file));
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String fileName;
	private String fileType;
	private String sealName;
	private String sealType;
	private String sealIndex;
	
	private void ReadFile(){
		 String path =  this._request.getSession().getServletContext()
				 .getRealPath("/DataUser/OfficeFile/"+this.FK_Flow);
		 File fileDir = new File(path);
         if (!fileDir.exists()) {
            fileDir.mkdirs();
         }
         boolean isHave = false;
         Node node = new Node(this.FK_Node);
         
         String fileStart = String.valueOf(this.WorkID);
         if (node.getHisNodeWorkType() == NodeWorkType.SubThreadWork){
             fileStart = String.valueOf(this.FID);
         }
		 
         try{
        	 WorkFlow workflow = new WorkFlow(this.FK_Flow, this.WorkID);
        	  if (workflow.getHisGenerWorkFlow().getPWorkID() != 0)
              {
                  BtnLab btnLab = new BtnLab(this.FK_Node);
                  if (btnLab.getOfficeIsParent())
                      fileStart = String.valueOf(workflow.getHisGenerWorkFlow().getPWorkID());
              }
         }catch(Exception e){
        	 e.printStackTrace();
         }
         
         for (File f : fileDir.listFiles()) { 
        	 String fileName = f.getName();
        	 if (fileName.startsWith(fileStart + ".") && fileName.contains(".doc"))
             {
                 setFileName(fileName);
                 setFileType(fileName.substring(fileName.lastIndexOf('.')));
                 isHave = true;
                 break;
             }
         }
         
         
         for (File f : fileDir.listFiles()) {  
        	 String fileName = f.getName();
        	 if (fileName.startsWith(fileStart + "Mark.")){
                 setMarkName(fileName);
                 break;
             }
         }
         
         if (!isHave){
             if (node.getIsStartNode()){
                 if (!StringHelper.isNullOrEmpty(OfficeTemplate)){
                	 this.setFileName("/" + OfficeTemplate);
                	 this.setFileType(OfficeTemplate.split("\\.")[1]);
                     IsLoadTempLate = true;
                 }
             }
             
             //if (node.HisNodeWorkType == NodeWorkType.SubThreadWork)
             //{
             //    File.Exists(path+)
             //    foreach (string file in files)
             //    {
             //        FileInfo fileInfo = new FileInfo(file);
             //        if (fileInfo.Name.StartsWith(this.FID.ToString()))
             //        {
             //            fileInfo.CopyTo(path + "\\" + this.WorkID + fileInfo.Extension);
             //            fileName.Text = this.WorkID + fileInfo.Extension;
             //            fileType.Text = fileInfo.Extension.TrimStart('.');
             //            break;
             //        }
             //    }
             //}
         }else{
        	 //    if (node.HisNodeWorkType == NodeWorkType.WorkHL || node.HisNodeWorkType == NodeWorkType.WorkFHL)
             //    {

             //        GenerWorkFlows generWorksFlows = new GenerWorkFlows();
             //        generWorksFlows.RetrieveByAttr(GenerWorkFlowAttr.FID, this.WorkID);
             //        string tempH = "";
             //        foreach (GenerWorkFlow generWork in generWorksFlows)
             //        {
             //            tempH += generWork.WorkID + ",";
             //        }
             //        HeBing = tempH.TrimEnd(',');
             //    }
         }
	 }
	
	 private void LoadFile(){
		 
		 try{
             String loadType = this._request.getParameter("LoadType")==null?"":this._request.getParameter("LoadType");
             //String type = this.getFileType();
             String name = this._request.getParameter("fileName")==null?"":this._request.getParameter("fileName");;
             name = java.net.URLDecoder.decode(name,"UTF-8");
             String path = null;
             if("1".equals(loadType)){
            	 path = this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeFile/"+this.FK_Flow+"/"+name);
             }else{
            	 path = this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeTemplate/"+name); 
             }
             File file = new File(path);
             this._response.reset();
             if (file.exists()){
            	 this._response.getOutputStream().write(FileUtils.readFileToByteArray(file));
             }
         }catch (Exception e){
             e.printStackTrace();;
         }
	}
	 private void DownloadFile()
	 {
		if (StringUtils.isBlank(FK_Flow) || this.WorkID == 0)
		{
			try {
				_response.getWriter().print("参数不完整，必须具备FK_Flow和WorkID参数。");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

//		String docPath = Server.MapPath("~/DataUser/OfficeFile/" + FK_Flow + "/");
		String docPath = this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeFile/"+this.FK_Flow);
		java.io.File docFile = null;
//		java.io.File[] docFiles = (new java.io.File(docPath)).GetFiles(this.WorkID + ".doc*");
		java.io.File[] docFiles = (new java.io.File(docPath)).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(WorkID + ".doc");
			}
		});
		if (docFiles.length > 0)
		{
			docFile = docFiles[0];
		}

		if (docFile == null)
		{
			try {
				_response.getWriter().print("未找到公文！");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		try {
			BP.Sys.PubClass.DownloadFile(docFile.getPath(), docFile.getName());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			_response.getWriter().print("<script language='JavaScript'> window.close();</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
 
	 private void LoadAttachment(){
         String EnName = "ND" + this.FK_Node;
         MapData mapdata = new MapData(EnName);

         FrmAttachments attachments = new FrmAttachments();
         attachments = mapdata.getFrmAttachments();
         
        boolean isCompleate = false;
		BP.WF.Node node = new BP.WF.Node(FK_Node);
		try
		{
			WorkFlow workFlow = new WorkFlow(node.getFK_Flow(), WorkID);
			isCompleate = workFlow.getIsComplete();

		}
		catch (RuntimeException e)
		{
			try
			{
				Flow fl = new Flow(node.getFK_Flow());
				GERpt rpt = fl.getHisGERpt();
				rpt.setOID(WorkID);
				rpt.Retrieve();

				if (rpt != null)
				{
					if (rpt.getWFState() == WFState.Complete)
					{
						isCompleate = true;
					}
				}
			}
			catch (java.lang.Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
         for (FrmAttachment ath : attachments.ToJavaList()){
        	 String src = null;
        	 if (!isCompleate){
        		 src = this.basePath + "WF/CCForm/AttachmentUpload.jsp?PKVal=" + this.WorkID + "&Ath=" + ath.getNoOfObj() + "&FK_MapData=" + EnName + "&FK_FrmAttachment=" + ath.getMyPK() + "&FK_Node=" + this.FK_Node;
        	 }else{
        		 src = this.basePath + "WF/CCForm/AttachmentUpload.jsp?PKVal=" + this.WorkID + "&Ath=" + ath.getNoOfObj() + "&FK_MapData=" + EnName + "&FK_FrmAttachment=" + ath.getMyPK() + "&FK_Node=" + this.FK_Node + "&IsReadonly=1";
        	 }
//        	 this.ui.append("<iframe ID='F" + ath.getMyPK() + "'    src='" + src + "' frameborder=0  style='position:absolute;width:" + ath.getW() + "px; height:" + ath.getH() + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
             this.ui.append("<iframe ID='F" + ath.getMyPK() + "'    src='" + src + "' frameborder=0  style='position:absolute;width:100%;text-align: left;' leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");
         }
     }
	 
	  private void LoadMenu(boolean isMenu){
		
		 BtnLab btnLab = new BtnLab(this.FK_Node);
		 boolean isCompleate = false;
	     Node node = new Node(this.FK_Node);
	     try{
	    	 WorkFlow workFlow = new WorkFlow(node.getFK_Flow(), this.WorkID);
	         isCompleate = workFlow.getIsComplete();
	     } catch(Exception e){
	    	 try{
	             Flow fl = new Flow(node.getFK_Flow());
	             GERpt rpt = fl.getHisGERpt();
	             rpt.setOID(this.WorkID);
	             rpt.Retrieve();
	
	             if (rpt != null){
	                 if (rpt.getWFState() == WFState.Complete)
	                     isCompleate = true;
	             }
	         }catch (Exception ex){
	        	 ex.printStackTrace();
	         }
	     }
//	     if (!isCompleate){
//	    	 try{
//	             isCompleate = !Dev2Interface.Flow_IsCanDoCurrentWork(node.getFK_Flow(), this.FK_Node, this.WorkID, WebUser.getNo());
//	             //WorkFlow workFlow = new WorkFlow(node.FK_Flow, WorkID);
//	             //isCompleate = !workFlow.IsCanDoCurrentWork(WebUser.getNo());
//	         }catch (Exception e){
//	        	 e.printStackTrace();
//	         }
//	     }
	     if (isMenu && !isCompleate){
	    	 if (btnLab.getOfficeMarksEnable()){
	    		 divMenu.append("查看留痕:<select id='marks' onchange='ShowUserName()'  style='width: 100px'><option value='1'>显示留痕</option><option value='2'>隐藏留痕</option><select> ");
	        
	    	 }
	    	 if (btnLab.getOfficeSaveEnable()){
	    		 this.AddBtn("saveFile", btnLab.getOfficeSaveLab(), "saveOffice");
	         }
	    	 if (btnLab.getOfficeOpenEnable()){
	             this.AddBtn("openFile", btnLab.getOfficeOpenLab(), "OpenFile");
	         }
	    	 if (btnLab.getOfficeOpenTemplateEnable()){
	    		 this.AddBtn("openTempLate", btnLab.getOfficeOpenTemplateLab(), "OpenTempLate");
	         }
	    	 if (btnLab.getOfficeAcceptEnable()){
	    		 this.AddBtn("accept", btnLab.getOfficeAcceptLab(), "acceptOffice");
	         }
	    	 if (btnLab.getOfficeRefuseEnable()){
	    		 this.AddBtn("refuse", btnLab.getOfficeRefuseLab(), "refuseOffice");
	    	 }
	    	 if (btnLab.getOfficeOverEnable()){
	    		 this.AddBtn("over", btnLab.getOfficeOVerLab(), "overOffice");
	         }
	    	/* if (btnLab.getOfficePrintEnable()){
	    		 this.AddBtn("print", btnLab.getOfficePrintLab(), "printOffice");
	         }*/
	    	 if (btnLab.getOfficeSealEnable()){
	    		 this.AddBtn("seal", btnLab.getOfficeSealLab(), "sealOffice");
	         }
	    	 if (btnLab.getOfficeInsertFlowEnable()){
	    		 this.AddBtn("flow", btnLab.getOfficeInsertFlowLab(), "InsertFlow");
	         }
	    	 if (btnLab.getOfficeDownEnable()){
	    		 this.AddBtn("download", btnLab.getOfficeDownLab(), "DownLoad");
	         }
	    	 if (btnLab.getOfficeIsMarks())
	             IsMarks = true;
	         if (btnLab.getOfficeNodeInfo())
	             IsCheckInfo = true;
	     }
	     IsSavePDF = btnLab.getOfficeReSavePDF();
	
	     if (!StringHelper.isNullOrEmpty(getMarkName())){
	    	 AddBtn("ViewMarks", "文档痕迹", "ViewMark");
	     }
	         
	     if (isMenu){
	         LoadAttachment();
	     }
	     
	}
	private void SaveFile( HttpServletRequest _request, HttpServletResponse _response){
		  try{
              Node node = new Node(FK_Node);

              String fileStart = String.valueOf(WorkID);
              if (node.getHisNodeWorkType() == NodeWorkType.SubThreadWork){
                  fileStart = String.valueOf(FID);
              }
              DiskFileItemFactory dfif = new DiskFileItemFactory();
              ServletFileUpload servletFileUpload = new ServletFileUpload(dfif);
              List<FileItem> fileList = servletFileUpload.parseRequest(_request); //获取上传的文件
              
              if (!fileList.isEmpty()) {
              for(int i=0;i<fileList.size();i++){
            	FileItem file = fileList.get(i);
				String fileName = file.getName();//获取文件名
				String path =  this._request.getSession().getServletContext().getRealPath("/DataUser/OfficeFile/"+this.FK_Flow);
				File fileDir = new File(path);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				
				String fileExtension = FileAccess.getExtensionName(fileName);
				String realSaveTo = path +  File.separator  + fileStart + "." + fileExtension;
	               
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realSaveTo));
				
				if(IsSavePDF){
					String urlPath = basePath + "WF/FilesUpload.do";
					
					String boundary = "*****";  //boundary就是request头和上传文件内容的分隔符
					HttpURLConnection conn = null;
					try {  
						URL url = new URL(urlPath);  
			            conn = (HttpURLConnection) url.openConnection();  
			            conn.setConnectTimeout(10000); //连接超时为10秒   
			            conn.setReadTimeout(10000); 
			            //发送POST请求必须设置如下两行 
			            conn.setDoOutput(true);  
			            conn.setDoInput(true);  
			            conn.setUseCaches(false); 
			            
			            conn.setRequestProperty("Start", fileStart);
			            conn.setRequestProperty("Path", path);
			            conn.setRequestProperty("Extension", fileExtension);
			            conn.setRequestProperty("Type", "savePDF");
			            
			            conn.setRequestMethod("POST");  
			            conn.setRequestProperty("Connection", "Keep-Alive");  
			            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
			            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);  
			            
			            OutputStream out = new DataOutputStream(conn.getOutputStream());
			            
			            StringBuffer strBuf = new StringBuffer();  
	                    strBuf.append("\r\n").append("--").append(boundary).append("--\r\n");  
	                    strBuf.append("Content-Disposition: form-data; name=\"" + fileExtension + "\"; filename=\"" + fileStart + "\"\r\n");  
	                    strBuf.append("Content-Type:" + file.getContentType() + "\r\n\r\n"); 
	                    out.write(strBuf.toString().getBytes()); 
			          
			            DataInputStream in = new DataInputStream(file.getInputStream());
			            int bytes = 0;  
			            byte[] buffer = new byte[1024];  
			            while ((bytes = in.read(buffer)) != -1) {  
			                out.write(buffer, 0, bytes);  
			            }  
			            in.close(); 
			            
			            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();  
			            out.write(endData);  
			            
			            out.flush();  
			            out.close();
			            
					}catch (Exception e) {  
						 e.printStackTrace();  
				    } finally {  
			            if (conn != null) {  
			                conn.disconnect();  
			                conn = null;  
			            }  
				    }  
				}
			 }
              }
		  }catch (Exception e){
              e.printStackTrace();
          }
	  }
	 
	
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public int getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
	public int getFID() {
		return FID;
	}
	public void setFID(int fID) {
		FID = fID;
	}
	public long getWorkID() {
		return WorkID;
	}
	public void setWorkID(long workID) {
		WorkID = workID;
	}
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
	public boolean isIsTrueTH() {
		return IsTrueTH;
	}
	public void setIsTrueTH(boolean isTrueTH) {
		IsTrueTH = isTrueTH;
	}
	public String getIsTrueTHTemplate() {
		return IsTrueTHTemplate;
	}
	public void setIsTrueTHTemplate(String isTrueTHTemplate) {
		IsTrueTHTemplate = isTrueTHTemplate;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getHeBing() {
		return HeBing;
	}
	public void setHeBing(String heBing) {
		HeBing = heBing;
	}
	public boolean isReadOnly() {
		return ReadOnly;
	}
	public void setReadOnly(boolean readOnly) {
		ReadOnly = readOnly;
	}
	public boolean isIsCheckInfo() {
		return IsCheckInfo;
	}
	public void setIsCheckInfo(boolean isCheckInfo) {
		IsCheckInfo = isCheckInfo;
	}
	public String getNodeInfo() {
		return NodeInfo;
	}
	public void setNodeInfo(String nodeInfo) {
		NodeInfo = nodeInfo;
	}
	public boolean isIsSavePDF() {
		return IsSavePDF;
	}
	public void setIsSavePDF(boolean isSavePDF) {
		IsSavePDF = isSavePDF;
	}
	public boolean isIsMarks() {
		return IsMarks;
	}
	public void setIsMarks(boolean isMarks) {
		IsMarks = isMarks;
	}
	public String getOfficeTemplate() {
		return OfficeTemplate;
	}
	public void setOfficeTemplate(String officeTemplate) {
		OfficeTemplate = officeTemplate;
	}
	public boolean isIsLoadTempLate() {
		return IsLoadTempLate;
	}
	public void setIsLoadTempLate(boolean isLoadTempLate) {
		IsLoadTempLate = isLoadTempLate;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public String getSealType() {
		return sealType;
	}

	public void setSealType(String sealType) {
		this.sealType = sealType;
	}

	 public String getSealIndex() {
		return sealIndex;
	}
	public void setSealIndex(String sealIndex) {
		this.sealIndex = sealIndex;
	}
	private void AddBtn(String id, String label, String clickEvent){
		 Button btn = this.ui.creatButton(id);
		 btn.setText(label);
		 
         btn.addAttr("onclick", "return " + clickEvent + "()");
         btn.addAttr("class", "btn");;
         divMenu.append(btn);
     }
}
