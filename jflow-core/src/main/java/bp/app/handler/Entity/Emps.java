package bp.app.handler.Entity;

import bp.en.EntitiesNoName;
import bp.en.Entity;

import java.util.ArrayList;

public class Emps extends EntitiesNoName {
    /**
     得到它的 Entity
     */
    @Override
    public Entity getGetNewEntity() {
        return new Emp();
    }
    /**
     工作人员s
     */
    public Emps()  {
    }

    ///#region 为了适应自动翻译成java的需要,把实体转换成List.
    /**
     转化成 java list,C#不能调用.

     @return List
     */
    public final java.util.List<Emp> ToJavaList() {
        return (java.util.List<Emp>)(Object)this;
    }
    /**
     转化成list

     @return List
     */
    public final ArrayList<Emp> Tolist()  {
        ArrayList<Emp> list = new ArrayList<Emp>();
        for (int i = 0; i < this.size(); i++)
        {
            list.add((Emp)this.get(i));
        }
        return list;
    }
}
