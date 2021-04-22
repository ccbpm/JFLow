package bp.port;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;
import java.util.*;

/** 
 岗位类型
*/
public class StationTypes extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
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
	public Entity getGetNewEntity()
	{
		return new StationType();
	}
	/** 
	 查询全部
	 
	 @param orderBy 排序
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAll(String orderBy) throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			return super.RetrieveAll(orderBy);
		}
		
		  //集团模式下的岗位体系: @0=每套组织都有自己的岗位体系@1=所有的组织共享一套岗则体系.
        if ( SystemConfig.getGroupStationModel() == 1)
            return super.RetrieveAll(orderBy);


		//按照orgNo查询.
		return this.Retrieve("OrgNo", WebUser.getOrgNo(), orderBy);
	}
	/** 
	 查询全部
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			return super.RetrieveAll();
		}
		
		  //集团模式下的岗位体系: @0=每套组织都有自己的岗位体系@1=所有的组织共享一套岗则体系.
        if ( SystemConfig.getGroupStationModel() == 1)
            return super.RetrieveAll();


		//按照orgNo查询.
		return this.Retrieve("OrgNo", WebUser.getOrgNo());
	}

	public String GetStationTypes(String orgNo) throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single || orgNo.equals("0"))
		{
			this.RetrieveAll();
			return this.ToJson();
		}
		this.Retrieve(StationTypeAttr.OrgNo, orgNo);

		return this.ToJson();
	}
		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<StationType> ToJavaList()
	{
		return (java.util.List<StationType>)(Object)this;
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}