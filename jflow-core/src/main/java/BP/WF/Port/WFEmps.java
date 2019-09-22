package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 操作员s 
*/
public class WFEmps extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 操作员s
	*/
	public WFEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new WFEmp();
	}

	@Override
	public int RetrieveAll()
	{
		return super.RetrieveAll("FK_Dept", "Idx");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<WFEmp> ToJavaList()
	{
		return (List<WFEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<WFEmp> Tolist()
	{
		ArrayList<WFEmp> list = new ArrayList<WFEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((WFEmp)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}