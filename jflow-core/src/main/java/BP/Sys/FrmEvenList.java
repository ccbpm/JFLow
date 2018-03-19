package BP.Sys;


/** 
 表单事件列表
 
*/
public enum FrmEvenList
{
	/** 
	 创建OID
	 
	*/
	CreateOID,
	/** 
	 装载前
	 
	*/
	FrmLoadBefore,
	/** 
	 装载后
	 
	*/
	FrmLoadAfter,
	/** 
	 保存前
	 
	*/
	SaveBefore,
	/** 
	 保存后
	 
	*/
	SaveAfter;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmEvenList forValue(int value)
	{
		return values()[value];
	}
}