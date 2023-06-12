package bp.cloud.port;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.difference.*;

/**
 * 部门编号人员s
 */
public class DeptEmps extends EntitiesMM {

    ///#region 构造

    /**
     * 部门编号s
     */
    public DeptEmps() {
    }

    /**
     * 得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new DeptEmp();
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
    public final java.util.List<DeptEmp> ToJavaList() {
        return (java.util.List<DeptEmp>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<DeptEmp> Tolist() {
        java.util.ArrayList<DeptEmp> list = new java.util.ArrayList<DeptEmp>();
        for (int i = 0; i < this.size(); i++) {
            list.add((DeptEmp) this.get(i));
        }
        return list;
    }

    ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}