package BP.WF.Template;


/** 
 公文工作模式
 
*/
public enum WebOfficeWorkModel
{
	/** 
	 不启用
	 
	*/
	None,
	/** 
	 按钮方式启用
	 
	*/
	Button,
	/** 
	 表单在前
	 
	*/
	FrmFirst,
	/** 
	 文件在前
	 
	*/
	WordFirst;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WebOfficeWorkModel forValue(int value)
	{
		return values()[value];
	}
}