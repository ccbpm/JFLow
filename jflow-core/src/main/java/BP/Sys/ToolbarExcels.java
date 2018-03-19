package BP.Sys;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 ToolbarExcel表单.
 
*/
public class ToolbarExcels extends EntitiesNoName
{
	/** 
	 功能控制
	 
	*/
	public ToolbarExcels()
	{
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ToolbarExcel();
	}

	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<ToolbarExcel> Tolist()
	{
		java.util.ArrayList<ToolbarExcel> list = new java.util.ArrayList<ToolbarExcel>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ToolbarExcel)this.get(i));
		}
		return list;
	}
}