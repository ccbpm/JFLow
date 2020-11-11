package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import bp.wf.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_Admin_FoolFormDesigner_SFTable extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_FoolFormDesigner_SFTable()
	{

	}


		///xxx 界面 .
	/** 
	  初始化sf0. @于庆海，新方法.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SF0_Init() throws Exception
	{
		String cl = "bp.en.Entities";
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
				Entity en = ens.getGetNewEntity();
				if (en == null)
				{
					continue;
				}

				if (en.getEnMap().getAttrs().Contains("No") == false)
				{
					continue;
				}

				if (sfs.Contains(ens.toString()) == true)
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue("No", ens.toString());

				if (en.getIsTreeEntity())
				{
					dr.setValue("Name", en.getEnMap().getEnDesc() + "(树结构) " + ens.toString());
				}
				else
				{
					dr.setValue("Name", en.getEnMap().getEnDesc() + " " + ens.toString());
				}

				dt.Rows.add(dr);
			}
			catch (java.lang.Exception e)
			{

			}
		}
		return bp.tools.Json.ToJson(dt);
	}
	public final String SF0_Save()
	{
		return "保存成功.";
	}

		/// xxx 界面方法.


		///表或者视图 .
	/** 
	  初始化sf2.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SF2_Init() throws Exception
	{
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		return srcs.ToJson();
	}

	public final String SF2_GetTVs() throws Exception
	{
		String src = this.GetRequestVal("src");

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetTables();

		return bp.tools.Json.ToJson(dt);
	}

	public final String SF2_GetCols() throws Exception
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
			r.setValue("Name", r.getValue("No") + (r.getValue("Name") == null || r.getValue("Name") == null || DataType.IsNullOrEmpty(r.getValue("Name").toString()) ? "" : String.format("[%1$s]", r.getValue("Name"))));
		}

		return bp.tools.Json.ToJson(dt);
	}

	public final String SF2_Save() throws Exception
	{
		SFTable sf = new SFTable();
		sf.setNo(this.GetValFromFrmByKey("No"));
		if (sf.getIsExits())
		{
			return "err@标记:" + sf.getNo() + "已经存在.";
		}

		sf.setName(this.GetValFromFrmByKey("Name"));
		sf.setFK_SFDBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
		sf.setSrcTable(this.GetValFromFrmByKey("SrcTable"));
		sf.setCodeStruct(CodeStruct.forValue(this.GetValIntFromFrmByKey("CodeStruct")));
		sf.setColumnValue(this.GetValFromFrmByKey("ColumnValue"));
		sf.setColumnText(this.GetValFromFrmByKey("ColumnText"));
		if (sf.getCodeStruct() == CodeStruct.Tree)
		{
			sf.setParentValue(this.GetValFromFrmByKey("ParentValue"));
			sf.setDefVal(this.GetValFromFrmByKey("RootValue"));
		}
		sf.setSelectStatement(this.GetValFromFrmByKey("Selectstatement"));
		sf.setSrcType(SrcType.TableOrView);
		sf.setFK_Val("FK_" + sf.getNo());
		sf.Save();

		return "保存成功！";
	}


		/// xxx 界面方法.

}