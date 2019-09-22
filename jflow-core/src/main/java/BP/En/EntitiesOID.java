package BP.En;

import BP.DA.*;

public abstract class EntitiesOID extends Entities
{
	/** 
	 构造
	*/
	public EntitiesOID()
	{

	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询方法, 专用于与语言有关的实体
	/** 
	 查询出来, 所有中文的实例 . 
	*/
	public final void RetrieveAllCNEntities()
	{
		this.RetrieveByLanguageNo("CH");
	}
	/** 
	 按语言查询。 
	*/
	public final void RetrieveByLanguageNo(String LanguageNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere("LanguageNo", LanguageNo);
		qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}