package cn.jflow.model.designer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.EditType;
import BP.En.EntitiesNoName;
import BP.En.FieldTypeS;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmShowWay;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.M2MType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapFrame;
import BP.Sys.MapFrames;
import BP.Sys.MapM2M;
import BP.Sys.MapM2Ms;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Entity.FrmWorkCheckSta;
import BP.WF.XML.MapDef.MapMenu;
import BP.WF.XML.MapDef.MapMenus;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class MapDefModel {
	public MapDefModel()
	{
		
	}
	private ArrayList<String> scripts = new ArrayList<String>();
	public ArrayList<String> getScripts() {
		return scripts;
	}
	public void setScripts(ArrayList<String> scripts) {
		this.scripts = scripts;
	}
	public static StringBuffer Left=new StringBuffer();
	public static StringBuffer Pub1=new StringBuffer();
	private String MyPK;
	private String FK_Flow;
	private boolean IsEditMapData;
	private String FK_MapData;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private String basePath;
	public MapDefModel(String basePath,HttpServletRequest req,HttpServletResponse res,String MyPK,String FK_Flow,boolean IsEditMapData)
	{
		this.MyPK=MyPK;
		this.FK_Flow=FK_Flow;
		this.IsEditMapData=IsEditMapData;
		this.FK_MapData=MyPK;
		this.req=req;
		this.res=res;
		this.basePath=basePath;
	}
     public void BindLeft()
     {
         MapMenus xmls = new MapMenus();
         xmls.RetrieveAll();

         //#region bindleft
         this.Left.append("<a href='http://ccflow.org' target=_blank ><img src='../../DataUser/ICON/" + SystemConfig.getCompanyID() + "/LogBiger.png' border=0/></a>");
         this.Left.append(BaseModel.AddHR());
         this.Left.append(BaseModel.AddUL());
         for (int i = 0; i < xmls.size(); i++) {
        	 MapMenu item=(MapMenu) xmls.get(i);
        	 this.Left.append(BaseModel.AddLi("<a href=\"" + item.getJS().replace("@MyPK", "'" + this.FK_MapData + "'").replace("@FK_Flow", "'" + this.FK_Flow + "'") + "\" ><img src='" + item.getImg() + "' width='16px' /><b>" + item.getName() + "</b></a><br><font color=green>" + item.getNote() + "</font>"));
		}
//         for (MapMenu item : xmls)
//         {
//             this.Left.AddLi("<a href=\"" + item.getJS().Replace("@MyPK", "'" + this.FK_MapData + "'").Replace("@FK_Flow", "'" + this.FK_Flow + "'") + "\" ><img src='" + item.Img + "' width='16px' /><b>" + item.Name + "</b></a><br><font color=green>" + item.Note + "</font>");
//         }
         this.Left.append(BaseModel.AddULEnd());
         //#endregion bindleft
     }
     public MapData md = null;
     public void Page_Load() throws IOException
     {
         String fk_node = req.getParameter("FK_Node");
         md = new MapData(this.FK_MapData);
         MapAttrs mattrs = new MapAttrs(md.getNo());
         int count = mattrs.size();
         this.BindLeft();

         //#region 计算出来列的宽度.
         int labCol = 50;
         int ctrlCol = 300;
         int width = (labCol + ctrlCol)*md.getTableCol()/2;
         //#endregion 计算出来列的宽度.

        // #region 生成表头.
         this.Pub1.append("\t\n<Table style='width:" + width + "px;' align=left>");
         this.Pub1.append(BaseModel.AddTR());
         this.Pub1.append(BaseModel.AddTD("colspan=" + md.getTableCol(), "<div style='float:left' ><img src='../../DataUser/ICON/Smaller.png' border=0/></div><h3><div style='float:center' >" + md.getName() + "</div></h3>"));
         this.Pub1.append(BaseModel.AddTREnd());

         this.Pub1.append(BaseModel.AddTR());
         boolean isLabel = true;
         for (int i = 0; i < md.getTableCol(); i++)
         {
             if (isLabel)
                 this.Pub1.append(BaseModel.AddTD("width='" + labCol + "px' align=center", i));
             else
                 this.Pub1.append(BaseModel.AddTD("width='" + ctrlCol + "px' align=center", Integer.toString(i)));
             isLabel = !isLabel;
         }
         this.Pub1.append(BaseModel.AddTREnd());
         //#endregion 生成表头.

         /*
          * 根据 GroupField 循环出现菜单。
          */
         for (int i = 0; i < this.get_gfs().size(); i++)  {
        	 GroupField gf=(GroupField) this.get_gfs().get(i);
             rowIdx = 0;
             String gfAttr = ""; 
             currGF = gf; 

            // #region 输出分组栏.
             this.Pub1.append(BaseModel.AddTR(gfAttr));
             if (this.get_gfs().size() == 1)
                 this.Pub1.append(BaseModel.AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top' align:left style='height: 24px;align:left' ", "<div style='text-align:left; float:left'>&nbsp;<a href=\"javascript:GroupField('" + this.FK_MapData + "','" + gf.getOID() + "')\" >" + gf.getLab() + "</a></div><div style='text-align:right; float:right'></div>"));
             else
                 this.Pub1.append(BaseModel.AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top'  style='height: 24px;align:left' ", "<div style='text-align:left; float:left'><img src='./Style/Min.gif' alert='Min' id='Img" + gf.getIdx() + "'  border=0 />&nbsp;<a href=\"javascript:GroupField('" + this.FK_MapData + "','" + gf.getOID() + "')\" >" + gf.getLab() + "</a></div><div style='text-align:right; float:right'> <a href=\"javascript:GFDoUp('" + gf.getOID() + "')\" ><img src='../Img/Btn/Up.gif' class='Arrow' border=0/></a> <a href=\"javascript:GFDoDown('" + gf.getOID() + "')\" ><img src='../Img/Btn/Down.gif' class='Arrow' border=0/></a></div>"));
             this.Pub1.append(BaseModel.AddTREnd());
//             #endregion 输出分组栏.

             this.idx = 0; // 设置字段的顺序号为0.1
             int colSpan = md.getTableCol();  // 定义colspan的宽度.
             for (int a = 0; a < mattrs.size(); a++)
             {
            	 MapAttr attr = (MapAttr)mattrs.get(a);
                 //MapAttr attr = mattrs[i] as MapAttr;

                 //#region 过滤不需要显示的字段.
                 if (attr.getGroupID() == 0)
                 {
                     attr.setGroupID((int)gf.getOID());
                     attr.Update();
                 }

                 if (attr.getGroupID() != gf.getOID())
                 {
                     if (gf.getIdx() == 0 && attr.getGroupID() == 0)
                     {
                     }
                     else
                         continue;
                 }
                 if (attr.getHisAttr().getIsRefAttr() || attr.getUIVisible() == false)
                     continue;

                 if (colSpan == 0)
                     this.InsertObjects(true);
                 //#endregion 过滤不需要显示的字段.

                 //#region 补充空白的列.
                 if (colSpan <= 0)
                 {
                     /*如果列已经用完.*/
                     this.Pub1.append(BaseModel.AddTREnd());
                     colSpan = md.getTableCol(); // 补充列.
                 }
                 //#endregion 补充空白的列.

                 //#region 处理两种状态下的大块文本.
                 if (attr.getIsBigDoc() && (attr.getColSpan() == md.getTableCol() || attr.getColSpan()==0 ))
                 {
                     if (colSpan == md.getTableCol())
                     {
                         /*说明刚刚加满列(处于已经换行的状态)*/
                         this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "'  " + gfAttr));
                     }
                     else
                     {
                         //补充上空格让它换行.
                         this.Pub1.append(BaseModel.AddTD("colspan=" + colSpan, ""));
                         this.Pub1.append(BaseModel.AddTREnd());
                         colSpan = md.getTableCol();
                     }

                     /*是大块文本，并且跨度在占领了整个剩余行单元格. */
                     this.Pub1.append("<TD colspan=" + md.getTableCol() + " width='100%' height='" + Float.toString(attr.getUIHeight()) + "px' >");
                     this.Pub1.append("<span style='float:left' height='" + Float.toString(attr.getUIHeight()) + "px' >" + this.GenerLab(attr, 0, count) + "</span>");
                     this.Pub1.append("<span style='float:right' height='" + Float.toString(attr.getUIHeight()) + "px'  >");

                     Label lab = new Label();
                     lab.setId("Lab" + attr.getKeyOfEn());
                     lab.setText("默认值");
                     this.Pub1.append(lab);
                     this.Pub1.append("</span><br>");

                     TextBox mytbLine = new TextBox();
                     mytbLine.setId("TB_" + attr.getKeyOfEn());
                     mytbLine.setTextMode(TextBoxMode.MultiLine);
                     mytbLine.addAttr("style", "width:100%;height:100%;padding: 0px;margin: 0px;");
                    // mytbLine.Attributes["style"] = "width:100%;height:100%;padding: 0px;margin: 0px;";
                     mytbLine.setEnabled(attr.getUIIsEnable());
                     this.Pub1.append(mytbLine);
                     mytbLine.addAttr("width", "100%");
                     //mytbLine.Attributes["width"] = "100%";
                     UiFatory ui=new UiFatory();
                     lab =ui.creatLabel("Lab"+attr.getKeyOfEn());// this.Pub1.GetLabelByID("Lab" + attr.getKeyOfEn());
                     String ctlID = mytbLine.getId();
                     lab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + basePath + "','" + md.getNo() + "','" + attr.getKeyOfEn() + "')\">默认值</a>");
                     this.Pub1.append(BaseModel.AddTDEnd());
                     this.Pub1.append(BaseModel.AddTREnd());
                     continue;
                 }

                 if (attr.getIsBigDoc())
                 {
                     /*如果是大文本, 并且没有整列显示它.*/
                     if (colSpan == md.getTableCol())
                     {
                         /*已经加满了*/
                         this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + rowIdx + "' " + gfAttr));
                         colSpan = colSpan - attr.getColSpan(); // 减去已经占用的col.
                     }

                     this.Pub1.append("<TD  colspan=" + attr.getColSpan() + " width='50%' height='" + Float.toString(attr.getUIHeight()) + "px' >");
                     this.Pub1.append("<span height='" + Float.toString(attr.getUIHeight()) + "px' style='float:left'>" + this.GenerLab(attr, 0, count) + "</span>");
                     this.Pub1.append("<span height='" + Float.toString(attr.getUIHeight()) + "px' style='float:right'>");
                     Label lab = new Label();
                     lab.setId("Lab" + attr.getKeyOfEn());
                     lab.setText("默认值");
                     this.Pub1.append(lab);
                     this.Pub1.append("</span>");

                     TextBox mytbLine = new TextBox();
                     mytbLine.setTextMode(TextBoxMode.MultiLine);
                     mytbLine.addAttr("class", "TBDoc");
                     //mytbLine.Attributes["class"] = "TBDoc"; // "width:100%;padding: 0px;margin: 0px;";
                     mytbLine.setId("TB_" + attr.getKeyOfEn());
                     mytbLine.setEnabled(attr.getUIIsEnable());
                     if (mytbLine.getEnabled() == false)
                    	 mytbLine.addAttr("class", "TBReadonly");
                         //mytbLine.Attributes["class"] = "TBReadonly";
                     mytbLine.addAttr("style", "width:100%;height:100%;padding: 0px;margin: 0px;");
                     //mytbLine.Attributes["style"] = "width:100%;height:100%;padding: 0px;margin: 0px;";
                     this.Pub1.append(mytbLine);
                     UiFatory ui=new UiFatory();
                     lab =ui.creatLabel("Lab" + attr.getKeyOfEn());// this.Pub1.GetLabelByID("Lab" + attr.KeyOfEn);
                     String ctlID = mytbLine.getId();
                     lab.setText("<a href=\"javascript:TBHelp('" + ctlID + "','" + req.getSession().getServletContext().getRealPath("/") + "','" + md.getNo() + "','" + attr.getKeyOfEn() + "')\">默认值</a>");
                     this.Pub1.append(BaseModel.AddTDEnd());
                     this.InsertObjects(false);
                     continue;
                 }
                // #endregion 处理两种状态下的大块文本.

                 /* 
                  * 以下就是一列标签一列控件的方式展现了.
                  */

               //  #region  首先判断当前剩余的单元格是否满足当前控件的需要。
                 if (attr.getColSpan() + 1 > md.getTableCol())
                     attr.setColSpan(md.getTableCol() - 1); //如果设置的

                 if (colSpan < attr.getColSpan() + 1 || colSpan == 1 || colSpan == 0)
                 {
                     /*如果剩余的列不能满足当前的单元格，就补充上它，让它换行.*/
                     this.Pub1.append(BaseModel.AddTD("colspan=" + colSpan, ""));
                     this.Pub1.append(BaseModel.AddTREnd());

                     colSpan = md.getTableCol();
                     this.Pub1.append(BaseModel.AddTR());
                 }
                // #endregion  首先判断当前剩余的单元格是否满足当前控件的需要。

                // #region 增加控件与描述.
                 // 增加上描述.
                 colSpan = colSpan - 1 - attr.getColSpan(); // 首先减去当前的占位.

                 TextBox tb = new TextBox();
                 tb.addAttr("width", "100%");
//                 tb.Attributes["width"] = "100%";
                 tb.setId("TB_" + attr.getKeyOfEn());
                 if(attr.getLGType()==BP.En.FieldTypeS.Normal)
                 {
                	 tb.setEnabled(attr.getUIIsEnable());
                     switch (attr.getMyDataType())
                     {
                         case BP.DA.DataType.AppString:
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                             tb.setShowType(TBType.TB);
                             tb.setText(attr.getDefVal());

                             if (attr.getIsSigan())
                                 this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), "<img src='/DataUser/Siganture/" + WebUser.getNo() + ".jpg' border=0 onerror=\"this.src='../../DataUser/Siganture/UnName.jpg'\"/>"));
                             else
                                 this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));

                             break;
                         case BP.DA.DataType.AppDate:
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                             TextBox tbD = new TextBox();
                             if (attr.getUIIsEnable())
                             {
                            	 tbD.addAttr("onfocus", "WdatePicker();");
                            	 tbD.addAttr("class", "TBcalendar");
                                 //tbD.Attributes["onfocus"] = "WdatePicker();";
                                 //tbD.Attributes["class"] = "TBcalendar";
                             }
                             else
                             {
                                 tbD.setEnabled(false);
                                 tbD.setReadOnly(true);
                                 tbD.addAttr("class", "TBcalendar");
                                 //tbD.Attributes["class"] = "TBcalendar";
                             }
                             this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tbD));
                             break;
                         case BP.DA.DataType.AppDateTime:
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                             TextBox tbDT = new TextBox();
                             tbDT.setText(attr.getDefVal());
                             if (attr.getUIIsEnable())
                             {
                            	 tbDT.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
                            	 tbDT.addAttr("class", "TBcalendar");
                                 //tbDT.Attributes["onfocus"] = "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});";
                                 //tbDT.Attributes["class"] = "TBcalendar";
                             }
                             else
                             {
                                 tbDT.setEnabled(false);
                                 tbDT.setReadOnly(true);
                                 tbDT.addAttr("class", "TBcalendar");
                                 //tbDT.Attributes["class"] = "TBcalendar";
                             }
                             this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tbDT));
                             break;
                         case BP.DA.DataType.AppBoolean:
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                             CheckBox cb = new CheckBox();
                             cb.setText(attr.getName());
                             cb.setChecked(attr.getDefValOfBool());
                             cb.setEnabled(attr.getUIIsEnable());
                             cb.setId("CB_" + attr.getKeyOfEn());
                             this.Pub1.append(BaseModel.AddTD("  colspan=" + attr.getColSpan(), cb));
                             break;
                         case BP.DA.DataType.AppDouble:
                         case BP.DA.DataType.AppFloat:
                         case BP.DA.DataType.AppInt:
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                             tb.setShowType(TBType.Num);
                             tb.setText(attr.getDefVal());
                             if (attr.getIsNull())
                                 tb.setText("");
                             this.Pub1.append(BaseModel.AddTD("  colspan=" + attr.getColSpan(), tb));
                             break;
                         case BP.DA.DataType.AppMoney:
                         case BP.DA.DataType.AppRate:
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                             tb.setShowType(TBType.Moneny);
                             tb.setText(attr.getDefVal());
                             if (attr.getIsNull())
                                 tb.setText("");

                             this.Pub1.append(BaseModel.AddTD(" colspan=" + attr.getColSpan(), tb));
                             break;
                         default:
                             break;
                     }
                     
                     tb.addAttr("width", "100%");
                     //tb.Attributes["width"] = "100%";
                     switch (attr.getMyDataType())
                     {
                         case BP.DA.DataType.AppString:
                         case BP.DA.DataType.AppDateTime:
                         case BP.DA.DataType.AppDate:
                             if (tb.getEnabled())
                            	 tb.addAttr("class", "TB");
                                 //tb.Attributes["class"] = "TB";
                             else
                            	 tb.addAttr("class", "TBReadonly");
                                 //tb.Attributes["class"] = "TBReadonly";
                             break;
                         default:
                             if (tb.getEnabled())
                            	 tb.addAttr("class", "TBNum");
                                 //tb.Attributes["class"] = "TBNum";
                             else
                            	 tb.addAttr("class", "TBNumReadonly");
                                 //tb.Attributes["class"] = "TBNumReadonly";
                             break;
                     }
                     if(attr.getLGType()==FieldTypeS.Enum)
                     {
                    	 if (attr.getUIContralType() == UIContralType.DDL)
                         {
                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));

                             DDL ddle = new DDL();
                             ddle.setId("DDL_" + attr.getKeyOfEn());
                             ddle.BindSysEnum(attr.getUIBindKey());
                             ddle.SetSelectItem(attr.getDefVal());
                             ddle.setEnabled(attr.getUIIsEnable());
                             this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), ddle));
                         }
                         else
                         {
                             this.Pub1.append("<TD class=TD colspan='" + attr.getColSpan() + "'>");
                             this.Pub1.append(this.GenerLab(attr, i, count));

                             SysEnums ses = new SysEnums(attr.getUIBindKey());
                             for (int j = 0; j < ses.size(); j++) {
                            	 SysEnum item=(SysEnum) ses.get(i);
                            	 RadioButton rb = new RadioButton();
                                 rb.setId("RB_" + attr.getKeyOfEn() + "_" + item.getIntKey());
                                 rb.setText(item.getLab());
                                 if (Integer.toString(item.getIntKey()) == attr.getDefVal())
                                     rb.setChecked(true);
                                 else
                                     rb.setChecked(false);
                                 //有错
                                 rb.setGroupName(item.getEnumKey() + attr.getKeyOfEn());
                                 this.Pub1.append(rb);
							}
//                             for (SysEnum item : ses)
//                             {
//                                 RadioButton rb = new RadioButton();
//                                 rb.setId("RB_" + attr.getKeyOfEn() + "_" + item.getIntKey());
//                                 rb.setText(item.getLab());
//                                 if (Integer.toString(item.getIntKey()) == attr.getDefVal())
//                                     rb.setChecked(true);
//                                 else
//                                     rb.setChecked(false);
//                                 //有错
//                                 rb.setGroupName(item.getEnumKey() + attr.getKeyOfEn());
//                                 this.Pub1.append(rb);
//                             }
                             this.Pub1.append(BaseModel.AddTDEnd());
                         }
                         break;
                     }
                     if(attr.getLGType()==FieldTypeS.FK)
                     {
                    	 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
                         DDL ddl1 = new DDL();
                         ddl1.setId("DDL_" + attr.getKeyOfEn());
                         try
                         {
                             EntitiesNoName ens = attr.getHisEntitiesNoName();
                             if (null == ens)
                             {
                            	 //ddlFK.Items.Add(new ListItem("数据错误"+attr.UIBindKey,"xx"));
                            	 Log.DebugWriteError("数据错误"+attr.getUIBindKey());
                             }else{
                            	 if (ens.size()==0)
                            	 {
                            		 ens.RetrieveAll();
                            	 }
                            	 ddl1.BindEntities(ens);
                            	 ddl1.SetSelectItem(attr.getDefVal());
                             }
                         }
                         catch(Exception e)
                         {
                        	 e.printStackTrace();
                         }
                         ddl1.setEnabled(attr.getUIIsEnable());
                         this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), ddl1));
                         break;
                     }
                 }
//                 switch (attr.getLGType())
//                 {
//                     case BP.En.FieldTypeS.Normal:
//                         tb.setEnabled(attr.getUIIsEnable());
//                         switch (attr.getMyDataType())
//                         {
//                             case BP.DA.DataType.AppString:
//                                 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                                 tb.setShowType(TBType.TB);
//                                 tb.setText(attr.getDefVal());
//
//                                 if (attr.getIsSigan())
//                                     this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), "<img src='/DataUser/Siganture/" + WebUser.getNo() + ".jpg' border=0 onerror=\"this.src='../../DataUser/Siganture/UnName.jpg'\"/>"));
//                                 else
//                                     this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tb));
//
//                                 break;
//                             case BP.DA.DataType.AppDate:
//                                 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                                 TextBox tbD = new TextBox();
//                                 if (attr.getUIIsEnable())
//                                 {
//                                	 tbD.addAttr("onfocus", "WdatePicker();");
//                                	 tbD.addAttr("class", "TBcalendar");
//                                     //tbD.Attributes["onfocus"] = "WdatePicker();";
//                                     //tbD.Attributes["class"] = "TBcalendar";
//                                 }
//                                 else
//                                 {
//                                     tbD.setEnabled(false);
//                                     tbD.setReadOnly(true);
//                                     tbD.addAttr("class", "TBcalendar");
//                                     //tbD.Attributes["class"] = "TBcalendar";
//                                 }
//                                 this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tbD));
//                                 break;
//                             case BP.DA.DataType.AppDateTime:
//                                 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                                 TextBox tbDT = new TextBox();
//                                 tbDT.setText(attr.getDefVal());
//                                 if (attr.getUIIsEnable())
//                                 {
//                                	 tbDT.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
//                                	 tbDT.addAttr("class", "TBcalendar");
//                                     //tbDT.Attributes["onfocus"] = "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});";
//                                     //tbDT.Attributes["class"] = "TBcalendar";
//                                 }
//                                 else
//                                 {
//                                     tbDT.setEnabled(false);
//                                     tbDT.setReadOnly(true);
//                                     tbDT.addAttr("class", "TBcalendar");
//                                     //tbDT.Attributes["class"] = "TBcalendar";
//                                 }
//                                 this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), tbDT));
//                                 break;
//                             case BP.DA.DataType.AppBoolean:
//                                 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                                 CheckBox cb = new CheckBox();
//                                 cb.setText(attr.getName());
//                                 cb.setChecked(attr.getDefValOfBool());
//                                 cb.setEnabled(attr.getUIIsEnable());
//                                 cb.setId("CB_" + attr.getKeyOfEn());
//                                 this.Pub1.append(BaseModel.AddTD("  colspan=" + attr.getColSpan(), cb));
//                                 break;
//                             case BP.DA.DataType.AppDouble:
//                             case BP.DA.DataType.AppFloat:
//                             case BP.DA.DataType.AppInt:
//                                 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                                 tb.setShowType(TBType.Num);
//                                 tb.setText(attr.getDefVal());
//                                 if (attr.getIsNull())
//                                     tb.setText("");
//                                 this.Pub1.append(BaseModel.AddTD("  colspan=" + attr.getColSpan(), tb));
//                                 break;
//                             case BP.DA.DataType.AppMoney:
//                             case BP.DA.DataType.AppRate:
//                                 this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                                 tb.setShowType(TBType.Moneny);
//                                 tb.setText(attr.getDefVal());
//                                 if (attr.getIsNull())
//                                     tb.setText("");
//
//                                 this.Pub1.append(BaseModel.AddTD(" colspan=" + attr.getColSpan(), tb));
//                                 break;
//                             default:
//                                 break;
//                         }
//                         
//                         tb.addAttr("width", "100%");
//                         //tb.Attributes["width"] = "100%";
//                         switch (attr.getMyDataType())
//                         {
//                             case BP.DA.DataType.AppString:
//                             case BP.DA.DataType.AppDateTime:
//                             case BP.DA.DataType.AppDate:
//                                 if (tb.getEnabled())
//                                	 tb.addAttr("class", "TB");
//                                     //tb.Attributes["class"] = "TB";
//                                 else
//                                	 tb.addAttr("class", "TBReadonly");
//                                     //tb.Attributes["class"] = "TBReadonly";
//                                 break;
//                             default:
//                                 if (tb.getEnabled())
//                                	 tb.addAttr("class", "TBNum");
//                                     //tb.Attributes["class"] = "TBNum";
//                                 else
//                                	 tb.addAttr("class", "TBNumReadonly");
//                                     //tb.Attributes["class"] = "TBNumReadonly";
//                                 break;
//                         }
//                         break;
//                     case FieldTypeS.Enum:
//                         if (attr.getUIContralType() == UIContralType.DDL)
//                         {
//                             this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//
//                             DDL ddle = new DDL();
//                             ddle.setId("DDL_" + attr.getKeyOfEn());
//                             ddle.BindSysEnum(attr.getUIBindKey());
//                             ddle.SetSelectItem(attr.getDefVal());
//                             ddle.setEnabled(attr.getUIIsEnable());
//                             this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), ddle));
//                         }
//                         else
//                         {
//                             this.Pub1.append("<TD class=TD colspan='" + attr.getColSpan() + "'>");
//                             this.Pub1.append(this.GenerLab(attr, i, count));
//
//                             SysEnums ses = new SysEnums(attr.getUIBindKey());
//                             for (SysEnum item : ses)
//                             {
//                                 RadioButton rb = new RadioButton();
//                                 rb.setId("RB_" + attr.getKeyOfEn() + "_" + item.getIntKey());
//                                 rb.setText(item.getLab());
//                                 if (Integer.toString(item.getIntKey()) == attr.getDefVal())
//                                     rb.setChecked(true);
//                                 else
//                                     rb.setChecked(false);
//                                 //有错
//                                 rb.setGroupName(item.getEnumKey() + attr.getKeyOfEn());
//                                 this.Pub1.append(rb);
//                             }
//                             this.Pub1.append(BaseModel.AddTDEnd());
//                         }
//                         break;
//                     case FieldTypeS.FK:
//                         this.Pub1.append(BaseModel.AddTDDesc(this.GenerLab(attr, i, count)));
//                         DDL ddl1 = new DDL();
//                         ddl1.setId("DDL_" + attr.getKeyOfEn());
//                         try
//                         {
//                             EntitiesNoName ens = attr.getHisEntitiesNoName();
//                             ens.RetrieveAll();
//                             ddl1.BindEntities(ens);
//                             ddl1.SetSelectItem(attr.getDefVal());
//                         }
//                         catch(Exception e)
//                         {
//                        	 e.printStackTrace();
//                         }
//                         ddl1.setEnabled(attr.getUIIsEnable());
//                         this.Pub1.append(BaseModel.AddTD("colspan=" + attr.getColSpan(), ddl1));
//                         break;
//                     default:
//                         break;
//                 }
                 //#endregion 增加控件.

             } // end循环分组.

             if (colSpan == 0)
             {
                 colSpan = md.getTableCol();
                 this.Pub1.append(BaseModel.AddTREnd());
                 this.InsertObjects(false);
             }

             // 在分组后处理它, 首先判断当前剩余的单元格是否满足当前控件的需要。
             if (colSpan!=md.getTableCol() )
             {
                 /*如果剩余的列不能满足当前的单元格，就补充上它，让它换行.*/
                 this.Pub1.append(BaseModel.AddTD("colspan=" + colSpan, ""));
                 this.Pub1.append(BaseModel.AddTREnd());
                 this.InsertObjects(false);
                 colSpan = md.getTableCol();
             }
             if (colSpan == 0)
             {
                 colSpan = md.getTableCol();
                 this.Pub1.append(BaseModel.AddTREnd());
                 this.InsertObjects(false);
             }

             // 在分组后处理它, 首先判断当前剩余的单元格是否满足当前控件的需要。
             if (colSpan!=md.getTableCol() )
             {
                 /*如果剩余的列不能满足当前的单元格，就补充上它，让它换行.*/
                 this.Pub1.append(BaseModel.AddTD("colspan=" + colSpan, ""));
                 this.Pub1.append(BaseModel.AddTREnd());
                 this.InsertObjects(false);
                 colSpan = md.getTableCol();
             }
         } // end循环分组.

         //#region 输出审核组件.
         FrmWorkCheck fwc = new FrmWorkCheck(md.getNo());
         if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable)
         {
             this.Pub1.append(BaseModel.AddTR());
             this.Pub1.append(BaseModel.AddTD("colspan=" + md.getTableCol() + " class=GroupField valign='top' align:left style='height: 24px;align:left' ", "<div style='text-align:left; float:left'>&nbsp;审核信息</div><div style='text-align:right; float:right'></div>"));
             this.Pub1.append(BaseModel.AddTREnd());

             this.Pub1.append(BaseModel.AddTR());
             this.Pub1.append("<TD valign=top colspan=" + md.getTableCol() + " ID='TDFWC' height='150px' > ");
             String src = "../WorkOpt/WorkCheck.jsp?s=2";
             String paras =req.getQueryString();// this.RequestParas;
             if (!paras.contains("OID="))
                 paras += "&OID=123";
             src += "&r=q" + paras;
             this.Pub1.append("<iframe ID='FWC' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "'  width='100%' height='150px' scrolling=auto  /></iframe>");
             this.Pub1.append(BaseModel.AddTDEnd());
             this.Pub1.append(BaseModel.AddTREnd());
         }
         //#endregion 

         this.Pub1.append(BaseModel.AddTableEnd());


        // #region 处理异常情况。
         for (int i = 0; i < this.get_dtls().size(); i++) {
        	 MapDtl dtl=(MapDtl) this.get_dtls().get(i);
        	 if (dtl.IsUse == false)
             {
                 dtl.setRowIdx(0);
                 dtl.setGroupID(0);
                 dtl.Update();
                 //    this.Response.Redirect(this.Request.RawUrl, true);
             }
		}
//         for (MapDtl dtl : _dtls)
//         {
//             if (dtl.IsUse == false)
//             {
//                 dtl.setRowIdx(0);
//                 dtl.setGroupID(0);
//                 dtl.Update();
//                 //    this.Response.Redirect(this.Request.RawUrl, true);
//             }
//         }
         //#endregion 处理异常情况。

         //#region 处理扩展信息。
         MapExts mes = new MapExts(this.FK_MapData);
         if (mes.size() != 0)
         {
        	 String appPath=req.getSession().getServletContext().getRealPath("/");
            // String appPath = this.Request.ApplicationPath;

        	 scripts.add("Scripts/jquery-1.4.1.min.js");
        	 scripts.add("CCForm/MapExt.js");
        	 scripts.add("DataUser/JSLibData/" + this.FK_MapData + ".js' ></script>");

//             this.Page.RegisterClientScriptBlock("s",
//           "<script language='JavaScript' src='../Scripts/jquery-1.4.1.min.js' ></script>");

//             this.Page.RegisterClientScriptBlock("b",
//           "<script language='JavaScript' src='../CCForm/MapExt.js' defer='defer' type='text/javascript' ></script>");

//             this.Page.RegisterClientScriptBlock("dC",
//           "<script language='JavaScript' src='" + appPath + "DataUser/JSLibData/" + this.FK_MapData + ".js' ></script>");

             this.Pub1.append("<div id='divinfo' style='width: 155px; position: absolute; color: Lime; display: none;cursor: pointer;align:left'></div>");
         }
         String jsStr = "";
         for (int i = 0; i < mes.size(); i++) {
			MapExt me=(MapExt) mes.get(i);
			UiFatory ui=new UiFatory();
			if(me.getExtType()!=null)
			{
				if(me.getExtType()==MapExtXmlList.DDLFullCtrl)
				{
					DDL ddlOper =ui.creatDDL("DDL_" + me.getAttrOfOper());// this.Pub1.GetDDLByID("DDL_" + me.AttrOfOper);
                    if (ddlOper == null)
                        continue;
                    ddlOper.addAttr("onchange", "DDLFullCtrl(this.value,\'" + ddlOper.getId() + "\', \'" + me.getMyPK() + "\')");//Attributes["onchange"] = "DDLFullCtrl(this.value,\'" + ddlOper.ClientID + "\', \'" + me.MyPK + "\')";
				}
				if(me.getExtType()==MapExtXmlList.ActiveDDL)
				{
					 DDL ddlPerant =ui.creatDDL("DDL_" + me.getAttrOfOper());// this.Pub1.GetDDLByID("DDL_" + me.AttrOfOper);
	                    DDL ddlChild = ui.creatDDL("DDL_" + me.getAttrsOfActive());
	                    if (ddlChild == null || ddlPerant == null)
	                    {
	                        me.Delete();
	                        continue;
	                    }

	                    ddlPerant.addAttr("onchange", "DDLAnsc(this.value,\'" + ddlChild.getId() + "\', \'" + me.getMyPK() + "\')");//Attributes["onchange"] = "DDLAnsc(this.value,\'" + ddlChild.ClientID + "\', \'" + me.MyPK + "\')";
	                    // ddlChild.Attributes["onchange"] = "ddlCity_onchange(this.value,'" + me.MyPK + "')";
				}
				if(me.getExtType()==MapExtXmlList.TBFullCtrl)
				{
					TextBox tbAuto = ui.creatTextBox("TB_" + me.getAttrOfOper());//this.Pub1.GetTBByID("TB_" + me.AttrOfOper);
                    if (tbAuto == null)
                    {
                        me.Delete();
                        continue;
                    }
                    tbAuto.addAttr("onkeyup", "DoAnscToFillDiv(this,this.value,\'" + tbAuto.getId() + "\', \'" + me.getMyPK() + "\');");
                    tbAuto.addAttr("AUTOCOMPLETE", "OFF");
				}
				if(me.getExtType()==MapExtXmlList.InputCheck)
				{
					TextBox tbJS =ui.creatTextBox("TB_" + me.getAttrOfOper());
                    if (tbJS != null)
                        tbJS.addAttr(me.getTag2(), me.getTag1() + "(this);");//Attributes[me.Tag2] += me.Tag1 + "(this);";
                    else
                        me.Delete();
				}
				if(me.getExtType()==MapExtXmlList.PopVal)
				{
					 TextBox  tbPop =ui.creatTextBox("TB_" + me.getAttrOfOper());// this.Pub1.GetTBByID("TB_" + me.AttrOfOper);
				}
				if(me.getExtType()==MapExtXmlList.AutoFull)
				{
					String  js = "\t\n <script type='text/javascript' >";
                    TextBox tb =ui.creatTextBox("TB_" + me.getAttrOfOper());// this.Pub1.GetTBByID("TB_" + me.AttrOfOper);
                    if (tb == null)
                        continue;

                    String left = "\n  document.forms[0]." + tb.getId() + ".value = ";
                    String right = me.getDoc();
                    for (int j = 0; j < mattrs.size(); j++) {
                    	MapAttr mattr=(MapAttr) mattrs.get(i);
                    	if (mattr.getIsNum() == false)
                            continue;

                        if (me.getDoc().contains("@" + mattr.getKeyOfEn())
                            || me.getDoc().contains("@" + mattr.getName()))
                        {
                        }
                        else
                        {
                            continue;
                        }

                        String tbID = "TB_" + mattr.getKeyOfEn();
                        TextBox mytb =ui.creatTextBox(tbID);// this.Pub1.GetTBByID(tbID);
                        if (mytb == null)
                            continue;

                        ui.creatTextBox(tbID).addAttr("onkeyup", "javascript:Auto" + me.getAttrOfOper() + "();");
                        //this.Pub1.GetTBByID(tbID).Attributes["onkeyup"] = "javascript:Auto" + me.AttrOfOper + "();";
                        right = right.replace("@" + mattr.getName(), " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
                        right = right.replace("@" + mattr.getKeyOfEn(), " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
					}
                    js += "\t\n function Auto" + me.getAttrOfOper() + "() { ";
                    js += left + right + ";";
                    js += " \t\n  document.forms[0]." + tb.getId() + ".value= VirtyMoney(document.forms[0]." + tb.getId() + ".value ) ;";
                    js += "\t\n } ";
                    js += "\t\n</script>";
                    this.Pub1.append(js);
				}
					
			}
//			switch (me.getExtType())
//            {
//                case MapExtXmlList.DDLFullCtrl: // 自动填充.
//               	 DDL ddlOper =ui.creatDDL("DDL_" + me.getAttrOfOper());// this.Pub1.GetDDLByID("DDL_" + me.AttrOfOper);
//                    if (ddlOper == null)
//                        continue;
//                    ddlOper.addAttr("onchange", "DDLFullCtrl(this.value,\'" + ddlOper.getId() + "\', \'" + me.getMyPK() + "\')");//Attributes["onchange"] = "DDLFullCtrl(this.value,\'" + ddlOper.ClientID + "\', \'" + me.MyPK + "\')";
//                    break;
//                case MapExtXmlList.ActiveDDL:
//                    DDL ddlPerant =ui.creatDDL("DDL_" + me.getAttrOfOper());// this.Pub1.GetDDLByID("DDL_" + me.AttrOfOper);
//                    DDL ddlChild = ui.creatDDL("DDL_" + me.getAttrsOfActive());
//                    if (ddlChild == null || ddlPerant == null)
//                    {
//                        me.Delete();
//                        continue;
//                    }
//
//                    ddlPerant.addAttr("onchange", "DDLAnsc(this.value,\'" + ddlChild.getId() + "\', \'" + me.getMyPK() + "\')");//Attributes["onchange"] = "DDLAnsc(this.value,\'" + ddlChild.ClientID + "\', \'" + me.MyPK + "\')";
//                    // ddlChild.Attributes["onchange"] = "ddlCity_onchange(this.value,'" + me.MyPK + "')";
//                    break;
//                case MapExtXmlList.TBFullCtrl: // 自动填充.
//                    TextBox tbAuto = ui.creatTextBox("TB_" + me.getAttrOfOper());//this.Pub1.GetTBByID("TB_" + me.AttrOfOper);
//                    if (tbAuto == null)
//                    {
//                        me.Delete();
//                        continue;
//                    }
//                    tbAuto.addAttr("onkeyup", "DoAnscToFillDiv(this,this.value,\'" + tbAuto.getId() + "\', \'" + me.getMyPK() + "\');");
//                    tbAuto.addAttr("AUTOCOMPLETE", "OFF");
//                    break;
//                case MapExtXmlList.InputCheck: /*js 检查 */
//                    TextBox tbJS =ui.creatTextBox("TB_" + me.getAttrOfOper());
//                    if (tbJS != null)
//                        tbJS.addAttr(me.getTag2(), me.getTag1() + "(this);");//Attributes[me.Tag2] += me.Tag1 + "(this);";
//                    else
//                        me.Delete();
//                    break;
//                case MapExtXmlList.PopVal: //弹出窗.
//                    TextBox  tbPop =ui.creatTextBox("TB_" + me.getAttrOfOper());// this.Pub1.GetTBByID("TB_" + me.AttrOfOper);
//                    //tb.Attributes["ondblclick"] = "ReturnVal(this,'" + me.Doc + "','sd');";
//                    break;
//                case MapExtXmlList.AutoFull: //自动填充.
//                    String  js = "\t\n <script type='text/javascript' >";
//                    TextBox tb =ui.creatTextBox("TB_" + me.getAttrOfOper());// this.Pub1.GetTBByID("TB_" + me.AttrOfOper);
//                    if (tb == null)
//                        continue;
//
//                    String left = "\n  document.forms[0]." + tb.getId() + ".value = ";
//                    String right = me.getDoc();
//                    for (int j = 0; j < mattrs.size(); j++) {
//                    	MapAttr mattr=(MapAttr) mattrs.get(i);
//                    	if (mattr.getIsNum() == false)
//                            continue;
//
//                        if (me.getDoc().contains("@" + mattr.getKeyOfEn())
//                            || me.getDoc().contains("@" + mattr.getName()))
//                        {
//                        }
//                        else
//                        {
//                            continue;
//                        }
//
//                        String tbID = "TB_" + mattr.getKeyOfEn();
//                        TextBox mytb =ui.creatTextBox(tbID);// this.Pub1.GetTBByID(tbID);
//                        if (mytb == null)
//                            continue;
//
//                        ui.creatTextBox(tbID).addAttr("onkeyup", "javascript:Auto" + me.getAttrOfOper() + "();");
//                        //this.Pub1.GetTBByID(tbID).Attributes["onkeyup"] = "javascript:Auto" + me.AttrOfOper + "();";
//                        right = right.replace("@" + mattr.getName(), " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
//                        right = right.replace("@" + mattr.getKeyOfEn(), " parseFloat( document.forms[0]." + mytb.getId() + ".value.replace( ',' ,  '' ) ) ");
//					}
//                    js += "\t\n function Auto" + me.getAttrOfOper() + "() { ";
//                    js += left + right + ";";
//                    js += " \t\n  document.forms[0]." + tb.getId() + ".value= VirtyMoney(document.forms[0]." + tb.getId() + ".value ) ;";
//                    js += "\t\n } ";
//                    js += "\t\n</script>";
//                    this.Pub1.append(js);
//                    break;
//                default:
//                    break;
//            }
		}
       //  #endregion 处理扩展信息。

        // #region 处理输入最小，最大验证.
         for (int j = 0; j < mattrs.size(); j++) {
        	 MapAttr attr=(MapAttr) mattrs.get(j);
        	 if (attr.getMyDataType() != DataType.AppString || attr.getMinLen() == 0)
                 continue;

             if (attr.getUIIsEnable() == false || attr.getUIVisible() == false)
                 continue;
             UiFatory ui=new UiFatory();
             ui.creatTextBox("TB_" + attr.getKeyOfEn()).addAttr("onblur","checkLength(this,'" + attr.getMinLen() + "','" + attr.getMaxLen() + "')");
		}
//         for (MapAttr attr : mattrs)
//         {
//             
//             //this.Pub1.GetTextBoxByID("TB_" + attr.KeyOfEn).Attributes["onblur"] = "checkLength(this,'" + attr.MinLen + "','" + attr.MaxLen + "')";
//         }
        // #endregion 处理输入最小，最大验证.

         //#region 处理iFrom 的自适应的问题。
         String myjs = "\t\n<script type='text/javascript' >";
         for (int i = 0; i < this.get_dtls().size(); i++) {
        	 MapDtl dtl=(MapDtl) this.get_dtls().get(i);
        	 myjs += "\t\n window.setInterval(\"ReinitIframe('F" + dtl.getNo() + "','TD" + dtl.getNo() + "')\", 200);";
		}
         for (int i = 0; i < this.get_dot2dots().size(); i++) {
        	 MapM2M M2M=(MapM2M) this.get_dot2dots().get(i);
        	 if (M2M.getShowWay() == FrmShowWay.FrmAutoSize)
                 myjs += "\t\n window.setInterval(\"ReinitIframe('F" + M2M.getNoOfObj() + "','TD" + M2M.getNoOfObj() + "')\", 200);";
		}
//         for (MapM2M M2M : _dot2dots)
//         {
//             if (M2M.getShowWay() == FrmShowWay.FrmAutoSize)
//                 myjs += "\t\n window.setInterval(\"ReinitIframe('F" + M2M.getNoOfObj() + "','TD" + M2M.getNoOfObj() + "')\", 200);";
//         }
         for (int i = 0; i < _aths.size(); i++) {
        	 FrmAttachment ath=(FrmAttachment) _aths.get(i);
        	 if (ath.getIsAutoSize())
                 myjs += "\t\n window.setInterval(\"ReinitIframe('F" + ath.getMyPK() + "','TD" + ath.getMyPK() + "')\", 200);";
		}
//         for (FrmAttachment ath : aths)
//         {
//             if (ath.getIsAutoSize())
//                 myjs += "\t\n window.setInterval(\"ReinitIframe('F" + ath.getMyPK() + "','TD" + ath.getMyPK() + "')\", 200);";
//         }
         for (int i = 0; i < this.get_frams().size(); i++) {
        	 MapFrame fr=(MapFrame) this.get_frams().get(i);
        	 myjs += "\t\n window.setInterval(\"ReinitIframe('F" + fr.getMyPK() + "','TD" + fr.getMyPK() + "')\", 200);";
		}
//         for (MapFrame fr : _frams)
//         {
//             myjs += "\t\n window.setInterval(\"ReinitIframe('F" + fr.getMyPK() + "','TD" + fr.getMyPK() + "')\", 200);";
//         }

         myjs += "\t\n</script>";
         this.Pub1.append(myjs);
         //#endregion 处理iFrom 的自适应的问题。

         //#region 处理隐藏字段。
         DataTable dt = DBAccess.RunSQLReturnTable("SELECT * FROM Sys_MapAttr WHERE FK_MapData='" + this.FK_MapData + "' AND GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.FK_MapData + "')");
         if (dt.Rows.size() != 0)
         {
             int gfid = ((GroupField)this.get_gfs().get(0)).GetValIntByKey("OID");
             for (DataRow dr : dt.Rows)
                 DBAccess.RunSQL("UPDATE Sys_MapAttr SET GroupID=" + gfid + " WHERE MyPK='" + dr.getValue("MyPK") + "'");
             res.sendRedirect(req.getRequestURI());	 
             //this.Response.Redirect(this.Request.RawUrl);
         }
        // #endregion 处理隐藏字段。
     }

     public void InsertObjects(boolean isJudgeRowIdx)
     {
        // #region 增加从表
    	 for (int i = 0; i < this.get_dtls().size(); i++) {
			MapDtl dtl=(MapDtl) this.get_dtls().get(i);
			if (dtl.IsUse)
                continue;

            if (isJudgeRowIdx)
            {
                if (dtl.getRowIdx() != rowIdx)
                    continue;
            }

            if (dtl.getGroupID() == 0 && rowIdx == 0)
            {
                dtl.setGroupID((int)currGF.getOID());
                dtl.setRowIdx(0);
                dtl.Update();
            }
            else if (dtl.getGroupID() == currGF.getOID())
            {

            }
            else
            {
                continue;
            }

            dtl.IsUse = true;
            int myidx = rowIdx + 10;
            this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
            this.Pub1.append("<TD colspan="+md.getTableCol()+" class=TRSum  ><div style='text-align:left; float:left'><a href=\"javascript:EditDtl('" + this.FK_MapData + "','" + dtl.getNo() + "')\" >" + dtl.getName() + "</a></div><div style='text-align:right; float:right'><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.AddF('" + dtl.getNo() + "');\"><img src='../Img/Btn/New.gif' border=0/>插入列</a><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.AddFGroup('" + dtl.getNo() + "');\"><img src='../Img/Btn/New.gif' border=0/>插入列组</a><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.CopyF('" + dtl.getNo() + "');\"><img src='../Img/Btn/Copy.gif' border=0/>复制列</a><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.HidAttr('" + dtl.getNo() + "');\"><img src='../Img/Btn/Copy.gif' border=0/>隐藏列</a><a href=\"javascript:document.getElementById('F" + dtl.getNo() + "').contentWindow.DtlMTR('" + dtl.getNo() + "');\"><img src='../Img/Btn/Copy.gif' border=0/>多表头</a> <a href='Action.jsp?FK_MapData=" + dtl.getNo() + "' >从表事件</a> <a href=\"javascript:DtlDoUp('" + dtl.getNo() + "')\" ><img src='../Img/Btn/Up.gif' border=0/></a> <a href=\"javascript:DtlDoDown('" + dtl.getNo() + "')\" ><img src='../Img/Btn/Down.gif' border=0/></a></div></td>");
            this.Pub1.append(BaseModel.AddTREnd());

            myidx++;
            this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
            this.Pub1.append("<TD colspan=" + md.getTableCol() + " ID='TD" + dtl.getNo() + "' height='50px' width='"+this.md.getTableWidth()+"' > ");
            String src = "MapDtlDe.jsp?DoType=Edit&FK_MapData=" + this.FK_MapData + "&FK_MapDtl=" + dtl.getNo();
            this.Pub1.append("<iframe ID='F" + dtl.getNo() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "'  width='" + this.md.getTableWidth() + "' height='10px' scrolling=no  /></iframe>");
            this.Pub1.append(BaseModel.AddTDEnd());
            this.Pub1.append(BaseModel.AddTREnd());
		}
         //#endregion 增加从表

        // #region 增加附件
    	 for (int i = 0; i < this.get_aths().size(); i++) {
			FrmAttachment dtl=(FrmAttachment) _aths.get(i);
			if (dtl.IsUse)
                continue;

            if (isJudgeRowIdx)
            {
                if (dtl.getRowIdx() != rowIdx)
                    continue;
            }

            if (dtl.getGroupID() == 0 && rowIdx == 0)
            {
                dtl.setGroupID((int)currGF.getOID());
                dtl.setRowIdx(0);
                dtl.Update();
            }
            else if (dtl.getGroupID() == currGF.getOID())
            {
            }
            else
            {
                continue;
            }

            dtl.IsUse = true;
            int myidx = rowIdx + 10;
            this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
            this.Pub1.append("<TD colspan=" + md.getTableCol() + " class=TRSum  ><div style='text-align:left; float:left'><a href=\"javascript:EditAth('" + this.FK_MapData + "','" + dtl.getNoOfObj() + "')\" >" + dtl.getName() + "</a></div><div style='text-align:right; float:right'><a href=\"javascript:AthDoUp('" + dtl.getMyPK() + "')\" ><img src='../Img/Btn/Up.gif' border=0/></a> <a href=\"javascript:AthDoDown('" + dtl.getMyPK() + "')\" ><img src='../Img/Btn/Down.gif' border=0/></a></div></td>");
            this.Pub1.append(BaseModel.AddTREnd());

            myidx++;
            this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
            this.Pub1.append("<TD colspan=" + md.getTableCol() + " ID='TD" + dtl.getMyPK() + "' height='50px' width='1000px'>");

            String src = "../CCForm/AttachmentUpload.jsp?PKVal=0&Ath=" + dtl.getNoOfObj() + "&FK_MapData=" + this.FK_MapData + "&FK_FrmAttachment=" + dtl.getMyPK();
            if (dtl.getIsAutoSize())
                this.Pub1.append("<iframe ID='F" + dtl.getMyPK() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='10px' scrolling=no  /></iframe>");
            else
                this.Pub1.append("<iframe ID='F" + dtl.getMyPK() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='" + dtl.getW() + "' height='" + dtl.getH() + "' scrolling=auto  /></iframe>");

            this.Pub1.append(BaseModel.AddTDEnd());
            this.Pub1.append(BaseModel.AddTREnd());
		}
        // #endregion 增加附件

         //#region 增加M2M
    	 for (int i = 0; i < this.get_dot2dots().size(); i++) {
    		 MapM2M m2m=(MapM2M) this.get_dot2dots().get(i);
    		 if (m2m.IsUse)
                 continue;

             if (isJudgeRowIdx)
             {
                 if (m2m.getRowIdx() != rowIdx)
                     continue;
             }

             if (m2m.getGroupID() == 0 && rowIdx == 0)
             {
                 m2m.setGroupID((int)currGF.getOID());
                 m2m.setRowIdx(0);
                 m2m.Update();
             }
             else if (m2m.getGroupID() == currGF.getOID())
             {
             }
             else
             {
                 continue;
             }

             m2m.IsUse = true;
             int myidx = rowIdx + 10;
             this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
             this.Pub1.append("<TD colspan=4 class=TRSum  ><div style='text-align:left; float:left'><a href=\"javascript:EditM2M('" + this.FK_MapData + "','" + m2m.getNoOfObj() + "')\" >" + m2m.getName() + "</a></div><div style='text-align:right; float:right'><a href=\"javascript:M2MDoUp('" + m2m.getMyPK() + "')\" ><img src='../Img/Btn/Up.gif' border=0/></a> <a href=\"javascript:M2MDoDown('" + m2m.getMyPK() + "')\" ><img src='../Img/Btn/Down.gif' border=0/></a></div></td>");
             this.Pub1.append(BaseModel.AddTREnd());

             myidx++;
             String src = "";
             if (m2m.getHisM2MType() == M2MType.M2M)
                 src = "../CCForm/M2M.jsp?FK_MapData=" + this.FK_MapData + "&NoOfObj=" + m2m.getNoOfObj() + "&OID=0";
             else
                 src = "../CCForm/M2MM.jsp?FK_MapData=" + this.FK_MapData + "&NoOfObj=" + m2m.getNoOfObj() + "&OID=0";

             switch (m2m.getShowWay())
             {
                 case FrmAutoSize:
                     //this.Pub1.AddTR(" ID='" + currGF.Idx + "_" + myidx + "'");
                     //this.Pub1.Add("<TD colspan=4 ID='TD" + m2m.NoOfObj + "' width='100%'>");
                     //this.Pub1.Add("<iframe ID='F" + m2m.NoOfObj + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%'   scrolling=no  /></iframe>");
                     //this.Pub1.AddTDEnd();
                     //this.Pub1.AddTREnd();

                     myidx++;
                     this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
                     this.Pub1.append("<TD colspan=" + md.getTableCol() + " ID='TD" + m2m.getNoOfObj() + "' height='50px' width='1000px'>");
                     this.Pub1.append("<iframe ID='F" + m2m.getNoOfObj() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='10px' scrolling=no  /></iframe>");
                     this.Pub1.append(BaseModel.AddTDEnd());
                     this.Pub1.append(BaseModel.AddTREnd());
                     break;
                 case FrmSpecSize:
                     this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "'"));
                     this.Pub1.append("<TD colspan=" + md.getTableCol() + "ID='TD" + m2m.getNoOfObj() + "' height='" + m2m.getH() + "' width='" + m2m.getW() + "'  >");
                     this.Pub1.append("<iframe ID='F" + m2m.getNoOfObj() + "' src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + m2m.getW() + "' height='" + m2m.getH() + "' scrolling=auto /></iframe>");
                     this.Pub1.append(BaseModel.AddTDEnd());
                     this.Pub1.append(BaseModel.AddTREnd());
                     break;
                 case Hidden:
                     break;
                 case WinOpen:
                     this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "'"));
                     this.Pub1.append("<TD colspan=" + md.getTableCol() + " ID='TD" + m2m.getNoOfObj() + "' height='20px' width='100%' >");
                     this.Pub1.append("<a href=\"javascript:WinOpen('" + src + "&IsOpen=1','" + m2m.getW() + "','" + m2m.getH() + "');\"  />" + m2m.getName() + "</a>");
                     this.Pub1.append(BaseModel.AddTDEnd());
                     this.Pub1.append(BaseModel.AddTREnd());
                     break;
                 default:
                     break;
             }
		}
        // #endregion 增加M2M

         //#region 增加框架
    	 for (int i = 0; i < this.get_frams().size(); i++) {
    		 MapFrame fram=(MapFrame) this.get_frams().get(i);
             if (fram.IsUse)
                 continue;

            

             fram.IsUse = true;
             int myidx = rowIdx + 20;
             this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
             // this.Pub1.Add("<TD colspan=4 class=TRSum  ><div style='text-align:left; float:left'><a href=\"javascript:EditDtl('" + this.FK_MapData + "','" + dtl.No + "')\" >" + dtl.Name + "</a></div><div style='text-align:right; float:right'><a href=\"javascript:document.getElementById('F" + dtl.No + "').contentWindow.AddF('" + dtl.No + "');\"><img src='../Img/Btn/New.gif' border=0/>插入列</a><a href=\"javascript:document.getElementById('F" + dtl.No + "').contentWindow.CopyF('" + dtl.No + "');\"><img src='../Img/Btn/Copy.gif' border=0/>复制列</a><a href=\"javascript:DtlDoUp('" + dtl.No + "')\" ><img src='../Img/Btn/Up.gif' border=0/></a> <a href=\"javascript:DtlDoDown('" + dtl.No + "')\" ><img src='../Img/Btn/Down.gif' border=0/></a></div></td>");
             this.Pub1.append("<TD colspan=" + md.getTableCol() + " class=TRSum  ><div style='text-align:left; float:left'><a href=\"javascript:EditFrame('" + this.FK_MapData + "','" + fram.getMyPK() + "')\" >" + fram.getName() + "</a></div><div style='text-align:right; float:right'><a href=\"javascript:FrameDoUp('" + fram.getMyPK() + "')\" ><img src='../Img/Btn/Up.gif' border=0/></a> <a href=\"javascript:FrameDoDown('" + fram.getMyPK() + "')\" ><img src='../Img/Btn/Down.gif' border=0/></a></div></td>");
             this.Pub1.append(BaseModel.AddTREnd());

             myidx++;
             this.Pub1.append(BaseModel.AddTR(" ID='" + currGF.getIdx() + "_" + myidx + "' "));
             if (fram.getIsAutoSize())
                 this.Pub1.append("<TD colspan=" + md.getTableCol() + " ID='TD" + fram.getMyPK() + "' height='50px' width='1000px'>");
             else
                 this.Pub1.append("<TD colspan=" + md.getTableCol() + " ID='TD" + fram.getMyPK() + "' height='" + fram.getH() + "' width='" + fram.getW() + "' >");


             String src = fram.getURL(); // "MapDtlDe.jsp?DoType=Edit&FK_MapData=" + this.FK_MapData + "&FK_MapDtl=" + fram.No;
             if (src.contains("?"))
                 src += "&FK_Node=" + this.getRefNo() + "&WorkID=" + this.getRefOID();
             else
                 src += "?FK_Node=" + this.getRefNo() + "&WorkID=" + this.getRefOID();

             if (fram.getIsAutoSize())
                 this.Pub1.append("<iframe ID='F" + fram.getMyPK() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='100%' height='100%' scrolling=no  /></iframe>");
             else
                 this.Pub1.append("<iframe ID='F" + fram.getMyPK() + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='" + fram.getW() + "' height='" + fram.getH() + "' scrolling=no  /></iframe>");

             //  this.Pub1.Add("<iframe ID='F" + fram.No + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' src='" + src + "' width='" + fram.W + "px' height='" + fram.H + "px' scrolling=no /></iframe>");

             this.Pub1.append(BaseModel.AddTDEnd());
             this.Pub1.append(BaseModel.AddTREnd());
         }
        // #endregion 增加从表
     }

     private String RefNo;
     private int RefOID;
     public String getRefNo() {
		return RefNo;
	}
	public void setRefNo(String refNo) {
		RefNo = refNo;
	}
	public int getRefOID() {
		return RefOID;
	}
	public void setRefOID(int refOID) {
		RefOID = refOID;
	}
	//#region varable.
     private FrmAttachments _aths;
     public FrmAttachments get_aths(){
    	 if (_aths == null)
    	 {
    		 _aths = new FrmAttachments(this.FK_MapData);
    	 }
         return _aths;
	}
	public void set_aths(FrmAttachments _aths) {
		this._aths = _aths;
	}
//	public FrmAttachments aths
//     {
//         get
//         {
//             if (_aths == null)
//                 _aths = new FrmAttachments(this.FK_MapData);
//             return _aths;
//         }
//     }

     public GroupField currGF = new GroupField();
     private MapDtls _dtls;
     public MapDtls get_dtls() {
    	 if (_dtls == null)
             _dtls = new MapDtls(this.FK_MapData);
         return _dtls;
	}
	public void set_dtls(MapDtls _dtls) {
		this._dtls = _dtls;
	}
//	public MapDtls dtls
//     {
//         get
//         {
//             if (_dtls == null)
//                 _dtls = new MapDtls(this.FK_MapData);
//             return _dtls;
//         }
//     }
     private MapFrames _frams;
     public MapFrames get_frams() {
    	 if (_frams == null)
             _frams = new MapFrames(this.FK_MapData);
         return _frams;
	}
	public void set_frams(MapFrames _frams) {
		this._frams = _frams;
	}
//	public MapFrames frams
//     {
//         get
//         {
//             if (_frams == null)
//                 _frams = new MapFrames(this.FK_MapData);
//             return _frams;
//         }
//     }
     private MapM2Ms _dot2dots;
     public MapM2Ms get_dot2dots() {
    	 if (_dot2dots == null)
             _dot2dots = new MapM2Ms(this.FK_MapData);
         return _dot2dots;
	}
	public void set_dot2dots(MapM2Ms _dot2dots) {
		this._dot2dots = _dot2dots;
	}
//	public MapM2Ms dot2dots
//     {
//         get
//         {
//             if (_dot2dots == null)
//                 _dot2dots = new MapM2Ms(this.FK_MapData);
//             return _dot2dots;
//         }
//     }
     private GroupFields _gfs;
     public GroupFields get_gfs() {
    	 if (_gfs == null)
             _gfs = new GroupFields(this.FK_MapData);

         return _gfs;
	}
	public void set_gfs(GroupFields _gfs) {
		this._gfs = _gfs;
	}
//	public GroupFields gfs
//     {
//         get
//         {
//             if (_gfs == null)
//                 _gfs = new GroupFields(this.FK_MapData);
//
//             return _gfs;
//         }
//     }
     public int rowIdx = 0;
     public boolean isLeftNext = true;
     //#endregion varable.

     public String GenerLab_arr(MapAttr attr, int idx, int i, int count)
     {
         String divAttr = " onmouseover=FieldOnMouseOver('" + attr.getMyPK() + "') onmouseout=FieldOnMouseOut('" + attr.getMyPK() + "') ";
         String lab = attr.getName();
         if (attr.getMyDataType() == DataType.AppBoolean && attr.getUIIsLine())
             lab = "编辑";

         boolean isLeft = true;
         if (i == 1)
             isLeft = false;

         if (attr.getHisEditType() == EditType.Edit || attr.getHisEditType() == EditType.UnDel)
         {
             switch (attr.getLGType())
             {
                 case Normal:
                     lab = "<a  href=\"javascript:Edit('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 case FK:
                     lab = "<a  href=\"javascript:EditTable('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 case Enum:
                     lab = "<a  href=\"javascript:EditEnum('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 default:
                     break;
             }
         }
         else
         {
             lab = attr.getName();
         }

         if (idx == 0)
         {
             /*第一个。*/
             return "<div " + divAttr + " >" + lab + "<a href=\"javascript:Down('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Right.gif' class='Arrow' alt='向右动顺序' border=0/></a></div>";
         }

         if (idx == count - 1)
         {
             /*到数第一个。*/
             return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Left.gif' alt='向左移动顺序' class='Arrow' border=0/></a>" + lab + "</div>";
         }
         return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Left.gif' alt='向下移动顺序' class='Arrow' border=0/></a>" + lab + "<a href=\"javascript:Down('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Right.gif' alt='向右移动顺序' class='Arrow' border=0/></a></div>";
     }
     /// <summary>
     /// 字段or控件的顺序号.
     /// </summary>
     public int idx = 0;
     /// <summary>
     /// 属性
     /// </summary>
     /// <param name="attr"></param>
     /// <param name="idx"></param>
     /// <param name="i"></param>
     /// <param name="count"></param>
     /// <returns></returns>
     public String GenerLab(MapAttr attr, int i, int count)
     {
         idx++;

         String divAttr = " onDragEnd=onDragEndF('" + attr.getMyPK() + "','" + attr.getGroupID() + "');  onDrag=onDragF('" + attr.getMyPK() + "','" + attr.getGroupID() + "'); ";
         divAttr += " onDragOver=FieldOnMouseOver('" + attr.getMyPK() + "','" + attr.getGroupID() + "');  onDragEnter=FieldOnMouseOver('" + attr.getMyPK() + "','" + attr.getGroupID() + "'); ";
         divAttr += " onDragLeave=FieldOnMouseOut();";

         //divAttr += " onDragLeave=FieldOnMouseOut('" + attr.MyPK + "','" + attr.GroupID + "');";

         String lab = attr.getName();
         if (attr.getMyDataType() == DataType.AppBoolean && attr.getUIIsLine())
             lab = "编辑";

         boolean isLeft = true;
         if (i == 1)
             isLeft = false;

         if (attr.getHisEditType() == EditType.Edit || attr.getHisEditType() == EditType.UnDel)
         {
             switch (attr.getLGType())
             {
                 case Normal:
                     lab = "<a  href=\"javascript:Edit('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 case FK:
                     lab = "<a  href=\"javascript:EditTable('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 case Enum:
                     lab = "<a  href=\"javascript:EditEnum('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 default:
                     break;
             }
         }
         else
         {
             lab = attr.getName();
         }


         if (idx == 0)
         {
             /*第一个。*/
             return "<div " + divAttr + " >" + lab + "<a href=\"javascript:Down('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Right.gif' class='Arrow' alt='向右动顺序' border=0/></a></div>";
         }

         if (idx == count - 1)
         {
             /*到数第一个。*/
             return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Left.gif' alt='向左移动顺序' class='Arrow' border=0/></a>" + lab + "</div>";
         }
         return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Left.gif' alt='向下移动顺序' class='Arrow' border=0/></a>" + lab + "<a href=\"javascript:Down('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Right.gif' alt='向右移动顺序' class='Arrow' border=0/></a></div>";

         //if (idx == 0)
         //{
         //    /*第一个。*/
         //    return "<div " + divAttr + " >" + lab + "</div>";
         //}

         //if (idx == count - 1)
         //{
         //    /*到数第一个。*/
         //    return "<div " + divAttr + " >" + lab + "</div>";
         //}
         //return "<div " + divAttr + " >" + lab + "</div>";
     }
     public String GenerLab_bak(MapAttr attr, int idx, int i, int count)
     {
         String divAttr = " onDragEnd=onDragEndF('" + attr.getMyPK() + "','" + attr.getGroupID() + "')  onDrag=onDragF('" + attr.getMyPK() + "','" + attr.getGroupID() + "')  onMouseUp=alert('sss'); onmouseover=FieldOnMouseOver('" + attr.getMyPK() + "','" + attr.getGroupID() + "') onmouseout=FieldOnMouseOut('" + attr.getMyPK() + "','" + attr.getGroupID() + "') ";
         String lab = attr.getName();
         if (attr.getMyDataType() == DataType.AppBoolean && attr.getUIIsLine())
             lab = "编辑";

         boolean isLeft = true;
         if (i == 1)
             isLeft = false;

         if (attr.getHisEditType() == EditType.Edit || attr.getHisEditType() == EditType.UnDel)
         {
             switch (attr.getLGType())
             {
                 case Normal:
                     lab = "<a  href=\"javascript:Edit('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 case FK:
                     lab = "<a  href=\"javascript:EditTable('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 case Enum:
                     lab = "<a  href=\"javascript:EditEnum('" + this.FK_MapData + "','" + attr.getMyPK() + "','" + attr.getMyDataType() + "');\">" + lab + "</a>";
                     break;
                 default:
                     break;
             }
         }
         else
         {
             lab = attr.getName();
         }

         if (idx == 0)
         {
             /*第一个。*/
             return "<div " + divAttr + " >" + lab + "<a href=\"javascript:Down('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Right.gif' class='Arrow' alt='向右动顺序' border=0/></a></div>";
         }

         if (idx == count - 1)
         {
             /*到数第一个。*/
             return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Left.gif' alt='向左移动顺序' class='Arrow' border=0/></a>" + lab + "</div>";
         }
         return "<div " + divAttr + " ><a href=\"javascript:Up('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Left.gif' alt='向下移动顺序' class='Arrow' border=0/></a>" + lab + "<a href=\"javascript:Down('" + this.FK_MapData + "','" + attr.getMyPK() + "','1');\" ><img src='../Img/Btn/Right.gif' alt='向右移动顺序' class='Arrow' border=0/></a></div>";
     }
}
