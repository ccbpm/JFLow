package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
部门集合
*/
public class Depts extends EntitiesTree
{
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Dept();
	}
	/** 
	 部门集合
	*/
	public Depts()
	{
	}
	/** 
	 部门集合
	 
	 @param parentNo 父部门No
	*/
	public Depts(String parentNo)
	{
		this.Retrieve(DeptAttr.ParentNo, parentNo);
	}
	@Override
	public int RetrieveAll()
	{
		QueryObject qo = new QueryObject(this);
		qo.addOrderBy(GPM.DeptAttr.Idx);
		return qo.DoQuery();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Dept> ToJavaList()
	{
		return (List<Dept>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Dept> Tolist()
	{
		ArrayList<Dept> list = new ArrayList<Dept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Dept)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
}