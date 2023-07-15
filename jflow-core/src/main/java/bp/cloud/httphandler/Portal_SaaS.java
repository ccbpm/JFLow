package bp.cloud.httphandler;

import bp.cloud.Org;
import bp.da.*;
import bp.difference.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;

import java.util.Hashtable;

/**
 * 页面功能实体
 */
public class Portal_SaaS extends WebContralBase {
    /**
     * 构造函数
     */
    public Portal_SaaS() {
    }

    public final String CheckEncryptEnable()
    {
        if (SystemConfig.getIsEnablePasswordEncryption() == true)
            return "1";
        return "0";
    }

    /**
     * 获取组织
     * @return
     */
    public final String SelectOneOrg_Init() throws Exception {
        bp.cloud.Orgs orgs = new bp.cloud.Orgs();
        orgs.Retrieve(bp.cloud.OrgAttr.OrgSta, 0);
        DataTable dt = orgs.ToDataTableField("Orgs");
        return bp.tools.Json.ToJson(dt);
    }

    public final String GetOrgByNo() throws Exception {
        String no = this.GetRequestVal("OrgNo");
        bp.cloud.Org org = new Org();
        org.setNo(no);
        if(org.RetrieveFromDBSources() == 0) {
            return "err@组织不存在.";
        }
        return org.ToJson();
    }

    public final String Login_Submit() throws Exception {
        try {
            String orgNo = this.getOrgNo();
            String userNo = this.GetRequestVal("TB_No");
            String pass = this.GetRequestVal("TB_PW");
            if (pass == null) {
                pass = this.GetRequestVal("TB_Pass");
            }
            pass = pass.trim();
            bp.port.Emp emp = new bp.port.Emp();
            emp.setNo(orgNo + "_" + userNo);

            if (emp.RetrieveFromDBSources() == 0) {
                return "err@用户名[" + userNo + "]不存在.";
            }

            if (!emp.CheckPass(pass)) {
                return "err@用户名或密码错误.";
            }

            bp.wf.Dev2Interface.Port_Login(userNo,this.getOrgNo());
            String token = bp.wf.Dev2Interface.Port_GenerToken();
            /*Hashtable ht = new Hashtable();
            ht.Add("No", emp.No);
            ht.Add("Name", emp.Name);
            ht.Add("Token", token);
            ht.Add("FK_Dept", emp.FK_Dept);
            ht.Add("FK_DeptName", emp.FK_DeptText);
            ht.Add("OrgNo", emp.OrgNo);

            return BP.Tools.Json.ToJson(ht);*/
            return "url@/Portal/Standard/Default.htm?Token=" + token + "&UserNo=" + emp.getUserID() + "&OrgNo=" + emp.getOrgNo();
        } catch (RuntimeException ex) {
            return "err@" + ex.getMessage();
        }
    }


    ///#region 执行父类的重写方法.

    /**
     * 默认执行的方法
     *
     * @return
     */
    @Override
    protected String DoDefaultMethod() throws Exception {

        switch (this.getDoType())
        {
            case "DtlFieldUp": //字段上移
                return "执行成功.";
            default:
                break;
        }

        //找不不到标记就抛出异常.
        throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + ContextHolderUtils.getRequest().getRequestURI());
    }

    ///#endregion 执行父类的重写方法.


    ///#region xxx 界面 .

    ///#endregion xxx 界面方法.

}