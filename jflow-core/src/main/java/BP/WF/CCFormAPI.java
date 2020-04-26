package BP.WF;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import BP.DA.*;
import BP.Difference.ContextHolderUtils;
import BP.Difference.SystemConfig;
import BP.Web.*;
import BP.WF.Template.*;
import BP.En.*;
import BP.Sys.*;

/**
 * 表单引擎api
 */
public class CCFormAPI extends Dev2Interface {
	/**
	 * 生成报表
	 * 
	 * @param templeteFullFile
	 *            模版路径
	 * @param ds
	 *            数据源
	 * @return 生成单据的路径
	 * @throws Exception
	 */
	public static void Frm_GenerBill(String templeteFullFile, String saveToDir, String saveFileName,
			BillFileType fileType, DataSet ds, String fk_mapData) throws Exception {

		MapData md = new MapData(fk_mapData);
		GEEntity entity = md.GenerGEEntityByDataSet(ds);

		BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();
		rtf.getHisEns().clear();
		rtf.getEnsDataDtls().clear();

		rtf.getHisEns().AddEntity(entity);
		ArrayList dtls = entity.getDtls();

		for (Object item : dtls) {
			rtf.getEnsDataDtls().add(item);
		}

		rtf.MakeDoc(templeteFullFile, saveToDir, saveFileName, false);
	}

	/**
	 * 仅获取表单数据
	 * 
	 * @param enName
	 * @param pkval
	 * @param atParas
	 * @return
	 * @throws Exception
	 */

	private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(String enName, Object pkval, String atParas)
			throws Exception {
		return GenerDBForVSTOExcelFrmModelOfEntity(enName, pkval, atParas, null);
	}

	private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(String enName, Object pkval, String atParas,
			String specDtlFrmID) throws Exception {
		DataSet myds = new DataSet();

		// 创建实体..

		/// #region 主表

		Entity en = BP.En.ClassFactory.GetEn(enName);
		en.setPKVal(pkval);

		en.Retrieve();

		// 设置外部传入的默认值.
		if (SystemConfig.getIsBSsystem() == true) {
			// 处理传递过来的参数。
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				en.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}
		}

		// 主表数据放入集合.
		DataTable mainTable = en.ToDataTableField("MainTable");
		myds.Tables.add(mainTable);

		/// #region 主表 Sys_MapData
		String sql = "SELECT * FROM Sys_MapData WHERE 1=2 ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";

		Map map = en.getEnMapInTime();
		DataRow dr = dt.NewRow();
		dr.setValue(MapDataAttr.No, enName);
		dr.setValue(MapDataAttr.Name, map.getEnDesc());
		dr.setValue(MapDataAttr.PTable, map.getPhysicsTable());
		dt.Rows.add(dr);
		myds.Tables.add(dt);

		/// #endregion 主表 Sys_MapData

		/// #region 主表 Sys_MapAttr
		sql = "SELECT * FROM Sys_MapAttr WHERE 1=2 ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		for (Attr attr : map.getAttrs()) {
			dr = dt.NewRow();
			dr.setValue(MapAttrAttr.MyPK, enName + "_" + attr.getKey());
			dr.setValue(MapAttrAttr.Name, attr.getDesc());

			dr.setValue(MapAttrAttr.MyDataType, attr.getMyDataType()); // 数据类型.
			dr.setValue(MapAttrAttr.MinLen, attr.getMinLength()); // 最小长度.
			dr.setValue(MapAttrAttr.MaxLen, attr.getMaxLength()); // 最大长度.

			// 设置他的逻辑类型.
			dr.setValue(MapAttrAttr.LGType, 0); // 逻辑类型.
			switch (attr.getMyFieldType()) {
			case Enum:
				dr.setValue(MapAttrAttr.LGType, 1);
				dr.setValue(MapAttrAttr.UIBindKey, attr.getUIBindKey());

				// 增加枚举字段.
				if (myds.Tables.contains(attr.getUIBindKey()) == false) {
					String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='"
							+ attr.getUIBindKey() + "' ORDER BY IntKey ";
					DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
					dtEnum.TableName = attr.getUIBindKey();
					myds.Tables.add(dtEnum);
				}

				break;
			case FK:
				dr.setValue(MapAttrAttr.LGType, 2);

				Entities ens = attr.getHisFKEns();
				dr.setValue(MapAttrAttr.UIBindKey, ens.toString());

				// 把外键字段也增加进去.
				if (myds.Tables.contains(ens.toString()) == false && attr.getUIIsReadonly() == false) {
					ens.RetrieveAll();
					DataTable mydt = ens.ToDataTableDescField();
					mydt.TableName = ens.toString();
					myds.Tables.add(mydt);
				}
				break;
			default:
				break;
			}

			// 设置控件类型.
			dr.setValue(MapAttrAttr.UIContralType, attr.getUIContralType().getValue());
			dt.Rows.add(dr);
		}
		myds.Tables.add(dt);

		/// #region 从表
		for (EnDtl item : map.getDtls()) {

			/// #region 把从表的数据放入集合.

			Entities dtls = item.getEns();

			QueryObject qo = qo = new QueryObject(dtls);

			if (dtls.toString().contains("CYSheBeiUse") == true) {
				qo.addOrderBy("RDT"); // 按照日期进行排序，不用也可以.
			}

			qo.AddWhere(item.getRefKey(), pkval);
			DataTable dtDtl = qo.DoQueryToTable();

			dtDtl.TableName = item.getEnsName(); // 修改明细表的名称.
			myds.Tables.add(dtDtl); // 加入这个明细表.

			Entity dtl = dtls.getNewEntity();
			map = dtl.getEnMap();

			// 明细表的 描述 .
			sql = "SELECT * FROM Sys_MapDtl WHERE 1=2";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_" + item.getEnsName();

			dr = dt.NewRow();
			dr.setValue(MapDtlAttr.No, item.getEnsName());
			dr.setValue(MapDtlAttr.Name, item.getDesc());
			dr.setValue(MapDtlAttr.PTable, dtl.getEnMap().getPhysicsTable());
			dt.Rows.add(dr);
			myds.Tables.add(dt);

			/// #region 明细表 Sys_MapAttr
			sql = "SELECT * FROM Sys_MapAttr WHERE 1=2";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapAttr_For_" + item.getEnsName();
			for (Attr attr : map.getAttrs()) {
				dr = dt.NewRow();
				dr.setValue(MapAttrAttr.MyPK, enName + "_" + attr.getKey());
				dr.setValue(MapAttrAttr.Name, attr.getDesc());

				dr.setValue(MapAttrAttr.MyDataType, attr.getMyDataType()); // 数据类型.
				dr.setValue(MapAttrAttr.MinLen, attr.getMinLength()); // 最小长度.
				dr.setValue(MapAttrAttr.MaxLen, attr.getMaxLength()); // 最大长度.

				// 设置他的逻辑类型.
				dr.setValue(MapAttrAttr.LGType, 0); // 逻辑类型.
				switch (attr.getMyFieldType()) {
				case Enum:
					dr.setValue(MapAttrAttr.LGType, 1);
					dr.setValue(MapAttrAttr.UIBindKey, attr.getUIBindKey());

					// 增加枚举字段.
					if (myds.Tables.contains(attr.getUIBindKey()) == false) {
						String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='"
								+ attr.getUIBindKey() + "' ORDER BY IntKey ";
						DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
						dtEnum.TableName = attr.getUIBindKey();
						myds.Tables.add(dtEnum);
					}
					break;
				case FK:
					dr.setValue(MapAttrAttr.LGType, 2);

					Entities ens = attr.getHisFKEns();
					dr.setValue(MapAttrAttr.UIBindKey, ens.toString());

					// 把外键字段也增加进去.
					if (myds.Tables.contains(ens.toString()) == false && attr.getUIIsReadonly() == false) {
						ens.RetrieveAll();
						DataTable mydt = ens.ToDataTableDescField();
						mydt.TableName = ens.toString();
						myds.Tables.add(mydt);
					}
					break;
				default:
					break;
				}

				// 设置控件类型.
				dr.setValue(MapAttrAttr.UIContralType, attr.getUIContralType().getValue());
				dt.Rows.add(dr);
			}
			myds.Tables.add(dt);

			/// #endregion 明细表 Sys_MapAttr

		}

		/// #endregion

		return myds;
	}

	/**
	 * 仅获取表单数据
	 * 
	 * @param frmID
	 *            表单ID
	 * @param pkval
	 *            主键
	 * @param atParas
	 *            参数
	 * @param specDtlFrmID
	 *            指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 * @return 数据
	 * @throws Exception 
	 */

	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, Object pkval, String atParas) throws Exception {
		return GenerDBForVSTOExcelFrmModel(frmID, pkval, atParas, null);
	}

	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, Object pkval, String atParas, String specDtlFrmID) throws Exception {
		// 如果是一个实体类.
		if (frmID.contains("BP.")) {
			// 执行map同步.
			Entities ens = BP.En.ClassFactory.GetEns(frmID + "s");
			Entity myen = ens.getNewEntity();
			myen.DTSMapToSys_MapData(myen.getClassID());
			return GenerDBForVSTOExcelFrmModelOfEntity(frmID, pkval, atParas, specDtlFrmID = null);

			// 上面这行代码的解释（2017-04-25）：
			// 若不加上这行，代码执行到" MapData md = new MapData(frmID); "会报错：
			// @没有找到记录[表单注册表 Sys_MapData, [ 主键=No 值=BP.LI.BZQX ]记录不存在,请与管理员联系,
			// 或者确认输入错误.@在Entity(BP.Sys.MapData)查询期间出现错误@ 在
			// BP.En.Entity.Retrieve() 位置
			// D:\ccflow\Components\BP.En30\En\Entity.cs:行号 1051
			// 即使加上：
			// frmID = frmID.Substring(0, frmID.Length - 1);
			// 也会出现该问题
			// 2017-04-25 15:26:34：new MapData(frmID)应传入"BZQX"，但考虑到
			// GenerDBForVSTOExcelFrmModelOfEntity()运行稳定，暂不采用『统一执行下方代码』的方案。
		}

		// 数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		// 映射实体.
		MapData md = new MapData(frmID);

		Map map = md.GenerHisMap();

		Entity en = null;
		if (map.getAttrs().Contains("OID") == true) {
			// 实体.
			GEEntity wk = new GEEntity(frmID);
			wk.setOID(Integer.parseInt(pkval.toString()));
			if (wk.RetrieveFromDBSources() == 0) {
				wk.Insert();
			}

			md.DoEvent(FrmEventList.FrmLoadBefore, wk, null);

			en = wk;
		}

		if (map.getAttrs().Contains("MyPK") == true) {
			// 实体.
			GEEntityMyPK wk = new GEEntityMyPK(frmID);
			wk.setMyPK(pkval.toString());
			if (wk.RetrieveFromDBSources() == 0) {
				wk.Insert();
			}
			md.DoEvent(FrmEventList.FrmLoadBefore, wk, null);
			en = wk;
		}

		// 加载事件.

		// 把参数放入到 En 的 Row 里面。
		if (DataType.IsNullOrEmpty(atParas) == false) {
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet()) {
				switch (key) {
				case "FrmID":
				case "FK_MapData":
					continue;
				default:
					break;
				}

				if (en.getRow().containsKey(key) == true) // 有就该变.
				{
					en.getRow().SetValByKey(key, ap.GetValStrByKey(key));
				} else {
					en.getRow().put(key, ap.GetValStrByKey(key)); // 增加他.
				}
			}
		}

		// 属性.
		MapExt me = null;
		DataTable dtMapAttr = null;
		MapExts mes = null;

		/// #region 表单模版信息.（含主、从表的，以及从表的枚举/外键相关数据）.
		// 增加表单字段描述.
		String sql = "SELECT * FROM Sys_MapData WHERE No='" + frmID + "' ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		myds.Tables.add(dt);

		// 增加表单字段描述.
		sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + frmID + "' ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		myds.Tables.add(dt);

		// 增加从表信息.
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData='" + frmID + "' ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapDtl";
		myds.Tables.add(dt);

		// 主表的配置信息.
		sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + frmID + "'";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		myds.Tables.add(dt);

		/// #region 加载 从表表单模版信息.（含 从表的枚举/外键相关数据）
		for (MapDtl item : md.getMapDtls().ToJavaList()) {

			/// #region 返回指定的明细表的数据.
			if (DataType.IsNullOrEmpty(specDtlFrmID) == true) {
			} else {
				if (!specDtlFrmID.equals(item.getNo())) {
					continue;
				}
			}

			/// #endregion 返回指定的明细表的数据.

			// 明细表的主表描述
			sql = "SELECT * FROM Sys_MapDtl WHERE No='" + item.getNo() + "'";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_"
					+ (DataType.IsNullOrEmpty(item.getAlias()) ? item.getNo() : item.getAlias());
			myds.Tables.add(dt);

			// 明细表的表单描述
			sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + item.getNo() + "'";
			dtMapAttr = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtMapAttr.TableName = "Sys_MapAttr_For_"
					+ (DataType.IsNullOrEmpty(item.getAlias()) ? item.getNo() : item.getAlias());
			myds.Tables.add(dtMapAttr);

			// 明细表的配置信息.
			sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + item.getNo() + "'";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapExt_For_"
					+ (DataType.IsNullOrEmpty(item.getAlias()) ? item.getNo() : item.getAlias());
			myds.Tables.add(dt);

			/// #region 从表的 外键表/枚举
			mes = new MapExts(item.getNo());
			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.getValue("LGType").toString();
				// 不是枚举/外键字段
				if (lgType.equals("0")) {
					continue;
				}

				String uiBindKey = dr.getValue("UIBindKey").toString();
				String mypk = dr.getValue("MyPK").toString();

				/// #region 枚举字段
				if (lgType.equals("1")) {
					// 如果是枚举值, 判断是否存在.
					if (myds.Tables.contains(uiBindKey) == true) {
						continue;
					}

					String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey
							+ "' ORDER BY IntKey ";
					DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
					dtEnum.TableName = uiBindKey;
					myds.Tables.add(dtEnum);
					continue;
				}

				/// #endregion

				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) // 字段未启用
				{
					continue;
				}

				/// #region 外键字段
				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();

				/// #region 处理下拉框数据范围. for 小杨.
				Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL,
						MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar instanceof MapExt ? (MapExt) tempVar : null;
				if (me != null) // 有范围限制时
				{
					Object tempVar2 = me.getDoc();
					String fullSQL = tempVar2 instanceof String ? (String) tempVar2 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);

					dt = DBAccess.RunSQLReturnTable(fullSQL);

					dt.TableName = mypk;
					myds.Tables.add(dt);
					continue;
				}

				/// #endregion 处理下拉框数据范围.
				else // 无范围限制时
				{
					// 判断是否存在.
					if (myds.Tables.contains(uiBindKey) == true) {
						continue;
					}

					myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
				}

				/// #endregion 外键字段
			}

			/// #endregion 从表的 外键表/枚举

		}

		/// #endregion 加载 从表表单模版信息.（含 从表的枚举/外键相关数据）

		/// #endregion 表单模版信息.（含主、从表的，以及从表的枚举/外键相关数据）.

		/// #region 主表数据
		if (SystemConfig.getIsBSsystem() == true) {
			// 处理传递过来的参数。
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				en.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}

		}

		// 执行表单事件..
		String msg = md.DoEvent(FrmEventList.FrmLoadBefore, en);
		if (DataType.IsNullOrEmpty(msg) == false) {
			throw new RuntimeException("err@错误:" + msg);
		}

		// 重设默认值.
		en.ResetDefaultVal();

		// 执行装载填充.
		me = new MapExt();
		if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, frmID) == 1) {
			// 执行通用的装载方法.
			MapAttrs attrs = new MapAttrs(frmID);
			MapDtls dtls = new MapDtls(frmID);
			Entity tempVar3 = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls);
			en = tempVar3 instanceof GEEntity ? (GEEntity) tempVar3 : null;
		}

		// 增加主表数据.
		DataTable mainTable = en.ToDataTableField(md.getNo());
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

		/// #endregion 主表数据

		/// #region 从表数据
		for (MapDtl dtl : md.getMapDtls().ToJavaList()) {

			/// #region 返回指定的明细表的数据.
			if (DataType.IsNullOrEmpty(specDtlFrmID) == true) {
			} else {
				if (!specDtlFrmID.equals(dtl.getNo())) {
					continue;
				}
			}

			/// #endregion 返回指定的明细表的数据.

			GEDtls dtls = new GEDtls(dtl.getNo());
			QueryObject qo = null;

			if (dtl.getRefPK().equals("")) {
				try {
					qo = new QueryObject(dtls);
					switch (dtl.getDtlOpenType()) {
					case ForEmp: // 按人员来控制.
						qo.AddWhere(GEDtlAttr.RefPK, pkval);
						qo.addAnd();
						qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
						break;
					case ForWorkID: // 按工作ID来控制
						qo.AddWhere(GEDtlAttr.RefPK, pkval);
						break;
					case ForFID: // 按流程ID来控制.
						qo.AddWhere(GEDtlAttr.FID, pkval);
						break;
					}
				} catch (java.lang.Exception e) {
					dtls.getNewEntity().CheckPhysicsTable();
				}
			} else {
				qo = new QueryObject(dtls);
				qo.AddWhere(dtl.getRefPK(), pkval);
			}

			// 条件过滤.
			if (!dtl.getFilterSQLExp().equals("")) {
				String[] strs = dtl.getFilterSQLExp().split("[=]", -1);
				qo.addAnd();
				qo.AddWhere(strs[0], strs[1]);
			}

			// 从表
			DataTable dtDtl = qo.DoQueryToTable();

			// 为明细表设置默认值.
			MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
			for (MapAttr attr : dtlAttrs.ToJavaList()) {
				// 处理它的默认值.
				if (attr.getDefValReal().contains("@") == false) {
					continue;
				}

				for (DataRow dr : dtDtl.Rows) {
					dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
				}
			}

			dtDtl.TableName = DataType.IsNullOrEmpty(dtl.getAlias()) ? dtl.getNo() : dtl.getAlias(); // edited
																						// by
																						// liuxc,2017-10-10.如果有别名，则使用别名，没有则使用No
			myds.Tables.add(dtDtl); // 加入这个明细表, 如果没有数据，xml体现为空.
		}

		/// #endregion 从表数据

		/// #region 主表的 外键表/枚举
		dtMapAttr = myds.GetTableByName("Sys_MapAttr");
		mes = md.getMapExts();
		for (DataRow dr : dtMapAttr.Rows) {
			String uiBindKey = dr.getValue("UIBindKey") instanceof String ? (String) dr.getValue("UIBindKey") : null;
			if (DataType.IsNullOrEmpty(uiBindKey) == true) {
				continue;
			}

			String myPK = dr.getValue("MyPK").toString();
			String lgType = dr.getValue("LGType").toString();

			if (lgType.equals("1")) {
				// 如果是枚举值, 判断是否存在.,
				if (myds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey
						+ "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;
				myds.Tables.add(dtEnum);
				continue;
			}

			if (lgType.equals("2") == false) {
				continue;
			}

			String UIIsEnable = dr.getValue("UIIsEnable").toString();
			if (UIIsEnable.equals("0")) {
//				Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper,keyOfEn);
//
//				me = tempVar instanceof MapExt ? (MapExt) tempVar : null;
//				if (me != null) // 有范围限制时
//				{
//					SFTable sftable=new SFTable(uiBindKey);
//
//					Object tempVar2 = sftable.getSelectStatement();
//					String fullSQL = tempVar2 instanceof String ? (String) tempVar2 : null;
//					fullSQL = fullSQL.replace("~", ",");
//					fullSQL = BP.WF.Glo.DealExp(fullSQL, null, null);
//
//					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
//
//					dt.TableName = uiBindKey;
//
//					dt.Columns.get(0).ColumnName = "No";
//					dt.Columns.get(1).ColumnName = "Name";
//
//					myds.Tables.add(dt);
//				}
				continue;
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();

			/// #region 处理下拉框数据范围. for 小杨.
			Object tempVar4 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper,
					keyOfEn);
			me = tempVar4 instanceof MapExt ? (MapExt) tempVar4 : null;
			if (me != null) {
				Object tempVar5 = me.getDoc();
				String fullSQL = tempVar5 instanceof String ? (String) tempVar5 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				dt.TableName = myPK; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				myds.Tables.add(dt);
				continue;
			}

			/// #endregion 处理下拉框数据范围.

			dt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
			dt.TableName = uiBindKey;
			myds.Tables.add(dt);
		}

		/// #endregion 主表的 外键表/枚举

		String name = "";
		for (DataTable item : myds.Tables) {
			name += item.TableName + ",";
		}
		// 返回生成的dataset.
		return myds;
	}

	/**
	 * 获取从表数据，用于显示dtl.htm
	 * 
	 * @param frmID
	 *            表单ID
	 * @param pkval
	 *            主键
	 * @param atParas
	 *            参数
	 * @param specDtlFrmID
	 *            指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 * @return 数据
	 * @throws Exception 
	 */

	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas) throws Exception {
		return GenerDBForCCFormDtl(frmID, dtl, pkval, atParas, 0);
	}

	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas, long fid) throws Exception {
		// 数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		// 实体.
		GEEntity en = new GEEntity(frmID);
		en.setOID(pkval);
		if (en.RetrieveFromDBSources() == 0) {
			en.Insert();
		}

		// 把参数放入到 En 的 Row 里面。
		if (DataType.IsNullOrEmpty(atParas) == false) {
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet()) {
				try {
					if (en.getRow().containsKey(key) == true) // 有就该变.
					{
						en.getRow().SetValByKey(key, ap.GetValStrByKey(key));
					} else {
						en.getRow().put(key, ap.GetValStrByKey(key)); // 增加他.
					}
				} catch (RuntimeException ex) {
					throw new RuntimeException(key);
				}
			}
		}
		/// #region 把主表数据放入.
		if (SystemConfig.getIsBSsystem() == true) {
			// 处理传递过来的参数。
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				en.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}


		}

		/// #region 加载从表表单模版信息.

		DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
		myds.Tables.add(Sys_MapDtl);

		// 明细表的表单描述
		DataTable Sys_MapAttr = dtl.getMapAttrs().ToDataTableField("Sys_MapAttr");
		myds.Tables.add(Sys_MapAttr);

		// 明细表的配置信息.
		DataTable Sys_MapExt = dtl.getMapExts().ToDataTableField("Sys_MapExt");
		myds.Tables.add(Sys_MapExt);

		/// #endregion 加载从表表单模版信息.

		/// #region 把从表的- 外键表/枚举 加入 DataSet.
		MapExts mes = dtl.getMapExts();
		MapExt me = null;

		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		Hashtable ht = null;
		boolean isFirst = true;
		for (DataRow dr : Sys_MapAttr.Rows) {
			String lgType = dr.getValue("LGType").toString();
			String ctrlType = dr.getValue(MapAttrAttr.UIContralType).toString();

			// 没有绑定外键
			String uiBindKey = dr.getValue("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true) {
				continue;
			}

			String mypk = dr.getValue("MyPK").toString();
			/// #region 枚举字段
			if (lgType.equals("1") == true) {
				// 如果是枚举值, 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey
						+ "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;

				dtEnum.Columns.get(0).ColumnName = "No";
				dtEnum.Columns.get(1).ColumnName = "Name";

				myds.Tables.add(dtEnum);
				continue;
			}

			/// #region 外键字段
			String UIIsEnable = dr.getValue("UIIsEnable").toString();
			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();

			/// #region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper,
					keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt) tempVar : null;
			if (me != null) // 有范围限制时
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = tempVar2 instanceof String ? (String) tempVar2 : null;
				fullSQL = fullSQL.replace("~", "'");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);

				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);

				dt.TableName = uiBindKey;

				dt.Columns.get(0).ColumnName = "No";
				dt.Columns.get(1).ColumnName = "Name";

				myds.Tables.add(dt);
				continue;
			}

			if ( UIIsEnable.equals("1") && myds.Tables.contains(uiBindKey)==false){
				DataTable mydt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey,en.getRow());
				mydt.TableName = uiBindKey;
				if (mydt == null) {
					DataRow ddldr = ddlTable.NewRow();
					ddldr.setValue("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				} else {
					mydt.Columns.get(0).ColumnName = "No";
					mydt.Columns.get(1).ColumnName = "Name";
					myds.Tables.add(mydt);

				}
				continue;
			}
			/// #endregion 外键字段
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);

		/// #endregion 把从表的- 外键表/枚举 加入 DataSet.

		// 重设默认值.
		en.ResetDefaultVal();

		// 增加主表数据.
		DataTable mainTable = en.ToDataTableField(frmID);
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

		/// #endregion 把主表数据放入.

		/// #region 把从表的数据放入.
		GEDtls dtls = new GEDtls(dtl.getNo());
		QueryObject qo = null;
		try {
			qo = new QueryObject(dtls);
			switch (dtl.getDtlOpenType()) {
			case ForEmp: // 按人员来控制.
				qo.AddWhere(GEDtlAttr.RefPK, pkval);
				qo.addAnd();
				qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
				break;
			case ForWorkID: // 按工作ID来控制
				qo.addLeftBracket();
				qo.AddWhere(GEDtlAttr.RefPK, String.valueOf(pkval));
				qo.addOr();
				qo.AddWhere(GEDtlAttr.FID, pkval);
				qo.addRightBracket();
				break;
			case ForFID: // 按流程ID来控制.
				if (fid == 0) {
					qo.AddWhere(GEDtlAttr.FID, pkval);
				} else {
					qo.AddWhere(GEDtlAttr.FID, fid);
				}
				break;
			}
		} catch (RuntimeException ex) {
			dtls.getNewEntity().CheckPhysicsTable();
			throw ex;
		}

		// 条件过滤.
		if (!dtl.getFilterSQLExp().equals("")) {
			String[] strs = dtl.getFilterSQLExp().split("[=]", -1);
			if (strs.length == 2) {
				qo.addAnd();
				String value = Glo.DealExp(strs[1],en);
				qo.AddWhere(strs[0], value);
			}
		}

		// 增加排序.
		// qo.addOrderByDesc( dtls.getNewEntity().PKField );

		// 从表
		DataTable dtDtl = qo.DoQueryToTable();

		// 查询所有动态SQL查询类型的字典表记录
		SFTable sftable = null;
		DataTable dtsftable = null;
		DataRow[] drs = null;

		// 为明细表设置默认值.
		MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
		for (MapAttr attr : dtlAttrs.ToJavaList()) {

			/// #region 修改区分大小写. Oracle
			if (BP.DA.DBType.Oracle == SystemConfig.getAppCenterDBType()) {
				for (DataColumn dr : dtDtl.Columns) {
					String a = attr.getKeyOfEn();
					String b = dr.ColumnName;
					if (attr.getKeyOfEn().toUpperCase().equals(dr.ColumnName)) {
						dr.ColumnName = attr.getKeyOfEn();
						continue;
					}

					if (attr.getLGType() == FieldTypeS.Enum || attr.getLGType() == FieldTypeS.FK) {
						if (dr.ColumnName.equals(attr.getKeyOfEn().toUpperCase() + "TEXT")) {
							dr.ColumnName = attr.getKeyOfEn() + "Text";
						}
					}
				}
				for (DataRow dr : dtDtl.Rows) {
					// 本身是大写的不进行修改
					if (DataType.IsNullOrEmpty(dr.getValue(attr.getKeyOfEn()) + "")) {
						dr.setValue(attr.getKeyOfEn(), dr.getValue(attr.getKeyOfEn().toUpperCase()));
						dr.setValue(attr.getKeyOfEn().toUpperCase(), null);
					}
				}
			}

			/// #endregion 修改区分大小写.

			/// #region 修改区分大小写. PostgreSQL
			if (BP.DA.DBType.PostgreSQL == SystemConfig.getAppCenterDBType()) {
				for (DataColumn dr : dtDtl.Columns) {
					String a = attr.getKeyOfEn();
					String b = dr.ColumnName;
					if (attr.getKeyOfEn().toLowerCase().equals(dr.ColumnName)) {
						dr.ColumnName = attr.getKeyOfEn();
						continue;
					}

					if (attr.getLGType() == FieldTypeS.Enum || attr.getLGType() == FieldTypeS.FK) {
						if (dr.ColumnName.equals(attr.getKeyOfEn().toLowerCase() + "TEXT")) {
							dr.ColumnName = attr.getKeyOfEn() + "Text";
						}
					}
				}
				for (DataRow dr : dtDtl.Rows) {
					// 本身是大写的不进行修改
					if (DataType.IsNullOrEmpty(dr.getValue(attr.getKeyOfEn()) + "")) {
						dr.setValue(attr.getKeyOfEn(), dr.getValue(attr.getKeyOfEn().toLowerCase()));
						dr.setValue(attr.getKeyOfEn().toLowerCase(), null);
					}
				}
			}

			/// #endregion 修改区分大小写.
			if (attr.getUIContralType() == UIContralType.TB) {
				continue;
			}

			// 处理它的默认值.
			if (attr.getDefValReal().contains("@") == false) {
				continue;
			}

			for (DataRow dr : dtDtl.Rows) {
				dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
			}
		}

		dtDtl.TableName = "DBDtl"; // 修改明细表的名称.
		myds.Tables.add(dtDtl); // 加入这个明细表, 如果没有数据，xml体现为空.

		/// #endregion 把从表的数据放入.

		// 放入一个空白的实体，用与获取默认值.
		GEDtl dtlBlank = dtls.getNewEntity() instanceof GEDtl ? (GEDtl) dtls.getNewEntity() : null;
		dtlBlank.ResetDefaultVal();

		myds.Tables.add(dtlBlank.ToDataTableField("Blank"));
		return myds;
	}
}