package BP.En;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import BP.DA.Cash;
import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Paras;
import BP.Sys.SysDocFile;

import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

/**
 * Entity 的摘要说明。
 */
public abstract class EnObj implements Serializable {
	 
	// 访问控制.
	private String _DBVarStr = null;

	public final String getHisDBVarStr() {
		if (_DBVarStr != null) {
			return _DBVarStr;
		}

		_DBVarStr = this.getEnMap().getEnDBUrl().getDBVarStr();
		return _DBVarStr;
	}

	/**
	 * 他的访问控制.
	 */
	protected UAC _HisUAC = null;

	/**
	 * 得到 uac 控制.
	 * 
	 * @return
	 * @throws Exception
	 */
	public UAC getHisUAC() throws Exception {
		if (_HisUAC == null) {
			_HisUAC = new UAC();
			// _HisUAC.OpenForSysAdmin();
			if (BP.Web.WebUser.getNo().equals("admin")) {
				_HisUAC.IsAdjunct = false;
				_HisUAC.IsDelete = true;
				_HisUAC.IsInsert = true;
				_HisUAC.IsUpdate = true;
				_HisUAC.IsView = true;
			}

		}
		return _HisUAC;
	}

	// 取出外部配置的属性信息
	/**
	 * 取出Map 的扩展属性。 用于第3方的扩展属性开发。
	 * 
	 * @param key
	 *            属性Key
	 * @return 设置的属性
	 * @throws Exception
	 */
	public final String GetMapExtAttrByKey(String key) throws Exception {
		Paras ps = new Paras();
		ps.Add("enName", this.toString());
		ps.Add("key", key);

		return (String) DBAccess.RunSQLReturnVal(
				"select attrValue from Sys_ExtMap WHERE className=" + SystemConfig.getAppCenterDBVarStr()
						+ "enName AND attrKey=" + SystemConfig.getAppCenterDBVarStr() + "key",
				ps);
	}

	public String toString() {
		return this.getClass().getName();
	}

	// CreateInstance
	/**
	 * 创建一个实例
	 * 
	 * @return 自身的实例
	 */
	public final Entity CreateInstance() {
		/*
		 * warning Object tempVar =
		 * this.getClass().Assembly.CreateInstance(this.toString());
		 */
		Object tempVar = null;
		try {
			tempVar = this.getClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Entity) ((tempVar instanceof Entity) ? tempVar : null);
		// return ClassFactory.GetEn(this.ToString());
	}

	private Entities _GetNewEntities = null;

	private final void ResetDefaultValRowValues() throws Exception {

		  if (this.get_enMap() == null)
              return ;
		  
		for (Attr attr : this.get_enMap().getAttrs()) {
			
			String key=attr.getKey();			

			String v = this.GetValStringByKey(key,null);

			if (v == null || v.indexOf('@') ==-1)
				continue;

			// 设置默认值.
			if (v.equals("@WebUser.No")) {

				this.SetValByKey(key, WebUser.getNo());

				continue;
			} else if (v.equals("@WebUser.Name")) {

				this.SetValByKey(key, WebUser.getName());

				continue;
			} else if (v.equals("@WebUser.FK_Dept")) {

				this.SetValByKey(key, WebUser.getFK_Dept());

				continue;
			} else if (v.equals("@WebUser.FK_DeptName")) {

				this.SetValByKey(key, WebUser.getFK_DeptName());

				continue;
			} else if (v.equals("@WebUser.FK_DeptNameOfFull")) {

				this.SetValByKey(key, WebUser.getFK_DeptNameOfFull());

				continue;
			} else if (v.equals("@RDT")) {

				//Attr attr = this.getEnMap().GetAttrByKey(key);

				if (attr.getMyDataType() == DataType.AppDate) {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
				}

				if (attr.getMyDataType() == DataType.AppDateTime) {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
				}
				continue;
			} else {
				continue;
			}

		}
	}

	// 方法
	/**
	 * 重新设置默信息.
	 * 
	 * @throws Exception
	 */
	public final void ResetDefaultVal() throws Exception {
		
		ResetDefaultValRowValues();
		
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {

            //含有特定值时取消重新设定默认值
			 String v = this.GetValStringByKey(attr.getKey(), null);  // this._row[key] as string;

             if (v !=null  && v.contains("@")==false)
                 continue;
             
			String tempVar = attr.getDefaultValOfReal();
			v = (String) ((tempVar instanceof String) ? tempVar : null);
			if (v == null) {
				continue;
			}

			if (attr.getDefaultValOfReal().contains("@") == false) {
				 
				String val = this.GetValStrByKey(attr.getKey());
				if (val == null || val == "")
					this.SetValByKey(attr.getKey(), attr.getDefaultVal());

				continue;
			}

			String myval = this.GetValStrByKey(attr.getKey());
			// 设置默认值.
			if (v.equals("@WebUser.No")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getNo());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getNo());
					}
				}
				continue;
			} else if (v.equals("@WebUser.Name")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getName());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getName());
					}
				}
				continue;
			} else if (v.equals("@WebUser.FK_Dept")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
					}
				}
				continue;
			} else if (v.equals("@WebUser.FK_DeptName")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
					}
				}
				continue;
			} else if (v.equals("@WebUser.FK_DeptNameOfFull")) {
				if (attr.getUIIsReadonly()) {
					this.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
					}
				}
				continue;
			} else if (v.equals("@RDT")) {
				if (attr.getUIIsReadonly()) {
					if (attr.getMyDataType() == DataType.AppDate || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
					}

					if (attr.getMyDataType() == DataType.AppDateTime || v.equals(myval)) {
						this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
					}
				} else {
					if (StringHelper.isNullOrEmpty(myval) || v.equals(myval)) {
						if (attr.getMyDataType() == DataType.AppDate) {
							this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
						} else {
							this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
						}
					}
				}
				continue;
			} else {
				continue;
			}
		}
	}

	/**
	 * 把所有的值都设置成默认值，但是主键除外。
	 * 
	 * @throws Exception
	 */
	public final void ResetDefaultValAllAttr() throws Exception {
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (!attr.getUIIsReadonly() && attr.getDefaultValOfReal() != null) {
				continue;
			}

			if (attr.getIsPK()) {
				continue;
			}

			String tempVar = attr.getDefaultValOfReal();
			String v = (String) ((tempVar instanceof String) ? tempVar : null);
			if (v == null) {
				this.SetValByKey(attr.getKey(), "");
				continue;
			}

			if (!v.contains("@") || v.equals("0") || v.equals("0.00")) {
				this.SetValByKey(attr.getKey(), v);
				continue;
			}

			// 设置默认值.
			if (v.equals("WebUser.No")) {
				this.SetValByKey(attr.getKey(), WebUser.getNo());
				continue;
			} else if (v.equals("@WebUser.Name")) {
				this.SetValByKey(attr.getKey(), WebUser.getName());
				continue;
			} else if (v.equals("@WebUser.FK_Dept")) {
				this.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
				continue;
			} else if (v.equals("@WebUser.FK_DeptName")) {
				this.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
				continue;
			} else if (v.equals("@WebUser.FK_DeptNameOfFull")) {
				this.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
				continue;
			} else if (v.equals("@RDT")) {
				if (attr.getMyDataType() == DataType.AppDate) {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart("yyyy-MM-dd"));
				} else {
					this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
				}
				continue;
			} else {
				continue;
			}
		}
	}

	// 构造
	/**
	 * 实体
	 */
	public EnObj() {
	}

	private Map _tmpEnMap = null;

	/**
	 * Map
	 */
	protected final Map get_enMap() {
		
		if (_tmpEnMap != null) {
			return _tmpEnMap;
		}

		Map obj = Cash.GetMap(this.toString());
		if (obj == null) {
			if (_tmpEnMap == null) {
				return null;
			} else {
				return _tmpEnMap;
			}
		} else {
			_tmpEnMap = obj;
		}
		return _tmpEnMap;
	}

	protected final void set_enMap(Map value) {
		if (value == null) {
			_tmpEnMap = null;
			return;
		}

		Map mp = (Map) value;
		if (SystemConfig.getIsDebug()) {
			// 检查map 是否合理。
			// if (mp != null)
			// {
			// int i = 0;
			// foreach (Attr attr in this.getEnMap().getAttrs())
			// {
			// if (attr.MyFieldType == FieldType.PK || attr.MyFieldType ==
			// FieldType.PKEnum || attr.MyFieldType == FieldType.PKFK)
			// i++;
			// }
			// if (i == 0)
			// throw new Exception("@没有给【" + this.EnDesc + "】定义主键。");

			// if (this.IsNoEntity)
			// {
			// if (!mp.Attrs.Contains("No"))
			// throw new Exception("@EntityNo 类map中没有 No 属性。@类" + mp.EnDesc +
			// " , " + this.ToString());

			// if (i != 1)
			// throw new Exception("@多个主键在 EntityNo 类中是不允许的。 @类" + mp.EnDesc +
			// " , " + this.ToString());
			// }
			// else if (this.IsOIDEntity)
			// {
			// if (!mp.Attrs.Contains("OID"))
			// throw new Exception("@EntityOID 类map中没有 OID 属性。@类" + mp.EnDesc +
			// " , " + this.ToString());
			// if (i != 1)
			// throw new Exception("@多个主键在 EntityOID 类中是不允许的。 @类" + mp.EnDesc +
			// " , " + this.ToString());
			// }
			// else
			// {
			// if (mp.Attrs.Contains("MyPK"))
			// if (i != 1)
			// throw new Exception("@多个主键在 EntityMyPK 类中是不允许的。 @类" + mp.EnDesc +
			// " , " + this.ToString());
			// }
			// }
			// 检查map 是否合理。
		}

		if (mp == null || mp.getDepositaryOfMap() == Depositary.None) {
			_tmpEnMap = mp;
			return;
		}
		Cash.SetMap(this.toString(), mp);
		_tmpEnMap = mp;
	}

	/**
	 * 子类需要继承
	 */
	public abstract Map getEnMap();

	/**
	 * 动态的获取map
	 */
	public Map getEnMapInTime() {
		_tmpEnMap = null;
		Cash.SetMap(this.getClass().getName(), null);
		return this.getEnMap();
	}

	/**
	 * 实体的 map 信息。
	 * 
	 * //public abstract void EnMap();
	 */
	private Row _row = null;

	public final Row getRow() {
		if (this._row == null) {
			this._row = new Row();
			this._row.LoadAttrs(this.getEnMap().getAttrs());
		}
		return this._row;
	}

	public final Row getRow2017() {
		if (this._row == null) {
			this._row = new Row();
			this._row.LoadAttrs2017(this.getEnMap().getAttrs());
		}
		return this._row;
	}

	public final void setRow(Row value) {
		this._row = value;
	}

	// 关于属性的操作。

	// 设置值方法
	public final void SetValByKeySuperLink(String attrKey, String val) {
		this.SetValByKey(attrKey, DataType.DealSuperLink(val));
	}

	/**
	 * 设置object类型的值
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValByKey(String attrKey, String val) {
		if (val == null || val.equals("&nbsp;")) {
			val = "";
		} else if (attrKey.equals("RDT")) {
			if (val.length() > 4) {
				this.SetValByKey("FK_NY", val.substring(0, 7));
				this.SetValByKey("FK_ND", val.substring(0, 4));
			}
		}
		this.getRow().SetValByKey(attrKey, val);
	}

	/**
	 * 设置object类型的值
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValByKey2017(String attrKey, String val) {
		if (val == null || val.equals("&nbsp;")) {
			val = "";
		} else if (attrKey.equals("RDT")) {
			if (val.length() > 4) {
				this.SetValByKey("FK_NY", val.substring(0, 7));
				this.SetValByKey("FK_ND", val.substring(0, 4));
			}
		} else {
		}
		this.getRow2017().SetValByKey_2017(attrKey, val);
	}

	public final void SetValByKey(String attrKey, int val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, long val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey_2017(String attrKey, long val) {
		this.getRow().SetValByKey_2017(attrKey, val);
	}

	public final void SetValByKey(String attrKey, float val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, java.math.BigDecimal val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByKey(String attrKey, Object val) {
		this.getRow().SetValByKey(attrKey, val);
	}

	// 区分大小写
	public final void SetValByKey_2017(String attrKey, Object val) {
		this.getRow2017().SetValByKey_2017(attrKey, val);
	}

	public final void SetValByDesc(String attrDesc, Object val) {
		if (val == null) {
			throw new RuntimeException("@不能设置属性[" + attrDesc + "]null 值。");
		}
		this.getRow().SetValByKey(this.getEnMap().GetAttrByDesc(attrDesc).getKey(), val);
	}

	/**
	 * 设置关联类型的值
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValRefTextByKey(String attrKey, Object val) {
		this.SetValByKey(attrKey + "Text", val);
	}

	/**
	 * 设置bool类型的值
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetValByKey(String attrKey, boolean val) {
		if (val) {
			this.SetValByKey(attrKey, 1);
		} else {
			this.SetValByKey(attrKey, 0);
		}
	}

	/**
	 * 设置默认值
	 */
	public final void SetDefaultVals() {
		for (Attr attr : this.getEnMap().getAttrs()) {
			this.SetValByKey(attr.getKey(), attr.getDefaultVal());
		}
	}

	/**
	 * 设置日期类型的值
	 * 
	 * @param attrKey
	 *            attrKey
	 * @param val
	 *            val
	 */
	public final void SetDateValByKey(String attrKey, String val) {
		try {
			this.SetValByKey(attrKey, DataType.StringToDateStr(val));
		} catch (RuntimeException ex) {
			throw new RuntimeException("@不合法的日期数据格式:key=[" + attrKey + "],value=" + val + " " + ex.getMessage());
		}
	}

	// 取值方法
	/**
	 * 取得Object
	 * 
	 * @param attrKey
	 * @return
	 */
	public final Object GetValByKey(String attrKey) {
		return this.getRow().GetValByKey(attrKey);

		// try
		// {
		// return this.Row.GetValByKey(attrKey);
		// }
		// catch(Exception ex)
		// {
		// throw new
		// Exception(ex.Message+" "+attrKey+" EnsName="+this.ToString() );
		// }
	}

	/**
	 * GetValDateTime
	 * 
	 * @param attrKey
	 * @return
	 */
	public final java.util.Date GetValDateTime(String attrKey) {
		return DataType.ParseSysDateTime2DateTime(this.GetValStringByKey(attrKey));
	}

	/**
	 * 在确定 attrKey 存在 map 的情况下才能使用它
	 * 
	 * @param attrKey
	 * @return
	 */
	public final String GetValStrByKey(String key) {
		Object value = this.getRow().GetValByKey(key);
		if (null == value) {
			return "";
		}

		return value.toString();
	}

	public final String GetValStrByKey(String key, String isNullAs) {
		try {
			return this.getRow().GetValByKey(key).toString();
		} catch (java.lang.Exception e) {
			return isNullAs;
		}
	}

	/**
	 * 取得String
	 * 
	 * @param attrKey
	 * @return
	 */
	public final String GetValStringByKey(String attrKey) {
		if (attrKey.equals("Doc")) {
			String s = this.getRow().GetValByKey(attrKey).toString();
			if (s.equals("")) {
				s = this.GetValDocText();
			}
			return s;
		}
		try {

			if (this.getRow() == null) {
				throw new RuntimeException("@没有初始化Row.");
			}
			return this.getRow().GetValByKey(attrKey).toString();
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@获取值期间出现如下异常：" + ex.getMessage() + "  " + attrKey + " 您没有在类增加这个属性，EnsName=" + this.toString());
		}
	}

	public final String GetValStringByKey(String attrKey, String defVal) {
		String val = this.GetValStringByKey(attrKey);
		if (val == null || val.equals("")) {
			return defVal;
		} else {
			return val;
		}
	}

	/**
	 * 取出大块文本
	 * 
	 * @return
	 */
	public final String GetValDocText() {
		String s = this.GetValStrByKey("Doc");
		if (s.trim().length() != 0) {
			return s;
		}

		// s = SysDocFile.GetValTextV2(this.toString(),
		// this.getPKVal().toString());
		this.SetValByKey("Doc", s);
		return s;
	}

	public final String GetValDocHtml() {
		String s = this.GetValHtmlStringByKey("Doc");
		if (s.trim().length() != 0) {
			return s;
		}

		s = SysDocFile.GetValHtmlV2(this.toString(), this.getPKVal().toString());
		this.SetValByKey("Doc", s);
		return s;
	}

	/**
	 * 取到Html 信息。
	 * 
	 * @param attrKey
	 *            attr
	 * @return html.
	 */
	public final String GetValHtmlStringByKey(String attrKey) {
		return DataType.ParseText2Html(this.GetValStringByKey(attrKey));
	}

	public final String GetValHtmlStringByKey(String attrKey, String defval) {
		return DataType.ParseText2Html(this.GetValStringByKey(attrKey, defval));
	}

	/**
	 * 取得枚举或者外键的标签 如果是枚举就获取枚举标签. 如果是外键就获取为外键的名称.
	 * 
	 * @param attrKey
	 * @return
	 */
	public final String GetValRefTextByKey(String attrKey) {
		String str = "";
		try {
			Object tempVar = this.getRow().GetValByKey(attrKey + "Text");
			str = (String) ((tempVar instanceof String) ? tempVar : null);
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage() + attrKey);
		}
		return str;
	}

	public long GetValInt64ByKey(String key) {
		return Long.parseLong(this.GetValStringByKey(key, "0"));
	}

	public final int GetValIntByKey(String key, int IsZeroAs) {
		int i = this.GetValIntByKey(key);
		if (i == 0) {
			i = IsZeroAs;
		}
		return i;
	}

	public final int GetValIntByKey11(String key) {
		return Integer.parseInt(this.GetValStrByKey(key));
	}

	/**
	 * 根据key 得到int val
	 * 
	 * @param key
	 * @return
	 */
	public int GetValIntByKey(String key) {
		try {
			String val = this.GetValStrByKey(key);
			if (val.equals("true")) {

				this.SetValByKey(key, 1);
				return 1;
			} else if (val.equals("")) {
				return 0;
			} else {
				return Integer.parseInt(val);
			}
		} catch (RuntimeException ex) {
			// if (!SystemConfig.getIsDebug())
			// throw new Exception("@[" + this.EnMap.GetAttrByKey(key).Desc +
			// "]请输入数字，您输入的是[" + this.GetValStrByKey(key) + "]。");
			// else
			// throw new Exception("@表[" + this.EnDesc + "]在获取属性[" + key +
			// "]值,出现错误，不能将[" + this.GetValStringByKey(key) + "]转换为int类型.错误信息："
			// + ex.Message +
			// "@请检查是否在存储枚举类型时，您在SetValbyKey中没有转换。正确做法是:this.SetValByKey( Key
			// ,(int)value) ");

			String v = this.GetValStrByKey(key).toLowerCase();
			if (v.equals("on")) {

				return 1;
			}
			if (v.equals("false")) {
				this.SetValByKey(key, 0);
				return 0;
			}

			if (key.equals("OID")) {
				this.SetValByKey("OID", 0);
				return 0;
			}

			if (this.GetValStrByKey(key).equals("")) {
				Attr attr = this.getEnMap().GetAttrByKey(key);
				if (attr.getIsNull()) {
					return 567567567;
				} else {
					if (attr.getDefaultVal().toString().equals("")) {
						return 0;
					} else {
						return Integer.parseInt(attr.getDefaultVal().toString());
					}
				}
			} else {
				String value = this.GetValStrByKey(key);
				if (value.indexOf(".") != -1) {
					value = value.substring(0, value.indexOf("."));
					return Integer.parseInt(value);
				}
			}

			// else
			// {
			// return
			// int.Parse(this.EnMap.GetAttrByKey(key).DefaultVal.ToString());
			// }

			if (!SystemConfig.getIsDebug()) {
				throw new RuntimeException(
						"@cls=" + this.toString() + "@字段[" + this.getEnMap().GetAttrByKey(key).getDesc()
								+ "]请输入数字，您输入的是[" + this.GetValStrByKey(key) + "]。");
			} else {
				throw new RuntimeException("@表[" + this.getEnDesc() + "]在获取属性[" + key + "]值,出现错误，不能将["
						+ this.GetValStringByKey(key) + "]转换为int类型.错误信息：" + ex.getMessage()
						+ "@请检查是否在存储枚举类型时，您在SetValbyKey中没有转换。正确做法是:this.SetValByKey( Key ,(int)value)  ");
			}
		}
	}

	/**
	 * 根据key 得到 bool val
	 * 
	 * @param key
	 * @return
	 */
	public final boolean GetValBooleanByKey(String key) {
		String s = this.GetValStrByKey(key);
		if (StringHelper.isNullOrEmpty(s)) {
			s = this.getEnMap().GetAttrByKey(key).getDefaultVal().toString();
		}
		if (s.equals("")) {
			return true;
		}

		if (s.toUpperCase().equals("FALSE")) {
			return false;
		}
		if (s.toUpperCase().equals("TRUE")) {
			return true;
		}

		if (s.toUpperCase().equals("ON")) {
			return true;
		}

		if (Integer.parseInt(s) == 0) {
			return false;
		} else {
			return true;
		}
	}

	public final boolean GetValBooleanByKey(String key, boolean defval) {
		try {

			if (Integer.parseInt(this.GetValStringByKey(key)) == 0) {
				return false;
			} else {
				return true;
			}
		} catch (java.lang.Exception e) {
			return defval;
		}
	}

	public final String GetValBoolStrByKey(String key) {
		if (Integer.parseInt(this.GetValStringByKey(key)) == 0) {
			return "否";
		} else {
			return "是";
		}
	}

	/**
	 * 根据key 得到flaot val
	 * 
	 * @param key
	 * @return
	 */
	public final float GetValFloatByKey(String key, int blNum) {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		return Float.parseFloat(decimalFormat.format(this.getRow().GetValByKey(key).toString()));
		/*
		 * warning return
		 * Float.parseFloat(Float.parseFloat(this.getRow().GetValByKey
		 * (key).toString()).ToString("0.00"));
		 */
	}

	/**
	 * 根据key 得到flaot val
	 * 
	 * @param key
	 * @return
	 */
	public final float GetValFloatByKey(String key) {
		try {
			DecimalFormat decimalFormat = new DecimalFormat("#.00");

			return Float.parseFloat(decimalFormat.format(this.getRow().GetValByKey(key)));
			/*
			 * warning return
			 * Float.parseFloat(Float.parseFloat(this.getRow().GetValByKey
			 * (key).toString()).ToString("0.00"));
			 */
		} catch (java.lang.Exception e) {
			if (this.GetValStringByKey(key).equals("")) {
				Attr attr = this.getEnMap().GetAttrByKey(key);
				if (attr.getIsNull()) {
					return 567567567;
				} else {
					if (attr.getDefaultVal().toString().equals("")) {
						return 0;
					} else {
						return Float.parseFloat(attr.getDefaultVal().toString());
					}
				}
			}
			return 0;
		}
	}

	public final java.math.BigDecimal GetValMoneyByKey(String key) {
		try {
			return this.GetValDecimalByKey(key).setScale(2, BigDecimal.ROUND_HALF_UP);
			/*
			 * warning return
			 * java.math.BigDecimal.Parse(this.GetValDecimalByKey(
			 * key).ToString("0.00"));
			 */
		} catch (java.lang.Exception e) {
			if (this.GetValStringByKey(key).equals("")) {
				Attr attr = this.getEnMap().GetAttrByKey(key);
				if (attr.getIsNull()) {
					return new BigDecimal(567567567);
				} else {
					return new java.math.BigDecimal(attr.getDefaultVal().toString());
				}
			}
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 根据key 得到flaot val
	 * 
	 * @param key
	 * @return
	 */
	public final java.math.BigDecimal GetValDecimalByKey(String key) {
		BigDecimal bd = new BigDecimal(this.GetValStrByKey(key));
		return bd.setScale(4, BigDecimal.ROUND_HALF_UP);
		/*
		 * warning return
		 * java.math.BigDecimal.Round(java.math.BigDecimal.Parse(this
		 * .GetValStrByKey(key)), 4);
		 */
	}

	public final java.math.BigDecimal GetValDecimalByKey(String key, String items) {
		if (items.equals("") || items == null) {
			return BigDecimal.ZERO;
		}

		if (items.indexOf("@" + key) == -1) {
			return BigDecimal.ZERO;
		}

		String str = items.substring(items.indexOf("@" + key));

		/*
		 * warning return
		 * java.math.BigDecimal.Round(java.math.BigDecimal.Parse(this
		 * .GetValStringByKey(key)), 4);
		 */
		return GetValDecimalByKey(key);
	}

	public final double GetValDoubleByKey(String key) {
		try {
			return Double.parseDouble(this.GetValStrByKey(key));
		} catch (RuntimeException ex) {
			throw new RuntimeException("@表[" + this.getEnDesc() + "]在获取属性[" + key + "]值,出现错误，不能将["
					+ this.GetValStringByKey(key) + "]转换为double类型.错误信息：" + ex.getMessage());
		}
	}

	public final String GetValAppDateByKey(String key) {
		try {
			String str = this.GetValStringByKey(key);
			if (str == null || str.equals("")) {
				return str;
			}
			return DataType.StringToDateStr(str);
		} catch (RuntimeException ex) {
			throw new RuntimeException("@实例：[" + this.getEnMap().getEnDesc() + "]  属性[" + key + "]值["
					+ this.GetValStringByKey(key).toString() + "]日期格式转换出现错误：" + ex.getMessage());
		}
		// return "2003-08-01";
	}

	public final boolean getIsBlank() {
		if (this._row == null) {
			return true;
		}

		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs) {

			if (attr.getUIIsReadonly() && !attr.getIsFKorEnum()) {
				continue;
			}

			// if (attr.getIsFK() &&
			// StringHelper.isNullOrEmpty(attr.getDefaultVal().toString()))
			// {
			// continue; //如果是外键,并且外键的默认值为null.
			// }

			String str = this.GetValStrByKey(attr.getKey());
			if (str.equals("") || attr.getDefaultVal().toString().equals(str) || str == null) {
				continue;
			}

			if (attr.getMyDataType() == DataType.AppDate && attr.getDefaultVal() == null) {
				if (DataType.getCurrentDateByFormart("yyyy-MM-dd").equals(str)) {
					continue;
				} else {
					return true;
				}
			}

			if (attr.getDefaultVal().toString().equals(str) && !attr.getIsFK()) {
				continue;
			}

			if (attr.getIsEnum()) {
				if (attr.getDefaultVal().toString().equals(str)) {
					continue;
				} else {
					return false;
				}
				/*
				 * warning continue;
				 */
			}

			if (attr.getIsNum()) {
				/*
				 * warning if (java.math.BigDecimal.Parse(str) !=
				 * java.math.BigDecimal.Parse(attr.getDefaultVal().toString()))
				 */
				if ((new BigDecimal(str)).compareTo(new BigDecimal(attr.getDefaultVal().toString())) != 0) {
					return false;
				} else {
					continue;
				}
			}

			if (attr.getIsFKorEnum()) {
				// if (attr.DefaultVal == null || attr.DefaultVal == "")
				// continue;

				if (!attr.getDefaultVal().toString().equals(str)) {
					return false;
				} else {
					continue;
				}
			}

			if (!attr.getDefaultVal().toString().equals(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取或者设置 是不是空的实体.
	 */
	public final boolean getIsEmpty() {
		if (this._row == null) {
			return true;
		} else {
			if (this.getPKVal() == null || this.getPKVal().toString().equals("0")
					|| this.getPKVal().toString().equals("")) {
				return true;
			}
			return false;
		}
	}

	public final void setIsEmpty(boolean value) {
		this._row = null;
	}

	/**
	 * 对这个实体的描述
	 */
	public final String getEnDesc() {
		return this.getEnMap().getEnDesc();
	}

	/**
	 * 取到主健值。如果它的主健不唯一，就返回第一个值。 获取或设置
	 */
	public final Object getPKVal() {
		return this.GetValByKey(this.getPK());
	}

	public final void setPKVal(Object value) {
		this.SetValByKey(this.getPK(), value);
	}

	/**
	 * 如果只有一个主键,就返回PK,如果有多个就返回第一个.PK
	 */
	public final int getPKCount() {
		if (this.getPK().equals("OID") || this.getPK().equals("No") || this.getPK().equals("MyPK")) {
			return 1;
		} else {
		}

		int i = 0;
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				i++;
			}
		}
		if (i == 0) {
			throw new RuntimeException("@没有给【" + this.getEnDesc() + "，" + this.getEnMap().getPhysicsTable() + "】定义主键。");
		} else {
			return i;
		}
	}

	/**
	 * 是不是OIDEntity
	 */
	public final boolean getIsOIDEntity() {
		if (this.getPK().equals("OID")) {
			return true;
		}
		return false;
	}

	/**
	 * 是不是OIDEntity
	 */
	public final boolean getIsNoEntity() {
		if (this.getPK().equals("No")) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是TreeEntity
	 */
	public final boolean getIsTreeEntity() {
		if (this.getPK().equals("ID")) {
			return true;
		}
		return false;
	}

	/**
	 * 是不是IsMIDEntity
	 */
	public final boolean getIsMIDEntity() {
		if (this.getPK().equals("MID")) {
			return true;
		}
		return false;
	}

	/**
	 * 如果只有一个主键,就返回PK,如果有多个就返回第一个.PK
	 */
	public String getPK() {
		String pks = "";
		for (Attr attr : this.getEnMap().getAttrs()) {
			/*
			 * if (attr.getKey().equals("No")) { return "No"; } else if
			 * (attr.getKey().equals("OID")) { return "OID"; } else if
			 * (attr.getKey().equals("MyPK")) { return "MyPK"; }
			 */
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				pks += attr.getKey() + ",";
			}
		}
		if (pks.equals("")) {
			throw new RuntimeException("@没有给【" + this.getEnDesc() + "，" + this.getEnMap().getPhysicsTable() + "】定义主键。");
		}
		pks = pks.substring(0, pks.length() - 1);
		return pks;
	}

	public String getPKField() {
		/*
		 * if (this.getIsOIDEntity()) return "OID";
		 * 
		 * if (this.getIsNoEntity()) return "No";
		 */
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				return attr.getField();
			}
		}
		throw new RuntimeException("@没有给【" + this.getEnDesc() + "】定义主键。");
	}

	/**
	 * 如果只有一个主键,就返回PK,如果有多个就返回第一个.PK
	 */
	public final String[] getPKs() {
		String[] strs1 = new String[this.getPKCount()];
		int i = 0;
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum
					|| attr.getMyFieldType() == FieldType.PKFK) {
				strs1[i] = attr.getKey();
				i++;
			}
		}
		return strs1;
	}

	/**
	 * 取到主健值。
	 */
	public final java.util.Hashtable getPKVals() {
		java.util.Hashtable ht = new java.util.Hashtable();
		String[] strs = this.getPKs();
		for (String str : strs) {
			ht.put(str, this.GetValStringByKey(str));
		}
		return ht;
	}

	public final void domens() {
	}
}