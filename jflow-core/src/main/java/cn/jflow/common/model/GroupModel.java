package cn.jflow.common.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Para;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.AttrSearch;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.UIContralType;
import BP.Sys.DTSearchWay;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Sys.XML.ActiveAttr;
import BP.Sys.XML.ActiveAttrAttr;
import BP.Sys.XML.ActiveAttrs;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Data.NDXRptBaseAttr;
import BP.WF.Rpt.MapRpt;
import BP.Web.WebUser;
import BP.XML.XmlEn;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.CheckBoxList;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.ToolBar;

public class GroupModel extends BaseModel{

	private String title;
	private String rptNo = null;
	private String fkFlow = "";
	private UserRegedit currUR = null;
	private MapRpt currMapRpt;
	private ToolBar toolBar;
	public CheckBoxList checkBoxList1 = new CheckBoxList("CheckBoxList1");
	private CheckBox CB_IsShowPict = new CheckBox();
	private TextBox TB_H = new TextBox();
	private TextBox TB_W = new TextBox();
	private LinkButton lbtnApply;
	private Entities HisEns = null;
	private Entity HisEn = null;
	private String CfgVal = "";
	private String IsContinueNDYF;
	private String NumKey = "";
	public UiFatory UCSys2;
	public StringBuilder UCSys1 = new StringBuilder();
	public StringBuilder UCSys3, UCSys4;
	private String IsPostBack;
	
	public GroupModel(HttpServletRequest request,
			HttpServletResponse response){
		super(request, response);
		
		CB_IsShowPict.setId("CB_IsShowPict");
		CB_IsShowPict.attributes.put("onClick", "State_Changed();");
		CB_IsShowPict.setName("CB_IsShowPict");
		CB_IsShowPict.setText("显示图形");
		CB_IsShowPict.setTitle("注意：仅当“显示内容”选择1项时，图表功能才可用！");
		
		if(null != getParameter("CB_IsShowPict")){
			CB_IsShowPict.setChecked(true);
		}
		
		TB_H.setId("TB_H");
		TB_H.setName("TB_H");
		if(!StringHelper.isNullOrEmpty(getParameter("TB_H"))){
			TB_H.setText(getParameter("TB_H"));
		}else{
			TB_H.setText("460");
		}
		TB_H.addAttr("style", "width: 100px; height: auto; text-align: right");
		
		TB_W.setId("TB_W");
		TB_W.setName("TB_W");
		if(!StringHelper.isNullOrEmpty(getParameter("TB_W"))){
			TB_W.setText(getParameter("TB_W"));
		}else{
			TB_W.setText("500");
		}
		TB_W.addAttr("style", "width: 100px; height: auto; text-align: right");
		
		if(lbtnApply == null){
			lbtnApply = new LinkButton(false, "lbtnApply", "应用");
		}
		lbtnApply.setHref("lbtnApply_Click();");
		lbtnApply.addAttr("iconCls", "icon-ok");
		
		initToolBar();
		initCheckBoxLists();
		initFXPoject();
		
		String btnName = request.getParameter("btnName");
		if(!StringHelper.isNullOrEmpty(btnName)){
			switch (NamesOfBtn.getEnumByCode(btnName)) {
			case Export:
			case Excel:
				DataTable dt = this.initPub();
				try {
					BaseModel.ExportDGToExcel(dt, this.HisEn.getEnMap(), this.HisEn.getEnDesc());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
					String key_value = getParameter("TB_Key");
					if(!StringHelper.isNullOrEmpty(key_value)){
						toolBar.SaveSearchState(getRptNo(), key_value);
						toolBar.GetTBByID("TB_Key").setText(key_value);
					}
					initPub();
				break;
			}
		}else{
			initPub();
		}
		
		
	}
	/**
	 * 初始化ToolBar
	 * @return
	 */
	public void initToolBar(){
		//#region 处理查询权限-通用部分
		//currMapRpt = new MapRpt(getRptNo(), getFkFlow());
		currMapRpt = new MapRpt(getRptNo());
		HisEn = getHisEns().getGetNewEntity();
        Flow fl = new Flow(currMapRpt.getFK_Flow());
        title = fl.getName();
        
        //初始化查询工具栏.
        toolBar = new ToolBar(get_request(), get_response());
        toolBar.InitToolbarOfMapRpt(fl, currMapRpt, getRptNo(), HisEn, 1);
        toolBar.AddLinkBtn(true, NamesOfBtn.Export.getCode()); //增加导出.
        DDL ddl = new DDL();
        ddl.setId("GoTo");
        ddl.Items.add(new ListItem("查询", "Search"));
//      ddl.Items.add(new ListItem("高级查询", "SearchAdv"));
        ddl.Items.add(new ListItem("分组分析", "Group"));
//      ddl.Items.add(new ListItem("交叉报表", "D3"));
//      ddl.Items.add(new ListItem("对比分析", "Contrast"));
        ddl.SetSelectItem(getPageID());
        toolBar.AddDDL(ddl);
        ddl.addAttr("onchange", "ddl_SelectedIndexChanged_Goto()");

        toolBar.GetLinkBtnByID(NamesOfBtn.Search).setHref("ToolBar1_ButtonClick('"+NamesOfBtn.Search.getCode()+"')");
        toolBar.GetLinkBtnByID(NamesOfBtn.Export).setHref("ToolBar1_ButtonClick('"+NamesOfBtn.Export.getCode()+"')");
        //#endregion 处理查询权限
	}
	
	public String getToolBar(){
		return toolBar.toString();
	}
	
	/**
	 * 初始化ToolBar
	 * @return
	 */
	public void initCheckBoxLists(){
		currUR = new UserRegedit(WebUser.getNo(), getRptNo() + "_Group");
		Attrs attrs = HisEn.getEnMap().getAttrs();
		CfgVal = currUR.getVals();
		//#region 处理分组字段的选择状态
		//是否为非首次请求，即第一次请求进，后面所有请求不进
		if(!getIsPostBack()){
            String reAttrs = get_request().getParameter("Attrs");
            // string reAttrs = null; 
            if (StringHelper.isNullOrEmpty(reAttrs))
                reAttrs = currUR.getVals();

            checkBoxList1.Items.clear();
            checkBoxList1.setOnclick("changeSelectedCheckBox()");
            checkBoxList1.addAttr("style", "border-style:None;width:100%");
            for(Attr attr : attrs){
                if (attr.getUIContralType() == UIContralType.DDL)
                {
                    ListItem li = new ListItem(attr.getDesc(), attr.getKey());
                    li.setId("CB_"+attr.getKey());
                    
                    if (reAttrs != null)
                        li.setSelected(reAttrs.contains(attr.getKey()));
                    // 根据状态 设置信息.
                    li.setSelected(CfgVal.contains(attr.getKey()));

                    //加入项目列表.
                    checkBoxList1.Items.add(li);
                }
            }

            if (checkBoxList1.Items.size() == 0){
                try {
					throw new Exception(currMapRpt.getName() + "@没有外键条件，不适合做分组查询"); //没有外键条件，不适合做分组查询。
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
            if (checkBoxList1.Items.size() == 1)
                checkBoxList1.addAttr("style", "display:none");
        }else{
        	checkBoxList1.Items.clear();
            checkBoxList1.setOnclick("changeSelectedCheckBox()");
            checkBoxList1.addAttr("style", "border-style:None;width:100%");
        	for(Attr attr : attrs){
                if (attr.getUIContralType() == UIContralType.DDL)
                {
                    ListItem li = new ListItem(attr.getDesc(), attr.getKey());
                    li.setId("CB_"+attr.getKey());
                    if(null == getParameter(li.getId())){
                    	li.setSelected(false);
                    }else{
                    	li.setSelected(true);
                    }
                    //加入项目列表.
                    checkBoxList1.Items.add(li);
                }
            }
        }
        //#endregion  处理分组字段的选择状态
		
	}
	
	public String getCheckBoxList1(){
		return checkBoxList1.toString();
	}
	
	public String OrderBy = "";
	public String OrderWay = "";
	
	public String getOrderBy() {
		if(!StringHelper.isNullOrEmpty(getParameter("OrderBy"))){
			return getParameter("OrderBy");
		}
		return OrderBy;
	}
	
	public String getOrderWay() {
		if(!StringHelper.isNullOrEmpty(getParameter("OrderWay"))){
			return getParameter("OrderWay");
		}
		return OrderWay;
	}
	
	public String getNumKey() {
		if(!StringHelper.isNullOrEmpty(getParameter("NumKey"))){
			return getParameter("NumKey");
		}
		return NumKey;
	}
	
	public void initFXPoject(){
		//#region 处理变量。
        if (!StringHelper.isNullOrEmpty(getOrderBy())){
            /*检查是否有排序的要求. */
                currUR.setOrderBy(getOrderBy());

            if (getOrderWay().equals("Up"))
                currUR.setOrderWay("DESC");
            else
                currUR.setOrderWay("");

            if (StringHelper.isNullOrEmpty(getNumKey()))
                this.NumKey = currUR.getNumKey();
        }
        OrderBy = currUR.getOrderBy();
        OrderWay = currUR.getOrderWay();
        CfgVal = currUR.getVals();
        
        //如果包含年度年月.
        if (HisEn.getEnMap().getAttrs().Contains("FK_NY")
            && HisEn.getEnMap().getAttrs().Contains("FK_ND"))
        	IsContinueNDYF = "TRUE";
        else
        	IsContinueNDYF = "FALSE";
        //#endregion  处理变量。

        //#region 增加排序
        if (!getIsPostBack())
            CB_IsShowPict.setChecked(currUR.getIsPic());
        //#endregion

        BindNums();
	}
	
	public String getUCSys2(){
		return UCSys2.ListToString();
	}
	
	private void BindNums(){
		// 查询出来关于它的活动列配置。
        ActiveAttrs aas = new ActiveAttrs();
        aas.RetrieveBy(ActiveAttrAttr.For, getRptNo());

        Attrs attrs = HisEn.getEnMap().getAttrs();
        attrs.AddTBInt("MyNum", 1, "流程数量", true, true);
        UCSys2 = new UiFatory();
        UCSys2.append(AddTable("class=\"Table\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%\""));

        for(Attr attr : attrs){
        	String key = attr.getKey();
            //#region 排除不必要的字段。
            if (attr.getUIContralType() != UIContralType.TB)
                continue;

            //edited by liuxc,2014-12-01
            if (!attr.getUIVisible() && !"MyNum".equals(key))
                continue;

            if (!attr.getIsNum())
                continue;
            
            if(NDXRptBaseAttr.OID.equals(key) || NDXRptBaseAttr.FID.equals(key)
            		|| "MID".equals(key) || NDXRptBaseAttr.PWorkID.equals(key)
            		|| NDXRptBaseAttr.FlowEndNode.equals(key) || "WorkID".equals(key)){
            	continue;
            }
            //#endregion 排除不必要的字段。

            //#region 排除配置以外的不需要计算的属性
            boolean isHave = false;
            // 有没有配置抵消它的属性。
            for(XmlEn xmlEn : aas.subList(0, aas.size())){
            	ActiveAttr aa = (ActiveAttr) xmlEn;
                if (!key.equals(aa.getAttrKey()))
                    continue;

                CheckBox cb1 = UCSys2.creatCheckBox("CB_" + key);
                cb1.setText(attr.getDesc());
                if(getIsPostBack()){
                	if(null == getParameter("CB_" + key)){
                		cb1.setChecked(false);
                	}else{
                		cb1.setChecked(true);
                	}
                }else{
                	 if (this.CfgVal.indexOf("@" + key) == -1)
                         cb1.setChecked(false); /* 如果不包含 key .*/
                     else
                         cb1.setChecked(true);
                }

                cb1.addAttr("onclick", "State_Changed();");

                UCSys2.append(AddTR());
                UCSys2.append(AddTD());
                UCSys2.append(cb1.toString());
                UCSys2.append(AddTDEnd());
                UCSys2.append(AddTREnd());
                isHave = true;
            }
            if (isHave)
                continue;
            //#endregion 排除配置以外的不需要计算的属性

            //#region 开始把数值类型的字段增加到列表里.
            UCSys2.append(AddTR());
            CheckBox cb = UCSys2.creatCheckBox("CB_" + key);
            cb.setText(attr.getDesc());
            cb.addAttr("onclick", "State_Changed()");
            if(getIsPostBack()){
            	if(null == getParameter("CB_" + key)){
            		cb.setChecked(false);
            	}else{
            		cb.setChecked(true);
            	}
            }else{
		        if (CfgVal.indexOf("@" + key) == -1)
		            cb.setChecked(false); /* 如果不包含 key .*/
		        else
		        	cb.setChecked(true);
            }
            UCSys2.append("<TD style=\"font-size:12px;text-align:left\">");
            UCSys2.append(cb.toString());
            UCSys2.append(AddTDEnd());

            //#region 处理计算方式。
            DDL ddl = UCSys2.creatDDL("DDL_" + key);
            ddl.Items.add(new ListItem("求和", "SUM"));
            ddl.Items.add(new ListItem("求平均", "AVG"));
            if ("TRUE".equals(IsContinueNDYF.toUpperCase()))
                ddl.Items.add(new ListItem("求累计", "AMOUNT"));

            //#region 处理分析项目的选中。
            if (this.CfgVal.indexOf("@" + key + "=AVG") != -1){
                ddl.SetSelectItemByIndex(1);
            }else if (this.CfgVal.indexOf("@" + key + "=SUM") != -1){
                ddl.SetSelectItemByIndex(0);
            }else if (this.CfgVal.indexOf("@" + key + "=AMOUNT") != -1){
            	ddl.SetSelectItemByIndex(2);
            }else if (this.CfgVal.indexOf("@" + key + "=MAX") != -1){
            	ddl.SetSelectItemByIndex(3);
            }else if (this.CfgVal.indexOf("@" + key + "=MIN") != -1){
            	ddl.SetSelectItemByIndex(4);
            }else if (this.CfgVal.indexOf("@" + key + "=BZC") != -1){
            	ddl.SetSelectItemByIndex(5);
            }else if (this.CfgVal.indexOf("@" + key + "=LSXS") != -1){
            	ddl.SetSelectItemByIndex(6);
            }
            
            if(getIsPostBack()){
            	if(null != getParameter("DDL_" + key)){
            		ddl.SetSelectItem(getParameter("DDL_" + key));
            	}
            }
            //#endregion 处理分析项目的选中。

            ddl.addAttr("onchange", "State_Changed()");

            UCSys2.append("<TD style=\"font-size:12px;text-align:left\">");
            UCSys2.append(ddl.toString());
            UCSys2.append(AddTDEnd());
            UCSys2.append(AddTREnd());
            //#endregion 处理计算方式。
            if (StringHelper.isNullOrEmpty(getNumKey()))
            {
                NumKey = key;
                String ucsys2Temp = UCSys2.ListToString();
                HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(ucsys2Temp, false);
                if(null != controls.get("CB_" + key)){
                	 CheckBox checkBox = (CheckBox) controls.get("CB_" + key);
                	 if(!checkBox.getChecked()){
                		 checkBox.setChecked(true);
                         UCSys2.getTmpList().clear();
                         UCSys2.append(HtmlUtils.setCtrlHtml(checkBox, ucsys2Temp));
                	 }
                }
            }
            //#endregion 开始把数值类型的字段增加到列表里.
        }
        UCSys2.append(AddTableEnd());
	}
	
	public DataTable initPub(){
		UCSys3 = new StringBuilder();
		UCSys4 = new StringBuilder();
		
		String basePath = Glo.getCCFlowAppPath();
		Map tempMap = HtmlUtils.httpParser(UCSys2.ListToString(), false);
	    UCSys2.setTmpMap(tempMap);
	        
		//解决任何数据项目都没有选择的情况.
        this.DealChoseNone();
//        Entities ens = this.HisEns;
        Entity en = this.HisEn;
        BP.En.Map enMap = en.getEnMap();
        
        // 查询出来关于它的活动列配置.
        ActiveAttrs aas = new ActiveAttrs();
        aas.RetrieveBy(ActiveAttrAttr.For, getRptNo());

        Paras myps = new Paras();
        Attrs attrs = enMap.getAttrs();

        // 找到分组的数据. 
        String sqlOfGroupKey = "";
        Attrs attrsOfNum = new Attrs(); //定义字段属性集合变量.
        
        String StateNumKey = "StateNumKey@"; // 为保存操作状态的需要。
        String Condition = ""; //处理特殊字段的条件问题。
        for(Object obj1 : tempMap.keySet()){
        	String id = (String) obj1;
            if (id == null)
                continue;
            if (id.indexOf("CB_") == -1)
                continue;

            String key = id.substring("CB_".length());
            CheckBox cb = (CheckBox) tempMap.get(id);
            if (!cb.getChecked())
                continue;

            attrsOfNum.Add(attrs.GetAttrByKey(key));

            //#region 特殊处理配置的东西.
            Object obj_ddl = tempMap.get("DDL_" + key);
            if (obj_ddl == null){
                ActiveAttr aa = (ActiveAttr)aas.GetEnByKey(ActiveAttrAttr.AttrKey, key);
                if (aa == null)
                    continue;

                Condition += aa.getCondition();
                sqlOfGroupKey += " round(" + aa.getExp() + ",4) AS " + key + ",";
                StateNumKey += key + "=Checked@"; // 记录状态
                continue;
            }
            //#endregion 特殊处理配置的东西.

            //#region 生成sqlOfGroupKey.
            DDL ddl = (DDL) obj_ddl;
            String str = ddl.getSelectedItemStringVal();
            if("SUM".equals(str)){
            	sqlOfGroupKey += " round( SUM(" + key + "), 4) " + key + ",";
                StateNumKey += key + "=SUM@"; // 记录状态
            }else if("AVG".equals(str)){
            	sqlOfGroupKey += " round (AVG(" + key + "), 4)  " + key + ",";
                StateNumKey += key + "=AVG@"; // 记录状态
            }else if("AMOUNT".equals(str)){
            	sqlOfGroupKey += " round ( SUM(" + key + "), 4) " + key + ",";
                StateNumKey += key + "=AMOUNT@"; // 记录状态
            }else{
                try {
					throw new Exception("没有判断的情况.");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
            //#endregion 生成sqlOfGroupKey.
        }
        //求是否有累计字段。
        boolean isHaveLJ = false; // 是否有累计字段。
        if (StateNumKey.indexOf("AMOUNT@") != -1)
            isHaveLJ = true;

        if (sqlOfGroupKey.equals("")){
        	UCSys1.append(AddMsgOfWarning("警告",
                "<img src=\""+basePath+"WF/Img/warning.gif\" /><b><font color=red>您没有选择分析的数据</font></b>")); //您没有选择分析的数据。
            return null;
        }

        /* 如果包含累计数据，那它一定需要一个月份字段。业务逻辑错误。*/

        // 把最后的一个逗号去了。
        sqlOfGroupKey = sqlOfGroupKey.substring(0, sqlOfGroupKey.length() - 1);

        Paras ps = new Paras();
        // 生成 sql.
        String selectSQL = "SELECT ";
        String groupBy = " GROUP BY ";
        Attrs AttrsOfGroup = new Attrs();
        String StatesqlOfGroupKey = "StatesqlOfGroupKey=@"; // 为保存操作状态的需要。
        for(ListItem li : this.checkBoxList1.Items){
        	String value = li.getValue();
            if ("FK_NY".equals(value)){
                /* 如果是年月 分组， 并且如果内部有 累计属性，就强制选择。*/
                if (isHaveLJ)
                    li.setSelected(true);
            }
            if (li.getSelected()){
                selectSQL += value + ",";
                groupBy += value + ",";
                // 加入组里面。
                AttrsOfGroup.Add(attrs.GetAttrByKey(value), false, false);
                StatesqlOfGroupKey += value + "@";
            }
        }

        //去掉最后一个逗号.
        groupBy = groupBy.substring(0, groupBy.length() - 1);

        //“显示图形”的可用性，只有显示内容只选择一项时，才可用 added by liuxc,2014-11-20
        CB_IsShowPict.setEnabled(AttrsOfGroup.size() == 1);
        if (!CB_IsShowPict.getEnabled()){
            CB_IsShowPict.setChecked(false);
            TB_H.setEnabled(false);
            TB_W.setEnabled(false);
            lbtnApply.setEnabled(false);
        }else{
            TB_H.setEnabled(true);
            TB_W.setEnabled(true);
            lbtnApply.setEnabled(true);
        }
        
        //#region 生成Where  通过这个过程产生两个 where.
        // 找到 WHERE 数据。
        String where = " WHERE ";
        String whereOfLJ = " WHERE "; // 累计的 where.
        String url = "";
        Map<String, Object> tempCtrls = this.toolBar.uiFactory.getTmpMap();
        for(String itemId : tempCtrls.keySet())
        {
            //#region 屏蔽特殊情况.
            if (itemId == null)
                continue;
            if (itemId.indexOf("DDL_") == -1)
                continue;
            if (itemId.indexOf("DDL_Form_") == 0 || itemId.indexOf("DDL_To_") == 0)
                continue;

            String key = itemId.substring("DDL_".length());
            DDL ddl = (DDL) tempCtrls.get(itemId);
            if(getIsPostBack()){
            	ddl.SetSelectItem(getParameter(itemId));
            } 
            String val = ddl.getSelectedItemStringVal();
            if ("all".equals(val))
                continue;
            if (val == null)
                continue;
            //#endregion 屏蔽特殊情况.

            //#region 判断多选 case.
            if (val.equals("mvals")){
                UserRegedit sUr = new UserRegedit();
                sUr.setMyPK(WebUser.getNo() + getRptNo() + "_SearchAttrs");
                sUr.RetrieveFromDBSources();

                /* 如果是多选值 */
                String cfgVal = sUr.getMVals();
                AtPara ap = new AtPara(cfgVal);
                String instr = ap.GetValStrByKey(key);
                if (StringHelper.isNullOrEmpty(instr)){
                    if ("FK_Dept".equals(key) || "FK_Unit".equals(key)){
                        if ("FK_Dept".equals(key)){
                            val = WebUser.getFK_Dept();
                            ddl.SetSelectItemByIndex(0);
                        }
                    }else{
                        continue;
                    }
                }else{
                    instr = instr.replace("..", ".");
                    instr = instr.replace("..", ".");
                    instr = instr.replace(".", "','");
                    instr = instr.substring(2);
                    instr = instr.substring(0, instr.length() - 2);
                    where += " " + key + " IN (" + instr + ")  AND ";
                    continue;
                }
            }
            //#endregion 判断多选 case.

            //#region 判断其他字段.
            where += " " + key + " =" + SystemConfig.getAppCenterDBVarStr() + key + "   AND ";
            if (key != "FK_NY")
                whereOfLJ += " " + key + " =" + SystemConfig.getAppCenterDBVarStr() + key + "   AND ";
            myps.Add(key, val);
            //#endregion 判断其他字段
        }
        //#endregion
        
        //#region 加上 where like 条件
        if (enMap.IsShowSearchKey
            && this.toolBar.GetTBByID("TB_Key").getText().trim().length() > 1){
            String key = this.toolBar.GetTBByID("TB_Key").getText().trim();
            if (key.length() > 1){
                String whereLike = "";
                boolean isAddOr = false;
                for(Attr likeKey : attrs){
                    if (likeKey.getIsNum())
                        continue;
                    if (likeKey.getIsRefAttr())
                        continue;

                    if (likeKey.getUIContralType() != UIContralType.TB)
                        continue;

                    switch (likeKey.getMyDataType())
                    {
                        case DataType.AppDate:
                        case DataType.AppDateTime:
                        case DataType.AppBoolean:
                            continue;
                        default:
                            break;
                    }
                    String field = likeKey.getField();
                    if("MyFileExt".equals(field) || "MyFilePath".equals(field) || "WebPath".equals(field)){
                    	continue;
                    }

                    if (!isAddOr){
                        isAddOr = true;
                        whereLike += " " + field + " LIKE '%" + key + "%' ";
                    }else{
                        whereLike += " OR " + field + " LIKE '%" + key + "%' ";
                    }
                }
                whereLike += " ";
                where += "(" + whereLike + ")";
            }
        }
        //#endregion
        
        //#region 加上日期时间段.
        if (enMap.DTSearchWay != DTSearchWay.None){
            String dtFrom = this.toolBar.GetTBByID("TB_S_From").getText().trim();
            String dtTo = this.toolBar.GetTBByID("TB_S_To").getText().trim();
            String field = enMap.DTSearchKey;
            String addAnd = "";
            if (enMap.IsShowSearchKey && this.toolBar.GetTBByID("TB_Key").getText().trim().length() > 0)
                addAnd = " AND ";

            if (enMap.DTSearchWay == DTSearchWay.ByDate){
                where += addAnd + "( " + field + ">='" + dtFrom + " 01:01' AND " + field + "<='" + dtTo + " 23:59')     ";
            }else{
                where += addAnd + "(";
                where += field + " >='" + dtFrom + "' AND " + field + "<='" + dtTo + "'";
                where += ")";
            }
        }
        //#endregion

        where = where.replace("AND  AND", " AND ");

        if (where.equals(" WHERE "))
        {
            where = "" + Condition.replace("and", "");
            whereOfLJ = "" + Condition.replace("and", "");
        }
        else
        {
            if (where.endsWith(" AND "))
                where = where.substring(0, where.length() - " AND ".length()) + Condition;
            else
                where = where + Condition;

            whereOfLJ = whereOfLJ.substring(0, whereOfLJ.length() - " AND ".length()) + Condition;
        }
        String orderByReq = getParameter("OrderBy");
        String orderby = "";

        if (null != orderByReq && !StringHelper.isNullOrEmpty(getOrderBy()) && (selectSQL.contains(orderByReq) || sqlOfGroupKey.contains(orderByReq)))
        {
            orderby = " ORDER BY " + OrderBy;
            if (!getOrderWay().equals("Up"))
                orderby += " DESC ";
        }

        // 组装成需要的 sql 
        StringBuilder sql = new StringBuilder();
       
        sql.append(selectSQL).append(sqlOfGroupKey).append(" FROM ").append(this.currMapRpt.getPTable()).append(where).append(groupBy).append(orderby);

        // 物理表。
        // this.ResponseWriteBlueMsg(sql);
        myps.SQL = sql.toString();
        DataTable dt2 = DBAccess.RunSQLReturnTable(sql.toString());
        DataTable dt1 = null;
        if(dt2!=null){
        	dt1=dt2.clone();
        }
        dt1.Columns.Add("IDX", Integer.class);
        
        //#region 对他进行分页面
        int myIdx = 0;
        dt1.Rows.clear();
        for(DataRow dr : dt2.Rows){
            myIdx++;
            DataRow mydr = dt1.NewRow();
            mydr.setValue("IDX", myIdx);
            for(DataColumn dc : dt2.Columns){
                mydr.setValue(dc.ColumnName, dr.getValue(dc.ColumnName));
            }
            dt1.Rows.add(mydr);
        }
        //#endregion
        
        //#region 处理 Int 类型的分组列。
        DataTable dt = dt1.clone();
        dt.Rows.clear();
        for(Attr attr : AttrsOfGroup){
            dt.Columns.get(attr.getKey()).setDataType(String.class);
        }
        dt.Rows.addAll(dt1.Rows);
        //#endregion

        // 处理这个物理表 , 如果有累计字段, 就扩展它的列。
        if (isHaveLJ){
            // 首先扩充列.
            for(Attr attr : attrsOfNum){
                if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") == -1)
                    continue;

                switch (attr.getMyDataType())
                {
                    case DataType.AppInt:
                        dt.Columns.Add(attr.getKey() + "Amount", Integer.class);
                        break;
                    default:
                        dt.Columns.Add(attr.getKey() + "Amount", BigDecimal.class);
                        break;
                }
            }

            // 添加累计汇总数据.
            for(DataRow dr : dt.Rows){
                for(Attr attr : attrsOfNum){
                    if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") == -1)
                        continue;

                    sql = sql.delete(0, sql.length()-1);
                    //形成查询sql.
                    if (whereOfLJ.length() > 10){
                     	 sql.append("SELECT SUM(").append(attr.getKey()).append(") FROM ").append(this.currMapRpt.getPTable()).append(whereOfLJ).append(" AND ");
                    } else{
                    	sql.append("SELECT SUM(").append(attr.getKey()).append(") FROM ").append(this.currMapRpt.getPTable()).append(" WHERE ");

                    }
                        
                    for(Attr attr1 : AttrsOfGroup){
                    	String key = attr1.getKey();
                    	if("FK_NY".equals(key)){
                    		sql.append(" FK_NY <= '").append(dr.getValue("FK_NY")).append( "' AND FK_ND='").append(dr.getValue("FK_NY").toString().substring(0, 4)).append("' AND ");
                    	}else if("FK_Dept".equals(key)){
                    		sql.append(key).append( "='").append(dr.getValue(key)).append("' AND ");
                    	}else if("FK_SJ".equals(key) || "FK_XJ".equals(key)){
                    		sql.append(key).append(" LIKE '").append(dr.getValue(key)).append("%' AND ");
                    	}else{
                            sql.append(key).append("='").append(dr.getValue(key)).append("' AND ");
                        }
                    }
                    String sqlTemp = sql.toString().substring(0, sql.length() - "AND ".length());
                    if (attr.getMyDataType() == DataType.AppInt)
                        dr.setValue(attr.getKey() + "Amount", DBAccess.RunSQLReturnValInt(sqlTemp, 0));
                    else
                        dr.setValue(attr.getKey() + "Amount", DBAccess.RunSQLReturnValDecimal(sqlTemp, new BigDecimal(0), 2));
                }
            }
        }

        //开始输出数据table.
        UCSys1.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));

        //#region 输出 分组 列头
        if (StateNumKey.indexOf("=AMOUNT") != -1){
            /* 如果包含累计 */

            // 增加分组条件。
            UCSys1.append(AddTR());  // 开始第一列。
            UCSys1.append("<td rowspan=2 class='Title'>ID</td>");
            for(Attr attr : AttrsOfGroup){
                UCSys1.append("<td rowspan=2 class='GroupTitle'>" + attr.getDesc() + "</td>");
            }
            // 增加数据列
            for(Attr attr : attrsOfNum){
                if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") != -1){
                    /*  如果本数据列 包含累计 */
                    UCSys1.append("<td  colspan=2 class='GroupTitle' >" + attr.getDesc() + "</td>");
                }else{
                    UCSys1.append("<td  rowspan=2 class='GroupTitle' >" + attr.getDesc() + "</td>");
                }
            }
            UCSys1.append(AddTREnd());  // end 开始第一列

            UCSys1.append(AddTR());
            for(Attr attr : attrsOfNum){
                if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") == -1)
                    continue;

                UCSys1.append("<td class='GroupTitle'>本月</td>"); //本月 this.ToE("OrderCondErr")
                UCSys1.append("<td class='GroupTitle'>累计</td>"); //累计
            }
            UCSys1.append(AddTR());
        }else{  /* 如果不包含累计 */
            UCSys1.append(AddTR());
            UCSys1.append(AddTDGroupTitleCenter("序"));

            // 分组条件
            for(Attr attr : AttrsOfGroup){
                if (attr.getKey().equals(getOrderBy())){
                	if("Down".equals(getOrderWay())){
                		UCSys1.append(AddTDGroupTitle("<a href='" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + this.getDoType() + "&RptNo=" + getRptNo() + "&OrderBy=" + attr.getKey() + "&OrderWay=Up' >" + attr.getDesc() + "<img src='"+basePath+"WF/Img/ArrDown.gif' border=0/></a>"));
                		//UCSys1.append(AddTDGroupTitle("<a href='/../../WF/RptDfine/Group.jsp?EnsName=" + this.getEnsName() + "&OrderBy=" + attr.getKey() + "&OrderWay=Up' >" + attr.getDesc() + "<img src='../Img/ArrDown.gif' border=0/></a>"));

                	}else{
                		UCSys1.append(AddTDGroupTitle("<a href='" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + this.getDoType() + "&RptNo=" + getRptNo() + "&OrderBy=" + attr.getKey() + "&OrderWay=Down' >" + attr.getDesc() + "<img src='"+basePath+"WF/Img/ArrUp.gif' border=0/></a>"));
                		//UCSys1.append(AddTDGroupTitle("<a href='Group.jsp?EnsName=" + this.getEnsName() + "&OrderBy=" + attr.getKey() + "&OrderWay=Down' >" + attr.getDesc() + "<img src='../Img/ArrUp.gif' border=0/></a>"));

                	}
                }else{
                    UCSys1.append(AddTDGroupTitle("<a href='" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + this.getDoType() + "&RptNo=" + getRptNo() + "&OrderBy=" + attr.getKey() + "&OrderWay=Down' >" + attr.getDesc() + "</a>"));
                	//UCSys1.append(AddTDGroupTitle("<a href='Group.jsp?EnsName=" + this.getEnsName() + "&OrderBy=" + attr.getKey() + "&OrderWay=Down' >" + attr.getDesc() + "</a>"));

                }
            }

            // 分组数据
            for(Attr attr : attrsOfNum){
                String lab = "";
                String key = attr.getKey();
                if (StateNumKey.contains(key + "=SUM")){
                    lab = "(合计)" + attr.getDesc();
                }else{
                    lab = "(平均)" + attr.getDesc();
                }

                if (key.equals(getOrderBy())){
                	if("Down".equals(getOrderWay())){
                		if (key.equals(getNumKey()))
                			UCSys1.append(AddTDGroupTitle(lab + "<a href='" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + getDoType() + "&RptNo=" + getRptNo() + "&NumKey=" + key + "&OrderBy=" + key + "&OrderWay=Up'><img src='"+basePath+"WF/Img/ArrDown.gif' border=0/></a>"));
                		else
                			UCSys1.append(AddTDGroupTitle("<a href=\"" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + getDoType() + "&RptNo=" + getRptNo() + "&NumKey=" + key + "\" >" + lab + "</a><a href='" + getPageID() + ".jsp?RptNo=" + getRptNo() + "&NumKey=" + key + "&OrderBy=" + key + "&OrderWay=Up&FK_Flow=" + getFkFlow() + "'><img src='"+basePath+"WF/Img/ArrDown.gif' border=0/></a>"));
                	}else{
                		if (key.equals(getNumKey()))
                			UCSys1.append(AddTDGroupTitle(lab + "<a href='" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + getDoType() + "&RptNo=" + getRptNo() + "&OrderBy=" + key + "&NumKey=" + key + "&OrderWay=Down'><img src='"+basePath+"WF/Img/ArrUp.gif' border=0/></a>"));
                		else
                			UCSys1.append(AddTDGroupTitle("<a href=\"" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + getDoType() + "&RptNo=" + getRptNo() + "&NumKey=" + key + "\" >" + lab + "</a><a href='" + getPageID() + ".jsp?RptNo=" + getRptNo() + "&OrderBy=" + key + "&NumKey=" + key + "&OrderWay=Down&FK_Flow=" + getFkFlow() + "'><img src='"+basePath+"WF/Img/ArrUp.gif' border=0/></a>"));
                	}
                }else{
                    if (key.equals(getNumKey()))
                        UCSys1.append(AddTDGroupTitle(lab + "<a href='" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + getDoType() + "&RptNo=" + getRptNo() + "&NumKey=" + key + "&OrderBy=" + key + "' ><img src='"+basePath+"WF/Img/ArrDownUp.gif' border=0/></a>"));
                    else
                        UCSys1.append(AddTDGroupTitle("<a href=\"" + getPageID() + ".jsp?FK_Flow=" + getFkFlow() + "&DoType=" + getDoType() + "&RptNo=" + getRptNo() + "&NumKey=" + key + "\" >" + lab + "</a><a href='" + getPageID() + ".jsp?RptNo=" + getRptNo() + "&NumKey=" + key + "&OrderBy=" + key + "&FK_Flow=" + getFkFlow() + "' ><img src='"+basePath+"WF/Img/ArrDownUp.gif' border=0/></a>"));
                }
            }
            
            UCSys1.append(AddTDGroupTitle(""));
            UCSys1.append(AddTREnd());
            // 
        }
        //#endregion 生成表头

        //#region 生成要查询条件
        StringBuilder YSurl = new StringBuilder();
        YSurl.append("GroupDtl.jsp?RptNo=").append(getRptNo()).append("&FK_Flow=").append(getFkFlow());
        
        TextBox TB_S_From = this.toolBar.GetTBByID("TB_S_From");
        TextBox DTTo = this.toolBar.GetTBByID("DTTo");
        TextBox TB_Key = this.toolBar.GetTBByID("TB_Key");
        
        if ( TB_S_From != null){
        	String tb_s_from = getParameter("TB_S_From");
        	String dtto = getParameter("DTTo");
        	if(getIsPostBack()){
	        	if(!StringHelper.isNullOrEmpty(tb_s_from)){
	        		TB_S_From.setText(tb_s_from);
	        	}
	        	if(!StringHelper.isNullOrEmpty(dtto)){
	        		DTTo.setText(dtto);
	        	}
        	}
        	
    		if(!StringHelper.isNullOrEmpty(TB_S_From.getText())){
    			YSurl.append("&DTFrom=").append(TB_S_From.getText());
    		}
    		if(!StringHelper.isNullOrEmpty(DTTo.getText())){
    			YSurl.append("&DTTo=").append(DTTo.getText());
    		}
        }
        
        if (TB_Key != null){
        	String tb_key_value = getParameter("TB_Key");
        	if(getIsPostBack()){
        		if(!StringHelper.isNullOrEmpty(tb_key_value)){
        			TB_Key.setText(tb_key_value);
	        	}	
        	}
        	
        	if(!StringHelper.isNullOrEmpty(TB_Key.getText())){
    			YSurl.append("&TB_Key=").append(TB_Key.getText());
    		}
        }

        // 分组的信息中是否包含部门？
        boolean IsHaveFK_Dept = false;
        for(Attr attr : AttrsOfGroup){
            if ("FK_Dept".equals(attr.getKey())){
                IsHaveFK_Dept = true;
                break;
            }
        }
        
        StringBuilder keys = new StringBuilder();
        
        for(AttrSearch a23 : enMap.getSearchAttrs()){
            Attr attrS = a23.HisAttr;
            if (attrS.getMyFieldType() == FieldType.RefText)
                continue;

            if (IsHaveFK_Dept && "FK_Dept".equals(attrS.getKey()))
                continue;

            DDL ddl = this.toolBar.GetDDLByKey("DDL_" + attrS.getKey());
            if (ddl == null){
                try {
					throw new Exception(attrS.getKey());
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
            
            if(getIsPostBack()){
            	String ddl_val = getParameter(ddl.getId());
            	ddl.SetSelectItem(ddl_val);
            }
            
            String val = ddl.getSelectedItemStringVal();
            if (val.equals("all"))
                continue;
            keys.append("&").append(attrS.getKey()).append("=").append(val);
        }
        YSurl.append(keys.toString());
        //#endregion

        //#region 扩充table 的外键，并且把外键或者枚举的中文名放入里面。
        // 为表扩充外键
        for(Attr attr : AttrsOfGroup){
            dt.Columns.Add(attr.getKey() + "T", String.class);
        }
        for(Attr attr : AttrsOfGroup){
            if (attr.getIsEnum()){
                /* 说明它是枚举类型 */
                SysEnums ses = new SysEnums(attr.getUIBindKey());
                for(DataRow dr : dt.Rows){
                    int val = 0;
                    try{
                        val = Integer.parseInt(dr.getValue(attr.getKey()).toString());
                    }catch(Exception e){
                        dr.setValue(attr.getKey() + "T", " ");
                        continue;
                    }

                    for(Object obj : ses.subList(0, ses.size())){
                    	SysEnum se = (SysEnum) obj;
                        if (se.getIntKey() == val)
                            dr.setValue(attr.getKey() + "T", se.getLab());
                    }
                }
                continue;
            }
            for(DataRow dr : dt.Rows){
                String val = dr.getValue(attr.getKey()) == null ? "": dr.getValue(attr.getKey()).toString();
                if (!attr.getUIBindKey().contains(".")){
                    try{
                        dr.setValue(attr.getKey() + "T", DBAccess.RunSQLReturnStringIsNull("SELECT Name FROM " + attr.getUIBindKey() + " WHERE No='" + val + "'", val));
                    }catch(Exception e){
                        dr.setValue(attr.getKey() + "T", val);
                    }
                    continue;
                }

                Entity myen = attr.getHisFKEn();
                myen.SetValByKey(attr.getUIRefKeyValue(), val);
                try{
                    myen.Retrieve();
                    dr.setValue(attr.getKey() + "T", myen.GetValStrByKey(attr.getUIRefKeyText()));
                }catch(Exception e){
                    if (val.equals("") || val.length() <= 1){
                        dr.setValue(attr.getKey() + "T", "");
                    }else{
                    	dr.setValue(attr.getKey() + "T", val);
                    }
                }
            }
        }
        //#endregion 扩充table 的外键，并且把外键或者枚举的中文名放入里面。

        //#region 生成datagrade表体
        int i = 0;
        boolean is1 = false;
        for(DataRow dr : dt.Rows){
            i++;
            url = YSurl.toString();
            String keyActive = "";
            // 产生url .
            for(Attr attr : AttrsOfGroup)
            	url += "&" + attr.getKey() + "=" +String.valueOf( dr.getValue(attr.getKey()));
            UCSys1.append(AddTR(is1));
            UCSys1.append(AddTDIdx(Integer.parseInt(String.valueOf(dr.getValue("IDX")))));
            // 分组条件
            for(Attr attr : AttrsOfGroup){
            	UCSys1.append(AddTD(String.valueOf(dr.getValue(attr.getKey() + "T"))));
            }

            // 分组数据
            for(Attr attr : attrsOfNum){
                BigDecimal obj = new BigDecimal(0);
                try{
                    //obj = BigDecimal.valueOf(Double.parseDouble(dr.getValue(attr.getKey()).toString()));
                	obj = BigDecimal.valueOf(Double.parseDouble(String.valueOf(dr.getValue(attr.getKey()))));
                }catch(Exception e){
                	System.err.println("GroupModel.java 905L");
                    // throw new Exception(dr[attr.Key].ToString() +"@SQL="+ sql +"@"+ex.Message +"@Attr="+attr.Key );
                }

                switch (attr.getMyDataType())
                {
                    case DataType.AppMoney:
                    case DataType.AppRate:
                        if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") != -1){ /*  如果本数据列 包含累计 */
                            UCSys1.append(AddTDJE(obj));
                            //UCSys1.append(AddTDJE(BigDecimal.valueOf(Double.parseDouble(dr.getValue(attr.getKey() + "Amount").toString()))));
                            UCSys1.append(AddTDJE(BigDecimal.valueOf(Double.parseDouble(String.valueOf(dr.getValue(attr.getKey() + "Amount"))))));
                        }else{
                            UCSys1.append(AddTDJE(obj));
                        }
                        break;
                    default:
                        if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") != -1){ /*  如果本数据列 包含累计 */
                            UCSys1.append(AddTDNum(obj));
                            UCSys1.append(AddTDNum(BigDecimal.valueOf(Double.parseDouble(String.valueOf(dr.getValue(attr.getKey() + "Amount"))))));
                        }else{
                            UCSys1.append(AddTDNum(obj));
                        }
                        break;
                }
            }
            UCSys1.append(AddTD("<a href=\"javascript:WinOpen('" + url + "', 'wincommgroup',900,900);\"  class='easyui-linkbutton'>详细</a>"));
            //UCSys1.append(AddTD("<a href=\"javascript:OpenEasyUiDialog('" + url + "','eudlgframe','详细信息',700,432,'icon-table',false,null,null,document.getElementById('mainDiv'))\" class='easyui-linkbutton'>详细</a>"));
            UCSys1.append(AddTREnd());
        }

        //#region  加入合计信息。
        UCSys1.append(AddTR("class='TRSum'"));
        UCSys1.append(AddTD("汇总"));
        for(Attr attr : AttrsOfGroup){
            UCSys1.append(AddTD());
        }

        //不显示合计列。
        String NoShowSum = SystemConfig.GetConfigXmlEns("NoShowSum", getRptNo());
        if (NoShowSum == null)
            NoShowSum = "";

        Attrs attrsOfNum1 = attrsOfNum.clone();
        BigDecimal d = new BigDecimal(0);
        for(Attr attr : attrsOfNum){
            if (NoShowSum.contains("@" + attr.getKey() + "@")){
                boolean isHave = false;
                for(XmlEn obj : aas.subList(0, aas.size())){
                	ActiveAttr aa = (ActiveAttr) obj;
                    if (!attr.getKey().equals(aa.getAttrKey()))
                        continue;

                    isHave = true;
                    /* 如果它是一个计算列 */
                    String exp = aa.getExpApp();
                    if (exp == null || "".equals(exp)){
                        UCSys1.append(AddTD());
                        break;
                    }
                    for(Attr myattr : attrsOfNum1){
                        if (exp.indexOf("@" + myattr.getKey() + "@") != -1){
                            d = BigDecimal.valueOf(0);
                            for(DataRow dr1 : dt.Rows){
                                try{
                                	d = d.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(dr1.getValue(myattr.getKey())))));
                                }catch(Exception e){
                                	e.printStackTrace();
                                }
                            }

                            exp = exp.replace("@" + myattr.getKey() + "@", String.valueOf(d));
                        }
                    }
                    UCSys1.append(AddTDNum(DataType.ParseExpToDecimal(exp)));
                }

                if (isHave == false)
                    UCSys1.append(AddTD());
                continue;
            }

            switch (attr.getMyDataType())
            {
                case DataType.AppMoney:
                case DataType.AppRate:
                    if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") != -1){ /*  如果本数据列 包含累计 */
                        d = BigDecimal.valueOf(0);
                        for(DataRow dr1 : dt.Rows)
                        	d = d.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(dr1.getValue(attr.getKey())))));
                        UCSys1.append(AddTDJE(d));

                        d = BigDecimal.valueOf(0);
                        for(DataRow dr1 : dt.Rows)
                        	d = d.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(dr1.getValue(attr.getKey() + "Amount")))));
                        UCSys1.append(AddTDJE(d));
                    }else{
                        d = BigDecimal.valueOf(0);
                        for(DataRow dr1 : dt.Rows){
                            try{
                            	if(String.valueOf(dr1.getValue(attr.getKey()))==null||String.valueOf(dr1.getValue(attr.getKey())).equals("")){
                            	d = d.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(dr1.getValue(attr.getKey())))));
                            	}else{
                            		d=d.add(BigDecimal.valueOf(0));
                            	}
                            	}catch(Exception e){e.printStackTrace();}
                        }

                        if (StateNumKey.indexOf(attr.getKey() + "=AVG") < 1){
                            UCSys1.append(AddTDJE(d));
                        }else{
                            if (dt.Rows.size() == 0)
                                UCSys1.append(AddTD());
                            else
                                UCSys1.append(AddTDJE(d.divide(new BigDecimal(dt.Rows.size()), 2, RoundingMode.HALF_UP)));
                        }
                    }
                    break;
                default:
                    if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") != -1){ /*  如果本数据列 包含累计 */
                        d = BigDecimal.valueOf(0);
                        for(DataRow dr1 : dt.Rows)
                        	d = d.add(new BigDecimal(dr1.getValue(attr.getKey()).toString()));
                        UCSys1.append(AddTDNum(d));

                        d = BigDecimal.valueOf(0);
                        for(DataRow dr1 : dt.Rows)
                        	d = d.add(new BigDecimal(dr1.getValue(attr.getKey() + "Amount").toString()));
                        UCSys1.append(AddTDNum(d));
                    }else{
                        d = BigDecimal.valueOf(0);
                        for(DataRow dr1 : dt.Rows){
                            try{
                            	d = d.add(new BigDecimal(dr1.getValue(attr.getKey()).toString()));
                            }catch(Exception e){e.printStackTrace();}
                        }

                        if (StateNumKey.indexOf(attr.getKey() + "=AVG") < 1){
                            UCSys1.append(AddTDNum(d));
                        }else{
                            if (dt.Rows.size() == 0)
                                UCSys1.append(AddTD());
                            else
                            	UCSys1.append(AddTDJE(d.divide(new BigDecimal(dt.Rows.size()), 2, RoundingMode.HALF_UP)));
                        }
                    }
                    break;
            }
        }
        UCSys1.append(AddTD());
        UCSys1.append(AddTREnd());
        //#endregion

        UCSys1.append(AddTableEnd());
        //#endregion 生成表体

        //#region 生成图形
        if (CB_IsShowPict.checked)
        {
            //使用tab页显示图表，填充tab框架
            UCSys3.append("<div class='easyui-tabs' data-options=\"fit:true\">");
            UCSys3.append("    <div title='分组数据' data-options=\"iconCls:'icon-table'\" style='padding:5px'>");
            UCSys4.append("    </div>");

            /* 如果是 1 纬 */
            String colOfGroupField = "";
            String colOfGroupName = "";
            String colOfNumField = "";
            String colOfNumName = "";
            String title = "";
            int chartHeight = Integer.parseInt(TB_H.getText());
            int chartWidth = Integer.parseInt(TB_W.getText());

            if (isHaveLJ){
                /*  如果有累计, 就按照累计字段分析。*/
                colOfGroupField = AttrsOfGroup.get(0).getKey();
                colOfGroupName = AttrsOfGroup.get(0).getDesc();

                colOfNumName = attrsOfNum.get(0).getDesc();
                if (dt.Columns.contains(attrsOfNum.get(0).getKey() + "AMOUNT"))
                    colOfNumField = attrsOfNum.get(0).getKey() + "AMOUNT";
                else
                    colOfNumField = attrsOfNum.get(0).getKey();
            }else{
                colOfGroupField = AttrsOfGroup.get(0).getKey();
                colOfGroupName = AttrsOfGroup.get(0).getDesc();

                if (StringHelper.isNullOrEmpty(getNumKey())){
                    colOfNumName = attrsOfNum.get(0).getDesc();
                    colOfNumField = attrsOfNum.get(0).getKey();
                }else{
                    //  colOfNumField = attrsOfNum[0].Key;
                    colOfNumName = attrs.GetAttrByKey(getNumKey()).getDesc(); // this.UCSys1.get;
                    colOfNumField = getNumKey();
                }
            }

            String colOfNumName1 = "";
            if (StateNumKey.contains(getNumKey() + "=SUM"))
                colOfNumName1 = "(合计)" + colOfNumName;
            else
                colOfNumName1 = "(平均)" + colOfNumName;

            if (!dt.Columns.contains(colOfNumField)){
                for(Attr item : attrsOfNum){
                    if (dt.Columns.contains(item.getKey())){
                        colOfNumField = item.getKey();
                        break;
                    }
                }
            }
            try{

                Map<String, String> yfields = new HashMap<String, String>();

                for(Attr attr : attrsOfNum){
                    yfields.put(attr.getKey(), attr.getDesc());
                }

                //增加柱状图
                UCSys4.append("    <div id='column_chart_div'  title='柱状图' data-options=\"iconCls:'icon-columnchart'\" style='padding:5px;text-align:center'  >");
               	UCSys4.append(GenerateColumnChart(dt, AttrsOfGroup.get(0).getKey() + "T", AttrsOfGroup.get(0).getDesc(), yfields, "分组分析" , chartWidth, chartHeight));
                UCSys4.append("    </div>");

                //增加饼图
                UCSys4.append("    <div id='pie_chart_div' title='饼状图' data-options=\"iconCls:'icon-piechart'\" style='padding:5px;text-align:center'>");
                UCSys4.append(GeneratePieChart(dt, AttrsOfGroup.get(0).getKey() + "T", AttrsOfGroup.get(0).getDesc(), yfields, "分组分析 ", chartWidth, chartHeight));
                UCSys4.append("    </div>");

                //增加拆线图
                UCSys4.append("    <div id='line_chart_div' title='折线图' data-options=\"iconCls:'icon-linechart'\" style='padding:5px;text-align:center'>");
                UCSys4.append(GenerateLineChart(dt, AttrsOfGroup.get(0).getKey() + "T", AttrsOfGroup.get(0).getDesc(), yfields, "分组分析 ", chartWidth, chartHeight));
                UCSys4.append("    </div>");
                UCSys4.append("</div>");


            }catch (Exception ex){
            	ex.printStackTrace();
                System.err.println("@产生图片文件出现错误:" + ex.getMessage());
            }
        }
        //#endregion

        //#region 保存操作状态
        currUR.setVals(StatesqlOfGroupKey + StateNumKey);
        currUR.setCfgKey(getRptNo() + "_Group");
        currUR.setFK_Emp(WebUser.getNo());
        currUR.setOrderBy(getOrderBy());
        currUR.setOrderWay(getOrderWay());
        currUR.setIsPic(CB_IsShowPict.checked);
        currUR.setGenerSQL(myps.SQL);
        currUR.setNumKey(getNumKey());
        String paras = "";
        for(Para para : myps){
        	paras += "@" + para.ParaName + "=" + para.val;
        }
        currUR.setParas(paras);
        currUR.Save();

        this.CfgVal = currUR.getVals();
        //#endregion
		return dt1;
	}
	
	 public DataTable DealTable(DataTable dt)
     {
         DataTable dtCopy = new DataTable();

        // #region 把他们转换为 string 类型。
         for(DataColumn dc:dt.Columns)
             dtCopy.Columns.Add(dc.ColumnName);

         for(DataRow dr:dt.Rows)
             dtCopy.Rows.add(dr);
       

         Entity en = this.HisEn;
         Map map = (Map) en.getEnMap();
         Attrs attrs = en.getEnMap().getAttrs();
         for(DataColumn dc:dt.Columns)
         {
             boolean isLJ = false;
             Attr attr = null;
             try
             {
                 attr = ((Attrs) map).GetAttrByKey(dc.ColumnName);
                 isLJ = false;
             }
             catch(Exception e)
             {
                 try
                 {
                     attr = ((Attrs) map).GetAttrByKey(dc.ColumnName + "AMOUNT");
                     isLJ = true;
                 }
                 catch(Exception e1)
                 {
                 }
             }

             if (attr == null)
                 continue;

             if (attr.getUIBindKey() == null || attr.getUIBindKey().equals(""))
             {
                 if (isLJ)
                     dtCopy.Columns.get(attr.getKey().toUpperCase() + "AMOUNT").ColumnName = "累计";
                 else
                     dtCopy.Columns.get(attr.getKey().toUpperCase()).ColumnName = attr.getDesc();
                 continue;
             }
             if (attr.getUIRefKeyValue().indexOf(".") != -1){
            	 Entity en1 = attr.getHisFKEn();
                 String pk = en1.getPK();
                 for(DataRow dr:dtCopy.Rows){
                	 if (dr.getValue(attr.getKey()) == null)
                     continue;

                 String val = (String)dr.getValue(attr.getKey());
                 if (val == null || val.equals(""))
                     continue;

                 en1.SetValByKey(pk, dr.getValue(attr.getKey()));
                 int i = en1.RetrieveFromDBSources();
                 if (i == 0)
                     continue;

                 dr.setValue(attr.getKey(),  en1.GetValStrByKey(attr.getUIRefKeyValue()) + en1.GetValStrByKey(attr.getUIRefKeyText()));
                 }
             }else if(attr.getUIRefKeyValue().length() >= 2){
            	 for(DataRow mydr:dtCopy.Rows)
                 {
                     if (mydr.getValue(attr.getKey()) == null)
                         continue;

                     int intVal = Integer.parseInt(mydr.getValue(attr.getKey()).toString());
                     SysEnum se = new SysEnum(attr.getUIBindKey(), intVal);
                     mydr.setValue(attr.getKey(), se.getLab());
                 }
             }
             dtCopy.Columns.get(attr.getKey().toUpperCase()).ColumnName = attr.getDesc();
             
             }
         try
         {
             dtCopy.Columns.get("MYNUM").ColumnName = "个数";
         }
         catch(Exception e)
         {
         }
         return dtCopy;    
     }
	 private void InitializeComponent()
     {
     }
	
	public String getUCSys(){
		StringBuffer ucSys = new StringBuffer();
		ucSys.append(UCSys3.toString());
		ucSys.append(UCSys1.toString());
		ucSys.append(UCSys4.toString());
		return ucSys.toString();
	}
	
	private void DealChoseNone(){
		//#region 检查数值列表是否被选择?
		Map<String, Object> temp = UCSys2.getTmpMap();
        boolean isCheck = false;
        for(String id : temp.keySet()){
            if (id == null)
                continue;

            if (id.indexOf("CB_") == -1)
                continue;

            CheckBox cb = (CheckBox) UCSys2.GetUIByID(id);
            if (!cb.getChecked())
                continue;
            isCheck = true;
            break;
        }

        if (!isCheck){
        	for(String id : temp.keySet()){
                if (id == null)
                    continue;

                if (id.indexOf("CB_") == -1)
                    continue;

                CheckBox cb = (CheckBox) UCSys2.GetUIByID(id);
                cb.setChecked(true);
                String tempSys2 = HtmlUtils.setCtrlHtml(cb, UCSys2.ListToString());
                UCSys2.getTmpList().clear();
                UCSys2.append(tempSys2);
                break;
            }
        }
        //#endregion 检查数值列表是否被选择?
        
        //#region 检查分组列表是否被选择?
        isCheck = false;
        for(ListItem li : checkBoxList1.Items){
            if (li.getSelected()){
            	 isCheck = true;
            	 break;
            }
               
        }
        if (!isCheck){
            for(ListItem li : this.checkBoxList1.Items){
                li.setSelected(true);
                break;
            }
        }
        //#endregion 检查分组列表是否被选择?
	}
	
	public boolean getIsPostBack() {
		IsPostBack = getParameter("isPostBack");
		if(StringHelper.isNullOrEmpty(IsPostBack) || IsPostBack.equals("false")){
			return false;
		}else{
			return true;
		}
	}

	public String getRptNo() {
		if(rptNo == null){
			rptNo = getParameter("RptNo");
			if(StringHelper.isNullOrEmpty(rptNo)){
				rptNo = "ND" + Integer.parseInt(getFkFlow()) + "MyRpt";
			}
		}
		return rptNo;
	}
	
	public String getFkFlow() {
		if(StringHelper.isNullOrEmpty(fkFlow)){
			fkFlow = getParameter("FK_Flow");
			if(StringHelper.isNullOrEmpty(fkFlow)){
				fkFlow = "1";
			}
			fkFlow = fkFlow.replace("ND", "");
			fkFlow = fkFlow.replace("Rpt", "");
		}
		return fkFlow;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getCB_IsShowPict(){
		return CB_IsShowPict.toString();
	}
	
	public String getTB_H(){
		return TB_H.toString();
	}
	
	public String getTB_W(){
		return TB_W.toString();
	}
	
	public String getLbtnApply(){
		return lbtnApply.toString();
	}
	
	public Entities _HisEns = null;
	public final Entities getHisEns() {
		if (_HisEns == null) {
			if (this.getRptNo() != null) {
				if (this._HisEns == null) {
					_HisEns = BP.En.ClassFactory.GetEns(this.getRptNo());
				}
			}
		}
		return _HisEns;
	}
}
