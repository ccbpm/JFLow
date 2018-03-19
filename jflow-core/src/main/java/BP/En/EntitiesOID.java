package BP.En;

public abstract class EntitiesOID extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 构造
	 */
	public EntitiesOID()
	{
	}
	
	// 查询方法, 专用于与语言有关的实体
	/**
	 * 查询出来, 所有中文的实例 .
	 */
	public final void RetrieveAllCNEntities()
	{
		this.RetrieveByLanguageNo("CH");
	}
	
	/**
	 * 按语言查询。
	 */
	public final void RetrieveByLanguageNo(String LanguageNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere("LanguageNo", LanguageNo);
		qo.DoQuery();
	}
}