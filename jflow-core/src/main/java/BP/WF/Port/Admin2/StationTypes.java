package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Port.*;
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
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		return this.Retrieve(StationAttr.OrgNo, BP.Web.WebUser.getOrgNo(), StationAttr.Idx);
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
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}