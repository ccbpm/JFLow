package bp.wf.httphandler;

import bp.*;
import bp.wf.*;

/** 
 页面功能实体
*/
public class WF_WorkOpt_StartGuide extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_WorkOpt_StartGuide() throws Exception {

	}

	//执行启动,并设置父子关系.
	public final String ParentFlowModel_StartIt() throws Exception {
		long pworkid = this.GetRequestValInt64("PWorkID");
		GenerWorkFlow gwfP = new GenerWorkFlow(pworkid);

		Flow fl = new Flow(this.getFK_Flow());
		Work wk = fl.NewWork();

		Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), pworkid, null, gwfP.getFK_Node(), false);
		return String.valueOf(wk.getOID());
	}


}