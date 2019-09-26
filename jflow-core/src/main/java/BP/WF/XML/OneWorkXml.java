package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.Sys.Plant;
import BP.WF.*;

/** 
 工作一户式
*/
public class OneWorkXml extends XmlEnNoName
{

		///#region 属性.
 
	public final String getURL()
	{
		 
			return this.GetValStringByKey("Url"+BP.WF.Glo.Plant);
		 
	}

	public final String getIsDefault()
	{
		return this.GetValStringByKey("IsDefault");
	}



		///#endregion 属性.


		///#region 构造
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

		///#endregion
}