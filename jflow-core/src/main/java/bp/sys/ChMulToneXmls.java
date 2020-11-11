package bp.sys;

import java.util.List;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;

/** 
 多音字s
*/
public class ChMulToneXmls extends XmlEns
{

	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 多音字s
	*/
	public ChMulToneXmls()
	{
	}

		///


		///重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ChMulToneXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "/XML/XmlDB.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "PinYin";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}

	public final List<ChMulToneXml> ToJavaList()
	{
		return (List<ChMulToneXml>)(Object)this;
	}
}