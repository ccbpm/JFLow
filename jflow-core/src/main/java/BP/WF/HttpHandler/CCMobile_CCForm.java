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

	/**
	 * 构造函数
	 */
	public CCMobile_CCForm()
	{
	
	}
	
	public final String HandlerMapExt() throws Exception
	{
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	public String AttachmentUpload_Down() throws Exception
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.AttachmentUpload_Down();
    }
	
	/// <summary>
    /// 表单初始化.
    /// </summary>
    /// <returns></returns>
    public String Frm_Init() throws Exception
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.Frm_Init();
    }

    public String Dtl_Init() throws Exception
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
     

}
