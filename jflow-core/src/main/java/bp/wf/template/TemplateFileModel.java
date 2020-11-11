package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;
import java.io.*;

/** 
 模版类型
*/
public enum TemplateFileModel
{
	/** 
	 旧版本的rtf模版格式
	*/
	RTF,
	/** 
	 Word模版格式
	*/
	VSTOForWord,
	/** 
	 Excel模版格式
	*/
	VSTOForExcel;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TemplateFileModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}