package BP.Sys;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/**
 * EnsAppXml 的摘要说明，属性的配置。
 */
public class EnsAppXml extends XmlEnNoName
{
	// 属性
	/**
	 * 枚举值
	 */
	public final String getEnumKey()
	{
		return this.GetValStringByKey(EnsAppXmlEnsName.EnumKey);
	}
	
	public final String getEnumVals()
	{
		return this.GetValStringByKey(EnsAppXmlEnsName.EnumVals);
	}
	
	/**
	 * 类名
	 */
	public final String getEnsName()
	{
		return this.GetValStringByKey(EnsAppXmlEnsName.EnsName);
	}
	
	/**
	 * 数据类型
	 */
	public final String getDBType()
	{
		return this.GetValStringByKey(EnsAppXmlEnsName.DBType);
	}
	
	/**
	 * 描述
	 */
	public final String getDesc()
	{
		return this.GetValStringByKey(EnsAppXmlEnsName.Desc);
	}
	
	/**
	 * 默认值
	 */
	public final String getDefVal()
	{
		return this.GetValStringByKey(EnsAppXmlEnsName.DefVal);
	}
	
	public final boolean getDefValBoolen()
	{
		return this.GetValBoolByKey(EnsAppXmlEnsName.DefVal);
	}
	
	public final int getDefValInt()
	{
		return this.GetValIntByKey(EnsAppXmlEnsName.DefVal);
	}
	
	// 构造
	public EnsAppXml()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EnsAppXmls();
	}
}