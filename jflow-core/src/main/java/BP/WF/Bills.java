package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import java.util.*;

/** 
 单据s
*/
public class Bills extends EntitiesMyPK
{

		///#region 构造方法属性
	/** 
	 单据s
	*/
	public Bills()
	{
	}

		///#endregion


		///#region 属性
	/** 
	 单据
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Bill();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Bill> ToJavaList()
	{
		return (List<Bill>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Bill> Tolist()
	{
		ArrayList<Bill> list = new ArrayList<Bill>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Bill)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}