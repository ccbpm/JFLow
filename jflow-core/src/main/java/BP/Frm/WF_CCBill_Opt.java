package BP.Frm;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.En.*;
import BP.WF.Data.*;

/**
 * 页面功能实体
 */
public class WF_CCBill_Opt extends WebContralBase {

	/**
	 * 构造函数
	 */
	public WF_CCBill_Opt() {
	}

	/**
	 * 设置父子关系.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String RefBill_Done() throws Exception {
		try {
			String frmID = this.GetRequestVal("FrmID");
			long workID = this.GetRequestValInt64("WorkID");
			GERpt rpt = new GERpt(frmID, workID);

			String pFrmID = this.GetRequestVal("PFrmID");
			long pWorkID = this.GetRequestValInt64("PWorkID");

			// 把数据copy到当前的子表单里.
			GERpt rptP = new GERpt(pFrmID, pWorkID);
			rpt.Copy(rptP);
			rpt.setPWorkID(pWorkID);
			rpt.SetValByKey("PFrmID", pFrmID);
			rpt.Update();

			// 更新控制表,设置父子关系.
			GenerBill gbill = new GenerBill(workID);
			gbill.setPFrmID(pFrmID);
			gbill.setPWorkID(pWorkID);
			gbill.Update();
			return "执行成功";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 单据初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String RefBill_Init() throws Exception {
		DataSet ds = new DataSet();

		/// #region 查询显示的列
		MapAttrs mapattrs = new MapAttrs();
		mapattrs.Retrieve(MapAttrAttr.FK_MapData, this.getFrmID(), MapAttrAttr.Idx);

		DataRow row = null;
		DataTable dt = new DataTable("Attrs");
		dt.Columns.Add("KeyOfEn", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);
		dt.Columns.Add("LGType", Integer.class);

		// 设置标题、单据号位于开始位置

		for (MapAttr attr : mapattrs.ToJavaList()) {
			String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
			if (searchVisable.equals("0")) {
				continue;
			}
			if (attr.getUIVisible() == false) {
				continue;
			}
			row = dt.NewRow();
			row.setValue("KeyOfEn", attr.getKeyOfEn());
			row.setValue("Name", attr.getName());
			row.setValue("Width", attr.getUIWidthInt());
			row.setValue("UIContralType", attr.getUIContralType());
			row.setValue("LGType", attr.getLGType());
			dt.Rows.add(row);
		}
		ds.Tables.add(dt);

		/// #endregion 查询显示的列

		/// #region 查询语句

		MapData md = new MapData(this.getFrmID());

		GEEntitys rpts = new GEEntitys(this.getFrmID());

		Attrs attrs = rpts.getNewEntity().getEnMap().getAttrs();

		QueryObject qo = new QueryObject(rpts);

		/// #region 关键字字段.
		String keyWord = this.GetRequestVal("SearchKey");

		if (DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1) {
			qo.addLeftBracket();
			if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals("?")) {
				qo.AddWhere("Title", " LIKE ",
						SystemConfig.getAppCenterDBType() == DBType.MySQL
								? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')")
								: (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
			} else {
				qo.AddWhere("Title", " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
			}
			qo.addOr();
			if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals("?")) {
				qo.AddWhere("BillNo", " LIKE ",
						SystemConfig.getAppCenterDBType() == DBType.MySQL
								? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')")
								: ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
			} else {
				qo.AddWhere("BillNo", " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
			}

			qo.getMyParas().Add("SKey", keyWord);
			qo.addRightBracket();

		} else {
			qo.AddHD();
		}

		/// #endregion 关键字段查询

		/// #region 时间段的查询
		String dtFrom = this.GetRequestVal("DTFrom");
		String dtTo = this.GetRequestVal("DTTo");
		if (DataType.IsNullOrEmpty(dtFrom) == false) {

			// 取前一天的24：00
			if (dtFrom.trim().length() == 10) // 2017-09-30
			{
				dtFrom += " 00:00:00";
			}
			if (dtFrom.trim().length() == 16) // 2017-09-30 00:00
			{
				dtFrom += ":00";
			}

			dtFrom = DateUtils.addDay(DateUtils.parse(dtFrom, "yyyy-MM-dd"), -1) + " 24:00";

			if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1) {
				dtTo += " 24:00";
			}

			qo.addAnd();
			qo.addLeftBracket();
			qo.setSQL(" RDT>= '" + dtFrom + "'");
			qo.addAnd();
			qo.setSQL("RDT <= '" + dtTo + "'");
			qo.addRightBracket();
		}

		/// #endregion 时间段的查询

		qo.DoQuery("OID", this.getPageSize(), this.getPageIdx());

		/// #endregion

		DataTable mydt = rpts.ToDataTableField();
		mydt.TableName = "DT";

		ds.Tables.add(mydt); // 把数据加入里面.

		return BP.Tools.Json.ToJson(ds);
	}

	/// #endregion 关联单据.

}