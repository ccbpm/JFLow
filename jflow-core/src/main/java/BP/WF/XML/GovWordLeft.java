package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 公文左边谓词
*/
public class GovWordLeft extends XmlEnNoName
{


		
	/** 
	 公文左边谓词
	*/
	public GovWordLeft()
	{
	}
	/** 
	 公文左边谓词s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GovWordLefts();
	}
}