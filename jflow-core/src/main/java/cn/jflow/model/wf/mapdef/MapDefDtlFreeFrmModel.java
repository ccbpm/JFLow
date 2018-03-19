package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Tools.StringHelper;
import BP.WF.Glo;

public class MapDefDtlFreeFrmModel {
	private HttpServletRequest request;
	private HttpServletResponse response;

	public String fkMapData;
	public String fkMapDtl;

	private MapDtl dtl;

	public MapDtl getDtl() {
		return dtl;
	}

	public MapDefDtlFreeFrmModel(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;

		fkMapData = request.getParameter("FK_MapData");
		fkMapDtl = request.getParameter("FK_MapDtl");
	}

	public boolean load() {
		dtl = new MapDtl();
		dtl.setNo(fkMapDtl);
		if (dtl.RetrieveFromDBSources() == 0) {
			dtl.setFK_MapData(fkMapData);
			dtl.setName(fkMapData);
			dtl.Insert();
			dtl.IntMapAttrs();
		}
		String fk_node = request.getParameter("FK_Node");

		if (!StringHelper.isNullOrEmpty(fk_node)) {
			// 如果传递来了节点信息, 就是说明了流程表单的节点方案处理, 现在就要做如下判断.
			// * 1, 如果已经有了.
			//
			dtl.setNo(fkMapDtl + "_" + fk_node);
			if (dtl.RetrieveFromDBSources() == 0) {

				// 开始复制它的属性.
				MapAttrs attrs = new MapAttrs(fkMapDtl);

				// 让其直接保存.
				dtl.setFK_MapData("Temp");
				dtl.DirectInsert(); // 生成一个明细表属性的主表.

				// 循环保存字段.
				int idx = 0;
				for (MapAttr item : attrs.ToJavaList()) {
					item.setFK_MapData(fkMapDtl + "_" + fk_node);
					item.setMyPK(item.getFK_MapData() + "_" + item.getKeyOfEn());
					item.Save();
					idx++;
					item.setIdx(idx);
					item.DirectUpdate();
				}

				MapData md = new MapData();
				md.setNo("Temp");
				if (md.getIsExits() == false) {
					md.setName("为权限方案设置的临时的数据");
					md.Insert();
				}
			}
			try {
				response.sendRedirect(Glo.getCCFlowAppPath()
						+ "WF/MapDef/MapDefDtlFreeFrm.jsp?FK_MapDtl=" + dtl.getNo() + "&FK_MapData=Temp");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return false;
		} else {
			return true;
		}
	}
}
