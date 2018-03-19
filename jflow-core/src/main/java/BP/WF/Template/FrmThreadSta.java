package BP.WF.Template;


/** 
 子线程组件控件状态
 
*/
public enum FrmThreadSta
{
	/** 
	 不可用
	 
	*/
	Disable,
	/** 
	 启用
	 
	*/
	Enable;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmThreadSta forValue(int value)
	{
		return values()[value];
	}
}