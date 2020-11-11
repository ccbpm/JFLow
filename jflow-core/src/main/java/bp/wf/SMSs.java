package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.port.*;
import java.util.*;

/** 
 消息s
*/
public class SMSs extends Entities
{
	/** 
	 获得实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SMS();
	}
	public SMSs()
	{
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SMS> ToJavaList()
	{
		return (List<SMS>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SMS> Tolist()
	{
		ArrayList<SMS> list = new ArrayList<SMS>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SMS)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}