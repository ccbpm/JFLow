package bp.wf.admin;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import java.util.*;

/** 
 表单目录
*/
public class FrmSorts extends EntitiesNoName
{

		///#region 构造.
	/** 
	 表单目录s
	*/
	public FrmSorts()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmSort();
	}

	/** 
	 
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception {
		if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			return this.Retrieve(FrmSortAttr.OrgNo, bp.web.WebUser.getOrgNo(), FrmSortAttr.Idx);
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmSortAttr.ParentNo, "!=", "0");
		qo.addOrderBy("Idx");
		int i = qo.DoQuery();

		if (i == 0)
		{
			FrmSort fs = new FrmSort();
			fs.setName("流程树");
			fs.setNo("100");
			fs.setParentNo("0");
			fs.Insert();

			fs = new FrmSort();
			fs.setName("公文类");
			fs.setNo("01");
			fs.setParentNo("100");
			fs.Insert();

			fs = new FrmSort();
			fs.setName("办公类");
			fs.setNo("02");
			fs.setParentNo("100");
			fs.Insert();

			qo = new QueryObject(this);
			qo.AddWhere(FrmSortAttr.ParentNo, "!=", "");
			qo.addOrderBy("Idx");
			i = qo.DoQuery();
		}
		return i;
	}

		///#endregion 构造.



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmSort> ToJavaList()
	{
		return (List<FrmSort>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmSort> Tolist()
	{
		ArrayList<FrmSort> list = new ArrayList<FrmSort>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmSort)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}