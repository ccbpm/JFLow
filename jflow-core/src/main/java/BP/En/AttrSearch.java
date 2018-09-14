package BP.En;

/**
 * 查询属性
 */
public class AttrSearch
{
	/**
	 * 查询属性
	 */
	public Attr HisAttr = null;
	/**
	 * 是否显示全部
	 */
	public boolean IsShowAll = true;
	/**
	 * 及联子菜单
	 */
	public String RelationalDtlKey = null;
	public String Key = null;
	public int Width=130; //宽度
	public AttrSearch()
	{
	}
	
	public String reationChildKey = null;
	public String reationGrandChildKey = null;
	public String reationParentKey = null;
}