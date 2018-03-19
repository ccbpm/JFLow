package BP.En;

import java.util.ArrayList;

/**
 * row 集合
 */
public class Rows extends ArrayList<Row>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Rows()
	{
	}
	
	public final Row getItem(int index)
	{
		return (Row) this.get(index);
		/*
		 * warning return (Row)this.get(index);
		 */
	}
	
	/**
	 * 增加一个Row .
	 * 
	 * @param r
	 *            row
	 */
	public final void Add(Row r)
	{
		this.add(r);
		/*
		 * warning this.add(r);
		 */
	}
}