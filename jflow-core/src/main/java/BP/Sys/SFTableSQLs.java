package BP.Sys;

import java.util.ArrayList;
import java.util.List;

import BP.En.*;

/** 
 用户自定义表s
 
*/
public class SFTableSQLs extends EntitiesNoName
{

		
	/** 
	 用户自定义表s
	 
	*/
	public SFTableSQLs()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SFTableSQL();
	}
	/** 
	 转化成 java list
	 @return List
	*/
	public final List<SFTableSQL> ToJavaList()
	{
		return (List<SFTableSQL>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final ArrayList<SFTableSQL> Tolist()
	{
		ArrayList<SFTableSQL> list = new ArrayList<SFTableSQL>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SFTableSQL)this.get(i));
		}
		return list;
	}
}