package BP.Pub;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * NDs
 */
public class NDs extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void OnClear()
	{
		super.clear();
	}
	
	/**
	 * 年度集合
	 */
	public NDs()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getNewEntity()
	{
		return new ND();
	}
	
	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll();
	}
}