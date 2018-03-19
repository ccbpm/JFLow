package BP.Sys;

import BP.DA.*;
import BP.En.*;

public enum FrmFrom
{
	Flow,
	Node,
	Dtl;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmFrom forValue(int value)
	{
		return values()[value];
	}
}