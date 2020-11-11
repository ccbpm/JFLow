package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 挂起
*/
public class HungUps extends EntitiesMyPK
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new HungUp();
	}
	/** 
	 挂起
	*/
	public HungUps()
	{
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<HungUp> ToJavaList()
	{
		return (List<HungUp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<HungUp> Tolist()
	{
		ArrayList<HungUp> list = new ArrayList<HungUp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((HungUp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}