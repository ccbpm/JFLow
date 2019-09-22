package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import java.util.*;

/** 
 操作员s
*/
// </summary>
public class GPMEmps extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GPMEmp();
	}
	/** 
	 操作员s
	*/
	public GPMEmps()
	{
	}
	@Override
	public int RetrieveAll()
	{
		return super.RetrieveAll("Name");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GPMEmp> ToJavaList()
	{
		return (List<GPMEmp>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GPMEmp> Tolist()
	{
		ArrayList<GPMEmp> list = new ArrayList<GPMEmp>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((GPMEmp)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}