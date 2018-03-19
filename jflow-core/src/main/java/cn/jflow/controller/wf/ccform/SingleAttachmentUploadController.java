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
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.GEEntity;
import BP.Sys.PubClass;
import BP.Tools.FileAccess;
import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/CCForm")
@Scope("request")
public class SingleAttachmentUploadController extends BaseController {

	@RequestMapping(value = "/SingleUpload", method = RequestMethod.POST)
	public void execute(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		String buttonId = request.getParameter("BtnID");// this.getBtnID();
		String[] ids = buttonId.split("[_]", -1);

		String doType = ids[1];

		String athPK = buttonId.replace("Btn_" + doType + "_", "");
		athPK = athPK.substring(0, athPK.lastIndexOf('_'));

		FrmAttachment frmAth = new FrmAttachment();
		frmAth.setMyPK(athPK);
		frmAth.RetrieveFromDBSources();
		GEEntity en = new GEEntity(frmAth.getFK_MapData());
		en.SetValByKey("OID", request.getParameter("OID"));
		en.RetrieveFromDBSources();
		en.ResetDefaultVal();
		String pkVal = request.getParameter("PKVal") == null ? "" : String .valueOf(request.getParameter("PKVal"));
		String athDBPK = athPK + "_" + pkVal;

		if (doType.equals("Delete")) {
			FrmAttachmentDB db = new FrmAttachmentDB();
			db.setMyPK(athDBPK);
			if (db.RetrieveFromDBSources() == 0) {
				db.Retrieve(FrmAttachmentDBAttr.FK_MapData, en.getClassID(),
						FrmAttachmentDBAttr.RefPKVal, en.getPKVal().toString(),
						FrmAttachmentDBAttr.FK_FrmAttachment,
						frmAth.getFK_MapData() + "_" + frmAth.getNoOfObj());
			}
			int id = db.Delete();
			if (id == 0) {
				throw new RuntimeException("@没有删除成功.");
			} else {
//				System.out.println("---" + db.getFileFullName());
				File file = new File(db.getFileFullName());
				if (file.isFile() && file.exists()) {
					file.delete();
//					System.out.println("删除单个文件" + db.getFileName() + "成功！");
//				} else {
//					System.out.println("删除单个文件" + db.getFileName() + "失败！");
				}
			}
			String url = Glo.getCCFlowAppPath()
					+ "WF/CCForm/SingleAttachmentUpload.jsp?"
					+ request.getQueryString();
			this.windowReload(response, url);

		} else if (doType.equals("Upload")) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest
					.getFile("file_single");
			if (item.getOriginalFilename() != null
					&& !item.getOriginalFilename().equals("")) {// 判断是否选择了文件
				long upFileSize = item.getSize(); // 上传文件的大小

				// 检查格式是否符合要求.
				if (frmAth.getExts().equals("")
						|| frmAth.getExts().equals("*.*")) {
					// 任何格式都可以上传.
				} else {
					String fileExt = item.getOriginalFilename().substring(
							item.getOriginalFilename().lastIndexOf('.') + 1);
					fileExt = fileExt.toLowerCase().replace(".", "");
					if (frmAth.getExts().toLowerCase().contains(fileExt) == false) {
						String url = Glo.getCCFlowAppPath()
								+ "WF/CCForm/SingleAttachmentUpload.jsp?"
								+ request.getQueryString();
						this.printAlertReload(response, "您上传的文件格式不符合要求,要求格式为:"
								+ frmAth.getExts(), url);
						return;
					}
				}

				// 处理保存路径.
				String saveTo = frmAth.getSaveTo();

				if (saveTo.contains("*") || saveTo.contains("@")) {
					// 如果路径里有变量.
					saveTo = saveTo.replace("*", "@");
					saveTo = BP.WF.Glo.DealExp(saveTo, en, null);
				}

				saveTo = saveTo.replace("\\\\", "\\");
				try {
					File file = new File(saveTo);
					if (!file.exists()) {
						saveTo = ContextHolderUtils.getRequest().getSession()
								.getServletContext()
								.getRealPath("/DataUser/UploadFile/");
						file = new File(saveTo);
					}

				} catch (RuntimeException e) {
				}

				try {
					File fileInfo = new File(saveTo);

					if (!fileInfo.exists()) {
						fileInfo.createNewFile();
					}
				} catch (RuntimeException ex) {
					throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
							+ ContextHolderUtils.getRequest().getSession()
									.getServletContext()
									.getRealPath("/" + saveTo) + "===" + saveTo
							+ "@技术问题:" + ex.getMessage());
				}

				saveTo = saveTo
						+ "\\"
						+ athDBPK
						+ "."
						+ item.getOriginalFilename()
								.substring(
										item.getOriginalFilename().lastIndexOf(
												'.') + 1);

				try {
					// 构造临时对象
					File file = new File(saveTo); // 获取根目录对应的真实物理路径
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
					String url = Glo.getCCFlowAppPath()
							+ "WF/CCForm/SingleAttachmentUpload.jsp?"
							+ request.getQueryString();

					this.printAlertReload(
							response,
							"@文件存储失败,有可能是路径的表达式出问题,导致是非法的路径名称:"
									+ ex.getMessage(), url);
					return;
				}

				File info = new File(saveTo);
				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
				dbUpload.setMyPK(athDBPK);
				dbUpload.setFK_FrmAttachment(athPK);
				dbUpload.setRefPKVal(pkVal);
				if (this.getEnName() == null) {
					dbUpload.setFK_MapData(en.toString());
				} else {
					dbUpload.setFK_MapData(this.getEnName());
				}

				String ext = FileAccess.getExtensionName(item
						.getOriginalFilename());
				dbUpload.setFileExts(ext);
				dbUpload.setFileFullName(saveTo);
				dbUpload.setFileName(item.getOriginalFilename());
				dbUpload.setFileSize(item.getSize());
				dbUpload.setRDT(DataType.getCurrentDataTimess());
				dbUpload.setRec(WebUser.getNo());
				dbUpload.setRecName(WebUser.getName());
				dbUpload.setNodeID(String.valueOf(this.getFK_Node()));
				dbUpload.Save();
				String url = Glo.getCCFlowAppPath()
						+ "WF/CCForm/SingleAttachmentUpload.jsp?"
						+ request.getQueryString();
				this.windowReload(response, url);
			} else {
				String url = Glo.getCCFlowAppPath()
						+ "WF/CCForm/SingleAttachmentUpload.jsp?"
						+ request.getQueryString();
				this.printAlertReload(response, "没有选择上传文件！", url);
				return;
			}

		}

		else if (doType.equals("Download")) {
			FrmAttachmentDB dbDown = new FrmAttachmentDB();
			dbDown.setMyPK(athDBPK);
			if (dbDown.RetrieveFromDBSources() == 0) { dbDown.Retrieve(FrmAttachmentDBAttr.FK_MapData,
						en.getClassID(), FrmAttachmentDBAttr.RefPKVal, en .getPKVal().toString(),
						FrmAttachmentDBAttr.FK_FrmAttachment,
						frmAth.getFK_MapData() + "_" + frmAth.getNoOfObj());
			}
			// String downPath = GetRealPath(dbDown.getFileFullName());
			File file = new File(dbDown.getFileFullName());
			if (file.exists()) {
				PubClass.DownloadFile(dbDown.getFileFullName(), dbDown.getFileName());
			} else {
				dbDown.Delete();
				String url = Glo.getCCFlowAppPath()
						+ "WF/CCForm/SingleAttachmentUpload.jsp?"
						+ request.getQueryString();
				this.printAlertReload(response, "【" + dbDown.getFileName()
						+ "】，该文件已不存在！", url);

			}

		} else if (doType.equals("Open")) {
			String url = BP.WF.Glo.getCCFlowAppPath()
					+ "WF/WebOffice/AttachOffice.jsp?DoType=EditOffice&DelPKVal="
					+ athDBPK + "&FK_FrmAttachment=" + frmAth.getMyPK()
					+ "&PKVal=" + pkVal + "&FK_Node="
					+ en.GetValStringByKey("FK_Node") + "&FK_MapData="
					+ frmAth.getFK_MapData() + "&NoOfObj="
					+ frmAth.getNoOfObj();
			PubClass.WinOpen(response, url, "WebOffice编辑", 850, 600);
		} else {
		}
		// return null;
	}
	// private String GetRealPath(String fileFullName) throws Exception
	// {
	// boolean isFile = false;
	// String downpath = "";
	// try
	// {
	// //如果相对路径获取不到可能存储的是绝对路径
	// File downInfo = new File(ContextHolderUtils.getRequest().getSession()
	// .getServletContext().getRealPath(fileFullName));
	// if(downInfo.isFile())
	// {
	// isFile = true;
	// downpath = ContextHolderUtils.getRequest().getSession()
	// .getServletContext().getRealPath(fileFullName);
	// }else
	// {
	// File downInfo2 = new File(fileFullName);
	// if(downInfo2.isFile())
	// {
	// isFile = true;
	// downpath = fileFullName;
	// }
	// }
	// }
	// catch (Exception e)
	// {
	// isFile = false;
	// }
	// if (!isFile)
	// {
	// throw new Exception("没有找到下载的文件路径！");
	// }
	//
	// return downpath;
	// }

}
