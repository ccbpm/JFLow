package BP.En;

import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.Sys.SysFileManager;
import BP.Sys.SysFileManagers;

/** 
  关于对Entity扩展，的方法。
 
*/
public class GloEntity
{
		///#region 用到ddl 的方法。
	public static String GetTextByValue(Entities ens, String no, String isNullAsVal)
	{
		try
		{
			return GetTextByValue(ens, no);
		}
		catch (java.lang.Exception e)
		{
			return isNullAsVal;
		}
	}
	public static String GetTextByValue(Entities ens, String no)
	{
		for (Entity en : Entities.convertEntities(ens))
		{
			if (en.GetValStringByKey("No").equals(no))
			{
				return en.GetValStringByKey("Name");
			}
		}
		if (ens.size() == 0)
		{
			throw new RuntimeException("@实体集合里面没有数据.");
		}
		else
		{
			throw new RuntimeException("@没有找到No=" + no + "在实体里面");
		}
	}
		///#endregion

	public static String GetEnFilesUrl(Entity en)
	{
		String str = null;
		SysFileManagers ens = en.getHisSysFileManagers();

		String path = BP.Sys.Glo.getRequest().getRemoteHost();
		for (Object file : ens)
		{
			str += "[<a href='" + path + ((SysFileManager)file).getMyFilePath() + "' target='f" + ((SysFileManager)file).getOID() + "' >" + ((SysFileManager)file).getMyFileName() + "</a>]";
		}
		return str;
	}

		///#region 关于对entity 的处理

		///#region 转换dataset
	/** 
	 把指定的ens 转换为 dataset
	 
	 @param spen 指定的ens
	 @return 返回关系dataset
	*/
	public static DataSet ToDataSet(Entities spens)
	{
		throw new RuntimeException("未实现的方法");
		/*
		 * warning DataSet ds = new DataSet(spens.toString());

		// 把主表加入DataSet 
		Entity en = spens.getGetNewEntity();
		DataTable dt = new DataTable();
		if (spens.size() == 0)
		{
			QueryObject qo = new QueryObject(spens);
			dt = qo.DoQueryToTable();
		}
		else
		{
			dt = spens.ToDataTableField();
		}
		dt.TableName = en.getEnDesc(); //设定主表的名称。

		dt.RowChanged += new DataRowChangeEventHandler(dt_RowChanged);

		//dt.RowChanged+=new DataRowChangeEventHandler(dt_RowChanged);

		ds.Tables.Add(DealBoolTypeInDataTable(en, dt));


		for (EnDtl ed : en.getEnMap().getDtlsAll())
		{
			// 循环主表的明细，编辑好关系并把他们放入 DataSet 里面。
			Entities edens = ed.getEns();
			Entity eden = edens.getGetNewEntity();
			DataTable edtable = edens.RetrieveAllToTable();
			edtable.TableName = eden.getEnDesc();
			ds.Tables.Add(DealBoolTypeInDataTable(eden, edtable));

			DataRelation r1 = new DataRelation(ed.getDesc(), ds.Tables[dt.TableName].Columns[en.getPK()], ds.Tables[edtable.TableName].Columns[ed.getRefKey()]);
			ds.Relations.Add(r1);


			//	int i = 0 ;

			for (EnDtl ed1 : eden.getEnMap().getDtlsAll())
			{
				// 主表的明细的明细。
				Entities edlens1 = ed1.getEns();
				Entity edlen1 = edlens1.getGetNewEntity();

				DataTable edlensTable1 = edlens1.RetrieveAllToTable();
				edlensTable1.TableName = edlen1.getEnDesc();
				//edlensTable1.TableName =ed1.Desc ;


				ds.Tables.Add(DealBoolTypeInDataTable(edlen1, edlensTable1));

				DataRelation r2 = new DataRelation(ed1.getDesc(), ds.Tables[edtable.TableName].Columns[eden.getPK()], ds.Tables[edlensTable1.TableName].Columns[ed1.getRefKey()]);
				ds.Relations.Add(r2);
			}

		}


		return ds;*/
	}
	/** 
	 
	 
	 @param en
	 @param dt
	 @return 
	*/
	private static DataTable DealBoolTypeInDataTable(Entity en, DataTable dt)
	{
		throw new RuntimeException("未实现的方法");
		/*
		 * warning for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyDataType() == DataType.AppBoolean)
			{
				DataColumn col = new DataColumn();
				col.ColumnName = "tmp" + attr.getKey();
				col.DataType = Boolean.class;
				dt.Columns.Add(col);
				for (DataRow dr : dt.Rows)
				{
					if (dr[attr.getKey()].toString().equals("1"))
					{
						dr["tmp" + attr.getKey()] = true;
					}
					else
					{
						dr["tmp" + attr.getKey()] = false;
					}
				}
				dt.Columns.remove(attr.getKey());
				dt.Columns["tmp" + attr.getKey()].ColumnName = attr.getKey();
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
			{
				DataColumn col = new DataColumn();
				col.ColumnName = "tmp" + attr.getKey();
				col.DataType = java.util.Date.class;
				dt.Columns.Add(col);
				for (DataRow dr : dt.Rows)
				{
					try
					{
						dr["tmp" + attr.getKey()] = new java.util.Date(java.util.Date.parse(dr[attr.getKey()].toString()));
					}
					catch (java.lang.Exception e)
					{
						if (attr.getDefaultVal().toString().equals(""))
						{
							dr["tmp" + attr.getKey()] = new java.util.Date();
						}
						else
						{
							dr["tmp" + attr.getKey()] = new java.util.Date(java.util.Date.parse(attr.getDefaultVal().toString()));
						}

					}

				}
				dt.Columns.remove(attr.getKey());
				dt.Columns["tmp" + attr.getKey()].ColumnName = attr.getKey();
				continue;
			}
		}
		return dt;*/
	}
	/** 
	 DataRowChangeEventArgs
	 
	 @param sender
	 @param e
	*/
//	private static void dt_RowChanged(Object sender, DataRowChangeEventArgs e)
//	{
//		throw new RuntimeException(sender.toString() + "  rows change ." + e.Row.toString());
//	}

		///#endregion

	/** 
	 把属性信息,与vlaue 转换为Table
	 
	 @param en 要转换的entity
	 @param editStyle 编辑风格
	 @return datatable
	*/
	public static DataTable ToTable(Entity en, int editStyle)
	{
		if (editStyle == 0)
		{
			return GloEntity.ToTable0(en);
		}
		else
		{
			return GloEntity.ToTable1(en);
		}
	}
	/** 
	 用户风格0
	 
	 @return 
	*/
	private static DataTable ToTable0(Entity en)
	{
		String nameOfEnterInfo = en.getEnDesc();
		DataTable dt = new DataTable();
		dt.Columns.Add(new DataColumn("输入项目", String.class));
		dt.Columns.Add(new DataColumn(nameOfEnterInfo, String.class));
		dt.Columns.Add(new DataColumn("信息输入要求", String.class));

		for (Attr attr : en.getEnMap().getAttrs())
		{
			DataRow dr = dt.NewRow();
			dr.put("输入项目", attr.getDesc());
			dr.put(nameOfEnterInfo, en.GetValByKey(attr.getKey()));
			dr.put("信息输入要求", attr.getEnterDesc());
			dt.Rows.add(dr);
			/*
			 * warning dr["输入项目"] = attr.getDesc();
			dr[nameOfEnterInfo] = en.GetValByKey(attr.getKey());
			dr["信息输入要求"] = attr.getEnterDesc();
			dt.Rows.Add(dr);*/
		}
		// 如果实体需要附件。
		if (en.getEnMap().getAdjunctType() != AdjunctType.None)
		{
			// 加入附件信息。
			DataRow dr1 = dt.NewRow();
			dr1.put("输入项目", "附件");
			dr1.put(nameOfEnterInfo, "");
			dr1.put("信息输入要求", "编辑附件");
			dt.Rows.add(dr1);
			/*
			 * warning dr1["输入项目"] = "附件";
			dr1[nameOfEnterInfo] = "";
			dr1["信息输入要求"] = "编辑附件";
			dt.Rows.Add(dr1);*/
		}
		// 明细
		for (EnDtl dtl : en.getEnMap().getDtls())
		{
			DataRow dr = dt.NewRow();
			dr.put("输入项目", dtl.getDesc());
			dr.put(nameOfEnterInfo, "EnsName_" + dtl.getEns().toString() + "_RefKey_" + dtl.getRefKey());
			dr.put("信息输入要求", "请进入编辑明细");
			dt.Rows.add(dr);
			/*
			 * warning dr["输入项目"] = dtl.getDesc();
			dr[nameOfEnterInfo] = "EnsName_" + dtl.getEns().toString() + "_RefKey_" + dtl.getRefKey();
			dr["信息输入要求"] = "请进入编辑明细";
			dt.Rows.Add(dr);*/
		}
		for (AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM())
		{
			DataRow dr = dt.NewRow();
			dr.put("输入项目", attr.getDesc());
			dr.put(nameOfEnterInfo, "OneVSM" + attr.getEnsOfMM().toString());
			dr.put("信息输入要求", "请进入编辑多选");
			dt.Rows.add(dr);
			/*
			 * warning dr["输入项目"] = attr.getDesc();
			dr[nameOfEnterInfo] = "OneVSM" + attr.getEnsOfMM().toString();
			dr["信息输入要求"] = "请进入编辑多选";
			dt.Rows.Add(dr);*/
		}
		return dt;

	}
	/** 
	 用户风格1
	 
	 @return 
	*/
	private static DataTable ToTable1(Entity en)
	{

		String col1 = "字段名1";
		String col2 = "内容1";
		String col3 = "字段名2";
		String col4 = "内容2";

		//string enterNote=null;
		//			if (this.EnMap.Dtls.Count==0 || this.EnMap.AttrsOfOneVSM.Count==0)
		//				enterNote="内容1";
		//			else
		//				enterNote="保存后才能编辑关联信息";


		DataTable dt = new DataTable();
		dt.Columns.Add(new DataColumn(col1, String.class));
		dt.Columns.Add(new DataColumn(col2, String.class));
		dt.Columns.Add(new DataColumn(col3, String.class));
		dt.Columns.Add(new DataColumn(col4, String.class));


		for (int i = 0; i < en.getEnMap().getHisPhysicsAttrs().size(); i++)
		{
			DataRow dr = dt.NewRow();
			Attr attr = en.getEnMap().getHisPhysicsAttrs().getItem(i);
			dr.put(col1, attr.getDesc());
			dr.put(col2, en.GetValByKey(attr.getKey()));
			/*
			 * warning dr[col1] = attr.getDesc();
			dr[col2] = en.GetValByKey(attr.getKey());*/

			i++;
			if (i == en.getEnMap().getHisPhysicsAttrs().size())
			{
				dt.Rows.add(dr);
				/*
				 * warning dt.Rows.Add(dr);*/
				break;
			}
			attr = en.getEnMap().getHisPhysicsAttrs().getItem(i);
			dr.put(col3, attr.getDesc());
			dr.put(col4, en.GetValByKey(attr.getKey()));
			dt.Rows.add(dr);
			/*
			 * warning dr[col3] = attr.getDesc();
			dr[col4] = en.GetValByKey(attr.getKey());
			dt.Rows.Add(dr);*/
		}


		// 如果实体需要附件。
		if (en.getEnMap().getAdjunctType() != AdjunctType.None)
		{
			// 加入附件信息。
			DataRow dr1 = dt.NewRow();
			dr1.put(col1, "附件");
			dr1.put(col2, "编辑附件");
			//dr["输入项目2"]="附件信息";

			dt.Rows.add(dr1);
			/*
			 * warning dr1[col1] = "附件";
			dr1[col2] = "编辑附件";
			//dr["输入项目2"]="附件信息";

			dt.Rows.Add(dr1);*/
		}
		// 明细
		for (EnDtl dtl : en.getEnMap().getDtls())
		{
			DataRow dr = dt.NewRow();
			dr.put(col1, dtl.getDesc());
			dr.put(col2, "EnsName_" + dtl.getEns().toString() + "_RefKey_" + dtl.getRefKey());
			//dr["输入项目2"]="明细信息";
			dt.Rows.add(dr);
			/*
			 * warning dr[col1] = dtl.getDesc();
			dr[col2] = "EnsName_" + dtl.getEns().toString() + "_RefKey_" + dtl.getRefKey();
			//dr["输入项目2"]="明细信息";
			dt.Rows.Add(dr);*/
		}
		// 多对多的关系
		for (AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM())
		{
			DataRow dr = dt.NewRow();
			dr.put(col1, attr.getDesc());
			dr.put(col2, "OneVSM" + attr.getEnsOfMM().toString());
			//dr["输入项目2"]="多选";
			dt.Rows.add(dr);
			/*
			 * warning dr[col1] = attr.getDesc();
			dr[col2] = "OneVSM" + attr.getEnsOfMM().toString();
			//dr["输入项目2"]="多选";
			dt.Rows.Add(dr);*/
		}
		return dt;
	}
		///#endregion

		///#region 张
	/** 
	 通过一个集合，一个key，一个分割符号，获得这个属性的子串。
	 
	 @param key
	 @param listspt
	 @return 
	*/
	public static String GetEnsString(Entities ens, String key, String listspt)
	{
		String str = "";
		for (Entity en : Entities.convertEntities(ens))
		{
			str += en.GetValByKey(key) + listspt;
		}
		return str;
	}
	/** 
	 通过一个集合，一个分割符号，获得这个属性的子串。
	 		
	 @param listspt
	 @return 
	*/
	public static String GetEnsString(Entities ens, String listspt)
	{
		return GetEnsString(ens, ens.getGetNewEntity().getPK(), listspt);
	}
	/** 
	 通过一个集合获得这个属性的子串。
	 		
	 @param listspt
	 @return 
	*/
	public static String GetEnsString(Entities ens)
	{
		return GetEnsString(ens, ens.getGetNewEntity().getPK(), ";");
	}
		///#endregion
}