package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 菜单类型
 
*/
public enum MenuType
{
	/** 
	 系统根目录
	 
	*/
	Root,
	/** 
	 系统类别
	 
	*/
	AppSort,
	/** 
	 系统
	 
	*/
	App,
	/** 
	 目录
	 
	*/
	Dir,
	/** 
	 菜单
	 
	*/
	Menu,
	/** 
	 功能控制点
	 
	*/
	Function;

	public int getValue()
	{
		return this.ordinal();
	}

	public static MenuType forValue(int value)
	{
		return values()[value];
	}
}