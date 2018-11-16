package BP.Demo;

import BP.En.EntitiesOID;
import BP.En.Entity;

public class QingJias extends EntitiesOID
{
	
	// 得到它的 Entity
	private static final long serialVersionUID = 1L;
	
	@Override
	public Entity getGetNewEntity()
	{
		return new QingJia();
	}
	
	// 请假s
	public QingJias()
	{
	}
}
