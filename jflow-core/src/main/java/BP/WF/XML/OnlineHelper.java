package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 在线帮助
*/
public class OnlineHelper extends XmlEnNoName
{
		
	/** 
	 在线帮助
	*/
	public OnlineHelper()
	{
	}
	/** 
	 在线帮助
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new OnlineHelpers();
	}
}