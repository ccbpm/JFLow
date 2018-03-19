package BP.En;

import java.util.ArrayList;

public class AARefs extends ArrayList<Object>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	public AARefs()
	{
	}
	
	public final AARefs getItem(int index)
	{
		return (AARefs) this.get(index);
		/*
		 * warning return (AARefs)this.get(index);
		 */
	}
	
	// 增加一个查询属性。
	/**
	 * 增加一个查询属性
	 * 
	 * @param lab
	 *            标签
	 * @param refKey
	 *            实体的属性
	 * @param defaultvalue
	 *            默认值
	 */
	public final void Add(String lab, String key, String refKey,
			String defaultSymbol, String defaultvalue, int tbWidth)
	{
		AttrOfSearch aos = new AttrOfSearch(key, lab, refKey, defaultSymbol,
				defaultvalue, tbWidth, false);
		this.add(aos);
		/*
		 * warning this.add(aos);
		 */
	}
}