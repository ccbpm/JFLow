package BP.WF.Port;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.WF.*;
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



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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
	@Override
	public int RetrieveAll() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() == 0)
		{
			return super.RetrieveAll();
		}

		//按照orgNo查询.
		QueryObject qo = new QueryObject(this);
		qo.AddWhere("OrgNo", BP.Web.WebUser.getOrgNo());
		qo.addOr();
		qo.AddWhere("OrgNo", "");
		return qo.DoQuery();
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}