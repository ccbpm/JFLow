package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;

/** 
 页面功能实体
*/
public class WF_Admin_AttrNode_AccepterRole extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_AttrNode_AccepterRole()
	{
	}


		/// 界面 .
	/** 
	 清空缓存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String AccepterRole_ClearStartFlowsCash() throws Exception
	{
		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows=''");
		}
		else
		{
			DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows='' WHERE OrgNo='" + WebUser.getOrgNo() + "'");
		}
		return "执行成功 ";
	}
	/** 
	 清楚所有组织的缓存,用于多组织.
	 
	 @return 
	*/
	public final String AccepterRole_ClearAllOrgStartFlowsCash()
	{
		DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows=''");
		return "执行成功 ";
	}

		/// 界面方法.

}