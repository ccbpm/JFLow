package bp.port;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import java.util.*;

/** 
部门s
*/
public class Depts extends bp.en.EntitiesNoName
{
	private static final long serialVersionUID = 1L;
	///初始化实体.
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
		/// 初始化实体.
 
		///重写查询,add by stone 2015.09.30 为了适应能够从webservice数据源查询数据.
	/** 
	     重写查询全部适应从WS取数据需要。	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() ==CCBPMRunModel.Single || WebUser.getNo().equals("admin"))
		{
			return super.RetrieveAll();
		}

		//按照orgNo查询.
	   return this.Retrieve("OrgNo", WebUser.getOrgNo());
	}
	/** 
	 重写重数据源查询全部适应从WS取数据需要
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAllFromDBSource() throws Exception
	{

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			return super.RetrieveAllFromDBSource();
		}

		//按照orgNo查询.
	   return this.Retrieve("OrgNo", WebUser.getOrgNo());
	}

		/// 重写查询.


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}