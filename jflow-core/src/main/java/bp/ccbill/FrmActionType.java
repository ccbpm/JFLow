package bp.ccbill;

import bp.ccbill.template.*;

/** 
 表单活动类型
*/
public enum FrmActionType
{
	/** 
	 创建
	*/
	Create(0),
	/** 
	 保存
	*/
	Save(1),
	/** 
	 评论
	*/
	BBS(2),
	/** 
	 打开
	*/
	View(3);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, FrmActionType> mappings;
	private static java.util.HashMap<Integer, FrmActionType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (FrmActionType.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, FrmActionType>();
				}
			}
		}
		return mappings;
	}

	private FrmActionType(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static FrmActionType forValue(int value)
	{
		return getMappings().get(value);
	}
}