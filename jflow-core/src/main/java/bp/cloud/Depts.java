package bp.cloud;

import bp.da.*;
import bp.en.*;
import bp.web.*;

/**
 * 部门集合
 */
public class Depts extends EntitiesTree {
    /**
     * 得到一个新实体
     */
    @Override
    public Entity getGetNewEntity() {
        return new Dept();
    }

    /**
     * 部门集合
     *
     * @param parentNo 父部门No
     */
    public Depts(String parentNo) throws Exception {
        this.Retrieve(DeptAttr.ParentNo, parentNo);
    }

    /**
     * 部门集合
     */
    public Depts() {
    }

    @Override
    public int RetrieveAll() throws Exception {
        return this.Retrieve(EmpAttr.OrgNo, bp.web.WebUser.getOrgNo());
    }


    ///#region 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。

    /**
     * 转化成 java list,C#不能调用.
     *
     * @return List
     */
    public final java.util.List<Dept> ToJavaList() {
        return (java.util.List<Dept>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<Dept> Tolist() {
        java.util.ArrayList<Dept> list = new java.util.ArrayList<Dept>();
        for (int i = 0; i < this.size(); i++) {
            list.add((Dept) this.get(i));
        }
        return list;
    }

    ///#endregion 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
}