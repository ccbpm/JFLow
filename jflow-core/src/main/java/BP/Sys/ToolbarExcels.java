package BP.Sys;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import java.util.*;

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
	public Entity getNewEntity()
	{
		return new ToolbarExcel();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ToolbarExcel> Tolist()
	{
		ArrayList<ToolbarExcel> list = new ArrayList<ToolbarExcel>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ToolbarExcel)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}