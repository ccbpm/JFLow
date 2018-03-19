package cn.jflow.controller.wf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.BaseModel;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF/search")
public class KeySearchController {

	/**
	 * 由返回视图文件修改返回json数据，页面由form提交改为ajax提交，方便与其他平台进行整合，
	 * 解决与其他平台进行整合时因项目配置问题二找不到视图文件的问题。
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年6月20日
	 */
	@RequestMapping(value = "/KeySearch", method = RequestMethod.POST)
	@ResponseBody
	//public ModelAndView execute(HttpServletRequest request,HttpServletResponse response) {
	public String execute(HttpServletRequest request,HttpServletResponse response) {
		String text = request.getParameter("textBox1");
		String btnName = request.getParameter("btnName");
		String isCheck = request.getParameter("isCheck");
		StringBuffer htmlStr = new StringBuffer();
        String sql = "";
        if("Btn_ByWorkID".equals(btnName)){
        	int workid = 0;
        	try {
                workid = Integer.parseInt(text);
            } catch(Exception e) {
            	htmlStr.append("<script language=JavaScript>alert('您输入的不是一个WorkID" + text + "');</script>");
            	//mv.addObject("htmlStr", htmlStr.toString());
            	//mv.addObject("text", text);
            	//mv.addObject("isCheck", isCheck);
            	//return mv;
                return htmlStr.toString();
            }
        	
        	//sql 注释部分根据ccflow最新版本修改，  BY peixiaofeng  2016-5-30
			if ("checked".equals(isCheck)) {
			/*	sql = "SELECT A.*,B.Name as FlowName FROM V_FlowData a,WF_Flow b  WHERE A.FK_Flow=B.No AND A.OID="
						+ workid
						+ " AND FlowEmps LIKE '@%"
						+ WebUser.getNo()
						+ "%'";*/
				
				  sql = "SELECT * FROM WF_GenerWorkFlow WHERE  WorkID=" + workid + " AND Emps LIKE '@%" + WebUser.getNo() + "%'";
			} else {
				/*sql = "SELECT A.*,B.Name as FlowName FROM V_FlowData a,WF_Flow b  WHERE A.FK_Flow=B.No AND A.OID="
						+ workid;*/
				  sql = "SELECT * FROM WF_GenerWorkFlow WHERE  WorkID=" + workid;
			}
        }else if("Btn_ByTitle".equals(btnName)){
			if ("checked".equals(isCheck)) {
				/*sql = "SELECT A.*,B.Name as FlowName FROM V_FlowData a,WF_Flow b  WHERE A.FK_Flow=B.No AND a.Title LIKE '%"
						+ text + "%' AND FlowEmps LIKE '@%" + WebUser.getNo() + "%'";*/
				
				 sql = "SELECT * FROM WF_GenerWorkFlow WHERE  Title LIKE '%" + text + "%' AND Emps LIKE '@%" + WebUser.getNo() + "%'";
			} else {
				/*sql = "SELECT A.*,B.Name as FlowName FROM V_FlowData a,WF_Flow b  WHERE A.FK_Flow=B.No AND a.Title LIKE '%"
						+ text + "%'";*/
				
				sql = "SELECT * FROM WF_GenerWorkFlow WHERE  Title LIKE '%" + text + "%'";
			}
        }
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        if (dt.Rows.size() == 0) {
        	htmlStr.append(BaseModel.AddH3("&nbsp;&nbsp;竟然没有查出任何东西，真不可思议。"));
        	//mv.addObject("htmlStr", htmlStr.toString());
        	//mv.addObject("text", text);
        	//mv.addObject("isCheck", isCheck);
        	//return mv;
            return htmlStr.toString();
        }
        htmlStr.append(BaseModel.AddTable());
        htmlStr.append(BaseModel.AddTR());
        htmlStr.append(BaseModel.AddTDTitle("Idx"));
        htmlStr.append(BaseModel.AddTDTitle("流程"));
        htmlStr.append(BaseModel.AddTDTitle("标题"));
        htmlStr.append(BaseModel.AddTDTitle("发起人"));
        htmlStr.append(BaseModel.AddTDTitle("发起日期"));
        htmlStr.append(BaseModel.AddTDTitle("状态"));
        htmlStr.append(BaseModel.AddTDTitle("参与人"));
        htmlStr.append(BaseModel.AddTREnd());
        int idx = 1;
        for(int i = 0;i < dt.Rows.size(); i++) {
        	DataRow dr = dt.Rows.get(i);
        	htmlStr.append(BaseModel.AddTR());
        	htmlStr.append(BaseModel.AddTDIdx(idx++));
        	htmlStr.append(BaseModel.AddTD(dr.getValue("FlowName").toString()));
        	//htmlStr.append(BaseModel.AddTD("<A href=\"javascript:OpenIt('" + dr.getValue("fk_flow").toString() + "','" + dr.getValue("FlowEndNode").toString() + "','" + dr.getValue("OID").toString() + "');\" >" + dr.getValue("Title").toString() + "</a>"));
        	htmlStr.append(BaseModel.AddTD("<A href=\"javascript:OpenIt('" + dr.getValue("fk_flow").toString() + "','" + dr.getValue("fk_node").toString() + "','" + dr.getValue("workid").toString() + "');\" >" + dr.getValue("Title").toString() + "</a>"));
        	htmlStr.append(BaseModel.AddTD(dr.getValue("FlowStarter").toString()));
        	htmlStr.append(BaseModel.AddTD(dr.getValue("FlowStartRDT").toString()));
            switch (Integer.parseInt(dr.getValue("WFState").toString())) {
                case 0:
                	htmlStr.append(BaseModel.AddTD("未完成"));
                    break;
                case 1:
                	htmlStr.append(BaseModel.AddTD("已完成"));
                    break;
                default:
                	htmlStr.append(BaseModel.AddTD("未知"));
                    break;
            }
            htmlStr.append(BaseModel.AddTDBigDoc(dr.getValue("FlowEmps").toString()));
            htmlStr.append(BaseModel.AddTREnd());
        }
        htmlStr.append(BaseModel.AddTableEnd());
		//mv.addObject("htmlStr", htmlStr);
		//mv.addObject("text", text);
    	//mv.addObject("isCheck", isCheck);
        //return mv;
		return htmlStr.toString();
	}

}
