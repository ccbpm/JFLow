package bp.en;


/** 
 可以被重写的类 
*/
public class OverrideFile
{

		///可以重写的表单事件.
	/** 
	 执行的事件
	 
	 param frmID
	 param en
	*/
	public static void FrmEvent_LoadBefore(String frmID, Entity en)
	{
	}
	/** 
	 装载填充的事件.
	 
	 param frmID
	 param en
	*/
	public static void FrmEvent_FrmLoadAfter(String frmID, Entity en)
	{

	}
	/** 
	 保存前事件
	 
	 param frmID
	 param en
	*/
	public static void FrmEvent_SaveBefore(String frmID, Entity en)
	{

	}
	/** 
	 保存后事件
	 
	 param frmID
	 param en
	*/
	public static void FrmEvent_SaveAfter(String frmID, Entity en)
	{
	}

		/// 可以重写的表单事件.
}