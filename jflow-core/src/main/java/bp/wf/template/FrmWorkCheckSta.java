package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 审核组件状态
*/
public enum FrmWorkCheckSta
{
	/** 
	 不可用
	*/
	Disable,
	/** 
	 可用
	*/
	Enable,
	/** 
	 只读
	*/
	Readonly;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static FrmWorkCheckSta forValue(int value)
	{return values()[value];
	}
}