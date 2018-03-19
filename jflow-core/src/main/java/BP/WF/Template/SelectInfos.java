package BP.WF.Template;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 接受人信息
*/
public class SelectInfos extends EntitiesMyPK
{
	/** 
	 接受人信息
	*/
	public SelectInfos()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SelectInfo();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SelectInfo> ToJavaList()
	{
		return (List<SelectInfo>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SelectInfo> Tolist()
	{
		ArrayList<SelectInfo> list = new ArrayList<SelectInfo>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SelectInfo)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}