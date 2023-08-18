package bp.ta;

import bp.en.EntitiesTree;
import bp.en.Entity;
import bp.sys.CCBPMRunModel;

import java.util.*;

/** 
 流程类别
*/
public class Sorts extends EntitiesTree
{
	/** 
	 流程类别s
	*/
	public Sorts()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Sort();
	}
	/** 
	 
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception {
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			return this.Retrieve(SortAttr.OrgNo, bp.web.WebUser.getOrgNo(), SortAttr.Idx);
		}

		int i = super.RetrieveAll(SortAttr.Idx);
		if (i == 0)
		{
			Sort fs = new Sort();
			fs.setName("流程树");
			fs.setNo("1");
			fs.setParentNo("0");
			fs.Insert();

			fs = new Sort();
			fs.setName("公文类");
			fs.setNo("01");
			fs.setParentNo("1");
			fs.Insert();

			fs = new Sort();
			fs.setName("办公类");
			fs.setNo("02");
			fs.setParentNo("1");
			fs.Insert();
			i = super.RetrieveAll(SortAttr.Idx);
		}
		return i;
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Sort> ToJavaList()
	{
		return (java.util.List<Sort>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Sort> Tolist()
	{
		ArrayList<Sort> list = new ArrayList<Sort>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Sort)this.get(i));
		}
		return list;
	}
	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
