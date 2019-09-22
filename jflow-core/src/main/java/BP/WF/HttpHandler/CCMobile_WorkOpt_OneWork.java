package BP.WF.HttpHandler;

import BP.DA.*;
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
public class CCMobile_WorkOpt_OneWork extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public CCMobile_WorkOpt_OneWork()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region xxx 界面 .
	public final String TimeBase_Init()
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		return en.TimeBase_Init();
	}
	/** 
	 执行撤销操作.
	 
	 @return 
	*/
	public final String TimeBase_UnSend()
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		return en.OP_UnSend();
	}
	public final String TimeBase_OpenFrm()
	{
		WF en = new WF();
		return en.Runing_OpenFrm();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion xxx 界面方法.

}