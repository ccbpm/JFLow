package BP.WF.Template;
import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 SQL模板s
 
*/
public class SQLTemplates extends EntitiesNoName
{

		///#region 构造
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SQLTemplate();
	}
	/** 
	 SQL模板
	 
	*/
	public SQLTemplates()
	{
	}

		///#endregion


		///#region 查询与构造

		///#endregion 查询与构造


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<SQLTemplate> ToJavaList()
	{
		return (java.util.List<SQLTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<SQLTemplate> Tolist()
	{
		java.util.ArrayList<SQLTemplate> list = new java.util.ArrayList<SQLTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SQLTemplate)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}