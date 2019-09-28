package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class CCMobile_WorkOpt_OneWork extends WebContralBase
{
	/** 
	 构造函数
	*/
	public CCMobile_WorkOpt_OneWork()
	{
	}


		///#region xxx 界面 .
	public final String TimeBase_Init() throws Exception
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		return en.TimeBase_Init();
	}
	/** 
	 执行撤销操作.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TimeBase_UnSend() throws Exception
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		return en.OP_UnSend();
	}
	public final String TimeBase_OpenFrm() throws Exception
	{
		WF en = new WF();
		return en.Runing_OpenFrm();
	}


}