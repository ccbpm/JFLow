package bp.cloud.port;

import bp.da.*;
import bp.en.*;
import bp.sys.*;

/**
 * 组织s
 * // </summary>
 */
public class Orgs extends EntitiesNoName {

    ///#region 构造方法

    /**
     * 得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new Org();
    }

    /**
     * 组织s
     */
    public Orgs() {
    }

    ///#endregion 构造方法


    ///#region 为了适应自动翻译成java的需要,把实体转换成List.

    /**
     * 转化成 java list,C#不能调用.
     *
     * @return List
     */
    public final java.util.List<Org> ToJavaList() {
        return (java.util.List<Org>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<Org> Tolist() {
        java.util.ArrayList<Org> list = new java.util.ArrayList<Org>();
        for (int i = 0; i < this.size(); i++) {
            list.add((Org) this.get(i));
        }
        return list;
    }

    ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}