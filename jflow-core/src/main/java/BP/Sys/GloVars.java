package BP.Sys;

import BP.DA.DataType;
import BP.En.EntitiesNoName;
import BP.En.Entity;


/**
 * 全局变量s
 */
public class GloVars extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// get value by key
	/**
	 * 设置配置文件
	 * 
	 * @param key
	 *            key
	 * @param val
	 *            val
	 */
	public static int SetValByKey(String key, Object val)
	{
		GloVar en = new GloVar(key, val);
		en.setValOfObject(val);
		return en.Update();
	}
	
	/**
	 * 获取html数据
	 * 
	 * @param key
	 * @return
	 */
	public static String GetValByKeyHtml(String key)
	{
		return DataType.ParseText2Html(GloVars.GetValByKey(key));
	}
	
	public static String GetValByKey(String key)
	{
		for (Object cfg : GloVars.getMyGloVars())
		{
			if (((GloVar) cfg).getNo().equals(key))
			{
				return ((GloVar) cfg).getVal();
			}
		}
		
		throw new RuntimeException("error key=" + key);
	}
	
	/**
	 * 得到，一个key.
	 * 
	 * @param key
	 * @return
	 */
	public static String GetValByKey(String key, String isNullAs)
	{
		for (Object cfg : GloVars.getMyGloVars())
		{
			if (((GloVar) cfg).getNo().equals(key))
			{
				return ((GloVar) cfg).getVal();
			}
		}
		
		GloVar en = new GloVar(key, isNullAs);
		// GloVar en = new GloVar(key);
		return en.getVal();
	}
	
	public static int GetValByKeyInt(String key, int isNullAs)
	{
		for (Object cfg : GloVars.getMyGloVars())
		{
			if (((GloVar) cfg).getNo().equals(key))
			{
				return ((GloVar) cfg).getValOfInt();
			}
		}
		
		GloVar en = new GloVar(key, isNullAs);
		// GloVar en = new GloVar(key);
		return en.getValOfInt();
	}
	
	public static int GetValByKeyDecimal(String key, int isNullAs)
	{
		for (Object cfg : GloVars.getMyGloVars())
		{
			if (((GloVar) cfg).getNo().equals(key))
			{
				return ((GloVar) cfg).getValOfInt();
			}
		}
		
		GloVar en = new GloVar(key, isNullAs);
		// GloVar en = new GloVar(key);
		return en.getValOfInt();
	}
	
	public static boolean GetValByKeyBoolen(String key, boolean isNullAs)
	{
		
		for (Object cfg : GloVars.getMyGloVars())
		{
			if (((GloVar) cfg).getNo().equals(key))
			{
				return ((GloVar) cfg).getValOfBoolen();
			}
		}
		
		int val = 0;
		if (isNullAs)
		{
			val = 1;
		}
		
		GloVar en = new GloVar(key, val);
		
		return en.getValOfBoolen();
	}
	
	public static float GetValByKeyFloat(String key, float isNullAs)
	{
		for (Object cfg : GloVars.getMyGloVars())
		{
			if (((GloVar) cfg).getNo().equals(key))
			{
				return ((GloVar) cfg).getValOfFloat();
			}
		}
		
		GloVar en = new GloVar(key, isNullAs);
		return en.getValOfFloat();
	}
	
	private static GloVars _MyGloVars = null;
	
	public static GloVars getMyGloVars()
	{
		if (_MyGloVars == null)
		{
			_MyGloVars = new GloVars();
			_MyGloVars.RetrieveAll();
		}
		return _MyGloVars;
	}
	
	public static void ReSetVal()
	{
		_MyGloVars = null;
	}
	
	// 构造
	/**
	 * 全局变量s
	 */
	public GloVars()
	{
	}
	
	/**
	 * 全局变量s
	 * 
	 * @param fk_mapdata
	 *            s
	 */
	public GloVars(String fk_mapdata)
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);
		} else
		{
			this.RetrieveFromCash(FrmLineAttr.FK_MapData, (Object) fk_mapdata);
		}
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new GloVar();
	}
}