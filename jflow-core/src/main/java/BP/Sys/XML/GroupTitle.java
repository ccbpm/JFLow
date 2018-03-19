package BP.Sys.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * GroupTitle 的摘要说明。 考核过错行为的数据元素 1，它是 GroupTitle 的一个明细。 2，它表示一个数据元素。
 */
public class GroupTitle extends XmlEn
{
	// 属性
	/**
	 * 选择这个属性时间需要的条件
	 */
	public final String getTitle()
	{
		return this.GetValStringByKey(GroupTitleAttr.Title);
	}
	
	public final String getFor()
	{
		return this.GetValStringByKey(GroupTitleAttr.For);
	}
	
	public final String getKey()
	{
		return this.GetValStringByKey(GroupTitleAttr.Key);
	}
	
	// 构造
	public GroupTitle()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GroupTitles();
	}
}