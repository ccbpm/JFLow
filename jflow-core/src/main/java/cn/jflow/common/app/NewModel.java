package cn.jflow.common.app;

import java.text.DecimalFormat;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ImageButton;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import cn.jflow.system.ui.core.ToolBar;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrFile;
import BP.En.AttrFiles;
import BP.En.AttrOfOneVSM;
import BP.En.Attrs;
import BP.En.AttrsOfOneVSM;
import BP.En.EditerType;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethods;
import BP.En.TBType;
import BP.En.UAC;
import BP.Sys.EnCfg;
import BP.Sys.SysFileManager;
import BP.Sys.SysFileManagerAttr;
import BP.Sys.SysFileManagers;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.Web.WebUser;

public class NewModel {

	private static String basePath = Glo.getCCFlowAppPath();

	 public static void Bind(Entity en, String enName, String ensName, boolean isReadonly, boolean isShowDtl, UiFatory ui){
         Bind(en, enName, ensName, isReadonly, isShowDtl, null, ui);
     }
     public static void Bind(Entity en, String enName, String ensName, boolean isReadonly, boolean isShowDtl, String noKey, UiFatory ui){
    	 
    	 ui.append(BaseModel.AddTable("class='Table cellpadding='1' cellspacing='1' border='1' style='width:100%'"));
    	 
    	 Map map = en.getEnMap();
    	 Attrs attrs = map.getAttrs();
    	 EnCfg cf = new EnCfg(enName);//读取Sys_EnCfg系统配置表的配置信息
    	 String[] gTitles = cf.getGroupTitle().split("@");
    	 
    	 String  val = "";
    	 boolean isLeft = true;
    	 for (Attr attr : attrs){
    		 if (attr.getKey().equals("MyNum"))
                 continue;
             if (attr.getMyFieldType() == FieldType.RefText)
                 continue;
             
             val = en.GetValStrByKey(attr.getKey());
             if (!attr.getUIVisible()){
            	 SetValByKey(attr.getKey(), val, ui);
                 continue;
             }
             for (String g : gTitles){//系统业务需要
				if (g.contains(attr.getKey() + "=")) {
					if (!isLeft) {
						ui.append(BaseModel.AddTD());
						ui.append(BaseModel.AddTD());
						ui.append(BaseModel.AddTREnd());
					}

					String[] ss = g.split("=");
					ui.append(BaseModel.AddTRSum());
					ui.append(BaseModel.AddTD("colspan='4' class='FDesc'", "<b>"	+ ss[1] + "</b>"));
					ui.append(BaseModel.AddTREnd());
					isLeft = true;
					break;
				}
             }
             
             //#region判断是否单列显示
             if (attr.UIIsLine){
            	 if (!isLeft){
            		 ui.append(BaseModel.AddTD());
            		 ui.append(BaseModel.AddTD());
            		 ui.append(BaseModel.AddTREnd());
                     isLeft = true;
                 }
            	 ui.append(BaseModel.AddTR());
            	 //大文本处理
                 if (attr.getUIHeight() != 0){
                	 /*大块文本采集, 特殊处理。*/
                     if (val.length() == 0 && !en.getIsEmpty() && "Doc".equals(attr.getKey()))
                         val = en.GetValDocText();
                     /* doc 文本类型。　*/
                     if (attr.getUIIsReadonly() || isReadonly){
                         TextBox areaR = ui.creatTextBox("TB_" + attr.getKey());
                         areaR.setText(val);
                         areaR.setRows(5);
                         areaR.setTextMode(TextBoxMode.MultiLine);
                         areaR.setReadOnly(true);
                         AddContral(attr.getDesc(), areaR, 4, ui);
                     }else{
                    	 EditerType type = EditerType.forValue(0);
                    	 switch (type){
                    	 	 case FKEditer:
                    	 	 case Sina:
	                        	 TextBox input = ui.creatTextBox("TBH_" + attr.getKey());
	                        	 input.setTextMode(TextBoxMode.Hidden);
	                        	 input.setText(val);
	                             ui.append("<td class='FDesc'  colspan='4' width='50%'>");
	                             ui.append(input);
	                             ui.append("<iframe ID='eWebEditor1' src='./Ctrl/editor/editor.htm?id=" + input.getId() + "&style=coolblue' frameborder='0' scrolling='no' width='600' HEIGHT='350'></iframe>");
	                             ui.append(BaseModel.AddTDEnd());
	                        	 break;
	                         default:
	                        	 TextBox area = ui.creatTextBox("TB_" + attr.getKey());
	                             area.LoadMapAttr(attr);
	                             area.setText(val);
	                             area.setRows(5);
	                             area.setTextMode(TextBoxMode.MultiLine);
	                             area.setIsHelpKey("1");
	                             AddContral(attr.getDesc(), area, 4, ui);
	                        	 break;
                    	 }
                     }
                     ui.append(BaseModel.AddTREnd());
                     
                     isLeft = true;
                     continue;
                 }
                 //大文本处理结束
                 
                 switch (attr.getUIContralType()){
                 	case TB:
                 		TextBox tb = ui.creatTextBox("TB_" + attr.getKey());
                 		switch (attr.getMyDataType()){
	                 		case DataType.AppMoney:
	                            tb.setText(decimalFormat(val));
	                            tb.addAttr("Class","TBNum");
	                            tb.setShowType(TBType.Moneny);
	                            break;
	                        case DataType.AppInt:
	                        case DataType.AppFloat:
	                        case DataType.AppDouble:
	                        case DataType.AppRate:
	                            tb.addAttr("Class","TBNum");
	                            tb.setText(val);
	                            tb.setShowType(TBType.Num);
	                            break;
	                        case DataType.AppDate:
	                            tb.setText(val);
	                            tb.setShowType(TBType.Date);
	                            tb.addAttr("Class","TBcalendar");
	                            tb.setIsHelpKey("1");
	                            if (!attr.getUIIsReadonly())
	                                tb.addAttr("onfocus","WdatePicker();");
	                            break;
	                        case DataType.AppDateTime:
	                            tb.setText(val);
	                            tb.setShowType(TBType.Date);
	                            tb.addAttr("Class","TBcalendar");
	                            tb.setIsHelpKey("1");
	                            if (!attr.getUIIsReadonly())
	                                tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
	                            break;
	                        default:
	                            tb.setText(val);
	                            tb.addAttr("Class","TB");
	                            break;
	                 		 
	                 	}
                 		
                 		 if (isReadonly || attr.getUIIsReadonly())
                             tb.setReadOnly(true);
                 		 tb.setEnabled(true);
                 		 if (attr.getKey().equals(noKey) && !attr.getUIIsReadonly() && attr.getUIVisible()){
                             //String helpScript = "";
                             String helpScript = "<a href=\"javascript:alert('请在文本框上双击，系统会出现查找窗口。');\" ><img src='"+ basePath +"WF/Img/Btn/Search.gif' border=0 /></a>";
                             tb.addAttr("ondblclick","OpenHelperTBNo('" + basePath + "WF','BP.Port.Taxpayers',this)");
                             AddContral(attr.getDesc(), tb, helpScript, 3, ui);
                         }else{
                             //tb.setCols(80);
                        	 tb.addAttr("style","width:93%");
                             AddContral(attr.getDesc(), tb, 3, ui);
                         }
                 		 
                 		break;
                 	case DDL:
                 		 if (isReadonly || attr.getUIIsReadonly()){
                 			 DDL ddl = ui.creatDDL("DDL_" + attr.getKey());
                             ddl.setCssClass("DDL" + WebUser.getStyle());
                             String text = en.GetValRefTextByKey(attr.getKey());
                             if (StringHelper.isNullOrEmpty(text))
                                 text = val;

                             ListItem li = new ListItem(text, val);
                             li.addAttr("class","TB");
                             ddl.Items.add(li);
                             ddl.setEnabled(false);
                             AddContral(attr.getDesc(), ddl, true, 3, ui);
                 			 
                 		 }else{
                 			 /* 可以使用的情况. */
                 			 if (attr.getMyFieldType() == FieldType.Enum) {
                 				  DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                  ddl1.setId("DDL_" + attr.getKey());
                                  ddl1.setName("DDL_" + attr.getKey());
                                  AddContral(attr.getDescHelper(), ddl1, true, ui);
                 			 }else{
                 				if(!StringHelper.isNullOrEmpty(attr.getGrandChildKey())){//三层级联
                 					  DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                      ddl1.setId("DDL_" + attr.getKey());
                                      ddl1.setName("DDL_" + attr.getKey());
                                      ddl1.addAttr("onchange", "onThreeCasca('DDL_"+attr.getKey()+"','DDL_" + attr.getChildKey() + "','DDL_" + attr.getGrandChildKey()
                                    		  + "','"+ensName+"','','" + attr.getChildKey() + "','" + attr.getGrandChildKey() + "')");
                                      AddContral(attr.getDescHelper(), ddl1, true, ui);
                 				}else if(!StringHelper.isNullOrEmpty(attr.getChildKey())){//二层级联
                 					if(!StringHelper.isNullOrEmpty(attr.getParentKey())){
    	         						 DDL ddl1 = new DDL();
    	         						 ddl1.setId("DDL_" + attr.getKey());
    	                                 ddl1.setName("DDL_" + attr.getKey());
    	         						 ddl1.addAttr("onchange", "onTwoCasca('DDL_"+attr.getParentKey()+"','DDL_"+attr.getKey()+"','DDL_" + attr.getChildKey()
    	        								+ "','"+ensName+"','" + attr.getKey() + "','" + attr.getChildKey() + "')");
    	         						 AddContral(attr.getDescHelper(), ddl1, true, ui);
                 					}else{
                 						 DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                         ddl1.setId("DDL_" + attr.getKey());
                                         ddl1.setName("DDL_" + attr.getKey());
                                         ddl1.addAttr("onchange", "onCasca('DDL_"+attr.getKey()+"','DDL_" + attr.getChildKey()
                 								+ "','"+ensName+"','" + attr.getChildKey() + "')");
                                         AddContral(attr.getDescHelper(), ddl1, true, ui);
                 					}
                 				}else if(!StringHelper.isNullOrEmpty(attr.getParentKey())){
                 					 DDL ddl1 = new DDL();
             						 ddl1.setId("DDL_" + attr.getKey());
                                     ddl1.setName("DDL_" + attr.getKey());
                                     AddContral(attr.getDescHelper(), ddl1, true, ui);
                 				}else{
                 					 DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                     ddl1.setId("DDL_" + attr.getKey());
                                     ddl1.setName("DDL_" + attr.getKey());
                                     AddContral(attr.getDescHelper(), ddl1, true, ui);
                 				}
                 			 }
                 		 }
                 		break;
                 	 case CheckBok:
                 		 CheckBox cb = ui.creatCheckBox("CB_" + attr.getKey());
                         cb.setChecked(en.GetValBooleanByKey(attr.getKey()));

                         if (isReadonly)
                             cb.setEnabled(false);
                         else
                             cb.setEnabled(attr.getUIIsReadonly());

                         cb.setText(attr.getDesc());
                         AddContral("", cb, 3, ui);
                 		break;
                 	  default:
                        break;
                 }
                 ui.append(BaseModel.AddTREnd());
                 isLeft = true;
                 continue;
             }
             //#endregion 判断是否单列显示  结束要显示单行的情况
             
             if (isLeft)
            	 ui.append(BaseModel.AddTR());
             
             switch (attr.getUIContralType()){
             	case TB:
             		 if (attr.getUIHeight() != 0){//大文本处理
             			 if (val.length() == 0 && !en.getIsEmpty() && "Doc".equals(attr.getKey()))
                               val = en.GetValDocText();
             			 
             			 EditerType type = EditerType.forValue(0);
                    	 switch (type){
                    	 	 case None:
                    	 		  if (attr.getUIIsReadonly() || isReadonly){
                    	 			  TextBox areaR = ui.creatTextBox("TB_" + attr.getKey());
	                                  areaR.setText(val);
	                                  areaR.setRows(8);
	                                  areaR.setTextMode(TextBoxMode.MultiLine);
	                                  areaR.setReadOnly(true);
	                                  AddContral(attr.getDescHelper(), areaR, ui);
                    	 		  }else{
                    	 			 TextBox area = ui.creatTextBox("TB_" + attr.getKey());
                                     area.LoadMapAttr(attr);
                                     area.setText(val);
                                     area.setRows(8);
                                     area.setTextMode(TextBoxMode.MultiLine);
                                     area.setIsHelpKey("1");
                                     AddContral(attr.getDescHelper(), area, ui);
                    	 		  }
                    	         break;
                    	 	 case FKEditer:
                    	 	 case Sina:
                    	 		 /* doc 文本类型。　*/
                            	 TextBox tb = ui.creatTextBox("txtContent");
                            	 tb.setTextMode(TextBoxMode.Hidden);
                            	 ui.append("<td class='FDesc'  colspan='2' nowrap width='50%' >" + attr.getDesc() + "<br>");
                            	 ui.append(tb);
                            	 ui.append("<iframe ID='eWebEditor1' src='./Ctrl/Edit/editor.htm?id=txtContent&style=coolblue' frameborder='0' scrolling='no' width='600' HEIGHT='350'></iframe>");
                            	 ui.append(BaseModel.AddTDEnd());
	                        	 break;
	                         default:
	                        	 break;
                    	 }
             		 }else{
             			 TextBox tb = ui.creatTextBox("TB_" + attr.getKey());
             		     switch (attr.getMyDataType()){
	             		     case DataType.AppRate:
	             		     case DataType.AppMoney:
	             		    	 try {
									tb.setText(decimalFormat(val));
									tb.addAttr("Class", "TBNum");
								 } catch (Exception e) {
									tb.setText(decimalFormat("0"));
									tb.addAttr("Class", "TBNum");
								 }
								 tb.setShowType(TBType.Moneny);
	                             break;
	             		    case DataType.AppInt:
                            case DataType.AppFloat:
                            case DataType.AppDouble:
                            	   tb.addAttr("Class","TBNum");
                                   tb.setShowType(TBType.Float);
                                   tb.setText(val);
                                break;
                            case DataType.AppDate:
                                tb.setText(val);
                                tb.setShowType(TBType.Date);
                                tb.setIsHelpKey("1");
                                //if (!attr.getUIIsReadonly())
                                    tb.addAttr("onfocus","WdatePicker();");
                                break;
                            case DataType.AppDateTime:
                                tb.setText(val);
                                tb.setShowType(TBType.Date);
                                tb.setIsHelpKey("1");
                                //if (!attr.getUIIsReadonly())
                                    tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
                                break;
                            default:
                                tb.setText(val);
                                tb.addAttr("Class", "TB");
                                break;   
             		    }
             		     
             		    if (isReadonly || attr.getUIIsReadonly())
                            tb.setReadOnly(true);

                        if (attr.getIsPK() && !StringHelper.isNullOrEmpty(tb.getText()))
                            tb.setReadOnly(true);

                        if (attr.getKey().equals(noKey) && !attr.getUIIsReadonly() && !attr.getUIVisible()){
                            //String helpScript = "";
                            String helpScript = "<a href=\"javascript:alert('请在文本框上双击，系统会出现查找窗口。');\" ><img src='./Img/Btn/Search.gif' border=0 /></a>";
                            tb.addAttr("ondblclick","OpenHelperTBNo('" + basePath + "WF','BP.Port.Taxpayers',this)");
                            AddContral(attr.getDescHelper(), tb, helpScript, ui);
                        }else{
                            AddContral(attr.getDescHelper(), tb, ui);
                        }
             		 }
             		 break;
             	case DDL:
             		 if (isReadonly || attr.getUIIsReadonly()){
             			 DDL ddl = ui.creatDDL("DDL_" + attr.getKey());
                         ddl.setCssClass("DDL" + WebUser.getStyle());
                         String text = en.GetValRefTextByKey(attr.getKey());
                         if (StringHelper.isNullOrEmpty(text))
                        	 text = val;

                         ListItem li = new ListItem(text, val);
                         li.addAttr("class","TB");
                         ddl.Items.add(li);
                         ddl.setEnabled(false);
                         AddContral(attr.getDescHelper(), ddl, true, ui);
             		 }else{
             			 /* 可以使用的情况. */
             			 if (attr.getMyFieldType() == FieldType.Enum) {
             				  DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                              ddl1.setId("DDL_" + attr.getKey());
                              ddl1.setName("DDL_" + attr.getKey());
                              AddContral(attr.getDescHelper(), ddl1, true, ui);
             			 }else{
             				if(!StringHelper.isNullOrEmpty(attr.getGrandChildKey())){//三层级联
             					  DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                  ddl1.setId("DDL_" + attr.getKey());
                                  ddl1.setName("DDL_" + attr.getKey());
                                  ddl1.addAttr("onchange", "onThreeCasca('DDL_"+attr.getKey()+"','DDL_" + attr.getChildKey() + "','DDL_" + attr.getGrandChildKey()
                                		  + "','"+ensName+"','','" + attr.getChildKey() + "','" + attr.getGrandChildKey() + "')");
                                  AddContral(attr.getDescHelper(), ddl1, true, ui);
             				}else if(!StringHelper.isNullOrEmpty(attr.getChildKey())){//二层级联
             					if(!StringHelper.isNullOrEmpty(attr.getParentKey())){
	         						 DDL ddl1 = new DDL();
	         						 ddl1.setId("DDL_" + attr.getKey());
	                                 ddl1.setName("DDL_" + attr.getKey());
	         						 ddl1.addAttr("onchange", "onTwoCasca('DDL_"+attr.getParentKey()+"','DDL_"+attr.getKey()+"','DDL_" + attr.getChildKey()
	        								+ "','"+ensName+"','" + attr.getKey() + "','" + attr.getChildKey() + "')");
	         						 AddContral(attr.getDescHelper(), ddl1, true, ui);
             					}else{
             						 DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                     ddl1.setId("DDL_" + attr.getKey());
                                     ddl1.setName("DDL_" + attr.getKey());
                                     ddl1.addAttr("onchange", "onCasca('DDL_"+attr.getKey()+"','DDL_" + attr.getChildKey()
             								+ "','"+ensName+"','" + attr.getChildKey() + "')");
                                     AddContral(attr.getDescHelper(), ddl1, true, ui);
             					}
             				}else if(!StringHelper.isNullOrEmpty(attr.getParentKey())){
             					 DDL ddl1 = new DDL();
         						 ddl1.setId("DDL_" + attr.getKey());
                                 ddl1.setName("DDL_" + attr.getKey());
                                 AddContral(attr.getDescHelper(), ddl1, true, ui);
             				}else{
             					 DDL ddl1 = new DDL(attr, val, "enumLab", true, basePath);
                                 ddl1.setId("DDL_" + attr.getKey());
                                 ddl1.setName("DDL_" + attr.getKey());
                                 AddContral(attr.getDescHelper(), ddl1, true, ui);
             				}
             			 }
             		 }
             		 break;
             	case CheckBok:
             		 CheckBox cb = ui.creatCheckBox("CB_" + attr.getKey());
                     cb.setChecked(en.GetValBooleanByKey(attr.getKey()));
             		
                     if (isReadonly)
                         cb.setEnabled(false);
                     else
                         cb.setEnabled(attr.getUIIsReadonly());
                     
                     cb.setText(attr.getDesc());
                     if (attr.HelperUrl == null)
                         AddContral("", cb, ui);
                     else
                         AddContral(attr.getDescHelperIcon(), cb, ui);
                     
             		break;
             	default:
                    break;
             }
             
             if (!isLeft)
            	 ui.append(BaseModel.AddTREnd());
             isLeft = !isLeft;
    	 }
    	 
    	 // 结束循环.
         if (!isLeft){
             AddContral(ui);
             ui.append(BaseModel.AddTREnd());
         }
         
         String two = "";
 		 String three = "";
         for (Attr attr : attrs){
		     switch (attr.getUIContralType()){
		    	 case DDL:
		    		 if(!StringHelper.isNullOrEmpty(attr.getGrandChildKey())){//三层级联
		    			 DDL bigDDL = ui.GetUIByID("DDL_" + attr.getKey()) == null?null:(DDL)ui.GetUIByID("DDL_" + attr.getKey());
		        		 DDL smallDDL = ui.GetUIByID("DDL_" + attr.getChildKey()) == null?null:(DDL)ui.GetUIByID("DDL_" + attr.getChildKey());
		        		 DDL litSmallDDL = ui.GetUIByID("DDL_" + attr.getGrandChildKey()) == null?null:(DDL)ui.GetUIByID("DDL_" + attr.getGrandChildKey());
		        		 three += "\t\n onThreeCasca(\"" + bigDDL.getId() + "\",\""+ smallDDL.getId() +"\",\""+ litSmallDDL.getId() +"\",\""+ensName+"\",\""+en.getPKVal()+"\",\""+attr.getChildKey()+"\",\""+attr.getGrandChildKey()+"\");";
		        	 }else if(!StringHelper.isNullOrEmpty(attr.getChildKey())){//二层级联
		        		 if(StringHelper.isNullOrEmpty(attr.getParentKey())){
		        			 DDL bigDDL = ui.GetUIByID("DDL_" + attr.getKey()) == null?null:(DDL)ui.GetUIByID("DDL_" + attr.getKey());
		           		   	 DDL smallDDL = ui.GetUIByID("DDL_" + attr.getChildKey()) == null?null:(DDL)ui.GetUIByID("DDL_" + attr.getChildKey());
		           		   	 two += "\t\n onCasca(\"" + bigDDL.getId() + "\",\""+ smallDDL.getId() +"\",\""+ensName+"\",\""+en.getPKVal()+"\",\""+attr.getChildKey()+"\");";
		        		 }
		        	 }
		    		break;
		    	 default:
	                break;
		     }
         }
         if(two.length()>0 || three.length()>0){//说明有级联下拉框
         	StringBuilder script = new StringBuilder();
         	script.append("\t\n <script type='text/javascript'>");
         	//script.append("\t\n$(document).ready(function(){");
         	script.append(two);
         	script.append(three);
         	//script.append("\t\n}); ");
         	script.append("\t\n </script>");
         	ui.append(script.toString());
         }
         
         for (Attr attr : attrs){
        	 //#region 查看是否包含 MyFile字段如果有就认为是附件。
        	 if("MyFileName".equals(attr.getKey())){
        		 /* 如果包含这二个字段。*/
                 String fileName = en.GetValStringByKey("MyFileName");
                 //String filePath = en.GetValStringByKey("MyFilePath");
                 String fileExt = en.GetValStringByKey("MyFileExt");
                 String webPath = en.GetValStringByKey("WebPath");
                 String url = "";
                 if (!StringHelper.isNullOrEmpty(fileExt)){
                	 // 系统物理路径。
                     //String path = request.getSession().getServletContext().getRealPath("/");
                     //String path1 = filePath.toLowerCase();
                     //path1 = path1.replace(path, "");
                     //url = "&nbsp;&nbsp;<a href='../Do.jsp?DoType=DownFile&EnName=" + enName + "&PK=" + en.getPKVal() + "' target=_blank ><img src='"+basePath +"WF/Img/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
                     url = "&nbsp;&nbsp;<a href='"+ basePath + webPath + fileName +"'><img src='"+basePath +"WF/Img/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>&nbsp;&nbsp;";
                 }
                 
                 ui.append(BaseModel.AddTR());
                 ui.append(BaseModel.AddTD("nowrap=true class='FDesc' ", en.getEnMap().GetAttrByKey("MyFileName").getDesc()));
                 
                 TextBox file = ui.creatTextBox("file");
                 file.setTextMode(TextBoxMode.Files);
                 ui.append(BaseModel.AddTD(file));

                 ui.append(BaseModel.AddTDBegin("colspan=2"));
                 ui.append(url);
                 
                 if (!StringHelper.isNullOrEmpty(fileExt)){
                	 ImageButton btn1 = ui.creatImageButton("Btn_DelFile");
                     btn1.setImageUrl(basePath+"WF/Img/Btn/Delete.gif");
                     btn1.setText("移除");
                     btn1.addAttr("class", "Btn1");
                     btn1.setHref(" return confirm('此操作要执行移除附件，是否继续？');");
                     ui.append(btn1);
                 }
                 ui.append(BaseModel.AddTDEnd());

                 ui.append(BaseModel.AddTREnd());
        	 }
         }
         
         //#region 绑定属性控件
         AttrFiles fileAttrs = en.getEnMap().getHisAttrFiles();
         SysFileManagers sfs = new SysFileManagers(enName, en.getPKVal().toString());
         
         for (AttrFile attrF:fileAttrs){
        	 ui.append(BaseModel.AddTR());
        	 ui.append(BaseModel.AddTD("nowrap=true class='FDesc' ", attrF.FileName));
        	 
        	 TextBox file= ui.creatTextBox("F" + attrF.FileNo);
        	 file.setTextMode(TextBoxMode.Files);
        	 file.addAttr("width","100%");
             ui.append(BaseModel.AddTD(file));
             
             /* 判断是否有文件没有文件就移除它。*/
             SysFileManager sf  = (SysFileManager) ((sfs.GetEntityByKey(SysFileManagerAttr.AttrFileNo, attrF.FileNo) instanceof SysFileManager) ? sfs.GetEntityByKey(SysFileManagerAttr.AttrFileNo, attrF.FileNo) : null);
             if (null  == sf){
            	 ui.append(BaseModel.AddTD());
            	 ui.append(BaseModel.AddTD());
             }else{
            	 ui.append("<TD class=TD colspan=2>");
                 String lab = "&nbsp;<a href='" + cf.getFJWebPath() + "/" + sf.getOID() + "." + sf.getMyFileExt() + "' target=_blank ><img src='"+basePath +"WF/Img/FileType/" + sf.getMyFileExt() + ".gif' border=0 />" + sf.getMyFileName() + "." + sf.getMyFileExt() + "</a>";
                 ui.append(lab);
                
                 ImageButton btn_m = ui.creatImageButton("Btn_DelFile" + attrF.FileNo);;
                 btn_m.setImageUrl(basePath+"WF/Img/Btn/Delete.gif");
                 //btn_m.ImageUrl = "./Img/Btn/Del.gif";
                 btn_m.addAttr("class", "Btn1");
                 btn_m.addAttr("onclick"," return confirm('此操作要执行移除附件，是否继续？');");
                 ui.append(btn_m);
                 ui.append(BaseModel.AddTDEnd());
             }
         }
         //#endregion 绑定属性控件。
         
         if (en.getEnMap().getAttrs().Contains("MyFileNum")){
        	 ui.append(BaseModel.AddTR());
        	 ui.append(BaseModel.AddTD("nowrap=true class='FDesc' ", " "));
             if (en.getPKVal() == null || "".equals(en.getPKVal()) || "0".equals(en.getPKVal()))
                 ui.append("<TD  colspan=3 ><a href=\"javascript:alert('请在保存后在执行。');\" target=_self>附件批量上传(请在保存后在执行)</a></TD>");
             else
                 ui.append("<TD  colspan=3 ><a href=\"../FileManager.jsp?EnName=" + enName + "&PK=" + en.getPKVal() + "\" target=_self >附件批量上传&编辑</a></TD>");
             ui.append(BaseModel.AddTREnd());
         }
         ui.append(BaseModel.AddTableEnd());
         
         if (!isShowDtl)
             return;
         
         int num = map.getAttrsOfOneVSM().size() + map.getHisRefMethods().size() + map.getDtls().size();
         if (num > 0 && !en.IsExit(en.getPK(), en.getPKVal())){
        	 
        	 String endMsg = "";
             // 增加相关信息
             AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
             for (AttrOfOneVSM vsM:oneVsM)
                 endMsg += "[<a href=\"javascript:alert('请在保存后填写。');\" >" + vsM.getDesc() + "</a>]";

             RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
             for (RefMethod func : myreffuncs){
                 if (!func.Visable)
                     continue;
                 endMsg += "[<a href=\"javascript:alert('请在保存后执行。');\" >" + func.Title + "</a>]";
             }
             
             if (isShowDtl){
                 EnDtls enDtls = en.getEnMap().getDtls();
                 for (EnDtl enDtl:enDtls)
                     endMsg += "[<a href=\"javascript:alert('请在保存后填写。')\" >" + enDtl.getDesc() + "</a>]";
             }

             if (!endMsg.equals("")){
                 ui.append("<table border=0><TR><TD class=TD><font style='font-size:12px' >" + endMsg + "</font></TD></TR></table>");
             }
         }
     }
     
     private static void SetValByKey(String key, String val, UiFatory ui){
         TextBox tb = ui.creatTextBox("TB_" + key);
         tb.setTextMode(TextBoxMode.Hidden);
         tb.setText(val);
         tb.setVisible(false);
         ui.append(tb);
     }
     private static void AddContral(String desc, TextBox tb, int colSpanOfCtl, UiFatory ui){
         if (tb.getReadOnly()){
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class","TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         //if (tb.ReadOnly == false)
         //    desc += "<font color=red><b>*</b></font>";

         //  tb.Attributes["style"] = "width=100%;height=100%";
         if (tb.getTextMode() == TextBoxMode.MultiLine){
             AddContralDoc(desc, tb, colSpanOfCtl, ui);
             return;
         }

         ui.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");

         if (colSpanOfCtl < 3)
        	 ui.append("<td class='TD' nowrap colspan=" + colSpanOfCtl + " width='30%' >");
         else
        	 ui.append("<td class='TD' nowrap colspan=" + colSpanOfCtl + " width='80%' >");

         ui.append(tb);
         ui.append(BaseModel.AddTDEnd());
     }
     
     private static void AddContral(String desc, TextBox tb, String helpScript, int colspan, UiFatory ui){
         if (tb.getReadOnly()){
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class","TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         //if (tb.ReadOnly == false)
         //    desc += "<font color=red><b>*</b></font>";

         //  tb.Attributes["style"] = "width=100%;height=100%";
         if (tb.getTextMode() == TextBoxMode.MultiLine){
             AddContralDoc(desc, tb, ui);
             return;
         }

         //   tb.Attributes["Width"] = "30%";

         ui.append("<td class='FDesc' nowrap width=1% >" + desc + "</td>");

         if (colspan < 3){
             ui.append("<td class='TD' nowrap colspan=" + colspan + " width='30%' >" + helpScript);
         }else{
             ui.append("<td class='TD' nowrap colspan=" + colspan + " width='80%' >" + helpScript);
         }

         ui.append(tb);
         ui.append(BaseModel.AddTDEnd());
     }
     
     
     private static void AddContralDoc(String desc, TextBox tb, int colspanOfctl, UiFatory ui){
         ui.append("<td  colspan='" + colspanOfctl + "' width='100%'>" + desc + "<br>");
         tb.setCols(0);
         tb.setCssClass("TBDoc");
         ui.append(tb);
         ui.append("</td>");
     }
     
     private static void AddContralDoc(String desc, TextBox tb, UiFatory ui){
         ui.append("<td colspan='2' width='500px' >" + desc + "<br>");
         if (tb.getReadOnly())
             tb.addAttr("Class","TBReadonly");
         ui.append(tb);
         ui.append("</td>");
     }
     
     
     private static void AddContral(String desc, DDL ddl, boolean isRefBtn, int colspan, UiFatory ui){
         ui.append("<td class='FDesc' nowrap width=1% > " + desc + "</td><td  colspan=" + colspan + " nowrap>");
         ui.getTmpMap().put(ddl.getId(), ddl);
         ui.append(ddl);
         if (ddl.getEnabled()){
             if (ddl.getSelfBindKey().indexOf(".") == -1) {
                 ui.append(BaseModel.AddTDEnd());
             }else{
                 if (isRefBtn && ddl.Items.size() > 4){
                     String srip = "javascript:HalperOfDDL('" + basePath + "WF','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.getId() + "' ); ";
                     ui.append("<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.getId() + "' ></td>");
                 }else{
                     ui.append(BaseModel.AddTDEnd());
                 }
             }
         }else{
             ui.append(BaseModel.AddTDEnd());
         }
     }  
     
     private static void AddContral(String desc, CheckBox cb, int colspan, UiFatory ui){
         ui.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
         ui.append("<td class='TD' nowrap colspan='" + colspan + "'>");
         ui.append(cb);
         ui.append("</td>");
     }
     
     private static void AddContral(String desc, TextBox tb, UiFatory ui){
		if (tb.getReadOnly()) {
			if ("TBNum".equals(tb.attributes.get("Class")))
				tb.addAttr("Class", "TBNumReadonly");
			else
				tb.addAttr("Class", "TBReadonly");
		}

        tb.addAttr("style","width:80%");
		if (tb.getTextMode() == TextBoxMode.MultiLine) {
			AddContralDoc(desc, tb, ui);
			return;
		}

         ui.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
         ui.append("<td class='TD' nowrap width='30%'>");
         ui.append(tb);
         ui.append(BaseModel.AddTDEnd()); // ("</td>");
     }
     
     private static void AddContral(String desc, TextBox tb, String helpScript, UiFatory ui){
         if (tb.getReadOnly()){
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class", "TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         //if (tb.ReadOnly == false)
         //    desc += "<font color=red><b>*</b></font>";

         // tb.Attributes["style"] = "width=100%;height=100%";
         if (tb.getTextMode() == TextBoxMode.MultiLine){
             AddContralDoc(desc, tb, ui);
             return;
         }

         tb.addAttr("Width","100%");

         ui.append("<td class='FDesc' nowrap width=1% >" + desc + "</td>");
         ui.append("<td class='TD' nowrap  >" + helpScript);
         ui.append(tb);
         ui.append(BaseModel.AddTDEnd()); // ("</td>");
     }
     
     private static void AddContral(String desc, DDL ddl, boolean isRefBtn, UiFatory ui){
         ui.append("<td class='FDesc' nowrap width=1% > " + desc + "</td><td class=TD nowrap>");
         ui.getTmpMap().put(ddl.getId(), ddl);
         ui.append(ddl);
         if (ddl.getEnabled()){
             if (StringHelper.isEmpty(ddl.getSelfBindKey(), "").indexOf(".") == -1){
                 ui.append(BaseModel.AddTDEnd());
             }else{
                 if (isRefBtn && ddl.Items.size() > 15){
                     String srip = "javascript:HalperOfDDL('" + basePath + "WF','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.getId() + "' ); ";
                     ui.append("<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.getId() + "' ></td>");
                 }else{
                     ui.append(BaseModel.AddTDEnd());
                 }
             }
         }
         else{
             ui.append(BaseModel.AddTDEnd());
         }
     }
     
     private static void AddContral(String desc, CheckBox cb, UiFatory ui){
         ui.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
         ui.append("<td class='TD' nowrap >");
         ui.append(cb);
         ui.append("</td>");
     }
     
     private static void AddContral(UiFatory ui){
         ui.append("<td class='FDesc' nowrap width=1% ></td>");
         ui.append("<td class='TD' nowrap ></TD>");
     }
     
     private static String decimalFormat(Object object){
 		if(null == object || "".equals(object.toString())){
 			object = 0.00;
 		}
 		String str = new DecimalFormat("#0.00").format(Double.parseDouble(object.toString()));
 		return str;
     }
     
     
     public static void InitFuncEn(UAC uac, Entity en, ToolBar toolBar)
     {
         if (en.getEnMap().getEnType() == EnType.View)
             uac.Readonly();

         /*if (uac.IsInsert){
             if (en.getEnMap().getEnType() != EnType.Dtl)
             {
            	 toolBar.AddLinkBtn(NamesOfBtn.New, "新建(N)");
             }
         }*/

         if (uac.IsUpdate){
        	 toolBar.AddLinkBtn(NamesOfBtn.Save, "保存(S)");
        	 toolBar.AddLinkBtn(NamesOfBtn.SaveAndClose, "保存并关闭");
         }

         /*if (uac.IsInsert && uac.IsUpdate){
             if (en.getEnMap().getEnType() != EnType.Dtl){
            	 toolBar.AddLinkBtn(NamesOfBtn.SaveAndNew, "保存并新建(R)");
             }
         }*/

         String pkval = en.getPKVal().toString();

         if (uac.IsDelete && pkval != "0" && pkval.length() >= 1){
        	 toolBar.AddLinkBtn(NamesOfBtn.Delete, "删除(D)");
         }

         /*if (uac.IsAdjunct){
        	 toolBar.AddLinkBtn(NamesOfBtn.Adjunct);
             if (!en.getIsEmpty()){
                 int i = DBAccess.RunSQLReturnValInt("select COUNT(*) from Sys_FileManager WHERE RefTable='" + en.toString() + "' AND RefKey='" + en.getPKVal() + "'");
                 if (i != 0){
                     toolBar.GetLinkBtnByID(NamesOfBtn.Adjunct);
                 }
             }
         }*/
     }
     
     
     
}
