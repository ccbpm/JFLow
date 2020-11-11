package bp.wf.port;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.CCBPMRunModel;
import bp.web.*;
import bp.wf.*;
import java.util.*;

/** 
部门集合
*/
public class Depts extends EntitiesNoName
{
	/** 
	 查询全部。
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		if (WebUser.getNo().equals("admin") == true)
		{
			return super.RetrieveAll();
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhere(DeptAttr.No, " = ", WebUser.getFK_Dept());
			qo.addOr();
			qo.AddWhere(DeptAttr.ParentNo, " = ", WebUser.getFK_Dept());
			return qo.DoQuery();
		}

		return this.Retrieve("OrgNo", WebUser.getOrgNo());
	}
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Dept();
	}
	/** 
	 create ens
	*/
	public Depts()
	{
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}