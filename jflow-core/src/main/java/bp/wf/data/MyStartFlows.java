package bp.wf.data;

import bp.en.*;
import java.util.*;

/** 
 我发起的流程s
*/
public class MyStartFlows extends Entities
{

		///#region 方法.
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MyStartFlow();
	}
	/** 
	 我发起的流程集合
	*/
	public MyStartFlows()throws Exception
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MyStartFlow> ToJavaList()
	{
		return (java.util.List<MyStartFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyStartFlow> Tolist()throws Exception
	{
		ArrayList<MyStartFlow> list = new ArrayList<>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyStartFlow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}