package bp.ta;


/** 
 项目状态
*/
public class PrjSta
{
	/** 
	 空白
	*/
	public static final int Blank = 0;
	/** 
	 草稿
	*/
	public static final int Draft = 1;
	/** 
	 运行中
	*/
	public static final int Runing = 2;
	/** 
	 已完成
	*/
	public static final int Complete = 3;
	///// <summary>
	///// 挂起
	///// </summary>
	//public const int Hungup = 4;
	/** 
	 删除(逻辑删除状态)
	*/
	public static final int Delete = 7;
	///// <summary>
	///// 冻结
	///// </summary>
	//public const int Fix = 9;
}
