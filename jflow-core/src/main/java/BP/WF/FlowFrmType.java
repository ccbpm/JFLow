package BP.WF;

public enum FlowFrmType
{
	/**
	 完整版-2019年更早版本
	*/
	Ver2019Earlier,
	/**
	 开发者表单
	*/
	DeveloperFrm,
	/**
	 傻瓜表单
	*/
	FoolFrm,
	/**
	 自定义表单
	*/
	SelfFrm,
	/**
	 * SDK表单
	 */
	SDKFrm;

	public static final int SIZE = Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowFrmType forValue(int value)
	{
		return values()[value];
	}
}