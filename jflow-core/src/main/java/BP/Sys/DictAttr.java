package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import java.util.*;

/** 
  配置信息
*/
public class DictAttr extends EntityMyPKAttr
{

	/// <summary>
	/// 属性Key
	/// </summary>
	public static final String TableID = "TableID";
	/// <summary>
	/// 工作人员
	/// </summary>
	public static final String TableName = "TableName";
	/// <summary>
	/// 列选择
	/// </summary>
	public static final String OrgNo = "OrgNo";
	/// <summary>
	/// 0=EntityNoName, 1=EntityTree
	/// </summary>
	public static final String DictType = "DictType";
	/// <summary>
	/// 顺序号
	/// </summary>
	public static final String Idx = "Idx";
}