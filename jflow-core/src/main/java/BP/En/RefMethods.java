package BP.En;

import BP.DA.*;
import BP.Web.Controls.*;
import java.io.*;

/** 
 
*/
public class RefMethods extends ArrayList<Object> implements Serializable
{
	/** 
	 加入
	 
	 @param attr attr
	*/
	public final void Add(RefMethod en)
	{
		if (this.IsExits(en))
		{
			return;
		}
		en.Index = this.InnerList.size();
		this.InnerList.add(en);
	}
	/** 
	 是不是存在集合里面
	 
	 @param en 要检查的RefMethod
	 @return true/false
	*/
	public final boolean IsExits(RefMethod en)
	{
		for (RefMethod dtl : this)
		{
			if (dtl.ClassMethodName.equals(en.ClassMethodName))
			{
				return true;
			}
		}
		return false;
	}
	/** 
	 能够看到的属性
	*/
	public final int getCountOfVisable()
	{
		int i = 0;
		for (RefMethod rm : this)
		{
			if (rm.Visable)
			{
				i++;
			}
		}
		return i;
	}
	/** 
	 根据索引访问集合内的元素Attr。
	*/
	public final RefMethod get(int index)
	{
		return (RefMethod)this.InnerList[index];
	}
}