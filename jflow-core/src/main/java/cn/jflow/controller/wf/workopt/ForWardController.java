package cn.jflow.controller.wf.workopt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.WF.Dev2Interface;
import BP.WF.Entity.ShiftWork;
import BP.Web.WebUser;
import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/WF/WorkOpt")
public class ForWardController {

	@RequestMapping(value = "/ForwardS", method = RequestMethod.POST)
	public ModelAndView forward(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
		try {
			String toEmp = "";
			String rbNo = object.getToEmp().split("_")[1];

			String sql = " SELECT No,Name FROM Port_Emp WHERE FK_Dept= '" + object.getFK_Dept() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows) {
				String no = dr.getValue("No").toString();
				if (no.equals(WebUser.getNo()))continue;
				if (no.equals(rbNo)){
					toEmp = no;
					break;
				}
			}
			String info = Dev2Interface.Node_Shift(object.getFK_Flow(),
					object.getFK_Node(), object.getWorkID(), object.getFID(),
					toEmp, object.getInfo());
			
			request.getSession().setAttribute("info", info);
			
			mv.addObject("DoType", "Msg");
			mv.addObject("FK_Flow", object.getFK_Flow());
			mv.setViewName("redirect:" + "/WF/MyFlowInfo.jsp");
		} catch (Exception e) {
			ShiftWork fw = new ShiftWork();
            fw.CheckPhysicsTable();
            
            mv.addObject("WorkID", object.getWorkID());
            mv.addObject("FID", object.getFID());
            mv.addObject("FK_Node", object.getFK_Node());
            mv.addObject("FK_Flow", object.getFK_Flow());
            mv.addObject("FK_Dept", object.getFK_Dept());
            mv.addObject("errMsg", "工作移交出错："+e.getMessage());
            mv.setViewName("redirect:" + "/WF/WorkOpt/Forward.jsp");
            //mv.setViewName("/WF/WorkOpt/Forward");
			return mv;
		}
		return mv;
	}
}