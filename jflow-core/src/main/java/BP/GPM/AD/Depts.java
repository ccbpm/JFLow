package BP.GPM.AD;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.GPM.*;
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
	 * @throws Exception 
	*/
	public Depts(String parentNo) throws Exception
	{
		this.Retrieve(DeptAttr.ParentNo, parentNo);
	}
	@Override
	public int RetrieveAll() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.addOrderBy(DeptAttr.Idx);
		return qo.DoQuery();
	}


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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
}