package bp.wf.data;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesOIDName;
import bp.en.Entity;

public class AutoRptDtls extends EntitiesOIDName {
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	  ///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new AutoRptDtl();
	}
	/** 
	 自动报表
	*/
//C# TO JAVA CONVERTER WARNING: The following constructor is declared outside of its associated class:
//ORIGINAL LINE: public AutoRptDtls()
	public AutoRptDtls()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<AutoRptDtl> ToJavaList()
	{
		return (List<AutoRptDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<AutoRptDtl> Tolist()
	{
		ArrayList<AutoRptDtl> list = new ArrayList<AutoRptDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((AutoRptDtl)this.get(i));
		}
		return list;
	}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}
