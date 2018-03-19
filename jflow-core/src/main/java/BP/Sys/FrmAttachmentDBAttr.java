package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 附件数据存储 - 属性
 
*/
public class FrmAttachmentDBAttr extends EntityMyPKAttr
{
	/** 
	 附件
	 
	*/
	public static final String FK_FrmAttachment = "FK_FrmAttachment";
	/** 
	 主表
	 
	*/
	public static final String FK_MapData = "FK_MapData";
	/** 
	 RefPKVal
	 
	*/
	public static final String RefPKVal = "RefPKVal";
	/** 
	 流程ID
	 
	*/
	public static final String FID = "FID";
	/** 
	 文件名称
	 
	*/
	public static final String FileName = "FileName";
	/** 
	 文件扩展
	 
	*/
	public static final String FileExts = "FileExts";
	/** 
	 文件大小
	 
	*/
	public static final String FileSize = "FileSize";
	/** 
	 保存到
	 
	*/
	public static final String FileFullName = "FileFullName";
	/** 
	 记录日期
	 
	*/
	public static final String RDT = "RDT";
	/** 
	 记录人
	 
	*/
	public static final String Rec = "Rec";
	/** 
	 记录人名字
	 
	*/
	public static final String RecName = "RecName";
	/** 
	 类别
	 
	*/
	public static final String Sort = "Sort";
	/** 
	 备注
	 
	*/
	public static final String MyNote = "MyNote";
	/** 
	 节点ID
	 
	*/
	public static final String NodeID = "NodeID";
	/** 
	 是否锁定行
	 
	*/
	public static final String IsRowLock = "IsRowLock";
	/** 
	 上传的GUID
	 
	*/
	public static final String UploadGUID = "UploadGUID";
	/** 
	 Idx
	 
	*/
	public static final String Idx = "Idx";
	/** 
	 保存方式
	 
	*/
	public static final String SaveWay = "SaveWay";
	public static final String NoOfObj = "NoOfObj";
}