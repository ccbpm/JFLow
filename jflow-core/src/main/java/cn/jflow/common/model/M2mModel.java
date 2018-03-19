package cn.jflow.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.M2M;
import BP.Sys.M2MType;
import BP.Sys.MapM2M;
import BP.Tools.StringHelper;
import cn.jflow.system.ui.core.CheckBox;
/**
 * 多对多业务数据
 * @author x_zp
 * 20150116
 */
public class M2mModel extends BaseModel{
	
	public StringBuilder Pub1 = new StringBuilder();
	
	public boolean visible = true;

	public M2mModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public String getVisible() {
		if(!visible)
			return "style=\"display: none;\"";
		return "";
	}
	
	public final long getOID()
	{
		return Long.parseLong(get_request().getParameter("OID"));
	}
	public final String getIsOpen()
	{
		return get_request().getParameter("IsOpen");
	}
	public final String getIsEdit()
	{
		return get_request().getParameter("IsEdit");
	}
	public final String getFK_MapExt()
	{
		return get_request().getParameter("FK_MapExt");
	}
	public final String getFK_MapData()
	{
		return get_request().getParameter("FK_MapData");
	}
	public final String getNoOfObj()
	{
		return get_request().getParameter("NoOfObj");
	}
	public final void loadData()
	{
		//this.Page.RegisterClientScriptBlock("s",
		//    "<link href='../Comm/Style/Table" + BP.Web.WebUser.Style + ".css' rel='stylesheet' type='text/css' />");
		MapM2M mapM2M = new MapM2M(this.getFK_MapData(), this.getNoOfObj());
		if (mapM2M.getHisM2MType() == M2MType.M2MM)
		{
			String url = "M2MM.jsp?FK_MapData="+this.getFK_MapData() + "&NoOfObj=" + this.getNoOfObj() + "&IsOpen=" + this.getIsOpen() + "&OID=" + this.getOID();
			try {
				get_response().sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		M2M m2m = new M2M();
		m2m.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getOID() + "_");
		m2m.RetrieveFromDBSources();
		DataTable dtGroup = new DataTable();
		if (mapM2M.getDBOfGroups().length() > 5)
		{
			dtGroup = BP.DA.DBAccess.RunSQLReturnTable(mapM2M.getDBOfGroupsRun());
		}
		else
		{
			dtGroup.Columns.Add("No", String.class);
			dtGroup.Columns.Add("Name", String.class);
			DataRow dr = dtGroup.NewRow();
			dr.setValue("No", "01");
			dr.setValue("Name", "全部选择");
			dtGroup.Rows.add(dr);
		}

 		DataTable dtObj = BP.DA.DBAccess.RunSQLReturnTable(mapM2M.getDBOfObjsRun());
		if (dtObj.Columns.size() == 2)
		{
			dtObj.Columns.Add("Group", String.class);
			for (DataRow dr : dtObj.Rows)
			{
				dr.setValue("Group","01");
			}
		}

		boolean isInsert = mapM2M.getIsInsert();
		boolean isDelete = mapM2M.getIsDelete();

		if ((isDelete || isInsert) && StringHelper.isNullOrEmpty(this.getIsOpen()) == false)
		{
			//this.Button1.setVisible(true);
			visible = true;
		}

		appendPub(AddTable(" width='100%' border=0 "));
		for (DataRow drGroup : dtGroup.Rows)
		{
			String ctlIDs = "";
			String groupNo = drGroup.getValue(0).toString();
			String minOrMax = "Min";
			//String minOrMax = this.getIsPostBack() ? "Min" : "Max";

			String cbsID = "CBs_" + drGroup.getValue(0).toString();
			//增加全部选择.
			appendPub(AddTR());
			CheckBox cbx = null;
			if (mapM2M.getIsCheckAll() == true)
			{
				cbx = new CheckBox();
				cbx.setId(cbsID);
				cbx.setText(drGroup.getValue(1).toString());
				appendPub("<TD class=Title onDblClick=\"GroupBarClick('" + cbx.getId() + "')\">");
				appendPub("<div class=ckbgroup style='float:left'>");
				appendPub(cbx.toString());
				appendPub("</div>");
			}
			else
			{
				appendPub("<TD class=Title >");
				appendPub("<div class=ckbgroup style='float:left'>");
				appendPub("<img onclick=\"GroupBarClick('" + cbsID + "')\" src='../Img/" + minOrMax + ".gif' id='I" + cbsID + "' alt = '" + minOrMax + "' />" + drGroup.getValue(1).toString());
				appendPub("</div>");
			}


			appendPub("<div style='float:right'><img onclick=\"GroupBarClick('" + cbsID + "')\" src='../Img/" + minOrMax + ".gif' id='I" + cbsID + "' alt = '" + minOrMax + "' /></div>");
			appendPub("</TD>");
			appendPub(AddTREnd());

			appendPub(AddTR("ID='TR" + cbsID + "' style='display:none'"));

			appendPub(AddTDBegin("nowarp=false"));
			appendPub("<table width=100% border=0 >");
			int colIdx = -1;
			for (DataRow drObj : dtObj.Rows)
			{
				String no = drObj.getValue(0).toString();
				String name = drObj.getValue(1).toString();
				String group = drObj.getValue(2).toString();
				if (!group.trim().equals(groupNo.trim()))
				{
					continue;
				}

				colIdx++;
				if (colIdx == 0)
				{
					appendPub("<TR>");
				}

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + no);
				ctlIDs += cb.getId() + ",";
				cb.attributes.put("onclick", "isChange=true;");
				cb.setText(name);
				cb.setChecked(m2m.getVals().contains("," + no + ","));
				if (cb.getChecked())
				{
					cb.setText("<font color=green>" + cb.getText() + "</font>");
				}
				appendPub("<TD>");
				appendPub(cb.toString());
				appendPub("</TD>");

				if (mapM2M.getCols() - 1 == colIdx)
				{
					appendPub("</TR>");
					colIdx = -1;
				}
			}
			if (mapM2M.getIsCheckAll() == true)
			{
				cbx.attributes.put("onclick", "SetSelected(this,'" + ctlIDs + "')");
			}

			if (colIdx != -1)
			{
				while (colIdx != mapM2M.getCols() - 1)
				{
					colIdx++;
					appendPub("<TD ></TD>");
				}
				appendPub("</TR>");
			}
			appendPub(AddTableEnd());

			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}

		// region 处理未分组的情况.
		boolean isHaveUnGroup = false;
		for (DataRow drObj : dtObj.Rows)
		{
			String group = drObj.getValue(2).toString();
			isHaveUnGroup = true;
			for (DataRow drGroup : dtGroup.Rows)
			{
				String groupNo = drGroup.getValue(0).toString();
				if (group.equals(groupNo))
				{
					isHaveUnGroup = false;
					break;
				}
			}
			if (isHaveUnGroup == false)
			{
				continue;
			}
		}

		if (isHaveUnGroup == true)
		{
			appendPub(AddTR());
			appendPub(AddTDBigDocBegain()); // ("nowarp=true");
			appendPub(AddTable());
			int colIdx = -1;
			String ctlIDs = "";
			for (DataRow drObj : dtObj.Rows)
			{
				String group = drObj.getValue(2).toString();
				isHaveUnGroup = true;
				for (DataRow drGroup : dtGroup.Rows)
				{
					String groupNo = drGroup.getValue(0).toString();
					if (!group.equals(groupNo))
					{
						isHaveUnGroup = true;
						break;
					}
				}

				if (isHaveUnGroup == false)
				{
					continue;
				}

				String no = drObj.getValue(0).toString();
				String name = drObj.getValue(1).toString();

				colIdx++;
				if (colIdx == 0)
				{
					appendPub(AddTR());
				}

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + no);
				ctlIDs += cb.getId() + ",";
				cb.setText(name + group);
				cb.setChecked(m2m.getVals().contains("," + no + ","));
				if (cb.getChecked())
				{
					cb.setText("<font color=green>" + cb.getText() + "</font>");
				}

				appendPub(AddTD(cb));

				if (mapM2M.getCols() - 1 == colIdx)
				{
					appendPub(AddTREnd());
					colIdx = -1;
				}
			}
			if (colIdx != -1)
			{
				while (colIdx != mapM2M.getCols() - 1)
				{
					colIdx++;
					appendPub(AddTD());
				}
				appendPub(AddTREnd());
			}
			appendPub(AddTableEnd());
			//cbx.Attributes("onclick") = "SetSelected(this,'" + ctlIDs + "')";
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}
		// endregion 处理未分组的情况.

		appendPub(AddTableEnd());
	}
	
	private void appendPub(String str){
		Pub1.append(str);
	}

}
