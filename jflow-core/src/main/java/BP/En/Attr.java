package BP.En;

import BP.Web.Controls.*;
import BP.DA.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 属性
*/
public class Attr
{
	public final BP.Sys.MapAttr getToMapAttr()
	{

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();

		attr.setKeyOfEn(this.getKey());
		attr.setName(this.getDesc());
		attr.setDefVal(this.getDefaultVal().toString());
		attr.setKeyOfEn(this.getField());

		attr.setMaxLen(this.getMaxLength());
		attr.setMinLen(this.getMinLength());
		attr.setUIBindKey(this.getUIBindKey());
		attr.setUIIsLine(this.UIIsLine);
		attr.setUIHeight(0);

		if (this.getMaxLength() > 3000)
		{
			attr.setUIHeight(10);
		}

		attr.setUIWidth(this.getUIWidth());
		attr.setMyDataType(this.getMyDataType());

		attr.setUIRefKey(this.getUIRefKeyValue());

		attr.setUIRefKeyText(this.getUIRefKeyText());
		attr.setUIVisible(this.getUIVisible());

			//if (this.IsPK)
			//    attr.setMyDataType ( = FieldType.PK;
			//    attr.MyFieldType = FieldType.PK;

		switch (this.getMyFieldType())
		{
			case Enum:
			case PKEnum:
				attr.setUIContralType(this.getUIContralType());
				attr.setLGType(FieldTypeS.Enum);
				attr.setUIIsEnable(this.getUIIsReadonly());
				break;
			case FK:
			case PKFK:
				attr.setUIContralType(this.getUIContralType());
				attr.setLGType(FieldTypeS.FK);
					//attr.setMyDataType ((int)FieldType.FK;
				attr.setUIRefKey("No");
				attr.setUIRefKeyText("Name");
				attr.setUIIsEnable(this.getUIIsReadonly());
				break;
			default:
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);

				attr.setUIIsEnable(!this.getUIIsReadonly());
				switch (this.getMyDataType())
				{
					case DataType.AppBoolean:
						attr.setUIContralType(UIContralType.CheckBok);
						attr.setUIIsEnable(this.getUIIsReadonly());
						break;
					case DataType.AppDate:
							//if (this.Tag == "1")
							//    attr.DefaultVal = DataType.CurrentData;
						break;
					case DataType.AppDateTime:
							//if (this.Tag == "1")
							//    attr.DefaultVal = DataType.CurrentData;
						break;
					default:
						break;
				}
				break;
		}

			//attr.HisAutoFull = this.AutoFullWay;
			//attr.AutoFullDoc = this.AutoFullDoc;
		return attr;
	}
	public final BP.Web.Controls.TBType getHisTBType()
	{
		switch (this.getMyDataType())
		{
			case BP.DA.DataType.AppMoney:
				return BP.Web.Controls.TBType.Moneny;
			case BP.DA.DataType.AppInt:
			case BP.DA.DataType.AppFloat:
			case BP.DA.DataType.AppDouble:
				return BP.Web.Controls.TBType.Num;
			default:
				return BP.Web.Controls.TBType.TB;
		}
	}
	public final boolean getIsFK()
	{
		if (this.getMyFieldType() == FieldType.FK || this.getMyFieldType() == FieldType.PKFK)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public final boolean getIsFKorEnum()
	{
		if (this.getMyFieldType() == FieldType.Enum || this.getMyFieldType() == FieldType.PKEnum || this.getMyFieldType() == FieldType.FK || this.getMyFieldType() == FieldType.PKFK)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 是不是能使用默认值。
	*/
	public final boolean getIsCanUseDefaultValues()
	{
		if (this.getMyDataType() == DataType.AppString && this.getUIIsReadonly() == false)
		{
			return true;
		}
		return false;
	}
	public final boolean getIsNum()
	{
		if (getMyDataType() == DataType.AppBoolean || getMyDataType() == DataType.AppDouble || getMyDataType() == DataType.AppFloat || getMyDataType() == DataType.AppInt || getMyDataType() == DataType.AppMoney)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public final boolean getIsEnum()
	{
		if (getMyFieldType() == FieldType.Enum || getMyFieldType() == FieldType.PKEnum)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public final boolean getIsRefAttr()
	{
		if (this.getMyFieldType() == FieldType.RefText)
		{
			return true;
		}
		return false;
	}
	/** 
	 计算属性是不是PK
	*/
	public final boolean getIsPK()
	{
		if (getMyFieldType() == FieldType.PK || getMyFieldType() == FieldType.PKFK || getMyFieldType() == FieldType.PKEnum)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private int _IsKeyEqualField = -1;
	public final boolean getIsKeyEqualField()
	{
		if (_IsKeyEqualField == -1)
		{
			if (this.getKey().equals(this.getField()))
			{
				_IsKeyEqualField = 1;
			}
			else
			{
				_IsKeyEqualField = 0;
			}
		}

		if (_IsKeyEqualField == 1)
		{
			return true;
		}
		return false;
	}
	/** 
	 输入描述
	*/
	public final String getEnterDesc()
	{
		if (this.getUIContralType() == UIContralType.TB)
		{
			if (this.getUIIsReadonly() || this.getUIVisible() == false)
			{
				return "此字段只读";
			}
			else
			{
				if (this.getMyDataType() == DataType.AppDate)
				{
					return "输入日期类型" + DataType.getSysDataFormat();
				}
				else if (this.getMyDataType() == DataType.AppDateTime)
				{
					return "输入日期时间类型" + DataType.getSysDataTimeFormat();
				}
				else if (this.getMyDataType() == DataType.AppString)
				{
					return "输入要求最小长度" + this.getMinLength() + "字符，最大长度" + this.getMaxLength() + "字符";
				}
				else if (this.getMyDataType() == DataType.AppMoney)
				{
					return "金额类型 0.00";
				}
				else
				{
					return "输入数值类型";
				}
			}

		}
		else if (this.getUIContralType() == UIContralType.DDL || this.getUIContralType() == UIContralType.CheckBok)
		{
			if (this.getUIIsReadonly())
			{
				return "此字段只读";
			}
			else
			{
				if (this.getMyDataType() == DataType.AppBoolean)
				{
					return "是/否";
				}
				else
				{
					return "列表选择";
				}
			}
		}

		return "";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	public Attr()
	{
	}
	/** 
	 构造函数
	 
	 @param key
	 @param field
	 @param defaultVal
	 @param dataType
	 @param isPK
	 @param desc
	*/
	public Attr(String key, String field, Object defaultVal, int dataType, boolean isPK, String desc, int minLength, int maxlength)
	{
		this._key = key;
		this._field = field;
		this._desc = desc;
		if (isPK)
		{
			this.setMyFieldType(FieldType.PK);
		}
		this._dataType = dataType;
		this._defaultVal = defaultVal;
		this._minLength = minLength;
		this._maxLength = maxlength;
	}
	public Attr(String key, String field, Object defaultVal, int dataType, boolean isPK, String desc)
	{
		this._key = key;
		this._field = field;
		this._desc = desc;
		if (isPK)
		{
			this.setMyFieldType(FieldType.PK);
		}
		this._dataType = dataType;
		this._defaultVal = defaultVal;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public String HelperUrl = null;
	public AutoFullWay AutoFullWay ;
	public String AutoFullDoc = null;
	/** 
	 属性名称
	*/
	private String _key = null;
	/** 
	 属性名称
	*/
	public final String getKey()
	{
		return this._key;
	}
	public final void setKey(String value)
	{
		if (value != null)
		{
			this._key = value.trim();
		}
	}
	/** 
	 属性对应的字段
	*/
	private String _field = null;
	/** 
	 属性对应的字段
	 
	 @return 
	*/
	public final String getField()
	{
		return this._field;
	}
	public final void setField(String value)
	{
		if (value != null)
		{
			this._field = value.trim();
		}
	}
	/** 
	 字段默认值
	*/
	private Object _defaultVal = null;
	public final String getDefaultValOfReal()
	{
		if (_defaultVal == null)
		{
			return null;
		}
		return _defaultVal.toString();
	}
	public final void setDefaultValOfReal(String value)
	{
		_defaultVal = value;
	}
	/** 
	 字段默认值
	*/
	public final Object getDefaultVal()
	{
		switch (this.getMyDataType())
		{
			case DataType.AppString :
				if (this._defaultVal == null)
				{
					return "";
				}
				break;
			case DataType.AppInt :
				if (this._defaultVal == null)
				{
					return 0;
				}
				try
				{
					return Integer.parseInt(this._defaultVal.toString());
				}
				catch (java.lang.Exception e)
				{
					return 0;
						//throw new Exception("@设置["+this.Key+"]默认值出现错误，["+_defaultVal.ToString()+"]不能向 int 转换。");
				}
			case DataType.AppMoney :
				if (this._defaultVal == null)
				{
					return 0;
				}
				try
				{
					return Float.parseFloat(this._defaultVal.toString());
				}
				catch (java.lang.Exception e2)
				{
					return 0;
					//	throw new Exception("@设置["+this.Key+"]默认值出现错误，["+_defaultVal.ToString()+"]不能向 AppMoney 转换。");
				}
			case DataType.AppFloat :
				if (this._defaultVal == null)
				{
					return 0;
				}
				try
				{
					return Float.parseFloat(this._defaultVal.toString());
				}
				catch (java.lang.Exception e3)
				{
					return 0;
					//	throw new Exception("@设置["+this.Key+"]默认值出现错误，["+_defaultVal.ToString()+"]不能向 float 转换。");
				}

			case DataType.AppBoolean :
				if (this._defaultVal == null || this._defaultVal.toString().equals(""))
				{
					return 0;
				}
				try
				{
					if (DataType.StringToBoolean(this._defaultVal.toString()))
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
				catch (java.lang.Exception e4)
				{
					throw new RuntimeException("@设置[" + this.getKey() + "]默认值出现错误，[" + this._defaultVal.toString() + "]不能向 bool 转换，请设置0/1。");
				}

			case 5:
				if (this._defaultVal == null)
				{
					return 0;
				}
				try
				{
					return Double.parseDouble(this._defaultVal.toString());
				}
				catch (java.lang.Exception e5)
				{
					throw new RuntimeException("@设置[" + this.getKey() + "]默认值出现错误，[" + _defaultVal.toString() + "]不能向 double 转换。");
				}

			case DataType.AppDate :
				if (this._defaultVal == null)
				{
					return "";
				}
				break;
			case DataType.AppDateTime :
				if (this._defaultVal == null)
				{
					return "";
				}
				break;
			default:
				throw new RuntimeException("@bulider insert sql error: 没有这个数据类型，字段名称:" + this.getDesc() + " 英文:" + this.getKey());
		}
		return this._defaultVal;
	}

	public final void setDefaultVal(Object value)
	{
		this._defaultVal = value;
	}
	/** 
	 数据类型。
	*/
	private int _dataType = 0;
	/** 
	 数据类型。
	*/
	public final int getMyDataType()
	{
		return this._dataType;
	}
	public final void setMyDataType(int value)
	{
		this._dataType = value;
	}
	public final String getMyDataTypeStr()
	{
		return DataType.GetDataTypeDese(this.getMyDataType());
	}

	/** 
	 是不是主键。
	*/
	private FieldType _FieldType = FieldType.Normal;
	/** 
	 是不是主键
	 
	 @return  yes / no
	*/
	public final FieldType getMyFieldType()
	{
		return this._FieldType;
	}
	public final void setMyFieldType(FieldType value)
	{
		this._FieldType = value;
	}
	/** 
	 描述。
	*/
	private String _desc = null;
	public final String getDesc()
	{
		return this._desc;
	}
	public final void setDesc(String value)
	{
		this._desc = value;
	}
	/** 
	 在线帮助
	*/
	public final String getDescHelper()
	{
		if (this.HelperUrl == null)
		{
			return this._desc;
		}

		if (this.HelperUrl.contains("script"))
		{
			return "<a href=\"" + this.HelperUrl + "\"  ><img src='../../Img/Help.png'  height='20px' border=0/>" + this._desc + "</a>";
		}
		else
		{
			return "<a href=\"" + this.HelperUrl + "\" target=_blank ><img src='../../Img/Help.png'  height='20px' border=0/>" + this._desc + "</a>";
		}
	}
	public final String getDescHelperIcon()
	{
		if (this.HelperUrl == null)
		{
			return this._desc;
		}
		return "<a href=\"" + this.HelperUrl + "\" ><img src='../../Img/Help.png' height='20px' border=0/></a>";
	}
	/** 
	 最大长度。
	*/
	private int _maxLength = 4000;
	/** 
	 最大长度。
	*/
	public final int getMaxLength()
	{
		switch (this.getMyDataType())
		{
			case DataType.AppDate:
				return 50;
			case DataType.AppDateTime:
				return 50;
			case DataType.AppString:
				if (this.getIsFK())
				{
					return 100;
				}
				else
				{
					if (this._maxLength == 0)
					{
						return 50;
					}
					return this._maxLength;
				}
			default:
				if (this.getIsFK())
				{
					return 100;
				}
				else
				{
					return this._maxLength;
				}
		}
	}
	public final void setMaxLength(int value)
	{
		this._maxLength = value;
	}
	/** 
	 最小长度。
	*/
	private int _minLength = 0;
	/** 
	 最小长度。
	*/
	public final int getMinLength()
	{
		return this._minLength;
	}
	public final void setMinLength(int value)
	{
		this._minLength = value;
	}
	/** 
	 是否可以为空, 对数值类型的数据有效.
	*/
	public final boolean getIsNull()
	{
		if (this.getMinLength() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region UI 的扩展属性
	public final int getUIWidthInt()
	{
		return (int)this.getUIWidth();
	}
	private float _UIWidth = 80;
	/** 
	 宽度
	*/
	public final float getUIWidth()
	{
		if (this._UIWidth <= 10)
		{
			return 15;
		}
		else
		{
			return this._UIWidth;
		}
	}
	public final void setUIWidth(float value)
	{
		this._UIWidth = value;
	}

	private int _UIHeight = 0;
	/** 
	 高度
	*/
	public final int getUIHeight()
	{
		return this._UIHeight * 10;
	}
	public final void setUIHeight(int value)
	{
		this._UIHeight = value;
	}

	private boolean _UIVisible = true;
	/** 
	 是不是可见
	*/
	public final boolean getUIVisible()
	{
		return this._UIVisible;
	}
	public final void setUIVisible(boolean value)
	{
		this._UIVisible = value;
	}


	/** 
	 是否单行显示
	*/
	public boolean UIIsLine = false;
	private boolean _UIIsReadonly = false;
	/** 
	 是不是只读
	*/
	public final boolean getUIIsReadonly()
	{
		return this._UIIsReadonly;
	}
	public final void setUIIsReadonly(boolean value)
	{
		this._UIIsReadonly = value;
	}
	private UIContralType _UIContralType = UIContralType.TB;
	/** 
	 控件类型。
	*/
	public final UIContralType getUIContralType()
	{
		return this._UIContralType;
	}
	public final void setUIContralType(UIContralType value)
	{
		this._UIContralType = value;
	}
	private String _UIBindKey = null;
	/** 
	 要Bind 的Key.
	 在TB 里面就是 DataHelpKey
	 在DDL 里面是  SelfBindKey.
	*/
	public final String getUIBindKey()
	{
		return this._UIBindKey;
	}
	public final void setUIBindKey(String value)
	{
		this._UIBindKey = value;
	}
	private String _UIBindKeyOfEn = null;
	public final boolean getUIIsDoc()
	{
		if (this.getUIHeight() != 0 && this.getUIContralType() == UIContralType.TB)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private Entity _HisFKEn = null;
	public final Entity getHisFKEn()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#warning new a entity.

	   return this.getHisFKEns().getNewEntity();

	 
	}
	private Entities _HisFKEns = null;
	/** 
	 它关联的ens.这个只有在,这个属性是fk, 时有效。
	*/
	public final Entities getHisFKEns()
	{
		if (_HisFKEns == null)
		{

			if (this.getMyFieldType() == FieldType.Enum || this.getMyFieldType() == FieldType.PKEnum)
			{
				return null;
			}
			else if (this.getMyFieldType() == FieldType.FK || this.getMyFieldType() == FieldType.PKFK)
			{
				if (this.getUIBindKey().contains("."))
				{
					_HisFKEns = ClassFactory.GetEns(this.getUIBindKey());
				}
				else
				{
					_HisFKEns = new GENoNames(this.getUIBindKey(), this.getDesc()); // ClassFactory.GetEns(this.UIBindKey);
				}
			}
			else
			{
				return null;
			}
		}
		return _HisFKEns;
	}
	public final void setHisFKEns(Entities value)
	{
		_HisFKEns = value;
	}
	private TBType _TBShowType = TBType.TB;
	/** 
	 要现实的控件类型。
	*/
	public final TBType getUITBShowType()
	{
		if (this.getMyDataType() == DataType.AppDate)
		{
			return TBType.Date;
		}
		else if (this.getMyDataType() == DataType.AppFloat)
		{
			return TBType.Float;
		}
		else if (this.getMyDataType() == DataType.AppBoolean)
		{
			return TBType.Date; //throw new Exception("@属性配置错误。");
		}
		else if (this.getMyDataType() == DataType.AppDouble)
		{
			return TBType.Decimal;
		}
		else if (this.getMyDataType() == DataType.AppInt)
		{
			return TBType.Num;
		}
		else if (this.getMyDataType() == DataType.AppMoney)
		{
			return TBType.Moneny;
		}
		else
		{
			return _TBShowType;
		}
	}
	public final void setUITBShowType(TBType value)
	{
		this._TBShowType = value;
	}
	private DDLShowType _UIDDLShowType = DDLShowType.None;
	/** 
	 要现实的控件类型。
	*/
	public final DDLShowType getUIDDLShowType()
	{
		if (this.getMyDataType() == DataType.AppBoolean)
		{
			return DDLShowType.Boolean;
		}
		else
		{
			return this._UIDDLShowType;
		}
	}
	public final void setUIDDLShowType(DDLShowType value)
	{
		this._UIDDLShowType = value;
	}

	private String _UIRefKey = null;
	/** 
	 要Bind 的Key. 在TB 里面就是 DataHelpKey 
	 在DDL 里面是SelfBindKey.
	*/
	public final String getUIRefKeyValue()
	{
		return this._UIRefKey;
	}
	public final void setUIRefKeyValue(String value)
	{
		this._UIRefKey = value;
	}
	private String _UIRefText = null;
	/** 
	 关联的实体valkey	 
	*/
	public final String getUIRefKeyText()
	{
		return this._UIRefText;
	}
	public final void setUIRefKeyText(String value)
	{
		this._UIRefText = value;
	}
	public String UITag = null;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}