package cn.jflow.model.wf.rpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.WF.Glo;
import BP.WF.Data.NDXRptBaseAttr;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;

public class S2ColsChoseModel {
	private HttpServletRequest req;
	private HttpServletResponse res;
	public UiFatory Pub2 = null;

	public S2ColsChoseModel() {
	}

	public S2ColsChoseModel(HttpServletRequest request,
			HttpServletResponse response) {
		this.req = request;
		this.res = response;
	}

	// #region 属性.
	private String RptNo;

	public String getRptNo() {
		return req.getParameter("RptNo");
	}

	public void setRptNo(String rptNo) {
		RptNo = rptNo;
	}

	// public string RptNo
	// {
	// get
	// {
	// return this.Request.QueryString["RptNo"];
	//
	// }
	// }
	private String FK_MapData;

	public String getFK_MapData() {
		return req.getParameter("FK_MapData");
	}

	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}

	// public string FK_MapData
	// {
	// get
	// {
	// return this.Request.QueryString["FK_MapData"];
	//
	// }
	// }
	private String FK_Flow;

	public String getFK_Flow() {
		return req.getParameter("FK_Flow");
	}

	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}

	// public string FK_Flow
	// {
	// get
	// {
	// return this.Request.QueryString["FK_Flow"];
	//
	// }
	// }
	// #endregion 属性.

	public void Page_Load() {
		Pub2 = new UiFatory();
		 GroupFields gfs = new GroupFields(this.getFK_Flow());
         MapAttrs mattrs = new MapAttrs(this.getFK_MapData());
         MapAttrs mattrsOfRpt = new MapAttrs(this.getRptNo());
         boolean isBr = false;
         Pub2.append("<div data-options=\"region:'center',title:'2. 设置报表显示列',border:false\" style=\"padding: 5px; height: auto\">");
         this.Pub2.append(BaseModel.AddTable1("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));

         /* for (int i = 0; i < gfs.size(); i++) {
        	 GroupField gf=(GroupField) gfs.get(i);
        	 this.Pub2.append(BaseModel.AddTR());
             this.Pub2.append(BaseModel.AddTDGroupTitle1(gf.getLab()));
             this.Pub2.append(BaseModel.AddTREnd());

             this.Pub2.append(BaseModel.AddTR());
             this.Pub2.append(BaseModel.AddTDBigDocBegain());

             this.Pub2.append(BaseModel.AddTable1("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));
             isBr = false;
             for (int j = 0; j < mattrs.size(); j++) {
            	 MapAttr attr=(MapAttr) mattrs.get(i);
            	 if (attr.getGroupID() != gf.getOID())
                     continue;

                 CheckBox cb = Pub2.creatCheckBox("CB_" + attr.getKeyOfEn());
                 cb.setText(attr.getName() + "(" + attr.getKeyOfEn() + ")");
                 cb.setChecked(mattrsOfRpt.Contains(MapAttrAttr.KeyOfEn, attr.getKeyOfEn()));

                 if (isBr == false)
                     this.Pub2.append(BaseModel.AddTR());

                 this.Pub2.append("<td style='width:50%'>");
                 this.Pub2.append(cb);
                 this.Pub2.append("</td>");
                 if (isBr)
                     this.Pub2.append(BaseModel.AddTREnd());

                 isBr = !isBr;
			 }

             if (isBr)
             {
                 Pub2.append(BaseModel.AddTD());
                 Pub2.append(BaseModel.AddTREnd());
             }

             this.Pub2.append(BaseModel.AddTableEnd());

             this.Pub2.append(BaseModel.AddTDEnd());
             this.Pub2.append(BaseModel.AddTREnd());
		 }*/

         Map<String,List<MapAttr>> map=new HashMap<String, List<MapAttr>>();
         map.put("系统字段", new ArrayList<MapAttr>());
         map.put("枚举字段", new ArrayList<MapAttr>());
         map.put("外键字段", new ArrayList<MapAttr>());
         map.put("普通字段", new ArrayList<MapAttr>());
         List<String> sysFields =Glo.getFlowFields();

         //将属性分组：系统、枚举、外键、普通
         for (int i = 0; i < mattrs.size(); i++) {
        	 MapAttr attr=(MapAttr) mattrs.get(i);
        	 if (gfs.Contains(attr.getGroupID()))
                 continue;

             if (sysFields.contains(attr.getKeyOfEn()))
             {
            	 map.get("系统字段").add(attr);
             }
             else if (attr.getHisAttr().getIsEnum())
             {
            	 map.get("枚举字段").add(attr);
             }
             else if (attr.getHisAttr().getIsFK())
             {
            	 map.get("外键字段").add(attr);
             }
             else
             {
            	 map.get("普通字段").add(attr);
             }
		}

         Set<String> keys = map.keySet();
         for (String key :keys) {
        	 List<MapAttr> de=map.get(key);
        	 if (de.size() == 0) continue;

             this.Pub2.append(BaseModel.AddTR());
             
             this.Pub2.append(BaseModel.AddTDGroupTitle1(key));
             this.Pub2.append(BaseModel.AddTREnd());

             this.Pub2.append(BaseModel.AddTR());
             this.Pub2.append(BaseModel.AddTDBigDocBegain());

             this.Pub2.append(BaseModel.AddTable1("class='Table' border='1' cellspacing='0' cellpadding='0' style='width:100%'"));

             isBr = false;

             for (MapAttr attr : de)
             {
                 CheckBox cb = Pub2.creatCheckBox("CB_" + attr.getKeyOfEn());
                 cb.setText(attr.getName() + "(" + attr.getKeyOfEn() + ")");
                 cb.setChecked(mattrsOfRpt.Contains(MapAttrAttr.KeyOfEn, attr.getKeyOfEn()));
                 if(attr.getKeyOfEn()!=null)
                 {
                	 if(attr.getKeyOfEn().equals(NDXRptBaseAttr.Title))
                	 {
                		 cb.setChecked(true);
                		 //cb.addAttr("display", "display");
                		 cb.addAttr("onclick", "return false;");
                	 }
                	 if(attr.getKeyOfEn().equals(NDXRptBaseAttr.MyNum))
                	 {
                		 cb.setChecked(true);
                		 //cb.addAttr("display", "display");
                		 cb.addAttr("onclick", "return false;");
                	 }
                	 if(attr.getKeyOfEn().equals(NDXRptBaseAttr.OID))
                	 {
                		 cb.setChecked(true);
                		 //cb.addAttr("display", "display");
                		 cb.addAttr("onclick", "return false;");
                	 }
                	 if(attr.getKeyOfEn().equals(NDXRptBaseAttr.WFSta))
                	 {
                		 cb.setChecked(true);
                		 //cb.addAttr("display", "display");
                		 cb.addAttr("onclick", "return false;");
                	 }
                	 if(attr.getKeyOfEn().equals(NDXRptBaseAttr.WFState))
                	 {
                		 continue;
                	 }
                 }

                 if (isBr == false)
                     this.Pub2.append(BaseModel.AddTR());

                 this.Pub2.append("<td style='width:50%'>");
                 this.Pub2.append(cb);
                 this.Pub2.append("</td>");

                 if (isBr)
                     this.Pub2.append(BaseModel.AddTREnd());

                 isBr = !isBr;
             }

             if (isBr)
             {
                 Pub2.append(BaseModel.AddTD());
                 Pub2.append(BaseModel.AddTREnd());
             }

             this.Pub2.append(BaseModel.AddTableEnd());

             this.Pub2.append(BaseModel.AddTDEnd());
             this.Pub2.append(BaseModel.AddTREnd());
		}

         this.Pub2.append(BaseModel.AddTableEnd());
	}
}
