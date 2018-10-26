package BP.GPM;

import BP.En.*;

/** 
 操作员s
// </summary>
*/
public class GPMEmps extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GPMEmp();
	}
	/** 
	 操作员s
	 
	*/
	public GPMEmps()
	{
	}
	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll("Name");
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GPMEmp> ToJavaList()
	{
		return (java.util.List<GPMEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GPMEmp> Tolist()
	{
		java.util.ArrayList<GPMEmp> list = new java.util.ArrayList<GPMEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GPMEmp)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}