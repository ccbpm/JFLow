package BP.WF.Template;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesTree;
import BP.En.Entity;

/** 
 流程类别
*/
public class FlowSorts extends EntitiesTree
{
	/** 
	 流程类别s
	*/
	public FlowSorts()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowSort();
	}

	/** 
	 流程类别s
	 
	 @param no ss
	 @param name anme
	*/
	public final void AddByNoName(String no, String name)
	{
		FlowSort en = new FlowSort();
		en.setNo (no);
		en.setName (name);
		this.AddEntity(en);
	}
	@Override
	public int RetrieveAll() throws Exception
	{
		int i = super.RetrieveAll(FlowSortAttr.Idx);
		if (i == 0)
		{
			FlowSort fs = new FlowSort();
			fs.setName("公文类");
			fs.setNo("01");
			fs.Insert();

			fs = new FlowSort();
			fs.setName("公文类");
			fs.setNo("02");
			fs.Insert();
			i = super.RetrieveAll();
		}

		return i;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowSort> ToJavaListFs()
	{
		return (List<FlowSort>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowSort> Tolist()
	{
		ArrayList<FlowSort> list = new ArrayList<FlowSort>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowSort)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}