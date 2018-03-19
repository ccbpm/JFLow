package cn.jflow.model.designer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrFile;
import BP.En.AttrFiles;
import BP.En.AttrOfOneVSM;
import BP.En.Attrs;
import BP.En.AttrsOfOneVSM;
import BP.En.ClassFactory;
import BP.En.DDLShowType;
import BP.En.EditerType;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.RefMethods;
import BP.En.TBType;
import BP.En.UIContralType;
import BP.Sys.EnCfg;
import BP.Sys.SysFileManager;
import BP.Sys.SysFileManagerAttr;
import BP.Sys.SysFileManagers;
import BP.Sys.SystemConfig;
import BP.Sys.UIConfig;
import BP.Sys.XML.AttrDesc;
import BP.Sys.XML.AttrDescAttr;
import BP.Sys.XML.AttrDescs;
import BP.WF.Glo;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ImageButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TB;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class UCEnModel extends BaseModel{
	
	UiFatory uf=null;
	
	private List<BaseWebControl> Controls=new ArrayList<BaseWebControl>();

	public StringBuffer pub=null;
	
    private boolean isReadonly;
    
    private boolean isShowDtl;
    
    public boolean isReadonly() {
		return isReadonly;
	}

	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	public boolean isShowDtl() {
		return isShowDtl;
	}

	public void setShowDtl(boolean isShowDtl) {
		this.isShowDtl = isShowDtl;
	}
	
	
	public HashMap<String,String> attributes  = new HashMap<String,String>();
	
	 public UCEnModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		pub=new StringBuffer();
		uf=new UiFatory();
	}

	public static String GetRefstrs(String keys, Entity en, Entities hisens)
     {
         return "";
     }
	
//     public UCEnModel()
//     {
//     }

     public void AddContral()
     {
         pub.append("<td class='FDesc' nowrap width=1% ></td>");
         pub.append("<td class='TD' nowrap ></TD>");
     }
     public void AddContral(String desc, CheckBox cb)
     {
         pub.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
         pub.append("<td class='TD' nowrap >");
         pub.append(cb.toString());
         pub.append("</td>");
     }
     public void AddContral(String desc, CheckBox cb, int colspan)
     {
         pub.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");
         pub.append("<td class='TD' nowrap colspan='" + colspan + "'>");
         pub.append(cb.toString());
         pub.append("</td>");
     }
     //		public void AddContral(String desc, String val)
     public void AddContral(String desc, String val)
     {
         pub.append("<TD class='FDesc' > " + desc.toString() + "</TD>");
         pub.append("<TD class='TD' > " + val + "</TD>");
     }
     public void AddContral(String desc, TextBox tb, String helpScript)
     {
         if (tb.getReadOnly())
         {
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class", "TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         //if (tb.ReadOnly == false)
         //    desc += "<font color=red><b>*</b></font>";

         // tb.Attributes["style"] = "width=100%;height=100%";
         if (tb.getTextMode() == TextBoxMode.MultiLine)
         {
             AddContralDoc(desc, tb);
             return;
         }

         tb.addAttr("Width","100%");

         pub.append("<td class='FDesc' nowrap width=1% >" + desc + "</td>");
         pub.append("<td class='TD' nowrap  >" + helpScript);
         pub.append(tb.toString());
         pub.append(this.AddTDEnd()); // ("</td>");
     }
     public void AddContral(String desc, TextBox tb, String helpScript, int colspan)
     {
         if (tb.getReadOnly())
         {
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class","TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         //if (tb.ReadOnly == false)
         //    desc += "<font color=red><b>*</b></font>";

         //  tb.Attributes["style"] = "width=100%;height=100%";
         if (tb.getTextMode() == TextBoxMode.MultiLine)
         {
             AddContralDoc(desc, tb);
             return;
         }

         //   tb.Attributes["Width"] = "30%";

         pub.append("<td class='FDesc' nowrap width=1% >" + desc + "</td>");

         if (colspan < 3)
         {
             pub.append("<td class='TD' nowrap colspan=" + colspan + " width='30%' >" + helpScript);
         }
         else
         {
             pub.append("<td class='TD' nowrap colspan=" + colspan + " width='80%' >" + helpScript);
         }

         pub.append(tb.toString());
         pub.append(this.AddTDEnd()); // ("</td>");
     }
     
     public void AddContral(String desc, DDL ddl, boolean isRefBtn, int colspan)
     {
    	 
    	 colspan=1;
    	 
         pub.append("<td class='FDesc' nowrap width=1% > " + desc + "</td><td  colspan=" + colspan + " nowrap>");
         this.Controls.add(ddl);
         pub.append(ddl.toString());
         if (ddl.getEnabled())
         {
        	 if (ddl.getSelfBindKey() ==null || ddl.getSelfBindKey().indexOf(".") == -1 )
             {
                 pub.append(this.AddTDEnd());
             }
             else
             {
                 if (isRefBtn && ddl.Items.size() > 4)
                 {
                     String srip = "javascript:HalperOfDDL('" + Glo.getCCFlowAppPath() + "WF"+"','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.getId() + "' ); ";
                     pub.append("<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.getId() + "' ></td>");
                 }
                 else
                 {
                     pub.append(this.AddTDEnd());
                 }
             }
         }
         else
         {
             pub.append(this.AddTDEnd());
         }
     }
     public void AddContral(String desc, DDL ddl, boolean isRefBtn)
     {
         pub.append("<td class='FDesc' nowrap width=1% > " + desc + "</td><td class=TD nowrap>");
         this.Controls.add(ddl);
         pub.append(ddl.toString());
         if (ddl.getEnabled())
         {
             if (ddl.getSelfBindKey().indexOf(".") == -1)
             {
                 pub.append(this.AddTDEnd());
             }
             else
             {
                 if (isRefBtn && ddl.Items.size() > 15)
                 {
                     String srip = "javascript:HalperOfDDL('" + Glo.getCCFlowAppPath() + "WF"+ "','" + ddl.getSelfBindKey() + "','" + ddl.getSelfEnsRefKey() + "','" + ddl.getSelfEnsRefKeyText() + "','" + ddl.getId() + "' ); ";
                     pub.append("<input type='button' value='...' onclick=\"" + srip + "\" name='b" + ddl.getId() + "' ></td>");
                 }
                 else
                 {
                     pub.append(this.AddTDEnd());
                 }
             }
         }
         else
         {
             pub.append(this.AddTDEnd());
         }
     }
     
     public void AddContral(String desc, TextBox tb, int colSpanOfCtl)
     {
         if (tb.getReadOnly())
         {
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class","TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         //if (tb.ReadOnly == false)
         //    desc += "<font color=red><b>*</b></font>";


         //  tb.Attributes["style"] = "width=100%;height=100%";
         if (tb.getTextMode() == TextBoxMode.MultiLine)
         {
             AddContralDoc(desc, tb, colSpanOfCtl);
             return;
         }

         pub.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");

         if (colSpanOfCtl < 3)
        	 pub.append("<td class='TD' nowrap colspan=" + colSpanOfCtl + " width='30%' >");
         else
        	 pub.append("<td class='TD' nowrap colspan=" + colSpanOfCtl + " width='80%' >");

         pub.append(tb.toString());
         pub.append(BaseModel.AddTDEnd());
     }
     /// <summary>
     /// 增加空件
     /// </summary>
     /// <param name="desc"></param>
     /// <param name="tb"></param>
     public void AddContral(String desc, TextBox tb)
     {
         if (tb.getReadOnly())
         {
             if ("TBNum".equals(tb.attributes.get("Class")))
                 tb.addAttr("Class","TBNumReadonly");
             else
                 tb.addAttr("Class","TBReadonly");
         }

         tb.addAttr("style","width=100%");
         if (tb.getTextMode() == TextBoxMode.MultiLine)
         {
             AddContralDoc(desc, tb);
             return;
         }

         pub.append("<td class='FDesc' nowrap width=1% > " + desc + "</td>");

         pub.append("<td class='TD' nowrap width='30%'>");
         pub.append(tb.toString());
         pub.append(BaseModel.AddTDEnd()); // ("</td>");
     }
     
     public void AddContralDoc(String desc, TextBox tb)
     {
         pub.append("<td colspan='2' width='500px' >" + desc + "<br>");
         if (tb.getReadOnly())
             tb.addAttr("Class","TBReadonly");
         pub.append(tb.toString());
         pub.append("</td>");
     }
     public void AddContralDoc(String desc, TextBox tb, int colspanOfctl)
     {
         pub.append("<td  colspan='" + colspanOfctl + "' width='100%'>" + desc + "<br>");
         tb.setCols(0);
         tb.setCssClass("TBDoc");
         pub.append(tb.toString());
         pub.append("</td>");
     }

     

	//
//     public bool IsReadonly
//     {
//         get
//         {
//             return (bool)this.ViewState["IsReadonly"];
//         }
//         set
//         {
//             ViewState["IsReadonly"] = value;
//         }
//     }
//     public bool IsShowDtl
//     {
//         get
//         {
//             return (bool)this.ViewState["IsShowDtl"];
//         }
//         set
//         {
//             ViewState["IsShowDtl"] = value;
//         }
//     }
     public void SetValByKey(String key, String val)
     {
         TextBox tb =uf.creatTextBox("TB_" + key);
         tb.setTextMode(TextBoxMode.Hidden);
         tb.setText(val);
         tb.setVisible(false);
         this.Controls.add(tb);
         pub.append(tb.toString());
     }
     public Object GetValByKey(String key)
     {
    	 UiFatory uf=new UiFatory();
         TextBox en = (TextBox)uf.GetUIByID("TB_" + key);
         return en.getText();
     }
     public void BindAttrs(Attrs attrs) throws Exception
     {
         //this.HisEn =en;
         boolean isReadonly = false;
         this.setReadonly(false);
         this.setShowDtl(false);
         this.attributes.put("visibility","hidden");
         pub.append("<table width='100%' id='a1' border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
         boolean isLeft = true;
         Object val = null;
         boolean isAddTR = true;
         for(Attr attr:attrs)
         {
             if (attr.getUIVisible() == false)
                 continue;

             if ("MyNum".equals(attr.getKey()))
                 continue;

             if (isLeft && isAddTR)
             {
                 pub.append(this.AddTR());
             }

             isAddTR = true;
             val = attr.getDefaultVal();
             if (attr.getUIContralType() == UIContralType.TB)
             {
                 if (attr.getMyFieldType() == FieldType.RefText)
                 {
                     this.SetValByKey(attr.getKey(), val.toString());
                     isAddTR = false;
                     continue;
                 }
                 else if (attr.getMyFieldType() == FieldType.MultiValues)
                 {
                     /* 如果是多值的.*/
                     ListBox lb = new ListBox(attr);
                     lb.setVisible(true);
                     lb.setHeight(128);
                     lb.setSelectionMode(ListSelectionMode.Multiple);
                     Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
                     ens.RetrieveAll();
                     this.Controls.add(lb);
                     pub.append(lb.toString());
                 }
                 else
                 {
                     if (attr.getUIVisible() == false)
                     {

                         TextBox tb = uf.creatTextBox("TB_" + attr.getKey());
                         tb.LoadMapAttr(attr);
                         tb.addAttr("Visible","false");
                         this.Controls.add(tb);
                         pub.append(tb.toString());
                         //this.AddContral(attr.Desc,area);
                         //this.SetValByKey(attr.Key, val.toString() );
                         continue;
                     }
                     else
                     {
                         if (attr.getUIHeight() != 0)
                         {
                             TextBox area = uf.creatTextBox("TB_" + attr.getKey());
                             area.LoadMapAttr(attr);
                             area.setText(val.toString());
                             area.setRows(8);
                             area.setTextMode(TextBoxMode.MultiLine);
                             if (isReadonly)
                                 area.setEnabled(false);
                             this.AddContral(attr.getDesc(), area);
                         }
                         else
                         {
                             TextBox tb = new TextBox();
                             tb.setId("TB_" + attr.getKey());
                             tb.LoadMapAttr(attr);

                             if (isReadonly)
                                 tb.setEnabled(false);
                             switch (attr.getMyDataType())
                             {
                                 case DataType.AppMoney:
                                     tb.setText(""+Float.parseFloat(val.toString()));
                                     break;
                                 default:
                                     tb.setText(val.toString());
                                     break;
                             }
                             tb.addAttr("width","100%");
                             this.AddContral(attr.getDesc(), tb);
                         }
                     }
                 }
             }
             else if (attr.getUIContralType() == UIContralType.CheckBok)
             {
                 CheckBox cb =uf.creatCheckBox("CB_" + attr.getKey());
                 if ("1".equals(attr.getDefaultVal().toString()))
                     cb.setChecked(true);
                 else
                     cb.setChecked(false);

                 if (isReadonly)
                     cb.setEnabled(false);
                 else
                     cb.setEnabled(attr.getUIVisible());

                 this.AddContral(attr.getDesc(), cb);
             }
             else if (attr.getUIContralType() == UIContralType.DDL)
             {
                 if (isReadonly || !attr.getUIIsReadonly())
                 {
                     /* 如果是 DDLIsEnable 的, 就要找到. */
                     if (attr.getMyFieldType() == FieldType.Enum)
                     {
                         /* 如果是 enum 类型 */
                         int enumKey = 0;
                         try
                         {
                             enumKey = Integer.parseInt(val.toString());
                         }
                         catch(Exception e)
                         {
                        	 e.printStackTrace();
                             throw new Exception("默认值错误：" + attr.getKey() + " = " + val.toString());
                         }

                         BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);


                         //DDL ddl = new DDL(attr,text,en.Lab,false);
                         DDL ddl = uf.creatDDL("DDL_" + attr.getKey());
                         ddl.Items.add(new ListItem(enEnum.getLab(), val.toString()));
//                         ddl.Items.add(new ListItem(enEnum.getLang(),val.toString()));
//                         ddl.Items.add(new ListItem(enEnum.getEnumKey(),val.toString()));
                         ddl.Items.get(0).setSelected(true);
                         ddl.setEnabled(false);

                         this.AddContral(attr.getDesc(), ddl, true);
                         //this.Controls.Add(ddl);
                     }
                     else
                     {
                         /* 如果是 ens 类型 */
                         Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
                         Entity en1 = ens.getGetNewEntity();
                         en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
                         String lab = "";
                         try
                         {
                             en1.Retrieve();
                             lab = en1.GetValStringByKey(attr.getUIRefKeyText());
                         }
                         catch(Exception e)
                         {
                             if (SystemConfig.getIsDebug() == false)
                             {
                                 lab = "" + val.toString();
                             }
                             else
                             {
                                 lab = "" + val.toString();
                                 //lab="没有关联到值"+val.toString()+"Class="+attr.UIBindKey+"EX="+ex.Message;
                             }
                         }
                         DDL ddl = uf.creatDDL("DDL_" + attr.getKey());//new DDL(attr, val.toString(), lab, false, this.get_request().getRealPath("/"));
                         ddl.setCssClass("DDL"+WebUser.getStyle());
                         ddl.setAppPath(this.get_request().getRealPath("/"));
                         ddl.setSelfDefaultText(val.toString());
                 		 ddl.setSelfDefaultVal(lab);
                         ddl.setEnabled(false);
                 		ddl.setSelfShowType(attr.getUIDDLShowType());

                 		ddl.setSelfBindKey(attr.getUIBindKey());
                 		ddl.setSelfEnsRefKey(attr.getUIRefKeyValue());
                 		ddl.setSelfEnsRefKeyText(attr.getUIRefKeyText());
                 		ddl.setHisFKEns(attr.getHisFKEns());
                 		ddl.setSelfIsShowVal(false);
                        this.AddContral(attr.getDesc(), ddl, true);
                         //this.Controls.Add(ddl);
                     }
                 }
                 else
                 {
                     /* 可以使用的情况. */
                     DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
                     ddl1.setId("DDL_" + attr.getKey());
                     ddl1.setName("DDL_" + attr.getKey());
                     this.AddContral(attr.getDesc(), ddl1, true);
                     //	this.Controls.Add(ddl1);
                 }
             }
             else if (attr.getUIContralType() == UIContralType.RadioBtn)
             {
                 //					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
                 //					for(SysEnum en in enums)
                 //					{
                 //						return ;
                 //					}
             }

             if (isLeft == false)
                 pub.append(this.AddTREnd());

             isLeft = !isLeft;
         } // 结束循环.

         pub.append("</TABLE>");
     }
     //		public void BindReadonly(Entity en )
     public void BindReadonly(Entity en)
     {
         this.HisEn = en;
         //this.IsReadonly = isReadonly;
         //this.IsShowDtl = isShowDtl;
         this.attributes.put("visibility","hidden");
         this.Controls.clear();
         pub.append(this.AddTable()); //("<table   width='100%' id='AutoNumber1'  border='1' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
         boolean isLeft = true;
         Object val = null;
         boolean isAddTR = true;
         for(Attr attr:en.getEnMap().getAttrs())
         {
             if (isLeft && isAddTR)
             {
                 pub.append("<tr>");
             }
             isAddTR = true;
             val = en.GetValByKey(attr.getKey());
             if (attr.getUIContralType() == UIContralType.TB)
             {
                 if (attr.getMyFieldType() == FieldType.RefText)
                 {
                     this.AddContral(attr.getDesc(), val.toString());
                     isAddTR = false;
                     continue;
                 }
                 else if (attr.getMyFieldType() == FieldType.MultiValues)
                 {
                     /* 如果是多值的.*/
                     ListBox lb = new ListBox(attr);
                     lb.setVisible(true);
                     lb.setHeight(128);
                     lb.setSelectionMode(ListSelectionMode.Multiple);
                     Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
                     ens.RetrieveAll();
                     this.Controls.add(lb);
                     pub.append(lb.toString());
                 }
                 else
                 {
                     if (attr.getUIVisible() == false)
                     {
                         this.SetValByKey(attr.getKey(), val.toString());
                         continue;
                     }
                     else
                     {

                         if (attr.getUIHeight() != 0)
                         {
                             this.AddContral(attr.getDesc(), val.toString());
                         }
                         else
                         {

                             switch (attr.getMyDataType())
                             {
                                 case DataType.AppMoney:
                                     //this.AddContral(attr.Desc, val.toString().toString("0.00")  );
                                     break;
                                 default:
                                     this.AddContral(attr.getDesc(), val.toString());
                                     break;
                             }
                         }
                     }

                 }
             }
             else if (attr.getUIContralType() == UIContralType.CheckBok)
             {
                 if (en.GetValBooleanByKey(attr.getKey()))
                     this.AddContral(attr.getDesc(), "是");
                 else
                     this.AddContral(attr.getDesc(), "否");
             }
             else if (attr.getUIContralType() == UIContralType.DDL)
             {
                 this.AddContral(attr.getDesc(), val.toString());
             }
             else if (attr.getUIContralType() == UIContralType.RadioBtn)
             {
                 //					Sys.SysEnums enums = new BP.Sys.SysEnums(attr.UIBindKey); 
                 //					for(SysEnum en in enums)
                 //					{
                 //						return ;
                 //					}
             }

             if (isLeft == false)
                 pub.append(this.AddTREnd());

             isLeft = !isLeft;
         } // 结束循环.

         pub.append("</TABLE>");



         if (en.IsExit(en.getPK(), en.getPKVal()) == false)
             return;

         String refstrs = "";
         if (en.equals(""))
         {
             refstrs += "";
             return;
         }

         String keys = "&PK=" + en.getPKVal().toString();
         for(Attr attr:en.getEnMap().getAttrs())
         {
             if (attr.getMyFieldType() == FieldType.Enum ||
                 attr.getMyFieldType()== FieldType.FK ||
                 attr.getMyFieldType() == FieldType.PK ||
                 attr.getMyFieldType() == FieldType.PKEnum ||
                 attr.getMyFieldType()== FieldType.PKFK)
                 keys += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
         }
         Entities hisens = en.getGetNewEntities();
         SimpleDateFormat sdf=new SimpleDateFormat("ddhhmmss");
         keys += "&r=" + sdf.format(new Date());
         refstrs = GetRefstrs(keys, en, en.getGetNewEntities());
         if (refstrs != "")
             refstrs += "<hr>";
         pub.append(refstrs);
     }
     /// <summary>
     /// 
     /// </summary>
     /// <param name="en"></param>
     /// <param name="isReadonly"></param>
     /// <param name="isShowDtl"></param>
     //		public void Bind3Item(Entity en, bool isReadonly, bool isShowDtl)
     public void Bind3Item(Entity en, boolean isReadonly, boolean isShowDtl)
     {
         AttrDescs ads = new AttrDescs(en.toString());
         this.HisEn = en;
         this.setReadonly(isReadonly);
         this.setShowDtl(isShowDtl);
         this.Controls.clear();
         
         this.attributes.put("visibility","hidden");
         this.Controls.clear();
         pub.append("<table   width='100%' id='AutoNumber1'  border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse' bordercolor='#111111' >");
         boolean isLeft = true;
         Object val = null;
         Attrs attrs = en.getEnMap().getAttrs();
         for(Attr attr:attrs)
         {

             if ("MyNum".equals(attr.getKey()))
                 continue;

             val = en.GetValByKey(attr.getKey());
             if (attr.getUIContralType() == UIContralType.TB)
             {
                 if (attr.getMyFieldType() == FieldType.RefText)
                 {
                     continue;
                 }
                 else if (attr.getMyFieldType() == FieldType.MultiValues)
                 {
                     /* 如果是多值的.*/
                     ListBox lb = new ListBox(attr);
                     lb.setVisible(true);

                     lb.setHeight(128);
                     lb.setSelectionMode(ListSelectionMode.Multiple);
                     Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
                     ens.RetrieveAll();
                     pub.append(this.AddTR());
                     this.Controls.add(lb);
                     pub.append(lb.toString());
                 }
                 else
                 {
                     if (attr.getUIVisible() == false)
                     {
                         this.SetValByKey(attr.getKey(), val.toString());
                         continue;
                     }
                     else
                     {
                         if (attr.getUIHeight() != 0)
                         {
                             /* doc 文本类型。　*/
                             TextBox area = uf.creatTextBox("TB_" + attr.getKey());
                             area.LoadMapAttr(attr);
                             area.setText(val.toString());
                             area.setRows(8);
                             area.setCols(30);

                             area.setTextMode(TextBoxMode.MultiLine);
                             area.addAttr("height","100px");
                             //area.Attributes["width"]="100px";
                             area.setIsHelpKey("0");

                             area.addAttr("Class", "TextArea1");

                             if (isReadonly)
                                 area.setEnabled(false);

                             pub.append(this.AddTR());
                             pub.append("<TD colspan=3 class='FDesc' >" + attr.getDesc() + "</TD>");
                             pub.append(this.AddTREnd());

                             pub.append(this.AddTR());
                             pub.append("<TD colspan=3 class='TD' height='250' >");
                             pub.append(area);
                             pub.append("</TD>");
                             pub.append(this.AddTREnd());
                             continue;
                         }
                         else
                         {
                             TextBox tb = uf.creatTextBox("TB_" + attr.getKey());
                             tb.setIsHelpKey("0");

                             if (isReadonly || attr.getUIIsReadonly())
                                 tb.setEnabled(false);
                             switch (attr.getMyDataType())
                             {
                                 case DataType.AppMoney:
                                     tb.setText(""+Float.parseFloat(val.toString()));
                                     break;
                                 default:
                                     tb.setText(val.toString());
                                     break;
                             }
                             tb.addAttr("width","100%");
                             pub.append(this.AddTR());
                             this.AddContral(attr.getDesc(), tb);

                             /*
                             AttrDesc ad = ads.GetEnByKey(AttrDescAttr.Attr,  attr.Key ) as AttrDesc;
                             if (ad!=null)
                                 this.AddContral(attr.Desc,tb);
                             else
                             {
                                 //this.AddContral(attr.Desc,tb);

                                 tb.Attributes["width"]="";

                                 //this.AddTR();
                                 this.Add("<TD class='FDesc' width='1%' >"+attr.Desc+"</TD>");
                                 this.Add("<TD class='TD' colspan=2 >");
                                 this.Add(tb);
                                 this.Add("</TD>");
                                 this.AddTREnd();
                                 continue;
                             }
                             */

                         }
                     }
                 }
             }
             else if (attr.getUIContralType() == UIContralType.CheckBok)
             {
                 CheckBox cb = uf.creatCheckBox("CB_" + attr.getKey());
                 cb.setChecked( en.GetValBooleanByKey(attr.getKey()));

                 if (isReadonly || !attr.getUIIsReadonly())
                     cb.setEnabled(false);
                 else
                     cb.setEnabled(attr.getUIVisible());


                 pub.append(this.AddTR());
                 this.AddContral(attr.getDesc(), cb);
             }
             else if (attr.getUIContralType() == UIContralType.DDL)
             {
                 if (isReadonly || !attr.getUIIsReadonly())
                 {
                     /* 如果是 DDLIsEnable 的, 就要找到. */
                     if (attr.getMyFieldType() == FieldType.Enum)
                     {
                         /* 如果是 enum 类型 */
                         int enumKey = Integer.parseInt(val.toString());
                         BP.Sys.SysEnum enEnum = new BP.Sys.SysEnum(attr.getUIBindKey(), "CH", enumKey);

                         //DDL ddl = new DDL(attr,text,en.Lab,false);
                         DDL ddl = new DDL();
                         ddl.Items.add(new ListItem(enEnum.getLab(), val.toString()));
                         ddl.Items.get(0).setSelected(true);
                         ddl.setEnabled(false);
                         ddl.setId("DDL_" + attr.getKey());

                         pub.append(this.AddTR());
                         this.AddContral(attr.getDesc(), ddl, false);
                         //this.Controls.Add(ddl);
                     }
                     else
                     {
                         /* 如果是 ens 类型 */
                         Entities ens = ClassFactory.GetEns(attr.getUIBindKey());
                         Entity en1 = ens.getGetNewEntity();
                         en1.SetValByKey(attr.getUIRefKeyValue(), val.toString());
                         String lab = "";
                         try
                         {
                             en1.Retrieve();
                             lab = en1.GetValStringByKey(attr.getUIRefKeyText());
                         }
                         catch(Exception e)
                         {
                             if (SystemConfig.getIsDebug() == false)
                             {
                                 lab = "" + val.toString();
                             }
                             else
                             {
                                 lab = "" + val.toString();
                                 //lab="没有关联到值"+val.toString()+"Class="+attr.UIBindKey+"EX="+ex.Message;
                             }
                         }
                         DDL ddl = new DDL(attr, val.toString(), lab, false, this.get_request().getRealPath("/"));
                         for(Attr a:attrs){
                             ddl.setId("DDL_" + a.getKey());
                             ddl.setName("DDL_" + a.getKey());
                             pub.append(this.AddTR());
                         }
                         this.AddContral(attr.getDesc(), ddl, false);
                         //this.Controls.Add(ddl);
                     }
                 }
                 else
                 {
                     /* 可以使用的情况. */
                     DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
                     ddl1.setId("DDL_" + attr.getKey());
                     ddl1.setName("DDL_" + attr.getKey());
                     //ddl1.SelfBindKey = ens.toString();
                     //ddl1.SelfEnsRefKey = attr.UIRefKeyValue;
                     //ddl1.SelfEnsRefKeyText = attr.UIRefKeyText;

                     pub.append(this.AddTR());
                     this.AddContral(attr.getDesc(), ddl1, true);
                 }
             }
             else if (attr.getUIContralType() == UIContralType.RadioBtn)
             {

             }

             AttrDesc ad1 = (AttrDesc)ads.GetEnByKey(AttrDescAttr.Attr, attr.getKey());
             if (ad1 == null)
            	 pub.append(this.AddTD("class='Note'", "&nbsp;"));
             else
            	 pub.append(this.AddTD("class='Note'", ad1.getDesc()));

             pub.append(this.AddTREnd());
         } //结束循环.

         if (en.getEnMap().getAttrs().Contains("MyFileName"))
         {
             /* 如果包含这二个字段。*/
             String fileName = en.GetValStringByKey("MyFileName");
             String filePath = en.GetValStringByKey("MyFilePath");
             String fileExt = en.GetValStringByKey("MyFileExt");

             String url = "";
             if (fileExt != "")
             {
                 // 系统物理路径。
                 String path = this.get_request().getRealPath("/").toLowerCase();
                 String path1 = filePath.toLowerCase();
                 path1 = path1.replace(path, "");
                 url = "&nbsp;&nbsp;<a href='../" + path1 + "/" + en.getPKVal() + "." + fileExt + "' target=_blank ><img src='../Images/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
             }

             pub.append(this.AddTR());
             pub.append(this.AddTD("align=right nowrap=true class='FDesc'", "附件或图片:"));
            
             TextBox tbf=uf.creatTextBox("file");
             tbf.setTextMode(TextBoxMode.Files);
             tbf.addAttr("style", "width:60%");
             pub.append("<TD colspan=2  class='FDesc' >");
             pub.append(tbf.toString());
             pub.append(url.toString());
             if (fileExt != "")
             {
                 Button btn1 = uf.creatButton("Btn_DelFile");
                 btn1.setText( "移除");
//                 btn1.setId("Btn_DelFile");
                 btn1.setCssClass("Btn");
                 btn1.addAttr("Class", "Btn1");
                 String s=btn1.attributes.get("onclick");
                 btn1.addAttr("onclick",s+" return confirm('此操作要执行移除附件或图片，是否继续？');");
                 pub.append(btn1);
             }
             pub.append("</TD>");

             pub.append(this.AddTREnd());
         }

         pub.append(this.AddTR());
         pub.append("<TD align=center colspan=3 >");


         Button btn = new Button();
         if (en.getHisUAC().IsInsert)
         {
             btn = new Button();
             btn.setId("Btn_New");
             btn.setName("Btn_New");
             btn.setText("  新 建  ");
             btn.setCssClass("Btn");
             btn.addAttr("Class", "Btn1");

             pub.append(btn);
             pub.append("&nbsp;");
         }

         if (en.getHisUAC().IsUpdate)
         {
             btn = new Button();
             btn.setName("Btn_Save");
             btn.setId("Btn_Save");
             btn.setCssClass("Btn");
             btn.setText("  保  存  ");
             btn.addAttr("Class", "Btn1");

             pub.append(btn);
             pub.append("&nbsp;");
         }


         if (en.getHisUAC().IsDelete)
         {
             btn = new Button();
             btn.setName("Btn_Del");
             btn.setId("Btn_Del");
             btn.setCssClass("Btn");
             btn.setText("  删  除  ");
             btn.addAttr("Class", "Btn1");

             btn.addAttr("onclick", " return confirm('您确定要执行删除吗？');");
             pub.append(btn);
             pub.append("&nbsp;");
         }

         pub.append("&nbsp;<input class='Btn' type=button onclick='javascript:window.close()' value='  关  闭  ' />");

         pub.append("</TD>");
         pub.append(this.AddTREnd());

         pub.append(this.AddTableEnd());

         if (isShowDtl == false)
             return;


         if (en.IsExit(en.getPK(), en.getPKVal()) == false)
             return;

         String refstrs = "";
         if (en.equals(""))
         {
             refstrs += "";
             return;
         }
         pub.append("<HR>");

         String keys = "&PK=" + en.getPKVal().toString();
         for(Attr attr:en.getEnMap().getAttrs())
         {
             if (attr.getMyFieldType() == FieldType.Enum ||
                 attr.getMyFieldType() == FieldType.FK ||
                 attr.getMyFieldType() == FieldType.PK ||
                 attr.getMyFieldType() == FieldType.PKEnum ||
                 attr.getMyFieldType() == FieldType.PKFK)
                 keys += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
         }
         Entities hisens = en.getGetNewEntities();
         SimpleDateFormat sdf=new SimpleDateFormat("ddhhmmss");
         keys += "&r=" + sdf.format(new Date());
         refstrs = GetRefstrs(keys, en, en.getGetNewEntities());
         if (refstrs != "")
             refstrs += "<hr>";

         pub.append(refstrs);
     }
//     private void btn_Click(object sender, EventArgs e)
//     {
//     }

     /// <summary>
     /// 绑定所有结点属性，使用EasyUI来布局
     /// </summary>
     /// <param name="en"></param>
     /// <param name="enName"></param>
     /// <param name="isReadonly"></param>
     /// <param name="isShowDtl"></param>
     public void BindV2(Entity en, String enName, boolean isReadonly, boolean isShowDtl) throws Exception
     {
         this.BindV2(en, enName, isReadonly, isShowDtl, null);
     }

     /// <summary>
     /// 绑定所有结点属性，使用EasyUI来布局
     /// </summary>
     /// <param name="en"></param>
     /// <param name="enName"></param>
     /// <param name="isReadonly"></param>
     /// <param name="isShowDtl"></param>
     /// <param name="noKey"></param>
     public void BindV2(Entity en, String enName, boolean isReadonly, boolean isShowDtl, String noKey) throws Exception
     {
         if (enName == null)
             enName = en.toString();

         this.HisEn = en;
         this.setReadonly(isReadonly);
         this.setShowDtl(isShowDtl);
         this.Controls.clear();
         boolean isLeft = true;
         Object val = null;
         Map map = en.getEnMap();
         Attrs attrs = map.getAttrs();
         EnCfg cf = new EnCfg(enName);
         String[] gTitles = cf.getGroupTitle().split("@",-1); // 获取分组标签.
         int idxL1, idxL2;
         String tabTitle;
         int idx = 0;
         java.util.LinkedHashMap<String,String> dictGroups=new java.util.LinkedHashMap<String, String>();
         for(int i=0;i<gTitles.length;i++){
        	 if(!gTitles[i].equals("")){
        		 String[] s=gTitles[i].split("=");
        		 dictGroups.put(s[0], s[1]);
        	 }
         }
         boolean haveFile = en.getEnMap().getAttrs().Contains("MyFileName") || en.getEnMap().getHisAttrFiles().size() > 0 || en.getEnMap().getAttrs().Contains("MyFileNum");

 		//
//         int dictGroups = gTitles.Select(g => g.Split("=".ToCharArray(), StringSplitOptions.RemoveEmptyEntries))
//             .Where(g => g.Length == 2)
//             .ToDictionary(g => g[0], g => g[1]);
       //如果此处没有分组，则默认增加一个分组，2015-8-4,added by liuxc
         if (dictGroups.size() == 0)
         {
//             String firstAttr = attrs.Cast<Attr>().FirstOrDefault(a => a.Key != "MyNum");
        	 Attr firstAttr = null;
        	 for (Attr a : attrs.ToJavaList()){
        		 if (!a.getKey().equals("MyNum")){
        			 firstAttr = a;
        			 break;
        		 }
        	 }
             if (firstAttr != null)
                 dictGroups.put(firstAttr.getKey(), map.getEnDesc());
         }

         UIConfig cfg = new UIConfig(en);

         //求出来相关字段与功能的键.
         RefMethods rms = map.getHisRefMethods();
         String rmKeys = "";
         for(RefMethod item:rms)
         {
             if (item.RefAttrKey == null)
                 continue;
             rmKeys += ","+item.RefAttrKey+",";
         }

         //增加tab工具栏
         pub.append("<div id='tab-tools'>");
         pub.append("<a href='javascript:void(0)' class='easyui-menubutton' data-options=\"plain:true,menu:'#tab-menu'\">选</a>");
         pub.append("</div>");

         //生成tab导航菜单
         pub.append("<div class='easyui-menu' id='tab-menu'>");

         for(String de:dictGroups.keySet())
         {
             idxL1 = dictGroups.get(de).indexOf(',');
             idxL2 = dictGroups.get(de).indexOf('，');

             //去除有英文/中文括号内的内容，标题只显示括号之前的内容，防止标题过长
             if (idxL1 > 0)
                 tabTitle = dictGroups.get(de).substring(0, idxL1);
             else if (idxL2 > 0)
                 tabTitle = dictGroups.get(de).substring(0, idxL2);
             else
                 tabTitle = dictGroups.get(de);
             int i=++idx;
             pub.append("<div onclick=\""+"selectTab('"+tabTitle+"')\">"+i+"."+tabTitle+"</div>" );
         }
         if (haveFile) {
        	 pub.append("<div onclick=\"selectTab('附件管理')\">" + (++idx) + ".附件管理</div>");
 		}

         pub.append("</div>");

         //为处理保存后显示之前打开的标签，所增加的隐藏域，记录当前打开的标签title
         //在保存事件处理后，获取该隐藏域中记录的标签title,将该title加入到redirect的url参数中,在页面前端的JS中捕获title，选中该标签
         //added by liuxc,2014-11-07
         TextBox hiddenField = uf.creatTextBox("Hid_CurrentTab");
         hiddenField.setTextMode(TextBoxMode.Hidden);
//         hiddenField.setId("Hid_CurrentTab");
         pub.append(hiddenField);

         pub.append(
             "<div id='nav-tab' class='easyui-tabs' data-options=\"tools:'#tab-tools',fit:true,onSelect:function(title,index){ $('#" +
             hiddenField.getId() + "').val(index); }\" style='width:740px;height:auto'>");

         // 循环组来输出数据.
         String[] garr = null;

         for (String g:dictGroups.keySet())
         {
             garr = dictGroups.get(g).split(",");
             String s=garr.length > 1 ? garr[1] : "";
             
             pub.append("<div title='"+garr[0]+"' data-g='"+garr[0]+"' data-gd='"+s+"'>");
             pub.append(AddTable("class='Table' cellpadding='0' cellspacing='0' style='width:100%;'"));
             //this.Add("<caption>" + g.Value + "</caption>");
             isLeft = true;
             String currKey = null;

             for (Attr attr:attrs)
             {
                 if (attr.getKey().equals("MyNum"))
                     continue;
                 if (dictGroups.containsKey(attr.getKey()))
                 {
                     if (g.equals(currKey))
                         break;

                     currKey = attr.getKey();

                     if (!currKey.equals(g))
                         continue;
                 }
                 else
                 {
                     if (!currKey.equals(g))
                         continue;
                 }

                 if (attr.getMyFieldType() == FieldType.RefText)
                     continue;

                 val = en.GetValByKey(attr.getKey());
                 if (attr.getUIVisible() == false)
                 {
                     this.SetValByKey(attr.getKey(), val.toString());
                     continue;
                 }

                // #region 判断是否单列显示
                 if (attr.UIIsLine)
                 {
                     if (isLeft == false)
                     {
                    	 pub.append(BaseModel.AddTD());
                    	 pub.append(BaseModel.AddTD());
                    	 pub.append(BaseModel.AddTREnd());
                         isLeft = true;
                     }

                     pub.append(BaseModel.AddTR());
                     if (attr.getUIHeight() != 0)
                     {
                         /*大块文本采集, 特殊处理。*/
                         if (val.toString().length() == 0 && en.getIsEmpty() == false && attr.getKey().equals("Doc"))
                             val = en.GetValDocText();

                         /* doc 文本类型。　*/
                         if (attr.getUIIsReadonly() || isReadonly)
                         {
                             TextBox areaR = uf.creatTextBox("TB_" + attr.getKey());
                             areaR.setText(val.toString());
                             areaR.setRows(5);
                             areaR.setTextMode(TextBoxMode.MultiLine);
                             areaR.setReadOnly(true);
                             this.AddContral(attr.getDesc(), areaR, 4);
                         }
                         else
                         {
                             int type =0; // EnsAppCfgs.GetValInt(en.toString() + "s", "EditerType");
                             
                             if(type==EditerType.FKEditer.getValue()){
                            	 
                             }
                             else if(type==EditerType.Sina.getValue()){
                            	 TextBox input = uf.creatTextBox("TBH_" + attr.getKey());
                            	 input.setTextMode(TextBoxMode.Hidden);
                                 //  input.Attributes["id"] = "TBH_"+attr.Key;
                                 input.setId("TBH_" + attr.getKey());
                                 input.setText(val.toString());
                                 pub.append("<td class='FDesc'  colspan='4' width='50%' >");
                                 pub.append(input.toString());
                                 pub.append("<iframe ID='eWebEditor1' src='./Ctrl/editor/editor.htm?id=" + input.getId() + "&style=coolblue' frameborder='0' scrolling='no' width='600' HEIGHT='350'></iframe>");
                                 pub.append(AddTDEnd());
                             }
                             else{
                            	 TextBox area = uf.creatTextBox("TB_" + attr.getKey());
                                 area.LoadMapAttr(attr);
                                 area.setId("TB_" + attr.getKey());
                                 area.setText(val.toString());
                                 area.setRows(5);
                                 area.setTextMode(TextBoxMode.MultiLine);
                                 area.setIsHelpKey("1");
                                 this.AddContral(attr.getDesc(), area, 4);
                             }
                             
                         }
                         pub.append(BaseModel.AddTREnd());

                         isLeft = true;
                         continue;
                     }

                     UIContralType ut=attr.getUIContralType();
                     
                     if(ut==UIContralType.TB){
                    	 TextBox tb =uf.creatTextBox("TB_" + attr.getKey());
                         tb.setId("TB_" + attr.getKey());
                         // tb.LoadMapAttr(attr);
                         switch (attr.getMyDataType())
                         {
                             case DataType.AppMoney:
                                 tb.setText(""+Float.parseFloat(val.toString()));
                                 tb.addAttr("Class", "TBNum");
                                 tb.setShowType(TBType.Moneny);
                                 break;
                             case DataType.AppInt:
                             case DataType.AppFloat:
                             case DataType.AppDouble:
                             case DataType.AppRate:
                                 tb.addAttr("Class", "TBNum");
                                 tb.setText(val.toString());
                                 tb.setShowType(TBType.Num);
                                 break;
                             case DataType.AppDate:
                                 tb.setText(val.toString());
                                 tb.setShowType(TBType.Date);
                                 tb.addAttr("Class","TBcalendar");
                                 tb.setIsHelpKey("1");
                                 if (attr.getUIIsReadonly() == false)
                                     tb.addAttr("onfocus", "WdatePicker();");
                                 break;
                             case DataType.AppDateTime:
                                 tb.setText(val.toString());
                                 tb.setShowType(TBType.Date);
                                 tb.addAttr("Class","TBcalendar");
                                 tb.setIsHelpKey("1");
                                 if (attr.getUIIsReadonly() == false)
                                     tb.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
                                 break;
                             default:
                                 tb.setText(val.toString());
                                 tb.addAttr("Class","TB");
//                                 tb.addAttr("Width","95%");                                
                                 break;
                         }

                         if (isReadonly || attr.getUIIsReadonly())
                             tb.setReadOnly(true);

                         tb.setEnabled(true);
                         if (attr.getKey().equals(noKey) && attr.getUIIsReadonly() == false && attr.getUIVisible() == true)
                         {
                             String path = this.get_request().getRealPath("/");
                             //String helpScript = "";
                             String helpScript = "<a href=\"javascript:alert('请在文本框上双击，系统会出现查找窗口。');\" ><img src='./Img/Btn/Search.gif' border=0 /></a>";
                             tb.addAttr("ondblclick","OpenHelperTBNo('" + path + "','BP.Port.Taxpayers',this)");
                             this.AddContral(attr.getDesc(), tb, helpScript, 3);
                         }
                         else
                         {
                        	 tb.setCols(80);
                             tb.addAttr("Class","TB");
//                             tb.addAttr("width", "500px");
//                             tb.addAttr("style", "width:99%");
                             if (attr.HelperUrl == null)
                                 this.AddContral(attr.getDesc(), tb, 3);
                             else
                                 this.AddContral(attr.getDescHelper(), tb, 3);
                         }
//                         tb.Dispose();
                     }
                     else if(ut==UIContralType.DDL){
                    	 if (isReadonly || !attr.getUIIsReadonly())
                         {
                             DDL ddl = uf.creatDDL("DDL_" + attr.getKey());
                             //ddl.CssClass = "easyui-combobox";// "DDL" + WebUser.Style;
//                             for(Attr a:attrs){
                            	 String text = en.GetValRefTextByKey(attr.getKey());
                                 if (text == null || "".equals(text))
                                     text = val.toString();

                                 ListItem li = new ListItem(text, val.toString());
                                 li.addAttr("Class","TB");
                                 ddl.Items.add(li);
//                             }
                             ddl.setEnabled(false);
                             
                             
                             this.AddContral(attr.getDesc(), ddl, true, 3);
                         }
                         else
                         {
                             /* 可以使用的情况. */
                        
                             DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true,this.get_request().getRealPath("/"));
                             ddl1.setName("DDL_" + attr.getKey());
                             this.AddContral(attr.getDesc(), ddl1, true, 3);
                         }
                     }
                     else if(ut==UIContralType.CheckBok){
                    	 CheckBox cb = uf.creatCheckBox("CB_" + attr.getKey());
                         cb.setChecked(en.GetValBooleanByKey(attr.getKey()));
                         cb.setId("CB_" + attr.getKey());

                         if (isReadonly)
                             cb.setEnabled(false);
                         else
                             cb.setEnabled(attr.getUIIsReadonly());

                         cb.setText(attr.getDesc());
                         if (attr.HelperUrl == null)
                             this.AddContral("", cb, 3);
                         else
                             this.AddContral(attr.getDescHelperIcon(), cb, 3);
                     }
                     else{
                    	 
                     }

                     pub.append(BaseModel.AddTREnd());
                     isLeft = true;
                     continue;
                 }
//
                 if (isLeft)
                     pub.append(BaseModel.AddTR());
                 
                 
                 UIContralType uct=attr.getUIContralType();
                 
                 if(uct==UIContralType.TB){
                	 if (attr.getUIHeight() != 0)
                     {
                         if (val.toString().length() == 0 && en.getIsEmpty() == false && "Doc".equals(attr.getKey()))
                             val = en.GetValDocText();

                         int type =0; // EnsAppCfgs.GetValInt(en.toString() + "s", "EditerType");
                         if(type==EditerType.None.getValue()){
                        	 /* doc 文本类型。　*/
                             if (attr.getUIIsReadonly() || isReadonly)
                             {
                                 TextBox areaR = uf.creatTextBox("TB_" + attr.getKey());
                                 areaR.setText(val.toString());
                                 areaR.setRows(8);
                                 areaR.setTextMode(TextBoxMode.MultiLine);
                                 areaR.setReadOnly(true);
                                 this.AddContral(attr.getDescHelper(), areaR);
                             }
                             else
                             {
                                 TextBox area = uf.creatTextBox("TB_" + attr.getKey());
                                 area.LoadMapAttr(attr);
//                                 area.setId("TB_" + attr.getKey());
                                 area.setText(val.toString());
                                 area.setRows(8);
                                 area.setTextMode(TextBoxMode.MultiLine);
                                 area.setIsHelpKey("1");
                                 this.AddContral(attr.getDescHelper(), area);
                             }
                         }
                         else if(type==EditerType.Sina.getValue()){
                        	 
                         }
                         else if(type==EditerType.FKEditer.getValue()){
                        	 /* doc 文本类型。　*/
                        	 TextBox input=uf.creatTextBox("txtContent");
                        	 input.setTextMode(TextBoxMode.Hidden);
//                             input.addAttr("id","txtContent");
                             //input.ID = attr.Key;
                             pub.append("<td class='FDesc' colspan='2' nowrap width='50%' >" + attr.getDesc() + "<br>");
                             pub.append(input);
                             pub.append("<iframe ID='eWebEditor1' src='./Ctrl/Edit/editor.htm?id=txtContent&style=coolblue' frameborder='0' scrolling='no' width='600' HEIGHT='350'></iframe>");
                             pub.append(this.AddTDEnd());
                         }
                         else{
                        	 
                         }
                         
                     }
                     else
                     {
                         TextBox tb = uf.creatTextBox("TB_" + attr.getKey());
//                         tb.setId("TB_" + attr.getKey());
                         switch (attr.getMyDataType())
                         {
                             case DataType.AppRate:
                             case DataType.AppMoney:
                                 try
                                 {
                                     tb.setText(""+Float.parseFloat(val.toString()));
                                     tb.addAttr("Class","TBNum");
                                 }
                                 catch(Exception e)
                                 {
                                     tb.setText(""+Float.parseFloat("0"));
                                     tb.addAttr("Class","TBNum");
                                     //  Log.DebugWriteWarning( "@"+en.toString()+"Attr="+attr.Key=" Val="+val +" Ex="+ex.Message );
                                 }
                                 tb.setShowType(TBType.Moneny);
                                 break;
                             case DataType.AppInt:
                             case DataType.AppFloat:
                             case DataType.AppDouble:
                                 tb.addAttr("Class","TBNum");
                                 tb.setShowType(TBType.Float);
                                 tb.setText(val.toString());
                                 break;
                             case DataType.AppDate:
                                 tb.setText(val.toString());
                                 tb.setShowType(TBType.Date);
                                 tb.setIsHelpKey("1");
                                 if (attr.getUIIsReadonly() == false)
                                     tb.addAttr("onfocus","WdatePicker();");
                                 break;
                             case DataType.AppDateTime:
                                 tb.setText(val.toString());
                                 tb.setShowType(TBType.Date);
                                 tb.setIsHelpKey("1");
                                 if (attr.getUIIsReadonly() == false)
                                     tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
                                 break;
                             default:
                                 tb.setText(val.toString());
                                 tb.addAttr("Class","TB");
                                 break;
                         }

                         if (isReadonly || attr.getUIIsReadonly())
                         {
                             tb.setReadOnly(true);
                         }

                         if (attr.getIsPK() && val.toString().equals("") == false)
                         {
                             tb.setReadOnly(true);
                         }


                         if (attr.getKey().equals(noKey) && attr.getUIIsReadonly() == false && attr.getUIVisible() == true)
                         {
                             String path = this.get_request().getRealPath("/");
                             //String helpScript = "";
                             String helpScript = "<a href=\"javascript:alert('请在文本框上双击，系统会出现查找窗口。');\" ><img src='./Img/Btn/Search.gif' border=0 /></a>";
                             tb.addAttr("ondblclick","OpenHelperTBNo('" + path + "','BP.Port.Taxpayers',this)");
                             this.AddContral(attr.getDescHelper(), tb, helpScript);
                         }
                         else
                         {
                             this.AddContral(attr.getDescHelper(), tb);
                         }
                     }
                 }
                 else if(uct==UIContralType.DDL){
                	 if (isReadonly || !attr.getUIIsReadonly())
                     {
                         DDL ddl = uf.creatDDL("DDL_" + attr.getKey());
//                         ddl.setId("DDL_" + attr.getKey());
                         ddl.setCssClass("DDL" + WebUser.getStyle());
                         String text = en.GetValRefTextByKey(attr.getKey());
                         if (text == null || "".equals(text))
                             text = val.toString();

                         ListItem li = new ListItem(text, val.toString());
                         li.addAttr("Class","TB");
                         ddl.Items.add(li);
                         ddl.setEnabled(false);
                         this.AddContral(attr.getDescHelper(), ddl, true);
                     }
                     else
                     {
                    	 /* 可以使用的情况. */
                         if (attr.getUIDDLShowType() == DDLShowType.BindTable)
                         {
                             DDL ddlSQL = new DDL();
                             ddlSQL.setId("DDL_" + attr.getKey());
                             String sql = attr.getUIBindKey();
                             if (sql.contains("@Web") == true)
                             {
                                 sql = sql.replace("@WebUser.No", BP.Web.WebUser.getNo());
                                 sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
                                 sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
                                 sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
                             }

                             if (sql.contains("@") == true)
                             {
                                 for(Attr myattr : en.getEnMap().getAttrs())
                                 {
                                     sql = sql.replace("@" + myattr.getKey(), en.GetValStrByKey(myattr.getKey()));
                                     if (sql.contains("@") == false)
                                         break;
                                 }
                             }

                             //求出数据源,并执行绑定.
                             DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
                             ddlSQL.Bind(dt, "No", "Name", val.toString());

                             this.AddContral(attr.getDescHelper(), ddlSQL, true,3);
                         }
                         else
                         {
                        	 /* 可以使用的情况. */
                        	 DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
                        	 ddl1.setName("DDL_" + attr.getKey());
                        	 this.AddContral(attr.getDescHelper(), ddl1, true);
                         }
                     }

                 }
                 else if(uct==UIContralType.CheckBok){
                	 CheckBox cb = uf.creatCheckBox("CB_" + attr.getKey());
                     cb.setChecked(en.GetValBooleanByKey(attr.getKey()));

                     if (isReadonly)
                         cb.setEnabled(false);
                     else
                         cb.setEnabled(attr.getUIIsReadonly());

//                     cb.setId("CB_" + attr.getKey());
                     cb.setText(attr.getDesc());
                     if (attr.HelperUrl == null)
                         this.AddContral("", cb);
                     else
                         this.AddContral(attr.getDescHelperIcon(), cb);
                 }
                 else{
                	 
                 }
                 
                 
                

                 if (isLeft == false)
                     pub.append(this.AddTREnd());
                 isLeft = !isLeft;

                 //#region 增加字段相关功能.
                 if (rmKeys.contains("," + attr.getKey() + ",") == true)
                 {
                     //#region 生成连接串.
                     String html = "";
                     for (RefMethod func:rms)
                     {
                         if (func.RefAttrKey != attr.getKey())
                             continue;
                         func.HisEn = en;
                         if (func.refMethodType == RefMethodType.Func)
                         {
                             /*如果是功能.*/
                        	 SimpleDateFormat sdf=new SimpleDateFormat("MMddhhmmss");
                             String mykeys = "&" + en.getPK() + "=" + en.getPKVal() + "&r=" + sdf.format(new Date());
                             String url = "../RefMethod.jsp?Index=" + func.Index + "&EnsName=" + en.getGetNewEntities().toString() + mykeys;
                             if (func.Warning == null)
                             {
                                 if (func.Target == null)
                                     html = "<a href='" + url + "' ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>";
                                 else
                                     html = "<a href=\"javascript:WinOpen('" + url + "','" + func.Target + "')\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>";
                             }
                             else
                             {
                                 if (func.Target == null && func.equals(""))
                                     html = "<a href=\"javascript: if ( confirm('" + func.Warning + "') ) { window.location.href='" + url + "' }\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>";
                                 else
                                     html = "<a href=\"javascript: if ( confirm('" + func.Warning + "') ) { WinOpen('" + url + "','" + func.Target + "') }\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>";
                             }
                         }
                         else
                         {
                             /* 如果是link.*/
                        	 String myurl = null; 
                             try {
                            	 myurl= func.Do(null).toString();
							} catch (Exception e) {
								;
							}
                             int h = func.Height;

                             if (func.Target == null && func.equals(""))
                                 html = "<a href='" + myurl + "' ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>";
                             else
                                 html = "<a href=\"javascript:WinOpen('" + myurl + "','" + func.Target + "')\" ToolTip='" + func.ToolTip + "' >" + func.Title + "</a>";
                         }


                     }

                     if (isLeft == false)
                     {
                         //this.AddTDDesc("");
                    	 pub.append(this.AddTD("colspan=2", html));
                    	 pub.append(this.AddTREnd());
                     }
                     else
                     {
                    	 pub.append(this.AddTR());
                    	 pub.append(this.AddTDDesc(""));
                    	 pub.append(this.AddTD("colspan=3", html));
                    	 pub.append(this.AddTREnd());
                     }

                     isLeft = true;
                   
                 }
                 //#endregion 增加字段相关功能.

             }

             // 结束循环.
             if (isLeft == false)
             {
                 AddContral();
                 pub.append(this.AddTREnd());
             }
            

             //#region 查看是否包含 MyFile字段如果有就认为是附件。
            /* if (en.getEnMap().getAttrs().Contains("MyFileName"))
             {
                  如果包含这二个字段。
                 String fileName = en.GetValStringByKey("MyFileName");
                 String filePath = en.GetValStringByKey("MyFilePath");
                 String fileExt = en.GetValStringByKey("MyFileExt");
                 String url = "";
                 if (fileExt != "")
                 {
                     // 系统物理路径。
                     String path = this.get_request().getRealPath("/");
                     String path1 = filePath.toLowerCase();
                     path1 = path1.replace(path, "");
                     // url = "&nbsp;&nbsp;<a href='" + cf.FJWebPath + "/" + en.PKVal + "." + fileExt + "' target=_blank ><img src='"+this.Request.ApplicationPath+"Images/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
                     url = "&nbsp;&nbsp;<a href='../Do.jsp?DoType=DownFile&EnName=" + enName + "&PK=" + en.getPKVal() + "' target=_blank ><img src='./../../Img/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
                 }

                 pub.append(this.AddTR());
                 pub.append(this.AddTD("nowrap=true class='FDesc' ", en.getEnMap().GetAttrByKey("MyFileName").getDesc()));

                 TextBox file=new TextBox();
                 file.setTextMode(TextBoxMode.Files);
                 file.setId("file");
                 pub.append(AddTD(file));

                 pub.append(this.AddTDBegin("colspan=2"));
                 pub.append(url);
                 if (fileExt.equals(""))
                 {
                    ImageButton btn1 = new ImageButton();
                    btn1.setImageUrl("./../../Img/Btn/Delete.gif");

                     //  btn1.Text = "移除";
                     btn1.addAttr("Class", "Btn1");
                     btn1.setId("Btn_DelFile");
                     btn1.addAttr("onclick", btn1.attributes.get("onclick")+" return confirm('此操作要执行移除附件，是否继续？');");
                     pub.append(btn1.toString());
                 }
                 pub.append(this.AddTDEnd());
                 pub.append(this.AddTREnd());
             } //结束字段内的分组循环.
*/
             pub.append(this.AddTableEnd());
             pub.append(this.AddDivEnd());
         } // 结束分组循环.
         if (haveFile) {
 			//this.Add(String.format("<div title='%1$s' data-g='%1$s' data-gd='%2$s'>", "附件管理", ""));
        	 pub.append(this.Add(String.format("<div title='"+garr[0]+"' data-g='"+garr[0]+"' data-gd='"+garr[0]+"'>")));
        	 pub.append(this.AddTable("class='Table' cellpadding='0' cellspacing='0' style='width:100%;'"));

 			//将附件增加于此，2015-8-4，added by liuxc
 			if (en.getEnMap().getAttrs().Contains("MyFileName")) {
 				String fileName = en.GetValStringByKey("MyFileName");
 				String filePath = en.GetValStringByKey("MyFilePath");
 				String fileExt = en.GetValStringByKey("MyFileExt");
 				String url = "";

 				if (!fileExt.equals("")) {
 					// 系统物理路径。
 					 String path = this.get_request().getRealPath("/");
                     String path1 = filePath.toLowerCase();
 					path1 = path1.replace(path, "");
 					// url = "&nbsp;&nbsp;<a href='" + cf.FJWebPath + "/" + en.PKVal + "." + fileExt + "' target=_blank ><img src='"+this.Request.ApplicationPath+"Images/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
 					url = "&nbsp;&nbsp;<a href='../Do.jsp?DoType=DownFile&EnName=" + enName + "&PK=" + en.getPKVal() + "' target=_blank ><img src='./../../Img/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
 				}

 				this.AddTR();
 				this.AddTD("nowrap=true class='FDesc' ", en.getEnMap().GetAttrByKey("MyFileName").getDesc());

 				/*HtmlInputFile file = new HtmlInputFile();
 				file.ID = "file";
 				this.AddTD(file);*/

 				this.AddTDBegin("colspan=2");
 				this.Add(url);
 				if (!fileExt.equals("")) {
 					 ImageButton btn1 = new ImageButton();
                     btn1.setImageUrl("./../../Img/Btn/Delete.gif");

                      //  btn1.Text = "移除";
                      btn1.addAttr("Class", "Btn1");
                      btn1.setId("Btn_DelFile");
                      btn1.addAttr("onclick", btn1.attributes.get("onclick")+" return confirm('此操作要执行移除附件，是否继续？');");
                      pub.append(btn1.toString());
 				}

 				this.AddTDEnd();
 				this.AddTREnd();
 			}

         /*pub.append(this.AddDivEnd());

         //#region 绑定属性控件。
         pub.append(this.AddTable());*/
         AttrFiles fileAttrs = en.getEnMap().getHisAttrFiles();
         SysFileManagers sfs = new SysFileManagers(en.toString(), en.getPKVal().toString());
         for(AttrFile attrF:fileAttrs)
         {
             pub.append(this.AddTR());
             pub.append(this.AddTD("nowrap=true class='FDesc' ", attrF.FileName));
             TextBox file = new TextBox();
             file.setTextMode(TextBoxMode.Files);
             file.setId("F" + attrF.FileNo);
             file.addAttr("width","100%");
             pub.append(this.AddTD(file));

             /* 判断是否有文件没有文件就移除它。*/
             SysFileManager sf = (SysFileManager)sfs.GetEntityByKey(SysFileManagerAttr.AttrFileNo, attrF.FileNo);
             if (sf == null)
             {
                 pub.append(this.AddTD());
                 pub.append(this.AddTD());
             }
             else
             {
                 pub.append("<TD class=TD colspan=2>");
                 String lab = "&nbsp;<a href='" + cf.getFJWebPath() + "/" + sf.getOID() + "." + sf.getMyFileExt() + "' target=_blank ><img src='../Images/FileType/" + sf.getMyFileExt() + ".gif' border=0 />" + sf.getMyFileName() + "." + sf.getMyFileExt() + "</a>";
                 pub.append(lab);

                 ImageButton btn_m = new ImageButton();
                 btn_m.setImageUrl("./Img/Btn/Delete.gif");

                 //btn_m.ImageUrl = "./Img/Btn/Del.gif";
                 btn_m.addAttr("Class", "Btn1");
                 btn_m.setId("Btn_DelFile" + attrF.FileNo);
                 btn_m.addAttr("onclick", btn_m.attributes.get("onclick")+" return confirm('此操作要执行移除附件，是否继续？');");
                 pub.append(btn_m);
                 pub.append(this.AddTDEnd());
             }
             pub.append(this.AddTREnd());
         }
         //#endregion 绑定属性控件。

         if (en.getEnMap().getAttrs().Contains("MyFileNum"))
         {
        	 pub.append(this.AddTR());
        	 pub.append(this.AddTD("nowrap=true class='FDesc' ", " "));
             if (en.getPKVal() == null || "".equals(en.getPKVal()) || "0".equals(en.getPKVal()))
            	 pub.append("<TD  colspan=3 ><a href=\"javascript:alert('请在保存后在执行。');\" target=_self>附件批量上传(请在保存后在执行)</a></TD>");
             else
            	 pub.append("<TD  colspan=3 ><a href=\"../FileManager.jsp?EnName=" + en.toString() + "&PK=" + en.getPKVal() + "\" target=_self >附件批量上传&编辑</a></TD>");
             pub.append(this.AddTREnd());
         }
         pub.append(this.AddTableEnd());
         pub.append(this.AddDivEnd());
		}

         
         pub.append(this.AddTableEnd());
         if (isShowDtl == false)
             return;

         int num = map.getAttrsOfOneVSM().size() + map.getHisRefMethods().size() + map.getDtls().size();
         if (num > 0 && en.IsExit(en.getPK(), en.getPKVal()) == false)
         {
             String endMsg = "";
           /*  // 增加相关信息
             AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
             for(AttrOfOneVSM vsM:oneVsM)
                 endMsg += "[<a href=\"javascript:alert('请在保存后填写。');\" >" + vsM.getDesc() + "</a>]";

             RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
             for(RefMethod func:myreffuncs)
             {
                 if (func.Visable == false)
                     continue;
                 endMsg += "[<a href=\"javascript:alert('请在保存后执行。');\" >" + func.Title + "</a>]";
             }

             if (isShowDtl)
             {
                 EnDtls enDtls = en.getEnMap().getDtls();
                 for(EnDtl enDtl:enDtls)
                     endMsg += "[<a href=\"javascript:alert('请在保存后填写。')\" >" + enDtl.getDesc() + "</a>]";
             }

             if (!endMsg.equals(""))
             {
            	 pub.append("<table border=0><TR><TD class=TD><font style='font-size:12px' >" + endMsg + "</font></TD></TR></table>");
             }*/
             return;
         }
         String refstrs = "";
         if (en.equals(""))
         {
             refstrs += "";
             return;
         }
         String keys = "&PK=" + en.getPKVal().toString();
         for(Attr attr:en.getEnMap().getAttrs())
         {
             if (attr.getMyFieldType() == FieldType.Enum ||
                 attr.getMyFieldType() == FieldType.FK ||
                 attr.getMyFieldType() == FieldType.PK ||
                 attr.getMyFieldType() == FieldType.PKEnum ||
                 attr.getMyFieldType() == FieldType.PKFK)
                 keys += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
         }


         Entities hisens = en.getGetNewEntities();
         SimpleDateFormat sdf=new SimpleDateFormat("ddhhmmss");
         keys += "&r=" + sdf.format(new Date());
         if (isShowDtl)
             refstrs = GetRefstrs(keys, en, en.getGetNewEntities());
         if (!refstrs.equals(""))
         {
        	 pub.append("<font style='font-size:12px' >" + refstrs + "</font>");
             // this.Add("<HR><table class=Table border=0 ><TR class=TR ><TD class=TD ><font style='font-size:12px' >" + refstrs + "</font></TD></TR></table>");
         }
         
         this.get_request().setAttribute("EnsName", getEnsName());
         //this.Add(refstrs);
     }
     //#endregion 绑定v2.

     public void Bind(Entity en, String enName, boolean isReadonly, boolean isShowDtl)
     {
         this.Bind(en, enName, isReadonly, isShowDtl, null);
     }
     public void Bind(Entity en, String enName, boolean isReadonly, boolean isShowDtl, String noKey)
     {
         if (enName == null)
             enName = en.toString();

         this.HisEn = en;
         this.setReadonly(isReadonly);
         this.setShowDtl(isShowDtl);
         this.Controls.clear();
         pub.append(this.AddTable("class='Table cellpadding='1' cellspacing='1' border='1' style='width:100%'"));
         boolean isLeft = true;
         Object val = null;
         Map map = en.getEnMap();
         Attrs attrs = map.getAttrs();
         EnCfg cf = new EnCfg(enName);
         String[] gTitles = cf.getGroupTitle().split("@");
         for (Attr attr:attrs)
         {
             if (attr.getKey().equals("MyNum"))
                 continue;
             if (attr.getMyFieldType() == FieldType.RefText)
                 continue;

             val = en.GetValByKey(attr.getKey());
             if (attr.getUIVisible() == false)
             {
                 this.SetValByKey(attr.getKey(), val.toString());
                 continue;
             }
             for (String g:gTitles)
             {
                 if (g.contains(attr.getKey() + "="))
                 {
                     if (isLeft == false)
                     {
                         pub.append(this.AddTD());
                         pub.append(this.AddTD());
                         pub.append(this.AddTREnd());
                     }

                     String[] ss = g.split("=");
                     pub.append(this.AddTRSum());
                     pub.append(this.AddTD("colspan=4 class=FDesc", "<b>" + ss[1] + "</b>"));
                     pub.append(this.AddTREnd());
                     isLeft = true;
                     break;
                 }
             }

             //#region 判断是否单列显示
             if (attr.UIIsLine)
             {
                 if (isLeft == false)
                 {
                     pub.append(BaseModel.AddTD());
                     pub.append(BaseModel.AddTD());
                     pub.append(BaseModel.AddTREnd());
                     isLeft = true;
                 }

                 pub.append(this.AddTR());
                 if (attr.getUIHeight() != 0)
                 {
                     /*大块文本采集, 特殊处理。*/
                     if (val.toString().length() == 0 && en.equals("") == false && "Doc".equals(attr.getKey()))
                         val = en.GetValDocText();

                     /* doc 文本类型。　*/
                     if (attr.getUIIsReadonly() || isReadonly)
                     {
                         TextBox areaR = uf.creatTextBox("TB_" + attr.getKey());
//                         areaR.setId("TB_" + attr.getKey());
                         areaR.setText(val.toString());
                         areaR.setRows(5);
                         areaR.setTextMode(TextBoxMode.MultiLine);
                         areaR.setReadOnly(true);
                         this.AddContral(attr.getDesc(), areaR, 4);
                     }
                     else
                     {
                         int type =0 ;// EnsAppCfgs.GetValInt(en.toString() + "s", "EditerType");
                         if(type==EditerType.FKEditer.getValue()){
                        	 
                         }
                         else if(type==EditerType.Sina.getValue()){
                        	 TextBox input=uf.creatTextBox("TBH_" + attr.getKey());
                        	 input.setTextMode(TextBoxMode.Hidden);
                             //  input.Attributes["id"] = "TBH_"+attr.Key;
//                             input.setId("TBH_" + attr.getKey());
                             input.setText(val.toString());
                             pub.append("<td class='FDesc'  colspan='4' width='50%'>");
                             pub.append(input.toString());
                             pub.append("<iframe ID='eWebEditor1' src='./Ctrl/editor/editor.htm?id=" + input.getId() + "&style=coolblue' frameborder='0' scrolling='no' width='600' HEIGHT='350'></iframe>");
                             pub.append(AddTDEnd());
                         }
                         else{
                        	 TextBox area = uf.creatTextBox("TB_" + attr.getKey());
                             area.LoadMapAttr(attr);
//                             area.setId("TB_" + attr.getKey());
                             area.setText(val.toString());
                             area.setRows(5);
                             area.setTextMode(TextBoxMode.MultiLine);
                             area.setIsHelpKey("1");
                             this.AddContral(attr.getDesc(), area, 4);
                         }
                        
                     }
                     pub.append(this.AddTREnd());

                     isLeft = true;
                     continue;
                 }
                 
                 UIContralType ut=attr.getUIContralType();
                 if(ut==UIContralType.TB){
                	 TextBox tb = uf.creatTextBox("TB_" + attr.getKey());
//                     tb.setId("TB_" + attr.getKey());
                     // tb.LoadMapAttr(attr);
                     switch (attr.getMyDataType())
                     {
                         case DataType.AppMoney:
                             tb.setText(""+Float.parseFloat(val.toString()));
                             tb.addAttr("Class","TBNum");
                             tb.setShowType(TBType.Moneny);
                             break;
                         case DataType.AppInt:
                         case DataType.AppFloat:
                         case DataType.AppDouble:
                         case DataType.AppRate:
                             tb.addAttr("Class","TBNum");
                             tb.setText(val.toString());
                             tb.setShowType(TBType.Num);
                             break;
                         case DataType.AppDate:
                             tb.setText(val.toString());
                             tb.setShowType(TBType.Date);
                             tb.addAttr("Class","TBcalendar");
                             tb.setIsHelpKey("1");
                             //if (attr.getUIIsReadonly() == false)
                                 tb.addAttr("onfocus","WdatePicker();");
                             break;
                         case DataType.AppDateTime:
                             tb.setText(val.toString());
                             tb.setShowType(TBType.Date);
                             tb.addAttr("Class","TBcalendar");
                             tb.setIsHelpKey("1");
                             //if (attr.getUIIsReadonly() == false)
                                 tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
                             break;
                         default:
                             tb.setText(val.toString());
                             tb.addAttr("Class","TB");
                             break;
                     }

                     if (isReadonly || attr.getUIIsReadonly())
                         tb.setReadOnly( true);

                     tb.setEnabled(true);
                     if (attr.getKey().equals(noKey) && attr.getUIIsReadonly() == false && attr.getUIVisible() == true)
                     {
                         String path = this.get_request().getRealPath("/");
                         //String helpScript = "";
                         String helpScript = "<a href=\"javascript:alert('请在文本框上双击，系统会出现查找窗口。');\" ><img src='./Img/Btn/Search.gif' border=0 /></a>";
                         tb.addAttr("ondblclick","OpenHelperTBNo('" + path + "','BP.Port.Taxpayers',this)");
                         this.AddContral(attr.getDesc(), tb, helpScript, 3);
                     }
                     else
                     {
                         tb.setCols(80);
                         this.AddContral(attr.getDesc(), tb, 3);
                     }
                 }
                 else if(ut==UIContralType.DDL){
                	 if (isReadonly || attr.getUIIsReadonly())
                     {
                         DDL ddl = uf.creatDDL("DDL_" + attr.getKey());
                         ddl.setId("DDL_" + attr.getKey());                       
                         String text = en.GetValRefTextByKey(attr.getKey());
                         if (text == null || text.equals(""))
                             text = val.toString();

                         ListItem li = new ListItem(text, val.toString());
                         li.addAttr("Class","TB");
                         ddl.Items.add(li);
                         ddl.setEnabled(false);
                         this.AddContral(attr.getDesc(), ddl, true, 3);
                     }
                     else
                     {
                         /* 可以使用的情况. */
                         DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
                         ddl1.setId("DDL_" + attr.getKey());
                         ddl1.setName("DDL_" + attr.getKey());
                         this.AddContral(attr.getDesc(), ddl1, true,1);
                     }
                 }
                 else if(ut==UIContralType.CheckBok){
                	 CheckBox cb = uf.creatCheckBox("CB_" + attr.getKey());
                     cb.setChecked(en.GetValBooleanByKey(attr.getKey()));
//                     cb.setId("CB_" + attr.getKey());

                     if (isReadonly)
                         cb.setEnabled(false);
                     else
                         cb.setEnabled(attr.getUIIsReadonly());

                     cb.setText(attr.getDesc());
                     this.AddContral("", cb, 3);
                 }
                 
//               
                 pub.append(this.AddTREnd());
                 isLeft = true;
                 continue;
             }
             //#endregion 判断是否单列显示 // 结束要显示单行的情况。

             if (isLeft)
            	 pub.append(this.AddTR());

             UIContralType ut=attr.getUIContralType();
             
             if(ut==UIContralType.TB){
            	 if (attr.getUIHeight() != 0)
                 {
                     if (val.toString().length() == 0 && en.equals("") == false && "Doc".equals(attr.getKey()))
                         val = en.GetValDocText();

                     int type =0 ;// EnsAppCfgs.GetValInt(en.toString() + "s", "EditerType");
                     if(type==EditerType.None.getValue()){
                    	 /* doc 文本类型。　*/
                         if (attr.getUIIsReadonly() || isReadonly)
                         {
                             TextBox areaR = uf.creatTextBox("TB_" + attr.getKey());
//                             areaR.setId("TB_" + attr.getKey());
                             areaR.setText(val.toString());
                             areaR.setRows(8);
                             areaR.setTextMode(TextBoxMode.MultiLine);
                             areaR.setReadOnly(true);
                             this.AddContral(attr.getDescHelper(), areaR);
                         }
                         else
                         {
                             TextBox area = uf.creatTextBox("TB_" + attr.getKey());
                             area.LoadMapAttr(attr);
//                             area.setId("TB_" + attr.getKey());
                             area.setText(val.toString());
                             area.setRows(8);
                             area.setTextMode(TextBoxMode.MultiLine);
                             area.setIsHelpKey("1");
                             this.AddContral(attr.getDescHelper(), area);
                         }
                     }
                     else if(type==EditerType.Sina.getValue()){
                    	 
                     }
                     else if(type==EditerType.FKEditer.getValue()){
                    	 /* doc 文本类型。　*/
                    	 TextBox tb=uf.creatTextBox("txtContent");
                    	 tb.setTextMode(TextBoxMode.Hidden);
//                    	 tb.addAttr("id", "txtContent");
                    	 pub.append("<td class='FDesc'  colspan='2' nowrap width='50%' >" + attr.getDesc() + "<br>");
                    	 pub.append(tb);
                    	 pub.append("<iframe ID='eWebEditor1' src='./Ctrl/Edit/editor.htm?id=txtContent&style=coolblue' frameborder='0' scrolling='no' width='600' HEIGHT='350'></iframe>");
                    	 pub.append(AddTDEnd());

                     }
                 }
                 else
                 {
                     TextBox tb = uf.creatTextBox("TB_" + attr.getKey());
//                     tb.setId("TB_" + attr.getKey());
                     switch (attr.getMyDataType())
                     {
                         case DataType.AppRate:
                         case DataType.AppMoney:
                             try
                             {
                                 tb.setText(""+Float.parseFloat(val.toString()));
                                 tb.addAttr("Class","TBNum");
                             }
                             catch(Exception e)
                             {
                                 tb.setText(""+Float.parseFloat("0"));
                                 tb.addAttr("Class","TBNum");
                                 //  Log.DebugWriteWarning( "@"+en.toString()+"Attr="+attr.Key=" Val="+val +" Ex="+ex.Message );
                             }
                             tb.setShowType(TBType.Moneny);
                             break;
                         case DataType.AppInt:
                         case DataType.AppFloat:
                         case DataType.AppDouble:
                             tb.addAttr("Class","TBNum");
                             tb.setShowType(TBType.Float);
                             tb.setText(val.toString());
                             break;
                         case DataType.AppDate:
                             tb.setText(val.toString());
                             tb.setShowType(TBType.Date);
                             tb.setIsHelpKey("1");
                             //if (attr.getUIIsReadonly() == false)
                                 tb.addAttr("onfocus","WdatePicker();");
                             break;
                         case DataType.AppDateTime:
                             tb.setText(val.toString());
                             tb.setShowType(TBType.Date);
                             tb.setIsHelpKey("1");
                             //if (attr.getUIIsReadonly() == false)
                                 tb.addAttr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
                             break;
                         default:
                             tb.setText(val.toString());
                             tb.addAttr("Class", "TB");
                             break;
                     }

                     if (isReadonly || attr.getUIIsReadonly())
                         tb.setReadOnly(true);

                     if (attr.getIsPK() && tb.getText().equals("")==false)
                         tb.setReadOnly(true);

                     if (attr.getKey().equals(noKey) && attr.getUIIsReadonly() == false && attr.getUIVisible() == true)
                     {
                         String path = this.get_request().getRealPath("/");
                         //String helpScript = "";
                         String helpScript = "<a href=\"javascript:alert('请在文本框上双击，系统会出现查找窗口。');\" ><img src='./Img/Btn/Search.gif' border=0 /></a>";
                         tb.addAttr("ondblclick","OpenHelperTBNo('" + path + "','BP.Port.Taxpayers',this)");
                         this.AddContral(attr.getDescHelper(), tb, helpScript);
                     }
                     else
                     {
                         this.AddContral(attr.getDescHelper(), tb);
                     }
                 }
             }
             else if(ut==UIContralType.DDL){
            	 // 小周鹏修正，与ccflow翻译不一致 Start
            	 if (isReadonly || !attr.getUIIsReadonly())
//            	 if (isReadonly || attr.getUIIsReadonly())
            		// 小周鹏修正，与ccflow翻译不一致 End
                 {
                     DDL ddl = uf.creatDDL("DDL_" + attr.getKey());
//                     ddl.setId("DDL_" + attr.getKey());
                     ddl.setCssClass("DDL" + WebUser.getStyle());
                     String text = en.GetValRefTextByKey(attr.getKey());
                     if (text == null || text.equals(""))
                         text = val.toString();

                     ListItem li = new ListItem(text, val.toString());
                     li.addAttr("Class","TB");
                     ddl.Items.add(li);
                     ddl.setEnabled(false);
                     this.AddContral(attr.getDescHelper(), ddl, true);
                 }
                 else
                 {
                	 /* 可以使用的情况. */
                     if (attr.getUIDDLShowType() == DDLShowType.BindTable)
                     {
                         DDL ddlSQL = new DDL();
                         ddlSQL.setId("DDL_" + attr.getKey());
                         String sql = attr.getUIBindKey();
                         if (sql.contains("@Web") == true)
                         {
                             sql = sql.replace("@WebUser.No", BP.Web.WebUser.getNo());
                             sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
                             sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
                             sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
                         }

                         if (sql.contains("@") == true)
                         {
                             for(Attr myattr : en.getEnMap().getAttrs())
                             {
                                 sql = sql.replace("@" + myattr.getKey(), en.GetValStrByKey(myattr.getKey()));
                                 if (sql.contains("@") == false)
                                     break;
                             }
                         }

                         //求出数据源,并执行绑定.
                         DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
                         ddlSQL.Bind(dt, "No", "Name", val.toString());

                         this.AddContral(attr.getDescHelper(), ddlSQL, true,3);
                     }
                     else
                     {
                         DDL ddl1 = new DDL(attr, val.toString(), "enumLab", true, this.get_request().getRealPath("/"));
                         ddl1.setId("DDL_" + attr.getKey());
                         ddl1.setName("DDL_" + attr.getKey());
                         this.AddContral(attr.getDescHelper(), ddl1, true,3);
                     }

                     ///* 可以使用的情况. */
                     //DDL ddl1 = new DDL(attr, val.ToString(), "enumLab", true, this.Page.Request.ApplicationPath);
                     //ddl1.ID = "DDL_" + attr.Key;
                     //this.AddContral(attr.Desc, ddl1, true, 3);
                 }
             }
             else if(ut==UIContralType.CheckBok){
            	 CheckBox cb =uf.creatCheckBox("CB_" + attr.getKey());
                 cb.setChecked(en.GetValBooleanByKey(attr.getKey()));

                 if (isReadonly)
                     cb.setEnabled(false);
                 else
                     cb.setEnabled(attr.getUIIsReadonly());

//                 cb.setId("CB_" + attr.getKey());
                 cb.setText(attr.getDesc());
                 if (attr.HelperUrl == null)
                     this.AddContral("", cb);
                 else
                     this.AddContral(attr.getDescHelperIcon(), cb);
             }
      

             if (isLeft == false)
            	 pub.append(this.AddTREnd());
             isLeft = !isLeft;
         }

         // 结束循环.
         if (isLeft == false)
         {
             AddContral();
             pub.append(this.AddTREnd());
         }

         //#region 查看是否包含 MyFile字段如果有就认为是附件。
         if (en.getEnMap().getAttrs().Contains("MyFileName"))
         {
             /* 如果包含这二个字段。*/
             String fileName = en.GetValStringByKey("MyFileName");
             String filePath = en.GetValStringByKey("MyFilePath");
             String fileExt = en.GetValStringByKey("MyFileExt");
             String url = "";
             if (!fileExt.equals(""))
             {
                 // 系统物理路径。
                 String path = this.get_request().getRealPath("/");
                 String path1 = filePath.toLowerCase();
                 path1 = path1.replace(path, "");
                 // url = "&nbsp;&nbsp;<a href='" + cf.FJWebPath + "/" + en.PKVal + "." + fileExt + "' target=_blank ><img src='"+this.Request.ApplicationPath+"Images/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
                 url = "&nbsp;&nbsp;<a href='../Do.jsp?DoType=DownFile&EnName=" + enName + "&PK=" + en.getPKVal() + "' target=_blank ><img src='./../../Img/FileType/" + fileExt + ".gif' border=0 />" + fileName + "</a>";
             }

             pub.append(this.AddTR());
             pub.append(this.AddTD("nowrap=true class='FDesc' ", en.getEnMap().GetAttrByKey("MyFileName").getDesc()));

             TextBox tb= new TextBox();
             tb.setTextMode(TextBoxMode.Files);
             tb.setId("file");
             pub.append(AddTD(tb));

             pub.append(this.AddTDBegin("colspan=2"));
             pub.append(url);
             if (!fileExt.equals(""))
             {
            	 ImageButton btn1=new ImageButton();
                 btn1.setImageUrl("./../../Img/Btn/Delete.gif");
                 //  btn1.Text = "移除";
                 btn1.addAttr("Class", "Btn1");
                 btn1.setId("Btn_DelFile");
                 btn1.addAttr("onclick"," return confirm('此操作要执行移除附件，是否继续？');");
                 pub.append(btn1);
             }
             pub.append(this.AddTDEnd());

             pub.append(this.AddTREnd());
         }

         //#region 绑定属性控件。
         AttrFiles fileAttrs = en.getEnMap().getHisAttrFiles();
         SysFileManagers sfs = new SysFileManagers(en.toString(), en.getPKVal().toString());
         for (AttrFile attrF:fileAttrs)
         {
        	 pub.append(this.AddTR());
        	 pub.append(this.AddTD("nowrap=true class='FDesc' ", attrF.FileName));
             
             TextBox file = new TextBox();
             file.setId("F" + attrF.FileNo);
             file.addAttr("width","100%");
             pub.append(AddTD(file.toString()));

             /* 判断是否有文件没有文件就移除它。*/
             SysFileManager sf = (SysFileManager)sfs.GetEntityByKey(SysFileManagerAttr.AttrFileNo, attrF.FileNo);
             if (sf == null)
             {
            	 pub.append(this.AddTD());
            	 pub.append(this.AddTD());
             }
             else
             {
                 pub.append("<TD class=TD colspan=2>");
                 String lab = "&nbsp;<a href='" + cf.getFJWebPath() + "/" + sf.getOID() + "." + sf.getMyFileExt() + "' target=_blank ><img src='../Images/FileType/" + sf.getMyFileExt() + ".gif' border=0 />" + sf.getMyFileName() + "." + sf.getMyFileExt() + "</a>";
                 pub.append(lab);
                
                 ImageButton btn_m = new ImageButton();
                 btn_m.setImageUrl("./Img/Btn/Delete.gif");

                 //btn_m.ImageUrl = "./Img/Btn/Del.gif";
                 btn_m.addAttr("Class", "Btn1");
                 btn_m.setId("Btn_DelFile" + attrF.FileNo);
                 String s=btn_m.attributes.get("onclick");
                 btn_m.addAttr("onclick",s+" return confirm('此操作要执行移除附件，是否继续？');");
                 pub.append(btn_m.toString());
                 pub.append(this.AddTDEnd());
             }
             pub.append(this.AddTREnd());
         }
         //#endregion 绑定属性控件。

         if (en.getEnMap().getAttrs().Contains("MyFileNum"))
         {
        	 pub.append(this.AddTR());
        	 pub.append(this.AddTD("nowrap=true class='FDesc' ", " "));
             if (en.getPKVal() == null || "".equals(en.getPKVal()) || "0".equals(en.getPKVal()))
                 pub.append("<TD  colspan=3 ><a href=\"javascript:alert('请在保存后在执行。');\" target=_self>附件批量上传(请在保存后在执行)</a></TD>");
             else
                 pub.append("<TD  colspan=3 ><a href=\"../FileManager.jsp?EnName=" + en.toString() + "&PK=" + en.getPKVal() + "\" target=_self >附件批量上传&编辑</a></TD>");
             pub.append(this.AddTREnd());
         }
         pub.append(this.AddTableEnd());

         if (isShowDtl == false)
             return;

         int num = map.getAttrsOfOneVSM().size() + map.getHisRefMethods().size() + map.getDtls().size();
         if (num > 0 && en.IsExit(en.getPK(), en.getPKVal()) == false)
         {
             String endMsg = "";
             // 增加相关信息
            /* AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
             for (AttrOfOneVSM vsM:oneVsM)
                 endMsg += "[<a href=\"javascript:alert('请在保存后填写。');\" >" + vsM.getDesc() + "</a>]";

             RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
             for (RefMethod func:myreffuncs)
             {
                 if (func.Visable == false)
                     continue;
                 endMsg += "[<a href=\"javascript:alert('请在保存后执行。');\" >" + func.Title + "</a>]";
             }


             if (isShowDtl)
             {
                 EnDtls enDtls = en.getEnMap().getDtls();
                 for (EnDtl enDtl:enDtls)
                     endMsg += "[<a href=\"javascript:alert('请在保存后填写。')\" >" + enDtl.getDesc() + "</a>]";
             }

             if (!endMsg.equals(""))
             {
                 pub.append("<table border=0><TR><TD class=TD><font style='font-size:12px' >" + endMsg + "</font></TD></TR></table>");
             }
*/
             return;
         }

         String refstrs = "";
         if (en.equals(""))
         {
             refstrs += "";
             return;
         }

         String keys = "&PK=" + en.getPKVal().toString();
         for (Attr attr:en.getEnMap().getAttrs())
         {
             if (attr.getMyFieldType() == FieldType.Enum ||
                 attr.getMyFieldType() == FieldType.FK ||
                 attr.getMyFieldType() == FieldType.PK ||
                 attr.getMyFieldType() == FieldType.PKEnum ||
                 attr.getMyFieldType() == FieldType.PKFK)
                 keys += "&" + attr.getKey() + "=" + en.GetValStringByKey(attr.getKey());
         }


         Entities hisens = en.getGetNewEntities();
         SimpleDateFormat sdf=new SimpleDateFormat("ddhhmmss");
         keys += "&r=" + sdf.format(new Date());
         if (isShowDtl)
             refstrs = GetRefstrs(keys, en, en.getGetNewEntities());
         if (!refstrs.equals(""))
         {
             pub.append("<font style='font-size:12px' >" + refstrs + "</font>");
             // this.Add("<HR><table class=Table border=0 ><TR class=TR ><TD class=TD ><font style='font-size:12px' >" + refstrs + "</font></TD></TR></table>");
         }
         //this.Add(refstrs);
     }

     //		public static String GetRefstrs(String keys, Entity en, Entities hisens)
     //		{
     //			String refstrs="";
     //			String path=System.Web.HttpContext.Current.Request.ApplicationPath;
     //			int i = 0;
     //
     //			#region 加入一对多的实体编辑
     //			AttrsOfOneVSM oneVsM= en.EnMap.AttrsOfOneVSM;
     //			if ( oneVsM.Count > 0 )
     //			{
     //				for(AttrOfOneVSM vsM in oneVsM)
     //				{
     //					String url=path+"/Comm/UIEn1ToM.jsp?EnsName="+en.toString()+"&AttrKey="+vsM.EnsOfMM.toString()+keys;
     //					String sql="SELECT COUNT(*) FROM "+vsM.EnsOfMM.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE "+vsM.AttrOfOneInMM+"='"+en.PKVal+"'";
     //					try
     //					{
     //						i=DBAccess.RunSQLReturnValInt(sql);
     //					}
     //					catch(Exception ex)
     //					{
     //						vsM.EnsOfMM.GetNewEntity.CheckPhysicsTable();
     //						throw ex;
     //					}
     //			 
     //					if (i==0)
     //						refstrs+="[<a href=\"javascript:WinShowModalDialog('"+url+"','onVsM'); \"  >"+vsM.Desc+"</a>]";
     //					else
     //						refstrs+="[<a href=\"javascript:WinShowModalDialog('"+url+"','onVsM'); \"  >"+vsM.Desc+"-"+i+"</a>]";
     //				}
     //			}
     //			#endregion
     //
     //			#region 加入他门的相关功能
     ////			SysUIEnsRefFuncs reffuncs = en.GetNewEntities.HisSysUIEnsRefFuncs ;
     ////			if ( reffuncs.Count > 0  )
     ////			{
     ////				for(SysUIEnsRefFunc en1 in reffuncs)
     ////				{
     ////					String url=path+"/Comm/RefFuncLink.jsp?RefFuncOID="+en1.OID.toString()+"&MainEnsName="+hisens.toString()+keys;
     ////					refstrs+="[<a href=\"javascript:WinOpen('"+url+"','ref'); \"  >"+en1.Name+"</a>]";
     ////				}
     ////			}
     //			#endregion
     //
     //			#region 加入他门的 方法
     //			RefMethods myreffuncs = en.EnMap.HisRefMethods ;
     //			if ( myreffuncs.Count > 0  )
     //			{
     //				for(RefMethod func in myreffuncs)
     //				{
     //					if (func.Visable==false)
     //						continue;
     //
     //					String url=path+"/Comm/RefMethod.jsp?Index="+func.Index+"&EnsName="+hisens.toString()+keys;
     //
     //					if (func.Warning==null)
     //					{
     //						if (func.Target==null)
     //							refstrs+="["+func.GetIcon( path ) +"<a href='"+url+"' ToolTip='"+func.ToolTip+"' >"+func.Title+"</a>]";
     //						else
     //							refstrs+="["+func.GetIcon( path )+"<a href=\"javascript:WinOpen('"+url+"','"+func.Target+"')\" ToolTip='"+func.ToolTip+"' >"+func.Title+"</a>]";
     //					}
     //					else
     //					{
     //						if (func.Target==null)
     //							refstrs+="["+func.GetIcon( path )+"<a href=\"javascript: if ( confirm('"+func.Warning+"') ) { window.location.href='"+url+"' }\" ToolTip='"+func.ToolTip+"' >"+func.Title+"</a>]";
     //						else
     //							refstrs+="["+func.GetIcon( path )+"<a href=\"javascript: if ( confirm('"+func.Warning+"') ) { WinOpen('"+url+"','"+func.Target+"') }\" ToolTip='"+func.ToolTip+"' >"+func.Title+"</a>]";
     //					}
     //				}
     //			}
     //			#endregion
     //
     //			#region 加入他的明细
     //			EnDtls enDtls= en.EnMap.Dtls;
     //			if ( enDtls.Count > 0 )
     //			{
     //				for(EnDtl enDtl in enDtls)
     //				{
     //					String url=path+"/Comm/UIEnDtl.jsp?EnsName="+enDtl.EnsName+"&Key="+enDtl.RefKey+"&Val="+en.PKVal.toString()+"&MainEnsName="+en.toString()+keys;
     //					try
     //					{
     //						 i=DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM "+enDtl.Ens.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE "+enDtl.RefKey+"='"+en.PKVal+"'"); 
     //					}
     //					catch(Exception ex)
     //					{
     //						enDtl.Ens.GetNewEntity.CheckPhysicsTable();
     //						throw ex;
     //					}
     //
     //					if (i==0)					 
     //						refstrs+="[<a href=\"javascript:WinOpen('"+url+"', 'dtl"+enDtl.RefKey+"'); \" >"+enDtl.Desc+"</a>]";
     //					else
     //						refstrs+="[<a href=\"javascript:WinOpen('"+url+"', 'dtl"+enDtl.RefKey+"'); \"  >"+enDtl.Desc+"-"+i+"</a>]";
     //				}
     //			}
     //			#endregion
     //			return refstrs;
     //		}
     //		public static String GetRefstrs1(String keys, Entity en, Entities hisens)
     //		{
     //			String refstrs="";
     //
     //			#region 加入一对多的实体编辑
     //			AttrsOfOneVSM oneVsM= en.EnMap.AttrsOfOneVSM;
     //			if ( oneVsM.Count > 0 )
     //			{
     //				for(AttrOfOneVSM vsM in oneVsM)
     //				{
     //					String url="UIEn1ToM.jsp?EnsName="+en.toString()+"&AttrKey="+vsM.EnsOfMM.toString()+keys;
     //					String sql="SELECT COUNT(*) FROM "+vsM.EnsOfMM.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE "+vsM.AttrOfOneInMM+"='"+en.PKVal+"'";
     //					int i=DBAccess.RunSQLReturnValInt(sql);
     //			 
     //					if (i==0)
     //						refstrs+="[<a href='"+url+"'  >"+vsM.Desc+"</a>]";
     //					else
     //						refstrs+="[<a href='"+url+"'  >"+vsM.Desc+"-"+i+"</a>]";
     //				 
     //				}
     //			}
     //			#endregion
     //
     //			#region 加入他门的相关功能
     ////			SysUIEnsRefFuncs reffuncs = en.GetNewEntities.HisSysUIEnsRefFuncs ;
     ////			if ( reffuncs.Count > 0  )
     ////			{
     ////				for(SysUIEnsRefFunc en1 in reffuncs)
     ////				{
     ////					String url="RefFuncLink.jsp?RefFuncOID="+en1.OID.toString()+"&MainEnsName="+hisens.toString()+keys;
     ////					refstrs+="[<a href='"+url+"' >"+en1.Name+"</a>]";
     ////				}
     ////			}
     //			#endregion	 
     //
     //			#region 加入他的明细
     //			EnDtls enDtls= en.EnMap.Dtls;
     //			if ( enDtls.Count > 0 )
     //			{
     //				for(EnDtl enDtl in enDtls)
     //				{
     //					String url="UIEnDtl.jsp?EnsName="+enDtl.EnsName+"&Key="+enDtl.RefKey+"&Val="+en.PKVal.toString()+"&MainEnsName="+en.toString()+keys;
     //
     //					int i=DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM "+enDtl.Ens.GetNewEntity.getEnMap().getPhysicsTable()+" WHERE "+enDtl.RefKey+"='"+en.PKVal+"'");
     //					if (i==0)					 
     //						refstrs+="[<a href='"+url+"'  >"+enDtl.Desc+"</a>]";
     //					else
     //						refstrs+="[<a href='"+url+"'  >"+enDtl.Desc+"-"+i+"</a>]";
     //				}
     //			}
     //			#endregion
     //			
     //
     //			return refstrs;
     //		}
     //		public void Delete()
     //		public Entity GetEnData(Entity en)

     public Entity GetEnData(Entity en) throws Exception
     {
         String key = "";
         try
         {
             for(Attr attr:en.getEnMap().getAttrs())
             {
                 if (attr.getMyFieldType() == FieldType.RefText)
                     continue;

                 if ("MyNum".equals(attr.getKey()))
                     continue;

                 if (attr.getUIVisible() == false)
                     continue;


                 key = attr.getKey();
                 
                 UIContralType ut=attr.getUIContralType();
                 if(ut==UIContralType.TB){
                	 if (attr.getUIVisible())
                     {
                    	 UiFatory uf=new UiFatory();
                         if (attr.getUIHeight() == 0)
                         {
                        	 TextBox tb=(TextBox)uf.GetUIByID("TB_" + attr.getKey());
                             en.SetValByKey(attr.getKey(),tb.getText());
                             continue;
                         }
                         else
                         {
                             if (uf.GetUIByID("TB_" + attr.getKey())==null)
                             {
                            	 TextBox tb=(TextBox)uf.GetUIByID("TB_" + attr.getKey());
                                 en.SetValByKey(attr.getKey(), tb.getText());
                                 continue;
                             }

                             if (uf.GetUIByID("TBH_" + attr.getKey())==null)
                             {
                                 TextBox input = (TextBox)uf.GetUIByID("TBH_" + attr.getKey());
                                 en.SetValByKey(attr.getKey(), input.getText());
                                 continue;
                             }

                             if (uf.GetUIByID("TBF_" + attr.getKey())==null)
                             {
                                 //  FredCK.FCKeditorV2.FCKeditor fck = (FredCK.FCKeditorV2.FCKeditor)this.FindControl("TB_" + attr.Key);
                                 // en.SetValByKey(attr.Key, fck.Value);
                                 continue;
                             }
                         }
                     }
                     else
                     {
                         en.SetValByKey(attr.getKey(), this.GetValByKey(attr.getKey()));
                     }
                 }
                 else if(ut==UIContralType.DDL){
                	 try
                     {
                         en.SetValByKey(attr.getKey(), this.GetDDLByKey("DDL_" + attr.getKey()).getSelectedItem().getValue());
                     }
                     catch(Exception e)
                     {
                    	 e.printStackTrace();
                     }
                 }
                 else if(ut==UIContralType.CheckBok){
                	  en.SetValByKey(attr.getKey(), this.GetCBByKey("CB_" + attr.getKey()).getChecked());
                 }
                 else{
                	 
                 }
                 
             }
         }
         catch (Exception ex)
         {
        	 ex.printStackTrace();
             throw new Exception("GetEnData error :" + ex.getMessage() + " key = " + key);
         }
         return en;
     }
     //		public DDL GetDDLByKey(String key)
     public DDL GetDDLByKey(String key)
     {
    	 UiFatory uf=new UiFatory();
         return (DDL)uf.GetUIByID(key);
     }
     //		public CheckBox GetCBByKey(String key)
     public CheckBox GetCBByKey(String key)
     {
    	 UiFatory uf=new UiFatory();
         return (CheckBox)uf.GetUIByID(key);
     }

     public void init()
     {
//         if (this.IsPostBack)
//         {
//             //	this.Bind(this.HisEn,this.IsReadonly,this.IsShowDtl ) ; 	
//         }
     }
     public Entity HisEn = null;

     public static String GetRefstrs1(String keys, Entity en, Entities hisens)
     {
         String refstrs = "";

         AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
         if (oneVsM.size() > 0)
         {
             for(AttrOfOneVSM vsM:oneVsM)
             {
                 String url = "UIEn1ToM.jsp?EnsName=" + en.toString() + "&AttrKey=" + vsM.getEnsOfMM().toString() + keys;
                 int i = 0;

                 try
                 {
                     i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'");
                 }
                 catch(Exception e)
                 {
                     i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*)  as NUM FROM " + vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + vsM.getAttrOfOneInMM() + "=" + en.getPKVal());
                 }

                 if (i == 0)
                     refstrs += "[<a href='" + url + "'  >" + vsM.getDesc() + "</a>]";
                 else
                     refstrs += "[<a href='" + url + "'  >" + vsM.getDesc() + "-" + i + "</a>]";

             }
         }

         //			SysUIEnsRefFuncs reffuncs = en.GetNewEntities.HisSysUIEnsRefFuncs ;
         //			if ( reffuncs.Count > 0  )
         //			{
         //				for(SysUIEnsRefFunc en1 in reffuncs)
         //				{
         //					String url="RefFuncLink.jsp?RefFuncOID="+en1.OID.toString()+"&MainEnsName="+hisens.toString()+keys;
         //					refstrs+="[<a href='"+url+"' >"+en1.Name+"</a>]";
         //				}
         //			}

         EnDtls enDtls = en.getEnMap().getDtls();
         if (enDtls.size() > 0)
         {
             for(EnDtl enDtl:enDtls)
             {
                 String url = "UIEnDtl.jsp?EnsName=" + enDtl.getEnsName() + "&RefKey=" + enDtl.getRefKey() + "&RefVal=" + en.getPKVal().toString() + "&MainEnsName=" + en.toString() + keys;

                 int i = 0;

                 try
                 {
                     i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
                 }
                 catch(Exception e)
                 {
                     i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
                 }
                 if (i == 0)
                     refstrs += "[<a href='" + url + "'  >" + enDtl.getDesc() + "</a>]";
                 else
                     refstrs += "[<a href='" + url + "'  >" + enDtl.getDesc() + "-" + i + "</a>]";
             }
         }

         return refstrs;
     }

     protected void OnInit()
     {
         //
         // CODEGEN: 该调用是 ASP.NET Web 窗体设计器所必需的。
         //
//         InitializeComponent();
//         base.OnInit(e);
     }

     /// <summary>
     ///		设计器支持所需的方法 - 不要使用代码编辑器
     ///		修改此方法的内容。
     /// </summary>
     private void InitializeComponent()
     {

     }


 }
	

