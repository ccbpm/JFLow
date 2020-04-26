package BP.Port;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.CCBPMRunModel;

import java.util.*;

/** 
 岗位s
*/
public class Stations extends EntitiesNoName
{
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
	public Entity getNewEntity()
	{
		return new Station();
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

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Station> ToJavaList()
	{
		return (List<Station>)(Object)this;
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
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}