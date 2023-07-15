package bp.cloud;

import bp.da.*;
import bp.en.*;
import bp.sys.*;

/**
 * 操作员s
 * // </summary>
 */
public class Emps extends EntitiesNoName {

    ///#region 构造方法

    /**
     * 得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new Emp();
    }

    /**
     * 操作员s
     */
    public Emps() {
    }


    ///#endregion 构造方法


    ///#region 为了适应自动翻译成java的需要,把实体转换成List.

    /**
     * 转化成 java list,C#不能调用.
     *
     * @return List
     */
    public final java.util.List<Emp> ToJavaList() {
        return (java.util.List<Emp>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<Emp> Tolist() {
        java.util.ArrayList<Emp> list = new java.util.ArrayList<Emp>();
        for (int i = 0; i < this.size(); i++) {
            list.add((Emp) this.get(i));
        }
        return list;
    }

    ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}