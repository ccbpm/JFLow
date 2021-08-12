package bp.gpm.home.windowext;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesMyPK;
import bp.en.Entity;

/** 
变量信息s
*/
public class Dtls extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造
	/** 
	 变量信息s
	*/
	public Dtls()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Dtl();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Dtl> ToJavaList()
	{
		return (List<Dtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Dtl> Tolist()
	{
		ArrayList<Dtl> list = new ArrayList<Dtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Dtl)this.get(i));

		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}

