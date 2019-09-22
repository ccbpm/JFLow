package BP.Sys.XML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import BP.DA.Cash;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Row;

/**
 * XmlEn 的摘要说明。
 */
public abstract class XmlEns extends ArrayList<XmlEn>
{
	
	public static ArrayList<XmlEn> convertXmlEns(Object obj)
	{
		return (ArrayList<XmlEn>) obj;
	}
	public List<XmlEn> ToJavaListXmlEns()
	{
		return (List<XmlEn>)(Object)this;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final int LoadXmlFile(String file)
	{
		return LoadXmlFile(file, this.getTableName());
	}
	
	public final int LoadXmlFile(String file, String table)
	{
		DataSet ds = new DataSet();
		ds.readXml(file);
		DataTable dt = ds.hashTables.get(table);
		/*
		 * warning DataTable dt = ds.Tables[table];
		 */
		for (DataRow dr : dt.Rows)
		{
			XmlEn en = this.getNewEntity();
			en.setRow(new Row());
			en.getRow().LoadDataTable(dt, dr);
			this.Add(en);
		}
		return dt.Rows.size();
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
	
	//  构造
	/**
	 * 构造
	 */
	public XmlEns()
	{
		
	}
	
	//  构造
	
	//  查询方法
	public final String getTname()
	{
		String tname = this.getFile().replace(".TXT", "").replace(".txt", "");
		tname = tname.substring(tname.lastIndexOf("/") + 1)
				+ this.getTableName() + "_X";
		return tname;
	}
	
	/*
	 * warning private DataTable GetTableTxts(FileInfo[] fis) {
	 */
	private DataTable GetTableTxts(List<File> fis)
	{
		
		Object tempVar = BP.DA.Cash.GetObj(this.getTname(),
				Depositary.Application);
		DataTable cdt = (DataTable) ((tempVar instanceof DataTable) ? tempVar
				: null);
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
		
		Object tempVar = BP.DA.Cash.GetObj(this.getTname(),
				Depositary.Application);
		DataTable cdt = (DataTable) ((tempVar instanceof DataTable) ? tempVar
				: null);
		if (cdt != null)
		{
			return cdt;
		}
		
		DataTable dt = new DataTable(this.getTableName());
		/*
		 * warning FileInfo fi = new FileInfo(this.getFile());
		 */
		File fi = new File(this.getFile());
		dt = GetTableTxt(dt, fi);
		
		BP.DA.Cash.AddObj(this.getTname(), Depositary.Application, dt);
		return dt;
	}
	
	/*
	 * warning private DataTable GetTableTxt(DataTable dt, FileInfo file) {
	 */
	private DataTable GetTableTxt(DataTable dt, File file)
	{
		BufferedReader br = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			String key = "";
			String val = "";
			String lin = "";
			while ((lin = br.readLine()) != null)
			{
				if (lin.trim().equals("") || lin == null)
				{
					continue;
				}
				if (lin.trim().indexOf("*") == 0)
				{
					// 遇到注释文件
					continue;
				}
				if (lin.indexOf("=") == 0)
				{
					// 约定的行记录, 开始以 = 开始就认为是一个新的记录。
					// 处理表结构。
					for (String ojbkey : ht.keySet())
					{
						if (!dt.Columns.contains(ojbkey))
						{
							dt.Columns
									.Add(new DataColumn(ojbkey, String.class));
						}
					}
					
					DataRow dr = dt.NewRow();
					for (String ojbkey : ht.keySet())
					{
						dr.put(ojbkey, ht.get(ojbkey));
						/*
						 * warning dr[ojbkey] = ht.get(ojbkey);
						 */
					}
					
					if (ht.keySet().size() > 1)
					{
						dt.Rows.add(dr);
					}
					
					ht.clear(); // clear hashtable.
					continue;
				}
				
				int idx = lin.indexOf("=");
				if (idx == -1)
				{
					throw new RuntimeException(this.getFile()
							+ "@不符合规则 key =val 的规则。");
				}
				
				key = lin.substring(0, idx);
				if (key.equals(""))
				{
					continue;
				}
				
				val = lin.substring(idx + 1);
				ht.put(key, val);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				br.close();
				isr.close();
				fis.close();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		return dt;
	}
	
	public final DataTable GetTable()
	{
		Object tempVar = BP.DA.Cash.GetObj(this.getTname(),
				Depositary.Application);
		DataTable cdt = (DataTable) ((tempVar instanceof DataTable) ? tempVar
				: null);
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
			ds1.readXml(this.getFile());
			DataTable mdt = ds1.hashTables.get(this.getTableName());
			/*
			 * warning DataTable mdt = ds1.Tables[this.getTableName()];
			 */
			if (mdt == null)
			{
				mdt = new DataTable();
			}
			
			BP.DA.Cash.AddObj(this.getTname(), Depositary.Application, mdt);
			
			return ds1.hashTables.get(this.getTableName());
			/*
			 * warning return ds1.Tables[this.getTableName()];
			 */
		}
		
		// 说明这个是目录
		File di = new File(this.getFile());
		if (!di.exists())
		{
			throw new RuntimeException("文件不存在:" + this.getFile());
		}
		/*
		 * warning System.IO.DirectoryInfo di = new
		 * System.IO.DirectoryInfo(this.getFile()); if (!di.Exists ) { throw new
		 * RuntimeException("文件不存在:" + this.getFile()); }
		 */
		
		/*
		 * warning FileInfo[] fis = di.GetFiles("*.xml"); if (fis.length == 0) {
		 * fis = di.GetFiles("*.txt"); return this.GetTableTxts(fis); }
		 */
		List<File> fis = getListFiles(this.getFile(), "xml", true);
		if (null == fis || fis.size() == 0)
		{
			fis = getListFiles(this.getFile(), "txt", true);
			return this.GetTableTxts(fis);
		}
		
		DataTable dt = new DataTable(this.getTableName());
		/*
		 * warning if (fis.length == 0) { return dt; }
		 */
		if (fis.size() == 0)
		{
			return dt;
		}
		
		DataTable tempDT = new DataTable();
		/*
		 * warning for (FileInfo fi : fis) {
		 */
		for (File fi : fis)
		{
			
			DataSet ds = new DataSet("myds");
			try
			{
				ds.readXml(this.getFile() + fi.getName());
			} catch (RuntimeException ex)
			{
				throw new RuntimeException("读取文件:" + fi.getName()
						+ "错误。Exception=" + ex.getMessage());
			}
			try
			{
				// ds.
				if (dt.Columns.size() == 0)
				{
					// 如果表还是空的，没有任何结构。
					try
					{
						dt = ds.hashTables.get(this.getTableName());
						/*
						 * warning dt = ds.Tables[this.getTableName()];
						 */
					} catch (RuntimeException ex)
					{
						throw new RuntimeException("可能是没有在" + fi.getName()
								+ "文件中找到表:" + this.getTableName()
								+ " exception=" + ex.getMessage());
					}
					tempDT = dt;
					/*
					 * warning tempDT = dt.clone();
					 */
					continue;
				}
				
				DataTable mydt = ds.hashTables.get(this.getTableName());
				/*
				 * warning DataTable mydt = ds.Tables[this.getTableName()];
				 */
				if (mydt == null)
				{
					throw new RuntimeException("无此表:" + this.getTableName());
				}
				
				if (mydt.Rows.size() == 0)
				{
					continue;
				}
				
				for (DataRow mydr : mydt.Rows)
				{
					// dt.ImportRow(mydr);
					DataRow dr = dt.NewRow();
					
					for (DataColumn dc : tempDT.Columns)
					{
						// string "sd".Clone();
						if (dc.ColumnName.indexOf("_Id") != -1)
						{
							continue;
						}
						
						try
						{
							Object obj = mydr.getValue(dc.ColumnName);
							dr.put(dc.ColumnName, obj);
							/*
							 * warning Object obj = mydr[dc.ColumnName];
							 * dr.getValue(dc.ColumnName) = obj;
							 */
						} catch (RuntimeException ex)
						{
							throw new RuntimeException("xml 配置错误，多个文件中的属性不对称。"
									+ ex.getMessage());
						}
					}
					
					dt.Rows.add(dr);
				}
			} catch (RuntimeException ex)
			{
				throw new RuntimeException("获取数据出现错误:fileName=" + fi.getName()
						+ " clasName=" + this.toString() + " MoreInfo="
						+ ex.getMessage());
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
	 * 装载XML
	 */
	public int RetrieveAll()
	{
		BP.DA.Log.DebugWriteInfo("开始读取:"+this.toString());
		this.clear(); // 清所有的信息。
		Object tempVar = BP.DA.Cash.GetObj(this.toString(),
				Depositary.Application);
		XmlEns ens = (XmlEns) ((tempVar instanceof XmlEns) ? tempVar : null);
		if (ens != null)
		{
			for (XmlEn en : ens)
			{
				this.Add(en);
			}
			return ens.size();
		}
		
		// 从内存中找。
		DataTable dt = this.GetTable();
		for (DataRow dr : dt.Rows)
		{
			XmlEn en = this.getNewEntity();
			en.setRow(new Row());
			en.getRow().LoadDataTable(dt, dr);
			this.Add(en);
		}
		
		BP.DA.Cash.AddObj(this.toString(), Depositary.Application, this);
		BP.DA.Log.DebugWriteInfo("读取结束"+this.toString());
		return dt.Rows.size();
	}
	
	public final void FullEnToCash(String pk)
	{
		this.RetrieveAll();
		XmlEn myen = this.getNewEntity();
		for (XmlEn en : this)
		{
			Cash.AddObj(myen.toString() + en.GetValByKey(pk),
					Depositary.Application, en);
		}
	}
	
	public final int RetrieveByLength(String key, int len)
	{
		this.clear(); // 清所有的信息
		DataTable dt = this.GetTable();
		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue(key).toString().length() == len)
			/*
			 * warning if (dr[key].toString().length() == len)
			 */
			{
				XmlEn en = this.getNewEntity();
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
		this.clear(); // 清所有的信息
		DataTable dt = this.GetTable();
		if (dt == null)
		{
			throw new RuntimeException("@错误：类"
					+ this.getNewEntity().toString() + " File= "
					+ this.getFile() + " Table=" + this.getTableName()
					+ " ，没有取到数据。");
		}
		
		int i = 0;
		try
		{
			for (DataRow dr : dt.Rows)
			{
				if (dr.getValue(key).toString().equals(val.toString())
						&& dr.getValue(key1).toString().equals(val1))
				/*
				 * warning if (dr[key].toString().equals(val.toString()) &&
				 * dr[key1].toString().equals(val1))
				 */
				{
					XmlEn en = this.getNewEntity();
					en.setRow(new Row());
					en.getRow().LoadDataTable(dt, dr);
					this.Add(en);
					i++;
				}
			}
		} catch (RuntimeException ex)
		{
			throw new RuntimeException("@装载错误:file=" + this.getFile() + " xml:"
					+ this.toString() + "Err:" + ex.getMessage());
		}
		return i;
	}
	
	/**
	 * 按照键值查询
	 * 
	 * @param key
	 *            要查询的健
	 * @param val
	 *            值
	 * @return 返回查询的个数
	 */
	public final int RetrieveBy(String key, Object val)
	{
		if (val == null)
		{
			return 0;
		}
		
		this.clear(); // 清所有的信息
		DataTable dt = this.GetTable();
		if (dt == null)
		{
			throw new RuntimeException("@错误：类"
					+ this.getNewEntity().toString() + " File= "
					+ this.getFile() + " Table=" + this.getTableName()
					+ " ，没有取到数据。");
		}
		
		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue(key).toString().equals(val.toString()))
			/*
			 * warning if (dr[key].toString().equals(val.toString()))
			 */
			{
				XmlEn en = this.getNewEntity();
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
		/*
		 * warning DataView dv = new DataView(dt, orderByAttr, orderByAttr,
		 * DataViewRowState.Unchanged);
		 */
		this.clear(); // 清所有的信息.
		int i = 0;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue(key).toString().equals(val.toString()))
			/*
			 * warning if (dr[key].toString().equals(val.toString()))
			 */
			{
				XmlEn en = this.getNewEntity();
				en.setRow(new Row());
				en.getRow().LoadDataTable(dt, dr);
				this.Add(en);
				i++;
			}
		}
		return i;
	}
	
	
	// /#endregion
	
	
	// /#region 公共方法
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
	
	
	// /#endregion
	
	
	// /#region 增加 便利访问
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
	 * 根据位置取得数据
	 */
	public final XmlEn getItem(int index)
	{
		return (XmlEn) this.get(index);
		/*
		 * warning return (XmlEn)this.get(index);
		 */
	}
	
	/**
	 * 获取数据
	 */
	public final XmlEn getItem(String key, String val)
	{
		for (XmlEn en : this)
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				return en;
			}
		}
		throw new RuntimeException("在[" + this.getTableName() + ","
				+ this.getFile() + "," + this.toString() + "]没有找到key=" + key
				+ ", val=" + val + "的实例。");
	}
	
	/**
	 * 增加一个xml en to Ens.
	 * 
	 * @param entity
	 * @return
	 */
	/*
	 * warning public int Add(XmlEn entity) { return this.add(entity); }
	 */
	public void Add(XmlEn entity)
	{
		this.add(entity);
	}
	
	public int Add(XmlEns ens)
	{
		if (ens == null)
		{
			return 0;
		}
		
		for (XmlEn en : ens)
		{
			this.add(en);
		}
		return ens.size();
	}
	
	
	// /#endregion
	
	
	// /#region 与 entities 接口
	/**
	 * 把数据装入一个实例集合中（把xml数据装入物理表中）
	 * 
	 * @param ens
	 *            实体集合
	 * @throws Exception 
	 */
	public final void FillXmlDataIntoEntities(Entities ens) throws Exception
	{
		this.RetrieveAll(); // 查询出来全部的数据。
		Entity en1 = ens.getNewEntity();
		
		String[] pks = en1.getPKs();
		for (XmlEn xmlen : this)
		{
			
			Entity en = ens.getNewEntity();
			for (String pk : pks)
			{
				Object obj = xmlen.GetValByKey(pk);
				en.SetValByKey(pk, obj);
			}
			
			try
			{
				en.RetrieveFromDBSources();
			} catch (RuntimeException ex)
			{
				try
				{
					en.CheckPhysicsTable();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
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
				/*
				 * warning if (obj==DBNull.getValue()){ continue; }
				 */
				
				en.SetValByKey(s, obj);
			}
			en.Save();
		}
	}
	
	public final void FillXmlDataIntoEntities() throws Exception
	{
		if (this.getRefEns() == null)
		{
			return;
		}
		this.FillXmlDataIntoEntities(this.getRefEns());
	}
	
	
	// /#endregion
	
	
	// /#region 子类实现xml 信息的描述.
	public abstract XmlEn getNewEntity();
	
	/**
	 * 获取它所在的xml file 位置.
	 */
	public abstract String getFile();
	
	/**
	 * 物理表名称(可能一个xml文件中有n个Table.)
	 */
	public abstract String getTableName();
	
	public abstract Entities getRefEns();
	
	
	// /#endregion
	
	public final DataTable ToDataTable()
	{
		DataTable dt = new DataTable(this.getTableName());
		
		if (this.size() == 0)
		{
			return dt;
		}
		
		XmlEn tempVar = this.getItem(0);
		XmlEn en = (tempVar instanceof XmlEn) ? tempVar : null;
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
				dr.put(key, en1.GetValStringByKey(key));
				/*
				 * warning dr[key] = en1.GetValStringByKey(key);
				 */
			}
			dt.Rows.add(dr);
		}
		
		return dt;
	}
	
	/**
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名, 为空则表示所有文件
	 * @param isdepth
	 *            是否遍历子目录
	 * @return list
	 */
	private List<File> getListFiles(String path, String suffix, boolean isdepth)
	{
		List<File> lstFileNames = new ArrayList<File>();
		File file = new File(path);
		return listFile(lstFileNames, file, suffix, isdepth);
	}
	
	private List<File> listFile(List<File> lstFileNames, File f, String suffix,
			boolean isdepth)
	{
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory())
		{
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++)
			{
				if (isdepth || t[i].isFile())
				{
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else
		{
			String filePath = f.getAbsolutePath();
			if (!suffix.equals(""))
			{
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";
				
				if (begIndex != -1)
				{
					tempsuffix = filePath.substring(begIndex + 1,
							filePath.length());
					if (tempsuffix.equals(suffix))
					{
						lstFileNames.add(f);
					}
				}
			} else
			{
				lstFileNames.add(f);
			}
		}
		return lstFileNames;
	}
	
	@Override
	public String toString()
	{
		return this.getClass().getName();
	}
	
}
