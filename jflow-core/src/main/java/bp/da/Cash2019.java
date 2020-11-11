package bp.da;
import java.util.Hashtable;
import bp.en.Entities;
import bp.en.Row;

/** 
 实体缓存
*/
public class Cash2019
{
	/** 
	 清除所有的实体缓存.
	*/
	public static void ClearCash()
	{
		_hts = null;
	}
	/** 
	 清除指定的缓存数据.
	 
	 @param enName 实体名称
	*/
	public static void ClearCashSpecEnName(String enName)
	{
		Object tempVar = getHts().get(enName);
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
		if (ht != null)
		{
			getHts().remove(enName);
		}
	}


		///缓存ht
	private static Hashtable _hts;
	public static Hashtable getHts()
	{
		if (_hts == null)
		{
			_hts = new Hashtable();
		}
		return _hts;
	}

		///


		///对实体的操作.
	/** 
	 把实体放入缓存里面
	 
	 @param enName
	 @param ens
	 @param enPK
	*/
	public static void PutRow(String enName, String pkVal, Row row)
	{
		synchronized (lockObj)
		{
			Object tempVar = getHts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				ht = new Hashtable();
				getHts().put(enName, ht);
			}
			if (ht.containsKey(pkVal) == true)
			{
				ht.put(pkVal, row);
			}
			else
			{
				ht.put(pkVal, row);
			}
		}
	}
	public static void UpdateRow(String enName, String pkVal, Row row)
	{
		synchronized (lockObj)
		{
			Object tempVar = getHts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				ht = new Hashtable();
				getHts().put(enName, ht);
			}
			ht.put(pkVal, row);
		}
	}
	public static void DeleteRow(String enName, String pkVal)
	{
		synchronized (lockObj)
		{
			Object tempVar = getHts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				ht = new Hashtable();
				getHts().put(enName, ht);
			}
			ht.remove(pkVal.toString());
		}
	}
	private static Object lockObj = new Object();
	/** 
	 获得实体类
	 
	 @param enName 实体名字
	 @param pkVal 键
	 @return row
	*/
	public static Row GetRow(String enName, String pkVal)
	{
		synchronized (lockObj)
		{
			Object tempVar = getHts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				return null;
			}
			if (ht.containsKey(pkVal) == true)
			{
				return ht.get(pkVal) instanceof Row ? (Row)ht.get(pkVal) : null;
			}
			return null;
		}
	}

		/// 对实体的操作.


		///对实体的集合操作.
	/** 
	 把集合放入缓存.
	 
	 @param ensName 集合实体类名
	 @param ens 实体集合
	*/
	public static void PutEns(String ensName, Entities ens)
	{

	}
	/** 
	 获取实体集合类
	 
	 @param ensName 集合类名
	 @param pkVal 主键
	 @return 实体集合
	*/
	public static Entities GetEns(String ensName, Object pkVal)
	{
		return null;
	}

		/// 对实体的集合操作.

}