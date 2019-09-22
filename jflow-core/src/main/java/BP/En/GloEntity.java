package BP.En;

import BP.En.*;
import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import java.time.*;

//using System.Web.SessionState;
//using System.Web.UI;
//using System.Web.UI.WebControls;
//using System.Web.UI.HtmlControls;

/** 
  关于对Entity扩展，的方法。
*/
public class GloEntity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		for (Entity en : ens)
		{
			if (en.GetValStringByKey("No").equals(no))
			{
				return en.GetValStringByKey("Name");
			}
		}
		if (ens.Count == 0)
		{
			throw new RuntimeException("@实体集合里面没有数据.");
		}
		else
		{
			throw new RuntimeException("@没有找到No=" + no + "在实体里面");
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	//public static string GetEnFilesUrl(Entity en)
	//{
	//    string str = null;
	//    SysFileManagers ens = null; // en.HisSysFileManagers;

	//    string path = BP.Sys.Glo.Request.ApplicationPath;
	//    foreach (SysFileManager file in ens)
	//    {
	//        str += "[<a href='" + path + file.MyFilePath + "' target='f" + file.OID + "' >" + file.MyFileName + "</a>]";
	//    }
	//    return str;
	//}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于对entity 的处理

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 转换dataset
	/** 
	 把指定的ens 转换为 dataset
	 
	 @param spen 指定的ens
	 @return 返回关系dataset
	*/
	public static DataSet ToDataSet(Entities spens)
	{

		DataSet ds = new DataSet(spens.toString());

		/* 把主表加入DataSet */
		Entity en = spens.getGetNewEntity();
		DataTable dt = new DataTable();
		if (spens.Count == 0)
		{
			QueryObject qo = new QueryObject(spens);
			dt = qo.DoQueryToTable();
		}
		else
		{
			dt = spens.ToDataTableField();
		}
		dt.TableName = en.getEnDesc(); //设定主表的名称。
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
		dt.RowChanged += new DataRowChangeEventHandler(dt_RowChanged);

		//dt.RowChanged+=new DataRowChangeEventHandler(dt_RowChanged);

		ds.Tables.Add(DealBoolTypeInDataTable(en, dt));


		for (EnDtl ed : en.getEnMap().getDtlsAll())
		{
			/* 循环主表的明细，编辑好关系并把他们放入 DataSet 里面。*/
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
				/* 主表的明细的明细。*/
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


		return ds;
	}
	/** 
	 
	 
	 @param en
	 @param dt
	 @return 
	*/
	private static DataTable DealBoolTypeInDataTable(Entity en, DataTable dt)
	{

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyDataType() == DataType.AppBoolean)
			{
				DataColumn col = new DataColumn();
				col.ColumnName = "tmp" + attr.getKey();
				col.DataType = Boolean.class;
				dt.Columns.Add(col);
				for (DataRow dr : dt.Rows)
				{
					if (dr.get(attr.getKey()).toString().equals("1"))
					{
						dr.set("tmp" + attr.getKey(), true);
					}
					else
					{
						dr.set("tmp" + attr.getKey(), false);
					}
				}
				dt.Columns.Remove(attr.getKey());
				dt.Columns["tmp" + attr.getKey()].ColumnName = attr.getKey();
				continue;
			}
			if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
			{
				DataColumn col = new DataColumn();
				col.ColumnName = "tmp" + attr.getKey();
				col.DataType = LocalDateTime.class;
				dt.Columns.Add(col);
				for (DataRow dr : dt.Rows)
				{
					try
					{
						dr.set("tmp" + attr.getKey(), LocalDateTime.parse(dr.get(attr.getKey()).toString()));
					}
					catch (java.lang.Exception e)
					{
						if (attr.getDefaultVal().toString().equals(""))
						{
							dr.set("tmp" + attr.getKey(), LocalDateTime.now());
						}
						else
						{
							dr.set("tmp" + attr.getKey(), LocalDateTime.parse(attr.getDefaultVal().toString()));
						}

					}

				}
				dt.Columns.Remove(attr.getKey());
				dt.Columns["tmp" + attr.getKey()].ColumnName = attr.getKey();
				continue;
			}
		}
		return dt;
	}
	/** 
	 DataRowChangeEventArgs
	 
	 @param sender
	 @param e
	*/
	private static void dt_RowChanged(Object sender, DataRowChangeEventArgs e)
	{
		throw new RuntimeException(sender.toString() + "  rows change ." + e.Row.toString());
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
			dr.set("输入项目", attr.getDesc());
			dr.set(nameOfEnterInfo, en.GetValByKey(attr.getKey()));
			dr.set("信息输入要求", attr.getEnterDesc());
			dt.Rows.Add(dr);
		}
		// 如果实体需要附件。
		if (en.getEnMap().getAdjunctType() != AdjunctType.None)
		{
			// 加入附件信息。
			DataRow dr1 = dt.NewRow();
			dr1.set("输入项目", "附件");
			dr1.set(nameOfEnterInfo, "");
			dr1.set("信息输入要求", "编辑附件");
			dt.Rows.Add(dr1);
		}
		// 明细
		for (EnDtl dtl : en.getEnMap().getDtls())
		{
			DataRow dr = dt.NewRow();
			dr.set("输入项目", dtl.getDesc());
			dr.set(nameOfEnterInfo, "EnsName_" + dtl.getEns().toString() + "_RefKey_" + dtl.getRefKey());
			dr.set("信息输入要求", "请进入编辑明细");
			dt.Rows.Add(dr);
		}
		for (AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM())
		{
			DataRow dr = dt.NewRow();
			dr.set("输入项目", attr.getDesc());
			dr.set(nameOfEnterInfo, "OneVSM" + attr.getEnsOfMM().toString());
			dr.set("信息输入要求", "请进入编辑多选");
			dt.Rows.Add(dr);
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


		for (int i = 0; i < en.getEnMap().getHisPhysicsAttrs().Count; i++)
		{
			DataRow dr = dt.NewRow();
			Attr attr = en.getEnMap().getHisPhysicsAttrs().get(i);
			dr.set(col1, attr.getDesc());
			dr.set(col2, en.GetValByKey(attr.getKey()));

			i++;
			if (i == en.getEnMap().getHisPhysicsAttrs().Count)
			{
				dt.Rows.Add(dr);
				break;
			}
			attr = en.getEnMap().getHisPhysicsAttrs().get(i);
			dr.set(col3, attr.getDesc());
			dr.set(col4, en.GetValByKey(attr.getKey()));
			dt.Rows.Add(dr);
		}


		// 如果实体需要附件。
		if (en.getEnMap().getAdjunctType() != AdjunctType.None)
		{
			// 加入附件信息。
			DataRow dr1 = dt.NewRow();
			dr1.set(col1, "附件");
			dr1.set(col2, "编辑附件");
			//dr["输入项目2"]="附件信息";

			dt.Rows.Add(dr1);
		}
		// 明细
		for (EnDtl dtl : en.getEnMap().getDtls())
		{
			DataRow dr = dt.NewRow();
			dr.set(col1, dtl.getDesc());
			dr.set(col2, "EnsName_" + dtl.getEns().toString() + "_RefKey_" + dtl.getRefKey());
			//dr["输入项目2"]="明细信息";
			dt.Rows.Add(dr);
		}
		// 多对多的关系
		for (AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM())
		{
			DataRow dr = dt.NewRow();
			dr.set(col1, attr.getDesc());
			dr.set(col2, "OneVSM" + attr.getEnsOfMM().toString());
			//dr["输入项目2"]="多选";
			dt.Rows.Add(dr);
		}
		return dt;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		for (Entity en : ens)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}