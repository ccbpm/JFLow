package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.io.*;
import java.time.*;

public enum FrmFrom
{
	Flow,
	Node,
	Dtl;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmFrom forValue(int value)
	{
		return values()[value];
	}
}