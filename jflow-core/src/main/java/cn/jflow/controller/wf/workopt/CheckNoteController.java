package cn.jflow.controller.wf.workopt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.Dev2Interface;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Template.CCSta;
import BP.Web.WebUser;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;

@Controller
@RequestMapping("/WF/WorkOpt")
public class CheckNoteController {

	@RequestMapping(value = "/CheckNote", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson checkNote(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		
		AjaxJson j = new AjaxJson();
		try {
			FrmWorkCheck fwc = new FrmWorkCheck(object.getFK_Node());
			Dev2Interface.WriteTrackWorkCheck(object.getFK_Flow(), object.getFK_Node(), object.getWorkID(),
					object.getFID(), object.getInfo(), fwc.getFWCOpLabel());
			
			//设置审核完成.
			Dev2Interface.Node_CC_SetSta(object.getFK_Node(), object.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
			
			j.setMsg("审核成功！");
		} catch (Exception e) {
			j.setMsg("审核失败："+e.getMessage());
		}
		return j;
	}
}