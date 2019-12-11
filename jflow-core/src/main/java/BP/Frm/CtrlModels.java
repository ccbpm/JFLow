package BP.Frm;

import BP.En.Entity;

import java.util.List;

public class CtrlModels  extends BP.En.EntitiesMyPK
{
    /**
     控制模型集合

     */
    public CtrlModels()
    {
    }


    @Override
    public Entity getNewEntity()
    {
        return new CtrlModel();
    }

    public final java.util.List<CtrlModel> ToJavaList()
    {
        return (List<CtrlModel>)(Object)this;
    }
    /**
     转化成list

     @return List
     */
    public final java.util.ArrayList<CtrlModel> Tolist()
    {
        java.util.ArrayList<CtrlModel> list = new java.util.ArrayList<CtrlModel>();
        for (int i = 0; i < this.size(); i++)
        {
            list.add((CtrlModel)this.get(i));
        }
        return list;
    }

}
