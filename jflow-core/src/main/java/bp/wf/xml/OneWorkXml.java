package bp.wf.xml;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEnNoName;
import bp.sys.xml.XmlEns;
import bp.web.WebUser;
import bp.sys.*;
import bp.wf.*;
import bp.wf.Plant;

/** 
 工作一户式
*/
public class OneWorkXml extends XmlEnNoName
{

		///属性.
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	public final String getURL()
	{
		if (bp.wf.Glo.Plant == Plant.CCFlow)
		{
			return this.GetValStringByKey("UrlCCFlow");
		}
		return this.GetValStringByKey("UrlJFlow");
	}

	public final String getIsDefault()
	{
		return this.GetValStringByKey("IsDefault");
	}



		/// 属性.


		///构造
	/** 
	 节点扩展信息
	*/
	public OneWorkXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new OneWorkXmls();
	}

		///
}