package BP.Frm;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.WF.HttpHandler.*;
import java.time.*;

/** 
 页面功能实体
*/
public class WF_CCBill_Opt extends DirectoryPageBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法.
	/** 
	 构造函数
	*/
	public WF_CCBill_Opt()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关联单据.
	/** 
	 设置父子关系.
	 
	 @return 
	*/
	public final String RefBill_Done()
	{
		try
		{
			String frmID = this.GetRequestVal("FrmID");
			long workID = this.GetRequestValInt64("WorkID");
			GERpt rpt = new GERpt(frmID, workID);

			String pFrmID = this.GetRequestVal("PFrmID");
			long pWorkID = this.GetRequestValInt64("PWorkID");

			//把数据copy到当前的子表单里.
			GERpt rptP = new GERpt(pFrmID, pWorkID);
			rpt.Copy(rptP);
			rpt.setPWorkID(pWorkID);
			rpt.SetValByKey("PFrmID", pFrmID);
			rpt.Update();

			//更新控制表,设置父子关系.
			GenerBill gbill = new GenerBill(workID);
			gbill.setPFrmID(pFrmID);
			gbill.setPWorkID(pWorkID);
			gbill.Update();
			return "执行成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 单据初始化
	 
	 @return 
	*/
	public final String RefBill_Init()
	{
		DataSet ds = new DataSet();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询显示的列
		MapAttrs mapattrs = new MapAttrs();
		mapattrs.Retrieve(MapAttrAttr.FK_MapData, this.getFrmID(), MapAttrAttr.Idx);

		DataRow row = null;
		DataTable dt = new DataTable("Attrs");
		dt.Columns.Add("KeyOfEn", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);
		dt.Columns.Add("LGType", Integer.class);

		//设置标题、单据号位于开始位置


		for (MapAttr attr : mapattrs)
		{
			String searchVisable = attr.atPara.GetValStrByKey("SearchVisable");
			if (searchVisable.equals("0"))
			{
				continue;
			}
			if (attr.UIVisible == false)
			{
				continue;
			}
			row = dt.NewRow();
			row.set("KeyOfEn", attr.KeyOfEn);
			row.set("Name", attr.Name);
			row.set("Width", attr.UIWidthInt);
			row.set("UIContralType", attr.UIContralType);
			row.set("LGType", attr.LGType);
			dt.Rows.Add(row);
		}
		ds.Tables.Add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 查询显示的列

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询语句

		MapData md = new MapData(this.getFrmID());

		GEEntitys rpts = new GEEntitys(this.getFrmID());

		Attrs attrs = rpts.GetNewEntity.EnMap.Attrs;

		QueryObject qo = new QueryObject(rpts);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 关键字字段.
		String keyWord = this.GetRequestVal("SearchKey");

		if (DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
		{
			qo.addLeftBracket();
			if (SystemConfig.AppCenterDBVarStr.equals("@") || SystemConfig.AppCenterDBVarStr.equals("?"))
			{
				qo.AddWhere("Title", " LIKE ", SystemConfig.AppCenterDBType == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.AppCenterDBVarStr + "SKey,'%')") : (" '%'+" + SystemConfig.AppCenterDBVarStr + "SKey+'%'"));
			}
			else
			{
				qo.AddWhere("Title", " LIKE ", " '%'||" + SystemConfig.AppCenterDBVarStr + "SKey||'%'");
			}
			qo.addOr();
			if (SystemConfig.AppCenterDBVarStr.equals("@") || SystemConfig.AppCenterDBVarStr.equals("?"))
			{
				qo.AddWhere("BillNo", " LIKE ", SystemConfig.AppCenterDBType == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.AppCenterDBVarStr + "SKey,'%')") : ("'%'+" + SystemConfig.AppCenterDBVarStr + "SKey+'%'"));
			}
			else
			{
				qo.AddWhere("BillNo", " LIKE ", "'%'||" + SystemConfig.AppCenterDBVarStr + "SKey||'%'");
			}

			qo.MyParas.Add("SKey", keyWord);
			qo.addRightBracket();

		}
		else
		{
			qo.AddHD();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 关键字段查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 时间段的查询
		String dtFrom = this.GetRequestVal("DTFrom");
		String dtTo = this.GetRequestVal("DTTo");
		if (DataType.IsNullOrEmpty(dtFrom) == false)
		{

			//取前一天的24：00
			if (dtFrom.trim().length() == 10) //2017-09-30
			{
				dtFrom += " 00:00:00";
			}
			if (dtFrom.trim().length() == 16) //2017-09-30 00:00
			{
				dtFrom += ":00";
			}

			dtFrom = LocalDateTime.parse(dtFrom).AddDays(-1).toString("yyyy-MM-dd") + " 24:00";

			if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
			{
				dtTo += " 24:00";
			}

			qo.addAnd();
			qo.addLeftBracket();
			qo.SQL = " RDT>= '" + dtFrom + "'";
			qo.addAnd();
			qo.SQL = "RDT <= '" + dtTo + "'";
			qo.addRightBracket();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 时间段的查询

		qo.DoQuery("OID", this.getPageSize(), this.getPageIdx());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		DataTable mydt = rpts.ToDataTableField();
		mydt.TableName = "DT";

		ds.Tables.Add(mydt); //把数据加入里面.

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 关联单据.

}