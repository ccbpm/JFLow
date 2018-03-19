package cn.jflow.system.ui.core;

public enum TextBoxMode
{
    // 摘要:
    // 表示单行输入模式。
    SingleLine(0),
    //
    // 摘要:
    //     表示多行输入模式。
    MultiLine(1),
    //
    // 摘要:
    //     表示密码输入模式。
    Password(2),
    //
    //摘要:
    //		隐藏
    Hidden(3),
    //
    //摘要
    //	  文件
    Files(4);
	private int intValue;
	private static java.util.HashMap<Integer, TextBoxMode> mappings;
	private synchronized static java.util.HashMap<Integer, TextBoxMode> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, TextBoxMode>();
		}
		return mappings;
	}

	private TextBoxMode(int value)
	{
		intValue = value;
		TextBoxMode.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static TextBoxMode forValue(int value)
	{
		return getMappings().get(value);
	}

}
