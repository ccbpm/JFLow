package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程测试集合
*/
public class TestCases extends EntitiesMyPK
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new TestCase();
	}

		///#endregion


		///#region 构造方法
	/** 
	 流程测试集合
	*/
	public TestCases()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TestCase> ToJavaList()
	{
		return (List<TestCase>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TestCase> Tolist()
	{
		ArrayList<TestCase> list = new ArrayList<TestCase>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TestCase)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}