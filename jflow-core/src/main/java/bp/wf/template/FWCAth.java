package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 附件类型
*/
public enum FWCAth
{
	/** 
	 使用附件
	*/
	None,
	/** 
	 多附件
	*/
	MinAth,
	/** 
	 单附件
	*/
	SingerAth,
	/** 
	 图片附件
	*/
	ImgAth;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()  {
		return this.ordinal();
	}

	public static FWCAth forValue(int value)
	{return values()[value];
	}
}