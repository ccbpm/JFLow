package bp.sys;

import bp.en.*;

import java.util.*;

/** 
 系统字典表s
*/
public class DictDtls extends EntitiesMyPK
{

		///#region 构造
	/** 
	 系统字典表s
	*/
	public DictDtls()throws Exception
	{
	}

	public DictDtls(String fk_sftable) throws Exception {
		this.Retrieve(DictDtlAttr.FK_SFTable, fk_sftable);
	}


	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DictDtl();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<DictDtl> ToJavaList()throws Exception
	{
		return (java.util.List<DictDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DictDtl> Tolist()throws Exception
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