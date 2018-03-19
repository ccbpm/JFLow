package BP.Sys;

import BP.En.EntitiesOID;
import BP.En.Entity;

/**
 * 默认值s
 */
public class DefVals extends EntitiesOID {
	/** 
	 默认值
	 
	*/
	public DefVals() {
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new DefVal();
	}

}