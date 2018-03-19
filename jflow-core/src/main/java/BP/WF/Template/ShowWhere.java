package BP.WF.Template;


/** 
 显示位置
 
*/
public enum ShowWhere
{
	/** 
	 树
	 
	*/
	Tree,
	/** 
	 工具栏
	 
	*/
	Toolbar;

	public int getValue()
	{
		return this.ordinal();
	}

	public static ShowWhere forValue(int value)
	{
		return values()[value];
	}
}