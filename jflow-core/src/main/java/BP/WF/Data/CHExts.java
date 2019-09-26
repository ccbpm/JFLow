package BP.WF.Data;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 时效考核s
*/
public class CHExts extends Entities
{

		///#region 构造方法属性
	/** 
	 时效考核s
	*/
	public CHExts()
	{
	}

		///#endregion


		///#region 属性
	/** 
	 时效考核
	*/
	@Override
	public Entity getNewEntity()
	{
		return new CHExt();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CHExt> ToJavaList()
	{
		return (List<CHExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CHExt> Tolist()
	{
		ArrayList<CHExt> list = new ArrayList<CHExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CHExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}