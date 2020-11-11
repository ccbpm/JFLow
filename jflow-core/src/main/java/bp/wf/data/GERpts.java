package bp.wf.data;

import bp.en.*;
import java.util.*;

/** 
 报表s
*/
public class GERpts extends EntitiesOID
{
	private static final long serialVersionUID = 1L;
	/** 
	 报表s
	*/
	public GERpts()
	{
	}

	/** 
	 获得一个实例.
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GERpt();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GERpt> ToJavaList()
	{
		return (List<GERpt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GERpt> Tolist()
	{
		ArrayList<GERpt> list = new ArrayList<GERpt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GERpt)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}