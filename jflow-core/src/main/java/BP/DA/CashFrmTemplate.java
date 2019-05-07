package BP.DA;

import BP.En.*;
import BP.Pub.*;
import BP.Sys.*;

import java.io.IOException;
import java.util.Hashtable;

public class CashFrmTemplate
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ����ht
	private static java.util.Hashtable _hts;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ��ʵ��Ĳ���.
	/** 
	 �����
	 
	 @param frmID ��ID
	 @param ds ��ģ��
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
	 �Ƴ�
	 
	 @param frmID ��ID
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
	 ��ñ�DataSetģʽ��ģ������
	 
	 @param frmID ��ID
	 @return ��ģ��
	 * @throws IOException 
	 * @throws ClassNotFoundException 
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
	 ��ñ�jsonģʽ��ģ������
	 
	 @param frmID ��ID
	 @return json
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion ��ʵ��Ĳ���.

}