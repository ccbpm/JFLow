package bp.ccbill.template;

import bp.en.*;
import java.util.*;

/** 
 控制模型集合s
*/
public class CtrlModelDtls extends EntitiesMyPK
{
	private static final long serialVersionUID = 1L;
		///构造方法.
	/** 
	 控制模型集合
	*/
	public CtrlModelDtls()
	{
	}
	@Override
	public Entity getGetNewEntity()
	{
		return new CtrlModelDtl();
	}

		/// 构造方法.


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CtrlModelDtl> ToJavaList()
	{
		return (List<CtrlModelDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CtrlModelDtl> Tolist()
	{
		ArrayList<CtrlModelDtl> list = new ArrayList<CtrlModelDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CtrlModelDtl)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}