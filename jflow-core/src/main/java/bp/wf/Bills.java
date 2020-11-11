package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.web.*;
import bp.sys.*;
import java.util.*;

/** 
 单据s
*/
public class Bills extends EntitiesMyPK
{

		///构造方法属性
	/** 
	 单据s
	*/
	public Bills()
	{
	}

		///


		///属性
	/** 
	 单据
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Bill();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}