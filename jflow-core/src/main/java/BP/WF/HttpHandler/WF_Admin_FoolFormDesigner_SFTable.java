package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_Admin_FoolFormDesigner_SFTable extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_FoolFormDesigner_SFTable()
	{

	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region xxx 界面 .
	/** 
	  初始化sf0. @于庆海，新方法.
	 
	 @return 
	*/
	public final String SF0_Init()
	{
		String cl = "BP.En.Entities";
		ArrayList al = ClassFactory.GetObjects(cl);

		//定义容器.
		DataTable dt = new DataTable();
		dt.Columns.Add("No");
		dt.Columns.Add("Name");

		SFTables sfs = new SFTables();
		sfs.RetrieveAll();

		for (Object obj : al)
		{
			Entities ens = obj instanceof Entities ? (Entities)obj : null;
			if (ens == null)
			{
				continue;
			}

			try
			{
				Entity en = ens.GetNewEntity;
				if (en == null)
				{
					continue;
				}

				if (en.EnMap.Attrs.Contains("No") == false)
				{
					continue;
				}

				if (sfs.Contains(ens.toString()) == true)
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.set("No", ens.toString());

				if (en.IsTreeEntity)
				{
					dr.set("Name", en.EnMap.EnDesc + "(树结构) " + ens.toString());
				}
				else
				{
					dr.set("Name", en.EnMap.EnDesc + " " + ens.toString());
				}

				dt.Rows.add(dr);
			}
			catch (java.lang.Exception e)
			{

			}
		}
		return BP.Tools.Json.ToJson(dt);
	}
	public final String SF0_Save()
	{
		return "保存成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion xxx 界面方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 表或者视图 .
	/** 
	  初始化sf2.
	 
	 @return 
	*/
	public final String SF2_Init()
	{
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		return srcs.ToJson();
	}

	public final String SF2_GetTVs()
	{
		String src = this.GetRequestVal("src");

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetTables();

		return BP.Tools.Json.ToJson(dt);
	}

	public final String SF2_GetCols()
	{
		String src = this.GetRequestVal("src");
		String table = this.GetRequestVal("table");

		if (DataType.IsNullOrEmpty(src))
		{
			throw new RuntimeException("err@参数不正确");
		}

		if (DataType.IsNullOrEmpty(table))
		{
			return "[]";
		}

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetColumns(table);

		for (DataRow r : dt.Rows)
		{
			r.set("Name", r.get("No") + (r.get("Name") == null || r.get("Name") == DBNull.Value || DataType.IsNullOrEmpty(r.get("Name").toString()) ? "" : String.format("[%1$s]", r.get("Name"))));
		}

		return BP.Tools.Json.ToJson(dt);
	}

	public final String SF2_Save()
	{
		SFTable sf = new SFTable();
		sf.No = this.GetValFromFrmByKey("No");
		if (sf.IsExits)
		{
			return "err@标记:" + sf.No + "已经存在.";
		}

		sf.Name = this.GetValFromFrmByKey("Name");
		sf.FK_SFDBSrc = this.GetValFromFrmByKey("FK_DBSrc");
		sf.SrcTable = this.GetValFromFrmByKey("SrcTable");
		sf.CodeStruct = (CodeStruct) this.GetValIntFromFrmByKey("CodeStruct");
		sf.ColumnValue = this.GetValFromFrmByKey("ColumnValue");
		sf.ColumnText = this.GetValFromFrmByKey("ColumnText");
		if (sf.CodeStruct == CodeStruct.Tree)
		{
			sf.ParentValue = this.GetValFromFrmByKey("ParentValue");
			sf.DefVal = this.GetValFromFrmByKey("RootValue");
		}
		sf.SelectStatement = this.GetValFromFrmByKey("Selectstatement");
		sf.SrcType = SrcType.TableOrView;
		sf.FK_Val = "FK_" + sf.No;
		sf.Save();

		return "保存成功！";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion xxx 界面方法.

}