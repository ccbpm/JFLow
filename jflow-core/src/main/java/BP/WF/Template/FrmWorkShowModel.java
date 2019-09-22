package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.*;
import java.util.*;

/** 
 显示格式
*/
public enum FrmWorkShowModel
{
	/** 
	 表格
	*/
	Table,
	/** 
	 自由显示
	*/
	Free;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmWorkShowModel forValue(int value)
	{
		return values()[value];
	}
}