package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 节点表单s
*/
public class FrmNodeExts extends EntitiesMyPK
{

		///构造方法..
	/** 
	 节点表单
	*/
	public FrmNodeExts()
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
		return new FrmNodeExt();
	}

		/// 公共方法.


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmNodeExt> ToJavaList()
	{
		return (List<FrmNodeExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNodeExt> Tolist()
	{
		ArrayList<FrmNodeExt> list = new ArrayList<FrmNodeExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNodeExt)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}