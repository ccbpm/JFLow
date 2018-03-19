package BP.Sys;

public enum ActionTypeS
{
	// / <summary>
	// / 不做任何事情
	// / </summary>
	None,
	// / <summary>
	// / 删除文件
	// / </summary>
	DeleteFile,
	// / <summary>
	// / 打印只有一个实体的单据
	// / </summary>
	PrintEnBill;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static ActionTypeS forValue(int value)
	{
		return values()[value];
	}
}
