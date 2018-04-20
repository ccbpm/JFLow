package BP.En;

/**
 * 编号实体集合。
 */
public abstract class EntitiesNo extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public int RetrieveAllFromDBSource() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.addOrderBy("No");
		return qo.DoQuery();
	}
}