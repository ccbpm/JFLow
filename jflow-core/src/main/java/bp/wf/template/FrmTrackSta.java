package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.sys.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 轨迹图标组件控件状态
*/
public enum FrmTrackSta
{
	/** 
	 不可用
	*/
	Disable,
	/** 
	 显示轨迹图
	*/
	Chart,
	/** 
	 显示轨迹表
	*/
	Table;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue() {
		return this.ordinal();
	}

	public static FrmTrackSta forValue(int value)
	{return values()[value];
	}
}