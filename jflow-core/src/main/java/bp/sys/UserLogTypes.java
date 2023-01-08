package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.difference.*;

/**
 * 类型
 */
public class UserLogTypes extends EntitiesNoName {
    ///#region 构造.

    /**
     * 表单目录s
     */
    public UserLogTypes() {
    }

    /**
     * 得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new UserLogType();
    }
    ///#endregion 构造.

    ///#region 为了适应自动翻译成java的需要,把实体转换成List.

    /**
     * 转化成 java list,C#不能调用.
     *
     * @return List
     */
    public final java.util.List<UserLogType> ToJavaList() {
        return (java.util.List<UserLogType>) (Object) this;
    }

    /**
     * 转化成list
     *
     * @return List
     */
    public final java.util.ArrayList<UserLogType> Tolist() {
        java.util.ArrayList<UserLogType> list = new java.util.ArrayList<UserLogType>();
        for (int i = 0; i < this.size(); i++) {
            list.add((UserLogType) this.get(i));
        }
        return list;
    }
    ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}