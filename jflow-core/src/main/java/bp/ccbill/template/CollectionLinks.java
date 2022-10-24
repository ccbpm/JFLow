package bp.ccbill.template;

import bp.en.*;
import java.util.*;

/** 
 连接方法
*/
public class CollectionLinks extends EntitiesNoName
{
	/** 
	 连接方法
	*/
	public CollectionLinks() throws Exception {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()  {
		return new CollectionLink();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<CollectionLink> ToJavaList() {
		return (java.util.List<CollectionLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CollectionLink> Tolist()  {
		ArrayList<CollectionLink> list = new ArrayList<CollectionLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CollectionLink)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}