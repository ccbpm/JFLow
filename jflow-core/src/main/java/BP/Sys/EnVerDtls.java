package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 部门岗位对应 
*/
public class EnVerDtls extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造

	public EnVerDtls()
	{
	}

	public EnVerDtls(String enVerPK)
	{
		this.Retrieve(EnVerDtlAttr.EnVerPK, enVerPK);
	}

	public EnVerDtls(String enVerPK, int ver)
	{
		this.Retrieve(EnVerDtlAttr.EnVerPK, enVerPK, EnVerDtlAttr.EnVer, ver);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new EnVerDtl();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EnVerDtl> ToJavaList()
	{
		return (List<EnVerDtl>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EnVerDtl> Tolist()
	{
		ArrayList<EnVerDtl> list = new ArrayList<EnVerDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EnVerDtl)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}