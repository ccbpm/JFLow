package BP.Pub;

import BP.En.Entity;
import BP.En.SimpleNoNameFixs;

/**
 * NDs
 */
public class NDs extends SimpleNoNameFixs
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
	public Entity getGetNewEntity()
	{
		return new ND();
	}
	
	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll();
	}
}