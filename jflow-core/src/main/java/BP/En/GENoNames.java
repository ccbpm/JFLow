package BP.En;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 GENoNames
*/
public class GENoNames extends EntitiesNoName
{
	/** 
	 物理表
	*/
	public String SFTable = null;
	public String Desc = null;

	/** 
	 GENoNames
	*/
	public GENoNames()
	{
	}
	public GENoNames(String sftable, String tableDesc)
	{
		this.SFTable = sftable;
		this.Desc = tableDesc;
	}
	@Override
	public Entity getNewEntity()
	{
		return new GENoName(this.SFTable, this.Desc);
	}
	@Override
	public int RetrieveAll() throws Exception
	{
		return this.RetrieveAllFromDBSource();
	}



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GENoName> ToJavaList()
	{
		return (List<GENoName>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GENoName> Tolist()
	{
		ArrayList<GENoName> list = new ArrayList<GENoName>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GENoName)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}