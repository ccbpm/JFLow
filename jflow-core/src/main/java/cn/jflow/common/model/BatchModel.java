package cn.jflow.common.model;

import java.text.DecimalFormat;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.EntitiesNoName;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Tools.StringHelper;
import BP.WF.BatchRole;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.RunModel;
import BP.WF.Work;
import BP.WF.Works;
import BP.WF.Data.GERpt;
import BP.WF.Template.BtnLab;
import BP.Web.WebUser;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;


public class BatchModel {

	private String basePath;
	private int FK_Node;
	private String FK_MapData;
	private int ListNum;
	private String Key;
	private String DoType;
	private String normMsg;
	
	public BatchModel(String basePath, int FK_Node, String FK_MapData, int ListNum, String Key, String DoType, String normMsg){
		this.basePath = basePath;
		this.FK_Node = FK_Node;
		this.FK_MapData = FK_MapData;
		this.ListNum = ListNum;
		this.Key = Key;
		this.DoType = DoType;
		this.normMsg = normMsg;
	}
	
	public UiFatory ui = null;
	public void init() {
		this.ui = new UiFatory();
		
		if("Send".equals(DoType) || "Delete".equals(DoType) || "Return".equals(DoType)){
			this.ui.append(BaseModel.AddFieldSet("批量处理信息"));
			if(null != normMsg && !"".equals(normMsg)){
				this.ui.append(normMsg);//.replace("@", "<BR>@"));
			}
			this.ui.append(BaseModel.AddFieldSetEnd());
		}else{
			if(0 == FK_Node){
				 // 如果没有接收到节点ID参数，就绑定当前人员可以执行批量审核的待办工作.
	            //int num = this.BindNodeList();
				this.BindNodeList();
	            return;
			}
			
			Node nd = new Node(this.FK_Node);
			Flow fl = nd.getHisFlow();
			String sql = "";

			
			if (nd.getHisRunModel().getValue() == RunModel.SubThread.getValue()) {
				sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.FID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
			}
			else {
				sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
			}

			
	       /* sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable()
	                      + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID()
	                      + " AND b.FK_Emp='" + WebUser.getNo() + "'";
	        
	        */
	        
	        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
	        if (nd.getHisBatchRole() == BatchRole.None){
	        	this.ui.append(BaseModel.AddFieldSetRed("错误", "节点(" + nd.getName() + ")不能执行批量处理操作."));
	            return;
	        }
	        
	       // String inSQL = "SELECT WorkID FROM WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "' AND WFState!=7 ";
	       
	        String inSQL = "SELECT WorkID FROM WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "' AND WFState!=7 AND FK_Node=" + this.getFK_Node();

	        Works wks = nd.getHisWorks();
	        wks.RetrieveInSQL(inSQL);
	        
	        BtnLab btnLab = new BtnLab(this.FK_Node);
	        this.ui.append(BaseModel.AddTable());
	        
	        //移动按钮位置
	        if (nd.getHisBatchRole() == BatchRole.Group){
	        	//this.ui.append(BaseModel.AddCaption("批处理: " + nd.getFlowName() + " --> " + nd.getName() + "&nbsp;&nbsp;<a href='"+basePath+"WF/Batch.jsp'>返回</a>&nbsp;&nbsp;<input  id=\"btnGroup\" type=\"button\" value=\"合卷批复\" CssClass=\"Btn\" onclick=\"BatchGroup()\" />"));
	        	this.ui.append(BaseModel.AddCaption("批处理: " + nd.getFlowName() + " --> " + nd.getName() + "&nbsp;&nbsp;<a href='"+basePath+"WF/Batch.jsp'>返回</a>"));
	        }else{
	        	this.ui.append(BaseModel.AddCaption("批处理: " + nd.getFlowName() + " --> " + nd.getName() + "&nbsp;&nbsp;<a href='"+basePath+"WF/Batch.jsp'>返回</a>"));
	        }
	        
	        //#region 生成标题.
	        this.ui.append(BaseModel.AddTR());
	        this.ui.append(BaseModel.AddTDTitle("class='table-title'","序"));
	        String str1 = "<input id='checkedAll' onclick='SelectAll()' value='选择' type='checkbox' name='checkedAll'>";
	        this.ui.append(BaseModel.AddTDTitle("align='left'", str1));
	        this.ui.append(BaseModel.AddTDTitle("标题"));
	        this.ui.append(BaseModel.AddTDTitle("发起人"));
	        this.ui.append(BaseModel.AddTDTitle("接受日期"));
	        
	        //显示出来字段. BatchParas的规则为 @字段中文名=fieldName@字段中文名1=fieldName1 
	        MapAttrs attrs = new MapAttrs(this.FK_MapData);
	        String[] strs = nd.getBatchParas().split(",");
	        for(String str : strs){
	        	if(StringHelper.isNullOrEmpty(str) || str.contains("@PFlowNo"))continue;
	        	
	        	 for (MapAttr attr : attrs.ToJavaList()){
	        		 if (!str.equals(attr.getKeyOfEn()))continue;
	        		 this.ui.append(BaseModel.AddTDTitle("class='table-title'",attr.getName()));
	        	 }
	        }
	        this.ui.append(BaseModel.AddTREnd());
	        //#region 生成标题.
	        
	        GERpt rpt = nd.getHisFlow().getHisGERpt();
	        boolean is1 = false;
	        int idx = 0;
	        for(Work wk : wks.ToJavaList()){
	        	  idx++;
	        	if(idx==  nd.getBatchListCount())break;
//	        	  if (idx == this.ListNum+1)break;
	        	  
	        	  //#region 显示必要的列.
	        	  //this.ui.append(BaseModel.AddTR(is1));
	        	  this.ui.append(BaseModel.AddTR());
	              is1 = !is1;
	              this.ui.append(BaseModel.AddTDIdx(idx));
	              CheckBox cb = ui.creatCheckBox("CB_" + wk.getOID());
	              for(DataRow dr : dt.Rows){
	            	  if (!(dr.getValue("WorkID").toString()).equals(String.valueOf(wk.getOID())))continue;
	            	  
	            	  cb.setText("");
	            	  this.ui.append("\n<TD >");
	            	  this.ui.append(cb);
	            	  this.ui.append("</TD>");
	            	  
	            	  //this.ui.append(BaseModel.AddTD("<a href=\"javascript:WinOpen('"+basePath+"WF/FlowFormTree/Default.jsp?WorkID=" + wk.getOID() + "&FK_Node=" + this.FK_Node + "&IsSend=0&FK_Flow=" + nd.getFK_Flow() + "','s')\" >" + dr.getValue("Title") + "</a>"));
	            	  this.ui.append("\n<TD nowrap = 'nowrap'>");
	  	              this.ui.append(dr.getValue("Title"));
	  	              this.ui.append("</TD>");
	            	  this.ui.append(BaseModel.AddTD(dr.getValue("Starter").toString()));
	            	  this.ui.append(BaseModel.AddTD(dr.getValue("ADT").toString()));
	                  break;
	              }
	              //#endregion 显示必要的列.
	              
	              //#region 显示出来自定义的字段数据.
	              for(String str : strs){
	            	  if(StringHelper.isNullOrEmpty(str) || str.contains("@PFlowNo"))continue;
	            	  
	            	  for (MapAttr attr : attrs.ToJavaList()){
	            		  if (!str.equals(attr.getKeyOfEn()))continue;
	            		  
	            		  TextBox tb = ui.creatTextBox("TB_" + attr.getKeyOfEn() + "_" + wk.getOID());
	            		  switch(attr.getLGType()){
	            		  	case Normal:
	            		  		switch(attr.getMyDataType()){
	            		  			case DataType.AppString:
	            		  				if (attr.getUIRows() == 1){
	            		  					 tb.setText(wk.GetValStringByKey(attr.getKeyOfEn()));
	            		  					 //tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 15px;padding: 0px;margin: 0px;");
	            		  					 tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 19px;padding: 0px;margin: 0px;");
	            		  					 if(attr.getUIIsEnable()){
	            		  						tb.setCssClass("TB");
	            		  					 }else{
	            		  						tb.setCssClass("TBReadonly");
	            		  					 }
	            		  					 
	            		  					 this.ui.append("\n<TD >");
	            		  	            	 this.ui.append(tb);
	            		  	            	 this.ui.append("</TD>");
	            		  				}else{
	            		  					tb.setTextMode(TextBoxMode.MultiLine);
	            		  					tb.setText(wk.GetValStringByKey(attr.getKeyOfEn()));
	            		  					tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left;padding: 0px;margin: 0px;");
	            		  					tb.addAttr("maxlength", String.valueOf(attr.getMaxLen()));
	            		  					tb.setRows(attr.getUIRows());
	            		  					if(attr.getUIIsEnable()){
	            		  						tb.setCssClass("TBDoc");
	            		  					}else{
	            		  						tb.setCssClass("TBReadonly");
	            		  					}
	            		  					
	            		  					this.ui.append("\n<TD >");
	           		  	            	  	this.ui.append(tb);
	           		  	            	  	this.ui.append("</TD>");
	            		  				}
	            		  				break;
	            		  			case DataType.AppDate:
	            		  				tb.setShowType(TBType.Date);
	            		  				tb.setText(wk.GetValStrByKey(attr.getKeyOfEn()));
	            		  				if (attr.getUIIsEnable()){
	            		  					tb.addAttr("onfocus", "WdatePicker();");
	        								tb.addAttr("class", "TB");
	            		  				}else{
	            		  					tb.addAttr("class", "TBReadonly");
	            		  				}
	            		  				tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 19px;");
	            		  				
	            		  				this.ui.append("\n<TD >");
	       		  	            	    this.ui.append(tb);
	       		  	            	  	this.ui.append("</TD>");
	            		  				break;
	            		  			case DataType.AppDateTime:
	            		  				tb.setShowType(TBType.DateTime);
	        							tb.setText(wk.GetValStrByKey(attr.getKeyOfEn()));
	        							if (attr.getUIIsEnable()){
	        								tb.addAttr("class", "TBcalendar");
	        								tb.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
	            		  				}else{
	            		  					tb.addAttr("class", "TBReadonly");
	            		  				}
	        							tb.addAttr("style", "width: " + attr.getUIWidth() + "px; text-align: left; height: 19px;");
	        							
	        							this.ui.append("\n<TD >");
	       		  	            	    this.ui.append(tb);
	       		  	            	  	this.ui.append("</TD>");
	            		  				break;
	            		  			case DataType.AppBoolean:
	            		  				 cb = ui.creatCheckBox("CB_" + attr.getKeyOfEn() + "_" + wk.getOID());
	            		  				 cb.setText(attr.getName());
	            		  				 //cb.setChecked(attr.getDefValOfBool());
	        							 cb.setEnabled(attr.getUIIsEnable());
	        							 cb.setChecked(wk.GetValBooleanByKey(attr.getKeyOfEn()));
	        							 
//	        							 if(!cb.getEnabled()){
//	        								 cb.setEnabled(false);
//	        							 }else{
//	        								 cb.setEnabled(true); 
//	        							 }
	        							 
	        							 this.ui.append("\n<TD nowrap = 'nowrap'>");
	        		  	            	 this.ui.append(cb);
	        		  	            	 this.ui.append("</TD>");
	            		  				break;
	            		  			case DataType.AppDouble:
	            		  			case DataType.AppFloat:
	            		  				tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;word-break: keep-all;");
	        							tb.setText(attr.getDefVal());
	        							if (attr.getUIIsEnable()){
	        								//增加验证
	        								tb.addAttr("onkeyup", "Change('" + attr.getFK_MapData() + "');");
	        								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,0);");
	        								tb.addAttr("onClick", "TB_ClickNum(this)");
	        								tb.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'float');");
	        								tb.addAttr("class", "TBNum");
	        							}else{
	        								tb.addAttr("class", "TBReadonly");
	        							}
	        							
	        							this.ui.append("\n<TD >");
	        		  	            	this.ui.append(tb);
	        		  	            	this.ui.append("</TD>");
	            		  				break;
	            		  			case DataType.AppInt:
	            		  				tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;word-break: keep-all;");
	        							tb.setText(attr.getDefVal());
	        							if (attr.getUIIsEnable()){
	        								//增加验证
	        								tb.addAttr("onkeyup", "Change('" + attr.getFK_MapData() + "');");
	        								tb.addAttr("onblur", "value=value.replace(/[^-?\\d]/g,'');TB_ClickNum(this,0);");
	        								tb.addAttr("onClick", "TB_ClickNum(this)");
	        								tb.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'int');");
	        								tb.addAttr("class", "TBNum");
	        							}else{
	        								tb.addAttr("class", "TBReadonly");
	        							}
	        							
	        							this.ui.append("\n<TD >");
	        		  	            	this.ui.append(tb);
	        		  	            	this.ui.append("</TD>");
	            		  				break;
	            		  			case DataType.AppMoney:
	            		  				if (attr.getUIIsEnable()){
	        								//增加验证
	        								tb.addAttr("onkeyup", "Change('" + attr.getFK_MapData() + "');");
	        								tb.addAttr("onblur", "value=value.replace(/[^-?\\d+\\.*\\d*$]/g,'');TB_ClickNum(this,'0.00');");
	        								tb.addAttr("onClick", "TB_ClickNum(this)");
	        								tb.addAttr("OnKeyPress", "javascript:return  VirtyNum(this,'float');");
	        								tb.addAttr("class", "TBNum");
	        							}else{
	        								tb.addAttr("class", "TBReadonly");
	        							}
	            		  				tb.setText(decimalFormat(wk.GetValMoneyByKey(attr.getKeyOfEn())));
	            		  				tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;");
	            		  				
	            		  				this.ui.append("\n<TD >");
	        		  	            	this.ui.append(tb);
	        		  	            	this.ui.append("</TD>");
	            		  				break;
	            		  			case DataType.AppRate:
	            		  				if (attr.getUIIsEnable()){
	            		  					tb.addAttr("class", "TBNum");
	            		  				}else{
	            		  					tb.addAttr("class", "TBReadonly");
	            		  				}
	            		  				tb.setShowType(TBType.Moneny);
	            		  				tb.setText(decimalFormat(wk.GetValMoneyByKey(attr.getKeyOfEn())));
	            		  				tb.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px; text-align: right; height: 19px;");
	            		  				
	            		  				this.ui.append("\n<TD >");
	        		  	            	this.ui.append(tb);
	        		  	            	this.ui.append("</TD>");
	            		  				break;
	            		  			default:
	                                    break;
	            		  		}
	            		  		break;
	            		  	case Enum:
	            		  		 if (attr.getUIContralType() == UIContralType.DDL){
	            		  			DDL ddle = ui.creatDDL("DDL_" + attr.getKeyOfEn() + "_" + wk.getOID());
	            		  			ddle.BindSysEnum(attr.getUIBindKey());
	            		  			ddle.SetSelectItem(wk.GetValIntByKey(attr.getKeyOfEn()));
	            		  			ddle.setEnabled(attr.getUIIsEnable());
	        						ddle.addAttr("tabindex", String.valueOf(attr.getIdx()));
	        						if (attr.getUIIsEnable()){
	        							ddle.addAttr("onchange", "Change('" + attr.getFK_MapData() + "')");
	        						}
	        						
	        						this.ui.append("\n<TD >");
	    		  	            	this.ui.append(ddle);
	    		  	            	this.ui.append("</TD>");
	            		  		 }else{}
	            		  		break;
	            		  	case FK:
	            		  		DDL ddl1 = ui.creatDDL("DDL_" + attr.getKeyOfEn() + "_" + wk.getOID());
	            		  		ddl1.addAttr("tabindex", String.valueOf(attr.getIdx()));
	            		  		if (ddl1.getEnabled()){
	            		  			 EntitiesNoName ens = attr.getHisEntitiesNoName();
	            		  			 ens.RetrieveAll();
	            		  			 ddl1.BindEntities(ens);
	            		  			 ddl1.SetSelectItem(wk.GetValStrByKey(attr.getKeyOfEn()));
	            		  		}else{
	            		  			ddl1.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px;height: 19px;");
	            		  			if (ddl1.getEnabled())ddl1.setEnabled(false);
	            		  			ddl1.addAttr("Width", String.valueOf(attr.getUIWidth()));
	            		  			ddl1.Items.add(new ListItem(wk.GetValRefTextByKey(attr.getKeyOfEn()), wk.GetValStrByKey(attr.getKeyOfEn())));
	            		  		}
	            		  		ddl1.setEnabled(attr.getUIIsEnable());
	            		  		
	    						this.ui.append("\n<TD >");
			  	            	this.ui.append(ddl1);
			  	            	this.ui.append("</TD>");
	            		  		break;
	            		  	default:
	                           break;
	            		  }
	            	  }
	              }
	              //#endregion 显示出来自定义的字段数据.
	              this.ui.append(BaseModel.AddTREnd());
	        }
	        this.ui.append(BaseModel.AddTableEndWithHR());
	        
	        MapExts mes = new MapExts(this.FK_MapData);
	        
	        //#region 处理扩展属性.
	        if (mes.size() != 0){
	        	 this.ui.append("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");
	        	 for (Work wk : wks.ToJavaList()){ 	        		  for(MapExt me : mes.ToJavaList()){
	        			  if(MapExtXmlList.DDLFullCtrl.equals(me.getExtType())){// 自动填充.
	        				  Object oper = ui.GetUIByID("DDL_" + me.getAttrOfOper() + "_" + wk.getOID());
	                          if (null == oper) continue;
	                          DDL ddlOper = (DDL) oper;
	                          ddlOper.addAttr("onchange", "DDLFullCtrl(this.value,\'" + ddlOper.getId()+ "\', \'" + me.getMyPK() + "\')");
	        			  }else if(MapExtXmlList.ActiveDDL.equals(me.getExtType())){
	        				  Object parent = ui.GetUIByID("DDL_" + me.getAttrOfOper() + "_" + wk.getOID());
	        				  if (null == parent || wk.getOID() < 100)continue;
	        				  DDL ddlPerant = (DDL) parent;
	        				  //#warning 此处需要优化
	        				  String ddlC = "DDL_" + me.getAttrsOfActive() + "_" + wk.getOID();
	        				  ddlPerant.addAttr("onchange", "DDLAnsc(this.value,\'" + ddlC+ "\', \'" + me.getMyPK() + "\')");
	        				  
	        				  String valC;
	        				  Object child = ui.GetUIByID("DDL_" + me.getAttrsOfActive() + "_" + wk.getOID());
	        				  if (null == child)continue;
	        				  DDL ddlChild = (DDL) child;
	        				  String val = ddlPerant.getSelectedItemStringVal();
	        				  if(ddlChild.Items.size() == 0){
	        					  valC = wk.GetValStrByKey(me.getAttrsOfActive());
	        				  }else{
	        					  valC = ddlChild.getSelectedItemStringVal();
	        				  }
	        				  
	        				  String mysql = me.getDoc().replace("@Key", val);
	        				  if (mysql.contains("@")){
	                              mysql = Glo.DealExp(mysql, wk, null);
	                          }
	        				  ddlChild.Bind(DBAccess.RunSQLReturnTable(mysql), "No", "Name");
	        				  if (!ddlChild.SetSelectItem(valC)){
	                              ddlChild.Items.add(0, new ListItem("请选择" + valC, valC));
	                              ddlChild.SetSelectItemByIndex(0);
	                          }
	        			  }else if(MapExtXmlList.AutoFullDLL.equals(me.getExtType())){//自动填充下拉框的范围.
	        				  Object full = ui.GetUIByID("DDL_" + me.getAttrOfOper() + "_" + wk.getOID());
	        				  if (null == full) continue;
	                          DDL ddlFull = (DDL) full;
	                          
	                          String valOld = wk.GetValStrByKey(me.getAttrOfOper());
	                          String fullSQL = me.getDoc().replace("WebUser.No", WebUser.getNo());
	                          fullSQL = fullSQL.replace("@WebUser.Name", WebUser.getName());
	                          fullSQL = fullSQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
	                          fullSQL = fullSQL.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
	                          fullSQL = fullSQL.replace("@Key", Key);
	                          
	                          if (fullSQL.contains("@")) {
	                              Attrs attrsFull = wk.getEnMap().getAttrs();
	                              for (Attr attr : attrsFull.ToJavaList()){
	                                  if (!fullSQL.contains("@")) break;
	                                  fullSQL = fullSQL.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
	                              }
	                          }
	                          ddlFull.Items.clear();;
	                          ddlFull.Bind(DBAccess.RunSQLReturnTable(fullSQL), "No", "Name");
	                          if (!ddlFull.SetSelectItem(valOld)){
	                              ddlFull.Items.add(0, new ListItem("请选择" + valOld, valOld));
	                              ddlFull.SetSelectItemByIndex(0);
	                          }
	        			  }else if(MapExtXmlList.TBFullCtrl.equals(me.getExtType())){// 自动填充.
	        				  Object auto = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + wk.getOID());
	        				  if (null == auto) continue;
	        				  TextBox tbAuto = (TextBox) auto;
	        				  tbAuto.addAttr("onkeyup", "DoAnscToFillDiv(this,this.value,\'" + tbAuto.getId() + "\', \'" + me.getMyPK() + "\');");
	                          tbAuto.addAttr("AUTOCOMPLETE", "OFF");
	                          if(!"".equals(me.getTag())){
	                        	  /* 处理下拉框的选择范围的问题 */
	                        	  String[] strsTmp = me.getTag().split("$");
	                        	  for(String str : strsTmp){
	                        		  String[] myCtl = str.split(":");
	                        		  String ctlID = myCtl[0];
	                        		  Object c1 = ui.GetUIByID("DDL_" + ctlID + "_" + wk.getOID());
	                        		  if(null == c1)continue;
	                        		  DDL ddlC1 = (DDL) c1;
	                        		  
	                        		  sql = myCtl[1].replace("~", "'");
	                                  sql = sql.replace("WebUser.No", WebUser.getNo());
	                                  sql = sql.replace("@WebUser.Name", WebUser.getName());
	                                  sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
	                                  sql = sql.replace("@Key", tbAuto.getText().trim());
	                                  dt = DBAccess.RunSQLReturnTable(sql);
	                                  String valC1 = ddlC1.getSelectedItemStringVal();
	                                  ddlC1.Items.clear();
	                                  for(DataRow dr : dt.Rows){
	                                	  ddlC1.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
	                                  }
	                                  ddlC1.SetSelectItem(valC1);
	                        	  }
	                          }
	        			  }else if(MapExtXmlList.InputCheck.equals(me.getExtType())){
	        				  Object check = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + wk.getOID());
	        				  if (null != check){
	        					  TextBox tbCheck = (TextBox) check;
	        					  tbCheck.addAttr(me.getTag2(), "rowPK=" + wk.getOID() + "; " + me.getTag1() + "(this);");
	        				  }
	        			  }else if(MapExtXmlList.PopVal.equals(me.getExtType())){//弹出窗.
	        				  Object obj = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + wk.getOID());
	        				  if (null != obj){
	        					  TextBox tb = (TextBox) obj;
	        					  tb.addAttr("ondblclick", "ReturnVal(this,'" + me.getDoc() + "','sd');");
	        				  }
	        			  }else if(MapExtXmlList.Link.equals(me.getExtType())){// 超链接.
	        				   //TB tb = this.Pub1.GetTBByID("TB_" + me.AttrOfOper + "_" + mydtl.OID);
	                          //tb.Attributes["ondblclick"] = " isChange=true; ReturnVal(this,'" + me.Doc + "','sd');";
	        				  break;
	        			  }else if(MapExtXmlList.RegularExpression.equals(me.getExtType())){//正则表达式,对数据控件处理
	        				  Object exp = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + wk.getOID());
	        				  if(null == exp || "onsubmit".equals(me.getTag())) continue;
	        				  TextBox tbExp = (TextBox) exp;
	        				  //验证输入的正则格式
	                          String regFilter = me.getDoc();
	                          if (regFilter.lastIndexOf("/g") < 0 && regFilter.lastIndexOf('/') < 0){
	                        	  regFilter = "'" + regFilter + "'";
	                          }
	                          tbExp.addAttr(me.getTag(), "return txtTest_Onkeyup(this," + regFilter + ",'" + me.getTag1() + "')");
	        			  }
	        		  }
	        	 }
	        }
	        //#endregion 扩展属性
	        
	        Button btn = null;
	        if (nd.getHisBatchRole() == BatchRole.Ordinary){
	        	  /*如果普通的批处理.*/
	        	 btn = ui.creatButton("Btn_Send");
	        	 btn.setCssClass("am-btn am-btn-primary am-btn-xs");
	        	 btn.setText("批量处理："+btnLab.getSendLab());
	        	 btn.addAttr("onclick", "Send_Click();");
	        	 this.ui.append(btn);
//	        	 if(!btnLab.getReturnEnable()){
//	        		 btn = ui.creatButton("Btn_Return");
//	        		 btn.setCssClass("Btn");
//	        		 btn.setText("批量处理:"+btnLab.getReturnEnable());
//	        		 btn.addAttr("onclick", "");
//	            	 this.ui.append(btn);
//	        	 }
	        }
	        
	        if(nd.getHisBatchRole().getValue() == BatchRole.Group.getValue()){
	        	this.ui.append("&nbsp;&nbsp;");
	        	/*如果分组审核？*/
	        	btn = ui.creatButton("Btn_Group");
	        	btn.setCssClass("am-btn am-btn-primary am-btn-xs");
	        	btn.setText("合卷批复");
	        	btn.addAttr("onclick", "Group_Click();");
	        	this.ui.append(btn);
	        }
	        
	       
	        
	        if (!btnLab.getReturnEnable()){
	        	 this.ui.append("&nbsp;&nbsp;");
	        	 btn = ui.creatButton("Btn_Return");
	        	 btn.setCssClass("am-btn am-btn-primary am-btn-xs");
	     		 btn.setText("批量处理："+btnLab.getReturnEnable());
	     		 btn.addAttr("onclick", "Return_Click();");
	         	 this.ui.append(btn);
	        }
	        
	        if (0 != btnLab.getDeleteEnable()){
	        	 this.ui.append("&nbsp;&nbsp;");
	        	 btn = ui.creatButton("Btn_Del");
	        	 btn.setCssClass("am-btn am-btn-primary am-btn-xs");
	     		 btn.setText("批量处理："+btnLab.getDeleteLab());
	     		 btn.addAttr("onclick", "Delete_Click();");
	         	 this.ui.append(btn);
	        }
		}
	}
	
//	public int BindNodeList(){
//		
//		String sql = "SELECT a.NodeID, a.Name,a.FlowName, COUNT(*) AS Num  FROM WF_Node a, WF_EmpWorks b WHERE A.NodeID=b.FK_Node AND B.FK_Emp='" + WebUser.getNo() + "' AND b.WFState NOT IN (7) AND a.BatchRole!=0 GROUP BY A.NodeID, a.Name,a.FlowName ";
//		DataTable dt = DBAccess.RunSQLReturnTable(sql);
//		this.ui.append(BaseModel.AddTable());
//		this.ui.append(BaseModel.AddCaption("批处理"));
//		this.ui.append(BaseModel.AddTR());
//		this.ui.append(BaseModel.AddTDBegin());
//		//this.ui.append(BaseModel.AddBR());
//		this.ui.append(BaseModel.AddUL());
//		
//		for(DataRow dr : dt.Rows){
//			this.ui.append("<Li style='list-style-type:square; color:#959595;'><a href='"+basePath+"WF/Batch.jsp?FK_Node=" + dr.getValue("NodeID")
//                    + "'  style=\"text-decoration:none; font-size:14px; font-weight:normal;\">" + dr.getValue("FlowName") + " --> " + dr.getValue("Name") + "(" + dr.getValue("Num") + ")" + "</a></Li>");
//			this.ui.append(BaseModel.AddBR());
//		}
//		this.ui.append(BaseModel.AddULEnd());
//
//
//		this.ui.append(BaseModel.AddTDEnd());
//		this.ui.append(BaseModel.AddTREnd());
//		this.ui.append(BaseModel.AddTableEnd());
//          
//		return dt.Rows.size();
//	}
	 public final int BindNodeList()
		{
			String sql = "SELECT a.NodeID, a.Name,a.FlowName, COUNT(WorkID) AS NUM  FROM WF_Node a, WF_EmpWorks b WHERE A.NodeID=b.FK_Node AND B.FK_Emp='" + WebUser.getNo() + "' AND b.WFState NOT IN (7) AND a.BatchRole!=0 GROUP BY A.NodeID, a.Name,a.FlowName ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() == 0)
			{
				this.ui.append(BaseModel.AddCaption("批处理"));
				this.ui.append("<div style='margin-left:30px;margin-top:20px;font-size:14px;' ><img src='Img/info.png' align='middle' />当前没有批处理的工作......</div>");
				
				return 0;
			}

			this.ui.append(BaseModel.AddTable("width=100%"));
			this.ui.append(BaseModel.AddCaption("批处理"));

			if (dt.Rows.size() == 0)
			{
				this.ui.append(BaseModel.AddTR());
				this.ui.append(BaseModel.AddTDBegin());

				this.ui.append("<img src='Img/info.png' align='middle' />当前没有批处理的工作.");

				this.ui.append(BaseModel.AddTDEnd());
				this.ui.append(BaseModel.AddTREnd());
				this.ui.append(BaseModel.AddTableEnd());
				return 0;
			}

			this.ui.append(BaseModel.AddTR());
			this.ui.append(BaseModel.AddTDBegin());

			this.ui.append(BaseModel.AddBR());
			this.ui.append(BaseModel.AddUL());
			for(DataRow dr : dt.Rows){
				this.ui.append("<Li style='list-style-type:square; color:#959595;'><a href='"+basePath+"WF/Batch.jsp?FK_Node=" + dr.getValue("NodeID")
	                    + "'  style=\"text-decoration:none; font-size:14px; font-weight:normal;\">" + dr.getValue("FlowName") + " --> " + dr.getValue("Name") + "(" + dr.getValue("Num") + ")" + "</a></Li>");
				this.ui.append(BaseModel.AddBR());
			}
			this.ui.append(BaseModel.AddULEnd());

			this.ui.append(BaseModel.AddTDEnd());
			this.ui.append(BaseModel.AddTREnd());
			this.ui.append(BaseModel.AddTableEnd());
	          

			return dt.Rows.size();
		}
	
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public int getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
	public String getFK_MapData() {
		return FK_MapData;
	}
	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}
	public int getListNum() {
		return ListNum;
	}
	public void setListNum(int listNum) {
		ListNum = listNum;
	}
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getDoType() {
		return DoType;
	}
	public void setDoType(String doType) {
		DoType = doType;
	}
	public String getNormMsg() {
		return normMsg;
	}
	public void setNormMsg(String normMsg) {
		this.normMsg = normMsg;
	}

	private String decimalFormat(Object object){
		if(null == object || "".equals(object.toString())){
			object = 0.00;
		}
		String str = new DecimalFormat("#0.00").format(Double.parseDouble(object.toString()));
		return str;
	}
	
//	private String WinOpen(String url){
//        return this.WinOpen(url, "", "msg", 900, 500, 0, 0);
//    }
//	private String WinOpen(String url, String title, String winName, int width, int height, int top, int left){
//        url = url.replace("<", "[");
//        url = url.replace(">", "]");
//        url = url.trim();
//        title = title.replace("<", "[");
//        title = title.replace(">", "]");
//        title = title.replace("\"", "‘");
//        return "<script type=='text/javascript'> var newWindow = window.open(' " + url + "','" + winName + "','width=" + width + ",top=" + top + ",left=" + left + ",height=" + height + ",scrollbars=yes,resizable=yes') ; newWindow.focus(); </script> ";
//    }
}
