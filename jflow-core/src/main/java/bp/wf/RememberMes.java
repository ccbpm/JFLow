package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.port.*;
import java.util.*;

/** 
 记忆我
*/
public class RememberMes extends Entities
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RememberMe();
	}
	/** 
	 RememberMe
	*/
	public RememberMes()
	{
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<RememberMe> ToJavaList()
	{
		return (List<RememberMe>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<RememberMe> Tolist()
	{
		ArrayList<RememberMe> list = new ArrayList<RememberMe>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RememberMe)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}