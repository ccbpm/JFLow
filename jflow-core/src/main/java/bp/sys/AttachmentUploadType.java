package bp.sys;



/** 
 附件上传类型
*/
public enum AttachmentUploadType
{
	/** 
	 单个的
	*/
	Single,
	/** 
	 多个的
	*/
	Multi,
	/** 
	 指定的
	*/
	Specifically;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AttachmentUploadType forValue(int value)
	{
		return values()[value];
	}
}