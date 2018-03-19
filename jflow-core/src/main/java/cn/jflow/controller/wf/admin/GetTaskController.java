package cn.jflow.controller.wf.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.PubClass;
import BP.WF.GetTask;
import BP.WF.GetTasks;
import BP.WF.Template.NodeAttr;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/wf/admin/getTask")
public class GetTaskController extends BaseController{
	
	@RequestMapping(value = "/btn_save", method = RequestMethod.POST)
	@ResponseBody
	public String btn_save(HttpServletRequest req,HttpServletResponse res){
		BP.WF.GetTasks jcs = new GetTasks();
		jcs.Retrieve(NodeAttr.FK_Flow, this.getRefNo());

		String nodeIds = req.getParameter("nodeIds");
		if(nodeIds.length()>0){
			nodeIds = nodeIds.substring(0, nodeIds.length() - 1);
		}
		GetTask myjc = new GetTask(this.getFK_Node());
		myjc.setCheckNodes(nodeIds);
		myjc.Update();
		return "success";
	}
	

}
