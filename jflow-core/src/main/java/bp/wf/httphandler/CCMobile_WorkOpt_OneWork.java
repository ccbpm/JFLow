package bp.wf.httphandler;

import bp.*;
import bp.difference.handler.WebContralBase;
import bp.wf.*;

/** 
 页面功能实体
*/
public class CCMobile_WorkOpt_OneWork extends WebContralBase
{
	/** 
	 构造函数
	*/
	public CCMobile_WorkOpt_OneWork() throws Exception {
		bp.web.WebUser.setSheBei( "Mobile");

	}


		///#region xxx 界面 .
	public final String TimeBase_Init() throws Exception {
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		return en.TimeBase_Init();
	}
	/** 
	 执行撤销操作.
	 
	 @return 
	*/
	public final String TimeBase_UnSend() throws Exception {
		//获取撤销到的节点
		int unSendToNode = this.GetRequestValInt("FK_Node");
		return Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(), unSendToNode, this.getFID());
		//WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		//return en.OP_UnSend();
	}


		///#endregion xxx 界面方法.

}