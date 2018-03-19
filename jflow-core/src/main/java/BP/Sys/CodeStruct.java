package BP.Sys;



/** 
 编码表类型
 
*/
public enum CodeStruct
{
	/** 
	 普通的编码表
	 
	*/
	NoName,
	/** 
	 树编码表(No,Name,ParentNo)
	 
	*/
	Tree,
	/** 
	 行政机构编码表
	 
	*/
	GradeNoName;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CodeStruct forValue(int value)
	{
		return values()[value];
	}
}