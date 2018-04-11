package BP.WF.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.AthSaveWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.GEEntity;
import BP.Tools.FileAccess;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;

/** 
 页面功能实体
 
*/
public class CCMobile_CCForm extends WebContralBase
{
	public final String HandlerMapExt() throws UnsupportedEncodingException
	{
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	public String AttachmentUpload_Down()
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.AttachmentUpload_Down();
    }
	
	/// <summary>
    /// 表单初始化.
    /// </summary>
    /// <returns></returns>
    public String Frm_Init()
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.Frm_Init();
    }

    public String Dtl_Init()
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.Dtl_Init();
    }
    
    public void multipleCommentImageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = false)List<MultipartFile> files) {
        response.setContentType("text/html;charset=utf-8");
      
        for (MultipartFile file:files) {
            String fileName = file.getOriginalFilename(); //获取文件名
        }
        

        
    }
    
  
    //多附件上传
    @RequestMapping(value = { "upload" })
    public String MoreAttach() {

		String attachPk = this.getRequest().getParameter("attachPk");
		String workid = this.getRequest().getParameter("workid");
		String fk_node = this.getRequest().getParameter("fk_node");
		String ensNamestring = this.getRequest().getParameter("ensNamestring");
		String fk_flow = this.getRequest().getParameter("fk_flow");
		String pkVal = this.getRequest().getParameter("pkVal");
		Enumeration enu=this.getRequest().getParameterNames();  
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement();  
		System.out.println(paraName+": "+this.getRequest().getParameter(paraName));  
		}
		
		//1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8"); 
		//3、判断提交上来的数据是否是上传表单的数据
		if(!ServletFileUpload.isMultipartContent(this.getRequest())){
		//按照传统方式获取数据
			
		}
		//4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
		try {
			List<FileItem> list = upload.parseRequest(this.getRequest());
			for(FileItem item : list){
				//如果fileitem中封装的是普通输入项的数据
				if(item.isFormField()){
				String name = item.getFieldName();
				//解决普通输入项的数据的中文乱码问题
				String value = item.getString("UTF-8");
				//value = new String(value.getBytes("iso8859-1"),"UTF-8");
				System.out.println(name + "=" + value);
				}else{//如果fileitem中封装的是上传文件
				//得到上传的文件名称，
				String filename = item.getName();
				System.out.println(filename);
				if(filename==null || filename.trim().equals("")){
				continue;
				}
				}
			}
		} catch (FileUploadException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
//		// ServletFileUpload upload = new ServletFileUpload();
//		             
//         // 解析HTTP请求消息头
//        try {
//			List<FileItem> fileItems = upload.parseRequest(this.getRequest());
//			System.out.println("fileItems="+fileItems.size());
//		} catch (FileUploadException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) this.getRequest();
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// 多附件描述.
		FrmAttachment athDesc = new FrmAttachment(attachPk);

		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			String fileName = mf.getOriginalFilename();
			String savePath = athDesc.getSaveTo();
			if (savePath.contains("@") == true || savePath.contains("*") == true) {
				// 如果有变量
				savePath = savePath.replace("*", "@");
				GEEntity en = new GEEntity(athDesc.getFK_MapData());
				en.setPKVal(pkVal);
				en.Retrieve();
				savePath = BP.WF.Glo.DealExp(savePath, en, null);

				if (savePath.contains("@") && fk_node != null) {
					// 如果包含 @
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

			// 替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");
			try {
				// savePath = context.Server.MapPath("~/" + savePath);
				if (savePath.indexOf(":") == -1) {
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
						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath)
						+ "===" + savePath + "@技术问题:" + ex.getMessage());
			}

			// String exts =
			// System.IO.Path.GetExtension(context.Request.Files[i].FileName).toLowerCase().replace(".",
			// "");
			String exts = FileAccess.getExtensionName(fileName);

			String guid = BP.DA.DBAccess.GenerGUID();
			// String fileName = context.Request.Files[i].FileName.substring(0,
			// context.Request.Files[i].FileName.lastIndexOf('.'));
			String ext = mf.getContentType();
			String realSaveTo = savePath + "/" + guid + "." + fileName + ext;
			String saveTo = realSaveTo;
			// context.Request.Files[i].SaveAs(realSaveTo);

			File info = new File(realSaveTo);

			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
			dbUpload.setNodeID(fk_node.toString());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFileExts(exts);

			/// #region 处理文件路径，如果是保存到数据库，就存储pk.
			if (athDesc.getAthSaveWay() == AthSaveWay.IISServer) {
				// 文件方式保存
				dbUpload.setFileFullName(saveTo);
			}

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				// 保存到数据库
				dbUpload.setFileFullName(dbUpload.getMyPK());
			}
			/// #endregion 处理文件路径，如果是保存到数据库，就存储pk.

			dbUpload.setFileName(fileName);
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(BP.Web.WebUser.getName());
			dbUpload.setRefPKVal(pkVal);

			dbUpload.setUploadGUID(guid);
			dbUpload.Insert();

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				// 执行文件保存.
				try {
					BP.DA.DBAccess.SaveFileToDB(saveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK",
							dbUpload.getMyPK(), "FDB");
				} catch (Exception e) {
					e.printStackTrace();
					return "false";
				}
			}
		}
		return "true";
	}
    
 

}
