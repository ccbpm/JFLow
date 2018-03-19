package BP.WF.Template;

/** 
 流程数据存储模式
 
*/
public enum DataStoreModel
{
	/** 
	 存储在CCFlow数据表里
	 
	*/
	ByCCFlow,
	/** 
	 指定的业务主表
	 
	*/
	SpecTable;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DataStoreModel forValue(int value)
	{
		return values()[value];
	}
}