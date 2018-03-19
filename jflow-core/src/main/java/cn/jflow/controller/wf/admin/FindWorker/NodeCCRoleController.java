package cn.jflow.controller.wf.admin.FindWorker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.CCRole;
import BP.WF.CCWriteTo;
import BP.WF.Node;
import BP.WF.Template.CC;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/NodeCcRole")
public class NodeCCRoleController extends BaseController {

	@ResponseBody
	@RequestMapping(value = "/btn_Save", method = RequestMethod.POST)
	public String btn_Save(HttpServletRequest request, HttpServletResponse response) {
		
		String DDL_CCRole=request.getParameter("DDL_CCRole");
		String DDL_CCWriteTo=request.getParameter("DDL_CCWriteTo");
		String title=request.getParameter("TB_title");
		String content=request.getParameter("TB_content");
		boolean CC_GW="true".equals(request.getParameter("CC_GW"))?true:false;
		boolean CC_DBM="true".equals(request.getParameter("CC_DBM"))?true:false;
		boolean CC_RY="true".equals(request.getParameter("CC_RY"))?true:false;
		boolean CC_SQL="true".equals(request.getParameter("CC_SQL"))?true:false;
		String CC_SQL_TEXT=request.getParameter("CC_SQL_TEXT");
		
		Node nd = new Node(this.getFK_Node());
		//保存抄送规则
		nd.setHisCCRole(CCRole.forValue(Integer.parseInt(DDL_CCRole)));
		//保存写入规则           
		nd.setCCWriteTo(CCWriteTo.forValue(Integer.parseInt(DDL_CCWriteTo)));
		nd.DirectUpdate();
		//保存模板
		CC cc = new CC();
		cc.setNodeID(this.getFK_Node());
		cc.Retrieve();
		cc.setCCTitle(title);
		cc.setCCDoc(content);
		cc.DirectUpdate();
		
		
		cc.setCCIsStations(CC_GW);
		cc.DirectUpdate();
		cc.setCCIsDepts(CC_DBM);
		cc.DirectUpdate();
		cc.setCCIsEmps(CC_RY);
		cc.DirectUpdate();
		cc.setCCIsSQLs(CC_SQL);
		cc.setCCSQL(CC_SQL_TEXT);
		cc.DirectUpdate();
		return "success";
	}

	
}