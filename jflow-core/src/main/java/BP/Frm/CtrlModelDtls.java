package BP.Frm;

import BP.En.Entity;

import java.util.List;

public class CtrlModelDtls extends BP.En.EntitiesMyPK{
    /**
     控制模型集合

     */
    public CtrlModelDtls()
    {
    }


    @Override
    public Entity getNewEntity()
    {
        return new CtrlModelDtl();
    }

    public final java.util.List<CtrlModelDtl> ToJavaList()
    {
        return (List<CtrlModelDtl>)(Object)this;
    }
    /**
     转化成list

     @return List
     */
    public final java.util.ArrayList<CtrlModelDtl> Tolist()
    {
        java.util.ArrayList<CtrlModelDtl> list = new java.util.ArrayList<CtrlModelDtl>();
        for (int i = 0; i < this.size(); i++)
        {
            list.add((CtrlModelDtl)this.get(i));
        }
        return list;
    }
}
