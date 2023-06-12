package bp.cloud.port;

import bp.da.*;
import bp.en.*;

/**
 * 部门角色人员对应
 */
public class DeptEmpStations extends EntitiesMyPK {

    ///#region 构造

    /**
     * 工作部门角色人员对应
     */
    public DeptEmpStations() {
    }

    ///#endregion


    ///#region 方法

    /**
     * 得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new DeptEmpStation();
    }

    ///#endregion

    @Override
    public int RetrieveAll() throws Exception {
        return this.Retrieve(EmpAttr.OrgNo, bp.web.WebUser.getOrgNo());
    }


    ///#region 为了适应自动翻译成java的需要,把实体转换成List.

    /**
     * 转化成 java list,C#不能调用.
     *
     * @return List
     */
    public final java.util.List<DeptEmpStation> ToJavaList() {
        return (java.util.List<DeptEmpStation>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<DeptEmpStation> Tolist() {
        java.util.ArrayList<DeptEmpStation> list = new java.util.ArrayList<DeptEmpStation>();
        for (int i = 0; i < this.size(); i++) {
            list.add((DeptEmpStation) this.get(i));
        }
        return list;
    }

    ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

    ///#region 删除方法
    public final void DelteNotInEmp() {
        String sql = "DELETE FROM Port_DeptEmpStation WHERE FK_Emp NOT IN (SELECT No FROM Port_Emp)";
        DBAccess.RunSQL(sql);
    }

    ///#endregion
}