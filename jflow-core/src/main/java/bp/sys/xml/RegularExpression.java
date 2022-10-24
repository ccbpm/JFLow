package bp.sys.xml;

/** 
  RegularExpression 正则表达模版
*/
public class RegularExpression extends XmlEn
{

		///#region 属性
	/** 
	 编号
	*/
	public final String getNo()  throws Exception
	{
		return this.GetValStringByKey("No");
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
	public final String getForCtrl()  throws Exception
	{
		return this.GetValStringByKey("ForCtrl");
	}
	public final String getExp()  throws Exception
	{
		return this.GetValStringByKey("Exp");
	}

		///#endregion


		///#region 构造
	/** 
	 节点扩展信息
	*/
	public RegularExpression()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new RegularExpressions();
	}

		///#endregion
}