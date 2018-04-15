package BP.WF;

import java.util.ArrayList;
import java.util.Enumeration;

import org.springframework.util.StringUtils;

import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.Template.*;
import BP.En.*;
import BP.Sys.*;
import BP.Tools.StringHelper;

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
		rtf.getHisEns().clear();
		rtf.getEnsDataDtls().clear();

		rtf.getHisEns().AddEntity(entity);

		ArrayList dtls = entity.getDtls();
		for (Object item : dtls)
		{
			rtf.getEnsDataDtls().add(item);
		}

		rtf.MakeDoc(templeteFullFile, saveToDir, saveFileName, null, false);
	}
	/**
	 * 保存
	 * @param enumKey
	 * @param enumLab
	 * @param cfg
	 * @param lang
	 * @return
	 */
	
    public static String SaveEnum(String enumKey, String enumLab, String cfg)
    {
    	String lang ="CH";
        SysEnumMain sem = new SysEnumMain();
        sem.setNo(enumKey);
        if (sem.RetrieveFromDBSources() == 0)
        {
            sem.setName(enumLab);
            sem.setCfgVal(cfg);
            sem.setLang(lang);
            sem.Insert();
        }
        else
        {
            sem.setName(enumLab);
            sem.setCfgVal(cfg);
            sem.setLang(lang);
            sem.Update();
        }

        String[] strs = cfg.split("@");
        for(String str : strs)
        {
            if (StringHelper.isNullOrEmpty(str))
                continue;
            String[] kvs = str.split("=");
            SysEnum se = new SysEnum();
            se.setEnumKey(enumKey);
            se.setLang(lang);
            se.setIntKey(Integer.parseInt(kvs[0]));
            se.setLab(kvs[1]);
            se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
            se.Save();
        }
        return "保存成功.";
    }
	
	/**
	 * 创建/修改-多附件
	 * @param fk_mapdata
	 * @param no
	 * @param name
	 * @param x
	 * @param y
	 */
    public static void getCreateOrSaveAthMulti(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(fk_mapdata);
        ath.setNoOfObj(no);
        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
       int i= ath.RetrieveFromDBSources();
        if (i==0)
        {
            ath.setSaveTo(SystemConfig.getPathOfDataUser() + "/UploadFile/" + fk_mapdata + "/");
        }

        ath.setUploadType(AttachmentUploadType.Multi);
        ath.setName(name);
        ath.setX(x);
        ath.setY(y);
        ath.Save();
    }
    /**
     *  获得外键表
     * @param pageNumber 第几页
     * @param pageSize 每页大小
     * @return json
     */
    public static String DB_SFTableList(int pageNumber, int pageSize)
    {
        //获得查询.
        SFTables sftables = new SFTables();
        QueryObject obj = new QueryObject(sftables);
        int RowCount = obj.GetCount();

        //查询
        obj.DoQuery(SysEnumMainAttr.No, pageSize, pageNumber);

        return BP.Tools.Entitis2Json.ConvertEntitis2GridJsonOnlyData(sftables, RowCount);
    }
    /**
     * 转拼音方法
     * @param name字段中文名称
     * @param isQuanPin是否转换全拼
     * @return转化后的拼音，不成功则抛出异常
     */
 
    public static String ParseStringToPinyinField(String name, boolean isQuanPin)
    {
        String s = "";
        try
        {
            if (isQuanPin == true)
            {
                s = BP.DA.DataType.ParseStringToPinyin(name);
                if (s.length() > 15)
                    s = BP.DA.DataType.ParseStringToPinyinWordFirst(name);
            }
            else
            {
                s = BP.DA.DataType.ParseStringToPinyinJianXie(name);
            }

            s = s.trim().replace(" ", "");
            s = s.trim().replace(" ", "");
            //常见符号
            s = s.replace(",", "").replace(".", "").replace("，", "").replace("。", "").replace("!", "");
            s = s.replace("*", "").replace("@", "").replace("#", "").replace("~", "").replace("|", "");
            s = s.replace("$", "").replace("%", "").replace("&", "").replace("（", "").replace("）", "").replace("【", "").replace("】", "");
            s = s.replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("/", "");
            if (s.length() > 0)
            {
                //去除开头数字
                String headStr = s.substring(0, 1);
                if (DataType.IsNumStr(headStr) == true)
                    s = "F" + s;
            }
            //去掉空格，去掉点.
            s = s.replace(" ", "");
            s = s.replace(".", "");
            return s;
        }
        catch (Exception ex)
        {
            Log.DebugWriteError(ex + "CCFormApI/ParseStringToPinyinField()");
        }
        return s;
    }
    /**
     * 
     * @param pageNumber 第几页
     * @param pageSize 每页大小
     * @return json
     */
    public static String DB_EnumerationList(int pageNumber, int pageSize)
    {
        SysEnumMains enumMains = new SysEnumMains();
        QueryObject obj = new QueryObject(enumMains);
        int RowCount = obj.GetCount();
        
        //查询
        obj.DoQuery(SysEnumMainAttr.No, pageSize, pageNumber);

        return BP.Tools.Entitis2Json.ConvertEntitis2GridJsonOnlyData(enumMains, RowCount);
    }
    /**
     * @param fk_mapdata 表单ID
     * @param ctrlType 控件类型
     * @param no  编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreatePublicNoNameCtrl(String fk_mapdata, String ctrlType, String no, String name, float x, float y)
    {
    	if("Dtl".equals(ctrlType))
    	{
    		CreateOrSaveDtl(fk_mapdata, no, name, x, y);
    	}else if("AthMulti".equals(ctrlType))
    	{
    		CreateOrSaveAthMulti(fk_mapdata, no, name, x, y);
    	} else if("AthSingle".equals(ctrlType))
    	{
    		CreateOrSaveAthSingle(fk_mapdata, no, name, x, y);
    	} else if("AthImg".equals(ctrlType))
    	{
    		CreateOrSaveAthImg(fk_mapdata, no, name, x, y);
    	} else if("Fieldset".equals(ctrlType))   //分组.
    	{
    		FrmEle fe = new FrmEle();
            fe.setMyPK(fk_mapdata + "_" + no);
            if (fe.RetrieveFromDBSources() != 0){
            	Log.DebugWriteError("@创建失败，已经有同名元素["+no+"]的控件.");
            }
            fe.setFK_MapData(fk_mapdata);
            fe.setEleType("Fieldset");
            fe.setEleName(name);
            fe.setEleID(no);
            fe.setX(x);
            fe.setY(y);
            fe.Insert();
    	} else 
    	{
    		Log.DebugWriteError("@没有判断的存储控件:"+ctrlType+",存储该控件前,需要做判断.");
    	}
    }
    
    /**
     * 创建/修改一个明细表 
     * @param fk_mapdata 表单ID
     * @param dtlNo 明细表编号
     * @param dtlName 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveDtl(String fk_mapdata, String dtlNo, String dtlName, float x, float y)
    {
        MapDtl dtl = new MapDtl();
        dtl.setNo(dtlNo);
        dtl.RetrieveFromDBSources();

        dtl.setX(x);
        dtl.setY(y);
        dtl.setName(dtlName);
        dtl.setFK_MapData(fk_mapdata);
        dtl.Save();

        //初始化他的map.
        dtl.IntMapAttrs();
    }
    
    /**
     * 创建/修改-多附件
     * @param fk_mapdata 表单ID
     * @param no 明细表编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveAthMulti(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(fk_mapdata);
        ath.setNoOfObj(no);
        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
       int i= ath.RetrieveFromDBSources();
        if (i==0)
        {
            ath.setSaveTo(SystemConfig.getPathOfDataUser() + "UploadFile/" + fk_mapdata + "/");
        }

        ath.setUploadType(AttachmentUploadType.Multi);
        ath.setName(name);
        ath.setX(x);
        ath.setY(y);
        ath.Save();
    }
    
    /**
     * 创建/修改-多附件
     * @param fk_mapdata 表单ID
     * @param no 明细表编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveAthSingle(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(fk_mapdata);
        ath.setNoOfObj(no);

        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
        ath.RetrieveFromDBSources();
        ath.setUploadType(AttachmentUploadType.Single);
        ath.setName(name);
        ath.setX(x);
        ath.setY(y);
        ath.Save();
    }
    
    /**
     * 创建/修改-图片附件
     * @param fk_mapdata 表单ID
     * @param no 明细表编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveAthImg(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmImgAth ath = new FrmImgAth();
        ath.setFK_MapData(fk_mapdata);
        ath.setCtrlID(no);
        ath.setMyPK(fk_mapdata + "_" + no);

        ath.setX(x);
        ath.setY(y);
        ath.Insert();
    }
	
    /**
     * 创建一个外部数据字段
     * @param fk_mapdata 表单ID
     * @param fieldName 字段名
     * @param fieldDesc 字段中文名
     * @param fk_SFTable 外键表
     * @param x 位置
     * @param y 位置
     */
    public static void SaveFieldSFTable(String fk_mapdata, String fieldName, String fieldDesc, String fk_SFTable, float x, float y)
    {
        //外键字段表.
        SFTable sf = new SFTable(fk_SFTable);

        MapAttr attr = new MapAttr();
        attr.setMyPK(fk_mapdata + "_" + fieldName);
        attr.RetrieveFromDBSources();

        //基本属性赋值.
        attr.setFK_MapData(fk_mapdata);
        attr.setKeyOfEn(fieldName);
        attr.setName(fieldDesc);
        attr.setMyDataType(BP.DA.DataType.AppString);
        attr.setUIContralType(BP.En.UIContralType.DDL);
        attr.setUIBindKey(fk_SFTable); //绑定信息.
        attr.setX(x);
        attr.setY(y);

        //根据外键表的类型不同，设置它的LGType.
        switch (sf.getSrcType())
        {
            case BPClass:
            case CreateTable:
            case TableOrView:
                attr.setLGType(FieldTypeS.FK);
                break;
            default:
                attr.setLGType(FieldTypeS.Normal);
                break;
        }
        attr.Save();
       
        //如果是普通的字段, 这个属于外部数据类型,或者webservices类型.
        if (attr.getLGType() == FieldTypeS.Normal)
        {
            MapAttr attrH = new MapAttr();
            attrH.Copy(attr);
            attrH.setKeyOfEn(attr.getKeyOfEn() + "T");
            attrH.setName(attr.getName());
            attrH.setUIContralType(BP.En.UIContralType.TB);
            attrH.setMinLen(0);
            attrH.setMaxLen(60);
            attrH.setMyDataType(BP.DA.DataType.AppString);
            attrH.setUIVisible(false);
            attrH.setUIIsEnable(false);
            attrH.setMyPK(attrH.getFK_MapData() + "_" + attrH.getKeyOfEn());
            attrH.Save();
        }
    }
    
    public static void NewEnumField(String fk_mapdata, String field, String fieldDesc, String enumKey, UIContralType ctrlType, float x, float y)
    {

        MapAttr ma = new MapAttr();
        ma.setFK_MapData(fk_mapdata);
        ma.setKeyOfEn(field);
        ma.setName(fieldDesc);
        ma.setMyDataType(DataType.AppInt);
        ma.setX(x);
        ma.setY(y);
        ma.setUIIsEnable(true);
        ma.setLGType(FieldTypeS.Enum);
        ma.setUIContralType(ctrlType);
        ma.setUIBindKey(enumKey);
        ma.Insert();

        if (ma.getUIContralType() != UIContralType.RadioBtn)
        {
        	return;
        }

        //删除可能存在的数据.
        BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmRB WHERE KeyOfEn='"+ma.getKeyOfEn()+"' AND FK_MapData='"+ma.getFK_MapData()+"'");

        SysEnums ses = new SysEnums(ma.getUIBindKey());
        int idx = 0;
        for(SysEnum item : ses.ToJavaList())
        {
            idx++;
            FrmRB rb = new FrmRB();
            rb.setFK_MapData(ma.getFK_MapData());
            rb.setKeyOfEn(ma.getKeyOfEn());
            rb.setEnumKey(ma.getUIBindKey());

            rb.setLab(item.getLab());
            rb.setIntKey(item.getIntKey());
            rb.setX(ma.getX());

            //让其变化y值.
            rb.setY(ma.getY() + idx * 30);
            rb.Insert();
         }
     }
    /**
     *  创建字段
     * @param fk_mapdata 表单ID
     * @param field 字段ID
     * @param fieldDesc 字段中文名
     * @param mydataType  
     * @param x 位置
     * @param y 位置
     */
    public static void NewField(String frmID, String field, String fieldDesc,int mydataType, float x, float y)
    {
        MapAttr ma = new MapAttr();
        ma.setFK_MapData(frmID);
        ma.setKeyOfEn(field);
        ma.setName(fieldDesc);
        ma.setMyDataType(mydataType);
        ma.setX(x);
        ma.setY(y);
        ma.Insert();
    }
  /// <summary>
    /// 创建字段分组
    /// </summary>
    /// <param name="frmID"></param>
    /// <param name="gKey"></param>
    /// <param name="gName"></param>
    /// <returns></returns>
    public static String NewCheckGroup(String frmID, String gKey, String gName)
    {
        MapAttr attrN = new MapAttr();
        int i = attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Note");
        i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Checker");
        i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_RDT");
        if (i > 0)
        {
        	return "error:前缀已经使用：" + gKey + " ， 请确认您是否增加了这个审核分组或者，请您更换其他的前缀。";
        }

        GroupField gf = new GroupField();
        gf.setLab(gName);
        gf.setEnName(frmID);
        gf.Insert();

        attrN = new MapAttr();
		attrN.setFK_MapData(frmID);
        attrN.setKeyOfEn(gKey + "_Note");
        attrN.setName("审核意见");
        attrN.setMyDataType(DataType.AppString);
        attrN.setUIContralType(UIContralType.TB);
        attrN.setUIIsEnable(true);
        attrN.setUIIsLine(true);
        attrN.setMaxLen(4000);
        attrN.setGroupID((int)gf.getOID());
        attrN.setUIHeight(23 * 3);
        attrN.setIdx(1); 
        attrN.Insert();

        attrN = new MapAttr();
        attrN.setFK_MapData(frmID);
        attrN.setKeyOfEn(gKey + "_Checker");
        attrN.setName("审核人");// "审核人");
        attrN.setMyDataType(DataType.AppString);
        attrN.setUIContralType(UIContralType.TB);
        attrN.setMaxLen(50);
        attrN.setMinLen(0);
        attrN.setUIIsEnable(true);
        attrN.setUIIsLine(false);
        attrN.setDefVal("@WebUser.No");
        attrN.setUIIsEnable(false);
        attrN.setGroupID((int)gf.getOID());
        attrN.setIsSigan(true);
        attrN.setIdx(2);
        attrN.Insert();

        attrN = new MapAttr();
        attrN.setFK_MapData(frmID);
        attrN.setKeyOfEn(gKey + "_RDT");
        attrN.setName("审核日期"); // "审核日期" 
        attrN.setMyDataType(DataType.AppDateTime);
        attrN.setUIContralType(UIContralType.TB);
        attrN.setUIIsEnable(true);
        attrN.setUIIsLine(false);
        attrN.setDefVal("@RDT");
        attrN.setUIIsEnable(false);
        attrN.setGroupID((int)gf.getOID());
        attrN.setIdx(3);
        attrN.Insert();

        /*
         * 判断是否是节点设置的审核分组，如果是就为节点设置焦点字段。
         */
        frmID = frmID.replace("ND", "");
        int nodeid = 0;
        try
        {
            nodeid = Integer.parseInt(frmID);
        }
        catch(Exception e)
        {
            //转化不成功就是不是节点表单字段.
            return "error:只能节点表单才可以使用审核分组组件。";
        }
        return null;
    }
    /** 
	 仅仅获取数据
	 @param frmID 表单ID
	 @param pkval 主键
	 @return 数据
	*/
	public static DataSet GenerDBForVSTOExcelFrmModel(String frmID, int pkval)
	{
		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();
		//映射实体.
		MapData md = new MapData(frmID);
		//实体.
		GEEntity wk = new GEEntity(frmID);
		wk.setOID(pkval);
		if (wk.RetrieveFromDBSources() == 0)
		{
			wk.Insert();
		}

		//属性.
		MapExt me = null;
		DataTable dtMapAttr = null;
		MapExts mes = null;
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

		//主表的配置信息.
		sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + frmID + "'";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		myds.Tables.add(dt);

		for (MapDtl item : md.getMapDtls().ToJavaList())
		{
			//明细表的主表描述
			sql = "SELECT * FROM Sys_MapDtl WHERE No='" + item.getNo() + "'";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapDtl_For_" + item.getNo();
			myds.Tables.add(dt);

			//明细表的表单描述
			sql = "SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + item.getNo() + "'";
			dtMapAttr = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtMapAttr.TableName = "Sys_MapAttr_For_" + item.getNo();
			myds.Tables.add(dtMapAttr);

			//明细表的配置信息.
			sql = "SELECT * FROM Sys_MapExt WHERE FK_MapData='" + item.getNo() + "'";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Sys_MapExt_For_" + item.getNo();
			myds.Tables.add(dt);

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

				if (lgType.equals("1"))
				{
					// 如果是枚举值, 判断是否存在.
					if (myds.Tables.contains(uiBindKey) == true)
					{
						continue;
					}

					String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
					DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
					dtEnum.TableName = uiBindKey;
					myds.Tables.add(dtEnum);
					continue;
				}
				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) //字段未启用
				{
					continue;
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();

				Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
				if (me != null) //有范围限制时
				{
					Object tempVar2 = me.getDoc();
					String fullSQL = (String)((tempVar2 instanceof String) ? tempVar2 : null);
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
					dt = DBAccess.RunSQLReturnTable(fullSQL);


					dt.TableName = mypk;
					myds.Tables.add(dt);
					continue;
				}
				else //无范围限制时
				{
					// 判断是否存在.
					if (myds.Tables.contains(uiBindKey) == true)
					{
						continue;
					}

					myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
				}
		if (BP.Sys.SystemConfig.getIsBSsystem() == true)
		{
//			// 处理传递过来的参数。
//			for (String k : System.Web.HttpContext.Current.Request.QueryString.AllKeys)
//			{
//				wk.SetValByKey(k, System.Web.HttpContext.Current.Request.QueryString[k]);
//			}
			Enumeration enu  = ContextHolderUtils.getRequest().getParameterNames();
			while(enu.hasMoreElements())
			{
				String k = (String) enu.nextElement();
				wk.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}
		}

		// 执行表单事件..
		String msg = md.getFrmEvents().DoEventNode(FrmEventList.FrmLoadBefore, wk);
		if (StringHelper.isNullOrEmpty(msg) == false)
		{
			throw new RuntimeException("err@错误:" + msg);
		}
		//重设默认值.
		wk.ResetDefaultVal();

		//执行装载填充.
		me = new MapExt();
		me.setMyPK(frmID + "_" + MapExtXmlList.PageLoadFull);
		if (me.RetrieveFromDBSources() == 1)
		{
			//执行通用的装载方法.
			MapAttrs attrs = new MapAttrs(frmID);
			MapDtls dtls = new MapDtls(frmID);
			Entity tempVar3 = BP.WF.Glo.DealPageLoadFull(wk, me, attrs, dtls);
			wk = (GEEntity)((tempVar3 instanceof GEEntity) ? tempVar3 : null);
		}

		//增加主表数据.
		DataTable mainTable = wk.ToDataTableField(md.getNo());
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);
		for (MapDtl dtl : md.getMapDtls().ToJavaList())
		{
			GEDtls dtls = new GEDtls(dtl.getNo());
			QueryObject qo = null;
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
					case ForFID: // 按流程ID来控制.
						qo.AddWhere(GEDtlAttr.FID, pkval);
						break;
				}
			}
			catch (java.lang.Exception e)
			{
				dtls.getGetNewEntity().CheckPhysicsTable();
			}

			//从表
			DataTable dtDtl = qo.DoQueryToTable();

			// 为明细表设置默认值.
			MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
			for (MapAttr attr : dtlAttrs.ToJavaList())
			{
				//处理它的默认值.
				if (attr.getDefValReal().contains("@") == false)
				{
					continue;
				}

				for (DataRow dr2 : dtDtl.Rows)
				{
					dr2.put(attr.getKeyOfEn(),attr.getDefVal());
				}
			}

			dtDtl.TableName = dtl.getNo(); //修改明细表的名称.
			myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
		}
		//dtMapAttr = myds.Tables["Sys_MapAttr"];
		for(DataTable dta :myds.Tables)
		{
			if("Sys_MapAttr".equals(dta.getTableName()))
			{
				dtMapAttr = dta;
			}
		}
		mes = md.getMapExts();
		for (DataRow dr1 : dtMapAttr.Rows)
		{
			String uiBindKey1 = dr1.get("UIBindKey").toString();
			String myPK = dr1.get("MyPK").toString();
			String lgType1 = dr1.get("LGType").toString();
			if (lgType1.equals("1"))
			{
				// 如果是枚举值, 判断是否存在., 
				if (myds.Tables.contains(uiBindKey1) == true)
				{
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + uiBindKey1 + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey1;
				myds.Tables.add(dtEnum);
				continue;
			}

			if (!lgType1.equals("2"))
			{
				continue;
			}

			String UIIsEnable1 = dr1.get("UIIsEnable").toString();
			if (UIIsEnable1.equals("0"))
			{
				continue;
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn1 = dr1.get("KeyOfEn").toString();
			String fk_mapData = dr1.get("FK_MapData").toString();

			Object tempVar4 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn1);
			me = (MapExt)((tempVar4 instanceof MapExt) ? tempVar4 : null);
			if (me != null)
			{
				Object tempVar5 = me.getDoc();
				String fullSQL = (String)((tempVar5 instanceof String) ? tempVar5 : null);
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				dt.TableName = myPK; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				myds.Tables.add(dt);
				continue;
			}

			dt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey1);
			dt.TableName = uiBindKey1;
			myds.Tables.add(dt);
		}
		//返回生成的dataset.
		return myds;
			}
		}
		return myds;
	}
	
	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas)
	{
		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//映射实体.
		MapData md = new MapData(frmID);

		//实体.
		GEEntity wk = new GEEntity(frmID);
		wk.setOID( pkval);
		if (wk.RetrieveFromDBSources() == 0)
			wk.Insert();

		//把参数放入到 En 的 Row 里面。
		if ( DataType.IsNullOrEmpty(atParas) == false)
		{
			AtPara ap = new AtPara(atParas);
			
//			for (String key : ap.getHisHT().keys() )
			for (String key : ap.getHisHT().keySet() )
			{
                try
                {

//                    if (wk.getRow().ContainsKey(key) == true) //有就该变.
//                        wk.getRow().get(key) = ap.GetValStrByKey(key);
                    if (wk.getRow().containsKey(key) == true) //有就该变.
                    	wk.getRow().SetValByKey_2017(key,ap.GetValStrByKey(key));

                    else
//                        wk.getRow().dd(key, ap.GetValStrByKey(key)); //增加他
                    	wk.getRow().put(key, ap.GetValStrByKey(key));
                }
                catch(Exception ex)
                {
//                    throw new Exception(key);
                    Log.DebugWriteError(ex.getMessage());
                }
			}
		}

		//#region 加载从表表单模版信息.

        DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
        myds.Tables.add(Sys_MapDtl);

		//明细表的表单描述
        DataTable Sys_MapAttr = dtl.getMapAttrs().ToDataTableField("Sys_MapAttr");
        myds.Tables.add(Sys_MapAttr);

		//明细表的配置信息.

        DataTable Sys_MapExt = dtl.getMapExts().ToDataTableField("Sys_MapExt");
        myds.Tables.add(Sys_MapExt);

		//#region 把从表的- 外键表/枚举 加入 DataSet.
        MapExts mes = dtl.getMapExts();
        MapExt me = null;

        for (DataRow dr : Sys_MapAttr.Rows)
        {
            String lgType = dr.getValue("LGType").toString();
            //不是枚举/外键字段
            if (lgType.equals("0"))
                continue;

            String uiBindKey = dr.getValue("UIBindKey").toString();
            String mypk = dr.getValue("MyPK").toString();

           // #region 枚举字段
            if (lgType.equals("1"))
            {
                // 如果是枚举值, 判断是否存在.
                if (myds.Tables.contains(uiBindKey) == true)
                    continue;

                String mysql = "SELECT IntKey AS \"No\", Lab as \"Name\" FROM Sys_Enum WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
                DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
                dtEnum.TableName = uiBindKey; 

                myds.Tables.add(dtEnum);
                continue;
            }
            //#endregion

            //#region 外键字段
            String UIIsEnable = dr.getValue("UIIsEnable").toString();
            if (UIIsEnable.equals("0")) //字段未启用
                continue;

            // 检查是否有下拉框自动填充。
            String keyOfEn = dr.getValue("KeyOfEn").toString();

           // #region 处理下拉框数据范围. for 小杨.
        	Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
			
           // me = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn) as MapExt;
            if (me != null) //有范围限制时
            {
                String fullSQL = (String)me.getDoc();
                fullSQL = fullSQL.replace("~", ",");
                fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);

                DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);

                dt.TableName = uiBindKey;

                dt.Columns.get(0).ColumnName = "No";
                dt.Columns.get(1).ColumnName = "Name";

                myds.Tables.add(dt);
                continue;
            }
            //#endregion 处理下拉框数据范围.

            // 判断是否存在.
            if (myds.Tables.contains(uiBindKey) == true)
                continue;

            myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
            //#endregion 外键字段
        }
		//#endregion 把从表的- 外键表/枚举 加入 DataSet.


		//#endregion 加载从表表单模版信息.

		//#region 把主表数据放入.
		if (BP.Sys.SystemConfig.getIsBSsystem())
		{
			// 处理传递过来的参数。
//			for (String k : System.Web.HttpContext.Current.Request.QueryString.AllKeys)
//			{
//				wk.SetValByKey(k, System.Web.HttpContext.Current.Request.QueryString[k]);
//			}
			Enumeration enu  = ContextHolderUtils.getRequest().getParameterNames();
			while(enu.hasMoreElements())
			{
				String k = (String) enu.nextElement();
				wk.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}
		}

		//重设默认值.
		wk.ResetDefaultVal();


		//增加主表数据.
		DataTable mainTable = wk.ToDataTableField(md.getNo());
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);
		//#endregion 把主表数据放入.

		//#region  把从表的数据放入.
		GEDtls dtls = new GEDtls(dtl.getNo());
		QueryObject qo = null;
        try
        {
            qo = new QueryObject(dtls);
            switch (dtl.getDtlOpenType())
            {
                case ForEmp:  // 按人员来控制.
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
        }
        catch (Exception ex)
        {
            dtls.getGetNewEntity().CheckPhysicsTable();
//            throw ex;
            Log.DebugWriteError(ex.getMessage());
        }

		//条件过滤.
		if (dtl.getFilterSQLExp() != "")
		{
//			String[] strs = dtl.getFilterSQLExp().split('=');
			String[] strs = dtl.getFilterSQLExp().split("=");
			qo.addAnd();
			qo.AddWhere(strs[0], strs[1]);
		}

        //增加排序.
    //    qo.addOrderByDesc( dtls.GetNewEntity.PKField );

		//从表
		DataTable dtDtl = qo.DoQueryToTable();

        //查询所有动态SQL查询类型的字典表记录
        SFTable sftable = null;
        DataTable dtsftable = null;
        DataRow[] drs = null;
        SFTables sftables = new SFTables();
//        sftables.Retrieve(SFTableAttr.SrcType, (int)SrcType.DynamicSQL);
        sftables.Retrieve(SFTableAttr.SrcType, SrcType.SQL);

		// 为明细表设置默认值.
		MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
		for (MapAttr attr : dtlAttrs.ToJavaList())
        {
			//MapAttr attr=(MapAttr)attr11;
           //#region 修改区分大小写.
            if (BP.DA.DBType.Oracle.equals( SystemConfig.getAppCenterDBType() ))
            {
                for (DataColumn dr : dtDtl.Columns )
                {
                    String a = attr.getKeyOfEn();
                    String b = dr.ColumnName;
                    
                    if (attr.getKeyOfEn().toUpperCase().equals( dr.ColumnName ))
                    {
                        dr.ColumnName = attr.getKeyOfEn();
                        continue;
                    }

                    if (attr.getLGType().equals(FieldTypeS.Enum) || attr.getLGType() == FieldTypeS.FK)
                    {
                        if (dr.ColumnName == attr.getKeyOfEn().toUpperCase() + "TEXT")
                        {
                            dr.ColumnName = attr.getKeyOfEn() + "Text";
                        }
                    }
                }
                
                for (DataRow dr : dtDtl.Rows)
                {
                    //本身是大写的不进行修改
                    if (DataType.IsNullOrEmpty(dr.get( attr.getKeyOfEn()) + ""))
                    {
                        dr.setValue( attr.getKeyOfEn() , dr.get(attr.getKeyOfEn().toUpperCase()));
                        
                        dr.setValue(attr.getKeyOfEn().toUpperCase(),  null);
                    }
                }
            }
         //   #endregion 修改区分大小写.

            //处理增加动态SQL查询类型的下拉框选中值Text值，added by liuxc,2017-9-22
            if(attr.getLGType() == FieldTypeS.FK && attr.getUIIsEnable() == false)
            {
                sftable = (SFTable)sftables.GetEntityByKey(attr.getUIBindKey());
                if (sftable != null)
                {
                	
//                    dtsftable = sftable.GenerHisDataTable;
                	dtsftable = sftable.getGenerHisDataTable();

                    //为Text赋值
                    for (DataRow dr : dtDtl.Rows)
                    {
                        drs = dtsftable.Select("No='" + dr.get(attr.getKeyOfEn()) + "'");
                        if (drs.length == 0)
                            continue;

                        dr.setValue(attr.getKeyOfEn() + "Text",  drs[0].getValue("Name"));
                    }
                }
            }

            //处理它的默认值.
			if (attr.getDefValReal().contains("@") == false)
				continue;

			for (DataRow dr : dtDtl.Rows)
//				dr.get(attr.getKeyOfEn()) = attr.getDefVal();
				dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
		}

		dtDtl.TableName = "DBDtl"; //修改明细表的名称.
		myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
		//#endregion 把从表的数据放入.


        //放入一个空白的实体，用与获取默认值.
//        GEDtl dtlBlank = (GEDtl)dtls.GetNewEntity;
        GEDtl dtlBlank = (GEDtl)dtls.getGetNewEntity();
		
        dtlBlank.ResetDefaultVal();

        myds.Tables.add(dtlBlank.ToDataTableField("Blank"));

		return myds;
		
	}
	/** 获取从表数据，用于显示dtl.htm 
	 
	 @param frmID 表单ID
	 @param pkval 主键
	 @param atParas 参数
	 @param specDtlFrmID 指定明细表的参数，如果为空就标识主表数据，否则就是从表数据.
	 @return 数据
*/
	public static DataSet GenerDBForCCFormDtl_old(String frmID, MapDtl dtl, int pkval, String atParas)
	{
		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//映射实体.
		MapData md = new MapData(frmID);

		//实体.
		GEEntity wk = new GEEntity(frmID);
		wk.setOID(pkval);
		if (wk.RetrieveFromDBSources() == 0)
		{
			wk.Insert();
		}

		//把参数放入到 En 的 Row 里面。
		if (DataType.IsNullOrEmpty(atParas) == false)
		{
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet())
			{
				if (wk.getRow().containsKey(key) == true) //有就该变.
				{
					wk.getRow().SetValByKey_2017(key,ap.GetValStrByKey(key));
				}
				else
				{
					wk.getRow().put(key, ap.GetValStrByKey(key)); //增加他.
				}
			}
		}
		///#region 加载从表表单模版信息.

		DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
		myds.Tables.add(Sys_MapDtl);

		//明细表的表单描述
		DataTable Sys_MapAttr = dtl.getMapAttrs().ToDataTableField("Sys_MapAttr");
		myds.Tables.add(Sys_MapAttr);

		//明细表的配置信息.

		DataTable Sys_MapExt = dtl.getMapExts().ToDataTableField("Sys_MapExt");
		myds.Tables.add(Sys_MapExt);

		///#region 把从表的- 外键表/枚举 加入 DataSet.
		MapExts mes = dtl.getMapExts();
		MapExt me = null;

		for (DataRow dr : Sys_MapAttr.Rows)
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
				if (myds.Tables.contains(uiBindKey) == true)
				{
					continue;
				}

				//sunxd 解决ORACLE数据为自动转大写问题 
				// IntKey AS No, Lab as Name 修改 为 IntKey \"No\", Lab \"Name\" 
				String mysql = "SELECT IntKey \"No\", Lab \"Name\" FROM Sys_Enum WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;

				//sunxd 注释掉以下内容
				//解决 Dtl.htm中JS方法 InitPage里的  data = JSON.parse(data);转换报错
				//dtEnum.Columns.Add("No");
				//dtEnum.Columns.Add("Name");

				myds.Tables.add(dtEnum);
				continue;
			}
			///#endregion

			///#region 外键字段
			String UIIsEnable = dr.getValue("UIIsEnable").toString();
			if (UIIsEnable.equals("0")) //字段未启用
			{
				continue;
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();

			///#region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
			if (me != null) //有范围限制时
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = (String)((tempVar2 instanceof String) ? tempVar2 : null);
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);

				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);

				dt.TableName = mypk;

				dt.Columns.Add("No");
				dt.Columns.Add("Name");

				myds.Tables.add(dt);
				continue;
			}
			///#endregion 处理下拉框数据范围.

			// 判断是否存在.
			if (myds.Tables.contains(uiBindKey) == true)
			{
				continue;
			}

			myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
			///#endregion 外键字段
		}
		///#endregion 把从表的- 外键表/枚举 加入 DataSet.


		///#endregion 加载从表表单模版信息.

		///#region 把主表数据放入.
		if (BP.Sys.SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			Enumeration paramNames = ContextHolderUtils.getRequest().getParameterNames();
			while(paramNames.hasMoreElements()){
				String paramName = (String) paramNames.nextElement();  
				String paramValues = ContextHolderUtils.getRequest().getParameter(paramName);
				wk.SetValByKey_2017(paramName, paramValues);
			}
		}

		//重设默认值.
		wk.ResetDefaultVal();


		//增加主表数据.
		DataTable mainTable = wk.ToDataTableField(md.getNo());
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);
		///#endregion 把主表数据放入.

		///#region  把从表的数据放入.
		GEDtls dtls = new GEDtls(dtl.getNo());
		QueryObject qo = null;
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
				case ForFID: // 按流程ID来控制.
					qo.AddWhere(GEDtlAttr.FID, pkval);
					break;
			}
		}
		catch (RuntimeException ex)
		{
			dtls.getGetNewEntity().CheckPhysicsTable();
			throw ex;
		}

		//条件过滤.
		if ( ! dtl.getFilterSQLExp().equals(""))
		{
			String[] strs = dtl.getFilterSQLExp().split("[=]", -1);
			qo.addAnd();
			qo.AddWhere(strs[0], strs[1]);
		}

		//从表
		DataTable dtDtl = qo.DoQueryToTable();

		// 为明细表设置默认值.
		MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
		for (MapAttr attr : dtlAttrs.ToJavaList())
		{
			if (BP.DA.DBType.Oracle == SystemConfig.getAppCenterDBType())//BP.WF.Glo.Plant == Plant.JFlow
			{
				for (DataColumn dr : dtDtl.Columns)
				{
					if (attr.getKeyOfEn().toUpperCase().equals(dr.ColumnName))
					{
						dr.ColumnName = attr.getKeyOfEn();
						continue;
					}

					if (attr.getLGType() == FieldTypeS.Enum || attr.getLGType() == FieldTypeS.FK)
					{
						if (dr.ColumnName == attr.getKeyOfEn().toUpperCase() + "TEXT")
						{
							dr.ColumnName = attr.getKeyOfEn() + "Text";
						}
					}
				}
				//循环修改rows
				for (DataRow dr : dtDtl.Rows)
				{
					//本身是大写的不进行修改
					if(StringUtils.isEmpty(dr.getValue(attr.getKeyOfEn()))){
						dr.setValue2017(attr.getKeyOfEn(), dr.getValue(attr.getKeyOfEn().toUpperCase()));
						dr.keySet().remove(attr.getKeyOfEn().toUpperCase());
					}
				}
			}
			
			
			
			
			
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
		dtDtl.TableName = "DBDtl"; //修改明细表的名称.
		myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
		///#endregion 把从表的数据放入.  


		//放入一个空白的实体，用与获取默认值.
		GEDtl dtlBlank = (GEDtl)((dtls.getGetNewEntity() instanceof GEDtl) ? dtls.getGetNewEntity() : null);
		dtlBlank.ResetDefaultVal();

		myds.Tables.add(dtlBlank.ToDataTableField("Blank"));

		return myds;
	}
}
