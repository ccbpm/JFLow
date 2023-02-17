package bp.da;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.en.*;
import java.util.*;

public class CashEntity
{

		///Hashtable 属性
	private static Hashtable<String, Object> _Cash = new Hashtable<>();
	private static String dCashKey = SystemConfig.getRedisCacheKey("DCash");
	public static  Hashtable<String, Object> getDCash()throws Exception
	{
		if(SystemConfig.getRedisIsEnable())
			_Cash = ContextHolderUtils.getRedisUtils().hget(false,dCashKey);
		if (_Cash == null)
			_Cash = new Hashtable();
		return _Cash;
	}
	/** 
	 把实体放入缓存里面
	 param enName
	 param ens
	 param enPK
	 * @throws Exception 
	*/
	public static void PubEns(String enName, Entities ens, String enPK) throws Exception
	{
		Object tempVar = CashEntity.getDCash().get(enName);
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
		if (ht == null)
			ht = new Hashtable();

		ht.clear();
		for (Entity en : ens)
		{
			ht.put(en.GetValStrByKey(enPK), en);
		}
		// 把实体集合放入.
		CashEntity.getDCash().put(enName + "Ens", ens);
		if(SystemConfig.getRedisIsEnable())
			ContextHolderUtils.getRedisUtils().hset(false,dCashKey,enName + "Ens",ens);
	}
	public static Entities GetEns(String enName) throws Exception {
		Object tempVar = CashEntity.getDCash().get(enName + "Ens");
		Entities ens = tempVar instanceof Entities ? (Entities)tempVar : null;
		return ens;
	}
	/** 
	 更新对象
	 
	 param enName
	 param key
	 param en
	*/
	public static void Update(String enName, String key, Entity en) throws Exception {
		Object tempVar = CashEntity.getDCash().get(enName);
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
		if (ht == null)
		{
			ht = new Hashtable();
			CashEntity.getDCash().put(enName, ht);
		}
		ht.put(key, en);
		//清除集合.
		CashEntity.getDCash().remove(enName + "Ens");
		if(SystemConfig.getRedisIsEnable())
			ContextHolderUtils.getRedisUtils().hdel(false,dCashKey,enName + "Ens");
	}
	/** 
	 获取一个实体
	 
	 param enName 实体Name
	 param pkVal 主键值
	 @return 返回这个实体
	*/
	public static Entity Select(String enName, String pkVal) throws Exception {
		Object tempVar = CashEntity.getDCash().get(enName);
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
		if (ht == null)
		{
			return null;
		}

		return ht.get(pkVal) instanceof Entity ? (Entity)ht.get(pkVal) : null;
	}
	/** 
	 删除
	 
	 param enName
	 param pkVal
	*/
	public static void Delete(String enName, String pkVal) throws Exception {
		Object tempVar = CashEntity.getDCash().get(enName);
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
		if (ht == null)
		{
			return;
		}

		ht.remove(pkVal);
		//清除集合.
		CashEntity.getDCash().remove(enName + "Ens");
		if(SystemConfig.getRedisIsEnable()){
			ContextHolderUtils.getRedisUtils().hset(false,dCashKey,enName,ht);
			ContextHolderUtils.getRedisUtils().hdel(false,dCashKey,enName + "Ens");
		}
	}
	/** 
	 插入
	 
	 param enName
	 param en
	 param pkVal
	*/
	public static void Insert(String enName, String pkVal, Entity en) throws Exception {
		Object tempVar = CashEntity.getDCash().get(enName);
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
		if (ht == null)
		{
			return;
		}

		if (ht.containsKey(pkVal))
		{
			ht.put(pkVal, en);
		}
		else
		{
			ht.put(pkVal, en);
		}

		//清除集合.
		CashEntity.getDCash().remove(enName + "Ens");
		if(SystemConfig.getRedisIsEnable()){
			ContextHolderUtils.getRedisUtils().hset(false,dCashKey,enName,ht);
			ContextHolderUtils.getRedisUtils().hdel(false,dCashKey,enName + "Ens");
		}
	}
}