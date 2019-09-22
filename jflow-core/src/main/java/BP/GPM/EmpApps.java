package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 管理员与系统权限s
*/
public class EmpApps extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 系统s
	*/
	public EmpApps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new EmpApp();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EmpApp> ToJavaList()
	{
		return (List<EmpApp>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EmpApp> Tolist()
	{
		ArrayList<EmpApp> list = new ArrayList<EmpApp>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((EmpApp)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}