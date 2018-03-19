package BP.En;

/**
 * 字段类型
 */
public enum FieldType
{
	/**
	 * 正常的
	 */
	Normal,
	/**
	 * 主键
	 */
	PK,
	/**
	 * 外键
	 */
	FK,
	/**
	 * 枚举
	 */
	Enum,
	/**
	 * 既是主键又是外键
	 */
	PKFK,
	/**
	 * 既是主键又是枚举
	 */
	PKEnum,
	/**
	 * 关连的文本.
	 */
	RefText,
	/**
	 * 虚拟的
	 */
	NormalVirtual,
	/**
	 * 多值的
	 */
	MultiValues,
	
	/*
	 * 绑定其它数据表
	 */
	BindTable;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static FieldType forValue(int value)
	{
		return values()[value];
	}
}