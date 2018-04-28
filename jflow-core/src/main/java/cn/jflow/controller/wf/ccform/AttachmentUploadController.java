package cn.jflow.controller.wf.ccform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import BP.DA.DataType;
import BP.Sys.AthSaveWay;
import BP.Sys.AthUploadWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.GEEntity;
import BP.Sys.PubClass;
import BP.Tools.FileAccess;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.common.BaseController;

@Controller
@RequestMapping("/WF/CCForm")
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
	
	@RequestMapping(value = "/AttachmentUpload.do", method = RequestMethod.POST)
	public void upload(@RequestParam("Filedata")MultipartFile multiFile,HttpServletRequest request, HttpServletResponse response, BindException errors)
			throws Exception {
		String error = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest .getFile("file");
		if(item == null) item = (CommonsMultipartFile) multiFile;
		int maxSize = 50 * 1024 * 1024; // 单个上传文件大小的上限

		if (item.getOriginalFilename() != null && !item.getOriginalFilename().equals("")) {// 判断是否选择了文件
			long upFileSize = item.getSize(); // 上传文件的大小
			String fileName = item.getOriginalFilename(); // 获取文件名
			if (upFileSize > maxSize) {
				error = "您上传的文件太大，请选择不超过50M的文件";
				
				return ;
			}
			FrmAttachment athDesc = new FrmAttachment( this.getFK_FrmAttachment());

			String exts = FileAccess.getExtensionName(fileName).toLowerCase() .replace(".", "");

			// 如果有上传类型限制，进行判断格式
			if (athDesc.getExts().equals("*.*") || athDesc.getExts().equals("")) {
				// 任何格式都可以上传
			} else {
				if (!athDesc.getExts().toLowerCase().contains(exts)) {
				
					error = "您上传的文件，不符合系统的格式要求，要求的文件格式:" + athDesc.getExts() + "，您现在上传的文件格式为:" + exts;
					
					return ;
				}
			}

			String savePath = athDesc.getSaveTo();

			if (savePath.contains("@") || savePath.contains("*")) {
				// 如果有变量
				savePath = savePath.replace("*", "@");
				GEEntity en = new GEEntity(athDesc.getFK_MapData());
				en.setPKVal(this.getPKVal());
				en.Retrieve();
				savePath = BP.WF.Glo.DealExp(savePath, en, null);

				if (savePath.contains("@") && this.getFK_Node() != 0) {
					// 如果包含 @
					Flow flow = new Flow(
							this.getFK_Flow());
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(this.getWorkID());
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@")) {
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来."
							+ savePath);
				}
			} 

			// 替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");
			try {
				if(savePath.indexOf(":")==-1){
					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);
				}
			} catch (RuntimeException e) {

			}
			try {
				File fileInfo = new File(savePath);

				if (!fileInfo.exists()) {
					fileInfo.mkdirs();
				}
			} catch (RuntimeException ex) {
				throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath) + "==="
						+ savePath + "@技术问题:" + ex.getMessage());
			}

			String guid = BP.DA.DBAccess.GenerGUID();

			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
			String ext = FileAccess
					.getExtensionName(item.getOriginalFilename());


			String realSaveTo = savePath + File.separator + guid + "." + fileName + "." + ext;

			String saveTo = realSaveTo;

			try {
				// 构造临时对象
				File file = new File(realSaveTo); // 获取根目录对应的真实物理路径
				InputStream is = item.getInputStream();
				int buffer = 1024; // 定义缓冲区的大小
				int length = 0;
				byte[] b = new byte[buffer];
				double percent = 0;
				FileOutputStream fos = new FileOutputStream(file);
				while ((length = is.read(b)) != -1) {
					percent += length / (double) upFileSize * 100D; // 计算上传文件的百分比
					fos.write(b, 0, length); // 向文件输出流写读取的数据
					// session.setAttribute("progressBar",Math.round(percent));
					// //将上传百分比保存到Session中
				}
				fos.close();
			} catch (RuntimeException ex) {
				error = "@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage();
				
				return ;
			}

			File info = new File(realSaveTo);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();

			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
			dbUpload.setNodeID((new Integer(getFK_Node())).toString());
			dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

			if (athDesc.getAthUploadWay() == AthUploadWay.Inherit) {
				// 如果是继承，就让他保持本地的PK.
				dbUpload.setRefPKVal(this.getPKVal().toString());
			}

			if (athDesc.getAthUploadWay() == AthUploadWay.Interwork) {
				// 如果是协同，就让他是PWorkID.
				String pWorkID = String.valueOf(BP.DA.DBAccess
						.RunSQLReturnValInt(
								"SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID="
										+ this.getPKVal(), 0));
				if (pWorkID == null || pWorkID.equals("0")) {
					pWorkID = this.getPKVal();
				}

				dbUpload.setRefPKVal(pWorkID);
			}

			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

			dbUpload.setFileExts(ext);
			dbUpload.setFileFullName(saveTo);
			dbUpload.setFileName(item.getOriginalFilename());
			dbUpload.setFileSize(item.getSize());

			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(WebUser.getName());
			if (athDesc.getIsNote()) {
				dbUpload.setMyNote(this.getTB_Note());
			}

			if (athDesc.getSort().contains(",")) {
				dbUpload.setSort(this.getddl());
			}

			dbUpload.setUploadGUID(guid);
			dbUpload.Insert();
		} else {
			error = "没有选择上传文件！";
			
			return;
		}
		
		try {
			
			//BaseModel.sendRedirect(Glo.getCCFlowAppPath()+"WF/CCForm/Ath.htm?"+request.getQueryString());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return;
		}
	}
	
	@RequestMapping(value = "/AttachmentUploadS.do", method = RequestMethod.POST)
	public void execute(HttpServletRequest request, HttpServletResponse response, BindException errors)
			throws Exception {
		String error = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest .getFile("file");
		int maxSize = 50 * 1024 * 1024; // 单个上传文件大小的上限

		if (item.getOriginalFilename() != null && !item.getOriginalFilename().equals("")) {// 判断是否选择了文件
			long upFileSize = item.getSize(); // 上传文件的大小
			String fileName = item.getOriginalFilename(); // 获取文件名
			if (upFileSize > maxSize) {
				error = "您上传的文件太大，请选择不超过50M的文件";				
				return ;
			}
			FrmAttachment athDesc = new FrmAttachment( this.getFK_FrmAttachment());

			String exts = FileAccess.getExtensionName(fileName).toLowerCase() .replace(".", "");

			// 如果有上传类型限制，进行判断格式
			if (athDesc.getExts().equals("*.*") || athDesc.getExts().equals("")) {
				// 任何格式都可以上传
			} else {
				if (!athDesc.getExts().toLowerCase().contains(exts)) {
				
					error = "您上传的文件，不符合系统的格式要求，要求的文件格式:" + athDesc.getExts() + "，您现在上传的文件格式为:" + exts;
					return ;
				}
			}

			String savePath = athDesc.getSaveTo();

			if (savePath.contains("@") || savePath.contains("*")) {
				// 如果有变量
				savePath = savePath.replace("*", "@");
				GEEntity en = new GEEntity(athDesc.getFK_MapData());
				en.setPKVal(this.getPKVal());
				en.Retrieve();
				savePath = BP.WF.Glo.DealExp(savePath, en, null);

				if (savePath.contains("@") && this.getFK_Node() != 0) {
					// 如果包含 @
					Flow flow = new Flow(
							this.getFK_Flow());
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(this.getWorkID());
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@")) {
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来."
							+ savePath);
				}
			} 

			// 替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");
			try {
				if(savePath.indexOf(":")==-1){
					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);
				}
			} catch (RuntimeException e) {

			}
			try {
				File fileInfo = new File(savePath);

				if (!fileInfo.exists()) {
					fileInfo.mkdirs();
				}
			} catch (RuntimeException ex) {
				throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath) + "==="
						+ savePath + "@技术问题:" + ex.getMessage());
			}

			String guid = BP.DA.DBAccess.GenerGUID();

			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
			String ext = FileAccess
					.getExtensionName(item.getOriginalFilename());


			String realSaveTo = savePath + guid + "." + fileName + "." + ext;

			String saveTo = realSaveTo;

			try {
				// 构造临时对象
				File file = new File(realSaveTo); // 获取根目录对应的真实物理路径
				InputStream is = item.getInputStream();
				int buffer = 1024; // 定义缓冲区的大小
				int length = 0;
				byte[] b = new byte[buffer];
				double percent = 0;
				FileOutputStream fos = new FileOutputStream(file);
				while ((length = is.read(b)) != -1) {
					percent += length / (double) upFileSize * 100D; // 计算上传文件的百分比
					fos.write(b, 0, length); // 向文件输出流写读取的数据
					// session.setAttribute("progressBar",Math.round(percent));
					// //将上传百分比保存到Session中
				}
				fos.close();
			} catch (RuntimeException ex) {
				error = "@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:" + ex.getMessage();
				
				return ;
			}

			File info = new File(realSaveTo);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();

			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
			dbUpload.setNodeID((new Integer(getFK_Node())).toString());
			dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

			if (athDesc.getAthUploadWay() == AthUploadWay.Inherit) {
				// 如果是继承，就让他保持本地的PK.
				dbUpload.setRefPKVal(this.getPKVal().toString());
			}

			if (athDesc.getAthUploadWay() == AthUploadWay.Interwork) {
				// 如果是协同，就让他是PWorkID.
				String pWorkID = String.valueOf(BP.DA.DBAccess
						.RunSQLReturnValInt(
								"SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID="
										+ this.getPKVal(), 0));
				if (pWorkID == null || pWorkID.equals("0")) {
					pWorkID = this.getPKVal();
				}

				dbUpload.setRefPKVal(pWorkID);
			}

			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

			dbUpload.setFileExts(ext);
			System.out.println("setname:"+saveTo);
			dbUpload.setFileFullName(saveTo);
			dbUpload.setFileName(item.getOriginalFilename());
			dbUpload.setFileSize(item.getSize());

			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(WebUser.getName());
			if (athDesc.getIsNote()) {
				dbUpload.setMyNote(this.getTB_Note());
			}

			if (athDesc.getSort().contains(",")) {
				dbUpload.setSort(this.getddl());
			}

			dbUpload.setUploadGUID(guid);
			dbUpload.Insert();
		} else {
			error = "没有选择上传文件！";
			
			return;
		}
		 

	}
	
	@RequestMapping(value = "/downLoad.do", method = RequestMethod.GET)
	public void downLoad(HttpServletRequest request, HttpServletResponse response){
		 FrmAttachmentDB downDB = new FrmAttachmentDB();
        
         try {
        	 downDB.setMyPK(this.getDelPKVal() == null ? this.getMyPK() : this.getDelPKVal());
             downDB.Retrieve();
             FrmAttachment dbAtt = new FrmAttachment();
             dbAtt.setMyPK(downDB.getFK_FrmAttachment());
             dbAtt.Retrieve();
          /*   if (dbAtt.getAthSaveWay() == AthSaveWay.IISServer)
             {
                 //#region 解密下载
                 //1、先解密到本地
                 String filepath = downDB.getFileFullName() + ".tmp";
                 String tempName = downDB.getFileFullName();

                 //EncHelper.DecryptDES(downDB.getFileFullName(), filepath);
                 
                 //#region 文件下载（并删除临时明文文件）
                 tempName = PubClass.toUtf8String(request, tempName);

                 response.setContentType("application/octet-stream;charset=utf8");
         		response.setHeader("Content-Disposition", "attachment;filename=" + tempName);
         		response.setHeader("Connection", "close");
 

              // 读取目标文件，通过response将目标文件写到客户端
         		// 读取文件
         		InputStream in = new FileInputStream(new File(filepath));
         		OutputStream out = response.getOutputStream();
         		// 写文件
         		int b;
         		while ((b = in.read()) != -1) {
         			out.write(b);
         		}
         		in.close();
         		out.close();
                 
             }

             if (dbAtt.getAthSaveWay() == AthSaveWay.FTPServer)
             {
                 PubClass.DownloadFile(downDB.GenerTempFile(dbAtt.getAthSaveWay()), downDB.getFileName());
             }

             if (dbAtt.getAthSaveWay() == AthSaveWay.DB)
             {*/
                 String downpath = downDB.getFileFullName();
                 BP.Sys.PubClass.DownloadFileByBuffer(downpath, downDB.getFileName());
//                 PubClass.DownloadHttpFile(downDB.getFileFullName(), downDB.getFileName(),response);
             //}
            
             return;
             
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 private String GetRealPath(String fileFullName) throws Exception
     {
         boolean isFile = false;
         String downpath = "";
         try
         {
             //如果相对路径获取不到可能存储的是绝对路径
             File downInfo = new File(ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + fileFullName));
             isFile = true;
             downpath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + fileFullName);
         }catch (Exception e ){
             File downInfo = new File(fileFullName);
             isFile = true;
             downpath = fileFullName;
         }
         if (!isFile)
         {
             throw new Exception("没有找到下载的文件路径！");
         }

         return downpath;
     }
	
	 
//	 private String uploadFile(File file,FrmAttachment athDesc,GEEntity en){
//		 ///#region 文件上传的iis服务器上 or db数据库里.
//         if (athDesc.getAthSaveWay() == AthSaveWay.IISServer){
//
//             String savePath = athDesc.getSaveTo();
//             if (savePath.contains("@") == true || savePath.contains("*") == true){
//                 /*如果有变量*/
//                 savePath = savePath.replace("*", "@");
//                 savePath = BP.WF.Glo.DealExp(savePath, en, null);
//
//                 if (savePath.contains("@") && this.getFK_Node() != 0){
//                     /*如果包含 @ */
//                     BP.WF.Flow flow = new BP.WF.Flow(this.getFK_Flow());
//                     BP.WF.Data.GERpt myen = flow.getHisGERpt();
//                     myen.setOID(this.getWorkID());
//                     myen.RetrieveFromDBSources();
//                     savePath = BP.WF.Glo.DealExp(savePath, myen, null);
//                 }
//                 if (savePath.contains("@") == true)
//                     throw new Exception("@路径配置错误,变量没有被正确的替换下来." + savePath);
//           }else{
//              savePath = athDesc.getSaveTo() + "\\" + getPKVal();
//           }
//
//          //替换关键的字串.
//          savePath = savePath.replace("\\\\", "\\");
//           try{
//            	 if(savePath.indexOf(":")==-1)
// 					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);
//            	  File fileInfo = new File(savePath);
//
//	 			 if (!fileInfo.exists()) 
//	 				fileInfo.mkdirs();
// 				
//             }catch (Exception ex){
//            	 throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
// 						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath) + "==="
// 						+ savePath + "@技术问题:" + ex.getMessage());
//             }
//
//             String exts = System.IO.Path.GetExtension(file.getName()).ToLower().Replace(".", "");
//             String guid = BP.DA.DBAccess.GenerGUID();
//             String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
//             String ext = System.IO.Path.GetExtension(file.getName());
//             String realSaveTo = savePath + "\\" + guid + "." + fileName + ext;
//
//             realSaveTo = realSaveTo.replace("~", "-");
//             realSaveTo = realSaveTo.replace("'", "-");
//             realSaveTo = realSaveTo.replace("*", "-");
//
//             file.saveAs(realSaveTo);
//
//             //执行附件上传前事件，added by liuxc,2017-7-15
//             msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + realSaveTo);
//             if (!DataType.IsNullOrEmpty(msg))
//             {
//                 BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);
//
//                 try
//                 {
//                     File.Delete(realSaveTo);
//                 }
//                 catch { }
//
//                
//                 return;
//             }
//
//             File info = new File(realSaveTo);
//
//             FrmAttachmentDB dbUpload = new FrmAttachmentDB();
//             dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
//             dbUpload.setNodeID(String.valueOf(this.getFK_Node()));
//             dbUpload.setFK_FrmAttachment(attachPk);
//             dbUpload.setFK_MapData(athDesc.getFK_MapData());
//             dbUpload.setFK_FrmAttachment(attachPk);
//             dbUpload.setFileExts(info.getExtension());
//
//             ///#region 处理文件路径，如果是保存到数据库，就存储pk.
//             if (athDesc.getAthSaveWay() == AthSaveWay.IISServer)
//             {
//                 //文件方式保存
//                 dbUpload.setFileFullName(realSaveTo);
//             }
//
//             if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
//             {
//                 //保存到数据库
//                 dbUpload.setFileFullName(dbUpload.getMyPK());
//             }
//             ///#endregion 处理文件路径，如果是保存到数据库，就存储pk.
//
//             dbUpload.setFileName(file.getName());
//             dbUpload.setFileSize ((float)info.length());
//             dbUpload.setRDT(DataType.getCurrentDataTimess());
//             dbUpload.setRec(BP.Web.WebUser.getNo());
//             dbUpload.setRecName(BP.Web.WebUser.getName());
//             dbUpload.setRefPKVal(getPKVal());
//             dbUpload.setFID(this.getFID());
//
//             //if (athDesc.IsNote)
//             //    dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;
//
//             //if (athDesc.Sort.Contains(","))
//             //    dbUpload.Sort = this.Pub1.GetDDLByID("ddl").SelectedItemStringVal;
//
//             dbUpload.setUploadGUID(guid);
//             dbUpload.Insert();
//
//             if (athDesc.getAthSaveWay() == AthSaveWay.DB)
//             {
//                 //执行文件保存.
//                 BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.getMyPK(), "FDB");
//             }
//
//             //执行附件上传后事件，added by liuxc,2017-7-15
//             msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK() + "@FileFullName=" + dbUpload.getFileFullName());
//             if (!DataType.IsNullOrEmpty(msg))
//                 BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);
//         }
//         ///#endregion 文件上传的iis服务器上 or db数据库里.
//
//         ///#region 保存到数据库 / FTP服务器上.
//         if (athDesc.getAthSaveWay() == AthSaveWay.DB || athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
//         {
//             string guid = DBAccess.GenerGUID();
//
//             //把文件临时保存到一个位置.
//             string temp = SystemConfig.PathOfTemp + "" + guid + ".tmp";
//             try
//             {
//                 file.SaveAs(temp);
//             }
//             catch (Exception ex)
//             {
//                 System.IO.File.Delete(temp);
//                 file.SaveAs(temp);
//             }
//
//           //  fu.SaveAs(temp);
//
//             //执行附件上传前事件，added by liuxc,2017-7-15
//             msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + temp);
//             if (DataType.IsNullOrEmpty(msg) == false)
//             {
//                 BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);
//
//                 try
//                 {
//                     File.Delete(temp);
//                 }
//                 catch
//                 {
//                 }
//
//                 throw new Exception("err@上传附件错误：" + msg);
//              }
//
//             FileInfo info = new FileInfo(temp);
//             FrmAttachmentDB dbUpload = new FrmAttachmentDB();
//             dbUpload.MyPK = BP.DA.DBAccess.GenerGUID();
//             dbUpload.NodeID = FK_Node.ToString();
//             dbUpload.FK_FrmAttachment = athDesc.MyPK;
//             dbUpload.FID = this.FID; //流程id.
//             if (athDesc.AthUploadWay == AthUploadWay.Inherit)
//             {
//                 /*如果是继承，就让他保持本地的PK. */
//                 dbUpload.RefPKVal = PKVal.ToString();
//             }
//
//             if (athDesc.AthUploadWay == AthUploadWay.Interwork)
//             {
//                 /*如果是协同，就让他是PWorkID. */
//                 string pWorkID = BP.DA.DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + PKVal, 0).ToString();
//                 if (pWorkID == null || pWorkID == "0")
//                     pWorkID = PKVal;
//                 dbUpload.RefPKVal = pWorkID;
//             }
//
//             dbUpload.FK_MapData = athDesc.FK_MapData;
//             dbUpload.FK_FrmAttachment = athDesc.MyPK;
//            // dbUpload.AthSaveWay = athDesc.AthSaveWay; //设置保存方式,以方便前台展示读取.
//             //dbUpload.FileExts = info.Extension;
//             // dbUpload.FileFullName = saveTo;
//             dbUpload.FileName = file.FileName;
//             dbUpload.FileSize = (float)info.Length;
//             dbUpload.RDT = DataType.CurrentDataTimess;
//             dbUpload.Rec = BP.Web.WebUser.No;
//             dbUpload.RecName = BP.Web.WebUser.Name;
//
//
//             dbUpload.UploadGUID = guid;
//
//             if (athDesc.AthSaveWay == AthSaveWay.DB)
//             {
//                 dbUpload.Insert();
//                 //把文件保存到指定的字段里.
//                 dbUpload.SaveFileToDB("FileDB", temp);
//             }
//
//             if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
//             {
//                 /*保存到fpt服务器上.*/
//                 FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP,
//                     SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);
//
//                 string ny = DateTime.Now.ToString("yyyy_MM");
//
//                 //判断目录年月是否存在.
//                 if (ftpconn.DirectoryExist(ny) == false)
//                     ftpconn.CreateDirectory(ny);
//                 ftpconn.SetCurrentDirectory(ny);
//
//                 //判断目录是否存在.
//                 if (ftpconn.DirectoryExist(athDesc.FK_MapData) == false)
//                     ftpconn.CreateDirectory(athDesc.FK_MapData);
//
//                 //设置当前目录，为操作的目录。
//                 ftpconn.SetCurrentDirectory(athDesc.FK_MapData);
//
//                 //把文件放上去.
//                 ftpconn.PutFile(temp, guid + "." + dbUpload.FileExts);
//                 ftpconn.Close();
//
//                 //设置路径.
//                 dbUpload.FileFullName = ny + "//" + athDesc.FK_MapData + "//" + guid + "." + dbUpload.FileExts;
//                 dbUpload.Insert();
//             }
//
//             //执行附件上传后事件，added by liuxc,2017-7-15
//             msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + temp);
//             if (!DataType.IsNullOrEmpty(msg))
//                 BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
//         }
//         ///#endregion 保存到数据库.
//		 return "";
//	 }

}