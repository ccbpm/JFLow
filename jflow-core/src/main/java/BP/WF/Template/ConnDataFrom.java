package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Data.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 条件数据源
*/
public enum ConnDataFrom
{
	/** 
	 表单数据
	*/
	NodeForm,
	 /** 
	 独立表单
	 */
	StandAloneFrm,
	/** 
	 岗位数据
	*/
	Stas,
	/** 
	 Depts
	*/
	Depts,
	/** 
	 按sql计算.
	*/
	SQL,
	/** 
	 按sql模版计算.
	*/
	SQLTemplate,
	/** 
	 按参数
	*/
	Paras,
	/** 
	 按Url.
	*/
	Url;


	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static ConnDataFrom forValue(int value)
	{
		return values()[value];
	}
}