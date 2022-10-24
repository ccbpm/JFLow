package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.template.sflow.*;
import bp.wf.template.frm.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 方案类型
*/
public enum FrmSln
{
	/** 
	 默认方案
	*/
	Default,
	/** 
	 只读方案
	*/
	Readonly,
	/** 
	 自定义方案
	*/
	Self;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static FrmSln forValue(int value) 
	{return values()[value];
	}
}