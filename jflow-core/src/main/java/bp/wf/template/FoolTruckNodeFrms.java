package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 累加表单方案s
*/
public class FoolTruckNodeFrms extends EntitiesMyPK
{

		///构造方法..
	/** 
	 累加表单方案
	*/
	public FoolTruckNodeFrms()
	{
	}

		/// 构造方法..


		///公共方法.
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FoolTruckNodeFrm();
	}

		/// 公共方法.


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FoolTruckNodeFrm> ToJavaList()
	{
		return (List<FoolTruckNodeFrm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FoolTruckNodeFrm> Tolist()
	{
		ArrayList<FoolTruckNodeFrm> list = new ArrayList<FoolTruckNodeFrm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FoolTruckNodeFrm)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}