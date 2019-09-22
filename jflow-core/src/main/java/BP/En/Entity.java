package BP.En;

import BP.DA.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 Entity 的摘要说明。
*/
public abstract class Entity extends EnObj implements Serializable
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region mapInfotoJson

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与缓存有关的操作
	private Entities _GetNewEntities = null;
	public Entities getGetNewEntities()
	{
		if (_GetNewEntities == null)
		{
			String str = this.toString();
			ArrayList al = BP.En.ClassFactory.GetObjects("BP.En.Entities");
			for (Object o : al)
			{
				Entities ens = o instanceof Entities ? (Entities)o : null;

				if (ens == null)
				{
					continue;
				}
				if (ens.getGetNewEntity().toString().equals(str))
				{
					_GetNewEntities = ens;
					return _GetNewEntities;
				}
			}
			throw new RuntimeException("@no ens" + this.toString());
		}
		return _GetNewEntities;
	}
	public String getClassID()
	{
		return this.toString();
	}
	public String getClassIDOfShort()
	{
		String clsID = this.getClassID();
		return clsID.substring(clsID.lastIndexOf('.') + 1);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与sql操作有关
	protected SQLCash _SQLCash = null;
	public SQLCash getSQLCash()
	{
		if (_SQLCash == null)
		{
			_SQLCash = BP.DA.Cash.GetSQL(this.toString());
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL(this.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}
	public void setSQLCash(SQLCash value)
	{
		_SQLCash = value;
	}
	/** 
	 转化成
	 
	 @return 
	*/
	public final String ToStringAtParas()
	{
		String str = "";
		for (Attr attr : this.getEnMap().getAttrs())
		{
			str += "@" + attr.getKey() + "=" + this.GetValByKey(attr.getKey());
		}
		return str;
	}
	public final BP.DA.KeyVals ToKeyVals()
	{
		KeyVals kvs = new KeyVals();
		for (Attr attr : this.getEnMap().getAttrs())
		{
			//kvs.Add(attr.Key, this.GetValByKey(attr.Key), );
		}
		return kvs;
	}

	/** 
	 把一个实体转化成Json.
	 
	 @param isInParaFields 是否转换参数字段
	 @return 返回该实体的单个json
	*/

	public final String ToJson()
	{
		return ToJson(true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string ToJson(bool isInParaFields = true)
	public final String ToJson(boolean isInParaFields)
	{
		Hashtable ht = this.getRow();
		//如果不包含参数字段.
		if (isInParaFields == false)
		{
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}



		if (ht.containsKey("AtPara") == false)
		{
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}

		try
		{
			/*如果包含这个字段  @FK_BanJi=01 */
			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet())
			{
				if (ht.containsKey(key) == true)
				{
					continue;
				}
				ht.put(key, ap.getHisHT().get(key));
			}

			//把参数属性移除.
			ht.remove("_ATObj_");
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteWarning("@ ToJson " + ex.getMessage());
		}

		return BP.Tools.Json.ToJson(ht);
	}

	/** 
	 转化成json字符串，包含外键与枚举，主表使用Main命名。
	 外键使用外键表命名，枚举值使用枚举值命名。
	 
	 @return 
	*/
	public final String ToJsonIncludeEnumAndForeignKey()
	{
		DataSet ds = new DataSet();

		ds.Tables.Add(this.ToDataTableField()); //把主表数据加入进去.

		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getUIBindKey().equals(""))
			{
				continue;
			}

			if (attr.getIsEnum())
			{
				if (ds.Tables.Contains(attr.getUIBindKey()))
				{
					continue;
				}
				SysEnums ses = new SysEnums(attr.getUIBindKey());

				DataTable dt = ses.ToDataTableField(attr.getUIBindKey()); //把枚举加入进去.
				ds.Tables.Add(dt); //把这个枚举值加入进去..
				continue;
			}

			if (attr.getIsFK())
			{
				if (ds.Tables.Contains(attr.getUIBindKey()))
				{
					continue;
				}

				Entities ens = attr.getHisFKEns();
				ens.RetrieveAll();

				DataTable dt = ens.ToDataTableField(attr.getUIBindKey()); //把外键表加入进去.
				ds.Tables.Add(dt); //把这个枚举值加入进去..
				continue;
			}
		}
		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 创建一个空的表
	 
	 @param en
	 @return 
	*/

	public final DataTable ToEmptyTableField()
	{
		return ToEmptyTableField(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public DataTable ToEmptyTableField(Entity en = null)
	public final DataTable ToEmptyTableField(Entity en)
	{
		DataTable dt = new DataTable();
		if (en == null)
		{
			en = this;
		}

		dt.TableName = en.getEnMap().getPhysicsTable();

		for (Attr attr : en.getEnMap().getAttrs())
		{
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					dt.Columns.Add(new DataColumn(attr.getKey(), String.class));
					break;
				case DataType.AppInt:
					dt.Columns.Add(new DataColumn(attr.getKey(), Integer.class));
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
					throw new RuntimeException("@bulider insert sql error: 没有这个数据类型");
			}
		}
		return dt;
	}

	public final DataTable ToDataTableField()
	{
		return ToDataTableField("Main");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public DataTable ToDataTableField(string tableName = "Main")
	public final DataTable ToDataTableField(String tableName)
	{
		DataTable dt = this.ToEmptyTableField(this);
		dt.TableName = tableName;

		//增加参数列.
		if (this.getRow().containsKey("AtPara") == true)
		{
			/*如果包含这个字段,就说明他有参数,把参数也要弄成一个列.*/
			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet())
			{
				if (dt.Columns.Contains(key) == true)
				{
					continue;
				}

				dt.Columns.Add(key, String.class);
			}
		}

		DataRow dr = dt.NewRow();
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyDataType() == DataType.AppBoolean)
			{
				if (this.GetValIntByKey(attr.getKey()) == 1)
				{
					dr.set(attr.getKey(), "1");
				}
				else
				{
					dr.set(attr.getKey(), "0");
				}
				continue;
			}

			/*如果是外键 就要去掉左右空格。
			 *  */
			if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
			{
				dr.set(attr.getKey(), this.GetValByKey(attr.getKey()).toString().trim());
			}
			else
			{
				//@sly 
				Object obj = this.GetValByKey(attr.getKey());
				if (obj == null && attr.getIsNum())
				{
					dr.set(attr.getKey(), 0);
					continue;
				}

				if (attr.getIsNum() == true && DataType.IsNumStr(obj.toString()) == false)
				{
					dr.set(attr.getKey(), 0);
				}
				else
				{
					dr.set(attr.getKey(), obj);
				}
			}
		}

		if (this.getRow().containsKey("AtPara") == true)
		{
			/*如果包含这个字段*/
			AtPara ap = this.getatPara();
			for (String key : ap.getHisHT().keySet())
			{
				dr.set(key, ap.getHisHT().get(key));
			}
		}

		dt.Rows.Add(dr);
		return dt;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于database 操作
	public final int RunSQL(String sql)
	{
		Paras ps = new Paras();
		ps.SQL = sql;
		return this.RunSQL(ps);
	}
	/** 
	 在此实体是运行sql 返回结果集合
	 
	 @param sql 要运行的sql
	 @return 执行的结果
	*/
	public final int RunSQL(Paras ps)
	{
		switch (this.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				return DBAccess.RunSQL(ps);
			//case DBUrlType.DBAccessOfMSSQL1:
			//    return DBAccessOfMSSQL1.RunSQL(ps.SQL);
			//case DBUrlType.DBAccessOfMSSQL2:
			//    return DBAccessOfMSSQL2.RunSQL(ps.SQL);
			//case DBUrlType.DBAccessOfOracle1:
			//    return DBAccessOfOracle1.RunSQL(ps.SQL);
			//case DBUrlType.DBAccessOfOracle2:
			//    return DBAccessOfOracle2.RunSQL(ps.SQL);
			case DBSrc:
				return this.getEnMap().getEnDBUrl().getHisDBSrc().RunSQL(ps.SQL, ps);
			default:
				throw new RuntimeException("@没有设置类型。");
		}
	}
	public final int RunSQL(String sql, Paras paras)
	{
		switch (this.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				return DBAccess.RunSQL(sql, paras);
			//case DBUrlType.DBAccessOfMSSQL1:
			//    return DBAccessOfMSSQL1.RunSQL(sql);
			//case DBUrlType.DBAccessOfMSSQL2:
			//    return DBAccessOfMSSQL2.RunSQL(sql);
			//case DBUrlType.DBAccessOfOracle1:
			//    return DBAccessOfOracle1.RunSQL(sql);
			//case DBUrlType.DBAccessOfOracle2:
			//    return DBAccessOfOracle2.RunSQL(sql);
			case DBSrc:
				return this.getEnMap().getEnDBUrl().getHisDBSrc().RunSQL(sql, paras);
			default:
				throw new RuntimeException("@没有设置类型。");
		}
	}
	/** 
	 
	 在此实体是运行sql 返回结果集合
	 
	 @param sql 要运行的 select sql
	 @return 执行的查询结果
	*/
	public final DataTable RunSQLReturnTable(String sql)
	{
		switch (this.getEnMap().getEnDBUrl().getDBUrlType())
		{
			case AppCenterDSN:
				return DBAccess.RunSQLReturnTable(sql);
			//case DBUrlType.DBAccessOfMSSQL1:
			//    return DBAccessOfMSSQL1.RunSQLReturnTable(sql);
			//case DBUrlType.DBAccessOfMSSQL2:
			//    return DBAccessOfMSSQL2.RunSQLReturnTable(sql);
			//case DBUrlType.DBAccessOfOracle1:
			//    return DBAccessOfOracle1.RunSQLReturnTable(sql);
			//case DBUrlType.DBAccessOfOracle2:
			//    return DBAccessOfOracle2.RunSQLReturnTable(sql);
			case DBSrc:
				return this.getEnMap().getEnDBUrl().getHisDBSrc().RunSQLReturnTable(sql);
			default:
				throw new RuntimeException("@没有设置类型。");
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于明细的操作
	public final Entities GetEnsDaOfOneVSM(AttrOfOneVSM attr)
	{
		Entities ensOfMM = attr.getEnsOfMM();
		Entities ensOfM = attr.getEnsOfM();
		ensOfM.Clear();

		QueryObject qo = new QueryObject(ensOfMM);
		qo.AddWhere(attr.getAttrOfOneInMM(), this.getPKVal().toString());
		qo.DoQuery();

		for (Entity en : ensOfMM)
		{
			Entity enOfM = ensOfM.getGetNewEntity();
			enOfM.setPKVal(en.GetValStringByKey(attr.getAttrOfMInMM()));
			enOfM.Retrieve();
			ensOfM.AddEntity(enOfM);
		}
		return ensOfM;
	}
	/** 
	 取得实体集合多对多的实体集合.
	 
	 @param ensOfMMclassName 实体集合的类名称
	 @return 数据实体
	*/
	public final Entities GetEnsDaOfOneVSM(String ensOfMMclassName)
	{
		AttrOfOneVSM attr = this.getEnMap().GetAttrOfOneVSM(ensOfMMclassName);

		return GetEnsDaOfOneVSM(attr);
	}
	public final Entities GetEnsDaOfOneVSMFirst()
	{
		AttrOfOneVSM attr = this.getEnMap().getAttrsOfOneVSM().get(0);
		//	throw new Exception("err "+attr.Desc); 
		return this.GetEnsDaOfOneVSM(attr);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于明细的操作
	/** 
	 得到他的数据实体
	 
	 @param EnsName 类名称
	 @return 
	*/
	public final Entities GetDtlEnsDa(String EnsName)
	{
		Entities ens = ClassFactory.GetEns(EnsName);
		return GetDtlEnsDa(ens);
		/*
		EnDtls eds =this.EnMap.Dtls; 
		foreach(EnDtl ed in eds)
		{
		    if (ed.EnsName==EnsName)
		    {
		        Entities ens =ClassFactory.GetEns(EnsName) ; 
		        QueryObject qo = new QueryObject(ClassFactory.GetEns(EnsName));
		        qo.AddWhere(ed.RefKey,this.PKVal.ToString());
		        qo.DoQuery();
		        return ens;
		    }
		}
		throw new Exception("@实体["+this.EnDesc+"],不包含"+EnsName);	
		*/
	}
	/** 
	 取出他的数据实体
	 
	 @param ens 集合
	 @return 执行后的实体信息
	*/
	public final Entities GetDtlEnsDa(Entities ens)
	{
		for (EnDtl dtl : this.getEnMap().getDtls())
		{
			if (dtl.getEns().getClass() == ens.getClass())
			{
				QueryObject qo = new QueryObject(dtl.getEns());
				qo.AddWhere(dtl.getRefKey(), this.getPKVal().toString());
				qo.DoQuery();
				return dtl.getEns();
			}
		}
		throw new RuntimeException("@在取[" + this.getEnDesc() + "]的明细时出现错误。[" + ens.getGetNewEntity().getEnDesc() + "],不在他的集合内。");
	}


	public final Entities GetDtlEnsDa(EnDtl dtl)
	{
		return GetDtlEnsDa(dtl, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public Entities GetDtlEnsDa(EnDtl dtl, string pkval = null)
	public final Entities GetDtlEnsDa(EnDtl dtl, String pkval)
	{
		try
		{
			if (pkval == null)
			{
				pkval = this.getPKVal().toString();
			}
			QueryObject qo = new QueryObject(dtl.getEns());
			MapDtl md = new MapDtl();
			md.setNo(dtl.getEns().getGetNewEntity().getClassID());
			if (md.RetrieveFromDBSources() == 0)
			{
				qo.AddWhere(dtl.getRefKey(), pkval);
				qo.DoQuery();
				return dtl.getEns();
			}

			//如果是freefrm 就考虑他的权限控制问题. 
			switch (md.getDtlOpenType())
			{
				case ForEmp: // 按人员来控制.
					qo.AddWhere(GEDtlAttr.RefPK, pkval);
					qo.addAnd();
					qo.AddWhere(GEDtlAttr.Rec, BP.Web.WebUser.getNo());
					break;
				case ForWorkID: // 按工作ID来控制
					qo.AddWhere(GEDtlAttr.RefPK, pkval);
					break;
				case ForFID: // 按流程ID来控制.这里不允许修改，如需修改则加新case.
					//if (nd == null)
					//    throw new Exception("@当前您是配置的权限是FID,但是当前没有节点ID.");

					//if (nd.HisNodeWorkType == BP.WF.NodeWorkType.SubThreadWork)
					//    qo.AddWhere(GEDtlAttr.RefPK, this.FID); //edit by zhoupeng 2016.04.23
					//else
					qo.AddWhere(GEDtlAttr.FID, pkval);
					break;
			}

			if (!md.getFilterSQLExp().equals(""))
			{
				String[] strs = md.getFilterSQLExp().split("[=]", -1);
				qo.addAnd();
				qo.AddWhere(strs[0], strs[1]);
			}

			qo.DoQuery();
			return dtl.getEns();
		}
		catch (RuntimeException e)
		{
			throw new RuntimeException("@在取[" + this.getEnDesc() + "]的明细时出现错误。[" + dtl.getDesc() + "],不在他的集合内。");
		}
	}

	//		/// <summary>
	//		/// 返回第一个实体
	//		/// </summary>
	//		/// <returns>返回第一个实体,如果没有就抛出异常</returns>
	//		public Entities GetDtl()
	//		{
	//			 return this.GetDtls(0);
	//		}
	//		/// <summary>
	//		/// 返回第一个实体
	//		/// </summary>
	//		/// <returns>返回第一个实体</returns>
	//		public Entities GetDtl(int index)
	//		{
	//			try
	//			{
	//				return this.GetDtls(this.EnMap.Dtls[index].Ens);
	//			}
	//			catch( Exception ex)
	//			{
	//				throw new Exception("@在取得按照顺序取["+this.EnDesc+"]的明细,出现错误:"+ex.Message);
	//			}			 
	//		}
	/** 
	 取出他的明细集合。
	 
	 @return 
	*/
	public final ArrayList GetDtlsDatasOfArrayList()
	{
		ArrayList al = new ArrayList();
		for (EnDtl dtl : this.getEnMap().getDtls())
		{
			al.add(this.GetDtlEnsDa(dtl.getEns()));
		}
		return al;
	}


	public final java.util.ArrayList<Entities> GetDtlsDatasOfList()
	{
		return GetDtlsDatasOfList(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public List<Entities> GetDtlsDatasOfList(string pkval = null)
	public final ArrayList<Entities> GetDtlsDatasOfList(String pkval)
	{
		ArrayList<Entities> al = new ArrayList<Entities>();
		for (EnDtl dtl : this.getEnMap().getDtls())
		{
			al.add(this.GetDtlEnsDa(dtl, pkval));
		}
		return al;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 检查一个属性值是否存在于实体集合中
	/** 
	 检查一个属性值是否存在于实体集合中
	 这个方法经常用到在beforeinsert中。
	 
	 @param key 要检查的key.
	 @param val 要检查的key.对应的val
	 @return 
	*/
	protected final int ExitsValueNum(String key, String val)
	{
		String field = this.getEnMap().GetFieldByKey(key);
		Paras ps = new Paras();
		ps.Add("p", val);

		String sql = "SELECT COUNT( " + key + " ) FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + key + "=" + this.getHisDBVarStr() + "p";
		return Integer.parseInt(DBAccess.RunSQLReturnVal(sql, ps).toString());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 于编号有关系的处理。
	/** 
	 这个方法是为不分级字典，生成一个编号。根据制订的 属性.
	 
	 @param attrKey 属性
	 @return 产生的序号 
	*/

	public final String GenerNewNoByKey(String attrKey)
	{
		return GenerNewNoByKey(attrKey, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string GenerNewNoByKey(string attrKey, Attr attr = null)
	public final String GenerNewNoByKey(String attrKey, Attr attr)
	{
		try
		{
			String sql = null;
			if (attr == null)
			{
				attr = this.getEnMap().GetAttrByKey(attrKey);
			}
		//    if (attr.UIIsReadonly == false)
		  //      throw new Exception("@需要自动生成编号的列(" + attr.Key + ")必须为只读。");

			String field = this.getEnMap().GetFieldByKey(attrKey);
			switch (this.getEnMap().getEnDBUrl().getDBType())
			{
				case MSSQL:
					sql = "SELECT CONVERT(INT, MAX(CAST(" + field + " as int)) )+1 AS No FROM " + this.get_enMap().getPhysicsTable();
					break;
				case PostgreSQL:
					sql = "SELECT to_number( MAX(" + field + ") ,'99999999')+1   FROM " + this.get_enMap().getPhysicsTable();
					break;
				case Oracle:
					sql = "SELECT MAX(" + field + ") +1 AS No FROM " + this.get_enMap().getPhysicsTable();
					break;
				case MySQL:
					sql = "SELECT CONVERT(MAX(CAST(" + field + " AS SIGNED INTEGER)),SIGNED) +1 AS No FROM " + this.get_enMap().getPhysicsTable();
					break;
				case Informix:
					sql = "SELECT MAX(" + field + ") +1 AS No FROM " + this.get_enMap().getPhysicsTable();
					break;
				case Access:
					sql = "SELECT MAX( [" + field + "]) +1 AS  No FROM " + this.get_enMap().getPhysicsTable();
					break;
				default:
					throw new RuntimeException("error");
			}
			String str = String.valueOf(DBAccess.RunSQLReturnValInt(sql, 1));
			if (str.equals("0") || str.equals(""))
			{
				str = "1";
			}
			return tangible.StringHelper.padLeft(str, Integer.parseInt(this.get_enMap().getCodeStruct()), '0');
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
	}
	/** 
	 按照一列产生顺序号码。
	 
	 @param attrKey 要产生的列
	 @param attrGroupKey 分组的列名
	 @param FKVal 分组的主键
	 @return 		
	*/
	public final String GenerNewNoByKey(int nolength, String attrKey, String attrGroupKey, String attrGroupVal)
	{
		if (attrGroupKey == null || attrGroupVal == null)
		{
			throw new RuntimeException("@分组字段attrGroupKey attrGroupVal 不能为空");
		}

		Paras ps = new Paras();
		ps.Add("groupKey", attrGroupKey);
		ps.Add("groupVal", attrGroupVal);

		String sql = "";
		String field = this.getEnMap().GetFieldByKey(attrKey);
		ps.Add("f", attrKey);

		switch (this.getEnMap().getEnDBUrl().getDBType())
		{
			case MSSQL:
				sql = "SELECT CONVERT(bigint, MAX([" + field + "]))+1 AS Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + attrGroupKey + "='" + attrGroupVal + "'";
				break;
			case Oracle:
			case Informix:
				sql = "SELECT MAX( :f )+1 AS No FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getHisDBVarStr() + "groupKey=" + this.getHisDBVarStr() + "groupVal ";
				break;
			case MySQL:
				sql = "SELECT MAX(" + field + ") +1 AS Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + attrGroupKey + "='" + attrGroupVal + "'";
				break;
			case Access:
				sql = "SELECT MAX([" + field + "]) +1 AS Num FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + attrGroupKey + "='" + attrGroupVal + "'";
				break;
			default:
				throw new RuntimeException("error");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		String str = "1";
		if (dt.Rows.Count != 0)
		{
			//System.DBNull n = new DBNull();
			if (dt.Rows[0][0] instanceof DBNull)
			{
				str = "1";
			}
			else
			{
				str = dt.Rows[0][0].toString();
			}
		}
		return tangible.StringHelper.padLeft(str, nolength, '0');
	}
	public final String GenerNewNoByKey(String attrKey, String attrGroupKey, String attrGroupVal)
	{
		return this.GenerNewNoByKey(Integer.parseInt(this.getEnMap().getCodeStruct()), attrKey, attrGroupKey, attrGroupVal);
	}
	/** 
	 按照两列查生顺序号码。
	 
	 @param attrKey
	 @param attrGroupKey1
	 @param attrGroupKey2
	 @param attrGroupVal1
	 @param attrGroupVal2
	 @return 
	*/
	public final String GenerNewNoByKey(String attrKey, String attrGroupKey1, String attrGroupKey2, Object attrGroupVal1, Object attrGroupVal2)
	{
		String f = this.getEnMap().GetFieldByKey(attrKey);
		Paras ps = new Paras();
		//   ps.Add("f", f);

		String sql = "";
		switch (this.getEnMap().getEnDBUrl().getDBType())
		{
			case Oracle:
			case Informix:
				sql = "SELECT   MAX(" + f + ") +1 AS No FROM " + this.getEnMap().getPhysicsTable();
				break;
			case MSSQL:
				sql = "SELECT CONVERT(INT, MAX(" + this.getEnMap().GetFieldByKey(attrKey) + ") )+1 AS No FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetFieldByKey(attrGroupKey1) + "='" + attrGroupVal1 + "' AND " + this.getEnMap().GetFieldByKey(attrGroupKey2) + "='" + attrGroupVal2 + "'";
				break;
			case Access:
				sql = "SELECT CONVERT(INT, MAX(" + this.getEnMap().GetFieldByKey(attrKey) + ") )+1 AS No FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetFieldByKey(attrGroupKey1) + "='" + attrGroupVal1 + "' AND " + this.getEnMap().GetFieldByKey(attrGroupKey2) + "='" + attrGroupVal2 + "'";
				break;
			default:
				break;
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql, ps);
		String str = "1";
		if (dt.Rows.Count != 0)
		{
			str = dt.Rows[0][0].toString();
		}
		return tangible.StringHelper.padLeft(str, Integer.parseInt(this.getEnMap().getCodeStruct()), '0');
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public Entity()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 排序操作
	protected final void DoOrderUp(String idxAttr)
	{
		//  string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + "  ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows)
		{
			idx++;
			myNo = dr.get(pk).toString();
			if (pkval.equals(myNo))
			{
				isMeet = true;
			}

			if (isMeet == false)
			{
				beforeNo = myNo;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");
		}
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo + "'");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval + "'");
	}
	protected final void DoOrderUp(String groupKeyAttr, String groupKeyVal, String idxAttr)
	{
		//  string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + " WHERE " + groupKeyAttr + "='" + groupKeyVal + "' ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows)
		{
			idx++;
			myNo = dr.get(pk).toString();
			if (pkval.equals(myNo))
			{
				isMeet = true;
			}

			if (isMeet == false)
			{
				beforeNo = myNo;
			}
			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");
		}
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo + "'");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval + "'");
	}
	protected final void DoOrderUp(String groupKeyAttr, String groupKeyVal, String gKey2, String gVal2, String idxAttr)
	{
		//  string pkval = this.PKVal as string;
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + "," + idxAttr + " FROM " + table + " WHERE (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKey2 + "='" + gVal2 + "') ORDER BY " + idxAttr;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String beforeNo = "";
		String myNo = "";
		boolean isMeet = false;

		for (DataRow dr : dt.Rows)
		{
			idx++;
			myNo = dr.get(pk).toString();

			if (myNo.equals(pkval) == true)
			{
				isMeet = true;
			}

			if (isMeet == false)
			{
				beforeNo = myNo;
			}

			DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'");
		}
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + beforeNo + "'");
		DBAccess.RunSQL("UPDATE " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + pkval + "'");
	}

	protected final void DoOrderDown(String idxAttr)
	{
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + "  order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;

		String sqls = "";
		for (DataRow dr : dt.Rows)
		{
			myNo = dr.get(pk).toString();
			if (isMeet == true)
			{
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo))
			{
				isMeet = true;
			}

			sqls += "@ UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'";
		}

		sqls += "@ UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo + "'";
		sqls += "@ UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval + "'";

		BP.DA.DBAccess.RunSQLs(sqls);
	}
	protected final void DoOrderDown(String groupKeyAttr, String groupKeyVal, String idxAttr)
	{
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + " WHERE " + groupKeyAttr + "='" + groupKeyVal + "' order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;

		String sqls = "";
		for (DataRow dr : dt.Rows)
		{
			myNo = dr.get(pk).toString();
			if (isMeet == true)
			{
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo))
			{
				isMeet = true;
			}

			sqls += "@ UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "'";
		}

		sqls += "@ UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo + "'";
		sqls += "@ UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval + "'";

		BP.DA.DBAccess.RunSQLs(sqls);
	}
	protected final void DoOrderDown(String groupKeyAttr, String groupKeyVal, String gKeyAttr2, String gKeyVal2, String idxAttr)
	{
		String pkval = this.getPKVal().toString();
		String pk = this.getPK();
		String table = this.getEnMap().getPhysicsTable();

		String sql = "SELECT " + pk + " ," + idxAttr + " FROM " + table + " WHERE (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' ) order by " + idxAttr;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = 0;
		String nextNo = "";
		String myNo = "";
		boolean isMeet = false;

		String sqls = "";
		for (DataRow dr : dt.Rows)
		{
			myNo = dr.get(pk).toString();
			if (isMeet == true)
			{
				nextNo = myNo;
				isMeet = false;
			}
			idx++;

			if (pkval.equals(myNo))
			{
				isMeet = true;
			}

			sqls += "@UPDATE " + table + " SET " + idxAttr + "=" + idx + " WHERE " + pk + "='" + myNo + "' AND  (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' ) ";
		}

		sqls += "@ UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "-1 WHERE " + pk + "='" + nextNo + "' AND (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' )";
		sqls += "@ UPDATE  " + table + " SET " + idxAttr + "=" + idxAttr + "+1 WHERE " + pk + "='" + pkval + "' AND (" + groupKeyAttr + "='" + groupKeyVal + "' AND " + gKeyAttr2 + "='" + gKeyVal2 + "' )";

		BP.DA.DBAccess.RunSQLs(sqls);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 排序操作

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 直接操作
	/** 
	 直接更新
	*/
	public final int DirectUpdate()
	{
		return EntityDBAccess.Update(this, null);
	}
	/** 
	 直接的Insert
	*/
	public int DirectInsert()
	{
		try
		{
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					return this.RunSQL(this.getSQLCash().Insert, SqlBuilder.GenerParas(this, null));
				case Access:
					return this.RunSQL(this.getSQLCash().Insert, SqlBuilder.GenerParas(this, null));
					break;
				case MySQL:
				case Informix:
				default:
					return this.RunSQL(this.getSQLCash().Insert.replace("[", "").replace("]", ""), SqlBuilder.GenerParas(this, null));
			}
		}
		catch (RuntimeException ex)
		{
			this.roll();
			if (SystemConfig.getIsDebug())
			{
				try
				{
					this.CheckPhysicsTable();
				}
				catch (RuntimeException ex1)
				{
					throw new RuntimeException(ex.getMessage() + " == " + ex1.getMessage());
				}
			}
			throw ex;
		}

		//this.RunSQL(this.SQLCash.Insert, SqlBuilder.GenerParas(this, null));
	}
	/** 
	 直接的Delete
	*/
	public final void DirectDelete()
	{
		EntityDBAccess.Delete(this);
	}
	public final void DirectSave()
	{
		if (this.getIsExits())
		{
			this.DirectUpdate();
		}
		else
		{
			this.DirectInsert();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Retrieve
	/** 
	 按照属性查询
	 
	 @param attr 属性名称
	 @param val 值
	 @return 是否查询到
	*/
	public final boolean RetrieveByAttrAnd(String attr1, Object val1, String attr2, Object val2)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr1, val1);
		qo.addAnd();
		qo.AddWhere(attr2, val2);

		if (qo.DoQuery() >= 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 按照属性查询
	 
	 @param attr 属性名称
	 @param val 值
	 @return 是否查询到
	*/
	public final boolean RetrieveByAttrOr(String attr1, Object val1, String attr2, Object val2)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr1, val1);
		qo.addOr();
		qo.AddWhere(attr2, val2);

		if (qo.DoQuery() == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/** 
	 按照属性查询
	 
	 @param attr 属性名称
	 @param val 值
	 @return 是否查询到
	*/
	public final boolean RetrieveByAttr(String attr, Object val)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(attr, val);

		if (qo.DoQuery() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 从DBSources直接查询
	 
	 @return 查询的个数
	*/
	public int RetrieveFromDBSources()
	{
		try
		{
			return EntityDBAccess.Retrieve(this, this.getSQLCash().Select, SqlBuilder.GenerParasPK(this));
		}
		catch (java.lang.Exception e)
		{
			this.CheckPhysicsTable();
			return EntityDBAccess.Retrieve(this, this.getSQLCash().Select, SqlBuilder.GenerParasPK(this));
		}
	}
	/** 
	 查询
	 
	 @param key
	 @param val
	 @return 
	*/
	public final int Retrieve(String key, Object val)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key, val);
		return qo.DoQuery();
	}

	public final int Retrieve(String key1, Object val1, String key2, Object val2)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		return qo.DoQuery();
	}
	public final int Retrieve(String key1, Object val1, String key2, Object val2, String key3, Object val3)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(key1, val1);
		qo.addAnd();
		qo.AddWhere(key2, val2);
		qo.addAnd();
		qo.AddWhere(key3, val3);
		return qo.DoQuery();
	}
	/** 
	 按主键查询，返回查询出来的个数。
	 如果查询出来的是多个实体，那把第一个实体给值。	 
	 
	 @return 查询出来的个数
	*/
	public int Retrieve()
	{
		/*如果是没有放入缓存的实体. @wangyanyan */
		if (this.getEnMap().getDepositaryOfEntity() == Depositary.Application)
		{
			BP.En.Row row = BP.DA.Cash2019.GetRow(this.toString(), this.getPKVal().toString());
			if (row != null && row.size() > 2)
			{
				this.setRow(row);
				return 1;
			}
		}

		try
		{
			int num = EntityDBAccess.Retrieve(this, this.getSQLCash().Select, SqlBuilder.GenerParasPK(this));
			if (num >= 1)
			{
				//@wangyanyan 放入缓存.
				if (this.getEnMap().getDepositaryOfEntity() == Depositary.Application)
				{
					BP.DA.Cash2019.PutRow(this.toString(), this.getPKVal().toString(), this.getRow());
				}
				return num;
			}
		}
		catch (RuntimeException ex)
		{
			if (ex.getMessage().Contains("does not exist") || ex.getMessage().Contains("不存在") || ex.getMessage().Contains("无效") || ex.getMessage().Contains("field list"))
			{
				this.CheckPhysicsTable();
				if (BP.DA.DBAccess.IsView(this.getEnMap().getPhysicsTable(), SystemConfig.getAppCenterDBType()) == false)
				{
					return Retrieve(); //让其在查询一遍.
				}
			}
			throw new RuntimeException(ex.getMessage() + "@在Entity(" + this.toString() + ")查询期间出现错误@" + ex.StackTrace);
		}

		String msg = ""; //@sly 这里需要翻译到java.
		switch (this.getPK())
		{
			case "OID":
				msg += "[ 主键=OID 值=" + this.GetValStrByKey("OID") + " ]";
				break;
			case "No":
				msg += "[ 主键=No 值=" + this.GetValStrByKey("No") + " ]";
				break;
			case "MyPK":
				msg += "[ 主键=MyPK 值=" + this.GetValStrByKey("MyPK") + " ]";
				break;
			case "NodeID":
				msg += "[ 主键=NodeID 值=" + this.GetValStrByKey("NodeID") + " ]";
				break;
			case "WorkID":
				msg += "[ 主键=WorkID 值=" + this.GetValStrByKey("WorkID") + " ]";
				break;
			default:
				Hashtable ht = this.getPKVals();
				for (String key : ht.keySet())
				{
					msg += "[ 主键=" + key + " 值=" + ht.get(key) + " ]";
				}
				break;
		}
		Log.DefaultLogWriteLine(LogType.Error, "@没有[" + this.getEnMap().getEnDesc() + "  " + this.getEnMap().getPhysicsTable() + ", 类[" + this.toString() + "], 物理表[" + this.getEnMap().getPhysicsTable() + "] 实例。PK = " + this.GetValByKey(this.getPK()));
		throw new RuntimeException("@没有找到记录[" + this.getEnMap().getEnDesc() + "  " + this.getEnMap().getPhysicsTable() + ", " + msg + "记录不存在,请与管理员联系, 或者确认输入错误.");
	}
	/** 
	 判断是不是存在的方法.
	 
	 @return 
	*/
	public boolean getIsExits()
	{
		try
		{
			if (this.getPKField().contains(","))
			{
				Attrs attrs = this.getEnMap().getAttrs();

					/*说明多个主键*/
				QueryObject qo = new QueryObject(this);
				String[] pks = this.getPKField().split("[,]", -1);

				boolean isNeedAddAnd = false;
				for (String pk : pks)
				{
					if (DataType.IsNullOrEmpty(pk))
					{
						continue;
					}

					if (isNeedAddAnd == true)
					{
						qo.addAnd();
					}
					else
					{
						isNeedAddAnd = true;
					}

					Attr attr = attrs.GetAttrByKey(pk);
					switch (attr.getMyDataType())
					{
						case DataType.AppBoolean:
						case DataType.AppInt:
							qo.AddWhere(pk, this.GetValIntByKey(attr.getKey()));
							break;
						case DataType.AppDouble:
						case DataType.AppMoney:
							qo.AddWhere(pk, this.GetValDecimalByKey(attr.getKey()));
							break;
						default:
							qo.AddWhere(pk, this.GetValStringByKey(attr.getKey()));
							break;
					}

				}

				if (qo.DoQueryToTable().Rows.Count == 0)
				{
					return false;
				}

				return true;
			}

			Object obj = this.getPKVal();
			if (obj == null || obj.toString().equals(""))
			{
				return false;
			}

			if (this.getIsOIDEntity())
			{
				if (obj.toString().equals("0"))
				{
					return false;
				}
			}

				// 生成数据库判断语句。
			String selectSQL = "SELECT " + this.getPKField() + " FROM " + this.getEnMap().getPhysicsTable() + " WHERE ";
			switch (this.getEnMap().getEnDBUrl().getDBType())
			{
				case MSSQL:
					selectSQL += SqlBuilder.GetKeyConditionOfMS(this);
					break;
				case Oracle:
				case PostgreSQL:
					selectSQL += SqlBuilder.GetKeyConditionOfOraForPara(this);
					break;
				case Informix:
					selectSQL += SqlBuilder.GetKeyConditionOfInformixForPara(this);
					break;
				case MySQL:
					selectSQL += SqlBuilder.GetKeyConditionOfMS(this);
					break;
				case Access:
					selectSQL += SqlBuilder.GetKeyConditionOfOLE(this);
					break;
				default:
					throw new RuntimeException("@没有设计到。" + this.getEnMap().getEnDBUrl().getDBUrlType());
			}

				// 从数据库里面查询，判断有没有。
			switch (this.getEnMap().getEnDBUrl().getDBUrlType())
			{
				case AppCenterDSN:
					return DBAccess.IsExits(selectSQL, SqlBuilder.GenerParasPK(this));
					//case DBUrlType.DBAccessOfMSSQL1:
					//    return DBAccessOfMSSQL1.IsExits(selectSQL);
					//case DBUrlType.DBAccessOfMSSQL2:
					//    return DBAccessOfMSSQL2.IsExits(selectSQL);
					//case DBUrlType.DBAccessOfOracle1:
					//    return DBAccessOfOracle1.IsExits(selectSQL);
					//case DBUrlType.DBAccessOfOracle2:
					//    return DBAccessOfOracle2.IsExits(selectSQL);
				default:
					throw new RuntimeException("@没有设计到的DBUrl。" + this.getEnMap().getEnDBUrl().getDBUrlType());
			}

		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
	}
	/** 
	 按照主键查询，查询出来的结果不赋给当前的实体。
	 
	 @return 查询出来的个数
	*/
	public final DataTable RetrieveNotSetValues()
	{
		return this.RunSQLReturnTable(SqlBuilder.Retrieve(this));
	}
	/** 
	 这个表里是否存在
	 
	 @param pk
	 @param val
	 @return 
	*/
	public final boolean IsExit(String pk, Object val)
	{
		if (pk.equals("OID"))
		{
			if (Integer.parseInt(val.toString()) == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(pk, val);
		if (qo.DoQuery() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public final boolean IsExit(String pk1, Object val1, String pk2, Object val2)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(pk1, val1);
		qo.addAnd();
		qo.AddWhere(pk2, val2);

		if (qo.DoQuery() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public final boolean IsExit(String pk1, Object val1, String pk2, Object val2, String pk3, Object val3)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(pk1, val1);
		qo.addAnd();
		qo.AddWhere(pk2, val2);
		qo.addAnd();
		qo.AddWhere(pk3, val3);

		if (qo.DoQuery() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 删除.
	private boolean CheckDB()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据.
		//CheckDatas  ens=new CheckDatas(this.EnMap.PhysicsTable);
		//foreach(CheckData en in ens)
		//{
		//    string sql="DELETE  FROM "+en.RefTBName+"   WHERE  "+en.RefTBFK+" ='"+this.GetValByKey(en.MainTBPK) +"' ";	
		//    DBAccess.RunSQL(sql);
		//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否有明细
		for (BP.En.EnDtl dtl : this.getEnMap().getDtls())
		{
			String sql = "DELETE  FROM  " + dtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + "   WHERE  " + dtl.getRefKey() + " ='" + this.getPKVal().toString() + "' ";
			//DBAccess.RunSQL(sql);
			/*
			//string sql="SELECT "+dtl.RefKey+" FROM  "+dtl.Ens.GetNewEntity.EnMap.PhysicsTable+"   WHERE  "+dtl.RefKey+" ='"+this.PKVal.ToString() +"' ";	
			DataTable dt= DBAccess.RunSQLReturnTable(sql); 
			if(dt.Rows.Count==0)
			    continue;
			else
			    throw new Exception("@["+this.EnDesc+"],删除期间出现错误，它有["+dt.Rows.Count+"]个明细存在,不能删除！");
			    */
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return true;
	}
	/** 
	 删除之前要做的工作
	 
	 @return 
	*/
	protected boolean beforeDelete()
	{
		this.CheckDB();
		return true;
	}
	/** 
	 删除它关连的实体．
	*/
	public final void DeleteHisRefEns()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据.
		//			CheckDatas  ens=new CheckDatas(this.EnMap.PhysicsTable);
		//			foreach(CheckData en in ens)
		//			{
		//				string sql="DELETE  FROM "+en.RefTBName+"   WHERE  "+en.RefTBFK+" ='"+this.GetValByKey(en.MainTBPK) +"' ";	
		//				DBAccess.RunSQL(sql); 
		//			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否有明细
		for (BP.En.EnDtl dtl : this.getEnMap().getDtls())
		{
			String sql = "DELETE FROM " + dtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + "   WHERE  " + dtl.getRefKey() + " ='" + this.getPKVal().toString() + "' ";
			DBAccess.RunSQL(sql);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否有一对对的关系.
		for (BP.En.AttrOfOneVSM dtl : this.getEnMap().getAttrsOfOneVSM())
		{
			String sql = "DELETE  FROM " + dtl.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + "   WHERE  " + dtl.getAttrOfOneInMM() + " ='" + this.getPKVal().toString() + "' ";
			DBAccess.RunSQL(sql);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
	}
	/** 
	 把缓存删除
	*/
	public final void DeleteDataAndCash()
	{
		this.Delete();
		this.DeleteFromCash();
	}
	public final void DeleteFromCash()
	{
		//删除缓存.
		CashEntity.Delete(this.toString(), this.getPKVal().toString());
		// 删除数据.
		this.getRow().clear();
	}
	public final int Delete()
	{
		if (this.beforeDelete() == false)
		{
			return 0;
		}

		int i = 0;
		try
		{
			i = EntityDBAccess.Delete(this);
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteInfo(ex.getMessage());
			throw ex;
		}

		//更新缓存.  @wangyanyan
		if (this.getEnMap().getDepositaryOfEntity() == Depositary.Application)
		{
			Cash2019.DeleteRow(this.toString(), this.getPKVal().toString());
		}

		this.afterDelete();
		return i;
	}
	/** 
	 直接删除指定的
	 
	 @param pk
	*/
	public final int Delete(Object pk)
	{
		Paras ps = new Paras();
		ps.Add(this.getPK(), pk);
		switch (this.getEnMap().getEnDBUrl().getDBType())
		{
			case Oracle:
			case MSSQL:
			case MySQL:
				return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getPK() + " =" + this.getHisDBVarStr() + pk);
			default:
				throw new RuntimeException("没有涉及到的类型。");
		}
	}
	/** 
	 删除指定的数据
	 
	 @param attr
	 @param val
	*/
	public final int Delete(String attr, Object val)
	{
		Paras ps = new Paras();
		ps.Add(attr, val);
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			ps.Add(attr, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr, val));
		}
		else
		{
			ps.Add(attr, val);
		}

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetAttrByKey(attr).getField() + " =" + this.getHisDBVarStr() + attr, ps);
	}
	public final int Delete(String attr1, Object val1, String attr2, Object val2)
	{
		Paras ps = new Paras();

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			ps.Add(attr1, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr1, val1));
			ps.Add(attr2, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr2, val2));

		}
		else
		{
			ps.Add(attr1, val1);
			ps.Add(attr2, val2);
		}

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetAttrByKey(attr1).getField() + " =" + this.getHisDBVarStr() + attr1 + " AND " + this.getEnMap().GetAttrByKey(attr2).getField() + " =" + this.getHisDBVarStr() + attr2, ps);
	}
	public final int Delete(String attr1, Object val1, String attr2, Object val2, String attr3, Object val3)
	{
		Paras ps = new Paras();

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			ps.Add(attr1, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr1, val1));
			ps.Add(attr2, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr2, val2));
			ps.Add(attr3, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr3, val3));
		}
		else
		{
			ps.Add(attr1, val1);
			ps.Add(attr2, val2);
			ps.Add(attr3, val3);
		}

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetAttrByKey(attr1).getField() + " =" + this.getHisDBVarStr() + attr1 + " AND " + this.getEnMap().GetAttrByKey(attr2).getField() + " =" + this.getHisDBVarStr() + attr2 + " AND " + this.getEnMap().GetAttrByKey(attr3).getField() + " =" + this.getHisDBVarStr() + attr3, ps);
	}
	public final int Delete(String attr1, Object val1, String attr2, Object val2, String attr3, Object val3, String attr4, Object val4)
	{
		Paras ps = new Paras();

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			ps.Add(attr1, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr1, val1));
			ps.Add(attr2, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr2, val2));
			ps.Add(attr3, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr3, val3));
			ps.Add(attr4, BP.Sys.Glo.GenerRealType(this.getEnMap().getAttrs(), attr4, val4));

		}
		else
		{
			ps.Add(attr1, val1);
			ps.Add(attr2, val2);
			ps.Add(attr3, val3);
			ps.Add(attr4, val4);
		}

		return DBAccess.RunSQL("DELETE FROM " + this.getEnMap().getPhysicsTable() + " WHERE " + this.getEnMap().GetAttrByKey(attr1).getField() + " =" + this.getHisDBVarStr() + attr1 + " AND " + this.getEnMap().GetAttrByKey(attr2).getField() + " =" + this.getHisDBVarStr() + attr2 + " AND " + this.getEnMap().GetAttrByKey(attr3).getField() + " =" + this.getHisDBVarStr() + attr3 + " AND " + this.getEnMap().GetAttrByKey(attr4).getField() + " =" + this.getHisDBVarStr() + attr4, ps);
	}
	protected void afterDelete()
	{
		if (this.getEnMap().getDepositaryOfEntity() != Depositary.Application)
		{
			return;
		}
		/**删除缓存。
		*/
		BP.DA.CashEntity.Delete(this.toString(), this.getPKVal().toString());
		return;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 参数字段
	public final AtPara getatPara()
	{
		Object tempVar = this.getRow().GetValByKey("_ATObj_");
		AtPara at = tempVar instanceof AtPara ? (AtPara)tempVar : null;
		if (at != null)
		{
			return at;
		}
		try
		{
			String atParaStr = this.GetValStringByKey("AtPara");
			if (DataType.IsNullOrEmpty(atParaStr))
			{
					/*没有发现数据，就执行初始化.*/
				this.InitParaFields();

					// 重新获取一次。
				atParaStr = this.GetValStringByKey("AtPara");
				if (DataType.IsNullOrEmpty(atParaStr))
				{
					atParaStr = "";
				}

				at = new AtPara(atParaStr);
				this.SetValByKey("_ATObj_", at);
				return at;
			}
			at = new AtPara(atParaStr);
			this.SetValByKey("_ATObj_", at);
			return at;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取参数AtPara时出现异常" + ex.getMessage() + "，可能是您没有加入约定的参数字段AtPara. " + ex.getMessage());
		}
	}
	/** 
	 初始化参数字段(需要子类重写)
	 
	 @return 
	*/
	protected void InitParaFields()
	{
	}
	/** 
	 获取参数
	 
	 @param key
	 @return 
	*/
	public final String GetParaString(String key)
	{
		return getatPara().GetValStrByKey(key);
	}
	/** 
	 获取参数
	 
	 @param key
	 @param isNullAsVal
	 @return 
	*/
	public final String GetParaString(String key, String isNullAsVal)
	{
		String str = getatPara().GetValStrByKey(key);
		if (DataType.IsNullOrEmpty(str))
		{
			return isNullAsVal;
		}
		return str;
	}
	/** 
	 获取参数Init值
	 
	 @param key
	 @return 
	*/

	public final int GetParaInt(String key)
	{
		return GetParaInt(key, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public int GetParaInt(string key, int isNullAsVal = 0)
	public final int GetParaInt(String key, int isNullAsVal)
	{
		return getatPara().GetValIntByKey(key, isNullAsVal);
	}

	public final float GetParaFloat(String key)
	{
		return GetParaFloat(key, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public float GetParaFloat(string key, float isNullAsVal = 0)
	public final float GetParaFloat(String key, float isNullAsVal)
	{
		return getatPara().GetValFloatByKey(key, isNullAsVal);
	}
	/** 
	 获取参数boolen值
	 
	 @param key
	 @return 
	*/
	public final boolean GetParaBoolen(String key)
	{
		return getatPara().GetValBoolenByKey(key);
	}
	/** 
	 获取参数boolen值
	 
	 @param key
	 @param IsNullAsVal
	 @return 
	*/
	public final boolean GetParaBoolen(String key, boolean IsNullAsVal)
	{
		return getatPara().GetValBoolenByKey(key, IsNullAsVal);
	}
	/** 
	 设置参数
	 
	 @param key
	 @param obj
	*/
	public final void SetPara(String key, String obj)
	{
		if (getatPara() != null)
		{
			this.getRow().remove("_ATObj_");
		}

		String atParaStr = this.GetValStringByKey("AtPara");
		if (atParaStr.contains("@" + key + "=") == false)
		{
			atParaStr += "@" + key + "=" + obj;
			this.SetValByKey("AtPara", atParaStr);
		}
		else
		{
			AtPara at = new AtPara(atParaStr);
			at.SetVal(key, obj);
			this.SetValByKey("AtPara", at.GenerAtParaStrs());
		}
	}
	public final void SetPara(String key, int obj)
	{
		SetPara(key, String.valueOf(obj));
	}
	public final void SetPara(String key, float obj)
	{
		SetPara(key, String.valueOf(obj));
	}
	public final void SetPara(String key, boolean obj)
	{
		if (obj == false)
		{
			SetPara(key, "0");
		}
		else
		{
			SetPara(key, "1");
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 通用方法
	/** 
	 获取实体
	 
	 @param key
	*/
	public final Object GetRefObject(String key)
	{
		return this.getRow().get("_" + key);
		//object obj = this.Row[key];
		//if (obj == null)
		//{
		//    if (this.Row.ContainsKey(key) == false)
		//        return null;
		//    obj = this.Row[key];
		//}
		//return obj;
	}
	/** 
	 设置实体
	 
	 @param key
	 @param obj
	*/
	public final void SetRefObject(String key, Object obj)
	{
		if (obj == null)
		{
			return;
		}

		this.getRow().SetValByKey("_" + key, obj);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region insert
	/** 
	 在插入之前要做的工作。
	 
	 @return 
	*/
	protected boolean beforeInsert()
	{
		return true;
	}
	protected boolean roll()
	{
		return true;
	}
	public void InsertWithOutPara()
	{
		this.RunSQL(SqlBuilder.Insert(this));
	}
	/** 
	 Insert .
	*/
	public int Insert()
	{
		if (this.beforeInsert() == false)
		{
			return 0;
		}

		if (this.beforeUpdateInsertAction() == false)
		{
			return 0;
		}

		int i = 0;
		try
		{
			i = this.DirectInsert();
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}

		// 开始更新内存数据。 @wangyanyan
		if (this.getEnMap().getDepositaryOfEntity() == Depositary.Application)
		{
			Cash2019.PutRow(this.toString(), this.getPKVal().toString(), this.getRow());
		}

		this.afterInsert();
		this.afterInsertUpdateAction();

		return i;
	}
	protected void afterInsert()
	{
		//added by liuxc,2016-02-19,新建时，新增一个版本记录
		if (this.getEnMap().IsEnableVer)
		{
			//增加版本为1的版本历史记录
			String enName = this.toString();
			String rdt = BP.DA.DataType.getCurrentDataTime();

			//edited by liuxc,2017-03-24,增加判断，如果相同主键的数据曾被删除掉，再次被增加时，会延续被删除时的版本，原有逻辑报错
			EnVer ver = new EnVer();
			ver.setMyPK(enName + "_" + this.getPKVal());

			if (ver.RetrieveFromDBSources() == 0)
			{
				ver.setNo(enName);
				ver.setPKValue(this.getPKVal().toString());
				ver.setName(this.getEnMap().getEnDesc());
			}
			else
			{
				ver.setEVer(ver.getEVer() + 1);
			}

			ver.setRDT(rdt);
			ver.setRec(BP.Web.WebUser.getName());
			ver.Save();

			// 保存字段数据.
			Attrs attrs = this.getEnMap().getAttrs();
			for (Attr attr : attrs)
			{
				if (attr.getIsRefAttr())
				{
					continue;
				}

				EnVerDtl dtl = new EnVerDtl();
				dtl.setEnVerPK(ver.getMyPK());
				dtl.setEnVer(ver.getEVer());
				dtl.setEnName(ver.getNo());
				dtl.setAttrKey(attr.getKey());
				dtl.setAttrName(attr.getDesc());
				//dtl.OldVal = this.GetValStrByKey(attr.Key);   //第一个版本时，旧值没有
				dtl.setRDT(rdt);
				dtl.setRec(BP.Web.WebUser.getName());
				dtl.setNewVal(this.GetValStrByKey(attr.getKey()));
				dtl.setMyPK(ver.getMyPK() + "_" + attr.getKey() + "_" + dtl.getEnVer());
				dtl.Insert();
			}
		}

		return;
	}
	/** 
	 在更新与插入之后要做的工作.
	*/
	protected void afterInsertUpdateAction()
	{
		if (this.getEnMap().getHisFKEnumAttrs().Count > 0)
		{
			this.RetrieveFromDBSources();
		}

		if (this.getEnMap().IsAddRefName)
		{
			this.ReSetNameAttrVal();
			this.DirectUpdate();
		}
		return;
	}
	/** 
	 从一个副本上copy.
	 用于两个数性基本相近的 实体 copy. 
	 
	 @param fromEn
	*/
	public void Copy(Entity fromEn)
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning zhoupeng 打开如下注释代码？没有考虑到为什么要改变PK.
			//if (attr.IsPK)
			//    continue;   //不能在打开，如果打开，就会与其他的约定出错，copy就是全部的属性，然后自己。

			Object obj = fromEn.GetValByKey(attr.getKey());
			if (obj == null)
			{
				continue;
			}

			this.SetValByKey(attr.getKey(), obj);
		}
	}
	/** 
	 从一个副本上
	 
	 @param fromRow
	*/
	public void Copy(Row fromRow)
	{
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			try
			{
				this.SetValByKey(attr.getKey(), fromRow.GetValByKey(attr.getKey()));
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	public void Copy(XmlEn xmlen)
	{
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			Object obj = null;
			try
			{
				obj = xmlen.GetValByKey(attr.getKey());
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			if (obj == null || obj.toString().equals(""))
			{
				continue;
			}
			this.SetValByKey(attr.getKey(), xmlen.GetValByKey(attr.getKey()));
		}
	}
	/** 
	 复制 Hashtable
	 
	 @param ht
	*/
	public void Copy(Hashtable ht)
	{
		for (String k : ht.keySet())
		{
			Object obj = null;
			try
			{
				obj = ht.get(k);
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			if (obj == null || obj.toString().equals(""))
			{
				continue;
			}
			this.SetValByKey(k, obj);
		}
	}
	public void Copy(DataRow dr)
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
			try
			{
				this.SetValByKey(attr.getKey(), dr.get(attr.getKey()));
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	public final String Copy(String refDoc)
	{
		for (Attr attr : this.get_enMap().getAttrs())
		{
			refDoc = refDoc.replace("@" + attr.getKey(), this.GetValStrByKey(attr.getKey()));
		}
		return refDoc;
	}


	public final void Copy()
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getIsPK() == false)
			{
				continue;
			}

			if (attr.getMyDataType() == DataType.AppInt)
			{
				this.SetValByKey(attr.getKey(), 0);
			}
			else
			{
				this.SetValByKey(attr.getKey(), "");
			}
		}

		try
		{
			this.SetValByKey("No", "");
		}
		catch (java.lang.Exception e)
		{
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region verify
	/** 
	 校验数据
	 
	 @return 
	*/
	public final boolean verifyData()
	{
		return true;

		String str = "";
		Attrs attrs = this.getEnMap().getAttrs();
		String s;
		for (Attr attr : attrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getMyDataType() == DataType.AppString && attr.getMinLength() > 0)
			{
				if (attr.getUIIsReadonly())
				{
					continue;
				}

				s = this.GetValStrByKey(attr.getKey());
				// 处理特殊字符.
				s = s.replace("'", "~");
				s = s.replace("\"", "”");
				s = s.replace(">", "》");
				s = s.replace("<", "《");
				this.SetValByKey(attr.getKey(), s);

				if (s.length() < attr.getMinLength() || s.length() > attr.getMaxLength())
				{
					if (attr.getKey().equals("No") && attr.getUIIsReadonly())
					{
						if (this.GetValStringByKey(attr.getKey()).trim().length() == 0 || this.GetValStringByKey(attr.getKey()).equals("自动生成"))
						{
							this.SetValByKey("No", this.GenerNewNoByKey("No"));
						}
					}
					else
					{
						str += "@[" + attr.getKey() + "," + attr.getDesc() + "]输入错误，请输入 " + attr.getMinLength() + " ～ " + attr.getMaxLength() + " 个字符范围，当前为空。";
					}
				}
			}

			//else if (attr.MyDataType == DataType.AppDateTime)
			//{
			//    if (this.GetValStringByKey(attr.Key).Trim().Length != 16)
			//    {
			//        //str+="@["+ attr.Desc +"]输入日期时间格式错误，输入的字段值["+this.GetValStringByKey(attr.Key)+"]不符合系统格式"+BP.DA.DataType.SysDataTimeFormat+"要求。";
			//    }
			//}
			//else if (attr.MyDataType == DataType.AppDate)
			//{
			//    if (this.GetValStringByKey(attr.Key).Trim().Length != 10)
			//    {
			//        //str+="@["+ attr.Desc +"]输入日期格式错误，输入的字段值["+this.GetValStringByKey(attr.Key)+"]不符合系统格式"+BP.DA.DataType.SysDataFormat+"要求。";
			//    }
			//}
		}

		if (str.equals(""))
		{
			return true;
		}



		// throw new Exception("@在保存[" + this.EnDesc + "],PK[" + this.PK + "=" + this.PKVal + "]时出现信息录入不整错误：" + str);

		if (SystemConfig.getIsDebug())
		{
			throw new RuntimeException("@在保存[" + this.getEnDesc() + "],主键[" + this.getPK() + "=" + this.getPKVal() + "]时出现信息录入不整错误：" + str);
		}
		else
		{
			throw new RuntimeException("@在保存[" + this.getEnDesc() + "][" + this.getEnMap().GetAttrByKey(this.getPK()).getDesc() + "=" + this.getPKVal() + "]时错误：" + str);
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 更新，插入之前的工作。
	protected boolean beforeUpdateInsertAction()
	{
		switch (this.getEnMap().getEnType())
		{
			case View:
			case XML:
			case Ext:
				return false;
			default:
				break;
		}

		this.verifyData();
		return true;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 更新，插入之前的工作。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 更新操作
	/** 
	 更新
	*/

	/** @return 
	*/
	public int Update()
	{
		return this.Update(null);
	}
	/** 
	 仅仅更新一个属性
	 
	 @param key1 key1
	 @param val1 val1
	 @return 更新的个数
	*/
	public final int Update(String key1, Object val1)
	{
		this.SetValByKey(key1, val1);

		String sql = "";

		if (val1.getClass() == Integer.class || val1.getClass() == Float.class || val1.getClass() == Long.class || val1.getClass() == BigDecimal.class)
		{
			sql = "UPDATE " + this.getEnMap().getPhysicsTable() + " SET " + key1 + " =" + val1 + " WHERE " + this.getPK() + "='" + this.getPKVal() + "'";
		}
		if (val1.getClass() == String.class)
		{
			sql = "UPDATE " + this.getEnMap().getPhysicsTable() + " SET " + key1 + " ='" + val1 + "' WHERE " + this.getPK() + "='" + this.getPKVal() + "'";
		}

		return this.RunSQL(sql);
	}
	public final int Update(String key1, Object val1, String key2, Object val2)
	{
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);

		key1 = key1 + "," + key2;
		return this.Update(key1.split("[,]", -1));
	}
	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3)
	{
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);

		key1 = key1 + "," + key2 + "," + key3;
		return this.Update(key1.split("[,]", -1));
	}
	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4, Object val4)
	{
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		key1 = key1 + "," + key2 + "," + key3 + "," + key4;
		return this.Update(key1.split("[,]", -1));
	}
	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4, Object val4, String key5, Object val5)
	{
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		this.SetValByKey(key5, val5);

		key1 = key1 + "," + key2 + "," + key3 + "," + key4 + "," + key5;
		return this.Update(key1.split("[,]", -1));
	}
	public final int Update(String key1, Object val1, String key2, Object val2, String key3, Object val3, String key4, Object val4, String key5, Object val5, String key6, Object val6)
	{
		this.SetValByKey(key1, val1);
		this.SetValByKey(key2, val2);
		this.SetValByKey(key3, val3);
		this.SetValByKey(key4, val4);
		this.SetValByKey(key5, val5);
		this.SetValByKey(key6, val6);
		key1 = key1 + "," + key2 + "," + key3 + "," + key4 + "," + key5 + "," + key6;
		return this.Update(key1.split("[,]", -1));
	}
	protected boolean beforeUpdate()
	{
		return true;
	}
	/** 
	 更新实体
	*/
	public final int Update(String[] keys)
	{
		String str = "";
		try
		{
			str = "@更新之前出现错误 ";
			if (this.beforeUpdate() == false)
			{
				return 0;
			}

			str = "@更新插入之前出现错误";
			if (this.beforeUpdateInsertAction() == false)
			{
				return 0;
			}

			str = "@更新时出现错误";
			int i = EntityDBAccess.Update(this, keys);
			str = "@更新之后出现错误";

			// 开始更新内存数据。
			switch (this.getEnMap().getDepositaryOfEntity())
			{
				case Application:
					//this.DeleteFromCash();
					CashEntity.Update(this.toString(), this.getPKVal().toString(), this);
					break;
				case None:
					break;
			}

			//更新缓存. @wangyanyan
			if (this.getEnMap().getDepositaryOfEntity() == Depositary.Application)
			{
				Cash2019.UpdateRow(this.toString(), this.getPKVal().toString(), this.getRow());
			}

			this.afterUpdate();
			str = "@更新插入之后出现错误";
			this.afterInsertUpdateAction();
			return i;
		}
		catch (RuntimeException ex)
		{
			if (ex.getMessage().Contains("列名") || ex.getMessage().Contains("将截断字符串") || ex.getMessage().Contains("缺少") || ex.getMessage().Contains("的值太大"))
			{
				/*说明字符串长度有问题.*/
				this.CheckPhysicsTable();

				/*比较参数那个字段长度有问题*/
				String errs = "";
				for (Attr attr : this.getEnMap().getAttrs())
				{
					if (attr.getMyDataType() != BP.DA.DataType.AppString)
					{
						continue;
					}

					if (attr.getMaxLength() < this.GetValStrByKey(attr.getKey()).length())
					{
						errs += "@映射里面的" + attr.getKey() + "," + attr.getDesc() + ", 相对于输入的数据:{" + this.GetValStrByKey(attr.getKey()) + "}, 太长。";
					}
				}

				if (!errs.equals(""))
				{
					throw new RuntimeException("@执行更新[" + this.toString() + "]出现错误@错误字段:" + errs + " <br>清你在提交一次。" + ex.getMessage());
				}
				else
				{
					throw ex;
				}
			}


			Log.DefaultLogWriteLine(LogType.Error, ex.getMessage());
			if (SystemConfig.getIsDebug())
			{
				throw new RuntimeException("@[" + this.getEnDesc() + "]更新期间出现错误:" + str + ex.getMessage());
			}
			else
			{
				throw ex;
			}
		}
	}
	private int UpdateOfDebug(String[] keys)
	{
		String str = "";
		try
		{
			str = "@在更新之前出现错误";
			if (this.beforeUpdate() == false)
			{
				return 0;
			}
			str = "@在beforeUpdateInsertAction出现错误";
			if (this.beforeUpdateInsertAction() == false)
			{
				return 0;
			}
			int i = EntityDBAccess.Update(this, keys);
			str = "@在afterUpdate出现错误";
			this.afterUpdate();
			str = "@在afterInsertUpdateAction出现错误";
			this.afterInsertUpdateAction();
			//this.UpdateMemory();
			return i;
		}
		catch (RuntimeException ex)
		{
			String msg = "@[" + this.getEnDesc() + "]UpdateOfDebug更新期间出现错误:" + str + ex.getMessage();
			Log.DefaultLogWriteLine(LogType.Error, msg);

			if (SystemConfig.getIsDebug())
			{
				throw new RuntimeException(msg);
			}
			else
			{
				throw ex;
			}
		}
	}

	protected void afterUpdate()
	{
		if (this.getEnMap().IsEnableVer)
		{
			/*处理版本号管理.*/
			// 取出来原来最后的版本数据.

			String enName = this.toString();
			String rdt = BP.DA.DataType.getCurrentDataTime();

			EnVer ver = new EnVer();
			ver.Retrieve(EnVerAttr.MyPK, enName + "_" + this.getPKVal());

			EnVerDtl dtl = null;
			EnVerDtl dtlTemp = null;

			if (ver.RetrieveFromDBSources() == 0)
			{
				/*初始化数据.*/
				ver.setPKValue(this.getPKVal().toString());
				ver.setNo(enName);
				ver.setRDT(rdt);
				ver.setName(this.getEnMap().getEnDesc());
				ver.setRec(BP.Web.WebUser.getName());
				ver.Insert();

				for (Attr attr : this.getEnMap().getAttrs())
				{
					if (attr.getIsRefAttr())
					{
						continue;
					}

					dtl = new EnVerDtl();
					dtl.setEnVerPK(ver.getMyPK());
					dtl.setEnVer(ver.getEVer());
					dtl.setEnName(ver.getNo());
					dtl.setAttrKey(attr.getKey());
					dtl.setAttrName(attr.getDesc());

					dtl.setRDT(rdt);
					dtl.setRec(BP.Web.WebUser.getName());
					dtl.setNewVal(this.GetValStrByKey(attr.getKey()));

					dtl.setMyPK(ver.getMyPK() + "_" + attr.getKey() + "_" + dtl.getEnVer());
					dtl.Insert();
				}
				return;
			}

			//更新主版本信息.
			ver.setEVer(ver.getEVer() + 1);
			ver.setRec(BP.Web.WebUser.getNo());
			ver.setRDT(rdt);
			ver.Update();

			//获取上一版本的数据
			EnVerDtls dtls = new EnVerDtls(ver.getMyPK(), ver.getEVer() - 1);

			// 保存字段数据.
			Attrs attrs = this.getEnMap().getAttrs();
			for (Attr attr : attrs)
			{
				if (attr.getIsRefAttr())
				{
					continue;
				}

				dtl = new BP.Sys.EnVerDtl();
				dtl.setEnVerPK(ver.getMyPK());
				dtl.setEnVer(ver.getEVer());
				dtl.setEnName(ver.getNo());
				dtl.setAttrKey(attr.getKey());
				dtl.setAttrName(attr.getDesc());

				BP.En.Entity tempVar = dtls.GetEntityByKey(EnVerDtlAttr.AttrKey, attr.getKey());
				dtlTemp = tempVar instanceof EnVerDtl ? (EnVerDtl)tempVar : null;

				if (dtlTemp != null)
				{
					dtl.setOldVal(dtlTemp.getNewVal());
				}
				else
				{
					dtl.setOldVal(this.GetValStrByKey(attr.getKey()));
				}

				dtl.setRDT(rdt);
				dtl.setRec(BP.Web.WebUser.getName());
				dtl.setNewVal(this.GetValStrByKey(attr.getKey()));

				dtl.setMyPK(ver.getMyPK() + "_" + attr.getKey() + "_" + dtl.getEnVer());
				dtl.Insert();
			}
		}
		return;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 对文件的处理.
	public final void SaveBigTxtToDB(String saveToField, String bigTxt)
	{
		bigTxt = "$" + bigTxt;

		try
		{
			String temp = BP.Sys.SystemConfig.getPathOfTemp() + "\\" + this.getEnMap().getPhysicsTable() + this.getPKVal() + ".tmp";
			DataType.WriteFile(temp, bigTxt);

			//写入数据库.
			SaveFileToDB(saveToField, temp);
		}
		catch (RuntimeException ex)
		{
			String err = "err@在保存大字段文本出现错误，类:" + this.toString() + " 属性:" + saveToField + ".";
			err += "@有可能是您的目录权限不够，请设置dataUser目录的访问权限,系统错误@" + ex.getMessage();
			throw new RuntimeException(err);
		}
	}
	/** 
	 保存文件到数据库
	 
	 @param saveToField 要保存的字段
	 @param bytes 文件流
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public void SaveFileToDB(string saveToField, byte[] bytesOfFile)
	public final void SaveFileToDB(String saveToField, byte[] bytesOfFile)
	{
		try
		{
			BP.DA.DBAccess.SaveBytesToDB(bytesOfFile, this.getEnMap().getPhysicsTable(), this.getPK(), this.getPKVal().toString(), saveToField);
		}
		catch (RuntimeException ex)
		{
			/* 为了防止任何可能出现的数据丢失问题，您应该先仔细检查此脚本，然后再在数据库设计器的上下文之外运行此脚本。*/
			String sql = "";
			if (BP.DA.DBAccess.getAppCenterDBType() == DBType.MSSQL)
			{
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Image NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType() == DBType.Oracle)
			{
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Image NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType() == DBType.MySQL)
			{
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Image NULL ";
			}

			BP.DA.DBAccess.RunSQL(sql);

			throw new RuntimeException("@保存文件期间出现错误，有可能该字段没有被自动创建，现在已经执行创建修复数据表，请重新执行一次." + ex.getMessage());
		}
	}

	/** 
	 保存文件到数据库
	 
	 @param saveToField 要保存的字段
	 @param fileFullName 文件路径
	*/
	public final void SaveFileToDB(String saveToField, String fileFullName)
	{
		try
		{
			BP.DA.DBAccess.SaveFileToDB(fileFullName, this.getEnMap().getPhysicsTable(), this.getPK(), this.getPKVal().toString(), saveToField);
		}
		catch (RuntimeException ex)
		{
			/* 为了防止任何可能出现的数据丢失问题，您应该先仔细检查此脚本，然后再在数据库设计器的上下文之外运行此脚本。*/
			String sql = "";
			if (BP.DA.DBAccess.getAppCenterDBType() == DBType.MSSQL)
			{
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Image NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType() == DBType.Oracle)
			{
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " Blob NULL ";
			}

			if (BP.DA.DBAccess.getAppCenterDBType() == DBType.MySQL)
			{
				sql = "ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + saveToField + " MediumBlob NULL ";
			}

			BP.DA.DBAccess.RunSQL(sql);

			throw new RuntimeException("@保存文件期间出现错误，有可能该字段没有被自动创建，现在已经执行创建修复数据表，请重新执行一次." + ex.getMessage());
		}
	}
	/** 
	 从表的字段里读取文件
	 
	 @param saveToField 字段
	 @param filefullName 文件路径,如果为空怎不保存直接返回文件流，如果不为空则创建文件。
	 @return 返回文件流
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public byte[] GetFileFromDB(string saveToField, string filefullName)
	public final byte[] GetFileFromDB(String saveToField, String filefullName)
	{
		return BP.DA.DBAccess.GetByteFromDB(this.getEnMap().getPhysicsTable(), this.getPK(), this.getPKVal().toString(), saveToField);
	}
	/** 
	 从表的字段里读取string
	 
	 @param imgFieldName 字段名
	 @return 大文本数据
	*/
	public final String GetBigTextFromDB(String imgFieldName)
	{
		return BP.DA.DBAccess.GetBigTextFromDB(this.getEnMap().getPhysicsTable(), this.getPK(), this.getPKVal().toString(), imgFieldName);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 对文件的处理.

	/** 
	 执行保存
	 
	 @return 
	*/
	public int Save()
	{
		switch (this.getPK())
		{
			case "OID":
				if (this.GetValIntByKey("OID") == 0)
				{
					//this.SetValByKey("OID",EnDA.GenerOID());
					this.Insert();
					return 1;
				}
				else
				{
					this.Update();
					return 1;
				}
				break;
			case "MyPK":
			case "No":
			case "ID":
				//自动生成的MYPK，插入前获取主键
				this.beforeUpdateInsertAction();
				String pk = this.GetValStrByKey(this.getPK());

				if (pk.equals("") || pk == null)
				{
					this.Insert();
					return 1;
				}
				else
				{
					int i = this.Update();
					if (i == 0)
					{
						this.Insert();
						i = 1;
					}
					return i;
				}
				break;
			default:
				if (this.Update() == 0)
				{
					this.Insert();
				}
				return 1;
				break;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于数据库的处理
	/** 
	 检查是否是日期
	*/
	protected final void CheckDateAttr()
	{
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() == DataType.AppDate || attr.getMyDataType() == DataType.AppDateTime)
			{
				LocalDateTime dt = this.GetValDateTime(attr.getKey());
			}
		}
	}
	/** 
	 创建物理表
	*/
	protected final void CreatePhysicsTable()
	{
		if (this.get_enMap().getEnDBUrl().getDBUrlType() == DBUrlType.AppCenterDSN)
		{
			switch (DBAccess.getAppCenterDBType())
			{
				case Oracle:
					DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfOra(this));
					break;
				case Informix:
					DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfInfoMix(this));
					break;
				case PostgreSQL:
					DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfPostgreSQL(this));
					break;
				case MSSQL:
					DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfMS(this));
					break;
				case MySQL:
					DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfMySQL(this));
					break;
				case Access:
					DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOf_OLE(this));
					break;
				default:
					throw new RuntimeException("@未判断的数据库类型。");
			}
			this.CreateIndexAndPK();
			return;
		}

		//if (this._enMap.EnDBUrl.DBUrlType == DBUrlType.DBAccessOfMSSQL1)
		//{
		//    DBAccessOfMSSQL1.RunSQL(SqlBuilder.GenerCreateTableSQLOfMS(this));
		//    this.CreateIndexAndPK();
		//    return;
		//}

		//if (this._enMap.EnDBUrl.DBUrlType == DBUrlType.DBAccessOfMSSQL2)
		//{
		//    DBAccessOfMSSQL2.RunSQL(SqlBuilder.GenerCreateTableSQLOfMS(this));
		//    this.CreateIndexAndPK();
		//    return;
		//}

		//if (this._enMap.EnDBUrl.DBUrlType == DBUrlType.DBAccessOfOracle1)
		//{
		//    DBAccessOfOracle1.RunSQL(SqlBuilder.GenerCreateTableSQLOfOra(this));
		//    this.CreateIndexAndPK();
		//    return;
		//}
		//if (this._enMap.EnDBUrl.DBUrlType == DBUrlType.DBAccessOfOracle2)
		//{
		//    DBAccessOfOracle2.RunSQL(SqlBuilder.GenerCreateTableSQLOfOra(this));
		//    this.CreateIndexAndPK();
		//    return;
		//}

	}
	private void CreateIndexAndPK()
	{
		if (this.getEnMap().getEnDBUrl().getDBType() != DBType.Informix)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 建立索引

			int pkconut = this.getPKCount();
			if (pkconut == 1)
			{
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), this.getPKField());
			}
			else if (pkconut == 2)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1);
			}
			else if (pkconut == 3)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				String pk2 = this.getPKs()[2];
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2);
			}
			else if (pkconut == 4)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				String pk2 = this.getPKs()[2];
				String pk3 = this.getPKs()[3];
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2, pk3);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 建立主键
		if (DBAccess.IsExitsTabPK(this.getEnMap().getPhysicsTable()) == false)
		{
			int pkconut = this.getPKCount();
			if (pkconut == 1)
			{
				DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), this.getPKField(), this.getEnMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), this.getPKField());
			}
			else if (pkconut == 2)
			{

				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), pk0, pk1, this.getEnMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1);
			}
			else if (pkconut == 3)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				String pk2 = this.getPKs()[2];
				DBAccess.CreatePK(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2, this.getEnMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.getEnMap().getPhysicsTable(), pk0, pk1, pk2);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
	}
	/** 
	 如果一个属性是外键，并且它还有一个字段存储它的名称。
	 设置这个外键名称的属性。
	*/
	protected final void ReSetNameAttrVal()
	{
		Attrs attrs = this.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getIsFKorEnum() == false)
			{
				continue;
			}

			String s = this.GetValRefTextByKey(attr.getKey());
			this.SetValByKey(attr.getKey() + "Name", s);
		}
	}
	private void CheckPhysicsTable_SQL()
	{
		String table = this.get_enMap().getPhysicsTable();
		DBType dbtype = this.get_enMap().getEnDBUrl().getDBType();
		String sqlFields = "";
		String sqlYueShu = "";

		sqlFields = "SELECT column_name as FName,data_type as FType,CHARACTER_MAXIMUM_LENGTH as FLen from information_schema.columns where table_name='" + this.getEnMap().getPhysicsTable() + "'";
		sqlYueShu = "SELECT b.name, a.name FName from sysobjects b join syscolumns a on b.id = a.cdefault where a.id = object_id('" + this.getEnMap().getPhysicsTable() + "') ";

		DataTable dtAttr = DBAccess.RunSQLReturnTable(sqlFields);
		DataTable dtYueShu = DBAccess.RunSQLReturnTable(sqlYueShu);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 修复表字段。
		Attrs attrs = this.get_enMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getIsRefAttr() || attr.getIsPK())
			{
				continue;
			}

			String FType = "";
			String Flen = "";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 判断是否存在.
			boolean isHave = false;
			for (DataRow dr : dtAttr.Rows)
			{
				if (dr.get("FName").toString().toLowerCase().equals(attr.getField().toLowerCase()))
				{
					isHave = true;
					FType = dr.get("FType") instanceof String ? (String)dr.get("FType") : null;
					Flen = dr.get("FLen").toString();
					break;
				}
			}
			if (isHave == false)
			{
				/*不存在此列 , 就增加此列。*/
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
					case DataType.AppDate:
					case DataType.AppDateTime:
						int len = attr.getMaxLength();
						if (len == 0)
						{
							len = 200;
						}
						//throw new Exception("属性的最小长度不能为0。");
						if (len > 4000)
						{
							if (dbtype == DBType.Access && len >= 254)
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + "  Memo DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
							else
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " NVARCHAR(MAX) DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
						}
						else
						{
							if (dbtype == DBType.Access && len >= 254)
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + "  Memo DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
							else
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " NVARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
						}
						continue;
					case DataType.AppInt:
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					case DataType.AppBoolean:
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					case DataType.AppFloat:
					case DataType.AppMoney:
					case DataType.AppDouble:
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					default:
						throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查类型是否匹配.
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (FType.toLowerCase().contains("char"))
					{
						/*类型正确，检查长度*/
						if (attr.getIsPK())
						{
							continue;
						}

						if (Flen == null)
						{
							throw new RuntimeException("" + attr.getKey() + " -" + sqlFields);
						}
						int len = Integer.parseInt(Flen);


						if (len == -1)
						{
							continue; //有可能是 nvarchar(MAX)
						}

						if (len < attr.getMaxLength())
						{
							try
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ALTER column " + attr.getKey() + " VARCHAR(" + attr.getMaxLength() + ")");
							}
							catch (java.lang.Exception e)
							{
								/*如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
								for (DataRow dr : dtYueShu.Rows)
								{
									if (dr.get("FName").toString().toLowerCase().equals(attr.getKey().toLowerCase()))
									{
										DBAccess.RunSQL("ALTER TABLE " + table + " drop constraint " + dr.get(0).toString());
									}
								}

								// 在执行一遍.
								if (attr.getMaxLength() >= 4000)
								{
									DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ALTER column " + attr.getKey() + " VARCHAR(4000)");
								}
								else
								{
									DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ALTER column " + attr.getKey() + " VARCHAR(" + attr.getMaxLength() + ")");
								}
							}
						}
					}
					else
					{
						String err = "err@字段类型不匹配,表[" + this.getEnMap().getPhysicsTable() + "]字段[" + attr.getKey() + "]名称[" + attr.getDesc() + "]映射类型为[" + attr.getMyDataTypeStr() + "],数据类型为[" + FType + "]";
						BP.DA.Log.DebugWriteWarning(err);

						// throw new Exception();

						/***如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
						*/
						//foreach (DataRow dr in dtYueShu.Rows)
						//{
						//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
						//        DBAccess.RunSQL("ALTER TABLE " + table + " drop constraint " + dr[0].ToString());
						//}

						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " drop column " + attr.Field);
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " ADD " + attr.Field + " NVARCHAR(" + attr.MaxLength + ") DEFAULT '" + attr.DefaultVal + "' NULL");
						continue;
					}
					break;
				case DataType.AppInt:
				case DataType.AppBoolean:
					if (FType.contains("int") == false)
					{
						String err = "err@字段类型不匹配,表[" + this.getEnMap().getPhysicsTable() + "]字段[" + attr.getKey() + "]名称[" + attr.getDesc() + "]映射类型为[" + attr.getMyDataTypeStr() + "],数据类型为[" + FType + "]";
						BP.DA.Log.DebugWriteWarning(err);
						/***如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
						*/
						//foreach (DataRow dr in dtYueShu.Rows)
						//{
						//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
						//        DBAccess.RunSQL("alter table " + table + " drop constraint " + dr[0].ToString());
						//}
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " drop column " + attr.Field);
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " ADD " + attr.Field + " INT DEFAULT '" + attr.DefaultVal + "' NULL");
						//continue;
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case 9:
					if (FType.contains("float") == false)
					{
						String err = "err@字段类型不匹配,表[" + this.getEnMap().getPhysicsTable() + "]字段[" + attr.getKey() + "]名称[" + attr.getDesc() + "]映射类型为[" + attr.getMyDataTypeStr() + "],数据类型为[" + FType + "]";
						BP.DA.Log.DebugWriteWarning(err);

						/***如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
						*/
						//foreach (DataRow dr in dtYueShu.Rows)
						//{
						//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
						//        DBAccess.RunSQL("alter table " + table + " drop constraint " + dr[0].ToString());
						//}
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " drop column " + attr.Field);
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " ADD " + attr.Field + " FLOAT DEFAULT '" + attr.DefaultVal + "' NULL");
						//continue;
					}
					break;
				default:
					//  throw new Exception("error MyFieldType= " + attr.MyFieldType + " key=" + attr.Key);
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 修复表字段。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}

			if (attr.UITag == null)
			{
				continue;
			}

			try
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			}
			catch (java.lang.Exception e2)
			{
			}

			try
			{
				String[] strs = attr.UITag.split("[@]", -1);
				SysEnums ens = new SysEnums();
				ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
				for (String s : strs)
				{
					if (s.equals("") || s == null)
					{
						continue;
					}

					String[] vk = s.split("[=]", -1);
					SysEnum se = new SysEnum();
					se.setIntKey(Integer.parseInt(vk[0]));
					se.setLab(vk[1]);
					se.setEnumKey(attr.getUIBindKey());
					se.Insert();
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@自动增加枚举时出现错误，请确定您的格式是否正确。" + ex.getMessage() + "attr.UIBindKey=" + attr.getUIBindKey());
			}

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 建立主键
		if (DBAccess.IsExitsTabPK(this.get_enMap().getPhysicsTable()) == false)
		{
			int pkconut = this.getPKCount();
			if (pkconut == 1)
			{
				DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), this.getPKField(), this.get_enMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), this.getPKField());
			}
			else if (pkconut == 2)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), pk0, pk1, this.get_enMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1);
			}
			else if (pkconut == 3)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				String pk2 = this.getPKs()[2];
				DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2, this.get_enMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 重命名表名字段名.

		String ptable = this.getEnMap().getPhysicsTable();

		String sql = "exec sp_rename '" + this.getEnMap().getPhysicsTable() + "','" + this.getEnMap().getPhysicsTable() + "'";
		DBAccess.RunSQL(sql);

		for (Attr item : this.getEnMap().getAttrs())
		{
			if (item.getIsRefAttr() == true)
			{
				continue;
			}

			sql = "exec sp_rename '" + ptable + ".[" + item.getKey() + "]','" + item.getKey() + "','column';";
			DBAccess.RunSQL(sql);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 重命名表名字段名.

	}
	/** 
	 PostgreSQL 检查.
	*/
	private void CheckPhysicsTable_PostgreSQL()
	{
		String table = this.get_enMap().getPhysicsTable();
		DBType dbtype = this.get_enMap().getEnDBUrl().getDBType();
		String sqlFields = "";
		String sqlYueShu = "";

		//字段信息: 名称fname, 类型ftype, 长度flen.
		sqlFields = "SELECT a.attname as fname, format_type(a.atttypid,a.atttypmod) as type,  0 as FLen, 'xxxxxxx' as FType,";
		sqlFields += " a.attnotnull as notnull FROM pg_class as c,pg_attribute as a  ";
		sqlFields += " where c.relname = '" + this.getEnMap().getPhysicsTable().toLowerCase() + "' and a.attrelid = c.oid and a.attnum>0  ";

		//约束信息.
		//sqlYueShu = "SELECT b.name, a.name FName from sysobjects b join syscolumns a on b.id = a.cdefault where a.id = object_id('" + this.EnMap.PhysicsTable + "') ";

		DataTable dtAttr = DBAccess.RunSQLReturnTable(sqlFields);

		for (DataRow dr : dtAttr.Rows)
		{
			String type = dr.get("type").toString();
			if (type.contains("char"))
			{
				dr.set("ftype", "char");

				int start = type.indexOf('(') + 1;
				int end = type.indexOf(')');
				String len = type.substring(start, end);
				dr.set("flen", Integer.parseInt(len));
			}
			else
			{
				dr.set("ftype", type);
			}

		}
		//DataTable dtYueShu = DBAccess.RunSQLReturnTable(sqlYueShu);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 修复表字段。
		Attrs attrs = this.get_enMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getIsRefAttr() || attr.getIsPK())
			{
				continue;
			}

			String FType = "";
			String Flen = "";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 判断是否存在.
			boolean isHave = false;
			for (DataRow dr : dtAttr.Rows)
			{
				if (dr.get("FName").toString().toLowerCase().equals(attr.getField().toLowerCase()))
				{
					isHave = true;
					FType = dr.get("FType") instanceof String ? (String)dr.get("FType") : null;
					Flen = dr.get("FLen").toString();
					break;
				}
			}
			if (isHave == false)
			{
				/*不存在此列 , 就增加此列。*/
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
					case DataType.AppDate:
					case DataType.AppDateTime:
						int len = attr.getMaxLength();
						if (len == 0)
						{
							len = 200;
						}
						//throw new Exception("属性的最小长度不能为0。");
						if (len > 4000)
						{
							if (dbtype == DBType.Access && len >= 254)
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + "  Memo DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
							else
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
						}
						else
						{
							if (dbtype == DBType.Access && len >= 254)
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + "  Memo DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
							else
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
							}
						}
						continue;
					case DataType.AppInt:
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					case DataType.AppBoolean:
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					case DataType.AppFloat:
					case DataType.AppMoney:
					case DataType.AppDouble:
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					default:
						throw new RuntimeException("err@MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查类型是否匹配.
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					if (FType.toLowerCase().contains("char"))
					{
						/*类型正确，检查长度*/
						if (attr.getIsPK())
						{
							continue;
						}

						if (Flen == null)
						{
							throw new RuntimeException("" + attr.getKey() + " -" + sqlFields);
						}
						int len = Integer.parseInt(Flen);


						if (len == -1)
						{
							continue; //有可能是 nvarchar(MAX)
						}

						if (len < attr.getMaxLength())
						{
							try
							{
								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ALTER column " + attr.getKey() + " type character varying(" + attr.getMaxLength() + ")");
							}
							catch (java.lang.Exception e)
							{
								/*如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
								//foreach (DataRow dr in dtYueShu.Rows)
								//{
								//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
								//        DBAccess.RunSQL("ALTER TABLE " + table + " drop constraint " + dr[0].ToString());
								//}

								DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ALTER column " + attr.getKey() + " type character varying(" + attr.getMaxLength() + ")");
							}
						}
					}
					else if (FType.toLowerCase().contains("text"))
					{

					}
					else
					{
						String err = "err@字段类型不匹配,表[" + this.getEnMap().getPhysicsTable() + "]字段[" + attr.getKey() + "]名称[" + attr.getDesc() + "]映射类型为[" + attr.getMyDataTypeStr() + "],数据类型为[" + FType + "]";
						BP.DA.Log.DebugWriteWarning(err);

						// throw new Exception();

						/***如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
						*/
						//foreach (DataRow dr in dtYueShu.Rows)
						//{
						//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
						//        DBAccess.RunSQL("ALTER TABLE " + table + " drop constraint " + dr[0].ToString());
						//}

						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " drop column " + attr.Field);
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " VARCHAR(" + attr.getMaxLength() + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
						continue;
					}
					break;
				case DataType.AppInt:
				case DataType.AppBoolean:
					if (FType.contains("int") == false)
					{
						//  string err = "err@字段类型不匹配,表[" + this.EnMap.PhysicsTable + "]字段[" + attr.Key + "]名称[" + attr.Desc + "]映射类型为[" + attr.MyDataTypeStr + "],数据类型为[" + FType + "]";
						//    BP.DA.Log.DebugWriteWarning(err);
						/***如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
						*/
						//foreach (DataRow dr in dtYueShu.Rows)
						//{
						//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
						//        DBAccess.RunSQL("alter table " + table + " drop constraint " + dr[0].ToString());
						//}
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " drop column " + attr.Field);
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " ADD " + attr.Field + " INT DEFAULT '" + attr.DefaultVal + "' NULL");
						//continue;
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case 9:
					if (!FType.equals("float"))
					{
						//  string err = "err@字段类型不匹配,表[" + this.EnMap.PhysicsTable + "]字段[" + attr.Key + "]名称[" + attr.Desc + "]映射类型为[" + attr.MyDataTypeStr + "],数据类型为[" + FType + "]";
						//  BP.DA.Log.DebugWriteWarning(err);

						/***如果类型不匹配，就删除它在重新建, 先删除约束，在删除列，在重建。*/
						*/
						//foreach (DataRow dr in dtYueShu.Rows)
						//{
						//    if (dr["FName"].ToString().ToLower() == attr.Key.ToLower())
						//        DBAccess.RunSQL("alter table " + table + " drop constraint " + dr[0].ToString());
						//}
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " drop column " + attr.Field);
						//DBAccess.RunSQL("ALTER TABLE " + this.EnMap.PhysicsTable + " ADD " + attr.Field + " FLOAT DEFAULT '" + attr.DefaultVal + "' NULL");
						//continue;
					}
					break;
				default:
					//  throw new Exception("error MyFieldType= " + attr.MyFieldType + " key=" + attr.Key);
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 修复表字段。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}

			if (attr.UITag == null)
			{
				continue;
			}

			try
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			}
			catch (java.lang.Exception e2)
			{
			}

			try
			{
				String[] strs = attr.UITag.split("[@]", -1);
				SysEnums ens = new SysEnums();
				ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
				for (String s : strs)
				{
					if (s.equals("") || s == null)
					{
						continue;
					}

					String[] vk = s.split("[=]", -1);
					SysEnum se = new SysEnum();
					se.setIntKey(Integer.parseInt(vk[0]));
					se.setLab(vk[1]);
					se.setEnumKey(attr.getUIBindKey());
					se.Insert();
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@自动增加枚举时出现错误，请确定您的格式是否正确。" + ex.getMessage() + "attr.UIBindKey=" + attr.getUIBindKey());
			}

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 建立主键
		if (DBAccess.IsExitsTabPK(this.get_enMap().getPhysicsTable()) == false)
		{
			int pkconut = this.getPKCount();
			if (pkconut == 1)
			{
				DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), this.getPKField(), this.get_enMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), this.getPKField());
			}
			else if (pkconut == 2)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), pk0, pk1, this.get_enMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1);
			}
			else if (pkconut == 3)
			{
				String pk0 = this.getPKs()[0];
				String pk1 = this.getPKs()[1];
				String pk2 = this.getPKs()[2];
				DBAccess.CreatePK(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2, this.get_enMap().getEnDBUrl().getDBType());
				DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 创建索引.
		if (this.get_enMap().IndexField != null)
		{
			DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), this.get_enMap().IndexField);
		}

		int pkconut22 = this.getPKCount();
		if (pkconut22 == 1)
		{
			DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), this.getPKField());
		}
		else if (pkconut22 == 2)
		{
			String pk0 = this.getPKs()[0];
			String pk1 = this.getPKs()[1];
			DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1);
		}
		else if (pkconut22 == 3)
		{
			String pk0 = this.getPKs()[0];
			String pk1 = this.getPKs()[1];
			String pk2 = this.getPKs()[2];
			DBAccess.CreatIndex(this.get_enMap().getPhysicsTable(), pk0, pk1, pk2);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
	}
	/** 
	 检查物理表
	*/
	public final void CheckPhysicsTable()
	{
		this.set_enMap(this.getEnMap());

		//  string msg = "";
		if (this.get_enMap().getEnType() == EnType.View || this.get_enMap().getEnType() == EnType.XML || this.get_enMap().getEnType() == EnType.ThirdPartApp || this.get_enMap().getEnType() == EnType.Ext)
		{
			return;
		}

		if (DBAccess.IsExitsObject(this.get_enMap().getEnDBUrl(), this.get_enMap().getPhysicsTable()) == false)
		{
			/* 如果物理表不存在就新建立一个物理表。*/
			this.CreatePhysicsTable();
			return;
		}
		if (this.get_enMap().getIsView())
		{
			return;
		}


		DBType dbtype = this.get_enMap().getEnDBUrl().getDBType();

		// 如果不是主应用程序的数据库就不让执行检查. 考虑第三方的系统的安全问题.
		if (this.get_enMap().getEnDBUrl().getDBUrlType() != DBUrlType.AppCenterDSN)
		{
			return;
		}
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				this.CheckPhysicsTable_SQL();
				break;
			case Oracle:
				this.CheckPhysicsTable_Ora();
				break;
			case MySQL:
				this.CheckPhysicsTable_MySQL();
				break;
			case Informix:
				this.CheckPhysicsTable_Informix();
				break;
			case PostgreSQL:
				this.CheckPhysicsTable_PostgreSQL();
				break;
			default:
				throw new RuntimeException("@没有涉及到的数据库类型");
		}

		////检查从表.
		//MapDtls dtls = new MapDtls(this.ClassID);
		//foreach (MapDtl dtl in dtls)
		//{
		//    GEDtl dtlen = new GEDtl(dtl.No);
		//    dtlen.CheckPhysicsTable();
		//}
	}
	private void CheckPhysicsTable_Informix()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查字段是否存在
		String sql = "SELECT *  FROM " + this.getEnMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		//如果不存在.
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getIsPK())
			{
				continue;
			}

			if (dt.Columns.Contains(attr.getKey()) == true)
			{
				continue;
			}

			if (attr.getKey().equals("AID"))
			{
				/* 自动增长列 */
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT  Identity(1,1)");
				continue;
			}

			/*不存在此列 , 就增加此列。*/
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					int len = attr.getMaxLength();
					if (len == 0)
					{
						len = 200;
					}

					if (len >= 254)
					{
						DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " add " + attr.getField() + " lvarchar(" + len + ") default '" + attr.getDefaultVal() + "'");
					}
					else
					{
						DBAccess.RunSQL("alter table " + this.getEnMap().getPhysicsTable() + " add " + attr.getField() + " varchar(" + len + ") default '" + attr.getDefaultVal() + "'");
					}
					break;
				case DataType.AppInt:
				case DataType.AppBoolean:
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT8 DEFAULT " + attr.getDefaultVal() + " ");
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppDouble:
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " FLOAT DEFAULT  " + attr.getDefaultVal() + " ");
					break;
				default:
					throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查字段长度是否符合最低要求
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat || attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney || attr.getMyDataType() == DataType.AppBoolean)
			{
				continue;
			}

			int maxLen = attr.getMaxLength();
			dt = new DataTable();
			sql = "select c.*  from syscolumns c inner join systables t on c.tabid = t.tabid where t.tabname = lower('" + this.getEnMap().getPhysicsTable().toLowerCase() + "') and c.colname = lower('" + attr.getKey() + "') and c.collength < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.Count == 0)
			{
				continue;
			}
			for (DataRow dr : dt.Rows)
			{
				try
				{
					if (attr.getMaxLength() >= 255)
					{
						this.RunSQL("alter table " + dr.get("owner") + "." + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " lvarchar(" + attr.getMaxLength() + ")");
					}
					else
					{
						this.RunSQL("alter table " + dr.get("owner") + "." + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " varchar(" + attr.getMaxLength() + ")");
					}
				}
				catch (RuntimeException ex)
				{
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型字段是否是INT 类型
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning 没有处理好。
			continue;

			sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE  TABLE_NAME='" + this.getEnMap().getPhysicsTableExt().toLowerCase() + "' AND COLUMN_NAME='" + attr.getField().toLowerCase() + "'";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null)
			{
				Log.DefaultLogWriteLineError("@没有检测到字段:" + attr.getKey());
			}

			if (val.indexOf("CHAR") != -1)
			{
				/*如果它是 varchar 字段*/
				sql = "SELECT OWNER FROM ALL_TAB_COLUMNS WHERE upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase() + "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
				String OWNER = DBAccess.RunSQLReturnString(sql);
				try
				{
					this.RunSQL("alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " NUMBER ");
				}
				catch (RuntimeException ex)
				{
					Log.DefaultLogWriteLineError("运行sql 失败:alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}
			if (attr.UITag == null)
			{
				continue;
			}
			try
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			}
			catch (java.lang.Exception e)
			{
			}
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		this.CreateIndexAndPK();
	}
	private void CheckPhysicsTable_MySQL()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查字段是否存在
		String sql = "SELECT *  FROM " + this.get_enMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		//如果不存在.
		for (Attr attr : this.get_enMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getIsPK())
			{
				continue;
			}

			if (dt.Columns.Contains(attr.getKey()) == true)
			{
				continue; //不存在此列.
			}

			if (attr.getKey().equals("AID"))
			{
				/* 自动增长列 */
				DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " INT  Identity(1,1) COMMENT '" + attr.getDesc() + "'");
				continue;
			}

			/*不存在此列 , 就增加此列。*/
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
					int len = attr.getMaxLength();
					if (len == 0)
					{
						len = 200;
					}
					if (len > 3000)
					{
						DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " TEXT COMMENT '" + attr.getDesc() + "'");
					}
					else
					{
						DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " NVARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL COMMENT '" + attr.getDesc() + "'");
					}
					break;
				case DataType.AppDate:
				case DataType.AppDateTime:
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " NVARCHAR(20) DEFAULT '" + attr.getDefaultVal() + "' NULL COMMENT '" + attr.getDesc() + "'");
					break;
				case DataType.AppInt:
				case DataType.AppBoolean:
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NULL COMMENT '" + attr.getDesc() + "'");
					break;
				case DataType.AppFloat:
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " FLOAT (11,2) DEFAULT '" + attr.getDefaultVal() + "' NULL COMMENT '" + attr.getDesc() + "'");
					break;
				case DataType.AppMoney:
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " DECIMAL (20,4) DEFAULT '" + attr.getDefaultVal() + "' NULL COMMENT '" + attr.getDesc() + "'");
					break;
				case DataType.AppDouble:
					DBAccess.RunSQL("ALTER TABLE " + this.get_enMap().getPhysicsTable() + " ADD " + attr.getField() + " DOUBLE DEFAULT '" + attr.getDefaultVal() + "' NULL COMMENT '" + attr.getDesc() + "'");
					break;
				default:
					throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " key=" + attr.getKey());
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查字段长度是否符合最低要求
		for (Attr attr : this.get_enMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat || attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney || attr.getMyDataType() == DataType.AppBoolean)
			{
				continue;
			}

			int maxLen = attr.getMaxLength();
			dt = new DataTable();
			sql = "select character_maximum_length as Len, table_schema as OWNER FROM information_schema.columns WHERE TABLE_SCHEMA='" + BP.Sys.SystemConfig.getAppCenterDBDatabase() + "' AND table_name ='" + this.get_enMap().getPhysicsTable() + "' and column_Name='" + attr.getField() + "' AND character_maximum_length < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.Count == 0)
			{
				continue;
			}
			for (DataRow dr : dt.Rows)
			{
				try
				{
					if (attr.getMaxLength() < 3000)
					{
						this.RunSQL("alter table " + dr.get("OWNER") + "." + this.get_enMap().getPhysicsTableExt() + " modify " + attr.getField() + " NVARCHAR(" + attr.getMaxLength() + ")");
					}
					else
					{
						this.RunSQL("alter table " + dr.get("OWNER") + "." + this.get_enMap().getPhysicsTableExt() + " modify " + attr.getField() + " text");
					}
				}
				catch (RuntimeException ex)
				{
					BP.DA.Log.DebugWriteWarning(ex.getMessage());
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型字段是否是INT 类型
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}

			sql = "SELECT DATA_TYPE FROM information_schema.columns WHERE table_name='" + this.get_enMap().getPhysicsTable() + "' AND COLUMN_NAME='" + attr.getField() + "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "'";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null)
			{
				Log.DefaultLogWriteLineError("@没有检测到字段eunm" + attr.getKey());
			}

			if (val.indexOf("CHAR") != -1)
			{
				/*如果它是 varchar 字段*/
				sql = "SELECT table_schema as OWNER FROM information_schema.columns WHERE  table_name='" + this.get_enMap().getPhysicsTableExt() + "' AND COLUMN_NAME='" + attr.getField() + "' and table_schema='" + SystemConfig.getAppCenterDBDatabase() + "'";
				String OWNER = DBAccess.RunSQLReturnString(sql);
				try
				{
					this.RunSQL("alter table  " + this.get_enMap().getPhysicsTableExt() + " modify " + attr.getField() + " NUMBER ");
				}
				catch (RuntimeException ex)
				{
					Log.DefaultLogWriteLineError("运行sql 失败:alter table  " + this.get_enMap().getPhysicsTableExt() + " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}
			if (attr.UITag == null)
			{
				continue;
			}
			try
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			}
			catch (java.lang.Exception e)
			{
			}
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		this.CreateIndexAndPK();
	}
	private void CheckPhysicsTable_Ora()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查字段是否存在
		String sql = "SELECT *  FROM " + this.getEnMap().getPhysicsTable() + " WHERE 1=2";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		//如果不存在.
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (attr.getIsPK())
			{
				continue;
			}

			if (dt.Columns.Contains(attr.getKey()) == true)
			{
				continue;
			}

			if (attr.getKey().equals("AID"))
			{
				/* 自动增长列 */
				DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT  Identity(1,1)");
				continue;
			}

			/*不存在此列 , 就增加此列。*/
			switch (attr.getMyDataType())
			{
				case DataType.AppString:
				case DataType.AppDate:
				case DataType.AppDateTime:
					int len = attr.getMaxLength();
					if (len == 0)
					{
						len = 200;
					}
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " VARCHAR(" + len + ") DEFAULT '" + attr.getDefaultVal() + "' NULL");
					break;
				case DataType.AppInt:
				case DataType.AppBoolean:
					if (attr.getIsPK() == true)
					{
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "' NOT NULL");
					}
					else
					{
						DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " INT DEFAULT '" + attr.getDefaultVal() + "'   NULL");
					}
					break;
				case DataType.AppFloat:
				case DataType.AppMoney:
				case DataType.AppDouble:
					DBAccess.RunSQL("ALTER TABLE " + this.getEnMap().getPhysicsTable() + " ADD " + attr.getField() + " FLOAT DEFAULT '" + attr.getDefaultVal() + "' NULL");
					break;
				default:
					throw new RuntimeException("error MyFieldType= " + attr.getMyFieldType() + " Key=" + attr.getKey());
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查字段长度是否符合最低要求
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat || attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney || attr.getMyDataType() == DataType.AppBoolean)
			{
				continue;
			}

			int maxLen = attr.getMaxLength();
			dt = new DataTable();
			//sql = "SELECT DATA_LENGTH AS LEN, OWNER FROM ALL_TAB_COLUMNS WHERE upper(TABLE_NAME)='" + this.EnMap.PhysicsTableExt.ToUpper() 
			//    + "' AND UPPER(COLUMN_NAME)='" + attr.Field.ToUpper() + "' AND DATA_LENGTH < " + attr.MaxLength;

			//update dgq 2016-5-24 不取所有用户的表列名，只要取自己的就可以了
			sql = "SELECT DATA_LENGTH AS LEN FROM USER_TAB_COLUMNS WHERE upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase() + "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' AND DATA_LENGTH < " + attr.getMaxLength();
			dt = this.RunSQLReturnTable(sql);
			if (dt.Rows.Count == 0)
			{
				continue;
			}

			for (DataRow dr : dt.Rows)
			{
				//this.RunSQL("alter table " + dr["OWNER"] + "." + this.EnMap.PhysicsTableExt + " modify " + attr.Field + " varchar2(" + attr.MaxLength + ")");

				this.RunSQL("alter table " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " varchar2(" + attr.getMaxLength() + ")");

			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型字段是否是INT 类型
		Attrs attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}
			sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase() + "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
			String val = DBAccess.RunSQLReturnString(sql);
			if (val == null)
			{
				Log.DefaultLogWriteLineError("@没有检测到字段eunm" + attr.getKey());
			}
			if (val.indexOf("CHAR") != -1)
			{
				/*如果它是 varchar 字段*/
				sql = "SELECT OWNER FROM ALL_TAB_COLUMNS WHERE upper(TABLE_NAME)='" + this.getEnMap().getPhysicsTableExt().toUpperCase() + "' AND UPPER(COLUMN_NAME)='" + attr.getField().toUpperCase() + "' ";
				String OWNER = DBAccess.RunSQLReturnString(sql);
				try
				{
					this.RunSQL("alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " NUMBER ");
				}
				catch (RuntimeException ex)
				{
					Log.DefaultLogWriteLineError("运行sql 失败:alter table  " + this.getEnMap().getPhysicsTableExt() + " modify " + attr.getField() + " NUMBER " + ex.getMessage());
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查枚举类型是否存在.
		attrs = this.get_enMap().getHisEnumAttrs();
		for (Attr attr : attrs)
		{
			if (attr.getMyDataType() != DataType.AppInt)
			{
				continue;
			}
			if (attr.UITag == null)
			{
				continue;
			}
			try
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey(), attr.UITag);
				continue;
			}
			catch (java.lang.Exception e)
			{
			}
			String[] strs = attr.UITag.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, attr.getUIBindKey());
			for (String s : strs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				se.setLab(vk[1]);
				se.setEnumKey(attr.getUIBindKey());
				se.Insert();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
		this.CreateIndexAndPK();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 自动处理数据
	public final void AutoFull()
	{
		if (this.getPKVal().equals("0") || this.getPKVal().equals(""))
		{
			return;
		}

		if (this.getEnMap().getIsHaveAutoFull() == false)
		{
			return;
		}

		Attrs attrs = this.getEnMap().getAttrs();
		String jsAttrs = "";
		ArrayList al = new ArrayList();
		for (Attr attr : attrs)
		{
			if (attr.AutoFullDoc == null || attr.AutoFullDoc.length() == 0)
			{
				continue;
			}

			// 这个代码需要提纯到基类中去。
			switch (attr.AutoFullWay)
			{
				case Way0:
					continue;
				case Way1_JS:
					al.add(attr);
					break;
				case Way2_SQL:
					String sql = attr.AutoFullDoc;
					sql = sql.replace("~", "'");

					sql = sql.replace("@WebUser.No", Web.WebUser.getNo());
					sql = sql.replace("@WebUser.Name", Web.WebUser.getName());
					sql = sql.replace("@WebUser.FK_Dept", Web.WebUser.getFK_Dept());

					if (sql.contains("@") == true)
					{
						Attrs attrs1 = this.getEnMap().getAttrs();
						for (Attr a1 : attrs1)
						{
							if (sql.contains("@") == false)
							{
								break;
							}

							if (sql.contains("@" + a1.getKey()) == false)
							{
								continue;
							}

							if (a1.getIsNum())
							{
								sql = sql.replace("@" + a1.getKey(), this.GetValStrByKey(a1.getKey()));
							}
							else
							{
								sql = sql.replace("@" + a1.getKey(), "'" + this.GetValStrByKey(a1.getKey()) + "'");
							}
						}
					}

					sql = sql.replace("''", "'");
					String val = "";
					try
					{
						val = DBAccess.RunSQLReturnString(sql);
					}
					catch (RuntimeException ex)
					{
						throw new RuntimeException("@字段(" + attr.getKey() + "," + attr.getDesc() + ")自动获取数据期间错误(有可能是您写的sql语句会返回多列多行的table,现在只要一列一行的table才能填充，请检查sql.):" + sql.replace("'", "“") + " @Tech Info:" + ex.getMessage().Replace("'", "“") + "@执行的sql:" + sql);
					}

					if (attr.getIsNum())
					{
						/* 如果是数值类型的就尝试着转换数值，转换不了就跑出异常信息。*/
						try
						{
							BigDecimal d = BigDecimal.Parse(val);
						}
						catch (java.lang.Exception e)
						{
							throw new RuntimeException(val);
						}
					}
					this.SetValByKey(attr.getKey(), val);
					break;
				case Way3_FK:
					try
					{
						String sqlfk = "SELECT @Field FROM @Table WHERE No=@AttrKey";
						String[] strsFK = attr.AutoFullDoc.split("[@]", -1);
						for (String str : strsFK)
						{
							if (str == null || str.length() == 0)
							{
								continue;
							}

							String[] ss = str.split("[=]", -1);
							if (ss[0].equals("AttrKey"))
							{
								String tempV = this.GetValStringByKey(ss[1]);
								if (tempV.equals("") || tempV == null)
								{
									if (this.getEnMap().getAttrs().Contains(ss[1]) == false)
									{
										throw new RuntimeException("@自动获取值信息不完整,Map 中已经不包含Key=" + ss[1] + "的属性。");
									}

									//throw new Exception("@自动获取值信息不完整,Map 中已经不包含Key=" + ss[1] + "的属性。");
									sqlfk = sqlfk.replace('@' + ss[0], "'@xxx'");
									Log.DefaultLogWriteLineWarning("@在自动取值期间出现错误:" + this.toString() + " , " + this.getPKVal() + "没有自动获取到信息。");
								}
								else
								{
									sqlfk = sqlfk.replace('@' + ss[0], "'" + this.GetValStringByKey(ss[1]) + "'");
								}
							}
							else
							{
								sqlfk = sqlfk.replace('@' + ss[0], ss[1]);
							}
						}

						sqlfk = sqlfk.replace("''", "'");
						this.SetValByKey(attr.getKey(), DBAccess.RunSQLReturnStringIsNull(sqlfk, null));
					}
					catch (RuntimeException ex)
					{
						throw new RuntimeException("@在处理自动完成：外键[" + attr.getKey() + ";" + attr.getDesc() + "],时出现错误。异常信息：" + ex.getMessage());
					}
					break;
				case Way4_Dtl:
					if (this.getPKVal().equals("0"))
					{
						continue;
					}

					String mysql = "SELECT @Way(@Field) FROM @Table WHERE RefPK =" + this.getPKVal();
					String[] strs = attr.AutoFullDoc.split("[@]", -1);
					for (String str : strs)
					{
						if (str == null || str.length() == 0)
						{
							continue;
						}

						String[] ss = str.split("[=]", -1);
						mysql = mysql.replace('@' + ss[0], ss[1]);
					}

					String v = DBAccess.RunSQLReturnString(mysql);
					if (v == null)
					{
						v = "0";
					}
					this.SetValByKey(attr.getKey(), BigDecimal.Parse(v));

					break;
				default:
					throw new RuntimeException("未涉及到的类型。");
			}
		}

		// 处理JS的计算。
		for (Attr attr : al)
		{
			String doc = attr.AutoFullDoc.Clone().toString();
			for (Attr a : attrs)
			{
				if (a.getKey().equals(attr.getKey()))
				{
					continue;
				}

				doc = doc.replace("@" + a.getKey(), this.GetValStrByKey(a.getKey()).toString());
				doc = doc.replace("@" + a.getDesc(), this.GetValStrByKey(a.getKey()).toString());
			}

			try
			{
				BigDecimal d = DataType.ParseExpToDecimal(doc);
				this.SetValByKey(attr.getKey(), d);
			}
			catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLineError("@(" + this.toString() + ")在处理自动计算{" + this.getEnDesc() + "}：" + this.getPK() + "=" + this.getPKVal() + "时，属性[" + attr.getKey() + "]，计算内容[" + doc + "]，出现错误：" + ex.getMessage());
				throw new RuntimeException("@(" + this.toString() + ")在处理自动计算{" + this.getEnDesc() + "}：" + this.getPK() + "=" + this.getPKVal() + "时，属性[" + attr.getKey() + "]，计算内容[" + doc + "]，出现错误：" + ex.getMessage());
			}
		}

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 把entity的实体属性调度到en里面去.
	*/

	public final MapData DTSMapToSys_MapData()
	{
		return DTSMapToSys_MapData(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public MapData DTSMapToSys_MapData(string fk_mapdata = null)
	public final MapData DTSMapToSys_MapData(String fk_mapdata)
	{
		if (fk_mapdata == null)
		{
			//fk_mapdata = this.ClassIDOfShort;

			//为了适应配置vsto系统的需要，这里需要全称.
			fk_mapdata = this.toString();
		}

		Map map = this.getEnMap();

		//获得短的类名称.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 更新主表信息.
		MapData md = new MapData();
		md.setNo(fk_mapdata);
		if (md.RetrieveFromDBSources() == 0)
		{
			md.Insert();
		}

		md.setEnPK(this.getPK()); //主键
		md.setEnsName(fk_mapdata); //类名.
		md.setName(map.getEnDesc());
		md.setPTable(map.getPhysicsTable());
		md.Update();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 更新主表信息.

		//删除.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, fk_mapdata);

		//同步属性 mapattr.
		DTSMapToSys_MapData_InitMapAttr(map.getAttrs(), fk_mapdata);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 同步从表.
		//同步从表.
		EnDtls dtls = map.getDtls();
		for (EnDtl dtl : dtls)
		{
			MapDtl mdtl = new MapDtl();

			Entity enDtl = dtl.getEns().getGetNewEntity();

			mdtl.setNo(enDtl.getClassIDOfShort());
			if (mdtl.RetrieveFromDBSources() == 0)
			{
				mdtl.Insert();
			}

			mdtl.setName(enDtl.getEnDesc());
			mdtl.setFK_MapData(fk_mapdata);
			mdtl.setPTable(enDtl.getEnMap().getPhysicsTable());

			mdtl.setRefPK(dtl.getRefKey()); //关联的主键.

			mdtl.Update();

			//同步字段.
			DTSMapToSys_MapData_InitMapAttr(enDtl.getEnMap().getAttrs(), enDtl.getClassIDOfShort());
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 同步从表.

		return md;

	}
	/** 
	 同步字段属性
	 
	 @param attrs
	 @param fk_mapdata
	*/
	private void DTSMapToSys_MapData_InitMapAttr(Attrs attrs, String fk_mapdata)
	{
		for (Attr attr : attrs)
		{
			if (attr.getIsRefAttr())
			{
				continue;
			}

			MapAttr mattr = new MapAttr();
			mattr.setKeyOfEn(attr.getKey());
			mattr.setFK_MapData(fk_mapdata);
			mattr.setMyPK(mattr.getFK_MapData() + "_" + mattr.getKeyOfEn());
			mattr.RetrieveFromDBSources();

			mattr.setName(attr.getDesc());
			mattr.setDefVal(attr.getDefaultVal().toString());
			mattr.setKeyOfEn(attr.getField());

			mattr.setMaxLen(attr.getMaxLength());
			mattr.setMinLen(attr.getMinLength());
			mattr.setUIBindKey(attr.getUIBindKey());
			mattr.setUIIsLine(attr.UIIsLine);
			mattr.setUIHeight(0);

			if (attr.getMaxLength() > 3000)
			{
				mattr.setUIHeight(10);
			}

			mattr.setUIWidth(attr.getUIWidth());
			mattr.setMyDataType(attr.getMyDataType());

			mattr.setUIRefKey(attr.getUIRefKeyValue());

			mattr.setUIRefKeyText(attr.getUIRefKeyText());
			mattr.setUIVisible(attr.getUIVisible());

			//设置显示与隐藏，按照默认值.
			if (mattr.GetParaString("SearchVisable").equals(""))
			{
				if (mattr.getUIVisible() == true)
				{
					mattr.SetPara("SearchVisable", 1);
				}
				else
				{
					mattr.SetPara("SearchVisable", 0);
				}
			}


			switch (attr.getMyFieldType())
			{
				case Enum:
				case PKEnum:
					mattr.setUIContralType(attr.getUIContralType());
					mattr.setLGType(FieldTypeS.Enum);
					mattr.setUIIsEnable(attr.getUIIsReadonly());
					break;
				case FK:
				case PKFK:
					mattr.setUIContralType(attr.getUIContralType());
					mattr.setLGType(FieldTypeS.FK);
					//attr.MyDataType = (int)FieldType.FK;
					mattr.setUIRefKey("No");
					mattr.setUIRefKeyText("Name");
					mattr.setUIIsEnable(attr.getUIIsReadonly());
					break;
				default:
					mattr.setUIContralType(UIContralType.TB);
					mattr.setLGType(FieldTypeS.Normal);
					mattr.setUIIsEnable(!attr.getUIIsReadonly());
					switch (attr.getMyDataType())
					{
						case DataType.AppBoolean:
							mattr.setUIContralType(UIContralType.CheckBok);
							mattr.setUIIsEnable(attr.getUIIsReadonly());
							break;
						case DataType.AppDate:
							//if (this.Tag == "1")
							//    attr.DefaultVal = DataType.CurrentData;
							break;
						case DataType.AppDateTime:
							//if (this.Tag == "1")
							//    attr.DefaultVal = DataType.CurrentData;
							break;
						default:
							break;
					}
					break;
			}
			if (mattr.Update() == 0)
			{
				mattr.Insert();
			}
		}
	}

}