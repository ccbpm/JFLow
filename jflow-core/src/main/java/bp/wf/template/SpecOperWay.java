package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.data.*;
import bp.web.*;
import bp.*;
import bp.wf.*;

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

	public int getValue() {
		return this.ordinal();
	}

	public static SpecOperWay forValue(int value) 
	{return values()[value];
	}
}