package BP.WF.HttpHandler;

import BP.DA.DBAccess;
import BP.Difference.Handler.WebContralBase;
import BP.WF.CCBPMRunModel;
import BP.WF.Glo;

public class WF_Admin_AttrNode_AccepterRole extends WebContralBase {

    /// <summary>
    /// 构造函数
    /// </summary>
    public WF_Admin_AttrNode_AccepterRole()
    {
    }

        //#region  界面 .
    /// <summary>
    ///  清空缓存
    /// </summary>
    /// <returns></returns>
    public String AccepterRole_ClearStartFlowsCash() throws Exception
    {
        if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
            DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows=''");
        else
            DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows='' WHERE OrgNo='" + BP.Web.WebUser.getOrgNo() + "'");
        return "执行成功 ";
    }
    /// <summary>
    /// 清楚所有组织的缓存,用于多组织.
    /// </summary>
    /// <returns></returns>
    public String AccepterRole_ClearAllOrgStartFlowsCash()
    {
        DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows=''");
        return "执行成功 ";
    }
        //#endregion 界面方法.


}
