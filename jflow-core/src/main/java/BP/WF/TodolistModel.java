package BP.WF;

/** 
 普通工作节点处理模式
 
*/
public enum TodolistModel
{
	/** 
	 抢办(谁抢到谁来办理,办理完后其他人就不能办理.)
	 
	*/
	QiangBan,
	/** 
	 协作(没有处理顺序，接受的人都要去处理,由最后一个人发送到下一个节点)
	 
	*/
	Teamup,
	/** 
	 队列(按照顺序处理，有最后一个人发送到下一个节点)
	 
	*/
	Order,
	/** 
	 共享模式(需要申请，申请后才能执行)
	 
	*/
	Sharing,
	/** 
	 协作组长模式
	 
	*/
	TeamupGroupLeader;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TodolistModel forValue(int value)
	{
		return values()[value];
	}
}