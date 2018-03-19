package cn.jflow.controller.des;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DBAccess;
import BP.En.QueryObject;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondOrAnd;
import BP.WF.Template.CondType;
import BP.WF.Template.Conds;
import BP.WF.Template.ConnDataFrom;
import cn.jflow.system.ui.core.NamesOfBtn;

@Controller
@RequestMapping("/des")
public class CondController {
	@RequestMapping(value = "/cond_ddl_SelectedIndexChanged", method = RequestMethod.GET)
	public ModelAndView ddl_SelectedIndexChanged(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView();
		String FK_Flow = request.getParameter("FK_Flow");
		String DDL_Node = request.getParameter("FK_Node");// 下拉框选中的值
		String FK_MainNode = request.getParameter("FK_MainNode");
		int HisCondType = Integer.parseInt(request.getParameter("CondType"));
		String DDL_Attr = request.getParameter("FK_Attr");// 下拉框选中的值
		String ToNodeID = request.getParameter("ToNodeID");
		mv.addObject("FK_Flow", FK_Flow);
		mv.addObject("FK_Node", DDL_Node);
		mv.addObject("FK_MainNode", FK_MainNode);
		mv.addObject("CondType", HisCondType);
		mv.addObject("FK_Attr", DDL_Attr);
		mv.addObject("ToNodeID", ToNodeID);
		mv.setViewName("redirect:" + "/WF/Admin/Cond.jsp");
		return mv;
		// response.sendRedirect("Cond.jsp?FK_Flow=" + FK_Flow + "&FK_Node="
		// + DDL_Node + "&FK_MainNode="
		// + FK_MainNode + "&CondType=" + HisCondType + "&FK_Attr="
		// + DDL_Attr + "&ToNodeID=" + ToNodeID);
	}

	@RequestMapping(value = "/cond_btn_Save_Click", method = RequestMethod.GET)
	public ModelAndView btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// var btn = sender as LinkBtn;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		ModelAndView mv = new ModelAndView();
		String btnId = request.getParameter("btnId");
		String ToNodeID = request.getParameter("ToNodeID");
		String FK_Node = request.getParameter("FK_Node");
		int FK_MainNode = Integer.parseInt(request.getParameter("FK_MainNode"));
		String FK_Attr = request.getParameter("FK_Attr");
		int DDL_Node = Integer.parseInt(request.getParameter("DDL_Node"));// DDL_Node下拉框选中的值
		String DDL_Oper = request.getParameter("DDL_Oper");// DDL_Oper下拉框的值
		String FK_Flow = request.getParameter("FK_Flow");
		String MyPK = request.getParameter("MyPK");
		String tb = request.getParameter("tb") == null ? "" : request
				.getParameter("tb");
		if (MyPK == null) {
			MyPK = "";
		}
		String HisCondType = request.getParameter("HisCondType");
		if (NamesOfBtn.getEnumByCode(btnId) == NamesOfBtn.Delete) {
			DBAccess.RunSQL("DELETE FROM WF_Cond WHERE  ToNodeID=" + ToNodeID
					+ " AND DataFrom=" + ConnDataFrom.NodeForm.getValue());
			// String str=request.getRequestURI();
			mv.addObject("MyPK", MyPK);
			mv.addObject("FK_Flow", FK_Flow);
			mv.addObject("FK_Node", FK_Node);
			mv.addObject("FK_MainNode", FK_MainNode);
			mv.addObject("FK_Attr", FK_Attr);
			mv.addObject("CondType", HisCondType);
			mv.addObject("tb", tb);
			mv.addObject("ToNodeID", ToNodeID);
			mv.addObject("success", "删除成功!");
			mv.setViewName("redirect:" + "/WF/Admin/Cond.jsp");
			// EasyUiHelper.AddEasyUiMessager(this, "保存成功！");
			return mv;
			// response.sendRedirect(request.getRequestURI());
			// this.Response.Redirect(this.Request.RawUrl, true);
		}
		// 前台写js，验证非空
		// if (this.GetOperVal == "" || this.GetOperVal == null)
		// {
		// this.Alert("您没有设置条件，请在值文本框中输入值。");
		// return;
		// }

		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (" + CondAttr.NodeID + "="
				+ FK_Node + "  AND ToNodeID=" + ToNodeID + ") AND DataFrom!="
				+ ConnDataFrom.NodeForm.getValue());

		Cond cond = new Cond();
		cond.setHisDataFrom(ConnDataFrom.NodeForm);
		cond.setNodeID(FK_MainNode);
		cond.setToNodeID(FK_MainNode);

		cond.setFK_Attr(FK_Attr);
		cond.setFK_Node(DDL_Node);
		cond.setFK_Operator(DDL_Oper);
		cond.setOperatorValue(tb);
		cond.setOperatorValueT(tb);
		cond.setFK_Flow(FK_Flow);
		cond.setHisCondType(CondType.forValue(Integer.parseInt(HisCondType)));

		if (btnId.equals("Btn_SaveAnd"))
			cond.setCondOrAnd(CondOrAnd.ByAnd);
		else
			cond.setCondOrAnd(CondOrAnd.ByOr);

		// #region 方向条件，全部更新.
		Conds conds = new Conds();
		QueryObject qo = new QueryObject(conds);
		qo.AddWhere(CondAttr.NodeID, FK_MainNode);
		qo.addAnd();
		qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.NodeForm.getValue());
		qo.addAnd();
		qo.AddWhere(CondAttr.CondType, HisCondType);
		if (Integer.parseInt(ToNodeID) != 0) {
			qo.addAnd();
			qo.AddWhere(CondAttr.ToNodeID, ToNodeID);
		}
		int num = qo.DoQuery();
		for (int i = 0; i < conds.size(); i++) {
			Cond item = (Cond) conds.get(i);
			item.setCondOrAnd(cond.getCondOrAnd());
			item.Update();
		}
		// for (Cond item : conds)
		// {
		// item.CondOrAnd = cond.CondOrAnd;
		// item.Update();
		// }
		// #endregion

		/* 执行同步 */
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NODEID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		String sql = "UPDATE WF_Cond SET DataFrom="
				+ ConnDataFrom.NodeForm.getValue() + " WHERE NodeID="
				+ cond.getNodeID() + "  AND FK_Node=" + cond.getFK_Node()
				+ " AND ToNodeID=" + ToNodeID;
		switch (CondType.forValue(Integer.parseInt(HisCondType))) {
		case Flow:
			cond.setMyPK(Integer.toString(BP.DA.DBAccess.GenerOID())); // cond.NodeID
																		// + "_"
																		// +
																		// cond.FK_Node
																		// + "_"
																		// +
																		// cond.FK_Attr
																		// + "_"
																		// +
																		// cond.OperatorValue;
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			mv.addObject("MyPK", cond.getMyPK());
			mv.addObject("FK_Flow", cond.getFK_Flow());
			mv.addObject("FK_Node", cond.getFK_Node());
			mv.addObject("FK_MainNode", cond.getNodeID());
			mv.addObject("FK_Attr", cond.getFK_Attr());
			mv.addObject("CondType", HisCondType);
			mv.addObject("tb", tb);
			mv.addObject("ToNodeID", ToNodeID);
			mv.addObject("success", "保存成功!");
			mv.setViewName("redirect:" + "/WF/Admin/Cond.jsp");
			return mv;
		case Node:
			// case CondType.FLRole:
			cond.setMyPK(Integer.toString(BP.DA.DBAccess.GenerOID())); // cond.NodeID
																		// + "_"
																		// +
																		// cond.FK_Node
																		// + "_"
																		// +
																		// cond.FK_Attr
																		// + "_"
																		// +
																		// cond.OperatorValue;
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			mv.addObject("MyPK", cond.getMyPK());
			mv.addObject("FK_Flow", cond.getFK_Flow());
			mv.addObject("FK_Node", cond.getFK_Node());
			mv.addObject("FK_MainNode", cond.getNodeID());
			mv.addObject("FK_Attr", cond.getFK_Attr());
			mv.addObject("CondType", HisCondType);
			mv.addObject("ToNodeID", ToNodeID);
			mv.addObject("tb", tb);
			mv.addObject("success", "保存成功!");
			mv.setViewName("redirect:" + "/WF/Admin/Cond.jsp");
			return mv;
			// response.sendRedirect("Cond.jsp?MyPK=" + cond.getMyPK() +
			// "&FK_Flow=" + cond.getFK_Flow() + "&FK_Node=" + cond.getFK_Node()
			// + "&FK_MainNode=" + cond.getNodeID() + "&CondType=" +
			// cond.getHisCondType().getValue() + "&FK_Attr=" +
			// cond.getFK_Attr() + "&ToNodeID=" + ToNodeID);
		case Dir:
			// cond.MyPK = cond.NodeID +"_"+
			// this.Request.QueryString["ToNodeID"]+"_" + cond.FK_Node + "_" +
			// cond.FK_Attr + "_" + cond.OperatorValue;
			cond.setMyPK(Integer.toString(BP.DA.DBAccess.GenerOID())); // cond.NodeID
																		// + "_"
																		// +
																		// cond.FK_Node
																		// + "_"
																		// +
																		// cond.FK_Attr
																		// + "_"
																		// +
																		// cond.OperatorValue;
			cond.setToNodeID(Integer.parseInt(ToNodeID));
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			mv.addObject("MyPK", cond.getMyPK());
			mv.addObject("FK_Flow", cond.getFK_Flow());
			mv.addObject("FK_Node", cond.getFK_Node());
			mv.addObject("FK_MainNode", cond.getNodeID());
			mv.addObject("FK_Attr", cond.getFK_Attr());
			mv.addObject("CondType", HisCondType);
			mv.addObject("tb", tb);
			mv.addObject("ToNodeID", ToNodeID);
			mv.addObject("success", "保存成功!");
			mv.setViewName("redirect:" + "/WF/Admin/Cond.jsp");
			return mv;
			// response.sendRedirect("Cond.jsp?MyPK=" + cond.getMyPK() +
			// "&FK_Flow=" + cond.getFK_Flow() + "&FK_Node=" + cond.getFK_Node()
			// + "&FK_MainNode=" + cond.getNodeID() + "&CondType=" +
			// cond.getHisCondType().getValue() + "&FK_Attr=" +
			// cond.getFK_Attr() + "&ToNodeID=" + ToNodeID);
		default:
			try {
				throw new Exception("未设计的情况。");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// response.sendRedirect(basePath+"WF/Admin/Cond.jsp?MyPK="+cond.getMyPK()+"&FK_Flow="+cond.getFK_Flow()
		// +"&FK_Node="+cond.getFK_Node()+"&FK_MainNode="+cond.getNodeID()+"&FK_Attr="+cond.getFK_Attr()
		// +"&CondType="+HisCondType+"&tb="+tb+"&ToNodeID="+ToNodeID+"&success=保存成功！");
		mv.addObject("MyPK", cond.getMyPK());
		mv.addObject("FK_Flow", cond.getFK_Flow());
		mv.addObject("FK_Node", cond.getFK_Node());
		mv.addObject("FK_MainNode", cond.getNodeID());
		mv.addObject("FK_Attr", cond.getFK_Attr());
		mv.addObject("CondType", HisCondType);
		mv.addObject("tb", tb);
		mv.addObject("ToNodeID", ToNodeID);
		mv.addObject("success", "保存成功!");
		mv.setViewName("redirect:" + "/WF/Admin/Cond.jsp");
		return mv;
	}
}
