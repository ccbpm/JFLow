package BP.En;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import BP.DA.Cash;
import BP.DA.CashEntity;
 
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;

/**
 * 数据实体集合
 */
public abstract class Entities extends ArrayList<Entity>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	// 获取配置信息.
	
	// 查询方法.
	public int RetrieveAllFromDBSource() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		return qo.DoQuery();
	}
	
	public int RetrieveAllFromDBSource(String orderByAttr) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.addOrderBy(orderByAttr);
		return qo.DoQuery();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Entity> convertEntities(Object obj)
	{
		return (ArrayList<Entity>) obj;
	}
	
	// 查询方法.
	
	// 过滤
	public final Entity Filter(String key, String val)
	{
		for (Entity en : convertEntities(this))
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				return en;
			}
		}
		return null;
	}
	
	public final Entity Filter(String key1, String val1, String key2,
			String val2)
	{
		for (Entity en : convertEntities(this))
		{
			if (en.GetValStringByKey(key1).equals(val1)
					&& en.GetValStringByKey(key2).equals(val2))
			{
				return en;
			}
		}
		return null;
	}
	
	public final Entity Filter(String key1, String val1, String key2,
			String val2, String key3, String val3)
	{
		for (Entity en : convertEntities(this))
		{
			if (en.GetValStringByKey(key1).equals(val1)
					&& en.GetValStringByKey(key2).equals(val2)
					&& en.GetValStringByKey(key3).equals(val3))
			{
				return en;
			}
		}
		return null;
	}
	
	// 虚拟方法
	/**
	 * 按照属性查询
	 * 
	 * @param attr
	 *            属性名称
	 * @param val
	 *            值
	 * @return 是否查询到
	 * @throws Exception 
	 */
	public final int RetrieveByAttr(String attr, Object val) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr, val);
		return qo.DoQuery();
	}
	
	public final int RetrieveLikeAttr(String attr, String val) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr, " like ", val);
		return qo.DoQuery();
	}
	
	// 扩展属性
	/**
	 * 是不是分级的字典。
	 */
	public final boolean getIsGradeEntities()
	{
		try
		{
			Attr attr = null;
			attr = this.getNewEntity().getEnMap().GetAttrByKey("Grade");
			attr = this.getNewEntity().getEnMap().GetAttrByKey("IsDtl");
			return true;
		} catch (java.lang.Exception e)
		{
			return false;
		}
	}
	
	// 通过datatable 转换为实体集合
	
	// 公共方法
	/**
	 * 写入到xml.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public int ExpDataToXml(String file) throws Exception
	{
		DataTable dt = this.ToDataTableField();
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		/*
		 * warning ds.Tables.add(dt);
		 */
		ds.WriteXml(file);
		return dt.Rows.size();
	}
	
	// /// <summary>
	// /// DBSimpleNoNames
	// /// </summary>
	// /// <returns></returns>
	// public DBSimpleNoNames ToEntitiesNoName(string refNo, string refName)
	// {
	// DBSimpleNoNames ens = new DBSimpleNoNames();
	// foreach (Entity en in this)
	// {
	// ens.AddByNoName(en.GetValStringByKey(refNo),
	// en.GetValStringByKey(refName));
	// }
	// return ens;
	// }
	/**
	 * 通过datatable 转换为实体集合这个Table其中一个列名称是主键
	 * 
	 * @param dt
	 *            Table
	 * @param fieldName
	 *            字段名称，这个字段时包含在table 中的主键
	 * @throws Exception 
	 */
	public final void InitCollectionByTable(DataTable dt, String fieldName) throws Exception
	{
		Entity en = this.getNewEntity();
		String pk = en.getPK();
		for (DataRow dr : dt.Rows)
		{
			Entity en1 = this.getNewEntity();
			en1.SetValByKey(pk, dr.getValue(fieldName));
			/*
			 * warning en1.SetValByKey(pk, dr[fieldName]);
			 */
			en1.Retrieve();
			this.AddEntity(en1);
		}
	}
	
	/**
	 * 通过datatable 转换为实体集合. 这个Table 的结构需要与属性结构相同。
	 * 
	 * @param dt
	 *            转换为Table
	 */
	public final void InitCollectionByTable(DataTable dt)
	{
		try
		{
			for (DataRow dr : dt.Rows)
			{
				Entity en = this.getNewEntity();
				for (Attr attr : en.getEnMap().getAttrs())
				{
					if (attr.getMyFieldType() == FieldType.RefText)
					{
						try
						{
							en.getRow().SetValByKey(attr.getKey(),
									dr.getValue(attr.getKey()));
							/*
							 * warning en.getRow().SetValByKey(attr.getKey(),
							 * dr[attr.getKey()]);
							 */
						} catch (java.lang.Exception e)
						{
						}
					} else
					{
						en.getRow().SetValByKey(attr.getKey(),
								dr.getValue(attr.getKey()));
						/*
						 * warning en.getRow().SetValByKey(attr.getKey(),
						 * dr[attr.getKey()]);
						 */
					}
				}
				this.AddEntity(en);
			}
		} catch (RuntimeException ex)
		{
			throw new RuntimeException("@此表不能向集合转换详细的错误:" + ex.getMessage());
		}
	}
	
	/**
	 * 判断两个实体集合是不是相同.
	 * 
	 * @param ens
	 * @return
	 */
	public final boolean equals(Entities ens)
	{
		if (ens.size() != this.size())
		{
			return false;
		}
		
		for (Entity en : convertEntities(this))
		{
			boolean isExits = false;
			for (Entity en1 : convertEntities(ens))
			{
				if (en.getPKVal().equals(en1.getPKVal()))
				{
					isExits = true;
					break;
				}
			}
			if (!isExits)
			{
				return false;
			}
		}
		return true;
	}
	
	// 扩展属性
	// /// <summary>
	// /// 他的相关功能。
	// /// </summary>
	// public SysUIEnsRefFuncs HisSysUIEnsRefFuncs
	// {
	// get
	// {
	// return new SysUIEnsRefFuncs(this.ToString()) ;
	// }
	//
	// }
	
	// 构造函数
	/**
	 * 构造函数
	 */
	public Entities()
	{
	}
	
	// 公共方法
	/**
	 * 是否存在key= val 的实体。
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public final boolean IsExits(String key, Object val)
	{
		for (Entity en : convertEntities(this))
		{
			if (val.toString().equals(en.GetValStringByKey(key)))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 创建一个该集合的元素的类型的新实例
	 * 
	 * @return
	 */
	public abstract Entity getNewEntity();
	
	/**
	 * 根据位置取得数据
	 */
	// 子类有重写，原来是final
	public Entity getItem(int index)
	{
		return (Entity) ((this.get(index) instanceof Entity) ? this.get(index)
				: null);
	}
	
	/**
	 * 将对象添加到集合尾处，如果对象已经存在，则不添加
	 * 
	 * @param entity
	 *            要添加的对象
	 * @return 返回添加到的地方
	 */
	public void AddEntity(Entity entity)
	{
		this.add(entity);
	}
	
	/*
	 * warning public int AddEntity(Entity entity) { return
	 * this.add(entity); }
	 */
	public int AddEntity(Entity entity, int idx)
	{
		this.add(idx, entity);
		return idx;
	}
	
	public void AddEntities(Entities ens)
	{
		for (Entity en : convertEntities(ens))
		{
			this.AddEntity(en);
		}
		// this.InnerList.AddRange(ens);
	}
	
	/**
	 * 增加entities
	 * 
	 * @param pks
	 *            主键的值，中间用@符号隔开
	 * @throws Exception 
	 */
	public void AddEntities(String pks) throws Exception
	{
		this.clear();
		if (pks == null || pks.equals(""))
		{
			return;
		}
		
		String[] strs = pks.split("[@]", -1);
		for (String str : strs)
		{
			if (str == null || str.equals(""))
			{
				continue;
			}
			
			Entity en = this.getNewEntity();
			en.setPKVal(str);
			if (en.RetrieveFromDBSources() == 0)
			{
				continue;
			}
			this.AddEntity(en);
		}
		
		 
	}
	
	/*
	 * warning public void Insert(int index, Entity entity) {
	 * this.InnerList.Insert(index, entity); }
	 */
	/**
	 * 判断是不是包含指定的Entity .
	 * 
	 * @param en
	 * @return
	 */
	public final boolean Contains(Entity en)
	{
		return this.Contains(en.getPKVal());
	}
	
	/**
	 * 是否包含这个集合
	 * 
	 * @param ens
	 * @return true / false
	 */
	public final boolean Contains(Entities ens)
	{
		return this.Contains(ens, ens.getNewEntity().getPK());
	}
	
	public final boolean Contains(Entities ens, String key)
	{
		if (ens.size() == 0)
		{
			return false;
		}
		for (Entity en : convertEntities(ens))
		{
			if (!this.Contains(key, en.GetValByKey(key)))
			{
				return false;
			}
		}
		return true;
	}
	
	public final boolean Contains(Entities ens, String key1, String key2)
	{
		if (ens.size() == 0)
		{
			return false;
		}
		for (Entity en : convertEntities(ens))
		{
			if (!this.Contains(key1, en.GetValByKey(key1), key2,
					en.GetValByKey(key2)))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是不是包含指定的PK
	 * 
	 * @param pkVal
	 * @return
	 */
	public final boolean Contains(Object pkVal)
	{
		String pk = this.getNewEntity().getPK();
		return this.Contains(pk, pkVal);
	}
	
	/**
	 * 指定的属性里面是否包含指定的值.
	 * 
	 * @param attr
	 *            指定的属性
	 * @param pkVal
	 *            指定的值
	 * @return 返回是否等于
	 */
	public final boolean Contains(String attr, Object pkVal)
	{
		for (Entity myen : convertEntities(this))
		{
			if (myen.GetValByKey(attr).toString().equals(pkVal.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	public final boolean Contains(String attr1, Object pkVal1, String attr2,
			Object pkVal2)
	{
		for (Entity myen : convertEntities(this))
		{
			if (myen.GetValByKey(attr1).toString().equals(pkVal1.toString())
					&& myen.GetValByKey(attr2).toString()
							.equals(pkVal2.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	public final boolean Contains(String attr1, Object pkVal1, String attr2,
			Object pkVal2, String attr3, Object pkVal3)
	{
		for (Entity myen : convertEntities(this))
		{
			if (myen.GetValByKey(attr1).toString().equals(pkVal1.toString())
					&& myen.GetValByKey(attr2).toString()
							.equals(pkVal2.toString())
					&& myen.GetValByKey(attr3).toString()
							.equals(pkVal3.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 取得当前集合于传过来的集合交集.
	 * 
	 * @param ens
	 *            一个实体集合
	 * @return 比较后的集合
	 */
	public final Entities GainIntersection(Entities ens)
	{
		Entities myens = this.CreateInstance();
		String pk = this.getNewEntity().getPK();
		for (Entity en : convertEntities(this))
		{
			for (Entity hisen : convertEntities(ens))
			{
				if (en.GetValByKey(pk).equals(hisen.GetValByKey(pk)))
				{
					myens.AddEntity(en);
				}
			}
		}
		return myens;
	}
	
	/**
	 * 创建立本身的一个实例.
	 * 
	 * @return Entities
	 */
	public final Entities CreateInstance()
	{
		return ClassFactory.GetEns(this.toString());
	}
	
	// 获取一个实体
	/**
	 * 获取一个实体
	 * 
	 * @param val
	 *            值
	 * @return
	 */
	public final Entity GetEntityByKey(Object val)
	{
		String pk = this.getNewEntity().getPK();
		for (Entity en : convertEntities(this))
		{
			if (val.toString().equals(en.GetValStrByKey(pk)))
			{
				return en;
			}
		}
		return null;
	}
	
	/**
	 * 获取一个实体
	 * 
	 * @param attr
	 *            属性
	 * @param val
	 *            值
	 * @return
	 */
	public final Entity GetEntityByKey(String attr, Object val)
	{
		for (Entity en : convertEntities(this))
		{
			Object v = en.GetValByKey(attr);
			if (v != null && v.equals(val))
			{
				return en;
			}
		}
		return null;
	}
	
	public final Entity GetEntityByKey(String attr, int val)
	{
		for (Entity en : convertEntities(this))
		{
			if (en.GetValIntByKey(attr) == val)
			{
				return en;
			}
		}
		return null;
	}
	
	public final Entity GetEntityByKey(String attr1, Object val1, String attr2,
			Object val2)
	{
		for (Entity en : convertEntities(this))
		{
			if (val1.toString().equalsIgnoreCase(en.GetValStrByKey(attr1))
					&& val2.toString().equalsIgnoreCase(
							en.GetValStrByKey(attr2)))
			{
				return en;
			}
		}
		return null;
	}
	
	public final Entity GetEntityByKey(String attr1, Object val1, String attr2,
			Object val2, String attr3, Object val3)
	{
		for (Entity en : convertEntities(this))
		{
			if (en.GetValByKey(attr1).equals(val1)
					&& en.GetValByKey(attr2).equals(val2)
					&& en.GetValByKey(attr3).equals(val3))
			{
				return en;
			}
		}
		return null;
	}
	
	// 对一个属性操作
	/**
	 * 求和
	 * 
	 * @param key
	 * @return
	 */
	public final java.math.BigDecimal GetSumDecimalByKey(String key)
	{
		java.math.BigDecimal sum = new java.math.BigDecimal(0);
		for (Entity en : convertEntities(this))
		{
			sum = sum.add(en.GetValDecimalByKey(key));
			/*
			 * warning sum += en.GetValDecimalByKey(key);
			 */
		}
		return sum;
	}
	
	public final java.math.BigDecimal GetSumDecimalByKey(String key,
			String attrOfGroup, Object valOfGroup)
	{
		java.math.BigDecimal sum = new java.math.BigDecimal(0);
		for (Entity en : convertEntities(this))
		{
			if (valOfGroup.toString().equals(en.GetValStrByKey(attrOfGroup)))
			{
				sum = sum.add(en.GetValDecimalByKey(key));
				/*
				 * warning sum += en.GetValDecimalByKey(key);
				 */
			}
		}
		return sum;
	}
	
	public final java.math.BigDecimal GetAvgDecimalByKey(String key)
	{
		if (this.size() == 0)
		{
			return BigDecimal.ZERO;
		}
		java.math.BigDecimal sum = new java.math.BigDecimal(0);
		for (Entity en : convertEntities(this))
		{
			sum = sum.add(en.GetValDecimalByKey(key));
		}
		return sum.divide(new BigDecimal(this.size()));
	}
	
	public final java.math.BigDecimal GetAvgIntByKey(String key)
	{
		if (this.size() == 0)
		{
			return BigDecimal.ZERO;
		}
		java.math.BigDecimal sum = new java.math.BigDecimal(0);
		for (Entity en : convertEntities(this))
		{
			sum = sum.add(en.GetValDecimalByKey(key));
		}
		return sum.divide(new BigDecimal(this.size()));
	}
	
	/**
	 * 求和
	 * 
	 * @param key
	 * @return
	 */
	public final int GetSumIntByKey(String key)
	{
		int sum = 0;
		for (Entity en : convertEntities(this))
		{
			sum += en.GetValIntByKey(key);
		}
		return sum;
	}
	
	/**
	 * 求和
	 * 
	 * @param key
	 * @return
	 */
	public final float GetSumFloatByKey(String key)
	{
		float sum = 0;
		for (Entity en : convertEntities(this))
		{
			sum += en.GetValFloatByKey(key);
		}
		return sum;
	}
	
	public final double GetSumDoubleByKey(String key)
	{
		double sum = 0d;
		for (Entity en : convertEntities(this))
		{
			sum += en.GetValDoubleByKey(key);
		}
		return sum;
		
	}
	
	/**
	 * 求个数
	 * 
	 * @param key
	 * @return
	 */
	public final int GetCountByKey(String key, String val)
	{
		int sum = 0;
		for (Entity en : convertEntities(this))
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				sum++;
			}
		}
		return sum;
	}
	
	public final int GetCountByKey(String key, int val)
	{
		int sum = 0;
		for (Entity en : convertEntities(this))
		{
			if (en.GetValIntByKey(key) == val)
			{
				sum++;
			}
		}
		return sum;
	}
	
	// 对集合的操作
	/**
	 * 装载到内存
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final int FlodInCash() throws Exception
	{
		// this.Clear();
		QueryObject qo = new QueryObject(this);
		
		// qo.Top = 2000;
		int num = qo.DoQuery();
		// 把查询个数加入内存
		Entity en = this.getNewEntity();
		CashEntity.PubEns(en.toString(), this, en.getPK());
		BP.DA.Log.DefaultLogWriteLineInfo("成功[" + en.toString() + "-" + num
				+ "]放入缓存。");
		return num;
	}
	
 
	
	/**
	 * 从集合中删除该对象
	 * 
	 * @param entity
	 */
	public void RemoveEn(Entity entity)
	{
		this.remove(entity);
	}
	
	/**
	 * 移除
	 * 
	 * @param pk
	 */
	public void RemoveEn(String pk)
	{
		String key = this.getNewEntity().getPK();
		RemoveEn(key, pk);
	}
	
	public void RemoveEn(String key, String val)
	{
		for (Entity en : convertEntities(this))
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				this.RemoveEn(en);
				return;
			}
		}
	}
	
	public void remove(String pks)
	{
		String[] mypks = pks.split("[@]", -1);
		String pkAttr = this.getNewEntity().getPK();
		
		for (String pk : mypks)
		{
			if (pk == null || pk.length() == 0)
			{
				continue;
			}
			
			this.RemoveEn(pkAttr, pk);
		}
	}
	
	/**
	 * 删除table.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final int ClearTable() throws Exception
	{
		Entity en = this.getNewEntity();
		return en.RunSQL("DELETE FROM " + en.getEnMap().getPhysicsTable());
	}
	
	/**
	 * 删除集合内的对象
	 * 
	 * @throws Exception
	 */
	public final void Delete() throws Exception
	{
		for (Object en : convertEntities(this))
		{
			if (((Entity) en).getIsExits())
			{
				((Entity) en).Delete();
			}
		}
		this.clear();
	}
	
	public final int RunSQL(String sql) throws Exception
	{
		return this.getNewEntity().RunSQL(sql);
	}
	
	public int Delete(String key, Object val)
	{
		Entity en = this.getNewEntity();
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE "
				+ key + "=" + en.getHisDBVarStr() + "p";
		
		if (val.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p", val.toString());
			} else
			{
				ps.Add("p", val);
			}
		} else
		{
			ps.Add("p", val);
		}
		ps.Add("p", val);
		return en.RunSQL(ps);
	}
	
	public final int Delete(String key1, Object val1, String key2, Object val2)
	{
		Entity en = this.getNewEntity();
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE "
				+ key1 + "=" + en.getHisDBVarStr() + "p1 AND " + key2 + "="
				+ en.getHisDBVarStr() + "p2";
		if (val1.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key1);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p1", val1.toString());
			} else
			{
				ps.Add("p1", val1);
			}
		} else
		{
			ps.Add("p1", val1);
		}
		
		if (val2.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key2);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p2", val2.toString());
			} else
			{
				ps.Add("p2", val2);
			}
		} else
		{
			ps.Add("p2", val2);
		}
		
		return en.RunSQL(ps);
	}
	
	public final int Delete(String key1, Object val1, String key2, Object val2,
			String key3, Object val3) throws Exception
	{
		Entity en = this.getNewEntity();
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE "
				+ key1 + "=" + en.getHisDBVarStr() + "p1 AND " + key2 + "="
				+ en.getHisDBVarStr() + "p2 AND " + key3 + "="
				+ en.getHisDBVarStr() + "p3";
		if (val1.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key1);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p1", val1.toString());
			} else
			{
				ps.Add("p1", val1);
			}
		} else
		{
			ps.Add("p1", val1);
		}
		
		if (val2.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key2);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p2", val2.toString());
			} else
			{
				ps.Add("p2", val2);
			}
		} else
		{
			ps.Add("p2", val2);
		}
		
		if (val3.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key3);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p3", val3.toString());
			} else
			{
				ps.Add("p3", val3);
			}
		} else
		{
			ps.Add("p3", val3);
		}
		
		return en.RunSQL(ps);
	}
	
	public final int Delete(String key1, Object val1, String key2, Object val2,
			String key3, Object val3, String key4, Object val4)
			throws Exception
	{
		Entity en = this.getNewEntity();
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE "
				+ key1 + "=" + en.getHisDBVarStr() + "p1 AND " + key2 + "="
				+ en.getHisDBVarStr() + "p2 AND " + key3 + "="
				+ en.getHisDBVarStr() + "p3 AND " + key4 + "="
				+ en.getHisDBVarStr() + "p4";
		if (val1.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key1);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p1", val1.toString());
			} else
			{
				ps.Add("p1", val1);
			}
		} else
		{
			ps.Add("p1", val1);
		}
		
		if (val2.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key2);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p2", val2.toString());
			} else
			{
				ps.Add("p2", val2);
			}
		} else
		{
			ps.Add("p2", val2);
		}
		
		if (val3.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key3);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p3", val3.toString());
			} else
			{
				ps.Add("p3", val3);
			}
		} else
		{
			ps.Add("p3", val3);
		}
		
		if (val4.getClass() != String.class)
		{
			Attr attr = en.getEnMap().GetAttrByKey(key4);
			if (attr.getMyDataType() == DataType.AppString)
			{
				ps.Add("p4", val4.toString());
			} else
			{
				ps.Add("p4", val4);
			}
		} else
		{
			ps.Add("p4", val4);
		}
		return en.RunSQL(ps);
	}
	
	/**
	 * 更新集合内的对象
	 * @throws Exception 
	 */
	public final void Update() throws Exception
	{
		// string msg="";
		for (Entity en : convertEntities(this))
		{
			en.Update();
		}
		
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	public final void Save() throws Exception
	{
		for (Entity en : convertEntities(this))
		{
			en.Save();
		}
	}
	
	/*
	 * warning public final void SaveToXml(String file) { String dir = "";
	 * 
	 * if (file.contains("\\")) { dir = file.substring(0,
	 * file.lastIndexOf('\\')); } else if (file.contains("/")) { dir =
	 * file.substring(0, file.lastIndexOf("/")); }
	 * 
	 * if (!dir.equals("")) { if (!System.IO.Directory.Exists(dir)) {
	 * System.IO.Directory.CreateDirectory(dir); } }
	 * 
	 * DataSet ds = this.ToDataSet(); ds.WriteXml(file); }
	 */
	
	// 查询方法
	/*
	 * warning public final int RetrieveByKeyNoConnection(String attr, Object
	 * val) { Entity en = this.getNewEntity(); String pk = en.getPK();
	 * 
	 * DataTable dt = DBAccess.RunSQLReturnTable("SELECT " + pk + " FROM " +
	 * this.getNewEntity().getEnMap().getPhysicsTable() + " WHERE " + attr +
	 * "=" + en.getHisDBVarStr() + "v", "v", val); for (DataRow dr : dt.Rows) {
	 * Entity en1 = this.getNewEntity(); en1.SetValByKey(pk, dr.getValue(0));
	 * en1.Retrieve(); this.AddEntity(en1); } return dt.Rows.size(); }
	 */
	/**
	 * 按照关键字查询。 说明这里是用Attrs接受
	 * 
	 * @param key
	 *            关键字
	 * @param al
	 *            实体
	 * @return 返回Table
	 */
	/*
	 * warning public final DataTable RetrieveByKeyReturnTable(String key, Attrs
	 * attrs) { QueryObject qo = new QueryObject(this);
	 * 
	 * // 在 Normal 属性里面增加，查询条件。 Map map = this.getNewEntity().getEnMap();
	 * qo.addLeftBracket(); for (Attr en : map.getAttrs()) { if
	 * (en.getUIContralType() == UIContralType.DDL || en.getUIContralType() ==
	 * UIContralType.CheckBok) { continue; } qo.addOr();
	 * qo.AddWhere(en.getKey(), " LIKE ", key); } qo.addRightBracket();
	 * 
	 * // // // Attrs searchAttrs = map.SearchAttrs; // foreach(Attr attr in
	 * attrs) // { // qo.addAnd(); // qo.addLeftBracket(); //
	 * qo.AddWhere(attr.Key, attr.DefaultVal.ToString() ) ; //
	 * qo.addRightBracket(); // } return qo.DoQueryToTable(); }
	 */
	/**
	 * 按照KEY 查找。
	 * 
	 * @param keyVal
	 *            KEY
	 * @return 返回朝找出来的个数。
	 */
	/*
	 * warning public final int RetrieveByKey(String keyVal) { keyVal = "%" +
	 * keyVal.trim() + "%"; QueryObject qo = new QueryObject(this); Attrs attrs
	 * = this.getNewEntity().getEnMap().getAttrs(); //qo.addLeftBracket();
	 * String pk = this.getNewEntity().getPK(); if (!pk.equals("OID")) {
	 * qo.AddWhere(this.getNewEntity().getPK(), " LIKE ", keyVal); } for
	 * (Attr en : attrs) {
	 * 
	 * if (en.getUIContralType() == UIContralType.DDL || en.getUIContralType()
	 * == UIContralType.CheckBok) { continue; }
	 * 
	 * if (en.getKey().equals(pk)) { continue; }
	 * 
	 * if (en.getMyDataType() != DataType.AppString) { continue; }
	 * 
	 * if (en.getMyFieldType() == FieldType.FK) { continue; }
	 * 
	 * if (en.getMyFieldType() == FieldType.RefText) { continue; }
	 * 
	 * qo.addOr(); qo.AddWhere(en.getKey(), " LIKE ", keyVal); }
	 * //qo.addRightBracket(); return qo.DoQuery(); }
	 */
	/**
	 * 按LIKE 去查.
	 * 
	 * @param key
	 * @param vals
	 * @return
	 * @throws Exception 
	 */
	public final int RetrieveByLike(String key, String vals) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, " LIKE ", vals);
		return qo.DoQuery();
	}
	
	/**
	 * 查询出来，包涵pks 的字串。 比例："001,002,003"
	 * 
	 * @return
	 * @throws Exception 
	 */
	public int Retrieve(String pks) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(this.getNewEntity().getPK(), " in ", pks);
		return qo.DoQuery();
	}
	
	/**
	 * 按照IDs查询并且排序
	 * @param key
	 * @param vals
	 * @param orderByKey
	 * @return
	 * @throws Exception
	 */
	public int RetrieveInOrderBy(String key, String vals,String orderByKey) throws Exception{
		QueryObject qo = new QueryObject(this);

        if (vals.contains("(") == false)
            qo.AddWhere(key, " IN ", "(" + vals + ")");
        else
            qo.AddWhere(key, " IN ", vals);
        
        if(DataType.IsNullOrEmpty(orderByKey) == false)
        	qo.addOrderBy(orderByKey);

        return qo.DoQuery();
	}
	 /// <summary>
    /// 按照IDs查询
    /// 比如: FrmID  IN  '001','002' 
    /// </summary>
    /// <param name="key"></param>
    /// <param name="vals"></param>
    /// <returns></returns>
    public int RetrieveIn(String key, String vals) throws Exception
    {
        QueryObject qo = new QueryObject(this);

        if (vals.contains("(") == false)
            qo.AddWhere(key, " IN ", "(" + vals + ")");
        else
            qo.AddWhere(key, " IN ", vals);

        return qo.DoQuery();
    }
	
	public final int RetrieveInSQL(String attr, String sql) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(attr, sql);
		return qo.DoQuery();
	}
	
	public final int RetrieveInSQL(String attr, String sql, String orderBy) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(attr, sql);
		qo.addOrderBy(orderBy);
		return qo.DoQuery();
	}
	
	public final int RetrieveInSQL(String sql) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(this.getNewEntity().getPK(), sql);
		return qo.DoQuery();
	}
	
	public final int RetrieveExistsSQL(String sql) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereExistsSQL(this.getNewEntity().getPK(), sql);
		return qo.DoQuery();
	}
	
	public final int RetrieveInSQL_Order(String sql, String orderby) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(this.getNewEntity().getPK(), sql);
		qo.addOrderBy(orderby);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, boolean val) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		if (val)
		{
			qo.AddWhere(key, 1);
		} else
		{
			qo.AddWhere(key, 0);
		}
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val, String orderby) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		// qo.addOrderByDesc(orderby);
		qo.addOrderBy(orderby); // 这个排序方式不要变化，否则会影响其他的地方。
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val, String key2, Object val2) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val, String key2, Object val2,
			String ordery) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addOrderBy(ordery);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val, String key2, Object val2,
			String key3, Object val3) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		return qo.DoQuery();
	}

	public final int Retrieve(String key, Object val, String key2, Object val2,
							  String key3, Object val3,String ordery) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		qo.addOrderBy(ordery);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val, String key2, Object val2,
			String key3, Object val3, String key4, Object val4) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		qo.addAnd();
		qo.AddWhere(key4, val4);
		return qo.DoQuery();
	}
	
	public final int Retrieve(String key, Object val, String key2, Object val2,
			String key3, Object val3, String key4, Object val4, String orderBy) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		qo.addAnd();
		qo.AddWhere(key4, val4);
		qo.addOrderBy(orderBy);
		return qo.DoQuery();
	}
	
	/**
	 * 查询全部
	 * 
	 * @return
	 * @throws Exception 
	 */
	public int RetrieveAll() throws Exception
	{
		return this.RetrieveAll(null);
	}
	
	public int RetrieveAllOrderByRandom() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.addOrderByRandom();
		return qo.DoQuery();
	}
	
	public int RetrieveAllOrderByRandom(int topNum) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.setTop(topNum);
		qo.addOrderByRandom();
		return qo.DoQuery();
	}
	
	public int RetrieveAll(int topNum, String orderby) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.setTop(topNum);
		qo.addOrderBy(orderby);
		return qo.DoQuery();
	}
	
	public int RetrieveAll(int topNum, String orderby, boolean isDesc) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.setTop(topNum);
		if (isDesc)
		{
			qo.addOrderByDesc(orderby);
		} else
		{
			qo.addOrderBy(orderby);
		}
		return qo.DoQuery();
	}
	
	/**
	 * 查询全部
	 * 
	 * @return
	 * @throws Exception 
	 */
	public int RetrieveAll(String orderBy) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		if (orderBy != null)
		{
			qo.addOrderBy(orderBy);
		}
		return qo.DoQuery();
	}
	
	/**
	 * 查询全部。
	 * 
	 * @return
	 * @throws Exception 
	 */
	public int RetrieveAll(String orderBy1, String orderBy2) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		if (orderBy1 != null)
		{
			qo.addOrderBy(orderBy1, orderBy2);
		}
		return qo.DoQuery();
	}
	
	/**
	 * 按照最大个数查询。
	 * 
	 * @param MaxNum
	 *            最大NUM
	 * @return
	 * @throws Exception 
	 */
	public final int RetrieveAll(int MaxNum) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.setTop(MaxNum);
		return qo.DoQuery();
	}
	
	/**
	 * 查询全部的结果放到DataTable。
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final DataTable RetrieveAllToTable() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		return qo.DoQueryToTable();
	}
	
	private DataTable DealBoolTypeInDataTable(Entity en, DataTable dt)
	{
		throw new RuntimeException("编译不通过");
		/*
		 * warning for (Attr attr : en.getEnMap().getAttrs()) { if
		 * (attr.getMyDataType() == DataType.AppBoolean) { DataColumn col = new
		 * DataColumn(); col.ColumnName = "tmp" + attr.getKey(); col.DataType =
		 * Boolean.class; dt.Columns.Add(col); for (DataRow dr : dt.Rows) { if
		 * (dr[attr.getKey()].toString().equals("1")) { dr["tmp" +
		 * attr.getKey()] = true; } else { dr["tmp" + attr.getKey()] = false; }
		 * } dt.Columns.remove(attr.getKey()); dt.Columns.get("tmp" +
		 * attr.getKey()].ColumnName = attr.getKey(); continue; } if
		 * (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType()
		 * == DataType.AppDate) { DataColumn col = new DataColumn();
		 * col.ColumnName = "tmp" + attr.getKey(); col.DataType =
		 * java.util.Date.class; dt.Columns.Add(col); for (DataRow dr : dt.Rows)
		 * { try { dr["tmp" + attr.getKey()] = new
		 * java.util.Date(java.util.Date.parse(dr[attr.getKey()].toString())); }
		 * catch (java.lang.Exception e) { if
		 * (attr.getDefaultVal().toString().equals("")) { dr["tmp" +
		 * attr.getKey()] = new java.util.Date(); } else { dr["tmp" +
		 * attr.getKey()] = new
		 * java.util.Date(java.util.Date.parse(attr.getDefaultVal
		 * ().toString())); }
		 * 
		 * }
		 * 
		 * } dt.Columns.remove(attr.getKey()); dt.Columns.get("tmp" +
		 * attr.getKey()].ColumnName = attr.getKey(); continue; } } return dt;
		 */
	}
	 
	 
	
	public final DataTable ToDataTableField()
	{
		return ToDataTableField("dt");
	}
	
	public final DataTable ToDataTableStringField()
	{
		return ToDataTableStringField("dt");
	}
	
	public final DataTable ToDataTableStringField(String tableName)
	{
		DataTable dt = this.ToEmptyTableStringField();
		Entity en = this.getNewEntity();
		
		dt.TableName = tableName;
		for (Entity myen : convertEntities(this))
		{
			DataRow dr = dt.NewRow();
			for (Attr attr : en.getEnMap().getAttrs())
			{
				if (attr.getMyDataType() == DataType.AppBoolean)
				{
					if (myen.GetValIntByKey(attr.getKey()) == 1)
					{
						dr.setValue(attr.getKey(), "1");
						/*
						 * warning dr[attr.getKey()] = "1";
						 */
					} else
					{
						dr.setValue(attr.getKey(), "0");
						/*
						 * warning dr[attr.getKey()] = "0";
						 */
					}
					continue;
				}
				
				// 如果是外键 就要去掉左右空格。
				if (attr.getMyFieldType() == FieldType.FK
						|| attr.getMyFieldType() == FieldType.PKFK)
				{
					dr.setValue(attr.getKey(), myen.GetValByKey(attr.getKey())
							.toString().trim());
					/*
					 * warning dr[attr.getKey()] =
					 * myen.GetValByKey(attr.getKey()).toString().trim();
					 */
				} else
				{
					dr.setValue(attr.getKey(), myen.GetValByKey(attr.getKey()));
					/*
					 * warning dr[attr.getKey()] =
					 * myen.GetValByKey(attr.getKey());
					 */
				}
			}
			dt.Rows.add(dr);
			/*
			 * warning dt.Rows.add(dr);
			 */
		}
		return dt;
	}
	
	 
	/**
	 * 把当前实体集合的数据库转换成Table。 区分大小写
	 * 
	 * @return DataTable
	 */
	public final DataTable ToDataTableField(String tableName)
	{
		DataTable dt = this.ToEmptyTableField();
		
		Entity en = this.getNewEntity();
		Attrs attrs = en.getEnMap().getAttrs();
		
		dt.TableName = tableName;
		for (Entity myen : convertEntities(this))
		{
			DataRow dr = dt.NewRow();
			for (Attr attr : attrs)
			{
				if (attr.getMyDataType() == DataType.AppBoolean)
				{
					if (myen.GetValIntByKey(attr.getKey()) == 1)
					{
						dr.setValue(attr.getKey(), "1");
						/*
						 * warning dr[attr.getKey()] = "1";
						 */
					} else
					{
						dr.setValue(attr.getKey(), "0");
						/*
						 * warning dr[attr.getKey()] = "0";
						 */
					}
					continue;
				}
				
				// 如果是外键 就要去掉左右空格。
				// *
				if (attr.getMyFieldType().equals(FieldType.FK)
						|| attr.getMyFieldType().equals(FieldType.PKFK))
				{
					dr.setValue(attr.getKey(), myen.GetValByKey(attr.getKey())
							.toString().trim());
					/*
					 * warning dr[attr.getKey()] =
					 * myen.GetValByKey(attr.getKey()).toString().trim();
					 */
				} else
				{
					dr.setValue(attr.getKey(), myen.GetValByKey(attr.getKey()));
					/*
					 * warning dr[attr.getKey()] =
					 * myen.GetValByKey(attr.getKey());
					 */
				}
			}
			dt.Rows.add(dr);
			/*
			 * warning dt.Rows.add(dr);
			 */
		}
		return dt;
	}
	
	public final DataTable ToDataTableDesc()
	{
		DataTable dt = this.ToEmptyTableDesc();
		Entity en = this.getNewEntity();
		
		dt.TableName = en.getEnMap().getPhysicsTable();
		for (Entity myen : convertEntities(this))
		{
			DataRow dr = dt.NewRow();
			for (Attr attr : en.getEnMap().getAttrs())
			{
				
				if (attr.getMyDataType() == DataType.AppBoolean)
				{
					if (myen.GetValBooleanByKey(attr.getKey()))
					{
						dr.setValue(attr.getDesc(), "是");
						/*
						 * warning dr[attr.getDesc()] = "是";
						 */
					} else
					{
						dr.setValue(attr.getDesc(), "否");
						/*
						 * warning dr[attr.getDesc()] = "否";
						 */
					}
					continue;
				}
				dr.setValue(attr.getDesc(), myen.GetValByKey(attr.getKey()));
				/*
				 * warning dr[attr.getDesc()] = myen.GetValByKey(attr.getKey());
				 */
			}
			dt.Rows.add(dr);
			/*
			 * warning dt.Rows.add(dr);
			 */
		}
		return dt;
	}
	
	public final DataTable ToEmptyTableDescField()
	{
		DataTable dt = new DataTable();
		Entity en = this.getNewEntity();
		try
		{
			
			for (Attr attr : en.getEnMap().getAttrs())
			{
				// if (!attr.UIVisible)
				// continue;
				
				// if (attr.MyFieldType == FieldType.Enum && attr.MyDataType ==
				// DataType.AppInt )
				// continue;
				
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), String.class));
						break;
					case DataType.AppInt:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), Integer.class));
						break;
					case DataType.AppFloat:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), Float.class));
						break;
					case DataType.AppBoolean:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), String.class));
						break;
					case DataType.AppDouble:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), Double.class));
						break;
					case DataType.AppMoney:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), Double.class));
						break;
					case DataType.AppDate:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), String.class));
						break;
					case DataType.AppDateTime:
						dt.Columns.Add(new DataColumn(attr.getDesc().trim()
								+ attr.getKey(), String.class));
						break;
					default:
						throw new RuntimeException(
								"@bulider insert sql error: 没有这个数据类型");
				}
			}
		} catch (RuntimeException ex)
		{
			throw new RuntimeException(en.getEnDesc() + " error "
					+ ex.getMessage());
			
		}
		return dt;
	}
	
	public final DataTable ToDataTableDescField()
	{
		DataTable dt = this.ToEmptyTableDescField();
		Entity en = this.getNewEntity();
		
		dt.TableName = en.getEnMap().getPhysicsTable();
		for (Entity myen : convertEntities(this))
		{
			DataRow dr = dt.NewRow();
			for (Attr attr : en.getEnMap().getAttrs())
			{
				// if (!attr.UIVisible)
				// continue;
				
				// if (attr.MyFieldType == FieldType.Enum && attr.MyDataType ==
				// DataType.AppInt)
				// continue;
				
				if (attr.getMyDataType() == DataType.AppBoolean)
				{
					if (myen.GetValBooleanByKey(attr.getKey()))
					{
						dr.setValue(attr.getDesc().trim() + attr.getKey(), "是");
						/*
						 * warning dr[attr.getDesc().trim() + attr.getKey()] =
						 * "是";
						 */
					} else
					{
						dr.setValue(attr.getDesc().trim() + attr.getKey(), "否");
						/*
						 * warning dr[attr.getDesc().trim() + attr.getKey()] =
						 * "否";
						 */
					}
					continue;
				}
				dr.setValue(attr.getDesc().trim() + attr.getKey(),
						myen.GetValByKey(attr.getKey()));
				/*
				 * warning dr[attr.getDesc().trim() + attr.getKey()] =
				 * myen.GetValByKey(attr.getKey());
				 */
			}
			dt.Rows.add(dr);
			/*
			 * warning dt.Rows.add(dr);
			 */
		}
		return dt;
	}
	
	/**
	 * 把系统的实体的PK转换为string 比如: "001,002,003,"。
	 * 
	 * @param flag
	 *            分割符号, 一般来说用 ' ; '
	 * @return 转化后的string / 实体集合为空就 return null
	 */
	public final String ToStringOfPK(String flag, boolean isCutEndFlag)
	{
		String pk = null;
		for (Entity en : convertEntities(this))
		{
			pk += en.getPKVal() + flag;
		}
		if (isCutEndFlag)
		{
			pk = pk.substring(0, pk.length() - 1);
		}
		
		return pk;
	}
	
	/**
	 * 把系统的实体的PK转换为 string 比如: "001,002,003,"。
	 * 
	 * @return 转化后的string / 实体集合为空就 return null
	 */
	public final String ToStringOfSQLModelByPK()
	{
		if (this.size() == 0)
		{
			return "''";
		}
		return ToStringOfSQLModelByKey(this.getItem(0).getPK());
	}
	
	/**
	 * 把系统的实体的PK转换为 string 比如: "001,002,003,"。
	 * 
	 * @return 转化后的string / 实体集合为空就 return "''"
	 */
	public final String ToStringOfSQLModelByKey(String key)
	{
		if (this.size() == 0)
		{
			return "''";
		}
		
		String pk = null;
		for (Entity en : convertEntities(this))
		{
			pk += "'" + en.GetValStringByKey(key) + "',";
		}
		
		pk = pk.substring(0, pk.length() - 1);
		
		return pk;
	}
	
	/**
	 * 空的Table 取到一个空表结构。
	 * 
	 * @return
	 */
	public final DataTable ToEmptyTableField_old()
	{
		DataTable dt = new DataTable();
		Entity en = this.getNewEntity();
		dt.TableName = en.getEnMap().getPhysicsTable();
		
		for (Attr attr : en.getEnMap().getAttrs())
		{
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class));
					break;
				case DataType.AppInt:
					dt.Columns
							.Add(new DataColumn(attr.getKey(), Integer.class));
					break;
				case DataType.AppFloat:
					dt.Columns.Add(new DataColumn(attr.getKey(), Float.class));
					break;
				case DataType.AppBoolean:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class));
					break;
				case DataType.AppDouble:
					dt.Columns.Add(new DataColumn(attr.getKey(), Double.class));
					break;
				case DataType.AppMoney:
					dt.Columns.Add(new DataColumn(attr.getKey(), Double.class));
					break;
				case DataType.AppDate:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class));
					break;
				case DataType.AppDateTime:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class));
					break;
				default:
					throw new RuntimeException(
							"@bulider insert sql error: 没有这个数据类型");
			}
		}
		return dt;
	}
	
	/**  区分大小写
	 * 空的Table 取到一个空表结构。
	 * 
	 * @return
	 */
	public final DataTable ToEmptyTableField()
	{
		DataTable dt = new DataTable();
		Entity en = this.getNewEntity();
		dt.TableName = en.getEnMap().getPhysicsTable();
		
		for (Attr attr : en.getEnMap().getAttrs())
		{
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
					break;
				case DataType.AppInt:
					dt.Columns.Add(new DataColumn(attr.getKey(), Integer.class, true));
					break;
				case DataType.AppFloat:
					dt.Columns.Add(new DataColumn(attr.getKey(), Float.class, true));
					break;
				case DataType.AppBoolean:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
					break;
				case DataType.AppDouble:
					dt.Columns.Add(new DataColumn(attr.getKey(), Double.class, true));
					break;
				case DataType.AppMoney:
					dt.Columns.Add(new DataColumn(attr.getKey(), Double.class, true));
					break;
				case DataType.AppDate:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
					break;
				case DataType.AppDateTime:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class, true));
					break;
				default:
					throw new RuntimeException(
							"@bulider insert sql error: 没有这个数据类型");
			}
		}
		return dt;
	}
	
	public final DataTable ToEmptyTableStringField()
	{
		DataTable dt = new DataTable();
		Entity en = this.getNewEntity();
		dt.TableName = en.getEnMap().getPhysicsTable();
		
		for (Attr attr : en.getEnMap().getAttrs())
		{
			dt.Columns.Add(new DataColumn(attr.getKey(), String.class));
		}
		return dt;
	}
	
	public final DataTable ToEmptyTableDesc()
	{
		DataTable dt = new DataTable();
		Entity en = this.getNewEntity();
		try
		{
			
			for (Attr attr : en.getEnMap().getAttrs())
			{
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								String.class));
						break;
					case DataType.AppInt:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								Integer.class));
						break;
					case DataType.AppFloat:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								Float.class));
						break;
					case DataType.AppBoolean:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								String.class));
						break;
					case DataType.AppDouble:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								Double.class));
						break;
					case DataType.AppMoney:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								Double.class));
						break;
					case DataType.AppDate:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								String.class));
						break;
					case DataType.AppDateTime:
						dt.Columns.Add(new DataColumn(attr.getDesc(),
								String.class));
						break;
					default:
						throw new RuntimeException(
								"@bulider insert sql error: 没有这个数据类型");
				}
			}
		} catch (RuntimeException ex)
		{
			throw new RuntimeException(en.getEnDesc() + " error "
					+ ex.getMessage());
			
		}
		return dt;
	}
	
	// 分组方法
	// 查询from cash
	/**
	 * 缓存查询: 根据 in sql 方式进行。
	 * 
	 * @param cashKey
	 *            指定的缓存Key，全局变量不要重复。
	 * @param inSQL
	 *            sql 语句
	 * @return 返回放在缓存里面的结果集合
	 * @throws Exception 
	 */
	public final int RetrieveFromCashInSQL(String cashKey, String inSQL) throws Exception
	{
		this.clear();
		BP.En.Entities tempVar = Cash.GetEnsDataExt(cashKey);
		Entities ens = (Entities) ((tempVar instanceof Entities) ? tempVar
				: null);
		if (ens == null)
		{
			QueryObject qo = new QueryObject(this);
			qo.AddWhereInSQL(this.getNewEntity().getPK(), inSQL);
			qo.DoQuery();
			Cash.SetEnsDataExt(cashKey, this);
		} else
		{
			this.AddEntities(ens);
		}
		return this.size();
	}
	
	/**
	 * 缓存查询: 根据相关的条件
	 * 
	 * @param attrKey
	 *            属性: 比如 FK_Sort
	 * @param val
	 *            值: 比如:01
	 * @param top
	 *            最大的取值信息
	 * @param orderBy
	 *            排序字段
	 * @param isDesc
	 * @return 返回放在缓存里面的结果集合
	 * @throws Exception 
	 */
	public final int RetrieveFromCash(String attrKey, Object val, int top,
			String orderBy, boolean isDesc) throws Exception
	{
		String cashKey = this.toString() + attrKey + val + top + orderBy
				+ isDesc;
		this.clear();
		Entities ens = Cash.GetEnsDataExt(cashKey);
		if (ens == null)
		{
			QueryObject qo = new QueryObject(this);
			qo.setTop(top);
			
			if (attrKey.equals("") || attrKey == null)
			{
			} else
			{
				qo.AddWhere(attrKey, val);
			}
			
			if (orderBy != null)
			{
				if (isDesc)
				{
					qo.addOrderByDesc(orderBy);
				} else
				{
					qo.addOrderBy(orderBy);
				}
			}
			
			qo.DoQuery();
			Cash.SetEnsDataExt(cashKey, this);
		} else
		{
			this.AddEntities(ens);
		}
		return this.size();
	}
	
	/**
	 * 缓存查询: 根据相关的条件
	 * 
	 * @param attrKey
	 * @param val
	 * @return
	 * @throws Exception 
	 */
	public final int RetrieveFromCash(String attrKey, Object val) throws Exception
	{
		return RetrieveFromCash(attrKey, val, 99999, null, true);
	}
	
	/**
	 * 缓存查询: 根据相关的条件
	 * 
	 * @param attrKey
	 * @param val
	 * @param orderby
	 * @return
	 * @throws Exception 
	 */
	public final int RetrieveFromCash(String attrKey, Object val, String orderby) throws Exception
	{
		return RetrieveFromCash(attrKey, val, 99999, orderby, true);
	}
	
	/**
	 * 缓存查询: 根据相关的条件
	 * 
	 * @param top
	 * @param orderBy
	 * @param isDesc
	 * @return
	 * @throws Exception 
	 */
	public final int RetrieveFromCash(String orderBy, boolean isDesc, int top) throws Exception
	{
		return RetrieveFromCash(null, null, top, orderBy, isDesc);
	}
	
	// 包含方法
	/**
	 * 是否包含任意一个实体主键编号
	 * 
	 * @param keys
	 *            多个主键用,符合分开
	 * @return true包含任意一个，fale 一个都不包含.
	 */
	public final boolean ContainsAnyOnePK(String keys)
	{
		keys = "," + keys + ",";
		for (Entity en : convertEntities(this))
		{
			if (keys.contains("," + en.getPKVal() + ","))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 包含所有的主键
	 * 
	 * @param keys
	 *            多个主键用,符合分开
	 * @return true全部包含.
	 */
	public final boolean ContainsAllPK(String keys)
	{
		keys = "," + keys + ",";
		for (Entity en : convertEntities(this))
		{
			if (!keys.contains("," + en.getPKVal() + ","))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		return this.getClass().getName();
	}
	
	public <T> List<T> toList()
	{
		return (List<T>)(Object)this;
	}
	public List<Entity> ToJavaListEn()
	{
		return (List<Entity>)(Object)this;
	}
	
	
	/** 
	 * ToJson.
	 * @return 
	 */
	public final String ToJson()
	{
		return ToJson("dt");
	}
	public final String ToJson(String dtName)
	{
		return BP.Tools.Json.ToJson(this.ToDataTableField(dtName));
	}

}