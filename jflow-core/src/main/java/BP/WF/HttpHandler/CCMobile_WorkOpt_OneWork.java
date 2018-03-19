package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class CCMobile_WorkOpt_OneWork extends WebContralBase
{

		///#region xxx 界面 .
	public final String TimeBase_Init()
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork(this.context);
		return en.TimeBase_Init();
	}
	/** 
	 执行撤销操作.
	 
	 @return 
	*/
	public final String TimeBase_UnSend()
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork(this.context);
		return en.OP_UnSend();
	}
	public final String TimeBase_OpenFrm()
	{
		WF en = new WF(this.context);
		return en.Runing_OpenFrm();
	}

		///#endregion xxx 界面方法.

}