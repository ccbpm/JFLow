package BP.WF;

import BP.WF.*;
import BP.Web.*;
import BP.En.*;
import BP.DA.*;

public enum FlowShowType
{
	/** 
	 当前工作
	*/
	MyWorks,
	/** 
	 新建
	*/
	WorkNew,
	/** 
	 工作步骤
	*/
	WorkStep,
	/** 
	 工作图片
	*/
	WorkImages;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowShowType forValue(int value)
	{
		return values()[value];
	}
}