package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
用户自定义表s
*/
public class SFTableClasss extends EntitiesNoName
{
	/** 
	 用户自定义表s
	*/
	public SFTableClasss()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SFTableClass();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SFTableClass> ToJavaList()
	{
		return (List<SFTableClass>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SFTableClass> Tolist()
	{
		ArrayList<SFTableClass> list = new ArrayList<SFTableClass>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SFTableClass)this.get(i));
		}
		return list;
	}
}
