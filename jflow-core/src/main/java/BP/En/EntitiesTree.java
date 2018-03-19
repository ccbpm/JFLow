package BP.En;

import java.util.ArrayList;
import java.util.List;

/**
 * 树实体s
 */
public abstract class EntitiesTree extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EntityTree> convertEntityTree(Object obj)
	{
		return (ArrayList<EntityTree>) obj;
	}
	public List<EntityTree> ToJavaListEnTree()
	{
		return (List<EntityTree>)(Object)this;
	}
	/**
	 * 查询他的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final int RetrieveHisChinren(EntityTree en)
	{
		int i = this.Retrieve(EntityTreeAttr.ParentNo, en.getNo());
		this.AddEntity(en);
		return i + 1;
	}
	
	/**
	 * 获取它的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final EntitiesTree GenerHisChinren(EntityTree en)
	{
		Entities tempVar = this.CreateInstance();
		EntitiesTree ens = (EntitiesTree) ((tempVar instanceof EntitiesTree) ? tempVar
				: null);
		for (EntityTree item : convertEntityTree(ens))
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
	public final EntityTree getItem(int index)
	{
		return (EntityTree) this.get(index);
	}
	
	/**
	 * 构造
	 */
	public EntitiesTree()
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
		return super.RetrieveAll("TreeNo");
	}
}