package bp.wf.data;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import java.util.*;

/** 
 我发起的流程s
*/
public class MyAuthtos extends Entities
{
	   
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyAuthto();
	}
	/** 
	 我发起的流程集合
	*/
	public MyAuthtos()
	{
	}
 
		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyAuthto> ToJavaList()
	{
		return (List<MyAuthto>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyAuthto> Tolist()
	{
		ArrayList<MyAuthto> list = new ArrayList<MyAuthto>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyAuthto)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}