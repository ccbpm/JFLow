package BP.WF.Template;


/** 
 单据模板属性
*/
public class BillTemplateAttr extends BP.En.EntityNoNameAttr
{
	public static final String Url = "Url";
	/** 
	 NodeID
	*/
	public static final String NodeID = "NodeID";
	/** 
	 是否需要送达
	*/
	public static final String IsNeedSend = "IsNeedSend";
	/** 
	 为生成单据使用
	*/
	public static final String IDX = "IDX";
	/** 
	 要排除的字段
	*/
	public static final String ExpField = "ExpField";
	/** 
	 要替换的值
	*/
	public static final String ReplaceVal = "ReplaceVal";
	/** 
	 单据类型
	*/
	public static final String FK_BillType = "FK_BillType";
	/** 
	 是否生成PDF
	*/
	public static final String BillFileType = "BillFileType";
}