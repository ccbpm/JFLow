package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Data.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 指定操作员方式
*/
public enum SpecOperWay
{
	/** 
	 当前的人员
	*/
	CurrOper,
	/** 
	 指定节点人员
	*/
	SpecNodeOper,
	/** 
	 指定表单人员
	*/
	SpecSheetField,
	/** 
	 指定人员编号
	*/
	SpenEmpNo;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SpecOperWay forValue(int value)
	{
		return values()[value];
	}
}