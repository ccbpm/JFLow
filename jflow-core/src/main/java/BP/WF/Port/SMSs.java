package BP.WF.Port;

import BP.En.Entities;
import BP.En.Entity;

/** 
 消息s
*/
public class SMSs extends Entities
{
	@Override
	public Entity getGetNewEntity()
	{
		return new SMS();
	}
	public SMSs()
	{
	}
}