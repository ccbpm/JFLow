package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.WF.Data.NDXRptBaseAttr;

@Controller
@RequestMapping("/mapdef")
public class S3ColsLabelController {
	@RequestMapping(value = "/S3ColsLabel_Btn_Save_Click", method = RequestMethod.POST)
	public ModelAndView Btn_Save_Click(HttpServletRequest req,
			HttpServletResponse res){
		ModelAndView mv = new ModelAndView();
		String RptNo = req.getParameter("RptNo");
		String FK_MapData = req.getParameter("FK_MapData");
		String FK_Flow = req.getParameter("FK_Flow");
		MapAttrs attrs = new MapAttrs(RptNo);
		for (int i = 0; i < attrs.size(); i++) {
			MapAttr item = (MapAttr) attrs.get(i);
			if (item.getKeyOfEn() != null || !item.getKeyOfEn().equals("")) {
				if (item.getKeyOfEn().equals(NDXRptBaseAttr.Title)) {
					continue;
				}
				if (item.getKeyOfEn().equals(NDXRptBaseAttr.OID)) {
					continue;
				}
				if (item.getKeyOfEn().equals(NDXRptBaseAttr.MyNum)) {
					continue;
				}
				
			}
			String TB_Text = req.getParameter("TB_" + item.getKeyOfEn());
			// TextBox tb = this.Pub2.GetTextBoxByID("TB_" + item.getKeyOfEn());
			item.setName(TB_Text);
			String TB_Idx = req
					.getParameter("TB_" + item.getKeyOfEn() + "_Idx");
			// tb = this.Pub2.GetTextBoxByID("TB_" + item.KeyOfEn + "_Idx");
			item.setIdx(Integer.parseInt(TB_Idx));

			item.Update();
		}
		mv.addObject("RptNo", RptNo);
		mv.addObject("FK_MapData", FK_MapData);
		mv.addObject("FK_Flow", FK_Flow);
		mv.addObject("success", "成功！");
		mv.setViewName("MapDef/Rpt/S3_ColsLabel");
		return mv;
	}
	
	@RequestMapping(value = "/S3ColsLabel_Btn_SaveAndNext1_Click", method = RequestMethod.POST)
	public ModelAndView Btn_SaveAndNext1_Click(HttpServletRequest req,HttpServletResponse res)
	{
			ModelAndView mv = new ModelAndView();
			String RptNo = req.getParameter("RptNo");
			String FK_MapData = req.getParameter("FK_MapData");
			String FK_Flow = req.getParameter("FK_Flow");
			MapAttrs attrs = new MapAttrs(RptNo);
			for (int i = 0; i < attrs.size(); i++) {
				MapAttr item = (MapAttr) attrs.get(i);
				if (item.getKeyOfEn() != null || !item.getKeyOfEn().equals("")) {
					if (item.getKeyOfEn().equals(NDXRptBaseAttr.Title)) {
						continue;
					}
					if (item.getKeyOfEn().equals(NDXRptBaseAttr.OID)) {
						continue;
					}
					if (item.getKeyOfEn().equals(NDXRptBaseAttr.MyNum)) {
						continue;
					}
					
				}
				String TB_Text = req.getParameter("TB_" + item.getKeyOfEn());
				// TextBox tb = this.Pub2.GetTextBoxByID("TB_" + item.getKeyOfEn());
				item.setName(TB_Text);
				String TB_Idx = req
						.getParameter("TB_" + item.getKeyOfEn() + "_Idx");
				// tb = this.Pub2.GetTextBoxByID("TB_" + item.KeyOfEn + "_Idx");
				item.setIdx(Integer.parseInt(TB_Idx));

				item.Update();
			}
			mv.addObject("RptNo", RptNo);
			mv.addObject("FK_MapData", FK_MapData);
			mv.addObject("FK_Flow", FK_Flow);
			//mv.addObject("success", "成功！");
			mv.setViewName("MapDef/Rpt/S5_SearchCond");
			return mv;
	}
}
