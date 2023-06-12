package bp.cloud.sys;

import bp.en.*;

/**
 * 纳税人集合
 */
public class SysEnumMains extends EntitiesNoName {
    /**
     * SysEnumMains
     */
    public SysEnumMains() {
    }

    /**
     * 得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new SysEnumMain();
    }

    /**
     * 查询所有枚举值，根据不同的运行平台.
     *
     * @return
     */
    @Override
    public int RetrieveAll() throws Exception {
        // 返回他组织下的数据.
        return this.Retrieve(SysEnumMainAttr.OrgNo, bp.web.WebUser.getOrgNo());
    }


    ///#region 为了适应自动翻译成java的需要,把实体转换成List.

    /**
     * 转化成 java list,C#不能调用.
     *
     * @return List
     */
    public final java.util.List<SysEnumMain> ToJavaList() {
        return (java.util.List<SysEnumMain>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<SysEnumMain> Tolist() {
        java.util.ArrayList<SysEnumMain> list = new java.util.ArrayList<SysEnumMain>();
        for (int i = 0; i < this.size(); i++) {
            list.add((SysEnumMain) this.get(i));
        }
        return list;
    }

    ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}