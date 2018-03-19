package BP.En;

import java.util.ArrayList;

/**
 * 多个树实体s
 */
public abstract class EntitiesMultiTree extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 查询他的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final int RetrieveHisChinren(EntityMultiTree en)
	{
		int i = this.Retrieve(EntityMultiTreeAttr.ParentNo, en.getNo());
		this.AddEntity(en);
		return i + 1;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EntityMultiTree> convertEntityMultiTree(Object obj)
	{
		return (ArrayList<EntityMultiTree>) obj;
	}
	
	/**
	 * 获取它的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final EntitiesTree GenerHisChinren(EntityMultiTree en)
	{
		Entities tempVar = this.CreateInstance();
		EntitiesTree ens = (EntitiesTree) ((tempVar instanceof EntitiesTree) ? tempVar
				: null);
		for (EntityMultiTree item : convertEntityMultiTree(ens))
		{
			if (en.getNo().equals(en.getParentNo()))
			{
				ens.AddEntity(item);
			}
		}
		return ens;
	}
	
	/**
	 * 根据位置取得数据
	 */
	public final EntityMultiTree getItem(int index)
	{
		return (EntityMultiTree) this.get(index);
		/*
		 * warning return (EntityMultiTree)this.get(index);
		 */
	}
	
	/**
	 * 构造
	 */
	public EntitiesMultiTree()
	{
	}
}