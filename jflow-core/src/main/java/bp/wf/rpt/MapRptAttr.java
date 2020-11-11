package bp.wf.rpt;
import bp.en.EntityNoNameAttr;
/** 
 报表设计
*/
public class MapRptAttr extends EntityNoNameAttr
{
	/** 
	 查询的物理表
	*/
	public static final String PTable = "PTable";
	/** 
	 备注
	*/
	public static final String Note = "Note";
	/** 
	 流程编号
	*/
	public static final String FK_Flow = "FK_Flow";


		///权限控制 2014-12-18
	/** 
	 报表查看权限控制方式
	*/
	public static final String RightViewWay = "RightViewWay";
	/** 
	 数据存储
	*/
	public static final String RightViewTag = "RightViewTag";
	/** 
	 部门数据权限控制方式
	*/
	public static final String RightDeptWay = "RightDeptWay";
	/** 
	 数据存储
	*/
	public static final String RightDeptTag = "RightDeptTag";

		/// 权限控制
}