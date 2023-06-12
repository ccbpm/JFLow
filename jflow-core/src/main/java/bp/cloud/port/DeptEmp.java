package bp.cloud.port;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.difference.*;

/**
 * 部门编号人员
 */
public class DeptEmp extends EntityMyPK {

    ///#region 属性

    /**
     * 是否是主部门
     */
    public final boolean getIsMainDept() {
        return this.GetValBooleanByKey(DeptEmpAttr.IsMainDept);
    }

    public final void setIsMainDept(boolean value) {
        this.SetValByKey(DeptEmpAttr.IsMainDept, value);
    }

    public final String getFK_Emp() {
        return this.GetValStringByKey(DeptEmpAttr.FK_Emp);
    }

    public final void setFK_Emp(String value) {
        this.SetValByKey(DeptEmpAttr.FK_Emp, value);
    }

    /**
     * 人员编号
     */
    public final String getEmpNo() {
        return this.GetValStringByKey(DeptEmpAttr.EmpNo);
    }

    public final void setEmpNo(String value) {
        this.SetValByKey(DeptEmpAttr.EmpNo, value);
    }

    public final String getFK_Dept() {
        return this.GetValStringByKey(DeptEmpAttr.FK_Dept);
    }

    public final void setFK_Dept(String value) {
        this.SetValByKey(DeptEmpAttr.FK_Dept, value);
    }

    /**
     * 组织结构编码
     */
    public final String getOrgNo() {
        return this.GetValStrByKey(EmpAttr.OrgNo);
    }

    public final void setOrgNo(String value) {
        this.SetValByKey(EmpAttr.OrgNo, value);
    }


    ///#endregion


    ///#region 构造方法

    /**
     * 部门编号人员
     */
    public DeptEmp() {
    }

    /**
     * 部门编号人员
     */
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null) {
            return this.get_enMap();
        }
        Map map = new Map("Port_DeptEmp", "部门人员");

        map.AddMyPK();
        map.AddTBString(DeptEmpAttr.FK_Dept, null, "编号", true, false, 0, 50, 20);
        map.AddTBString(DeptEmpAttr.FK_Emp, null, "人员", true, false, 0, 50, 20);
        map.AddTBString(EmpAttr.OrgNo, null, "OrgNo", false, false, 0, 50, 36);


        // SAAS 模式下用到.
        if (SystemConfig.getCCBPMRunModel() == bp.sys.CCBPMRunModel.SAAS) {
            map.AddTBString(DeptEmpAttr.EmpNo, null, "EmpNo", false, false, 0, 50, 36);
            map.AddTBInt(DeptEmpAttr.IsMainDept, 1, "是否是主部门", false, false);
        }

        this.set_enMap(map);
        return this.get_enMap();
    }

    ///#endregion


    @Override
    protected boolean beforeDelete() throws Exception {

        if (DataType.IsNullOrEmpty(this.getEmpNo()) == false) {
            bp.cloud.port.Emp emp = new Emp();
            emp.setNo(this.getEmpNo());
            if (emp.RetrieveFromDBSources() == 1) {
                this.setFK_Emp(emp.getUserID());
                this.setOrgNo(emp.getOrgNo());
            }
        }

        this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp());

        //删除角色信息.
        DeptEmpStations des = new DeptEmpStations();
        des.Delete(DeptEmpStationAttr.FK_Emp, this.getFK_Emp(), DeptEmpStationAttr.FK_Dept, this.getFK_Dept());

        return super.beforeDelete();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        //if (bp.difference.SystemConfig.getCCBPMRunModel()!= bp.sys.CCBPMRunModel.Single && this.EmpNo.contains(bp.web.WebUser.getOrgNo() + "_") == true)
        //{
        //    bp.cloud.Emp emp = new bp.cloud.Emp(this.EmpNo);
        //    this.setMyPK(this.FK_Dept + "_" + emp.UserID);
        //    this.FK_Emp = emp.UserID;
        //    this.IsMainDept = false;
        //}
        //if (DataType.IsNullOrEmpty(this.EmpNo) == true)
        //{
        //    this.EmpNo = bp.web.WebUser.getOrgNo() + "_" + this.FK_Emp;
        //}

        ////组织编号.
        //this.OrgNo = bp.web.WebUser.getOrgNo();

        //当前人员所在的部门.
        //this.OrgNo = bp.web.WebUser.FK_Dept;
        return super.beforeInsert();
    }
}