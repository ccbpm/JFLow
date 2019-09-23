package BP.En;

import java.util.ArrayList;
import java.util.List;

/**
 * 具有编号名称的基类实体s
 */
public abstract class EntitiesNoName extends EntitiesNo
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EntityNoName> convertEntityNoName(Object obj)
	{
		return (ArrayList<EntityNoName>) obj;
	}
	public List<EntityNoName> ToJavaListEnNo()
	{
		return (List<EntityNoName>)(Object)this;
	}
	/**
	 * 将对象添加到集合尾处，如果对象已经存在，则不添加.
	 * 
	 * @param entity
	 *            要添加的对象
	 * @return 返回添加到的地方
	 * @throws Exception 
	 */
	/*
	 * warning public int AddEntity(EntityNoName entity) { for (EntityNoName en
	 * : convertEntityNoName(this)) { if (en.getNo().equals(entity.getNo())) {
	 * return 0; } } return this.add(entity); }
	 */
	public void AddEntity(EntityNoName entity) throws Exception
	{
		for (EntityNoName en : convertEntityNoName(this))
		{
			if (en.getNo().equals(entity.getNo()))
			{
				return;
			}
		}
		this.add(entity);
	}
	
	/**
	 * 将对象添加到集合尾处，如果对象已经存在，则不添加
	 * 
	 * @param entity
	 *            要添加的对象
	 * @return 返回添加到的地方
	 * @throws Exception 
	 */
	public void Insert(int index, EntityNoName entity) throws Exception
	{
		for (EntityNoName en : convertEntityNoName(this))
		{
			if (en.getNo().equals(entity.getNo()))
			{
				return;
			}
		}
		
		this.add(index, entity);
	}
	
	/**
	 * 根据位置取得数据
	 */
	public final EntityNoName getItem(int index)
	{
		return (EntityNoName) ((this.get(index) instanceof EntityNoName) ? this
				.get(index) : null);
		/*
		 * warning return (EntityNoName)this.get(index);
		 */
	}
	
	/**
	 * 构造
	 */
	public EntitiesNoName()
	{
	}
	
	/**
	 * 按照名称模糊查询
	 * 
	 * @param likeName
	 *            likeName
	 * @return 返回查询的Num
	 * @throws Exception 
	 */
	public final int RetrieveByLikeName(String likeName) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere("Name", "like", " %" + likeName + "% ");
		return qo.DoQuery();
	}
	
	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll("No");
	}
}