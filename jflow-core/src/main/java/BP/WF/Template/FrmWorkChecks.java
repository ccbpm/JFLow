package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 审核组件s
 
*/
public class FrmWorkChecks extends Entities
{

		
	/** 
	 审核组件s
	 
	*/
	public FrmWorkChecks()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmWorkCheck();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmWorkCheck> ToJavaList()
	{
		return (java.util.List<FrmWorkCheck>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmWorkCheck> Tolist()
	{
		java.util.ArrayList<FrmWorkCheck> list = new java.util.ArrayList<FrmWorkCheck>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmWorkCheck)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}