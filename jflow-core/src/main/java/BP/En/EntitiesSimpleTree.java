package BP.En;

import java.util.ArrayList;

/**
 * 树实体s
 */
public abstract class EntitiesSimpleTree extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EntitySimpleTree> convertEntitySimpleTree(Object obj)
	{
		return (ArrayList<EntitySimpleTree>) obj;
	}
	
	/**
	 * 查询他的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final int RetrieveHisChinren(EntitySimpleTree en)
	{
		int i = this.Retrieve(EntitySimpleTreeAttr.ParentNo, en.getNo());
		this.AddEntity(en);
		return i + 1;
	}
	
	/**
	 * 获取它的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final EntitiesTree GenerHisChinren(EntitySimpleTree en)
	{
		Entities tempVar = this.CreateInstance();
		EntitiesTree ens = (EntitiesTree) ((tempVar instanceof EntitiesTree) ? tempVar
				: null);
		for (EntitySimpleTree item : convertEntitySimpleTree(ens))
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
	public final EntitySimpleTree getItem(int index)
	{
		return (EntitySimpleTree) this.get(index);
	}
	
	/**
	 * 构造
	 */
	public EntitiesSimpleTree()
	{
	}
	
	/**
	 * 查询全部
	 * 
	 * @return
	 */
	@Override
	public int RetrieveAll()
	{
		return super.RetrieveAll("No");
	}
}