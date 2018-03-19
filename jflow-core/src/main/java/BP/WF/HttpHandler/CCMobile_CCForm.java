package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;

import BP.WF.HttpHandler.Base.WebContralBase;

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

	public final String AttachmentUpload_Down()
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.AttachmentUpload_Down();
	}


}
