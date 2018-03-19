package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.Web.WebUser;

public class StartModel extends BaseModel {

	public StringBuilder Pub1 = null;

	public StartModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub1 = new StringBuilder();
	}

	public void init() {
		Pub1.append("<table border=1px align=center width='100%'>");
		Pub1.append("<Caption >发起列表</Caption>");
		Pub1.append("<tr>");
		Pub1.append("<TH class='Title' nowrap=\"nowrap\" width=\"5%\">序</TH>");
		Pub1.append("<TH class='Title' nowrap=\"nowrap\" width=\"30%\">类别</TH>");
		Pub1.append("<TH class='Title' nowrap=\"nowrap\" width=\"65%\">流程</TH>");
		Pub1.append("</tr>");

		DataTable dt = Dev2Interface.DB_GenerCanStartFlowsOfDataTable(WebUser
				.getNo());
		if (null != dt) {
			int i = 0;
			for (DataRow dr : dt.Rows) {
				i++;
				Pub1.append("<tr>");
				Pub1.append("<td class='Idx'>" + i + "</td>");
				Pub1.append("<td>" + dr.getValue("FK_FlowSortText") + "</td>");
				Pub1.append("<td class=\"TTD\">");
				Pub1.append("<a href=\"javascript:WinOpen('"
						+ Glo.getCCFlowAppPath() + "WF/MyFlow.htm?FK_Flow="
						+ dr.getValue("No") + "&FK_Node="
						+ dr.getValue("No").toString()
						+ "01&WorkID=0&FID=0&IsToobar=1')" + "<img src='"
						+ Glo.getCCFlowAppPath()
						+ "WF/Img/WFState/Runing.png' class='Icon' />"
						+ dr.getValue("Name").toString());
				Pub1.append("</a>");
				Pub1.append("</td>");
				Pub1.append("</tr>");
			}
			Pub1.append("</table>");
		}
	}
}
