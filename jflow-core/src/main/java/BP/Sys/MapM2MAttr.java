package BP.Sys;

import BP.En.*;

/** 
 点对点
 
*/
public class MapM2MAttr extends EntityMyPKAttr
{
	/** 
	 主表
	 
	*/
	public static final String FK_MapData = "FK_MapData";
	/** 
	 插入表单的位置
	 
	*/
	public static final String RowIdx = "RowIdx";
	/** 
	 GroupID
	 
	*/
	public static final String GroupID = "GroupID";
	/** 
	 是否可以自适应大小
	 
	*/
	public static final String ShowWay = "ShowWay";
	/** 
	 类型
	 
	*/
	public static final String M2MType = "M2MType";
	/** 
	 DBOfLists (对一对多对多模式有效）
	 
	*/
	public static final String DBOfLists = "DBOfLists";
	/** 
	 DBOfObjs
	 
	*/
	public static final String DBOfObjs = "DBOfObjs";
	public static final String DBOfGroups = "DBOfGroups";
	public static final String IsDelete = "IsDelete";
	public static final String IsInsert = "IsInsert";
	/** 
	 是否显示选择全部?
	 
	*/
	public static final String IsCheckAll = "IsCheckAll";
	public static final String W = "W";
	public static final String H = "H";
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String Cols = "Cols";
	/** 
	 对象编号
	 
	*/
	public static final String NoOfObj = "NoOfObj";
	/** 
	 名称
	 
	*/
	public static final String Name = "Name";
	/** 
	 GUID
	 
	*/
	public static final String GUID = "GUID";
}