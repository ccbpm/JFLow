package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 Excel表单属性s
*/
public class MapFrmExcels extends EntitiesNoName
{

		///#region 构造
	/** 
	 Excel表单属性s
	*/
	public MapFrmExcels()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MapFrmExcel();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrmExcel> ToJavaList()
	{
		return (List<MapFrmExcel>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrmExcel> Tolist()
	{
		ArrayList<MapFrmExcel> list = new ArrayList<MapFrmExcel>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmExcel)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}