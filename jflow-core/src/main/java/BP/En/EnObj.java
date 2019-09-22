package BP.En;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 Entity 的摘要说明。
*/
public abstract class EnObj implements Serializable
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 访问控制.
	private String _DBVarStr = null;
	public final String getHisDBVarStr()
	{
		if (_DBVarStr != null)
		{
			return _DBVarStr;
		}

		_DBVarStr = this.getEnMap().getEnDBUrl().getDBVarStr();
		return _DBVarStr;
	}
	/** 
	 他的访问控制.
	*/
	protected UAC _HisUAC = null;
	/** 
	 得到 uac 控制.
	 
	 @return 
	*/
	public UAC getHisUAC()
	{
		if (_HisUAC == null)
		{
			_HisUAC = new UAC();
			if (BP.Web.WebUser.getNo().equals("admin"))
			{
				_HisUAC.IsAdjunct = false;
				_HisUAC.IsDelete = true;
				_HisUAC.IsInsert = true;
				_HisUAC.IsUpdate = true;
				_HisUAC.IsView = true;
			}
		}
		return _HisUAC;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 取出外部配置的属性信息
	/** 
	 取出Map 的扩展属性。
	 用于第3方的扩展属性开发。
	 
	 @param key 属性Key
	 @return 设置的属性
	*/
	public final String GetMapExtAttrByKey(String key)
	{
		Paras ps = new Paras();
		ps.Add("enName", this.toString());
		ps.Add("key", key);

		return (String)DBAccess.RunSQLReturnVal("select attrValue from Sys_ExtMap WHERE className=" + SystemConfig.getAppCenterDBVarStr() + "enName AND attrKey=" + SystemConfig.getAppCenterDBVarStr() + "key", ps);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region CreateInstance
	/** 
	 创建一个实例
	 
	 @return 自身的实例
	*/
	public final Entity CreateInstance()
	{
		Object tempVar = this.getClass().Assembly.CreateInstance(this.toString());
		return tempVar instanceof Entity ? (Entity)tempVar : null;
		//return ClassFactory.GetEn(this.ToString());
	}
	private Entities _GetNewEntities = null;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 设置默认值.
	*/
	public final void ResetDefaultValRowValues()
	{
		if (this.get_enMap() == null)
		{
			return;
		}

		for (Attr attr : this.get_enMap().getAttrs())
		{
			String key = attr.getKey();

			String v = this.GetValStringByKey(key, null); // this._row[key] as string;

			if (v == null || v.indexOf('@') == -1)
			{
				continue;
			}


			// 设置默认值.
			if (v.equals("@WebUser.No"))
			{

				this.SetValByKey(attr.getKey(), Web.WebUser.getNo());

				continue;
			}
			else if (v.equals("@WebUser.Name"))
			{
				this.SetValByKey(attr.getKey(), Web.WebUser.getName());
				continue;
			}
			else if (v.equals("@WebUser.FK_Dept"))
			{
				this.SetValByKey(attr.getKey(), Web.WebUser.getFK_Dept());
				continue;
			}
			else if (v.equals("@WebUser.FK_DeptName"))
			{
				this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptName());
				continue;
			}
			else if (v.equals("@WebUser.FK_DeptNameOfFull") || v.equals("@WebUser.FK_DeptFullName"))
			{
				this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptNameOfFull());
				continue;
			}
			else if (v.equals("@RDT"))
			{
				if (attr.getMyDataType() == DataType.AppDate)
				{
					this.SetValByKey(attr.getKey(), DataType.getCurrentData());
				}

				if (attr.getMyDataType() == DataType.AppDateTime)
				{
					this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
				}
				continue;
			}
			else
			{
				continue;
			}
		}
	}
	/** 
	 重新设置默信息. @yuanlina 这里有问题，需要重构到jflow上去.
	*/
	public final void ResetDefaultVal()
	{

		ResetDefaultValRowValues();

		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getIsRefAttr())
			{
				this.SetValRefTextByKey(attr.getKey(), "");
			}

		   String tempVar = attr.getDefaultValOfReal();
		   String v = tempVar instanceof String ? (String)tempVar : null;
			if (v == null || v.contains("@") == false)
			{
				continue;
			}

			String myval = this.GetValStrByKey(attr.getKey());


			// 设置默认值.
			switch (v)
			{
				case "@WebUser.No":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), Web.WebUser.getNo());
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), Web.WebUser.getNo());
						}
					}
					continue;
				case "@WebUser.Name":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), Web.WebUser.getName());
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), Web.WebUser.getName());
						}
					}
					continue;
				case "@WebUser.FK_Dept":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), Web.WebUser.getFK_Dept());
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), Web.WebUser.getFK_Dept());
						}
					}
					continue;
				case "@WebUser.FK_DeptName":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptName());
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptName());
						}
					}
					continue;
				case "@WebUser.FK_DeptNameOfFull":
				case "@WebUser.FK_DeptFullName":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptNameOfFull());
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptNameOfFull());
						}
					}
					continue;
				case "@RDT":
					if (attr.getUIIsReadonly() == true)
					{
						if (attr.getMyDataType() == DataType.AppDate || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), DataType.getCurrentData());
						}

						if (attr.getMyDataType() == DataType.AppDateTime || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
						}
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							if (attr.getMyDataType() == DataType.AppDate)
							{
								this.SetValByKey(attr.getKey(), DataType.getCurrentData());
							}
							else
							{
								this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
							}
						}
					}
					continue;
				case "@FK_ND":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), DataType.getCurrentYear());
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), DataType.getCurrentYear());
						}
					}
					continue;
				case "@yyyy年MM月dd日":
				case "@yyyy年MM月dd日HH时mm分":
				case "@yy年MM月dd日":
				case "@yy年MM月dd日HH时mm分":
					if (attr.getUIIsReadonly() == true)
					{
						this.SetValByKey(attr.getKey(), LocalDateTime.now().toString(v.replace("@", "")));
					}
					else
					{
						if (DataType.IsNullOrEmpty(myval) || v.equals(myval))
						{
							this.SetValByKey(attr.getKey(), LocalDateTime.now().toString(v.replace("@", "")));
						}
					}
					continue;
				default:
					GloVar gloVar = new GloVar();
					gloVar.setPKVal(v);
					int count = gloVar.RetrieveFromDBSources();
					if (count == 1)
					{
						//执行SQL获取默认值
						String sql = gloVar.getVal();
						sql = DealExp(sql, null, null);
						try
						{
							String val = DBAccess.RunSQLReturnString(sql);
							this.SetValByKey(attr.getKey(), val);
						}
						catch (RuntimeException ex)
						{
							this.SetValByKey(attr.getKey(), ex.getMessage() + sql);
						}

					}
					continue;
			}
		}
	}

	/** 
	 处理表达式
	 
	 @param exp 表达式
	 @param en 数据源
	 @param errInfo 错误
	 @return 
	*/
	private static String DealExp(String exp, Entity en, String errInfo)
	{
		if (exp.contains("@") == false)
		{
			return exp;
		}

		exp = exp.replace("~", "'");

		//首先替换加; 的。
		exp = exp.replace("@WebUser.No;", Web.WebUser.getNo());
		exp = exp.replace("@WebUser.Name;", Web.WebUser.getName());
		exp = exp.replace("@WebUser.FK_Dept;", Web.WebUser.getFK_Dept());
		exp = exp.replace("@WebUser.FK_DeptName;", Web.WebUser.getFK_DeptName());

		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.No", Web.WebUser.getNo());
		exp = exp.replace("@WebUser.Name", Web.WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName", Web.WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept", Web.WebUser.getFK_Dept());
		//  exp = exp.Replace("@WorkID", "0");

		if (exp.contains("@") == false)
		{
			exp = exp.replace("~", "'");
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null)
		{
			Row row = en.getRow();
			//特殊判断.
			if (row.containsKey("OID") == true)
			{
				exp = exp.replace("@WorkID", row.get("OID").toString());
			}

			if (exp.contains("@") == false)
			{
				return exp;
			}

			for (String key : row.keySet())
			{
				if (exp.contains("@" + key + ";"))
				{
					exp = exp.replace("@" + key + ";", row.get(key).toString());
				}
			}
			if (exp.contains("@") == false)
			{
				return exp;
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 解决排序问题.
			Attrs attrs = en.getEnMap().getAttrs();
			String mystrs = "";
			for (Attr attr : attrs)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					mystrs += "@" + attr.getKey() + ",";
				}
				else
				{
					mystrs += "@" + attr.getKey();
				}
			}
			String[] strs = mystrs.split("[@]", -1);
			DataTable dt = new DataTable();
			dt.Columns.Add(new DataColumn("No", String.class));
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.set(0, str);
				dt.Rows.Add(dr);
			}
			DataView dv = dt.DefaultView;
			dv.Sort = "No DESC";
			DataTable dtNew = dv.Table;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  解决排序问题.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 替换变量.
			for (DataRow dr : dtNew.Rows)
			{
				String key = dr.get(0).toString();
				boolean isStr = key.contains(",");
				if (isStr == true)
				{
					key = key.replace(",", "");
					exp = exp.replace("@" + key, en.GetValStrByKey(key));
				}
				else
				{
					exp = exp.replace("@" + key, en.GetValStrByKey(key));
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			if (exp.contains("@") == false)
			{
				return exp;
			}

			//替换全部的变量.
			for (String key : row.keySet())
			{
				if (exp.contains("@" + key))
				{
					exp = exp.replace("@" + key, row.get(key).toString());
				}
			}
		}



		if (exp.contains("@") && SystemConfig.getIsBSsystem() == true)
		{
			/*如果是bs*/
			for (String key : HttpContextHelper.getRequestParamKeys())
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				exp = exp.replace("@" + key, HttpContextHelper.RequestParams(key));
			}
		}

		exp = exp.replace("~", "'");

		return exp;
	}

	/** 
	 把所有的值都设置成默认值，但是主键除外。
	*/
	public final void ResetDefaultValAllAttr()
	{
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getUIIsReadonly() == false && attr.getDefaultValOfReal() != null)
			{
				continue;
			}

			if (attr.getIsPK())
			{
				continue;
			}

			String tempVar = attr.getDefaultValOfReal();
			String v = tempVar instanceof String ? (String)tempVar : null;
			if (v == null)
			{
				this.SetValByKey(attr.getKey(), "");
				continue;
			}

			if (v.contains("@") == false && v != null)
			{
				this.SetValByKey(attr.getKey(), v);
				continue;
			}


			// 设置默认值.
			switch (v)
			{
				case "@WebUser.No":
					this.SetValByKey(attr.getKey(), Web.WebUser.getNo());
					continue;
				case "@WebUser.Name":
					this.SetValByKey(attr.getKey(), Web.WebUser.getName());
					continue;
				case "@WebUser.FK_Dept":
					this.SetValByKey(attr.getKey(), Web.WebUser.getFK_Dept());
					continue;
				case "@WebUser.FK_DeptName":
					this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptName());
					continue;
				case "@WebUser.FK_DeptNameOfFull":
					this.SetValByKey(attr.getKey(), Web.WebUser.getFK_DeptNameOfFull());
					continue;
				case "@RDT":
					if (attr.getMyDataType() == DataType.AppDate)
					{
						this.SetValByKey(attr.getKey(), DataType.getCurrentData());
					}
					else
					{
						this.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
					}
					continue;
				default:
					continue;
			}
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 实体
	*/
	public EnObj()
	{
	}
	private Map _tmpEnMap = null;
	/** 
	 Map
	*/
	protected final Map get_enMap()
	{
		if (_tmpEnMap == null)
		{
			_tmpEnMap = Cash.GetMap(this.toString());
		}
		return _tmpEnMap;
	}
	protected final void set_enMap(Map value)
	{
		if (value == null)
		{
			_tmpEnMap = null;
			return;
		}

		Map mp = (Map)value;
		if (SystemConfig.getIsDebug())
		{

		}

		if (mp == null || mp.getDepositaryOfMap() == Depositary.None)
		{
			_tmpEnMap = mp;
			return;
		}

		Cash.SetMap(this.toString(), mp);
		_tmpEnMap = mp;
	}
	/** 
	 子类需要继承
	*/
	public abstract Map getEnMap();
	/** 
	 动态的获取map
	*/
	public final Map getEnMapInTime()
	{
		_tmpEnMap = null;
		Cash.SetMap(this.toString(), null);
		return this.getEnMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region row 存放实体数据的
	/** 
	 实体的 map 信息。	
	*/
	//public abstract void EnMap();		
	private Row _row = null;
	public final Row getRow()
	{
		if (this._row == null)
		{
			this._row = new Row();
			this._row.LoadAttrs(this.getEnMap().getAttrs());
		}
		return this._row;
	}
	public final void setRow(Row value)
	{
		this._row = value;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于属性的操作。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 设置值方法
	/** 
	 设置object类型的值
	 
	 @param attrKey attrKey
	 @param val val
	*/
	public final void SetValByKey(String attrKey, String val)
	{
		switch (val)
		{
			case null:
			case "&nbsp;":
				val = "";
				break;
			case "RDT":
				if (val.length() > 4)
				{
					this.SetValByKey("FK_NY", val.substring(0, 7));
					this.SetValByKey("FK_ND", val.substring(0, 4));
				}
				break;
			default:
				break;
		}
		this.getRow().SetValByKey(attrKey, val);
	}
	public final void SetValByKey(String attrKey, int val)
	{
		this.getRow().SetValByKey(attrKey, val);
	}
	public final void SetValByKey(String attrKey, long val)
	{
		this.getRow().SetValByKey(attrKey, val);
	}
	public final void SetValByKey(String attrKey, float val)
	{
		this.getRow().SetValByKey(attrKey, val);
	}
	public final void SetValByKey(String attrKey, BigDecimal val)
	{
		this.getRow().SetValByKey(attrKey, val);
	}
	public final void SetValByKey(String attrKey, Object val)
	{
		this.getRow().SetValByKey(attrKey, val);
	}

	public final void SetValByDesc(String attrDesc, Object val)
	{
		if (val == null)
		{
			throw new RuntimeException("@不能设置属性[" + attrDesc + "]null 值。");
		}
		this.getRow().SetValByKey(this.getEnMap().GetAttrByDesc(attrDesc).getKey(), val);
	}

	/** 
	 设置关联类型的值
	 
	 @param attrKey attrKey
	 @param val val
	*/
	public final void SetValRefTextByKey(String attrKey, Object val)
	{
		this.SetValByKey(attrKey + "Text", val);
	}
	/** 
	 设置bool类型的值
	 
	 @param attrKey attrKey
	 @param val val
	*/
	public final void SetValByKey(String attrKey, boolean val)
	{
		if (val)
		{
			this.SetValByKey(attrKey, 1);
		}
		else
		{
			this.SetValByKey(attrKey, 0);
		}
	}
	/** 
	 设置默认值
	*/
	public final void SetDefaultVals()
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
			this.SetValByKey(attr.getKey(), attr.getDefaultVal());
		}
	}
	/** 
	 设置日期类型的值
	 
	 @param attrKey attrKey
	 @param val val
	*/
	public final void SetDateValByKey(String attrKey, String val)
	{
		try
		{
			this.SetValByKey(attrKey, DataType.StringToDateStr(val));
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@不合法的日期数据格式:key=[" + attrKey + "],value=" + val + " " + ex.getMessage());
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 取值方法
	/** 
	 取得Object
	 
	 @param attrKey
	 @return 
	*/
	public final Object GetValByKey(String attrKey)
	{
		return this.getRow().GetValByKey(attrKey);

		//try
		//{
		//    return this.Row.GetValByKey(attrKey);				
		//}
		//catch(Exception ex)
		//{
		//    throw new Exception(ex.Message+"  "+attrKey+" EnsName="+this.ToString() );
		//}
	}
	/** 
	 GetValDateTime
	 
	 @param attrKey
	 @return 
	*/
	public final LocalDateTime GetValDateTime(String attrKey)
	{
		try
		{
			return DataType.ParseSysDateTime2DateTime(this.GetValStringByKey(attrKey));
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("字段[" + this.getEnMap().GetAttrByKey(attrKey).getDesc() + "],值[" + this.GetValStringByKey(attrKey) + "]，不是一个有效的时间格式.");
		}
	}
	/** 
	 在确定  attrKey 存在 map 的情况下才能使用它
	 
	 @param attrKey
	 @return 
	*/
	public final String GetValStrByKey(String key)
	{
		if (this.getRow().GetValByKey(key) != null)
		{
		return this.getRow().GetValByKey(key).toString();
		}
		return "";
	}
	public final String GetValStrByKey(String key, String isNullAs)
	{
		try
		{
			String s = this.getRow().GetValByKey(key).toString();
			if (DataType.IsNullOrEmpty(s))
			{
				return isNullAs;
			}
			else
			{
				return s;
			}
		}
		catch (java.lang.Exception e)
		{
			return isNullAs;
		}
	}
	/** 
	 取得String
	 
	 @param attrKey
	 @return 
	*/
	public final String GetValStringByKey(String attrKey)
	{
		switch (attrKey)
		{
			case "Doc":
				String s = this.getRow().GetValByKey(attrKey).toString();
				if (s.equals(""))
				{
					s = this.GetValDocText();
				}
				return s;
			default:
				try
				{
					if (this.getRow() == null)
					{
						throw new RuntimeException("@没有初始化Row.");
					}
					return this.getRow().GetValByKey(attrKey).toString();
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@获取值期间出现如下异常：" + ex.getMessage() + "  " + attrKey + " 您没有在类增加这个属性，EnsName=" + this.toString());
				}
				break;
		}
	}
	public final String GetValStringByKey(String attrKey, String defVal)
	{
		String val = this.GetValStringByKey(attrKey);
		if (val == null || val.equals(""))
		{
			return defVal;
		}
		else
		{
			return val;
		}
	}
	/** 
	  取出大块文本
	 
	 @return 
	*/
	public final String GetValDocText()
	{
		String s = this.GetValStrByKey("Doc");
		if (s.trim().length() != 0)
		{
			return s;
		}

		s = SysDocFile.GetValTextV2(this.toString(), this.getPKVal().toString());
		this.SetValByKey("Doc", s);
		return s;
	}
	public final String GetValDocHtml()
	{
		String s = this.GetValHtmlStringByKey("Doc");
		if (s.trim().length() != 0)
		{
			return s;
		}

		s = SysDocFile.GetValHtmlV2(this.toString(), this.getPKVal().toString());
		this.SetValByKey("Doc", s);
		return s;
	}
	/** 
	 取到Html 信息。
	 
	 @param attrKey attr
	 @return html.
	*/
	public final String GetValHtmlStringByKey(String attrKey)
	{
		return DataType.ParseText2Html(this.GetValStringByKey(attrKey));
	}
	public final String GetValHtmlStringByKey(String attrKey, String defval)
	{
		return DataType.ParseText2Html(this.GetValStringByKey(attrKey, defval));
	}
	/** 
	 取得枚举或者外键的标签
	 如果是枚举就获取枚举标签.
	 如果是外键就获取为外键的名称.
	 
	 @param attrKey
	 @return 
	*/
	public final String GetValRefTextByKey(String attrKey)
	{
		String str = "";
		try
		{
			Object tempVar = this.getRow().GetValByKey(attrKey + "Text");
			str = tempVar instanceof String ? (String)tempVar : null;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + attrKey);
		}
		if (str == null)
		{
			/*判断是否是外键字段？*/
			if (this.getRow().containsKey(attrKey + "Text") == false)
			{
				throw new RuntimeException(attrKey + "非外键或者枚举，不能使用:GetValRefTextByKey ，获取数据。");
			}
		}
		return str;
	}
	public final long GetValInt64ByKey(String key)
	{
		return Long.parseLong(this.GetValStringByKey(key, "0"));
	}

	public final int GetValIntByKey(String key, int isNullAsVal)
	{
		String str = this.GetValStrByKey(key);
		if (str == null || str.equals("") || str.equals("null"))
		{
			return isNullAsVal;
		}

		if (DataType.IsNumStr(str) == false)
		{
			return isNullAsVal;
		}
		return Integer.parseInt(str);
	}
	/** 
	 根据key 得到int val
	 
	 @param key
	 @return 
	*/
	public final int GetValIntByKey(String key)
	{
		try
		{
			return Integer.parseInt(this.GetValStrByKey(key));
		}
		catch (RuntimeException ex)
		{
			//if (SystemConfig.IsDebug == false)
			//    throw new Exception("@[" + this.EnMap.GetAttrByKey(key).Desc + "]请输入数字，您输入的是[" + this.GetValStrByKey(key) + "]。");
			//else
			//    throw new Exception("@表[" + this.EnDesc + "]在获取属性[" + key + "]值,出现错误，不能将[" + this.GetValStringByKey(key) + "]转换为int类型.错误信息：" + ex.Message + "@请检查是否在存储枚举类型时，您在SetValbyKey中没有转换。正确做法是:this.SetValByKey( Key ,(int)value)  ");

			String v = this.GetValStrByKey(key).toLowerCase();
			if (v.equals("null"))
			{
				return 0;
			}

			if (v.equals("true"))
			{
				this.SetValByKey(key, 1);
				return 1;
			}
			if (v.equals("false"))
			{
				this.SetValByKey(key, 0);
				return 0;
			}

			if (key.equals("OID"))
			{
				this.SetValByKey("OID", 0);
				return 0;
			}

			if (this.GetValStrByKey(key).equals(""))
			{
				Attr attr = this.getEnMap().GetAttrByKey(key);
				if (attr.getIsNull())
				{
					return 567567567;
				}
				else
				{
					return Integer.parseInt(attr.getDefaultVal().toString());
				}
			}

			throw new RuntimeException("@实体类[" + this.toString() + "]@[" + this.getEnMap().GetAttrByKey(key).getDesc() + "]请输入数字，您输入的是[" + this.GetValStrByKey(key) + "],错误信息:" + ex.getMessage());
		}
	}
	/** 
	 根据key 得到 bool val
	 
	 @param key
	 @return 
	*/
	public final boolean GetValBooleanByKey(String key)
	{
		String s = this.GetValStrByKey(key);
		if (DataType.IsNullOrEmpty(s))
		{
			s = this.getEnMap().GetAttrByKey(key).getDefaultVal().toString();
		}

		if (s.equals("0"))
		{
			return false;
		}

		if (s.equals("1"))
		{
			return true;
		}

		if (s.toUpperCase().equals("FALSE"))
		{
			return false;
		}
		if (s.toUpperCase().equals("TRUE"))
		{
			return true;
		}

		if (DataType.IsNullOrEmpty(s))
		{
			return false;
		}

		if (Integer.parseInt(s) == 0)
		{
			return false;
		}

		return true;
	}

	public final boolean GetValBooleanByKey(String key, boolean defval)
	{
		try
		{

			if (Integer.parseInt(this.GetValStringByKey(key)) == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (java.lang.Exception e)
		{
			return defval;
		}
	}
	public final String GetValBoolStrByKey(String key)
	{
		if (Integer.parseInt(this.GetValStringByKey(key)) == 0)
		{
			return "否";
		}
		else
		{
			return "是";
		}
	}
	/** 
	 根据key 得到flaot val
	 
	 @param key
	 @return 
	*/
	public final float GetValFloatByKey(String key, int blNum)
	{
		String val = this.getRow().GetValByKey(key).toString();
		if (DataType.IsNullOrEmpty(val))
		{
			return Float.parseFloat((new Integer(blNum)).toString("0.00"));
		}

		return Float.parseFloat(Float.parseFloat(val).toString("0.00"));
	}
	/** 
	 根据key 得到flaot val
	 
	 @param key
	 @return 
	*/
	public final float GetValFloatByKey(String key)
	{
		try
		{
			return Float.parseFloat(Float.parseFloat(this.getRow().GetValByKey(key).toString()).toString("0.00"));
		}
		catch (java.lang.Exception e)
		{
			if (this.GetValStringByKey(key).equals(""))
			{
				Attr attr = this.getEnMap().GetAttrByKey(key);
				if (attr.getIsNull())
				{
					return 567567567;
				}
				else
				{
					return Float.parseFloat(attr.getDefaultVal().toString());
				}
			}
			return 0;
		}
	}
	public final BigDecimal GetValMoneyByKey(String key)
	{
		try
		{
			return BigDecimal.Parse(this.GetValDecimalByKey(key).toString("0.00"));
		}
		catch (java.lang.Exception e)
		{
			if (this.GetValStringByKey(key).equals(""))
			{
				Attr attr = this.getEnMap().GetAttrByKey(key);
				if (attr.getIsNull())
				{
					return 567567567;
				}
				else
				{
					return BigDecimal.Parse(attr.getDefaultVal().toString());
				}
			}
			return 0;
		}
	}
	/** 
	 根据key 得到flaot val
	 
	 @param key
	 @return 
	*/
	public final BigDecimal GetValDecimalByKey(String key)
	{
		try
		{
			return BigDecimal.Round(BigDecimal.Parse(this.GetValStrByKey(key)), 4);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@表[" + this.getEnDesc() + "]在获取属性[" + key + "]值,出现错误，不能将[" + this.GetValStrByKey(key) + "]转换为float类型.错误信息：" + ex.getMessage());
		}
	}
	public final BigDecimal GetValDecimalByKey(String key, String items)
	{
		if (items.equals("") || items == null)
		{
			return 0;
		}

		if (items.indexOf("@" + key) == -1)
		{
			return 0;
		}

		String str = items.substring(items.indexOf("@" + key));

		return BigDecimal.Round(BigDecimal.Parse(this.GetValStringByKey(key)), 4);
	}
	public final double GetValDoubleByKey(String key)
	{
		try
		{
			return Double.parseDouble(this.GetValStrByKey(key));
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@表[" + this.getEnDesc() + "]在获取属性[" + key + "]值,出现错误，不能将[" + this.GetValStringByKey(key) + "]转换为double类型.错误信息：" + ex.getMessage());
		}
	}
	public final String GetValAppDateByKey(String key)
	{
		try
		{
			String str = this.GetValStringByKey(key);
			if (str == null || str.equals(""))
			{
				return str;
			}
			return DataType.StringToDateStr(str);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@实例：[" + this.getEnMap().getEnDesc() + "]  属性[" + key + "]值[" + this.GetValStringByKey(key).toString() + "]日期格式转换出现错误：" + ex.getMessage());
		}
		//return "2003-08-01";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 是否是空白的实体？
	*/
	public final boolean getIsBlank()
	{
		if (this._row == null)
		{
			return true;
		}

		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getUIIsReadonly() && attr.getIsFKorEnum() == false)
			{
				continue;
			}

				//日期类型.  @杜. 这里需要翻译.
			if (attr.getKey().equals("RDT") || attr.getKey().equals("Rec"))
			{
				continue;
			}

			if (attr.getDefaultValOfReal() != null && attr.getDefaultValOfReal().contains("@") == true)
			{
				continue;
			}

				//if (attr.IsFK && DataType.IsNullOrEmpty(attr.DefaultVal.ToString()) == true)
				//    continue; /*如果是外键,并且外键的默认值为null.*/

			if (attr.getIsFK())
			{
					//如果打开下面的代码，就会出现 /RefFunc/dtl.aspx 连续增加的问题. 
				if (this.GetValByKey(attr.getKey()).equals("") || attr.getDefaultValOfReal().equals(this.GetValByKey(attr.getKey())))
				{
					continue;
				}

					//if (this.GetValByKey(attr.Key) == "" || this.GetValByKey(attr.Key)== attr.DefaultValOfReal)
					//    continue;

				return false;
					//continue; /*如果是外键,并且外键的默认值为null.*/
			}

			String str = this.GetValStrByKey(attr.getKey());
			if (attr.getDefaultVal().toString().equals(str) || str.equals("0.00"))
			{
				continue;
			}
			else
			{
				return false;
			}

			if (attr.getMyDataType() == DataType.AppDate && attr.getDefaultVal() == null)
			{
				if (DataType.getCurrentData().equals(str))
				{
					continue;
				}
			}

			if (attr.getDefaultVal().toString().equals(str) && attr.getIsFK() == false)
			{
				continue;
			}

			if (attr.getIsEnum())
			{
				if (attr.getDefaultVal().toString().equals(str))
				{
					continue;
				}
				else
				{
					return false;
				}
				continue;
			}

			if (attr.getIsNum())
			{
				if (BigDecimal.Parse(str).compareTo(BigDecimal.Parse(attr.getDefaultVal().toString())) != 0)
				{
					return false;
				}
				else
				{
					continue;
				}
			}

			if (attr.getIsFKorEnum())
			{
					//if (attr.DefaultVal == null || attr.DefaultVal == "")
					//    continue;

				if (!attr.getDefaultVal().toString().equals(str))
				{
					return false;
				}
				else
				{
					continue;
				}
			}

			if (!attr.getDefaultVal().toString().equals(str))
			{
				return false;
			}
		}
		return true;
	}
	/** 
	 获取或者设置
	 是不是空的实体.
	*/
	public final boolean getIsEmpty()
	{
		if (this._row == null)
		{
			return true;
		}
		else
		{
			if (this.getPKVal() == null || this.getPKVal().toString().equals("0") || this.getPKVal().toString().equals(""))
			{
				return true;
			}
			return false;
		}
	}
	public final void setIsEmpty(boolean value)
	{
		this._row = null;
	}
	/** 
	 对这个实体的描述
	*/
	public final String getEnDesc()
	{
		return this.getEnMap().getEnDesc();
	}
	/** 
	 取到主健值。如果它的主健不唯一，就返回第一个值。
	 获取或设置
	*/
	public final Object getPKVal()
	{
		return this.GetValByKey(this.getPK());
	}
	public final void setPKVal(Object value)
	{
		this.SetValByKey(this.getPK(), value);
	}
	/** 
	 如果只有一个主键,就返回PK,如果有多个就返回第一个.PK
	*/
	public final int getPKCount()
	{
		switch (this.getPK())
		{
			case "OID":
			case "No":
			case "MyPK":
				return 1;
			default:
				break;
		}

		int i = 0;
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.PKFK)
			{
				i++;
			}
		}
		if (i == 0)
		{
			throw new RuntimeException("@没有给【" + this.getEnDesc() + "，" + this.getEnMap().getPhysicsTable() + "】定义主键。");
		}
		else
		{
			return i;
		}
	}
	/** 
	 是不是OIDEntity
	*/
	public final boolean getIsOIDEntity()
	{
		if (this.getPK().equals("OID"))
		{
			return true;
		}
		return false;
	}
	/** 
	 是不是OIDEntity
	*/
	public final boolean getIsNoEntity()
	{
		if (this.getPK().equals("No"))
		{
			return true;
		}
		return false;
	}
	/** 
	 是否是TreeEntity
	*/
	public final boolean getIsTreeEntity()
	{
		if (this.getPK().equals("ID"))
		{
			return true;
		}
		return false;
	}
	/** 
	 是不是IsMIDEntity
	*/
	public final boolean getIsMIDEntity()
	{
		if (this.getPK().equals("MID"))
		{
			return true;
		}
		return false;
	}
	/** 
	 如果只有一个主键,就返回PK,如果有多个就返回第一个.PK
	*/
	public String getPK()
	{
		String pks = "";
		for (Attr attr : this.getEnMap().getAttrs())
		{
			switch (attr.getKey())
			{
				case "OID":
					return "OID";
				case "No":
					return "No";
				case "MyPK":
					return "MyPK";
				default:
					break;
			}

			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.PKFK)
			{
				pks += attr.getKey() + ",";
			}
		}
		if (pks.equals(""))
		{
			throw new RuntimeException("@没有给【" + this.getEnDesc() + "，" + this.getEnMap().getPhysicsTable() + "】定义主键。");
		}
		pks = pks.substring(0, pks.length() - 1);
		return pks;
	}
	public String getPKField()
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
			switch (attr.getKey())
			{
				case "OID":
					return attr.getField();
				case "No":
					return attr.getField();
				case "MyPK":
					return attr.getField();
				default:
					break;
			}

			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.PKFK)
			{
				return attr.getField();
			}
		}

		throw new RuntimeException("@没有给【" + this.getEnDesc() + "】定义主键。");
	}
	/** 
	 如果只有一个主键,就返回PK,如果有多个就返回第一个.PK
	*/
	public final String[] getPKs()
	{
		String[] strs1 = new String[this.getPKCount()];
		int i = 0;
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.PK || attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.PKFK)
			{
				strs1[i] = attr.getKey();
				i++;
			}
		}
		return strs1;
	}
	/** 
	 取到主健值。
	*/
	public final Hashtable getPKVals()
	{
		Hashtable ht = new Hashtable();
		String[] strs = this.getPKs();
		for (String str : strs)
		{
			ht.put(str, this.GetValStringByKey(str));
		}
		return ht;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}