package BP.En;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import BP.DA.AtPara;
import BP.DA.DataType;

/**
 * 灞炴�ч泦鍚�
 */
public class Attrs extends ArrayList<Attr>
{
	public static ArrayList<Attr> convertAttrs(Object obj)
	{
		return (ArrayList<Attr>) obj;
	}
	public List<Attr> ToJavaList()
	{
		return (List<Attr>)(Object)this;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 鍏充簬灞炴�х殑澧炲姞 String
	protected final void AddTBString(String key, String field,
			Object defaultVal, FieldType _FieldType, TBType tbType,
			String desc, boolean uiVisable, boolean isReadonly, int minLength,
			int maxLength, int tbWith)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppString);
		attr.setDesc(desc);
		attr.setUITBShowType(tbType);
		attr.setUIVisible(uiVisable);
		attr.setUIWidth(tbWith);
		attr.setUIIsReadonly(isReadonly);
		attr.setMaxLength(maxLength);
		attr.setMinLength(minLength);
		attr.setMyFieldType(_FieldType);
		this.Add(attr);
	}
	
	public final void AddTBString(String key, String defaultVal, String desc,
			boolean uiVisable, boolean isReadonly, int minLength,
			int maxLength, int tbWith)
	{
		AddTBString(key, key, defaultVal, FieldType.Normal, TBType.TB, desc,
				uiVisable, isReadonly, minLength, maxLength, tbWith);
	}
	
	public final void AddTBString(String key, String field, Object defaultVal,
			String desc, boolean uiVisable, boolean isReadonly, int minLength,
			int maxLength, int tbWith)
	{
		AddTBString(key, field, defaultVal, FieldType.Normal, TBType.TB, desc,
				uiVisable, isReadonly, minLength, maxLength, tbWith);
	}
	
	public final void AddTBStringDoc(String key, String defaultVal,
			String desc, boolean uiVisable, boolean isReadonly)
	{
		AddTBStringDoc(key, key, defaultVal, desc, uiVisable, isReadonly, 0,
				2000, 300, 300);
	}
	
	public final void AddTBStringDoc(String key, String defaultVal,
			String desc, boolean uiVisable, boolean isReadonly, int minLength,
			int maxLength, int tbWith, int rows)
	{
		AddTBStringDoc(key, key, defaultVal, desc, uiVisable, isReadonly,
				minLength, maxLength, tbWith, rows);
	}
	
	public final void AddTBStringDoc(String key, String field,
			String defaultVal, String desc, boolean uiVisable,
			boolean isReadonly, int minLength, int maxLength, int tbWith,
			int rows)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppString);
		attr.setDesc(desc);
		attr.setUITBShowType(TBType.TB);
		attr.setUIVisible(uiVisable);
		attr.setUIWidth(300);
		attr.setUIIsReadonly(isReadonly);
		attr.setMaxLength(maxLength);
		attr.setMinLength(minLength);
		attr.setMyFieldType(FieldType.Normal);
		attr.setUIHeight(rows);
		this.Add(attr);
	}
	
	/**
	 * 澧炲姞闄勪欢
	 * 
	 * @param fileDesc
	 */
	public final void AddMyFile(String fileDesc)
	{
		this.AddTBString(EntityNoMyFileAttr.MyFileName, null, fileDesc, false,
				false, 0, 100, 200);
		this.AddTBString(EntityNoMyFileAttr.MyFilePath, null, "MyFilePath",
				false, false, 0, 100, 200);
		this.AddTBString(EntityNoMyFileAttr.MyFileExt, null, "MyFileExt",
				false, false, 0, 10, 10);
		// this.AddTBInt(EntityNoMyFileAttr.MyFileNum, 0, "MyFileNum", false,
		// false);
		this.AddTBInt(EntityNoMyFileAttr.MyFileH, 0, "MyFileH", false, false);
		this.AddTBInt(EntityNoMyFileAttr.MyFileW, 0, "MyFileW", false, false);
		this.AddTBInt("MyFileSize", 0, "MyFileSize", false, false);
		
		// this.IsHaveFJ = true;
	}
	
	// 鍏充簬灞炴�х殑澧炲姞 String
	
	// 鍏充簬灞炴�х殑澧炲姞 Int
	
	/**
	 * 澧炲姞涓�涓櫘閫氱殑绫诲瀷銆�
	 * 
	 * @param key
	 *            閿�
	 * @param _Field
	 *            瀛楁
	 * @param defaultVal
	 *            榛樿鍊�
	 * @param desc
	 *            鎻忚堪
	 * @param uiVisable
	 *            鏄笉鏄彲瑙�
	 * @param isReadonly
	 *            鏄笉鏄彧璇�
	 */
	public final void AddTBInt(String key, String _Field, int defaultVal,
			String desc, boolean uiVisable, boolean isReadonly)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(_Field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppInt);
		attr.setMyFieldType(FieldType.Normal);
		attr.setDesc(desc);
		attr.setUITBShowType(TBType.Int);
		attr.setUIVisible(uiVisable);
		attr.setUIIsReadonly(isReadonly);
		this.Add(attr);
	}
	
	/**
	 * 澧炲姞涓�涓櫘閫氱殑绫诲瀷銆傚瓧娈靛�间笌灞炴�х浉鍚屻��
	 * 
	 * @param key
	 *            閿�
	 * @param defaultVal
	 *            榛樿鍊�
	 * @param desc
	 *            鎻忚堪
	 * @param uiVisable
	 *            鏄笉鏄彲瑙�
	 * @param isReadonly
	 *            鏄笉鏄彧璇�
	 */
	public final void AddTBInt(String key, int defaultVal, String desc,
			boolean uiVisable, boolean isReadonly)
	{
		this.AddTBInt(key, key, defaultVal, desc, uiVisable, isReadonly);
	}
	
	public final void AddBoolen(String key, boolean defaultVal, String desc)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(key);
		
		if (defaultVal)
		{
			attr.setDefaultVal(1);
		} else
		{
			attr.setDefaultVal(0);
		}
		
		attr.setMyDataType(DataType.AppBoolean);
		attr.setDesc(desc);
		attr.setUIContralType(UIContralType.CheckBok);
		attr.setUIIsReadonly(true);
		attr.setUIVisible(true);
		this.Add(attr);
	}
	
	// 鍏充簬灞炴�х殑澧炲姞 Int
	
	// 鍏充簬灞炴�х殑澧炲姞 Float绫诲瀷
	public final void AddTBFloat(String key, String _Field, float defaultVal,
			String desc, boolean uiVisable, boolean isReadonly)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(_Field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppFloat);
		attr.setDesc(desc);
		attr.setUITBShowType(TBType.Num);
		attr.setUIVisible(uiVisable);
		attr.setUIIsReadonly(isReadonly);
		this.Add(attr);
	}
	
	public final void AddTBFloat(String key, float defaultVal, String desc,
			boolean uiVisable, boolean isReadonly)
	{
		this.AddTBFloat(key, key, defaultVal, desc, uiVisable, isReadonly);
	}
	
	// 鍏充簬灞炴�х殑澧炲姞 Float
	
	// Decimal绫诲瀷
	public final void AddTBDecimal(String key, String _Field,
			java.math.BigDecimal defaultVal, String desc, boolean uiVisable,
			boolean isReadonly)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(_Field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppDouble);
		attr.setDesc(desc);
		attr.setUITBShowType(TBType.Decimal);
		attr.setUIVisible(uiVisable);
		attr.setUIIsReadonly(isReadonly);
		this.Add(attr);
	}
	
	public final void AddTBDecimal(String key, java.math.BigDecimal defaultVal,
			String desc, boolean uiVisable, boolean isReadonly)
	{
		this.AddTBDecimal(key, key, defaultVal, desc, uiVisable, isReadonly);
	}
	
	// 鏃ユ湡
	public final void AddTBDate(String key, String field, String defaultVal,
			String desc, boolean uiVisable, boolean isReadonly)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppDate);
		attr.setDesc(desc);
		attr.setUITBShowType(TBType.Date);
		attr.setUIVisible(uiVisable);
		attr.setUIIsReadonly(isReadonly);
		attr.setMaxLength(20);
		this.Add(attr);
	}
	
	public final void AddTBDate(String key, String defaultVal, String desc,
			boolean uiVisable, boolean isReadonly)
	{
		this.AddTBDate(key, key, defaultVal, desc, uiVisable, isReadonly);
	}
	
	/**
	 * 澧炲姞鏃ユ湡绫诲瀷鐨勬帶鍋�(榛樿鏃ユ湡鏄綋鍓嶆棩鏈�)
	 * 
	 * @param key
	 *            key
	 * @param desc
	 *            desc
	 * @param uiVisable
	 *            uiVisable
	 * @param isReadonly
	 *            isReadonly
	 */
	public final void AddTBDate(String key, String desc, boolean uiVisable,
			boolean isReadonly)
	{
		this.AddTBDate(key, key,
				DataType.dateToStr(new Date(), DataType.getSysDataFormat()),
				desc, uiVisable, isReadonly);
		
		/*
		 * warning this.AddTBDate(key, key, new
		 * java.util.Date().ToString(DataType.getSysDataFormat()), desc,
		 * uiVisable, isReadonly);
		 */
	}
	
	// 鏃ユ湡鏃堕棿绫诲瀷銆�
	/**
	 * 澧炲姞鏃ユ湡绫诲瀷鐨勬帶鍋�
	 * 
	 * @param key
	 *            鍋ュ��
	 * @param defaultVal
	 *            榛樿鍊�
	 * @param desc
	 *            鎻忚堪
	 * @param uiVisable
	 *            鏄笉鏄彲瑙�
	 * @param isReadonly
	 *            鏄笉鏄彧璇�
	 */
	public final void AddTBDateTime(String key, String field,
			String defaultVal, String desc, boolean uiVisable,
			boolean isReadonly)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppDateTime);
		attr.setDesc(desc);
		attr.setUITBShowType(TBType.DateTime);
		attr.setUIVisible(uiVisable);
		attr.setUIIsReadonly(isReadonly);
		attr.setMaxLength(30);
		attr.setMinLength(0);
		attr.setUIWidth(100);
		this.Add(attr);
	}
	
	public final void AddTBDateTime(String key, String defaultVal, String desc,
			boolean uiVisable, boolean isReadonly)
	{
		this.AddTBDateTime(key, key, defaultVal, desc, uiVisable, isReadonly);
	}
	
	public final void AddTBDateTime(String key, String desc, boolean uiVisable,
			boolean isReadonly)
	{
		this.AddTBDateTime(
				key,
				key,
				DataType.dateToStr(new Date(), DataType.getSysDataTimeFormat()),
				desc, uiVisable, isReadonly);
		/*
		 * warning this.AddTBDateTime(key, key, new
		 * java.util.Date().ToString(DataType.getSysDataTimeFormat()), desc,
		 * uiVisable, isReadonly);
		 */
	}
	
	// 浜庡府瀹氳嚜瀹氫箟,鏋氫妇绫诲瀷鏈夊叧绯荤殑鎿嶄綔銆�
	public final void AddDDLSysEnum(String key, int defaultVal, String desc,
			boolean isUIVisable, boolean isUIEnable, String sysEnumKey)
	{
		this.AddDDLSysEnum(key, key, defaultVal, desc, isUIVisable, isUIEnable,
				sysEnumKey, null);
	}
	
	/**
	 * /
	 * 
	 * @param key
	 * @param field
	 * @param defaultVal
	 * @param desc
	 * @param isUIVisable
	 * @param isUIEnable
	 * @param sysEnumKey
	 */
	public final void AddDDLSysEnum(String key, String field, int defaultVal,
			String desc, boolean isUIVisable, boolean isUIEnable,
			String sysEnumKey)
	{
		this.AddDDLSysEnum(key, field, defaultVal, desc, isUIVisable,
				isUIEnable, sysEnumKey, null);
	}
	
	/**
	 * 鑷畾涔夋灇涓剧被鍨�
	 * 
	 * @param key
	 *            閿�
	 * @param field
	 *            瀛楁
	 * @param defaultVal
	 *            榛樿
	 * @param desc
	 *            鎻忚堪
	 * @param sysEnumKey
	 *            Key
	 */
	public final void AddDDLSysEnum(String key, String field, int defaultVal,
			String desc, boolean isUIVisable, boolean isUIEnable,
			String sysEnumKey, String cfgVal)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(DataType.AppInt);
		attr.setMyFieldType(FieldType.Enum);
		attr.setDesc(desc);
		attr.setUIContralType(UIContralType.DDL);
		attr.setUIDDLShowType(DDLShowType.SysEnum);
		attr.setUIBindKey(sysEnumKey);
		attr.UITag = cfgVal;
		attr.setUIVisible(isUIVisable);
		attr.setUIIsReadonly(isUIEnable);
		this.Add(attr);
	}
	
	/**
	 * 鑷畾涔夋灇涓剧被鍨�
	 * 
	 * @param key
	 *            閿�
	 * @param defaultVal
	 *            榛樿
	 * @param desc
	 *            鎻忚堪
	 * @param sysEnumKey
	 *            Key
	 */
	public final void AddDDLSysEnum(String key, int defaultVal, String desc,
			boolean isUIVisable, boolean isUIEnable, String sysEnumKey,
			String cfgVals)
	{
		AddDDLSysEnum(key, key, defaultVal, desc, isUIVisable, isUIEnable,
				sysEnumKey, cfgVals);
	}
	
	public final void AddDDLSysEnum(String key, int defaultVal, String desc,
			boolean isUIVisable, boolean isUIEnable)
	{
		AddDDLSysEnum(key, key, defaultVal, desc, isUIVisable, isUIEnable, key);
	}
	
	// entities
	/**
	 * 浜庡疄浣撴湁鍏崇郴鐨勬搷浣溿��
	 * 
	 * @param key
	 *            鍋ュ��
	 * @param field
	 *            瀛楁
	 * @param defaultVal
	 *            榛樿鍊�
	 * @param dataType
	 *            DataType绫诲瀷
	 * @param desc
	 *            鎻忚堪
	 * @param ens
	 *            瀹炰綋闆嗗悎
	 * @param refKey
	 *            鍏宠仈鐨勫缓
	 * @param refText
	 *            鍏宠仈鐨凾ext
	 */
	public final void AddDDLEntities(String key, String field,
			Object defaultVal, int dataType, FieldType _fildType, String desc,
			Entities ens, String refKey, String refText, boolean uiIsEnable)
	{
		Attr attr = new Attr();
		attr.setKey(key);
		attr.setField(field);
		attr.setDefaultVal(defaultVal);
		attr.setMyDataType(dataType);
		attr.setMyFieldType(_fildType);
		
		attr.setDesc(desc);
		attr.setUIContralType(UIContralType.DDL);
		attr.setUIDDLShowType(DDLShowType.Ens);
		attr.setUIBindKey(ens.toString());
		// attr.UIBindKeyOfEn = ens.GetNewEntity.ToString();
		attr.setHisFKEns(ens);
		
		attr.setHisFKEns(ens);
		attr.setUIRefKeyText(refText);
		attr.setUIRefKeyValue(refKey);
		attr.setUIIsReadonly(uiIsEnable);
		this.Add(attr, true, false);
	}
	
	public final void AddDDLEntities(String key, String field,
			Object defaultVal, int dataType, String desc, Entities ens,
			String refKey, String refText, boolean uiIsEnable)
	{
		AddDDLEntities(key, field, defaultVal, dataType, FieldType.FK, desc,
				ens, refKey, refText, uiIsEnable);
	}
	
	/**
	 * 浜庡疄浣撴湁鍏崇郴鐨勬搷浣溿�傚瓧娈典笌灞炴�у悕绉扮浉鍚屻��
	 * 
	 * @param key
	 *            鍋ュ��
	 * @param field
	 *            瀛楁
	 * @param defaultVal
	 *            榛樿鍊�
	 * @param dataType
	 *            DataType绫诲瀷
	 * @param desc
	 *            鎻忚堪
	 * @param ens
	 *            瀹炰綋闆嗗悎
	 * @param refKey
	 *            鍏宠仈鐨勫缓
	 * @param refText
	 *            鍏宠仈鐨凾ext
	 */
	public final void AddDDLEntities(String key, Object defaultVal,
			int dataType, String desc, Entities ens, String refKey,
			String refText, boolean uiIsEnable)
	{
		AddDDLEntities(key, key, defaultVal, dataType, desc, ens, refKey,
				refText, uiIsEnable);
	}
	
	// entityNoName
	public final void AddDDLEntities(String key, Object defaultVal,
			String desc, EntitiesNoName ens, boolean uiIsEnable)
	{
		this.AddDDLEntities(key, key, defaultVal, DataType.AppString, desc,
				ens, "No", "Name", uiIsEnable);
	}
	
	public final void AddDDLEntities(String key, String field,
			Object defaultVal, String desc, EntitiesNoName ens,
			boolean uiIsEnable)
	{
		this.AddDDLEntities(key, field, defaultVal, DataType.AppString, desc,
				ens, "No", "Name", uiIsEnable);
	}
	
	// EntitiesSimpleTree
	public final void AddDDLEntities(String key, Object defaultVal,
			String desc, EntitiesSimpleTree ens, boolean uiIsEnable)
	{
		this.AddDDLEntities(key, key, defaultVal, DataType.AppString, desc,
				ens, "No", "Name", uiIsEnable);
	}
	
	public final void AddDDLEntities(String key, Object defaultVal,
			String desc, EntitiesTree ens, boolean uiIsEnable)
	{
		this.AddDDLEntities(key, key, defaultVal, DataType.AppString, desc,
				ens, "No", "Name", uiIsEnable);
	}
	
	// EntitiesOIDName
	public final void AddDDLEntities(String key, Object defaultVal,
			String desc, EntitiesOIDName ens, boolean uiIsEnable)
	{
		this.AddDDLEntities(key, key, defaultVal, DataType.AppInt, desc, ens,
				"OID", "Name", uiIsEnable);
	}
	
	public final void AddDDLEntities(String key, String field,
			Object defaultVal, String desc, EntitiesOIDName ens,
			boolean uiIsEnable)
	{
		this.AddDDLEntities(key, field, defaultVal, DataType.AppInt, desc, ens,
				"OID", "Name", uiIsEnable);
	}
	
	public final Attrs clone()
	{
		Attrs attrs = new Attrs();
		for (Attr attr : this)
		{
			attrs.Add(attr);
		}
		return attrs;
	}
	
	/**
	 * 涓嬩竴涓狝ttr 鏄惁鏄� Doc 绫诲瀷.
	 * 
	 * @param key
	 * @return
	 */
	public final Attr NextAttr(String CurrentKey)
	{
		int i = this.GetIndexByKey(CurrentKey);
		
		if (this.size() > i)
		{
			return null;
		}
		
		Attr tempVar = this.getItem(i + 1);
		return (Attr) ((tempVar instanceof Attr) ? tempVar : null);
	}
	
	public final Attr PrvAttr(String CurrentKey)
	{
		int i = this.GetIndexByKey(CurrentKey);
		
		if (this.size() < i)
		{
			return null;
		}
		
		Attr tempVar = this.getItem(i - 1);
		return (Attr) ((tempVar instanceof Attr) ? tempVar : null);
	}
	
	/**
	 * 鏄惁鍖呭惈灞炴�ey銆�
	 * 
	 * @param key
	 * @return
	 */
	public final boolean Contains(String key)
	{
		for (Attr attr : this)
		{
			if (attr.getKey().equals(key))
			{
				return true;
			}
		}
		return false;
	}
	
	public final boolean ContainsUpper(String key)
	{
		for (Attr attr : this)
		{
			if (attr.getKey().toUpperCase().equals(key.toUpperCase()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 鐗╃悊瀛楁Num
	 */
	public final int getConutOfPhysicsFields()
	{
		int i = 0;
		for (Attr attr : this)
		{
			if (attr.getMyFieldType() != FieldType.RefText)
			{
				i++;
			}
		}
		return i;
	}
	
	/*
	 * warning @Override protected void OnInsertComplete(int index, Object
	 * value) { super.OnInsertComplete(index, value); }
	 */
	
	/**
	 * 閫氳繃Key 锛� 鍙栧嚭浠栫殑Index.
	 * 
	 * @param key
	 *            Key
	 * @return index
	 */
	public final int GetIndexByKey(String key)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (this.getItem(i).getKey().equals(key))
			{
				return i;
			}
		}
		return -1;
	}
	
	public final Attr GetAttrByKey(String key)
	{
		for (Attr item : this)
		{
			if (item.getKey().equals(key))
			{
				return item;
			}
		}
		return null;
	}
	/** 灞炴�ч泦鍚�
	 
	 @param cfgKeys 绾﹀畾鐨勫瓧娈垫牸寮�
*/
//ORIGINAL LINE: public Attrs(string cfgKeys)
	public Attrs(String cfgKeys)
	{

//       *
//        *  鏍规嵁绾﹀畾鐨勬牸寮忕殑瀛楃涓茬敓鎴愰泦鍚�.
//        *  
//@Key=MyStringField;Name=鎴戠殑涓枃瀛楁;Type=String;DefVal=榛樿鍊�;AppType=Normal;IsLine=1             
//@Key=MyIntField;Name=鎴戠殑鏁板瓧瀛楁;Type=Int;DefVal=12;AppType=Normal;IsLine=false
//@Key=MyFloatField;Name=鎴戠殑娴偣瀛楁;Type=Float;DefVal=12.0;AppType=Normal;IsLine=false
//@Key=MyEnumField;Name=鎴戠殑鏋氫妇瀛楁;Type=Int;DefVal=0;AppType=Enum;IsLine=false;BindKey=[0=Yes,1=No,2=Unhnow]
//@Key=MyFKField;Name=鎴戠殑澶栭敭瀛楁;Type=String;DefVal=01;AppType=FK;IsLine=false;BindKey=BP.Port.Depts
//        * 

		String[] strs = cfgKeys.split("[@]", -1);
		for (String str : strs)
		{
			AtPara ap = new AtPara(str.replace(";", "@"));
			FieldTypeS ft = FieldTypeS.forValue(ap.GetValIntByKey("AppType"));//
			switch (ft)
			{
				case Enum:
					this.AddDDLSysEnum(ap.GetValStrByKey("Key"), ap.GetValStrByKey("Key"), ap.GetValIntByKey("DefVal"), ap.GetValStrByKey("Name"), true, true, ap.GetValStrByKey("Key"), "@"+ap.GetValStrByKey("BindKey").replace(",","@"));
					break;
				case FK:
					EntitiesNoName ens = (EntitiesNoName)BP.En.ClassFactory.GetEns(ap.GetValStrByKey("BindKey"));
					this.AddDDLEntities(ap.GetValStrByKey("Key"), ap.GetValStrByKey("DefVal"), ap.GetValStrByKey("Name"), ens,true);
					break;
				default:
//					switch (ap.GetValStrByKey("Type"))
//ORIGINAL LINE: case "String":
					if (ap.GetValStrByKey("Type").equals("String"))
					{
							this.AddTBString(ap.GetValStrByKey("Key"), ap.GetValStrByKey("DefVal"), ap.GetValStrByKey("Name"), true, false, 0, 1000, 500);
					}
//ORIGINAL LINE: case "Int":
					else if (ap.GetValStrByKey("Type").equals("Int"))
					{
							this.AddTBInt(ap.GetValStrByKey("Key"), ap.GetValIntByKey("DefVal"), ap.GetValStrByKey("Name"), true, false);
					}
//ORIGINAL LINE: case "Float":
					else if (ap.GetValStrByKey("Type").equals("Float"))
					{
							this.AddTBFloat(ap.GetValStrByKey("Key"), ap.GetValFloatByKey("DefVal"), ap.GetValStrByKey("Name"), true, false);
					}
					else
					{
					}
					break;
			}
		}
	}
	/**
	 * 灞炴�ч泦鍚�
	 */
	public Attrs()
	{
	}
	
	public final void Add(Attr attr)
	{
		if (attr.getField() == null || attr.getField().equals(""))
		{
			throw new RuntimeException("灞炴�ц缃敊璇細鎮ㄤ笉鑳借缃� key='" + attr.getKey()
					+ "',寰楀瓧娈靛�间负绌�");
		}
		
		boolean k = attr.getIsKeyEqualField();
		this.Add(attr, true, false);
	}
	
	/**
	 * 鍔犲叆涓�涓睘鎬с��
	 * 
	 * @param attr
	 *            attr
	 * @param isAddHisRefText
	 *            isAddHisRefText
	 */
	public final void Add(Attr attr, boolean isAddHisRefText,
			boolean isAddHisRefName)
	{
		for (Attr myattr : this)
		{
			if (myattr.getKey().equals(attr.getKey()))			
				return;
			
		}
		
		this.add(attr);
		/*
		 * warning this.add(attr);
		 */
		
		if (isAddHisRefText)
		{
			this.AddRefAttrText(attr);
		}
		
		if (isAddHisRefName)
		{
			this.AddRefAttrName(attr);
		}
	}
	
	private void AddRefAttrText(Attr attr)
	{
		if (attr.getMyFieldType() == FieldType.FK
				|| attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum
				|| attr.getMyFieldType() == FieldType.PKFK
				|| attr.getMyFieldType() == FieldType.BindTable)
		{
			
			Attr myattr = new Attr();
			myattr.setMyFieldType(FieldType.RefText);
			myattr.setMyDataType(DataType.AppString);
			myattr.setUIContralType(UIContralType.TB);
			myattr.setUIWidth(attr.getUIWidth() * 2);
			myattr.setKey(attr.getKey() + "Text");
			
			myattr.setUIIsReadonly(true);
			myattr.setUIBindKey(attr.getUIBindKey());
			// myattr.UIBindKeyOfEn = attr.UIBindKeyOfEn;
			myattr.setHisFKEns(attr.getHisFKEns());
			
			// myattr.Desc=attr.Desc+"鍚嶇О";
			
			String desc = "鍚嶇О";
			myattr.setDesc(desc);
			/*
			 * warning string desc=myattr.Desc="鍚嶇О";
			 */
			if (desc.indexOf("缂栧彿") >= 0)
			{
				myattr.setDesc(attr.getDesc().replace("缂栧彿", "鍚嶇О"));
			} else
			{
				myattr.setDesc(attr.getDesc() + "鍚嶇О");
			}
			
			if (attr.getUIContralType() == UIContralType.DDL)
			{
				myattr.setUIVisible(false);
			}
			
			this.add(myattr);
			/*
			 * warning this.add(myattr);
			 */
			
			// this.Add(myattr,true);
		}
	}
	
	private void AddRefAttrName(Attr attr)
	{
		if (attr.getMyFieldType() == FieldType.FK
				|| attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum
				|| attr.getMyFieldType() == FieldType.PKFK)
		{
			
			Attr myattr = new Attr();
			myattr.setMyFieldType(FieldType.Normal);
			myattr.setMyDataType(DataType.AppString);
			myattr.setUIContralType(UIContralType.TB);
			myattr.setUIWidth(attr.getUIWidth() * 2);
			
			myattr.setKey(attr.getKey() + "Name");
			myattr.setField(attr.getKey() + "Name");
			
			myattr.setMaxLength(200);
			myattr.setMinLength(0);
			
			myattr.setUIVisible(false);
			myattr.setUIIsReadonly(true);
			
			myattr.setDesc("Name");
			myattr.setDesc(myattr.getDesc());
			this.add(myattr);
			/*
			 * warning this.add(myattr);
			 */
		}
	}
	

	/**
	 * 鏍规嵁绱㈠紩璁块棶闆嗗悎鍐呯殑鍏冪礌Attr銆�
	 */
	public final Attr getItem(int index)
	{
		return (Attr) this.get(index);		
	}
	public final BP.Sys.MapAttrs ToMapAttrs() 
	{		

		BP.Sys.MapAttrs mapAttrs = new BP.Sys.MapAttrs();
        for (Attr item : this)
        {
            if (item.getMyFieldType() == FieldType.RefText)
                continue;

         //   if (item.IsPK == true && item.UIVisible == false)
           //     continue;

            BP.Sys.MapAttr mattr = new BP.Sys.MapAttr();
            mattr.setKeyOfEn(  item.getKey());
            mattr.setName( item.getDesc());
            mattr.setMyDataType(item.getMyDataType()); 
            mattr.setUIContralType(item.getUIContralType());
            mattr.setUIBindKey(item.getUIBindKey());

            //@于庆海，这里需要翻译.
            mattr.setUIWidth(item.getUIWidthInt());
            mattr.setUIHeightInt(item.getUIHeight());

            mattr.setMaxLen(item.getMaxLength());
            mattr.setMinLen(item.getMinLength());
            mattr.setUIVisible(item.getUIVisible());
            mattr.setDefValReal(item.getDefaultValOfReal());

            mattr.setUIIsEnable(item.getUIIsReadonly());
            if (item.getMyFieldType() == FieldType.Normal 
            		|| item.getMyFieldType() == FieldType.PK)
            {
                if (item.getMyDataType() == DataType.AppInt ||
                    item.getMyDataType() == DataType.AppFloat ||
                    item.getMyDataType() == DataType.AppDouble ||
                    item.getMyDataType() == DataType.AppMoney ||
                    item.getMyDataType() == DataType.AppString ||
	                item.getMyDataType() == DataType.AppDate ||
	                item.getMyDataType() == DataType.AppDateTime)
                {
                    mattr.setUIIsEnable(!item.getUIIsReadonly()) ; 
                }
            }

            if (item.UIIsLine == true)
                mattr.setColSpan(3);

            //帮助url.
            mattr.setUIRefKeyText(item.HelperUrl);

            //if (item.UIIsReadonly == true && item.MyFieldType== FieldType.Normal)
            //    mattr.UIIsEnable = !item.UIIsReadonly;
            //else
            //    mattr.UIIsEnable = item.UIIsReadonly;
           // else
             //   mattr.UIIsEnable = !item.UIIsReadonly;



            if (item.getMyFieldType() == FieldType.Enum)
                mattr.setLGType(FieldTypeS.Enum)  ;

            if (item.getMyFieldType() == FieldType.FK)
                mattr.setLGType( FieldTypeS.FK) ;

            mapAttrs.AddEntity(mattr);
        }
	
        return mapAttrs;
     }
 }
	
	

 