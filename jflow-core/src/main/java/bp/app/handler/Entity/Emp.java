package bp.app.handler.Entity;

import bp.en.Entities;
import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.UAC;
import bp.port.Depts;
import bp.port.Emps;
import bp.tools.Encodes;

public class Emp extends EntityNoName {

    public final String getOpenID() throws Exception
    {
        return this.GetValStrByKey(EmpAttr.OpenID);
    }
    public final void setOpenID(String value)  throws Exception
    {
        this.SetValByKey(EmpAttr.OpenID, value);
    }
    public final String getPass() throws Exception
    {
        return this.GetValStrByKey(EmpAttr.Pass);
    }
    public final void setPass(String value)  throws Exception
    {
        this.SetValByKey(EmpAttr.Pass, value);
    }
    public final String getFK_Dept() throws Exception
    {
        return this.GetValStrByKey(EmpAttr.FK_Dept);
    }
    public final void setFK_Dept(String value)  throws Exception
    {
        this.SetValByKey(EmpAttr.FK_Dept, value);
    }



    ///#endregion

    /**
     工作人员
     */
    public Emp()  {
    }

    /**
     UI界面上的访问控制
     */
    @Override
    public UAC getHisUAC()  {
        UAC uac = new UAC();
        uac.OpenForAppAdmin();
        return uac;
    }
    /**
     重写基类方法
     */
    @Override
    public bp.en.Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("Port_Emp", "用户");


        ///#region 字段
        /*关于字段属性的增加 */
        map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 50, 100);
        map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 100, 100);
        map.AddTBString(EmpAttr.OpenID, null, "OpenID", true, false, 0, 100, 100);

        map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new Depts(), true);
        map.AddTBString(EmpAttr.Pass, null, "密码", false, false, 0, 11, 30);

        ///#endregion 字段


        this.set_enMap(map);
        return this.get_enMap();
    }

    public Entities getNewEntities()  {
        return new Emps();
    }
    @Override
    protected boolean beforeInsert() throws Exception {
        if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true)
        {
            this.setPass(Encodes.encodeBase64(this.getPass()));
        }
        return super.beforeInsert();
    }
}
