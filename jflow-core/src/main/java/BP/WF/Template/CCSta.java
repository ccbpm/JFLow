package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

public enum CCSta
{
	/** 
	 未读
	*/
	UnRead,
	/** 
	 已读取
	*/
	Read,
	/** 
	 已经回复
	*/
	CheckOver,
	/** 
	 已删除
	*/
	Del;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CCSta forValue(int value)
	{
		return values()[value];
	}
}