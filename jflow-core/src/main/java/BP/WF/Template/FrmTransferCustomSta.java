package BP.WF.Template;

/** 
轨迹图标组件控件状态
*/
public enum FrmTransferCustomSta
{
	/** 
	 不可用
	*/
	Disable,
	/** 
	 只读
	*/
	ReadOnly,
	/** 
	 可以设置人员
	*/
	SetWorkers;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmTransferCustomSta forValue(int value)
	{
		return values()[value];
	}
}

