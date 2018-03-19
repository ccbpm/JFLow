package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 系统表单s
 
*/
public class SysForms extends EntitiesNoName
{
	/** 
	 Frm
	 
	*/
	public SysForms()
	{

	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SysForm();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<SysForm> ToJavaList()
	{
		return (java.util.List<SysForm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<SysForm> Tolist()
	{
		java.util.ArrayList<SysForm> list = new java.util.ArrayList<SysForm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysForm)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}