package BP.Difference.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import BP.DA.DataType;
import BP.Difference.ContextHolderUtils;
import BP.Difference.SystemConfig;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.AthCtrlWay;
import BP.Sys.AthSaveWay;
import BP.Sys.AthUploadWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmEventList;
import BP.Sys.GEEntity;
import BP.Sys.MapData;
import BP.Sys.PubClass;
import BP.Tools.AesEncodeUtil;
import BP.Tools.FileAccess;
import BP.Tools.FtpUtil;
import BP.Tools.SftpUtil;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmSln;
import BP.WF.Template.WhoIsPK;
import BP.Web.WebUser;


@Controller
@RequestMapping("/WF/Ath")
@Scope("request") 
public class AttachmentUploadController extends BaseController {

	public String getFK_FrmAttachment() {
		return ContextHolderUtils.getRequest().getParameter("FK_FrmAttachment");
	}

	public String getTB_Note() {
		return ContextHolderUtils.getRequest().getParameter("TB_Note");
	}

	public String getddl() {
		return ContextHolderUtils.getRequest().getParameter("ddl");
	}

	public String getDelPKVal() {
		return ContextHolderUtils.getRequest().getParameter("DelPKVal");
	}

	public String getMyPK() {
		return ContextHolderUtils.getRequest().getParameter("MyPK");
	}

	public String getPKVal() {
		return ContextHolderUtils.getRequest().getParameter("PKVal");
	}

	public String getParasData() {
		return ContextHolderUtils.getRequest().getParameter("parasData");
	}
	public String getSort() {
		String sort =  ContextHolderUtils.getRequest().getParameter("Sort");
		if(DataType.IsNullOrEmpty(sort))
			sort ="";
		return sort;
	}
	
	public String getPWorkID() {
		return ContextHolderUtils.getRequest().getParameter("PWorkID");
	}

	@RequestMapping(value = "/AttachmentUpload.do")
	public void AttachmentUpload(@RequestParam("Filedata") MultipartFile multiFile, HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws Exception {
	
		MultipartHttpServletRequest multipartRequest = CommonFileUtils.getMultipartHttpServletRequest(request);
		String parasData = multipartRequest.getParameter("parasData");
		MultipartFile item = multipartRequest.getFile("file");
		if (item == null)
			item = multiFile;
		int maxSize = 50 * 1024 * 1024; // 单个上传文件大小的上限

		// 获取初始化信息
		FrmAttachment athDesc = new FrmAttachment(this.getFK_FrmAttachment());
		GEEntity en = new GEEntity(athDesc.getFK_MapData());
		en.setPKVal(this.getPKVal());
		en.RetrieveFromDBSources();
		MapData mapData = new MapData(athDesc.getFK_MapData());
		String msg = null;
		
		uploadFile(item, athDesc, en, msg, mapData, this.getFK_FrmAttachment(), parasData);

		return;
 
	}

	@RequestMapping(value = "/AttachmentUploadS.do", method = RequestMethod.POST)
	public void AttachmentUploadS(HttpServletRequest request, HttpServletResponse response, BindException errors)
			throws Exception {
		String error = "";
		MultipartHttpServletRequest multipartRequest = CommonFileUtils.getMultipartHttpServletRequest(request);
		List<MultipartFile> items = multipartRequest.getFiles("file");
		int maxSize = 50 * 1024 * 1024; // 单个上传文件大小的上限

		// 获取初始化信息
		FrmAttachment athDesc = new FrmAttachment(this.getFK_FrmAttachment());
		GEEntity en = new GEEntity(athDesc.getFK_MapData());
		en.setPKVal(this.getPKVal());
		en.Retrieve();
		MapData mapData = new MapData(athDesc.getFK_MapData());
		String msg = null;
		for(MultipartFile item : items)
			uploadFile(item, athDesc, en, msg, mapData, this.getFK_FrmAttachment(), getParasData());
		return;
		 
	}

	@RequestMapping(value = "/downLoad.do", method = RequestMethod.GET)
	public void downLoad(HttpServletRequest request, HttpServletResponse response) {
		FrmAttachmentDB downDB = new FrmAttachmentDB();

		try {
			
			downDB.setMyPK(this.getDelPKVal() == null ? this.getMyPK() : this.getDelPKVal());
			downDB.Retrieve();
			FrmAttachment dbAtt = new FrmAttachment();
			dbAtt.setMyPK(downDB.getFK_FrmAttachment());
			dbAtt.Retrieve();
			//获取文件是否加密
            boolean fileEncrypt = SystemConfig.getIsEnableAthEncrypt();
            boolean isEncrypt = downDB.GetParaBoolen("IsEncrypt");
            
			if (dbAtt.getAthSaveWay() == AthSaveWay.WebServer) {
				
				String filepath = downDB.getFileFullName();
				
				 if (fileEncrypt == true && isEncrypt == true)
                 {
					 filepath = downDB.getFileFullName() + ".tmp";
                     if (new File(filepath).exists() == true)
                    	 new File(filepath).delete();
                     
                     AesEncodeUtil.decryptFile(downDB.getFileFullName(), filepath);
                    
                 } 
				 
				PubClass.DownloadFile(filepath, downDB.getFileName());

			}

			if (dbAtt.getAthSaveWay() == AthSaveWay.FTPServer) {
				// #region 解密下载
				// 1、先下载到本地
				String guid = BP.DA.DBAccess.GenerGUID();
				// 把文件临时保存到一个位置.
				String temp = SystemConfig.getPathOfTemp() + "" + guid + ".tmp";

				// 解密的文件保存的路径
				String jieMiFile = SystemConfig.getPathOfTemp() +"/" + "" + guid + downDB.getFileExts();

				if (SystemConfig.getFTPServerType().equals("SFTP") ) {

					// 连接FTP服务器并下载文件到本地
					SftpUtil ftpUtil =BP.WF.Glo.getSftpUtil(); 
					ftpUtil.downloadFile(downDB.getFileFullName(), temp);
				}
				
				
				if (SystemConfig.getFTPServerType().equals("FTP") ) {

					// 连接FTP服务器并下载文件到本地
					FtpUtil ftpUtil =BP.WF.Glo.getFtpUtil();					 
					ftpUtil.downloadFile(downDB.getFileFullName(), temp);
				}

				if (fileEncrypt == true && isEncrypt == true){
					// 解密文件
					AesEncodeUtil.decryptFile(temp, jieMiFile);
				}else{
					jieMiFile = temp;
				}
				// #region 文件下载（并删除临时明文文件）
				jieMiFile = PubClass.toUtf8String(request, jieMiFile);

				response.setContentType("application/octet-stream;charset=utf8");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + PubClass.toUtf8String(request, downDB.getFileName()));
				response.setHeader("Connection", "close");
				// 读取目标文件，通过response将目标文件写到客户端
				// 读取文件
				InputStream in = new FileInputStream(new File(jieMiFile));
				OutputStream out = response.getOutputStream();
				// 写文件
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				in.close();
				out.close();

				// 删除临时文件
				new File(temp).delete();
				if(new File(jieMiFile).exists() == true)
					new File(jieMiFile).delete();
			}

			if (dbAtt.getAthSaveWay() == AthSaveWay.DB) {

				PubClass.DownloadFile(downDB.getFileFullName(), downDB.getFileName());
			}

			return;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	@RequestMapping(value = "/EntityFileLoad.do", method = RequestMethod.GET)
	public void EntityFileLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 //根据EnsName获取Entity
        Entities ens = ClassFactory.GetEns(this.getEnsName());
        Entity en = ens.getNewEntity();
        en.setPKVal(this.getDelPKVal());
        int i = en.RetrieveFromDBSources();
        if (i == 0)
            return ;
        
        String filePath = (String)en.GetValByKey("MyFilePath");
        String fileName = (String)en.GetValByKey("MyFileName");
        //获取使用的客户 TianYe集团保存在FTP服务器上
        if (SystemConfig.getCustomerNo().equals("TianYe"))
        {
           
            //临时存储位置
            String guid = BP.DA.DBAccess.GenerGUID();
            String tempFile = SystemConfig.getPathOfTemp() + guid + "." + en.GetValByKey("MyFileExt");

            if (new File(tempFile).exists() == true)
            	new File(tempFile).delete();
            
            // 连接FTP服务器并下载文件到本地
			FtpUtil ftpUtil =BP.WF.Glo.getFtpUtil();					 
			ftpUtil.downloadFile(filePath, tempFile);

     
            PubClass.DownloadFile(tempFile, fileName);
            //删除临时文件
            new File(tempFile);
        }
        else
        {
        	PubClass.DownloadFile(filePath, fileName);
           
        }
        
		

	}

	
	private String GetRealPath(String fileFullName) throws Exception {
		boolean isFile = false;
		String downpath = "";
		try {
			// 如果相对路径获取不到可能存储的是绝对路径
			File downInfo = new File(
					ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + fileFullName));
			isFile = true;
			downpath = ContextHolderUtils.getRequest().getSession().getServletContext()
					.getRealPath("~/" + fileFullName);
		} catch (Exception e) {
			File downInfo = new File(fileFullName);
			isFile = true;
			downpath = fileFullName;
		}
		if (!isFile) {
			throw new Exception("没有找到下载的文件路径！");
		}

		return downpath;
	}

	private void uploadFile(MultipartFile item, FrmAttachment athDesc, GEEntity en, String msg, MapData mapData,
			String attachPk, String parasData) throws Exception {
		String pkVal = this.getPKVal();
		//获取sort\
		String sort = this.getSort();
        if (DataType.IsNullOrEmpty(sort))
        {
            if (parasData != null && parasData.length() > 0)
            {
                for (String para : parasData.split("@"))
                {
                    if (para.indexOf("Sort") != -1)
                        sort = para.split("=")[1];
                }
            }
        }
        
      //求主键. 如果该表单挂接到流程上.
        if (this.getFK_Node() != 0 && this.getFK_Node()!=999999)
        {
            //判断表单方案。
            FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), athDesc.getFK_MapData());
            if (fn.getFrmSln() == FrmSln.Readonly)
                throw new Exception("err@不允许上传附件.");

            //是默认的方案的时候.
            if (fn.getFrmSln() == FrmSln.Default)
            {
                //判断当前方案设置的whoIsPk ，让附件集成 whoIsPK 的设置。
                if (fn.getWhoIsPK() == WhoIsPK.FID)
                    pkVal = Long.toString(this.getFID());

                if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
                    pkVal = this.getPWorkID();
				if (fn.getWhoIsPK() == WhoIsPK.OID)
				{
					//如果是继承模式(AthUploadWay.Inherit)，上传附件使用本流程的WorkID,pkVal不做处理

					//如果是协作模式(AthUploadWay.Interwork),上传附件就是用控制呈现模式
					if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
					{
						if (athDesc.getHisCtrlWay() == AthCtrlWay.FID)
							pkVal = Long.toString(this.getFID());;
						if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
							pkVal = this.getPWorkID();
						if (athDesc.getHisCtrlWay() == AthCtrlWay.P2WorkID)
						{
							//根据流程的PWorkID获取他的P2流程
							String pWorkID = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + this.getPWorkID(), "0");
							pkVal = pWorkID;
						}
						if (athDesc.getHisCtrlWay() == AthCtrlWay.P3WorkID)
						{
							String sql = "Select PWorkID From WF_GenerWorkFlow Where WorkID=(Select PWorkID From WF_GenerWorkFlow Where WorkID=" + this.getPWorkID() + ")";
							//根据流程的PWorkID获取他的P2流程
							pkVal = BP.DA.DBAccess.RunSQLReturnStringIsNull(sql,"0");

						}
					}
				}
            }

            //自定义方案.
            if (fn.getFrmSln() == FrmSln.Self)
            {
				FrmAttachment myathDesc = new FrmAttachment();
				myathDesc.setMyPK(attachPk + "_" + this.getFK_Node());
                int count = myathDesc.RetrieveFromDBSources();
                if(count !=0){
	                if (myathDesc.getHisCtrlWay() == AthCtrlWay.FID)
	                    pkVal =Long.toString(this.getFID());
	
	                if (myathDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
	                    pkVal = this.getPWorkID();
                }
            }
        }
        //获取上传文件是否需要加密
        boolean fileEncrypt = SystemConfig.getIsEnableAthEncrypt();
        
		// 获取文件名
		String fileName = item.getOriginalFilename();
		// 扩展名
		String exts = FileAccess.getExtensionName(fileName).toLowerCase().replace(".", "");

		if (athDesc.getAthSaveWay() == AthSaveWay.WebServer) {

			String savePath = athDesc.getSaveTo();
			if (savePath.contains("@") == true || savePath.contains("*") == true) {
				/* 如果有变量 */
				savePath = savePath.replace("*", "@");
				savePath = BP.WF.Glo.DealExp(savePath, en, null);

				if (savePath.contains("@") && this.getFK_Node() != 0) {
					/* 如果包含 @ */
					BP.WF.Flow flow = new BP.WF.Flow(this.getFK_Flow());
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(this.getWorkID());
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true)
					throw new Exception("@路径配置错误,变量没有被正确的替换下来." + savePath);
				return;
			} else {
				savePath = athDesc.getSaveTo() +  getPKVal();
			}

			// 替换关键的字串.
			savePath = savePath.replace("\\\\", "/");
			try {
				if (savePath.indexOf(":") == -1)
					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);

				File fileInfo = new File(savePath);

				if (fileInfo.exists() == false)
					fileInfo.mkdirs();

			} catch (Exception ex) {
				throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath)
						+ "===" + savePath + "@技术问题:" + ex.getMessage());

			}

			String guid = BP.DA.DBAccess.GenerGUID();
			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
			String ext = FileAccess.getExtensionName(item.getOriginalFilename());
			String realSaveTo = savePath + "/" + guid + "." + fileName + "." + ext;

			realSaveTo = realSaveTo.replace("~", "-");
			realSaveTo = realSaveTo.replace("'", "-");
			realSaveTo = realSaveTo.replace("*", "-");
			
			String saveTo = realSaveTo;
			if (fileEncrypt == true)
				saveTo = realSaveTo + ".tmp";
			File file = new File(saveTo); // 获取根目录对应的真实物理路径
			
			try {
				// 构造临时对象
				InputStream is = item.getInputStream();
				int buffer = 1024; // 定义缓冲区的大小
				int length = 0;
				byte[] b = new byte[buffer];
				FileOutputStream fos = new FileOutputStream(file);
				while ((length = is.read(b)) != -1) {
					// 计算上传文件的百分比
					fos.write(b, 0, length); // 向文件输出流写读取的数据
				}
				fos.close();
			} catch (RuntimeException ex) {
				
				throw new RuntimeException("@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage());
			}
			
			 if (fileEncrypt == true)
             {
                 File fileT = new File(saveTo);
                 AesEncodeUtil.encryptFile(saveTo, realSaveTo);
                 fileT.delete();//删除临时文件
             }
            
			// 执行附件上传前事件，added by liuxc,2017-7-15
			msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en,
					"@FK_FrmAttachment=" + athDesc.getMyPK() + "@FileFullName=" + realSaveTo);
			if (!DataType.IsNullOrEmpty(msg)) {
				BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.getName() + "，" + msg);
				file.delete();
				return;
			}

			// Glo.File_JiaMi(realSaveTo,"D:"+ "//" + guid + "." + ext);
			File info = new File(realSaveTo);

			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();			 
			dbUpload.setSort(this.getSort());
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFileExts(exts);
			dbUpload.setFID(this.getFID());
			dbUpload.setNodeID( String.valueOf(this.getFK_Node()));
			 if (fileEncrypt == true)
                 dbUpload.SetPara("IsEncrypt", 1);
			 
			if (athDesc.getIsExpCol() == true) {
				if (parasData != null && parasData.length() > 0) {
					for (String para : parasData.split("@")) {
						if (para.split("=").length == 2)
							dbUpload.SetPara(para.split("=")[0], para.split("=")[1]);
					}
				}
			}

			/// #region 处理文件路径，如果是保存到数据库，就存储pk.
			if (athDesc.getAthSaveWay() == AthSaveWay.WebServer) {
				// 文件方式保存
				dbUpload.setFileFullName(realSaveTo);
			}

			if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
				// 保存到数据库
				dbUpload.setFileFullName(dbUpload.getMyPK());
			}
			/// #endregion 处理文件路径，如果是保存到数据库，就存储pk.

			dbUpload.setFileName(item.getOriginalFilename());
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(WebUser.getName());
			dbUpload.setFID(this.getFID());
			dbUpload.setUploadGUID(guid);
			dbUpload.setRefPKVal(pkVal);
			
			dbUpload.Insert();

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				// 执行文件保存.
				BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK",
						dbUpload.getMyPK(), "FDB");
			}

			// 执行附件上传后事件，added by liuxc,2017-7-15
			msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en,
					"@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK()
							+ "@FileFullName=" + dbUpload.getFileFullName());
			if (!DataType.IsNullOrEmpty(msg))
				BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);
		}
		/// #endregion 文件上传的iis服务器上 or db数据库里.

		/// #region 保存到数据库 / FTP服务器上.
		if (athDesc.getAthSaveWay() == AthSaveWay.DB || athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
			String guid = BP.DA.DBAccess.GenerGUID();

			// 把文件临时保存到一个位置.
			String temp = SystemConfig.getPathOfTemp() +"/"+ "" + guid + ".tmp";
			
			String tempD = temp;
			if (fileEncrypt == true)
				tempD = SystemConfig.getPathOfTemp()+"/"+ "" + guid + "_Desc" + ".tmp";
			File tempFile = new File(tempD);
			InputStream is = null;
			try {
				// 构造临时对象
				is = item.getInputStream();
				int buffer = 1024; // 定义缓冲区的大小
				int length = 0;
				byte[] b = new byte[buffer];
				FileOutputStream fos = new FileOutputStream(tempFile);
				while ((length = is.read(b)) != -1) {
					fos.write(b, 0, length); // 向文件输出流写读取的数据
				}
				fos.close();
				is.close();
			} catch (Exception ex) {
				tempFile.delete();
				throw new RuntimeException("@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage());

			}
			 if (fileEncrypt == true)
             {
                 File fileTD =  new File(tempD);
                 AesEncodeUtil.encryptFile(tempD, temp);//加密
                 fileTD.delete();//删除临时文件
             }

			// 执行附件上传前事件，added by liuxc,2017-7-15
			msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en,
					"@FK_FrmAttachment=" + athDesc.getMyPK() + "@FileFullName=" + temp);
			if (DataType.IsNullOrEmpty(msg) == false) {
				BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + fileName + "，" + msg);

				tempFile.delete();

				throw new Exception("err@上传附件错误：" + msg);
			}

			File info = new File(temp);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(BP.DA.DBAccess.GenerGUID());
			dbUpload.setNodeID( String.valueOf(getFK_Node()));
			dbUpload.setSort(this.getSort());
			dbUpload.setFID(this.getFID()); // 流程id.

			dbUpload.setRefPKVal(pkVal);
			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(athDesc.getMyPK());
			dbUpload.setFileName(item.getOriginalFilename());
			dbUpload.setFileExts(exts);
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(WebUser.getName());
			if (fileEncrypt == true)
                  dbUpload.SetPara("IsEncrypt", 1);
			if (athDesc.getIsExpCol() == true) {
				if (parasData != null && parasData.length() > 0) {
					for (String para : parasData.split("@")) {
						if (para.split("=").length == 2)
							dbUpload.SetPara(para.split("=")[0], para.split("=")[1]);
					}
				}
			}

			dbUpload.setUploadGUID(guid);

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				dbUpload.Insert();
				// 把文件保存到指定的字段里.
				dbUpload.SaveFileToDB("FileDB", temp);
			}
			

			if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM");
				String ny = sdf.format(new Date());

				String workDir = ny + "/" + athDesc.getFK_MapData() + "/";
  
				//特殊处理文件路径.
				if (SystemConfig.getCustomerNo().equals( "BWDA") ) {
					
					sdf = new SimpleDateFormat("yyyy_MM_dd");
					ny = sdf.format(new Date());

					ny = ny.replace("_", "/");
					ny = ny.replace("_", "/");
					
					workDir =  ny+ "/" + WebUser.getNo()+"/";
				}
				
				boolean  isOK=false;
				
				if (SystemConfig.getFTPServerType().equals("FTP") ) {

					FtpUtil ftpUtil = BP.WF.Glo.getFtpUtil();
					
					ftpUtil.changeWorkingDirectory(workDir,true);

					// 把文件放在FTP服务器上去.
					isOK=ftpUtil.uploadFile( guid + "." + dbUpload.getFileExts(),temp);

					ftpUtil.releaseConnection();
				}

				if (SystemConfig.getFTPServerType().equals("SFTP") ) {

					SftpUtil ftpUtil = BP.WF.Glo.getSftpUtil();
					 
					ftpUtil.changeWorkingDirectory(workDir,true);
					// 把文件放在FTP服务器上去.
					isOK=ftpUtil.uploadFile(guid + "." + dbUpload.getFileExts(),temp);
					ftpUtil.releaseConnection();
				}

				// 删除临时文件
				tempFile.delete();
				new File(SystemConfig.getPathOfTemp() + "" + guid + "_Desc" + ".tmp").delete();

				// 设置路径.
				dbUpload.setFileFullName( workDir  + guid + "." + dbUpload.getFileExts());
				
				if (isOK==false)
					throw new com.sun.star.uno.Exception("err文件上传失败，请检查ftp服务器配置信息");
					
				dbUpload.Insert();
				
			}

			// 执行附件上传后事件，added by liuxc,2017-7-15
			msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en,
					"@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK()
							+ "@FileFullName=" + temp);
			
			if (DataType.IsNullOrEmpty(msg)==false)
				BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);
		}
		/// #endregion 保存到数据库.
		return;
	}

}
