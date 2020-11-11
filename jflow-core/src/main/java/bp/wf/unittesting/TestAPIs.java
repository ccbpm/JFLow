package bp.wf.unittesting;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import java.util.*;

/** 
 测试过程
*/
public class TestAPIs extends EntitiesNoName
{
	/** 
	 测试过程s
	*/
	public TestAPIs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TestAPI();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TestAPI> ToJavaList()
	{
		return (List<TestAPI>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TestAPI> Tolist()
	{
		ArrayList<TestAPI> list = new ArrayList<TestAPI>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TestAPI)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}