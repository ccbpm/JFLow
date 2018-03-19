package BP.DA;

public enum TWay {
	/**
	 * 计算节假日
	 */
    Holiday,
    /**
     * 不计算节假日
     */
    AllDays;
    
    public int getValue()
	{
		return this.ordinal();
	}
	
	public static TWay forValue(int value)
	{
		return values()[value];
	}
    
}
