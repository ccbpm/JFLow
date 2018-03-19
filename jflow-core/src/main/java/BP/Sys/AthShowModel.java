package BP.Sys;

import BP.En.UIContralType;


/**
 * 附件在扩展控件里的显示方式
 * @author Administrator
 *
 */
public enum AthShowModel
{
	/**
	 * 简单的
	 */
    
    Simple,
   
    /**
     * 只有文件名称
     */
    
    FileNameOnly;
	
    public int getValue()
	{
		return this.ordinal();
	}
	
	public static AthShowModel forValue(int value)
	{
		return values()[value];
	}
}