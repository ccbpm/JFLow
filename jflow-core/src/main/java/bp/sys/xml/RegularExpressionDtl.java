package bp.sys.xml;


/** 
  RegularExpressionDtl 正则表达模版
*/
public class RegularExpressionDtl extends XmlEn
{

		///#region 属性
	/** 
	 编号
	*/
	public final String getItemNo()  throws Exception
	{
		return this.GetValStringByKey("ItemNo");
	}
	/** 
	 名称
	*/
	public final String getName()  throws Exception
	{
		return this.GetValStringByKey("Name");
	}
	public final String getNote()  throws Exception
	{
		return this.GetValStringByKey("Note");
	}
	public final String getExp()  throws Exception
	{
		return this.GetValStringByKey("Exp");
	}
	public final String getForEvent()  throws Exception
	{
		return this.GetValStringByKey("ForEvent");
	}
	public final String getMsg()  throws Exception
	{
		return this.GetValStringByKey("Msg");
	}

		///#endregion


		///#region 构造
	/** 
	 节点扩展信息
	*/
	public RegularExpressionDtl()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new RegularExpressionDtls();
	}

		///#endregion
}