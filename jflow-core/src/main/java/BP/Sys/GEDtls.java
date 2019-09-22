package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.math.*;

/** 
 通用从表s
*/
public class GEDtls extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载基类方法
	/** 
	 节点ID
	*/
	public String FK_MapDtl = null;
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
		if (this.FK_MapDtl == null)
		{
			return new GEDtl();
		}
		return new GEDtl(this.FK_MapDtl);
	}
	/** 
	 通用从表ID
	*/
	public GEDtls()
	{
	}
	/** 
	 通用从表ID
	 
	 @param fk_mapdtl
	*/
	public GEDtls(String fk_mapdtl)
	{
		this.FK_MapDtl = fk_mapdtl;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GEDtl> ToJavaList()
	{
		return (List<GEDtl>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEDtl> Tolist()
	{
		ArrayList<GEDtl> list = new ArrayList<GEDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEDtl)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}