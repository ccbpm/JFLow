package bp.da;

/** 
 字段
*/
public class KeyVal
{
	/** 
	 字段名称
	*/
	private String key;
	public final String getkey()
	{
		return key;
	}
	public final void setkey(String value) throws Exception
	{
		key = value;
	}
	/** 
	 字段值
	*/
	private String value;
	public final String getvalue()
	{
		return value;
	}
	public final void setvalue(String value) throws Exception
	{
		value = value;
	}
	/** 
	 类型
	*/
	private String type;
	public final String gettype()
	{
		return type;
	}
	public final void settype(String value) throws Exception
	{
		type = value;
	}
}