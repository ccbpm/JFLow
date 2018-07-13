package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 映射基础
 
*/
public class MapDataAttr extends EntityNoNameAttr
{
	public static final String PTable = "PTable";
	public static final String PTableModel = "PTableModel";	
	public static final String Dtls = "Dtls";
	public static final String EnPK = "EnPK";
	public static final String FrmW = "FrmW";
	public static final String FrmH = "FrmH";
	public static final String FlowCtrls = "FlowCtrls";
	
	/** 
	 表格列(对傻瓜表单有效)
	 
	*/
	public static final String TableCol = "TableCol";
	/** 
	 表格宽度
	 
	*/
	public static final String TableWidth = "TableWidth";
	/** 
	 表格高度
	 
	*/
	public static final String TableHeight = "TableHeight";
	/** 
	 来源
	 
	*/
	public static final String FrmFrom = "FrmFrom";
	/** 
	 设计者
	 
	*/
	public static final String Designer = "Designer";
	/** 
	 设计者单位
	 
	*/
	public static final String DesignerUnit = "DesignerUnit";
	/** 
	 设计者联系方式
	 
	*/
	public static final String DesignerContact = "DesignerContact";
	/** 
	 设计器
	 
	*/
	public static final String DesignerTool = "DesignerTool";
	/** 
	 表单类别
	 
	*/
	public static final String FK_FrmSort = "FK_FrmSort";
	/** 
	 表单树类别
	 
	*/
	public static final String FK_FormTree = "FK_FormTree";
	/** 
	 表单类型
	 
	*/
	public static final String FrmType = "FrmType";
	/** 
	 Url(对于嵌入式表单有效)
	 
	*/
	public static final String Url = "Url";
	/** 
	 Tag
	 
	*/
	public static final String Tag = "Tag";
	/** 
	 备注
	 
	*/
	public static final String Note = "Note";
	/** 
	 Idx
	 
	*/
	public static final String Idx = "Idx";
	/** 
	 GUID
	 
	*/
	public static final String GUID = "GUID";
	/** 
	 版本号
	 
	*/
	public static final String Ver = "Ver";
	/** 
	 数据源
	 
	*/
	public static final String DBSrc = "DBSrc";
	/** 
	 应用类型
	 
	*/
	public static final String AppType = "AppType";
	/** 
	 表单body属性.
	 
	*/
	public static final String BodyAttr = "BodyAttr";


		///#region 报表属性(参数的方式存储).
	/** 
	 是否关键字查询
	 
	*/
	public static final String RptIsSearchKey = "RptIsSearchKey";
	/** 
	 时间段查询方式
	 
	*/
	public static final String RptDTSearchWay = "RptDTSearchWay";
	/** 
	 时间字段
	 
	*/
	public static final String RptDTSearchKey = "RptDTSearchKey";
	/** 
	 查询外键枚举字段
	 
	*/
	public static final String RptSearchKeys = "RptSearchKeys";

		///#endregion 报表属性(参数的方式存储).


		///#region 其他计算属性，参数存储.
	/** 
	 最左边的值
	 
	*/
	public static final String MaxLeft = "MaxLeft";
	/** 
	 最右边的值
	 
	*/
	public static final String MaxRight = "MaxRight";
	/** 
	 最头部的值
	 
	*/
	public static final String MaxTop = "MaxTop";
	/** 
	 最底部的值
	 
	*/
	public static final String MaxEnd = "MaxEnd";

		///#endregion 其他计算属性，参数存储.


		///#region weboffice属性。
	/** 
	 是否启用锁定行
	 
	*/
	public static final String IsRowLock = "IsRowLock";
	/** 
	 是否启用weboffice
	 
	*/
	public static final String IsWoEnableWF = "IsWoEnableWF";
	/** 
	 是否启用保存
	 
	*/
	public static final String IsWoEnableSave = "IsWoEnableSave";
	/** 
	 是否只读
	 
	*/
	public static final String IsWoEnableReadonly = "IsWoEnableReadonly";
	/** 
	 是否启用修订
	 
	*/
	public static final String IsWoEnableRevise = "IsWoEnableRevise";
	/** 
	 是否查看用户留痕
	 
	*/
	public static final String IsWoEnableViewKeepMark = "IsWoEnableViewKeepMark";
	/** 
	 是否打印
	 
	*/
	public static final String IsWoEnablePrint = "IsWoEnablePrint";
	/** 
	 是否启用签章
	 
	*/
	public static final String IsWoEnableSeal = "IsWoEnableSeal";
	/** 
	 是否启用套红
	 
	*/
	public static final String IsWoEnableOver = "IsWoEnableOver";
	/** 
	 是否启用公文模板
	 
	*/
	public static final String IsWoEnableTemplete = "IsWoEnableTemplete";
	/** 
	 是否自动写入审核信息
	 
	*/
	public static final String IsWoEnableCheck = "IsWoEnableCheck";
	/** 
	 是否插入流程
	 
	*/
	public static final String IsWoEnableInsertFlow = "IsWoEnableInsertFlow";
	/** 
	 是否插入风险点
	 
	*/
	public static final String IsWoEnableInsertFengXian = "IsWoEnableInsertFengXian";
	/** 
	 是否启用留痕模式
	 
	*/
	public static final String IsWoEnableMarks = "IsWoEnableMarks";
	/** 
	 是否启用下载
	 
	*/
	public static final String IsWoEnableDown = "IsWoEnableDown";
	 /** 
	 表单事件实体类
	 
*/
	public static final String FormEventEntity = "FormEventEntity";
	
	/**
	 *  参数属性.
	 */
	public static final String EnsName = "EnsName";

		///#endregion weboffice属性。
}