package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程测试属性
*/
public class TestCaseDtlAttr extends BP.En.EntityOIDNameAttr
{

		///#region 基本属性
	/** 
	 节点
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 流程编号
	*/
	public static final String FK_Flow = "FK_Flow";
	/** 
	 参数类型
	*/
	public static final String ParaType = "ParaType";
	/** 
	 Vals
	*/
	public static final String Vals = "Vals";
	/** 
	 顺序号
	*/
	public static final String Idx = "Idx";
	/** 
	 显示在那里？
	*/
	public static final String ShowWhere = "ShowWhere";

		///#endregion
}