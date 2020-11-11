package bp.sys.xml;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;

/** 
  RegularExpression 正则表达模版
*/
public class RegularExpression extends XmlEn
{

		///属性
	/** 
	 编号
	*/
	public final String getNo()
	{
		return this.GetValStringByKey("No");
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
	public final String getForCtrl()
	{
		return this.GetValStringByKey("ForCtrl");
	}
	public final String getExp()
	{
		return this.GetValStringByKey("Exp");
	}

		///


		///构造
	/** 
	 节点扩展信息
	*/
	public RegularExpression()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new RegularExpressions();
	}

		///
}