package bp.wf.template;
/** 
 单据模板属性
*/
public class BillTemplateAttr extends bp.en.EntityNoNameAttr
{
	/** 
	 路径
	*/
	public static final String TempFilePath = "TempFilePath";
	/** 
	 NodeID
	*/
	public static final String NodeID = "NodeID";
	/** 
	 为生成单据使用
	*/
	public static final String Idx = "Idx";
	/** 
	 单据类型
	*/
	public static final String TemplateFileModel = "TemplateFileModel";
	/** 
	 是否生成PDF
	*/
	public static final String BillFileType = "BillFileType";
	/** 
	 二维码生成方式
	*/
	public static final String QRModel = "QRModel";
	/** 
	 文件打开方式
	*/
	public static final String BillOpenModel = "BillOpenModel";
	/** 
	 表单的ID
	*/
	public static final String FK_MapData = "FK_MapData";
}