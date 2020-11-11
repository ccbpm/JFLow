package bp.gpm;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.web.*;
import bp.sys.CCBPMRunModel;
import java.util.*;

/** 
部门集合
*/
public class Depts extends EntitiesTree
{
	private static final long serialVersionUID = 1L;
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getGetNewEntity()
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

		if (WebUser.getNo().equals("admin") == true)
		{
			QueryObject qo = new QueryObject(this);
			qo.addOrderBy(DeptAttr.Idx);
			return qo.DoQuery();
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(DeptAttr.No, " = ", WebUser.getFK_Dept());
			qo.addOr();
			qo.AddWhere(DeptAttr.ParentNo, " = ", WebUser.getFK_Dept());
			qo.addOrderBy(DeptAttr.Idx);
			return qo.DoQuery();
		}

		return this.Retrieve("OrgNo", WebUser.getOrgNo(), DeptAttr.Idx);


	}


		///为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Dept> ToJavaList()
	{
		return (java.util.List<Dept>)(Object)this;
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

		/// 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
}