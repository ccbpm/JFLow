package BP.Sys;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 ToolbarExcel表单.
 
*/
public class ToolbarExcelSlns extends EntitiesMyPK
{
	/** 
	 功能控制
	 
	*/
	public ToolbarExcelSlns()
	{
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ToolbarExcelSln();
	}

	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<ToolbarExcelSln> ToJavaList()
	{
		return (java.util.List<ToolbarExcelSln>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<ToolbarExcelSln> Tolist()
	{
		java.util.ArrayList<ToolbarExcelSln> list = new java.util.ArrayList<ToolbarExcelSln>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ToolbarExcelSln)this.get(i));
		}
		return list;
	}
}