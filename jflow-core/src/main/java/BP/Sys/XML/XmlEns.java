package BP.Sys.XML;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Sys.*;
import java.util.*;
import java.io.*;
import java.math.*;

/** 
 XmlEn 的摘要说明。
*/
public abstract class XmlEns extends ArrayList<Object>
{
	public final int LoadXmlFile(String file)
	{
		return LoadXmlFile(file, this.getTableName());
	}
	public final int LoadXmlFile(String file, String table)
	{
		DataSet ds = new DataSet();
		ds.ReadXml(file);
		DataTable dt = ds.Tables[table];
		for (DataRow dr : dt.Rows)
		{
			XmlEn en = this.getGetNewEntity();
			en.setRow(new Row());
			en.getRow().LoadDataTable(dt, dr);
			this.Add(en);
		}
		return dt.Rows.Count;
	}
	public final boolean Contine(String key, String val)
	{
		for (XmlEn en : this)
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				return true;
			}
		}
		return false;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 构造
	*/
	public XmlEns()
	{

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询方法
	public final String getTname()
	{
		String tname = this.getFile().replace(".TXT", "").replace(".txt", "");
		tname = tname.substring(tname.lastIndexOf("\\") + 1) + this.getTableName() + "_X";
		return tname;
	}

	private DataTable GetTableTxts(File[] fis)
	{
		Object tempVar = BP.DA.Cash.GetObj(this.getTname(), Depositary.Application);
		DataTable cdt = tempVar instanceof DataTable ? (DataTable)tempVar : null;
		if (cdt != null)
		{
			return cdt;
		}

		DataTable dt = new DataTable(this.getTableName());
		for (File fi : fis)
		{
			dt = GetTableTxt(dt, fi);
		}

		BP.DA.Cash.AddObj(this.getTname(), Depositary.Application, dt);
		return dt;
	}
	private DataTable GetTableTxt()
	{
		Object tempVar = BP.DA.Cash.GetObj(this.getTname(), Depositary.Application);
		DataTable cdt = tempVar instanceof DataTable ? (DataTable)tempVar : null;
		if (cdt != null)
		{
			return cdt;
		}

		DataTable dt = new DataTable(this.getTableName());
		File fi = new File(this.getFile());
		dt = GetTableTxt(dt, fi);

		BP.DA.Cash.AddObj(this.getTname(), Depositary.Application, dt);
		return dt;
	}
	private DataTable GetTableTxt(DataTable dt, File file)
	{
		InputStreamReader sr = new InputStreamReader(file.getPath(), System.Text.ASCIIEncoding.GetEncoding("GB2312"));
		Hashtable ht = new Hashtable();
		String key = "";
		String val = "";
		while (true)
		{
			if (sr.EndOfStream)
			{
				break;
			}
			String lin = sr.ReadLine();
			if (lin.equals("") || lin == null)
			{
				continue;
			}


			if (lin.indexOf("*") == 0)
			{
				/* 遇到注释文件 */
				continue;
			}

			if (lin.indexOf("=") == 0 || sr.EndOfStream)
			{
				/* 约定的行记录, 开始以 = 开始就认为是一个新的记录。 */
				// 处理表结构。
				for (String ojbkey : ht.keySet())
				{
					if (dt.Columns.Contains(ojbkey) == false)
					{
						dt.Columns.Add(new DataColumn(ojbkey, String.class));
					}
				}

				DataRow dr = dt.NewRow();
				for (String ojbkey : ht.keySet())
				{
					dr.set(ojbkey, ht.get(ojbkey));
				}

				if (ht.keySet().size() > 1)
				{
					dt.Rows.Add(dr);
				}


				ht.clear(); // clear hashtable.
				if (sr.EndOfStream)
				{
					break;
				}
				continue;
			}

			int idx = lin.indexOf("=");
			if (idx == -1)
			{
				throw new RuntimeException(this.getFile() + "@不符合规则 key =val 的规则。");
			}

			key = lin.substring(0, idx);
			if (key.equals(""))
			{
				continue;
			}

			val = lin.substring(idx + 1);
			ht.put(key, val);
		}


		return dt;
	}
	public final DataTable GetTable()
	{
		Object tempVar = BP.DA.Cash.GetObj(this.getTname(), Depositary.Application);
		DataTable cdt = tempVar instanceof DataTable ? (DataTable)tempVar : null;
		if (cdt != null)
		{
			return cdt;
		}

		if (this.getFile().toLowerCase().indexOf(".txt") > 0)
		{
			return this.GetTableTxt();
		}

		if (this.getFile().toLowerCase().indexOf(".xml") > 0)
		{
			DataSet ds1 = new DataSet();
			ds1.ReadXml(this.getFile());
			DataTable mdt = ds1.Tables[this.getTableName()];
			if (mdt == null)
			{
				mdt = new DataTable();
			}

			BP.DA.Cash.AddObj(this.getTname(), Depositary.Application, mdt);

			return ds1.Tables[this.getTableName()];
		}

		/* 说明这个是目录 */
		File di = new File(this.getFile());
		if (di.exists() == false)
		{
			throw new RuntimeException("文件不存在:" + this.getFile());
		}

		File[] fis = di.GetFiles("*.xml");
		if (fis.length == 0)
		{
			fis = di.GetFiles("*.txt");
			return this.GetTableTxts(fis);
		}

		DataTable dt = new DataTable(this.getTableName());
		if (fis.length == 0)
		{
			return dt;
		}

		DataTable tempDT = new DataTable();
		for (File fi : fis)
		{

			DataSet ds = new DataSet("myds");
			try
			{
				ds.ReadXml(this.getFile() + "\\" + fi.getName());
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("读取文件:" + fi.getName() + "错误。Exception=" + ex.getMessage());
			}
			try
			{
				//ds.
				if (dt.Columns.Count == 0)
				{
					/* 如果表还是空的，没有任何结构。*/
					try
					{
						dt = ds.Tables[this.getTableName()];
					}
					catch (RuntimeException ex)
					{
						throw new RuntimeException("可能是没有在" + fi.getName() + "文件中找到表:" + this.getTableName() + " exception=" + ex.getMessage());
					}
					tempDT = dt.Clone();
					continue;
				}

				DataTable mydt = ds.Tables[this.getTableName()];
				if (mydt == null)
				{
					throw new RuntimeException("无此表:" + this.getTableName());
				}

				if (mydt.Rows.Count == 0)
				{
					continue;
				}

				for (DataRow mydr : mydt.Rows)
				{
					//dt.ImportRow(mydr);
					DataRow dr = dt.NewRow();

					for (DataColumn dc : tempDT.Columns)
					{
						//string "sd".Clone();
						if (dc.ColumnName.indexOf("_Id") != -1)
						{
							continue;
						}

						try
						{
							Object obj = mydr.get(dc.ColumnName);
							dr.set(dc.ColumnName, obj);
						}
						catch (RuntimeException ex)
						{
							throw new RuntimeException("xml 配置错误，多个文件中的属性不对称。" + ex.getMessage());
						}
					}

					dt.Rows.Add(dr);
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("获取数据出现错误:fileName=" + fi.getName() + " clasName=" + this.toString() + " MoreInfo=" + ex.getMessage());
			}
		}
		BP.DA.Cash.AddObj(this.getTname(), Depositary.Application, dt);
		return dt;
	}
	public int RetrieveAllFromDBSource()
	{
		BP.DA.Cash.RemoveObj(this.getTname());
		return this.RetrieveAll();
	}
	/** 
	 装载XML
	*/
	public int RetrieveAll()
	{
		this.Clear(); // 清所有的信息。
		Object tempVar = BP.DA.Cash.GetObj(this.toString(), Depositary.Application);
		XmlEns ens = tempVar instanceof XmlEns ? (XmlEns)tempVar : null;
		if (ens != null)
		{
			for (XmlEn en : ens)
			{
				this.Add(en);
			}
			return ens.Count;
		}

		// 从内存中找。
		DataTable dt = this.GetTable();
		for (DataRow dr : dt.Rows)
		{
			XmlEn en = this.getGetNewEntity();
			en.setRow(new Row());
			en.getRow().LoadDataTable(dt, dr);
			this.Add(en);
		}

		BP.DA.Cash.AddObj(this.toString(), Depositary.Application, this);
		return dt.Rows.Count;
	}
	public final void FullEnToCash(String pk)
	{
		this.RetrieveAll();
		XmlEn myen = this.getGetNewEntity();
		for (XmlEn en : this)
		{
			Cash.AddObj(myen.toString() + en.GetValByKey(pk), Depositary.Application, en);
		}
	}

	public final int RetrieveByLength(String key, int len)
	{
		this.Clear(); //清所有的信息
		DataTable dt = this.GetTable();
		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			if (dr.get(key).toString().length() == len)
			{
				XmlEn en = this.getGetNewEntity();
				en.setRow(new Row());
				en.getRow().LoadDataTable(dt, dr);
				this.Add(en);
				i++;
			}
		}
		return i;
	}
	public final int Retrieve(String key, Object val)
	{
		return RetrieveBy(key, val);
	}
	public final int Retrieve(String key, Object val, String key1, String val1)
	{
		this.Clear(); //清所有的信息
		DataTable dt = this.GetTable();
		if (dt == null)
		{
			throw new RuntimeException("@错误：类" + this.getGetNewEntity().toString() + " File= " + this.getFile() + " Table=" + this.getTableName() + " ，没有取到数据。");
		}

		int i = 0;
		try
		{
			for (DataRow dr : dt.Rows)
			{
				if (dr.get(key).toString().equals(val.toString()) && dr.get(key1).toString().equals(val1))
				{
					XmlEn en = this.getGetNewEntity();
					en.setRow(new Row());
					en.getRow().LoadDataTable(dt, dr);
					this.Add(en);
					i++;
				}
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@装载错误:file=" + this.getFile() + " xml:" + this.toString() + "Err:" + ex.getMessage());
		}
		return i;
	}
	/** 
	 按照键值查询
	 
	 @param key 要查询的健
	 @param val 值
	 @return 返回查询的个数
	*/
	public final int RetrieveBy(String key, Object val)
	{
		if (val == null)
		{
			return 0;
		}

		this.Clear(); //清所有的信息
		DataTable dt = this.GetTable();
		if (dt == null)
		{
			throw new RuntimeException("@错误：类" + this.getGetNewEntity().toString() + " File= " + this.getFile() + " Table=" + this.getTableName() + " ，没有取到数据。");
		}

		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			if (dr.get(key).toString().equals(val.toString()))
			{
				XmlEn en = this.getGetNewEntity();
				en.setRow(new Row());
				en.getRow().LoadDataTable(dt, dr);
				this.Add(en);
				i++;
			}
		}
		return i;
	}

	public final int RetrieveBy(String key, Object val, String orderByAttr)
	{
		DataTable dt = this.GetTable();
		DataView dv = new DataView(dt, orderByAttr,orderByAttr,DataViewRowState.Unchanged);

		this.Clear(); //清所有的信息.
		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			if (dr.get(key).toString().equals(val.toString()))
			{
				XmlEn en = this.getGetNewEntity();
				en.setRow(new Row());
				en.getRow().LoadDataTable(dt, dr);
				this.Add(en);
				i++;
			}
		}
		return i;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共方法
	public final XmlEn Find(String key, Object val)
	{
		for (XmlEn en : this)
		{
			if (val.toString().equals(en.GetValStringByKey(key)))
			{
				return en;
			}
		}
		return null;

	}
	public final boolean IsExits(String key, Object val)
	{
		for (XmlEn en : this)
		{
			if (val.toString().equals(en.GetValStringByKey(key)))
			{
				return true;
			}
		}
		return false;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  增加 便利访问
	public final XmlEn GetEnByKey(String key, String val)
	{
		for (XmlEn en : this)
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				return en;
			}
		}
		return null;

	}
	/** 
	 根据位置取得数据
	*/
	public final XmlEn get(int index)
	{
		return (XmlEn)this.InnerList[index];
	}
	/** 
	 获取数据
	*/
	public final XmlEn get(String key, String val)
	{
		for (XmlEn en : this)
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				return en;
			}
		}
		throw new RuntimeException("在[" + this.getTableName() + "," + this.getFile() + "," + this.toString() + "]没有找到key=" + key + ", val=" + val + "的实例。");
	}
	/** 
	 增加一个xml en to Ens.
	 
	 @param entity
	 @return 
	*/
	public int Add(XmlEn entity)
	{
		return this.InnerList.add(entity);
	}
	public int Add(XmlEns ens)
	{
		if (ens == null)
		{
			return 0;
		}

		for (XmlEn en : ens)
		{
			this.InnerList.add(en);
		}
		return ens.Count;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与 entities 接口
	/** 
	 把数据装入一个实例集合中（把xml数据装入物理表中）
	 
	 @param ens 实体集合
	*/
	public final void FillXmlDataIntoEntities(Entities ens)
	{
		this.RetrieveAll(); // 查询出来全部的数据。
		Entity en1 = ens.getGetNewEntity();

		String[] pks = en1.getPKs();
		for (XmlEn xmlen : this)
		{

			Entity en = ens.getGetNewEntity();
			for (String pk : pks)
			{
				Object obj = xmlen.GetValByKey(pk);
				en.SetValByKey(pk, obj);
			}

			try
			{
				en.RetrieveFromDBSources();
			}
			catch (RuntimeException ex)
			{
				en.CheckPhysicsTable();
				Log.DebugWriteError(ex.getMessage());
			}


			for (String s : xmlen.getRow().keySet())
			{
				Object obj = xmlen.GetValByKey(s);
				if (obj == null)
				{
					continue;
				}
				if (obj.toString().equals(""))
				{
					continue;
				}
				if (obj.toString().equals("None"))
				{
					continue;
				}
				if (obj == DBNull.Value)
				{
					continue;
				}
				en.SetValByKey(s, obj);
			}
			en.Save();
		}
	}
	public final void FillXmlDataIntoEntities()
	{
		if (this.getRefEns() == null)
		{
			return;
		}
		this.FillXmlDataIntoEntities(this.getRefEns());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 子类实现xml 信息的描述.
	public abstract XmlEn getGetNewEntity();
	/** 
	 获取它所在的xml file 位置.
	*/
	public abstract String getFile();
	/** 
	 物理表名称(可能一个xml文件中有n个Table.)
	*/
	public abstract String getTableName();
	public abstract Entities getRefEns();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final DataTable ToDataTable()
	{
		DataTable dt = new DataTable(this.getTableName());

		if (this.Count == 0)
		{
			return dt;
		}

		XmlEn en = this.get(0) instanceof XmlEn ? (XmlEn)this.get(0) : null;
		Row r = en.getRow();
		for (String key : r.keySet())
		{
			dt.Columns.Add(key, String.class);
		}

		for (XmlEn en1 : this)
		{
			DataRow dr = dt.NewRow();
			for (String key : r.keySet())
			{
				dr.set(key, en1.GetValStringByKey(key));
			}
			dt.Rows.Add(dr);
		}

		return dt;
	}
}