package bp.da;

import bp.*;

/** 
 时间计算方式
*/
public enum FieldCaseModel
{
	/// <summary>
    /// 无
    /// </summary>
    None,
    /// <summary>
    /// 大写
    /// </summary>
    UpperCase,
    /// <summary>
    /// 小写
    /// </summary>
    Lowercase;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FieldCaseModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}