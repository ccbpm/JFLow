package cn.jflow.controller.wf.mapdef.mapExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.MapData;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/wf/mapdef/mapext/bodyattr")
public class BodyAttrController extends BaseController {
	
	

	@RequestMapping(value = "/btn_Save_Click", method = RequestMethod.POST)
	@ResponseBody
	public String btn_Save_Click(TempObject object,HttpServletRequest request, HttpServletResponse response) {
		String FK_MapData=request.getParameter("FK_MapData");
		String bodyText=request.getParameter("TB_Attr");
		MapData md = new MapData(FK_MapData);
	    md.setBodyAttr(bodyText) ;
	    md.Update();
	    return "success";
	}
}
