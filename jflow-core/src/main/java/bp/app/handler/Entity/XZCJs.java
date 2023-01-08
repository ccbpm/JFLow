package bp.app.handler.Entity;

import bp.en.EntitiesMyPK;
import bp.en.Entity;

import java.util.ArrayList;

public class XZCJs extends EntitiesMyPK {
    public XZCJs()  {
    }
    @Override
    public Entity getGetNewEntity()  {
        return new XZCJ();
    }

    /**
     * 获取对象
     * @return
     */
    public final java.util.List<XZCJ> ToJavaList()  {
        return (java.util.List<XZCJ>)(Object)this;
    }
    /**
     转化成list

     @return List
     */
    public final ArrayList<XZCJ> Tolist()  {
        ArrayList<XZCJ> list = new ArrayList<XZCJ>();
        for (int i = 0; i < this.size(); i++)
        {
            list.add((XZCJ)this.get(i));
        }
        return list;
    }
}
