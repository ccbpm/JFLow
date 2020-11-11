package bp.gpm;

import bp.en.*;
import java.util.*;

/** 
 管理员与系统权限s
*/
public class EmpApps extends EntitiesMyPK
{
	private static final long serialVersionUID = 1L;
	///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EmpApp> ToJavaList()
	{
		return (List<EmpApp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EmpApp> Tolist()
	{
		ArrayList<EmpApp> list = new ArrayList<EmpApp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EmpApp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}