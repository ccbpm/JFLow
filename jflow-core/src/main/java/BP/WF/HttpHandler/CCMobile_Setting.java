package BP.WF.HttpHandler;

import BP.WF.HttpHandler.Base.WebContralBase;

/** 
 页面功能实体
 
*/
public class CCMobile_Setting extends WebContralBase
{
	public final String ChangeDept_Init()
	{
		WF_Setting ccform = new WF_Setting();
		return ccform.ChangeDept_Init();
	}


}
