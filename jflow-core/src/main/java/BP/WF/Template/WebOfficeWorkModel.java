package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 公文工作模式
*/
public enum WebOfficeWorkModel
{
	/** 
	 不启用
	*/
	None,
	/** 
	 按钮方式启用
	*/
	Button,
	/** 
	 表单在前
	*/
	FrmFirst,
	/** 
	 文件在前
	*/
	WordFirst;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static WebOfficeWorkModel forValue(int value)
	{
		return values()[value];
	}
}