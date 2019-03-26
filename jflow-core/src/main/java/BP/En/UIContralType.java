package BP.En;

/**
 * 控件类型
 */
public enum UIContralType
{
	/**
	 * 文本框
	 */
	TB(0),
	/**
	 * 下拉框
	 */
	DDL(1),
	/**
	 * CheckBok
	 */
	CheckBok(2),
	/**
	 * 单选择按钮
	 */
	RadioBtn(3),
	/**
	 * 地图定位
	 */
	MapPin(4),
	/**
	 * 录音控件
	 */
	 MicHot(5),
	 /**
	  * 附件展示控件
	  */
	 AthShow(6),
	 /**
	  * 拍照控件
	  */
	 Picture(7),
	 /**
	  * 手写签名版
	  */
	 HandWriting(8),
	/**
	 * 超链接
	 */
     HyperLink(9);
	
	private int intValue;
	private static java.util.HashMap<Integer, UIContralType> mappings;
	
	private synchronized static java.util.HashMap<Integer, UIContralType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, UIContralType>();
		}
		return mappings;
	}
	
	private UIContralType(int value)
	{
		intValue = value;
		UIContralType.getMappings().put(value, this);
	}
	
	public int getValue()
	{
		return intValue;
	}
	
	public static UIContralType forValue(int value)
	{
		return getMappings().get(value);
	}
}