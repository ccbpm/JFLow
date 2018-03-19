package BP.WF.Data;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 工作质量评价s BP.Port.FK.Evals
*/
public class Evals extends EntitiesMyPK
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Eval();
	}
	/** 
	 工作质量评价s
	*/
	public Evals()
	{
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Eval> ToJavaList()
	{
		return (java.util.List<Eval>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Eval> Tolist()
	{
		java.util.ArrayList<Eval> list = new java.util.ArrayList<Eval>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Eval)this.get(i));
		}
		return list;
	}
}