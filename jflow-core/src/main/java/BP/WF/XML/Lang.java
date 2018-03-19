package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 语言
*/
public class Lang extends XmlEnNoName
{

		
	/** 
	 节点扩展信息
	*/
	public Lang()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new Langs();
	}
}