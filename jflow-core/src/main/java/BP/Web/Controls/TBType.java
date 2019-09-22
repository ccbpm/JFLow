package BP.Web.Controls;

import BP.Web.*;

/** 
 TaxBox 类型
*/
public enum TBType
{
	/** 
	 Entities 的DataHelp, 如果这里说明了，他是Ens , 就要指明DataHelpKey. 
	 这样，系统就会在右键帮助会出现他。
	*/
	Ens,
	/** 
	 Entities 的DataHelp, 如果这里说明了，他是Ens , 就要指明DataHelpKey. 
	 这样，系统就会在右键帮助会出现他。
	 可能是需要多个值得选择问题。当选择多个值的时间，就用',' 把他们分开返回。 
	*/
	EnsOfMany,
	/** 
	 自定义的类型。
	*/
	Self,
	/** 
	 正常的
	*/
	TB,
	/** 
	 Num
	*/
	Num,
	/** 
	 Int
	*/
	Int,
	/** 
	 Float
	*/
	Float,
	/** 
	 Decimal
	*/
	Decimal,
	/** 
	 Moneny
	*/
	Moneny,
	/** 
	 Date
	*/
	Date,
	/** 
	 DateTime
	*/
	DateTime,
	/** 
	 Email
	*/
	Email,
	/** 
	 Key
	*/
	Key,
	Area;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TBType forValue(int value)
	{
		return values()[value];
	}
}