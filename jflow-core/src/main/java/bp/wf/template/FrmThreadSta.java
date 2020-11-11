package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 子线程组件控件状态
*/
public enum FrmThreadSta
{
	/** 
	 不可用
	*/
	Disable,
	/** 
	 启用
	*/
	Enable;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmThreadSta forValue(int value) throws Exception
	{
		return values()[value];
	}
}