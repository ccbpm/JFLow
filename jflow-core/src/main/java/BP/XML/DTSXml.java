package BP.XML;

import BP.En.ClassFactory;

public class DTSXml extends BP.DTS.DataIOEn
{
	public DTSXml()
	{
		this.Title = "把xml数据调度到物理表中";
		// this.HisDoType =BP.DTS.DoType.
		// this.d
	}
	
	@Override
	public void Do()
	{
		java.util.ArrayList al = ClassFactory.GetObjects("BP.XML.XmlEns");
		for (Object obj : al)
		{
			BP.XML.XmlEns en = (BP.XML.XmlEns) obj;
			if (en.getRefEns() == null)
			{
				continue;
			}
			
			en.FillXmlDataIntoEntities(en.getRefEns());
		}
	}
}