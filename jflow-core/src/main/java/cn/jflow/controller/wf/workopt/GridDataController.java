package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Work;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodes;

@Controller
@RequestMapping("/WF/WorkOpt")
public class GridDataController extends BaseController {

	@RequestMapping(value = "/GridData", method = RequestMethod.GET)
	public void GridData(HttpServletRequest request,
			HttpServletResponse response) {
		String WorkID = String.valueOf(getWorkID());
		String FK_Flow = getFK_Flow();
		String FK_Node = String.valueOf(getFK_Node());
		String doType = getDoType();

		String jsonResult = "";
		if (doType.equals("0")) {
			jsonResult = GetDtlCount(WorkID, FK_Flow, FK_Node);
		} else if (doType.equals("1")) {

			String getName = request.getParameter("Name");
			if (!StringHelper.isNullOrEmpty(getName)) {
				jsonResult = GetMainPage(WorkID, FK_Flow, FK_Node, getName);
			}
		} else if (doType.equals("2")) {
			jsonResult = GetDtlCountFlow(FK_Flow, FK_Node);
		} else if (doType.equals("3")) {
			String childName = request.getParameter("ChildName");
			jsonResult = GetChildDtlCount(FK_Flow, FK_Node);
		} else if (doType.equals("4")) {
			String getType = request.getParameter("GetType");
			// string name = context.Request.QueryString["Name"];
			jsonResult = GetDataByType(FK_Flow, FK_Node, WorkID, getType);
		} else if (doType.equals("5")) // 获取所有的图片信息
		{

			jsonResult = GetFlowPhoto(WorkID, FK_Flow, FK_Node);
		} else if (doType.equals("6")) // 获取所有的图片信息
		{
			jsonResult = GetPhoto(WorkID, FK_Flow, FK_Node);
		}
		try {
			wirteMsg(response, jsonResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// context.Response.Clear();
		// context.Response.ContentType = "text/plain";
		// context.Response.Write("Hello World");
	}

	private String GetFlowPhoto(String workID, String flow, String node) {

		String json = null;

		node = "ND" + node;

		String sql = String
				.format("select EnPk,ImgPath from Sys_FrmImg where FK_MapData ='%1$s' and ImgPath is not null UNION ALL  select v1.EnPK ,v2.Tag1 as ImgPath  from Sys_FrmImg v1 left join Sys_FrmEleDB v2 on v1.MyPK = v2.RefPKVal  where v1.FK_MapData='%1$s' and EleID='%2$s'",
						node, workID);

		DataTable table = DBAccess.RunSQLReturnTable(sql);

		ArrayList<PhotoEntity> photoEntities = new ArrayList<PhotoEntity>();
		if (table.Rows.size() > 0) {
			for (DataRow row : table.Rows) {
				PhotoEntity tempVar = new PhotoEntity();
				tempVar.setName(row.getValue(0).toString());
				tempVar.setValue(row.getValue(1).toString());
				photoEntities.add(tempVar);
			}
		}

		json = getSerializeObject(photoEntities);

		return json;

	}

	private String GetPhoto(String workID, String fk_flow, String fk_node) {
		String json = null;
		FrmNodes fns = new FrmNodes(fk_flow, Integer.parseInt(fk_node));

		ArrayList<PhotoEntity> photoEntities = new ArrayList<PhotoEntity>();
		for (FrmNode fn : fns.ToJavaList()) {
			String sql = String
					.format("select EnPk,ImgPath from Sys_FrmImg where FK_MapData ='%1$s' and ImgPath is not null UNION ALL  select v1.EnPK ,v2.Tag1 as ImgPath  from Sys_FrmImg v1 left join Sys_FrmEleDB v2 on v1.MyPK = v2.RefPKVal  where v1.FK_MapData='%1$s' and EleID='%2$s'",
							fn.getFK_Frm(), workID);

			DataTable table = DBAccess.RunSQLReturnTable(sql);

			if (table.Rows.size() > 0) {
				for (DataRow row : table.Rows) {
					PhotoEntity tempVar = new PhotoEntity();
					tempVar.setName(row.getValue(0).toString());
					tempVar.setValue(row.getValue(1).toString());
					photoEntities.add(tempVar);
				}
			}
		}

		json = getSerializeObject(photoEntities);

		return json;

	}

	private String GetChildDtlCount(String fk_flow, String fk_node) {
		FrmNodes fns = new FrmNodes(fk_flow, Integer.parseInt(fk_node));
		ArrayList<TempEntity> list = new ArrayList<TempEntity>();
		for (FrmNode fn : fns.ToJavaList()) {
			MapDtls mdtls = new MapDtls(fn.getFK_Frm());

			for (MapDtl single : mdtls.ToJavaList()) {
				TempEntity tempVar = new TempEntity();
				tempVar.setName(single.getPTable());
				list.add(tempVar);
			}
		}
		return getSerializeObject(list);
	}

	private String GetDataByType(String fk_flow, String fk_node, String workID,
			String getType) {
		FrmNodes fns = new FrmNodes(fk_flow, Integer.parseInt(fk_node));

		String result = "{";
		if (!getType.equals("MainPage")) {
			for (FrmNode fn : fns.ToJavaList()) {
				MapDtls mdtls = new MapDtls(fn.getFK_Frm());

				for (MapDtl dtl : mdtls.ToJavaList()) {
					if (dtl.getPTable().equals(getType)) {

						GEDtls ens = new GEDtls(dtl.getNo());
						ens.Retrieve(GEDtlAttr.RefPK, workID);
						result += "\""
								+ dtl.getPTable()
								+ "\":"
								+ getSerializeObject(ens
										.ToDataTableField()) + "}";
						break;
					}
				}
			}
		} else {
			for (FrmNode fn : fns.ToJavaList()) {
				GEEntity ge = new GEEntity(fn.getFK_Frm(), workID);
				String tempJson = getSerializeObject(ge.getRow());

				tempJson = StringHelper.trimStart(tempJson, '{');
				tempJson = StringHelper.trimEnd(tempJson, '}');

				result += tempJson + ",";
			}
			result = StringHelper.trimEnd(result, ',') + "}";
		}
		// foreach (FrmNode fn in fns)
		// {
		// if (fn.FK_Frm == name)
		// {
		// GEEntity ge = new GEEntity(fn.FK_Frm, workID);
		// if (getType == "MainPage")
		// {
		// result = getSerializeObject(ge.Row);
		// }
		// else
		// {
		// result = getSerializeObject(ge.Row);
		// result = result.substing(0, result.Length - 1);
		// MapDtls mdtls = new MapDtls(fn.FK_Frm);

		// foreach (MapDtl dtl in mdtls)
		// {
		// if (dtl.PTable.Equals(getType))
		// {

		// GEDtls ens = new GEDtls(dtl.No);
		// ens.Retrieve(GEDtlAttr.RefPK, workID);
		// result += ",\"" + dtl.PTable + "\":" +
		// getSerializeObject(ens.ToDataTableField()) + "}";
		// break;
		// }
		// }
		// }
		// break;
		// }
		// }
		return result;
	}

	private String GetDtlCountFlow(String flow, String node) {
		FrmNodes fns = new FrmNodes(flow, Integer.parseInt(node));
		java.util.ArrayList<TempEntity> list = new java.util.ArrayList<TempEntity>();
		for (FrmNode fn : fns.ToJavaList()) {
			TempEntity tempVar = new TempEntity();
			tempVar.setName(fn.getFK_Frm());
			list.add(tempVar);
		}
		return getSerializeObject(list);
	}

	private String GetDtlCount(String workID, String flow, String node) {
		Flow myFlow = new Flow(flow);

		String pTable = myFlow.getPTable();

		if (StringHelper.isNullOrEmpty(pTable)) {
			pTable = "ND" + Integer.parseInt(flow) + "Rpt";
		}

		Node nd = new Node(node);
		Work wk = nd.getHisWork();
		wk.setOID(Integer.parseInt(workID));
		wk.Retrieve();
		wk.ResetDefaultVal();

		GEEntity ndxxRpt = new GEEntity(pTable);
		ndxxRpt.setPKVal(workID);
		ndxxRpt.Retrieve();
		ndxxRpt.Copy(wk);

		// 把数据赋值给wk.
		wk.setRow(ndxxRpt.getRow());

		String jsonData = null;

		// 执行序列化
		// string jsonData = getSerializeObject(wk.Row);

		// jsonData = jsonData.substing(1, jsonData.Length - 2);
		// 加入他的明细表.
		java.util.ArrayList<Entities> al = wk.GetDtlsDatasOfList();

		java.util.ArrayList<TempEntity> tempName = new java.util.ArrayList<TempEntity>();

		for (Entities singleEntities : al) {
			TempEntity tempVar = new TempEntity();
			tempVar.setName(singleEntities.getGetNewEntity().toString());
			tempName.add(tempVar);
		}

		return getSerializeObject(tempName);
		// if (al.Count > 1)
		// {
		// foreach (Entities ens in al)
		// {
		// string dtlJson = getSerializeObject(ens.ToDataTableField());

		// var index = al.IndexOf(ens);
		// jsonData += ",\"jsonDtl" + index + "\":" + dtlJson;
		// }
		// }
		// else
		// {
		// jsonData = getSerializeObject(wk.Row);

		// jsonData = jsonData.substing(0, jsonData.Length - 1);

		// jsonData += ",\"jsonDtl\":" +
		// getSerializeObject(al[0].ToDataTableField());

		// jsonData += "}";
		// }
	}

	private String GetMainPage(String workID, String flow, String node,
			String name) {
		String jsonData = "{";

		Flow myFlow = new Flow(flow);

		String pTable = myFlow.getPTable();

		if (StringHelper.isNullOrEmpty(pTable)) {
			pTable = "ND" + Integer.parseInt(flow) + "Rpt";
		}

		Node nd = new Node(node);
		Work wk = nd.getHisWork();
		wk.setOID(Integer.parseInt(workID));
		wk.Retrieve();
		wk.ResetDefaultVal();

		GEEntity ndxxRpt = new GEEntity(pTable);
		ndxxRpt.setPKVal(workID);
		ndxxRpt.Retrieve();
		ndxxRpt.Copy(wk);

		// 执行序列化
		// string jsonData = getSerializeObject(wk.Row);

		// jsonData = jsonData.substing(1, jsonData.Length - 2);
		// 加入他的明细表.
		java.util.ArrayList<Entities> al = wk.GetDtlsDatasOfList();
		if (!name.equals("MainPage")) {

			for (Entities singlEntities : al) {
				if (singlEntities.getGetNewEntity().toString().equals(name)) {
					jsonData += "\"jsonDtl\":"
							+ getSerializeObject(singlEntities
									.ToDataTableField()) + "}";
					break;
				}
			}
		} else {
			// 把数据赋值给wk.
			wk.setRow(ndxxRpt.getRow());
			String tempJson = getSerializeObject(wk.getRow());
			jsonData = tempJson;
		}
		return jsonData;
	}

	public final boolean getIsReusable() {
		return false;
	}
	
	public String getSerializeObject(Object obj) {
		// 返回結果
		String jsonStr = null;
		// 判空
		if (obj == null) {
			return "{}";
		}

		// 判断是否是list
		if (obj instanceof Collection || obj instanceof Object[]) {
			jsonStr = JSONArray.fromObject(obj).toString();
		} else {
			jsonStr = JSONObject.fromObject(obj).toString();
		}
		return jsonStr;
	}
}

class TempEntity {
	private String privateName;

	public final String getName() {
		return privateName;
	}

	public final void setName(String value) {
		privateName = value;
	}
}

class PhotoEntity {
	private String privateName;

	public final String getName() {
		return privateName;
	}

	public final void setName(String value) {
		privateName = value;
	}

	private String privateValue;

	public final String getValue() {
		return privateValue;
	}

	public final void setValue(String value) {
		privateValue = value;
	}

}