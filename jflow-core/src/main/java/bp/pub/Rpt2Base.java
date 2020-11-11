package bp.pub;

/** 
 报表基类
*/
public abstract class Rpt2Base
{

		///构造方法
	/** 
	 报表基类
	*/
	public Rpt2Base()
	{
	}

		/// 构造方法


		///要求子类强制重写的属性.
	/** 
	 显示的标题.
	*/
	public abstract String getTitle();
	/** 
	 默认选择的属性.
	*/
	public abstract int getAttrDefSelected();
	/** 
	 分组显示属性, 多个属性用@符号隔开.
	*/
	public abstract Rpt2Attrs getAttrsOfGroup();

		/// 要求子类重写的属性.


		///提供给操作者的方法.

		/// 提供给操作者的方法
}