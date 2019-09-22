package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;

/** 
 开始工作基类属性
*/
public class StartWorkAttr extends WorkAttr
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 部门
	*/
	public static final String FK_Dept = "FK_Dept";
	/** 
	 工作内容标题
	*/
	public static final String Title = "Title";
	/** 
	 PRI
	*/
	public static final String PRI = "PRI";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 父子流程属性
	public static final String PFlowNo = "PFlowNo";
	public static final String PWorkID = "PWorkID";
	public static final String PNodeID = "PNodeID";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 父子流程属性
}