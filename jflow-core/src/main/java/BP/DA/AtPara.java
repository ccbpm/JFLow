package BP.DA;

import java.util.Hashtable;

public class AtPara
{
	/**
	 * 工作
	 */
	public final String getFK_Work()
	{
		return this.GetValStrByKey("FK_Work");
	}
	
	public final String getFK_ZJ()
	{
		return this.GetValStrByKey("FK_ZJ");
	}
	
	public final int getOID()
	{
		return this.GetValIntByKey("OID");
	}
	
	public final String getDoType()
	{
		return this.GetValStrByKey("DoType");
	}
	
	public AtPara()
	{
	}
	
	/**
	 * 执行一个para
	 * 
	 * @param para
	 */
	public AtPara(String para)
	{
		if (para == null)
		{
			return;
		}
		
		String[] strs = para.split("[@]", -1);
		for (String str : strs)
		{
			if (str == null || str.equals(""))
			{
				continue;
			}
			String[] mystr = str.split("[=]", -1);
			if (mystr.length == 2)
			{
				this.SetVal(mystr[0], mystr[1]);
			} else
			{
				String v = "";
				for (int i = 1; i < mystr.length; i++)
				{
					if (i == 1)
					{
						v += mystr[i];
					} else
					{
						v += "=" + mystr[i];
					}
				}
				this.SetVal(mystr[0], v);
			}
		}
	}
	
	public final void SetVal(String key, String val)
	{
		try
		{
			this.getHisHT().put(key, val);
		} catch (java.lang.Exception e)
		{
			this.getHisHT().put(key, "");
		}
	}
	
	public final String GetValStrByKey(String key)
	{
		try
		{
			return this.getHisHT().get(key).toString();
		} catch (java.lang.Exception e)
		{
			return "";
		}
	}
	
	public final boolean GetValBoolenByKey(String key)
	{
		if (this.GetValIntByKey(key) == 0)
		{
			return false;
		}
		return true;
	}
	
	public final boolean GetValBoolenByKey(String key, boolean isNullAsVal)
	{
		try
		{
			int i = Integer.parseInt(this.GetValStrByKey(key));
			if (i <= 0)
			{
				return false;
			}
			return true;
		} catch (java.lang.Exception e)
		{
			return isNullAsVal;
		}
	}
	
	public final float GetValFloatByKey(String key)
	{
		try
		{
			return Float.parseFloat(this.GetValStrByKey(key));
		} catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	
	public final int GetValIntByKey(String key)
	{
		try
		{
			return Integer.parseInt(this.GetValStrByKey(key));
		} catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	
	
	public final int GetValIntByKey(String key, int isNullAsVal)
		  {
			  try
			  {
				  return Integer.parseInt(this.GetValStrByKey(key));
			  }
			  catch (java.lang.Exception e)
			  {
				  return isNullAsVal;
			  }
		  }
	public final long GetValInt64ByKey(String key)
	{
		try
		{
			return Long.parseLong(this.GetValStrByKey(key));
		} catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	
	private Hashtable<String, String> _HisHT = null;
	
	public final Hashtable<String, String> getHisHT()
	{
		if (_HisHT == null)
		{
			_HisHT = new Hashtable<String, String>();
		}
		return _HisHT;
	}
	
	public final String GenerAtParaStrs()
	{
		String s = "";
		for (Object key : this.getHisHT().keySet())
		{
			s += "@" + key + "=" + this._HisHT.get(key).toString();
		}
		return s;
	}
}