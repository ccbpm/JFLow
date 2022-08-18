package bp.dts;

import java.io.*;
import java.util.ArrayList;

/**
 属性集合
 */
public class FFs extends ArrayList<FF> implements Serializable
{
	private static final long serialVersionUID = 1L;
	public final int getPKCount() throws Exception {
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
	public FFs() throws Exception {
	}
	/**
	 加入一个属性

	 param ff
	 */
	public final void Add(FF ff)
	{
		this.add(ff);
	}
	/**
	 增加一个数据影射

	 param fromF
	 param toF
	 param dataType
	 param isPk
	 */
	public final void Add(String fromF, String toF, int dataType, boolean isPk)
	{
		this.Add(new FF(fromF, toF, dataType, isPk));
	}

}