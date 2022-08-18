package bp.wf.httphandler;

import bp.da.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;

/** 
 页面功能实体
*/
public class WF_Admin_FoolFormDesigner_Template_FrmAttachmentSingle extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_FoolFormDesigner_Template_FrmAttachmentSingle() throws Exception {

	}


		///#region  界面 .
//	/**
//	 生成模板文件
//
//	 @return
//	*/
//	public final String UploadAthTemplateWPS_Init() throws Exception {
//		bp.sys.frmui.FrmAttachmentSingle ath = new sys.frmui.FrmAttachmentSingle(this.getMyPK());
//		String file = SystemConfig.getPathOfTemp() + "/" + this.getMyPK() + ".wps";
//		DBAccess.GetFileFromDB(file, ath.getEnMap().getPhysicsTable(), "MyPK", this.getMyPK(), "TemplateFile");
//		return file;
//	}
//	/**
//	 保存模板文件
//
//	 @return
//	*/
//	public final String UploadAthTemplateWPS_Save() throws Exception {
//		if (HttpContextHelper.RequestFilesCount == 0)
//		{
//			return "err@请上传文件模板";
//		}
//		HttpPostedFile file = HttpContextHelper.RequestFiles(0);
//		//保存文件到临时目录
//		String path = SystemConfig.getPathOfTemp() + file.FileName;
//		HttpContextHelper.UploadFile(file, path);
//		bp.sys.frmui.FrmAttachmentSingle ath = new sys.frmui.FrmAttachmentSingle(this.getMyPK());
//		//存储到模板库里。
//		DBAccess.SaveFileToDB(path, ath.getEnMap().getPhysicsTable(), "MyPK", this.getMyPK(), "TemplateFile");
//		return "保存成功";
//	}

		///#endregion 界面.

}