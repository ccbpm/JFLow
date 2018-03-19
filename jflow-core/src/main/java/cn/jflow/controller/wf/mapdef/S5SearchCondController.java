package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.Cash;
import BP.En.UIContralType;
import BP.Sys.DTSearchWay;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;

@Controller
@RequestMapping("/mapdef")
public class S5SearchCondController {

	@RequestMapping(value = "/S5SearchCond_Btn_Save_Click", method = RequestMethod.POST)
	public ModelAndView Btn_Save_Click(HttpServletRequest req,HttpServletResponse res)
	{
		ModelAndView mv=new ModelAndView();
		String RptNo=req.getParameter("RptNo");
		String DDL_DTSearchWay=req.getParameter("DDL_DTSearchWay");
		String DDL_DTSearchKey=req.getParameter("DDL_DTSearchKey");
		String FK_MapData=req.getParameter("FK_MapData");
		String FK_Flow=req.getParameter("FK_Flow");
		MapData md = new MapData();
        md.setNo(RptNo);
        md.RetrieveFromDBSources();

        MapAttrs mattrs = new MapAttrs(RptNo);
        String keys = "";
        for (int i = 0; i < mattrs.size(); i++) {
        	MapAttr mattr=(MapAttr) mattrs.get(i);
        	if (mattr.getUIContralType() != UIContralType.DDL)
                continue;
        	String cbId=req.getParameter("CB_F_" + mattr.getKeyOfEn());
//            CheckBox cb = this.Pub2.GetCBByID("CB_F_" + mattr.KeyOfEn);
            if (cbId!=null)
                keys += "*" + mattr.getKeyOfEn();
		}
//        foreach (MapAttr mattr in mattrs)
//        {
//            if (mattr.UIContralType != UIContralType.DDL)
//                continue;
//            CheckBox cb = this.Pub2.GetCBByID("CB_F_" + mattr.KeyOfEn);
//            if (cb.Checked)
//                keys += "*" + mattr.KeyOfEn;
//        }

        md.setRptSearchKeys(keys + "*");
        md.setRptIsSearchKey(true);// = this.Pub2.GetCBByID("CB_IsSearchKey").Checked;

        if(DDL_DTSearchWay!=null || !DDL_DTSearchWay.equals(""))
//        if (this.Pub2.IsExit("DDL_DTSearchWay"))
        {
//            BP.Web.Controls.DDL ddl = this.Pub2.GetDDLByID("DDL_DTSearchWay");
            md.setRptDTSearchWay(DTSearchWay.forValue(Integer.parseInt(DDL_DTSearchWay)));// = (DTSearchWay)ddl.SelectedItemIntVal;

            
//            ddl = this.Pub2.GetDDLByID("DDL_DTSearchKey");
            md.setRptDTSearchKey(DDL_DTSearchKey);//= ddl.SelectedItemStringVal;
        }
        md.Update();

        Cash.getMap_Cash().remove(RptNo);
        mv.addObject("FK_MapData", FK_MapData);
        mv.addObject("RptNo", RptNo);
        mv.addObject("FK_Flow", FK_Flow);
        mv.addObject("success", "成功!");
        mv.setViewName("MapDef/Rpt/S5_SearchCond");
//        this.Response.Redirect("S6_Power.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
		return mv;
	}
	
	@RequestMapping(value = "/S5SearchCond_Btn_SaveAndNext1_Click", method = RequestMethod.POST)
	public ModelAndView Btn_SaveAndNext1_Click(HttpServletRequest req,HttpServletResponse res)
	{
		ModelAndView mv=new ModelAndView();
		String RptNo=req.getParameter("RptNo");
		String DDL_DTSearchWay=req.getParameter("DDL_DTSearchWay");
		String DDL_DTSearchKey=req.getParameter("DDL_DTSearchKey");
		String FK_MapData=req.getParameter("FK_MapData");
		String FK_Flow=req.getParameter("FK_Flow");
		MapData md = new MapData();
        md.setNo(RptNo);
        md.RetrieveFromDBSources();

        MapAttrs mattrs = new MapAttrs(RptNo);
        String keys = "";
        for (int i = 0; i < mattrs.size(); i++) {
        	MapAttr mattr=(MapAttr) mattrs.get(i);
        	if (mattr.getUIContralType() != UIContralType.DDL)
                continue;
        	String cbId=req.getParameter("CB_F_" + mattr.getKeyOfEn());
//            CheckBox cb = this.Pub2.GetCBByID("CB_F_" + mattr.KeyOfEn);
            if (cbId!=null)
                keys += "*" + mattr.getKeyOfEn();
		}
//        foreach (MapAttr mattr in mattrs)
//        {
//            if (mattr.UIContralType != UIContralType.DDL)
//                continue;
//            CheckBox cb = this.Pub2.GetCBByID("CB_F_" + mattr.KeyOfEn);
//            if (cb.Checked)
//                keys += "*" + mattr.KeyOfEn;
//        }

        md.setRptSearchKeys(keys + "*");
        md.setRptIsSearchKey(true);// = this.Pub2.GetCBByID("CB_IsSearchKey").Checked;

        if(DDL_DTSearchWay!=null || !DDL_DTSearchWay.equals(""))
//        if (this.Pub2.IsExit("DDL_DTSearchWay"))
        {
//            BP.Web.Controls.DDL ddl = this.Pub2.GetDDLByID("DDL_DTSearchWay");
            md.setRptDTSearchWay(DTSearchWay.forValue(Integer.parseInt(DDL_DTSearchWay)));// = (DTSearchWay)ddl.SelectedItemIntVal;

            
//            ddl = this.Pub2.GetDDLByID("DDL_DTSearchKey");
            md.setRptDTSearchKey(DDL_DTSearchKey);//= ddl.SelectedItemStringVal;
        }
        md.Update();

        Cash.getMap_Cash().remove(RptNo);
        mv.addObject("FK_MapData", FK_MapData);
        mv.addObject("RptNo", RptNo);
        mv.addObject("FK_Flow", FK_Flow);
        mv.addObject("success", "成功!");
        mv.setViewName("MapDef/Rpt/S6_Power");
//        this.Response.Redirect("S6_Power.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
		return mv;
	}
}
