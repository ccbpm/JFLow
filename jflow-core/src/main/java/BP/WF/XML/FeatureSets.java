package BP.WF.XML;

import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 个性化设置s
*/
public class FeatureSets extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public FeatureSets()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FeatureSet();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "FeatureSet.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Item";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<FeatureSet> ToJavaList()
	{
		return (List<FeatureSet>)(Object)this;
	}
}