package BP.En;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询属性s
 */
public class AttrSearchs extends ArrayList<AttrSearch>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AttrSearchs()
	{
	}
	
	public final void Add(Attr attr, boolean isShowSelectedAll,
			String relationalDtlKey)
	{
		AttrSearch en = new AttrSearch();
		en.HisAttr = attr;
		en.IsShowAll = isShowSelectedAll;
		en.RelationalDtlKey = relationalDtlKey;
		en.Key = attr.getKey();
		this.add(en);
		/*
		 * warning en.setKey(attr.getKey()); this.add(en);
		 */
	}
	
	public final void addChild(Attr attr, boolean isShowSelectedAll,
			String reationChildKey)
	{
		AttrSearch en = new AttrSearch();
		en.HisAttr = attr;
		en.IsShowAll = isShowSelectedAll;
		en.reationChildKey = reationChildKey;
		en.Key = attr.getKey();
		this.add(en);
	}
	
	public final void addGrandChild(Attr attr, boolean isShowSelectedAll,
			String reationChildKey, String reationGrandChildKey)
	{
		AttrSearch en = new AttrSearch();
		en.HisAttr = attr;
		en.IsShowAll = isShowSelectedAll;
		en.reationChildKey = reationChildKey;
		en.reationGrandChildKey = reationGrandChildKey;
		en.Key = attr.getKey();
		this.add(en);
	}
	
	public final void addParentAndChild(Attr attr, boolean isShowSelectedAll,
			String reationParentKey, String reationChildKey)
	{
		AttrSearch en = new AttrSearch();
		en.HisAttr = attr;
		en.IsShowAll = isShowSelectedAll;
		en.reationParentKey = reationParentKey;
		en.reationChildKey = reationChildKey;
		en.Key = attr.getKey();
		this.add(en);
	}
	
	public final void addParent(Attr attr, boolean isShowSelectedAll,
			String reationParentKey)
	{
		AttrSearch en = new AttrSearch();
		en.HisAttr = attr;
		en.IsShowAll = isShowSelectedAll;
		en.reationParentKey = reationParentKey;
		en.Key = attr.getKey();
		this.add(en);
	}
	
	public final void Add(AttrSearch attr)
	{
		this.add(attr);
		/*
		 * warning this.add(attr);
		 */
	}
	
	public List<AttrSearch> toList(){
		return (List)(Object)this;
	}
}