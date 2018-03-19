package BP.Demo;

import BP.En.EntitiesNoName;
import BP.En.Entity;

public class AnQuanItems extends EntitiesNoName
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new AnQuanItem();
	}
	//#endregion

	//#region 构造方法
	/** 
	 安全项目s
	*/
	public AnQuanItems()
	{
	}

}
