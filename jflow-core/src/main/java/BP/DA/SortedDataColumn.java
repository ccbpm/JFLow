package BP.DA;

/**
 * 此类描述的是： 用于保存排序字段信息的对象
 * 
 * @author: caoyong
 * @version: 2.0
 */
public class SortedDataColumn
{
	private DataColumn column;
	
	private SortType sortType;
	
	/**
	 * @param column
	 */
	public void setColumn(DataColumn column)
	{
		this.column = column;
	}
	
	/**
	 * @return the column
	 */
	public DataColumn getColumn()
	{
		return column;
	}
	
	/**
	 * @param sortType
	 */
	public void setSortType(SortType sortType)
	{
		this.sortType = sortType;
	}
	
	/**
	 * @return the sortType
	 */
	public SortType getSortType()
	{
		return sortType;
	}
	
}
