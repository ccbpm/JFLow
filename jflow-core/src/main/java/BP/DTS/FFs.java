package BP.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import java.io.*;

/** 
 属性集合
*/
public class FFs extends ArrayList<Object> implements Serializable
{
	public final int getPKCount()
	{
		int i = 0;
		for (FF ff : this)
		{
			if (ff.IsPK)
			{
				i++;
			}
		}
		if (i == 0)
		{
			throw new RuntimeException("没有设置PK. 请检查map 错误.");
		}
		return i;
	}
	/** 
	 属性集合
	*/
	public FFs()
	{
	}
	/** 
	 加入一个属性
	 
	 @param attr
	*/
	public final void Add(FF ff)
	{
		this.InnerList.add(ff);
	}
	/** 
	 增加一个数据影射
	 
	 @param fromF
	 @param toF
	 @param dataType
	 @param isPk
	*/
	public final void Add(String fromF, String toF, int dataType, boolean isPk)
	{
		this.Add(new FF(fromF, toF, dataType, isPk));
	}
	/** 
	 根据索引访问集合内的元素Attr。
	*/
	public final FF get(int index)
	{
		return (FF)this.InnerList[index];
	}
}