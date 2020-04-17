package BP.Frm;

import java.util.HashMap;

/** 
 单据状态
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

	public static final int SIZE = Integer.SIZE;

	private int intValue;
	private static HashMap<Integer, FrmActionType> mappings;
	private static HashMap<Integer, FrmActionType> getMappings()
	{
		if (mappings == null)
		{
			synchronized (FrmActionType.class)
			{
				if (mappings == null)
				{
					mappings = new HashMap<Integer, FrmActionType>();
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