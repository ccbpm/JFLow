package BP.WF;

import BP.En.Entities;
import BP.En.Entity;
/** 
 退回轨迹s 
*/
public class ReturnWorks extends Entities
{
	/** 
	 退回轨迹s
	*/
	public ReturnWorks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ReturnWork();
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<ReturnWork> ToJavaList()
	{
		return (java.util.List<ReturnWork>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<ReturnWork> Tolist()
	{
		java.util.ArrayList<ReturnWork> list = new java.util.ArrayList<ReturnWork>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ReturnWork)this.get(i));
		}
		return list;
	}
}