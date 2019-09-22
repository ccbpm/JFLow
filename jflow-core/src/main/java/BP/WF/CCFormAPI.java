package BP.WF;

import BP.WF.*;
import BP.DA.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.Template.*;
import BP.En.*;
import BP.Sys.*;

/** 
 表单引擎api
*/
public class CCFormAPI extends Dev2Interface
{
	/** 
	 生成报表
	 
	 @param templeteFilePath 模版路径
	 @param ds 数据源
	 @return 生成单据的路径
	*/
	public static void Frm_GenerBill(String templeteFullFile, String saveToDir, String saveFileName, BillFileType fileType, DataSet ds, String fk_mapData)
	{

		MapData md = new MapData(fk_mapData);
		GEEntity entity = md.GenerGEEntityByDataSet(ds);

		BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();
		rtf.HisEns.Clear();
		rtf.EnsDataDtls.Clear();

		rtf.HisEns.AddEntity(entity);
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var dtls = entity.Dtls;

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		for (var item : dtls.ToJavaList())
		{
			rtf.EnsDataDtls.Add(item);
		}

		rtf.MakeDoc(templeteFullFile, saveToDir, saveFileName, false);
	}

	/** 
	 仅获取表单数据
	 
	 @param enName
	 @param pkval
	 @param atParas
	 @param specDtlFrmID
	 @return 
	*/

	private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(String enName, Object pkval, String atParas)
	{
		return GenerDBForVSTOExcelFrmModelOfEntity(enName, pkval, atParas, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(string enName, object pkval, string atParas, string specDtlFrmID = null)
	private static DataSet GenerDBForVSTOExcelFrmModelOfEntity(String enName, Object pkval, String atParas, String specDtlFrmID)
	{
		DataSet myds = new DataSet();

		// 创建实体..
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 主表

		Entity en = BP.En.ClassFactory.GetEn(enName);
		en.PKVal = pkval;

		// if (DataType.IsNullOrEmpty(pkval)==false)
		en.Retrieve();


		//设置外部传入的默认值.
		if (BP.Sys.SystemConfig.IsBSsystem == true)
		{
			// 处理传递过来的参数。
			//2019-07-25 zyt改造
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				en.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}
		}

		//主表数据放入集合.
		DataTable mainTable = en.ToDataTableField();
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 主表 Sys_MapData
		String sql = "SELECT * FROM Sys_MapData WHERE 1=2 ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";

		Map map = en.EnMapInTime;
		DataRow dr = dt.NewRow();
		dr.set(MapDataAttr.No, enName);
		dr.set(MapDataAttr.Name, map.EnDesc);
		dr.set(MapDataAttr.PTable, map.PhysicsTable);
		dt.Rows.add(dr);
		myds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 主表 Sys_MapData

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 主表 Sys_MapAttr
		sql = "SELECT * FROM Sys_MapAttr WHERE 1=2 ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		for (Attr attr : map.Attrs)
		{
			dr = dt.NewRow();
			dr.set(MapAttrAttr.MyPK, enName + "_" + attr.Key);
			dr.set(MapAttrAttr.Name, attr.Desc);

			dr.set(MapAttrAttr.MyDataType, attr.MyDataType); //数据类型.
			dr.set(MapAttrAttr.MinLen, attr.MinLength); //最小长度.
			dr.set(MapAttrAttr.MaxLen, attr.MaxLength); //最大长度.

			// 设置他的逻辑类型.
			dr.set(MapAttrAttr.LGType, 0); //逻辑类型.
			switch (attr.MyFieldType)
			{
				case FieldType.Enum:
					dr.set(MapAttrAttr.LGType, 1);
					dr.set(MapAttrAttr.UIBindKey, attr.UIBindKey);

					//增加枚举字段.
					if (myds.Tables.Contains(attr.UIBindKey) == false)
					{
						String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + attr.UIBindKey + "' ORDER BY IntKey ";
						DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
						dtEnum.TableName = attr.UIBindKey;
						myds.Tables.add(dtEnum);
					}

					break;
				case FieldType.FK:
					dr.set(MapAttrAttr.LGType, 2);

					Entities ens = attr.HisFKEns;
					dr.set(MapAttrAttr.UIBindKey, ens.toString());

					//把外键字段也增加进去.
					if (myds.Tables.Contains(ens.toString()) == false && attr.UIIsReadonly == false)
					{
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
			dr.set(MapAttrAttr.UIContralType, (int)attr.UIContralType);
			dt.Rows.add(dr);
		}
		myds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 主表 Sys_MapAttr

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region //主表 Sys_MapExt 扩展属性
		////主表的配置信息.
		//sql = "SELECT * FROM Sys_MapExt WHERE 1=2";
		//dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "Sys_MapExt";
		//myds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion //主表 Sys_MapExt 扩展属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 从表
		for (EnDtl item : map.Dtls)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  把从表的数据放入集合.

			Entities dtls = item.Ens;

			QueryObject qo = qo = new QueryObject(dtls);

			if (dtls.toString().contains("CYSheBeiUse") == true)
			{
				qo.addOrderBy("RDT"); //按照日期进行排序，不用也可以.
			}

			qo.AddWhere(item.RefKey, pkval);
			DataTable dtDtl = qo.DoQueryToTable();

			dtDtl.TableName = item.EnsName; //修改明细表的名称.
			myds.Tables.add(dtDtl); //加入这个明细表.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 把从表的数据放入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 从表 Sys_MapDtl (相当于mapdata)

			Entity dtl = dtls.GetNewEntity;
			map = dtl.EnMap;

			//明细表的 描述 . 
			sql = "SELECT * FROM Sys_MapDtl WHERE 1=2";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_" + item.EnsName;

			dr = dt.NewRow();
			dr.set(MapDtlAttr.No, item.EnsName);
			dr.set(MapDtlAttr.Name, item.Desc);
			dr.set(MapDtlAttr.PTable, dtl.EnMap.PhysicsTable);
			dt.Rows.add(dr);
			myds.Tables.add(dt);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 从表 Sys_MapDtl (相当于mapdata)

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 明细表 Sys_MapAttr
			sql = "SELECT * FROM Sys_MapAttr WHERE 1=2";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapAttr_For_" + item.EnsName;
			for (Attr attr : map.Attrs)
			{
				dr = dt.NewRow();
				dr.set(MapAttrAttr.MyPK, enName + "_" + attr.Key);
				dr.set(MapAttrAttr.Name, attr.Desc);

				dr.set(MapAttrAttr.MyDataType, attr.MyDataType); //数据类型.
				dr.set(MapAttrAttr.MinLen, attr.MinLength); //最小长度.
				dr.set(MapAttrAttr.MaxLen, attr.MaxLength); //最大长度.

				// 设置他的逻辑类型.
				dr.set(MapAttrAttr.LGType, 0); //逻辑类型.
				switch (attr.MyFieldType)
				{
					case FieldType.Enum:
						dr.set(MapAttrAttr.LGType, 1);
						dr.set(MapAttrAttr.UIBindKey, attr.UIBindKey);

						//增加枚举字段.
						if (myds.Tables.Contains(attr.UIBindKey) == false)
						{
							String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + attr.UIBindKey + "' ORDER BY IntKey ";
							DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
							dtEnum.TableName = attr.UIBindKey;
							myds.Tables.add(dtEnum);
						}
						break;
					case FieldType.FK:
						dr.set(MapAttrAttr.LGType, 2);

						Entities ens = attr.HisFKEns;
						dr.set(MapAttrAttr.UIBindKey, ens.toString());

						//把外键字段也增加进去.
						if (myds.Tables.Contains(ens.toString()) == false && attr.UIIsReadonly == false)
						{
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
				dr.set(MapAttrAttr.UIContralType, (int)attr.UIContralType);
				dt.Rows.add(dr);
			}
			myds.Tables.add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 明细表 Sys_MapAttr

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return myds;
	}
	/** 
	 仅获取表单数据
	 
	 @param frmID 表单ID
	 @param pkval 主键
	 @param atParas 参数
	 @param specDtlFrmID 指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 @return 数据
	*/

	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, Object pkval, String atParas)
	{
		return GenerDBForVSTOExcelFrmModel(frmID, pkval, atParas, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataSet GenerDBForVSTOExcelFrmModel(string frmID, object pkval, string atParas, string specDtlFrmID = null)
	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, Object pkval, String atParas, String specDtlFrmID)
	{
		//如果是一个实体类.
		if (frmID.contains("BP."))
		{
			// 执行map同步.
			Entities ens = BP.En.ClassFactory.GetEns(frmID + "s");
			Entity myen = ens.GetNewEntity;
			myen.DTSMapToSys_MapData();
			return GenerDBForVSTOExcelFrmModelOfEntity(frmID, pkval, atParas, specDtlFrmID = null);

			//上面这行代码的解释（2017-04-25）：
			//若不加上这行，代码执行到" MapData md = new MapData(frmID); "会报错：
			//@没有找到记录[表单注册表  Sys_MapData, [ 主键=No 值=BP.LI.BZQX ]记录不存在,请与管理员联系, 或者确认输入错误.@在Entity(BP.Sys.MapData)查询期间出现错误@   在 BP.En.Entity.Retrieve() 位置 D:\ccflow\Components\BP.En30\En\Entity.cs:行号 1051
			//即使加上：
			//frmID = frmID.Substring(0, frmID.Length - 1);
			//也会出现该问题
			//2017-04-25 15:26:34：new MapData(frmID)应传入"BZQX"，但考虑到 GenerDBForVSTOExcelFrmModelOfEntity()运行稳定，暂不采用『统一执行下方代码』的方案。
		}

		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//映射实体.
		MapData md = new MapData(frmID);

		Map map = md.GenerHisMap();

		Entity en = null;
		if (map.Attrs.Contains("OID") == true)
		{
			//实体.
			GEEntity wk = new GEEntity(frmID);
			wk.OID = Integer.parseInt(pkval.toString());
			if (wk.RetrieveFromDBSources() == 0)
			{
				wk.Insert();
			}

			md.DoEvent(FrmEventList.FrmLoadBefore, wk, null);

			en = wk;
		}

		if (map.Attrs.Contains("MyPK") == true)
		{
			//实体.
			GEEntityMyPK wk = new GEEntityMyPK(frmID);
			wk.setMyPK( pkval.toString();
			if (wk.RetrieveFromDBSources() == 0)
			{
				wk.Insert();
			}
			md.DoEvent(FrmEventList.FrmLoadBefore, wk, null);
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

				if (en.Row.ContainsKey(key) == true) //有就该变.
				{
					en.Row[key] = ap.GetValStrByKey(key);
				}
				else
				{
					en.Row.Add(key, ap.GetValStrByKey(key)); //增加他.
				}
			}
		}

		//属性.
		MapExt me = null;
		DataTable dtMapAttr = null;
		MapExts mes = null;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 表单模版信息.（含主、从表的，以及从表的枚举/外键相关数据）.
		//增加表单字段描述.
		String sql = "SELECT * FROM Sys_MapData WHERE No='" + frmID + "' ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		myds.Tables.add(dt);

		//增加表单字段描述.
		sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + frmID + "' ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		myds.Tables.add(dt);

		//增加从表信息.
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData='" + frmID + "' ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapDtl";
		myds.Tables.add(dt);


		//主表的配置信息.
		sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + frmID + "'";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		myds.Tables.add(dt);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加载 从表表单模版信息.（含 从表的枚举/外键相关数据）
		for (MapDtl item : md.MapDtls)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 返回指定的明细表的数据.
			if (DataType.IsNullOrEmpty(specDtlFrmID) == true)
			{
			}
			else
			{
				if (!specDtlFrmID.equals(item.No))
				{
					continue;
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 返回指定的明细表的数据.

			//明细表的主表描述
			sql = "SELECT * FROM Sys_MapDtl WHERE No='" + item.No + "'";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_" + (DataType.IsNullOrEmpty(item.Alias) ? item.No : item.Alias);
			myds.Tables.add(dt);

			//明细表的表单描述
			sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + item.No + "'";
			dtMapAttr = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtMapAttr.TableName = "Sys_MapAttr_For_" + (DataType.IsNullOrEmpty(item.Alias) ? item.No : item.Alias);
			myds.Tables.add(dtMapAttr);

			//明细表的配置信息.
			sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + item.No + "'";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapExt_For_" + (DataType.IsNullOrEmpty(item.Alias) ? item.No : item.Alias);
			myds.Tables.add(dt);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 从表的 外键表/枚举
			mes = new MapExts(item.No);
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.get("LGType").toString();
				//不是枚举/外键字段
				if (lgType.equals("0"))
				{
					continue;
				}

				String uiBindKey = dr.get("UIBindKey").toString();
				String mypk = dr.get("MyPK").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 枚举字段
				if (lgType.equals("1"))
				{
					// 如果是枚举值, 判断是否存在.
					if (myds.Tables.Contains(uiBindKey) == true)
					{
						continue;
					}

					String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
					DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
					dtEnum.TableName = uiBindKey;
					myds.Tables.add(dtEnum);
					continue;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion

				String UIIsEnable = dr.get("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) //字段未启用
				{
					continue;
				}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 外键字段
				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
				if (me != null) //有范围限制时
				{
					Object tempVar2 = me.Doc.Clone();
					String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);

					dt = DBAccess.RunSQLReturnTable(fullSQL);

					dt.TableName = mypk;
					myds.Tables.add(dt);
					continue;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理下拉框数据范围.
				else //无范围限制时
				{
					// 判断是否存在.
					if (myds.Tables.Contains(uiBindKey) == true)
					{
						continue;
					}

					myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 外键字段
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 从表的 外键表/枚举

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加载 从表表单模版信息.（含 从表的枚举/外键相关数据）

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 表单模版信息.（含主、从表的，以及从表的枚举/外键相关数据）.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 主表数据
		if (BP.Sys.SystemConfig.IsBSsystem == true)
		{
			// 处理传递过来的参数。
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				en.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}
		}

		// 执行表单事件..
		String msg = md.DoEvent(FrmEventList.FrmLoadBefore, en);
		if (DataType.IsNullOrEmpty(msg) == false)
		{
			throw new RuntimeException("err@错误:" + msg);
		}

		//重设默认值.
		en.ResetDefaultVal();

		//执行装载填充.
		me = new MapExt();
		if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, frmID) == 1)
		{
			//执行通用的装载方法.
			MapAttrs attrs = new MapAttrs(frmID);
			MapDtls dtls = new MapDtls(frmID);
			Entity tempVar3 = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls);
			en = tempVar3 instanceof GEEntity ? (GEEntity)tempVar3 : null;
		}

		//增加主表数据.
		DataTable mainTable = en.ToDataTableField(md.No);
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 主表数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  从表数据
		for (MapDtl dtl : md.MapDtls)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 返回指定的明细表的数据.
			if (DataType.IsNullOrEmpty(specDtlFrmID) == true)
			{
			}
			else
			{
				if (!specDtlFrmID.equals(dtl.No))
				{
					continue;
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 返回指定的明细表的数据.

			GEDtls dtls = new GEDtls(dtl.No);
			QueryObject qo = null;

			if (dtl.RefPK.equals(""))
			{
				try
				{
					qo = new QueryObject(dtls);
					switch (dtl.DtlOpenType)
					{
						case DtlOpenType.ForEmp: // 按人员来控制.
							qo.AddWhere(GEDtlAttr.RefPK, pkval);
							qo.addAnd();
							qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
							break;
						case DtlOpenType.ForWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, pkval);
							break;
						case DtlOpenType.ForFID: // 按流程ID来控制.
							qo.AddWhere(GEDtlAttr.FID, pkval);
							break;
					}
				}
				catch (java.lang.Exception e)
				{
					dtls.GetNewEntity.CheckPhysicsTable();
				}
			}
			else
			{
				qo = new QueryObject(dtls);
				qo.AddWhere(dtl.RefPK, pkval);
			}

			//条件过滤.
			if (!dtl.FilterSQLExp.equals(""))
			{
				String[] strs = dtl.FilterSQLExp.split("[=]", -1);
				qo.addAnd();
				qo.AddWhere(strs[0], strs[1]);
			}

			//从表
			DataTable dtDtl = qo.DoQueryToTable();

			// 为明细表设置默认值.
			MapAttrs dtlAttrs = new MapAttrs(dtl.No);
			for (MapAttr attr : dtlAttrs)
			{
				//处理它的默认值.
				if (attr.DefValReal.Contains("@") == false)
				{
					continue;
				}

				for (DataRow dr : dtDtl.Rows)
				{
					dr.set(attr.KeyOfEn, attr.DefVal);
				}
			}

			dtDtl.TableName = DataType.IsNullOrEmpty(dtl.Alias) ? dtl.No : dtl.Alias; //edited by liuxc,2017-10-10.如果有别名，则使用别名，没有则使用No
			myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 从表数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 主表的 外键表/枚举
		dtMapAttr = myds.Tables["Sys_MapAttr"];
		mes = md.MapExts;
		for (DataRow dr : dtMapAttr.Rows)
		{
			String uiBindKey = dr.get("UIBindKey") instanceof String ? (String)dr.get("UIBindKey") : null;
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue;
			}

			String myPK = dr.get("MyPK").toString();
			String lgType = dr.get("LGType").toString();

			if (lgType.equals("1"))
			{
				// 如果是枚举值, 判断是否存在., 
				if (myds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;
				myds.Tables.add(dtEnum);
				continue;
			}

			if (lgType.equals("2") == false)
			{
				continue;
			}

			String UIIsEnable = dr.get("UIIsEnable").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();
			String fk_mapData = dr.get("FK_MapData").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理下拉框数据范围. for 小杨.
			Object tempVar4 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar4 instanceof MapExt ? (MapExt)tempVar4 : null;
			if (me != null)
			{
				Object tempVar5 = me.Doc.Clone();
				String fullSQL = tempVar5 instanceof String ? (String)tempVar5 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				dt.TableName = myPK; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				myds.Tables.add(dt);
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理下拉框数据范围.

			dt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
			dt.TableName = uiBindKey;
			myds.Tables.add(dt);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	 
	 @param frmID 表单ID
	 @param pkval 主键
	 @param atParas 参数
	 @param specDtlFrmID 指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 @return 数据
	*/

	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas)
	{
		return GenerDBForCCFormDtl(frmID, dtl, pkval, atParas, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataSet GenerDBForCCFormDtl(string frmID, MapDtl dtl, int pkval, string atParas, Int64 fid = 0)
	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas, long fid)
	{
		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//实体.
		GEEntity en = new GEEntity(frmID);
		en.OID = pkval;
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
					if (en.Row.ContainsKey(key) == true) //有就该变.
					{
						en.Row[key] = ap.GetValStrByKey(key);
					}
					else
					{
						en.Row.Add(key, ap.GetValStrByKey(key)); //增加他.
					}
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException(key);
				}
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加载从表表单模版信息.

		DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
		myds.Tables.add(Sys_MapDtl);

		//明细表的表单描述
		DataTable Sys_MapAttr = dtl.MapAttrs.ToDataTableField("Sys_MapAttr");
		myds.Tables.add(Sys_MapAttr);

		//明细表的配置信息.
		DataTable Sys_MapExt = dtl.MapExts.ToDataTableField("Sys_MapExt");
		myds.Tables.add(Sys_MapExt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加载从表表单模版信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把从表的- 外键表/枚举 加入 DataSet.
		MapExts mes = dtl.MapExts;
		MapExt me = null;

		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		for (DataRow dr : Sys_MapAttr.Rows)
		{
			String lgType = dr.get("LGType").toString();
			String ctrlType = dr.get(MapAttrAttr.UIContralType).toString();

			//没有绑定外键
			String uiBindKey = dr.get("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue;
			}

			String mypk = dr.get("MyPK").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 枚举字段
			if (lgType.equals("1") == true)
			{
				// 如果是枚举值, 判断是否存在.
				if (myds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;

				dtEnum.Columns[0].ColumnName = "No";
				dtEnum.Columns[1].ColumnName = "Name";

				myds.Tables.add(dtEnum);
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 外键字段
			String UIIsEnable = dr.get("UIIsEnable").toString();
			if (UIIsEnable.equals("0")) //字段未启用
			{
				continue;
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
			if (me != null) //有范围限制时
			{
				Object tempVar2 = me.Doc.Clone();
				String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);

				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);

				dt.TableName = uiBindKey;

				dt.Columns[0].ColumnName = "No";
				dt.Columns[1].ColumnName = "Name";

				myds.Tables.add(dt);
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理下拉框数据范围.

			// 判断是否存在.
			if (myds.Tables.Contains(uiBindKey) == true)
			{
				continue;
			}

			// 获得数据.
			DataTable mydt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);

			if (mydt == null)
			{
				DataRow ddldr = ddlTable.NewRow();
				ddldr.set("No", uiBindKey);
				ddlTable.Rows.add(ddldr);
			}
			else
			{
				myds.Tables.add(mydt);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 外键字段
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 把从表的- 外键表/枚举 加入 DataSet.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把主表数据放入.
		if (BP.Sys.SystemConfig.IsBSsystem == true)
		{
			// 处理传递过来的参数。
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				en.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}
		}

		//重设默认值.
		en.ResetDefaultVal();


		//增加主表数据.
		DataTable mainTable = en.ToDataTableField(frmID);
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 把主表数据放入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  把从表的数据放入.
		GEDtls dtls = new GEDtls(dtl.No);
		QueryObject qo = null;
		try
		{
			qo = new QueryObject(dtls);
			switch (dtl.DtlOpenType)
			{
				case DtlOpenType.ForEmp: // 按人员来控制.
					qo.AddWhere(GEDtlAttr.RefPK, pkval);
					qo.addAnd();
					qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
					break;
				case DtlOpenType.ForWorkID: // 按工作ID来控制
					qo.addLeftBracket();
					qo.AddWhere(GEDtlAttr.RefPK, String.valueOf(pkval));
					qo.addOr();
					qo.AddWhere(GEDtlAttr.FID, pkval);
					qo.addRightBracket();
					break;
				case DtlOpenType.ForFID: // 按流程ID来控制.
					if (fid == 0)
					{
						qo.AddWhere(GEDtlAttr.FID, pkval);
					}
					else
					{
						qo.AddWhere(GEDtlAttr.FID, fid);
					}
					break;
			}
		}
		catch (RuntimeException ex)
		{
			dtls.GetNewEntity.CheckPhysicsTable();
			throw ex;
		}

		//条件过滤.
		if (!dtl.FilterSQLExp.equals(""))
		{
			String[] strs = dtl.FilterSQLExp.split("[=]", -1);
			if (strs.length == 2)
			{
				qo.addAnd();
				qo.AddWhere(strs[0], strs[1]);
			}
		}

		//增加排序.
		//    qo.addOrderByDesc( dtls.GetNewEntity.PKField );

		//从表
		DataTable dtDtl = qo.DoQueryToTable();

		//查询所有动态SQL查询类型的字典表记录
		SFTable sftable = null;
		DataTable dtsftable = null;
		DataRow[] drs = null;

		// 为明细表设置默认值.
		MapAttrs dtlAttrs = new MapAttrs(dtl.No);
		for (MapAttr attr : dtlAttrs)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 修改区分大小写. Oracle
			if (BP.DA.DBType.Oracle == SystemConfig.getAppCenterDBType())
			{
				for (DataColumn dr : dtDtl.Columns)
				{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
					var a = attr.KeyOfEn;
					String b = dr.ColumnName;
					if (attr.KeyOfEn.toUpperCase().equals(dr.ColumnName))
					{
						dr.ColumnName = attr.KeyOfEn;
						continue;
					}

					if (attr.LGType == FieldTypeS.Enum || attr.LGType == FieldTypeS.FK)
					{
						if (dr.ColumnName.equals(attr.KeyOfEn.toUpperCase() + "TEXT"))
						{
							dr.ColumnName = attr.KeyOfEn + "Text";
						}
					}
				}
				for (DataRow dr : dtDtl.Rows)
				{
					//本身是大写的不进行修改
					if (DataType.IsNullOrEmpty(dr.get(attr.KeyOfEn) + ""))
					{
						dr.set(attr.KeyOfEn, dr.get(attr.KeyOfEn.toUpperCase()));
						dr.set(attr.KeyOfEn.toUpperCase(), null);
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 修改区分大小写.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 修改区分大小写. PostgreSQL
			if (BP.DA.DBType.PostgreSQL == SystemConfig.getAppCenterDBType())
			{
				for (DataColumn dr : dtDtl.Columns)
				{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
					var a = attr.KeyOfEn;
					String b = dr.ColumnName;
					if (attr.KeyOfEn.toLowerCase().equals(dr.ColumnName))
					{
						dr.ColumnName = attr.KeyOfEn;
						continue;
					}

					if (attr.LGType == FieldTypeS.Enum || attr.LGType == FieldTypeS.FK)
					{
						if (dr.ColumnName.equals(attr.KeyOfEn.toLowerCase() + "TEXT"))
						{
							dr.ColumnName = attr.KeyOfEn + "Text";
						}
					}
				}
				for (DataRow dr : dtDtl.Rows)
				{
					//本身是大写的不进行修改
					if (DataType.IsNullOrEmpty(dr.get(attr.KeyOfEn) + ""))
					{
						dr.set(attr.KeyOfEn, dr.get(attr.KeyOfEn.toLowerCase()));
						dr.set(attr.KeyOfEn.toLowerCase(), null);
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 修改区分大小写.

			if (attr.UIContralType == UIContralType.TB)
			{
				continue;
			}

			//处理它的默认值.
			if (attr.DefValReal.Contains("@") == false)
			{
				continue;
			}

			for (DataRow dr : dtDtl.Rows)
			{
				dr.set(attr.KeyOfEn, attr.DefVal);
			}
		}

		dtDtl.TableName = "DBDtl"; //修改明细表的名称.
		myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 把从表的数据放入.


		//放入一个空白的实体，用与获取默认值.
		GEDtl dtlBlank = dtls.GetNewEntity instanceof GEDtl ? (GEDtl)dtls.GetNewEntity : null;
		dtlBlank.ResetDefaultVal();

		myds.Tables.add(dtlBlank.ToDataTableField("Blank"));

		// myds.WriteXml("c:\\xx.xml");

		return myds;
	}
}