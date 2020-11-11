package bp.sys.xml;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;

/** 
  RegularExpressionDtl 正则表达模版
*/
public class RegularExpressionDtl extends XmlEn
{

		///属性
	/** 
	 编号
	*/
	public final String getItemNo()
	{
		return this.GetValStringByKey("ItemNo");
	}
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	public final String getNote()
	{
		return this.GetValStringByKey("Note");
	}
	public final String getExp()
	{
		return this.GetValStringByKey("Exp");
	}
	public final String getForEvent()
	{
		return this.GetValStringByKey("ForEvent");
	}
	public final String getMsg() throws Exception
	{
		return this.GetValStringByKey("Msg");
	}

		///


		///构造
	/** 
	 节点扩展信息
	*/
	public RegularExpressionDtl()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new RegularExpressionDtls();
	}

		///
}