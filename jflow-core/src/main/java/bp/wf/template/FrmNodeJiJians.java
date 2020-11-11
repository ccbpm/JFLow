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
public class FrmNodeJiJians extends EntitiesMyPK
{

		///构造方法..
	/** 
	 节点表单
	*/
	public FrmNodeJiJians()
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
		return new FrmNodeJiJian();
	}

		/// 公共方法.


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmNodeJiJian> ToJavaList()
	{
		return (List<FrmNodeJiJian>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNodeJiJian> Tolist()
	{
		ArrayList<FrmNodeJiJian> list = new ArrayList<FrmNodeJiJian>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNodeJiJian)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}