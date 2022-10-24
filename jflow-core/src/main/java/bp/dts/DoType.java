package bp.dts;

/** 
 执行类型
*/
public enum DoType
{
	/** 
	 没有指定
	*/
	UnName,
	/** 
	 先删除后插入,适合任何情况,但是运行效率较低．
	*/
	DeleteInsert,
	/** 
	 增量同步，适合增量的情况,比如纳税人的纳税信息.
	 对于增量以前的部分，不更新．
	*/
	Incremental,
	/** 
	 同步,适合任意中情况,比如纳税人．
	*/
	Inphase,
	/** 
	 直接的导入.把一个表从另外的一个地方导入近来．
	*/
	Directly,
	/** 
	 特殊的．
	*/
	Especial;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DoType forValue(int value) 
	{
		return values()[value];
	}
}