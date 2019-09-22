package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import java.util.*;

/** 
 实体集合
*/
public class GroupEnsTemplates extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	public GroupEnsTemplates()
	{
	}
	/** 
	 
	 
	 @param emp
	*/
	public GroupEnsTemplates(String emp)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GroupEnsTemplateAttr.Rec, emp);
		qo.addOr();
		qo.AddWhere(GroupEnsTemplateAttr.Rec, "admin");
		qo.DoQuery();

	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GroupEnsTemplate();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GroupEnsTemplate> ToJavaList()
	{
		return (List<GroupEnsTemplate>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GroupEnsTemplate> Tolist()
	{
		ArrayList<GroupEnsTemplate> list = new ArrayList<GroupEnsTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupEnsTemplate)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}