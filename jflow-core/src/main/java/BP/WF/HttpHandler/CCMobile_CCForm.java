package BP.WF.HttpHandler;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import BP.WF.HttpHandler.Base.WebContralBase;

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
