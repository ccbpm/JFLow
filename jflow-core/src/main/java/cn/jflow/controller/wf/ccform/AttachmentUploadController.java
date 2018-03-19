package cn.jflow.controller.wf.ccform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import BP.DA.DataType;
import BP.Sys.AthUploadWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.GEEntity;
import BP.Tools.FileAccess;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;

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

	public String getPKVal() {
		return ContextHolderUtils.getRequest().getParameter("PKVal");
	}
	
	@RequestMapping(value = "/AttachmentUploadS", method = RequestMethod.POST)
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
				printAlertReload(response, error, Glo.getCCFlowAppPath()+"WF/CCForm/AttachmentUpload.jsp?"+request.getQueryString());
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
					printAlertReload(response, error, Glo.getCCFlowAppPath()+"WF/CCForm/AttachmentUpload.jsp?"+request.getQueryString());
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


			String realSaveTo = savePath + "/" + guid + "." + fileName + "." + ext;

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
				printAlertReload(response, error, Glo.getCCFlowAppPath()+"WF/CCForm/AttachmentUpload.jsp?"+request.getQueryString());
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
			printAlertReload(response, error, Glo.getCCFlowAppPath()+"WF/CCForm/AttachmentUpload.jsp?"+request.getQueryString());
			return;
		}
		try {
			BaseModel.sendRedirect(Glo.getCCFlowAppPath()+"WF/CCForm/AttachmentUpload.jsp?"+request.getQueryString());
		} catch (Exception e) {
			e.printStackTrace();
			printAlertReload(response, e.getMessage(), Glo.getCCFlowAppPath()+"WF/CCForm/AttachmentUpload.jsp?"+request.getQueryString());
			return;
		}

	}
	
	

	

}