package BP.WF.Template;


/** 
 轨迹图标组件控件状态
 
*/
public enum FrmTrackSta
{
	/** 
	 不可用
	 
	*/
	Disable,
	/** 
	 显示轨迹图
	 
	*/
	Chart,
	/** 
	 显示轨迹表
	 
	*/
	Table;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmTrackSta forValue(int value)
	{
		return values()[value];
	}
}