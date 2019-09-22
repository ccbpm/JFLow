package BP.En;

import BP.DA.*;
import java.util.*;
import java.math.*;

/** 
 row 集合
*/
public class Rows extends ArrayList<Object>
{
	public Rows()
	{
	}
	public final Row get(int index)
	{
		return (Row)this.InnerList[index];
	}
	/** 
	 增加一个Row .
	 
	 @param r row
	*/
	public final void Add(Row r)
	{
		this.InnerList.add(r);
	}
}