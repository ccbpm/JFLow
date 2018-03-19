package cn.jflow.controller.wf.ccform;

import java.io.File;
import java.net.BindException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import BP.DA.DataType;
import BP.Sys.AthSaveWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.GEEntity;
import BP.Tools.FileAccess;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/CCFormHeader")
@Scope("request")
public class CCFormHeaderController extends BaseController {

	public String getAttachPK() {
		return ContextHolderUtils.getRequest().getParameter("AttachPK");
	}

	public String getPKVal() {
		return ContextHolderUtils.getRequest().getParameter("PKVal");
	}

	@ResponseBody
	@RequestMapping(value = "/AttachmentUpload", method = RequestMethod.POST)
	public final String ProcessRequest(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
		//context.Request.ContentEncoding = System.Text.UTF8Encoding.UTF8;
		String message = "true";
		if (getDoType().equals("DelWorkCheckAttach")) //删除附件
		{
			message = DelWorkCheckAttach(getPKVal());
			//printAlertReload(response, message, Glo.getCCFlowAppPath() + "WF/WorkOpt/WorkCheck.jsp?" + request.getQueryString());
			return message;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest.getFile("file");
		//String fileName = item.getOriginalFilename(); // 获取文件名
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		//如果没有workid，将不不保存附件内容
		if(getWorkID()<=0||"".equals(getWorkID()+"")){
			message = "false";
			return message;
		}
		//判断是否包含附件，包含附件则是上传，否则是功能执行
		if (fileMap.size()>0)////context.Request.Files.size() > 0
		{
			//			switch (doType)
			//ORIGINAL LINE: case "SingelAttach":
			if (getDoType().equals("SingelAttach")) //单附件上传
			{
				message = SingleAttach(getAttachPK(), getWorkID() + "", getFK_Node() + "", getEnsName(),item);
			}
			//ORIGINAL LINE: case "MoreAttach":
			else if (getDoType().equals("MoreAttach")) //多附件上传
			{
				message = MoreAttach(getAttachPK(), getWorkID() + "", getFK_Node() + "", getEnsName(), getFK_Flow(), getPKVal(),fileMap);
			}
		} 
		return message;
		//System.out.println(Glo.getCCFlowAppPath() + "WF/WorkOpt/WorkCheck.jsp?" + request.getQueryString());
		//printAlertReload(response, message, Glo.getCCFlowAppPath() + "WF/WorkOpt/WorkCheck.jsp?" + request.getQueryString());
	}

	//单附件上传方法
	private String SingleAttach(String attachPk, String workid, String fk_node, String ensName,CommonsMultipartFile item) {
		FrmAttachment frmAth = new FrmAttachment();
		frmAth.setMyPK(attachPk);
		frmAth.RetrieveFromDBSources();

		String athDBPK = attachPk + "_" + workid;

		BP.WF.Node currND = new BP.WF.Node(fk_node);
		BP.WF.Work currWK = currND.getHisWork();
		currWK.setOID(Long.parseLong(workid));
		currWK.Retrieve();
		//处理保存路径.
		String saveTo = frmAth.getSaveTo();

		if (saveTo.contains("*") || saveTo.contains("@")) {
			//如果路径里有变量.
			saveTo = saveTo.replace("*", "@");
			saveTo = BP.WF.Glo.DealExp(saveTo, currWK, null);
		}

		// 替换关键的字串.
		saveTo = saveTo.replace("\\\\", "\\");//增加
		try {
			//saveTo = response.Server.MapPath("~/" + saveTo);
			if(saveTo.indexOf(":")==-1){
				saveTo = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(saveTo);
			}
		} catch (java.lang.Exception e) {
			return "false";
			//saveTo = saveTo;
		}

		/*if (System.IO.Directory.Exists(saveTo) == false) {
			System.IO.Directory.CreateDirectory(saveTo);
		}

		saveTo = saveTo + "\\" + athDBPK + "." + response.Request.Files[0].FileName.substring(context.Request.Files[0].FileName.lastIndexOf('.') + 1);
		response.Request.Files[0].SaveAs(saveTo);*/

		try {
			File fileInfo = new File(saveTo);

			if (!fileInfo.exists()) {
				fileInfo.mkdirs();
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
					+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + saveTo) + "==="
					+ saveTo + "@技术问题:" + ex.getMessage());
		}

		FrmAttachmentDB dbUpload = new FrmAttachmentDB();
		dbUpload.setMyPK(athDBPK);
		dbUpload.setFK_FrmAttachment(attachPk);
		dbUpload.setRefPKVal(workid);

		dbUpload.setFK_MapData(ensName);

		String ext = FileAccess.getExtensionName(item.getOriginalFilename());
		
		dbUpload.setFileExts(ext);

		///#region 处理文件路径，如果是保存到数据库，就存储pk.
		if (frmAth.getAthSaveWay() ==AthSaveWay.IISServer) {
			//文件方式保存
			dbUpload.setFileFullName(saveTo);
		}

		if (frmAth.getAthSaveWay() == AthSaveWay.DB) {
			//保存到数据库
			dbUpload.setFileFullName(dbUpload.getMyPK());
		}
		///#endregion 处理文件路径，如果是保存到数据库，就存储pk.

		dbUpload.setFileName(item.getOriginalFilename());
		dbUpload.setFileSize(item.getSize());
		dbUpload.setRec(WebUser.getNo());
		dbUpload.setRecName(WebUser.getName());
		dbUpload.setRDT(DataType.getCurrentDataTimess());

		dbUpload.setNodeID(fk_node);
		dbUpload.Save();

		if (frmAth.getAthSaveWay() == AthSaveWay.DB) {
			//执行文件保存.
			try {
				BP.DA.DBAccess.SaveFileToDB(saveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.getMyPK(), "FDB");
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		}
		return "true";

	}

	//多附件上传方法
	public String MoreAttach(String attachPk, String workid, String fk_node, String ensNamestring, String fk_flow,
			String pkVal, Map<String, MultipartFile> fileMap) {
		// 多附件描述.
		FrmAttachment athDesc = new FrmAttachment(attachPk);

		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			String fileName = mf.getOriginalFilename();
			String savePath = athDesc.getSaveTo();
			if (savePath.contains("@") == true || savePath.contains("*") == true) {
				//如果有变量
				savePath = savePath.replace("*", "@");
				GEEntity en = new GEEntity(athDesc.getFK_MapData());
				en.setPKVal(pkVal);
				en.Retrieve();
				savePath = BP.WF.Glo.DealExp(savePath, en, null);

				if (savePath.contains("@") && fk_node != null) {
					//如果包含 @ 
					BP.WF.Flow flow = new BP.WF.Flow(fk_flow);
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(Long.parseLong(workid));
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true) {
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
				}
			} else {
				savePath = athDesc.getSaveTo() + "/" + pkVal;
			}

			//替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");
			try {
				//savePath = context.Server.MapPath("~/" + savePath);
				if(savePath.indexOf(":")==-1){
					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);
				}
			} catch (RuntimeException e) {
				return "false";
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

			//String exts = System.IO.Path.GetExtension(context.Request.Files[i].FileName).toLowerCase().replace(".", "");
			String exts = FileAccess.getExtensionName(fileName);

			String guid = BP.DA.DBAccess.GenerGUID();
			//String fileName = context.Request.Files[i].FileName.substring(0, context.Request.Files[i].FileName.lastIndexOf('.'));
			String ext = mf.getContentType();
			String realSaveTo = savePath + "/" + guid + "." + fileName + ext;
			String saveTo = realSaveTo;
			//context.Request.Files[i].SaveAs(realSaveTo);

			File info = new File(realSaveTo);

			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
			dbUpload.setNodeID(fk_node.toString());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFileExts(exts);

			///#region 处理文件路径，如果是保存到数据库，就存储pk.
			if (athDesc.getAthSaveWay() == AthSaveWay.IISServer) {
				//文件方式保存
				dbUpload.setFileFullName(saveTo);
			}

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				//保存到数据库
				dbUpload.setFileFullName(dbUpload.getMyPK());
			}
			///#endregion 处理文件路径，如果是保存到数据库，就存储pk.

			dbUpload.setFileName(fileName);
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(BP.Web.WebUser.getName());
			dbUpload.setRefPKVal(pkVal);
			//if (athDesc.IsNote)
			//    dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;

			//if (athDesc.Sort.Contains(","))
			//    dbUpload.Sort = this.Pub1.GetDDLByID("ddl").SelectedItemStringVal;

			dbUpload.setUploadGUID(guid);
			dbUpload.Insert();

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				//执行文件保存.
				try {
					BP.DA.DBAccess.SaveFileToDB(saveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.getMyPK(), "FDB");
				} catch (Exception e) {
					e.printStackTrace();
					return "false";
				}
			}
		}
		return "true";
	}

	/**
	 * 删除附件
	 * @param MyPK
	 * @return
	 */
	private String DelWorkCheckAttach(String MyPK) {
		FrmAttachmentDB athDB = new FrmAttachmentDB();
		athDB.RetrieveByAttr(FrmAttachmentDBAttr.MyPK, MyPK);
		//删除文件
		if (athDB.getFileFullName() != null) {
			/*if (File.Exists(athDB.getFileFullName()) == true) {
				File.Delete(athDB.getFileFullName());
			}*/
			try {
				File fileInfo = new File(athDB.getFileFullName());
				if(fileInfo.exists()){
					fileInfo.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int i = athDB.Delete(FrmAttachmentDBAttr.MyPK, MyPK);
		if (i > 0) {
			return "true";
		}
		return "false";
	}

	public final boolean getIsReusable() {
		return false;
	}

}