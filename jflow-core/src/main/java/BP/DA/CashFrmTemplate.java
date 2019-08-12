package BP.DA;
import java.io.IOException;

public class CashFrmTemplate
{
	//缓存
	private static java.util.Hashtable _hts;

	/**
	 * 放入表单
	 * @param frmID
	 * @param ds
	 */
	public static void Put(String frmID, DataSet ds)
	{
		String json = BP.Tools.Json.ToJson(ds);

		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new java.util.Hashtable();
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
	 *  移除
	 * @param frmID
	 */
	public static void Remove(String frmID)
	{
		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new java.util.Hashtable();
			}

			_hts.remove(frmID);
		}
	}
	private static Object lockObj = new Object();

	/**
	 * 获得表单DataSet模式的模版数据
	 * @param frmID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static DataSet GetFrmDataSetModel(String frmID) throws ClassNotFoundException, IOException
	{
		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new java.util.Hashtable();
			}

			if (_hts.containsKey(frmID) == true)
			{
				String json = (String)((_hts.get(frmID) instanceof String) ? _hts.get(frmID) : null);
				
				DataSet ds = BP.Tools.Json.ToDataSet(json);
				return ds;
			}
			return null;
		}
	}

	/**
	 * 获得表单json模式的模版数据
	 * @param frmID
	 * @return
	 */
	public static String GetFrmJsonModel(String frmID)
	{
		synchronized (lockObj)
		{
			if (_hts == null)
			{
				_hts = new java.util.Hashtable();
			}

			if (_hts.containsKey(frmID) == true)
			{
				String json = (String)((_hts.get(frmID) instanceof String) ? _hts.get(frmID) : null);
				return json;
			}
			return null;
		}
	}
}