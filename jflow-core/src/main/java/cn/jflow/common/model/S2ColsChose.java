package cn.jflow.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.CheckBox;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;

public class S2ColsChose extends BaseModel{

	StringBuffer Pub2=null;
	
	private String FK_Flow;
	
	private String RptNo;
	
	private String FK_MapData;
	
	public S2ColsChose(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub2=new StringBuffer();
	}

	
	public void init(){
		 GroupFields gfs = new GroupFields(this.FK_Flow);
         MapAttrs mattrs = new MapAttrs(this.FK_MapData);
         MapAttrs mattrsOfRpt = new MapAttrs(this.RptNo);
         boolean isBr = false;

         this.Pub2.append(this.AddTable("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));

         for (GroupField gf: gfs.ToJavaList())
         {
             this.Pub2.append(this.AddTR());
             this.Pub2.append(AddTDGroupTitle(gf.getLab()));
             this.Pub2.append(AddTREnd());

             this.Pub2.append(AddTR());
             this.Pub2.append(AddTDBigDocBegain());

             this.Pub2.append(AddTable("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));
             isBr = false;
             for (MapAttr attr: mattrs.ToJavaList())
             {
                 if (attr.getGroupID() != gf.getOID())
                     continue;

                 CheckBox cb = new CheckBox();
                 cb.setId("CB_" + attr.getKeyOfEn());
                 cb.setText(attr.getName() + "(" + attr.getKeyOfEn() + ")");
                 cb.setChecked(mattrsOfRpt.Contains(MapAttrAttr.KeyOfEn, attr.getKeyOfEn()));

                 if (isBr == false)
                     this.Pub2.append(AddTR());

                 this.Pub2.append(AddTD("style='width:50%'", cb));

                 if (isBr)
                     this.Pub2.append(AddTREnd());

                 isBr = !isBr;
             }

             if (isBr)
             {
                 Pub2.append(AddTD());
                 Pub2.append(AddTREnd());
             }

             this.Pub2.append(AddTableEnd());

             this.Pub2.append(AddTDEnd());
             this.Pub2.append(AddTREnd());
         }
         Map<String,List<MapAttr>> dictAttrs=new HashMap<String, List<MapAttr>>();
//         Dictionary<String, List<MapAttr>> dictAttrs = null;//new Dictionary<string, List<MapAttr>>();
         dictAttrs.put("系统字段", new ArrayList<MapAttr>());
         dictAttrs.put("枚举字段", new ArrayList<MapAttr>());
         dictAttrs.put("外键字段", new ArrayList<MapAttr>());
         dictAttrs.put("普通字段", new ArrayList<MapAttr>());
//         var sysFields =BP.WF.Glo.FlowFields;

         //将属性分组：系统、枚举、外键、普通
         for (MapAttr attr: mattrs.ToJavaList())
         {
             if (gfs.Contains(attr.getGroupID()))
                 continue;

//             if (sysFields.Contains(attr.getKeyOfEn()))
//             {
//                 dictAttrs["系统字段"].Add(attr);
//             }
//             else if (attr.HisAttr.IsEnum)
//             {
//                 dictAttrs["枚举字段"].Add(attr);
//             }
//             else if (attr.HisAttr.IsFK)
//             {
//                 dictAttrs["外键字段"].Add(attr);
//             }
//             else
//             {
//                 dictAttrs["普通字段"].Add(attr);
//             }
         }

//         foreach (var de in dictAttrs)
//         {
//             if (de.Value.Count == 0) continue;
//
//             this.Pub2.AddTR();
//             this.Pub2.AddTDGroupTitle(de.Key);
//             this.Pub2.AddTREnd();
//
//             this.Pub2.AddTR();
//             this.Pub2.AddTDBigDocBegain();
//
//             this.Pub2.AddTable("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'");

             isBr = false;

//             foreach (var attr in de.Value)
//             {
//                 CheckBox cb = new CheckBox();
//                 cb.ID = "CB_" + attr.KeyOfEn;
//                 cb.Text = attr.Name + "(" + attr.KeyOfEn + ")";
//                 cb.Checked = mattrsOfRpt.Contains(MapAttrAttr.KeyOfEn, attr.KeyOfEn);
//
//                 switch (attr.KeyOfEn)
//                 {
//                     case NDXRptBaseAttr.Title:
//                     case NDXRptBaseAttr.MyNum:
//                     case NDXRptBaseAttr.OID:
//                     case NDXRptBaseAttr.WFSta:
//                         cb.Checked = true;
//                         cb.Enabled = false;
//                         break;
//                     case NDXRptBaseAttr.WFState:
//                         continue;
//                     default:
//                         break;
//                 }
//
//                 if (isBr == false)
//                     this.Pub2.append(AddTR());
//
//                 this.Pub2.AddTD("style='width:50%'", cb);
//
//                 if (isBr)
//                     this.Pub2.append(AddTREnd());
//
//                 isBr = !isBr;
//             }

             if (isBr)
             {
                 Pub2.append(AddTD());
                 Pub2.append(AddTREnd());
             }

             this.Pub2.append(AddTableEnd());

             this.Pub2.append(AddTDEnd());
             this.Pub2.append(AddTREnd());
//         }
//
//         this.Pub2.append(AddTableEnd());
	}
	
//	 protected void Btn_Save_Click(object sender, EventArgs e)
//     {
//         Save();
//
//         Response.Redirect("S2_ColsChose.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//     }
//
//     protected void Btn_Cancel_Click(object sender, EventArgs e)
//     {
//         this.WinClose();
//     }
//
//     protected void Btn_SaveAndNext1_Click(object sender, EventArgs e)
//     {
//         Save();
//
//         Response.Redirect("S3_ColsLabel.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//     }
//
//     private void Save()
//     {
//         MapAttrs mattrs = new MapAttrs(this.FK_MapData);
//         mattrs.Delete(MapAttrAttr.FK_MapData, this.RptNo);
//
//         MapData md = new MapData(this.FK_MapData);
//         foreach (MapAttr attr in mattrs)
//         {
//             CheckBox cb = this.Pub2.GetCBByID("CB_" + attr.KeyOfEn);
//             if (cb == null)
//                 continue;
//             if (cb.Checked == false)
//                 continue;
//
//             attr.FK_MapData = this.RptNo;
//             attr.Insert();
//         }
//     }
	
}
