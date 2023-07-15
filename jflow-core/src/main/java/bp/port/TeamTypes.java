package bp.port;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.CCBPMRunModel;

import java.util.*;

/** 
 用户组类型
*/
public class TeamTypes extends EntitiesNoName
{
	/** 
	 用户组类型s
	*/
	public TeamTypes()  {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new TeamType();
	}


	/**
	 查询全部
	 @return
	 */
	@Override
	public int RetrieveAll() throws Exception {
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
			return super.RetrieveAll();

		//集团模式下的岗位体系: @0=每套组织都有自己的岗位体系@1=所有的组织共享一套岗则体系.
		if (SystemConfig.getGroupStationModel() == 1)
			return super.RetrieveAll();
		//按照orgNo查询.
		return this.Retrieve("OrgNo", bp.web.WebUser.getOrgNo());
	}
	@Override
	public int RetrieveAllFromDBSource()  throws Exception
	{
		return this.RetrieveAll();
	}
	@Override
	public int RetrieveAllFromDBSource(String orderBY) throws Exception {
		return this.RetrieveAll(orderBY);
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<TeamType> ToJavaList() {
		return (java.util.List<TeamType>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TeamType> Tolist()  {
		ArrayList<TeamType> list = new ArrayList<TeamType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TeamType)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}