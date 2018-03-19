package BP.Demo;

import BP.En.EntitiesNoName;
import BP.En.Entity;

public class Suppliers extends EntitiesNoName
{
	@Override
	public Entity getGetNewEntity()
	{
		return new Supplier();
	}
	public Suppliers()
	{
	}
}
