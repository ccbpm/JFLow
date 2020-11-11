package bp.wf.rpt;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 报表岗位 
*/
public class RptStations extends Entities
{

		///构造
	/** 
	 报表与岗位集合
	*/
	public RptStations()
	{
	}

		///


		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RptStation();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<RptStation> ToJavaList()
	{
		return (List<RptStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<RptStation> Tolist()
	{
		ArrayList<RptStation> list = new ArrayList<RptStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptStation)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}