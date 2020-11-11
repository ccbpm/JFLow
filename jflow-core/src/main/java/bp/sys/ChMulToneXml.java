package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.*;

/** 
 多音字
*/
public class ChMulToneXml extends XmlEn
{

		///属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	public final String getDesc()
	{
		return this.GetValStringByKey("No");
	}

		///


		///构造
	/** 
	 节点扩展信息
	*/
	public ChMulToneXml()
	{
	}
	/** 
	 获取一个实例s
	*/
	public XmlEns getGetNewEntities()
	{
		return new ChMulToneXmls();
	}

		///
}