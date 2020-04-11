package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import java.util.*;

/** 
 实体集合
*/
public class DictDtls extends EntitiesNo
{

		///#region 构造
	/** 
	 配置信息
	*/
	public DictDtls()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new DictDtl();
	}
    public DictDtls(String dictMyPK) throws Exception
    {
        this.Retrieve(DictDtlAttr.DictMyPK, dictMyPK);
    }
		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<DictDtl> ToJavaList()
	{
		return (List<DictDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DictDtl> Tolist()
	{
		ArrayList<DictDtl> list = new ArrayList<DictDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DictDtl)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}