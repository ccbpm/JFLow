package bp.port;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;
import java.util.*;

/** 
 岗位s
*/
public class Stations extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
	/** 
	 岗位
	*/
	public Stations()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Station();
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


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Station> ToJavaList()
	{
		return (java.util.List<Station>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Station> Tolist()
	{
		ArrayList<Station> list = new ArrayList<Station>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Station)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}