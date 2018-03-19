package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Rpt.MapRpt;

@Controller
@RequestMapping("/mapdef")
public class S1EditController {
	@RequestMapping(value = "/S1Edit", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req,HttpServletResponse res)
	{
		ModelAndView mv=new ModelAndView();
		String FK_Flow=req.getParameter("FK_Flow");
		String RptNo=req.getParameter("RptNo");
		String FK_MapData=req.getParameter("FK_MapData");
		MapRpt rpt = new MapRpt();
        if (RptNo != null)
        {
            /**/
            rpt.setNo(RptNo);
            rpt.RetrieveFromDBSources();
        }

        mv.addObject("TB_No", rpt.getNo());
        mv.addObject("TB_Name", rpt.getName());
        mv.addObject("TB_Note", rpt.getNote());
        mv.addObject("FK_Flow", FK_Flow);
        mv.addObject("RptNo", RptNo);
        mv.addObject("FK_MapData", FK_MapData);
//        TB_No.Text = rpt.No;
//        this.TB_Name.Text = rpt.Name;
//        this.TB_Note.Text = rpt.Note;

        if (StringHelper.isNullOrEmpty(rpt.getNo()) == false)
        {
        	mv.addObject("disabled", "disabled");
        }
            //this.TB_No.Enabled = false;
        mv.setViewName("MapDef/Rpt/S1_Edit");
		return mv;
	}
	
	@RequestMapping(value = "/S1EditSave", method = RequestMethod.POST)
	public ModelAndView Btn_Save_Click(HttpServletRequest req,HttpServletResponse res)
	{
		ModelAndView mv=new ModelAndView();
		String FK_Flow=req.getParameter("FK_Flow");
		String RptNo=req.getParameter("RptNo")==null?"":req.getParameter("RptNo");
		String FK_MapData=req.getParameter("FK_MapData");
		String TB_No=RptNo;
		String TB_Name=req.getParameter("TB_Name");
		String TB_Note= req.getParameter("TB_Note");
		 MapRpt rpt = new MapRpt();
         rpt = (MapRpt) BP.Sys.PubClass.copyFromRequest(rpt, req);
         if (RptNo != null)
             rpt.setNo(RptNo);

         //rpt.setParentMapData(FK_MapData);

         Flow fl = new Flow(FK_Flow);
         rpt.setPTable(fl.getPTable());

         if (RptNo!=null && !RptNo.equals(""))
         {
             if (rpt.getNo().equals(RptNo))
             {
                 BP.Sys.PubClass.Alert("@该编号已经存在:" + rpt.getNo(), res);
                 rpt.setName(TB_Name);
                 rpt.setNote(TB_Note);
                 rpt.Update();
                 mv.addObject("TB_No", rpt.getNo());
                 mv.addObject("TB_Name", rpt.getName());
                 mv.addObject("TB_Note", rpt.getNote());
                 mv.addObject("FK_Flow", FK_Flow);
                 mv.addObject("RptNo", RptNo);
                 mv.addObject("FK_MapData", FK_MapData);
                 mv.addObject("success", "修改成功！");
                 mv.setViewName("MapDef/Rpt/S1_Edit");
                 if (StringHelper.isNullOrEmpty(rpt.getNo()) == false)
                 {
                 	mv.addObject("disabled", "disabled");
                 }
                 return mv;
             }

             rpt.Insert();
             mv.addObject("success", "保存成功！");
         }
         else
         {
             rpt.Update();
             mv.addObject("success", "修改成功！");
         }
         if (StringHelper.isNullOrEmpty(rpt.getNo()) == false)
         {
         	mv.addObject("disabled", "disabled");
         }
         mv.addObject("TB_No", rpt.getNo());
         mv.addObject("TB_Name", rpt.getName());
         mv.addObject("TB_Note", rpt.getNote());
         mv.addObject("FK_Flow", FK_Flow);
         mv.addObject("RptNo", RptNo);
         mv.addObject("FK_MapData", FK_MapData);
         mv.addObject("success", "保存成功！");
         mv.setViewName("MapDef/Rpt/S1_Edit");
         return mv;
	}
	
	@RequestMapping(value = "/S1EditSaveAndNext1", method = RequestMethod.POST)
	public ModelAndView Btn_SaveAndNext1_Click(HttpServletRequest req,HttpServletResponse res)
	{
		ModelAndView mv=new ModelAndView();
		String FK_Flow=req.getParameter("FK_Flow");
		String RptNo=req.getParameter("RptNo");
		String FK_MapData=req.getParameter("FK_MapData");
		String TB_No=RptNo;
		String TB_Name=req.getParameter("TB_Name");
		String TB_Note= req.getParameter("TB_Note");
		 MapRpt rpt = new MapRpt();
         rpt = (MapRpt) BP.Sys.PubClass.copyFromRequest(rpt, req);
         if (RptNo != null)
             rpt.setNo(RptNo);

         //rpt.setParentMapData(FK_MapData);

         Flow fl = new Flow(FK_Flow);
         rpt.setPTable(fl.getPTable());

         if (RptNo!=null && !RptNo.equals(""))
         {
             if (rpt.getNo().equals(RptNo))
             {
                 BP.Sys.PubClass.Alert("@该编号已经存在:" + rpt.getNo(), res);
                 mv.addObject("TB_No", rpt.getNo());
                 mv.addObject("TB_Name", rpt.getName());
                 mv.addObject("TB_Note", rpt.getNote());
                 mv.addObject("FK_Flow", FK_Flow);
                 mv.addObject("RptNo", RptNo);
                 mv.addObject("FK_MapData", FK_MapData);
                 mv.setViewName("MapDef/Rpt/S2_ColsChose");
                 if (StringHelper.isNullOrEmpty(rpt.getNo()) == false)
                 {
                 	mv.addObject("disabled", "disabled");
                 }
                 return mv;
             }

             rpt.Insert();
         }
         else
         {
             rpt.Update();
         }
         mv.addObject("TB_No", rpt.getNo());
         mv.addObject("TB_Name", rpt.getName());
         mv.addObject("TB_Note", rpt.getNote());
         mv.addObject("FK_Flow", FK_Flow);
         mv.addObject("RptNo", RptNo);
         mv.addObject("FK_MapData", FK_MapData);
         mv.addObject("success", "保存成功！");
         mv.setViewName("MapDef/Rpt/S2_ColsChose");
		return mv;
	}

}
