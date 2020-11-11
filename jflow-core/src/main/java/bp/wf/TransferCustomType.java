package bp.wf;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.template.*;
import java.util.*;

/** 
 流程运行类型
*/
public enum TransferCustomType
{
	/** 
	 按照流程定义的模式执行(自动模式)
	 s
	*/
	ByCCBPMDefine,
	/** 
	 按照工作人员的设置执行(人工干涉模式,人工定义模式.)
	*/
	ByWorkerSet;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TransferCustomType forValue(int value) throws Exception
	{
		return values()[value];
	}
}