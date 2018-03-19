package BP.WF;

/** 
 当没有找到处理人时
*/
public enum WhenNoWorker
{
	 /** 
	 提示错误
	 */
	AlertErr,
	/** 
	 跳转到下一步
	*/
	Skip;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WhenNoWorker forValue(int value)
	{
		return values()[value];
	}
}