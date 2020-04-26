package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 权限组部门s
*/
public class GroupDepts extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 权限组s
	*/
	public GroupDepts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new GroupDept();
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GroupDept> ToJavaList()
	{
		return (List<GroupDept>) (Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GroupDept> Tolist()
	{
		ArrayList<GroupDept> list = new ArrayList<GroupDept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupDept)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}