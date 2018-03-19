package cn.jflow.system.ui.core;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.AddAllLocation;
import BP.En.Attr;
import BP.En.AttrOfSearch;
import BP.En.AttrSearch;
import BP.En.AttrSearchs;
import BP.En.Attrs;
import BP.En.AttrsOfSearch;
import BP.En.DDLShowType;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityNoName;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.TBType;
import BP.En.UAC;
import BP.Port.Dept;
import BP.Port.Depts;
import BP.Sys.DTSearchWay;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Tools.DateUtils;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Data.NDXRptBaseAttr;
import BP.WF.Rpt.MapRpt;
import BP.WF.Template.FlowDeptDataRightCtrlType;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.UiFatory;

public class ToolBar extends PageBase{

	private boolean isVisible = true;
	public String EnsName;
	public boolean _AddSearchBtn = true;
	public UiFatory uiFactory;

	


	public String getEnsName(){
		if(StringHelper.isNullOrEmpty(EnsName)){
			EnsName = ContextHolderUtils.getRequest().getParameter("EnsName");
		}
		return EnsName;
	}
	
	public void setEnsName(String ensName){
		this.EnsName = ensName;
	}
	
	public ToolBar(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		setEnsName(request.getParameter("EnsName"));
	}
	
	public ToolBar(HttpServletRequest request, HttpServletResponse response, UiFatory uiFactory){
		super(request, response);
		this.uiFactory = uiFactory;
	}
	 public void Page_Load()
     {
		 
     }
	
	 public final void SaveSearchState(String ensName, String key)
		{
			if (StringHelper.isNullOrEmpty(ensName))
			{
				throw new RuntimeException("@EnsName 为空" + ensName);
			}

			UserRegedit ur = new UserRegedit();
			ur.setMyPK(WebUser.getNo() + ensName + "_SearchAttrs");
			ur.RetrieveFromDBSources();
			ur.setFK_Emp(WebUser.getNo());
			ur.setCfgKey("SearchAttrs");
			ur.setSearchKey(key);

			if (StringHelper.isNullOrEmpty(key))
			{
				try
				{
					ur.setSearchKey(this.GetTBByID("TB_Key").getText());
				}
				catch (java.lang.Exception e)
				{
				}
			}

			//查询时间.
			try {
				ur.setDTFrom_Datatime(this.GetTBByID("TB_S_From").getText());
				ur.setDTTo_Datatime(this.GetTBByID("TB_S_To").getText());
			} catch (java.lang.Exception e2) {
			}

			String str = "";
			java.util.Map<String, Object> controlMap = uiFactory.getTmpMap();
			for (java.util.Map.Entry<String, Object> entry : controlMap.entrySet()) {
				if(StringHelper.isNullOrEmpty(entry.getKey())){
					continue;
				}
				
				if(entry.getKey().indexOf("DDL_") == -1){
					continue;
				}
				
				DDL ddl = (DDL)entry.getValue();
				if (ddl.Items.size() == 0)
				{
					continue;
				}

				str += "@" + entry.getKey() + "=" + ddl.getSelectedItemStringVal();
				
			}
			ur.setFK_Emp(WebUser.getNo());
			ur.setCfgKey(ensName + "_SearchAttrs");
			ur.setVals(str);
			try
			{
				ur.setSearchKey(this.GetTBByID("TB_Key").getText());
			}
			catch (java.lang.Exception e3)
			{
			}
			ur.Save();
		}
	
	public QueryObject GetnQueryObject(Entities ens, Entity en){
		if (en.getEnMap().getEnDBUrl().getDBType() == DBType.Oracle)
            return this.GetnQueryObjectOracle(ens, en);
		
		Map map = en.getEnMapInTime();
		QueryObject qo = this.InitQueryObjectByEns(ens, map.IsShowSearchKey, map.DTSearchWay, map.DTSearchKey,
				map.getAttrs(), map.getAttrsOfSearch(), map.getSearchAttrs());
		String pkStr = en.getPK();
		if("No".equals(pkStr)){
			qo.addOrderBy("No");
		}else if("OID".equals(pkStr)){
			qo.addOrderBy("OID");
		}
        return qo;
	}
	
	private QueryObject GetnQueryObjectOracle(Entities ens, Entity en){
		Map map = en.getEnMap();
        QueryObject qo = this.InitQueryObjectByEns(ens, map.IsShowSearchKey, map.DTSearchWay, map.DTSearchKey,
        		map.getAttrs(), map.getAttrsOfSearch(), map.getSearchAttrs());
        if (map.getAttrs().Contains("No"))
            qo.addOrderBy("No");
        return qo;
    }
	
	private QueryObject InitQueryObjectByEns(Entities ens, boolean IsShowSearchKey, DTSearchWay dw, String dtKey, Attrs attrs, 
			AttrsOfSearch attrsOfSearch, AttrSearchs searchAttrs){
		QueryObject qo = new QueryObject(ens);
		String keyVal = "";
        if (IsShowSearchKey) {
            TextBox keyTB = GetTBByID("TB_Key");
            if (keyTB != null) {
                Object obj = this.getValueByComponentId("TB_Key");
                if(obj != null){
//                	keyVal = obj.toString();
                	keyVal = ((String[]) obj)[0];
                }
            }else{
                UserRegedit ur = new UserRegedit();
                QueryObject urObj = new QueryObject(ur);
                urObj.AddWhere("MyPK",WebUser.getNo() + ens.getGetNewEntity().getClassID() + "_SearchAttrs");
                urObj.DoQuery();
                
                keyVal = ur.getSearchKey();
            }
            get_request().getSession().setAttribute("SKey", keyVal);
        }

        if (keyVal.length() >= 1) {
//            Attr attrPK = new Attr();
//            for(Attr attr : attrs) {
//                if (attr.getIsPK()) {
//                    attrPK = attr;
//                    break;
//                }
//            }
            int i = 0;
            for(Attr attr : attrs) {
                switch (attr.getMyFieldType()){
                    case Enum:
                    case FK:
                    case PKFK:
                        continue;
                    default:
                        break;
                }

                if (attr.getMyDataType() != DataType.AppString)
                    continue;

                if (attr.getMyFieldType() == FieldType.RefText)
                    continue;

                if ("FK_Dept".equals(attr.getKey()))
                    continue;

                i++;
                if (i == 1){
                    /* 第一次进来。 */
                    qo.addLeftBracket();
                    switch (SystemConfig.getAppCenterDBType())
                    {
                        case Oracle:
                        case Informix:
                            qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
                            break;
                        case MySQL:
                            qo.AddWhere(attr.getKey(), " LIKE ", "CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')");
                            break;
                        default:
                            qo.AddWhere(attr.getKey(), " LIKE ", " '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'");
                            break;
                    }
                    continue;
                }
                qo.addOr();

                switch (SystemConfig.getAppCenterDBType())
                {
                    case Oracle:
                    case Informix:
                        qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
                        break;
                    case MySQL:
                        qo.AddWhere(attr.getKey(), " LIKE ", "CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')");
                        break;
                    default:
                        qo.AddWhere(attr.getKey(), " LIKE ", "'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'");
                        break;
                }
            }
            qo.getMyParas().Add("SKey", keyVal);
            qo.addRightBracket();
        }else{
            qo.addLeftBracket();
            qo.AddWhere("abc", "all");
            qo.addRightBracket();
        }
        //普通属性
        String opkey = ""; // 操作符号。
        for(AttrOfSearch attr : attrsOfSearch){
            if (attr.getIsHidden()){
                qo.addAnd();
                qo.addLeftBracket();
                qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(), attr.getDefaultValRun());
                qo.addRightBracket();
                continue;
            }

            if (attr.getSymbolEnable()){
                DDL ddl = GetDDLByKey("DDL_" + attr.getKey());
                if(ddl == null){
                	continue;
                }
                opkey = ddl.getSelectedItemStringVal();
                if ("all".equals(opkey))
                    continue;
            }else{
                opkey = attr.getDefaultSymbol();
            }

            qo.addAnd();
            qo.addLeftBracket();

            if (attr.getDefaultVal().length() >= 8){
                String date = "2005-09-01";
                try{
                    /* 就可能是年月日。 */
                    String y = this.GetDDLByKey("DDL_" + attr.getKey() + "_Year").getSelectedItemStringVal();
                    String m = this.GetDDLByKey("DDL_" + attr.getKey() + "_Month").getSelectedItemStringVal();
                    String d = this.GetDDLByKey("DDL_" + attr.getKey() + "_Day").getSelectedItemStringVal();
                    date = y + "-" + m + "-" + d;

                    if ("<=".equals(opkey))
                    {
                        Date dt = DateUtils.addDay(DataType.ParseSysDate2DateTime(date), 1);
                        date = DateUtils.format(dt, DataType.getSysDataFormat());
                    }
                }catch(Exception e){e.printStackTrace();}

                qo.AddWhere(attr.getRefAttrKey(), opkey, date);
            }
            else
            {
                qo.AddWhere(attr.getRefAttrKey(), opkey, this.GetTBByID("TB_" + attr.getKey()).getText());
            }
            qo.addRightBracket();
        }
        //外键
        for(AttrSearch attr1 : searchAttrs)
        {
            Attr attr = attr1.HisAttr;

            if (attr.getMyFieldType() == FieldType.RefText)
                continue;

            DDL ddl = this.GetDDLByKey("DDL_" + attr.getKey());
            if (ddl.Items.size() == 0)
                continue;
            Object obj = this.getValueByComponentId("DDL_" + attr.getKey());
            String selectVal = "";
            if(obj == null){
            	selectVal = ddl.getSelectedItemStringVal();
            }else{
            	selectVal = ((String[]) obj)[0];
            }
            
            if ("all".equals(selectVal))
                continue;

            if ("mvals".equals(selectVal)){
                UserRegedit sUr = new UserRegedit();
                sUr.setMyPK(WebUser.getNo() + getEnsName() + "_SearchAttrs");
                sUr.RetrieveFromDBSources();

                /* 如果是多选值 */
                String cfgVal = sUr.getMVals();
                AtPara ap = new AtPara(cfgVal);
                String key = attr.getKey();
                String instr = ap.GetValStrByKey(key);
                if (instr == null || "".equals(instr)){
                    if ("FK_Dept".equals(key) || "FK_Unit".equals(key)){
                        if ("FK_Dept".equals(key)){
                            selectVal = WebUser.getFK_Dept();
                            ddl.SetSelectItemByIndex(0);
                        }
                    }else{
                        continue;
                    }
                }else{
                	// 小周鹏添加，公用多项组合查询时，不能查询数据 Start
                	if(instr.indexOf(".") != 0){
                		instr = "."+instr;
                	}
                	if(instr.indexOf(".") != instr.length()-1){
                		instr = instr + ".";
                	}
                	// 小周鹏添加，公用多项组合查询时，不能查询数据 End
                    instr = instr.replace("..", ".");
                    instr = instr.replace(".", "','");
                    instr = instr.substring(2);
                    instr = instr.substring(0, instr.length() - 2);

                    qo.addAnd();
                    qo.addLeftBracket();
                    qo.AddWhereIn(key, "(" + instr + ")");
                    qo.addRightBracket();
                    continue;
                }
            }

            qo.addAnd();
            qo.addLeftBracket();

            if ("BP.Port.Depts".equals(attr.getUIBindKey()) || "BP.Port.Units".equals(attr.getUIBindKey()))  //判断特殊情况。
                qo.AddWhere(attr.getKey(), " LIKE ", selectVal + "%");
            else
                qo.AddWhere(attr.getKey(), selectVal);
            qo.addRightBracket();
        }
        if (dw != DTSearchWay.None)
        {
            String dtFrom = this.GetTBByID("TB_S_From").getText().trim();
            String dtTo = this.GetTBByID("TB_S_To").getText().trim();
            if (dw == DTSearchWay.ByDate)
            {
                qo.addAnd();
                qo.addLeftBracket();
                qo.setSQL("'" + dtKey + "' >= '" + dtFrom + " 01:01'");
                qo.addAnd();
                qo.setSQL("'" + dtKey + "' <= '" + dtTo + " 23:59'");
                qo.addRightBracket();
            }

            if (dw == DTSearchWay.ByDateTime)
            {
                qo.addAnd();
                qo.addLeftBracket();
                qo.setSQL("'" + dtKey + "' >= '" + dtFrom + "'");
                qo.addAnd();
                qo.setSQL("'" + dtKey + "' <= '" + dtTo + "'");
                qo.addRightBracket();
            }
        }
		return qo;
    }
	
	public void InitFuncEn(UAC uac, Entity en)
    {
        if (en.getEnMap().getEnType() == EnType.View)
            uac.Readonly();


        if (uac.IsInsert)
        {
            if (en.getEnMap().getEnType() != EnType.Dtl)
            {
                this.AddLinkBtn(NamesOfBtn.New, "新建(N)");
                //this.GetLinkBtnByID(NamesOfBtn.New).OnClientClick = "this.disabled=true;"; 
            }
        }

        if (uac.IsUpdate)
        {
            this.AddLinkBtn(NamesOfBtn.Save, "保存(S)");
            this.AddLinkBtn(NamesOfBtn.SaveAndClose, "保存并关闭");
            //this.GetLinkBtnByID(NamesOfBtn.Save).OnClientClick = "this.disabled=true;"; 
        }

        if (uac.IsInsert && uac.IsUpdate)
        {
            if (en.getEnMap().getEnType() != EnType.Dtl)
            {
                this.AddLinkBtn(NamesOfBtn.SaveAndNew, "保存并新建(R)");
                //this.GetLinkBtnByID(NamesOfBtn.SaveAndNew).OnClientClick = "this.disabled=true;"; 
            }
        }

        String pkval = en.getPKVal().toString();

        if (uac.IsDelete && pkval != "0" && pkval.length() >= 1)
        {
            this.AddLinkBtn(NamesOfBtn.Delete, "删除(D)");
            // this.GetLinkBtnByID(NamesOfBtn.Delete).OnClientClick = "this.disabled=true;"; 
        }

        if (uac.IsAdjunct)
        {
            this.AddLinkBtn(NamesOfBtn.Adjunct);
            if (en.equals("") == false)
            {
                int i = DBAccess.RunSQLReturnValInt("select COUNT(*) from Sys_FileManager WHERE RefTable='" + en.toString() + "' AND RefKey='" + en.getPKVal() + "'");
                if (i != 0)
                {
//                    this.GetLinkBtnByID(NamesOfBtn.Adjunct).Text += "-" + i;
                }
            }
        }
    }
	
	 public void AddLinkBtn(NamesOfBtn id, String text)
     {
         LinkButton en = uiFactory.creatLinkButton(true, id.getCode(), text);

//         if (id == "Btn_Delete")
//             en.Attributes["onclick"] = "return confirm('您确定要执行删除吗？');";

         uiFactory.append(en);
     }
	 
	 
	 public void AddLinkBtn(NamesOfBtn id, String text, String js) {
		 LinkButton en = uiFactory.creatLinkButton(true, id.getCode(), text);
			en.setHref(js);
			  uiFactory.append(en);
		}


	 
	 public void AddLinkBtn(NamesOfBtn id)
     {
         LinkButton btn = uiFactory.creatLinkButton(true, id.getCode(), id.getDesc());

//         switch (id)
//         {
//             case NamesOfBtn.Delete:
//                 btn.Attributes["onclick"] = "return confirm('您确定要执行删除吗？');";
//                 break;
//             default:
//                 break;
//         }

         uiFactory.append(btn);
     }
	public void InitToolbarOfMapRpt(Flow fl, MapRpt currMapRpt, String RptNo, Entity en, int pageidx){
		if(uiFactory == null){
			uiFactory = new UiFatory();
		}
		Map map = en.getEnMap();
		InitByMapV2(map, 1, RptNo);
		
		//特殊处理权限.
        AttrSearchs searchs = map.getSearchAttrs();
        String defVal = "";
        DataTable dt = null;
        for(AttrSearch attr : searchs.subList(0, searchs.size()))
        {
            Object obj = uiFactory.getTmpMap().get("DDL_" + attr.Key);
            if (obj == null)
                continue;
            DDL mydll = (DDL) obj;
            Object def = this.getValueByComponentId("DDL_" + attr.Key);
            if(def != null){
            	defVal = ((String[]) def)[0];
            }else{
            	defVal = mydll.getSelectedItemStringVal();
            }
            mydll.addAttr("onchange", "DDL_mvals_OnChange(this,'" + RptNo + "','" + attr.Key + "')");
            String tempKey = attr.HisAttr.getKey();
            if("FK_NY".equals(tempKey)){
            	dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_NY FROM " + currMapRpt.getPTable() + " WHERE FK_NY!='' ORDER BY FK_NY");
                mydll.Items.clear();
                mydll.Items.add(new ListItem("=>月份", "all"));
                for(DataRow dr : dt.Rows.subList(0, dt.Rows.size())){
                    mydll.Items.add(new ListItem(dr.getValue(0).toString(), dr.getValue(0).toString()));
                }
                mydll.SetSelectItem(defVal);
            }else if("FlowStarter".equals(tempKey) || "FlowEnder".equals(tempKey)){
            	String sql = "";
            	FlowDeptDataRightCtrlType tempEnums = fl.getHisFlowDeptDataRightCtrlType();
                switch (tempEnums) {
                    case MyDeptOnly: // 我的部门.
                        sql = "SELECT No,Name FROM WF_Emp WHERE FK_Dept='" + WebUser.getFK_Dept() + "' AND No IN (SELECT DISTINCT FlowStarter FROM " + currMapRpt.getPTable() + " WHERE FlowStarter!='')";
                        break;
                    case MyDeptAndBeloneToMyDeptOnly: //我的部门，或者隶属我部门下面的部门.
//#warning 这里有错误，怎么递归循环出来?
                        sql = "SELECT No,Name FROM WF_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "'  AND FK_Flow='" + currMapRpt.getFK_Flow() + "') AND No IN (SELECT DISTINCT FlowStarter FROM " + currMapRpt.getPTable() + " WHERE FlowStarter!='')";
                        break;
                    case BySpecFlowDept: // 指定权限.
                        sql = "SELECT No,Name FROM WF_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "'  AND FK_Flow='" + currMapRpt.getFK_Flow() + "') AND No IN (SELECT DISTINCT FlowStarter FROM " + currMapRpt.getPTable() + " WHERE FlowStarter!='')";
                        break;
                    case AnyoneAndAnydept:  // 任何部门.
                        sql = "SELECT No,Name FROM WF_Emp WHERE No IN (SELECT DISTINCT FlowStarter FROM " + currMapRpt.getPTable() + " WHERE FlowStarter!='')";
                        break;
                    default:
                        break;
                }
                dt = DBAccess.RunSQLReturnTable(sql);

                mydll.Items.clear();
                if (NDXRptBaseAttr.FlowStarter.equals(attr.Key))
                    mydll.Items.add(new ListItem("=>发起人", "all"));
                else
                    mydll.Items.add(new ListItem("=>结束人", "all"));

                for(DataRow dr : dt.Rows.subList(0, dt.Rows.size()))
                {
                    mydll.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
                }
                mydll.SetSelectItem(defVal);
                mydll.addAttr("onchange", "DDL_mvals_OnChange(this,'" + RptNo + "','" + attr.Key + "')");

            }else if("FK_Dept".equals(tempKey)){
                if (!"admin".equals(WebUser.getNo())){
                    dt = DBAccess.RunSQLReturnTable("SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + currMapRpt.getFK_Flow() + "')");
                    if (dt.Rows.size() == 0)
                    {
                        BP.WF.Port.DeptFlowSearch dfs = new BP.WF.Port.DeptFlowSearch();
                        dfs.setFK_Dept(WebUser.getFK_Dept());
                        dfs.setFK_Emp(WebUser.getNo());
                        dfs.setFK_Flow(currMapRpt.getFK_Flow());
                        dfs.setMyPK(WebUser.getFK_Dept() + "_" + WebUser.getNo() + "_" + currMapRpt.getFK_Flow());
                        dfs.Insert();
                        dt = DBAccess.RunSQLReturnTable("SELECT No,Name FROM Port_Dept WHERE No IN (SELECT FK_Dept FROM  WF_DeptFlowSearch WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + currMapRpt.getFK_Flow() + "')");
                    }
                    mydll.Items.clear();
                    for(DataRow dr : dt.Rows.subList(0, dt.Rows.size()))
                        mydll.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
                }

                if (mydll.Items.size() >= 2)
                {
                    ListItem liMvals = new ListItem("*多项组合..", "mvals");
                    liMvals.addAttr("style", "color:green");
                    liMvals.addAttr("color", "green");
                }
                mydll.SetSelectItem(defVal);
            }
        }
	}
	
	public void InitByMapV2(Map map, int page){
		if(uiFactory == null){
			uiFactory = new UiFatory();
		}
        String str = getEnsName();
        if (StringHelper.isNullOrEmpty(str))
            str = get_request().getParameter("EnName");

        if (StringHelper.isNullOrEmpty(str))
            return;

        InitByMapV2(map, page, str);
    }
	
	public void InitByMapV2(Map map, int page, String ensName){
		UserRegedit ur = new UserRegedit(WebUser.getNo(), ensName + "_SearchAttrs");
		String cfgKey = ur.getVals();
		InitByMapV2(map.IsShowSearchKey, map.DTSearchWay, map.getAttrsOfSearch(), map.getSearchAttrs(), null, page, ur);
		String[] keys = cfgKey.split("@");
		java.util.Map<String, Object> tempContollers = uiFactory.getTmpMap();
		for(String ti : tempContollers.keySet()){
			if(ti == null){
				continue;
			}
			if("TB_Key".equals(ti)){
				TextBox tb = (TextBox) tempContollers.get(ti);
				Object obj = this.getValueByComponentId("TB_Key");
				if(obj != null){
					tb.setText(((String[]) obj)[0]);
				}else{
					tb.setText("");
				}
				continue;
			}
			if("TB_S_From".equals(ti)){
				TextBox tb = (TextBox) tempContollers.get(ti);
				if(map.DTSearchWay == DTSearchWay.ByDate){
					tb.setText(ur.getDTFrom_Data());
					tb.addAttr("onfocus", "WdatePicker();");
				}else{
					tb.setText(ur.getDTFrom_Datatime());
				}
				continue;
			}
			if("TB_S_To".equals(ti)){
				TextBox tb = (TextBox) tempContollers.get(ti);
				if(map.DTSearchWay == DTSearchWay.ByDate){
					tb.setText(ur.getDTTo_Data());
					tb.addAttr("onfocus", "WdatePicker();");
				}else{
					tb.setText(ur.getDTFrom_Datatime());
				}
				continue;
			}
			if(ti.indexOf("DDL_") == -1){
				continue;
			}
			if(cfgKey.indexOf(ti) == -1){
				continue;
			}
			for(String key : keys){
				if(key.length() < 3){
					continue;
				}
				if(key.indexOf(ti) == -1){
					continue;
				}
				String[] vals = key.split("=");
				DDL ddl = (DDL) tempContollers.get(ti);
				boolean isHave = ddl.SetSelectItem(vals[1]);
				if(!isHave){
					/*没有有找到要选择的人员*/
					try{
						Attr attr = map.GetAttrByKey(vals[0].replace("DDL_", ""));
                        ddl.SetSelectItem(vals[1], attr);
					}catch(Exception e){e.printStackTrace();}
				}
			}
		}
//		boolean IsCheckRelational = false;

        // 获取大类的选择集合。
        AttrSearchs bigAttrs = new AttrSearchs();
        AttrSearchs temp = map.getSearchAttrs();
        for(AttrSearch attr : temp.subList(0, temp.size())){
            if (attr.RelationalDtlKey == null)
                continue;
            bigAttrs.Add(attr);
        }

        // 遍历他们，为他们生成事件。
        for(AttrSearch attr : bigAttrs.subList(0, bigAttrs.size())) {
            AttrSearch smallAttr = null;
            for(AttrSearch attr1 : temp.subList(0, temp.size())) {
                if ((attr.RelationalDtlKey).equals(attr1.Key))
                    smallAttr = attr1;
            }

            if (smallAttr == null){
            	System.err.println("@您设置的级联子菜单键值在查询集合属性里面不存在。");
            	return;
//                throw new Exception("@您设置的级联子菜单键值在查询集合属性里面不存在。");
            }

            Entities ens = smallAttr.HisAttr.getHisFKEns();
            ens.RetrieveAll();
            Entity en = smallAttr.HisAttr.getHisFKEn();

            // 增加事件.
            DDL ddl = tempContollers.get("DDL_" + attr.Key) == null?null:(DDL) tempContollers.get("DDL_" + attr.Key);
            ddl.addAttr("onchange", "Redirect" + attr.Key + "()");
            DDL ddlSmil = tempContollers.get("DDL_" + attr.RelationalDtlKey) == null?null:(DDL) tempContollers.get("DDL_" + attr.RelationalDtlKey);
            String script = "";
            // 判断级联的方式，是按照编号规则级联还是按照外键级联。
            if (en.getEnMap().getAttrs().Contains(attr.Key))
            {
                /*按照外键或者枚举类型级联 */
            }
            else
            {
                /*按照编号规则级联。*/
                script = "\t\n<script type='text/javascript'>";
                script += "\t\n<!--";
                String arrayStrs = "";
                boolean isfirst = true;
                for(Object obj : ens.subList(0, ens.size())) {
                	EntityNoName en1 = (EntityNoName) obj;
                    if (isfirst) {
                        isfirst = false;
                        arrayStrs += "[\"" + en1.getName() + "\",\"" + en1.getNo() + "\"]";
                    }else{
                        arrayStrs += ",[\"" + en1.getName() + "\",\"" + en1.getNo() + "\"]";
                    }
                }
                script += "\t\n var data" + attr.Key + " = new Array(" + arrayStrs + "); ";
                script += "\t\n Redirect" + attr.Key + "();";
                //数据联动
                script += "\t\n function Redirect" + attr.Key + "(){";
                script += "\t\n	var ddlBig" + attr.Key + " = document.getElementById(\"" + ddl.getId() + "\");";
                script += "\t\n	var ddlSmall" + attr.Key + " = document.getElementById(\"" + ddlSmil.getId() + "\");";
                script += "\t\n	var value_Big" + attr.Key + " = getSelectValue" + attr.Key + "( ddlBig" + attr.Key + " );";
                script += "\t\n	var value_Big_length" + attr.Key + " = value_Big" + attr.Key + ".length;";
                script += "\t\n	var index" + attr.Key + " = 0;";
                script += "\t\n	ddlSmall" + attr.Key + ".options.length = 0;";
                script += "\t\n	for(i=0; i<data" + attr.Key + ".length; i++){					";
                script += "\t\n		if(data" + attr.Key + "[i][1].substr(0, value_Big_length" + attr.Key + ") == value_Big" + attr.Key + "){";
                script += "\t\n			ddlSmall" + attr.Key + ".options[index" + attr.Key + "++] = new Option(data" + attr.Key + "[i][0],data" + attr.Key + "[i][1]);";
                script += "\t\n		}";
                script += "\t\n	}";
                script += "\t\n	ddlSmall" + attr.Key + ".options[0].selected = true;";
                script += "\t\n	}";
                script += " //获取指定下拉列表的值";
                script += "\t\n function getSelectValue" + attr.Key + "(oper) { ";
                script += "\t\n	return oper.options[oper.options.selectedIndex].value;";
                script += "\t\n	} ";
                script += "\t\n	//-->";
                script += "\t\n	</script> ";
            }
            System.out.println("need to add script for html");
            System.out.println(script);
            // 注册他,页面调用到此处时，需要将打印出的东西添加到相应的html页面中
//            this.Page.RegisterClientScriptBlock(attr.Key, script);
        }
	}
	
	private void InitByMapV2(boolean isShowKey, DTSearchWay sw, AttrsOfSearch attrsOfSearch, AttrSearchs attrsOfFK, Attrs attrD1, int page, UserRegedit ur){
		int keysNum = 0;
		if(isShowKey){
			AddLab("Lab_Key", "关键字:&nbsp;");
			TextBox tb = uiFactory.creatTextBox("TB_Key");
			tb.setCols(13);
			uiFactory.append(tb);
			keysNum++;
		}
		uiFactory.append("&nbsp;");
		if (sw != DTSearchWay.None) {
			Label lab1 = uiFactory.creatLabel("Lab_From");
            lab1.setText("日期从:");
            uiFactory.append(lab1);
            TextBox tbDT1 = uiFactory.creatTextBox("TB_S_From");
            if (sw == DTSearchWay.ByDate)
                tbDT1.setShowType(TBType.Date);
            if (sw == DTSearchWay.ByDateTime)
                tbDT1.setShowType(TBType.DateTime);
            uiFactory.append(tbDT1);

            Label lab2 = uiFactory.creatLabel("Lab_To");
            lab2.setText("到:");
            uiFactory.append(lab2);

            TextBox tbDT2 = uiFactory.creatTextBox("TB_S_To");
            if (sw == DTSearchWay.ByDate)
                tbDT2.setShowType(TBType.Date);
            if (sw == DTSearchWay.ByDateTime)
                tbDT2.setShowType(TBType.DateTime);
            uiFactory.append(tbDT2);
        }
		for(AttrOfSearch attr : attrsOfSearch.subList(0, attrsOfSearch.size())){
			String key = attr.getKey();
			if(attr.getIsHidden()){
				continue;
			}
			AddLab("Lab_" + key, attr.getLab());
			keysNum++;
			if (attr.getSymbolEnable()) {
				DDL ddl = uiFactory.creatDDL("DDL_" + key);
                ddl.setSelfShowType(DDLShowType.Ens); //  attr.UIDDLShowType;		 
                ddl.setSelfBindKey("BP.Sys.Operators");
                ddl.setSelfEnsRefKey("No");
                ddl.setSelfEnsRefKeyText("Name");
                ddl.setSelfDefaultVal(attr.getDefaultSymbol());
                ddl.setSelfAddAllLocation(AddAllLocation.None);
                ddl.setSelfIsShowVal(false); ///不让显示编号
                //ddl.ID="DDL_"+attr.Key;
                //ddl.SelfBind();
                ddl.SelfBind();
                uiFactory.append(ddl);
            }
			if (attr.getDefaultVal().length() >= 8) {
                Date mydt = DataType.ParseSysDate2DateTime(attr.getDefaultVal());

                DDL ddl1 = uiFactory.creatDDL("DDL_" + key + "_Year");
                ddl1.setSelfShowType(DDLShowType.Ens);
                ddl1.setSelfBindKey("BP.Pub.NDs");
                ddl1.setSelfEnsRefKey("No");
                ddl1.setSelfEnsRefKeyText("Name");
                ddl1.setSelfDefaultVal(DataType.dateToStr(mydt, "yyyy"));
                ddl1.setSelfAddAllLocation(AddAllLocation.None);
                ddl1.setSelfIsShowVal(false); ///不让显示编号
                ddl1.SelfBind();
                uiFactory.append(ddl1);

                DDL ddl2 = uiFactory.creatDDL("DDL_" + key + "_Month");
                ddl2.setSelfShowType(DDLShowType.Ens);
                ddl2.setSelfBindKey("BP.Pub.YFs");
                ddl2.setSelfEnsRefKey("No");
                ddl2.setSelfEnsRefKeyText("Name");
                ddl2.setSelfDefaultVal(DataType.dateToStr(mydt, "MM"));
                ddl2.setSelfAddAllLocation(AddAllLocation.None);
                ddl2.setSelfIsShowVal(false); ///不让显示编号
                ddl2.SelfBind();
                uiFactory.append(ddl2);

                DDL ddl3 = uiFactory.creatDDL("DDL_" + key + "_Day");
                ddl3.setSelfShowType(DDLShowType.Ens);
                ddl3.setSelfBindKey("BP.Pub.Days");
                ddl3.setSelfEnsRefKey("No");
                ddl3.setSelfEnsRefKeyText("Name");
                ddl3.setSelfDefaultVal(DataType.dateToStr(mydt, "dd"));
                ddl3.setSelfAddAllLocation(AddAllLocation.None);
                ddl3.setSelfIsShowVal(false); ///不让显示编号
                ddl3.SelfBind();
                uiFactory.append(ddl3);
            } else {
                TextBox tb = uiFactory.creatTextBox("TB_" + key);
                tb.setText(attr.getDefaultVal());
                tb.setCols(attr.getTBWidth());
                uiFactory.append(tb);
            }
		}
//        String cfgVal = ur.getVals();
        // 外键属性查询 
        boolean isfirst = true;
        for(AttrSearch attr1 : attrsOfFK.subList(0, attrsOfFK.size())) {
            Attr attr = attr1.HisAttr;
            String key = attr.getKey();
            String desc = attr.getDesc();
            if (attr.getMyFieldType() == FieldType.RefText)
                continue;

            DDL ddl = uiFactory.creatDDL("DDL_" + key);
            keysNum++;
            if (attr.getMyFieldType() == FieldType.Enum) {
            	ddl.BindSysEnum(attr.getUIBindKey(), false, AddAllLocation.TopAndEndWithMVal);
            	ddl.Items.get(0).setText(">>" + desc);
            	ddl.addAttr("onclick", "DDL_mvals_OnChange(this,'" + EnsName + "','" + key + "')");
            } else {
                ListItem liMvals = new ListItem("*多项组合..", "mvals");
                liMvals.addAttr("color", "green");
                liMvals.addAttr("style", "color:green");
                if("BP.Port.Depts".equals(attr.getUIBindKey())){
                	ddl.Items.clear();
                    Depts depts = new Depts();
                    depts.RetrieveAll();
                    for(Object obj : depts.subList(0, depts.size())){
                    	Dept dept = (Dept) obj;
                        String space = "";
                        ListItem li = new ListItem(space + dept.getName(), dept.getNo());
                        ddl.Items.add(li);
                    }
                    if (depts.size() > SystemConfig.getMaxDDLNum())
                        AddLab("lD", "<a href=\"javascript:onDDLSelectedMore('DDL_" + key + "', '" + EnsName + "', 'BP.Port.Depts', 'No','Name')\" >...</a>");

                    if (ddl.Items.size() >= 2)
                        ddl.Items.add(liMvals);

                    ddl.addAttr("onchange", "DDL_mvals_OnChange(this,'" + EnsName + "','" + key + "')");
                }else{
                	ddl.Items.clear();
                    if (attr.getMyDataType() == DataType.AppBoolean)
                    {
                        ddl.Items.add(new ListItem(">>" + desc, "all"));
                        ddl.Items.add(new ListItem("是", "1"));
                        ddl.Items.add(new ListItem("否", "0"));
                        break;
                    }
                    Entities ens = attr.getHisFKEns();
                    if(ens != null){
	                    ens.RetrieveAll();
	                    ddl.Items.add(new ListItem(">>" + desc, "all"));
	                    for(Object obj : ens.subList(0, ens.size())){
	                    	Entity en = (Entity) obj;
	                        ddl.Items.add(new ListItem(en.GetValStrByKey("Name"), en.GetValStrByKey("No")));
	                    }
                    }
                    if (ddl.Items.size() >= 2)
                        ddl.Items.add(liMvals);

                    ddl.addAttr("onchange", "DDL_mvals_OnChange(this,'" + EnsName + "','" + key + "')");
                }
            }
            uiFactory.append(ddl);
            if (isfirst)
                isfirst = false;
        }
        //System.out.println("html label count:::::::::::" + keysNum);
        if (_AddSearchBtn)
            AddLinkBtn(true, NamesOfBtn.Search.getCode(), "查询");
	}
//	public void AddBtn(String id){
//		Button btn = uiFactory.creatButton(id);
//		btn.setText(GetTextById(id));
//		uiFactory.append(btn);
//	}
	public void AddSpt(String id){
		uiFactory.append("&nbsp;");
	}
	public void AddDDL(DDL ddl){
		DDL temp = uiFactory.creatDDL(ddl.getId());
		temp = ddl;
		uiFactory.append(temp);
	}
	public DDL AddDDL(String id){
		DDL ddl = uiFactory.creatDDL(id);
		uiFactory.append(ddl);
		return ddl;
	}
	public CheckBox AddCB(String id){
		CheckBox cb = uiFactory.creatCheckBox(id);
		uiFactory.append(cb);
		return cb;
	}
	public void AddLab(String id, String lab) {
		Label en = uiFactory.creatLabel(id);
        en.setText(lab);
        uiFactory.append(en);
    }
	public void AddLinkBtn(boolean isPlain, String id){
		AddLinkBtn(isPlain, id, GetTextById(id));
	}
	public void AddLinkBtn(boolean isPlain, String id, String text){
		LinkButton lb = uiFactory.creatLinkButton(isPlain, id);
		lb.setText(text);
		if(NamesOfBtn.Delete.equals(id)){
			lb.setHref("return confirm('您确定要执行删除吗？');");
		}
		uiFactory.append(lb);
    }
	public void AddLinkBtn(String id, String text){
		LinkButton lb = uiFactory.creatLinkButton(true, id, text);
		if(NamesOfBtn.Delete.equals(id)){
			lb.setHref("return confirm('您确定要执行删除吗？');");
		}
		uiFactory.append(lb);
    }
	public TextBox GetTBByID(String id){
		return uiFactory.getTmpMap().get(id) == null?null:(TextBox)uiFactory.getTmpMap().get(id);
	}
	public DDL GetDDLByKey(String id){
		return uiFactory.getTmpMap().get(id) == null?null:(DDL)uiFactory.getTmpMap().get(id);
	}
	public LinkButton GetLinkBtnByID(String id) {
		return uiFactory.getTmpMap().get(id) == null?null:(LinkButton)uiFactory.getTmpMap().get(id);
	}
	public LinkButton GetLinkBtnByID(NamesOfBtn search) {
		return uiFactory.getTmpMap().get(search.getCode()) == null?null:(LinkButton)uiFactory.getTmpMap().get(search.getCode());
	}
	private Object getValueByComponentId(String id){
		return get_request().getParameterMap().get(id);
	}
	
	private String GetTextById(String id){
		String text = "";
        switch (NamesOfBtn.getEnumByCode(id))
        {
            case UnDo:
                text = "撤消操作";
                break;
            case Do:
                text = "执行";
                break;
            case ChoseField:
                text = "选择字段";
                break;
            case DataGroup:
                text = "分组查询";
                break;
            case Copy:
                text = "复制";
                break;
            case Go:
                text = "转到";
                break;
            case ExportToModel:
                text = "模板";
                break;
            case DataCheck:
                text = "数据检查";
                break;
            case DataIO:
                text = "数据导入";
                break;
            case Statistic:
                text = "统计";
                break;
            case Balance:
                text = "持平";
                break;
            case Down:
                text = "下降";
                break;
            case Up:
                text = "上升";
                break;
            case Chart:
                text = "图形";
                break;
            case Rpt:
                text = "报表";
                break;
            case ChoseCols:
                text = "选择列查询";
                break;
            case Excel:
                text = "导出全部";
                break;
            case Excel_S:
                text = "导出当前";
                break;
            case Xml:
                text = "导出到Xml";
                break;
            case Send:
                text = "发送";
                break;
            case Reply:
                text = "回复";
                break;
            case Forward:
                text = "转发";
                break;
            case Next:
                text = "下一个";
                break;
            case Previous:
                text = "上一个";
                break;
            case Selected:
                text = "选择";
                break;
            case Add:
                text = "增加";
                break;
            case Adjunct:
                text = "附件";
                break;
            case AllotTask:
                text = "分批任务";
                break;
            case Apply:
                text = "申请";
                break;
            case ApplyTask:
                text = "申请任务";
                break;
            case Back:
                text = "后退";
                break;
            case Card:
                text = "卡片";
                break;
            case Close:
                text = "关闭";
                break;
            case Confirm:
                text = "确定";
                break;
            case Delete:
                text = "删除";
                break;
            case Edit:
                text = "编辑";
                break;
            case EnList:
                text = "列表";
                break;
            case Cancel:
                text = "取消";
                break;
            case Export:
                text = "导出";
                break;
            case FileManager:
                text = "文件管理";
                break;
            case Help:
                text = "帮助";
                break;
            case Insert:
                text = "插入";
                break;
            case LogOut:
                text = "注销";
                break;
            case Messagers:
                text = "消息";
                break;
            case New:
                text = "新建";
                break;
            case Print:
                text = "打印";
                break;
            case Refurbish:
                text = "刷新";
                break;
            case Reomve:
                text = "移除";
                break;
            case Save:
                text = "保存";
                break;
            case SaveAndClose:
                text = "保存并关闭";
                break;
            case SaveAndNew:
                text = "保存并新建";
                break;
            case SaveAsDraft:
                text = "保存草稿";
                break;
            case Search:
                text = "查找(F)";
                break;
            case SelectAll:
                text = "选择全部";
                break;
            case SelectNone:
                text = "不选";
                break;
            case View:
                text = "查看";
                break;
            case Update:
                text = "更新";
                break;
            default:
            	try{
            		throw new Exception("@没有定义ToolBarBtn 标记 " + id);
            	}catch(Exception e){
            		e.printStackTrace();
            	}
        }
        return text;
	}
	
	 public boolean IsExit(NamesOfBtn id){
		 Object obj = this.uiFactory.GetUIByID(id.getCode());
		 if(null != obj){
			 return true;
		 }
		 return false;
	 }
	
	 public boolean IsExit(String id){
		 Object obj = this.uiFactory.GetUIByID(id);
		 if(null != obj){
			 return true;
		 }
		 return false;
	 }
	
	public void AddSpace(int num){
		this.uiFactory.append(DataType.GenerSpace(num));
	}
	public String toString(){
		if(getIsVisible()){
			return uiFactory.ListToString();
		}
		return "";
	}
	public void setIsVisible(boolean isVisible){
		this.isVisible = isVisible;
	}
	public boolean getIsVisible(){
		return isVisible;
	}                                                                                            
/*	public String toString(){
		return uiFactory.ListToString();
	}*/
	public void append(Object obj){
		this.uiFactory.append(obj);
	}
}
