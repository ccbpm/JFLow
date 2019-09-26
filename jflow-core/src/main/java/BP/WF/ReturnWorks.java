package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import java.util.*;

/** 
 退回轨迹s 
*/
public class ReturnWorks extends Entities
{

		///#region 构造
	/** 
	 退回轨迹s
	*/
	public ReturnWorks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ReturnWork();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ReturnWork> ToJavaList()
	{
		return (List<ReturnWork>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ReturnWork> Tolist()
	{
		ArrayList<ReturnWork> list = new ArrayList<ReturnWork>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ReturnWork)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}