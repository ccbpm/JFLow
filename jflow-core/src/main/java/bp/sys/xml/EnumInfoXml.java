package bp.sys.xml;
import bp.web.WebUser;
/** 
 EnumInfoXml 的摘要说明，属性的配置。
*/
public class EnumInfoXml extends XmlEn
{

		///属性
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	public final String getKey()
	{
		return this.GetValStringByKey("Key");
	}
	/** 
	 Vals
	*/
	public final String getVals()
	{
		String str = WebUser.getSysLang();
		str = "CH";
		return this.GetValStringByKey(str);
	}

		///


		///构造
	public EnumInfoXml()
	{
	}
	public EnumInfoXml(String key) throws Exception
	{
		this.RetrieveByPK("Key", key);
	}

	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EnumInfoXmls();
	}

		///
}