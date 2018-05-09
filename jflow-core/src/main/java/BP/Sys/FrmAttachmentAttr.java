package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 附件
 
*/
public class FrmAttachmentAttr extends EntityMyPKAttr
{
	/** 
	 Name
	 
	*/
	public static final String Name = "Name";
	/** 
	 主表
	 
	*/
	public static final String FK_MapData = "FK_MapData";
	
	/**
	 * 运行模式
	 */
    public static String AthRunModel = "AthRunModel";
	
	/** 
	 节点ID
	 
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 X
	 
	*/
	public static final String X = "X";
	/** 
	 Y
	 
	*/
	public static final String Y = "Y";
	/** 
	 宽度
	 
	*/
	public static final String W = "W";
	/** 
	 高度
	 
	*/
	public static final String H = "H";
	/** 
	 要求上传的格式
	 
	*/
	public static final String Exts = "Exts";
	/** 
	 附件编号
	 
	*/
	public static final String NoOfObj = "NoOfObj";
	/** 
	 是否可以上传
	 
	*/
	public static final String IsUpload = "IsUpload";
	/** 
	 是否是合流汇总
	 
	*/
	public static final String IsHeLiuHuiZong = "IsHeLiuHuiZong";
	/** 
	 是否汇总到合流节点上去？
	 
	*/
	public static final String IsToHeLiuHZ = "IsToHeLiuHZ";
	/** 
	 是否可以删除
	*/
	public static final String IsDelete = "IsDelete";
	/** 
	 是否增加
	 
	*/
	public static final String IsNote = "IsNote";
	
	/// <summary>
    /// 是否启用扩展列
    /// </summary>
    public static final String IsExpCol = "IsExpCol";
    
	/** 
	 是否显示标题列
	 
	*/
	public static final String IsShowTitle = "IsShowTitle";
	/** 
	 是否可以下载
	 
	*/
	public static final String IsDownload = "IsDownload";
	/** 
	 是否可以排序
	 
	*/
	public static final String IsOrder = "IsOrder";
	/** 
	 数据存储方式
	 
	*/
	//public static final String SaveWay = "SaveWay";
	public static final String AthSaveWay = "AthSaveWay";
	/** 
	 保存到
	 
	*/
	public static final String SaveTo = "SaveTo";
	/** 
	 是否要转换成html，方便在线浏览.
	 
	*/
	public static final String IsTurn2Hmtl = "IsTurn2Hmtl";
	/** 
	 类别
	 
	*/
	public static final String Sort = "Sort";
	/** 
	 上传类型
	 
	*/
	public static final String UploadType = "UploadType";
	/** 
	 GroupID
	 
	*/
	public static final String GroupID = "GroupID";
	/** RowIdx
	 
	*/
	public static final String RowIdx = "RowIdx";
	/** 
	 
	 自动控制大小
	 
	*/
	public static final String IsAutoSize = "IsAutoSize";
	/** 
	 GUID
	 
	*/
	public static final String GUID = "GUID";
	/** 
	 数据控制方式(对父子流程有效果)
	 
	*/
	public static final String CtrlWay = "CtrlWay";
	/** 
	 上传方式(对父子流程有效果)
	 
	*/
	public static final String AthUploadWay = "AthUploadWay";
	/** 
	 文件展现方式
	 
	*/
	public static final String FileShowWay = "FileShowWay";
	/** 
	 上传方式
	 0，批量上传。
	 1，单个上传。
	 
	*/
	public static final String UploadCtrl = "UploadCtrl";
	/** 
	 上传校验
	 0=不校验.
	 1=不能为空.
	 2=每个类别下不能为空.
	 
	*/
	public static final String UploadFileNumCheck = "UploadFileNumCheck";
	/** 
	 是否可见？
	 
	*/
	public static final String IsVisable = "IsVisable";

	/** 
	 附件删除方式
	 
	*/
	public static final String DeleteWay = "DeleteWay";



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

	//#endregion weboffice属性。


	//#region 快捷键.
	/** 
	 是否启用快捷键
	 
	*/
	public static final String FastKeyIsEnable = "FastKeyIsEnable";
	/** 
	 快捷键生成规则
	 
	*/
	public static final String FastKeyGenerRole = "FastKeyGenerRole";

	//#endregion
	
	/**
	 * 是否要转换成html，方便在线浏览.
	 */
    public static String IsTurn2Html = "IsTurn2Html";
    /** 
	 数据引用
	 
*/
	public static final String DataRefNoOfObj = "DataRefNoOfObj";
}