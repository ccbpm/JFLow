package bp.wf.unittesting;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import java.util.*;

/** 
 测试版本
*/
public class TestVers extends EntitiesNoName
{
	/** 
	 测试版本s
	*/
	public TestVers()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TestVer();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TestVer> ToJavaList()
	{
		return (List<TestVer>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TestVer> Tolist()
	{
		ArrayList<TestVer> list = new ArrayList<TestVer>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TestVer)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}