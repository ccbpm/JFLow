package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/mapdef")
@Scope("request")
public class S2ColsChoseController {

	@RequestMapping(value = "/S2ColsChose_Btn_Save_Click", method = RequestMethod.POST)
	public ModelAndView Btn_Save_Click(TempObject object,HttpServletRequest req,
			HttpServletResponse res) {
		ModelAndView mv = new ModelAndView();
		String RptNo = req.getParameter("RptNo");
		String FK_MapData = req.getParameter("FK_MapData");
		String FK_Flow = req.getParameter("FK_Flow");
		MapAttrs mattrs = new MapAttrs(FK_MapData);
        mattrs.Delete(MapAttrAttr.FK_MapData, RptNo);
        MapData md = new MapData(FK_MapData);
        for (int i = 0; i < mattrs.size(); i++) {
        	MapAttr attr=(MapAttr) mattrs.get(i);
        	String cbId=req.getParameter("CB_" + attr.getKeyOfEn());
//            CheckBox cb = this.Pub2.GetCBByID("CB_" + attr.getKeyOfEn());
            if (cbId==null)
                continue;
//            if (cb.Checked == false)
//                continue;

            attr.setFK_MapData(RptNo);
            
            attr.Insert();
        }
        // 清理缓存，否则会造成流程查询，修改后列不更新问题。
        BP.DA.Cash.getMap_Cash().remove(RptNo);
        mv.addObject("FK_MapData", FK_MapData);
        mv.addObject("RptNo", RptNo);
        mv.addObject("FK_Flow", FK_Flow);
        mv.addObject("success", "成功!");
        mv.setViewName("MapDef/Rpt/S2_ColsChose");
//		Response.Redirect("S2_ColsChose.aspx?FK_MapData=" + this.FK_MapData
//				+ "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
        return mv;
	}

	@RequestMapping(value = "/S2ColsChose_Btn_SaveAndNext1_Click", method = RequestMethod.POST)
	public ModelAndView Btn_SaveAndNext1_Click(TempObject object,HttpServletRequest req,
			HttpServletResponse res) {
		ModelAndView mv = new ModelAndView();
		String RptNo = req.getParameter("RptNo");
		String FK_MapData = req.getParameter("FK_MapData");
		String FK_Flow = req.getParameter("FK_Flow");
		MapAttrs mattrs = new MapAttrs(FK_MapData);
        mattrs.Delete(MapAttrAttr.FK_MapData, RptNo);
        MapData md = new MapData(FK_MapData);
        for (int i = 0; i < mattrs.size(); i++) {
        	MapAttr attr=(MapAttr) mattrs.get(i);
//		}
//        for (MapAttr attr : mattrs)
//        {
        	String cbId=req.getParameter("CB_" + attr.getKeyOfEn());
//            CheckBox cb = this.Pub2.GetCBByID("CB_" + attr.KeyOfEn);
            if (cbId==null)
                continue;
//            if (cb.Checked == false)
//                continue;

            attr.setFK_MapData(RptNo);
            attr.Insert();
        }
        mv.addObject("FK_MapData", FK_MapData);
        mv.addObject("RptNo", RptNo);
        mv.addObject("FK_Flow", FK_Flow);
        //mv.addObject("success", "成功!");
        mv.setViewName("MapDef/Rpt/S3_ColsLabel");
//		Response.Redirect("S3_ColsLabel.aspx?FK_MapData=" + this.FK_MapData
//				+ "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
        return mv;
	}
	
	
	@RequestMapping(value = "/do_post_back", method = RequestMethod.POST)
	public ModelAndView do_post_back(TempObject object,HttpServletRequest req,
			HttpServletResponse res) {
		ModelAndView mv = new ModelAndView();
		String RptNo = req.getParameter("RptNo");
		String FK_MapData = req.getParameter("FK_MapData");
		String FK_Flow = req.getParameter("FK_Flow");
        mv.addObject("FK_MapData", FK_MapData);
        mv.addObject("RptNo", RptNo);
        mv.addObject("FK_Flow", FK_Flow);
        mv.setViewName("MapDef/Rpt/S2_ColsChose");
        return mv;
	}

}
