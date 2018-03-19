package BP.WF.Template;

/** 
 寻找领导模式
 
*/
public enum FindLeaderModel
{
	/** 
	 直接领导
	 
	*/
	DirLeader,
	/** 
	 指定职务级别的领导
	 
	*/
	SpecDutyLevelLeader,
	/** 
	 特定职务领导
	 
	*/
	DutyLeader,
	/** 
	 特定岗位
	 
	*/
	SpecStation;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FindLeaderModel forValue(int value)
	{
		return values()[value];
	}
}