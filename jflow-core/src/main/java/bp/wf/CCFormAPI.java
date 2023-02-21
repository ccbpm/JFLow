package bp.wf;

import bp.difference.handler.CommonUtils;
import bp.pub.PubClass;
import bp.wf.*;
import bp.da.*;
import bp.port.*;
import bp.web.*;
import bp.wf.template.*;
import bp.en.*;
import bp.sys.*;
import bp.difference.*;
import bp.wf.template.frm.*;
import bp.*;

import java.util.ArrayList;
import java.util.Enumeration;

/** 
 表单引擎api
*/
public class CCFormAPI extends Dev2Interface
{
	/** 
	 生成报表
	 
	 param templeteFilePath 模版路径
	 param ds 数据源
	 @return 生成单据的路径
	*/
	public static void Frm_GenerBill(String templeteFullFile, String saveToDir, String saveFileName, PrintFileType fileType, DataSet ds, String fk_mapData) throws Exception {
		MapData md = new MapData(fk_mapData);
		GEEntity entity = md.GenerGEEntityByDataSet(ds);

		bp.pub.RTFEngine rtf = new bp.pub.RTFEngine();
		rtf.getHisEns().clear();
		rtf.getEnsDataDtls().clear();

		rtf.getHisEns().AddEntity(entity);
		ArrayList dtls = entity.getDtls();

		for (Object item : dtls)
		{
			rtf.getEnsDataDtls().add(item);
		}

		rtf.MakeDoc(templeteFullFile, saveToDir, saveFileName, null);
	}

	/** 
	 仅获取表单数据
	 
	 param enName
	 param pkval
	 param atParas
	 param specDtlFrmID
	 @return 
	*/

	private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(String enName, Object pkval, String atParas) throws Exception {
		return GenerDBForVSTOExcelFrmModelOfEntity(enName, pkval, atParas, null);
	}

//ORIGINAL LINE: private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(string enName, object pkval, string atParas, string specDtlFrmID = null)
	private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(String enName, Object pkval, String atParas, String specDtlFrmID) throws Exception {
		DataSet myds = new DataSet();

		// 创建实体..

			///#region 主表

		Entity en = ClassFactory.GetEn(enName);
		en.setPKVal(pkval);

		// if (DataType.IsNullOrEmpty(pkval)==false)
		en.Retrieve();


		//设置外部传入的默认值.
		if (SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			//2019-07-25 zyt改造
			for (String k : CommonUtils.getRequest().getParameterMap().keySet())
			{
				en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
			}
		}

		//主表数据放入集合.
		DataTable mainTable = en.ToDataTableField("Main");
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);


			///#region 主表 Sys_MapData
		String sql = "SELECT * FROM Sys_MapData WHERE 1=2 ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";

		Map map = en.getEnMapInTime();
		DataRow dr = dt.NewRow();
		dr.setValue(MapDataAttr.No, enName);
		dr.setValue(MapDataAttr.Name, map.getEnDesc());
		dr.setValue(MapDataAttr.PTable, map.getPhysicsTable());
		dt.Rows.add(dr);
		myds.Tables.add(dt);

			///#endregion 主表 Sys_MapData


			///#region 主表 Sys_MapAttr
		sql = "SELECT * FROM Sys_MapAttr WHERE 1=2 ";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		for (Attr attr : map.getAttrs())
		{
			dr = dt.NewRow();
			dr.setValue(MapAttrAttr.MyPK, enName + "_" + attr.getKey());
			dr.setValue(MapAttrAttr.Name, attr.getDesc());

			dr.setValue(MapAttrAttr.MyDataType, attr.getMyDataType()); //数据类型.
			dr.setValue(MapAttrAttr.MinLen, attr.getMinLength()); //最小长度.
			dr.setValue(MapAttrAttr.MaxLen, attr.getMaxLength()); //最大长度.

			// 设置他的逻辑类型.
			dr.setValue(MapAttrAttr.LGType, 0); //逻辑类型.
			switch (attr.getMyFieldType())
			{
				case Enum:
					dr.setValue(MapAttrAttr.LGType, 1);
					dr.setValue(MapAttrAttr.UIBindKey, attr.getUIBindKey());

					//增加枚举字段.
					if (myds.contains(attr.getUIBindKey()) == false)
					{
						String mysql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + attr.getUIBindKey() + "' ORDER BY IntKey ";
						DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
						dtEnum.TableName = attr.getUIBindKey();
						myds.Tables.add(dtEnum);
					}

					break;
				case FK:
					dr.setValue(MapAttrAttr.LGType, 2);

					Entities ens = attr.getHisFKEns();
					dr.setValue(MapAttrAttr.UIBindKey, ens.toString());

					//把外键字段也增加进去.
					if (myds.contains(ens.toString()) == false && attr.getUIIsReadonly() == false)
					{
						ens.RetrieveAll();
						DataTable mydt = ens.ToDataTableDescField("dt");
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

			///#endregion 主表 Sys_MapAttr


			///#region //主表 Sys_MapExt 扩展属性
		////主表的配置信息.
		//sql = "SELECT * FROM Sys_MapExt WHERE 1=2";
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "Sys_MapExt";
		//myds.Tables.add(dt);

			///#endregion //主表 Sys_MapExt 扩展属性


			///#endregion


			///#region 从表
		for (EnDtl item : map.getDtls())
		{

				///#region  把从表的数据放入集合.

			Entities dtls = item.getEns();

			QueryObject qo = qo = new QueryObject(dtls);

			if (dtls.toString().contains("CYSheBeiUse") == true)
			{
				qo.addOrderBy("RDT"); //按照日期进行排序，不用也可以.
			}

			qo.AddWhere(item.getRefKey(), pkval);
			DataTable dtDtl = qo.DoQueryToTable();

			dtDtl.TableName = item.getEnsName(); //修改明细表的名称.
			myds.Tables.add(dtDtl); //加入这个明细表.


				///#endregion 把从表的数据放入.


				///#region 从表 Sys_MapDtl (相当于mapdata)

			Entity dtl = dtls.getGetNewEntity();
			map = dtl.getEnMap();

			//明细表的 描述 . 
			sql = "SELECT * FROM Sys_MapDtl WHERE 1=2";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_" + item.getEnsName();

			dr = dt.NewRow();
			dr.setValue(MapDtlAttr.No, item.getEnsName());
			dr.setValue(MapDtlAttr.Name, item.getDesc());
			dr.setValue(MapDtlAttr.PTable, dtl.getEnMap().getPhysicsTable());
			dt.Rows.add(dr);
			myds.Tables.add(dt);


				///#endregion 从表 Sys_MapDtl (相当于mapdata)


				///#region 明细表 Sys_MapAttr
			sql = "SELECT * FROM Sys_MapAttr WHERE 1=2";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapAttr_For_" + item.getEnsName();
			for (Attr attr : map.getAttrs())
			{
				dr = dt.NewRow();
				dr.setValue(MapAttrAttr.MyPK, enName + "_" + attr.getKey());
				dr.setValue(MapAttrAttr.Name, attr.getDesc());

				dr.setValue(MapAttrAttr.MyDataType, attr.getMyDataType()); //数据类型.
				dr.setValue(MapAttrAttr.MinLen, attr.getMinLength()); //最小长度.
				dr.setValue(MapAttrAttr.MaxLen, attr.getMaxLength()); //最大长度.

				// 设置他的逻辑类型.
				dr.setValue(MapAttrAttr.LGType, 0); //逻辑类型.
				switch (attr.getMyFieldType())
				{
					case Enum:
						dr.setValue(MapAttrAttr.LGType, 1);
						dr.setValue(MapAttrAttr.UIBindKey, attr.getUIBindKey());

						//增加枚举字段.
						if (myds.contains(attr.getUIBindKey()) == false)
						{
							String mysql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + attr.getUIBindKey() + "' ORDER BY IntKey ";
							DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
							dtEnum.TableName = attr.getUIBindKey();
							myds.Tables.add(dtEnum);
						}
						break;
					case FK:
						dr.setValue(MapAttrAttr.LGType, 2);

						Entities ens = attr.getHisFKEns();
						dr.setValue(MapAttrAttr.UIBindKey, ens.toString());

						//把外键字段也增加进去.
						if (myds.contains(ens.toString()) == false && attr.getUIIsReadonly() == false)
						{
							ens.RetrieveAll();
							DataTable mydt = ens.ToDataTableDescField("dt");
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

				///#endregion 明细表 Sys_MapAttr

		}

			///#endregion

		return myds;
	}
	/** 
	 仅获取表单数据
	 
	 param frmID 表单ID
	 param pkval 主键
	 param atParas 参数
	 param specDtlFrmID 指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 @return 数据
	*/

	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, Object pkval, String atParas) throws Exception {
		return GenerDBForVSTOExcelFrmModel(frmID, pkval, atParas, null);
	}

//ORIGINAL LINE: public static DataSet GenerDBForVSTOExcelFrmModel(string frmID, object pkval, string atParas, string specDtlFrmID = null)
	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, Object pkval, String atParas, String specDtlFrmID) throws Exception {
		//如果是一个实体类.
		if (frmID.toUpperCase().contains("BP."))
		{
			// 执行map同步.
			Entities ens = ClassFactory.GetEns(frmID + "s");
			Entity myen = ens.getGetNewEntity();
			myen.DTSMapToSys_MapData(null);
			return GenerDBForVSTOExcelFrmModelOfEntity(frmID, pkval, atParas, specDtlFrmID = null);

			//上面这行代码的解释（2017-04-25）：
			//若不加上这行，代码执行到" MapData md = new MapData(frmID); ”会报错：
			//@没有找到记录[表单注册表  Sys_MapData, [ 主键=No 值=BP.LI.BZQX ]记录不存在,请与管理员联系, 或者确认输入错误.@在Entity(BP.Sys.MapData)查询期间出现错误@   在 bp.en.Entity.Retrieve() 位置 D:\ccflow\Components\BP.En30\En\Entity.cs:行号 1051
			//即使加上：
			//frmID = frmID.Substring(0, frmID.Length - 1);
			//也会出现该问题
			//2017-04-25 15:26:34：new MapData(frmID)应传入"BZQX”，但考虑到 GenerDBForVSTOExcelFrmModelOfEntity()运行稳定，暂不采用『统一执行下方代码』的方案。
		}

		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//映射实体.
		MapData md = new MapData(frmID);

		Map map = md.GenerHisMap();

		Entity en = null;
		if (map.getAttrs().contains("OID") == true)
		{
			//实体.
			GEEntity wk = new GEEntity(frmID);
			wk.setOID(Integer.parseInt(pkval.toString()));
			if (wk.RetrieveFromDBSources() == 0)
			{
				wk.Insert();
			}

			ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, wk, null);

			en = wk;
		}

		if (map.getAttrs().contains("MyPK") == true)
		{
			//实体.
			GEEntityMyPK wk = new GEEntityMyPK(frmID);
			wk.setMyPK(pkval.toString());
			if (wk.RetrieveFromDBSources() == 0)
			{
				wk.Insert();
			}
			ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, wk, null);
			en = wk;
		}

		//加载事件.

		//把参数放入到 En 的 Row 里面。
		if (DataType.IsNullOrEmpty(atParas) == false)
		{
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet())
			{
				switch (key)
				{
					case "FrmID":
					case "FK_MapData":
						continue;
					default:
						break;
				}

				if (en.getRow().containsKey(key) == true) //有就该变.
				{
					en.getRow().SetValByKey(key, ap.GetValStrByKey(key));
				}
				else
				{
					en.getRow().put(key, ap.GetValStrByKey(key)); //增加他.
				}
			}
		}

		//属性.
		MapExt me = null;
		DataTable dtMapAttr = null;
		MapExts mes = null;


			///#region 表单模版信息.（含主、从表的，以及从表的枚举/外键相关数据）.
		//增加表单字段描述.
		String sql = "SELECT * FROM Sys_MapData WHERE No='" + frmID + "' ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		myds.Tables.add(dt);

		//增加表单字段描述.
		sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + frmID + "' ";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		myds.Tables.add(dt);

		//增加从表信息.
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData='" + frmID + "' ";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapDtl";
		myds.Tables.add(dt);


		//主表的配置信息.
		sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + frmID + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		myds.Tables.add(dt);


			///#region 加载 从表表单模版信息.（含 从表的枚举/外键相关数据）
		for (MapDtl item : md.getMapDtls().ToJavaList())
		{

				///#region 返回指定的明细表的数据.
			if (DataType.IsNullOrEmpty(specDtlFrmID) == true)
			{
			}
			else
			{
				if (!item.getNo().equals(specDtlFrmID))
				{
					continue;
				}
			}

				///#endregion 返回指定的明细表的数据.

			//明细表的主表描述
			sql = "SELECT * FROM Sys_MapDtl WHERE No='" + item.getNo() + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_" + (DataType.IsNullOrEmpty(item.getAlias()) ? item.getNo() : item.getAlias());
			myds.Tables.add(dt);

			//明细表的表单描述
			sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + item.getNo() + "'";
			dtMapAttr = DBAccess.RunSQLReturnTable(sql);
			dtMapAttr.TableName = "Sys_MapAttr_For_" + (DataType.IsNullOrEmpty(item.getAlias()) ? item.getNo() : item.getAlias());
			myds.Tables.add(dtMapAttr);

			//明细表的配置信息.
			sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + item.getNo() + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapExt_For_" + (DataType.IsNullOrEmpty(item.getAlias()) ? item.getNo() : item.getAlias());
			myds.Tables.add(dt);


				///#region 从表的 外键表/枚举
			mes = new MapExts(item.getNo());
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.getValue("LGType").toString();
				//不是枚举/外键字段
				if (lgType.equals("0"))
				{
					continue;
				}

				String uiBindKey = dr.getValue("UIBindKey").toString();
				String mypk = dr.getValue("MyPK").toString();


					///#region 枚举字段
				if (lgType.equals("1"))
				{
					// 如果是枚举值, 判断是否存在.
					if (myds.contains(uiBindKey) == true)
					{
						continue;
					}

					String mysql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
					DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
					dtEnum.TableName = uiBindKey;
					myds.Tables.add(dtEnum);
					continue;
				}

					///#endregion

				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) //字段未启用
				{
					continue;
				}


					///#region 外键字段
				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();


					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
				if (me != null) //有范围限制时
				{
					Object tempVar2 = me.getDoc();
					String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = Glo.DealExp(fullSQL, en, null);

					dt = DBAccess.RunSQLReturnTable(fullSQL);

					dt.TableName = mypk;
					myds.Tables.add(dt);
					continue;
				}

					///#endregion 处理下拉框数据范围.
				else //无范围限制时
				{
					// 判断是否存在.
					if (myds.contains(uiBindKey) == true)
					{
						continue;
					}

					myds.Tables.add(bp.pub.PubClass.GetDataTableByUIBineKey(uiBindKey, null));
				}

					///#endregion 外键字段
			}

				///#endregion 从表的 外键表/枚举

		}

			///#endregion 加载 从表表单模版信息.（含 从表的枚举/外键相关数据）


			///#endregion 表单模版信息.（含主、从表的，以及从表的枚举/外键相关数据）.


			///#region 主表数据
		if (SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			for (String k : CommonUtils.getRequest().getParameterMap().keySet())
			{
				en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
			}
		}

		// 执行表单事件..
		String msg = ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, en);
		if (DataType.IsNullOrEmpty(msg) == false)
		{
			throw new RuntimeException("err@错误:" + msg);
		}

		//重设默认值.
		en.ResetDefaultVal(null, null, 0);

		//执行装载填充.
		me = new MapExt();
		if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, frmID) == 1)
		{
			//执行通用的装载方法.
			MapAttrs attrs = new MapAttrs(frmID);
			MapDtls dtls = new MapDtls(frmID);
			Entity tempVar3 = Glo.DealPageLoadFull(en, me, attrs, dtls);
			en = tempVar3 instanceof GEEntity ? (GEEntity)tempVar3 : null;
		}

		//增加主表数据.
		DataTable mainTable = en.ToDataTableField(md.getNo());
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);


			///#endregion 主表数据


			///#region  从表数据
		for (MapDtl dtl : md.getMapDtls().ToJavaList())
		{

				///#region 返回指定的明细表的数据.
			if (DataType.IsNullOrEmpty(specDtlFrmID) == true)
			{
			}
			else
			{
				if (!dtl.getNo().equals(specDtlFrmID))
				{
					continue;
				}
			}

				///#endregion 返回指定的明细表的数据.

			GEDtls dtls = new GEDtls(dtl.getNo());
			QueryObject qo = null;

			if (dtl.getRefPK().equals(""))
			{
				try
				{
					qo = new QueryObject(dtls);
					switch (dtl.getDtlOpenType())
					{
						case ForEmp: // 按人员来控制.
							qo.AddWhere(GEDtlAttr.RefPK, pkval);
							qo.addAnd();
							qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
							break;
						case ForWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, pkval);
							break;
						case ForPWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + pkval));
							break;
						case ForFID: // 按流程ID来控制.
							qo.AddWhere(GEDtlAttr.FID, pkval);
							break;
					}
				}
				catch (java.lang.Exception e)
				{
					dtls.getGetNewEntity().CheckPhysicsTable();
				}
			}
			else
			{
				qo = new QueryObject(dtls);
				qo.AddWhere(dtl.getRefPK(), pkval);
			}

			//条件过滤.
			if (DataType.IsNullOrEmpty(dtl.getFilterSQLExp()) == false)
			{
				String[] strs = dtl.getFilterSQLExp().split("[=]", -1);
				qo.addAnd();
				qo.AddWhere(strs[0], strs[1]);
			}

			//排序.
			if (DataType.IsNullOrEmpty(dtl.getOrderBySQLExp()) == false)
			{
				qo.addOrderBy(dtl.getOrderBySQLExp());
			}


			//从表
			DataTable dtDtl = qo.DoQueryToTable();

			// 为明细表设置默认值.
			MapAttrs mattrs = new MapAttrs(dtl.getNo());
			for (MapAttr attr : mattrs.ToJavaList())
			{
				//处理它的默认值.
				if (attr.getDefValReal().contains("@") == false)
				{
					continue;
				}

				for (DataRow dr : dtDtl.Rows)
				{
					dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
				}
			}

			dtDtl.TableName = DataType.IsNullOrEmpty(dtl.getAlias()) ? dtl.getNo() : dtl.getAlias(); //edited by liuxc,2017-10-10.如果有别名，则使用别名，没有则使用No
			myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
		}

			///#endregion 从表数据


			///#region 主表的 外键表/枚举
		dtMapAttr = myds.GetTableByName("Sys_MapAttr");
		mes = md.getMapExts();
		for (DataRow dr : dtMapAttr.Rows)
		{
			String uiBindKey = dr.getValue("UIBindKey") instanceof String ? (String)dr.getValue("UIBindKey") : null;
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue;
			}

			String myPK = dr.getValue("MyPK").toString();
			String lgType = dr.getValue("LGType").toString();

			if (lgType.equals("1"))
			{
				// 如果是枚举值, 判断是否存在., 
				if (myds.contains(uiBindKey) == true)
				{
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;
				myds.Tables.add(dtEnum);
				continue;
			}

			if (lgType.equals("2") == false)
			{
				continue;
			}

			String UIIsEnable = dr.getValue("UIIsEnable").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();


				///#region 处理下拉框数据范围. for 小杨.
			Object tempVar4 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar4 instanceof MapExt ? (MapExt)tempVar4 : null;
			if (me != null)
			{
				Object tempVar5 = me.getDoc();
				String fullSQL = tempVar5 instanceof String ? (String)tempVar5 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = Glo.DealExp(fullSQL, en, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				dt.TableName = myPK; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				myds.Tables.add(dt);
				continue;
			}

				///#endregion 处理下拉框数据范围.

			dt = bp.pub.PubClass.GetDataTableByUIBineKey(uiBindKey, null);
			dt.TableName = uiBindKey;
			myds.Tables.add(dt);
		}

			///#endregion 主表的 外键表/枚举


		String name = "";
		for (DataTable item : myds.Tables)
		{
			name += item.TableName + ",";
		}
		//返回生成的dataset.
		return myds;
	}
	/** 
	 获取从表数据，用于显示dtl.htm 
	 
	 param frmID 表单ID
	 param pkval 主键
	 param atParas 参数
	 param specDtlFrmID 指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 @return 数据
	*/
	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas) throws Exception
	{
		return GenerDBForCCFormDtl(frmID, dtl, pkval, atParas, "0",0);
	}


	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas, String dtlRefPKVal,long fid) throws Exception
	{
		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//实体.
		GEEntity en = new GEEntity(frmID);
		en.setOID(pkval);
		if (en.RetrieveFromDBSources() == 0)
		{
			en.Insert();
		}

		//把参数放入到 En 的 Row 里面。
		if (DataType.IsNullOrEmpty(atParas) == false)
		{
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet())
			{
				try
				{
					if (en.getRow().containsKey(key) == true) //有就该变.
						en.getRow().SetValByKey(key,ap.GetValStrByKey(key));
					else
						en.getRow().put(key, ap.GetValStrByKey(key)); //增加他.
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException(key);
				}
			}
		}
		if (SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			Enumeration enu = bp.sys.Glo.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
			}

		}



		///加载从表表单模版信息.

		DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
		myds.Tables.add(Sys_MapDtl);

		//明细表的表单描述
		DataTable Sys_MapAttr = dtl.getMapAttrs().ToDataTableField("Sys_MapAttr");
		myds.Tables.add(Sys_MapAttr);

		//明细表的配置信息.
		DataTable Sys_MapExt = dtl.getMapExts().ToDataTableField("Sys_MapExt");
		myds.Tables.add(Sys_MapExt);

		//启用附件，增加附件信息
		DataTable Sys_FrmAttachment = dtl.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		myds.Tables.add(Sys_FrmAttachment);
		/// 加载从表表单模版信息.


		///把从表的- 外键表/枚举 加入 DataSet.
		MapExts mes = dtl.getMapExts();
		MapExt me = null;

		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		for (DataRow dr : Sys_MapAttr.Rows)
		{
			String lgType = dr.getValue("LGType").toString();
			String ctrlType = dr.getValue(MapAttrAttr.UIContralType).toString();

			//没有绑定外键
			String uiBindKey = dr.getValue("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue;
			}

			String mypk = dr.getValue("MyPK").toString();


			///枚举字段
			if (lgType.equals("1") == true)
			{
				// 如果是枚举值, 判断是否存在.
				if (myds.GetTableByName(uiBindKey) !=null)
				{
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;

				dtEnum.Columns.get(0).ColumnName = "No";
				dtEnum.Columns.get(1).ColumnName = "Name";

				myds.Tables.add(dtEnum);
				continue;
			}

			///

			String UIIsEnable = dr.getValue("UIIsEnable").toString();
			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();


			///处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
			if (me != null && myds.GetTableByName(uiBindKey) ==null) //是否存在.
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
				fullSQL = fullSQL.replace("~", "'");
				fullSQL = bp.wf.Glo.DealExp(fullSQL, en, null);

				if (DataType.IsNullOrEmpty(fullSQL) == true)
				{
					throw new RuntimeException("err@没有给AutoFullDLL配置SQL：MapExt：=" + me.getMyPK() + ",原始的配置SQL为:" + me.getDoc());
				}

				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);

				dt.TableName = uiBindKey;

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle
						|| SystemConfig.getAppCenterDBType().equals(DBType.KingBaseR3)
						|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
				{
					if (dt.Columns.contains("NO") == true)
					{
						dt.Columns.get("NO").ColumnName = "No";
					}
					if (dt.Columns.contains("NAME") == true)
					{
						dt.Columns.get("NAME").ColumnName = "Name";
					}
					if (dt.Columns.contains("PARENTNO") == true)
					{
						dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
					}
				}

				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					if (dt.Columns.contains("no") == true)
					{
						dt.Columns.get("no").ColumnName = "No";
					}
					if (dt.Columns.contains("name") == true)
					{
						dt.Columns.get("name").ColumnName = "Name";
					}
					if (dt.Columns.contains("parentno") == true)
					{
						dt.Columns.get("parentno").ColumnName = "ParentNo";
					}
				}

				myds.Tables.add(dt);
				continue;
			}

			/// 处理下拉框数据范围.


			///外键字段

			// 判断是否存在.
			if (myds.GetTableByName(uiBindKey) !=null)
			{
				continue;
			}

			// 获得数据.
			DataTable mydt = PubClass.GetDataTableByUIBineKey(uiBindKey,en.getRow());

			if (mydt == null)
			{
				DataRow ddldr = ddlTable.NewRow();
				ddldr.setValue("No", uiBindKey);
				ddlTable.Rows.add(ddldr);
			}
			else
			{
				myds.Tables.add(mydt);
			}

			/// 外键字段
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);

		/// 把从表的- 外键表/枚举 加入 DataSet.


		///把主表数据放入.

		//重设默认值.
		en.ResetDefaultVal();


		//增加主表数据.
		DataTable mainTable = en.ToDataTableField(frmID);
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

		/// 把主表数据放入.


		/// 把从表的数据放入.
		GEDtls dtls = new GEDtls(dtl.getNo());
		DataTable dtDtl = GetDtlInfo(dtl,  en,dtlRefPKVal);

		//从表数据的填充
		if (dtDtl.Rows.size() == 0 && DataType.IsNullOrEmpty(dtl.getInitDBAttrs()) == false)
		{
			String[] keys = dtl.getInitDBAttrs().split(",");
			GEDtl endtl = null;
			MapAttr attr = null;
			for(String keyOfEn : keys)
			{
				Entity ent  = dtl.getMapAttrs().GetEntityByKey(dtl.getNo() + "_" + keyOfEn);
				if (ent == null)
					continue;
				attr =(MapAttr) ent ;
				if (DataType.IsNullOrEmpty(attr.getUIBindKey()) == true)
					continue;
				DataTable dt = null;
				//枚举字段
				if(attr.getLGType() == FieldTypeS.Enum && attr.getMyDataType() == DataType.AppInt)
					dt = myds.GetTableByName(attr.getUIBindKey());
				//外键、外部数据源
				if ((attr.getLGType() == FieldTypeS.FK && attr.getMyDataType() == DataType.AppString)
						|| (attr.getLGType() == FieldTypeS.Normal && attr.getMyDataType() == DataType.AppString && attr.getUIContralType() == UIContralType.DDL))
					dt = myds.GetTableByName(attr.getUIBindKey());;
				if (dt == null)
					continue;
				for(DataRow dr : dt.Rows)
				{
					endtl = new GEDtl(dtl.getNo());
					endtl.ResetDefaultVal();
					endtl.SetValByKey(keyOfEn, dr.getValue("No"));
					endtl.setRefPK(dtlRefPKVal);
					endtl.setFID(fid);
					endtl.Insert();
				}

			}
			dtDtl = GetDtlInfo(dtl, en, dtlRefPKVal);
		}

		// 为明细表设置默认值.
		MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
		for (MapAttr attr : dtlAttrs.ToJavaList())
		{
			if (attr.getUIContralType()== UIContralType.TB)
				continue;


			//处理它的默认值.
			if (attr.getDefValReal().contains("@") == false)
				continue;

			for (DataRow dr : dtDtl.Rows)
			{
				if (dr.getValue(attr.getKeyOfEn()) == null || DataType.IsNullOrEmpty(dr.getValue(attr.getKeyOfEn()).toString()) == true)
				{
					dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
				}
			}

		}

		dtDtl.TableName = "DBDtl"; //修改明细表的名称.
		myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.

		/// 把从表的数据放入.


		//放入一个空白的实体，用与获取默认值.
		GEDtl dtlBlank = dtls.getGetNewEntity() instanceof GEDtl ? (GEDtl)dtls.getGetNewEntity() : null;
		dtlBlank.ResetDefaultVal();

		myds.Tables.add(dtlBlank.ToDataTableField("Blank"));

		// myds.WriteXml("c:\\xx.xml");

		return myds;
	}
	public static DataTable GetDtlInfo(MapDtl dtl, GEEntity en,String dtlRefPKVal) throws Exception {
		return GetDtlInfo(dtl, en, dtlRefPKVal, false);
	}
	public static DataTable GetDtlInfo(MapDtl dtl, GEEntity en,String dtlRefPKVal,boolean isReload) throws Exception
	{
		QueryObject qo = null;
		GEDtls dtls = new GEDtls(dtl.getNo());
		try
		{

			qo = new QueryObject(dtls);
			switch (dtl.getDtlOpenType())
			{
				case ForEmp: // 按人员来控制.
					qo.AddWhere(GEDtlAttr.RefPK, dtlRefPKVal);
					qo.addAnd();
					qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
					break;
				case ForWorkID: // 按工作ID来控制
					//qo.addLeftBracket();
					qo.AddWhere(GEDtlAttr.RefPK, dtlRefPKVal);
					/*qo.addOr();
					qo.AddWhere(GEDtlAttr.FID, pkval);
					qo.addRightBracket();*/

					break;
				case ForFID: // 按工作ID来控制
					qo.AddWhere(GEDtlAttr.FID, dtlRefPKVal);
					break;
				default:
					qo.AddWhere(GEDtlAttr.RefPK, dtlRefPKVal);
					break;
			}
			//条件过滤.
			if ( DataType.IsNullOrEmpty( dtl.getFilterSQLExp()) ==false )
			{
				String exp=dtl.getFilterSQLExp();
				exp=Glo.DealExp(exp, en);

				exp = exp.replace("''", "'");

				if (exp.substring(0, 5).toLowerCase().contains("and") == false)
					exp = " AND " + exp;
				qo.setSQL(exp);

			}

			//增加排序.
			//排序.
			if (DataType.IsNullOrEmpty(dtl.getOrderBySQLExp()) == false)
			{
				qo.addOrderBy(dtl.getOrderBySQLExp());
			}
			else
			{
				//增加排序.
				qo.addOrderBy(GEDtlAttr.OID);
			}
			qo.DoQuery();
			return dtls.ToDataTableField();
		}
		catch (RuntimeException ex)
		{   if (isReload == false)
			return GetDtlInfo(dtl, en, dtlRefPKVal, true);
		else
			throw new Exception("获取从表[" + dtl.getName() + "]失败,错误:" + ex.getMessage());

		}

	}
}