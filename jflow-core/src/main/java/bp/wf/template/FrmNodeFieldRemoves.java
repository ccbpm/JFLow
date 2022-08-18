package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 表单节点排除规则s
*/
public class FrmNodeFieldRemoves extends EntitiesMyPK
{

		///#region 构造方法..
	/** 
	 表单节点排除规则
	*/
	public FrmNodeFieldRemoves() throws Exception {
	}

		///#endregion 构造方法..


		///#region 公共方法.
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new FrmNodeFieldRemove();
	}

		///#endregion 公共方法.


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmNodeFieldRemove> ToJavaList() {
		return (java.util.List<FrmNodeFieldRemove>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNodeFieldRemove> Tolist()  {
		ArrayList<FrmNodeFieldRemove> list = new ArrayList<FrmNodeFieldRemove>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNodeFieldRemove)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}