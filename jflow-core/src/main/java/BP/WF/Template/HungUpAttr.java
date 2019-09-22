package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 挂起 属性
*/
public class HungUpAttr extends EntityMyPKAttr
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	public static final String Title = "Title";
	/** 
	 工作ID
	*/
	public static final String WorkID = "WorkID";
	/** 
	 执行人
	*/
	public static final String Rec = "Rec";
	/** 
	 通知给
	*/
	public static final String NoticeTo = "NoticeTo";
	/** 
	 挂起原因
	*/
	public static final String Note = "Note";
	/** 
	 挂起时间
	*/
	public static final String DTOfHungUp = "DTOfHungUp";
	/** 
	 节点ID
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 接受人
	*/
	public static final String Accepter = "Accepter";
	/** 
	 挂起方式.
	*/
	public static final String HungUpWay = "HungUpWay";
	/** 
	 解除挂起时间
	*/
	public static final String DTOfUnHungUp = "DTOfUnHungUp";
	/** 
	 计划解除挂起时间
	*/
	public static final String DTOfUnHungUpPlan = "DTOfUnHungUpPlan";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}