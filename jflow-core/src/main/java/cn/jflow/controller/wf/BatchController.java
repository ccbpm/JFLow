package cn.jflow.controller.wf;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.TempObject;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.FieldTypeS;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Tools.DataTableConvertJson;
import BP.Tools.FormatToJson;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.SendReturnObjs;
import BP.WF.Node;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF")
public class BatchController {

	
	@RequestMapping(value = "/SMS", method = RequestMethod.POST)
	@ResponseBody
	public String PopAlert(HttpServletRequest request,
			HttpServletResponse response)
    {
        // IsRead = 0 未读 
        String type = request.getParameter("type");

        DataTable dt = BP.WF.Dev2Interface.DB_GenerPopAlert(type);
        if (dt.Rows.size() >= 1)
        {
            return FormatToJson.ToJson(dt);
        }
        else
        {
            return "";
        }
    }

	@RequestMapping(value = "/LoadWorId", method = RequestMethod.POST)
	@ResponseBody
	public String loadEmps(HttpServletRequest request,
			HttpServletResponse response) {

		String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"
				+ WebUser.getNo() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		return DataTableConvertJson.DataTable2Json(dt);
	}

	@RequestMapping(value = "/BatchSend", method = RequestMethod.POST)
	public ModelAndView batchSend(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		// HashMap<String,BaseWebControl> controls =
		// HtmlUtils.httpParser(object.getFormHtml());

		/*String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"
				+ WebUser.getNo() + "'";
		*/
		String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"+WebUser.getNo()+"' and FK_Node='"+object.getFK_Node()+"'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		Node nd = new Node(object.getFK_Node());
		String[] strs = nd.getBatchParas().split(",");
		MapAttrs attrs = new MapAttrs(object.getFK_MapData());

		String msg = "";
		int idx = -1;
		
		String workids = null;
		if (nd.getBatchParas_IsSelfUrl() == true) {
			workids = "";
		}

		try{
			for (DataRow dr : dt.Rows) {
				idx++;
				if (idx == nd.getBatchListCount())//object.getListNum())
					break;
	
				Long workid = Long.parseLong(dr.getValue("WorkID").toString());
				Object cb = request.getParameter("CB_" + workid);// 选种了为on 不选中为null
				if (null == cb)
					continue;
	
				// 如果是自定义的,就记录workids, 让其转到
				if (nd.getBatchParas_IsSelfUrl() == true) {
					workids += "," + workid;
					continue;
				}


				Hashtable<String, Object> ht = new Hashtable<String, Object>();
	
				// #region 给属性赋值.
				//boolean isChange = false;
				for (String str : strs) {
					if (StringHelper.isNullOrEmpty(str))
						continue;
					for (MapAttr attr : attrs.ToJavaList()) {
						if (!str.equals(attr.getKeyOfEn()))
							continue;
	
						if (attr.getLGType() == FieldTypeS.Normal) {
							Object obj = request.getParameter("TB_"
									+ attr.getKeyOfEn() + "_" + workid);
							if (null != obj) {
	//							if (!attr.getDefVal().equals(obj.toString()))
	//								isChange = true;
								ht.put(str, obj.toString());
								continue;
							}
	
							obj = request.getParameter("CB_" + attr.getKeyOfEn()
									+ "_" + workid);// 选种了为on 不选中为null
							if (null != obj) {// 选种
	//							if (!attr.getDefValOfBool())
	//								isChange = true;
								ht.put(str, 1);
							} else {// 不选中
	//							if (attr.getDefValOfBool())
	//								isChange = true;
								ht.put(str, 0);
							}
						} else {
							Object obj = request.getParameter("DDL_"
									+ attr.getKeyOfEn() + "_" + workid);
							if (null != obj && !"".equals(obj.toString())) {
	//							if (!attr.getDefVal().equals(obj.toString()))
	//								isChange = true;
								if (attr.getLGType() == FieldTypeS.Enum)
									ht.put(str, Integer.parseInt(obj.toString()));
								else
									ht.put(str, obj.toString());
							}
						}
					}
				}
				// #endregion 给属性赋值.
				  //msg += "<fieldset>";
				  msg += "@对工作(" + dr.getValue("Title") + ")处理情况如下。<br>";
	              SendReturnObjs objs = Dev2Interface.Node_SendWork(nd.getFK_Flow(), workid, ht);
	              msg += objs.ToMsgOfHtml();
	              msg += "<hr>";
	             // msg += "</fieldset>";

			}
		}catch(Exception e){
			msg = "批量审核失败："+e.getMessage();
		}
		
		msg += "<br><a href='"+Glo.getCCFlowAppPath() +"WF/Batch.jsp'>返回...</a>";

		mv.addObject("normMsg",msg);
		mv.addObject("DoType","Send");
		//mv.setViewName("redirect:" + "/WF/Batch.jsp");
		//mv.setViewName("forward:" + "/WF/Batch.jsp");
		mv.setViewName("Batch");
		return mv;
	}
	
	@RequestMapping(value = "/BatchGroup", method = RequestMethod.POST)
	public ModelAndView batchGroup(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		
		String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"
				+ WebUser.getNo() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		Node nd = new Node(object.getFK_Node());
		String[] strs = nd.getBatchParas().split(",");
		MapAttrs attrs = new MapAttrs(object.getFK_MapData());

		//String msg = "";
		String ids = "";
		int idx = -1;
		for (DataRow dr : dt.Rows) {

			Long workid = Long.parseLong(dr.getValue("WorkID").toString());
			Object cb = request.getParameter("CB_" + workid);// 选种了为on 不选中为null
			if (null == cb)
				continue;
			idx++;
			if (idx == nd.getBatchListCount())//object.getListNum())
				break;
			ids += workid + ",";
			Hashtable<String, Object> ht = new Hashtable<String, Object>();

			// #region 给属性赋值.
			//boolean isChange = false;
			for (String str : strs) {
				if (StringHelper.isNullOrEmpty(str))
					continue;
				for (MapAttr attr : attrs.ToJavaList()) {
					if (!str.equals(attr.getKeyOfEn()))
						continue;

					if (attr.getLGType() == FieldTypeS.Normal) {
						Object obj = request.getParameter("TB_"
								+ attr.getKeyOfEn() + "_" + workid);
						if (null != obj) {
//							if (!attr.getDefVal().equals(obj.toString()))
//								isChange = true;
							ht.put(str, obj.toString());
							continue;
						}

						obj = request.getParameter("CB_" + attr.getKeyOfEn()
								+ "_" + workid);// 选种了为on 不选中为null
						if (null != obj) {// 选种
//							if (!attr.getDefValOfBool())
//								isChange = true;
							ht.put(str, 1);
						} else {// 不选中
//							if (attr.getDefValOfBool())
//								isChange = true;
							ht.put(str, 0);
						}
					} else {
						Object obj = request.getParameter("DDL_"
								+ attr.getKeyOfEn() + "_" + workid);
						if (null != obj && !"".equals(obj.toString())) {
//							if (!attr.getDefVal().equals(obj.toString()))
//								isChange = true;
							if (attr.getLGType() == FieldTypeS.Enum)
								ht.put(str, Integer.parseInt(obj.toString()));
							else
								ht.put(str, obj.toString());
						}
					}
				}
			}
			// #endregion 给属性赋值.
			
			//执行保存.
			Dev2Interface.Node_SaveWork(nd.getFK_Flow(), object.getFK_Node(), workid, ht);
		}
		
		 String[] paras = nd.getBatchParas().split(",");
         String[] mystrs = paras[0].split("=");
         String flowNo = mystrs[1];

         //BP.Sys.PubClass.WinOpen("MyFlow.aspx?FK_Flow=" + flowNo + "&FK_Node=" + flowNo + "01&DoFunc=SetParentFlow&CFlowNo=" + nd.FK_Flow + "&WorkIDs=" + ids, 1000, 900);
        //this.Response.Redirect("MyFlow.aspx?FK_Flow=" + flowNo + "&FK_Node=" + flowNo + "01&DoFunc=SetParentFlow&CFlowNo=" + nd.FK_Flow + "&WorkIDs=" + ids, true);
        //msg = Glo.getCCFlowAppPath()+"WF/MyFlow.htm?FK_Flow=" + flowNo + "&FK_Node=" + flowNo + "01&DoFunc=SetParentFlow&CFlowNo=" + nd.getFK_Flow() + "&WorkIDs=" + ids;
		
     	mv.addObject("FK_Flow", flowNo);
 		mv.addObject("FK_Node", flowNo+"01");
 		mv.addObject("DoFunc", "SetParentFlow");
 		mv.addObject("CFlowNo", nd.getFK_Flow());
 		mv.addObject("WorkIDs", ids);
 		mv.setViewName("redirect:" + "/WF/MyFlow.htm");
		return mv;
	}
	
	@RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
	public ModelAndView batchDelete(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		// HashMap<String,BaseWebControl> controls =
		// HtmlUtils.httpParser(object.getFormHtml());

		String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"
				+ WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		Node nd = new Node(object.getFK_Node());

		String msg = "";
		for (DataRow dr : dt.Rows) {

			Long workid = Long.parseLong(dr.getValue("WorkID").toString());
			//Long fid = Long.parseLong(dr.getValue("FID").toString());
			Object cb = request.getParameter("CB_" + workid);// 选种了为on 不选中为null
			if (null == cb)
				continue;

			msg += "@对工作(" + dr.getValue("Title") + ")处理情况如下。<br>";
			msg += Dev2Interface.Flow_DoDeleteFlowByFlag(nd.getFK_Flow(), workid, "批量删除", true);
			msg += "<hr>";
		}
		
		msg += "<a href='"+Glo.getCCFlowAppPath() +"WF/Batch.jsp'>返回...</a>";
		
		mv.addObject("normMsg",msg);
		mv.addObject("DoType","Delete");
		mv.setViewName("redirect:" + "/WF/Batch.jsp");
		return mv;
	}
	
	
	@RequestMapping(value = "/BatchReturn", method = RequestMethod.POST)
	public ModelAndView batchReturn(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		// HashMap<String,BaseWebControl> controls =
		// HtmlUtils.httpParser(object.getFormHtml());

		String sql = "SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='"
				+ WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		Node nd = new Node(object.getFK_Node());

		String msg = "";
		for (DataRow dr : dt.Rows) {

			Long workid = Long.parseLong(dr.getValue("WorkID").toString());
			//Long fid = Long.parseLong(dr.getValue("FID").toString());
			Object cb = request.getParameter("CB_" + workid);// 选种了为on 不选中为null
			if (null == cb)
				continue;

			msg += "@对工作(" + dr.getValue("Title") + ")处理情况如下。<br>";
			//BP.WF.SendReturnObjs objs = null;BP.WF.Dev2Interface.Node_ReturnWork(nd.FK_Flow, workid,fid,this.FK_Node,"批量退回");
            //msg += objs.ToMsgOfHtml();
			msg += "<hr>";
		}
		
		msg += "<a href='"+Glo.getCCFlowAppPath() +"WF/Batch.jsp'>返回...</a>";
		
		mv.addObject("normMsg",msg);
		mv.addObject("DoType","Return");
		mv.setViewName("redirect:" + "/WF/Batch.jsp");
		return mv;
	}
}