package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.WF.GenerWorkFlowAttr;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerLists;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import cn.jflow.common.model.ServAccepterModel;

@Controller
@RequestMapping("/WF/WorkOpt")
public class WorkOptController {

	@RequestMapping(value = "/ReturnWorkS", method = RequestMethod.POST)
	public void returnWork(HttpServletRequest request,
			HttpServletResponse response) {
		// model.addAttribute("result", "用户不存在");
		Map<String, String> map = new HashMap<String, String>();
		String btnId = request.getParameter("BtnID");
		int FK_Node = Integer.valueOf(request.getParameter("FK_Node"));
		long FID = Long.valueOf(request.getParameter("FID"));
		long WorkID = Long.valueOf(request.getParameter("WorkID"));
		String FK_Flow = request.getParameter("FK_Flow");
		if (btnId.equals("Btn_Cancel")) {

			String isThread = request.getParameter("isThread");
			Node curNd = new Node(FK_Node);
			if (curNd.getFormType() == NodeFormType.SheetTree ||"isThread".equals(isThread)) {
				try {
					response.getOutputStream().write(
							"<script>window.close();</script>".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				//return null;
			} else {
				String url = Glo.getCCFlowAppPath()+"WF/MyFlow.htm?"
						+ "FK_Flow=" + FK_Flow + "&WorkID=" + WorkID
						+ "&FK_Node=" + FK_Node + "&FID=" + FID;
				
				
				if (FID == 0){
					url=Glo.getCCFlowAppPath()+"WF/MyFlow.htm?FK_Flow=" + FK_Flow + "&WorkID=" + WorkID + "&FK_Node=" + FK_Node + "&FID=" + FID;
				}
				else
				{
					String from = request.getParameter("FromUrl");
					if (from != null && from.equals("FHLFlow")){ // 修正退回消息界面，点击取消按钮报null错误
						url= Glo.getCCFlowAppPath()+"WF/WorkOpt/fhlflow.jsp?FK_Flow=" + FK_Flow + "&WorkID=" + WorkID + "&FK_Node=" + FK_Node + "&FID=" + FID;
					}
					else{
						url=Glo.getCCFlowAppPath()+"WF/MyFlow.htm?FK_Flow=" + FK_Flow + "&WorkID=" + WorkID + "&FK_Node=" + FK_Node + "&FID=" + FID;
					}
				}
				try {
					response.sendRedirect(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (btnId.equals("Btn_OK")) {
			String returnInfo = request.getParameter("TB_Doc");
			String reNodeEmp = request.getParameter("DDL1");
			boolean IsBackTracking = false;
			String CB_IsBackTracking = request
					.getParameter("CB_IsBackTracking");
			if (CB_IsBackTracking != null && (CB_IsBackTracking.equals("1") || CB_IsBackTracking.equals("on")))
				IsBackTracking = true;
			String[] strs = reNodeEmp.split("@");
			// 执行退回api.
			String rInfo;
			try {
				rInfo = BP.WF.Dev2Interface.Node_ReturnWork(FK_Flow, WorkID,
						FID, FK_Node, Integer.valueOf(strs[0]), strs[1],
						returnInfo, IsBackTracking);
				
				//删除其他子线程中的待办信息
				GenerWorkerLists gwls = new GenerWorkerLists();
				gwls.Retrieve(GenerWorkFlowAttr.FID,WorkID);

				/*for (GenerWorkerList item : gwls.ToJavaList())
				{*/
				for(int i=0;i<gwls.size();i++){	
					GenerWorkerList item=(GenerWorkerList) gwls.get(i);
				
					// 删除 子线程数据 
					DBAccess.RunSQL("DELETE FROM ND" + item.getFK_Node() + " WHERE OID=" + item.getWorkID());
				}

				//删除流程控制数据。
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FID=" + WorkID);
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + WorkID);
				DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FID=" + WorkID);


				
				String url = Glo.getCCFlowAppPath()+"WF/MyFlowInfo.jsp";
				request.getSession().setAttribute("info", rInfo);
				response.sendRedirect(url);
			} catch (Exception e) {
				try{
					request.getSession().setAttribute("info", e.getMessage());
					response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Comm/Port/ToErrorPage.jsp");
				}catch(IOException io){
					
				}
			}

		}
		// return map;
		// return new ModelAndView("redirect:/admin/systemChoose.do");
		// return new ModelAndView("admin/Managelogin");
		//return null;

		
	}
	@RequestMapping(value = "/ServAccepterController")
	public String ProcessRequest(HttpServletRequest req,HttpServletResponse res) throws Exception
	{
		ServAccepterModel ser=new ServAccepterModel();
		ser.setFK_Node(req.getParameter("FK_Node"));
		ser.setFK_Station(req.getParameter("StationID"));
		ser.setReq(req);
		ser.setRes(res);
		ser.setWorkID(req.getParameter("WorkID"));
		ser.setFK_Dept(req.getParameter("DeptId"));
		ser.setName(req.getParameter("name"));
		ser.setPage(Integer.parseInt(req.getParameter("page")));
		ser.setRows(Integer.parseInt(req.getParameter("rows")));
		return ser.ProcessRequest(res, req.getParameter("type").toString());
	}
	
}