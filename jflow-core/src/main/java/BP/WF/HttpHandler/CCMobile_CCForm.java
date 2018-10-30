package BP.WF.HttpHandler;


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
    

}
