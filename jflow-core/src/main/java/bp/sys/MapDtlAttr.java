package bp.sys;
import bp.en.*;
/** 
 明细
*/
public class MapDtlAttr extends EntityNoNameAttr
{
	/** 
	 行Idx
	*/
	public static final String RowIdx = "RowIdx";
	/** 
	 工作模式
	*/
	public static final String Model = "Model";
	/** 
	 使用的版本
	*/
	public static final String DtlVer = "DtlVer";
	/** 
	 主表
	*/
	public static final String FK_MapData = "FK_MapData";
	/** 
	 别名
	*/
	public static final String Alias = "Alias";
	/** 
	 PTable
	*/
	public static final String PTable = "PTable";
	/** 
	 DtlOpenType
	*/
	public static final String DtlOpenType = "DtlOpenType";
	/** 
	 行数量
	*/
	public static final String RowsOfList = "RowsOfList";
	/** 
	 是否显示合计
	*/
	public static final String IsShowSum = "IsShowSum";
	/** 
	 是否显示idx
	*/
	public static final String IsShowIdx = "IsShowIdx";
	/** 
	 是否允许copy数据
	*/
	public static final String IsCopyNDData = "IsCopyNDData";
	/** 
	 是否只读
	*/
	public static final String IsReadonly = "IsReadonly";
	/** 
	 WhenOverSize
	*/
	public static final String WhenOverSize = "WhenOverSize";
	/** 
	 是否可以删除
	*/
	public static final String IsDelete = "IsDelete";
	/** 
	 是否可以插入
	*/
	public static final String IsInsert = "IsInsert";
	/** 
	 是否可以更新
	*/
	public static final String IsUpdate = "IsUpdate";
	/** 
	 是否启用通过
	*/
	public static final String IsEnablePass = "IsEnablePass";
	/** 
	 是否是合流汇总数据
	*/
	public static final String IsHLDtl = "IsHLDtl";
	/** 
	 是否是分流
	*/
	public static final String IsFLDtl = "IsFLDtl";
	/** 
	 是否显示标题
	*/
	public static final String IsShowTitle = "IsShowTitle";
	/** 
	 列表显示格式
	*/
	public static final String ListShowModel = "ListShowModel";
	/** 
	 行数据显示格式
	*/
	public static final String EditModel = "EditModel";
	/** 
	 移动端显示方式
	*/
	public static final String MobileShowModel = "MobileShowModel";
	/** 
	 移动端列表展示时显示的字段
	*/
	public static final String MobileShowField = "MobileShowField";
	/** 
	 过滤的SQL 表达式.
	*/
	public static final String FilterSQLExp = "FilterSQLExp";
	/** 
	 列自动计算表达式
	*/
	public static final String ColAutoExp = "ColAutoExp";
	/** 
	 显示列
	*/
	public static final String ShowCols = "ShowCols";
	/** 
	 是否可见
	*/
	public static final String IsView = "IsView";
	/** 
	 x
	*/
	public static final String X = "X";
	/** 
	 Y
	*/
	public static final String Y = "Y";
	/** 
	 H高度
	*/
	public static final String H = "H";
	/** 
	 w宽度
	*/
	public static final String W = "W";
	/** 
	 宽度
	*/
	public static final String FrmW = "FrmW";
	/** 
	 高度
	*/
	public static final String FrmH = "FrmH";
	/** 
	 是否启用多附件
	*/
	public static final String IsEnableAthM = "IsEnableAthM";
	/** 
	 是否启用一对多
	*/
	public static final String IsEnableM2M = "IsEnableM2M";
	/** 
	 是否启用一对多多
	*/
	public static final String IsEnableM2MM = "IsEnableM2MM";
	/** 
	 多表头列
	*/
	//public const string MTR = "MTR";
	/** 
	 GUID
	*/
	public static final String GUID = "GUID";
	/** 
	 分组
	*/
	public static final String GroupField = "GroupField";
	/** 
	 关联主键
	*/
	public static final String RefPK = "RefPK";
	/** 
	 是否启用分组字段
	*/
	public static final String IsEnableGroupField = "IsEnableGroupField";
	/** 
	 节点(用于多表单的权限控制)
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 映射的实体事件类
	*/
	public static final String FEBD = "FEBD";
	/** 
	 导入模式.
	*/
	public static final String ImpModel = "ImpModel";


		///参数属性.
	public static final String IsEnableLink = "IsEnableLink";
	public static final String LinkLabel = "LinkLabel";
	public static final String LinkUrl = "LinkUrl";
	public static final String LinkTarget = "LinkTarget";
	/** 
	 从表存盘方式(失去焦点自动存盘，手工存盘)
	*/
	public static final String DtlSaveModel = "DtlSaveModel";
	/** 
	 明细表追加模式
	*/
	public static final String DtlAddRecModel = "DtlAddRecModel";

		/// 参数属性.


		///参数属性.
	/** 
	 是否启用锁定
	*/
	public static final String IsRowLock = "IsRowLock";
	/** 
	 子线程处理人字段
	*/
	public static final String SubThreadWorker = "SubThreadWorker";
	/** 
	 子线程分组字段.
	*/
	public static final String SubThreadGroupMark = "SubThreadGroupMark";

		/// 参数属性.


		///导入导出属性.
	/** 
	 是否可以导入
	*/
	public static final String IsImp = "IsImp";
	/** 
	 是否可以导出
	*/
	public static final String IsExp = "IsExp";
	/** 
	 查询sql
	*/
	public static final String ImpSQLSearch = "ImpSQLSearch";
	/** 
	 选择sql
	*/
	public static final String ImpSQLInit = "ImpSQLInit";
	/** 
	 填充数据一行数据
	*/
	public static final String ImpSQLFullOneRow = "ImpSQLFullOneRow";
	/** 
	 列的中文名称
	*/
	public static final String ImpSQLNames = "ImpSQLNames";

		///
}