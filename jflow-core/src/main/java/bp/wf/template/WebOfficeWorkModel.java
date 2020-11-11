package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.port.*;
import bp.wf.*;
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

	public static WebOfficeWorkModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}