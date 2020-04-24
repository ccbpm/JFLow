package BP.Port;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import java.util.*;

/** 
 用户组人员s
*/
public class TeamEmps extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 用户组s
	*/
	public TeamEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new TeamEmp();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TeamEmp> ToJavaList()
	{
		return (List<TeamEmp>) (Object) this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TeamEmp> Tolist()
	{
		ArrayList<TeamEmp> list = new ArrayList<TeamEmp>();
		for (int i = 0; i < this.size(); i++) {
			list.add((TeamEmp) this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}