package bp.wf.xml;

import java.util.List;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.sys.*;
import bp.wf.*;

/** 
 工作一户式s
*/
public class OneWorkXmls extends XmlEns
{

		///构造
	/** 
	 工作一户式s
	*/
	public OneWorkXmls()
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
		return new OneWorkXml();
	}
	/** 
	 文件
	*/
	@Override
	public String getFile()
	{
		//return SystemConfig.getPathOfData() + "Xml/WFAdmin.xml";
		return SystemConfig.getPathOfDataUser() + "XML/OneWork.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "OneWork";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public final List<OneWorkXml> ToJavaList()
	{
		return (List<OneWorkXml>)(Object)this;
	}
}