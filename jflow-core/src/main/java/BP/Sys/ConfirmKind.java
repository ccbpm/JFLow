package BP.Sys;

import BP.En.*;
import java.util.*;

public enum ConfirmKind
{
	/** 
	 当前单元格
	*/
	Cell,
	/** 
	 左方单元格
	*/
	LeftCell,
	/** 
	 上方单元格
	*/
	TopCell,
	/** 
	 右方单元格
	*/
	RightCell,
	/** 
	 下方单元格
	*/
	BottomCell;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static ConfirmKind forValue(int value)
	{
		return values()[value];
	}
}