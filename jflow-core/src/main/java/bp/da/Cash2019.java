package bp.da;

import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.en.Entities;
import bp.en.Row;
import bp.tools.StringUtils;

import java.util.Hashtable;

/**
 *  hashtable
 *  	--  entity
 *  		--  row
 */

/**
 实体缓存
*/
public class Cash2019
{

	 public static String redisKey = SystemConfig.getRedisCacheKey("Case2019");
	/**
	 清除所有的实体缓存.
	*/
	public static void ClearCash()
	{
		_hts = null;
		if(SystemConfig.getRedisIsEnable())
			ContextHolderUtils.getRedisUtils().del(false,redisKey);
	}

	//缓存ht
	private static Hashtable _hts;
	public static Hashtable getHts()
	{
		if (_hts == null)
		{
			_hts = new Hashtable();
		}
		return _hts;
	}
	///对实体的操作.
	/** 
	 把实体放入缓存里面
	 
	 param enName
	 param pkVal
	 param row
	*/
	public static void PutRow(String enName, String pkVal, Row row)  {
		if(SystemConfig.getRedisIsEnable()){
			if(StringUtils.isBlank(pkVal)) return;
			ContextHolderUtils.getRedisUtils().set(false, SystemConfig.getRedisCacheKey(enName + "_"+pkVal), row);
			return;
		}
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
	public static void UpdateRow(String enName, String pkVal, Row row)  {
		if(SystemConfig.getRedisIsEnable()){
			if(StringUtils.isBlank(pkVal)) return;
			ContextHolderUtils.getRedisUtils().set(false, SystemConfig.getRedisCacheKey(enName + "_"+pkVal), row);
			return;
		}
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
	public static void DeleteRow(String enName, String pkVal)  {
		if(SystemConfig.getRedisIsEnable()){
			ContextHolderUtils.getRedisUtils().del(false, SystemConfig.getRedisCacheKey(enName + "_"+pkVal));
			return;
		}
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
	private static final Object lockObj = new Object();
	/** 
	 获得实体类
	 
	 param enName 实体名字
	 param pkVal 键
	 @return row
	*/
	public static Row GetRow(String enName, String pkVal)  {
		if(SystemConfig.getRedisIsEnable()){
			Object obj = ContextHolderUtils.getRedisUtils().get(false,SystemConfig.getRedisCacheKey(enName + "_"+pkVal));
			return obj==null ? null : (Row) obj;
		}
		synchronized (lockObj)
		{
			Object tempVar = getHts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
				return null;
			if (ht.containsKey(pkVal) == true)
				return ht.get(pkVal) instanceof Row ? (Row)ht.get(pkVal) : null;
			return null;
		}
	}
	/// 对实体的操作.
}