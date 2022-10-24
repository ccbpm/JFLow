package bp.ccbill.template;

import bp.en.*;
import java.util.*;

/** 
 台账子流程
*/
public class DictFlows extends EntitiesMyPK
{
	/** 
	 台账子流程
	*/
	public DictFlows() throws Exception {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new DictFlow();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<DictFlow> ToJavaList() {
		return (java.util.List<DictFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DictFlow> Tolist()  {
		ArrayList<DictFlow> list = new ArrayList<DictFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DictFlow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}