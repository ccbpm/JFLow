package bp.wf.httphandler;
import bp.difference.handler.WebContralBase;
import bp.web.*;

/** 
 页面功能实体
*/
public class CCMobile_WorkOpt_OneWork extends WebContralBase
{
	/** 
	 构造函数
	 * @throws Exception 
	*/
	public CCMobile_WorkOpt_OneWork() throws Exception
	{
		WebUser.setSheBei("Mobile");

	}


		///xxx 界面 .
	public final String TimeBase_Init() throws Exception
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork();
		return en.TimeBase_Init();
	}
	/** 
	 执行撤销操作.
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String TimeBase_UnSend() throws NumberFormatException, Exception
	{
		//获取撤销到的节点
		int unSendToNode = this.GetRequestValInt("FK_Node");
		return bp.wf.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(), unSendToNode, this.getFID());
		
	}
	public final String TimeBase_OpenFrm() throws Exception
	{
		WF en = new WF();
		return en.Runing_OpenFrm();
	}

		/// xxx 界面方法.

}