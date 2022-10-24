package bp.wf.httphandler;

import bp.da.*;
import bp.sys.*;
import bp.*;
import bp.wf.*;
import bp.wf.Glo;

/** 
 页面功能实体
*/
public class WF_Admin_AttrNode_AccepterRole extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_AttrNode_AccepterRole() throws Exception {
	}


		///#region  界面 .
	/** 
	 清空缓存
	 
	 @return 
	*/
	public final String AccepterRole_ClearStartFlowsCash() throws Exception {
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows=''");
		}
		else
		{
			DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows='' WHERE OrgNo='" + bp.web.WebUser.getOrgNo() + "'");
		}
		return "执行成功 ";
	}
	/** 
	 清楚所有组织的缓存,用于多组织.
	 
	 @return 
	*/
	public final String AccepterRole_ClearAllOrgStartFlowsCash() throws Exception {
		DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows=''");
		return "执行成功 ";
	}

		///#endregion 界面方法.

}