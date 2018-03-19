package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

public class FrmNodeExts extends EntitiesMyPK{
	 /**
	  * 节点表单
	  */
    public FrmNodeExts() { }
   // #endregion 构造方法..

    //#region 公共方法.
    /**
     * 得到它的 Entity 
     */
    
    @Override
	public Entity getGetNewEntity() {
            return new FrmNodeExt();
    }
   // #endregion 公共方法.
    public final java.util.List<FrmNodeExt> ToJavaList()
	{
		return (java.util.List<FrmNodeExt>)(Object)this;
	}
    /// <summary>
    /// 转化成list
    /// </summary>
    /// <returns>List</returns>
    
    public final java.util.ArrayList<FrmNodeExt> Tolist()
	{
		java.util.ArrayList<FrmNodeExt> list = new java.util.ArrayList<FrmNodeExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNodeExt)this.get(i));
		}
		return list;
	}
}

