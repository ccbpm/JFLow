package bp.dts;


/** 
 属性
*/
public class FF
{
	/** 
	 从字段
	*/
	public String FromField = null;
	/** 
	 到字段
	*/
	public String ToField = null;
	/** 
	 数据源类型
	*/
	public int DataType = 1; //DataType.AppString;
	/** 
	 是否是主键
	*/
	public boolean IsPK = false;
	public FF()throws Exception
	{
	}
	/** 
	 构造
	 
	 param from 从
	 param to 到
	 param datatype 数据类型
	 param isPk 是否PK
	*/
	public FF(String from, String to, int datatype, boolean isPk)
	{
		this.FromField = from;
		this.ToField = to;
		this.DataType = datatype;
		this.IsPK = isPk;
	}
}