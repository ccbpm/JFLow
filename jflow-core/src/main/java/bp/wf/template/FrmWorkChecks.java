package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;

/** 
 审核组件s
*/
public class FrmWorkChecks extends Entities
{

		///构造
	/** 
	 审核组件s
	*/
	public FrmWorkChecks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmWorkCheck();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmWorkCheck> ToJavaList()
	{
		return (List<FrmWorkCheck>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmWorkCheck> Tolist()
	{
		ArrayList<FrmWorkCheck> list = new ArrayList<FrmWorkCheck>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmWorkCheck)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}