package BP.WF.XML;

import java.io.File;
import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 工作一户式s
*/
public class OneWorkXmls extends XmlEns
{

		
	/** 
	 工作一户式s
	*/
	public OneWorkXmls()
	{
	}
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
		System.out.println("Xml"+File.separator+"WFAdmin.xml");
		return SystemConfig.getPathOfData() + "Xml"+File.separator+"WFAdmin.xml";
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
	public List<OneWorkXml> ToJavaListXmlEnss()
	{
		return (List<OneWorkXml>)(Object)this;
	}
}