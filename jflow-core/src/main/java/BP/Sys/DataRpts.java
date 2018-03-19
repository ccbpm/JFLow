package BP.Sys;

import BP.En.Entity;
import BP.En.SimpleNoNames;

/**
 * DataRpt数据存储
 */
public class DataRpts extends SimpleNoNames
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * DataRpt数据存储s
	 */
	public DataRpts()
	{
	}
	
	/**
	 * DataRpt数据存储 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new DataRpt();
	}
}