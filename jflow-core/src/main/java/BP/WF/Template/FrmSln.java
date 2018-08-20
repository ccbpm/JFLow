package BP.WF.Template;

public enum FrmSln {

	Default,
	Readonly,
	Self;
	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmSln forValue(int value)
	{
		return values()[value];
	}
}
 