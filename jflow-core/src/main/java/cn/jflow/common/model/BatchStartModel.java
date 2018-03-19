package cn.jflow.common.model;

import java.text.DecimalFormat;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
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
import BP.WF.Glo;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Work;
import BP.Web.WebUser;


public class BatchStartModel {

	private int RowNum;
	private String Key;
	private String basePath;
	private String FK_Flow;
	private String FK_MapData;
	
	private String DoType;
	private String normMsg;
	
	public BatchStartModel(String basePath, String Key, int RowNum, String FK_MapData, String FK_Flow, String DoType, String normMsg){
		this.basePath = basePath;
		this.Key = Key;
		this.FK_MapData = FK_MapData;
		this.RowNum = RowNum;
		this.FK_Flow = FK_Flow;
		
		this.DoType = DoType;
		this.normMsg = normMsg;
	}
	
	public String title;
	public UiFatory ui = null;
	public void init() {
		this.ui = new UiFatory();
		
		if("Send".equals(DoType)){
			this.ui.append(BaseModel.AddFieldSet("批量处理信息"));
			if(null != normMsg && !"".equals(normMsg)){
				 normMsg = normMsg.replace("@@","@");
				 this.ui.append(normMsg);//.replace("@", "<br>@"));
			}
			this.ui.append(BaseModel.AddFieldSetEnd());
//			this.ui.append(BaseModel.AddH2("&nbsp;&nbsp;发起信息"));
//			if(null != normMsg && !"".equals(normMsg)){
//				 normMsg = normMsg.replace("@@","@");
//				 this.ui.append(normMsg.replace("@", "<br>@"));
//			}
		}else{
			Flow fl = new Flow(this.FK_Flow);
	        this.title = fl.getName();
	        MapAttrs attrs = new MapAttrs(this.FK_MapData);
	        if (fl.getBatchStartFields().length() == 0){
	             this.ui.append(BaseModel.AddFieldSet("流程属性设置错误"));
	             this.ui.append("您需要在流程属性里设置批量发起需要填写的字段。");
	             this.ui.append(BaseModel.AddFieldSetEnd());
	         }
			
	         MapExts mes = new MapExts(this.FK_MapData);

	         Node nd = new Node(Integer.parseInt(this.FK_Flow + "01"));
	         Work wk = nd.getHisWork();
	         wk.ResetDefaultVal();
	         
	         this.ui.append(BaseModel.AddTable());
	         this.ui.append(BaseModel.AddCaption("批量发起:" + fl.getName() + " --> " + nd.getName()));

	         //#region 输出标题.
	         this.ui.append(BaseModel.AddTR());
	         this.ui.append(BaseModel.AddTDTitle("序"));
	         
	         String str1 = "<input id='checkedAll' onclick=\"SelectAllBS(this);\" value='选择' type='checkbox' name='checkedAll' >全部选择";
	         this.ui.append(BaseModel.AddTDTitle("align='left'", str1));
	         
	         //this.AddTDTitle("align='left'", "");

	         String[] strs = fl.getBatchStartFields().split(",");
	         for (String str : strs){
	        	 if (StringHelper.isNullOrEmpty(str))
	                 continue;
	        	 
	        	 for (MapAttr attr : attrs.ToJavaList()){
	                 if (!str.equals(attr.getKeyOfEn()))
	                     continue;
	                 this.ui.append(BaseModel.AddTDTitle(attr.getName()));
	             }
	         }
	         this.ui.append(BaseModel.AddTREnd());
	         //#endregion 输出标题.
	         
	         // #region 输出标题.
	         for (int i = 1; i <= this.RowNum; i++){
	        	 this.ui.append(BaseModel.AddTR());
	        	 this.ui.append(BaseModel.AddTDIdx(i));
	             CheckBox cbIdx = this.ui.creatCheckBox("CB_IDX_" + i);
	             cbIdx.setChecked(false);
	             cbIdx.setText("发起否?");
	             this.ui.append(BaseModel.AddTD(cbIdx));
	             
	             for (String str : strs){
	            	 if (StringHelper.isNullOrEmpty(str))
	                     continue;
	            	 
	                 for (MapAttr attr : attrs.ToJavaList()){
	                	 if (!str.equals(attr.getKeyOfEn()))
	                         continue;
	                	 
	                	 TextBox tb = ui.creatTextBox("TB_" + attr.getKeyOfEn() + "_" + i);
	                	 switch (attr.getLGType()){
	                	  	case Normal:
	                          switch (attr.getMyDataType()){
	                          		case DataType.AppString:
	                          			if (attr.getUIRows() == 1){
		           		  					 tb.setText(attr.getDefVal());
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
			    		  					tb.setText(attr.getDefVal());
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
	            		  				tb.setText(attr.getDefVal());
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
	        							tb.setText(attr.getDefVal());
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
	            		  				 CheckBox cb = ui.creatCheckBox("CB_" + attr.getKeyOfEn() + "_" + i);
	            		  				 //cb.Width = 350;
	            		  				 cb.setText(attr.getName());
	        							 cb.setEnabled(attr.getUIIsEnable());
	        							 cb.setChecked(attr.getDefValOfBool());
	        							 
	        							 if (!cb.getEnabled()){
	        								 cb.setEnabled(false);
	        							 }else{
	                                         //add by dgq 2013-4-9,添加内容修改后的事件
	                                        // cb.Attributes["onmousedown"] = "Change('" + attr.FK_MapData + "')";
	                                    	 cb.setEnabled(true);
	                                     }
	        							 
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
	            		  				tb.setText(decimalFormat(attr.getDefVal()));
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
	            		  				tb.setText(decimalFormat(attr.getDefVal()));
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
	            		  			DDL ddle = ui.creatDDL("DDL_" + attr.getKeyOfEn() + "_" + i);
	            		  			ddle.BindSysEnum(attr.getUIBindKey());
	            		  			ddle.SetSelectItem(attr.getDefVal());
	            		  			ddle.setEnabled(attr.getUIIsEnable());
	        						ddle.addAttr("tabindex", String.valueOf(attr.getIdx()));
	        						if (attr.getUIIsEnable()){
	        							//add by dgq 2013-4-9,添加内容修改后的事件
	        							//ddle.addAttr("onchange", "Change('" + attr.getFK_MapData() + "')");
	        						}
	        						
	        						this.ui.append("\n<TD >");
	    		  	            	this.ui.append(ddle);
	    		  	            	this.ui.append("</TD>");
	            		  		 }else{}
	                            break;
	                        case FK:
	                        	DDL ddl1 = ui.creatDDL("DDL_" + attr.getKeyOfEn() + "_" + i);
	            		  		ddl1.addAttr("tabindex", String.valueOf(attr.getIdx()));
	            		  		if (ddl1.getEnabled()){
	            		  			 EntitiesNoName ens = attr.getHisEntitiesNoName();
	            		  			 ens.RetrieveAll();
	            		  			 ddl1.BindEntities(ens);
	            		  			 ddl1.SetSelectItem(attr.getDefVal());
	            		  			 //add by dgq 2013-4-9,添加内容修改后的事件
	            		  			//ddl1.addAttr("onchange", "Change('" + attr.getFK_MapData() + "')");
	            		  		}else{
	            		  			ddl1.addAttr("style", "width: " + attr.GetValStrByKey("UIWidth") + "px;height: 19px;");
	            		  			if (ddl1.getEnabled())ddl1.setEnabled(false);
	            		  			ddl1.addAttr("Width", String.valueOf(attr.getUIWidth()));
	            		  			ddl1.Items.add(new ListItem(attr.getDefVal(), attr.getDefVal()));
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
	             this.ui.append(BaseModel.AddTREnd());
	         }
	         //#endregion 输出标题.
	         
//	         this.ui.append(BaseModel.AddTR());
//	     	 Button btn = this.ui.creatButton("Btn_Start");
//		     btn.setCssClass("am-btn am-btn-primary am-btn-xs");
//		   	 btn.setText("执行发起");
//		   	 btn.addAttr("onclick", "Send_Click();");
//		   	 this.ui.append(BaseModel.AddCaption(btn.toString()));
//	         this.ui.append(BaseModel.AddTREnd());
	         
	         this.ui.append(BaseModel.AddTableEndWithHR());
	         
	         //#region 处理扩展属性.
	         if (mes.size() != 0){
	        	 this.ui.append("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");
	        	 for (int i = 1; i <= this.RowNum; i++){ 	        		 for(MapExt me : mes.ToJavaList()){
	        			 if(MapExtXmlList.DDLFullCtrl.equals(me.getExtType())){// 自动填充.
	        				  Object oper = ui.GetUIByID("DDL_" + me.getAttrOfOper() + "_" + i);
	                          if (null == oper) continue;
	                          DDL ddlOper = (DDL) oper;
	                          ddlOper.addAttr("onchange", "DDLFullCtrl(this.value,\'" + ddlOper.getId()+ "\', \'" + me.getMyPK() + "\')");
	        			 }else if(MapExtXmlList.ActiveDDL.equals(me.getExtType())){
	       				  Object parent = ui.GetUIByID("DDL_" + me.getAttrOfOper() + "_" + i);
	       				  if (null == parent)continue;
	       				  DDL ddlPerant = (DDL) parent;
	       				  //#warning 此处需要优化
	       				  String ddlC = "DDL_" + me.getAttrsOfActive() + "_" + i;
	       				  ddlPerant.addAttr("onchange", "DDLAnsc(this.value,\'" + ddlC+ "\', \'" + me.getMyPK() + "\')");
	       				  
	       				  String valC;
	       				  Object child = ui.GetUIByID("DDL_" + me.getAttrsOfActive() + "_" + i);
	       				  if (null == child)continue;
	       				  DDL ddlChild = (DDL) child;
	       				  String val = ddlPerant.getSelectedItemStringVal();
	       				  if(ddlChild.Items.size() == 0){
	       					  valC = wk.GetValStrByKey(me.getAttrsOfActive());
	       				  }else{
	       					  valC = ddlChild.getSelectedItemStringVal();
	       				  }
	       				  
	       				  String mysql = me.getDoc().replace("@Key", val);
	       				  if (mysql.contains("@") && i >= 100){
	                             mysql = Glo.DealExp(mysql, wk, null);
	                      }else{
	                    	  continue;
	                          //mysql = mysql.replace("WebUser.No", WebUser.getNo());
	                          //mysql = mysql.replace("@WebUser.Name", WebUser.getName());
	                          //mysql = mysql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
	                      }
	       				  ddlChild.Bind(DBAccess.RunSQLReturnTable(mysql), "No", "Name");
	       				  if (!ddlChild.SetSelectItem(valC)){
	                         ddlChild.Items.add(0, new ListItem("请选择" + valC, valC));
	                         ddlChild.SetSelectItemByIndex(0);
	                      }
	       			  	}else if(MapExtXmlList.AutoFullDLL.equals(me.getExtType())){//自动填充下拉框的范围.
	       				  Object full = ui.GetUIByID("DDL_" + me.getAttrOfOper() + "_" + i);
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
	    				  Object auto = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + i);
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
	                    		  Object c1 = ui.GetUIByID("DDL_" + ctlID + "_" + i);
	                    		  if(null == c1)continue;
	                    		  DDL ddlC1 = (DDL) c1;
	                    		  
	                    		  String sql = myCtl[1].replace("~", "'");
	                              sql = sql.replace("WebUser.No", WebUser.getNo());
	                              sql = sql.replace("@WebUser.Name", WebUser.getName());
	                              sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
	                              sql = sql.replace("@Key", tbAuto.getText().trim());
	                              DataTable dt = DBAccess.RunSQLReturnTable(sql);
	                              String valC1 = ddlC1.getSelectedItemStringVal();
	                              ddlC1.Items.clear();
	                              for(DataRow dr : dt.Rows){
	                            	  ddlC1.Items.add(new ListItem(dr.getValue(1).toString(), dr.getValue(0).toString()));
	                              }
	                              ddlC1.SetSelectItem(valC1);
	                    	  }
	                      }
	       			  	}else if(MapExtXmlList.InputCheck.equals(me.getExtType())){
	       			  	  Object check = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + i);
	    				  if (null != check){
	    					  TextBox tbCheck = (TextBox) check;
	    					  tbCheck.addAttr(me.getTag2(), "rowPK=" + i + "; " + me.getTag1() + "(this);");
	    				  }
	       			  	}else if(MapExtXmlList.PopVal.equals(me.getExtType())){//弹出窗.
	      				  Object obj = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + i);
	      				  if (null != obj){
	      					  TextBox tb = (TextBox) obj;
	      					  tb.addAttr("ondblclick", "ReturnVal(this,'" + me.getDoc() + "','sd');");
	      				  }
	       			  	}else if(MapExtXmlList.Link.equals(me.getExtType())){// 超链接.
	     				   //TB tb = this.Pub1.GetTBByID("TB_" + me.AttrOfOper + "_" + mydtl.OID);
	                        //tb.Attributes["ondblclick"] = " isChange=true; ReturnVal(this,'" + me.Doc + "','sd');";
		      			}else if(MapExtXmlList.RegularExpression.equals(me.getExtType())){//正则表达式,对数据控件处理
		      				  Object exp = ui.GetUIByID("TB_" + me.getAttrOfOper() + "_" + i);
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
	         
	        Button btn = this.ui.creatButton("Btn_Start");
	        btn.setCssClass("am-btn am-btn-primary am-btn-xs");
	   	    btn.setText("批量处理：发送");
	   	    btn.addAttr("onclick", "Send_Click();");
	   	    this.ui.append(btn);
	   	    
		   	//#region 文件上传.
//	   	    this.ui.append(BaseModel.AddFieldSet("通过Excel导入方式发起:<a href='"+basePath+"DataUser/BatchStartFlowTemplete/" + this.FK_Flow + ".xls'><img src='"+basePath+"WF/Img/FileType/xls.gif' />下载Excel模版</a>"));
//	   	    this.ui.append("文件名:");
//		    FileUpload fu = this.ui.creatFileUpload("fileupload");
//		    this.ui.append(fu);
//		    btn = this.ui.creatButton("Btn_Imp");
//		    btn.setText("导入");
//		    btn.addAttr("onclick", "Upload_Click();");
//		    this.ui.append(btn);
//		    this.ui.append(BaseModel.AddFieldSetEnd());
		    //#endregion 文件上传.
		}
	}
	
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public int getRowNum() {
		return RowNum;
	}
	public void setRowNum(int rowNum) {
		RowNum = rowNum;
	}
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
	public String getFK_MapData() {
		return FK_MapData;
	}
	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
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
	
}
