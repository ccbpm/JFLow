package BP.Rpt;

/**
 * 报表基类
 */
public abstract class Rpt2Base
{
	/**
	 * 报表基类
	 */
	public Rpt2Base()
	{
	}
	
	/**
	 * 显示的标题.
	 */
	public abstract String getTitle();
	
	/**
	 * 默认选择的属性.
	 */
	public abstract int getAttrDefSelected();
	
	/**
	 * 分组显示属性, 多个属性用@符号隔开.
	 */
	public abstract Rpt2Attrs getAttrsOfGroup();
}
