package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.difference.*;
import bp.*;
import java.util.*;

/** 
 编号生成规则
*/
public enum NoGenerModel
{
	/** 
	 自定义
	*/
	None,
	/** 
	 流水号
	*/
	ByLSH,
	/** 
	 标签的全拼
	*/
	ByQuanPin,
	/** 
	 标签的简拼
	*/
	ByJianPin,
	/** 
	 按GUID生成
	*/
	ByGUID;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static NoGenerModel forValue(int value) 
	{return values()[value];
	}
}