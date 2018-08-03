package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 管理员与系统权限s
 
*/
public class EmpApps extends EntitiesMyPK
{
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
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<EmpApp> ToJavaList()
	{
		return (java.util.List<EmpApp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<EmpApp> Tolist()
	{
		java.util.ArrayList<EmpApp> list = new java.util.ArrayList<EmpApp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EmpApp)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}