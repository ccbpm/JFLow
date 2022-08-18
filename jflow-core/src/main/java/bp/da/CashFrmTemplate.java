package bp.da;
import java.util.*;

public class CashFrmTemplate
{

		///缓存ht
	private static Hashtable _hts;

		///


		///对实体的操作.
	/** 
	 放入表单
	 
	 param frmID 表单ID
	 param ds 表单模版
	 * @throws Exception 
	*/
	public static void Put(String frmID, DataSet ds) throws Exception
	{
		String json = bp.tools.Json.ToJson(ds);

		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new Hashtable();
			}

			if (_hts.containsKey(frmID) == false)
			{
				_hts.put(frmID, json);
			}
			else
			{
				_hts.put(frmID, json);
			}
		}
	}

	/** 
	 移除
	 
	 param frmID 表单ID
	*/
	public static void Remove(String frmID)
	{
		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new Hashtable();
			}

			_hts.remove(frmID);
		}
	}
	private static Object lockObj = new Object();
	/** 
	 获得表单DataSet模式的模版数据
	 
	 param frmID 表单ID
	 @return 表单模版
	*/
	public static DataSet GetFrmDataSetModel(String frmID) throws Exception {
		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new Hashtable();
			}

			if (_hts.containsKey(frmID) == true)
			{
				String json = _hts.get(frmID) instanceof String ? (String)_hts.get(frmID) : null;
				DataSet ds = bp.tools.Json.ToDataSet(json);
				return ds;
			}
			return null;
		}
	}
	/** 
	 获得表单json模式的模版数据
	 
	 param frmID 表单ID
	 @return json
	*/
	public static String GetFrmJsonModel(String frmID)
	{
		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new Hashtable();
			}

			if (_hts.containsKey(frmID) == true)
			{
				String json = _hts.get(frmID) instanceof String ? (String)_hts.get(frmID) : null;
				return json;
			}
			return null;
		}
	}

		/// 对实体的操作.

}