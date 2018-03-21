package BP.WF.HttpHandler;

import org.apache.commons.collections.bag.TreeBag;
import org.springframework.format.datetime.DateFormatterRegistrar;

import com.sun.star.lib.uno.environments.remote.Job;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrOfOneVSM;
import BP.En.Attrs;
import BP.En.AttrsOfOneVSM;
import BP.En.ClassFactory;
import BP.En.Dot2DotModel;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.RefMethods;
import BP.Sys.EnCfg;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Tools.Json;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.Web.WebUser;

public class WF_CommEntity extends WebContralBase {

	
	  public String BranchesAndLeaf_SearchByNodeID()
      {
		  String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		  String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");
		  String key = this.GetRequestVal("Key"); //查询关键字.

          String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
          Entities ensMen = ClassFactory.GetEns(ensOfM);
          QueryObject qo = new QueryObject(ensMen); //集合.
          qo.AddWhere(defaultGroupAttrKey, key);
          qo.DoQuery();


          return ensMen.ToJson();
      }
      public String BranchesAndLeaf_SearchByKey()
      {
    	  String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
    	  String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");

    	  String key = this.GetRequestVal("Key"); //查询关键字.

    	  String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
          Entities ensMen = ClassFactory.GetEns(ensOfM);
          QueryObject qo = new QueryObject(ensMen); //集合.
          qo.AddWhere("No", " LIKE ", "%" + key + "%");
          qo.addOr();
          qo.AddWhere("Name", " LIKE ", "%" + key + "%");
          qo.DoQuery();

          return ensMen.ToJson();
      }
      public String BranchesAndLeaf_Delete()
      {
          try
          {
        	  String dot2DotEnName = this.GetRequestVal("Dot2DotEnName");
        	  String AttrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");
        	  String AttrOfMInMM = this.GetRequestVal("AttrOfMInMM");
              Entity mm = ClassFactory.GetEn(dot2DotEnName);
              mm.Delete(AttrOfOneInMM, this.getPKVal(), AttrOfMInMM, this.GetRequestVal("Key"));
              return "删除成功.";
          }
          catch (Exception ex)
          {
              return "err@" + ex.getMessage();
          }
      }
	
	 /// <summary>
    /// 初始化
    /// </summary>
    /// <returns></returns>
    public String BranchesAndLeaf_Init()
    {
        String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
        String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");

        //string enName = this.GetRequestVal("EnName");
        Entity en = ClassFactory.GetEn(this.getEnName());
        en.setPKVal(this.getPKVal());
        en.Retrieve();

        //找到映射.
        AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
        AttrOfOneVSM vsM = null;
        
        	
        for (AttrOfOneVSM item : oneVsM)
        {
            if (item.dot2DotModel  == Dot2DotModel.TreeDeptEmp
                && item.getEnsOfMM().toString().equals(dot2DotEnsName)
                && item.DefaultGroupAttrKey.equals(defaultGroupAttrKey))
            {
                vsM = item;
                break;
            } 
        }
        if (vsM == null)
            return "err@参数错误,没有找到VSM";

        //组织数据.
        DataSet ds = new DataSet();
        String rootNo = vsM.RootNo;
        if (rootNo.equals("@WebUser.FK_Dept") || rootNo.equals("WebUser.FK_Dept"))
            rootNo = WebUser.getFK_Dept();

        //#region 生成树目录.
        String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
        Entities ensMen = ClassFactory.GetEns(ensOfM);
        Entity enMen = ensMen.getGetNewEntity();

        Attr attr = enMen.getEnMap().GetAttrByKey(defaultGroupAttrKey);
        if (attr == null)
            return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不存在，请确认是否删除了该属性?";

        if (attr.getMyFieldType() == FieldType.Normal)
            return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不能是普通字段，必须是外键或者枚举.";

        Entities trees = attr.getHisFKEns();
        trees.RetrieveAll();

        DataTable dt = trees.ToDataTableField("DBTrees");
        //如果没有parnetNo 列，就增加上, 有可能是分组显示使用这个模式.
        if (dt.Columns.contains("ParentNo") == false)
        {
            dt.Columns.Add("ParentNo");           
            
            for (DataRow dr : dt.Rows)
                dr.setValue("ParentNo", rootNo);
        }
        
        ds.Tables.add(dt);
        //#endregion 生成树目录.

        //#region 生成选择的数据.
        Entities dot2Dots = ClassFactory.GetEns(dot2DotEnsName);
        dot2Dots.Retrieve(vsM.getAttrOfOneInMM(), this.getPKVal());

        DataTable dtSelected = dot2Dots.ToDataTableField("DBMMs");

        String attrOfMInMM = this.GetRequestVal("AttrOfMInMM");
        String AttrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");

        dtSelected.Columns.get(attrOfMInMM).ColumnName = "No";
        dtSelected.Columns.get(attrOfMInMM + "Text").ColumnName = "Name";
        dtSelected.Columns.remove(AttrOfOneInMM);
        ds.Tables.add(dtSelected); //已经选择的数据.
        //#endregion 生成选择的数据.

        return BP.Tools.Json.ToJson(ds);
    }
    
	/// #region 实体的操作.
	public final String Entity_Init() {
		// 初始化entity.
		String enName = this.getEnName();
		Entity en = null;
		if (DataType.IsNullOrEmpty(enName) == true) {
			if (DataType.IsNullOrEmpty(this.getEnsName()) == true) {
				return "err@类名没有传递过来";
			}

			Entities ens = ClassFactory.GetEns(this.getEnsName());
			en = ens.getGetNewEntity();
		} else {
			en = ClassFactory.GetEn(this.getEnName());
		}

		// 获得描述.
		Map map = en.getEnMap();
		String pkVal = this.getPKVal();
		if (DataType.IsNullOrEmpty(pkVal) == false) {
			en.setPKVal(pkVal);
			en.RetrieveFromDBSources();
		} else {
			for (Attr attr : en.getEnMap().getAttrs()) {
				en.SetValByKey(attr.getKey(), attr.getDefaultVal());
			}
			// 设置默认的数据.
			en.ResetDefaultVal();
		}

		// 定义容器.
		DataSet ds = new DataSet();

		// 定义Sys_MapData.
		MapData md = new MapData();
		md.setNo(this.getEnName());
		md.setName(map.getEnDesc());

		/// #region 加入权限信息.
		// 把权限加入参数里面.
		if (en.getHisUAC().IsInsert) {
			md.SetPara("IsInsert", "1");
		}
		if (en.getHisUAC().IsUpdate) {
			md.SetPara("IsUpdate", "1");
		}
		if (en.getHisUAC().IsDelete) {
			md.SetPara("IsDelete", "1");
		}

		/// #endregion 加入权限信息.

		/// #region 增加 上方法.
		DataTable dtM = new DataTable("dtM");
		dtM.Columns.Add("No");
		dtM.Columns.Add("Title");
		dtM.Columns.Add("Tip");
		dtM.Columns.Add("Visable");

		dtM.Columns.Add("Url");
		dtM.Columns.Add("Target");
		dtM.Columns.Add("Warning");
		dtM.Columns.Add("RefMethodType");
		dtM.Columns.Add("GroupName");
		dtM.Columns.Add("W");
		dtM.Columns.Add("H");
		dtM.Columns.Add("Icon");
		dtM.Columns.Add("IsCanBatch");
		dtM.Columns.Add("RefAttrKey");

		RefMethods rms = map.getHisRefMethods();
		for (RefMethod item : rms) {
			String myurl = "";
			if (item.refMethodType != RefMethodType.Func) {
				Object tempVar = null;
				try {
					tempVar = item.Do(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				myurl = (String) ((tempVar instanceof String) ? tempVar : null);
				if (myurl == null) {
					continue;
				}
			} else {
				myurl = "../RefMethod.htm?Index=" + item.Index + "&EnsName=" + en.getGetNewEntities().toString()
						+ "&PK=" + this.getPKVal();
			}

			DataRow dr = dtM.NewRow();

			dr.setValue("No", item.Index);
			dr.setValue("Title", item.Title);
			dr.setValue("Tip", item.ToolTip);
			dr.setValue("Visable", item.Visable);
			dr.setValue("Warning", item.Warning);
			dr.setValue("RefMethodType", item.refMethodType.getValue());
			dr.setValue("RefAttrKey", item.RefAttrKey);
			dr.setValue("URL", myurl);
			dr.setValue("W", item.Width);
			dr.setValue("H", item.Height);
			dr.setValue("Icon", item.Icon);
			dr.setValue("IsCanBatch", item.IsCanBatch);
			dr.setValue("GroupName", item.GroupName);

			dtM.Rows.add(dr); // 增加到rows.
		}

		/// #endregion 增加 上方法.

		/// #region 加入一对多的实体编辑
		AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
		String sql = "";
		int i = 0;
		if (oneVsM.size() > 0) {
			for (AttrOfOneVSM vsM : oneVsM) {
				// 判断该dot2dot是否显示？
				Entity enMM = vsM.getEnsOfMM().getGetNewEntity();
				enMM.SetValByKey(vsM.getAttrOfOneInMM(), this.getPKVal());
				if (enMM.getHisUAC().IsView == false) {
					continue;
				}
				DataRow dr = dtM.NewRow();
//				//新增内容开始
//				dr.setValue("No", enMM.toString());
//
//               // dr["GroupName"] = vsM.GroupName;
//                if (null != en.getPKVal())
//                {
//                    //判断模式.
//                    String url = "";
//                    if (vsM.Dot2DotModel.equals(Dot2DotModel.TreeDept))
//                    {
//                        //url = "Dot2DotTreeDeptModel.htm?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
//                      //  url = "Branches.htm?EnName=" + en.ToString() + "&AttrKey=" + vsM.EnsOfMM.ToString();
//
//                        url = "Branches.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName=" + vsM.EnsOfMM.ToString();
//                       // url += "&PKVal=" + en.PKVal;
//                        url += "&Dot2DotEnName=" + vsM.getEnsOfMM().getGetNewEntity().toString(); //存储实体类.
//                        url += "&AttrOfOneInMM=" + vsM.getAttrOfOneInMM(); //存储表那个与主表关联. 比如: FK_Node
//                        url += "&AttrOfMInMM=" + vsM.getAttrOfMInMM(); //dot2dot存储表那个与实体表.  比如:FK_Station.
//                        url += "&EnsOfM=" + vsM.getEnsOfM().toString(); //默认的B实体分组依据.  比如:FK_Station.
//                        url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; //默认的B实体分组依据.  
//
//                    }
//                    else if (vsM.Dot2DotModel.equals(Dot2DotModel.TreeDeptEmp))
//                    {
//                        //   url = "Dot2DotTreeDeptEmpModel.htm?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
//                        // url = "Dot2Dot.aspx?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
//                        url = "BranchesAndLeaf.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName=" + vsM.EnsOfMM.ToString();
//                     //   url += "&PKVal=" + en.PKVal;
//                        url += "&Dot2DotEnName=" + vsM.getEnsOfMM().getGetNewEntity().toString(); //存储实体类.
//                        url += "&AttrOfOneInMM=" + vsM.getAttrOfOneInMM(); //存储表那个与主表关联. 比如: FK_Node
//                        url += "&AttrOfMInMM=" + vsM.getAttrOfMInMM(); //dot2dot存储表那个与实体表.  比如:FK_Station.
//                        url += "&EnsOfM=" + vsM.getEnsOfM().toString(); //默认的B实体分组依据.  比如:FK_Station.
//                        url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; //默认的B实体分组依据.  比如:FK_Station.
//                        //url += "&RootNo=" + vsM.RootNo; //默认的B实体分组依据.  比如:FK_Station.
//                    }
//                    else
//                    {
//                        // url = "Dot2Dot.aspx?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
//                        url = "Dot2Dot.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName=" + vsM.getEnsOfMM().toString(); //比如:BP.WF.Template.NodeStations
//                        url += "&AttrOfOneInMM=" + vsM.getAttrOfOneInMM(); //存储表那个与主表关联. 比如: FK_Node
//                        url += "&AttrOfMInMM=" + vsM.getAttrOfMInMM();  //dot2dot存储表那个与实体表.  比如:FK_Station.
//                        url += "&EnsOfM=" + vsM.getEnsOfM().toString(); //默认的B实体.   //比如:BP.Port.Stations
//                        url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; //默认的B实体分组依据.  比如:FK_Station.
//
//                        //+"&RefAttrEnsName=" + vsM.EnsOfM.ToString();
//                        //url += "&RefAttrKey=" + vsM.AttrOfOneInMM + "&RefAttrEnsName=" + vsM.EnsOfM.ToString();
//                    }
//
//                    dr.setValue("URL" , url + "&" + en.getPK() + "=" + en.getPKVal() + "&PKVal=" + en.getPKVal());
//                    dr.setValue("Icon", "../Img/M2M.png");
//                    
//                }
//
//                dr.setValue("W", "900");
//                dr.setValue("H", "500");
//                dr.setValue("RefMethodType", (int)RefMethodType.RightFrameOpen.getValue());
//
//                // 获得选择的数量.
//                try
//                {
//                    sql = "SELECT COUNT(*) as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'";
//                    i = DBAccess.RunSQLReturnValInt(sql);
//                }
//                catch
//                {
//                    sql = "SELECT COUNT(*) as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "=" + en.PKVal;
//                    try
//                    {
//                        i = DBAccess.RunSQLReturnValInt(sql);
//                    }
//                    catch
//                    {
//                        vsM.EnsOfMM.GetNewEntity.CheckPhysicsTable();
//                    }
//                }
//                dr["Title"] = vsM.Desc + "(" + i + ")";
//                dtM.Rows.Add(dr);
                //新增内容介绍
			}
		}
		ds.Tables.add(dtM);

		/// #endregion 增加 一对多.

		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		// 把主数据放入里面去.
		DataTable dtMain = en.ToDataTableField("MainTable");
		ds.Tables.add(dtMain);

		/// #region 增加上分组信息.
		EnCfg ec = new EnCfg(this.getEnName());
		String groupTitle = ec.getGroupTitle();
		if (DataType.IsNullOrEmpty(groupTitle) == true) {
			groupTitle = "@" + en.getPK() + ",基本信息," + map.getEnDesc() + "";
		}

		// 增加上.
		DataTable dtGroups = new DataTable("Sys_GroupField");
		dtGroups.Columns.Add("OID");
		dtGroups.Columns.Add("Lab");
		dtGroups.Columns.Add("Tip");
		dtGroups.Columns.Add("CtrlType");
		dtGroups.Columns.Add("CtrlID");

		String[] strs = groupTitle.split("[@]", -1);
		for (String str : strs) {
			if (DataType.IsNullOrEmpty(str)) {
				continue;
			}

			String[] vals = str.split("[=]", -1);
			if (vals.length == 1) {
				vals = str.split("[,]", -1);
			}

			if (vals.length == 0) {
				continue;
			}

			DataRow dr = dtGroups.NewRow();
			dr.setValue("OID", vals[0]);
			dr.setValue("Lab", vals[1]);
			if (vals.length == 3) {
				dr.setValue("Tip", vals[2]);
			}
			dtGroups.Rows.add(dr);
		}


		ds.Tables.add(dtGroups);

		/// #endregion 增加上分组信息.

		/// #region 字段属性.
		MapAttrs attrs = en.getEnMap().getAttrs().ToMapAttrs();
		DataTable sys_MapAttrs = attrs.ToDataTableField("Sys_MapAttr");
		sys_MapAttrs.Columns.remove(MapAttrAttr.GroupID);
		sys_MapAttrs.Columns.Add("GroupID");

		// sys_MapAttrs.Columns[MapAttrAttr.GroupID].DataType = typeof(string);
		// //改变列类型.

		// 给字段增加分组.
		String currGroupID = "";
		for (DataRow drAttr : sys_MapAttrs.Rows) {
			if (currGroupID.equals("") == true) {
				currGroupID = dtGroups.Rows.get(0).getValue("OID").toString();
			}

			String keyOfEn = drAttr.getValue(MapAttrAttr.KeyOfEn).toString();
			for (DataRow drGroup : dtGroups.Rows) {
				String field = drGroup.getValue("OID").toString();
				if (keyOfEn.equals(field)) {
					currGroupID = field;
				}
			}
			drAttr.setValue(MapAttrAttr.GroupID, currGroupID);
		}
		ds.Tables.add(sys_MapAttrs);

		/// #endregion 字段属性.

		/// #region 把外键与枚举放入里面去.
		for (DataRow dr : sys_MapAttrs.Rows) {
			String uiBindKey = dr.getValue("UIBindKey").toString();
			String lgType = dr.getValue("LGType").toString();
			if (!lgType.equals("2")) {
				continue;
			}

			String UIIsEnable = dr.getValue("UIVisible").toString();
			if (UIIsEnable.equals("0")) {
				continue;
			}

			if (DotNetToJavaStringHelper.isNullOrEmpty(uiBindKey) == true) {
				String myPK = dr.getValue("MyPK").toString();
				// 如果是空的
				// throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name +
				// ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey
				// IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();

			// 判断是否存在.
			if (ds.Tables.contains(uiBindKey) == true) {
				continue;
			}

			ds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
		}
		
		   //加入sql模式的外键.
        for (Attr attr : en.getEnMap().getAttrs())
        {
            if (attr.getIsRefAttr() == true)
                continue;

            if (DataType.IsNullOrEmpty(attr.getUIBindKey()) || attr.getUIBindKey().length() <= 10)
                continue;

            if (attr.getUIIsReadonly() == true)
                continue;

            if (attr.getUIBindKey().contains("SELECT") == true || attr.getUIBindKey().contains("select") == true)
            {
                /*是一个sql*/
                String sqlBindKey = attr.getUIBindKey();
                
                sqlBindKey = BP.WF.Glo.DealExp(sqlBindKey, en, null);

                DataTable dt = DBAccess.RunSQLReturnTable(sqlBindKey);
                dt.TableName = attr.getKey();
                ds.Tables.add(dt);
            }
        }
		

		String enumKeys = "";
		for (Attr attr : map.getAttrs()) {
			if (attr.getMyFieldType() == FieldType.Enum) {
				enumKeys += "'" + attr.getUIBindKey() + "',";
			}
		}

		if (enumKeys.length() > 2) {
			enumKeys = enumKeys.substring(0, enumKeys.length() - 1);
			// Sys_Enum
			String sqlEnum = "SELECT * FROM Sys_Enum WHERE EnumKey IN (" + enumKeys + ")";
			DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);
			dtEnum.TableName = "Sys_Enum";
			ds.Tables.add(dtEnum);
		}

		/// #endregion 把外键与枚举放入里面去.
		System.out.println(BP.Tools.Json.ToJson(ds));
		return BP.Tools.Json.ToJson(ds);
	}
	
	//#region 分组数据.
    /// <summary>
    /// 执行保存
    /// </summary>
    /// <returns></returns>
    public String Dot2Dot_Save()
    {

        try
        {
        	String eles = this.GetRequestVal("Eles");

            //实体集合.
        	String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
        	String attrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");
            String attrOfMInMM = this.GetRequestVal("AttrOfMInMM");

            //获得点对点的实体.
            Entity en = ClassFactory.GetEns(dot2DotEnsName).getGetNewEntity();
            en.Delete(attrOfOneInMM, this.getPKVal()); //首先删除.

            String[] strs = eles.split(",");
            for (String str : strs)
            {
                if (DataType.IsNullOrEmpty(str) == true)
                    continue;

                en.SetValByKey(attrOfOneInMM, this.getPKVal());
                en.SetValByKey(attrOfMInMM, str);
                en.Insert();
            }
            return "数据保存成功.";
        }
        catch (Exception ex)
        {
            return "err@"+ex.getMessage();
        }
    }
    /// <summary>
    /// 获得分组的数据源
    /// </summary>
    /// <returns></returns>
    public String Dot2Dot_GenerGroupEntitis()
    {
        String key = this.GetRequestVal("DefaultGroupAttrKey");

        //实体集合.
        String ensName = this.GetRequestVal("EnsOfM");
        Entities ens = ClassFactory.GetEns(ensName);
        Entity en = ens.getGetNewEntity();

        Attrs attrs = en.getEnMap().getAttrs();
        Attr attr = attrs.GetAttrByKey(key);

        if (attr == null)
            return "err@设置的分组外键错误[" + key + "],不存在[" + ensName + "]或者已经被删除.";

        if (attr.getMyFieldType() == FieldType.Normal)
            return "err@设置的默认分组["+key+"]不能是普通字段.";

        if (attr.getMyFieldType() == FieldType.FK)
        {
            Entities ensFK = attr.getHisFKEns();
            ensFK.clear();
            ensFK.RetrieveAll();
            return ensFK.ToJson();
        }

        if (attr.getMyFieldType() == FieldType.Enum)
        {
            /* 如果是枚举 */
            SysEnums ses = new SysEnums();
            ses.Retrieve(SysEnumAttr.IntKey, attr.getUIBindKey());

            //ses.ToStringOfSQLModelByKey

            BP.Pub.NYs nys = new BP.Pub.NYs();
            for (SysEnum item : ses.ToJavaList())
            {
                BP.Pub.NY ny =new BP.Pub.NY();
                ny.setNo(  item.GetValStrByKey(SysEnumAttr.IntKey )  );
                ny.setName( item.getLab());
                nys.AddEntity(ny);
            }
            return nys.ToJson();
        }

        return "err@设置的默认分组[" + key + "]不能是普通字段.";
    }
   // #endregion 分组数据.
}
