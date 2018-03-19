package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 公文右边谓词
 
*/
public class GovWordRight extends XmlEnNoName
{

		
	/** 
	 公文右边谓词
	*/
	public GovWordRight()
	{
	}
	/** 
	 公文右边谓词s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GovWordRights();
	}
}