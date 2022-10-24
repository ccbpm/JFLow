package bp.wf.httphandler;

import bp.da.DataRow;
import bp.da.DataTable;
import bp.da.DataType;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.en.*;
import bp.*;
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
	public WF_Admin_FoolFormDesigner_SFTable() throws Exception {

	}


		///#region xxx 界面 .
	/** 
	  初始化sf0. @于庆海，新方法.
	 
	 @return 
	*/
//	public final String SF0_Init() throws Exception {
//		String cl = "bp.en.Entities";
//		ArrayList al = ClassFactory.GetObjects(cl);
//
//		//定义容器.
//		DataTable dt = new DataTable();
//		dt.Columns.Add("No");
//		dt.Columns.Add("Name");
//
//		SFTables sfs = new SFTables();
//		sfs.RetrieveAll();
//
//		for (Object obj : al)
//		{
//			Entities ens = obj instanceof Entities ? (Entities)obj : null;
//			if (ens == null)
//			{
//				continue;
//			}
//
//			try
//			{
//				Entity en = ens.getGetNewEntity();
//				if (en == null)
//				{
//					continue;
//				}
//
//				if (en.getEnMap().getAttrs().contains("No") == false)
//				{
//					continue;
//				}
//
//				if (sfs.contains(ens.toString()) == true)
//				{
//					continue;
//				}
//
//				DataRow dr = dt.NewRow();
//				dr.setValue("No", ens.toString());
//
//				if (en.IsTreeEntity)
//				{
//					dr.setValue("Name", en.getEnMap().getEnDesc() + "(树结构) " + ens.toString());
//				}
//				else
//				{
//					dr.setValue("Name", en.getEnMap().getEnDesc() + " " + ens.toString());
//				}
//
//				dt.Rows.add(dr);
//			}
//			catch (java.lang.Exception e)
//			{
//
//			}
//		}
//		return bp.tools.Json.ToJson(dt);
//	}
	public final String SF0_Save() throws Exception {
		return "保存成功.";
	}

		///#endregion xxx 界面方法.


		///#region 表或者视图 .
	/** 
	  初始化sf2.
	 
	 @return 
	*/
	public final String SF2_Init() throws Exception {
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		return srcs.ToJson("dt");
	}

	public final String SF2_GetTVs() throws Exception {
		String src = this.GetRequestVal("src");

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetTables(false);

		return bp.tools.Json.ToJson(dt);
	}

//	public final String SF2_GetCols() throws Exception {
//		String src = this.GetRequestVal("src");
//		String table = this.GetRequestVal("table");
//
//		if (DataType.IsNullOrEmpty(src))
//		{
//			throw new RuntimeException("err@参数不正确");
//		}
//
//		if (DataType.IsNullOrEmpty(table))
//		{
//			return "[]";
//		}
//
//		SFDBSrc sr = new SFDBSrc(src);
//		DataTable dt = sr.GetColumns(table);
//
//		for (DataRow r : dt.Rows)
//		{
//			r.set("Name", r.get("No") + (r.get("Name") == null || r.get("Name") == DBNull.Value || DataType.IsNullOrEmpty(r.get("Name").toString()) ? "" : String.format("[%1$s]", r.get("Name"))));
//		}
//
//		return bp.tools.Json.ToJson(dt);
//	}

//	public final String SF2_Save() throws Exception {
//		SFTable sf = new SFTable();
//		sf.No = this.GetValFromFrmByKey("No");
//		if (sf.IsExits)
//		{
//			return "err@标记:" + sf.No + "已经存在.";
//		}
//
//		sf.Name = this.GetValFromFrmByKey("Name");
//		sf.FK_SFDBSrc = this.GetValFromFrmByKey("FK_DBSrc");
//		sf.SrcTable = this.GetValFromFrmByKey("SrcTable");
//		sf.CodeStruct = CodeStruct.forValue(this.GetValIntFromFrmByKey("CodeStruct"));
//		sf.ColumnValue = this.GetValFromFrmByKey("ColumnValue");
//		sf.ColumnText = this.GetValFromFrmByKey("ColumnText");
//		if (sf.CodeStruct == CodeStruct.Tree)
//		{
//			sf.ParentValue = this.GetValFromFrmByKey("ParentValue");
//			sf.DefVal = this.GetValFromFrmByKey("RootValue");
//		}
//		sf.SelectStatement = this.GetValFromFrmByKey("Selectstatement");
//		sf.SrcType = SrcType.TableOrView;
//		sf.FK_Val = "FK_" + sf.No;
//		sf.Save();
//
//		return "保存成功！";
//	}


		///#endregion xxx 界面方法.

}