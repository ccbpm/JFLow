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
		///#region 构造
	public GroupEnsTemplates()
	{
	}
	/** 
	 
	 
	 @param emp
	 * @throws Exception 
	*/
	public GroupEnsTemplates(String emp) throws Exception
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
	public Entity getNewEntity()
	{
		return new GroupEnsTemplate();
	}

	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GroupEnsTemplate> ToJavaList()
	{
		return (List<GroupEnsTemplate>)(Object)this;
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

}