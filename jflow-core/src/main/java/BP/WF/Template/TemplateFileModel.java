package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.*;
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

	public static TemplateFileModel forValue(int value)
	{
		return values()[value];
	}
}