package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;

/** 
 SQL模板s
*/
public class SQLTemplates extends EntitiesNoName
{

		///构造
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

		///


		///查询与构造

		/// 查询与构造


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SQLTemplate> ToJavaList()
	{
		return (List<SQLTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SQLTemplate> Tolist()
	{
		ArrayList<SQLTemplate> list = new ArrayList<SQLTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SQLTemplate)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}