package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 消息类型
*/
public class MsgType extends XmlEnNoName
{

		
	/** 
	 节点扩展信息
	*/
	public MsgType()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new MsgTypes();
	}
}