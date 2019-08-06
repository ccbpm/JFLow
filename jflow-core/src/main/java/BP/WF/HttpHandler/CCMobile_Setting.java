package BP.WF.HttpHandler;

import BP.Difference.Handler.WebContralBase;

/** 
 页面功能实体
 
*/
public class CCMobile_Setting extends WebContralBase
{
	/**
	 * 构造函数
	 */
	public CCMobile_Setting()
	{
	
	}
	
	public final String ChangeDept_Init() throws Exception
	{
		WF_Setting ccform = new WF_Setting();
		return ccform.ChangeDept_Init();
	}


}
