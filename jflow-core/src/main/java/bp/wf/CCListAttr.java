package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.port.*;
import bp.*;
import java.util.*;

/** 
 抄送 属性
*/
public class CCListAttr extends EntityMyPKAttr
{

		///#region 基本属性
	/** 
	 标题
	*/
	public static final String Title = "Title";
	/** 
	 抄送内容
	*/
	public static final String Doc = "Doc";
	/** 
	 抄送的节点
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 从节点
	*/
	public static final String NDFrom = "NDFrom";
	/** 
	 流程
	*/
	public static final String FK_Flow = "FK_Flow";
	public static final String FlowName = "FlowName";
	public static final String NodeName = "NodeName";
	/** 
	 是否读取
	*/
	public static final String Sta = "Sta";
	public static final String WorkID = "WorkID";
	public static final String FID = "FID";
	/** 
	 抄送给
	*/
	public static final String CCTo = "CCTo";
	/** 
	 抄送给人员名称
	*/
	public static final String CCToName = "CCToName";
	/** 
	 审核时间（回复时间）
	*/
	public static final String CDT = "CDT";
	/** 
	 阅读时间
	*/
	public static final String ReadDT = "ReadDT";
	/** 
	 抄送人员
	*/
	public static final String Rec = "Rec";
	/** 
	 RDT
	*/
	public static final String RDT = "RDT";
	/** 
	 父流程ID
	*/
	public static final String PWorkID = "PWorkID";
	/** 
	 父流程编号
	*/
	public static final String PFlowNo = "PFlowNo";
	/** 
	 优先级
	*/
	public static final String PRI = "PRI";
	/** 
	 是否加入待办列表
	*/
	public static final String InEmpWorks = "InEmpWorks";
	/** 
	 domain
	*/
	public static final String Domain = "Domain";
	/** 
	 组织编号
	*/
	public static final String OrgNo = "OrgNo";

		///#endregion

	public static final String CCToOrgNo = "CCToOrgNo";
	public static final String CCToOrgName = "CCToOrgName";
	public static final String CCToDept = "CCToDept";
	public static final String CCToDeptName = "CCToDeptName";
}