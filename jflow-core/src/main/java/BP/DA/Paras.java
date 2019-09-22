package BP.DA;

import BP.Sys.*;
import java.math.*;

/** 
 参数集合
*/
public class Paras extends ArrayList<Object>
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 特殊处理
	public final void AddFK_Emp(String userNo)
	{
		this.Add("FK_Emp", userNo);
	}
	public final void AddFK_Emp()
	{
		this.Add("FK_Emp", Web.WebUser.getNo());
	}

	public final void AddFK_NY(String fk_ny)
	{
		this.Add("FK_NY", fk_ny);
	}
	public final void AddFK_NY()
	{
		this.Add("FK_NY", DataType.getCurrentYearMonth());
	}


	public final void AddFK_Dept(String val)
	{
		this.Add("FK_Dept", val);
	}
	public final void AddFK_Dept()
	{
		this.Add("FK_Dept", BP.Web.WebUser.getFK_Dept());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String ToDesc()
	{
		String msg = "";
		for (Para p : this)
		{
			msg += "@" + p.ParaName + " = " + p.val;
		}
		return msg;
	}

	public String SQL = null;
	public final String getSQLNoPara()
	{
		Object tempVar = this.SQL.Clone();
		String mysql = tempVar instanceof String ? (String)tempVar : null;
		for (Para p : this)
		{
			if (p.DAType == System.Data.DbType.String)
			{
				if (mysql.contains(SystemConfig.getAppCenterDBVarStr() + p.ParaName + ","))
				{
					mysql = mysql.replace(SystemConfig.getAppCenterDBVarStr() + p.ParaName + ",", "'" + p.val.toString() + "',");
				}
				else
				{
					mysql = mysql.replace(SystemConfig.getAppCenterDBVarStr() + p.ParaName, "'" + p.val.toString() + "'");
				}
			}
			else
			{
				if (mysql.contains(SystemConfig.getAppCenterDBVarStr() + p.ParaName + ","))
				{
					mysql = mysql.replace(SystemConfig.getAppCenterDBVarStr() + p.ParaName + ",", p.val.toString() + ",");
				}
				else
				{
					mysql = mysql.replace(SystemConfig.getAppCenterDBVarStr() + p.ParaName, p.val.toString());
				}
			}
		}
		return mysql;
	}

	public Paras()
	{
	}
	public Paras(Object o)
	{
		this.Add("p", o);
	}
	public final String getDBStr()
	{
		return BP.Sys.SystemConfig.getAppCenterDBVarStr();
	}
	public Paras(String p, Object v)
	{
		this.Add(p, v);
	}
	public Paras(String p1, Object o1, String p2, Object o2)
	{
		this.Add(p1, o1);
		this.Add(p2, o2);
	}

	public final boolean COntinckey(String key)
	{
		for (Para p : this)
		{
			if (p.ParaName.equals(key))
			{
				return true;
			}
		}
		return false;
	}

	public final void Add(Para para)
	{
		for (Para p : this)
		{
			if (p.ParaName.equals(para.ParaName))
			{
				p.val = para.val;
				return;
			}
		}

		this.InnerList.add(para);
	}
	public final void Add(Object obj)
	{
	   this.Add("p", obj);
	}
	public final void Add(String _name, Object obj)
	{
		if (_name.equals("abc"))
		{
			return;
		}

		if (obj == null)
		{
			throw new RuntimeException("@参数:" + _name + " 值无效.");
		}

		for (Para p : this)
		{
			if (p.ParaName.equals(_name))
			{
				p.val = obj;
				return;
			}
		}

		// 2019-8-8 适配pgsql数据库的新版驱动，要求数据类型一致
		//if (String.Compare("FK_Node", _name, StringComparison.OrdinalIgnoreCase) == 0)
		//{
		//    this.Add(_name, Convert.ToInt32(obj));
		//    return;
		//}
		//if (String.Compare("WorkID", _name, StringComparison.OrdinalIgnoreCase) == 0)
		//{
		//    this.Add(_name, Convert.ToInt64(obj));
		//    return;
		//}

		if (obj.getClass() == String.class)
		{
			this.Add(_name, obj.toString());
			return;
		}

		if (obj.getClass() == Integer.class || obj.getClass() == Integer.class || obj.getClass() == Short.class)
		{
			this.Add(_name, Integer.parseInt(obj.toString()));
			return;
		}

		if (obj.getClass() == Long.class)
		{
			this.Add(_name, Long.parseLong(obj.toString()));
			return;
		}

		if (obj.getClass() == Float.class)
		{
			this.Add(_name, Float.parseFloat(obj.toString()));
			return;
		}

		if (obj.getClass() == BigDecimal.class)
		{
			this.Add(_name, BigDecimal.Parse(obj.toString()));
			return;
		}

		if (obj.getClass() == Double.class)
		{
			this.Add(_name, Double.parseDouble(obj.toString()));
			return;
		}

		if (obj == DBNull.Value)
		{
			this.AddDBNull(_name);
		}
		else
		{
			throw new RuntimeException("@没有判断的参数类型:" + _name);
		}
		//     this.Add(_name, obj.ToString());
	}
	/** 
	 是否是大块文本?
	 
	 @param _name 名称
	 @param _val 值
	 @param isBigTxt 是否是大文本?
	*/

	public final void Add(String _name, String _val)
	{
		Add(_name, _val, false);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void Add(string _name, string _val, bool isBigTxt=false)
	public final void Add(String _name, String _val, boolean isBigTxt)
	{
		Para en = new Para();

		en.DAType = System.Data.DbType.String;
		en.val = _val;
		en.ParaName = _name;
		en.Size = _val.length();
		en.IsBigText = isBigTxt; //是否是大块文本.
		this.Add(en);
	}

	public final void Add(String _name, int _val)
	{
		Para en = new Para();
		en.DAType = System.Data.DbType.Int32;
		en.val = _val;
		en.ParaName = _name;
		this.Add(en);
	}
	public final void Add(String _name, long _val)
	{
		Para en = new Para();
		en.DAType = System.Data.DbType.Int64;
		en.val = _val;
		en.ParaName = _name;
		this.Add(en);
	}
	public final void Add(String _name, float _val)
	{
		Para en = new Para();
		en.DAType = System.Data.DbType.Decimal;
		//   en.DAType = System.Data.DbType.Int64;
		en.val = _val;
		en.ParaName = _name;
		this.Add(en);
	}
	public final void AddDBNull(String _name)
	{
		Para en = new Para();
		en.DAType = System.Data.DbType.Object;
		en.val = DBNull.Value;
		en.ParaName = _name;
		this.Add(en);
	}
	public final void Add(String _name, BigDecimal _val)
	{
		Para en = new Para();
		en.DAType = System.Data.DbType.Decimal;
		en.val = _val;
		en.ParaName = _name;
		this.Add(en);
	}
	public final void Add(String _name, double _val)
	{
		Para en = new Para();
		en.DAType = System.Data.DbType.Decimal;
		en.val = _val;
		en.ParaName = _name;
		this.Add(en);
	}
	public final void Remove(String paraName)
	{
		int i = 0;
		for (Para p : this)
		{
			if (p.ParaName.equals(paraName))
			{
				break;
			}
			i++;
		}
		this.RemoveAt(i);
	}
}