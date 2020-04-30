package BP.Port;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.CCBPMRunModel;

import java.util.*;

/** 
 岗位类型
*/
public class StationTypes extends EntitiesNoName
{
	/** 
	 岗位类型s
	*/
	public StationTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new StationType();
	}
	/** 
	 查询全部
	 
	 @param orderBy 排序
	 @return 
	*/
	@Override
	public int RetrieveAll(String orderBy) throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			return super.RetrieveAll(orderBy);
		}

		//按照orgNo查询.
		return this.Retrieve("OrgNo", BP.Web.WebUser.getOrgNo(), orderBy);
	}
	/** 
	 查询全部
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			return super.RetrieveAll();
		}

		//按照orgNo查询.
		return this.Retrieve("OrgNo", BP.Web.WebUser.getOrgNo());
	}

	/**
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationType> ToJavaList()
	{
		return (List<StationType>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationType> Tolist()
	{
		ArrayList<StationType> list = new ArrayList<StationType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationType)this.get(i));
		}
		return list;
	}
}