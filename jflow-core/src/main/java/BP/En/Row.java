package BP.En;

import BP.DA.*;
import java.util.*;
import java.math.*;

/** 
 Row 的摘要说明。
 用来处理一条记录
*/
public class Row extends Hashtable
{
	public Row()
	{
		super(System.StringComparer.Create(System.Globalization.CultureInfo.CurrentCulture, true));
	}
	/** 
	 初始化数据.
	 
	 @param attrs
	*/
	public final void LoadAttrs(Attrs attrs)
	{
		this.clear();
		for (Attr attr : attrs)
		{
			switch (attr.getMyDataType())
			{
				case BP.DA.DataType.AppInt:
					if (attr.getIsNull())
					{
						this.put(attr.getKey(), DBNull.Value);
					}
					else
					{
						this.put(attr.getKey(), Integer.parseInt(attr.getDefaultVal().toString()));
					}
					break;
				case BP.DA.DataType.AppFloat:
					if (attr.getIsNull())
					{
						this.put(attr.getKey(), DBNull.Value);
					}
					else
					{
						this.put(attr.getKey(), Float.parseFloat(attr.getDefaultVal().toString()));
					}
					break;
				case BP.DA.DataType.AppMoney:
					if (attr.getIsNull())
					{
						this.put(attr.getKey(), DBNull.Value);
					}
					else
					{
						this.put(attr.getKey(), BigDecimal.Parse(attr.getDefaultVal().toString()));
					}
					break;
				case BP.DA.DataType.AppDouble:
					if (attr.getIsNull())
					{
						this.put(attr.getKey(), DBNull.Value);
					}
					else
					{
						this.put(attr.getKey(), Double.parseDouble(attr.getDefaultVal().toString()));
					}
					break;
				default:
					this.put(attr.getKey(), attr.getDefaultVal());
					break;
			}
		}
	}
	/** 
	 LoadAttrs
	 
	 @param attrs
	*/
	public final void LoadDataTable(DataTable dt, DataRow dr)
	{
		this.clear();
		for (DataColumn dc : dt.Columns)
		{
			this.put(dc.ColumnName, dr.get(dc.ColumnName));
		}
	}
	/** 
	 设置一个值by key . 
	 
	 @param key
	 @param val
	*/
	public final void SetValByKey(String key, Object val)
	{
		if (key == null)
		{
			return;
		}

		// warning 需要商榷，不增加就会导致赋值错误.
		if (this.containsKey(key) == false)
		{
			this.put(key, val);
			return;
		}

		if (val == null)
		{
			this.put(key, val);
			return;
		}

		if (val.getClass() == TypeCode.class)
		{
			this.put(key, (Integer)val);
		}
		else
		{
			this.put(key, val);
		}
	}


	public final boolean GetBoolenByKey(String key)
	{
		Object obj = this.get(key);
		if (obj == null || DataType.IsNullOrEmpty(obj.toString()) == true || obj.toString().equals("0"))
		{
			return false;
		}
		return true;
	}
	public final Object GetValByKey(String key)
	{
		return this.get(key);
		/*
		if (SystemConfig.IsDebug)
		{
		    try
		    {
		        return this[key];
		    }
		    catch(Exception ex)
		    {
		        throw new Exception("@GetValByKey没有找到key="+key+"的属性Vale , 请确认Map 里面是否有此属性."+ex.Message);
		    }
		}
		else
		{
		    return this[key];
		}
		*/
	}

}